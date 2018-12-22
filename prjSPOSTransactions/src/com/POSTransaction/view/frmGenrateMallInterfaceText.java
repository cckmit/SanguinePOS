/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSTransaction.view;

import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.view.frmOkCancelPopUp;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import org.apache.commons.net.ftp.FTPClient;

public class frmGenrateMallInterfaceText extends javax.swing.JFrame
{

    private String fileName, sql, fromDate, toDate, reportType = "", textfileName, filePath;
    private static final int BUFFER_SIZE = 4096;

    public frmGenrateMallInterfaceText()
    {
        initComponents();
        try
        {
            java.util.Date objDate = new java.util.Date();
            String currDate = objDate.getDate() + "-" + (objDate.getMonth() + 1) + "-" + (objDate.getYear() + 1900);
            java.util.Date date;
            date = new SimpleDateFormat("dd-MM-yyyy").parse(clsGlobalVarClass.gPOSDateToDisplay);
            dteFromDate.setDate(date);
            dteToDate.setDate(date);

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void funResetFields()
    {
        try
        {
            java.util.Date objDate = new java.util.Date();
            String currDate = objDate.getDate() + "-" + (objDate.getMonth() + 1) + "-" + (objDate.getYear() + 1900);
            java.util.Date date;
            date = new SimpleDateFormat("dd-MM-yyyy").parse(clsGlobalVarClass.gPOSDateToDisplay);
            dteFromDate.setDate(date);
            dteToDate.setDate(date);
            rdbOldDate.setSelected(false);
            rdbCurrent.setSelected(false);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public int funWriteToFile(String fromDate, String toDate, String reportType, String dayEndYN)
    {
        try
        {
            long lastNo = 0;
            String receiprNo = "";
            String receiprNoOpening = "";
            String receiprNoClosing = "";
            String billHdName = "tblbillhd";
            String billDtlName = "tblbilldtl";
            String billsettlementdtl ="tblbillsettlementdtl";
            if (reportType.equals("Old"))
            {
                billHdName = "tblqbillhd";
                billDtlName = "tblqbilldtl";
                billsettlementdtl = "tblqbillsettlementdtl";

            }

            frmOkCancelPopUp okOb = new frmOkCancelPopUp(null, "Do you want Generate Mall Interface");
            okOb.setVisible(true);
            int res = okOb.getResult();
            if (res == 1)
            {

                if (reportType.equals("Current"))
                {
                    if (dayEndYN.equals("Y"))
                    {
                        sql = "select strOpeningReceipt from tblmallinterface where date(dteClosingDate) ='" + toDate + "' ; ";
                        ResultSet rsCheckCurrentReceipt = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                        if (rsCheckCurrentReceipt.next())
                        {
                            receiprNoClosing = rsCheckCurrentReceipt.getString(1);
                            int len = receiprNoClosing.length();
                            String repNo = receiprNoClosing.split("R")[1];
                            int num = Integer.parseInt(repNo);
                            num = num - 1;
                            receiprNoOpening = "R" + num;
//                             sql="Insert into tblmallinterface values('"+receiprNoClosing+"','"+clsGlobalVarClass.gPOSCode+"','"+toDate+"') ";
//                             clsGlobalVarClass.dbMysql.execute(sql);

                        }
                        else
                        {
                            lastNo = funGenMIReciptCode();
                            receiprNoClosing = "R" + lastNo;
                            receiprNoOpening = "R" + (lastNo - 1);
                            System.out.println("receiptNo=" + receiprNo);
                            sql = "Insert into tblmallinterface values('" + receiprNoClosing + "','" + clsGlobalVarClass.gPOSCode + "','" + toDate + "') ";
                            clsGlobalVarClass.dbMysql.execute(sql);
                        }

                    }
                    else
                    {
                        sql = "select strOpeningReceipt from tblmallinterface where date(dteClosingDate) ='" + toDate + "' ; ";
                        ResultSet rsCheckCurrentReceipt = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                        if (rsCheckCurrentReceipt.next())
                        {
                            receiprNoClosing = rsCheckCurrentReceipt.getString(1);
                            int len = receiprNoClosing.length();
                            String repNo = receiprNoClosing.split("R")[1];
                            int num = Integer.parseInt(repNo);
                            num = num - 1;
                            receiprNoOpening = "R" + num;

   //                             sql="Insert into tblmallinterface values('"+receiprNoClosing+"','"+clsGlobalVarClass.gPOSCode+"','"+toDate+"') ";
                            //                             clsGlobalVarClass.dbMysql.execute(sql);
                        }
                        else
                        {
                            lastNo = funGenMIReciptCode();
                            receiprNoClosing = "R" + lastNo;
                            receiprNoOpening = "R" + (lastNo - 1);
                            System.out.println("receiptNo=" + receiprNo);
                            sql = "Insert into tblmallinterface values('" + receiprNoClosing + "','" + clsGlobalVarClass.gPOSCode + "','" + toDate + "') ";
                            clsGlobalVarClass.dbMysql.execute(sql);
                        }

                    }

                }
                else
                {
                    sql = "select min(a.strOpeningReceipt),max(a.strOpeningReceipt) from tblmallinterface a where date(a.dteClosingDate) between '" + fromDate + "' and '" + toDate + "'  ";
                    ResultSet rsBackReceipt = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                    if (rsBackReceipt.next())
                    {
                        receiprNoOpening = rsBackReceipt.getString(1);
                        receiprNoClosing = rsBackReceipt.getString(2);

                        int len = receiprNoOpening.length();
                        String repNo = receiprNoOpening.split("R")[1];
                        int num = Integer.parseInt(repNo);
                        num = num - 1;
                        receiprNoOpening = "R" + num;

                    }

                }

           // String delBoyCategoryCode = "CA" + String.format("%03d", lastNo);
                ArrayList<String> listBillNo = new ArrayList();
                ArrayList<String> listCustInfoWriteText = new ArrayList();
                String TENANTID = "CRYLEAS00010615";
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                String fileTime = sdf.format(cal.getTime());
                System.out.println(sdf.format(cal.getTime()));
                fileTime = fileTime.replaceAll(":", "");
                SimpleDateFormat sdate = new SimpleDateFormat("YY-MM-dd");
                Date TodayDate =new Date();
                String fileDate = sdate.format(TodayDate);
                System.out.println(fileDate);
                fileDate = fileDate.replaceAll("-", "");;
                String cot = "'";
                String pipe = "|";
                int reciNo=1;
                String tranNumberSql="select dblLastNo from tblinternal where strTransactionType='MIReceiptNo'";
                ResultSet rs_tranNumberSql=clsGlobalVarClass.dbMysql.executeResultSet(tranNumberSql);
                if(rs_tranNumberSql.next())
                {
                    reciNo=rs_tranNumberSql.getInt(1);
                }
                if(reciNo>9999)
                {
                 reciNo=reciNo%10000;
                }
                String filePath = System.getProperty("user.dir");
                textfileName = "t" + TENANTID + "_" + clsGlobalVarClass.gPOSCode + "_"+reciNo+"_" + fileDate + fileTime + ".txt";
                filePath = filePath + "/Temp/" + textfileName;
                PrintWriter pw = new PrintWriter(filePath, "UTF-8");

                File file = new File(filePath);
                if (!file.exists())
                {
                    file.createNewFile();
                }

                String writeText = "", isCustomerCode = "", disCode = "", billno = "", settlemode = "", posCode = "", date = "", printDate = "", time = "", usercode = "", tableNo = "";
                double totAmt = 0.00, totDisAmt = 0.00, disper = 0.00, totTaxAmt = 0.00, grandTot = 0.00,taxPercent=0.00,onlyTotTax=0.00;
                int shiftCode = 1, intPaxNo = 0;
                
                sql = " select date(a.dteBillDate),Time(a.dteBillDate),a.strUserCreated from " + billHdName + " a "
                        + " where date(a.dteBillDate) BETWEEN '" + fromDate + "' and '" + toDate + "' and a.strPOSCode='" + clsGlobalVarClass.gPOSCode + "' ";
                ResultSet rsHerder = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                if (rsHerder.next())
                {
                    posCode = clsGlobalVarClass.gPOSCode;
                    date = rsHerder.getString(1);
                    time = rsHerder.getString(2);
                    usercode = rsHerder.getString(3);
                    if(usercode.length()>8)
                    {
                      usercode=usercode.substring(0, 8);
                    }
                    printDate = date.replaceAll("-", "");

                   

                    writeText = "1'OPENED'" + TENANTID + "'" + posCode + "'" + receiprNoOpening + "'" + reciNo + "'" + printDate + "'" + time + "'" + usercode + "'" + printDate + "";
                    writeText = writeText.replaceAll(cot, pipe);
                    pw.println(writeText);
                    System.out.println("With | Cot");
                    System.out.println(writeText);
                }
                rsHerder.close();

                sql = "select strBillNo ,strCustomerCode,date(dteBillDate) from  " + billHdName + " where strPosCode='" + clsGlobalVarClass.gPOSCode + "' "
                        + " and date(dteBillDate) BETWEEN '" + fromDate + "' and '" + toDate + "' ";
                ResultSet rsTransectionCount = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                while (rsTransectionCount.next())
                {
                    totDisAmt=0.00;
                    onlyTotTax=0.00;
                    totAmt=0.00;
                    String billDisCode="";
                    billno = rsTransectionCount.getString(1);
                    listBillNo.add(billno);
                    isCustomerCode = rsTransectionCount.getString(2);
                    printDate = rsTransectionCount.getString(3);
                    printDate = date.replaceAll("-", "");
                    sql = "select intShiftCode from tbldayendprocess where strPOSCode='" + posCode + "' and strDayEnd='N' and strShiftEnd='N' ";
                    ResultSet rsshiftCode = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                    if (rsshiftCode.next())
                    {
                        shiftCode = rsshiftCode.getInt(1);
                    }
                    rsshiftCode.close();
                    sql = "select strTableNo,intPaxNo,strReasonCode from " + billHdName + " where strBillNo='" + billno + "' and strPosCode='" + posCode + "' "
                            + " and date(dteBillDate) BETWEEN '" + fromDate + "' and '" + toDate + "'";
                    ResultSet rsTableNo = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                    if (rsTableNo.next())
                    {
                        tableNo = rsTableNo.getString(1);
                        intPaxNo = rsTableNo.getInt(2);
                        billDisCode = rsTableNo.getString(3);
                        if(!billDisCode.isEmpty())
                        {
                            billDisCode=billDisCode.substring(1, 3);
                        }
                        
                    }
                    rsTableNo.close();
                    
                    writeText = "101'" + billno + "'" + shiftCode + "'" + printDate + "'" + time + "'" + usercode + "'''"+billDisCode+"'" + usercode + "'" + tableNo + "'" + intPaxNo + "'N'SALE";
                    writeText = writeText.replaceAll(cot, pipe);
                    if (isCustomerCode.length() > 0)
                    {
                        listCustInfoWriteText.add(writeText);
                    }

                    pw.println(writeText);
                    
                    String taxSql="select dblPercent from tbltaxhd where strClientCode='"+clsGlobalVarClass.gClientCode+"' ";
                    ResultSet rsTaxSql=clsGlobalVarClass.dbMysql.executeResultSet(taxSql);
                    if(rsTaxSql.next())
                    {
                        taxPercent=rsTaxSql.getDouble(1);
                    }
                    rsTaxSql.close();
                    
                    
                    //writeText+="\n\r";
                    sql = "select '111',a.strItemCode,a.dblQuantity,a.dblRate,a.dblRate,'', c.strTaxIndicator,b.strReasonCode, "
                            + " a.dblDiscountAmt,a.strItemCode,'','','N', a.dblAmount ,a.dblDiscountAmt ,'%','0.00',a.strItemCode,"
                            + " b.dblDiscountPer,b.dblTaxAmt ,b.strSettelmentMode,b.dblGrandTotal "
                            + " from " + billDtlName + " a, " + billHdName + " b ,tblitemmaster c "
                            + " where a.strBillNo=b.strBillNo and a.strItemCode=c.strItemCode and  "
                            + " b.strBillNo='" + billno + "' and b.strPosCode='" + posCode + "' "
                            + " and date(b.dteBillDate) BETWEEN '" + fromDate + "' and '" + toDate + "' ";
                    ResultSet rsTransectionData = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                    while (rsTransectionData.next())
                    {
                        disCode = rsTransectionData.getString(8);
                        if(!disCode.isEmpty())
                        {
                            disCode=disCode.substring(1, 3);
                        }
                        double actualItemAmt=rsTransectionData.getDouble(14);
                        //double disamt=rsTransectionData.getDouble(15);
                        double amtTax=0.00;
//                        actualItemAmt=actualItemAmt-disamt;
                        amtTax=(actualItemAmt/100)*taxPercent;
                         DecimalFormat objdecmal = new DecimalFormat("#.##");
                         amtTax = Double.parseDouble(objdecmal.format(amtTax));
//                        actualItemAmt=actualItemAmt-amtTax;
                        writeText = "" + rsTransectionData.getString(1) + "'" + rsTransectionData.getString(2) + "'" + rsTransectionData.getString(3) + "'"
                                + "" + rsTransectionData.getString(4) + "'" + rsTransectionData.getString(5) + "'" + rsTransectionData.getString(6) + "'"
                                + "" + rsTransectionData.getString(7) + "'" + disCode + "'" + rsTransectionData.getString(9) + "'"
                                + "" + rsTransectionData.getString(10) + "'" + rsTransectionData.getString(11) + "'" + rsTransectionData.getString(12) + "'"
                                + "" + rsTransectionData.getString(13) + "'" + rsTransectionData.getDouble(14) + "'" + rsTransectionData.getString(15) + "'"
                                + "" + rsTransectionData.getString(16) + "'" + amtTax + "'" + rsTransectionData.getString(18) + "  ";
                        writeText = writeText.replaceAll(cot, pipe);
                        pw.println(writeText);
                        // writeText+='\n';
                        onlyTotTax +=amtTax;
                        disper = rsTransectionData.getDouble(19);
                        totDisAmt += rsTransectionData.getDouble(15);
                        totAmt += rsTransectionData.getDouble(14);
                        totTaxAmt = rsTransectionData.getDouble(20);
                        settlemode = rsTransectionData.getString(21);
                        grandTot = rsTransectionData.getDouble(22);
                        
                        if(settlemode.length()==0)
                        {
                            String settleSql ="select b.strSettelmentType from "+billsettlementdtl+" a , tblsettelmenthd b "
                                    + " where a.strBillNo='" + billno + "' and a.strSettlementCode=b.strSettelmentCode ";
                            ResultSet rsSettleMode=clsGlobalVarClass.dbMysql.executeResultSet(settleSql);
                            if(rsSettleMode.next()) {                                
                                settlemode=rsSettleMode.getString(1);
                            }
                        rsSettleMode.close();
                        }
                        if(settlemode.length()>8)
                        {
                        settlemode=settlemode.substring(0, 8);
                        }
                    }
                    rsTransectionData.close();
                    DecimalFormat objdec = new DecimalFormat("#.##");
                    totDisAmt = Double.parseDouble(objdec.format(totDisAmt));
                    disper = Double.parseDouble(objdec.format(disper));
                    totAmt = Double.parseDouble(objdec.format(totAmt));
                    totTaxAmt = Double.parseDouble(objdec.format(totTaxAmt));
                    grandTot = Double.parseDouble(objdec.format(grandTot));
                    writeText = "121'" + totAmt + "'" + totDisAmt + "'0.00'0.00'"+onlyTotTax+"'I'N'"+disCode+"'0.00'"+disper+"'0.00";
                    writeText = writeText.replaceAll(cot, pipe);
                    pw.println(writeText);
                    writeText = "131'T'" + settlemode + "'INR'1.00'"+grandTot+"'''"+grandTot;
                    writeText = writeText.replaceAll(cot, pipe);
                    pw.println(writeText);
                }
                ///////////Customer INFO////////////
                for (int i = 0; i < listBillNo.size(); i++)
                {
                    sql = "select c.strCustomerName,b.strCustomerCode,c.strStreetName,c.strLandmark,c.strArea,"
                            + " b.strRemarks,c.longMobileNo from " + billHdName + " b ,tblcustomermaster c  "
                            + " where b.strBillNo='" + listBillNo.get(i) + "' and LENGTH(b.strCustomerCode)>0 "
                            + " and b.strPosCode='" + posCode + "' and b.strCustomerCode=c.strCustomerCode and "
                            + " date(b.dteBillDate) BETWEEN '" + fromDate + "' and '" + toDate + "' ";
                    ResultSet rsCustInfo = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                    if (rsCustInfo.next())
                    {
                        String custName = rsCustInfo.getString(1);
                        String custCode = rsCustInfo.getString(2);
                        String add1 = rsCustInfo.getString(3);
                        String add2 = rsCustInfo.getString(4);
                        String add3 = rsCustInfo.getString(5);
                        String remark = rsCustInfo.getString(6);
                        String mobileNo = rsCustInfo.getString(7);
                        int cnt = 0;
                        boolean flg = false;
                        for (int j = 0; j < listCustInfoWriteText.size(); j++)
                        {
                            if (listCustInfoWriteText.get(j).contains(listBillNo.get(i)))
                            {
                                cnt = j;
                                flg = true;
                            }
                        }
                        if (flg)
                        {
                            pw.println(listCustInfoWriteText.get(cnt));
                        }
                        ///////////////////////////////////////////////////   
                        writeText = "104'" + custName + "'" + custCode + "'" + mobileNo + "'" + add1 + "'" + add2 + "'" + add3 + "'" + remark + "";
                        writeText = writeText.replaceAll(cot, pipe);
                        pw.println(writeText);
                    }
                }
                writeText = "1'CLOSED'" + TENANTID + "'" + posCode + "'" + receiprNoClosing + "'" + reciNo + "'" + printDate + "'" + time + "'" + usercode + "'" + printDate + "";
                writeText = writeText.replaceAll(cot, pipe);
                pw.println(writeText);


                writeText = writeText.replaceAll(cot, pipe);
                System.out.println("With | Cot");
                System.out.println(writeText);
                pw.close();
//                if (fromDate.equals(toDate))
//                {
//                    funFTPUploadFile();
//                    //funup();
//                }
            }
            else
            {
                return 0;
            }

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
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
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
        btnGenrateText = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        lblModuleName = new javax.swing.JLabel();
        lblMessage = new javax.swing.JLabel();
        dteFromDate = new com.toedter.calendar.JDateChooser();
        dteToDate = new com.toedter.calendar.JDateChooser();
        lblPosDateForCheck = new javax.swing.JLabel();
        rdbOldDate = new javax.swing.JRadioButton();
        rdbCurrent = new javax.swing.JRadioButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setExtendedState(MAXIMIZED_BOTH);
        setMinimumSize(new java.awt.Dimension(800, 600));
        setUndecorated(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
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
        lblformName.setText("- Mall Interface");
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

        btnGenrateText.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnGenrateText.setForeground(new java.awt.Color(255, 255, 255));
        btnGenrateText.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnGenrateText.setText("GENERATE");
        btnGenrateText.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnGenrateText.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnGenrateText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenrateTextActionPerformed(evt);
            }
        });

        btnReset.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnReset.setForeground(new java.awt.Color(255, 255, 255));
        btnReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnReset.setText("RESET");
        btnReset.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnReset.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });

        btnClose.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnClose.setForeground(new java.awt.Color(255, 255, 255));
        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnClose.setText("CLOSE");
        btnClose.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnClose.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

        lblModuleName.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        lblModuleName.setForeground(new java.awt.Color(14, 7, 7));
        lblModuleName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblModuleName.setText("Mall Interface");

        lblMessage.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

        lblPosDateForCheck.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

        buttonGroup1.add(rdbOldDate);
        rdbOldDate.setText("Old Date");
        rdbOldDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdbOldDateActionPerformed(evt);
            }
        });

        buttonGroup1.add(rdbCurrent);
        rdbCurrent.setText("Current Date");
        rdbCurrent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdbCurrentActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(111, 111, 111)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(btnGenrateText, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(100, 100, 100)
                        .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(110, 110, 110)
                        .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                                    .addGap(170, 170, 170)
                                    .addComponent(lblModuleName, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                                    .addGap(11, 11, 11)
                                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(rdbOldDate)
                                        .addComponent(dteFromDate, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGap(148, 148, 148))
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addComponent(rdbCurrent)
                            .addGap(54, 54, 54)
                            .addComponent(lblPosDateForCheck, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(100, 100, 100)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(dteToDate, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblMessage, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(117, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(lblModuleName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblPosDateForCheck, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(rdbOldDate)
                        .addComponent(rdbCurrent)))
                .addGap(31, 31, 31)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(dteFromDate, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                    .addComponent(dteToDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(83, 83, 83)
                .addComponent(lblMessage, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(70, 70, 70)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnGenrateText, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(141, Short.MAX_VALUE))
        );

        jPanel2.add(jPanel3, new java.awt.GridBagConstraints());

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnGenrateTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenrateTextActionPerformed
        // TODO add your handling code here:
        if (rdbOldDate.isSelected() || rdbCurrent.isSelected())
        {
            funCheckCurrentOROldReport();
            int num = funWriteToFile(fromDate, toDate, reportType, "N");
            fromDate = funGetCalenderDate(dteFromDate.getDate());
            toDate = funGetCalenderDate(dteToDate.getDate());

            if (num == 1)
            {
                dispose();
            }
        }
        else
        {
            JOptionPane.showMessageDialog(null, "Please Select One");
            return;

        }


    }//GEN-LAST:event_btnGenrateTextActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        // TODO add your handling code here:
        funResetFields();
    }//GEN-LAST:event_btnResetActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        dispose();
        clsGlobalVarClass.hmActiveForms.remove("GenrateMallInterfaceText");
    }//GEN-LAST:event_btnCloseActionPerformed

    private void rdbCurrentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdbCurrentActionPerformed
        // TODO add your handling code here:
        funCheckCurrentOROldReport();
    }//GEN-LAST:event_rdbCurrentActionPerformed

    private void rdbOldDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdbOldDateActionPerformed
        // TODO add your handling code here:
        funCheckCurrentOROldReport();
    }//GEN-LAST:event_rdbOldDateActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosed
    {//GEN-HEADEREND:event_formWindowClosed
        clsGlobalVarClass.hmActiveForms.remove("GenrateMallInterfaceText");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosing
    {//GEN-HEADEREND:event_formWindowClosing
        clsGlobalVarClass.hmActiveForms.remove("GenrateMallInterfaceText");
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
            java.util.logging.Logger.getLogger(frmGenrateMallInterfaceText.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(frmGenrateMallInterfaceText.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(frmGenrateMallInterfaceText.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(frmGenrateMallInterfaceText.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                new frmGenrateMallInterfaceText().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnGenrateText;
    private javax.swing.JButton btnReset;
    private javax.swing.ButtonGroup buttonGroup1;
    private com.toedter.calendar.JDateChooser dteFromDate;
    private com.toedter.calendar.JDateChooser dteToDate;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblMessage;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblModuleName1;
    private javax.swing.JLabel lblPosDateForCheck;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblformName;
    private javax.swing.JRadioButton rdbCurrent;
    private javax.swing.JRadioButton rdbOldDate;
    // End of variables declaration//GEN-END:variables

    private String funGetCalenderDate(Date objDate)
    {
        return (objDate.getYear() + 1900) + "-" + (objDate.getMonth() + 1) + "-" + (objDate.getDate());
    }

    private void funCheckCurrentOROldReport()
    {
        try
        {
            if (rdbCurrent.isSelected())
            {
                reportType = "Current";
                java.util.Date objDate = new java.util.Date();
                String currDate = objDate.getDate() + "-" + (objDate.getMonth() + 1) + "-" + (objDate.getYear() + 1900);
                java.util.Date date;
                date = new SimpleDateFormat("dd-MM-yyyy").parse(clsGlobalVarClass.gPOSDateToDisplay);
                fromDate = funGetCalenderDate(date);
                toDate = funGetCalenderDate(date);
                lblPosDateForCheck.setText(fromDate);
                dteFromDate.setEnabled(false);
                dteToDate.setEnabled(false);
            }
            else if (rdbOldDate.isSelected())
            {
                reportType = "Old";
                dteFromDate.setEnabled(true);
                dteToDate.setEnabled(true);
                fromDate = funGetCalenderDate(dteFromDate.getDate());
                toDate = funGetCalenderDate(dteToDate.getDate());
                lblPosDateForCheck.setText("");
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void funFTPUploadFile()
    {
        // get an ftpClient object  
        try
        {
            FTPClient ftpClient = new FTPClient();
            FileInputStream inputStream = null;
            String ftpUrl = "";
            String userName = "";
            String pass = "";
            String sqlAdd = "select strFTPAddress ,strFTPServerUserName,strFTPServerPass from tblsetup";
            ResultSet rsFTPAddress = clsGlobalVarClass.dbMysql.executeResultSet(sqlAdd);
            if (rsFTPAddress.next())
            {
                ftpUrl = rsFTPAddress.getString(1);
                userName = rsFTPAddress.getString(2);
                pass = rsFTPAddress.getString(3);
            }
            //ftpUrl = ftpUrl+"/%s;type=i";
            String filePath = System.getProperty("user.dir");
            filePath += "/Temp/" + textfileName;
            String uploadPath = "\\" + textfileName;

            // pass directory path on server to connect  
            ftpClient.connect(ftpUrl, 21);

            // pass username and password, returned true if authentication is  
            // successful  
            boolean login = ftpClient.login(userName, pass);

            if (login)
            {
                System.out.println("Connection established...");
                inputStream = new FileInputStream(filePath);
                boolean uploaded = ftpClient.storeFile(uploadPath, inputStream);
                if (uploaded)
                {
                    System.out.println("File uploaded successfully !");
                }
                else
                {
                    System.out.println("Error in uploading file !");
                }

                // logout the user, returned true if logout successfully  
                boolean logout = ftpClient.logout();
                if (logout)
                {
                    System.out.println("Connection close...");
                }
                ftpClient.disconnect();

            }
            else
            {
                System.out.println("Connection fail...");
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private long funGenMIReciptCode()
    {
        long lastNo = 1;
        try
        {
            sql = "select count(dblLastNo) from tblinternal where strTransactionType='MIReceiptNo'";
            ResultSet rsDelBoyCatCode = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            rsDelBoyCatCode.next();
            int cntDelBoyCategory = rsDelBoyCatCode.getInt(1);
            rsDelBoyCatCode.close();
            if (cntDelBoyCategory > 0)
            {
                sql = "select dblLastNo from tblinternal where strTransactionType='MIReceiptNo'";
                rsDelBoyCatCode = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                rsDelBoyCatCode.next();
                long code = rsDelBoyCatCode.getLong(1);
                code = code + 1;
                lastNo = code;
                rsDelBoyCatCode.close();
            }
            else
            {
                lastNo = 1;
            }
            String updateSql = "update tblinternal set dblLastNo=" + lastNo + " "
                    + "where strTransactionType='MIReceiptNo'";
            clsGlobalVarClass.dbMysql.execute(updateSql);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return lastNo;
    }

}
