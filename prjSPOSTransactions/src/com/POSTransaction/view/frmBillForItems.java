/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSTransaction.view;

import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsUtility;
import com.POSGlobal.view.frmNumberKeyPad;
import com.POSTransaction.controller.clsMakeKotItemDtl;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

public class frmBillForItems extends javax.swing.JFrame
{

    public Vector vAllTableNo, vAllTableName, vBussyTableNo, vBussyTableName;
    public String sql, fromTableNo;
    public int cntNavigate, cntNavigate1, tblStartIndex, tblEndIndex;
    private JButton[] btnTableArray;
    private clsUtility objUtility;
    private HashMap<String, String> mapGetPOSCode;
    private HashMap<String, String> mapGetBussyTableNo;

    private final DecimalFormat gDecimalFormat = clsGlobalVarClass.funGetGlobalDecimalFormatter();
    private final DecimalFormat gDecimalFormatForQty = new DecimalFormat("0.0");
    private String fromTableName;
    private ArrayList<clsMakeKotItemDtl> listOfKOTWiseItemDtl;

    public frmBillForItems()
    {
	initComponents();
	try
	{

	    objUtility = new clsUtility();
	    vAllTableNo = new Vector();
	    vAllTableName = new Vector();
	    vBussyTableNo = new Vector();
	    vBussyTableName = new Vector();

	    mapGetPOSCode = new HashMap<String, String>();
	    mapGetBussyTableNo = new HashMap<String, String>();

	    cntNavigate = 0;
	    cntNavigate1 = 0;
	    lblPosName.setText(clsGlobalVarClass.gPOSName);
	    Date date1 = new Date();
	    String new_str = String.format("%tr", date1);
	    String dateAndTime = clsGlobalVarClass.gPOSDateToDisplay + " " + new_str;
	    lblDate.setText(dateAndTime);
	    lblUserCode.setText(clsGlobalVarClass.gUserCode);
	    lblModuleName.setText(clsGlobalVarClass.gSelectedModule);

	    funFillBussyTableCombo();

	    if (clsGlobalVarClass.gUserType.equalsIgnoreCase("Super"))
	    {
		cmbBusyTables.setEnabled(true);
	    }
	    else
	    {

	    }

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funFillBussyTableCombo() throws Exception
    {
	//cmbTable.addItem("Select Table");

	cmbBusyTables.removeAllItems();
	mapGetBussyTableNo.clear();

	cmbBusyTables.addItem("All");
	mapGetBussyTableNo.put("All", "All");
	sql = "select distinct a.strTableNo,b.strTableName,b.strStatus "
		+ "from tblitemrtemp a,tbltablemaster b "
		+ "where a.strTableNo=b.strTableNo "
		+ "and a.strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
		+ "and a.strNCKotYN='N' "
		+ "order by b.intSequence ";
	ResultSet rsTable = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	while (rsTable.next())
	{
	    cmbBusyTables.addItem(rsTable.getString(2));
	    mapGetBussyTableNo.put(rsTable.getString(2), rsTable.getString(1));
	}
	rsTable.close();
    }

    private void funMakeBillForItems() throws CloneNotSupportedException
    {
	try
	{

	    List<clsMakeKotItemDtl> listOfItemsForToBeBilled = new ArrayList<clsMakeKotItemDtl>();

	    DefaultTableModel defaultTableModel = (DefaultTableModel) tblBussyTableItems.getModel();
	    int rowCount = defaultTableModel.getRowCount();

	    for (int r = 0; r < rowCount; r++)
	    {

		String itemName = defaultTableModel.getValueAt(r, 0).toString();
		double itemRate = Double.parseDouble(defaultTableModel.getValueAt(r, 1).toString());
		double itemQty = Double.parseDouble(defaultTableModel.getValueAt(r, 2).toString());
		double moveQty = Double.parseDouble(defaultTableModel.getValueAt(r, 3).toString());

		boolean isItemSelected = Boolean.parseBoolean(defaultTableModel.getValueAt(r, 5).toString());
		String itemCode = defaultTableModel.getValueAt(r, 6).toString();

		double itemAmt = itemRate * itemQty;
		double moveItemAmt = itemRate * moveQty;

		if (isItemSelected)
		{

		    itemAmt = itemRate * itemQty;
		    moveItemAmt = itemRate * moveQty;

		    while (moveQty > 0)
		    {
			/**
			 * For exact Qty will match
			 */
			boolean isExactQtyMatch = false;
			Iterator<clsMakeKotItemDtl> itForExactQtyKOTWiseItemDtl = listOfKOTWiseItemDtl.iterator();
			while (itForExactQtyKOTWiseItemDtl.hasNext())
			{
			    clsMakeKotItemDtl objKOTWiseItemDtlOriginal = itForExactQtyKOTWiseItemDtl.next();

			    String kotWiseItemCode = objKOTWiseItemDtlOriginal.getItemCode();
			    double kotWiseItemQty = objKOTWiseItemDtlOriginal.getQty();

			    if (kotWiseItemCode.equals(itemCode) && kotWiseItemQty == moveQty)
			    {
				clsMakeKotItemDtl objKOTWiseItemDtlClone = (clsMakeKotItemDtl) objKOTWiseItemDtlOriginal.clone();

				listOfItemsForToBeBilled.add(objKOTWiseItemDtlClone);

				objKOTWiseItemDtlOriginal.setQty(0.00);
				objKOTWiseItemDtlOriginal.setAmt(0.00);

				isExactQtyMatch = true;

				moveQty = 0;
				break;
			    }
			    else
			    {
				continue;
			    }
			}

			/**
			 * if exact Qty not match
			 */
			if (!isExactQtyMatch)
			{
			    Iterator<clsMakeKotItemDtl> itForKOTWiseItemDtl = listOfKOTWiseItemDtl.iterator();
			    while (itForKOTWiseItemDtl.hasNext())
			    {
				clsMakeKotItemDtl objKOTWiseItemDtlOriginal = itForKOTWiseItemDtl.next();

				String kotWiseItemCode = objKOTWiseItemDtlOriginal.getItemCode();
				double kotWiseItemQty = objKOTWiseItemDtlOriginal.getQty();

				if (kotWiseItemQty <= 0)
				{
				    continue;
				}
				else
				{
				    if (kotWiseItemCode.equals(itemCode))
				    {
					clsMakeKotItemDtl objKOTWiseItemDtlClone = (clsMakeKotItemDtl) objKOTWiseItemDtlOriginal.clone();

					if (kotWiseItemQty > moveQty)
					{

					    kotWiseItemQty = kotWiseItemQty - moveQty;

					    objKOTWiseItemDtlOriginal.setQty(kotWiseItemQty);
					    objKOTWiseItemDtlOriginal.setAmt(objKOTWiseItemDtlOriginal.getItemRate() * kotWiseItemQty);

					    objKOTWiseItemDtlClone.setQty(moveQty);
					    objKOTWiseItemDtlClone.setAmt(objKOTWiseItemDtlClone.getItemRate() * moveQty);

					    listOfItemsForToBeBilled.add(objKOTWiseItemDtlClone);

					    moveQty = 0;

					    break;
					}
					else if (moveQty > kotWiseItemQty)
					{

					    objKOTWiseItemDtlOriginal.setQty(0);
					    objKOTWiseItemDtlOriginal.setAmt(0);

					    objKOTWiseItemDtlClone.setQty(kotWiseItemQty);
					    objKOTWiseItemDtlClone.setAmt(objKOTWiseItemDtlClone.getItemRate() * kotWiseItemQty);

					    listOfItemsForToBeBilled.add(objKOTWiseItemDtlClone);

					    moveQty = moveQty - kotWiseItemQty;
					}
				    }
				    else
				    {
					continue;
				    }
				}
			    }
			}
		    }
		}

	    }

	    /**
	     * make bill for these items and for this table
	     */
	    new frmBillSettlement(this, fromTableNo, listOfKOTWiseItemDtl, listOfItemsForToBeBilled).setVisible(true);

	    sql = "select strKOTNo,dteDateCreated from tblitemrtemp where strTableNo='" + fromTableNo + "' and strNCKotYN='N' ";
	    ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (!rs.next())
	    {
		sql = "update tbltablemaster set strStatus='Normal',intPaxNo=0 "
			+ "where strTableNo='" + fromTableNo + "'";
		clsGlobalVarClass.dbMysql.execute(sql);
//            }

		//insert into itemrtempbck tabl
		objUtility.funInsertIntoTblItemRTempBck(fromTableNo);
	    }

	    funClearFields();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funClearFields() throws Exception
    {
	lblFromTableName.setText("");

	cmbBusyTables.setSelectedIndex(0);

	DefaultTableModel defaultTableModel = (DefaultTableModel) tblBussyTableItems.getModel();
	defaultTableModel.setRowCount(0);

	funFillBussyTableCombo();
    }

    private void funTableComboSelected(String bussyTableName)
    {
	String bussyTableNo = mapGetBussyTableNo.get(bussyTableName);

	funFillItemsForSelectedTable(bussyTableNo, bussyTableName);
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
        panelBusyTableItems = new javax.swing.JPanel();
        scrollBussyTableItems = new javax.swing.JScrollPane();
        tblBussyTableItems = new javax.swing.JTable();
        lblOpenTable = new javax.swing.JLabel();
        btnSave = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        btnShowKOT = new javax.swing.JButton();
        cmbBusyTables = new javax.swing.JComboBox();
        lblFromTableName = new javax.swing.JLabel();

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
        lblformName.setText("- Bill For Items");
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

        panelBusyTableItems.setBackground(new java.awt.Color(255, 255, 255));
        panelBusyTableItems.setEnabled(false);
        panelBusyTableItems.setOpaque(false);

        tblBussyTableItems.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tblBussyTableItems.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Description", "Rate", "Qty", "Move Qty", "Amount", "Select", "", ""
            }
        )
        {
            Class[] types = new Class []
            {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean []
            {
                false, false, false, false, false, true, false, false
            };

            public Class getColumnClass(int columnIndex)
            {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return canEdit [columnIndex];
            }
        });
        tblBussyTableItems.setPreferredSize(new java.awt.Dimension(400, 420));
        tblBussyTableItems.setRowHeight(35);
        tblBussyTableItems.getTableHeader().setReorderingAllowed(false);
        tblBussyTableItems.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tblBussyTableItemsMouseClicked(evt);
            }
        });
        scrollBussyTableItems.setViewportView(tblBussyTableItems);
        if (tblBussyTableItems.getColumnModel().getColumnCount() > 0)
        {
            tblBussyTableItems.getColumnModel().getColumn(0).setPreferredWidth(350);
            tblBussyTableItems.getColumnModel().getColumn(3).setPreferredWidth(120);
            tblBussyTableItems.getColumnModel().getColumn(4).setPreferredWidth(100);
            tblBussyTableItems.getColumnModel().getColumn(6).setPreferredWidth(0);
            tblBussyTableItems.getColumnModel().getColumn(7).setPreferredWidth(0);
        }

