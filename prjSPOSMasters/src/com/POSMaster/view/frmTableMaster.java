/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSMaster.view;

import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsUtility;
import com.POSGlobal.view.frmAlfaNumericKeyBoard;
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
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;

public class frmTableMaster extends javax.swing.JFrame
{

    private String strAreaCode, strWaiterNo, operational;
    public java.util.Vector vAreaCode, vWaiterNo, vTableNo, vTableName;
    private int selectedMenuHeadRowNo;
    private int seqNoToInsert;
    private Map<String, String> hmPOS;
    private StringBuilder sqlBuilder;
    clsUtility objUtility = new clsUtility();

    /**
     * This method is used to initialize frmTableMaster
     */
    public frmTableMaster()
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
            txtTableNo.requestFocus();
            lblUserCode.setText(clsGlobalVarClass.gUserCode);
            lblPosName.setText(clsGlobalVarClass.gPOSName);
            lblModuleName.setText(clsGlobalVarClass.gSelectedModule);

            //  txtTableName.requestFocus();
            vAreaCode = new java.util.Vector();
            vWaiterNo = new java.util.Vector();
            vTableNo = new java.util.Vector();
            vTableName = new java.util.Vector();
            cmbAreaName.addItem("Select");
            String sql = "select strAreaCode,strAreaName from tblareamaster";
            ResultSet rsAreaCode = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            while (rsAreaCode.next())
            {
                vAreaCode.add(rsAreaCode.getString(1));
                cmbAreaName.addItem(rsAreaCode.getString(2));
            }
            rsAreaCode.close();

            cmbWaiterName.addItem("Select");
            sql = "select strWaiterNo,strWShortName from tblwaitermaster";
            ResultSet rsWaiterNo = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            while (rsWaiterNo.next())
            {
                vWaiterNo.add(rsWaiterNo.getString(1));
                cmbWaiterName.addItem(rsWaiterNo.getString(2));
            }
            rsWaiterNo.close();

            sql = "select strTableNo,strTableName from tbltablemaster";
            ResultSet rsTable = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            while (rsTable.next())
            {
                vTableNo.add(rsTable.getString(1));
                vTableName.add(rsTable.getString(2));
            }
            rsTable.close();

            sqlBuilder = new StringBuilder();
            funFillPOSCombo();
            funLoadTable();
            funSetShortCutKeys();
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
     * This method is used to fill pos code combo
     *
     * @throws Exception
     */
    private void funFillPOSCombo() throws Exception
    {
        hmPOS = new HashMap<String, String>();
        cmbPosCode.removeAllItems();
        //hmPOS.put("All", "All");
        //cmbPosCode.addItem("All");
        String sqlPOS = "select strPOSCode,strPOSName from tblposmaster";
        ResultSet rsPOS = clsGlobalVarClass.dbMysql.executeResultSet(sqlPOS);
        while (rsPOS.next())
        {
            hmPOS.put(rsPOS.getString(2).trim(), rsPOS.getString(1));
            cmbPosCode.addItem(rsPOS.getString(2).trim());
        }
        rsPOS.close();
    }

