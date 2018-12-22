/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSTransaction.view;

/**
 *
 * @author Prashant
 */
import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsPosConfigFile;
import com.POSGlobal.controller.clsUtility;
import com.POSGlobal.view.frmClearTransaction;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class frmImportDatabase extends javax.swing.JFrame 
{
    private int cnt;
    private String dbURL;
    static String unicode= "?useUnicode=yes&characterEncoding=UTF-8";
    private String connectDatabase="N";
    public frmImportDatabase()
    {
        initComponents();
           lblUserCode.setText(clsGlobalVarClass.gUserCode);
           lblPosName.setText(clsGlobalVarClass.gPOSName);
           lblDate.setText(clsGlobalVarClass.gPOSDateToDisplay);
           lblModuleName1.setText(clsGlobalVarClass.gSelectedModule);
    }

       public Connection funOpenMSSQLConnection(String dbType,String database) throws Exception
    {
        Connection dbCon=null;
        if(dbType.equalsIgnoreCase("mssql"))
        {
            
            String dbName=txtDatabaseName.getText();
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            dbURL="jdbc:sqlserver://"+txtIPAddress.getText()+":"+txtPortNo.getText()+";user="+txtUserName.getText()+";password="+txtPassword.getText()+";database="+dbName;
            dbCon = DriverManager.getConnection(dbURL);
            
        }
        
        return dbCon;
    }
       
  private int funFillTempTable()
  {
      Connection con=null;
      Statement st=null;
      int res=0;
      String sql="";
       Date objDate = new Date();
       int day = objDate.getDate();
       int month = objDate.getMonth() + 1;
       int year = objDate.getYear() + 1900;
       String currentDate = year + "-" + month + "-" + day;
        try
	   {
		con=funOpenMSSQLConnection("mssql","trans");
		st = con.createStatement();
                clsGlobalVarClass.dbMysql.execute("truncate table tblimportexcel");
                sql="select b.MenuItemDescription,'' ShortName,c.MenuHeadDescription,'' SubMenuHeadName"
                    + ",h.IncomeHeadDescription,d.POSDescription,f.MenuSubGroupDescription"
                    + ",g.MenuGroupDescription,e.CostCenterDescription,'All' Area"
                    + ",'' TaxIndicator,'0.00' PurchaseRate,'' ExternalCode,'' ItemDetails"
                    + ",'Food' ItemType,'Y' ApplyDiscount,'Y' StockInEnable"
                     + ",a.Sun,a.Mon,a.Tue,a.Wed,a.Thr,a.Fri,a.Sat,'' Counter\n" +
                     " from TblRateMst a,TblMenuItemMst b,TblMenuHeadMst c,TblPosMst d,TblCostCenterMst e,TblMenuSubGroupMst f,TblMenuGroupMst g,TblIncomeHeadMst h\n" +
                     " where a.MenuItemCode=b.MenuItemCode and a.MenuHeadCode=c.MenuHeadCode and c.POSCode=d.POSCode\n" +
                     " and a.POSCode=d.POSCode and a.CostCenterCode=e.CostCenterCode and e.POSCode=d.POSCode\n" +
                     " and b.MenuSubGroupCode=f.MenuSubGroupCode and f.MenuGroupCode=g.MenuGroupCode and b.IncomeHeadCode=h.IncomeHeadCode "
                     + " AND '"+currentDate+"' between a.FromDate and a.ToDate;";
                
                System.out.println(sql);
                ResultSet rs=st.executeQuery(sql);
                while(rs.next())
                {
                    String menuItemName=rs.getString(1).replaceAll("'", "");
                    String menuGroupName=rs.getString(8).replaceAll("'", "");
                    String costCenterDesc=rs.getString(9).replaceAll("'", "");
                    String menuHeadName=rs.getString(3).replaceAll("'", "");
                    String posName=rs.getString(6).replaceAll("'", "");
                    String subGroupName=rs.getString(7).replaceAll("'", "");
                    String incomeHeadDesc=rs.getString(5).replaceAll("'", "");                    
                                        
                      sql="insert into tblimportexcel (strItemName,strShortName,strMenuHeadName,strSubMenuHeadName"
                        + ",strRevenueHead,strPOSName"
                        + ",strSubGroupName,strGroupName,strCostCenterName,strAreaName,dblTax,dblPurchaseRate"
                        + ",strExternalCode,strItemDetails,strItemType,strApplyDiscount,strStockInEnable"
                        + ",dblPriceSunday,dblPriceMonday,dblPriceTuesday,dblPriceWednesday,dblPriceThursday"
                        + ",dblPriceFriday,dblPriceSaturday,strCounterName) values("
                        + "'"+menuItemName+"','"+rs.getString(2)+"','"+menuHeadName+"',"
                        + "'"+rs.getString(4)+"','"+incomeHeadDesc+"','"+posName+"',"
                        + "'"+subGroupName+"','"+menuGroupName+"','"+costCenterDesc+"',"
                        + "'"+rs.getString(10)+"','"+rs.getString(11)+"','"+rs.getString(12)+"',"
                        + "'"+rs.getString(13)+"','"+rs.getString(14)+"','"+rs.getString(15)+"',"
                        + "'"+rs.getString(16)+"','"+rs.getString(17)+"','"+rs.getDouble(18)+"',"
                        + "'"+rs.getDouble(19)+"','"+rs.getDouble(20)+"','"+rs.getDouble(21)+"',"
                        + "'"+rs.getDouble(22)+"','"+rs.getDouble(23)+"','"+rs.getDouble(24)+"','"+rs.getString(25)+"')";
                      
                       System.out.println(sql);
                      clsGlobalVarClass.dbMysql.execute(sql);
                      
                      
                    
                }
                rs.close();
                st.close();
                con.close();
               res=1;
                
                
           } 
        catch(Exception e)
        {
            res=0;
            e.printStackTrace();
        }
        finally
          {
             return res;
              
          }
      
  }
       
    
          
        public boolean funGenerateCode()
        {
            boolean flgReturn=false;
           
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
                flgReturn=funGenerateMenuItemPriceDTL();
            
         
            return flgReturn;
        }
        
 
         private boolean funGenerateGroup()
        {
            boolean flgReturn=false;
            String query="",code="";
            long docNo=0;
            try
            {
                //clsGlobalVarClass.dbMysql.execute("truncate table tblgrouphd");
                String sql="select distinct(strGroupName) from tblimportexcel";
                ResultSet rsGroup=clsGlobalVarClass.dbMysql.executeResultSet(sql);
                while(rsGroup.next())
                {
                    
                    if(rsGroup.getString(1).trim().length()>0)
                    {
                        docNo++;
                        code = "G" + String.format("%07d",docNo);
                        query="insert into tblgrouphd (strGroupCode,strGroupName,strUserCreated,"
                                + "strUserEdited,dteDateCreated,dteDateEdited,strClientCode)"
                                + "values('"+code+"','"+rsGroup.getString(1)+"','"+clsGlobalVarClass.gUserCode+"',"
                                + "'"+clsGlobalVarClass.gUserCode+"','"+clsGlobalVarClass.getCurrentDateTime()+"',"
                                + "'"+clsGlobalVarClass.getCurrentDateTime()+"','"+clsGlobalVarClass.gClientCode+"')";
                        int insert=clsGlobalVarClass.dbMysql.execute(query);
                        if(insert==1)
                        {
                            query="update tblimportexcel set strGroupCode='"+code+"' "
                                    + "where strGroupName='"+rsGroup.getString(1)+"'";
                            int update=clsGlobalVarClass.dbMysql.execute(query);
                        }
                    }
                }
                rsGroup.close();
                flgReturn=true;
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
            }*/
            catch(Exception e)
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
            boolean flgReturn=false;
            String query="",code="";
            long docNo=0;
            try
            {
                //clsGlobalVarClass.dbMysql.execute("truncate table tblsubgrouphd");
                String sql="select distinct(strSubGroupName),strGroupCode from tblimportexcel";
                ResultSet rsSubGroup=clsGlobalVarClass.dbMysql.executeResultSet(sql);
                while(rsSubGroup.next())
                {
                    if(rsSubGroup.getString(1).trim().length()>0)
                    {
                        docNo++;
                        code = "SG" + String.format("%07d",docNo);
                        query="insert into tblsubgrouphd (strSubGroupCode,strSubGroupName,strGroupCode,"
                                + "strUserCreated,strUserEdited,dteDateCreated,dteDateEdited,strClientCode)"
                                + "values('"+code+"','"+rsSubGroup.getString(1)+"','"+rsSubGroup.getString(2)+"',"
                                + "'"+clsGlobalVarClass.gUserCode+"','"+clsGlobalVarClass.gUserCode+"',"
                                + "'"+clsGlobalVarClass.getCurrentDateTime()+"','"
                                +clsGlobalVarClass.getCurrentDateTime()+"','"+clsGlobalVarClass.gClientCode+"')";
                        int insert=clsGlobalVarClass.dbMysql.execute(query);
                        if(insert==1)
                        {
                            query="update tblimportexcel set strSubGroupCode='"+code+"' "
                                    + "where strSubGroupName='"+rsSubGroup.getString(1)+"'";
                            int update=clsGlobalVarClass.dbMysql.execute(query);
                        }
                    }
                }
                rsSubGroup.close();
                flgReturn=true;
                
            }catch(Exception e)
            {
                flgReturn=false;
                e.printStackTrace();
            }
            finally
            {
                return flgReturn;
            }
        }
        
        
        private boolean funGenerateCostCenter()
        {
            boolean flgReturn=false;
            String query="",code="";
            long docNo=0;
            try
            {
                //clsGlobalVarClass.dbMysql.execute("truncate table tblcostcentermaster");
                String sql="select distinct(strCostCenterName) from tblimportexcel";
                ResultSet rsCostCenter=clsGlobalVarClass.dbMysql.executeResultSet(sql);
                while(rsCostCenter.next())
                {
                    
                    
                    if(rsCostCenter.getString(1).trim().length()>0)
                    {
                        docNo++;
                        code = "C" + String.format("%02d",docNo);
                        query="insert into tblCostCenterMaster (strCostCenterCode,strCostCenterName,strPrinterPort"
                            + ",strSecondaryPrinterPort,strUserCreated,strUserEdited,dteDateCreated,dteDateEdited"
                            + ",strClientCode,strDataPostFlag)"
                            + " values('"+code+"','"+rsCostCenter.getString(1)+"','','','"+clsGlobalVarClass.gUserCode+"',"
                            + "'"+clsGlobalVarClass.gUserCode+"','"+clsGlobalVarClass.getCurrentDateTime()+"',"
                            + "'"+clsGlobalVarClass.getCurrentDateTime()+"','"+clsGlobalVarClass.gClientCode+"','N')";
                        int insert=clsGlobalVarClass.dbMysql.execute(query);
                        if(insert==1)
                        {
                            query="update tblimportexcel set strCostCenterCode='"+code+"' "
                                    + "where strCostCenterName='"+rsCostCenter.getString(1)+"'";
                            int update=clsGlobalVarClass.dbMysql.execute(query);
                        }
                    }
                }
                rsCostCenter.close();
                flgReturn=true;
                
            }catch(Exception e)
            {
                flgReturn=false;
                e.printStackTrace();
            }
            finally
            {
                return flgReturn;
            }
        }
        
        
        private boolean funGenerateCounterMasterHd()
        {
            boolean flgReturn=false;
            String query="",code="";
            long docNo=0;
            try
            {
                //clsGlobalVarClass.dbMysql.execute("truncate table tblcostcentermaster");
                String sql="select distinct(strCounterName) from tblimportexcel";
                ResultSet rsCounter=clsGlobalVarClass.dbMysql.executeResultSet(sql);
                while(rsCounter.next())
                {
                    
                    
                    if(rsCounter.getString(1).trim().length()>0)
                    {
                        docNo++;
                        code = "C" + String.format("%02d",docNo);
                        query="insert into tblcounterhd (strCounterCode,strCounterName,strPOSCode,"
                            + "strUserCreated,strUserEdited,dteDateCreated,dteDateEdited,strClientCode,strDataPostFlag,strOperational)"
                            + " values('"+code+"','"+rsCounter.getString(1)+"','"+clsGlobalVarClass.gPOSCode+"','"+clsGlobalVarClass.gUserCode+"',"
                            + "'"+clsGlobalVarClass.gUserCode+"','"+clsGlobalVarClass.getCurrentDateTime()+"',"
                            + "'"+clsGlobalVarClass.getCurrentDateTime()+"','"+clsGlobalVarClass.gClientCode+"','N','Yes')";
                        int insert=clsGlobalVarClass.dbMysql.execute(query);
                        if(insert==1)
                        {
                            query="update tblimportexcel set strCounterCode='"+code+"' "
                                    + "where strCounterName='"+rsCounter.getString(1)+"'";
                            int update=clsGlobalVarClass.dbMysql.execute(query);
                        }
                    }
                }
                rsCounter.close();
                flgReturn=true;
                
            }catch(Exception e)
            {
                flgReturn=false;
                e.printStackTrace();
            }
            finally
            {
                return flgReturn;
            }
        }
        
        private boolean funGenerateCounterMasterDtl()
        {
            boolean flgReturn=false;
            String query="";
            try
            {
                //clsGlobalVarClass.dbMysql.execute("truncate table tblcostcentermaster");
                String sql="select distinct(strMenuHeadCode),strCounterCode from tblimportexcel order by strCounterCode";
                ResultSet rsCounter=clsGlobalVarClass.dbMysql.executeResultSet(sql);
                while(rsCounter.next())
                {
                    
                    
                    query="insert into tblcounterdtl (strCounterCode,strMenuCode,strClientCode)"
                        + " values('"+rsCounter.getString(2)+"','"+rsCounter.getString(1)+"','"+clsGlobalVarClass.gClientCode+"')";
                    int insert=clsGlobalVarClass.dbMysql.execute(query);
                }
                rsCounter.close();
                flgReturn=true;
                
            }catch(Exception e)
            {
                flgReturn=false;
                e.printStackTrace();
            }
            finally
            {
                return flgReturn;
            }
        }
        
        
        private boolean funGenerateMenuHead()
        {
            boolean flgReturn=false;
            String query="",code="";
            long docNo=0;
            try
            {
                //clsGlobalVarClass.dbMysql.execute("truncate table tblmenuhd");
                String sql="select distinct(strMenuHeadName) from tblimportexcel";
                ResultSet rsMenuHead=clsGlobalVarClass.dbMysql.executeResultSet(sql);
                while(rsMenuHead.next())
                {
                    
                    if(rsMenuHead.getString(1).trim().length()>0)
                    {
                        docNo++;
                        code = "M" + String.format("%06d",docNo);
                        query="insert into tblmenuhd (strMenuCode,strMenuName,strUserCreated,strUserEdited,"
                                + "dteDateCreated,dteDateEdited,strClientCode,strOperational) "
                            + "values('" +code+ "','"+rsMenuHead.getString(1)+ "','"+ clsGlobalVarClass.gUserCode + "'"
                            + ",'" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.getCurrentDateTime() + "'"
                            + ",'" + clsGlobalVarClass.getCurrentDateTime() + "','"+clsGlobalVarClass.gClientCode+"','Y' )";
                        int insert=clsGlobalVarClass.dbMysql.execute(query);
                        if(insert==1)
                        {
                            query="update tblimportexcel set strMenuHeadCode='"+code+"' "
                                    + "where strMenuHeadName='"+rsMenuHead.getString(1)+"'";
                            int update=clsGlobalVarClass.dbMysql.execute(query);
                        }
                    }
                    flgReturn=true;
                }
                rsMenuHead.close();
                
            }catch(Exception e)
            {
                flgReturn=false;
                e.printStackTrace();
            }
            finally
            {
                return flgReturn;
            }
        }
        
        
        private boolean funGeneratePOS()
        {
            boolean flgReturn=false;
            String query="",code="";
            long docNo=0;
            try
            {
                clsGlobalVarClass.dbMysql.execute("truncate table tblposmaster");
                String sql="select distinct(strPOSName) from tblimportexcel";
                ResultSet rsPOSMaster=clsGlobalVarClass.dbMysql.executeResultSet(sql);
                while(rsPOSMaster.next())
                {                    
                    if(rsPOSMaster.getString(1).trim().length()>0)
                    {                    
                        docNo++;
                        code = "P" + String.format("%02d",docNo);
                        query="insert into tblposmaster(strPosCode,strPosName,strPosType,strDebitCardTransactionYN,"
                            + "strPropertyPOSCode,strUserCreated,strUserEdited,dteDateCreated,dteDateEdited"
                            + ",strCounterWiseBilling,strPrintVatNo,strPrintServiceTaxNo,strVatNo,strServiceTaxNo) "
                            + "values('" +code+ "','" +rsPOSMaster.getString(1)+ "','Dine In','No',''"
                            + ",'"+clsGlobalVarClass.gUserCode+ "','"+clsGlobalVarClass.gUserCode+ "',"
                            + "'"+clsGlobalVarClass.getCurrentDateTime()+"','"+ clsGlobalVarClass.getCurrentDateTime() + "'"
                            + ",'No','N','N','','')";
                        int insert=clsGlobalVarClass.dbMysql.execute(query);
                        if(insert==1)
                        {
                            query="update tblimportexcel set strPOSCode='"+code+"' "
                                    + "where strPOSName='"+rsPOSMaster.getString(1)+"'";
                            int update=clsGlobalVarClass.dbMysql.execute(query);
                        }
                    }
                }
                rsPOSMaster.close();
                flgReturn=true;
                
            }catch(Exception e)
            {
                flgReturn=false;
                e.printStackTrace();
            }
            finally
            {
                return flgReturn;
            }
        }
        
        
        private boolean funGenerateItemMaster()
        {
            boolean flgReturn=false;
            String query="",code="",stkInEnable="N",purchaseRate="0.00";
            long docNo=0;
            try
            {
                //clsGlobalVarClass.dbMysql.execute("truncate table tblitemmaster");
                String sql="select distinct(strItemName),strSubGroupCode,strStockInEnable,dblPurchaseRate"
                            + ",strExternalCode,strItemDetails,strItemType,strApplyDiscount,strShortName,dblTax,strRevenueHead "
                            + "from tblimportexcel";
                ResultSet rsItemMaster=clsGlobalVarClass.dbMysql.executeResultSet(sql);
                while(rsItemMaster.next())
                {
                    if(rsItemMaster.getString(1).trim().length()>0)
                    {
                        if(rsItemMaster.getString(3).equals("Y"))
                        {
                            stkInEnable="Y";
                        }
                        if(rsItemMaster.getString(4).trim().length()==0)
                        {
                            purchaseRate="0.00";
                        }else
                        {
                            purchaseRate=rsItemMaster.getString(4);
                        }
                        docNo++;
                        code = "I" + String.format("%06d",docNo);
                        query="insert into tblitemmaster (strItemCode,strItemName,strSubGroupCode,strTaxIndicator"
                                + ",strStockInEnable,dblPurchaseRate,strExternalCode,strItemDetails,strUserCreated"
                                + ",strUserEdited,dteDateCreated,dteDateEdited,strClientCode,strItemType,strDiscountApply"
                                + ",strShortName,strRevenueHead)"
                                + " values('"+code+"','"+rsItemMaster.getString(1)+"','"+rsItemMaster.getString(2)+"'"
                                + ",'"+rsItemMaster.getString(10)+"','"+stkInEnable+"','"+purchaseRate+"','"+rsItemMaster.getString(5)+"'"
                                + ",'"+rsItemMaster.getString(6)+"','"+clsGlobalVarClass.gUserCode+"'"
                                + ",'"+clsGlobalVarClass.gUserCode+"','"+clsGlobalVarClass.getCurrentDateTime()+"'"
                                + ",'"+clsGlobalVarClass.getCurrentDateTime()+"','"+clsGlobalVarClass.gClientCode+"'"
                                + ",'"+rsItemMaster.getString(7)+"','"+rsItemMaster.getString(8)+"'"
                                + ",'"+rsItemMaster.getString(9)+"','"+rsItemMaster.getString(11)+"')";
                        int insert=clsGlobalVarClass.dbMysql.execute(query);
                        if(insert==1)
                        {
                            query="update tblimportexcel set strItemCode='"+code+"' "
                                    + "where strItemName='"+rsItemMaster.getString(1)+"'";
                            int update=clsGlobalVarClass.dbMysql.execute(query);
                        }
                    }
                }
                rsItemMaster.close();
                flgReturn=true;
                
            }catch(Exception e)
            {
                flgReturn=false;
                e.printStackTrace();
            }
            finally
            {
                return flgReturn;
            }
        }
        
        
        private boolean funGenerateItemMasterForRetail()
        {
            boolean flgReturn=false;
            String query="",code="",stkInEnable="N",purchaseRate="0.00",saleRate="0.00";
            long docNo=0;
            try
            {
                //clsGlobalVarClass.dbMysql.execute("truncate table tblitemmaster");
                String sql="select distinct(strItemName),strSubGroupCode,strStockInEnable,dblPurchaseRate"
                        + ",strExternalCode,strItemDetails,strItemType,strApplyDiscount,strShortName"
                        + ",dblPriceMonday,strRevenueHead "
                        + "from tblimportexcel";
                ResultSet rsItemMaster=clsGlobalVarClass.dbMysql.executeResultSet(sql);
                while(rsItemMaster.next())
                {
                  
                    if(rsItemMaster.getString(1).trim().length()>0)
                    {
                        if(rsItemMaster.getString(3).equals("Y"))
                        {
                            stkInEnable="Y";
                        }
                        if(rsItemMaster.getString(4).trim().length()==0)
                        {
                            purchaseRate="0.00";
                        }
                        if(rsItemMaster.getString(10).trim().length()==0)
                        {
                            saleRate="0.00";
                        }
                        else
                        {
                            saleRate=rsItemMaster.getString(10);
                        }
                        docNo++;
                        code = "I" + String.format("%06d",docNo);
                        query="insert into tblitemmaster (strItemCode,strItemName,strSubGroupCode,strTaxIndicator"
                                + ",strStockInEnable,dblPurchaseRate,strExternalCode,strItemDetails,strUserCreated"
                                + ",strUserEdited,dteDateCreated,dteDateEdited,strClientCode,strItemType,strDiscountApply"
                                + ",strShortName,dblSalePrice,strRevenueHead)"
                                + " values('"+code+"','"+rsItemMaster.getString(1)+"','"+rsItemMaster.getString(2)+"'"
                                + ",'','"+stkInEnable+"','"+purchaseRate+"','"+rsItemMaster.getString(5)+"'"
                                + ",'"+rsItemMaster.getString(6)+"','"+clsGlobalVarClass.gUserCode+"'"
                                + ",'"+clsGlobalVarClass.gUserCode+"','"+clsGlobalVarClass.getCurrentDateTime()+"'"
                                + ",'"+clsGlobalVarClass.getCurrentDateTime()+"','"+clsGlobalVarClass.gClientCode+"'"
                                + ",'"+rsItemMaster.getString(7)+"','"+rsItemMaster.getString(8)+"'"
                                + ",'"+rsItemMaster.getString(9)+"',"+saleRate+",'"+rsItemMaster.getString(11)+"')";
                        
                        System.out.println(query);
                        int insert=clsGlobalVarClass.dbMysql.execute(query);
                        if(insert==1)
                        {
                            query="update tblimportexcel set strItemCode='"+code+"' "
                                + "where strItemName='"+rsItemMaster.getString(1)+"'";
                            clsGlobalVarClass.dbMysql.execute(query);
                        }
                    }
                }
                rsItemMaster.close();
                flgReturn=true;
                
            }catch(Exception e)
            {
                flgReturn=false;
                e.printStackTrace();
            }
            finally
            {
                return flgReturn;
            }
        }
        
        
        private boolean funGenerateAreaMaster()
        {
            boolean flgReturn=false;
            String query="",code="";
            long docNo=0;
            try
            {
                //clsGlobalVarClass.dbMysql.execute("truncate table tblareamaster");
                //String sql="select distinct(strAreaName) from tblimportexcel where strAreaName!='All'";
                String sql="select distinct(strAreaName) from tblimportexcel";
                ResultSet rsAreaMaster=clsGlobalVarClass.dbMysql.executeResultSet(sql);
                while(rsAreaMaster.next())
                {
                    if(rsAreaMaster.getString(1).trim().length()>0)
                    {
                        docNo++;
                        code = "A" + String.format("%03d",docNo);
                        query="insert into tblareamaster (strAreaCode,strAreaName,strUserCreated,strUserEdited,"
                            + "dteDateCreated,dteDateEdited)"
                            + "values('"+code+"','"+rsAreaMaster.getString(1)+"'"
                            + ",'"+clsGlobalVarClass.gUserCode+"','"+clsGlobalVarClass.gUserCode+"'"
                            + ",'"+clsGlobalVarClass.getCurrentDateTime()+"','"+clsGlobalVarClass.getCurrentDateTime()+"')";
                        int insert=clsGlobalVarClass.dbMysql.execute(query);
                        if(insert==1)
                        {
                            query="update tblimportexcel set strAreaCode='"+code+"' "
                                + "where strAreaName='"+rsAreaMaster.getString(1)+"'";
                            clsGlobalVarClass.dbMysql.execute(query);
                        }
                    }
                }
                rsAreaMaster.close();
                query="update tblinternal set dblLastNo="+docNo+" where strTransactionType='Area'";
                clsGlobalVarClass.dbMysql.execute(query);
                
                flgReturn=true;
                
            }catch(Exception e)
            {
                flgReturn=false;
                e.printStackTrace();
            }
            finally
            {
                return flgReturn;
            }
        }
        
        private boolean funGenerateSubMenuHead()
        {
            boolean flgReturn=false;
            String query="",code="";
            long docNo=0;
            try
            {
                //clsGlobalVarClass.dbMysql.execute("truncate table tblsubmenuhead");
                String sql="select distinct(strSubMenuHeadName),strMenuHeadCode from tblimportexcel";
                ResultSet rsSubMenuHead=clsGlobalVarClass.dbMysql.executeResultSet(sql);
                while(rsSubMenuHead.next())
                {
                    if(rsSubMenuHead.getString(1).trim().length()>0)
                    {
                        docNo++;
                        code = "SM" + String.format("%06d",docNo);
                        query="insert into tblsubmenuhead (strSubMenuHeadCode,strMenuCode,strSubMenuHeadShortName,"
                               + "strSubMenuHeadName,strSubMenuOperational,strUserCreated,strUserEdited,dteDateCreated,"
                               + "dteDateEdited,strClientCode)"
                               + " values('"+code+"','"+rsSubMenuHead.getString(2)+"',''"
                               + ",'"+rsSubMenuHead.getString(1).trim()+"','Y','"+clsGlobalVarClass.gUserCode+"'"
                               + ",'"+clsGlobalVarClass.gUserCode+"','"+clsGlobalVarClass.getCurrentDateTime()+"'"
                               + ",'"+clsGlobalVarClass.getCurrentDateTime()+"','"+clsGlobalVarClass.gClientCode+"')";
                        int insert=clsGlobalVarClass.dbMysql.execute(query);
                        if(insert==1)
                        {
                            query="update tblimportexcel set strSubMenuHeadCode='"+code+"' "
                                    + "where strSubMenuHeadName='"+rsSubMenuHead.getString(1)+"'";
                            int update=clsGlobalVarClass.dbMysql.execute(query);
                        }
                    }
                }
                rsSubMenuHead.close();
                flgReturn=true;
                
            }catch(Exception e)
            {
                flgReturn=false;
                e.printStackTrace();
            }
            finally
            {
                return flgReturn;
            }
        }
        
        
        private boolean funGenerateMenuItemPriceHD()
        {
            boolean flgReturn=false;
            String query="",code="";
            long docNo=0;
            try
            {
                //clsGlobalVarClass.dbMysql.execute("truncate table tblmenuitempricinghd");
                String sql="select distinct(strMenuHeadCode),strMenuHeadName,strPOSCode from tblimportexcel";
                ResultSet rsMenuItemPriceHd=clsGlobalVarClass.dbMysql.executeResultSet(sql);
                while(rsMenuItemPriceHd.next())
                {
                    docNo++;
                    query="insert into tblmenuitempricinghd(strPosCode,strMenuCode,strMenuName,strUserCreated"
                            + ",strUserEdited,dteDateCreated,dteDateEdited) "
                            + "values('"+rsMenuItemPriceHd.getString(3)+"','"+rsMenuItemPriceHd.getString(1)+"'"
                            + ",'"+rsMenuItemPriceHd.getString(2)+"','"+clsGlobalVarClass.gUserCode+"'"
                            + ",'"+ clsGlobalVarClass.gUserCode+"','"+clsGlobalVarClass.getCurrentDateTime()+"'"
                            + ",'"+clsGlobalVarClass.getCurrentDateTime()+"')";
                    int insert=clsGlobalVarClass.dbMysql.execute(query);
                }
                rsMenuItemPriceHd.close();
                flgReturn=true;
                
            }catch(Exception e)
            {
                flgReturn=false;
                e.printStackTrace();
            }
            finally
            {
                return flgReturn;
            }
        }
        
        private String funFormatPrice(String price)
        {
            if(price.contains(","))
            {
                price=price.replace(",","");
            }
            return price;
        }
        
        private boolean funGenerateMenuItemPriceDTL()
        {
            boolean flgReturn=false;
            String fromDate="",toDate="",priceMon="",priceTue="",priceWed="",priceThu="",priceFri="",priceSat="";
            String priceSun="";
            Date dt=new Date();
            fromDate=(dt.getYear()+1900)+"-"+(dt.getMonth()+1)+"-"+dt.getDate()+" ";
            fromDate+=dt.getHours()+":"+dt.getMinutes()+":"+dt.getSeconds();
            
            toDate=(dt.getYear()+1901)+"-"+(dt.getMonth()+1)+"-"+dt.getDate()+" ";
            toDate+=dt.getHours()+":"+dt.getMinutes()+":"+dt.getSeconds();
            
            String query="",code="";
            long docNo=0;
            try
            {
                //clsGlobalVarClass.dbMysql.execute("truncate table tblmenuitempricingdtl");
                String sql="select distinct(strItemCode),strItemName,strPOSCode,strMenuHeadCode"
                        + ",dblPriceMonday,dblPriceTuesday,dblPriceWednesday,dblPriceThursday,dblPriceFriday"
                        + ",dblPriceSaturday,dblPriceSunday,strCostCenterCode,strAreaCode,strSubMenuHeadCode "
                        + "from tblimportexcel";
                ResultSet rsMenuItemPriceDtl=clsGlobalVarClass.dbMysql.executeResultSet(sql);
                while(rsMenuItemPriceDtl.next())
                {
                    docNo++;
                    if(rsMenuItemPriceDtl.getString(5).trim().length()==0)
                        priceMon="0.00";
                    else
                        priceMon=funFormatPrice(rsMenuItemPriceDtl.getString(5).trim());
                    
                    if(rsMenuItemPriceDtl.getString(6).trim().length()==0)
                        priceTue="0.00";
                    else
                        priceTue=funFormatPrice(rsMenuItemPriceDtl.getString(6).trim());
                    
                    if(rsMenuItemPriceDtl.getString(7).trim().length()==0)
                        priceWed="0.00";
                    else
                        priceWed=funFormatPrice(rsMenuItemPriceDtl.getString(7).trim());
                    
                    if(rsMenuItemPriceDtl.getString(8).trim().length()==0)
                        priceThu="0.00";
                    else
                        priceThu=funFormatPrice(rsMenuItemPriceDtl.getString(8).trim());
                    
                    if(rsMenuItemPriceDtl.getString(9).trim().length()==0)
                        priceFri="0.00";
                    else
                        priceFri=funFormatPrice(rsMenuItemPriceDtl.getString(9).trim());
                    
                    if(rsMenuItemPriceDtl.getString(10).trim().length()==0)
                        priceSat="0.00";
                    else
                        priceSat=funFormatPrice(rsMenuItemPriceDtl.getString(10).trim());
                    
                    if(rsMenuItemPriceDtl.getString(11).trim().length()==0)
                        priceSun="0.00";
                    else
                        priceSun=funFormatPrice(rsMenuItemPriceDtl.getString(11).trim());
                    
                    query="insert into tblmenuitempricingdtl(strItemCode,strItemName,strPosCode,strMenuCode"
                        + ",strPopular,strPriceMonday,strPriceTuesday,strPriceWednesday,strPriceThursday,strPriceFriday"
                        + ",strPriceSaturday,strPriceSunday,dteFromDate,dteToDate,tmeTimeFrom,strAMPMFrom,tmeTimeTo"
                        + ",strAMPMTo,strCostCenterCode,strTextColor,strUserCreated,strUserEdited,dteDateCreated"
                        + ",dteDateEdited,strAreaCode,strSubMenuHeadCode,strHourlyPricing) "
                        + "values('"+rsMenuItemPriceDtl.getString(1)+"','"+rsMenuItemPriceDtl.getString(2)+"'"
                        + ",'"+rsMenuItemPriceDtl.getString(3)+"','"+rsMenuItemPriceDtl.getString(4)+"'"
                        + ",'N','"+priceMon+"','"+priceTue+"'"+ ",'"+priceWed+"','"+priceThu+"'"+ ",'"+priceFri+"'"
                        + ",'"+priceSat+"'"+ ",'"+priceSun+"','"+fromDate+"','"+toDate+"'"
                        + ",'HH:MM', 'AM', 'HH:MM', 'AM','"+rsMenuItemPriceDtl.getString(12)+"','Black'"
                        + ",'"+clsGlobalVarClass.gUserCode+"','"+clsGlobalVarClass.gUserCode+"'"
                        + ",'"+clsGlobalVarClass.getCurrentDateTime()+"','"+clsGlobalVarClass.getCurrentDateTime()+"'"
                        + ",'"+rsMenuItemPriceDtl.getString(13)+"','"+rsMenuItemPriceDtl.getString(14)+"','No')";
                    System.out.println(query);
                    int insert=clsGlobalVarClass.dbMysql.execute(query);
                }
                rsMenuItemPriceDtl.close();
                flgReturn=true;
                
            }catch(Exception e)
            {
                flgReturn=false;
                e.printStackTrace();
            }
            finally
            {
                return flgReturn;
            }
        }
        
    private void funImportButtonClicked()
    {
        funEmptyMasterTables();
        if(funCheckEmptyDB())
        {
            if(funFillTempTable()>0)
            {
                funGenerateCode();
                JOptionPane.showMessageDialog(this,"Data Imported Successfully");
            }
        }
    }
  
    private int funEmptyMasterTables() 
        {
            try
            {
                String sql="truncate table tblgrouphd";
                clsGlobalVarClass.dbMysql.execute(sql);

                sql="truncate table tblsubgrouphd";
                clsGlobalVarClass.dbMysql.execute(sql);

                sql="truncate table tblmenuhd";
                clsGlobalVarClass.dbMysql.execute(sql);

                sql="truncate table tblsubmenuhead";
                clsGlobalVarClass.dbMysql.execute(sql);

                sql="truncate table tblcostcentermaster";
                clsGlobalVarClass.dbMysql.execute(sql);

                sql="truncate table tblareamaster";
                clsGlobalVarClass.dbMysql.execute(sql);
                sql="update tblinternal set dblLastNo=0 where strTransactionType='Area'";
                clsGlobalVarClass.dbMysql.execute(sql);

                sql="truncate table tblitemmaster";
                clsGlobalVarClass.dbMysql.execute(sql);

                sql="truncate table tblcounterhd";
                clsGlobalVarClass.dbMysql.execute(sql);

                sql="truncate table tblmenuitempricinghd";
                clsGlobalVarClass.dbMysql.execute(sql);

                sql="truncate table tblmenuitempricingdtl";
                clsGlobalVarClass.dbMysql.execute(sql);
                
            }catch(Exception e)
            {
                e.printStackTrace();
            }
            
            return 1;
        }
        
        
        
        public boolean funCheckEmptyDB()
        {
            boolean flgResult=false;
            int groupCount=0,subGroupCount=0,itemMasterCount=0,menuHeadCount=0,subMenuHeadCount=0,counterCount=0;
            int costCenterCount=0,menuItemPricingHd=0,menuItemPricingDtl=0;
                        
            try
            {
                String sql="select count(strGroupCode) from tblgrouphd "
                        + "where strClientCode='"+clsGlobalVarClass.gClientCode+"'";
                ResultSet rsGroup=clsGlobalVarClass.dbMysql.executeResultSet(sql);
                rsGroup.next();
                groupCount=rsGroup.getInt(1);
                rsGroup.close();
                
                sql="select count(strSubGroupCode) from tblsubgrouphd "
                        + "where strClientCode='"+clsGlobalVarClass.gClientCode+"'";
                ResultSet rsSubGroup=clsGlobalVarClass.dbMysql.executeResultSet(sql);
                rsSubGroup.next();
                subGroupCount=rsSubGroup.getInt(1);
                rsSubGroup.close();
                
                sql="select count(strMenuCode) from tblmenuhd "
                        + "where strClientCode='"+clsGlobalVarClass.gClientCode+"'";
                ResultSet rsMenuHead=clsGlobalVarClass.dbMysql.executeResultSet(sql);
                rsMenuHead.next();
                menuHeadCount=rsMenuHead.getInt(1);
                rsMenuHead.close();
                
                sql="select count(strSubMenuHeadCode) from tblsubmenuhead "
                        + "where strClientCode='"+clsGlobalVarClass.gClientCode+"'";
                ResultSet rsSubMenu=clsGlobalVarClass.dbMysql.executeResultSet(sql);
                rsSubMenu.next();
                subMenuHeadCount=rsSubMenu.getInt(1);
                rsSubMenu.close();
                
                sql="select count(strCostCenterCode) from tblcostcentermaster "
                        + "where strClientCode='"+clsGlobalVarClass.gClientCode+"'";
                ResultSet rsCostCenter=clsGlobalVarClass.dbMysql.executeResultSet(sql);
                rsCostCenter.next();
                costCenterCount=rsCostCenter.getInt(1);
                rsCostCenter.close();
                
                sql="select count(strAreaCode) from tblareamaster "
                        + "where strClientCode='"+clsGlobalVarClass.gClientCode+"'";
                ResultSet rsArea=clsGlobalVarClass.dbMysql.executeResultSet(sql);
                rsArea.next();
                rsArea.close();
                
                sql="select count(strItemCode) from tblitemmaster "
                        + "where strClientCode='"+clsGlobalVarClass.gClientCode+"'";
                ResultSet rsItemMaster=clsGlobalVarClass.dbMysql.executeResultSet(sql);
                rsItemMaster.next();
                itemMasterCount=rsItemMaster.getInt(1);
                rsItemMaster.close();
                
                sql="select count(strCounterCode) from tblcounterhd "
                        + "where strClientCode='"+clsGlobalVarClass.gClientCode+"'";
                ResultSet rsCounter=clsGlobalVarClass.dbMysql.executeResultSet(sql);
                rsCounter.next();
                counterCount=rsCounter.getInt(1);
                rsCounter.close();
                
                sql="select count(strMenuCode) from tblmenuitempricinghd";
                ResultSet rsItemPriceHd=clsGlobalVarClass.dbMysql.executeResultSet(sql);
                rsItemPriceHd.next();
                menuItemPricingHd=rsItemPriceHd.getInt(1);
                rsItemPriceHd.close();
                
                sql="select count(strItemCode) from tblmenuitempricingdtl";
                ResultSet rsItemPriceDtl=clsGlobalVarClass.dbMysql.executeResultSet(sql);
                rsItemPriceDtl.next();
                menuItemPricingDtl=rsItemPriceDtl.getInt(1);
                rsItemPriceDtl.close();
                
                if(menuHeadCount==0 && groupCount==0 && subGroupCount==0 && subMenuHeadCount==0 
                    && itemMasterCount==0 && costCenterCount==0 && menuItemPricingHd==0 
                    && menuItemPricingDtl==0 && counterCount==0)
                {
                    flgResult=true;
                }
                
            }catch(Exception e)
            {
                flgResult=false;
                e.printStackTrace();
            }
            finally
            {
                return flgResult;
            }
        }   
    
    
      
    private void funResetFields()
    {
        txtIPAddress.setText("");
        txtPortNo.setText("");
        txtDatabaseName.setText("");
        txtUserName.setText("");
        txtPassword.setText("");
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
        txtIPAddress = new javax.swing.JTextField();
        btnImportFile = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        lblModuleName = new javax.swing.JLabel();
        lblMaster = new javax.swing.JLabel();
        btnConnect = new javax.swing.JButton();
        lblMaster2 = new javax.swing.JLabel();
        lblMaster4 = new javax.swing.JLabel();
        txtPassword = new javax.swing.JTextField();
        txtPortNo = new javax.swing.JTextField();
        lblMaster5 = new javax.swing.JLabel();
        txtDatabaseName = new javax.swing.JTextField();
        lblMaster3 = new javax.swing.JLabel();
        txtUserName = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setExtendedState(MAXIMIZED_BOTH);
        setMinimumSize(new java.awt.Dimension(800, 600));
        setUndecorated(true);
        setPreferredSize(new java.awt.Dimension(823, 600));
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
        lblformName.setText("- Import Database");
        lblformName.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                lblformNameMouseClicked(evt);
            }
        });
        jPanel1.add(lblformName);
        jPanel1.add(filler4);
        jPanel1.add(filler5);

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
        jPanel1.add(lblPosName);
        jPanel1.add(filler6);

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
        jPanel1.add(lblUserCode);

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
        jPanel1.add(lblDate);

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
        jPanel1.add(lblHOSign);

        jPanel2.setOpaque(false);
        jPanel2.setLayout(new java.awt.GridBagLayout());

        jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        jPanel3.setMinimumSize(new java.awt.Dimension(800, 570));
        jPanel3.setOpaque(false);

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

        lblMaster.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblMaster.setText("UserName          :");

        btnConnect.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnConnect.setForeground(new java.awt.Color(255, 255, 255));
        btnConnect.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnConnect.setText("CONNECT");
        btnConnect.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnConnect.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnConnect.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnConnectActionPerformed(evt);
            }
        });

        lblMaster2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblMaster2.setText("IP Address         : ");

        lblMaster4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblMaster4.setText("Database Name  :");

        lblMaster5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblMaster5.setText("PORT No           :");

        lblMaster3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblMaster3.setText("Password           :");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(281, 281, 281)
                        .addComponent(lblModuleName, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(389, 389, 389)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtIPAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtPortNo, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtDatabaseName, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtUserName, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(236, 236, 236)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblMaster4, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblMaster, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblMaster5, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblMaster3, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(110, 110, 110)
                        .addComponent(btnConnect, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(42, 42, 42)
                        .addComponent(btnImportFile, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(44, 44, 44)
                        .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(38, 38, 38)
                        .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(122, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addGap(236, 236, 236)
                    .addComponent(lblMaster2, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(450, Short.MAX_VALUE)))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(lblModuleName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(81, 81, 81)
                .addComponent(txtIPAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(txtPortNo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtDatabaseName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblMaster5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblMaster4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(lblMaster, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(txtUserName, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMaster3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnConnect, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnImportFile, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(147, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addGap(127, 127, 127)
                    .addComponent(lblMaster2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(409, Short.MAX_VALUE)))
        );

        jPanel2.add(jPanel3, new java.awt.GridBagConstraints());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 869, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 869, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 552, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 582, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnImportFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImportFileActionPerformed
        // TODO add your handling code here:
        if(connectDatabase.equalsIgnoreCase("Y"))
        {
               funImportButtonClicked();
        }
        else
         {
               JOptionPane.showMessageDialog(null, "Database Not Connected");
                    
         }
        

    }//GEN-LAST:event_btnImportFileActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        // TODO add your handling code here:
        funResetFields();

    }//GEN-LAST:event_btnResetActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        dispose();
        clsGlobalVarClass.hmActiveForms.remove("Import Database");
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnConnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConnectActionPerformed
        try {
            // TODO add your handling code here:
            Connection con=null;
            con=funOpenMSSQLConnection("mssql","trans");
            if(con.isValid(cnt))
            {
                 connectDatabase="Y";
                 JOptionPane.showMessageDialog(null, "Connection Successful !!");
            }
           
          
        } catch (Exception ex) {
           // Logger.getLogger(frmImportDatabase.class.getName()).log(Level.SEVERE, null, ex);
            new clsUtility().funWriteErrorLog(ex);
        }
  
    
     
    }//GEN-LAST:event_btnConnectActionPerformed

    private void lblPosNameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblPosNameMouseClicked
        // TODO add your handling code here:
        clsUtility objUtility=new clsUtility();
        objUtility.funMinimizeWindow();
    }//GEN-LAST:event_lblPosNameMouseClicked

    private void lblformNameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblformNameMouseClicked
        // TODO add your handling code here:
        clsUtility objUtility=new clsUtility();
        objUtility.funMinimizeWindow();
    }//GEN-LAST:event_lblformNameMouseClicked

    private void lblUserCodeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblUserCodeMouseClicked
        // TODO add your handling code here:
        clsUtility objUtility=new clsUtility();
        objUtility.funMinimizeWindow();
    }//GEN-LAST:event_lblUserCodeMouseClicked

    private void lblDateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblDateMouseClicked
        // TODO add your handling code here:
        clsUtility objUtility=new clsUtility();
        objUtility.funMinimizeWindow();
    }//GEN-LAST:event_lblDateMouseClicked

    private void lblHOSignMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblHOSignMouseClicked
        // TODO add your handling code here:
          clsUtility objUtility=new clsUtility();
         objUtility.funMinimizeWindow();
    }//GEN-LAST:event_lblHOSignMouseClicked

    private void btnResetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnResetMouseClicked
        // TODO add your handling code here:
        funResetFields();
    }//GEN-LAST:event_btnResetMouseClicked

    private void formWindowClosed(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosed
    {//GEN-HEADEREND:event_formWindowClosed
        clsGlobalVarClass.hmActiveForms.remove("Import Database");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosing
    {//GEN-HEADEREND:event_formWindowClosing
        clsGlobalVarClass.hmActiveForms.remove("Import Database");
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
            java.util.logging.Logger.getLogger(frmImportDatabase.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmImportDatabase.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmImportDatabase.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmImportDatabase.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmImportDatabase().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnConnect;
    private javax.swing.JButton btnImportFile;
    private javax.swing.JButton btnReset;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblMaster;
    private javax.swing.JLabel lblMaster2;
    private javax.swing.JLabel lblMaster3;
    private javax.swing.JLabel lblMaster4;
    private javax.swing.JLabel lblMaster5;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblModuleName1;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblformName;
    private javax.swing.JTextField txtDatabaseName;
    private javax.swing.JTextField txtIPAddress;
    private javax.swing.JTextField txtPassword;
    private javax.swing.JTextField txtPortNo;
    private javax.swing.JTextField txtUserName;
    // End of variables declaration//GEN-END:variables
}
