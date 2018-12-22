/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.POSTransaction.view;

import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsUtility;
import com.POSGlobal.view.frmOkPopUp;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.util.Date;
import java.util.Vector;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.Attribute;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class frmTableStatusView extends javax.swing.JFrame {
    
    public java.util.Vector vTableNo,vTableName,vOpenTableNo,vOpenTableName;
    public String sql,clsTableNo,clsTableName,clsOpenTableNo,clsOpenTableName;
    public int cntNavigate,cntNavigate1,tblStartIndex, tblEndIndex;
    
    public frmTableStatusView() {
        initComponents();
        try
        {
            vTableNo=new Vector();
            vTableName=new Vector();
            vOpenTableNo=new Vector();
            vOpenTableName=new Vector();
            btnPrevious.setEnabled(false);
            btnPrevious1.setEnabled(false);
            cntNavigate=0;
            cntNavigate1=0;
            lblPosName.setText(clsGlobalVarClass.gPOSName);
            Date date1 = new Date();
            String new_str = String.format("%tr", date1 );
            String dateAndTime=clsGlobalVarClass.gPOSDateToDisplay+" "+new_str;
            lblDate.setText(dateAndTime);
            lblUserCode.setText(clsGlobalVarClass.gUserCode);
            lblModuleName.setText(clsGlobalVarClass.gSelectedModule);

            funFillTableVector();
            
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    private void funFillTableVector()
    {
        try
        {
            cntNavigate=0;
            cntNavigate1=0;
             
            btnPrevious.setEnabled(false);
            btnPrevious1.setEnabled(false);
            btnNext.setEnabled(true);
            btnNext1.setEnabled(true);
            vTableNo.removeAllElements();
            vOpenTableNo.removeAllElements();
            vTableName.removeAllElements();
            vOpenTableName.removeAllElements();
            sql="select strTableNo,strTableName from tbltablemaster ";
            if(!clsGlobalVarClass.gMoveTableToOtherPOS)
            {
                sql+= " where strPOSCode='"+clsGlobalVarClass.gPOSCode+"' ";
            }
            sql+= "order by intSequence;";
            ResultSet rsTblNo=clsGlobalVarClass.dbMysql.executeResultSet(sql);
            while(rsTblNo.next())
            {
                vTableNo.add(rsTblNo.getString(1));
                vTableName.add(rsTblNo.getString(2));
            }            
            
            sql="select strTableNo,strTableName from tbltablemaster "
                + "where strStatus='Occupied' ";
            if(!clsGlobalVarClass.gMoveTableToOtherPOS)
            {
                sql+= " and strPOSCode='"+clsGlobalVarClass.gPOSCode+"' ";
            }
            sql+= "order by intSequence;";
            rsTblNo=clsGlobalVarClass.dbMysql.executeResultSet(sql);
            while(rsTblNo.next())
            {
                vOpenTableNo.add(rsTblNo.getString(1));
                vOpenTableName.add(rsTblNo.getString(2));
            }
            funLoadTables(0,vTableNo.size());
            funLoadOpenTables(0,vOpenTableNo.size());
             
            if(vTableNo.size()<=16)
            {
                btnNext1.setEnabled(false);
            }
            if(vOpenTableNo.size()<=16)
            {
                btnNext.setEnabled(false);
            }
            
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    private void funLoadTables(int startIndex,int totalSize)
    {
        try
        {
            int cntIndex=0;
             
            JButton[] btnTableArray = {btnTableNo1,btnTableNo2,btnTableNo3,btnTableNo4,btnTableNo5,btnTableNo6,btnTableNo7,btnTableNo8,btnTableNo9,btnTableNo10,btnTableNo11,btnTableNo12,btnTableNo13,btnTableNo14,btnTableNo15,btnTableNo16};
            for(int k=0;k<btnTableArray.length;k++)
            {
                btnTableArray[k].setForeground(Color.black);
                btnTableArray[k].setBackground(Color.lightGray);
                btnTableArray[k].setText("");
            }
            for(int i=startIndex;i<totalSize;i++)
            {
                if(i==vTableNo.size())
                {
                    break;
                }
                String tblName=vTableName.elementAt(i).toString();
                sql="select strTableNo,strStatus from tbltablemaster "
                    + " where strTableNo='"+vTableNo.elementAt(i).toString()+"' ";
                if(!clsGlobalVarClass.gMoveTableToOtherPOS)
                {
                    sql+= " and strPOSCode='"+clsGlobalVarClass.gPOSCode+"' ";
                }
                ResultSet rs=clsGlobalVarClass.dbMysql.executeResultSet(sql);
                rs.next();
                String status=rs.getString(2);
                int pax=0;
                if(cntIndex<16)
                {
                    if(status.equals("Occupied"))
                    {
                        sql="select intPaxNo from tblitemrtemp "
                            + " where strTableNo='"+vTableNo.elementAt(i).toString()+"' ";
                        if(!clsGlobalVarClass.gMoveTableToOtherPOS)
                        {
                            sql+= " and strPOSCode='"+clsGlobalVarClass.gPOSCode+"' ";
                        }
                        ResultSet tblRs=clsGlobalVarClass.dbMysql.executeResultSet(sql);
                        if(tblRs.next())
                        {
                            pax=tblRs.getInt(1);
                        }
                        tblRs.close();
                    }
                    if(tblName.contains(" "))
                    {
                        StringBuilder sb=new StringBuilder(tblName);
                        int len = sb.length();
                        int seq = sb.lastIndexOf(" ");
                        String split = sb.substring(0, seq);
                        String last = sb.substring(seq + 1, len);
                        
                        if(status.equals("Occupied"))
                        {
                            btnTableArray[cntIndex].setForeground(Color.white);
                            btnTableArray[cntIndex].setBackground(Color.red);
                        }
                        else if(status.equals("Billed"))
                        {
                            btnTableArray[cntIndex].setForeground(Color.black);
                            btnTableArray[cntIndex].setBackground(Color.lightGray);
                        }
                        else if(status.equals("Normal"))
                        {
                            btnTableArray[cntIndex].setForeground(Color.black);
                            btnTableArray[cntIndex].setBackground(Color.lightGray);
                        }
                        btnTableArray[cntIndex].setText("<html>" + split + "<br>" + last +"<br>"+pax +"</html>");
                    }
                    else
                    {
                        if(status.equals("Occupied"))
                        {
                           btnTableArray[cntIndex].setForeground(Color.white);
                            btnTableArray[cntIndex].setBackground(Color.red);
                        }
                        else if(status.equals("Billed"))
                        {
                            btnTableArray[cntIndex].setForeground(Color.black);
                            btnTableArray[cntIndex].setBackground(Color.lightGray);
                        }
                        else if(status.equals("Normal"))
                        {
                            btnTableArray[cntIndex].setForeground(Color.black);
                            btnTableArray[cntIndex].setBackground(Color.lightGray);
                        }
                        btnTableArray[cntIndex].setText("<html>"+tblName+"<br>"+pax+"</html>");
                    }
                    btnTableArray[cntIndex].setEnabled(true);
                    cntIndex++;
                }
            }
            for(int j=cntIndex;j<16;j++)
            {
                btnTableArray[j].setEnabled(false);
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }    
    }
    
    private void funLoadOpenTables(int startIndex,int totalSize)
    {
        try
        {
            int cntIndex=0;
             
            JButton[] btnTableArray = {btnOpenTable1,btnOpenTable2,btnOpenTable3,btnOpenTable4,btnOpenTable5,btnOpenTable6,btnOpenTable7,btnOpenTable8,btnOpenTable9,btnOpenTable10,btnOpenTable11,btnOpenTable12,btnOpenTable13,btnOpenTable14,btnOpenTable15,btnOpenTable16};
            for(int k=0;k<btnTableArray.length;k++)
            {
                btnTableArray[k].setForeground(Color.black);
                  btnTableArray[k].setBackground(Color.lightGray);
                btnTableArray[k].setText("");
            }
            System.out.println(vOpenTableName.size());
            for(int i=startIndex;i<totalSize;i++)
            {
                if(i==vOpenTableName.size())
                {
                    break;
                }
                String tblName=vOpenTableName.elementAt(i).toString();
                sql="select strTableNo,strStatus from tbltablemaster where strTableNo='"+vOpenTableNo.elementAt(i).toString()+"' ;";
                ResultSet rs=clsGlobalVarClass.dbMysql.executeResultSet(sql);
                rs.next();
                String status=rs.getString(2);
                int pax=0;
                if(cntIndex<16)
                {
                    if(status.equals("Occupied"))
                    {
                        sql="select intPaxNo from tblitemrtemp where strTableNo='"+vOpenTableNo.elementAt(i).toString()+"'";
                        ResultSet tblRs=clsGlobalVarClass.dbMysql.executeResultSet(sql);
                        if(tblRs.next())
                        {
                            pax=tblRs.getInt(1);
                        }
                    }
                    if(tblName.contains(" "))
                    {
                        StringBuilder sb=new StringBuilder(tblName);
                        int len = sb.length();
                        int seq = sb.lastIndexOf(" ");
                        String split = sb.substring(0, seq);
                        String last = sb.substring(seq + 1, len);
                        
                        if(status.equals("Occupied"))
                        {
                            btnTableArray[cntIndex].setForeground(Color.white);
                            btnTableArray[cntIndex].setBackground(Color.red);
                        }
                        else if(status.equals("Billed"))
                        {
                            btnTableArray[cntIndex].setForeground(Color.black);
                            btnTableArray[cntIndex].setBackground(Color.lightGray);
                        }
                        else if(status.equals("Normal"))
                        {
                            btnTableArray[cntIndex].setForeground(Color.black);
                            btnTableArray[cntIndex].setBackground(Color.lightGray);
                        }
                        btnTableArray[cntIndex].setText("<html>" + split + "<br>" + last +"<br>"+pax +"</html>");
                    }
                    else
                    {
                        if(status.equals("Occupied"))
                        {
                             btnTableArray[cntIndex].setForeground(Color.white);
                            btnTableArray[cntIndex].setBackground(Color.red);
                            
                        }
                        else if(status.equals("Billed"))
                        {
                              btnTableArray[cntIndex].setForeground(Color.black);
                            btnTableArray[cntIndex].setBackground(Color.lightGray);
                        }
                        else if(status.equals("Normal"))
                        {
                            btnTableArray[cntIndex].setForeground(Color.black);
                            btnTableArray[cntIndex].setBackground(Color.lightGray);
                        }
                        btnTableArray[cntIndex].setText("<html>"+tblName+"<br>"+pax+"</html>");
                    }
                    btnTableArray[cntIndex].setEnabled(true);
                    cntIndex++;
                }
            }
            //System.out.println("Open Table Index="+cntIndex);
            for(int j=cntIndex;j<16;j++)
            {
                btnTableArray[j].setEnabled(false);
            }
             
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
   
    private void funSelectTable(String tableName,int index)
    {
        try
        {
           if(tableName.trim().length()>0)
           {
                index=(cntNavigate1*16)+index;
                clsTableNo=vTableNo.elementAt(index).toString();
                clsTableName=vTableName.elementAt(index).toString();
                lblAllTableName.setText(vTableName.elementAt(index).toString());
           }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    private void funSelectOpenTable(String tableName,int index)
    {
        try
        {
            if(tableName.trim().length()>0)
            {
                index=(cntNavigate*16)+index;
                lblOpenTableName.setText(vOpenTableName.elementAt(index).toString());
                clsOpenTableNo=vOpenTableNo.elementAt(index).toString();
                clsOpenTableName=vOpenTableName.elementAt(index).toString();
            }
            
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    private boolean funValidateTables()
    {
        boolean flgValidate=true;
        try
        {
            if(clsTableName.length()==0 )
            {
                JOptionPane.showMessageDialog(this, "Select table from All Tables");
                flgValidate=false;
            }
            else if(clsOpenTableName.length()==0)
            {
                JOptionPane.showMessageDialog(this, "Select table from Open Tables");
                flgValidate=false;
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return flgValidate;
    }
    
    private void funMoveTable()
    {
        clsUtility objUtility=new clsUtility();
        try
        {
            if(clsTableNo.equalsIgnoreCase(clsOpenTableNo))
            {
                new frmOkPopUp(this,"Can Not Move On Same Table","Error",1).setVisible(true);
            }
            else
            {
                sql="update tblitemrtemp set strTableNo='"+clsTableNo+"' "
                    + "where strTableNo='"+clsOpenTableNo+"'";
                clsGlobalVarClass.dbMysql.execute(sql);
                
                sql="select strStatus,intPaxNo,strTableName "
                    + "from tbltablemaster where strTableNo='"+clsOpenTableNo+"'";
                ResultSet rs=clsGlobalVarClass.dbMysql.executeResultSet(sql);
                if(rs.next())
                {
                    String status=rs.getString(1);
                    int pax=rs.getInt(2);
                    String tableName=rs.getString(3);
                    
                    sql="update tbltablemaster set strStatus='"+status+"',intPaxNo="+pax+" "
                        + "where strTableNo='"+clsTableNo+"'";
                    clsGlobalVarClass.dbMysql.execute(sql);
                    
                    sql="update tbltablemaster set strStatus='Normal',intPaxNo=0 "
                        + "where strTableNo='"+clsOpenTableNo+"'";
                    clsGlobalVarClass.dbMysql.execute(sql);
                    
                    //Update Table Status to Inresto POS
                    if (clsGlobalVarClass.gInrestoPOSIntegrationYN)
                    {
                        objUtility.funUpdateTableStatusToInrestoApp(clsTableNo,tableName.trim(),status);
                        
                        String sqlTableName="select strTableName from tbltablemaster where strTableNo='"+clsOpenTableNo+"'";
                        ResultSet rsTableName=clsGlobalVarClass.dbMysql.executeResultSet(sqlTableName);
                        if(rsTableName.next())
                        {
                            objUtility.funUpdateTableStatusToInrestoApp(clsOpenTableNo,rsTableName.getString(1).trim(),status);
                        }
                        rsTableName.close();
                    }
                }
                rs.close();
                
                sql="select strPOSCode from tbltablemaster "
                    + " where strTableNo='"+clsTableNo+"'";
                rs=clsGlobalVarClass.dbMysql.executeResultSet(sql);
                if(rs.next())
                {
                    String posCode=rs.getString(1);
                    sql="update tblitemrtemp set strPOSCode='"+posCode+"' "
                        + " where strTableNo='"+clsTableNo+"'";
                    clsGlobalVarClass.dbMysql.execute(sql);
                }
                rs.close();
                
                //send message to all cost centers 
                funSendMessageToCostCenters();
                
                JOptionPane.showMessageDialog(this,clsOpenTableName+" Shifted to "+clsTableName);                                                                
                funFillTableVector();
                funClearFields();
             }
             
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
     
    private void funClearFields()
    {
        clsTableName="";
        clsOpenTableName="";
        lblAllTableName.setText("");
        lblOpenTableName.setText("");
    }
    
    private void funSetDefaultColorAll(int btnIndex)
    {
        try
        {            
            JButton[] btnTableArray = {btnTableNo1,btnTableNo2,btnTableNo3,btnTableNo4,btnTableNo5,btnTableNo6,btnTableNo7,btnTableNo8,btnTableNo9,btnTableNo10,btnTableNo11,btnTableNo12,btnTableNo13,btnTableNo14,btnTableNo15,btnTableNo16};
            Color btnColor=btnTableArray[btnIndex].getBackground();
            if(btnColor!=Color.black)
            {
                btnTableArray[btnIndex].setBackground(Color.black);
                for(int cnt=0;cnt<btnTableArray.length;cnt++)
                {
                    if(cnt!=btnIndex)
                    {
                        btnTableArray[cnt].setBackground(btnColor);
                    }
                }
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    private void funSetDefaultColorOpen(String tableName ,int btnIndex)
    {
        try
        {
            if(tableName.trim().length()>0)
            {
                JButton[] btnTableArray = {btnOpenTable1,btnOpenTable2,btnOpenTable3,btnOpenTable4,btnOpenTable5,btnOpenTable6,btnOpenTable7,btnOpenTable8,btnOpenTable9,btnOpenTable10,btnOpenTable11,btnOpenTable12,btnOpenTable13,btnOpenTable14,btnOpenTable15,btnOpenTable16};
                Color btnColor=btnTableArray[btnIndex].getBackground();
                if(btnColor!=Color.black)
                {
                    btnTableArray[btnIndex].setBackground(Color.black);
                    for(int cnt=0;cnt<btnTableArray.length;cnt++)
                    {
                        if(cnt!=btnIndex)
                        {
                            btnTableArray[cnt].setBackground(btnColor);
                        }
                    }
                }
            }
        }catch(Exception e)
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
    private void initComponents() {

        lblformName = new javax.swing.JPanel();
        lblProductName = new javax.swing.JLabel();
        lblModuleName = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        filler4 = new javax.swing.Box.Filler(new java.awt.Dimension(20, 0), new java.awt.Dimension(20, 0), new java.awt.Dimension(20, 32767));
        filler5 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        lblPosName = new javax.swing.JLabel();
        filler6 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        lblUserCode = new javax.swing.JLabel();
        lblDate = new javax.swing.JLabel();
        lblHOSign = new javax.swing.JLabel();
        panelMainForm = new JPanel() {  
            public void paintComponent(Graphics g) {  
                Image img = Toolkit.getDefaultToolkit().getImage(  
                    getClass().getResource("/com/POSTransaction/images/imgBackgroundImage.png"));  
                g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);  
            }  
        };  ;
        panelFormBody = new javax.swing.JPanel();
        panelOpenTable = new javax.swing.JPanel();
        btnOpenTable2 = new javax.swing.JButton();
        btnOpenTable1 = new javax.swing.JButton();
        btnOpenTable3 = new javax.swing.JButton();
        btnOpenTable4 = new javax.swing.JButton();
        btnOpenTable5 = new javax.swing.JButton();
        btnOpenTable6 = new javax.swing.JButton();
        btnOpenTable7 = new javax.swing.JButton();
        btnOpenTable8 = new javax.swing.JButton();
        btnOpenTable9 = new javax.swing.JButton();
        btnOpenTable10 = new javax.swing.JButton();
        btnOpenTable11 = new javax.swing.JButton();
        btnOpenTable12 = new javax.swing.JButton();
        btnOpenTable13 = new javax.swing.JButton();
        btnOpenTable14 = new javax.swing.JButton();
        btnOpenTable15 = new javax.swing.JButton();
        btnOpenTable16 = new javax.swing.JButton();
        panelAllTables = new javax.swing.JPanel();
        btnTableNo2 = new javax.swing.JButton();
        btnTableNo1 = new javax.swing.JButton();
        btnTableNo3 = new javax.swing.JButton();
        btnTableNo4 = new javax.swing.JButton();
        btnTableNo5 = new javax.swing.JButton();
        btnTableNo6 = new javax.swing.JButton();
        btnTableNo7 = new javax.swing.JButton();
        btnTableNo8 = new javax.swing.JButton();
        btnTableNo9 = new javax.swing.JButton();
        btnTableNo10 = new javax.swing.JButton();
        btnTableNo11 = new javax.swing.JButton();
        btnTableNo12 = new javax.swing.JButton();
        btnTableNo13 = new javax.swing.JButton();
        btnTableNo14 = new javax.swing.JButton();
        btnTableNo15 = new javax.swing.JButton();
        btnTableNo16 = new javax.swing.JButton();
        lblOpenTableName = new javax.swing.JLabel();
        btnPrevious = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        btnPrevious1 = new javax.swing.JButton();
        lblAllTableName = new javax.swing.JLabel();
        btnNext1 = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setExtendedState(MAXIMIZED_BOTH);
        setMinimumSize(new java.awt.Dimension(800, 600));
        setUndecorated(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        lblformName.setBackground(new java.awt.Color(69, 164, 238));
        lblformName.setLayout(new javax.swing.BoxLayout(lblformName, javax.swing.BoxLayout.LINE_AXIS));

        lblProductName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblProductName.setForeground(new java.awt.Color(255, 255, 255));
        lblProductName.setText("SPOS -");
        lblformName.add(lblProductName);

        lblModuleName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblModuleName.setForeground(new java.awt.Color(255, 255, 255));
        lblformName.add(lblModuleName);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText(" - Move Table");
        lblformName.add(jLabel2);
        lblformName.add(filler4);
        lblformName.add(filler5);

        lblPosName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblPosName.setForeground(new java.awt.Color(255, 255, 255));
        lblPosName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPosName.setMaximumSize(new java.awt.Dimension(321, 30));
        lblPosName.setMinimumSize(new java.awt.Dimension(321, 30));
        lblPosName.setPreferredSize(new java.awt.Dimension(321, 30));
        lblformName.add(lblPosName);
        lblformName.add(filler6);

        lblUserCode.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblUserCode.setForeground(new java.awt.Color(255, 255, 255));
        lblUserCode.setMaximumSize(new java.awt.Dimension(90, 30));
        lblUserCode.setMinimumSize(new java.awt.Dimension(90, 30));
        lblUserCode.setPreferredSize(new java.awt.Dimension(90, 30));
        lblformName.add(lblUserCode);

        lblDate.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblDate.setForeground(new java.awt.Color(255, 255, 255));
        lblDate.setMaximumSize(new java.awt.Dimension(192, 30));
        lblDate.setMinimumSize(new java.awt.Dimension(192, 30));
        lblDate.setPreferredSize(new java.awt.Dimension(192, 30));
        lblformName.add(lblDate);

        lblHOSign.setMaximumSize(new java.awt.Dimension(34, 30));
        lblHOSign.setMinimumSize(new java.awt.Dimension(34, 30));
        lblHOSign.setPreferredSize(new java.awt.Dimension(34, 30));
        lblformName.add(lblHOSign);

        getContentPane().add(lblformName, java.awt.BorderLayout.PAGE_START);

        panelMainForm.setLayout(new java.awt.GridBagLayout());

        panelFormBody.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        panelFormBody.setMinimumSize(new java.awt.Dimension(800, 570));
        panelFormBody.setOpaque(false);

        panelOpenTable.setBackground(new java.awt.Color(255, 255, 255));
        panelOpenTable.setEnabled(false);

        btnOpenTable2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnOpenTable2MouseClicked(evt);
            }
        });

        btnOpenTable1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnOpenTable1MouseClicked(evt);
            }
        });

        btnOpenTable3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnOpenTable3MouseClicked(evt);
            }
        });

        btnOpenTable4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnOpenTable4MouseClicked(evt);
            }
        });

        btnOpenTable5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnOpenTable5MouseClicked(evt);
            }
        });

        btnOpenTable6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnOpenTable6MouseClicked(evt);
            }
        });

        btnOpenTable7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnOpenTable7MouseClicked(evt);
            }
        });

        btnOpenTable8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnOpenTable8MouseClicked(evt);
            }
        });

        btnOpenTable9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnOpenTable9MouseClicked(evt);
            }
        });

        btnOpenTable10.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnOpenTable10MouseClicked(evt);
            }
        });

        btnOpenTable11.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnOpenTable11MouseClicked(evt);
            }
        });

        btnOpenTable12.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnOpenTable12MouseClicked(evt);
            }
        });

        btnOpenTable13.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable13.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnOpenTable13MouseClicked(evt);
            }
        });

        btnOpenTable14.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable14.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnOpenTable14MouseClicked(evt);
            }
        });

        btnOpenTable15.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable15.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnOpenTable15MouseClicked(evt);
            }
        });

        btnOpenTable16.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenTable16.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnOpenTable16MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout panelOpenTableLayout = new javax.swing.GroupLayout(panelOpenTable);
        panelOpenTable.setLayout(panelOpenTableLayout);
        panelOpenTableLayout.setHorizontalGroup(
            panelOpenTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelOpenTableLayout.createSequentialGroup()
                .addComponent(btnOpenTable13, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnOpenTable14, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnOpenTable15, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnOpenTable16, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(panelOpenTableLayout.createSequentialGroup()
                .addGroup(panelOpenTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelOpenTableLayout.createSequentialGroup()
                        .addComponent(btnOpenTable1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnOpenTable2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnOpenTable3, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnOpenTable4, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelOpenTableLayout.createSequentialGroup()
                        .addComponent(btnOpenTable5, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnOpenTable6, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnOpenTable7, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnOpenTable8, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelOpenTableLayout.createSequentialGroup()
                        .addComponent(btnOpenTable9, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnOpenTable10, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnOpenTable11, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnOpenTable12, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(2, 2, 2))
        );
        panelOpenTableLayout.setVerticalGroup(
            panelOpenTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelOpenTableLayout.createSequentialGroup()
                .addGap(15, 15, Short.MAX_VALUE)
                .addGroup(panelOpenTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelOpenTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(panelOpenTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnOpenTable3, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnOpenTable4, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(panelOpenTableLayout.createSequentialGroup()
                            .addComponent(btnOpenTable2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(1, 1, 1)))
                    .addComponent(btnOpenTable1, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelOpenTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelOpenTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnOpenTable7, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnOpenTable8, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelOpenTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnOpenTable5, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnOpenTable6, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(panelOpenTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelOpenTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnOpenTable11, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnOpenTable12, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelOpenTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnOpenTable9, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnOpenTable10, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(panelOpenTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelOpenTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnOpenTable15, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnOpenTable16, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelOpenTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnOpenTable13, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnOpenTable14, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        panelAllTables.setBackground(new java.awt.Color(255, 255, 255));
        panelAllTables.setEnabled(false);

        btnTableNo2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTableNo2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnTableNo2MouseClicked(evt);
            }
        });

        btnTableNo1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTableNo1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnTableNo1MouseClicked(evt);
            }
        });

        btnTableNo3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTableNo3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnTableNo3MouseClicked(evt);
            }
        });

        btnTableNo4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTableNo4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnTableNo4MouseClicked(evt);
            }
        });

        btnTableNo5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTableNo5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnTableNo5MouseClicked(evt);
            }
        });

        btnTableNo6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTableNo6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnTableNo6MouseClicked(evt);
            }
        });

        btnTableNo7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTableNo7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnTableNo7MouseClicked(evt);
            }
        });

        btnTableNo8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTableNo8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnTableNo8MouseClicked(evt);
            }
        });

        btnTableNo9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTableNo9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnTableNo9MouseClicked(evt);
            }
        });

        btnTableNo10.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTableNo10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnTableNo10MouseClicked(evt);
            }
        });

        btnTableNo11.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTableNo11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnTableNo11MouseClicked(evt);
            }
        });

        btnTableNo12.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTableNo12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnTableNo12MouseClicked(evt);
            }
        });

        btnTableNo13.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTableNo13.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnTableNo13MouseClicked(evt);
            }
        });

        btnTableNo14.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTableNo14.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnTableNo14MouseClicked(evt);
            }
        });

        btnTableNo15.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTableNo15.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnTableNo15MouseClicked(evt);
            }
        });

        btnTableNo16.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTableNo16.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnTableNo16MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout panelAllTablesLayout = new javax.swing.GroupLayout(panelAllTables);
        panelAllTables.setLayout(panelAllTablesLayout);
        panelAllTablesLayout.setHorizontalGroup(
            panelAllTablesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAllTablesLayout.createSequentialGroup()
                .addComponent(btnTableNo13, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnTableNo14, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnTableNo15, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnTableNo16, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(panelAllTablesLayout.createSequentialGroup()
                .addGroup(panelAllTablesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelAllTablesLayout.createSequentialGroup()
                        .addComponent(btnTableNo1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnTableNo2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnTableNo3, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnTableNo4, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelAllTablesLayout.createSequentialGroup()
                        .addComponent(btnTableNo5, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnTableNo6, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnTableNo7, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnTableNo8, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelAllTablesLayout.createSequentialGroup()
                        .addComponent(btnTableNo9, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnTableNo10, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnTableNo11, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnTableNo12, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(2, 2, 2))
        );
        panelAllTablesLayout.setVerticalGroup(
            panelAllTablesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAllTablesLayout.createSequentialGroup()
                .addGap(15, 15, Short.MAX_VALUE)
                .addGroup(panelAllTablesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelAllTablesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(panelAllTablesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnTableNo3, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnTableNo4, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(panelAllTablesLayout.createSequentialGroup()
                            .addComponent(btnTableNo2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(1, 1, 1)))
                    .addComponent(btnTableNo1, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelAllTablesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelAllTablesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnTableNo7, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnTableNo8, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelAllTablesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnTableNo5, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnTableNo6, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(panelAllTablesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelAllTablesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnTableNo11, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnTableNo12, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelAllTablesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnTableNo9, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnTableNo10, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(panelAllTablesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelAllTablesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnTableNo15, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnTableNo16, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelAllTablesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnTableNo13, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnTableNo14, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        btnPrevious.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnPrevious.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgBackBtn1.png"))); // NOI18N
        btnPrevious.setText("<<<");
        btnPrevious.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPrevious.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgBackBtn2.png"))); // NOI18N
        btnPrevious.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPreviousActionPerformed(evt);
            }
        });

        btnNext.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnNext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgBackBtn1.png"))); // NOI18N
        btnNext.setText(">>>");
        btnNext.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNext.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgBackBtn2.png"))); // NOI18N
        btnNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextActionPerformed(evt);
            }
        });

        btnPrevious1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnPrevious1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgBackBtn1.png"))); // NOI18N
        btnPrevious1.setText("<<<");
        btnPrevious1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPrevious1.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgBackBtn2.png"))); // NOI18N
        btnPrevious1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrevious1ActionPerformed(evt);
            }
        });

        btnNext1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnNext1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgBackBtn1.png"))); // NOI18N
        btnNext1.setText(">>>");
        btnNext1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNext1.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgBackBtn2.png"))); // NOI18N
        btnNext1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNext1ActionPerformed(evt);
            }
        });

        btnSave.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnSave.setForeground(new java.awt.Color(255, 255, 255));
        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnSave.setText("SAVE");
        btnSave.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSave.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        btnClose.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnClose.setForeground(new java.awt.Color(255, 255, 255));
        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnClose.setText("CLOSE");
        btnClose.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnClose.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelFormBodyLayout = new javax.swing.GroupLayout(panelFormBody);
        panelFormBody.setLayout(panelFormBodyLayout);
        panelFormBodyLayout.setHorizontalGroup(
            panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFormBodyLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addComponent(panelOpenTable, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(100, 100, 100)
                        .addComponent(panelAllTables, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(btnPrevious, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40)
                        .addComponent(lblOpenTableName, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40)
                        .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(120, 120, 120)
                        .addComponent(btnPrevious1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(34, 34, 34)
                        .addComponent(lblAllTableName, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36)
                        .addComponent(btnNext1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addGap(480, 480, 480)
                        .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(56, 56, 56)
                        .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        panelFormBodyLayout.setVerticalGroup(
            panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFormBodyLayout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelOpenTable, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panelAllTables, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnPrevious, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblOpenTableName, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPrevious1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblAllTableName, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNext1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelMainForm.add(panelFormBody, new java.awt.GridBagConstraints());

        getContentPane().add(panelMainForm, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnOpenTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable2MouseClicked
        // TODO add your handling code here:
        funSetDefaultColorOpen(btnOpenTable11.getText(),1);
        funSelectOpenTable(btnOpenTable2.getText(),1);
    }//GEN-LAST:event_btnOpenTable2MouseClicked

    private void btnOpenTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable1MouseClicked
        // TODO add your handling code here:
        funSetDefaultColorOpen(btnOpenTable11.getText(),0);
        funSelectOpenTable(btnOpenTable1.getText(),0);
    }//GEN-LAST:event_btnOpenTable1MouseClicked

    private void btnOpenTable3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable3MouseClicked
        // TODO add your handling code here:
        funSetDefaultColorOpen(btnOpenTable11.getText(),2);
        funSelectOpenTable(btnOpenTable3.getText(),2);
    }//GEN-LAST:event_btnOpenTable3MouseClicked

    private void btnOpenTable4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable4MouseClicked
        // TODO add your handling code here:
        funSetDefaultColorOpen(btnOpenTable11.getText(),3);
        funSelectOpenTable(btnOpenTable4.getText(),3);
    }//GEN-LAST:event_btnOpenTable4MouseClicked

    private void btnOpenTable5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable5MouseClicked
        // TODO add your handling code here:
        funSetDefaultColorOpen(btnOpenTable11.getText(),4);
        funSelectOpenTable(btnOpenTable5.getText(),4);
    }//GEN-LAST:event_btnOpenTable5MouseClicked

    private void btnOpenTable6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable6MouseClicked
        // TODO add your handling code here:
        funSetDefaultColorOpen(btnOpenTable11.getText(),5);
        funSelectOpenTable(btnOpenTable6.getText(),5);
    }//GEN-LAST:event_btnOpenTable6MouseClicked

    private void btnOpenTable7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable7MouseClicked
        // TODO add your handling code here:
        funSetDefaultColorOpen(btnOpenTable11.getText(),6);
        funSelectOpenTable(btnOpenTable7.getText(),6);
    }//GEN-LAST:event_btnOpenTable7MouseClicked

    private void btnOpenTable8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable8MouseClicked
        // TODO add your handling code here:
        funSetDefaultColorOpen(btnOpenTable11.getText(),7);
        funSelectOpenTable(btnOpenTable8.getText(),7);
    }//GEN-LAST:event_btnOpenTable8MouseClicked

    private void btnOpenTable9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable9MouseClicked
        // TODO add your handling code here:
        funSetDefaultColorOpen(btnOpenTable11.getText(),8);
        funSelectOpenTable(btnOpenTable9.getText(),8);
    }//GEN-LAST:event_btnOpenTable9MouseClicked

    private void btnOpenTable10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable10MouseClicked
        // TODO add your handling code here:
        funSetDefaultColorOpen(btnOpenTable11.getText(),9);
        funSelectOpenTable(btnOpenTable10.getText(),9);
    }//GEN-LAST:event_btnOpenTable10MouseClicked

    private void btnOpenTable11MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable11MouseClicked
        // TODO add your handling code here:
        funSetDefaultColorOpen(btnOpenTable11.getText(),10);
        funSelectOpenTable(btnOpenTable11.getText(),10);
    }//GEN-LAST:event_btnOpenTable11MouseClicked

    private void btnOpenTable12MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable12MouseClicked
        // TODO add your handling code here:
        funSetDefaultColorOpen(btnOpenTable11.getText(),11);
        funSelectOpenTable(btnOpenTable12.getText(),11);
    }//GEN-LAST:event_btnOpenTable12MouseClicked

    private void btnOpenTable13MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable13MouseClicked
        // TODO add your handling code here:
        funSetDefaultColorOpen(btnOpenTable11.getText(),12);
        funSelectOpenTable(btnOpenTable13.getText(),12);
    }//GEN-LAST:event_btnOpenTable13MouseClicked

    private void btnOpenTable14MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable14MouseClicked
        // TODO add your handling code here:
        funSetDefaultColorOpen(btnOpenTable11.getText(),13);
        funSelectOpenTable(btnOpenTable14.getText(),13);
    }//GEN-LAST:event_btnOpenTable14MouseClicked

    private void btnOpenTable15MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable15MouseClicked
        // TODO add your handling code here:
        funSetDefaultColorOpen(btnOpenTable11.getText(),14);
        funSelectOpenTable(btnOpenTable15.getText(),14);
    }//GEN-LAST:event_btnOpenTable15MouseClicked

    private void btnOpenTable16MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenTable16MouseClicked
        // TODO add your handling code here:
        funSetDefaultColorOpen(btnOpenTable11.getText(),15);
        funSelectOpenTable(btnOpenTable16.getText(),15);
    }//GEN-LAST:event_btnOpenTable16MouseClicked

    private void btnTableNo2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTableNo2MouseClicked
        // TODO add your handling code here:
        funSetDefaultColorAll(1);
        funSelectTable(btnTableNo2.getText(),1);
    }//GEN-LAST:event_btnTableNo2MouseClicked

    private void btnTableNo1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTableNo1MouseClicked
        // TODO add your handling code here:
        System.out.println(btnTableNo1.getBackground());
        funSetDefaultColorAll(0);
        funSelectTable(btnTableNo1.getText(),0);
    }//GEN-LAST:event_btnTableNo1MouseClicked

    private void btnTableNo3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTableNo3MouseClicked
        // TODO add your handling code here:
        funSetDefaultColorAll(2);
        funSelectTable(btnTableNo3.getText(),2);
    }//GEN-LAST:event_btnTableNo3MouseClicked

    private void btnTableNo4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTableNo4MouseClicked
        // TODO add your handling code here:
        funSetDefaultColorAll(3);
        funSelectTable(btnTableNo4.getText(),3);
    }//GEN-LAST:event_btnTableNo4MouseClicked

    private void btnTableNo5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTableNo5MouseClicked
        // TODO add your handling code here:
        funSetDefaultColorAll(4);
        funSelectTable(btnTableNo5.getText(),4);
    }//GEN-LAST:event_btnTableNo5MouseClicked

    private void btnTableNo6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTableNo6MouseClicked
        // TODO add your handling code here:
        funSetDefaultColorAll(5);
        funSelectTable(btnTableNo6.getText(),5);
    }//GEN-LAST:event_btnTableNo6MouseClicked

    private void btnTableNo7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTableNo7MouseClicked
        // TODO add your handling code here:
        funSetDefaultColorAll(6);
        funSelectTable(btnTableNo7.getText(),6);
    }//GEN-LAST:event_btnTableNo7MouseClicked

    private void btnTableNo8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTableNo8MouseClicked
        // TODO add your handling code here:
        funSetDefaultColorAll(7);
        funSelectTable(btnTableNo8.getText(),7);
    }//GEN-LAST:event_btnTableNo8MouseClicked

    private void btnTableNo9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTableNo9MouseClicked
        // TODO add your handling code here:
        funSetDefaultColorAll(8);
        funSelectTable(btnTableNo9.getText(),8);
    }//GEN-LAST:event_btnTableNo9MouseClicked

    private void btnTableNo10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTableNo10MouseClicked
        // TODO add your handling code here:
        funSetDefaultColorAll(9);
        funSelectTable(btnTableNo10.getText(),9);
    }//GEN-LAST:event_btnTableNo10MouseClicked

    private void btnTableNo11MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTableNo11MouseClicked
        // TODO add your handling code here:
        funSetDefaultColorAll(10);
        funSelectTable(btnTableNo11.getText(),10);
    }//GEN-LAST:event_btnTableNo11MouseClicked

    private void btnTableNo12MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTableNo12MouseClicked
        // TODO add your handling code here:
        funSetDefaultColorAll(11);
        funSelectTable(btnTableNo12.getText(),11);
    }//GEN-LAST:event_btnTableNo12MouseClicked

    private void btnTableNo13MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTableNo13MouseClicked
        // TODO add your handling code here:
        funSetDefaultColorAll(12);
        funSelectTable(btnTableNo13.getText(),12);
    }//GEN-LAST:event_btnTableNo13MouseClicked

    private void btnTableNo14MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTableNo14MouseClicked
        // TODO add your handling code here:
        funSetDefaultColorAll(13);
        funSelectTable(btnTableNo14.getText(),13);
    }//GEN-LAST:event_btnTableNo14MouseClicked

    private void btnTableNo15MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTableNo15MouseClicked
        // TODO add your handling code here:
        funSetDefaultColorAll(14);
        funSelectTable(btnTableNo15.getText(),14);
    }//GEN-LAST:event_btnTableNo15MouseClicked

    private void btnTableNo16MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTableNo16MouseClicked
        // TODO add your handling code here:
        funSetDefaultColorAll(15);
        funSelectTable(btnTableNo16.getText(),15);
    }//GEN-LAST:event_btnTableNo16MouseClicked

    private void btnPreviousActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPreviousActionPerformed
        // TODO add your handling code here:
        try
        {
            cntNavigate--;
            if(cntNavigate==0)
            {
                btnPrevious.setEnabled(false);
                btnNext.setEnabled(true);
                funLoadOpenTables(0,vOpenTableNo.size());
            }
            else
            {
                int tableSize=cntNavigate*16;
                int resMod=vOpenTableNo.size()%tableSize;
                int resDiv=vOpenTableNo.size()/tableSize;
                int totalSize=tableSize+16;
                //System.out.println("Size="+vOpenTableNo.size()+"\tMod="+resMod+"\tdiv="+resDiv+"\tsss="+tableSize);
                funLoadOpenTables(tableSize,totalSize);
            }

        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnPreviousActionPerformed

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed
        // TODO add your handling code here:
        try
        {
            cntNavigate++;
            int tableSize=cntNavigate*16;
            int resMod=vOpenTableNo.size()%tableSize;
            int resDiv=vOpenTableNo.size()/tableSize;
            int totalSize=tableSize+16;
            funLoadOpenTables(tableSize,totalSize);
            btnPrevious.setEnabled(true);
            if(resDiv==cntNavigate)
            {
                btnNext.setEnabled(false);
            }

        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnNextActionPerformed

    private void btnPrevious1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrevious1ActionPerformed
        // TODO add your handling code here:
        try
        {
            cntNavigate1--;
            btnNext1.setEnabled(true);
            if(cntNavigate1==0)
            {
                btnPrevious1.setEnabled(false);
                btnNext1.setEnabled(true);
                funLoadTables(0,vTableNo.size());
            }
            else
            {
                int tableSize=cntNavigate1*16;
                int resMod=vTableNo.size()%tableSize;
                int resDiv=vTableNo.size()/tableSize;
                int totalSize=tableSize+16;
                funLoadTables(tableSize,totalSize);
            }

        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnPrevious1ActionPerformed

    private void btnNext1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNext1ActionPerformed
        // TODO add your handling code here:

        try {
            cntNavigate1++;
            int tableSize = cntNavigate1 * 16;
            int resMod = vTableNo.size() % tableSize;
            int resDiv = vTableNo.size() / 16;
            int totalSize = tableSize + 16;
            tblStartIndex = tableSize;
            tblEndIndex = totalSize;
            funLoadTables(tableSize, totalSize);
            btnPrevious1.setEnabled(true);
            if (resDiv == cntNavigate1) {
                btnNext1.setEnabled(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnNext1ActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        if(funValidateTables())
        {
            funMoveTable();
        }
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        dispose();
        clsGlobalVarClass.hmActiveForms.remove("Move Table");
    }//GEN-LAST:event_btnCloseActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosed
    {//GEN-HEADEREND:event_formWindowClosed
        clsGlobalVarClass.hmActiveForms.remove("Move Table");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosing
    {//GEN-HEADEREND:event_formWindowClosing
        clsGlobalVarClass.hmActiveForms.remove("Move Table");
    }//GEN-LAST:event_formWindowClosing

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(frmTableStatusView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmTableStatusView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmTableStatusView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmTableStatusView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmTableStatusView().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnNext1;
    private javax.swing.JButton btnOpenTable1;
    private javax.swing.JButton btnOpenTable10;
    private javax.swing.JButton btnOpenTable11;
    private javax.swing.JButton btnOpenTable12;
    private javax.swing.JButton btnOpenTable13;
    private javax.swing.JButton btnOpenTable14;
    private javax.swing.JButton btnOpenTable15;
    private javax.swing.JButton btnOpenTable16;
    private javax.swing.JButton btnOpenTable2;
    private javax.swing.JButton btnOpenTable3;
    private javax.swing.JButton btnOpenTable4;
    private javax.swing.JButton btnOpenTable5;
    private javax.swing.JButton btnOpenTable6;
    private javax.swing.JButton btnOpenTable7;
    private javax.swing.JButton btnOpenTable8;
    private javax.swing.JButton btnOpenTable9;
    private javax.swing.JButton btnPrevious;
    private javax.swing.JButton btnPrevious1;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnTableNo1;
    private javax.swing.JButton btnTableNo10;
    private javax.swing.JButton btnTableNo11;
    private javax.swing.JButton btnTableNo12;
    private javax.swing.JButton btnTableNo13;
    private javax.swing.JButton btnTableNo14;
    private javax.swing.JButton btnTableNo15;
    private javax.swing.JButton btnTableNo16;
    private javax.swing.JButton btnTableNo2;
    private javax.swing.JButton btnTableNo3;
    private javax.swing.JButton btnTableNo4;
    private javax.swing.JButton btnTableNo5;
    private javax.swing.JButton btnTableNo6;
    private javax.swing.JButton btnTableNo7;
    private javax.swing.JButton btnTableNo8;
    private javax.swing.JButton btnTableNo9;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel lblAllTableName;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblOpenTableName;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JPanel lblformName;
    private javax.swing.JPanel panelAllTables;
    private javax.swing.JPanel panelFormBody;
    private javax.swing.JPanel panelMainForm;
    private javax.swing.JPanel panelOpenTable;
    // End of variables declaration//GEN-END:variables
    private void funSendMessageToCostCenters()
    {
        try
        {
            funCreateTempFolder();
            String filePath = System.getProperty("user.dir");
            String filename = (filePath + "/Temp/MoveTable.txt");
            File file = new File(filename);
                    
            funCreateTestTextFile(file,clsOpenTableName,clsTableName);
            
            String sqlCostCenters = "select b.strCostCenterCode ,c.strCostCenterName,c.strPrinterPort,c.strSecondaryPrinterPort,c.strPrintOnBothPrinters "
                    +"from tblitemrtemp a,tblmenuitempricingdtl b,tblcostcentermaster c "
                    +"where a.strTableNo='"+clsTableNo+"' "
                    +"and a.strItemCode=b.strItemCode "
                    +"and a.strPOSCode=b.strPosCode "
                    +"and b.strCostCenterCode=c.strCostCenterCode "
                    +"group by c.strCostCenterCode; ";
            ResultSet rsCostCenters = clsGlobalVarClass.dbMysql.executeResultSet(sqlCostCenters);
            while (rsCostCenters.next())
            {
                
                String printerName=rsCostCenters.getString(3);
                String secondaryPrinterName=rsCostCenters.getString(4);                               
                try
                {
                       //show text file                                  
//                    clsTextFileGeneratorForPrinting fileGeneratorForPrinting = new clsTextFileGeneratorForPrinting();
//                    fileGeneratorForPrinting.funShowTextFile(file, "", "");

                    int printerIndex = 0;
                    String printerStatus = "Not Found";

                    PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
                    DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
                    printerName = printerName.replaceAll("#", "\\\\");

                    PrintService printService[] = PrintServiceLookup.lookupPrintServices(flavor, pras);
                    for (int i = 0; i < printService.length; i++)
                    {
                        String printerServiceName = printService[i].getName();
                        if (printerName.equalsIgnoreCase(printerServiceName))
                        {
                            System.out.println("Printer=" + printerName);
                            printerIndex = i;
                            printerStatus = "Found";
                            break;
                        }
                    }

                    if (printerStatus.equals("Found"))
                    {
                        DocPrintJob job = printService[printerIndex].createPrintJob();
                        FileInputStream fis = new FileInputStream(filename);
                        DocAttributeSet das = new HashDocAttributeSet();
                        Doc doc = new SimpleDoc(fis, flavor, das);
                        job.print(doc, pras);

                        PrintServiceAttributeSet att = printService[printerIndex].getAttributes();
                        for (Attribute a : att.toArray())
                        {
                            String attributeName;
                            String attributeValue;
                            attributeName = a.getName();
                            attributeValue = att.get(a.getClass()).toString();
                            if (attributeName.trim().equalsIgnoreCase("queued-job-count"))
                            {
                                System.out.println(attributeName + " : " + attributeValue);
                            }
                        }
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(null, printerName + " Printer Not Found");
                    }
                }
                catch (Exception e)
                {

                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, e.getMessage(), "Error Code - TFG 01", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void funCreateTempFolder()
    {
        try
        {
            String filePath = System.getProperty("user.dir");
            File PrintText = new File(filePath + "/Temp");
            if (!PrintText.exists())
            {
                PrintText.mkdirs();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void funCreateTestTextFile(File file,String oldTable,String newTable)
    {
        BufferedWriter fileWriter = null;
        try
        {
            //File file=new File(filename);
            fileWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF8"));

            String fileHeader = "------TABLE SHIFTED MESSAGE------";
            String dottedLine = "----------------------------------";
            String newLine = "\n";
            String blankLine = "                                   ";

            fileWriter.write(fileHeader);
            fileWriter.newLine();
            fileWriter.write(dottedLine);
            fileWriter.newLine();
            fileWriter.write("User Name : " + clsGlobalVarClass.gUserName);
            fileWriter.newLine();
            fileWriter.write("POS Name : " + clsGlobalVarClass.gPOSName);
            fileWriter.newLine();           
            //message
            fileWriter.newLine();     
            fileWriter.write("MESSAGE : ");            
            fileWriter.write(oldTable+" Shifted To "+newTable+".");
            fileWriter.newLine();
                       
            fileWriter.write(dottedLine);

        }
        catch (FileNotFoundException ex)
        {
            ex.printStackTrace();
        }
        catch (UnsupportedEncodingException ex)
        {
            ex.printStackTrace();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            try
            {
                fileWriter.close();
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }

    }
}
