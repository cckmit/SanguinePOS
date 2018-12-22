/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSTransaction.view;

import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsUtility;
import com.POSPrinting.clsKOTGeneration;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Timer;
import javax.swing.text.html.HTMLEditorKit;

/**
 *
 * @author sss11
 */
public class frmTableStatusReport extends javax.swing.JFrame
{

    private clsUtility objUtility = new clsUtility();
    private Map<String, String> hmTable;
    private Map<String, Integer> hmTableSeq;
    private String clsAreaCode, clsAreaName;
    public boolean flgTableSelection;
    private String sql, fieldSelected;
    private int cntNavigate;
    private int tblStartIndex;
    private HashMap<String, String> mapPOSCode;
    private HashMap<String, String> mapPOSName;

    public frmTableStatusReport()
    {
        initComponents();
        lblModuleName.setText(clsGlobalVarClass.gSelectedModule);
        lblUserCode.setText(clsGlobalVarClass.gUserCode);
        lblPosName.setText(clsGlobalVarClass.gPOSName);
        lblDate.setText(clsGlobalVarClass.gPOSDateToDisplay);
//        funFillAllTables();
        try
        {
            tblStartIndex = 0;
            cntNavigate = 0;
            hmTable = new HashMap<String, String>();
            hmTableSeq = new HashMap<String, Integer>();
            funFillAreaCombo();
            funFillPOSNameCombo();
            funInitTables();
            funLoadTables(0, hmTable.size());

        }
        catch (Exception ex)
        {
            Logger.getLogger(frmTableStatusReport.class.getName()).log(Level.SEVERE, null, ex);
        }

        cmbPOSName.addItemListener(new ItemListener()
        {
            @Override
            public void itemStateChanged(ItemEvent e)
            {
                funCmbPosNameFillData();
            }
        });

        cmbTableStatus.addItemListener(new ItemListener()
        {
            @Override
            public void itemStateChanged(ItemEvent e)
            {
                funFillData();
            }
        });

        cmbAreaCombo.addItemListener(new ItemListener()
        {
            @Override
            public void itemStateChanged(ItemEvent e)
            {
                funForAreaNameChanged();
            }
        });

//        Timer timer = new Timer(1000, new ActionListener()
//        {
//            @Override
//            public void actionPerformed(ActionEvent e)
//            {
////                int oldBillSize = mapKOTHd.size();
//                /*
//                 * int newBillSize = funGetNewBillSize(); if (oldBillSize !=
//                 * newBillSize) { funRefreshForm(); }
//                 */
//                funLoadTables(0, hmTable.size());
//            }
//        });
//        timer.setRepeats(true);
//        timer.setCoalesce(true);
//        timer.setInitialDelay(0);
//        timer.start();
    }

