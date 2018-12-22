/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSMaster.view;

import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsUtility;
import com.POSGlobal.view.frmAlfaNumericKeyBoard;
import com.POSGlobal.view.frmOkCancelPopUp;
import com.POSGlobal.view.frmOkPopUp;
import com.POSGlobal.view.frmSearchFormDialog;
import com.POSPrinting.Utility.clsPrintingUtility;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class frmCostCenterMaster extends javax.swing.JFrame
{

    private ResultSet countSet, countSet1;
    private String selectQuery, insertQuery;
    private String updateQuery, strCode, code, printerName, sql;
    private String gpCode = "CC00000";
    boolean flag;
    private java.util.Vector vPrinterNames;
    private String testPrinter;
    private HashMap<String, String> hmCostCenterParams;
    private clsUtility objUtility;

    /**
     * This method is used to initialize CostCenterMaster
     */
    public frmCostCenterMaster()
    {
        initComponents();
        try
        {
            objUtility = new clsUtility();
            hmCostCenterParams = new HashMap<String, String>();
            Timer timer = new Timer(500, new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    Date date1 = new Date();
                    String newstr = String.format("%tr", date1);
                    String dateAndTime = clsGlobalVarClass.gPOSDateToDisplay + " " + newstr;
                    lblDate.setText(dateAndTime);
                }
            });
            timer.setRepeats(true);
            timer.setCoalesce(true);
            timer.setInitialDelay(0);
            timer.start();
            lblUserCode.setText(clsGlobalVarClass.gUserCode);
            lblPosName.setText(clsGlobalVarClass.gPOSName);
            lblDate.setText(clsGlobalVarClass.gPOSDateToDisplay);
            lblModuleName.setText(clsGlobalVarClass.gSelectedModule);
            txtCostCode.requestFocus();
            clsUtility objUtility = new clsUtility();
            vPrinterNames = objUtility.funGetPrinterNames();

            for (int cntPrinters = 0; cntPrinters < vPrinterNames.size(); cntPrinters++)
            {
                cmbPrimaryPrinters.addItem(vPrinterNames.elementAt(cntPrinters).toString());
                cmbSecondaryPrinters.addItem(vPrinterNames.elementAt(cntPrinters).toString());
            }
            funSetShortCutKeys();
            btnTestPrinter1.setVisible(false);
            btnTestPrinter2.setVisible(false);

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }

    }

    private void funSetShortCutKeys()
    {
        btnCancel.setMnemonic('c');
        btnNew.setMnemonic('s');
        btnReset.setMnemonic('r');

    }

    /**
     * This method is used to set data
     *
     * @param data
     */
    private void funSetCostCenterData(Object[] data) throws Exception
    {
        sql = "select a.strCostCenterCode,a.strCostCenterName ,ifnull(b.strPrimaryPrinterPort,'')"
                + ", ifnull(b.strSecondaryPrinterPort,''), ifnull(b.strPrintOnBothPrintersYN,'N'),strLabelOnKOT,intPrimaryPrinterNoOfCopies,intSecondaryPrinterNoOfCopies "
                + " from tblcostcentermaster  a "
                + " left outer join tblprintersetup b on a.strCostCenterCode=b.strCostCenterCode "
                + " where a.strCostCenterCode='" + clsGlobalVarClass.gSearchedItem + "'";
        //System.out.println(sql);
        ResultSet rsCostCenter = clsGlobalVarClass.dbMysql.executeResultSet(sql);
        rsCostCenter.next();
        txtCostCode.setText(rsCostCenter.getString(1));//code
        txtCostName.setText(rsCostCenter.getString(2));//name
        btnNew.setMnemonic('u');
        String primaryPrinter = rsCostCenter.getString(3);//primary
        primaryPrinter = primaryPrinter.replaceAll("#", "\\\\");
        String secondaryPrinter = rsCostCenter.getString(4);//secondary
        secondaryPrinter = secondaryPrinter.replaceAll("#", "\\\\");
        cmbPrimaryPrinters.setSelectedItem(primaryPrinter);
        txtPrimaryPrinterName.setText(primaryPrinter);
        cmbSecondaryPrinters.setSelectedItem(secondaryPrinter);
        txtSecondaryPrinterName.setText(secondaryPrinter);
        String printOnBothPrinters = rsCostCenter.getString(5);//printOnBothPrintersYN
        if (printOnBothPrinters.equalsIgnoreCase("Y"))
        {
            chkBoxPrintOnBothPrinters.setSelected(true);
        }
        else
        {
            chkBoxPrintOnBothPrinters.setSelected(false);
        }
        txtLabelOnKOT.setText(rsCostCenter.getString(6));
	txtPrimaryPinterNoOfCopies.setText(rsCostCenter.getString(7));
	txtSecondaryPrinterNoOfCopies1.setText(rsCostCenter.getString(8));
	
        rsCostCenter.close();

        txtCostCode.requestFocus();

        if (!txtPrimaryPrinterName.getText().isEmpty())
        {
            btnTestPrinter1.setVisible(true);
        }
        else
        {
            btnTestPrinter1.setVisible(false);
        }
        if (!txtSecondaryPrinterName.getText().isEmpty())
        {
            btnTestPrinter2.setVisible(true);
        }
        else
        {
            btnTestPrinter2.setVisible(false);
        }
    }

    /**
     * This method is used to save cost center
     */
    private void funSaveCostCenter()
    {
        try
        {
            selectQuery = "select count(*) from tblcostcentermaster";
            countSet1 = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery);
            countSet1.next();
            int cn = countSet1.getInt(1);
            countSet1.close();
            if (cn > 0)
            {
                selectQuery = "select max(strCostCenterCode) from tblcostcentermaster";
                countSet = clsGlobalVarClass.dbMysql.executeResultSet(selectQuery);
                countSet.next();
                code = countSet.getString(1);
                StringBuilder sb = new StringBuilder(code);
                String ss = sb.delete(0, 1).toString();
                for (int i = 0; i < ss.length(); i++)
                {
                    if (ss.charAt(i) != '0')
                    {
                        strCode = ss.substring(i, ss.length());
                        break;
                    }
                }
                int intCode = Integer.parseInt(strCode);
                intCode++;
                if (intCode < 10)
                {
                    gpCode = "C0" + intCode;
                }
                else
                {
                    gpCode = "C" + intCode;
                }
            }
            else
            {
                code = "0";
                gpCode = "C01";
            }
            clsUtility obj = new clsUtility();
            String itemItem = txtCostName.getText().trim();
            String code = "";
            if (clsGlobalVarClass.funCheckItemName("tblcostcentermaster", "strCostCenterName", "strCodeCenterCode", itemItem, code, "save", ""))
            {
                new frmOkPopUp(this, "This Cost Center Name is Already Exist", "Error", 0).setVisible(true);
                txtCostName.requestFocus();
            }
            else if (!clsGlobalVarClass.validateEmpty(txtCostName.getText()))
            {
                new frmOkPopUp(this, "Please Enter Cost Center Name", "Error", 0).setVisible(true);
                txtCostCode.requestFocus();
            }
            else if (!obj.funCheckLength(txtCostName.getText(), 20))
            {
                new frmOkPopUp(this, "Cost Center Name length must be less than 20", "Error", 0).setVisible(true);
                txtCostName.requestFocus();
            }
            else
            {

                String primaryPrinterName = txtPrimaryPrinterName.getText().toString().replaceAll("\\\\", "#");
                String secondaryPrinterName = txtSecondaryPrinterName.getText().trim().replaceAll("\\\\", "#");
                String printOnBothPrinters = "N";
                if (chkBoxPrintOnBothPrinters.isSelected())
                {
                    printOnBothPrinters = "Y";
                }
                else
                {
                    printOnBothPrinters = "N";
                }

                txtCostCode.setText(gpCode);
                insertQuery = "insert into tblcostcentermaster "
                        + "(strCostCenterCode,strCostCenterName,strPrinterPort,strSecondaryPrinterPort,"
                        + "strUserCreated,strUserEdited,dteDateCreated,dteDateEdited,strClientCode,strDataPostFlag,strPrintOnBothPrinters"
                        + ",strLabelOnKOT,intPrimaryPrinterNoOfCopies,intSecondaryPrinterNoOfCopies) "
                        + "values('" + txtCostCode.getText() + "','" + txtCostName.getText() + "'"
                        + ",'" + primaryPrinterName + "','" + secondaryPrinterName + "','" + clsGlobalVarClass.gUserCode + "'"
                        + ",'" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.getCurrentDateTime() + "'"
                        + ",'" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.gClientCode + "','N'"
                        + ",'" + printOnBothPrinters + "','" + txtLabelOnKOT.getText().trim() + "','"+txtPrimaryPinterNoOfCopies.getText().trim()+"','"+txtSecondaryPrinterNoOfCopies1.getText().trim()+"')";
                //System.out.println(insertQuery);
                int exc = clsGlobalVarClass.dbMysql.execute(insertQuery);

                insertQuery = " insert into tblprintersetup values"
                        + " ('" + txtCostCode.getText() + "','" + txtCostName.getText() + "','" + primaryPrinterName + "','" + secondaryPrinterName + "','" + printOnBothPrinters + "'"
                        + "  ,'" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.gClientCode + "','N')";
                clsGlobalVarClass.dbMysql.execute(insertQuery);

                if (exc > 0)
                {
                    sql = "update tblmasteroperationstatus set dteDateEdited='" + clsGlobalVarClass.getCurrentDateTime() + "' "
                            + " where strTableName='CostCenter' ";
                    clsGlobalVarClass.dbMysql.execute(sql);
                    new frmOkPopUp(this, "Entry added Successfully", "Successfull", 3).setVisible(true);
                    funResetField();
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
     * This method is used to update cost center
     */
    private void funUpdateCostCenter()
    {
        try
        {
            clsUtility obj = new clsUtility();
            String itemItem = txtCostName.getText().trim();
            String costCenterCode = txtCostCode.getText().trim();

            if (clsGlobalVarClass.funCheckItemName("tblcostcentermaster", "strCostCenterName", "strCostCenterCode", itemItem, costCenterCode, "update", ""))
            {
                new frmOkPopUp(this, "This Cost Center Name is Already Exist", "Error", 0).setVisible(true);
                txtCostName.requestFocus();
            }
            else if (!clsGlobalVarClass.validateEmpty(txtCostName.getText()))
            {
                new frmOkPopUp(this, "Please Enter Cost Center Name", "Error", 0).setVisible(true);
                txtCostCode.requestFocus();
            }
            else if (!obj.funCheckLength(txtCostName.getText(), 20))
            {
                new frmOkPopUp(this, "Cost Center Name length must be less than 20", "Error", 0).setVisible(true);
                txtCostName.requestFocus();
            }
            else
            {
                printerName = txtPrimaryPrinterName.getText().toString().replaceAll("\\\\", "#");
                String secondaryPrinterName = txtSecondaryPrinterName.getText().trim().replaceAll("\\\\", "#");

                String printOnBothPrinters = "N";
                if (chkBoxPrintOnBothPrinters.isSelected())
                {
                    printOnBothPrinters = "Y";
                }
                else
                {
                    printOnBothPrinters = "N";
                }

                updateQuery = "UPDATE tblcostcentermaster "
                        + "SET strCostCenterName = '" + txtCostName.getText() + "'"
                        + ",strPrinterPort='" + printerName + "',strSecondaryPrinterPort='" + secondaryPrinterName + "'"
                        + ",strUserEdited='" + clsGlobalVarClass.gUserCode + "'"
                        + ",dteDateEdited='" + clsGlobalVarClass.getCurrentDateTime() + "'"
                        + ",strDataPostFlag='N',strPrintOnBothPrinters='" + printOnBothPrinters + "' "
                        + ",strLabelOnKOT='" + txtLabelOnKOT.getText().trim() + "'"
			+ " ,intPrimaryPrinterNoOfCopies = '"+txtPrimaryPinterNoOfCopies.getText().trim()+"'"
			+ " ,intSecondaryPrinterNoOfCopies='"+txtSecondaryPrinterNoOfCopies1.getText().trim()+"' "
                        + " WHERE strCostCenterCode ='" + txtCostCode.getText() + "'";
                //System.out.println(updateQuery);
                int exc = clsGlobalVarClass.dbMysql.execute(updateQuery);

                clsGlobalVarClass.dbMysql.execute("delete from tblprintersetup where strcostcentercode='" + txtCostCode.getText() + "'");
                String primaryPrinterName = printerName;
                insertQuery = " insert into tblprintersetup values"
                        + " ('" + txtCostCode.getText() + "','" + txtCostName.getText() + "','" + primaryPrinterName + "','" + secondaryPrinterName + "','" + printOnBothPrinters + "'"
                        + "  ,'" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.gClientCode + "','N')";

                clsGlobalVarClass.dbMysql.execute(insertQuery);

                if (exc > 0)
                {
                    sql = "update tblmasteroperationstatus set dteDateEdited='" + clsGlobalVarClass.getCurrentDateTime() + "' "
                            + " where strTableName='CostCenter' ";
                    clsGlobalVarClass.dbMysql.execute(sql);
                    new frmOkPopUp(this, "Updated Successfully", "Successfull", 3).setVisible(true);
                    funResetField();
                }
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    private void funSelectCostCenter()
    {
        try
        {
            //Search for cost center to update
            //btnNew.setText("UPDATE");
            clsUtility obj = new clsUtility();
            obj.funCallForSearchForm("CostCenter");
            new frmSearchFormDialog(this, true).setVisible(true);
            if (clsGlobalVarClass.gSearchItemClicked)
            {
                btnNew.setText("UPDATE");
                btnNew.setMnemonic('u');
                Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
                funSetCostCenterData(data);
                clsGlobalVarClass.gSearchItemClicked = false;
            }
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    /**
     * This method is used to reset all fields
     */
    private void funResetField()
    {

        txtCostCode.requestFocus();
        btnNew.setText("SAVE");
        btnNew.setMnemonic('s');
        flag = false;
        txtCostCode.setText("");
        txtCostName.setText("");
        txtPrimaryPrinterName.setText("");
        txtSecondaryPrinterName.setText("");
        cmbPrimaryPrinters.setSelectedIndex(0);
        cmbSecondaryPrinters.setSelectedIndex(0);
        chkBoxPrintOnBothPrinters.setSelected(false);
        txtLabelOnKOT.setText("KOT");
	txtPrimaryPinterNoOfCopies.setText("1");
	txtSecondaryPrinterNoOfCopies1.setText("0");
    }

    private void funTestPrint(String printerName)
    {
        funCreateTempFolder();
        String filePath = System.getProperty("user.dir");
        String filename = (filePath + "/Temp/TestCCPrinter.txt");
        try
        {
            File file = new File(filename);
            funCreateTestTextFile(file);

            clsPrintingUtility objPrintingUtility = new clsPrintingUtility();

            objPrintingUtility.funShowTextFile(file, "", "");

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
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error Code - TFG 01", JOptionPane.ERROR_MESSAGE);
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
        lblfromName = new javax.swing.JLabel();
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
        };  ;
        panelBody = new javax.swing.JPanel();
        lblFormName = new javax.swing.JLabel();
        lblCostCode = new javax.swing.JLabel();
        txtCostCode = new javax.swing.JTextField();
        txtCostName = new javax.swing.JTextField();
        lblGroupName2 = new javax.swing.JLabel();
        lblPrinterPort = new javax.swing.JLabel();
        cmbPrimaryPrinters = new javax.swing.JComboBox();
        btnCancel = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        btnNew = new javax.swing.JButton();
        txtPrimaryPrinterName = new javax.swing.JTextField();
        lblPrinterPort1 = new javax.swing.JLabel();
        cmbSecondaryPrinters = new javax.swing.JComboBox();
        txtSecondaryPrinterName = new javax.swing.JTextField();
        btnTestPrinter2 = new javax.swing.JButton();
        btnTestPrinter1 = new javax.swing.JButton();
        lblPrintOnBothPrinters = new javax.swing.JLabel();
        chkBoxPrintOnBothPrinters = new javax.swing.JCheckBox();
        lblLabelOnKOT = new javax.swing.JLabel();
        txtLabelOnKOT = new javax.swing.JTextField();
        lblNoOfCopies = new javax.swing.JLabel();
        txtPrimaryPinterNoOfCopies = new javax.swing.JTextField();
        lblNoOfCopies1 = new javax.swing.JLabel();
        txtSecondaryPrinterNoOfCopies1 = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
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
        lblProductName.setText("SPOS - ");
        panelHeader.add(lblProductName);

        lblModuleName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblModuleName.setForeground(new java.awt.Color(255, 255, 255));
        panelHeader.add(lblModuleName);

        lblfromName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblfromName.setForeground(new java.awt.Color(255, 255, 255));
        lblfromName.setText("-Cost Center");
        panelHeader.add(lblfromName);
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

        panelLayout.setBackground(new java.awt.Color(255, 255, 255));
        panelLayout.setOpaque(false);
        panelLayout.setPreferredSize(new java.awt.Dimension(800, 559));
        panelLayout.setLayout(new java.awt.GridBagLayout());

        panelBody.setBackground(new java.awt.Color(255, 255, 255));
        panelBody.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        panelBody.setMinimumSize(new java.awt.Dimension(800, 570));
        panelBody.setOpaque(false);

        lblFormName.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        lblFormName.setForeground(new java.awt.Color(14, 7, 7));
        lblFormName.setText("Cost Center Master");

        lblCostCode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblCostCode.setText("Cost Center Code       :");

        txtCostCode.setEditable(false);
        txtCostCode.setBackground(new java.awt.Color(204, 204, 204));
        txtCostCode.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtCostCodeMouseClicked(evt);
            }
        });
        txtCostCode.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtCostCodeKeyPressed(evt);
            }
        });

        txtCostName.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtCostNameMouseClicked(evt);
            }
        });
        txtCostName.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtCostNameActionPerformed(evt);
            }
        });
        txtCostName.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtCostNameKeyPressed(evt);
            }
        });

        lblGroupName2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblGroupName2.setText("Cost Center Name      :");

        lblPrinterPort.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblPrinterPort.setText("Primary Printer           :");

        cmbPrimaryPrinters.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbPrimaryPrintersActionPerformed(evt);
            }
        });
        cmbPrimaryPrinters.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbPrimaryPrintersKeyPressed(evt);
            }
        });

        btnCancel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnCancel.setForeground(new java.awt.Color(255, 255, 255));
        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnCancel.setText("CLOSE");
        btnCancel.setToolTipText("Close Cost Center Master");
        btnCancel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCancel.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnCancel.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCancelMouseClicked(evt);
            }
        });
        btnCancel.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCancelActionPerformed(evt);
            }
        });

        btnReset.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnReset.setForeground(new java.awt.Color(255, 255, 255));
        btnReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnReset.setText("RESET");
        btnReset.setToolTipText("Reset All Fields");
        btnReset.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnReset.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnReset.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnResetMouseClicked(evt);
            }
        });
        btnReset.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnResetActionPerformed(evt);
            }
        });

        btnNew.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnNew.setForeground(new java.awt.Color(255, 255, 255));
        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnNew.setText("SAVE");
        btnNew.setToolTipText("Save Cost Center Master");
        btnNew.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNew.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnNew.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnNewMouseClicked(evt);
            }
        });
        btnNew.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnNewActionPerformed(evt);
            }
        });
        btnNew.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                btnNewKeyPressed(evt);
            }
        });

        txtPrimaryPrinterName.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtPrimaryPrinterNameMouseClicked(evt);
            }
        });
        txtPrimaryPrinterName.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtPrimaryPrinterNameActionPerformed(evt);
            }
        });
        txtPrimaryPrinterName.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtPrimaryPrinterNameKeyPressed(evt);
            }
        });

        lblPrinterPort1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblPrinterPort1.setText("Secondary Printer       :");

        cmbSecondaryPrinters.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbSecondaryPrintersActionPerformed(evt);
            }
        });
        cmbSecondaryPrinters.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbSecondaryPrintersKeyPressed(evt);
            }
        });

        txtSecondaryPrinterName.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtSecondaryPrinterNameMouseClicked(evt);
            }
        });
        txtSecondaryPrinterName.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtSecondaryPrinterNameKeyPressed(evt);
            }
        });

        btnTestPrinter2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnTestPrinter2.setForeground(new java.awt.Color(255, 255, 255));
        btnTestPrinter2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnTestPrinter2.setText("TEST");
        btnTestPrinter2.setToolTipText("Save Cost Center Master");
        btnTestPrinter2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTestPrinter2.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnTestPrinter2.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnTestPrinter2MouseClicked(evt);
            }
        });
        btnTestPrinter2.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnTestPrinter2ActionPerformed(evt);
            }
        });
        btnTestPrinter2.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                btnTestPrinter2KeyPressed(evt);
            }
        });

        btnTestPrinter1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnTestPrinter1.setForeground(new java.awt.Color(255, 255, 255));
        btnTestPrinter1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn1.png"))); // NOI18N
        btnTestPrinter1.setText("TEST");
        btnTestPrinter1.setToolTipText("Save Cost Center Master");
        btnTestPrinter1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTestPrinter1.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSMaster/images/imgCmnBtn2.png"))); // NOI18N
        btnTestPrinter1.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnTestPrinter1MouseClicked(evt);
            }
        });
        btnTestPrinter1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnTestPrinter1ActionPerformed(evt);
            }
        });
        btnTestPrinter1.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                btnTestPrinter1KeyPressed(evt);
            }
        });

        lblPrintOnBothPrinters.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblPrintOnBothPrinters.setText("Print On Both Printers :");

        chkBoxPrintOnBothPrinters.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                chkBoxPrintOnBothPrintersActionPerformed(evt);
            }
        });

        lblLabelOnKOT.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblLabelOnKOT.setText("Label On KOT      :");

        txtLabelOnKOT.setText("KOT");
        txtLabelOnKOT.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtLabelOnKOTMouseClicked(evt);
            }
        });
        txtLabelOnKOT.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtLabelOnKOTActionPerformed(evt);
            }
        });
        txtLabelOnKOT.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtLabelOnKOTKeyPressed(evt);
            }
        });

        lblNoOfCopies.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblNoOfCopies.setText("Primary Printer Copies :");

        txtPrimaryPinterNoOfCopies.setText("1");
        txtPrimaryPinterNoOfCopies.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtPrimaryPinterNoOfCopiesMouseClicked(evt);
            }
        });
        txtPrimaryPinterNoOfCopies.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtPrimaryPinterNoOfCopiesActionPerformed(evt);
            }
        });
        txtPrimaryPinterNoOfCopies.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtPrimaryPinterNoOfCopiesKeyPressed(evt);
            }
        });

        lblNoOfCopies1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblNoOfCopies1.setText("Secondary Printer Copies :");

        txtSecondaryPrinterNoOfCopies1.setText("0");
        txtSecondaryPrinterNoOfCopies1.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtSecondaryPrinterNoOfCopies1MouseClicked(evt);
            }
        });
        txtSecondaryPrinterNoOfCopies1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtSecondaryPrinterNoOfCopies1ActionPerformed(evt);
            }
        });
        txtSecondaryPrinterNoOfCopies1.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtSecondaryPrinterNoOfCopies1KeyPressed(evt);
            }
        });

        javax.swing.GroupLayout panelBodyLayout = new javax.swing.GroupLayout(panelBody);
        panelBody.setLayout(panelBodyLayout);
        panelBodyLayout.setHorizontalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addGap(245, 245, 245)
                .addComponent(lblFormName, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addGap(108, 108, 108)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBodyLayout.createSequentialGroup()
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelBodyLayout.createSequentialGroup()
                                .addComponent(lblLabelOnKOT, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtLabelOnKOT, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(258, 258, 258))
                            .addGroup(panelBodyLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(20, 20, 20)
                                .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(20, 20, 20)
                                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(80, 80, 80))
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelBodyLayout.createSequentialGroup()
                                .addComponent(lblCostCode, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtCostCode, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelBodyLayout.createSequentialGroup()
                                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(panelBodyLayout.createSequentialGroup()
                                        .addComponent(lblNoOfCopies, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtPrimaryPinterNoOfCopies))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelBodyLayout.createSequentialGroup()
                                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(lblGroupName2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(lblPrinterPort, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(10, 10, 10)
                                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(cmbPrimaryPrinters, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtCostName, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtPrimaryPrinterName, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnTestPrinter1, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBodyLayout.createSequentialGroup()
                                    .addGap(150, 150, 150)
                                    .addComponent(lblPrintOnBothPrinters, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(chkBoxPrintOnBothPrinters)
                                    .addGap(31, 31, 31))
                                .addGroup(panelBodyLayout.createSequentialGroup()
                                    .addComponent(lblNoOfCopies1, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(txtSecondaryPrinterNoOfCopies1, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(panelBodyLayout.createSequentialGroup()
                                .addComponent(lblPrinterPort1, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cmbSecondaryPrinters, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtSecondaryPrinterName, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnTestPrinter2, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        panelBodyLayout.setVerticalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(lblFormName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCostCode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCostCode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblGroupName2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCostName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblPrinterPort, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cmbPrimaryPrinters, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtPrimaryPrinterName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnTestPrinter1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNoOfCopies, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPrimaryPinterNoOfCopies, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbSecondaryPrinters, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPrinterPort1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSecondaryPrinterName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTestPrinter2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNoOfCopies1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSecondaryPrinterNoOfCopies1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblPrintOnBothPrinters, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBodyLayout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(chkBoxPrintOnBothPrinters, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblLabelOnKOT, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtLabelOnKOT, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(47, 47, 47)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(50, 50, 50))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 6, 0);
        panelLayout.add(panelBody, gridBagConstraints);

        getContentPane().add(panelLayout, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtCostCodeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtCostCodeMouseClicked
        // TODO add your handling code here:
        funSelectCostCenter();

    }//GEN-LAST:event_txtCostCodeMouseClicked

    private void txtCostNameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtCostNameMouseClicked
        // TODO add your handling code here:
        if (txtCostName.getText().length() == 0)
        {
            new frmAlfaNumericKeyBoard(this, true, "1", "Enter Cost Center Name").setVisible(true);
            txtCostName.setText(clsGlobalVarClass.gKeyboardValue);
        }
        else
        {
            new frmAlfaNumericKeyBoard(this, true, txtCostName.getText(), "1", "Enter Cost Center Name").setVisible(true);
            txtCostName.setText(clsGlobalVarClass.gKeyboardValue);
        }
    }//GEN-LAST:event_txtCostNameMouseClicked

    private void txtCostNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCostNameKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            cmbPrimaryPrinters.requestFocus();
        }
    }//GEN-LAST:event_txtCostNameKeyPressed

    private void btnCancelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseClicked
        // TODO add your handling code here:
        try
        {
         //    int totalTableRows = tblItemTable.getRowCount();

            dispose();
            clsGlobalVarClass.hmActiveForms.remove("Cost Center");
        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
        }

    }//GEN-LAST:event_btnCancelMouseClicked

    private void btnResetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnResetMouseClicked
        // TODO add your handling code here:
        funResetField();
    }//GEN-LAST:event_btnResetMouseClicked

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        // TODO add your handling code here:
        funResetField();
    }//GEN-LAST:event_btnResetActionPerformed

    private void btnNewMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNewMouseClicked
        // TODO add your handling code here:
        printerName = "";
        if (btnNew.getText().equalsIgnoreCase("SAVE"))
        {
            //Add new cost center
            funSaveCostCenter();
        }
        else
        {
            //Update existing cost center
            funUpdateCostCenter();
        }
    }//GEN-LAST:event_btnNewMouseClicked

    private void txtPrimaryPrinterNameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPrimaryPrinterNameMouseClicked
        try
        {
            if (txtPrimaryPrinterName.getText().length() == 0)
            {
                new frmAlfaNumericKeyBoard(this, true, "1", "Please Enter Primary Printer Name.").setVisible(true);
                txtPrimaryPrinterName.setText(clsGlobalVarClass.gKeyboardValue);
            }
            else
            {
                new frmAlfaNumericKeyBoard(this, true, txtPrimaryPrinterName.getText(), "1", "Please Enter Primary Printer Name.").setVisible(true);
                txtPrimaryPrinterName.setText(clsGlobalVarClass.gKeyboardValue);
            }

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_txtPrimaryPrinterNameMouseClicked

    private void txtPrimaryPrinterNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrimaryPrinterNameKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPrimaryPrinterNameKeyPressed

    private void cmbPrimaryPrintersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbPrimaryPrintersActionPerformed
        // 31-03-2015       
        if (!cmbPrimaryPrinters.getSelectedItem().toString().isEmpty())
        {
            txtPrimaryPrinterName.setText(cmbPrimaryPrinters.getSelectedItem().toString());
            btnTestPrinter1.setVisible(true);
        }
        else
        {
            if (!txtPrimaryPrinterName.getText().isEmpty())
            {
                btnTestPrinter1.setVisible(true);
            }
            else
            {
                btnTestPrinter1.setVisible(false);
            }

        }
    }//GEN-LAST:event_cmbPrimaryPrintersActionPerformed

    private void txtCostCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCostCodeKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() == '?' || evt.getKeyChar() == '/')
        {
            funSelectCostCenter();
        }
        if (evt.getKeyCode() == 10)
        {
            txtCostName.requestFocus();
        }

    }//GEN-LAST:event_txtCostCodeKeyPressed

    private void cmbPrimaryPrintersKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbPrimaryPrintersKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            cmbSecondaryPrinters.requestFocus();
        }
    }//GEN-LAST:event_cmbPrimaryPrintersKeyPressed

    private void btnNewKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnNewKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            printerName = "";
            if (btnNew.getText().equalsIgnoreCase("SAVE"))
            {
                //Add new cost center
                funSaveCostCenter();
            }
            else
            {
                //Update existing cost center
                funUpdateCostCenter();
            }
        }
    }//GEN-LAST:event_btnNewKeyPressed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        printerName = "";
        if (btnNew.getText().equalsIgnoreCase("SAVE"))
        {
            //Add new cost center
            funSaveCostCenter();
        }
        else
        {
            //Update existing cost center
            funUpdateCostCenter();
        }
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        dispose();
        clsGlobalVarClass.hmActiveForms.remove("Cost Center");
    }//GEN-LAST:event_btnCancelActionPerformed

    private void cmbSecondaryPrintersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSecondaryPrintersActionPerformed
        // TODO add your handling code here:        
        if (!cmbSecondaryPrinters.getSelectedItem().toString().isEmpty())
        {
            txtSecondaryPrinterName.setText(cmbSecondaryPrinters.getSelectedItem().toString());
            btnTestPrinter2.setVisible(true);
        }
        else
        {
            if (!txtSecondaryPrinterName.getText().isEmpty())
            {
                btnTestPrinter2.setVisible(true);
            }
            else
            {
                btnTestPrinter2.setVisible(false);
            }
        }
    }//GEN-LAST:event_cmbSecondaryPrintersActionPerformed

    private void cmbSecondaryPrintersKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbSecondaryPrintersKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10)
        {
            btnNew.requestFocus();
        }
    }//GEN-LAST:event_cmbSecondaryPrintersKeyPressed

    private void txtSecondaryPrinterNameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtSecondaryPrinterNameMouseClicked
        try
        {
            if (txtSecondaryPrinterName.getText().length() == 0)
            {
                new frmAlfaNumericKeyBoard(this, true, "1", "Please Enter Secondary Printer Name.").setVisible(true);
                txtSecondaryPrinterName.setText(clsGlobalVarClass.gKeyboardValue);
            }
            else
            {
                new frmAlfaNumericKeyBoard(this, true, txtSecondaryPrinterName.getText(), "1", "Please Enter Secondary Printer Name.").setVisible(true);
                txtSecondaryPrinterName.setText(clsGlobalVarClass.gKeyboardValue);
            }

        }
        catch (Exception e)
        {
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_txtSecondaryPrinterNameMouseClicked

    private void txtSecondaryPrinterNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSecondaryPrinterNameKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSecondaryPrinterNameKeyPressed

    private void btnTestPrinter2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTestPrinter2MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnTestPrinter2MouseClicked

    private void btnTestPrinter2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTestPrinter2ActionPerformed
        testPrinter = "secondary";
        funTestPrint(txtSecondaryPrinterName.getText().trim());
    }//GEN-LAST:event_btnTestPrinter2ActionPerformed

    private void btnTestPrinter2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnTestPrinter2KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnTestPrinter2KeyPressed

    private void btnTestPrinter1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTestPrinter1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnTestPrinter1MouseClicked

    private void btnTestPrinter1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTestPrinter1ActionPerformed
        testPrinter = "primary";
        funTestPrint(txtPrimaryPrinterName.getText().trim());
    }//GEN-LAST:event_btnTestPrinter1ActionPerformed

    private void btnTestPrinter1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnTestPrinter1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnTestPrinter1KeyPressed

    private void txtPrimaryPrinterNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPrimaryPrinterNameActionPerformed

    }//GEN-LAST:event_txtPrimaryPrinterNameActionPerformed

    private void chkBoxPrintOnBothPrintersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkBoxPrintOnBothPrintersActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkBoxPrintOnBothPrintersActionPerformed

    private void txtCostNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCostNameActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_txtCostNameActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
        clsGlobalVarClass.hmActiveForms.remove("Cost Center");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        clsGlobalVarClass.hmActiveForms.remove("Cost Center");
    }//GEN-LAST:event_formWindowClosing

    private void txtLabelOnKOTMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtLabelOnKOTMouseClicked
    {//GEN-HEADEREND:event_txtLabelOnKOTMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_txtLabelOnKOTMouseClicked

    private void txtLabelOnKOTActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtLabelOnKOTActionPerformed
    {//GEN-HEADEREND:event_txtLabelOnKOTActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtLabelOnKOTActionPerformed

    private void txtLabelOnKOTKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtLabelOnKOTKeyPressed
    {//GEN-HEADEREND:event_txtLabelOnKOTKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtLabelOnKOTKeyPressed

    private void txtPrimaryPinterNoOfCopiesMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtPrimaryPinterNoOfCopiesMouseClicked
    {//GEN-HEADEREND:event_txtPrimaryPinterNoOfCopiesMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPrimaryPinterNoOfCopiesMouseClicked

    private void txtPrimaryPinterNoOfCopiesActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtPrimaryPinterNoOfCopiesActionPerformed
    {//GEN-HEADEREND:event_txtPrimaryPinterNoOfCopiesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPrimaryPinterNoOfCopiesActionPerformed

    private void txtPrimaryPinterNoOfCopiesKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtPrimaryPinterNoOfCopiesKeyPressed
    {//GEN-HEADEREND:event_txtPrimaryPinterNoOfCopiesKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPrimaryPinterNoOfCopiesKeyPressed

    private void txtSecondaryPrinterNoOfCopies1MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtSecondaryPrinterNoOfCopies1MouseClicked
    {//GEN-HEADEREND:event_txtSecondaryPrinterNoOfCopies1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSecondaryPrinterNoOfCopies1MouseClicked

    private void txtSecondaryPrinterNoOfCopies1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtSecondaryPrinterNoOfCopies1ActionPerformed
    {//GEN-HEADEREND:event_txtSecondaryPrinterNoOfCopies1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSecondaryPrinterNoOfCopies1ActionPerformed

    private void txtSecondaryPrinterNoOfCopies1KeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtSecondaryPrinterNoOfCopies1KeyPressed
    {//GEN-HEADEREND:event_txtSecondaryPrinterNoOfCopies1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSecondaryPrinterNoOfCopies1KeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnTestPrinter1;
    private javax.swing.JButton btnTestPrinter2;
    private javax.swing.JCheckBox chkBoxPrintOnBothPrinters;
    private javax.swing.JComboBox cmbPrimaryPrinters;
    private javax.swing.JComboBox cmbSecondaryPrinters;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JLabel lblCostCode;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblFormName;
    private javax.swing.JLabel lblGroupName2;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblLabelOnKOT;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblNoOfCopies;
    private javax.swing.JLabel lblNoOfCopies1;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblPrintOnBothPrinters;
    private javax.swing.JLabel lblPrinterPort;
    private javax.swing.JLabel lblPrinterPort1;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblfromName;
    private javax.swing.JPanel panelBody;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelLayout;
    private javax.swing.JTextField txtCostCode;
    private javax.swing.JTextField txtCostName;
    private javax.swing.JTextField txtLabelOnKOT;
    private javax.swing.JTextField txtPrimaryPinterNoOfCopies;
    private javax.swing.JTextField txtPrimaryPrinterName;
    private javax.swing.JTextField txtSecondaryPrinterName;
    private javax.swing.JTextField txtSecondaryPrinterNoOfCopies1;
    // End of variables declaration//GEN-END:variables

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
            objUtility.funWriteErrorLog(e);
            e.printStackTrace();
        }
    }

    private void funCreateTestTextFile(File file)
    {
        BufferedWriter fileWriter = null;
        try
        {
            //File file=new File(filename);
            fileWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF8"));

            String fileHeader = "----------Print Testing------------";
            String dottedLine = "-----------------------------------";
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
            fileWriter.write("Cost Center Name : " + txtCostName.getText());
            fileWriter.newLine();
            if (testPrinter.equalsIgnoreCase("primary"))
            {
                fileWriter.write("Primary Printer Name : " + txtPrimaryPrinterName.getText());
                fileWriter.newLine();
            }
            if (testPrinter.equalsIgnoreCase("secondary"))
            {
                fileWriter.write("Secondary Printer Name : " + txtSecondaryPrinterName.getText());
                fileWriter.newLine();
            }
            fileWriter.write(dottedLine);

        }
        catch (FileNotFoundException ex)
        {
            objUtility.funWriteErrorLog(ex);
            ex.printStackTrace();
        }
        catch (UnsupportedEncodingException ex)
        {
            objUtility.funWriteErrorLog(ex);
            ex.printStackTrace();
        }
        catch (IOException ex)
        {
            objUtility.funWriteErrorLog(ex);
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
                objUtility.funWriteErrorLog(ex);
                ex.printStackTrace();
            }
        }

    }
}
