/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.POSMaster.view;
import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsUtility;
import com.POSGlobal.view.frmOkPopUp;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.sql.ResultSet;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

public class frmAssignHomeDelivery extends javax.swing.JFrame {
    
    private ResultSet rs;
    private int nextCnt, limit,count;
    private String[] itemNames;
    private int nextItemClick = 0;
    private double homeDeliveryCharge=0.00;
    java.util.Vector v,billNo;
    private String sql;
    private clsUtility objUtility;
    
    /**
     * This default constructor is used to initialized object
     */     
    public frmAssignHomeDelivery() {
        initComponents();
        
        objUtility=new clsUtility();
        v=new java.util.Vector();
        billNo=new java.util.Vector();
        lblUserCode.setText(clsGlobalVarClass.gUserCode);
        lblPosName.setText(clsGlobalVarClass.gPOSName);
        lblDate.setText(clsGlobalVarClass.gPOSDateToDisplay);
        lblModuleName.setText(clsGlobalVarClass.gSelectedModule);
        funSetShortCutKeys();
        try
        {
            v.add("--select--");
            cmbDelPersonName.addItem("----SELECT----");
            sql="select strDPCode,strDPName from tbldeliverypersonmaster ";
            ResultSet rs1=clsGlobalVarClass.dbMysql.executeResultSet(sql);
            while(rs1.next())
            {
                v.add(rs1.getString(1));
                cmbDelPersonName.addItem(rs1.getString(2));
            }
            rs1.close();
            funGetBillNo();
        }
        catch(Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }
    private void funSetShortCutKeys() {
        btnCancel.setMnemonic('c');
        btnNew.setMnemonic('s');
          

    }
    /**
     * This method is used to get bill no
     * 
     * @return 
     */
    private void funGetBillNo()
    {
        try
        {
            int cou = 0;
            int i=0;
            JToggleButton[] btnPopularArray = {btnIItem1, btnIItem2, btnIItem3, btnIItem4, btnIItem5, btnIItem6, btnIItem7, btnIItem8, btnIItem9, btnIItem10, btnIItem11, btnIItem12, btnIItem13, btnIItem14, btnIItem15, btnIItem16};
            btnNextItem.setEnabled(false);
            btnPrevItem.setEnabled(false);
            nextItemClick = 0;
            rs = clsGlobalVarClass.dbMysql.executeResultSet("select count(strBillNo) from tblhomedelivery ");
            rs.next();
            cou = rs.getInt(1);
            if (cou > 8) 
            {
                btnNextItem.setEnabled(true);
            }            
            itemNames = new String[cou];
            sql="select strBillNo  from tblhomedelivery "
                + "where strDPCode ='null' and dteDate between '"+objUtility.funGetPOSDateForTransaction()+"' and '"+objUtility.funGetPOSDateForTransaction()+"'";
            rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);           
            while (rs.next())
            {
                String s = rs.getString(1);
                if (i < 16) 
                {                                        
                        btnPopularArray[i].setText(s);
                        itemNames[i] = s;                    
                    btnPopularArray[i].setEnabled(true);
                }
                else 
                {                                       
                        itemNames[i] = s;                    
                }
                i++;
            }            
            for (int j = i; j < 16; j++) 
            {                
                btnPopularArray[j].setEnabled(false);
            }                         
        }
        catch(Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }
    
    
    /**
     * This method is used to set bill
     * 
     * @return 
     */
    private void funSettleBill()
    {
        try
        {
            int i=0;
            int index=cmbDelPersonName.getSelectedIndex();  
            if(cmbDelPersonName.getSelectedIndex()==0)
            {
                JOptionPane.showMessageDialog(this,"Select Delivery Boy Name");
            }
            else
            {
                String delPersonCode=v.elementAt(index).toString();           
                for(count=0;count<billNo.size();count++)
                {
                    String billno = billNo.elementAt(count).toString();
                    sql = "Update tblhomedelivery set strDPCode='" + delPersonCode + "', dblHomeDeliCharge='" + homeDeliveryCharge + "' where strBillNo='" + billno + "'";
                    i = clsGlobalVarClass.dbMysql.execute(sql);
                }
                if(i>0)
                {
                    new frmOkPopUp(this,"Updated Successfully", "Successfull",3).setVisible(true);
                    homeDeliveryCharge=0.00;
                    funResetItemNames();
                    funGetBillNo();
                }
            }
        }
        catch(Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }
    /**
     * This method is used to get customer info
     * @param billNo
     * @return 
     */
    private void funGetCustomerInfo(String billNo)
    {
        try
        {
            sql="select strCustomerName,strBuldingCode,strBuldingCode,strBuildingName,strStreetName "
                + " from tblhomedelivery a,tblcustomermaster b "
                + " where a.strCustomerCode=b.strCustomerCode and a.strBillNo='"+billNo+"'";
            ResultSet rs=clsGlobalVarClass.dbMysql.executeResultSet(sql);
            if(rs.next())
            {
                String custname=rs.getString(1);
                sql="select strBuildingName,dblHomeDeliCharge from tblbuildingmaster "
                    + " where strBuildingCode='"+rs.getString(2) +"'";
                ResultSet rsBuildingInfo=clsGlobalVarClass.dbMysql.executeResultSet(sql);
                if(rsBuildingInfo.next())
                {
                    String buldingName=rsBuildingInfo.getString(1);
                    homeDeliveryCharge=rsBuildingInfo.getDouble(2);
                    String address=buldingName+","+rs.getString(3)+","+rs.getString(4)+","+rs.getString(5);
                    lblAddress2.setText(address);
                    lblCustName2.setText(custname);
                    
                    sql="select dblGrandTotal from tblbillhd where strBillNo='"+billNo+"'";
                    ResultSet rs2=clsGlobalVarClass.dbMysql.executeResultSet(sql);
                    if(rs2.next())
                    {
                        String amount=rs2.getString(1);
                        lblAmount2.setText(amount);
                    }
                    rs2.close();
                }
                else
                {
                    String address=rs.getString(3)+","+rs.getString(4)+","+rs.getString(5);
                    lblCustName2.setText(custname);
                    lblAddress2.setText(address);
                }
                rsBuildingInfo.close();
            }
            rs.close();
            
        }
        catch(Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }
    /**
     * This method is used to reset item names
     * 
     * @return 
     */
    private void funResetItemNames() 
    {
        JToggleButton[] btnItemArray = {btnIItem1, btnIItem2, btnIItem3, btnIItem4, btnIItem5, btnIItem6, btnIItem7, btnIItem8, btnIItem9, btnIItem10, btnIItem11, btnIItem12, btnIItem13, btnIItem14, btnIItem15, btnIItem16};//create the JButton array group
        for (int i = 0; i < btnItemArray.length; i++) 
        {
            btnItemArray[i].setText("");//set the item button blank
            btnItemArray[i].setSelected(false);
        }
       funResetFields();
    }
    /**
     * This method is used to reset all fields
     * 
     * @return 
     */
    private void funResetFields()
    {
        lblCustName2.setText("");
        lblAmount2.setText("");
        lblAddress2.setText("");
    }
    /**
     * This method is used to set image
     * 
     * @return imageIcon 
     */
    private ImageIcon funSetImage()
    {
        ImageIcon cup=null;
        try {
            cup = new ImageIcon("D:\\Java pos dss\\JavaPOS\\src\\PosDesign\\enter-btn.png");
        } catch (Exception ex) 
        {
            objUtility.funWriteErrorLog(ex);
            ex.printStackTrace();
        }
        return cup;
    }
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

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
        }; ;
        panelBody = new JPanel() {  
            public void paintComponent(Graphics g) {  
                Image img = Toolkit.getDefaultToolkit().getImage(  
                    getClass().getResource("/com/POSMaster/images/imgBGJPOS.png"));  
                g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);  
            }  
        }; ;
        cmbDelPersonName = new javax.swing.JComboBox();
        panelitem = new javax.swing.JPanel();
        btnIItem1 = new javax.swing.JToggleButton();
        btnIItem2 = new javax.swing.JToggleButton();
        btnIItem3 = new javax.swing.JToggleButton();
        btnIItem4 = new javax.swing.JToggleButton();
        btnIItem5 = new javax.swing.JToggleButton();
        btnIItem6 = new javax.swing.JToggleButton();
        btnIItem7 = new javax.swing.JToggleButton();
        btnIItem8 = new javax.swing.JToggleButton();
        btnIItem10 = new javax.swing.JToggleButton();
        btnIItem9 = new javax.swing.JToggleButton();
        btnIItem11 = new javax.swing.JToggleButton();
        btnIItem12 = new javax.swing.JToggleButton();
        btnIItem13 = new javax.swing.JToggleButton();
        btnIItem14 = new javax.swing.JToggleButton();
        btnIItem15 = new javax.swing.JToggleButton();
        btnIItem16 = new javax.swing.JToggleButton();
        lblFormName = new javax.swing.JLabel();
        btnNew = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        lblDeliveryBoyName = new javax.swing.JLabel();
        panelAddress = new javax.swing.JPanel();
        panelAddData = new javax.swing.JPanel();
        lblCustName1 = new javax.swing.JLabel();
        lblCustName2 = new javax.swing.JLabel();
        lblAmount1 = new javax.swing.JLabel();
        lblAmount2 = new javax.swing.JLabel();
        lblAddress1 = new javax.swing.JLabel();
        lblAddress2 = new javax.swing.JLabel();
        btnPrevItem = new javax.swing.JButton();
        btnNextItem = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setExtendedState(MAXIMIZED_BOTH);
        setMinimumSize(new java.awt.Dimension(800, 600));
        setUndecorated(true);

        panelHeader.setBackground(new java.awt.Color(69, 164, 238));
        panelHeader.setLayout(new javax.swing.BoxLayout(panelHeader, javax.swing.BoxLayout.LINE_AXIS));

        lblProductName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblProductName.setForeground(new java.awt.Color(255, 255, 255));
        lblProductName.setText("SPOS - ");
        panelHeader.add(lblProductName);

        lblModuleName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblModuleName.setForeground(new java.awt.Color(255, 255, 255));
        panelHeader.add(lblModuleName);

        lblformName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblformName.setForeground(new java.awt.Color(255, 255, 255));
        lblformName.setText(" -Assign Home Delivery");
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
        lblUserCode.setPreferredSize(new java.awt.Dimension(71, 30));
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

        cmbDelPersonName.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        cmbDelPersonName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbDelPersonNameActionPerformed(evt);
            }
        });

        panelitem.setBackground(new java.awt.Color(255, 255, 255));
        panelitem.setEnabled(false);
        panelitem.setOpaque(false);

        btnIItem1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnIItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIItem1ActionPerformed(evt);
            }
        });

        btnIItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIItem2ActionPerformed(evt);
            }
        });

        btnIItem3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnIItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIItem3ActionPerformed(evt);
            }
        });

        btnIItem4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnIItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIItem4ActionPerformed(evt);
            }
        });

        btnIItem5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnIItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIItem5ActionPerformed(evt);
            }
        });

        btnIItem6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnIItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIItem6ActionPerformed(evt);
            }
        });

        btnIItem7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnIItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIItem7ActionPerformed(evt);
            }
        });

        btnIItem8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnIItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIItem8ActionPerformed(evt);
            }
        });

        btnIItem10.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnIItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIItem10ActionPerformed(evt);
            }
        });

        btnIItem9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnIItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIItem9ActionPerformed(evt);
            }
        });

        btnIItem11.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnIItem11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIItem11ActionPerformed(evt);
            }
        });

        btnIItem12.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnIItem12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIItem12ActionPerformed(evt);
            }
        });

        btnIItem13.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnIItem13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIItem13ActionPerformed(evt);
            }
        });

        btnIItem14.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnIItem14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIItem14ActionPerformed(evt);
            }
        });

        btnIItem15.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnIItem15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIItem15ActionPerformed(evt);
            }
        });

        btnIItem16.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnIItem16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIItem16ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelitemLayout = new javax.swing.GroupLayout(panelitem);
        panelitem.setLayout(panelitemLayout);
        panelitemLayout.setHorizontalGroup(
            panelitemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelitemLayout.createSequentialGroup()
                .addGroup(panelitemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelitemLayout.createSequentialGroup()
                        .addComponent(btnIItem1, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnIItem2, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnIItem3, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnIItem4, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnIItem5, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnIItem6, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnIItem7, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnIItem8, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelitemLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(btnIItem9, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnIItem10, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnIItem11, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnIItem12, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnIItem13, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnIItem14, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnIItem15, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnIItem16, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelitemLayout.setVerticalGroup(
            panelitemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelitemLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelitemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnIItem7, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnIItem6, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnIItem5, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnIItem4, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnIItem3, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnIItem2, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnIItem1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnIItem8, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelitemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnIItem10, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnIItem9, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnIItem11, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnIItem12, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnIItem13, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnIItem14, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnIItem15, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnIItem16, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(38, Short.MAX_VALUE))
        );

        lblFormName.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        lblFormName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblFormName.setText("Assign Home Delivery");

        btnNew.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnNew.setForeground(new java.awt.Color(255, 255, 255));
        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnNew.setText("SAVE");
        btnNew.setToolTipText("Save Assign Home Delivery");
        btnNew.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNew.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnNew.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnNewMouseClicked(evt);
            }
        });
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });

        btnCancel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnCancel.setForeground(new java.awt.Color(255, 255, 255));
        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnCancel.setText("CLOSE");
        btnCancel.setToolTipText("Close Assign Home Delivery");
        btnCancel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCancel.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnCancel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCancelMouseClicked(evt);
            }
        });
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        lblDeliveryBoyName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblDeliveryBoyName.setText("Delivery Boy Name :");

        panelAddress.setBackground(new java.awt.Color(255, 255, 255));

        panelAddData.setBackground(new java.awt.Color(0, 153, 255));
        panelAddData.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        lblCustName1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblCustName1.setText("Customer Name");

        lblCustName2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblCustName2.setForeground(new java.awt.Color(255, 255, 255));

        lblAmount1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblAmount1.setText("Amount");

        lblAmount2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblAmount2.setForeground(new java.awt.Color(255, 255, 255));
        lblAmount2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        lblAddress1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblAddress1.setText("Address");

        lblAddress2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblAddress2.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout panelAddDataLayout = new javax.swing.GroupLayout(panelAddData);
        panelAddData.setLayout(panelAddDataLayout);
        panelAddDataLayout.setHorizontalGroup(
            panelAddDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAddDataLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelAddDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(lblAmount1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblCustName1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelAddDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelAddDataLayout.createSequentialGroup()
                        .addComponent(lblCustName2, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblAddress1, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblAddress2, javax.swing.GroupLayout.DEFAULT_SIZE, 311, Short.MAX_VALUE))
                    .addGroup(panelAddDataLayout.createSequentialGroup()
                        .addComponent(lblAmount2, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelAddDataLayout.setVerticalGroup(
            panelAddDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAddDataLayout.createSequentialGroup()
                .addGroup(panelAddDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblAddress1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelAddDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(lblCustName1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
                        .addComponent(lblCustName2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(lblAddress2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(panelAddDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblAmount1, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(lblAmount2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        btnPrevItem.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnPrevItem.setForeground(new java.awt.Color(255, 255, 255));
        btnPrevItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgPOSSelection1.png"))); // NOI18N
        btnPrevItem.setText("<<<");
        btnPrevItem.setToolTipText("Preveous");
        btnPrevItem.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPrevItem.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgPOSSelection2.png"))); // NOI18N
        btnPrevItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrevItemActionPerformed(evt);
            }
        });

        btnNextItem.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnNextItem.setForeground(new java.awt.Color(255, 255, 255));
        btnNextItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgPOSSelection1.png"))); // NOI18N
        btnNextItem.setText(">>>");
        btnNextItem.setToolTipText("Next");
        btnNextItem.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNextItem.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgPOSSelection2.png"))); // NOI18N
        btnNextItem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnNextItemMouseClicked(evt);
            }
        });
        btnNextItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextItemActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelAddressLayout = new javax.swing.GroupLayout(panelAddress);
        panelAddress.setLayout(panelAddressLayout);
        panelAddressLayout.setHorizontalGroup(
            panelAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAddressLayout.createSequentialGroup()
                .addComponent(btnPrevItem, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelAddData, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnNextItem, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(12, Short.MAX_VALUE))
        );
        panelAddressLayout.setVerticalGroup(
            panelAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAddressLayout.createSequentialGroup()
                .addGroup(panelAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelAddressLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnNextItem, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelAddressLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnPrevItem, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelAddressLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(panelAddData, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout panelBodyLayout = new javax.swing.GroupLayout(panelBody);
        panelBody.setLayout(panelBodyLayout);
        panelBodyLayout.setHorizontalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBodyLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addGap(280, 280, 280)
                        .addComponent(lblFormName, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addGap(190, 190, 190)
                        .addComponent(lblDeliveryBoyName, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(cmbDelPersonName, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(panelAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panelitem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBodyLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panelBodyLayout.setVerticalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(lblFormName, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(lblDeliveryBoyName, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(cmbDelPersonName, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4)
                .addComponent(panelAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(panelitem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 53, Short.MAX_VALUE)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(36, 36, 36))
        );

        panelLayout.add(panelBody, new java.awt.GridBagConstraints());

        getContentPane().add(panelLayout, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmbDelPersonNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbDelPersonNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbDelPersonNameActionPerformed

    private void btnIItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIItem1ActionPerformed
        // TODO add your handling code here:
        JToggleButton btn = (JToggleButton) evt.getSource();
        if (btn.isSelected())
        {
            billNo.add(btnIItem1.getText());
            funGetCustomerInfo(btnIItem1.getText());
        }
        else
        {
            billNo.remove(btnIItem1.getText());
            funResetFields();
        }
    }//GEN-LAST:event_btnIItem1ActionPerformed

    private void btnIItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIItem2ActionPerformed
        // TODO add your handling code here:
        JToggleButton btn = (JToggleButton) evt.getSource();
        if (btn.isSelected())
        {
            billNo.add(btnIItem2.getText());
            funGetCustomerInfo(btnIItem2.getText());
        }
        else
        {
            billNo.remove(btnIItem2.getText());
            funResetFields();
        }
    }//GEN-LAST:event_btnIItem2ActionPerformed

    private void btnIItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIItem3ActionPerformed
        // TODO add your handling code here:
        JToggleButton btn = (JToggleButton) evt.getSource();
        if (btn.isSelected())
        {
            billNo.add(btnIItem3.getText());
            funGetCustomerInfo(btnIItem3.getText());
        }
        else
        {
            billNo.remove(btnIItem3.getText());
            funResetFields();
        }
    }//GEN-LAST:event_btnIItem3ActionPerformed

    private void btnIItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIItem4ActionPerformed
        // TODO add your handling code here:
        JToggleButton btn = (JToggleButton) evt.getSource();
        if (btn.isSelected())
        {
            billNo.add(btnIItem4.getText());
            funGetCustomerInfo(btnIItem4.getText());
        }
        else
        {
            billNo.remove(btnIItem4.getText());
            funResetFields();
        }
    }//GEN-LAST:event_btnIItem4ActionPerformed

    private void btnIItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIItem5ActionPerformed
        // TODO add your handling code here:
        JToggleButton btn = (JToggleButton) evt.getSource();
        if (btn.isSelected())
        {
            billNo.add(btnIItem5.getText());
            funGetCustomerInfo(btnIItem5.getText());
        }
        else
        {
            billNo.remove(btnIItem5.getText());
            funResetFields();
        }
    }//GEN-LAST:event_btnIItem5ActionPerformed

    private void btnIItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIItem6ActionPerformed
        // TODO add your handling code here:
        JToggleButton btn = (JToggleButton) evt.getSource();
        if (btn.isSelected())
        {
            billNo.add(btnIItem6.getText());
            funGetCustomerInfo(btnIItem6.getText());
        }
        else
        {
            billNo.remove(btnIItem6.getText());
            funResetFields();
        }
    }//GEN-LAST:event_btnIItem6ActionPerformed

    private void btnIItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIItem7ActionPerformed
        // TODO add your handling code here:
        JToggleButton btn = (JToggleButton) evt.getSource();
        if (btn.isSelected())
        {
            billNo.add(btnIItem7.getText());
            funGetCustomerInfo(btnIItem7.getText());

        }
        else
        {
            billNo.remove(btnIItem7.getText());
            funResetFields();
        }
    }//GEN-LAST:event_btnIItem7ActionPerformed

    private void btnIItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIItem8ActionPerformed
        // TODO add your handling code here:
        JToggleButton btn = (JToggleButton) evt.getSource();
        if (btn.isSelected())
        {
            billNo.add(btnIItem8.getText());
            funGetCustomerInfo(btnIItem8.getText());

        }
        else
        {
            billNo.remove(btnIItem8.getText());
            funResetFields();
        }
    }//GEN-LAST:event_btnIItem8ActionPerformed

    private void btnIItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIItem10ActionPerformed
        // TODO add your handling code here:
        JToggleButton btn = (JToggleButton) evt.getSource();
        if (btn.isSelected())
        {
            billNo.add(btnIItem10.getText());
            funGetCustomerInfo(btnIItem10.getText());

        }
        else
        {
            billNo.remove(btnIItem10.getText());
            funResetFields();
        }
    }//GEN-LAST:event_btnIItem10ActionPerformed

    private void btnIItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIItem9ActionPerformed
        // TODO add your handling code here:
        JToggleButton btn = (JToggleButton) evt.getSource();
        if (btn.isSelected())
        {
            billNo.add(btnIItem9.getText());
            funGetCustomerInfo(btnIItem9.getText());

        }
        else
        {
            billNo.remove(btnIItem9.getText());
            funResetFields();
        }
    }//GEN-LAST:event_btnIItem9ActionPerformed

    private void btnIItem11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIItem11ActionPerformed
        // TODO add your handling code here:
        JToggleButton btn = (JToggleButton) evt.getSource();
        if (btn.isSelected())
        {
            billNo.add(btnIItem11.getText());
            funGetCustomerInfo(btnIItem11.getText());

        }
        else
        {
            billNo.remove(btnIItem11.getText());
            funResetFields();
        }
    }//GEN-LAST:event_btnIItem11ActionPerformed

    private void btnIItem12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIItem12ActionPerformed
        // TODO add your handling code here:
        JToggleButton btn = (JToggleButton) evt.getSource();
        if (btn.isSelected())
        {
            billNo.add(btnIItem12.getText());
            funGetCustomerInfo(btnIItem12.getText());

        }
        else
        {
            billNo.remove(btnIItem12.getText());
            funResetFields();
        }
    }//GEN-LAST:event_btnIItem12ActionPerformed

    private void btnIItem13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIItem13ActionPerformed
        // TODO add your handling code here:
        JToggleButton btn = (JToggleButton) evt.getSource();
        if (btn.isSelected())
        {
            billNo.add(btnIItem13.getText());
            funGetCustomerInfo(btnIItem13.getText());

        }
        else
        {
            billNo.remove(btnIItem13.getText());
            funResetFields();
        }
    }//GEN-LAST:event_btnIItem13ActionPerformed

    private void btnIItem14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIItem14ActionPerformed
        // TODO add your handling code here:
        JToggleButton btn = (JToggleButton) evt.getSource();
        if (btn.isSelected())
        {
            billNo.add(btnIItem14.getText());
            funGetCustomerInfo(btnIItem14.getText());

        }
        else
        {
            billNo.remove(btnIItem14.getText());
            funResetFields();
        }
    }//GEN-LAST:event_btnIItem14ActionPerformed

    private void btnIItem15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIItem15ActionPerformed
        // TODO add your handling code here:
        JToggleButton btn = (JToggleButton) evt.getSource();
        if (btn.isSelected())
        {
            billNo.add(btnIItem15.getText());
            funGetCustomerInfo(btnIItem15.getText());

        }
        else
        {
            billNo.remove(btnIItem15.getText());
            funResetFields();
        }
    }//GEN-LAST:event_btnIItem15ActionPerformed

    private void btnIItem16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIItem16ActionPerformed
        // TODO add your handling code here:
        JToggleButton btn = (JToggleButton) evt.getSource();
        if (btn.isSelected())
        {
            billNo.add(btnIItem16.getText());
            funGetCustomerInfo(btnIItem16.getText());

        }
        else
        {
            billNo.remove(btnIItem16.getText());
            funResetFields();
        }
    }//GEN-LAST:event_btnIItem16ActionPerformed

    private void btnNewMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNewMouseClicked
        // TODO add your handling code here:
        funSettleBill();
    }//GEN-LAST:event_btnNewMouseClicked

    private void btnCancelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseClicked
        
        dispose();
    }//GEN-LAST:event_btnCancelMouseClicked

    private void btnPrevItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrevItemActionPerformed
        // TODO add your handling code here:
        try
        {
            btnNextItem.setEnabled(true);
            JToggleButton[] btnItemArray = {btnIItem1, btnIItem2, btnIItem3, btnIItem4, btnIItem5, btnIItem6, btnIItem7, btnIItem8, btnIItem9, btnIItem10, btnIItem11, btnIItem12, btnIItem13, btnIItem14, btnIItem15, btnIItem16};
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
        } catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnPrevItemActionPerformed

    private void btnNextItemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNextItemMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnNextItemMouseClicked

    private void btnNextItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextItemActionPerformed
        // TODO add your handling code here:
        try
        {
            btnPrevItem.setEnabled(true);
            JToggleButton[] btnItemArray = {btnIItem1, btnIItem2, btnIItem3, btnIItem4, btnIItem5, btnIItem6, btnIItem7, btnIItem8, btnIItem9, btnIItem10, btnIItem11, btnIItem12, btnIItem13, btnIItem14, btnIItem15, btnIItem16};
            nextItemClick++;
            int itemDiv = itemNames.length / 17;
            if (itemDiv == nextItemClick)
            {
                btnNextItem.setEnabled(false);
            }
            int k = 0;
            nextCnt = nextItemClick * 16;
            limit = nextCnt + 16;
            for (int m = 0; m < 16; m++)
            {
                btnItemArray[m].setText("");
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
        } catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnNextItemActionPerformed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
         funSettleBill();
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
            dispose();
    }//GEN-LAST:event_btnCancelActionPerformed
    
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JToggleButton btnIItem1;
    private javax.swing.JToggleButton btnIItem10;
    private javax.swing.JToggleButton btnIItem11;
    private javax.swing.JToggleButton btnIItem12;
    private javax.swing.JToggleButton btnIItem13;
    private javax.swing.JToggleButton btnIItem14;
    private javax.swing.JToggleButton btnIItem15;
    private javax.swing.JToggleButton btnIItem16;
    private javax.swing.JToggleButton btnIItem2;
    private javax.swing.JToggleButton btnIItem3;
    private javax.swing.JToggleButton btnIItem4;
    private javax.swing.JToggleButton btnIItem5;
    private javax.swing.JToggleButton btnIItem6;
    private javax.swing.JToggleButton btnIItem7;
    private javax.swing.JToggleButton btnIItem8;
    private javax.swing.JToggleButton btnIItem9;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnNextItem;
    private javax.swing.JButton btnPrevItem;
    private javax.swing.JComboBox cmbDelPersonName;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JLabel lblAddress1;
    private javax.swing.JLabel lblAddress2;
    private javax.swing.JLabel lblAmount1;
    private javax.swing.JLabel lblAmount2;
    private javax.swing.JLabel lblCustName1;
    private javax.swing.JLabel lblCustName2;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblDeliveryBoyName;
    private javax.swing.JLabel lblFormName;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblformName;
    private javax.swing.JPanel panelAddData;
    private javax.swing.JPanel panelAddress;
    private javax.swing.JPanel panelBody;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelLayout;
    private javax.swing.JPanel panelitem;
    // End of variables declaration//GEN-END:variables
}
