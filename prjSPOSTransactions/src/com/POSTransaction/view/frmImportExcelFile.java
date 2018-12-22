/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSTransaction.view;

import com.POSGlobal.controller.clsGlobalVarClass;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.sql.ResultSet;
import java.util.Date;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class frmImportExcelFile extends javax.swing.JFrame
{

    private String fileName;

    public frmImportExcelFile()
    {
	initComponents();
	lblUserCode.setText(clsGlobalVarClass.gUserCode);
	lblPosName.setText(clsGlobalVarClass.gPOSName);
	lblDate.setText(clsGlobalVarClass.gPOSDateToDisplay);
	lblModuleName1.setText(clsGlobalVarClass.gSelectedModule);
    }

    private void funResetFields()
    {
	txtFileName.setText("");
    }

    private boolean funCheckClientCode()
    {
	boolean flgClientCode = false;

	return flgClientCode;
    }

    private void funImportExcel()
    {
	//boolean flgClientCode=funCheckClientCode();

	btnImportFile.setEnabled(false);
	btnReset.setEnabled(false);
	btnClose.setEnabled(false);
	btnBrowse.setEnabled(false);
	StringBuilder sb = new StringBuilder(fileName);
	String fileExtension = sb.substring(sb.indexOf(".") + 1, sb.length()).toString();
	if (!fileExtension.equals("xls"))
	{
	    JOptionPane.showMessageDialog(this, "Invalid File, Please Import .xls File");
	    return;
	}

	if (cmbMaster.getSelectedItem().toString().equalsIgnoreCase("Item"))
	{
	    if (funImportMasters())
	    {
		JOptionPane.showMessageDialog(this, "Data Imported Successfully");
	    }
	}
	else
	{
	    if (funImportCustomerMaster())
	    {
		JOptionPane.showMessageDialog(this, "Data Imported Successfully");
	    }
	}
	btnImportFile.setEnabled(true);
	btnReset.setEnabled(true);
	btnClose.setEnabled(true);
	btnBrowse.setEnabled(true);
	lblMessage.setText("");
    }

    private void funBrowseFile()
    {
	try
	{
	    JFileChooser jfc = new JFileChooser();
	    if (jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
	    {
		File tempFile = jfc.getSelectedFile();
		String imagePath = tempFile.getAbsolutePath();
		fileName = imagePath.substring(imagePath.lastIndexOf("/") + 1, imagePath.length());
		txtFileName.setText(tempFile.getAbsolutePath());
		System.out.println(fileName);
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private boolean funReadExcelFile(String inputFile)
    {
	boolean flgResult = false;
	String query = "";
	File inputWorkbook = new File(inputFile);
	Workbook w;
	try
	{
	    w = Workbook.getWorkbook(inputWorkbook);
	    // Get the first sheet
	    Sheet sheet = w.getSheet(0);
	    // Loop over first 10 column and lines
	    clsGlobalVarClass.dbMysql.execute("truncate table tblimportexcel");

	    for (int row = 1; row < sheet.getRows(); row++)
	    {
		query = "insert into tblimportexcel (strItemCode,strItemName,strShortName,strMenuHeadName,strSubMenuHeadName"
			+ ",strRevenueHead,strPOSName,strSubGroupName,strGroupName,strCostCenterName,strAreaName"
			+ ",dblTax,dblPurchaseRate,strExternalCode,strItemDetails,strItemType,strApplyDiscount"
			+ ",strStockInEnable,dblPriceSunday,dblPriceMonday,dblPriceTuesday,dblPriceWednesday"
			+ ",dblPriceThursday,dblPriceFriday,dblPriceSaturday,strCounterName,strUOM,strRecipeUOM,strRawMaterial"
			+ ",strHourlyPricing,tmeTimeFrom,tmeTimeTo) "
			+ "values(";
		for (int col = 0; col < sheet.getColumns(); col++)
		{
		    Cell cell = sheet.getCell(col, row);
		    CellType type = cell.getType();

		    String name = cell.getContents().trim();
		    //System.out.println(name+"\t"+col+"  "+row);//�
		    name = name.replaceAll("�", "");

		    if (col == 1)
		    {
			if (name.length() > 199)
			{
			    name = name.substring(0, 199);
			}
		    }

		    if (col == 28)
		    {
			if (name == null || name.isEmpty())
			{
			    name = "N";
			}
		    }

		    if (col == 29)
		    {
			if (name == null || name.isEmpty())
			{
			    name = "NO";
			}
		    }

		    if (col == 30)
		    {
			if (name == null || name.isEmpty())
			{
			    name = "HH:MM:S";
			}
		    }
		    if (col == 31)
		    {
			if (name == null || name.isEmpty())
			{
			    name = "HH:MM:S";
			}
		    }

		    if (col > 0)
		    {
			//query+=",'"+cell.getContents().trim()+"'";
			query += ",'" + name + "'";
		    }
		    else
		    {
			//query+="'"+cell.getContents().trim()+"'";
			query += "'" + name + "'";
		    }
		}
		query += ")";
		System.out.println(query);
		clsGlobalVarClass.dbMysql.execute(query);
	    }
	    flgResult = true;
	}
	catch (Exception e)
	{
	    JOptionPane.showMessageDialog(null, "Invalid Excel File");
	    e.printStackTrace();
	}

	return flgResult;
    }

    private boolean funImportMasters()
    {
	boolean flgImport = false;

	//funEmptyMasterTables();
	if (funCheckEmptyDB())
	{
	    if (funReadExcelFile(fileName))
	    {
		flgImport = funGenerateCode();
	    }
	}
	else
	{
	    JOptionPane.showMessageDialog(null, "Data is present in Database, This module reuires Blank Database.");
	}
	return flgImport;
    }

    private boolean funImportCustomerMaster()
    {
	boolean flgImport = false;
	if (funCheckEmptyDBForCustomer())
	{
	    if (funReadExcelAndInsertCustomerData(fileName))//funReadExcelForCustomer old function
	    {
		flgImport = funGenerateCustMaster();
		flgImport = true;
	    }
	    //flgImport = funGenerateCustMaster();
	}
	else
	{
	    JOptionPane.showMessageDialog(null, "Data is present in Database, This module reuires Blank Database.");
	}
	return flgImport;
    }

    private boolean funReadExcelAndInsertCustomerData(String inputFile)
    {
	boolean flgResult = false;
	String query = "";
	File inputWorkbook = new File(inputFile);
	Workbook w;
	try
	{
	    w = Workbook.getWorkbook(inputWorkbook);
	    // Get the first sheet
	    Sheet sheet = w.getSheet(0);
	    // Loop over first 10 column and lines

	    clsGlobalVarClass.dbMysql.execute("truncate table tblcustomermaster");
	    StringBuilder queryBuilder = new StringBuilder();
	    StringBuilder rowBuilder = new StringBuilder();

	    queryBuilder.setLength(0);
	    rowBuilder.setLength(0);

	    String sqlInsert = "INSERT INTO `tblcustomermaster` "
		    + "(`strCustomerCode`, `strCustomerName`, `strBuldingCode`, `strBuildingName`, `strStreetName`, `strLandmark`, `strArea`,"
		    + " `strCity`, `strState`, `intPinCode`, `longMobileNo`, `longAlternateMobileNo`, `strOfficeBuildingCode`, "
		    + "`strOfficeBuildingName`, `strOfficeStreetName`, `strOfficeLandmark`, `strOfficeArea`, `strOfficeCity`, "
		    + "`strOfficePinCode`, `strOfficeState`, `strOfficeNo`, `strUserCreated`, `strUserEdited`, `dteDateCreated`, "
		    + "`dteDateEdited`, `strDataPostFlag`, `strClientCode`, `strOfficeAddress`, `strExternalCode`, `strCustomerType`, "
		    + "`dteDOB`, `strGender`, `dteAnniversary`, `strEmailId`, `strCRMId`, `strCustAddress`) VALUES ";

	    queryBuilder.append(sqlInsert);

	    long lastNo = 1;
	    String propertCode = clsGlobalVarClass.gClientCode.substring(4);

	    System.out.println("sheet.getRows()->" + sheet.getRows());
	    Integer insertLimit = 500;
	    boolean isEOF = false;
	    for (int row = 1; row < sheet.getRows(); row++)
	    {
		Cell cell = sheet.getCell(0, row);
		String contents = cell.getContents().trim();
//                if (contents.isEmpty())
//                {
//                    break;
//                }

		//System.out.println("row==" + row);
		String customerCode = propertCode + "C" + String.format("%07d", lastNo++);

		if (row <= insertLimit)
		{
		    rowBuilder.setLength(0);
		    rowBuilder.append("(");
		    for (int col = 0; col < 36; col++)
		    {
			cell = sheet.getCell(col, row);
			CellType type = cell.getType();
			contents = cell.getContents().trim();
			contents = contents.replaceAll("\\s", " ");
			contents = contents.replaceAll("/", " ");
//                        contents = contents.replaceAll("\\", ",");
			contents = contents.replaceAll("'", " ");
			contents = contents.replaceAll("--", " ");
			contents = contents.replaceAll("\\W", " ");
			//contents = contents.replaceAll("\\", " ");

//                        if (col == 0)//cust code
//                        {
//                            rowBuilder.append("'" + customerCode + "',");
//                        }
//                        else 
			if (col == 21)//strUserCreated
			{
			    rowBuilder.append("'" + clsGlobalVarClass.gUserCode + "',");
			}
			else if (col == 22)//strUserEdited
			{
			    rowBuilder.append("'" + clsGlobalVarClass.gUserCode + "',");
			}
			else if (col == 23)//dteDateCreated
			{
			    rowBuilder.append("'" + clsGlobalVarClass.getCurrentDateTime() + "',");
			}
			else if (col == 24)//dteDateEdited
			{
			    rowBuilder.append("'" + clsGlobalVarClass.getCurrentDateTime() + "',");
			}
			else if (col == 25)//strDataPostFlag
			{
			    rowBuilder.append("'N',");
			}
			else if (col == 26)//strClientCode
			{
			    rowBuilder.append("'" + clsGlobalVarClass.gClientCode + "',");
			}
			else if (col == 35)//cust address
			{
			    rowBuilder.append("'" + contents + "'");
			}
			else
			{
			    rowBuilder.append("'" + contents + "',");
			}

		    }
		    rowBuilder.append("),");
		    //System.out.println("row builder->"+rowBuilder);
		    queryBuilder.append(rowBuilder.toString());

		    if (row == insertLimit)
		    {
			queryBuilder.deleteCharAt(queryBuilder.lastIndexOf(","));

//                        String filePath = System.getProperty("user.dir");
//                        File txt = new File(filePath + "/_" + insertLimit + ".txt");
//
//                        FileWriter fstream_Report = new FileWriter(txt);
//                        BufferedWriter bufferedWriter = new BufferedWriter(fstream_Report);
//                        
//                        bufferedWriter.write(queryBuilder.toString());
			clsGlobalVarClass.dbMysql.execute(queryBuilder.toString());

			insertLimit = insertLimit + 500;
			if (insertLimit > sheet.getRows())
			{
			    insertLimit = sheet.getRows();
			}

			queryBuilder.setLength(0);
			queryBuilder.append(sqlInsert);
		    }

		}
		else
		{
		    queryBuilder.deleteCharAt(queryBuilder.lastIndexOf(","));

		    // System.out.println("queryBuilder->"+queryBuilder);
		    clsGlobalVarClass.dbMysql.execute(queryBuilder.toString());

		    queryBuilder.setLength(0);
		    queryBuilder.append(sqlInsert);
		    rowBuilder.setLength(0);
		    rowBuilder.append("(");
		    for (int col = 0; col < 36; col++)
		    {
			cell = sheet.getCell(col, row);
			CellType type = cell.getType();
			contents = cell.getContents().trim();
			contents = contents.replaceAll("\\s", " ");
			contents = contents.replaceAll("/", ",");
			contents = contents.replaceAll("'", ",");
			//contents = contents.replaceAll("\\", " ");

//                          if (col == 0)//cust code
//                        {
//                            rowBuilder.append("'" + customerCode + "',");
//                        }
//                        else 
			if (col == 21)//strUserCreated
			{
			    rowBuilder.append("'" + clsGlobalVarClass.gUserCode + "',");
			}
			else if (col == 22)//strUserEdited
			{
			    rowBuilder.append("'" + clsGlobalVarClass.gUserCode + "',");
			}
			else if (col == 23)//dteDateCreated
			{
			    rowBuilder.append("'" + clsGlobalVarClass.getCurrentDateTime() + "',");
			}
			else if (col == 24)//dteDateEdited
			{
			    rowBuilder.append("'" + clsGlobalVarClass.getCurrentDateTime() + "',");
			}
			else if (col == 25)//strDataPostFlag
			{
			    rowBuilder.append("'N',");
			}
			else if (col == 26)//strClientCode
			{
			    rowBuilder.append("'" + clsGlobalVarClass.gClientCode + "',");
			}
			else if (col == 35)//cust address
			{
			    rowBuilder.append("'" + contents + "'");
			}
			else
			{
			    rowBuilder.append("'" + contents + "',");
			}

		    }
		    rowBuilder.append("),");
		    //System.out.println("row builder->"+rowBuilder);
		    queryBuilder.append(rowBuilder.toString());

		    insertLimit += insertLimit;
		}
	    }

//            if (queryBuilder.length() > 0)
//            {
//                queryBuilder.deleteCharAt(queryBuilder.lastIndexOf(","));
//
//                System.out.println("queryBuilder->" + queryBuilder);
//                clsGlobalVarClass.dbMysql.execute(queryBuilder.toString());
//            }
	    flgResult = true;
	}
	catch (Exception e)
	{
	    JOptionPane.showMessageDialog(null, "Invalid Excel File");
	    e.printStackTrace();
	}

	return flgResult;
    }

    private boolean funReadExcelForCustomer(String inputFile)
    {
	boolean flgResult = false;
	String query = "";
	File inputWorkbook = new File(inputFile);
	Workbook w;
	try
	{
	    w = Workbook.getWorkbook(inputWorkbook);
	    // Get the first sheet
	    Sheet sheet = w.getSheet(0);
	    // Loop over first 10 column and lines

	    clsGlobalVarClass.dbMysql.execute("truncate table tblimportexcel");
	    for (int row = 1; row < sheet.getRows(); row++)
	    {
		query = "insert into tblimportexcel (strItemName,strShortName,strMenuHeadName,strPOSName"
			+ ",strSubGroupName,strSubMenuHeadName,strGroupName,strCostCenterName,strAreaName"
			+ ",strCustName,strBuildName,strBuildingArea,strTelephoneNo,strEmail,strDOB)"
			+ " values('','','','','','','','',''";
		for (int col = 0; col < sheet.getColumns(); col++)
		{
		    Cell cell = sheet.getCell(col, row);
		    CellType type = cell.getType();
		    String newString = cell.getContents().trim();
		    if (col == 3)
		    {
			if (newString.contains(" "))
			{
			    int ind = newString.indexOf(" ");
			    StringBuilder sb = new StringBuilder(newString);
			    newString = sb.insert(ind, ",").toString();
			    newString = newString.replaceAll("\\s", "");
			    System.out.println("Char without space=" + newString);
			}
			if (newString.contains("/"))
			{
			    newString = newString.replace("/", ",");
			    System.out.println("Char without slash=" + newString);
			}
		    }
		    if (newString.contains("'"))
		    {
			newString = newString.replace("'", " ");
		    }
		    query += ",'" + newString + "'";
		}
		query += ")";
		System.out.println(query);
		clsGlobalVarClass.dbMysql.execute(query);
	    }
	    clsGlobalVarClass.dbMysql.execute("delete from tblimportexcel where strCustName=''");
	    flgResult = true;
	}
	catch (Exception e)
	{
	    JOptionPane.showMessageDialog(null, "Invalid Excel File");
	    e.printStackTrace();
	}

	return flgResult;
    }

    private boolean funGenerateCustomerCode()
    {
	boolean flgReturn = false;
	String query = "", code = "";
	long docNo = 0;
	try
	{
	    //clsGlobalVarClass.dbMysql.execute("truncate table tblgrouphd");
	    String sql = "select distinct(strCustName),strBuildCode,strBuildName,"
		    + "strTelephoneNo,strEmail,strDOB from tblimportexcel";
	    ResultSet rsCustomer = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rsCustomer.next())
	    {
		docNo++;
		code = "C" + String.format("%07d", docNo);
		String dob = rsCustomer.getString(6);
		if (dob.trim().length() == 0 || dob.equals("_") || dob.contains("-"))
		{
		    dob = "";
		}
		else
		{
		    String[] spDOB = dob.split("/");
		    dob = spDOB[2] + "-" + spDOB[0] + "-" + spDOB[1];
		}
		query = "insert into tblcustomermaster (strCustomerCode,strCustomerName,"
			+ "strBuldingCode,strBuildingName,longMobileNo,strUserCreated,"
			+ "strUserEdited,dteDateCreated,dteDateEdited,strClientCode,dteDOB,"
			+ "strEmailId) "
			+ "values('" + code + "','" + rsCustomer.getString(1) + "','" + rsCustomer.getString(2) + "',"
			+ "'" + rsCustomer.getString(3) + "','" + rsCustomer.getString(4) + "',"
			+ "'" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "',"
			+ "'" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "',"
			+ "'" + clsGlobalVarClass.gClientCode + "','" + dob + "','" + rsCustomer.getString(5) + "')";
		int insert = clsGlobalVarClass.dbMysql.execute(query);
		if (insert == 1)
		{
		    query = "update tblimportexcel set strCustCode='" + code + "' "
			    + "where strCustName='" + rsCustomer.getString(1) + "'";
		    int update = clsGlobalVarClass.dbMysql.execute(query);
		}
	    }
	    rsCustomer.close();
	    flgReturn = true;
	}
	/*
         catch(com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException integrityEx)
         {
         flgReturn=false;
         System.out.println("Message="+integrityEx.getMessage()+"\t"+integrityEx.getErrorCode());
         if(integrityEx.getMessage().startsWith("Duplicate entry"))
         {
         JOptionPane.showMessageDialog(null,"Data Already Present");
         }
         }*/ catch (Exception e)
	{
	    //System.out.println("Message="+e.getMessage());
	    e.printStackTrace();
	}
	finally
	{
	    return flgReturn;
	}
    }

    private boolean funGenerateCustArea()
    {
	boolean flgReturn = false;
	String query = "", code = "";
	long docNo = 0;
	try
	{
	    String sql = "select distinct(strBuildingName) from tblcustomermaster";
	    ResultSet rsBuilding = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    String strZoneCode = "";
	    while (rsBuilding.next())
	    {
		docNo++;
		code = "B" + String.format("%07d", docNo);
		query = "insert into tblbuildingmaster (strBuildingCode,strBuildingName,"
			+ "strAddress,strUserCreated,strUserEdited,dteDateCreated,"
			+ "dteDateEdited,strClientCode,strZoneCode) "
			+ "values('" + code + "','" + rsBuilding.getString(1) + "','',"
			+ "'" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "',"
			+ "'" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "',"
			+ "'" + clsGlobalVarClass.gClientCode + "','" + strZoneCode + "')";

		int insert = clsGlobalVarClass.dbMysql.execute(query);
		if (insert == 1)
		{
		    query = "update tblcustomermaster set strBuldingCode='" + code + "' "
			    + "where strBuildingName='" + rsBuilding.getString(1) + "'";
		    int update = clsGlobalVarClass.dbMysql.execute(query);
		}
	    }
	    rsBuilding.close();
	    flgReturn = true;
	}
	/*
         catch(com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException integrityEx)
         {
         flgReturn=false;
         System.out.println("Message="+integrityEx.getMessage()+"\t"+integrityEx.getErrorCode());
         if(integrityEx.getMessage().startsWith("Duplicate entry"))
         {
         JOptionPane.showMessageDialog(null,"Data Already Present");
         }
         }*/ catch (Exception e)
	{
	    //System.out.println("Message="+e.getMessage());
	    e.printStackTrace();
	}
	finally
	{
	    return flgReturn;
	}
    }

    public boolean funCheckEmptyDBForCustomer()
    {
	boolean flgResult = false;
	int custCount = 0, buildingCount = 0;

	try
	{
	    String sql = "select count(strCustomerCode) from tblcustomermaster "
		    + "where strClientCode='" + clsGlobalVarClass.gClientCode + "'";
	    ResultSet rsCustomer = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    rsCustomer.next();
	    custCount = rsCustomer.getInt(1);
	    rsCustomer.close();

	    sql = "select count(strBuildingCode) from tblbuildingmaster "
		    + "where strClientCode='" + clsGlobalVarClass.gClientCode + "'";
	    ResultSet rsBuilding = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    rsBuilding.next();
	    buildingCount = rsBuilding.getInt(1);
	    rsBuilding.close();

	    if (custCount == 0 && buildingCount == 0)
	    {
		flgResult = true;
	    }

	}
	catch (Exception e)
	{
	    flgResult = false;
	    e.printStackTrace();
	}
	finally
	{
	    return flgResult;
	}
    }

    private boolean funGenerateCustMaster()
    {
	System.out.println("In Gen Cust Master");
	boolean flgReturn = false;
	funGenerateCustArea();
	//flgReturn = funGenerateCustomerCode();

	return flgReturn;
    }

    private int funEmptyMasterTables()
    {
	try
	{
	    String sql = "truncate table tblgrouphd";
	    clsGlobalVarClass.dbMysql.execute(sql);

	    sql = "truncate table tblsubgrouphd";
	    clsGlobalVarClass.dbMysql.execute(sql);

	    sql = "truncate table tblmenuhd";
	    clsGlobalVarClass.dbMysql.execute(sql);

	    sql = "truncate table tblsubmenuhead";
	    clsGlobalVarClass.dbMysql.execute(sql);

	    sql = "truncate table tblcostcentermaster";
	    clsGlobalVarClass.dbMysql.execute(sql);

	    sql = "truncate table tblareamaster";
	    clsGlobalVarClass.dbMysql.execute(sql);
	    sql = "update tblinternal set dblLastNo=0 where strTransactionType='Area'";
	    clsGlobalVarClass.dbMysql.execute(sql);

	    sql = "truncate table tblitemmaster";
	    clsGlobalVarClass.dbMysql.execute(sql);

	    sql = "truncate table tblcounterhd";
	    clsGlobalVarClass.dbMysql.execute(sql);

	    sql = "truncate table tblmenuitempricinghd";
	    clsGlobalVarClass.dbMysql.execute(sql);

	    sql = "truncate table tblmenuitempricingdtl";
	    clsGlobalVarClass.dbMysql.execute(sql);

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}

	return 1;
    }

    public boolean funCheckEmptyDB()
    {
	boolean flgResult = false;
	int groupCount = 0, subGroupCount = 0, itemMasterCount = 0, menuHeadCount = 0, subMenuHeadCount = 0, counterCount = 0;
	int costCenterCount = 0, menuItemPricingHd = 0, menuItemPricingDtl = 0;

	try
	{
//            String sql = "select count(strGroupCode) from tblgrouphd "
//                    + "where strClientCode='" + clsGlobalVarClass.gClientCode + "'";
//            ResultSet rsGroup = clsGlobalVarClass.dbMysql.executeResultSet(sql);
//            rsGroup.next();
//            groupCount = rsGroup.getInt(1);
//            rsGroup.close();
//
//            sql = "select count(strSubGroupCode) from tblsubgrouphd "
//                    + "where strClientCode='" + clsGlobalVarClass.gClientCode + "'";
//            ResultSet rsSubGroup = clsGlobalVarClass.dbMysql.executeResultSet(sql);
//            rsSubGroup.next();
//            subGroupCount = rsSubGroup.getInt(1);
//            rsSubGroup.close();
//
//            sql = "select count(strMenuCode) from tblmenuhd "
//                    + "where strClientCode='" + clsGlobalVarClass.gClientCode + "'";
//            ResultSet rsMenuHead = clsGlobalVarClass.dbMysql.executeResultSet(sql);
//            rsMenuHead.next();
//            menuHeadCount = rsMenuHead.getInt(1);
//            rsMenuHead.close();
//
//            sql = "select count(strSubMenuHeadCode) from tblsubmenuhead "
//                    + "where strClientCode='" + clsGlobalVarClass.gClientCode + "'";
//            ResultSet rsSubMenu = clsGlobalVarClass.dbMysql.executeResultSet(sql);
//            rsSubMenu.next();
//            subMenuHeadCount = rsSubMenu.getInt(1);
//            rsSubMenu.close();
//
//            sql = "select count(strCostCenterCode) from tblcostcentermaster "
//                    + "where strClientCode='" + clsGlobalVarClass.gClientCode + "'";
//            ResultSet rsCostCenter = clsGlobalVarClass.dbMysql.executeResultSet(sql);
//            rsCostCenter.next();
//            costCenterCount = rsCostCenter.getInt(1);
//            rsCostCenter.close();
//
//            sql = "select count(strAreaCode) from tblareamaster "
//                    + "where strClientCode='" + clsGlobalVarClass.gClientCode + "'";
//            ResultSet rsArea = clsGlobalVarClass.dbMysql.executeResultSet(sql);
//            rsArea.next();
//            rsArea.close();
//
//            sql = "select count(strItemCode) from tblitemmaster "
//                    + "where strClientCode='" + clsGlobalVarClass.gClientCode + "'";
//            ResultSet rsItemMaster = clsGlobalVarClass.dbMysql.executeResultSet(sql);
//            rsItemMaster.next();
//            itemMasterCount = rsItemMaster.getInt(1);
//            rsItemMaster.close();
//
//            sql = "select count(strCounterCode) from tblcounterhd "
//                    + "where strClientCode='" + clsGlobalVarClass.gClientCode + "'";
//            ResultSet rsCounter = clsGlobalVarClass.dbMysql.executeResultSet(sql);
//            rsCounter.next();
//            counterCount = rsCounter.getInt(1);
//            rsCounter.close();
//
//            sql = "select count(strMenuCode) from tblmenuitempricinghd";
//            ResultSet rsItemPriceHd = clsGlobalVarClass.dbMysql.executeResultSet(sql);
//            rsItemPriceHd.next();
//            menuItemPricingHd = rsItemPriceHd.getInt(1);
//            rsItemPriceHd.close();
//
//            sql = "select count(strItemCode) from tblmenuitempricingdtl";
//            ResultSet rsItemPriceDtl = clsGlobalVarClass.dbMysql.executeResultSet(sql);
//            rsItemPriceDtl.next();
//            menuItemPricingDtl = rsItemPriceDtl.getInt(1);
//            rsItemPriceDtl.close();
//
//            if (menuHeadCount == 0 && groupCount == 0 && subGroupCount == 0 && subMenuHeadCount == 0
//                    && itemMasterCount == 0 && costCenterCount == 0 && menuItemPricingHd == 0
//                    && menuItemPricingDtl == 0 && counterCount == 0)
//            {
//                flgResult = true;
//            }
	    flgResult = true;
	}
	catch (Exception e)
	{
	    flgResult = false;
	    e.printStackTrace();
	}
	finally
	{
	    return flgResult;
	}
    }

    public boolean funGenerateCode()
    {
	boolean flgReturn = false;
	if (cmbIndType.getSelectedItem().toString().equals("F&B"))
	{
	    funGeneratePOS();
	    funGenerateGroup();
	    funGenerateSubGroup();
	    funGenerateMenuHead();
	    funGenerateSubMenuHead();
	    funGenerateItemMaster();
	    funGenerateCostCenter();
	    funGenerateCounterMasterHd();
	    funGenerateCounterMasterDtl();
	    funGenerateAreaMaster();
	    funGenerateMenuItemPriceHD();
	    flgReturn = funGenerateMenuItemPriceDTL();
	}
	else
	{
	    funGenerateItemMasterForRetail();
	    funGenerateGroup();
	    funGenerateSubGroup();
	    funGenerateMenuHead();
	    funGenerateSubMenuHead();
	    funGenerateCostCenter();
	    funGenerateCounterMasterHd();
	    funGenerateCounterMasterDtl();
	    flgReturn = funGenerateAreaMaster();
	}
	return flgReturn;
    }

    private boolean funGenerateGroup()
    {
	boolean flgReturn = false;
	String query = "", code = "";
	long docNo = 0;
	try
	{
	    //clsGlobalVarClass.dbMysql.execute("truncate table tblgrouphd");
	    String sql = "select distinct(strGroupName) from tblimportexcel";
	    ResultSet rsGroup = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rsGroup.next())
	    {
		lblMessage.setText("");
		lblMessage.setText("Importing Group Master");
		String sqlNameCheck = " select a.strGroupCode from tblgrouphd a where a.strGroupName='" + rsGroup.getString(1) + "' ";
		ResultSet rsNameCheck = clsGlobalVarClass.dbMysql.executeResultSet(sqlNameCheck);
		if (!rsNameCheck.next())
		{
		    if (rsGroup.getString(1).trim().length() > 0)
		    {
			String docSql = " select ifnull(max(MID(a.strGroupCode,2,7)),'0' )as strGroupCode  from tblgrouphd a   ";
			ResultSet rsDocCode = clsGlobalVarClass.dbMysql.executeResultSet(docSql);

			if (rsDocCode.next())
			{
			    docNo = Long.parseLong(rsDocCode.getString(1)) + 1;
			    code = "G" + String.format("%07d", docNo);
			}
			else
			{
			    docNo++;
			    code = "G" + String.format("%07d", docNo);
			}
			query = "insert into tblgrouphd (strGroupCode,strGroupName,strUserCreated,"
				+ "strUserEdited,dteDateCreated,dteDateEdited,strClientCode)"
				+ "values('" + code + "','" + rsGroup.getString(1) + "','" + clsGlobalVarClass.gUserCode + "',"
				+ "'" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.getCurrentDateTime() + "',"
				+ "'" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.gClientCode + "')";
			int insert = clsGlobalVarClass.dbMysql.execute(query);
			if (insert == 1)
			{
			    query = "update tblimportexcel set strGroupCode='" + code + "' "
				    + "where strGroupName='" + rsGroup.getString(1) + "'";
			    int update = clsGlobalVarClass.dbMysql.execute(query);
			}
		    }

		}
		else
		{
		    code = rsNameCheck.getString(1);
		    query = "update tblimportexcel set strGroupCode='" + code + "' "
			    + "where strGroupName='" + rsGroup.getString(1) + "'";
		    int update = clsGlobalVarClass.dbMysql.execute(query);
		}

	    }
	    rsGroup.close();
	    flgReturn = true;
	}
	/*
         catch(com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException integrityEx)
         {
         flgReturn=false;
         System.out.println("Message="+integrityEx.getMessage()+"\t"+integrityEx.getErrorCode());
         if(integrityEx.getMessage().startsWith("Duplicate entry"))
         {
         JOptionPane.showMessageDialog(null,"Data Already Present");
         }
         }*/ catch (Exception e)
	{
	    //System.out.println("Message="+e.getMessage());
	    //e.printStackTrace();
	}
	finally
	{
	    return flgReturn;
	}
    }

    private boolean funGenerateSubGroup()
    {
	boolean flgReturn = false;
	String query = "", code = "";
	long docNo = 0;
	try
	{
	    //clsGlobalVarClass.dbMysql.execute("truncate table tblsubgrouphd");
	    String sql = "select distinct(strSubGroupName),strGroupCode from tblimportexcel";
	    ResultSet rsSubGroup = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rsSubGroup.next())
	    {
		lblMessage.setText("");
		lblMessage.setText("Importing Sub Group Master");
		String sqlNameCheck = " select a.strSubGroupCode from tblsubgrouphd a where a.strSubGroupName='" + rsSubGroup.getString(1) + "' ";
		ResultSet rsNameCheck = clsGlobalVarClass.dbMysql.executeResultSet(sqlNameCheck);
		if (!rsNameCheck.next())
		{
		    if (rsSubGroup.getString(1).trim().length() > 0)
		    {
			String docSql = " select ifnull(max(MID(a.strSubGroupCode,3,7)),'0' )as strSubGroupCode  from tblsubgrouphd a   ";
			ResultSet rsDocCode = clsGlobalVarClass.dbMysql.executeResultSet(docSql);

			if (rsDocCode.next())
			{
			    docNo = Long.parseLong(rsDocCode.getString(1)) + 1;
			    code = "SG" + String.format("%07d", docNo);
			    rsDocCode.close();
			}
			else
			{
			    docNo++;
			    code = "SG" + String.format("%07d", docNo);
			}

			query = "insert into tblsubgrouphd (strSubGroupCode,strSubGroupName,strGroupCode,"
				+ "strUserCreated,strUserEdited,dteDateCreated,dteDateEdited,strClientCode)"
				+ "values('" + code + "','" + rsSubGroup.getString(1) + "','" + rsSubGroup.getString(2) + "',"
				+ "'" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "',"
				+ "'" + clsGlobalVarClass.getCurrentDateTime() + "','"
				+ clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.gClientCode + "')";
			int insert = clsGlobalVarClass.dbMysql.execute(query);
			if (insert == 1)
			{
			    query = "update tblimportexcel set strSubGroupCode='" + code + "' "
				    + "where strSubGroupName='" + rsSubGroup.getString(1) + "'";
			    int update = clsGlobalVarClass.dbMysql.execute(query);
			}
		    }
		}
		else
		{
		    code = rsNameCheck.getString(1);
		    query = "update tblimportexcel set strSubGroupCode='" + code + "' "
			    + "where strSubGroupName='" + rsSubGroup.getString(1) + "'";
		    int update = clsGlobalVarClass.dbMysql.execute(query);
		}
		rsNameCheck.close();
	    }
	    rsSubGroup.close();
	    flgReturn = true;

	}
	catch (Exception e)
	{
	    flgReturn = false;
	    e.printStackTrace();
	}
	finally
	{
	    return flgReturn;
	}
    }

    private boolean funGenerateCostCenter()
    {
	boolean flgReturn = false;
	String query = "", code = "";
	long docNo = 0;
	try
	{
	    //clsGlobalVarClass.dbMysql.execute("truncate table tblcostcentermaster");
	    String sql = "select distinct(strCostCenterName) from tblimportexcel";
	    ResultSet rsCostCenter = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rsCostCenter.next())
	    {
		lblMessage.setText("");
		lblMessage.setText("Importing Cost Center Master");

		String sqlNameCheck = " select a.strCostCenterCode from tblCostCenterMaster a where a.strCostCenterName='" + rsCostCenter.getString(1) + "' ";
		ResultSet rsNameCheck = clsGlobalVarClass.dbMysql.executeResultSet(sqlNameCheck);
		if (!rsNameCheck.next())
		{
		    if (rsCostCenter.getString(1).trim().length() > 0)
		    {
			String docSql = " select ifnull(max(MID(a.strCostCenterCode,2,2)),'0' )as strCostCenterCode "
				+ " from tblCostCenterMaster a  ";
			ResultSet rsDocCode = clsGlobalVarClass.dbMysql.executeResultSet(docSql);

			if (rsDocCode.next())
			{
			    docNo = Long.parseLong(rsDocCode.getString(1)) + 1;
			    code = "C" + String.format("%02d", docNo);
			}
			else
			{
			    docNo++;
			    code = "C" + String.format("%02d", docNo);
			}

			query = "insert into tblCostCenterMaster (strCostCenterCode,strCostCenterName,strPrinterPort"
				+ ",strSecondaryPrinterPort,strUserCreated,strUserEdited,dteDateCreated,dteDateEdited"
				+ ",strClientCode,strDataPostFlag)"
				+ " values('" + code + "','" + rsCostCenter.getString(1) + "','','','" + clsGlobalVarClass.gUserCode + "',"
				+ "'" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.getCurrentDateTime() + "',"
				+ "'" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.gClientCode + "','N')";
			int insert = clsGlobalVarClass.dbMysql.execute(query);
			if (insert == 1)
			{
			    query = "update tblimportexcel set strCostCenterCode='" + code + "' "
				    + "where strCostCenterName='" + rsCostCenter.getString(1) + "'";
			    int update = clsGlobalVarClass.dbMysql.execute(query);
			}
		    }
		}
		else
		{
		    code = rsNameCheck.getString(1);
		    query = "update tblimportexcel set strCostCenterCode='" + code + "' "
			    + "where strCostCenterName='" + rsCostCenter.getString(1) + "'";
		    int update = clsGlobalVarClass.dbMysql.execute(query);
		}
	    }
	    rsCostCenter.close();
	    flgReturn = true;

	}
	catch (Exception e)
	{
	    flgReturn = false;
	    e.printStackTrace();
	}
	finally
	{
	    return flgReturn;
	}
    }

    private boolean funGenerateCounterMasterHd()
    {
	boolean flgReturn = false;
	String query = "", code = "";
	long docNo = 0;
	try
	{
	    //clsGlobalVarClass.dbMysql.execute("truncate table tblcostcentermaster");
	    String sql = "select distinct(strCounterName) from tblimportexcel";
	    ResultSet rsCounter = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rsCounter.next())
	    {
		lblMessage.setText("");
		lblMessage.setText("Importing Counter Master");
		if (rsCounter.getString(1).trim().length() > 0)
		{
		    docNo++;
		    code = "C" + String.format("%02d", docNo);
		    query = "insert into tblcounterhd (strCounterCode,strCounterName,strPOSCode,"
			    + "strUserCreated,strUserEdited,dteDateCreated,dteDateEdited,strClientCode,strDataPostFlag,strOperational)"
			    + " values('" + code + "','" + rsCounter.getString(1) + "','" + clsGlobalVarClass.gPOSCode + "','" + clsGlobalVarClass.gUserCode + "',"
			    + "'" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.getCurrentDateTime() + "',"
			    + "'" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.gClientCode + "','N','Yes')";
		    int insert = clsGlobalVarClass.dbMysql.execute(query);
		    if (insert == 1)
		    {
			query = "update tblimportexcel set strCounterCode='" + code + "' "
				+ "where strCounterName='" + rsCounter.getString(1) + "'";
			int update = clsGlobalVarClass.dbMysql.execute(query);
		    }
		}
	    }
	    rsCounter.close();
	    flgReturn = true;

	}
	catch (Exception e)
	{
	    flgReturn = false;
	    e.printStackTrace();
	}
	finally
	{
	    return flgReturn;
	}
    }

    private boolean funGenerateCounterMasterDtl()
    {
	boolean flgReturn = false;
	String query = "";
	try
	{
	    //clsGlobalVarClass.dbMysql.execute("truncate table tblcostcentermaster");
	    String sql = "select distinct(strMenuHeadCode),strCounterCode from tblimportexcel order by strCounterCode";
	    ResultSet rsCounter = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rsCounter.next())
	    {
		lblMessage.setText("");
		lblMessage.setText("Importing Counter Master");
		query = "insert into tblcounterdtl (strCounterCode,strMenuCode,strClientCode)"
			+ " values('" + rsCounter.getString(2) + "','" + rsCounter.getString(1) + "','" + clsGlobalVarClass.gClientCode + "')";
		int insert = clsGlobalVarClass.dbMysql.execute(query);
	    }
	    rsCounter.close();
	    flgReturn = true;

	}
	catch (Exception e)
	{
	    flgReturn = false;
	    e.printStackTrace();
	}
	finally
	{
	    return flgReturn;
	}
    }

    private boolean funGenerateMenuHead()
    {
	boolean flgReturn = false;
	String query = "", code = "";
	long docNo = 0;
	try
	{
	    //clsGlobalVarClass.dbMysql.execute("truncate table tblmenuhd");
	    String sql = "select distinct(strMenuHeadName) from tblimportexcel";
	    ResultSet rsMenuHead = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rsMenuHead.next())
	    {
		lblMessage.setText("");
		lblMessage.setText("Importing Menu Head");
		String sqlNameCheck = "select a.strMenuCode from tblmenuhd a where a.strMenuName='" + rsMenuHead.getString(1) + "'";
		ResultSet rsNameCheck = clsGlobalVarClass.dbMysql.executeResultSet(sqlNameCheck);
		if (!rsNameCheck.next())
		{
		    if (rsMenuHead.getString(1).trim().length() > 0)
		    {
			String docSql = " select ifnull(max(MID(a.strMenuCode,2,6)),'0' )as strMenuCode  from tblmenuhd a ";
			ResultSet rsDocCode = clsGlobalVarClass.dbMysql.executeResultSet(docSql);

			if (rsDocCode.next())
			{
			    docNo = Long.parseLong(rsDocCode.getString(1)) + 1;
			    code = "M" + String.format("%06d", docNo);;
			}
			else
			{
			    docNo++;
			    code = "M" + String.format("%06d", docNo);
			}
			rsDocCode.close();

			query = "insert into tblmenuhd (strMenuCode,strMenuName,strUserCreated,strUserEdited,"
				+ "dteDateCreated,dteDateEdited,strClientCode,strOperational,imgImage) "
				+ "values('" + code + "','" + rsMenuHead.getString(1) + "','" + clsGlobalVarClass.gUserCode + "'"
				+ ",'" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.getCurrentDateTime() + "'"
				+ ",'" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.gClientCode + "','Y','' )";
			int insert = clsGlobalVarClass.dbMysql.execute(query);
			if (insert == 1)
			{
			    query = "update tblimportexcel set strMenuHeadCode='" + code + "' "
				    + "where strMenuHeadName='" + rsMenuHead.getString(1) + "'";
			    int update = clsGlobalVarClass.dbMysql.execute(query);
			}
		    }
		    flgReturn = true;
		}
		else
		{
		    code = rsNameCheck.getString(1);
		    query = "update tblimportexcel set strMenuHeadCode='" + code + "' "
			    + "where strMenuHeadName='" + rsMenuHead.getString(1) + "'";
		    int update = clsGlobalVarClass.dbMysql.execute(query);
		}
		rsNameCheck.close();
	    }
	    rsMenuHead.close();

	}
	catch (Exception e)
	{
	    flgReturn = false;
	    e.printStackTrace();
	}
	finally
	{
	    return flgReturn;
	}
    }

    private boolean funGeneratePOS()
    {
	boolean flgReturn = false;
	String query = "", code = "";
	long docNo = 0;
	try
	{
	    //  clsGlobalVarClass.dbMysql.execute("truncate table tblposmaster");
	    String sql = "select distinct(strPOSName) from tblimportexcel";
	    ResultSet rsPOSMaster = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rsPOSMaster.next())
	    {
		lblMessage.setText("");
		lblMessage.setText("Importing POS Master");

		String posName = rsPOSMaster.getString(1);

		String sqlNameCheck = " select a.strPosCode from tblposmaster a where a.strPosName='" + posName + "' ";
		ResultSet rsNameCheck = clsGlobalVarClass.dbMysql.executeResultSet(sqlNameCheck);
		if (rsNameCheck.next())
		{
		    code = rsNameCheck.getString(1);
		    query = "update tblimportexcel set strPOSCode='" + code + "' "
			    + "where strPOSName='" + posName + "'";
		    int update = clsGlobalVarClass.dbMysql.execute(query);
		}
		else
		{
		    if (posName.length() > 0 && !posName.equalsIgnoreCase("All"))
		    {
			String docSql = " select ifnull(max(MID(a.strPosCode,2,2)),'0' )as strPosCode "
				+ " from tblposmaster a  ";
			ResultSet rsDocCode = clsGlobalVarClass.dbMysql.executeResultSet(docSql);

			if (rsDocCode.next())
			{
			    docNo = Long.parseLong(rsDocCode.getString(1)) + 1;
			    code = "P" + String.format("%02d", docNo);
			}
			else
			{
			    docNo++;
			    code = "P" + String.format("%02d", docNo);
			}
			query = "insert into tblposmaster(strPosCode,strPosName,strPosType,strDebitCardTransactionYN,"
				+ "strPropertyPOSCode,strUserCreated,strUserEdited,dteDateCreated,dteDateEdited"
				+ ",strCounterWiseBilling,strPrintVatNo,strPrintServiceTaxNo,strVatNo,strServiceTaxNo) "
				+ "values('" + code + "','" + rsPOSMaster.getString(1) + "','Dine In','No',''"
				+ ",'" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "',"
				+ "'" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "'"
				+ ",'No','N','N','','')";
			int insert = clsGlobalVarClass.dbMysql.execute(query);
			if (insert == 1)
			{
			    query = "update tblimportexcel set strPOSCode='" + code + "' "
				    + "where strPOSName='" + rsPOSMaster.getString(1) + "'";
			    int update = clsGlobalVarClass.dbMysql.execute(query);
			}
		    }
		}
	    }
	    rsPOSMaster.close();

	    clsGlobalVarClass.dbMysql.execute("update tblimportexcel  set strPOSCode='All' where strPOSName='All' ");
	    flgReturn = true;

	}
	catch (Exception e)
	{
	    flgReturn = false;
	    e.printStackTrace();
	}
	finally
	{
	    return flgReturn;
	}
    }

    private boolean funGenerateItemMaster()
    {
	boolean flgReturn = false;
	String query = "", code = "", stkInEnable = "N", purchaseRate = "0.00", applyDiscount = "Y";
	long docNo = 0;
	try
	{
	    //clsGlobalVarClass.dbMysql.execute("truncate table tblitemmaster");
	    String sql = "select distinct(strItemName),strSubGroupCode,strStockInEnable,dblPurchaseRate"
		    + ",strExternalCode,strItemDetails,strItemType,strApplyDiscount,strShortName,dblTax,strRevenueHead"
		    + ",strUOM,strRawMaterial,strRecipeUOM "
		    + "from tblimportexcel";
	    ResultSet rsItemMaster = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rsItemMaster.next())
	    {
		lblMessage.setText("");
		lblMessage.setText("Importing Item Master");
		String sqlNameCheck = "select a.strItemCode from tblitemmaster a where a.strItemName='" + rsItemMaster.getString(1) + "'";
		ResultSet rsNameCheck = clsGlobalVarClass.dbMysql.executeResultSet(sqlNameCheck);
		if (rsNameCheck.next())
		{
		    code = rsNameCheck.getString(1);
		    query = "update tblimportexcel set strItemCode='" + code + "' "
			    + "where strItemName='" + rsItemMaster.getString(1) + "'";
		    int update = clsGlobalVarClass.dbMysql.execute(query);
		}
		else
		{
		    if (rsItemMaster.getString(1).trim().length() > 0)
		    {
			String docSql = " select ifnull(max(MID(a.strItemCode,2,6)),'0' )as strItemCode  from tblitemmaster a  ";
			ResultSet rsDocCode = clsGlobalVarClass.dbMysql.executeResultSet(docSql);

			if (rsDocCode.next())
			{
			    docNo = Long.parseLong(rsDocCode.getString(1)) + 1;
			    code = "I" + String.format("%06d", docNo);
			}
			else
			{
			    docNo++;
			    code = "I" + String.format("%06d", docNo);
			}

			if (rsItemMaster.getString(3).equals("Y"))
			{
			    stkInEnable = "Y";
			}
			if (rsItemMaster.getString(4).trim().length() == 0)
			{
			    purchaseRate = "0.00";
			}
			else
			{
			    purchaseRate = rsItemMaster.getString(4);
			}

			if (rsItemMaster.getString(8).trim().length() == 0)
			{
			    applyDiscount = "Y";
			}
			else
			{
			    applyDiscount = rsItemMaster.getString(8);
			}

			String rawMaterial = rsItemMaster.getString(13);
			String itemForSale = "Y";
			if (rawMaterial.equalsIgnoreCase("Yes") || rawMaterial.equalsIgnoreCase("Y"))
			{
			    rawMaterial = "Y";
			    itemForSale = "Y";
			}
			else
			{
			    rawMaterial = "N";
			    itemForSale = "N";
			}
			String recipeUOM = rsItemMaster.getString(14);

			query = "insert into tblitemmaster (strItemCode,strItemName,strSubGroupCode,strTaxIndicator"
				+ ",strStockInEnable,dblPurchaseRate,strExternalCode,strItemDetails,strUserCreated"
				+ ",strUserEdited,dteDateCreated,dteDateEdited,strClientCode,strItemType,strDiscountApply"
				+ ",strShortName,strRevenueHead,strUOM,imgImage,strRawMaterial,strItemForSale,strRecipeUOM)"
				+ " values('" + code + "','" + rsItemMaster.getString(1) + "','" + rsItemMaster.getString(2) + "'"
				+ ",'" + rsItemMaster.getString(10) + "','" + stkInEnable + "','" + purchaseRate + "','" + rsItemMaster.getString(5) + "'"
				+ ",'" + rsItemMaster.getString(6) + "','" + clsGlobalVarClass.gUserCode + "'"
				+ ",'" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.getCurrentDateTime() + "'"
				+ ",'" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.gClientCode + "'"
				+ ",'" + rsItemMaster.getString(7) + "','" + applyDiscount + "','" + rsItemMaster.getString(9) + "'"
				+ ",'" + rsItemMaster.getString(11) + "','" + rsItemMaster.getString(12) + "','','" + rawMaterial + "'"
				+ ",'" + itemForSale + "','" + recipeUOM + "')";
			int insert = clsGlobalVarClass.dbMysql.execute(query);
			if (insert == 1)
			{
			    query = "update tblimportexcel set strItemCode='" + code + "' "
				    + "where strItemName='" + rsItemMaster.getString(1) + "'";
			    int update = clsGlobalVarClass.dbMysql.execute(query);
			}
		    }
		}
	    }
	    rsItemMaster.close();
	    flgReturn = true;

	}
	catch (Exception e)
	{
	    flgReturn = false;
	    e.printStackTrace();
	}
	finally
	{
	    return flgReturn;
	}
    }

    private boolean funGenerateItemMasterForRetail()
    {
	boolean flgReturn = false;
	String query = "", code = "", stkInEnable = "N", purchaseRate = "0.00", saleRate = "0.00";
	long docNo = 0;
	try
	{
	    //clsGlobalVarClass.dbMysql.execute("truncate table tblitemmaster");
	    String sql = "select distinct(strItemName),strSubGroupCode,strStockInEnable,dblPurchaseRate"
		    + ",strExternalCode,strItemDetails,strItemType,strApplyDiscount,strShortName"
		    + ",dblPriceMonday,strRevenueHead,strUOM,strRawMaterial,strRecipeUOM "
		    + "from tblimportexcel";
	    ResultSet rsItemMaster = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rsItemMaster.next())
	    {
		lblMessage.setText("");
		lblMessage.setText("Importing Item Master");
		if (rsItemMaster.getString(1).trim().length() > 0)
		{
		    if (rsItemMaster.getString(3).equals("Y"))
		    {
			stkInEnable = "Y";
		    }
		    if (rsItemMaster.getString(4).trim().length() == 0)
		    {
			purchaseRate = "0.00";
		    }
		    if (rsItemMaster.getString(10).trim().length() == 0)
		    {
			saleRate = "0.00";
		    }
		    else
		    {
			saleRate = rsItemMaster.getString(10);
		    }
		    String receivedUOM = rsItemMaster.getString(12);
		    String rawMaterial = rsItemMaster.getString(13);
		    String itemForSale = "Y";
		    if (rawMaterial.equalsIgnoreCase("Yes") || rawMaterial.equalsIgnoreCase("Y"))
		    {
			rawMaterial = "Y";
			itemForSale = "Y";
		    }
		    else
		    {
			rawMaterial = "N";
			itemForSale = "N";
		    }
		    String recipeUOM = rsItemMaster.getString(14);

		    docNo++;
		    code = "I" + String.format("%06d", docNo);
		    query = "insert into tblitemmaster (strItemCode,strItemName,strSubGroupCode,strTaxIndicator"
			    + ",strStockInEnable,dblPurchaseRate,strExternalCode,strItemDetails,strUserCreated"
			    + ",strUserEdited,dteDateCreated,dteDateEdited,strClientCode,strItemType,strDiscountApply"
			    + ",strShortName,dblSalePrice,strRevenueHead,imgImage,strUOM,strRawMaterial,strRecipeUOM )"
			    + " values('" + code + "','" + rsItemMaster.getString(1) + "','" + rsItemMaster.getString(2) + "'"
			    + ",'','" + stkInEnable + "','" + purchaseRate + "','" + rsItemMaster.getString(5) + "'"
			    + ",'" + rsItemMaster.getString(6) + "','" + clsGlobalVarClass.gUserCode + "'"
			    + ",'" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.getCurrentDateTime() + "'"
			    + ",'" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.gClientCode + "'"
			    + ",'" + rsItemMaster.getString(7) + "','" + rsItemMaster.getString(8) + "'"
			    + ",'" + rsItemMaster.getString(9) + "'," + saleRate + ",'" + rsItemMaster.getString(11) + "'"
			    + ",'','" + receivedUOM + "','" + rawMaterial + "','" + recipeUOM + "')";

		    System.out.println(query);
		    int insert = clsGlobalVarClass.dbMysql.execute(query);
		    if (insert == 1)
		    {
			query = "update tblimportexcel set strItemCode='" + code + "' "
				+ "where strItemName='" + rsItemMaster.getString(1) + "'";
			clsGlobalVarClass.dbMysql.execute(query);
		    }
		}
	    }
	    rsItemMaster.close();
	    flgReturn = true;

	}
	catch (Exception e)
	{
	    flgReturn = false;
	    e.printStackTrace();
	}
	finally
	{
	    return flgReturn;
	}
    }

    private boolean funGenerateAreaMaster()
    {
	boolean flgReturn = false;
	String query = "", code = "";
	long docNo = 0;
	try
	{
	    //clsGlobalVarClass.dbMysql.execute("truncate table tblareamaster");
	    //String sql="select distinct(strAreaName) from tblimportexcel where strAreaName!='All'";
	    String sql = "select distinct(strAreaName) from tblimportexcel";
	    ResultSet rsAreaMaster = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rsAreaMaster.next())
	    {
		lblMessage.setText("");
		lblMessage.setText("Importing Area Master");

		String sqlNameCheck = " select a.strAreaCode from tblareamaster a where a.strAreaName='" + rsAreaMaster.getString(1) + "' ";
		ResultSet rsNameCheck = clsGlobalVarClass.dbMysql.executeResultSet(sqlNameCheck);
		if (!rsNameCheck.next())
		{
		    if (rsAreaMaster.getString(1).trim().length() > 0)
		    {
			String docSql = " select ifnull(max(MID(a.strAreaCode,2,3)),'0' )as strAreaCode from tblareamaster a  ";
			ResultSet rsDocCode = clsGlobalVarClass.dbMysql.executeResultSet(docSql);
			if (rsDocCode.next())
			{
			    docNo = Long.parseLong(rsDocCode.getString(1)) + 1;
			    code = "A" + String.format("%03d", docNo);
			}
			else
			{
			    docNo++;
			    code = "A" + String.format("%03d", docNo);
			}
			query = "insert into tblareamaster (strAreaCode,strAreaName,strUserCreated,strUserEdited,"
				+ "dteDateCreated,dteDateEdited)"
				+ "values('" + code + "','" + rsAreaMaster.getString(1) + "'"
				+ ",'" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "'"
				+ ",'" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "')";
			int insert = clsGlobalVarClass.dbMysql.execute(query);
			if (insert == 1)
			{
			    query = "update tblimportexcel set strAreaCode='" + code + "' "
				    + "where strAreaName='" + rsAreaMaster.getString(1) + "'";
			    clsGlobalVarClass.dbMysql.execute(query);
			}
		    }
		}
		else
		{
		    code = rsNameCheck.getString(1);
		    query = "update tblimportexcel set strAreaCode='" + code + "' "
			    + "where strAreaName='" + rsAreaMaster.getString(1) + "'";
		    clsGlobalVarClass.dbMysql.execute(query);
		}
	    }
	    rsAreaMaster.close();
	    query = "update tblinternal set dblLastNo=" + docNo + " where strTransactionType='Area'";
	    clsGlobalVarClass.dbMysql.execute(query);

	    flgReturn = true;

	}
	catch (Exception e)
	{
	    flgReturn = false;
	    e.printStackTrace();
	}
	finally
	{
	    return flgReturn;
	}
    }

    private boolean funGenerateSubMenuHead()
    {
	boolean flgReturn = false;
	String query = "", code = "";
	long docNo = 0;
	try
	{
	    //clsGlobalVarClass.dbMysql.execute("truncate table tblsubmenuhead");
	    String sql = "select distinct(strSubMenuHeadName),strMenuHeadCode from tblimportexcel";
	    ResultSet rsSubMenuHead = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rsSubMenuHead.next())
	    {
		lblMessage.setText("");
		lblMessage.setText("Importing Sub MenuHead Master");
		String sqlNameCheck = "select a.strSubMenuHeadCode from tblsubmenuhead a where a.strSubMenuHeadName='" + rsSubMenuHead.getString(1) + "'";
		ResultSet rsNameCheck = clsGlobalVarClass.dbMysql.executeResultSet(sqlNameCheck);
		if (!rsNameCheck.next())
		{
		    if (rsSubMenuHead.getString(1).trim().length() > 0)
		    {
			String docSql = " select ifnull(max(MID(a.strSubMenuHeadCode,3,6)),'0' )as strSubMenuHeadCode  from tblsubmenuhead a ";
			ResultSet rsDocCode = clsGlobalVarClass.dbMysql.executeResultSet(docSql);

			if (rsDocCode.next())
			{
			    docNo = Long.parseLong(rsDocCode.getString(1)) + 1;
			    code = "SM" + String.format("%06d", docNo);
			}
			else
			{
			    docNo++;
			    code = "SM" + String.format("%06d", docNo);
			}

			query = "insert into tblsubmenuhead (strSubMenuHeadCode,strMenuCode,strSubMenuHeadShortName,"
				+ "strSubMenuHeadName,strSubMenuOperational,strUserCreated,strUserEdited,dteDateCreated,"
				+ "dteDateEdited,strClientCode)"
				+ " values('" + code + "','" + rsSubMenuHead.getString(2) + "',''"
				+ ",'" + rsSubMenuHead.getString(1).trim() + "','Y','" + clsGlobalVarClass.gUserCode + "'"
				+ ",'" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.getCurrentDateTime() + "'"
				+ ",'" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.gClientCode + "')";
			int insert = clsGlobalVarClass.dbMysql.execute(query);
			if (insert == 1)
			{
			    query = "update tblimportexcel set strSubMenuHeadCode='" + code + "' "
				    + "where strSubMenuHeadName='" + rsSubMenuHead.getString(1) + "'";
			    int update = clsGlobalVarClass.dbMysql.execute(query);
			}
		    }
		}
		else
		{
		    code = rsNameCheck.getString(1);
		    query = "update tblimportexcel set strSubMenuHeadCode='" + code + "' "
			    + "where strSubMenuHeadName='" + rsSubMenuHead.getString(1) + "'";
		    int update = clsGlobalVarClass.dbMysql.execute(query);
		}

		flgReturn = true;
	    }
	    rsSubMenuHead.close();
	}
	catch (Exception e)
	{
	    flgReturn = false;
	    e.printStackTrace();
	}
	finally
	{
	    return flgReturn;
	}
    }

    private boolean funGenerateMenuItemPriceHD()
    {
	boolean flgReturn = false;
	String query = "";
	try
	{
	    //clsGlobalVarClass.dbMysql.execute("truncate table tblmenuitempricinghd");
	    String sql = "select distinct(strMenuHeadCode),strMenuHeadName,strPOSCode from tblimportexcel";
	    ResultSet rsMenuItemPriceHd = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rsMenuItemPriceHd.next())
	    {
		lblMessage.setText("");
		lblMessage.setText("Importing Price Master");
		query = "insert into tblmenuitempricinghd(strPosCode,strMenuCode,strMenuName,strUserCreated"
			+ ",strUserEdited,dteDateCreated,dteDateEdited) "
			+ "values('" + rsMenuItemPriceHd.getString(3) + "','" + rsMenuItemPriceHd.getString(1) + "'"
			+ ",'" + rsMenuItemPriceHd.getString(2) + "','" + clsGlobalVarClass.gUserCode + "'"
			+ ",'" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.getCurrentDateTime() + "'"
			+ ",'" + clsGlobalVarClass.getCurrentDateTime() + "')";
		clsGlobalVarClass.dbMysql.execute(query);
	    }
	    rsMenuItemPriceHd.close();
	    flgReturn = true;

	}
	catch (Exception e)
	{
	    flgReturn = false;
	    e.printStackTrace();
	}
	finally
	{
	    return flgReturn;
	}
    }

    private String funFormatPrice(String price)
    {
	if (price.contains(","))
	{
	    price = price.replace(",", "");
	}
	return price;
    }

    private boolean funGenerateMenuItemPriceDTL()
    {
	boolean flgReturn = false;
	String fromDate = "", toDate = "", priceMon = "", priceTue = "", priceWed = "", priceThu = "", priceFri = "", priceSat = "";
	String priceSun = "";
	Date dt = new Date();
	fromDate = (dt.getYear() + 1900) + "-" + (dt.getMonth() + 1) + "-" + dt.getDate() + " ";
	fromDate += dt.getHours() + ":" + dt.getMinutes() + ":" + dt.getSeconds();

	toDate = (dt.getYear() + 1901) + "-" + (dt.getMonth() + 1) + "-" + dt.getDate() + " ";
	toDate += dt.getHours() + ":" + dt.getMinutes() + ":" + dt.getSeconds();

	String query = "";
	try
	{
	    String sqlEmptyPricingTable = "truncate table tblmenuitempricingdtl";
	    clsGlobalVarClass.dbMysql.execute(sqlEmptyPricingTable);

	    String sql = "select distinct(strItemCode),strItemName,strPOSCode,strMenuHeadCode"
		    + ",dblPriceMonday,dblPriceTuesday,dblPriceWednesday,dblPriceThursday,dblPriceFriday"
		    + ",dblPriceSaturday,dblPriceSunday,strCostCenterCode,strAreaCode,strSubMenuHeadCode "
		    + ",strHourlyPricing,tmeTimeFrom,tmeTimeTo "
		    + "from tblimportexcel "
		    + "where (strRawMaterial='N' or strRawMaterial='') ";
	    ResultSet rsMenuItemPriceDtl = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rsMenuItemPriceDtl.next())
	    {
		lblMessage.setText("");
		lblMessage.setText("Importing Price Master");
		if (rsMenuItemPriceDtl.getString(5).trim().length() == 0)
		{
		    priceMon = "0.00";
		}
		else
		{
		    priceMon = funFormatPrice(rsMenuItemPriceDtl.getString(5).trim());
		}

		if (rsMenuItemPriceDtl.getString(6).trim().length() == 0)
		{
		    priceTue = "0.00";
		}
		else
		{
		    priceTue = funFormatPrice(rsMenuItemPriceDtl.getString(6).trim());
		}

		if (rsMenuItemPriceDtl.getString(7).trim().length() == 0)
		{
		    priceWed = "0.00";
		}
		else
		{
		    priceWed = funFormatPrice(rsMenuItemPriceDtl.getString(7).trim());
		}

		if (rsMenuItemPriceDtl.getString(8).trim().length() == 0)
		{
		    priceThu = "0.00";
		}
		else
		{
		    priceThu = funFormatPrice(rsMenuItemPriceDtl.getString(8).trim());
		}

		if (rsMenuItemPriceDtl.getString(9).trim().length() == 0)
		{
		    priceFri = "0.00";
		}
		else
		{
		    priceFri = funFormatPrice(rsMenuItemPriceDtl.getString(9).trim());
		}

		if (rsMenuItemPriceDtl.getString(10).trim().length() == 0)
		{
		    priceSat = "0.00";
		}
		else
		{
		    priceSat = funFormatPrice(rsMenuItemPriceDtl.getString(10).trim());
		}

		if (rsMenuItemPriceDtl.getString(11).trim().length() == 0)
		{
		    priceSun = "0.00";
		}
		else
		{
		    priceSun = funFormatPrice(rsMenuItemPriceDtl.getString(11).trim());
		}

		query = "insert into tblmenuitempricingdtl(strItemCode,strItemName,strPosCode,strMenuCode"
			+ ",strPopular,strPriceMonday,strPriceTuesday,strPriceWednesday,strPriceThursday,strPriceFriday"
			+ ",strPriceSaturday,strPriceSunday,dteFromDate,dteToDate,tmeTimeFrom,strAMPMFrom,tmeTimeTo"
			+ ",strAMPMTo,strCostCenterCode,strTextColor,strUserCreated,strUserEdited,dteDateCreated"
			+ ",dteDateEdited,strAreaCode,strSubMenuHeadCode,strHourlyPricing,strClientCode) "
			+ "values('" + rsMenuItemPriceDtl.getString(1) + "','" + rsMenuItemPriceDtl.getString(2) + "'"
			+ ",'" + rsMenuItemPriceDtl.getString(3) + "','" + rsMenuItemPriceDtl.getString(4) + "'"
			+ ",'N','" + priceMon + "','" + priceTue + "'" + ",'" + priceWed + "','" + priceThu + "'" + ",'" + priceFri + "'"
			+ ",'" + priceSat + "'" + ",'" + priceSun + "'"
			+ ",'" + fromDate + "','" + toDate + "'  "
			+ ",'" + rsMenuItemPriceDtl.getString(16) + "', 'AM', '" + rsMenuItemPriceDtl.getString(17) + "', 'AM','" + rsMenuItemPriceDtl.getString(12) + "','Black'"
			+ ",'" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "'"
			+ ",'" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "'"
			+ ",'" + rsMenuItemPriceDtl.getString(13) + "','" + rsMenuItemPriceDtl.getString(14) + "'"
			+ ",'" + rsMenuItemPriceDtl.getString(15) + "','" + clsGlobalVarClass.gClientCode + "') ";
		int insert = clsGlobalVarClass.dbMysql.execute(query);
	    }
	    rsMenuItemPriceDtl.close();
	    flgReturn = true;

	}
	catch (Exception e)
	{
	    flgReturn = false;
	    e.printStackTrace();
	}
	finally
	{
	    return flgReturn;
	}
    }

    private int funExportData()
    {
	try
	{
	    HSSFWorkbook hwb = new HSSFWorkbook();
	    HSSFSheet sheet = hwb.createSheet("new sheet");
	    HSSFRow rowhead = sheet.createRow((short) 0);

	    rowhead.createCell((short) 0).setCellValue("Item Code");
	    rowhead.createCell((short) 1).setCellValue("Item Name");
	    rowhead.createCell((short) 2).setCellValue("Short Name");
	    rowhead.createCell((short) 3).setCellValue("Menu Name");
	    rowhead.createCell((short) 4).setCellValue("SubMenuHead");
	    rowhead.createCell((short) 5).setCellValue("RevenueHead");
	    rowhead.createCell((short) 6).setCellValue("POS Name");
	    rowhead.createCell((short) 7).setCellValue("Sub Group Name");
	    rowhead.createCell((short) 8).setCellValue("Group Name");
	    rowhead.createCell((short) 9).setCellValue("Cost Center");
	    rowhead.createCell((short) 10).setCellValue("Area Name");
	    rowhead.createCell((short) 11).setCellValue("Tax");
	    rowhead.createCell((short) 12).setCellValue("PuChase Rate");
	    rowhead.createCell((short) 13).setCellValue("External Code");
	    rowhead.createCell((short) 14).setCellValue("Item Details");
	    rowhead.createCell((short) 15).setCellValue("Item Type");
	    rowhead.createCell((short) 16).setCellValue("Apply DisCount(Yes/No)");
	    rowhead.createCell((short) 17).setCellValue("StoCk In Enable");
	    rowhead.createCell((short) 18).setCellValue("Sun PriCe");
	    rowhead.createCell((short) 19).setCellValue("Mon PriCe");
	    rowhead.createCell((short) 20).setCellValue("Tue PriCe");
	    rowhead.createCell((short) 21).setCellValue("Wed PriCe");
	    rowhead.createCell((short) 22).setCellValue("Thu PriCe");
	    rowhead.createCell((short) 23).setCellValue("Fri PriCe");
	    rowhead.createCell((short) 24).setCellValue("Sat PriCe");
	    rowhead.createCell((short) 25).setCellValue("Counter");
	    rowhead.createCell((short) 26).setCellValue("Received UOM");
	    rowhead.createCell((short) 27).setCellValue("Recipe UOM");
	    rowhead.createCell((short) 28).setCellValue("Raw Material");

	    rowhead.createCell((short) 29).setCellValue("Hourly Price(YES/NO)");
	    rowhead.createCell((short) 30).setCellValue("From Time(24 HRS)");
	    rowhead.createCell((short) 31).setCellValue("To Time(24 HRS)");

	    String sql = "SELECT b.strItemCode,b.strItemName,b.strShortName, IFNULL(c.strMenuName,''),IFNULL(g.strSubMenuHeadShortName,''),b.strRevenueHead "
		    + ",IFNULL(h.strPosName,'All'),e.strSubGroupName,f.strGroupName,IFNULL(d.strCostCenterName,''),IFNULL(i.strAreaName,''),b.strTaxIndicator "
		    + ",b.dblPurchaseRate,b.strExternalCode,b.strItemDetails,b.strItemType,b.strDiscountApply,b.strStockInEnable,IFNULL(a.strPriceSunday,0) "
		    + ",IFNULL(a.strPriceMonday,0),IFNULL(a.strPriceTuesday,0),IFNULL(a.strPriceWednesday,0),IFNULL(a.strPriceThursday,0) "
		    + ",IFNULL(a.strPriceFriday,0),IFNULL(a.strPriceSaturday,0),'',b.strUOM,b.strRecipeUOM,b.strRawMaterial,ifnull(a.strHourlyPricing,'NO'),ifnull(a.tmeTimeFrom,'HH:MM:S'),ifnull(a.tmeTimeTo,'HH:MM:S') "
		    + "FROM tblitemmaster b "
		    + "LEFT OUTER JOIN  tblmenuitempricingdtl a ON a.strItemCode=b.strItemCode "
		    + "LEFT OUTER JOIN tblsubgrouphd e ON b.strSubGroupCode=e.strSubGroupCode "
		    + "LEFT OUTER JOIN tblgrouphd f ON e.strGroupCode=f.strGroupCode "
		    + "LEFT OUTER JOIN tblmenuhd c ON a.strMenuCode = c.strMenuCode "
		    + "LEFT OUTER JOIN tblcostcentermaster d ON a.strCostCenterCode=d.strCostCenterCode "
		    + "LEFT OUTER JOIN tblsubmenuhead g ON a.strSubMenuHeadCode=g.strSubMenuHeadCode "
		    + "LEFT OUTER JOIN tblposmaster h ON a.strPosCode=h.strPosCode "
		    + "LEFT OUTER JOIN tblareamaster i ON a.strAreaCode=i.strAreaCode "
		    + "ORDER BY b.strItemCode,a.strPosCode ";
	    System.out.println("sql=" + sql);
	    ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    Integer i = 1;

	    while (rs.next())
	    {
		//System.out.println("i="+i);
		HSSFRow row = sheet.createRow(i);
		row.createCell((short) 0).setCellValue(rs.getString(1));
		row.createCell((short) 1).setCellValue(rs.getString(2));
		row.createCell((short) 2).setCellValue(rs.getString(3));
		row.createCell((short) 3).setCellValue(rs.getString(4));
		row.createCell((short) 4).setCellValue(rs.getString(5));
		row.createCell((short) 5).setCellValue(rs.getString(6));
		row.createCell((short) 6).setCellValue(rs.getString(7));
		row.createCell((short) 7).setCellValue(rs.getString(8));
		row.createCell((short) 8).setCellValue(rs.getString(9));
		row.createCell((short) 9).setCellValue(rs.getString(10));
		row.createCell((short) 10).setCellValue(rs.getString(11));
		row.createCell((short) 11).setCellValue(rs.getString(12));
		row.createCell((short) 12).setCellValue(rs.getString(13));
		row.createCell((short) 13).setCellValue(rs.getString(14));
		row.createCell((short) 14).setCellValue(rs.getString(15));
		row.createCell((short) 15).setCellValue(rs.getString(16));
		row.createCell((short) 16).setCellValue(rs.getString(17));
		row.createCell((short) 17).setCellValue(rs.getString(18));
		row.createCell((short) 18).setCellValue(rs.getString(19));
		row.createCell((short) 19).setCellValue(rs.getString(20));
		row.createCell((short) 20).setCellValue(rs.getString(21));
		row.createCell((short) 21).setCellValue(rs.getString(22));
		row.createCell((short) 22).setCellValue(rs.getString(23));
		row.createCell((short) 23).setCellValue(rs.getString(24));
		row.createCell((short) 24).setCellValue(rs.getString(25));
		row.createCell((short) 25).setCellValue(rs.getString(26));
		row.createCell((short) 26).setCellValue(rs.getString(27));
		row.createCell((short) 27).setCellValue(rs.getString(28));
		row.createCell((short) 28).setCellValue(rs.getString(29));

		row.createCell((short) 29).setCellValue(rs.getString(30));
		row.createCell((short) 30).setCellValue(rs.getString(31));
		row.createCell((short) 31).setCellValue(rs.getString(32));

		i++;
	    }

	    String filePath = System.getProperty("user.dir");
	    File file = new File(filePath + "/Valid Item Master.xls");
	    FileOutputStream fileOut = new FileOutputStream(file);
	    hwb.write(fileOut);
	    fileOut.close();
	    //Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + filePath + "/CustomerData.xls");
	    rs.close();
	    JOptionPane.showMessageDialog(this, "Data Exported Successfully!!!");
	}
	catch (FileNotFoundException ex)
	{
	    JOptionPane.showMessageDialog(this, "File is already opened please close ");
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	finally
	{
	    return 1;
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

        jPanel1 = new javax.swing.JPanel();
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
        jPanel2 = new JPanel() {  
            public void paintComponent(Graphics g) {  
                Image img = Toolkit.getDefaultToolkit().getImage(  
                    getClass().getResource("/com/POSTransaction/images/imgBackgroundImage.png"));  
                g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);  
            }  
        };  ;
        jPanel3 = new javax.swing.JPanel();
        lblFileSelection = new javax.swing.JLabel();
        txtFileName = new javax.swing.JTextField();
        btnBrowse = new javax.swing.JButton();
        btnImportFile = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        lblModuleName = new javax.swing.JLabel();
        lblMessage = new javax.swing.JLabel();
        cmbMaster = new javax.swing.JComboBox();
        lblMaster = new javax.swing.JLabel();
        lblMaster1 = new javax.swing.JLabel();
        cmbIndType = new javax.swing.JComboBox();
        btnExportFile = new javax.swing.JButton();

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

        jPanel1.setBackground(new java.awt.Color(69, 164, 238));
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.LINE_AXIS));

        lblProductName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblProductName.setForeground(new java.awt.Color(255, 255, 255));
        lblProductName.setText("SPOS -");
        jPanel1.add(lblProductName);

        lblModuleName1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblModuleName1.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.add(lblModuleName1);

        lblformName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblformName.setForeground(new java.awt.Color(255, 255, 255));
        lblformName.setText("- Import Masters");
        jPanel1.add(lblformName);
        jPanel1.add(filler4);
        jPanel1.add(filler5);

        lblPosName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblPosName.setForeground(new java.awt.Color(255, 255, 255));
        lblPosName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPosName.setMaximumSize(new java.awt.Dimension(321, 30));
        lblPosName.setMinimumSize(new java.awt.Dimension(321, 30));
        lblPosName.setPreferredSize(new java.awt.Dimension(321, 30));
        jPanel1.add(lblPosName);
        jPanel1.add(filler6);

        lblUserCode.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblUserCode.setForeground(new java.awt.Color(255, 255, 255));
        lblUserCode.setMaximumSize(new java.awt.Dimension(90, 30));
        lblUserCode.setMinimumSize(new java.awt.Dimension(90, 30));
        lblUserCode.setPreferredSize(new java.awt.Dimension(90, 30));
        jPanel1.add(lblUserCode);

        lblDate.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblDate.setForeground(new java.awt.Color(255, 255, 255));
        lblDate.setMaximumSize(new java.awt.Dimension(192, 30));
        lblDate.setMinimumSize(new java.awt.Dimension(192, 30));
        lblDate.setPreferredSize(new java.awt.Dimension(192, 30));
        jPanel1.add(lblDate);

        lblHOSign.setMaximumSize(new java.awt.Dimension(34, 30));
        lblHOSign.setMinimumSize(new java.awt.Dimension(34, 30));
        lblHOSign.setPreferredSize(new java.awt.Dimension(34, 30));
        jPanel1.add(lblHOSign);

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_START);

        jPanel2.setLayout(new java.awt.GridBagLayout());

        jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        jPanel3.setMinimumSize(new java.awt.Dimension(800, 570));
        jPanel3.setOpaque(false);

        lblFileSelection.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblFileSelection.setText("Select File          :");

        txtFileName.setEditable(false);

        btnBrowse.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnBrowse.setForeground(new java.awt.Color(254, 254, 254));
        btnBrowse.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnBrowse.setText("Browse");
        btnBrowse.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnBrowse.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
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

        btnImportFile.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnImportFile.setForeground(new java.awt.Color(255, 255, 255));
        btnImportFile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnImportFile.setText("IMPORT");
        btnImportFile.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnImportFile.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnImportFile.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnImportFileActionPerformed(evt);
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

        btnClose.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnClose.setForeground(new java.awt.Color(255, 255, 255));
        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnClose.setText("CLOSE");
        btnClose.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnClose.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnClose.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCloseActionPerformed(evt);
            }
        });

        lblModuleName.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        lblModuleName.setForeground(new java.awt.Color(14, 7, 7));
        lblModuleName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblModuleName.setText("Import Masters");

        lblMessage.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

        cmbMaster.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmbMaster.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Customer", "Item" }));
        cmbMaster.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbMasterActionPerformed(evt);
            }
        });

        lblMaster.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblMaster.setText("Select Master     :");

        lblMaster1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblMaster1.setText("Industry Type    :");

        cmbIndType.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmbIndType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "F&B", "Retail" }));
        cmbIndType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbIndTypeActionPerformed(evt);
            }
        });

        btnExportFile.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnExportFile.setForeground(new java.awt.Color(255, 255, 255));
        btnExportFile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnExportFile.setText("EXPORT");
        btnExportFile.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnExportFile.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnExportFile.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnExportFileActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(lblMaster, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(cmbMaster, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(lblMaster1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(cmbIndType, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(110, 110, 110)
                                .addComponent(txtFileName, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lblFileSelection, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(btnBrowse, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(111, 111, 111))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(111, 111, 111)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnExportFile, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(170, 170, 170)
                        .addComponent(lblModuleName, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(100, 100, 100)
                        .addComponent(lblMessage, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(btnImportFile, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(100, 100, 100)
                        .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(110, 110, 110)
                        .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(145, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(lblModuleName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(64, 64, 64)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblMaster, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbMaster, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblMaster1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbIndType, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtFileName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblFileSelection, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBrowse, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblMessage, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(70, 70, 70)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnImportFile, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(btnExportFile, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(105, Short.MAX_VALUE))
        );

        jPanel2.add(jPanel3, new java.awt.GridBagConstraints());

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBrowseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBrowseMouseClicked
	// TODO add your handling code here:
	funBrowseFile();
    }//GEN-LAST:event_btnBrowseMouseClicked

    private void btnBrowseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBrowseActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_btnBrowseActionPerformed

    private void btnImportFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImportFileActionPerformed
	// TODO add your handling code here:
	funImportExcel();
    }//GEN-LAST:event_btnImportFileActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
	// TODO add your handling code here:
	funResetFields();
    }//GEN-LAST:event_btnResetActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
	// TODO add your handling code here:
	dispose();
	clsGlobalVarClass.hmActiveForms.remove("ImportExcel");
    }//GEN-LAST:event_btnCloseActionPerformed

    private void cmbMasterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbMasterActionPerformed
	// TODO add your handling code here:
	if (cmbMaster.getSelectedItem().toString().equals("Item"))
	{
	    lblProductName.setText("Import Masters");
	}
	else if (cmbMaster.getSelectedItem().toString().equals("Customer"))
	{
	    lblProductName.setText("Import Customer Master");
	}
    }//GEN-LAST:event_cmbMasterActionPerformed

    private void cmbIndTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbIndTypeActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_cmbIndTypeActionPerformed

    private void btnExportFileActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnExportFileActionPerformed
    {//GEN-HEADEREND:event_btnExportFileActionPerformed
	// TODO add your handling code here:
	funExportData();
    }//GEN-LAST:event_btnExportFileActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosed
    {//GEN-HEADEREND:event_formWindowClosed
	clsGlobalVarClass.hmActiveForms.remove("ImportExcel");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosing
    {//GEN-HEADEREND:event_formWindowClosing
	clsGlobalVarClass.hmActiveForms.remove("ImportExcel");
    }//GEN-LAST:event_formWindowClosing

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
	    java.util.logging.Logger.getLogger(frmImportExcelFile.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	}
	catch (InstantiationException ex)
	{
	    java.util.logging.Logger.getLogger(frmImportExcelFile.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	}
	catch (IllegalAccessException ex)
	{
	    java.util.logging.Logger.getLogger(frmImportExcelFile.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	}
	catch (javax.swing.UnsupportedLookAndFeelException ex)
	{
	    java.util.logging.Logger.getLogger(frmImportExcelFile.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	}
	//</editor-fold>
	//</editor-fold>

	/* Create and display the form */
	java.awt.EventQueue.invokeLater(new Runnable()
	{
	    public void run()
	    {
		new frmImportExcelFile().setVisible(true);
	    }
	});
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBrowse;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnExportFile;
    private javax.swing.JButton btnImportFile;
    private javax.swing.JButton btnReset;
    private javax.swing.JComboBox cmbIndType;
    private javax.swing.JComboBox cmbMaster;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblFileSelection;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblMaster;
    private javax.swing.JLabel lblMaster1;
    private javax.swing.JLabel lblMessage;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblModuleName1;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblformName;
    private javax.swing.JTextField txtFileName;
    // End of variables declaration//GEN-END:variables
}
