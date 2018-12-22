/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


package com.POSMaster.view;

import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsUtility;
import com.POSGlobal.view.frmAlfaNumericKeyBoard;
import com.POSGlobal.view.frmOkPopUp;
import com.POSGlobal.view.frmSearchFormDialog;
import com.POSMaster.controller.clsPrinterSetup;
import com.POSPrinting.clsTestPrinter;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author sss11
 */
public class frmPrinterSetup extends javax.swing.JFrame
{

    private Map<String, String> hmPOS;
    private clsUtility objUtility;
    private HashMap<String, String> hmArea;
    private HashMap<String, String> hmCostCenter;
    private java.util.Vector vPrinterNames;
    private HashMap<String, clsPrinterSetup> mapPrinterSetup;
    private ArrayList<String> arrListPrinters;

    /**
     * This default constructor is used to initialized area master
     *
     */
    public frmPrinterSetup()
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
	    lblUserCode.setText(clsGlobalVarClass.gUserCode);
	    lblPosName.setText(clsGlobalVarClass.gPOSName);
	    lblModuleName.setText(clsGlobalVarClass.gSelectedModule);
	    java.util.Date dt = new java.util.Date();
	    int day = dt.getDate();
	    int month = dt.getMonth() + 1;
	    int year = dt.getYear() + 1900;
	    String dte = day + "-" + month + "-" + year;
	    lblDate.setText(clsGlobalVarClass.gPOSDateToDisplay);
	    java.util.Date date = new SimpleDateFormat("dd-MM-yyyy").parse(dte);

	    objUtility = new clsUtility();
	    funFillPOSCombo();
	    funFillAreaCombo();
	    funFillCostCenterCombo();

	    vPrinterNames = objUtility.funGetPrinterNames();
	    arrListPrinters = new ArrayList<String>();

	    for (int cntPrinters = 0; cntPrinters < vPrinterNames.size(); cntPrinters++)
	    {
		cmbPrimaryPrinters.addItem(vPrinterNames.elementAt(cntPrinters).toString());
		cmbSecondaryPrinters.addItem(vPrinterNames.elementAt(cntPrinters).toString());

		arrListPrinters.add(vPrinterNames.elementAt(cntPrinters).toString());
	    }

