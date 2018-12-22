/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSMaster.view;

import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsUtility;
import com.POSGlobal.view.frmNumericKeyboard;
import com.POSGlobal.view.frmOkPopUp;
import com.POSGlobal.view.frmSearchFormDialog;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author sss11
 */
public class frmRecipeMaster extends javax.swing.JFrame
{

    private DefaultTableModel dmChildRows, dmEmptyModel;
    private String selectedItemCode;
    clsUtility objUtility = new clsUtility();
    
    /**
     * This method is used to initialize frmRecipeMaster
     */
    public frmRecipeMaster()
    {
        initComponents();
        try
        {
            selectedItemCode = "";
            txtRecipeCode.requestFocus();
            Timer timer = new Timer(500, new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
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
            lblUserCode.setText(clsGlobalVarClass.gUserCode);
            lblPosName.setText(clsGlobalVarClass.gPOSName);
            lblModuleName.setText(clsGlobalVarClass.gSelectedModule);

            dmEmptyModel = (DefaultTableModel) tblChildItems.getModel();
            dmChildRows = (DefaultTableModel) tblChildItems.getModel();
            funSetFormToInDateChosser();
            funSetShortCutKeys();
            txtRecipeCode.requestFocus();

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

    /**
     * This method is used to set data chooser
     */
    private void funSetFormToInDateChosser()
    {
        try
        {
              java.util.Date posDate = new SimpleDateFormat("dd-MM-yyyy").parse(clsGlobalVarClass.gPOSDateToDisplay);
            dteFromDate.setDate(posDate);
            dteToDate.setDate(posDate);
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    /**
     * This method is used to selected menu item
     *
     * @param itemType
     */
    private void funSelectMenuItem(String itemType)
    {
        try
        {
            if (itemType.equals("Parent"))
            {
                clsUtility obj=new clsUtility();
                obj.funCallForSearchForm("MenuItemForPrice");
            }
            else
            {
                clsUtility obj=new clsUtility();
                obj.funCallForSearchForm("MenuItemForRecipeChild");
            }
            new frmSearchFormDialog(this, true).setVisible(true);
            if (clsGlobalVarClass.gSearchItemClicked)
            {
                Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
                funSetMenuItemData(data, itemType);
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
     * This method is used to set menu item data
     *
     * @param data
     * @param itemType
     */
    private void funSetMenuItemData(Object[] data, String itemType)
    {
        if (itemType.equalsIgnoreCase("Parent"))
        {
            txtMenuItemCode.setText(data[0].toString());
            lblMenuItemName.setText(data[1].toString());
        }
        else
        {
            txtChildItemName.setText(data[1].toString());
            selectedItemCode = data[0].toString();
            txtQty.requestFocus();
        }
    }

    /**
     * This method is used to add row
     *
     * @param itemCode
     * @param itemName
     * @param qty
     */
    private void funAddRow(String itemCode, String itemName, String qty)
    {
        try
        {
            Object[] column = new Object[4];
            long result;

            column[0] = itemCode;
            column[1] = itemName;
            column[2] = qty;
            column[3] = false;

            dmChildRows.addRow(column);
            tblChildItems.setModel(dmChildRows);
            tblChildItems.setRowHeight(30);

            DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
            rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
            leftRenderer.setHorizontalAlignment(JLabel.LEFT);
            tblChildItems.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);

            tblChildItems.getColumnModel().getColumn(0).setPreferredWidth(0);
            tblChildItems.getColumnModel().getColumn(1).setPreferredWidth(200);
            tblChildItems.getColumnModel().getColumn(2).setPreferredWidth(40);
            tblChildItems.getColumnModel().getColumn(3).setPreferredWidth(20);
            tblChildItems.setSize(260, 900);

            funResetChildItemFields();

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    /**
     * This method is used to load menu item
     */
    private void funLoadMenuItem()
    {
        try
        {
            String sql = "select strItemCode,strItemName from tblitemmaster";
            ResultSet rsMenuItem = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            while (rsMenuItem.next())
            {
                Object row[] =
                {
                    rsMenuItem.getString(2), 0, false
                };
                dmChildRows.addRow(row);
            }
            tblChildItems.setModel(dmChildRows);
            rsMenuItem.close();

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    /**
     * This method is used to remove row
     */
    private void funRemoveRow()
    {
        int rowNo = tblChildItems.getRowCount();
        java.util.Vector vIndexToDelete = new java.util.Vector();
        int rowCount = 0;
        for (int i = 0; i < rowNo; i++)
        {
            boolean select = Boolean.parseBoolean(tblChildItems.getValueAt(i, 3).toString());
            if (select)
            {
                rowCount++;
                vIndexToDelete.add(i);
            }
        }
        int cnt = 0;
        while (cnt < tblChildItems.getRowCount())
        {
            boolean select = Boolean.parseBoolean(tblChildItems.getValueAt(cnt, 3).toString());
            if (select)
            {
                dmChildRows.removeRow(cnt);
            }
            else
            {
                cnt++;
            }
        }
        btnRemove.setEnabled(false);
    }

    /**
     * This method is used to check selection
     */
    private void funCheckSelection()
    {
        int row = 0;
        boolean flg = false;
        int rowNo = tblChildItems.getSelectedRow();
        String rowValue = tblChildItems.getValueAt(rowNo, 3).toString();
        if (Boolean.parseBoolean(rowValue))
        {
            btnRemove.setEnabled(true);
        }

        boolean flgSelect = false;
        for (int i = 0; i < tblChildItems.getRowCount(); i++)
        {
            String rowValue1 = tblChildItems.getValueAt(i, 3).toString();
            if (Boolean.parseBoolean(rowValue1))
            {
                flgSelect = true;
                break;
            }
        }
        if (!flgSelect)
        {
            btnRemove.setEnabled(false);
        }
    }

    /**
     * This method is used to save recipe
     */
    private void funSaveRecipeMaster()
    {
        try
        {
            long lastNo = funGenerateRecipeCode();
            String recipeCode = "R" + String.format("%07d", lastNo);
            String fromDate = (dteFromDate.getDate().getYear() + 1900) + "-" + (dteFromDate.getDate().getMonth() + 1) + "-" + (dteFromDate.getDate().getDate());
            String toDate = (dteToDate.getDate().getYear() + 1900) + "-" + (dteToDate.getDate().getMonth() + 1) + "-" + (dteToDate.getDate().getDate());

            String sqlSaveRecipe = "insert into tblrecipehd (strRecipeCode,strItemCode"
                                   + ",dteFromDate,dteToDate,strPOSCode,strUserCreated,strUserEdited,dteDateCreated"
                                   + ",dteDateEdited,strClientCode) "
                                   + "values('" + recipeCode + "','" + txtMenuItemCode.getText().trim() + "'" + ",'" + fromDate + "','" + toDate + "'"
                                   + ",'" + clsGlobalVarClass.gPOSCode + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "'"
                                   + ",'" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "'"
                                   + ",'" + clsGlobalVarClass.gClientCode + "')";
            clsGlobalVarClass.dbMysql.execute(sqlSaveRecipe);

            if (funSaveRecipeDtl(recipeCode) == 1)
            {
                String sqlInternalUpdate = "update tblinternal set dblLastNo=" + lastNo + " "
                                           + "where strTransactionType='Recipe'";
                clsGlobalVarClass.dbMysql.execute(sqlInternalUpdate);
                JOptionPane.showMessageDialog(this, "Record Saved Successfully.");
                funResetFields();
            }

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    /**
     * This method is used to update recipe
     */
    private void funUpdateRecipeMaster()
    {
        try
        {
            String fromDate = (dteFromDate.getDate().getYear() + 1900) + "-" + (dteFromDate.getDate().getMonth() + 1) + "-" + (dteFromDate.getDate().getDate());
            String toDate = (dteToDate.getDate().getYear() + 1900) + "-" + (dteToDate.getDate().getMonth() + 1) + "-" + (dteToDate.getDate().getDate());
            String sqlUpdateRecipe = "update tblrecipehd set "
                                     + "strItemCode='" + txtMenuItemCode.getText().trim() + "'"
                                     + ",dteFromDate='" + fromDate + "',dteToDate='" + toDate + "'"
                                     + ",strPOSCode='" + clsGlobalVarClass.gPOSCode + "'"
                                     + ",strUserEdited='" + clsGlobalVarClass.gUserCode + "'"
                                     + ",dteDateEdited='" + clsGlobalVarClass.getCurrentDateTime() + "'"
                                     + ",strClientCode='" + clsGlobalVarClass.gClientCode + "',strDataPostFlag='N' "
                                     + "where strRecipeCode='" + txtRecipeCode.getText().trim() + "'";
            clsGlobalVarClass.dbMysql.execute(sqlUpdateRecipe);

            if (funSaveRecipeDtl(txtRecipeCode.getText().trim()) == 1)
            {
                JOptionPane.showMessageDialog(this, "Records Updated Successfully.");
                funResetFields();
            }

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    /**
     * This method is used to save recipe dtl
     *
     * @param recipeCode
     * @return
     * @throws Exception
     */
    private int funSaveRecipeDtl(String recipeCode) throws Exception
    {
        String sqlDelete = "delete from tblrecipedtl where strRecipeCode='" + recipeCode + "'";
        clsGlobalVarClass.dbMysql.execute(sqlDelete);
        for (int cnt = 0; cnt < tblChildItems.getRowCount(); cnt++)
        {
            String childItemCode = tblChildItems.getValueAt(cnt, 0).toString();
            String childItemName = tblChildItems.getValueAt(cnt, 1).toString();
            String childItemQty = tblChildItems.getValueAt(cnt, 2).toString();

            String sqlSaveRecipe = "insert into tblrecipedtl (strRecipeCode,strChildItemCode"
                                   + ",dblQuantity,strPOSCode,strClientCode,strDataPostFlag) "
                                   + "values('" + recipeCode + "','" + childItemCode + "','" + childItemQty + "'"
                                   + ",'" + clsGlobalVarClass.gPOSCode + "','" + clsGlobalVarClass.gClientCode + "','N')";
            clsGlobalVarClass.dbMysql.execute(sqlSaveRecipe);
        }
        return 1;
    }

    /**
     * This method is used to generate recipe code
     *
     * @return
     * @throws Exception
     */
    private long funGenerateRecipeCode() throws Exception
    {
        long lastNo = 0;

        String sqlInternal = "select dblLastNo from tblinternal "
                             + "where strTransactionType='Recipe'";
        ResultSet rsInternal = clsGlobalVarClass.dbMysql.executeResultSet(sqlInternal);
        if (rsInternal.next())
        {
            lastNo = rsInternal.getLong(1);
            lastNo++;
        }

        return lastNo;
    }

    /**
     * This method is used to select recipe data
     */
    private void funSelectRecipeData()
    {
        try
        {
            clsUtility obj=new clsUtility();
            obj.funCallForSearchForm("Recipe");
            new frmSearchFormDialog(this, true).setVisible(true);
            if (clsGlobalVarClass.gSearchItemClicked)
            {
                Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
                funSetRecipeData(data);
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
     * This method is used to set recipe data
     *
     * @param data
     * @throws Exception
     */
    private void funSetRecipeData(Object[] data) throws Exception
    {
        btnNew.setText("UPDATE");
        btnNew.setMnemonic('u');
        txtRecipeCode.setText(data[0].toString());
        String sql = "select a.strItemCode,b.strItemName,dteFromDate,dteToDate "
                     + "from tblrecipehd a,tblitemmaster b "
                     + "where a.strItemCode=b.strItemCode and strRecipeCode='" + data[0].toString() + "'";
        ResultSet rsRecipeHd = clsGlobalVarClass.dbMysql.executeResultSet(sql);
        if (rsRecipeHd.next())
        {
            txtMenuItemCode.setText(rsRecipeHd.getString(1));
            lblMenuItemName.setText(rsRecipeHd.getString(2));

            String fromDate = rsRecipeHd.getString(3);
            String[] spFrom = fromDate.split(" ");
            String[] spFrom1 = spFrom[0].split("-");

            fromDate = spFrom1[2] + "-" + spFrom1[1] + "-" + spFrom1[0];
            java.util.Date date = new SimpleDateFormat("dd-MM-yyyy").parse(fromDate);
            dteFromDate.setDate(date);

            String toDate = rsRecipeHd.getString(4);
            String[] spTo = toDate.split(" ");
            String[] spTo1 = spTo[0].split("-");

            toDate = spTo1[2] + "-" + spTo1[1] + "-" + spTo1[0];
            date = new SimpleDateFormat("dd-MM-yyyy").parse(toDate);
            dteToDate.setDate(date);

            funSetRecipeDtl(data[0].toString());
        }
    }

    /**
     * This method is used to set recipe dtl
     *
     * @param recipeCode
     * @throws Exception
     */
    private void funSetRecipeDtl(String recipeCode) throws Exception
    {
        dmChildRows.setRowCount(0);
        String sql = "select a.strChildItemCode,a.dblQuantity,b.strItemName "
                     + "from tblrecipedtl a,tblitemmaster b "
                     + "where a.strChildItemCode=b.strItemCode and strRecipeCode='" + recipeCode + "'";
        System.out.println(sql);
        ResultSet rsRecipeDtl = clsGlobalVarClass.dbMysql.executeResultSet(sql);
        while (rsRecipeDtl.next())
        {
            funAddRow(rsRecipeDtl.getString(1), rsRecipeDtl.getString(3), rsRecipeDtl.getString(2));
        }
    }

    /**
     * This method is used to reset fields
     */
    private void funResetFields()
    {
        txtMenuItemCode.setText("");
        txtRecipeCode.setText("");
        lblMenuItemName.setText("");
        funResetChildItemFields();
        funSetFormToInDateChosser();
        dmChildRows.setRowCount(0);
        btnNew.setMnemonic('s');
        txtRecipeCode.requestFocus();

    }

    /**
     * This method is used to reset child item fields
     */
    private void funResetChildItemFields()
    {
        txtChildItemName.setText("");
        txtQty.setText("0");

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelheader = new javax.swing.JPanel();
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
        }; ;
        panelbody = new javax.swing.JPanel();
        lblFormName = new javax.swing.JLabel();
        lblRecipeCode = new javax.swing.JLabel();
        lblItemName = new javax.swing.JLabel();
        btnNew = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        txtRecipeCode = new javax.swing.JTextField();
        txtMenuItemCode = new javax.swing.JTextField();
        lblMenuItemName = new javax.swing.JLabel();
        dteFromDate = new com.toedter.calendar.JDateChooser();
        lblFromDate = new javax.swing.JLabel();
        dteToDate = new com.toedter.calendar.JDateChooser();
        lblToDate = new javax.swing.JLabel();
        scrollPane = new javax.swing.JScrollPane();
        tblChildItems = new javax.swing.JTable();
        lblChildItemName = new javax.swing.JLabel();
        txtChildItemName = new javax.swing.JTextField();
        btnAdd = new javax.swing.JButton();
        btnResetChild = new javax.swing.JButton();
        lblQty = new javax.swing.JLabel();
        txtQty = new javax.swing.JTextField();
        btnRemove = new javax.swing.JButton();

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

        panelheader.setBackground(new java.awt.Color(69, 164, 238));
        panelheader.setLayout(new javax.swing.BoxLayout(panelheader, javax.swing.BoxLayout.LINE_AXIS));

        lblProductName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblProductName.setForeground(new java.awt.Color(255, 255, 255));
        lblProductName.setText("SPOS - ");
        panelheader.add(lblProductName);

        lblModuleName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblModuleName.setForeground(new java.awt.Color(255, 255, 255));
        panelheader.add(lblModuleName);

        lblformName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblformName.setForeground(new java.awt.Color(255, 255, 255));
        lblformName.setText("- Recipe Master");
        panelheader.add(lblformName);
        panelheader.add(filler4);
        panelheader.add(filler5);

        lblPosName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblPosName.setForeground(new java.awt.Color(255, 255, 255));
        lblPosName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPosName.setMaximumSize(new java.awt.Dimension(321, 30));
        lblPosName.setMinimumSize(new java.awt.Dimension(321, 30));
        lblPosName.setPreferredSize(new java.awt.Dimension(321, 30));
        panelheader.add(lblPosName);
        panelheader.add(filler6);

        lblUserCode.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblUserCode.setForeground(new java.awt.Color(255, 255, 255));
        lblUserCode.setMaximumSize(new java.awt.Dimension(90, 30));
        lblUserCode.setMinimumSize(new java.awt.Dimension(90, 30));
        lblUserCode.setPreferredSize(new java.awt.Dimension(90, 30));
        panelheader.add(lblUserCode);

        lblDate.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblDate.setForeground(new java.awt.Color(255, 255, 255));
        lblDate.setMaximumSize(new java.awt.Dimension(192, 30));
        lblDate.setMinimumSize(new java.awt.Dimension(192, 30));
        lblDate.setPreferredSize(new java.awt.Dimension(192, 30));
        panelheader.add(lblDate);

        lblHOSign.setMaximumSize(new java.awt.Dimension(34, 30));
        lblHOSign.setMinimumSize(new java.awt.Dimension(34, 30));
        lblHOSign.setPreferredSize(new java.awt.Dimension(34, 30));
        panelheader.add(lblHOSign);

        getContentPane().add(panelheader, java.awt.BorderLayout.PAGE_START);

        panelLayout.setOpaque(false);
        panelLayout.setLayout(new java.awt.GridBagLayout());

        panelbody.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        panelbody.setMinimumSize(new java.awt.Dimension(800, 570));
        panelbody.setOpaque(false);

        lblFormName.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        lblFormName.setForeground(new java.awt.Color(24, 19, 19));
        lblFormName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblFormName.setText("Recipe Master");

        lblRecipeCode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblRecipeCode.setText("Recipe Code          :");

        lblItemName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblItemName.setText("Menu Item Name   :");

        btnNew.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnNew.setForeground(new java.awt.Color(255, 255, 255));
        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnNew.setText("SAVE");
        btnNew.setToolTipText("Save Recipe");
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

        btnCancel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnCancel.setForeground(new java.awt.Color(255, 255, 255));
        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnCancel.setText("CLOSE");
        btnCancel.setToolTipText("Close Recipe Master");
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

        txtRecipeCode.setEditable(false);
        txtRecipeCode.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtRecipeCodeMouseClicked(evt);
            }
        });
        txtRecipeCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtRecipeCodeKeyPressed(evt);
            }
        });

        txtMenuItemCode.setEditable(false);
        txtMenuItemCode.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtMenuItemCodeMouseClicked(evt);
            }
        });
        txtMenuItemCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtMenuItemCodeKeyPressed(evt);
            }
        });

        dteFromDate.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                dteFromDateKeyPressed(evt);
            }
        });

        lblFromDate.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblFromDate.setText("From Date             :");

        dteToDate.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                dteToDateKeyPressed(evt);
            }
        });

