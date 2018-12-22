
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
import java.sql.PreparedStatement;

import java.sql.ResultSet;
import java.util.Date;
import java.util.HashMap;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;

public class frmPaymentInterfaceMaster extends javax.swing.JFrame
{

    private String selectQuery;
    private String strCode, code, sql;
    boolean flag;
    private clsUtility objUtility;
    private HashMap<String, String> mapPOSCode;
    private HashMap<String, String> mapPOSName;
    

    /**
     * This method is used to initialize CostCenterMaster
     */
    public frmPaymentInterfaceMaster()
    {
        initComponents();
        try
        {
            objUtility = new clsUtility();
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
            lblDate.setText(clsGlobalVarClass.gPOSDateToDisplay);
            lblModuleName.setText(clsGlobalVarClass.gSelectedModule);
            txtPGCode.requestFocus();
            
            funLoadPOSCombo();

            funSetShortCutKeys();
           

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    private void funSetShortCutKeys()
    {
        btnCancel.setMnemonic('c');
        btnNew.setMnemonic('s');
        btnReset.setMnemonic('r');

    }

    private void funLoadPOSCombo()
    {
        try
        {
            ResultSet rsPOS = clsGlobalVarClass.dbMysql.executeResultSet("select strPOSCode,strPOSName from tblposmaster ");
            mapPOSCode = new HashMap<String, String>();
            mapPOSName = new HashMap<String, String>();
            cmbPosCode.addItem("All");
            mapPOSCode.put("All", "All");
            mapPOSName.put("All", "All");
            while (rsPOS.next())
            {
                cmbPosCode.addItem(rsPOS.getString(2));
                mapPOSCode.put(rsPOS.getString(1), rsPOS.getString(2));
                mapPOSName.put(rsPOS.getString(2), rsPOS.getString(1));
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * This method is used to set data
     *
     * @param data
     */
    private void funSetDiscountData(Object[] data) throws Exception
    {
        sql = "select * from tblonlinepaymentconfighd a "
                + " where  a.strPGCode='" + data[0].toString() + "'";
        ResultSet rsDisc = clsGlobalVarClass.dbMysql.executeResultSet(sql);
        if (rsDisc.next())
        {
            txtPGCode.setText(rsDisc.getString(1));//code
            txtPGName.setText(rsDisc.getString(2));

            btnNew.setText("UPDATE");//updated
            btnNew.setMnemonic('u');

            cmbPosCode.setSelectedItem(mapPOSCode.get(rsDisc.getString(5)));
            rsDisc.close();
            txtPGCode.requestFocus();
        }
        rsDisc.close();

        DefaultTableModel dtm = (DefaultTableModel) tblPGDtl.getModel();
        dtm.setRowCount(0);

        sql = "select * from tblonlinepaymentconfigdtl a "
                + " where  a.strPGCode='" + data[0].toString() + "'";
        rsDisc = clsGlobalVarClass.dbMysql.executeResultSet(sql);
        while (rsDisc.next())
        {
            Object row[] =
            {
                rsDisc.getString(2), rsDisc.getString(3)
            };

            dtm.addRow(row);
        }
        rsDisc.close();
    }

    /**
     * This method is used to save cost center
     */
    private void funSelectDiscountCode()
    {
        try
        {
            clsUtility obj = new clsUtility();
            obj.funCallForSearchForm("Payment Interface Master");
            new frmSearchFormDialog(this, true).setVisible(true);
            if (clsGlobalVarClass.gSearchItemClicked)
            {
                btnNew.setText("UPDATE");
                btnNew.setMnemonic('u');
                Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
                funSetDiscountData(data);
                clsGlobalVarClass.gSearchItemClicked = false;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

  
   public void funSaveDiscount()
    {
        try
        {
            String posCode = mapPOSName.get(cmbPosCode.getSelectedItem().toString());


            String paymentCode = funGetPaymentCode();
            String name = txtPGName.getText().trim();
           
           
            String insertSql = "INSERT INTO tblonlinepaymentconfighd (strPGCode, strPGName, strClientCode, strDataPostFlag,strPosCode) "
                    + " VALUES ('" + paymentCode + "','" + name + "', '" + clsGlobalVarClass.gClientCode + "','N','"+posCode+"')";
            clsGlobalVarClass.dbMysql.execute(insertSql);

            StringBuffer sqlDtl = new StringBuffer("INSERT INTO tblonlinepaymentconfigdtl (strPGCode, strFieldDesc, strFieldValue, strClientCode"
                    + ",strDataPostFlag) "
                    + " VALUES ");
            boolean insert = false;
            for (int row = 0; row < tblPGDtl.getRowCount(); row++)
            {
                insert = true;
                
                String fieldDesc = tblPGDtl.getValueAt(row, 0).toString();
                String fieldValue = tblPGDtl.getValueAt(row, 1).toString();

                if (row == 0)
                {
                    sqlDtl.append("('" + paymentCode + "','" + fieldDesc + "','" + fieldValue + "','" + clsGlobalVarClass.gClientCode + "','N')");
                            
                }
                else
                {
                   sqlDtl.append(",('" + paymentCode + "','" + fieldDesc + "','" + fieldValue + "','" + clsGlobalVarClass.gClientCode + "','N')");
                    
                }
            }
            if (insert)
            {
                clsGlobalVarClass.dbMysql.execute(sqlDtl.toString());
                String sql = "update tblmasteroperationstatus set dteDateEdited='" + clsGlobalVarClass.getCurrentDateTime() + "' "
                        + " where strTableName='Payment Interface Master' ";
                clsGlobalVarClass.dbMysql.execute(sql);
                new frmOkPopUp(this, "Entry Added Successfully", "Successfull", 3).setVisible(true);
                funResetField();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void funUpdateDiscount()
    {

        try
        {
            String posCode = mapPOSName.get(cmbPosCode.getSelectedItem().toString());
            String paymentCode = txtPGCode.getText();
            String name = txtPGName.getText().trim();
           
            
            String updateQuery = "Update tblonlinepaymentconfighd "
                    + "SET strPGName=?,strPosCode=? "
                    + "WHERE strPGCode =? ";
            PreparedStatement pre = clsGlobalVarClass.conPrepareStatement.prepareStatement(updateQuery);
            pre.setString(1, name);
            pre.setString(2, posCode);
            pre.setString(3, paymentCode);
            
            int exc = pre.executeUpdate();
            pre.close();

            String deleteSql = "delete from tblonlinepaymentconfigdtl "
                    + "where strPGCode='" + paymentCode + "' ";
            clsGlobalVarClass.dbMysql.execute(deleteSql.toString());

            StringBuffer sqlDtl = new StringBuffer("INSERT INTO tblonlinepaymentconfigdtl (strPGCode, strFieldDesc, strFieldValue,strClientCode "
                    + ",strDataPostFlag) "
                    + " VALUES ");
            boolean insert = false;
            for (int row = 0; row < tblPGDtl.getRowCount(); row++)
            {
                insert = true;
                String fieldDesc = tblPGDtl.getValueAt(row, 0).toString();
                String fieldValue = tblPGDtl.getValueAt(row, 1).toString();
               

               if (row == 0)
                {
                    sqlDtl.append("('" + paymentCode + "','" + fieldDesc + "','" + fieldValue + "','" + clsGlobalVarClass.gClientCode + "','N')");
                            
                }
                else
                {
                   sqlDtl.append("('" + paymentCode + "','" + fieldDesc + "','" + fieldValue + "','" + clsGlobalVarClass.gClientCode + "','N')");
                    
                }
            }
            if (insert)
            {
                clsGlobalVarClass.dbMysql.execute(sqlDtl.toString());

                String sql = "update tblmasteroperationstatus set dteDateEdited='" + clsGlobalVarClass.getCurrentDateTime() + "' "
                        + " where strTableName='Payment Interface Master' ";
                clsGlobalVarClass.dbMysql.execute(sql);
                new frmOkPopUp(this, "Entry Added Successfully", "Successfull", 3).setVisible(true);
                funResetField();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private String funGetPaymentCode()
    {

        String payCode = "";

        try
        {

            selectQuery = "select count(*) from `tblonlinepaymentconfighd`  ";
            ResultSet countSet1 = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery);
            countSet1.next();
            int cn = countSet1.getInt(1);
            countSet1.close();
            if (cn > 0)
            {
                selectQuery = "select max(strPGCode ) from `tblonlinepaymentconfighd`  ";
                ResultSet countSet = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery);
                countSet.next();
                code = countSet.getString(1);
                StringBuilder sb = new StringBuilder(code);
                String ss = sb.delete(0, 2).toString();
                for (int i = 0; i < ss.length(); i++)
                {
                    if (ss.charAt(i) != '0')
                    {
                        strCode = ss.substring(i, ss.length());
                        break;
                    }
                }
                int intCode = Integer.parseInt(strCode);
                intCode++;

                if (intCode < 10)
                {
                    payCode = "PG00000" + intCode;
                }
                else if (intCode < 100)
                {
                    payCode = "PG0000" + intCode;
                }
                else if (intCode < 1000)
                {
                    payCode = "PG000" + intCode;
                }
                else if (intCode < 10000)
                {
                    payCode = "PG00" + intCode;
                }
                else if (intCode < 100000)
                {
                    payCode = "PG0" + intCode;
                }
                else if (intCode < 1000000)
                {
                    payCode = "PG" + intCode;
                }

            }
            else
            {
                code = "0";
                payCode = "PG000001";
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return payCode;
    }

    /**
     * This method is used to reset all fields
     */
    private void funResetField()
    {

        txtPGCode.requestFocus();
        btnNew.setText("SAVE");
        btnNew.setMnemonic('s');
        flag = false;
        txtPGCode.setText("");
        txtPGName.setText("");
        txtFieldDescName.setText("");
        txtFieldDescValue.setText("");
        cmbPosCode.setSelectedIndex(0);
       
        DefaultTableModel dtm = (DefaultTableModel) tblPGDtl.getModel();
        dtm.setRowCount(0);

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
        java.awt.GridBagConstraints gridBagConstraints;

        panelHeader = new javax.swing.JPanel();
        lblProductName = new javax.swing.JLabel();
        lblModuleName = new javax.swing.JLabel();
        lblfromName = new javax.swing.JLabel();
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
        lblDiscountCode = new javax.swing.JLabel();
        txtPGCode = new javax.swing.JTextField();
        btnCancel = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        btnNew = new javax.swing.JButton();
        lblFieldDescValue = new javax.swing.JLabel();
        lblCounterUserCode1 = new javax.swing.JLabel();
        cmbPosCode = new javax.swing.JComboBox();
        txtPGName = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPGDtl = new javax.swing.JTable();
        btnAdd = new javax.swing.JButton();
        btnRemove = new javax.swing.JButton();
        lblDiscOnValue = new javax.swing.JLabel();
        txtFieldDescValue = new javax.swing.JTextField();
        lblFiledDescName = new javax.swing.JLabel();
        txtFieldDescName = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
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
        lblProductName.setText("SPOS - ");
        panelHeader.add(lblProductName);

        lblModuleName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblModuleName.setForeground(new java.awt.Color(255, 255, 255));
        panelHeader.add(lblModuleName);

        lblfromName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblfromName.setForeground(new java.awt.Color(255, 255, 255));
        lblfromName.setText("-Third Party Interface Master");
        panelHeader.add(lblfromName);
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
        panelLayout.setOpaque(false);
        panelLayout.setPreferredSize(new java.awt.Dimension(800, 559));
        panelLayout.setLayout(new java.awt.GridBagLayout());

        panelBody.setBackground(new java.awt.Color(255, 255, 255));
        panelBody.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        panelBody.setMinimumSize(new java.awt.Dimension(800, 570));
        panelBody.setOpaque(false);

        lblDiscountCode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblDiscountCode.setText("Code :");

        txtPGCode.setEditable(false);
        txtPGCode.setBackground(new java.awt.Color(204, 204, 204));
        txtPGCode.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtPGCodeMouseClicked(evt);
            }
        });
        txtPGCode.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtPGCodeKeyPressed(evt);
            }
        });

        btnCancel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnCancel.setForeground(new java.awt.Color(255, 255, 255));
        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnCancel.setText("CLOSE");
        btnCancel.setToolTipText("Close Cost Center Master");
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

        btnNew.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnNew.setForeground(new java.awt.Color(255, 255, 255));
        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnNew.setText("SAVE");
        btnNew.setToolTipText("Save Cost Center Master");
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

        lblFieldDescValue.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblFieldDescValue.setText("Field Value         :");

        lblCounterUserCode1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblCounterUserCode1.setText("POS                      : ");

        cmbPosCode.setBackground(new java.awt.Color(51, 102, 255));
        cmbPosCode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmbPosCode.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbPosCodeKeyPressed(evt);
            }
        });

        txtPGName.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtPGNameMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt)
            {
                txtPGNameMouseEntered(evt);
            }
        });
        txtPGName.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtPGNameActionPerformed(evt);
            }
        });
        txtPGName.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtPGNameKeyPressed(evt);
            }
        });

        tblPGDtl.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tblPGDtl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Name", "Value"
            }
        )
        {
            boolean[] canEdit = new boolean []
            {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return canEdit [columnIndex];
            }
        });
        tblPGDtl.setRowHeight(25);
        tblPGDtl.setSelectionBackground(new java.awt.Color(15, 131, 240));
        tblPGDtl.setSelectionForeground(new java.awt.Color(254, 254, 254));
        tblPGDtl.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tblPGDtl);

        btnAdd.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnAdd.setForeground(new java.awt.Color(255, 255, 255));
        btnAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnAdd.setText("ADD");
        btnAdd.setToolTipText("Save Cost Center Master");
        btnAdd.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnAdd.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnAdd.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnAddMouseClicked(evt);
            }
        });
        btnAdd.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnAddActionPerformed(evt);
            }
        });
        btnAdd.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                btnAddKeyPressed(evt);
            }
        });

        btnRemove.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnRemove.setForeground(new java.awt.Color(255, 255, 255));
        btnRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnRemove.setText("REMOVE");
        btnRemove.setToolTipText("Save Cost Center Master");
        btnRemove.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRemove.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnRemove.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnRemoveMouseClicked(evt);
            }
        });
        btnRemove.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnRemoveActionPerformed(evt);
            }
        });
        btnRemove.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                btnRemoveKeyPressed(evt);
            }
        });

        txtFieldDescValue.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtFieldDescValueMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt)
            {
                txtFieldDescValueMouseEntered(evt);
            }
        });
        txtFieldDescValue.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtFieldDescValueActionPerformed(evt);
            }
        });
        txtFieldDescValue.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtFieldDescValueKeyPressed(evt);
            }
        });

        lblFiledDescName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblFiledDescName.setText("Field Description :");

        txtFieldDescName.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtFieldDescNameMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt)
            {
                txtFieldDescNameMouseEntered(evt);
            }
        });
        txtFieldDescName.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtFieldDescNameActionPerformed(evt);
            }
        });
        txtFieldDescName.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtFieldDescNameKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout panelBodyLayout = new javax.swing.GroupLayout(panelBody);
        panelBody.setLayout(panelBodyLayout);
        panelBodyLayout.setHorizontalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBodyLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblFieldDescValue, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                            .addComponent(lblFiledDescName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelBodyLayout.createSequentialGroup()
                                .addComponent(txtFieldDescName, javax.swing.GroupLayout.PREFERRED_SIZE, 333, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(28, 28, 28))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBodyLayout.createSequentialGroup()
                                .addComponent(txtFieldDescValue, javax.swing.GroupLayout.PREFERRED_SIZE, 339, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addComponent(lblDiscOnValue, javax.swing.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE))
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelBodyLayout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 579, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnRemove, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelBodyLayout.createSequentialGroup()
                                .addComponent(lblDiscountCode)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtPGCode, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtPGName, javax.swing.GroupLayout.PREFERRED_SIZE, 478, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelBodyLayout.createSequentialGroup()
                                .addComponent(lblCounterUserCode1, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmbPosCode, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())))
        );
        panelBodyLayout.setVerticalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDiscountCode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPGName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPGCode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblCounterUserCode1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBodyLayout.createSequentialGroup()
                        .addComponent(cmbPosCode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)))
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelBodyLayout.createSequentialGroup()
                                .addGap(73, 73, 73)
                                .addComponent(lblDiscOnValue, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelBodyLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtFieldDescName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblFiledDescName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblFieldDescValue, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtFieldDescValue, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE))
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnRemove, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(92, 92, 92)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 6, 0);
        panelLayout.add(panelBody, gridBagConstraints);

        getContentPane().add(panelLayout, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
        clsGlobalVarClass.hmActiveForms.remove("Payment Interface Master");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        clsGlobalVarClass.hmActiveForms.remove("Payment Interface Master");
    }//GEN-LAST:event_formWindowClosing

    private void btnNewKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnNewKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            if (btnNew.getText().equalsIgnoreCase("SAVE"))
            {
                //Add new cost center
                funSaveDiscount();
            }
            else
            {
                //Update existing cost center
                funUpdateDiscount();
            }
        }
    }//GEN-LAST:event_btnNewKeyPressed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        funSaveUpdate();
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnNewMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNewMouseClicked
        // TODO add your handling code here:
        if (btnNew.getText().equalsIgnoreCase("SAVE"))
        {
            //Add new cost center
            funSaveDiscount();
        }
        else
        {
            //Update existing cost center
            funUpdateDiscount();
        }
    }//GEN-LAST:event_btnNewMouseClicked

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        // TODO add your handling code here:
        funResetField();
    }//GEN-LAST:event_btnResetActionPerformed

    private void btnResetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnResetMouseClicked
        // TODO add your handling code here:
        funResetField();
    }//GEN-LAST:event_btnResetMouseClicked

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        dispose();
        clsGlobalVarClass.hmActiveForms.remove("Payment Interface Master");
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnCancelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseClicked
        // TODO add your handling code here:
        try
        {
            dispose();
            clsGlobalVarClass.hmActiveForms.remove("Payment Interface Master");
        }
        catch (Exception e)
        {
        }
    }//GEN-LAST:event_btnCancelMouseClicked

    private void txtPGCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPGCodeKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() == '?' || evt.getKeyChar() == '/')
        {
            funSelectDiscountCode();
        }
        if (evt.getKeyCode() == 10)
        {

        }
    }//GEN-LAST:event_txtPGCodeKeyPressed

    private void txtPGCodeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPGCodeMouseClicked
        // TODO add your handling code here:
        funSelectDiscountCode();
    }//GEN-LAST:event_txtPGCodeMouseClicked

    private void cmbPosCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbPosCodeKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbPosCodeKeyPressed

    private void txtPGNameMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtPGNameMouseClicked
    {//GEN-HEADEREND:event_txtPGNameMouseClicked
        try
        {
            if (txtPGName.getText().length() == 0)
            {
                new frmAlfaNumericKeyBoard(this, true, "1", "Enter Name").setVisible(true);
                txtPGName.setText(clsGlobalVarClass.gKeyboardValue);
            }
            else
            {
                new frmAlfaNumericKeyBoard(this, true, txtPGName.getText(), "1", "Enter Name").setVisible(true);
                txtPGName.setText(clsGlobalVarClass.gKeyboardValue);
            }
            
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_txtPGNameMouseClicked

    private void txtPGNameActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtPGNameActionPerformed
    {//GEN-HEADEREND:event_txtPGNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPGNameActionPerformed

    private void txtPGNameKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtPGNameKeyPressed
    {//GEN-HEADEREND:event_txtPGNameKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPGNameKeyPressed

    private void btnAddMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnAddMouseClicked
    {//GEN-HEADEREND:event_btnAddMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAddMouseClicked

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnAddActionPerformed
    {//GEN-HEADEREND:event_btnAddActionPerformed
        funAddButtonClicked();
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnAddKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_btnAddKeyPressed
    {//GEN-HEADEREND:event_btnAddKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAddKeyPressed

    private void btnRemoveMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnRemoveMouseClicked
    {//GEN-HEADEREND:event_btnRemoveMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnRemoveMouseClicked

    private void btnRemoveActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnRemoveActionPerformed
    {//GEN-HEADEREND:event_btnRemoveActionPerformed
        funRemoveButtonClicked();
    }//GEN-LAST:event_btnRemoveActionPerformed

    private void btnRemoveKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_btnRemoveKeyPressed
    {//GEN-HEADEREND:event_btnRemoveKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnRemoveKeyPressed

    private void txtPGNameMouseEntered(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtPGNameMouseEntered
    {//GEN-HEADEREND:event_txtPGNameMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPGNameMouseEntered

    private void txtFieldDescValueMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtFieldDescValueMouseClicked
    {//GEN-HEADEREND:event_txtFieldDescValueMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFieldDescValueMouseClicked

    private void txtFieldDescValueMouseEntered(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtFieldDescValueMouseEntered
    {//GEN-HEADEREND:event_txtFieldDescValueMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFieldDescValueMouseEntered

    private void txtFieldDescValueActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtFieldDescValueActionPerformed
    {//GEN-HEADEREND:event_txtFieldDescValueActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFieldDescValueActionPerformed

    private void txtFieldDescValueKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtFieldDescValueKeyPressed
    {//GEN-HEADEREND:event_txtFieldDescValueKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFieldDescValueKeyPressed

    private void txtFieldDescNameMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtFieldDescNameMouseClicked
    {//GEN-HEADEREND:event_txtFieldDescNameMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFieldDescNameMouseClicked

    private void txtFieldDescNameMouseEntered(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtFieldDescNameMouseEntered
    {//GEN-HEADEREND:event_txtFieldDescNameMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFieldDescNameMouseEntered

    private void txtFieldDescNameActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtFieldDescNameActionPerformed
    {//GEN-HEADEREND:event_txtFieldDescNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFieldDescNameActionPerformed

    private void txtFieldDescNameKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtFieldDescNameKeyPressed
    {//GEN-HEADEREND:event_txtFieldDescNameKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFieldDescNameKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnRemove;
    private javax.swing.JButton btnReset;
    private javax.swing.JComboBox cmbPosCode;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblCounterUserCode1;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblDiscOnValue;
    private javax.swing.JLabel lblDiscountCode;
    private javax.swing.JLabel lblFieldDescValue;
    private javax.swing.JLabel lblFiledDescName;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblfromName;
    private javax.swing.JPanel panelBody;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelLayout;
    private javax.swing.JTable tblPGDtl;
    private javax.swing.JTextField txtFieldDescName;
    private javax.swing.JTextField txtFieldDescValue;
    private javax.swing.JTextField txtPGCode;
    private javax.swing.JTextField txtPGName;
    // End of variables declaration//GEN-END:variables

    private void funSaveUpdate()
    {
        String discName = txtPGName.getText().trim();
        

        if (discName.isEmpty())
        {
            new frmOkPopUp(this, "Please Enter Name", "Error", 1).setVisible(true);
            return;
        }
        
        if (tblPGDtl.getRowCount() <= 0)
        {
            new frmOkPopUp(this, "Please Enter Detail", "Error", 1).setVisible(true);
            return;
        }

        if (btnNew.getText().equalsIgnoreCase("SAVE"))
        {
            funSaveDiscount();
        }
        else
        {
            funUpdateDiscount();
        }

        funResetField();
    }

    private void funAddButtonClicked()
    {

        String paymentCode = txtPGCode.getText();
        String name = txtPGName.getText().trim();
        String fieldDesc = txtFieldDescName.getText().trim();
        String fieldValue = txtFieldDescValue.getText().trim();
       
        if (txtFieldDescName.getText().toString().equalsIgnoreCase(""))
        {
            new frmOkPopUp(this, "Please Enter Field Desc", "Error", 0).setVisible(true);
            return;
        }
        
        if (txtFieldDescValue.getText().toString().equalsIgnoreCase(""))
        {
            new frmOkPopUp(this, "Please Enter Field Desc Value", "Error", 0).setVisible(true);
            return;
        }
        
        boolean isExists = false;
        for (int i = 0; i < tblPGDtl.getRowCount(); i++)
        {
            if (fieldDesc.equalsIgnoreCase(tblPGDtl.getValueAt(i, 0).toString()))
            {
                isExists = true;
                break;
            }
        }
        if (isExists)
        {
            new frmOkPopUp(this, "Duplicate Field Name.", "Error", 0).setVisible(true);
            return;
        }


        Object row[] =
        {
             fieldDesc,fieldValue
        };
        DefaultTableModel dtm = (DefaultTableModel) tblPGDtl.getModel();
        dtm.addRow(row);

       txtFieldDescName.setText("");
       txtFieldDescValue.setText("");

    }

    private void funRemoveButtonClicked()
    {
        int selectedRow = tblPGDtl.getSelectedRow();
        if (selectedRow < 0)
        {
            new frmOkPopUp(this, "Please Select Details.", "Error", 0).setVisible(true);
            return;
        }
        DefaultTableModel dtm = (DefaultTableModel) tblPGDtl.getModel();
        dtm.removeRow(selectedRow);

    }

    

}
