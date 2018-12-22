/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSMaster.view;


import com.POSGlobal.controller.clsGlobalSingleObject;
import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsUtility;
import com.POSGlobal.view.frmAlfaNumericKeyBoard;
import com.POSGlobal.view.frmOkPopUp;
import com.POSGlobal.view.frmSearchFormDialog;
import com.POSMaster.controller.clsUserGroupRightsBean;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author Padalkar Vinayak 
 */
public class frmUserGroupRights extends javax.swing.JFrame {

    private String code, strCode,dteCreated, dteEdited, moduleName,validDate;
    boolean flag;
    Map<String, String> mapModelImg=new HashMap<>();
    List alReportModel=new ArrayList();
    clsUtility objUtility = new clsUtility();
    HashMap<String, List<clsUserGroupRightsBean>> mapUserRights=new HashMap<String, List<clsUserGroupRightsBean>>();
    /**
     * This method is used to initialize frmUserGroupRights
     */
    public frmUserGroupRights() {
        initComponents();


        try {
            Timer timer = new Timer(500, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Date date1 = new Date();
                    String newstr = String.format("%tr", date1);
                    String dateAndTime = clsGlobalVarClass.gPOSDateToDisplay + " " + newstr;
                    lblDate.setText(dateAndTime);
                }
            });
            timer.setRepeats(true);
            timer.setCoalesce(true);
            timer.setInitialDelay(0);
            timer.start();
            
            java.util.Date dt = new java.util.Date();
            String dte = dt.getDate() + "-" + (dt.getMonth() + 1) + "-" + (dt.getYear() + 1900+10);
            java.util.Date date = new SimpleDateFormat("dd-MM-yyyy").parse(dte);
           // dteValid.setDate(date);
            lblUserCode.setText(clsGlobalVarClass.gUserCode);
            lblPosName.setText(clsGlobalVarClass.gPOSName);
            lblDate.setText(clsGlobalVarClass.gPOSDateToDisplay);
            lblModuleName.setText(clsGlobalVarClass.gSelectedModule);
            
            dteExpiryCaptain.setDate(date);
            dteExpiryCashier.setDate(date);
            dteExpiryManager.setDate(date);
            dteExpiryOwner.setDate(date);
            
            txtUserCodeCashier.setText("Cashier");
            txtUserNameCashier.setText("Cashier");
            txtPassCashier.setText("cashier123");
            
            txtUserCodeCaptain.setText("Captain");
            txtUserNameCaptain.setText("Captain");
            txtPassCaptain.setText("captain123");
            
            txtUserCodeManager.setText("Manager");
            txtUserNameManager.setText("Manager");
            txtPassManager.setText("manager123");
            
            txtUserCodeOwner.setText("Owner");
            txtUserNameOwner.setText("Owner");
            txtPassOwner.setText("owner123");
            
