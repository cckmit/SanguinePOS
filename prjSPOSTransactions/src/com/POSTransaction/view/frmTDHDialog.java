/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSTransaction.view;

import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSTransaction.controller.clsDirectBillerItemDtl;
import com.POSTransaction.controller.clsMakeKotItemDtl;
import com.POSGlobal.controller.clsTDHOnItemDtl;
import com.POSGlobal.view.frmOkPopUp;
import java.awt.Color;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.text.html.HTMLDocument;

public class frmTDHDialog extends javax.swing.JDialog
{
    private boolean flagTDHOnComboItem = false,flagDirectBiller = false;
    
    private int intMenuIndex,intItemIndex,nextMenuIndex,freeItem = 0,itemModifierSeqNO = 01,nextCnt,limit,itemNumber;
    private int totalItems,nextItemClick = 0;
    
    private String[] itemNames;
    private String[] selectedGroupName = new String[1];
    private String KOTNO,WaiterNo,TableNO,selectedGroupCode = null,seqNo,tdhOnComboItemCode,selectedMenuName;
    
    private Map<String, String> mapMenus;
    private Map<String, Integer> mapMenuMaxQty;
    private Map<String, Map<String, String>> mapSelectedMenuAndItemList;
    private Map<String, clsModifierGroupDtl> hm_ModifierGroup = null;
    private Map<String, clsModifierDtl> hm_ModifierDtl = null;
    
    private List<String> listGroupName;
    private List<String> listGroupCode;
    private List<String> listOfMenuCode;
    private List<String> listOfMenuName;
    private List<String> listOfItemCode;
    private List<String> listOfItemName;
    private List<clsDirectBillerItemDtl> objListUnselectedDefaultModifiers;
    private List<clsMakeKotItemDtl> objListUnselectedDefaultModifiersForKOT;
    private List<String> selectedModefier = new ArrayList<>();
    private List<clsMakeKotItemDtl> obj_List_KOT_ItemDtl;
    private List<clsDirectBillerItemDtl> obj_List_ItemDtl;
    
    private clsDirectBillerItemDtl objDirectBillerItemDtl;
    private frmMakeKOT objMakeKot;
    private frmDirectBiller objDirectBiller;
    

    /**
     *
     * @param parent
     * @param modal
     * @param itemCode for make kot
     */
    public frmTDHDialog(java.awt.Frame parent, boolean modal, String itemCode, clsMakeKotItemDtl ob)
    {
        super(parent, modal);

        initComponents();
        this.setLocationRelativeTo(null);
        objMakeKot = (frmMakeKOT) parent;
        KOTNO = objMakeKot.KOTNo;
        WaiterNo = objMakeKot.globalWaiterNo;
        TableNO = objMakeKot.globalTableNo;
        obj_List_KOT_ItemDtl = new ArrayList<>();
        seqNo = ob.getSequenceNo();
        obj_List_KOT_ItemDtl.add(ob);
        selectedModefier.clear();
        selectedGroupName[0] = "";

        objListUnselectedDefaultModifiersForKOT = new ArrayList<clsMakeKotItemDtl>();

        String itemName = ob.getItemName();
        lblItemName.setText(itemName);
        fun_ShowModifier(itemCode);

    }

    //for direct biller tdh on modifier
    public frmTDHDialog(frmDirectBiller aThis, boolean b, String itemCode, clsDirectBillerItemDtl obTDHitem)
    {
        super(aThis, b);

        initComponents();
        this.setLocationRelativeTo(null);
        this.objDirectBiller = aThis;
        KOTNO = "";
        WaiterNo = "";
        TableNO = "";
        flagDirectBiller = true;
        obj_List_ItemDtl = new ArrayList<>();
        obj_List_ItemDtl.add(obTDHitem);
        selectedModefier.clear();
        selectedGroupName[0] = "";
        String itemName = obTDHitem.getItemName();
        lblItemName.setText(itemName);

        objDirectBillerItemDtl = obTDHitem;
        objListUnselectedDefaultModifiers = new ArrayList<clsDirectBillerItemDtl>();

        fun_ShowModifier(itemCode);
        try
        {
            String sql_FreeItem = "select intMaxQuantity from tbltdhhd where strItemCode='" + itemCode + "'  and strComboItemYN='N'";
            ResultSet rsFreeItemDtl = clsGlobalVarClass.dbMysql.executeResultSet(sql_FreeItem);
            if (rsFreeItemDtl.next())
            {
                freeItem = rsFreeItemDtl.getInt("intMaxQuantity");
            }
            rsFreeItemDtl.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    //for direct biller tdh on item(comboItem)
    public frmTDHDialog(frmDirectBiller aThis, boolean b, String itemCode, String itemName, Map mapComboItemDtl)
    {

        super(aThis, b);
        try
        {

            initComponents();
            this.setLocationRelativeTo(null);
            this.flagTDHOnComboItem = true;
            this.objDirectBiller = aThis;
            this.flagDirectBiller = true;
            this.obj_List_ItemDtl = new ArrayList<>();
            this.tdhOnComboItemCode = itemCode;
            this.lblItemName.setText(itemName);
            this.objListUnselectedDefaultModifiers = new ArrayList<>();
            this.mapMenus = new HashMap<>();
            this.listOfMenuCode = new ArrayList<>();
            this.listOfMenuName = new ArrayList<>();
            this.listOfItemCode = new ArrayList<>();
            this.listOfItemName = new ArrayList<>();
            this.mapMenuMaxQty = new HashMap();
            this.mapSelectedMenuAndItemList = new HashMap<>();
            funResetItemButtons();
            reset_TopSortingButtons();
            btnSkipGroup.setEnabled(false);
            intMenuIndex = 0;
            intItemIndex = 0;
            nextMenuIndex = 0;
            funLoadMenuNames(tdhOnComboItemCode);
            if (listOfMenuCode.size() > 0)
            {
                funFillMenuNames();
                selectedMenuName = lblgroup1.getText();
                funLoadMenuItems();
                if (listOfMenuCode.size() > 1)
                {
                    btnSkipGroup.setEnabled(true);
                }
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    //for make kot tdh on item(comboItem)
    public frmTDHDialog(frmMakeKOT aThis, boolean b, String itemCode, String itemName, Map mapComboItemDtl)
    {

        super(aThis, b);
        try
        {

            initComponents();
            this.setLocationRelativeTo(null);
            this.flagTDHOnComboItem = true;
            this.objMakeKot = aThis;
            this.flagDirectBiller = false;
            this.obj_List_ItemDtl = new ArrayList<>();
            this.tdhOnComboItemCode = itemCode;
            this.lblItemName.setText(itemName);
            this.objListUnselectedDefaultModifiers = new ArrayList<>();
            this.mapMenus = new HashMap<>();
            this.listOfMenuCode = new ArrayList<>();
            this.listOfMenuName = new ArrayList<>();
            this.listOfItemCode = new ArrayList<>();
            this.listOfItemName = new ArrayList<>();
            this.mapMenuMaxQty = new HashMap();
            this.mapSelectedMenuAndItemList = new HashMap<>();
            funResetItemButtons();
            reset_TopSortingButtons();
            btnSkipGroup.setEnabled(false);
            intMenuIndex = 0;
            intItemIndex = 0;
            nextMenuIndex = 0;
            funLoadMenuNames(tdhOnComboItemCode);
            if (listOfMenuCode.size() > 0)
            {
                funFillMenuNames();
                selectedMenuName = lblgroup1.getText();
                funLoadMenuItems();
                if (listOfMenuCode.size() > 1)
                {
                    btnSkipGroup.setEnabled(true);
                }
            }

        }
        catch (Exception e)
        {
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

        panelModifierGroup = new javax.swing.JPanel();
        lblgroup1 = new javax.swing.JLabel();
        lblgroup2 = new javax.swing.JLabel();
        lblgroup3 = new javax.swing.JLabel();
        lblgroup4 = new javax.swing.JLabel();
        lblgroupNext = new javax.swing.JLabel();
        lblgroupPrev = new javax.swing.JLabel();
        panleModifier = new javax.swing.JPanel();
        btnModi1 = new javax.swing.JButton();
        btnModi2 = new javax.swing.JButton();
        btnModi3 = new javax.swing.JButton();
        btnModi4 = new javax.swing.JButton();
        btnModi5 = new javax.swing.JButton();
        btnModi9 = new javax.swing.JButton();
        btnModi13 = new javax.swing.JButton();
        btnModi6 = new javax.swing.JButton();
        btnModi10 = new javax.swing.JButton();
        btnModi14 = new javax.swing.JButton();
        btnModi7 = new javax.swing.JButton();
        btnModi11 = new javax.swing.JButton();
        btnModi15 = new javax.swing.JButton();
        btnModi8 = new javax.swing.JButton();
        btnModi12 = new javax.swing.JButton();
        btnModi16 = new javax.swing.JButton();
        lblDefaultModifierHint = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lblMinLimit = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        lblMaxLimit = new javax.swing.JLabel();
        lblDefaultModifier = new javax.swing.JLabel();
        lblHint1 = new javax.swing.JLabel();
        panelItemDtl = new javax.swing.JPanel();
        lblItemName = new javax.swing.JLabel();
        panelOperationalButtons = new javax.swing.JPanel();
        btnPrevModifier = new javax.swing.JButton();
        btnSkipGroup = new javax.swing.JButton();
        btnNextModifier = new javax.swing.JButton();
        btnDoneTDh = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(800, 600));
        setUndecorated(true);
        setResizable(false);
        getContentPane().setLayout(null);

        panelModifierGroup.setBackground(new java.awt.Color(216, 238, 254));
        panelModifierGroup.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        panelModifierGroup.setLayout(null);

        lblgroup1.setBackground(new java.awt.Color(36, 143, 230));
        lblgroup1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblgroup1.setForeground(new java.awt.Color(255, 255, 255));
        lblgroup1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblgroup1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblgroup1.setOpaque(true);
        lblgroup1.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                lblgroup1MouseClicked(evt);
            }
        });
        panelModifierGroup.add(lblgroup1);
        lblgroup1.setBounds(75, 10, 155, 40);

        lblgroup2.setBackground(new java.awt.Color(113, 180, 250));
        lblgroup2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblgroup2.setForeground(new java.awt.Color(255, 255, 255));
        lblgroup2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblgroup2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblgroup2.setOpaque(true);
        lblgroup2.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                lblgroup2MouseClicked(evt);
            }
        });
        panelModifierGroup.add(lblgroup2);
        lblgroup2.setBounds(240, 10, 155, 40);

