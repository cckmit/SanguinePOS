/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSTransaction.view;

import com.POSGlobal.controller.clsBillItemDtl;
import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.view.frmOkPopUp;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.AbstractListModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Timer;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;

public class frmKDSBookAndProcess extends javax.swing.JFrame
{

    private JScrollPane scrollPaneArray[];
    private JList listViewArray[];
    private LinkedHashMap<String, ArrayList<clsBillItemDtl>> mapBillHd;
    private LinkedHashMap<String, ArrayList<clsBillItemDtl>> mapCountBillSize;
    private ArrayList<ArrayList<clsBillItemDtl>> listOfBills;
    private int navigatorNew = 0;
    private int navigator = 0;
    //private String gBillNo="";    
    private int startIndex = 0;
    private int endIndex = 0;
    private ArrayList<String> listOfBillsToBeProcess;
    //private String gBillDateTime="";
    //private final JLabel lblBillNoArray[];
    private final JLabel[] lblBillDelayArray;

    public frmKDSBookAndProcess()
    {
        ////////////////////////////
        initComponents();
        scrollPaneArray = new JScrollPane[]
        {
            scrollPane8, scrollPane7, scrollPane6, scrollPane5, scrollPane4, scrollPane3, scrollPane2, scrollPane1
        };
        listViewArray = new JList[]
        {
            list8, list7, list6, list5, list4, list3, list2, list1
        };
//        lblBillNoArray=new JLabel[]{lblBillNo8,lblBillNo7,lblBillNo6,lblBillNo5,lblBillNo4,lblBillNo3,lblBillNo2,lblBillNo1};
        lblBillDelayArray = new JLabel[]
        {
            lblBillDelay8, lblBillDelay7, lblBillDelay6, lblBillDelay5, lblBillDelay4, lblBillDelay3, lblBillDelay2, lblBillDelay1
        };
        mapBillHd = new LinkedHashMap();
        mapCountBillSize = new LinkedHashMap();
        listOfBills = new ArrayList<ArrayList<clsBillItemDtl>>();
        listOfBillsToBeProcess = new ArrayList<String>();

        funRefreshForm();
        funSetBillDelayTimer();

        Timer timer = new Timer(1000, new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                int oldBillSize = mapBillHd.size();
                int newBillSize = funGetNewBillSize();
//               System.out.println("old-->"+oldBillSize);
//               System.out.println("new-->"+newBillSize);
                if (oldBillSize != newBillSize)
                {
                    funRefreshForm();
                }
            }

            private int funGetNewBillSize()
            {
                try
                {
                    String sqlBillDtl = " SELECT a.strBillNo,a.strItemCode,a.strItemName,a.dblRate,a.dblQuantity,a.dblAmount,a.tmeOrderProcessing\n"
                            + " FROM tblbilldtl a\n"
                            + " where a.strBillNo not in(select strDocNo from tblkdsprocess where strBP='P' and strKDSName='BILL' ) "
                            + " GROUP BY a.strBillNo,a.strKOTNo,a.strItemCode\n"
                            + " ORDER BY a.dteBillDate desc,time(a.dteBillDate) desc  ";
                    //System.out.println("total bills-->"+sqlBillDtl);
                    ResultSet resultSet = clsGlobalVarClass.dbMysql.executeResultSet(sqlBillDtl);

                    mapCountBillSize.clear();
                    while (resultSet.next())
                    {
                        clsBillItemDtl billItemDtl = new clsBillItemDtl();

                        String billNo = resultSet.getString(1);
                        billItemDtl.setBillNo(billNo);
                        billItemDtl.setItemCode(resultSet.getString(2));
                        billItemDtl.setItemName(resultSet.getString(3));
                        billItemDtl.setRate(resultSet.getDouble(4));
                        billItemDtl.setQuantity(resultSet.getDouble(5));
                        billItemDtl.setAmount(resultSet.getDouble(6));

                        if (mapCountBillSize.containsKey(billNo))
                        {
                            mapCountBillSize.get(billNo).add(billItemDtl);
                        }
                        else
                        {
                            ArrayList<clsBillItemDtl> listBillItemDtl = new ArrayList<clsBillItemDtl>();

                            listBillItemDtl.add(billItemDtl);

                            mapCountBillSize.put(billNo, listBillItemDtl);
                        }
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                return mapCountBillSize.size();
            }
        });
        timer.setRepeats(true);
        timer.setCoalesce(true);
        timer.setInitialDelay(0);
        timer.start();
    }

    private void funSetCustomListCellRenderer()
    {
        for (int i = 0; i < listViewArray.length; i++)
        {
            listViewArray[i].setCellRenderer(new MyCellRenderer());
        }
    }

    private void funOldButtonClicked()
    {
        navigator++;
        btnNew.setEnabled(true);
        endIndex = listOfBills.size() - (navigator * 8) - 1;
        if ((listOfBills.size() - (navigator * 8) - 1) == 0)
        {
            btnOld.setEnabled(false);
        }
        if (endIndex > 7)
        {
            funLoadScrollPanes(0, 7);
        }
        else
        {
            btnOld.setEnabled(false);
            funLoadScrollPanes(0, endIndex);
        }
    }

    private void funNewButtonClicked()
    {
        navigator--;

        btnOld.setEnabled(true);
        if (navigator == 0)
        {
            btnNew.setEnabled(false);
        }

        funLoadScrollPanes(0, 7);
    }