    /**
     * Component initialization
     */
    public void funFillAllTables()
    {
        try
        {
            int cntRows = 0, totalTables = 0;

            // lblProductName.setText("JPOS - Table Status  All Tables");
            String sql = "select count(strTableName) from tbltablemaster";
            ResultSet rsTables = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            if (rsTables.next())
            {
                cntRows = rsTables.getInt(1);
                totalTables = rsTables.getInt(1);
            }
            if (cntRows >= 10)
            {
                cntRows = 10;
            }
            sql = "select strTableName,strStatus,intPaxNo from tbltablemaster order by strTableName";
            rsTables = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            List<Map> listAllTables = new ArrayList();
            java.util.Vector vAllTables = new java.util.Vector();
            Map<String, String> mapAllTables = new HashMap<String, String>();
            int cnt = 0, cnt1 = 0;
            while (rsTables.next())
            {
                String tableName = rsTables.getString(1) + "(" + rsTables.getString(3) + ")";
                //System.out.println(tableName);
                String status = rsTables.getString(2);
                vAllTables.add(tableName);
                String color = "green";
                if (status.equalsIgnoreCase("occupied"))
                {
                    color = "red";
                }
                else if (status.equalsIgnoreCase("billed"))
                {
                    color = "blue";
                }
                mapAllTables.put(tableName, color);
                //System.out.println(mapAllTables.size());                
                cnt++;
                if (cnt % cntRows == 0)
                {
                    cnt1 = cnt1 + cntRows;
                    listAllTables.add(mapAllTables);
                    mapAllTables = new HashMap<String, String>();
                }
            }
            //System.out.println("Cnt="+cnt+"\tTotal="+totalTables);
            if (cnt > cnt1)
            {
                listAllTables.add(mapAllTables);
            }

//            txtPanelTables.setEditorKit(new HTMLEditorKit());
//            String text="";
//            int k=0;
//            int m=cntRows;
//            //System.out.println("List Size="+listAllTables.size());
//            for(int i=0;i<listAllTables.size();i++)
//            {
//                Map mapTempTables=listAllTables.get(i);
//                if(i>0)
//                {
//                    m=m+mapTempTables.size();
//                }
//                for(int j=k;j<m-1;j++)
//                {
//                    String color=mapTempTables.get(vAllTables.elementAt(j).toString()).toString();
////                    text+="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
//                    text+= "<font size=\"4\" face=\"verdana\" color=\""+color+"\">"+vAllTables.elementAt(j).toString()+"</font>";
//                    text+="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
//                }
//                text+="<br><br>";
//                k=m;
//                //m=m+cntRows;
//            }
//            String finalText="<html>"+text+"</html>";
//            txtPanelTables.setText(finalText);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    /**
     * Fill Occupied Tables
     */
    public void funFillOccupiedTables()
    {
        try
        {
            int cntRows = 0;
            lblProductName.setText("JPOS - Table Status  Busy Tables");
            /*String sql ="select a.strTableName,a.strStatus from tbltablemaster a,tblitemrtemp b "
                    + "where a.strTableNo=b.strTableNo";*/
            String sql = "select count(strTableName) from tbltablemaster where strStatus='Occupied' order by strTableName";
            ResultSet rsTables = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            if (rsTables.next())
            {
                cntRows = rsTables.getInt(1);
            }
            if (cntRows >= 9)
            {
                cntRows = 9;
            }
            /*String sql ="select a.strTableName,a.strStatus from tbltablemaster a,tblitemrtemp b "
                    + "where a.strTableNo=b.strTableNo";*/
            sql = "select strTableName,strStatus,intPaxNo from tbltablemaster where strStatus='Occupied' order by strTableName";
            rsTables = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            List<Map> listOccupiedTables = new ArrayList();
            java.util.Vector vOccpiedTables = new java.util.Vector();
            Map<String, String> mapOccupiedTables = new HashMap<String, String>();
            int cnt = 0, cnt1 = 0;
            while (rsTables.next())
            {
                String tableName = rsTables.getString(1) + "(" + rsTables.getString(3) + ")";
                String status = rsTables.getString(2);
                vOccpiedTables.add(tableName);
                String color = "green";
                if (status.equalsIgnoreCase("occupied"))
                {
                    color = "red";
                }
                else if (status.equalsIgnoreCase("billed"))
                {
                    color = "blue";
                }
                mapOccupiedTables.put(tableName, color);
                cnt++;
                if (cnt % cntRows == 0)
                {
                    cnt1 = cnt1 + cntRows;
                    listOccupiedTables.add(mapOccupiedTables);
                    mapOccupiedTables = new HashMap<String, String>();
                }
            }
            if (cnt > cnt1)
            {
                listOccupiedTables.add(mapOccupiedTables);
            }

//            txtPanelTables.setEditorKit(new HTMLEditorKit());
//            String text="";
//            int k=0;
//            int m=cntRows;
//            for(int i=0;i<listOccupiedTables.size();i++)
//            {
//                Map mapTempTables=listOccupiedTables.get(i);
//                if(i>0)
//                {
//                    m=m+mapTempTables.size();
//                }
//                for(int j=k;j<m;j++)
//                {
//                    String color=mapTempTables.get(vOccpiedTables.elementAt(j).toString()).toString();                    
//                    text+= "<font size=\"4\" face=\"verdana\" color=\""+color+"\">"+vOccpiedTables.elementAt(j).toString()+"</font>";
//                    text+="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
//                }
//                text+="<br><br>";
//                k=m;
//                //m=m+cntRows;
//            }
//            String finalText="<html>"+text+"</html>";
//            txtPanelTables.setText(finalText);
            //txtPanelTables.setForeground(Color.red);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Fill Billed Tables
     */
    public void funFillBilledTables()
    {
        try
        {
            int cntRows = 0;
            lblProductName.setText("JPOS - Table Status  Billed Tables");
            /*String sql ="select a.strTableName,a.strStatus from tbltablemaster a,tblitemrtemp b "
                    + "where a.strTableNo=b.strTableNo";*/
            String sql = "select count(strTableName) from tbltablemaster where strStatus='Billed' order by strTableName";
            ResultSet rsTables = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            if (rsTables.next())
            {
                cntRows = rsTables.getInt(1);
            }
            if (cntRows >= 9)
            {
                cntRows = 9;
            }
            sql = "select strTableName,strStatus,intPaxNo from tbltablemaster where strStatus='Billed' order by strTableName";
            rsTables = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            List<Map> listBilledTables = new ArrayList();
            java.util.Vector vBilledTables = new java.util.Vector();
            Map<String, String> mapBilledTables = new HashMap<String, String>();
            int cnt = 0, cnt1 = 0;
            while (rsTables.next())
            {
                String tableName = rsTables.getString(1) + "(" + rsTables.getString(3) + ")";
                String status = rsTables.getString(2);
                vBilledTables.add(tableName);
                String color = "green";
                if (status.equalsIgnoreCase("Billed"))
                {
                    color = "blue";
                }
                mapBilledTables.put(tableName, color);
                cnt++;
                if (cnt % cntRows == 0)
                {
                    cnt1 = cnt1 + cntRows;
                    listBilledTables.add(mapBilledTables);
                    mapBilledTables = new HashMap<String, String>();
                }
            }
            if (cnt > cnt1)
            {
                listBilledTables.add(mapBilledTables);
            }

//            txtPanelTables.setEditorKit(new HTMLEditorKit());
//            String text="";
//            int k=0;
//            int m=cntRows;
//            for(int i=0;i<listBilledTables.size();i++)
//            {
//                Map mapTempTables=listBilledTables.get(i);
//                if(i>0)
//                {
//                    m=m+mapTempTables.size();
//                }
//                for(int j=k;j<m;j++)
//                {
//                    String color=mapTempTables.get(vBilledTables.elementAt(j).toString()).toString();                    
//                    text+= "<font size=\"4\" face=\"verdana\" color=\""+color+"\">"+vBilledTables.elementAt(j).toString()+"</font>";
//                    text+="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
//                }
//                text+="<br><br>";
//                k=m;
//                //m=m+cntRows;
//            }
//            String finalText="<html>"+text+"</html>";
//            txtPanelTables.setText(finalText);
            //txtPanelTables.setForeground(Color.red);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Fill Vacant Tables
     */
    public void funFillVacantTables()
    {
        try
        {
            int cntRows = 0;
            lblProductName.setText("JPOS - Table Status  Vacant Tables");
            /*String sql ="select a.strTableName,a.strStatus from tbltablemaster a,tblitemrtemp b "
                    + "where a.strTableNo=b.strTableNo";*/
            String sql = "select count(strTableName) from tbltablemaster where strStatus='Normal' order by strTableName";
            ResultSet rsTables = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            if (rsTables.next())
            {
                cntRows = rsTables.getInt(1);
            }
            if (cntRows >= 9)
            {
                cntRows = 9;
            }

            /*String sql ="select a.strTableName,a.strStatus from tbltablemaster a,tblitemrtemp b "
                    + "where a.strTableNo=b.strTableNo";*/
            sql = "select strTableName,strStatus,intPaxNo from tbltablemaster where strStatus='Normal' order by strTableName";
            rsTables = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            List<Map> listVacantTables = new ArrayList();
            java.util.Vector vVacantTables = new java.util.Vector();
            Map<String, String> mapVacantTables = new HashMap<String, String>();
            int cnt = 0, cnt1 = 0;
            while (rsTables.next())
            {
                String tableName = rsTables.getString(1) + "(" + rsTables.getString(3) + ")";
                String status = rsTables.getString(2);
                vVacantTables.add(tableName);
                String color = "green";
                mapVacantTables.put(tableName, color);
                cnt++;
                if (cnt % cntRows == 0)
                {
                    cnt1 = cnt1 + cntRows;
                    listVacantTables.add(mapVacantTables);
                    mapVacantTables = new HashMap<String, String>();
                }
            }
            if (cnt > cnt1)
            {
                listVacantTables.add(mapVacantTables);
            }
//            
//            txtPanelTables.setEditorKit(new HTMLEditorKit());
//            String text="";
//            int k=0;
//            int m=cntRows;
//            for(int i=0;i<listVacantTables.size();i++)
//            {
//                Map mapTempTables=listVacantTables.get(i);
//                if(i>0)
//                {
//                    m=m+mapTempTables.size();
//                }
//                for(int j=k;j<m;j++)
//                {
//                    String color=mapTempTables.get(vVacantTables.elementAt(j).toString()).toString();                    
//                    text+= "<font size=\"4\" face=\"verdana\" color=\""+color+"\">"+vVacantTables.elementAt(j).toString()+"</font>";
//                    text+="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
//                }
//                text+="<br><br>";
//                k=m;
//                //m=m+cntRows;
//            }
//            String finalText="<html>"+text+"</html>";
//            txtPanelTables.setText(finalText);
            //txtPanelTables.setForeground(Color.red);

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

        pnlheader = new javax.swing.JPanel();
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
        pnlBackGround = new JPanel()
        {
            public void paintComponent(Graphics g)
            {
                Image img = Toolkit.getDefaultToolkit().getImage(
                    getClass().getResource("/com/POSReport/images/imgBGJPOS.png"));
                g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
            }
        };  ;
        pnlMain = new javax.swing.JPanel();
        btnExit = new javax.swing.JButton();
        lblVacant = new javax.swing.JLabel();
        lbloccupied = new javax.swing.JLabel();
        lblBilled = new javax.swing.JLabel();
        cmbTableStatus = new javax.swing.JComboBox();
        lblTableStatus = new javax.swing.JLabel();
        lblAreaName = new javax.swing.JLabel();
        cmbAreaCombo = new javax.swing.JComboBox();
        jPanel1 = new javax.swing.JPanel();
        btnPrev = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jButton16 = new javax.swing.JButton();
        jButton17 = new javax.swing.JButton();
        jButton18 = new javax.swing.JButton();
        jButton19 = new javax.swing.JButton();
        jButton20 = new javax.swing.JButton();
        jButton21 = new javax.swing.JButton();
        jButton22 = new javax.swing.JButton();
        jButton23 = new javax.swing.JButton();
        jButton24 = new javax.swing.JButton();
        jButton25 = new javax.swing.JButton();
        jButton26 = new javax.swing.JButton();
        jButton27 = new javax.swing.JButton();
        jButton28 = new javax.swing.JButton();
        jButton29 = new javax.swing.JButton();
        jButton30 = new javax.swing.JButton();
        jButton31 = new javax.swing.JButton();
        jButton32 = new javax.swing.JButton();
        jButton33 = new javax.swing.JButton();
        jButton34 = new javax.swing.JButton();
        jButton35 = new javax.swing.JButton();
        jButton36 = new javax.swing.JButton();
        jButton37 = new javax.swing.JButton();
        jButton38 = new javax.swing.JButton();
        jButton39 = new javax.swing.JButton();
        jButton40 = new javax.swing.JButton();
        jButton41 = new javax.swing.JButton();
        jButton42 = new javax.swing.JButton();
        jButton43 = new javax.swing.JButton();
        jButton44 = new javax.swing.JButton();
        jButton45 = new javax.swing.JButton();
        jButton46 = new javax.swing.JButton();
        jButton47 = new javax.swing.JButton();
        jButton48 = new javax.swing.JButton();
        jButton49 = new javax.swing.JButton();
        jButton50 = new javax.swing.JButton();
        jButton51 = new javax.swing.JButton();
        jButton52 = new javax.swing.JButton();
        jButton53 = new javax.swing.JButton();
        jButton54 = new javax.swing.JButton();
        jButton55 = new javax.swing.JButton();
        jButton56 = new javax.swing.JButton();
        jButton57 = new javax.swing.JButton();
        jButton58 = new javax.swing.JButton();
        jButton59 = new javax.swing.JButton();
        jButton60 = new javax.swing.JButton();
        jButton61 = new javax.swing.JButton();
        jButton62 = new javax.swing.JButton();
        jButton63 = new javax.swing.JButton();
        jButton64 = new javax.swing.JButton();
        jButton65 = new javax.swing.JButton();
        jButton66 = new javax.swing.JButton();
        jButton67 = new javax.swing.JButton();
        jButton68 = new javax.swing.JButton();
        jButton69 = new javax.swing.JButton();
        jButton70 = new javax.swing.JButton();
        jButton71 = new javax.swing.JButton();
        jButton72 = new javax.swing.JButton();
        jButton73 = new javax.swing.JButton();
        jButton74 = new javax.swing.JButton();
        jButton75 = new javax.swing.JButton();
        jButton76 = new javax.swing.JButton();
        jButton77 = new javax.swing.JButton();
        jButton78 = new javax.swing.JButton();
        jButton79 = new javax.swing.JButton();
        jButton80 = new javax.swing.JButton();
        jButton81 = new javax.swing.JButton();
        jButton82 = new javax.swing.JButton();
        jButton83 = new javax.swing.JButton();
        jButton84 = new javax.swing.JButton();
        jButton85 = new javax.swing.JButton();
        jButton86 = new javax.swing.JButton();
        jButton87 = new javax.swing.JButton();
        jButton88 = new javax.swing.JButton();
        jButton89 = new javax.swing.JButton();
        lblPOSName = new javax.swing.JLabel();
        cmbPOSName = new javax.swing.JComboBox();
        lblTxtTotalPax = new javax.swing.JLabel();
        lblTotalPax = new javax.swing.JLabel();

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

        pnlheader.setBackground(new java.awt.Color(69, 164, 238));
        pnlheader.setLayout(new javax.swing.BoxLayout(pnlheader, javax.swing.BoxLayout.LINE_AXIS));

        lblProductName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblProductName.setForeground(new java.awt.Color(255, 255, 255));
        lblProductName.setText("SPOS -");
        pnlheader.add(lblProductName);

        lblModuleName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblModuleName.setForeground(new java.awt.Color(255, 255, 255));
        pnlheader.add(lblModuleName);

        lblformName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblformName.setForeground(new java.awt.Color(255, 255, 255));
        lblformName.setText("- Table Status");
        pnlheader.add(lblformName);
        pnlheader.add(filler4);
        pnlheader.add(filler5);

        lblPosName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblPosName.setForeground(new java.awt.Color(255, 255, 255));
        lblPosName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPosName.setMaximumSize(new java.awt.Dimension(321, 30));
        lblPosName.setMinimumSize(new java.awt.Dimension(321, 30));
        lblPosName.setPreferredSize(new java.awt.Dimension(321, 30));
        pnlheader.add(lblPosName);
        pnlheader.add(filler6);

        lblUserCode.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblUserCode.setForeground(new java.awt.Color(255, 255, 255));
        lblUserCode.setMaximumSize(new java.awt.Dimension(90, 30));
        lblUserCode.setMinimumSize(new java.awt.Dimension(90, 30));
        lblUserCode.setPreferredSize(new java.awt.Dimension(90, 30));
        pnlheader.add(lblUserCode);

        lblDate.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblDate.setForeground(new java.awt.Color(255, 255, 255));
        lblDate.setMaximumSize(new java.awt.Dimension(192, 30));
        lblDate.setMinimumSize(new java.awt.Dimension(192, 30));
        lblDate.setPreferredSize(new java.awt.Dimension(192, 30));
        pnlheader.add(lblDate);

        lblHOSign.setMaximumSize(new java.awt.Dimension(34, 30));
        lblHOSign.setMinimumSize(new java.awt.Dimension(34, 30));
        lblHOSign.setPreferredSize(new java.awt.Dimension(34, 30));
        pnlheader.add(lblHOSign);

        getContentPane().add(pnlheader, java.awt.BorderLayout.PAGE_START);

        pnlBackGround.setLayout(new java.awt.GridBagLayout());

        pnlMain.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        pnlMain.setMinimumSize(new java.awt.Dimension(800, 570));
        pnlMain.setOpaque(false);

        btnExit.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        btnExit.setForeground(new java.awt.Color(255, 255, 255));
        btnExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSReport/images/imgPOSChooser3.png"))); // NOI18N
        btnExit.setText("EXIT");
        btnExit.setToolTipText("Exit Window ");
        btnExit.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnExit.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnExitMouseClicked(evt);
            }
        });

        lblVacant.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        lblVacant.setForeground(new java.awt.Color(204, 204, 204));
        lblVacant.setText("VACANT");

        lbloccupied.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        lbloccupied.setForeground(new java.awt.Color(255, 0, 0));
        lbloccupied.setText("OCCUPIED");

        lblBilled.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        lblBilled.setForeground(new java.awt.Color(0, 0, 255));
        lblBilled.setText("BILLED");

        cmbTableStatus.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        cmbTableStatus.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "All", "Vacant", "Billed", "Occupied" }));

        lblTableStatus.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        lblTableStatus.setText("Table Status");

        lblAreaName.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        lblAreaName.setText("Area ");

        cmbAreaCombo.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        cmbAreaCombo.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                cmbAreaComboMouseClicked(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        btnPrev.setForeground(new java.awt.Color(255, 255, 255));
        btnPrev.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgBackButton1.png"))); // NOI18N
        btnPrev.setText("<<");
        btnPrev.setToolTipText("Previous");
        btnPrev.setEnabled(false);
        btnPrev.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPrev.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgBackButton2.png"))); // NOI18N
        btnPrev.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnPrevActionPerformed(evt);
            }
        });

