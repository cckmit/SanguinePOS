/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSTransaction.view;

import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsUtility;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class frmPostPOSDataToCMS extends javax.swing.JFrame {

    private String posName="",posDate="",time="";
    private clsUtility objUtility;
    
    public frmPostPOSDataToCMS() {
        initComponents();
        try {
            objUtility=new clsUtility();
            String bdte=clsGlobalVarClass.gPOSStartDate;
            SimpleDateFormat dFormat=new SimpleDateFormat("yyyy-MM-dd");
            Date bDate=dFormat.parse(bdte);
            String date1=(bDate.getYear()+1900)+"-"+(bDate.getMonth()+1)+"-"+bDate.getDate();
            time=bDate.getHours()+":"+bDate.getMinutes()+":"+bDate.getSeconds();
            posDate=date1;
            
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
            cmbPosCode.addItem("All");
            String sql_POS = "select strPosName,strPosCode from tblposmaster";
            ResultSet rsPOS = clsGlobalVarClass.dbMysql.executeResultSet(sql_POS);
            while (rsPOS.next()) {
                cmbPosCode.addItem(rsPOS.getString(1) + "                                         !" + rsPOS.getString(2));
            }
            rsPOS.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void funPostPOSDataToCMS(String posCode,String fromDate,String toDate)
    {
        double taxAmt=0,subTotal=0;
        try
        {
            String sql="select sum(dblgrandtotal) from tblqbillhd "
                + "where date(dteBillDate) between '"+fromDate+"' and '"+toDate+"' "
                + "and strPOSCode='"+posCode+"'";
            ResultSet rs=clsGlobalVarClass.dbMysql.executeResultSet(sql);
            if(rs.next())
            {
                System.out.println("Bill Wise= "+rs.getString(1));
            }
            rs.close();
            
            sql="select ifnull(sum(dblTaxAmount),0) from tblqbilltaxdtl "
                + "where strBillNo in (select strBillNo from tblqbillhd "
                + "where date(dteBillDate) between '"+fromDate+"' and '"+toDate+"' "
                + "and strPOSCode='"+posCode+"')";
            rs=clsGlobalVarClass.dbMysql.executeResultSet(sql);
            if(rs.next())
            {
                System.out.println("Tax = "+rs.getString(1));
                taxAmt=rs.getDouble(1);
            }
            rs.close();

            sql="select ifnull(sum(dblAmount),0) from tblqbilldtl "
                + "where strBillNo in (select strBillNo from tblqbillhd "
                + "where date(dteBillDate) between '"+fromDate+"' and '"+toDate+"' "
                + "and strPOSCode='"+posCode+"')";
            rs=clsGlobalVarClass.dbMysql.executeResultSet(sql);
            if(rs.next())
            {
                System.out.println("Item Wise= "+rs.getString(1));
                subTotal=rs.getDouble(1);
            }
            rs.close();

            sql="select sum(dblSettlementAmt) from tblqbillsettlementdtl "
                + "where strBillNo in (select strBillNo from tblqbillhd "
                + "where date(dteBillDate) between '"+fromDate+"' and '"+toDate+"' "
                + "and strPOSCode='"+posCode+"')";
            rs=clsGlobalVarClass.dbMysql.executeResultSet(sql);
            if(rs.next())
            {
                System.out.println("Settlement Amt= "+rs.getString(1));
            }
            rs.close();
            
            
            System.out.println("Final Amt="+(taxAmt+subTotal));
            if(funPostBillDataToCMS(posCode,fromDate,toDate)==1)
            {
                JOptionPane.showMessageDialog(null, "Data Posted to CMS for "+posName);
            }
            
            
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    
    
    private int funPostBillDataToCMS(String posCode,String fromDate,String toDate)
    {
        int res=0;
        double roundOff=0,creditAmt=0,debitAmt=0;
        try
        {
            JSONObject jObj=new JSONObject();
            JSONArray arrObj=new JSONArray();

            String sql_SubGroupWise="select a.strPOSCode,ifnull(d.strSubGroupCode,'NA'),ifnull(d.strSubGroupName,'NA')"
                + ",sum(b.dblAmount),date(a.dteBillDate) "
                + "from tblqbillhd a left outer join tblqbilldtl b on a.strBillNo=b.strBillNo "
                + "left outer join tblitemmaster c on b.strItemCode=c.strItemCode "
                + "left outer join tblsubgrouphd d on c.strSubGroupCode=d.strSubGroupCode "
                + "where a.strPOSCode='"+posCode+"' "
                + "and date(a.dteBillDate) between '"+fromDate+"' and '"+toDate+"' "
                + "group by d.strSubGroupCode,d.strSubGroupName";
            System.out.println(sql_SubGroupWise);
            ResultSet rsSubGroupWise=clsGlobalVarClass.dbMysql.executeResultSet(sql_SubGroupWise);
            while(rsSubGroupWise.next())
            {
                JSONObject objSubGroupWise=new JSONObject();
                objSubGroupWise.put("RVCode",rsSubGroupWise.getString(1)+"-"+rsSubGroupWise.getString(2));
                objSubGroupWise.put("RVName",posName+"-"+rsSubGroupWise.getString(3));
                objSubGroupWise.put("CRAmt",rsSubGroupWise.getDouble(4));
                objSubGroupWise.put("DRAmt",0);
                objSubGroupWise.put("ClientCode",clsGlobalVarClass.gClientCode);
                objSubGroupWise.put("BillDate",rsSubGroupWise.getString(5));
                objSubGroupWise.put("CMSPOSCode",clsGlobalVarClass.gCMSPOSCode);
                objSubGroupWise.put("POSCode",posCode);
                objSubGroupWise.put("BillDateTo",toDate);
                arrObj.add(objSubGroupWise);
                creditAmt+=rsSubGroupWise.getDouble(4);
            }
            rsSubGroupWise.close();

            String sql_TaxWise="select a.strPOSCode,c.strTaxCode,c.strTaxDesc,sum(b.dblTaxAmount),date(a.dteBillDate) "
                + "from tblqbillhd a left outer join tblqbilltaxdtl b on a.strBillNo=b.strBillNo "
                + "left outer join tbltaxhd c on b.strTaxCode=c.strTaxCode "
                + "where a.strPOSCode='"+posCode+"' "
                + "and date(a.dteBillDate) between '"+fromDate+"' and '"+toDate+"' "
                + "group by c.strTaxCode";
            System.out.println(sql_TaxWise);
            ResultSet rsTaxWise=clsGlobalVarClass.dbMysql.executeResultSet(sql_TaxWise);
            while(rsTaxWise.next())
            {
                JSONObject objTaxWise=new JSONObject();
                objTaxWise.put("RVCode",rsTaxWise.getString(1)+"-"+rsTaxWise.getString(2));
                objTaxWise.put("RVName",posName+"-"+rsTaxWise.getString(3));
                objTaxWise.put("CRAmt",rsTaxWise.getDouble(4));
                objTaxWise.put("DRAmt",0);
                objTaxWise.put("ClientCode",clsGlobalVarClass.gClientCode);
                objTaxWise.put("BillDate",rsTaxWise.getString(5));
                objTaxWise.put("CMSPOSCode",clsGlobalVarClass.gCMSPOSCode);
                objTaxWise.put("POSCode",posCode);
                objTaxWise.put("BillDateTo",toDate);
                arrObj.add(objTaxWise);
                creditAmt+=rsTaxWise.getDouble(4);
            }
            rsTaxWise.close();

            String sql_Discount="select strPOSCode,sum(dblDiscountAmt),date(dteBillDate) "
                + "from tblqbillhd "
                + "where strPOSCode='"+posCode+"' "
                + "and date(dteBillDate) between '"+fromDate+"' and '"+toDate+"' "
                + "group by strPOSCode";
            System.out.println(sql_Discount);
            ResultSet rsDiscount=clsGlobalVarClass.dbMysql.executeResultSet(sql_Discount);
            while(rsDiscount.next())
            {
                JSONObject objDiscount=new JSONObject();
                objDiscount.put("RVCode",rsDiscount.getString(1)+"-Discount");
                objDiscount.put("RVName","Discount");
                objDiscount.put("CRAmt",0);
                objDiscount.put("DRAmt",rsDiscount.getDouble(2));
                objDiscount.put("ClientCode",clsGlobalVarClass.gClientCode);
                objDiscount.put("BillDate",rsDiscount.getString(3));
                objDiscount.put("CMSPOSCode",clsGlobalVarClass.gCMSPOSCode);
                objDiscount.put("POSCode",posCode);
                objDiscount.put("BillDateTo",toDate);
                arrObj.add(objDiscount);
                creditAmt+=rsDiscount.getDouble(2);
            }
            rsDiscount.close();
                        
            
            String sql_Settlement="select a.strPOSCode,ifnull(b.strSettlementCode,'')"
                + " ,ifnull(c.strSettelmentDesc,''),ifnull(sum(b.dblSettlementAmt),0),date(a.dteBillDate) "
                + " from tblqbillhd a left outer join tblqbillsettlementdtl b on a.strBillNo=b.strBillNo "
                + " left outer join tblsettelmenthd c on b.strSettlementCode=c.strSettelmentCode "
                + " where c.strSettelmentType='Member' "
                + "and a.strPOSCode='"+posCode+"' "
                + "and date(a.dteBillDate) between '"+fromDate+"' and '"+toDate+"' "
                + " group by a.strPOSCode, b.strSettlementCode, c.strSettelmentDesc";
            System.out.println(sql_Settlement);
            ResultSet rsSettlement=clsGlobalVarClass.dbMysql.executeResultSet(sql_Settlement);
            while(rsSettlement.next())
            {
                JSONObject objSettlementWise=new JSONObject();
                objSettlementWise.put("RVCode",rsSettlement.getString(1)+"-"+rsSettlement.getString(2));
                objSettlementWise.put("RVName",posName+"-"+rsSettlement.getString(3));
                objSettlementWise.put("CRAmt",0);
                objSettlementWise.put("DRAmt",rsSettlement.getDouble(4));
                objSettlementWise.put("ClientCode",clsGlobalVarClass.gClientCode);
                objSettlementWise.put("BillDate",rsSettlement.getString(5));
                objSettlementWise.put("CMSPOSCode",clsGlobalVarClass.gCMSPOSCode);
                objSettlementWise.put("POSCode",posCode);
                objSettlementWise.put("BillDateTo",toDate);
                arrObj.add(objSettlementWise);
                debitAmt+=rsSettlement.getDouble(4);
            }
            rsSettlement.close();
            
            sql_Settlement="select a.strPOSCode,ifnull(b.strSettlementCode,'')"
                + " ,ifnull(c.strSettelmentDesc,''),ifnull(sum(b.dblSettlementAmt),0),date(a.dteBillDate) "
                + " from tblqbillhd a left outer join tblqbillsettlementdtl b on a.strBillNo=b.strBillNo "
                + " left outer join tblsettelmenthd c on b.strSettlementCode=c.strSettelmentCode "
                + " where c.strSettelmentType='Cash' and a.strPOSCode='"+posCode+"' "
                + "and date(a.dteBillDate) between '"+fromDate+"' and '"+toDate+"' "
                + " group by a.strPOSCode, b.strSettlementCode, c.strSettelmentDesc";
            System.out.println(sql_Settlement);
            rsSettlement=clsGlobalVarClass.dbMysql.executeResultSet(sql_Settlement);
            while(rsSettlement.next())
            {
                JSONObject objSettlementWise=new JSONObject();
                objSettlementWise.put("RVCode",rsSettlement.getString(1)+"-"+rsSettlement.getString(2));
                objSettlementWise.put("RVName",posName+"-"+rsSettlement.getString(3));
                objSettlementWise.put("CRAmt",0);
                objSettlementWise.put("DRAmt",rsSettlement.getDouble(4));
                objSettlementWise.put("ClientCode",clsGlobalVarClass.gClientCode);
                objSettlementWise.put("BillDate",rsSettlement.getString(5));
                objSettlementWise.put("CMSPOSCode",clsGlobalVarClass.gCMSPOSCode);
                objSettlementWise.put("POSCode",posCode);
                objSettlementWise.put("BillDateTo",toDate);
                arrObj.add(objSettlementWise);
                debitAmt+=rsSettlement.getDouble(4);
            }
            rsSettlement.close();
            
            
            JSONObject objRoundOff=new JSONObject();
            objRoundOff.put("RVCode",posCode+"-Roff");
            objRoundOff.put("RVName",posName+"-Roff");
            roundOff=debitAmt-creditAmt;
            if(roundOff<0)
            {
                roundOff=roundOff*(-1);
                objRoundOff.put("DRAmt",roundOff);
                objRoundOff.put("CRAmt",0);
            }
            else
            {
                objRoundOff.put("DRAmt",0);
                objRoundOff.put("CRAmt",roundOff);
            }                
            objRoundOff.put("ClientCode",clsGlobalVarClass.gClientCode);
            objRoundOff.put("BillDate",fromDate);
            objRoundOff.put("CMSPOSCode",clsGlobalVarClass.gCMSPOSCode);
            objRoundOff.put("POSCode",posCode);
            objRoundOff.put("BillDateTo",toDate);
            arrObj.add(objRoundOff);
            
            jObj.put("BillInfo",arrObj);
            System.out.println(jObj);            
            String cmsURL=clsGlobalVarClass.gCMSWebServiceURL+"/funPostRVDataToCMS";
            System.out.println(cmsURL);
            URL url = new URL(cmsURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            OutputStream os = conn.getOutputStream();
            os.write(jObj.toString().getBytes());
            os.flush();

            if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) 
            {
                throw new RuntimeException("Failed : HTTP error code : "
                + conn.getResponseCode());
            }
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            String output="",op="";
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) 
            {
                op+=output;
            }
            System.out.println(op);
            conn.disconnect();
            if(op.equals("false"))
            {
                res=0;
            }
            else
            {
                JSONObject jObjCL=new JSONObject();
                JSONArray arrObjCL=new JSONArray();
                /*String sql_MemberCL="select a.strCustomerCode,'',a.strBillNo,date(a.dteBillDate),b.dblSettlementAmt "
                    + "from tblqbillhd a,tblqbillsettlementdtl b where strPOSCode='"+posCode+"' "
                    + "and date(dteBillDate) between '"+fromDate+"' and '"+toDate+"' "
                    + "and strSettelmentMode='Member'";*/
                
                String sql_MemberCL="select left(a.strCustomerCode,8),d.strCustomerName,a.strBillNo,date(a.dteBillDate),b.dblSettlementAmt "
                    + "from tblqbillhd a,tblqbillsettlementdtl b,tblsettelmenthd c,tblcustomermaster d "
                    + "where a.strBillNo=b.strBillNo and b.strSettlementCode=c.strSettelmentCode "
                    + "and a.strCustomerCode=d.strCustomerCode "
                    + "and a.strPOSCode='"+posCode+"' "
                    + "and date(a.dteBillDate) between '"+fromDate+"' and '"+toDate+"' "
                    + "and c.strSettelmentType='Member'";
                System.out.println(sql_MemberCL);
                ResultSet rsMemeberCL=clsGlobalVarClass.dbMysql.executeResultSet(sql_MemberCL);
                while(rsMemeberCL.next())
                {
                    JSONObject objMemeberCL=new JSONObject();
                    objMemeberCL.put("DebtorCode",rsMemeberCL.getString(1).trim());
                    objMemeberCL.put("DebtorName",rsMemeberCL.getString(2));
                    objMemeberCL.put("BillNo",rsMemeberCL.getString(3));
                    objMemeberCL.put("BillDate",rsMemeberCL.getString(4));
                    objMemeberCL.put("BillAmt",rsMemeberCL.getDouble(5));
                    objMemeberCL.put("ClientCode",clsGlobalVarClass.gClientCode);
                    objMemeberCL.put("CMSPOSCode",clsGlobalVarClass.gCMSPOSCode);
                    objMemeberCL.put("POSCode",posCode);
                    objMemeberCL.put("POSName",posName);
                    objMemeberCL.put("BillDateTo",toDate);
                    arrObjCL.add(objMemeberCL);
                }
                rsMemeberCL.close();

                jObjCL.put("MemberCLInfo",arrObjCL);
                System.out.println(jObjCL);
                String cmsURLCL=clsGlobalVarClass.gCMSWebServiceURL+"/funPostCLDataToCMS";
                System.out.println(cmsURLCL);
                URL urlCL = new URL(cmsURLCL);
                HttpURLConnection connCL = (HttpURLConnection) urlCL.openConnection();
                connCL.setDoOutput(true);
                connCL.setRequestMethod("POST");
                connCL.setRequestProperty("Content-Type", "application/json");
                OutputStream osCL = connCL.getOutputStream();
                osCL.write(jObjCL.toString().getBytes());
                osCL.flush();

                if (connCL.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
                    throw new RuntimeException("Failed : HTTP error code : "
                    + connCL.getResponseCode());
                }
                BufferedReader brCL = new BufferedReader(new InputStreamReader((connCL.getInputStream())));
                String output1="",op1="";
                System.out.println("Output from Server .... \n");
                while ((output1 = brCL.readLine()) != null) 
                {
                    op1+=output1;
                }
                connCL.disconnect();
                System.out.println(op1);
                if(op1.equals("false"))
                {
                    res=0;
                }
                else
                {
                    res=1;
                }
            }
        }
        catch(Exception e)
        {
            res=0;
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,"Check CMS Web Service URL and Internet Connection!!!");
        }
        finally
        {
            return res;
        }
    }
     
    public int funPostSanguineCMSData(String posCode,String billDate,String fromDate,String toDate)
    {
        int res=0;
        String roundOffAccCode="";
        double roundOff=0,creditAmt=0,debitAmt=0;
        try
        {
	    String gAmount="sum(b.dblAmount)";
	    String gTaxAmount="sum(b.dblTaxAmount)";
	    String gDiscAmount="sum(a.dblDiscountAmt)";
	    String gSettlementAmount="ifnull(sum(b.dblSettlementAmt),0)";
	    if(clsGlobalVarClass.gPOSToWebBooksPostingCurrency.equalsIgnoreCase("USD"))
	    {
		gAmount="sum(b.dblAmount/a.dblUSDConverionRate)";
		gTaxAmount="sum(b.dblTaxAmount/a.dblUSDConverionRate)";
		gDiscAmount="sum(a.dblDiscountAmt/a.dblUSDConverionRate)";
		gSettlementAmount="ifnull(sum(b.dblSettlementAmt/a.dblUSDConverionRate),0)";
	    }
	    
            JSONObject jObj=new JSONObject();

            String sql_SubGroupWise="select a.strPOSCode,ifnull(d.strSubGroupCode,'NA'),ifnull(d.strSubGroupName,'NA')"
                + ","+gAmount+",date(a.dteBillDate),d.strAccountCode "
                + "from tblqbillhd a left outer join tblqbilldtl b on a.strBillNo=b.strBillNo "
                + "left outer join tblitemmaster c on b.strItemCode=c.strItemCode "
                + "left outer join tblsubgrouphd d on c.strSubGroupCode=d.strSubGroupCode "
                + "where a.strPOSCode='"+posCode+"' "
                + "and date(a.dteBillDate) between '"+fromDate+"' and '"+toDate+"' "     
                + "group by d.strSubGroupCode,d.strSubGroupName";

            JSONArray arrObjSubGroupwise=new JSONArray();
            ResultSet rsSubGroupWise=clsGlobalVarClass.dbMysql.executeResultSet(sql_SubGroupWise);
            while(rsSubGroupWise.next())
            {
                JSONObject objSubGroupWise=new JSONObject();
                creditAmt+= rsSubGroupWise.getDouble(4);
                objSubGroupWise.put("RVCode",rsSubGroupWise.getString(1)+"-"+rsSubGroupWise.getString(2));
                objSubGroupWise.put("RVName",clsGlobalVarClass.gPOSName+"-"+rsSubGroupWise.getString(3));
                objSubGroupWise.put("CRAmt",rsSubGroupWise.getDouble(4));
                objSubGroupWise.put("DRAmt",0);
                objSubGroupWise.put("ClientCode",clsGlobalVarClass.gClientCode);
                objSubGroupWise.put("BillDate",rsSubGroupWise.getString(5));
                objSubGroupWise.put("CMSPOSCode",clsGlobalVarClass.gCMSPOSCode);
                objSubGroupWise.put("POSCode",posCode);
                objSubGroupWise.put("BillDateTo",rsSubGroupWise.getString(5));
                objSubGroupWise.put("AccountCode",rsSubGroupWise.getString(6));
                arrObjSubGroupwise.add(objSubGroupWise);
            }
            rsSubGroupWise.close();
            jObj.put("SubGroupwise", arrObjSubGroupwise);


            String sql_TaxWise="select a.strPOSCode,c.strTaxCode,c.strTaxDesc,"+gTaxAmount+",date(a.dteBillDate),c.strAccountCode "
                + "from tblqbillhd a left outer join tblqbilltaxdtl b on a.strBillNo=b.strBillNo "
                + "left outer join tbltaxhd c on b.strTaxCode=c.strTaxCode "
                + "where a.strPOSCode='"+posCode+"' "
                + "and date(a.dteBillDate) between '"+fromDate+"' and '"+toDate+"' "       
                + "group by c.strTaxCode";

            JSONArray arrObjTaxwise=new JSONArray();
            ResultSet rsTaxWise=clsGlobalVarClass.dbMysql.executeResultSet(sql_TaxWise);
            while(rsTaxWise.next())
            {
                JSONObject objTaxWise=new JSONObject();
                creditAmt+= rsTaxWise.getDouble(4);
                objTaxWise.put("RVCode",rsTaxWise.getString(1)+"-"+rsTaxWise.getString(2));
                objTaxWise.put("RVName",clsGlobalVarClass.gPOSName+"-"+rsTaxWise.getString(3));
                objTaxWise.put("CRAmt",rsTaxWise.getDouble(4));
                objTaxWise.put("DRAmt",0);
                objTaxWise.put("ClientCode",clsGlobalVarClass.gClientCode);
                objTaxWise.put("BillDate",rsTaxWise.getString(5));
                objTaxWise.put("CMSPOSCode",clsGlobalVarClass.gCMSPOSCode);
                objTaxWise.put("POSCode",posCode);
                objTaxWise.put("BillDateTo",rsTaxWise.getString(5));
                objTaxWise.put("AccountCode",rsTaxWise.getString(6));
                arrObjTaxwise.add(objTaxWise);
            }
            rsTaxWise.close();
            jObj.put("Taxwise", arrObjTaxwise);

            String sql_Discount="select a.strPOSCode,"+gDiscAmount+",date(a.dteBillDate),b.strRoundOff,b.strTip,b.strDiscount "
                + "from tblqbillhd a,tblposmaster b "
                + "where a.strPOSCode='"+posCode+"' "
                + "and date(a.dteBillDate) between '"+fromDate+"' and '"+toDate+"' "   
                + "group by a.strPOSCode";

            JSONArray arrObjDiscountwise=new JSONArray();
            ResultSet rsDiscount=clsGlobalVarClass.dbMysql.executeResultSet(sql_Discount);
            while(rsDiscount.next())
            {
                JSONObject objDiscount=new JSONObject();
                debitAmt+= rsDiscount.getDouble(2);
                roundOffAccCode=rsDiscount.getString(4);
                objDiscount.put("RVCode",rsDiscount.getString(1)+"-Discount");
                objDiscount.put("RVName","Discount");
                objDiscount.put("CRAmt",0);
                objDiscount.put("DRAmt",rsDiscount.getDouble(2));
                objDiscount.put("ClientCode",clsGlobalVarClass.gClientCode);
                objDiscount.put("BillDate",rsDiscount.getString(3));
                objDiscount.put("CMSPOSCode",clsGlobalVarClass.gCMSPOSCode);
                objDiscount.put("POSCode",posCode);
                objDiscount.put("BillDateTo",rsDiscount.getString(3));
                objDiscount.put("AccountCode",rsDiscount.getString(6));
                arrObjDiscountwise.add(objDiscount);
            }
            rsDiscount.close();
            jObj.put("Discountwise", arrObjDiscountwise);

            String sql_Settlement="select a.strPOSCode,ifnull(b.strSettlementCode,'')"
                + " ,ifnull(c.strSettelmentDesc,''),"+gSettlementAmount+",date(a.dteBillDate),c.strAccountCode "
                + " from tblqbillhd a left outer join tblqbillsettlementdtl b on a.strBillNo=b.strBillNo "
                + " left outer join tblsettelmenthd c on b.strSettlementCode=c.strSettelmentCode "
                + " where c.strSettelmentType='Member' and a.strPOSCode='"+posCode+"'  "
                + "and date(a.dteBillDate) between '"+fromDate+"' and '"+toDate+"' "   
                + " group by a.strPOSCode, b.strSettlementCode, c.strSettelmentDesc";
            JSONArray arrObjMemberSettlewise=new JSONArray();
            ResultSet rsCashSettlement=clsGlobalVarClass.dbMysql.executeResultSet(sql_Settlement);
            while(rsCashSettlement.next())
            {
                JSONObject objSettlementWise=new JSONObject();
                debitAmt+= rsCashSettlement.getDouble(4);
                objSettlementWise.put("RVCode",rsCashSettlement.getString(1)+"-"+rsCashSettlement.getString(2));
                objSettlementWise.put("RVName",clsGlobalVarClass.gPOSName+"-"+rsCashSettlement.getString(3));
                objSettlementWise.put("CRAmt",0);
                objSettlementWise.put("DRAmt",rsCashSettlement.getDouble(4));
                objSettlementWise.put("ClientCode",clsGlobalVarClass.gClientCode);
                objSettlementWise.put("BillDate",rsCashSettlement.getString(5));
                objSettlementWise.put("CMSPOSCode",clsGlobalVarClass.gCMSPOSCode);
                objSettlementWise.put("POSCode",posCode);
                objSettlementWise.put("BillDateTo",rsCashSettlement.getString(5));
                objSettlementWise.put("AccountCode",rsCashSettlement.getString(6));
                arrObjMemberSettlewise.add(objSettlementWise);
            }
            rsCashSettlement.close();
            jObj.put("MemberSettlewise", arrObjMemberSettlewise);

            sql_Settlement="select a.strPOSCode,ifnull(b.strSettlementCode,'')"
                + " ,ifnull(c.strSettelmentDesc,''),"+gSettlementAmount+",date(a.dteBillDate),c.strAccountCode  "
                + " from tblqbillhd a left outer join tblqbillsettlementdtl b on a.strBillNo=b.strBillNo "
                + " left outer join tblsettelmenthd c on b.strSettlementCode=c.strSettelmentCode "
                + " where c.strSettelmentType='Cash' and a.strPOSCode='"+posCode+"'  "
                + "and date(a.dteBillDate) between '"+fromDate+"' and '"+toDate+"' "   
                + " group by a.strPOSCode, b.strSettlementCode, c.strSettelmentDesc";
            JSONArray arrObjCashSettlewise=new JSONArray();
            ResultSet rsMemberSettlement=clsGlobalVarClass.dbMysql.executeResultSet(sql_Settlement);
            while(rsMemberSettlement.next())
            {
                JSONObject objSettlementWise=new JSONObject();
                debitAmt+= rsMemberSettlement.getDouble(4);
                objSettlementWise.put("RVCode",rsMemberSettlement.getString(1)+"-"+rsMemberSettlement.getString(2));
                objSettlementWise.put("RVName",clsGlobalVarClass.gPOSName+"-"+rsMemberSettlement.getString(3));
                objSettlementWise.put("CRAmt",0);
                objSettlementWise.put("DRAmt",rsMemberSettlement.getDouble(4));
                objSettlementWise.put("ClientCode",clsGlobalVarClass.gClientCode);
                objSettlementWise.put("BillDate",rsMemberSettlement.getString(5));
                objSettlementWise.put("CMSPOSCode",clsGlobalVarClass.gCMSPOSCode);
                objSettlementWise.put("POSCode",posCode);
                objSettlementWise.put("BillDateTo",rsMemberSettlement.getString(5));
                objSettlementWise.put("AccountCode",rsMemberSettlement.getString(6));
                arrObjCashSettlewise.add(objSettlementWise);
            }
            rsMemberSettlement.close();
            jObj.put("CashSettlewise", arrObjCashSettlewise);

            String sql_MemberCL="select left(a.strCustomerCode,8),d.strCustomerName,a.strBillNo,date(a.dteBillDate),"+gSettlementAmount+",c.strAccountCode "
                    + "from tblqbillhd a,tblqbillsettlementdtl b,tblsettelmenthd c,tblcustomermaster d "
                    + "where a.strBillNo=b.strBillNo and b.strSettlementCode=c.strSettelmentCode "
                    + "and a.strCustomerCode=d.strCustomerCode "
                    + "and a.strPOSCode='"+posCode+"'  "
                    + "and date(a.dteBillDate) between '"+fromDate+"' and '"+toDate+"' "   
                    + "and c.strSettelmentType='Member'";
            JSONArray arrObjMemberClData=new JSONArray();
            ResultSet rsMemeberCL=clsGlobalVarClass.dbMysql.executeResultSet(sql_MemberCL);
            while(rsMemeberCL.next())
            {
                JSONObject objMemeberCL=new JSONObject();
                objMemeberCL.put("DebtorCode",rsMemeberCL.getString(1).trim());
                objMemeberCL.put("DebtorName",rsMemeberCL.getString(2));
                objMemeberCL.put("BillNo",rsMemeberCL.getString(3));
                objMemeberCL.put("BillDate",rsMemeberCL.getString(4));
                objMemeberCL.put("BillAmt",rsMemeberCL.getDouble(5));
                objMemeberCL.put("ClientCode",clsGlobalVarClass.gClientCode);
                objMemeberCL.put("CMSPOSCode",clsGlobalVarClass.gCMSPOSCode);
                objMemeberCL.put("POSCode",posCode);
                objMemeberCL.put("POSName",clsGlobalVarClass.gPOSName);
                objMemeberCL.put("BillDateTo",rsMemeberCL.getString(4));
                objMemeberCL.put("AccountCode",rsMemeberCL.getString(6));
                arrObjMemberClData.add(objMemeberCL);
            }
            rsMemeberCL.close();
            jObj.put("MemberCLData", arrObjMemberClData);
            String posDate=billDate;
            JSONArray arrObjRoundOff=new JSONArray();
            JSONObject objRoundOff=new JSONObject();
            objRoundOff.put("RVCode",clsGlobalVarClass.gPOSCode+"-Roff");
            objRoundOff.put("RVName",clsGlobalVarClass.gPOSName+"-Roff");
            roundOff=debitAmt-creditAmt;
            if(roundOff<0)
            {
                roundOff=roundOff*(-1);
                objRoundOff.put("DRAmt",roundOff);
                objRoundOff.put("CRAmt",0);
            }
            else
            {
                objRoundOff.put("DRAmt",0);
                objRoundOff.put("CRAmt",roundOff);
            }                
            objRoundOff.put("ClientCode",clsGlobalVarClass.gClientCode);
            objRoundOff.put("BillDate",posDate);
            objRoundOff.put("CMSPOSCode",clsGlobalVarClass.gCMSPOSCode);
            objRoundOff.put("POSCode",posCode);
            objRoundOff.put("BillDateTo",posDate);
            objRoundOff.put("AccountCode",roundOffAccCode);
            arrObjRoundOff.add(objRoundOff);

            jObj.put("RoundOffDtl",arrObjRoundOff);
            System.out.println(jObj);

            String cmsURL=clsGlobalVarClass.gWebBooksWebServiceURL+"/funPostRevenueToCMS";
            System.out.println(cmsURL);
            URL url = new URL(cmsURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            OutputStream os = conn.getOutputStream();
            os.write(jObj.toString().getBytes());
            os.flush();

            if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) 
            {
                throw new RuntimeException("Failed : HTTP error code : "
                + conn.getResponseCode());
            }
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            String output="",op="";
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) 
            {
                op+=output;
            }
            System.out.println(op);
            conn.disconnect();
            if(op.equals("false"))
            {
                res=0;
            }
            else
            {
                res=1;
            }
        }
        catch(Exception e)
        {
            res=0;
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,"Check CMS Web Service URL and Internet Connection!!!"); // there is this at null postion
        }
        finally
        {
            return res;
        }
    }
    
    
    
    private int funInsertQbillData(String posCode,String fromDate,String toDate)
    {
        try
        {
            String qSql="delete from tblqbilldtl where strClientCode='"+clsGlobalVarClass.gClientCode+"' "
                + "and strBillNo in (select strBillNo from tblqbillhd "
                + "where strPOSCode = '"+posCode+"' "
                + "and date(dteBillDate) between '"+fromDate+"' and '"+toDate+"') ";
            //clsGlobalVarClass.dbMysql.execute(qSql);
            
            qSql="insert into tblqbilldtl (select * from tblbilldtl "
                + "where strClientCode='"+clsGlobalVarClass.gClientCode+"' "
                + "and strBillNo in (select strBillNo from vqbillhd "
                + "where strPOSCode = '"+posCode+"' "
                + "and date(dteBillDate) between '"+fromDate+"' and '"+toDate+"'"
                + " ))";
            System.out.println("BillDtl= "+clsGlobalVarClass.dbMysql.execute(qSql));
            
            qSql="delete from tblbilldtl where strClientCode='"+clsGlobalVarClass.gClientCode+"' "
                + "and strBillNo in (select strBillNo from vqbillhd "
                + "where strPOSCode = '"+posCode+"' "
                + "and date(dteBillDate) between '"+fromDate+"' and '"+toDate+"') ";
            System.out.println("BillDtl delete= "+clsGlobalVarClass.dbMysql.execute(qSql));
            
        
        // settlementdtl
            qSql="delete from tblqbillsettlementdtl where strClientCode='"+clsGlobalVarClass.gClientCode+"' "
                + "and strBillNo in (select strBillNo from vqbillhd "
                + "where strPOSCode = '"+posCode+"' "
                + "and date(dteBillDate) between '"+fromDate+"' and '"+toDate+"') ";
            //clsGlobalVarClass.dbMysql.execute(qSql);
            
            qSql="insert into tblqbillsettlementdtl (select * from tblbillsettlementdtl "
                + "where strClientCode='"+clsGlobalVarClass.gClientCode+"' "
                + "and strBillNo in (select strBillNo from vqbillhd "
                + "where strPOSCode = '"+posCode+"' "
                + "and date(dteBillDate) between '"+fromDate+"' and '"+toDate+"'"
                + " ))";
            clsGlobalVarClass.dbMysql.execute(qSql);
            
            qSql="delete from tblbillsettlementdtl where strClientCode='"+clsGlobalVarClass.gClientCode+"' "
                + "and strBillNo in (select strBillNo from vqbillhd "
                + "where strPOSCode = '"+posCode+"' "
                + "and date(dteBillDate) between '"+fromDate+"' and '"+toDate+"') ";
            clsGlobalVarClass.dbMysql.execute(qSql);
            
        
        // taxdtl    
            qSql="delete from tblqbilltaxdtl where strClientCode='"+clsGlobalVarClass.gClientCode+"' "
                + "and strBillNo in (select strBillNo from tblqbillhd "
                + "where strPOSCode = '"+posCode+"' "
                + "and date(dteBillDate) between '"+fromDate+"' and '"+toDate+"') ";
            //clsGlobalVarClass.dbMysql.execute(qSql);
            
            qSql="insert into tblqbilltaxdtl (select * from tblbilltaxdtl "
                + "where strClientCode='"+clsGlobalVarClass.gClientCode+"' "
                + "and strBillNo in (select strBillNo from vqbillhd "
                + "where strPOSCode = '"+posCode+"' "
                + "and date(dteBillDate) between '"+fromDate+"' and '"+toDate+"'"
                + " ))";
            clsGlobalVarClass.dbMysql.execute(qSql);
            
            qSql="delete from tblbilltaxdtl where strClientCode='"+clsGlobalVarClass.gClientCode+"' "
                + "and strBillNo in (select strBillNo from vqbillhd "
                + "where strPOSCode = '"+posCode+"' "
                + "and date(dteBillDate) between '"+fromDate+"' and '"+toDate+"') ";
            clsGlobalVarClass.dbMysql.execute(qSql);
            
            
            
        // billhd    
            qSql="delete from tblqbillhd where strClientCode='"+clsGlobalVarClass.gClientCode+"' "
                + "and strPOSCode = '"+posCode+"' "
                + "and date(dteBillDate) between '"+fromDate+"' and '"+toDate+"') ";
            //clsGlobalVarClass.dbMysql.execute(qSql);
            
            qSql="insert into tblqbillhd (select * from tblbillhd "
                + "where strClientCode='"+clsGlobalVarClass.gClientCode+"' "
                + "and strBillNo in (select strBillNo from vqbillhd "
                + "where strPOSCode = '"+posCode+"' "
                + "and date(dteBillDate) between '"+fromDate+"' and '"+toDate+"'"
                + " ))";
            clsGlobalVarClass.dbMysql.execute(qSql);
            
            qSql="delete from tblbillhd where strClientCode='"+clsGlobalVarClass.gClientCode+"' "
                + "and strBillNo in (select strBillNo from vqbillhd "
                + "where strPOSCode = '"+posCode+"' "
                + "and date(dteBillDate) between '"+fromDate+"' and '"+toDate+"') ";
            clsGlobalVarClass.dbMysql.execute(qSql);
            
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return 1;
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
        btnBack = new javax.swing.JButton();
        btnGenerateTax = new javax.swing.JButton();
        disReportName = new javax.swing.JLabel();
        dteFromDate = new com.toedter.calendar.JDateChooser();
        lblToDate = new javax.swing.JLabel();
        lblFromDate = new javax.swing.JLabel();

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
        lblformName.setText("- Post POS Data To CMS");
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
        lblPOSName.setText("POS Name       :");

        btnBack.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnBack.setForeground(new java.awt.Color(255, 255, 255));
        btnBack.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnBack.setText("CLOSE");
        btnBack.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnBack.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnBack.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnBackMouseClicked(evt);
            }
        });
        btnBack.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnBackActionPerformed(evt);
            }
        });

        btnGenerateTax.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnGenerateTax.setForeground(new java.awt.Color(255, 255, 255));
        btnGenerateTax.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnGenerateTax.setText("<html>Post Data<br> to CMS</html>");
        btnGenerateTax.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnGenerateTax.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnGenerateTax.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnGenerateTaxMouseClicked(evt);
            }
        });
        btnGenerateTax.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnGenerateTaxActionPerformed(evt);
            }
        });

        disReportName.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        disReportName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        disReportName.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        lblToDate.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblToDate.setText("To Date          :");

        lblFromDate.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblFromDate.setText("From Date       :");

        javax.swing.GroupLayout panelFormBodyLayout = new javax.swing.GroupLayout(panelFormBody);
        panelFormBody.setLayout(panelFormBodyLayout);
        panelFormBodyLayout.setHorizontalGroup(
            panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFormBodyLayout.createSequentialGroup()
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addGap(311, 311, 311)
                        .addComponent(lblFormName, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(disReportName, javax.swing.GroupLayout.PREFERRED_SIZE, 760, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panelFormBodyLayout.createSequentialGroup()
                                .addGap(209, 209, 209)
                                .addComponent(btnGenerateTax, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(57, 57, 57)
                                .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(0, 16, Short.MAX_VALUE))
            .addGroup(panelFormBodyLayout.createSequentialGroup()
                .addGap(221, 221, 221)
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelFormBodyLayout.createSequentialGroup()
                                .addComponent(lblPOSName, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(9, 9, 9)
                                .addComponent(cmbPosCode, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lblFromDate, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addComponent(lblToDate, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dteToDate, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dteFromDate, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelFormBodyLayout.setVerticalGroup(
            panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFormBodyLayout.createSequentialGroup()
                .addComponent(disReportName, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblFormName)
                .addGap(35, 35, 35)
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblPOSName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbPosCode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(36, 36, 36)
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(dteFromDate, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblFromDate, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dteToDate, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblToDate, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(153, 153, 153)
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnGenerateTax, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(123, 123, 123))
        );

        panelMainForm.add(panelFormBody, new java.awt.GridBagConstraints());

        getContentPane().add(panelMainForm, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBackMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBackMouseClicked
        // TODO add your handling code here:
        dispose();
        clsGlobalVarClass.hmActiveForms.remove("Post POS Data To CMS");
    }//GEN-LAST:event_btnBackMouseClicked

    private void btnGenerateTaxMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGenerateTaxMouseClicked
        
        String fromDate=(dteFromDate.getDate().getYear()+1900)+"-"+(dteFromDate.getDate().getMonth()+1)+"-"+dteFromDate.getDate().getDate();
        String toDate=(dteToDate.getDate().getYear()+1900)+"-"+(dteToDate.getDate().getMonth()+1)+"-"+dteToDate.getDate().getDate();
        String posCode=cmbPosCode.getSelectedItem().toString().split("!")[1];
        posName=cmbPosCode.getSelectedItem().toString().split("!")[0].trim();
        posDate=posDate+" "+time;
        String fDate=fromDate+" "+time;
      
        if(objUtility.funCompareTime(posDate,fDate)<0)
        {
            //funInsertQbillData(posCode, fromDate, toDate);
            if(clsGlobalVarClass.gCMSIntegrationYN)
            {
                if(clsGlobalVarClass.gCMSPostingType.equals("Sanguine CMS"))
                {
                    funPostSanguineCMSData(posCode,posDate,fromDate,toDate);
                }
                else
                {
                // Post Sales Data to CMS CL and RV Tables. 
                    funPostPOSDataToCMS(posCode,fromDate,toDate);
                }
            }
        }
        else
        {
            JOptionPane.showMessageDialog(null,"Please select valid date");
        }
        //dispose();
    }//GEN-LAST:event_btnGenerateTaxMouseClicked

    private void btnGenerateTaxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerateTaxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnGenerateTaxActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnBackActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosed
    {//GEN-HEADEREND:event_formWindowClosed
        clsGlobalVarClass.hmActiveForms.remove("Post POS Data To CMS");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosing
    {//GEN-HEADEREND:event_formWindowClosing
        clsGlobalVarClass.hmActiveForms.remove("Post POS Data To CMS");
    }//GEN-LAST:event_formWindowClosing

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(frmPostPOSDataToCMS.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmPostPOSDataToCMS.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmPostPOSDataToCMS.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmPostPOSDataToCMS.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmPostPOSDataToCMS().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnGenerateTax;
    private javax.swing.JComboBox cmbPosCode;
    private javax.swing.JLabel disReportName;
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
    private javax.swing.JLabel lblToDate;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblformName;
    private javax.swing.JPanel panelFormBody;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelMainForm;
    // End of variables declaration//GEN-END:variables
}