	    mapPrinterSetup = new HashMap<String, clsPrinterSetup>();
	    funFillSavedPrinterSetup();

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
     * This method is used to fill pos name combobox
     *
     * @throws Exception
     * @return
     */
    private void funFillPOSCombo()
    {
	try
	{

	    hmPOS = new HashMap<String, String>();
	    cmbPOS.removeAllItems();

	    hmPOS.put("All", "All");
	    cmbPOS.addItem("All");
	    String sqlPOS = "select strPOSCode,strPOSName from tblposmaster";
	    ResultSet rsPOS = clsGlobalVarClass.dbMysql.executeResultSet(sqlPOS);
	    while (rsPOS.next())
	    {
		hmPOS.put(rsPOS.getString(2), rsPOS.getString(1));
		cmbPOS.addItem(rsPOS.getString(2));
	    }
	    rsPOS.close();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funFillAreaCombo()
    {
	try
	{

	    hmArea = new HashMap<String, String>();
	    cmbArea.removeAllItems();

	    String selectedPOSCode = hmPOS.get(cmbPOS.getSelectedItem().toString());

	    String sqlArea = "select a.strAreaCode,a.strAreaName,a.strPOSCode "
		    + "from tblareamaster a "
		    + "where (a.strPOSCode='All' or a.strPOSCode='" + selectedPOSCode + "') ";
	    ResultSet rsArea = clsGlobalVarClass.dbMysql.executeResultSet(sqlArea);
	    while (rsArea.next())
	    {
		hmArea.put(rsArea.getString(2), rsArea.getString(1));
		cmbArea.addItem(rsArea.getString(2));
	    }
	    rsArea.close();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funFillCostCenterCombo()
    {
	try
	{

	    hmCostCenter = new HashMap<String, String>();
	    cmbCostCenter.removeAllItems();

	    String selectedPOSCode = hmPOS.get(cmbPOS.getSelectedItem().toString());

	    String sqlCostCenter = "select a.strCostCenterCode,a.strCostCenterName "
		    + "from tblcostcentermaster a ";
	    ResultSet rsCostCenter = clsGlobalVarClass.dbMysql.executeResultSet(sqlCostCenter);
	    while (rsCostCenter.next())
	    {
		hmCostCenter.put(rsCostCenter.getString(2), rsCostCenter.getString(1));
		cmbCostCenter.addItem(rsCostCenter.getString(2));
	    }
	    rsCostCenter.close();
	}
	catch (Exception e)
	{
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
	    funFillPOSCombo();

	}
	catch (Exception e)
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
        };  
        ;
        panelBody = new javax.swing.JPanel();
        btnCancel = new javax.swing.JButton();
        btnNew = new javax.swing.JButton();
        lblFormName = new javax.swing.JLabel();
        btnReset = new javax.swing.JButton();
        lblPOSName = new javax.swing.JLabel();
        cmbPOS = new javax.swing.JComboBox();
        cmbArea = new javax.swing.JComboBox();
        lblArea = new javax.swing.JLabel();
        lblCosteCenter = new javax.swing.JLabel();
        cmbCostCenter = new javax.swing.JComboBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblCostCenterPrinterSetup = new javax.swing.JTable();
        btnAdd = new javax.swing.JButton();
        btnRemove = new javax.swing.JButton();
        lblPrinterPort = new javax.swing.JLabel();
        cmbPrimaryPrinters = new javax.swing.JComboBox();
        txtPrimaryPrinterName = new javax.swing.JTextField();
        btnTestPrinter1 = new javax.swing.JButton();
        lblPrinterPort1 = new javax.swing.JLabel();
        cmbSecondaryPrinters = new javax.swing.JComboBox();
        txtSecondaryPrinterName = new javax.swing.JTextField();
        btnTestPrinter2 = new javax.swing.JButton();
        lblConsolidatedKOTPrinterPort = new javax.swing.JLabel();
        cmbConsolidatedKOTPrinterPort = new javax.swing.JComboBox();
        txtConsolidatedKOTPrinterPort = new javax.swing.JTextField();
        btnTestConsolidatedKOTPrinterPort = new javax.swing.JButton();

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
        lblProductName.setText("SPOS- ");
        panelHeader.add(lblProductName);

        lblModuleName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblModuleName.setForeground(new java.awt.Color(255, 255, 255));
        panelHeader.add(lblModuleName);

        lblformName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblformName.setForeground(new java.awt.Color(255, 255, 255));
        lblformName.setText("-Printer Setup");
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

        btnCancel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnCancel.setForeground(new java.awt.Color(255, 255, 255));
        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnCancel.setText("CLOSE");
        btnCancel.setToolTipText("Close Area Master");
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

        btnNew.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnNew.setForeground(new java.awt.Color(255, 255, 255));
        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnNew.setText("SAVE");
        btnNew.setToolTipText("Save Area Master");
        btnNew.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNew.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnNew.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnNewMouseClicked(evt);
            }
        });

        lblFormName.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        lblFormName.setForeground(new java.awt.Color(14, 7, 7));
        lblFormName.setText("PRINTER SETUP");

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

        lblPOSName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblPOSName.setText("POS  :");

        cmbPOS.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbPOSActionPerformed(evt);
            }
        });
        cmbPOS.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbPOSKeyPressed(evt);
            }
        });

        cmbArea.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbAreaKeyPressed(evt);
            }
        });

        lblArea.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblArea.setText("AREA :");

        lblCosteCenter.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblCosteCenter.setText("COST CENTER :");

        cmbCostCenter.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbCostCenterKeyPressed(evt);
            }
        });

        tblCostCenterPrinterSetup.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "POS", "AREA", "COST CENTER", "PRIMARY PRINTER", "SECONDARY PRINTER", "PRINT ON BOTH PRINTERS"
            }
        )
        {
            Class[] types = new Class []
            {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean []
            {
                false, false, false, true, true, true
            };

            public Class getColumnClass(int columnIndex)
            {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return canEdit [columnIndex];
            }
        });
        tblCostCenterPrinterSetup.setRowHeight(35);
        tblCostCenterPrinterSetup.getTableHeader().setReorderingAllowed(false);
        tblCostCenterPrinterSetup.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseReleased(java.awt.event.MouseEvent evt)
            {
                tblCostCenterPrinterSetupMouseReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tblCostCenterPrinterSetup);
        if (tblCostCenterPrinterSetup.getColumnModel().getColumnCount() > 0)
        {
            tblCostCenterPrinterSetup.getColumnModel().getColumn(0).setResizable(false);
            tblCostCenterPrinterSetup.getColumnModel().getColumn(1).setResizable(false);
            tblCostCenterPrinterSetup.getColumnModel().getColumn(2).setResizable(false);
            tblCostCenterPrinterSetup.getColumnModel().getColumn(3).setResizable(false);
            tblCostCenterPrinterSetup.getColumnModel().getColumn(3).setPreferredWidth(120);
            tblCostCenterPrinterSetup.getColumnModel().getColumn(4).setResizable(false);
            tblCostCenterPrinterSetup.getColumnModel().getColumn(4).setPreferredWidth(120);
            tblCostCenterPrinterSetup.getColumnModel().getColumn(5).setResizable(false);
            tblCostCenterPrinterSetup.getColumnModel().getColumn(5).setPreferredWidth(135);
        }

        btnAdd.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnAdd.setForeground(new java.awt.Color(255, 255, 255));
        btnAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgCmnBtn1.png"))); // NOI18N
        btnAdd.setText("Add");
        btnAdd.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnAdd.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgCmnBtn2.png"))); // NOI18N
        btnAdd.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnAddMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt)
            {
                btnAddMouseEntered(evt);
            }
        });

        btnRemove.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnRemove.setForeground(new java.awt.Color(255, 255, 255));
        btnRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgCmnBtn1.png"))); // NOI18N
        btnRemove.setText("Remove");
        btnRemove.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRemove.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgCmnBtn2.png"))); // NOI18N
        btnRemove.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnRemoveMouseClicked(evt);
            }
        });

        lblPrinterPort.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblPrinterPort.setText("Primary Printer     :");

        cmbPrimaryPrinters.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbPrimaryPrintersActionPerformed(evt);
            }
        });
        cmbPrimaryPrinters.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbPrimaryPrintersKeyPressed(evt);
            }
        });

        txtPrimaryPrinterName.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtPrimaryPrinterNameMouseClicked(evt);
            }
        });
        txtPrimaryPrinterName.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtPrimaryPrinterNameActionPerformed(evt);
            }
        });
        txtPrimaryPrinterName.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtPrimaryPrinterNameKeyPressed(evt);
            }
        });

        btnTestPrinter1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnTestPrinter1.setForeground(new java.awt.Color(255, 255, 255));
        btnTestPrinter1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnTestPrinter1.setText("TEST");
        btnTestPrinter1.setToolTipText("Save Cost Center Master");
        btnTestPrinter1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTestPrinter1.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnTestPrinter1.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnTestPrinter1MouseClicked(evt);
            }
        });
        btnTestPrinter1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnTestPrinter1ActionPerformed(evt);
            }
        });
        btnTestPrinter1.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                btnTestPrinter1KeyPressed(evt);
            }
        });

        lblPrinterPort1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblPrinterPort1.setText("Secondary Printer :");

        cmbSecondaryPrinters.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbSecondaryPrintersActionPerformed(evt);
            }
        });
        cmbSecondaryPrinters.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbSecondaryPrintersKeyPressed(evt);
            }
        });

        txtSecondaryPrinterName.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtSecondaryPrinterNameMouseClicked(evt);
            }
        });
        txtSecondaryPrinterName.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtSecondaryPrinterNameKeyPressed(evt);
            }
        });

        btnTestPrinter2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnTestPrinter2.setForeground(new java.awt.Color(255, 255, 255));
        btnTestPrinter2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnTestPrinter2.setText("TEST");
        btnTestPrinter2.setToolTipText("Save Cost Center Master");
        btnTestPrinter2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTestPrinter2.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnTestPrinter2.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnTestPrinter2MouseClicked(evt);
            }
        });
        btnTestPrinter2.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnTestPrinter2ActionPerformed(evt);
            }
        });
        btnTestPrinter2.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                btnTestPrinter2KeyPressed(evt);
            }
        });

        lblConsolidatedKOTPrinterPort.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblConsolidatedKOTPrinterPort.setText("Consolidated KOT Printer :");

        cmbConsolidatedKOTPrinterPort.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbConsolidatedKOTPrinterPortActionPerformed(evt);
            }
        });
        cmbConsolidatedKOTPrinterPort.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbConsolidatedKOTPrinterPortKeyPressed(evt);
            }
        });

        txtConsolidatedKOTPrinterPort.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtConsolidatedKOTPrinterPortMouseClicked(evt);
            }
        });
        txtConsolidatedKOTPrinterPort.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtConsolidatedKOTPrinterPortActionPerformed(evt);
            }
        });
        txtConsolidatedKOTPrinterPort.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtConsolidatedKOTPrinterPortKeyPressed(evt);
            }
        });

        btnTestConsolidatedKOTPrinterPort.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnTestConsolidatedKOTPrinterPort.setForeground(new java.awt.Color(255, 255, 255));
        btnTestConsolidatedKOTPrinterPort.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgCmnBtn1.png"))); // NOI18N
        btnTestConsolidatedKOTPrinterPort.setText("TEST");
        btnTestConsolidatedKOTPrinterPort.setToolTipText("Save Cost Center Master");
        btnTestConsolidatedKOTPrinterPort.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTestConsolidatedKOTPrinterPort.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgCmnBtn2.png"))); // NOI18N
        btnTestConsolidatedKOTPrinterPort.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnTestConsolidatedKOTPrinterPortMouseClicked(evt);
            }
        });
        btnTestConsolidatedKOTPrinterPort.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnTestConsolidatedKOTPrinterPortActionPerformed(evt);
            }
        });
        btnTestConsolidatedKOTPrinterPort.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                btnTestConsolidatedKOTPrinterPortKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout panelBodyLayout = new javax.swing.GroupLayout(panelBody);
        panelBody.setLayout(panelBodyLayout);
        panelBodyLayout.setHorizontalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBodyLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBodyLayout.createSequentialGroup()
                        .addComponent(lblFormName, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(252, 252, 252))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBodyLayout.createSequentialGroup()
                        .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(panelBodyLayout.createSequentialGroup()
                                .addComponent(lblConsolidatedKOTPrinterPort, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmbConsolidatedKOTPrinterPort, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtConsolidatedKOTPrinterPort, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnTestConsolidatedKOTPrinterPort, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnRemove, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelBodyLayout.createSequentialGroup()
                                    .addComponent(lblPrinterPort)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(cmbPrimaryPrinters, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(3, 3, 3))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelBodyLayout.createSequentialGroup()
                                    .addComponent(lblPOSName)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(cmbPOS, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(lblArea)))
                            .addGroup(panelBodyLayout.createSequentialGroup()
                                .addComponent(lblPrinterPort1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmbSecondaryPrinters, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(3, 3, 3)))
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelBodyLayout.createSequentialGroup()
                                .addComponent(cmbArea, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(lblCosteCenter)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmbCostCenter, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelBodyLayout.createSequentialGroup()
                                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelBodyLayout.createSequentialGroup()
                                        .addComponent(txtSecondaryPrinterName, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(panelBodyLayout.createSequentialGroup()
                                        .addComponent(txtPrimaryPrinterName, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(5, 5, 5)))
                                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnTestPrinter1, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnTestPrinter2, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(299, 299, 299)))))
                .addContainerGap())
            .addComponent(jScrollPane1)
        );
        panelBodyLayout.setVerticalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblFormName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPOSName, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbPOS, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblArea, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbArea, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCosteCenter, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbCostCenter, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPrinterPort, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbPrimaryPrinters, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPrimaryPrinterName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTestPrinter1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbSecondaryPrinters, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPrinterPort1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSecondaryPrinterName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTestPrinter2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRemove, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblConsolidatedKOTPrinterPort, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbConsolidatedKOTPrinterPort, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtConsolidatedKOTPrinterPort, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTestConsolidatedKOTPrinterPort, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        panelLayout.add(panelBody, new java.awt.GridBagConstraints());

        getContentPane().add(panelLayout, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseClicked
	// TODO add your handling code here:
	dispose();
	clsGlobalVarClass.hmActiveForms.remove("Printer Setup");
    }//GEN-LAST:event_btnCancelMouseClicked

    private void btnResetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnResetMouseClicked
	funResetFields();
    }//GEN-LAST:event_btnResetMouseClicked

    private void cmbPOSKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbPOSKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    btnNew.requestFocus();
	}
    }//GEN-LAST:event_cmbPOSKeyPressed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
	// TODO add your handling code here:
	funResetFields();
    }//GEN-LAST:event_btnResetActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
	// TODO add your handling code here:
	dispose();
	clsGlobalVarClass.hmActiveForms.remove("Printer Setup");
    }//GEN-LAST:event_btnCancelActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
	// TODO add your handling code here:
	clsGlobalVarClass.hmActiveForms.remove("Printer Setup");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
	// TODO add your handling code here:
	clsGlobalVarClass.hmActiveForms.remove("Printer Setup");
    }//GEN-LAST:event_formWindowClosing

    private void btnNewMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnNewMouseClicked
    {//GEN-HEADEREND:event_btnNewMouseClicked
	funSaveButtonClicked();
    }//GEN-LAST:event_btnNewMouseClicked

    private void cmbAreaKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_cmbAreaKeyPressed
    {//GEN-HEADEREND:event_cmbAreaKeyPressed
	// TODO add your handling code here:
    }//GEN-LAST:event_cmbAreaKeyPressed

    private void cmbCostCenterKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_cmbCostCenterKeyPressed
    {//GEN-HEADEREND:event_cmbCostCenterKeyPressed
	// TODO add your handling code here:
    }//GEN-LAST:event_cmbCostCenterKeyPressed

    private void btnAddMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnAddMouseClicked
    {//GEN-HEADEREND:event_btnAddMouseClicked
	funAddButtonClicked();
    }//GEN-LAST:event_btnAddMouseClicked

    private void btnAddMouseEntered(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnAddMouseEntered
    {//GEN-HEADEREND:event_btnAddMouseEntered
	// TODO add your handling code here:
    }//GEN-LAST:event_btnAddMouseEntered

    private void btnRemoveMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnRemoveMouseClicked
    {//GEN-HEADEREND:event_btnRemoveMouseClicked
	if (tblCostCenterPrinterSetup.getRowCount() > 0)
	{
	    funRemoveButtonClicked();
	}
	else
	{
	    new frmOkPopUp(this, "No data found.", "Error", 0).setVisible(true);
	    return;
	}
    }//GEN-LAST:event_btnRemoveMouseClicked

    private void cmbPrimaryPrintersActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbPrimaryPrintersActionPerformed
    {//GEN-HEADEREND:event_cmbPrimaryPrintersActionPerformed
	// 31-03-2015
	if (!cmbPrimaryPrinters.getSelectedItem().toString().isEmpty())
	{
	    txtPrimaryPrinterName.setText(cmbPrimaryPrinters.getSelectedItem().toString());
	    btnTestPrinter1.setVisible(true);
	}
	else
	{
	    if (!txtPrimaryPrinterName.getText().isEmpty())
	    {
		btnTestPrinter1.setVisible(true);
	    }
	    else
	    {
		btnTestPrinter1.setVisible(false);
	    }

	}
    }//GEN-LAST:event_cmbPrimaryPrintersActionPerformed

    private void cmbPrimaryPrintersKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_cmbPrimaryPrintersKeyPressed
    {//GEN-HEADEREND:event_cmbPrimaryPrintersKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    cmbSecondaryPrinters.requestFocus();
	}
    }//GEN-LAST:event_cmbPrimaryPrintersKeyPressed

    private void txtPrimaryPrinterNameMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtPrimaryPrinterNameMouseClicked
    {//GEN-HEADEREND:event_txtPrimaryPrinterNameMouseClicked
	try
	{
	    if (txtPrimaryPrinterName.getText().length() == 0)
	    {
		new frmAlfaNumericKeyBoard(this, true, "1", "Please Enter Primary Printer Name.").setVisible(true);
		txtPrimaryPrinterName.setText(clsGlobalVarClass.gKeyboardValue);
	    }
	    else
	    {
		new frmAlfaNumericKeyBoard(this, true, txtPrimaryPrinterName.getText(), "1", "Please Enter Primary Printer Name.").setVisible(true);
		txtPrimaryPrinterName.setText(clsGlobalVarClass.gKeyboardValue);
	    }

	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }//GEN-LAST:event_txtPrimaryPrinterNameMouseClicked

    private void txtPrimaryPrinterNameActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtPrimaryPrinterNameActionPerformed
    {//GEN-HEADEREND:event_txtPrimaryPrinterNameActionPerformed

    }//GEN-LAST:event_txtPrimaryPrinterNameActionPerformed

    private void txtPrimaryPrinterNameKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtPrimaryPrinterNameKeyPressed
    {//GEN-HEADEREND:event_txtPrimaryPrinterNameKeyPressed
	// TODO add your handling code here:
    }//GEN-LAST:event_txtPrimaryPrinterNameKeyPressed

    private void btnTestPrinter1MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnTestPrinter1MouseClicked
    {//GEN-HEADEREND:event_btnTestPrinter1MouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_btnTestPrinter1MouseClicked

    private void btnTestPrinter1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnTestPrinter1ActionPerformed
    {//GEN-HEADEREND:event_btnTestPrinter1ActionPerformed

	funPrimaryTestPrintButtonClicked();
    }//GEN-LAST:event_btnTestPrinter1ActionPerformed

    private void btnTestPrinter1KeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_btnTestPrinter1KeyPressed
    {//GEN-HEADEREND:event_btnTestPrinter1KeyPressed
	// TODO add your handling code here:
    }//GEN-LAST:event_btnTestPrinter1KeyPressed

    private void cmbSecondaryPrintersActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbSecondaryPrintersActionPerformed
    {//GEN-HEADEREND:event_cmbSecondaryPrintersActionPerformed
	// TODO add your handling code here:
	if (!cmbSecondaryPrinters.getSelectedItem().toString().isEmpty())
	{
	    txtSecondaryPrinterName.setText(cmbSecondaryPrinters.getSelectedItem().toString());
	    btnTestPrinter2.setVisible(true);
	}
	else
	{
	    if (!txtSecondaryPrinterName.getText().isEmpty())
	    {
		btnTestPrinter2.setVisible(true);
	    }
	    else
	    {
		btnTestPrinter2.setVisible(false);
	    }
	}
    }//GEN-LAST:event_cmbSecondaryPrintersActionPerformed

    private void cmbSecondaryPrintersKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_cmbSecondaryPrintersKeyPressed
    {//GEN-HEADEREND:event_cmbSecondaryPrintersKeyPressed
	// TODO add your handling code here:
	if (evt.getKeyCode() == 10)
	{
	    btnNew.requestFocus();
	}
    }//GEN-LAST:event_cmbSecondaryPrintersKeyPressed

    private void txtSecondaryPrinterNameMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtSecondaryPrinterNameMouseClicked
    {//GEN-HEADEREND:event_txtSecondaryPrinterNameMouseClicked
	try
	{
	    if (txtSecondaryPrinterName.getText().length() == 0)
	    {
		new frmAlfaNumericKeyBoard(this, true, "1", "Please Enter Secondary Printer Name.").setVisible(true);
		txtSecondaryPrinterName.setText(clsGlobalVarClass.gKeyboardValue);
	    }
	    else
	    {
		new frmAlfaNumericKeyBoard(this, true, txtSecondaryPrinterName.getText(), "1", "Please Enter Secondary Printer Name.").setVisible(true);
		txtSecondaryPrinterName.setText(clsGlobalVarClass.gKeyboardValue);
	    }

	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }//GEN-LAST:event_txtSecondaryPrinterNameMouseClicked

    private void txtSecondaryPrinterNameKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtSecondaryPrinterNameKeyPressed
    {//GEN-HEADEREND:event_txtSecondaryPrinterNameKeyPressed
	// TODO add your handling code here:
    }//GEN-LAST:event_txtSecondaryPrinterNameKeyPressed

    private void btnTestPrinter2MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnTestPrinter2MouseClicked
    {//GEN-HEADEREND:event_btnTestPrinter2MouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_btnTestPrinter2MouseClicked

    private void btnTestPrinter2ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnTestPrinter2ActionPerformed
    {//GEN-HEADEREND:event_btnTestPrinter2ActionPerformed
	funSecondaryTestPrintButtonClicked();
    }//GEN-LAST:event_btnTestPrinter2ActionPerformed

    private void btnTestPrinter2KeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_btnTestPrinter2KeyPressed
    {//GEN-HEADEREND:event_btnTestPrinter2KeyPressed
	// TODO add your handling code here:
    }//GEN-LAST:event_btnTestPrinter2KeyPressed

    private void cmbConsolidatedKOTPrinterPortActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbConsolidatedKOTPrinterPortActionPerformed
    {//GEN-HEADEREND:event_cmbConsolidatedKOTPrinterPortActionPerformed
	// 31-03-2015
	if (!cmbConsolidatedKOTPrinterPort.getSelectedItem().toString().isEmpty())
	{
	    txtConsolidatedKOTPrinterPort.setText(cmbConsolidatedKOTPrinterPort.getSelectedItem().toString());
	    btnTestConsolidatedKOTPrinterPort.setVisible(true);
	}
	else
	{
	    if (!txtConsolidatedKOTPrinterPort.getText().isEmpty())
	    {
		btnTestConsolidatedKOTPrinterPort.setVisible(true);
	    }
	    else
	    {
		btnTestConsolidatedKOTPrinterPort.setVisible(false);
	    }

	}
    }//GEN-LAST:event_cmbConsolidatedKOTPrinterPortActionPerformed

    private void cmbConsolidatedKOTPrinterPortKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_cmbConsolidatedKOTPrinterPortKeyPressed
    {//GEN-HEADEREND:event_cmbConsolidatedKOTPrinterPortKeyPressed

    }//GEN-LAST:event_cmbConsolidatedKOTPrinterPortKeyPressed

    private void txtConsolidatedKOTPrinterPortMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtConsolidatedKOTPrinterPortMouseClicked
    {//GEN-HEADEREND:event_txtConsolidatedKOTPrinterPortMouseClicked
	try
	{
	    if (txtConsolidatedKOTPrinterPort.getText().length() == 0)
	    {
		new frmAlfaNumericKeyBoard(this, true, "1", "Please Enter Printer Name.").setVisible(true);
		txtConsolidatedKOTPrinterPort.setText(clsGlobalVarClass.gKeyboardValue);
	    }
	    else
	    {
		new frmAlfaNumericKeyBoard(this, true, txtConsolidatedKOTPrinterPort.getText(), "1", "Please Enter Printer Name.").setVisible(true);
		txtConsolidatedKOTPrinterPort.setText(clsGlobalVarClass.gKeyboardValue);
	    }

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }//GEN-LAST:event_txtConsolidatedKOTPrinterPortMouseClicked

    private void txtConsolidatedKOTPrinterPortActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtConsolidatedKOTPrinterPortActionPerformed
    {//GEN-HEADEREND:event_txtConsolidatedKOTPrinterPortActionPerformed

    }//GEN-LAST:event_txtConsolidatedKOTPrinterPortActionPerformed

    private void txtConsolidatedKOTPrinterPortKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtConsolidatedKOTPrinterPortKeyPressed
    {//GEN-HEADEREND:event_txtConsolidatedKOTPrinterPortKeyPressed
	// TODO add your handling code here:
    }//GEN-LAST:event_txtConsolidatedKOTPrinterPortKeyPressed

    private void btnTestConsolidatedKOTPrinterPortMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnTestConsolidatedKOTPrinterPortMouseClicked
    {//GEN-HEADEREND:event_btnTestConsolidatedKOTPrinterPortMouseClicked

	funConsolidatedKOTTestPrintButtonClicked();
    }//GEN-LAST:event_btnTestConsolidatedKOTPrinterPortMouseClicked

    private void btnTestConsolidatedKOTPrinterPortActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnTestConsolidatedKOTPrinterPortActionPerformed
    {//GEN-HEADEREND:event_btnTestConsolidatedKOTPrinterPortActionPerformed

    }//GEN-LAST:event_btnTestConsolidatedKOTPrinterPortActionPerformed

    private void btnTestConsolidatedKOTPrinterPortKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_btnTestConsolidatedKOTPrinterPortKeyPressed
    {//GEN-HEADEREND:event_btnTestConsolidatedKOTPrinterPortKeyPressed
	// TODO add your handling code here:
    }//GEN-LAST:event_btnTestConsolidatedKOTPrinterPortKeyPressed

    private void cmbPOSActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbPOSActionPerformed
    {//GEN-HEADEREND:event_cmbPOSActionPerformed
	funFillAreaCombo();
    }//GEN-LAST:event_cmbPOSActionPerformed

    private void tblCostCenterPrinterSetupMouseReleased(java.awt.event.MouseEvent evt)//GEN-FIRST:event_tblCostCenterPrinterSetupMouseReleased
    {//GEN-HEADEREND:event_tblCostCenterPrinterSetupMouseReleased
	System.out.println("Click Count=" + evt.getClickCount());
	if (evt.getClickCount() > 1)
	{
	    int row = tblCostCenterPrinterSetup.getSelectedRow();
	    int col = tblCostCenterPrinterSetup.getSelectedColumn();
	    if (col == 3 || col == 4)
	    {
		funGetThePrinterHelp(row, col);
	    }
	}

    }//GEN-LAST:event_tblCostCenterPrinterSetupMouseReleased

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
	    java.util.logging.Logger.getLogger(frmPrinterSetup.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	}
	catch (InstantiationException ex)
	{
	    java.util.logging.Logger.getLogger(frmPrinterSetup.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	}
	catch (IllegalAccessException ex)
	{
	    java.util.logging.Logger.getLogger(frmPrinterSetup.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	}
	catch (javax.swing.UnsupportedLookAndFeelException ex)
	{
	    java.util.logging.Logger.getLogger(frmPrinterSetup.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	}
	//</editor-fold>
	//</editor-fold>
	//</editor-fold>
	//</editor-fold>

	/* Create and display the form */
	java.awt.EventQueue.invokeLater(new Runnable()
	{
	    public void run()
	    {
		new frmPrinterSetup().setVisible(true);
	    }
	});
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnRemove;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnTestConsolidatedKOTPrinterPort;
    private javax.swing.JButton btnTestPrinter1;
    private javax.swing.JButton btnTestPrinter2;
    private javax.swing.JComboBox cmbArea;
    private javax.swing.JComboBox cmbConsolidatedKOTPrinterPort;
    private javax.swing.JComboBox cmbCostCenter;
    private javax.swing.JComboBox cmbPOS;
    private javax.swing.JComboBox cmbPrimaryPrinters;
    private javax.swing.JComboBox cmbSecondaryPrinters;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblArea;
    private javax.swing.JLabel lblConsolidatedKOTPrinterPort;
    private javax.swing.JLabel lblCosteCenter;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblFormName;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblPOSName;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblPrinterPort;
    private javax.swing.JLabel lblPrinterPort1;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblformName;
    private javax.swing.JPanel panelBody;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelLayout;
    private javax.swing.JTable tblCostCenterPrinterSetup;
    private javax.swing.JTextField txtConsolidatedKOTPrinterPort;
    private javax.swing.JTextField txtPrimaryPrinterName;
    private javax.swing.JTextField txtSecondaryPrinterName;
    // End of variables declaration//GEN-END:variables

    private void funPrimaryTestPrintButtonClicked()
    {
	String printerType = "primary";
	String costCenterName = cmbCostCenter.getSelectedItem().toString();
	String printerName = txtPrimaryPrinterName.getText();

	clsTestPrinter objTestPrinter = new clsTestPrinter();
	objTestPrinter.funTestPrint(printerType, costCenterName, printerName);
    }

    private void funSecondaryTestPrintButtonClicked()
    {
	String printerType = "secondary";
	String costCenterName = cmbCostCenter.getSelectedItem().toString();
	String printerName = txtPrimaryPrinterName.getText();

	clsTestPrinter objTestPrinter = new clsTestPrinter();
	objTestPrinter.funTestPrint(printerType, costCenterName, printerName);
    }

    private void funConsolidatedKOTTestPrintButtonClicked()
    {
	String consolidatedKOTPrinter = txtConsolidatedKOTPrinterPort.getText().trim();

	clsTestPrinter objTestPrinter = new clsTestPrinter();
	objTestPrinter.funTestPrint(consolidatedKOTPrinter);

    }

    private void funFillSavedPrinterSetup()
    {
	try
	{

	    String sql = "select a.strPOSCode,if(a.strPOSCode='All','All',b.strPosName)strPosName "
		    + ",a.strAreaCode,c.strAreaName,a.strCostCenterCode,d.strCostCenterName "
		    + ",a.strPrimaryPrinterPort,a.strSecondaryPrinterPort,a.strPrintOnBothPrintersYN "
		    + "from tblprintersetupmaster a,tblposmaster b,tblareamaster c,tblcostcentermaster d "
		    + "where (a.strPOSCode=b.strPosCode or a.strPOSCode='All') "
		    + "and a.strAreaCode=c.strAreaCode "
		    + "and a.strCostCenterCode=d.strCostCenterCode "
		    + "and a.strPrinterType='Cost Center' "
		    + " ";
	    ResultSet rsSavedPrinterSetup = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rsSavedPrinterSetup.next())
	    {
		String posCode = rsSavedPrinterSetup.getString(1);
		String posName = rsSavedPrinterSetup.getString(2);

		String areaCode = rsSavedPrinterSetup.getString(3);
		String areaName = rsSavedPrinterSetup.getString(4);

		String costCenterCode = rsSavedPrinterSetup.getString(5);
		String costCenterName = rsSavedPrinterSetup.getString(6);

		String key = posName + "!" + areaName + "!" + costCenterName;

		String primaryPrinter = rsSavedPrinterSetup.getString(7);
		String secondaryPrinter = rsSavedPrinterSetup.getString(8);
		String printOnBothPrinters = rsSavedPrinterSetup.getString(9);
		boolean isPrintOnBothPrinters = false;
		if (printOnBothPrinters.equalsIgnoreCase("Y"))
		{
		    isPrintOnBothPrinters = true;
		}

		clsPrinterSetup objPrinterSetup = new clsPrinterSetup();
		objPrinterSetup.setStrPOSCode(posCode);
		objPrinterSetup.setStrPOSName(posName);
		objPrinterSetup.setStrAreaCode(areaCode);
		objPrinterSetup.setStrAreaName(areaName);
		objPrinterSetup.setStrCostCenterCode(costCenterCode);
		objPrinterSetup.setStrCostCenterName(costCenterName);
		objPrinterSetup.setStrPrimaryPrinter(primaryPrinter);
		objPrinterSetup.setStrSecondaryPrinter(secondaryPrinter);
		objPrinterSetup.setStrPrintOnBothPrinters(printOnBothPrinters);

		mapPrinterSetup.put(key, objPrinterSetup);

		DefaultTableModel defaultTableModel = (DefaultTableModel) tblCostCenterPrinterSetup.getModel();

		Object[] row =
		{
		    posName, areaName, costCenterName, primaryPrinter, secondaryPrinter, isPrintOnBothPrinters
		};

		defaultTableModel.addRow(row);
	    }
	    rsSavedPrinterSetup.close();

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funAddButtonClicked()
    {
	String posName = cmbPOS.getSelectedItem().toString();
	String posCode = hmPOS.get(posName);

	String areaName = cmbArea.getSelectedItem().toString();
	String areaCode = hmArea.get(areaName);

	String costCenterName = cmbCostCenter.getSelectedItem().toString();
	String costCenterCode = hmCostCenter.get(costCenterName);

	String key = posName + "!" + areaName + "!" + costCenterName;

	String primaryPrinter = txtPrimaryPrinterName.getText();
	String secondaryPrinter = txtSecondaryPrinterName.getText();

	if (mapPrinterSetup.containsKey(key))
	{
	    new frmOkPopUp(this, "Already set the printer.", "Error", 0).setVisible(true);
	    return;
	}
	else
	{
	    clsPrinterSetup objPrinterSetup = new clsPrinterSetup();
	    objPrinterSetup.setStrPOSCode(posCode);
	    objPrinterSetup.setStrPOSName(posName);
	    objPrinterSetup.setStrAreaCode(areaCode);
	    objPrinterSetup.setStrAreaName(areaName);
	    objPrinterSetup.setStrCostCenterCode(costCenterCode);
	    objPrinterSetup.setStrCostCenterName(costCenterName);
	    objPrinterSetup.setStrPrimaryPrinter(primaryPrinter);
	    objPrinterSetup.setStrSecondaryPrinter(secondaryPrinter);
	    objPrinterSetup.setStrPrintOnBothPrinters("N");

	    mapPrinterSetup.put(key, objPrinterSetup);

	    DefaultTableModel defaultTableModel = (DefaultTableModel) tblCostCenterPrinterSetup.getModel();

	    Object[] row =
	    {
		posName, areaName, costCenterName, primaryPrinter, secondaryPrinter, false
	    };

	    defaultTableModel.addRow(row);

	    cmbPrimaryPrinters.setSelectedIndex(0);
	    cmbSecondaryPrinters.setSelectedIndex(0);

	    txtPrimaryPrinterName.setText("");
	    txtSecondaryPrinterName.setText("");
	}

    }

    private void funRemoveButtonClicked()
    {
	DefaultTableModel defaultTableModel = (DefaultTableModel) tblCostCenterPrinterSetup.getModel();

	int selectedRow = tblCostCenterPrinterSetup.getSelectedRow();
	if (selectedRow > -1)
	{

	    String posName = defaultTableModel.getValueAt(selectedRow, 0).toString();
	    String posCode = hmPOS.get(posName);

	    String areaName = defaultTableModel.getValueAt(selectedRow, 1).toString();
	    String areaCode = hmArea.get(areaName);

	    String costCenterName = defaultTableModel.getValueAt(selectedRow, 2).toString();
	    String costCenterCode = hmCostCenter.get(costCenterName);

	    String key = posName + "!" + areaName + "!" + costCenterName;

	    mapPrinterSetup.remove(key);

	    defaultTableModel.removeRow(selectedRow);
	}

    }

    private void funGetThePrinterHelp(int selectedRow, int selectedColumn)
    {
	new frmSearchFormDialog(this, true, arrListPrinters, "Printers").setVisible(true);
	if (clsGlobalVarClass.gSearchItemClicked)
	{
	    Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
	    clsGlobalVarClass.gSearchItemClicked = false;
	    System.out.println(clsGlobalVarClass.gSearchedItem);

	    tblCostCenterPrinterSetup.setValueAt(data[0], selectedRow, selectedColumn);

	    tblCostCenterPrinterSetup.getValueAt(selectedRow, selectedColumn).toString();

	}
    }

    private void funSaveButtonClicked()
    {
	try
	{
	    StringBuilder sqlBuilder = new StringBuilder("insert into tblprintersetupmaster"
		    + "(strPOSCode,strAreaCode,strCostCenterCode,strPrinterType,strPrimaryPrinterPort,strSecondaryPrinterPort,strPrintOnBothPrintersYN"
		    + ",strUserCreated,strUserEdited,dteDateCreated,dteDateEdited,strClientCode,strDataPostFlag) "
		    + "values ");
	    for (int row = 0; row < tblCostCenterPrinterSetup.getRowCount(); row++)
	    {

		String posName = tblCostCenterPrinterSetup.getValueAt(row, 0).toString();
		String posCode = hmPOS.get(posName);

		String areaName = tblCostCenterPrinterSetup.getValueAt(row, 1).toString();
		String areaCode = hmArea.get(areaName);

		String costCenterName = tblCostCenterPrinterSetup.getValueAt(row, 2).toString();
		String costCenterCode = hmCostCenter.get(costCenterName);

		String printOnBothPrinters = "N";
		if (Boolean.parseBoolean(tblCostCenterPrinterSetup.getValueAt(row, 5).toString()))
		{
		    printOnBothPrinters = "Y";
		}

		String printerType = "Cost Center";

		String primaryPrinter = tblCostCenterPrinterSetup.getValueAt(row, 3).toString();
		String secondaryPrinter = tblCostCenterPrinterSetup.getValueAt(row, 4).toString();

		if (row == 0)
		{
		    sqlBuilder.append("('" + posCode + "','" + areaCode + "','" + costCenterCode + "','" + printerType + "','" + primaryPrinter + "','" + secondaryPrinter + "','" + printOnBothPrinters + "'"
			    + ",'" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.getPOSDateForTransaction() + "','" + clsGlobalVarClass.getPOSDateForTransaction() + "'"
			    + ",'" + clsGlobalVarClass.gClientCode + "','N')");
		}
		else
		{
		    sqlBuilder.append(",('" + posCode + "','" + areaCode + "','" + costCenterCode + "','" + printerType + "','" + primaryPrinter + "','" + secondaryPrinter + "','" + printOnBothPrinters + "'"
			    + ",'" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.getPOSDateForTransaction() + "','" + clsGlobalVarClass.getPOSDateForTransaction() + "'"
			    + ",'" + clsGlobalVarClass.gClientCode + "','N')");
		}

	    }

	    if (tblCostCenterPrinterSetup.getRowCount() > 0)
	    {
		clsGlobalVarClass.dbMysql.execute("truncate tblprintersetupmaster ");

		clsGlobalVarClass.dbMysql.execute(sqlBuilder.toString());

		new frmOkPopUp(this, "Saved successfully.", "Success", -1).setVisible(true);
		return;
	    }
	    else
	    {
		new frmOkPopUp(this, "Please selecte the data.", "Error", 0).setVisible(true);
		return;
	    }

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

}
