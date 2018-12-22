/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSMaster.view;

import com.POSGlobal.controller.clsFixedSizeText;
import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsUtility;
import com.POSGlobal.view.frmAlfaNumericKeyBoard;
import com.POSGlobal.view.frmOkPopUp;
import com.POSGlobal.view.frmSearchFormDialog;
import com.POSMaster.controller.clsPromotionGroupMaster;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

public class frmTaxGroupMaster extends javax.swing.JFrame
{
    clsUtility objUtility = new clsUtility();
    private Map<String, clsPromotionGroupMaster> hmPromoGroupMasterItemDtl=new HashMap<String, clsPromotionGroupMaster>();
    private String subGroupName,groupName;
    DefaultTableModel dmTaxGroups;
    
    /**
     * This method is used to initialize frmPromotionGroupMaster
     */
    public frmTaxGroupMaster()
    {
        initComponents();
        try
        {
            lblUserCode.setText(clsGlobalVarClass.gUserCode);
            lblPosName.setText(clsGlobalVarClass.gPOSName);
            lblModuleName.setText(clsGlobalVarClass.gSelectedModule);
           
            lblDate.setText(clsGlobalVarClass.gPOSDateToDisplay);
            dmTaxGroups = (DefaultTableModel) tblTaxDetails.getModel();
            funLoadTaxGroups();
            funSetShortCutKeys();
            subGroupName="";
            groupName="";
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    private void funSetShortCutKeys()
    {
        btnCancel.setMnemonic('c');
        btnNew.setMnemonic('s');
        btnReset.setMnemonic('r');
    }

    
    private void funSetMenuItemData(Object[] data) throws Exception
    { 
       
       
    }

    
    private void funAddRow()
    {
//        clsTaxGroupMaster objTaxGroupMaster=new clsTaxGroupMaster();
//        objTaxGroupMaster.setTaxCode(txtTaxCode.getText().trim());
//         objTaxGroupMaster.setTaxName(txtTaxName.getText().trim());
//        objTaxGroupMaster.setTaxOnSp(txtTaxOnSP.getText().trim());
//        objTaxGroupMaster.setTaxType(txtTaxType.rim());
//        objTaxGroupMaster.setGroupName(groupName.trim());
//        hmPromoGroupMasterItemDtl.put(txtTaxCode.getText().trim(),objTaxGroupMaster);
//        
        funRefreshMenuItemGrid();
        
        //lblTaxOnSP.setText("");
    }
    
    
    private void funRemoveRow()
    {
        for(int i=0;i<tblTaxDetails.getRowCount();i++)
        {
            String itemCode=tblTaxDetails.getValueAt(i, 0).toString();
            boolean flgSelect=Boolean.parseBoolean(tblTaxDetails.getValueAt(i, 4).toString());
            if(flgSelect)
            {
                if(hmPromoGroupMasterItemDtl.containsKey(itemCode))
                {
                    hmPromoGroupMasterItemDtl.remove(itemCode);
                }
            }
        }
        funRefreshMenuItemGrid();
    }
    
    
    private void funRefreshMenuItemGrid()
    {
        DefaultTableModel dmMenuItems = (DefaultTableModel) tblTaxDetails.getModel();
        dmMenuItems.setRowCount(0);
        
        for(Map.Entry<String,clsPromotionGroupMaster> entry:hmPromoGroupMasterItemDtl.entrySet())
        {
            Object[] arrObj=
            {
                entry.getValue().getItemCode(),entry.getValue().getItemName()
                    ,entry.getValue().getSubGroupName(),entry.getValue().getGroupName(),false
            };
            dmMenuItems.addRow(arrObj);
        }
        
        tblTaxDetails.setModel(dmMenuItems);
    }
    
    
    private void funOpenTaxGroupMasterSearch()
    {
        try
        {
            objUtility.funCallForSearchForm("TaxGroupMaster");
            new frmSearchFormDialog(this, true).setVisible(true);
            if (clsGlobalVarClass.gSearchItemClicked)
            {
                btnNew.setText("UPDATE");
                btnNew.setMnemonic('u');
                Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
                funSetPromoGroupMasterData(data);
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
     * This method is used to set data
     *
     * @param data
     */
    private void funSetPromoGroupMasterData(Object[] data)
    {
        try
        {
            txtTaxGroupCode.setText(data[0].toString());
            txtTaxGroupName.setText(data[1].toString());
             funFillTaxDetailsTable(data[0].toString());
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }
   
     /**
     * This method is used to load tax groups
     */
    private void funLoadTaxGroups() {
        try {
            String sql = "SELECT a.strTaxCode,a.strTaxDesc,a.strTaxOnSP,a.strTaxType,a.strTaxCalculation FROM tbltaxhd a" 
                    +" ORDER BY a.strTaxCode";
            ResultSet rsTaxGroups = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            while (rsTaxGroups.next()) {
                Object row[] = {rsTaxGroups.getString(1), rsTaxGroups.getString(2), false};
                dmTaxGroups.addRow(row);
            }
            tblTaxDetails.setModel(dmTaxGroups);
            rsTaxGroups.close();

        } catch (Exception e) {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }
    
    private void funFillTaxDetailsTable(String taxGroupCode) {
        try {
            boolean select = false;
            java.util.Vector vTaxGroupDtl = new java.util.Vector();
            String sqlDtl = "SELECT a.strTaxGroupCode,a.strTaxDesc,a.strTaxOnSP,a.strTaxType,a.strTaxCalculation FROM tbltaxhd a "
                    + "where a.strTaxGroupCode='" + taxGroupCode + "' "
                    + " order by a.strTaxGroupCode";
            ResultSet rsDtl = clsGlobalVarClass.dbMysql.executeResultSet(sqlDtl);
            while (rsDtl.next()) {
                vTaxGroupDtl.add(rsDtl.getString(1));
                //Object row[]={rsDtl.getString(1),rsDtl.getString(2),true};
                //dm.addRow(row);
            }
            rsDtl.close();
            DefaultTableModel dm = (DefaultTableModel) tblTaxDetails.getModel();
            dm.setRowCount(0);
            String sqltaxDtl = "select a.strTaxGroupCode,a.strTaxCode,a.strTaxDesc,a.strTaxOnSP,a.strTaxType,a.strTaxCalculation FROM tbltaxhd a order by a.strTaxCode";
            ResultSet rsTaxDtl = clsGlobalVarClass.dbMysql.executeResultSet(sqltaxDtl);
            while (rsTaxDtl.next()) {
                select = false;
                for (int i = 0; i < vTaxGroupDtl.size(); i++) {
                    if (rsTaxDtl.getString(1).equals(vTaxGroupDtl.elementAt(i).toString())) {
                        select = true;
                        break;
                    }
                }
                Object row[] = {rsTaxDtl.getString(2), rsTaxDtl.getString(3),rsTaxDtl.getString(4),rsTaxDtl.getString(5),rsTaxDtl.getString(6), select};
                dm.addRow(row);
            }
            rsTaxDtl.close();
            tblTaxDetails.setModel(dm);
        } catch (Exception e) {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }
    
    private String funGenerateTaxGroupCode() throws Exception
    {
        String TaxGroupCode = "";
        
        String sql = "select ifnull(max(right(a.strtaxGroupCode,3)),0) from tbltaxgrouphd a";
        ResultSet rsTaxGroup = clsGlobalVarClass.dbMysql.executeResultSet(sql);
        rsTaxGroup.next();
        int code = rsTaxGroup.getInt(1);
       
        code = code + 1;
        TaxGroupCode = "TG" + String.format("%03d", code);
        
        sql = "insert into tblinternal values('Area'," + 1 + ")";
        clsGlobalVarClass.dbMysql.execute(sql);
        
        
        return TaxGroupCode;
    }
    

    /**
     * This method is used to save promotion group master
     */
    private void funSavePromotionGroupMaster()
    {
        try
        {
            if (!clsGlobalVarClass.validateEmpty(txtTaxGroupName.getText().trim()))
            {
                new frmOkPopUp(this, "Please Enter Tax Group Name", "Error", 0).setVisible(true);
                txtTaxGroupName.requestFocus();
            }
            else
            {
                String taxGroupCode=funGenerateTaxGroupCode();
                txtTaxGroupCode.setText(taxGroupCode);
                
//                if(funFilltaxGroupDtlTable())
//                {
                    String sql ="delete from tbltaxgrouphd where strTaxGroupCode='"+txtTaxGroupCode.getText()+"' "
                        + "and strClientCode='"+clsGlobalVarClass.gClientCode+"'";
                    clsGlobalVarClass.dbMysql.execute(sql);
                    
                    sql = "insert into tbltaxgrouphd (strTaxGroupCode,strTaxGroupName,strClientCode,"
                        + "strUserCreated,strUserEdited,dteDateCreated,dteDateEdited,strDataPostFlag) "
                        + "values('" + txtTaxGroupCode.getText() + "','" + txtTaxGroupName.getText().trim() + "'"
                        + ",'"+clsGlobalVarClass.gClientCode+"','"+ clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "'"
                        + ",'"+ clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "'"
                        + ",'N')";
                    int exc = clsGlobalVarClass.dbMysql.execute(sql);

                    if (exc > 0)
                    {
                        sql="update tblmasteroperationstatus set dteDateEdited='"+clsGlobalVarClass.getCurrentDateTime()+"' "
                            + " where strTableName='TaxGroupMaster' ";
                        clsGlobalVarClass.dbMysql.execute(sql);
                        new frmOkPopUp(this, "Entry added Successfully", "Successfull", 3).setVisible(true);
                        funResetFields();
                    }
                //}
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

//    private boolean funFilltaxGroupDtlTable() throws Exception
//    {
//        String sql ="delete from tblpromogroupdtl where strPromoGroupCode='"+txtTaxGroupCode.getText()+"' "
//            + "and strClientCode='"+clsGlobalVarClass.gClientCode+"'";
//        clsGlobalVarClass.dbMysql.execute(sql);
//        
//        for(Map.Entry<String,clsPromotionGroupMaster> entry : hmPromoGroupMasterItemDtl.entrySet())
//        {
//                sql = "insert into tblpromogroupdtl (strPromoGroupCode,strItemCode,strMenuCode,"
//                    + "strClientCode,strDataPostFlag) "
//                    + "values('" + txtTaxGroupCode.getText() + "','" + entry.getValue().getItemCode() + "'"
//                    + ",'','"+ clsGlobalVarClass.gClientCode + "','N')";
//                clsGlobalVarClass.dbMysql.execute(sql);
//        }
//        
//        return true;
//    }
    
       
    private boolean funCheckDuplicateName(String promoGroupName,String promoGroupCode)
    {
        boolean flgResult=true;
        try
        {
            String sql="select strPromoGroupName from tblpromogroupmaster "
                + " where strPromoGroupName='"+promoGroupName+"' and strPromoGroupCode!='"+promoGroupCode+"'";
            ResultSet rs=clsGlobalVarClass.dbMysql.executeResultSet(sql);
            if(rs.next())
            {
                flgResult=false;
            }
            rs.close();
            
        }catch(Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
        finally
        {
            return flgResult;
        }
    }
    

    /**
     * This method is used to reset fields
     */
    private void funResetFields()
    {
        try
        {
            btnNew.setText("SAVE");
            btnNew.setMnemonic('u');
            txtTaxGroupCode.requestFocus();
            txtTaxGroupCode.setText("");
            
            txtTaxGroupName.setText("");
//            lblTaxOnSP.setText("");
            
            subGroupName="";
            groupName="";
            DefaultTableModel dm = (DefaultTableModel) tblTaxDetails.getModel();
            dm.setRowCount(0);
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }
    
    
    private void funValidateAndSave()
    {
        try
        {
            if(!funCheckDuplicateName(txtTaxGroupName.getText().trim(),txtTaxGroupCode.getText().trim()))
            {
                JOptionPane.showMessageDialog(null, "Tax Group Name akready exists.");
                return;
            }
            funSavePromotionGroupMaster();
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
            if (e.getMessage().startsWith("Duplicate entry"))
            {
                new frmOkPopUp(this, "Tax Code is already present", "Error", 1).setVisible(true);
                return;
            }
        }
    }
    
     private void funUpdateTaxGroup()
    {
        try
        {
            clsUtility obj = new clsUtility();
            String taxGroupCode = txtTaxGroupCode.getText().trim();
            String taxGroupName = txtTaxGroupName.getText().trim();

            if (clsGlobalVarClass.funCheckItemName("tbltaxgrouphd", "strtaxGroupName", "strtaxGroupCode", taxGroupName, taxGroupCode, "update", ""))
            {
                new frmOkPopUp(this, "This Tax Group Name is Already Exist", "Error", 0).setVisible(true);
                txtTaxGroupName.requestFocus();
            }
            else if (!clsGlobalVarClass.validateEmpty(txtTaxGroupName.getText()))
            {
                new frmOkPopUp(this, "Please Enter Tax Group Name", "Error", 0).setVisible(true);
                txtTaxGroupCode.requestFocus();
            }
            else if (!obj.funCheckLength(txtTaxGroupName.getText(), 20))
            {
                new frmOkPopUp(this, "Tax Group Name length must be less than 20", "Error", 0).setVisible(true);
                txtTaxGroupCode.requestFocus();
            }
            else
            {
              boolean select = false; 
              String  updateQuery = "UPDATE tbltaxgrouphd "
                        + "SET strtaxGroupName = '" + txtTaxGroupName.getText() + "'"
                        + ",strUserEdited='" + clsGlobalVarClass.gUserCode + "'"
                        + ",dteDateEdited='" + clsGlobalVarClass.getCurrentDateTime() + "'"
                        + ",strDataPostFlag='N'"
                        + " WHERE strtaxGroupCode ='" + txtTaxGroupCode.getText() + "'";
                //System.out.println(updateQuery);
                int exc = clsGlobalVarClass.dbMysql.execute(updateQuery);
                
                String insertSql = "";
               
                for (int i = 0; i < tblTaxDetails.getRowCount(); i++) 
                {
                select = Boolean.parseBoolean(tblTaxDetails.getValueAt(i, 5).toString());
                if (select) 
                {  
                    insertSql = "update tbltaxhd set strTaxGroupCode='"+txtTaxGroupCode.getText()+"' where strTaxCode='"+tblTaxDetails.getValueAt(i, 0).toString()+"' AND strClientCode='"+clsGlobalVarClass.gClientCode+"';";
                     
                }
                else
                {
                 String notSelected="";
                  insertSql = "update tbltaxhd set strTaxGroupCode='"+notSelected+"' where strTaxCode='"+tblTaxDetails.getValueAt(i, 0).toString()+"' AND strClientCode='"+clsGlobalVarClass.gClientCode+"';";
                      
                }    
                clsGlobalVarClass.dbMysql.execute(insertSql);
                
                
                }
                
               
            
                if (exc > 0)
                {
                  String sql="update tblmasteroperationstatus set dteDateEdited='"+clsGlobalVarClass.getCurrentDateTime()+"' "
                        + " where strTableName='TaxGroupMaster' ";
                    clsGlobalVarClass.dbMysql.execute(sql);
                    new frmOkPopUp(this, "Updated Successfully", "Successfull", 3).setVisible(true);
                    funResetFields();
                }
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }
    
    private void funOpenTaxSearch()
    {
        try
        {
            objUtility.funCallForSearchForm("Tax");
            new frmSearchFormDialog(this, true).setVisible(true);
            if (clsGlobalVarClass.gSearchItemClicked)
            {
                Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
                funSetMenuItemData(data);
                clsGlobalVarClass.gSearchItemClicked = false;
            }
        }catch(Exception e)
        {
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
        lblProductName1 = new javax.swing.JLabel();
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
        panelbody = new javax.swing.JPanel();
        lblFormName = new javax.swing.JLabel();
        txtTaxGroupCode = new javax.swing.JTextField();
        lblPromoGroupCode = new javax.swing.JLabel();
        lblPromoGroupName = new javax.swing.JLabel();
        scrollPaneItems = new javax.swing.JScrollPane();
        tblTaxDetails = new javax.swing.JTable();
        btnNew = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        txtTaxGroupName = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
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

        lblProductName1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblProductName1.setForeground(new java.awt.Color(255, 255, 255));
        lblProductName1.setText("SPOS - ");
        panelHeader.add(lblProductName1);

        lblModuleName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblModuleName.setForeground(new java.awt.Color(255, 255, 255));
        panelHeader.add(lblModuleName);

        lblformName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblformName.setForeground(new java.awt.Color(255, 255, 255));
        lblformName.setText("- Promotion Group Master");
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

        panelLayout.setBackground(new java.awt.Color(255, 255, 255));
        panelLayout.setLayout(new java.awt.GridBagLayout());

        panelbody.setBackground(new java.awt.Color(255, 255, 255));
        panelbody.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        panelbody.setOpaque(false);
        panelbody.setPreferredSize(new java.awt.Dimension(800, 570));
        panelbody.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblFormName.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        lblFormName.setForeground(new java.awt.Color(14, 7, 7));
        lblFormName.setText("Tax Group Master");
        panelbody.add(lblFormName, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 20, 270, 30));

        txtTaxGroupCode.setEditable(false);
        txtTaxGroupCode.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtTaxGroupCodeMouseClicked(evt);
            }
        });
        txtTaxGroupCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTaxGroupCodeKeyPressed(evt);
            }
        });
        panelbody.add(txtTaxGroupCode, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 80, 100, 30));

        lblPromoGroupCode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblPromoGroupCode.setText("Tax Group Code  :");
        panelbody.add(lblPromoGroupCode, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 120, 30));

        lblPromoGroupName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblPromoGroupName.setText("Tax Group Name    :");
        panelbody.add(lblPromoGroupName, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 80, 130, 30));

        scrollPaneItems.setBackground(new java.awt.Color(255, 255, 255));

        tblTaxDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Tax Code", "Tax Name", "Tax On S/P", "TaxCalculation", "Tax Type", "Select"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblTaxDetails.setRowHeight(25);
        tblTaxDetails.setRowMargin(2);
        tblTaxDetails.getTableHeader().setReorderingAllowed(false);
        tblTaxDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblTaxDetailsMouseClicked(evt);
            }
        });
        scrollPaneItems.setViewportView(tblTaxDetails);
        if (tblTaxDetails.getColumnModel().getColumnCount() > 0) {
            tblTaxDetails.getColumnModel().getColumn(0).setMinWidth(80);
            tblTaxDetails.getColumnModel().getColumn(0).setPreferredWidth(80);
            tblTaxDetails.getColumnModel().getColumn(0).setMaxWidth(80);
            tblTaxDetails.getColumnModel().getColumn(4).setMinWidth(60);
            tblTaxDetails.getColumnModel().getColumn(4).setPreferredWidth(60);
            tblTaxDetails.getColumnModel().getColumn(4).setMaxWidth(60);
        }

        panelbody.add(scrollPaneItems, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, 760, 370));

        btnNew.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnNew.setForeground(new java.awt.Color(255, 255, 255));
        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnNew.setText("SAVE");
        btnNew.setToolTipText("Save Modifier Master");
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
        panelbody.add(btnNew, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 520, 90, 40));

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
        panelbody.add(btnReset, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 520, 90, 40));

        btnCancel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnCancel.setForeground(new java.awt.Color(255, 255, 255));
        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnCancel.setText("CLOSE");
        btnCancel.setToolTipText("Close Modifier Master");
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
        panelbody.add(btnCancel, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 520, 90, 40));

