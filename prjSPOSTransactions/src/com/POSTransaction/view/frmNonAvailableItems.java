/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSTransaction.view;

import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsUtility;
import com.POSGlobal.view.frmAlfaNumericKeyBoard;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class frmNonAvailableItems extends javax.swing.JFrame
{

  
    public String sql;
    public int cntNavigate, tblStartIndex, tblEndIndex;
    private JButton[] btnNonAvblItemArray ;
    private clsUtility objUtility;
    private DefaultTableModel dm;
    private HashMap<String,String> mapItemNameCode;
    private HashMap<String,String> mapNonAvailableItem;
    private HashSet setNonAvailableItem;
    ArrayList arrItemNames=new ArrayList();
    public frmNonAvailableItems()
    {
        initComponents();
        try
        {
            btnSave.setVisible(false);
            objUtility = new clsUtility();
           
            btnPrevious.setEnabled(false);
            txtItemSearch.requestFocus();
            cntNavigate = 0;
           // cntNavigate1 = 0;
            lblPosName.setText(clsGlobalVarClass.gPOSName);
            Date date1 = new Date();
            String new_str = String.format("%tr", date1);
            String dateAndTime = clsGlobalVarClass.gPOSDateToDisplay + " " + new_str;
            lblDate.setText(dateAndTime);
            lblUserCode.setText(clsGlobalVarClass.gUserCode);
            lblModuleName.setText(clsGlobalVarClass.gSelectedModule);
            dm =(DefaultTableModel) tblItems.getModel();
            mapNonAvailableItem=new HashMap();
            setNonAvailableItem=new HashSet<String>();
            
            btnNonAvblItemArray =new JButton[]{
                btnNonAvailableItem1,btnNonAvailableItem2,btnNonAvailableItem3,btnNonAvailableItem4,btnNonAvailableItem5,
                btnNonAvailableItem6,btnNonAvailableItem7,btnNonAvailableItem8,btnNonAvailableItem9,
                btnNonAvailableItem10,btnNonAvailableItem11,btnNonAvailableItem12,btnNonAvailableItem13,
                btnNonAvailableItem14,btnNonAvailableItem15,btnNonAvailableItem16
                };
            for (int cntTable = 0; cntTable < btnNonAvblItemArray.length; cntTable++)
            {
                btnNonAvblItemArray[cntTable].setVisible(false);
                btnNonAvblItemArray[cntTable].setText("");
                //btnNonAvblItemArray[cntTable].setIcon(null);
            }
            btnNext.setEnabled(false);
            btnPrevious.setEnabled(false);
            tblItems.setRowHeight(36);
            funFillItemTable();
            funGetNonAvailableItems();

           
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

   private void funFillItemTable()
   {
       try{
            Object[] rows = new Object[1];
            mapItemNameCode=new HashMap<String,String>();
            sql="select strItemCode ,strItemName from tblitemmaster"
                    + " where strClientCode ='"+clsGlobalVarClass.gClientCode+"' ORDER BY strItemName ASC";
            ResultSet rsItems = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                while (rsItems.next())
                {
                    mapItemNameCode.put( rsItems.getString(2),rsItems.getString(1));
                   
                    rows[0]=rsItems.getString(2);
                    dm.addRow(rows);
                }
                rsItems.close();
       }
       catch(Exception e)
       {
           e.printStackTrace();
       }
   }


  private void funSearchItem()
    {
        try
        {
            String text = txtItemSearch.getText().trim();
            DefaultTableModel dmPLUItemTable = (DefaultTableModel) tblItems.getModel();
            final TableRowSorter<TableModel> sorter;
            sorter = new TableRowSorter<TableModel>(dmPLUItemTable);
            tblItems.setRowSorter(sorter);
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
            sorter.setSortKeys(null);
            if (tblItems.getModel().getRowCount() > 0)
            {
                int selectedRow = tblItems.getSelectedRow();
                int rowcount = tblItems.getRowCount();
                if (selectedRow == -1)
                {
                    selectedRow = 0;
                    tblItems.changeSelection(selectedRow, 0, false, false);
                }
                else if (selectedRow == rowcount)
                {
                    selectedRow = 0;
                    tblItems.changeSelection(selectedRow, 0, false, false);
                }
                else if (selectedRow < rowcount)
                {
                    tblItems.changeSelection(selectedRow - 1, 0, false, false);
                }
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            txtItemSearch.setText("");
        }
    }
   
  private void funGetNonAvailableItems()
  {
      arrItemNames.clear();
      setNonAvailableItem.clear();
      mapNonAvailableItem.clear();
       cntNavigate=0;
       btnPrevious.setEnabled(false);
       btnNext.setEnabled(true);
      
       try{
         
          sql="select strItemCode, strItemName from tblnonavailableitems where strClientCode='"+clsGlobalVarClass.gClientCode+"'";
           ResultSet rsNoAvlItems = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                while (rsNoAvlItems.next())
                {
                    arrItemNames.add(rsNoAvlItems.getString(2));
                    setNonAvailableItem.add(rsNoAvlItems.getString(2));
                    mapNonAvailableItem.put(rsNoAvlItems.getString(2),rsNoAvlItems.getString(1));//item name .. code
                    
                }
                 funLoadNonAvailableItems(0, setNonAvailableItem.size());
                  if (setNonAvailableItem.size() < 16)
                    {
                        btnNext.setEnabled(false);
                    }
                     
      }
      catch(Exception e)
      {
          e.printStackTrace();
      }
  }
  
  private void funItemsMouseClicked()
  {
      try{
            String strItemCode="",strItemName="";
            int ch = JOptionPane.showConfirmDialog(this, tblItems.getValueAt(tblItems.getSelectedRow(),0) +"\n Is Not Availble" , "Item Move", JOptionPane.YES_NO_OPTION);
            if (ch == JOptionPane.YES_OPTION)
            {
                if(setNonAvailableItem.contains(tblItems.getValueAt(tblItems.getSelectedRow(),0).toString()))
                {
                     JOptionPane.showMessageDialog(this," Item Already Not Available");
                }
                else{
                    for (int cntTable = 0; cntTable < btnNonAvblItemArray.length; cntTable++)
                     {
                       if(btnNonAvblItemArray[cntTable].getText().equals(""))
                        {
                            strItemName=tblItems.getValueAt(tblItems.getSelectedRow(),0).toString();
                            setNonAvailableItem.add(strItemName);
                            arrItemNames.add(strItemName);
//                            if(setNonAvailableItem.size()>16)
//                            {
//                                cntNavigate++;
//                            }
                            
                            btnNonAvblItemArray[cntTable].setVisible(true);
                            btnNonAvblItemArray[cntTable].setText("<html><h5>" +strItemName+"</h5></html>");
                           
                            strItemCode=mapItemNameCode.get(strItemName);

                            sql="insert into tblnonavailableitems (strItemCode,strItemName,strClientCode,dteDate,strPOSCode) "
                                    + "values ('"+strItemCode+"','"+strItemName+"','"+clsGlobalVarClass.gClientCode+"','"+objUtility.funGetPOSDateForTransaction()+"','"+clsGlobalVarClass.gPOSCode+"')";
                            clsGlobalVarClass.dbMysql.execute(sql);
                           
//                            if(setNonAvailableItem.size()%16==0){
//                                  btnNext.setVisible(false);
//                            }
                             
                            
                            break;
                        }
                        if(cntTable==15)
                        {
                           // btnNext.setVisible(true);
                            
                            cntNavigate++;
                            int tableSize = cntNavigate * 16;
                            int resMod = setNonAvailableItem.size() % tableSize;
                            int resDiv = setNonAvailableItem.size() / tableSize;
                            int totalSize = tableSize + 16;
                            funLoadNonAvailableItems(tableSize, totalSize);
                            btnPrevious.setEnabled(true);
                             if (resDiv == cntNavigate)
                            {
                                btnNext.setEnabled(false);
                            }
                             funItemsMouseClicked();
                        }
                
                    }
                }
            
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
  }
   private void funLoadNonAvailableItems(int startIndex, int totalSize)
    {
        String itemName ="";
        try{
            
            int cntIndex = 0;
              for (int k = 0; k < btnNonAvblItemArray.length; k++)
                {
                     btnNonAvblItemArray[k].setVisible(false);
                     btnNonAvblItemArray[k].setText("");
                }
                 for (int i = startIndex; i < totalSize; i++)
                {
                    if (i == setNonAvailableItem.size())
                    {
                        btnNext.setVisible(false);
                        break;
                    }
                    
                        if(btnNonAvblItemArray[cntIndex].getText().equals(""))
                        {
                            itemName = arrItemNames.get(i).toString();
                            btnNonAvblItemArray[cntIndex].setVisible(true);
                            btnNonAvblItemArray[cntIndex].setText("<html><h5>" +itemName+"</h5></html>");
                        }
                    
                    btnNonAvblItemArray[cntIndex].setEnabled(true);
                    cntIndex++;
                    if(cntIndex>=16)
                    {
                        break;
                    }
                }
//                 if(totalSize>16)
//                    {
//                        cntNavigate++;
//                        cntIndex=cntIndex%16;
//                        for (int k = 0; k < btnNonAvblItemArray.length; k++)
//                        {
//                            btnNonAvblItemArray[k].setVisible(false);
//                            btnNonAvblItemArray[k].setText("");
//                        }
//                         for (int i = startIndex; i < totalSize; i++)
//                           {
//                            if(btnNonAvblItemArray[i].getText().equals(""))
//                            {
//                                itemName = arrItemNames.get(i).toString();
//                                btnNonAvblItemArray[i].setVisible(true);
//                                btnNonAvblItemArray[i].setText("<html><h5>" +itemName+"</h5></html>");
//                            } 
//                           }
//                    }
                   
//             for (int j = cntIndex; j < 16; j++)
//            {
//                btnNonAvblItemArray[j].setEnabled(false);
//            }
              
          btnNext.setVisible(true);     
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
  
   

    private void funRemoveNonAvailableItem(String strItemName,int index)
    {
      strItemName=strItemName.substring(10, strItemName.length()-12);
      int ch = JOptionPane.showConfirmDialog(this, strItemName +"\n Is Now Availble" , "Item Move", JOptionPane.YES_NO_OPTION);
      if (ch == JOptionPane.YES_OPTION)
       {
            try{
                
                if(strItemName.trim().length()>0)
                {
                    String strItemCode=mapItemNameCode.get(strItemName);
                    sql="delete from tblnonavailableitems where"
                            + " strItemCode='"+strItemCode+"' and strClientCode='"+clsGlobalVarClass.gClientCode+"' and strPOSCode='"+clsGlobalVarClass.gPOSCode+"' ";
                    clsGlobalVarClass.dbMysql.execute(sql);
                    funGetNonAvailableItems();
                    //btnNonAvblItemArray[index].setVisible(false);
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            
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
        panelMainForm = 
        new JPanel() {  
            public void paintComponent(Graphics g) {  
                Image img = Toolkit.getDefaultToolkit().getImage(  
                    getClass().getResource("/com/POSTransaction/images/imgBackgroundImage.png"));  
                g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);  
            }  
        };  ;
        panelFormBody = new javax.swing.JPanel();
        panelSelectItem = new javax.swing.JPanel();
        txtItemSearch = new javax.swing.JTextField();
        lblSearch = new javax.swing.JLabel();
        scrPLU = new javax.swing.JScrollPane();
        tblItems = new javax.swing.JTable();
        panelOpenTable = new javax.swing.JPanel();
        btnNonAvailableItem1 = new javax.swing.JButton();
        btnNonAvailableItem2 = new javax.swing.JButton();
        btnNonAvailableItem3 = new javax.swing.JButton();
        btnNonAvailableItem4 = new javax.swing.JButton();
        btnNonAvailableItem5 = new javax.swing.JButton();
        btnNonAvailableItem6 = new javax.swing.JButton();
        btnNonAvailableItem7 = new javax.swing.JButton();
        btnNonAvailableItem8 = new javax.swing.JButton();
        btnNonAvailableItem9 = new javax.swing.JButton();
        btnNonAvailableItem10 = new javax.swing.JButton();
        btnNonAvailableItem11 = new javax.swing.JButton();
        btnNonAvailableItem12 = new javax.swing.JButton();
        btnNonAvailableItem13 = new javax.swing.JButton();
        btnNonAvailableItem14 = new javax.swing.JButton();
        btnNonAvailableItem15 = new javax.swing.JButton();
        btnNonAvailableItem16 = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        btnPrevious = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        lblOpenTableName = new javax.swing.JLabel();

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
        lblProductName.setText("SPOS ");
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
        lblformName.setText("-Non Available Items");
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

        panelMainForm.setOpaque(false);
        panelMainForm.setLayout(new java.awt.GridBagLayout());

        panelFormBody.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        panelFormBody.setMinimumSize(new java.awt.Dimension(800, 570));
        panelFormBody.setOpaque(false);

        panelSelectItem.setBackground(new java.awt.Color(255, 255, 255));
        panelSelectItem.setEnabled(false);
        panelSelectItem.setOpaque(false);

        txtItemSearch.addFocusListener(new java.awt.event.FocusAdapter()
        {
            public void focusGained(java.awt.event.FocusEvent evt)
            {
                txtItemSearchFocusGained(evt);
            }
        });
        txtItemSearch.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtItemSearchMouseClicked(evt);
            }
        });
        txtItemSearch.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtItemSearchKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt)
            {
                txtItemSearchKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt)
            {
                txtItemSearchKeyTyped(evt);
            }
        });

        lblSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgSearch.png"))); // NOI18N
        lblSearch.setToolTipText("Search Menu");
        lblSearch.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                lblSearchMouseClicked(evt);
            }
        });

        tblItems.setModel(new javax.swing.table.DefaultTableModel(
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
        tblItems.setRowHeight(25);
        tblItems.getTableHeader().setReorderingAllowed(false);
        tblItems.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tblItemsMouseClicked(evt);
            }
        });
        tblItems.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                tblItemsKeyPressed(evt);
            }
        });
        scrPLU.setViewportView(tblItems);

        javax.swing.GroupLayout panelSelectItemLayout = new javax.swing.GroupLayout(panelSelectItem);
        panelSelectItem.setLayout(panelSelectItemLayout);
        panelSelectItemLayout.setHorizontalGroup(
            panelSelectItemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSelectItemLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(panelSelectItemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(scrPLU, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(panelSelectItemLayout.createSequentialGroup()
                        .addComponent(txtItemSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(23, 23, 23)
                        .addComponent(lblSearch)))
                .addContainerGap(43, Short.MAX_VALUE))
        );
        panelSelectItemLayout.setVerticalGroup(
            panelSelectItemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSelectItemLayout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addGroup(panelSelectItemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtItemSearch)
                    .addComponent(lblSearch))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrPLU, javax.swing.GroupLayout.DEFAULT_SIZE, 485, Short.MAX_VALUE))
        );

        panelOpenTable.setBackground(new java.awt.Color(255, 255, 255));
        panelOpenTable.setEnabled(false);
        panelOpenTable.setOpaque(false);
        panelOpenTable.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnNonAvailableItem1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnNonAvailableItem1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNonAvailableItem1.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnNonAvailableItem1MouseClicked(evt);
            }
        });
        btnNonAvailableItem1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnNonAvailableItem1ActionPerformed(evt);
            }
        });
        panelOpenTable.add(btnNonAvailableItem1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 80, 81));

        btnNonAvailableItem2.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnNonAvailableItem2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNonAvailableItem2.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnNonAvailableItem2MouseClicked(evt);
            }
        });
        panelOpenTable.add(btnNonAvailableItem2, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 0, 80, 80));

        btnNonAvailableItem3.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnNonAvailableItem3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNonAvailableItem3.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnNonAvailableItem3MouseClicked(evt);
            }
        });
        panelOpenTable.add(btnNonAvailableItem3, new org.netbeans.lib.awtextra.AbsoluteConstraints(198, 0, 80, 80));

        btnNonAvailableItem4.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnNonAvailableItem4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNonAvailableItem4.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnNonAvailableItem4MouseClicked(evt);
            }
        });
        panelOpenTable.add(btnNonAvailableItem4, new org.netbeans.lib.awtextra.AbsoluteConstraints(288, 0, 80, 80));

        btnNonAvailableItem5.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnNonAvailableItem5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNonAvailableItem5.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnNonAvailableItem5MouseClicked(evt);
            }
        });
        panelOpenTable.add(btnNonAvailableItem5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 80, 80));

        btnNonAvailableItem6.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnNonAvailableItem6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNonAvailableItem6.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnNonAvailableItem6MouseClicked(evt);
            }
        });
        panelOpenTable.add(btnNonAvailableItem6, new org.netbeans.lib.awtextra.AbsoluteConstraints(102, 110, 80, 80));

        btnNonAvailableItem7.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnNonAvailableItem7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNonAvailableItem7.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnNonAvailableItem7MouseClicked(evt);
            }
        });
        panelOpenTable.add(btnNonAvailableItem7, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 110, 80, 80));

        btnNonAvailableItem8.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnNonAvailableItem8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNonAvailableItem8.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnNonAvailableItem8MouseClicked(evt);
            }
        });
        panelOpenTable.add(btnNonAvailableItem8, new org.netbeans.lib.awtextra.AbsoluteConstraints(293, 110, 80, 80));

        btnNonAvailableItem9.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnNonAvailableItem9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNonAvailableItem9.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnNonAvailableItem9MouseClicked(evt);
            }
        });
        panelOpenTable.add(btnNonAvailableItem9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 208, 80, 80));

        btnNonAvailableItem10.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnNonAvailableItem10.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNonAvailableItem10.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnNonAvailableItem10MouseClicked(evt);
            }
        });
        panelOpenTable.add(btnNonAvailableItem10, new org.netbeans.lib.awtextra.AbsoluteConstraints(105, 208, 80, 80));

        btnNonAvailableItem11.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnNonAvailableItem11.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNonAvailableItem11.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnNonAvailableItem11MouseClicked(evt);
            }
        });
        panelOpenTable.add(btnNonAvailableItem11, new org.netbeans.lib.awtextra.AbsoluteConstraints(203, 208, 80, 80));

        btnNonAvailableItem12.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnNonAvailableItem12.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNonAvailableItem12.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnNonAvailableItem12MouseClicked(evt);
            }
        });
        panelOpenTable.add(btnNonAvailableItem12, new org.netbeans.lib.awtextra.AbsoluteConstraints(293, 208, 80, 80));

        btnNonAvailableItem13.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnNonAvailableItem13.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNonAvailableItem13.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnNonAvailableItem13MouseClicked(evt);
            }
        });
        panelOpenTable.add(btnNonAvailableItem13, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 306, 80, 80));

        btnNonAvailableItem14.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnNonAvailableItem14.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNonAvailableItem14.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnNonAvailableItem14MouseClicked(evt);
            }
        });
        btnNonAvailableItem14.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnNonAvailableItem14ActionPerformed(evt);
            }
        });
        panelOpenTable.add(btnNonAvailableItem14, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 306, 80, 80));

        btnNonAvailableItem15.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnNonAvailableItem15.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNonAvailableItem15.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnNonAvailableItem15MouseClicked(evt);
            }
        });
        panelOpenTable.add(btnNonAvailableItem15, new org.netbeans.lib.awtextra.AbsoluteConstraints(198, 306, 80, 80));

        btnNonAvailableItem16.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnNonAvailableItem16.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNonAvailableItem16.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnNonAvailableItem16MouseClicked(evt);
            }
        });
        panelOpenTable.add(btnNonAvailableItem16, new org.netbeans.lib.awtextra.AbsoluteConstraints(296, 306, 80, 80));

        btnSave.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnSave.setForeground(new java.awt.Color(255, 255, 255));
        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnSave.setText("SAVE");
        btnSave.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSave.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnSave.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnSaveActionPerformed(evt);
            }
        });

        btnClose.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnClose.setForeground(new java.awt.Color(255, 255, 255));
        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnClose.setText("CLOSE");
        btnClose.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnClose.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnClose.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCloseActionPerformed(evt);
            }
        });

        btnPrevious.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnPrevious.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgBackBtn1.png"))); // NOI18N
        btnPrevious.setText("<<<");
        btnPrevious.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPrevious.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgBackBtn2.png"))); // NOI18N
        btnPrevious.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnPreviousActionPerformed(evt);
            }
        });

        btnNext.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnNext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgBackBtn1.png"))); // NOI18N
        btnNext.setText(">>>");
        btnNext.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNext.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgBackBtn2.png"))); // NOI18N
        btnNext.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnNextActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelFormBodyLayout = new javax.swing.GroupLayout(panelFormBody);
        panelFormBody.setLayout(panelFormBodyLayout);
        panelFormBodyLayout.setHorizontalGroup(
            panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFormBodyLayout.createSequentialGroup()
                .addContainerGap(48, Short.MAX_VALUE)
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addGap(438, 438, 438)
                        .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(56, 56, 56)
                        .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(81, 81, 81))
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(panelOpenTable, javax.swing.GroupLayout.PREFERRED_SIZE, 401, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panelFormBodyLayout.createSequentialGroup()
                                .addGap(14, 14, 14)
                                .addComponent(btnPrevious, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblOpenTableName, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(32, 32, 32)
                                .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(panelSelectItem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(16, 16, 16))))
        );
        panelFormBodyLayout.setVerticalGroup(
            panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFormBodyLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addComponent(panelOpenTable, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(43, 43, 43)
                        .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnPrevious, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblOpenTableName, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(19, 19, 19))
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addComponent(panelSelectItem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)))
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        panelMainForm.add(panelFormBody, new java.awt.GridBagConstraints());

        getContentPane().add(panelMainForm, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

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

    private void formWindowClosed(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosed
    {//GEN-HEADEREND:event_formWindowClosed
        clsGlobalVarClass.hmActiveForms.remove("Move KOT");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosing
    {//GEN-HEADEREND:event_formWindowClosing
        clsGlobalVarClass.hmActiveForms.remove("Move KOT");
    }//GEN-LAST:event_formWindowClosing

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed
        // TODO add your handling code here:
        try
        {
           
            cntNavigate++;
            int tableSize = cntNavigate * 16;
            int resMod = setNonAvailableItem.size() % tableSize;
            int resDiv = setNonAvailableItem.size() / 16;
            int totalSize = tableSize + 16;
            funLoadNonAvailableItems(tableSize, totalSize);
            btnPrevious.setEnabled(true);
             if (resDiv == cntNavigate)
            {
                btnNext.setEnabled(false);
            }
//            if (resDiv > cntNavigate)
//            {
//                btnNext.setEnabled(true);
//            }else{
//                btnNext.setEnabled(false);
//            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnNextActionPerformed

    private void btnPreviousActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPreviousActionPerformed
        // TODO add your handling code here:
        try
        {
            cntNavigate--;
            btnNext.setEnabled(true);
            if (cntNavigate == 0)
            {
                btnPrevious.setEnabled(false);
                funLoadNonAvailableItems(0, setNonAvailableItem.size());
            }
            else
            {
                int tableSize = cntNavigate * 16;
                int resMod = setNonAvailableItem.size() % tableSize;
                int resDiv = setNonAvailableItem.size() / tableSize;
                int totalSize = tableSize + 16;
                //System.out.println("Size="+vOpenTableNo.size()+"\tMod="+resMod+"\tdiv="+resDiv+"\tsss="+tableSize);
                 funLoadNonAvailableItems(tableSize, totalSize);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnPreviousActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        dispose();
        clsGlobalVarClass.hmActiveForms.remove("Non Available Items");
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        try
        {

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnNonAvailableItem16MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNonAvailableItem16MouseClicked
        // TODO add your handling code here:
         funRemoveNonAvailableItem(btnNonAvailableItem16.getText(),15);
    }//GEN-LAST:event_btnNonAvailableItem16MouseClicked

    private void btnNonAvailableItem15MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNonAvailableItem15MouseClicked
        funRemoveNonAvailableItem(btnNonAvailableItem15.getText(),14);
    }//GEN-LAST:event_btnNonAvailableItem15MouseClicked

    private void btnNonAvailableItem14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNonAvailableItem14ActionPerformed
        // TODO add your handling code here:
         funRemoveNonAvailableItem(btnNonAvailableItem14.getText(),13);
    }//GEN-LAST:event_btnNonAvailableItem14ActionPerformed

    private void btnNonAvailableItem14MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNonAvailableItem14MouseClicked
         funRemoveNonAvailableItem(btnNonAvailableItem14.getText(),13);
    }//GEN-LAST:event_btnNonAvailableItem14MouseClicked

    private void btnNonAvailableItem13MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNonAvailableItem13MouseClicked
        funRemoveNonAvailableItem(btnNonAvailableItem13.getText(),12);
    }//GEN-LAST:event_btnNonAvailableItem13MouseClicked

    private void btnNonAvailableItem12MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNonAvailableItem12MouseClicked
        funRemoveNonAvailableItem(btnNonAvailableItem12.getText(),11);
    }//GEN-LAST:event_btnNonAvailableItem12MouseClicked

    private void btnNonAvailableItem11MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNonAvailableItem11MouseClicked
       funRemoveNonAvailableItem(btnNonAvailableItem11.getText(),10);
    }//GEN-LAST:event_btnNonAvailableItem11MouseClicked

    private void btnNonAvailableItem10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNonAvailableItem10MouseClicked
         funRemoveNonAvailableItem(btnNonAvailableItem10.getText(),9);
    }//GEN-LAST:event_btnNonAvailableItem10MouseClicked

    private void btnNonAvailableItem9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNonAvailableItem9MouseClicked
        funRemoveNonAvailableItem(btnNonAvailableItem9.getText(),8);
    }//GEN-LAST:event_btnNonAvailableItem9MouseClicked

    private void btnNonAvailableItem8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNonAvailableItem8MouseClicked
         funRemoveNonAvailableItem(btnNonAvailableItem8.getText(),7);

    }//GEN-LAST:event_btnNonAvailableItem8MouseClicked

    private void btnNonAvailableItem7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNonAvailableItem7MouseClicked
         funRemoveNonAvailableItem(btnNonAvailableItem7.getText(),6);
    }//GEN-LAST:event_btnNonAvailableItem7MouseClicked

    private void btnNonAvailableItem6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNonAvailableItem6MouseClicked
         funRemoveNonAvailableItem(btnNonAvailableItem6.getText(),5);

    }//GEN-LAST:event_btnNonAvailableItem6MouseClicked

    private void btnNonAvailableItem5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNonAvailableItem5MouseClicked
        funRemoveNonAvailableItem(btnNonAvailableItem5.getText(),4);
    }//GEN-LAST:event_btnNonAvailableItem5MouseClicked

    private void btnNonAvailableItem4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNonAvailableItem4MouseClicked
       funRemoveNonAvailableItem(btnNonAvailableItem4.getText(),3);
    }//GEN-LAST:event_btnNonAvailableItem4MouseClicked

    private void btnNonAvailableItem3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNonAvailableItem3MouseClicked
        // TODO add your handling code here:
            //JOptionPane.showMessageDialog(this, "3");
             funRemoveNonAvailableItem(btnNonAvailableItem3.getText(),2);
        
    }//GEN-LAST:event_btnNonAvailableItem3MouseClicked

    private void btnNonAvailableItem2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNonAvailableItem2MouseClicked
        // TODO add your handling code here:
       //  JOptionPane.showMessageDialog(this, "2");
         funRemoveNonAvailableItem(btnNonAvailableItem2.getText(),1);
    }//GEN-LAST:event_btnNonAvailableItem2MouseClicked

    private void btnNonAvailableItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNonAvailableItem1ActionPerformed
        // TODO add your handling code here:
       // JOptionPane.showMessageDialog(this, "1");
        funRemoveNonAvailableItem(btnNonAvailableItem1.getText(),0);
    }//GEN-LAST:event_btnNonAvailableItem1ActionPerformed

    private void btnNonAvailableItem1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNonAvailableItem1MouseClicked
        // TODO add your handling code here:
        JOptionPane.showMessageDialog(this, "1");
        if (btnNonAvailableItem1.isEnabled())
        {
            // funSetDefaultColorOpen(0);

        }
    }//GEN-LAST:event_btnNonAvailableItem1MouseClicked

    private void lblSearchMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblSearchMouseClicked
        // TODO add your handling code here:
        funSearchItem();
    }//GEN-LAST:event_lblSearchMouseClicked

    private void tblItemsKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblItemsKeyPressed
        if (evt.getKeyCode() == 10)
        {
            // btnButton2.requestFocus();
            int selectedRow = tblItems.getSelectedRow();
            // funPLUItemsMouseClicked(selectedRow);
        }
    }//GEN-LAST:event_tblItemsKeyPressed

    private void tblItemsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblItemsMouseClicked
        int selectedRow = tblItems.getSelectedRow();
        if(setNonAvailableItem.size()%16==0)
        {
//            if(!btnNext.isEnabled()){
//                btnNext.setEnabled(true);
//            }
            //cntNavigate++;
        }
        funItemsMouseClicked();
    }//GEN-LAST:event_tblItemsMouseClicked

    private void txtItemSearchKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtItemSearchKeyTyped
