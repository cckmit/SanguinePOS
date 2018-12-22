package com.spos.controller;

import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.view.frmOkCancelPopUp;
import com.POSGlobal.view.frmOkPopUp;
import com.POSGlobal.view.frmTools;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.border.BevelBorder;

public class frmPOSSelection extends javax.swing.JFrame
{
    //private String[] posCode, psNames, posType, debitCardYN, propertyCode, counterWiseBilling;
    //private String[] delayedSett, billPrinterPort, advReceiptPrinterPort;
    private String sql, posAccess;
    private String[] splitPosCode;
    private JButton[] posButtons;
    private Map<String,clsPOSMaster> hmPOSMaster=new HashMap<String,clsPOSMaster>();
    private int navigate=0;

    public frmPOSSelection()
    {
        initComponents();
        lblModuleName.setText(clsGlobalVarClass.gSelectedModule);

        posButtons = new JButton[]
        {
            btnPos1, btnPos2, btnPos3, btnPos4, btnPos5, btnPos6, btnPos7, btnPos8,
            btnPos9, btnPos10, btnPos11, btnPos12, btnPos13, btnPos14, btnPos15
        };

        this.setLocationRelativeTo(null);
        try
        {
            Date dt = new Date();
            String currentDate = dt.getDate() + "-" + (dt.getMonth() + 1) + "-" + (dt.getYear() + 1900);
            lblDate.setText(currentDate);
            lblUserCode.setText(clsGlobalVarClass.gUserCode);

            for (int j = 0; j < posButtons.length; j++)
            {
                posButtons[j].setVisible(false);
            }

            ResultSet posSet = clsGlobalVarClass.dbMysql.executeResultSet("select count(strPosCode) from tblposmaster");
            posSet.next();
            int cnt = posSet.getInt(1);
            if (cnt > 0)
            {
                btnPosMast.setVisible(false);
                
                if (clsGlobalVarClass.gSanguneUser)
                {
                    sql = "select a.strPosCode,a.strPosName,a.strPosType,'All POS',"
                            + "a.strDebitCardTransactionYN,a.strPropertyPOSCode"
                            + ",a.strCounterWiseBilling,strDelayedSettlementForDB"
                            + ",a.strBillPrinterPort,a.strAdvReceiptPrinterPort "
                            + ",a.strPrintVatNo,a.strPrintServiceTaxNo,a.strVatNo,a.strServiceTaxNo "
                            + "from tblposmaster a";
                }
                else
                {
                    sql = "select a.strPosCode,a.strPosName,a.strPosType,b.strPOSAccess,"
                            + "a.strDebitCardTransactionYN,a.strPropertyPOSCode"
                            + ",a.strCounterWiseBilling,a.strDelayedSettlementForDB"
                            + ",a.strBillPrinterPort,a.strAdvReceiptPrinterPort "
                            + ",a.strPrintVatNo,a.strPrintServiceTaxNo,a.strVatNo,a.strServiceTaxNo "
                            + "from tblposmaster a,tbluserhd b"
                            + " where strUserCode='" + clsGlobalVarClass.gUserCode + "'";
                }

                posSet = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                while (posSet.next())
                {
                    if (posSet.getString(4).equals("All POS"))
                    {
                        posAccess = posSet.getString(4);
                    }
                    else
                    {
                        String posCodeForSplit = posSet.getString(4);
                        splitPosCode = posCodeForSplit.split(",");
                        posAccess = "";
                    }
                    
                    if (posAccess.equals("All POS"))
                    {
                        clsPOSMaster objPOSMaster=new clsPOSMaster();
                        objPOSMaster.setPOSCode(posSet.getString(1));
                        objPOSMaster.setPOSName(posSet.getString(2));
                        objPOSMaster.setPOSType(posSet.getString(3));
                        objPOSMaster.setDebitCardYN(posSet.getString(5));
                        objPOSMaster.setPropertyCode(posSet.getString(6));
                        objPOSMaster.setCounterWiseBilling(posSet.getString(7));
                        objPOSMaster.setDelayedSettlement(posSet.getString(8));
                        objPOSMaster.setBillPrinterPort(posSet.getString(9));
                        objPOSMaster.setAdvOrderPrinterPort(posSet.getString(10));
                        objPOSMaster.setPrintVatNo(posSet.getString(11));
                        objPOSMaster.setPrintServiceTaxNo(posSet.getString(12));
                        objPOSMaster.setVatNo(posSet.getString(13));
                        objPOSMaster.setServiceTaxNo(posSet.getString(14));
                        hmPOSMaster.put(posSet.getString(2), objPOSMaster);
                    }
                    else
                    {
                        for (int j = 0; j < splitPosCode.length; j++)
                        {
                            if (splitPosCode[j].equals(posSet.getString(1)))
                            {
                                clsPOSMaster objPOSMaster=new clsPOSMaster();
                                objPOSMaster.setPOSCode(posSet.getString(1));
                                objPOSMaster.setPOSName(posSet.getString(2));
                                objPOSMaster.setPOSType(posSet.getString(3));
                                objPOSMaster.setDebitCardYN(posSet.getString(5));
                                objPOSMaster.setPropertyCode(posSet.getString(6));
                                objPOSMaster.setCounterWiseBilling(posSet.getString(7));
                                objPOSMaster.setDelayedSettlement(posSet.getString(8));
                                objPOSMaster.setBillPrinterPort(posSet.getString(9));
                                objPOSMaster.setAdvOrderPrinterPort(posSet.getString(10));
                                objPOSMaster.setPrintVatNo(posSet.getString(11));
                                objPOSMaster.setPrintServiceTaxNo(posSet.getString(12));
                                objPOSMaster.setVatNo(posSet.getString(13));
                                objPOSMaster.setServiceTaxNo(posSet.getString(14));
                                hmPOSMaster.put(posSet.getString(2), objPOSMaster);
                            }
                        }
                    }
                }
                
                btnPrev.setEnabled(false);
                funLoadPOSNames(0, 15);
                posButtons[0].requestFocus();
                posButtons[0].setBorder(new BevelBorder(BevelBorder.LOWERED));
            }
            else
            {
                btnPosMast.setVisible(true);
                btnPosMast.setText("<html>POS<br>Master</html>");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            this.dispose();
            new frmOkPopUp(null, "Please Perform Structure Update", "Warning", 1).setVisible(true);
            new frmTools("startup").setVisible(true);
        }
    }
    
    
    private void funLoadPOSNames(int start,int end)
    {
        int cnt=0;
        List arrListPOSMaster=new ArrayList<clsPOSMaster>();
        for (Map.Entry<String, clsPOSMaster> entry : hmPOSMaster.entrySet()) 
        {
            arrListPOSMaster.add(entry.getValue());
        }
        
        for (int i=start;i<end;i++) 
        {
            if(i==hmPOSMaster.size())
                break;
            
            if(cnt<15)
            {
                clsPOSMaster objPOSMaster =(clsPOSMaster) arrListPOSMaster.get(i);
                String posName=objPOSMaster.getPOSName();
                if (posName.contains(" "))
                {
                    String[] sp = posName.split(" ");
                    //posButtons[cnt].setText("<html>" + sp[0] + "<br>" + sp[1] + "</html>");
                    posName=funFormatPOSName(posName);
                    posButtons[cnt].setText(posName);
                    posButtons[cnt].setVisible(true);
                }
                else
                {
                    posButtons[cnt].setText(posName);
                    posButtons[cnt].setVisible(true);
                }
                cnt++;
            }
        }
        
        for(int cn=cnt;cn<15;cn++)
        {
            posButtons[cn].setText("");
            posButtons[cn].setVisible(false);
        }
        
        if(end<hmPOSMaster.size())
        {
            btnNext.setEnabled(true);
        }
        else
        {
            btnNext.setEnabled(false);
        }
    }
    
    
    private void funNextButtonClicked()
    {
        navigate++;
        btnPrev.setEnabled(true);
        int start=15*navigate;
        int end=15;
        int endLimit=(15*navigate)+15;
        if(hmPOSMaster.size()<endLimit)
        {
            end=endLimit-hmPOSMaster.size();
            end=endLimit-end;
        }
        funLoadPOSNames(start, end);
    }
    
    
    private void funPreviousButtonClicked()
    {
        navigate--;
        if(navigate==0)
        {
            btnPrev.setEnabled(false);
        }
        int start=15*navigate;
        int end=(15*navigate)+15;
        funLoadPOSNames(start, end);
    }
    

    private String funCheckPOSName(String name)
    {
        String posName=name;
        posName=posName.replaceAll("<html>","");
        posName=posName.replaceAll("</html>","");
        posName=posName.replaceAll("<br>"," ");
        return posName;
    }
    
    
    private String funFormatPOSName(String name)
    {
        String posName=name;
        posName=posName.replaceAll(" ","<br>");
        posName="<html>"+posName+"</html>";
        return posName;
    }
    
    /**
     * This method is used to add PLU item DTL
     */
    /*
     private void funAddPLUItemDTL() {
     clsPLUItemDtl objPLUItemDtl = new clsPLUItemDtl();
     objPLUItemDtl.fun_fillHasMap();
     }*/
    private void funInitPOSSessionValues(String posName)
    {
        posName=funCheckPOSName(posName);
        clsGlobalVarClass.setPOSCode(hmPOSMaster.get(posName).getPOSCode());
        clsGlobalVarClass.setPOSName(posName);
        clsGlobalVarClass.gDebitCardPayment = hmPOSMaster.get(posName).getDebitCardYN();
        clsGlobalVarClass.setPropertyCode(hmPOSMaster.get(posName).getPropertyCode());
        clsGlobalVarClass.gDelayedSettlementForDB = hmPOSMaster.get(posName).getDelayedSettlement();
        clsGlobalVarClass.setCounterWiseBilling(hmPOSMaster.get(posName).getCounterWiseBilling());
        //clsGlobalVarClass.gBillPrintPrinterPort=billPrinterPort[index];
        clsGlobalVarClass.gAdvReceiptPrinterPort = hmPOSMaster.get(posName).getAdvOrderPrinterPort();
        clsGlobalVarClass.gPrintVatNoPOS= hmPOSMaster.get(posName).getPrintVatNo();
        clsGlobalVarClass.gPrintServiceTaxNoPOS= hmPOSMaster.get(posName).getPrintServiceTaxNo();
        clsGlobalVarClass.gPOSVatNo= hmPOSMaster.get(posName).getVatNo();
        clsGlobalVarClass.gPOSServiceTaxNo= hmPOSMaster.get(posName).getServiceTaxNo();
        //funAddPLUItemDTL();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        lblProductName = new javax.swing.JLabel();
        lblModuleName = new javax.swing.JLabel();
        lblformName = new javax.swing.JLabel();
        filler4 = new javax.swing.Box.Filler(new java.awt.Dimension(20, 0), new java.awt.Dimension(20, 0), new java.awt.Dimension(20, 32767));
        filler5 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        lblPosName = new javax.swing.JLabel();
        filler6 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        lblUserCode = new javax.swing.JLabel();
        lblDate = new javax.swing.JLabel();
        lblLogOut = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        panelHeader = new javax.swing.JPanel();
        btnPos5 = new javax.swing.JButton();
        btnPos2 = new javax.swing.JButton();
        btnPos3 = new javax.swing.JButton();
        btnPos4 = new javax.swing.JButton();
        btnPos1 = new javax.swing.JButton();
        btnPos7 = new javax.swing.JButton();
        btnPos8 = new javax.swing.JButton();
        btnPos9 = new javax.swing.JButton();
        btnPosMast = new javax.swing.JButton();
        btnPos6 = new javax.swing.JButton();
        btnPos11 = new javax.swing.JButton();
        btnPos12 = new javax.swing.JButton();
        btnPos13 = new javax.swing.JButton();
        btnPos14 = new javax.swing.JButton();
        btnPos10 = new javax.swing.JButton();
        btnPos15 = new javax.swing.JButton();
        btnPrev = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        lblBackgroundImage = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setExtendedState(MAXIMIZED_BOTH);
        setMinimumSize(new java.awt.Dimension(800, 600));
        setUndecorated(true);
        setPreferredSize(new java.awt.Dimension(824, 600));

        jPanel1.setBackground(new java.awt.Color(69, 164, 238));
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.LINE_AXIS));