        txtTaxGroupName.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtTaxGroupNameMouseClicked(evt);
            }
        });
        txtTaxGroupName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTaxGroupNameKeyPressed(evt);
            }
        });
        panelbody.add(txtTaxGroupName, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 80, 390, 30));

        panelLayout.add(panelbody, new java.awt.GridBagConstraints());

        getContentPane().add(panelLayout, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
    private void txtTaxGroupCodeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtTaxGroupCodeMouseClicked
        // TODO add your handling code here:
        funOpenTaxGroupMasterSearch();
    }//GEN-LAST:event_txtTaxGroupCodeMouseClicked
    
    
    private void tblTaxDetailsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblTaxDetailsMouseClicked
        
    }//GEN-LAST:event_tblTaxDetailsMouseClicked

    private void btnResetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnResetMouseClicked
        // TODO add your handling code here:
        funResetFields();
    }//GEN-LAST:event_btnResetMouseClicked

    private void btnCancelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseClicked
        // TODO add your handling code here:
        dispose();
        clsGlobalVarClass.hmActiveForms.remove("Item Modifier");
    }//GEN-LAST:event_btnCancelMouseClicked

    private void txtTaxGroupCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTaxGroupCodeKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() == '?' || evt.getKeyChar() == '/')
        {
            funOpenTaxGroupMasterSearch();
        }
        if (evt.getKeyCode() == 10)
        {
            txtTaxGroupName.requestFocus();
        }
    }//GEN-LAST:event_txtTaxGroupCodeKeyPressed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        // TODO add your handling code here:
        funResetFields();
    }//GEN-LAST:event_btnResetActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        dispose();
        clsGlobalVarClass.hmActiveForms.remove("Tax Group Master");
    }//GEN-LAST:event_btnCancelActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
        clsGlobalVarClass.hmActiveForms.remove("Tax Group Master");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        clsGlobalVarClass.hmActiveForms.remove("Tax Group Master");
    }//GEN-LAST:event_formWindowClosing

    private void txtTaxGroupNameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtTaxGroupNameMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTaxGroupNameMouseClicked

    private void txtTaxGroupNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTaxGroupNameKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTaxGroupNameKeyPressed

    private void btnNewKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnNewKeyPressed
        // TODO add your handling code here:

        if (evt.getKeyCode() == 10)
        {
            funValidateAndSave();
        }
    }//GEN-LAST:event_btnNewKeyPressed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        String printerName = "";
        if (btnNew.getText().equalsIgnoreCase("SAVE"))
        {
            //Add new cost center
            funValidateAndSave();
        }
        else
        {
            //Update existing cost center
            funUpdateTaxGroup();
        }

    }//GEN-LAST:event_btnNewActionPerformed

    private void btnNewMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNewMouseClicked

        try
        {
            if (btnNew.getText().equalsIgnoreCase("SAVE"))
            {
                funSavePromotionGroupMaster();
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnNewMouseClicked
   
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnReset;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblFormName;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName1;
    private javax.swing.JLabel lblPromoGroupCode;
    private javax.swing.JLabel lblPromoGroupName;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblformName;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelLayout;
    private javax.swing.JPanel panelbody;
    private javax.swing.JScrollPane scrollPaneItems;
    private javax.swing.JTable tblTaxDetails;
    private javax.swing.JTextField txtTaxGroupCode;
    private javax.swing.JTextField txtTaxGroupName;
    // End of variables declaration//GEN-END:variables
}
