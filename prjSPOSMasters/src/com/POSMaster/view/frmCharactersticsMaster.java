/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSMaster.view;

import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsInvokeDataFromSanguineERPModules;
import com.POSGlobal.controller.clsLinkupDtl;
import com.POSGlobal.controller.clsUtility;
import com.POSGlobal.view.frmOkPopUp;
import com.POSGlobal.view.frmSearchFormDialog;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class frmCharactersticsMaster extends javax.swing.JFrame
{
    private String sql;
    DefaultTableModel dmCharValueLinkup;
    clsUtility objUtility = new clsUtility();

    public frmCharactersticsMaster()
    {
        initComponents();
        try
        {
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
            dmCharValueLinkup = (DefaultTableModel) tblCharValueLinkup.getModel();
            lblUserCode.setText(clsGlobalVarClass.gUserCode);
            lblPosName.setText(clsGlobalVarClass.gPOSName);
            lblModuleName.setText(clsGlobalVarClass.gSelectedModule);
            lblDate.setText(clsGlobalVarClass.gPOSDateToDisplay);
            txtCharName.requestFocus();
            if (cmbCharType.getSelectedItem().equals("Value"))
            {
                scrCharacteristics.setVisible(true);
                btnAdd.setVisible(true);
                btnRemove.setVisible(true);
                btnLinkupReset.setVisible(true);
            }
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
     * This method is used to save or update master
     */
    private void funSaveAndUpdate()
    {
        try
        {
            if (btnNew.getText().equalsIgnoreCase("SAVE"))
            {
                funSave();
            }
            else
            {
                funUpdate();
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    /**
     * This method is used to save area
     *
     * @return
     */
    private void funSave()
    {
        try
        {            
            if (funCheckWSCharCodeForCharacter(""))
            {
                JOptionPane.showMessageDialog(this, "This WS Characterstics code already used for Characterstic in POS");
                return;
            }
            sql="delete from tblcharactersticsmaster where strWSCharCode='"+txtWSCharCode.getText().toString()+"'";
            clsGlobalVarClass.dbMysql.execute(sql);
            txtCharCode.setText(funGenerateCharCode());
            if (cmbCharType.getSelectedItem().toString().equals("Text"))
            {   
                sql = "insert into tblcharactersticsmaster (strCharCode,strCharName,strCharType,strWSCharCode,strValue"
                        + ",strUserCreated,strUserEdited,dteDateCreated,dteDateEdited,strClientCode,strDataPostFlag,strPOSCode)"
                        + "values('" + txtCharCode.getText() + "','" + txtCharName.getText() + "','" + cmbCharType.getSelectedItem().toString() + "','" + txtWSCharCode.getText().toString() + "','" + txtTextValue.getText().toString() + "',"
                        + "'" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.gClientCode + "','N','All')";
                System.out.println(sql);
            }
            else if (cmbCharType.getSelectedItem().toString().equals("Value"))
            {
                sql = "insert into tblcharactersticsmaster (strCharCode,strCharName,strCharType,strWSCharCode,strValue"
                        + ",strUserCreated,strUserEdited,dteDateCreated,dteDateEdited,strClientCode,strDataPostFlag,strPOSCode)"
                        + "values('" + txtCharCode.getText() + "','" + txtCharName.getText() + "','" + cmbCharType.getSelectedItem().toString() + "'"
                        + ",'" + txtWSCharCode.getText().toString() + "','',"
                        + "'" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.gClientCode + "','N','All')";
                System.out.println(sql);
            }

            clsGlobalVarClass.dbMysql.execute(sql);
            funSaveLinkedCharactersticsData();
            
            sql="update tblmasteroperationstatus set dteDateEdited='"+clsGlobalVarClass.getCurrentDateTime()+"' "
                + " where strTableName='Characteristics' ";
            clsGlobalVarClass.dbMysql.execute(sql);
            new frmOkPopUp(null, "Entry added Successfully", "Successfull", 3).setVisible(true);
            funResetFields();
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    /**
     * This method is used to update Char Master
     */
    private void funUpdate()
    {
        try
        {
            String updateQuery = "";
            String code = txtCharCode.getText();
            String name = txtCharName.getText();            
            if (funCheckWSCharCodeForCharacter(txtCharCode.getText()))
            {
                if (clsGlobalVarClass.funCheckItemName("tblcharactersticsmaster", "strCharName", "strCharCode", name, code, "update", ""))
                {
                    new frmOkPopUp(null, "Char Name is Already Exist", "Error", 0).setVisible(true);
                    txtCharName.requestFocus();
                }
                else
                {
                    if (cmbCharType.getSelectedItem().toString().equals("Value"))
                    {
                        updateQuery = " UPDATE tblcharactersticsmaster SET strCharName = '" + txtCharName.getText() + "',strCharType='" + cmbCharType.getSelectedItem().toString() + "' "
                            + ",strWSCharCode='" + txtWSCharCode.getText().toString() + "',strValue='',strUserEdited='" + clsGlobalVarClass.gUserCode + "'"
                            + ",dteDateEdited='" + clsGlobalVarClass.getCurrentDateTime() + "',strPOSCode='All' "
                            + " WHERE strCharCode ='" + txtCharCode.getText() + "' ";
                    }
                    else
                    {
                        updateQuery = " UPDATE tblcharactersticsmaster SET strCharName = '" + txtCharName.getText() + "',strCharType='" + cmbCharType.getSelectedItem().toString() + "' "
                            + ",strWSCharCode='" + txtWSCharCode.getText().toString() + "',strValue='" + txtTextValue.getText().toString() + "'"
                            + ",strUserEdited='" + clsGlobalVarClass.gUserCode + "',dteDateEdited='" + clsGlobalVarClass.getCurrentDateTime() + "'"
                            + ",strPOSCode='All' "
                            + " WHERE strCharCode ='" + txtCharCode.getText() + "' ";
                    }
                    int exc = clsGlobalVarClass.dbMysql.execute(updateQuery);
                    funSaveLinkedCharactersticsData();
                    if (exc > 0)
                    {
                        sql="update tblmasteroperationstatus set dteDateEdited='"+clsGlobalVarClass.getCurrentDateTime()+"' "
                            + " where strTableName='Characteristics' ";
                        clsGlobalVarClass.dbMysql.execute(sql);
                        JOptionPane.showMessageDialog(this, "Updated Successfully");
                        funResetFields();
                    }
                }
            }
            else
            {
                JOptionPane.showMessageDialog(this, "This WS Characterstics code already used for Other Characterstic");
                return;
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    /**
     * This method is used to reset all fields
     *
     * @return
     */
    private void funResetFields()
    {
        try
        {
            btnNew.setText("SAVE");
            txtCharName.setText("");
            txtCharCode.setText("");
            txtTextValue.setText("");
            txtWSCharCode.setText("");
            dmCharValueLinkup.setRowCount(0);
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    //help for fetching shape code
    private void funProdCharCodeTextFieldClicked()
    {
        clsInvokeDataFromSanguineERPModules objLinkSangERP=new clsInvokeDataFromSanguineERPModules();
        try
        {
            List<clsLinkupDtl> listProductCharacterstics = objLinkSangERP.funGetProductCharDtl(clsGlobalVarClass.gWSClientCode);

            List<String> listColumns = new ArrayList<String>();
            listColumns.add("Characterstics Code");
            listColumns.add("Chararacterstics Name");
            new frmSearchFormDialog(this, true, listProductCharacterstics, "MMS Products", listColumns).setVisible(true);

            if (clsGlobalVarClass.gSearchItemClicked)
            {
                Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
                txtWSCharCode.setText(data[0].toString());
                clsGlobalVarClass.gSearchItemClicked = false;
            }

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
        finally
        {
            objLinkSangERP=null;
        }
    }

    private void funSearchCharCode()
    {

        clsUtility obj = new clsUtility();
        obj.funCallForSearchForm("CharactersticsMaster");
        new frmSearchFormDialog(null, true).setVisible(true);
        if (clsGlobalVarClass.gSearchItemClicked)
        {
            btnNew.setText("UPDATE");//UpdateD
            Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
            funSetCharacteristicsData(data);
        }
    }

    private void funSetCharacteristicsData(Object[] data)
    {
        try
        {
            sql = "select strCharCode,strCharName,strCharType,strWSCharCode,strValue,strPOSCode from tblcharactersticsmaster where strCharCode='" + clsGlobalVarClass.gSearchedItem + "'";
            ResultSet rsCharInfo = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            if (rsCharInfo.next())
            {
                txtCharCode.setText(rsCharInfo.getString(1));
                txtCharName.setText(rsCharInfo.getString(2));
                cmbCharType.setSelectedItem(rsCharInfo.getString(3));
                txtWSCharCode.setText(rsCharInfo.getString(4));
                String posCode=rsCharInfo.getString(6);

                if (cmbCharType.getSelectedItem().toString().equals("Value"))
                {
                    funLoadCharLinkupData();
                    txtTextValue.setText(rsCharInfo.getString(5));
                }
                else
                {
                    txtTextValue.setText(rsCharInfo.getString(5));
                }

            }
            rsCharInfo.close();

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    //Check duplicate WSChar code for character in POS 
    private boolean funCheckWSCharCodeForCharacter(String charCode) throws Exception
    {
        boolean flgResult = false;
        if (!charCode.isEmpty())
        {
            String query = " select a.strCharCode from tblcharactersticsmaster a "
                + " where a.strWSCharCode='" + txtWSCharCode.getText().toString() + "' and a.strCharCode!='"+charCode+"' ";
            ResultSet rsCheckProdCharCode = clsGlobalVarClass.dbMysql.executeResultSet(query);
            if (rsCheckProdCharCode.next())
            {
                flgResult = false;
            }
            else
            {
                flgResult = true;
            }
            rsCheckProdCharCode.close();
        }
        else
        {
            String query = " select a.strCharCode from tblcharactersticsmaster a "
                + " where a.strWSCharCode='" + txtWSCharCode.getText().toString() + "' ;";
            ResultSet rsCheckProdCharCode = clsGlobalVarClass.dbMysql.executeResultSet(query);
            if (rsCheckProdCharCode.next())
            {
                flgResult = true;
            }
            rsCheckProdCharCode.close();
        }

        return flgResult;

    }

    /**
     * This method is used to generate char codes
     *
     * @return String charCode
     */
    private String funGenerateCharCode()
    {
        String charCode = "";
        try
        {
            sql = "select count(dblLastNo) from tblinternal where strTransactionType='Characterstic'";
            ResultSet rsCharCode = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            rsCharCode.next();
            int charCodeCnt = rsCharCode.getInt(1);
            rsCharCode.close();
            if (charCodeCnt > 0)
            {
                sql = "select dblLastNo from tblinternal where strTransactionType='Characterstic'";
                rsCharCode = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                rsCharCode.next();
                long code = rsCharCode.getLong(1);
                code = code + 1;
                charCode = "CH" + String.format("%06d", code);
                charCode = charCode;
                clsGlobalVarClass.gUpdatekot = true;
                clsGlobalVarClass.gKOTCode = code;
                sql = "update tblinternal set dblLastNo='" + code + "' where strTransactionType='Characterstic'";
                clsGlobalVarClass.dbMysql.execute(sql);
            }
            else
            {
                charCode = "CH000001";
                clsGlobalVarClass.gUpdatekot = false;
                sql = "insert into tblinternal values('Characterstic'," + 1 + ")";
                clsGlobalVarClass.dbMysql.execute(sql);
            }
            //System.out.println("A Code="+areaCode);
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
        return charCode;
    }

    private void funInsertCharValueInRow()
    {
        try
        {
            Object row[] = new Object[]
            {
                txtTextValue.getText()

            };
            dmCharValueLinkup.addRow(row);
            DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
            rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
            leftRenderer.setHorizontalAlignment(JLabel.LEFT);
            tblCharValueLinkup.getColumnModel().getColumn(0).setCellRenderer(leftRenderer);
            tblCharValueLinkup.getColumnModel().getColumn(0).setPreferredWidth(150);
            tblCharValueLinkup.setSize(300, 300);
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    private void funLinkupTableRowSelection()
    {
        int rowNo = tblCharValueLinkup.getSelectedRow();
        String rowValue = tblCharValueLinkup.getValueAt(rowNo, 1).toString();
        if (Boolean.parseBoolean(rowValue))
        {
            btnRemove.setEnabled(true);
        }

        boolean flgSelect = false;
        for (int i = 0; i < tblCharValueLinkup.getRowCount(); i++)
        {
            String rowValue1 = tblCharValueLinkup.getValueAt(i, 1).toString();
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

    private void funRemoveRow()
    {

        int rowNo = tblCharValueLinkup.getRowCount();
        java.util.Vector vIndexToDelete = new java.util.Vector();
        for (int i = 0; i < rowNo; i++)
        {
            boolean select = Boolean.parseBoolean(tblCharValueLinkup.getValueAt(i, 1).toString());
            if (select)
            {
                vIndexToDelete.add(i);
                //dmDeliverycharges.removeRow(i);
            }
        }
        int cnt = 0;
        while (cnt < tblCharValueLinkup.getRowCount())
        {
            boolean select = Boolean.parseBoolean(tblCharValueLinkup.getValueAt(cnt, 1).toString());
            if (select)
            {
                if (tblCharValueLinkup.getRowCount() > 0)
                {
                    dmCharValueLinkup.removeRow(cnt);
                }
            }
            else
            {
                cnt++;
            }
        }
    }

    /**
     * This method is used to reset linkup details
     */
    private void funResetCharValueLinkupField()
    {
        try
        {
            dmCharValueLinkup.setRowCount(0);

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    //Load Characterstics data to fill table tblCharValueLinkup
    private void funLoadCharLinkupData()
    {
        try
        {
            String sql = " select a.strCharValues from tblcharvalue a,tblcharactersticsmaster b "
                    + " where  a.strCharCode=b.strCharCode "
                    + " and a.strPOSCode=b.strPOSCode "
                    + " and a.strCharCode='" + clsGlobalVarClass.gSearchedItem + "' ";
            System.out.println(sql);
            ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);

            dmCharValueLinkup.setRowCount(0);
            while (rs.next())
            {
                Object[] column = new Object[3];
                column[0] = rs.getString(1);
                column[1] = false;

                dmCharValueLinkup.addRow(column);
            }
            rs.close();
            tblCharValueLinkup.setModel(dmCharValueLinkup);

            DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
            rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
            leftRenderer.setHorizontalAlignment(JLabel.LEFT);
            tblCharValueLinkup.getColumnModel().getColumn(0).setCellRenderer(leftRenderer);
            tblCharValueLinkup.getColumnModel().getColumn(0).setPreferredWidth(150);

            tblCharValueLinkup.setSize(700, 900);

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    //Save to linked data to tblCharValue table in database  
    private int funSaveLinkedCharactersticsData() throws Exception
    {
        if (tblCharValueLinkup.getRowCount() > 0)
        {
            String deleteQuery = " delete from tblcharvalue where strCharCode='" + txtCharCode.getText().trim() + "' "
                + " and strPOSCode='All' ";
            clsGlobalVarClass.dbMysql.execute(deleteQuery);                       

            String insertQuery = "insert into tblcharvalue (strCharCode,strCharName,strCharValues,strClientCode,strPOScode,strDataPostFlag) values ";
            for (int row = 0; row < tblCharValueLinkup.getRowCount(); row++)
            {
                if (row == 0)
                {
                    insertQuery += "('" + txtCharCode.getText().toString() + "','" + txtCharName.getText().toString() + "','" + tblCharValueLinkup.getValueAt(row, 0) + "','" + clsGlobalVarClass.gClientCode + "', 'All', 'N') ";
                }
                else
                {
                    insertQuery += ",('" + txtCharCode.getText().toString() + "','" + txtCharName.getText().toString() + "','" + tblCharValueLinkup.getValueAt(row, 0) + "','" + clsGlobalVarClass.gClientCode + "', 'All', 'N') ";
                }
            }
            clsGlobalVarClass.dbMysql.execute(insertQuery);
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
        };  
        ;
        panelBody = new javax.swing.JPanel();
        lblCharCode = new javax.swing.JLabel();
        btnCancel = new javax.swing.JButton();
        btnNew = new javax.swing.JButton();
        lblFormName = new javax.swing.JLabel();
        btnReset = new javax.swing.JButton();
        lblCharName = new javax.swing.JLabel();
        txtCharCode = new javax.swing.JTextField();
        lblCharType = new javax.swing.JLabel();
        cmbCharType = new javax.swing.JComboBox();
        lblWSCharCode = new javax.swing.JLabel();
        txtWSCharCode = new javax.swing.JTextField();
        btnAdd = new javax.swing.JButton();
        btnLinkupReset = new javax.swing.JButton();
        btnRemove = new javax.swing.JButton();
        scrCharacteristics = new javax.swing.JScrollPane();
        tblCharValueLinkup = new javax.swing.JTable();
        txtTextValue = new javax.swing.JTextField();
        txtCharName = new javax.swing.JTextField();

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
        lblProductName.setText("SPOS- ");
        panelHeader.add(lblProductName);

        lblModuleName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblModuleName.setForeground(new java.awt.Color(255, 255, 255));
        panelHeader.add(lblModuleName);

        lblformName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblformName.setForeground(new java.awt.Color(255, 255, 255));
        lblformName.setText("-Characterstics Linkup Master");
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

        panelLayout.setOpaque(false);
        panelLayout.setLayout(new java.awt.GridBagLayout());

        panelBody.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        panelBody.setMinimumSize(new java.awt.Dimension(800, 570));
        panelBody.setOpaque(false);

        lblCharCode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblCharCode.setText("Characterstics Code  :");

        btnCancel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnCancel.setForeground(new java.awt.Color(255, 255, 255));
        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnCancel.setText("CLOSE");
        btnCancel.setToolTipText("Close Area Master");
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

        btnNew.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnNew.setForeground(new java.awt.Color(255, 255, 255));
        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnNew.setText("SAVE");
        btnNew.setToolTipText("Save Area Master");
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

        lblFormName.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        lblFormName.setForeground(new java.awt.Color(14, 7, 7));
        lblFormName.setText("Characterstics Master");

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

        lblCharName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblCharName.setText("Characterstics Name  :");

        txtCharCode.setEditable(false);
        txtCharCode.setEnabled(false);
        txtCharCode.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtCharCodeMouseClicked(evt);
            }
        });
        txtCharCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCharCodeKeyPressed(evt);
            }
        });

        lblCharType.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblCharType.setText("Characterstics Type :");

        cmbCharType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Value", "Text", "Decimal", "Date" }));
        cmbCharType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbCharTypeActionPerformed(evt);
            }
        });
        cmbCharType.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbCharTypeKeyPressed(evt);
            }
        });

        lblWSCharCode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblWSCharCode.setText("WS Char Code :");

        txtWSCharCode.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtWSCharCodeMouseClicked(evt);
            }
        });
        txtWSCharCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtWSCharCodeKeyPressed(evt);
            }
        });

        btnAdd.setBackground(new java.awt.Color(255, 255, 255));
        btnAdd.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnAdd.setForeground(new java.awt.Color(255, 255, 255));
        btnAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnAdd.setText("ADD");
        btnAdd.setToolTipText("Add Delivery Charges");
        btnAdd.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnAdd.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnAdd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnAddMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnAddMouseEntered(evt);
            }
        });
        btnAdd.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnAddKeyPressed(evt);
            }
        });

        btnLinkupReset.setBackground(new java.awt.Color(255, 255, 255));
        btnLinkupReset.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnLinkupReset.setForeground(new java.awt.Color(255, 255, 255));
        btnLinkupReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnLinkupReset.setText("RESET");
        btnLinkupReset.setToolTipText("Add Delivery Charges");
        btnLinkupReset.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnLinkupReset.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnLinkupReset.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnLinkupResetMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnLinkupResetMouseEntered(evt);
            }
        });
        btnLinkupReset.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnLinkupResetKeyPressed(evt);
            }
        });

        btnRemove.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnRemove.setForeground(new java.awt.Color(255, 255, 255));
        btnRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnRemove.setText("Remove");
        btnRemove.setToolTipText(" Remove Delivery Charges");
        btnRemove.setEnabled(false);
        btnRemove.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRemove.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnRemove.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRemoveMouseClicked(evt);
            }
        });
        btnRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveActionPerformed(evt);
            }
        });

        tblCharValueLinkup.setRowHeight(30);
        tblCharValueLinkup.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Value", "Select"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblCharValueLinkup.setColumnSelectionAllowed(true);
        tblCharValueLinkup.getTableHeader().setReorderingAllowed(false);
        tblCharValueLinkup.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblCharValueLinkupMouseClicked(evt);
            }
        });
        scrCharacteristics.setViewportView(tblCharValueLinkup);
        tblCharValueLinkup.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        txtTextValue.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtTextValueMouseClicked(evt);
            }
        });
        txtTextValue.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTextValueKeyPressed(evt);
            }
        });

        txtCharName.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtCharNameMouseClicked(evt);
            }
        });
        txtCharName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCharNameKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout panelBodyLayout = new javax.swing.GroupLayout(panelBody);
        panelBody.setLayout(panelBodyLayout);
        panelBodyLayout.setHorizontalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBodyLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBodyLayout.createSequentialGroup()
                .addGap(282, 282, 282)
                .addComponent(lblFormName, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(291, 291, 291))
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addComponent(scrCharacteristics, javax.swing.GroupLayout.PREFERRED_SIZE, 769, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelBodyLayout.createSequentialGroup()
                                .addComponent(lblCharCode, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtCharCode))
                            .addComponent(txtTextValue)
                            .addGroup(panelBodyLayout.createSequentialGroup()
                                .addComponent(lblCharName, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtCharName)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblCharType)
                            .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(panelBodyLayout.createSequentialGroup()
                                .addComponent(cmbCharType, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblWSCharCode, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelBodyLayout.createSequentialGroup()
                                .addComponent(btnLinkupReset, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnRemove, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtWSCharCode, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18))))
        );
        panelBodyLayout.setVerticalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(lblFormName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCharCode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCharCode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtCharName, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCharType, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                    .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblCharName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmbCharType, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblWSCharCode, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtWSCharCode, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtTextValue, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                    .addComponent(btnRemove, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnLinkupReset, javax.swing.GroupLayout.PREFERRED_SIZE, 37, Short.MAX_VALUE)))
                .addGap(18, 18, 18)
                .addComponent(scrCharacteristics, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(142, 142, 142)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24))
        );

        panelLayout.add(panelBody, new java.awt.GridBagConstraints());

        getContentPane().add(panelLayout, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseClicked
        // TODO add your handling code here:
        dispose();
         clsGlobalVarClass.hmActiveForms.remove("Characterstics Master");
    }//GEN-LAST:event_btnCancelMouseClicked

    private void btnNewMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNewMouseClicked
        // TODO add your handling code here:
        if (txtCharName.getText().isEmpty())
        {
            JOptionPane.showMessageDialog(this, "Enter Char Name !!!");
            return;
        }

        funSaveAndUpdate();

    }//GEN-LAST:event_btnNewMouseClicked

    private void btnResetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnResetMouseClicked
        funResetFields();
    }//GEN-LAST:event_btnResetMouseClicked

    private void btnNewKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnNewKeyPressed
        // TODO add your handling code here:

    }//GEN-LAST:event_btnNewKeyPressed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:

        if (txtCharName.getText().isEmpty())
        {
            JOptionPane.showMessageDialog(this, "Enter Char Name !!!");
            return;
        }
        if (txtWSCharCode.getText().isEmpty())
        {
            JOptionPane.showMessageDialog(this, "Enter WS Char Code !!!");
            return;
        }
        if (cmbCharType.getSelectedItem().equals("Value"))
        {
            if (tblCharValueLinkup.getRowCount() == 0)
            {
                JOptionPane.showMessageDialog(this, "Enter Char Value !!!");
                return;
            }
        }

        funSaveAndUpdate();


    }//GEN-LAST:event_btnNewActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        // TODO add your handling code here:
        funResetFields();
    }//GEN-LAST:event_btnResetActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        dispose();
         clsGlobalVarClass.hmActiveForms.remove("Characterstics Master");
    }//GEN-LAST:event_btnCancelActionPerformed

    private void txtCharCodeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtCharCodeMouseClicked
        // TODO add your handling code here:
        funSearchCharCode();
    }//GEN-LAST:event_txtCharCodeMouseClicked

    private void txtCharCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCharCodeKeyPressed
        // TODO add your handling code here:
        //Open help on key '?' or key '/'
        if (evt.getKeyChar() == '?' || evt.getKeyChar() == '/')
        {
            funSearchCharCode();
        }
        //Focus goes to select pos
        if (evt.getKeyCode() == 10)
        {
            txtCharName.requestFocus();
        }
    }//GEN-LAST:event_txtCharCodeKeyPressed

    private void cmbCharTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbCharTypeActionPerformed
        // TODO add your handling code here:
        if (cmbCharType.getSelectedItem().equals("Value"))
        {

            scrCharacteristics.setVisible(true);
            btnAdd.setVisible(true);
            btnRemove.setVisible(true);
            btnLinkupReset.setVisible(true);
        }
        else
        {
            scrCharacteristics.setVisible(false);
            btnAdd.setVisible(false);
            btnRemove.setVisible(false);
            btnLinkupReset.setVisible(false);
        }

    }//GEN-LAST:event_cmbCharTypeActionPerformed

    private void cmbCharTypeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbCharTypeKeyPressed
        // TODO add your handling code here:
          //Focus goes to select pos
        if (evt.getKeyCode() == 10)
        {
            txtWSCharCode.requestFocus();
        }
    }//GEN-LAST:event_cmbCharTypeKeyPressed

    private void txtWSCharCodeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtWSCharCodeMouseClicked
        // TODO add your handling code here:
        funProdCharCodeTextFieldClicked();
    }//GEN-LAST:event_txtWSCharCodeMouseClicked

    private void txtWSCharCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtWSCharCodeKeyPressed
        // TODO add your handling code here:
         //Open help on key '?' or key '/'
        if (evt.getKeyChar() == '?' || evt.getKeyChar() == '/')
        {
            funProdCharCodeTextFieldClicked();
        }
        //Focus goes to select pos
        if (evt.getKeyCode() == 10)
        {
            txtTextValue.requestFocus();
        }
        
    }//GEN-LAST:event_txtWSCharCodeKeyPressed

    private void btnAddMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAddMouseClicked

        try
        {
            if (txtWSCharCode.getText().isEmpty())
            {
                JOptionPane.showMessageDialog(this, "Please Enter WS Char Code");
                return;
            }
            funInsertCharValueInRow();
            txtTextValue.setText("");
            txtTextValue.requestFocus();
        }
        catch (Exception ex)
        {
            objUtility.funWriteErrorLog(ex);
            ex.printStackTrace();
        }

    }//GEN-LAST:event_btnAddMouseClicked

    private void btnAddMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAddMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAddMouseEntered

    private void btnAddKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnAddKeyPressed
        // TODO add your handling code here:
        //Focus goes to select pos
        if (evt.getKeyCode() == 10)
        {
            try
            {
                if (txtWSCharCode.getText().isEmpty())
                {
                    JOptionPane.showMessageDialog(this, "Please Enter WS Char Code");
                    return;
                }
                funInsertCharValueInRow();
                txtTextValue.setText("");
                txtTextValue.requestFocus();
            }
            catch (Exception ex)
            {
                objUtility.funWriteErrorLog(ex);
                ex.printStackTrace();
            }
        }
    }//GEN-LAST:event_btnAddKeyPressed

    private void btnLinkupResetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLinkupResetMouseClicked
        // TODO add your handling code here:
        funResetCharValueLinkupField();
    }//GEN-LAST:event_btnLinkupResetMouseClicked

    private void btnLinkupResetMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLinkupResetMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_btnLinkupResetMouseEntered

    private void btnLinkupResetKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnLinkupResetKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnLinkupResetKeyPressed

    private void btnRemoveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRemoveMouseClicked
        funRemoveRow();
    }//GEN-LAST:event_btnRemoveMouseClicked

    private void btnRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnRemoveActionPerformed

    private void tblCharValueLinkupMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCharValueLinkupMouseClicked

        funLinkupTableRowSelection();
    }//GEN-LAST:event_tblCharValueLinkupMouseClicked

    private void txtTextValueMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtTextValueMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTextValueMouseClicked

    private void txtTextValueKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTextValueKeyPressed
        // TODO add your handling code here:
        //Focus goes to select pos
        if (evt.getKeyCode() == 10)
        {
            btnAdd.requestFocus();
        }
    }//GEN-LAST:event_txtTextValueKeyPressed

    private void txtCharNameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtCharNameMouseClicked
        // TODO add your handling code here:
        
    }//GEN-LAST:event_txtCharNameMouseClicked

    private void txtCharNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCharNameKeyPressed
        // TODO add your handling code here:
         //Open help on key '?' or key '/'
        if (evt.getKeyChar() == '?' || evt.getKeyChar() == '/')
        {
            funSearchCharCode();
        }
        //Focus goes to select pos
        if (evt.getKeyCode() == 10)
        {
            cmbCharType.requestFocus();
        }
         
    }//GEN-LAST:event_txtCharNameKeyPressed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
        clsGlobalVarClass.hmActiveForms.remove("Characterstics Master");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        clsGlobalVarClass.hmActiveForms.remove("Characterstics Master");
    }//GEN-LAST:event_formWindowClosing


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnLinkupReset;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnRemove;
    private javax.swing.JButton btnReset;
    private javax.swing.JComboBox cmbCharType;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JLabel lblCharCode;
    private javax.swing.JLabel lblCharName;
    private javax.swing.JLabel lblCharType;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblFormName;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblWSCharCode;
    private javax.swing.JLabel lblformName;
    private javax.swing.JPanel panelBody;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelLayout;
    private javax.swing.JScrollPane scrCharacteristics;
    private javax.swing.JTable tblCharValueLinkup;
    private javax.swing.JTextField txtCharCode;
    private javax.swing.JTextField txtCharName;
    private javax.swing.JTextField txtTextValue;
    private javax.swing.JTextField txtWSCharCode;
    // End of variables declaration//GEN-END:variables

}
