/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSTransaction.view;

import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsItemPriceDtl;
import com.POSGlobal.controller.clsPLUItemDtl;
import com.POSGlobal.controller.clsUtility;
import com.POSGlobal.view.frmAlfaNumericKeyBoard;
import com.POSGlobal.view.frmNumberKeyPad;
import com.POSGlobal.view.frmNumericKeyboard;
import com.POSGlobal.view.frmOkCancelPopUp;
import com.POSGlobal.view.frmOkPopUp;
import com.POSGlobal.view.frmSearchFormDialog;
import com.POSTransaction.controller.clsCharacteristics;
import com.POSTransaction.controller.clsCustomerDataModelForSQY;
import static com.POSTransaction.view.frmAdvanceOrder.lblCustInfo;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.sql.Blob;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.ActionMap;
import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.CANCEL_OPTION;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.RowFilter;
import javax.swing.Timer;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class frmAdvanceOrder extends javax.swing.JFrame
{

    private int menuCount, nextCnt, limit, count;
    private String orderdate = "";
    private static int nextClick;
    String[] menuNames, menuNames1, dayPrice =
    {
        "strPriceSunday", "strPriceMonday", "strPriceTuesday", "strPriceWednesday", "strPriceThursday", "strPriceFriday", "strPriceSaturday"
    }, itemNames;
    BigDecimal netTotalAmt, subTotalAmt, grandTotalAmt, totalAmt, discountAmt, discountPer;
    private String sql, menuHeadCode, posDate, customerCode;
    int rowNumber, nextItemClick = 0;
    int serailNo = 1, requiredDays = 0;
    private boolean flgPopular, flgModifyAdvOrder;
    private ArrayList<String> listTopButtonName, listTopButtonCode;
    private int itemNumber, totalItems;
    private String strDeliveryTime, waiterNo, homeDelivery, noDeliverDays;
    private Map hmAdvOrderType;
    private double selectedQty, itemIncrWgt, itemMinWgt;
    private boolean flgChangeQty;
    private ArrayList<String> list_ItemNames_Buttoms;
    private List<clsItemPriceDtl> obj_List_ItemPrice;
    private clsCustomerDataModelForSQY objData;
    panelModifier objPanelModifier;
    private static File tempFile;
    private File destFile;
    private String filePath;
    private File fileImageSpecialSymbol;
    private String CANCEL_ACTION = "cancel-search";
    clsUtility objUtility;
    private Map<String, List<String>> hmExtraParam;
    private Map<String, List<String>> hmChar;
    //private Map<String,List<String>> hmValueChar;
    private Map<String, Map<String, clsCharacteristics>> hmValueChar;
    private Map<String, Map<String, clsCharacteristics>> hmTextChar;
    private Date dtCalOrderDate;
    private Map<String, File> hmAttachedImage;
    private List<clsCharacteristics> listCharactersticsValue;

    /**
     * This method is used to initialize Advance Order Form
     */
    public frmAdvanceOrder()
    {
        initComponents();
        this.setLocationRelativeTo(null);
        try
        {
            lblHItemCode.setVisible(false);
            lblHCharCode.setVisible(false);
            lblHItemName.setVisible(false);
            objUtility = new clsUtility();
            hmExtraParam = new HashMap<String, List<String>>();
            hmChar = new HashMap<String, List<String>>();
            hmValueChar = new HashMap<String, Map<String, clsCharacteristics>>();
            hmTextChar = new HashMap<String, Map<String, clsCharacteristics>>();
            hmAttachedImage = new HashMap<String, File>();
            fun_isVisiblePanels(true);
            noDeliverDays = "";
            waiterNo = "";
            homeDelivery = "N";
            flgPopular = false;
            btnNextMenu.setEnabled(false);
            btnPrevMenu.setEnabled(false);
            this.setName("AdvanceOrder");
            selectedQty = 1;
            itemIncrWgt = 0;
            itemMinWgt = 0;

            DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
            rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
            tblItemTable.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
            tblItemTable.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
            tblItemTable.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
            tblItemTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            tblItemTable.getColumnModel().getColumn(0).setPreferredWidth(170);
            tblItemTable.getColumnModel().getColumn(1).setPreferredWidth(40);
            tblItemTable.getColumnModel().getColumn(2).setPreferredWidth(83);
            tblItemTable.getColumnModel().getColumn(3).setPreferredWidth(50);
            tblItemTable.setShowHorizontalLines(true);

            java.util.Date dt1 = new java.util.Date();
            String bdte = clsGlobalVarClass.gPOSStartDate;
            SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date bDate = dFormat.parse(bdte);

            String date1 = (bDate.getYear() + 1900) + "-" + (bDate.getMonth() + 1) + "-" + bDate.getDate();
            String time = dt1.getHours() + ":" + dt1.getMinutes() + ":" + dt1.getSeconds();
            posDate = date1 + " " + time;
            lblUserCode.setText(clsGlobalVarClass.gUserCode);
            lblPosName.setText(clsGlobalVarClass.gPOSName);
            lblModuleName.setText(clsGlobalVarClass.gSelectedModule);
            int day = dt1.getDate();
            int month = dt1.getMonth() + 1;
            int year = dt1.getYear() + 1900;
            String dte = day + "-" + month + "-" + year;
            dte = bDate.getDate() + "-" + (bDate.getMonth() + 1) + "-" + (bDate.getYear() + 1900);
            Date date2 = new SimpleDateFormat("dd-MM-yyyy").parse(dte);
            dteOrderDate.setDate(date2);
            discountAmt = new BigDecimal("0.00");
            discountPer = new BigDecimal("0");
            netTotalAmt = new BigDecimal("0.00");
            Timer timer = new Timer(500, new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    Date date1 = new Date();
                    String new_str = String.format("%tr", date1);
                    String dateAndTime = clsGlobalVarClass.gPOSDateToDisplay + " " + new_str;
                    lblDate.setText(dateAndTime);
                }
            });
            timer.setRepeats(true);
            timer.setCoalesce(true);
            timer.setInitialDelay(0);
            timer.start();
            btnChangeQty.setVisible(false);

            funLoadPriceOfItems();
            funTruncateInitiallyTempTable();
            funLoadMenuNames();
            funPopularItem();
            funSetDefaultOrderTime();
            funFillAdvOrderType();
            funSetShortCutKeys();

            InputMap im = txtPLU_ItemSearch.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
            ActionMap am = txtPLU_ItemSearch.getActionMap();
            im.put(KeyStroke.getKeyStroke("ESCAPE"), CANCEL_OPTION);
            am.put(CANCEL_ACTION, new frmAdvanceOrder.CancelAction());
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    private void funFillAdvOrderType() throws Exception
    {
        hmAdvOrderType = new HashMap<String, String>();
        String sql_AdvOrderType = "select strAdvOrderTypeCode,strAdvOrderTypeName "
                + "from tbladvanceordertypemaster where strOperational='Yes'";
        ResultSet rsAdvOrderType = clsGlobalVarClass.dbMysql.executeResultSet(sql_AdvOrderType);
        while (rsAdvOrderType.next())
        {
            hmAdvOrderType.put(rsAdvOrderType.getString(2), rsAdvOrderType.getString(1));
        }
        rsAdvOrderType.close();
    }

    private void funSetDefaultOrderTime()
    {
        int hour = new Date().getHours();
        if (hour > 12)
        {
            hour = hour - 12;
        }
        cmbHour.setSelectedItem(String.valueOf(hour));
        int min = new Date().getMinutes();
        cmbMinute.setSelectedItem(String.valueOf(min));
    }

    private void funLoadMenuNames() throws Exception
    {
        JButton[] btnMenuArray =
        {
            btnMenu2, btnMenu3, btnMenu4, btnMenu5, btnMenu6, btnMenu7, btnMenu8
        };
        int i = 0;
        btnPrevMenu.setEnabled(false);
        sql = "select count(distinct(a.strMenuCode)) from tblmenuitempricinghd a,tblmenuhd b "
                + "where a.strMenuCode=b.strMenuCode and b.strOperational='Y' "
                + "and (a.strPosCode='" + clsGlobalVarClass.gPOSCode + "' or a.strPosCode='ALL')"
                + " order by b.strMenuName ";
        ResultSet rsMenuHead = clsGlobalVarClass.dbMysql.executeResultSet(sql);
        while (rsMenuHead.next())
        {
            menuCount = rsMenuHead.getInt(1);
        }
        if (menuCount > 7)
        {
            btnNextMenu.setEnabled(true);
        }
        menuNames1 = new String[menuCount];
        menuNames = new String[menuCount];

        sql = "select distinct(a.strMenuCode),b.strMenuName "
                + "from tblmenuitempricinghd a left outer join tblmenuhd b "
                + "on a.strMenuCode=b.strMenuCode where  b.strOperational='Y' "
                + "and (a.strPosCode='" + clsGlobalVarClass.gPOSCode + "' or a.strPosCode='ALL') "
                + " ORDER by b.intSequence,b.strMenuName ";

        rsMenuHead = clsGlobalVarClass.dbMysql.executeResultSet(sql);
        for (int m = 0; m < 7; m++)
        {
            btnMenuArray[m].setText("");
            btnMenuArray[m].setEnabled(false);
        }
        while (rsMenuHead.next())
        {
            if (i < 7)
            {
                String strMenuName = rsMenuHead.getString(2);
                btnMenuArray[i].setText(clsGlobalVarClass.convertString(strMenuName));
                btnMenuArray[i].setEnabled(true);
            }
            menuNames[i] = rsMenuHead.getString(2);
            menuNames1[i] = rsMenuHead.getString(2);
            i++;
        }
        rsMenuHead.close();
    }

    /**
     * To get the List of AdvanceOrder
     */
    private void funFillAdvanceOrderList(JTable tblAdvOrderList1, String fromDate, String Todate, String operationType)
    {
        StringBuilder sbSql = new StringBuilder();
        try
        {
            DefaultTableModel dm1 = new DefaultTableModel()
            {
                @Override
                public boolean isCellEditable(int row, int column)
                {
                    //all cells false
                    return false;
                }
            };
            dm1.addColumn("c");
            dm1.addColumn("Cust Name");
            dm1.addColumn("Adv Order No.");
            dm1.addColumn("Manual No.");
            dm1.addColumn("Receipt No.");
            dm1.addColumn("Order Date");
            dm1.addColumn("Time");
            dm1.addColumn("Adv Amt");
            dm1.addColumn("Total Amt");
            dm1.addColumn("Area");

            sbSql.setLength(0);
            sbSql.append("select a.strCustomerCode,a.strAdvBookingNo,ifnull(b.strReceiptNo,'') "
                    + " ,date(a.dteOrderFor),ifnull(sum(b.dblAdvDeposite),0),a.dblGrandTotal "
                    + " ,c.strCustomerName,a.strDeliveryTime,a.strManualAdvOrderNo,c.strBuldingCode,c.strBuildingName "
                    + " from tbladvbookbillhd a left outer join tbladvancereceipthd b on a.strAdvBookingNo=b.strAdvBookingNo "
                    + " inner join tblcustomermaster c on a.strCustomerCode=c.strCustomerCode "
                    + " where a.strAdvBookingNo NOT IN(select strAdvBookingNo from tblbillhd) "
                    + " and Date(dteOrderFor) between '" + fromDate + "' and '" + Todate + "' ");
            if (clsGlobalVarClass.gDontShowAdvOrderInOtherPOS)
            {
                sbSql.append(" and a.strPOSCode='" + clsGlobalVarClass.gPOSCode + "' ");
            }
            sbSql.append(" GROUP by a.strAdvBookingNo "
                    + " order by date(dteAdvBookingDate), a.strDeliveryTime");
            dm1.setRowCount(0);

            //System.out.println(sql);
            ResultSet rsAdvOrder = clsGlobalVarClass.dbMysql.executeResultSet(sbSql.toString());
            while (rsAdvOrder.next())
            {
                Object[] rows =
                {
                    rsAdvOrder.getString(1), rsAdvOrder.getString(7), rsAdvOrder.getString(2), rsAdvOrder.getString(9), rsAdvOrder.getString(3), rsAdvOrder.getString(4), rsAdvOrder.getString(8), rsAdvOrder.getString(5), rsAdvOrder.getString(6), rsAdvOrder.getString(11)
                };
                dm1.addRow(rows);
            }
            tblAdvOrderList1.setModel(dm1);
            tblAdvOrderList1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

            DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
            rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
            tblAdvOrderList1.getColumnModel().getColumn(6).setCellRenderer(rightRenderer);
            tblAdvOrderList1.getColumnModel().getColumn(7).setCellRenderer(rightRenderer);
            tblAdvOrderList1.getColumnModel().getColumn(0).setPreferredWidth(0);
            tblAdvOrderList1.getColumnModel().getColumn(1).setPreferredWidth(100);//custName
            tblAdvOrderList1.getColumnModel().getColumn(2).setPreferredWidth(80);//adv no
            tblAdvOrderList1.getColumnModel().getColumn(3).setPreferredWidth(80);//rec no
            tblAdvOrderList1.getColumnModel().getColumn(4).setPreferredWidth(80);//order date
            tblAdvOrderList1.getColumnModel().getColumn(5).setPreferredWidth(80);//del time
            tblAdvOrderList1.getColumnModel().getColumn(6).setPreferredWidth(80);//adv amt
            tblAdvOrderList1.getColumnModel().getColumn(7).setPreferredWidth(80);//total amt
            tblAdvOrderList1.getColumnModel().getColumn(8).setPreferredWidth(150);//total amt

            rsAdvOrder.close();
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
        java.awt.GridBagConstraints gridBagConstraints;

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
        panelLayout = new javax.swing.JPanel();
        panelFormBody = new javax.swing.JPanel();
        panelItemDtl = new javax.swing.JPanel();
        scrItemDetials = new javax.swing.JScrollPane();
        tblItemTable = new javax.swing.JTable();
        btnChangeQty = new javax.swing.JButton();
        txtTotal = new javax.swing.JTextField();
        lblTotal = new javax.swing.JLabel();
        btnUp = new javax.swing.JButton();
        btnDown = new javax.swing.JButton();
        btnDelItem = new javax.swing.JButton();
        panelOrderDate = new javax.swing.JPanel();
        lblAdvOrderNo = new javax.swing.JLabel();
        lblAdvOrderno = new javax.swing.JLabel();
        dteOrderDate = new com.toedter.calendar.JDateChooser();
        cmbHour = new javax.swing.JComboBox();
        cmbMinute = new javax.swing.JComboBox();
        cmbAMPM = new javax.swing.JComboBox();
        chkUrgentOrder = new javax.swing.JCheckBox();
        tabPaneAdvOrder = new javax.swing.JTabbedPane();
        NewOrderPanel = new javax.swing.JPanel();
        panelNavigate = new javax.swing.JPanel();
        btnPrevItem = new javax.swing.JButton();
        btnNextItem = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        lblCustInfo = new javax.swing.JLabel();
        IItemPanel = new javax.swing.JPanel();
        btnIItem2 = new javax.swing.JButton();
        btnIItem1 = new javax.swing.JButton();
        btnIItem3 = new javax.swing.JButton();
        btnIItem4 = new javax.swing.JButton();
        btnIItem5 = new javax.swing.JButton();
        btnIItem6 = new javax.swing.JButton();
        btnIItem7 = new javax.swing.JButton();
        btnIItem8 = new javax.swing.JButton();
        btnIItem9 = new javax.swing.JButton();
        btnIItem10 = new javax.swing.JButton();
        btnIItem11 = new javax.swing.JButton();
        btnIItem12 = new javax.swing.JButton();
        btnIItem13 = new javax.swing.JButton();
        btnIItem14 = new javax.swing.JButton();
        btnIItem15 = new javax.swing.JButton();
        btnIItem16 = new javax.swing.JButton();
        panelSubGroup = new javax.swing.JPanel();
        btnPrevItemSorting = new javax.swing.JButton();
        btnItemSorting1 = new javax.swing.JButton();
        btnItemSorting2 = new javax.swing.JButton();
        btnItemSorting3 = new javax.swing.JButton();
        btnNextItemSorting = new javax.swing.JButton();
        btnItemSorting4 = new javax.swing.JButton();
        IItemGroupPanel = new javax.swing.JPanel();
        btnMenu3 = new javax.swing.JButton();
        btnMenu2 = new javax.swing.JButton();
        btnMenu4 = new javax.swing.JButton();
        btnMenu6 = new javax.swing.JButton();
        btnMenu8 = new javax.swing.JButton();
        btnMenu7 = new javax.swing.JButton();
        btnPopular = new javax.swing.JButton();
        btnMenu5 = new javax.swing.JButton();
        btnPrevMenu = new javax.swing.JButton();
        btnNextMenu = new javax.swing.JButton();
        NumberPanel = new javax.swing.JPanel();
        btnNumber2 = new javax.swing.JButton();
        btnNumber1 = new javax.swing.JButton();
        btnNumber4 = new javax.swing.JButton();
        btnNumber3 = new javax.swing.JButton();
        btnNumber5 = new javax.swing.JButton();
        btnNumber6 = new javax.swing.JButton();
        btnNumber7 = new javax.swing.JButton();
        btnNumber8 = new javax.swing.JButton();
        btnNumber9 = new javax.swing.JButton();
        btnMultiQty = new javax.swing.JButton();
        panel_PLU = new javax.swing.JPanel();
        txtPLU_ItemSearch = new javax.swing.JTextField();
        bttn_PLU_Panel_Close = new javax.swing.JButton();
        jScrollPane6 = new javax.swing.JScrollPane();
        tbl_PLU_Items = new javax.swing.JTable();
        ModifyAdvOrderPanel = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblModifyAdvanceOrderList = new javax.swing.JTable();
        dteFromModify = new com.toedter.calendar.JDateChooser();
        dteToModify = new com.toedter.calendar.JDateChooser();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        btnExecuteModify = new javax.swing.JButton();
        ListAdvOrderPanel = new javax.swing.JPanel();
        panelListAdvOrder = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblAdvanceOrderList = new javax.swing.JTable();
        dteTo = new com.toedter.calendar.JDateChooser();
        dteFrom = new com.toedter.calendar.JDateChooser();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        btnExecuteList = new javax.swing.JButton();
        panelKOTDetails = new javax.swing.JPanel();
        lblNote = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        txtNote = new javax.swing.JTextArea();
        lblManualAdvOrderNo = new javax.swing.JLabel();
        txtManualAdvOrderNo = new javax.swing.JTextField();
        lblMessage = new javax.swing.JLabel();
        lblShape = new javax.swing.JLabel();
        txtShape = new javax.swing.JTextField();
        jScrollPane7 = new javax.swing.JScrollPane();
        txtMessage = new javax.swing.JTextArea();
        NewTab = new javax.swing.JPanel();
        btnBrows = new javax.swing.JButton();
        btnBrowseSpecialSymbol = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        lblImage = new javax.swing.JLabel();
        lblItemlImage = new javax.swing.JLabel();
        lblSymbolImage = new javax.swing.JLabel();
        lblItemNameForImage = new javax.swing.JLabel();
        btnAttatchImage = new javax.swing.JButton();
        panelDetails = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        tblCharactersticsMaster = new javax.swing.JTable();
        lblItemName = new javax.swing.JLabel();
        lblTextCharName = new javax.swing.JLabel();
        txtTextCharValue = new javax.swing.JTextField();
        btnApplyTextChar = new javax.swing.JButton();
        lblHCharCode = new javax.swing.JLabel();
        lblHItemCode = new javax.swing.JLabel();
        lblHItemName = new javax.swing.JLabel();
        txtCharValueSearch = new javax.swing.JTextField();
        cmbCharValue = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        btnApplyValueChar = new javax.swing.JButton();
        btnHome = new javax.swing.JButton();
        btnDone = new javax.swing.JButton();
        btnModifier = new javax.swing.JButton();
        btnPlu = new javax.swing.JButton();
        btnSettle1 = new javax.swing.JButton();
        btnWaiterName = new javax.swing.JButton();
        btnHomeDel = new javax.swing.JButton();

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
        });

        panelHeader.setBackground(new java.awt.Color(69, 164, 238));
        panelHeader.setLayout(new javax.swing.BoxLayout(panelHeader, javax.swing.BoxLayout.LINE_AXIS));

        lblProductName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblProductName.setForeground(new java.awt.Color(255, 255, 255));
        lblProductName.setText("SPOS - ");
        lblProductName.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                lblProductNameMouseClicked(evt);
            }
        });
        panelHeader.add(lblProductName);

        lblModuleName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblModuleName.setForeground(new java.awt.Color(255, 255, 255));
        panelHeader.add(lblModuleName);

        lblformName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblformName.setForeground(new java.awt.Color(255, 255, 255));
        lblformName.setText(" -Advance Order");
        lblformName.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                lblformNameMouseClicked(evt);
            }
        });
        panelHeader.add(lblformName);
        panelHeader.add(filler4);
        panelHeader.add(filler5);

        lblPosName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblPosName.setForeground(new java.awt.Color(255, 255, 255));
        lblPosName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPosName.setMaximumSize(new java.awt.Dimension(321, 30));
        lblPosName.setMinimumSize(new java.awt.Dimension(321, 30));
        lblPosName.setPreferredSize(new java.awt.Dimension(321, 30));
        lblPosName.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                lblPosNameMouseClicked(evt);
            }
        });
        panelHeader.add(lblPosName);
        panelHeader.add(filler6);

        lblUserCode.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblUserCode.setForeground(new java.awt.Color(255, 255, 255));
        lblUserCode.setMaximumSize(new java.awt.Dimension(90, 30));
        lblUserCode.setMinimumSize(new java.awt.Dimension(90, 30));
        lblUserCode.setPreferredSize(new java.awt.Dimension(90, 30));
        lblUserCode.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                lblUserCodeMouseClicked(evt);
            }
        });
        panelHeader.add(lblUserCode);

        lblDate.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblDate.setForeground(new java.awt.Color(255, 255, 255));
        lblDate.setMaximumSize(new java.awt.Dimension(192, 30));
        lblDate.setMinimumSize(new java.awt.Dimension(192, 30));
        lblDate.setPreferredSize(new java.awt.Dimension(192, 30));
        lblDate.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                lblDateMouseClicked(evt);
            }
        });
        panelHeader.add(lblDate);

        lblHOSign.setMaximumSize(new java.awt.Dimension(34, 30));
        lblHOSign.setMinimumSize(new java.awt.Dimension(34, 30));
        lblHOSign.setPreferredSize(new java.awt.Dimension(34, 30));
        panelHeader.add(lblHOSign);

        getContentPane().add(panelHeader, java.awt.BorderLayout.PAGE_START);

        panelLayout.setBackground(new java.awt.Color(255, 255, 255));
        panelLayout.setLayout(new java.awt.GridBagLayout());

        panelFormBody.setBackground(new java.awt.Color(255, 255, 255));
        panelFormBody.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        panelFormBody.setMinimumSize(new java.awt.Dimension(800, 600));
        panelFormBody.setPreferredSize(new java.awt.Dimension(800, 600));

        panelItemDtl.setBackground(new java.awt.Color(255, 255, 255));
        panelItemDtl.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        panelItemDtl.setForeground(new java.awt.Color(255, 255, 255));

        tblItemTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Discription ", "Qty", "Amount", "wgt"
            }
        ));
        tblItemTable.setRowHeight(30);
        tblItemTable.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tblItemTableMouseClicked(evt);
            }
        });
        scrItemDetials.setViewportView(tblItemTable);
        if (tblItemTable.getColumnModel().getColumnCount() > 0)
        {
            tblItemTable.getColumnModel().getColumn(1).setMinWidth(2);
            tblItemTable.getColumnModel().getColumn(1).setPreferredWidth(2);
            tblItemTable.getColumnModel().getColumn(1).setMaxWidth(2);
            tblItemTable.getColumnModel().getColumn(2).setMinWidth(2);
            tblItemTable.getColumnModel().getColumn(2).setPreferredWidth(2);
            tblItemTable.getColumnModel().getColumn(2).setMaxWidth(2);
            tblItemTable.getColumnModel().getColumn(3).setMinWidth(2);
            tblItemTable.getColumnModel().getColumn(3).setPreferredWidth(2);
            tblItemTable.getColumnModel().getColumn(3).setMaxWidth(2);
        }

        btnChangeQty.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnChangeQty.setForeground(new java.awt.Color(255, 255, 255));
        btnChangeQty.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnChangeQty.setText("CHANGE QTY");
        btnChangeQty.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnChangeQty.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnChangeQty.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnChangeQtyActionPerformed(evt);
            }
        });

        txtTotal.setEditable(false);
        txtTotal.setBackground(new java.awt.Color(255, 255, 255));
        txtTotal.setFont(new java.awt.Font("DejaVu Sans", 1, 14)); // NOI18N
        txtTotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        lblTotal.setFont(new java.awt.Font("DejaVu Sans", 1, 14)); // NOI18N
        lblTotal.setText("TOTAL");

        btnUp.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnUp.setForeground(new java.awt.Color(255, 255, 255));
        btnUp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnUp.setText("UP");
        btnUp.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnUp.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnUp.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnUpMouseClicked(evt);
            }
        });
        btnUp.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnUpActionPerformed(evt);
            }
        });

        btnDown.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnDown.setForeground(new java.awt.Color(255, 255, 255));
        btnDown.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnDown.setText("DOWN");
        btnDown.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDown.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnDown.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnDownMouseClicked(evt);
            }
        });
        btnDown.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnDownActionPerformed(evt);
            }
        });

        btnDelItem.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnDelItem.setForeground(new java.awt.Color(255, 255, 255));
        btnDelItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnDelItem.setText("DELETE");
        btnDelItem.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDelItem.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnDelItem.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnDelItemMouseClicked(evt);
            }
        });
        btnDelItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnDelItemActionPerformed(evt);
            }
        });

        panelOrderDate.setBackground(new java.awt.Color(255, 255, 255));
        panelOrderDate.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        lblAdvOrderNo.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblAdvOrderNo.setText("Adv OrderNo :");

        cmbHour.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "HH", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" }));

        cmbMinute.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "MM", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        cmbMinute.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbMinuteActionPerformed(evt);
            }
        });

        cmbAMPM.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "PM", "AM" }));

        chkUrgentOrder.setText("Urgent Order");

        javax.swing.GroupLayout panelOrderDateLayout = new javax.swing.GroupLayout(panelOrderDate);
        panelOrderDate.setLayout(panelOrderDateLayout);
        panelOrderDateLayout.setHorizontalGroup(
            panelOrderDateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelOrderDateLayout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(panelOrderDateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelOrderDateLayout.createSequentialGroup()
                        .addComponent(dteOrderDate, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(cmbHour, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbMinute, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbAMPM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelOrderDateLayout.createSequentialGroup()
                        .addComponent(lblAdvOrderNo, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblAdvOrderno, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(chkUrgentOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelOrderDateLayout.setVerticalGroup(
            panelOrderDateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelOrderDateLayout.createSequentialGroup()
                .addGroup(panelOrderDateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblAdvOrderNo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblAdvOrderno, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chkUrgentOrder, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelOrderDateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dteOrderDate, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelOrderDateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cmbHour, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmbMinute, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmbAMPM, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panelItemDtlLayout = new javax.swing.GroupLayout(panelItemDtl);
        panelItemDtl.setLayout(panelItemDtlLayout);
        panelItemDtlLayout.setHorizontalGroup(
            panelItemDtlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelOrderDate, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(scrItemDetials, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(panelItemDtlLayout.createSequentialGroup()
                .addGroup(panelItemDtlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelItemDtlLayout.createSequentialGroup()
                        .addComponent(btnChangeQty, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelItemDtlLayout.createSequentialGroup()
                        .addComponent(btnUp, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnDown, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(14, 14, 14)
                        .addComponent(btnDelItem, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 3, Short.MAX_VALUE))
        );
        panelItemDtlLayout.setVerticalGroup(
            panelItemDtlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelItemDtlLayout.createSequentialGroup()
                .addComponent(panelOrderDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrItemDetials, javax.swing.GroupLayout.PREFERRED_SIZE, 341, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelItemDtlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnChangeQty, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelItemDtlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnUp, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDown, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDelItem, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0))
        );

        tabPaneAdvOrder.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        tabPaneAdvOrder.setAutoscrolls(true);
        tabPaneAdvOrder.setPreferredSize(new java.awt.Dimension(480, 515));
        tabPaneAdvOrder.addChangeListener(new javax.swing.event.ChangeListener()
        {
            public void stateChanged(javax.swing.event.ChangeEvent evt)
            {
                tabPaneAdvOrderStateChanged(evt);
            }
        });
        tabPaneAdvOrder.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tabPaneAdvOrderMouseClicked(evt);
            }
        });

        NewOrderPanel.setBackground(new java.awt.Color(255, 255, 255));
        NewOrderPanel.setPreferredSize(new java.awt.Dimension(475, 470));
        NewOrderPanel.setLayout(null);

        panelNavigate.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnPrevItem.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnPrevItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgBackBtn1.png"))); // NOI18N
        btnPrevItem.setText("<<<");
        btnPrevItem.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPrevItem.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgBackBtn2.png"))); // NOI18N
        btnPrevItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnPrevItemActionPerformed(evt);
            }
        });

        btnNextItem.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnNextItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgBackBtn1.png"))); // NOI18N
        btnNextItem.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNextItem.setLabel(">>>");
        btnNextItem.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgBackBtn2.png"))); // NOI18N
        btnNextItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnNextItemActionPerformed(evt);
            }
        });

        jPanel8.setBackground(new java.awt.Color(0, 102, 255));
        jPanel8.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel8.setForeground(new java.awt.Color(240, 200, 80));

        lblCustInfo.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblCustInfo.setForeground(new java.awt.Color(255, 255, 255));
        lblCustInfo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCustInfo.setText("Customer Info");
        lblCustInfo.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                lblCustInfoMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(lblCustInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblCustInfo, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
        );

        IItemPanel.setBackground(new java.awt.Color(255, 255, 255));
        IItemPanel.setEnabled(false);

        btnIItem2.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnIItem2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnIItem2.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnIItem2MouseClicked(evt);
            }
        });
        btnIItem2.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnIItem2ActionPerformed(evt);
            }
        });

        btnIItem1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnIItem1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnIItem1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnIItem1ActionPerformed(evt);
            }
        });

        btnIItem3.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnIItem3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnIItem3.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnIItem3MouseClicked(evt);
            }
        });
        btnIItem3.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnIItem3ActionPerformed(evt);
            }
        });

        btnIItem4.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnIItem4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnIItem4.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnIItem4MouseClicked(evt);
            }
        });
        btnIItem4.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnIItem4ActionPerformed(evt);
            }
        });

        btnIItem5.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnIItem5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnIItem5.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnIItem5MouseClicked(evt);
            }
        });
        btnIItem5.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnIItem5ActionPerformed(evt);
            }
        });

        btnIItem6.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnIItem6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnIItem6.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnIItem6MouseClicked(evt);
            }
        });
        btnIItem6.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnIItem6ActionPerformed(evt);
            }
        });

        btnIItem7.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnIItem7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnIItem7.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnIItem7MouseClicked(evt);
            }
        });
        btnIItem7.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnIItem7ActionPerformed(evt);
            }
        });

        btnIItem8.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnIItem8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnIItem8.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnIItem8MouseClicked(evt);
            }
        });
        btnIItem8.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnIItem8ActionPerformed(evt);
            }
        });

        btnIItem9.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnIItem9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnIItem9.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnIItem9MouseClicked(evt);
            }
        });
        btnIItem9.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnIItem9ActionPerformed(evt);
            }
        });

        btnIItem10.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnIItem10.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnIItem10.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnIItem10MouseClicked(evt);
            }
        });
        btnIItem10.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnIItem10ActionPerformed(evt);
            }
        });

        btnIItem11.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnIItem11.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnIItem11.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnIItem11MouseClicked(evt);
            }
        });
        btnIItem11.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnIItem11ActionPerformed(evt);
            }
        });

        btnIItem12.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnIItem12.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnIItem12.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnIItem12MouseClicked(evt);
            }
        });
        btnIItem12.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnIItem12ActionPerformed(evt);
            }
        });

        btnIItem13.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnIItem13.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnIItem13.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnIItem13MouseClicked(evt);
            }
        });
        btnIItem13.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnIItem13ActionPerformed(evt);
            }
        });

        btnIItem14.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnIItem14.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnIItem14.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnIItem14MouseClicked(evt);
            }
        });
        btnIItem14.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnIItem14ActionPerformed(evt);
            }
        });

        btnIItem15.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnIItem15.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnIItem15.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnIItem15MouseClicked(evt);
            }
        });
        btnIItem15.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnIItem15ActionPerformed(evt);
            }
        });

        btnIItem16.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnIItem16.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnIItem16.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnIItem16MouseClicked(evt);
            }
        });
        btnIItem16.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnIItem16ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout IItemPanelLayout = new javax.swing.GroupLayout(IItemPanel);
        IItemPanel.setLayout(IItemPanelLayout);
        IItemPanelLayout.setHorizontalGroup(
            IItemPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(IItemPanelLayout.createSequentialGroup()
                .addComponent(btnIItem13, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnIItem14, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnIItem15, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnIItem16, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, IItemPanelLayout.createSequentialGroup()
                .addComponent(btnIItem9, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnIItem10, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnIItem11, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnIItem12, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, IItemPanelLayout.createSequentialGroup()
                .addComponent(btnIItem5, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnIItem6, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnIItem7, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnIItem8, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, IItemPanelLayout.createSequentialGroup()
                .addComponent(btnIItem1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnIItem2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnIItem3, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnIItem4, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        IItemPanelLayout.setVerticalGroup(
            IItemPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(IItemPanelLayout.createSequentialGroup()
                .addGroup(IItemPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(IItemPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(IItemPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnIItem3, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnIItem4, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(IItemPanelLayout.createSequentialGroup()
                            .addComponent(btnIItem2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(1, 1, 1)))
                    .addComponent(btnIItem1, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(IItemPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(IItemPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnIItem7, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnIItem8, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(IItemPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnIItem5, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnIItem6, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(IItemPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(IItemPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnIItem11, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnIItem12, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(IItemPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnIItem9, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnIItem10, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(IItemPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(IItemPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnIItem15, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnIItem16, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(IItemPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnIItem13, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnIItem14, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 16, Short.MAX_VALUE))
        );

        panelSubGroup.setBackground(new java.awt.Color(204, 255, 204));
        panelSubGroup.setLayout(null);

        btnPrevItemSorting.setText("<<");
        btnPrevItemSorting.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        btnPrevItemSorting.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPrevItemSorting.setMargin(new java.awt.Insets(1, 1, 1, 1));
        btnPrevItemSorting.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnPrevItemSortingMouseClicked(evt);
            }
        });
        panelSubGroup.add(btnPrevItemSorting);
        btnPrevItemSorting.setBounds(0, 3, 30, 40);

        btnItemSorting1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnItemSorting1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnItemSorting1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnItemSorting1.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnItemSorting1MouseClicked(evt);
            }
        });
        panelSubGroup.add(btnItemSorting1);
        btnItemSorting1.setBounds(30, 3, 70, 40);

        btnItemSorting2.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnItemSorting2.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnItemSorting2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnItemSorting2.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnItemSorting2MouseClicked(evt);
            }
        });
        panelSubGroup.add(btnItemSorting2);
        btnItemSorting2.setBounds(100, 3, 70, 40);

        btnItemSorting3.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnItemSorting3.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnItemSorting3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnItemSorting3.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnItemSorting3MouseClicked(evt);
            }
        });
        panelSubGroup.add(btnItemSorting3);
        btnItemSorting3.setBounds(170, 3, 70, 40);

        btnNextItemSorting.setText(">>");
        btnNextItemSorting.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        btnNextItemSorting.setMargin(new java.awt.Insets(1, 1, 1, 1));
        btnNextItemSorting.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnNextItemSortingMouseClicked(evt);
            }
        });
        panelSubGroup.add(btnNextItemSorting);
        btnNextItemSorting.setBounds(310, 3, 40, 40);

        btnItemSorting4.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnItemSorting4.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnItemSorting4.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnItemSorting4MouseClicked(evt);
            }
        });
        panelSubGroup.add(btnItemSorting4);
        btnItemSorting4.setBounds(240, 3, 70, 40);

        javax.swing.GroupLayout panelNavigateLayout = new javax.swing.GroupLayout(panelNavigate);
        panelNavigate.setLayout(panelNavigateLayout);
        panelNavigateLayout.setHorizontalGroup(
            panelNavigateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelNavigateLayout.createSequentialGroup()
                .addComponent(btnPrevItem, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnNextItem, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(panelNavigateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                .addComponent(panelSubGroup, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(IItemPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelNavigateLayout.setVerticalGroup(
            panelNavigateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelNavigateLayout.createSequentialGroup()
                .addGroup(panelNavigateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelNavigateLayout.createSequentialGroup()
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(btnNextItem, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPrevItem, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(2, 2, 2)
                .addComponent(panelSubGroup, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(IItemPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        NewOrderPanel.add(panelNavigate);
        panelNavigate.setBounds(0, 0, 344, 470);

        IItemGroupPanel.setBackground(new java.awt.Color(255, 255, 255));
        IItemGroupPanel.setForeground(new java.awt.Color(255, 236, 205));

        btnMenu3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnMenu3.setForeground(new java.awt.Color(255, 255, 255));
        btnMenu3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnMenu3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnMenu3.setPreferredSize(new java.awt.Dimension(45, 82));
        btnMenu3.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnMenu3.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnMenu3MouseClicked(evt);
            }
        });
        btnMenu3.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnMenu3ActionPerformed(evt);
            }
        });

        btnMenu2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnMenu2.setForeground(new java.awt.Color(255, 255, 255));
        btnMenu2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnMenu2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnMenu2.setPreferredSize(new java.awt.Dimension(45, 82));
        btnMenu2.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnMenu2.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnMenu2ActionPerformed(evt);
            }
        });

        btnMenu4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnMenu4.setForeground(new java.awt.Color(255, 255, 255));
        btnMenu4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnMenu4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnMenu4.setPreferredSize(new java.awt.Dimension(45, 82));
        btnMenu4.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnMenu4.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnMenu4MouseClicked(evt);
            }
        });
        btnMenu4.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnMenu4ActionPerformed(evt);
            }
        });

        btnMenu6.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnMenu6.setForeground(new java.awt.Color(255, 255, 255));
        btnMenu6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnMenu6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnMenu6.setPreferredSize(new java.awt.Dimension(45, 82));
        btnMenu6.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnMenu6.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnMenu6ActionPerformed(evt);
            }
        });

        btnMenu8.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnMenu8.setForeground(new java.awt.Color(255, 255, 255));
        btnMenu8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnMenu8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnMenu8.setPreferredSize(new java.awt.Dimension(45, 82));
        btnMenu8.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnMenu8.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnMenu8ActionPerformed(evt);
            }
        });

        btnMenu7.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnMenu7.setForeground(new java.awt.Color(255, 255, 255));
        btnMenu7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnMenu7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnMenu7.setPreferredSize(new java.awt.Dimension(45, 82));
        btnMenu7.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnMenu7.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnMenu7ActionPerformed(evt);
            }
        });

        btnPopular.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnPopular.setForeground(new java.awt.Color(255, 255, 255));
        btnPopular.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnPopular.setText("Popular");
        btnPopular.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPopular.setPreferredSize(new java.awt.Dimension(45, 82));
        btnPopular.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnPopular.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnPopularActionPerformed(evt);
            }
        });

        btnMenu5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnMenu5.setForeground(new java.awt.Color(255, 255, 255));
        btnMenu5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnMenu5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnMenu5.setPreferredSize(new java.awt.Dimension(45, 82));
        btnMenu5.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnMenu5.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnMenu5ActionPerformed(evt);
            }
        });

        btnPrevMenu.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnPrevMenu.setForeground(new java.awt.Color(255, 255, 255));
        btnPrevMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnPrevMenu.setText("^");
        btnPrevMenu.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPrevMenu.setPreferredSize(new java.awt.Dimension(45, 82));
        btnPrevMenu.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnPrevMenu.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnPrevMenuMouseClicked(evt);
            }
        });
        btnPrevMenu.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnPrevMenuActionPerformed(evt);
            }
        });

        btnNextMenu.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnNextMenu.setForeground(new java.awt.Color(255, 255, 255));
        btnNextMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnNextMenu.setText("V");
        btnNextMenu.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNextMenu.setPreferredSize(new java.awt.Dimension(45, 82));
        btnNextMenu.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnNextMenu.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnNextMenuActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout IItemGroupPanelLayout = new javax.swing.GroupLayout(IItemGroupPanel);
        IItemGroupPanel.setLayout(IItemGroupPanelLayout);
        IItemGroupPanelLayout.setHorizontalGroup(
            IItemGroupPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, IItemGroupPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(btnMenu7, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(btnMenu8, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(btnMenu6, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(btnMenu5, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(btnMenu4, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(btnMenu2, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(btnPopular, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(btnNextMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(btnPrevMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(btnMenu3, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        IItemGroupPanelLayout.setVerticalGroup(
            IItemGroupPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, IItemGroupPanelLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(btnPrevMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnPopular, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(btnMenu2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnMenu3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnMenu4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnMenu5, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnMenu6, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnMenu7, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnMenu8, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(btnNextMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6))
        );

        NewOrderPanel.add(IItemGroupPanel);
        IItemGroupPanel.setBounds(387, 0, 90, 458);

        NumberPanel.setBackground(new java.awt.Color(255, 255, 255));
        NumberPanel.setForeground(new java.awt.Color(255, 236, 205));

        btnNumber2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnNumber2.setForeground(new java.awt.Color(255, 255, 255));
        btnNumber2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgNumericButton1.png"))); // NOI18N
        btnNumber2.setText("2");
        btnNumber2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNumber2.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgNumericButton2.png"))); // NOI18N
        btnNumber2.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnNumber2MouseClicked(evt);
            }
        });

        btnNumber1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnNumber1.setForeground(new java.awt.Color(255, 255, 255));
        btnNumber1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgNumericButton1.png"))); // NOI18N
        btnNumber1.setText("1");
        btnNumber1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNumber1.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgNumericButton2.png"))); // NOI18N
        btnNumber1.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnNumber1MouseClicked(evt);
            }
        });

        btnNumber4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnNumber4.setForeground(new java.awt.Color(255, 255, 255));
        btnNumber4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgNumericButton1.png"))); // NOI18N
        btnNumber4.setText("4");
        btnNumber4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNumber4.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgNumericButton2.png"))); // NOI18N
        btnNumber4.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnNumber4MouseClicked(evt);
            }
        });

        btnNumber3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnNumber3.setForeground(new java.awt.Color(255, 255, 255));
        btnNumber3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgNumericButton1.png"))); // NOI18N
        btnNumber3.setText("3");
        btnNumber3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNumber3.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgNumericButton2.png"))); // NOI18N
        btnNumber3.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnNumber3MouseClicked(evt);
            }
        });

        btnNumber5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnNumber5.setForeground(new java.awt.Color(255, 255, 255));
        btnNumber5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgNumericButton1.png"))); // NOI18N
        btnNumber5.setText("5");
        btnNumber5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNumber5.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgNumericButton2.png"))); // NOI18N
        btnNumber5.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnNumber5MouseClicked(evt);
            }
        });

        btnNumber6.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnNumber6.setForeground(new java.awt.Color(255, 255, 255));
        btnNumber6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgNumericButton1.png"))); // NOI18N
        btnNumber6.setText("6");
        btnNumber6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNumber6.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgNumericButton2.png"))); // NOI18N
        btnNumber6.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnNumber6MouseClicked(evt);
            }
        });

        btnNumber7.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnNumber7.setForeground(new java.awt.Color(255, 255, 255));
        btnNumber7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgNumericButton1.png"))); // NOI18N
        btnNumber7.setText("7");
        btnNumber7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNumber7.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgNumericButton2.png"))); // NOI18N
        btnNumber7.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnNumber7MouseClicked(evt);
            }
        });

        btnNumber8.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnNumber8.setForeground(new java.awt.Color(255, 255, 255));
        btnNumber8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgNumericButton1.png"))); // NOI18N
        btnNumber8.setText("8");
        btnNumber8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNumber8.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgNumericButton2.png"))); // NOI18N
        btnNumber8.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnNumber8MouseClicked(evt);
            }
        });

        btnNumber9.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnNumber9.setForeground(new java.awt.Color(255, 255, 255));
        btnNumber9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgNumericButton1.png"))); // NOI18N
        btnNumber9.setText("9");
        btnNumber9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNumber9.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgNumericButton2.png"))); // NOI18N
        btnNumber9.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnNumber9MouseClicked(evt);
            }
        });

        btnMultiQty.setBackground(new java.awt.Color(102, 153, 255));
        btnMultiQty.setFont(new java.awt.Font("Tahoma", 1, 8)); // NOI18N
        btnMultiQty.setForeground(new java.awt.Color(255, 255, 255));
        btnMultiQty.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgNumericButton1.png"))); // NOI18N
        btnMultiQty.setText(">>");
        btnMultiQty.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnMultiQty.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgNumericButton2.png"))); // NOI18N
        btnMultiQty.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnMultiQtyMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout NumberPanelLayout = new javax.swing.GroupLayout(NumberPanel);
        NumberPanel.setLayout(NumberPanelLayout);
        NumberPanelLayout.setHorizontalGroup(
            NumberPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(NumberPanelLayout.createSequentialGroup()
                .addGroup(NumberPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btnMultiQty, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(btnNumber9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(btnNumber8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(btnNumber7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(btnNumber6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(btnNumber5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(btnNumber4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(btnNumber3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(btnNumber2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(btnNumber1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0))
        );
        NumberPanelLayout.setVerticalGroup(
            NumberPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, NumberPanelLayout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(btnNumber1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnNumber2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnNumber3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnNumber4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnNumber5, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(btnNumber6, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(btnNumber7, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnNumber8, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(btnNumber9, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnMultiQty, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        NewOrderPanel.add(NumberPanel);
        NumberPanel.setBounds(341, 2, 40, 456);

        panel_PLU.setBackground(new java.awt.Color(255, 255, 255));
        panel_PLU.setLayout(null);

        txtPLU_ItemSearch.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtPLU_ItemSearch.addFocusListener(new java.awt.event.FocusAdapter()
        {
            public void focusGained(java.awt.event.FocusEvent evt)
            {
                txtPLU_ItemSearchFocusGained(evt);
            }
        });
        txtPLU_ItemSearch.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtPLU_ItemSearchMouseClicked(evt);
            }
        });
        txtPLU_ItemSearch.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtPLU_ItemSearchKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt)
            {
                txtPLU_ItemSearchKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt)
            {
                txtPLU_ItemSearchKeyTyped(evt);
            }
        });
        panel_PLU.add(txtPLU_ItemSearch);
        txtPLU_ItemSearch.setBounds(10, 10, 210, 30);

        bttn_PLU_Panel_Close.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        bttn_PLU_Panel_Close.setForeground(new java.awt.Color(255, 255, 255));
        bttn_PLU_Panel_Close.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn1.png"))); // NOI18N
        bttn_PLU_Panel_Close.setText("CLOSE");
        bttn_PLU_Panel_Close.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        bttn_PLU_Panel_Close.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn2.png"))); // NOI18N
        bttn_PLU_Panel_Close.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                bttn_PLU_Panel_CloseMouseClicked(evt);
            }
        });
        panel_PLU.add(bttn_PLU_Panel_Close);
        bttn_PLU_Panel_Close.setBounds(230, 5, 100, 40);

        tbl_PLU_Items.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Item Name"
            }
        )
        {
            boolean[] canEdit = new boolean []
            {
                false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return canEdit [columnIndex];
            }
        });
        tbl_PLU_Items.setRowHeight(25);
        tbl_PLU_Items.getTableHeader().setReorderingAllowed(false);
        tbl_PLU_Items.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tbl_PLU_ItemsMouseClicked(evt);
            }
        });
        tbl_PLU_Items.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                tbl_PLU_ItemsKeyPressed(evt);
            }
        });
        jScrollPane6.setViewportView(tbl_PLU_Items);

        panel_PLU.add(jScrollPane6);
        jScrollPane6.setBounds(0, 50, 340, 420);

        NewOrderPanel.add(panel_PLU);
        panel_PLU.setBounds(0, 0, 340, 470);

        tabPaneAdvOrder.addTab("New", NewOrderPanel);

        jPanel12.setBackground(new java.awt.Color(255, 255, 255));
        jPanel12.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 235, 174)));
        jPanel12.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel12.setInheritsPopupMenu(true);
        jPanel12.setLayout(null);

        tblModifyAdvanceOrderList.setBackground(new java.awt.Color(204, 255, 255));
        tblModifyAdvanceOrderList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Cust Name", "Adv Order No.", "Receipt Date", "Order Date", "Adv Amt", "Total Amt"
            }
        )
        {
            boolean[] canEdit = new boolean []
            {
                true, false, false, false, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return canEdit [columnIndex];
            }
        });
        tblModifyAdvanceOrderList.setRowHeight(25);
        tblModifyAdvanceOrderList.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tblModifyAdvanceOrderListMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tblModifyAdvanceOrderList);

        jPanel12.add(jScrollPane3);
        jScrollPane3.setBounds(0, 40, 460, 430);
        jPanel12.add(dteFromModify);
        dteFromModify.setBounds(30, 10, 140, 30);
        jPanel12.add(dteToModify);
        dteToModify.setBounds(200, 10, 150, 30);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setText("From");
        jPanel12.add(jLabel2);
        jLabel2.setBounds(0, 10, 30, 30);

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setText("To");
        jPanel12.add(jLabel3);
        jLabel3.setBounds(180, 10, 20, 30);

        btnExecuteModify.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnExecuteModify.setForeground(new java.awt.Color(255, 255, 255));
        btnExecuteModify.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn1.png"))); // NOI18N
        btnExecuteModify.setText("EXECUTE");
        btnExecuteModify.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnExecuteModify.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn2.png"))); // NOI18N
        btnExecuteModify.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnExecuteModifyMouseClicked(evt);
            }
        });
        jPanel12.add(btnExecuteModify);
        btnExecuteModify.setBounds(383, 0, 80, 30);

        javax.swing.GroupLayout ModifyAdvOrderPanelLayout = new javax.swing.GroupLayout(ModifyAdvOrderPanel);
        ModifyAdvOrderPanel.setLayout(ModifyAdvOrderPanelLayout);
        ModifyAdvOrderPanelLayout.setHorizontalGroup(
            ModifyAdvOrderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ModifyAdvOrderPanelLayout.createSequentialGroup()
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, 463, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        ModifyAdvOrderPanelLayout.setVerticalGroup(
            ModifyAdvOrderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, 495, Short.MAX_VALUE)
        );

        tabPaneAdvOrder.addTab("Modify", ModifyAdvOrderPanel);

        ListAdvOrderPanel.setBackground(new java.awt.Color(255, 255, 255));

        panelListAdvOrder.setBackground(new java.awt.Color(255, 255, 255));
        panelListAdvOrder.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 235, 174)));
        panelListAdvOrder.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        panelListAdvOrder.setInheritsPopupMenu(true);
        panelListAdvOrder.setLayout(null);

        tblAdvanceOrderList.setBackground(new java.awt.Color(204, 255, 255));
        tblAdvanceOrderList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Adv Order No.", "Receipt Date", "Order Date", "Amt Deposit"
            }
        )
        {
            boolean[] canEdit = new boolean []
            {
                false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return canEdit [columnIndex];
            }
        });
        tblAdvanceOrderList.setRowHeight(25);
        tblAdvanceOrderList.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tblAdvanceOrderListMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblAdvanceOrderList);

        panelListAdvOrder.add(jScrollPane2);
        jScrollPane2.setBounds(0, 40, 460, 440);
        panelListAdvOrder.add(dteTo);
        dteTo.setBounds(200, 10, 150, 30);
        panelListAdvOrder.add(dteFrom);
        dteFrom.setBounds(30, 10, 140, 30);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setText("From");
        panelListAdvOrder.add(jLabel4);
        jLabel4.setBounds(0, 10, 40, 30);

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setText("To");
        panelListAdvOrder.add(jLabel6);
        jLabel6.setBounds(180, 10, 20, 30);

        btnExecuteList.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnExecuteList.setForeground(new java.awt.Color(255, 255, 255));
        btnExecuteList.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn1.png"))); // NOI18N
        btnExecuteList.setText("Execute");
        btnExecuteList.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnExecuteList.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn2.png"))); // NOI18N
        btnExecuteList.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnExecuteListMouseClicked(evt);
            }
        });
        panelListAdvOrder.add(btnExecuteList);
        btnExecuteList.setBounds(380, 0, 80, 30);

        javax.swing.GroupLayout ListAdvOrderPanelLayout = new javax.swing.GroupLayout(ListAdvOrderPanel);
        ListAdvOrderPanel.setLayout(ListAdvOrderPanelLayout);
        ListAdvOrderPanelLayout.setHorizontalGroup(
            ListAdvOrderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ListAdvOrderPanelLayout.createSequentialGroup()
                .addComponent(panelListAdvOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 463, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        ListAdvOrderPanelLayout.setVerticalGroup(
            ListAdvOrderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelListAdvOrder, javax.swing.GroupLayout.DEFAULT_SIZE, 495, Short.MAX_VALUE)
        );

        tabPaneAdvOrder.addTab("List", ListAdvOrderPanel);

        panelKOTDetails.setBackground(new java.awt.Color(255, 255, 255));

        lblNote.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblNote.setText("Remark        :");

        txtNote.setColumns(20);
        txtNote.setRows(5);
        txtNote.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtNoteMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(txtNote);

        lblManualAdvOrderNo.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblManualAdvOrderNo.setText("MANUAL ADV ORDER NO      :");

        txtManualAdvOrderNo.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtManualAdvOrderNoMouseClicked(evt);
            }
        });
        txtManualAdvOrderNo.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtManualAdvOrderNoKeyPressed(evt);
            }
        });

        lblMessage.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblMessage.setText("MESSAGE     :");

        lblShape.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblShape.setText("SHAPE         :");

        txtShape.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtShapeMouseClicked(evt);
            }
        });
        txtShape.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtShapeActionPerformed(evt);
            }
        });
        txtShape.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtShapeKeyPressed(evt);
            }
        });

        txtMessage.setColumns(20);
        txtMessage.setRows(5);
        txtMessage.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtMessageMouseClicked(evt);
            }
        });
        txtMessage.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtMessageKeyPressed(evt);
            }
        });
        jScrollPane7.setViewportView(txtMessage);

        javax.swing.GroupLayout panelKOTDetailsLayout = new javax.swing.GroupLayout(panelKOTDetails);
        panelKOTDetails.setLayout(panelKOTDetailsLayout);
        panelKOTDetailsLayout.setHorizontalGroup(
            panelKOTDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelKOTDetailsLayout.createSequentialGroup()
                .addGroup(panelKOTDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelKOTDetailsLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(panelKOTDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblManualAdvOrderNo, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panelKOTDetailsLayout.createSequentialGroup()
                                .addComponent(lblMessage, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 349, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(panelKOTDetailsLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(panelKOTDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtManualAdvOrderNo, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panelKOTDetailsLayout.createSequentialGroup()
                                .addGroup(panelKOTDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(lblShape, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblNote, javax.swing.GroupLayout.Alignment.LEADING))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelKOTDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 349, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtShape, javax.swing.GroupLayout.PREFERRED_SIZE, 349, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(6, 6, 6)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelKOTDetailsLayout.setVerticalGroup(
            panelKOTDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelKOTDetailsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelKOTDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblManualAdvOrderNo, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtManualAdvOrderNo, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(panelKOTDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(lblMessage, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE))
                .addGap(26, 26, 26)
                .addGroup(panelKOTDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtShape, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblShape, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(panelKOTDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelKOTDetailsLayout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelKOTDetailsLayout.createSequentialGroup()
                        .addGap(49, 49, 49)
                        .addComponent(lblNote, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(213, Short.MAX_VALUE))
        );

        tabPaneAdvOrder.addTab("KOT Details", panelKOTDetails);

        NewTab.setBackground(new java.awt.Color(255, 255, 255));

        btnBrows.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnBrows.setForeground(new java.awt.Color(255, 255, 255));
        btnBrows.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnBrows.setText("Browse");
        btnBrows.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnBrows.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnBrows.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnBrowsMouseClicked(evt);
            }
        });
        btnBrows.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnBrowsActionPerformed(evt);
            }
        });

        btnBrowseSpecialSymbol.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnBrowseSpecialSymbol.setForeground(new java.awt.Color(255, 255, 255));
        btnBrowseSpecialSymbol.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnBrowseSpecialSymbol.setText("Browse");
        btnBrowseSpecialSymbol.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnBrowseSpecialSymbol.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnBrowseSpecialSymbol.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnBrowseSpecialSymbolMouseClicked(evt);
            }
        });
        btnBrowseSpecialSymbol.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnBrowseSpecialSymbolActionPerformed(evt);
            }
        });

        jLabel5.setText("Special Symbol");

        lblImage.setText("Item Image");

        btnAttatchImage.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnAttatchImage.setForeground(new java.awt.Color(255, 255, 255));
        btnAttatchImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnAttatchImage.setText("<html>Attatch<br> Image</html>");
        btnAttatchImage.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnAttatchImage.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnAttatchImage.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnAttatchImageActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout NewTabLayout = new javax.swing.GroupLayout(NewTab);
        NewTab.setLayout(NewTabLayout);
        NewTabLayout.setHorizontalGroup(
            NewTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(NewTabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(NewTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblSymbolImage, javax.swing.GroupLayout.PREFERRED_SIZE, 453, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, NewTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(NewTabLayout.createSequentialGroup()
                            .addComponent(lblImage, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnBrows, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(NewTabLayout.createSequentialGroup()
                            .addComponent(lblItemNameForImage, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jLabel5)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnBrowseSpecialSymbol, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(btnAttatchImage, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(lblItemlImage, javax.swing.GroupLayout.PREFERRED_SIZE, 453, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(48, Short.MAX_VALUE))
        );

        NewTabLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel5, lblImage});

        NewTabLayout.setVerticalGroup(
            NewTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(NewTabLayout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(lblItemlImage, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(NewTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBrows, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblImage, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addComponent(lblSymbolImage, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(NewTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(NewTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnBrowseSpecialSymbol, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblItemNameForImage, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(NewTabLayout.createSequentialGroup()
                        .addComponent(btnAttatchImage, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        tabPaneAdvOrder.addTab("Image", NewTab);

        panelDetails.setBackground(new java.awt.Color(255, 255, 255));
        panelDetails.setPreferredSize(new java.awt.Dimension(450, 495));

        tblCharactersticsMaster.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Name", "Value", "Code", "ItemCode", "Type"
            }
        )
        {
            boolean[] canEdit = new boolean []
            {
                false, true, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return canEdit [columnIndex];
            }
        });
        tblCharactersticsMaster.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tblCharactersticsMasterMouseClicked(evt);
            }
        });
        jScrollPane8.setViewportView(tblCharactersticsMaster);
        if (tblCharactersticsMaster.getColumnModel().getColumnCount() > 0)
        {
            tblCharactersticsMaster.getColumnModel().getColumn(2).setMinWidth(2);
            tblCharactersticsMaster.getColumnModel().getColumn(2).setPreferredWidth(2);
            tblCharactersticsMaster.getColumnModel().getColumn(2).setMaxWidth(2);
            tblCharactersticsMaster.getColumnModel().getColumn(3).setMinWidth(2);
            tblCharactersticsMaster.getColumnModel().getColumn(3).setPreferredWidth(2);
            tblCharactersticsMaster.getColumnModel().getColumn(3).setMaxWidth(2);
            tblCharactersticsMaster.getColumnModel().getColumn(4).setMinWidth(2);
            tblCharactersticsMaster.getColumnModel().getColumn(4).setPreferredWidth(2);
            tblCharactersticsMaster.getColumnModel().getColumn(4).setMaxWidth(2);
        }

        btnApplyTextChar.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnApplyTextChar.setForeground(new java.awt.Color(255, 255, 255));
        btnApplyTextChar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnApplyTextChar.setText("APPLY");
        btnApplyTextChar.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 255), 1, true));
        btnApplyTextChar.setBorderPainted(false);
        btnApplyTextChar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnApplyTextChar.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnApplyTextChar.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnApplyTextCharActionPerformed(evt);
            }
        });

        txtCharValueSearch.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtCharValueSearch.addFocusListener(new java.awt.event.FocusAdapter()
        {
            public void focusGained(java.awt.event.FocusEvent evt)
            {
                txtCharValueSearchFocusGained(evt);
            }
        });
        txtCharValueSearch.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtCharValueSearchMouseClicked(evt);
            }
        });
        txtCharValueSearch.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtCharValueSearchKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt)
            {
                txtCharValueSearchKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt)
            {
                txtCharValueSearchKeyTyped(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel1.setText("Value :");
        jLabel1.setMaximumSize(new java.awt.Dimension(42, 17));
        jLabel1.setMinimumSize(new java.awt.Dimension(42, 17));
        jLabel1.setPreferredSize(new java.awt.Dimension(42, 17));

        btnApplyValueChar.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnApplyValueChar.setForeground(new java.awt.Color(255, 255, 255));
        btnApplyValueChar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnApplyValueChar.setText("APPLY VALUE");
        btnApplyValueChar.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 255), 1, true));
        btnApplyValueChar.setBorderPainted(false);
        btnApplyValueChar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnApplyValueChar.setPreferredSize(new java.awt.Dimension(139, 42));
        btnApplyValueChar.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnApplyValueChar.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnApplyValueCharActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelDetailsLayout = new javax.swing.GroupLayout(panelDetails);
        panelDetails.setLayout(panelDetailsLayout);
        panelDetailsLayout.setHorizontalGroup(
            panelDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelDetailsLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblHItemCode, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblHCharCode, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(136, 136, 136))
            .addGroup(panelDetailsLayout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(panelDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(txtCharValueSearch)
                        .addGroup(panelDetailsLayout.createSequentialGroup()
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(cmbCharValue, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnApplyValueChar, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panelDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addGroup(panelDetailsLayout.createSequentialGroup()
                            .addGap(0, 0, Short.MAX_VALUE)
                            .addComponent(lblHItemName, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(63, 63, 63))
                        .addComponent(lblItemName, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 342, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(panelDetailsLayout.createSequentialGroup()
                            .addComponent(txtTextCharValue, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnApplyTextChar, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)
                            .addGap(4, 4, 4)))
                    .addComponent(lblTextCharName, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(89, 89, 89))
        );
        panelDetailsLayout.setVerticalGroup(
            panelDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDetailsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblItemName, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtCharValueSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnApplyValueChar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmbCharValue, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)))
                .addGroup(panelDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelDetailsLayout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(lblHItemName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelDetailsLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(lblTextCharName, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTextCharValue, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnApplyTextChar))
                .addGap(23, 23, 23)
                .addGroup(panelDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblHItemCode, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblHCharCode, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(48, 48, 48))
        );

        tabPaneAdvOrder.addTab("Characteristics", panelDetails);

        btnHome.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnHome.setForeground(new java.awt.Color(255, 255, 255));
        btnHome.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnHome.setText("HOME");
        btnHome.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 255), 1, true));
        btnHome.setBorderPainted(false);
        btnHome.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnHome.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnHome.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnHomeMouseClicked(evt);
            }
        });
        btnHome.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnHomeActionPerformed(evt);
            }
        });

        btnDone.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnDone.setForeground(new java.awt.Color(255, 255, 255));
        btnDone.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnDone.setText("DONE");
        btnDone.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 255), 1, true));
        btnDone.setBorderPainted(false);
        btnDone.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDone.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnDone.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnDoneActionPerformed(evt);
            }
        });

        btnModifier.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnModifier.setForeground(new java.awt.Color(255, 255, 255));
        btnModifier.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnModifier.setText("MODIFIER");
        btnModifier.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnModifier.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnModifier.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnModifierActionPerformed(evt);
            }
        });

        btnPlu.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnPlu.setForeground(new java.awt.Color(255, 255, 255));
        btnPlu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnPlu.setText("PLU");
        btnPlu.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPlu.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnPlu.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnPluActionPerformed(evt);
            }
        });

        btnSettle1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnSettle1.setForeground(new java.awt.Color(255, 255, 255));
        btnSettle1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnSettle1.setText("<html><body>DIRECT<br>BILLER</body></html>");
        btnSettle1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 255), 1, true));
        btnSettle1.setBorderPainted(false);
        btnSettle1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSettle1.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnSettle1.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnSettle1MouseClicked(evt);
            }
        });
        btnSettle1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnSettle1ActionPerformed(evt);
            }
        });

        btnWaiterName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnWaiterName.setForeground(new java.awt.Color(255, 255, 255));
        btnWaiterName.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnWaiterName.setText("ORDER BY");
        btnWaiterName.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 255), 1, true));
        btnWaiterName.setBorderPainted(false);
        btnWaiterName.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnWaiterName.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnWaiterName.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnWaiterNameMouseClicked(evt);
            }
        });

        btnHomeDel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnHomeDel.setForeground(new java.awt.Color(255, 255, 255));
        btnHomeDel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnHomeDel.setText("HOME DELIVERY");
        btnHomeDel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 255), 1, true));
        btnHomeDel.setBorderPainted(false);
        btnHomeDel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnHomeDel.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnHomeDel.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnHomeDelMouseClicked(evt);
            }
        });
        btnHomeDel.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnHomeDelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelFormBodyLayout = new javax.swing.GroupLayout(panelFormBody);
        panelFormBody.setLayout(panelFormBodyLayout);
        panelFormBodyLayout.setHorizontalGroup(
            panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelFormBodyLayout.createSequentialGroup()
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnHome, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnModifier, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnPlu, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(panelItemDtl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addComponent(btnWaiterName, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnHomeDel, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(39, 39, 39)
                        .addComponent(btnDone, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnSettle1, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(56, Short.MAX_VALUE))
                    .addComponent(tabPaneAdvOrder, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        panelFormBodyLayout.setVerticalGroup(
            panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFormBodyLayout.createSequentialGroup()
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(panelItemDtl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tabPaneAdvOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnModifier, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnHome)
                        .addComponent(btnPlu, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnWaiterName)
                        .addComponent(btnHomeDel, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnDone)
                    .addComponent(btnSettle1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 13, Short.MAX_VALUE))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 16;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 10, 11, 23);
        panelLayout.add(panelFormBody, gridBagConstraints);

        getContentPane().add(panelLayout, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void funPopularItem() throws Exception
    {
        funResetItemNames();
        int cou = 0;
        int i = 0;
        JButton[] btnPopularArray =
        {
            btnIItem1, btnIItem2, btnIItem3, btnIItem4, btnIItem5, btnIItem6, btnIItem7, btnIItem8, btnIItem9, btnIItem10, btnIItem11, btnIItem12, btnIItem13, btnIItem14, btnIItem15, btnIItem16
        };
        btnNextItem.setEnabled(false);
        btnPrevItem.setEnabled(false);
        nextItemClick = 0;
        ResultSet rsPopularItems = clsGlobalVarClass.dbMysql.executeResultSet("select count(strItemName) from tblmenuitempricingdtl ");
        while (rsPopularItems.next())
        {
            cou = rsPopularItems.getInt(1);
        }
        rsPopularItems.close();
        itemNames = new String[cou];
        sql = "SELECT strItemName FROM tblmenuitempricingdtl where strPopular='Y' "
                + "and (strPosCode='" + clsGlobalVarClass.gPOSCode + "' or strPosCode='All') "
                + "and (strAreaCode='" + clsGlobalVarClass.gDineInAreaForDirectBiller + "' or strAreaCode='') ";
        rsPopularItems = clsGlobalVarClass.dbMysql.executeResultSet(sql);
        while (rsPopularItems.next())
        {
            String s = rsPopularItems.getString(1);
            if (i < 16)
            {
                if (s.contains(" "))
                {
                    StringBuilder sb1 = new StringBuilder(s);
                    int len = sb1.length();
                    int seq = sb1.lastIndexOf(" ");
                    String split = sb1.substring(0, seq);
                    String last = sb1.substring(seq + 1, len);
                    btnPopularArray[i].setText("<html>" + split + "<br>" + last + "</html>");
                    itemNames[i] = "<html>" + split + "<br>" + last + "</html>";
                }
                else
                {
                    btnPopularArray[i].setText(s);
                    itemNames[i] = s;
                }
                btnPopularArray[i].setEnabled(true);
            }
            else
            {
                if (s.contains(" "))
                {
                    StringBuilder sb1 = new StringBuilder(s);
                    int len = sb1.length();
                    int seq = sb1.lastIndexOf(" ");
                    String split = sb1.substring(0, seq);
                    String last = sb1.substring(seq + 1, len);
                    itemNames[i] = "<html>" + split + "<br>" + last + "</html>";
                }
                else
                {
                    itemNames[i] = s;
                }
            }
            i++;
        }
        for (int j = i; j < 16; j++)
        {
            btnPopularArray[j].setEnabled(false);
        }
        if (!"NA".equalsIgnoreCase(clsGlobalVarClass.gMenuItemSortingOn))
        {
            flgPopular = true;
            funFillTopButtonList(menuHeadCode);
        }
        else
        {
            flgPopular = false;
        }
    }

    /*
     * this methode refresh the Item
     * when another MenuName click
     */
    private void funResetItemButtonText(String MenuName)
    {
        int i = 0;
        JButton[] btnSubMenuArray =
        {
            btnIItem1, btnIItem2, btnIItem3, btnIItem4, btnIItem5, btnIItem6, btnIItem7, btnIItem8, btnIItem9, btnIItem10, btnIItem11, btnIItem12, btnIItem13, btnIItem14, btnIItem15, btnIItem16
        };
        try
        {
            btnNextItem.setEnabled(false);
            btnPrevItem.setEnabled(false);
            nextItemClick = 0;
            sql = "select strMenuCode from tblmenuhd where strMenuName='" + MenuName + "'";
            ResultSet rsItems = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            if (rsItems.next())
            {
                menuHeadCode = rsItems.getString(1);
            }

            if (clsGlobalVarClass.gAreaWisePricing.equals("N"))
            {
                sql = "SELECT count(strItemName) FROM tblmenuitempricingdtl "
                        + "WHERE strMenuCode = '" + menuHeadCode + "'"
                        + " and (strPosCode='" + clsGlobalVarClass.gPOSCode + "' or strPosCode='All')"
                        + "ORDER BY strItemName ASC";

            }
            else
            {
                sql = "SELECT count(strItemName) FROM tblmenuitempricingdtl "
                        + "WHERE strAreaCode='" + clsGlobalVarClass.gDineInAreaForDirectBiller + "' and strMenuCode = '" + menuHeadCode + "'"
                        + " and (strPosCode='" + clsGlobalVarClass.gPOSCode + "' or strPosCode='All')"
                        + "ORDER BY strItemName ASC";

            }
            rsItems = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            rsItems.next();
            int cn = rsItems.getInt(1);
            if (cn > 8)
            {
                btnNextItem.setEnabled(true);
            }
            itemNames = new String[cn];

            if (clsGlobalVarClass.gAreaWisePricing.equals("N"))
            {
                sql = "SELECT b.strItemName,a.strTextColor FROM tblmenuitempricingdtl a,tblitemmaster b "
                        + "WHERE a.strItemCode=b.strItemCode and a.strMenuCode = '" + menuHeadCode + "'"
                        + " and (a.strPosCode='" + clsGlobalVarClass.gPOSCode + "' or a.strPosCode='All') ";
            }
            else
            {
                sql = "SELECT b.strItemName,a.strTextColor FROM tblmenuitempricingdtl a,tblitemmaster b "
                        + "WHERE a.strItemCode=b.strItemCode and a.strAreaCode='" + clsGlobalVarClass.gDineInAreaForDirectBiller + "' "
                        + "and a.strMenuCode = '" + menuHeadCode + "'"
                        + " and (a.strPosCode='" + clsGlobalVarClass.gPOSCode + "' or a.strPosCode='All') ";

            }
            if (clsGlobalVarClass.gMenuItemSequence.equals("Ascending"))
            {
                sql += "ORDER BY strItemName ASC;";
            }

            rsItems = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            while (rsItems.next())
            {
                String s = rsItems.getString(1);
                String txtColor = rsItems.getString(2);
                if (i < 16)
                {
                    if (s.contains(" "))
                    {
                        StringBuilder sb1 = new StringBuilder(s);
                        int len = sb1.length();
                        int seq = sb1.lastIndexOf(" ");
                        String split = sb1.substring(0, seq);
                        String last = sb1.substring(seq + 1, len);
                        btnSubMenuArray[i].setText("<html>" + split + "<br>" + last + "</html>");

                        switch (txtColor)
                        {
                            case "Red":
                                btnSubMenuArray[i].setForeground(Color.BLACK);
                                btnSubMenuArray[i].setBackground(Color.red);
                                break;
                            case "Black":
                                btnSubMenuArray[i].setForeground(Color.BLACK);
                                btnSubMenuArray[i].setBackground(Color.LIGHT_GRAY);
                                break;
                            case "Green":
                                btnSubMenuArray[i].setForeground(Color.BLACK);
                                btnSubMenuArray[i].setBackground(Color.GREEN);
                                break;
                            case "CYAN":
                                btnSubMenuArray[i].setForeground(Color.BLACK);
                                btnSubMenuArray[i].setBackground(Color.CYAN);
                                break;
                            case "MAGENTA":
                                btnSubMenuArray[i].setForeground(Color.BLACK);
                                btnSubMenuArray[i].setBackground(Color.MAGENTA);
                                break;
                            case "ORANGE":
                                btnSubMenuArray[i].setForeground(Color.BLACK);
                                btnSubMenuArray[i].setBackground(Color.ORANGE);
                                break;
                            case "PINK":
                                btnSubMenuArray[i].setForeground(Color.BLACK);
                                btnSubMenuArray[i].setBackground(Color.PINK);
                                break;
                            case "YELLOW":
                                btnSubMenuArray[i].setForeground(Color.BLACK);
                                btnSubMenuArray[i].setBackground(Color.YELLOW);
                                break;
                            default:
                                btnSubMenuArray[i].setForeground(Color.BLACK);
                                break;
                        }
                        itemNames[i] = "<html>" + split + "<br>" + last + "</html>";
                    }
                    else
                    {
                        btnSubMenuArray[i].setText(s);
                        itemNames[i] = s;
                    }
                    btnSubMenuArray[i].setEnabled(true);
                }
                else
                {
                    if (s.contains(" "))
                    {
                        StringBuilder sb1 = new StringBuilder(s);
                        int len = sb1.length();
                        int seq = sb1.lastIndexOf(" ");
                        String split = sb1.substring(0, seq);
                        String last = sb1.substring(seq + 1, len);
                        itemNames[i] = "<html>" + split + "<br>" + last + "</html>";
                    }
                    else
                    {
                        itemNames[i] = s;
                    }
                }
                i++;
            }
            for (int j = i; j < 16; j++)
            {
                btnSubMenuArray[j].setEnabled(false);
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    private void funDisableSelectedQtyBtn(String strQty)
    {
        JButton arrBtnQty[] =
        {
            btnNumber1, btnNumber2, btnNumber3, btnNumber4, btnNumber5, btnNumber6, btnNumber7, btnNumber8, btnNumber9, btnMultiQty
        };
        for (int cnt = 0; cnt < arrBtnQty.length; cnt++)
        {
            if (strQty.equals(arrBtnQty[cnt].getText()))
            {
                arrBtnQty[cnt].setEnabled(false);
                break;
            }
        }
    }

    private void funSetSelectedQty(String buttonName)
    {
        try
        {
            funDisableSelectedQtyBtn(buttonName);
            if ("".equals(lblAdvOrderno.getText()))
            {
                selectedQty = Double.parseDouble(buttonName);
            }

            if (flgChangeQty == true)
            {
                funChangeQtyOfItem(buttonName);
                flgChangeQty = false;
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void funChangeQtyOfItem(String qty)
    {
        try
        {
            String tempItemName = tblItemTable.getValueAt(tblItemTable.getSelectedRow(), 0).toString();
            String tempItemCode = "";
            if (clsGlobalVarClass.gProductionLinkup)
            {
                tempItemCode = tblItemTable.getValueAt(tblItemTable.getSelectedRow(), 4).toString();
            }
            else
            {
                tempItemCode = tblItemTable.getValueAt(tblItemTable.getSelectedRow(), 3).toString();
            }
            selectedQty = Double.parseDouble(qty);
            if (!clsGlobalVarClass.gNegBilling)
            {
                if (!clsGlobalVarClass.funCheckNegativeStock(tempItemCode, selectedQty))
                {
                    selectedQty = 1;
                }
            }
            int selectrow = tblItemTable.getSelectedRow();
            double price = Double.parseDouble(tblItemTable.getModel().getValueAt(selectrow, 2).toString());
            double rowQty = Double.parseDouble(tblItemTable.getModel().getValueAt(selectrow, 1).toString());
            price = price / rowQty;

            price = price * Double.parseDouble(qty);
            sql = "update tbladvbookitemtemp set strItemName='" + tempItemName + "'"
                    + ",dblItemQuantity=" + qty + ",dblAmount=" + price + ""
                    + ",strUserEdited='" + clsGlobalVarClass.gUserCode + "',dteDateEdited='" + clsGlobalVarClass.getCurrentDateTime() + "' "
                    + "where strItemCode='" + tempItemCode + "'";
            clsGlobalVarClass.dbMysql.execute(sql);
            selectedQty = 1;
            funFillAdvanceOrderItemGrid();
            flgChangeQty = false;
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
        finally
        {
            clsGlobalVarClass.gNumerickeyboardValue = null;
        }
    }

    private void funItemTablePressed(String clickType)
    {
        try
        {
            if (tblItemTable.getRowCount() > 0)
            {
                flgChangeQty = true;
                int columnNo = tblItemTable.getSelectedColumn();
                int rowNo = tblItemTable.getSelectedRow();
                String itemCode = "";
                if (clsGlobalVarClass.gProductionLinkup)
                {
                    itemCode = tblItemTable.getValueAt(rowNo, 4).toString();
                }
                else
                {
                    itemCode = tblItemTable.getValueAt(rowNo, 3).toString();
                }
                String itemName = tblItemTable.getValueAt(rowNo, 0).toString();
                lblItemNameForImage.setText(itemName);

                if (hmAdvOrderType.containsKey(itemCode))
                {
                }
                else
                {
                    lblSymbolImage.setText("");
                }

                if (clsGlobalVarClass.gProductionLinkup)
                {
                    //  jPanelRadioButton.removeAll();
                    //   jPanelRadioButton.revalidate();
                    // jPanelRadioButton.repaint();
                    funFillCharactersticsMaster(itemCode, itemName);
                    funSetImageIcon(itemCode);
                }

                if (clickType.equals("Mouse"))
                {
                    if (columnNo == 1)
                    {
                        frmNumberKeyPad num = new frmNumberKeyPad(this, true, "qty");
                        num.setVisible(true);
                        if (null != clsGlobalVarClass.gNumerickeyboardValue)
                        {
                            if (Double.parseDouble(clsGlobalVarClass.gNumerickeyboardValue) > 0)
                            {
                                funChangeQtyOfItem(clsGlobalVarClass.gNumerickeyboardValue);
                            }
                        }
                    }
                    else if (columnNo == 3)
                    {
                        if (clsGlobalVarClass.gProductionLinkup)
                        {
                            frmNumberKeyPad num = new frmNumberKeyPad(this, true, "Weight");
                            num.setVisible(true);
                            if (null != clsGlobalVarClass.gNumerickeyboardValue)
                            {
                                if (Double.parseDouble(clsGlobalVarClass.gNumerickeyboardValue) > 0)
                                {
                                    funChangeWeightOfItem(clsGlobalVarClass.gNumerickeyboardValue);
                                }
                            }
                        }

                    }
                }
                else
                {
                    frmNumberKeyPad num = new frmNumberKeyPad(this, true, "qty");
                    num.setVisible(true);
                    if (null != clsGlobalVarClass.gNumerickeyboardValue)
                    {
                        if (Double.parseDouble(clsGlobalVarClass.gNumerickeyboardValue) > 0)
                        {
                            funChangeQtyOfItem(clsGlobalVarClass.gNumerickeyboardValue);
                        }
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

    /*
     * to Fill the Jtable for Running The Order
     */
    private void funFillAdvanceOrderItemGrid()
    {
        try
        {
            totalAmt = new BigDecimal("0.00");
            String item = null, quantity = null, weight = null, amount = null, price = "0.00";
            DefaultTableModel dm = new DefaultTableModel()
            {
                @Override
                public boolean isCellEditable(int row, int column)
                {
                    //all cells false
                    return false;
                }
            };

            dm.addColumn("Description");
            dm.addColumn("Qty");
            dm.addColumn("Amount");

            if (clsGlobalVarClass.gProductionLinkup)
            {
                dm.addColumn("Wgt");
                dm.addColumn("ItemCode");
                dm.addColumn("Price");
                ResultSet rsAdvBook = clsGlobalVarClass.dbMysql.executeResultSet("select strItemName,dblItemQuantity"
                        + " ,dblWeight,dblAmount,strItemCode,dblPrice "
                        + " from tbladvbookitemtemp "
                        + " where strPosCode='" + clsGlobalVarClass.gPOSCode + "' "
                        + " and strUserCreated='" + clsGlobalVarClass.gUserCode + "' "
                        + " order by strSerialno ASC");

                while (rsAdvBook.next())
                {
                    item = rsAdvBook.getString(1);
                    quantity = rsAdvBook.getString(2);
                    amount = rsAdvBook.getString(4);
                    weight = rsAdvBook.getString(3);
                    BigDecimal tempAmt = new BigDecimal(amount);
                    totalAmt = totalAmt.add(tempAmt);
                    Object[] rows =
                    {
                        item, quantity, amount, weight, rsAdvBook.getString(5), rsAdvBook.getString(6)
                    };
                    dm.addRow(rows);
                }
                rsAdvBook.close();
                txtTotal.setText(totalAmt.toString());
                netTotalAmt = totalAmt.subtract(discountAmt);
                grandTotalAmt = netTotalAmt;
                tblItemTable.setModel(dm);
                DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
                rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
                tblItemTable.setShowHorizontalLines(true);
                tblItemTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                tblItemTable.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
                tblItemTable.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
                tblItemTable.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
                tblItemTable.getColumnModel().getColumn(0).setPreferredWidth(170);
                tblItemTable.getColumnModel().getColumn(1).setPreferredWidth(40);
                tblItemTable.getColumnModel().getColumn(2).setPreferredWidth(83);
                tblItemTable.getColumnModel().getColumn(3).setPreferredWidth(50);
                tblItemTable.getColumnModel().getColumn(4).setPreferredWidth(5);
                tblItemTable.getColumnModel().getColumn(5).setPreferredWidth(5);
            }
            else
            {
                dm.addColumn("ItemCode");
                ResultSet rsAdvBook = clsGlobalVarClass.dbMysql.executeResultSet("select strItemName,dblItemQuantity,dblWeight,dblAmount,strItemCode "
                        + "from tbladvbookitemtemp "
                        + "where strPosCode='" + clsGlobalVarClass.gPOSCode + "' "
                        + "and strUserCreated='" + clsGlobalVarClass.gUserCode + "' "
                        + "order by strSerialno ASC");
                while (rsAdvBook.next())
                {
                    item = rsAdvBook.getString(1);
                    quantity = rsAdvBook.getString(2);
                    amount = rsAdvBook.getString(4);
                    weight = rsAdvBook.getString(3);
                    BigDecimal tempAmt = new BigDecimal(amount);
                    totalAmt = totalAmt.add(tempAmt);
                    Object[] rows =
                    {
                        item, quantity, amount, rsAdvBook.getString(5)
                    };
                    dm.addRow(rows);
                }
                rsAdvBook.close();
                txtTotal.setText(totalAmt.toString());
                netTotalAmt = totalAmt.subtract(discountAmt);
                grandTotalAmt = netTotalAmt;
                tblItemTable.setModel(dm);
                DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
                rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
                tblItemTable.setShowHorizontalLines(true);
                tblItemTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                tblItemTable.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
                tblItemTable.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);

                tblItemTable.getColumnModel().getColumn(0).setPreferredWidth(190);
                tblItemTable.getColumnModel().getColumn(1).setPreferredWidth(70);
                tblItemTable.getColumnModel().getColumn(2).setPreferredWidth(90);
                tblItemTable.getColumnModel().getColumn(3).setPreferredWidth(5);
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    public void getModifierRate(String ModifierName)
    {
        try
        {
            String modifierCode = null;
            double rate = 0;
            sql = "select strModifierCode from tblModifierMaster "
                    + "where strModifierName='" + ModifierName + "'";//to retrive the modifiercode            
            ResultSet rsModifier = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            if (rsModifier.next())
            {
                modifierCode = rsModifier.getString(1);
            }
            rsModifier.close();

            //retrive the rate of modifier
            sql = "select dblRate from tblitemmodofier "
                    + "where strModifierCode='" + modifierCode + "' and strChargable='Y' "
                    + "and strApplicable='Y'";
            rsModifier = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            while (rsModifier.next())
            {
                rate = rsModifier.getDouble(1);
            }
            rsModifier.close();
            int r = tblItemTable.getSelectedRow();
            int serialno = 0;
            String tempitemcode = null;
            String tempitemName = (String) tblItemTable.getValueAt(r, 0);
            sql = "select strSerialno,strItemCode,dblItemQuantity "
                    + "from tbladvbookitemtemp "
                    + "where strItemName='" + tempitemName + "' and  strPosCode='" + clsGlobalVarClass.gPOSCode + "' "
                    + "and strUserCreated='" + clsGlobalVarClass.gUserCode + "'";
            ResultSet rsAdvBook = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            if (rsAdvBook.next())
            {
                serialno = rsAdvBook.getInt(1);
                tempitemcode = rsAdvBook.getString(2);
            }
            rsAdvBook.close();
            String s = Integer.toString(serialno);
            s = s.concat(".1");
            if (selectedQty > 0)//check the Qty select or not
            {
                double finalAmt = rate * selectedQty;
                sql = "insert into tbladvbookitemtemp(strSerialno,strPosCode,strItemCode,strItemName,"
                        + "dblItemQuantity,dblAmount,strUserCreated,strUserEdited,dteDateCreated,dteDateEdited)"
                        + " values('" + s + "','" + clsGlobalVarClass.gPOSCode + "','" + tempitemcode.concat(modifierCode) + "','"
                        + ModifierName + "','" + selectedQty + "','" + finalAmt + "','" + clsGlobalVarClass.gUserCode
                        + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.getCurrentDateTime() + "'"
                        + ",'" + clsGlobalVarClass.getCurrentDateTime() + "')";//insert the modifier data in tblitemtemp
                clsGlobalVarClass.dbMysql.execute(sql);
                selectedQty = 1;
                funFillAdvanceOrderItemGrid();
            }
            else
            {
                new frmOkPopUp(null, "Please select quantity first", "Error", 1).setVisible(true);
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    private void funSaveAdvOrderBill(String orderType)
    {
        try
        {
            orderdate = (dteOrderDate.getDate().getYear() + 1900) + "-"
                    + (dteOrderDate.getDate().getMonth() + 1) + "-"
                    + (dteOrderDate.getDate().getDate());
            if (cmbHour.getSelectedItem() == "HH")
            {
                JOptionPane.showMessageDialog(this, "Please Enter vaild Hour in From Time");
                return;
            }
            if (cmbMinute.getSelectedItem() == "MM")
            {
                JOptionPane.showMessageDialog(this, "Please Enter vaild Minute in From Time");
                return;
            }
            if (clsGlobalVarClass.gCustCodeForAdvOrder == null)
            {
                new frmOkPopUp(null, "Please fill Customer Information", "Warning", 1).setVisible(true);
            }
            else
            {
                Date posDateDate = null;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try
                {
                    posDateDate = sdf.parse(posDate);
                }
                catch (ParseException e)
                {
                    objUtility.funWriteErrorLog(e);
                    e.printStackTrace();
                }
                long time = (dteOrderDate.getDate().getTime() - posDateDate.getTime());
                long days = time / (24 * 60 * 60 * 1000);

                if (days < 0)
                {
                    new frmOkPopUp(null, "Please Enter valid Order Date", "Warning", 1).setVisible(true);
                    return;
                }
                String strCustomerCode = null;
                String settlementMode = "";
                strDeliveryTime = cmbHour.getSelectedItem() + ":" + cmbMinute.getSelectedItem() + " " + cmbAMPM.getSelectedItem();
                String orderdate1 = orderdate + " " + strDeliveryTime;
                int ch = JOptionPane.showConfirmDialog(this, "<html>Order For Date is<br>" + orderdate1 + " " + "<br>Do You Want to Continue</html>", "Order for Date", JOptionPane.YES_NO_OPTION);
                if (ch == JOptionPane.YES_OPTION)
                {
                    strCustomerCode = clsGlobalVarClass.gCustCodeForAdvOrder;
                    subTotalAmt = new BigDecimal("0");
                    int tRows = tblItemTable.getRowCount();
                    String dbGrandTotal = txtTotal.getText();
                    if (tRows == 0)
                    {
                        new frmOkPopUp(null, "", "Please Select Item", 1).setVisible(true);
                    }
                    else if ("".equals(lblAdvOrderno.getText()))
                    {
                        String advOrderNo = funGenerateAdvanceOrderNo();
                        for (int row = 0; row < tblItemTable.getRowCount(); row++)
                        {
                            subTotalAmt = subTotalAmt.add(new BigDecimal(tblItemTable.getValueAt(row, 2).toString()));
                        }

                        if (homeDelivery.equals("Y"))
                        {
                            String buildingCode = "", custTypeCode = "";
                            sql = "select strBuldingCode,strCustomerType from tblcustomermaster "
                                    + " where strCustomerCode='" + clsGlobalVarClass.gCustCodeForAdvOrder + "'";
                            ResultSet rsBuilding = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                            if (rsBuilding.next())
                            {
                                buildingCode = rsBuilding.getString(1);
                                custTypeCode = rsBuilding.getString(2);
                            }
                            rsBuilding.close();

                            double totalAmount = Double.parseDouble(dbGrandTotal.toString());
                            double minAmount = objUtility.funGetMinBillAmountForDelCharges(buildingCode, custTypeCode);
                            if (totalAmount < minAmount)
                            {
                                int res = JOptionPane.showConfirmDialog(this, "Bill Amount is " + totalAmount + " and minimum delivery charges amount is " + minAmount + " Do you want to continue?");
                                if (res != 0)
                                {
                                    return;
                                }
                            }
                            objUtility.funGetDeliveryCharges(buildingCode, totalAmount, clsGlobalVarClass.gCustCodeForAdvOrder);
                            totalAmount += clsGlobalVarClass.gDeliveryCharges;
                            dbGrandTotal = String.valueOf(totalAmount);
                        }
                        String message = txtMessage.getText().trim();
                        String shape = txtShape.getText().trim();
                        clsGlobalVarClass.gAdvOrderNoForBilling = advOrderNo;
                        String imgName = "";
                        String imgSplSymbol = "";
                        if (tempFile != null)
                        {
                            imgName = tempFile.getName();
                        }
                        if (fileImageSpecialSymbol != null)
                        {
                            imgSplSymbol = fileImageSpecialSymbol.getName();
                        }

                        String urgentOrder = "N";
                        if (chkUrgentOrder.isSelected())
                        {
                            urgentOrder = "Y";
                        }
                        orderdate = orderdate + " " + "0:00:00";
                        //set POS Date in dteAdvBookingDate
                        sql = "insert into tbladvbookbillhd(strAdvBookingNo,dteAdvBookingDate,dteOrderFor,"
                                + "strPOSCode,strSettelmentMode,dblDiscountAmt,dblDiscountPer,dblTaxAmt,dblSubTotal,"
                                + "dblGrandTotal,strUserCreated,strUserEdited,dteDateCreated,dteDateEdited,strClientCode"
                                + ",strCustomerCode,strMessage,strShape,strNote,intShiftCode,strDeliveryTime,strWaiterNo"
                                + ",strHomeDelivery,dblHomeDelCharges,strOrderType,strManualAdvOrderNo,strImageName"
                                + ",strSpecialsymbolImage,strUrgentOrder) "
                                + "values('" + advOrderNo + "','" + posDate + "','" + orderdate + "','" + clsGlobalVarClass.gPOSCode + "'"
                                + ",'" + settlementMode + "','" + discountAmt.toString() + "','" + discountPer.toString() + "','0.00'"
                                + ",'" + subTotalAmt + "','" + dbGrandTotal.toString() + "','" + clsGlobalVarClass.gUserCode
                                + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.getCurrentDateTime() + "',"
                                + "'" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.gClientCode + "',"
                                + "'" + strCustomerCode + "','" + message + "','" + shape + "','" + txtNote.getText().trim() + "'"
                                + ",'" + clsGlobalVarClass.gShiftNo + "','" + strDeliveryTime + "','" + waiterNo + "'"
                                + ",'" + homeDelivery + "'," + clsGlobalVarClass.gDeliveryCharges + ",'" + orderType + "'"
                                + ",'" + txtManualAdvOrderNo.getText().trim() + "','" + imgName + "','" + imgSplSymbol + "'"
                                + ",'" + urgentOrder + "')";
                        clsGlobalVarClass.dbMysql.execute(sql);
                        if (clsGlobalVarClass.gProductionLinkup)
                        {
                            funSaveCharDtlTable();
                            funSaveImageDtlToTable();
                        }
                        tempFile = null;
                        fileImageSpecialSymbol = null;
                        destFile = null;
                        lblItemlImage.setIcon(null);
                        lblSymbolImage.setIcon(null);

                        sql = "select strSerialno,strItemCode,strItemName,dblItemQuantity,dblWeight,dblAmount "
                                + " from tbladvbookitemtemp";
                        ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                        while (rs.next())
                        {
                            double itemQty = Double.parseDouble(rs.getString(4));
                            if (!rs.getString(2).contains("M"))
                            {
                                sql = "insert into tbladvbookbilldtl (strItemCode,strItemName,strAdvBookingNo,"
                                        + "dblQuantity,dblWeight,dblAmount,dblTaxAmount,dteAdvBookingDate,dteOrderFor,"
                                        + "strClientCode,strCustomerCode) "
                                        + "values('" + rs.getString(2) + "','" + rs.getString(3) + "','" + advOrderNo
                                        + "','" + itemQty + "','" + rs.getString(5) + "','" + rs.getString(6) + "','0.00','" + posDate + "','" + orderdate + "','" + clsGlobalVarClass.gClientCode
                                        + "','" + strCustomerCode + "')";
                                clsGlobalVarClass.dbMysql.execute(sql);
                            }

                            if (rs.getString(2).contains("M"))
                            {
                                StringBuilder sb1 = new StringBuilder(rs.getString(2));
                                int seq = sb1.lastIndexOf("M");//break the string(if itemcode contains Itemcode with modifier code then break the string into substring )
                                String itemCode = sb1.substring(0, seq);//SubString Itemcode
                                String modifierCode = sb1.substring(seq, sb1.length());//SubString modifier Code
                                sql = "insert into  tbladvordermodifierdtl(strAdvOrderNo,strItemCode,strModifierCode"
                                        + ",strModifierName,dblQuantity,dblAmount,strClientCode,strCustomerCode"
                                        + ",strUserCreated,strUserEdited,dteDateCreated,dteDateEdited) "
                                        + "values('" + advOrderNo + "','" + itemCode + "','" + modifierCode + "','" + rs.getString(3) + "'"
                                        + ",'" + itemQty + "','" + rs.getString(6) + "','" + clsGlobalVarClass.gClientCode + "','" + strCustomerCode + "'"
                                        + ",'" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "'"
                                        + ",'" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "')";
                                clsGlobalVarClass.dbMysql.execute(sql);
                            }
                        }
                        rs.close();
                        lblAdvOrderno.setText(advOrderNo);
                        clsGlobalVarClass.setAdvOrderNo(advOrderNo);
                        clsGlobalVarClass.setAdvanceAmt(txtTotal.getText());
                        clsGlobalVarClass.setAdvanceOrderForDate(orderdate);
                        hmTextChar.clear();
                        new frmAdvanceReceipt("AdvOrder", false).setVisible(true);
                        funTruncateTempTable();
                    }
                    else // Modify Adv Order
                    {
                        for (int row = 0; row < tblItemTable.getRowCount(); row++)
                        {
                            subTotalAmt = subTotalAmt.add(new BigDecimal(tblItemTable.getValueAt(row, 2).toString()));
                        }
                        String imgName = "";
                        String imgSplSymbol = "";
                        if (tempFile != null)
                        {
                            imgName = tempFile.getName();
                        }
                        if (fileImageSpecialSymbol != null)
                        {
                            imgSplSymbol = fileImageSpecialSymbol.getName();
                        }

                        if (homeDelivery.equals("Y"))
                        {
                            String buildingCode = "", custTypeCode = "";
                            sql = "select strBuldingCode,strCustomerType from tblcustomermaster "
                                    + " where strCustomerCode='" + clsGlobalVarClass.gCustCodeForAdvOrder + "'";
                            ResultSet rsBuilding = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                            if (rsBuilding.next())
                            {
                                buildingCode = rsBuilding.getString(1);
                                custTypeCode = rsBuilding.getString(2);
                            }
                            rsBuilding.close();
                            double totalAmount = Double.parseDouble(dbGrandTotal.toString());
                            double minAmount = objUtility.funGetMinBillAmountForDelCharges(buildingCode, custTypeCode);
                            if (totalAmount < minAmount)
                            {
                                int res = JOptionPane.showConfirmDialog(this, "Bill Amount is " + totalAmount + " and minimum delivery charges amount is " + minAmount + " Do you want to continue?");
                                if (res != 0)
                                {
                                    return;
                                }
                            }
                            objUtility.funGetDeliveryCharges(buildingCode, totalAmount, clsGlobalVarClass.gCustCodeForAdvOrder);
                            totalAmount += clsGlobalVarClass.gDeliveryCharges;
                            dbGrandTotal = String.valueOf(totalAmount);
                        }

                        String urgentOrder = "N";
                        if (chkUrgentOrder.isSelected())
                        {
                            urgentOrder = "Y";
                        }
                        String message = txtMessage.getText().toString();
                        String shape = txtShape.getText().toString();
                        //strMessage,strShape,strNote
                        sql = "update tbladvbookbillhd "
                                + "set dteOrderFor='" + orderdate + "',strPOSCode='" + clsGlobalVarClass.gPOSCode + "'"
                                + ",dblTaxAmt='0.00',dblSubTotal='" + subTotalAmt + "'"
                                + ",dblGrandTotal='" + dbGrandTotal.toString() + "',strUserEdited='" + clsGlobalVarClass.gUserCode + "'"
                                + ",dteDateEdited='" + clsGlobalVarClass.getCurrentDateTime() + "',strDataPostFlag='N'"
                                + ",strManualAdvOrderNo='" + txtManualAdvOrderNo.getText().trim() + "' "
                                + ",strImageName='" + imgName + "',strMessage='" + message + "',strShape='" + shape + "'"
                                + ",strNote='" + txtNote.getText().trim() + "',strSpecialsymbolImage='" + imgSplSymbol + "'"
                                + ",dblHomeDelCharges='" + clsGlobalVarClass.gDeliveryCharges + "',strUrgentOrder='" + urgentOrder + "' "
                                + "where strAdvBookingNo='" + lblAdvOrderno.getText() + "'";
                        clsGlobalVarClass.dbMysql.execute(sql);
                        tempFile = null;
                        fileImageSpecialSymbol = null;
                        destFile = null;
                        lblItemlImage.setIcon(null);
                        lblSymbolImage.setIcon(null);

                        if (clsGlobalVarClass.gProductionLinkup)
                        {
                            funSaveCharDtlTable();
                            funSaveImageDtlToTable();
                        }

                        clsGlobalVarClass.dbMysql.execute("Delete from tbladvordermodifierdtl where strAdvOrderNo='" + lblAdvOrderno.getText() + "'");
                        clsGlobalVarClass.dbMysql.execute("Delete from tbladvbookbilldtl where strAdvBookingNo='" + lblAdvOrderno.getText() + "'");
                        sql = "select strSerialno,strItemCode,strItemName,dblItemQuantity,dblWeight,dblAmount "
                                + " from tbladvbookitemtemp";
                        ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                        while (rs.next())
                        {
                            double itemQty = Double.parseDouble(rs.getString(4));
                            double itemWeight = Double.parseDouble(rs.getString(5));
                            String itemAmt = rs.getString(6);

                            if (!rs.getString(2).contains("M"))
                            {
                                sql = "insert into tbladvbookbilldtl "
                                        + "(strItemCode,strItemName,strAdvBookingNo,dblQuantity,dblWeight,dblAmount"
                                        + ",dblTaxAmount,dteAdvBookingDate,dteOrderFor,strClientCode,strCustomerCode) "
                                        + "values('" + rs.getString(2) + "','" + rs.getString(3) + "','" + lblAdvOrderno.getText() + "'"
                                        + ",'" + itemQty + "','" + itemWeight + "','" + itemAmt + "','0.00','" + posDate + "','" + orderdate + "'"
                                        + ",'" + clsGlobalVarClass.gClientCode + "','" + customerCode + "')";
                                clsGlobalVarClass.dbMysql.execute(sql);
                            }

                            if (rs.getString(2).contains("M"))
                            {
                                StringBuilder sb1 = new StringBuilder(rs.getString(2));
                                int seq = sb1.lastIndexOf("M");//break the string(if itemcode contains Itemcode with modifier code then break the string into substring )
                                String itemCode = sb1.substring(0, seq);//SubString Itemcode
                                String modifierCode = sb1.substring(seq, sb1.length());//SubString modifier Code
                                sql = "insert into  tbladvordermodifierdtl(strAdvOrderNo,strItemCode,strModifierCode,strModifierName,dblQuantity,dblAmount,strClientCode,strCustomerCode,strUserCreated,strUserEdited,dteDateCreated,dteDateEdited)"
                                        + " values('" + lblAdvOrderno.getText() + "','" + itemCode + "','" + modifierCode + "'"
                                        + ",'" + rs.getString(3) + "','" + itemQty + "','" + itemAmt + "','" + clsGlobalVarClass.gClientCode + "'"
                                        + ",'" + strCustomerCode + "','" + clsGlobalVarClass.gUserCode + "'"
                                        + ",'" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.getCurrentDateTime() + "'"
                                        + ",'" + clsGlobalVarClass.getCurrentDateTime() + "')";
                                clsGlobalVarClass.dbMysql.execute(sql);
                            }
                        }

                        clsGlobalVarClass.setAdvOrderNo(lblAdvOrderno.getText());
                        clsGlobalVarClass.setAdvanceAmt(txtTotal.getText());
                        clsGlobalVarClass.setAdvanceOrderForDate(orderdate);
                        hmTextChar.clear();
                        new frmAdvanceReceipt("AdvOrder", true).setVisible(true);
                        funTruncateTempTable();
                    }
                }
                else
                {
                    new frmOkPopUp(this, "Please enter Customer Information ", "Error", 1).setVisible(true);
                }
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    private void funHomeButtonPressed()
    {
        boolean flgHomeButton = false;
        try
        {
            if (tblItemTable.getRowCount() > 0)
            {
                frmOkCancelPopUp okOb = new frmOkCancelPopUp(this, "Do you want to end transaction");
                okOb.setVisible(true);
                int res = okOb.getResult();
                if (res == 1)
                {
                    flgHomeButton = true;
                    clsGlobalVarClass.hmActiveForms.remove("Advance Order");
                    funTruncateTempTable();
                    clsGlobalVarClass.gCustCodeForAdvOrder = null;
                    clsGlobalVarClass.setAdvOrderNo(null);
                    clsGlobalVarClass.gAdvOrderNoForBilling = null;
                    clsGlobalVarClass.setAdvanceOrderForDate(null);
                    clsGlobalVarClass.setAdvanceAmt(null);
                    hmAttachedImage = null;
                    dispose();
                }
            }
            else
            {
                flgHomeButton = true;
                clsGlobalVarClass.hmActiveForms.remove("Advance Order");
                clsGlobalVarClass.gCustCodeForAdvOrder = null;
                clsGlobalVarClass.setAdvOrderNo(null);
                clsGlobalVarClass.gAdvOrderNoForBilling = null;
                clsGlobalVarClass.setAdvanceOrderForDate(null);
                clsGlobalVarClass.setAdvanceAmt(null);
                hmAttachedImage = null;
                dispose();
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
        finally
        {
            if (flgHomeButton)
            {
                funFreeObjectsFromMemory();
            }
        }
    }

    private void funFreeObjectsFromMemory()
    {
        objUtility = null;
        objData = null;
        objPanelModifier = null;
        objData = null;
    }

    private String funConvertString(String ItemName)
    {
        if (ItemName.contains("<html>"))
        {
            String tempitemName = ItemName;
            StringBuilder sb1 = new StringBuilder(tempitemName);
            sb1 = sb1.delete(0, 6);
            int seq = sb1.lastIndexOf("<br>");
            String split = sb1.substring(0, seq);
            int end = sb1.lastIndexOf("</html>");
            String last = sb1.substring(seq + 4, end);

            ItemName = split + " " + last;
        }
        return ItemName;
    }

    private void funDeleteButtonPressed()
    {
        try
        {
            int r = tblItemTable.getSelectedRow();
            if (r == -1)
            {
                new frmOkPopUp(null, "Please Select Item", "Error", 1).setVisible(true);
            }
            else
            {
                //delete the selected item
                int ch = JOptionPane.showConfirmDialog(new JPanel(), "Do you want to delete item?", "Item Delete", JOptionPane.YES_NO_OPTION);
                if (ch == JOptionPane.YES_OPTION)
                {
                    String item = tblItemTable.getValueAt(r, 0).toString();
                    clsGlobalVarClass.dbMysql.execute("Delete from tbladvbookitemtemp where strItemName='" + item + "'");
                }
                funFillAdvanceOrderItemGrid();
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    private void funUPButtonPressed()
    {
        try
        {
            if (tblItemTable.getModel().getRowCount() > 0)
            {
                int selectedQty = tblItemTable.getSelectedRow();
                int rowcount = tblItemTable.getRowCount();
                if (selectedQty == -1)
                {
                    selectedQty = 0;
                    tblItemTable.changeSelection(selectedQty, 0, false, false);
                }
                else if (selectedQty == rowcount)
                {
                    selectedQty = 0;
                    tblItemTable.changeSelection(selectedQty, 0, false, false);
                }
                else if (selectedQty < rowcount)
                {
                    tblItemTable.changeSelection(selectedQty - 1, 0, false, false);
                }
            }
            else
            {
                new frmOkPopUp(null, "Please select Item first", "Error", 1).setVisible(true);
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    private void funDownButtonPressed()
    {
        try
        {
            if (tblItemTable.getModel().getRowCount() > 0)
            {
                int selectedQty = tblItemTable.getSelectedRow();
                int rowcount = tblItemTable.getRowCount();
                if (selectedQty < rowcount)
                {
                    tblItemTable.changeSelection(selectedQty + 1, 0, false, false);
                }
                else if (selectedQty == rowcount)
                {
                    selectedQty = 0;
                    tblItemTable.changeSelection(selectedQty, 0, false, false);
                }
            }
            else
            {
                new frmOkPopUp(null, "Please select Item first", "Error", 1).setVisible(true);
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    private void funPLUItemSearch()
    {
        try
        {
            String text = txtPLU_ItemSearch.getText().trim();
            DefaultTableModel dm_PLU_Item_Table = (DefaultTableModel) tbl_PLU_Items.getModel();
            final TableRowSorter<TableModel> sorter;
            sorter = new TableRowSorter<TableModel>(dm_PLU_Item_Table);
            tbl_PLU_Items.setRowSorter(sorter);
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
            sorter.setSortKeys(null);
            if (tbl_PLU_Items.getModel().getRowCount() > 0)
            {
                int r = tbl_PLU_Items.getSelectedRow();
                int rowcount = tbl_PLU_Items.getRowCount();
                if (r == -1)
                {
                    r = 0;
                    tbl_PLU_Items.changeSelection(r, 0, false, false);
                }
                else if (r == rowcount)
                {
                    r = 0;
                    tbl_PLU_Items.changeSelection(r, 0, false, false);
                }
                else if (r < rowcount)
                {
                    tbl_PLU_Items.changeSelection(r - 1, 0, false, false);
                }
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            txtPLU_ItemSearch.setText("");
        }
    }

    private void funDownArrowPressedForPLU()
    {
        if (tbl_PLU_Items.getModel().getRowCount() > 0)
        {
            int selectedRow = tbl_PLU_Items.getSelectedRow();
            int rowcount = tbl_PLU_Items.getRowCount();
            if (selectedRow < rowcount)
            {
                tbl_PLU_Items.changeSelection(selectedRow + 1, 0, false, false);
            }
            else if (selectedRow == rowcount)
            {
                selectedRow = 0;
                tbl_PLU_Items.changeSelection(selectedRow, 0, false, false);
            }
        }
    }

    private void funPLUTableItemsMouseClicked()
    {
        String tempItemName = tbl_PLU_Items.getValueAt(tbl_PLU_Items.getSelectedRow(), 0).toString();
        txtPLU_ItemSearch.setText("");
        String clsAreaCode = "";
        if (clsGlobalVarClass.gAreaWisePricing.equals("N"))
        {
            clsAreaCode = clsGlobalVarClass.gAreaCodeForTrans;
        }
        else
        {
            clsAreaCode = clsGlobalVarClass.gDineInAreaForDirectBiller;
        }
        Map<String, clsItemPriceDtl> x = clsPLUItemDtl.hmPLUItemDtl.get(clsAreaCode);
        clsItemPriceDtl objPrice = x.get(tempItemName);
        String itemName = objPrice.getStrItemName();
        String itemCode = objPrice.getStrItemCode();
        funGetPrice(itemName);
        funFillAdvanceOrderItemGrid();
        //fun_Close_PLU_Pannel();
        funPLUButtonPressed();
    }

    private void funPLUButtonPressed()
    {
        fun_isVisiblePanels(false);
        txtPLU_ItemSearch.requestFocus();
        funFillPLUTable();
    }

    private void funFillPLUTable()
    {
        try
        {
            DefaultTableModel dm_PLU_Item_Table = (DefaultTableModel) tbl_PLU_Items.getModel();
            dm_PLU_Item_Table.setRowCount(0);
            String clsAreaCode = "";
            if (clsGlobalVarClass.gAreaWisePricing.equals("N"))
            {
                clsAreaCode = clsGlobalVarClass.gAreaCodeForTrans;
            }
            else
            {
                clsAreaCode = clsGlobalVarClass.gDineInAreaForDirectBiller;
            }
            Map<String, clsItemPriceDtl> x = clsPLUItemDtl.hmPLUItemDtl.get(clsAreaCode);

            for (String iName : x.keySet())
            {

                Object[] rows =
                {
                    iName
                };
                dm_PLU_Item_Table.addRow(rows);
            }
            tbl_PLU_Items.setModel(dm_PLU_Item_Table);

            final TableRowSorter<TableModel> sorter;
            sorter = new TableRowSorter<TableModel>(dm_PLU_Item_Table);
            tbl_PLU_Items.setRowSorter(sorter);
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    private void fun_PLU_Closed_Button_Pressed()
    {
        fun_Close_PLU_Pannel();
    }

    private void fun_Close_PLU_Pannel()
    {
        fun_isVisiblePanels(true);
    }

    private void fun_isVisiblePanels(boolean isVisible)
    {
        IItemPanel.setVisible(isVisible);
        panelNavigate.setVisible(isVisible);
        panelSubGroup.setVisible(isVisible);

        panel_PLU.setVisible(!isVisible);
        txtPLU_ItemSearch.setText("");
        //  btnPLU.requestFocus();
    }

    private void funSelectWaiter()
    {
        try
        {
            objUtility.funCallForSearchForm("WaiterMaster");
            new frmSearchFormDialog(this, true).setVisible(true);
            if (clsGlobalVarClass.gSearchItemClicked)
            {
                Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
                funSetWaiterData(data);
                clsGlobalVarClass.gSearchItemClicked = false;
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    private void funSetWaiterData(Object[] arrWaiter)
    {
        waiterNo = arrWaiter[0].toString();
        btnWaiterName.setText(arrWaiter[1].toString());
    }

    private boolean funSelectCounterCode()
    {
        boolean flgCounter = false;
        objUtility.funCallForSearchForm("CounterForOperation");
        new frmSearchFormDialog(this, true).setVisible(true);
        if (clsGlobalVarClass.gSearchItemClicked)
        {
            Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
            clsGlobalVarClass.gCounterCode = data[0].toString();
            clsGlobalVarClass.gCounterName = data[1].toString();
            clsGlobalVarClass.gSearchItemClicked = false;
            flgCounter = true;
        }
        return flgCounter;
    }

    private void funOpenDirectBiller()
    {
        if (clsGlobalVarClass.gCounterWise.equals("Yes"))
        {
            if (funSelectCounterCode())
            {
                clsGlobalVarClass.gTransactionType = "Direct Biller";
                //clsGlobalVarClass.setBillingType("Direct Biller");
                frmDirectBiller billfrm = new frmDirectBiller();
                billfrm.setVisible(true);
            }
        }
        else
        {
            clsGlobalVarClass.gTransactionType = "Direct Biller";
            //clsGlobalVarClass.setBillingType("Direct Biller");
            frmDirectBiller billfrm = new frmDirectBiller();
            billfrm.setVisible(true);
        }
    }

//Reset the Button array
    private void funResetItemNames()
    {
        flgPopular = false;
        JButton[] btnItemArray =
        {
            btnIItem1, btnIItem2, btnIItem3, btnIItem4, btnIItem5, btnIItem6, btnIItem7, btnIItem8, btnIItem9, btnIItem10, btnIItem11, btnIItem12, btnIItem13, btnIItem14, btnIItem15, btnIItem16
        };//create the JButton array group
        for (int i = 0; i < btnItemArray.length; i++)
        {
            btnItemArray[i].setText("");//set the item button blank
        }
    }

    private String funGetDate(Date dt, int reqdDays)
    {
        String date = "";
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(dt);
        cal.add(Calendar.DATE, reqdDays);
        date = (cal.getTime().getYear() + 1900) + "-" + (cal.getTime().getMonth() + 1) + "-" + (cal.getTime().getDate());
        return date;
    }

    private boolean funCheckDays(String day)
    {
        boolean flgDays = false;

        switch (day)
        {
            case "Sunday":
                flgDays = true;
                break;

            case "Monday":
                flgDays = true;
                break;

            case "Tuesday":
                flgDays = true;
                break;

            case "Wednesday":
                flgDays = true;
                break;

            case "Thursday":
                flgDays = true;
                break;

            case "Friday":
                flgDays = true;
                break;

            case "Saturday":
                flgDays = true;
                break;
        }

        return flgDays;
    }

    //To get The Price of Item
    private void funGetPrice(String itemName)
    {
        try
        {
            String itemCode = "";
            double price = 0;
            frmNumberKeyPad num;

            int index = list_ItemNames_Buttoms.indexOf(itemName);
            clsItemPriceDtl priceObject = obj_List_ItemPrice.get(index);
            itemCode = priceObject.getStrItemCode();
            price = funGetFinalPrice(priceObject);

            if (clsGlobalVarClass.gItemQtyNumpad)
            {
                num = new frmNumberKeyPad(this, true, "qty");
                num.setVisible(true);
                if (null != clsGlobalVarClass.gNumerickeyboardValue)
                {
                    selectedQty = Double.parseDouble(clsGlobalVarClass.gNumerickeyboardValue);
                    clsGlobalVarClass.gNumerickeyboardValue = null;
                }
            }
            if (clsGlobalVarClass.gProductionLinkup)
            {
                dtCalOrderDate = null;
                if (hmExtraParam.size() > 0)
                {
                    List<String> listOfParam = hmExtraParam.get(itemCode);
                    for (int i = 0; i < listOfParam.size(); i++)
                    {
                        String[] param = listOfParam.get(i).split("#");
                        requiredDays = Integer.parseInt(param[0]);
                        itemIncrWgt = Double.parseDouble(param[1]);
                        itemMinWgt = Double.parseDouble(param[2]);
                        if (param.length > 3)
                        {
                            noDeliverDays = param[3];
                        }
                        else
                        {
                            noDeliverDays = "NA";
                        }
                    }
                }

                if (price == 0)
                {
                    frmNumberKeyPad obj = new frmNumberKeyPad(this, true, "Rate" + price);
                    obj.setVisible(true);
                    if (clsGlobalVarClass.gRateEntered)
                    {
                        if (null != clsGlobalVarClass.gNumerickeyboardValue)
                        {
                            price = Double.parseDouble(clsGlobalVarClass.gNumerickeyboardValue);
                            clsGlobalVarClass.gNumerickeyboardValue = null;
                        }
                        if (selectedQty != 0)
                        {
                            funInsertData(selectedQty, price, itemCode, itemName, itemMinWgt, price);
                            funFillAdvanceOrderItemGrid();
                            selectedQty = 1;
                        }
                        else
                        {
                            new frmOkPopUp(null, "Please select quantity first", "Error", 1).setVisible(true);
                        }
                        clsGlobalVarClass.gRateEntered = false;
                    }
                }
                else
                {
                    if (selectedQty > 0)
                    {
                        funInsertData(selectedQty, price, itemCode, itemName, itemMinWgt, price);
                        funFillAdvanceOrderItemGrid();
                        selectedQty = 1;
                    }
                    else
                    {
                        new frmOkPopUp(null, "Please select quantity first", "Error", 1).setVisible(true);
                    }
                }
                funFillCharactersticsMaster(itemCode, itemName);

                String[] posDate = clsGlobalVarClass.gPOSDateForTransaction.split(" ");
                String[] arrSpDate = posDate[0].split("-");
                Date dtNextDate = new Date(Integer.parseInt(arrSpDate[0]) - 1900, Integer.parseInt(arrSpDate[1]) - 1, Integer.parseInt(arrSpDate[2]));
                String calDate = funGetDate(dtNextDate, requiredDays);
                String[] sp = calDate.split("-");
                dtNextDate = new Date(Integer.parseInt(sp[0]) - 1900, Integer.parseInt(sp[1]) - 1, Integer.parseInt(sp[2]));

                int dayCount = 0;
                String dayOfWeek = funGetDayOfWeek(dtNextDate.getDay());
                String[] arrSpNoDelDays = noDeliverDays.split(",");
                List<String> arrListDays = new ArrayList<String>();

                for (int cnt = 0; cnt < arrSpNoDelDays.length; cnt++)
                {
                    arrListDays.add(arrSpNoDelDays[cnt]);
                }

                for (int cnt = 0; cnt < arrListDays.size(); cnt++)
                {
                    if (arrListDays.contains(dayOfWeek))
                    {
                        String tempDate = funGetDate(dtNextDate, 1);
                        String[] sp1 = tempDate.split("-");
                        dtNextDate = new Date(Integer.parseInt(sp1[0]) - 1900, Integer.parseInt(sp1[1]) - 1, Integer.parseInt(sp1[2]));
                        dayOfWeek = funGetDayOfWeek(dtNextDate.getDay());
                        dayCount++;
                    }
                }

                requiredDays += dayCount;
                Date dt = new Date(Integer.parseInt(sp[0]) - 1900, Integer.parseInt(sp[1]) - 1, Integer.parseInt(sp[2]));
                GregorianCalendar cal = new GregorianCalendar();
                cal.setTime(dt);
                cal.add(Calendar.DATE, dayCount);
                dteOrderDate.setDate(new Date(cal.getTime().getYear(), (cal.getTime().getMonth()), (cal.getTime().getDate())));
                dtCalOrderDate = new Date(cal.getTime().getYear(), (cal.getTime().getMonth()), (cal.getTime().getDate()));
            }
            else
            {
                if (price == 0)
                {
                    frmNumberKeyPad obj = new frmNumberKeyPad(this, true, "Rate" + price);
                    obj.setVisible(true);
                    if (clsGlobalVarClass.gRateEntered)
                    {
                        if (null != clsGlobalVarClass.gNumerickeyboardValue)
                        {
                            price = Double.parseDouble(clsGlobalVarClass.gNumerickeyboardValue);
                            clsGlobalVarClass.gNumerickeyboardValue = null;
                        }
                        if (selectedQty != 0)
                        {
                            funInsertData(selectedQty, price, itemCode, itemName, itemMinWgt, price);
                            funFillAdvanceOrderItemGrid();
                            selectedQty = 1;
                        }
                        else
                        {
                            new frmOkPopUp(null, "Please select quantity first", "Error", 1).setVisible(true);
                        }
                        clsGlobalVarClass.gRateEntered = false;
                    }
                }
                else
                {
                    if (selectedQty > 0)
                    {
                        funInsertData(selectedQty, price, itemCode, itemName, itemMinWgt, price);
                        funFillAdvanceOrderItemGrid();
                        selectedQty = 1;
                    }
                    else
                    {
                        new frmOkPopUp(null, "Please select quantity first", "Error", 1).setVisible(true);
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

    private String funGetDayOfWeek(int day)
    {
        String dayOfWeek = "";
        switch (day)
        {
            case 0:
                dayOfWeek = "Sunday";
                break;

            case 1:
                dayOfWeek = "Monday";
                break;

            case 2:
                dayOfWeek = "Tuesday";
                break;

            case 3:
                dayOfWeek = "Wednesday";
                break;

            case 4:
                dayOfWeek = "Thursday";
                break;

            case 5:
                dayOfWeek = "Friday";
                break;

            case 6:
                dayOfWeek = "Saturday";
                break;
        }
        return dayOfWeek;
    }

// to inset the record into tbladvbookitemtemp
    private void funInsertData(double qty, double price, String itemCode, String itemName, double minWeight, double singleItemPrice)
    {
        try
        {
            sql = "select dblItemQuantity,dblAmount from tbladvbookitemtemp where strItemCode='" + itemCode + "'";
            ResultSet rsAdvOrder = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            if (rsAdvOrder.next())
            {
                double finalAmt = rsAdvOrder.getDouble(2) + (qty * price);
                qty = qty + rsAdvOrder.getDouble(1);
                sql = "update tbladvbookitemtemp set dblItemQuantity='" + qty + "',dblAmount='" + finalAmt + "'"
                        + ",strUserEdited='" + clsGlobalVarClass.gUserCode + "'"
                        + ",dteDateEdited='" + clsGlobalVarClass.getCurrentDateTime() + "' "
                        + "where strItemCode='" + itemCode + "'";
                clsGlobalVarClass.dbMysql.execute(sql);
                selectedQty = 1;
            }
            else
            {
                double finalAmt = qty * price;
                sql = "insert into tbladvbookitemtemp(strSerialno,strPosCode,strItemCode,strItemName"
                        + ",dblItemQuantity,dblWeight,dblAmount,strUserCreated,strUserEdited,dteDateCreated,dteDateEdited"
                        + ",dblPrice) "
                        + "values('" + serailNo + "','" + clsGlobalVarClass.gPOSCode + "','" + itemCode + "','" + itemName + "'"
                        + ",'" + qty + "','" + minWeight + "','" + finalAmt + "','" + clsGlobalVarClass.gUserCode + "'"
                        + ",'" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.getCurrentDateTime() + "'"
                        + ",'" + clsGlobalVarClass.getCurrentDateTime() + "','" + singleItemPrice + "')";
                clsGlobalVarClass.dbMysql.execute(sql);
                serailNo++;
                selectedQty = 1;
            }
            rsAdvOrder.close();
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    //delete the all the item from tbladvbookitemtemp
    private void funTruncateTempTable() throws Exception
    {
        clsGlobalVarClass.dbMysql.execute("truncate table tbladvbookitemtemp");
        funResetFields();
    }

    private void funTruncateInitiallyTempTable() throws Exception
    {
        clsGlobalVarClass.dbMysql.execute("truncate table tbladvbookitemtemp");
    }


    /*
     * In this Methode Generate the Advance Order no.
     */
    private String funGenerateAdvanceOrderNo()
    {
        //the below code, generate the Advance Order no.

        String advOrderNo = "";
        try
        {
            long code = 0;
            sql = "select count(strAdvBookingNo) from  tbllaststoreadvbookingbill where strPosCode='" + clsGlobalVarClass.gPOSCode + "'";//generate Bill No.
            ResultSet rsAdvOrder = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            rsAdvOrder.next();
            count = rsAdvOrder.getInt(1);
            rsAdvOrder.close();
            if (count > 0)
            {
                sql = "select strAdvBookingNo from tbllaststoreadvbookingbill where strPosCode='" + clsGlobalVarClass.gPOSCode + "'";
                rsAdvOrder = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                rsAdvOrder.next();
                code = rsAdvOrder.getLong(1);
                code = code + 1;
                advOrderNo = "AB" + String.format("%05d", code);
                sql = "update tbllaststoreadvbookingbill set strAdvBookingNo='" + code + "' where strPosCode='" + clsGlobalVarClass.gPOSCode + "'";
                clsGlobalVarClass.dbMysql.execute(sql);
            }
            else
            {
                advOrderNo = "AB" + "00001";
                code = 1;
                sql = "insert into tbllaststoreadvbookingbill values('" + clsGlobalVarClass.gPOSCode + "','" + code + "')";
                clsGlobalVarClass.dbMysql.execute(sql);
            }
            rsAdvOrder.close();
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
        finally
        {
            return advOrderNo;
        }
    }

    /**
     *
     * In this methode Reset The All Fields
     *
     */
    private void funResetFields()
    {
        try
        {
            serailNo = 1;
            selectedQty = 1;
            String bdte = clsGlobalVarClass.gPOSStartDate;
            SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date bDate = dFormat.parse(bdte);
            String dte = bDate.getDate() + "-" + (bDate.getMonth() + 1) + "-" + (bDate.getYear() + 1900);
            Date date2 = new SimpleDateFormat("dd-MM-yyyy").parse(dte);
            dteOrderDate.setDate(date2);
            funPopularItem();
            txtMessage.setText("");
            txtShape.setText("");
            txtNote.setText("");
            homeDelivery = "N";
            cmbAMPM.setSelectedIndex(0);
            cmbHour.setSelectedIndex(0);
            cmbMinute.setSelectedIndex(0);
            btnHomeDel.setForeground(Color.white);
            lblAdvOrderno.setText("");
            lblCustInfo.setText("Customer Info");
            txtManualAdvOrderNo.setText("");
            clsGlobalVarClass.gDeliveryCharges = 0;
            funSetDefaultOrderTime();
            funFillAdvanceOrderItemGrid();
            hmChar.clear();
            hmValueChar.clear();
            DefaultTableModel dm = (DefaultTableModel) tblCharactersticsMaster.getModel();
            dm.setRowCount(0);
            tblCharactersticsMaster.setModel(dm);
            lblItemName.setText("");
            hmAttachedImage.clear();
            hmAttachedImage = new HashMap<String, File>();
            chkUrgentOrder.setSelected(false);
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    /*
     * This methode to get the FreeFlow Modifier on the Item
     */
    public void funInsertFreeFlowModifier(String modifierName, double rate)
    {
        try
        {
            int serialno = 0;
            String tempitemcode = null;
            String tempitemName = (String) tblItemTable.getValueAt(tblItemTable.getSelectedRow(), 0);
            sql = "select strSerialno,strItemCode,dblItemQuantity from tbladvbookitemtemp "
                    + " where strItemName='" + tempitemName + "' and  strPosCode='" + clsGlobalVarClass.gPOSCode + "' "
                    + " and strUserCreated='" + clsGlobalVarClass.gUserCode + "'";
            ResultSet rsModifier = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            if (rsModifier.next())
            {
                serialno = rsModifier.getInt(1);
                tempitemcode = rsModifier.getString(2);
            }
            rsModifier.close();

            String s = Integer.toString(serialno);
            s = s.concat(".1");
            if (selectedQty != 0)//check the Qty select or not
            {
                double amount = selectedQty * rate;
                sql = "insert into tbladvbookitemtemp(strSerialno,strPosCode,strItemCode,strItemName,"
                        + "dblItemQuantity,dblAmount,strUserCreated,strUserEdited,dteDateCreated,dteDateEdited)"
                        + " values('" + s + "','" + clsGlobalVarClass.gPOSCode + "','" + tempitemcode.concat("M99") + "','" + "-->"
                        + modifierName + "','" + selectedQty + "','" + amount + "','" + clsGlobalVarClass.gUserCode
                        + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.getCurrentDateTime() + "'"
                        + ",'" + clsGlobalVarClass.getCurrentDateTime() + "')";
                clsGlobalVarClass.dbMysql.execute(sql);
                selectedQty = 1;
                funFillAdvanceOrderItemGrid();
            }
            else
            {
                new frmOkPopUp(null, "Please select quantity first", "Error", 1).setVisible(true);
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    /*
     * In this Method Show the Panel Like Reset
     */
    public void funShowItemPanel()
    {
        selectedQty = 1;
        IItemPanel.setVisible(true);
        panelNavigate.setVisible(true);
        NewOrderPanel.setVisible(true);
        this.revalidate();
    }

    /*
     * To search the Mobile no. In Customer Master If exists
     * then Display the Information In Form, and If not Exits Then Open The Customer Master Form
     */
    public void funSetMobileno(String text)
    {
        try
        {
            if (text.trim().length() == 0)
            {
                objUtility.funCallForSearchForm("CustomerMaster");
                new frmSearchFormDialog(this, true).setVisible(true);
                if (clsGlobalVarClass.gSearchItemClicked)
                {
                    Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
                    clsGlobalVarClass.gCustCodeForAdvOrder = data[0].toString();
                    clsGlobalVarClass.gSearchItemClicked = false;
                    lblCustInfo.setText("<html>" + data[1].toString() + "</html>");
                }
            }
            else
            {
                sql = "select count(*) from tblcustomermaster "
                        + "where longMobileNo like '%" + text + "%' ";
                ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                rs.next();
                int found = rs.getInt(1);
                if (found > 0)
                {
                    sql = "select strCustomerCode,strCustomerName,strBuildingName,strStreetName,strLandmark "
                            + "from tblcustomermaster where longMobileNo like '%" + text + "%' ";
                    ResultSet rsCustDtl = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                    if (rsCustDtl.next())
                    {
                        clsGlobalVarClass.gCustCodeForAdvOrder = rsCustDtl.getString(1);
                        lblCustInfo.setText(rsCustDtl.getString(2));
                    }
                    rsCustDtl.close();
                }
                else
                {
                    clsGlobalVarClass.gNewCustomerMobileNo = Long.parseLong(text);
                    clsGlobalVarClass.gNewCustForAdvOrder = true;
                    new frmCustomerMaster().setVisible(true);
                }
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    public void funSendItemName(String itemName)
    {
        funGetPrice(itemName);
        funFillAdvanceOrderItemGrid();
    }

    /*
     * To Modify the Advance Order
     */
    private void funModifyAdvanceOrder()
    {
        try
        {
            tabPaneAdvOrder.setSelectedIndex(0);
            clsGlobalVarClass.dbMysql.execute("truncate table tbladvbookitemtemp");

            int i = tblModifyAdvanceOrderList.getSelectedRow();
            customerCode = tblModifyAdvanceOrderList.getModel().getValueAt(i, 0).toString();
            String advanceOrderNo = tblModifyAdvanceOrderList.getModel().getValueAt(i, 2).toString();
            clsGlobalVarClass.gAdvOrderNoForBilling = advanceOrderNo;
            String OrderDatetemp = tblModifyAdvanceOrderList.getModel().getValueAt(i, 4).toString();
            clsGlobalVarClass.setAdvanceOrderForDate(OrderDatetemp);
            lblAdvOrderno.setText(advanceOrderNo);
            String custCode = "";
            sql = "select a.strCustomerCode,a.dteOrderFor,b.strCustomerName,b.longMobileNo "
                    + "from tbladvbookbilldtl a,tblcustomermaster b "
                    + "where a.strCustomerCode=b.strCustomerCode "
                    + "and a.strAdvBookingNo='" + advanceOrderNo + "'";
            ResultSet rsCustomer = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            if (rsCustomer.next())
            {
                custCode = rsCustomer.getString(1);
                Date OrderDate = rsCustomer.getDate(2);
                dteOrderDate.setDate(null);
                dteOrderDate.setDate(OrderDate);
                lblCustInfo.setText(rsCustomer.getString(3) + "," + rsCustomer.getString(4));
                clsGlobalVarClass.gCustCodeForAdvOrder = custCode;
            }
            rsCustomer.close();
            double sum = 0.00;
            DefaultTableModel dm = new DefaultTableModel()
            {
                @Override
                public boolean isCellEditable(int row, int column)
                {
                    //all cells false
                    return false;
                }
            };

            if (clsGlobalVarClass.gProductionLinkup)
            {
                hmExtraParam.clear();
            }

            dm.addColumn("Description");
            dm.addColumn("Qty");
            dm.addColumn("Amount");
            dm.addColumn("Wgt");
            dm.addColumn("ItemCode");
            dm.addColumn("Price");
            sql = "select a.strItemCode,a.strItemName,a.dblQuantity,a.dblWeight,a.dblAmount,a.dteAdvBookingDate"
                    + " ,a.strCustomerCode,b.intDeliveryDays,b.dblIncrementalWeight,b.dblMinWeight,b.strNoDeliveryDays "
                    + " from tbladvbookbilldtl a,tblitemmaster b "
                    + " where a.strItemCode=b.strItemCode and a.strAdvBookingNo='" + advanceOrderNo + "'";
            ResultSet rsItemCode = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            while (rsItemCode.next())
            {
                String itemCode = rsItemCode.getString(1);
                String itemname = rsItemCode.getString(2);
                String qty = rsItemCode.getString(3);
                String weight = rsItemCode.getString(4);
                String amount = rsItemCode.getString(5);
                String receiptDate = rsItemCode.getString(6);
                double price = Double.parseDouble(amount) / Double.parseDouble(qty);
                custCode = rsItemCode.getString(7);

                List arrExtraParameter = new ArrayList<String>();
                if (hmExtraParam.containsKey(rsItemCode.getString(1)))
                {
                    arrExtraParameter.add(rsItemCode.getString(8) + "#" + rsItemCode.getString(9) + "#" + rsItemCode.getString(10) + "#" + rsItemCode.getString(11));
                }
                else
                {
                    arrExtraParameter.add(rsItemCode.getString(8) + "#" + rsItemCode.getString(9) + "#" + rsItemCode.getString(10) + "#" + rsItemCode.getString(11));
                }
                hmExtraParam.put(rsItemCode.getString(1), arrExtraParameter);

                sql = "select dteAdvBookingDate,dblTaxAmt,strImageName,strSpecialsymbolImage "
                        + " from tbladvbookbillhd where strAdvBookingNo='" + advanceOrderNo + "'";
                ResultSet rsImage = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                if (rsImage.next())
                {
                    String imgName = rsImage.getString(3);
                    funSetImage(imgName);
                    String imgSplSymbol = rsImage.getString(4);
                    funSetSpecialSymbolImage(imgSplSymbol);
                }
                rsImage.close();

                sql = "insert into tbladvbookitemtemp(strSerialno,strPosCode,strItemCode,strItemName,dblItemQuantity"
                        + ",dblWeight,dblAmount,strUserCreated,strUserEdited,dteDateCreated,dteDateEdited,dblPrice) "
                        + "values('" + serailNo + "','" + clsGlobalVarClass.gPOSCode + "','" + itemCode + "','" + itemname + "'"
                        + ",'" + qty + "','" + weight + "','" + amount + "','" + clsGlobalVarClass.gUserCode + "'"
                        + ",'" + clsGlobalVarClass.gUserCode + "','" + receiptDate + "','" + clsGlobalVarClass.getCurrentDateTime() + "'"
                        + ",'" + price + "')";
                clsGlobalVarClass.dbMysql.execute(sql);

                sql = "select strModifierCode,strModifierName,dblQuantity,dblAmount,strUserCreated "
                        + "from tbladvordermodifierdtl "
                        + "where strItemCode='" + itemCode + "' and strAdvOrderNo='" + advanceOrderNo + "'";
                ResultSet rs1 = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                while (rs1.next())
                {
                    String s = Integer.toString(serailNo);
                    s = s.concat(".1");
                    String modifierCode = rs1.getString(1);
                    String modifierName = rs1.getString(2);
                    String modifierQty = rs1.getString(3);
                    String modifierAmount = rs1.getString(4);
                    sql = "insert into tbladvbookitemtemp(strSerialno,strPosCode,strItemCode,strItemName,"
                            + "dblItemQuantity,dblAmount,strUserCreated,strUserEdited,dteDateCreated,dteDateEdited) "
                            + "values('" + s + "','" + clsGlobalVarClass.gPOSCode + "','"
                            + itemCode.concat(modifierCode) + "','" + modifierName + "','" + modifierQty + "','" + modifierAmount
                            + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.getCurrentDateTime() + "'"
                            + ",'" + clsGlobalVarClass.getCurrentDateTime() + "') ";
                    clsGlobalVarClass.dbMysql.execute(sql);
                }
                rs1.close();
                serailNo++;
            }
            rsItemCode.close();

            sql = "select strMessage,strShape,strNote,strManualAdvOrderNo,strUrgentOrder,dteOrderFor "
                    + " from tbladvbookbillhd "
                    + " where strAdvBookingNo='" + advanceOrderNo + "'";
            ResultSet rsAdvHd = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            if (rsAdvHd.next())
            {
                if (rsAdvHd.getString(5).equalsIgnoreCase("Y"))
                {
                    chkUrgentOrder.setSelected(true);
                }

                String[] arrMessage = rsAdvHd.getString(1).split("}");
                if (arrMessage.length > 0)
                {
                    if (arrMessage.length == 1)
                    {
                        txtMessage.setText(arrMessage[0]);
                    }
                }
                String[] arrShape = rsAdvHd.getString(2).split("}");
                if (arrShape.length > 0)
                {
                    if (arrShape.length == 1)
                    {
                        txtShape.setText(arrShape[0]);
                    }
                }
                txtNote.setText(rsAdvHd.getString(3));
                txtManualAdvOrderNo.setText(rsAdvHd.getString(4));
            }
            rsAdvHd.close();

            /*
             List<String> listCharValue=null;
             Map<String,clsCharacteristics> hmTextCharDtl=null;
             sql="select a.strItemCode,a.strCharCode,a.strCharValues,b.strCharType,b.strCharName "
             + " from tbladvbookbillchardtl a,tblcharactersticsmaster b "
             + " where a.strCharCode=b.strCharCode and a.strAdvBookingNo='"+advanceOrderNo+"' "
             + " order by a.strItemCode";
             ResultSet rsAdvOrderCharDtl=clsGlobalVarClass.dbMysql.executeResultSet(sql);
             while(rsAdvOrderCharDtl.next())
             {
             if(rsAdvOrderCharDtl.getString(4).equalsIgnoreCase("Value"))
             {
             if(hmChar.containsKey(rsAdvOrderCharDtl.getString(1)))
             {
             listCharValue=hmChar.get(rsAdvOrderCharDtl.getString(1));
             }
             else
             {
             listCharValue=new ArrayList<String>();
             }
             String val=rsAdvOrderCharDtl.getString(2)+"#"+rsAdvOrderCharDtl.getString(3)+"#"+rsAdvOrderCharDtl.getString(4)+"#"+rsAdvOrderCharDtl.getString(5);
             listCharValue.add(val);
             hmChar.put(rsAdvOrderCharDtl.getString(1),listCharValue);
             }
             else
             {
             if(hmTextChar.containsKey(rsAdvOrderCharDtl.getString(1)))
             {
             hmTextCharDtl=hmTextChar.get(rsAdvOrderCharDtl.getString(1));
             }
             else
             {
             hmTextCharDtl=new HashMap<String,clsCharacteristics>();
             }
                    
             clsCharacteristics objCharDtl=new clsCharacteristics();
             objCharDtl.setItemCode(rsAdvOrderCharDtl.getString(1));
             objCharDtl.setCharCode(rsAdvOrderCharDtl.getString(2));
             objCharDtl.setCharName(rsAdvOrderCharDtl.getString(5));
             objCharDtl.setCharValue(rsAdvOrderCharDtl.getString(3));
             objCharDtl.setCharType("Text");
             hmTextCharDtl.put(rsAdvOrderCharDtl.getString(2),objCharDtl);
             hmTextChar.put(rsAdvOrderCharDtl.getString(1),hmTextCharDtl);
             }
             }
             rsAdvOrderCharDtl.close();
             */
            //List<String> listCharValue=null;
            Map<String, clsCharacteristics> hmValueCharDtl = null;

            Map<String, clsCharacteristics> hmTextCharDtl = null;
            sql = "select a.strItemCode,a.strCharCode,a.strCharValues,b.strCharType,b.strCharName "
                    + " from tbladvbookbillchardtl a,tblcharactersticsmaster b "
                    + " where a.strCharCode=b.strCharCode and a.strAdvBookingNo='" + advanceOrderNo + "' "
                    + " order by a.strItemCode";
            ResultSet rsAdvOrderCharDtl = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            while (rsAdvOrderCharDtl.next())
            {
                if (rsAdvOrderCharDtl.getString(4).equalsIgnoreCase("Value"))
                {
                    if (hmValueChar.containsKey(rsAdvOrderCharDtl.getString(1)))
                    {
                        hmValueCharDtl = hmValueChar.get(rsAdvOrderCharDtl.getString(1));
                    }
                    else
                    {
                        //listCharValue=new ArrayList<String>();
                        hmValueCharDtl = new HashMap<String, clsCharacteristics>();
                    }
                    /*String val=rsAdvOrderCharDtl.getString(2)+"#"+rsAdvOrderCharDtl.getString(3)+"#"+rsAdvOrderCharDtl.getString(4)+"#"+rsAdvOrderCharDtl.getString(5);
                     listCharValue.add(val);
                     hmValueChar.put(rsAdvOrderCharDtl.getString(1),listCharValue);*/

                    clsCharacteristics objCharValues = new clsCharacteristics();
                    objCharValues.setCharCode(rsAdvOrderCharDtl.getString(2));
                    objCharValues.setCharName(rsAdvOrderCharDtl.getString(5));
                    objCharValues.setCharType(rsAdvOrderCharDtl.getString(4));
                    objCharValues.setCharValue(rsAdvOrderCharDtl.getString(3));
                    objCharValues.setItemCode(rsAdvOrderCharDtl.getString(1));
                    hmValueCharDtl.put(rsAdvOrderCharDtl.getString(2), objCharValues);
                    hmValueChar.put(rsAdvOrderCharDtl.getString(1), hmValueCharDtl);
                }
                else
                {
                    if (hmTextChar.containsKey(rsAdvOrderCharDtl.getString(1)))
                    {
                        hmTextCharDtl = hmTextChar.get(rsAdvOrderCharDtl.getString(1));
                    }
                    else
                    {
                        hmTextCharDtl = new HashMap<String, clsCharacteristics>();
                    }

                    clsCharacteristics objCharDtl = new clsCharacteristics();
                    objCharDtl.setItemCode(rsAdvOrderCharDtl.getString(1));
                    objCharDtl.setCharCode(rsAdvOrderCharDtl.getString(2));
                    objCharDtl.setCharName(rsAdvOrderCharDtl.getString(5));
                    objCharDtl.setCharValue(rsAdvOrderCharDtl.getString(3));
                    objCharDtl.setCharType("Text");
                    hmTextCharDtl.put(rsAdvOrderCharDtl.getString(2), objCharDtl);
                    hmTextChar.put(rsAdvOrderCharDtl.getString(1), hmTextCharDtl);
                }
            }
            rsAdvOrderCharDtl.close();

            sql = "select a.strItemCode,a.blobCakeImage from tbladvbookbillimgdtl a "
                    + " where a.strAdvBookingNo='" + advanceOrderNo + "' order by a.strItemCode ";
            ResultSet rsAdvOrderImageDtl = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            while (rsAdvOrderImageDtl.next())
            {
                if (clsGlobalVarClass.gProductionLinkup)
                {
                    Blob blob = rsAdvOrderImageDtl.getBlob(2);
                    int blobLength = (int) blob.length();
                    byte[] blobAsBytes = blob.getBytes(1, blobLength);
                    byte[] decoded = Base64.getDecoder().decode(blobAsBytes);
                    String filePath = funCreateTempFolderForImage();
                    File file = new File(filePath + "/" + rsAdvOrderImageDtl.getString(1) + ".jpg");
                    if (file.exists())
                    {
                        file.delete();
                    }
                    file.createNewFile();
                    FileOutputStream fos = new FileOutputStream(file);
                    fos.write(decoded);
                    fos.close();
                    String itemCode = rsAdvOrderImageDtl.getString(1);
                    /*if(hmAttachedImage.containsKey(itemCode))
                     {
                     file=hmAttachedImage.get(itemCode);
                     }*/
                    hmAttachedImage.put(itemCode, file);
                }
            }
            rsAdvOrderImageDtl.close();

            sql = "select strItemName,dblItemQuantity,dblAmount,dblWeight,strItemCode,dblPrice "
                    + "from tbladvbookitemtemp "
                    + "where strPosCode='" + clsGlobalVarClass.gPOSCode + "'";
            rsItemCode = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            while (rsItemCode.next())
            {
                double tempAmt = rsItemCode.getDouble(3);
                sum = sum + tempAmt;
                Object[] rows =
                {
                    rsItemCode.getString(1), rsItemCode.getString(2), rsItemCode.getString(3), rsItemCode.getString(4), rsItemCode.getString(5), rsItemCode.getString(6)
                };
                dm.addRow(rows);
            }

            txtTotal.setText(String.valueOf(sum));
            tblItemTable.setModel(dm);
            DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
            rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
            tblItemTable.setShowHorizontalLines(true);
            tblItemTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            tblItemTable.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
            tblItemTable.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
            tblItemTable.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);

            tblItemTable.getColumnModel().getColumn(0).setPreferredWidth(170);
            tblItemTable.getColumnModel().getColumn(1).setPreferredWidth(40);
            tblItemTable.getColumnModel().getColumn(2).setPreferredWidth(83);
            tblItemTable.getColumnModel().getColumn(3).setPreferredWidth(40);
            tblItemTable.getColumnModel().getColumn(4).setPreferredWidth(5);
            flgModifyAdvOrder = true;
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    private String funCreateTempFolderForImage()
    {
        String filePath = System.getProperty("user.dir");
        String fileName = filePath + "/Advance Order Image";
        File theDir = new File(fileName);
        if (!theDir.exists())
        {
            boolean result = false;
            try
            {
                theDir.mkdir();
                result = true;
            }
            catch (SecurityException e)
            {
                objUtility.funWriteErrorLog(e);
                e.printStackTrace();
            }
        }
        return fileName;
    }

    /*
     * Open the Modifier panel
     * and Check which item is selected 
     * and Check Modifier is applicable or not In this selected Item
     * and also check the which panel is opened Like Plu or Modifier Panel
     */
    private void funOpenModifierPanel()
    {
        try
        {
            String itemcode = null;
            String itemName = null;
            int row = tblItemTable.getSelectedRow();
            itemName = tblItemTable.getModel().getValueAt(row, 0).toString();
            sql = "select strItemCode from tbladvbookitemtemp "
                    + " where strItemName='" + itemName + "'";
            ResultSet rsItemCode = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            while (rsItemCode.next())
            {
                itemcode = rsItemCode.getString(1);
            }
            rsItemCode.close();
            sql = "select strApplicable from tblitemmodofier "
                    + " where strItemCode='" + itemcode + "' Or strItemCode='All' "
                    + " group by strModifierCode";
            ResultSet rsModifierApplicable = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            if (rsModifierApplicable.next())
            {
                if ("y".equalsIgnoreCase(rsModifierApplicable.getString(1)))
                {
                    IItemPanel.setVisible(false);
                    panelNavigate.setVisible(false);
                    if (objPanelModifier == null)
                    {
                        objPanelModifier = new panelModifier(this);
                        objPanelModifier.getItemCode(itemcode);
                        NewOrderPanel.add(objPanelModifier);
                        objPanelModifier.setLocation(panelNavigate.getLocation());
                        objPanelModifier.setVisible(true);
                        objPanelModifier.setSize(380, 500);
                        objPanelModifier.revalidate();
                        if (panelNavigate == null)
                        {
                        }
                        else
                        {
                            panelNavigate.setVisible(false);
                        }
                    }
                    else
                    {
                        objPanelModifier.getItemCode(itemcode);
                        objPanelModifier.setVisible(true);
                        if (panelNavigate != null)
                        {
                            panelNavigate.setVisible(false);
                        }
                    }
                }
                else
                {
                    new frmOkPopUp(this, "No Modifier for this item", "Error", 1).setVisible(true);
                }
            }
            else
            {
                IItemPanel.setVisible(false);

                if (objPanelModifier == null)
                {
                    objPanelModifier = new panelModifier(this);
                    NewOrderPanel.add(objPanelModifier);
                    objPanelModifier.setLocation(panelNavigate.getLocation());
                    objPanelModifier.setVisible(true);
                    objPanelModifier.setSize(380, 500);
                    objPanelModifier.revalidate();
                    if (panelNavigate == null)
                    {
                    }
                    else
                    {
                        panelNavigate.setVisible(false);
                    }
                }
                else
                {
                    objPanelModifier.setVisible(true);
                    if (panelNavigate != null)
                    {
                        panelNavigate.setVisible(false);
                    }
                }
            }
            rsModifierApplicable.close();
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    private void funFillTopButtonList(String tempMenuCode)
    {
        try
        {
            if (listTopButtonName == null)
            {
                listTopButtonName = new ArrayList<>();
            }
            if (listTopButtonCode == null)
            {
                listTopButtonCode = new ArrayList<>();
            }
            listTopButtonName.clear();
            listTopButtonCode.clear();
            funReset_TopSortingButtons();
            JButton[] btnSubGroupArray =
            {
                btnItemSorting1, btnItemSorting2, btnItemSorting3, btnItemSorting4
            };
            btnPrevItemSorting.setEnabled(false);
            btnPrevItemSorting.setEnabled(false);
            String sqlCountItem = "";
            if ("subgroupWise".equalsIgnoreCase(clsGlobalVarClass.gMenuItemSortingOn))
            {
                sqlCountItem = "select count(distinct(a.strSubGroupCode)) from tblitemmaster a,tblmenuitempricingdtl b "
                        + "where a.strItemCode=b.strItemCode";
                if (flgPopular)
                {
                    sqlCountItem += " and b.strPopular='Y'";
                }
                else
                {
                    sqlCountItem += " and b.strMenuCode='" + tempMenuCode + "'";
                }
            }
            else if ("subMenuHeadWise".equalsIgnoreCase(clsGlobalVarClass.gMenuItemSortingOn))
            {
                sqlCountItem = "select count(distinct(a.strSubMenuHeadCode)) from tblsubmenuhead a,tblmenuitempricingdtl b ";

                if (flgPopular)
                {
                    sqlCountItem += " where a.strSubMenuHeadCode=b.strSubMenuHeadCode and b.strPopular='Y'";
                }
                else
                {
                    sqlCountItem += " where a.strSubMenuHeadCode=b.strSubMenuHeadCode and b.strMenuCode='" + tempMenuCode + "'";
                }
            }

            ResultSet rsItemCount = clsGlobalVarClass.dbMysql.executeResultSet(sqlCountItem);
            rsItemCount.next();
            int ItemCount = rsItemCount.getInt(1);
            rsItemCount.close();
            if (ItemCount > 4)
            {
                btnNextItemSorting.setEnabled(true);
            }
            else
            {
                btnNextItemSorting.setEnabled(false);
            }
            String sqlItems = "";
            if ("subgroupWise".equalsIgnoreCase(clsGlobalVarClass.gMenuItemSortingOn))
            {
                sqlItems = "select c.strSubGroupName,a.strSubGroupCode,a.strItemCode ,b.strMenuCode \n"
                        + "from tblitemmaster a,tblmenuitempricingdtl b,tblsubgrouphd c  where a.strSubGroupCode !='null' \n"
                        + "and a.strItemCode=b.strItemCode and a.strSubGroupCode=c.strSubGroupCode";

                if (flgPopular)
                {
                    sqlItems += " and b.strPopular='Y' group by a.strSubGroupCode  ORDER by c.strSubGroupName";
                }
                else
                {
                    sqlItems += " and b.strMenuCode='" + tempMenuCode + "' group by a.strSubGroupCode  ORDER by c.strSubGroupName";
                }
            }
            else if ("subMenuHeadWise".equalsIgnoreCase(clsGlobalVarClass.gMenuItemSortingOn))
            {
                sqlItems = "select a.strSubMenuHeadName,a.strSubMenuHeadCode,b.strItemCode "
                        + "from tblsubmenuhead a,tblitemmaster b,tblmenuitempricingdtl c where "
                        + "b.strItemCode=c.strItemCode and a.strSubMenuHeadCode !='null' \n"
                        + "and a.strSubMenuHeadCode=c.strSubMenuHeadCode";
                if (flgPopular)
                {
                    sqlItems += " and c.strPopular='Y' group by a.strSubMenuHeadCode";
                }

                else
                {
                    sqlItems += " and a.strMenuCode='" + tempMenuCode + "' group by a.strSubMenuHeadCode";
                }
            }
            //System.out.println(sqlItems);
            ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sqlItems);
            while (rs.next())
            {
                listTopButtonName.add(rs.getString(1));
                listTopButtonCode.add(rs.getString(2));
            }
            if (!listTopButtonName.isEmpty())
            {
                funFillTopButtons();
            }
            if (!listTopButtonName.isEmpty() && ItemCount > 4)
            {
                btnNextItemSorting.setEnabled(true);
            }
            else
            {
                btnNextItemSorting.setEnabled(false);
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    private void funRefreshButtonItemSelectionWise(String MenuCode, String selectedButtonCode)
    {
        int i = 0;
        //create the button array of Item
        JButton[] btnSubMenuArray =
        {
            btnIItem1, btnIItem2, btnIItem3, btnIItem4, btnIItem5, btnIItem6, btnIItem7, btnIItem8, btnIItem9, btnIItem10, btnIItem11, btnIItem12, btnIItem13, btnIItem14, btnIItem15, btnIItem16
        };
        try
        {
            btnNextItem.setEnabled(false);
            btnPrevItem.setEnabled(false);
            nextItemClick = 0;
            String sqlCount = "";
            if ("subgroupWise".equalsIgnoreCase(clsGlobalVarClass.gMenuItemSortingOn))
            {
                sqlCount = "SELECT count(c.strItemName) "
                        + "FROM tblmenuhd a LEFT OUTER JOIN tblmenuitempricingdtl b ON a.strMenuCode = b.strMenuCode "
                        + "RIGHT OUTER JOIN tblitemmaster c ON b.strItemCode = c.strItemCode "
                        + "WHERE";

                if (flgPopular)
                {
                    sqlCount += " b.strPopular = 'Y' and  c.strSubGroupCode='" + selectedButtonCode + "' and (strAreaCode='" + clsGlobalVarClass.gDineInAreaForDirectBiller + "' or strAreaCode='')";
                }
                else
                {
                    sqlCount += " a.strMenuCode = '" + MenuCode + "' and  c.strSubGroupCode='" + selectedButtonCode + "' and (strAreaCode='" + clsGlobalVarClass.gDineInAreaForDirectBiller + "' or strAreaCode='')";
                }
            }
            else if ("subMenuHeadWise".equalsIgnoreCase(clsGlobalVarClass.gMenuItemSortingOn))
            {
                sqlCount = "select count(*) from tblmenuitempricingdtl";

                if (flgPopular)
                {
                    sqlCount += " where strPopular=''Y and strSubMenuHeadCode='" + selectedButtonCode + "' and (strAreaCode='" + clsGlobalVarClass.gDineInAreaForDirectBiller + "' or strAreaCode='')";
                }
                else
                {
                    sqlCount += " where strMenuCode='" + menuHeadCode + "' and strSubMenuHeadCode='" + selectedButtonCode + "' and (strAreaCode='" + clsGlobalVarClass.gDineInAreaForDirectBiller + "' or strAreaCode='')";
                }
            }
            //System.out.println(sqlCount);
            ResultSet rsCount = clsGlobalVarClass.dbMysql.executeResultSet(sqlCount);
            rsCount.next();
            int cn = rsCount.getInt(1);
            if (cn > 8)
            {
                btnNextItem.setEnabled(true);
            }
            rsCount.close();
            itemNames = new String[cn];
            String sql1 = "";
            if ("subgroupWise".equalsIgnoreCase(clsGlobalVarClass.gMenuItemSortingOn))
            {
                sql1 = "SELECT c.strItemName,b.strTextColor "
                        + "FROM tblmenuhd a LEFT OUTER JOIN tblmenuitempricingdtl b ON a.strMenuCode = b.strMenuCode "
                        + "RIGHT OUTER JOIN tblitemmaster c ON b.strItemCode = c.strItemCode "
                        + "WHERE";

                if (flgPopular)
                {
                    sql1 += " b.strPopular = 'Y' and c.strSubGroupCode='" + selectedButtonCode + "' and (b.strPosCode='" + clsGlobalVarClass.gPOSCode + "' or b.strPosCode='All')  and strAreaCode='" + clsGlobalVarClass.gDineInAreaForDirectBiller + "' ";
                }
                else
                {
                    sql1 += " a.strMenuCode = '" + MenuCode + "' and c.strSubGroupCode='" + selectedButtonCode + "' and (b.strPosCode='" + clsGlobalVarClass.gPOSCode + "' or b.strPosCode='All')  and strAreaCode='" + clsGlobalVarClass.gDineInAreaForDirectBiller + "' ";
                }

                sql1 = sql1 + " ORDER BY c.strItemName ASC";
            }
            else if ("subMenuHeadWise".equalsIgnoreCase(clsGlobalVarClass.gMenuItemSortingOn))
            {
                sql1 = "SELECT strItemName,strTextColor "
                        + "FROM tblmenuitempricingdtl "
                        + "WHERE";

                if (flgPopular)
                {
                    sql1 += " strMenuCode = '" + menuHeadCode + "' and strSubMenuHeadCode='" + selectedButtonCode + "' and (strPosCode='P01' or strPosCode='All')  and strAreaCode='" + clsGlobalVarClass.gDineInAreaForDirectBiller + "' ";
                }
                else
                {
                    sql1 += " strMenuCode = '" + menuHeadCode + "' and strSubMenuHeadCode='" + selectedButtonCode + "' and (strPosCode='P01' or strPosCode='All')  and strAreaCode='" + clsGlobalVarClass.gDineInAreaForDirectBiller + "' ";
                }
                sql1 = sql1 + " ORDER BY strItemName ASC";
            }

            ResultSet rsItemInfo = clsGlobalVarClass.dbMysql.executeResultSet(sql1);
            while (rsItemInfo.next())
            {
                String temItemName = rsItemInfo.getString(1);
                String txtColor = rsItemInfo.getString(2);
                if (i < 16)
                {
                    if (temItemName.contains(" "))
                    {
                        StringBuilder sb1 = new StringBuilder(temItemName);
                        int len = sb1.length();
                        int seq = sb1.lastIndexOf(" ");
                        String split = sb1.substring(0, seq);
                        String last = sb1.substring(seq + 1, len);

                        btnSubMenuArray[i].setText("<html>" + split + "<br>" + last + "</html>");
                        switch (txtColor)
                        {
                            case "Red":
                                btnSubMenuArray[i].setForeground(Color.BLACK);
                                btnSubMenuArray[i].setBackground(Color.red);
                                break;
                            case "Black":
                                btnSubMenuArray[i].setForeground(Color.BLACK);
                                btnSubMenuArray[i].setBackground(Color.LIGHT_GRAY);
                                break;
                            case "Green":
                                btnSubMenuArray[i].setForeground(Color.BLACK);
                                btnSubMenuArray[i].setBackground(Color.GREEN);
                                break;
                            case "CYAN":
                                btnSubMenuArray[i].setForeground(Color.BLACK);
                                btnSubMenuArray[i].setBackground(Color.CYAN);
                                break;
                            case "MAGENTA":
                                btnSubMenuArray[i].setForeground(Color.BLACK);
                                btnSubMenuArray[i].setBackground(Color.MAGENTA);
                                break;
                            case "ORANGE":
                                btnSubMenuArray[i].setForeground(Color.BLACK);
                                btnSubMenuArray[i].setBackground(Color.ORANGE);
                                break;
                            case "PINK":
                                btnSubMenuArray[i].setForeground(Color.BLACK);
                                btnSubMenuArray[i].setBackground(Color.PINK);
                                break;
                            case "YELLOW":
                                btnSubMenuArray[i].setForeground(Color.BLACK);
                                btnSubMenuArray[i].setBackground(Color.YELLOW);
                                break;
                            default:
                                btnSubMenuArray[i].setForeground(Color.BLACK);
                                break;
                        }
                        itemNames[i] = "<html>" + split + "<br>" + last + "</html>";
                    }
                    else
                    {
                        btnSubMenuArray[i].setText(temItemName);
                        itemNames[i] = temItemName;
                    }
                    btnSubMenuArray[i].setEnabled(true);
                }
                else
                {
                    if (temItemName.contains(" "))
                    {
                        StringBuilder sb1 = new StringBuilder(temItemName);
                        int len = sb1.length();
                        int seq = sb1.lastIndexOf(" ");
                        String split = sb1.substring(0, seq);
                        String last = sb1.substring(seq + 1, len);
                        itemNames[i] = "<html>" + split + "<br>" + last + "</html>";
                    }
                    else
                    {
                        itemNames[i] = temItemName;
                    }
                }
                i++;
            }
            rsItemInfo.close();
            for (int j = i; j < 16; j++)
            {
                btnSubMenuArray[j].setEnabled(false);
                btnSubMenuArray[j].setText("");
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    private void funReset_TopSortingButtons()
    {
        try
        {
            btnItemSorting1.setEnabled(false);
            btnItemSorting2.setEnabled(false);
            btnItemSorting3.setEnabled(false);
            btnItemSorting4.setEnabled(false);
            btnPrevItemSorting.setEnabled(false);
            btnNextItemSorting.setEnabled(false);
            btnItemSorting1.setText("");
            btnItemSorting2.setText("");
            btnItemSorting3.setText("");
            btnItemSorting4.setText("");
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    private void funFillTopButtons()
    {
        try
        {
            JButton btnArray[] =
            {
                btnItemSorting1, btnItemSorting2, btnItemSorting3, btnItemSorting4
            };
            itemNumber = 0;
            totalItems = listTopButtonName.size();
            if (totalItems > 4)
            {
                btnNextItemSorting.setEnabled(true);
            }
            if (totalItems >= 4)
            {
                for (int i = itemNumber; itemNumber < 4; itemNumber++)
                {
                    btnArray[itemNumber].setText(listTopButtonName.get(itemNumber));
                    btnArray[itemNumber].setEnabled(true);
                }
            }
            else
            {
                for (int i = itemNumber; itemNumber < totalItems; itemNumber++)
                {
                    btnArray[itemNumber].setText(listTopButtonName.get(itemNumber));
                    btnArray[itemNumber].setEnabled(true);
                }
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    private String funGetStdTimeFormatFromAMPMFormat(String ampmTime)
    {
        String stdTime = "";
        String ampm = ampmTime.split(" ")[1];
        String time = ampmTime.split(" ")[0];
        int hours = Integer.parseInt(time.split(":")[0]);
        int minutes = Integer.parseInt(time.split(":")[1]);

        if (ampm.equalsIgnoreCase("PM"))
        {
            if (hours != 12)
            {
                hours = hours + 12;
            }
        }
        stdTime = hours + ":" + minutes + ":00";

        return stdTime;
    }

    private void funDoneButtonClicked() throws Exception
    {
        if (chkUrgentOrder.isSelected())
        {
            if (clsGlobalVarClass.gSetUpToTimeForUrgentOrder)
            {
                String time = funGetStdTimeFormatFromAMPMFormat(clsGlobalVarClass.gUpToTimeForUrgentOrder);
                Date dt = new Date();
                String currentDateTime = (dt.getYear() + 1900) + "-" + (dt.getMonth() + 1) + "-" + dt.getDate() + " " + dt.getHours() + ":" + dt.getMinutes() + ":" + dt.getSeconds();
                String uptoTimeForUrgentOrder = (dt.getYear() + 1900) + "-" + (dt.getMonth() + 1) + "-" + dt.getDate() + " " + time;
                long timeDiff = objUtility.funCompareTime(currentDateTime, uptoTimeForUrgentOrder);
                if (timeDiff < 0)
                {
                    JOptionPane.showMessageDialog(null, "You Can not place order after " + clsGlobalVarClass.gUpToTimeForUrgentOrder + " !!!");
                    return;
                }
            }
        }
        else
        {
            if (clsGlobalVarClass.gSetUpToTimeForAdvOrder)
            {
                String time = funGetStdTimeFormatFromAMPMFormat(clsGlobalVarClass.gUpToTimeForAdvOrder);
                Date dt = new Date();
                String currentDateTime = (dt.getYear() + 1900) + "-" + (dt.getMonth() + 1) + "-" + dt.getDate() + " " + dt.getHours() + ":" + dt.getMinutes() + ":" + dt.getSeconds();
                String uptoTimeForAdvOrder = (dt.getYear() + 1900) + "-" + (dt.getMonth() + 1) + "-" + dt.getDate() + " " + time;
                long timeDiff = objUtility.funCompareTime(currentDateTime, uptoTimeForAdvOrder);
                if (timeDiff < 0)
                {
                    JOptionPane.showMessageDialog(null, "You Can not place order after " + clsGlobalVarClass.gUpToTimeForAdvOrder + " !!!");
                    return;
                }
            }
        }

        if (clsGlobalVarClass.gProductionLinkup)
        {
            boolean flgNonDeliveredItems = false;
            String nonDeliveredItems = "";
            int dayOfWeek = dteOrderDate.getDate().getDay();
            String dayOfWeekInString = objUtility.funGetDayOfWeek(dayOfWeek);
            for (int cnt = 0; cnt < tblItemTable.getRowCount(); cnt++)
            {
                sql = "select strNoDeliveryDays,strItemName from tblitemmaster "
                        + " where strItemCode='" + tblItemTable.getValueAt(cnt, 4) + "' ";
                ResultSet rsItemDtl = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                if (rsItemDtl.next())
                {
                    String[] arrNoDelDays = rsItemDtl.getString(1).split(",");
                    for (int cn = 0; cn < arrNoDelDays.length; cn++)
                    {
                        if (arrNoDelDays[cn].equalsIgnoreCase(dayOfWeekInString))
                        {
                            flgNonDeliveredItems = true;
                            nonDeliveredItems += rsItemDtl.getString(2) + ",";
                        }
                    }
                }
                rsItemDtl.close();

                if (flgNonDeliveredItems)
                {
                    JOptionPane.showMessageDialog(null, "Following items " + nonDeliveredItems + " are not delivered on " + dayOfWeekInString);
                    return;
                }
            }

            if (chkUrgentOrder.isSelected())
            {
                boolean flgUrgentOrder = true;
                String nonUrgentOrderItems = "";
                for (int cnt = 0; cnt < tblItemTable.getRowCount(); cnt++)
                {
                    sql = "select strUrgentOrder,strItemName from tblitemmaster where strItemCode='" + tblItemTable.getValueAt(cnt, 4) + "' ";
                    ResultSet rsUrgentOrder = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                    if (rsUrgentOrder.next())
                    {
                        if (rsUrgentOrder.getString(1).equalsIgnoreCase("N"))
                        {
                            nonUrgentOrderItems += rsUrgentOrder.getString(2) + ",";
                            flgUrgentOrder = false;
                        }
                    }
                    rsUrgentOrder.close();
                }

                if (!flgUrgentOrder)
                {
                    JOptionPane.showMessageDialog(null, "Following are non urgent order items " + nonUrgentOrderItems);
                    return;
                }

                String fromDate = (dteOrderDate.getDate().getYear() + 1900) + "-"
                        + (dteOrderDate.getDate().getMonth() + 1) + "-"
                        + (dteOrderDate.getDate().getDate()) + " " + "0:00:00";
                String[] posDate = clsGlobalVarClass.gPOSDateForTransaction.split(" ");
                String[] arrSpDate = posDate[0].split("-");
                Date dtNextDate = new Date(Integer.parseInt(arrSpDate[0]) - 1900, Integer.parseInt(arrSpDate[1]) - 1, Integer.parseInt(arrSpDate[2]));
                String calDate = funGetDate(dtNextDate, clsGlobalVarClass.gNoOfDelDaysForUrgentOrder);
                long timeDiff = objUtility.funCompareDate(calDate, fromDate);
                long diffDays = timeDiff / (24 * 60 * 60 * 1000);

                if (diffDays > 0)
                {
                    JOptionPane.showMessageDialog(null, "Delivery date should not be greater than " + clsGlobalVarClass.gNoOfDelDaysForUrgentOrder + " !!!");
                    return;
                }
            }

            if (!chkUrgentOrder.isSelected())
            {
                String fromDate = (dteOrderDate.getDate().getYear() + 1900) + "-"
                        + (dteOrderDate.getDate().getMonth() + 1) + "-"
                        + (dteOrderDate.getDate().getDate()) + " " + "0:00:00";
                String[] posDate = clsGlobalVarClass.gPOSDateForTransaction.split(" ");
                String[] arrSpDate = posDate[0].split("-");
                Date dtNextDate = new Date(Integer.parseInt(arrSpDate[0]) - 1900, Integer.parseInt(arrSpDate[1]) - 1, Integer.parseInt(arrSpDate[2]));
                String calDate = funGetDate(dtNextDate, clsGlobalVarClass.gNoOfDelDaysForAdvOrder);
                long timeDiff = objUtility.funCompareDate(calDate, fromDate);
                long diffDays = timeDiff / (24 * 60 * 60 * 1000);

                if (diffDays <= 0)
                {
                    JOptionPane.showMessageDialog(null, "Invalid Delivery date, Difference should be " + clsGlobalVarClass.gNoOfDelDaysForAdvOrder + " days!!!");
                    return;
                }
            }
        }

        String orderType = "";
        if (hmAdvOrderType.size() > 0)
        {
            int cnt = 0;
            String[] orderFrom = new String[hmAdvOrderType.size()];
            Iterator iter = hmAdvOrderType.entrySet().iterator();
            while (iter.hasNext())
            {
                Map.Entry mEntry = (Map.Entry) iter.next();
                orderFrom[cnt] = mEntry.getKey().toString();
                cnt++;
            }
            orderType = (String) JOptionPane.showInputDialog(this, "Please Select Order Type", "Order Type", JOptionPane.INFORMATION_MESSAGE, null, orderFrom, orderFrom[0]);
            orderType = hmAdvOrderType.get(orderType).toString();
        }

        if (clsGlobalVarClass.gCompulsoryManualAdvOrderNo)
        {
            if (txtManualAdvOrderNo.getText().trim().length() == 0)
            {
                JOptionPane.showMessageDialog(this, "Please Enter Manual Advance Order No!!!");
                return;
            }
            else
            {
                try
                {
                    if (flgModifyAdvOrder)
                    {
                        sql = "select strManualAdvOrderNo from tbladvbookbillhd "
                                + "where strManualAdvOrderNo = '" + txtManualAdvOrderNo.getText().trim() + "' "
                                + "and strAdvBookingNo='" + clsGlobalVarClass.gAdvOrderNoForBilling + "'";
                        //System.out.println(sql);
                        ResultSet rsManualOrder = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                        if (rsManualOrder.next())
                        {
                        }
                        else
                        {
                            JOptionPane.showMessageDialog(this, "Invalid Manual No !!!");
                            return;
                        }
                        rsManualOrder.close();
                    }
                    else
                    {
                        sql = "select strManualAdvOrderNo from tbladvbookbillhd "
                                + "where strManualAdvOrderNo='" + txtManualAdvOrderNo.getText().trim() + "'";
                        ResultSet rsManualOrder = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                        if (rsManualOrder.next())
                        {
                            JOptionPane.showMessageDialog(this, "Manual No is already used!!!");
                            return;
                        }
                        rsManualOrder.close();
                    }
                }
                catch (Exception e)
                {
                    objUtility.funWriteErrorLog(e);
                    e.printStackTrace();
                }
            }
        }
        funSaveAdvOrderBill(orderType);
    }

    /**
     * @return the objData
     */
    public clsCustomerDataModelForSQY getObjData()
    {
        return objData;
    }

    private void funAddTextCharacteristicsToMap()
    {
        if (txtTextCharValue.getText().trim().isEmpty())
        {
            JOptionPane.showMessageDialog(null, lblTextCharName.getText() + " can not be blank!!!");
        }
        else
        {
            String itemCode = lblHItemCode.getText().trim();
            String charCode = lblHCharCode.getText().trim();

            Map<String, clsCharacteristics> hmTextCharDtl = null;
            if (hmTextChar.containsKey(itemCode))
            {
                hmTextCharDtl = hmTextChar.get(itemCode);
            }
            else
            {
                hmTextCharDtl = new HashMap<String, clsCharacteristics>();
            }
            clsCharacteristics objCharDtl = new clsCharacteristics();
            objCharDtl.setItemCode(itemCode);
            objCharDtl.setCharCode(charCode);
            objCharDtl.setCharName(lblTextCharName.getText());
            objCharDtl.setCharValue(txtTextCharValue.getText().trim());
            objCharDtl.setCharType("Text");

            hmTextCharDtl.put(charCode, objCharDtl);
            hmTextChar.put(itemCode, hmTextCharDtl);

            //int row=tblItemTable.getSelectedRow();
            //String itemName=tblItemTable.getValueAt(row,0).toString();
            JOptionPane.showMessageDialog(null, lblTextCharName.getText() + " Applied to " + lblItemName.getText());
        }
        funFillCharactersticsMaster(lblHItemCode.getText().trim(), lblItemName.getText().toString());
    }

    private void funCharMasterTableClicked() throws Exception
    {
        int row = tblCharactersticsMaster.getSelectedRow();
        String itemCode = tblCharactersticsMaster.getValueAt(row, 3).toString();
        String charName = tblCharactersticsMaster.getValueAt(row, 0).toString();
        String charCode = tblCharactersticsMaster.getValueAt(row, 2).toString();
        String charType = tblCharactersticsMaster.getValueAt(row, 4).toString();

        if (charType.equals("Value"))
        {
            // jPanelRadioButton.removeAll();
            //jPanelRadioButton.revalidate();
            //jPanelRadioButton.repaint();
            txtCharValueSearch.requestFocus();
            txtCharValueSearch.setText("");
            lblHItemCode.setText(itemCode);
            lblHCharCode.setText(charCode);
            funFillCharactersticsValue(itemCode, charCode, charName, charType);
        }
        else if (charType.equals("Text"))
        {
            txtTextCharValue.setText("");
            lblTextCharName.setText(charName);
            lblHItemCode.setText(itemCode);
            lblHCharCode.setText(charCode);

            if (hmTextChar.containsKey(itemCode))
            {
                txtTextCharValue.requestFocus();
                Map<String, clsCharacteristics> hmTextCharDtl = hmTextChar.get(itemCode);
                if (hmTextCharDtl.containsKey(charCode))
                {
                    txtTextCharValue.selectAll();
                    txtTextCharValue.setText(hmTextCharDtl.get(charCode).getCharValue());
                }
                else
                {
                    txtTextCharValue.requestFocus();
                    txtTextCharValue.setText("");
                }
            }
            else
            {
                txtTextCharValue.requestFocus();
                txtTextCharValue.setText("");
            }
        }
    }

    //Function To set image
    private void funSetImageIcon(String itemCode)
    {
        try
        {
            lblSymbolImage.setIcon(null);
            if (hmAttachedImage.containsKey(itemCode))
            {
                File file = hmAttachedImage.get(itemCode);
                String imagePath = file.getAbsolutePath();
                lblSymbolImage.setIcon(new ImageIcon(imagePath));
            }
            else
            {
                lblSymbolImage.setIcon(null);
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    //Save data in tbladvbookbillchardtl table
    private void funSaveCharDtlTable() throws Exception
    {
        int cnt = 0;
        String deleteQuery = " delete from tbladvbookbillchardtl where strAdvBookingNo='" + clsGlobalVarClass.gAdvOrderNoForBilling + "' ";
        clsGlobalVarClass.dbMysql.execute(deleteQuery);
        if (hmValueChar.size() > 0)
        {
            String insertQuery = "insert into tbladvbookbillchardtl (strItemCode,strAdvBookingNo,strCharCode,strCharValues,strClientCode,strDataPostFlag) values ";
            /*for (Map.Entry<String, List<String>> entry : hmValueChar.entrySet())
             {
             List<String> listOfChar = entry.getValue();
             for(int i=0;i<listOfChar.size();i++)
             {
             String []charValue=listOfChar.get(i).split("#");
             if (cnt == 0)
             {
             insertQuery += "('" +entry.getKey()+ "', '"+clsGlobalVarClass.gAdvOrderNoForBilling+"','" +charValue[0]+ "','" +charValue[1]+ "','"+clsGlobalVarClass.gClientCode+"','N') ";
             }
             else
             {
             insertQuery += ",('"+entry.getKey()+ "','" +clsGlobalVarClass.gAdvOrderNoForBilling+ "', '" +charValue[0]+ "', '" +charValue[1]+"','"+clsGlobalVarClass.gClientCode +"','N')";
             }
             cnt++;
             }
             }*/

            for (Map.Entry<String, Map<String, clsCharacteristics>> entry : hmValueChar.entrySet())
            {
                Map<String, clsCharacteristics> hmTextCharDtl = entry.getValue();
                for (Map.Entry<String, clsCharacteristics> entryDtl : hmTextCharDtl.entrySet())
                {
                    if (cnt == 0)
                    {
                        insertQuery += "('" + entryDtl.getValue().getItemCode() + "', '" + clsGlobalVarClass.gAdvOrderNoForBilling + "','" + entryDtl.getValue().getCharCode() + "','" + entryDtl.getValue().getCharValue() + "','" + clsGlobalVarClass.gClientCode + "','N') ";
                    }
                    else
                    {
                        insertQuery += ",('" + entryDtl.getValue().getItemCode() + "', '" + clsGlobalVarClass.gAdvOrderNoForBilling + "','" + entryDtl.getValue().getCharCode() + "','" + entryDtl.getValue().getCharValue() + "','" + clsGlobalVarClass.gClientCode + "','N') ";
                    }
                    cnt++;
                }
            }
            System.out.println("insertQuery=" + insertQuery);
            if (cnt > 0)
            {
                clsGlobalVarClass.dbMysql.execute(insertQuery);
            }
        }

        cnt = 0;
        if (hmTextChar.size() > 0)
        {
            String insertQuery = "insert into tbladvbookbillchardtl (strItemCode,strAdvBookingNo,strCharCode,strCharValues,strClientCode,strDataPostFlag) values ";
            for (Map.Entry<String, Map<String, clsCharacteristics>> entry : hmTextChar.entrySet())
            {
                Map<String, clsCharacteristics> hmTextCharDtl = entry.getValue();
                for (Map.Entry<String, clsCharacteristics> entryDtl : hmTextCharDtl.entrySet())
                {
                    if (cnt == 0)
                    {
                        insertQuery += "('" + entryDtl.getValue().getItemCode() + "', '" + clsGlobalVarClass.gAdvOrderNoForBilling + "','" + entryDtl.getValue().getCharCode() + "','" + entryDtl.getValue().getCharValue() + "','" + clsGlobalVarClass.gClientCode + "','N') ";
                    }
                    else
                    {
                        insertQuery += ",('" + entryDtl.getValue().getItemCode() + "', '" + clsGlobalVarClass.gAdvOrderNoForBilling + "','" + entryDtl.getValue().getCharCode() + "','" + entryDtl.getValue().getCharValue() + "','" + clsGlobalVarClass.gClientCode + "','N') ";
                    }
                    cnt++;
                }
            }
            //System.out.println("insertQuery="+insertQuery);
            if (cnt > 0)
            {
                clsGlobalVarClass.dbMysql.execute(insertQuery);
            }
        }

    }

    //Save data in tbladvbookbillimgdtl table
    private void funSaveImageDtlToTable() throws Exception
    {
        int cnt = 0;
        String deleteQuery = " delete from tbladvbookbillimgdtl where strAdvBookingNo='" + clsGlobalVarClass.gAdvOrderNoForBilling + "' ";
        clsGlobalVarClass.dbMysql.execute(deleteQuery);
        if (hmAttachedImage.size() > 0)
        {
            String insertQuery = "insert into tbladvbookbillimgdtl (strItemCode,strAdvBookingNo,blobCakeImage,strClientCode,strDataPostFlag) values ";
            for (Map.Entry<String, File> entry : hmAttachedImage.entrySet())
            {
                File file = entry.getValue();
                byte[] bytes = Files.readAllBytes(file.toPath());
                String encoded = Base64.getEncoder().encodeToString(bytes);
                if (cnt == 0)
                {
                    insertQuery += "('" + entry.getKey() + "', '" + clsGlobalVarClass.gAdvOrderNoForBilling + "','" + encoded + "','" + clsGlobalVarClass.gClientCode + "','N') ";
                }
                else
                {
                    insertQuery += ",('" + entry.getKey() + "','" + clsGlobalVarClass.gAdvOrderNoForBilling + "', '" + encoded + "','" + clsGlobalVarClass.gClientCode + "','N')";
                }
                cnt++;
            }
            //System.out.println("insertQuery="+insertQuery);
            if (cnt > 0)
            {
                clsGlobalVarClass.dbMysql.execute(insertQuery);
            }
        }
    }

    private String funCheckDay(String selectedDay) throws Exception
    {
        String strFound = "No";
        String[] spDays = noDeliverDays.split(",");
        for (int cnt = 0; cnt < spDays.length; cnt++)
        {
            if ((spDays[cnt].equals(selectedDay)))
            {
                strFound = "Yes";
            }
        }
        return strFound;
    }

    //Function To Fill tblCharacterstics Master using radio button
    private void funFillCharactersticsMaster(String itemCode, String itemName)
    {
        try
        {
            lblItemName.setText(itemName);
            DefaultTableModel dmCharactersticsMaster = (DefaultTableModel) tblCharactersticsMaster.getModel();
            dmCharactersticsMaster.setRowCount(0);
            String sql = " select b.strCharCode,b.strCharName,b.strCharType "
                    + " from tblitemcharctersticslinkupdtl a,tblcharactersticsmaster b "
                    + " where a.strCharCode=b.strCharCode "
                    + " and a.strItemCode='" + itemCode + "' group by a.strCharCode  order by b.strCharType desc";
            ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            while (rs.next())
            {
                String charVal = "";
                if (rs.getString(3).equalsIgnoreCase("Value"))
                {
                    if (hmValueChar.containsKey(itemCode))
                    {
                        /*
                         List<String> listCharDtl=hmValueChar.get(itemCode);
                         for(String charDtl:listCharDtl)
                         {
                         String[] arrCharDtl=charDtl.split("#");
                         if(arrCharDtl[2].equals("Value"))
                         {
                         if(arrCharDtl[0].equals(rs.getString(1)))
                         {
                         charVal=arrCharDtl[1];
                         }
                         }
                         }*/

                        Map<String, clsCharacteristics> hmValueCharDtl = hmValueChar.get(itemCode);
                        if (hmValueCharDtl.containsKey(rs.getString(1)))
                        {
                            charVal = hmValueCharDtl.get(rs.getString(1)).getCharValue();
                        }
                    }
                }
                else if (hmTextChar.containsKey(itemCode))
                {
                    Map<String, clsCharacteristics> hmTextCharDtl = hmTextChar.get(itemCode);
                    for (Map.Entry<String, clsCharacteristics> entry : hmTextCharDtl.entrySet())
                    {
                        clsCharacteristics objCharDtl = entry.getValue();
                        if (objCharDtl.getCharCode().equals(rs.getString(1)))
                        {
                            charVal = objCharDtl.getCharValue();
                        }
                    }
                }
                Object[] column =
                {
                    rs.getString(2), charVal, rs.getString(1), itemCode, rs.getString(3)
                };
                dmCharactersticsMaster.addRow(column);
            }
            rs.close();

            tblCharactersticsMaster.setRowHeight(25);
            tblCharactersticsMaster.setModel(dmCharactersticsMaster);

            txtTextCharValue.setText("");
            lblTextCharName.setText("");
            lblHCharCode.setText("");
            lblHItemCode.setText("");
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    //Function To Show Dynamically Radio Button For Selected Characterstics like Flavour or Shape 
    private void funFillCharactersticsValue(final String itemCode, final String charCode, final String charName, final String charType) throws Exception
    {
        listCharactersticsValue = new ArrayList<>();

        String sql = " select a.strCharValue from tblitemcharctersticslinkupdtl a"
                + " where a.strCharCode='" + charCode + "' and a.strItemCode='" + itemCode + "'"
                + " group by a.strCharValue order by a.strCharValue ";
        ResultSet rsCharValue = clsGlobalVarClass.dbMysql.executeResultSet(sql);
        while (rsCharValue.next())
        {
            /*
             List<String> listCharValue1=new ArrayList<String>();
             if(hmValueChar.containsKey(itemCode))
             {
             listCharValue1=hmValueChar.get(itemCode);
             }
             if(listCharValue1.size()>0)
             {
             if(listCharValue1.contains(charCode+"#"+rsCharValue.getString(1)+"#"+charType+"#"+charName))
             {
             }
             }*/

            clsCharacteristics objCharDtl = new clsCharacteristics();
            objCharDtl.setItemCode(itemCode);
            objCharDtl.setCharCode(charCode);
            objCharDtl.setCharName(charName);
            objCharDtl.setCharValue(rsCharValue.getString(1));
            objCharDtl.setCharType("Value");
            listCharactersticsValue.add(objCharDtl);
        }
        rsCharValue.close();
        funFillCharactersticsValueTable(listCharactersticsValue);
    }

//Function To Fill tblCharacterstics value table using radio button
    private void funFillCharactersticsValueTable(List<clsCharacteristics> arrListCharactersticsValue)
    {
        try
        {
            cmbCharValue.removeAllItems();
            if (arrListCharactersticsValue.size() > 0)
            {
                for (int i = 0; i < arrListCharactersticsValue.size(); i++)
                {
                    clsCharacteristics objChar = arrListCharactersticsValue.get(i);
                    cmbCharValue.addItem(objChar.getCharValue());
                }
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }


    private void tblItemTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblItemTableMouseClicked
        // TODO add your handling code here:
        funItemTablePressed("Mouse");
    }//GEN-LAST:event_tblItemTableMouseClicked

    private void btnChangeQtyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChangeQtyActionPerformed
        // TODO add your handling code here:
        flgChangeQty = true;
        funItemTablePressed("KeyBoard");
    }//GEN-LAST:event_btnChangeQtyActionPerformed

    private void btnUpMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnUpMouseClicked
        // TODO add your handling code here:
        funUPButtonPressed();
    }//GEN-LAST:event_btnUpMouseClicked

    private void btnUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpActionPerformed
        funUPButtonPressed();
    }//GEN-LAST:event_btnUpActionPerformed

    private void btnDownMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDownMouseClicked
        // TODO add your handling code here:
        funDownButtonPressed();
    }//GEN-LAST:event_btnDownMouseClicked

    private void btnDelItemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDelItemMouseClicked
        // TODO add your handling code here:
        funDeleteButtonPressed();
    }//GEN-LAST:event_btnDelItemMouseClicked

    private void btnDelItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelItemActionPerformed
        funDeleteButtonPressed();
    }//GEN-LAST:event_btnDelItemActionPerformed

    private void btnPrevItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrevItemActionPerformed
        // TODO add your handling code here:
        try
        {
            btnNextItem.setEnabled(true);
            JButton[] btnItemArray =
            {
                btnIItem1, btnIItem2, btnIItem3, btnIItem4, btnIItem5, btnIItem6, btnIItem7, btnIItem8, btnIItem9, btnIItem10, btnIItem11, btnIItem12, btnIItem13, btnIItem14, btnIItem15, btnIItem16
            };
            nextItemClick--;
            if (nextItemClick == 0)
            {
                btnPrevItem.setEnabled(false);
            }
            int k = 0;
            nextCnt = nextItemClick * 16;
            limit = nextCnt + 16;
            for (int m = 0; m < 16; m++)
            {
                btnItemArray[m].setText("");
                btnItemArray[m].setForeground(Color.black);
            }
            for (int j = nextCnt; j < limit; j++)
            {
                if (j == itemNames.length)
                {
                    break;
                }
                btnItemArray[k].setText(itemNames[j]);
                k++;
            }
            int startLimit = itemNames.length - 16;
            for (int j = startLimit; j < 16; j++)
            {
                btnItemArray[j].setEnabled(true);
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnPrevItemActionPerformed

    private void btnNextItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextItemActionPerformed
        // TODO add your handling code here:
        try
        {
            btnPrevItem.setEnabled(true);
            JButton[] btnItemArray =
            {
                btnIItem1, btnIItem2, btnIItem3, btnIItem4, btnIItem5, btnIItem6, btnIItem7, btnIItem8, btnIItem9, btnIItem10, btnIItem11, btnIItem12, btnIItem13, btnIItem14, btnIItem15, btnIItem16
            };
            nextItemClick++;
            int itemDiv = itemNames.length / 17;
            if (itemDiv == nextItemClick)
            {
                btnNextItem.setEnabled(false);
            }
            int k = 0;
            nextCnt = nextItemClick * 16;
            limit = nextCnt + 16;
            for (int m1 = 0; m1 < 16; m1++)
            {
                btnItemArray[m1].setText("");
                btnItemArray[m1].setForeground(Color.black);
            }
            for (int j = nextCnt; j < limit; j++)
            {
                if (j == itemNames.length)
                {
                    break;
                }
                btnItemArray[k].setText(itemNames[j]);
                k++;
            }

            int startLimit = itemNames.length - 16;
            for (int j = startLimit; j < 16; j++)
            {
                btnItemArray[j].setEnabled(false);
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnNextItemActionPerformed

    private void lblCustInfoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCustInfoMouseClicked
        // TODO add your handling code here:
        try
        {
            if (clsGlobalVarClass.gCRMInterface.equals("SQY"))
            {
                new frmNumericKeyboard(this, true, "", "Long", "Enter Mobile number").setVisible(true);
                if (clsGlobalVarClass.gNumerickeyboardValue.trim().length() > 0)
                {
                    funCallWebService();
                }
            }
            else
            {
                new frmNumericKeyboard(this, true, "", "Long", "Enter Mobile number").setVisible(true);
                funSetMobileno(clsGlobalVarClass.gNumerickeyboardValue);
            }

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_lblCustInfoMouseClicked

    private int funCallWebService()
    {
        try
        {
            String custCode = "";
            String custMobileNo = clsGlobalVarClass.gNumerickeyboardValue;
            String sql_CustInfo = "select strCustomerCode from tblcustomermaster where longMobileNo=" + custMobileNo;
            ResultSet rsCust = clsGlobalVarClass.dbMysql.executeResultSet(sql_CustInfo);
            if (rsCust.next())
            {
                custCode = rsCust.getString(1);
            }
            rsCust.close();

            clsGlobalVarClass.gCustCodeForAdvOrder = custCode;

            objData = new clsCustomerDataModelForSQY();
            getObjData().setTransactionId("");
            getObjData().setRedeemed_amt(0);
            getObjData().setCustMobileNo(Long.parseLong(custMobileNo));
            getObjData().setOutlet_uuid(clsGlobalVarClass.gOutletUID);
            getObjData().setCustomerCode(custCode);

            DefaultHttpClient httpClient = new DefaultHttpClient();
            String getWebServiceURL = clsGlobalVarClass.gGetWebserviceURL;

            getWebServiceURL += "" + custMobileNo + "/outlet/" + clsGlobalVarClass.gOutletUID + "/";
            HttpGet getRequest = new HttpGet(getWebServiceURL);
            //HttpGet getRequest = new HttpGet("http://4review.firstquadrant.co.in/v1/redeemphonetransaction/phonenumber/"+custMobileNo+"/outlet/f3fb30af-2aad-4b76-bd66-31c67230a1aa/");
            //System.out.println("http://4review.firstquadrant.co.in/v1/redeemphonetransaction/phonenumber/" + custMobileNo + "/outlet/f3fb30af-2aad-4b76-bd66-31c67230a1aa/");
            HttpResponse response = httpClient.execute(getRequest);
            BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));

            String output, op = "";
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null)
            {
                op += output;
            }
            System.out.println(op);

            JSONParser p = new JSONParser();
            Object o = p.parse(op);
            JSONObject obj = (JSONObject) o;
            clsGlobalVarClass.gFlgPoints = "DiscountPoints";

            if (null != obj.get("code"))
            {
                if (Integer.parseInt(obj.get("code").toString()) == 323)
                {
                    JOptionPane.showMessageDialog(this, "Discount Request Expired! Please ask the customer to regenerate discount request!");
                    return 0;
                }
            }
            getObjData().setTransactionId(obj.get("transaction_id").toString());
            getObjData().setStatus(Integer.parseInt(obj.get("status").toString()));
            getObjData().setConsumer(obj.get("consumer").toString());
            getObjData().setRedeemed_amt(Double.parseDouble(obj.get("redeemed_amt").toString()));
            httpClient.getConnectionManager().shutdown();
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
        return 1;
    }

    private void funGetManualAdvOrderNo()
    {

        if (txtManualAdvOrderNo.getText().trim().length() == 0)
        {
            new frmAlfaNumericKeyBoard(this, true, "1", "Enter Manual Adv Order No").setVisible(true);
            txtManualAdvOrderNo.setText(clsGlobalVarClass.gKeyboardValue);
        }
        else
        {
            new frmAlfaNumericKeyBoard(this, true, txtManualAdvOrderNo.getText(), "1", "Enter Manual Adv Order No").setVisible(true);
            txtManualAdvOrderNo.setText(clsGlobalVarClass.gKeyboardValue);
        }
    }

    //Edit Item weight from grid  
    private void funChangeWeightOfItem(String wgt)
    {
        try
        {
            int selectrow = tblItemTable.getSelectedRow();
            double tempWgt = 0;
            String tempItemName = tblItemTable.getValueAt(tblItemTable.getSelectedRow(), 0).toString();
            String tempItemCode = tblItemTable.getValueAt(tblItemTable.getSelectedRow(), 4).toString();
            double price = Double.parseDouble(tblItemTable.getModel().getValueAt(selectrow, 5).toString());
            double qty = Double.parseDouble(tblItemTable.getModel().getValueAt(selectrow, 1).toString());
            if (clsGlobalVarClass.gProductionLinkup)
            {
                double weight = Double.parseDouble(wgt);
                if ((weight % itemIncrWgt) == 0)
                {
                    tempWgt += weight;
                    //price = originalPrice / itemMinWgt;
                    price = price * tempWgt * qty;
                }
                else
                {
                    JOptionPane.showMessageDialog(this, "Please Enter weight in multiple of" + " " + itemIncrWgt + " kg " + "!!!!!");
                    return;
                }
            }
            sql = "update tbladvbookitemtemp set strItemName='" + tempItemName + "'"
                    + ",dblItemQuantity=" + qty + ",dblAmount=" + price + ",dblWeight=" + tempWgt + ""
                    + ",strUserEdited='" + clsGlobalVarClass.gUserCode + "',dteDateEdited='" + clsGlobalVarClass.getCurrentDateTime() + "' "
                    + "where strItemCode='" + tempItemCode + "'";
            clsGlobalVarClass.dbMysql.execute(sql);
            funFillAdvanceOrderItemGrid();
            flgChangeQty = false;
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
        finally
        {
            clsGlobalVarClass.gNumerickeyboardValue = null;
        }
    }

    private void funCopyImageIfPresent()
    {
        try
        {
            if (null != tempFile && null != lblItemlImage.getIcon())
            {
                String filePath = System.getProperty("user.dir");

                funCreateItemImagesFolder();
                destFile = new File(filePath + "/itemImages/" + tempFile.getName());
                if (destFile.exists())
                {
                    destFile.delete();
                }
                funCopyImageFiles(tempFile, destFile);
            }
        }
        catch (IOException e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    private void funCopyImage2IfPresent()
    {
        try
        {
            if (null != fileImageSpecialSymbol && null != lblSymbolImage.getIcon())
            {
                String filePath = System.getProperty("user.dir");

                funCreateItemImagesFolder();
                destFile = new File(filePath + "/itemImages/" + fileImageSpecialSymbol.getName());
                if (destFile.exists())
                {
                    destFile.delete();
                }
                funCopyImageFiles(fileImageSpecialSymbol, destFile);
            }
        }
        catch (IOException e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    private void funCreateItemImagesFolder()
    {
        try
        {
            filePath = System.getProperty("user.dir");
            File file = new File(filePath + "/itemImages");
            if (!file.exists())
            {
                if (file.mkdir())
                {
                    //System.out.println("Directory is created!");
                }
                else
                {
                    //System.out.println("Failed to create directory!");
                }
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    private void funCopyImageFiles(File source, File dest) throws IOException
    {
        Files.copy(source.toPath(), dest.toPath());
    }

    private void funSetImage(String imageName)
    {
        try
        {
            String filePath = System.getProperty("user.dir");
            tempFile = new File(filePath + "/itemImages/" + imageName);
            ImageIcon icon1 = new ImageIcon(ImageIO.read(tempFile));
            lblItemlImage.setText("");
            lblItemlImage.setIcon(icon1);
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            lblItemlImage.setText("NO IMAGE");
            lblItemlImage.setIcon(null);
        }
    }

    private void funSetSpecialSymbolImage(String imageName)
    {
        try
        {
            String filePath = System.getProperty("user.dir");
            fileImageSpecialSymbol = new File(filePath + "/itemImages/" + imageName);
            ImageIcon icon1 = new ImageIcon(ImageIO.read(fileImageSpecialSymbol));
            lblSymbolImage.setText("");
            lblSymbolImage.setIcon(icon1);
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            lblSymbolImage.setText("NO IMAGE");
            lblSymbolImage.setIcon(null);
        }
    }

    private void funLoadPriceOfItems()
    {
        obj_List_ItemPrice = new ArrayList<>();
        list_ItemNames_Buttoms = new ArrayList();
        String sql_itmdtl = "";
        try
        {
            sql_itmdtl = " SELECT a.strItemCode,b.strItemName,a.strTextColor,a.strPriceMonday,"
                    + "a.strPriceTuesday,a.strPriceWednesday,a.strPriceThursday,a.strPriceFriday, "
                    + "a.strPriceSaturday,a.strPriceSunday,a.tmeTimeFrom,a.strAMPMFrom,a.tmeTimeTo,"
                    + "a.strAMPMTo,a.strCostCenterCode,a.strHourlyPricing,a.strSubMenuHeadCode,"
                    + "a.dteFromDate,a.dteToDate,b.strStockInEnable,b.intDeliveryDays,b.dblIncrementalWeight"
                    + ",b.dblMinWeight,b.strNoDeliveryDays,b.dblPurchaseRate,a.strMenuCode "
                    + "FROM tblmenuitempricingdtl a ,tblitemmaster b "
                    + "WHERE a.strItemCode=b.strItemCode and (a.strPosCode='" + clsGlobalVarClass.gPOSCode + "' or a.strPosCode='All POS') "
                    + "ORDER BY b.strItemName ASC ";
            //System.out.println("sql_itmdtl:"+sql_itmdtl) ;
            List<String> arrExtraParameter;
            ResultSet rsItemPriceInfo = clsGlobalVarClass.dbMysql.executeResultSet(sql_itmdtl);
            while (rsItemPriceInfo.next())
            {
                arrExtraParameter = new ArrayList<String>();
                list_ItemNames_Buttoms.add(rsItemPriceInfo.getString(2));
                if (hmExtraParam.containsKey(rsItemPriceInfo.getString(1)))
                {
                    arrExtraParameter.add(rsItemPriceInfo.getString(21) + "#" + rsItemPriceInfo.getString(22) + "#" + rsItemPriceInfo.getString(23) + "#" + rsItemPriceInfo.getString(24));
                }
                else
                {
                    arrExtraParameter.add(rsItemPriceInfo.getString(21) + "#" + rsItemPriceInfo.getString(22) + "#" + rsItemPriceInfo.getString(23) + "#" + rsItemPriceInfo.getString(24));
                }
                hmExtraParam.put(rsItemPriceInfo.getString(1), arrExtraParameter);

                clsItemPriceDtl ob = new clsItemPriceDtl(rsItemPriceInfo.getString(1), rsItemPriceInfo.getString(2),
                        rsItemPriceInfo.getDouble(4),
                        rsItemPriceInfo.getDouble(5), rsItemPriceInfo.getDouble(6), rsItemPriceInfo.getDouble(7), rsItemPriceInfo.getDouble(8),
                        rsItemPriceInfo.getDouble(9), rsItemPriceInfo.getDouble(10), rsItemPriceInfo.getString(11), rsItemPriceInfo.getString(12),
                        rsItemPriceInfo.getString(13), rsItemPriceInfo.getString(14), rsItemPriceInfo.getString(15),
                        rsItemPriceInfo.getString(3), rsItemPriceInfo.getString(16), rsItemPriceInfo.getString(17), rsItemPriceInfo.getString(18),
                        rsItemPriceInfo.getString(19), rsItemPriceInfo.getString(20), rsItemPriceInfo.getDouble(21), rsItemPriceInfo.getString(22));
                obj_List_ItemPrice.add(ob);
                rsItemPriceInfo.getString(2);
            }
            rsItemPriceInfo.close();
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    private double funGetFinalPrice(clsItemPriceDtl ob)
    {
        double Price = 0.00;
        String fromTime = ob.getTmeTimeFrom();
        String toTime = ob.getTmeTimeTo();
        String fromAMPM = ob.getStrAMPMFrom();
        String toAMPM = ob.getStrAMPMTo();
        String hourlyPricing = ob.getStrHourlyPricing();

        if (hourlyPricing.equals("Yes"))
        {
            String[] spFromTime = fromTime.split(":");
            String[] spToTime = toTime.split(":");
            int fromHour = Integer.parseInt(spFromTime[0]);
            int fromMin = Integer.parseInt(spFromTime[1]);
            if (fromAMPM.equals("PM"))
            {
                fromHour += 12;
            }
            int toHour = Integer.parseInt(spToTime[0]);
            int toMin = Integer.parseInt(spToTime[1]);
            if (toAMPM.equals("PM"))
            {
                toHour += 12;
            }
            String[] spCurrTime = objUtility.funGetCurrentTime().split(" ");
            String[] spCurrentTime = spCurrTime[0].split(":");

            int currHour = Integer.parseInt(spCurrentTime[0]);
            int currMin = Integer.parseInt(spCurrentTime[1]);
            String currDate = objUtility.funGetCurrentDate();
            currDate = currDate + " " + currHour + ":" + currMin + ":00";

            //2014-09-09 23:35:00                    
            String fromDate = objUtility.funGetCurrentDate();
            String toDate = objUtility.funGetCurrentDate();
            fromDate = fromDate + " " + fromHour + ":" + fromMin + ":00";
            toDate = toDate + " " + toHour + ":" + toMin + ":00";

            long diff1 = objUtility.funCompareTime(fromDate, currDate);
            long diff2 = objUtility.funCompareTime(currDate, toDate);
            if (diff1 > 0 && diff2 > 0)
            {
                switch (objUtility.funGetDayForPricing())
                {
                    case "strPriceMonday":
                        Price = ob.getStrPriceMonday();
                        break;

                    case "strPriceTuesday":
                        Price = ob.getStrPriceTuesday();
                        break;

                    case "strPriceWednesday":
                        Price = ob.getStrPriceWednesday();
                        break;

                    case "strPriceThursday":
                        Price = ob.getStrPriceThursday();
                        break;

                    case "strPriceFriday":
                        Price = ob.getStrPriceFriday();
                        break;

                    case "strPriceSaturday":
                        Price = ob.getStrPriceSaturday();
                        break;

                    case "strPriceSunday":
                        Price = ob.getStrPriceSunday();
                        break;
                }
            }
        }
        else
        {
            switch (objUtility.funGetDayForPricing())
            {
                case "strPriceMonday":
                    Price = ob.getStrPriceMonday();
                    break;

                case "strPriceTuesday":
                    Price = ob.getStrPriceTuesday();
                    break;

                case "strPriceWednesday":
                    Price = ob.getStrPriceWednesday();
                    break;

                case "strPriceThursday":
                    Price = ob.getStrPriceThursday();
                    break;

                case "strPriceFriday":
                    Price = ob.getStrPriceFriday();
                    break;

                case "strPriceSaturday":
                    Price = ob.getStrPriceSaturday();
                    break;

                case "strPriceSunday":
                    Price = ob.getStrPriceSunday();
                    break;
            }
        }
        return Price;
    }

    private void funSetShortCutKeys()
    {
        btnChangeQty.setVisible(true);
        btnHome.setMnemonic('h');
        btnModifier.setMnemonic('f');
        btnHomeDel.setMnemonic('o');
        btnPlu.setMnemonic('p');
        btnUp.setMnemonic('u');
        btnDown.setMnemonic('w');
        btnDelItem.setMnemonic('l');
        btnChangeQty.setMnemonic('q');
        btnDone.setMnemonic('d');
        btnSettle1.setMnemonic('r');
    }

    private class CancelAction extends AbstractAction
    {

        @Override
        public void actionPerformed(ActionEvent ev)
        {
            if (txtPLU_ItemSearch.isFocusable() && txtPLU_ItemSearch.getText().trim().length() == 0)
            {
                funSetVisiblePanels(true);
            }
            else
            {
                txtPLU_ItemSearch.setText("");
                txtPLU_ItemSearch.requestFocus();
            }
        }
    }

    private void funSetVisiblePanels(boolean isVisible)
    {
        NewOrderPanel.setVisible(isVisible);
        panelNavigate.setVisible(isVisible);
        panelSubGroup.setVisible(isVisible);

        panel_PLU.setVisible(!isVisible);
        txtPLU_ItemSearch.setText("");
        btnPlu.requestFocus();
    }

    private void funAttatchSpecialSymbol()
    {
        try
        {
            int selectedRow = tblItemTable.getSelectedRow();
            if (selectedRow == -1)
            {
                JOptionPane.showMessageDialog(null, "Please Select Item to Attatch Image!!!");
                return;
            }
            else
            {
                File file = fileImageSpecialSymbol;
                if (clsGlobalVarClass.gProductionLinkup)
                {
                    String itemCode = tblItemTable.getValueAt(selectedRow, 4).toString();
                    hmAttachedImage.put(itemCode, file);
                    JOptionPane.showMessageDialog(null, "Image Attached!!!");
                    lblSymbolImage.setIcon(null);
                    fileImageSpecialSymbol = null;
                }
                else
                {
                    String itemCode = tblItemTable.getValueAt(selectedRow, 3).toString();
                }
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    private void funSetText()
    {
        List<clsCharacteristics> arrayListTemp = new ArrayList<>();
        String str = txtCharValueSearch.getText().toString();

        for (int cnt = 0; cnt < listCharactersticsValue.size(); cnt++)
        {
            clsCharacteristics objChar = (clsCharacteristics) listCharactersticsValue.get(cnt);
            String charValue = objChar.getCharValue().toString().toLowerCase();
            str = str.toString().toLowerCase();
            if (charValue.contains(str))
            {
                clsCharacteristics objCharDtl = new clsCharacteristics();
                objCharDtl.setItemCode(objChar.getItemCode());
                objCharDtl.setCharCode(objChar.getCharCode());
                objCharDtl.setCharName(objChar.getCharName());
                objCharDtl.setCharValue(objChar.getCharValue());
                objCharDtl.setCharType(objChar.getCharType());
                arrayListTemp.add(objCharDtl);
            }
        }
        if (arrayListTemp.size() > 0)
        {
            listCharactersticsValue = arrayListTemp;
            funFillCharactersticsValueTable(arrayListTemp);
        }
    }

    private void funSetSelectedCharValueToTable()
    {
        String selectedCharValue = cmbCharValue.getSelectedItem().toString().trim();
        String itemCode = lblHItemCode.getText().trim();
        String charCode = "", charName = "", charType = "";
        boolean flgFound = false;

        /*
         List<String> listCharValue=new ArrayList<String>();
         if(hmValueChar.containsKey(itemCode))
         {
         listCharValue=hmValueChar.get(itemCode);
         hmValueChar.remove(itemCode);
         }
         for (int cnt = 0; cnt <listCharactersticsValue.size(); cnt++)
         {
         clsCharacteristics objChar = (clsCharacteristics) listCharactersticsValue.get(cnt);
         if (itemCode.equals(objChar.getItemCode()) && selectedCharValue.equals(objChar.getCharValue()) )
         {
         flgFound=true;
         charCode=objChar.getCharCode().trim();
         charName=objChar.getCharName().trim();
         charType=objChar.getCharType().trim();
         break;
         }
         }
         if(flgFound)
         {
         String charValues=charCode+"#"+selectedCharValue+"#"+charType+"#"+charName;
         if(listCharValue.contains(charValues))
         {
         listCharValue.remove(charValues);
         }
         listCharValue.add(charValues);
         }
         if(listCharValue.size()>0)
         {
         hmValueChar.put(itemCode, listCharValue);
         funFillCharactersticsMaster(itemCode,lblItemName.getText().toString());
         txtCharValueSearch.setText("");
         }
         else
         {
         JOptionPane.showMessageDialog(null, cmbCharValue.getSelectedItem().toString()+"Select character value first!!!");
         }
         */
        Map<String, clsCharacteristics> hmCharValueDtl = new HashMap<String, clsCharacteristics>();
        if (hmValueChar.containsKey(itemCode))
        {
            hmCharValueDtl = hmValueChar.get(itemCode);
            hmValueChar.remove(itemCode);
        }
        for (int cnt = 0; cnt < listCharactersticsValue.size(); cnt++)
        {
            clsCharacteristics objChar = (clsCharacteristics) listCharactersticsValue.get(cnt);
            if (itemCode.equals(objChar.getItemCode().trim()) && selectedCharValue.equals(objChar.getCharValue().trim()))
            {
                flgFound = true;
                charCode = objChar.getCharCode().trim();
                charName = objChar.getCharName().trim();
                charType = objChar.getCharType().trim();
                break;
            }
        }
        if (flgFound)
        {
            clsCharacteristics objCharValues = new clsCharacteristics();
            objCharValues.setCharCode(charCode);
            objCharValues.setCharType(charType);
            objCharValues.setCharName(charName);
            objCharValues.setItemCode(itemCode);
            objCharValues.setCharValue(selectedCharValue);
            hmCharValueDtl.put(charCode, objCharValues);
        }
        if (hmCharValueDtl.size() > 0)
        {
            hmValueChar.put(itemCode, hmCharValueDtl);
            funFillCharactersticsMaster(itemCode, lblItemName.getText().toString());
            txtCharValueSearch.setText("");
        }
        else
        {
            JOptionPane.showMessageDialog(null, cmbCharValue.getSelectedItem().toString() + " Select character value first!!!");
        }
    }


    private void btnIItem2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnIItem2MouseClicked

    }//GEN-LAST:event_btnIItem2MouseClicked

    private void btnIItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIItem2ActionPerformed
        funGetPrice(funConvertString(btnIItem2.getText().trim()));
    }//GEN-LAST:event_btnIItem2ActionPerformed

    private void btnIItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIItem1ActionPerformed

        funGetPrice(funConvertString(btnIItem1.getText().trim()));
    }//GEN-LAST:event_btnIItem1ActionPerformed

    private void btnIItem3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnIItem3MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnIItem3MouseClicked

    private void btnIItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIItem3ActionPerformed
        // TODO add your handling code here:
        funGetPrice(funConvertString(btnIItem3.getText().trim()));
    }//GEN-LAST:event_btnIItem3ActionPerformed

    private void btnIItem4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnIItem4MouseClicked
        // TODO add your handling code here:itemName=btnIItem1.getText();
    }//GEN-LAST:event_btnIItem4MouseClicked

    private void btnIItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIItem4ActionPerformed
        // TODO add your handling code here:
        funGetPrice(funConvertString(btnIItem4.getText().trim()));
    }//GEN-LAST:event_btnIItem4ActionPerformed

    private void btnIItem5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnIItem5MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnIItem5MouseClicked

    private void btnIItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIItem5ActionPerformed
        // TODO add your handling code here:
        funGetPrice(funConvertString(btnIItem5.getText().trim()));
    }//GEN-LAST:event_btnIItem5ActionPerformed

    private void btnIItem6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnIItem6MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnIItem6MouseClicked

    private void btnIItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIItem6ActionPerformed
        // TODO add your handling code here:
        funGetPrice(funConvertString(btnIItem6.getText().trim()));
    }//GEN-LAST:event_btnIItem6ActionPerformed

    private void btnIItem7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnIItem7MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnIItem7MouseClicked

    private void btnIItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIItem7ActionPerformed
        // TODO add your handling code here:
        funGetPrice(funConvertString(btnIItem7.getText().trim()));
    }//GEN-LAST:event_btnIItem7ActionPerformed

    private void btnIItem8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnIItem8MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnIItem8MouseClicked

    private void btnIItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIItem8ActionPerformed
        // TODO add your handling code here:
        funGetPrice(funConvertString(btnIItem8.getText().trim()));
    }//GEN-LAST:event_btnIItem8ActionPerformed

    private void btnIItem9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnIItem9MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnIItem9MouseClicked

    private void btnIItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIItem9ActionPerformed
        // TODO add your handling code here:
        funGetPrice(funConvertString(btnIItem9.getText().trim()));
    }//GEN-LAST:event_btnIItem9ActionPerformed

    private void btnIItem10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnIItem10MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnIItem10MouseClicked

    private void btnIItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIItem10ActionPerformed
        // TODO add your handling code here:
        funGetPrice(funConvertString(btnIItem10.getText().trim()));
    }//GEN-LAST:event_btnIItem10ActionPerformed

    private void btnIItem11MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnIItem11MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnIItem11MouseClicked

    private void btnIItem11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIItem11ActionPerformed
        // TODO add your handling code here:
        funGetPrice(funConvertString(btnIItem11.getText().trim()));
    }//GEN-LAST:event_btnIItem11ActionPerformed

    private void btnIItem12MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnIItem12MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnIItem12MouseClicked

    private void btnIItem12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIItem12ActionPerformed
        // TODO add your handling code here:
        funGetPrice(funConvertString(btnIItem12.getText().trim()));
    }//GEN-LAST:event_btnIItem12ActionPerformed

    private void btnIItem13MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnIItem13MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnIItem13MouseClicked

    private void btnIItem13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIItem13ActionPerformed
        // TODO add your handling code here:
        funGetPrice(funConvertString(btnIItem13.getText().trim()));
    }//GEN-LAST:event_btnIItem13ActionPerformed

    private void btnIItem14MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnIItem14MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnIItem14MouseClicked

    private void btnIItem14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIItem14ActionPerformed
        // TODO add your handling code here:
        funGetPrice(funConvertString(btnIItem14.getText().trim()));
    }//GEN-LAST:event_btnIItem14ActionPerformed

    private void btnIItem15MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnIItem15MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnIItem15MouseClicked

    private void btnIItem15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIItem15ActionPerformed
        // TODO add your handling code here:
        funGetPrice(funConvertString(btnIItem15.getText().trim()));
    }//GEN-LAST:event_btnIItem15ActionPerformed

    private void btnIItem16MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnIItem16MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnIItem16MouseClicked

    private void btnIItem16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIItem16ActionPerformed
        // TODO add your handling code here:
        funGetPrice(funConvertString(btnIItem16.getText().trim()));
    }//GEN-LAST:event_btnIItem16ActionPerformed

    private void btnPrevItemSortingMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPrevItemSortingMouseClicked
        // TODO add your handling code here:
        try
        {
            if (btnPrevItemSorting.isEnabled())
            {
                JButton btnArray[] =
                {
                    btnItemSorting1, btnItemSorting2, btnItemSorting3, btnItemSorting4
                };
                funReset_TopSortingButtons();

                if (totalItems > 4)
                {
                    int x = totalItems - itemNumber;
                    itemNumber = x;
                    for (int i = 0; i < 4; i++, itemNumber++)
                    {
                        btnArray[i].setText(listTopButtonName.get(itemNumber));
                        btnArray[i].setEnabled(true);
                    }
                }

                btnNextItemSorting.setEnabled(true);
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnPrevItemSortingMouseClicked

    private void btnItemSorting1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnItemSorting1MouseClicked

        try
        {
            if (btnItemSorting1.isEnabled())
            {
                int index = listTopButtonName.indexOf(btnItemSorting1.getText());
                String buttonCode = listTopButtonCode.get(index);
                funRefreshButtonItemSelectionWise(menuHeadCode, buttonCode);
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnItemSorting1MouseClicked

    private void btnItemSorting2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnItemSorting2MouseClicked
        // TODO add your handling code here:
        try
        {
            if (btnItemSorting2.isEnabled())
            {

                int index = listTopButtonName.indexOf(btnItemSorting2.getText());
                String buttonCode = listTopButtonCode.get(index);
                funRefreshButtonItemSelectionWise(menuHeadCode, buttonCode);
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnItemSorting2MouseClicked

    private void btnItemSorting3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnItemSorting3MouseClicked
        // TODO add your handling code here:
        try
        {
            if (btnItemSorting3.isEnabled())
            {
                int index = listTopButtonName.indexOf(btnItemSorting3.getText());
                String buttonCode = listTopButtonCode.get(index);
                funRefreshButtonItemSelectionWise(menuHeadCode, buttonCode);
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnItemSorting3MouseClicked

    private void btnNextItemSortingMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNextItemSortingMouseClicked
        // TODO add your handling code here:
        try
        {
            if (btnNextItemSorting.isEnabled())
            {
                JButton btnArray[] =
                {
                    btnItemSorting1, btnItemSorting2, btnItemSorting3, btnItemSorting4
                };
                funReset_TopSortingButtons();
                int x = totalItems - itemNumber;
                if (x > 4)
                {
                    for (int i = 0; i < 4; i++, itemNumber++)
                    {

                        btnArray[i].setText(listTopButtonName.get(itemNumber));
                        btnArray[i].setEnabled(true);
                    }
                }
                else
                {
                    for (int i = 0; i < x; i++, itemNumber++)
                    {
                        btnArray[i].setText(listTopButtonName.get(itemNumber));
                        btnArray[i].setEnabled(true);
                    }
                }
                if (x > 4)
                {
                    btnNextItemSorting.setEnabled(true);
                }
                btnPrevItemSorting.setEnabled(true);
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnNextItemSortingMouseClicked

    private void btnItemSorting4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnItemSorting4MouseClicked
        // TODO add your handling code here:
        try
        {
            if (btnItemSorting4.isEnabled())
            {
                int index = listTopButtonName.indexOf(btnItemSorting4.getText());
                String buttonCode = listTopButtonCode.get(index);
                funRefreshButtonItemSelectionWise(menuHeadCode, buttonCode);
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
        }
    }//GEN-LAST:event_btnItemSorting4MouseClicked

    private void btnMenu3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMenu3MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnMenu3MouseClicked

    private void btnMenu3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMenu3ActionPerformed
        // TODO add your handling code here:
        try
        {
            funResetItemNames();
            funResetItemButtonText(menuNames1[1]);
            if (!"NA".equalsIgnoreCase(clsGlobalVarClass.gMenuItemSortingOn))
            {
                funFillTopButtonList(menuHeadCode);
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnMenu3ActionPerformed

    private void btnMenu2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMenu2ActionPerformed
        // TODO add your handling code here:
        try
        {
            funResetItemNames();
            funResetItemButtonText(menuNames1[0]);
            if (!"NA".equalsIgnoreCase(clsGlobalVarClass.gMenuItemSortingOn))
            {
                funFillTopButtonList(menuHeadCode);
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnMenu2ActionPerformed

    private void btnMenu4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMenu4MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnMenu4MouseClicked

    private void btnMenu4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMenu4ActionPerformed
        // TODO add your handling code here:
        try
        {
            funResetItemNames();
            funResetItemButtonText(menuNames1[2]);
            if (!"NA".equalsIgnoreCase(clsGlobalVarClass.gMenuItemSortingOn))
            {
                funFillTopButtonList(menuHeadCode);
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnMenu4ActionPerformed

    private void btnMenu6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMenu6ActionPerformed
        // TODO add your handling code here:
        try
        {
            funResetItemNames();
            funResetItemButtonText(menuNames1[4]);
            if (!"NA".equalsIgnoreCase(clsGlobalVarClass.gMenuItemSortingOn))
            {
                funFillTopButtonList(menuHeadCode);
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnMenu6ActionPerformed

    private void btnMenu8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMenu8ActionPerformed
        // TODO add your handling code here:
        try
        {
            funResetItemNames();
            funResetItemButtonText(menuNames1[6]);
            if (!"NA".equalsIgnoreCase(clsGlobalVarClass.gMenuItemSortingOn))
            {
                funFillTopButtonList(menuHeadCode);
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnMenu8ActionPerformed

    private void btnMenu7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMenu7ActionPerformed
        // TODO add your handling code here:
        try
        {
            funResetItemNames();
            funResetItemButtonText(menuNames1[5]);
            if (!"NA".equalsIgnoreCase(clsGlobalVarClass.gMenuItemSortingOn))
            {
                funFillTopButtonList(menuHeadCode);
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnMenu7ActionPerformed

    private void btnPopularActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPopularActionPerformed
        // TODO add your handling code here:
        try
        {
            funPopularItem();
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnPopularActionPerformed

    private void btnMenu5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMenu5ActionPerformed
        // TODO add your handling code here:
        try
        {
            funResetItemNames();
            funResetItemButtonText(menuNames1[3]);
            if (!"NA".equalsIgnoreCase(clsGlobalVarClass.gMenuItemSortingOn))
            {
                funFillTopButtonList(menuHeadCode);
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnMenu5ActionPerformed

    private void btnPrevMenuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPrevMenuMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnPrevMenuMouseClicked

    private void btnPrevMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrevMenuActionPerformed
        try
        {
            int cntMenu = 0, cntMenu1 = 0;
            btnNextMenu.setEnabled(true);
            JButton[] btnMenuArray =
            {
                btnMenu2, btnMenu3, btnMenu4, btnMenu5, btnMenu6, btnMenu7, btnMenu8
            };
            nextClick--;
            if (nextClick == 0)
            {
                btnPrevMenu.setEnabled(false);
            }
            nextCnt = nextClick * 7;
            limit = nextCnt + 7;
            for (int cntMenu2 = 0; cntMenu2 < 7; cntMenu2++)
            {
                btnMenuArray[cntMenu2].setText("");
                btnMenuArray[cntMenu2].setEnabled(true);
            }
            for (int cntMenu2 = nextCnt; cntMenu2 < limit; cntMenu2++)
            {
                if (cntMenu2 == menuNames.length)
                {
                    break;
                }
                btnMenuArray[cntMenu1].setText(clsGlobalVarClass.convertString(menuNames[cntMenu2]));
                btnMenuArray[cntMenu1].setEnabled(true);
                menuNames1[cntMenu] = menuNames[cntMenu2];
                cntMenu1++;
                cntMenu++;
            }
            for (int cntMenu2 = cntMenu1; cntMenu1 < 7; cntMenu1++)
            {
                btnMenuArray[cntMenu1].setEnabled(false);
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnPrevMenuActionPerformed

    private void btnNextMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextMenuActionPerformed
        try
        {
            int cntMenu = 0, cntMenu1 = 0;
            btnPrevMenu.setEnabled(true);
            JButton[] btnMenuArray =
            {
                btnMenu2, btnMenu3, btnMenu4, btnMenu5, btnMenu6, btnMenu7, btnMenu8
            };
            nextClick++;
            int div = menuNames.length / 7;
            if (nextClick == div)
            {
                btnNextMenu.setEnabled(false);
            }

            nextCnt = nextClick * 7;
            limit = nextCnt + 7;
            for (int m = 0; m < 7; m++)
            {
                btnMenuArray[m].setText("");
            }
            for (int cntMenu2 = nextCnt; cntMenu2 < limit; cntMenu2++)
            {
                if (cntMenu2 == menuNames.length)
                {
                    break;
                }
                btnMenuArray[cntMenu1].setText(clsGlobalVarClass.convertString(menuNames[cntMenu2]));
                menuNames1[cntMenu] = menuNames[cntMenu2];
                cntMenu1++;
                cntMenu++;
            }
            for (int cntMenu2 = cntMenu1; cntMenu2 < 7; cntMenu2++)
            {
                btnMenuArray[cntMenu2].setEnabled(false);
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnNextMenuActionPerformed

    private void btnNumber2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNumber2MouseClicked
        // TODO add your handling code here:
        funSetSelectedQty(btnNumber2.getText().trim());
    }//GEN-LAST:event_btnNumber2MouseClicked

    private void btnNumber1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNumber1MouseClicked
        // TODO add your handling code here:
        funSetSelectedQty(btnNumber1.getText().trim());
    }//GEN-LAST:event_btnNumber1MouseClicked

    private void btnNumber4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNumber4MouseClicked
        // TODO add your handling code here:
        funSetSelectedQty(btnNumber4.getText().trim());
    }//GEN-LAST:event_btnNumber4MouseClicked

    private void btnNumber3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNumber3MouseClicked
        // TODO add your handling code here:
        funSetSelectedQty(btnNumber3.getText().trim());
    }//GEN-LAST:event_btnNumber3MouseClicked

    private void btnNumber5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNumber5MouseClicked
        // TODO add your handling code here:
        funSetSelectedQty(btnNumber5.getText().trim());
    }//GEN-LAST:event_btnNumber5MouseClicked

    private void btnNumber6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNumber6MouseClicked
        // TODO add your handling code here:
        funSetSelectedQty(btnNumber6.getText().trim());
    }//GEN-LAST:event_btnNumber6MouseClicked

    private void btnNumber7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNumber7MouseClicked
        // TODO add your handling code here:
        funSetSelectedQty(btnNumber7.getText().trim());
    }//GEN-LAST:event_btnNumber7MouseClicked

    private void btnNumber8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNumber8MouseClicked
        // TODO add your handling code here:
        funSetSelectedQty(btnNumber8.getText().trim());
    }//GEN-LAST:event_btnNumber8MouseClicked

    private void btnNumber9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNumber9MouseClicked
        // TODO add your handling code here:
        funSetSelectedQty(btnNumber9.getText().trim());
    }//GEN-LAST:event_btnNumber9MouseClicked

    private void btnMultiQtyMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMultiQtyMouseClicked
        // TODO add your handling code here:
        try
        {
            frmNumberKeyPad num = new frmNumberKeyPad(this, true, "qty");
            num.setVisible(true);
            double result = 0;
            if (null != clsGlobalVarClass.gNumerickeyboardValue)
            {
                result = Double.parseDouble(clsGlobalVarClass.gNumerickeyboardValue);
                clsGlobalVarClass.gNumerickeyboardValue = null;
            }
            if ("".equals(lblAdvOrderno.getText()))
            {
                selectedQty = result;
            }
            if (flgChangeQty == true)
            {
                funChangeQtyOfItem(btnMultiQty.getText().trim());
                flgChangeQty = false;
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnMultiQtyMouseClicked

    private void txtPLU_ItemSearchMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPLU_ItemSearchMouseClicked
        new frmAlfaNumericKeyBoard(null, true, "1", "Search").setVisible(true);
        txtPLU_ItemSearch.setText(clsGlobalVarClass.gKeyboardValue);
    }//GEN-LAST:event_txtPLU_ItemSearchMouseClicked

    private void txtPLU_ItemSearchFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPLU_ItemSearchFocusGained
        funPLUItemSearch();
    }//GEN-LAST:event_txtPLU_ItemSearchFocusGained

    private void txtPLU_ItemSearchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPLU_ItemSearchKeyPressed
        if (txtPLU_ItemSearch.isFocusable() && evt.getKeyCode() == 40)
        {
            tbl_PLU_Items.requestFocus();
            funDownArrowPressedForPLU();
        }
    }//GEN-LAST:event_txtPLU_ItemSearchKeyPressed

    private void txtPLU_ItemSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPLU_ItemSearchKeyReleased
        funPLUItemSearch();
    }//GEN-LAST:event_txtPLU_ItemSearchKeyReleased

    private void txtPLU_ItemSearchKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPLU_ItemSearchKeyTyped

    }//GEN-LAST:event_txtPLU_ItemSearchKeyTyped

    private void bttn_PLU_Panel_CloseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bttn_PLU_Panel_CloseMouseClicked
        fun_PLU_Closed_Button_Pressed();
    }//GEN-LAST:event_bttn_PLU_Panel_CloseMouseClicked

    private void tbl_PLU_ItemsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_PLU_ItemsMouseClicked
        funPLUTableItemsMouseClicked();
    }//GEN-LAST:event_tbl_PLU_ItemsMouseClicked

    private void tbl_PLU_ItemsKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbl_PLU_ItemsKeyPressed
        if (evt.getKeyCode() == 10)
        {
            funPLUTableItemsMouseClicked();
        }
    }//GEN-LAST:event_tbl_PLU_ItemsKeyPressed

    private void tblModifyAdvanceOrderListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblModifyAdvanceOrderListMouseClicked
        // TODO add your handling code here:
        funModifyAdvanceOrder();
    }//GEN-LAST:event_tblModifyAdvanceOrderListMouseClicked

    private void btnExecuteModifyMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnExecuteModifyMouseClicked

        java.util.Date dt = new java.util.Date();
        dt = dteFromModify.getDate();
        String fromdate = (dt.getYear() + 1900) + "-" + (dt.getMonth() + 1) + "-" + (dt.getDate());
        dt = dteToModify.getDate();
        String todate = (dt.getYear() + 1900) + "-" + (dt.getMonth() + 1) + "-" + (dt.getDate());
        funFillAdvanceOrderList(tblModifyAdvanceOrderList, fromdate, todate, "Modify");
    }//GEN-LAST:event_btnExecuteModifyMouseClicked

    private void tblAdvanceOrderListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblAdvanceOrderListMouseClicked
        // TODO add your handling code here:
        int selectedRow = tblAdvanceOrderList.getSelectedRow();
        String custCode = tblAdvanceOrderList.getValueAt(selectedRow, 0).toString();
        String advOrderNo = tblAdvanceOrderList.getValueAt(selectedRow, 2).toString();
        tabPaneAdvOrder.setSelectedIndex(0);
        DefaultTableModel dm = (DefaultTableModel) tblAdvanceOrderList.getModel();
        dm.setRowCount(0);
        tblAdvanceOrderList.setModel(dm);
        frmDirectBiller objDirectBiller = new frmDirectBiller(advOrderNo, custCode);
        objDirectBiller.setVisible(true);
    }//GEN-LAST:event_tblAdvanceOrderListMouseClicked

    private void btnExecuteListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnExecuteListMouseClicked
        // TODO add your handling code here:
        java.util.Date dt = new java.util.Date();
        dt = dteFrom.getDate();
        String fromdate = (dt.getYear() + 1900) + "-" + (dt.getMonth() + 1) + "-" + (dt.getDate());
        dt = dteTo.getDate();
        String todate = (dt.getYear() + 1900) + "-" + (dt.getMonth() + 1) + "-" + (dt.getDate());;
        funFillAdvanceOrderList(tblAdvanceOrderList, fromdate, todate, "List");
    }//GEN-LAST:event_btnExecuteListMouseClicked

    private void txtNoteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtNoteMouseClicked
        // TODO add your handling code here:
        if (txtNote.getText().length() == 0)
        {
            new frmAlfaNumericKeyBoard(this, true, "1", "Enter Remark").setVisible(true);
            txtNote.setText(clsGlobalVarClass.gKeyboardValue);
        }
        else
        {
            new frmAlfaNumericKeyBoard(this, true, txtNote.getText(), "1", "Enter Remark").setVisible(true);
            txtNote.setText(clsGlobalVarClass.gKeyboardValue);
        }
    }//GEN-LAST:event_txtNoteMouseClicked

    private void txtManualAdvOrderNoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtManualAdvOrderNoMouseClicked
        // TODO add your handling code here:
        funGetManualAdvOrderNo();
    }//GEN-LAST:event_txtManualAdvOrderNoMouseClicked

    private void txtManualAdvOrderNoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtManualAdvOrderNoKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtManualAdvOrderNoKeyPressed

    private void btnBrowsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBrowsMouseClicked
        // TODO add your handling code here:

        try
        {
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "png");
            JFileChooser jfc = new JFileChooser();
            jfc.setFileFilter(filter);
            if (jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
            {
                tempFile = jfc.getSelectedFile();
                String imagePath = tempFile.getAbsolutePath();
                lblItemlImage.setIcon(new ImageIcon(imagePath));
                funCopyImageIfPresent();
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnBrowsMouseClicked

    private void btnBrowsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBrowsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnBrowsActionPerformed

    private void btnBrowseSpecialSymbolMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBrowseSpecialSymbolMouseClicked
        // TODO add your handling code here:
        try
        {
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "png");
            JFileChooser jfc = new JFileChooser();
            jfc.setFileFilter(filter);
            if (jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
            {
                fileImageSpecialSymbol = jfc.getSelectedFile();
                String imagePath = fileImageSpecialSymbol.getAbsolutePath();
                lblSymbolImage.setIcon(new ImageIcon(imagePath));
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnBrowseSpecialSymbolMouseClicked

    private void btnBrowseSpecialSymbolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBrowseSpecialSymbolActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnBrowseSpecialSymbolActionPerformed

    private void tabPaneAdvOrderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tabPaneAdvOrderStateChanged
        // TODO add your handling code here:

        if (tabPaneAdvOrder.getSelectedIndex() == 1)
        {
            java.util.Date dt = new java.util.Date();
            dteFromModify.setDate(dt);
            dteToModify.setDate(dt);
            String fromdate = (dteFromModify.getDate().getYear() + 1900) + "-" + (dteFromModify.getDate().getMonth() + 1) + "-" + (dteFromModify.getDate().getDate());
            String todate = (dteToModify.getDate().getYear() + 1900) + "-" + (dteToModify.getDate().getMonth() + 1) + "-" + (dteToModify.getDate().getDate());
            funFillAdvanceOrderList(tblModifyAdvanceOrderList, fromdate, todate, "Modify");
        }
        if (tabPaneAdvOrder.getSelectedIndex() == 2)
        {
            java.util.Date dt = new java.util.Date();
            dteFrom.setDate(dt);
            dteTo.setDate(dt);
            dt = dteFrom.getDate();
            String fromdate = (dt.getYear() + 1900) + "-" + (dt.getMonth() + 1) + "-" + (dt.getDate());
            dt = dteTo.getDate();
            String todate = (dt.getYear() + 1900) + "-" + (dt.getMonth() + 1) + "-" + (dt.getDate());
            funFillAdvanceOrderList(tblAdvanceOrderList, fromdate, todate, "List");
        }
    }//GEN-LAST:event_tabPaneAdvOrderStateChanged

    private void tabPaneAdvOrderMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabPaneAdvOrderMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tabPaneAdvOrderMouseClicked

    private void btnHomeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHomeMouseClicked
        // TODO add your handling code here:
        funHomeButtonPressed();
    }//GEN-LAST:event_btnHomeMouseClicked

    private void btnDoneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDoneActionPerformed

        try
        {
            funDoneButtonClicked();
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnDoneActionPerformed

    private void btnModifierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModifierActionPerformed
        // TODO add your handling code here:
        funOpenModifierPanel();
    }//GEN-LAST:event_btnModifierActionPerformed

    private void btnPluActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPluActionPerformed
        // TODO add your handling code here:
        funPLUButtonPressed();
    }//GEN-LAST:event_btnPluActionPerformed

    private void btnSettle1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSettle1MouseClicked
        // TODO add your handling code here:
        funOpenDirectBiller();
    }//GEN-LAST:event_btnSettle1MouseClicked

    private void btnWaiterNameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnWaiterNameMouseClicked
        // TODO add your handling code here:
        funSelectWaiter();
    }//GEN-LAST:event_btnWaiterNameMouseClicked

    private void btnHomeDelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHomeDelMouseClicked
        homeDelivery = "Y";
        btnHomeDel.setForeground(Color.black);
    }//GEN-LAST:event_btnHomeDelMouseClicked

    private void btnHomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHomeActionPerformed
        funHomeButtonPressed();
    }//GEN-LAST:event_btnHomeActionPerformed

    private void btnHomeDelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHomeDelActionPerformed
        homeDelivery = "Y";
        btnHomeDel.setForeground(Color.black);
    }//GEN-LAST:event_btnHomeDelActionPerformed

    private void btnSettle1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSettle1ActionPerformed
        funOpenDirectBiller();
    }//GEN-LAST:event_btnSettle1ActionPerformed

    private void btnDownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDownActionPerformed
        funDownButtonPressed();
    }//GEN-LAST:event_btnDownActionPerformed

    private void lblProductNameMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblProductNameMouseClicked
    {//GEN-HEADEREND:event_lblProductNameMouseClicked
        // TODO add your handling code here:
        objUtility.funMinimizeWindow();
    }//GEN-LAST:event_lblProductNameMouseClicked

    private void lblformNameMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblformNameMouseClicked
    {//GEN-HEADEREND:event_lblformNameMouseClicked
        // TODO add your handling code here:
        objUtility.funMinimizeWindow();
    }//GEN-LAST:event_lblformNameMouseClicked

    private void lblPosNameMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblPosNameMouseClicked
    {//GEN-HEADEREND:event_lblPosNameMouseClicked
        // TODO add your handling code here:
        objUtility.funMinimizeWindow();
    }//GEN-LAST:event_lblPosNameMouseClicked

    private void lblUserCodeMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblUserCodeMouseClicked
    {//GEN-HEADEREND:event_lblUserCodeMouseClicked
        // TODO add your handling code here:
        objUtility.funMinimizeWindow();
    }//GEN-LAST:event_lblUserCodeMouseClicked

    private void lblDateMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblDateMouseClicked
    {//GEN-HEADEREND:event_lblDateMouseClicked
        // TODO add your handling code here:
        objUtility.funMinimizeWindow();
    }//GEN-LAST:event_lblDateMouseClicked

    private void txtShapeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtShapeMouseClicked
        // TODO add your handling code here:

        if (txtShape.getText().length() == 0)
        {
            new frmAlfaNumericKeyBoard(this, true, "1", "Enter Shape").setVisible(true);
            txtShape.setText(clsGlobalVarClass.gKeyboardValue);
        }
        else
        {
            new frmAlfaNumericKeyBoard(this, true, txtShape.getText(), "1", "Enter Shape").setVisible(true);
            txtShape.setText(clsGlobalVarClass.gKeyboardValue);
        }
    }//GEN-LAST:event_txtShapeMouseClicked

    private void txtShapeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtShapeKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtShapeKeyPressed

    private void txtMessageMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtMessageMouseClicked
        // TODO add your handling code here:
        if (txtMessage.getText().length() == 0)
        {
            new frmAlfaNumericKeyBoard(this, true, "1", "Enter Message").setVisible(true);
            txtMessage.setText(clsGlobalVarClass.gKeyboardValue);
        }
        else
        {
            new frmAlfaNumericKeyBoard(this, true, txtMessage.getText(), "1", "Enter Message").setVisible(true);
            txtMessage.setText(clsGlobalVarClass.gKeyboardValue);
        }
    }//GEN-LAST:event_txtMessageMouseClicked

    private void txtMessageKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMessageKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMessageKeyPressed

    private void txtShapeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtShapeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtShapeActionPerformed

    private void tblCharactersticsMasterMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCharactersticsMasterMouseClicked
        // TODO add your handling code here:
        try
        {
            funCharMasterTableClicked();
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_tblCharactersticsMasterMouseClicked

    private void cmbMinuteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbMinuteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbMinuteActionPerformed

    private void btnAttatchImageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAttatchImageActionPerformed
        // TODO add your handling code here:
        funAttatchSpecialSymbol();
    }//GEN-LAST:event_btnAttatchImageActionPerformed

    private void btnApplyTextCharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnApplyTextCharActionPerformed
        // TODO add your handling code here:
        funAddTextCharacteristicsToMap();
    }//GEN-LAST:event_btnApplyTextCharActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
        dispose();
        clsGlobalVarClass.hmActiveForms.remove("Advance Order");
    }//GEN-LAST:event_formWindowClosed

    private void txtCharValueSearchMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtCharValueSearchMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCharValueSearchMouseClicked

    private void txtCharValueSearchFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCharValueSearchFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCharValueSearchFocusGained

    private void txtCharValueSearchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCharValueSearchKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCharValueSearchKeyPressed

    private void txtCharValueSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCharValueSearchKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCharValueSearchKeyReleased

    private void txtCharValueSearchKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCharValueSearchKeyTyped
        // TODO add your handling code here:
        funSetText();
    }//GEN-LAST:event_txtCharValueSearchKeyTyped

    private void btnApplyValueCharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnApplyValueCharActionPerformed
        // TODO add your handling code here:
        if (cmbCharValue.getItemCount() > 0)
        {
            funSetSelectedCharValueToTable();
        }
        else
        {
            JOptionPane.showMessageDialog(null, "Please Select Characteristics for Items!!!");
        }

    }//GEN-LAST:event_btnApplyValueCharActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel IItemGroupPanel;
    private javax.swing.JPanel IItemPanel;
    private javax.swing.JPanel ListAdvOrderPanel;
    private javax.swing.JPanel ModifyAdvOrderPanel;
    public javax.swing.JPanel NewOrderPanel;
    private javax.swing.JPanel NewTab;
    private javax.swing.JPanel NumberPanel;
    private javax.swing.JButton btnApplyTextChar;
    private javax.swing.JButton btnApplyValueChar;
    private javax.swing.JButton btnAttatchImage;
    private javax.swing.JButton btnBrows;
    private javax.swing.JButton btnBrowseSpecialSymbol;
    private javax.swing.JButton btnChangeQty;
    private javax.swing.JButton btnDelItem;
    private javax.swing.JButton btnDone;
    private javax.swing.JButton btnDown;
    private javax.swing.JButton btnExecuteList;
    private javax.swing.JButton btnExecuteModify;
    private javax.swing.JButton btnHome;
    private javax.swing.JButton btnHomeDel;
    private javax.swing.JButton btnIItem1;
    private javax.swing.JButton btnIItem10;
    private javax.swing.JButton btnIItem11;
    private javax.swing.JButton btnIItem12;
    private javax.swing.JButton btnIItem13;
    private javax.swing.JButton btnIItem14;
    private javax.swing.JButton btnIItem15;
    private javax.swing.JButton btnIItem16;
    private javax.swing.JButton btnIItem2;
    private javax.swing.JButton btnIItem3;
    private javax.swing.JButton btnIItem4;
    private javax.swing.JButton btnIItem5;
    private javax.swing.JButton btnIItem6;
    private javax.swing.JButton btnIItem7;
    private javax.swing.JButton btnIItem8;
    private javax.swing.JButton btnIItem9;
    private javax.swing.JButton btnItemSorting1;
    private javax.swing.JButton btnItemSorting2;
    private javax.swing.JButton btnItemSorting3;
    private javax.swing.JButton btnItemSorting4;
    private javax.swing.JButton btnMenu2;
    private javax.swing.JButton btnMenu3;
    private javax.swing.JButton btnMenu4;
    private javax.swing.JButton btnMenu5;
    private javax.swing.JButton btnMenu6;
    private javax.swing.JButton btnMenu7;
    private javax.swing.JButton btnMenu8;
    private javax.swing.JButton btnModifier;
    private javax.swing.JButton btnMultiQty;
    private javax.swing.JButton btnNextItem;
    private javax.swing.JButton btnNextItemSorting;
    private javax.swing.JButton btnNextMenu;
    private javax.swing.JButton btnNumber1;
    private javax.swing.JButton btnNumber2;
    private javax.swing.JButton btnNumber3;
    private javax.swing.JButton btnNumber4;
    private javax.swing.JButton btnNumber5;
    private javax.swing.JButton btnNumber6;
    private javax.swing.JButton btnNumber7;
    private javax.swing.JButton btnNumber8;
    private javax.swing.JButton btnNumber9;
    private javax.swing.JButton btnPlu;
    private javax.swing.JButton btnPopular;
    private javax.swing.JButton btnPrevItem;
    private javax.swing.JButton btnPrevItemSorting;
    private javax.swing.JButton btnPrevMenu;
    private javax.swing.JButton btnSettle1;
    private javax.swing.JButton btnUp;
    private javax.swing.JButton btnWaiterName;
    private javax.swing.JButton bttn_PLU_Panel_Close;
    private javax.swing.JCheckBox chkUrgentOrder;
    private javax.swing.JComboBox cmbAMPM;
    private javax.swing.JComboBox cmbCharValue;
    private javax.swing.JComboBox cmbHour;
    private javax.swing.JComboBox cmbMinute;
    private com.toedter.calendar.JDateChooser dteFrom;
    private com.toedter.calendar.JDateChooser dteFromModify;
    private com.toedter.calendar.JDateChooser dteOrderDate;
    private com.toedter.calendar.JDateChooser dteTo;
    private com.toedter.calendar.JDateChooser dteToModify;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JLabel lblAdvOrderNo;
    private javax.swing.JLabel lblAdvOrderno;
    public static javax.swing.JLabel lblCustInfo;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblHCharCode;
    private javax.swing.JLabel lblHItemCode;
    private javax.swing.JLabel lblHItemName;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblImage;
    private javax.swing.JLabel lblItemName;
    private javax.swing.JLabel lblItemNameForImage;
    private javax.swing.JLabel lblItemlImage;
    private javax.swing.JLabel lblManualAdvOrderNo;
    private javax.swing.JLabel lblMessage;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblNote;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblShape;
    private javax.swing.JLabel lblSymbolImage;
    private javax.swing.JLabel lblTextCharName;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblformName;
    private javax.swing.JPanel panelDetails;
    private javax.swing.JPanel panelFormBody;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelItemDtl;
    private javax.swing.JPanel panelKOTDetails;
    private javax.swing.JPanel panelLayout;
    private javax.swing.JPanel panelListAdvOrder;
    public javax.swing.JPanel panelNavigate;
    private javax.swing.JPanel panelOrderDate;
    private javax.swing.JPanel panelSubGroup;
    private javax.swing.JPanel panel_PLU;
    private javax.swing.JScrollPane scrItemDetials;
    public javax.swing.JTabbedPane tabPaneAdvOrder;
    private javax.swing.JTable tblAdvanceOrderList;
    private javax.swing.JTable tblCharactersticsMaster;
    private javax.swing.JTable tblItemTable;
    private javax.swing.JTable tblModifyAdvanceOrderList;
    private javax.swing.JTable tbl_PLU_Items;
    private javax.swing.JTextField txtCharValueSearch;
    private javax.swing.JTextField txtManualAdvOrderNo;
    private javax.swing.JTextArea txtMessage;
    private javax.swing.JTextArea txtNote;
    private javax.swing.JTextField txtPLU_ItemSearch;
    private javax.swing.JTextField txtShape;
    private javax.swing.JTextField txtTextCharValue;
    private javax.swing.JTextField txtTotal;
    // End of variables declaration//GEN-END:variables

}
