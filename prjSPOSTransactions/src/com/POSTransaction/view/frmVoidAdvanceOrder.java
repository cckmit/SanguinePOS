package com.POSTransaction.view;

import com.POSGlobal.controller.clsGlobalVarClass;
import static com.POSGlobal.controller.clsGlobalVarClass.gSanguineWebServiceURL;
import com.POSGlobal.controller.clsUtility;
import com.POSGlobal.view.frmOkPopUp;
import com.POSGlobal.view.frmSearchFormDialog;
import com.POSTransaction.controller.clsSpecialOrderItemDtl;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class frmVoidAdvanceOrder extends javax.swing.JFrame
{

    private clsUtility objUtility;
    String userCode = clsGlobalVarClass.gUserCode;

    String sqlQuery = "select strAuditing from tbluserdtl where strUserCode='" + userCode + "' and strFormName='VoidAdvanceOrder' ";
    ResultSet rs, rs1;

    public frmVoidAdvanceOrder()
    {
        try
        {

            initComponents();
            objUtility = new clsUtility();
            lblModuleName.setText(clsGlobalVarClass.gSelectedModule);
            lblUserCode.setText(clsGlobalVarClass.gUserCode);
            lblPosName.setText(clsGlobalVarClass.gPOSName);
            lblModuleName.setText(clsGlobalVarClass.gSelectedModule);
            lblAdvDateHidden.setVisible(false);
            Timer timer = new Timer(500, new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    Date date1 = new Date();
                    String new_str = String.format("%tr", date1);
                    String dateAndTime = clsGlobalVarClass.gPOSDateToDisplay + " " + new_str;
                    lblDate.setText(dateAndTime);
                }
            });
            timer.setRepeats(true);
            timer.setCoalesce(true);
            timer.setInitialDelay(0);
            timer.start();

            if(clsGlobalVarClass.gSuperUser)
            {
                sqlQuery = "select strAuditing from tblsuperuserdtl where strUserCode='" + userCode + "' and strFormName='VoidAdvanceOrder'";
            }
            
            
            String sql = "select strReasonCode,strreasonName from tblreasonmaster where strVoidAdvOrder='Y' ";
            ResultSet rsReason = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            while (rsReason.next())
            {
                cmbReason.addItem(rsReason.getString(2));
            }
            rsReason.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void funVoidAdvOrder()
    {
        boolean flgSODeletionFromMMS = false;
        Map<String, List<clsSpecialOrderItemDtl>> hmAdvSOOrderItemDtl = new HashMap<String, List<clsSpecialOrderItemDtl>>();
        String advOrderNo = "", orderDeliveryDate = lblAdvDateHidden.getText();
        try
        {
            advOrderNo = txtBillNo.getText().toString();
            String reasonCode = "";
            String sqlResason = "select strReasonCode,strReasonName from tblreasonmaster "
                    + " where strReasonName='" + cmbReason.getSelectedItem().toString() + "' ";
            rs = clsGlobalVarClass.dbMysql.executeResultSet(sqlResason);
            if (rs.next())
            {
                reasonCode = rs.getString(1);
            }
            rs.close();

            if (clsGlobalVarClass.gProductionLinkup)
            {
                int daysBeforeOrderToCancel = 0;
                String sql = "select intDaysBeforeOrderToCancel from tblsetup "
                        + " where (strPOSCode='" + clsGlobalVarClass.gPOSCode + "'  OR strPOSCode='All')";
                rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                if (rs.next())
                {
                    daysBeforeOrderToCancel = rs.getInt(1);
                }
                rs.close();

                long timeDiff = objUtility.funCompareDate(clsGlobalVarClass.gPOSDateForTransaction.split(" ")[0], orderDeliveryDate);
                long diffDays = timeDiff / (24 * 60 * 60 * 1000);

                /*
                 Date dtCurrentDate = new Date();
                 String currentDate = (dtCurrentDate.getYear() + 1900) + "-" + (dtCurrentDate.getMonth() + 1) + "-" + (dtCurrentDate.getDate());
                 long diff = objUtility.funCompareDate(currentDate, orderDeliveryDate);
                 long diffDays = diff / (24 * 60 * 60 * 1000);
                 System.out.println(diffDays);*/
                if (daysBeforeOrderToCancel <= diffDays)
                {
                    boolean flgAdvOrderPlaced = false;
                    String sqlAdvSOOrderDtl = "select a.strOrderCode,a.strSOCode,b.strAdvOrderNo,b.strItemCode,b.strProductCode "
                            + " from tblplaceorderhd a,tblplaceorderdtl b,tbladvbookbillhd c,tbladvbookbilldtl d "
                            + " where a.strOrderCode=b.strOrderCode and b.strAdvOrderNo=c.strAdvBookingNo "
                            + " and c.strAdvBookingNo=d.strAdvBookingNo and b.strItemCode=d.strItemCode "
                            + " and a.strOrderType!='Normal' and c.strAdvBookingNo='" + advOrderNo + "' "
                            + " group by a.strOrderCode,b.strItemCode ";
                    rs = clsGlobalVarClass.dbMysql.executeResultSet(sqlAdvSOOrderDtl);
                    while (rs.next())
                    {
                        flgAdvOrderPlaced = true;
                        List<clsSpecialOrderItemDtl> listItemDtl = null;
                        if (hmAdvSOOrderItemDtl.containsKey(advOrderNo))
                        {
                            listItemDtl = hmAdvSOOrderItemDtl.get(advOrderNo);
                        }
                        else
                        {
                            listItemDtl = new ArrayList<clsSpecialOrderItemDtl>();
                        }
                        clsSpecialOrderItemDtl objSpecialItemDtl = new clsSpecialOrderItemDtl();
                        objSpecialItemDtl.setItemCode(rs.getString(4));
                        objSpecialItemDtl.setProductCode(rs.getString(5));
                        objSpecialItemDtl.setSOCode(rs.getString(2));
                        objSpecialItemDtl.setPlaceOrderCode(rs.getString(1));
                        objSpecialItemDtl.setAdvOrderNo(rs.getString(3));
                        listItemDtl.add(objSpecialItemDtl);
                        hmAdvSOOrderItemDtl.put(advOrderNo, listItemDtl);
                    }
                    rs.close();

                    if (!flgAdvOrderPlaced)
                    {
                        funInsertAuditAndDeleteFromLive(advOrderNo, reasonCode);
                    }

                    if (hmAdvSOOrderItemDtl.size() > 0)
                    {
                        flgSODeletionFromMMS = funDeleteAdvOrderSOFromMMS(hmAdvSOOrderItemDtl);
                        if (flgSODeletionFromMMS)
                        {
                            funInsertAuditAndDeleteFromLive(advOrderNo, reasonCode);
                        }
                        else
                        {
                            JOptionPane.showMessageDialog(null, "SO Delete failed in MMS!!!");
                            return;
                        }
                    }
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "Cannot delete this order check Days Before Order Can be Cancelled in Property Setup !!!");
                }
            }
            else
            {
                funInsertAuditAndDeleteFromLive(advOrderNo, reasonCode);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void funInsertAuditAndDeleteFromLive(String advOrderNo, String reasonCode) throws Exception
    {
        String sql = "insert into tblvoidadvbookbillhd(strAdvBookingNo,dteAdvBookingDate,dteOrderFor,strPOSCode,strSettelmentMode,dblDiscountAmt"
                + ",dblDiscountPer,dblTaxAmt,dblSubTotal,dblGrandTotal,"
                + "strUserCreated,strUserEdited,dteDateCreated,dteDateEdited,strClientCode,strCustomerCode,intShiftCode,strMessage,strShape,strNote,"
                + "strDataPostFlag,strDeliveryTime,strWaiterNo,strHomeDelivery,dblHomeDelCharges,strOrderType,strManualAdvOrderNo,strImageName"
                + ",strSpecialsymbolImage,strUrgentOrder,strReasonCode,strRemark) "
                + "(select strAdvBookingNo,dteAdvBookingDate,dteOrderFor,strPOSCode,strSettelmentMode,dblDiscountAmt,dblDiscountPer,dblTaxAmt,dblSubTotal,dblGrandTotal,"
                + "'" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "'"
                + ",strClientCode,strCustomerCode,intShiftCode,strMessage,strShape,strNote,'N',strDeliveryTime,strWaiterNo,strHomeDelivery,dblHomeDelCharges,strOrderType,strManualAdvOrderNo,strImageName,strSpecialsymbolImage,strUrgentOrder,"
                + "'" + reasonCode + "','' "
                + "from tbladvbookbillhd where strAdvBookingNo='" + advOrderNo + "') ";
        rs1 = clsGlobalVarClass.dbMysql.executeResultSet(sqlQuery);
        if (rs1.next())
        {
            if (Boolean.parseBoolean(rs1.getString(1)))
            {
                clsGlobalVarClass.dbMysql.execute(sql);
            }
        }

        sql = "insert into tblvoidadvbookbilldtl (strItemCode,strItemName,strAdvBookingNo,dblQuantity,dblAmount"
                + " ,dblTaxAmount,dteAdvBookingDate,dteOrderFor,strClientCode,strCustomerCode,dblWeight,strDataPostFlag)"
                + " (select strItemCode,strItemName,strAdvBookingNo,dblQuantity,dblAmount"
                + " ,dblTaxAmount,dteAdvBookingDate,dteOrderFor,strClientCode,strCustomerCode,dblWeight,'N' "
                + " from tbladvbookbilldtl where strAdvBookingNo='" + advOrderNo + "') ";
        rs1 = clsGlobalVarClass.dbMysql.executeResultSet(sqlQuery);
        if (rs1.next())
        {
            if (Boolean.parseBoolean(rs1.getString(1)))
            {
                clsGlobalVarClass.dbMysql.execute(sql);
            }
        }

        sql = "insert into tblvoidadvbookbillchardtl (strItemCode,strAdvBookingNo,strCharCode,strCharValues"
                + " ,strClientCode,strDataPostFlag) "
                + " (select strItemCode,strAdvBookingNo,strCharCode,strCharValues"
                + " ,strClientCode,'N' from tbladvbookbillchardtl where strAdvBookingNo='" + advOrderNo + "') ";
        rs1 = clsGlobalVarClass.dbMysql.executeResultSet(sqlQuery);
        if (rs1.next())
        {
            if (Boolean.parseBoolean(rs1.getString(1)))
            {
                clsGlobalVarClass.dbMysql.execute(sql);
            }
        }
        sql = "insert into tblvoidadvancereceipthd (strReceiptNo, strAdvBookingNo, strPOSCode, strSettelmentMode"
                + ", dtReceiptDate, dblAdvDeposite, intShiftCode, strUserCreated, strUserEdited, dtDateCreated"
                + ", dtDateEdited, strClientCode, strDataPostFlag)"
                + "(select strReceiptNo, strAdvBookingNo, strPOSCode, strSettelmentMode, dtReceiptDate, dblAdvDeposite"
                + ", intShiftCode,'" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "'"
                + ",'" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "'"
                + ", strClientCode, 'N' "
                + " from tbladvancereceipthd where strAdvBookingNo='" + advOrderNo + "') ";
        rs1 = clsGlobalVarClass.dbMysql.executeResultSet(sqlQuery);
        if (rs1.next())
        {
            if (Boolean.parseBoolean(rs1.getString(1)))
            {
                clsGlobalVarClass.dbMysql.execute(sql);
            }
        }

        sql = "insert into tblvoidadvancereceiptdtl "
                + "(strReceiptNo, strSettlementCode, strCardNo, strExpirydate, strChequeNo, dteCheque"
                + ", strBankName, dblAdvDepositesettleAmt, strRemark, dblPaidAmt, strClientCode,strDataPostFlag,dteInstallment)"
                + " (select b.strReceiptNo, b.strSettlementCode, b.strCardNo, b.strExpirydate, b.strChequeNo, b.dteCheque"
                + ", b.strBankName, b.dblAdvDepositesettleAmt, b.strRemark, b.dblPaidAmt, b.strClientCode, 'N',b.dteInstallment "
                + " from tbladvancereceipthd a,tbladvancereceiptdtl b "
                + " where a.strReceiptNo=b.strReceiptNo and a.strAdvBookingNo='" + advOrderNo + "') ";
        rs1 = clsGlobalVarClass.dbMysql.executeResultSet(sqlQuery);
        if (rs1.next())
        {
            if (Boolean.parseBoolean(rs1.getString(1)))
            {
                clsGlobalVarClass.dbMysql.execute(sql);
            }
        }

        sql = "insert into tblvoidadvordermodifierdtl "
                + " (strAdvOrderNo,strItemCode,strModifierCode,strModifierName,dblQuantity,dblAmount,strClientCode"
                + " ,strCustomerCode,strUserCreated,strUserEdited,dteDateCreated,dteDateEdited,strDataPostFlag)"
                + " (select strAdvOrderNo,strItemCode,strModifierCode,strModifierName,dblQuantity,dblAmount,strClientCode"
                + " ,strCustomerCode,strUserCreated,strUserEdited,dteDateCreated,dteDateEdited,'N' "
                + " from tbladvordermodifierdtl where strAdvOrderNo='" + advOrderNo + "') ";
        rs1 = clsGlobalVarClass.dbMysql.executeResultSet(sqlQuery);
        if (rs1.next())
        {
            if (Boolean.parseBoolean(rs1.getString(1)))
            {
                clsGlobalVarClass.dbMysql.execute(sql);
            }
        }

        int exc = clsGlobalVarClass.dbMysql.execute("delete from tbladvbookbillhd where strAdvBookingNo='" + advOrderNo + "'");
        clsGlobalVarClass.dbMysql.execute("delete from tbladvbookbilldtl where strAdvBookingNo='" + advOrderNo + "'");
        clsGlobalVarClass.dbMysql.execute("delete from tbladvbookbillchardtl where strAdvBookingNo='" + advOrderNo + "'");
        clsGlobalVarClass.dbMysql.execute("delete a.*,b.* "
                + "from tbladvancereceipthd a inner join tbladvancereceiptdtl b on a.strReceiptNo=b.strReceiptNo "
                + "where  a.strAdvBookingNo='" + advOrderNo + "' ");
        //clsGlobalVarClass.dbMysql.execute("delete from tbladvancereceipthd where strAdvBookingNo='" + advOrderNo + "'");
        clsGlobalVarClass.dbMysql.execute("delete from tbladvordermodifierdtl where strAdvOrderNo='" + advOrderNo + "'");

        if (exc > 0)
        {
            new frmOkPopUp(this, "Advance Order Voided Successfully", "Successfull", 3).setVisible(true);
            funResetFields();
            if (clsGlobalVarClass.gConnectionActive.equals("Y"))
            {
                if (clsGlobalVarClass.gDataSendFrequency.equals("After Every Bill"))
                {
                    clsGlobalVarClass.funInvokeHOWebserviceForTrans("Audit", "Void");
                }
            }
        }
    }

    private void funSetAdvOrderData(Object[] data)
    {
        try
        {
            txtBillNo.setText(data[0].toString());
            lblCustomer.setText(data[1].toString());
            lblAdvDate.setText(data[2].toString());
            lblAdvDateHidden.setText(data[2].toString());
            lblDepositeAmt.setText(data[3].toString());
            lblTotalAmt.setText(data[4].toString());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void funResetFields()
    {
        txtBillNo.setText("");
        lblCustomer.setText("");
        lblAdvDate.setText("");
        lblAdvDateHidden.setText("");
        lblDepositeAmt.setText("");
        lblTotalAmt.setText("");
        cmbReason.setSelectedIndex(0);
    }

    private boolean funDeleteAdvOrderSOFromMMS(Map<String, List<clsSpecialOrderItemDtl>> hmItemListDtl)
    {
        boolean flgResult = false;
        try
        {
            if (hmItemListDtl.size() > 0)
            {
                JSONObject objJson = new JSONObject();
                JSONArray arrItemDtl = new JSONArray();

                for (Map.Entry<String, List<clsSpecialOrderItemDtl>> entry : hmItemListDtl.entrySet())
                {
                    List<clsSpecialOrderItemDtl> listItemDtl = entry.getValue();
                    for (clsSpecialOrderItemDtl obj : listItemDtl)
                    {
                        JSONObject jObjItemDtl = new JSONObject();
                        jObjItemDtl.put("ProdCode", obj.getProductCode());
                        jObjItemDtl.put("WSClientCode", clsGlobalVarClass.gWSClientCode);
                        jObjItemDtl.put("SOCode", obj.getSOCode());
                        jObjItemDtl.put("POSItemCode", obj.getItemCode());
                        jObjItemDtl.put("AdvOrderNo", obj.getAdvOrderNo());
                        arrItemDtl.add(jObjItemDtl);
                    }
                }

                if (arrItemDtl.size() > 0)
                {
                    objJson.put("SODetails", arrItemDtl);
                    String hoURL = gSanguineWebServiceURL + "/MMSIntegration/funDeleteAdvanceOrder";
                    System.out.println(hoURL);
                    URL url = new URL(hoURL);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoOutput(true);
                    conn.setRequestMethod("DELETE");
                    conn.setRequestProperty("Content-Type", "application/json");
                    OutputStream os = conn.getOutputStream();
                    os.write(objJson.toString().getBytes());
                    os.flush();
                    int resCode = conn.getResponseCode();
                    if (resCode != HttpURLConnection.HTTP_OK)
                    {
                        throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
                    }
                    BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
                    String output = "", response = "";
                    while ((output = br.readLine()) != null)
                    {
                        response += output;
                    }
                    if (response.equalsIgnoreCase("Deleted Successfully"))
                    {
                        String placeOrderCode = "", sql = "";
                        for (Map.Entry<String, List<clsSpecialOrderItemDtl>> entry : hmItemListDtl.entrySet())
                        {
                            List<clsSpecialOrderItemDtl> listItemDtl = entry.getValue();
                            for (clsSpecialOrderItemDtl obj : listItemDtl)
                            {
                                sql = "delete from tblplaceorderdtl "
                                        + " where strOrderCode='" + obj.getPlaceOrderCode() + "' and strItemCode='" + obj.getItemCode() + "' "
                                        + " and strAdvOrderNo='" + obj.getAdvOrderNo() + "' and strClientCode='" + clsGlobalVarClass.gClientCode + "' ";
                                clsGlobalVarClass.dbMysql.execute(sql);

                                sql = "delete from tblplaceorderadvorderdtl "
                                        + " where strAdvOrderNo='" + obj.getAdvOrderNo() + "' and strClientCode='" + clsGlobalVarClass.gClientCode + "' ";
                                clsGlobalVarClass.dbMysql.execute(sql);
                                placeOrderCode = obj.getPlaceOrderCode();
                            }
                        }

                        sql = "select strOrderCode from tblplaceorderdtl "
                                + " where strOrderCode='" + placeOrderCode + "' and strClientCode='" + clsGlobalVarClass.gClientCode + "' ";
                        rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                        if (!rs.next())
                        {
                            sql = "delete from tblplaceorderhd "
                                    + " where strOrderCode='" + placeOrderCode + "' and strClientCode='" + clsGlobalVarClass.gClientCode + "' ";
                            clsGlobalVarClass.dbMysql.execute(sql);
                        }
                        rs.close();
                        flgResult = true;
                    }
                    System.out.println("res=" + response);
                    conn.disconnect();
                }
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jpanelHeader = new javax.swing.JPanel();
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
        jPanelLayout = new JPanel() {  
            public void paintComponent(Graphics g) {  
                Image img = Toolkit.getDefaultToolkit().getImage(  
                    getClass().getResource("/com/POSTransaction/images/imgBackgroundImage.png"));  
                g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);  
            }  
        };  ;
        jPanelBody = new javax.swing.JPanel();
        txtBillNo = new javax.swing.JTextField();
        lblHeader = new javax.swing.JLabel();
        btnVoid = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        lblBillNo = new javax.swing.JLabel();
        lblAdvDate = new javax.swing.JLabel();
        lblCustomer = new javax.swing.JLabel();
        lblDepositeAmt = new javax.swing.JLabel();
        lblTotalAmt = new javax.swing.JLabel();
        lblName = new javax.swing.JLabel();
        lblDte = new javax.swing.JLabel();
        lblDepoAmt = new javax.swing.JLabel();
        lblTotAmt = new javax.swing.JLabel();
        cmbReason = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        lblAdvDateHidden = new javax.swing.JLabel();

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

        jpanelHeader.setBackground(new java.awt.Color(69, 164, 238));
        jpanelHeader.setLayout(new javax.swing.BoxLayout(jpanelHeader, javax.swing.BoxLayout.LINE_AXIS));

        lblProductName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblProductName.setForeground(new java.awt.Color(255, 255, 255));
        lblProductName.setText("SPOS -");
        jpanelHeader.add(lblProductName);

        lblModuleName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblModuleName.setForeground(new java.awt.Color(255, 255, 255));
        jpanelHeader.add(lblModuleName);

        lblformName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblformName.setForeground(new java.awt.Color(255, 255, 255));
        lblformName.setText("- Void Advance Order");
        jpanelHeader.add(lblformName);
        jpanelHeader.add(filler4);
        jpanelHeader.add(filler5);

        lblPosName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblPosName.setForeground(new java.awt.Color(255, 255, 255));
        lblPosName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPosName.setMaximumSize(new java.awt.Dimension(321, 30));
        lblPosName.setMinimumSize(new java.awt.Dimension(321, 30));
        lblPosName.setPreferredSize(new java.awt.Dimension(321, 30));
        jpanelHeader.add(lblPosName);
        jpanelHeader.add(filler6);

        lblUserCode.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblUserCode.setForeground(new java.awt.Color(255, 255, 255));
        lblUserCode.setMaximumSize(new java.awt.Dimension(90, 30));
        lblUserCode.setMinimumSize(new java.awt.Dimension(90, 30));
        lblUserCode.setPreferredSize(new java.awt.Dimension(90, 30));
        jpanelHeader.add(lblUserCode);

        lblDate.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblDate.setForeground(new java.awt.Color(255, 255, 255));
        lblDate.setMaximumSize(new java.awt.Dimension(192, 30));
        lblDate.setMinimumSize(new java.awt.Dimension(192, 30));
        lblDate.setPreferredSize(new java.awt.Dimension(192, 30));
        jpanelHeader.add(lblDate);

        lblHOSign.setMaximumSize(new java.awt.Dimension(34, 30));
        lblHOSign.setMinimumSize(new java.awt.Dimension(34, 30));
        lblHOSign.setPreferredSize(new java.awt.Dimension(34, 30));
        jpanelHeader.add(lblHOSign);

        getContentPane().add(jpanelHeader, java.awt.BorderLayout.PAGE_START);

        jPanelLayout.setLayout(new java.awt.GridBagLayout());

        jPanelBody.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        jPanelBody.setMinimumSize(new java.awt.Dimension(800, 570));
        jPanelBody.setOpaque(false);

        txtBillNo.setEditable(false);
        txtBillNo.setEnabled(false);
        txtBillNo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtBillNoMouseClicked(evt);
            }
        });

        lblHeader.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lblHeader.setText("Void Advance Order");

        btnVoid.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnVoid.setForeground(new java.awt.Color(255, 255, 255));
        btnVoid.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn1.png"))); // NOI18N
        btnVoid.setText("VOID");
        btnVoid.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnVoid.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn2.png"))); // NOI18N
        btnVoid.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVoidActionPerformed(evt);
            }
        });

        btnReset.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnReset.setForeground(new java.awt.Color(255, 255, 255));
        btnReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn1.png"))); // NOI18N
        btnReset.setText("RESET");
        btnReset.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnReset.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn2.png"))); // NOI18N
        btnReset.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnResetMouseClicked(evt);
            }
        });

        btnClose.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnClose.setForeground(new java.awt.Color(255, 255, 255));
        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn1.png"))); // NOI18N
        btnClose.setText("CLOSE");
        btnClose.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnClose.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn2.png"))); // NOI18N
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        btnClose.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnCloseKeyPressed(evt);
            }
        });

        lblBillNo.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblBillNo.setText("Advance Order Bill No.");

        lblDepositeAmt.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        lblTotalAmt.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        lblName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblName.setText("Customer Name");

        lblDte.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblDte.setText("Date ");

        lblDepoAmt.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblDepoAmt.setText("Deposit Amount");

        lblTotAmt.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblTotAmt.setText("Total Amount");

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel1.setText("Reason");

        javax.swing.GroupLayout jPanelBodyLayout = new javax.swing.GroupLayout(jPanelBody);
        jPanelBody.setLayout(jPanelBodyLayout);
        jPanelBodyLayout.setHorizontalGroup(
            jPanelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelBodyLayout.createSequentialGroup()
                .addContainerGap(263, Short.MAX_VALUE)
                .addGroup(jPanelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelBodyLayout.createSequentialGroup()
                        .addGroup(jPanelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblHeader, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanelBodyLayout.createSequentialGroup()
                                .addComponent(btnVoid, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(35, 35, 35)
                                .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(34, 34, 34)
                                .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(218, 218, 218))
                    .addGroup(jPanelBodyLayout.createSequentialGroup()
                        .addGroup(jPanelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(lblTotAmt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblDte, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanelBodyLayout.createSequentialGroup()
                                    .addComponent(lblDepoAmt, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, Short.MAX_VALUE))
                                .addComponent(lblName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanelBodyLayout.createSequentialGroup()
                                .addGroup(jPanelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblBillNo, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cmbReason, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 304, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanelBodyLayout.createSequentialGroup()
                                        .addComponent(lblAdvDate, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(53, 53, 53)
                                        .addComponent(lblAdvDateHidden, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(lblDepositeAmt, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblTotalAmt, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtBillNo, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanelBodyLayout.setVerticalGroup(
            jPanelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelBodyLayout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addComponent(lblHeader, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBillNo, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblBillNo, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19)
                .addGroup(jPanelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblName, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblAdvDate, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
                    .addComponent(lblDte, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblAdvDateHidden, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblDepositeAmt, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDepoAmt, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTotalAmt, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTotAmt, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbReason, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(jPanelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnVoid, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(112, Short.MAX_VALUE))
        );

        jPanelLayout.add(jPanelBody, new java.awt.GridBagConstraints());

        getContentPane().add(jPanelLayout, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnResetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnResetMouseClicked
        // TODO add your handling code here:
        funResetFields();
    }//GEN-LAST:event_btnResetMouseClicked

    private void btnCloseKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnCloseKeyPressed

    }//GEN-LAST:event_btnCloseKeyPressed

    private void txtBillNoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtBillNoMouseClicked
        // TODO add your handling code here:
        objUtility.funCallForSearchForm("VoidAdvOrder");
        new frmSearchFormDialog(this, true).setVisible(true);
        if (clsGlobalVarClass.gSearchItemClicked)
        {
            Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
            funSetAdvOrderData(data);
            clsGlobalVarClass.gSearchItemClicked = false;
        }
    }//GEN-LAST:event_txtBillNoMouseClicked

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        dispose();
        clsGlobalVarClass.hmActiveForms.remove("VoidAdvanceOrder");
    }//GEN-LAST:event_btnCloseActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosed
    {//GEN-HEADEREND:event_formWindowClosed
        clsGlobalVarClass.hmActiveForms.remove("VoidAdvanceOrder");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosing
    {//GEN-HEADEREND:event_formWindowClosing
        clsGlobalVarClass.hmActiveForms.remove("VoidAdvanceOrder");
    }//GEN-LAST:event_formWindowClosing

    private void btnVoidActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVoidActionPerformed
        // TODO add your handling code here:
        if (txtBillNo.getText().isEmpty())
        {
            JOptionPane.showMessageDialog(this, "Please Select Advance Order Number");
            return;
        }
        if (cmbReason.getSelectedIndex() < 0)
        {
            JOptionPane.showMessageDialog(this, "Please Select/Create Reason For Void Advance Order.");
            return;
        }
        funVoidAdvOrder();
    }//GEN-LAST:event_btnVoidActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnVoid;
    private javax.swing.JComboBox cmbReason;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanelBody;
    private javax.swing.JPanel jPanelLayout;
    private javax.swing.JPanel jpanelHeader;
    private javax.swing.JLabel lblAdvDate;
    private javax.swing.JLabel lblAdvDateHidden;
    private javax.swing.JLabel lblBillNo;
    private javax.swing.JLabel lblCustomer;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblDepoAmt;
    private javax.swing.JLabel lblDepositeAmt;
    private javax.swing.JLabel lblDte;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblHeader;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblName;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblTotAmt;
    private javax.swing.JLabel lblTotalAmt;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblformName;
    private javax.swing.JTextField txtBillNo;
    // End of variables declaration//GEN-END:variables
}
