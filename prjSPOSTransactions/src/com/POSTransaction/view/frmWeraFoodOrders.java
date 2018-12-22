/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSTransaction.view;

import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsUtility;
import com.POSGlobal.view.frmAcceptRejectPopUp;
import com.POSGlobal.view.frmOkCancelPopUp;
import com.POSGlobal.view.frmOkPopUp;
import com.POSTransaction.controller.clsCustomer;
import com.POSTransaction.controller.clsDirectBillerItemDtl;
import com.POSTransaction.controller.clsOrderDtl;
import com.POSTransaction.controller.clsWERAOnlineOrderIntegration;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.Timer;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeSelectionModel;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.net.URL;
import java.text.DecimalFormat;
import javax.swing.JOptionPane;
import javax.swing.tree.TreePath;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import sun.audio.AudioData;
import sun.audio.AudioDataStream;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import sun.audio.ContinuousAudioDataStream;

@SuppressWarnings("unchecked")
public class frmWeraFoodOrders extends javax.swing.JFrame
{

    private HashMap<String, String> mapPOSCode, mapPOSName;
    private StringBuilder sb = new StringBuilder();
    private ResultSet rs;
    DefaultTableModel dmItemLinkup, dmPOSLinkup, dmTaxLinkup, dmSubGroupLinkup, dmSettlementLinkup, dmCostCenterLinkup;
    clsUtility objUtility = new clsUtility();
    private Socket socket;
    private int newOrders = 0;
    private clsWERAOnlineOrderIntegration objWERAOnlineOrderIntegration;
    private HashMap<String, JSONObject> mapOrderInfo;
    private HashMap<String, JSONObject> mapAcceptedOrderInfo;
    private HashMap<String, JSONObject> mapRejectedOrderInfo;
    private HashMap<String, JSONObject> mapPickedUpOrderInfo;
    private HashMap<String, JSONObject> mapDeliveredOrderInfo;
    private HashMap<String, String> mapWeraOrderExternalOrderNo;
    private Pattern itemNamePatter;
    private String strSelectedTab;

    public frmWeraFoodOrders()
    {

	objWERAOnlineOrderIntegration = new clsWERAOnlineOrderIntegration();
	mapOrderInfo = new HashMap<String, JSONObject>();
	mapAcceptedOrderInfo= new HashMap<String, JSONObject>();
	mapRejectedOrderInfo= new HashMap<String, JSONObject>();
	mapPickedUpOrderInfo= new HashMap<String, JSONObject>();
	mapDeliveredOrderInfo= new HashMap<String, JSONObject>();
	mapWeraOrderExternalOrderNo=new HashMap<>();
	itemNamePatter = Pattern.compile("[^a-zA-Z0-9_() ]");
	strSelectedTab="newOrder";
	initComponents();
	try
	{
	    clsGlobalVarClass.gSearchItem = "";
	    clsGlobalVarClass.gSearchFormName = "";

	    lblUserCode.setText(clsGlobalVarClass.gUserCode);
	    lblPosName.setText(clsGlobalVarClass.gPOSName);
	    lblDate.setText(clsGlobalVarClass.gPOSDateToDisplay);
	    lblModuleName.setText(clsGlobalVarClass.gSelectedModule);

	    Timer timer = new Timer(500, new ActionListener()
	    {
		@Override
		public void actionPerformed(ActionEvent e)
		{
		    Date date1 = new Date();
		    String new_str = String.format("%tr", date1);
		    String dateAndTime = clsGlobalVarClass.gPOSDateToDisplay + " " + new_str;
		    lblDate.setText(dateAndTime);

//		    ImageIcon imageIcon = new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgGreenSymbol.png"));
//		    lblNewOrders.setIcon(null);
		    lblNewOrdersValue.setText(String.valueOf(newOrders));

		}
	    });
	    timer.setRepeats(true);
	    timer.setCoalesce(true);
	    timer.setInitialDelay(0);
	    timer.start();

	    funSetShortCutKeys();
	    funResetOrderData();

	    treeOrders.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
	    treeOrders.addTreeSelectionListener(new TreeListener());
	    treeOrders.setCellRenderer(new CustomTreeCellRenderer());

	    treeAcceptedOrder.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
	    treeAcceptedOrder.addTreeSelectionListener(new TreeListener());
	    treeAcceptedOrder.setCellRenderer(new CustomTreeCellRenderer());
	    
	    treeRejectedOrder.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
	    treeRejectedOrder.addTreeSelectionListener(new TreeListener());
	    treeRejectedOrder.setCellRenderer(new CustomTreeCellRenderer());
	    
	    treePickedUpOrder.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
	    treePickedUpOrder.addTreeSelectionListener(new TreeListener());
	    treePickedUpOrder.setCellRenderer(new CustomTreeCellRenderer());
	    
	    treeDeliveredOrder.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
	    treeDeliveredOrder.addTreeSelectionListener(new TreeListener());
	    treeDeliveredOrder.setCellRenderer(new CustomTreeCellRenderer());
	    
	    //header X-Wera-Api-Key:124dfb7e-3266-417c-af75-7bd327ae72ae
	    //body {"merchant_id":330}
	    //make sure WERA integration is ON and Outlet Id is configured.
	    if (clsGlobalVarClass.gWERAOnlineOrderIntegration && clsGlobalVarClass.gWERAMerchantOutletId.trim().length() > 0)
	    {
		tabbedPane.setSelectedIndex(0);

		funConnectToWERAFoodSockect();

		objWERAOnlineOrderIntegration.funSendRequestForPendingOrders();
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
//        btnExit.setMnemonic('c');
//        btnReset.setMnemonic('r');
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
        lblModuleName = new javax.swing.JLabel();
        lblProductName = new javax.swing.JLabel();
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
        mainPanel = new javax.swing.JPanel();
        tabbedPane = new javax.swing.JTabbedPane();
        panelNewOrders = new javax.swing.JPanel();
        btnDownloadMenu1 = new javax.swing.JButton();
        scrollTreeOrder = new javax.swing.JScrollPane();
        treeOrders = new javax.swing.JTree();
        scrollOrderDetail = new javax.swing.JScrollPane();
        tblOrderItemDtl = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        lblOrderNo = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        lblCustomerName = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        lblMobileNo = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        lblAddress = new javax.swing.JLabel();
        lblTotalFinalAmt = new javax.swing.JLabel();
        lblTotalFinalAmtValue = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lblOrderFromValue = new javax.swing.JLabel();
        lblFinalDiscAmt = new javax.swing.JLabel();
        lblDiscAmt = new javax.swing.JLabel();
        lblTotalExtraAmt = new javax.swing.JLabel();
        lblExtraAmt = new javax.swing.JLabel();
        panelAcceptedOrdes = new javax.swing.JPanel();
        scrollTreeAcceptedOrder1 = new javax.swing.JScrollPane();
        treeAcceptedOrder = new javax.swing.JTree();
        jLabel4 = new javax.swing.JLabel();
        lblOrderNo1 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        lblCustomerName1 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        lblMobileNo1 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        lblAddress1 = new javax.swing.JLabel();
        lblTotalFinalAmt1 = new javax.swing.JLabel();
        lblTotalFinalAmtValue1 = new javax.swing.JLabel();
        lblOrderFromValue1 = new javax.swing.JLabel();
        scrollOrderDetail1 = new javax.swing.JScrollPane();
        tblOrderItemDtl1 = new javax.swing.JTable();
        btnPickUpOrder = new javax.swing.JButton();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        txtRiderName = new javax.swing.JTextField();
        txtRiderNo = new javax.swing.JTextField();
        panelRejectedOrders = new javax.swing.JPanel();
        scrollTreeRejectedOrder = new javax.swing.JScrollPane();
        treeRejectedOrder = new javax.swing.JTree();
        jLabel11 = new javax.swing.JLabel();
        lblOrderNo2 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        lblCustomerName2 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        lblMobileNo2 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        lblAddress2 = new javax.swing.JLabel();
        lblTotalFinalAmt2 = new javax.swing.JLabel();
        lblTotalFinalAmtValue2 = new javax.swing.JLabel();
        lblOrderFromValue2 = new javax.swing.JLabel();
        scrollOrderDetail2 = new javax.swing.JScrollPane();
        tblOrderItemDtl2 = new javax.swing.JTable();
        panelPickedUpOrders = new javax.swing.JPanel();
        scrollTreePickedUpOrder = new javax.swing.JScrollPane();
        treePickedUpOrder = new javax.swing.JTree();
        jLabel16 = new javax.swing.JLabel();
        lblOrderNo3 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        lblCustomerName3 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        lblMobileNo3 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        lblAddress3 = new javax.swing.JLabel();
        lblTotalFinalAmt3 = new javax.swing.JLabel();
        lblTotalFinalAmtValue3 = new javax.swing.JLabel();
        lblOrderFromValue3 = new javax.swing.JLabel();
        scrollOrderDetail3 = new javax.swing.JScrollPane();
        tblOrderItemDtl3 = new javax.swing.JTable();
        btnDeliveredOrder = new javax.swing.JButton();
        panelDeliverdOrders = new javax.swing.JPanel();
        scrollTreeDeliveredOrder = new javax.swing.JScrollPane();
        treeDeliveredOrder = new javax.swing.JTree();
        jLabel21 = new javax.swing.JLabel();
        lblOrderNo4 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        lblCustomerName4 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        lblMobileNo4 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        lblAddress4 = new javax.swing.JLabel();
        lblTotalFinalAmt4 = new javax.swing.JLabel();
        lblTotalFinalAmtValue4 = new javax.swing.JLabel();
        lblOrderFromValue4 = new javax.swing.JLabel();
        scrollOrderDetail4 = new javax.swing.JScrollPane();
        tblOrderItemDtl4 = new javax.swing.JTable();
        panelAddUpdateMenu = new javax.swing.JPanel();
        panelDownloadMenu = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDownloadMenu = new javax.swing.JTable();
        btnDownloadMenu = new javax.swing.JButton();
        btnExit = new javax.swing.JButton();
        lvlNewOrders = new javax.swing.JLabel();
        lblNewOrdersValue = new javax.swing.JLabel();
        btnDownloadMenu2 = new javax.swing.JButton();

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

        lblModuleName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblModuleName.setForeground(new java.awt.Color(255, 255, 255));
        panelHeader.add(lblModuleName);

        lblProductName.setFont(new java.awt.Font("Trebuchet MS", 1, 14)); // NOI18N
        lblProductName.setForeground(new java.awt.Color(255, 255, 255));
        lblProductName.setText("SPOS -");
        panelHeader.add(lblProductName);

        lblformName.setFont(new java.awt.Font("Trebuchet MS", 1, 14)); // NOI18N
        lblformName.setForeground(new java.awt.Color(255, 255, 255));
        lblformName.setText("-WERA ONLINE ORDERS");
        lblformName.setMaximumSize(new java.awt.Dimension(170, 17));
        lblformName.setMinimumSize(new java.awt.Dimension(170, 17));
        lblformName.setPreferredSize(new java.awt.Dimension(170, 17));
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

        mainPanel.setOpaque(false);

        tabbedPane.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        tabbedPane.addChangeListener(new javax.swing.event.ChangeListener()
        {
            public void stateChanged(javax.swing.event.ChangeEvent evt)
            {
                tabbedPaneStateChanged(evt);
            }
        });

        panelNewOrders.setOpaque(false);

        btnDownloadMenu1.setFont(new java.awt.Font("Trebuchet MS", 1, 15)); // NOI18N
        btnDownloadMenu1.setForeground(new java.awt.Color(251, 246, 246));
        btnDownloadMenu1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnDownloadMenu1.setText("PROCEED");
        btnDownloadMenu1.setToolTipText("Close Menu Item Pricing");
        btnDownloadMenu1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDownloadMenu1.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnDownloadMenu1.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnDownloadMenu1MouseClicked(evt);
            }
        });

        treeOrders.setFont(new java.awt.Font("Trebuchet MS", 0, 16)); // NOI18N
        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("Pending Orders");
        javax.swing.tree.DefaultMutableTreeNode treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("Order Info");
        treeNode1.add(treeNode2);
        treeOrders.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        treeOrders.setName(""); // NOI18N
        scrollTreeOrder.setViewportView(treeOrders);

        scrollOrderDetail.setBackground(new java.awt.Color(255, 255, 255));
        scrollOrderDetail.setOpaque(false);

