package com.POSTransaction.view;

import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsUtility;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class frmPostDataToHO extends javax.swing.JFrame
{

    private String[] liveTableItems;
    private String[] qFileTableItems;
    private DefaultComboBoxModel liveTableModel;
    private DefaultComboBoxModel qFileTableModel;
    private DefaultTableModel dtmDataPostModel;
    private SimpleDateFormat dateFormat;
    private String strFromDate;
    private String strToDate;
    private StringBuilder selectQuery;
    private String posCode = "";
    private String[] posCodeNposName;

    public frmPostDataToHO()
    {
        initComponents();
        try
        {
            java.util.Date dt1 = new java.util.Date();
            int day = dt1.getDate();
            int month = dt1.getMonth() + 1;
            int year = dt1.getYear() + 1900;
            String dte = day + "-" + month + "-" + year;
            java.util.Date date = new SimpleDateFormat("dd-MM-yyyy").parse(clsGlobalVarClass.gPOSDateToDisplay);
            dteFromDate.setDate(date);
            dteToDate.setDate(date);
            lblPosName.setText(clsGlobalVarClass.gPOSName);
            lblUserCode.setText(clsGlobalVarClass.gUserCode);
            lblModuleName.setText(clsGlobalVarClass.gSelectedModule);

            lblDate.setText(clsGlobalVarClass.gPOSDateToDisplay);
            cmbPosCode.addItem("All                                         !All");
            String sql_POS = "select strPosName,strPosCode from tblposmaster";
            ResultSet rsPOS = clsGlobalVarClass.dbMysql.executeResultSet(sql_POS);
            while (rsPOS.next())
            {
                cmbPosCode.addItem(rsPOS.getString(1) + "                                         !" + rsPOS.getString(2));
            }
            rsPOS.close();
            /*liveTableItems=new String[]{"tblbillhd","tblbilldtl","tblbillsettlementdtl","tblbillmodifierdtl","tblbilldiscdtl"
             ,"tblbilltaxdtl","tbladvbookbillhd","tbladvbookbilldtl","tbladvancereceiptdtl","tbladvancereceipthd"
             ,"tbladvordermodifierdtl","tblhomedelivery","tblhomedeldtl","tblbillpromotiondtl","tblcrmpoints"
             ,"tblbillcomplementrydtl,tblvoidbillhd,tblvoidbilldtl,tblvoidmodifierdtl,tblvoidkot"};*/

            liveTableItems = new String[]
            {
                "tblbillhd", "tblbilldtl", "tblbillsettlementdtl", "tblbillmodifierdtl", "tblbilldiscdtl", "tblbilltaxdtl", "tbladvbookbillhd", "tbladvbookbilldtl", "tbladvancereceiptdtl", "tbladvancereceipthd", "tbladvordermodifierdtl", "tblhomedelivery", "tblhomedeldtl", "tblbillpromotiondtl", "tblcrmpoints", "tblbillcomplementrydtl", "Audit", "tbldayendprocess", "tblqcreditbillreceipthd"
            };

            qFileTableItems = new String[]
            {
                "tblqbillhd", "tblqbilldtl", "tblqbillsettlementdtl", "tblqbillmodifierdtl", "tblqbilldiscdtl", "tblqbilltaxdtl", "tblqbillpromotiondtl", "tblqadvbookbillhd", "tblqadvbookbilldtl", "tblqadvancereceiptdtl", "tblqadvancereceipthd", "tblqadvordermodifierdtl", "tblqbillcomplementrydtl", "tbldayendprocess", "tblqcreditbillreceipthd"
            };
            liveTableModel = new DefaultComboBoxModel(liveTableItems);
            qFileTableModel = new DefaultComboBoxModel(qFileTableItems);
            cmbTableNames.setModel(liveTableModel);
            selectQuery = new StringBuilder();
            selectQuery.setLength(0);
            funCreateTable();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void funSubmitButtonClicked()
    {
        if (dteFromDate.getDate() == null)
        {
            JOptionPane.showMessageDialog(null, "Invalid FromDate");
            return;
        }
        if (dteToDate.getDate() == null)
        {
            JOptionPane.showMessageDialog(null, "Invalid ToDate");
            return;
        }
        if (dteToDate.getDate().before(dteFromDate.getDate()))
        {
            JOptionPane.showMessageDialog(null, "Invalid ToDate");
            return;
        }
        dtmDataPostModel.setRowCount(0);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        strFromDate = dateFormat.format(dteFromDate.getDate());
        strToDate = dateFormat.format(dteToDate.getDate());
        posCodeNposName = cmbPosCode.getSelectedItem().toString().split("!");
        posCode = posCodeNposName[1].trim();

        clsUtility objUtility = new clsUtility();
        long diff = objUtility.funCompareDate(strFromDate, strToDate);
        long diffDays = diff / (24 * 60 * 60 * 1000);
        if (diffDays == 0)
        {
            funCreateDynamicQuery();
        }
        funCreateDynamicQuery();
    }

    private void funCreateTable()
    {
        tblDataPostTable = new javax.swing.JTable();
        tblDataPostTable.setRowHeight(25);
        dtmDataPostModel = new javax.swing.table.DefaultTableModel(
                new Object[][]
                {
                },
                new String[]
                {
                    "Bill No", "Bill Date", "Grand Total"
                }
        )
        {
            Class[] types = new Class[]
            {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean[]
            {
                false, false, false
            };

            public Class getColumnClass(int columnIndex)
            {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return canEdit[columnIndex];
            }
        };
        tblDataPostTable.setModel(dtmDataPostModel);
        tblDataPostTable.getTableHeader().setReorderingAllowed(false);
        scrollPaneForTable.setViewportView(tblDataPostTable);
    }

    private void funCreateDynamicQuery()
    {
        if (cmbRecordType.getSelectedItem().toString().trim().equalsIgnoreCase("Live"))
        {
            funCreateQueryForLiveData();
        }
        else if (cmbRecordType.getSelectedItem().toString().trim().equalsIgnoreCase("QFile"))
        {
            funCreateQueryForQFileData();
        }
    }

    private void funCreateQueryForLiveData()
    {
        String dataPostFlag = "N";
        if (chkPostedData.isSelected())
        {
            dataPostFlag = "Y";
        }

        String tableName = cmbTableNames.getSelectedItem().toString().trim();
        ResultSet resultSet = null;
        List<ArrayList> dataList = new ArrayList<ArrayList>();
        dataList.clear();
        switch (tableName)
        {
            case "tblbillhd":
                selectQuery.setLength(0);
                selectQuery.append("select strBillNo,dteBillDate,dblGrandTotal "
                        + "from tblbillhd "
                        + "where date(dteBillDate) between '" + strFromDate + "' and '" + strToDate + "' "
                        + "and strDataPostFlag='" + dataPostFlag + "' ");
                if (cmbPosCode.getSelectedIndex() > 0)
                {
                    selectQuery.append(" and strPOSCode='" + posCode + "' ");
                }
                final int billHdSize = 4;
                try
                {
                    resultSet = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery.toString());
                    while (resultSet.next())
                    {
                        ArrayList rowData = new ArrayList();
                        rowData.add(resultSet.getString("strBillNo"));
                        rowData.add(resultSet.getString("dteBillDate"));
                        rowData.add(resultSet.getString("dblGrandTotal"));
                        rowData.add(true);
                        dataList.add(rowData);
                    }
                    resultSet.close();
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
                Object billHdData[][] = new Object[dataList.size()][billHdSize];
                Iterator<ArrayList> billHdRowIt = dataList.iterator();
                for (int row = 0; billHdRowIt.hasNext(); row++)
                {
                    ArrayList listRow = billHdRowIt.next();
                    Iterator billHdColIt = listRow.iterator();
                    for (int col = 0; billHdColIt.hasNext(); col++)
                    {
                        billHdData[row][col] = billHdColIt.next();
                    }
                }
                String[] billHdColumns = new String[]
                {
                    "Bill No", "Bill Date", "Grand Total", "Select"
                };
                funGenerateTable(billHdData, billHdColumns, billHdSize);
                break;

            case "tblbilldtl":
                selectQuery.setLength(0);
                selectQuery.append("select b.*, a.dteBillDate "
                        + "from tblbillhd a,tblbilldtl b "
                        + "where a.strBillNo=b.strBillNo and date(a.dteBillDate) between '" + strFromDate + "' and '" + strToDate + "'  ");
                if (cmbPosCode.getSelectedIndex() > 0)
                {
                    selectQuery.append(" and a.strPOSCode='" + posCode + "' ");
                }
                selectQuery.append(" and b.strDataPostFlag='" + dataPostFlag + "' ");
                final int billDtlSize = 6;
                try
                {
                    resultSet = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery.toString());
                    while (resultSet.next())
                    {
                        ArrayList rowData = new ArrayList();
                        rowData.add(resultSet.getString("strBillNo"));
                        rowData.add(resultSet.getString("dteBillDate"));
                        rowData.add(resultSet.getString("strItemCode"));
                        rowData.add(resultSet.getString("strItemName"));
                        rowData.add(resultSet.getString("dblAmount"));
                        rowData.add(true);
                        dataList.add(rowData);
                    }
                    resultSet.close();
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
                Object billDtlData[][] = new Object[dataList.size()][billDtlSize];
                Iterator<ArrayList> billDtlRowIt = dataList.iterator();
                for (int row = 0; billDtlRowIt.hasNext(); row++)
                {
                    ArrayList listRow = billDtlRowIt.next();
                    Iterator billDtlColIt = listRow.iterator();
                    for (int col = 0; billDtlColIt.hasNext(); col++)
                    {
                        billDtlData[row][col] = billDtlColIt.next();
                    }
                }
                String[] billDtlColumns = new String[]
                {
                    "Bill No", "Bill Date", "Item Code", "Item Name", "Amount", "Select"
                };
                funGenerateTable(billDtlData, billDtlColumns, billDtlSize);
                break;

            case "tblbilldiscdtl":
                selectQuery.setLength(0);
                selectQuery.append("select b.*, a.dteBillDate "
                        + "from tblbillhd a,tblbilldiscdtl b "
                        + "where a.strBillNo=b.strBillNo and date(a.dteBillDate) between '" + strFromDate + "' and '" + strToDate + "'  ");
                if (cmbPosCode.getSelectedIndex() > 0)
                {
                    selectQuery.append(" and a.strPOSCode='" + posCode + "' ");
                }
                selectQuery.append(" and b.strDataPostFlag='" + dataPostFlag + "' ");
                final int billDiscDtlSize = 6;
                try
                {
                    resultSet = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery.toString());
                    while (resultSet.next())
                    {
                        ArrayList rowData = new ArrayList();
                        rowData.add(resultSet.getString("strBillNo"));
                        rowData.add(resultSet.getString("strDiscOnType"));
                        rowData.add(resultSet.getString("dblDiscOnAmt"));
                        rowData.add(resultSet.getString("dblDiscAmt"));
                        rowData.add(resultSet.getString("dblDiscPer"));
                        rowData.add(true);
                        dataList.add(rowData);
                    }
                    resultSet.close();
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
                Object billDiscDtlData[][] = new Object[dataList.size()][billDiscDtlSize];
                Iterator<ArrayList> billDiscDtlRowIt = dataList.iterator();
                for (int row = 0; billDiscDtlRowIt.hasNext(); row++)
                {
                    ArrayList listRow = billDiscDtlRowIt.next();
                    Iterator billDiscDtlColIt = listRow.iterator();
                    for (int col = 0; billDiscDtlColIt.hasNext(); col++)
                    {
                        billDiscDtlData[row][col] = billDiscDtlColIt.next();
                    }
                }
                String[] billDiscDtlColumns = new String[]
                {
                    "Bill No", "Disc Type", "Amount", "Disc Amt", "Disc Per", "Select"
                };
                funGenerateTable(billDiscDtlData, billDiscDtlColumns, billDiscDtlSize);
                break;

            case "tblbillsettlementdtl":

                selectQuery.setLength(0);
                selectQuery.append("select b.*, a.dteBillDate from tblbillhd a,tblbillsettlementdtl b "
                        + "where a.strBillNo=b.strBillNo and date(a.dteBillDate) between '" + strFromDate + "' and '" + strToDate + "'  ");
                if (cmbPosCode.getSelectedIndex() > 0)
                {
                    selectQuery.append(" and a.strPOSCode='" + posCode + "' ");
                }
                selectQuery.append(" and b.strDataPostFlag='" + dataPostFlag + "' ");
                ////System.out.println("tblbillsettlementdtl-->" + selectQuery);
                final int billSettlDtlSize = 4;
                try
                {
                    resultSet = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery.toString());

                    while (resultSet.next())
                    {
                        ArrayList rowData = new ArrayList();
                        rowData.add(resultSet.getString("strBillNo"));
                        rowData.add(resultSet.getString("dblSettlementAmt"));
                        rowData.add(resultSet.getString("dblrefundAmt"));
                        rowData.add(true);
                        dataList.add(rowData);
                    }
                    resultSet.close();
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
                Object billSettlDtlData[][] = new Object[dataList.size()][billSettlDtlSize];
                Iterator<ArrayList> billSettlDtlRowIt = dataList.iterator();
                for (int row = 0; billSettlDtlRowIt.hasNext(); row++)
                {
                    ArrayList listRow = billSettlDtlRowIt.next();
                    Iterator billSettlDtlColIt = listRow.iterator();
                    for (int col = 0; billSettlDtlColIt.hasNext(); col++)
                    {
                        billSettlDtlData[row][col] = billSettlDtlColIt.next();
                    }
                }
                String[] billSettlDtlColumns = new String[]
                {
                    "Bill No", "Settlement Amount", "Refund Amount", "Select"
                };
                funGenerateTable(billSettlDtlData, billSettlDtlColumns, billSettlDtlSize);
                break;

            case "tblbillmodifierdtl":

                selectQuery.setLength(0);
                selectQuery.append("select b.*, a.dteBillDate from tblbillhd a,tblbillmodifierdtl b "
                        + "where a.strBillNo=b.strBillNo and date(a.dteBillDate) between '" + strFromDate + "' and '" + strToDate + "'  ");
                if (cmbPosCode.getSelectedIndex() > 0)
                {
                    selectQuery.append(" and a.strPOSCode='" + posCode + "' ");
                }
                selectQuery.append(" and b.strDataPostFlag='" + dataPostFlag + "' ");
                //System.out.println("tblbillmodifierdtl-->" + selectQuery);
                final int billModiDtlSize = 5;
                try
                {
                    resultSet = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery.toString());
                    while (resultSet.next())
                    {
                        ArrayList rowData = new ArrayList();
                        rowData.add(resultSet.getString("strBillNo"));
                        rowData.add(resultSet.getString("strModifierCode"));
                        rowData.add(resultSet.getString("strModifierName"));
                        rowData.add(resultSet.getString("dblAmount"));
                        rowData.add(true);
                        dataList.add(rowData);
                    }
                    resultSet.close();
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
                Object billModiDtlData[][] = new Object[dataList.size()][billModiDtlSize];
                Iterator<ArrayList> billModiDtlRowIt = dataList.iterator();
                for (int row = 0; billModiDtlRowIt.hasNext(); row++)
                {
                    ArrayList listRow = billModiDtlRowIt.next();
                    Iterator billModiDtlColIt = listRow.iterator();
                    for (int col = 0; billModiDtlColIt.hasNext(); col++)
                    {
                        billModiDtlData[row][col] = billModiDtlColIt.next();
                    }
                }
                String[] billModiDtlColumns = new String[]
                {
                    "Bill No", "Modifier Code", "Modifier Name", "Amount", "Select"
                };
                funGenerateTable(billModiDtlData, billModiDtlColumns, billModiDtlSize);
                break;

            case "tblbilltaxdtl":
                selectQuery.setLength(0);
                selectQuery.append("select b.*, a.dteBillDate from tblbillhd a,tblbilltaxdtl b "
                        + "where a.strBillNo=b.strBillNo and date(a.dteBillDate) between '" + strFromDate + "' and '" + strToDate + "'  ");
                if (cmbPosCode.getSelectedIndex() > 0)
                {
                    selectQuery.append(" and a.strPOSCode='" + posCode + "' ");
                }
                selectQuery.append(" and b.strDataPostFlag='" + dataPostFlag + "' ");
                //System.out.println("tblbilltaxdtl-->" + selectQuery);
                final int billTaxDtlSize = 5;
                try
                {
                    resultSet = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery.toString());
                    while (resultSet.next())
                    {
                        ArrayList rowData = new ArrayList();
                        rowData.add(resultSet.getString("strBillNo"));
                        rowData.add(resultSet.getString("strTaxCode"));
                        rowData.add(resultSet.getString("dblTaxableAmount"));
                        rowData.add(resultSet.getString("dblTaxAmount"));
                        rowData.add(true);
                        dataList.add(rowData);
                    }
                    resultSet.close();
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
                Object billTaxDtlData[][] = new Object[dataList.size()][billTaxDtlSize];
                Iterator<ArrayList> billTaxDtlRowIt = dataList.iterator();
                for (int row = 0; billTaxDtlRowIt.hasNext(); row++)
                {
                    ArrayList listRow = billTaxDtlRowIt.next();
                    Iterator billTaxDtlColIt = listRow.iterator();
                    for (int col = 0; billTaxDtlColIt.hasNext(); col++)
                    {
                        billTaxDtlData[row][col] = billTaxDtlColIt.next();
                    }
                }
                String[] billTaxDtlColumns = new String[]
                {
                    "Bill No", "Tax Code", "Taxable Amount", "Tax Amount", "Select"
                };
                funGenerateTable(billTaxDtlData, billTaxDtlColumns, billTaxDtlSize);
                break;

            case "tbladvbookbillhd":
                selectQuery.setLength(0);
                selectQuery.append("select b.*, a.dteBillDate from tblbillhd a,tbladvbookbillhd b "
                        + "where a.strAdvBookingNo=b.strAdvBookingNo and date(a.dteBillDate) between '" + strFromDate + "' and '" + strToDate + "'    ");
                if (cmbPosCode.getSelectedIndex() > 0)
                {
                    selectQuery.append(" and a.strPOSCode='" + posCode + "' ");
                }
                selectQuery.append(" and b.strDataPostFlag='" + dataPostFlag + "' ");
                //System.out.println("tbladvbookbillhd-->" + selectQuery);
                final int advBoolBillHdSize = 4;
                try
                {
                    resultSet = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery.toString());
                    while (resultSet.next())
                    {
                        ArrayList rowData = new ArrayList();
                        rowData.add(resultSet.getString("strAdvBookingNo"));
                        rowData.add(resultSet.getString("dteAdvBookingDate"));
                        rowData.add(resultSet.getString("dblGrandTotal"));
                        rowData.add(true);
                        dataList.add(rowData);
                    }
                    resultSet.close();
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
                Object advBookBillHdData[][] = new Object[dataList.size()][advBoolBillHdSize];
                Iterator<ArrayList> advBookBillHdRowIt = dataList.iterator();
                for (int row = 0; advBookBillHdRowIt.hasNext(); row++)
                {
                    ArrayList listRow = advBookBillHdRowIt.next();
                    Iterator advBookBillHdColIt = listRow.iterator();
                    for (int col = 0; advBookBillHdColIt.hasNext(); col++)
                    {
                        advBookBillHdData[row][col] = advBookBillHdColIt.next();
                    }
                }
                String[] advBookBillHdColumns = new String[]
                {
                    "Advance Booking No", "Advance Booking Date", "Grand Total", "Select"
                };
                funGenerateTable(advBookBillHdData, advBookBillHdColumns, advBoolBillHdSize);
                break;

            case "tbladvbookbilldtl":
                selectQuery.setLength(0);
                selectQuery.append("select b.*, a.dteBillDate from tblbillhd a,tbladvbookbilldtl b "
                        + "where a.strAdvBookingNo=b.strAdvBookingNo and date(a.dteBillDate) between '" + strFromDate + "' and '" + strToDate + "'  ");
                if (cmbPosCode.getSelectedIndex() > 0)
                {
                    selectQuery.append(" and a.strPOSCode='" + posCode + "' ");
                }
                selectQuery.append(" and b.strDataPostFlag='" + dataPostFlag + "' ");
                //System.out.println("tbladvbookbilldtl-->" + selectQuery);
                final int advBookBillDtlSize = 6;
                try
                {
                    resultSet = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery.toString());
                    while (resultSet.next())
                    {
                        ArrayList rowData = new ArrayList();
                        rowData.add(resultSet.getString("strAdvBookingNo"));
                        rowData.add(resultSet.getString("dteAdvBookingDate"));
                        rowData.add(resultSet.getString("strItemCode"));
                        rowData.add(resultSet.getString("strItemName"));
                        rowData.add(resultSet.getString("dblAmount"));
                        rowData.add(true);
                        dataList.add(rowData);
                    }
                    resultSet.close();
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
                Object advBookBillDtlData[][] = new Object[dataList.size()][advBookBillDtlSize];
                Iterator<ArrayList> advBookBillDtlRowIt = dataList.iterator();
                for (int row = 0; advBookBillDtlRowIt.hasNext(); row++)
                {
                    ArrayList listRow = advBookBillDtlRowIt.next();
                    Iterator advBookBillDtlColIt = listRow.iterator();
                    for (int col = 0; advBookBillDtlColIt.hasNext(); col++)
                    {
                        advBookBillDtlData[row][col] = advBookBillDtlColIt.next();
                    }
                }
                String[] advBookBillDtlColumns = new String[]
                {
                    "Advance Booking No", "Advance Booking Date", "Item Code", "Item Name", "Amount", "Select"
                };
                funGenerateTable(advBookBillDtlData, advBookBillDtlColumns, advBookBillDtlSize);
                break;

            case "tbladvancereceiptdtl":

                selectQuery.setLength(0);
                selectQuery.append("select c.* ,a.dteBillDate from tblbillhd a,tbladvancereceipthd b,tbladvancereceiptdtl c "
                        + "where a.strAdvBookingNo=b.strAdvBookingNo and b.strReceiptNo=c.strReceiptNo  and date(a.dteBillDate) between '" + strFromDate + "' and '" + strToDate + "'  ");
                if (cmbPosCode.getSelectedIndex() > 0)
                {
                    selectQuery.append(" and a.strPOSCode='" + posCode + "' ");
                }
                selectQuery.append(" and c.strDataPostFlag='" + dataPostFlag + "' ");
                //System.out.println("tbladvancereceiptdtl-->" + selectQuery);
                final int advReceiptDtlSize = 5;
                try
                {
                    resultSet = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery.toString());

                    while (resultSet.next())
                    {
                        ArrayList rowData = new ArrayList();

                        rowData.add(resultSet.getString("strReceiptNo"));
                        rowData.add(resultSet.getString("strExpirydate"));
                        rowData.add(resultSet.getString("dblAdvDepositesettleAmt"));
                        rowData.add(resultSet.getString("dblPaidAmt"));
                        rowData.add(true);

                        dataList.add(rowData);
                    }
                    resultSet.close();
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
                Object advReceiptDtlData[][] = new Object[dataList.size()][advReceiptDtlSize];
                Iterator<ArrayList> advReceiptDtlRowIt = dataList.iterator();
                for (int row = 0; advReceiptDtlRowIt.hasNext(); row++)
                {
                    ArrayList listRow = advReceiptDtlRowIt.next();
                    Iterator advReceiptDtlColIt = listRow.iterator();
                    for (int col = 0; advReceiptDtlColIt.hasNext(); col++)
                    {

                        advReceiptDtlData[row][col] = advReceiptDtlColIt.next();
                    }
                }
                String[] advReceiptDtlColumns = new String[]
                {
                    "Receipt No", "Expiry Date", "Advance Deposite Amount", "Paid Amount", "Select"
                };
                funGenerateTable(advReceiptDtlData, advReceiptDtlColumns, advReceiptDtlSize);

                break;
            case "tbladvancereceipthd":

                selectQuery.setLength(0);
                selectQuery.append("select c.* from tblbillhd a,tbladvbookbillhd b,tbladvancereceipthd c "
                        + "where a.strAdvBookingNo=b.strAdvBookingNo and b.strAdvBookingNo=c.strAdvBookingNo  and date(a.dteBillDate) between '" + strFromDate + "' and '" + strToDate + "'  ");
                if (cmbPosCode.getSelectedIndex() > 0)
                {
                    selectQuery.append(" and a.strPOSCode='" + posCode + "' ");
                }
                selectQuery.append(" and c.strDataPostFlag='" + dataPostFlag + "' ");
                //System.out.println("tbladvancereceipthd-->" + selectQuery);
                final int advReceiptHdSize = 5;
                try
                {
                    resultSet = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery.toString());

                    while (resultSet.next())
                    {
                        ArrayList rowData = new ArrayList();

                        rowData.add(resultSet.getString("strReceiptNo"));
                        rowData.add(resultSet.getString("strAdvBookingNo"));
                        rowData.add(resultSet.getString("dtReceiptDate"));
                        rowData.add(resultSet.getString("dblAdvDeposite"));
                        rowData.add(true);

                        dataList.add(rowData);
                    }
                    resultSet.close();
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
                Object advReceiptHdData[][] = new Object[dataList.size()][advReceiptHdSize];
                Iterator<ArrayList> advReceiptHdRowIt = dataList.iterator();
                for (int row = 0; advReceiptHdRowIt.hasNext(); row++)
                {
                    ArrayList listRow = advReceiptHdRowIt.next();
                    Iterator advReceiptHdColIt = listRow.iterator();
                    for (int col = 0; advReceiptHdColIt.hasNext(); col++)
                    {

                        advReceiptHdData[row][col] = advReceiptHdColIt.next();
                    }
                }
                String[] advReceiptHdColumns = new String[]
                {
                    "Receipt No", "Expiry Date", "Advance Deposite Amount", "Paid Amount", "Select"
                };

                funGenerateTable(advReceiptHdData, advReceiptHdColumns, advReceiptHdSize);
                break;

            case "tbladvordermodifierdtl":

                selectQuery.setLength(0);
                selectQuery.append("select c.*, a.dteBillDate from tblbillhd a,tbladvbookbillhd b,tbladvordermodifierdtl c  "
                        + "where a.strAdvBookingNo=b.strAdvBookingNo and b.strAdvBookingNo=c.strAdvOrderNo and date(a.dteBillDate) between '" + strFromDate + "' and '" + strToDate + "'  ");
                if (cmbPosCode.getSelectedIndex() > 0)
                {
                    selectQuery.append(" and a.strPOSCode='" + posCode + "' ");
                }
                selectQuery.append(" and c.strDataPostFlag='" + dataPostFlag + "' ");
                //System.out.println("tbladvordermodifierdtl-->" + selectQuery);
                final int advOrdModiDtlSize = 5;
                try
                {
                    resultSet = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery.toString());

                    while (resultSet.next())
                    {
                        ArrayList rowData = new ArrayList();

                        rowData.add(resultSet.getString("strAdvOrderNo"));
                        rowData.add(resultSet.getString("strModifierCode"));
                        rowData.add(resultSet.getString("strModifierName"));
                        rowData.add(resultSet.getString("dblAmount"));
                        rowData.add(true);

                        dataList.add(rowData);
                    }
                    resultSet.close();
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
                Object advOrdModiDtlData[][] = new Object[dataList.size()][advOrdModiDtlSize];
                Iterator<ArrayList> advOrdModiDtlRowIt = dataList.iterator();
                for (int row = 0; advOrdModiDtlRowIt.hasNext(); row++)
                {
                    ArrayList listRow = advOrdModiDtlRowIt.next();
                    Iterator advOrdModiDtlColIt = listRow.iterator();
                    for (int col = 0; advOrdModiDtlColIt.hasNext(); col++)
                    {

                        advOrdModiDtlData[row][col] = advOrdModiDtlColIt.next();
                    }
                }
                String[] advOdModiDtlColumns = new String[]
                {
                    "Receipt No", "Expiry Date", "Advance Deposite Amount", "Paid Amount", "Select"
                };
                funGenerateTable(advOrdModiDtlData, advOdModiDtlColumns, advOrdModiDtlSize);
                break;

            case "tblhomedelivery":

                selectQuery.setLength(0);
                selectQuery.append("select b.*, a.dteBillDate from tblbillhd a,tblhomedelivery b "
                        + "where a.strBillNo=b.strBillNo and date(a.dteBillDate) between '" + strFromDate + "' and '" + strToDate + "'  ");
                if (cmbPosCode.getSelectedIndex() > 0)
                {
                    selectQuery.append(" and a.strPOSCode='" + posCode + "' ");
                }
                selectQuery.append(" and b.strDataPostFlag='" + dataPostFlag + "' ");
                final int homeDeliSize = 3;
                try
                {
                    resultSet = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery.toString());
                    while (resultSet.next())
                    {
                        ArrayList rowData = new ArrayList();
                        rowData.add(resultSet.getString("strBillNo"));
                        rowData.add(resultSet.getString("dteDate"));
                        rowData.add(true);
                        dataList.add(rowData);
                    }
                    resultSet.close();
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
                Object homeDeliData[][] = new Object[dataList.size()][homeDeliSize];
                Iterator<ArrayList> homeDeliRowIt = dataList.iterator();
                for (int row = 0; homeDeliRowIt.hasNext(); row++)
                {
                    ArrayList listRow = homeDeliRowIt.next();
                    Iterator homeDeliColIt = listRow.iterator();
                    for (int col = 0; homeDeliColIt.hasNext(); col++)
                    {

                        homeDeliData[row][col] = homeDeliColIt.next();
                    }
                }
                String[] homeDeliColumns = new String[]
                {
                    "Bill No", "Date", "Select"
                };
                funGenerateTable(homeDeliData, homeDeliColumns, homeDeliSize);
                break;

            case "tblbillpromotiondtl":

                selectQuery.setLength(0);
                selectQuery.append("select b.*, a.dteBillDate from tblbillhd a ,tblbillpromotiondtl b "
                        + " where date(a.dteBillDate) BETWEEN '" + strFromDate + "' and '" + strToDate + "' ");
                if (cmbPosCode.getSelectedIndex() > 0)
                {
                    selectQuery.append(" and a.strPOSCode='" + posCode + "' ");
                }
                selectQuery.append(" and b.strDataPostFlag='" + dataPostFlag + "' ");
                final int promoDtlSize = 6;
                try
                {
                    resultSet = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery.toString());

                    while (resultSet.next())
                    {
                        ArrayList rowData = new ArrayList();

                        rowData.add(resultSet.getString(1));
                        rowData.add(resultSet.getString(2));
                        rowData.add(resultSet.getString(10));
                        rowData.add(resultSet.getString(4));
                        rowData.add(resultSet.getString(9));
                        rowData.add(true);

                        dataList.add(rowData);
                    }
                    resultSet.close();
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
                Object promoDtlData[][] = new Object[dataList.size()][promoDtlSize];
                Iterator<ArrayList> promoDtlRowIt = dataList.iterator();
                for (int row = 0; promoDtlRowIt.hasNext(); row++)
                {
                    ArrayList listRow = promoDtlRowIt.next();
                    Iterator promoDtlColIt = listRow.iterator();
                    for (int col = 0; promoDtlColIt.hasNext(); col++)
                    {

                        promoDtlData[row][col] = promoDtlColIt.next();
                    }
                }
                String[] promoDtlColumns = new String[]
                {
                    "Bill No", "Item Code", "Bill Date", "Quantity", "Amount"
                };
                funGenerateTable(promoDtlData, promoDtlColumns, promoDtlSize);
                break;

            case "tblbillcomplementrydtl":
                selectQuery.setLength(0);
                selectQuery.append("select b.*, a.dteBillDate "
                        + "from tblbillhd a,tblbillcomplementrydtl b "
                        + "where a.strBillNo=b.strBillNo and date(a.dteBillDate) between '" + strFromDate + "' and '" + strToDate + "'  ");
                if (cmbPosCode.getSelectedIndex() > 0)
                {
                    selectQuery.append(" and a.strPOSCode='" + posCode + "' ");
                }
                selectQuery.append(" and b.strDataPostFlag='" + dataPostFlag + "' ");
                final int billComplDtlSize = 6;
                try
                {
                    resultSet = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery.toString());
                    while (resultSet.next())
                    {
                        ArrayList rowData = new ArrayList();
                        rowData.add(resultSet.getString("strBillNo"));
                        rowData.add(resultSet.getString("dteBillDate"));
                        rowData.add(resultSet.getString("strItemCode"));
                        rowData.add(resultSet.getString("strItemName"));
                        rowData.add(resultSet.getString("dblAmount"));
                        rowData.add(true);
                        dataList.add(rowData);
                    }
                    resultSet.close();
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
                Object billComplDtlData[][] = new Object[dataList.size()][billComplDtlSize];
                Iterator<ArrayList> billComplDtlRowIt = dataList.iterator();
                for (int row = 0; billComplDtlRowIt.hasNext(); row++)
                {
                    ArrayList listRow = billComplDtlRowIt.next();
                    Iterator billDtlColIt = listRow.iterator();
                    for (int col = 0; billDtlColIt.hasNext(); col++)
                    {
                        billComplDtlData[row][col] = billDtlColIt.next();
                    }
                }
                String[] billComplDtlColumns = new String[]
                {
                    "Bill No", "Bill Date", "Item Code", "Item Name", "Amount", "Select"
                };
                funGenerateTable(billComplDtlData, billComplDtlColumns, billComplDtlSize);
                break;

            case "tblcrmpoints":

                selectQuery.setLength(0);
                selectQuery.append("select b.*, a.dteBillDate from tblbillhd a,tblcrmpoints b "
                        + "where a.strBillNo=b.strBillNo and date(a.dteBillDate) between '" + strFromDate + "' and '" + strToDate + "'  ");
                if (cmbPosCode.getSelectedIndex() > 0)
                {
                    selectQuery.append(" and a.strPOSCode='" + posCode + "' ");
                }
                selectQuery.append(" and b.strDataPostFlag='" + dataPostFlag + "' ");
                final int crmPointsSize = 6;
                try
                {
                    resultSet = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery.toString());

                    while (resultSet.next())
                    {
                        ArrayList rowData = new ArrayList();

                        rowData.add(resultSet.getString("strBillNo"));
                        rowData.add(resultSet.getString("dteBillDate"));
                        rowData.add(resultSet.getString("dblPoints"));
                        rowData.add(resultSet.getString("dblRedeemedAmt"));
                        rowData.add(resultSet.getString("dblValue"));
                        rowData.add(true);

                        dataList.add(rowData);
                    }
                    resultSet.close();
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
                Object crmPointsData[][] = new Object[dataList.size()][crmPointsSize];
                Iterator<ArrayList> crmPointsRowIt = dataList.iterator();
                for (int row = 0; crmPointsRowIt.hasNext(); row++)
                {
                    ArrayList listRow = crmPointsRowIt.next();
                    Iterator crmPointsColIt = listRow.iterator();
                    for (int col = 0; crmPointsColIt.hasNext(); col++)
                    {
                        crmPointsData[row][col] = crmPointsColIt.next();
                    }
                }
                String[] crmPointsColumns = new String[]
                {
                    "Bill No", "Bill Date", "points", "Redeemed Amount", "Value", "Select"
                };
                funGenerateTable(crmPointsData, crmPointsColumns, crmPointsSize);
                break;

            case "tblvoidbillhd":
                selectQuery.setLength(0);
                selectQuery.append("select b.*, a.dteBillDate from tblvoidbillhd a "
                        + "where date(a.dteBillDate) between '" + strFromDate + "' and '" + strToDate + "'  ");
                if (cmbPosCode.getSelectedIndex() > 0)
                {
                    selectQuery.append(" and a.strPOSCode='" + posCode + "' ");
                }
                selectQuery.append(" and b.strDataPostFlag='" + dataPostFlag + "' ");
                //System.out.println("tblbilltaxdtl-->" + selectQuery);
                final int voidBillSize = 5;
                try
                {
                    resultSet = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery.toString());
                    while (resultSet.next())
                    {
                        ArrayList rowData = new ArrayList();
                        rowData.add(resultSet.getString("strBillNo"));
                        rowData.add(resultSet.getString("dteBillDate"));
                        rowData.add(resultSet.getString("strPosCode"));
                        rowData.add(resultSet.getString("strTableNo"));
                        rowData.add(resultSet.getString("strWaiterNo"));
                        rowData.add(resultSet.getString("dblActualAmount"));
                        rowData.add(resultSet.getString("dblModifiedAmount"));
                        rowData.add(resultSet.getString("strTransType"));

                        rowData.add(true);
                        dataList.add(rowData);
                    }
                    resultSet.close();
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
                Object voidBillData[][] = new Object[dataList.size()][voidBillSize];
                Iterator<ArrayList> voidBillRowIt = dataList.iterator();
                for (int row = 0; voidBillRowIt.hasNext(); row++)
                {
                    ArrayList listRow = voidBillRowIt.next();
                    Iterator voidBillColIt = listRow.iterator();
                    for (int col = 0; voidBillColIt.hasNext(); col++)
                    {
                        voidBillData[row][col] = voidBillColIt.next();
                    }
                }
                String[] voidBillColumns = new String[]
                {
                    "Bill No", "Bill Date", "POS", "Table", "Waiter", "Act Amt", "Modified Amt", "Select"
                };
                funGenerateTable(voidBillData, voidBillColumns, voidBillSize);
                break;

            case "tbldayendprocess":
                selectQuery.setLength(0);
                selectQuery.append("select b.strPosName,a.*,if(a.dteDayEndDateTime='0000-00-00 00:00:00','NA',a.dteDayEndDateTime) as dayEndDateTime "
                        + "from tbldayendprocess a,tblposmaster b "
                        + "where  a.strPOSCode=b.strPosCode "
                        + "  ");
//                if (cmbPosCode.getSelectedIndex() > 0)
//                {
//                    selectQuery.append(" and a.strPOSCode='" + posCode + "' ");
//                }
                selectQuery.append(" and a.strDataPostFlag='" + dataPostFlag + "' ");//and a.strDayEnd='Y' 

                System.out.println("day end ->" + selectQuery);

                final int colSize = 40;
                try
                {
                    resultSet = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery.toString());
                    while (resultSet.next())
                    {
                        ArrayList rowData = new ArrayList();
                        rowData.add(resultSet.getString("strPosName"));
                        rowData.add(resultSet.getString("dtePOSDate"));
                        rowData.add(resultSet.getString("strDayEnd"));
                        rowData.add(resultSet.getString("dblTotalSale"));
                        rowData.add(resultSet.getString("dblNoOfBill"));
                        rowData.add(resultSet.getString("dblNoOfVoidedBill"));
                        rowData.add(resultSet.getString("dblNoOfModifyBill"));
                        rowData.add(resultSet.getString("dblHDAmt"));
                        rowData.add(resultSet.getString("dblDiningAmt"));
                        rowData.add(resultSet.getString("dblTakeAway"));
                        rowData.add(resultSet.getString("dblFloat"));
                        rowData.add(resultSet.getString("dblCash"));
                        rowData.add(resultSet.getString("dblAdvance"));
                        rowData.add(resultSet.getString("dblTransferIn"));
                        rowData.add(resultSet.getString("dblTotalReceipt"));
                        rowData.add(resultSet.getString("dblPayments"));
                        rowData.add(resultSet.getString("dblWithdrawal"));
                        rowData.add(resultSet.getString("dblTransferOut"));
                        rowData.add(resultSet.getString("dblTotalPay"));
                        rowData.add(resultSet.getString("dblCashInHand"));
                        rowData.add(resultSet.getString("dblRefund"));
                        rowData.add(resultSet.getString("dblTotalDiscount"));
                        rowData.add(resultSet.getString("dblNoOfDiscountedBill"));
                        rowData.add(resultSet.getString("intShiftCode"));
                        rowData.add(resultSet.getString("strShiftEnd"));
                        rowData.add(resultSet.getString("intTotalPax"));
                        rowData.add(resultSet.getString("intNoOfTakeAway"));
                        rowData.add(resultSet.getString("intNoOfHomeDelivery"));
                        rowData.add(resultSet.getString("strUserCreated"));
                        rowData.add(resultSet.getString("dteDateCreated"));
                        String dayEndDateTime = resultSet.getString("dayEndDateTime");
                        System.out.println("day end date time=" + dayEndDateTime);
                        if (dayEndDateTime.equals("NA"))
                        {
                            rowData.add("0000-00-00 00:00:00");
                        }
                        else
                        {
                            rowData.add(dayEndDateTime);
                        }
                        rowData.add(resultSet.getString("strUserEdited"));
                        rowData.add(resultSet.getString("strDataPostFlag"));
                        rowData.add(resultSet.getString("intNoOfNCKOT"));
                        rowData.add(resultSet.getString("intNoOfComplimentaryKOT"));
                        rowData.add(resultSet.getString("intNoOfVoidKOT"));
                        rowData.add(resultSet.getString("dblUsedDebitCardBalance"));
                        rowData.add(resultSet.getString("dblUnusedDebitCardBalance"));
                        rowData.add(resultSet.getString("strWSStockAdjustmentNo"));
                        rowData.add(true);

                        dataList.add(rowData);
                    }
                    resultSet.close();
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
                Object dayEndData[][] = new Object[dataList.size()][colSize];
                Iterator<ArrayList> dayEndRowIt = dataList.iterator();
                for (int row = 0; dayEndRowIt.hasNext(); row++)
                {
                    ArrayList listRow = dayEndRowIt.next();
                    Iterator dayEndColIt = listRow.iterator();
                    for (int col = 0; dayEndColIt.hasNext(); col++)
                    {
                        dayEndData[row][col] = dayEndColIt.next();
                    }
                }
                String[] dayEndColumns = new String[]
                {
                    "POS", "POS Date", "Day End", "Total Sale", "No Of Bills", "No Of Voided Bills", "No Of Modify Bills", "HD Amt", "Dining Amt", "Take Away",
                    "Float", "Cash", "Advance", "Transfer In", "Total Receipt", "Payments", "Withdrawal", "Transfer Out", "Total Pay", "Cash In Hand",
                    "Refund", "Total Discount", "No Of Discounted Bills", "Shift", "Shift End", "Total Pax", "No Of TakeAway", "No Of HomeDelivery", "User Created", "Date Created",
                    "DayEnd DateTime", "User Edited", "Data PostFlag", "No Of NCKOT", "No Of Complimentary KOT", "No Of Void KOT", "Used DebitCardBalance", "Unused DebitCardBalance", "WS Stock AdjustmentNo", "Select"
                };
                funCreateDayEndTable(dayEndData, dayEndColumns, colSize);
                break;

            case "tblqcreditbillreceipthd":

                selectQuery.setLength(0);
                selectQuery.append("select a.strReceiptNo,a.strBillNo,date(a.dteBillDate)dteBillDate,a.dteReceiptDate,a.dblReceiptAmt"
                        + ",a.strSettlementName,a.strUserEdited  "
                        + "from tblqcreditbillreceipthd  a "
                        + "where date(a.dteReceiptDate) between '" + strFromDate + "' and '" + strToDate + "'  ");
                if (cmbPosCode.getSelectedIndex() > 0)
                {
                    selectQuery.append(" and a.strPOSCode='" + posCode + "' ");
                }
                selectQuery.append(" and a.strDataPostFlag='" + dataPostFlag + "' ");
                //System.out.println("tbladvancereceiptdtl-->" + selectQuery);
                final int creditReceiptDtlSize = 8;
                try
                {
                    resultSet = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery.toString());

                    while (resultSet.next())
                    {
                        ArrayList rowData = new ArrayList();

                        rowData.add(resultSet.getString("strReceiptNo"));
                        rowData.add(resultSet.getString("strBillNo"));
                        rowData.add(resultSet.getString("dteBillDate"));
                        rowData.add(resultSet.getString("dteReceiptDate"));
                        rowData.add(resultSet.getString("dblReceiptAmt"));
                        rowData.add(resultSet.getString("strSettlementName"));
                        rowData.add(resultSet.getString("strUserEdited"));
                        rowData.add(true);

                        dataList.add(rowData);
                    }
                    resultSet.close();
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
                Object creditReceiptDtlData[][] = new Object[dataList.size()][creditReceiptDtlSize];
                Iterator<ArrayList> creditReceiptDtlRowIt = dataList.iterator();
                for (int row = 0; creditReceiptDtlRowIt.hasNext(); row++)
                {
                    ArrayList listRow = creditReceiptDtlRowIt.next();
                    Iterator creditReceiptDtlColIt = listRow.iterator();
                    for (int col = 0; creditReceiptDtlColIt.hasNext(); col++)
                    {
                        creditReceiptDtlData[row][col] = creditReceiptDtlColIt.next();
                    }
                }
                String[] creditReceiptDtlColumns = new String[]
                {
                    "Receipt No", "Bill No", "Bill Date", "Receipt Date", "Receipt Amount", "Settlement", "User", "Select"
                };
                funGenerateTable(creditReceiptDtlData, creditReceiptDtlColumns, creditReceiptDtlSize);

                break;

        }
    }

    private void funCreateQueryForQFileData()
    {
        String dataPostFlag = "N";
        if (chkPostedData.isSelected())
        {
            dataPostFlag = "Y";
        }
        //System.out.println("CreateQueryForQFileData");
        String tableName = cmbTableNames.getSelectedItem().toString().trim();
        ResultSet resultSet = null;
        List<ArrayList> dataList = new ArrayList<ArrayList>();
        dataList.clear();

        switch (tableName)
        {

            case "tblqbillhd":
                selectQuery.setLength(0);
                selectQuery.append("select strBillNo,dteBillDate,dblGrandTotal from tblqbillhd "
                        + "where date(dteBillDate) between '" + strFromDate + "' and '" + strToDate + "' and strDataPostFlag='" + dataPostFlag + "' ");
                if (cmbPosCode.getSelectedIndex() > 0)
                {
                    selectQuery.append(" and strPOSCode='" + posCode + "' ");
                }
                //System.out.println("tblQbillhd-->" + selectQuery);
                final int billHdSize = 4;
                try
                {
                    resultSet = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery.toString());
                    while (resultSet.next())
                    {
                        ArrayList rowData = new ArrayList();
                        rowData.add(resultSet.getString("strBillNo"));
                        rowData.add(resultSet.getString("dteBillDate"));
                        rowData.add(resultSet.getString("dblGrandTotal"));
                        rowData.add(true);
                        dataList.add(rowData);
                    }
                    resultSet.close();
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
                Object billHdData[][] = new Object[dataList.size()][billHdSize];
                Iterator<ArrayList> billHdRowIt = dataList.iterator();
                for (int row = 0; billHdRowIt.hasNext(); row++)
                {
                    ArrayList listRow = billHdRowIt.next();
                    Iterator billHdColIt = listRow.iterator();
                    for (int col = 0; billHdColIt.hasNext(); col++)
                    {
                        billHdData[row][col] = billHdColIt.next();
                    }
                }
                String[] billHdColumns = new String[]
                {
                    "Bill No", "Bill Date", "Grand Total", "Select"
                };
                funGenerateTable(billHdData, billHdColumns, billHdSize);
                break;

            case "tblqbilldtl":

                selectQuery.setLength(0);
                selectQuery.append("select b.*, a.dteBillDate from tblqbillhd a,tblqbilldtl b "
                        + "where a.strBillNo=b.strBillNo and date(a.dteBillDate) between '" + strFromDate + "' and '" + strToDate + "'  ");
                if (cmbPosCode.getSelectedIndex() > 0)
                {
                    selectQuery.append(" and a.strPOSCode='" + posCode + "' ");
                }
                selectQuery.append(" and b.strDataPostFlag='" + dataPostFlag + "' ");
                final int billDtlSize = 6;
                try
                {
                    resultSet = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery.toString());
                    while (resultSet.next())
                    {
                        ArrayList rowData = new ArrayList();
                        rowData.add(resultSet.getString("strBillNo"));
                        rowData.add(resultSet.getString("dteBillDate"));
                        rowData.add(resultSet.getString("strItemCode"));
                        rowData.add(resultSet.getString("strItemName"));
                        rowData.add(resultSet.getString("dblAmount"));
                        rowData.add(true);

                        dataList.add(rowData);
                    }
                    resultSet.close();
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
                Object billDtlData[][] = new Object[dataList.size()][billDtlSize];
                Iterator<ArrayList> billDtlRowIt = dataList.iterator();
                for (int row = 0; billDtlRowIt.hasNext(); row++)
                {
                    ArrayList listRow = billDtlRowIt.next();
                    Iterator billDtlColIt = listRow.iterator();
                    for (int col = 0; billDtlColIt.hasNext(); col++)
                    {

                        billDtlData[row][col] = billDtlColIt.next();
                    }
                }
                String[] billDtlColumns = new String[]
                {
                    "Bill No", "Bill Date", "Item Code", "Item Name", "Amount", "Select"
                };
                funGenerateTable(billDtlData, billDtlColumns, billDtlSize);
                break;

            case "tblqbilldiscdtl":

                selectQuery.setLength(0);
                selectQuery.append("select b.*, a.dteBillDate "
                        + "from tblqbillhd a,tblqbilldiscdtl b "
                        + "where a.strBillNo=b.strBillNo and date(a.dteBillDate) between '" + strFromDate + "' and '" + strToDate + "'  ");
                if (cmbPosCode.getSelectedIndex() > 0)
                {
                    selectQuery.append(" and a.strPOSCode='" + posCode + "' ");
                }
                selectQuery.append(" and b.strDataPostFlag='" + dataPostFlag + "' ");
                final int billDiscDtlSize = 6;
                try
                {
                    resultSet = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery.toString());
                    while (resultSet.next())
                    {
                        ArrayList rowData = new ArrayList();
                        rowData.add(resultSet.getString("strBillNo"));
                        rowData.add(resultSet.getString("strDiscOnType"));
                        rowData.add(resultSet.getString("dblDiscOnAmt"));
                        rowData.add(resultSet.getString("dblDiscAmt"));
                        rowData.add(resultSet.getString("dblDiscPer"));
                        rowData.add(true);
                        dataList.add(rowData);
                    }
                    resultSet.close();
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
                Object billDiscDtlData[][] = new Object[dataList.size()][billDiscDtlSize];
                Iterator<ArrayList> billDiscDtlRowIt = dataList.iterator();
                for (int row = 0; billDiscDtlRowIt.hasNext(); row++)
                {
                    ArrayList listRow = billDiscDtlRowIt.next();
                    Iterator billDiscDtlColIt = listRow.iterator();
                    for (int col = 0; billDiscDtlColIt.hasNext(); col++)
                    {
                        billDiscDtlData[row][col] = billDiscDtlColIt.next();
                    }
                }
                String[] billDiscDtlColumns = new String[]
                {
                    "Bill No", "Disc Type", "Amount", "Disc Amt", "Disc Per", "Select"
                };
                funGenerateTable(billDiscDtlData, billDiscDtlColumns, billDiscDtlSize);
                break;

            case "tblqbillsettlementdtl":

                selectQuery.setLength(0);
                selectQuery.append("select b.*, a.dteBillDate from tblqbillhd a,tblqbillsettlementdtl b "
                        + "where a.strBillNo=b.strBillNo and date(a.dteBillDate) between '" + strFromDate + "' and '" + strToDate + "'  ");
                if (cmbPosCode.getSelectedIndex() > 0)
                {
                    selectQuery.append(" and a.strPOSCode='" + posCode + "' ");
                }
                selectQuery.append(" and b.strDataPostFlag='" + dataPostFlag + "' ");
                //System.out.println("tblqbillsettlementdtl-->" + selectQuery);
                final int billSettlDtlSize = 4;
                try
                {
                    resultSet = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery.toString());
                    while (resultSet.next())
                    {

                        ArrayList rowData = new ArrayList();
                        rowData.add(resultSet.getString("strBillNo"));
                        rowData.add(resultSet.getString("dblSettlementAmt"));
                        rowData.add(resultSet.getString("dblrefundAmt"));
                        rowData.add(true);
                        dataList.add(rowData);
                    }
                    resultSet.close();

                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
                Object billSettlDtlData[][] = new Object[dataList.size()][billSettlDtlSize];
                Iterator<ArrayList> billSettlDtlRowIt = dataList.iterator();
                for (int row = 0; billSettlDtlRowIt.hasNext(); row++)
                {
                    ArrayList listRow = billSettlDtlRowIt.next();
                    Iterator billSettlDtlColIt = listRow.iterator();
                    for (int col = 0; billSettlDtlColIt.hasNext(); col++)
                    {
                        billSettlDtlData[row][col] = billSettlDtlColIt.next();
                    }
                }
                String[] billSettlDtlColumns = new String[]
                {
                    "Bill No", "Settlement Amount", "Refund Amount", "Select"
                };
                funGenerateTable(billSettlDtlData, billSettlDtlColumns, billSettlDtlSize);
                break;

            case "tblqbillmodifierdtl":

                selectQuery.setLength(0);
                selectQuery.append("select b.*, a.dteBillDate from tblqbillhd a,tblqbillmodifierdtl b "
                        + "where a.strBillNo=b.strBillNo and date(a.dteBillDate) between '" + strFromDate + "' and '" + strToDate + "'  ");
                if (cmbPosCode.getSelectedIndex() > 0)
                {
                    selectQuery.append(" and a.strPOSCode='" + posCode + "' ");
                }
                selectQuery.append(" and b.strDataPostFlag='" + dataPostFlag + "' ");
                final int billModiDtlSize = 5;
                try
                {
                    resultSet = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery.toString());
                    while (resultSet.next())
                    {
                        ArrayList rowData = new ArrayList();
                        rowData.add(resultSet.getString("strBillNo"));
                        rowData.add(resultSet.getString("strModifierCode"));
                        rowData.add(resultSet.getString("strModifierName"));
                        rowData.add(resultSet.getString("dblAmount"));
                        rowData.add(true);
                        dataList.add(rowData);
                    }
                    resultSet.close();
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
                Object billModiDtlData[][] = new Object[dataList.size()][billModiDtlSize];
                Iterator<ArrayList> billModiDtlRowIt = dataList.iterator();
                for (int row = 0; billModiDtlRowIt.hasNext(); row++)
                {
                    ArrayList listRow = billModiDtlRowIt.next();
                    Iterator billModiDtlColIt = listRow.iterator();
                    for (int col = 0; billModiDtlColIt.hasNext(); col++)
                    {

                        billModiDtlData[row][col] = billModiDtlColIt.next();
                    }
                }
                String[] billModiDtlColumns = new String[]
                {
                    "Bill No", "Modifier Code", "Modifier Name", "Amount", "Select"
                };
                funGenerateTable(billModiDtlData, billModiDtlColumns, billModiDtlSize);
                break;

            case "tblqbilltaxdtl":

                selectQuery.setLength(0);
                selectQuery.append("select b.*, a.dteBillDate from tblqbillhd a,tblqbilltaxdtl b "
                        + "where a.strBillNo=b.strBillNo and date(a.dteBillDate) between '" + strFromDate + "' and '" + strToDate + "'  ");
                if (cmbPosCode.getSelectedIndex() > 0)
                {
                    selectQuery.append(" and a.strPOSCode='" + posCode + "' ");
                }
                selectQuery.append(" and b.strDataPostFlag='" + dataPostFlag + "' ");
                final int billTaxDtlSize = 5;
                try
                {
                    resultSet = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery.toString());
                    while (resultSet.next())
                    {
                        ArrayList rowData = new ArrayList();
                        rowData.add(resultSet.getString("strBillNo"));
                        rowData.add(resultSet.getString("strBillNo"));
                        rowData.add(resultSet.getString("dblTaxableAmount"));
                        rowData.add(resultSet.getString("dblTaxAmount"));
                        rowData.add(true);
                        dataList.add(rowData);
                    }
                    resultSet.close();
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
                Object billTaxDtlData[][] = new Object[dataList.size()][billTaxDtlSize];
                Iterator<ArrayList> billTaxDtlRowIt = dataList.iterator();
                for (int row = 0; billTaxDtlRowIt.hasNext(); row++)
                {
                    ArrayList listRow = billTaxDtlRowIt.next();
                    Iterator billTaxDtlColIt = listRow.iterator();
                    for (int col = 0; billTaxDtlColIt.hasNext(); col++)
                    {
                        billTaxDtlData[row][col] = billTaxDtlColIt.next();
                    }
                }
                String[] billTaxDtlColumns = new String[]
                {
                    "Bill No", "Tax Code", "Taxable Amount", "Tax Amount", "Select"
                };
                funGenerateTable(billTaxDtlData, billTaxDtlColumns, billTaxDtlSize);
                break;

            case "tblqbillpromotiondtl":

                selectQuery.setLength(0);
                selectQuery.append("select b.*, a.dteBillDate from tblqbillhd a ,tblqbillpromotiondtl b "
                        + " where date(a.dteBillDate) BETWEEN '" + strFromDate + "' and '" + strToDate + "' "
                        + " and a.strBillNo=b.strBillNo ");
                if (cmbPosCode.getSelectedIndex() > 0)
                {
                    selectQuery.append(" and a.strPOSCode='" + posCode + "' ");
                }
                selectQuery.append(" and b.strDataPostFlag='" + dataPostFlag + "' ");
                final int promoDtlSize = 6;
                try
                {
                    resultSet = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery.toString());

                    while (resultSet.next())
                    {
                        ArrayList rowData = new ArrayList();

                        rowData.add(resultSet.getString(1));
                        rowData.add(resultSet.getString(2));
                        rowData.add(resultSet.getString(10));
                        rowData.add(resultSet.getString(4));
                        rowData.add(resultSet.getString(9));
                        rowData.add(true);

                        dataList.add(rowData);
                    }
                    resultSet.close();

                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
                Object promoDtlData[][] = new Object[dataList.size()][promoDtlSize];
                Iterator<ArrayList> promoDtlRowIt = dataList.iterator();
                for (int row = 0; promoDtlRowIt.hasNext(); row++)
                {
                    ArrayList listRow = promoDtlRowIt.next();
                    Iterator promoDtlColIt = listRow.iterator();
                    for (int col = 0; promoDtlColIt.hasNext(); col++)
                    {
                        promoDtlData[row][col] = promoDtlColIt.next();
                    }
                }
                String[] promoDtlColumns = new String[]
                {
                    "Bill No", "Item Code", "Bill Date", "Quantity", "Amount"
                };
                funGenerateTable(promoDtlData, promoDtlColumns, promoDtlSize);
                break;

            case "tblqbillcomplementrydtl":
                selectQuery.setLength(0);
                selectQuery.append("select b.*, a.dteBillDate "
                        + "from tblqbillhd a,tblqbillcomplementrydtl b "
                        + "where a.strBillNo=b.strBillNo and date(a.dteBillDate) between '" + strFromDate + "' and '" + strToDate + "'  ");
                if (cmbPosCode.getSelectedIndex() > 0)
                {
                    selectQuery.append(" and a.strPOSCode='" + posCode + "' ");
                }
                selectQuery.append(" and b.strDataPostFlag='" + dataPostFlag + "' ");
                final int billComplDtlSize = 6;
                try
                {
                    resultSet = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery.toString());
                    while (resultSet.next())
                    {
                        ArrayList rowData = new ArrayList();
                        rowData.add(resultSet.getString("strBillNo"));
                        rowData.add(resultSet.getString("dteBillDate"));
                        rowData.add(resultSet.getString("strItemCode"));
                        rowData.add(resultSet.getString("strItemName"));
                        rowData.add(resultSet.getString("dblAmount"));
                        rowData.add(true);
                        dataList.add(rowData);
                    }
                    resultSet.close();
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
                Object billComplDtlData[][] = new Object[dataList.size()][billComplDtlSize];
                Iterator<ArrayList> billComplDtlRowIt = dataList.iterator();
                for (int row = 0; billComplDtlRowIt.hasNext(); row++)
                {
                    ArrayList listRow = billComplDtlRowIt.next();
                    Iterator billDtlColIt = listRow.iterator();
                    for (int col = 0; billDtlColIt.hasNext(); col++)
                    {
                        billComplDtlData[row][col] = billDtlColIt.next();
                    }
                }
                String[] billComplDtlColumns = new String[]
                {
                    "Bill No", "Bill Date", "Item Code", "Item Name", "Amount", "Select"
                };
                funGenerateTable(billComplDtlData, billComplDtlColumns, billComplDtlSize);
                break;

            case "tblqadvbookbillhd":

                selectQuery.setLength(0);
                selectQuery.append("select b.*, a.dteBillDate from tblqbillhd a,tblqadvbookbillhd b "
                        + "where a.strAdvBookingNo=b.strAdvBookingNo and date(a.dteBillDate) between '" + strFromDate + "' and '" + strToDate + "'    ");
                if (cmbPosCode.getSelectedIndex() > 0)
                {
                    selectQuery.append(" and a.strPOSCode='" + posCode + "' ");
                }
                selectQuery.append(" and b.strDataPostFlag='" + dataPostFlag + "' ");
                final int advBoolBillHdSize = 4;
                try
                {
                    resultSet = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery.toString());
                    while (resultSet.next())
                    {
                        ArrayList rowData = new ArrayList();
                        rowData.add(resultSet.getString("strAdvBookingNo"));
                        rowData.add(resultSet.getString("dteAdvBookingDate"));
                        rowData.add(resultSet.getString("dblGrandTotal"));
                        rowData.add(true);
                        dataList.add(rowData);
                    }
                    resultSet.close();
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
                Object advBookBillHdData[][] = new Object[dataList.size()][advBoolBillHdSize];
                Iterator<ArrayList> advBookBillHdRowIt = dataList.iterator();
                for (int row = 0; advBookBillHdRowIt.hasNext(); row++)
                {
                    ArrayList listRow = advBookBillHdRowIt.next();
                    Iterator advBookBillHdColIt = listRow.iterator();
                    for (int col = 0; advBookBillHdColIt.hasNext(); col++)
                    {

                        advBookBillHdData[row][col] = advBookBillHdColIt.next();
                    }
                }
                String[] advBookBillHdColumns = new String[]
                {
                    "Advance Booking No", "Advance Booking Date", "Grand Total", "Select"
                };
                funGenerateTable(advBookBillHdData, advBookBillHdColumns, advBoolBillHdSize);
                break;

            case "tblqadvbookbilldtl":

                selectQuery.setLength(0);
                selectQuery.append("select b.*, a.dteBillDate from tblqbillhd a,tblqadvbookbilldtl b where a.strAdvBookingNo=b.strAdvBookingNo and date(a.dteBillDate) between '" + strFromDate + "' and '" + strToDate + "'  ");
                if (cmbPosCode.getSelectedIndex() > 0)
                {
                    selectQuery.append(" and a.strPOSCode='" + posCode + "' ");
                }
                selectQuery.append(" and b.strDataPostFlag='" + dataPostFlag + "' ");
                final int advBookBillDtlSize = 6;
                try
                {
                    resultSet = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery.toString());
                    while (resultSet.next())
                    {
                        ArrayList rowData = new ArrayList();
                        rowData.add(resultSet.getString("strAdvBookingNo"));
                        rowData.add(resultSet.getString("dteAdvBookingDate"));
                        rowData.add(resultSet.getString("strItemCode"));
                        rowData.add(resultSet.getString("strItemName"));
                        rowData.add(resultSet.getString("dblAmount"));
                        rowData.add(true);
                        dataList.add(rowData);
                    }
                    resultSet.close();
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
                Object advBookBillDtlData[][] = new Object[dataList.size()][advBookBillDtlSize];
                Iterator<ArrayList> advBookBillDtlRowIt = dataList.iterator();
                for (int row = 0; advBookBillDtlRowIt.hasNext(); row++)
                {
                    ArrayList listRow = advBookBillDtlRowIt.next();
                    Iterator advBookBillDtlColIt = listRow.iterator();
                    for (int col = 0; advBookBillDtlColIt.hasNext(); col++)
                    {
                        advBookBillDtlData[row][col] = advBookBillDtlColIt.next();
                    }
                }
                String[] advBookBillDtlColumns = new String[]
                {
                    "Advance Booking No", "Advance Booking Date", "Item Code", "Item Name", "Amount", "Select"
                };
                funGenerateTable(advBookBillDtlData, advBookBillDtlColumns, advBookBillDtlSize);
                break;

            case "tblqadvancereceiptdtl":

                selectQuery.setLength(0);
                selectQuery.append("select c.* ,a.dteBillDate from tblqbillhd a,tblqadvancereceipthd b,tblqadvancereceiptdtl c "
                        + " where a.strAdvBookingNo=b.strAdvBookingNo and b.strReceiptNo=c.strReceiptNo "
                        + " and date(a.dteBillDate) between '" + strFromDate + "' and '" + strToDate + "'  ");
                if (cmbPosCode.getSelectedIndex() > 0)
                {
                    selectQuery.append(" and a.strPOSCode='" + posCode + "' ");
                }
                selectQuery.append(" and b.strDataPostFlag='" + dataPostFlag + "' ");
                //System.out.println("tblqadvancereceiptdtl-->" + selectQuery);
                final int advReceiptDtlSize = 5;
                try
                {
                    resultSet = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery.toString());
                    while (resultSet.next())
                    {
                        ArrayList rowData = new ArrayList();
                        rowData.add(resultSet.getString("strReceiptNo"));
                        rowData.add(resultSet.getString("strExpirydate"));
                        rowData.add(resultSet.getString("dblAdvDepositesettleAmt"));
                        rowData.add(resultSet.getString("dblPaidAmt"));
                        rowData.add(true);
                        dataList.add(rowData);
                    }
                    resultSet.close();
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
                Object advReceiptDtlData[][] = new Object[dataList.size()][advReceiptDtlSize];
                Iterator<ArrayList> advReceiptDtlRowIt = dataList.iterator();
                for (int row = 0; advReceiptDtlRowIt.hasNext(); row++)
                {
                    ArrayList listRow = advReceiptDtlRowIt.next();
                    Iterator advReceiptDtlColIt = listRow.iterator();
                    for (int col = 0; advReceiptDtlColIt.hasNext(); col++)
                    {
                        advReceiptDtlData[row][col] = advReceiptDtlColIt.next();
                    }
                }
                String[] advReceiptDtlColumns = new String[]
                {
                    "Receipt No", "Expiry Date", "Advance Deposite Amount", "Paid Amount", "Select"
                };
                funGenerateTable(advReceiptDtlData, advReceiptDtlColumns, advReceiptDtlSize);
                break;

            case "tblqadvancereceipthd":

                selectQuery.setLength(0);
                selectQuery.append("select c.*,a.dteBillDate from tblqbillhd a,tblqadvbookbillhd b,tblqadvancereceipthd c where a.strAdvBookingNo=b.strAdvBookingNo and b.strAdvBookingNo=c.strAdvBookingNo  and date(a.dteBillDate) between '" + strFromDate + "' and '" + strToDate + "'  ");
                if (cmbPosCode.getSelectedIndex() > 0)
                {
                    selectQuery.append(" and a.strPOSCode='" + posCode + "' ");
                }
                selectQuery.append(" and b.strDataPostFlag='" + dataPostFlag + "' ");
                final int advReceiptHdSize = 5;
                try
                {
                    resultSet = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery.toString());
                    while (resultSet.next())
                    {
                        ArrayList rowData = new ArrayList();
                        rowData.add(resultSet.getString("strReceiptNo"));
                        rowData.add(resultSet.getString("dteBillDate"));
                        rowData.add(resultSet.getString("dtReceiptDate"));
                        rowData.add(resultSet.getString("dblAdvDeposite"));
                        rowData.add(true);
                        dataList.add(rowData);
                    }
                    resultSet.close();
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
                Object advReceiptHdData[][] = new Object[dataList.size()][advReceiptHdSize];
                Iterator<ArrayList> advReceiptHdRowIt = dataList.iterator();
                for (int row = 0; advReceiptHdRowIt.hasNext(); row++)
                {
                    ArrayList listRow = advReceiptHdRowIt.next();
                    Iterator advReceiptHdColIt = listRow.iterator();
                    for (int col = 0; advReceiptHdColIt.hasNext(); col++)
                    {
                        advReceiptHdData[row][col] = advReceiptHdColIt.next();
                    }
                }
                String[] advReceiptHdColumns = new String[]
                {
                    "Receipt No", "Bill Date", "Receipt Date", "Advance Deposite", "Select"
                };
                funGenerateTable(advReceiptHdData, advReceiptHdColumns, advReceiptHdSize);
                break;

            case "tblqadvordermodifierdtl":

                selectQuery.setLength(0);
                selectQuery.append("select c.*, a.dteBillDate from tblqbillhd a,tblqadvbookbillhd b,tblqadvordermodifierdtl c  "
                        + "where a.strAdvBookingNo=b.strAdvBookingNo and b.strAdvBookingNo=c.strAdvOrderNo and date(a.dteBillDate) between '" + strFromDate + "' and '" + strToDate + "'  ");
                if (cmbPosCode.getSelectedIndex() > 0)
                {
                    selectQuery.append(" and a.strPOSCode='" + posCode + "' ");
                }
                selectQuery.append(" and b.strDataPostFlag='" + dataPostFlag + "' ");
                final int advOrdModiDtlSize = 5;
                try
                {
                    resultSet = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery.toString());
                    while (resultSet.next())
                    {
                        ArrayList rowData = new ArrayList();
                        rowData.add(resultSet.getString("strAdvOrderNo"));
                        rowData.add(resultSet.getString("strModifierCode"));
                        rowData.add(resultSet.getString("strModifierName"));
                        rowData.add(resultSet.getString("dblAmount"));
                        rowData.add(true);
                        dataList.add(rowData);
                    }
                    resultSet.close();
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
                Object advOrdModiDtlData[][] = new Object[dataList.size()][advOrdModiDtlSize];
                Iterator<ArrayList> advOrdModiDtlRowIt = dataList.iterator();
                for (int row = 0; advOrdModiDtlRowIt.hasNext(); row++)
                {
                    ArrayList listRow = advOrdModiDtlRowIt.next();
                    Iterator advOrdModiDtlColIt = listRow.iterator();
                    for (int col = 0; advOrdModiDtlColIt.hasNext(); col++)
                    {
                        advOrdModiDtlData[row][col] = advOrdModiDtlColIt.next();
                    }
                }
                String[] advOdModiDtlColumns = new String[]
                {
                    "Advance Order No", "Modifier Code", "Modifier Name", "Amount", "Select"
                };
                funGenerateTable(advOrdModiDtlData, advOdModiDtlColumns, advOrdModiDtlSize);
                break;

            case "tbldayendprocess":
                selectQuery.setLength(0);
                selectQuery.append("select b.strPosName,a.*,if(a.dteDayEndDateTime='0000-00-00 00:00:00','NA',a.dteDayEndDateTime) as dayEndDateTime "
                        + "from tbldayendprocess a,tblposmaster b "
                        + "where  a.strPOSCode=b.strPosCode "
                        + "  ");
                selectQuery.append(" and a.strDataPostFlag='" + dataPostFlag + "' ");//and a.strDayEnd='Y' 

                System.out.println("day end ->" + selectQuery);

                final int colSize = 40;
                try
                {
                    resultSet = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery.toString());
                    while (resultSet.next())
                    {
                        ArrayList rowData = new ArrayList();
                        rowData.add(resultSet.getString("strPosName"));
                        rowData.add(resultSet.getString("dtePOSDate"));
                        rowData.add(resultSet.getString("strDayEnd"));
                        rowData.add(resultSet.getString("dblTotalSale"));
                        rowData.add(resultSet.getString("dblNoOfBill"));
                        rowData.add(resultSet.getString("dblNoOfVoidedBill"));
                        rowData.add(resultSet.getString("dblNoOfModifyBill"));
                        rowData.add(resultSet.getString("dblHDAmt"));
                        rowData.add(resultSet.getString("dblDiningAmt"));
                        rowData.add(resultSet.getString("dblTakeAway"));
                        rowData.add(resultSet.getString("dblFloat"));
                        rowData.add(resultSet.getString("dblCash"));
                        rowData.add(resultSet.getString("dblAdvance"));
                        rowData.add(resultSet.getString("dblTransferIn"));
                        rowData.add(resultSet.getString("dblTotalReceipt"));
                        rowData.add(resultSet.getString("dblPayments"));
                        rowData.add(resultSet.getString("dblWithdrawal"));
                        rowData.add(resultSet.getString("dblTransferOut"));
                        rowData.add(resultSet.getString("dblTotalPay"));
                        rowData.add(resultSet.getString("dblCashInHand"));
                        rowData.add(resultSet.getString("dblRefund"));
                        rowData.add(resultSet.getString("dblTotalDiscount"));
                        rowData.add(resultSet.getString("dblNoOfDiscountedBill"));
                        rowData.add(resultSet.getString("intShiftCode"));
                        rowData.add(resultSet.getString("strShiftEnd"));
                        rowData.add(resultSet.getString("intTotalPax"));
                        rowData.add(resultSet.getString("intNoOfTakeAway"));
                        rowData.add(resultSet.getString("intNoOfHomeDelivery"));
                        rowData.add(resultSet.getString("strUserCreated"));
                        rowData.add(resultSet.getString("dteDateCreated"));
                        String dayEndDateTime = resultSet.getString("dayEndDateTime");
                        System.out.println("day end date time=" + dayEndDateTime);
                        if (null == dayEndDateTime || dayEndDateTime.equals("NA"))
                        {
                            rowData.add(null);
                        }
                        else
                        {
                            rowData.add(dayEndDateTime);
                        }
                        rowData.add(resultSet.getString("strUserEdited"));
                        rowData.add(resultSet.getString("strDataPostFlag"));
                        rowData.add(resultSet.getString("intNoOfNCKOT"));
                        rowData.add(resultSet.getString("intNoOfComplimentaryKOT"));
                        rowData.add(resultSet.getString("intNoOfVoidKOT"));
                        rowData.add(resultSet.getString("dblUsedDebitCardBalance"));
                        rowData.add(resultSet.getString("dblUnusedDebitCardBalance"));
                        rowData.add(resultSet.getString("strWSStockAdjustmentNo"));
                        rowData.add(true);

                        dataList.add(rowData);
                    }
                    resultSet.close();
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
                Object dayEndData[][] = new Object[dataList.size()][colSize];
                Iterator<ArrayList> dayEndRowIt = dataList.iterator();
                for (int row = 0; dayEndRowIt.hasNext(); row++)
                {
                    ArrayList listRow = dayEndRowIt.next();
                    Iterator dayEndColIt = listRow.iterator();
                    for (int col = 0; dayEndColIt.hasNext(); col++)
                    {
                        dayEndData[row][col] = dayEndColIt.next();
                    }
                }
                String[] dayEndColumns = new String[]
                {
                    "POS", "POS Date", "Day End", "Total Sale", "No Of Bills", "No Of Voided Bills", "No Of Modify Bills", "HD Amt", "Dining Amt", "Take Away",
                    "Float", "Cash", "Advance", "Transfer In", "Total Receipt", "Payments", "Withdrawal", "Transfer Out", "Total Pay", "Cash In Hand",
                    "Refund", "Total Discount", "No Of Discounted Bills", "Shift", "Shift End", "Total Pax", "No Of TakeAway", "No Of HomeDelivery", "User Created", "Date Created",
                    "DayEnd DateTime", "User Edited", "Data PostFlag", "No Of NCKOT", "No Of Complimentary KOT", "No Of Void KOT", "Used DebitCardBalance", "Unused DebitCardBalance", "WS Stock AdjustmentNo", "Select"
                };
                funCreateDayEndTable(dayEndData, dayEndColumns, colSize);
                break;

            case "tblqcreditbillreceipthd":

                selectQuery.setLength(0);
                selectQuery.append("select a.strReceiptNo,a.strBillNo,date(a.dteBillDate)dteBillDate,a.dteReceiptDate,a.dblReceiptAmt"
                        + ",a.strSettlementName,a.strUserEdited  "
                        + "from tblqcreditbillreceipthd  a "
                        + "where strDataPostFlag='N' "
                        + "and date(a.dteReceiptDate) between '" + strFromDate + "' and '" + strToDate + "'  ");
                if (cmbPosCode.getSelectedIndex() > 0)
                {
                    selectQuery.append(" and a.strPOSCode='" + posCode + "' ");
                }
                selectQuery.append(" and a.strDataPostFlag='" + dataPostFlag + "' ");
                //System.out.println("tbladvancereceiptdtl-->" + selectQuery);
                final int creditReceiptDtlSize = 8;
                try
                {
                    resultSet = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery.toString());

                    while (resultSet.next())
                    {
                        ArrayList rowData = new ArrayList();

                        rowData.add(resultSet.getString("strReceiptNo"));
                        rowData.add(resultSet.getString("strBillNo"));
                        rowData.add(resultSet.getString("dteBillDate"));
                        rowData.add(resultSet.getString("dteReceiptDate"));
                        rowData.add(resultSet.getString("dblReceiptAmt"));
                        rowData.add(resultSet.getString("strSettlementName"));
                        rowData.add(resultSet.getString("strUserEdited"));
                        rowData.add(true);

                        dataList.add(rowData);
                    }
                    resultSet.close();
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
                Object creditReceiptDtlData[][] = new Object[dataList.size()][creditReceiptDtlSize];
                Iterator<ArrayList> creditReceiptDtlRowIt = dataList.iterator();
                for (int row = 0; creditReceiptDtlRowIt.hasNext(); row++)
                {
                    ArrayList listRow = creditReceiptDtlRowIt.next();
                    Iterator creditReceiptDtlColIt = listRow.iterator();
                    for (int col = 0; creditReceiptDtlColIt.hasNext(); col++)
                    {
                        creditReceiptDtlData[row][col] = creditReceiptDtlColIt.next();
                    }
                }
                String[] creditReceiptDtlColumns = new String[]
                {
                    "Receipt No", "Bill No", "Bill Date", "Receipt Date", "Receipt Amount", "Settlement", "User", "Select"
                };
                funGenerateTable(creditReceiptDtlData, creditReceiptDtlColumns, creditReceiptDtlSize);

                break;
        }
    }

    private void funGenerateTable(Object[][] billHdData, String[] billHdColumns, final int billHdSize)
    {

        dtmDataPostModel = new javax.swing.table.DefaultTableModel(
                billHdData,
                billHdColumns
        )
        {

            private Class[] types = new Class[billHdSize];
            private boolean[] canEdit = new boolean[billHdSize];

            
            {
                for (int i = 0; i < billHdSize - 1; i++)
                {
                    this.types[i] = java.lang.Object.class;
                }
                this.types[billHdSize - 1] = java.lang.Boolean.class;
            }

            ;

            {
                for(int i=0;i<billHdSize-1;i++)
                {
                    this.canEdit[i]=false;
                } 
                this.canEdit[billHdSize-1]=true;
            }

            public Class getColumnClass(int columnIndex)
            {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return canEdit[columnIndex];
            }
        };
        tblDataPostTable.setModel(dtmDataPostModel);

    }

    private void funClearFields()
    {

        dteFromDate.setDate(dteFromDate.getCalendar().getTime());
        dteToDate.setDate(dteFromDate.getCalendar().getTime());
        cmbPosCode.setSelectedIndex(0);
        cmbRecordType.setSelectedIndex(0);
        cmbTableNames.setSelectedIndex(0);
    }

    private void funPostDataToHOClicked(String tableName) throws Exception
    {
        switch (tableName)
        {
            case "tblqbillhd":
                clsGlobalVarClass.funPostDataToHOManually("billhd", strFromDate, strToDate, "ManuallyQFile");
                break;

            case "tblqbilldtl":
                clsGlobalVarClass.funPostDataToHOManually("billdtl", strFromDate, strToDate, "ManuallyQFile");
                break;

            case "tblqbilldiscdtl":
                clsGlobalVarClass.funPostDataToHOManually("billdiscdtl", strFromDate, strToDate, "ManuallyQFile");
                break;

            case "tblqbillsettlementdtl":
                clsGlobalVarClass.funPostDataToHOManually("billsettlementdtl", strFromDate, strToDate, "ManuallyQFile");
                break;

            case "tblqbilltaxdtl":
                clsGlobalVarClass.funPostDataToHOManually("billtaxdtl", strFromDate, strToDate, "ManuallyQFile");
                break;

            case "tblqbillmodifierdtl":
                clsGlobalVarClass.funPostDataToHOManually("billmodifierdtl", strFromDate, strToDate, "ManuallyQFile");
                break;

            case "tblqbillpromotiondtl":
                clsGlobalVarClass.funPostDataToHOManually("billpromotiondtl", strFromDate, strToDate, "ManuallyQFile");
                break;

            case "tblqbillcomplementrydtl":
                clsGlobalVarClass.funPostDataToHOManually("billcomplementrydtl", strFromDate, strToDate, "ManuallyQFile");
                break;

            case "tblbillhd":
                clsGlobalVarClass.funPostDataToHOManually("billhd", strFromDate, strToDate, "ManuallyLive");
                break;

            case "tblbilldtl":
                clsGlobalVarClass.funPostDataToHOManually("billdtl", strFromDate, strToDate, "ManuallyLive");
                break;

            case "tblbilldiscdtl":
                clsGlobalVarClass.funPostDataToHOManually("billdiscdtl", strFromDate, strToDate, "ManuallyLive");
                break;

            case "tblbillsettlementdtl":
                clsGlobalVarClass.funPostDataToHOManually("billsettlementdtl", strFromDate, strToDate, "ManuallyLive");
                break;

            case "tblbilltaxdtl":
                clsGlobalVarClass.funPostDataToHOManually("billtaxdtl", strFromDate, strToDate, "ManuallyLive");
                break;

            case "tblbillmodifierdtl":
                clsGlobalVarClass.funPostDataToHOManually("billmodifierdtl", strFromDate, strToDate, "ManuallyLive");
                break;

            case "tblbillpromotiondtl":
                clsGlobalVarClass.funPostDataToHOManually("billpromotiondtl", strFromDate, strToDate, "ManuallyLive");
                break;

            case "tblbillcomplementrydtl":
                clsGlobalVarClass.funPostDataToHOManually("billcomplementrydtl", strFromDate, strToDate, "ManuallyLive");
                break;

            case "tblcrmpoints":
                clsGlobalVarClass.funPostDataToHOManually("billcrmpoints", strFromDate, strToDate, "ManuallyLive");
                clsGlobalVarClass.funPostDataToHOManually("billcrmpoints", strFromDate, strToDate, "ManuallyQFile");
                break;

            case "tblhomedelivery":
                clsGlobalVarClass.funPostDataToHOManually("homedelivery", strFromDate, strToDate, "ManuallyLive");
                clsGlobalVarClass.funPostDataToHOManually("homedelivery", strFromDate, strToDate, "ManuallyQFile");
                break;

            case "tblhomedeldtl":
                clsGlobalVarClass.funPostDataToHOManually("homedeliverydtl", strFromDate, strToDate, "ManuallyLive");
                clsGlobalVarClass.funPostDataToHOManually("homedeliverydtl", strFromDate, strToDate, "ManuallyQFile");
                break;

            case "tblqadvbookbillhd":
                clsGlobalVarClass.funPostDataToHOManually("advorderhd", strFromDate, strToDate, "ManuallyQFile");
                break;

            case "tblqadvbookbilldtl":
                clsGlobalVarClass.funPostDataToHOManually("advorderdtl", strFromDate, strToDate, "ManuallyQFile");
                break;

            case "tblqadvordermodifierdtl":
                clsGlobalVarClass.funPostDataToHOManually("advordermodifierdtl", strFromDate, strToDate, "ManuallyQFile");
                break;

            case "tblqadvancereceipthd":
                clsGlobalVarClass.funPostDataToHOManually("advreceipthd", strFromDate, strToDate, "ManuallyQFile");
                break;

            case "tblqadvancereceiptdtl":
                clsGlobalVarClass.funPostDataToHOManually("advreceiptdtl", strFromDate, strToDate, "ManuallyQFile");
                break;

            case "tbladvbookbillhd":
                clsGlobalVarClass.funPostDataToHOManually("advorderhd", strFromDate, strToDate, "ManuallyQFile");
                break;

            case "tbladvbookbilldtl":
                clsGlobalVarClass.funPostDataToHOManually("advorderdtl", strFromDate, strToDate, "ManuallyQFile");
                break;

            case "tbladvordermodifierdtl":
                clsGlobalVarClass.funPostDataToHOManually("advordermodifierdtl", strFromDate, strToDate, "ManuallyQFile");
                break;

            case "tbladvancereceipthd":
                clsGlobalVarClass.funPostDataToHOManually("advreceipthd", strFromDate, strToDate, "ManuallyQFile");
                break;

            case "tbladvancereceiptdtl":
                clsGlobalVarClass.funPostDataToHOManually("advreceiptdtl", strFromDate, strToDate, "ManuallyQFile");
                break;

            case "Audit":
                clsGlobalVarClass.funInvokeHOWebserviceForTrans("Audit", "");
                break;

            case "tbldayendprocess":
                clsGlobalVarClass.funPostDataToHOManually("tbldayendprocess", strFromDate, strToDate, "ManuallyLive");
                break;

            case "tblqcreditbillreceipthd":
                clsGlobalVarClass.funPostDataToHOManually("Credit Bill Receipts", strFromDate, strToDate, "ManuallyQFile");
                break;
        }
    }

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
        lblUserCode = new javax.swing.JLabel();
        filler6 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        lblDate = new javax.swing.JLabel();
        lblHOSign = new javax.swing.JLabel();
        panelMainForm = new JPanel() {  
            public void paintComponent(Graphics g) {  
                Image img = Toolkit.getDefaultToolkit().getImage(  
                    getClass().getResource("/com/POSTransaction/images/imgBackgroundImage.png"));  
                g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);  
            }  
        };  
        ;
        panelFormBody = new javax.swing.JPanel();
        lblFormName = new javax.swing.JLabel();
        dteToDate = new com.toedter.calendar.JDateChooser();
        cmbPosCode = new javax.swing.JComboBox();
        lblPOSName = new javax.swing.JLabel();
        btnReset = new javax.swing.JButton();
        btnPostData = new javax.swing.JButton();
        dteFromDate = new com.toedter.calendar.JDateChooser();
        lblToDate = new javax.swing.JLabel();
        lblFromDate = new javax.swing.JLabel();
        lblRecordType = new javax.swing.JLabel();
        cmbRecordType = new javax.swing.JComboBox();
        lblTableName = new javax.swing.JLabel();
        cmbTableNames = new javax.swing.JComboBox();
        btnSubmit = new javax.swing.JButton();
        btnBack1 = new javax.swing.JButton();
        scrollPaneForTable = new javax.swing.JScrollPane();
        tblDataPostTable = new javax.swing.JTable();
        btnClear = new javax.swing.JButton();
        chkPostedData = new javax.swing.JCheckBox();
        lblShowPostedData = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setExtendedState(MAXIMIZED_BOTH);
        setMinimumSize(new java.awt.Dimension(800, 600));
        setUndecorated(true);
        setPreferredSize(new java.awt.Dimension(800, 600));
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
        lblformName.setText("  HO Data Posting");
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

        lblUserCode.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblUserCode.setForeground(new java.awt.Color(255, 255, 255));
        lblUserCode.setMaximumSize(new java.awt.Dimension(90, 30));
        lblUserCode.setMinimumSize(new java.awt.Dimension(90, 30));
        lblUserCode.setPreferredSize(new java.awt.Dimension(90, 30));
        panelHeader.add(lblUserCode);
        panelHeader.add(filler6);

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

        panelMainForm.setOpaque(false);
        panelMainForm.setLayout(new java.awt.GridBagLayout());

        panelFormBody.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        panelFormBody.setMinimumSize(new java.awt.Dimension(800, 570));
        panelFormBody.setOpaque(false);

        cmbPosCode.setBackground(new java.awt.Color(51, 102, 255));
        cmbPosCode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmbPosCode.setForeground(new java.awt.Color(255, 255, 255));

        lblPOSName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblPOSName.setText("POS Name    :");

        btnReset.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnReset.setForeground(new java.awt.Color(255, 255, 255));
        btnReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnReset.setText("Reset");
        btnReset.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnReset.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
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

        btnPostData.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnPostData.setForeground(new java.awt.Color(255, 255, 255));
        btnPostData.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnPostData.setText("<html>Post Data<br> To HO</html>");
        btnPostData.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPostData.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnPostData.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnPostDataMouseClicked(evt);
            }
        });
        btnPostData.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnPostDataActionPerformed(evt);
            }
        });

        lblToDate.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblToDate.setText("To Date       :");

        lblFromDate.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblFromDate.setText("From Date     :");

        lblRecordType.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblRecordType.setText("Record Type :");

        cmbRecordType.setBackground(new java.awt.Color(51, 102, 255));
        cmbRecordType.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmbRecordType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Live", "QFile" }));
        cmbRecordType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbRecordTypeActionPerformed(evt);
            }
        });

        lblTableName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblTableName.setText("Table Name   :");

        cmbTableNames.setBackground(new java.awt.Color(51, 102, 255));
        cmbTableNames.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmbTableNames.setForeground(new java.awt.Color(255, 255, 255));

        btnSubmit.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnSubmit.setForeground(new java.awt.Color(255, 255, 255));
        btnSubmit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnSubmit.setText("Submit");
        btnSubmit.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSubmit.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnSubmit.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnSubmitMouseClicked(evt);
            }
        });

        btnBack1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnBack1.setForeground(new java.awt.Color(255, 255, 255));
        btnBack1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnBack1.setText("CLOSE");
        btnBack1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnBack1.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnBack1.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnBack1MouseClicked(evt);
            }
        });
        btnBack1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnBack1ActionPerformed(evt);
            }
        });

        dtmDataPostModel=new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Bill No", "Bill Date", "Grand Total"
            }
        )
        {
            Class[] types = new Class []
            {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean []
            {
                false, false, false
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
        tblDataPostTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {

            }
        ));
        tblDataPostTable.getTableHeader().setReorderingAllowed(false);
        scrollPaneForTable.setViewportView(tblDataPostTable);

        btnClear.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnClear.setForeground(new java.awt.Color(255, 255, 255));
        btnClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnClear.setText("Clear");
        btnClear.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnClear.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnClear.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnClearMouseClicked(evt);
            }
        });
        btnClear.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnClearActionPerformed(evt);
            }
        });

        chkPostedData.setText("jCheckBox1");

        lblShowPostedData.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblShowPostedData.setText("Show Posted Data");

        javax.swing.GroupLayout panelFormBodyLayout = new javax.swing.GroupLayout(panelFormBody);
        panelFormBody.setLayout(panelFormBodyLayout);
        panelFormBodyLayout.setHorizontalGroup(
            panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFormBodyLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(lblFormName, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50)
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelFormBodyLayout.createSequentialGroup()
                                .addComponent(lblTableName, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmbTableNames, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnSubmit, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelFormBodyLayout.createSequentialGroup()
                                .addComponent(lblFromDate)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(dteFromDate, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(lblToDate))
                            .addGroup(panelFormBodyLayout.createSequentialGroup()
                                .addGap(353, 353, 353)
                                .addComponent(dteToDate, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblShowPostedData, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chkPostedData, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(66, 66, 66))
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panelFormBodyLayout.createSequentialGroup()
                                .addComponent(lblPOSName, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmbPosCode, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(lblRecordType)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmbRecordType, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addGroup(panelFormBodyLayout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(scrollPaneForTable, javax.swing.GroupLayout.PREFERRED_SIZE, 916, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(39, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFormBodyLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnPostData, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(53, 53, 53)
                .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(56, 56, 56)
                .addComponent(btnBack1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(299, 299, 299))
        );
        panelFormBodyLayout.setVerticalGroup(
            panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFormBodyLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblShowPostedData, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(lblFromDate, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(dteFromDate, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(lblToDate, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(dteToDate, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(chkPostedData, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFormBodyLayout.createSequentialGroup()
                        .addComponent(lblFormName)
                        .addGap(83, 83, 83))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFormBodyLayout.createSequentialGroup()
                        .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblPOSName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbPosCode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblRecordType, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbRecordType, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                        .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btnSubmit, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFormBodyLayout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lblTableName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cmbTableNames, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(6, 6, 6)))
                .addComponent(scrollPaneForTable, javax.swing.GroupLayout.PREFERRED_SIZE, 341, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnPostData, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnBack1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, 0))
        );

        panelMainForm.add(panelFormBody, new java.awt.GridBagConstraints());

        getContentPane().add(panelMainForm, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnResetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnResetMouseClicked

        funClearFields();
        dtmDataPostModel.setRowCount(0);
    }//GEN-LAST:event_btnResetMouseClicked

    private void btnPostDataMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPostDataMouseClicked

        try
        {
            funPostDataToHOClicked(cmbTableNames.getSelectedItem().toString());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            new clsUtility().funWriteErrorLog(e);
        }
    }//GEN-LAST:event_btnPostDataMouseClicked

    private void btnPostDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPostDataActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnPostDataActionPerformed

    private void btnSubmitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSubmitMouseClicked
        funSubmitButtonClicked();
    }//GEN-LAST:event_btnSubmitMouseClicked

    private void btnBack1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBack1MouseClicked
        dispose();
        clsGlobalVarClass.hmActiveForms.remove("Post Sale Data");
    }//GEN-LAST:event_btnBack1MouseClicked

    private void cmbRecordTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbRecordTypeActionPerformed

        if (cmbRecordType.getSelectedIndex() == 0)
        {
            cmbTableNames.setModel(liveTableModel);
        }
        else
        {
            cmbTableNames.setModel(qFileTableModel);
        }
    }//GEN-LAST:event_cmbRecordTypeActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnClearMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnClearMouseClicked

        funClearFields();
    }//GEN-LAST:event_btnClearMouseClicked

    private void btnBack1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBack1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnBack1ActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnResetActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosed
    {//GEN-HEADEREND:event_formWindowClosed
        clsGlobalVarClass.hmActiveForms.remove("Post Sale Data");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosing
    {//GEN-HEADEREND:event_formWindowClosing
        clsGlobalVarClass.hmActiveForms.remove("Post Sale Data");
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
            java.util.logging.Logger.getLogger(frmPostDataToHO.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(frmPostDataToHO.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(frmPostDataToHO.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(frmPostDataToHO.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                new frmPostDataToHO().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack1;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnPostData;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnSubmit;
    private javax.swing.JCheckBox chkPostedData;
    private javax.swing.JComboBox cmbPosCode;
    private javax.swing.JComboBox cmbRecordType;
    private javax.swing.JComboBox cmbTableNames;
    private com.toedter.calendar.JDateChooser dteFromDate;
    private com.toedter.calendar.JDateChooser dteToDate;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblFormName;
    private javax.swing.JLabel lblFromDate;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblPOSName;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblRecordType;
    private javax.swing.JLabel lblShowPostedData;
    private javax.swing.JLabel lblTableName;
    private javax.swing.JLabel lblToDate;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblformName;
    private javax.swing.JPanel panelFormBody;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelMainForm;
    private javax.swing.JScrollPane scrollPaneForTable;
    private javax.swing.JTable tblDataPostTable;
    // End of variables declaration//GEN-END:variables

    private void funCreateDayEndTable(Object[][] dayEndData, String[] dayEndColumns, final int colSize)
    {
        dtmDataPostModel = new javax.swing.table.DefaultTableModel(
                dayEndData,
                dayEndColumns
        )
        {

            private Class[] types = new Class[colSize];
            private boolean[] canEdit = new boolean[colSize];

            
            {
                for (int i = 0; i < colSize - 1; i++)
                {
                    this.types[i] = java.lang.Object.class;
                }
                this.types[colSize - 1] = java.lang.Boolean.class;
            }

            ;

            {
                for(int i=0;i<colSize-1;i++)
                {
                    this.canEdit[i]=false;
                } 
                this.canEdit[colSize-1]=true;
            }

            public Class getColumnClass(int columnIndex)
            {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return canEdit[columnIndex];
            }
        };
        tblDataPostTable.setModel(dtmDataPostModel);

        tblDataPostTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        tblDataPostTable.getColumnModel().getColumn(0).setPreferredWidth(70);
        tblDataPostTable.getColumnModel().getColumn(1).setPreferredWidth(70);
        tblDataPostTable.getColumnModel().getColumn(2).setPreferredWidth(70);
        tblDataPostTable.getColumnModel().getColumn(3).setPreferredWidth(70);
        tblDataPostTable.getColumnModel().getColumn(4).setPreferredWidth(70);
        tblDataPostTable.getColumnModel().getColumn(5).setPreferredWidth(70);
        tblDataPostTable.getColumnModel().getColumn(6).setPreferredWidth(70);
        tblDataPostTable.getColumnModel().getColumn(7).setPreferredWidth(70);
        tblDataPostTable.getColumnModel().getColumn(8).setPreferredWidth(70);
        tblDataPostTable.getColumnModel().getColumn(9).setPreferredWidth(70);
        tblDataPostTable.getColumnModel().getColumn(10).setPreferredWidth(70);
        tblDataPostTable.getColumnModel().getColumn(11).setPreferredWidth(70);
        tblDataPostTable.getColumnModel().getColumn(12).setPreferredWidth(70);
        tblDataPostTable.getColumnModel().getColumn(13).setPreferredWidth(70);
        tblDataPostTable.getColumnModel().getColumn(14).setPreferredWidth(70);
        tblDataPostTable.getColumnModel().getColumn(15).setPreferredWidth(70);
        tblDataPostTable.getColumnModel().getColumn(16).setPreferredWidth(70);
        tblDataPostTable.getColumnModel().getColumn(17).setPreferredWidth(70);
        tblDataPostTable.getColumnModel().getColumn(18).setPreferredWidth(70);
        tblDataPostTable.getColumnModel().getColumn(19).setPreferredWidth(70);
        tblDataPostTable.getColumnModel().getColumn(20).setPreferredWidth(70);
        tblDataPostTable.getColumnModel().getColumn(21).setPreferredWidth(70);
        tblDataPostTable.getColumnModel().getColumn(22).setPreferredWidth(70);
        tblDataPostTable.getColumnModel().getColumn(23).setPreferredWidth(70);
        tblDataPostTable.getColumnModel().getColumn(24).setPreferredWidth(70);
        tblDataPostTable.getColumnModel().getColumn(25).setPreferredWidth(70);
        tblDataPostTable.getColumnModel().getColumn(26).setPreferredWidth(70);
        tblDataPostTable.getColumnModel().getColumn(27).setPreferredWidth(70);
        tblDataPostTable.getColumnModel().getColumn(28).setPreferredWidth(70);
        tblDataPostTable.getColumnModel().getColumn(29).setPreferredWidth(70);
        tblDataPostTable.getColumnModel().getColumn(30).setPreferredWidth(70);
        tblDataPostTable.getColumnModel().getColumn(31).setPreferredWidth(70);
        tblDataPostTable.getColumnModel().getColumn(32).setPreferredWidth(70);
        tblDataPostTable.getColumnModel().getColumn(33).setPreferredWidth(70);
        tblDataPostTable.getColumnModel().getColumn(34).setPreferredWidth(70);
        tblDataPostTable.getColumnModel().getColumn(35).setPreferredWidth(70);
        tblDataPostTable.getColumnModel().getColumn(36).setPreferredWidth(70);
        tblDataPostTable.getColumnModel().getColumn(37).setPreferredWidth(70);
        tblDataPostTable.getColumnModel().getColumn(38).setPreferredWidth(70);
        tblDataPostTable.getColumnModel().getColumn(39).setPreferredWidth(70);

    }

}
