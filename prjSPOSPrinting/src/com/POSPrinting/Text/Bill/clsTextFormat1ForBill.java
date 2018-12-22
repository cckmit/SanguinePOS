package com.POSPrinting.Text.Bill;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import com.POSPrinting.Utility.clsPrintingUtility;
import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsPosConfigFile;
import com.POSGlobal.controller.clsUtility;
import com.POSGlobal.controller.clsUtility2;
import com.POSPrinting.Interfaces.clsBillGenerationFormat;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

/**
 *
 * @author Ajim
 * @date Aug 26, 2017
 */
public class clsTextFormat1ForBill implements clsBillGenerationFormat
{

    private DecimalFormat decimalFormat = new DecimalFormat("#.###");
    private SimpleDateFormat ddMMyyyyAMPMDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a ");
    private clsUtility objUtility = new clsUtility();
    private clsUtility2 objUtility2 = new clsUtility2();
    private clsPrintingUtility objPrintingUtility = new clsPrintingUtility();
    private DecimalFormat stdDecimalFormat = new DecimalFormat("######.##");
    private DecimalFormat decimalFormatFor2DecPoint = new DecimalFormat("0.00");
    private DecimalFormat decimalFormatFor3DecPoint = new DecimalFormat("0.000");
    private final String dashedLineFor40Chars = "  --------------------------------------";

    //format 1

