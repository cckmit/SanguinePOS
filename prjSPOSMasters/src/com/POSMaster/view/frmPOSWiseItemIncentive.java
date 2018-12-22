/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSMaster.view;

import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsUtility;
import com.POSGlobal.view.frmOkPopUp;
import com.POSGlobal.view.frmSearchFormDialog;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.sql.ResultSet;
import java.util.HashMap;
import javax.swing.AbstractCellEditor;
import javax.swing.DefaultCellEditor;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.NumberFormat;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.Number;

public class frmPOSWiseItemIncentive extends javax.swing.JFrame
{

    private HashMap<String, String> mapPOSCode, mapPOSName;
    private StringBuilder sb = new StringBuilder();
    private ResultSet rs;
    clsUtility objUtility = new clsUtility();
    String strSearchItemCode = "";

    public frmPOSWiseItemIncentive()
    {
	initComponents();
	try
	{
	    lblUserCode.setText(clsGlobalVarClass.gUserCode);
	    lblPosName.setText(clsGlobalVarClass.gPOSName);
	    lblDate.setText(clsGlobalVarClass.gPOSDateToDisplay);
	    lblModuleName.setText(clsGlobalVarClass.gSelectedModule);
	    funLoadPOSCombo();
	    funSetShortCutKeys();
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}

    }

    private void funSetShortCutKeys()
    {
	btnExit.setMnemonic('c');
	btnExecute.setMnemonic('s');
	btnReset.setMnemonic('r');
    }

    /**
     * This method is used to load pos codes in cobobox
     */
    private void funLoadPOSCombo() throws Exception
    {
	ResultSet rsPOS = clsGlobalVarClass.dbMysql.executeResultSet("select strPOSCode,strPOSName from tblposmaster ");
	mapPOSCode = new HashMap<String, String>();
	mapPOSName = new HashMap<String, String>();
	cmbPosCode.addItem("All");
	mapPOSCode.put("All", "All");
	mapPOSName.put("All", "All");
	while (rsPOS.next())
	{
	    cmbPosCode.addItem(rsPOS.getString(2));
	    mapPOSCode.put(rsPOS.getString(1), rsPOS.getString(2));
	    mapPOSName.put(rsPOS.getString(2), rsPOS.getString(1));
	}

    }

    private void funExecuteClick()
    {
	try
	{

	    boolean flgPreviousRecordFound = false;
	    String posCode = mapPOSName.get(cmbPosCode.getSelectedItem().toString());
	    DefaultTableModel dmImemTable = (DefaultTableModel) tblPoswiseItemIncentiveDtl.getModel();
	    dmImemTable.setRowCount(0);
	    sb.setLength(0);
	    rs = clsGlobalVarClass.dbMysql.executeResultSet(funGetDataFromIncentiveDtlTable().toString());

	    while (rs.next())
	    {
		flgPreviousRecordFound = true;
		Object[] itemRows =
		{
		    rs.getString(1), rs.getString(2), rs.getString(7), rs.getString(8), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(3)
		};

		dmImemTable.addRow(itemRows);
	    }

	    sb.setLength(0);
	    rs = clsGlobalVarClass.dbMysql.executeResultSet(funGetDataFromPricingTable().toString());

	    while (rs.next())
	    {
		flgPreviousRecordFound = true;
		Object[] itemRows =
		{
		    rs.getString(1), rs.getString(2), rs.getString(5), rs.getString(6), rs.getString(4), jComboBox1.getSelectedItem().toString(), "0.0", rs.getString(3)
		};

		dmImemTable.addRow(itemRows);
	    }

	    rs.close();

	    if (!flgPreviousRecordFound)
	    {
		rs = clsGlobalVarClass.dbMysql.executeResultSet(funGetExecuteString().toString());

		while (rs.next())
		{
		    Object[] itemRows =
		    {
			rs.getString(1), rs.getString(2), rs.getString(5), rs.getString(6), rs.getString(4), jComboBox1.getSelectedItem().toString(), "0.0", rs.getString(3)
		    };

		    dmImemTable.addRow(itemRows);

		}
		rs.close();
	    }
	    tblPoswiseItemIncentiveDtl.setModel(dmImemTable);

	    sb.setLength(0);

	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}

    }