        lblProductName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblProductName.setForeground(new java.awt.Color(255, 255, 255));
        lblProductName.setText("SPOS - ");
        jPanel1.add(lblProductName);

        lblModuleName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblModuleName.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.add(lblModuleName);

        lblformName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblformName.setForeground(new java.awt.Color(255, 255, 255));
        lblformName.setText("-  POS Selection");
        jPanel1.add(lblformName);
        jPanel1.add(filler4);
        jPanel1.add(filler5);

        lblPosName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblPosName.setForeground(new java.awt.Color(255, 255, 255));
        lblPosName.setMaximumSize(new java.awt.Dimension(321, 30));
        lblPosName.setMinimumSize(new java.awt.Dimension(321, 30));
        lblPosName.setPreferredSize(new java.awt.Dimension(321, 30));
        jPanel1.add(lblPosName);
        jPanel1.add(filler6);

        lblUserCode.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblUserCode.setForeground(new java.awt.Color(255, 255, 255));
        lblUserCode.setMaximumSize(new java.awt.Dimension(90, 30));
        lblUserCode.setMinimumSize(new java.awt.Dimension(90, 30));
        lblUserCode.setPreferredSize(new java.awt.Dimension(90, 30));
        jPanel1.add(lblUserCode);

        lblDate.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblDate.setForeground(new java.awt.Color(255, 255, 255));
        lblDate.setMaximumSize(new java.awt.Dimension(192, 30));
        lblDate.setMinimumSize(new java.awt.Dimension(192, 30));
        lblDate.setPreferredSize(new java.awt.Dimension(192, 30));
        jPanel1.add(lblDate);