            funLoadModelWithImages();
            funLoadReportModel();
        } catch (Exception e) {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }

    }
    private void funLoadModelWithImages()
    {
        try{
            String sql = "select a.strModuleName, a.strImageName  from tblforms a  order by strModuleName;";
            ResultSet rsForms = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            while(rsForms.next())
            {
                mapModelImg.put(rsForms.getString(1),rsForms.getString(2));
            }
            rsForms.close();
        }
        catch(Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
        
    }
     private void funLoadReportModel()
    {
        try{
            String sql = "select a.strModuleName  from tblforms a  where a.strModuleType ='R' order by strModuleName;";
            ResultSet rsForms = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            while(rsForms.next())
            {
                alReportModel.add(rsForms.getString(1));
            }
            rsForms.close();
        }
        catch(Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
        
    }
    
    private String funGetValidDate(Date dteValiTillDate)
    {
           // Date dteValiTillDate = dteExpiryCaptain.getDate();
            int d = dteValiTillDate.getDate();
            int m = dteValiTillDate.getMonth() + 1;
            int y = dteValiTillDate.getYear() + 1900;
            
            return y + "-" + m + "-" + d;
    }
    private void funSaveUserGroupRights()
    {
        if(!(clsGlobalVarClass.gUserCode.equalsIgnoreCase("Sanguine")))
        {
            new frmOkPopUp(null, "You dont have rights !!!", "Error", 0).setVisible(true);
            return;
        }
        java.util.Date curDt = new java.util.Date();
            dteCreated = ((curDt.getYear() + 1900) + "-" + (curDt.getMonth() + 1) + "-" + curDt.getDate())
                    + " " + (curDt.getHours() + ":" + curDt.getMinutes() + ":" + curDt.getSeconds());
            dteEdited = ((curDt.getYear() + 1900) + "-" + (curDt.getMonth() + 1) + "-" + curDt.getDate())
                    + " " + (curDt.getHours() + ":" + curDt.getMinutes() + ":" + curDt.getSeconds());
         
        String selectedPOSCodes="";
       
            Date currentDate = new Date();
            ArrayList alValidDates=new ArrayList();
           
            alValidDates.add(funGetValidDate(dteExpiryCaptain.getDate()));
            alValidDates.add(funGetValidDate(dteExpiryCashier.getDate()));
            alValidDates.add(funGetValidDate(dteExpiryManager.getDate()));
            alValidDates.add(funGetValidDate(dteExpiryOwner.getDate()));
            
            String validDateOwner = funGetValidDate(dteExpiryOwner.getDate());
         
         clsUtility obj = new clsUtility();
         try
            { 
            String selectQuery = "select strPosCode,strPosName from tblposmaster  where strOperationalYN='Y'";
            ResultSet rsPOS = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery);
            while (rsPOS.next())
            {
                selectedPOSCodes +=rsPOS.getString(1)+",";
            }
            if (selectedPOSCodes.isEmpty())
            {
                JOptionPane.showMessageDialog(null, "Select Atleast one POS!!!");
                return;
            }
            selectedPOSCodes = selectedPOSCodes.substring(0, selectedPOSCodes.length() - 1);

                
               if (txtUserCodeCaptain.getText().trim().length() == 0)
                {
                   new frmOkPopUp(null, "Please Enter User Code", "Error", 0).setVisible(true);
                   txtUserCodeCaptain.requestFocus();
                   return;
                }
               if (txtUserCodeCaptain.getText().trim().equalsIgnoreCase("sanguine"))
               {
                   new frmOkPopUp(null, "User Code Not Allowed", "Error", 0).setVisible(true);
                   txtUserCodeCaptain.setText("");
                   txtUserCodeCaptain.requestFocus();
                   return;
               }
               if (txtUserCodeCaptain.getText().length() > 10)
               {
                   new frmOkPopUp(null, "User Code length must be less than 10", "Error", 0).setVisible(true);
                   return;
               }

               if (txtUserNameCaptain.getText().length() == 1)
               {
                   new frmOkPopUp(null, "Full Name should not blank", "Error", 0).setVisible(true);
                   return;
               }

               if (!obj.funCheckLength(txtUserNameCaptain.getText(), 25))
               {
                   new frmOkPopUp(this, "User Name length must be less than 25", "Error", 0).setVisible(true);
                   txtUserNameCaptain.requestFocus();
                   return;
               }
               if (dteExpiryCaptain.getDate().before(currentDate))
               {
                   new frmOkPopUp(null, "Invalid valid till date.", "Error", 0).setVisible(true);
                   return;
               }

               if (txtPassCaptain.getText().length() == 0)
               {
                   new frmOkPopUp(null, "Password field is blank", "Error", 0).setVisible(true);
                   return;
               }



                if (txtUserCodeCashier.getText().trim().length() == 0)
               {
                   new frmOkPopUp(null, "Please Enter User Code", "Error", 0).setVisible(true);
                   txtUserCodeCashier.requestFocus();
                   return;
               }
               if (txtUserCodeCashier.getText().trim().equalsIgnoreCase("sanguine"))
               {
                   new frmOkPopUp(null, "User Code Not Allowed", "Error", 0).setVisible(true);
                   txtUserCodeCashier.setText("");
                   txtUserCodeCashier.requestFocus();
                   return;
               }
               if (txtUserCodeCashier.getText().length() > 10)
               {
                   new frmOkPopUp(null, "User Code length must be less than 10", "Error", 0).setVisible(true);
                   return;
               }

               if (txtUserNameCashier.getText().length() == 1)
               {
                   new frmOkPopUp(null, "Full Name should not blank", "Error", 0).setVisible(true);
                   return;
               }

               if (!obj.funCheckLength(txtUserNameCashier.getText(), 25))
               {
                   new frmOkPopUp(this, "User Name length must be less than 25", "Error", 0).setVisible(true);
                   txtUserNameCashier.requestFocus();
                   return;
               }
               if (dteExpiryCashier.getDate().before(currentDate))
               {
                   new frmOkPopUp(null, "Invalid valid till date.", "Error", 0).setVisible(true);
                   return;
               }

               if (txtPassCashier.getText().length() == 0)
               {
                   new frmOkPopUp(null, "Password field is blank", "Error", 0).setVisible(true);
                   return;
               }

                   String encKey = "04081977";
                   ArrayList alUserCode=new ArrayList();
                   ArrayList alPassword=new ArrayList();
                   ArrayList alUserName=new ArrayList();
                   ArrayList alUserType=new ArrayList();
                   
                   alUserCode.add(txtUserCodeCaptain.getText().trim().toUpperCase());
                   alUserCode.add(txtUserCodeCashier.getText().trim().toUpperCase());
                   alUserCode.add(txtUserCodeManager.getText().trim().toUpperCase());
                   alUserCode.add(txtUserCodeOwner.getText().trim().toUpperCase());
                   
                   alPassword.add(txtPassCaptain.getText().trim().toUpperCase());
                   alPassword.add(txtPassCashier.getText().trim().toUpperCase());
                   alPassword.add(txtPassManager.getText().trim().toUpperCase());
                   alPassword.add(txtPassOwner.getText().trim().toUpperCase());
                   
                   alUserName.add(txtUserNameCaptain.getText().trim().toUpperCase());
                   alUserName.add(txtUserNameCashier.getText().trim().toUpperCase());
                   alUserName.add(txtUserNameManager.getText().trim().toUpperCase());
                   alUserName.add(txtUserNameOwner.getText().trim().toUpperCase());
                   
                   String[] captain = lblCaptain.getText().split(" ");
                   String[] cashier = lblCashier.getText().split(" ");
                   String[] manager = lblManager.getText().split(" ");
                   String[] owner = lblManager1.getText().split(" ");
                   
                   
                   alUserType.add(captain[0].toUpperCase());
                   alUserType.add(cashier[0].toUpperCase());
                   alUserType.add(manager[0].toUpperCase());
                   alUserType.add(owner[0].toUpperCase());
                  
                   String userCodeOwner = txtUserCodeOwner.getText().trim().toUpperCase();
                   String password = "";//
                   //String passwordOwner=txtPassOwner.getText().trim().toUpperCase();
                   
                   String userNameOwner=txtUserNameOwner.getText().trim();
                   if(alValidDates.size()==4&&alUserCode.size()==4&&alPassword.size()==4&&alUserName.size()==4)
                   {
                       
                       String sqlCheck="select a.strUserCode from tbluserhd a where a.strClientCode like '"+clsGlobalVarClass.gClientCode+"%'; ";
                       ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sqlCheck);
                       while(rs.next())
                       {
                           for(int i=0;i<alUserCode.size();i++)
                           {
                               if(alUserCode.get(i).toString().equalsIgnoreCase(rs.getString(1)))
                               {
                                    new frmOkPopUp(null, "User Already Exist ", "Error", 0).setVisible(true);
                                    return;
                               }
                           }
                       }
                    for(int i=0;i<alUserCode.size();i++)
                    {
			String userType="op";
                        if(alUserCode.get(i).toString().equalsIgnoreCase("Owner"))
                        {
                            userType="Super";
                        }
                        password = clsGlobalSingleObject.getObjPasswordEncryptDecreat().encrypt(encKey, alPassword.get(i).toString());

                        String query = "insert into tbluserhd values( ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? )";
                        PreparedStatement pre = clsGlobalVarClass.conPrepareStatement.prepareStatement(query);
                        pre.setString(1, alUserCode.get(i).toString());
                        pre.setString(2, alUserName.get(i).toString());
                        pre.setString(3, password);
                        pre.setString(4, userType);
                        pre.setString(5, alValidDates.get(i).toString());
                        pre.setString(6, selectedPOSCodes);
                        pre.setString(7, clsGlobalVarClass.gUserCode);
                        pre.setString(8, clsGlobalVarClass.gUserCode);
                        pre.setString(9, dteCreated);
                        pre.setString(10, dteEdited);
                        pre.setString(11, clsGlobalVarClass.gPropertyCode);
                        pre.setString(12, "N");
                        pre.setString(13, "");
                        pre.setString(14, "");
                        pre.setString(15, "");
                        pre.setString(16, " ");
                        pre.setString(17, alUserType.get(i).toString());
			pre.setInt(18, 0);
                        int cnt = pre.executeUpdate();
                        pre.close();
                    }
                   }
                


                   List listUserRights=new ArrayList<clsUserGroupRightsBean>();
                   clsUserGroupRightsBean obBean=new clsUserGroupRightsBean();

                   obBean.setStrFormName("Make KOT");
                   obBean.setGrant(true);
                   obBean.setEnableAuditing(true);
                   obBean.setTranAuthentication(false);
                   listUserRights.add(obBean);

                   obBean=new clsUserGroupRightsBean();
                   obBean.setStrFormName("Move KOT");
                   obBean.setGrant(true);
                   obBean.setEnableAuditing(true);
                   obBean.setTranAuthentication(false);
                   listUserRights.add(obBean);

                   obBean=new clsUserGroupRightsBean();
                   obBean.setStrFormName("Move KOT Items");
                   obBean.setGrant(true);
                   obBean.setEnableAuditing(true);
                   obBean.setTranAuthentication(false);
                   listUserRights.add(obBean);

                   obBean=new clsUserGroupRightsBean();
                   obBean.setStrFormName("Move Table");
                   obBean.setGrant(true);
                   obBean.setEnableAuditing(true);
                   obBean.setTranAuthentication(false);
                   listUserRights.add(obBean);

                   mapUserRights.put("Captain",listUserRights);

                   listUserRights=new ArrayList<clsUserGroupRightsBean>();
                   
                   obBean=new clsUserGroupRightsBean();
                   obBean.setStrFormName("Make Bill");
                   obBean.setGrant(true);
                   obBean.setEnableAuditing(true);
                   obBean.setTranAuthentication(false);
                   listUserRights.add(obBean);

                   obBean=new clsUserGroupRightsBean();
                   obBean.setStrFormName("Add KOT To Bill");
                   obBean.setGrant(true);
                   obBean.setEnableAuditing(true);
                   obBean.setTranAuthentication(false);
                   listUserRights.add(obBean);

                   obBean=new clsUserGroupRightsBean();
                   obBean.setStrFormName("AssignHomeDelivery");
                   obBean.setGrant(true);
                   obBean.setEnableAuditing(true);
                   obBean.setTranAuthentication(false);
                   listUserRights.add(obBean);

                   obBean=new clsUserGroupRightsBean();
                   obBean.setStrFormName("BillFromKOTs");
                   obBean.setGrant(true);
                   obBean.setEnableAuditing(true);
                   obBean.setTranAuthentication(false);
                   listUserRights.add(obBean);

                   obBean=new clsUserGroupRightsBean();
                   obBean.setStrFormName("ChangeCustomerOnBill");
                   obBean.setGrant(true);
                   obBean.setEnableAuditing(true);
                   obBean.setTranAuthentication(false);
                   listUserRights.add(obBean);

                   obBean=new clsUserGroupRightsBean();
                   obBean.setStrFormName("Credit Bill Receipt");
                   obBean.setGrant(true);
                   obBean.setEnableAuditing(true);
                   obBean.setTranAuthentication(false);
                   listUserRights.add(obBean);

                   obBean=new clsUserGroupRightsBean();
                   obBean.setStrFormName("Day End");
                   obBean.setGrant(true);
                   obBean.setEnableAuditing(true);
                   obBean.setTranAuthentication(false);
                   listUserRights.add(obBean);

                   obBean=new clsUserGroupRightsBean();
                   obBean.setStrFormName("Direct Biller");
                   obBean.setGrant(true);
                   obBean.setEnableAuditing(true);
                   obBean.setTranAuthentication(false);
                   listUserRights.add(obBean);

                   obBean=new clsUserGroupRightsBean();
                   obBean.setStrFormName("Discount On Bill");
                   obBean.setGrant(true);
                   obBean.setEnableAuditing(true);
                   obBean.setTranAuthentication(false);
                   listUserRights.add(obBean);

                   obBean=new clsUserGroupRightsBean();
                   obBean.setStrFormName("Modify Bill");
                   obBean.setGrant(true);
                   obBean.setEnableAuditing(true);
                   obBean.setTranAuthentication(false);
                   listUserRights.add(obBean);

                   obBean=new clsUserGroupRightsBean();
                   obBean.setStrFormName("Multi Bill Settle");
                   obBean.setGrant(true);
                   obBean.setEnableAuditing(true);
                   obBean.setTranAuthentication(false);
                   listUserRights.add(obBean);

                   obBean=new clsUserGroupRightsBean();
                   obBean.setStrFormName("SplitBill");
                   obBean.setGrant(true);
                   obBean.setEnableAuditing(true);
                   obBean.setTranAuthentication(false);
                   listUserRights.add(obBean);

                   obBean=new clsUserGroupRightsBean();
                   obBean.setStrFormName("Void Bill");
                   obBean.setGrant(true);
                   obBean.setEnableAuditing(true);
                   obBean.setTranAuthentication(false);
                   listUserRights.add(obBean);

                   obBean=new clsUserGroupRightsBean();
                   obBean.setStrFormName("VoidKot");
                   obBean.setGrant(true);
                   obBean.setEnableAuditing(true);
                   obBean.setTranAuthentication(false);
                   listUserRights.add(obBean);

                   obBean=new clsUserGroupRightsBean();
                   obBean.setStrFormName("Reprint");
                   obBean.setGrant(true);
                   obBean.setEnableAuditing(true);
                   obBean.setTranAuthentication(false);
                   listUserRights.add(obBean);

                   mapUserRights.put("Cashier", listUserRights);

                   listUserRights=new ArrayList<clsUserGroupRightsBean>();

                   obBean=new clsUserGroupRightsBean();
                   obBean.setStrFormName("Cash Management");
                   obBean.setGrant(true);
                   obBean.setEnableAuditing(true);
                   obBean.setTranAuthentication(false);
                   listUserRights.add(obBean);

                   obBean=new clsUserGroupRightsBean();
                   obBean.setStrFormName("Table Reservation");
                   obBean.setGrant(true);
                   obBean.setEnableAuditing(true);
                   obBean.setTranAuthentication(false);
                   listUserRights.add(obBean);

                   obBean=new clsUserGroupRightsBean();
                   obBean.setStrFormName("AssignHomeDelivery");
                   obBean.setGrant(true);
                   obBean.setEnableAuditing(true);
                   obBean.setTranAuthentication(false);
                   listUserRights.add(obBean);

                   obBean=new clsUserGroupRightsBean();
                   obBean.setStrFormName("NCKOT");
                   obBean.setGrant(true);
                   obBean.setEnableAuditing(true);
                   obBean.setTranAuthentication(false);
                   listUserRights.add(obBean);

                   obBean=new clsUserGroupRightsBean();
                   obBean.setStrFormName("Reprint");
                   obBean.setGrant(true);
                   obBean.setEnableAuditing(true);
                   obBean.setTranAuthentication(false);
                   listUserRights.add(obBean);

                   obBean=new clsUserGroupRightsBean();
                   obBean.setStrFormName("Change Settlement");
                   obBean.setGrant(true);
                   obBean.setEnableAuditing(true);
                   obBean.setTranAuthentication(false);
                   listUserRights.add(obBean);

                   obBean=new clsUserGroupRightsBean();
                   obBean.setStrFormName("Complimentry Settlement");
                   obBean.setGrant(true);
                   obBean.setEnableAuditing(true);
                   obBean.setTranAuthentication(false);
                   listUserRights.add(obBean);

                   obBean=new clsUserGroupRightsBean();
                   obBean.setStrFormName("JioMoney Refund");
                   obBean.setGrant(true);
                   obBean.setEnableAuditing(true);
                   obBean.setTranAuthentication(false);
                   listUserRights.add(obBean);

                   obBean=new clsUserGroupRightsBean();
                   obBean.setStrFormName("Unsettle Bill");
                   obBean.setGrant(true);
                   obBean.setEnableAuditing(true);
                   obBean.setTranAuthentication(false);
                   listUserRights.add(obBean);
                   for(int k=0;k<alReportModel.size();k++)
                   {
                        obBean=new clsUserGroupRightsBean();
                        obBean.setStrFormName(alReportModel.get(k).toString());
                        obBean.setGrant(true);
                        obBean.setEnableAuditing(true);
                        obBean.setTranAuthentication(false);
                        listUserRights.add(obBean);
                   }
                   mapUserRights.put("Manager", listUserRights);
                   
                   //mapModelImg
                   Set setFormNames=mapModelImg.keySet();
                   Iterator itr=setFormNames.iterator();
                   listUserRights=new ArrayList<clsUserGroupRightsBean>();
                   while(itr.hasNext())
                   {
                        obBean=new clsUserGroupRightsBean();
                        obBean.setStrFormName(itr.next().toString());
                        obBean.setGrant(true);
                        obBean.setEnableAuditing(false);
                        obBean.setTranAuthentication(true);
                        listUserRights.add(obBean);
                       
                   }
                  
                   mapUserRights.put("Owner", listUserRights);
                   int sequence=0;
                   String sql="";
                   if(mapUserRights.size()>0)
                   {
                    // insert Captain data
                        listUserRights=mapUserRights.get("Captain");
                        for(int i=0;i<listUserRights.size();i++)
                        {
                            clsUserGroupRightsBean obRights=(clsUserGroupRightsBean)listUserRights.get(i);
                            sequence++;
                            
                                moduleName = obRights.getStrFormName();
                                boolean grant = obRights.isGrant();
                                boolean isTLA = obRights.isTranAuthentication();
                                boolean isAudit = obRights.isEnableAuditing();
                                if (grant==true || isTLA==true || isAudit==true)
                                {
                                    sql = "insert into tbluserdtl values('" + alUserCode.get(0).toString() + "','" + moduleName + "'"
                                            + ",'" + mapModelImg.get(moduleName) + "'," + sequence + ",'" + grant + "','" + grant + "'"
                                            + ",'" + grant + "','" + grant + "','" + grant + "','" + grant + "','" + grant + "','" + isTLA + "','" + isAudit + "')";
                                    clsGlobalVarClass.dbMysql.execute(sql);
                                }
                            //}
                        }
                        // insert Cashier data
                        listUserRights=mapUserRights.get("Cashier");
                        for(int i=0;i<listUserRights.size();i++)
                        {
                            clsUserGroupRightsBean obRights=(clsUserGroupRightsBean)listUserRights.get(i);
                            sequence++;
                            
                                moduleName = obRights.getStrFormName();
                                boolean grant = obRights.isGrant();
                                boolean isTLA = obRights.isTranAuthentication();
                                boolean isAudit = obRights.isEnableAuditing();
                                if (grant==true || isTLA==true || isAudit==true)
                                {
                                    sql = "insert into tbluserdtl values('" + alUserCode.get(1).toString() + "','" + moduleName + "'"
                                            + ",'" + mapModelImg.get(moduleName) + "'," + sequence + ",'" + grant + "','" + grant + "'"
                                            + ",'" + grant + "','" + grant + "','" + grant + "','" + grant + "','" + grant + "','" + isTLA + "','" + isAudit + "')";
                                    clsGlobalVarClass.dbMysql.execute(sql);
                                }
                            //}
                        }
                  
                        // insert Manager data
                        listUserRights=mapUserRights.get("Manager");
                        for(int i=0;i<listUserRights.size();i++)
                        {
                            clsUserGroupRightsBean obRights=(clsUserGroupRightsBean)listUserRights.get(i);
                            sequence++;
                            
                                moduleName = obRights.getStrFormName();
                                boolean grant = obRights.isGrant();
                                boolean isTLA = obRights.isTranAuthentication();
                                boolean isAudit = obRights.isEnableAuditing();
                                if (grant==true || isTLA==true || isAudit==true)
                                {
                                    sql = "insert into tbluserdtl values('" + alUserCode.get(2).toString() + "','" + moduleName + "'"
                                            + ",'" + mapModelImg.get(moduleName) + "'," + sequence + ",'" + grant + "','" + grant + "'"
                                            + ",'" + grant + "','" + grant + "','" + grant + "','" + grant + "','" + grant + "','" + isTLA + "','" + isAudit + "')";
                                    clsGlobalVarClass.dbMysql.execute(sql);
                                }
                            //}
                        }
                        
                        sequence=0;
                        listUserRights=mapUserRights.get("Owner");
                        for(int i=0;i<listUserRights.size();i++)
                        {
                            
                                clsUserGroupRightsBean obRights=(clsUserGroupRightsBean)listUserRights.get(i);
                                sequence++;
                                moduleName = obRights.getStrFormName();
                                boolean isTLA = obRights.isTranAuthentication();
                                boolean isAudit = obRights.isEnableAuditing();
                                sql = "insert into tblsuperuserdtl values('" + userCodeOwner
                                        + "','" + moduleName + "','" + mapModelImg.get(moduleName)
                                        + "'," + sequence + ",'true','true','true','true','true','true','true','" + isTLA + "','" + isAudit + "')";
                                clsGlobalVarClass.dbMysql.execute(sql);
                           
                        }
                            
                   }
                   new frmOkPopUp(this, "All Users are Created", "Successfull", 3).setVisible(true);
                   
                 //   clsGlobalVarClass.hmActiveForms.remove("User Group Rights");
            }
            catch(Exception e)
            {
                objUtility.funWriteErrorLog(e);
                e.printStackTrace();
            }
            
         
        
    }
    
    
    
    /**
     * This method is used to set data
     *
     * @param data
     */
  

    
   

    /**
     * This method is used to reset fields
     */
    private void funResetField() {
        try {
          //  txtUserCode.setText("");
           // txtDebitCardString.setText("");
            btnNew.setText("SAVE");
            btnNew.setMnemonic('s');
            
            txtUserCodeCashier.setText("");
            txtUserNameCashier.setText("");
            txtPassCashier.setText("");
            
            txtUserCodeCaptain.setText("");
            txtUserNameCaptain.setText("");
            txtPassCaptain.setText("");
            
            txtUserCodeManager.setText("");
            txtUserNameManager.setText("");
            txtPassManager.setText("");
            
            txtUserCodeOwner.setText("");
            txtUserNameOwner.setText("");
            txtPassOwner.setText("");

        } catch (Exception e) {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
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
        };  ;
        panelBody = new javax.swing.JPanel();
        lblFormName = new javax.swing.JLabel();
        btnReset = new javax.swing.JButton();
        btnNew = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        lblCashier = new javax.swing.JLabel();
        lblCaptain = new javax.swing.JLabel();
        lblManager = new javax.swing.JLabel();
        lblUCode1 = new javax.swing.JLabel();
        lblUCode2 = new javax.swing.JLabel();
        lblUCode3 = new javax.swing.JLabel();
        txtPassCaptain = new javax.swing.JPasswordField();
        txtPassCashier = new javax.swing.JPasswordField();
        txtPassManager = new javax.swing.JPasswordField();
        lblUCode4 = new javax.swing.JLabel();
        dteExpiryCaptain = new com.toedter.calendar.JDateChooser();
        dteExpiryCashier = new com.toedter.calendar.JDateChooser();
        dteExpiryManager = new com.toedter.calendar.JDateChooser();
        txtUserCodeCaptain = new javax.swing.JTextField();
        txtUserCodeCashier = new javax.swing.JTextField();
        txtUserCodeManager = new javax.swing.JTextField();
        txtUserNameCaptain = new javax.swing.JTextField();
        txtUserNameCashier = new javax.swing.JTextField();
        txtUserNameManager = new javax.swing.JTextField();
        lblManager1 = new javax.swing.JLabel();
        txtUserCodeOwner = new javax.swing.JTextField();
        txtUserNameOwner = new javax.swing.JTextField();
        txtPassOwner = new javax.swing.JPasswordField();
        dteExpiryOwner = new com.toedter.calendar.JDateChooser();

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
        lblformName.setText("- User Card");
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
        lblUserCode.setPreferredSize(new java.awt.Dimension(71, 30));
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

        panelLayout.setBackground(new java.awt.Color(255, 255, 255));
        panelLayout.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        panelLayout.setMinimumSize(new java.awt.Dimension(800, 570));
        panelLayout.setOpaque(false);
        panelLayout.setLayout(new java.awt.GridBagLayout());

        panelBody.setBackground(new java.awt.Color(255, 255, 255));
        panelBody.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        panelBody.setMinimumSize(new java.awt.Dimension(800, 570));
        panelBody.setOpaque(false);

        lblFormName.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        lblFormName.setForeground(new java.awt.Color(14, 7, 7));
        lblFormName.setText("User Group Rights");

        btnReset.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnReset.setForeground(new java.awt.Color(255, 255, 255));
        btnReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnReset.setText("RESET");
        btnReset.setToolTipText("Reset All Fields");
        btnReset.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnReset.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnReset.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnResetMouseClicked(evt);
            }
        });
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });

        btnNew.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnNew.setForeground(new java.awt.Color(255, 255, 255));
        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnNew.setText("SAVE");
        btnNew.setToolTipText("Save Waiter");
        btnNew.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNew.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnNew.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnNewMouseClicked(evt);
            }
        });
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        btnNew.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnNewKeyPressed(evt);
            }
        });

        btnCancel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnCancel.setForeground(new java.awt.Color(255, 255, 255));
        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnCancel.setText("CLOSE");
        btnCancel.setToolTipText("Reset All Fields");
        btnCancel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCancel.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnCancel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCancelMouseClicked(evt);
            }
        });
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        lblCashier.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblCashier.setText("Cashier       :");

        lblCaptain.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblCaptain.setText("Captain     :");

        lblManager.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblManager.setText("Manager     :");

        lblUCode1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblUCode1.setText("User Code   ");

        lblUCode2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblUCode2.setText("User Name   ");

        lblUCode3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblUCode3.setText("Password   ");

        txtPassCaptain.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtPassCaptainMouseClicked(evt);
            }
        });
        txtPassCaptain.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPassCaptainKeyPressed(evt);
            }
        });

        txtPassCashier.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtPassCashierMouseClicked(evt);
            }
        });
        txtPassCashier.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPassCashierKeyPressed(evt);
            }
        });

        txtPassManager.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtPassManagerMouseClicked(evt);
            }
        });
        txtPassManager.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPassManagerKeyPressed(evt);
            }
        });

        lblUCode4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblUCode4.setText("Expiry Date    ");

        dteExpiryCaptain.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                dteExpiryCaptainKeyPressed(evt);
            }
        });

        dteExpiryCashier.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                dteExpiryCashierKeyPressed(evt);
            }
        });

        dteExpiryManager.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                dteExpiryManagerKeyPressed(evt);
            }
        });

        txtUserCodeCaptain.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtUserCodeCaptainMouseClicked(evt);
            }
        });
        txtUserCodeCaptain.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtUserCodeCaptainKeyPressed(evt);
            }
        });

        txtUserCodeCashier.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtUserCodeCashierMouseClicked(evt);
            }
        });
        txtUserCodeCashier.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtUserCodeCashierKeyPressed(evt);
            }
        });

        txtUserCodeManager.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtUserCodeManagerMouseClicked(evt);
            }
        });
        txtUserCodeManager.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtUserCodeManagerKeyPressed(evt);
            }
        });

        txtUserNameCaptain.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtUserNameCaptainMouseClicked(evt);
            }
        });
        txtUserNameCaptain.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtUserNameCaptainKeyPressed(evt);
            }
        });

        txtUserNameCashier.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtUserNameCashierMouseClicked(evt);
            }
        });
        txtUserNameCashier.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtUserNameCashierKeyPressed(evt);
            }
        });

        txtUserNameManager.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtUserNameManagerMouseClicked(evt);
            }
        });
        txtUserNameManager.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtUserNameManagerKeyPressed(evt);
            }
        });

        lblManager1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblManager1.setText("Owner        :");

        txtUserCodeOwner.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtUserCodeOwnerMouseClicked(evt);
            }
        });
        txtUserCodeOwner.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtUserCodeOwnerKeyPressed(evt);
            }
        });

        txtUserNameOwner.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtUserNameOwnerMouseClicked(evt);
            }
        });
        txtUserNameOwner.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtUserNameOwnerKeyPressed(evt);
            }
        });

        txtPassOwner.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPassOwnerActionPerformed(evt);
            }
        });
        txtPassOwner.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPassOwnerKeyPressed(evt);
            }
        });

        dteExpiryOwner.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                dteExpiryOwnerKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout panelBodyLayout = new javax.swing.GroupLayout(panelBody);
        panelBody.setLayout(panelBodyLayout);
        panelBodyLayout.setHorizontalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblManager, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblCaptain, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblCashier, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(47, 47, 47)
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtUserCodeCaptain)
                            .addComponent(txtUserCodeManager)
                            .addComponent(txtUserCodeCashier, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addComponent(lblManager1, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtUserCodeOwner, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addGap(152, 152, 152)
                .addComponent(lblUCode1)
                .addGap(78, 78, 78)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addComponent(lblFormName, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblUCode2)
                            .addComponent(txtUserNameOwner, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtUserNameManager, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtUserNameCashier, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtUserNameCaptain, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBodyLayout.createSequentialGroup()
                                .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(20, 20, 20)
                                .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(35, 35, 35))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBodyLayout.createSequentialGroup()
                                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtPassOwner, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblUCode3)
                                    .addComponent(txtPassManager, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtPassCaptain, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtPassCashier, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(34, 34, 34)
                                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(lblUCode4)
                                        .addComponent(dteExpiryManager, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(dteExpiryCaptain, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(dteExpiryCashier, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(dteExpiryOwner, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(26, 26, 26))))))
        );
        panelBodyLayout.setVerticalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addGap(54, 54, 54)
                .addComponent(lblFormName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42, 42, 42)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblUCode3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblUCode4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblUCode1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblUCode2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dteExpiryOwner, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtUserCodeOwner, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblManager1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtUserNameOwner, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtPassOwner, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(25, 25, 25)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtUserNameManager, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtPassManager, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtUserCodeManager, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addComponent(lblManager, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(dteExpiryManager, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(dteExpiryCashier, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtUserCodeCashier, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblCashier, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtUserNameCashier, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtPassCashier, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(30, 30, 30)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblCaptain, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtUserCodeCaptain, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtUserNameCaptain, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtPassCaptain, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 49, Short.MAX_VALUE)
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(66, 66, 66))
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addComponent(dteExpiryCaptain, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        panelLayout.add(panelBody, new java.awt.GridBagConstraints());

        getContentPane().add(panelLayout, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
        clsGlobalVarClass.hmActiveForms.remove("User Group Rights");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        clsGlobalVarClass.hmActiveForms.remove("User Group Rights");
    }//GEN-LAST:event_formWindowClosing

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        dispose();
        clsGlobalVarClass.hmActiveForms.remove("User Group Rights");
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnCancelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseClicked
        // TODO add your handling code here:
        dispose();
        clsGlobalVarClass.hmActiveForms.remove("User Group Rights");
    }//GEN-LAST:event_btnCancelMouseClicked

    private void btnNewKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnNewKeyPressed
        // TODO add your handling code here:
        

    }//GEN-LAST:event_btnNewKeyPressed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        
        funSaveUserGroupRights();
        //funResetField();

    }//GEN-LAST:event_btnNewActionPerformed

    private void btnNewMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNewMouseClicked
        // TODO add your handling code here:
        //funUpdateWaiter();

    }//GEN-LAST:event_btnNewMouseClicked

    private void txtPassCaptainKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPassCaptainKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPassCaptainKeyPressed

    private void txtPassCashierKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPassCashierKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPassCashierKeyPressed

    private void txtPassManagerKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPassManagerKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPassManagerKeyPressed

    private void dteExpiryCaptainKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dteExpiryCaptainKeyPressed
        // TODO add your handling code here:
       
    }//GEN-LAST:event_dteExpiryCaptainKeyPressed

    private void dteExpiryCashierKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dteExpiryCashierKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_dteExpiryCashierKeyPressed

    private void dteExpiryManagerKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dteExpiryManagerKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_dteExpiryManagerKeyPressed

    private void txtUserCodeCaptainMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtUserCodeCaptainMouseClicked
        // TODO add your handling code here:
        try
        {
            if (txtUserCodeCaptain.getText().length() == 0)
            {
                new frmAlfaNumericKeyBoard(this, true, "1", "Enter User Code for Captain").setVisible(true);
                txtUserCodeCaptain.setText(clsGlobalVarClass.gKeyboardValue);
            }
            else
            {
                new frmAlfaNumericKeyBoard(this, true, txtUserCodeCaptain.getText(), "1", "Enter User Code  for Captain").setVisible(true);
                txtUserCodeCaptain.setText(clsGlobalVarClass.gKeyboardValue);
            }

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_txtUserCodeCaptainMouseClicked

    private void txtUserCodeCaptainKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUserCodeCaptainKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
           // dteValid.requestFocus();
        }
    }//GEN-LAST:event_txtUserCodeCaptainKeyPressed

    private void txtUserCodeCashierMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtUserCodeCashierMouseClicked
        // TODO add your handling code here:
          try
        {
            if (txtUserCodeCashier.getText().length() == 0)
            {
                new frmAlfaNumericKeyBoard(this, true, "1", "Enter User Code for Cashier").setVisible(true);
                txtUserCodeCashier.setText(clsGlobalVarClass.gKeyboardValue);
            }
            else
            {
                new frmAlfaNumericKeyBoard(this, true, txtUserCodeCashier.getText(), "1", "Enter User Code for Cashier").setVisible(true);
                txtUserCodeCashier.setText(clsGlobalVarClass.gKeyboardValue);
            }

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_txtUserCodeCashierMouseClicked

    private void txtUserCodeCashierKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUserCodeCashierKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUserCodeCashierKeyPressed

    private void txtUserCodeManagerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtUserCodeManagerMouseClicked
        // TODO add your handling code here:
          try
        {
            if (txtUserCodeManager.getText().length() == 0)
            {
                new frmAlfaNumericKeyBoard(this, true, "1", "Enter User Code for Owner").setVisible(true);
                txtUserCodeManager.setText(clsGlobalVarClass.gKeyboardValue);
            }
            else
            {
                new frmAlfaNumericKeyBoard(this, true, txtUserCodeManager.getText(), "1", "Enter User Code for Owner").setVisible(true);
                txtUserCodeManager.setText(clsGlobalVarClass.gKeyboardValue);
            }

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_txtUserCodeManagerMouseClicked

    private void txtUserCodeManagerKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUserCodeManagerKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUserCodeManagerKeyPressed

    private void txtUserNameCaptainMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtUserNameCaptainMouseClicked
        // TODO add your handling code here:
         try
        {
            if (txtUserNameCaptain.getText().length() == 0)
            {
                new frmAlfaNumericKeyBoard(this, true, "1", "Enter User Name for Captain").setVisible(true);
                txtUserNameCaptain.setText(clsGlobalVarClass.gKeyboardValue);
            }
            else
            {
                new frmAlfaNumericKeyBoard(this, true, txtUserNameCaptain.getText(), "1", "Enter User Name for Captain").setVisible(true);
                txtUserNameCaptain.setText(clsGlobalVarClass.gKeyboardValue);
            }

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_txtUserNameCaptainMouseClicked

    private void txtUserNameCaptainKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUserNameCaptainKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUserNameCaptainKeyPressed

    private void txtUserNameCashierMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtUserNameCashierMouseClicked
        // TODO add your handling code here:
         try
        {
            if (txtUserNameCashier.getText().length() == 0)
            {
                new frmAlfaNumericKeyBoard(this, true, "1", "Enter User Name for Cashier").setVisible(true);
                txtUserNameCashier.setText(clsGlobalVarClass.gKeyboardValue);
            }
            else
            {
                new frmAlfaNumericKeyBoard(this, true, txtUserNameCashier.getText(), "1", "Enter User Name for Cashier").setVisible(true);
                txtUserNameCashier.setText(clsGlobalVarClass.gKeyboardValue);
            }

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_txtUserNameCashierMouseClicked

    private void txtUserNameCashierKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUserNameCashierKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUserNameCashierKeyPressed

    private void txtUserNameManagerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtUserNameManagerMouseClicked
        // TODO add your handling code here:
         try
        {
            if (txtUserNameManager.getText().length() == 0)
            {
                new frmAlfaNumericKeyBoard(this, true, "1", "Enter User Name for Manager").setVisible(true);
                txtUserNameManager.setText(clsGlobalVarClass.gKeyboardValue);
            }
            else
            {
                new frmAlfaNumericKeyBoard(this, true, txtUserNameManager.getText(), "1", "Enter User Name for Manager").setVisible(true);
                txtUserNameManager.setText(clsGlobalVarClass.gKeyboardValue);
            }

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_txtUserNameManagerMouseClicked

    private void txtUserNameManagerKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUserNameManagerKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUserNameManagerKeyPressed

    private void txtUserCodeOwnerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtUserCodeOwnerMouseClicked
        // TODO add your handling code here:
        try
        {
            if (txtUserCodeOwner.getText().length() == 0)
            {
                new frmAlfaNumericKeyBoard(this, true, "1", "Enter User Code for Owner").setVisible(true);
                txtUserCodeOwner.setText(clsGlobalVarClass.gKeyboardValue);
            }
            else
            {
                new frmAlfaNumericKeyBoard(this, true, txtUserCodeOwner.getText(), "1", "Enter User Code for Owner").setVisible(true);
                txtUserCodeOwner.setText(clsGlobalVarClass.gKeyboardValue);
            }

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_txtUserCodeOwnerMouseClicked

    private void txtUserCodeOwnerKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUserCodeOwnerKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUserCodeOwnerKeyPressed

    private void txtUserNameOwnerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtUserNameOwnerMouseClicked
        // TODO add your handling code here:
         try
        {
            if (txtUserNameOwner.getText().length() == 0)
            {
                new frmAlfaNumericKeyBoard(this, true, "1", "Enter User Name for Owner").setVisible(true);
                txtUserNameOwner.setText(clsGlobalVarClass.gKeyboardValue);
            }
            else
            {
                new frmAlfaNumericKeyBoard(this, true, txtUserNameOwner.getText(), "1", "Enter User Name for Owner").setVisible(true);
                txtUserNameOwner.setText(clsGlobalVarClass.gKeyboardValue);
            }

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_txtUserNameOwnerMouseClicked

    private void txtUserNameOwnerKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUserNameOwnerKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUserNameOwnerKeyPressed

    private void txtPassOwnerKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPassOwnerKeyPressed
        // TODO add your handling code here:
          try
        {
            if (txtPassOwner.getText().length() == 0)
            {
                new frmAlfaNumericKeyBoard(this, true, "1", "Enter Password for Owner").setVisible(true);
                txtPassOwner.setText(clsGlobalVarClass.gKeyboardValue);
            }
            else
            {
                new frmAlfaNumericKeyBoard(this, true, txtPassOwner.getText(), "1", "Enter Password for Owner").setVisible(true);
                txtPassOwner.setText(clsGlobalVarClass.gKeyboardValue);
            }

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_txtPassOwnerKeyPressed

    private void dteExpiryOwnerKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dteExpiryOwnerKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_dteExpiryOwnerKeyPressed

    private void txtPassOwnerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPassOwnerActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPassOwnerActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        // TODO add your handling code here:
        funResetField();
    }//GEN-LAST:event_btnResetActionPerformed

    private void btnResetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnResetMouseClicked
        // TODO add your handling code here:

        funResetField();
    }//GEN-LAST:event_btnResetMouseClicked

    private void txtPassManagerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPassManagerMouseClicked
        // TODO add your handling code here:
          try
        {
            if (txtPassManager.getText().length() == 0)
            {
                new frmAlfaNumericKeyBoard(this, true, "1", "Enter Password for Manager").setVisible(true);
                txtPassManager.setText(clsGlobalVarClass.gKeyboardValue);
            }
            else
            {
                new frmAlfaNumericKeyBoard(this, true, txtPassManager.getText(), "1", "Enter Password for Manager").setVisible(true);
                txtPassManager.setText(clsGlobalVarClass.gKeyboardValue);
            }

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_txtPassManagerMouseClicked

    private void txtPassCashierMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPassCashierMouseClicked
        // TODO add your handling code here:
          try
        {
            if (txtPassCashier.getText().length() == 0)
            {
                new frmAlfaNumericKeyBoard(this, true, "1", "Enter Password for Cashier").setVisible(true);
                txtPassCashier.setText(clsGlobalVarClass.gKeyboardValue);
            }
            else
            {
                new frmAlfaNumericKeyBoard(this, true, txtPassCashier.getText(), "1", "Enter Password for Cashier").setVisible(true);
                txtPassCashier.setText(clsGlobalVarClass.gKeyboardValue);
            }

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_txtPassCashierMouseClicked

    private void txtPassCaptainMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPassCaptainMouseClicked
        // TODO add your handling code here:
         try
        {
            if (txtPassCaptain.getText().length() == 0)
            {
                new frmAlfaNumericKeyBoard(this, true, "1", "Enter Password for Captain").setVisible(true);
                txtPassCaptain.setText(clsGlobalVarClass.gKeyboardValue);
            }
            else
            {
                new frmAlfaNumericKeyBoard(this, true, txtPassCaptain.getText(), "1", "Enter Password for Captain").setVisible(true);
                txtPassCaptain.setText(clsGlobalVarClass.gKeyboardValue);
            }

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_txtPassCaptainMouseClicked

    /**
     * @param args the command line arguments
     */
    


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnReset;
    private com.toedter.calendar.JDateChooser dteExpiryCaptain;
    private com.toedter.calendar.JDateChooser dteExpiryCashier;
    private com.toedter.calendar.JDateChooser dteExpiryManager;
    private com.toedter.calendar.JDateChooser dteExpiryOwner;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JLabel lblCaptain;
    private javax.swing.JLabel lblCashier;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblFormName;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblManager;
    private javax.swing.JLabel lblManager1;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblUCode1;
    private javax.swing.JLabel lblUCode2;
    private javax.swing.JLabel lblUCode3;
    private javax.swing.JLabel lblUCode4;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblformName;
    private javax.swing.JPanel panelBody;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelLayout;
    private javax.swing.JPasswordField txtPassCaptain;
    private javax.swing.JPasswordField txtPassCashier;
    private javax.swing.JPasswordField txtPassManager;
    private javax.swing.JPasswordField txtPassOwner;
    private javax.swing.JTextField txtUserCodeCaptain;
    private javax.swing.JTextField txtUserCodeCashier;
    private javax.swing.JTextField txtUserCodeManager;
    private javax.swing.JTextField txtUserCodeOwner;
    private javax.swing.JTextField txtUserNameCaptain;
    private javax.swing.JTextField txtUserNameCashier;
    private javax.swing.JTextField txtUserNameManager;
    private javax.swing.JTextField txtUserNameOwner;
    // End of variables declaration//GEN-END:variables
}
