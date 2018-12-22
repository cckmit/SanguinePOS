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

public class frmPromotionGroupMaster extends javax.swing.JFrame
{
    clsUtility objUtility = new clsUtility();
    private Map<String, clsPromotionGroupMaster> hmPromoGroupMasterItemDtl=new HashMap<String, clsPromotionGroupMaster>();
    private String subGroupName,groupName;
    
    /**
     * This method is used to initialize frmPromotionGroupMaster
     */
    public frmPromotionGroupMaster()
    {
        initComponents();
        try
        {
            lblUserCode.setText(clsGlobalVarClass.gUserCode);
            lblPosName.setText(clsGlobalVarClass.gPOSName);
            lblModuleName.setText(clsGlobalVarClass.gSelectedModule);
            txtPromoGroupName.setDocument(new clsFixedSizeText(30));
            lblDate.setText(clsGlobalVarClass.gPOSDateToDisplay);
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
        String sql = "select a.strItemCode,a.strItemName,b.strSubGroupName,c.strGroupName "
                + " from tblitemmaster a,tblsubgrouphd b,tblgrouphd c"
                + " where a.strSubGroupCode=b.strSubGroupCode and b.strGroupCode=c.strGroupCode "
                + " and a.strItemCode='" + clsGlobalVarClass.gSearchedItem + "'";
        ResultSet rsItemInfo = clsGlobalVarClass.dbMysql.executeResultSet(sql);
        if (rsItemInfo.next())
        {
            txtItemCode.setText(rsItemInfo.getString(1));
            lblItemName.setText(rsItemInfo.getString(2));
            subGroupName=rsItemInfo.getString(3);
            groupName=rsItemInfo.getString(4);
        }
        rsItemInfo.close();
    }

    
    private void funAddRow()
    {
        clsPromotionGroupMaster objPromotionGroupMaster=new clsPromotionGroupMaster();
        objPromotionGroupMaster.setItemCode(txtItemCode.getText().trim());
        objPromotionGroupMaster.setItemName(lblItemName.getText().trim());
        objPromotionGroupMaster.setSubGroupName(subGroupName.trim());
        objPromotionGroupMaster.setGroupName(groupName.trim());
        hmPromoGroupMasterItemDtl.put(txtItemCode.getText().trim(),objPromotionGroupMaster);
        
        funRefreshMenuItemGrid();
        
        txtItemCode.setText("");
        lblItemName.setText("");
    }
    
    
    private void funRemoveRow()
    {
        for(int i=0;i<tblItemDetails.getRowCount();i++)
        {
            String itemCode=tblItemDetails.getValueAt(i, 0).toString();
            boolean flgSelect=Boolean.parseBoolean(tblItemDetails.getValueAt(i, 4).toString());
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
        DefaultTableModel dmMenuItems = (DefaultTableModel) tblItemDetails.getModel();
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
        
        tblItemDetails.setModel(dmMenuItems);
    }
    
    
    private void funOpenPromoGroupMasterSearch()
    {
        try
        {
            objUtility.funCallForSearchForm("PromoGroupMaster");
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
            hmPromoGroupMasterItemDtl.clear();
            
            DefaultTableModel dm = (DefaultTableModel) tblItemDetails.getModel();
            dm.setRowCount(0);
            
            String promoGroupCode=data[0].toString();
            String sql="select * from tblpromogroupmaster where strPromoGroupCode='"+promoGroupCode+"' "
                + " and strClientCode='"+clsGlobalVarClass.gClientCode+"'";
            ResultSet rs=clsGlobalVarClass.dbMysql.executeResultSet(sql);
            while(rs.next())
            {
                txtPromoGroupCode.setText(rs.getString(1));
                txtPromoGroupName.setText(rs.getString(2));
            }
            rs.close();
            
            sql = "select a.strItemCode,a.strItemName,b.strSubGroupName,c.strGroupName "
                + " from tblpromogroupdtl z,tblitemmaster a,tblsubgrouphd b,tblgrouphd c"
                + " where z.strItemCode=a.strItemCode and a.strSubGroupCode=b.strSubGroupCode "
                + " and b.strGroupCode=c.strGroupCode and z.strPromoGroupCode='"+promoGroupCode+"' "
                + " and z.strClientCode='"+clsGlobalVarClass.gClientCode+"' ";
            
            rs=clsGlobalVarClass.dbMysql.executeResultSet(sql);
            while(rs.next())
            {
                clsPromotionGroupMaster objPromotionGroupMaster=new clsPromotionGroupMaster();
                objPromotionGroupMaster.setItemCode(rs.getString(1));
                objPromotionGroupMaster.setItemName(rs.getString(2));
                objPromotionGroupMaster.setSubGroupName(rs.getString(3));
                objPromotionGroupMaster.setGroupName(rs.getString(4));
                hmPromoGroupMasterItemDtl.put(rs.getString(1),objPromotionGroupMaster);
            }
            rs.close();
            
            funRefreshMenuItemGrid();
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }
   
    
    
    private String funGeneratePromoGroupCode() throws Exception
    {
        String promoGroupCode = "";
        
        String sql = "select count(dblLastNo) from tblinternal where strTransactionType='PromoGroup'";
        ResultSet rsPromoGroup = clsGlobalVarClass.dbMysql.executeResultSet(sql);
        rsPromoGroup.next();
        int cnt = rsPromoGroup.getInt(1);
        rsPromoGroup.close();
        if (cnt > 0)
        {
            sql = "select dblLastNo from tblinternal where strTransactionType='PromoGroup'";
            rsPromoGroup = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            rsPromoGroup.next();
            long code = rsPromoGroup.getLong(1);
            code = code + 1;
            promoGroupCode = "PG" + String.format("%03d", code);
            sql = "update tblinternal set dblLastNo='" + code + "' where strTransactionType='PromoGroup'";
            clsGlobalVarClass.dbMysql.execute(sql);
            rsPromoGroup.close();
        }
        else
        {
            promoGroupCode = "PG001";
            sql = "insert into tblinternal values('Area'," + 1 + ")";
            clsGlobalVarClass.dbMysql.execute(sql);
        }
        
        return promoGroupCode;
    }
    

    /**
     * This method is used to save promotion group master
     */
    private void funSavePromotionGroupMaster()
    {
        try
        {
            if (!clsGlobalVarClass.validateEmpty(txtPromoGroupName.getText().trim()))
            {
                new frmOkPopUp(this, "Please Enter Promotion Group Name", "Error", 0).setVisible(true);
                txtPromoGroupName.requestFocus();
            }
            else
            {
                String promoGroupCode=funGeneratePromoGroupCode();
                txtPromoGroupCode.setText(promoGroupCode);
                
                if(funFillPromoGroupDtlTable())
                {
                    String sql ="delete from tblpromogroupmaster where strPromoGroupCode='"+txtPromoGroupCode.getText()+"' "
                        + "and strClientCode='"+clsGlobalVarClass.gClientCode+"'";
                    clsGlobalVarClass.dbMysql.execute(sql);
                    
                    sql = "insert into tblpromogroupmaster (strPromoGroupCode,strPromoGroupName,strClientCode,"
                        + "strUserCreated,strUserEdited,dteDateCreated,dteDateEdited,strDataPostFlag) "
                        + "values('" + txtPromoGroupCode.getText() + "','" + txtPromoGroupName.getText().trim() + "'"
                        + ",'"+clsGlobalVarClass.gClientCode+"','"+ clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "'"
                        + ",'"+ clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "'"
                        + ",'N')";
                    int exc = clsGlobalVarClass.dbMysql.execute(sql);

                    if (exc > 0)
                    {
                        sql="update tblmasteroperationstatus set dteDateEdited='"+clsGlobalVarClass.getCurrentDateTime()+"' "
                            + " where strTableName='PromoGroupMaster' ";
                        clsGlobalVarClass.dbMysql.execute(sql);
                        new frmOkPopUp(this, "Entry added Successfully", "Successfull", 3).setVisible(true);
                        funResetFields();
                    }
                }
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    private boolean funFillPromoGroupDtlTable() throws Exception
    {
        String sql ="delete from tblpromogroupdtl where strPromoGroupCode='"+txtPromoGroupCode.getText()+"' "
            + "and strClientCode='"+clsGlobalVarClass.gClientCode+"'";
        clsGlobalVarClass.dbMysql.execute(sql);
        
        for(Map.Entry<String,clsPromotionGroupMaster> entry : hmPromoGroupMasterItemDtl.entrySet())
        {
                sql = "insert into tblpromogroupdtl (strPromoGroupCode,strItemCode,strMenuCode,"
                    + "strClientCode,strDataPostFlag) "
                    + "values('" + txtPromoGroupCode.getText() + "','" + entry.getValue().getItemCode() + "'"
                    + ",'','"+ clsGlobalVarClass.gClientCode + "','N')";
                clsGlobalVarClass.dbMysql.execute(sql);
        }
        
        return true;
    }
    
       
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
            txtPromoGroupCode.requestFocus();
            txtPromoGroupCode.setText("");
            txtPromoGroupName.setText("");
            txtItemCode.setText("");
            lblItemName.setText("");
            
            subGroupName="";
            groupName="";
            DefaultTableModel dm = (DefaultTableModel) tblItemDetails.getModel();
            dm.setRowCount(0);
            
            hmPromoGroupMasterItemDtl.clear();
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
            if (txtPromoGroupName.getText().trim().contains("'"))
            {
                JOptionPane.showMessageDialog(null, "Invalid Character '");
                return;
            }
            else if(!funCheckDuplicateName(txtPromoGroupName.getText().trim(),txtPromoGroupCode.getText().trim()))
            {
                JOptionPane.showMessageDialog(null, "Promotion Group Name akready exists.");
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
                new frmOkPopUp(this, "Modifier Code is already present", "Error", 1).setVisible(true);
                return;
            }
        }
    }
    
    
    private void funOpenMenuItemSearch()
    {
        try
        {
            objUtility.funCallForSearchForm("MenuItemForPrice");
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
        txtPromoGroupCode = new javax.swing.JTextField();
        lblPromoGroupCode = new javax.swing.JLabel();
        lblPromoGroupName = new javax.swing.JLabel();
        txtPromoGroupName = new javax.swing.JTextField();
        scrollPaneItems = new javax.swing.JScrollPane();
        tblItemDetails = new javax.swing.JTable();
        btnNew = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        lblPromoGroupCode1 = new javax.swing.JLabel();
        txtItemCode = new javax.swing.JTextField();
        lblItemName = new javax.swing.JLabel();
        btnResetItemDtlGrid = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        btnRemove = new javax.swing.JButton();

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
        lblFormName.setText("Promotion Group Master");
        panelbody.add(lblFormName, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 20, 270, 30));

        txtPromoGroupCode.setEditable(false);
        txtPromoGroupCode.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtPromoGroupCodeMouseClicked(evt);
            }
        });
        txtPromoGroupCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPromoGroupCodeKeyPressed(evt);
            }
        });
        panelbody.add(txtPromoGroupCode, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 80, 100, 30));

        lblPromoGroupCode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblPromoGroupCode.setText("Promo Group Code  :");
        panelbody.add(lblPromoGroupCode, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 120, 30));

        lblPromoGroupName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblPromoGroupName.setText("Promo Group Name    :");
        panelbody.add(lblPromoGroupName, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 80, 130, 30));

        txtPromoGroupName.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtPromoGroupNameMouseClicked(evt);
            }
        });
        txtPromoGroupName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPromoGroupNameKeyPressed(evt);
            }
        });
        panelbody.add(txtPromoGroupName, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 80, 390, 30));

        scrollPaneItems.setBackground(new java.awt.Color(255, 255, 255));

        tblItemDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Item Code", "Item Name", "Sub Group Name", "Group Name", "Select"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblItemDetails.setRowHeight(25);
        tblItemDetails.setRowMargin(2);
        tblItemDetails.getTableHeader().setReorderingAllowed(false);
        tblItemDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblItemDetailsMouseClicked(evt);
            }
        });
        scrollPaneItems.setViewportView(tblItemDetails);
        if (tblItemDetails.getColumnModel().getColumnCount() > 0) {
            tblItemDetails.getColumnModel().getColumn(0).setMinWidth(80);
            tblItemDetails.getColumnModel().getColumn(0).setPreferredWidth(80);
            tblItemDetails.getColumnModel().getColumn(0).setMaxWidth(80);
            tblItemDetails.getColumnModel().getColumn(4).setMinWidth(60);
            tblItemDetails.getColumnModel().getColumn(4).setPreferredWidth(60);
            tblItemDetails.getColumnModel().getColumn(4).setMaxWidth(60);
        }

        panelbody.add(scrollPaneItems, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 160, 760, 350));

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

        lblPromoGroupCode1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblPromoGroupCode1.setText("Select Item   :");
        panelbody.add(lblPromoGroupCode1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, 90, 30));

        txtItemCode.setEditable(false);
        txtItemCode.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtItemCodeMouseClicked(evt);
            }
        });
        txtItemCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtItemCodeActionPerformed(evt);
            }
        });
        panelbody.add(txtItemCode, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 120, 110, 30));
        panelbody.add(lblItemName, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 120, 300, 30));

        btnResetItemDtlGrid.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnResetItemDtlGrid.setForeground(new java.awt.Color(255, 255, 255));
        btnResetItemDtlGrid.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnResetItemDtlGrid.setText("RESET");
        btnResetItemDtlGrid.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        panelbody.add(btnResetItemDtlGrid, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 120, 70, 30));

        btnAdd.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnAdd.setForeground(new java.awt.Color(255, 255, 255));
        btnAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnAdd.setText("ADD");
        btnAdd.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnAdd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnAddMouseClicked(evt);
            }
        });
        panelbody.add(btnAdd, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 120, 60, 30));

        btnRemove.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnRemove.setForeground(new java.awt.Color(255, 255, 255));
        btnRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnRemove.setText("REMOVE");
        btnRemove.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRemove.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRemoveMouseClicked(evt);
            }
        });
        panelbody.add(btnRemove, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 120, 80, 30));

        panelLayout.add(panelbody, new java.awt.GridBagConstraints());

        getContentPane().add(panelLayout, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
    private void txtPromoGroupCodeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPromoGroupCodeMouseClicked
        // TODO add your handling code here:
        funOpenPromoGroupMasterSearch();
    }//GEN-LAST:event_txtPromoGroupCodeMouseClicked
    
    
    private void txtPromoGroupNameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPromoGroupNameMouseClicked
        // TODO add your handling code here:
        try
        {
            if (txtPromoGroupName.getText().trim().length() == 0)
            {
                new frmAlfaNumericKeyBoard(this, true, "1", "Enter Modifier Name").setVisible(true);
                txtPromoGroupName.setText(clsGlobalVarClass.gKeyboardValue.trim());
            }
            else
            {
                new frmAlfaNumericKeyBoard(this, true, txtPromoGroupName.getText().trim(), "1", "Enter Modifier Name").setVisible(true);
                txtPromoGroupName.setText(clsGlobalVarClass.gKeyboardValue.trim());
            }

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_txtPromoGroupNameMouseClicked

    private void txtPromoGroupNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPromoGroupNameKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            if (txtPromoGroupName.getText().trim().contains("'"))
            {
                JOptionPane.showMessageDialog(null, "Invalid Character '");
                return;
            }
        }
    }//GEN-LAST:event_txtPromoGroupNameKeyPressed

    private void tblItemDetailsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblItemDetailsMouseClicked
        
    }//GEN-LAST:event_tblItemDetailsMouseClicked

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

    private void btnResetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnResetMouseClicked
        // TODO add your handling code here:
        funResetFields();
    }//GEN-LAST:event_btnResetMouseClicked

    private void btnCancelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseClicked
        // TODO add your handling code here:
        dispose();
        clsGlobalVarClass.hmActiveForms.remove("Item Modifier");
    }//GEN-LAST:event_btnCancelMouseClicked

    private void txtPromoGroupCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPromoGroupCodeKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() == '?' || evt.getKeyChar() == '/')
        {
            funOpenPromoGroupMasterSearch();
        }
        if (evt.getKeyCode() == 10)
        {
            txtPromoGroupName.requestFocus();
        }
    }//GEN-LAST:event_txtPromoGroupCodeKeyPressed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        funValidateAndSave();
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        // TODO add your handling code here:
        funResetFields();
    }//GEN-LAST:event_btnResetActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        dispose();
        clsGlobalVarClass.hmActiveForms.remove("Promotion Group Master");
    }//GEN-LAST:event_btnCancelActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
        clsGlobalVarClass.hmActiveForms.remove("Promotion Group Master");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        clsGlobalVarClass.hmActiveForms.remove("Promotion Group Master");
    }//GEN-LAST:event_formWindowClosing

    private void btnNewKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnNewKeyPressed
        // TODO add your handling code here:
         
        if (evt.getKeyCode() == 10)
        {
            funValidateAndSave();
        }
    }//GEN-LAST:event_btnNewKeyPressed

    private void txtItemCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtItemCodeActionPerformed
        // TODO add your handling code here:
        funOpenMenuItemSearch();
    }//GEN-LAST:event_txtItemCodeActionPerformed

    private void btnAddMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAddMouseClicked
        // TODO add your handling code here:
        funAddRow();
    }//GEN-LAST:event_btnAddMouseClicked

    private void btnRemoveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRemoveMouseClicked
        // TODO add your handling code here:
        if(tblItemDetails.getSelectedRow()>-1)
        {
            funRemoveRow();
        }
        else
        {
            JOptionPane.showMessageDialog(null, "Please select item from grid to delete");
        }
    }//GEN-LAST:event_btnRemoveMouseClicked

    private void txtItemCodeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtItemCodeMouseClicked
        // TODO add your handling code here:
        funOpenMenuItemSearch();
    }//GEN-LAST:event_txtItemCodeMouseClicked
   
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnRemove;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnResetItemDtlGrid;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblFormName;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblItemName;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName1;
    private javax.swing.JLabel lblPromoGroupCode;
    private javax.swing.JLabel lblPromoGroupCode1;
    private javax.swing.JLabel lblPromoGroupName;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblformName;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelLayout;
    private javax.swing.JPanel panelbody;
    private javax.swing.JScrollPane scrollPaneItems;
    private javax.swing.JTable tblItemDetails;
    private javax.swing.JTextField txtItemCode;
    private javax.swing.JTextField txtPromoGroupCode;
    private javax.swing.JTextField txtPromoGroupName;
    // End of variables declaration//GEN-END:variables
}