    /**
     *
     * @param billNo
     * @param reprint
     * @param formName
     * @param transType
     * @param billDate
     * @param posCode
     * @param viewORprint
     */
    @Override
    public void funGenerateBill(String billNo, String reprint, String formName, String transType, String billDate, String posCode, String viewORprint)
    {
        try
        {
            String lineFor1 = "  ----------------------------------------";
            String lineForTotalHighLight = "                                ----------";
            String user = "";
            String billhd;
            String billdtl;
            String billModifierdtl;
            String billSettlementdtl;
            String billtaxdtl;
            String billDscFrom = "tblbilldiscdtl";
            String billPromoDtl;
            if (clsGlobalVarClass.gHOPOSType.equalsIgnoreCase("HOPOS"))
            {
                billhd = "tblqbillhd";
                billdtl = "tblqbilldtl";
                billModifierdtl = "tblqbillmodifierdtl";
                billSettlementdtl = "tblqbillsettlementdtl";
                billtaxdtl = "tblqbilltaxdtl";
                billDscFrom = "tblqbilldiscdtl";
                billPromoDtl = "tblqbillpromotiondtl";
            }
            else
            {
                if ("sales report".equalsIgnoreCase(formName))
                {
                    billhd = "tblbillhd";
                    billdtl = "tblbilldtl";
                    billModifierdtl = "tblbillmodifierdtl";
                    billSettlementdtl = "tblbillsettlementdtl";
                    billtaxdtl = "tblbilltaxdtl";
                    billDscFrom = "tblbilldiscdtl";
                    billPromoDtl = "tblbillpromotiondtl";

                    long dateDiff = new clsUtility().funCompareDate(billDate, objUtility.funGetPOSDateForTransaction());
                    if (dateDiff > 0)
                    {
                        billhd = "tblqbillhd";
                        billdtl = "tblqbilldtl";
                        billModifierdtl = "tblqbillmodifierdtl";
                        billSettlementdtl = "tblqbillsettlementdtl";
                        billtaxdtl = "tblqbilltaxdtl";
                        billDscFrom = "tblqbilldiscdtl";
                        billPromoDtl = "tblqbillpromotiondtl";
                    }
                }
                else
                {
                    billhd = "tblbillhd";
                    billdtl = "tblbilldtl";
                    billModifierdtl = "tblbillmodifierdtl";
                    billSettlementdtl = "tblbillsettlementdtl";
                    billtaxdtl = "tblbilltaxdtl";
                    billPromoDtl = "tblbillpromotiondtl";
                }
            }
            PreparedStatement pst = null;
            objPrintingUtility.funCreateTempFolder();
            String filePath = System.getProperty("user.dir");
            File Text_Bill = new File(filePath + "/Temp/Temp_Bill.txt");
            String subTotal = "";
            double totalSubTotal = 0.00;
            String discount = "";
            String tax = "";
            String grandTotal = "";
            String advAmount = "";
            String deliveryCharge = "";
            String customerCode = "";
            int discPer = 0;
            boolean flag_DirectBiller = false;
            FileWriter fstream_bill = new FileWriter(Text_Bill);
            BufferedWriter BillOut = new BufferedWriter(fstream_bill);
            boolean isReprint = false;
            if ("reprint".equalsIgnoreCase(reprint))
            {
                isReprint = true;
                objPrintingUtility.funPrintBlankSpace("[DUPLICATE]", BillOut);
                BillOut.write("[DUPLICATE]");
                BillOut.newLine();
            }

            if (transType.equals("Void"))
            {
                objPrintingUtility.funPrintBlankSpace("VOIDED BILL", BillOut);
                BillOut.write("VOIDED BILL");
                BillOut.newLine();
            }
            boolean flag_isHomeDelvBill = false;
            String SQL_HomeDelivery = "select strBillNo,strCustomerCode,strDPCode,tmeTime from tblhomedelivery where strBillNo=? ;";
            pst = clsGlobalVarClass.conPrepareStatement.prepareStatement(SQL_HomeDelivery);
            pst.setString(1, billNo);
            ResultSet rs_HomeDelivery = pst.executeQuery();

            if (rs_HomeDelivery.next())
            {
                flag_isHomeDelvBill = true;
                customerCode = rs_HomeDelivery.getString(2);
                if (clsGlobalVarClass.gPrintHomeDeliveryYN)
                {
                    objPrintingUtility.funPrintBlankSpace("HOME DELIVERY", BillOut);
                    BillOut.write("HOME DELIVERY");
                    BillOut.newLine();
                }

                String SQL_CustomerDtl = "select a.strCustomerName,a.strBuildingName"
                        + " ,a.strStreetName,a.strLandmark,a.strArea,a.strCity,a.longMobileNo,b.strAddress "
                        + " from tblcustomermaster a left outer join tblbuildingmaster b "
                        + " on a.strBuldingCode=b.strBuildingCode "
                        + " where a.strCustomerCode=? ;";
                pst = clsGlobalVarClass.conPrepareStatement.prepareStatement(SQL_CustomerDtl);
                pst.setString(1, rs_HomeDelivery.getString(2));
                ResultSet rs_CustomerDtl = pst.executeQuery();
                while (rs_CustomerDtl.next())
                {
                    BillOut.write("  NAME      :" + rs_CustomerDtl.getString(1).toUpperCase());
                    BillOut.newLine();
                    if (null != rs_CustomerDtl.getString(8) && rs_CustomerDtl.getString(8).trim().length() > 0)
                    {
                        BillOut.write("  ADDRESS1   :" + rs_CustomerDtl.getString(8).toUpperCase());
                        BillOut.newLine();
                    }
                    String add = rs_CustomerDtl.getString(2);
                    int strlen = add.length();
                    String add1 = "";
                    if (strlen < 28)
                    {
                        add1 = add.substring(0, strlen);
                        BillOut.write("  ADDRESS2   :" + add1.toUpperCase());
                        BillOut.newLine();
                    }
                    else
                    {
                        add1 = add.substring(0, 28);
                        BillOut.write("  ADDRESS2   :" + add1.toUpperCase());
                        BillOut.newLine();

                    }
                    //add1=add.substring(len,56);
                    for (int i = 28; i <= strlen; i++)
                    {
                        int end = 0;
                        end = i + 28;
                        if (strlen > end)
                        {
                            add1 = add.substring(i, end);
                            i = end;
                            BillOut.write("             " + add1.toUpperCase());
                            BillOut.newLine();
                        }
                        else
                        {
                            add1 = add.substring(i, strlen);
                            BillOut.write("             " + add1.toUpperCase());
                            BillOut.newLine();
                            i = strlen + 1;
                        }
                    }

                    String street = rs_CustomerDtl.getString(3);
                    String street1;
                    int streetlen = street.length();
                    for (int i = 0; i <= streetlen; i++)
                    {
                        int end = 0;
                        end = i + 28;
                        if (streetlen > end)
                        {
                            street1 = street.substring(i, end);
                            BillOut.write("             " + street1.toUpperCase());
                            BillOut.newLine();
                            i = end;
                        }
                        else
                        {
                            street1 = street.substring(i, streetlen);
                            BillOut.write("             " + street1.toUpperCase());
                            BillOut.newLine();
                            i = streetlen + 1;
                        }
                    }

                    if (rs_CustomerDtl.getString(4).trim().length() > 0)
                    {
                        BillOut.write("             " + rs_CustomerDtl.getString(4).toUpperCase());
                        BillOut.newLine();
                    }
                    if (rs_CustomerDtl.getString(4).trim().length() > 0)
                    {
                        BillOut.write("             " + rs_CustomerDtl.getString(5).toUpperCase());
                        BillOut.newLine();
                    }
                    if (rs_CustomerDtl.getString(4).trim().length() > 0)
                    {
                        BillOut.write("             " + rs_CustomerDtl.getString(6).toUpperCase());
                        BillOut.newLine();
                    }
                    BillOut.write("  MOBILE NO :" + rs_CustomerDtl.getString(7));
                    BillOut.newLine();
                }
                rs_CustomerDtl.close();
                if (null != rs_HomeDelivery.getString(3) && rs_HomeDelivery.getString(3).trim().length() > 0)
                {
                    String SQL_DeliveryBoyDtl = "select strDPName from tbldeliverypersonmaster where strDPCode=? ;";
                    pst = clsGlobalVarClass.conPrepareStatement.prepareStatement(SQL_DeliveryBoyDtl);

                    pst.setString(1, rs_HomeDelivery.getString(3));
                    ResultSet rs_DeliveryBoyDtl = pst.executeQuery();
                    if (rs_DeliveryBoyDtl.next())
                    {
                        BillOut.write("  DELV BOY  :" + rs_DeliveryBoyDtl.getString(1).toUpperCase());
                    }
                    BillOut.newLine();
                    rs_DeliveryBoyDtl.close();
                }
                /*
                 * BillOut.write(" DELV TIME :" +
                 * rs_HomeDelivery.getString(4).toUpperCase());
                 * BillOut.newLine();
                 */
                BillOut.write(lineFor1);
                BillOut.newLine();
            }
            rs_HomeDelivery.close();

            objPrintingUtility.funPrintTakeAway(billhd, billNo, BillOut, 4);

            if (clsGlobalVarClass.gPrintTaxInvoice.equalsIgnoreCase("Y"))
            {
                objPrintingUtility.funPrintBlankSpace("TAX INVOICE", BillOut);
                BillOut.write("TAX INVOICE");
                BillOut.newLine();
            }

            if (clsGlobalVarClass.gClientCode.equals("092.001") || clsGlobalVarClass.gClientCode.equals("092.002") || clsGlobalVarClass.gClientCode.equals("092.003"))//Shree Sound Pvt. Ltd.(Waters)
            {
                objPrintingUtility.funPrintBlankSpace("SSPL", BillOut);
                BillOut.write("SSPL");
                BillOut.newLine();
            }
            else
            {
                objPrintingUtility.funPrintBlankSpace(clsGlobalVarClass.gClientName, BillOut);
                BillOut.write(clsGlobalVarClass.gClientName.toUpperCase());
                BillOut.newLine();
            }
            objPrintingUtility.funPrintBlankSpace(clsGlobalVarClass.gClientAddress1, BillOut);
            BillOut.write(clsGlobalVarClass.gClientAddress1.toUpperCase());
            BillOut.newLine();

            if (clsGlobalVarClass.gClientAddress2.trim().length() > 0)
            {
                objPrintingUtility.funPrintBlankSpace(clsGlobalVarClass.gClientAddress2, BillOut);
                BillOut.write(clsGlobalVarClass.gClientAddress2.toUpperCase());
                BillOut.newLine();
            }

            if (clsGlobalVarClass.gClientAddress3.trim().length() > 0)
            {
                objPrintingUtility.funPrintBlankSpace(clsGlobalVarClass.gClientAddress3, BillOut);
                BillOut.write(clsGlobalVarClass.gClientAddress3.toUpperCase());
                BillOut.newLine();
            }

            if (clsGlobalVarClass.gCityName.trim().length() > 0)
            {
                objPrintingUtility.funPrintBlankSpace(clsGlobalVarClass.gCityName, BillOut);
                BillOut.write(clsGlobalVarClass.gCityName.toUpperCase());
                BillOut.newLine();
            }

            BillOut.write("  TEL NO.   :" + " ");
            BillOut.write(String.valueOf(clsGlobalVarClass.gClientTelNo));
            BillOut.newLine();
            BillOut.write("  EMAIL ID  :" + " ");
            BillOut.write(clsGlobalVarClass.gClientEmail);
            BillOut.newLine();
            String query = "";
            String SQL_BillHD = "";
            String waiterName = "";
            String waiterNo = "";
            String tblName = "";
            ResultSet rsQuery = null;
            ResultSet rs_BillHD = null;
            ResultSet rsTblName = null;
            String sqlTblName = "";
            String tabNo = "";
            boolean flag_DirectBillerBlill = false;
            if (objPrintingUtility.funIsDirectBillerBill(billNo, billhd))
            {
                flag_DirectBillerBlill = true;
                SQL_BillHD = "select a.dteBillDate,time(a.dteBillDate),a.dblDiscountAmt,a.dblSubTotal,"
                        + "a.strCustomerCode,a.dblGrandTotal,a.dblTaxAmt,a.strReasonCode,a.strRemarks,a.strUserCreated "
                        + ",ifnull(dblDeliveryCharges,0.00),ifnull(b.dblAdvDeposite,0.00),a.dblDiscountPer ,c.strCustomerName,c.longMobileNo"
                        + ",strTakeAwayRemarks "
                        + "from " + billhd + " a left outer join tbladvancereceipthd b on a.strAdvBookingNo=b.strAdvBookingNo "
                        + "left outer join tblcustomermaster c on c.strCustomerCode=a.strCustomerCode "
                        + "where a.strBillNo=?  ";
                flag_DirectBiller = true;
                pst = clsGlobalVarClass.conPrepareStatement.prepareStatement(SQL_BillHD);

                pst.setString(1, billNo);
                rs_BillHD = pst.executeQuery();
                rs_BillHD.next();
            }
            else
            {
                SQL_BillHD = "select a.strTableNo,a.strWaiterNo,a.dteBillDate,time(a.dteBillDate),a.dblDiscountAmt,a.dblSubTotal,"
                        + "a.strCustomerCode,a.dblGrandTotal,a.dblTaxAmt,a.strReasonCode,a.strRemarks,a.strUserCreated "
                        + ",dblDeliveryCharges,ifnull(c.dblAdvDeposite,0.00),a.dblDiscountPer "
                        + "from " + billhd + " a left outer join tbltablemaster b on a.strTableNo=b.strTableNo "
                        + "left outer join tbladvancereceipthd c on a.strAdvBookingNo=c.strAdvBookingNo "
                        + "where a.strBillNo=?  ";
                pst = clsGlobalVarClass.conPrepareStatement.prepareStatement(SQL_BillHD);
                pst.setString(1, billNo);
                rs_BillHD = pst.executeQuery();
                rs_BillHD.next();
                tabNo = rs_BillHD.getString(1);

                if (rs_BillHD.getString(2).equalsIgnoreCase("null") || rs_BillHD.getString(2).equalsIgnoreCase(""))
                {
                    waiterNo = "";
                }
                else
                {
                    waiterNo = rs_BillHD.getString(2);
                    query = "select strWShortName from tblwaitermaster where strWaiterNo=? ;";
                    pst = clsGlobalVarClass.conPrepareStatement.prepareStatement(query);
                    pst.setString(1, waiterNo);
                    rsQuery = pst.executeQuery();
                    if(rsQuery.next())
                    {
                    waiterName = rsQuery.getString(1);
                    }
                    rsQuery.close();
                    pst.close();
                }

                sqlTblName = "select strTableName from tbltablemaster where strTableNo=? ;";
                pst = clsGlobalVarClass.conPrepareStatement.prepareStatement(sqlTblName);
                pst.setString(1, tabNo);
                rsTblName = pst.executeQuery();
                if(rsTblName.next())
                {
                tblName = rsTblName.getString(1);
                }
                rsTblName.close();
                pst.close();
            }

            if (flag_DirectBillerBlill)
            {
                BillOut.write(lineFor1);
                BillOut.newLine();
                BillOut.write("  BILL NO.    : ");
                BillOut.write(billNo);
                //BillOut.write("  ");
                BillOut.newLine();
                if (clsGlobalVarClass.gPrintTimeOnBillYN)
                {
                    BillOut.write("  DATE & TIME : ");
                    SimpleDateFormat ft = new SimpleDateFormat("dd-MM-yyyy hh:mm a ");
                    BillOut.write(ft.format(rs_BillHD.getObject(1)));
                    BillOut.newLine();
                }
                else
                {
                    BillOut.write("  DATE        : ");
                    SimpleDateFormat ft = new SimpleDateFormat("dd-MM-yyyy");
                    BillOut.write(ft.format(rs_BillHD.getObject(1)));
                    BillOut.newLine();
                }
                if (clsGlobalVarClass.gRemarksOnTakeAway)
                {
                    if (!rs_BillHD.getObject(16).equals(""))
                    {
                        BillOut.write("  Cust Name   : ");
                        BillOut.write(rs_BillHD.getString(16));
                        BillOut.newLine();
                    }
                }

                subTotal = rs_BillHD.getString(4);
                totalSubTotal = rs_BillHD.getDouble(4);
                discount = rs_BillHD.getString(3);
                tax = rs_BillHD.getString(7);
                grandTotal = rs_BillHD.getString(6);
                user = rs_BillHD.getString(10);
                deliveryCharge = rs_BillHD.getString(11);
                advAmount = rs_BillHD.getString(12);
                discPer = rs_BillHD.getInt(13);
            }
            else
            {
                BillOut.write("  TABLE NAME:" + "  ");
                BillOut.write(tblName);
                BillOut.newLine();
                if (waiterName.trim().length() > 0)
                {
                    BillOut.write("  STEWARD   :" + "  ");
                    BillOut.write(waiterName);
                    BillOut.newLine();
                }

                BillOut.write(lineFor1);
                BillOut.newLine();
                BillOut.write("  BILL NO.    : ");
                BillOut.write(billNo);
                BillOut.newLine();
                if (clsGlobalVarClass.gPrintTimeOnBillYN)
                {
                    BillOut.write("  DATE & TIME : ");
                    SimpleDateFormat ft = new SimpleDateFormat("dd-MM-yyyy hh:mm a ");
                    BillOut.write(ft.format(rs_BillHD.getObject(3)));
                    BillOut.newLine();
                }
                else
                {
                    BillOut.write("  DATE        : ");
                    SimpleDateFormat ft = new SimpleDateFormat("dd-MM-yyyy");
                    BillOut.write(ft.format(rs_BillHD.getObject(3)));
                    BillOut.newLine();
                }

                subTotal = rs_BillHD.getString(6);
                totalSubTotal = rs_BillHD.getDouble(6);

                discount = rs_BillHD.getString(5);
                tax = rs_BillHD.getString(9);
                grandTotal = rs_BillHD.getString(8);
                user = rs_BillHD.getString(12);
                deliveryCharge = rs_BillHD.getString(13);
                advAmount = rs_BillHD.getString(14);
                discPer = rs_BillHD.getInt(15);
            }
            //print card available balance
            String isSttled = "select strBillNo from " + billSettlementdtl + " where strBillNo='" + billNo + "';  ";
            ResultSet rsIsSettled = clsGlobalVarClass.dbMysql.executeResultSet(isSttled);
            if (rsIsSettled.next())
            {
                rsIsSettled.close();
                String availBal = "select a.strCardNo,(b.dblRedeemAmt)"
                        + "from " + billhd + " a inner join tbldebitcardmaster b on a.strCardNo=b.strCardNo "
                        + "where a.strBillNo='" + billNo + "' "
                        + "and date(a.dteBillDate)='" + billDate + "' ";;
                ResultSet rsAvailBal = clsGlobalVarClass.dbMysql.executeResultSet(availBal);
                if (rsAvailBal.next())
                {
                    BillOut.write("  Available Balance(" + rsAvailBal.getString(1) + "):" + rsAvailBal.getString(2));
                    BillOut.newLine();
                }
            }
            else
            {
                String availBal = "select a.strCardNo,(b.dblRedeemAmt-a.dblGrandTotal)"
                        + "from " + billhd + " a inner join tbldebitcardmaster b on a.strCardNo=b.strCardNo "
                        + "where a.strBillNo='" + billNo + "' "
                        + "and date(a.dteBillDate)='" + billDate + "' ";;
                ResultSet rsAvailBal = clsGlobalVarClass.dbMysql.executeResultSet(availBal);
                if (rsAvailBal.next())
                {
                    BillOut.write("  Available Balance(" + rsAvailBal.getString(1) + "):" + rsAvailBal.getString(2));
                    BillOut.newLine();
                }
            }
            //print card available balance

            if (transType.equals("Void"))
            {
                String reasonName = "select a.strReasonName "
                        + "from tblvoidbillhd a "
                        + "where a.strBillNo='" + billNo + "'; ";
                ResultSet rsReason = clsGlobalVarClass.dbMysql.executeResultSet(reasonName);
                if (rsReason.next())
                {
                    BillOut.write("  Reason      :" + " " + rsReason.getString(1));
                    BillOut.newLine();
                }
                rsReason.close();
            }
            else
            {
                String reasonName = "select ifnull(c.strReasonName,''),a.strRemarks "
                        + "from tblbillhd a,tblbilldtl b,tblreasonmaster c, tblbillsettlementdtl d,tblsettelmenthd e "
                        + "where a.strBillNo=b.strBillNo "
                        + "and a.strReasonCode=c.strReasonCode "
                        + "and a.strBillNo=d.strBillNo "
                        + "and d.strSettlementCode=e.strSettelmentCode "
                        + "and e.strSettelmentType='Complementary' "
                        + "and a.strBillNo='" + billNo + "' "
                        + "and date(a.dteBillDate)=date(b.dteBillDate) "
                        + "group by a.strBillNo; ";
                ResultSet rsReason = clsGlobalVarClass.dbMysql.executeResultSet(reasonName);
                if (rsReason.next())
                {
                    BillOut.write("  Reason      :" + " " + rsReason.getString(1));
                    BillOut.newLine();
                    BillOut.write("  Remark      :" + " " + rsReason.getString(2));
                    BillOut.newLine();
                }
                rsReason.close();
            }

            if (clsGlobalVarClass.gCMSIntegrationYN)
            {
                String sql = "select b.strCustomerCode,b.strCustomerName from " + billhd + " a,tblcustomermaster b "
                        + " where a.strCustomerCode=b.strCustomerCode and a.strBillNo='" + billNo + "'"
                        + "and date(a.dteBillDate)='" + billDate + "' ";
                ResultSet rsBillPrint = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                if (rsBillPrint.next())
                {
                    BillOut.write("  Member Code : ");
                    BillOut.write(rsBillPrint.getString(1));
                    BillOut.newLine();
                    BillOut.write("  Member Name : ");
                    BillOut.write(rsBillPrint.getString(2));
                    BillOut.newLine();
                }
                rsBillPrint.close();
            }
            BillOut.write(lineFor1);
            ////////////////////////llllllllll/////////////////////////////

            String sql_advOrder_SuTtotal = "select a.strAdvBookingNo,a.strMessage,a.strShape,a.strNote"
                    + " from tbladvbookbillhd a, " + billhd + " b where a.strAdvBookingNo=b.strAdvBookingNo "
                    + "and b.strBillNo=? ";
            pst = clsGlobalVarClass.conPrepareStatement.prepareStatement(sql_advOrder_SuTtotal);

            pst.setString(1, billNo);
            ResultSet rs_advOrder_Tot = pst.executeQuery();
            if (rs_advOrder_Tot.next())
            {
                objPrintingUtility.funPrintBlankSpace("ORDER DETAIL", BillOut);
                BillOut.write("ORDER DETAIL");
                BillOut.newLine();
                BillOut.write(lineFor1);
                BillOut.newLine();
                String msg = rs_advOrder_Tot.getString(2).toUpperCase();
                String shape = rs_advOrder_Tot.getString(3).toUpperCase();
                String note = rs_advOrder_Tot.getString(4).toUpperCase();
                int strlenMsg = msg.length();
                if (strlenMsg > 0)
                {
                    String msg1 = "";
                    if (strlenMsg < 27)
                    {
                        msg1 = msg.substring(0, strlenMsg);
                        BillOut.write("  MESSAGE     :" + msg1);
                        BillOut.newLine();
                    }
                    else
                    {
                        msg1 = msg.substring(0, 27);
                        BillOut.write("  MESSAGE     :" + msg1);;
                        BillOut.newLine();
                    }
                    for (int i = 27; i <= strlenMsg; i++)
                    {
                        int endmsg = 0;
                        endmsg = i + 27;
                        if (strlenMsg > endmsg)
                        {
                            msg1 = msg.substring(i, endmsg);
                            i = endmsg;
                            BillOut.write("               " + msg1);
                            BillOut.newLine();
                        }
                        else
                        {
                            msg1 = msg.substring(i, strlenMsg);
                            BillOut.write("               " + msg1);
                            BillOut.newLine();
                            i = strlenMsg + 1;
                        }
                    }
                }
                //////////////////////////////////////////////////

                int strlenShape = shape.length();
                if (strlenShape > 0)
                {

                    String shape1 = "";
                    if (strlenShape < 27)
                    {
                        shape1 = shape.substring(0, strlenShape);
                        BillOut.write("  SHAPE       :" + shape1);
                        BillOut.newLine();
                    }
                    else
                    {
                        shape1 = shape.substring(0, 27);
                        BillOut.write("  SHAPE       :" + shape1);
                        BillOut.newLine();
                    }
                    for (int j = 27; j <= strlenShape; j++)
                    {
                        int endShape = 0;
                        endShape = j + 27;
                        if (strlenShape > endShape)
                        {
                            shape1 = shape.substring(j, endShape);
                            j = endShape;
                            BillOut.write("               " + shape1);
                            BillOut.newLine();
                        }
                        else
                        {
                            shape1 = shape.substring(j, strlenShape);
                            BillOut.write("               " + shape1);
                            BillOut.newLine();
                            j = strlenShape + 1;
                        }
                    }
                }
                int strlenNote = note.length();
                if (strlenNote > 0)
                {

                    String note1 = "";
                    if (strlenNote < 27)
                    {
                        note1 = note.substring(0, strlenNote);
                        BillOut.write("  NOTE        :" + note1);
                        BillOut.newLine();
                    }
                    else
                    {
                        note1 = note.substring(0, 27);
                        BillOut.write("  NOTE        :" + note1);
                        BillOut.newLine();
                    }
                    for (int i = 27; i <= strlenNote; i++)
                    {
                        int endNote = 0;
                        endNote = i + 27;
                        if (strlenNote > endNote)
                        {
                            note1 = note.substring(i, endNote);
                            i = endNote;
                            BillOut.write("               " + note1);
                            BillOut.newLine();
                        }
                        else
                        {
                            note1 = note.substring(i, strlenNote);
                            BillOut.write("               " + note1);
                            BillOut.newLine();
                            i = strlenNote + 1;
                        }
                    }
                }
                BillOut.newLine();
                BillOut.write(lineFor1);
            }
            /////////////////////////lllllllll////////////////////////////           
            BillOut.newLine();
            BillOut.write("     QTY  ITEM NAME                  AMT");
            BillOut.newLine();
            BillOut.write(lineFor1);
            BillOut.newLine();

            String SQL_BillDtl = "SELECT SUM(a.dblQuantity),a.strItemName, SUM(a.dblAmount),a.strItemCode,a.strKOTNo,b.strItemType "
                    + "FROM " + billdtl + "  a,tblitemmaster b "
                    + "WHERE a.strBillNo=? "
                    + "AND a.tdhYN='N' "
                    + "AND a.strItemCode=b.strItemCode "
                    + "and date(a.dteBillDate) = '" + billDate + "' ";
            if (!clsGlobalVarClass.gPrintOpenItemsOnBill)
            {
                SQL_BillDtl += "and a.dblAmount>0 ";
            }
            SQL_BillDtl += "GROUP BY b.strItemType,a.strItemCode "
                    + "ORDER BY b.strItemType,a.strItemCode ";
            pst = clsGlobalVarClass.conPrepareStatement.prepareStatement(SQL_BillDtl);
            pst.setString(1, billNo);
            ResultSet rs_BillDtl = pst.executeQuery();
            String itemType = "";
            String itemTypeHeadingPrefix = "  ---------------";
            String itemTypeHeadingSuffix = "-------------------------";
            double itemTypeTotal = 0.00;
            for (int r = 0; rs_BillDtl.next(); r++)
            {
                if (r == 0)
                {
                    itemType = rs_BillDtl.getString(6);
                    int itemTypeLength = itemType.length();
                    BillOut.write(itemTypeHeadingPrefix + itemType.toUpperCase() + itemTypeHeadingSuffix.substring(itemTypeLength));
                    BillOut.newLine();
                }
                else if (itemType.equalsIgnoreCase(rs_BillDtl.getString(6)))
                {
                }
                else
                {
                    BillOut.write(lineForTotalHighLight);
                    BillOut.newLine();
                    objPrintingUtility.funWriteTotal(itemType.toUpperCase() + " TOTAL", String.valueOf(itemTypeTotal), BillOut, "Format1");
                    BillOut.newLine();
                    itemTypeTotal = 0;

                    itemType = rs_BillDtl.getString(6);
                    int itemTypeLength = itemType.length();
                    BillOut.write(itemTypeHeadingPrefix + itemType.toUpperCase() + itemTypeHeadingSuffix.substring(itemTypeLength));
                    BillOut.newLine();
                }

                double amount = Double.parseDouble(rs_BillDtl.getString(3));
                amount = Double.parseDouble(stdDecimalFormat.format(amount));

                itemTypeTotal += amount;

                objPrintingUtility.funWriteToText(BillOut, rs_BillDtl.getString(1), rs_BillDtl.getString(2).toUpperCase(), String.valueOf(amount), "Format1");
                BillOut.newLine();
                String sqlModifier = "select Count(*) from " + billModifierdtl + " where strBillNo=? "
                        + "and left(strItemCode,7)=? ";
                if (!clsGlobalVarClass.gPrintZeroAmtModifierOnBill)
                {
                    sqlModifier += " and  dblAmount !=0.00 ";
                }
                pst = clsGlobalVarClass.conPrepareStatement.prepareStatement(sqlModifier);

                pst.setString(1, billNo);
                pst.setString(2, rs_BillDtl.getString(4));
                ResultSet rs_count = pst.executeQuery();
                rs_count.next();
                int cntRecord = rs_count.getInt(1);
                rs_count.close();
                if (cntRecord > 0)
                {
                    sqlModifier = "select strModifierName,dblQuantity,dblAmount "
                            + "from " + billModifierdtl + " where strBillNo=? and left(strItemCode,7)=? ";
                    if (!clsGlobalVarClass.gPrintZeroAmtModifierOnBill)
                    {
                        sqlModifier += " and  dblAmount !=0.00 ";
                    }
                    pst = clsGlobalVarClass.conPrepareStatement.prepareStatement(sqlModifier);

                    pst.setString(1, billNo);
                    pst.setString(2, rs_BillDtl.getString(4));
                    ResultSet rs_modifierRecord = pst.executeQuery();
                    while (rs_modifierRecord.next())
                    {
                        amount = Double.parseDouble(rs_modifierRecord.getString(3));
                        amount = Double.parseDouble(stdDecimalFormat.format(amount));
                        itemTypeTotal += amount;

                        objPrintingUtility.funWriteToText(BillOut, "", rs_modifierRecord.getString(1).toUpperCase(), rs_modifierRecord.getString(3), "Format1");
                        BillOut.newLine();
                    }
                    rs_modifierRecord.close();
                }

                if (rs_BillDtl.isLast())
                {
                    BillOut.write(lineForTotalHighLight);
                    BillOut.newLine();
                    objPrintingUtility.funWriteTotal(itemType.toUpperCase() + " TOTAL", String.valueOf(itemTypeTotal), BillOut, "Format1");
                    BillOut.newLine();
                    itemTypeTotal = 0;
                }

            }
            rs_BillDtl.close();

            //funPrintPromoItemsInBill(billNo, BillOut,4);  // Print Promotion Items in Bill for this billno.
            BillOut.write(lineFor1);
            BillOut.newLine();

            if (clsGlobalVarClass.gPointsOnBillPrint)
            {
                String sql_CRMPoints = "select b.dblPoints from tblbillhd a, tblcrmpoints b "
                        + "where a.strBillNo=b.strBillNo and a.strBillNo='" + billNo + "'";
                ResultSet rsCRMPoints = clsGlobalVarClass.dbMysql.executeResultSet(sql_CRMPoints);
                if (rsCRMPoints.next())
                {
                    objPrintingUtility.funWriteTotal("POINTS ", rsCRMPoints.getString(1), BillOut, "Format1");
                }
                rsCRMPoints.close();
                BillOut.newLine();
            }
            objPrintingUtility.funWriteTotal("SUB TOTAL", subTotal, BillOut, "Format1");
            BillOut.newLine();
            String sql = "select a.dblDiscPer,a.dblDiscAmt,a.strDiscOnType,a.strDiscOnValue "
                    + "from " + billDscFrom + " a "
                    + "where a.strBillNo='" + billNo + "' ";
            ResultSet rsDisc = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            boolean flag = true;
            double totalDisc = 0.00;
            while (rsDisc.next())
            {
                if (flag)
                {
                    flag = false;
                    BillOut.write("  DISCOUNT");
                    BillOut.newLine();
                }
                double dbl = Double.parseDouble(rsDisc.getString("dblDiscPer"));
                String discText = String.format("%.1f", dbl) + "%" + " On " + rsDisc.getString("strDiscOnValue") + "";
                if (discText.length() > 30)
                {
                    discText = discText.substring(0, 30);
                }
                else
                {
                    discText = String.format("%-30s", discText);
                }
                BillOut.write("  " + discText);
                String discountOnItem = objUtility.funPrintTextWithAlignment(rsDisc.getString("dblDiscAmt"), 9, "Right");
                BillOut.write(discountOnItem);
                BillOut.newLine();
                totalDisc += rsDisc.getDouble(2);
            }
            if (totalDisc > 0)
            {
                double netTotal = totalSubTotal - totalDisc;
                objPrintingUtility.funWriteTotal("NET TOTAL", String.valueOf(netTotal), BillOut, "Format1");
                BillOut.newLine();
            }

            String sql_Tax = "select b.strTaxDesc,sum(a.dblTaxAmount) "
                    + " from " + billtaxdtl + " a,tbltaxhd b "
                    + " where a.strBillNo='" + billNo + "' and a.strTaxCode=b.strTaxCode "
                    + " and b.strTaxCalculation='Forward' "
                    + " group by a.strTaxCode";
            ResultSet rsTax = clsGlobalVarClass.dbMysql.executeResultSet(sql_Tax);
            while (rsTax.next())
            {
                objPrintingUtility.funWriteTotal(rsTax.getString(1), rsTax.getString(2), BillOut, "Format1");
                BillOut.newLine();
            }
            if (deliveryCharge != null && deliveryCharge.trim().length() > 0 && !"0.00".equalsIgnoreCase(deliveryCharge))
            {
                objPrintingUtility.funWriteTotal("DELV. CHARGE", deliveryCharge, BillOut, "Format1");
                BillOut.newLine();
            }

            if (advAmount.trim().length() > 0 && !"0.00".equalsIgnoreCase(advAmount))
            {
                objPrintingUtility.funWriteTotal("ADVANCE", advAmount, BillOut, "Format1");
                BillOut.newLine();
            }

            BillOut.write(lineFor1);
            BillOut.newLine();
            objPrintingUtility.funWriteTotal("TOTAL(ROUNDED)", grandTotal, BillOut, "Format1");

            //BillOut.write(lineFor1);
            BillOut.newLine();
            //settlement breakup part
            String SQL_Settlement_Breakup = "select a.dblSettlementAmt ,b.strSettelmentDesc from " + billSettlementdtl + " a ,"
                    + "tblsettelmenthd b where a.strBillNo=? and a.strSettlementCode=b.strSettelmentCode";
            pst = clsGlobalVarClass.conPrepareStatement.prepareStatement(SQL_Settlement_Breakup);

            pst.setString(1, billNo);
            ResultSet rs_Bill_Settlement = pst.executeQuery();
            while (rs_Bill_Settlement.next())
            {
                objPrintingUtility.funWriteTotal(rs_Bill_Settlement.getString(2), rs_Bill_Settlement.getString(1), BillOut, "Format1");
                BillOut.newLine();
            }
            rs_Bill_Settlement.close();
            if (flag_isHomeDelvBill)
            {
                BillOut.write(lineFor1);
                BillOut.newLine();
                String sql_count = "select count(*) from tblhomedelivery where strCustomerCode=?";
                pst = clsGlobalVarClass.conPrepareStatement.prepareStatement(sql_count);
                pst.setString(1, customerCode);
                ResultSet rs_Count = pst.executeQuery();
                rs_Count.next();
                BillOut.write("  CUSTOMER COUNT : " + rs_Count.getString(1));
                rs_Count.close();
                BillOut.newLine();
            }
            //footer part
            BillOut.write(lineFor1);
            BillOut.newLine();
            if (clsGlobalVarClass.gPrintInclusiveOfAllTaxes.equalsIgnoreCase("Y"))
            {
                objPrintingUtility.funPrintBlankSpace("(INCLUSIVE OF ALL TAXES)", BillOut);
                BillOut.write("(INCLUSIVE OF ALL TAXES)");
                BillOut.newLine();
            }
            BillOut.newLine();

            objPrintingUtility.funPrintServiceVatNo(BillOut, 4, billNo, billDate, billtaxdtl);

            int num = clsGlobalVarClass.gBillFooter.trim().length() / 30;
            int num1 = clsGlobalVarClass.gBillFooter.trim().length() % 30;
            int cnt1 = 0;
            for (int cnt = 0; cnt < num; cnt++)
            {
                String footer = clsGlobalVarClass.gBillFooter.trim().substring(cnt1, (cnt1 + 30));
                footer = footer.replaceAll("\n", "");
                BillOut.write("     " + footer.trim());
                BillOut.newLine();
                cnt1 += 30;
            }
            BillOut.write("     " + clsGlobalVarClass.gBillFooter.trim().substring(cnt1, (cnt1 + num1)).trim());
            BillOut.newLine();

            if (!clsGlobalVarClass.gClientCode.equals("100.001"))
            {
                objPrintingUtility.funPrintBlankSpace(user, BillOut);
                BillOut.write(user);
            }

            for (int i = 0; i < 5; i++)
            {
                BillOut.newLine();
            }

            //BillOut.write("m");
            if ("linux".equalsIgnoreCase(clsPosConfigFile.gPrintOS))
            {
                BillOut.write("V");//Linux
            }
            else if ("windows".equalsIgnoreCase(clsPosConfigFile.gPrintOS))
            {
                if ("Inbuild".equalsIgnoreCase(clsPosConfigFile.gPrinterType))
                {
                    BillOut.write("V");
                }
                else if ("Star".equalsIgnoreCase(clsPosConfigFile.gPrinterType))
                {
                    BillOut.write(" P ");
                }
                else
                {
                    BillOut.write("m");//windows
                }
            }
            // BillOut.write("V");//Linux
            rs_BillHD.close();
            BillOut.close();
            fstream_bill.close();
            pst.close();

            if (formName.equalsIgnoreCase("sales report"))
            {
                objPrintingUtility.funShowTextFile(Text_Bill, formName, clsGlobalVarClass.gBillPrintPrinterPort);
            }
            else
            {
                if (clsGlobalVarClass.gShowBill)
                {
                    objPrintingUtility.funShowTextFile(Text_Bill, formName, clsGlobalVarClass.gBillPrintPrinterPort);
                }
            }
            if (!formName.equalsIgnoreCase("sales report"))
            {
                if (transType.equalsIgnoreCase("void"))
                {
                    if (clsGlobalVarClass.gPrintOnVoidBill)
                    {
                        if (!viewORprint.equalsIgnoreCase("view"))
                        {
                            objPrintingUtility.funPrintToPrinter(clsGlobalVarClass.gBillPrintPrinterPort, "", "bill", "N", isReprint,"");
                        }
                    }
                }
                else
                {
                    if (!viewORprint.equalsIgnoreCase("view"))
                    {
                        objPrintingUtility.funPrintToPrinter(clsGlobalVarClass.gBillPrintPrinterPort, "", "bill", "N", isReprint,"");
                    }
                }
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