//        new frmAlfaNumericKeyBoard(this, true, "1", "Search").setVisible(true);
//        txtItemSearch.setText(clsGlobalVarClass.gKeyboardValue);
//        funSearchItem();
    }//GEN-LAST:event_txtItemSearchKeyTyped

    private void txtItemSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtItemSearchKeyReleased
        // funPLUItemSearch();
    }//GEN-LAST:event_txtItemSearchKeyReleased

    private void txtItemSearchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtItemSearchKeyPressed
      //  new frmAlfaNumericKeyBoard(this, true, "1", "Search").setVisible(true);
       // txtItemSearch.setText(clsGlobalVarClass.gKeyboardValue);
         funSearchItem();
    }//GEN-LAST:event_txtItemSearchKeyPressed

    private void txtItemSearchMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtItemSearchMouseClicked
      //  new frmAlfaNumericKeyBoard(this, true, "1", "Search").setVisible(true);
      //  txtItemSearch.setText(clsGlobalVarClass.gKeyboardValue);
         funSearchItem();
        // new frmAlfaNumericKeyBoard1(this, true, "Make KOT");
    }//GEN-LAST:event_txtItemSearchMouseClicked

    private void txtItemSearchFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtItemSearchFocusGained
        //  funPLUItemSearch();
    }//GEN-LAST:event_txtItemSearchFocusGained

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
            java.util.logging.Logger.getLogger(frmNonAvailableItems.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(frmNonAvailableItems.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(frmNonAvailableItems.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(frmNonAvailableItems.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                new frmNonAvailableItems().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnNonAvailableItem1;
    private javax.swing.JButton btnNonAvailableItem10;
    private javax.swing.JButton btnNonAvailableItem11;
    private javax.swing.JButton btnNonAvailableItem12;
    private javax.swing.JButton btnNonAvailableItem13;
    private javax.swing.JButton btnNonAvailableItem14;
    private javax.swing.JButton btnNonAvailableItem15;
    private javax.swing.JButton btnNonAvailableItem16;
    private javax.swing.JButton btnNonAvailableItem2;
    private javax.swing.JButton btnNonAvailableItem3;
    private javax.swing.JButton btnNonAvailableItem4;
    private javax.swing.JButton btnNonAvailableItem5;
    private javax.swing.JButton btnNonAvailableItem6;
    private javax.swing.JButton btnNonAvailableItem7;
    private javax.swing.JButton btnNonAvailableItem8;
    private javax.swing.JButton btnNonAvailableItem9;
    private javax.swing.JButton btnPrevious;
    private javax.swing.JButton btnSave;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblOpenTableName;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblSearch;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblformName;
    private javax.swing.JPanel panelFormBody;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelMainForm;
    private javax.swing.JPanel panelOpenTable;
    private javax.swing.JPanel panelSelectItem;
    private javax.swing.JScrollPane scrPLU;
    private javax.swing.JTable tblItems;
    private javax.swing.JTextField txtItemSearch;
    // End of variables declaration//GEN-END:variables

   
}