        lblLogOut.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblLogOut.setForeground(new java.awt.Color(0, 255, 255));
        lblLogOut.setText("LOG OUT");
        lblLogOut.setMaximumSize(new java.awt.Dimension(34, 30));
        lblLogOut.setMinimumSize(new java.awt.Dimension(34, 30));
        lblLogOut.setPreferredSize(new java.awt.Dimension(180, 30));
        lblLogOut.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblLogOutMouseClicked(evt);
            }
        });
        jPanel1.add(lblLogOut);

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_START);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new java.awt.GridBagLayout());

        jPanel3.setMinimumSize(new java.awt.Dimension(800, 570));
        jPanel3.setOpaque(false);

        panelHeader.setBackground(new java.awt.Color(255, 255, 255));
        panelHeader.setMinimumSize(new java.awt.Dimension(800, 500));
        panelHeader.setPreferredSize(new java.awt.Dimension(800, 550));
        panelHeader.setLayout(null);

        btnPos5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnPos5.setForeground(new java.awt.Color(255, 255, 255));
        btnPos5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgPOSSelection2.png"))); // NOI18N
        btnPos5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPos5.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgPOSSelection2.png"))); // NOI18N
        btnPos5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnPos5MouseClicked(evt);
            }
        });
        btnPos5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnPos5KeyPressed(evt);
            }
        });
        panelHeader.add(btnPos5);
        btnPos5.setBounds(580, 120, 121, 80);

        btnPos2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnPos2.setForeground(new java.awt.Color(255, 255, 255));
        btnPos2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgPOSSelection2.png"))); // NOI18N
        btnPos2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPos2.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgPOSSelection2.png"))); // NOI18N
        btnPos2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnPos2MouseClicked(evt);
            }
        });
        btnPos2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnPos2KeyPressed(evt);
            }
        });
        panelHeader.add(btnPos2);
        btnPos2.setBounds(190, 120, 120, 80);

        btnPos3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnPos3.setForeground(new java.awt.Color(255, 255, 255));
        btnPos3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgPOSSelection2.png"))); // NOI18N
        btnPos3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPos3.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgPOSSelection2.png"))); // NOI18N
        btnPos3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnPos3MouseClicked(evt);
            }
        });
        btnPos3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnPos3KeyPressed(evt);
            }
        });
        panelHeader.add(btnPos3);
        btnPos3.setBounds(320, 120, 120, 80);

        btnPos4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnPos4.setForeground(new java.awt.Color(255, 255, 255));
        btnPos4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgPOSSelection2.png"))); // NOI18N
        btnPos4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPos4.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgPOSSelection2.png"))); // NOI18N
        btnPos4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnPos4MouseClicked(evt);
            }
        });
        btnPos4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnPos4KeyPressed(evt);
            }
        });
        panelHeader.add(btnPos4);
        btnPos4.setBounds(450, 120, 120, 80);

        btnPos1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnPos1.setForeground(new java.awt.Color(255, 255, 255));
        btnPos1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgPOSSelection2.png"))); // NOI18N
        btnPos1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPos1.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgPOSSelection2.png"))); // NOI18N
        btnPos1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnPos1MouseClicked(evt);
            }
        });
        btnPos1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnPos1KeyPressed(evt);
            }
        });
        panelHeader.add(btnPos1);
        btnPos1.setBounds(60, 120, 120, 80);

        btnPos7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnPos7.setForeground(new java.awt.Color(255, 255, 255));
        btnPos7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgPOSSelection2.png"))); // NOI18N
        btnPos7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPos7.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgPOSSelection2.png"))); // NOI18N
        btnPos7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnPos7MouseClicked(evt);
            }
        });
        btnPos7.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnPos7KeyPressed(evt);
            }
        });
        panelHeader.add(btnPos7);
        btnPos7.setBounds(190, 210, 120, 80);

        btnPos8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnPos8.setForeground(new java.awt.Color(255, 255, 255));
        btnPos8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgPOSSelection2.png"))); // NOI18N
        btnPos8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPos8.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgPOSSelection2.png"))); // NOI18N
        btnPos8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnPos8MouseClicked(evt);
            }
        });
        btnPos8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPos8ActionPerformed(evt);
            }
        });
        btnPos8.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnPos8KeyPressed(evt);
            }
        });
        panelHeader.add(btnPos8);
        btnPos8.setBounds(320, 210, 120, 80);

        btnPos9.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnPos9.setForeground(new java.awt.Color(255, 255, 255));
        btnPos9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgPOSSelection2.png"))); // NOI18N
        btnPos9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPos9.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgPOSSelection2.png"))); // NOI18N
        btnPos9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnPos9MouseClicked(evt);
            }
        });
        btnPos9.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnPos9KeyPressed(evt);
            }
        });
        panelHeader.add(btnPos9);
        btnPos9.setBounds(450, 210, 120, 80);

        btnPosMast.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnPosMast.setForeground(new java.awt.Color(255, 255, 255));
        btnPosMast.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgPOSSelection2.png"))); // NOI18N
        btnPosMast.setText("POS Master");
        btnPosMast.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPosMast.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgPOSSelection2.png"))); // NOI18N
        btnPosMast.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnPosMastMouseClicked(evt);
            }
        });
        panelHeader.add(btnPosMast);
        btnPosMast.setBounds(60, 410, 116, 80);

        btnPos6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnPos6.setForeground(new java.awt.Color(255, 255, 255));
        btnPos6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgPOSSelection2.png"))); // NOI18N
        btnPos6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPos6.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgPOSSelection2.png"))); // NOI18N
        btnPos6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnPos6MouseClicked(evt);
            }
        });
        btnPos6.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnPos6KeyPressed(evt);
            }
        });
        panelHeader.add(btnPos6);
        btnPos6.setBounds(60, 210, 120, 80);

        btnPos11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnPos11.setForeground(new java.awt.Color(255, 255, 255));
        btnPos11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgPOSSelection2.png"))); // NOI18N
        btnPos11.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPos11.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgPOSSelection2.png"))); // NOI18N
        btnPos11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnPos11MouseClicked(evt);
            }
        });
        btnPos11.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnPos11KeyPressed(evt);
            }
        });
        panelHeader.add(btnPos11);
        btnPos11.setBounds(60, 300, 120, 80);

        btnPos12.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnPos12.setForeground(new java.awt.Color(255, 255, 255));
        btnPos12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgPOSSelection2.png"))); // NOI18N
        btnPos12.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPos12.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgPOSSelection2.png"))); // NOI18N
        btnPos12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPos12ActionPerformed(evt);
            }
        });
        btnPos12.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnPos12KeyPressed(evt);
            }
        });
        panelHeader.add(btnPos12);
        btnPos12.setBounds(190, 300, 120, 80);

        btnPos13.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnPos13.setForeground(new java.awt.Color(255, 255, 255));
        btnPos13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgPOSSelection2.png"))); // NOI18N
        btnPos13.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPos13.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgPOSSelection2.png"))); // NOI18N
        btnPos13.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnPos13MouseClicked(evt);
            }
        });
        btnPos13.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnPos13KeyPressed(evt);
            }
        });
        panelHeader.add(btnPos13);
        btnPos13.setBounds(320, 300, 120, 80);

        btnPos14.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnPos14.setForeground(new java.awt.Color(255, 255, 255));
        btnPos14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgPOSSelection2.png"))); // NOI18N
        btnPos14.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPos14.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgPOSSelection2.png"))); // NOI18N
        btnPos14.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnPos14MouseClicked(evt);
            }
        });
        btnPos14.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnPos14KeyPressed(evt);
            }
        });
        panelHeader.add(btnPos14);
        btnPos14.setBounds(450, 300, 120, 80);

        btnPos10.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnPos10.setForeground(new java.awt.Color(255, 255, 255));
        btnPos10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgPOSSelection2.png"))); // NOI18N
        btnPos10.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPos10.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgPOSSelection2.png"))); // NOI18N
        btnPos10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnPos10MouseClicked(evt);
            }
        });
        btnPos10.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnPos10KeyPressed(evt);
            }
        });
        panelHeader.add(btnPos10);
        btnPos10.setBounds(580, 210, 121, 80);

        btnPos15.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnPos15.setForeground(new java.awt.Color(255, 255, 255));
        btnPos15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgPOSSelection2.png"))); // NOI18N
        btnPos15.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPos15.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgPOSSelection2.png"))); // NOI18N
        btnPos15.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnPos15MouseClicked(evt);
            }
        });
        btnPos15.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnPos15KeyPressed(evt);
            }
        });
        panelHeader.add(btnPos15);
        btnPos15.setBounds(580, 300, 121, 80);

        btnPrev.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/spos/images/imgPreviousButton.png"))); // NOI18N
        btnPrev.setToolTipText("");
        btnPrev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrevActionPerformed(evt);
            }
        });
        panelHeader.add(btnPrev);
        btnPrev.setBounds(60, 71, 100, 40);

        btnNext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/spos/images/imgNextButton.png"))); // NOI18N
        btnNext.setToolTipText("");
        btnNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextActionPerformed(evt);
            }
        });
        panelHeader.add(btnNext);
        btnNext.setBounds(210, 70, 100, 40);

        lblBackgroundImage.setBackground(new java.awt.Color(153, 153, 153));
        lblBackgroundImage.setForeground(new java.awt.Color(255, 255, 255));
        panelHeader.add(lblBackgroundImage);
        lblBackgroundImage.setBounds(-10, 0, 830, 560);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 818, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(panelHeader, javax.swing.GroupLayout.PREFERRED_SIZE, 818, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 570, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(panelHeader, javax.swing.GroupLayout.PREFERRED_SIZE, 560, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        jPanel2.add(jPanel3, new java.awt.GridBagConstraints());

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void lblLogOutMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblLogOutMouseClicked
        // TODO add your handling code here:
        frmOkCancelPopUp objOKCancel = new frmOkCancelPopUp(this, "Do you want to log out??");
        objOKCancel.setVisible(true);
        if (objOKCancel.getResult() == 1)
        {
            dispose();
            new frmLogin().setVisible(true);
        }
    }//GEN-LAST:event_lblLogOutMouseClicked

    private void btnPos5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPos5MouseClicked
        // TODO add your handling code here:
        dispose();
        /*clsGlobalVarClass.setPOSCode(posCode[4]);
         clsGlobalVarClass.setPOSName(psNames[4]);
         clsGlobalVarClass.gDebitCardPayment=debitCardYN[4];
         clsGlobalVarClass.setPropertyCode(propertyCode[4]);
         clsGlobalVarClass.gDelayedSettlementForDB=delayedSett[4];
         clsGlobalVarClass.setCounterWiseBilling(counterWiseBilling[4]);
         clsGlobalVarClass.gBillPrintPrinterPort=billPrinterPort[4];*/

        funInitPOSSessionValues(btnPos5.getText().trim());
        new frmMainMenu().setVisible(true);
    }//GEN-LAST:event_btnPos5MouseClicked

    private void btnPos2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPos2MouseClicked
        // TODO add your handling code here:
        dispose();
        funInitPOSSessionValues(btnPos2.getText().trim());
        new frmMainMenu().setVisible(true);
    }//GEN-LAST:event_btnPos2MouseClicked

    private void btnPos3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPos3MouseClicked
        // TODO add your handling code here:
        dispose();
        funInitPOSSessionValues(btnPos3.getText().trim());
        new frmMainMenu().setVisible(true);
    }//GEN-LAST:event_btnPos3MouseClicked

    private void btnPos4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPos4MouseClicked
        // TODO add your handling code here:
        dispose();
        funInitPOSSessionValues(btnPos4.getText().trim());
        new frmMainMenu().setVisible(true);
    }//GEN-LAST:event_btnPos4MouseClicked

    private void btnPos1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPos1MouseClicked
        // TODO add your handling code here:
        dispose();
        funInitPOSSessionValues(btnPos1.getText().trim());
        new frmMainMenu().setVisible(true);
    }//GEN-LAST:event_btnPos1MouseClicked

    private void btnPos7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPos7MouseClicked
        // TODO add your handling code here:
        dispose();
        funInitPOSSessionValues(btnPos7.getText().trim());
        new frmMainMenu().setVisible(true);
    }//GEN-LAST:event_btnPos7MouseClicked

    private void btnPos8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPos8MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnPos8MouseClicked

    private void btnPos8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPos8ActionPerformed
        // TODO add your handling code here:
        dispose();
        funInitPOSSessionValues(btnPos8.getText().trim());
        new frmMainMenu().setVisible(true);
    }//GEN-LAST:event_btnPos8ActionPerformed

    private void btnPos9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPos9MouseClicked
        // TODO add your handling code here:
        dispose();
        funInitPOSSessionValues(btnPos9.getText().trim());
        new frmMainMenu().setVisible(true);
    }//GEN-LAST:event_btnPos9MouseClicked

    private void btnPosMastMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPosMastMouseClicked
        // TODO add your handling code here:
        new frmPOSMaster(clsGlobalVarClass.gUserCode).setVisible(true);
    }//GEN-LAST:event_btnPosMastMouseClicked

    private void btnPos6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPos6MouseClicked
        // TODO add your handling code here:
        dispose();
        funInitPOSSessionValues(btnPos6.getText().trim());
        new frmMainMenu().setVisible(true);
    }//GEN-LAST:event_btnPos6MouseClicked

    private void btnPos11MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPos11MouseClicked
        // TODO add your handling code here:
        dispose();
        funInitPOSSessionValues(btnPos11.getText().trim());
        new frmMainMenu().setVisible(true);
    }//GEN-LAST:event_btnPos11MouseClicked

    private void btnPos12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPos12ActionPerformed
        // TODO add your handling code here:
        dispose();
        funInitPOSSessionValues(btnPos12.getText().trim());
        new frmMainMenu().setVisible(true);
    }//GEN-LAST:event_btnPos12ActionPerformed

    private void btnPos13MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPos13MouseClicked
        // TODO add your handling code here:
        dispose();
        funInitPOSSessionValues(btnPos13.getText().trim());
        new frmMainMenu().setVisible(true);
    }//GEN-LAST:event_btnPos13MouseClicked

    private void btnPos14MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPos14MouseClicked
        // TODO add your handling code here:
        dispose();
        funInitPOSSessionValues(btnPos14.getText().trim());
        new frmMainMenu().setVisible(true);
    }//GEN-LAST:event_btnPos14MouseClicked

    private void btnPos10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPos10MouseClicked
        // TODO add your handling code here:
        dispose();
        funInitPOSSessionValues(btnPos10.getText().trim());
        new frmMainMenu().setVisible(true);
    }//GEN-LAST:event_btnPos10MouseClicked

    private void btnPos15MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPos15MouseClicked
        // TODO add your handling code here:
        dispose();
        funInitPOSSessionValues(btnPos15.getText().trim());
        new frmMainMenu().setVisible(true);
    }//GEN-LAST:event_btnPos15MouseClicked

    private void btnPos1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnPos1KeyPressed
        int buttonNo = 0;
        int keyCode = evt.getKeyCode();
        switch (keyCode)
        {
            //left
            case 37:
                funMoveFocusLeft(buttonNo);
                break;
            //up            
            case 38:
                funMoveFocusUp(buttonNo);
                break;
            //right            
            case 39:
                funMoveFocusRight(buttonNo);
                break;
            //down            
            case 40:
                funMoveFocusDown(buttonNo);
                break;
            //Entered    
            case 10:
                dispose();
                funInitPOSSessionValues(btnPos1.getText().trim());
                new frmMainMenu().setVisible(true);
                break;
        }
    }//GEN-LAST:event_btnPos1KeyPressed

    private void btnPos2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnPos2KeyPressed
        int buttonNo = 1;
        int keyCode = evt.getKeyCode();
        switch (keyCode)
        {
            //left
            case 37:
                funMoveFocusLeft(buttonNo);
                break;
            //up            
            case 38:
                funMoveFocusUp(buttonNo);
                break;
            //right            
            case 39:
                funMoveFocusRight(buttonNo);
                break;
            //down            
            case 40:
                funMoveFocusDown(buttonNo);
                break;
            //Entered    
            case 10:
                dispose();
                funInitPOSSessionValues(btnPos2.getText().trim());
                new frmMainMenu().setVisible(true);
                break;
        }
    }//GEN-LAST:event_btnPos2KeyPressed

    private void btnPos3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnPos3KeyPressed

        int buttonNo = 2;
        int keyCode = evt.getKeyCode();
        switch (keyCode)
        {
            //left
            case 37:
                funMoveFocusLeft(buttonNo);
                break;
            //up            
            case 38:
                funMoveFocusUp(buttonNo);
                break;
            //right            
            case 39:
                funMoveFocusRight(buttonNo);
                break;
            //down            
            case 40:
                funMoveFocusDown(buttonNo);
                break;
            //Entered    
            case 10:
                dispose();
                funInitPOSSessionValues(btnPos3.getText().trim());
                new frmMainMenu().setVisible(true);
                break;
        }
    }//GEN-LAST:event_btnPos3KeyPressed

    private void btnPos4KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnPos4KeyPressed
        int buttonNo = 3;
        int keyCode = evt.getKeyCode();
        switch (keyCode)
        {
            //left
            case 37:
                funMoveFocusLeft(buttonNo);
                break;
            //up            
            case 38:
                funMoveFocusUp(buttonNo);
                break;
            //right            
            case 39:
                funMoveFocusRight(buttonNo);
                break;
            //down            
            case 40:
                funMoveFocusDown(buttonNo);
                break;
            //Entered    
            case 10:
                dispose();
                funInitPOSSessionValues(btnPos4.getText().trim());
                new frmMainMenu().setVisible(true);
                break;
        }
    }//GEN-LAST:event_btnPos4KeyPressed

    private void btnPos5KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnPos5KeyPressed

        int buttonNo = 4;
        int keyCode = evt.getKeyCode();
        switch (keyCode)
        {
            //left
            case 37:
                funMoveFocusLeft(buttonNo);
                break;
            //up            
            case 38:
                funMoveFocusUp(buttonNo);
                break;
            //right            
            case 39:
                funMoveFocusRight(buttonNo);
                break;
            //down            
            case 40:
                funMoveFocusDown(buttonNo);
                break;
            //Entered    
            case 10:
                dispose();
                funInitPOSSessionValues(btnPos5.getText().trim());
                new frmMainMenu().setVisible(true);
                break;
        }
    }//GEN-LAST:event_btnPos5KeyPressed

    private void btnPos6KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnPos6KeyPressed

        int buttonNo = 5;
        int keyCode = evt.getKeyCode();
        switch (keyCode)
        {
            //left
            case 37:
                funMoveFocusLeft(buttonNo);
                break;
            //up            
            case 38:
                funMoveFocusUp(buttonNo);
                break;
            //right            
            case 39:
                funMoveFocusRight(buttonNo);
                break;
            //down            
            case 40:
                funMoveFocusDown(buttonNo);
                break;
            //Entered    
            case 10:
                dispose();
                funInitPOSSessionValues(btnPos6.getText().trim());
                new frmMainMenu().setVisible(true);
                break;
        }
    }//GEN-LAST:event_btnPos6KeyPressed

    private void btnPos7KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnPos7KeyPressed
        int buttonNo = 6;
        int keyCode = evt.getKeyCode();
        switch (keyCode)
        {
            //left
            case 37:
                funMoveFocusLeft(buttonNo);
                break;
            //up            
            case 38:
                funMoveFocusUp(buttonNo);
                break;
            //right            
            case 39:
                funMoveFocusRight(buttonNo);
                break;
            //down            
            case 40:
                funMoveFocusDown(buttonNo);
                break;
            //Entered    
            case 10:
                dispose();
                funInitPOSSessionValues(btnPos7.getText().trim());
                new frmMainMenu().setVisible(true);
                break;
        }
    }//GEN-LAST:event_btnPos7KeyPressed

    private void btnPos8KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnPos8KeyPressed
        int buttonNo = 7;
        int keyCode = evt.getKeyCode();
        switch (keyCode)
        {
            //left
            case 37:
                funMoveFocusLeft(buttonNo);
                break;
            //up            
            case 38:
                funMoveFocusUp(buttonNo);
                break;
            //right            
            case 39:
                funMoveFocusRight(buttonNo);
                break;
            //down            
            case 40:
                funMoveFocusDown(buttonNo);
                break;
            //Entered    
            case 10:
                dispose();
                funInitPOSSessionValues(btnPos8.getText().trim());
                new frmMainMenu().setVisible(true);
                break;
        }
    }//GEN-LAST:event_btnPos8KeyPressed

    private void btnPos9KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnPos9KeyPressed
        int buttonNo = 8;
        int keyCode = evt.getKeyCode();
        switch (keyCode)
        {
            //left
            case 37:
                funMoveFocusLeft(buttonNo);
                break;
            //up            
            case 38:
                funMoveFocusUp(buttonNo);
                break;
            //right            
            case 39:
                funMoveFocusRight(buttonNo);
                break;
            //down            
            case 40:
                funMoveFocusDown(buttonNo);
                break;
            //Entered    
            case 10:
                dispose();
                funInitPOSSessionValues(btnPos9.getText().trim());
                new frmMainMenu().setVisible(true);
                break;
        }
    }//GEN-LAST:event_btnPos9KeyPressed

    private void btnPos10KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnPos10KeyPressed
        int length = posButtons.length;
        int buttonNo = 9;
        int keyCode = evt.getKeyCode();
        switch (keyCode)
        {
            //left
            case 37:
                funMoveFocusLeft(buttonNo);
                break;
            //up            
            case 38:
                funMoveFocusUp(buttonNo);
                break;
            //right            
            case 39:
                funMoveFocusRight(buttonNo);
                break;
            //down            
            case 40:
                funMoveFocusDown(buttonNo);
                break;
            //Entered    
            case 10:
                dispose();
                funInitPOSSessionValues(btnPos10.getText().trim());
                new frmMainMenu().setVisible(true);
                break;
        }
    }//GEN-LAST:event_btnPos10KeyPressed

    private void btnPos11KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnPos11KeyPressed
        int buttonNo = 10;
        int keyCode = evt.getKeyCode();
        switch (keyCode)
        {
            //left
            case 37:
                funMoveFocusLeft(buttonNo);
                break;
            //up            
            case 38:
                funMoveFocusUp(buttonNo);
                break;
            //right            
            case 39:
                funMoveFocusRight(buttonNo);
                break;
            //down            
            case 40:
                funMoveFocusDown(buttonNo);
                break;
            //Entered    
            case 10:
                dispose();
                funInitPOSSessionValues(btnPos11.getText().trim());
                new frmMainMenu().setVisible(true);
                break;
        }
    }//GEN-LAST:event_btnPos11KeyPressed

    private void btnPos12KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnPos12KeyPressed
        int buttonNo = 11;
        int keyCode = evt.getKeyCode();
        switch (keyCode)
        {
            //left
            case 37:
                funMoveFocusLeft(buttonNo);
                break;
            //up            
            case 38:
                funMoveFocusUp(buttonNo);
                break;
            //right            
            case 39:
                funMoveFocusRight(buttonNo);
                break;
            //down            
            case 40:
                funMoveFocusDown(buttonNo);
                break;
            //Entered    
            case 10:
                dispose();
                funInitPOSSessionValues(btnPos12.getText().trim());
                new frmMainMenu().setVisible(true);
                break;
        }
    }//GEN-LAST:event_btnPos12KeyPressed

    private void btnPos13KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnPos13KeyPressed
        int buttonNo = 12;
        int keyCode = evt.getKeyCode();
        switch (keyCode)
        {
            //left
            case 37:
                funMoveFocusLeft(buttonNo);
                break;
            //up            
            case 38:
                funMoveFocusUp(buttonNo);
                break;
            //right            
            case 39:
                funMoveFocusRight(buttonNo);
                break;
            //down            
            case 40:
                funMoveFocusDown(buttonNo);
                break;
            //Entered    
            case 10:
                dispose();
                funInitPOSSessionValues(btnPos13.getText().trim());
                new frmMainMenu().setVisible(true);
                break;
        }
    }//GEN-LAST:event_btnPos13KeyPressed

    private void btnPos14KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnPos14KeyPressed
        int buttonNo = 13;
        int keyCode = evt.getKeyCode();
        switch (keyCode)
        {
            //left
            case 37:
                funMoveFocusLeft(buttonNo);
                break;
            //up            
            case 38:
                funMoveFocusUp(buttonNo);
                break;
            //right            
            case 39:
                funMoveFocusRight(buttonNo);
                break;
            //down            
            case 40:
                funMoveFocusDown(buttonNo);
                break;
            //Entered    
            case 10:
                dispose();
                funInitPOSSessionValues(btnPos14.getText().trim());
                new frmMainMenu().setVisible(true);
                break;
        }
    }//GEN-LAST:event_btnPos14KeyPressed

    private void btnPos15KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnPos15KeyPressed
        int buttonNo = 14;
        int keyCode = evt.getKeyCode();
        switch (keyCode)
        {
            //left
            case 37:
                funMoveFocusLeft(buttonNo);
                break;
            //up            
            case 38:
                funMoveFocusUp(buttonNo);
                break;
            //right            
            case 39:
                funMoveFocusRight(buttonNo);
                break;
            //down            
            case 40:
                funMoveFocusDown(buttonNo);
                break;
            //Entered    
            case 10:
                dispose();
                funInitPOSSessionValues(btnPos15.getText().trim());
                new frmMainMenu().setVisible(true);
                break;
        }
    }//GEN-LAST:event_btnPos15KeyPressed

    private void btnPrevActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnPrevActionPerformed
    {//GEN-HEADEREND:event_btnPrevActionPerformed
        // TODO add your handling code here:
        funPreviousButtonClicked();
    }//GEN-LAST:event_btnPrevActionPerformed

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnNextActionPerformed
    {//GEN-HEADEREND:event_btnNextActionPerformed
        // TODO add your handling code here:
        funNextButtonClicked();
    }//GEN-LAST:event_btnNextActionPerformed

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
            java.util.logging.Logger.getLogger(frmPOSSelection.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(frmPOSSelection.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(frmPOSSelection.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(frmPOSSelection.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                new frmPOSSelection().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnPos1;
    private javax.swing.JButton btnPos10;
    private javax.swing.JButton btnPos11;
    private javax.swing.JButton btnPos12;
    private javax.swing.JButton btnPos13;
    private javax.swing.JButton btnPos14;
    private javax.swing.JButton btnPos15;
    private javax.swing.JButton btnPos2;
    private javax.swing.JButton btnPos3;
    private javax.swing.JButton btnPos4;
    private javax.swing.JButton btnPos5;
    private javax.swing.JButton btnPos6;
    private javax.swing.JButton btnPos7;
    private javax.swing.JButton btnPos8;
    private javax.swing.JButton btnPos9;
    private javax.swing.JButton btnPosMast;
    private javax.swing.JButton btnPrev;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JLabel lblBackgroundImage;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblLogOut;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblformName;
    private javax.swing.JPanel panelHeader;
    // End of variables declaration//GEN-END:variables

    private void funMoveFocusRight(int buttonNo)
    {
        int focusbutton = buttonNo + 1;
        for (int i = 0; i < posButtons.length; i++)
        {
            posButtons[i].setBorder(null);
        }
        if (buttonNo >= posButtons.length - 1)
        {
            posButtons[0].requestFocus();
            posButtons[0].setBorder(new BevelBorder(BevelBorder.LOWERED));
        }
        else
        {
            if (focusbutton < posButtons.length)
            {
                if (posButtons[focusbutton].isVisible())
                {
                    posButtons[focusbutton].requestFocus();
                    posButtons[focusbutton].setBorder(new BevelBorder(BevelBorder.LOWERED));
                }
                else
                {
                    posButtons[0].requestFocus();
                    posButtons[0].setBorder(new BevelBorder(BevelBorder.LOWERED));
                    posButtons[buttonNo].setBorder(null);
                }
            }
        }
    }

    private void funMoveFocusLeft(int buttonNo)
    {
        int focusbutton = buttonNo - 1;
        for (int i = 0; i < posButtons.length; i++)
        {
            posButtons[i].setBorder(null);
        }
        if (buttonNo == 0)
        {
            for (int i = posButtons.length - 1; i >= 0; i--)
            {
                if (posButtons[i].isVisible())
                {
                    posButtons[i].requestFocus();
                    posButtons[i].setBorder(new BevelBorder(BevelBorder.LOWERED));
                    break;
                }

            }
        }
        else
        {
            posButtons[focusbutton].requestFocus();
            posButtons[focusbutton].setBorder(new BevelBorder(BevelBorder.LOWERED));
        }
    }

    private void funMoveFocusDown(int buttonNo)
    {
        int focusButtonNo = (buttonNo + 5) % posButtons.length;
        for (int i = 0; i < posButtons.length; i++)
        {
            posButtons[i].setBorder(null);
        }
        if (posButtons[focusButtonNo].isVisible())
        {
            posButtons[focusButtonNo].requestFocus();
            posButtons[focusButtonNo].setBorder(new BevelBorder(BevelBorder.LOWERED));
        }
        else
        {
            funMoveFocusDown(focusButtonNo);
        }
    }

    private void funMoveFocusUp(int buttonNo)
    {
        int focusButtonNo = (buttonNo - 5) % posButtons.length;
        for (int i = 0; i < posButtons.length; i++)
        {
            posButtons[i].setBorder(null);
        }
        if (focusButtonNo < 0)
        {
            focusButtonNo = posButtons.length + focusButtonNo;
            if (posButtons[focusButtonNo].isVisible())
            {
                posButtons[focusButtonNo].requestFocus();
                posButtons[focusButtonNo].setBorder(new BevelBorder(BevelBorder.LOWERED));
            }
            else
            {
                funMoveFocusUp(focusButtonNo);
            }
        }
        else
        {
            if (posButtons[focusButtonNo].isVisible())
            {
                posButtons[focusButtonNo].requestFocus();
                posButtons[focusButtonNo].setBorder(new BevelBorder(BevelBorder.LOWERED));
            }
            else
            {
                funMoveFocusUp(focusButtonNo);
            }
        }
    }
}