        javax.swing.GroupLayout panelBusyTableItemsLayout = new javax.swing.GroupLayout(panelBusyTableItems);
        panelBusyTableItems.setLayout(panelBusyTableItemsLayout);
        panelBusyTableItemsLayout.setHorizontalGroup(
            panelBusyTableItemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 768, Short.MAX_VALUE)
            .addGroup(panelBusyTableItemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelBusyTableItemsLayout.createSequentialGroup()
                    .addComponent(scrollBussyTableItems, javax.swing.GroupLayout.PREFERRED_SIZE, 768, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        panelBusyTableItemsLayout.setVerticalGroup(
            panelBusyTableItemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 463, Short.MAX_VALUE)
            .addGroup(panelBusyTableItemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBusyTableItemsLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(scrollBussyTableItems, javax.swing.GroupLayout.DEFAULT_SIZE, 452, Short.MAX_VALUE)))
        );

        lblOpenTable.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblOpenTable.setForeground(new java.awt.Color(51, 51, 255));
        lblOpenTable.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblOpenTable.setText("BUSY TABLES");

        btnSave.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnSave.setForeground(new java.awt.Color(255, 255, 255));
        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnSave.setText("MAKE BILL");
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

        btnShowKOT.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnShowKOT.setForeground(new java.awt.Color(255, 255, 255));
        btnShowKOT.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong1.png"))); // NOI18N
        btnShowKOT.setText("VIEW KOT");
        btnShowKOT.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnShowKOT.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtnLong2.png"))); // NOI18N
        btnShowKOT.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnShowKOTActionPerformed(evt);
            }
        });

        cmbBusyTables.setToolTipText("Select Table");
        cmbBusyTables.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbBusyTablesActionPerformed(evt);
            }
        });

        lblFromTableName.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N

        javax.swing.GroupLayout panelFormBodyLayout = new javax.swing.GroupLayout(panelFormBody);
        panelFormBody.setLayout(panelFormBodyLayout);
        panelFormBodyLayout.setHorizontalGroup(
            panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFormBodyLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelBusyTableItems, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelFormBodyLayout.createSequentialGroup()
                                .addComponent(btnShowKOT, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblFromTableName, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelFormBodyLayout.createSequentialGroup()
                                .addComponent(cmbBusyTables, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(lblOpenTable)))
                        .addGap(225, 225, 225)
                        .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(56, 56, 56)
                        .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(13, 13, 13))
        );
        panelFormBodyLayout.setVerticalGroup(
            panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFormBodyLayout.createSequentialGroup()
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addComponent(cmbBusyTables, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8))
                    .addGroup(panelFormBodyLayout.createSequentialGroup()
                        .addComponent(lblOpenTable, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(panelBusyTableItems, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelFormBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnShowKOT, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblFromTableName, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        panelMainForm.add(panelFormBody, new java.awt.GridBagConstraints());

        getContentPane().add(panelMainForm, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
	// TODO add your handling code here:
	try
	{
	    if (clsGlobalVarClass.gEnableLockTables && objUtility.funCheckTableStatusFromItemRTemp(fromTableNo))
	    {
		JOptionPane.showMessageDialog(null, "Billing is in process on this table, table No = " + fromTableName);
	    }
	    else
	    {
		funMakeBillForItems();
	    }

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
	// TODO add your handling code here:
	dispose();
	clsGlobalVarClass.hmActiveForms.remove("Bill For Items");
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnShowKOTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShowKOTActionPerformed


    }//GEN-LAST:event_btnShowKOTActionPerformed

    private void cmbBusyTablesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbBusyTablesActionPerformed
	// TODO add your handling code here:
	if (cmbBusyTables.getSelectedIndex() > 0)
	{
	    funTableComboSelected(cmbBusyTables.getSelectedItem().toString());
	}
    }//GEN-LAST:event_cmbBusyTablesActionPerformed

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
	clsGlobalVarClass.hmActiveForms.remove("Bill For Items");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosing
    {//GEN-HEADEREND:event_formWindowClosing
	clsGlobalVarClass.hmActiveForms.remove("Bill For Items");
    }//GEN-LAST:event_formWindowClosing

    private void tblBussyTableItemsMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_tblBussyTableItemsMouseClicked
    {//GEN-HEADEREND:event_tblBussyTableItemsMouseClicked
	funBussyTableMouseClicked(evt);
    }//GEN-LAST:event_tblBussyTableItemsMouseClicked

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
	    java.util.logging.Logger.getLogger(frmBillForItems.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	}
	catch (InstantiationException ex)
	{
	    java.util.logging.Logger.getLogger(frmBillForItems.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	}
	catch (IllegalAccessException ex)
	{
	    java.util.logging.Logger.getLogger(frmBillForItems.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	}
	catch (javax.swing.UnsupportedLookAndFeelException ex)
	{
	    java.util.logging.Logger.getLogger(frmBillForItems.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
		new frmBillForItems().setVisible(true);
	    }
	});
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnShowKOT;
    private javax.swing.JComboBox cmbBusyTables;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblFromTableName;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblOpenTable;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblformName;
    private javax.swing.JPanel panelBusyTableItems;
    private javax.swing.JPanel panelFormBody;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelMainForm;
    private javax.swing.JScrollPane scrollBussyTableItems;
    private javax.swing.JTable tblBussyTableItems;
    // End of variables declaration//GEN-END:variables

    private void funFillItemsForSelectedTable(String bussyTableNo, String bussyTableName)
    {
	try
	{

	    DefaultTableModel defaultTableModel = (DefaultTableModel) tblBussyTableItems.getModel();
	    defaultTableModel.setRowCount(0);

	    String sqlTableItemDtl = " select strItemCode,strItemName,dblRate,sum(dblItemQuantity),sum(dblAmount),sum(a.dblTaxAmt),a.strWaiterNo "
		    + "from tblitemrtemp a  "
		    + "where strTableNo='" + bussyTableNo + "'  "
		    + "and strPosCode='" + clsGlobalVarClass.gPOSCode + "' "
		    + "and strNcKotYN='N' "
		    + "group by a.strItemCode "
		    + "order by strKOTNo desc ,strSerialNo ";
	    ResultSet rsBussyTableItems = clsGlobalVarClass.dbMysql.executeResultSet(sqlTableItemDtl);
	    while (rsBussyTableItems.next())
	    {
		Object[] row =
		{
		    rsBussyTableItems.getString(2), gDecimalFormat.format(rsBussyTableItems.getDouble(3)), gDecimalFormatForQty.format(rsBussyTableItems.getDouble(4)), gDecimalFormatForQty.format(rsBussyTableItems.getDouble(4)), gDecimalFormat.format(rsBussyTableItems.getDouble(5)), false, rsBussyTableItems.getString(1), rsBussyTableItems.getString(7)
		};

		defaultTableModel.addRow(row);
	    }
	    rsBussyTableItems.close();

	    fromTableNo = bussyTableNo;
	    fromTableName = bussyTableName;
	    lblFromTableName.setText(fromTableName);

	    /**
	     * fill KOT wise item dtl list
	     */
	    listOfKOTWiseItemDtl = new ArrayList<clsMakeKotItemDtl>();

	    sqlTableItemDtl = " select strItemCode,strItemName,a.strKOTNo,dblRate,sum(dblItemQuantity),sum(dblAmount),sum(a.dblTaxAmt),a.strWaiterNo "
		    + ",a.strSerialNo,a.intPaxNo "
		    + "from tblitemrtemp a  "
		    + "where strTableNo='" + bussyTableNo + "'  "
		    + "and strPosCode='" + clsGlobalVarClass.gPOSCode + "'  "
		    + "and strNcKotYN='N' "
		    + "group by a.strKOTNo,a.strItemCode "
		    + "order by strKOTNo ,strSerialNo ";
	    rsBussyTableItems = clsGlobalVarClass.dbMysql.executeResultSet(sqlTableItemDtl);
	    while (rsBussyTableItems.next())
	    {
		String itemCode = rsBussyTableItems.getString(1);
		String itemName = rsBussyTableItems.getString(2);
		String kotNo = rsBussyTableItems.getString(3);
		double itemRate = Double.parseDouble(gDecimalFormat.format(rsBussyTableItems.getDouble(4)));
		double itemQty = Double.parseDouble(gDecimalFormat.format(rsBussyTableItems.getDouble(5)));
		double itemAmt = Double.parseDouble(gDecimalFormat.format(rsBussyTableItems.getDouble(6)));
		double taxAmt = Double.parseDouble(gDecimalFormat.format(rsBussyTableItems.getDouble(7)));
		String waiterNo = rsBussyTableItems.getString(8);
		String sequenseNo = rsBussyTableItems.getString(9);
		int pax = rsBussyTableItems.getInt(10);

		clsMakeKotItemDtl objItemForFromTable = new clsMakeKotItemDtl(sequenseNo, kotNo, bussyTableNo, waiterNo, itemName, itemCode, itemQty, itemAmt, pax, "Y", "N", false, "", "", "", "N", itemRate);

		listOfKOTWiseItemDtl.add(objItemForFromTable);

	    }
	    rsBussyTableItems.close();

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funBussyTableMouseClicked(MouseEvent evt)
    {
	try
	{
	    int columnNo = tblBussyTableItems.getSelectedColumn();
	    int rowNo = tblBussyTableItems.getSelectedRow();

	    if (columnNo == 3)//move qty
	    {
		double originalQty = Double.parseDouble(tblBussyTableItems.getValueAt(rowNo, 2).toString());
		if (originalQty > 1)
		{
		    frmNumberKeyPad num = new frmNumberKeyPad(this, true, "Move Qty");
		    num.setVisible(true);
		    if (null != clsGlobalVarClass.gNumerickeyboardValue)
		    {
			if (!clsGlobalVarClass.gNumerickeyboardValue.isEmpty())
			{
			    double enterQty = Double.parseDouble(clsGlobalVarClass.gNumerickeyboardValue);

			    if (enterQty > 0)
			    {
				if (enterQty > originalQty)
				{
				    JOptionPane.showMessageDialog(null, "Please enter the valid quantity.");
				    return;
				}
				else
				{
				    tblBussyTableItems.setValueAt(gDecimalFormatForQty.format(enterQty), rowNo, 3);
				    clsGlobalVarClass.gNumerickeyboardValue = null;
				}
			    }
			    else
			    {
				JOptionPane.showMessageDialog(null, "Please enter the valid quantity.");
				return;
			    }
			}
			else
			{
			    JOptionPane.showMessageDialog(null, "Please enter the valid quantity.");
			    return;
			}
		    }
		    else
		    {
			JOptionPane.showMessageDialog(null, "Please enter the valid quantity.");
			return;
		    }
		}
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }
}