        btnNext.setForeground(new java.awt.Color(255, 255, 255));
        btnNext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgBackButton1.png"))); // NOI18N
        btnNext.setText(">>");
        btnNext.setToolTipText("Next");
        btnNext.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNext.setIconTextGap(5);
        btnNext.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgBackButton2.png"))); // NOI18N
        btnNext.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnNextActionPerformed(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        jButton4.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton4.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        jButton5.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton5.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        jButton6.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton6.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        jButton7.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton7.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        jButton8.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton8.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        jButton9.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton9.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        jButton10.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton10.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        jButton11.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton11.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        jButton12.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton12.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        jButton13.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton13.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        jButton14.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton14.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        jButton15.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton15.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        jButton16.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton16.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        jButton17.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton17.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        jButton18.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton18.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        jButton19.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton19.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        jButton20.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton20.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        jButton21.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton21.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        jButton22.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton22.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        jButton23.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton23.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        jButton24.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton24.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        jButton25.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton25.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        jButton26.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton26.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        jButton27.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton27.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        jButton28.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton28.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        jButton29.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton29.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        jButton30.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton30.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        jButton31.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton31.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        jButton32.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton32.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        jButton33.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton33.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        jButton34.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton34.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        jButton35.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton35.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        jButton36.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton36.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        jButton37.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton37.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        jButton38.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton38.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        jButton39.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton39.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        jButton40.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton40.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        jButton41.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton41.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        jButton42.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton42.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        jButton43.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton43.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        jButton44.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton44.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        jButton45.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton45.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        jButton46.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton46.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        jButton47.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton47.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        jButton48.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton48.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        jButton49.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton49.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        jButton50.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton50.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        jButton51.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton51.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        jButton52.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton52.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        jButton53.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton53.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        jButton54.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton54.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        jButton55.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton55.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        jButton56.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton56.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        jButton57.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton57.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        jButton58.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton58.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        jButton59.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton59.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        jButton60.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton60.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        jButton61.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton61.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        jButton62.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton62.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        jButton63.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton63.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        jButton64.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton64.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        jButton65.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton65.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        jButton66.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton66.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        jButton67.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton67.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        jButton68.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton68.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        jButton69.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton69.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        jButton70.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton70.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        jButton71.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton71.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        jButton72.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton72.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        jButton73.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton73.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        jButton74.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton74.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        jButton75.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton75.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        jButton76.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton76.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        jButton77.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton77.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        jButton78.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton78.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        jButton79.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton79.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        jButton80.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton80.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        jButton81.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton81.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        jButton82.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton82.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        jButton83.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton83.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        jButton84.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton84.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        jButton85.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton85.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        jButton86.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton86.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        jButton87.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton87.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        jButton88.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton88.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        jButton89.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jButton89.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnPrev, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton16, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton17, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton18, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton19, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton20, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton21, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton22, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton23, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton24, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton25, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton26, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton27, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton28, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton29, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton30, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton31, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton32, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton33, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton34, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton35, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton36, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton37, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton38, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton39, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton40, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton41, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton42, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton43, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton44, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton45, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton46, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton47, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton48, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton49, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton50, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton51, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton52, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton53, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton54, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton55, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton56, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton57, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton58, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton59, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton60, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton61, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton62, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton63, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton64, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton65, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton66, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton67, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton68, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton69, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton70, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton71, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton72, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton73, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton74, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton75, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton76, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton77, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton78, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton79, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton80, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton81, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton82, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton83, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton84, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton85, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton86, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton87, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton88, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton89, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(10, 10, 10))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jButton1, jButton10, jButton11, jButton12, jButton13, jButton14, jButton15, jButton16, jButton17, jButton18, jButton19, jButton2, jButton20, jButton21, jButton22, jButton23, jButton24, jButton25, jButton26, jButton27, jButton28, jButton29, jButton3, jButton30, jButton31, jButton32, jButton33, jButton34, jButton35, jButton36, jButton37, jButton38, jButton39, jButton4, jButton40, jButton41, jButton42, jButton43, jButton44, jButton45, jButton46, jButton47, jButton48, jButton49, jButton5, jButton50, jButton51, jButton52, jButton53, jButton54, jButton55, jButton56, jButton57, jButton58, jButton59, jButton6, jButton60, jButton61, jButton62, jButton63, jButton64, jButton65, jButton66, jButton67, jButton68, jButton69, jButton7, jButton70, jButton71, jButton72, jButton73, jButton74, jButton75, jButton76, jButton77, jButton78, jButton79, jButton8, jButton80, jButton81, jButton82, jButton83, jButton84, jButton85, jButton86, jButton87, jButton88, jButton89, jButton9});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton53, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(11, 11, 11)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton66, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton67, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton68, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton69, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton70, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton71, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton72, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton73, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton74, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton75, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton76, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton77, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton79, javax.swing.GroupLayout.DEFAULT_SIZE, 62, Short.MAX_VALUE)
                            .addComponent(jButton80, javax.swing.GroupLayout.DEFAULT_SIZE, 62, Short.MAX_VALUE)
                            .addComponent(jButton81, javax.swing.GroupLayout.DEFAULT_SIZE, 62, Short.MAX_VALUE)
                            .addComponent(jButton82, javax.swing.GroupLayout.DEFAULT_SIZE, 62, Short.MAX_VALUE)
                            .addComponent(jButton83, javax.swing.GroupLayout.DEFAULT_SIZE, 62, Short.MAX_VALUE)
                            .addComponent(jButton84, javax.swing.GroupLayout.DEFAULT_SIZE, 62, Short.MAX_VALUE)
                            .addComponent(jButton85, javax.swing.GroupLayout.DEFAULT_SIZE, 62, Short.MAX_VALUE)
                            .addComponent(jButton86, javax.swing.GroupLayout.DEFAULT_SIZE, 62, Short.MAX_VALUE)
                            .addComponent(jButton87, javax.swing.GroupLayout.DEFAULT_SIZE, 62, Short.MAX_VALUE)
                            .addComponent(jButton88, javax.swing.GroupLayout.DEFAULT_SIZE, 62, Short.MAX_VALUE)
                            .addComponent(jButton89, javax.swing.GroupLayout.DEFAULT_SIZE, 62, Short.MAX_VALUE)
                            .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 62, Short.MAX_VALUE)
                                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 62, Short.MAX_VALUE)
                                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, 62, Short.MAX_VALUE)
                                    .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, 62, Short.MAX_VALUE)
                                    .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, 62, Short.MAX_VALUE)
                                    .addComponent(jButton7, javax.swing.GroupLayout.DEFAULT_SIZE, 62, Short.MAX_VALUE)
                                    .addComponent(jButton8, javax.swing.GroupLayout.DEFAULT_SIZE, 62, Short.MAX_VALUE)
                                    .addComponent(jButton9, javax.swing.GroupLayout.DEFAULT_SIZE, 62, Short.MAX_VALUE)
                                    .addComponent(jButton10, javax.swing.GroupLayout.DEFAULT_SIZE, 62, Short.MAX_VALUE)
                                    .addComponent(jButton11, javax.swing.GroupLayout.DEFAULT_SIZE, 62, Short.MAX_VALUE)
                                    .addComponent(jButton12, javax.swing.GroupLayout.DEFAULT_SIZE, 62, Short.MAX_VALUE)
                                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 62, Short.MAX_VALUE)
                                    .addComponent(btnPrev, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jButton23, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jButton16, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jButton17, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jButton18, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jButton19, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jButton20, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jButton21, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jButton22, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(jButton25, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton24, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButton27, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton26, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton28, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton29, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton30, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton31, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton32, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton33, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton34, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton35, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton36, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton37, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton38, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jButton39, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jButton41, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jButton42, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton43, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton44, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton45, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton46, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton47, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton48, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton49, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton50, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton51, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jButton40, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jButton52, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButton54, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jButton55, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton56, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton57, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton58, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton59, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton60, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton61, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton62, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton63, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton64, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton65, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton78, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jButton1, jButton10, jButton11, jButton12, jButton13, jButton14, jButton15, jButton16, jButton17, jButton18, jButton19, jButton2, jButton20, jButton21, jButton22, jButton23, jButton24, jButton25, jButton26, jButton27, jButton28, jButton29, jButton3, jButton30, jButton31, jButton32, jButton33, jButton34, jButton35, jButton36, jButton37, jButton38, jButton39, jButton4, jButton40, jButton41, jButton42, jButton43, jButton44, jButton45, jButton46, jButton47, jButton48, jButton49, jButton5, jButton50, jButton51, jButton52, jButton53, jButton54, jButton55, jButton56, jButton57, jButton58, jButton59, jButton6, jButton60, jButton61, jButton62, jButton63, jButton64, jButton65, jButton66, jButton67, jButton68, jButton69, jButton7, jButton70, jButton71, jButton72, jButton73, jButton74, jButton75, jButton76, jButton77, jButton78, jButton79, jButton8, jButton80, jButton81, jButton82, jButton83, jButton84, jButton85, jButton86, jButton87, jButton88, jButton89, jButton9});

        lblPOSName.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        lblPOSName.setText("POS Name");

        cmbPOSName.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N

        lblTxtTotalPax.setFont(new java.awt.Font("Trebuchet MS", 0, 24)); // NOI18N
        lblTxtTotalPax.setText("Total Pax");

        lblTotalPax.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N

        javax.swing.GroupLayout pnlMainLayout = new javax.swing.GroupLayout(pnlMain);
        pnlMain.setLayout(pnlMainLayout);
        pnlMainLayout.setHorizontalGroup(
            pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMainLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(lblPOSName)
                .addGap(18, 18, 18)
                .addComponent(cmbPOSName, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblTableStatus)
                .addGap(18, 18, 18)
                .addComponent(cmbTableStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(lblAreaName)
                .addGap(18, 18, 18)
                .addComponent(cmbAreaCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlMainLayout.createSequentialGroup()
                .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 802, Short.MAX_VALUE)
                    .addGroup(pnlMainLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lbloccupied, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(43, 43, 43)
                        .addComponent(lblVacant, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(37, 37, 37)
                        .addComponent(lblBilled, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(35, 35, 35)
                        .addComponent(lblTxtTotalPax)
                        .addGap(18, 18, 18)
                        .addComponent(lblTotalPax, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(66, 66, 66)
                        .addComponent(btnExit, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(2, 2, 2))
        );
        pnlMainLayout.setVerticalGroup(
            pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMainLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbTableStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTableStatus)
                    .addComponent(lblAreaName)
                    .addComponent(cmbAreaCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPOSName)
                    .addComponent(cmbPOSName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlMainLayout.createSequentialGroup()
                        .addComponent(btnExit, javax.swing.GroupLayout.PREFERRED_SIZE, 40, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(lblTotalPax, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblTxtTotalPax, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbloccupied, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblVacant, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblBilled, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))))
        );

        pnlMainLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {cmbAreaCombo, cmbPOSName, cmbTableStatus});

        pnlBackGround.add(pnlMain, new java.awt.GridBagConstraints());

        getContentPane().add(pnlBackGround, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnExitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnExitMouseClicked
        // TODO add your handling code here:
        dispose();
        clsGlobalVarClass.hmActiveForms.remove("TableStatusReport");
    }//GEN-LAST:event_btnExitMouseClicked

    private void formWindowClosed(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosed
    {//GEN-HEADEREND:event_formWindowClosed
        clsGlobalVarClass.hmActiveForms.remove("TableStatusReport");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosing
    {//GEN-HEADEREND:event_formWindowClosing
        clsGlobalVarClass.hmActiveForms.remove("TableStatusReport");
    }//GEN-LAST:event_formWindowClosing

    private void cmbAreaComboMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cmbAreaComboMouseClicked

    }//GEN-LAST:event_cmbAreaComboMouseClicked

    private void btnPrevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrevActionPerformed

        funPrevButtonPressed();
    }//GEN-LAST:event_btnPrevActionPerformed

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed

        funNextButtonPressed();
    }//GEN-LAST:event_btnNextActionPerformed

    private void ButtonClicked(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ButtonClicked
    {//GEN-HEADEREND:event_ButtonClicked
        
        JButton button = (JButton) evt.getSource();

        funButtonClicked(button);
    }//GEN-LAST:event_ButtonClicked

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
            java.util.logging.Logger.getLogger(frmTableStatusReport.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(frmTableStatusReport.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(frmTableStatusReport.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(frmTableStatusReport.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                new frmTableStatusReport().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnPrev;
    private javax.swing.JComboBox cmbAreaCombo;
    private javax.swing.JComboBox cmbPOSName;
    private javax.swing.JComboBox cmbTableStatus;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton19;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton20;
    private javax.swing.JButton jButton21;
    private javax.swing.JButton jButton22;
    private javax.swing.JButton jButton23;
    private javax.swing.JButton jButton24;
    private javax.swing.JButton jButton25;
    private javax.swing.JButton jButton26;
    private javax.swing.JButton jButton27;
    private javax.swing.JButton jButton28;
    private javax.swing.JButton jButton29;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton30;
    private javax.swing.JButton jButton31;
    private javax.swing.JButton jButton32;
    private javax.swing.JButton jButton33;
    private javax.swing.JButton jButton34;
    private javax.swing.JButton jButton35;
    private javax.swing.JButton jButton36;
    private javax.swing.JButton jButton37;
    private javax.swing.JButton jButton38;
    private javax.swing.JButton jButton39;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton40;
    private javax.swing.JButton jButton41;
    private javax.swing.JButton jButton42;
    private javax.swing.JButton jButton43;
    private javax.swing.JButton jButton44;
    private javax.swing.JButton jButton45;
    private javax.swing.JButton jButton46;
    private javax.swing.JButton jButton47;
    private javax.swing.JButton jButton48;
    private javax.swing.JButton jButton49;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton50;
    private javax.swing.JButton jButton51;
    private javax.swing.JButton jButton52;
    private javax.swing.JButton jButton53;
    private javax.swing.JButton jButton54;
    private javax.swing.JButton jButton55;
    private javax.swing.JButton jButton56;
    private javax.swing.JButton jButton57;
    private javax.swing.JButton jButton58;
    private javax.swing.JButton jButton59;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton60;
    private javax.swing.JButton jButton61;
    private javax.swing.JButton jButton62;
    private javax.swing.JButton jButton63;
    private javax.swing.JButton jButton64;
    private javax.swing.JButton jButton65;
    private javax.swing.JButton jButton66;
    private javax.swing.JButton jButton67;
    private javax.swing.JButton jButton68;
    private javax.swing.JButton jButton69;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton70;
    private javax.swing.JButton jButton71;
    private javax.swing.JButton jButton72;
    private javax.swing.JButton jButton73;
    private javax.swing.JButton jButton74;
    private javax.swing.JButton jButton75;
    private javax.swing.JButton jButton76;
    private javax.swing.JButton jButton77;
    private javax.swing.JButton jButton78;
    private javax.swing.JButton jButton79;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton80;
    private javax.swing.JButton jButton81;
    private javax.swing.JButton jButton82;
    private javax.swing.JButton jButton83;
    private javax.swing.JButton jButton84;
    private javax.swing.JButton jButton85;
    private javax.swing.JButton jButton86;
    private javax.swing.JButton jButton87;
    private javax.swing.JButton jButton88;
    private javax.swing.JButton jButton89;
    private javax.swing.JButton jButton9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblAreaName;
    private javax.swing.JLabel lblBilled;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblPOSName;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblTableStatus;
    private javax.swing.JLabel lblTotalPax;
    private javax.swing.JLabel lblTxtTotalPax;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblVacant;
    private javax.swing.JLabel lblformName;
    private javax.swing.JLabel lbloccupied;
    private javax.swing.JPanel pnlBackGround;
    private javax.swing.JPanel pnlMain;
    private javax.swing.JPanel pnlheader;
    // End of variables declaration//GEN-END:variables

    private void funFillAreaCombo() throws Exception
    {
        String sql = "select a.strAreaName,a.strAreaCode from tblareamaster a";
        ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
        while (rs.next())
        {
            cmbAreaCombo.addItem(rs.getString(1) + "                                               !" + rs.getString(2));
        }
        rs.close();
    }

    public void funSetArea(String selAreaCode)
    {
        String posName = cmbPOSName.getSelectedItem().toString().split("!")[0].trim();
        StringBuilder sb = new StringBuilder(posName);
        String posCode = sb.substring(sb.lastIndexOf(" "), sb.length()).trim();
        try
        {
            objUtility = new clsUtility();

            clsAreaCode = selAreaCode;

            clsAreaName = "NA";
            String sql = "select strAreaName from tblareamaster "
                    + "where strAreaCode='" + clsAreaCode + "'";
            ResultSet rsAreaInfo = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            if (rsAreaInfo.next())
            {
                clsAreaName = rsAreaInfo.getString(1);
            }
            rsAreaInfo.close();

//            cmbAreaCombo.addItem(clsAreaName);
            hmTable.clear();
            hmTableSeq.clear();

            String tableStatus = cmbTableStatus.getSelectedItem().toString().split("!")[0].trim();
            if (tableStatus.equalsIgnoreCase("Vacant"))
            {
                tableStatus = "Normal";
            }
            if (clsAreaName.equalsIgnoreCase("All"))
            {
                if ((tableStatus.equalsIgnoreCase("All")))
                {

                    sql = "select strTableNo,strTableName,intSequence from tbltablemaster "
                            + "where (strPOSCode='" + posCode + "' or strPOSCode='All') "
                            + "and strOperational='Y' order by intSequence ";
                }
                else
                {
                    sql = "select strTableNo,strTableName,intSequence from tbltablemaster "
                            + "where (strPOSCode='" + posCode + "' or strPOSCode='All') "
                            + "and strOperational='Y'  and strStatus='" + tableStatus + "' order by intSequence ";
                }
            }
            else
            {
                if ((tableStatus.equalsIgnoreCase("All")))
                {
                    sql = "select strTableNo,strTableName,intSequence from tbltablemaster "
                            + "where strAreaCode='" + clsAreaCode + "' "
                            + "and (strPOSCode='" + posCode + "' or strPOSCode='All') "
                            + "and strOperational='Y' order by intSequence ";
                }
                else
                {
                    sql = "select strTableNo,strTableName,intSequence from tbltablemaster "
                            + "where strAreaCode='" + clsAreaCode + "' "
                            + "and (strPOSCode='" + posCode + "' or strPOSCode='All') "
                            + "and strOperational='Y' and strStatus='" + tableStatus + "' order by intSequence ";
                }
            }

            //System.out.println(sql);
            ResultSet rsTableCode = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            while (rsTableCode.next())
            {
                //vTableNo.add(rsTableCode.getString(1));
                //vTableName.add(rsTableCode.getString(2));
                hmTable.put(rsTableCode.getString(2).toUpperCase(), rsTableCode.getString(1));
                hmTableSeq.put(rsTableCode.getString(1) + "!" + rsTableCode.getString(2), rsTableCode.getInt(3));
            }
            rsTableCode.close();
            //funLoadTables(0, vTableNo.size());
            funLoadTables(0, hmTableSeq.size());
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    public void funInitTables() throws Exception
    {
        String posName = cmbPOSName.getSelectedItem().toString();
        StringBuilder sb = new StringBuilder(posName);
        String posCode = sb.substring(sb.lastIndexOf(" "), sb.length()).trim();
        hmTable.clear();
        hmTableSeq.clear();
        String sql = "";
        if (clsGlobalVarClass.gCMSIntegrationYN)
        {
            if (clsGlobalVarClass.gTreatMemberAsTable)
            {
                sql = "select strTableNo,strTableName from tbltablemaster "
                        + " where (strPOSCode='" + posCode + "' or strPOSCode='All') "
                        + " and strOperational='Y' and strStatus!='Normal' "
                        + " order by strTableName";
            }
            else
            {
                sql = "select strTableNo,strTableName,intSequence from tbltablemaster "
                        + " where (strPOSCode='" + posCode + "' or strPOSCode='All') "
                        + " and strOperational='Y' "
                        + " order by intSequence";
            }
        }
        else
        {
            sql = "select strTableNo,strTableName,intSequence from tbltablemaster "
                    + " where (strPOSCode='" + posCode + "' or strPOSCode='All') "
                    + " and strOperational='Y' "
                    + " order by intSequence";
        }
        //System.out.println(sql);
        ResultSet rsTableInfo = clsGlobalVarClass.dbMysql.executeResultSet(sql);
        while (rsTableInfo.next())
        {
            hmTable.put(rsTableInfo.getString(2).toUpperCase(), rsTableInfo.getString(1));
            hmTableSeq.put(rsTableInfo.getString(1) + "!" + rsTableInfo.getString(2), rsTableInfo.getInt(3));
        }
        rsTableInfo.close();

    }

    public void funLoadTables(int startIndex, int totalSize)
    {
        try
        {

            flgTableSelection = true;
            fieldSelected = "Table";
            int cntIndex = 0;
            if (startIndex == 0)
            {
                btnPrev.setEnabled(false);
            }

            JButton btnTableArray[] = new JButton[]
            {
                jButton1, jButton2, jButton3, jButton4, jButton5, jButton6, jButton7, jButton8, jButton9, jButton10,
                jButton11, jButton12, jButton13, jButton14, jButton15, jButton16, jButton17, jButton18, jButton19, jButton20,
                jButton21, jButton22, jButton23, jButton24, jButton25, jButton26, jButton27, jButton28, jButton29, jButton30,
                jButton31, jButton32, jButton33, jButton34, jButton35, jButton36, jButton37, jButton38, jButton39, jButton40,
                jButton41, jButton42, jButton43, jButton44, jButton45, jButton46, jButton47, jButton48, jButton49, jButton50,
                jButton51, jButton52, jButton53, jButton54, jButton55, jButton56, jButton57, jButton58, jButton59, jButton60,
                jButton61, jButton62, jButton63, jButton64, jButton65, jButton66, jButton67, jButton68, jButton69, jButton70,
                jButton71, jButton72, jButton73, jButton74, jButton75, jButton76, jButton77, jButton78, jButton79, jButton80,
                jButton81, jButton82, jButton83, jButton84, jButton85, jButton86, jButton87, jButton88, jButton89,

            };

            for (int cntTable = 0; cntTable < btnTableArray.length; cntTable++)
            {
                btnTableArray[cntTable].setForeground(Color.black);
                btnTableArray[cntTable].setText("");
                btnTableArray[cntTable].setIcon(null);
            }

            hmTableSeq = clsGlobalVarClass.funSortMapOnValues(hmTableSeq);

            Object[] arrObjTables = hmTableSeq.entrySet().toArray();
            int totPax = 0;
            for (int cntTable = startIndex; cntTable < totalSize; cntTable++)
            {
                if (cntTable == totalSize)
                {
                    break;
                }

                String tblInfo = arrObjTables[cntTable].toString().split("=")[0];
                String tblNo = tblInfo.split("!")[0];
                String tblName = tblInfo.split("!")[1];

                sql = "select strTableNo,strStatus,intPaxNo from tbltablemaster "
                        + " where strTableNo='" + tblNo + "' "
                        + " and strOperational='Y' "
                        + " order by intSequence";

                ResultSet rsTableInfo = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                rsTableInfo.next();
                String status = rsTableInfo.getString(2);
                int pax = rsTableInfo.getInt(3);

                if (cntIndex < 89)
                {
                    if (status.equals("Occupied"))
                    {
                        btnTableArray[cntIndex].setBackground(Color.red);
                        btnTableArray[cntIndex].setForeground(Color.white);
                        String timeDiffInFirstKOTAndCurrentTime = funGetTimeDiffInFirstKOTAndCurrentTime(tblNo);
                        if (timeDiffInFirstKOTAndCurrentTime.startsWith("-"))
                        {
                            timeDiffInFirstKOTAndCurrentTime = "";
                        }
                        btnTableArray[cntIndex].setText("<html><h5>" + tblName + "</h5>" + pax + "<br>" + timeDiffInFirstKOTAndCurrentTime + "</html>");

                        totPax = totPax + pax;
                    }
                    else if (status.equals("Billed"))
                    {
                        btnTableArray[cntIndex].setBackground(Color.blue);
                        btnTableArray[cntIndex].setForeground(Color.white);
                        String timeDiffInLastBilledAndCurrentTime = funGetTimeDiffInBilledAndCurrentTime(tblNo);
                        if (timeDiffInLastBilledAndCurrentTime.startsWith("-"))
                        {
                            timeDiffInLastBilledAndCurrentTime = "";
                        }
                        btnTableArray[cntIndex].setText("<html><h5>" + tblName + "</h5>" + pax + "<br>" + timeDiffInLastBilledAndCurrentTime + "</html>");
                    }
                    else if (status.equals("Normal"))
                    {
                        btnTableArray[cntIndex].setBackground(Color.lightGray);
                        btnTableArray[cntIndex].setForeground(Color.black);
                        btnTableArray[cntIndex].setText("<html><h5>" + tblName + "</h5>" + pax + "</html>");
                    }
                    else if (status.equals("Reserve"))
                    {
                        btnTableArray[cntIndex].setBackground(Color.green);
                        btnTableArray[cntIndex].setForeground(Color.black);
                        btnTableArray[cntIndex].setText("<html><h5>" + tblName + "</h5><br>" + pax + "</html>");
                    }
                    btnTableArray[cntIndex].setEnabled(true);
                    cntIndex++;
                }
                rsTableInfo.close();

            }
            lblTotalPax.setText(String.valueOf(totPax));
            for (int cntTable1 = cntIndex; cntTable1 < 89; cntTable1++)
            {
                btnTableArray[cntTable1].setEnabled(false);
            }
            if (totalSize > 89)
            {
                btnNext.setEnabled(true);
            }
            else
            {
                btnNext.setEnabled(false);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private String funGetTimeDiffInFirstKOTAndCurrentTime(String tableNo)
    {
        String timeDiffInFirstKOTAndCurrentTime = "";
        try
        {
            String sqlKot = "select TIME_FORMAT(TIMEDIFF(CURRENT_TIME(),time(dteDateCreated)),'%i:%s'),strKOTNo "
                    + "from tblitemrtemp  "
                    + "where strTableNo='" + tableNo + "' "
                    + "group by strKOTNo asc "
                    + "limit 1 ";
            ResultSet rsKOTTime = clsGlobalVarClass.dbMysql.executeResultSet(sqlKot);
            if (rsKOTTime.next())
            {
                timeDiffInFirstKOTAndCurrentTime = rsKOTTime.getString(1);
            }
            rsKOTTime.close();
        }
        catch (Exception e)
        {

            e.printStackTrace();
        }
        finally
        {
            return timeDiffInFirstKOTAndCurrentTime;
        }
    }

    private String funGetTimeDiffInBilledAndCurrentTime(String tableNo)
    {
        String timeDiffInLastBilledAndCurrentTime = "";
        try
        {
            String sqlKot = "select TIME_FORMAT(TIMEDIFF(CURRENT_TIME(),time(dteBillDate)),'%i:%s'),a.strBillNo "
                    + "from tblbillhd a "
                    + "where date(a.dteBillDate)='" + clsGlobalVarClass.gPOSOnlyDateForTransaction + "' "
                    + "and a.strTableNo='" + tableNo + "' "
                    + "and a.strBillNo not in(select strBillNo from tblbillsettlementdtl) "
                    + "order by a.dteBillDate desc "
                    + "limit 1; ";
            ResultSet rsKOTTime = clsGlobalVarClass.dbMysql.executeResultSet(sqlKot);
            if (rsKOTTime.next())
            {
                timeDiffInLastBilledAndCurrentTime = rsKOTTime.getString(1);
            }
            rsKOTTime.close();
        }
        catch (Exception e)
        {
//            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
        finally
        {
            return timeDiffInLastBilledAndCurrentTime;
        }
    }

    public void funForAreaNameChanged()
    {
        String areaCode = cmbAreaCombo.getSelectedItem().toString().split("!")[1].trim();
        funSetArea(areaCode);
    }

    public void funFillData()
    {
        try
        {

            String areaCode = cmbAreaCombo.getSelectedItem().toString().split("!")[1].trim();

            String tableStatus = cmbTableStatus.getSelectedItem().toString().split("!")[0].trim();
            if (tableStatus.equalsIgnoreCase("All"))
            {
                funInitTables();
                funLoadTables(0, hmTable.size());
            }

            if (tableStatus.equalsIgnoreCase("Vacant"))
            {
                funForAll(tableStatus);
                funLoadTables(0, hmTable.size());
            }
            if (tableStatus.equalsIgnoreCase("Billed"))
            {
                funForAll(tableStatus);
                funLoadTables(0, hmTable.size());
            }
            if (tableStatus.equalsIgnoreCase("Occupied"))
            {
                funForAll(tableStatus);
                funLoadTables(0, hmTable.size());
            }

            funSetAreaOnTableStatusChanged(areaCode);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public void funCmbPosNameFillData()
    {
        try
        {

            String areaCode = cmbAreaCombo.getSelectedItem().toString().split("!")[1].trim();

            String tableStatus = cmbTableStatus.getSelectedItem().toString().split("!")[0].trim();
            if (tableStatus.equalsIgnoreCase("All"))
            {
                funInitTables();
                funLoadTables(0, hmTable.size());
            }

            if (tableStatus.equalsIgnoreCase("Vacant"))
            {
                funForAll(tableStatus);
                funLoadTables(0, hmTable.size());
            }
            if (tableStatus.equalsIgnoreCase("Billed"))
            {
                funForAll(tableStatus);
                funLoadTables(0, hmTable.size());
            }
            if (tableStatus.equalsIgnoreCase("Occupied"))
            {
                funForAll(tableStatus);
                funLoadTables(0, hmTable.size());
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public void funForAll(String tableStatus)
    {
        hmTable = new HashMap<String, String>();
        hmTableSeq = new HashMap<String, Integer>();
        hmTable.clear();
        hmTableSeq.clear();
        String sql = "";
        try
        {
            if (tableStatus.equalsIgnoreCase("Vacant"))
            {
                sql = "SELECT strTableNo,strTableName,intSequence,strStatus"
                        + " FROM tbltablemaster"
                        + " WHERE strStatus='Normal'"
                        + " ORDER BY strTableName";

            }
            else if (tableStatus.equalsIgnoreCase("Billed"))
            {
                sql = "SELECT strTableNo,strTableName,intSequence,strStatus"
                        + " FROM tbltablemaster"
                        + " WHERE strStatus='Billed'"
                        + " ORDER BY strTableName";

            }
            else
            {
                sql = "SELECT strTableNo,strTableName,intSequence,strStatus"
                        + " FROM tbltablemaster"
                        + " WHERE strStatus='Occupied'"
                        + " ORDER BY strTableName";
            }
            ResultSet rsTableInfo = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            while (rsTableInfo.next())
            {
                hmTable.put(rsTableInfo.getString(2).toUpperCase(), rsTableInfo.getString(1));
                hmTableSeq.put(rsTableInfo.getString(1) + "!" + rsTableInfo.getString(2), rsTableInfo.getInt(3));
            }
            rsTableInfo.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void funNextButtonPressed()
    {
        cntNavigate++;
        int tableSize = cntNavigate * 89;
        int resMod = hmTable.size() % tableSize;
        int resDiv = hmTable.size() / 89;
        int totalSize = tableSize + 89;
        tblStartIndex = tableSize;
        if (hmTable.size() < totalSize)
        {
            funLoadTables(tableSize, hmTable.size());
        }
        else
        {
            funLoadTables(tableSize, totalSize);
        }
        btnPrev.setEnabled(true);
        if (resDiv == cntNavigate)
        {
            btnNext.setEnabled(false);
        }

    }

    private void funPrevButtonPressed()
    {
        cntNavigate--;
        if (cntNavigate == 0)
        {
            btnPrev.setEnabled(false);
            btnNext.setEnabled(true);
            tblStartIndex = 0;
            funLoadTables(0, hmTable.size());
        }
        else
        {
            int tableSize = cntNavigate * 89;
            int totalSize = tableSize + 89;
            tblStartIndex = tableSize;
            funLoadTables(tableSize, totalSize);
        }

    }

    public void funFillPOSNameCombo()
    {
        try
        {

            sql = "select strPOSCode,strPOSName from tblposmaster ";
            ResultSet rsPOS = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            while (rsPOS.next())
            {
                cmbPOSName.addItem(rsPOS.getString(2) + "                                                                " + rsPOS.getString(1));
                if (clsGlobalVarClass.gPOSCode.equals(rsPOS.getString(1)))
                {
                    cmbPOSName.setSelectedItem(rsPOS.getString(2) + "                                                                " + rsPOS.getString(1));
                }
            }
            rsPOS.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void funSetAreaOnTableStatusChanged(String selAreaCode)
    {
        String posName = cmbPOSName.getSelectedItem().toString().split("!")[0].trim();
        StringBuilder sb = new StringBuilder(posName);
        String posCode = sb.substring(sb.lastIndexOf(" "), sb.length()).trim();
        try
        {
            objUtility = new clsUtility();

            clsAreaCode = selAreaCode;

            clsAreaName = "NA";
            String sql = "select strAreaName from tblareamaster "
                    + "where strAreaCode='" + clsAreaCode + "'";
            ResultSet rsAreaInfo = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            if (rsAreaInfo.next())
            {
                clsAreaName = rsAreaInfo.getString(1);
            }
            rsAreaInfo.close();

//            cmbAreaCombo.addItem(clsAreaName);
            hmTable.clear();
            hmTableSeq.clear();

            String tableStatus = cmbTableStatus.getSelectedItem().toString().split("!")[0].trim();
            if (tableStatus.equalsIgnoreCase("Vacant"))
            {
                tableStatus = "Normal";
            }
            if ((tableStatus.equalsIgnoreCase("All")))
            {
                if (!clsAreaName.equalsIgnoreCase("All"))
                {
                    sql = "select strTableNo,strTableName,intSequence from tbltablemaster "
                            + "where strAreaCode='" + clsAreaCode + "' "
                            + "and (strPOSCode='" + posCode + "' or strPOSCode='All') "
                            + "and strOperational='Y' order by intSequence ";
                }
                else
                {
                    sql = "select strTableNo,strTableName,intSequence from tbltablemaster "
                            + "where (strPOSCode='" + posCode + "' or strPOSCode='All') "
                            + "and strOperational='Y' order by intSequence ";
                }
            }

            else
            {

                if (clsAreaName.equalsIgnoreCase("All"))
                {
                    sql = "select strTableNo,strTableName,intSequence from tbltablemaster "
                            + "where (strPOSCode='" + posCode + "' or strPOSCode='All') "
                            + "and strOperational='Y'  and strStatus='" + tableStatus + "' order by intSequence ";
                }
                else
                {
                    sql = "select strTableNo,strTableName,intSequence from tbltablemaster "
                            + "where strAreaCode='" + clsAreaCode + "' "
                            + "and (strPOSCode='" + posCode + "' or strPOSCode='All') "
                            + "and strOperational='Y' and strStatus='" + tableStatus + "' order by intSequence ";
                }
            }

            //System.out.println(sql);
            ResultSet rsTableCode = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            while (rsTableCode.next())
            {
                //vTableNo.add(rsTableCode.getString(1));
                //vTableName.add(rsTableCode.getString(2));
                hmTable.put(rsTableCode.getString(2).toUpperCase(), rsTableCode.getString(1));
                hmTableSeq.put(rsTableCode.getString(1) + "!" + rsTableCode.getString(2), rsTableCode.getInt(3));
            }
            rsTableCode.close();
            //funLoadTables(0, vTableNo.size());
            funLoadTables(0, hmTableSeq.size());
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    private void funButtonClicked(JButton button)
    {
        if (button.getBackground() == Color.red)//occupied//busy
        {
            String tableName = button.getText().split("<h5>")[1].split("</h5>")[0].toUpperCase();
            String tableNo = hmTable.get(tableName);
            // System.out.println(tableName + " \n" + tableNo);

            clsKOTGeneration objKOTGeneration = new clsKOTGeneration();
            objKOTGeneration.funCkeckKotTextFile(tableNo, "", "N","TableStatusView");

        }
    }
}