    /**
     * This method is used to set data
     *
     * @param data
     */
    private void funSetTableData(Object[] data)
    {
        try
        {
            String sql = "select * from tbltablemaster where strTableNo='" + clsGlobalVarClass.gSearchedItem + "'";
            ResultSet rsTableData = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            if (rsTableData.next())
            {
                txtTableNo.setText(rsTableData.getString(1));
                txtTableName.setText(rsTableData.getString(2));
                for (int cntAreaCode = 0; cntAreaCode < vAreaCode.size(); cntAreaCode++)
                {
                    if (vAreaCode.elementAt(cntAreaCode).toString().equals(rsTableData.getString(4)))
                    {
                        cmbAreaName.setSelectedItem(cmbAreaName.getItemAt(cntAreaCode + 1));
                        break;
                    }
                }
                for (int cntWaiterCode = 0; cntWaiterCode < vWaiterNo.size(); cntWaiterCode++)
                {
                    if (vWaiterNo.elementAt(cntWaiterCode).toString().equals(rsTableData.getString(5)))
                    {
                        cmbWaiterName.setSelectedItem(cmbWaiterName.getItemAt(cntWaiterCode + 1));
                        break;
                    }
                }
                txtPaxCapacity.setText(rsTableData.getString(6));
                String op = rsTableData.getString(7);
                if (op.equalsIgnoreCase("Y"))
                {
                    cmbOperational.setSelectedIndex(0);
                }
                else
                {
                    cmbOperational.setSelectedIndex(1);
                }
                
                if(rsTableData.getString(16).equalsIgnoreCase("Y"))
                {
                    cmbNCTable.setSelectedItem("Yes");
                }
                else
                {
                    cmbNCTable.setSelectedItem("No");
                }
                
                
            }
            rsTableData.close();
            cmbPosCode.setSelectedItem(data[4].toString().trim());
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    /**
     * This method is used to generate table no
     *
     * @return
     */
    /*
     * private String funGenerateTableNo() { try { String sql = "select count(*)
     * from tbltablemaster"; ResultSet rs =
     * clsGlobalVarClass.dbMysql.executeResultSet(sql); rs.next(); int cn =
     * rs.getInt(1); rs.close(); if (cn > 0) { sql = "select
     * max(strTableNo),MAX(intSequence) from tbltablemaster"; rs =
     * clsGlobalVarClass.dbMysql.executeResultSet(sql); rs.next(); code =
     * rs.getString(1); sequenceNoToInsert = rs.getInt(2) + 1; StringBuilder sb
     * = new StringBuilder(code); String ss = sb.delete(0, 2).toString(); for
     * (int i = 0; i < ss.length(); i++) { if (ss.charAt(i) != '0') { strCode =
     * ss.substring(i, ss.length()); break; } } int intCode =
     * Integer.parseInt(strCode); intCode++;
     *
     * if (intCode < 100 && intCode > 9) { tbCode = "TB0" + intCode; } else if
     * (intCode < 10) { tbCode = "TB00" + intCode; } else { tbCode = "TB" +
     * intCode; } } else { tbCode = "TB001"; sequenceNoToInsert = 0; } } catch
     * (Exception e) { e.printStackTrace(); } return tbCode;
     }
     */
    private String funGenerateTableNo()
    {
        String tableCode = "";
        try
        {
            String sql = "select count(*) from tbltablemaster";
            ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            rs.next();
            int cn = rs.getInt(1);
            rs.close();
            if (cn > 0)
            {
                sql = "select max(strTableNo),MAX(intSequence) from tbltablemaster";
                rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                rs.next();
                String tableNo = rs.getString(1);
                seqNoToInsert = rs.getInt(2) + 1;
                StringBuilder sb = new StringBuilder(tableNo);
                String numTableNo = sb.delete(0, 2).toString();
                int intCode = Integer.parseInt(numTableNo);
                intCode++;

                if (intCode < 10)
                {
                    tableCode = "TB000000" + intCode;
                }
                else if (intCode < 100)
                {
                    tableCode = "TB00000" + intCode;
                }
                else if (intCode < 1000)
                {
                    tableCode = "TB0000" + intCode;
                }
                else if (intCode < 10000)
                {
                    tableCode = "TB000" + intCode;
                }
                else if (intCode < 100000)
                {
                    tableCode = "TB00" + intCode;
                }
                else if (intCode < 1000000)
                {
                    tableCode = "TB0" + intCode;
                }
            }
            else
            {
                tableCode = "TB0000001";
                seqNoToInsert = 0;
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
        return tableCode;
    }

    /**
     * This method is used to save table
     */
    private void funSaveTable()
    {
        try
        {
            String posCode = hmPOS.get(cmbPosCode.getSelectedItem().toString().trim());
            String sql = "";
            if (!clsGlobalVarClass.validateEmpty(txtTableName.getText()))
            {
                new frmOkPopUp(this, "Please Enter Table Name", "Error", 0).setVisible(true);
            }
            else if (txtTableName.getText().length() > 20)
            {
                new frmOkPopUp(this, "Table Name should be less than 20 characters", "Error", 0).setVisible(true);
            }
            else
            {
                String tableName = txtTableName.getText().trim();
                String code = "";
                if (clsGlobalVarClass.funCheckItemName("tbltablemaster", "strTableName", "strTableCode", tableName, code, "save", posCode))
                {
                    new frmOkPopUp(this, "This  Table Name is Already Exist", "Error", 0).setVisible(true);
                    txtTableName.requestFocus();
                }
                else
                {
                    if (!cmbAreaName.getSelectedItem().toString().equals("Select"))
                    {
                        strAreaCode = vAreaCode.elementAt(cmbAreaName.getSelectedIndex() - 1).toString();
                    }
                    else
                    {
                        strAreaCode = "";
                    }
                    if (!cmbWaiterName.getSelectedItem().toString().equals("Select"))
                    {
                        strWaiterNo = vWaiterNo.elementAt(cmbWaiterName.getSelectedIndex() - 1).toString();
                    }
                    else
                    {
                        strWaiterNo = "all";
                    }
                    if (cmbOperational.getSelectedItem().toString().equals("Yes"))
                    {
                        operational = "Y";
                    }
                    else
                    {
                        operational = "N";
                    }
                    String ncTable = "Y";
                    if (cmbNCTable.getSelectedItem().toString().equalsIgnoreCase("Yes"))
                    {
                        ncTable = "Y";
                    }
                    else
                    {
                        ncTable = "N";
                    }

                    txtTableNo.setText(funGenerateTableNo());
                    sql = "insert into tbltablemaster "
                            + "(strTableNo,strTableName,strStatus,strAreaCode,strWaiterNo,intPaxNo,strOperational,strUserCreated"
                            + ",strUserEdited,dteDateCreated,dteDateEdited,strClientCode,intSequence,strPOSCode,strNCTable) "
                            + "values('" + txtTableNo.getText() + "','" + txtTableName.getText()
                            + "','Normal','" + strAreaCode + "','" + strWaiterNo + "','" + txtPaxCapacity.getText() + "','" + operational + "','"
                            + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.getCurrentDateTime()
                            + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.gClientCode + "'"
                            + "," + seqNoToInsert + ",'" + posCode + "','" + ncTable + "')";
                    //System.out.println(sql);
                    int exc = clsGlobalVarClass.dbMysql.execute(sql);
                    if (exc > 0)
                    {
                        sql = "update tblmasteroperationstatus set dteDateEdited='" + clsGlobalVarClass.getCurrentDateTime() + "' "
                                + " where strTableName='Table' ";
                        clsGlobalVarClass.dbMysql.execute(sql);
                        new frmOkPopUp(this, "Entry added Successfully", "Successfull", 3).setVisible(true);
                        funResetField();
                        funLoadTable();
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

    /**
     * This method is used to update table
     */
    private void funUpdateTable()
    {
        try
        {
            String posCode = hmPOS.get(cmbPosCode.getSelectedItem().toString().trim());
            String tableName = txtTableName.getText().trim();
            String tableNo = txtTableNo.getText().trim();
            if (clsGlobalVarClass.funCheckItemName("tbltablemaster", "strTableName", "strTableNo", tableName, tableNo, "update", posCode))
            {
                new frmOkPopUp(this, "This Table Name is Already Exist", "Error", 0).setVisible(true);
                txtTableName.requestFocus();
            }
            else if (!clsGlobalVarClass.validateEmpty(txtTableName.getText()))
            {
                new frmOkPopUp(this, "Please Enter Table Name", "Error", 0).setVisible(true);
            }
            else if (txtTableName.getText().length() > 20)
            {
                new frmOkPopUp(this, "Table Name should be less than 20 characters", "Error", 0).setVisible(true);
            }
            else if (IsTableInOperation(tableNo))
            {
                new frmOkPopUp(this, "Table Is In Operation!!!", "Error", 0).setVisible(true);
                return;
            }
            else
            {
                if (!cmbAreaName.getSelectedItem().toString().equals("Select"))
                {
                    strAreaCode = vAreaCode.elementAt(cmbAreaName.getSelectedIndex() - 1).toString();
                }
                else
                {
                    strAreaCode = "";
                }
                if (!cmbWaiterName.getSelectedItem().toString().equals("Select"))
                {
                    strWaiterNo = vWaiterNo.elementAt(cmbWaiterName.getSelectedIndex() - 1).toString();
                }
                else
                {
                    strWaiterNo = "all";
                }
                if (cmbOperational.getSelectedItem().toString().equals("Yes"))
                {
                    operational = "Y";
                }
                else
                {
                    operational = "N";
                }
                String ncTable = "Y";
                if (cmbNCTable.getSelectedItem().toString().equalsIgnoreCase("Yes"))
                {
                    ncTable = "Y";
                }
                else
                {
                    ncTable = "N";
                }

                String sql = "UPDATE tbltablemaster SET strTableName = '" + txtTableName.getText() + "',strAreaCode='" + strAreaCode
                        + "',strWaiterNo='" + strWaiterNo + "',strOperational='" + operational + "',strUserEdited='" + clsGlobalVarClass.gUserCode
                        + "',dteDateEdited='" + clsGlobalVarClass.getCurrentDateTime() + "',strDataPostFlag='N',strPOSCode='" + posCode + "' "
                        + ",intPaxNo='" + txtPaxCapacity.getText() + "' "
                        + ",strNCTable='"+ncTable+"' "
                        + " WHERE strTableNo ='" + txtTableNo.getText() + "'";
                //System.out.println(sql);
                int exc = clsGlobalVarClass.dbMysql.execute(sql);
                if (exc > 0)
                {
                    sql = "update tblmasteroperationstatus set dteDateEdited='" + clsGlobalVarClass.getCurrentDateTime() + "' "
                            + " where strTableName='Table' ";
                    clsGlobalVarClass.dbMysql.execute(sql);
                    new frmOkPopUp(this, "Updated Successfully", "Successfull", 3).setVisible(true);
                    funResetField();
                    funLoadTable();
                }
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    private void funOpenTableSearch()
    {
        clsUtility obj = new clsUtility();
        obj.funCallForSearchForm("TableMaster");
        new frmSearchFormDialog(this, true).setVisible(true);
        if (clsGlobalVarClass.gSearchItemClicked)
        {
            btnNew.setText("UPDATE");
            btnNew.setMnemonic('u');
            Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
            funSetTableData(data);
            clsGlobalVarClass.gSearchItemClicked = false;
        }
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
        filler6 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        lblUserCode = new javax.swing.JLabel();
        lblDate = new javax.swing.JLabel();
        lblHOSign = new javax.swing.JLabel();
        panelLayout = new JPanel() {       public void paintComponent(Graphics g) {         Image img = Toolkit.getDefaultToolkit().getImage(         getClass().getResource("/com/POSMaster/images/imgBGJPOS.png"));         g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);         }       };  ;
        panelBody = new javax.swing.JPanel();
        tabPane = new javax.swing.JTabbedPane();
        panelTabTableMaster = new javax.swing.JPanel();
        lblFormNameTblMast = new javax.swing.JLabel();
        lblGroupCode2 = new javax.swing.JLabel();
        txtTableNo = new javax.swing.JTextField();
        txtTableName = new javax.swing.JTextField();
        lblGroupName2 = new javax.swing.JLabel();
        lblAreaName = new javax.swing.JLabel();
        cmbAreaName = new javax.swing.JComboBox();
        cmbWaiterName = new javax.swing.JComboBox();
        lblWaiterName = new javax.swing.JLabel();
        lblWaiterName1 = new javax.swing.JLabel();
        cmbOperational = new javax.swing.JComboBox();
        btnNew = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        lblPoscode = new javax.swing.JLabel();
        cmbPosCode = new javax.swing.JComboBox();
        lblPaxCapacity = new javax.swing.JLabel();
        txtPaxCapacity = new javax.swing.JTextField();
        lblWaiterName2 = new javax.swing.JLabel();
        cmbNCTable = new javax.swing.JComboBox();
        panelTabTableSeq = new javax.swing.JPanel();
        scrollPane = new javax.swing.JScrollPane();
        tblTableSequence = new javax.swing.JTable();
        lblFormNameTblSeq = new javax.swing.JLabel();
        lblMoveUp = new javax.swing.JLabel();
        lblMoveDown = new javax.swing.JLabel();
        btnSeqSave = new javax.swing.JButton();
        btnSeqClose = new javax.swing.JButton();

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
        lblformName.setText(" - Table Master");
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
        panelLayout.setOpaque(false);
        panelLayout.setLayout(new java.awt.GridBagLayout());

        panelBody.setBackground(new java.awt.Color(255, 255, 255));
        panelBody.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        panelBody.setMinimumSize(new java.awt.Dimension(800, 570));
        panelBody.setOpaque(false);

        panelTabTableMaster.setBackground(new java.awt.Color(216, 238, 254));
        panelTabTableMaster.setLayout(null);

        lblFormNameTblMast.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        lblFormNameTblMast.setForeground(new java.awt.Color(14, 7, 7));
        lblFormNameTblMast.setText("Table Master");
        panelTabTableMaster.add(lblFormNameTblMast);
        lblFormNameTblMast.setBounds(300, 20, 180, 30);

        lblGroupCode2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblGroupCode2.setText("Table No        :");
        panelTabTableMaster.add(lblGroupCode2);
        lblGroupCode2.setBounds(190, 90, 90, 30);

        txtTableNo.setEditable(false);
        txtTableNo.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtTableNoMouseClicked(evt);
            }
        });
        txtTableNo.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtTableNoKeyPressed(evt);
            }
        });
        panelTabTableMaster.add(txtTableNo);
        txtTableNo.setBounds(280, 90, 80, 30);

        txtTableName.addFocusListener(new java.awt.event.FocusAdapter()
        {
            public void focusLost(java.awt.event.FocusEvent evt)
            {
                txtTableNameFocusLost(evt);
            }
        });
        txtTableName.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtTableNameMouseClicked(evt);
            }
        });
        txtTableName.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtTableNameKeyPressed(evt);
            }
        });
        panelTabTableMaster.add(txtTableName);
        txtTableName.setBounds(460, 90, 150, 30);

