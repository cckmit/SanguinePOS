/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSTransaction.view;

import com.POSGlobal.controller.clsFixedSizeText;
import com.POSGlobal.controller.clsGlobalSingleObject;
import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsSMSSender;
import com.POSGlobal.controller.clsUtility;
import com.POSGlobal.view.frmAlfaNumericKeyBoard;
import com.POSGlobal.view.frmNumericKeyboard;
import com.POSGlobal.view.frmOkPopUp;
import com.POSGlobal.view.frmSearchFormDialog;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Window;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class frmCustomerMaster extends javax.swing.JFrame
{

    private String sql, errorType, buildingType;
    private java.util.Vector vCustType, vCustTypeCode;
    private double redeemAmt, cardValue, minCharges;
    private boolean flgOperation;
    clsUtility objUtility;
    private String transactionUserCode = null;
    StringBuilder sqlBuilder = null;
    private List<String> arrMobileNoList = new ArrayList<String>();
    

    public frmCustomerMaster()
    {
        initComponents();

        try
        {
            objUtility = new clsUtility();
            txtMobileNo.requestFocus();
            buildingType = "residential";

            txtCustomerName.setDocument(new clsFixedSizeText(50));

            txtBuildingName.setDocument(new clsFixedSizeText(99));
            txtStreetName.setDocument(new clsFixedSizeText(99));
            if (!clsGlobalVarClass.gClientCode.equals("009.001"))
            {
                // panelBuilding.setLocation(panelDebitCard.getLocation());
                panelBuilding.setVisible(true);
                panelDebitCard.setVisible(false);
            }
            else
            {
                panelBuilding.setVisible(false);
                panelDebitCard.setVisible(true);
            };

            lblUserCode.setText(clsGlobalVarClass.gUserCode);
            lblPosName.setText(clsGlobalVarClass.gPOSName);
            lblDate.setText(clsGlobalVarClass.gPOSDateToDisplay);
            lblModuleName.setText(clsGlobalVarClass.gSelectedModule);
            sqlBuilder = new StringBuilder();
            //if (String.valueOf(clsGlobalVarClass.gNewCustomerMobileNo).length() == 10) {
            if (String.valueOf(clsGlobalVarClass.gNewCustomerMobileNo).length() > 1)
            {
                txtMobileNo.setText(String.valueOf(clsGlobalVarClass.gNewCustomerMobileNo));
            }

            vCustType = new java.util.Vector();
            vCustTypeCode = new java.util.Vector();
            sql = "select strCustTypeCode,strCustType from tblcustomertypemaster";
            ResultSet rsCustTypeData = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            while (rsCustTypeData.next())
            {
                vCustTypeCode.add(rsCustTypeData.getString(1));
                vCustType.add(rsCustTypeData.getString(2));
            }
            rsCustTypeData.close();
            funFillCustTypeCombo();
            funFillCardTypeCombo();

            SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date dt = sFormat.parse("1980-01-01");
            dteDOB.setDate(dt);
            dteAnniversory.setDate(dt);
            cmbCity.setSelectedItem(clsGlobalVarClass.gCityName);
            cmbState.setSelectedItem(clsGlobalVarClass.gStateName);
            cmbOfficeCity.setSelectedItem("Mumbai");
            cmbOfficeState.setSelectedItem("Maharashtra");
            this.setEnabled(true);
            txtMobileNo.requestFocus();
            //this.setAlwaysOnTop(true);
            funSetShortCutKeys();

            if (!clsGlobalVarClass.gAllowNewAreaMasterFromCustMaster)
            {
                txtBuildingName.setEnabled(false);
                txtBuildingName.setEditable(false);
            }
            else
            {
                txtBuildingName.setEnabled(true);
                txtBuildingName.setEditable(true);
            }

            if (clsGlobalVarClass.gClientCode.equals("062.001")) // For Client Patio
            {
                lblBuildingCode.setText("Address1");
                lblStreetName.setText("Address2");
            }

            funSetSelectedCityFromSetup();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    

    private void funSetShortCutKeys()
    {
        btnCancel.setMnemonic('c');
        btnNew.setMnemonic('s');
        btnReset.setMnemonic('r');
    }

    private void funResetField()
    {
        try
        {
            btnNew.setText("SAVE");
            txtCustomerCode.setText("");
            txtCustomerName.setText("");
            txtMobileNo.setText("");
            txtStreetName.setText("");
            txtPinCode.setText("");
            txtMobileNo.requestFocus();
            txtEmail.setText("");
            cmbCustType.setSelectedIndex(0);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void funFillCustTypeCombo()
    {
        try
        {
            if (cmbCustType.getItemCount() == 0)
            {
                for (int i = 0; i < vCustType.size(); i++)
                {
                    cmbCustType.addItem(vCustType.elementAt(i).toString());
                }
                if (clsGlobalVarClass.gClientCode.equals("009.001"))
                {
                    cmbCustType.setSelectedItem(vCustType.elementAt(3).toString());
                }
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void funFillCardTypeCombo()
    {
        try
        {
            sql = "select strCardTypeCode from tbldebitcardtype";
            ResultSet rsFillComboBox = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            while (rsFillComboBox.next())
            {
                cmbCardType.addItem(rsFillComboBox.getString(1));
            }
            rsFillComboBox.close();
            if (clsGlobalVarClass.gClientCode.equals("009.001"))
            {
                funSetCardType();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void funSetCardType()
    {
        try
        {
            String debitCardCode = cmbCardType.getSelectedItem().toString();
            if (clsGlobalVarClass.validateEmpty(debitCardCode))
            {
                sql = "select strCardName from tbldebitcardtype where strCardTypeCode='" + debitCardCode + "'";
                ResultSet rsCarName = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                rsCarName.next();
                lblCardName.setText(rsCarName.getString(1));
                rsCarName.close();
            }
            else
            {
                lblCardName.setText("");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    void funSetCustomerData(Object[] data)
    {
        try
        {
            String buildingCode = null;
            if (clsGlobalVarClass.gClientCode.equals("009.001"))
            {
                sql = "select * from tblcustomermaster where strExternalCode='" + clsGlobalVarClass.gSearchedItem + "'";
            }
            else
            {
                sql = "select * from tblcustomermaster where strCustomerCode='" + clsGlobalVarClass.gSearchedItem + "'";
            }
            ResultSet rsCustomerInfo = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            rsCustomerInfo.next();
            txtCustomerCode.setText(rsCustomerInfo.getString(1));
            txtCustomerName.setText(rsCustomerInfo.getString(2));
            txtBuildingCode.setText(rsCustomerInfo.getString(3));
            txtBuildingName.setText(rsCustomerInfo.getString(4));
            txtStreetName.setText(rsCustomerInfo.getString(5));
            txtLandmark.setText(rsCustomerInfo.getString(6));
            cmbCity.setSelectedItem(rsCustomerInfo.getString(8));
            cmbState.setSelectedItem(rsCustomerInfo.getString(9));
            txtPinCode.setText(rsCustomerInfo.getString(10));
            txtMobileNo.setText(rsCustomerInfo.getString(11));
            txtOfficeBuildingCode.setText(rsCustomerInfo.getString(13));
            txtOfficeBuildingName.setText(rsCustomerInfo.getString(14));
            txtOfficeStreet.setText(rsCustomerInfo.getString(15));
            txtOfficeLandmark.setText(rsCustomerInfo.getString(16));
            txtOfficePincode.setText(rsCustomerInfo.getString(19));
            cmbOfficeCity.setSelectedItem(rsCustomerInfo.getString(18));
            cmbOfficeState.setSelectedItem(rsCustomerInfo.getString(20));
            txtOfficeMobileNo.setText(rsCustomerInfo.getString(21));
            txtExtCode.setText(rsCustomerInfo.getString(29));
            cmbGender.setSelectedItem(rsCustomerInfo.getString(32));
            txtEmail.setText(rsCustomerInfo.getString(34));
            txtCustAddress.setText(rsCustomerInfo.getString(36));

            txtTempCustAddress.setText(rsCustomerInfo.getString(37));
            txtTempStreetName.setText(rsCustomerInfo.getString(38));
            txtTempLandmark.setText(rsCustomerInfo.getString(39));
            txtGSTNo.setText(rsCustomerInfo.getString(40));

            if (rsCustomerInfo.getString(31).length() > 0)
            {
                SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date dt = dtFormat.parse(rsCustomerInfo.getString(31));
                dteDOB.setDate(dt);
            }

            if (rsCustomerInfo.getString(33).length() > 0)
            {
                SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date dt1 = dtFormat.parse(rsCustomerInfo.getString(33));
                dteAnniversory.setDate(dt1);
            }

            String sql_CustType = "select strCustType from tblcustomertypemaster "
                    + "where strCustTypeCode='" + rsCustomerInfo.getString(30) + "'";
            ResultSet rsCustType = clsGlobalVarClass.dbMysql.executeResultSet(sql_CustType);
            if (rsCustType.next())
            {
                cmbCustType.setSelectedItem(rsCustType.getString(1));
                funCustomerTypeComboSelect();
            }
            rsCustType.close();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public long funGenSBWNo()
    {
        long lastNo = 0;
        try
        {
            if (txtExtCode.getText().trim().startsWith("SBW"))
            {
                sql = "select count(dblLastNo) from tblinternal where strTransactionType='SBW'";
                ResultSet rsSBW = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                rsSBW.next();
                int cntSBW = rsSBW.getInt(1);
                rsSBW.close();
                if (cntSBW > 0)
                {
                    sql = "select dblLastNo from tblinternal where strTransactionType='SBW'";
                    rsSBW = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                    rsSBW.next();
                    long code = rsSBW.getLong(1);
                    code = code + 1;
                    lastNo = code;
                    rsSBW.close();
                }
                else
                {
                    lastNo = 1;
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            return lastNo;
        }
    }

    public void funUpdateInternalTable(long lastNo)
    {
        try
        {
            if (lastNo > 1)
            {
                sql = "update tblinternal set dblLastNo='" + lastNo + "' where strTransactionType='SBW'";
            }
            else
            {
                sql = "insert into tblinternal values('SBW'," + 1 + ")";
            }
            clsGlobalVarClass.dbMysql.execute(sql);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public boolean funPostCustomerDataTORMS(String transType)
    {
        boolean flgRFIDReturn = false;
        Connection conRMS = null;
        int customerType = 0;
        try
        {
            String rmsConURL = "jdbc:sqlserver://" + clsGlobalVarClass.gRFIDDBServerName + ":1433;user=" + clsGlobalVarClass.gRFIDDBUserName + ";password=" + clsGlobalVarClass.gRFIDDBPassword + ";database=" + clsGlobalVarClass.gRFIDDBName + "";
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conRMS = DriverManager.getConnection(rmsConURL);
            conRMS.setAutoCommit(false);

            if (cmbCustType.getSelectedItem().toString().equalsIgnoreCase("Executive"))
            {
                customerType = 1;
            }
            else if (cmbCustType.getSelectedItem().toString().equalsIgnoreCase("Member"))
            {
                customerType = 2;
            }
            else if (cmbCustType.getSelectedItem().toString().equalsIgnoreCase("Employee"))
            {
                customerType = 3;
            }
            if (cmbCustType.getSelectedItem().toString().equalsIgnoreCase("Walk In"))
            {
                customerType = 4;
            }

            String sql1 = "select strCustomerCode,strCustomerName,strArea,strCity,strState,longMobileNo,strUserCreated"
                    + ",strUserEdited,dteDateCreated,dteDateEdited,strExternalCode,strCustomerType "
                    + "from tblcustomermaster where strDataPostFlag='N'";
            ResultSet rsCustData = clsGlobalVarClass.dbMysql.executeResultSet(sql1);
            while (rsCustData.next())
            {
                String deleteSql = "delete from tblCustomerMast where strCustomerCode='" + rsCustData.getString(11) + "'";
                Statement st = conRMS.createStatement();
                st.execute(deleteSql);

                if (rsCustData.getString(12).equalsIgnoreCase("Executive"))
                {
                    customerType = 1;
                }
                else if (rsCustData.getString(12).equalsIgnoreCase("Member"))
                {
                    customerType = 2;
                }
                else if (rsCustData.getString(12).equalsIgnoreCase("Employee"))
                {
                    customerType = 3;
                }
                if (rsCustData.getString(12).equalsIgnoreCase("Walk In"))
                {
                    customerType = 4;
                }
                sql = "insert into tblCustomerMast(strCustomerCode,strCustomerName,strAddress,intAreaCode,intCityCode,"
                        + "intStateCode,strExpiry,strExpiryType,strAutoSelect,intCustomerType,strAddressLine1,"
                        + "strAddressLine2,strAddressLine3,strMobileNo,dteEntryDate,dteModifiedDate) "
                        + "values('" + rsCustData.getString(11) + "','" + rsCustData.getString(2) + "','"
                        + rsCustData.getString(3) + "',1,1," + "1,'No','NA','No'," + customerType + ",'" + rsCustData.getString(3)
                        + "','',''," + rsCustData.getString(6) + ",'" + rsCustData.getString(9) + "','"
                        + rsCustData.getString(10) + "')";
                st.execute(sql);
                String updateSql = "update tblcustomermaster set strDataPostFlag='Y' "
                        + "where strExternalCode='" + rsCustData.getString(11) + "'";
                clsGlobalVarClass.dbMysql.execute(updateSql);
            }
            //System.out.println(sql);
            conRMS.commit();
            flgRFIDReturn = true;

        }
        catch (Exception e)
        {
            flgRFIDReturn = false;
            try
            {
                conRMS.rollback();
            }
            catch (Exception ex)
            {
            }
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
            return flgRFIDReturn;
        }
    }

    public boolean funCheckDuplicateMobileNo(String noType, String op)
    {
        boolean flgDuplicateMB = true;
        try
        {
            if (op.equalsIgnoreCase("Save"))
            {
                sql = "select count(longMobileNo),strExternalCode,strCustomerName from tblcustomermaster "
                        + "where longMobileNo='" + txtMobileNo.getText().trim() + "' ";
            }
            else
            {
                sql = "select count(longMobileNo),strExternalCode,strCustomerName from tblcustomermaster "
                        + "where longMobileNo='" + txtMobileNo.getText().trim() + "' "
                        + "and strCustomerCode!='" + txtCustomerCode.getText() + "'";
            }
            //System.out.println(sql);
            ResultSet rsDuplicateMB = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            if (rsDuplicateMB.next())
            {
                if (rsDuplicateMB.getInt(1) > 0)
                {
                    flgDuplicateMB = false;
                    if (clsGlobalVarClass.gClientCode.equals("009.001"))
                    {
                        new frmOkPopUp(this, "Error", "<html>Mobile No already exists under <br>" + rsDuplicateMB.getString(3)
                                + ".<br>Your Customer Code is " + rsDuplicateMB.getString(2) + "</html>", 0).setVisible(true);
                        //JOptionPane.showMessageDialog(this,"<html>Mobile No already exists.<br>Your Customer Code is "+rsDuplicateMB.getString(2)+"</html>");
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(this, "Mobile No already exists");
                    }
                }
            }
            rsDuplicateMB.close();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            return flgDuplicateMB;
        }
    }

    public boolean funGetBillAmount(String billNo)
    {
        boolean flgBillAmt = false;
        double billAmt = 0;
        errorType = "Insufficient Amount";
        try
        {
            clsGlobalVarClass.funOpenRMSDBCon();
            sql = "select dblGrandTotal from tblSaleHd where intVochNo=" + billNo + " and strCustCode='1'";

            ResultSet rsBillAmt = clsGlobalVarClass.stmtRMS.executeQuery(sql);
            if (rsBillAmt.next())
            {
                billAmt = Double.parseDouble(rsBillAmt.getString(1));
                double pointAmt = billAmt * 0.01;
                StringBuilder sbRoundOff = new StringBuilder(String.valueOf(pointAmt));
                int dot = sbRoundOff.indexOf(".");
                pointAmt = Double.parseDouble(sbRoundOff.substring(0, dot).toString());
                txtTotalPoints.setText(String.valueOf(pointAmt));
                txtBillAmount.setText(String.valueOf(billAmt));
                if (billAmt >= 500)
                {
                    flgBillAmt = true;
                }
            }
            else
            {
                errorType = "Invalid BillNo";
            }
            rsBillAmt.close();
            clsGlobalVarClass.stmtRMS.close();
            clsGlobalVarClass.conRMS.close();
        }
        catch (Exception e)
        {
            flgBillAmt = false;
            e.printStackTrace();
        }
        finally
        {
            return flgBillAmt;
        }
    }

    public boolean funCheckDuplicateExternalCode(String transType)
    {
        boolean flgDupExtCode = true;
        try
        {
            if (transType.equalsIgnoreCase("save"))
            {
                sql = "select count(strCustomerCode) from tblcustomermaster where strExternalCode='" + txtExtCode.getText() + "'";
            }
            else
            {
                sql = "select count(strCustomerCode) from tblcustomermaster "
                        + "where strExternalCode='" + txtExtCode.getText() + "' and strCustomerCode!='" + txtCustomerCode.getText() + "'";
            }
            ResultSet rsCountExtCode = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            if (rsCountExtCode.next())
            {
                if (rsCountExtCode.getInt(1) > 0)
                {
                    flgDupExtCode = false;
                }
            }
            rsCountExtCode.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            return flgDupExtCode;
        }
    }

    private String getCustomercode()
    {
        String customerCode = "", strCode = "", code = "";
        String propertCode = clsGlobalVarClass.gClientCode.substring(4);
        long lastNo = 1;
        try
        {
            sql = "select count(*) from tblcustomermaster";
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

    private String funGetRechargeNo()
    {
        String rechargeNo = "", code = "", strCode = "";
        try
        {
            sql = "select count(*) from tbldebitcardrecharge";
            ResultSet rsCountSet1 = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            rsCountSet1.next();
            int cn = rsCountSet1.getInt(1);
            rsCountSet1.close();
            if (cn > 0)
            {
                sql = "select max(intRechargeNo) from tbldebitcardrecharge";
                ResultSet rsCountSet = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                rsCountSet.next();
                code = rsCountSet.getString(1);
                StringBuilder sb = new StringBuilder(code);
                String ss = sb.delete(0, 2).toString();
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
                    rechargeNo = "RC000000" + intCode;
                }
                else if (intCode < 100)
                {
                    rechargeNo = "RC00000" + intCode;
                }
                else if (intCode < 1000)
                {
                    rechargeNo = "RC0000" + intCode;
                }
                else if (intCode < 10000)
                {
                    rechargeNo = "RC000" + intCode;
                }
                else if (intCode < 100000)
                {
                    rechargeNo = "RC00" + intCode;
                }
                else if (intCode < 1000000)
                {
                    rechargeNo = "RC0" + intCode;
                }
            }
            else
            {
                rechargeNo = "RC0000001";
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            return rechargeNo;
        }
    }

    private String funGetRedeemNo()
    {
        String redeemNo = "", code = "", strCode = "";
        try
        {
            sql = "select count(*) from tbldebitcardrecharge";
            ResultSet rsCountSet1 = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            rsCountSet1.next();
            int cn = rsCountSet1.getInt(1);
            rsCountSet1.close();
            if (cn > 0)
            {
                sql = "select max(intRedeemNo) from tbldebitcardrecharge";
                ResultSet rscountSet = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                rscountSet.next();
                code = rscountSet.getString(1);
                StringBuilder sb = new StringBuilder(code);
                String ss = sb.delete(0, 2).toString();
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
                //System.out.println(intCode+"\t"+strCode);
                if (intCode < 10)
                {
                    redeemNo = "RD000000" + intCode;
                }
                else if (intCode < 100)
                {
                    redeemNo = "RD00000" + intCode;
                }
                else if (intCode < 1000)
                {
                    redeemNo = "RD0000" + intCode;
                }
                else if (intCode < 10000)
                {
                    redeemNo = "RD000" + intCode;
                }
                else if (intCode < 100000)
                {
                    redeemNo = "RD00" + intCode;
                }
                else if (intCode < 1000000)
                {
                    redeemNo = "RD0" + intCode;
                }
            }
            else
            {
                redeemNo = "RD0000001";
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            return redeemNo;
        }
    }

    public boolean funRecharge(String mbNo, String custName, String custCode, String extCode, double billAmount, double rechargeAmount, String billNo, String remarks)
    {
        String customerCode = "", cardTypeCode = "", cardNo = "", complementry = "", allowRecharge = "", redeemable = "", returnMsg = "";
        String customerMobileNo = "", sql1 = "", sql = "";
        double totalRechAmt = 0;
        boolean flgRecharge = false;
        int insertRows = 0;
        Connection conRMS = null;
        try
        {
            String rmsConURL = "jdbc:sqlserver://" + clsGlobalVarClass.gRFIDDBServerName + ":1433;user=" + clsGlobalVarClass.gRFIDDBUserName + ";password=" + clsGlobalVarClass.gRFIDDBPassword + ";database=" + clsGlobalVarClass.gRFIDDBName + "";
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conRMS = DriverManager.getConnection(rmsConURL);
            conRMS.setAutoCommit(false);
            Statement st = conRMS.createStatement();
            Date dt = new Date();
            String date = (dt.getYear() + 1900) + "-" + (dt.getMonth() + 1) + "-" + dt.getDate();
            String time = dt.getHours() + ":" + dt.getMinutes() + ":" + dt.getSeconds();
            String currentDate = date + " " + time;
            String rechargeNo = funGetRechargeNo();
            String redeemNo = funGetRedeemNo();

            customerCode = custCode;
            customerMobileNo = mbNo;
            sql1 = "select strCardTypeCode,strCardNo from tbldebitcardmaster where strCustomerCode='" + customerCode + "'";
            ResultSet rsCustDebitCard = clsGlobalVarClass.dbMysql.executeResultSet(sql1);
            if (rsCustDebitCard.next())
            {
                cardTypeCode = rsCustDebitCard.getString(1);
                cardNo = rsCustDebitCard.getString(2);
                sql1 = "select * from tbldebitcardtype where strCardTypeCode='" + cardTypeCode + "'";
                ResultSet rsDebitCardTypeData = clsGlobalVarClass.dbMysql.executeResultSet(sql1);
                if (rsDebitCardTypeData.next())
                {
                    if ((rsDebitCardTypeData.getString(7).equals("Y")))
                    {
                        redeemable = "Y";
                    }
                    else
                    {
                        redeemable = "N";
                    }
                    if ((rsDebitCardTypeData.getString(5).equals("Y")))
                    {
                        complementry = "Y";
                    }
                    else
                    {
                        complementry = "N";
                    }
                    if (rsDebitCardTypeData.getString(12).toString().equalsIgnoreCase("Y"))
                    {
                        allowRecharge = "Y";
                    }
                    else
                    {
                        allowRecharge = "N";
                    }
                }
                rsDebitCardTypeData.close();
                String rechargeType = "RewardPoints";
                sql1 = "insert into tbldebitcardrecharge (intRechargeNo,intRedeemNo,strCardTypeCode,strCardNo,"
                        + "strRedeemable,strComplementary,dblRechargeAmount,strUserCreated,dteDateCreated,"
                        + "strPOSCode,strRemarks,strRechargeType) "
                        + "values ('" + rechargeNo + "','" + redeemNo + "','" + cardTypeCode + "','" + cardNo + "','"
                        + redeemable + "','" + complementry + "'," + rechargeAmount + ",'" + clsGlobalVarClass.gUserCode
                        + "','" + currentDate + "'," + "'P01','" + remarks + "','" + rechargeType + "')";
                insertRows = clsGlobalVarClass.dbMysql.execute(sql1);

                sql = "insert into tblRechargeDebitCard(strRechargeNo,strDebitCardString,dblRechargeAmount"
                        + ",dtEntryDate,strCustomerCode,strRemarks) "
                        + "values('" + rechargeNo + "','" + cardNo + "'," + rechargeAmount + ",'" + currentDate + "','" + extCode + "',"
                        + "'" + remarks + "')";
                insertRows = st.executeUpdate(sql);

                sql = "select dblLastNo from tblInternal where strTransactionType='JV'";
                ResultSet rsLastNo = st.executeQuery(sql);
                rsLastNo.next();
                double voucherNo = Double.parseDouble(rsLastNo.getString(1));
                voucherNo++;

                StringBuilder sb = new StringBuilder(voucherNo + "");
                sb = sb.delete(sb.indexOf("."), sb.length());
                int vNo = Integer.parseInt(sb.toString());

                sb = new StringBuilder(rechargeAmount + "");
                sb = sb.delete(sb.indexOf("."), sb.length());
                int amount = Integer.parseInt(sb.toString());

                sql = "insert into tblJvHd(intVochNo,dteVochDate,strNarration,dblGrandTotal,strUser"
                        + ",strTransactionType,dteEntryDate,dteModifiedDate,strUserModified) "
                        + "values(" + vNo + ",'" + currentDate + "','" + remarks + "','" + amount + "','HFP',"
                        + "'JV','" + currentDate + "','" + currentDate + "','HFP')";
                insertRows = st.executeUpdate(sql);

                sql = "insert into tblRewardCardPoints(intVochNo,strCustomerCode,dblBillAmt,dblPoints,dtVochDate) "
                        + "values(" + billNo + ",'" + extCode + "'," + billAmount + "," + rechargeAmount + ",'" + currentDate + "')";
                insertRows = st.executeUpdate(sql);

                sql = "insert into tblJvDtl(intVochNo,dteVochDate,strAccountCode,strSubCode,dblDrAmount,dblCrAmount) "
                        + "values(" + vNo + ",'" + currentDate + "','600-001-01',''," + amount + ",0)";
                insertRows = st.executeUpdate(sql);

                sql = "insert into tblJvDtl(intVochNo,dteVochDate,strAccountCode,strSubCode,dblDrAmount,dblCrAmount) "
                        + "values(" + vNo + ",'" + currentDate + "','002-001-01','" + extCode + "',0," + amount + ")";
                insertRows = st.executeUpdate(sql);

                if (insertRows > 0)
                {
                    sql = "update tblInternal set dblLastNo=" + vNo + " where strTransactionType='JV'";
                    insertRows = st.executeUpdate(sql);
                    returnMsg = rechargeNo;
                    String smsText = "Hello Mr./Mrs/Miss " + custName
                            + ". Thanks For Shopping with SANSKAR BAZAAR. " + rechargeAmount + " Points has been Rewarded to your "
                            + "account For Rs." + billAmount + " Shopping on " + date + ".Your total shopping upto date "
                            + "is Rs." + billAmount + " and Your Available Balance is " + rechargeAmount;

                    ArrayList<String> mobileNoList = new ArrayList<>();
                    mobileNoList.add(customerMobileNo);
                    clsSMSSender objSMSSender = new clsSMSSender(mobileNoList, smsText);
                    objSMSSender.start();
                }
                flgRecharge = true;
            }
            else
            {
                flgRecharge = false;
            }
            rsCustDebitCard.close();
            conRMS.commit();

        }
        catch (Exception e)
        {
            insertRows = 0;
            flgRecharge = false;
            conRMS.rollback();
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
                ex.printStackTrace();
            }
            return flgRecharge;
        }
    }

    /*
     * private int funPOSTCustomerDataToPMAM() { try { Map<String, String>
     * hmCRMid = new HashMap<String, String>(); String userToken =
     * clsGlobalVarClass.gOutletUID; String accessToken =
     * clsGlobalVarClass.gPOSID;
     *
     * String custMobileNo = ""; String custName = ""; String address = "";
     * String email = ""; String dob = (dteDOB.getDate().getMonth() + 1) + "/" +
     * (dteDOB.getDate().getDate()) + "/" + (dteDOB.getDate().getYear() + 1900);
     * String anniversory = (dteAnniversory.getDate().getMonth() + 1) + "/" +
     * (dteAnniversory.getDate().getDate()) + "/" +
     * (dteAnniversory.getDate().getYear() + 1900); URL pmamURL = new
     * URL(clsGlobalVarClass.gGetWebserviceURL);
     * com.pmam.crm.CRMLoyalityProgramSvc obj = new
     * com.pmam.crm.CRMLoyalityProgramSvc(pmamURL);
     *
     * String sql = "select strCustomerCode,strCustomerName,strBuildingName" +
     * ",longMobileNo,dteDOB,dteAnniversary,strEmailId " + "from
     * tblcustomermaster where strCRMId=''"; ResultSet rsCustData =
     * clsGlobalVarClass.dbMysql.executeResultSet(sql); while
     * (rsCustData.next()) { dob = ""; anniversory = ""; custMobileNo =
     * rsCustData.getString(4); custName = rsCustData.getString(2); address =
     * rsCustData.getString(3); if (rsCustData.getString(5).trim().length() > 0)
     * { String[] sp = rsCustData.getString(5).split("-"); dob = sp[2] + "/" +
     * sp[1] + "/" + sp[0]; }
     *
     * if (rsCustData.getString(6).trim().length() > 0) { String[] spAnniversory
     * = rsCustData.getString(6).split("-"); anniversory = spAnniversory[2] +
     * "/" + spAnniversory[1] + "/" + spAnniversory[0]; } email =
     * rsCustData.getString(7); int serailNo = 1;
     *
     * String data = "{\"ContactInfo\":[{\"SRNumber\":\"" + serailNo +
     * "\",\"CustomerID\"" + ":\"0\",\"UserName\":\"" + custMobileNo +
     * "\",\"CustomerName\":\"" + custName + "\"" + ",\"Phone\":\"" +
     * custMobileNo + "\" ,\"Address\":\"" + address + "\",\"Email\"" + ":\"" +
     * email + "\",\"DOB\":\"" + dob + "\",\"SpouseDOB\":\"\"" +
     * ",\"AnniversaryDate\":\"" + anniversory + "\",\"BranchID\":\"1\" }]}";
     * //System.out.println(data); String crmId =
     * obj.getCRMLoyalityProgramSvcSoap12().synchronise(userToken, accessToken,
     * data, "1"); //System.out.println("ID="+crmId); JSONParser p = new
     * JSONParser(); Object o = p.parse(crmId);
     *
     * JSONParser jsonParser = new JSONParser(); JSONObject jsonObject =
     * (JSONObject) jsonParser.parse(crmId); JSONArray lang = (JSONArray)
     * jsonObject.get("ContactInfo"); Iterator i = lang.iterator(); while
     * (i.hasNext()) { JSONObject innerObj = (JSONObject) i.next(); String
     * custId = innerObj.get("CustomerID").toString();
     * hmCRMid.put(rsCustData.getString(1), custId); } } rsCustData.close(); for
     * (Map.Entry<String, String> entry : hmCRMid.entrySet()) { String up =
     * "update tblcustomermaster set strCRMId='" + entry.getValue() + "' " +
     * "where strCustomerCode='" + entry.getKey() + "'";
     * clsGlobalVarClass.dbMysql.execute(up); }
     *
     * }
     * catch (Exception e) { e.printStackTrace(); } return 1; }
     */
    public void funSaveCustomerMaster()
    {
        try
        {
            btnNew.setText("Save");
            boolean flgRFID = false;
            long no = 0;
            String finalExtCode = "";

            if (!clsGlobalVarClass.validateEmpty(txtCustomerName.getText()))
            {
                new frmOkPopUp(this, "Please Enter Customer Name", "Error", 0).setVisible(true);
                txtCustomerName.requestFocus();
                return;
            }
            if (!objUtility.funCheckLength(txtCustomerName.getText(), 50))
            {
                new frmOkPopUp(this, "Customer Name length must be less than 50", "Error", 0).setVisible(true);
                txtCustomerName.requestFocus();
                return;
            }

            if (!txtMobileNo.getText().isEmpty())
            {
                if (arrMobileNoList.size() > 0)
                {
                    for (int cnt = 0; cnt < arrMobileNoList.size(); cnt++)
                    {
                        String mobileNo = arrMobileNoList.get(cnt);
                        if (!objUtility.funCheckLengthForContactNos(mobileNo, 50))
                        {
                            new frmOkPopUp(this, "Contact Nos minimum 6 digit or above", "Error", 0).setVisible(true);
                            txtMobileNo.requestFocus();
                            return;
                        }
                    }

                }
                else
                {
                    if (!objUtility.funCheckLengthForContactNos(txtMobileNo.getText(), 50))
                    {
                        new frmOkPopUp(this, "Contact Nos minimum 6 digit or above", "Error", 0).setVisible(true);
                        txtMobileNo.requestFocus();
                        return;
                    }
                }

                if (!clsGlobalVarClass.gClientCode.equals("024.001"))
                {
                    if (!funCheckDuplicateMobileNo("primary", "Save"))
                    {
                        txtMobileNo.requestFocus();
                        return;
                    }
                }
            }
            else
            {
                new frmOkPopUp(this, "Please Enter Contact Nos", "Error", 0).setVisible(true);
                txtMobileNo.requestFocus();
                return;
            }

            if (clsGlobalVarClass.gCustAreaCompulsory)
            {
                if (txtBuildingCode.getText().trim().length() == 0)
                {
                    new frmOkPopUp(this, "Please Enter Area", "Error", 0).setVisible(true);
                    return;
                }
            }

            if (clsGlobalVarClass.gClientCode.equals("009.001"))
            {
                if (txtExtCode.getText().isEmpty())
                {
                    txtExtCode.requestFocus();
                    new frmOkPopUp(this, "Please Enter External Code", "Error", 0).setVisible(true);
                    return;
                }
            }
            if (txtExtCode.getText().trim().length() > 0)
            {
                if (!funCheckDuplicateExternalCode("Save"))
                {
                    txtExtCode.requestFocus();
                    new frmOkPopUp(this, "External Code is already present", "Error", 0).setVisible(true);
                    return;
                }
            }
            if (vCustTypeCode.size() == 0)
            {
                new frmOkPopUp(this, "No records found in customer type master", "Error", 0).setVisible(true);
                return;
            }

            else
            {
                Date dtDOB = dteDOB.getDate();
                String dob = (dtDOB.getYear() + 1900) + "-" + (dtDOB.getMonth() + 1) + "-" + dtDOB.getDate();

                Date dt = dteAnniversory.getDate();
                String anniversaryDate = (dt.getYear() + 1900) + "-" + (dt.getMonth() + 1) + "-" + dt.getDate();

                //clsGlobalVarClass.dbMysql.funStartTransaction();
                String customerCode = getCustomercode();
                if (clsGlobalVarClass.gClientCode.equals("009.001"))
                {
                    if (txtExtCode.getText().trim().startsWith("SBW"))
                    {
                        no = funGenSBWNo();
                        txtExtCode.setText(txtExtCode.getText().trim().substring(0, 3) + no);
                    }
                }
                finalExtCode = txtExtCode.getText().trim();
                String custTypeCode = vCustTypeCode.elementAt(cmbCustType.getSelectedIndex()).toString().trim();

                String strGender = "Male";
                strGender = cmbGender.getSelectedItem().toString();
                if (cmbGender.getSelectedIndex() == 1)
                {
                    strGender = "Female";
                }
                String GSTNo = txtGSTNo.getText().trim();

                sql = "insert into tblcustomermaster(strCustomerCode,strCustomerName,strBuldingCode,"
                        + "strBuildingName,strStreetName,strLandmark,strArea,strCity,strState,intPinCode,"
                        + "longMobileNo,longAlternateMobileNo,strOfficeBuildingCode,strOfficeBuildingName"
                        + ",strOfficeStreetName,strOfficeLandmark,strOfficeArea,strOfficeCity,strOfficePinCode"
                        + ",strOfficeState,strOfficeNo,strUserCreated,strUserEdited,dteDateCreated,dteDateEdited,"
                        + "strClientCode,strOfficeAddress,strExternalCode,strCustomerType,dteDOB,strGender"
                        + ",dteAnniversary,strEmailId,strCustAddress,strTempAddress,strTempStreet,strTempLandmark,strGSTNo) "
                        + " values('" + customerCode + "','" + txtCustomerName.getText() + "',"
                        + "'" + txtBuildingCode.getText() + "','" + txtBuildingName.getText() + "',"
                        + "'" + txtStreetName.getText() + "','" + txtLandmark.getText().trim() + "','','" + cmbCity.getSelectedItem().toString() + "'"
                        + ",'" + cmbState.getSelectedItem().toString() + "','" + txtPinCode.getText() + "',"
                        + "'" + txtMobileNo.getText() + "','','" + txtOfficeBuildingCode.getText() + "'"
                        + ",'" + txtOfficeBuildingName.getText() + "','" + txtOfficeStreet.getText() + "',"
                        + "'" + txtOfficeLandmark.getText() + "','','" + cmbOfficeCity.getSelectedItem().toString() + "'"
                        + ",'" + txtOfficePincode.getText() + "','" + cmbOfficeState.getSelectedItem().toString() + "'"
                        + ",'" + txtOfficeMobileNo.getText() + "','" + clsGlobalVarClass.gUserCode + "',"
                        + "'" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.getCurrentDateTime() + "',"
                        + "'" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.gClientCode + "',''"
                        + ",'" + txtExtCode.getText().trim() + "','" + custTypeCode + "','" + dob + "'"
                        + ",'" + strGender + "','" + anniversaryDate + "'"
                        + ",'" + txtEmail.getText() + "','" + txtCustAddress.getText().trim() + "','" + txtTempCustAddress.getText().trim() + "','" + txtTempStreetName.getText().trim() + "'"
                        + ",'" + txtTempLandmark.getText().trim() + "','" + GSTNo + "')";
                clsGlobalVarClass.dbMysql.execute(sql);

                if (clsGlobalVarClass.gNewCustomerForHomeDel || clsGlobalVarClass.gNewCustForAdvOrder)
                {
                    if (clsGlobalVarClass.gTransactionType.equals("Direct Biller"))
                    {
                        clsGlobalVarClass.gCustMBNo = txtMobileNo.getText();
                        clsGlobalVarClass.gCustomerCode = customerCode;
                        clsGlobalVarClass.gCustomerName = txtCustomerName.getText();
                        clsGlobalVarClass.gCustomerAddress1 = txtBuildingName.getText();
                        clsGlobalVarClass.gCustomerAddress2 = txtStreetName.getText();
                        clsGlobalVarClass.gCustomerCity = cmbCity.getSelectedItem().toString();
                        clsGlobalVarClass.gBuildingCodeForHD = txtBuildingCode.getText().trim();
                        frmDirectBiller.lblCustInfo.setText(txtCustomerName.getText().trim());
                        //objUtility.funGetDeliveryCharges(txtBuildingCode.getText().trim(), clsGlobalVarClass.gTotalBillAmount);
                    }
                    else if (clsGlobalVarClass.gTransactionType.equals("Make KOT"))
                    {
                        clsGlobalVarClass.gCustMBNo = txtMobileNo.getText();
                        clsGlobalVarClass.gCustomerCode = customerCode;
                        clsGlobalVarClass.gCustomerName = txtCustomerName.getText();
                        clsGlobalVarClass.gCustomerAddress1 = txtBuildingName.getText();
                        clsGlobalVarClass.gCustomerAddress2 = txtStreetName.getText();
                        clsGlobalVarClass.gCustomerCity = cmbCity.getSelectedItem().toString();
                        clsGlobalVarClass.gBuildingCodeForHD = txtBuildingCode.getText().trim();

                        frmMakeKOT.btnNewCustomer.setText(txtCustomerName.getText());
                    }
                    else if (clsGlobalVarClass.gTransactionType.equals("Make Bill"))
                    {
                        clsGlobalVarClass.gCustomerCode = customerCode;
                        frmMakeBill.lblCustomerName.setText(txtCustomerName.getText());
                    }
                    else if (clsGlobalVarClass.gTransactionType.equals("ChangeCustomerOnBill"))
                    {
                        clsGlobalVarClass.gCustomerCode = customerCode;
                        frmChangeCustomerOnBill.lblMemberNameValue.setText(txtCustomerName.getText());
                        frmChangeCustomerOnBill.lblMemberCodeValue.setText(txtCustomerCode.getText());
                    }
                    else if (clsGlobalVarClass.gTransactionType.equals("Advance Order"))
                    {
                        clsGlobalVarClass.gCustCodeForAdvOrder = customerCode;
                        clsGlobalVarClass.gCustomerName = txtCustomerName.getText();
                        frmAdvanceOrder.lblCustInfo.setText(txtCustomerName.getText());
                    }
                }

                if (clsGlobalVarClass.gRFIDInterface.equals("Y"))
                {
                    if (clsGlobalVarClass.gClientCode.equals("009.001"))
                    {
                        if (funPostCustomerDataTORMS("Save"))
                        {
                            if (txtExtCode.getText().trim().startsWith("SBW"))
                            {
                                funUpdateInternalTable(no);
                            }
                            txtBillNo.requestFocus();
                            if (!clsGlobalVarClass.validateEmpty(txtCardNumber.getText()))
                            {
                                new frmOkPopUp(this, "Swipe the Card", "", 1).setVisible(true);
                                txtCardNumber.requestFocus();
                                return;
                            }
                            else
                            {
                                if (funRegisterDebitCard(customerCode))
                                {
                                    if (funRecharge(txtMobileNo.getText(), txtCustomerName.getText(), customerCode, finalExtCode, Double.parseDouble(txtBillAmount.getText()), Double.parseDouble(txtTotalPoints.getText()), txtBillNo.getText(), "Points On Shopping Using Sanskar Reward Card with BillNo=" + txtBillNo.getText()))
                                    {
                                        flgRFID = true;
                                    }
                                }
                            }
                        }
                        else
                        {
                            flgRFID = false;
                        }
                    }
                }
                else
                {
                    flgRFID = true;
                }

                if (flgRFID)
                {
                    if (txtExtCode.getText().trim().length() > 0)
                    {
                        new frmOkPopUp(this, "External Code=" + finalExtCode, "Successful", 3).setVisible(true);
                    }
                    new frmOkPopUp(this, "Entry added Successfully", "Successful", 3).setVisible(true);
                    //clsGlobalVarClass.dbMysql.funCommitTransaction();

                    
                    
                    if (clsGlobalVarClass.gDataSendFrequency.equals("After Every Bill"))
                    {
                        clsGlobalVarClass.funPostCustomerDataToHOPOS();
                    }
                    if (clsGlobalVarClass.gCRMInterface.equalsIgnoreCase("PMAM"))
                    {
                        //funPOSTCustomerDataToPMAM();
                    }
                    funResetFields();
                }
                else
                {
                    new frmOkPopUp(this, "<html>Failed to Save, Check Internet Connection <br>and Database credentials</html>", "ERROR", 3).setVisible(true);
                    //clsGlobalVarClass.dbMysql.funRollbackTransaction();
                }
            }
        }
        catch (Exception e)
        {
            //lobalVarClass.dbMysql.funRollbackTransaction();
            e.printStackTrace();
        }
    }

    public void funUpdateCustomerMaster()
    {
        try
        {
            //String customerCode = getCustomercode();

            if (!clsGlobalVarClass.validateEmpty(txtCustomerName.getText()))
            {
                new frmOkPopUp(this, "Please Enter Customer Name", "Error", 0).setVisible(true);
                txtCustomerName.requestFocus();
                return;
            }
            if (clsGlobalVarClass.gClientCode.equals("009.001"))
            {
                if (!objUtility.funCheckLength(txtCustomerName.getText(), 50))
                {
                    new frmOkPopUp(this, "Customer Name length must be less than 50", "Error", 0).setVisible(true);
                    txtCustomerName.requestFocus();
                    return;
                }
            }

            if (!txtMobileNo.getText().isEmpty())
            {
                if (arrMobileNoList.size() > 0)
                {
                    for (int cnt = 0; cnt < arrMobileNoList.size(); cnt++)
                    {
                        String mobileNo = arrMobileNoList.get(cnt);
                        if (!objUtility.funCheckLengthForContactNos(mobileNo, 50))
                        {
                            new frmOkPopUp(this, "Contact Nos minimum 6 digit or above", "Error", 0).setVisible(true);
                            txtMobileNo.requestFocus();
                            return;
                        }
                    }
                }
                else
                {
                    if (!objUtility.funCheckLengthForContactNos(txtMobileNo.getText(), 50))
                    {
                        new frmOkPopUp(this, "Contact Nos minimum 6 digit or above", "Error", 0).setVisible(true);
                        txtMobileNo.requestFocus();
                        return;
                    }
                }
            }
            else
            {
                new frmOkPopUp(this, "Please Enter Mobile No", "Error", 0).setVisible(true);
                txtMobileNo.requestFocus();
                return;
            }
            if (clsGlobalVarClass.gClientCode.equals("009.001"))
            {
                if (txtExtCode.getText().isEmpty())
                {
                    txtExtCode.requestFocus();
                    new frmOkPopUp(this, "Please Enter External Code", "Error", 0).setVisible(true);
                    return;
                }
            }
            if (txtExtCode.getText().trim().length() > 0)
            {
                if (!funCheckDuplicateExternalCode("update"))
                {
                    txtExtCode.requestFocus();
                    new frmOkPopUp(this, "External Code is already present", "Error", 0).setVisible(true);
                    return;
                }
            }
            if (vCustTypeCode.size() == 0)
            {
                new frmOkPopUp(this, "No records found in customer type master", "Error", 0).setVisible(true);
                return;
            }
            else
            {
                //clsGlobalVarClass.dbMysql.funStartTransaction();
                Date dtDOB = dteDOB.getDate();
                String dob = (dtDOB.getYear() + 1900) + "-" + (dtDOB.getMonth() + 1) + "-" + dtDOB.getDate();
                String custTypeCode = vCustTypeCode.elementAt(cmbCustType.getSelectedIndex()).toString().trim();
                Date dt = dteAnniversory.getDate();
                String anniversaryDate = (dt.getYear() + 1900) + "-" + (dt.getMonth() + 1) + "-" + dt.getDate();
                String strGender = "Male";
                strGender = cmbGender.getSelectedItem().toString();
                if (cmbGender.getSelectedIndex() == 1)
                {
                    strGender = "Female";
                }

                String GSTNo = txtGSTNo.getText().trim();

                //String adderss = txtArea.getText();
                sql = "update tblcustomermaster set strCustomerName='" + txtCustomerName.getText()
                        + "',strArea='',longMobileNo='" + txtMobileNo.getText() + "'"
                        + ",strUserEdited='" + clsGlobalVarClass.gUserCode + "',dteDateEdited='"
                        + clsGlobalVarClass.getCurrentDateTime() + "',strDataPostFlag='N'"
                        + ",strExternalCode='" + txtExtCode.getText().trim() + "',strCustomerType='" + custTypeCode + "'"
                        + ",dteDOB='" + dob + "',strBuildingName='" + txtBuildingName.getText() + "',strBuldingCode='" + txtBuildingCode.getText() + "'"
                        + ",strCity='" + cmbCity.getSelectedItem() + "',strState='" + cmbState.getSelectedItem() + "'"
                        + ",strStreetName='" + txtStreetName.getText() + "',intPinCode='" + txtPinCode.getText() + "'"
                        + ",strOfficeBuildingCode='" + txtOfficeBuildingCode.getText() + "',strOfficeBuildingName="
                        + "'" + txtOfficeBuildingName.getText() + "',strOfficeStreetName='" + txtOfficeStreet.getText() + "'"
                        + ",strOfficeLandmark='" + txtOfficeLandmark.getText() + "',strOfficeCity='" + cmbOfficeCity.getSelectedItem().toString() + "'"
                        + ",strOfficeState='" + cmbOfficeState.getSelectedItem().toString() + "',strOfficePinCode="
                        + "'" + txtOfficePincode.getText() + "',strOfficeNo='" + txtOfficeMobileNo.getText() + "'"
                        + ",strGender='" + strGender + "',dteAnniversary='" + anniversaryDate + "'"
                        + ",strEmailId='" + txtEmail.getText() + "',strCRMId='' "
                        + ",strCustAddress='" + txtCustAddress.getText() + "',strLandmark='" + txtLandmark.getText() + "'"
                        + ",strTempAddress='" + txtTempCustAddress.getText().trim() + "'"
                        + ",strTempStreet='" + txtTempStreetName.getText().trim() + "'"
                        + ",strTempLandmark='" + txtTempLandmark.getText().trim() + "'"
                        + ",strGSTNo='" + GSTNo + "' "
                        + " where strCustomerCode='" + txtCustomerCode.getText() + "' ";
                //System.out.println(sql);
                clsGlobalVarClass.dbMysql.execute(sql);

                if (clsGlobalVarClass.gNewCustomerForHomeDel || clsGlobalVarClass.gNewCustForAdvOrder)
                {
                    if (clsGlobalVarClass.gTransactionType.equals("Direct Biller"))
                    {
                        clsGlobalVarClass.gCustomerCode = txtCustomerCode.getText();
                        clsGlobalVarClass.gCustomerName = txtCustomerName.getText();
                        clsGlobalVarClass.gCustomerAddress1 = txtBuildingName.getText();
                        clsGlobalVarClass.gCustomerAddress2 = txtStreetName.getText();
                        clsGlobalVarClass.gCustomerCity = cmbCity.getSelectedItem().toString();
                        clsGlobalVarClass.gBuildingCodeForHD = txtBuildingCode.getText().trim();
                        //objUtility.funGetDeliveryCharges(txtBuildingCode.getText().trim(), clsGlobalVarClass.gTotalBillAmount);
                    }
                    else if (clsGlobalVarClass.gTransactionType.equals("Make KOT"))
                    {
                        clsGlobalVarClass.gCustomerCode = txtCustomerCode.getText();
                        clsGlobalVarClass.gBuildingCodeForHD = txtBuildingCode.getText().trim();
                        frmMakeKOT.btnNewCustomer.setText(txtCustomerName.getText());
                    }
                    else if (clsGlobalVarClass.gTransactionType.equals("Advance Order"))
                    {
                        clsGlobalVarClass.gCustCodeForAdvOrder = txtCustomerCode.getText();
                        clsGlobalVarClass.gCustomerName = txtCustomerName.getText();
                        clsGlobalVarClass.gBuildingCodeForHD = txtBuildingCode.getText().trim();
                        frmAdvanceOrder.lblCustInfo.setText(txtCustomerName.getText());
                    }
                }

                if (clsGlobalVarClass.gRFIDInterface.equals("Y"))
                {
                    if (funPostCustomerDataTORMS("Update"))
                    {
                        //clsGlobalVarClass.dbMysql.funCommitTransaction();
                        new frmOkPopUp(this, "Record Updated Successfully", "Successful", 3).setVisible(true);
                        funResetFields();
                        clsGlobalVarClass.funPostCustomerDataToHOPOS();
                    }
                    else
                    {
                        //clsGlobalVarClass.dbMysql.funRollbackTransaction();
                        new frmOkPopUp(this, "<html>Failed to Update Check Internet Connection <br>and Database credentials</html>", "ERROR", 3).setVisible(true);
                    }
                }
                else
                {
                    //clsGlobalVarClass.dbMysql.funCommitTransaction();
                    new frmOkPopUp(this, "Record Updated Successfully", "Successful", 3).setVisible(true);
                    if (clsGlobalVarClass.gCRMInterface.equalsIgnoreCase("PMAM"))
                    {
                        //funPOSTCustomerDataToPMAM();
                    }
                    funResetFields();

                    if (clsGlobalVarClass.gDataSendFrequency.equals("After Every Bill"))
                    {
                        clsGlobalVarClass.funPostCustomerDataToHOPOS();
                    }
                }
            }
        }
        catch (Exception e)
        {
            //clsGlobalVarClass.dbMysql.funRollbackTransaction();
            e.printStackTrace();
        }
    }

    private boolean funRegisterDebitCard(String custCode)
    {
        boolean flgCard = false;
        try
        {
            String status = "";
            String customerMobileNo = txtMobileNo.getText().trim();
            status = "Active";
            String debitCardNo = txtCardNumber.getText();
            String sqlCheckDuplicate = "";
            sqlCheckDuplicate = "select count(*) from tbldebitcardmaster where strCardNo='" + debitCardNo + "'";
            ResultSet rsCheckDuplicate = null;
            rsCheckDuplicate = clsGlobalVarClass.dbMysql.executeResultSet(sqlCheckDuplicate);
            rsCheckDuplicate.next();
            int cn = rsCheckDuplicate.getInt(1);
            sql = "select count(strCustomerCode) from tbldebitcardmaster where strCustomerCode='" + custCode + "'";
            ResultSet rsCustCode = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            rsCustCode.next();
            int cntCustCode = rsCustCode.getInt(1);
            rsCustCode.close();
            if (cn > 0)
            {
                new frmOkPopUp(this, "This Card Is Already Register", "Error", 0).setVisible(true);
                txtCardNumber.requestFocus();
            }
            else if (cntCustCode > 0) //for register card
            {
                new frmOkPopUp(this, "<html>The Customer Is Already Registered <br> with Different Card</html>", "Error", 0).setVisible(true);
            }
            else if (status.equals("Active")) //for register card
            {
                String debitCardCode = cmbCardType.getSelectedItem().toString();
                sql = "select dblCardValueFixed,dblMinCharge from tbldebitcardtype "
                        + "where strCardTypeCode='" + debitCardCode + "'";
                redeemAmt = 0.0;
                ResultSet rsCardValue = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                rsCardValue.next();
                cardValue = Double.parseDouble(rsCardValue.getString(1));
                minCharges = Double.parseDouble(rsCardValue.getString(2));
                redeemAmt = redeemAmt - (cardValue + minCharges);
                rsCardValue.close();
                sql = "insert into tbldebitcardmaster (strCardTypeCode,strCardNo,dblRedeemAmt,strStatus,"
                        + "strUserCreated,dteDateCreated,strCustomerCode,intPassword) "
                        + "values('" + debitCardCode + "','" + txtCardNumber.getText() + "','" + redeemAmt
                        + "','Active','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.getCurrentDateTime()
                        + "','" + custCode + "',1111)";
                int exc = clsGlobalVarClass.dbMysql.execute(sql);

                // Post Registered Debit Card from JPOS to RMS
                if (exc > 0)
                {
                    if (funPostDebitCardInfoToRMS("Save"))
                    {
                        String date = clsGlobalVarClass.getCurrentDateTime();
                        StringBuilder sb = new StringBuilder(date);
                        date = sb.delete(sb.indexOf(" "), sb.length()).toString();
                        String smsText = "Welcome to Sanskar Bazaar You have registered with Reward Card Your Card No is=" + debitCardNo;

                        ArrayList<String> mobileNoList = new ArrayList<>();
                        mobileNoList.add(customerMobileNo);
                        clsSMSSender objSMSSender = new clsSMSSender(mobileNoList, smsText);
                        objSMSSender.start();
                        //System.out.println(smsApiResponse);
                        new frmOkPopUp(this, "Card Registered Successfully", "Successful", 3).setVisible(true);
                        flgCard = true;
                    }
                }
            }
        }
        catch (Exception e)
        {
            flgCard = false;
            e.printStackTrace();
        }
        finally
        {
            return flgCard;
        }
    }

    public boolean funPostDebitCardInfoToRMS(String transType)
    {
        boolean flgRegDC = false;
        Connection conRMS = null;
        String extCode = "", status = "E";
        try
        {
            String rmsConURL = "jdbc:sqlserver://" + clsGlobalVarClass.gRFIDDBServerName + ":1433;user=" + clsGlobalVarClass.gRFIDDBUserName + ";password=" + clsGlobalVarClass.gRFIDDBPassword + ";database=" + clsGlobalVarClass.gRFIDDBName + "";
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conRMS = DriverManager.getConnection(rmsConURL);
            conRMS.setAutoCommit(false);
            Statement st = conRMS.createStatement();

            sql = "select strCustomerCode,strStatus from tbldebitcardmaster where strCardNo='" + txtCardNumber.getText().trim() + "'";
            ResultSet rsCustCode = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            if (rsCustCode.next())
            {
                String custCode = rsCustCode.getString(1);
                sql = "select strExternalCode from tblcustomermaster where strCustomerCode='" + custCode + "'";
                ResultSet rsExtCode = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                rsExtCode.next();
                extCode = rsExtCode.getString(1);
                rsExtCode.close();

                if (rsCustCode.getString(2).equals("Active"))
                {
                    status = "A";
                }
                else
                {
                    status = "E";
                }
                /*
                 * if(transType.equalsIgnoreCase("Save")) { sql = "insert into
                 * tblCustomerDebitCard(strCustomerCode,strDebitCardString,strStatus)
                 * " + "values('" + extCode + "','" +
                 * txtCardNumber.getText().trim() + "','" + status + "')"; }
                 * else { sql = "update tblCustomerDebitCard set
                 * strStatus='"+status+"' " + "where
                 * strCustomerCode='"+extCode+"'"; }
                 */

                String sql1 = "select b.strExternalCode,a.strCardNo,a.strStatus "
                        + "from tbldebitcardmaster a,tblcustomermaster b "
                        + "where a.strCustomerCode=b.strCustomerCode and a.strDataPostFlag='N'";
                ResultSet rsDebitCardData = clsGlobalVarClass.dbMysql.executeResultSet(sql1);
                while (rsDebitCardData.next())
                {
                    String deleteSql = "delete from tblCustomerDebitCard where strCustomerCode='" + rsDebitCardData.getString(1) + "'";
                    boolean delete = st.execute(deleteSql);
                    sql = "insert into tblCustomerDebitCard(strCustomerCode,strDebitCardString,strStatus) "
                            + "values('" + rsDebitCardData.getString(1) + "','" + rsDebitCardData.getString(2) + "',"
                            + "'A')";
                    boolean insert = st.execute(sql);;
                    String updateSql = "update tbldebitcardmaster set strDataPostFlag='Y' "
                            + "where strCardNo='" + rsDebitCardData.getString(2) + "'";
                    int update = clsGlobalVarClass.dbMysql.execute(updateSql);
                }
                rsDebitCardData.close();
            }
            rsCustCode.close();
            conRMS.commit();
            flgRegDC = true;
        }
        catch (Exception e)
        {
            flgRegDC = false;
            try
            {
                new frmOkPopUp(this, "<html>Failed to Save Check Internet Connection <br>and Database credentials</html>", "ERROR", 3).setVisible(true);
                conRMS.rollback();
            }
            catch (Exception ex)
            {
            }
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
            return flgRegDC;
        }
    }

    public void funResetFields()
    {
        btnNew.setText("SAVE");
        txtCustomerName.requestFocus();
        txtCustomerCode.setText("");
        txtCustomerName.setText("");
        txtMobileNo.setText("");
        txtExtCode.setText("");
        funFillCustTypeCombo();
        lblCardName.setText("");
        txtBillNo.setText("");
        txtBillAmount.setText("");
        txtTotalPoints.setText("");
        txtCardNumber.setText("");
        errorType = "";
        cmbCity.setSelectedItem("Mumbai");
        cmbOfficeCity.setSelectedItem("Mumbai");
        cmbState.setSelectedItem("Maharashtra");
        cmbOfficeState.setSelectedItem("Maharashtra");
        txtBuildingCode.setText("");
        txtBuildingName.setText("");
        txtOfficeBuildingCode.setText("");
        txtOfficeBuildingName.setText("");
        txtOfficeLandmark.setText("");
        txtOfficeMobileNo.setText("");
        txtOfficePincode.setText("");
        txtOfficeStreet.setText("");
        txtMobileNo.requestFocus();
        txtStreetName.setText("");
        txtPinCode.setText("");
        txtEmail.setText("");
        txtLandmark.setText("");
        txtCustAddress.setText("");
        txtTempCustAddress.setText("");
        txtTempStreetName.setText("");
        txtTempLandmark.setText("");
        txtGSTNo.setText("");

        if (clsGlobalVarClass.gNewCustomerForHomeDel || clsGlobalVarClass.gNewCustForAdvOrder)
        {
            clsGlobalVarClass.gNewCustomerForHomeDel = false;
            clsGlobalVarClass.gNewCustForAdvOrder = false;
            dispose();
        }

    }

    private String funGetBuildingCode() throws Exception
    {
        String code = "", buildingCode = "", strCode = "";
        long lastNo = 1;
        String query = "select ifnull(max(strBuildingCode),0) from tblbuildingmaster";
        ResultSet rsBuildingCode = clsGlobalVarClass.dbMysql.executeResultSet(query);
        if (rsBuildingCode.next())
        {
            code = rsBuildingCode.getString(1);
            if (!code.equalsIgnoreCase("0"))
            {
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
                lastNo = Long.parseLong(strCode);
                lastNo++;
                buildingCode = "B" + String.format("%07d", lastNo);
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
                buildingCode = "B" + String.format("%07d", lastNo);
            }
        }
        return buildingCode;
    }

    private void funCheckBuilding()
    {
        try
        {
            String sqlBuilding = "select strBuildingCode from tblbuildingmaster "
                    + "where strBuildingName='" + txtBuildingName.getText().trim() + "'";
            ResultSet rsBuilding = clsGlobalVarClass.dbMysql.executeResultSet(sqlBuilding);
            if (rsBuilding.next())
            {
                txtBuildingCode.setText(rsBuilding.getString(1));
            }
            else
            {
                txtBuildingCode.setText(funGetBuildingCode());
                String query = "insert into tblbuildingmaster (strBuildingCode,strBuildingName,strAddress"
                        + ",strUserCreated,strUserEdited,dteDateCreated,dteDateEdited,dblHomeDeliCharge"
                        + ",strClientCode,dblDeliveryBoyPayOut,dblHelperPayOut,strZoneCode)"
                        + " values('" + txtBuildingCode.getText() + "','" + txtBuildingName.getText() + "'"
                        + ",'NA','" + clsGlobalVarClass.gUserCode + "'" + ",'" + clsGlobalVarClass.gUserCode + "'"
                        + ",'" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "'"
                        + ",'0.00','" + clsGlobalVarClass.gClientCode + "',0.00,0.00,'')";
                //System.out.println(insertQuery);
                clsGlobalVarClass.dbMysql.execute(query);
                clsGlobalVarClass.funPostCustomerAreaDataToHOPOS();
            }
            rsBuilding.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

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
            txtBuildingName.setText(data[1].toString());
        }
        else
        {
            txtOfficeBuildingCode.setText(data[0].toString());
            txtOfficeBuildingName.setText(data[1].toString());
        }
    }

    private void funOpenCustomerSearch()
    {
        this.setAlwaysOnTop(false);
        buildingType = "residential";
        objUtility.funCallForSearchForm("CustomerMaster");
        new frmSearchFormDialog(this, true).setVisible(true);
        if (clsGlobalVarClass.gSearchItemClicked)
        {
            btnNew.setText("UPDATE");
            Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
            funSetCustomerData(data);
            clsGlobalVarClass.gSearchItemClicked = false;
        }
    }

    private void funFreeMemory()
    {
        objUtility = null;
    }

    private boolean funCheckUserAuthentication()
    {
        boolean isUserGranted = false;
        try
        {
            String[] options = new String[]
            {
                "OK", "Cancel"
            };
            txtUsername.setText("");
            txtPassword.setText("");
            txtUsername.requestFocus();
            int option = JOptionPane.showOptionDialog(null, panelUserAuthentication, "User Authentication!!!",
                    JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE,
                    null, options, txtUsername);
            if (option == 0) // pressing OK button
            {
                isUserGranted = funUserAuthenticationOKButtonPressed();
            }
            else
            {
                isUserGranted = false;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            return isUserGranted;
        }
    }

    private boolean funUserAuthenticationOKButtonPressed()
    {
        boolean isUserGranted = false;
        try
        {
            Date objDate = new Date();
            int day = objDate.getDate();
            int month = objDate.getMonth() + 1;
            int year = objDate.getYear() + 1900;
            String currentDate = year + "-" + month + "-" + day;
            if (txtUsername.getText().trim().equalsIgnoreCase("SANGUINE"))
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

                String userPassword = txtPassword.getText().trim();
                if (finalPassword.equalsIgnoreCase(userPassword))
                {
                    String userCode = txtUsername.getText();
                    String userName = "SANGUINE";
                    String userType = "Super";
                    String posAccessCode = "All POS";

                    funExportData();
                    isUserGranted = true;
                }
            }
            else
            {
                if (txtPassword.getText().length() == 0)
                {
                    String sql = "select count(*) from tbluserhd where strUsercode='" + txtUsername.getText() + "' ";
                    ResultSet rssql = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                    if (rssql.next())
                    {

                        sql = "  select count(*) from tbluserhd where strDebitCardString='" + txtUsername.getText() + "' ";
                        ResultSet rssql1 = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                        if (rssql1.next())
                        {
                            if (rssql1.getInt(1) > 0)
                            {
                                if (funCHeckLoginForDebitCardString(txtUsername.getText()))
                                {
                                    return true;
                                }
                            }
                            else
                            {
                                txtUsername.setText("");
                                new frmOkPopUp(null, "Invalid User", "Error", 1).setVisible(true);
                                return false;
                            }
                        }

                    }
                }
                String encKey = "04081977";
                String password = clsGlobalSingleObject.getObjPasswordEncryptDecreat().encrypt(encKey, txtPassword.getText().trim().toUpperCase());
                //System.out.println(password);
                String selectQuery = "select count(*),strUserName,strSuperType,dteValidDate,strPOSAccess from tbluserhd "
                        + "where strUserCode='" + txtUsername.getText() + "' and strPassword='" + password + "'";
                //System.out.println(selectQuery);
                ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery);
                rs.next();
                if (rs.getInt(1) == 1)
                {
                    String userCode = txtUsername.getText();
                    String userName = rs.getString(2);
                    String userType = rs.getString(3);
                    String posAccessCode = rs.getString(5);

                    selectQuery = "select count(*) from tbluserhd WHERE strUserCode = '" + txtUsername.getText()
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
                        funCloseAuthenticationDialog();
                        if (userType.equalsIgnoreCase("Super"))
                        {
                            funExportData();
                            isUserGranted = true;
                        }
                        else
                        {
                            sqlBuilder.setLength(0);
                            sqlBuilder.append("select a.strUserCode,a.strFormName,a.strGrant,a.strTLA "
                                    + "from tbluserdtl a "
                                    + "where a.strFormName='Customer Master' "
                                    + "and a.strGrant='true' "
                                    + "and a.strUserCode='" + userCode + "' ");
                            ResultSet rsTLA = clsGlobalVarClass.dbMysql.executeResultSet(sqlBuilder.toString());
                            if (rsTLA.next())
                            {
                                funExportData();
                                isUserGranted = true;
                            }
                            else
                            {
                                txtUsername.requestFocus();
                                new frmOkPopUp(null, "User \"" + userCode + "\" Not Granted.", "Error", 1).setVisible(true);
                            }
                            rsTLA.close();
                        }
                    }
                }
                else
                {
                    rs.close();
                    txtUsername.requestFocus();
                    new frmOkPopUp(null, "Login Failed", "Error", 1).setVisible(true);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            return isUserGranted;
        }
    }

    private boolean funCHeckLoginForDebitCardString(String user) throws Exception
    {
        boolean flgLoginStatus = false;
        String selectQuery = "select count(*),strUserName,strSuperType,dteValidDate,strPOSAccess,strUserCode from tbluserhd "
                + "where strDebitCardString='" + user + "'";
        //System.out.println(selectQuery);
        ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery);
        rs.next();
        if (rs.getInt(1) == 1)
        {
            String userCode = rs.getString(6);
            String userName = rs.getString(2);
            String userType = rs.getString(3);
            String posAccessCode = rs.getString(5);

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
                if (userType.equalsIgnoreCase("Super"))
                {
                    funCloseAuthenticationDialog();
                    flgLoginStatus = true;
                    funExportData();
                }
                else
                {
                    sqlBuilder.setLength(0);
                    sqlBuilder.append("select a.strUserCode,a.strFormName,a.strGrant,a.strTLA "
                            + "from tbluserdtl a "
                            + "where a.strFormName='Customer Master' "
                            + "and a.strGrant='true' "
                            + "and a.strUserCode='" + userCode + "' ");
                    ResultSet rsTLA = clsGlobalVarClass.dbMysql.executeResultSet(sqlBuilder.toString());
                    if (rsTLA.next())
                    {
                        funCloseAuthenticationDialog();
                        flgLoginStatus = true;
                        funExportData();
                    }
                    else
                    {
                        txtUsername.requestFocus();
                        new frmOkPopUp(null, "User \"" + userCode + " Not Granted.", "Error", 1).setVisible(true);
                    }
                    rsTLA.close();
                }
            }
        }
        return flgLoginStatus;
    }

    private void funCloseAuthenticationDialog()
    {
        Window[] windows = Window.getWindows();
        for (Window window : windows)
        {
            if (window instanceof JDialog)
            {
                JDialog dialog = (JDialog) window;
                if (dialog.getContentPane().getComponentCount() == 1 && dialog.getContentPane().getComponent(0) instanceof JOptionPane)
                {
                    dialog.dispose();
                }
            }
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

        panelUserAuthentication = new javax.swing.JPanel();
        lblUsername = new javax.swing.JLabel();
        lblPassword = new javax.swing.JLabel();
        txtUsername = new javax.swing.JTextField();
        txtPassword = new javax.swing.JPasswordField();
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
        panelMain = new JPanel() {  
            public void paintComponent(Graphics g) {  
                Image img = Toolkit.getDefaultToolkit().getImage(  
                    getClass().getResource("/com/POSTransaction/images/imgBackgroundImage.png"));  
                g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);  
            }  
        }; ;
        panelContainer = new javax.swing.JPanel();
        panelDeliveryCharges = new javax.swing.JTabbedPane();
        panelFormBody = new javax.swing.JPanel();
        lblCustomerCode = new javax.swing.JLabel();
        lblCustomerName = new javax.swing.JLabel();
        txtCustomerName = new javax.swing.JTextField();
        txtCustomerCode = new javax.swing.JTextField();
        lblFormName = new javax.swing.JLabel();
        lblMobileNo = new javax.swing.JLabel();
        lblCustType = new javax.swing.JLabel();
        cmbCustType = new javax.swing.JComboBox();
        txtMobileNo = new javax.swing.JTextField();
        panelDebitCard = new javax.swing.JPanel();
        txtCardNumber = new javax.swing.JTextField();
        btnSwipeCard = new javax.swing.JButton();
        lblTotalPoints = new javax.swing.JLabel();
        txtTotalPoints = new javax.swing.JTextField();
        lblBillNo = new javax.swing.JLabel();
        txtBillNo = new javax.swing.JTextField();
        txtBillAmount = new javax.swing.JTextField();
        lblCardType = new javax.swing.JLabel();
        lblcardName = new javax.swing.JLabel();
        cmbCardType = new javax.swing.JComboBox();
        lblCardName = new javax.swing.JLabel();
        panelBuilding = new javax.swing.JPanel();
        lblStreetName = new javax.swing.JLabel();
        cmbCity = new javax.swing.JComboBox();
        lblCity = new javax.swing.JLabel();
        lblState = new javax.swing.JLabel();
        cmbState = new javax.swing.JComboBox();
        txtStreetName = new javax.swing.JTextField();
        lblPinCode = new javax.swing.JLabel();
        txtPinCode = new javax.swing.JTextField();
        txtCustAddress = new javax.swing.JTextField();
        lblCustAddress = new javax.swing.JLabel();
        lblLandmark = new javax.swing.JLabel();
        txtLandmark = new javax.swing.JTextField();
        panelOtherDetail = new javax.swing.JPanel();
        lblExtCode = new javax.swing.JLabel();
        txtExtCode = new javax.swing.JTextField();
        lblEmail = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        lblDOB = new javax.swing.JLabel();
        dteDOB = new com.toedter.calendar.JDateChooser();
        lblGender = new javax.swing.JLabel();
        cmbGender = new javax.swing.JComboBox();
        lblAnniversory = new javax.swing.JLabel();
        dteAnniversory = new com.toedter.calendar.JDateChooser();
        lblBuildingCode = new javax.swing.JLabel();
        txtBuildingCode = new javax.swing.JTextField();
        txtBuildingName = new javax.swing.JTextField();
        panelOfficeAddress = new javax.swing.JPanel();
        lblBuildingName1 = new javax.swing.JLabel();
        txtOfficeBuildingName = new javax.swing.JTextField();
        lblRoadStreetName1 = new javax.swing.JLabel();
        txtOfficeStreet = new javax.swing.JTextField();
        lblLandmark1 = new javax.swing.JLabel();
        txtOfficeLandmark = new javax.swing.JTextField();
        lblCity1 = new javax.swing.JLabel();
        cmbOfficeCity = new javax.swing.JComboBox();
        lblState1 = new javax.swing.JLabel();
        cmbOfficeState = new javax.swing.JComboBox();
        lblPinCode1 = new javax.swing.JLabel();
        txtOfficePincode = new javax.swing.JTextField();
        lblOfficeNo = new javax.swing.JLabel();
        txtOfficeMobileNo = new javax.swing.JTextField();
        txtOfficeBuildingCode = new javax.swing.JTextField();
        lblGSTNo = new javax.swing.JLabel();
        txtGSTNo = new javax.swing.JTextField();
        panelTemporaryAddress = new javax.swing.JPanel();
        lblTempCustAddress = new javax.swing.JLabel();
        txtTempCustAddress = new javax.swing.JTextField();
        lblTempStreetName = new javax.swing.JLabel();
        txtTempStreetName = new javax.swing.JTextField();
        lblTempLandmark = new javax.swing.JLabel();
        txtTempLandmark = new javax.swing.JTextField();
        btnCancel = new javax.swing.JButton();
        btnNew = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        btnExport = new javax.swing.JButton();

        lblUsername.setText("Enter a Username:");

        lblPassword.setText("Enter a Password:");

        txtUsername.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtUsernameMouseClicked(evt);
            }
        });
        txtUsername.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtUsernameActionPerformed(evt);
            }
        });
        txtUsername.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtUsernameKeyPressed(evt);
            }
        });

        txtPassword.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtPasswordMouseClicked(evt);
            }
        });
        txtPassword.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtPasswordKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt)
            {
                txtPasswordKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout panelUserAuthenticationLayout = new javax.swing.GroupLayout(panelUserAuthentication);
        panelUserAuthentication.setLayout(panelUserAuthenticationLayout);
        panelUserAuthenticationLayout.setHorizontalGroup(
            panelUserAuthenticationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelUserAuthenticationLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelUserAuthenticationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelUserAuthenticationLayout.createSequentialGroup()
                        .addComponent(lblPassword)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(panelUserAuthenticationLayout.createSequentialGroup()
                        .addComponent(lblUsername)
                        .addGap(2, 2, 2)))
                .addGroup(panelUserAuthenticationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(txtUsername)
                    .addComponent(txtPassword, javax.swing.GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelUserAuthenticationLayout.setVerticalGroup(
            panelUserAuthenticationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelUserAuthenticationLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelUserAuthenticationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelUserAuthenticationLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(txtUsername)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelUserAuthenticationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

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
        lblProductName.setText("SPOS -  ");
        lblProductName.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                lblProductNameMouseClicked(evt);
            }
        });
        panelHeader.add(lblProductName);

        lblModuleName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblModuleName.setForeground(new java.awt.Color(255, 255, 255));
        panelHeader.add(lblModuleName);

        lblformName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblformName.setForeground(new java.awt.Color(255, 255, 255));
        lblformName.setText("- Customer Master");
        lblformName.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                lblformNameMouseClicked(evt);
            }
        });
        panelHeader.add(lblformName);
        panelHeader.add(filler4);
        panelHeader.add(filler5);

        lblPosName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblPosName.setForeground(new java.awt.Color(255, 255, 255));
        lblPosName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPosName.setMaximumSize(new java.awt.Dimension(321, 30));
        lblPosName.setMinimumSize(new java.awt.Dimension(321, 30));
        lblPosName.setPreferredSize(new java.awt.Dimension(321, 30));
        lblPosName.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                lblPosNameMouseClicked(evt);
            }
        });
        panelHeader.add(lblPosName);

        lblUserCode.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblUserCode.setForeground(new java.awt.Color(255, 255, 255));
        lblUserCode.setMaximumSize(new java.awt.Dimension(90, 30));
        lblUserCode.setMinimumSize(new java.awt.Dimension(90, 30));
        lblUserCode.setPreferredSize(new java.awt.Dimension(90, 30));
        lblUserCode.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                lblUserCodeMouseClicked(evt);
            }
        });
        panelHeader.add(lblUserCode);
        panelHeader.add(filler6);

        lblDate.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblDate.setForeground(new java.awt.Color(255, 255, 255));
        lblDate.setMaximumSize(new java.awt.Dimension(192, 30));
        lblDate.setMinimumSize(new java.awt.Dimension(192, 30));
        lblDate.setPreferredSize(new java.awt.Dimension(192, 30));
        lblDate.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                lblDateMouseClicked(evt);
            }
        });
        panelHeader.add(lblDate);

        lblHOSign.setMaximumSize(new java.awt.Dimension(34, 30));
        lblHOSign.setMinimumSize(new java.awt.Dimension(34, 30));
        lblHOSign.setPreferredSize(new java.awt.Dimension(34, 30));
        lblHOSign.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                lblHOSignMouseClicked(evt);
            }
        });
        panelHeader.add(lblHOSign);

        getContentPane().add(panelHeader, java.awt.BorderLayout.PAGE_START);

        panelMain.setOpaque(false);
        panelMain.setLayout(new java.awt.GridBagLayout());

        panelContainer.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        panelContainer.setMinimumSize(new java.awt.Dimension(800, 570));
        panelContainer.setOpaque(false);

        panelFormBody.setBackground(new java.awt.Color(255, 255, 255));
        panelFormBody.setOpaque(false);
        panelFormBody.setPreferredSize(new java.awt.Dimension(610, 600));
        panelFormBody.setLayout(null);

        lblCustomerCode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblCustomerCode.setText("Customer Code   :");
        panelFormBody.add(lblCustomerCode);
        lblCustomerCode.setBounds(10, 60, 100, 30);

        lblCustomerName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblCustomerName.setText("Customer Name  :");
        panelFormBody.add(lblCustomerName);
        lblCustomerName.setBounds(10, 140, 100, 30);

        txtCustomerName.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtCustomerNameMouseClicked(evt);
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
        panelFormBody.add(txtCustomerName);
        txtCustomerName.setBounds(120, 140, 260, 30);

        txtCustomerCode.setEditable(false);
        txtCustomerCode.setEnabled(false);
        txtCustomerCode.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtCustomerCodeMouseClicked(evt);
            }
        });
        txtCustomerCode.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtCustomerCodeKeyPressed(evt);
            }
        });
        panelFormBody.add(txtCustomerCode);
        txtCustomerCode.setBounds(120, 60, 150, 30);

        lblFormName.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        lblFormName.setForeground(new java.awt.Color(24, 19, 19));
        lblFormName.setText("Customer Master");
        panelFormBody.add(lblFormName);
        lblFormName.setBounds(300, 0, 190, 30);

        lblMobileNo.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblMobileNo.setText("Contact Nos       :");
        panelFormBody.add(lblMobileNo);
        lblMobileNo.setBounds(10, 100, 100, 30);

        lblCustType.setText("Customer Type       :");
        panelFormBody.add(lblCustType);
        lblCustType.setBounds(10, 180, 110, 30);

        cmbCustType.setToolTipText("Customer Type List");
        cmbCustType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbCustTypeActionPerformed(evt);
            }
        });
        cmbCustType.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbCustTypeKeyPressed(evt);
            }
        });
        panelFormBody.add(cmbCustType);
        cmbCustType.setBounds(120, 180, 260, 30);

        txtMobileNo.addFocusListener(new java.awt.event.FocusAdapter()
        {
            public void focusLost(java.awt.event.FocusEvent evt)
            {
                txtMobileNoFocusLost(evt);
            }
        });
        txtMobileNo.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtMobileNoMouseClicked(evt);
            }
        });
        txtMobileNo.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtMobileNoActionPerformed(evt);
            }
        });
        txtMobileNo.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtMobileNoKeyPressed(evt);
            }
        });
        panelFormBody.add(txtMobileNo);
        txtMobileNo.setBounds(120, 100, 260, 30);

        panelDebitCard.setOpaque(false);

        txtCardNumber.setEditable(false);
        txtCardNumber.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtCardNumber.setFocusable(false);

        btnSwipeCard.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnSwipeCard.setForeground(new java.awt.Color(255, 255, 255));
        btnSwipeCard.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnSwipeCard.setText("Swipe..");
        btnSwipeCard.setToolTipText("Swipe Card");
        btnSwipeCard.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSwipeCard.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnSwipeCard.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnSwipeCardMouseClicked(evt);
            }
        });

        lblTotalPoints.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblTotalPoints.setText("Total Points   :");

        txtTotalPoints.setEditable(false);

        lblBillNo.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblBillNo.setText("Bill No        :");

        txtBillNo.addFocusListener(new java.awt.event.FocusAdapter()
        {
            public void focusLost(java.awt.event.FocusEvent evt)
            {
                txtBillNoFocusLost(evt);
            }
        });
        txtBillNo.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtBillNoKeyPressed(evt);
            }
        });

        txtBillAmount.setEditable(false);

        lblCardType.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblCardType.setText("Debit Card Type  :");

        lblcardName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblcardName.setText("Card Name  :");

        cmbCardType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbCardTypeActionPerformed(evt);
            }
        });

        lblCardName.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

        javax.swing.GroupLayout panelDebitCardLayout = new javax.swing.GroupLayout(panelDebitCard);
        panelDebitCard.setLayout(panelDebitCardLayout);
        panelDebitCardLayout.setHorizontalGroup(
            panelDebitCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelDebitCardLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelDebitCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panelDebitCardLayout.createSequentialGroup()
                        .addComponent(lblCardType, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(cmbCardType, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelDebitCardLayout.createSequentialGroup()
                        .addComponent(lblcardName, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblCardName, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelDebitCardLayout.createSequentialGroup()
                        .addComponent(txtCardNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(btnSwipeCard, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelDebitCardLayout.createSequentialGroup()
                        .addGroup(panelDebitCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelDebitCardLayout.createSequentialGroup()
                                .addComponent(lblTotalPoints, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addComponent(txtTotalPoints))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelDebitCardLayout.createSequentialGroup()
                                .addComponent(lblBillNo, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addComponent(txtBillNo, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(10, 10, 10)
                        .addComponent(txtBillAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(20, 20, 20))
        );
        panelDebitCardLayout.setVerticalGroup(
            panelDebitCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelDebitCardLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(panelDebitCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblCardType, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbCardType, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelDebitCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblcardName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCardName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelDebitCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblBillNo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtBillNo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtBillAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelDebitCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTotalPoints, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTotalPoints, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelDebitCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtCardNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSwipeCard, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelFormBody.add(panelDebitCard);
        panelDebitCard.setBounds(400, 280, 390, 210);

        panelBuilding.setOpaque(false);

        lblStreetName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblStreetName.setText("Street Name      :");

        cmbCity.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmbCity.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Select City", "Agalgaon", "Agartala", "Agra", "Ahmedabad", "Ahmednagar", "Aizawl", "Ajmer", "Akluj", "Akola", "Akot", "Allahabad", "Allepey", "Amalner", "Ambernath", "Amravati", "Amritsar", "Anand", "Arvi", "Asansol", "Ashta", "Aurangabad", "Aziwal", "Baddi", "Bangalore", "Bansarola", "Baramati", "Bareilly", "Baroda", "Barshi", "Beed", "Belgum", "Bellary", "Bhandara", "Bhilai", "Bhivandi", "Bhiwandi", "Bhopal", "Bhubaneshwar", "Bhusawal", "Bikaner", "Bokaro", "Bombay", "Buldhana", "Burhanpur", "Chandigad", "Chandigarh", "Chattisgad", "Chennai", "Chennai(Madras)", "Cochin", "Coimbature", "Dehradun", "Delhi", "Dhanbad", "Dhule", "Dispur", "Faridabad", "Gandhinagar", "Gangtok", "Goa", "Gujrat", "Gurgaon", "Guwahati", "Gwalior", "Hyderabad", "Ichalkaranji", "Imphal", "Indapur", "Indore", "Itanagar", "Jabalpur", "Jaipur", "Jalandhar", "Jalgaon", "Jalna", "Jammu", "Jamshedpur", "Kalamnuri", "Kalyan", "Kanpur", "Karad", "Kinshasa", "Kochi(Cochin)", "Kohima", "Kolhapur", "Kolkata", "Kolkata(Calcutta)", "Kozhikode(Calicut)", "Latur", "Lucknow", "Ludhiana", "Madurai", "Mangalvedha", "Manmad", "Meerut", "Mumbai", "Mumbai(Bombay)", "Muscat", "Mysore", "Nagpur", "Nanded", "Nandurbar", "Nashik", "Orisa", "Osmanabad", "Pachora", "Panaji", "Pandharpur", "Parbhani", "Patna", "Pratapgad", "Pune", "Raipur", "Rajasthan", "Rajkot", "Ranchi", "Ratnagiri", "Salalah", "Salem", "Sangamner", "Sangli", "Satara", "Sawantwadi", "Seawood", "Secunderabad", "Shillong", "Shimla", "Shirdi", "Sindhudurga", "Solapur", "Srinagar", "Surat", "Thane", "Thiruvananthapuram", "Tiruchirapalli", "Vadodara(Baroda)", "Varanasi(Benares)", "Vashi", "Vijayawada", "Visakhapatnam", "Yawatmal", "Other" }));
        cmbCity.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbCityActionPerformed(evt);
            }
        });
        cmbCity.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbCityKeyPressed(evt);
            }
        });

        lblCity.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblCity.setText("City                  :");

        lblState.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblState.setText("State               :");

        cmbState.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmbState.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Select State", "Andaman", "Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chandigarh", "Chhattisgarh", "Congo", "Delhi", "Goa", "Gujarat", "Haryana", "Himachal Pradesh", "Jammu & Kashmir", "Jharkhand", "Karnataka", "Kerala", "Lakshadweep", "Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya", "Mizoram", "Muscat", "Nagaland", "Odisha", "Pondicherry", "Punjab", "Rajasthan", "Salalah", "Sikkim", "Tamil Nadu", "Telangana", "Tripura", "Uttar Pradesh", "Uttarakhand", "Uttaranchal", "West Bengal", "Other" }));
        cmbState.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbStateKeyPressed(evt);
            }
        });

        txtStreetName.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtStreetNameMouseClicked(evt);
            }
        });
        txtStreetName.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtStreetNameKeyPressed(evt);
            }
        });

        lblPinCode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblPinCode.setText("Pin Code           :");

        txtPinCode.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtPinCode.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtPinCodeMouseClicked(evt);
            }
        });
        txtPinCode.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtPinCodeKeyPressed(evt);
            }
        });

        txtCustAddress.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtCustAddressMouseClicked(evt);
            }
        });
        txtCustAddress.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtCustAddressKeyPressed(evt);
            }
        });

        lblCustAddress.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblCustAddress.setText("Address/Flat No. :");

        lblLandmark.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblLandmark.setText("Landmark          :");

        txtLandmark.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtLandmarkMouseClicked(evt);
            }
        });
        txtLandmark.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtLandmarkKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout panelBuildingLayout = new javax.swing.GroupLayout(panelBuilding);
        panelBuilding.setLayout(panelBuildingLayout);
        panelBuildingLayout.setHorizontalGroup(
            panelBuildingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBuildingLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelBuildingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBuildingLayout.createSequentialGroup()
                        .addGroup(panelBuildingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelBuildingLayout.createSequentialGroup()
                                .addComponent(lblPinCode, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtPinCode, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelBuildingLayout.createSequentialGroup()
                                .addGroup(panelBuildingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(lblCity, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblState, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(4, 4, 4)
                                .addGroup(panelBuildingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(cmbState, 0, 0, Short.MAX_VALUE)
                                    .addComponent(cmbCity, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 113, Short.MAX_VALUE))
                    .addGroup(panelBuildingLayout.createSequentialGroup()
                        .addGroup(panelBuildingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblStreetName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblCustAddress, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelBuildingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtCustAddress)
                            .addComponent(txtStreetName)))
                    .addGroup(panelBuildingLayout.createSequentialGroup()
                        .addComponent(lblLandmark, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtLandmark)))
                .addContainerGap())
        );
        panelBuildingLayout.setVerticalGroup(
            panelBuildingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBuildingLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelBuildingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtCustAddress, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
                    .addComponent(lblCustAddress, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelBuildingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtStreetName, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelBuildingLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(lblStreetName, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelBuildingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtLandmark, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelBuildingLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(lblLandmark, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelBuildingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtPinCode, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelBuildingLayout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(lblPinCode, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelBuildingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbCity, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCity, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelBuildingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(cmbState, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblState, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        panelFormBody.add(panelBuilding);
        panelBuilding.setBounds(0, 260, 380, 230);

        panelOtherDetail.setOpaque(false);
        panelOtherDetail.setLayout(null);

        lblExtCode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblExtCode.setText("External Code/Permit No.   :");
        panelOtherDetail.add(lblExtCode);
        lblExtCode.setBounds(10, 10, 160, 30);

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
        panelOtherDetail.add(txtExtCode);
        txtExtCode.setBounds(170, 10, 220, 30);

        lblEmail.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblEmail.setText("Email ID           :");
        panelOtherDetail.add(lblEmail);
        lblEmail.setBounds(10, 180, 100, 30);

        txtEmail.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtEmailMouseClicked(evt);
            }
        });
        txtEmail.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtEmailKeyPressed(evt);
            }
        });
        panelOtherDetail.add(txtEmail);
        txtEmail.setBounds(130, 180, 260, 30);

        lblDOB.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblDOB.setText("D.O.B.             :");
        panelOtherDetail.add(lblDOB);
        lblDOB.setBounds(10, 50, 100, 30);

        dteDOB.setPreferredSize(new java.awt.Dimension(119, 35));
        dteDOB.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                dteDOBKeyPressed(evt);
            }
        });
        panelOtherDetail.add(dteDOB);
        dteDOB.setBounds(130, 50, 150, 30);

        lblGender.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblGender.setText("Gender            :");
        panelOtherDetail.add(lblGender);
        lblGender.setBounds(10, 90, 100, 30);

        cmbGender.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Male", "Female" }));
        cmbGender.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbGenderKeyPressed(evt);
            }
        });
        panelOtherDetail.add(cmbGender);
        cmbGender.setBounds(130, 90, 150, 30);

        lblAnniversory.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblAnniversory.setText("Anniversary       :");
        panelOtherDetail.add(lblAnniversory);
        lblAnniversory.setBounds(10, 140, 100, 30);

        dteAnniversory.setPreferredSize(new java.awt.Dimension(119, 35));
        dteAnniversory.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                dteAnniversoryKeyPressed(evt);
            }
        });
        panelOtherDetail.add(dteAnniversory);
        dteAnniversory.setBounds(130, 140, 150, 30);

        panelFormBody.add(panelOtherDetail);
        panelOtherDetail.setBounds(400, 40, 390, 220);

        lblBuildingCode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblBuildingCode.setText("Area                 :");
        panelFormBody.add(lblBuildingCode);
        lblBuildingCode.setBounds(10, 220, 106, 30);

        txtBuildingCode.setEditable(false);
        txtBuildingCode.setEnabled(false);
        txtBuildingCode.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtBuildingCodeMouseClicked(evt);
            }
        });
        txtBuildingCode.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtBuildingCodeActionPerformed(evt);
            }
        });
        txtBuildingCode.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtBuildingCodeKeyPressed(evt);
            }
        });
        panelFormBody.add(txtBuildingCode);
        txtBuildingCode.setBounds(120, 220, 92, 30);

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
        panelFormBody.add(txtBuildingName);
        txtBuildingName.setBounds(220, 220, 146, 32);

        panelDeliveryCharges.addTab("Customer", panelFormBody);

        panelOfficeAddress.setOpaque(false);
        panelOfficeAddress.setLayout(null);

        lblBuildingName1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblBuildingName1.setText("Building Name /Flat No  :");
        panelOfficeAddress.add(lblBuildingName1);
        lblBuildingName1.setBounds(30, 120, 150, 30);

        txtOfficeBuildingName.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtOfficeBuildingNameMouseClicked(evt);
            }
        });
        panelOfficeAddress.add(txtOfficeBuildingName);
        txtOfficeBuildingName.setBounds(270, 120, 140, 30);

        lblRoadStreetName1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblRoadStreetName1.setText("Road / Street Name     :");
        panelOfficeAddress.add(lblRoadStreetName1);
        lblRoadStreetName1.setBounds(30, 180, 150, 30);

        txtOfficeStreet.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtOfficeStreetMouseClicked(evt);
            }
        });
        panelOfficeAddress.add(txtOfficeStreet);
        txtOfficeStreet.setBounds(180, 180, 230, 30);

        lblLandmark1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblLandmark1.setText("Area                          :");
        panelOfficeAddress.add(lblLandmark1);
        lblLandmark1.setBounds(30, 240, 150, 30);

        txtOfficeLandmark.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtOfficeLandmarkMouseClicked(evt);
            }
        });
        panelOfficeAddress.add(txtOfficeLandmark);
        txtOfficeLandmark.setBounds(180, 240, 230, 30);

        lblCity1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblCity1.setText("City                       :");
        panelOfficeAddress.add(lblCity1);
        lblCity1.setBounds(470, 180, 150, 30);

        cmbOfficeCity.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmbOfficeCity.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Select City", "Agalgaon", "Agartala", "Agra", "Ahmedabad", "Ahmednagar", "Ajmer", "Akluj", "Akola", "Akot", "Allahabad", "Allepey", "Amalner", "Ambernath", "Amravati", "Amritsar", "Anand", "Arvi", "Asansol", "Ashta", "Aurangabad", "Aziwal", "Baddi", "Bangalore", "Bansarola", "Baramati", "Bareilly", "Baroda", "Barshi", "Beed", "Belgum", "Bellary", "Bhandara", "Bhilai", "Bhivandi", "Bhiwandi", "Bhopal", "Bhubaneshwar", "Bhusawal", "Bikaner", "Bokaro", "Bombay", "Buldhana", "Burhanpur", "Chandigad", "Chandigarh", "Chandigarh", "Chattisgad", "Chennai", "Chennai(Madras)", "Cochin", "Coimbature", "Dehradun", "Delhi", "Dhanbad", "Dhule", "Faridabad", "Gandhinagar", "Goa", "Gujrat", "Gurgaon", "Guwahati", "Gwalior", "Hyderabad", "Ichalkaranji", "Indapur", "Indore", "Jabalpur", "Jaipur", "Jalandhar", "Jalgaon", "Jalna", "Jamshedpur", "Kalamnuri", "Kalyan", "Kanpur", "Karad", "Kinshasa", "Kochi(Cochin)", "Kolhapur", "Kolkata(Calcutta)", "Kozhikode(Calicut)", "Latur", "Lucknow", "Ludhiana", "Madurai", "Mangalvedha", "Manmad", "Meerut", "Mumbai", "Mumbai(Bombay)", "Muscat", "Mysore", "Nagpur", "Nanded", "Nandurbar", "Nashik", "Orisa", "Osmanabad", "Pachora", "Pandharpur", "Parbhani", "Patna", "Pratapgad", "Pune", "Raipur", "Rajasthan", "Rajkot", "Ranchi", "Ratnagiri", "Salalah", "Salem", "Sangamner", "Sangli", "Satara", "Sawantwadi", "Seawood", "Secunderabad", "Shirdi", "Sindhudurga", "Solapur", "Srinagar", "Surat", "Thane", "Tiruchirapalli", "Vadodara(Baroda)", "Varanasi(Benares)", "Vashi", "Vijayawada", "Visakhapatnam", "Yawatmal", "Other" }));
        panelOfficeAddress.add(cmbOfficeCity);
        cmbOfficeCity.setBounds(620, 180, 140, 30);

        lblState1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblState1.setText("State                     :");
        panelOfficeAddress.add(lblState1);
        lblState1.setBounds(470, 240, 150, 30);

        cmbOfficeState.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmbOfficeState.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Select State", "Andaman", "Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chandigarh", "Chhattisgarh", "Congo", "Delhi", "Goa", "Gujarat", "Gujrat", "Haryana", "Himachal Pradesh", "Jammu & Kashmir", "Jharkhand", "Karnataka", "Kerala", "Lakshadweep", "Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya", "Mizoram", "Muscat", "Nagaland", "Orissa", "Pondicherry", "Punjab", "Rajasthan", "Salalah", "Sikkim", "Tamil Nadu", "TamilNadu", "Telangana", "Tripura", "Uttar Pradesh", "Uttaranchal", "West Bengal", "Other" }));
        panelOfficeAddress.add(cmbOfficeState);
        cmbOfficeState.setBounds(620, 240, 140, 30);

        lblPinCode1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblPinCode1.setText("Pin Code                 :");
        panelOfficeAddress.add(lblPinCode1);
        lblPinCode1.setBounds(470, 300, 150, 30);

        txtOfficePincode.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtOfficePincode.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtOfficePincodeMouseClicked(evt);
            }
        });
        panelOfficeAddress.add(txtOfficePincode);
        txtOfficePincode.setBounds(620, 300, 140, 30);

        lblOfficeNo.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblOfficeNo.setText("Office No.              :");
        panelOfficeAddress.add(lblOfficeNo);
        lblOfficeNo.setBounds(470, 120, 150, 30);

        txtOfficeMobileNo.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtOfficeMobileNoMouseClicked(evt);
            }
        });
        panelOfficeAddress.add(txtOfficeMobileNo);
        txtOfficeMobileNo.setBounds(620, 120, 140, 30);

        txtOfficeBuildingCode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtOfficeBuildingCode.setEnabled(false);
        txtOfficeBuildingCode.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtOfficeBuildingCodeMouseClicked(evt);
            }
        });
        panelOfficeAddress.add(txtOfficeBuildingCode);
        txtOfficeBuildingCode.setBounds(180, 120, 90, 30);

        lblGSTNo.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblGSTNo.setText("GST No.                    :");
        panelOfficeAddress.add(lblGSTNo);
        lblGSTNo.setBounds(30, 300, 150, 30);

        txtGSTNo.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtGSTNoMouseClicked(evt);
            }
        });
        panelOfficeAddress.add(txtGSTNo);
        txtGSTNo.setBounds(180, 300, 230, 30);

        panelDeliveryCharges.addTab("Office Address", panelOfficeAddress);

        panelTemporaryAddress.setOpaque(false);

        lblTempCustAddress.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblTempCustAddress.setText("Temp Address    :");

        txtTempCustAddress.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtTempCustAddressMouseClicked(evt);
            }
        });
        txtTempCustAddress.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtTempCustAddressKeyPressed(evt);
            }
        });

        lblTempStreetName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblTempStreetName.setText("Street Name      :");

        txtTempStreetName.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtTempStreetNameMouseClicked(evt);
            }
        });
        txtTempStreetName.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtTempStreetNameKeyPressed(evt);
            }
        });

        lblTempLandmark.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblTempLandmark.setText("Landmark          :");

        txtTempLandmark.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtTempLandmarkMouseClicked(evt);
            }
        });
        txtTempLandmark.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtTempLandmarkKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout panelTemporaryAddressLayout = new javax.swing.GroupLayout(panelTemporaryAddress);
        panelTemporaryAddress.setLayout(panelTemporaryAddressLayout);
        panelTemporaryAddressLayout.setHorizontalGroup(
            panelTemporaryAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTemporaryAddressLayout.createSequentialGroup()
                .addGap(118, 118, 118)
                .addGroup(panelTemporaryAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelTemporaryAddressLayout.createSequentialGroup()
                        .addGroup(panelTemporaryAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblTempStreetName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblTempCustAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelTemporaryAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtTempStreetName, javax.swing.GroupLayout.DEFAULT_SIZE, 403, Short.MAX_VALUE)
                            .addComponent(txtTempCustAddress)))
                    .addGroup(panelTemporaryAddressLayout.createSequentialGroup()
                        .addComponent(lblTempLandmark, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTempLandmark)))
                .addGap(159, 159, 159))
        );
        panelTemporaryAddressLayout.setVerticalGroup(
            panelTemporaryAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTemporaryAddressLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(panelTemporaryAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtTempCustAddress)
                    .addComponent(lblTempCustAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelTemporaryAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtTempStreetName, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelTemporaryAddressLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(lblTempStreetName, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelTemporaryAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtTempLandmark, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelTemporaryAddressLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(lblTempLandmark, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(355, Short.MAX_VALUE))
        );

        panelDeliveryCharges.addTab("Temporary Address", panelTemporaryAddress);

        btnCancel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnCancel.setForeground(new java.awt.Color(255, 255, 255));
        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnCancel.setText("CLOSE");
        btnCancel.setToolTipText("Close Customer Master Form");
        btnCancel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCancel.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgRolloverIconForButtons.png"))); // NOI18N
        btnCancel.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
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

        btnNew.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnNew.setForeground(new java.awt.Color(255, 255, 255));
        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnNew.setText("SAVE");
        btnNew.setToolTipText("Save Customer Master");
        btnNew.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNew.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgRolloverIconForButtons.png"))); // NOI18N
        btnNew.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnNew.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnNewActionPerformed(evt);
            }
        });

        btnReset.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnReset.setForeground(new java.awt.Color(255, 255, 255));
        btnReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnReset.setText("RESET");
        btnReset.setToolTipText("Reset Customer Master Form");
        btnReset.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnReset.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgRolloverIconForButtons.png"))); // NOI18N
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

        btnExport.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnExport.setForeground(new java.awt.Color(255, 255, 255));
        btnExport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnExport.setText("<html>Export <br>Data</html>");
        btnExport.setToolTipText("Export Customer Master Data");
        btnExport.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnExport.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgRolloverIconForButtons.png"))); // NOI18N
        btnExport.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnExport.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnExportMouseClicked(evt);
            }
        });
        btnExport.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnExportActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelContainerLayout = new javax.swing.GroupLayout(panelContainer);
        panelContainer.setLayout(panelContainerLayout);
        panelContainerLayout.setHorizontalGroup(
            panelContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelContainerLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(panelDeliveryCharges, javax.swing.GroupLayout.PREFERRED_SIZE, 795, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(panelContainerLayout.createSequentialGroup()
                .addGap(118, 118, 118)
                .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(70, 70, 70)
                .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(70, 70, 70)
                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50)
                .addComponent(btnExport, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelContainerLayout.setVerticalGroup(
            panelContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelContainerLayout.createSequentialGroup()
                .addComponent(panelDeliveryCharges, javax.swing.GroupLayout.PREFERRED_SIZE, 522, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnExport, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        panelMain.add(panelContainer, new java.awt.GridBagConstraints());

        getContentPane().add(panelMain, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtCustomerNameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtCustomerNameMouseClicked
        // TODO add your handling code here:
        if (txtCustomerName.getText().length() == 0)
        {
            new frmAlfaNumericKeyBoard(this, true, "1", "Enter Customer Name").setVisible(true);
            txtCustomerName.setText(clsGlobalVarClass.gKeyboardValue);
        }
        else
        {
            new frmAlfaNumericKeyBoard(this, true, txtCustomerName.getText(), "1", "Enter Customer Name").setVisible(true);
            txtCustomerName.setText(clsGlobalVarClass.gKeyboardValue);
        }
    }//GEN-LAST:event_txtCustomerNameMouseClicked

    private void txtCustomerNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCustomerNameKeyPressed
        // TODO add your handling code here:  
        if (evt.getKeyCode() == 10)
        {
            cmbCustType.requestFocus();
        }
    }//GEN-LAST:event_txtCustomerNameKeyPressed

    private void txtCustomerNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCustomerNameKeyReleased
        // TODO add your handling code here:
        txtCustomerName.setText(txtCustomerName.getText().toUpperCase());
    }//GEN-LAST:event_txtCustomerNameKeyReleased

    private void txtCustomerCodeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtCustomerCodeMouseClicked
        // TODO add your handling code here:
        funOpenCustomerSearch();
    }//GEN-LAST:event_txtCustomerCodeMouseClicked

    private void cmbCustTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbCustTypeActionPerformed
        funCustomerTypeComboSelect();
    }//GEN-LAST:event_cmbCustTypeActionPerformed

    private void cmbCustTypeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbCustTypeKeyPressed
        // TODO add your handling code here:
        /*
         * if (evt.getKeyCode() == 10) { txtExtCode.requestFocus(); }
         */
        if (evt.getKeyCode() == 10)
        {
            if (clsGlobalVarClass.gAllowNewAreaMasterFromCustMaster)
            {
                txtBuildingName.requestFocus();
            }
            else
            {
                setAlwaysOnTop(false);
                buildingType = "residential";
                funSelectBuilding();
            }
        }
    }//GEN-LAST:event_cmbCustTypeKeyPressed

    private void txtMobileNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMobileNoFocusLost
        // TODO add your handling code here:
        if (txtMobileNo.getText().isEmpty())
        {

        }
        else
        {
            if (!clsGlobalVarClass.gClientCode.equals("024.001"))
            {
                if (flgOperation)
                {
                    if (btnNew.getText().equalsIgnoreCase("Save"))
                    {
                        if (!funCheckDuplicateMobileNo("primary", "Save"))
                        {
                            txtMobileNo.requestFocus();
                            return;
                        }
                    }
                }
                else
                {
                    if (btnNew.getText().equalsIgnoreCase("Save"))
                    {
                        if (!funCheckDuplicateMobileNo("primary", "Update"))
                        {
                            txtMobileNo.requestFocus();
                            return;
                        }
                    }
                }
            }
        }
    }//GEN-LAST:event_txtMobileNoFocusLost

    private void txtMobileNoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtMobileNoMouseClicked
        // TODO add your handling code here:
        if (txtMobileNo.getText().length() == 0)
        {
            new frmNumericKeyboard(this, true, "", "Long", "Enter Mobile No ").setVisible(true);
            txtMobileNo.setText(clsGlobalVarClass.gNumerickeyboardValue);
            txtMobileNo.requestFocus();
            return;
        }
        else
        {
            new frmNumericKeyboard(this, true, txtMobileNo.getText(), "Long", "Enter Mobile No").setVisible(true);
            txtMobileNo.setText(clsGlobalVarClass.gNumerickeyboardValue);
        }
    }//GEN-LAST:event_txtMobileNoMouseClicked

    private void txtMobileNoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMobileNoKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            txtCustomerName.requestFocus();
        }
        else if (evt.getKeyChar() == '?' || evt.getKeyChar() == '/')
        {
            funOpenCustomerSearch();
        }
    }//GEN-LAST:event_txtMobileNoKeyPressed

    private void btnSwipeCardMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSwipeCardMouseClicked
        // TODO add your handling code here:
        new frmSwipCardPopUp(this, "frmRegisterDebitCard").setVisible(true);
        if (objUtility.funValidateDebitCardString(clsGlobalVarClass.gDebitCardNo))
        {
            txtCardNumber.setText(clsGlobalVarClass.gDebitCardNo);
        }
        else
        {
            JOptionPane.showMessageDialog(this, "Invalid Card No");
        }
    }//GEN-LAST:event_btnSwipeCardMouseClicked

    private void txtBillNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBillNoFocusLost
        // TODO add your handling code here:
        if (clsGlobalVarClass.gClientCode.equals("009.001"))
        {
            if (cmbCustType.getSelectedItem().toString().equals("Walk In"))
            {
                if (txtBillNo.getText().isEmpty())
                {
                    new frmOkPopUp(this, "Error", "Enter Bill No", 0).setVisible(true);
                    return;
                }
                else if (!funGetBillAmount(txtBillNo.getText().trim()))
                {
                    if (errorType.equals("Invalid Settlement"))
                    {
                        new frmOkPopUp(this, "Error", "Invalid Settlement Mode", 0).setVisible(true);
                    }
                    else
                    {
                        new frmOkPopUp(this, "Error", "Bill Amount is not sufficient to issue Sanskar Card", 0).setVisible(true);
                    }
                    return;
                }
            }
        }
    }//GEN-LAST:event_txtBillNoFocusLost

    private void txtBillNoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBillNoKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            btnSwipeCard.requestFocus();
        }
    }//GEN-LAST:event_txtBillNoKeyPressed

    private void cmbCardTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbCardTypeActionPerformed
        funSetCardType();
    }//GEN-LAST:event_cmbCardTypeActionPerformed

    private void cmbCityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbCityActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbCityActionPerformed

    private void txtStreetNameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtStreetNameMouseClicked
        // TODO add your handling code here:
        if (txtStreetName.getText().length() == 0)
        {
            new frmAlfaNumericKeyBoard(this, true, "1", "Enter Street Name").setVisible(true);
            txtStreetName.setText(clsGlobalVarClass.gKeyboardValue);
        }
        else
        {
            new frmAlfaNumericKeyBoard(this, true, txtStreetName.getText(), "1", "Enter Street Name").setVisible(true);
            txtStreetName.setText(clsGlobalVarClass.gKeyboardValue);
        }
    }//GEN-LAST:event_txtStreetNameMouseClicked

    private void txtPinCodeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPinCodeMouseClicked
        // TODO add your handling code here:
        if (txtPinCode.getText().length() == 0)
        {
            new frmNumericKeyboard(this, true, "", "Long", "Enter Pin Code ").setVisible(true);
            txtPinCode.setText(clsGlobalVarClass.gNumerickeyboardValue);
        }
        else
        {
            new frmNumericKeyboard(this, true, txtPinCode.getText(), "Long", "Enter Pin Code").setVisible(true);
            txtPinCode.setText(clsGlobalVarClass.gNumerickeyboardValue);
        }
    }//GEN-LAST:event_txtPinCodeMouseClicked

    private void txtBuildingCodeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtBuildingCodeMouseClicked
        // TODO add your handling code here:
        setAlwaysOnTop(false);
        buildingType = "residential";
        funSelectBuilding();
    }//GEN-LAST:event_txtBuildingCodeMouseClicked

    private void txtBuildingNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBuildingNameFocusLost
        // TODO add your handling code here:
        if (txtBuildingName.getText().trim().length() > 0)
        {
            funCheckBuilding();
        }
    }//GEN-LAST:event_txtBuildingNameFocusLost

    private void txtBuildingNameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtBuildingNameMouseClicked
        // TODO add your handling code here:
        if (clsGlobalVarClass.gAllowNewAreaMasterFromCustMaster)
        {
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
        }
    }//GEN-LAST:event_txtBuildingNameMouseClicked

    private void txtOfficeBuildingNameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtOfficeBuildingNameMouseClicked
        // TODO add your handling code here:
        if (txtOfficeBuildingName.getText().length() == 0)
        {
            new frmAlfaNumericKeyBoard(this, true, "1", "Enter Building Name").setVisible(true);
            txtOfficeBuildingName.setText(clsGlobalVarClass.gKeyboardValue);
        }
        else
        {
            new frmAlfaNumericKeyBoard(this, true, txtOfficeBuildingName.getText(), "1", "Enter Building Name").setVisible(true);
            txtOfficeBuildingName.setText(clsGlobalVarClass.gKeyboardValue);
        }
    }//GEN-LAST:event_txtOfficeBuildingNameMouseClicked

    private void txtOfficeStreetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtOfficeStreetMouseClicked
        // TODO add your handling code here:
        if (txtOfficeStreet.getText().length() == 0)
        {
            new frmAlfaNumericKeyBoard(this, true, "1", "Enter Street Name").setVisible(true);
            txtOfficeStreet.setText(clsGlobalVarClass.gKeyboardValue);
        }
        else
        {
            new frmAlfaNumericKeyBoard(this, true, txtOfficeStreet.getText(), "1", "Enter Street Name").setVisible(true);
            txtOfficeStreet.setText(clsGlobalVarClass.gKeyboardValue);
        }
    }//GEN-LAST:event_txtOfficeStreetMouseClicked

    private void txtOfficeLandmarkMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtOfficeLandmarkMouseClicked
        // TODO add your handling code here:
        if (txtOfficeLandmark.getText().length() == 0)
        {
            new frmAlfaNumericKeyBoard(this, true, "1", "Enter Landmark").setVisible(true);
            txtOfficeLandmark.setText(clsGlobalVarClass.gKeyboardValue);
        }
        else
        {
            new frmAlfaNumericKeyBoard(this, true, txtOfficeLandmark.getText(), "1", "Enter Landmark").setVisible(true);
            txtOfficeLandmark.setText(clsGlobalVarClass.gKeyboardValue);
        }
    }//GEN-LAST:event_txtOfficeLandmarkMouseClicked

    private void txtOfficePincodeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtOfficePincodeMouseClicked
        // TODO add your handling code here:
        if (txtOfficePincode.getText().length() == 0)
        {
            new frmNumericKeyboard(this, true, "", "Long", "Enter Pin Code ").setVisible(true);
            txtOfficePincode.setText(clsGlobalVarClass.gNumerickeyboardValue);
        }
        else
        {
            new frmNumericKeyboard(this, true, txtOfficePincode.getText(), "Long", "Enter Pin Code").setVisible(true);
            txtOfficePincode.setText(clsGlobalVarClass.gNumerickeyboardValue);
        }
    }//GEN-LAST:event_txtOfficePincodeMouseClicked

    private void txtOfficeMobileNoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtOfficeMobileNoMouseClicked
        // TODO add your handling code here:
        if (txtOfficeMobileNo.getText().length() == 0)
        {
            new frmNumericKeyboard(this, true, "", "Long", "Enter Mobile No ").setVisible(true);
            txtOfficeMobileNo.setText(clsGlobalVarClass.gNumerickeyboardValue);
        }
        else
        {
            new frmNumericKeyboard(this, true, txtOfficeMobileNo.getText(), "Long", "Enter Mobile No").setVisible(true);
            txtOfficeMobileNo.setText(clsGlobalVarClass.gNumerickeyboardValue);
        }
    }//GEN-LAST:event_txtOfficeMobileNoMouseClicked

    private void txtOfficeBuildingCodeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtOfficeBuildingCodeMouseClicked
        // TODO add your handling code here:
        setAlwaysOnTop(false);
        buildingType = "office";
        funSelectBuilding();
    }//GEN-LAST:event_txtOfficeBuildingCodeMouseClicked

    private void btnCancelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_btnCancelMouseClicked

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed

        if (txtMobileNo.getText().toString().contains(","))
        {
            String[] arrMobileList = txtMobileNo.getText().toString().split(",");
            for (int cnt = 0; cnt < arrMobileList.length; cnt++)
            {
                if (!arrMobileList[cnt].matches("\\d{8}") && !arrMobileList[cnt].matches("\\d{9}") && !arrMobileList[cnt].matches("\\d{10}"))
                {
                    new frmOkPopUp(this, "Please Enter Valid Mobile Number.", "Error", 0).setVisible(true);
                    return;
                }
                else
                {
                    if (cnt > 0)
                    {
                        try
                        {
                            sql = "select count(strCustomerCode),strCustomerName from tblcustomermaster where longMobileNo like '%" + arrMobileList[cnt] + "%'";
                            ResultSet rsCustomer = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                            rsCustomer.next();
                            int found = rsCustomer.getInt(1);
                            String customerName = rsCustomer.getString(2);
                            rsCustomer.close();

                            if (found > 0)
                            {
                                new frmOkPopUp(this, "Mobile No already exists for another customer ", "Error", 0).setVisible(true);
                                return;
                            }
                            else
                            {
                                arrMobileNoList.add(arrMobileList[cnt]);
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        arrMobileNoList.add(arrMobileList[cnt]);
                    }

                }
            }
        }
        else
        {
            if (!txtMobileNo.getText().matches("\\d{8}") && !txtMobileNo.getText().matches("\\d{9}") && !txtMobileNo.getText().matches("\\d{10}"))
            {
                new frmOkPopUp(this, "Please Enter Valid Mobile Number.", "Error", 0).setVisible(true);
                return;
            }
        }

        if (btnNew.getText().equalsIgnoreCase("SAVE"))
        {
            funSaveCustomerMaster();
            flgOperation = true;
        }
        else
        {
            funUpdateCustomerMaster();
            flgOperation = false;
        }
                
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnResetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnResetMouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_btnResetMouseClicked

    private void btnExportMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnExportMouseClicked


    }//GEN-LAST:event_btnExportMouseClicked

    private void txtMobileNoActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtMobileNoActionPerformed
    {//GEN-HEADEREND:event_txtMobileNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMobileNoActionPerformed

    private void txtCustomerCodeKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtCustomerCodeKeyPressed
    {//GEN-HEADEREND:event_txtCustomerCodeKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() == '?' || evt.getKeyChar() == '/')
        {
            funOpenCustomerSearch();
        }
        if (evt.getKeyCode() == 10)
        {
            txtMobileNo.requestFocus();
        }
    }//GEN-LAST:event_txtCustomerCodeKeyPressed

    private void txtBuildingNameKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtBuildingNameKeyPressed
    {//GEN-HEADEREND:event_txtBuildingNameKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            txtStreetName.requestFocus();
        }
    }//GEN-LAST:event_txtBuildingNameKeyPressed

    private void cmbStateKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_cmbStateKeyPressed
    {//GEN-HEADEREND:event_cmbStateKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            txtExtCode.requestFocus();
        }
    }//GEN-LAST:event_cmbStateKeyPressed

    private void cmbCityKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_cmbCityKeyPressed
    {//GEN-HEADEREND:event_cmbCityKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            cmbState.requestFocus();
        }
    }//GEN-LAST:event_cmbCityKeyPressed

    private void txtStreetNameKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtStreetNameKeyPressed
    {//GEN-HEADEREND:event_txtStreetNameKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            txtPinCode.requestFocus();
        }
    }//GEN-LAST:event_txtStreetNameKeyPressed

    private void txtPinCodeKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtPinCodeKeyPressed
    {//GEN-HEADEREND:event_txtPinCodeKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            cmbCity.requestFocus();
        }
    }//GEN-LAST:event_txtPinCodeKeyPressed

    private void txtExtCodeKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtExtCodeKeyPressed
    {//GEN-HEADEREND:event_txtExtCodeKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            txtEmail.requestFocus();
        }
    }//GEN-LAST:event_txtExtCodeKeyPressed

    private void txtEmailKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtEmailKeyPressed
    {//GEN-HEADEREND:event_txtEmailKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            dteDOB.requestFocus();
        }
    }//GEN-LAST:event_txtEmailKeyPressed

    private void dteDOBKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_dteDOBKeyPressed
    {//GEN-HEADEREND:event_dteDOBKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            cmbGender.requestFocus();
        }
    }//GEN-LAST:event_dteDOBKeyPressed

    private void dteAnniversoryKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_dteAnniversoryKeyPressed
    {//GEN-HEADEREND:event_dteAnniversoryKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_dteAnniversoryKeyPressed

    private void cmbGenderKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_cmbGenderKeyPressed
    {//GEN-HEADEREND:event_cmbGenderKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            dteAnniversory.requestFocus();
        }
    }//GEN-LAST:event_cmbGenderKeyPressed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnResetActionPerformed
    {//GEN-HEADEREND:event_btnResetActionPerformed
        // TODO add your handling code here:
        funResetField();
    }//GEN-LAST:event_btnResetActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnCancelActionPerformed
    {//GEN-HEADEREND:event_btnCancelActionPerformed
        // TODO add your handling code here:
        funFreeMemory();
        dispose();
        clsGlobalVarClass.hmActiveForms.remove("Customer Master");
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnExportActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnExportActionPerformed
    {//GEN-HEADEREND:event_btnExportActionPerformed
        // TODO add your handling code here:

        try
        {
            if (clsGlobalVarClass.gUserType.equalsIgnoreCase("super"))
            {
                funExportData();
            }
            else
            {
                sqlBuilder.setLength(0);
                sqlBuilder.append("select a.strUserCode,a.strFormName,a.strGrant,a.strTLA "
                        + " from tbluserdtl a "
                        + " where a.strFormName='Customer Master' "
                        + " and a.strTLA='true' "
                        + " and a.strUserCode='" + clsGlobalVarClass.gUserCode + "' ");
                ResultSet rsTLA = clsGlobalVarClass.dbMysql.executeResultSet(sqlBuilder.toString());
                if (rsTLA.next())
                {
                    funCheckUserAuthentication();
                }
                else
                {
                    funExportData();
                }
                rsTLA.close();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


    }//GEN-LAST:event_btnExportActionPerformed

    private void txtBuildingCodeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtBuildingCodeActionPerformed
    {//GEN-HEADEREND:event_txtBuildingCodeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBuildingCodeActionPerformed

    private void txtBuildingCodeKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtBuildingCodeKeyPressed
    {//GEN-HEADEREND:event_txtBuildingCodeKeyPressed
        if (evt.getKeyCode() == 10)
        {
            txtStreetName.requestFocus();
        }
    }//GEN-LAST:event_txtBuildingCodeKeyPressed

    private void lblProductNameMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblProductNameMouseClicked
    {//GEN-HEADEREND:event_lblProductNameMouseClicked
        // TODO add your handling code here:
        objUtility = new clsUtility();
    }//GEN-LAST:event_lblProductNameMouseClicked

    private void lblformNameMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblformNameMouseClicked
    {//GEN-HEADEREND:event_lblformNameMouseClicked
        // TODO add your handling code here:
        objUtility = new clsUtility();
    }//GEN-LAST:event_lblformNameMouseClicked

    private void lblPosNameMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblPosNameMouseClicked
    {//GEN-HEADEREND:event_lblPosNameMouseClicked
        // TODO add your handling code here:
        objUtility = new clsUtility();
    }//GEN-LAST:event_lblPosNameMouseClicked

    private void lblUserCodeMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblUserCodeMouseClicked
    {//GEN-HEADEREND:event_lblUserCodeMouseClicked
        // TODO add your handling code here:
        objUtility = new clsUtility();
    }//GEN-LAST:event_lblUserCodeMouseClicked

    private void lblDateMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblDateMouseClicked
    {//GEN-HEADEREND:event_lblDateMouseClicked
        // TODO add your handling code here:
        objUtility = new clsUtility();
    }//GEN-LAST:event_lblDateMouseClicked

    private void lblHOSignMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblHOSignMouseClicked
    {//GEN-HEADEREND:event_lblHOSignMouseClicked
        // TODO add your handling code here:
        objUtility = new clsUtility();
    }//GEN-LAST:event_lblHOSignMouseClicked

    private void formWindowClosed(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosed
    {//GEN-HEADEREND:event_formWindowClosed
        clsGlobalVarClass.hmActiveForms.remove("Customer Master");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosing
    {//GEN-HEADEREND:event_formWindowClosing
        clsGlobalVarClass.hmActiveForms.remove("Customer Master");
    }//GEN-LAST:event_formWindowClosing

    private void txtCustAddressMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtCustAddressMouseClicked
    {//GEN-HEADEREND:event_txtCustAddressMouseClicked
        if (txtCustAddress.getText().length() == 0)
        {
            new frmAlfaNumericKeyBoard(this, true, "1", "Enter Customer Address").setVisible(true);
            txtCustAddress.setText(clsGlobalVarClass.gKeyboardValue);
        }
        else
        {
            new frmAlfaNumericKeyBoard(this, true, txtCustAddress.getText(), "1", "Enter Customer Address").setVisible(true);
            txtCustAddress.setText(clsGlobalVarClass.gKeyboardValue);
        }
    }//GEN-LAST:event_txtCustAddressMouseClicked

    private void txtCustAddressKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtCustAddressKeyPressed
    {//GEN-HEADEREND:event_txtCustAddressKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCustAddressKeyPressed

    private void txtLandmarkMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtLandmarkMouseClicked
    {//GEN-HEADEREND:event_txtLandmarkMouseClicked
        if (txtLandmark.getText().length() == 0)
        {
            new frmAlfaNumericKeyBoard(this, true, "1", "Enter Landmark.").setVisible(true);
            txtLandmark.setText(clsGlobalVarClass.gKeyboardValue);
        }
        else
        {
            new frmAlfaNumericKeyBoard(this, true, txtLandmark.getText(), "1", "Enter Landmark.").setVisible(true);
            txtLandmark.setText(clsGlobalVarClass.gKeyboardValue);
        }
    }//GEN-LAST:event_txtLandmarkMouseClicked

    private void txtLandmarkKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtLandmarkKeyPressed
    {//GEN-HEADEREND:event_txtLandmarkKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtLandmarkKeyPressed

    private void txtUsernameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtUsernameMouseClicked
        if (clsGlobalVarClass.gTouchScreenMode)
        {
            if (txtUsername.getText().length() == 0)
            {
                new frmAlfaNumericKeyBoard(this, true, "1", "Enter User Name.").setVisible(true);
                txtUsername.setText(clsGlobalVarClass.gKeyboardValue);
            }
            else
            {
                new frmAlfaNumericKeyBoard(this, true, txtUsername.getText(), "1", "Enter User Name.").setVisible(true);
                txtUsername.setText(clsGlobalVarClass.gKeyboardValue);
            }
        }
    }//GEN-LAST:event_txtUsernameMouseClicked

    private void txtUsernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUsernameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUsernameActionPerformed

    private void txtUsernameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUsernameKeyPressed
        try
        {
            if (evt.getKeyCode() == 10 && txtUsername.getText().equalsIgnoreCase("SANGUINE"))
            {
                txtPassword.requestFocus();
            }
            else
            {
                if (evt.getKeyCode() == 10)
                {
                    String sql = "select count(*) from tbluserhd where strUsercode='" + txtUsername.getText() + "' ";
                    ResultSet rssql = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                    if (rssql.next())
                    {
                        if (rssql.getInt(1) > 0)
                        {
                            txtPassword.requestFocus();
                        }
                        else
                        {
                            sql = "  select count(*) from tbluserhd where strDebitCardString='" + txtUsername.getText() + "' ";
                            ResultSet rssql1 = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                            if (rssql1.next())
                            {
                                if (rssql1.getInt(1) > 0)
                                {
                                    if (funCHeckLoginForDebitCardString(txtUsername.getText()))
                                    {

                                    }

                                    else
                                    {
                                        txtUsername.setText("");
                                        new frmOkPopUp(null, "Invalid User", "Error", 1).setVisible(true);
                                        return;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_txtUsernameKeyPressed

    private void txtPasswordMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPasswordMouseClicked
        if (txtPassword.getPassword().length == 0)
        {
            new frmAlfaNumericKeyBoard(this, true, "1", "Enter  Password.").setVisible(true);
            txtPassword.setText(clsGlobalVarClass.gKeyboardValue);
        }
        else
        {
            new frmAlfaNumericKeyBoard(this, true, txtPassword.getPassword().toString(), "1", "Enter Password.").setVisible(true);
            txtPassword.setText(clsGlobalVarClass.gKeyboardValue);
        }
    }//GEN-LAST:event_txtPasswordMouseClicked

    private void txtPasswordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPasswordKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            funUserAuthenticationOKButtonPressed();

        }
    }//GEN-LAST:event_txtPasswordKeyPressed

    private void txtPasswordKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPasswordKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPasswordKeyReleased

    private void txtExtCodeMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtExtCodeMouseClicked
    {//GEN-HEADEREND:event_txtExtCodeMouseClicked
        if (txtExtCode.getText().length() == 0)
        {
            new frmAlfaNumericKeyBoard(this, true, "1", "Enter External Code.").setVisible(true);
            txtExtCode.setText(clsGlobalVarClass.gKeyboardValue);
        }
        else
        {
            new frmAlfaNumericKeyBoard(this, true, txtExtCode.getText(), "1", "Enter External Code.").setVisible(true);
            txtExtCode.setText(clsGlobalVarClass.gKeyboardValue);
        }
    }//GEN-LAST:event_txtExtCodeMouseClicked

    private void txtEmailMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtEmailMouseClicked
    {//GEN-HEADEREND:event_txtEmailMouseClicked
        if (txtEmail.getText().length() == 0)
        {
            new frmAlfaNumericKeyBoard(this, true, "1", "Enter Mail Id.").setVisible(true);
            txtEmail.setText(clsGlobalVarClass.gKeyboardValue);
        }
        else
        {
            new frmAlfaNumericKeyBoard(this, true, txtEmail.getText(), "1", "Enter Mail Id.").setVisible(true);
            txtEmail.setText(clsGlobalVarClass.gKeyboardValue);
        }
    }//GEN-LAST:event_txtEmailMouseClicked

    private void txtTempCustAddressMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtTempCustAddressMouseClicked
    {//GEN-HEADEREND:event_txtTempCustAddressMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTempCustAddressMouseClicked

    private void txtTempCustAddressKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtTempCustAddressKeyPressed
    {//GEN-HEADEREND:event_txtTempCustAddressKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTempCustAddressKeyPressed

    private void txtTempStreetNameMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtTempStreetNameMouseClicked
    {//GEN-HEADEREND:event_txtTempStreetNameMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTempStreetNameMouseClicked

    private void txtTempStreetNameKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtTempStreetNameKeyPressed
    {//GEN-HEADEREND:event_txtTempStreetNameKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTempStreetNameKeyPressed

    private void txtTempLandmarkMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtTempLandmarkMouseClicked
    {//GEN-HEADEREND:event_txtTempLandmarkMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTempLandmarkMouseClicked

    private void txtTempLandmarkKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtTempLandmarkKeyPressed
    {//GEN-HEADEREND:event_txtTempLandmarkKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTempLandmarkKeyPressed

    private void txtGSTNoMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtGSTNoMouseClicked
    {//GEN-HEADEREND:event_txtGSTNoMouseClicked
        if (txtGSTNo.getText().length() == 0)
        {
            new frmAlfaNumericKeyBoard(this, true, "1", "Enter GST No.").setVisible(true);
            txtGSTNo.setText(clsGlobalVarClass.gKeyboardValue);
        }
        else
        {
            new frmAlfaNumericKeyBoard(this, true, txtGSTNo.getText(), "1", "Enter GST No.").setVisible(true);
            txtGSTNo.setText(clsGlobalVarClass.gKeyboardValue);
        }
    }//GEN-LAST:event_txtGSTNoMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[])
    {
        /*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
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
            java.util.logging.Logger.getLogger(frmCustomerMaster.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(frmCustomerMaster.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(frmCustomerMaster.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(frmCustomerMaster.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                new frmCustomerMaster().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnExport;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnSwipeCard;
    private javax.swing.JComboBox cmbCardType;
    private javax.swing.JComboBox cmbCity;
    private javax.swing.JComboBox cmbCustType;
    private javax.swing.JComboBox cmbGender;
    public javax.swing.JComboBox cmbOfficeCity;
    public javax.swing.JComboBox cmbOfficeState;
    private javax.swing.JComboBox cmbState;
    private com.toedter.calendar.JDateChooser dteAnniversory;
    private com.toedter.calendar.JDateChooser dteDOB;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JLabel lblAnniversory;
    private javax.swing.JLabel lblBillNo;
    private javax.swing.JLabel lblBuildingCode;
    private javax.swing.JLabel lblBuildingName1;
    private javax.swing.JLabel lblCardName;
    private javax.swing.JLabel lblCardType;
    private javax.swing.JLabel lblCity;
    private javax.swing.JLabel lblCity1;
    private javax.swing.JLabel lblCustAddress;
    private javax.swing.JLabel lblCustType;
    private javax.swing.JLabel lblCustomerCode;
    private javax.swing.JLabel lblCustomerName;
    private javax.swing.JLabel lblDOB;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblExtCode;
    private javax.swing.JLabel lblFormName;
    private javax.swing.JLabel lblGSTNo;
    private javax.swing.JLabel lblGender;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblLandmark;
    private javax.swing.JLabel lblLandmark1;
    private javax.swing.JLabel lblMobileNo;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblOfficeNo;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JLabel lblPinCode;
    private javax.swing.JLabel lblPinCode1;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblRoadStreetName1;
    private javax.swing.JLabel lblState;
    private javax.swing.JLabel lblState1;
    private javax.swing.JLabel lblStreetName;
    private javax.swing.JLabel lblTempCustAddress;
    private javax.swing.JLabel lblTempLandmark;
    private javax.swing.JLabel lblTempStreetName;
    private javax.swing.JLabel lblTotalPoints;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblUsername;
    private javax.swing.JLabel lblcardName;
    private javax.swing.JLabel lblformName;
    private javax.swing.JPanel panelBuilding;
    private javax.swing.JPanel panelContainer;
    private javax.swing.JPanel panelDebitCard;
    private javax.swing.JTabbedPane panelDeliveryCharges;
    private javax.swing.JPanel panelFormBody;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelMain;
    private javax.swing.JPanel panelOfficeAddress;
    private javax.swing.JPanel panelOtherDetail;
    private javax.swing.JPanel panelTemporaryAddress;
    private javax.swing.JPanel panelUserAuthentication;
    private javax.swing.JTextField txtBillAmount;
    private javax.swing.JTextField txtBillNo;
    public javax.swing.JTextField txtBuildingCode;
    private javax.swing.JTextField txtBuildingName;
    private javax.swing.JTextField txtCardNumber;
    private javax.swing.JTextField txtCustAddress;
    private javax.swing.JTextField txtCustomerCode;
    private javax.swing.JTextField txtCustomerName;
    public javax.swing.JTextField txtEmail;
    public javax.swing.JTextField txtExtCode;
    public javax.swing.JTextField txtGSTNo;
    private javax.swing.JTextField txtLandmark;
    public javax.swing.JTextField txtMobileNo;
    private javax.swing.JTextField txtOfficeBuildingCode;
    public javax.swing.JTextField txtOfficeBuildingName;
    public javax.swing.JTextField txtOfficeLandmark;
    public javax.swing.JTextField txtOfficeMobileNo;
    public javax.swing.JTextField txtOfficePincode;
    public javax.swing.JTextField txtOfficeStreet;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtPinCode;
    private javax.swing.JTextField txtStreetName;
    private javax.swing.JTextField txtTempCustAddress;
    private javax.swing.JTextField txtTempLandmark;
    private javax.swing.JTextField txtTempStreetName;
    private javax.swing.JTextField txtTotalPoints;
    private javax.swing.JTextField txtUsername;
    // End of variables declaration//GEN-END:variables

    private void funExportData()
    {
        txtUsername.setText("");
        txtPassword.setText("");
        try
        {

            HSSFWorkbook hwb = new HSSFWorkbook();
            HSSFSheet sheet = hwb.createSheet("sheet1");

            HSSFRow rowhead = sheet.createRow((short) 0);

            rowhead.createCell((short) 0).setCellValue("Customer Code");
            rowhead.createCell((short) 1).setCellValue("Customer Name");
            rowhead.createCell((short) 2).setCellValue("Bulding Code");
            rowhead.createCell((short) 3).setCellValue("Building Name");
            rowhead.createCell((short) 4).setCellValue("Street Name");
            rowhead.createCell((short) 5).setCellValue("Landmark");
            rowhead.createCell((short) 6).setCellValue("Area");
            rowhead.createCell((short) 7).setCellValue("City");
            rowhead.createCell((short) 8).setCellValue("State");
            rowhead.createCell((short) 9).setCellValue("Pin Code");

            rowhead.createCell((short) 10).setCellValue("Mobile No");
            rowhead.createCell((short) 11).setCellValue("Alternate Mobile No");
            rowhead.createCell((short) 12).setCellValue("Office Building Code");
            rowhead.createCell((short) 13).setCellValue("Office Building Name");
            rowhead.createCell((short) 14).setCellValue("Office Street Name");
            rowhead.createCell((short) 15).setCellValue("Office Landmark");
            rowhead.createCell((short) 16).setCellValue("Office Area");
            rowhead.createCell((short) 17).setCellValue("Office City");
            rowhead.createCell((short) 18).setCellValue("Office Pin Code");
            rowhead.createCell((short) 19).setCellValue("Office State");

            rowhead.createCell((short) 20).setCellValue("Office No");
            rowhead.createCell((short) 21).setCellValue("User Created");
            rowhead.createCell((short) 22).setCellValue("User Edited");
            rowhead.createCell((short) 23).setCellValue("Date Created");
            rowhead.createCell((short) 24).setCellValue("Date Edited");
            rowhead.createCell((short) 25).setCellValue("Data Post Flag");
            rowhead.createCell((short) 26).setCellValue("Client Code");
            rowhead.createCell((short) 27).setCellValue("Office Address");
            rowhead.createCell((short) 28).setCellValue("External Code");
            rowhead.createCell((short) 29).setCellValue("Customer Type");

            rowhead.createCell((short) 30).setCellValue("DOB");
            rowhead.createCell((short) 31).setCellValue("Gender");
            rowhead.createCell((short) 32).setCellValue("Anniversary");
            rowhead.createCell((short) 33).setCellValue("Email Id");
            rowhead.createCell((short) 34).setCellValue("CRM Id");
            rowhead.createCell((short) 35).setCellValue("Customer Address");

            String sql_CustomerDtl = "select a.strCustomerCode,a.strCustomerName,a.strBuldingCode,a.strBuildingName,a.strStreetName "
                    + ",a.strLandmark,a.strArea,a.strCity,a.strState,a.intPinCode,a.longMobileNo,a.longAlternateMobileNo,a.strOfficeBuildingCode "
                    + ",a.strOfficeBuildingName,a.strOfficeStreetName,a.strOfficeLandmark,a.strOfficeArea,a.strOfficeCity "
                    + ",a.strOfficePinCode,a.strOfficeState,a.strOfficeNo,a.strUserCreated,a.strUserEdited,a.dteDateCreated "
                    + ",a.dteDateEdited,a.strDataPostFlag,a.strClientCode,a.strOfficeAddress "
                    + ",a.strExternalCode,a.strCustomerType,a.dteDOB,a.strGender,a.dteAnniversary,a.strEmailId,a.strCRMId,a.strCustAddress "
                    + "from tblcustomermaster a";
            ResultSet rs_CustomerDtl = clsGlobalVarClass.dbMysql.executeResultSet(sql_CustomerDtl);
            int i = 1;

            while (rs_CustomerDtl.next())
            {
                HSSFRow row = sheet.createRow((short) i);

                row.createCell((short) 0).setCellValue(rs_CustomerDtl.getString(1));
                row.createCell((short) 1).setCellValue(rs_CustomerDtl.getString(2));
                row.createCell((short) 2).setCellValue(rs_CustomerDtl.getString(3));
                row.createCell((short) 3).setCellValue(rs_CustomerDtl.getString(4));
                row.createCell((short) 4).setCellValue(rs_CustomerDtl.getString(5));
                row.createCell((short) 5).setCellValue(rs_CustomerDtl.getString(6));
                row.createCell((short) 6).setCellValue(rs_CustomerDtl.getString(7));
                row.createCell((short) 7).setCellValue(rs_CustomerDtl.getString(8));
                row.createCell((short) 8).setCellValue(rs_CustomerDtl.getString(9));
                row.createCell((short) 9).setCellValue(rs_CustomerDtl.getString(10));

                row.createCell((short) 10).setCellValue(rs_CustomerDtl.getString(11));
                row.createCell((short) 11).setCellValue(rs_CustomerDtl.getString(12));
                row.createCell((short) 12).setCellValue(rs_CustomerDtl.getString(13));
                row.createCell((short) 13).setCellValue(rs_CustomerDtl.getString(14));
                row.createCell((short) 14).setCellValue(rs_CustomerDtl.getString(15));
                row.createCell((short) 15).setCellValue(rs_CustomerDtl.getString(16));
                row.createCell((short) 16).setCellValue(rs_CustomerDtl.getString(17));
                row.createCell((short) 17).setCellValue(rs_CustomerDtl.getString(18));
                row.createCell((short) 18).setCellValue(rs_CustomerDtl.getString(19));
                row.createCell((short) 19).setCellValue(rs_CustomerDtl.getString(20));

                row.createCell((short) 20).setCellValue(rs_CustomerDtl.getString(21));
                row.createCell((short) 21).setCellValue(rs_CustomerDtl.getString(22));
                row.createCell((short) 22).setCellValue(rs_CustomerDtl.getString(23));
                row.createCell((short) 23).setCellValue(rs_CustomerDtl.getString(24));
                row.createCell((short) 24).setCellValue(rs_CustomerDtl.getString(25));
                row.createCell((short) 25).setCellValue(rs_CustomerDtl.getString(26));
                row.createCell((short) 26).setCellValue(rs_CustomerDtl.getString(27));
                row.createCell((short) 27).setCellValue(rs_CustomerDtl.getString(28));
                row.createCell((short) 28).setCellValue(rs_CustomerDtl.getString(29));
                row.createCell((short) 29).setCellValue(rs_CustomerDtl.getString(30));

                row.createCell((short) 30).setCellValue(rs_CustomerDtl.getString(31));
                row.createCell((short) 31).setCellValue(rs_CustomerDtl.getString(32));
                row.createCell((short) 32).setCellValue(rs_CustomerDtl.getString(33));
                row.createCell((short) 33).setCellValue(rs_CustomerDtl.getString(34));
                row.createCell((short) 34).setCellValue(rs_CustomerDtl.getString(35));
                row.createCell((short) 35).setCellValue(rs_CustomerDtl.getString(36));

                i++;

            }

            String filePath = System.getProperty("user.dir");
            File file = new File(filePath + "/CustomerMaster.xls");
            FileOutputStream fileOut = new FileOutputStream(file);
            hwb.write(fileOut);
            fileOut.close();
            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + filePath + "/CustomerMaster.xls");
            //System.out.println("Your excel file has been generated!");

            rs_CustomerDtl.close();
        }
        catch (FileNotFoundException ex)
        {
            JOptionPane.showMessageDialog(this, "File is already opened please close ");

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void funSetSelectedCityFromSetup()
    {
        try
        {
            String sql = "select a.strCityName,a.strState,a.strCountry  from tblsetup a ";
            ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            while (rs.next())
            {
                String setupCity = rs.getString(1);
                String setupState = rs.getString(2);
                String setupCountry = rs.getString(3);

                cmbCity.setSelectedItem(setupCity);
                cmbState.setSelectedItem(setupState);

                cmbOfficeCity.setSelectedItem(setupCity);
                cmbOfficeState.setSelectedItem(setupState);
            }
            rs.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void funCustomerTypeComboSelect()
    {
        String customerType = cmbCustType.getSelectedItem().toString().trim();

        if (clsGlobalVarClass.gClientCode.equals("009.001"))
        {
            if (customerType.equalsIgnoreCase("Walk In"))
            {
                txtExtCode.setText("SBW");
            }
            else
            {
                txtExtCode.setText("");
            }
            txtExtCode.requestFocus();
        }
        else if (clsGlobalVarClass.gClientCode.equals("190.001"))//"190.001", "SQUARE ONE HOSPITALITY LLP" (QUARTER HOUSE)
        {
            if (customerType.equalsIgnoreCase("LIQOUR") || customerType.equalsIgnoreCase("LIQUOR"))
            {
                lblDOB.setText("EXP. Date        :");
            }
            else
            {
                lblDOB.setText("D.O.B.             :");
            }
        }
    }

}