        tblOrderItemDtl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "ITEMS", "QTY", "AMT", "DISC%", "DISC AMT", "FINAL AMT", "", ""
            }
        )
        {
            boolean[] canEdit = new boolean []
            {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return canEdit [columnIndex];
            }
        });
        tblOrderItemDtl.setOpaque(false);
        tblOrderItemDtl.setRowHeight(30);
        tblOrderItemDtl.getTableHeader().setReorderingAllowed(false);
        scrollOrderDetail.setViewportView(tblOrderItemDtl);
        if (tblOrderItemDtl.getColumnModel().getColumnCount() > 0)
        {
            tblOrderItemDtl.getColumnModel().getColumn(0).setPreferredWidth(400);
            tblOrderItemDtl.getColumnModel().getColumn(6).setResizable(false);
            tblOrderItemDtl.getColumnModel().getColumn(6).setPreferredWidth(0);
            tblOrderItemDtl.getColumnModel().getColumn(7).setResizable(false);
        }

        jLabel1.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        jLabel1.setText("ORDER NO. :");

        lblOrderNo.setFont(new java.awt.Font("Trebuchet MS", 1, 14)); // NOI18N
        lblOrderNo.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblOrderNo.setText("2458");

        jLabel3.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        jLabel3.setText("CUSTOMER :");

        lblCustomerName.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        lblCustomerName.setText("AJIM SAYYAD");

        jLabel5.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        jLabel5.setText("MOBILE      :");

        lblMobileNo.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        lblMobileNo.setText("9975852590");

        jLabel7.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        jLabel7.setText("ADDRESS    :");

        lblAddress.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        lblAddress.setText("Goregaon East,Mumbai,Maharashtra,India");
        lblAddress.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        lblTotalFinalAmt.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        lblTotalFinalAmt.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblTotalFinalAmt.setText("TOTAL");
        lblTotalFinalAmt.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        lblTotalFinalAmtValue.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        lblTotalFinalAmtValue.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotalFinalAmtValue.setText("0.00");
        lblTotalFinalAmtValue.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        jLabel2.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        jLabel2.setText("ORDER FROM :");

        lblOrderFromValue.setFont(new java.awt.Font("Trebuchet MS", 1, 14)); // NOI18N
        lblOrderFromValue.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        lblFinalDiscAmt.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        lblFinalDiscAmt.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblFinalDiscAmt.setText("0.00");
        lblFinalDiscAmt.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        lblDiscAmt.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        lblDiscAmt.setText("Total Discount Amt");

        lblTotalExtraAmt.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        lblTotalExtraAmt.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotalExtraAmt.setText("0.00");
        lblTotalExtraAmt.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        lblExtraAmt.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        lblExtraAmt.setText("Delivery & Packaging Charges");

        javax.swing.GroupLayout panelNewOrdersLayout = new javax.swing.GroupLayout(panelNewOrders);
        panelNewOrders.setLayout(panelNewOrdersLayout);
        panelNewOrdersLayout.setHorizontalGroup(
            panelNewOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelNewOrdersLayout.createSequentialGroup()
                .addComponent(scrollTreeOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(panelNewOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelNewOrdersLayout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addGroup(panelNewOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelNewOrdersLayout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(18, 18, 18))
                            .addGroup(panelNewOrdersLayout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(1, 1, 1)))
                        .addGroup(panelNewOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblOrderNo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(panelNewOrdersLayout.createSequentialGroup()
                                .addComponent(lblOrderFromValue, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(18, 18, 18)
                        .addGroup(panelNewOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panelNewOrdersLayout.createSequentialGroup()
                                .addGroup(panelNewOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelNewOrdersLayout.createSequentialGroup()
                                        .addComponent(jLabel3)
                                        .addGap(8, 8, 8))
                                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addGroup(panelNewOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelNewOrdersLayout.createSequentialGroup()
                                        .addGap(3, 3, 3)
                                        .addGroup(panelNewOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lblMobileNo, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(lblCustomerName, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(panelNewOrdersLayout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lblAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 363, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                    .addGroup(panelNewOrdersLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnDownloadMenu1, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(panelNewOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelNewOrdersLayout.createSequentialGroup()
                                .addComponent(lblDiscAmt)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblFinalDiscAmt, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelNewOrdersLayout.createSequentialGroup()
                                .addGroup(panelNewOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblExtraAmt)
                                    .addComponent(lblTotalFinalAmt, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelNewOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblTotalExtraAmt, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblTotalFinalAmtValue, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(98, 98, 98))
                    .addGroup(panelNewOrdersLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(scrollOrderDetail, javax.swing.GroupLayout.PREFERRED_SIZE, 706, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(66, Short.MAX_VALUE))))
        );
        panelNewOrdersLayout.setVerticalGroup(
            panelNewOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelNewOrdersLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelNewOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelNewOrdersLayout.createSequentialGroup()
                        .addGroup(panelNewOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblOrderNo, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelNewOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblOrderFromValue, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panelNewOrdersLayout.createSequentialGroup()
                        .addGroup(panelNewOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblCustomerName, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelNewOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblMobileNo, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelNewOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollOrderDetail, javax.swing.GroupLayout.PREFERRED_SIZE, 394, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(panelNewOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelNewOrdersLayout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(btnDownloadMenu1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelNewOrdersLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelNewOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblFinalDiscAmt, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblDiscAmt, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelNewOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblExtraAmt, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblTotalExtraAmt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelNewOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblTotalFinalAmtValue, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblTotalFinalAmt, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(panelNewOrdersLayout.createSequentialGroup()
                .addComponent(scrollTreeOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 599, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        tabbedPane.addTab("New Orders", panelNewOrders);

        panelAcceptedOrdes.setOpaque(false);

        treeAcceptedOrder.setFont(new java.awt.Font("Trebuchet MS", 0, 16)); // NOI18N
        treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("Accepted Orders");
        treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("Order Info");
        treeNode1.add(treeNode2);
        treeAcceptedOrder.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        treeAcceptedOrder.setName(""); // NOI18N
        scrollTreeAcceptedOrder1.setViewportView(treeAcceptedOrder);

        jLabel4.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        jLabel4.setText("ORDER NO. :");

        lblOrderNo1.setFont(new java.awt.Font("Trebuchet MS", 1, 14)); // NOI18N
        lblOrderNo1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblOrderNo1.setText("2458");

        jLabel6.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        jLabel6.setText("CUSTOMER :");

        lblCustomerName1.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        lblCustomerName1.setText("AJIM SAYYAD");

        jLabel8.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        jLabel8.setText("MOBILE      :");

        jLabel9.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        jLabel9.setText("ORDER FROM :");

        lblMobileNo1.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        lblMobileNo1.setText("9975852590");

        jLabel10.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        jLabel10.setText("ADDRESS    :");

        lblAddress1.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        lblAddress1.setText("Goregaon East,Mumbai,Maharashtra,India");
        lblAddress1.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        lblTotalFinalAmt1.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        lblTotalFinalAmt1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblTotalFinalAmt1.setText("TOTAL");
        lblTotalFinalAmt1.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        lblTotalFinalAmtValue1.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        lblTotalFinalAmtValue1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotalFinalAmtValue1.setText("0.00");
        lblTotalFinalAmtValue1.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        lblOrderFromValue1.setFont(new java.awt.Font("Trebuchet MS", 1, 14)); // NOI18N
        lblOrderFromValue1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        scrollOrderDetail1.setBackground(new java.awt.Color(255, 255, 255));
        scrollOrderDetail1.setOpaque(false);

        tblOrderItemDtl1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "ITEMS", "QTY", "AMT", "DISC%", "DISC AMT", "FINAL AMT", ""
            }
        )
        {
            boolean[] canEdit = new boolean []
            {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return canEdit [columnIndex];
            }
        });
        tblOrderItemDtl1.setOpaque(false);
        tblOrderItemDtl1.setRowHeight(30);
        tblOrderItemDtl1.getTableHeader().setReorderingAllowed(false);
        scrollOrderDetail1.setViewportView(tblOrderItemDtl1);
        if (tblOrderItemDtl1.getColumnModel().getColumnCount() > 0)
        {
            tblOrderItemDtl1.getColumnModel().getColumn(0).setPreferredWidth(400);
            tblOrderItemDtl1.getColumnModel().getColumn(6).setMinWidth(0);
            tblOrderItemDtl1.getColumnModel().getColumn(6).setPreferredWidth(0);
            tblOrderItemDtl1.getColumnModel().getColumn(6).setMaxWidth(0);
        }

        btnPickUpOrder.setFont(new java.awt.Font("Trebuchet MS", 1, 15)); // NOI18N
        btnPickUpOrder.setForeground(new java.awt.Color(251, 246, 246));
        btnPickUpOrder.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnPickUpOrder.setText("Pick Up");
        btnPickUpOrder.setToolTipText("Close Menu Item Pricing");
        btnPickUpOrder.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPickUpOrder.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnPickUpOrder.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnPickUpOrderMouseClicked(evt);
            }
        });
        btnPickUpOrder.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnPickUpOrderActionPerformed(evt);
            }
        });

        jLabel26.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        jLabel26.setText("Rider Name:");

        jLabel27.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        jLabel27.setText("Rider Mob NO. :");

        txtRiderName.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtRiderNameActionPerformed(evt);
            }
        });

        txtRiderNo.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtRiderNoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelAcceptedOrdesLayout = new javax.swing.GroupLayout(panelAcceptedOrdes);
        panelAcceptedOrdes.setLayout(panelAcceptedOrdesLayout);
        panelAcceptedOrdesLayout.setHorizontalGroup(
            panelAcceptedOrdesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAcceptedOrdesLayout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(scrollTreeAcceptedOrder1, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelAcceptedOrdesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelAcceptedOrdesLayout.createSequentialGroup()
                        .addGroup(panelAcceptedOrdesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel27))
                        .addGap(18, 18, 18)
                        .addGroup(panelAcceptedOrdesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtRiderNo, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtRiderName, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(147, 147, 147)
                        .addGroup(panelAcceptedOrdesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelAcceptedOrdesLayout.createSequentialGroup()
                                .addComponent(lblTotalFinalAmt1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblTotalFinalAmtValue1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(btnPickUpOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(panelAcceptedOrdesLayout.createSequentialGroup()
                        .addGroup(panelAcceptedOrdesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelAcceptedOrdesLayout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(6, 6, 6)
                                .addComponent(lblOrderNo1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(75, 75, 75))
                            .addGroup(panelAcceptedOrdesLayout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblOrderFromValue1, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGroup(panelAcceptedOrdesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelAcceptedOrdesLayout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblAddress1, javax.swing.GroupLayout.PREFERRED_SIZE, 381, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panelAcceptedOrdesLayout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addGap(11, 11, 11)
                                .addGroup(panelAcceptedOrdesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblMobileNo1, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblCustomerName1, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(163, 163, 163))
                    .addGroup(panelAcceptedOrdesLayout.createSequentialGroup()
                        .addComponent(scrollOrderDetail1, javax.swing.GroupLayout.PREFERRED_SIZE, 738, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        panelAcceptedOrdesLayout.setVerticalGroup(
            panelAcceptedOrdesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAcceptedOrdesLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(panelAcceptedOrdesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelAcceptedOrdesLayout.createSequentialGroup()
                        .addGroup(panelAcceptedOrdesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblCustomerName1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblOrderNo1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(panelAcceptedOrdesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(panelAcceptedOrdesLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(panelAcceptedOrdesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblMobileNo1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelAcceptedOrdesLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblOrderFromValue1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(panelAcceptedOrdesLayout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelAcceptedOrdesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAddress1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollOrderDetail1, javax.swing.GroupLayout.PREFERRED_SIZE, 386, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23)
                .addGroup(panelAcceptedOrdesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTotalFinalAmt1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTotalFinalAmtValue1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtRiderName, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelAcceptedOrdesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtRiderNo, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPickUpOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(panelAcceptedOrdesLayout.createSequentialGroup()
                .addComponent(scrollTreeAcceptedOrder1)
                .addContainerGap())
        );

        tabbedPane.addTab("Accepted Orders", panelAcceptedOrdes);

        panelRejectedOrders.setOpaque(false);

        treeRejectedOrder.setFont(new java.awt.Font("Trebuchet MS", 0, 16)); // NOI18N
        treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("Rejected Orders");
        treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("Order Info");
        treeNode1.add(treeNode2);
        treeRejectedOrder.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        treeRejectedOrder.setName(""); // NOI18N
        scrollTreeRejectedOrder.setViewportView(treeRejectedOrder);

        jLabel11.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        jLabel11.setText("ORDER NO. :");

        lblOrderNo2.setFont(new java.awt.Font("Trebuchet MS", 1, 14)); // NOI18N
        lblOrderNo2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblOrderNo2.setText("2458");

        jLabel12.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        jLabel12.setText("CUSTOMER :");

        lblCustomerName2.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        lblCustomerName2.setText("AJIM SAYYAD");

        jLabel13.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        jLabel13.setText("MOBILE      :");

        jLabel14.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        jLabel14.setText("ORDER FROM :");

        lblMobileNo2.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        lblMobileNo2.setText("9975852590");

        jLabel15.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        jLabel15.setText("ADDRESS    :");

        lblAddress2.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        lblAddress2.setText("Goregaon East,Mumbai,Maharashtra,India");
        lblAddress2.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        lblTotalFinalAmt2.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        lblTotalFinalAmt2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblTotalFinalAmt2.setText("TOTAL");
        lblTotalFinalAmt2.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        lblTotalFinalAmtValue2.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        lblTotalFinalAmtValue2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotalFinalAmtValue2.setText("0.00");
        lblTotalFinalAmtValue2.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        lblOrderFromValue2.setFont(new java.awt.Font("Trebuchet MS", 1, 14)); // NOI18N
        lblOrderFromValue2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        scrollOrderDetail2.setBackground(new java.awt.Color(255, 255, 255));
        scrollOrderDetail2.setOpaque(false);

        tblOrderItemDtl2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "ITEMS", "QTY", "AMT", "DISC%", "DISC AMT", "FINAL AMT", ""
            }
        )
        {
            boolean[] canEdit = new boolean []
            {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return canEdit [columnIndex];
            }
        });
        tblOrderItemDtl2.setOpaque(false);
        tblOrderItemDtl2.setRowHeight(30);
        tblOrderItemDtl2.getTableHeader().setReorderingAllowed(false);
        scrollOrderDetail2.setViewportView(tblOrderItemDtl2);
        if (tblOrderItemDtl2.getColumnModel().getColumnCount() > 0)
        {
            tblOrderItemDtl2.getColumnModel().getColumn(0).setPreferredWidth(400);
            tblOrderItemDtl2.getColumnModel().getColumn(6).setMinWidth(0);
            tblOrderItemDtl2.getColumnModel().getColumn(6).setPreferredWidth(0);
            tblOrderItemDtl2.getColumnModel().getColumn(6).setMaxWidth(0);
        }

        javax.swing.GroupLayout panelRejectedOrdersLayout = new javax.swing.GroupLayout(panelRejectedOrders);
        panelRejectedOrders.setLayout(panelRejectedOrdersLayout);
        panelRejectedOrdersLayout.setHorizontalGroup(
            panelRejectedOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRejectedOrdersLayout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(scrollTreeRejectedOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(panelRejectedOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelRejectedOrdersLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelRejectedOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelRejectedOrdersLayout.createSequentialGroup()
                                .addGroup(panelRejectedOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelRejectedOrdersLayout.createSequentialGroup()
                                        .addGap(1, 1, 1)
                                        .addComponent(jLabel14))
                                    .addComponent(jLabel11))
                                .addGap(6, 6, 6)
                                .addComponent(lblOrderNo2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addGroup(panelRejectedOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelRejectedOrdersLayout.createSequentialGroup()
                                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(lblOrderFromValue2, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(440, 440, 440))
                                    .addGroup(panelRejectedOrdersLayout.createSequentialGroup()
                                        .addGroup(panelRejectedOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(panelRejectedOrdersLayout.createSequentialGroup()
                                                .addComponent(jLabel12)
                                                .addGap(8, 8, 8))
                                            .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.TRAILING))
                                        .addGroup(panelRejectedOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(panelRejectedOrdersLayout.createSequentialGroup()
                                                .addGap(3, 3, 3)
                                                .addComponent(lblCustomerName2, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(panelRejectedOrdersLayout.createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(lblAddress2, javax.swing.GroupLayout.PREFERRED_SIZE, 384, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGap(153, 153, 153)))
                                .addComponent(lblMobileNo2, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(panelRejectedOrdersLayout.createSequentialGroup()
                                .addComponent(scrollOrderDetail2, javax.swing.GroupLayout.PREFERRED_SIZE, 708, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelRejectedOrdersLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblTotalFinalAmt2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblTotalFinalAmtValue2, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(387, 387, 387))))
        );
        panelRejectedOrdersLayout.setVerticalGroup(
            panelRejectedOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRejectedOrdersLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelRejectedOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelRejectedOrdersLayout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelRejectedOrdersLayout.createSequentialGroup()
                        .addGroup(panelRejectedOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelRejectedOrdersLayout.createSequentialGroup()
                                .addGroup(panelRejectedOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblCustomerName2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblOrderNo2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(panelRejectedOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblMobileNo2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(panelRejectedOrdersLayout.createSequentialGroup()
                                .addGap(34, 34, 34)
                                .addComponent(lblOrderFromValue2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(panelRejectedOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelRejectedOrdersLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelRejectedOrdersLayout.createSequentialGroup()
                                .addGap(13, 13, 13)
                                .addComponent(lblAddress2, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollOrderDetail2, javax.swing.GroupLayout.PREFERRED_SIZE, 386, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(panelRejectedOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTotalFinalAmt2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTotalFinalAmtValue2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(51, Short.MAX_VALUE))
            .addComponent(scrollTreeRejectedOrder)
        );

        tabbedPane.addTab("Rejected Orders", panelRejectedOrders);

        panelPickedUpOrders.setOpaque(false);

        treePickedUpOrder.setFont(new java.awt.Font("Trebuchet MS", 0, 16)); // NOI18N
        treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("PickedUp Orders");
        treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("Order Info");
        treeNode1.add(treeNode2);
        treePickedUpOrder.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        treePickedUpOrder.setName(""); // NOI18N
        scrollTreePickedUpOrder.setViewportView(treePickedUpOrder);

        jLabel16.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        jLabel16.setText("ORDER NO. :");

        lblOrderNo3.setFont(new java.awt.Font("Trebuchet MS", 1, 14)); // NOI18N
        lblOrderNo3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblOrderNo3.setText("2458");

        jLabel17.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        jLabel17.setText("CUSTOMER :");

        lblCustomerName3.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        lblCustomerName3.setText("AJIM SAYYAD");

        jLabel18.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        jLabel18.setText("MOBILE      :");

        jLabel19.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        jLabel19.setText("ORDER FROM :");

        lblMobileNo3.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        lblMobileNo3.setText("9975852590");

        jLabel20.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        jLabel20.setText("ADDRESS    :");

        lblAddress3.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        lblAddress3.setText("Goregaon East,Mumbai,Maharashtra,India");
        lblAddress3.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        lblTotalFinalAmt3.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        lblTotalFinalAmt3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblTotalFinalAmt3.setText("TOTAL");
        lblTotalFinalAmt3.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        lblTotalFinalAmtValue3.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        lblTotalFinalAmtValue3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotalFinalAmtValue3.setText("0.00");
        lblTotalFinalAmtValue3.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        lblOrderFromValue3.setFont(new java.awt.Font("Trebuchet MS", 1, 14)); // NOI18N
        lblOrderFromValue3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        scrollOrderDetail3.setBackground(new java.awt.Color(255, 255, 255));
        scrollOrderDetail3.setOpaque(false);

        tblOrderItemDtl3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "ITEMS", "QTY", "AMT", "DISC%", "DISC AMT", "FINAL AMT", ""
            }
        )
        {
            boolean[] canEdit = new boolean []
            {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return canEdit [columnIndex];
            }
        });
        tblOrderItemDtl3.setOpaque(false);
        tblOrderItemDtl3.setRowHeight(30);
        tblOrderItemDtl3.getTableHeader().setReorderingAllowed(false);
        scrollOrderDetail3.setViewportView(tblOrderItemDtl3);
        if (tblOrderItemDtl3.getColumnModel().getColumnCount() > 0)
        {
            tblOrderItemDtl3.getColumnModel().getColumn(0).setPreferredWidth(400);
            tblOrderItemDtl3.getColumnModel().getColumn(6).setMinWidth(0);
            tblOrderItemDtl3.getColumnModel().getColumn(6).setPreferredWidth(0);
            tblOrderItemDtl3.getColumnModel().getColumn(6).setMaxWidth(0);
        }

        btnDeliveredOrder.setFont(new java.awt.Font("Trebuchet MS", 1, 15)); // NOI18N
        btnDeliveredOrder.setForeground(new java.awt.Color(251, 246, 246));
        btnDeliveredOrder.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnDeliveredOrder.setText("Delivered");
        btnDeliveredOrder.setToolTipText("Close Menu Item Pricing");
        btnDeliveredOrder.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDeliveredOrder.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnDeliveredOrder.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnDeliveredOrderMouseClicked(evt);
            }
        });
        btnDeliveredOrder.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnDeliveredOrderActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelPickedUpOrdersLayout = new javax.swing.GroupLayout(panelPickedUpOrders);
        panelPickedUpOrders.setLayout(panelPickedUpOrdersLayout);
        panelPickedUpOrdersLayout.setHorizontalGroup(
            panelPickedUpOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPickedUpOrdersLayout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(scrollTreePickedUpOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(panelPickedUpOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelPickedUpOrdersLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelPickedUpOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelPickedUpOrdersLayout.createSequentialGroup()
                                .addGroup(panelPickedUpOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelPickedUpOrdersLayout.createSequentialGroup()
                                        .addGap(1, 1, 1)
                                        .addComponent(jLabel19))
                                    .addComponent(jLabel16))
                                .addGap(6, 6, 6)
                                .addGroup(panelPickedUpOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblOrderFromValue3, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblOrderNo3, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelPickedUpOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(panelPickedUpOrdersLayout.createSequentialGroup()
                                        .addGroup(panelPickedUpOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(panelPickedUpOrdersLayout.createSequentialGroup()
                                                .addComponent(jLabel17)
                                                .addGap(8, 8, 8))
                                            .addComponent(jLabel20, javax.swing.GroupLayout.Alignment.TRAILING))
                                        .addGroup(panelPickedUpOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(panelPickedUpOrdersLayout.createSequentialGroup()
                                                .addGap(3, 3, 3)
                                                .addGroup(panelPickedUpOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(lblMobileNo3, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(lblCustomerName3, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                            .addGroup(panelPickedUpOrdersLayout.createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(lblAddress3, javax.swing.GroupLayout.PREFERRED_SIZE, 397, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelPickedUpOrdersLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(scrollOrderDetail3, javax.swing.GroupLayout.PREFERRED_SIZE, 741, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(88, 88, 88))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelPickedUpOrdersLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(panelPickedUpOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelPickedUpOrdersLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnDeliveredOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelPickedUpOrdersLayout.createSequentialGroup()
                                .addComponent(lblTotalFinalAmt3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblTotalFinalAmtValue3, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(125, 125, 125))))
        );
        panelPickedUpOrdersLayout.setVerticalGroup(
            panelPickedUpOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPickedUpOrdersLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelPickedUpOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelPickedUpOrdersLayout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelPickedUpOrdersLayout.createSequentialGroup()
                        .addGroup(panelPickedUpOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(panelPickedUpOrdersLayout.createSequentialGroup()
                                .addGroup(panelPickedUpOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lblOrderNo3, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblOrderFromValue3, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelPickedUpOrdersLayout.createSequentialGroup()
                                .addGroup(panelPickedUpOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblCustomerName3, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(panelPickedUpOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblMobileNo3, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelPickedUpOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelPickedUpOrdersLayout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addComponent(lblAddress3, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(scrollOrderDetail3, javax.swing.GroupLayout.PREFERRED_SIZE, 386, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelPickedUpOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTotalFinalAmt3, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTotalFinalAmtValue3, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnDeliveredOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
            .addComponent(scrollTreePickedUpOrder)
        );

        tabbedPane.addTab("Picked Up Orders", panelPickedUpOrders);

        panelDeliverdOrders.setOpaque(false);

        treeDeliveredOrder.setFont(new java.awt.Font("Trebuchet MS", 0, 16)); // NOI18N
        treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("Delivered Orders");
        treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("Order Info");
        treeNode1.add(treeNode2);
        treeDeliveredOrder.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        treeDeliveredOrder.setName(""); // NOI18N
        scrollTreeDeliveredOrder.setViewportView(treeDeliveredOrder);

        jLabel21.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        jLabel21.setText("ORDER NO. :");

        lblOrderNo4.setFont(new java.awt.Font("Trebuchet MS", 1, 14)); // NOI18N
        lblOrderNo4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblOrderNo4.setText("2458");

        jLabel22.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        jLabel22.setText("CUSTOMER :");

        lblCustomerName4.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        lblCustomerName4.setText("AJIM SAYYAD");

        jLabel23.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        jLabel23.setText("MOBILE      :");

        jLabel24.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        jLabel24.setText("ORDER FROM :");

        lblMobileNo4.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        lblMobileNo4.setText("9975852590");

        jLabel25.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        jLabel25.setText("ADDRESS    :");

        lblAddress4.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        lblAddress4.setText("Goregaon East,Mumbai,Maharashtra,India");
        lblAddress4.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        lblTotalFinalAmt4.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        lblTotalFinalAmt4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblTotalFinalAmt4.setText("TOTAL");
        lblTotalFinalAmt4.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        lblTotalFinalAmtValue4.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        lblTotalFinalAmtValue4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotalFinalAmtValue4.setText("0.00");
        lblTotalFinalAmtValue4.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        lblOrderFromValue4.setFont(new java.awt.Font("Trebuchet MS", 1, 14)); // NOI18N
        lblOrderFromValue4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        scrollOrderDetail4.setBackground(new java.awt.Color(255, 255, 255));
        scrollOrderDetail4.setOpaque(false);

        tblOrderItemDtl4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "ITEMS", "QTY", "AMT", "DISC%", "DISC AMT", "FINAL AMT", ""
            }
        )
        {
            boolean[] canEdit = new boolean []
            {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return canEdit [columnIndex];
            }
        });
        tblOrderItemDtl4.setOpaque(false);
        tblOrderItemDtl4.setRowHeight(30);
        tblOrderItemDtl4.getTableHeader().setReorderingAllowed(false);
        scrollOrderDetail4.setViewportView(tblOrderItemDtl4);
        if (tblOrderItemDtl4.getColumnModel().getColumnCount() > 0)
        {
            tblOrderItemDtl4.getColumnModel().getColumn(0).setPreferredWidth(400);
            tblOrderItemDtl4.getColumnModel().getColumn(6).setMinWidth(0);
            tblOrderItemDtl4.getColumnModel().getColumn(6).setPreferredWidth(0);
            tblOrderItemDtl4.getColumnModel().getColumn(6).setMaxWidth(0);
        }

        javax.swing.GroupLayout panelDeliverdOrdersLayout = new javax.swing.GroupLayout(panelDeliverdOrders);
        panelDeliverdOrders.setLayout(panelDeliverdOrdersLayout);
        panelDeliverdOrdersLayout.setHorizontalGroup(
            panelDeliverdOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDeliverdOrdersLayout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(scrollTreeDeliveredOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelDeliverdOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelDeliverdOrdersLayout.createSequentialGroup()
                        .addGroup(panelDeliverdOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelDeliverdOrdersLayout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addComponent(jLabel24))
                            .addComponent(jLabel21))
                        .addGap(6, 6, 6)
                        .addComponent(lblOrderNo4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(109, 109, 109)
                        .addGroup(panelDeliverdOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelDeliverdOrdersLayout.createSequentialGroup()
                                .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(panelDeliverdOrdersLayout.createSequentialGroup()
                                .addGroup(panelDeliverdOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelDeliverdOrdersLayout.createSequentialGroup()
                                        .addComponent(jLabel22)
                                        .addGap(8, 8, 8))
                                    .addComponent(jLabel25, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addGroup(panelDeliverdOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelDeliverdOrdersLayout.createSequentialGroup()
                                        .addGap(3, 3, 3)
                                        .addGroup(panelDeliverdOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lblMobileNo4, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(lblCustomerName4, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(panelDeliverdOrdersLayout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lblAddress4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblOrderFromValue4, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panelDeliverdOrdersLayout.createSequentialGroup()
                        .addGroup(panelDeliverdOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(scrollOrderDetail4, javax.swing.GroupLayout.PREFERRED_SIZE, 750, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelDeliverdOrdersLayout.createSequentialGroup()
                                .addComponent(lblTotalFinalAmt4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblTotalFinalAmtValue4, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(15, 15, 15)))
                        .addContainerGap(23, Short.MAX_VALUE))))
        );
        panelDeliverdOrdersLayout.setVerticalGroup(
            panelDeliverdOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDeliverdOrdersLayout.createSequentialGroup()
                .addGroup(panelDeliverdOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelDeliverdOrdersLayout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(lblOrderFromValue4, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelDeliverdOrdersLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(panelDeliverdOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelDeliverdOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lblOrderNo4, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelDeliverdOrdersLayout.createSequentialGroup()
                                .addGap(34, 34, 34)
                                .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelDeliverdOrdersLayout.createSequentialGroup()
                                .addGroup(panelDeliverdOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblCustomerName4, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(panelDeliverdOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblMobileNo4, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(panelDeliverdOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lblAddress4, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(scrollOrderDetail4, javax.swing.GroupLayout.PREFERRED_SIZE, 386, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelDeliverdOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTotalFinalAmt4, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTotalFinalAmtValue4, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(54, Short.MAX_VALUE))
            .addComponent(scrollTreeDeliveredOrder)
        );

        tabbedPane.addTab("Deliverd Orders", panelDeliverdOrders);

        panelAddUpdateMenu.setOpaque(false);

        javax.swing.GroupLayout panelAddUpdateMenuLayout = new javax.swing.GroupLayout(panelAddUpdateMenu);
        panelAddUpdateMenu.setLayout(panelAddUpdateMenuLayout);
        panelAddUpdateMenuLayout.setHorizontalGroup(
            panelAddUpdateMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1068, Short.MAX_VALUE)
        );
        panelAddUpdateMenuLayout.setVerticalGroup(
            panelAddUpdateMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 618, Short.MAX_VALUE)
        );

        tabbedPane.addTab("Add/Update Menu", panelAddUpdateMenu);

        panelDownloadMenu.setOpaque(false);

        tblDownloadMenu.setAutoCreateRowSorter(true);
        tblDownloadMenu.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "ITEM", "GROUP", "SUB GROUP", "MENU HEAD", "COST CENTER", "RATE", "IS MODIFIER", "MODIFIER GROUP", "MIN", "MAX"
            }
        )
        {
            boolean[] canEdit = new boolean []
            {
                false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return canEdit [columnIndex];
            }
        });
        tblDownloadMenu.setRowHeight(30);
        tblDownloadMenu.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tblDownloadMenu);

        btnDownloadMenu.setFont(new java.awt.Font("Trebuchet MS", 1, 15)); // NOI18N
        btnDownloadMenu.setForeground(new java.awt.Color(251, 246, 246));
        btnDownloadMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnDownloadMenu.setText("SAVE");
        btnDownloadMenu.setToolTipText("Close Menu Item Pricing");
        btnDownloadMenu.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDownloadMenu.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnDownloadMenu.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnDownloadMenuMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout panelDownloadMenuLayout = new javax.swing.GroupLayout(panelDownloadMenu);
        panelDownloadMenu.setLayout(panelDownloadMenuLayout);
        panelDownloadMenuLayout.setHorizontalGroup(
            panelDownloadMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDownloadMenuLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(panelDownloadMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnDownloadMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1056, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(12, Short.MAX_VALUE))
        );
        panelDownloadMenuLayout.setVerticalGroup(
            panelDownloadMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDownloadMenuLayout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 563, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnDownloadMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        tabbedPane.addTab("Download Menu", panelDownloadMenu);

        btnExit.setFont(new java.awt.Font("Trebuchet MS", 1, 15)); // NOI18N
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

        lvlNewOrders.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        lvlNewOrders.setForeground(new java.awt.Color(0, 51, 255));
        lvlNewOrders.setText("New Orders : ");

        lblNewOrdersValue.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        lblNewOrdersValue.setForeground(new java.awt.Color(0, 51, 255));
        lblNewOrdersValue.setText("547");

        btnDownloadMenu2.setFont(new java.awt.Font("Trebuchet MS", 1, 15)); // NOI18N
        btnDownloadMenu2.setForeground(new java.awt.Color(251, 246, 246));
        btnDownloadMenu2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnDownloadMenu2.setText("REFRESH");
        btnDownloadMenu2.setToolTipText("Close Menu Item Pricing");
        btnDownloadMenu2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDownloadMenu2.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnDownloadMenu2.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnDownloadMenu2MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addComponent(lvlNewOrders, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblNewOrdersValue, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addComponent(btnDownloadMenu2, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnExit, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(tabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 1073, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 11, Short.MAX_VALUE))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblNewOrdersValue, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
                    .addComponent(lvlNewOrders, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(1, 1, 1)
                .addComponent(tabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 647, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnExit, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDownloadMenu2, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.GroupLayout panelLayoutLayout = new javax.swing.GroupLayout(panelLayout);
        panelLayout.setLayout(panelLayoutLayout);
        panelLayoutLayout.setHorizontalGroup(
            panelLayoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLayoutLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(149, Short.MAX_VALUE))
        );
        panelLayoutLayout.setVerticalGroup(
            panelLayoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        getContentPane().add(panelLayout, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
	// TODO add your handling code here:
	clsGlobalVarClass.hmActiveForms.remove("Wera Food Online Orders");
	if (socket != null)
	{
	    socket.disconnect();
	    socket.close();
	}
	funExitClick();

    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
	// TODO add your handling code here:
	clsGlobalVarClass.hmActiveForms.remove("Wera Food Online Orders");
	if (socket != null)
	{
	    socket.disconnect();
	    socket.close();
	}
	funExitClick();
    }//GEN-LAST:event_formWindowClosing

    private void btnExitMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnExitMouseClicked
    {//GEN-HEADEREND:event_btnExitMouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_btnExitMouseClicked

    private void btnExitActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnExitActionPerformed
    {//GEN-HEADEREND:event_btnExitActionPerformed
	clsGlobalVarClass.hmActiveForms.remove("Wera Food Online Orders");
	if (socket != null)
	{
	    socket.disconnect();
	    socket.close();
	}

	funExitClick();
    }//GEN-LAST:event_btnExitActionPerformed

    private void tabbedPaneStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_tabbedPaneStateChanged
    {//GEN-HEADEREND:event_tabbedPaneStateChanged
	funTabbedChanged();
    }//GEN-LAST:event_tabbedPaneStateChanged

    private void btnDownloadMenuMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnDownloadMenuMouseClicked
    {//GEN-HEADEREND:event_btnDownloadMenuMouseClicked
	if (tblDownloadMenu.getRowCount() <= 0)
	{
	    frmOkPopUp objOkPopUp = new frmOkPopUp(this, "No data found.", "Download Menu", 2);
	    objOkPopUp.setVisible(true);
	}
	else
	{
	    funDownloadButtonClicked();
	}
    }//GEN-LAST:event_btnDownloadMenuMouseClicked

    private void btnDownloadMenu1MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnDownloadMenu1MouseClicked
    {//GEN-HEADEREND:event_btnDownloadMenu1MouseClicked
	funSaveButtonClicked();
    }//GEN-LAST:event_btnDownloadMenu1MouseClicked

    private void btnDownloadMenu2MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnDownloadMenu2MouseClicked
    {//GEN-HEADEREND:event_btnDownloadMenu2MouseClicked
	funRefreshManually();
    }//GEN-LAST:event_btnDownloadMenu2MouseClicked

    private void btnPickUpOrderMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnPickUpOrderMouseClicked
    {//GEN-HEADEREND:event_btnPickUpOrderMouseClicked
        // TODO add your handling code here:
	funPickedUpOrder();
    }//GEN-LAST:event_btnPickUpOrderMouseClicked

    private void btnPickUpOrderActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnPickUpOrderActionPerformed
    {//GEN-HEADEREND:event_btnPickUpOrderActionPerformed
        // TODO add your handling code here:
	
    }//GEN-LAST:event_btnPickUpOrderActionPerformed

    private void txtRiderNameActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtRiderNameActionPerformed
    {//GEN-HEADEREND:event_txtRiderNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtRiderNameActionPerformed

    private void txtRiderNoActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtRiderNoActionPerformed
    {//GEN-HEADEREND:event_txtRiderNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtRiderNoActionPerformed

    private void btnDeliveredOrderMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnDeliveredOrderMouseClicked
    {//GEN-HEADEREND:event_btnDeliveredOrderMouseClicked
        // TODO add your handling code here:
	funDeliveredOrder();
    }//GEN-LAST:event_btnDeliveredOrderMouseClicked

    private void btnDeliveredOrderActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnDeliveredOrderActionPerformed
    {//GEN-HEADEREND:event_btnDeliveredOrderActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDeliveredOrderActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDeliveredOrder;
    private javax.swing.JButton btnDownloadMenu;
    private javax.swing.JButton btnDownloadMenu1;
    private javax.swing.JButton btnDownloadMenu2;
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnPickUpOrder;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblAddress;
    private javax.swing.JLabel lblAddress1;
    private javax.swing.JLabel lblAddress2;
    private javax.swing.JLabel lblAddress3;
    private javax.swing.JLabel lblAddress4;
    private javax.swing.JLabel lblCustomerName;
    private javax.swing.JLabel lblCustomerName1;
    private javax.swing.JLabel lblCustomerName2;
    private javax.swing.JLabel lblCustomerName3;
    private javax.swing.JLabel lblCustomerName4;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblDiscAmt;
    private javax.swing.JLabel lblExtraAmt;
    private javax.swing.JLabel lblFinalDiscAmt;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblMobileNo;
    private javax.swing.JLabel lblMobileNo1;
    private javax.swing.JLabel lblMobileNo2;
    private javax.swing.JLabel lblMobileNo3;
    private javax.swing.JLabel lblMobileNo4;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblNewOrdersValue;
    private javax.swing.JLabel lblOrderFromValue;
    private javax.swing.JLabel lblOrderFromValue1;
    private javax.swing.JLabel lblOrderFromValue2;
    private javax.swing.JLabel lblOrderFromValue3;
    private javax.swing.JLabel lblOrderFromValue4;
    private javax.swing.JLabel lblOrderNo;
    private javax.swing.JLabel lblOrderNo1;
    private javax.swing.JLabel lblOrderNo2;
    private javax.swing.JLabel lblOrderNo3;
    private javax.swing.JLabel lblOrderNo4;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblTotalExtraAmt;
    private javax.swing.JLabel lblTotalFinalAmt;
    private javax.swing.JLabel lblTotalFinalAmt1;
    private javax.swing.JLabel lblTotalFinalAmt2;
    private javax.swing.JLabel lblTotalFinalAmt3;
    private javax.swing.JLabel lblTotalFinalAmt4;
    private javax.swing.JLabel lblTotalFinalAmtValue;
    private javax.swing.JLabel lblTotalFinalAmtValue1;
    private javax.swing.JLabel lblTotalFinalAmtValue2;
    private javax.swing.JLabel lblTotalFinalAmtValue3;
    private javax.swing.JLabel lblTotalFinalAmtValue4;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblformName;
    private javax.swing.JLabel lvlNewOrders;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JPanel panelAcceptedOrdes;
    private javax.swing.JPanel panelAddUpdateMenu;
    private javax.swing.JPanel panelDeliverdOrders;
    private javax.swing.JPanel panelDownloadMenu;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelLayout;
    private javax.swing.JPanel panelNewOrders;
    private javax.swing.JPanel panelPickedUpOrders;
    private javax.swing.JPanel panelRejectedOrders;
    private javax.swing.JScrollPane scrollOrderDetail;
    private javax.swing.JScrollPane scrollOrderDetail1;
    private javax.swing.JScrollPane scrollOrderDetail2;
    private javax.swing.JScrollPane scrollOrderDetail3;
    private javax.swing.JScrollPane scrollOrderDetail4;
    private javax.swing.JScrollPane scrollTreeAcceptedOrder1;
    private javax.swing.JScrollPane scrollTreeDeliveredOrder;
    private javax.swing.JScrollPane scrollTreeOrder;
    private javax.swing.JScrollPane scrollTreePickedUpOrder;
    private javax.swing.JScrollPane scrollTreeRejectedOrder;
    private javax.swing.JTabbedPane tabbedPane;
    private javax.swing.JTable tblDownloadMenu;
    private javax.swing.JTable tblOrderItemDtl;
    private javax.swing.JTable tblOrderItemDtl1;
    private javax.swing.JTable tblOrderItemDtl2;
    private javax.swing.JTable tblOrderItemDtl3;
    private javax.swing.JTable tblOrderItemDtl4;
    private javax.swing.JTree treeAcceptedOrder;
    private javax.swing.JTree treeDeliveredOrder;
    private javax.swing.JTree treeOrders;
    private javax.swing.JTree treePickedUpOrder;
    private javax.swing.JTree treeRejectedOrder;
    private javax.swing.JTextField txtRiderName;
    private javax.swing.JTextField txtRiderNo;
    // End of variables declaration//GEN-END:variables

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
	    java.util.logging.Logger.getLogger(frmWeraFoodOrders.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	}
	catch (InstantiationException ex)
	{
	    java.util.logging.Logger.getLogger(frmWeraFoodOrders.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	}
	catch (IllegalAccessException ex)
	{
	    java.util.logging.Logger.getLogger(frmWeraFoodOrders.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	}
	catch (javax.swing.UnsupportedLookAndFeelException ex)
	{
	    java.util.logging.Logger.getLogger(frmWeraFoodOrders.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	}
	dispose();
    }

    private void funConnectToWERAFoodSockect()
    {
	try
	{
	    socket = IO.socket("https://www.werafoods.com:9005");
	    socket.on("notification", new Emitter.Listener()
	    {
		@Override
		public void call(Object... args)
		{
		    try
		    {
			org.json.JSONObject obj = (org.json.JSONObject) args[0];
			org.json.JSONObject obj1 = (org.json.JSONObject) obj;
			//System.out.println("Merchant Id : " + obj1.getString("message"));

			//for testing 
			if (obj1.getString("message").equals(clsGlobalVarClass.gWERAMerchantOutletId))
			{
			    System.out.println("New order received,It's your restaurant id." + obj1.getString("message"));
			    funPlayNewOrderNotificationAlert();

			    funNewOrderSelected();
			}

		    }
		    catch (Exception e)
		    {
			e.printStackTrace();
		    }
		}

		private void funPlayNewOrderNotificationAlert()
		{
		    try
		    {
			AudioPlayer audioPlayer = AudioPlayer.player;
			String path = getClass().getResource("/com/POSTransaction/images/notificationXperiaForNewOrder.wav").getPath();

			//FileInputStream fis = new FileInputStream(new File(System.getProperty("user.dir")+"//src//com//spos//images//notificationXperiaForNewOrder.wav"));
			InputStream is = frmWeraFoodOrders.class.getResourceAsStream("/com/POSTransaction/images/notificationXperiaForNewOrder.wav");

			//FileInputStream fis = new FileInputStream(new File(path));
			AudioStream as = new AudioStream(is); // header plus audio data
			AudioData ad = as.getData(); // audio data only, no header
			AudioDataStream audioDataStream = new AudioDataStream(ad);
			ContinuousAudioDataStream continuousAudioDataStream = new ContinuousAudioDataStream(ad);

			audioPlayer.start(audioDataStream);
		    }
		    catch (Exception e)
		    {
			e.printStackTrace();
		    }
		}
	    });
	    socket.connect();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funSendRequestForPendingOrders()
    {
	try
	{
	    socket = IO.socket("https://www.werafoods.com:9005/?message=" + clsGlobalVarClass.gWERAMerchantOutletId);
	    socket.on("notification", new Emitter.Listener()
	    {
		@Override
		public void call(Object... args)
		{
		    try
		    {
			org.json.JSONObject obj = (org.json.JSONObject) args[0];
			org.json.JSONObject obj1 = (org.json.JSONObject) obj;
			//System.out.println("Merchant Id : " + obj1.getString("message"));

			newOrders = Integer.parseInt(obj1.getString("message"));
			System.out.println("");
		    }
		    catch (Exception e)
		    {
			e.printStackTrace();
		    }
		}
	    });
	    socket.connect();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funTabbedChanged()
    {
	int selectedTabbed = tabbedPane.getSelectedIndex();

	if (selectedTabbed == 0)//new orders
	{
	    strSelectedTab="newOrder";
//	    if (clsGlobalVarClass.gWERAOnlineOrderIntegration && clsGlobalVarClass.gWERAMerchantOutletId.trim().length() > 0)
//	    {
//		  funNewOrderSelected();
//	    }
	    //funNewOrderSelected();

//	    javax.swing.tree.DefaultMutableTreeNode rooNodePendingOrders = new javax.swing.tree.DefaultMutableTreeNode("Pending Orders");
//
//	    javax.swing.tree.DefaultMutableTreeNode orderInfo = new javax.swing.tree.DefaultMutableTreeNode("Order Info");
//
//	    javax.swing.tree.DefaultMutableTreeNode orderNo = new javax.swing.tree.DefaultMutableTreeNode("2458");
//	    javax.swing.tree.DefaultMutableTreeNode customerName = new javax.swing.tree.DefaultMutableTreeNode("Ajim Sayyad");
//	    orderNo.add(customerName);
//	    javax.swing.tree.DefaultMutableTreeNode items = new javax.swing.tree.DefaultMutableTreeNode("Items");
//	    javax.swing.tree.DefaultMutableTreeNode item = new javax.swing.tree.DefaultMutableTreeNode("A");
//	    items.add(item);
//	    item = new javax.swing.tree.DefaultMutableTreeNode("B");
//	    items.add(item);
//	    item = new javax.swing.tree.DefaultMutableTreeNode("C");
//	    items.add(item);
//	    orderNo.add(items);
//	    orderInfo.add(orderNo);
//
//	    orderNo = new javax.swing.tree.DefaultMutableTreeNode("8975");
//	    customerName = new javax.swing.tree.DefaultMutableTreeNode("Rahul Ara");
//	    orderNo.add(customerName);
//	    items = new javax.swing.tree.DefaultMutableTreeNode("Items");
//	    item = new javax.swing.tree.DefaultMutableTreeNode("U");
//	    items.add(item);
//	    item = new javax.swing.tree.DefaultMutableTreeNode("F");
//	    items.add(item);
//	    item = new javax.swing.tree.DefaultMutableTreeNode("P");
//	    items.add(item);
//	    orderNo.add(items);
//	    orderInfo.add(orderNo);
//
//	    rooNodePendingOrders.add(orderInfo);
//	    treeOrders.setModel(new javax.swing.tree.DefaultTreeModel(rooNodePendingOrders));
//
//	    treeOrders.setShowsRootHandles(true);
//	    treeOrders.putClientProperty("JTree.lineStyle", "Angled");
	}
	else if (selectedTabbed == 1)//accecpted orders
	{
	    strSelectedTab="accecptedOrder";
	    funAcceptedOrderSelected();
	}
	else if (selectedTabbed == 2)//rejected orders
	{
	    strSelectedTab="rejectedOrder";
	    funRejectedOrderSelected();
	}
	else if (selectedTabbed == 3)//pickedUp orders
	{
	    strSelectedTab="pickedUpOrder";
	    funPickedUpOrderSelected();
	}
	else if (selectedTabbed == 4)//delverded orders
	{
	    strSelectedTab="deliverdedOrder";
	    funDeliveredOrderSelected();
	}
	else if (selectedTabbed == 5)//Add/Update menu
	{
	    strSelectedTab="AddUpdateOrder";
	    funAddUpdateMenuOrderSelected();
	}
	else if (selectedTabbed == 6)//Download menu
	{
	    strSelectedTab="DownloadOrder";
	    funDownloadMenuSelected();
	}
	else
	{
	    tabbedPane.setSelectedIndex(0);
	    //funNewOrderSelected();
	}

    }

    private void funNewOrderSelected()
    {
	JSONObject rootJSONObject = objWERAOnlineOrderIntegration.funGetAllPendingOrders();
	//JSONObject rootJSONObject = objWERAOnlineOrderIntegration.funAllAcceptedOrder(); //for testing
	
	mapOrderInfo.clear();

	JSONObject jObjDetails = (JSONObject) rootJSONObject.get("details");

	JSONArray jArrOrders = (JSONArray) jObjDetails.get("orders");

	javax.swing.tree.DefaultMutableTreeNode rooNodePendingOrders = new javax.swing.tree.DefaultMutableTreeNode("Pending Orders");

	javax.swing.tree.DefaultMutableTreeNode orderInfo = new javax.swing.tree.DefaultMutableTreeNode("Order Info");

	newOrders = jArrOrders.size();

	for (int order = 0; order < jArrOrders.size(); order++)
	{
	    JSONObject jObjOrder = (JSONObject) jArrOrders.get(order);

	    JSONObject jObjOrderInfo = (JSONObject) jObjOrder.get("order_info");
	    String external_order_id = jObjOrderInfo.get("external_order_id").toString();
	    String orderId = jObjOrderInfo.get("order_id").toString();
	    //putting for nexr manupulation
	    mapOrderInfo.put(external_order_id, jObjOrder);
	    mapWeraOrderExternalOrderNo.put(external_order_id, orderId);
	    
	    JSONObject jObjCustomer = (JSONObject) jObjOrder.get("customer");
	    String customerName = jObjCustomer.get("name").toString();

	    clsCustomer objCustomer = new clsCustomer();
	    objCustomer.setStrCustomerName(customerName);

	    javax.swing.tree.DefaultMutableTreeNode nodeOrderNo = new javax.swing.tree.DefaultMutableTreeNode(external_order_id);
	    javax.swing.tree.DefaultMutableTreeNode nodeCustomerName = new javax.swing.tree.DefaultMutableTreeNode(objCustomer);
	    nodeOrderNo.add(nodeCustomerName);

	    javax.swing.tree.DefaultMutableTreeNode nodeItems = new javax.swing.tree.DefaultMutableTreeNode("Items");
	    JSONArray jArrCart = (JSONArray) jObjOrder.get("cart");

	    clsOrderDtl[] arrItems = new clsOrderDtl[jArrCart.size()];

	    for (int cart = 0; cart < jArrCart.size(); cart++)
	    {
		JSONObject jObjItem = (JSONObject) jArrCart.get(cart);
		String item = jObjItem.get("item_name").toString();

		clsOrderDtl objOrderDtl = new clsOrderDtl();
		objOrderDtl.setItemName(item);

		arrItems[cart] = objOrderDtl;
	    }
	    for (clsOrderDtl item : arrItems)
	    {
		javax.swing.tree.DefaultMutableTreeNode nodeItem = new javax.swing.tree.DefaultMutableTreeNode(item);
		nodeItems.add(nodeItem);
	    }

	    nodeOrderNo.add(nodeItems);
	    orderInfo.add(nodeOrderNo);
	}

	rooNodePendingOrders.add(orderInfo);
	treeOrders.setModel(new javax.swing.tree.DefaultTreeModel(rooNodePendingOrders));

//	treeOrders.setShowsRootHandles(true);
//	treeOrders.putClientProperty("JTree.lineStyle", "Angled");
    }

    private void funAcceptedOrderSelected()
    {
	// objWERAOnlineOrderIntegration.funAllAcceptedOrder();

	JSONObject rootJSONObject = objWERAOnlineOrderIntegration.funAllAcceptedOrder();
	mapAcceptedOrderInfo.clear();

	JSONObject jObjDetails = (JSONObject) rootJSONObject.get("details");

	JSONArray jArrOrders = (JSONArray) jObjDetails.get("orders");

	javax.swing.tree.DefaultMutableTreeNode rooNodePendingOrders = new javax.swing.tree.DefaultMutableTreeNode("Accepted Orders");

	javax.swing.tree.DefaultMutableTreeNode orderInfo = new javax.swing.tree.DefaultMutableTreeNode("Order Info");

	newOrders = jArrOrders.size();

	for (int order = 0; order < jArrOrders.size(); order++)
	{
	    JSONObject jObjOrder = (JSONObject) jArrOrders.get(order);

	    JSONObject jObjOrderInfo = (JSONObject) jObjOrder.get("order_info");

	    String orderId = jObjOrderInfo.get("order_id").toString();
	    String external_order_id = jObjOrderInfo.get("external_order_id").toString();
	    mapWeraOrderExternalOrderNo.put(external_order_id, orderId);
	    
	    //putting for nexr manupulation
	    mapAcceptedOrderInfo.put(external_order_id, jObjOrder);

	    JSONObject jObjCustomer = (JSONObject) jObjOrder.get("customer");
	    String customerName = jObjCustomer.get("name").toString();

	    clsCustomer objCustomer = new clsCustomer();
	    objCustomer.setStrCustomerName(customerName);

	    javax.swing.tree.DefaultMutableTreeNode nodeOrderNo = new javax.swing.tree.DefaultMutableTreeNode(external_order_id);
	    javax.swing.tree.DefaultMutableTreeNode nodeCustomerName = new javax.swing.tree.DefaultMutableTreeNode(objCustomer);
	    nodeOrderNo.add(nodeCustomerName);

	    javax.swing.tree.DefaultMutableTreeNode nodeItems = new javax.swing.tree.DefaultMutableTreeNode("Items");
	    JSONArray jArrCart = (JSONArray) jObjOrder.get("cart");

	    clsOrderDtl[] arrItems = new clsOrderDtl[jArrCart.size()];

	    for (int cart = 0; cart < jArrCart.size(); cart++)
	    {
		JSONObject jObjItem = (JSONObject) jArrCart.get(cart);
		String item = jObjItem.get("item_name").toString();

		clsOrderDtl objOrderDtl = new clsOrderDtl();
		objOrderDtl.setItemName(item);

		arrItems[cart] = objOrderDtl;
	    }
	    for (clsOrderDtl item : arrItems)
	    {
		javax.swing.tree.DefaultMutableTreeNode nodeItem = new javax.swing.tree.DefaultMutableTreeNode(item);
		nodeItems.add(nodeItem);
	    }

	    nodeOrderNo.add(nodeItems);
	    orderInfo.add(nodeOrderNo);
	}

	rooNodePendingOrders.add(orderInfo);
	treeAcceptedOrder.setModel(new javax.swing.tree.DefaultTreeModel(rooNodePendingOrders));
    }

    private void funRejectedOrderSelected()
    {
	    //
	JSONObject rootJSONObject = objWERAOnlineOrderIntegration.funAllRejectedOrder();
	mapRejectedOrderInfo.clear();

	JSONObject jObjDetails = (JSONObject) rootJSONObject.get("details");

	JSONArray jArrOrders = (JSONArray) jObjDetails.get("orders");

	javax.swing.tree.DefaultMutableTreeNode rooNodePendingOrders = new javax.swing.tree.DefaultMutableTreeNode("Rejected Orders");

	javax.swing.tree.DefaultMutableTreeNode orderInfo = new javax.swing.tree.DefaultMutableTreeNode("Order Info");

	newOrders = jArrOrders.size();

	for (int order = 0; order < jArrOrders.size(); order++)
	{
	    JSONObject jObjOrder = (JSONObject) jArrOrders.get(order);

	    JSONObject jObjOrderInfo = (JSONObject) jObjOrder.get("order_info");

	    String orderId = jObjOrderInfo.get("order_id").toString();
	     String external_order_id = jObjOrderInfo.get("external_order_id").toString();
	    mapWeraOrderExternalOrderNo.put(external_order_id, orderId);
	    //putting for nexr manupulation
	    mapRejectedOrderInfo.put(external_order_id, jObjOrder);

	    JSONObject jObjCustomer = (JSONObject) jObjOrder.get("customer");
	    String customerName = jObjCustomer.get("name").toString();

	    clsCustomer objCustomer = new clsCustomer();
	    objCustomer.setStrCustomerName(customerName);

	    javax.swing.tree.DefaultMutableTreeNode nodeOrderNo = new javax.swing.tree.DefaultMutableTreeNode(external_order_id);
	    javax.swing.tree.DefaultMutableTreeNode nodeCustomerName = new javax.swing.tree.DefaultMutableTreeNode(objCustomer);
	    nodeOrderNo.add(nodeCustomerName);

	    javax.swing.tree.DefaultMutableTreeNode nodeItems = new javax.swing.tree.DefaultMutableTreeNode("Items");
	    JSONArray jArrCart = (JSONArray) jObjOrder.get("cart");

	    clsOrderDtl[] arrItems = new clsOrderDtl[jArrCart.size()];

	    for (int cart = 0; cart < jArrCart.size(); cart++)
	    {
		JSONObject jObjItem = (JSONObject) jArrCart.get(cart);
		String item = jObjItem.get("item_name").toString();

		clsOrderDtl objOrderDtl = new clsOrderDtl();
		objOrderDtl.setItemName(item);

		arrItems[cart] = objOrderDtl;
	    }
	    for (clsOrderDtl item : arrItems)
	    {
		javax.swing.tree.DefaultMutableTreeNode nodeItem = new javax.swing.tree.DefaultMutableTreeNode(item);
		nodeItems.add(nodeItem);
	    }

	    nodeOrderNo.add(nodeItems);
	    orderInfo.add(nodeOrderNo);
	}

	rooNodePendingOrders.add(orderInfo);
	treeRejectedOrder.setModel(new javax.swing.tree.DefaultTreeModel(rooNodePendingOrders));
    }

    private void funPickedUpOrderSelected()
    {

	JSONObject rootJSONObject = objWERAOnlineOrderIntegration.funAllPickedUpOrder();
	mapPickedUpOrderInfo.clear();

	JSONObject jObjDetails = (JSONObject) rootJSONObject.get("details");

	JSONArray jArrOrders = (JSONArray) jObjDetails.get("orders");

	javax.swing.tree.DefaultMutableTreeNode rooNodePendingOrders = new javax.swing.tree.DefaultMutableTreeNode("PickedUp Orders");

	javax.swing.tree.DefaultMutableTreeNode orderInfo = new javax.swing.tree.DefaultMutableTreeNode("Order Info");

	newOrders = jArrOrders.size();

	for (int order = 0; order < jArrOrders.size(); order++)
	{
	    JSONObject jObjOrder = (JSONObject) jArrOrders.get(order);

	    JSONObject jObjOrderInfo = (JSONObject) jObjOrder.get("order_info");

	    String orderId = jObjOrderInfo.get("order_id").toString();
	    String external_order_id = jObjOrderInfo.get("external_order_id").toString();
	    mapWeraOrderExternalOrderNo.put(external_order_id, orderId);
	    //putting for nexr manupulation
	    mapPickedUpOrderInfo.put(external_order_id, jObjOrder);

	    JSONObject jObjCustomer = (JSONObject) jObjOrder.get("customer");
	    String customerName = jObjCustomer.get("name").toString();

	    clsCustomer objCustomer = new clsCustomer();
	    objCustomer.setStrCustomerName(customerName);

	    javax.swing.tree.DefaultMutableTreeNode nodeOrderNo = new javax.swing.tree.DefaultMutableTreeNode(external_order_id);
	    javax.swing.tree.DefaultMutableTreeNode nodeCustomerName = new javax.swing.tree.DefaultMutableTreeNode(objCustomer);
	    nodeOrderNo.add(nodeCustomerName);

	    javax.swing.tree.DefaultMutableTreeNode nodeItems = new javax.swing.tree.DefaultMutableTreeNode("Items");
	    JSONArray jArrCart = (JSONArray) jObjOrder.get("cart");

	    clsOrderDtl[] arrItems = new clsOrderDtl[jArrCart.size()];

	    for (int cart = 0; cart < jArrCart.size(); cart++)
	    {
		JSONObject jObjItem = (JSONObject) jArrCart.get(cart);
		String item = jObjItem.get("item_name").toString();

		clsOrderDtl objOrderDtl = new clsOrderDtl();
		objOrderDtl.setItemName(item);

		arrItems[cart] = objOrderDtl;
	    }
	    for (clsOrderDtl item : arrItems)
	    {
		javax.swing.tree.DefaultMutableTreeNode nodeItem = new javax.swing.tree.DefaultMutableTreeNode(item);
		nodeItems.add(nodeItem);
	    }

	    nodeOrderNo.add(nodeItems);
	    orderInfo.add(nodeOrderNo);
	}

	rooNodePendingOrders.add(orderInfo);
	treePickedUpOrder.setModel(new javax.swing.tree.DefaultTreeModel(rooNodePendingOrders));
    }

    private void funDeliveredOrderSelected()
    {

	JSONObject rootJSONObject = objWERAOnlineOrderIntegration.funAllDeliveredOrder();
	mapDeliveredOrderInfo.clear();

	JSONObject jObjDetails = (JSONObject) rootJSONObject.get("details");

	JSONArray jArrOrders = (JSONArray) jObjDetails.get("orders");

	javax.swing.tree.DefaultMutableTreeNode rooNodePendingOrders = new javax.swing.tree.DefaultMutableTreeNode("Delivered Orders");

	javax.swing.tree.DefaultMutableTreeNode orderInfo = new javax.swing.tree.DefaultMutableTreeNode("Order Info");

	newOrders = jArrOrders.size();

	for (int order = 0; order < jArrOrders.size(); order++)
	{
	    JSONObject jObjOrder = (JSONObject) jArrOrders.get(order);

	    JSONObject jObjOrderInfo = (JSONObject) jObjOrder.get("order_info");

	    String orderId = jObjOrderInfo.get("order_id").toString();
	    String external_order_id = jObjOrderInfo.get("external_order_id").toString();
	    mapWeraOrderExternalOrderNo.put(external_order_id, orderId);
	    //putting for nexr manupulation
	    mapDeliveredOrderInfo.put(external_order_id, jObjOrder);

	    JSONObject jObjCustomer = (JSONObject) jObjOrder.get("customer");
	    String customerName = jObjCustomer.get("name").toString();

	    clsCustomer objCustomer = new clsCustomer();
	    objCustomer.setStrCustomerName(customerName);

	    javax.swing.tree.DefaultMutableTreeNode nodeOrderNo = new javax.swing.tree.DefaultMutableTreeNode(external_order_id);
	    javax.swing.tree.DefaultMutableTreeNode nodeCustomerName = new javax.swing.tree.DefaultMutableTreeNode(objCustomer);
	    nodeOrderNo.add(nodeCustomerName);

	    javax.swing.tree.DefaultMutableTreeNode nodeItems = new javax.swing.tree.DefaultMutableTreeNode("Items");
	    JSONArray jArrCart = (JSONArray) jObjOrder.get("cart");

	    clsOrderDtl[] arrItems = new clsOrderDtl[jArrCart.size()];

	    for (int cart = 0; cart < jArrCart.size(); cart++)
	    {
		JSONObject jObjItem = (JSONObject) jArrCart.get(cart);
		String item = jObjItem.get("item_name").toString();

		clsOrderDtl objOrderDtl = new clsOrderDtl();
		objOrderDtl.setItemName(item);

		arrItems[cart] = objOrderDtl;
	    }
	    for (clsOrderDtl item : arrItems)
	    {
		javax.swing.tree.DefaultMutableTreeNode nodeItem = new javax.swing.tree.DefaultMutableTreeNode(item);
		nodeItems.add(nodeItem);
	    }

	    nodeOrderNo.add(nodeItems);
	    orderInfo.add(nodeOrderNo);
	}

	rooNodePendingOrders.add(orderInfo);
	treeDeliveredOrder.setModel(new javax.swing.tree.DefaultTreeModel(rooNodePendingOrders));
    }

    private void funAddUpdateMenuOrderSelected()
    {

    }

    private void funDownloadMenuSelected()
    {

	try
	{

	    JSONObject rootJSONObject = objWERAOnlineOrderIntegration.funCallDownloadMenuAPI();
	    if (rootJSONObject.size() > 0)
	    {
		if (rootJSONObject.containsKey("items"))
		{

		    StringBuilder sqlInsertBuilder = new StringBuilder();
		    sqlInsertBuilder.setLength(0);
		    sqlInsertBuilder.append("INSERT INTO tblonlinemenuimport "
			    + "(strItemName,strItemCode,strSubMenuHeadName,strSubMenuHeadCode,strGroupName"
			    + ",strGroupCode,strCostCenterName,strCostCenterCode,strMenuHeadName,strMenuHeadCode"
			    + ",strPOSCode,strSubGroupName,strSubGroupCode,strAreaName,strAreaCode"
			    + ",strExternalCode,dblRate,strIsModifier,strModifierCode,strModifierOnItemName"
			    + ",strModifierGroupName,strModifierGroupCode,min,max ) "
			    + "VALUES ");

		    JSONArray jArrItems = (JSONArray) rootJSONObject.get("items");

		    boolean isFirstRow = true;
		    for (int item = 0; item < jArrItems.size(); item++)
		    {
			JSONObject jObjItem = (JSONObject) jArrItems.get(item);

			String itemName = jObjItem.get("itemName").toString();
			String externalCode = jObjItem.get("itemId").toString();// posItemCode
			Matcher match = itemNamePatter.matcher(itemName);
			while (match.find())
			{
			    String s = match.group();
			    itemName = itemName.replaceAll("\\" + s, "");
			}

			String menuHeadName = jObjItem.get("menuHeadName").toString();
			String menuHeadId = jObjItem.get("menuHeadId").toString();
			boolean isBeverage=Boolean.parseBoolean(jObjItem.get("isBeverage").toString());
			String groupName = menuHeadName;
			if(isBeverage){
			    groupName="BEVERAGE";
			}else{
			    groupName="FOOD";
			}
			
			String subGroupName =menuHeadName;// jObjItem.get("subGroupName").toString();;
			String costCenterName = "KITCHEN";
			String posName = "All";
			String posCode = "All";
			String areaName = "All";
			double rate = Double.parseDouble(jObjItem.get("itemRate").toString());
//			if(externalCode.equals("I000270") ){
//			    
//			}else{
			    if (isFirstRow)
			    {
				sqlInsertBuilder.append("('" + itemName + "','"+externalCode+"','','','" + groupName + "'"
					+ ",'','" + costCenterName + "','','" + menuHeadName + "',''"
					+ ",'" + posCode + "','" + subGroupName + "','','" + areaName + "',''"
					+ ",'"+externalCode+"','" + rate + "','N','',''"
					+ ",'','','0','0')");
			    }
			    else
			    {
				sqlInsertBuilder.append(",('" + itemName + "','"+externalCode+"','','','" + groupName + "'"
					+ ",'','" + costCenterName + "','','" + menuHeadName + "',''"
					+ ",'" + posCode + "','" + subGroupName + "','','" + areaName + "',''"
					+ ",'"+externalCode+"','" + rate + "','N','',''"
					+ ",'','','0','0')");
			    }    
			//}
			

			isFirstRow = false;

			boolean isAddOnExists = Boolean.parseBoolean(jObjItem.get("isAddOnItemExists").toString());
			if (isAddOnExists)
			{
			    JSONArray jArrModifires = (JSONArray) jObjItem.get("modifiers");
			    for (int modifier = 0; modifier < jArrModifires.size(); modifier++)
			    {
				JSONObject jObjModifier = (JSONObject) jArrModifires.get(modifier);

				String modifierName = jObjModifier.get("modifierName").toString();

				Matcher modifierMatcher = itemNamePatter.matcher(modifierName);
				while (modifierMatcher.find())
				{
				    String s = modifierMatcher.group();
				    modifierName = modifierName.replaceAll("\\" + s, "");
				}

				modifierName = "-->" + modifierName;

				double modifierRate = Double.parseDouble(jObjModifier.get("modifierRate").toString());
				String modifierGroupName = jObjModifier.get("modifierGroupName").toString();
				int min = Integer.parseInt(jObjModifier.get("min").toString());
				int max = Integer.parseInt(jObjModifier.get("max").toString());

				String modifierOnItemName = itemName;
//				if(modifierName.equals("-->Veg Hakka Noodles") || modifierName.equals("-->Veg Fried Rice")){
//				    
//				}else{
				if (isFirstRow)
				{
				    sqlInsertBuilder.append("('" + modifierName + "','','','','" + groupName + "'"
					    + ",'','" + costCenterName + "','','" + menuHeadName + "',''"
					    + ",'" + posCode + "','" + subGroupName + "','','" + areaName + "',''"
					    + ",'','" + modifierRate + "','Y','','" + modifierOnItemName + "'"
					    + ",'" + modifierGroupName + "','','" + min + "','" + max + "')");
				}
				else
				{
				    sqlInsertBuilder.append(",('" + modifierName + "','','','','" + groupName + "'"
					    + ",'','" + costCenterName + "','','" + menuHeadName + "',''"
					    + ",'" + posCode + "','" + subGroupName + "','','" + areaName + "',''"
					    + ",'','" + modifierRate + "','Y','','" + modifierOnItemName + "'"
					    + ",'" + modifierGroupName + "','','" + min + "','" + max + "')");
				}
				//}
				

			    }
			}

		    }

		    if (!isFirstRow)
		    {
			clsGlobalVarClass.dbMysql.execute("truncate tblonlinemenuimport");

 			int affectedRows = clsGlobalVarClass.dbMysql.execute(sqlInsertBuilder.toString());
			System.out.println("affectedRows->" + affectedRows);
			if (affectedRows > 0)
			{
			    funFillDownloadMenuTable();
			}

		    }

		}
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}

    }

    private boolean funGenerateGroup()
    {
	boolean isSuccess = false;
	String query = "", code = "";
	long docNo = 0;
	try
	{
	    String sql = "select distinct(strGroupName) from tblonlinemenuimport";
	    ResultSet rsGroup = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rsGroup.next())
	    {

		String sqlNameCheck = " select a.strGroupCode from tblgrouphd a where a.strGroupName='" + rsGroup.getString(1) + "' ";
		ResultSet rsNameCheck = clsGlobalVarClass.dbMysql.executeResultSet(sqlNameCheck);
		if (!rsNameCheck.next())
		{
		    if (rsGroup.getString(1).trim().length() > 0)
		    {
			String docSql = " select ifnull(max(MID(a.strGroupCode,2,7)),'0' )as strGroupCode  from tblgrouphd a   ";
			ResultSet rsDocCode = clsGlobalVarClass.dbMysql.executeResultSet(docSql);

			if (rsDocCode.next())
			{
			    docNo = Long.parseLong(rsDocCode.getString(1)) + 1;
			    code = "G" + String.format("%07d", docNo);
			}
			else
			{
			    docNo++;
			    code = "G" + String.format("%07d", docNo);
			}
			query = "insert into tblgrouphd (strGroupCode,strGroupName,strUserCreated,"
				+ "strUserEdited,dteDateCreated,dteDateEdited,strClientCode)"
				+ "values('" + code + "','" + rsGroup.getString(1) + "','" + clsGlobalVarClass.gUserCode + "',"
				+ "'" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.getCurrentDateTime() + "',"
				+ "'" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.gClientCode + "')";
			int insert = clsGlobalVarClass.dbMysql.execute(query);
			if (insert == 1)
			{
			    query = "update tblonlinemenuimport set strGroupCode='" + code + "' "
				    + "where strGroupName='" + rsGroup.getString(1) + "'";
			    int update = clsGlobalVarClass.dbMysql.execute(query);
			}
		    }

		}
		else
		{
		    code = rsNameCheck.getString(1);
		    query = "update tblonlinemenuimport set strGroupCode='" + code + "' "
			    + "where strGroupName='" + rsGroup.getString(1) + "'";
		    int update = clsGlobalVarClass.dbMysql.execute(query);
		}

	    }
	    rsGroup.close();
	    isSuccess = true;
	}
	catch (Exception e)
	{
	    isSuccess = false;
	    e.printStackTrace();
	}
	finally
	{
	    return isSuccess;
	}
    }

    private boolean funGenerateSubGroup()
    {
	boolean isSuccess = false;
	String query = "", code = "";
	long docNo = 0;
	try
	{
	    //clsGlobalVarClass.dbMysql.execute("truncate table tblsubgrouphd");
	    String sql = "select distinct(strSubGroupName),strGroupCode from tblonlinemenuimport";
	    ResultSet rsSubGroup = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rsSubGroup.next())
	    {
		String sqlNameCheck = " select a.strSubGroupCode from tblsubgrouphd a where a.strSubGroupName='" + rsSubGroup.getString(1) + "' ";
		ResultSet rsNameCheck = clsGlobalVarClass.dbMysql.executeResultSet(sqlNameCheck);
		if (!rsNameCheck.next())
		{
		    if (rsSubGroup.getString(1).trim().length() > 0)
		    {
			String docSql = " select ifnull(max(MID(a.strSubGroupCode,3,7)),'0' )as strSubGroupCode  from tblsubgrouphd a   ";
			ResultSet rsDocCode = clsGlobalVarClass.dbMysql.executeResultSet(docSql);

			if (rsDocCode.next())
			{
			    docNo = Long.parseLong(rsDocCode.getString(1)) + 1;
			    code = "SG" + String.format("%07d", docNo);
			    rsDocCode.close();
			}
			else
			{
			    docNo++;
			    code = "SG" + String.format("%07d", docNo);
			}

			query = "insert into tblsubgrouphd (strSubGroupCode,strSubGroupName,strGroupCode,"
				+ "strUserCreated,strUserEdited,dteDateCreated,dteDateEdited,strClientCode)"
				+ "values('" + code + "','" + rsSubGroup.getString(1) + "','" + rsSubGroup.getString(2) + "',"
				+ "'" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "',"
				+ "'" + clsGlobalVarClass.getCurrentDateTime() + "','"
				+ clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.gClientCode + "')";
			int insert = clsGlobalVarClass.dbMysql.execute(query);
			if (insert == 1)
			{
			    query = "update tblonlinemenuimport set strSubGroupCode='" + code + "' "
				    + "where strSubGroupName='" + rsSubGroup.getString(1) + "'";
			    int update = clsGlobalVarClass.dbMysql.execute(query);
			}
		    }
		}
		else
		{
		    code = rsNameCheck.getString(1);
		    query = "update tblonlinemenuimport set strSubGroupCode='" + code + "' "
			    + "where strSubGroupName='" + rsSubGroup.getString(1) + "'";
		    int update = clsGlobalVarClass.dbMysql.execute(query);
		}
		rsNameCheck.close();
	    }
	    rsSubGroup.close();
	    isSuccess = true;

	}
	catch (Exception e)
	{
	    isSuccess = false;
	    e.printStackTrace();
	}
	finally
	{
	    return isSuccess;
	}
    }

    private boolean funGenerateCostCenter()
    {
	boolean isSuccess = false;
	String query = "", code = "";
	long docNo = 0;
	try
	{
	    String sql = "select distinct(strCostCenterName) from tblonlinemenuimport";
	    ResultSet rsCostCenter = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rsCostCenter.next())
	    {

		String sqlNameCheck = " select a.strCostCenterCode from tblCostCenterMaster a where a.strCostCenterName='" + rsCostCenter.getString(1) + "' ";
		ResultSet rsNameCheck = clsGlobalVarClass.dbMysql.executeResultSet(sqlNameCheck);
		if (!rsNameCheck.next())
		{
		    if (rsCostCenter.getString(1).trim().length() > 0)
		    {
			String docSql = " select ifnull(max(MID(a.strCostCenterCode,2,2)),'0' )as strCostCenterCode "
				+ " from tblCostCenterMaster a  ";
			ResultSet rsDocCode = clsGlobalVarClass.dbMysql.executeResultSet(docSql);

			if (rsDocCode.next())
			{
			    docNo = Long.parseLong(rsDocCode.getString(1)) + 1;
			    code = "C" + String.format("%02d", docNo);
			}
			else
			{
			    docNo++;
			    code = "C" + String.format("%02d", docNo);
			}

			query = "insert into tblCostCenterMaster (strCostCenterCode,strCostCenterName,strPrinterPort"
				+ ",strSecondaryPrinterPort,strUserCreated,strUserEdited,dteDateCreated,dteDateEdited"
				+ ",strClientCode,strDataPostFlag)"
				+ " values('" + code + "','" + rsCostCenter.getString(1) + "','','','" + clsGlobalVarClass.gUserCode + "',"
				+ "'" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.getCurrentDateTime() + "',"
				+ "'" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.gClientCode + "','N')";
			int insert = clsGlobalVarClass.dbMysql.execute(query);
			if (insert == 1)
			{
			    query = "update tblonlinemenuimport set strCostCenterCode='" + code + "' "
				    + "where strCostCenterName='" + rsCostCenter.getString(1) + "'";
			    int update = clsGlobalVarClass.dbMysql.execute(query);
			}
		    }
		}
		else
		{
		    code = rsNameCheck.getString(1);
		    query = "update tblonlinemenuimport set strCostCenterCode='" + code + "' "
			    + "where strCostCenterName='" + rsCostCenter.getString(1) + "'";
		    int update = clsGlobalVarClass.dbMysql.execute(query);
		}
	    }
	    rsCostCenter.close();
	    isSuccess = true;

	}
	catch (Exception e)
	{
	    isSuccess = false;
	    e.printStackTrace();
	}
	finally
	{
	    return isSuccess;
	}
    }

    private boolean funGenerateMenuHead()
    {
	boolean isSuccess = false;
	String query = "", code = "";
	long docNo = 0;
	try
	{
	    
	    String sql = "select distinct(strMenuHeadName) from tblonlinemenuimport";
	    ResultSet rsMenuHead = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    	while(rsMenuHead.next())
		{
		    String sqlNameCheck = "select a.strMenuCode from tblmenuhd a where a.strMenuName='" + rsMenuHead.getString(1) + "'";
		    ResultSet rsNameCheck = clsGlobalVarClass.dbMysql.executeResultSet(sqlNameCheck);
		    if (!rsNameCheck.next())
		    {
			if (rsMenuHead.getString(1).trim().length() > 0)
			{
			    String docSql = " select ifnull(max(MID(a.strMenuCode,2,6)),'0' )as strMenuCode  from tblmenuhd a ";
			    ResultSet rsDocCode = clsGlobalVarClass.dbMysql.executeResultSet(docSql);

			    if (rsDocCode.next())
			    {
				docNo = Long.parseLong(rsDocCode.getString(1)) + 1;
				code = "M" + String.format("%06d", docNo);;
			    }
			    else
			    {
				docNo++;
				code = "M" + String.format("%06d", docNo);
			    }
			    rsDocCode.close();

			    query = "insert into tblmenuhd (strMenuCode,strMenuName,strUserCreated,strUserEdited,"
				    + "dteDateCreated,dteDateEdited,strClientCode,strOperational,imgImage) "
				    + "values('" + code + "','" + rsMenuHead.getString(1) + "','" + clsGlobalVarClass.gUserCode + "'"
				    + ",'" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.getCurrentDateTime() + "'"
				    + ",'" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.gClientCode + "','Y','' )";
			    int insert = clsGlobalVarClass.dbMysql.execute(query);
			    if (insert == 1)
			    {
				query = "update tblonlinemenuimport set strMenuHeadCode='" + code + "' "
					+ "where strMenuHeadName='" + rsMenuHead.getString(1) + "'";
				int update = clsGlobalVarClass.dbMysql.execute(query);
			    }
			}

		    }
		    else
		    {
			code = rsNameCheck.getString(1);
			query = "update tblonlinemenuimport set strMenuHeadCode='" + code + "' "
				+ "where strMenuHeadName='" + rsMenuHead.getString(1) + "'";
			int update = clsGlobalVarClass.dbMysql.execute(query);
		    }
		    rsNameCheck.close();
		}
	    
	    rsMenuHead.close();

	    isSuccess = true;

	}
	catch (Exception e)
	{
	    isSuccess = false;
	    e.printStackTrace();
	}
	finally
	{
	    return isSuccess;
	}
    }

    private boolean funGenerateSubMenuHead()
    {
	boolean isSuccess = false;
	String query = "", code = "";
	long docNo = 0;
	try
	{
	    //clsGlobalVarClass.dbMysql.execute("truncate table tblsubmenuhead");
	    String sql = "select distinct(strSubMenuHeadName),strMenuHeadCode from tblonlinemenuimport";
	    ResultSet rsSubMenuHead = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rsSubMenuHead.next())
	    {
		String sqlNameCheck = "select a.strSubMenuHeadCode from tblsubmenuhead a where a.strSubMenuHeadName='" + rsSubMenuHead.getString(1) + "'";
		ResultSet rsNameCheck = clsGlobalVarClass.dbMysql.executeResultSet(sqlNameCheck);
		if (!rsNameCheck.next())
		{
		    if (rsSubMenuHead.getString(1).trim().length() > 0)
		    {
			String docSql = " select ifnull(max(MID(a.strSubMenuHeadCode,3,6)),'0' )as strSubMenuHeadCode  from tblsubmenuhead a ";
			ResultSet rsDocCode = clsGlobalVarClass.dbMysql.executeResultSet(docSql);

			if (rsDocCode.next())
			{
			    docNo = Long.parseLong(rsDocCode.getString(1)) + 1;
			    code = "SM" + String.format("%06d", docNo);
			}
			else
			{
			    docNo++;
			    code = "SM" + String.format("%06d", docNo);
			}

			query = "insert into tblsubmenuhead (strSubMenuHeadCode,strMenuCode,strSubMenuHeadShortName,"
				+ "strSubMenuHeadName,strSubMenuOperational,strUserCreated,strUserEdited,dteDateCreated,"
				+ "dteDateEdited,strClientCode)"
				+ " values('" + code + "','" + rsSubMenuHead.getString(2) + "',''"
				+ ",'" + rsSubMenuHead.getString(1).trim() + "','Y','" + clsGlobalVarClass.gUserCode + "'"
				+ ",'" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.getCurrentDateTime() + "'"
				+ ",'" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.gClientCode + "')";
			int insert = clsGlobalVarClass.dbMysql.execute(query);
			if (insert == 1)
			{
			    query = "update tblonlinemenuimport set strSubMenuHeadCode='" + code + "' "
				    + "where strSubMenuHeadName='" + rsSubMenuHead.getString(1) + "'";
			    int update = clsGlobalVarClass.dbMysql.execute(query);
			}
		    }
		}
		else
		{
		    code = rsNameCheck.getString(1);
		    query = "update tblonlinemenuimport set strSubMenuHeadCode='" + code + "' "
			    + "where strSubMenuHeadName='" + rsSubMenuHead.getString(1) + "'";
		    int update = clsGlobalVarClass.dbMysql.execute(query);
		}

		isSuccess = true;
	    }
	    rsSubMenuHead.close();
	}
	catch (Exception e)
	{
	    isSuccess = false;
	    e.printStackTrace();
	}
	finally
	{
	    return isSuccess;
	}
    }

    private boolean funGenerateAreaMaster()
    {
	boolean isSuccess = false;
	String query = "", code = "";
	long docNo = 0;
	try
	{

	    String sql = "select distinct(strAreaName) from tblonlinemenuimport";
	    ResultSet rsAreaMaster = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rsAreaMaster.next())
	    {

		String sqlNameCheck = " select a.strAreaCode from tblareamaster a where a.strAreaName='" + rsAreaMaster.getString(1) + "' ";
		ResultSet rsNameCheck = clsGlobalVarClass.dbMysql.executeResultSet(sqlNameCheck);
		if (!rsNameCheck.next())
		{
		    if (rsAreaMaster.getString(1).trim().length() > 0)
		    {
			String docSql = " select ifnull(max(MID(a.strAreaCode,2,3)),'0' )as strAreaCode from tblareamaster a  ";
			ResultSet rsDocCode = clsGlobalVarClass.dbMysql.executeResultSet(docSql);
			if (rsDocCode.next())
			{
			    docNo = Long.parseLong(rsDocCode.getString(1)) + 1;
			    code = "A" + String.format("%03d", docNo);
			}
			else
			{
			    docNo++;
			    code = "A" + String.format("%03d", docNo);
			}
			query = "insert into tblareamaster (strAreaCode,strAreaName,strUserCreated,strUserEdited,"
				+ "dteDateCreated,dteDateEdited)"
				+ "values('" + code + "','" + rsAreaMaster.getString(1) + "'"
				+ ",'" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "'"
				+ ",'" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "')";
			int insert = clsGlobalVarClass.dbMysql.execute(query);
			if (insert == 1)
			{
			    query = "update tblonlinemenuimport set strAreaCode='" + code + "' "
				    + "where strAreaName='" + rsAreaMaster.getString(1) + "'";
			    clsGlobalVarClass.dbMysql.execute(query);
			}
		    }
		}
		else
		{
		    code = rsNameCheck.getString(1);
		    query = "update tblonlinemenuimport set strAreaCode='" + code + "' "
			    + "where strAreaName='" + rsAreaMaster.getString(1) + "'";
		    clsGlobalVarClass.dbMysql.execute(query);
		}
	    }
	    rsAreaMaster.close();
	    query = "update tblinternal set dblLastNo=" + docNo + " where strTransactionType='Area'";
	    clsGlobalVarClass.dbMysql.execute(query);

	    isSuccess = true;

	}
	catch (Exception e)
	{
	    isSuccess = false;
	    e.printStackTrace();
	}
	finally
	{
	    return isSuccess;
	}
    }

    private boolean funGenerateItemMaster()
    {
	boolean isSuccess = false;
	String query = "", code = "", stkInEnable = "N", purchaseRate = "0.00", applyDiscount = "Y";
	long docNo = 0;
	try
	{

	    String sql = "select distinct(strItemName),strSubGroupCode,'N'StockInEnable,'0.00' PurchaseRate  "
		    + ",strExternalCode,''ItemDetails,'FOOD'ItemType,'Y'ApplyDiscount,''ShortName,''dblTax,'FOOD'RevenueHead "
		    + ",''ReceivedUOM,'N'RawMaterial,''RecipeUOM ,strItemCode "
		    + "from tblonlinemenuimport "
		    + "where strIsModifier='N' ";
	    ResultSet rsItemMaster = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    int i=0;
	    while(rsItemMaster.next())
		{
		    String sqlItemCodeCheck = "select a.strItemCode from tblitemmaster a where a.strItemCode='" + rsItemMaster.getString(15) + "'";
		    ResultSet rsItemCodeCheck = clsGlobalVarClass.dbMysql.executeResultSet(sqlItemCodeCheck);
		    if (rsItemCodeCheck.next())
		    {
			code = rsItemCodeCheck.getString(1);
			query = "update tblonlinemenuimport set strItemCode='" + code + "' "
				+ "where (strItemName='" + rsItemMaster.getString(1) + "' or strModifierOnItemName='" + rsItemMaster.getString(1) + "') ";
			int update = clsGlobalVarClass.dbMysql.execute(query);
		    }
		    else
		    {
		    	if (rsItemMaster.getString(1).trim().length() > 0)
			{
//			    String docSql = " select ifnull(max(MID(a.strItemCode,2,6)),'0' )as strItemCode  from tblitemmaster a  ";
//			    ResultSet rsDocCode = clsGlobalVarClass.dbMysql.executeResultSet(docSql);
//
//			    if (rsDocCode.next())
//			    {
//				docNo = Long.parseLong(rsDocCode.getString(1)) + 1;
//				code = "I" + String.format("%06d", docNo);
//			    }
//			    else
//			    {
//				docNo++;
//				code = "I" + String.format("%06d", docNo);
//			    }
			    
			    code= rsItemMaster.getString(15) ; //POS Item Code

			    if (rsItemMaster.getString(3).equals("Y"))
			    {
				stkInEnable = "Y";
			    }
			    if (rsItemMaster.getString(4).trim().length() == 0)
			    {
				purchaseRate = "0.00";
			    }
			    else
			    {
				purchaseRate = rsItemMaster.getString(4);
			    }

			    if (rsItemMaster.getString(8).trim().length() == 0)
			    {
				applyDiscount = "Y";
			    }
			    else
			    {
				applyDiscount = rsItemMaster.getString(8);
			    }

			    String rawMaterial = rsItemMaster.getString(13);
			    String itemForSale = "Y";
			    if (rawMaterial.equalsIgnoreCase("Yes") || rawMaterial.equalsIgnoreCase("Y"))
			    {
				rawMaterial = "Y";
				itemForSale = "Y";
			    }
			    else
			    {
				rawMaterial = "N";
				itemForSale = "N";
			    }
			    String recipeUOM = rsItemMaster.getString(14);

			    query = "insert into tblitemmaster (strItemCode,strItemName,strSubGroupCode,strTaxIndicator"
				    + ",strStockInEnable,dblPurchaseRate,strExternalCode,strItemDetails,strUserCreated"
				    + ",strUserEdited,dteDateCreated,dteDateEdited,strClientCode,strItemType,strDiscountApply"
				    + ",strShortName,strRevenueHead,strUOM,imgImage,strRawMaterial,strItemForSale,strRecipeUOM)"
				    + " values('" + code + "','" + rsItemMaster.getString(1) + "','" + rsItemMaster.getString(2) + "'"
				    + ",'" + rsItemMaster.getString(10) + "','" + stkInEnable + "','" + purchaseRate + "','" + rsItemMaster.getString(5) + "'"
				    + ",'" + rsItemMaster.getString(6) + "','" + clsGlobalVarClass.gUserCode + "'"
				    + ",'" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.getCurrentDateTime() + "'"
				    + ",'" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.gClientCode + "'"
				    + ",'" + rsItemMaster.getString(7) + "','" + applyDiscount + "','" + rsItemMaster.getString(9) + "'"
				    + ",'" + rsItemMaster.getString(11) + "','" + rsItemMaster.getString(12) + "','','" + rawMaterial + "'"
				    + ",'" + itemForSale + "','" + recipeUOM + "')";
			    int insert = clsGlobalVarClass.dbMysql.execute(query);
			    if (insert == 1)
			    {
				query = "update tblonlinemenuimport set strItemCode='" + code + "' "
					+ "where (strItemName='" + rsItemMaster.getString(1) + "' or strModifierOnItemName='" + rsItemMaster.getString(1) + "') ";
				int update = clsGlobalVarClass.dbMysql.execute(query);
			    }
			}
		    }
		}
	    
//	    while (rsItemMaster.next())
//	    {
//		String sqlNameCheck = "select a.strItemCode from tblitemmaster a where a.strItemName='" + rsItemMaster.getString(1) + "'";
//		ResultSet rsNameCheck = clsGlobalVarClass.dbMysql.executeResultSet(sqlNameCheck);
//		if (rsNameCheck.next())
//		{
//		    code = rsNameCheck.getString(1);
//		    query = "update tblonlinemenuimport set strItemCode='" + code + "' "
//			    + "where (strItemName='" + rsItemMaster.getString(1) + "' or strModifierOnItemName='" + rsItemMaster.getString(1) + "') ";
//		    int update = clsGlobalVarClass.dbMysql.execute(query);
//		}
//		else
//		{
//		}
//	    }
	    rsItemMaster.close();
	    isSuccess = true;

	}
	catch (Exception e)
	{
	    isSuccess = false;
	    e.printStackTrace();
	}
	finally
	{
	    return isSuccess;
	}
    }

    private boolean funGenerateMenuItemPriceHD()
    {
	boolean isSuccess = false;
	String query = "";
	try
	{
	    //clsGlobalVarClass.dbMysql.execute("truncate table tblmenuitempricinghd");
	    String sql = "select distinct(strMenuHeadCode),strMenuHeadName,strPOSCode from tblonlinemenuimport";
	    ResultSet rsMenuItemPriceHd = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rsMenuItemPriceHd.next())
	    {
		query = "insert into tblmenuitempricinghd(strPosCode,strMenuCode,strMenuName,strUserCreated"
			+ ",strUserEdited,dteDateCreated,dteDateEdited) "
			+ "values('" + rsMenuItemPriceHd.getString(3) + "','" + rsMenuItemPriceHd.getString(1) + "'"
			+ ",'" + rsMenuItemPriceHd.getString(2) + "','" + clsGlobalVarClass.gUserCode + "'"
			+ ",'" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.getCurrentDateTime() + "'"
			+ ",'" + clsGlobalVarClass.getCurrentDateTime() + "')";
		clsGlobalVarClass.dbMysql.execute(query);
	    }
	    rsMenuItemPriceHd.close();
	    isSuccess = true;

	}
	catch (Exception e)
	{
	    isSuccess = false;
	    e.printStackTrace();
	}
	finally
	{
	    return isSuccess;
	}
    }

    private boolean funGenerateMenuItemPriceDTL()
    {
	boolean isSuccess = false;
	String fromDate = "", toDate = "", priceMon = "", priceTue = "", priceWed = "", priceThu = "", priceFri = "", priceSat = "";
	String priceSun = "";
	Date dt = new Date();
	fromDate = (dt.getYear() + 1900) + "-" + (dt.getMonth() + 1) + "-" + dt.getDate() + " ";
	fromDate += dt.getHours() + ":" + dt.getMinutes() + ":" + dt.getSeconds();

	toDate = (dt.getYear() + 1901) + "-" + (dt.getMonth() + 1) + "-" + dt.getDate() + " ";
	toDate += dt.getHours() + ":" + dt.getMinutes() + ":" + dt.getSeconds();

	String query = "";
	try
	{
	    String sqlEmptyPricingTable = "delete b.* "
		    + "from tblonlinemenuimport a  "
		    + "join tblmenuitempricingdtl b on a.strItemCode=b.strItemCode "
		    + "where a.strIsModifier='N' ";
	    clsGlobalVarClass.dbMysql.execute(sqlEmptyPricingTable);

	    String sql = "select distinct(strItemCode),strItemName,strPOSCode,strMenuHeadCode"
		    + ",dblRate dblPriceMonday,dblRate dblPriceTuesday,dblRate dblPriceWednesday,dblRate dblPriceThursday,dblRate dblPriceFriday"
		    + ",dblRate dblPriceSaturday,dblRate dblPriceSunday,strCostCenterCode,strAreaCode,strSubMenuHeadCode "
		    + "from tblonlinemenuimport "
		    + "where strIsModifier='N' ";
	    ResultSet rsMenuItemPriceDtl = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rsMenuItemPriceDtl.next())
	    {
		if (rsMenuItemPriceDtl.getString(5).trim().length() == 0)
		{
		    priceMon = "0.00";
		}
		else
		{
		    priceMon = funFormatPrice(rsMenuItemPriceDtl.getString(5).trim());
		}

		if (rsMenuItemPriceDtl.getString(6).trim().length() == 0)
		{
		    priceTue = "0.00";
		}
		else
		{
		    priceTue = funFormatPrice(rsMenuItemPriceDtl.getString(6).trim());
		}

		if (rsMenuItemPriceDtl.getString(7).trim().length() == 0)
		{
		    priceWed = "0.00";
		}
		else
		{
		    priceWed = funFormatPrice(rsMenuItemPriceDtl.getString(7).trim());
		}

		if (rsMenuItemPriceDtl.getString(8).trim().length() == 0)
		{
		    priceThu = "0.00";
		}
		else
		{
		    priceThu = funFormatPrice(rsMenuItemPriceDtl.getString(8).trim());
		}

		if (rsMenuItemPriceDtl.getString(9).trim().length() == 0)
		{
		    priceFri = "0.00";
		}
		else
		{
		    priceFri = funFormatPrice(rsMenuItemPriceDtl.getString(9).trim());
		}

		if (rsMenuItemPriceDtl.getString(10).trim().length() == 0)
		{
		    priceSat = "0.00";
		}
		else
		{
		    priceSat = funFormatPrice(rsMenuItemPriceDtl.getString(10).trim());
		}

		if (rsMenuItemPriceDtl.getString(11).trim().length() == 0)
		{
		    priceSun = "0.00";
		}
		else
		{
		    priceSun = funFormatPrice(rsMenuItemPriceDtl.getString(11).trim());
		}

		query = "insert into tblmenuitempricingdtl(strItemCode,strItemName,strPosCode,strMenuCode"
			+ ",strPopular,strPriceMonday,strPriceTuesday,strPriceWednesday,strPriceThursday,strPriceFriday"
			+ ",strPriceSaturday,strPriceSunday,dteFromDate,dteToDate,tmeTimeFrom,strAMPMFrom,tmeTimeTo"
			+ ",strAMPMTo,strCostCenterCode,strTextColor,strUserCreated,strUserEdited,dteDateCreated"
			+ ",dteDateEdited,strAreaCode,strSubMenuHeadCode,strHourlyPricing,strClientCode) "
			+ "values('" + rsMenuItemPriceDtl.getString(1) + "','" + rsMenuItemPriceDtl.getString(2) + "'"
			+ ",'" + rsMenuItemPriceDtl.getString(3) + "','" + rsMenuItemPriceDtl.getString(4) + "'"
			+ ",'N','" + priceMon + "','" + priceTue + "'" + ",'" + priceWed + "','" + priceThu + "'" + ",'" + priceFri + "'"
			+ ",'" + priceSat + "'" + ",'" + priceSun + "'"
			+ ",'" + fromDate + "','" + toDate + "'  "
			+ ",'HH:MM', 'AM', 'HH:MM', 'AM','" + rsMenuItemPriceDtl.getString(12) + "','Black'"
			+ ",'" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "'"
			+ ",'" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "'"
			+ ",'" + rsMenuItemPriceDtl.getString(13) + "','" + rsMenuItemPriceDtl.getString(14) + "','No','" + clsGlobalVarClass.gClientCode + "')";
		int insert = clsGlobalVarClass.dbMysql.execute(query);
	    }
	    rsMenuItemPriceDtl.close();
	    isSuccess = true;

	}
	catch (Exception e)
	{
	    isSuccess = false;
	    e.printStackTrace();
	}
	finally
	{
	    return isSuccess;
	}
    }

    private String funFormatPrice(String price)
    {
	if (price.contains(","))
	{
	    price = price.replace(",", "");
	}
	return price;
    }

    private boolean funGenerateModifierGroup()
    {
	boolean isSuccess = false;
	String query = "", code = "";
	long docNo = 0;
	try
	{
	    String sql = "select distinct(strModifierGroupName),a.min,a.max "
		    + "from tblonlinemenuimport a "
		    + "where a.strIsModifier='Y' "
		    + "and a.strModifierGroupName<>'' ";
	    ResultSet rsGroup = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rsGroup.next())
	    {

		String sqlNameCheck = " select a.strModifierGroupCode from tblmodifiergrouphd a where a.strModifierGroupName='" + rsGroup.getString(1) + "' ";
		ResultSet rsNameCheck = clsGlobalVarClass.dbMysql.executeResultSet(sqlNameCheck);
		if (!rsNameCheck.next())
		{
		    if (rsGroup.getString(1).trim().length() > 0)
		    {
			String docSql = " select ifnull(max(MID(a.strModifierGroupCode,3,7)),'0' )as strGroupCode  from tblmodifiergrouphd a   ";
			ResultSet rsDocCode = clsGlobalVarClass.dbMysql.executeResultSet(docSql);

			if (rsDocCode.next())
			{
			    docNo = Long.parseLong(rsDocCode.getString(1)) + 1;
			    code = "MG" + String.format("%06d", docNo);
			}
			else
			{
			    docNo++;
			    code = "MG" + String.format("%06d", docNo);
			}
			String modShortName="";
			if(rsGroup.getString(1).trim().length()>13){
			     modShortName=rsGroup.getString(1).substring(0, 13);
			}else{
			    modShortName=rsGroup.getString(1);
			}
			
			String sqlinsert = "insert into tblmodifiergrouphd (strModifierGroupCode,strModifierGroupName,strModifierGroupShortName,strApplyMaxItemLimit,"
				+ "intItemMaxLimit,strOperational,strUserCreated,strUserEdited,dteDateCreated,dteDateEdited,strClientCode,strApplyMinItemLimit,intItemMinLimit,intSequenceNo) "
				+ "values ('" + code + "','" + rsGroup.getString(1) + "','" + modShortName + "','Y',"
				+ "'" + rsGroup.getInt(2) + "','Y','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "',"
				+ "'" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "'"
				+ ",'" + clsGlobalVarClass.gClientCode + "','Y','" + rsGroup.getInt(3) + "','1') ";
			int insert = clsGlobalVarClass.dbMysql.execute(sqlinsert);

			if (insert == 1)
			{
			    query = "update tblonlinemenuimport set strModifierGroupCode='" + code + "' "
				    + "where strModifierGroupName='" + rsGroup.getString(1) + "'";
			    int update = clsGlobalVarClass.dbMysql.execute(query);
			}
		    }

		}
		else
		{
		    code = rsNameCheck.getString(1);
		    query = "update tblonlinemenuimport set strModifierGroupCode='" + code + "' "
			    + "where strModifierGroupName='" + rsGroup.getString(1) + "'";
		    int update = clsGlobalVarClass.dbMysql.execute(query);
		}

	    }
	    rsGroup.close();
	    isSuccess = true;
	}
	catch (Exception e)
	{
	    isSuccess = false;
	    e.printStackTrace();
	}
	finally
	{
	    return isSuccess;
	}
    }

    private boolean funGenerateModifierMaster()
    {
	boolean isSuccess = false;
	String query = "", code = "";
	long docNo = 0;
	try
	{
	    String sql = "select strItemName,a.strItemCode,a.strModifierOnItemName,a.strModifierCode,a.strModifierGroupName,a.strModifierGroupCode,a.dblRate "
		    + "from tblonlinemenuimport a  "
		    + "where a.strIsModifier='Y' "
		    + "order by a.strItemName  ";
	    ResultSet rsGroup = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rsGroup.next())
	    {

		String sqlNameCheck = " select a.strModifierCode,a.strModifierName,a.strModifierGroupCode  "
			+ "from tblmodifiermaster a  "
			+ "where a.strModifierName='" + rsGroup.getString(1) + "' "
			+ "and a.strModifierGroupCode='" + rsGroup.getString(6) + "' ";
		ResultSet rsNameCheck = clsGlobalVarClass.dbMysql.executeResultSet(sqlNameCheck);
		if (!rsNameCheck.next())
		{
		    if (rsGroup.getString(1).trim().length() > 0)
		    {
			String docSql = " select ifnull(max(MID(a.strModifierCode,2,3)),'0' )as ModiCode  from tblmodifiermaster a   ";
			ResultSet rsDocCode = clsGlobalVarClass.dbMysql.executeResultSet(docSql);

			if (rsDocCode.next())
			{
			    docNo = Long.parseLong(rsDocCode.getString(1)) + 1;
			    code = "M" + String.format("%03d", docNo);
			}
			else
			{
			    docNo++;
			    code = "M" + String.format("%03d", docNo);
			}

			String sqlinsert = "insert into tblmodifiermaster(strModifierCode,strModifierName,strModifierDesc,"
				+ "strUserCreated,strUserEdited,dteDateCreated,dteDateEdited,strClientCode,strModifierGroupCode) "
				+ "values('" + code + "','" + rsGroup.getString(1) + "','" + rsGroup.getString(1) + "'"
				+ ",'" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "'"
				+ ",'" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "'"
				+ ",'" + clsGlobalVarClass.gClientCode + "','" + rsGroup.getString(6) + "')";
			int insert = clsGlobalVarClass.dbMysql.execute(sqlinsert);
			if (insert == 1)
			{
			    query = "update tblonlinemenuimport set strModifierCode='" + code + "' "
				    + "where strItemName='" + rsGroup.getString(1) + "' "
				    + "and strModifierGroupCode='" + rsGroup.getString(6) + "' ";
			    int update = clsGlobalVarClass.dbMysql.execute(query);
			}
		    }

		}
		else
		{
		    code = rsNameCheck.getString(1);
		    query = "update tblonlinemenuimport set strModifierCode='" + code + "' "
			    + "where strItemName='" + rsGroup.getString(1) + "' "
			    + "and strModifierGroupCode='" + rsGroup.getString(6) + "' ";
		    int update = clsGlobalVarClass.dbMysql.execute(query);
		}

	    }
	    rsGroup.close();
	    isSuccess = true;
	}
	catch (Exception e)
	{
	    isSuccess = false;
	    e.printStackTrace();
	}
	finally
	{
	    return isSuccess;
	}
    }

    private boolean funGenerateItemModifierMaster()
    {
	boolean isSuccess = false;
	String query = "", code = "";
	long docNo = 0;
	try
	{
	    query = "delete b.* "
		    + "from tblonlinemenuimport a "
		    + "join tblitemmodofier b on a.strItemCode=b.strItemCode and a.strModifierCode=b.strModifierCode ";
	    int deleted = clsGlobalVarClass.dbMysql.execute(query);

	    query = "INSERT into tblitemmodofier(select a.strItemCode,a.strModifierCode,'Y' Chargable,a.dblRate,'Y' Applicable,'N' DefaultModifier "
		    + "from tblonlinemenuimport a  "
		    + "where a.strIsModifier='Y' group by  a.strModifierCode)";
	    int inserted = clsGlobalVarClass.dbMysql.execute(query);

	    isSuccess = true;
	}
	catch (Exception e)
	{
	    isSuccess = false;
	    e.printStackTrace();
	}
	finally
	{
	    return isSuccess;
	}
    }

    private void funDownloadButtonClicked()
    {
	//code call for make bill and Make Kot form
	frmOkCancelPopUp okOb = new frmOkCancelPopUp(null, "Do you want to download menu?");
	okOb.setVisible(true);
	int res = okOb.getResult();
	if (res == 1)
	{
	    boolean isMenuDownloaded = funDownloadMenu();
	    //boolean isMenuDownloaded=funExportData();
	    if (isMenuDownloaded)
	    {
		frmOkPopUp objOkPopUp = new frmOkPopUp(this, "Menu Downloaded Successfully.", "Download Menu", 2);
		objOkPopUp.setVisible(true);
	    }
	    else
	    {
		frmOkPopUp objOkPopUp = new frmOkPopUp(this, "Unable to downloaded Menu.", "Download Menu", 2);
		objOkPopUp.setVisible(true);
	    }

	}
	return;

    }
    
   

    private boolean funDownloadMenu()
    {
	boolean isMenuDownloaded = false;
	try
	{
	    System.out.println("Groups created->" + (isMenuDownloaded = funGenerateGroup()));
	    if (isMenuDownloaded)
	    {
		System.out.println("Sub Groups created->" + (isMenuDownloaded = funGenerateSubGroup()));
	    }

	    if (isMenuDownloaded)
	    {
		System.out.println("Menu heads created->" + (isMenuDownloaded = funGenerateMenuHead()));
	    }

	    if (isMenuDownloaded)
	    {
		System.out.println("Sub menuheads created->" + (isMenuDownloaded = funGenerateSubMenuHead()));
	    }

	    if (isMenuDownloaded)
	    {
		System.out.println("cost centers created->" + (isMenuDownloaded = funGenerateCostCenter()));
	    }

	    if (isMenuDownloaded)
	    {
		System.out.println("Area created->" + (isMenuDownloaded = funGenerateAreaMaster()));
	    }

	    if (isMenuDownloaded)
	    {
		System.out.println("item masters created->" + (isMenuDownloaded = funGenerateItemMaster()));
	    }

	    if (isMenuDownloaded)
	    {
		System.out.println("Modifier Group masters created->" + (isMenuDownloaded = funGenerateModifierGroup()));
	    }

	    if (isMenuDownloaded)
	    {
		System.out.println("Modifier masters created->" + (isMenuDownloaded = funGenerateModifierMaster()));
	    }

	    if (isMenuDownloaded)
	    {
		System.out.println("Item Modifier Group masters created->" + (isMenuDownloaded = funGenerateItemModifierMaster()));
	    }
	    if (isMenuDownloaded)
	    {
		System.out.println("Item Pricing HD created->" + (isMenuDownloaded = funGenerateMenuItemPriceHD()));
	    }

	    if (isMenuDownloaded)
	    {
		System.out.println("Item pricing Dtl created->" + (isMenuDownloaded = funGenerateMenuItemPriceDTL()));
	    }

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	finally
	{
	    return isMenuDownloaded;
	}

    }

    private void funFillDownloadMenuTable()
    {
	try
	{
	    DefaultTableModel defaultTableModel = (DefaultTableModel) tblDownloadMenu.getModel();

	    defaultTableModel.setRowCount(0);

	    String sql = "select a.strItemName,a.strGroupName,a.strSubGroupName,a.strMenuHeadName "
		    + ",a.strCostCenterName,a.dblRate,a.strIsModifier,a.strModifierGroupName,a.min,a.max "
		    + "from  tblonlinemenuimport a ";
	    //+ "order by a.strItemCode,a.strIsModifier";
	    ResultSet rsDownloadMenu = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rsDownloadMenu.next())
	    {
		Object[] rowData =
		{
		    rsDownloadMenu.getString(1), rsDownloadMenu.getString(2), rsDownloadMenu.getString(3),
		    rsDownloadMenu.getString(4), rsDownloadMenu.getString(5), rsDownloadMenu.getString(6),
		    rsDownloadMenu.getString(7), rsDownloadMenu.getString(8), rsDownloadMenu.getString(9),
		    rsDownloadMenu.getString(10)
		};
		defaultTableModel.addRow(rowData);
	    }
	    rsDownloadMenu.close();

	    tblDownloadMenu.setModel(defaultTableModel);

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funAcceptTheOrder()
    {
	try
	{
	    funGenerateBillFromDirectBiller();
	    
	    String orderNo = mapWeraOrderExternalOrderNo.get(lblOrderNo.getText());
	 //   objWERAOnlineOrderIntegration.funCallAcceptTheOrder( orderNo,  60);
	    funResetOrderData();
	    objWERAOnlineOrderIntegration.funSendRequestForPendingOrders();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funRejectTheOrder()
    {
//	    Id Message
//	    1  Items out of stock.
//	    2  No delivery boys available.
//	    3  Nearing closing time
//	    4  Out of Subzone/Area
//	    5  Kitchen is Full

	String orderNo = mapWeraOrderExternalOrderNo.get(lblOrderNo.getText());
	int rejectionId = 1;

	try
	{
	    clsWERAOnlineOrderIntegration objOnlineOrderIntegration = new clsWERAOnlineOrderIntegration();
	    objOnlineOrderIntegration.funCallRejectTheOrder(orderNo, rejectionId);

	    funResetOrderData();
	    objWERAOnlineOrderIntegration.funSendRequestForPendingOrders();

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funRefreshManually()
    {
	objWERAOnlineOrderIntegration.funSendRequestForPendingOrders();
	if (strSelectedTab.equals("accecptedOrder"))//accecpted orders
	{
	    funAcceptedOrderSelected();
	}
	else if (strSelectedTab.equals("rejectedOrder"))//rejected orders
	{
	    funRejectedOrderSelected();
	}
	else if ( strSelectedTab.equals("pickedUpOrder"))//pickedUp orders
	{
	    funPickedUpOrderSelected();
	}
	else if (strSelectedTab.equals("deliverdedOrder"))//delverded orders
	{
	    funDeliveredOrderSelected();
	}
// 
    }

    private class TreeListener implements TreeSelectionListener
    {

	public void valueChanged(TreeSelectionEvent e)
	{
	    if(strSelectedTab.equalsIgnoreCase("newOrder")){
		Object o = treeOrders.getLastSelectedPathComponent();
		DefaultMutableTreeNode show = (DefaultMutableTreeNode) o;

		if (show != null)
		{
		    Object nodeObject = show.getUserObject();
		    if (nodeObject instanceof String)
		    {
			String orderNo = (String) show.getUserObject();
			//
			funSetOrderData(orderNo);
		    }
		}
	    }else if(strSelectedTab.equalsIgnoreCase("accecptedOrder")){
		Object o = treeAcceptedOrder.getLastSelectedPathComponent();
		DefaultMutableTreeNode show = (DefaultMutableTreeNode) o;

		if (show != null)
		{
		    Object nodeObject = show.getUserObject();
		    if (nodeObject instanceof String)
		    {
			String orderNo = (String) show.getUserObject();
			if(orderNo.equalsIgnoreCase("Items")){
			   // treeAcceptedOrder.collapsePath(new TreePath(show));
			}else{
			    funSetAcceptedOrderData(orderNo);
			}
		    }
		}
	    }else if(strSelectedTab.equalsIgnoreCase("rejectedOrder")){
		Object o = treeRejectedOrder.getLastSelectedPathComponent();
		DefaultMutableTreeNode show = (DefaultMutableTreeNode) o;

		if (show != null)
		{
		    Object nodeObject = show.getUserObject();
		    if (nodeObject instanceof String)
		    {
			String orderNo = (String) show.getUserObject();
			//
			funSetRejectedOrderData(orderNo);
		    }
		}
	    }else if(strSelectedTab.equalsIgnoreCase("pickedUpOrder")){
		Object o = treePickedUpOrder.getLastSelectedPathComponent();
		DefaultMutableTreeNode show = (DefaultMutableTreeNode) o;

		if (show != null)
		{
		    Object nodeObject = show.getUserObject();
		    if (nodeObject instanceof String)
		    {
			String orderNo = (String) show.getUserObject();
			//
			funSetPickedUpOrderData(orderNo);
		    }
		}
	    }else if(strSelectedTab.equalsIgnoreCase("deliverdedOrder")){
		Object o = treeDeliveredOrder.getLastSelectedPathComponent();
		DefaultMutableTreeNode show = (DefaultMutableTreeNode) o;

		if (show != null)
		{
		    Object nodeObject = show.getUserObject();
		    if (nodeObject instanceof String)
		    {
			String orderNo = (String) show.getUserObject();
			//
			funSetDeliveredOrderData(orderNo);
		    }
		}
	    }
	    
	}
    }

    private void funSetOrderData(String orderNo)
    {
	try
	{
	    if (mapOrderInfo.containsKey(orderNo))
	    {
		//
		funResetOrderData();

		JSONObject jObjOrder = mapOrderInfo.get(orderNo);

		JSONObject jObjOrderInfo = (JSONObject) jObjOrder.get("order_info");

		String orderId = jObjOrderInfo.get("order_id").toString();
		String external_order_id = jObjOrderInfo.get("external_order_id").toString();
	    
		lblOrderNo.setText(external_order_id);
		
		String orderType = "";
		if (jObjOrderInfo.containsKey("order_type"))
		{
		    orderType = jObjOrderInfo.get("order_type").toString();
		}
		lblOrderFromValue.setText(orderType.toUpperCase());

		JSONObject jObjCustomer = (JSONObject) jObjOrder.get("customer");
		String customerName = jObjCustomer.get("name").toString();
		String mobile = jObjCustomer.get("mobile").toString();
		String address = jObjCustomer.get("street").toString();

		lblCustomerName.setText(customerName);
		lblMobileNo.setText(mobile);
		lblAddress.setText(address);

		DefaultTableModel dtm = (DefaultTableModel) tblOrderItemDtl.getModel();

		JSONArray jArrCart = (JSONArray) jObjOrder.get("cart");
		double totalFinalAmt = 0.00,dblTotalDiscAmt=0.00,dblExtraCharges=0.00;
		for (int cart = 0; cart < jArrCart.size(); cart++)
		{
		    JSONObject jObjItem = (JSONObject) jArrCart.get(cart);
		    String itemName = jObjItem.get("item_name").toString();
		    String itemId = jObjItem.get("pos_item_id").toString();
		    Matcher match = itemNamePatter.matcher(itemName);
		    while (match.find())
		    {
			String s = match.group();
			itemName = itemName.replaceAll("\\" + s, "");
		    }

		    double itemAmount = Double.parseDouble(jObjItem.get("total").toString());//itemRate * itemQty;
		    double itemQty = Double.parseDouble(jObjItem.get("qty").toString());
		    double itemRate = itemAmount/itemQty; //Double.parseDouble(jObjItem.get("price").toString());
		    double itemDiscPer = 0.00;
		    double itemDiscAmt = 0.00;
		    double itemFinalAmt = itemAmount - itemDiscAmt;

		    Object[] row =
		    {
			itemName, itemQty, itemAmount, itemDiscPer, itemDiscAmt, itemFinalAmt, "",itemId
		    };

		    dtm.addRow(row);
		    totalFinalAmt += itemFinalAmt;
		    /**
		     * Add add-ons(Modifiers)
		     */
		    if (jObjItem.containsKey("sub_item"))
		    {
			JSONObject jObjSubItem = (JSONObject) jObjItem.get("sub_item");
			if(jObjSubItem != null){
			    if(jObjSubItem.containsKey("sub_item_content")){
				JSONArray jArrModifiers = (JSONArray) jObjSubItem.get("sub_item_content");
				for (int modifier = 0; modifier < jArrModifiers.size(); modifier++)
				{
				    JSONObject jObjModifier = (JSONObject) jArrModifiers.get(modifier);
				    if(jObjModifier.get("sub_item_name")!=null)
				    {
					String modifierName = jObjModifier.get("sub_item_name").toString();
					Matcher modifierNameMatch = itemNamePatter.matcher(modifierName);
					while (modifierNameMatch.find())
					{
					    String s = modifierNameMatch.group();
					    modifierName = modifierName.replaceAll("\\" + s, "");
					}
					modifierName = "-->" + modifierName;

					String modifierGroupName = jObjModifier.get("category_name").toString();
//					double dblModPrice=0;
//					if(!jObjModifier.get("price").toString().equals("false")){
//					    dblModPrice=Double.parseDouble(jObjModifier.get("price").toString());
//					}    
					double modifierRate = Double.parseDouble(jObjModifier.get("price").toString());
					double modifierQty = Double.parseDouble(jObjModifier.get("qty").toString());
					double modifierAmount = modifierRate * modifierQty;
					double modifierDiscPer = 0.00;
					double modifierDiscAmt = 0.00;
					double modifierFinalAmt = modifierAmount - modifierDiscAmt;

					Object[] modifierRow =
					{
					    modifierName, modifierQty, modifierAmount, modifierDiscPer, modifierDiscAmt, modifierFinalAmt, modifierGroupName,""
					};

					dtm.addRow(modifierRow);
					totalFinalAmt += modifierFinalAmt;
				    }
				    
				}
			    }
			}
		    }

		}
		JSONArray jArrDisc = (JSONArray) jObjOrder.get("discount");
		if(jArrDisc.size()>0){
		    for (int disc = 0; disc < jArrDisc.size(); disc++)
		    {
			JSONObject jObjDisc = (JSONObject) jArrDisc.get(disc);
			double dblDiscAmt=Double.parseDouble(jObjDisc.get("amount").toString());
			dblTotalDiscAmt=dblTotalDiscAmt+dblDiscAmt;
				
		    }
		    
		}
		lblFinalDiscAmt.setText(String.valueOf(dblTotalDiscAmt));
		
		JSONArray jArrExtra = (JSONArray) jObjOrder.get("extra");
		if(jArrExtra.size()>0){
		    for (int extra = 0; extra < jArrExtra.size(); extra++)
		    {
			JSONObject jObjExtra = (JSONObject) jArrExtra.get(extra);
			 double dblExtraAmt=0;
			if(jObjExtra.get("type").toString().equals("Delivery Charge") || jObjExtra.get("type").toString().equals("Packaging Charge")){
			     dblExtraAmt=Double.parseDouble(jObjExtra.get("amount").toString());
			}
			
			dblExtraCharges=dblExtraCharges+dblExtraAmt;
				
		    }
		    
		}
		lblTotalExtraAmt.setText(String.valueOf(dblExtraCharges));
		
		totalFinalAmt=totalFinalAmt+dblExtraCharges-dblTotalDiscAmt;
		lblTotalFinalAmtValue.setText(String.valueOf(totalFinalAmt));
		
		funFillOrderTable(tblOrderItemDtl,dtm);
	    }
	    else
	    {
		funResetOrderData();
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funSetAcceptedOrderData(String orderNo){
	try
	{
	    if (mapAcceptedOrderInfo.containsKey(orderNo))
	    {
		//
		funResetOrderData();

		JSONObject jObjOrder = mapAcceptedOrderInfo.get(orderNo);

		JSONObject jObjOrderInfo = (JSONObject) jObjOrder.get("order_info");

		String orderId = jObjOrderInfo.get("order_id").toString();
		String external_order_id = jObjOrderInfo.get("external_order_id").toString();
		lblOrderNo1.setText(external_order_id);

		String orderType = "";
		if (jObjOrderInfo.containsKey("order_type"))
		{
		    orderType = jObjOrderInfo.get("order_type").toString();
		}
		lblOrderFromValue1.setText(orderType.toUpperCase());

		JSONObject jObjCustomer = (JSONObject) jObjOrder.get("customer");
		String customerName = jObjCustomer.get("name").toString();
		String mobile = jObjCustomer.get("mobile").toString();
		String address = jObjCustomer.get("street").toString();

		lblCustomerName1.setText(customerName);
		lblMobileNo1.setText(mobile);
		lblAddress1.setText(address);

		 if(null!=jObjOrderInfo.get("rider_info")){
		    JSONObject jObjRiderDtl=(JSONObject)jObjOrderInfo.get("rider_info");
		    txtRiderName.setText(jObjRiderDtl.get("rider_name").toString());
		    txtRiderNo.setText(jObjRiderDtl.get("rider_phone_number").toString());
		}
		DefaultTableModel dtm = (DefaultTableModel) tblOrderItemDtl1.getModel();

		JSONArray jArrCart = (JSONArray) jObjOrder.get("cart");
		double totalFinalAmt = 0.00;
		for (int cart = 0; cart < jArrCart.size(); cart++)
		{
		    JSONObject jObjItem = (JSONObject) jArrCart.get(cart);
		    String itemName = jObjItem.get("item_name").toString();
		    String itemId = jObjItem.get("pos_item_id").toString();
		    Matcher match = itemNamePatter.matcher(itemName);
		    while (match.find())
		    {
			String s = match.group();
			itemName = itemName.replaceAll("\\" + s, "");
		    }

		    double itemAmount = Double.parseDouble(jObjItem.get("total").toString());//itemRate * itemQty;
		    double itemQty = Double.parseDouble(jObjItem.get("qty").toString());
		    double itemRate = itemAmount/itemQty; //Double.parseDouble(jObjItem.get("price").toString());
		    double itemDiscPer = 0.00;
		    double itemDiscAmt = 0.00;
		    double itemFinalAmt = itemAmount - itemDiscAmt;

		    Object[] row =
		    {
			itemName, itemQty, itemAmount, itemDiscPer, itemDiscAmt, itemFinalAmt, ""
		    };

		    dtm.addRow(row);
		    totalFinalAmt += itemFinalAmt;
		    /**
		     * Add add-ons(Modifiers)
		     */
		    if (jObjItem.containsKey("sub_item"))
		    {
			JSONObject jObjSubItem = (JSONObject) jObjItem.get("sub_item");
			if(jObjSubItem != null){
			    if(jObjSubItem.containsKey("sub_item_content")){
				JSONArray jArrModifiers = (JSONArray) jObjSubItem.get("sub_item_content");
				for (int modifier = 0; modifier < jArrModifiers.size(); modifier++)
				{
				    JSONObject jObjModifier = (JSONObject) jArrModifiers.get(modifier);
				    if(jObjModifier.get("sub_item_name")!=null){
					String modifierName = jObjModifier.get("sub_item_name").toString();
				    Matcher modifierNameMatch = itemNamePatter.matcher(modifierName);
				    while (modifierNameMatch.find())
				    {
					String s = modifierNameMatch.group();
					modifierName = modifierName.replaceAll("\\" + s, "");
				    }
				    modifierName = "-->" + modifierName;

				    String modifierGroupName = jObjModifier.get("category_name").toString();

				    double modifierRate = Double.parseDouble(jObjModifier.get("price").toString());
				    double modifierQty = Double.parseDouble(jObjModifier.get("qty").toString());
				    double modifierAmount = modifierRate * modifierQty;
				    double modifierDiscPer = 0.00;
				    double modifierDiscAmt = 0.00;
				    double modifierFinalAmt = modifierAmount - modifierDiscAmt;

				    Object[] modifierRow =
				    {
					modifierName, modifierQty, modifierAmount, modifierDiscPer, modifierDiscAmt, modifierFinalAmt, modifierGroupName
				    };

				    dtm.addRow(modifierRow);
				    totalFinalAmt += modifierFinalAmt;

				    }
				    				}
			    }
			}
		    }
		}
		lblTotalFinalAmtValue1.setText(String.valueOf(totalFinalAmt));

		funFillOrderTable(tblOrderItemDtl1,dtm);
	    }
	    else
	    {
		funResetOrderData();
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }
    
     private void funSetRejectedOrderData(String orderNo){
	try
	{
	    if (mapRejectedOrderInfo.containsKey(orderNo))
	    {
		//
		funResetOrderData();

		JSONObject jObjOrder = mapRejectedOrderInfo.get(orderNo);

		JSONObject jObjOrderInfo = (JSONObject) jObjOrder.get("order_info");

		String orderId = jObjOrderInfo.get("order_id").toString();
		String external_order_id = jObjOrderInfo.get("external_order_id").toString();
		lblOrderNo2.setText(external_order_id);

		String orderType = "";
		if (jObjOrderInfo.containsKey("order_type"))
		{
		    orderType = jObjOrderInfo.get("order_type").toString();
		}
		lblOrderFromValue2.setText(orderType.toUpperCase());

		JSONObject jObjCustomer = (JSONObject) jObjOrder.get("customer");
		String customerName = jObjCustomer.get("name").toString();
		String mobile = jObjCustomer.get("mobile").toString();
		String address = jObjCustomer.get("street").toString();

		lblCustomerName2.setText(customerName);
		lblMobileNo2.setText(mobile);
		lblAddress2.setText(address);

		DefaultTableModel dtm = (DefaultTableModel) tblOrderItemDtl2.getModel();

		JSONArray jArrCart = (JSONArray) jObjOrder.get("cart");
		double totalFinalAmt = 0.00;
		for (int cart = 0; cart < jArrCart.size(); cart++)
		{
		    JSONObject jObjItem = (JSONObject) jArrCart.get(cart);
		    String itemName = jObjItem.get("item_name").toString();
		    String itemId = jObjItem.get("pos_item_id").toString();
		    Matcher match = itemNamePatter.matcher(itemName);
		    while (match.find())
		    {
			String s = match.group();
			itemName = itemName.replaceAll("\\" + s, "");
		    }

		    double itemAmount = Double.parseDouble(jObjItem.get("total").toString());//itemRate * itemQty;
		    double itemQty = Double.parseDouble(jObjItem.get("qty").toString());
		    double itemRate = itemAmount/itemQty; //Double.parseDouble(jObjItem.get("price").toString());
		    double itemDiscPer = 0.00;
		    double itemDiscAmt = 0.00;
		    double itemFinalAmt = itemAmount - itemDiscAmt;

		    Object[] row =
		    {
			itemName, itemQty, itemAmount, itemDiscPer, itemDiscAmt, itemFinalAmt, ""
		    };

		    dtm.addRow(row);
		    totalFinalAmt += itemFinalAmt;
		    /**
		     * Add add-ons(Modifiers)
		     */
		    if (jObjItem.containsKey("sub_item"))
		    {
			JSONObject jObjSubItem = (JSONObject) jObjItem.get("sub_item");
		    if(jObjSubItem != null){
			if(jObjSubItem.containsKey("sub_item_content")){
			    JSONArray jArrModifiers = (JSONArray) jObjSubItem.get("sub_item_content");
			    for (int modifier = 0; modifier < jArrModifiers.size(); modifier++)
			    {
				JSONObject jObjModifier = (JSONObject) jArrModifiers.get(modifier);
				String modifierName = jObjModifier.get("sub_item_name").toString();
				Matcher modifierNameMatch = itemNamePatter.matcher(modifierName);
				while (modifierNameMatch.find())
				{
				    String s = modifierNameMatch.group();
				    modifierName = modifierName.replaceAll("\\" + s, "");
				}
				modifierName = "-->" + modifierName;

				String modifierGroupName = jObjModifier.get("category_name").toString();

				double modifierRate = Double.parseDouble(jObjModifier.get("price").toString());
				double modifierQty = Double.parseDouble(jObjModifier.get("qty").toString());
				double modifierAmount = modifierRate * modifierQty;
				double modifierDiscPer = 0.00;
				double modifierDiscAmt = 0.00;
				double modifierFinalAmt = modifierAmount - modifierDiscAmt;

				Object[] modifierRow =
				{
				    modifierName, modifierQty, modifierAmount, modifierDiscPer, modifierDiscAmt, modifierFinalAmt, modifierGroupName
				};

				dtm.addRow(modifierRow);
				totalFinalAmt += modifierFinalAmt;
			    }
			}
		    }
		    }

		}
		lblTotalFinalAmtValue2.setText(String.valueOf(totalFinalAmt));

		funFillOrderTable(tblOrderItemDtl2,dtm);
	    }
	    else
	    {
		funResetOrderData();
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }
     
     
      private void funSetPickedUpOrderData(String orderNo){
	try
	{
	    if (mapPickedUpOrderInfo.containsKey(orderNo))
	    {
		//
		funResetOrderData();

		JSONObject jObjOrder = mapPickedUpOrderInfo.get(orderNo);

		JSONObject jObjOrderInfo = (JSONObject) jObjOrder.get("order_info");

		String orderId = jObjOrderInfo.get("order_id").toString();
		String external_order_id = jObjOrderInfo.get("external_order_id").toString();
		lblOrderNo3.setText(external_order_id);

		String orderType = "";
		if (jObjOrderInfo.containsKey("order_type"))
		{
		    orderType = jObjOrderInfo.get("order_type").toString();
		}
		lblOrderFromValue3.setText(orderType.toUpperCase());

		JSONObject jObjCustomer = (JSONObject) jObjOrder.get("customer");
		String customerName = jObjCustomer.get("name").toString();
		String mobile = jObjCustomer.get("mobile").toString();
		String address = jObjCustomer.get("street").toString();

		lblCustomerName3.setText(customerName);
		lblMobileNo3.setText(mobile);
		lblAddress3.setText(address);

		DefaultTableModel dtm = (DefaultTableModel) tblOrderItemDtl3.getModel();

		JSONArray jArrCart = (JSONArray) jObjOrder.get("cart");
		double totalFinalAmt = 0.00;
		for (int cart = 0; cart < jArrCart.size(); cart++)
		{
		    JSONObject jObjItem = (JSONObject) jArrCart.get(cart);
		    String itemName = jObjItem.get("item_name").toString();
		    String itemId = jObjItem.get("pos_item_id").toString();
		    Matcher match = itemNamePatter.matcher(itemName);
		    while (match.find())
		    {
			String s = match.group();
			itemName = itemName.replaceAll("\\" + s, "");
		    }

		    double itemAmount = Double.parseDouble(jObjItem.get("total").toString());//itemRate * itemQty;
		    double itemQty = Double.parseDouble(jObjItem.get("qty").toString());
		    double itemRate = itemAmount/itemQty; //Double.parseDouble(jObjItem.get("price").toString());
		    double itemDiscPer = 0.00;
		    double itemDiscAmt = 0.00;
		    double itemFinalAmt = itemAmount - itemDiscAmt;

		    Object[] row =
		    {
			itemName, itemQty, itemAmount, itemDiscPer, itemDiscAmt, itemFinalAmt, ""
		    };

		    dtm.addRow(row);
		    totalFinalAmt += itemFinalAmt;
		    /**
		     * Add add-ons(Modifiers)
		     */
		    if (jObjItem.containsKey("sub_item"))
		    {
			JSONObject jObjSubItem = (JSONObject) jObjItem.get("sub_item");
		    if(jObjSubItem != null){
			if(jObjSubItem.containsKey("sub_item_content")){
			    JSONArray jArrModifiers = (JSONArray) jObjSubItem.get("sub_item_content");
			    for (int modifier = 0; modifier < jArrModifiers.size(); modifier++)
			    {
				JSONObject jObjModifier = (JSONObject) jArrModifiers.get(modifier);
				String modifierName = jObjModifier.get("sub_item_name").toString();
				Matcher modifierNameMatch = itemNamePatter.matcher(modifierName);
				while (modifierNameMatch.find())
				{
				    String s = modifierNameMatch.group();
				    modifierName = modifierName.replaceAll("\\" + s, "");
				}
				modifierName = "-->" + modifierName;

				String modifierGroupName = jObjModifier.get("category_name").toString();

				double modifierRate = Double.parseDouble(jObjModifier.get("price").toString());
				double modifierQty = Double.parseDouble(jObjModifier.get("qty").toString());
				double modifierAmount = modifierRate * modifierQty;
				double modifierDiscPer = 0.00;
				double modifierDiscAmt = 0.00;
				double modifierFinalAmt = modifierAmount - modifierDiscAmt;

				Object[] modifierRow =
				{
				    modifierName, modifierQty, modifierAmount, modifierDiscPer, modifierDiscAmt, modifierFinalAmt, modifierGroupName
				};

				dtm.addRow(modifierRow);
				totalFinalAmt += modifierFinalAmt;
			    }

			    
			}
		    }
		    }

		}
		lblTotalFinalAmtValue3.setText(String.valueOf(totalFinalAmt));

		funFillOrderTable(tblOrderItemDtl3,dtm);
	    }
	    else
	    {
		funResetOrderData();
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }
    
       private void funSetDeliveredOrderData(String orderNo){
	try
	{
	    if (mapDeliveredOrderInfo.containsKey(orderNo))
	    {
		//
		funResetOrderData();

		JSONObject jObjOrder = mapDeliveredOrderInfo.get(orderNo);

		JSONObject jObjOrderInfo = (JSONObject) jObjOrder.get("order_info");

		String orderId = jObjOrderInfo.get("order_id").toString();
		String external_order_id = jObjOrderInfo.get("external_order_id").toString();
		lblOrderNo4.setText(external_order_id);

		String orderType = "";
		if (jObjOrderInfo.containsKey("order_type"))
		{
		    orderType = jObjOrderInfo.get("order_type").toString();
		}
		lblOrderFromValue4.setText(orderType.toUpperCase());

		JSONObject jObjCustomer = (JSONObject) jObjOrder.get("customer");
		String customerName = jObjCustomer.get("name").toString();
		String mobile = jObjCustomer.get("mobile").toString();
		String address = jObjCustomer.get("street").toString();

		lblCustomerName4.setText(customerName);
		lblMobileNo4.setText(mobile);
		lblAddress4.setText(address);

		DefaultTableModel dtm = (DefaultTableModel) tblOrderItemDtl4.getModel();

		JSONArray jArrCart = (JSONArray) jObjOrder.get("cart");
		double totalFinalAmt = 0.00;
		for (int cart = 0; cart < jArrCart.size(); cart++)
		{
		    JSONObject jObjItem = (JSONObject) jArrCart.get(cart);
		    String itemName = jObjItem.get("item_name").toString();
		    
		    Matcher match = itemNamePatter.matcher(itemName);
		    while (match.find())
		    {
			String s = match.group();
			itemName = itemName.replaceAll("\\" + s, "");
		    }

		    double itemAmount = Double.parseDouble(jObjItem.get("total").toString());//itemRate * itemQty;
		    double itemQty = Double.parseDouble(jObjItem.get("qty").toString());
		    double itemRate = itemAmount/itemQty; //Double.parseDouble(jObjItem.get("price").toString());
		    double itemDiscPer = 0.00;
		    double itemDiscAmt = 0.00;
		    double itemFinalAmt = itemAmount - itemDiscAmt;

		    Object[] row =
		    {
			itemName, itemQty, itemAmount, itemDiscPer, itemDiscAmt, itemFinalAmt, ""
		    };

		    dtm.addRow(row);
		    totalFinalAmt += itemFinalAmt;
		    /**
		     * Add add-ons(Modifiers)
		     */
		    if (jObjItem.containsKey("sub_item"))
		    {
			JSONObject jObjSubItem = (JSONObject) jObjItem.get("sub_item");
			if(jObjSubItem != null){
			if(jObjSubItem.containsKey("sub_item_content")){
			    JSONArray jArrModifiers = (JSONArray) jObjSubItem.get("sub_item_content");
			    for (int modifier = 0; modifier < jArrModifiers.size(); modifier++)
			    {
				JSONObject jObjModifier = (JSONObject) jArrModifiers.get(modifier);

				String modifierName = jObjModifier.get("sub_item_name").toString();
				Matcher modifierNameMatch = itemNamePatter.matcher(modifierName);
				while (modifierNameMatch.find())
				{
				    String s = modifierNameMatch.group();
				    modifierName = modifierName.replaceAll("\\" + s, "");
				}
				modifierName = "-->" + modifierName;

				String modifierGroupName = jObjModifier.get("category_name").toString();

				double modifierRate = Double.parseDouble(jObjModifier.get("price").toString());
				double modifierQty = Double.parseDouble(jObjModifier.get("qty").toString());
				double modifierAmount = modifierRate * modifierQty;
				double modifierDiscPer = 0.00;
				double modifierDiscAmt = 0.00;
				double modifierFinalAmt = modifierAmount - modifierDiscAmt;

				Object[] modifierRow =
				{
				    modifierName, modifierQty, modifierAmount, modifierDiscPer, modifierDiscAmt, modifierFinalAmt, modifierGroupName
				};
				dtm.addRow(modifierRow);
				totalFinalAmt += modifierFinalAmt;
			    }
			}
			}
		    }
		}
		lblTotalFinalAmtValue4.setText(String.valueOf(totalFinalAmt));
		funFillOrderTable(tblOrderItemDtl4,dtm);
	    }
	    else
	    {
		funResetOrderData();
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }
    
    private void funResetOrderData()
    {
	lblOrderNo.setText("");
	lblCustomerName.setText("");
	lblMobileNo.setText("");
	lblAddress.setText("");
	lblTotalFinalAmtValue.setText("0.00");
	lblOrderFromValue.setText("");
	lblFinalDiscAmt.setText("0.00");
	lblTotalExtraAmt.setText("0.00");
	
	lblOrderNo1.setText("");
	lblCustomerName1.setText("");
	lblMobileNo1.setText("");
	lblAddress1.setText("");
	lblTotalFinalAmtValue1.setText("0.00");
	lblOrderFromValue1.setText("");
	
	lblOrderNo2.setText("");
	lblCustomerName2.setText("");
	lblMobileNo2.setText("");
	lblAddress2.setText("");
	lblTotalFinalAmtValue2.setText("0.00");
	lblOrderFromValue2.setText("");
	
	
	lblOrderNo3.setText("");
	lblCustomerName3.setText("");
	lblMobileNo3.setText("");
	lblAddress3.setText("");
	lblTotalFinalAmtValue3.setText("0.00");
	lblOrderFromValue3.setText("");
	
	lblOrderNo4.setText("");
	lblCustomerName4.setText("");
	lblMobileNo4.setText("");
	lblAddress4.setText("");
	lblTotalFinalAmtValue4.setText("0.00");
	lblOrderFromValue4.setText("");
	
	txtRiderName.setText("");
	txtRiderNo.setText("");
	
	DefaultTableModel dm = (DefaultTableModel) tblOrderItemDtl.getModel();
	dm.setRowCount(0);
	dm = (DefaultTableModel) tblOrderItemDtl1.getModel();
	dm.setRowCount(0);
	dm = (DefaultTableModel) tblOrderItemDtl2.getModel();
	dm.setRowCount(0);
	dm = (DefaultTableModel) tblOrderItemDtl3.getModel();
	dm.setRowCount(0);
	dm = (DefaultTableModel) tblOrderItemDtl4.getModel();
	dm.setRowCount(0);

    }

    private void funFillOrderTable(JTable tblOrderItemDtl,DefaultTableModel dtm)
    {

	
	tblOrderItemDtl.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	tblOrderItemDtl.setRowHeight(40);
	tblOrderItemDtl.setRowMargin(5);

	tblOrderItemDtl.getColumnModel().getColumn(0).setPreferredWidth(440);
	tblOrderItemDtl.getColumnModel().getColumn(1).setPreferredWidth(75);
	tblOrderItemDtl.getColumnModel().getColumn(2).setPreferredWidth(100);
	tblOrderItemDtl.getColumnModel().getColumn(3).setPreferredWidth(75);
	tblOrderItemDtl.getColumnModel().getColumn(4).setPreferredWidth(75);
	tblOrderItemDtl.getColumnModel().getColumn(5).setPreferredWidth(100);
	tblOrderItemDtl.getColumnModel().getColumn(6).setPreferredWidth(0);
	if(tblOrderItemDtl.getColumnCount()==8){
	    tblOrderItemDtl.getColumnModel().getColumn(7).setPreferredWidth(0);
	}
	
	DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
	leftRenderer.setHorizontalAlignment(JLabel.LEFT);
	leftRenderer.setBackground(Color.WHITE);

	DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
	rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
	rightRenderer.setBackground(Color.WHITE);

	tblOrderItemDtl.getColumnModel().getColumn(0).setCellRenderer(leftRenderer);
	tblOrderItemDtl.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
	tblOrderItemDtl.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
	tblOrderItemDtl.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
	tblOrderItemDtl.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
	tblOrderItemDtl.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);

	tblOrderItemDtl.setModel(dtm);
    }

    private void funSaveButtonClicked()
    {
	frmAcceptRejectPopUp objAcceptRejectPopUp = new frmAcceptRejectPopUp(null, "Do want to accept the order?");
	objAcceptRejectPopUp.setVisible(true);
	int res = objAcceptRejectPopUp.getResult();
	if (res == 1)
	{
	    funAcceptTheOrder();
	}
	else
	{
	    funRejectTheOrder();
	}
    }

    private void funPickedUpOrder(){
	if(tblOrderItemDtl1.getRowCount()>0){
	    String orderNo=mapWeraOrderExternalOrderNo.get(lblOrderNo1.getText());
	    String riderName=txtRiderName.getText();
	    String riderNo=txtRiderNo.getText();
	    if(riderName.isEmpty() && riderNo.isEmpty()){
		new frmOkPopUp(this, "Enter Rider Details", "Error", 0).setVisible(true);
	    }else{
		 objWERAOnlineOrderIntegration.funCallPickedUpTheOrder(orderNo, riderName, riderNo);
		 funResetOrderData();
	    }
	   
	}else{
	    new frmOkPopUp(this, "First Select The Order ", "Error", 0).setVisible(true);
	}
	
    }
    private void funDeliveredOrder(){
	if(tblOrderItemDtl3.getRowCount()>0){
	    String orderNo=mapWeraOrderExternalOrderNo.get(lblOrderNo3.getText());
	    if(orderNo.isEmpty()){
		new frmOkPopUp(this, "Select Order", "Error", 0).setVisible(true);
	    }else{
		 objWERAOnlineOrderIntegration.funCallDeliveredOrder(orderNo);
		 funResetOrderData();
	    }
	   
	}else{
	    new frmOkPopUp(this, "Select Order First", "Error", 0).setVisible(true);
	}
    }
    
    private void funGenerateBillFromDirectBiller()
    {
	try
	{
	    List<clsDirectBillerItemDtl> listDirectBillerItemDtl = new ArrayList<>();
	    clsGlobalVarClass.gTransactionType = "Direct Biller";
	    frmDirectBiller objDirectBiller = new frmDirectBiller();

	    boolean isItemPresent = true,isFirstRow=true;
	    String parentItemCode = "";
	    double dblModSeqNo=0;
	    int intSeqNo=0;
	    for (int row = 0; row < tblOrderItemDtl.getRowCount(); row++)
	    {
		
		String itemName = tblOrderItemDtl.getValueAt(row, 0).toString();
		String itemCode="";
		if (tblOrderItemDtl.getValueAt(row, 7) != null && tblOrderItemDtl.getValueAt(row, 7).toString().trim().length() > 0)
		{
		    if(isFirstRow){
			isFirstRow=false;
		    }else{
			intSeqNo++;
		    }
		    dblModSeqNo=0;
		    itemCode=tblOrderItemDtl.getValueAt(row, 7).toString();
		    
		}
		String modifierGroupName = "";
		boolean isModifier = false;
		if (tblOrderItemDtl.getValueAt(row, 6) != null && tblOrderItemDtl.getValueAt(row, 6).toString().trim().length() > 0)
		{
		    modifierGroupName = tblOrderItemDtl.getValueAt(row, 6).toString();
		    isModifier=true;
		    if(dblModSeqNo>intSeqNo){
			dblModSeqNo=dblModSeqNo+0.01;
		    }else{
			 dblModSeqNo=intSeqNo+0.01;
		    }
		   
		}

		//String itemCode = funGetItemCode(itemName, modifierGroupName,strItemCode);
		//(String strItemCode,String itemName,String modfireGroup){
		 itemCode=funCheckItemCode(itemCode,itemName,modifierGroupName);
		if (tblOrderItemDtl.getValueAt(row, 6)== null || tblOrderItemDtl.getValueAt(row, 6).toString().trim().isEmpty())
		{
		    parentItemCode = itemCode;
		}
		String modifierCode="";
		if (tblOrderItemDtl.getValueAt(row, 6) != null && tblOrderItemDtl.getValueAt(row, 6).toString().trim().length() > 0)
		{
		    modifierCode=itemCode;
		    itemCode=parentItemCode+modifierCode;
		}

		if (itemCode == null || itemCode.trim().isEmpty())
		{
		    isItemPresent = false;
		    break;
		}
		double qty = Double.parseDouble(tblOrderItemDtl.getValueAt(row, 1).toString());
		double amount = Double.parseDouble(tblOrderItemDtl.getValueAt(row, 2).toString());
		double itemRate = amount / qty;
		double discPer = Double.parseDouble(tblOrderItemDtl.getValueAt(row, 3).toString());
		double discAmt = Double.parseDouble(tblOrderItemDtl.getValueAt(row, 4).toString());
		double finalAmt = Double.parseDouble(tblOrderItemDtl.getValueAt(row, 5).toString());

		String strSeq=String.valueOf(row);
		DecimalFormat two = new DecimalFormat("0.00");
		if(itemCode.length()>8){
		    strSeq=two.format(dblModSeqNo);
		}else{
		    strSeq=String.valueOf(intSeqNo);
		}

		clsDirectBillerItemDtl objDirectBillerItemDtl = new clsDirectBillerItemDtl(itemName, itemCode, qty, amount,isModifier,modifierCode, "N", "", itemRate, "", strSeq, itemRate);
		listDirectBillerItemDtl.add(objDirectBillerItemDtl);
		
	    }
	    double dblDiscAmt=Double.parseDouble(lblFinalDiscAmt.getText());
	    double dblExtraAmt=Double.parseDouble(lblTotalExtraAmt.getText());
	    if (isItemPresent)
	    {
		objDirectBiller.setObj_List_ItemDtl(listDirectBillerItemDtl);
		objDirectBiller.setDblDiscountAmt(dblDiscAmt);
		objDirectBiller.setHomeDelCharges(dblExtraAmt);
		frmBillSettlement objBillSettlement = new frmBillSettlement(objDirectBiller, "WERAOnlineFood" + "!" +"OrderFrom"+lblOrderFromValue.getText()+" "+lblOrderNo.getText());//+"#"+String.valueOf(dblDiscAmt)
		objBillSettlement.setVisible(true);
		
	    }
	    else
	    {
		new frmOkPopUp(this, "Item not available", "Error", 0).setVisible(true);
		return;
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private String funCheckItemCode(String strItemCode,String itemName,String modfireGroup){
	String itemCode = "";
	try{
	    ResultSet rsItemCode =null;
	    if (itemName.startsWith("-->"))
	    {
		String sql = "select b.strModifierCode,b.strModifierName "
			+ "from tblmodifiergrouphd a,tblmodifiermaster b "
			+ "where a.strModifierGroupCode=b.strModifierGroupCode "
			+ "and a.strModifierGroupName='" + modfireGroup + "' "
			+ "and b.strModifierName='" + itemName + "' ";
		rsItemCode = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		 if(rsItemCode.next()){
		    itemCode=rsItemCode.getString(1);
		}else{
		     itemCode="M99";
		 }
	    }else{
		String sql = "select strItemCode from tblitemmaster where strItemCode='"+strItemCode+"'";
		rsItemCode = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		 if(rsItemCode.next()){
		    itemCode=rsItemCode.getString(1);
		}	
	    }
	   
	     rsItemCode.close();
	}catch(Exception e){
	    e.printStackTrace();
	}
	
	return itemCode;
    }
    private String funGetItemCode(String itemName, String modifierGroupName,String externalCode)
    {
	String itemCode = "";
	try
	{
	    ResultSet rsItemCode = null;
	    if (itemName.startsWith("-->"))
	    {
		String sql = "select b.strModifierCode,b.strModifierName "
			+ "from tblmodifiergrouphd a,tblmodifiermaster b "
			+ "where a.strModifierGroupCode=b.strModifierGroupCode "
			+ "and a.strModifierGroupName='" + modifierGroupName + "' "
			+ "and b.strModifierName='" + itemName + "' ";
		rsItemCode = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    }
	    else
	    {
		String sql="";
		if(externalCode!="" ||externalCode.length()>0){
		    sql = "select a.strItemCode  "
			+ "from tblitemmaster a "
			+ "where a.strExternalCode='" + externalCode + "' ";
		}else{
		    sql = "select a.strItemCode  "
			+ "from tblmenuitempricingdtl a "
			+ "where a.strItemName='" + itemName + "' ";
		}
		
		rsItemCode = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    }
	    if (rsItemCode.next())
	    {
		itemCode = rsItemCode.getString(1);
	    }
	    rsItemCode.close();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	finally
	{
	    return itemCode;
	}
    }

}

class CustomTreeCellRenderer implements TreeCellRenderer
{

    private JLabel label;

    CustomTreeCellRenderer()
    {

    }

    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus)
    {
	Object o = ((DefaultMutableTreeNode) value).getUserObject();
	tree.expandRow(row);
	if (o instanceof clsCustomer)
	{
	    label = new JLabel();
	    clsCustomer country = (clsCustomer) o;
	    URL imageUrl = getClass().getResource("/com/POSTransaction/images/imgOrderCustomer.png");
	    if (imageUrl != null)
	    {

		label.setIcon(new ImageIcon(imageUrl));
	    }
	    label.setText(country.getStrCustomerName());
	}
	else if (o instanceof String)
	{
	    label = new JLabel();
	    label.setIcon(null);
	    label.setText("" + value);
	}
	else if (o instanceof clsOrderDtl)
	{
	    label = new JLabel();
	    clsOrderDtl orderDtl = (clsOrderDtl) o;
	    URL imageUrl = getClass().getResource("/com/POSTransaction/images/imgOrderedItem.png");
	    if (imageUrl != null)
	    {
		label.setIcon(new ImageIcon(imageUrl));
	    }
	    label.setText(orderDtl.getItemName());
	}
	return label;
    }
}