        lblgroup3.setBackground(new java.awt.Color(113, 180, 250));
        lblgroup3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblgroup3.setForeground(new java.awt.Color(255, 255, 255));
        lblgroup3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblgroup3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblgroup3.setOpaque(true);
        lblgroup3.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                lblgroup3MouseClicked(evt);
            }
        });
        panelModifierGroup.add(lblgroup3);
        lblgroup3.setBounds(405, 10, 155, 40);

        lblgroup4.setBackground(new java.awt.Color(113, 180, 250));
        lblgroup4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblgroup4.setForeground(new java.awt.Color(255, 255, 255));
        lblgroup4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblgroup4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblgroup4.setOpaque(true);
        lblgroup4.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                lblgroup4MouseClicked(evt);
            }
        });
        panelModifierGroup.add(lblgroup4);
        lblgroup4.setBounds(570, 10, 155, 40);

        lblgroupNext.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblgroupNext.setForeground(new java.awt.Color(255, 255, 255));
        lblgroupNext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgBackBtn1.png"))); // NOI18N
        lblgroupNext.setText(">>");
        lblgroupNext.setFocusable(false);
        lblgroupNext.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblgroupNext.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                lblgroupNextMouseClicked(evt);
            }
        });
        panelModifierGroup.add(lblgroupNext);
        lblgroupNext.setBounds(730, 10, 60, 40);

        lblgroupPrev.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblgroupPrev.setForeground(new java.awt.Color(255, 255, 255));
        lblgroupPrev.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgBackBtn1.png"))); // NOI18N
        lblgroupPrev.setText("<<");
        lblgroupPrev.setAlignmentY(0.0F);
        lblgroupPrev.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblgroupPrev.setIconTextGap(1);
        lblgroupPrev.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                lblgroupPrevMouseClicked(evt);
            }
        });
        panelModifierGroup.add(lblgroupPrev);
        lblgroupPrev.setBounds(10, 10, 60, 40);

        getContentPane().add(panelModifierGroup);
        panelModifierGroup.setBounds(0, 60, 800, 60);

        panleModifier.setBackground(new java.awt.Color(205, 233, 254));
        panleModifier.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 255), new java.awt.Color(204, 204, 255), new java.awt.Color(153, 204, 255), new java.awt.Color(153, 204, 255)));
        panleModifier.setLayout(null);

        btnModi1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnModi1.setText("1");
        btnModi1.setMargin(new java.awt.Insets(2, 10, 2, 10));
        btnModi1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnModi1ActionPerformed(evt);
            }
        });
        panleModifier.add(btnModi1);
        btnModi1.setBounds(10, 10, 170, 74);

        btnModi2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnModi2.setText("2");
        btnModi2.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnModi2ActionPerformed(evt);
            }
        });
        panleModifier.add(btnModi2);
        btnModi2.setBounds(210, 10, 170, 74);

        btnModi3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnModi3.setText("3");
        btnModi3.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnModi3ActionPerformed(evt);
            }
        });
        panleModifier.add(btnModi3);
        btnModi3.setBounds(410, 10, 170, 74);

        btnModi4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnModi4.setText("4");
        btnModi4.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnModi4ActionPerformed(evt);
            }
        });
        panleModifier.add(btnModi4);
        btnModi4.setBounds(610, 10, 170, 74);

        btnModi5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnModi5.setText("5");
        btnModi5.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnModi5ActionPerformed(evt);
            }
        });
        panleModifier.add(btnModi5);
        btnModi5.setBounds(10, 90, 170, 74);

        btnModi9.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnModi9.setText("9");
        btnModi9.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnModi9ActionPerformed(evt);
            }
        });
        panleModifier.add(btnModi9);
        btnModi9.setBounds(10, 170, 170, 74);

        btnModi13.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnModi13.setText("13");
        btnModi13.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnModi13ActionPerformed(evt);
            }
        });
        panleModifier.add(btnModi13);
        btnModi13.setBounds(10, 250, 170, 74);

        btnModi6.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnModi6.setText("6");
        btnModi6.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnModi6ActionPerformed(evt);
            }
        });
        panleModifier.add(btnModi6);
        btnModi6.setBounds(210, 90, 170, 74);

        btnModi10.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnModi10.setText("10");
        btnModi10.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnModi10ActionPerformed(evt);
            }
        });
        panleModifier.add(btnModi10);
        btnModi10.setBounds(210, 170, 170, 74);

        btnModi14.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnModi14.setText("14");
        btnModi14.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnModi14ActionPerformed(evt);
            }
        });
        panleModifier.add(btnModi14);
        btnModi14.setBounds(210, 250, 170, 74);

        btnModi7.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnModi7.setText("7");
        btnModi7.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnModi7ActionPerformed(evt);
            }
        });
        panleModifier.add(btnModi7);
        btnModi7.setBounds(410, 90, 170, 74);

        btnModi11.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnModi11.setText("11");
        btnModi11.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnModi11ActionPerformed(evt);
            }
        });
        panleModifier.add(btnModi11);
        btnModi11.setBounds(410, 170, 170, 74);

        btnModi15.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnModi15.setText("15");
        btnModi15.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnModi15ActionPerformed(evt);
            }
        });
        panleModifier.add(btnModi15);
        btnModi15.setBounds(410, 250, 170, 74);

        btnModi8.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnModi8.setText("8");
        btnModi8.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnModi8ActionPerformed(evt);
            }
        });
        panleModifier.add(btnModi8);
        btnModi8.setBounds(610, 90, 170, 74);

        btnModi12.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnModi12.setText("12");
        btnModi12.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnModi12ActionPerformed(evt);
            }
        });
        panleModifier.add(btnModi12);
        btnModi12.setBounds(610, 170, 170, 74);

        btnModi16.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnModi16.setText("16");
        btnModi16.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnModi16ActionPerformed(evt);
            }
        });
        panleModifier.add(btnModi16);
        btnModi16.setBounds(610, 250, 170, 74);

        lblDefaultModifierHint.setBackground(new java.awt.Color(255, 105, 180));
        lblDefaultModifierHint.setOpaque(true);
        panleModifier.add(lblDefaultModifierHint);
        lblDefaultModifierHint.setBounds(240, 350, 20, 20);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setText("SELECTED ITEMS");
        panleModifier.add(jLabel1);
        jLabel1.setBounds(10, 350, 100, 20);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel2.setText("MIN. LIMIT  :");
        panleModifier.add(jLabel2);
        jLabel2.setBounds(380, 350, 70, 20);
        panleModifier.add(lblMinLimit);
        lblMinLimit.setBounds(470, 350, 110, 20);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel4.setText("MAX. LIMIT  :");
        panleModifier.add(jLabel4);
        jLabel4.setBounds(580, 350, 80, 20);
        panleModifier.add(lblMaxLimit);
        lblMaxLimit.setBounds(660, 350, 110, 20);

        lblDefaultModifier.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblDefaultModifier.setText("Default Modifier");
        panleModifier.add(lblDefaultModifier);
        lblDefaultModifier.setBounds(140, 350, 90, 20);

        lblHint1.setBackground(new java.awt.Color(255, 204, 204));
        lblHint1.setOpaque(true);
        panleModifier.add(lblHint1);
        lblHint1.setBounds(110, 350, 20, 20);

        getContentPane().add(panleModifier);
        panleModifier.setBounds(0, 120, 800, 380);

        panelItemDtl.setBackground(new java.awt.Color(205, 233, 254));
        panelItemDtl.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Item Name", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N
        panelItemDtl.setLayout(null);

        lblItemName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        panelItemDtl.add(lblItemName);
        lblItemName.setBounds(70, 20, 560, 30);

        getContentPane().add(panelItemDtl);
        panelItemDtl.setBounds(0, 0, 800, 60);

        panelOperationalButtons.setBackground(new java.awt.Color(216, 238, 254));
        panelOperationalButtons.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        panelOperationalButtons.setLayout(null);

        btnPrevModifier.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnPrevModifier.setForeground(new java.awt.Color(255, 255, 255));
        btnPrevModifier.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnPrevModifier.setText("<<<");
        btnPrevModifier.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPrevModifier.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnPrevModifier.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnPrevModifierMouseClicked(evt);
            }
        });
        panelOperationalButtons.add(btnPrevModifier);
        btnPrevModifier.setBounds(90, 30, 110, 40);

        btnSkipGroup.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnSkipGroup.setForeground(new java.awt.Color(255, 255, 255));
        btnSkipGroup.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnSkipGroup.setText("NEXT");
        btnSkipGroup.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSkipGroup.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnSkipGroup.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnSkipGroupActionPerformed(evt);
            }
        });
        panelOperationalButtons.add(btnSkipGroup);
        btnSkipGroup.setBounds(400, 30, 110, 40);

        btnNextModifier.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnNextModifier.setForeground(new java.awt.Color(255, 255, 255));
        btnNextModifier.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnNextModifier.setText(">>>");
        btnNextModifier.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNextModifier.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnNextModifier.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnNextModifierMouseClicked(evt);
            }
        });
        panelOperationalButtons.add(btnNextModifier);
        btnNextModifier.setBounds(220, 30, 110, 40);

        btnDoneTDh.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnDoneTDh.setForeground(new java.awt.Color(255, 255, 255));
        btnDoneTDh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnDoneTDh.setText("DONE");
        btnDoneTDh.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDoneTDh.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnDoneTDh.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnDoneTDhActionPerformed(evt);
            }
        });
        panelOperationalButtons.add(btnDoneTDh);
        btnDoneTDh.setBounds(540, 30, 110, 40);

        btnClose.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnClose.setForeground(new java.awt.Color(255, 255, 255));
        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnClose.setText("CLOSE");
        btnClose.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnClose.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnClose.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCloseMouseClicked(evt);
            }
        });
        panelOperationalButtons.add(btnClose);
        btnClose.setBounds(670, 30, 110, 40);

        getContentPane().add(panelOperationalButtons);
        panelOperationalButtons.setBounds(0, 500, 800, 80);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCloseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCloseMouseClicked
        dispose();
    }//GEN-LAST:event_btnCloseMouseClicked

    private void btnNextModifierMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNextModifierMouseClicked
        if (btnNextModifier.isEnabled())
        {
            if (flagTDHOnComboItem)
            {
                intItemIndex++;
                funNextPrevItemButtonClicked();
            }
            else
            {
                fun_NextModifierClick();
            }
        }
    }//GEN-LAST:event_btnNextModifierMouseClicked

    private void btnPrevModifierMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPrevModifierMouseClicked
        if (btnPrevModifier.isEnabled())
        {
            if (flagTDHOnComboItem)
            {
                intItemIndex--;
                funNextPrevItemButtonClicked();
            }
            else
            {
                fun_PrevModifierClick();
            }
        }

    }//GEN-LAST:event_btnPrevModifierMouseClicked

    private void lblgroup1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblgroup1MouseClicked
        if (lblgroup1.isEnabled() && lblgroup1.getText().length() > 0)
        {
            if (flagTDHOnComboItem)
            {
                nextMenuIndex = 0;
                if (!(nextMenuIndex + 1 < listOfMenuCode.size()))
                {
                    btnSkipGroup.setEnabled(false);
                }
                else
                {
                    btnSkipGroup.setEnabled(true);
                }
                selectedMenuName = lblgroup1.getText();
                funLoadMenuItems();
            }
            else
            {
                funFillModifier(lblgroup1.getText());
                btnSkipGroup.setEnabled(true);
            }
        }
    }//GEN-LAST:event_lblgroup1MouseClicked

    private void lblgroup2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblgroup2MouseClicked
        if (lblgroup2.isEnabled() && lblgroup2.getText().length() > 0)
        {
            if (flagTDHOnComboItem)
            {
                nextMenuIndex = 1;
                if (!(nextMenuIndex + 1 < listOfMenuCode.size()))
                {
                    btnSkipGroup.setEnabled(false);
                }
                 else
                {
                    btnSkipGroup.setEnabled(true);
                }
                selectedMenuName = lblgroup2.getText();
                funLoadMenuItems();
            }
            else
            {
                if (fun_calculateMinMaxLevel())
                {
                    funFillModifier(lblgroup2.getText());
                    btnSkipGroup.setEnabled(true);
                }
            }
        }
    }//GEN-LAST:event_lblgroup2MouseClicked

    private void lblgroup3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblgroup3MouseClicked
        if (lblgroup3.isEnabled() && lblgroup3.getText().length() > 0)
        {
            if (flagTDHOnComboItem)
            {
                nextMenuIndex = 2;
                if (!(nextMenuIndex + 1 < listOfMenuCode.size()))
                {
                    btnSkipGroup.setEnabled(false);
                }
                 else
                {
                    btnSkipGroup.setEnabled(true);
                }
                selectedMenuName = lblgroup3.getText();
                funLoadMenuItems();
            }
            else
            {
                if (fun_calculateMinMaxLevel())
                {
                    funFillModifier(lblgroup3.getText());
                    btnSkipGroup.setEnabled(false);
                }
            }
        }
    }//GEN-LAST:event_lblgroup3MouseClicked

    private void lblgroup4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblgroup4MouseClicked
        if (lblgroup4.isEnabled() && lblgroup1.getText().length() > 0)
        {
            if (flagTDHOnComboItem)
            {
                nextMenuIndex = 3;
                if (!(nextMenuIndex + 1 < listOfMenuCode.size()))
                {
                    btnSkipGroup.setEnabled(false);
                }
                 else
                {
                    btnSkipGroup.setEnabled(true);
                }
                selectedMenuName = lblgroup4.getText();
                funLoadMenuItems();
            }
            else
            {
                if (fun_calculateMinMaxLevel())
                {
                    funFillModifier(lblgroup4.getText());
                }
            }
        }
    }//GEN-LAST:event_lblgroup4MouseClicked

    private void lblgroupNextMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblgroupNextMouseClicked
        if (lblgroupNext.isEnabled())
        {
            if (flagTDHOnComboItem)
            {
                intMenuIndex++;
                funResetItemButtons();
                reset_TopSortingButtons();
                lblgroupPrev.setEnabled(true);
                funFillMenuAndItems();
            }
            else
            {
                funNextGroupClick();
            }
        }
    }//GEN-LAST:event_lblgroupNextMouseClicked

    private void lblgroupPrevMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblgroupPrevMouseClicked
        if (lblgroupPrev.isEnabled())
        {
            if (flagTDHOnComboItem)
            {
                intMenuIndex--;
                funResetItemButtons();
                reset_TopSortingButtons();
                lblgroupNext.setEnabled(true);
                funFillMenuAndItems();

            }
            else
            {
                funPrevGroupClick();
            }
        }
    }//GEN-LAST:event_lblgroupPrevMouseClicked

    private void btnModi1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModi1ActionPerformed
        if (flagTDHOnComboItem)
        {
            funItemClicked(btnModi1, 0 + (intItemIndex * 16));
        }
        else
        {
            if (flagDirectBiller)
            {
                fun_Add_Modifier_To_DirectBiller(btnModi1.getText(), btnModi1);
            }
            else
            {
                funAddModifierToKOT(btnModi1.getText(), btnModi1);
            }
        }


    }//GEN-LAST:event_btnModi1ActionPerformed

    private void btnModi2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModi2ActionPerformed
        if (flagTDHOnComboItem)
        {
            funItemClicked(btnModi2, 1 + (intItemIndex * 16));
        }
        else
        {
            if (flagDirectBiller)
            {
                fun_Add_Modifier_To_DirectBiller(btnModi2.getText(), btnModi2);

            }
            else
            {
                funAddModifierToKOT(btnModi2.getText(), btnModi2);
            }
        }


    }//GEN-LAST:event_btnModi2ActionPerformed

    private void btnModi3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModi3ActionPerformed
        if (flagTDHOnComboItem)
        {
            funItemClicked(btnModi3, 2 + (intItemIndex * 16));
        }
        else
        {
            if (flagDirectBiller)
            {
                fun_Add_Modifier_To_DirectBiller(btnModi3.getText(), btnModi3);
            }
            else
            {

                funAddModifierToKOT(btnModi3.getText(), btnModi3);
            }
        }


    }//GEN-LAST:event_btnModi3ActionPerformed

    private void btnModi4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModi4ActionPerformed
        if (flagTDHOnComboItem)
        {
            funItemClicked(btnModi4, 3 + (intItemIndex * 16));
        }
        else
        {
            if (flagDirectBiller)
            {
                fun_Add_Modifier_To_DirectBiller(btnModi4.getText(), btnModi4);
            }
            else
            {

                funAddModifierToKOT(btnModi4.getText(), btnModi4);
            }
        }


    }//GEN-LAST:event_btnModi4ActionPerformed

    private void btnModi5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModi5ActionPerformed
        if (flagTDHOnComboItem)
        {
            funItemClicked(btnModi5, 4 + (intItemIndex * 16));
        }
        else
        {
            if (flagDirectBiller)
            {
                fun_Add_Modifier_To_DirectBiller(btnModi5.getText(), btnModi5);
            }
            else
            {
                funAddModifierToKOT(btnModi5.getText(), btnModi5);
            }
        }

    }//GEN-LAST:event_btnModi5ActionPerformed

    private void btnModi6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModi6ActionPerformed
        if (flagTDHOnComboItem)
        {
            funItemClicked(btnModi6, 5 + (intItemIndex * 16));
        }
        else
        {
            if (flagDirectBiller)
            {
                fun_Add_Modifier_To_DirectBiller(btnModi6.getText(), btnModi6);
            }
            else
            {

                funAddModifierToKOT(btnModi6.getText(), btnModi6);
            }
        }

    }//GEN-LAST:event_btnModi6ActionPerformed

    private void btnModi7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModi7ActionPerformed
        if (flagTDHOnComboItem)
        {
            funItemClicked(btnModi7, 6 + (intItemIndex * 16));
        }
        else
        {
            if (flagDirectBiller)
            {
                fun_Add_Modifier_To_DirectBiller(btnModi7.getText(), btnModi7);
            }
            else
            {

                funAddModifierToKOT(btnModi7.getText(), btnModi7);
            }
        }
    }//GEN-LAST:event_btnModi7ActionPerformed

    private void btnModi8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModi8ActionPerformed
        if (flagTDHOnComboItem)
        {
            funItemClicked(btnModi8, 7 + (intItemIndex * 16));
        }
        else
        {
            if (flagDirectBiller)
            {
                fun_Add_Modifier_To_DirectBiller(btnModi8.getText(), btnModi8);
            }
            else
            {

                funAddModifierToKOT(btnModi8.getText(), btnModi8);
            }
        }


    }//GEN-LAST:event_btnModi8ActionPerformed

    private void btnModi9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModi9ActionPerformed
        if (flagTDHOnComboItem)
        {
            funItemClicked(btnModi9, 8 + (intItemIndex * 16));
        }
        else
        {
            if (flagDirectBiller)
            {
                fun_Add_Modifier_To_DirectBiller(btnModi9.getText(), btnModi9);
            }
            else
            {

                funAddModifierToKOT(btnModi9.getText(), btnModi9);
            }
        }


    }//GEN-LAST:event_btnModi9ActionPerformed

    private void btnModi10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModi10ActionPerformed
        if (flagTDHOnComboItem)
        {
            funItemClicked(btnModi10, 9 + (intItemIndex * 16));
        }
        else
        {
            if (flagDirectBiller)
            {
                fun_Add_Modifier_To_DirectBiller(btnModi10.getText(), btnModi10);
            }
            else
            {
                funAddModifierToKOT(btnModi10.getText(), btnModi10);
            }
        }


    }//GEN-LAST:event_btnModi10ActionPerformed

    private void btnModi11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModi11ActionPerformed
        if (flagTDHOnComboItem)
        {
            funItemClicked(btnModi11, 10 + (intItemIndex * 16));
        }
        else
        {
            if (flagDirectBiller)
            {
                fun_Add_Modifier_To_DirectBiller(btnModi11.getText(), btnModi11);
            }
            else
            {
                funAddModifierToKOT(btnModi11.getText(), btnModi11);
            }
        }
    }//GEN-LAST:event_btnModi11ActionPerformed

    private void btnModi12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModi12ActionPerformed
        if (flagTDHOnComboItem)
        {
            funItemClicked(btnModi12, 11 + (intItemIndex * 16));
        }
        else
        {
            if (flagDirectBiller)
            {
                fun_Add_Modifier_To_DirectBiller(btnModi12.getText(), btnModi12);
            }
            else
            {
                funAddModifierToKOT(btnModi12.getText(), btnModi12);
            }
        }
    }//GEN-LAST:event_btnModi12ActionPerformed

    private void btnModi13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModi13ActionPerformed
        if (flagTDHOnComboItem)
        {
            funItemClicked(btnModi13, 12 + (intItemIndex * 16));
        }
        else
        {
            if (flagDirectBiller)
            {
                fun_Add_Modifier_To_DirectBiller(btnModi13.getText(), btnModi13);
            }
            else
            {
                funAddModifierToKOT(btnModi13.getText(), btnModi13);

            }
        }
    }//GEN-LAST:event_btnModi13ActionPerformed

    private void btnModi14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModi14ActionPerformed
        if (flagTDHOnComboItem)
        {
            funItemClicked(btnModi14, 13 + (intItemIndex * 16));
        }
        else
        {
            if (flagDirectBiller)
            {
                fun_Add_Modifier_To_DirectBiller(btnModi14.getText(), btnModi14);
            }
            else
            {

                funAddModifierToKOT(btnModi14.getText(), btnModi14);
            }
        }
    }//GEN-LAST:event_btnModi14ActionPerformed

    private void btnModi15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModi15ActionPerformed
        if (flagTDHOnComboItem)
        {
            funItemClicked(btnModi15, 14 + (intItemIndex * 16));
        }
        else
        {
            if (flagDirectBiller)
            {
                fun_Add_Modifier_To_DirectBiller(btnModi15.getText(), btnModi15);
            }
            else
            {
                funAddModifierToKOT(btnModi15.getText(), btnModi15);
            }
        }
    }//GEN-LAST:event_btnModi15ActionPerformed

    private void btnModi16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModi16ActionPerformed
        if (flagTDHOnComboItem)
        {
            funItemClicked(btnModi16, 15 + (intItemIndex * 16));
        }
        else
        {
            if (flagDirectBiller)
            {
                fun_Add_Modifier_To_DirectBiller(btnModi16.getText(), btnModi16);

            }
            else
            {
                funAddModifierToKOT(btnModi16.getText(), btnModi16);
            }
        }
    }//GEN-LAST:event_btnModi16ActionPerformed

    private void btnSkipGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSkipGroupActionPerformed

        if (btnSkipGroup.isEnabled())
        {
            if (flagTDHOnComboItem)
            {
                nextMenuIndex++;
                if (nextMenuIndex < listOfMenuCode.size())
                {
                    selectedMenuName = listOfMenuName.get(nextMenuIndex);
                    funLoadMenuItems();
                    if (!(nextMenuIndex + 1 < listOfMenuCode.size()))
                    {
                        btnSkipGroup.setEnabled(false);
                    }
                }
                else
                {
                    btnSkipGroup.setEnabled(false);
                }
            }
            else
            {
                if (fun_calculateMinMaxLevel())
                {
                    funSkipSelectedGroup();
                }
            }
        }
    }//GEN-LAST:event_btnSkipGroupActionPerformed

    private void btnDoneTDhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDoneTDhActionPerformed
        if (flagTDHOnComboItem)
        {
            Iterator<Map.Entry<String, Map<String, String>>> itMenu = mapSelectedMenuAndItemList.entrySet().iterator();
            while (itMenu.hasNext())
            {
                Map.Entry<String, Map<String, String>> menuEntry = itMenu.next();
                String menuCode = menuEntry.getKey();
                Iterator<Map.Entry<String, String>> itItems = menuEntry.getValue().entrySet().iterator();
                while (itItems.hasNext())
                {
                    Map.Entry<String, String> entry = itItems.next();
                    String itemCode = entry.getKey();
                    String itemName = entry.getValue();
                    if(flagDirectBiller)
                    {
                        objDirectBiller.funAddSubItems(itemCode, itemName, mapMenuMaxQty.get(menuCode), tdhOnComboItemCode);
                    }
                    else
                    {
                        objMakeKot.funAddSubItems(itemCode, itemName, mapMenuMaxQty.get(menuCode), tdhOnComboItemCode);
                    }
                }
            }
            dispose();
            objDirectBiller = null;
            objMakeKot=null;
            System.gc();
        }
        else
        {
            fun_Done_Selection();
        }

    }//GEN-LAST:event_btnDoneTDhActionPerformed

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
            java.util.logging.Logger.getLogger(frmTDHDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(frmTDHDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(frmTDHDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(frmTDHDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                frmTDHDialog dialog = new frmTDHDialog(new javax.swing.JFrame(), true, "", null);
                dialog.addWindowListener(new java.awt.event.WindowAdapter()
                {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e)
                    {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnDoneTDh;
    private javax.swing.JButton btnModi1;
    private javax.swing.JButton btnModi10;
    private javax.swing.JButton btnModi11;
    private javax.swing.JButton btnModi12;
    private javax.swing.JButton btnModi13;
    private javax.swing.JButton btnModi14;
    private javax.swing.JButton btnModi15;
    private javax.swing.JButton btnModi16;
    private javax.swing.JButton btnModi2;
    private javax.swing.JButton btnModi3;
    private javax.swing.JButton btnModi4;
    private javax.swing.JButton btnModi5;
    private javax.swing.JButton btnModi6;
    private javax.swing.JButton btnModi7;
    private javax.swing.JButton btnModi8;
    private javax.swing.JButton btnModi9;
    private javax.swing.JButton btnNextModifier;
    private javax.swing.JButton btnPrevModifier;
    private javax.swing.JButton btnSkipGroup;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel lblDefaultModifier;
    private javax.swing.JLabel lblDefaultModifierHint;
    private javax.swing.JLabel lblHint1;
    private javax.swing.JLabel lblItemName;
    private javax.swing.JLabel lblMaxLimit;
    private javax.swing.JLabel lblMinLimit;
    private javax.swing.JLabel lblgroup1;
    private javax.swing.JLabel lblgroup2;
    private javax.swing.JLabel lblgroup3;
    private javax.swing.JLabel lblgroup4;
    private javax.swing.JLabel lblgroupNext;
    private javax.swing.JLabel lblgroupPrev;
    private javax.swing.JPanel panelItemDtl;
    private javax.swing.JPanel panelModifierGroup;
    private javax.swing.JPanel panelOperationalButtons;
    private javax.swing.JPanel panleModifier;
    // End of variables declaration//GEN-END:variables

    /**
     * Ritesh 10 Nov 2014
     *
     * @param itemCode
     */
    private void fun_ShowModifier(String itemCode)
    {
        funResetItemButtons();
        reset_TopSortingButtons();
        hm_ModifierGroup = fun_fill_Top_Sorting_Buttons_for_Modifier(itemCode);
        fun_Asign_ModifierGroup_TopSortingButtons((HashMap<String, clsModifierGroupDtl>) hm_ModifierGroup);
        if (hm_ModifierGroup.isEmpty())
        {
            hm_ModifierDtl = fun_Get_Modifier_All(itemCode);
            fun_setModifierName_to_Buttons((HashMap<String, clsModifierDtl>) hm_ModifierDtl);
        }
        else
        {
            funFillModifier(lblgroup1.getText());
            selectedGroupName[0] = lblgroup1.getText();
            funSetColorToLabel(lblgroup1);
        }

    }

    private void funResetItemButtons()
    {
        JButton[] btnModifierArray =
        {
            btnModi1, btnModi2, btnModi3, btnModi4, btnModi5, btnModi6, btnModi7, btnModi8, btnModi9, btnModi10, btnModi11, btnModi12, btnModi13, btnModi14, btnModi15, btnModi16
        };
        for (int k = 0; k < 16; k++)
        {
            btnModifierArray[k].setText("");
            btnModifierArray[k].setBackground(Color.LIGHT_GRAY);
            btnModifierArray[k].setIcon(null);
            btnModifierArray[k].setEnabled(false);
            btnModifierArray[k].setVisible(false);
        }
        btnPrevModifier.setEnabled(false);
        btnNextModifier.setEnabled(false);
    }

    private void reset_TopSortingButtons()
    {
        lblgroup1.setEnabled(false);
        lblgroup2.setEnabled(false);
        lblgroup3.setEnabled(false);
        lblgroup4.setEnabled(false);
        lblgroup1.setBackground(new java.awt.Color(240, 240, 240));
        lblgroup2.setBackground(new java.awt.Color(240, 240, 240));
        lblgroup3.setBackground(new java.awt.Color(240, 240, 240));
        lblgroup4.setBackground(new java.awt.Color(240, 240, 240));
        lblgroupPrev.setEnabled(false);
        lblgroupNext.setEnabled(false);
        //[36,143,230]0   [240,240,240]
        lblgroup1.setText("");
        lblgroup2.setText("");
        lblgroup3.setText("");
        lblgroup4.setText("");
    }

    /**
     * Ritesh 08 Nov 2014
     *
     * @param itemCode
     * @return
     */
    private HashMap<String, clsModifierGroupDtl> fun_fill_Top_Sorting_Buttons_for_Modifier(String itemCode)
    {
        HashMap<String, clsModifierGroupDtl> hm_ModifierGroupDetail = null;
        try
        {
            String modifierGroupName = null;
            hm_ModifierGroupDetail = new HashMap<>();
            listGroupCode = new ArrayList<>();
            listGroupName = new ArrayList<>();
            String sql_select = "select a.strModifierGroupCode,a.strModifierGroupShortName,a.strApplyMaxItemLimit,"
                    + "a.intItemMaxLimit,a.strApplyMinItemLimit,a.intItemMinLimit  from tblmodifiergrouphd a,tblmodifiermaster b,tblitemmodofier c "
                    + "where a.strOperational='YES' and a.strModifierGroupCode=b.strModifierGroupCode and "
                    + "b.strModifierCode=c.strModifierCode and c.strItemCode='" + itemCode + "' group by a.strModifierGroupCode order by intSequenceNo;";
            ResultSet rs_ModifierGroupDtl = clsGlobalVarClass.dbMysql.executeResultSet(sql_select);
            while (rs_ModifierGroupDtl.next())
            {
                listGroupCode.add(rs_ModifierGroupDtl.getString(1));
                listGroupName.add(rs_ModifierGroupDtl.getString(2));
                modifierGroupName = rs_ModifierGroupDtl.getString(2);
                clsModifierGroupDtl obj = new clsModifierGroupDtl(rs_ModifierGroupDtl.getString(1), rs_ModifierGroupDtl.getString(2), rs_ModifierGroupDtl.getString(3), rs_ModifierGroupDtl.getInt(4), itemCode, rs_ModifierGroupDtl.getString(5), rs_ModifierGroupDtl.getInt(6));
                hm_ModifierGroupDetail.put(modifierGroupName, obj);
            }
            rs_ModifierGroupDtl.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return hm_ModifierGroupDetail;
    }

    /**
     * Ritesh 10 Nov 2014
     *
     * @param itemCode
     * @return
     */
    private HashMap<String, clsModifierDtl> fun_Get_Modifier_All(String itemCode)
    {
        HashMap<String, clsModifierDtl> temp_hm_ModifierDtl = null;
        try
        {
            temp_hm_ModifierDtl = new HashMap<>();
            String sql_selectModifier = "select a.strModifierName,a.strModifierCode,b.dblRate,a.strModifierGroupCode,b.strDefaultModifier "
                    + " from tblmodifiermaster  a,"
                    + " tblitemmodofier b where a.strModifierCode=b.strModifierCode   "
                    + " and b.strItemCode='" + itemCode + "' group by a.strModifierCode;";
            ResultSet rs_ModifierDtl = clsGlobalVarClass.dbMysql.executeResultSet(sql_selectModifier);
            while (rs_ModifierDtl.next())
            {
                clsModifierDtl obj = new clsModifierDtl(rs_ModifierDtl.getString(2), rs_ModifierDtl.getString(1), "NA", rs_ModifierDtl.getDouble(3), itemCode, "N", 0.00, "N", 0.00, rs_ModifierDtl.getString(3));
                temp_hm_ModifierDtl.put(rs_ModifierDtl.getString(1), obj);
            }
            rs_ModifierDtl.close();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return temp_hm_ModifierDtl;
    }

    /**
     * Ritesh 08 Nov 2014
     *
     * @param hm_ModifierDtl
     */
    private void fun_setModifierName_to_Buttons(HashMap<String, clsModifierDtl> hm_ModifierDtl)
    {
        JButton[] btnSubMenuArray =
        {
            btnModi1, btnModi2, btnModi3, btnModi4, btnModi5, btnModi6, btnModi7, btnModi8, btnModi9, btnModi10, btnModi11, btnModi12, btnModi13, btnModi14, btnModi15, btnModi16
        };
        int i = 0;
        itemNames = new String[hm_ModifierDtl.size()];
        for (String modiName : hm_ModifierDtl.keySet())
        {
            itemNames[i] = modiName;
            if (i < 16)
            {
                btnSubMenuArray[i].setEnabled(true);
                btnSubMenuArray[i].setVisible(true);
                btnSubMenuArray[i].setText(modiName);
                btnSubMenuArray[i].setBackground(funGetSelecteColor(modiName));
                clsModifierDtl objModiDtl = hm_ModifierDtl.get(modiName);
                if (objModiDtl.getIsDefaultModifier().equalsIgnoreCase("Y"))
                {
                    btnSubMenuArray[i].setBackground(new Color(255, 105, 180));
                }
            }
            i++;
        }
        if (i > 16)
        {
            btnNextModifier.setEnabled(true);
        }
    }

    private void fun_Asign_ModifierGroup_TopSortingButtons(HashMap<String, clsModifierGroupDtl> hm_ModifierGroup)
    {
//        if (hm_ModifierGroup.size() > 0) {
//            listTopButtonName = new ArrayList<>();
//            for (String name : hm_ModifierGroup.keySet()) {
//                listTopButtonName.add(name);
//            }
//        }
        if (null != listGroupName && !listGroupName.isEmpty())
        {
            fillTopButtons();
        }
    }

    private void fillTopButtons()
    {
        try
        {
            JLabel btnArray[] =
            {
                lblgroup1, lblgroup2, lblgroup3, lblgroup4
            };
            itemNumber = 0;
            lblgroupNext.setEnabled(false);
            totalItems = listGroupName.size();

            if (totalItems > 4)
            {
                lblgroupNext.setEnabled(true);
            }
            if (totalItems >= 4)
            {
                for (int i = itemNumber; itemNumber < 4; itemNumber++)
                {
                    btnArray[itemNumber].setText(listGroupName.get(itemNumber));
                    btnArray[itemNumber].setEnabled(true);
                    btnArray[itemNumber].setBackground(new java.awt.Color(189, 218, 249));
                }
            }
            else
            {
                for (int i = itemNumber; itemNumber < totalItems; itemNumber++)
                {
                    btnArray[itemNumber].setText(listGroupName.get(itemNumber));
                    btnArray[itemNumber].setEnabled(true);
                    btnArray[itemNumber].setBackground(new java.awt.Color(189, 218, 249));
                }
            }

            for (int i = 0; i < 4; i++)
            {
                if (btnArray[i].getText().trim().length() == 0)
                {
                    btnArray[i].setEnabled(false);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void funFillModifier(String modifierGroupName)
    {
        if (modifierGroupName.trim().length() > 0)
        {
            nextItemClick = 0;
            selectedGroupName[0] = modifierGroupName;
            funSetColor();
            clsModifierGroupDtl obj = hm_ModifierGroup.get(modifierGroupName);
            String itemCode = obj.getTemp_ItemCode();
            String groupCode = obj.getStrModifierGroupCode();
            selectedGroupCode = groupCode;
            String strApplyMaxItemLimit = obj.getStrApplyItemLimit();
            String strApplyMinItemLimit = obj.getStrApplyMinItemLimit();
            if ("Y".equalsIgnoreCase(strApplyMaxItemLimit))
            {
                strApplyMaxItemLimit = String.valueOf(obj.getIntItemMaxLimit());
            }
            else
            {
                strApplyMaxItemLimit = "Not Applicable";
            }
            if ("Y".equalsIgnoreCase(strApplyMinItemLimit))
            {
                strApplyMinItemLimit = String.valueOf(obj.getIntItemMinLimit());
            }
            else
            {
                strApplyMinItemLimit = "Not Applicable";
            }
            funSetLabelMinMax(strApplyMaxItemLimit, strApplyMinItemLimit);
            hm_ModifierDtl = fun_Get_Modifier_GroupWise(itemCode, groupCode);
            funResetItemButtons();
            fun_setModifierName_to_Buttons((HashMap<String, clsModifierDtl>) hm_ModifierDtl);
        }
    }

    /**
     * Ritesh 08 Nov 2014
     *
     * @param itemCode
     * @param groupCode
     * @return
     */
    private HashMap<String, clsModifierDtl> fun_Get_Modifier_GroupWise(String itemCode, String groupCode)
    {
        HashMap<String, clsModifierDtl> temp_hm_ModifierDtl = null;
        try
        {
            temp_hm_ModifierDtl = new HashMap<>();
            String sql_selectModifier = "select a.strModifierName,a.strModifierCode,b.dblRate,a.strModifierGroupCode,c.strApplyMaxItemLimit,c.intItemMaxLimit,c.strApplyMinItemLimit,c.intItemMinLimit,b.strDefaultModifier"
                    + " from tblmodifiermaster  a,"
                    + " tblitemmodofier b, tblmodifiergrouphd c where a.strModifierCode=b.strModifierCode and  "
                    + "a.strModifierGroupCode=c.strModifierGroupCode and a.strModifierGroupCode='" + groupCode + "' "
                    + "and b.strItemCode='" + itemCode + "' group by a.strModifierCode;";
            ResultSet rs_ModifierDtl = clsGlobalVarClass.dbMysql.executeResultSet(sql_selectModifier);
            while (rs_ModifierDtl.next())
            {
                clsModifierDtl obj = new clsModifierDtl(rs_ModifierDtl.getString(2), rs_ModifierDtl.getString(1), rs_ModifierDtl.getString(4), rs_ModifierDtl.getDouble(3), itemCode, rs_ModifierDtl.getString(5), rs_ModifierDtl.getDouble(6), rs_ModifierDtl.getString(7), rs_ModifierDtl.getDouble(8), rs_ModifierDtl.getString(9));
                temp_hm_ModifierDtl.put(rs_ModifierDtl.getString(1), obj);
            }
            rs_ModifierDtl.close();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return temp_hm_ModifierDtl;
    }

    private void fun_NextModifierClick()
    {
        funResetItemButtons();
        btnPrevModifier.setEnabled(true);
        nextItemClick++;
        int itemDiv = itemNames.length / 17;
        JButton[] btnModifierArray =
        {
            btnModi1, btnModi2, btnModi3, btnModi4, btnModi5, btnModi6, btnModi7, btnModi8, btnModi9, btnModi10, btnModi11, btnModi12, btnModi13, btnModi14, btnModi15, btnModi16
        };
        if (itemDiv == nextItemClick)
        {
            btnNextModifier.setEnabled(false);
        }

        int k = 0;
        nextCnt = nextItemClick * 16;
        limit = nextCnt + 16;
        for (int j = nextCnt; j < limit; j++)
        {
            if (j == itemNames.length)
            {
                break;
            }
            btnModifierArray[k].setVisible(true);
            btnModifierArray[k].setEnabled(true);
            btnModifierArray[k].setText(itemNames[j]);
            k++;
        }
    }

    private void fun_PrevModifierClick()
    {
        funResetItemButtons();
        btnNextModifier.setEnabled(true);
        nextItemClick--;
        JButton[] btnModifierArray =
        {
            btnModi1, btnModi2, btnModi3, btnModi4, btnModi5, btnModi6, btnModi7, btnModi8, btnModi9, btnModi10, btnModi11, btnModi12, btnModi13, btnModi14, btnModi15, btnModi16
        };
        if (nextItemClick == 0)
        {
            btnPrevModifier.setEnabled(false);
        }
        int cntItem = 0;
        nextCnt = nextItemClick * 16;
        limit = nextCnt + 16;
        for (int cntItem1 = nextCnt; cntItem1 < limit; cntItem1++)
        {
            if (cntItem1 == itemNames.length)
            {
                break;
            }
            btnModifierArray[cntItem].setVisible(true);
            btnModifierArray[cntItem].setEnabled(true);
            btnModifierArray[cntItem].setText(itemNames[cntItem1]);
            btnModifierArray[cntItem].setBackground(funGetSelecteColor(itemNames[cntItem1]));
            cntItem++;
        }

    }

    private void fun_Add_Modifier_To_DirectBiller(String modiname, JButton btnModi1)
    {
        clsModifierDtl objModiDtl = hm_ModifierDtl.get(modiname);
        String temp_itemCode = objModiDtl.getItemCode();
        double rate = 0.00;
        rate = objModiDtl.getDblRate();
        double qty = 1;
        double amt = rate * qty;
        String ModifierCode = objModiDtl.getModifierCode();
        String ModifierName = objModiDtl.getModifierName();
        String modifierGroup = objModiDtl.getModifierGroupCode();
        String applyMaxLimit = objModiDtl.getStrApplyMaxItemLimit();
        double maxLimit = objModiDtl.getIntItemMaxLimit();
        boolean flag_is_ItemAdded = false;

        if (objModiDtl.getIsDefaultModifier().equalsIgnoreCase("Y"))
        {
            if (btnModi1.getBackground() != Color.LIGHT_GRAY)
            {
                btnModi1.setBackground(Color.LIGHT_GRAY);
                clsDirectBillerItemDtl obj_row = new clsDirectBillerItemDtl(ModifierName, temp_itemCode.concat(ModifierCode), qty, amt, true, ModifierCode, "", "", rate, "N", objDirectBillerItemDtl.getSeqNo() + "." + itemModifierSeqNO,0);
                obj_row.setStrDefaultModifierDeselectedYN("Y");
                itemModifierSeqNO++;
                objListUnselectedDefaultModifiers.add(obj_row);
            }
            else
            {
                btnModi1.setBackground(new Color(255, 105, 180));
                boolean flag = false;
                for (clsDirectBillerItemDtl ob1 : objListUnselectedDefaultModifiers)
                {
                    if (ob1.isIsModifier() && ob1.getModifierCode().equalsIgnoreCase(ModifierCode)/*&& ob.getModifierGroupCode().equalsIgnoreCase(modifierGroup)*/)
                    {
                        objListUnselectedDefaultModifiers.remove(ob1);
                        flag = true;
                    }
                    if (flag)
                    {
                        break;
                    }
                }
            }
        }
        else if (!fun_ItemAlready_selected(temp_itemCode, ModifierCode, modifierGroup))
        {
            if (!fun_is_ItemLimitExceed(applyMaxLimit, maxLimit))
            {
                clsDirectBillerItemDtl obj_row = new clsDirectBillerItemDtl(ModifierName, temp_itemCode.concat(ModifierCode), qty, amt, true, ModifierCode, "", "", rate, "N", objDirectBillerItemDtl.getSeqNo() + "." + itemModifierSeqNO,0);
                obj_row.setModifierGroupCode(modifierGroup);
                itemModifierSeqNO++;
                obj_List_ItemDtl.add(obj_row);
                selectedModefier.add(modiname);
                flag_is_ItemAdded = true;
                btnModi1.setBackground(funGetSelecteColor(modiname));
            }
            else
            {
                JOptionPane.showMessageDialog(this, "Maximimum Limit Exceed", "ALERT", JOptionPane.ERROR_MESSAGE);
            }
        }
        else
        {
            selectedModefier.remove(modiname);
            btnModi1.setBackground(funGetSelecteColor(modiname));
            boolean flag = false;
            for (clsDirectBillerItemDtl ob1 : obj_List_ItemDtl)
            {
                if (ob1.isIsModifier() && ob1.getModifierCode().equalsIgnoreCase(ModifierCode)/*&& ob.getModifierGroupCode().equalsIgnoreCase(modifierGroup)*/)
                {
                    obj_List_ItemDtl.remove(ob1);
                    flag = true;
                }
                if (flag)
                {
                    break;
                }
            }
        }

        if (flag_is_ItemAdded && fun_is_ItemLimitExceed(applyMaxLimit, maxLimit))
        {
            funSkipSelectedGroup();
        }
    }

    private void funAddModifierToKOT(String modiname, JButton btnModi1)
    {
        int freeItem = 0;
        clsModifierDtl objModiDtl = hm_ModifierDtl.get(modiname);
        String temp_itemCode = objModiDtl.getItemCode();
        double rate = objModiDtl.getDblRate();
        String ModifierCode = objModiDtl.getModifierCode();
        String ModifierName = objModiDtl.getModifierName();
        String modifierGroup = objModiDtl.getModifierGroupCode();
        String applyMaxLimit = objModiDtl.getStrApplyMaxItemLimit();
        double maxLimit = objModiDtl.getIntItemMaxLimit();
        boolean flgItemAdded = false;

        if (objModiDtl.getIsDefaultModifier().equalsIgnoreCase("Y"))
        {
            if (btnModi1.getBackground() != Color.LIGHT_GRAY)
            {
                btnModi1.setBackground(Color.LIGHT_GRAY);
                clsMakeKotItemDtl obj_row = new clsMakeKotItemDtl(seqNo + "." + itemModifierSeqNO, KOTNO, TableNO, WaiterNo, ModifierName, temp_itemCode.concat(ModifierCode), 1.00, rate, 0, "N", "N", true, ModifierCode, "", modifierGroup, "N", rate);
                obj_row.setStrDefaultModifierDeselectedYN("Y");
                itemModifierSeqNO++;
                objListUnselectedDefaultModifiersForKOT.add(obj_row);
            }
            else
            {
                btnModi1.setBackground(new Color(255, 105, 180));
                boolean flag = false;
                for (clsMakeKotItemDtl ob1 : objListUnselectedDefaultModifiersForKOT)
                {
                    if (ob1.isIsModifier() && ob1.getModifierCode().equalsIgnoreCase(ModifierCode)/*&& ob.getModifierGroupCode().equalsIgnoreCase(modifierGroup)*/)
                    {
                        objListUnselectedDefaultModifiersForKOT.remove(ob1);
                        flag = true;
                    }
                    if (flag)
                    {
                        break;
                    }
                }
            }
        }
        else if (!fun_ItemAlready_selected(temp_itemCode, ModifierCode, modifierGroup))
        {
            if (!fun_is_ItemLimitExceed(applyMaxLimit, maxLimit))
            {
                clsMakeKotItemDtl obj_row = new clsMakeKotItemDtl(seqNo + "." + itemModifierSeqNO, KOTNO, TableNO, WaiterNo, ModifierName, temp_itemCode.concat(ModifierCode), 1.00, rate, 0, "N", "N", true, ModifierCode, "", modifierGroup, "N", rate);
                itemModifierSeqNO++;
                obj_List_KOT_ItemDtl.add(obj_row);
                selectedModefier.add(modiname);
                flgItemAdded = true;
                btnModi1.setBackground(funGetSelecteColor(modiname));
            }
            else
            {
                JOptionPane.showMessageDialog(this, "Maximimum Limit Exceed", "ALERT", JOptionPane.ERROR_MESSAGE);
            }
        }
        else
        {
            selectedModefier.remove(modiname);
            btnModi1.setBackground(funGetSelecteColor(modiname));
            boolean flag = false;
            for (clsMakeKotItemDtl ob : obj_List_KOT_ItemDtl)
            {
                if (ob.isIsModifier() && ob.getModifierCode().equalsIgnoreCase(ModifierCode)/*&& ob.getModifierGroupCode().equalsIgnoreCase(modifierGroup)*/)
                {
                    obj_List_KOT_ItemDtl.remove(ob);
                    flag = true;
                }
                if (flag)
                {
                    break;
                }
            }
        }

        if (flgItemAdded && fun_is_ItemLimitExceed(applyMaxLimit, maxLimit))
        {
            funSkipSelectedGroup();
        }

    }

    private Color funGetSelecteColor(String name)
    {
        Color rr = Color.LIGHT_GRAY;
        try
        {
            if (selectedModefier.contains(name))
            {
                rr = Color.PINK;
            }
        }
        catch (Exception e)
        {
        }

        return rr;
    }

    private void funSetColorToLabel(JLabel lblgroup)
    {
        String x = selectedGroupName[0];
        if (x.equalsIgnoreCase(lblgroup.getText()))
        {
            lblgroup.setBackground(new java.awt.Color(36, 143, 230));
        }
    }

    private void fun_Done_Selection()
    {

        if (flagDirectBiller)
        {
            if (fun_Check_FinalOrderQty())
            {

                if (freeItem > 0)
                {
                    int i = 0;
                    for (clsDirectBillerItemDtl obtemp : obj_List_ItemDtl)
                    {
                        if (obtemp.isIsModifier())
                        {
                            obtemp.setAmt(0.00);
                            i++;
                        }
                        if (freeItem == i)
                        {
                            break;
                        }
                    }
                }

                ///                
                if (objDirectBiller.fun_Add_TDH_Modifier(objListUnselectedDefaultModifiers))
                {

                }
                //

                if (objDirectBiller.fun_Add_TDH_Modifier(obj_List_ItemDtl))
                {
                    //add unselectedDefaultModifiers from +""+objListUnselectedDefaultModifiers+"" to direct biller

                    dispose();
                    objDirectBiller = null;
                    System.gc();
                }
            }
        }
        else
        {

            ///                
            if (objMakeKot.fun_Add_TDH_Modifier(objListUnselectedDefaultModifiersForKOT))
            {

            }
            //

            if (fun_Check_FinalOrderQty())
            {
                if (objMakeKot.fun_Add_TDH_Modifier(obj_List_KOT_ItemDtl))
                {
                    dispose();
                    objMakeKot = null;
                    System.gc();
                }
            }
        }
    }

    private void funSkipSelectedGroup()
    {
        boolean nextSkip = false;
        String currentSelectedGroup = selectedGroupName[0];
        if (currentSelectedGroup.equalsIgnoreCase("size"))
        {
            btnSkipGroup.setEnabled(false);
        }
        else
        {
            btnSkipGroup.setEnabled(true);
        }
        int index = listGroupName.indexOf(currentSelectedGroup);
        int newindex = index + 1;
        int listSize = listGroupName.size();
        int test = listSize - newindex;
        if (test >= 1)
        {
            String nextGroup = listGroupName.get(newindex);
            // selectedGroup[0]=nextGroup;
            funFillModifier(nextGroup);
            nextSkip = true;
        }
        if (nextSkip)
        {
            int x = listSize - 1;
            x = newindex / x;
            if (listSize > 4 && x == 1)
            {
                if (test >= 1)
                {
                    funNextGroupClick();
                }
            }
        }

    }

    private void funSetColor()
    {
        JLabel btnArray[] =
        {
            lblgroup1, lblgroup2, lblgroup3, lblgroup4
        };
        for (int i = 0; i < 4; i++)
        {
            String x = selectedGroupName[0];
            if (x.equalsIgnoreCase(btnArray[i].getText()))
            {
                btnArray[i].setBackground(new java.awt.Color(36, 143, 230));
            }
            else
            {
                btnArray[i].setBackground(new java.awt.Color(189, 218, 249));
            }
        }
    }

    private void funSetColor(String selectedMenuName)
    {
        JLabel btnArray[] =
        {
            lblgroup1, lblgroup2, lblgroup3, lblgroup4
        };
        for (int i = 0; i < 4; i++)
        {
            String x = selectedMenuName;
            if (x.equalsIgnoreCase(btnArray[i].getText()))
            {
                btnArray[i].setBackground(new java.awt.Color(36, 143, 230));
            }
            else
            {
                btnArray[i].setBackground(new java.awt.Color(189, 218, 249));
            }
        }
    }

    private void funSetLabelMinMax(String strApplyMaxItemLimit, String strApplyMinItemLimit)
    {
        lblMinLimit.setText(strApplyMinItemLimit);
        lblMaxLimit.setText(strApplyMaxItemLimit);

    }

    private boolean fun_calculateMinMaxLevel()
    {
        boolean flag = false;
        boolean isMinLimitApplicable = false;
        boolean isMaxLimitApplicable = false;
        int selecedQty = 0;
        int minQty = 0;
        int maxQty = 0;
        try
        {
            if ("Not Applicable".equalsIgnoreCase(lblMinLimit.getText().trim()) || lblMinLimit.getText().trim().length() == 0)
            {
                flag = true;
                isMinLimitApplicable = false;
            }
            else
            {
                minQty = Integer.parseInt(lblMinLimit.getText());
                isMinLimitApplicable = true;
            }
//            if("Not Applicable".equalsIgnoreCase(lblMaxLimit.getText().trim())){
//               flag=true; 
//               isMaxLimitApplicable=false;
//            }else{
//               maxQty =Integer.parseInt(lblMaxLimit.getText());  
//               isMaxLimitApplicable=true;
//            }

            if (flagDirectBiller)
            {
                for (clsDirectBillerItemDtl obj1 : obj_List_ItemDtl)
                {
                    if (obj1.getModifierGroupCode().equalsIgnoreCase(selectedGroupCode))
                    {
                        selecedQty++;
                    }
                }
            }
            else
            {
                for (clsMakeKotItemDtl obj : obj_List_KOT_ItemDtl)
                {
                    if (obj.getModifierGroupCode().equalsIgnoreCase(selectedGroupCode))
                    {
                        selecedQty++;
                    }
                }

            }
            if (!isMinLimitApplicable)
            {
                flag = true;
            }
            else if (isMinLimitApplicable && minQty <= selecedQty)
            {
                flag = true;
            }
            else
            {
                JOptionPane.showMessageDialog(this, "Please Select Minimum Item " + minQty + "", "ALERT", JOptionPane.INFORMATION_MESSAGE);
                flag = false;
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return flag;
    }

    private boolean fun_is_ItemLimitExceed(String applyMaxLimit, double maxLimit)
    {
        boolean flag = false;
        double currentMaxLimit = 0.00;
        if ("N".equalsIgnoreCase(applyMaxLimit))
        {
            flag = false;
        }
        else
        {
            if (flagDirectBiller)
            {
                for (clsDirectBillerItemDtl ob : obj_List_ItemDtl)
                {
                    if (ob.getModifierGroupCode().equalsIgnoreCase(selectedGroupCode))//should be, ob.getModifierGroupCode()
                    {
                        currentMaxLimit++;
                    }
                }
                if (currentMaxLimit >= maxLimit)
                {
                    flag = true;

                }
            }
            else
            {
                for (clsMakeKotItemDtl ob : obj_List_KOT_ItemDtl)
                {
                    if (ob.getModifierGroupCode().equalsIgnoreCase(selectedGroupCode))
                    {
                        currentMaxLimit++;
                    }
                }
                if (currentMaxLimit >= maxLimit)
                {
                    flag = true;

                }
            }
        }
        return flag;

    }

    private boolean fun_ItemAlready_selected(String temp_itemCode, String ModifierCode, String modifierGroup)
    {
        boolean flag = false;
        if (flagDirectBiller)
        {
            for (clsDirectBillerItemDtl obj1 : obj_List_ItemDtl)
            {
                if (obj1.isIsModifier() && obj1.getModifierCode().equalsIgnoreCase(ModifierCode)/*&& ob.getModifierGroupCode().equalsIgnoreCase(modifierGroup)*/)
                {
                    flag = true;
                }
                if (flag)
                {
                    break;
                }
            }

        }

        else
        {
            for (clsMakeKotItemDtl ob : obj_List_KOT_ItemDtl)
            {
                if (ob.isIsModifier() && ob.getModifierCode().equalsIgnoreCase(ModifierCode)/*&& ob.getModifierGroupCode().equalsIgnoreCase(modifierGroup)*/)
                {
                    flag = true;
                }
                if (flag)
                {
                    break;
                }
            }

        }
        return flag;
    }

    private void funNextGroupClick()
    {
        if (lblgroupNext.isEnabled())
        {
            JLabel btnArray[] =
            {
                lblgroup1, lblgroup2, lblgroup3, lblgroup4
            };
            reset_TopSortingButtons();
            int x = totalItems - itemNumber;
            if (x > 4)
            {
                for (int i = 0; i < 4; i++, itemNumber++)
                {
                    btnArray[i].setText(listGroupName.get(itemNumber));
                    btnArray[i].setEnabled(true);
                    btnArray[i].setBackground(new java.awt.Color(189, 218, 249));

                }
            }
            else
            {
                for (int i = 0; i < x; i++, itemNumber++)
                {
                    btnArray[i].setText(listGroupName.get(itemNumber));
                    btnArray[i].setEnabled(true);
                    btnArray[i].setBackground(new java.awt.Color(189, 218, 249));
                }
            }
            if (x > 4)
            {
                lblgroupNext.setEnabled(true);
            }
            lblgroupPrev.setEnabled(true);
            funSetColor();
        }
    }

    private void funPrevGroupClick()
    {
        if (lblgroupPrev.isEnabled())
        {
            JLabel btnArray[] =
            {
                lblgroup1, lblgroup2, lblgroup3, lblgroup4
            };
            reset_TopSortingButtons();
            if (totalItems > 4)
            {
                int x = totalItems - itemNumber;
                itemNumber = x;
                for (int i = 0; i < 4; i++, itemNumber++)
                {
                    btnArray[i].setText(listGroupName.get(itemNumber));
                    btnArray[i].setEnabled(true);
                    btnArray[i].setBackground(new java.awt.Color(189, 218, 249));
                }
                lblgroupNext.setEnabled(true);
            }
            funSetColor();
        }

    }

    private boolean fun_Check_FinalOrderQty()
    {

        int qty = 0;
        boolean flag_minLevel = false;
        boolean orderFulfil = false;
        if (clsGlobalVarClass.gTransactionType.equals("Make KOT"))
        {

            if (obj_List_KOT_ItemDtl.size() == 1)
            {
                JOptionPane.showMessageDialog(this, "Please Select Item", "Error", JOptionPane.ERROR_MESSAGE);
                orderFulfil = false;
            }
            else
            {
                for (String groupName : listGroupName)
                {
                    qty = 0;
                    clsModifierGroupDtl ob = hm_ModifierGroup.get(groupName);
                    String applyMinLimit = ob.getStrApplyMinItemLimit();
                    int minLimit = ob.getIntItemMinLimit();
                    String ModifierGroupCode = ob.getStrModifierGroupCode();
                    if ("Y".equalsIgnoreCase(applyMinLimit))
                    {
                        for (clsMakeKotItemDtl objMakeKotlist : obj_List_KOT_ItemDtl)
                        {
                            if (objMakeKotlist.isIsModifier() && objMakeKotlist.getModifierGroupCode().equalsIgnoreCase(ModifierGroupCode))
                            {
                                qty++;
                            }
                        }
                        if (qty < minLimit)
                        {
                            flag_minLevel = true;
                            orderFulfil = false;
                        }
                    }
                    if (flag_minLevel)
                    {
                        JOptionPane.showMessageDialog(this, "Please Select Minimum Item " + minLimit + " with " + groupName + "", "ALERT", JOptionPane.INFORMATION_MESSAGE);
                        break;
                    }
                }
                if (!flag_minLevel)
                {
                    orderFulfil = true;
                }
            }
        }

        if (clsGlobalVarClass.gTransactionType.equals("Direct Biller"))
        {
            if (obj_List_ItemDtl.size() == 1)
            {
                JOptionPane.showMessageDialog(this, "Please Select Item", "Error", JOptionPane.ERROR_MESSAGE);
                orderFulfil = false;
            }
            else
            {
                for (String groupName : listGroupName)
                {
                    qty = 0;
                    clsModifierGroupDtl ob = hm_ModifierGroup.get(groupName);
                    String applyMinLimit = ob.getStrApplyMinItemLimit();
                    int minLimit = ob.getIntItemMinLimit();
                    String ModifierGroupCode = ob.getStrModifierGroupCode();
                    if ("Y".equalsIgnoreCase(applyMinLimit))
                    {
                        for (clsDirectBillerItemDtl obJbilllist : obj_List_ItemDtl)
                        {
                            if (obJbilllist.isIsModifier() && obJbilllist.getModifierGroupCode().equalsIgnoreCase(ModifierGroupCode))
                            {
                                qty++;
                            }
                        }
                        if (qty < minLimit)
                        {
                            flag_minLevel = true;
                            orderFulfil = false;
                        }
                    }
                    if (flag_minLevel)
                    {
                        JOptionPane.showMessageDialog(this, "Please Select Minimum Item " + minLimit + " with " + groupName + "", "ALERT", JOptionPane.INFORMATION_MESSAGE);
                        break;
                    }
                }
                if (!flag_minLevel)
                {
                    orderFulfil = true;
                }
            }
        }

        return orderFulfil;

    }

    private void funLoadMenuNames(String tdhOnComboItemCode)
    {
        try
        {
            String sqlMenues = "select a.strSubItemMenuCode,ifnull(b.strMenuName,'NA'),ifnull(a.intSubItemQty,0) as MaxQty "
                    + "from tbltdhcomboitemdtl a "
                    + "left outer join tblmenuhd b on a.strSubItemMenuCode=b.strMenuCode "
                    + "left outer join tbltdhhd c on a.strTDHCode=c.strTDHCode "
                    + "where c.strItemCode='" + tdhOnComboItemCode + "' and c.strComboItemYN='Y' "
                    + "group by a.strSubItemMenuCode";
            //System.out.println("menues="+sqlMenues);
            ResultSet rsMenu = clsGlobalVarClass.dbMysql.executeResultSet(sqlMenues);
            while (rsMenu.next())
            {
                mapMenus.put(rsMenu.getString(2), rsMenu.getString(1));
                listOfMenuCode.add(rsMenu.getString(1));
                listOfMenuName.add(rsMenu.getString(2));
                mapMenuMaxQty.put(rsMenu.getString(1), rsMenu.getInt(3));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void funFillMenuNames()
    {
        try
        {
            JLabel btnArray[] =
            {
                lblgroup1, lblgroup2, lblgroup3, lblgroup4
            };

            totalItems = listOfMenuCode.size();
            if (totalItems > 4)
            {
                lblgroupNext.setEnabled(true);
            }
            itemNumber = 0;
            if (totalItems >= 4)
            {
                for (int i = itemNumber; itemNumber < 4; itemNumber++)
                {
                    btnArray[itemNumber].setText(listOfMenuName.get(itemNumber));
                    btnArray[itemNumber].setEnabled(true);
                    btnArray[itemNumber].setBackground(new java.awt.Color(189, 218, 249));
                }
            }
            else
            {
                for (int i = itemNumber; itemNumber < totalItems; itemNumber++)
                {
                    btnArray[itemNumber].setText(listOfMenuName.get(itemNumber));
                    btnArray[itemNumber].setEnabled(true);
                    btnArray[itemNumber].setBackground(new java.awt.Color(189, 218, 249));
                }
            }
            btnArray[0].setBackground(new java.awt.Color(36, 143, 230));
            for (int i = 0; i < 4; i++)
            {
                if (btnArray[i].getText().trim().length() == 0)
                {
                    btnArray[i].setEnabled(false);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void funLoadMenuItems()
    {
        try
        {
            listOfItemCode.clear();
            listOfItemName.clear();
            intItemIndex = 0;

            funSetColor(selectedMenuName);
            String menuCode = mapMenus.get(selectedMenuName);
            funSetLabelMinMax(mapMenuMaxQty.get(menuCode).toString(), "0");

            String sqlItems = "select a.strSubItemCode,b.strItemName "
                    + "from tbltdhcomboitemdtl a "
                    + "left outer join tblitemmaster b on a.strSubItemCode=b.strItemCode "
                    + "where a.strItemCode='" + tdhOnComboItemCode + "' "
                    + "and a.strSubItemMenuCode='" + menuCode + "' ";
            //System.out.println("menu Items="+sqlItems);
            ResultSet rsItems = clsGlobalVarClass.dbMysql.executeResultSet(sqlItems);
            while (rsItems.next())
            {
                listOfItemCode.add(rsItems.getString(1));
                listOfItemName.add(rsItems.getString(2));
            }
            funResetItemButtons();
            funFillMenuItems();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void funFillMenuItems()
    {
        if (listOfItemCode.size() > 0)
        {
            JButton[] btnSubMenuArray =
            {
                btnModi1, btnModi2, btnModi3, btnModi4, btnModi5, btnModi6, btnModi7, btnModi8, btnModi9, btnModi10, btnModi11, btnModi12, btnModi13, btnModi14, btnModi15, btnModi16
            };
            int remainingSizeOfItems = (listOfItemCode.size() - (intItemIndex * 16));
            for (int i = 0; (i < 16) && (i < remainingSizeOfItems); i++)
            {
                if (i < 16)
                {
                    btnSubMenuArray[i].setEnabled(true);
                    btnSubMenuArray[i].setVisible(true);
                    btnSubMenuArray[i].setText(listOfItemName.get(i + (intItemIndex * 16)));
                    if (funIsItemAlreadySelected(mapMenus.get(selectedMenuName), i + (intItemIndex * 16)))
                    {
                        btnSubMenuArray[i].setBackground(Color.PINK);
                    }
                    else
                    {
                        btnSubMenuArray[i].setBackground(Color.LIGHT_GRAY);
                    }
                }
            }
            if (remainingSizeOfItems > 16)
            {
                btnNextModifier.setEnabled(true);
            }
            else
            {
                btnNextModifier.setEnabled(false);
            }
            if (intItemIndex <= 0)
            {
                btnPrevModifier.setEnabled(false);
            }
            else
            {
                btnPrevModifier.setEnabled(true);
            }
        }
    }

    private void funFillMenuAndItems()
    {
        try
        {
            JLabel btnArray[] =
            {
                lblgroup1, lblgroup2, lblgroup3, lblgroup4
            };
            int intMenuSize = listOfMenuCode.size();
            int intRemainingSize = intMenuSize - (intMenuIndex * 4);
            if (intMenuIndex <= 0)
            {
                lblgroupPrev.setEnabled(false);
            }
            else
            {
                lblgroupPrev.setEnabled(true);
            }
            if (intRemainingSize > 4)
            {
                lblgroupNext.setEnabled(true);
            }
            else
            {
                lblgroupNext.setEnabled(false);
            }
            for (int i = 0; (i < 4) && (i < intRemainingSize); i++)
            {
                btnArray[i].setText(listOfMenuName.get(i + (intMenuIndex * 4)));
                btnArray[i].setEnabled(true);
                btnArray[i].setBackground(new java.awt.Color(189, 218, 249));
            }

            btnArray[0].setBackground(new java.awt.Color(36, 143, 230));
            for (int i = 0; i < 4; i++)
            {
                if (btnArray[i].getText().trim().length() == 0)
                {
                    btnArray[i].setEnabled(false);
                }
            }

            selectedMenuName = lblgroup1.getText();
            funLoadMenuItems();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void funNextPrevItemButtonClicked()
    {
        funResetItemButtons();
        funFillMenuItems();
    }

    private boolean funIsItemAlreadySelected(String menuCode, int itemIndex)
    {
        boolean isItemAlreadySelected = false;
        String itemCode = listOfItemCode.get(itemIndex);
        if (mapSelectedMenuAndItemList.size() > 0 && mapSelectedMenuAndItemList.containsKey(menuCode))
        {
            Map<String, String> mapItems = mapSelectedMenuAndItemList.get(menuCode);
            if (mapItems.containsKey(itemCode))
            {
                isItemAlreadySelected = true;
            }
        }

        return isItemAlreadySelected;
    }

    private void funAddItem(String menuCode, int itemIndex)
    {
        String itemCode = listOfItemCode.get(itemIndex);
        String itemName = listOfItemName.get(itemIndex);

        Map<String, String> mapItem = new HashMap<String, String>();
        mapItem.put(itemCode, itemName);
        if (mapSelectedMenuAndItemList.size() > 0 && mapSelectedMenuAndItemList.containsKey(menuCode))
        {
            mapSelectedMenuAndItemList.get(menuCode).put(itemCode, itemName);
        }
        else
        {
            mapSelectedMenuAndItemList.put(menuCode, mapItem);
        }
    }

    private void funRemoveItem(String menuCode, int itemIndex)
    {
        String itemCode = listOfItemCode.get(itemIndex);
        mapSelectedMenuAndItemList.get(menuCode).remove(itemCode);
    }

    private void funItemClicked(JButton button, int itemIndex)
    {
        String menuCode = mapMenus.get(selectedMenuName);
        int menuMaxQty = mapMenuMaxQty.get(menuCode);
        if (mapSelectedMenuAndItemList.size() > 0 && mapSelectedMenuAndItemList.containsKey(menuCode))
        {
            boolean isItemSelected = funIsItemAlreadySelected(menuCode, itemIndex);
            if (isItemSelected)
            {
                funRemoveItem(menuCode, itemIndex);
                button.setBackground(Color.LIGHT_GRAY);
            }
            else
            {
                if (mapSelectedMenuAndItemList.get(menuCode).size() < menuMaxQty)
                {
                    funAddItem(menuCode, itemIndex);
                    button.setBackground(Color.PINK);
                }
                else
                {
                    new frmOkPopUp(null, "You have selected Max Item Quanity.", "Menu Item Max Quanity.", 2).setVisible(true);
                    return;
                }
            }
        }
        else
        {
            funAddItem(menuCode, itemIndex);
            button.setBackground(Color.PINK);
        }
    }

    private class clsModifierGroupDtl
    {

        private final String strModifierGroupCode;
        private final String strModifierGroupShortName;
        private final String strApplyMaxItemLimit;
        private final int intItemMaxLimit;
        private final String temp_ItemCode;
        private final String strApplyMinItemLimit;
        private final int intItemMinLimit;

        clsModifierGroupDtl(String strModifierGroupCode, String strModifierGroupShortName, String strApplyMaxItemLimit, int intItemMaxLimit, String temp_ItemCode, String strApplyMinItemLimit, int intItemMinLimit)
        {
            this.strModifierGroupCode = strModifierGroupCode;
            this.strModifierGroupShortName = strModifierGroupShortName;
            this.strApplyMaxItemLimit = strApplyMaxItemLimit;
            this.intItemMaxLimit = intItemMaxLimit;
            this.temp_ItemCode = temp_ItemCode;
            this.strApplyMinItemLimit = strApplyMinItemLimit;
            this.intItemMinLimit = intItemMinLimit;
        }

        /**
         * @return the strModifierGroupCode
         */
        public String getStrModifierGroupCode()
        {
            return strModifierGroupCode;
        }

        /**
         * @return the strModifierGroupShortName
         */
        public String getStrModifierGroupShortName()
        {
            return strModifierGroupShortName;
        }

        /**
         * @return the strApplyMaxItemLimit
         */
        public String getStrApplyItemLimit()
        {
            return strApplyMaxItemLimit;
        }

        /**
         * @return the intItemMaxLimit
         */
        public int getIntItemMaxLimit()
        {
            return intItemMaxLimit;
        }

        /**
         * @return the temp_ItemCode
         */
        public String getTemp_ItemCode()
        {
            return temp_ItemCode;
        }

        /**
         * @return the strApplyMinItemLimit
         */
        public String getStrApplyMinItemLimit()
        {
            return strApplyMinItemLimit;
        }

        /**
         * @return the intItemMinLimit
         */
        public int getIntItemMinLimit()
        {
            return intItemMinLimit;
        }

    }

    private class clsModifierDtl
    {

        private final String modifierCode;
        private final String modifierName;
        private final String modifierGroupCode;
        private final double dblRate;
        private final String itemCode;
        private final String strApplyMaxItemLimit;
        private final double intItemMaxLimit;
        private final String strApplyMinItemLimit;
        private final double intItemMinLimit;
        private String isDefaultModifier;

        /**
         * Ritesh 08 Nov 2014
         *
         * @param strModifierCode
         * @param strModifierName
         * @param strModifierGroupCode
         * @param dblRate
         * @param itemCode
         * @param strApplyItemLimit
         * @param intItemLimit
         */
        clsModifierDtl(String strModifierCode, String strModifierName, String strModifierGroupCode, double dblRate, String itemCode,
                String strApplyMaxItemLimit, double intItemMaxLimit, String strApplyMinItemLimit, double intItemMinLimit, String isDefaultModifier)
        {
            this.modifierCode = strModifierCode;
            this.modifierName = strModifierName;
            this.modifierGroupCode = strModifierGroupCode;
            this.dblRate = dblRate;
            this.itemCode = itemCode;
            this.strApplyMaxItemLimit = strApplyMaxItemLimit;
            this.intItemMaxLimit = intItemMaxLimit;
            this.strApplyMinItemLimit = strApplyMinItemLimit;
            this.intItemMinLimit = intItemMinLimit;
            this.isDefaultModifier = isDefaultModifier;
        }

        /**
         * @return the modifierCode
         */
        public String getModifierCode()
        {
            return modifierCode;
        }

        /**
         * @return the modifierName
         */
        public String getModifierName()
        {
            return modifierName;
        }

        /**
         * @return the modifierGroupCode
         */
        public String getModifierGroupCode()
        {
            return modifierGroupCode;
        }

        /**
         * @return the dblRate
         */
        public double getDblRate()
        {
            return dblRate;
        }

        /**
         * @return the itemCode
         */
        public String getItemCode()
        {
            return itemCode;
        }

        /**
         * @return the strApplyMaxItemLimit
         */
        public String getStrApplyMaxItemLimit()
        {
            return strApplyMaxItemLimit;
        }

        /**
         * @return the intItemMaxLimit
         */
        public double getIntItemMaxLimit()
        {
            return intItemMaxLimit;
        }

        /**
         * @return the strApplyMinItemLimit
         */
        public String getStrApplyMinItemLimit()
        {
            return strApplyMinItemLimit;
        }

        /**
         * @return the intItemMinLimit
         */
        public double getIntItemMinLimit()
        {
            return intItemMinLimit;
        }

        public String getIsDefaultModifier()
        {
            return isDefaultModifier;
        }

        public void setIsDefaultModifier(String isDefaultModifier)
        {
            this.isDefaultModifier = isDefaultModifier;
        }

    }

}