        lblGroupName2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblGroupName2.setText("Table Name    :");
        panelTabTableMaster.add(lblGroupName2);
        lblGroupName2.setBounds(370, 90, 90, 30);

        lblAreaName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblAreaName.setText("Area Name     :");
        panelTabTableMaster.add(lblAreaName);
        lblAreaName.setBounds(190, 260, 90, 30);

        cmbAreaName.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbAreaNameKeyPressed(evt);
            }
        });
        panelTabTableMaster.add(cmbAreaName);
        cmbAreaName.setBounds(280, 260, 270, 30);

        cmbWaiterName.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbWaiterNameActionPerformed(evt);
            }
        });
        cmbWaiterName.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbWaiterNameKeyPressed(evt);
            }
        });
        panelTabTableMaster.add(cmbWaiterName);
        cmbWaiterName.setBounds(280, 310, 270, 30);

        lblWaiterName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblWaiterName.setText("Waiter Name  :");
        panelTabTableMaster.add(lblWaiterName);
        lblWaiterName.setBounds(190, 310, 90, 30);

        lblWaiterName1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblWaiterName1.setText("Operational    :");
        panelTabTableMaster.add(lblWaiterName1);
        lblWaiterName1.setBounds(190, 360, 90, 30);

        cmbOperational.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Yes", "No" }));
        cmbOperational.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbOperationalKeyPressed(evt);
            }
        });
        panelTabTableMaster.add(cmbOperational);
        cmbOperational.setBounds(280, 360, 150, 30);

        btnNew.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnNew.setForeground(new java.awt.Color(255, 255, 255));
        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnNew.setText("SAVE");
        btnNew.setToolTipText("Save Table");
        btnNew.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNew.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnNew.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnNewMouseClicked(evt);
            }
        });
        btnNew.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnNewActionPerformed(evt);
            }
        });
        btnNew.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                btnNewKeyPressed(evt);
            }
        });
        panelTabTableMaster.add(btnNew);
        btnNew.setBounds(450, 470, 90, 40);

        btnReset.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnReset.setForeground(new java.awt.Color(255, 255, 255));
        btnReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnReset.setText("RESET");
        btnReset.setToolTipText("Reset All Fields");
        btnReset.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnReset.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
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
        btnReset.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                btnResetKeyPressed(evt);
            }
        });
        panelTabTableMaster.add(btnReset);
        btnReset.setBounds(570, 470, 90, 40);

        btnCancel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnCancel.setForeground(new java.awt.Color(255, 255, 255));
        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnCancel.setText("CLOSE");
        btnCancel.setToolTipText("Close Table Master");
        btnCancel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCancel.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnCancel.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCancelMouseClicked(evt);
            }
        });
        btnCancel.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCancelActionPerformed(evt);
            }
        });
        panelTabTableMaster.add(btnCancel);
        btnCancel.setBounds(690, 470, 90, 40);

        lblPoscode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblPoscode.setText("POS Name     :");
        panelTabTableMaster.add(lblPoscode);
        lblPoscode.setBounds(190, 210, 90, 30);

        cmbPosCode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmbPosCode.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbPosCodeKeyPressed(evt);
            }
        });
        panelTabTableMaster.add(cmbPosCode);
        cmbPosCode.setBounds(280, 210, 270, 30);

        lblPaxCapacity.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblPaxCapacity.setText("PAX Capacity   : ");
        panelTabTableMaster.add(lblPaxCapacity);
        lblPaxCapacity.setBounds(190, 150, 100, 30);

        txtPaxCapacity.setText("0");
        txtPaxCapacity.addFocusListener(new java.awt.event.FocusAdapter()
        {
            public void focusLost(java.awt.event.FocusEvent evt)
            {
                txtPaxCapacityFocusLost(evt);
            }
        });
        txtPaxCapacity.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtPaxCapacityMouseClicked(evt);
            }
        });
        txtPaxCapacity.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtPaxCapacityKeyPressed(evt);
            }
        });
        panelTabTableMaster.add(txtPaxCapacity);
        txtPaxCapacity.setBounds(280, 150, 50, 30);

        lblWaiterName2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblWaiterName2.setText("NC Table      :");
        panelTabTableMaster.add(lblWaiterName2);
        lblWaiterName2.setBounds(200, 410, 80, 30);

        cmbNCTable.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "No", "Yes" }));
        cmbNCTable.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbNCTableKeyPressed(evt);
            }
        });
        panelTabTableMaster.add(cmbNCTable);
        cmbNCTable.setBounds(280, 410, 150, 30);

        tabPane.addTab("Table Master", panelTabTableMaster);

        panelTabTableSeq.setBackground(new java.awt.Color(216, 238, 254));
        panelTabTableSeq.setLayout(null);

        tblTableSequence.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Sequence No.", "TableCode", "Table Name", "Area", "POS"
            }
        )
        {
            boolean[] canEdit = new boolean []
            {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return canEdit [columnIndex];
            }
        });
        tblTableSequence.setRowHeight(25);
        tblTableSequence.getTableHeader().setReorderingAllowed(false);
        tblTableSequence.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tblTableSequenceMouseClicked(evt);
            }
        });
        scrollPane.setViewportView(tblTableSequence);

        panelTabTableSeq.add(scrollPane);
        scrollPane.setBounds(60, 60, 620, 330);

        lblFormNameTblSeq.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        lblFormNameTblSeq.setText("Table Sequence");
        panelTabTableSeq.add(lblFormNameTblSeq);
        lblFormNameTblSeq.setBounds(300, 20, 200, 30);

        lblMoveUp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgMoveUp.png"))); // NOI18N
        lblMoveUp.setToolTipText("Move Up");
        lblMoveUp.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                lblMoveUpMouseClicked(evt);
            }
        });
        panelTabTableSeq.add(lblMoveUp);
        lblMoveUp.setBounds(320, 400, 60, 60);

        lblMoveDown.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgMoveDown.png"))); // NOI18N
        lblMoveDown.setToolTipText("Move Down");
        lblMoveDown.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                lblMoveDownMouseClicked(evt);
            }
        });
        panelTabTableSeq.add(lblMoveDown);
        lblMoveDown.setBounds(390, 400, 60, 60);

        btnSeqSave.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnSeqSave.setForeground(new java.awt.Color(255, 255, 255));
        btnSeqSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnSeqSave.setText("SAVE");
        btnSeqSave.setToolTipText("Save Table Sequence");
        btnSeqSave.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSeqSave.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnSeqSave.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnSeqSaveMouseClicked(evt);
            }
        });
        panelTabTableSeq.add(btnSeqSave);
        btnSeqSave.setBounds(560, 470, 100, 40);

        btnSeqClose.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnSeqClose.setForeground(new java.awt.Color(255, 255, 255));
        btnSeqClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnSeqClose.setText("CLOSE");
        btnSeqClose.setToolTipText("Close Table Sequence");
        btnSeqClose.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSeqClose.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnSeqClose.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnSeqCloseMouseClicked(evt);
            }
        });
        panelTabTableSeq.add(btnSeqClose);
        btnSeqClose.setBounds(680, 470, 100, 40);

        tabPane.addTab("Table Sequence", panelTabTableSeq);

        javax.swing.GroupLayout panelBodyLayout = new javax.swing.GroupLayout(panelBody);
        panelBody.setLayout(panelBodyLayout);
        panelBodyLayout.setHorizontalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBodyLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(tabPane, javax.swing.GroupLayout.PREFERRED_SIZE, 800, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        panelBodyLayout.setVerticalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBodyLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(tabPane, javax.swing.GroupLayout.PREFERRED_SIZE, 550, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        panelLayout.add(panelBody, new java.awt.GridBagConstraints());

        getContentPane().add(panelLayout, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtTableNoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtTableNoMouseClicked
        // TODO add your handling code here:
        funOpenTableSearch();
    }//GEN-LAST:event_txtTableNoMouseClicked

    private void txtTableNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTableNameFocusLost
        // TODO add your handling code here:
        funCheckDuplicateTableName();
    }//GEN-LAST:event_txtTableNameFocusLost

    private void txtTableNameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtTableNameMouseClicked
        // TODO add your handling code here:
        try
        {
            if (txtTableName.getText().length() == 0)
            {
                new frmAlfaNumericKeyBoard(this, true, "1", "Enter Table Name").setVisible(true);
                txtTableName.setText(clsGlobalVarClass.gKeyboardValue);
            }
            else
            {
                new frmAlfaNumericKeyBoard(this, true, txtTableName.getText(), "1", "Enter Table Name").setVisible(true);
                txtTableName.setText(clsGlobalVarClass.gKeyboardValue);
            }

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_txtTableNameMouseClicked

    private void txtTableNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTableNameKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            if (txtTableName.getText().length() > 8)
            {
                JOptionPane.showMessageDialog(this, "Table Name should not be more than 8 characters");
                cmbAreaName.requestFocus();
            }
            else if (funCheckDuplicateTableName())
            {
                funSaveTable();
                cmbAreaName.requestFocus();
            }
        }
    }//GEN-LAST:event_txtTableNameKeyPressed

    private void cmbAreaNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbAreaNameKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            cmbWaiterName.requestFocus();
        }
    }//GEN-LAST:event_cmbAreaNameKeyPressed

    private void cmbWaiterNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbWaiterNameKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            cmbOperational.requestFocus();
        }
    }//GEN-LAST:event_cmbWaiterNameKeyPressed

    private void cmbOperationalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbOperationalKeyPressed
        // TODO add your handling code here:

        if (evt.getKeyCode() == 10)
        {
            cmbPosCode.requestFocus();
        }
    }//GEN-LAST:event_cmbOperationalKeyPressed

    private void btnNewMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNewMouseClicked
        // TODO add your handling code here:
        if (btnNew.getText().equalsIgnoreCase("SAVE"))
        {
            funSaveTable();
        }
        else
        {
            funUpdateTable();
        }
    }//GEN-LAST:event_btnNewMouseClicked

    private void btnResetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnResetMouseClicked
        // TODO add your handling code here:
        funResetField();
    }//GEN-LAST:event_btnResetMouseClicked

    private void btnCancelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseClicked
        // TODO add your handling code here:
        try
        {
            dispose();
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
        clsGlobalVarClass.hmActiveForms.remove("Table Master");
    }//GEN-LAST:event_btnCancelMouseClicked

    private void tblTableSequenceMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblTableSequenceMouseClicked
        // TODO add your handling code here:
        selectedMenuHeadRowNo = 0;
        selectedMenuHeadRowNo = tblTableSequence.getSelectedRow();
    }//GEN-LAST:event_tblTableSequenceMouseClicked

    private void lblMoveUpMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblMoveUpMouseClicked
        if (selectedMenuHeadRowNo == 0)
        {
            //do nothing this is first row
        }
        else
        {

            String temSelectedMenuHeadCode = tblTableSequence.getValueAt(selectedMenuHeadRowNo, 1).toString();
            String tempSelectedMenuHeadName = tblTableSequence.getValueAt(selectedMenuHeadRowNo, 2).toString();
            String tempUpperMenuHeadCode = tblTableSequence.getValueAt(selectedMenuHeadRowNo - 1, 1).toString();
            String tempUpperMenuHeadName = tblTableSequence.getValueAt(selectedMenuHeadRowNo - 1, 2).toString();
            tblTableSequence.setValueAt(tempUpperMenuHeadCode, selectedMenuHeadRowNo, 1);
            tblTableSequence.setValueAt(tempUpperMenuHeadName, selectedMenuHeadRowNo, 2);
            tblTableSequence.setValueAt(temSelectedMenuHeadCode, selectedMenuHeadRowNo - 1, 1);
            tblTableSequence.setValueAt(tempSelectedMenuHeadName, selectedMenuHeadRowNo - 1, 2);
            selectedMenuHeadRowNo = selectedMenuHeadRowNo - 1;
            tblTableSequence.setRowSelectionInterval(selectedMenuHeadRowNo, selectedMenuHeadRowNo);
        }        // TODO add your handling code here:
    }//GEN-LAST:event_lblMoveUpMouseClicked

    private void lblMoveDownMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblMoveDownMouseClicked
        // TODO add your handling code here:
        if (tblTableSequence.getRowCount() == selectedMenuHeadRowNo + 1)
        {
            //do nothing this is last row
        }
        else
        {
            String temSelectedMenuHeadCode = tblTableSequence.getValueAt(selectedMenuHeadRowNo, 1).toString();
            String tempSelectedMenuHeadName = tblTableSequence.getValueAt(selectedMenuHeadRowNo, 2).toString();
            String tempLowerMenuHeadCode = tblTableSequence.getValueAt(selectedMenuHeadRowNo + 1, 1).toString();
            String tempLowerMenuHeadName = tblTableSequence.getValueAt(selectedMenuHeadRowNo + 1, 2).toString();
            tblTableSequence.setValueAt(tempLowerMenuHeadCode, selectedMenuHeadRowNo, 1);
            tblTableSequence.setValueAt(tempLowerMenuHeadName, selectedMenuHeadRowNo, 2);
            tblTableSequence.setValueAt(temSelectedMenuHeadCode, selectedMenuHeadRowNo + 1, 1);
            tblTableSequence.setValueAt(tempSelectedMenuHeadName, selectedMenuHeadRowNo + 1, 2);
            selectedMenuHeadRowNo = selectedMenuHeadRowNo + 1;
            tblTableSequence.setRowSelectionInterval(selectedMenuHeadRowNo, selectedMenuHeadRowNo);
        }
    }//GEN-LAST:event_lblMoveDownMouseClicked

    private void btnSeqSaveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSeqSaveMouseClicked
        funSaveTableSequence();
    }//GEN-LAST:event_btnSeqSaveMouseClicked

    private void btnSeqCloseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSeqCloseMouseClicked
        dispose();
    }//GEN-LAST:event_btnSeqCloseMouseClicked

    private void btnNewKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnNewKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            if (btnNew.getText().equalsIgnoreCase("SAVE"))
            {
                funSaveTable();
            }
            else
            {
                funUpdateTable();
            }
        }
    }//GEN-LAST:event_btnNewKeyPressed

    private void txtTableNoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTableNoKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() == '?' || evt.getKeyChar() == '/')
        {
            funOpenTableSearch();
        }
        if (evt.getKeyCode() == 10)
        {
            txtTableName.requestFocus();
        }
    }//GEN-LAST:event_txtTableNoKeyPressed

    private void cmbPosCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbPosCodeKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            btnNew.requestFocus();
        }
    }//GEN-LAST:event_cmbPosCodeKeyPressed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        if (btnNew.getText().equalsIgnoreCase("SAVE"))
        {
            funSaveTable();
        }
        else
        {
            funUpdateTable();
        }
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        // TODO add your handling code here:
        funResetField();
    }//GEN-LAST:event_btnResetActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        funClearObjects();
        dispose();
        clsGlobalVarClass.hmActiveForms.remove("Table Master");
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnResetKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnResetKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnResetKeyPressed

    private void txtPaxCapacityFocusLost(java.awt.event.FocusEvent evt)//GEN-FIRST:event_txtPaxCapacityFocusLost
    {//GEN-HEADEREND:event_txtPaxCapacityFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPaxCapacityFocusLost

    private void txtPaxCapacityMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtPaxCapacityMouseClicked
    {//GEN-HEADEREND:event_txtPaxCapacityMouseClicked
        if (txtPaxCapacity.getText().length() == 0)
        {
            new frmNumericKeyboard(this, true, "", "Long", "Enter PAX Capacity. ").setVisible(true);
            txtPaxCapacity.setText(clsGlobalVarClass.gNumerickeyboardValue);
        }
        else
        {
            new frmNumericKeyboard(this, true, txtPaxCapacity.getText(), "Long", "Enter PAX Capacity.").setVisible(true);
            txtPaxCapacity.setText(clsGlobalVarClass.gNumerickeyboardValue);
        }
    }//GEN-LAST:event_txtPaxCapacityMouseClicked

    private void txtPaxCapacityKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtPaxCapacityKeyPressed
    {//GEN-HEADEREND:event_txtPaxCapacityKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPaxCapacityKeyPressed

    private void cmbWaiterNameActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbWaiterNameActionPerformed
    {//GEN-HEADEREND:event_cmbWaiterNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbWaiterNameActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
        clsGlobalVarClass.hmActiveForms.remove("Table Master");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        clsGlobalVarClass.hmActiveForms.remove("Table Master");
    }//GEN-LAST:event_formWindowClosing

    private void cmbNCTableKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_cmbNCTableKeyPressed
    {//GEN-HEADEREND:event_cmbNCTableKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbNCTableKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnSeqClose;
    private javax.swing.JButton btnSeqSave;
    private javax.swing.JComboBox cmbAreaName;
    private javax.swing.JComboBox cmbNCTable;
    private javax.swing.JComboBox cmbOperational;
    private javax.swing.JComboBox cmbPosCode;
    private javax.swing.JComboBox cmbWaiterName;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JLabel lblAreaName;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblFormNameTblMast;
    private javax.swing.JLabel lblFormNameTblSeq;
    private javax.swing.JLabel lblGroupCode2;
    private javax.swing.JLabel lblGroupName2;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblMoveDown;
    private javax.swing.JLabel lblMoveUp;
    private javax.swing.JLabel lblPaxCapacity;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblPoscode;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblWaiterName;
    private javax.swing.JLabel lblWaiterName1;
    private javax.swing.JLabel lblWaiterName2;
    private javax.swing.JLabel lblformName;
    private javax.swing.JPanel panelBody;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelLayout;
    private javax.swing.JPanel panelTabTableMaster;
    private javax.swing.JPanel panelTabTableSeq;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JTabbedPane tabPane;
    private javax.swing.JTable tblTableSequence;
    private javax.swing.JTextField txtPaxCapacity;
    private javax.swing.JTextField txtTableName;
    private javax.swing.JTextField txtTableNo;
    // End of variables declaration//GEN-END:variables
    /**
     * This method is used to check duplicate table name
     *
     * @return
     */
    private boolean funCheckDuplicateTableName()
    {
        boolean flgDupTable = true;
        try
        {
            for (int tableCount = 0; tableCount < vTableNo.size(); tableCount++)
            {
                if (vTableName.elementAt(tableCount).toString().equals(txtTableName.getText().trim()))
                {
                    JOptionPane.showMessageDialog(this, "Table Name is already present");
                    break;
                }
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
        return flgDupTable;
    }

    /**
     * This method is used to reset fields
     */
    private void funResetField()
    {
        try
        {
            btnNew.setText("SAVE");
            btnNew.setMnemonic('s');
            txtTableNo.setText("");
            txtTableName.setText("");
            txtPaxCapacity.setText("0");
            txtTableName.requestFocus();
            cmbAreaName.setSelectedItem(cmbAreaName.getItemAt(0));
            cmbWaiterName.setSelectedItem(cmbWaiterName.getItemAt(0));
            cmbOperational.setSelectedItem("Yes");
            cmbNCTable.setSelectedIndex(0);//No
            txtTableNo.requestFocus();
            funFillPOSCombo();
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    private void funClearObjects()
    {
        hmPOS = null;
        vAreaCode = null;
        vWaiterNo = null;
        vTableNo = null;
        vTableName = null;
    }

    /**
     * This method is used to load table
     */
    private void funLoadTable()
    {

        try
        {
            int SequenceNo = 1;
            DefaultTableModel dm1 = (DefaultTableModel) tblTableSequence.getModel();
            dm1.setRowCount(0);
            String sql = "select a.strTableNo,a.strTableName,b.strAreaName,ifnull(c.strPosName,'All') "
                    + " from tbltablemaster a left outer join tblareamaster b on a.strAreaCode=b.strAreaCode "
                    + " left outer join tblposmaster c on a.strPOSCode=c.strPosCode "
                    + "ORDER by a.intSequence";
            ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            while (rs.next())
            {
                Object[] ob =
                {
                    SequenceNo, rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4)
                };
                dm1.addRow(ob);
                SequenceNo++;
            }
            rs.close();
            tblTableSequence.setModel(dm1);

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    /**
     * This method is used to save table sequence
     */
    private void funSaveTableSequence()
    {
        try
        {
            int exec = 0;
            for (int i = 0; i < tblTableSequence.getRowCount(); i++)
            {
                String sql = "update tbltablemaster set intSequence=" + i + " where strTableNo='" + tblTableSequence.getValueAt(i, 1) + "'";
                exec = clsGlobalVarClass.dbMysql.execute(sql);
            }
            if (exec > 0)
            {
                new frmOkPopUp(this, "Sequence Updated Successfully", "Successfull", 3).setVisible(true);
                funLoadTable();
            }

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    private boolean IsTableInOperation(String tableNo)
    {
        boolean isTableInOperation = false;
        try
        {
            sqlBuilder.setLength(0);
            sqlBuilder.append("select a.strTableNo  "
                    + "from tblitemrtemp a "
                    + "where a.strTableNo='" + tableNo + "' and a.strNCKotYN='N' ");
            ResultSet rsTable = clsGlobalVarClass.dbMysql.executeResultSet(sqlBuilder.toString());
            if (rsTable.next())
            {
                isTableInOperation = true;
            }
            rsTable.close();
            if (!isTableInOperation)
            {
                sqlBuilder.setLength(0);
                sqlBuilder.append("select a.strTableNo  "
                        + "from tblbillhd a "
                        + "where a.strTableNo='" + tableNo + "'  and strBillNo not in (select strBillNo from tblbillsettlementdtl) ");
                rsTable = clsGlobalVarClass.dbMysql.executeResultSet(sqlBuilder.toString());
                if (rsTable.next())
                {
                    isTableInOperation = true;
                }
                rsTable.close();
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
        finally
        {
            return isTableInOperation;
        }
    }

}
