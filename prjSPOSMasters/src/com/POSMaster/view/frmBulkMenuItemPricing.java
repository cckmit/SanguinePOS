/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSMaster.view;

import com.POSGlobal.controller.clsFixedColumnTable;
import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsTextFieldOnlyNumber;
import com.POSGlobal.controller.clsUtility;
import com.POSGlobal.view.frmOkPopUp;
import java.awt.Desktop;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;

public class frmBulkMenuItemPricing extends javax.swing.JFrame
{

    private Map<String, String> mapPosCode = new TreeMap<>();
    private Map<String, String> mapMenuHead = new TreeMap<>();
    private Map<String, String> mapSubMenuHead = new TreeMap<String, String>();
    private Set<String> setForMultiple = new HashSet<String>();
    private Map<String, String> mapCostCenter = new TreeMap<>();
    private Map<String, String> mapArea = new TreeMap<>();
    private Map<String, String> mapAreaForMultiple = new TreeMap<>();
    private Map<String, String> mapMultipleArea = new TreeMap<>();
    private StringBuilder sb = new StringBuilder();
    private StringBuilder sbBulkUpdate = new StringBuilder();
    private ResultSet rs;
    private JComboBox menuHeadComboBox = new JComboBox();
    private JComboBox subMenuHeadComboBox = new JComboBox();

    private JComboBox areaComboBox = new JComboBox();
    private JComboBox costCenterComboBox = new JComboBox();
    private JComboBox colorComboBox = new JComboBox();
    private JComboBox popularComoBox = new JComboBox();

    private JComboBox amPmFromComoBox = new JComboBox();
    private JComboBox amPmToComoBox = new JComboBox();

    private JTextField priceText = new JTextField();
    private clsFixedColumnTable fct;
    private Set<String> setMenuNames = new HashSet<String>();
    private ArrayList valuesList;
    private Set<String> keySet;
    private ArrayList keyList;
    private clsUtility objUtility = new clsUtility();
    private DecimalFormat gDecimalFormat = clsGlobalVarClass.funGetGlobalDecimalFormatter();

    /**
     * Creates new form frmBulkMenuItemPricing
     */
    public frmBulkMenuItemPricing()
    {
        try
        {

            // Set System L&F
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        }
        catch (UnsupportedLookAndFeelException e)
        {
            // handle exception
        }
        catch (ClassNotFoundException e)
        {
            // handle exception
        }
        catch (InstantiationException e)
        {
            // handle exception
        }
        catch (IllegalAccessException e)
        {
            // handle exception
        }

        initComponents();

        lblUserCode.setText(clsGlobalVarClass.gUserCode);
        lblPosName.setText(clsGlobalVarClass.gPOSName);
        lblDate.setText(clsGlobalVarClass.gPOSDateToDisplay);
        lblModuleName.setText(clsGlobalVarClass.gSelectedModule);
        fct = new clsFixedColumnTable(2, srollPane);
        fct.getFixedTable().getTableHeader().setReorderingAllowed(false);
        fct.getFixedTable().setRowHeight(25);
        priceText.setDocument(new clsTextFieldOnlyNumber(7,3).new JNumberFieldFilter());
        funLoadColorComboBox();
        funLoadPopularComboBox();
        funLoadmapPosCode();
        funLoadmapMenuHead();
        funLoadmapSubMenuHead();

        funLoadmapCostCenter();
        funLoadmapArea();

        funLoadExpiredCombo();
        funLoadPOSCombo();
        funLoadMenuHeadCombo();
        funLoadSubMenuHeadCombo();
        funLoadCostCenterCombo();
        funLoadAreaCombo();
        funLoadampmFromComboBox();
        funLoadampmToComboBox();

        funsetPriceDtlTableProperty();
        funSetShortCutKeys();
    }

    private void funSetShortCutKeys()
    {
        btnExit.setMnemonic('c');
        btnExecute.setMnemonic('s');
        btnReset.setMnemonic('r');
        btnUpdate.setMnemonic('u');

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
        panelLayout = new JPanel() {  
            public void paintComponent(Graphics g) {  
                Image img = Toolkit.getDefaultToolkit().getImage(  
                    getClass().getResource("/com/POSMaster/images/imgBGJPOS.png"));  
                g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);  
            }  
        };  ;
        panelBody = new javax.swing.JPanel();
        lblPosCode = new javax.swing.JLabel();
        lblMenuHead = new javax.swing.JLabel();
        lblCostCenter = new javax.swing.JLabel();
        lblArea = new javax.swing.JLabel();
        btnExecute = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnExport = new javax.swing.JButton();
        btnExit = new javax.swing.JButton();
        cmbPosCode = new javax.swing.JComboBox();
        cmbMenuHead = new javax.swing.JComboBox();
        cmbCostCenter = new javax.swing.JComboBox();
        cmbArea = new javax.swing.JComboBox();
        separator1 = new javax.swing.JSeparator();
        srollPane = new javax.swing.JScrollPane();
        tblPriceDtl = new javax.swing.JTable();
        btnReset = new javax.swing.JButton();
        btnChangePrice = new javax.swing.JButton();
        lblSortBy = new javax.swing.JLabel();
        cmbSortBy = new javax.swing.JComboBox();
        cmbExpiredItem = new javax.swing.JComboBox();
        lblExpiredItems = new javax.swing.JLabel();

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
        lblformName.setText("- Bulk Menu Item Pricing");
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

        panelLayout.setLayout(new java.awt.GridBagLayout());

        panelBody.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        panelBody.setMinimumSize(new java.awt.Dimension(800, 570));
        panelBody.setOpaque(false);
        panelBody.setPreferredSize(new java.awt.Dimension(800, 570));

        lblPosCode.setText("POS Code");

        lblMenuHead.setText("Menu Head");

        lblCostCenter.setText("Cost Center ");

        lblArea.setText("Area");