    private StringBuilder funGetExecuteString()
    {
	try
	{
	    String posCode = "";
	    if (!cmbPosCode.getSelectedItem().toString().equals("All"))
	    {
		posCode = mapPOSName.get(cmbPosCode.getSelectedItem().toString());
	    }
	    else
	    {
		posCode = cmbPosCode.getSelectedItem().toString();
	    }
	    sb.setLength(0);
	    sb.append("SELECT distinct(a.strItemCode),a.strItemName,a.strPosCode,b.strPosName,e.strGroupName,d.strSubGroupName "
		    + " FROM tblmenuitempricingdtl a  "
		    + " left outer join tblposmaster b on a.strPosCode=b.strPosCode "
		    + "join tblitemmaster c on a.strItemCode=c.strItemCode "
		    + "join tblsubgrouphd d on c.strSubGroupCode=d.strSubGroupCode "
		    + "join tblgrouphd e on d.strGroupCode=e.strGroupCode "
		    + " ");
	    //conditions append
	    if (!cmbPosCode.getSelectedItem().toString().equals("All"))
	    {
		sb.append("Where a.strPOSCode='").append(posCode).append("' ");
	    }
	    if (!strSearchItemCode.equals(""))
	    {
		sb.append("and a.strItemCode='").append(strSearchItemCode).append("' ");
	    }
	    sb.append(" order by a.strItemCode,e.strGroupName,d.strSubGroupName,b.strPosName  ");

	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}

	System.out.println("sb==== " + sb);

	return sb;
    }

    private StringBuilder funGetDataFromIncentiveDtlTable()
    {
	try
	{
	    String posCode = "";
	    if (!cmbPosCode.getSelectedItem().toString().equals("All"))
	    {
		posCode = mapPOSName.get(cmbPosCode.getSelectedItem().toString());
	    }
	    else
	    {
		posCode = cmbPosCode.getSelectedItem().toString();
	    }
	    sb.setLength(0);
	    sb.append("SELECT a.strItemCode,a.strItemName,a.strPOSCode,b.strPosName,a.strIncentiveType,a.dblIncentiveValue "
		    + ",e.strGroupName,d.strSubGroupName "
		    + " FROM tblposwiseitemwiseincentives a  "
		    + "left outer join tblposmaster b on (a.strPosCode=b.strPosCode or a.strPosCode='All') "
		    + "join tblitemmaster c on a.strItemCode=c.strItemCode "
		    + "join tblsubgrouphd d on c.strSubGroupCode=d.strSubGroupCode "
		    + "join tblgrouphd e on d.strGroupCode=e.strGroupCode ");
	    //conditions append
	    if (!cmbPosCode.getSelectedItem().toString().equals("All"))
	    {
		sb.append("Where a.strPOSCode='").append(posCode).append("' ");
	    }
	    if (!strSearchItemCode.equals(""))
	    {
		sb.append("and a.strItemCode='").append(strSearchItemCode).append("' ");
	    }
	    sb.append(" order by a.strItemCode,e.strGroupName,d.strSubGroupName,b.strPosName ");

	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}

	System.out.println("sb==== " + sb);

	return sb;
    }

    private StringBuilder funGetDataFromPricingTable()
    {
	try
	{
	    String posCode = "";
	    if (!cmbPosCode.getSelectedItem().toString().equals("All"))
	    {
		posCode = mapPOSName.get(cmbPosCode.getSelectedItem().toString());
	    }
	    else
	    {
		posCode = cmbPosCode.getSelectedItem().toString();
	    }
	    sb.setLength(0);
	    sb.append("SELECT distinct(a.strItemCode),a.strItemName,a.strPosCode,b.strPosName "
		    + ",e.strGroupName,d.strSubGroupName "
		    + " FROM tblmenuitempricingdtl a  "
		    + " left outer join tblposmaster b on (a.strPosCode=b.strPosCode or a.strPosCode='All') "
		    + "join tblitemmaster c on a.strItemCode=c.strItemCode "
		    + "join tblsubgrouphd d on c.strSubGroupCode=d.strSubGroupCode "
		    + "join tblgrouphd e on d.strGroupCode=e.strGroupCode "
		    + " where a.strItemCode NOT IN(SELECT c.strItemCode FROM tblposwiseitemwiseincentives c   ) ");
	    //conditions append
	    if (!cmbPosCode.getSelectedItem().toString().equals("All"))
	    {
		sb.append("and (a.strPOSCode='").append(posCode).append("' or a.strPosCode='All' ) ");
	    }
	    if (!strSearchItemCode.equals(""))
	    {
		sb.append("and a.strItemCode='").append(strSearchItemCode).append("' ");
	    }

	    sb.append(" order by a.strItemCode,e.strGroupName,d.strSubGroupName,b.strPosName  ");

	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}

	System.out.println("sb==== " + sb);

	return sb;
    }