    private void funButtonOrderProcessClicked()
    {
        try
        {
            StringBuilder sqlBillOrderProcess = new StringBuilder();

            sqlBillOrderProcess.append("delete from tblkdsprocess "
                    + "where strKDSName='BILL' "
                    + "and strDocNo IN ");
            for (int i = 0; i < listOfBillsToBeProcess.size(); i++)
            {
                if (i == 0)
                {
                    sqlBillOrderProcess.append("('" + listOfBillsToBeProcess.get(i) + "'");
                }
                else
                {
                    sqlBillOrderProcess.append(",'" + listOfBillsToBeProcess.get(i) + "'");
                }
            }
            sqlBillOrderProcess.append(")");
            clsGlobalVarClass.dbMysql.execute(sqlBillOrderProcess.toString());

            sqlBillOrderProcess.setLength(0);
            sqlBillOrderProcess.append("insert into tblkdsprocess values");
            for (int i = 0; i < listOfBillsToBeProcess.size(); i++)
            {
                if (i == 0)
                {
                    sqlBillOrderProcess.append("('" + listOfBillsToBeProcess.get(i) + "','P','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.gUserCode + "','BILL','','','','" + clsGlobalVarClass.getCurrentDateTime() + "' )");
                }
                else
                {
                    sqlBillOrderProcess.append(",('" + listOfBillsToBeProcess.get(i) + "','P','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.gUserCode + "','BILL','','','','" + clsGlobalVarClass.getCurrentDateTime() + "' )");
                }
            }

            clsGlobalVarClass.dbMysql.execute(sqlBillOrderProcess.toString());
            listOfBillsToBeProcess.clear();

            new frmOkPopUp(null, "Order Process Successfully.", "Successfull", 3).setVisible(true);

            funRefreshForm();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void funRefreshForm()
    {

        funResetDefault();
        ////////////////////////////
        fumLoadMapBillHd();
        funLoadBillArrayList();
        if (mapBillHd.size() > 7)
        {
            if (mapBillHd.size() > 8)
            {
                btnOld.setEnabled(true);
            }
            funLoadScrollPanes(0, 7);
        }
        else if (mapBillHd.size() > 0)
        {
            funLoadScrollPanes(0, mapBillHd.size() - 1);
        }

        System.gc();
    }

    private String funGetBillTime(String billDateTime)
    {
        SimpleDateFormat hhmmssTimeFormat = new SimpleDateFormat("HH:mm:ss");

        String hhmmssTime = hhmmssTimeFormat.format(new Date(billDateTime));

        System.out.println("" + billDateTime + "\t" + hhmmssTime);

        return hhmmssTime;
    }

    private void funSetBillDelayTimer()
    {
        final SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        final StringBuilder displayDelayTime = new StringBuilder();

        Timer timer = new Timer(1000, new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    Date currentDate = new Date();
                    Date currDate = df.parse(df.format(currentDate));

                    int ch = currDate.getHours();
                    int cm = currDate.getMinutes();
                    int cs = currDate.getSeconds();
                    int currentSeconds = (ch * 3600) + (cm * 60) + cs;

                    for (int i = 0; i < 8; i++)
                    {
                        JScrollPane scrollPane = scrollPaneArray[i];
                        if (scrollPane.isVisible())
                        {

                            Date delay = df.parse(listOfBills.get((navigator * 8) + i).get(0).getBillDateTime());
                            int dh = delay.getHours();
                            int dm = delay.getMinutes();
                            int ds = delay.getSeconds();
                            int delaySeconds = (dh * 3600) + (dm * 60) + ds;

                            int differenceSeconds = 0;
                            if (currDate.getTime() > delay.getTime())
                            {
                                differenceSeconds = currentSeconds - delaySeconds;
                            }
                            else
                            {
                                differenceSeconds = delaySeconds - currentSeconds;
                            }
                            int hh = differenceSeconds / 3600;
                            differenceSeconds = differenceSeconds % 3600;

                            int mm = differenceSeconds / 60;
                            differenceSeconds = differenceSeconds % 60;

                            int ss = differenceSeconds;

                            displayDelayTime.setLength(0);
                            if (hh > 0)
                            {
                                displayDelayTime.append(hh + ":");
                            }
                            if (mm > 0)
                            {
                                displayDelayTime.append(mm + ":");
                            }
                            displayDelayTime.append(ss);

                            lblBillDelayArray[i].setText(displayDelayTime.toString());
                        }
                    }
                }
                catch (ParseException pe)
                {
                    pe.printStackTrace();
                }
            }
        });
        timer.setRepeats(true);
        timer.setCoalesce(true);
        timer.setInitialDelay(0);
        timer.start();
    }

    private class MyCellRenderer extends DefaultListCellRenderer
    {

//        final static ImageIcon longIcon = new ImageIcon("long.gif");
//        final static ImageIcon shortIcon = new ImageIcon("short.gif");

        /* This is the only method defined by ListCellRenderer.  We just
         * reconfigure the Jlabel each time we're called.
         */
        public Component getListCellRendererComponent(
                JList list,
                Object value, // value to display
                int index, // cell index
                boolean iss, // is the cell selected
                boolean chf)    // the list and the cell have the focus
        {
            /* The DefaultListCellRenderer class will take care of
             * the JLabels text property, it's foreground and background
             * colors, and so on.
             */
            super.getListCellRendererComponent(list, value, index, iss, chf);

            /* We additionally set the JLabels icon property here.
             */
            String item = value.toString();            
            if(item.contains("-->"))
            {
                setForeground(Color.RED);
            }
            else
            {
                setForeground(Color.BLUE);
            }
            //setIcon((s.length > 10) ? longIcon : shortIcon);                        
            //setFont(new Font("Serif", Font.PLAIN, 12));

            return this;
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
        lblUserCode = new javax.swing.JLabel();
        filler6 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        lblDate = new javax.swing.JLabel();
        lblHOSign = new javax.swing.JLabel();
        panelMain = new JPanel() {  
            public void paintComponent(Graphics g) {  
                Image img = Toolkit.getDefaultToolkit().getImage(  
                    getClass().getResource("/com/POSTransaction/images/imgBackgroundImage.png"));  
                g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);  
            }  
        }; ;
        panelBody = new javax.swing.JPanel();
        btnOld = new javax.swing.JButton();
        btnNew = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        scrollPane1 = new javax.swing.JScrollPane();
        list1 = new javax.swing.JList();
        scrollPane2 = new javax.swing.JScrollPane();
        list2 = new javax.swing.JList();
        scrollPane3 = new javax.swing.JScrollPane();
        list3 = new javax.swing.JList();
        scrollPane4 = new javax.swing.JScrollPane();
        list4 = new javax.swing.JList();
        scrollPane5 = new javax.swing.JScrollPane();
        list5 = new javax.swing.JList();
        scrollPane6 = new javax.swing.JScrollPane();
        list6 = new javax.swing.JList();
        scrollPane7 = new javax.swing.JScrollPane();
        list7 = new javax.swing.JList();
        scrollPane8 = new javax.swing.JScrollPane();
        list8 = new javax.swing.JList();
        btnOrderBooked = new javax.swing.JButton();
        btnOrderProcess = new javax.swing.JButton();
        lblBillDelay8 = new javax.swing.JLabel();
        lblBillDelay7 = new javax.swing.JLabel();
        lblBillDelay6 = new javax.swing.JLabel();
        lblBillDelay5 = new javax.swing.JLabel();
        lblBillDelay4 = new javax.swing.JLabel();
        lblBillDelay3 = new javax.swing.JLabel();
        lblBillDelay2 = new javax.swing.JLabel();
        lblBillDelay1 = new javax.swing.JLabel();

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
        lblProductName.setText("SPOS -  ");
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
        lblformName.setText("- KDS With Book And Process");
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
        panelHeader.add(filler6);

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
        lblHOSign.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                lblHOSignMouseClicked(evt);
            }
        });
        panelHeader.add(lblHOSign);

        getContentPane().add(panelHeader, java.awt.BorderLayout.PAGE_START);

        panelMain.setOpaque(false);
        panelMain.setLayout(new java.awt.GridBagLayout());

        panelBody.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        panelBody.setMinimumSize(new java.awt.Dimension(800, 570));
        panelBody.setOpaque(false);
        panelBody.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnOld.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgOldButton1.png"))); // NOI18N
        btnOld.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgOldButton2.png"))); // NOI18N
        btnOld.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnOldMouseClicked(evt);
            }
        });
        panelBody.add(btnOld, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 102, 45));

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgNewButton1.png"))); // NOI18N
        btnNew.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgNewButton2.png"))); // NOI18N
        btnNew.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnNewMouseClicked(evt);
            }
        });
        panelBody.add(btnNew, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 520, 102, 45));

        btnClose.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnClose.setForeground(new java.awt.Color(255, 255, 255));
        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn1.png"))); // NOI18N
        btnClose.setText("CLOSE");
        btnClose.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnClose.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn2.png"))); // NOI18N
        btnClose.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCloseMouseClicked(evt);
            }
        });
        panelBody.add(btnClose, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 520, 102, 45));

        scrollPane1.setBorder(null);
        scrollPane1.setViewportBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "Bill No", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        scrollPane1.setMinimumSize(new java.awt.Dimension(55, 160));
        scrollPane1.setPreferredSize(new java.awt.Dimension(55, 160));
        scrollPane1.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                scrollPane1MouseClicked(evt);
            }
        });

        list1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        list1.setModel(new javax.swing.AbstractListModel()
        {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        list1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        list1.setFixedCellHeight(25);
        list1.setFixedCellWidth(150);
        list1.setName(""); // NOI18N
        list1.setSelectionBackground(new java.awt.Color(0, 153, 255));
        list1.setSelectionForeground(new java.awt.Color(254, 254, 254));
        scrollPane1.setViewportView(list1);

        panelBody.add(scrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 60, 200, 210));

        scrollPane2.setBorder(null);
        scrollPane2.setViewportBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "Bill No", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        scrollPane2.setMinimumSize(new java.awt.Dimension(55, 160));
        scrollPane2.setPreferredSize(new java.awt.Dimension(55, 160));
        scrollPane2.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                scrollPane2MouseClicked(evt);
            }
        });

        list2.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        list2.setModel(new javax.swing.AbstractListModel()
        {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        list2.setFixedCellHeight(25);
        list2.setFixedCellWidth(150);
        list2.setSelectionBackground(new java.awt.Color(0, 153, 255));
        list2.setSelectionForeground(new java.awt.Color(254, 254, 254));
        list2.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                list2MouseClicked(evt);
            }
        });
        scrollPane2.setViewportView(list2);

        panelBody.add(scrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 60, 200, 210));

        scrollPane3.setBorder(null);
        scrollPane3.setViewportBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "Bill No", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        scrollPane3.setMinimumSize(new java.awt.Dimension(55, 160));
        scrollPane3.setPreferredSize(new java.awt.Dimension(55, 160));
        scrollPane3.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                scrollPane3MouseClicked(evt);
            }
        });

        list3.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        list3.setModel(new javax.swing.AbstractListModel()
        {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        list3.setFixedCellHeight(25);
        list3.setFixedCellWidth(150);
        list3.setSelectionBackground(new java.awt.Color(0, 153, 255));
        list3.setSelectionForeground(new java.awt.Color(254, 254, 254));
        list3.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                list3MouseClicked(evt);
            }
        });
        scrollPane3.setViewportView(list3);

        panelBody.add(scrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 60, 200, 210));

        scrollPane4.setBorder(null);
        scrollPane4.setViewportBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "Bill No", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        scrollPane4.setMinimumSize(new java.awt.Dimension(55, 160));
        scrollPane4.setPreferredSize(new java.awt.Dimension(55, 160));
        scrollPane4.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                scrollPane4MouseClicked(evt);
            }
        });

        list4.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        list4.setModel(new javax.swing.AbstractListModel()
        {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        list4.setFixedCellHeight(25);
        list4.setFixedCellWidth(150);
        list4.setSelectionBackground(new java.awt.Color(0, 153, 255));
        list4.setSelectionForeground(new java.awt.Color(254, 254, 254));
        list4.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                list4MouseClicked(evt);
            }
        });
        scrollPane4.setViewportView(list4);

        panelBody.add(scrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 60, 200, 210));

        scrollPane5.setBorder(null);
        scrollPane5.setViewportBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "Bill No", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        scrollPane5.setMinimumSize(new java.awt.Dimension(55, 160));
        scrollPane5.setPreferredSize(new java.awt.Dimension(55, 160));
        scrollPane5.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                scrollPane5MouseClicked(evt);
            }
        });

        list5.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        list5.setModel(new javax.swing.AbstractListModel()
        {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        list5.setFixedCellHeight(25);
        list5.setFixedCellWidth(150);
        list5.setSelectionBackground(new java.awt.Color(0, 153, 255));
        list5.setSelectionForeground(new java.awt.Color(254, 254, 254));
        list5.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                list5MouseClicked(evt);
            }
        });
        scrollPane5.setViewportView(list5);

        panelBody.add(scrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 290, 200, 210));

        scrollPane6.setBorder(null);
        scrollPane6.setViewportBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "Bill No", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        scrollPane6.setMinimumSize(new java.awt.Dimension(55, 160));
        scrollPane6.setPreferredSize(new java.awt.Dimension(55, 160));
        scrollPane6.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                scrollPane6MouseClicked(evt);
            }
        });

        list6.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        list6.setModel(new javax.swing.AbstractListModel()
        {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        list6.setFixedCellHeight(25);
        list6.setFixedCellWidth(150);
        list6.setSelectionBackground(new java.awt.Color(0, 153, 255));
        list6.setSelectionForeground(new java.awt.Color(254, 254, 254));
        list6.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                list6MouseClicked(evt);
            }
        });
        scrollPane6.setViewportView(list6);

        panelBody.add(scrollPane6, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 290, 200, 210));

        scrollPane7.setBorder(null);
        scrollPane7.setViewportBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "Bill No", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        scrollPane7.setMinimumSize(new java.awt.Dimension(55, 160));
        scrollPane7.setPreferredSize(new java.awt.Dimension(55, 160));
        scrollPane7.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                scrollPane7MouseClicked(evt);
            }
        });

        list7.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        list7.setModel(new javax.swing.AbstractListModel()
        {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        list7.setFixedCellHeight(25);
        list7.setFixedCellWidth(150);
        list7.setSelectionBackground(new java.awt.Color(0, 153, 255));
        list7.setSelectionForeground(new java.awt.Color(254, 254, 254));
        list7.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                list7MouseClicked(evt);
            }
        });
        scrollPane7.setViewportView(list7);

        panelBody.add(scrollPane7, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 290, 200, 210));

        scrollPane8.setBorder(null);
        scrollPane8.setViewportBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "Bill No", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        scrollPane8.setMinimumSize(new java.awt.Dimension(55, 160));
        scrollPane8.setPreferredSize(new java.awt.Dimension(55, 160));
        scrollPane8.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                scrollPane8MouseClicked(evt);
            }
        });

        list8.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        list8.setModel(new javax.swing.AbstractListModel()
        {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        list8.setFixedCellHeight(25);
        list8.setFixedCellWidth(150);
        list8.setSelectionBackground(new java.awt.Color(0, 153, 255));
        list8.setSelectionForeground(new java.awt.Color(254, 254, 254));
        list8.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                list8MouseClicked(evt);
            }
        });
        scrollPane8.setViewportView(list8);

        panelBody.add(scrollPane8, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 290, 200, 210));

        btnOrderBooked.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        btnOrderBooked.setForeground(new java.awt.Color(255, 255, 255));
        btnOrderBooked.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgOrderProcessed.png"))); // NOI18N
        btnOrderBooked.setText("Order Book");
        btnOrderBooked.setEnabled(false);
        btnOrderBooked.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOrderBooked.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgOrderProcessed1.png"))); // NOI18N
        panelBody.add(btnOrderBooked, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 510, 230, 50));

        btnOrderProcess.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        btnOrderProcess.setForeground(new java.awt.Color(255, 255, 255));
        btnOrderProcess.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgOrderProcessed.png"))); // NOI18N
        btnOrderProcess.setText("Order Process");
        btnOrderProcess.setEnabled(false);
        btnOrderProcess.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOrderProcess.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgOrderProcessed1.png"))); // NOI18N
        btnOrderProcess.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnOrderProcessMouseClicked(evt);
            }
        });
        panelBody.add(btnOrderProcess, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 510, 230, 50));

        lblBillDelay8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblBillDelay8.setForeground(new java.awt.Color(255, 51, 0));
        lblBillDelay8.setText("00:00:00");
        lblBillDelay8.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        panelBody.add(lblBillDelay8, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 270, 60, 20));

        lblBillDelay7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblBillDelay7.setForeground(new java.awt.Color(255, 51, 0));
        lblBillDelay7.setText("00:00:00");
        lblBillDelay7.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        panelBody.add(lblBillDelay7, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 270, 60, 20));

        lblBillDelay6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblBillDelay6.setForeground(new java.awt.Color(255, 51, 0));
        lblBillDelay6.setText("00:00:00");
        lblBillDelay6.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        panelBody.add(lblBillDelay6, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 270, 60, 20));

        lblBillDelay5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblBillDelay5.setForeground(new java.awt.Color(255, 51, 0));
        lblBillDelay5.setText("00:00:00");
        lblBillDelay5.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        panelBody.add(lblBillDelay5, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 270, 60, 20));

        lblBillDelay4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblBillDelay4.setForeground(new java.awt.Color(255, 51, 0));
        lblBillDelay4.setText("00:00:00");
        lblBillDelay4.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        panelBody.add(lblBillDelay4, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 40, 60, 20));

        lblBillDelay3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblBillDelay3.setForeground(new java.awt.Color(255, 51, 0));
        lblBillDelay3.setText("00:00:00");
        lblBillDelay3.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        panelBody.add(lblBillDelay3, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 40, 60, 20));

        lblBillDelay2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblBillDelay2.setForeground(new java.awt.Color(255, 51, 0));
        lblBillDelay2.setText("00:00:00");
        lblBillDelay2.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        panelBody.add(lblBillDelay2, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 40, 60, 20));

        lblBillDelay1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblBillDelay1.setForeground(new java.awt.Color(255, 51, 0));
        lblBillDelay1.setText("00:00:00");
        lblBillDelay1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        panelBody.add(lblBillDelay1, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 40, 60, 20));

        panelMain.add(panelBody, new java.awt.GridBagConstraints());

        getContentPane().add(panelMain, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void lblProductNameMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblProductNameMouseClicked
    {//GEN-HEADEREND:event_lblProductNameMouseClicked

    }//GEN-LAST:event_lblProductNameMouseClicked

    private void lblformNameMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblformNameMouseClicked
    {//GEN-HEADEREND:event_lblformNameMouseClicked

    }//GEN-LAST:event_lblformNameMouseClicked

    private void lblPosNameMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblPosNameMouseClicked
    {//GEN-HEADEREND:event_lblPosNameMouseClicked

    }//GEN-LAST:event_lblPosNameMouseClicked

    private void lblUserCodeMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblUserCodeMouseClicked
    {//GEN-HEADEREND:event_lblUserCodeMouseClicked

    }//GEN-LAST:event_lblUserCodeMouseClicked

    private void lblDateMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblDateMouseClicked
    {//GEN-HEADEREND:event_lblDateMouseClicked

    }//GEN-LAST:event_lblDateMouseClicked

    private void lblHOSignMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblHOSignMouseClicked
    {//GEN-HEADEREND:event_lblHOSignMouseClicked

    }//GEN-LAST:event_lblHOSignMouseClicked

    private void btnCloseMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnCloseMouseClicked
    {//GEN-HEADEREND:event_btnCloseMouseClicked
        dispose();
        clsGlobalVarClass.hmActiveForms.remove("KDSBookAndProcess");
    }//GEN-LAST:event_btnCloseMouseClicked

    private void list2MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_list2MouseClicked
    {//GEN-HEADEREND:event_list2MouseClicked

    }//GEN-LAST:event_list2MouseClicked

    private void list3MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_list3MouseClicked
    {//GEN-HEADEREND:event_list3MouseClicked

    }//GEN-LAST:event_list3MouseClicked

    private void list4MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_list4MouseClicked
    {//GEN-HEADEREND:event_list4MouseClicked

    }//GEN-LAST:event_list4MouseClicked

    private void list5MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_list5MouseClicked
    {//GEN-HEADEREND:event_list5MouseClicked

    }//GEN-LAST:event_list5MouseClicked

    private void list6MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_list6MouseClicked
    {//GEN-HEADEREND:event_list6MouseClicked

    }//GEN-LAST:event_list6MouseClicked

    private void list7MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_list7MouseClicked
    {//GEN-HEADEREND:event_list7MouseClicked

    }//GEN-LAST:event_list7MouseClicked

    private void list8MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_list8MouseClicked
    {//GEN-HEADEREND:event_list8MouseClicked

    }//GEN-LAST:event_list8MouseClicked

    private void scrollPane1MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_scrollPane1MouseClicked
    {//GEN-HEADEREND:event_scrollPane1MouseClicked
        funScrollPaneMouseClicked(7);
    }//GEN-LAST:event_scrollPane1MouseClicked

    private void scrollPane2MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_scrollPane2MouseClicked
    {//GEN-HEADEREND:event_scrollPane2MouseClicked
        funScrollPaneMouseClicked(6);
    }//GEN-LAST:event_scrollPane2MouseClicked

    private void scrollPane4MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_scrollPane4MouseClicked
    {//GEN-HEADEREND:event_scrollPane4MouseClicked
        funScrollPaneMouseClicked(4);
    }//GEN-LAST:event_scrollPane4MouseClicked

    private void scrollPane5MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_scrollPane5MouseClicked
    {//GEN-HEADEREND:event_scrollPane5MouseClicked
        funScrollPaneMouseClicked(3);
    }//GEN-LAST:event_scrollPane5MouseClicked

    private void scrollPane6MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_scrollPane6MouseClicked
    {//GEN-HEADEREND:event_scrollPane6MouseClicked
        funScrollPaneMouseClicked(2);
    }//GEN-LAST:event_scrollPane6MouseClicked

    private void scrollPane7MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_scrollPane7MouseClicked
    {//GEN-HEADEREND:event_scrollPane7MouseClicked
        funScrollPaneMouseClicked(1);
    }//GEN-LAST:event_scrollPane7MouseClicked

    private void scrollPane8MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_scrollPane8MouseClicked
    {//GEN-HEADEREND:event_scrollPane8MouseClicked
        funScrollPaneMouseClicked(0);
    }//GEN-LAST:event_scrollPane8MouseClicked

    private void scrollPane3MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_scrollPane3MouseClicked
    {//GEN-HEADEREND:event_scrollPane3MouseClicked
        funScrollPaneMouseClicked(5);
    }//GEN-LAST:event_scrollPane3MouseClicked

    private void btnOldMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnOldMouseClicked
    {//GEN-HEADEREND:event_btnOldMouseClicked
        if (btnOld.isEnabled())
        {
            funOldButtonClicked();
        }
    }//GEN-LAST:event_btnOldMouseClicked

    private void btnNewMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnNewMouseClicked
    {//GEN-HEADEREND:event_btnNewMouseClicked
        if (btnNew.isEnabled())
        {
            funNewButtonClicked();
        }
    }//GEN-LAST:event_btnNewMouseClicked

    private void btnOrderProcessMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnOrderProcessMouseClicked
    {//GEN-HEADEREND:event_btnOrderProcessMouseClicked
        if (btnOrderProcess.isEnabled())
        {
            if (listOfBillsToBeProcess.size() > 0)
            {
                funButtonOrderProcessClicked();
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Please Select The Bill.");
                return;
            }
        }
    }//GEN-LAST:event_btnOrderProcessMouseClicked

    private void formWindowClosed(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosed
    {//GEN-HEADEREND:event_formWindowClosed
        clsGlobalVarClass.hmActiveForms.remove("KDSBookAndProcess");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosing
    {//GEN-HEADEREND:event_formWindowClosing
        clsGlobalVarClass.hmActiveForms.remove("KDSBookAndProcess");
    }//GEN-LAST:event_formWindowClosing

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
            java.util.logging.Logger.getLogger(frmKDSBookAndProcess.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(frmKDSBookAndProcess.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(frmKDSBookAndProcess.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(frmKDSBookAndProcess.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                new frmKDSBookAndProcess().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnOld;
    private javax.swing.JButton btnOrderBooked;
    private javax.swing.JButton btnOrderProcess;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JLabel lblBillDelay1;
    private javax.swing.JLabel lblBillDelay2;
    private javax.swing.JLabel lblBillDelay3;
    private javax.swing.JLabel lblBillDelay4;
    private javax.swing.JLabel lblBillDelay5;
    private javax.swing.JLabel lblBillDelay6;
    private javax.swing.JLabel lblBillDelay7;
    private javax.swing.JLabel lblBillDelay8;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblformName;
    private javax.swing.JList list1;
    private javax.swing.JList list2;
    private javax.swing.JList list3;
    private javax.swing.JList list4;
    private javax.swing.JList list5;
    private javax.swing.JList list6;
    private javax.swing.JList list7;
    private javax.swing.JList list8;
    private javax.swing.JPanel panelBody;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelMain;
    private javax.swing.JScrollPane scrollPane1;
    private javax.swing.JScrollPane scrollPane2;
    private javax.swing.JScrollPane scrollPane3;
    private javax.swing.JScrollPane scrollPane4;
    private javax.swing.JScrollPane scrollPane5;
    private javax.swing.JScrollPane scrollPane6;
    private javax.swing.JScrollPane scrollPane7;
    private javax.swing.JScrollPane scrollPane8;
    // End of variables declaration//GEN-END:variables

    private void funScrollPaneMouseClicked(int index)
    {
        boolean isSelected = funCheckSelectedOrDeselected(index);
        if (isSelected)
        {
            funDeSelectScrollPane(index);
        }
        else
        {
            funSelectScrollPane(index);
        }
    }

    private boolean funCheckSelectedOrDeselected(int index)
    {
        boolean isSelectedYN = true;

        if (scrollPaneArray[index].getBorder() == null)
        {
            isSelectedYN = false;
        }
        else
        {
            isSelectedYN = true;
        }

        return isSelectedYN;
    }

    private void funDeSelectScrollPane(int index)
    {
        scrollPaneArray[index].setBorder(null);

        listOfBillsToBeProcess.remove(listOfBills.get((navigator * 8) + index).get(0).getBillNo());

        if (listOfBillsToBeProcess.size() > 0)
        {
            btnOrderProcess.setEnabled(true);
        }
        else
        {
            btnOrderProcess.setEnabled(false);
        }
    }

    private void funSelectScrollPane(int index)
    {
        scrollPaneArray[index].setBorder(new BevelBorder(BevelBorder.LOWERED, Color.RED, Color.RED));

        listOfBillsToBeProcess.add(listOfBills.get((navigator * 8) + index).get(0).getBillNo());

        if (listOfBillsToBeProcess.size() > 0)
        {
            btnOrderProcess.setEnabled(true);
        }
        else
        {
            btnOrderProcess.setEnabled(false);
        }
    }

    private void funResetDefault()
    {
        btnNew.setEnabled(false);
        btnOld.setEnabled(false);
        navigatorNew = 0;
        navigator = 0;
        funSetScrollPanesVisisble(false);
        funSetCustomListCellRenderer();
        mapBillHd.clear();
        listOfBills.clear();
        listOfBillsToBeProcess.clear();
    }

    private void fumLoadMapBillHd()
    {
        try
        {
            String sqlBillDtl = " SELECT a.strBillNo,a.strItemCode,a.strItemName,a.dblRate,a.dblQuantity,a.dblAmount,a.tmeOrderProcessing,time(a.dteBillDate)\n"
                    + " FROM tblbilldtl a\n"
                    + " where a.strBillNo not in(select strDocNo from tblkdsprocess where strBP='P' and strKDSName='BILL' ) "
                    + " GROUP BY a.strBillNo,a.strKOTNo,a.strItemCode\n"
                    + " ORDER BY a.dteBillDate desc,time(a.dteBillDate) desc  ";
            //System.out.println("total bills-->"+sqlBillDtl);
            ResultSet resultSet = clsGlobalVarClass.dbMysql.executeResultSet(sqlBillDtl);
            while (resultSet.next())
            {
                clsBillItemDtl billItemDtl = new clsBillItemDtl();

                String billNo = resultSet.getString(1);
                billItemDtl.setBillNo(billNo);
                billItemDtl.setItemCode(resultSet.getString(2));
                billItemDtl.setItemName(resultSet.getString(3));
                billItemDtl.setRate(resultSet.getDouble(4));
                billItemDtl.setQuantity(resultSet.getDouble(5));
                billItemDtl.setAmount(resultSet.getDouble(6));
                billItemDtl.setBillDateTime(resultSet.getString(8));

                if (mapBillHd.containsKey(billNo))
                {
                    mapBillHd.get(billNo).add(billItemDtl);

//                    String sqlModifier = "select strModifierCode,strModifierName,dblRate,dblQuantity,dblAmount "
//                            + "from tblbillmodifierdtl where strBillNo='" + billNo + "' and left(strItemCode,7)='" + resultSet.getString(2) + "' ";
//                    ResultSet modiResultSet = clsGlobalVarClass.dbMysql.executeResultSet(sqlModifier);
//                    while (modiResultSet.next())
//                    {
//                        clsBillItemDtl billItemModiDtl = new clsBillItemDtl();
//
//                        billItemModiDtl.setItemCode(modiResultSet.getString(1));
//                        billItemModiDtl.setItemName(modiResultSet.getString(2));
//                        billItemModiDtl.setQuantity(modiResultSet.getDouble(4));
//
//                        mapBillHd.get(billNo).add(billItemModiDtl);
//                    }
                    String sqlModifierDtl=" SELECT b.strModifierCode,b.strModifierName,b.dblQuantity,b.dblAmount,a.strDefaultModifier,b.strDefaultModifierDeselectedYN  "
                        +" FROM tblbillmodifierdtl b,tblitemmodofier a " 
                        +" WHERE " 
                        +" a.strItemCode=left(b.strItemCode,7) "
                        +" and a.strModifierCode=b.strModifierCode "
                        +" and b.strBillNo=? AND LEFT(b.strItemCode,7)=? ";
                    
                    /* if do not want to  show 0 amount modifiers
                    if (clsGlobalVarClass.gPrintZeroAmtModifierOnBill.equals("N"))
                    {
                        sqlModifierDtl += " and b.dblAmount !=0.00 ;";
                    }
                    */
                    PreparedStatement prst = clsGlobalVarClass.conPrepareStatement.prepareStatement(sqlModifierDtl);

                    prst.setString(1, billNo);
                    prst.setString(2, resultSet.getString(2));
                    ResultSet modiResultSet = prst.executeQuery();
                    while (modiResultSet.next())
                    {
                        if(modiResultSet.getString(5).equalsIgnoreCase("N"))
                        {
                            clsBillItemDtl billItemModiDtl = new clsBillItemDtl();

                            billItemModiDtl.setItemCode(modiResultSet.getString(1));
                            billItemModiDtl.setItemName(modiResultSet.getString(2));
                            billItemModiDtl.setQuantity(modiResultSet.getDouble(3));

                            mapBillHd.get(billNo).add(billItemModiDtl);
                        }
                        else if(modiResultSet.getString(5).equalsIgnoreCase("Y") && modiResultSet.getString(6).equalsIgnoreCase("Y"))
                        {
                            clsBillItemDtl billItemModiDtl = new clsBillItemDtl();

                            billItemModiDtl.setItemCode(modiResultSet.getString(1));
                            billItemModiDtl.setItemName("No"+modiResultSet.getString(2));
                            billItemModiDtl.setQuantity(modiResultSet.getDouble(3));

                            mapBillHd.get(billNo).add(billItemModiDtl);
                        }
                    }
                }
                else
                {
                    ArrayList<clsBillItemDtl> listBillItemDtl = new ArrayList<clsBillItemDtl>();

                    listBillItemDtl.add(billItemDtl);

                    mapBillHd.put(billNo, listBillItemDtl);

//                    String sqlModifier = "select strModifierCode,strModifierName,dblRate,dblQuantity,dblAmount "
//                            + "from tblbillmodifierdtl where strBillNo='" + billNo + "' and left(strItemCode,7)='" + resultSet.getString(2) + "' ";
//                    ResultSet modiResultSet = clsGlobalVarClass.dbMysql.executeResultSet(sqlModifier);
//                    while (modiResultSet.next())
//                    {
//                        clsBillItemDtl billItemModiDtl = new clsBillItemDtl();
//
//                        billItemModiDtl.setItemCode(modiResultSet.getString(1));
//                        billItemModiDtl.setItemName(modiResultSet.getString(2));
//                        billItemModiDtl.setQuantity(modiResultSet.getDouble(4));
//
//                        mapBillHd.get(billNo).add(billItemModiDtl);
//                    }
                    String sqlModifierDtl=" SELECT b.strModifierCode,b.strModifierName,b.dblQuantity,b.dblAmount,a.strDefaultModifier,b.strDefaultModifierDeselectedYN  "
                        +" FROM tblbillmodifierdtl b,tblitemmodofier a " 
                        +" WHERE " 
                        +" a.strItemCode=left(b.strItemCode,7) "
                        +" and a.strModifierCode=b.strModifierCode "
                        +" and b.strBillNo=? AND LEFT(b.strItemCode,7)=? ";
                    
                    /* if do not want to  show 0 amount modifiers
                    if (clsGlobalVarClass.gPrintZeroAmtModifierOnBill.equals("N"))
                    {
                        sqlModifierDtl += " and b.dblAmount !=0.00 ;";
                    }
                    */
                    PreparedStatement prst = clsGlobalVarClass.conPrepareStatement.prepareStatement(sqlModifierDtl);

                    prst.setString(1, billNo);
                    prst.setString(2, resultSet.getString(2));
                    ResultSet modiResultSet = prst.executeQuery();
                    while (modiResultSet.next())
                    {
                        if(!modiResultSet.getString(5).equalsIgnoreCase("Y"))
                        {
                            clsBillItemDtl billItemModiDtl = new clsBillItemDtl();

                            billItemModiDtl.setItemCode(modiResultSet.getString(1));
                            billItemModiDtl.setItemName(modiResultSet.getString(2));
                            billItemModiDtl.setQuantity(modiResultSet.getDouble(3));

                            mapBillHd.get(billNo).add(billItemModiDtl);
                        } 
                        else if(modiResultSet.getString(5).equalsIgnoreCase("Y") && modiResultSet.getString(6).equalsIgnoreCase("Y"))
                        {
                            clsBillItemDtl billItemModiDtl = new clsBillItemDtl();

                            billItemModiDtl.setItemCode(modiResultSet.getString(1));
                            billItemModiDtl.setItemName("No"+modiResultSet.getString(2));
                            billItemModiDtl.setQuantity(modiResultSet.getDouble(3));

                            mapBillHd.get(billNo).add(billItemModiDtl);
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

    private void funSetScrollPaneData(int index)//index of scrollPane
    {
        final String[] billItemList = funGetListDtl((navigator * 8) + index);//index of bill in list

        AbstractListModel listModel = new DefaultListModel()
        {
            private String[] strings = billItemList;

            public int getSize()
            {
                return strings.length;
            }

            public Object getElementAt(int i)
            {
                return strings[i];
            }

        };

        listViewArray[index].setModel(listModel);

        funDeSelectScrollPane(index);
        scrollPaneArray[index].setVisible(true);

        lblBillDelayArray[index].setVisible(true);
        ((TitledBorder) scrollPaneArray[index].getViewportBorder()).setTitle(listOfBills.get((navigator * 8) + index).get(0).getBillNo());
    }

    private String[] funGetListDtl(int billIndex)
    {
        ArrayList<clsBillItemDtl> listBillItemDtl = listOfBills.get(billIndex);
        String[] modelList = new String[listBillItemDtl.size()];
        int itemIndex = 0;
        for (int i = 0; i < listBillItemDtl.size(); i++)
        {
            clsBillItemDtl objBillItemDtl = listBillItemDtl.get(i);
            modelList[itemIndex++] = objBillItemDtl.getQuantity() + " " + objBillItemDtl.getItemName();
            
            //modifiers could be added here but check the flow of coding.
        }

        return modelList;
    }

    private void funLoadScrollPanes(int startIndex, int endIndex)
    {
        funSetScrollPanesVisisble(false);

        for (int i = startIndex; i <= endIndex; i++)
        {
            funSetScrollPaneData(i);
        }
    }

    private void funSetScrollPanesVisisble(boolean flag)
    {
        for (int i = 0; i < 8; i++)
        {
            scrollPaneArray[i].setColumnHeader(null);
            scrollPaneArray[i].setColumnHeaderView(null);
            scrollPaneArray[i].setVisible(flag);

//            lblBillNoArray[i].setText("Bill No");
//            lblBillNoArray[i].setVisible(flag);
            lblBillDelayArray[i].setText("00:00:00");
            lblBillDelayArray[i].setVisible(flag);
        }
    }

    private void funLoadBillArrayList()
    {
        Iterator<Map.Entry<String, ArrayList<clsBillItemDtl>>> it = mapBillHd.entrySet().iterator();
        while (it.hasNext())
        {
            Map.Entry<String, ArrayList<clsBillItemDtl>> entry = it.next();
            listOfBills.add(entry.getValue());
        }
    }

}