        lblToDate.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblToDate.setText("To Date           :");

        tblChildItems.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ItemCode", "Item Name", "Quantity", "Select"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblChildItems.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblChildItemsMouseClicked(evt);
            }
        });
        scrollPane.setViewportView(tblChildItems);

        lblChildItemName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblChildItemName.setText("Child Item Name    :");

        txtChildItemName.setEditable(false);
        txtChildItemName.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtChildItemNameMouseClicked(evt);
            }
        });
        txtChildItemName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtChildItemNameKeyPressed(evt);
            }
        });

        btnAdd.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnAdd.setForeground(new java.awt.Color(255, 255, 255));
        btnAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnAdd.setText("ADD");
        btnAdd.setToolTipText("Add Item");
        btnAdd.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnAdd.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnAdd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnAddMouseClicked(evt);
            }
        });

        btnResetChild.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnResetChild.setForeground(new java.awt.Color(255, 255, 255));
        btnResetChild.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnResetChild.setText("RESET");
        btnResetChild.setToolTipText("Reset All Fields");
        btnResetChild.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnResetChild.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnResetChild.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnResetChildMouseClicked(evt);
            }
        });

        lblQty.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblQty.setText("Quantity           :");

        txtQty.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtQty.setText("0");
        txtQty.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtQtyMouseClicked(evt);
            }
        });
        txtQty.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtQtyKeyPressed(evt);
            }
        });

        btnRemove.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnRemove.setForeground(new java.awt.Color(255, 255, 255));
        btnRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnRemove.setText("REMOVE");
        btnRemove.setToolTipText("Remove Item");
        btnRemove.setEnabled(false);
        btnRemove.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRemove.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnRemove.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRemoveMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout panelbodyLayout = new javax.swing.GroupLayout(panelbody);
        panelbody.setLayout(panelbodyLayout);
        panelbodyLayout.setHorizontalGroup(
            panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelbodyLayout.createSequentialGroup()
                .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelbodyLayout.createSequentialGroup()
                        .addGap(118, 118, 118)
                        .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(panelbodyLayout.createSequentialGroup()
                                    .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(37, 37, 37)
                                    .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(scrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 534, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelbodyLayout.createSequentialGroup()
                                .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelbodyLayout.createSequentialGroup()
                                        .addComponent(lblChildItemName, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtChildItemName, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addGroup(panelbodyLayout.createSequentialGroup()
                                            .addComponent(lblRecipeCode, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(txtRecipeCode))
                                        .addGroup(panelbodyLayout.createSequentialGroup()
                                            .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                .addComponent(lblFromDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(lblItemName, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGap(18, 18, 18)
                                            .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(dteFromDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(txtMenuItemCode, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                    .addGroup(panelbodyLayout.createSequentialGroup()
                                        .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(29, 29, 29)
                                        .addComponent(btnRemove, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(btnResetChild, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblMenuItemName, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(panelbodyLayout.createSequentialGroup()
                                        .addGap(202, 202, 202)
                                        .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelbodyLayout.createSequentialGroup()
                                        .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(lblToDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(lblQty))
                                        .addGap(18, 18, 18)
                                        .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(dteToDate, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtQty, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(30, 30, 30))))))
                    .addGroup(panelbodyLayout.createSequentialGroup()
                        .addGap(268, 268, 268)
                        .addComponent(lblFormName, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(13, Short.MAX_VALUE))
        );
        panelbodyLayout.setVerticalGroup(
            panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelbodyLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblFormName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelbodyLayout.createSequentialGroup()
                        .addGap(60, 60, 60)
                        .addComponent(lblMenuItemName, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(dteToDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblToDate, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(27, 27, 27)
                        .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblQty, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtQty, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panelbodyLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                        .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblRecipeCode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtRecipeCode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblItemName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtMenuItemCode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(dteFromDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblFromDate, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(26, 26, 26)
                        .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblChildItemName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtChildItemName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnResetChild, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRemove, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(scrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelbodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        panelLayout.add(panelbody, new java.awt.GridBagConstraints());

        getContentPane().add(panelLayout, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnNewMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNewMouseClicked

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = dteFromDate.getDate();
        Date date2 = dteToDate.getDate();
        long diff = date2.getTime() - date1.getTime();
        System.out.println("diff==" + diff);
        if (diff < 0)
        {
            new frmOkPopUp(this, "Invalid To Date", "Error", 1).setVisible(true);
            return;
        }

        if (btnNew.getText().equalsIgnoreCase("Save"))
        {
            funSaveRecipeMaster();
        }
        else
        {
            funUpdateRecipeMaster();
        }
    }//GEN-LAST:event_btnNewMouseClicked

    private void btnResetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnResetMouseClicked
        // TODO add your handling code here:
        funResetFields();
    }//GEN-LAST:event_btnResetMouseClicked

    private void btnCancelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseClicked
        // TODO add your handling code here:
        dispose();
         clsGlobalVarClass.hmActiveForms.remove("RecipeMaster");
    }//GEN-LAST:event_btnCancelMouseClicked

    private void txtRecipeCodeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtRecipeCodeMouseClicked
        // TODO add your handling code here:
        funSelectRecipeData();
    }//GEN-LAST:event_txtRecipeCodeMouseClicked

    private void txtMenuItemCodeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtMenuItemCodeMouseClicked
        // TODO add your handling code here:
        funSelectMenuItem("Parent");
    }//GEN-LAST:event_txtMenuItemCodeMouseClicked

    private void tblChildItemsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblChildItemsMouseClicked
        // TODO add your handling code here:
        funCheckSelection();
    }//GEN-LAST:event_tblChildItemsMouseClicked

    private void txtChildItemNameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtChildItemNameMouseClicked
        // TODO add your handling code here:
        funSelectMenuItem("Child");
    }//GEN-LAST:event_txtChildItemNameMouseClicked

    private void btnAddMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAddMouseClicked
        // TODO add your handling code here:
        if (txtChildItemName.getText().trim().length() == 0)
        {
            JOptionPane.showMessageDialog(this, "Please Select Child Item!");
            return;
        }
        if (!clsGlobalVarClass.validateNumbers(txtQty.getText().trim()))
        {
            JOptionPane.showMessageDialog(this, "Please Enter Numbers Only in Quantity Field!");
            txtQty.requestFocus();
            return;
        }
        funAddRow(selectedItemCode, txtChildItemName.getText().trim(), txtQty.getText().trim());
    }//GEN-LAST:event_btnAddMouseClicked

    private void btnResetChildMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnResetChildMouseClicked
        // TODO add your handling code here:
        funResetFields();
    }//GEN-LAST:event_btnResetChildMouseClicked

    private void txtQtyMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtQtyMouseClicked
        // TODO add your handling code here:
        if (txtQty.getText().length() == 0)
        {
            new frmNumericKeyboard(this,true,"","Double", "Enter Item Quantity").setVisible(true);
            txtQty.setText(clsGlobalVarClass.gNumerickeyboardValue);
        }
        else
        {
            new frmNumericKeyboard(this,true,"","Double", "Enter Item Quantity").setVisible(true);
            txtQty.setText(clsGlobalVarClass.gNumerickeyboardValue);
        }
    }//GEN-LAST:event_txtQtyMouseClicked

    private void btnRemoveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRemoveMouseClicked
        // TODO add your handling code here:
        funRemoveRow();
    }//GEN-LAST:event_btnRemoveMouseClicked

    private void txtMenuItemCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMenuItemCodeKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            dteFromDate.requestFocus();
        }
    }//GEN-LAST:event_txtMenuItemCodeKeyPressed

    private void dteFromDateKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dteFromDateKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            dteToDate.requestFocus();
        }
    }//GEN-LAST:event_dteFromDateKeyPressed

    private void dteToDateKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dteToDateKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            txtChildItemName.requestFocus();
        }
    }//GEN-LAST:event_dteToDateKeyPressed

    private void txtChildItemNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtChildItemNameKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            txtQty.requestFocus();
        }
    }//GEN-LAST:event_txtChildItemNameKeyPressed

    private void txtQtyKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtQtyKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            btnAdd.requestFocus();
        }
    }//GEN-LAST:event_txtQtyKeyPressed

    private void txtRecipeCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRecipeCodeKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() == '?' || evt.getKeyChar() == '/')
        {
            funSelectRecipeData();
        }
        if (evt.getKeyCode() == 10)
        {
            txtMenuItemCode.requestFocus();
        }
    }//GEN-LAST:event_txtRecipeCodeKeyPressed

    private void btnNewKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnNewKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnNewKeyPressed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = dteFromDate.getDate();
        Date date2 = dteToDate.getDate();
        long diff = date2.getTime() - date1.getTime();
        System.out.println("diff==" + diff);
        if (diff < 0)
        {
            new frmOkPopUp(this, "Invalid To Date", "Error", 1).setVisible(true);
            return;
        }

        if (btnNew.getText().equalsIgnoreCase("Save"))
        {
            funSaveRecipeMaster();
        }
        else
        {
            funUpdateRecipeMaster();
        }
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        // TODO add your handling code here:
        funResetFields();
    }//GEN-LAST:event_btnResetActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        dispose();
         clsGlobalVarClass.hmActiveForms.remove("RecipeMaster");
    }//GEN-LAST:event_btnCancelActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
         clsGlobalVarClass.hmActiveForms.remove("RecipeMaster");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
         clsGlobalVarClass.hmActiveForms.remove("RecipeMaster");
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
            java.util.logging.Logger.getLogger(frmRecipeMaster.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(frmRecipeMaster.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(frmRecipeMaster.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(frmRecipeMaster.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                new frmRecipeMaster().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnRemove;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnResetChild;
    private com.toedter.calendar.JDateChooser dteFromDate;
    private com.toedter.calendar.JDateChooser dteToDate;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JLabel lblChildItemName;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblFormName;
    private javax.swing.JLabel lblFromDate;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblItemName;
    private javax.swing.JLabel lblMenuItemName;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblQty;
    private javax.swing.JLabel lblRecipeCode;
    private javax.swing.JLabel lblToDate;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblformName;
    private javax.swing.JPanel panelLayout;
    private javax.swing.JPanel panelbody;
    private javax.swing.JPanel panelheader;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JTable tblChildItems;
    private javax.swing.JTextField txtChildItemName;
    private javax.swing.JTextField txtMenuItemCode;
    private javax.swing.JTextField txtQty;
    private javax.swing.JTextField txtRecipeCode;
    // End of variables declaration//GEN-END:variables
}