    /**
     * This method is used to save data
     */
    private void funSave()
    {
	try
	{
	    String posCode = mapPOSName.get(cmbPosCode.getSelectedItem().toString());
	    int cnt = 0;
	    int ch = JOptionPane.showConfirmDialog(new JPanel(), "Do you want to Update All Item?", "Confirmation", JOptionPane.YES_NO_OPTION);
	    if (ch == JOptionPane.YES_OPTION)
	    {

		if (tblPoswiseItemIncentiveDtl.getRowCount() > 0 && tblPoswiseItemIncentiveDtl.getRowCount() == 1)
		{

		    int row = 0;

		    String deleteQuery = " delete from tblposwiseitemwiseincentives where strPOSCode='" + tblPoswiseItemIncentiveDtl.getValueAt(row, 7) + "'  and strItemCode='" + tblPoswiseItemIncentiveDtl.getValueAt(row, 0) + "' ";
		    clsGlobalVarClass.dbMysql.execute(deleteQuery);

		    String insertQuery = "insert into tblposwiseitemwiseincentives (strPOSCode,strItemCode,strItemName,strIncentiveType,dblIncentiveValue,strClientCode,strDataPostFlag,dteDateCreated,dteDateEdited) values ";

		    insertQuery += "('" + tblPoswiseItemIncentiveDtl.getValueAt(row, 7) + "', '" + tblPoswiseItemIncentiveDtl.getValueAt(row, 0) + "','" + tblPoswiseItemIncentiveDtl.getValueAt(row, 1) + "','" + tblPoswiseItemIncentiveDtl.getValueAt(row, 5) + "','" + tblPoswiseItemIncentiveDtl.getValueAt(row, 6) + "','" + clsGlobalVarClass.gClientCode + "','N','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "')";
		    System.out.println("insertQuery=" + insertQuery);

		    clsGlobalVarClass.dbMysql.execute(insertQuery);

		    String sql = "update tblmasteroperationstatus set dteDateEdited='" + clsGlobalVarClass.getCurrentDateTime() + "' "
			    + " where strTableName='PosWiseItemWiseIncentive' and strClientCode='" + clsGlobalVarClass.gClientCode + "'";
		    clsGlobalVarClass.dbMysql.execute(sql);

		    new frmOkPopUp(this, "Entry added Successfully", "Successfull", 3).setVisible(true);

		    funResetFields();

		}
		else
		{
		    if (!cmbPosCode.getSelectedItem().toString().equals("All"))
		    {
			String deleteQuery = " delete from tblposwiseitemwiseincentives where strPOSCode='" + posCode + "' ";
			clsGlobalVarClass.dbMysql.execute(deleteQuery);
		    }
		    else
		    {
			String deleteQuery = " truncate table tblposwiseitemwiseincentives  ";
			clsGlobalVarClass.dbMysql.execute(deleteQuery);
		    }

		    if (tblPoswiseItemIncentiveDtl.getRowCount() > 0)
		    {
			String insertQuery = "insert into tblposwiseitemwiseincentives (strPOSCode,strItemCode,strItemName,strIncentiveType,dblIncentiveValue,strClientCode,strDataPostFlag,dteDateCreated,dteDateEdited) values ";
			for (int row = 0; row < tblPoswiseItemIncentiveDtl.getRowCount(); row++)
			{
			    if (cnt == 0)
			    {
				insertQuery += "('" + tblPoswiseItemIncentiveDtl.getValueAt(row, 7) + "', '" + tblPoswiseItemIncentiveDtl.getValueAt(row, 0) + "','" + tblPoswiseItemIncentiveDtl.getValueAt(row, 1) + "','" + tblPoswiseItemIncentiveDtl.getValueAt(row, 5) + "','" + tblPoswiseItemIncentiveDtl.getValueAt(row, 6) + "','" + clsGlobalVarClass.gClientCode + "','N','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "')";
			    }
			    else
			    {
				insertQuery += ",('" + tblPoswiseItemIncentiveDtl.getValueAt(row, 7) + "','" + tblPoswiseItemIncentiveDtl.getValueAt(row, 0) + "', '" + tblPoswiseItemIncentiveDtl.getValueAt(row, 1) + "', '" + tblPoswiseItemIncentiveDtl.getValueAt(row, 5) + "','" + tblPoswiseItemIncentiveDtl.getValueAt(row, 6) + "','" + clsGlobalVarClass.gClientCode + "','N','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "')";
			    }
			    cnt++;
			}
			System.out.println("insertQuery=" + insertQuery);
			if (cnt > 0)
			{
			    clsGlobalVarClass.dbMysql.execute(insertQuery);
			}

			String sql = "update tblmasteroperationstatus set dteDateEdited='" + clsGlobalVarClass.getCurrentDateTime() + "' "
				+ " where strTableName='PosWiseItemWiseIncentive' and strClientCode='" + clsGlobalVarClass.gClientCode + "'";
			clsGlobalVarClass.dbMysql.execute(sql);

			new frmOkPopUp(this, "Entry added Successfully", "Successfull", 3).setVisible(true);
			funResetFields();
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        jComboBox1 = new javax.swing.JComboBox();
        panelHeader = new javax.swing.JPanel();
        lblModuleName = new javax.swing.JLabel();
        lblProductName = new javax.swing.JLabel();
        filler4 = new javax.swing.Box.Filler(new java.awt.Dimension(20, 0), new java.awt.Dimension(20, 0), new java.awt.Dimension(20, 32767));
        lblformName = new javax.swing.JLabel();
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
        panelBody = new javax.swing.JPanel();
        lblPosCode = new javax.swing.JLabel();
        btnExecute = new javax.swing.JButton();
        btnExit = new javax.swing.JButton();
        cmbPosCode = new javax.swing.JComboBox();
        separator1 = new javax.swing.JSeparator();
        srollPane = new javax.swing.JScrollPane();
        tblPoswiseItemIncentiveDtl = new javax.swing.JTable();
        btnReset = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        txtItemCode = new javax.swing.JTextField();
        lblPosCode1 = new javax.swing.JLabel();
        btnExport = new javax.swing.JButton();

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Per", "Amt", " " }));

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

        lblProductName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblProductName.setForeground(new java.awt.Color(255, 255, 255));
        lblProductName.setText("SPOS -");
        panelHeader.add(lblProductName);
        panelHeader.add(filler4);

        lblformName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblformName.setForeground(new java.awt.Color(255, 255, 255));
        lblformName.setText("-POSWise Item Incentives");
        lblformName.setMaximumSize(new java.awt.Dimension(170, 17));
        lblformName.setMinimumSize(new java.awt.Dimension(170, 17));
        lblformName.setPreferredSize(new java.awt.Dimension(170, 17));
        panelHeader.add(lblformName);
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

        panelLayout.setLayout(new java.awt.GridBagLayout());

        panelBody.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        panelBody.setMinimumSize(new java.awt.Dimension(800, 570));
        panelBody.setOpaque(false);
        panelBody.setPreferredSize(new java.awt.Dimension(800, 570));

        lblPosCode.setText("POS Code");

        btnExecute.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        btnExecute.setForeground(new java.awt.Color(251, 246, 246));
        btnExecute.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnExecute.setText("Execute");
        btnExecute.setToolTipText("Execute");
        btnExecute.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnExecute.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnExecute.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnExecuteActionPerformed(evt);
            }
        });
        btnExecute.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                btnExecuteKeyPressed(evt);
            }
        });

        btnExit.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
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

        cmbPosCode.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbPosCodeKeyPressed(evt);
            }
        });

        tblPoswiseItemIncentiveDtl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Code", " Name", "Group", "Sub Group", "POS Name", "Incentive Type", "Incentive Value", "POSCode"
            }
        )
        {
            boolean[] canEdit = new boolean []
            {
                false, false, false, false, false, true, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return canEdit [columnIndex];
            }
        });
        tblPoswiseItemIncentiveDtl.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblPoswiseItemIncentiveDtl.setRowHeight(25);
        tblPoswiseItemIncentiveDtl.getTableHeader().setReorderingAllowed(false);
        tblPoswiseItemIncentiveDtl.addFocusListener(new java.awt.event.FocusAdapter()
        {
            public void focusGained(java.awt.event.FocusEvent evt)
            {
                tblPoswiseItemIncentiveDtlFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt)
            {
                tblPoswiseItemIncentiveDtlFocusLost(evt);
            }
        });
        tblPoswiseItemIncentiveDtl.addInputMethodListener(new java.awt.event.InputMethodListener()
        {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt)
            {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt)
            {
                tblPoswiseItemIncentiveDtlInputMethodTextChanged(evt);
            }
        });
        tblPoswiseItemIncentiveDtl.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                tblPoswiseItemIncentiveDtlKeyPressed(evt);
            }
        });
        srollPane.setViewportView(tblPoswiseItemIncentiveDtl);
        if (tblPoswiseItemIncentiveDtl.getColumnModel().getColumnCount() > 0)
        {
            tblPoswiseItemIncentiveDtl.getColumnModel().getColumn(0).setResizable(false);
            tblPoswiseItemIncentiveDtl.getColumnModel().getColumn(0).setPreferredWidth(70);
            tblPoswiseItemIncentiveDtl.getColumnModel().getColumn(1).setResizable(false);
            tblPoswiseItemIncentiveDtl.getColumnModel().getColumn(1).setPreferredWidth(300);
            tblPoswiseItemIncentiveDtl.getColumnModel().getColumn(2).setResizable(false);
            tblPoswiseItemIncentiveDtl.getColumnModel().getColumn(2).setPreferredWidth(100);
            tblPoswiseItemIncentiveDtl.getColumnModel().getColumn(3).setResizable(false);
            tblPoswiseItemIncentiveDtl.getColumnModel().getColumn(3).setPreferredWidth(100);
            tblPoswiseItemIncentiveDtl.getColumnModel().getColumn(4).setResizable(false);
            tblPoswiseItemIncentiveDtl.getColumnModel().getColumn(4).setPreferredWidth(100);
            tblPoswiseItemIncentiveDtl.getColumnModel().getColumn(5).setResizable(false);
            tblPoswiseItemIncentiveDtl.getColumnModel().getColumn(5).setPreferredWidth(100);
            tblPoswiseItemIncentiveDtl.getColumnModel().getColumn(5).setCellEditor(new DefaultCellEditor(jComboBox1));
            tblPoswiseItemIncentiveDtl.getColumnModel().getColumn(6).setResizable(false);
            tblPoswiseItemIncentiveDtl.getColumnModel().getColumn(6).setPreferredWidth(100);
            tblPoswiseItemIncentiveDtl.getColumnModel().getColumn(7).setResizable(false);
            tblPoswiseItemIncentiveDtl.getColumnModel().getColumn(7).setPreferredWidth(0);
        }

        btnReset.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        btnReset.setForeground(new java.awt.Color(250, 243, 243));
        btnReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnReset.setText("Reset");
        btnReset.setToolTipText("Reset All Fields");
        btnReset.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnReset.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnReset.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnResetActionPerformed(evt);
            }
        });

        btnSave.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        btnSave.setForeground(new java.awt.Color(251, 246, 246));
        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnSave.setText("Save");
        btnSave.setToolTipText("Close Menu Item Pricing");
        btnSave.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSave.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnSave.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnSaveMouseClicked(evt);
            }
        });
        btnSave.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnSaveActionPerformed(evt);
            }
        });
        btnSave.addVetoableChangeListener(new java.beans.VetoableChangeListener()
        {
            public void vetoableChange(java.beans.PropertyChangeEvent evt)throws java.beans.PropertyVetoException
            {
                btnSaveVetoableChange(evt);
            }
        });

        txtItemCode.setEditable(false);
        txtItemCode.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtItemCodeMouseClicked(evt);
            }
        });
        txtItemCode.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtItemCodeKeyPressed(evt);
            }
        });

        lblPosCode1.setText("Item");

        btnExport.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        btnExport.setForeground(new java.awt.Color(251, 246, 246));
        btnExport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnExport.setText("Export");
        btnExport.setToolTipText("Execute");
        btnExport.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnExport.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnExport.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnExportActionPerformed(evt);
            }
        });
        btnExport.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                btnExportKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout panelBodyLayout = new javax.swing.GroupLayout(panelBody);
        panelBody.setLayout(panelBodyLayout);
        panelBodyLayout.setHorizontalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(separator1)
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblPosCode)
                    .addComponent(lblPosCode1, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtItemCode)
                    .addComponent(cmbPosCode, 0, 179, Short.MAX_VALUE))
                .addGap(26, 26, 26)
                .addComponent(btnExecute, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnExport, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnExit, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(141, 141, 141))
            .addComponent(srollPane)
        );
        panelBodyLayout.setVerticalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblPosCode, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cmbPosCode, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                    .addComponent(btnExecute, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtItemCode, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnExport, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(lblPosCode1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnExit, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(separator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(srollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 463, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        panelLayout.add(panelBody, new java.awt.GridBagConstraints());

        getContentPane().add(panelLayout, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
	// TODO add your handling code here:
	clsGlobalVarClass.hmActiveForms.remove("POSWise Item Incentive");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
	// TODO add your handling code here:
	clsGlobalVarClass.hmActiveForms.remove("POSWise Item Incentive");
    }//GEN-LAST:event_formWindowClosing

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
	funResetClick();
    }//GEN-LAST:event_btnResetActionPerformed

    private void tblPoswiseItemIncentiveDtlKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblPoswiseItemIncentiveDtlKeyPressed
	// TODO add your handling code here:
	// System.out.println("key code="+evt.getKeyCode()+"\tChar="+evt.getKeyChar()+"\textendes key code"+evt.getExtendedKeyCode());

    }//GEN-LAST:event_tblPoswiseItemIncentiveDtlKeyPressed

    private void tblPoswiseItemIncentiveDtlInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_tblPoswiseItemIncentiveDtlInputMethodTextChanged
	// TODO add your handling code here:
    }//GEN-LAST:event_tblPoswiseItemIncentiveDtlInputMethodTextChanged

    private void tblPoswiseItemIncentiveDtlFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tblPoswiseItemIncentiveDtlFocusLost

    }//GEN-LAST:event_tblPoswiseItemIncentiveDtlFocusLost

    private void tblPoswiseItemIncentiveDtlFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tblPoswiseItemIncentiveDtlFocusGained

	/*
        if (tblPoswiseItemIncentiveDtl.getSelectedColumn() == 2)
        {
            tblPoswiseItemIncentiveDtl.setValueAt(tblPoswiseItemIncentiveDtl.getValueAt(tblPoswiseItemIncentiveDtl.getSelectedRow(), 2), tblPoswiseItemIncentiveDtl.getSelectedRow(), 3);
            tblPoswiseItemIncentiveDtl.setValueAt(tblPoswiseItemIncentiveDtl.getValueAt(tblPoswiseItemIncentiveDtl.getSelectedRow(), 2), tblPoswiseItemIncentiveDtl.getSelectedRow(), 4);
            tblPoswiseItemIncentiveDtl.setValueAt(tblPoswiseItemIncentiveDtl.getValueAt(tblPoswiseItemIncentiveDtl.getSelectedRow(), 2), tblPoswiseItemIncentiveDtl.getSelectedRow(), 5);
            tblPoswiseItemIncentiveDtl.setValueAt(tblPoswiseItemIncentiveDtl.getValueAt(tblPoswiseItemIncentiveDtl.getSelectedRow(), 2), tblPoswiseItemIncentiveDtl.getSelectedRow(), 6);
            tblPoswiseItemIncentiveDtl.setValueAt(tblPoswiseItemIncentiveDtl.getValueAt(tblPoswiseItemIncentiveDtl.getSelectedRow(), 2), tblPoswiseItemIncentiveDtl.getSelectedRow(), 7);
            tblPoswiseItemIncentiveDtl.setValueAt(tblPoswiseItemIncentiveDtl.getValueAt(tblPoswiseItemIncentiveDtl.getSelectedRow(), 2), tblPoswiseItemIncentiveDtl.getSelectedRow(), 8);
        }*/
    }//GEN-LAST:event_tblPoswiseItemIncentiveDtlFocusGained

    private void cmbPosCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbPosCodeKeyPressed
	// TODO add your handling code here:

    }//GEN-LAST:event_cmbPosCodeKeyPressed

    private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitActionPerformed
	clsGlobalVarClass.hmActiveForms.remove("Item Wise Incentives");
	funExitClick();
    }//GEN-LAST:event_btnExitActionPerformed

    private void btnExitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnExitMouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_btnExitMouseClicked

    private void btnExecuteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnExecuteKeyPressed
	// TODO add your handling code here:

    }//GEN-LAST:event_btnExecuteKeyPressed

    private void btnExecuteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExecuteActionPerformed
	funExecuteClick();
    }//GEN-LAST:event_btnExecuteActionPerformed

    private void btnSaveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSaveMouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_btnSaveMouseClicked

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
	// TODO add your handling code here:
	funSave();
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnSaveVetoableChange(java.beans.PropertyChangeEvent evt)throws java.beans.PropertyVetoException {//GEN-FIRST:event_btnSaveVetoableChange
	// TODO add your handling code here:
    }//GEN-LAST:event_btnSaveVetoableChange

    private void txtItemCodeMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtItemCodeMouseClicked
    {//GEN-HEADEREND:event_txtItemCodeMouseClicked
	// TODO add your handling code here:
	funSelectItemCode();
    }//GEN-LAST:event_txtItemCodeMouseClicked

    private void txtItemCodeKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtItemCodeKeyPressed
    {//GEN-HEADEREND:event_txtItemCodeKeyPressed
	// TODO add your handling code here:
	//open item help on click of '?' or '/' key
	if (evt.getKeyChar() == '?' || evt.getKeyChar() == '/')
	{
	    funSelectItemCode();
	}


    }//GEN-LAST:event_txtItemCodeKeyPressed

    private void btnExportActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnExportActionPerformed
    {//GEN-HEADEREND:event_btnExportActionPerformed
	funExportButtonClicked();
    }//GEN-LAST:event_btnExportActionPerformed

    private void btnExportKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_btnExportKeyPressed
    {//GEN-HEADEREND:event_btnExportKeyPressed
	// TODO add your handling code here:
    }//GEN-LAST:event_btnExportKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnExecute;
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnExport;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnSave;
    private javax.swing.JComboBox cmbPosCode;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblPosCode;
    private javax.swing.JLabel lblPosCode1;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblformName;
    private javax.swing.JPanel panelBody;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelLayout;
    private javax.swing.JSeparator separator1;
    private javax.swing.JScrollPane srollPane;
    private javax.swing.JTable tblPoswiseItemIncentiveDtl;
    private javax.swing.JTextField txtItemCode;
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
	    java.util.logging.Logger.getLogger(frmBulkMenuItemPricing.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	}
	catch (InstantiationException ex)
	{
	    java.util.logging.Logger.getLogger(frmBulkMenuItemPricing.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	}
	catch (IllegalAccessException ex)
	{
	    java.util.logging.Logger.getLogger(frmBulkMenuItemPricing.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	}
	catch (javax.swing.UnsupportedLookAndFeelException ex)
	{
	    java.util.logging.Logger.getLogger(frmBulkMenuItemPricing.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	}
	dispose();
    }

    private void funResetFields()
    {
	DefaultTableModel dm = (DefaultTableModel) tblPoswiseItemIncentiveDtl.getModel();
	dm.setRowCount(0);
	cmbPosCode.setSelectedIndex(0);
	sb.setLength(0);
    }

    /**
     * This method is used to reset
     */
    private void funResetClick()
    {
	funResetFields();
    }

    private void funExportButtonClicked()
    {
	try
	{
	    if (tblPoswiseItemIncentiveDtl.getRowCount() <= 0)
	    {
		new frmOkPopUp(this, "No data found.", "Warning", 3).setVisible(true);
		return;
	    }
	    else
	    {
		funExportTheData();
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funExportTheData()
    {
	String filePath = System.getProperty("user.dir");
	File file = new File(filePath + File.separator + "Reports" + File.separator + "Item Incentive.xls");
	try
	{
	    WritableWorkbook workbook1 = Workbook.createWorkbook(file);
	    WritableSheet sheet1 = workbook1.createSheet("Item Incentive", 0);

	    WritableFont cellFont = new WritableFont(WritableFont.ARIAL, 8);
	    // cellFont.setBoldStyle(WritableFont.BOLD);

	    WritableCellFormat cellFormat = new WritableCellFormat(cellFont);

	    WritableFont headerCellFont = new WritableFont(WritableFont.ARIAL, 8);
	    headerCellFont.setBoldStyle(WritableFont.BOLD);

	    WritableCellFormat headerCell = new WritableCellFormat(headerCellFont);

	    NumberFormat decimalNo = new NumberFormat("0.00");
	    WritableCellFormat numberCellFormat = new WritableCellFormat(decimalNo);

	    Label header1 = new Label(0, 0, "ITEM CODE", headerCell);
	    sheet1.addCell(header1);
	    sheet1.setColumnView(0, 10);

	    Label header2 = new Label(1, 0, "ITEM NAME", headerCell);
	    sheet1.addCell(header2);
	    sheet1.setColumnView(1, 40);

	    Label header3 = new Label(2, 0, "GROUP", headerCell);
	    sheet1.addCell(header3);
	    sheet1.setColumnView(2, 15);

	    Label header4 = new Label(3, 0, "SUB GROUP", headerCell);
	    sheet1.addCell(header4);
	    sheet1.setColumnView(3, 15);

	    Label header5 = new Label(4, 0, "POS", headerCell);
	    sheet1.addCell(header5);
	    sheet1.setColumnView(4, 15);

	    Label header6 = new Label(5, 0, "INC. TYPE", headerCell);
	    sheet1.addCell(header6);
	    sheet1.setColumnView(5, 15);

	    Label header7 = new Label(6, 0, "INCENTIVE", headerCell);
	    sheet1.addCell(header7);
	    sheet1.setColumnView(6, 10);

	    
	    int dataRow=1,dataCol=0;
	    for (int row = 0; row < tblPoswiseItemIncentiveDtl.getRowCount(); row++)
	    {
		for (int col = 0; col < tblPoswiseItemIncentiveDtl.getColumnCount() - 1; col++)
		{
		    if (col == 6)
		    {
			Number number = new Number(dataCol, dataRow, Double.parseDouble(tblPoswiseItemIncentiveDtl.getValueAt(row, col).toString()), numberCellFormat);
			sheet1.addCell(number);
		    }
		    else
		    {
			Label cell = new Label(dataCol, dataRow, tblPoswiseItemIncentiveDtl.getValueAt(row, col).toString(), cellFormat);
			sheet1.addCell(cell);
		    }
		    
		    dataCol++;
		}
		
		dataRow++;
		dataCol=0;
	    }

	    workbook1.write();
	    workbook1.close();

	    Desktop dt = Desktop.getDesktop();
	    dt.open(file);

	}
	catch (Exception ex)
	{
	    JOptionPane.showMessageDialog(null, ex.getMessage());
	    ex.printStackTrace();
	}
    }

    class MyTableCellEditor extends AbstractCellEditor implements TableCellEditor
    {

	JComponent component = new JTextField();

	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected,
		int rowIndex, int vColIndex)
	{

	    ((JTextField) component).setText((String) value);

	    return component;
	}

	public Object getCellEditorValue()
	{
	    return ((JTextField) component).getText();
	}
    }

    private void funSelectItemCode()
    {
	try
	{
	    objUtility.funCallForSearchForm("MenuItem");
	    new frmSearchFormDialog(this, true).setVisible(true);
	    if (clsGlobalVarClass.gSearchItemClicked)
	    {
		Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
		// funSetData(data);
		strSearchItemCode = clsGlobalVarClass.gSearchedItem;
		funExecuteClick();
		strSearchItemCode = "";
		clsGlobalVarClass.gSearchItemClicked = false;

	    }
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }
}
