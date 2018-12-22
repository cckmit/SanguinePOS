/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSTransaction.view;

import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsSMSSender;
import com.POSGlobal.view.frmOkPopUp;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import org.apache.poi.ss.usermodel.Row;

/**
 *
 * @author sss11
 */
public class frmSendBulkSMS extends javax.swing.JFrame
{

    private String fileName;
    private ArrayList<String> mobileNumberList;
    private DefaultTableModel dtm;
    private Object[] records, ob1;
    private JTable table;
    private JScrollPane scroll;
// header is Vector contains table Column
    private Vector headers = new Vector();
// Model is used to construct JTable
    private DefaultTableModel model = null;
// data is Vector contains Data from Excel File
    private Vector data = new Vector();

    private JFileChooser jChooser;
    private int tableWidth = 0; // set the tableWidth
    private int tableHeight = 0; // set the tableHeight
    private DefaultTableModel dm;

    public frmSendBulkSMS()
    {
        initComponents();
        funSetLookAndFeel();
        //SwingUtilities.updateComponentTreeUI( this );

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

            mobileNumberList = new ArrayList<String>();

            funFillCustTypeCombo();
            funFillAreaCombo();
            funSetShortCutKeys();

            funCreateTable(chkDOB.isSelected());

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void funSetShortCutKeys()
    {
        btnExecute.setMnemonic('e');
        btnSendTestSMS.setMnemonic('t');
        btnCancel.setMnemonic('c');
        btnSend.setMnemonic('s');
        btnReset.setMnemonic('r');

    }

    /**
     * This method is used to fill pos name combobox
     *
     * @throws Exception
     * @return
     */
    private void funFillCustTypeCombo() throws Exception
    {
        try
        {
            String custTypeSql = "select a.strCustTypeCode,a.strCustType from tblcustomertypemaster a";
            ResultSet resultSet = clsGlobalVarClass.dbMysql.executeResultSet(custTypeSql);
            cmbCustomerType.addItem("All                                                             !All");
            while (resultSet.next())
            {
                cmbCustomerType.addItem(resultSet.getString(2) + "                                                             !" + resultSet.getString(1));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

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
        btnSend = new javax.swing.JButton();
        lblFormName = new javax.swing.JLabel();
        btnReset = new javax.swing.JButton();
        lblCustomerType = new javax.swing.JLabel();
        cmbCustomerType = new javax.swing.JComboBox();
        lblSMS = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtSMS = new javax.swing.JTextArea();
        lblArea = new javax.swing.JLabel();
        cmbArea = new javax.swing.JComboBox();
        chkDOB = new javax.swing.JCheckBox();
        lblTestMobileNo = new javax.swing.JLabel();
        txtTestMobileNo = new javax.swing.JTextField();
        btnExecute = new javax.swing.JButton();
        btnSendTestSMS = new javax.swing.JButton();
        scrPaneCustomers = new javax.swing.JScrollPane();
        tblCustomers = new javax.swing.JTable();
        lblFileSelection = new javax.swing.JLabel();
        txtFileName = new javax.swing.JTextField();
        btnBrowse = new javax.swing.JButton();
        btnImportFile = new javax.swing.JButton();

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
        lblformName.setText("-Send Bulk SMS");
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
        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn1.png"))); // NOI18N
        btnCancel.setText("CLOSE");
        btnCancel.setToolTipText("Close Area Master");
        btnCancel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCancel.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn2.png"))); // NOI18N
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

        btnSend.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnSend.setForeground(new java.awt.Color(255, 255, 255));
        btnSend.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn1.png"))); // NOI18N
        btnSend.setText("SEND SMS");
        btnSend.setToolTipText("Save Area Master");
        btnSend.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSend.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn2.png"))); // NOI18N
        btnSend.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnSendMouseClicked(evt);
            }
        });
        btnSend.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnSendActionPerformed(evt);
            }
        });
        btnSend.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                btnSendKeyPressed(evt);
            }
        });

        lblFormName.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        lblFormName.setForeground(new java.awt.Color(14, 7, 7));
        lblFormName.setText("Bulk SMS");

        btnReset.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnReset.setForeground(new java.awt.Color(255, 255, 255));
        btnReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn1.png"))); // NOI18N
        btnReset.setText("RESET");
        btnReset.setToolTipText("Reset All Fields");
        btnReset.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnReset.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn2.png"))); // NOI18N
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

        lblCustomerType.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblCustomerType.setText("Customer Type :");

        cmbCustomerType.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbCustomerTypeKeyPressed(evt);
            }
        });

        lblSMS.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblSMS.setText("SMS                :");

        txtSMS.setColumns(20);
        txtSMS.setRows(5);
        jScrollPane1.setViewportView(txtSMS);

        lblArea.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblArea.setText("Area :");

        cmbArea.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbAreaKeyPressed(evt);
            }
        });

        chkDOB.setText("DOB :");
        chkDOB.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        lblTestMobileNo.setText("Test Mobile No. :");

        txtTestMobileNo.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtTestMobileNoActionPerformed(evt);
            }
        });

        btnExecute.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnExecute.setForeground(new java.awt.Color(255, 255, 255));
        btnExecute.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn1.png"))); // NOI18N
        btnExecute.setText("EXECUTE");
        btnExecute.setToolTipText("Reset All Fields");
        btnExecute.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnExecute.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn2.png"))); // NOI18N
        btnExecute.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnExecuteMouseClicked(evt);
            }
        });
        btnExecute.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnExecuteActionPerformed(evt);
            }
        });

        btnSendTestSMS.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnSendTestSMS.setForeground(new java.awt.Color(255, 255, 255));
        btnSendTestSMS.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnSendTestSMS.setText("SEND TEST SMS");
        btnSendTestSMS.setToolTipText("Save Area Master");
        btnSendTestSMS.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSendTestSMS.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnSendTestSMS.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnSendTestSMSMouseClicked(evt);
            }
        });
        btnSendTestSMS.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnSendTestSMSActionPerformed(evt);
            }
        });
        btnSendTestSMS.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                btnSendTestSMSKeyPressed(evt);
            }
        });

        tblCustomers.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Customer Name", "Area", "Customer Type", " Visited", "SMS"
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
        scrPaneCustomers.setViewportView(tblCustomers);
        if (tblCustomers.getColumnModel().getColumnCount() > 0)
        {
            tblCustomers.getColumnModel().getColumn(0).setResizable(false);
            tblCustomers.getColumnModel().getColumn(0).setPreferredWidth(150);
            tblCustomers.getColumnModel().getColumn(1).setResizable(false);
            tblCustomers.getColumnModel().getColumn(1).setPreferredWidth(100);
            tblCustomers.getColumnModel().getColumn(2).setResizable(false);
            tblCustomers.getColumnModel().getColumn(2).setPreferredWidth(100);
            tblCustomers.getColumnModel().getColumn(3).setResizable(false);
            tblCustomers.getColumnModel().getColumn(3).setPreferredWidth(50);
            tblCustomers.getColumnModel().getColumn(4).setResizable(false);
            tblCustomers.getColumnModel().getColumn(4).setPreferredWidth(250);
        }

        lblFileSelection.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblFileSelection.setText("Select File       :");

        txtFileName.setEditable(false);

        btnBrowse.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnBrowse.setForeground(new java.awt.Color(254, 254, 254));
        btnBrowse.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnBrowse.setText("Browse");
        btnBrowse.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnBrowse.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnBrowse.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnBrowseMouseClicked(evt);
            }
        });
        btnBrowse.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnBrowseActionPerformed(evt);
            }
        });

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

        javax.swing.GroupLayout panelBodyLayout = new javax.swing.GroupLayout(panelBody);
        panelBody.setLayout(panelBodyLayout);
        panelBodyLayout.setHorizontalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblFileSelection, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtFileName, javax.swing.GroupLayout.PREFERRED_SIZE, 451, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBrowse, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnImportFile, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelBodyLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblCustomerType)
                                    .addComponent(lblSMS, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelBodyLayout.createSequentialGroup()
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 552, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(btnExecute, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(panelBodyLayout.createSequentialGroup()
                                        .addComponent(cmbCustomerType, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(lblArea)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cmbArea, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(chkDOB)
                                        .addGap(18, 18, 18)
                                        .addComponent(lblTestMobileNo)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtTestMobileNo, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(panelBodyLayout.createSequentialGroup()
                                .addGap(353, 353, 353)
                                .addComponent(lblFormName)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBodyLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnSendTestSMS, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnSend, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(scrPaneCustomers, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        panelBodyLayout.setVerticalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addComponent(lblFormName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtTestMobileNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblCustomerType, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmbCustomerType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblArea)
                        .addComponent(cmbArea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(chkDOB)
                        .addComponent(lblTestMobileNo)))
                .addGap(18, 18, 18)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addComponent(btnExecute, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelBodyLayout.createSequentialGroup()
                                .addComponent(lblSMS)
                                .addGap(0, 54, Short.MAX_VALUE))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtFileName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnBrowse, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnImportFile, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lblFileSelection, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(15, 15, 15)))
                .addComponent(scrPaneCustomers, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnSendTestSMS, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnSend, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        panelBodyLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {chkDOB, cmbArea, cmbCustomerType, lblArea, lblCustomerType, lblTestMobileNo, txtTestMobileNo});

        panelBodyLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnBrowse, btnImportFile});

        panelLayout.add(panelBody, new java.awt.GridBagConstraints());

        getContentPane().add(panelLayout, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseClicked

        dispose();
        clsGlobalVarClass.hmActiveForms.remove("SendBulkSMS");
    }//GEN-LAST:event_btnCancelMouseClicked

    private void btnSendMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSendMouseClicked


    }//GEN-LAST:event_btnSendMouseClicked

    private void btnResetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnResetMouseClicked

    }//GEN-LAST:event_btnResetMouseClicked

    private void cmbCustomerTypeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbCustomerTypeKeyPressed


    }//GEN-LAST:event_cmbCustomerTypeKeyPressed

    private void btnSendKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnSendKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {

        }
    }//GEN-LAST:event_btnSendKeyPressed

    private void btnSendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSendActionPerformed

        if (clsGlobalVarClass.gSMSApi.isEmpty() || clsGlobalVarClass.gSMSApi == null)
        {
            new frmOkPopUp(null, "Invalid SMS URL.", "Message", 1).setVisible(true);
            return;
        }

        if (tblCustomers.getRowCount() > 0)
        {
            boolean flag = funSendBulkSMS(tblCustomers);

            if (flag)
            {
                new frmOkPopUp(null, "Messages are sending...", "Message", 1).setVisible(true);
                return;
            }
        }
        else
        {
            new frmOkPopUp(null, "No Customer Is Selected.", "Error", 1).setVisible(true);
            return;
        }
        dtm.setRowCount(0);
    }//GEN-LAST:event_btnSendActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        funResetFields();
    }//GEN-LAST:event_btnResetActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed

        funResetLookAndFeel();
        dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void cmbAreaKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_cmbAreaKeyPressed
    {//GEN-HEADEREND:event_cmbAreaKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbAreaKeyPressed

    private void btnExecuteMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnExecuteMouseClicked
    {//GEN-HEADEREND:event_btnExecuteMouseClicked

    }//GEN-LAST:event_btnExecuteMouseClicked

    private void btnExecuteActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnExecuteActionPerformed
    {//GEN-HEADEREND:event_btnExecuteActionPerformed
        if (txtSMS.getText().isEmpty())
        {
            new frmOkPopUp(null, "Please Enter The Message..", "Message", 1).setVisible(true);
            return;
        }
        String custTypeCode = cmbCustomerType.getSelectedItem().toString().split("!")[1];
        String areaCode = cmbArea.getSelectedItem().toString().split("!")[1];

        funFillCustomerTable(custTypeCode, areaCode, chkDOB.isSelected());
    }//GEN-LAST:event_btnExecuteActionPerformed

    private void btnSendTestSMSMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnSendTestSMSMouseClicked
    {//GEN-HEADEREND:event_btnSendTestSMSMouseClicked
//        if(txtSMS.getText().isEmpty())
//        {
//            new frmOkPopUp(null, "Please Enter The Message..", "Error", 1).setVisible(true);
//            return;
//        }
//        if(txtTestMobileNo.getText().isEmpty())
//        {
//            new frmOkPopUp(null, "Please Enter The Test Mobile Number.", "Error", 1).setVisible(true);
//            return;
//        }
//        String custTypeCode=cmbCustomerType.getSelectedItem().toString().split("!")[1];
//        String areaCode=cmbArea.getSelectedItem().toString().split("!")[1];
//
//        funSendTestSMS(txtTestMobileNo.getText());
    }//GEN-LAST:event_btnSendTestSMSMouseClicked

    private void btnSendTestSMSActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnSendTestSMSActionPerformed
    {//GEN-HEADEREND:event_btnSendTestSMSActionPerformed
        if (txtSMS.getText().isEmpty())
        {
            new frmOkPopUp(null, "Please Enter The Message..", "Error", 1).setVisible(true);
            return;
        }
        if (txtTestMobileNo.getText().isEmpty())
        {
            new frmOkPopUp(null, "Please Enter The Test Mobile Number.", "Error", 1).setVisible(true);
            return;
        }
        if (!txtTestMobileNo.getText().matches("\\d{10}"))
        {
            new frmOkPopUp(null, "Please Enter Valid Mobile Number.", "Error", 1).setVisible(true);
            return;
        }

        if (clsGlobalVarClass.gSMSApi.isEmpty() || clsGlobalVarClass.gSMSApi == null)
        {
            new frmOkPopUp(null, "Invalid SMS URL.", "Message", 1).setVisible(true);
            return;
        }
        String custTypeCode = cmbCustomerType.getSelectedItem().toString().split("!")[1];
        String areaCode = cmbArea.getSelectedItem().toString().split("!")[1];

        funSendTestSMS(txtTestMobileNo.getText());
    }//GEN-LAST:event_btnSendTestSMSActionPerformed

    private void btnSendTestSMSKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_btnSendTestSMSKeyPressed
    {//GEN-HEADEREND:event_btnSendTestSMSKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSendTestSMSKeyPressed

    private void txtTestMobileNoActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtTestMobileNoActionPerformed
    {//GEN-HEADEREND:event_txtTestMobileNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTestMobileNoActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosed
    {//GEN-HEADEREND:event_formWindowClosed
        clsGlobalVarClass.hmActiveForms.remove("SendBulkSMS");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosing
    {//GEN-HEADEREND:event_formWindowClosing
        clsGlobalVarClass.hmActiveForms.remove("SendBulkSMS");
    }//GEN-LAST:event_formWindowClosing

    private void btnBrowseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBrowseMouseClicked
        // TODO add your handling code here:
        funBrowseFile();
    }//GEN-LAST:event_btnBrowseMouseClicked

    private void btnBrowseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBrowseActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnBrowseActionPerformed

    private void btnImportFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImportFileActionPerformed
        // TODO add your handling code here:
        jChooser = new JFileChooser();
        funImportExcel();
    }//GEN-LAST:event_btnImportFileActionPerformed

    /**
     * @param args the command line arguments
     */
//    public static void main(String args[])
//    {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
//         */
//        try
//        {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels())
//            {
//                if ("Nimbus".equals(info.getName()))
//                {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        }
//        catch (ClassNotFoundException ex)
//        {
//            java.util.logging.Logger.getLogger(frmSendBulkSMS.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        catch (InstantiationException ex)
//        {
//            java.util.logging.Logger.getLogger(frmSendBulkSMS.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        catch (IllegalAccessException ex)
//        {
//            java.util.logging.Logger.getLogger(frmSendBulkSMS.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        catch (javax.swing.UnsupportedLookAndFeelException ex)
//        {
//            java.util.logging.Logger.getLogger(frmSendBulkSMS.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//        //</editor-fold>
//        //</editor-fold>
//        //</editor-fold>
//        //</editor-fold>
//        //</editor-fold>
//        //</editor-fold>
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable()
//        {
//            public void run()
//            {
//                new frmSendBulkSMS().setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBrowse;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnExecute;
    private javax.swing.JButton btnImportFile;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnSend;
    private javax.swing.JButton btnSendTestSMS;
    private javax.swing.JCheckBox chkDOB;
    private javax.swing.JComboBox cmbArea;
    private javax.swing.JComboBox cmbCustomerType;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblArea;
    private javax.swing.JLabel lblCustomerType;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblFileSelection;
    private javax.swing.JLabel lblFormName;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblSMS;
    private javax.swing.JLabel lblTestMobileNo;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblformName;
    private javax.swing.JPanel panelBody;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelLayout;
    private javax.swing.JScrollPane scrPaneCustomers;
    private javax.swing.JTable tblCustomers;
    private javax.swing.JTextField txtFileName;
    private javax.swing.JTextArea txtSMS;
    private javax.swing.JTextField txtTestMobileNo;
    // End of variables declaration//GEN-END:variables

    private boolean funSendBulkSMS(ArrayList<String> mobileNumberList)
    {
        try
        {
            if (mobileNumberList.size() > 0)
            {
                clsSMSSender objSMSSender = new clsSMSSender(mobileNumberList, txtSMS.getText().trim());
                objSMSSender.start();
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void funResetFields()
    {
        cmbCustomerType.setSelectedIndex(0);
        cmbArea.setSelectedIndex(0);
        chkDOB.setSelected(false);
        txtTestMobileNo.setText("");
        txtSMS.setText("");
        ((DefaultTableModel) tblCustomers.getModel()).setRowCount(0);
    }

    private void funFillAreaCombo()
    {
        try
        {
            String custTypeSql = "select a.strBuildingCode,a.strBuildingName from tblbuildingmaster a ";
            ResultSet resultSet = clsGlobalVarClass.dbMysql.executeResultSet(custTypeSql);
            cmbArea.addItem("All                                                             !All");
            while (resultSet.next())
            {
                cmbArea.addItem(resultSet.getString(2) + "                                                             !" + resultSet.getString(1));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private DefaultTableModel funCreateTable(boolean isDOBSelected)
    {

        try
        {
            dtm = new DefaultTableModel()
            {

                @Override
                public boolean isCellEditable(int row, int column)
                {
                    if (column == 6)
                    {
                        return true;
                    }
                    else
                    {
                        return false;
                    }
                }

            };

            dtm.setRowCount(0);

            dtm.addColumn("Customer Name");
            dtm.addColumn("Mobile Number");
            dtm.addColumn("DOB");
            dtm.addColumn("Customer Type");
            dtm.addColumn("Area");
            dtm.addColumn("Visited");
            dtm.addColumn("SMS");

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return dtm;
    }

    private void funFillCustomerTable(String custTypeCode, String areaCode, boolean isDOBSelected)
    {
        try
        {
            String sql = "";
            String filter = "";
            int month = 0;
            int day = 0;

            if (isDOBSelected)
            {
                Date currentDate = new Date();
                month = currentDate.getMonth();
                day = currentDate.getDay();

                month++;
                day--;
            }

            sql = "select a.strCustomerName,a.longMobileNo,a.dteDOB,c.strCustType,b.strBuildingName,a.strCustomerCode\n"
                    + "from tblcustomermaster a\n"
                    + "left outer join tblbuildingmaster b on a.strBuldingCode=b.strBuildingCode\n"
                    + "left outer join tblcustomertypemaster c on c.strCustTypeCode=a.strCustomerType ";

            if (custTypeCode.equalsIgnoreCase("All"))
            {
                if (areaCode.equalsIgnoreCase("All"))
                {
                    if (isDOBSelected)
                    {
                        filter = filter + " where MONTH(a.dteDOB) = '" + month + "' AND DAY(a.dteDOB) ='" + day + "' ";
                    }
                }
                else
                {
                    filter = filter + " where b.strBuildingCode='" + areaCode + "' ";
                    if (isDOBSelected)
                    {
                        filter = filter + " and MONTH(a.dteDOB) = '" + month + "' AND DAY(a.dteDOB) ='" + day + "' ";
                    }
                }
            }
            else
            {
                filter = filter + " where c.strCustTypeCode='" + custTypeCode + "' ";
                if (areaCode.equalsIgnoreCase("All"))
                {
                    if (isDOBSelected)
                    {
                        filter = filter + " and MONTH(a.dteDOB) = '" + month + "' AND DAY(a.dteDOB) ='" + day + "' ";
                    }
                }
                else
                {
                    filter = filter + " and b.strBuildingCode='" + areaCode + "' ";
                    if (isDOBSelected)
                    {
                        filter = filter + " and MONTH(a.dteDOB) = '" + month + "' AND DAY(a.dteDOB) ='" + day + "' ";
                    }
                }
            }
            filter = filter + " group by a.strCustomerCode";

            sql = sql + filter;
            System.out.println("cust sql=" + sql);
            ResultSet resultSet = clsGlobalVarClass.dbMysql.executeResultSet(sql);

            dtm.setRowCount(0);
            mobileNumberList.clear();
            while (resultSet.next())
            {
                int noOfTimeVisited = funGetNoOfTimesVisited(resultSet.getString(6));
                Object row[] = new Object[]
                {
                    resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5), noOfTimeVisited, txtSMS.getText()
                };
                mobileNumberList.add(resultSet.getString(2));

                dtm.addRow(row);
            }
            tblCustomers.setModel(dtm);
            tblCustomers.setRowHeight(30);

            DefaultTableCellRenderer rightRender = new DefaultTableCellRenderer();
            rightRender.setHorizontalAlignment(JLabel.RIGHT);

            tblCustomers.getColumnModel().getColumn(0).setPreferredWidth(150);
            tblCustomers.getColumnModel().getColumn(1).setPreferredWidth(80);
            tblCustomers.getColumnModel().getColumn(2).setPreferredWidth(80);
            tblCustomers.getColumnModel().getColumn(3).setPreferredWidth(100);
            tblCustomers.getColumnModel().getColumn(4).setPreferredWidth(100);
            tblCustomers.getColumnModel().getColumn(5).setPreferredWidth(70);
            tblCustomers.getColumnModel().getColumn(5).setCellRenderer(rightRender);
            tblCustomers.getColumnModel().getColumn(6).setPreferredWidth(200);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private int funGetNoOfTimesVisited(String customerCode)
    {
        int noOfTimesCustomerVisited = 0;
        try
        {
            String sql = "select count(*) from tblbillhd a\n"
                    + "left outer join tblqbillhd b on a.strCustomerCode=b.strCustomerCode\n"
                    + "left outer join tblcustomermaster c on a.strCustomerCode=c.strCustomerCode\n"
                    + "where a.strCustomerCode='" + customerCode + "' ";
            ResultSet resultSet = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            if (resultSet.next())
            {
                noOfTimesCustomerVisited = resultSet.getInt(1);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return noOfTimesCustomerVisited;
    }

    private void funSendTestSMS(String testMobileNumber)
    {
        try
        {
            ArrayList<String> mobileNumberList = new ArrayList<String>();
            mobileNumberList.add(testMobileNumber);
            boolean isSend = funSendBulkSMS(mobileNumberList);
            if (isSend)
            {
                new frmOkPopUp(null, "Test SMS Sent To :" + testMobileNumber + ".", "Message", 1).setVisible(true);
                return;
            }
            else
            {
                new frmOkPopUp(null, "Unable To Send SMS To :" + testMobileNumber + ".", "Error", 1).setVisible(true);
                return;
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private boolean funSendBulkSMS(JTable tblCustomers)
    {
        try
        {
            ArrayList<String> mobileNoList = new ArrayList<>();
            for (int i = 0; i < tblCustomers.getRowCount(); i++)
            {
                if ((!tblCustomers.getValueAt(i, 1).toString().isEmpty()))
                {
                    mobileNoList.add(tblCustomers.getValueAt(i, 1).toString());
                }
            }
            if (mobileNoList.size() > 0)
            {
                clsSMSSender objSMSSender = new clsSMSSender(mobileNoList, txtSMS.getText().trim().toString());
                objSMSSender.start();
            }
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            new frmOkPopUp(null, "Unable To Send Messages.", "Error", 1).setVisible(true);
            return false;
        }
    }

    private void funSetLookAndFeel()
    {
        try
        {
            // Set System L&F
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(this);
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
    }

    private void funResetLookAndFeel()
    {
        try
        {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels())
            {
                System.out.println("lookandfeel" + info.getName());
                if ("Nimbus".equals(info.getName()))
                {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    SwingUtilities.updateComponentTreeUI(this);
                    break;
                }
            }
        }
        catch (ClassNotFoundException ex)
        {
            java.util.logging.Logger.getLogger(frmSendBulkSMS.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(frmSendBulkSMS.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(frmSendBulkSMS.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(frmSendBulkSMS.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }

    /*
     Browse File For Import
     */
    private void funBrowseFile()
    {
        try
        {
            JFileChooser jfc = new JFileChooser();
            if (jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
            {
                File tempFile = jfc.getSelectedFile();
                String imagePath = tempFile.getAbsolutePath();
                fileName = imagePath.substring(imagePath.lastIndexOf("/") + 1, imagePath.length());
                txtFileName.setText(tempFile.getAbsolutePath());
                System.out.println(fileName);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void funImportExcel()
    {
        btnImportFile.setEnabled(true);
        btnReset.setEnabled(false);
        btnCancel.setEnabled(true);
        btnBrowse.setEnabled(false);
        StringBuilder sb = new StringBuilder(fileName);
        String fileExtension = sb.substring(sb.indexOf(".") + 1, sb.length()).toString();
        if (!fileExtension.equals("xls"))
        {
            JOptionPane.showMessageDialog(this, "Invalid File, Please Import .xls File");
            return;
        }
        else
        {
            funImportMasters();
            JOptionPane.showMessageDialog(this, "Data Imported Successfully");

        }

    }

    private boolean funImportMasters()
    {
        boolean flgImport = false;

        flgImport = funReadExcelFile(fileName);
        if (!flgImport)
        {
            JOptionPane.showMessageDialog(null, "Data is present in Database, This module reuires Blank Database.");
        }
        return flgImport;
    }

    private boolean funReadExcelFile(String inputFile)
    {
        Vector data = new Vector();
        dm = new DefaultTableModel()
        {
            @Override
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
        };
        dm.addColumn("Customer Name");
        dm.addColumn("Mobile Number");
        dm.addColumn("Message");

        boolean flgResult = false;
        String name = null;
        String query = "";
        File inputWorkbook = new File(inputFile);
        Workbook w;
        Row row1 = null;

        try
        {
            w = Workbook.getWorkbook(inputWorkbook);
            // Get the first sheet
            Sheet sheet = w.getSheet(0);

//            for (int i = 0; i < sheet.getColumns(); i++) {
//                Cell cell1 = sheet.getCell(i, 0);
//                dm.addColumn(cell1.getContents());
//            }
            String message = txtSMS.getText().trim();

            boolean isEOF = false;
            for (int row = 4; row < sheet.getRows(); row++)
            {

                boolean createdBy = sheet.getRow(row).equals("");
                if (createdBy)
                {
                    break;
                }
                if (isEOF)
                {
                    break;
                }

                Object tblRow[] = new Object[3];
                for (int col = 0; col < 2; col++)
                {

                    Cell cell = sheet.getCell(col, row);
                    CellType type = cell.getType();

                    name = cell.getContents().trim();

                    if (name.equalsIgnoreCase("Total"))
                    {

                        //isEOF=true;
                        break;
                    }

                    if (name.equalsIgnoreCase(""))
                    {
                        createdBy = true;
                        break;
                    }

                    tblRow[col] = name;

                }

                tblRow[2] = message;
                String space = "";
                String total = "Total";
                if ((name == null) || (name.equals(space)) || (name.equals(total)))
                {
                    break;
                }
                else
                {
                    dm.addRow(tblRow);
                }
            }

            tblCustomers.setModel(dm);
            tblCustomers.setRowHeight(25);

            flgResult = true;
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(null, "Invalid Excel File");
            e.printStackTrace();
        }

        return flgResult;
    }
}