        btnExecute.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        btnExecute.setForeground(new java.awt.Color(251, 246, 246));
        btnExecute.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnExecute.setText("Execute");
        btnExecute.setToolTipText("Execute");
        btnExecute.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnExecute.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnExecute.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnExecuteActionPerformed(evt);
            }
        });
        btnExecute.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                btnExecuteKeyPressed(evt);
            }
        });

        btnUpdate.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        btnUpdate.setForeground(new java.awt.Color(251, 246, 246));
        btnUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnUpdate.setText("Update");
        btnUpdate.setToolTipText("Update Data");
        btnUpdate.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnUpdate.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnUpdate.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnUpdateActionPerformed(evt);
            }
        });

        btnExport.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        btnExport.setForeground(new java.awt.Color(251, 246, 246));
        btnExport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnExport.setText("Export");
        btnExport.setToolTipText("Export To Excel File");
        btnExport.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnExport.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnExport.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnExportActionPerformed(evt);
            }
        });

        btnExit.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        btnExit.setForeground(new java.awt.Color(251, 246, 246));
        btnExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnExit.setText("Exit");
        btnExit.setToolTipText("Close Menu Item Pricing");
        btnExit.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnExit.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnExit.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnExitMouseClicked(evt);
            }
        });
        btnExit.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnExitActionPerformed(evt);
            }
        });
        btnExit.addVetoableChangeListener(new java.beans.VetoableChangeListener()
        {
            public void vetoableChange(java.beans.PropertyChangeEvent evt)throws java.beans.PropertyVetoException
            {
                btnExitVetoableChange(evt);
            }
        });

        cmbPosCode.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbPosCodeActionPerformed(evt);
            }
        });
        cmbPosCode.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbPosCodeKeyPressed(evt);
            }
        });

        cmbMenuHead.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbMenuHeadKeyPressed(evt);
            }
        });

        cmbCostCenter.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbCostCenterKeyPressed(evt);
            }
        });

        cmbArea.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbAreaActionPerformed(evt);
            }
        });
        cmbArea.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbAreaKeyPressed(evt);
            }
        });

        tblPriceDtl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Item Code", "Item Name", "Menu Name", "Popular", "PriceSunday", "PriceMonday", "PriceTuesday", "PriceWednesday", "PriceThursday", "PriceFriday", "PriceSaturday", "FromDate", "ToDate", "TimeFrom", "AMPMFrom", "TimeTo", "AMPMTo", "CostCenter", "TextColor", "Area ", "SubMenuHeadCode", "HourlyPricing", "Is Expired"
            }
        )
        {
            boolean[] canEdit = new boolean []
            {
                false, false, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return canEdit [columnIndex];
            }
        });
        tblPriceDtl.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblPriceDtl.setRowHeight(25);
        tblPriceDtl.getTableHeader().setReorderingAllowed(false);
        tblPriceDtl.addFocusListener(new java.awt.event.FocusAdapter()
        {
            public void focusGained(java.awt.event.FocusEvent evt)
            {
                tblPriceDtlFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt)
            {
                tblPriceDtlFocusLost(evt);
            }
        });
        tblPriceDtl.addInputMethodListener(new java.awt.event.InputMethodListener()
        {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt)
            {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt)
            {
                tblPriceDtlInputMethodTextChanged(evt);
            }
        });
        tblPriceDtl.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                tblPriceDtlKeyPressed(evt);
            }
        });
        srollPane.setViewportView(tblPriceDtl);
        if (tblPriceDtl.getColumnModel().getColumnCount() > 0)
        {
            tblPriceDtl.getColumnModel().getColumn(0).setResizable(false);
            tblPriceDtl.getColumnModel().getColumn(1).setPreferredWidth(220);
            tblPriceDtl.getColumnModel().getColumn(2).setPreferredWidth(220);
            tblPriceDtl.getColumnModel().getColumn(3).setMinWidth(50);
            tblPriceDtl.getColumnModel().getColumn(3).setPreferredWidth(50);
            tblPriceDtl.getColumnModel().getColumn(3).setMaxWidth(50);
        }

        btnReset.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        btnReset.setForeground(new java.awt.Color(250, 243, 243));
        btnReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnReset.setText("Reset");
        btnReset.setToolTipText("Reset All Fields");
        btnReset.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnReset.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnReset.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnResetActionPerformed(evt);
            }
        });

        btnChangePrice.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        btnChangePrice.setForeground(new java.awt.Color(251, 246, 246));
        btnChangePrice.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn3.png"))); // NOI18N
        btnChangePrice.setText("Change Price");
        btnChangePrice.setToolTipText("Change Item Price");
        btnChangePrice.setEnabled(false);
        btnChangePrice.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnChangePrice.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnChangePrice.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnChangePriceActionPerformed(evt);
            }
        });
        btnChangePrice.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                btnChangePriceKeyPressed(evt);
            }
        });

        lblSortBy.setText("Sort By");

        cmbSortBy.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "NONE", "Item Code", "Item Name", "Menu Head", "Cost Center", "Area", "POS" }));
        cmbSortBy.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbSortByActionPerformed(evt);
            }
        });
        cmbSortBy.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbSortByKeyPressed(evt);
            }
        });

        cmbExpiredItem.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Yes", "No" }));
        cmbExpiredItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbExpiredItemActionPerformed(evt);
            }
        });

        lblExpiredItems.setText("Expired Items");

        javax.swing.GroupLayout panelBodyLayout = new javax.swing.GroupLayout(panelBody);
        panelBody.setLayout(panelBodyLayout);
        panelBodyLayout.setHorizontalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(separator1)
            .addComponent(srollPane)
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addComponent(btnExecute, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnChangePrice, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addComponent(lblPosCode)
                        .addGap(23, 23, 23)
                        .addComponent(cmbPosCode, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addComponent(lblMenuHead)
                        .addGap(18, 18, 18)
                        .addComponent(cmbMenuHead, 0, 174, Short.MAX_VALUE)))
                .addGap(19, 19, 19)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(lblArea, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblSortBy, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(16, 16, 16)
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cmbArea, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmbSortBy, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBodyLayout.createSequentialGroup()
                                .addComponent(lblCostCenter)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                            .addGroup(panelBodyLayout.createSequentialGroup()
                                .addComponent(lblExpiredItems)
                                .addGap(5, 5, 5)))
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(cmbExpiredItem, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmbCostCenter, 0, 164, Short.MAX_VALUE))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBodyLayout.createSequentialGroup()
                        .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32)
                        .addComponent(btnExport, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 63, Short.MAX_VALUE)
                        .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnExit, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        panelBodyLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cmbArea, cmbCostCenter, cmbMenuHead, cmbPosCode});

        panelBodyLayout.setVerticalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(cmbPosCode, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                        .addComponent(lblPosCode, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(cmbCostCenter, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblCostCenter, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmbArea)))
                    .addComponent(lblArea, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(cmbExpiredItem))
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelBodyLayout.createSequentialGroup()
                                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lblMenuHead, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cmbMenuHead)
                                    .addComponent(lblSortBy, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(cmbSortBy, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblExpiredItems, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(18, 18, 18)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnExecute, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnExport, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnExit, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnChangePrice, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(separator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16)
                .addComponent(srollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 391, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        panelBodyLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {cmbArea, cmbCostCenter, cmbMenuHead, cmbPosCode});

        panelLayout.add(panelBody, new java.awt.GridBagConstraints());

        getContentPane().add(panelLayout, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void funExecute()
    {

        funExecuteClick();

        valuesList = new ArrayList();
        keySet = mapSubMenuHead.keySet();
        keyList = new ArrayList(keySet);

        for (int i = 0; i < keyList.size(); i++)
        {
            //System.out.println(" \nkey ="+keyList.get(i));
            //System.out.println(" \nvalue ="+valuesList.get(i));
            valuesList.add(mapSubMenuHead.get(keyList.get(i)));
        }

    }
    private void btnExecuteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExecuteActionPerformed

        funExecute();

    }//GEN-LAST:event_btnExecuteActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        if (cmbArea.getSelectedItem().toString().equalsIgnoreCase("Multiple"))
        {
            funUpdateMultipleAreaClick();
        }
        else
        {
            funUpdateClick();
        }
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportActionPerformed
        funExportClick();
    }//GEN-LAST:event_btnExportActionPerformed

    private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitActionPerformed
        clsGlobalVarClass.hmActiveForms.remove("Bulk Menu Item Pricing");
        funExitClick();

    }//GEN-LAST:event_btnExitActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        funResetClick();
    }//GEN-LAST:event_btnResetActionPerformed

    private void cmbAreaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbAreaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbAreaActionPerformed

    private void tblPriceDtlInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_tblPriceDtlInputMethodTextChanged
        // TODO add your handling code here:

    }//GEN-LAST:event_tblPriceDtlInputMethodTextChanged

    private void tblPriceDtlKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblPriceDtlKeyPressed
        // TODO add your handling code here:
        // System.out.println("key code="+evt.getKeyCode()+"\tChar="+evt.getKeyChar()+"\textendes key code"+evt.getExtendedKeyCode());


    }//GEN-LAST:event_tblPriceDtlKeyPressed

    private void tblPriceDtlFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tblPriceDtlFocusLost

    }//GEN-LAST:event_tblPriceDtlFocusLost

    private void tblPriceDtlFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tblPriceDtlFocusGained
        // TODO add your handling code here:

        System.out.println("Focus gain");

        if (tblPriceDtl.getSelectedColumn() == 2)
        {
            System.out.println("Price=" + tblPriceDtl.getValueAt(tblPriceDtl.getSelectedRow(), 2));
            tblPriceDtl.setValueAt(tblPriceDtl.getValueAt(tblPriceDtl.getSelectedRow(), 2), tblPriceDtl.getSelectedRow(), 3);
            tblPriceDtl.setValueAt(tblPriceDtl.getValueAt(tblPriceDtl.getSelectedRow(), 2), tblPriceDtl.getSelectedRow(), 4);
            tblPriceDtl.setValueAt(tblPriceDtl.getValueAt(tblPriceDtl.getSelectedRow(), 2), tblPriceDtl.getSelectedRow(), 5);
            tblPriceDtl.setValueAt(tblPriceDtl.getValueAt(tblPriceDtl.getSelectedRow(), 2), tblPriceDtl.getSelectedRow(), 6);
            tblPriceDtl.setValueAt(tblPriceDtl.getValueAt(tblPriceDtl.getSelectedRow(), 2), tblPriceDtl.getSelectedRow(), 7);
            tblPriceDtl.setValueAt(tblPriceDtl.getValueAt(tblPriceDtl.getSelectedRow(), 2), tblPriceDtl.getSelectedRow(), 8);

        }
    }//GEN-LAST:event_tblPriceDtlFocusGained

    private void btnChangePriceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChangePriceActionPerformed

        new frmBulkItemPriceChange(tblPriceDtl).setVisible(true);

    }//GEN-LAST:event_btnChangePriceActionPerformed

    private void cmbPosCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbPosCodeKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            cmbMenuHead.requestFocus();
        }
    }//GEN-LAST:event_cmbPosCodeKeyPressed

    private void cmbMenuHeadKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbMenuHeadKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            cmbCostCenter.requestFocus();
        }
    }//GEN-LAST:event_cmbMenuHeadKeyPressed

    private void cmbCostCenterKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbCostCenterKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            cmbArea.requestFocus();
        }
    }//GEN-LAST:event_cmbCostCenterKeyPressed

    private void cmbAreaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbAreaKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            btnChangePrice.requestFocus();
        }
    }//GEN-LAST:event_cmbAreaKeyPressed

    private void btnExecuteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnExecuteKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            funExecute();
        }
    }//GEN-LAST:event_btnExecuteKeyPressed

    private void btnChangePriceKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnChangePriceKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            btnExecute.requestFocus();
        }
    }//GEN-LAST:event_btnChangePriceKeyPressed

    private void cmbSortByKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_cmbSortByKeyPressed
    {//GEN-HEADEREND:event_cmbSortByKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbSortByKeyPressed

    private void cmbSortByActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbSortByActionPerformed
    {//GEN-HEADEREND:event_cmbSortByActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbSortByActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
        clsGlobalVarClass.hmActiveForms.remove("Bulk Menu Item Pricing");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        clsGlobalVarClass.hmActiveForms.remove("Bulk Menu Item Pricing");
    }//GEN-LAST:event_formWindowClosing

    private void btnExitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnExitMouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_btnExitMouseClicked

    private void btnExitVetoableChange(java.beans.PropertyChangeEvent evt)throws java.beans.PropertyVetoException {//GEN-FIRST:event_btnExitVetoableChange
        // TODO add your handling code here:
    }//GEN-LAST:event_btnExitVetoableChange

    private void cmbExpiredItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbExpiredItemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbExpiredItemActionPerformed

    private void cmbPosCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbPosCodeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbPosCodeActionPerformed

    /**
     *
     *
     * public static void main(String args[]) {
     *
     * try { for (javax.swing.UIManager.LookAndFeelInfo info :
     * javax.swing.UIManager.getInstalledLookAndFeels()) { if
     * ("Nimbus".equals(info.getName())) {
     * javax.swing.UIManager.setLookAndFeel(info.getClassName()); break; } } }
     * catch (ClassNotFoundException ex) {
     * java.util.logging.Logger.getLogger(frmBulkMenuItemPricing.class.getName()).log(java.util.logging.Level.SEVERE,
     * null, ex); } catch (InstantiationException ex) {
     * java.util.logging.Logger.getLogger(frmBulkMenuItemPricing.class.getName()).log(java.util.logging.Level.SEVERE,
     * null, ex); } catch (IllegalAccessException ex) {
     * java.util.logging.Logger.getLogger(frmBulkMenuItemPricing.class.getName()).log(java.util.logging.Level.SEVERE,
     * null, ex); } catch (javax.swing.UnsupportedLookAndFeelException ex) {
     * java.util.logging.Logger.getLogger(frmBulkMenuItemPricing.class.getName()).log(java.util.logging.Level.SEVERE,
     * null, ex); }
     *
     * java.awt.EventQueue.invokeLater(new Runnable() { public void run() { new
     * frmBulkMenuItemPricing().setVisible(true); } }); }
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnChangePrice;
    private javax.swing.JButton btnExecute;
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnExport;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JComboBox cmbArea;
    private javax.swing.JComboBox cmbCostCenter;
    private javax.swing.JComboBox cmbExpiredItem;
    private javax.swing.JComboBox cmbMenuHead;
    private javax.swing.JComboBox cmbPosCode;
    private javax.swing.JComboBox cmbSortBy;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JLabel lblArea;
    private javax.swing.JLabel lblCostCenter;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblExpiredItems;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblMenuHead;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblPosCode;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblSortBy;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblformName;
    private javax.swing.JPanel panelBody;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelLayout;
    private javax.swing.JSeparator separator1;
    private javax.swing.JScrollPane srollPane;
    private javax.swing.JTable tblPriceDtl;
    // End of variables declaration//GEN-END:variables

    /**
     * This method is used to execute on click
     */
    private void funExecuteClick()
    {

        try
        {
            if (cmbPosCode.getSelectedIndex() == 0)
            {
                JOptionPane.showMessageDialog(this, "Please Select POS", "Error", JOptionPane.INFORMATION_MESSAGE);
                cmbPosCode.requestFocus();
                return;
            }
            if (cmbArea.getSelectedIndex() == 0)
            {
                JOptionPane.showMessageDialog(this, "Please Select area", "Error", JOptionPane.INFORMATION_MESSAGE);
                cmbArea.requestFocus();
                return;
            }

            funFreezeComboBox(false);
            String posCode = mapPosCode.get(cmbPosCode.getSelectedItem().toString());
            DefaultTableModel dmImemTable = (DefaultTableModel) tblPriceDtl.getModel();
            dmImemTable.setRowCount(0);
            sb.setLength(0);

            rs = clsGlobalVarClass.dbMysql.executeResultSet(funGetExecuteString().toString());

            while (rs.next())
            {

                Object[] itemRows =
                {
                    rs.getString(1), rs.getString(2), rs.getString(4), rs.getString(5),
                    gDecimalFormat.format(rs.getDouble(6)),gDecimalFormat.format(rs.getDouble(7)),gDecimalFormat.format(rs.getDouble(8)),
                    gDecimalFormat.format(rs.getDouble(9)),gDecimalFormat.format(rs.getDouble(10)),gDecimalFormat.format(rs.getDouble(11)),
                    gDecimalFormat.format(rs.getDouble(12)), rs.getString(13), rs.getString(14), rs.getString(15),
                    rs.getString(16), rs.getString(17), rs.getString(18), rs.getString(20),
                    rs.getString(21), rs.getString(23), rs.getString(26), rs.getString(25), rs.getString(27)
                };
                String area = rs.getString(23);
                String areaCode = mapArea.get(area);
                setForMultiple.add(areaCode);
//                if((!area.equalsIgnoreCase("All"))&&(!area.equalsIgnoreCase("Multiple")))
//                {
                dmImemTable.addRow(itemRows);
//                }

            }
            System.out.println(areaComboBox);
            if (cmbArea.getSelectedItem().toString().equalsIgnoreCase("Multiple"))
            {
                areaComboBox.removeItem("All");
                areaComboBox.removeItem("Multiple");
            }
            rs.close();
            tblPriceDtl.setModel(dmImemTable);
            sb.setLength(0);

            if (tblPriceDtl.getRowCount() > 0)
            {
                btnChangePrice.setEnabled(true);
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }

    }

    /**
     * This method is used to update data
     */
    private void funUpdateClick()
    {
        try
        {
            if (tblPriceDtl.getRowCount() == 0)
            {
                JOptionPane.showMessageDialog(this, "Please Perform Execute", "Error", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            if (cmbPosCode.getSelectedIndex() == 0)
            {
                JOptionPane.showMessageDialog(this, "Please Select POS", "Error", JOptionPane.INFORMATION_MESSAGE);
                cmbPosCode.requestFocus();
                return;
            }
            if (cmbArea.getSelectedIndex() == 0)
            {
                JOptionPane.showMessageDialog(this, "Please Select area", "Error", JOptionPane.INFORMATION_MESSAGE);
                cmbArea.requestFocus();
                return;
            }

            String posCode = mapPosCode.get(cmbPosCode.getSelectedItem().toString());

            int ch = JOptionPane.showConfirmDialog(new JPanel(), "Do you want to Update All Item?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (ch == JOptionPane.YES_OPTION)
            {

                sb.setLength(0);
                sb.append("delete from tblmenuitempricingdtl where strPosCode='");
                sb.append(posCode).append("' ");

                if (cmbMenuHead.getSelectedIndex() > 0)
                {
                    String menuCode = mapMenuHead.get(cmbMenuHead.getSelectedItem().toString());
                    sb.append("and strMenuCode='");
                    sb.append(menuCode).append("' ");
                }

                if (cmbCostCenter.getSelectedIndex() > 0)
                {
                    String costCenterCode = mapCostCenter.get(cmbCostCenter.getSelectedItem().toString());
                    sb.append("and strCostCenterCode='");
                    sb.append(costCenterCode).append("' ");
                }

                if (cmbArea.getSelectedIndex() > 0)
                {
                    String areaCode = mapArea.get(cmbArea.getSelectedItem().toString());

                    sb.append("and strAreaCode='");
                    sb.append(areaCode).append("' ");

                }
                clsGlobalVarClass.dbMysql.execute(sb.toString());

                sb.setLength(0);
                sbBulkUpdate.setLength(0);
                sbBulkUpdate.append("INSERT INTO tblmenuitempricingdtl(strItemCode,strItemName,strPosCode,"
                        + "strMenuCode,strPopular,strPriceSunday,strPriceMonday,strPriceTuesday,strPriceWednesday"
                        + ",strPriceThursday,strPriceFriday,strPriceSaturday,dteFromDate,dteToDate,tmeTimeFrom,"
                        + "strAMPMFrom,tmeTimeTo,strAMPMTo,strCostCenterCode,strTextColor,strUserCreated"
                        + ",strUserEdited,dteDateCreated,dteDateEdited,strAreaCode,strSubMenuHeadCode,strHourlyPricing,strClientCode)"
                        + " VALUES ");
                DefaultTableModel model = (DefaultTableModel) tblPriceDtl.getModel();
                java.util.Vector data = model.getDataVector();
                Iterator itr = data.iterator();
                while (itr.hasNext())
                {
                    java.util.Vector row = (java.util.Vector) itr.next();
                    setMenuNames.add((String) row.get(2));
                    sbBulkUpdate.append("('").append(row.get(0)).append("','").append(row.get(1)).append("','").append(posCode);
                    sbBulkUpdate.append("','").append(mapMenuHead.get(row.get(2))).append("','").append(row.get(3)).append("',").append(row.get(4));
                    sbBulkUpdate.append(",").append(row.get(5)).append(",").append(row.get(6));
                    sbBulkUpdate.append(",").append(row.get(7)).append(",").append(row.get(8));
                    sbBulkUpdate.append(",").append(row.get(9)).append(",").append(row.get(10));
                    sbBulkUpdate.append(",'").append(row.get(11)).append("','").append(row.get(12)).append("','").append(row.get(13));
                    sbBulkUpdate.append("','").append(row.get(14)).append("','").append(row.get(15)).append("','").append(row.get(16));
                    sbBulkUpdate.append("','").append(mapCostCenter.get(row.get(17))).append("','").append(row.get(18));
                    sbBulkUpdate.append("','").append(clsGlobalVarClass.gUserCode).append("','").append(clsGlobalVarClass.gUserCode).append("','").append(clsGlobalVarClass.getCurrentDateTime());
                    sbBulkUpdate.append("','").append(clsGlobalVarClass.getCurrentDateTime()).append("','");
                    String areaCode = mapArea.get(cmbArea.getSelectedItem().toString());
                    sbBulkUpdate.append(areaCode);
                    sbBulkUpdate.append("','").append(mapSubMenuHead.get(row.get(20))).append("',");
                    sbBulkUpdate.append("'").append(row.get(21)).append("',");
                    sbBulkUpdate.append("'").append(clsGlobalVarClass.gClientCode).append("'),");
                }
                sbBulkUpdate.deleteCharAt(sbBulkUpdate.length() - 1);

                clsGlobalVarClass.dbMysql.execute(sbBulkUpdate.toString());
                sb.setLength(0);
                sb.append("delete from tblmenuitempricinghd where strPosCode='");
                sb.append(posCode).append("' and (strMenuCode=");
                boolean first = true;
                for (String menuname : setMenuNames)
                {
                    if (first)
                    {
                        sb.append("'").append(mapMenuHead.get(menuname)).append("' ");
                        first = false;
                    }
                    else
                    {
                        sb.append(" or strMenuCode='").append(mapMenuHead.get(menuname)).append("' ");
                    }
                }

                sb.append(")");
                clsGlobalVarClass.dbMysql.execute(sb.toString());

                sb.setLength(0);
                sb.append("INSERT INTO tblmenuitempricinghd"
                        + "(strPosCode,strMenuCode,strMenuName,strUserCreated,strUserEdited,dteDateCreated,dteDateEdited)"
                        + "VALUES");
                for (String menuname : setMenuNames)
                {
                    sb.append("('").append(posCode).append("','");
                    sb.append(mapMenuHead.get(menuname)).append("','");
                    sb.append(menuname).append("','");
                    sb.append(clsGlobalVarClass.gUserCode).append("','");
                    sb.append(clsGlobalVarClass.gUserCode).append("','");
                    sb.append(clsGlobalVarClass.getCurrentDateTime()).append("','");
                    sb.append(clsGlobalVarClass.getCurrentDateTime()).append("'),");
                }
                sb.deleteCharAt(sb.length() - 1);
                clsGlobalVarClass.dbMysql.execute(sb.toString());
                setMenuNames.clear();

                String sql = "update tblmasteroperationstatus set dteDateEdited='" + clsGlobalVarClass.getCurrentDateTime() + "' "
                        + " where strTableName='MenuItemPricing' and strClientCode='" + clsGlobalVarClass.gClientCode + "'";
                clsGlobalVarClass.dbMysql.execute(sql);
                new frmOkPopUp(this, "Entry added Successfully", "Successfull", 3).setVisible(true);
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
     * This method is used to update Multiple Area data
     */
    private void funUpdateMultipleAreaClick()
    {
        try
        {
            if (tblPriceDtl.getRowCount() == 0)
            {
                JOptionPane.showMessageDialog(this, "Please Perform Execute", "Error", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            if (cmbPosCode.getSelectedIndex() == 0)
            {
                JOptionPane.showMessageDialog(this, "Please Select POS", "Error", JOptionPane.INFORMATION_MESSAGE);
                cmbPosCode.requestFocus();
                return;
            }
            String posCode = mapPosCode.get(cmbPosCode.getSelectedItem().toString());

            int ch = JOptionPane.showConfirmDialog(new JPanel(), "Do you want to Update All Item?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (ch == JOptionPane.YES_OPTION)
            {

                sb.setLength(0);
                sb.append("delete from tblmenuitempricingdtl where strPosCode='");
                sb.append(posCode).append("' ");

                if (cmbMenuHead.getSelectedIndex() > 0)
                {
                    String menuCode = mapMenuHead.get(cmbMenuHead.getSelectedItem().toString());
                    sb.append("and strMenuCode='");
                    sb.append(menuCode).append("' ");
                }

                if (cmbCostCenter.getSelectedIndex() > 0)
                {
                    String costCenterCode = mapCostCenter.get(cmbCostCenter.getSelectedItem().toString());
                    sb.append("and strCostCenterCode='");
                    sb.append(costCenterCode).append("' ");
                }
                String listOfAreaCode = "";

                if (cmbArea.getSelectedIndex() > 0)
                {
//                    String areaCode = mapArea.get(cmbArea.getSelectedItem().toString());
                    System.out.println(setForMultiple);
                    boolean first = true;

                    for (String areaCode : setForMultiple)
                    {
                        if (first)
                        {
                            listOfAreaCode = "'" + areaCode + "'";
                            first = false;
                        }
                        else
                        {
                            listOfAreaCode += ",'" + areaCode + "'";
                        }
                    }

                    sb.append("and strAreaCode in (" + listOfAreaCode + ")");

                }

                sbBulkUpdate.setLength(0);
                sbBulkUpdate.append("INSERT INTO tblmenuitempricingdtl(strItemCode,strItemName,strPosCode,"
                        + "strMenuCode,strPopular,strPriceSunday,strPriceMonday,strPriceTuesday,strPriceWednesday"
                        + ",strPriceThursday,strPriceFriday,strPriceSaturday,dteFromDate,dteToDate,tmeTimeFrom,"
                        + "strAMPMFrom,tmeTimeTo,strAMPMTo,strCostCenterCode,strTextColor,strUserCreated"
                        + ",strUserEdited,dteDateCreated,dteDateEdited,strAreaCode,strSubMenuHeadCode,strHourlyPricing,strClientCode)"
                        + " VALUES ");
                DefaultTableModel model = (DefaultTableModel) tblPriceDtl.getModel();
                java.util.Vector data = model.getDataVector();
                Iterator itr = data.iterator();
                int i = 0;
                while (itr.hasNext())
                {

                    java.util.Vector row = (java.util.Vector) itr.next();
                    setMenuNames.add((String) row.get(2));
                    sbBulkUpdate.append("('").append(row.get(0)).append("','").append(row.get(1)).append("','").append(posCode);
                    sbBulkUpdate.append("','").append(mapMenuHead.get(row.get(2))).append("','").append(row.get(3)).append("',").append(row.get(4));
                    sbBulkUpdate.append(",").append(row.get(5)).append(",").append(row.get(6));
                    sbBulkUpdate.append(",").append(row.get(7)).append(",").append(row.get(8));
                    sbBulkUpdate.append(",").append(row.get(9)).append(",").append(row.get(10));
                    sbBulkUpdate.append(",'").append(row.get(11)).append("','").append(row.get(12)).append("','").append(row.get(13));
                    sbBulkUpdate.append("','").append(row.get(14)).append("','").append(row.get(15)).append("','").append(row.get(16));
                    sbBulkUpdate.append("','").append(mapCostCenter.get(row.get(17))).append("','").append(row.get(18));
                    sbBulkUpdate.append("','").append(clsGlobalVarClass.gUserCode).append("','").append(clsGlobalVarClass.gUserCode).append("','").append(clsGlobalVarClass.getCurrentDateTime());
                    sbBulkUpdate.append("','").append(clsGlobalVarClass.getCurrentDateTime()).append("','");
                    String areaName = row.get(19).toString();
                    String areaCode = mapMultipleArea.get(areaName);
//                    String areaCode = mapMultipleArea.get(cmbArea.getSelectedItem().toString());
                    sbBulkUpdate.append(areaCode);
                    sbBulkUpdate.append("','").append(mapSubMenuHead.get(row.get(20))).append("',");
                    sbBulkUpdate.append("'").append(row.get(21)).append("',");

                    sbBulkUpdate.append("'").append(clsGlobalVarClass.gClientCode).append("'),");
//                  
                }

                clsGlobalVarClass.dbMysql.execute(sb.toString());
                clsGlobalVarClass.dbMysql.execute(sbBulkUpdate.toString().substring(0, sbBulkUpdate.length() - 1));

                sb.setLength(0);
                sb.append("delete from tblmenuitempricinghd where strPosCode='");
                sb.append(posCode).append("' and (strMenuCode=");
                boolean first = true;
                for (String menuname : setMenuNames)
                {
                    if (first)
                    {
                        sb.append("'").append(mapMenuHead.get(menuname)).append("' ");
                        first = false;
                    }
                    else
                    {
                        sb.append(" or strMenuCode='").append(mapMenuHead.get(menuname)).append("' ");
                    }
                }

                sb.append(")");
                clsGlobalVarClass.dbMysql.execute(sb.toString());

                sb.setLength(0);
                sb.append("INSERT INTO tblmenuitempricinghd"
                        + "(strPosCode,strMenuCode,strMenuName,strUserCreated,strUserEdited,dteDateCreated,dteDateEdited)"
                        + "VALUES");
                for (String menuname : setMenuNames)
                {
                    sb.append("('").append(posCode).append("','");
                    sb.append(mapMenuHead.get(menuname)).append("','");
                    sb.append(menuname).append("','");
                    sb.append(clsGlobalVarClass.gUserCode).append("','");
                    sb.append(clsGlobalVarClass.gUserCode).append("','");
                    sb.append(clsGlobalVarClass.getCurrentDateTime()).append("','");
                    sb.append(clsGlobalVarClass.getCurrentDateTime()).append("'),");
                }
                sb.deleteCharAt(sb.length() - 1);
                clsGlobalVarClass.dbMysql.execute(sb.toString());
                setMenuNames.clear();

                String sql = "update tblmasteroperationstatus set dteDateEdited='" + clsGlobalVarClass.getCurrentDateTime() + "' "
                        + " where strTableName='MenuItemPricing' and strClientCode='" + clsGlobalVarClass.gClientCode + "'";
                clsGlobalVarClass.dbMysql.execute(sql);
                new frmOkPopUp(this, "Entry added Successfully", "Successfull", 3).setVisible(true);
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
     * This method is used to export excel file
     */
    private void funExportClick()
    {

        try
        {
            if (cmbPosCode.getSelectedIndex() == 0)
            {
                JOptionPane.showMessageDialog(this, "Please Select POS", "Error", JOptionPane.INFORMATION_MESSAGE);
                cmbPosCode.requestFocus();
                return;
            }
            HSSFWorkbook hwb = new HSSFWorkbook();
            HSSFSheet sheet = hwb.createSheet("new sheet");
            CellStyle style = hwb.createCellStyle();
            HSSFFont font = hwb.createFont();
            font.setFontName("Arial");
            style.setFillForegroundColor(HSSFColor.BLUE.index);
            style.setFillPattern(CellStyle.SOLID_FOREGROUND);
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            font.setColor(HSSFColor.WHITE.index);
            style.setFont(font);
            HSSFRow rowhead = sheet.createRow((short) 0);
            rowhead.createCell((short) 0).setCellValue("Item Code");
            rowhead.getCell(0).setCellStyle(style);
            rowhead.createCell((short) 1).setCellValue("Item Name");
            rowhead.getCell(1).setCellStyle(style);
            rowhead.createCell((short) 2).setCellValue("Menu Code");
            rowhead.getCell(2).setCellStyle(style);
            rowhead.createCell((short) 3).setCellValue("Menu Name");
            rowhead.getCell(3).setCellStyle(style);
            rowhead.createCell((short) 4).setCellValue("Popular");
            rowhead.getCell(4).setCellStyle(style);
            rowhead.createCell((short) 5).setCellValue("PriceMonday");
            rowhead.getCell(5).setCellStyle(style);
            rowhead.createCell((short) 6).setCellValue("PriceTuesday");
            rowhead.getCell(6).setCellStyle(style);
            rowhead.createCell((short) 7).setCellValue("PriceWednesday");
            rowhead.getCell(7).setCellStyle(style);
            rowhead.createCell((short) 8).setCellValue("PriceThursday");
            rowhead.getCell(8).setCellStyle(style);
            rowhead.createCell((short) 9).setCellValue("PriceFriday");
            rowhead.getCell(9).setCellStyle(style);
            rowhead.createCell((short) 10).setCellValue("PriceSaturday");
            rowhead.getCell(10).setCellStyle(style);
            rowhead.createCell((short) 11).setCellValue("PriceSunday");
            rowhead.getCell(11).setCellStyle(style);
            rowhead.createCell((short) 12).setCellValue("FromDate");
            rowhead.getCell(12).setCellStyle(style);
            rowhead.createCell((short) 13).setCellValue("ToDate");
            rowhead.getCell(13).setCellStyle(style);
            rowhead.createCell((short) 14).setCellValue("TimeFrom");
            rowhead.getCell(14).setCellStyle(style);
            rowhead.createCell((short) 15).setCellValue("AMPMFrom");
            rowhead.getCell(15).setCellStyle(style);
            rowhead.createCell((short) 16).setCellValue("TimeTo");
            rowhead.getCell(16).setCellStyle(style);
            rowhead.createCell((short) 17).setCellValue("AMPMTo");
            rowhead.getCell(17).setCellStyle(style);
            rowhead.createCell((short) 18).setCellValue("CostCenterCode");
            rowhead.getCell(18).setCellStyle(style);
            rowhead.createCell((short) 19).setCellValue("CostCenterName");
            rowhead.getCell(19).setCellStyle(style);
            rowhead.createCell((short) 20).setCellValue("TextColor");
            rowhead.getCell(20).setCellStyle(style);
            rowhead.createCell((short) 21).setCellValue("AreaCode");
            rowhead.getCell(21).setCellStyle(style);
            rowhead.createCell((short) 22).setCellValue("AreaName");
            rowhead.getCell(22).setCellStyle(style);
            rowhead.createCell((short) 23).setCellValue("SubMenuHeadCode");
            rowhead.getCell(23).setCellStyle(style);
            rowhead.createCell((short) 24).setCellValue("HourlyPricing");
            rowhead.getCell(24).setCellStyle(style);
            rowhead.createCell((short) 25).setCellValue("Is Expired");
            rowhead.getCell(25).setCellStyle(style);

            rs = clsGlobalVarClass.dbMysql.executeResultSet(funGetExecuteString().toString());
            Integer i = 1;
            while (rs.next())
            {
                HSSFRow row = sheet.createRow(i);
                row.createCell((short) 0).setCellValue(rs.getString(1));
                row.createCell((short) 1).setCellValue(rs.getString(2));
                row.createCell((short) 2).setCellValue(rs.getString(3));
                row.createCell((short) 3).setCellValue(rs.getString(4));
                row.createCell((short) 4).setCellValue(rs.getString(5));
                row.createCell((short) 5).setCellValue(rs.getString(6));
                row.createCell((short) 6).setCellValue(rs.getString(7));
                row.createCell((short) 7).setCellValue(rs.getString(8));
                row.createCell((short) 8).setCellValue(rs.getString(9));
                row.createCell((short) 9).setCellValue(rs.getString(10));
                row.createCell((short) 10).setCellValue(rs.getString(11));
                row.createCell((short) 11).setCellValue(rs.getString(12));
                row.createCell((short) 12).setCellValue(rs.getString(13));
                row.createCell((short) 13).setCellValue(rs.getString(14));
                row.createCell((short) 14).setCellValue(rs.getString(15));
                row.createCell((short) 15).setCellValue(rs.getString(16));
                row.createCell((short) 16).setCellValue(rs.getString(17));
                row.createCell((short) 17).setCellValue(rs.getString(18));
                row.createCell((short) 18).setCellValue(rs.getString(19));
                row.createCell((short) 19).setCellValue(rs.getString(20));
                row.createCell((short) 20).setCellValue(rs.getString(21));
                row.createCell((short) 21).setCellValue(rs.getString(22));
                row.createCell((short) 22).setCellValue(rs.getString(23));
                row.createCell((short) 23).setCellValue(rs.getString(24));
                row.createCell((short) 24).setCellValue(rs.getString(25));
                row.createCell((short) 25).setCellValue(rs.getString(27));
                i++;
            }
            rs.close();
            String filePath = System.getProperty("user.dir");
            File file = new File(filePath + "/MenuItemPricing.xls");
            FileOutputStream fileOut = new FileOutputStream(file);
            hwb.write(fileOut);
            fileOut.close();
            JOptionPane.showMessageDialog(this, "File Created Successfully \n" + filePath + " : " + "MenuItemPricing.xls");
            funResetFields();
            //Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + filePath + "/MenuItemPricing.xls");

            Desktop dt = Desktop.getDesktop();
            dt.open(file);

        }
        catch (FileNotFoundException ex)
        {
            //JOptionPane.showMessageDialog(this, "File is already opened please close ");

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    /**
     * This method is used to exit from form
     */
    private void funExitClick()
    {
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
            java.util.logging.Logger.getLogger(frmBulkMenuItemPricing.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(frmBulkMenuItemPricing.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(frmBulkMenuItemPricing.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(frmBulkMenuItemPricing.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        dispose();
        System.gc();
    }

    /**
     * This method is used to load pos codes
     */
    private void funLoadmapPosCode()
    {
        try
        {
            mapPosCode.clear();
            sb.setLength(0);
            sb.append("select strPosName,strPosCode from tblposmaster;");
            rs = clsGlobalVarClass.dbMysql.executeResultSet(sb.toString());
            mapPosCode.put("--SELECT--", "--SELECT--");
            mapPosCode.put("All", "All");
            while (rs.next())
            {
                mapPosCode.put(rs.getString(1), rs.getString(2));
            }
            rs.close();
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    /**
     * This method is used to load menu heads
     */
    private void funLoadmapMenuHead()
    {
        try
        {
            mapMenuHead.clear();
            sb.setLength(0);
            sb.append("select a.strMenuName,a.strMenuCode from tblmenuhd a, tblmenuitempricingdtl b "
                    + "where a.strMenuCode=b.strMenuCode group by a.StrMenuCode;");
            mapMenuHead.put("--SELECT--", "--SELECT--");
            rs = clsGlobalVarClass.dbMysql.executeResultSet(sb.toString());
            while (rs.next())
            {
                mapMenuHead.put(rs.getString(1), rs.getString(2));
            }
            rs.close();
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    /**
     * This method is used to load sub menu heads
     */
    private void funLoadmapSubMenuHead()
    {
        try
        {
            mapSubMenuHead.clear();
            sb.setLength(0);
            sb.append("select strSubMenuHeadName,strSubMenuHeadCode from tblsubmenuhead;");

            mapSubMenuHead.put("--SELECT--", "--SELECT--");
            rs = clsGlobalVarClass.dbMysql.executeResultSet(sb.toString());
            while (rs.next())
            {
                mapSubMenuHead.put(rs.getString(1), rs.getString(2));

            }
            rs.close();

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    /**
     * This method is used to load cost centers
     */
    private void funLoadmapCostCenter()
    {
        try
        {
            mapCostCenter.clear();
            sb.setLength(0);
            //sb.append("select a.strCostCenterName,a.strCostCenterCode from tblcostcentermaster a,tblmenuitempricingdtl b "
            //    + "where a.strCostCenterCode=b.strCostCenterCode group by a.strCostCenterCode;");

            sb.append("select a.strCostCenterName,a.strCostCenterCode "
                    + "from tblcostcentermaster a");
            mapCostCenter.put("--SELECT--", "--SELECT--");
            rs = clsGlobalVarClass.dbMysql.executeResultSet(sb.toString());
            while (rs.next())
            {
                mapCostCenter.put(rs.getString(1), rs.getString(2));
            }
            rs.close();
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    /**
     * This method is used to load areas
     */
    private void funLoadmapArea()
    {
        try
        {
            int count = 0;
            String areaCode = "";
            mapArea.clear();

            sb.setLength(0);
            sb.append("select a.strAreaName,a.strAreaCode "
                    + " from tblareamaster a left outer join tblmenuitempricingdtl b "
                    + " ON a.strAreaCode=b.strAreaCode group by a.strAreaCode;");

            mapArea.put("--SELECT--", "--SELECT--");

            rs = clsGlobalVarClass.dbMysql.executeResultSet(sb.toString());
            while (rs.next())
            {
                mapArea.put(rs.getString(1), rs.getString(2));

                //  mapMultipleArea.put(rs.getString(1), rs.getString(2));
                sb.setLength(0);

            }

            rs.close();

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    /**
     * This method is used to load pos codes in cobobox
     */
    private void funLoadPOSCombo()
    {
        cmbPosCode.removeAllItems();
        for (String posCode : mapPosCode.keySet())
        {
            cmbPosCode.addItem(posCode);
        }
    }

    private void funLoadExpiredCombo()
    {
        cmbExpiredItem.removeAllItems();

        cmbExpiredItem.addItem("BOTH");
        cmbExpiredItem.addItem("YES");
        cmbExpiredItem.addItem("NO");

    }

    /**
     * This method is used to load menu head combo box
     */
    private void funLoadMenuHeadCombo()
    {
        cmbMenuHead.removeAllItems();
        for (String menuHead : mapMenuHead.keySet())
        {
            cmbMenuHead.addItem(menuHead);
            if (!"--select--".equalsIgnoreCase(menuHead))
            {
                menuHeadComboBox.addItem(menuHead);
            }
        }
    }

    /**
     * This method is used to load sub menu head combo box
     */
    private void funLoadSubMenuHeadCombo()
    {
        subMenuHeadComboBox.removeAllItems();

        for (Entry<String, String> entry : mapSubMenuHead.entrySet())
        {

            if (!"--select--".equalsIgnoreCase(entry.getKey()))
            {
                subMenuHeadComboBox.addItem(entry.getKey());
            }
        }

    }

    /**
     * This method is used to load cost center combo box
     */
    private void funLoadCostCenterCombo()
    {
        cmbCostCenter.removeAllItems();
        for (String costCenter : mapCostCenter.keySet())
        {
            cmbCostCenter.addItem(costCenter);
            if (!"--select--".equalsIgnoreCase(costCenter))
            {
                costCenterComboBox.addItem(costCenter);
            }
        }
    }

    /**
     * This method is used to load area combo box
     */
    private void funLoadAreaCombo()
    {
        cmbArea.removeAllItems();

        for (String area : mapArea.keySet())
        {

            cmbArea.addItem(area);

//          if(i==2)
//            {
//               cmbArea.addItem("Multiple"); 
//            }
            if (!"--select--".equalsIgnoreCase(area))
            {
                areaComboBox.addItem(area);
            }

        }
    }

    /**
     * This method is used to load color combo box
     */
    private void funLoadColorComboBox()
    {
        colorComboBox.removeAllItems();
        colorComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[]
        {
            "Black", "Green", "Red", "BLUE", "CYAN", "ORANGE", "PINK", "YELLOW", "WHITE"
        }));
    }

    /**
     * This method is used to load popular combo box
     */
    private void funLoadPopularComboBox()
    {
        popularComoBox.removeAllItems();
        popularComoBox.setModel(new javax.swing.DefaultComboBoxModel(new String[]
        {
            "Y", "N"
        }));
    }

    /**
     * This method is used to load amPmFrom combo box
     */
    private void funLoadampmFromComboBox()
    {
        amPmFromComoBox.removeAllItems();
        amPmFromComoBox.setModel(new javax.swing.DefaultComboBoxModel(new String[]
        {
            "AM", "PM"
        }));
    }

    /**
     * This method is used to load amPmTo combo box
     */
    private void funLoadampmToComboBox()
    {
        amPmToComoBox.removeAllItems();
        amPmToComoBox.setModel(new javax.swing.DefaultComboBoxModel(new String[]
        {
            "AM", "PM"
        }));
    }

    /**
     * This method is used to set price detail table property
     */
    private void funsetPriceDtlTableProperty()
    {

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(DefaultTableCellRenderer.RIGHT);
        tblPriceDtl.getColumn("PriceSunday").setCellRenderer(rightRenderer);
        tblPriceDtl.getColumn("PriceMonday").setCellRenderer(rightRenderer);
        tblPriceDtl.getColumn("PriceTuesday").setCellRenderer(rightRenderer);
        tblPriceDtl.getColumn("PriceWednesday").setCellRenderer(rightRenderer);
        tblPriceDtl.getColumn("PriceThursday").setCellRenderer(rightRenderer);
        tblPriceDtl.getColumn("PriceFriday").setCellRenderer(rightRenderer);
        tblPriceDtl.getColumn("PriceSaturday").setCellRenderer(rightRenderer);

        TableColumn MenuNameColumn = tblPriceDtl.getColumnModel().getColumn(0);
        MenuNameColumn.setCellEditor(new DefaultCellEditor(menuHeadComboBox));

        TableColumn popularColumn = tblPriceDtl.getColumnModel().getColumn(1);
        popularColumn.setCellEditor(new DefaultCellEditor(popularComoBox));

        TableColumn costCenterColumn = tblPriceDtl.getColumnModel().getColumn(15);
        costCenterColumn.setCellEditor(new DefaultCellEditor(costCenterComboBox));

        TableColumn colorColumn = tblPriceDtl.getColumnModel().getColumn(16);
        colorColumn.setCellEditor(new DefaultCellEditor(colorComboBox));

        TableColumn areaColumn = tblPriceDtl.getColumnModel().getColumn(17);
        areaColumn.setCellEditor(new DefaultCellEditor(areaComboBox));

        TableColumn mondayPrice = tblPriceDtl.getColumnModel().getColumn(2);
        mondayPrice.setCellEditor(new DefaultCellEditor(priceText));

        TableColumn tuesdayPrice = tblPriceDtl.getColumnModel().getColumn(3);
        tuesdayPrice.setCellEditor(new DefaultCellEditor(priceText));

        TableColumn wedPrice = tblPriceDtl.getColumnModel().getColumn(4);
        wedPrice.setCellEditor(new DefaultCellEditor(priceText));

        TableColumn thusPrice = tblPriceDtl.getColumnModel().getColumn(5);
        thusPrice.setCellEditor(new DefaultCellEditor(priceText));

        TableColumn friPrice = tblPriceDtl.getColumnModel().getColumn(6);
        friPrice.setCellEditor(new DefaultCellEditor(priceText));

        TableColumn satPrice = tblPriceDtl.getColumnModel().getColumn(7);
        satPrice.setCellEditor(new DefaultCellEditor(priceText));

        TableColumn sunPrice = tblPriceDtl.getColumnModel().getColumn(8);
        sunPrice.setCellEditor(new DefaultCellEditor(priceText));

        TableColumn tblColAMPMFrom = tblPriceDtl.getColumnModel().getColumn(12);
        tblColAMPMFrom.setCellEditor(new DefaultCellEditor(amPmFromComoBox));

        TableColumn tblColAMPMTo = tblPriceDtl.getColumnModel().getColumn(14);
        tblColAMPMTo.setCellEditor(new DefaultCellEditor(amPmToComoBox));

        TableColumn tblColSubMenuName = tblPriceDtl.getColumnModel().getColumn(18);
        tblColSubMenuName.setCellEditor(new DefaultCellEditor(subMenuHeadComboBox));
    }

    /**
     * This method is used to reset all fields
     */
    private void funResetFields()
    {
        DefaultTableModel dm = (DefaultTableModel) tblPriceDtl.getModel();
        dm.setRowCount(0);
        cmbArea.setSelectedIndex(0);
        cmbPosCode.setSelectedIndex(0);
        cmbMenuHead.setSelectedIndex(0);
        cmbCostCenter.setSelectedIndex(0);
        cmbSortBy.setSelectedIndex(0);
        sb.setLength(0);
        sbBulkUpdate.setLength(0);
        setMenuNames.clear();
        btnChangePrice.setEnabled(false);
        funFreezeComboBox(true);
    }

    /**
     * This method is used to get execute string
     *
     * @return
     */
    private StringBuilder funGetExecuteString()
    {
        try
        {
            String posCode = mapPosCode.get(cmbPosCode.getSelectedItem().toString());
            DefaultTableModel dmImemTable = (DefaultTableModel) tblPriceDtl.getModel();
            dmImemTable.setRowCount(0);
            sb.setLength(0);
            sb.append("SELECT a.strItemCode,b.strItemName,a.strMenuCode,c.strMenuName,a.strPopular,a.strPriceSunday,a.strPriceMonday,a.strPriceTuesday,a.strPriceWednesday, "
                    + "a.strPriceThursday,a.strPriceFriday,a.strPriceSaturday,date(a.dteFromDate),date(a.dteToDate),a.tmeTimeFrom, "
                    + "a.strAMPMFrom,a.tmeTimeTo,a.strAMPMTo,a.strCostCenterCode,d.strCostCenterName,a.strTextColor,a.strAreaCode,f.strAreaName, "
                    + "a.strSubMenuHeadCode,a.strHourlyPricing ,ifNULL(g.strSubMenuHeadName,'ND') as strSubMenuHeadName"
                    + ",if(date('" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "')> DATE(a.dteToDate),'YES','NO')IsExpired "
                    + "FROM tblmenuitempricingdtl a  "
                    + "left outer join tblitemmaster b on b.strItemCode=a.strItemCode  "
                    + "left outer join tblmenuhd c on a.strMenuCode=c.strMenuCode "
                    + "left outer join tblcostcentermaster d on a.strCostCenterCode=d.strCostCenterCode "
                    + "left outer join tblareamaster f on a.strAreaCode=f.strAreaCode  "
                    + "left outer join tblsubmenuhead g on a.strSubMenuHeadCode=g.strSubMenuHeadCode ");
            //conditions append
            sb.append("Where a.strPosCode='").append(posCode).append("' ");
            if (cmbMenuHead.getSelectedIndex() > 0)
            {
                String menuCode = mapMenuHead.get(cmbMenuHead.getSelectedItem().toString());
                sb.append("and a.strMenuCode='");
                sb.append(menuCode).append("' ");
            }

            if (cmbCostCenter.getSelectedIndex() > 0)
            {
                String costCenterCode = mapCostCenter.get(cmbCostCenter.getSelectedItem().toString());
                sb.append("and a.strCostCenterCode='");
                sb.append(costCenterCode).append("' ");
            }

            if (cmbArea.getSelectedIndex() > 0)
            {
                String areaCode = mapArea.get(cmbArea.getSelectedItem().toString());

                sb.append("and a.strAreaCode='");
                sb.append(areaCode).append("' ");

            }

            if (cmbExpiredItem.getSelectedItem().toString().equalsIgnoreCase("YES"))//only expired items
            {
                sb.append(" and DATE(a.dteToDate)<date('" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "') ");//expired
            }
            else if (cmbExpiredItem.getSelectedItem().toString().equalsIgnoreCase("NO"))//only live items
            {
                sb.append(" and DATE(a.dteToDate)>=date('" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "') ");//not expired
            }
            else
            {
                //both
            }

            sb.append(" and c.strMenuName is not null and d.strCostCenterCode is not null and f.strAreaCode is not null ");
            //sb.append(" group by a.strItemCode,a.strHourlyPricing ");
            if (cmbArea.getSelectedItem().toString().equalsIgnoreCase("Multiple"))
            {
                sb.append(",a.strAreaCode");
            }
            int sortBy = cmbSortBy.getSelectedIndex();
            switch (sortBy)
            {
                case 0:
                    sb.append(" order by a.strAreaCode,a.strItemName ");
                    break;
                case 1:
                    sb.append(" order by a.strItemCode ");
                    break;
                case 2:
                    sb.append(" order by a.strItemName ");
                    break;
                case 3:
                    sb.append(" order by a.strMenuCode ");
                    break;
                case 4:
                    sb.append(" order by a.strCostCenterCode ");
                    break;
                case 5:
                    sb.append(" order by a.strAreaCode ");
                    break;
                case 6:
                    sb.append(" order by a.strPosCode ");
                    break;
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }

        System.out.println("sb==== " + sb);

        return sb;
    }

    /**
     * This method is used to freeze combo boxes
     *
     * @param value
     */
    private void funFreezeComboBox(Boolean value)
    {
        cmbMenuHead.setEnabled(value);
        cmbCostCenter.setEnabled(value);
        cmbArea.setEnabled(true);

        btnChangePrice.setEnabled(false);
    }

    /**
     * This method is used to reset
     */
    private void funResetClick()
    {
        funResetFields();
    }

}
