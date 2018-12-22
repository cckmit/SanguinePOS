/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSTransaction.view;

import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsUtility;
import com.POSGlobal.view.frmNumberKeyPad;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;

public class frmAssignHomeDelivery extends javax.swing.JFrame
{

    private int cntDelBoyNavigate;
    private int cntBillNoNavigate1;
    private String sql;
    private Map<String, String> hmDeliveryBoy;
    private Map<String, String> hmBillNo;
    private Map<String, String> hmAssignedDelBoy;
    private Map<String, String> hmAssignedBillNo;
    private JButton[] btnDelBoysArray;
    private JButton[] btnBillNoArray;
    private clsUtility objUtility;

    public frmAssignHomeDelivery()
    {
        initComponents();
        try
        {
            Timer timer = new Timer(500, new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    String dateAndTime = clsGlobalVarClass.gPOSDateToDisplay + " " + String.format("%tr", new Date());
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

            objUtility = new clsUtility();

            btnDelBoysArray = new JButton[]
            {
                btnDelBoyTable1, btnDelBoyTable2, btnDelBoyTable3, btnDelBoyTable4, btnDelBoyTable5, btnDelBoyTable6, btnDelBoyTable7, btnDelBoyTable8, btnDelBoyTable9, btnDelBoyTable10, btnDelBoyTable11, btnDelBoyTable12, btnDelBoyTable13, btnDelBoyTable14, btnDelBoyTable15
            };
            btnBillNoArray = new JButton[]
            {
                btnBillNoTable1, btnBillNoTable2, btnBillNoTable3, btnBillNoTable4, btnBillNoTable5, btnBillNoTable6, btnBillNoTable7, btnBillNoTable8, btnBillNoTable9, btnBillNoTable10, btnBillNoTable11, btnBillNoTable12, btnBillNoTable13, btnBillNoTable14, btnBillNoTable15
            };
            hmBillNo = new HashMap<String, String>();
            hmDeliveryBoy = new HashMap<String, String>();
            hmAssignedDelBoy = new HashMap<String, String>();
            hmAssignedBillNo = new HashMap<String, String>();
            btnDelBoyPrev.setEnabled(false);
            btnBillNoPrev.setEnabled(false);
            cntDelBoyNavigate = 0;
            cntBillNoNavigate1 = 0;

            funFillComboZones();
            String zoneCode = cmbZone.getSelectedItem().toString().split("!")[1].trim();
            cmbArea.addItem("All Area                                                     !All");
            funFillComboAreas(zoneCode);

            cmbZone.addItemListener(new ItemListener()
            {
                @Override
                public void itemStateChanged(ItemEvent e)
                {
                    String zoneCode = cmbZone.getSelectedItem().toString().split("!")[1].trim();
                    funFillComboAreas(zoneCode);
                }
            });

            cmbArea.addItemListener(new ItemListener()
            {
                @Override
                public void itemStateChanged(ItemEvent e)
                {
                    funFillBillAndDelBoy();
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void funFillBillAndDelBoy()
    {
        try
        {
            cntDelBoyNavigate = 0;
            cntBillNoNavigate1 = 0;

            btnDelBoyPrev.setEnabled(false);
            btnBillNoPrev.setEnabled(false);
            btnDelBoyNext.setEnabled(true);
            btnBillNoNext.setEnabled(true);
            hmBillNo.clear();
            hmDeliveryBoy.clear();

            sql = "select  strDPCode, strDPName from tbldeliverypersonmaster order by strDPCode";
            ResultSet rsTblNo = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            while (rsTblNo.next())
            {
                hmDeliveryBoy.put(rsTblNo.getString(2), rsTblNo.getString(1));
            }
            rsTblNo.close();

            sql = "select a.strBillNo,b.strCustomerCode,ifnull(d.strZoneName,''),ifnull(e.strDeliveryTime,'') "
                    + " from tblbillhd a "
                    + " left outer join tblcustomermaster b on a.strCustomerCode=b.strCustomerCode "
                    + " left outer join tblbuildingmaster c on b.strBuldingCode=c.strBuildingCode "
                    + " left outer join tblzonemaster d on c.strZoneCode=d.strZoneCode "
                    + " left outer join tbladvbookbillhd e on a.strAdvBookingNo=e.strAdvBookingNo "
                    + " left outer join tblhomedelivery f on a.strBillNo=f.strBillNo "
                    + " where a.strBillNo not in (select strBillNo from tblbillsettlementdtl) "
                    //+ " and a.strAdvBookingNo in (select strAdvBookingNo from tbladvbookbillhd) "
                    + " and a.strOperationType='HomeDelivery' "
                    + " and length(f.strDPCode)=0 ";

            String zoneCode = cmbZone.getSelectedItem().toString().split("!")[1].trim();
            String areaCode = cmbArea.getSelectedItem().toString().split("!")[1].trim();
            System.out.println("Zone code=" + zoneCode + "      AreaCode=" + areaCode);

            if (!zoneCode.equalsIgnoreCase("All"))
            {
                sql = sql + " and c.strZoneCode='" + zoneCode + "' ";
            }
            if (!areaCode.equalsIgnoreCase("All"))
            {
                sql = sql + " and c.strBuildingCode='" + areaCode + "' ";
            }
            sql = sql + " order by d.strZoneName";
            System.out.println(sql);

            rsTblNo = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            while (rsTblNo.next())
            {
                String zoneWithDelTime = rsTblNo.getString(3) + " " + rsTblNo.getString(4);
                hmBillNo.put(rsTblNo.getString(1), zoneWithDelTime);
            }

            funLoadDelBoyTables(0, hmDeliveryBoy.size());
            funLoadBillNoTables(0, hmBillNo.size());

            if (hmBillNo.size() <= 15)
            {
                btnBillNoNext.setEnabled(false);
            }
            if (hmDeliveryBoy.size() <= 15)
            {
                btnDelBoyNext.setEnabled(false);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void funLoadBillNoTables(int startIndex, int totalSize)
    {
        try
        {
            int cntIndex = 0;
            for (int k = 0; k < btnBillNoArray.length; k++)
            {
                btnBillNoArray[k].setForeground(Color.BLACK);
                btnBillNoArray[k].setBackground(Color.LIGHT_GRAY);
                btnBillNoArray[k].setText("");
            }

            Object[] arrObjBillNo = hmBillNo.entrySet().toArray();
            for (int i = startIndex; i < totalSize; i++)
            {
                if (i == hmBillNo.size())
                {
                    break;
                }
                String billNo = arrObjBillNo[i].toString().split("=")[0];
                String zoneWithTime = arrObjBillNo[i].toString().split("=")[1];

                if (cntIndex < 15)
                {
                    btnBillNoArray[cntIndex].setText("<html>" + billNo + "<br>" + zoneWithTime + "</html>");
                    btnBillNoArray[cntIndex].setEnabled(true);

                    String sql = "select strBillNo from tblhomedeldtl "
                            + "where strBillNo='" + billNo + "' and strClientCode='" + clsGlobalVarClass.gClientCode + "' ";
                    ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                    if (rs.next())
                    {
                        btnBillNoArray[cntIndex].setBackground(Color.RED);
                        btnBillNoArray[cntIndex].setForeground(Color.WHITE);
                    }
                    rs.close();
                    cntIndex++;
                }
            }

            for (int j = cntIndex; j < 15; j++)
            {
                btnBillNoArray[j].setEnabled(false);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void funLoadDelBoyTables(int startIndex, int totalSize)
    {
        try
        {
            int cntIndex = 0;

            for (int k = 0; k < btnDelBoysArray.length; k++)
            {
                btnDelBoysArray[k].setForeground(Color.BLACK);
                btnDelBoysArray[k].setBackground(Color.LIGHT_GRAY);
                btnDelBoysArray[k].setText("");
            }
            Object[] arrObjDelBoy = hmDeliveryBoy.entrySet().toArray();

            for (int i = startIndex; i < totalSize; i++)
            {
                if (i == hmDeliveryBoy.size() || cntIndex == 15)
                {
                    break;
                }

                String delBoyCode = arrObjDelBoy[i].toString().split("=")[1];
                String delBoyName = arrObjDelBoy[i].toString().split("=")[0];

                btnDelBoysArray[cntIndex].setText(delBoyName);
                btnDelBoysArray[cntIndex].setEnabled(true);

                String sql = "select strBillNo from tblhomedeldtl "
                        + " where strDPCode='" + delBoyCode + "' and strClientCode='" + clsGlobalVarClass.gClientCode + "' and strSettleYN='N' ";
                //System.out.println(sql);
                ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
                if (rs.next())
                {
                    
                    if (cntIndex < 15)
                    {
                        btnDelBoysArray[cntIndex].setBackground(Color.RED);
                        btnDelBoysArray[cntIndex].setForeground(Color.WHITE);
                    }
                }
                rs.close();
                cntIndex++;
            }
            for (int j = cntIndex; j < 15; j++)
            {
                btnDelBoysArray[j].setEnabled(false);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void funAddDelBoysToMap(JButton buttonClicked)
    {
        StringBuilder sbDelBoys = new StringBuilder();

        if (hmAssignedDelBoy.containsKey(buttonClicked.getActionCommand()))
        {
            buttonClicked.setBackground(Color.LIGHT_GRAY);
            buttonClicked.setForeground(Color.BLACK);
            hmAssignedDelBoy.remove(buttonClicked.getActionCommand());
        }
        else
        {
            buttonClicked.setBackground(Color.BLUE);
            buttonClicked.setForeground(Color.WHITE);
            hmAssignedDelBoy.put(buttonClicked.getActionCommand(), hmDeliveryBoy.get(buttonClicked.getActionCommand()));
        }
        sbDelBoys.setLength(0);

        Iterator keySetIterator = hmAssignedDelBoy.keySet().iterator();
        for (int i = 1; keySetIterator.hasNext(); i++)
        {
            String key = keySetIterator.next().toString();
            if (i == 1)
            {
                lblDelBoyNames.setText(key);
                sbDelBoys.append(key);
            }
            else
            {
                sbDelBoys.append(", ");
                sbDelBoys.append(key);
            }
        }
        lblDelBoyNames.setText(sbDelBoys.toString());
    }

    private void funAddToBillNoMap(JButton buttonClicked)
    {

        //System.out.println(buttonClicked.getActionCommand());
        //<html>P0132030<br>NA NA</html>
        String btnText = buttonClicked.getText().trim();
        StringBuilder sbBillNo = new StringBuilder();
        StringBuilder sb = new StringBuilder(btnText);
        int ind1 = sb.lastIndexOf("<html>");
        int ind2 = sb.lastIndexOf("<br>");

        String billNo = btnText.split("<br>")[0].split("<html>")[1];
        sb = null;

        if (hmAssignedBillNo.containsValue(billNo))
        {
            buttonClicked.setBackground(Color.LIGHT_GRAY);
            buttonClicked.setForeground(Color.BLACK);
            hmAssignedBillNo.remove(billNo);

            if (hmAssignedBillNo.size() > 1)
            {
                lblBillAmountValue.setText("0.00");
                txtBillLooseCashAmount.setText("0.00");
            }
            if (hmAssignedBillNo.size() == 1)
            {
                for (String stringBillNo : hmAssignedBillNo.values())
                {
                    funSetBillAmountAndLooseCash(stringBillNo);
                }
            }
        }
        else
        {
            buttonClicked.setBackground(Color.BLUE);
            buttonClicked.setForeground(Color.WHITE);
            hmAssignedBillNo.put(billNo, billNo);
            if (hmAssignedBillNo.size() > 1)
            {
                lblBillAmountValue.setText("0.00");
                txtBillLooseCashAmount.setText("0.00");
            }
            else
            {
                funSetBillAmountAndLooseCash(billNo);
            }
        }

        sbBillNo.setLength(0);
        Iterator keySetIterator = hmAssignedBillNo.keySet().iterator();
        for (int i = 1; keySetIterator.hasNext(); i++)
        {
            String key = keySetIterator.next().toString();
            if (i == 1)
            {
                sbBillNo.append(key);
            }
            else
            {
                sbBillNo.append(", ");
                sbBillNo.append(key);
            }
        }
        lblBillNoNames.setText(sbBillNo.toString());
    }

    private void funInsertDelBoyBillDtl()
    {
        try
        {
            StringBuilder sql = new StringBuilder();
            int countAffectedRows = 0;
            String dpCode = "";
            String sqltemptable = "";
            ArrayList<String> deliveryBoyList = new ArrayList<String>();
            Iterator billNoIterator = hmAssignedBillNo.keySet().iterator();
            while (billNoIterator.hasNext())
            {
                String billNo = hmAssignedBillNo.get(billNoIterator.next().toString());

                sql.setLength(0);
                sql.append("Update tblhomedelivery set strDPCode='");
                String sql2 = "";
                Iterator delBoyIterator = hmAssignedDelBoy.keySet().iterator();
                while (delBoyIterator.hasNext())
                {
                    dpCode = hmAssignedDelBoy.get(delBoyIterator.next().toString());
                    sql2 += "," + dpCode;
                    deliveryBoyList.add(dpCode);
                }
                sql2 = sql2.substring(1, sql2.length());
                sql.append(sql2);
                
                sql.append("' ");
                sql.append(",dblLooseCashAmt='" + txtBillLooseCashAmount.getText() + "' ");                
                sql.append(" where strBillNo='" + billNo + "' ");
                
                String sqldeltemp = "Delete from tblhomedeldtl where strBillNo='" + billNo + "' ";
                clsGlobalVarClass.dbMysql.execute(sqldeltemp);
                sqltemptable = "Insert into tblhomedeldtl(strBillNo,strDPCode,strClientCode,strDataPostFlag,dblDBIncentives,dteBillDate) "
                        + " Values ";

                String sqlCustAreaCode = "select c.strBuildingCode "
                        + "from tblbillhd a "
                        + "left outer join tblcustomermaster b on a.strCustomerCode=b.strCustomerCode "
                        + "left outer join tblbuildingmaster c on b.strBuldingCode=c.strBuildingCode "
                        + "where a.strBillNo='" + billNo + "'; ";
                ResultSet rsCustCode = clsGlobalVarClass.dbMysql.executeResultSet(sqlCustAreaCode);
                String buildingCode = "";
                if (rsCustCode.next())
                {
                    buildingCode = rsCustCode.getString(1);
                }

                Iterator temp = deliveryBoyList.iterator();
                while (temp.hasNext())
                {
                    String delBoyCode = temp.next().toString();
                    String sqlDBIncenetives = "select d.strCustAreaCode,d.strDeliveryBoyCode,ifnull(d.dblValue,0.00) "
                            + "from tblareawisedelboywisecharges d "
                            + "where d.strCustAreaCode='" + buildingCode + "' "
                            + "and strDeliveryBoyCode='" + delBoyCode + "'; ";
                    ResultSet rsDBIncentives = clsGlobalVarClass.dbMysql.executeResultSet(sqlDBIncenetives);
                    String dbIncentives = "0.00";
                    if (rsDBIncentives.next())
                    {
                        dbIncentives = rsDBIncentives.getString(3);
                    }

                    sqltemptable += "('" + billNo + "','" + delBoyCode + "','" + clsGlobalVarClass.gClientCode + "','N','" + dbIncentives + "','"+clsGlobalVarClass.getPOSDateForTransaction()+"'), ";
                }
                StringBuilder sb = new StringBuilder(sqltemptable);
                int index = sb.lastIndexOf(",");
                sqltemptable = sb.delete(index, sb.length()).toString();
                clsGlobalVarClass.dbMysql.execute(sqltemptable);
                countAffectedRows += funUpdateBill(sql);

                if (clsGlobalVarClass.gHomeDelSMSYN)
                {
                    objUtility.funSendSMS(billNo, clsGlobalVarClass.gHomeDeliverySMS, "Home Delivery");
                }

                if (clsGlobalVarClass.gPrintType.equalsIgnoreCase("Text File"))
                {
                    objUtility.funPrintBill(billNo, objUtility.funGetPOSDateForTransaction(), false, clsGlobalVarClass.gPOSCode,"print");
                }

            }

            if (countAffectedRows > 0)
            {
                new com.POSGlobal.view.frmOkPopUp(this, "Updated successfully.", "Message", 3).setVisible(true);
                hmAssignedBillNo.clear();
                hmAssignedDelBoy.clear();
                lblDelBoyNames.setText("");
                lblBillNoNames.setText("");
                lblBillAmountValue.setText("0.00");
                txtBillLooseCashAmount.setText("0.00");
                
                funFillBillAndDelBoy();
//                funLoadDelBoyTables(0, hmDeliveryBoy.size());
//                funLoadBillNoTables(0, hmBillNo.size());
            }
            else
            {
                new com.POSGlobal.view.frmOkPopUp(this, "Unable to update bill detail.", "Error", 0).setVisible(true);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private int funUpdateBill(StringBuilder sql)
    {

        try
        {
            int affectedRows = clsGlobalVarClass.dbMysql.execute(sql.toString());
            return affectedRows;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return 0;
    }

    private void funDelBoyPrevButtonPressed()
    {
        btnDelBoyNext.setEnabled(true);
        cntDelBoyNavigate--;
        if (cntDelBoyNavigate == 0)
        {
            btnDelBoyPrev.setEnabled(false);
            funLoadDelBoyTables(0, hmDeliveryBoy.size());
        }
        else
        {
            int tableSize = cntDelBoyNavigate * 15;
            int totalSize = tableSize + 15;
            funLoadDelBoyTables(tableSize, totalSize);
        }
    }

    private void funBillNoPrevButtonPressed()
    {
        btnBillNoNext.setEnabled(true);
        cntBillNoNavigate1--;
        if (cntBillNoNavigate1 == 0)
        {
            btnBillNoPrev.setEnabled(false);
            funLoadDelBoyTables(0, hmBillNo.size());
        }
        else
        {
            int tableSize = cntBillNoNavigate1 * 15;
            int totalSize = tableSize + 15;
            funLoadBillNoTables(tableSize, totalSize);
        }
    }

    private void funDelBoyNextButtonPressed()
    {
        cntDelBoyNavigate++;
        int tableSize = cntDelBoyNavigate * 15;
        int resMod = hmDeliveryBoy.size() % 15;
        int resDiv = hmDeliveryBoy.size() / 15;
        int totalSize = tableSize + 15;
        funLoadDelBoyTables(tableSize, totalSize);
        btnDelBoyPrev.setEnabled(true);

        /*
         System.out.println("Total Size="+vDelBoyName.size());
         System.out.println("Div="+resDiv);
         System.out.println("Mod="+resMod);
         System.out.println("counter="+cntDelBoyNavigate);
         */
        if (resDiv == cntDelBoyNavigate || resMod <= 0)
        {
            btnDelBoyNext.setEnabled(false);
        }
    }

    private void funBillNoNextButtonPressed()
    {
        cntBillNoNavigate1++;
        int tableSize = cntBillNoNavigate1 * 15;
        int resMod = hmBillNo.size() % 15;
        int resDiv = hmBillNo.size() / 15;
        int totalSize = tableSize + 15;
        funLoadBillNoTables(tableSize, totalSize);
        btnBillNoPrev.setEnabled(true);
        if (resDiv == cntBillNoNavigate1 || resMod <= 0)
        {
            btnBillNoNext.setEnabled(false);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        panelHeader = new javax.swing.JPanel();
        lblProductName = new javax.swing.JLabel();
        filler4 = new javax.swing.Box.Filler(new java.awt.Dimension(20, 0), new java.awt.Dimension(20, 0), new java.awt.Dimension(20, 32767));
        lblUserCode = new javax.swing.JLabel();
        filler5 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        lblPosName = new javax.swing.JLabel();
        filler6 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        lblDate = new javax.swing.JLabel();
        lblHOSign = new javax.swing.JLabel();
        panelLayout = 
        new JPanel() {  
            public void paintComponent(Graphics g) {  
                Image img = Toolkit.getDefaultToolkit().getImage(  
                    getClass().getResource("/com/POSTransaction/images/imgBackgroundImage.png"));  
                g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);  
            }  
        };  ;
        panelBodyRoot = new javax.swing.JPanel();
        panelBodt = new javax.swing.JPanel();
        panelBottom = new javax.swing.JPanel();
        btnSave = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        lblDelBoys = new javax.swing.JLabel();
        lblBillNos = new javax.swing.JLabel();
        lblDelBoyNames = new javax.swing.JLabel();
        lblBillNoNames = new javax.swing.JLabel();
        lblBillAmountName = new javax.swing.JLabel();
        lblBillAmountValue = new javax.swing.JLabel();
        lblBillLooseCashName = new javax.swing.JLabel();
        txtBillLooseCashAmount = new javax.swing.JTextField();
        panelDeliveryBoy = new javax.swing.JPanel();
        btnDelBoyTable1 = new javax.swing.JButton();
        btnDelBoyTable2 = new javax.swing.JButton();
        btnDelBoyTable3 = new javax.swing.JButton();
        btnDelBoyTable4 = new javax.swing.JButton();
        btnDelBoyTable5 = new javax.swing.JButton();
        btnDelBoyTable6 = new javax.swing.JButton();
        btnDelBoyTable7 = new javax.swing.JButton();
        btnDelBoyTable8 = new javax.swing.JButton();
        btnDelBoyTable9 = new javax.swing.JButton();
        btnDelBoyNext = new javax.swing.JButton();
        btnDelBoyPrev = new javax.swing.JButton();
        btnDelBoyTable10 = new javax.swing.JButton();
        btnDelBoyTable11 = new javax.swing.JButton();
        btnDelBoyTable12 = new javax.swing.JButton();
        lblDelBoyList = new javax.swing.JLabel();
        btnDelBoyTable13 = new javax.swing.JButton();
        btnDelBoyTable14 = new javax.swing.JButton();
        btnDelBoyTable15 = new javax.swing.JButton();
        panelBillNo = new javax.swing.JPanel();
        btnBillNoTable1 = new javax.swing.JButton();
        btnBillNoTable2 = new javax.swing.JButton();
        btnBillNoTable3 = new javax.swing.JButton();
        btnBillNoTable4 = new javax.swing.JButton();
        btnBillNoTable5 = new javax.swing.JButton();
        btnBillNoTable6 = new javax.swing.JButton();
        btnBillNoTable7 = new javax.swing.JButton();
        btnBillNoTable8 = new javax.swing.JButton();
        btnBillNoTable9 = new javax.swing.JButton();
        btnBillNoPrev = new javax.swing.JButton();
        btnBillNoNext = new javax.swing.JButton();
        btnBillNoTable10 = new javax.swing.JButton();
        btnBillNoTable11 = new javax.swing.JButton();
        btnBillNoTable12 = new javax.swing.JButton();
        lblBillNoList = new javax.swing.JLabel();
        btnBillNoTable13 = new javax.swing.JButton();
        btnBillNoTable14 = new javax.swing.JButton();
        btnBillNoTable15 = new javax.swing.JButton();
        cmbZone = new javax.swing.JComboBox();
        cmbArea = new javax.swing.JComboBox();

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
        lblProductName.setText("JPOS - Assign Home Delivery");
        panelHeader.add(lblProductName);
        panelHeader.add(filler4);

        lblUserCode.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblUserCode.setForeground(new java.awt.Color(255, 255, 255));
        lblUserCode.setMaximumSize(new java.awt.Dimension(71, 30));
        lblUserCode.setMinimumSize(new java.awt.Dimension(71, 30));
        lblUserCode.setPreferredSize(new java.awt.Dimension(71, 30));
        panelHeader.add(lblUserCode);
        panelHeader.add(filler5);

        lblPosName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblPosName.setForeground(new java.awt.Color(255, 255, 255));
        lblPosName.setMaximumSize(new java.awt.Dimension(321, 30));
        lblPosName.setMinimumSize(new java.awt.Dimension(321, 30));
        lblPosName.setPreferredSize(new java.awt.Dimension(321, 30));
        panelHeader.add(lblPosName);
        panelHeader.add(filler6);

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

        panelBodyRoot.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        panelBodyRoot.setMinimumSize(new java.awt.Dimension(800, 570));
        panelBodyRoot.setOpaque(false);

        panelBodt.setBackground(new java.awt.Color(255, 255, 255));
        panelBodt.setOpaque(false);
        panelBodt.setPreferredSize(new java.awt.Dimension(610, 600));
        panelBodt.setLayout(null);

        panelBottom.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, null, java.awt.Color.lightGray, null, null));
        panelBottom.setOpaque(false);

        btnSave.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnSave.setForeground(new java.awt.Color(255, 255, 255));
        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn1.png"))); // NOI18N
        btnSave.setText("SAVE");
        btnSave.setToolTipText("Save Assign Home Delivery");
        btnSave.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSave.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn2.png"))); // NOI18N
        btnSave.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn2.png"))); // NOI18N
        btnSave.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnSaveMouseClicked(evt);
            }
        });

        btnCancel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnCancel.setForeground(new java.awt.Color(255, 255, 255));
        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn1.png"))); // NOI18N
        btnCancel.setText("CLOSE");
        btnCancel.setToolTipText("Close Assign Home Delivery");
        btnCancel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCancel.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn2.png"))); // NOI18N
        btnCancel.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn2.png"))); // NOI18N
        btnCancel.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnCancelMouseClicked(evt);
            }
        });

        lblDelBoys.setText("Delivery Boys :");

        lblBillNos.setText("Bill No :");

        lblDelBoyNames.setForeground(Color.BLUE);

        lblBillNoNames.setForeground(Color.blue);

        lblBillAmountName.setText("Bill Amount     :");

        lblBillAmountValue.setText("0.00");
        lblBillAmountValue.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        lblBillLooseCashName.setText("Loose Cash :");

        txtBillLooseCashAmount.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtBillLooseCashAmount.setText("0");
        txtBillLooseCashAmount.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                txtBillLooseCashAmountMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout panelBottomLayout = new javax.swing.GroupLayout(panelBottom);
        panelBottom.setLayout(panelBottomLayout);
        panelBottomLayout.setHorizontalGroup(
            panelBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBottomLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(panelBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblDelBoys, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblBillAmountName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBottomLayout.createSequentialGroup()
                        .addComponent(lblDelBoyNames, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblBillNos)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblBillNoNames, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBottomLayout.createSequentialGroup()
                        .addComponent(lblBillAmountValue, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblBillLooseCashName, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtBillLooseCashAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(224, 224, 224)
                        .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)
                        .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29))))
        );
        panelBottomLayout.setVerticalGroup(
            panelBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBottomLayout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addGroup(panelBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblDelBoyNames, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblDelBoys, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblBillNoNames, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblBillNos))
                .addGroup(panelBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBottomLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblBillAmountName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblBillAmountValue, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblBillLooseCashName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtBillLooseCashAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBottomLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                        .addGroup(panelBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())))
        );

        panelBodt.add(panelBottom);
        panelBottom.setBounds(0, 470, 800, 100);

        panelDeliveryBoy.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, null, java.awt.Color.lightGray, null, null));
        panelDeliveryBoy.setMinimumSize(new java.awt.Dimension(400, 460));
        panelDeliveryBoy.setOpaque(false);

        btnDelBoyTable1.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnDelBoyTable1MouseClicked(evt);
            }
        });

        btnDelBoyTable2.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnDelBoyTable2MouseClicked(evt);
            }
        });

        btnDelBoyTable3.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnDelBoyTable3MouseClicked(evt);
            }
        });

        btnDelBoyTable4.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnDelBoyTable4MouseClicked(evt);
            }
        });

        btnDelBoyTable5.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnDelBoyTable5MouseClicked(evt);
            }
        });

        btnDelBoyTable6.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnDelBoyTable6MouseClicked(evt);
            }
        });

        btnDelBoyTable7.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnDelBoyTable7MouseClicked(evt);
            }
        });

        btnDelBoyTable8.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnDelBoyTable8MouseClicked(evt);
            }
        });

        btnDelBoyTable9.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnDelBoyTable9MouseClicked(evt);
            }
        });

        btnDelBoyNext.setForeground(new java.awt.Color(255, 255, 255));
        btnDelBoyNext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn1.png"))); // NOI18N
        btnDelBoyNext.setText(">>");
        btnDelBoyNext.setToolTipText("Next");
        btnDelBoyNext.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDelBoyNext.setIconTextGap(5);
        btnDelBoyNext.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnDelBoyNextActionPerformed(evt);
            }
        });

        btnDelBoyPrev.setForeground(new java.awt.Color(255, 255, 255));
        btnDelBoyPrev.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn1.png"))); // NOI18N
        btnDelBoyPrev.setText("<<");
        btnDelBoyPrev.setToolTipText("Previous");
        btnDelBoyPrev.setEnabled(false);
        btnDelBoyPrev.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDelBoyPrev.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnDelBoyPrevActionPerformed(evt);
            }
        });

        btnDelBoyTable10.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnDelBoyTable10MouseClicked(evt);
            }
        });

        btnDelBoyTable11.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnDelBoyTable11MouseClicked(evt);
            }
        });

        btnDelBoyTable12.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnDelBoyTable12MouseClicked(evt);
            }
        });

        lblDelBoyList.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblDelBoyList.setForeground(new java.awt.Color(51, 51, 255));
        lblDelBoyList.setText("DELIVERY BOY");

        btnDelBoyTable13.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnDelBoyTable13MouseClicked(evt);
            }
        });

        btnDelBoyTable14.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnDelBoyTable14MouseClicked(evt);
            }
        });

        btnDelBoyTable15.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnDelBoyTable15MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout panelDeliveryBoyLayout = new javax.swing.GroupLayout(panelDeliveryBoy);
        panelDeliveryBoy.setLayout(panelDeliveryBoyLayout);
        panelDeliveryBoyLayout.setHorizontalGroup(
            panelDeliveryBoyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDeliveryBoyLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelDeliveryBoyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelDeliveryBoyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(btnDelBoyTable10, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnDelBoyTable7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnDelBoyTable4, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnDelBoyTable1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnDelBoyTable13, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDelBoyPrev, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(panelDeliveryBoyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnDelBoyTable2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDelBoyTable5, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDelBoyTable8, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDelBoyTable11, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDelBoyTable14, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDelBoyList, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(28, 28, 28)
                .addGroup(panelDeliveryBoyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnDelBoyNext, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDelBoyTable3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDelBoyTable6, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDelBoyTable9, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDelBoyTable12, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDelBoyTable15, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31))
        );
        panelDeliveryBoyLayout.setVerticalGroup(
            panelDeliveryBoyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDeliveryBoyLayout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(panelDeliveryBoyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btnDelBoyTable2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDelBoyTable1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDelBoyTable3, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelDeliveryBoyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btnDelBoyTable5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDelBoyTable4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDelBoyTable6, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelDeliveryBoyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btnDelBoyTable8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDelBoyTable7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDelBoyTable9, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelDeliveryBoyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btnDelBoyTable11, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDelBoyTable10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDelBoyTable12, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(panelDeliveryBoyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnDelBoyTable15, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDelBoyTable14, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDelBoyTable13, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelDeliveryBoyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnDelBoyNext, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelDeliveryBoyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnDelBoyPrev, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblDelBoyList, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(5, 5, 5))
        );

        panelBodt.add(panelDeliveryBoy);
        panelDeliveryBoy.setBounds(0, 0, 400, 470);

        panelBillNo.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, null, java.awt.Color.lightGray, null, null));
        panelBillNo.setOpaque(false);
        panelBillNo.setPreferredSize(new java.awt.Dimension(400, 460));

        btnBillNoTable1.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnBillNoTable1MouseClicked(evt);
            }
        });

        btnBillNoTable2.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnBillNoTable2MouseClicked(evt);
            }
        });

        btnBillNoTable3.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnBillNoTable3MouseClicked(evt);
            }
        });

        btnBillNoTable4.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnBillNoTable4MouseClicked(evt);
            }
        });

        btnBillNoTable5.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnBillNoTable5MouseClicked(evt);
            }
        });

        btnBillNoTable6.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnBillNoTable6MouseClicked(evt);
            }
        });

        btnBillNoTable7.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnBillNoTable7MouseClicked(evt);
            }
        });

        btnBillNoTable8.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnBillNoTable8MouseClicked(evt);
            }
        });

        btnBillNoTable9.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnBillNoTable9MouseClicked(evt);
            }
        });

        btnBillNoPrev.setForeground(new java.awt.Color(255, 255, 255));
        btnBillNoPrev.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn1.png"))); // NOI18N
        btnBillNoPrev.setText("<<");
        btnBillNoPrev.setToolTipText("Previous");
        btnBillNoPrev.setEnabled(false);
        btnBillNoPrev.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnBillNoPrev.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnBillNoPrevActionPerformed(evt);
            }
        });

        btnBillNoNext.setForeground(new java.awt.Color(255, 255, 255));
        btnBillNoNext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn1.png"))); // NOI18N
        btnBillNoNext.setText(">>");
        btnBillNoNext.setToolTipText("Next");
        btnBillNoNext.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnBillNoNext.setIconTextGap(5);
        btnBillNoNext.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnBillNoNextActionPerformed(evt);
            }
        });

        btnBillNoTable10.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnBillNoTable10MouseClicked(evt);
            }
        });

        btnBillNoTable11.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnBillNoTable11MouseClicked(evt);
            }
        });

        btnBillNoTable12.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnBillNoTable12MouseClicked(evt);
            }
        });

        lblBillNoList.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblBillNoList.setForeground(new java.awt.Color(51, 51, 255));
        lblBillNoList.setText("      BILL NO");

        btnBillNoTable13.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnBillNoTable13MouseClicked(evt);
            }
        });

        btnBillNoTable14.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnBillNoTable14MouseClicked(evt);
            }
        });

        btnBillNoTable15.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnBillNoTable15MouseClicked(evt);
            }
        });

        cmbZone.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbZoneActionPerformed(evt);
            }
        });

        cmbArea.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbAreaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelBillNoLayout = new javax.swing.GroupLayout(panelBillNo);
        panelBillNo.setLayout(panelBillNoLayout);
        panelBillNoLayout.setHorizontalGroup(
            panelBillNoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBillNoLayout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(panelBillNoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBillNoLayout.createSequentialGroup()
                        .addComponent(cmbZone, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cmbArea, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelBillNoLayout.createSequentialGroup()
                        .addGroup(panelBillNoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnBillNoTable10, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnBillNoTable7, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnBillNoTable4, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnBillNoTable1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnBillNoTable13, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnBillNoPrev, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(27, 27, 27)
                        .addGroup(panelBillNoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelBillNoLayout.createSequentialGroup()
                                .addGroup(panelBillNoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(btnBillNoTable2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnBillNoTable5, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnBillNoTable8, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnBillNoTable11, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(27, 27, 27)
                                .addGroup(panelBillNoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnBillNoTable3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnBillNoTable6, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnBillNoTable9, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnBillNoTable12, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(panelBillNoLayout.createSequentialGroup()
                                .addGroup(panelBillNoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(btnBillNoTable14, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblBillNoList, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(27, 27, 27)
                                .addGroup(panelBillNoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(btnBillNoNext, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnBillNoTable15, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap())
        );

        panelBillNoLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cmbArea, cmbZone});

        panelBillNoLayout.setVerticalGroup(
            panelBillNoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBillNoLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(panelBillNoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbArea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbZone, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelBillNoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnBillNoTable3, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBillNoTable2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBillNoTable1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelBillNoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnBillNoTable6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBillNoTable5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBillNoTable4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelBillNoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnBillNoTable9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBillNoTable8, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBillNoTable7, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelBillNoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnBillNoTable11, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBillNoTable12, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBillNoTable10, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelBillNoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnBillNoTable15, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBillNoTable14, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBillNoTable13, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelBillNoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnBillNoPrev, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelBillNoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnBillNoNext, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblBillNoList, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelBillNoLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {cmbArea, cmbZone});

        panelBodt.add(panelBillNo);
        panelBillNo.setBounds(400, 0, 400, 470);

        javax.swing.GroupLayout panelBodyRootLayout = new javax.swing.GroupLayout(panelBodyRoot);
        panelBodyRoot.setLayout(panelBodyRootLayout);
        panelBodyRootLayout.setHorizontalGroup(
            panelBodyRootLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBodyRootLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(panelBodt, javax.swing.GroupLayout.PREFERRED_SIZE, 799, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        panelBodyRootLayout.setVerticalGroup(
            panelBodyRootLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBodyRootLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(panelBodt, javax.swing.GroupLayout.PREFERRED_SIZE, 570, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        panelLayout.add(panelBodyRoot, new java.awt.GridBagConstraints());

        getContentPane().add(panelLayout, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseClicked
        // TODO add your handling code here:
        dispose();
        clsGlobalVarClass.hmActiveForms.remove("AssignHomeDelivery");
    }//GEN-LAST:event_btnCancelMouseClicked

    private void btnSaveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSaveMouseClicked

        funInsertDelBoyBillDtl();
    }//GEN-LAST:event_btnSaveMouseClicked

    private void btnDelBoyPrevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelBoyPrevActionPerformed

        funDelBoyPrevButtonPressed();
    }//GEN-LAST:event_btnDelBoyPrevActionPerformed

    private void btnBillNoPrevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBillNoPrevActionPerformed

        funBillNoPrevButtonPressed();
    }//GEN-LAST:event_btnBillNoPrevActionPerformed

    private void btnBillNoNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBillNoNextActionPerformed

        funBillNoNextButtonPressed();
    }//GEN-LAST:event_btnBillNoNextActionPerformed

    private void btnDelBoyTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDelBoyTable1MouseClicked

        JButton buttonClicked = (JButton) evt.getComponent();
        //System.out.println("Button Clicked"+buttonClicked);
        funAddDelBoysToMap(buttonClicked);
    }//GEN-LAST:event_btnDelBoyTable1MouseClicked

    private void btnDelBoyTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDelBoyTable2MouseClicked

        JButton buttonClicked = (JButton) evt.getComponent();
        //System.out.println("Button Clicked"+buttonClicked);
        funAddDelBoysToMap(buttonClicked);
    }//GEN-LAST:event_btnDelBoyTable2MouseClicked

    private void btnDelBoyTable3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDelBoyTable3MouseClicked

        JButton buttonClicked = (JButton) evt.getComponent();
        //System.out.println("Button Clicked"+buttonClicked);
        funAddDelBoysToMap(buttonClicked);
    }//GEN-LAST:event_btnDelBoyTable3MouseClicked

    private void btnDelBoyTable4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDelBoyTable4MouseClicked

        JButton buttonClicked = (JButton) evt.getComponent();
        //System.out.println("Button Clicked"+buttonClicked);
        funAddDelBoysToMap(buttonClicked);
    }//GEN-LAST:event_btnDelBoyTable4MouseClicked

    private void btnDelBoyTable5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDelBoyTable5MouseClicked

        JButton buttonClicked = (JButton) evt.getComponent();
        //System.out.println("Button Clicked"+buttonClicked);
        funAddDelBoysToMap(buttonClicked);
    }//GEN-LAST:event_btnDelBoyTable5MouseClicked

    private void btnDelBoyTable6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDelBoyTable6MouseClicked

        JButton buttonClicked = (JButton) evt.getComponent();
        // System.out.println("Button Clicked"+buttonClicked);
        funAddDelBoysToMap(buttonClicked);
    }//GEN-LAST:event_btnDelBoyTable6MouseClicked

    private void btnDelBoyTable7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDelBoyTable7MouseClicked

        JButton buttonClicked = (JButton) evt.getComponent();
        //System.out.println("Button Clicked"+buttonClicked);
        funAddDelBoysToMap(buttonClicked);
    }//GEN-LAST:event_btnDelBoyTable7MouseClicked

    private void btnDelBoyTable8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDelBoyTable8MouseClicked

        JButton buttonClicked = (JButton) evt.getComponent();
        //System.out.println("Button Clicked"+buttonClicked);
        funAddDelBoysToMap(buttonClicked);
    }//GEN-LAST:event_btnDelBoyTable8MouseClicked

    private void btnDelBoyTable9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDelBoyTable9MouseClicked

        JButton buttonClicked = (JButton) evt.getComponent();
        //System.out.println("Button Clicked"+buttonClicked);
        funAddDelBoysToMap(buttonClicked);
    }//GEN-LAST:event_btnDelBoyTable9MouseClicked

    private void btnDelBoyTable10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDelBoyTable10MouseClicked

        JButton buttonClicked = (JButton) evt.getComponent();
        // System.out.println("Button Clicked"+buttonClicked);
        funAddDelBoysToMap(buttonClicked);
    }//GEN-LAST:event_btnDelBoyTable10MouseClicked

    private void btnDelBoyTable11MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDelBoyTable11MouseClicked

        JButton buttonClicked = (JButton) evt.getComponent();
        //System.out.println("Button Clicked"+buttonClicked);
        funAddDelBoysToMap(buttonClicked);
    }//GEN-LAST:event_btnDelBoyTable11MouseClicked

    private void btnDelBoyTable12MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDelBoyTable12MouseClicked

        JButton buttonClicked = (JButton) evt.getComponent();
        // System.out.println("Button Clicked"+buttonClicked);
        funAddDelBoysToMap(buttonClicked);
    }//GEN-LAST:event_btnDelBoyTable12MouseClicked

    private void btnDelBoyTable13MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDelBoyTable13MouseClicked

        JButton buttonClicked = (JButton) evt.getComponent();
        //System.out.println("Button Clicked"+buttonClicked);
        funAddDelBoysToMap(buttonClicked);
    }//GEN-LAST:event_btnDelBoyTable13MouseClicked

    private void btnDelBoyTable14MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDelBoyTable14MouseClicked

        JButton buttonClicked = (JButton) evt.getComponent();
        //System.out.println("Button Clicked"+buttonClicked);
        funAddDelBoysToMap(buttonClicked);
    }//GEN-LAST:event_btnDelBoyTable14MouseClicked

    private void btnDelBoyTable15MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDelBoyTable15MouseClicked

        JButton buttonClicked = (JButton) evt.getComponent();
        //System.out.println("Button Clicked"+buttonClicked);
        funAddDelBoysToMap(buttonClicked);
    }//GEN-LAST:event_btnDelBoyTable15MouseClicked

    private void btnBillNoTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBillNoTable1MouseClicked

        JButton buttonClicked = (JButton) evt.getComponent();
        // System.out.println("Button Clicked"+buttonClicked);
        funAddToBillNoMap(buttonClicked);
    }//GEN-LAST:event_btnBillNoTable1MouseClicked

    private void btnBillNoTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBillNoTable2MouseClicked

        JButton buttonClicked = (JButton) evt.getComponent();
        // System.out.println("Button Clicked"+buttonClicked);
        funAddToBillNoMap(buttonClicked);
    }//GEN-LAST:event_btnBillNoTable2MouseClicked

    private void btnBillNoTable3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBillNoTable3MouseClicked

        JButton buttonClicked = (JButton) evt.getComponent();
        //System.out.println("Button Clicked"+buttonClicked);
        funAddToBillNoMap(buttonClicked);
    }//GEN-LAST:event_btnBillNoTable3MouseClicked

    private void btnBillNoTable4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBillNoTable4MouseClicked

        JButton buttonClicked = (JButton) evt.getComponent();
        //System.out.println("Button Clicked"+buttonClicked);
        funAddToBillNoMap(buttonClicked);
    }//GEN-LAST:event_btnBillNoTable4MouseClicked

    private void btnBillNoTable5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBillNoTable5MouseClicked

        JButton buttonClicked = (JButton) evt.getComponent();
        //System.out.println("Button Clicked"+buttonClicked);
        funAddToBillNoMap(buttonClicked);
    }//GEN-LAST:event_btnBillNoTable5MouseClicked

    private void btnBillNoTable6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBillNoTable6MouseClicked

        JButton buttonClicked = (JButton) evt.getComponent();
        //System.out.println("Button Clicked"+buttonClicked);
        funAddToBillNoMap(buttonClicked);
    }//GEN-LAST:event_btnBillNoTable6MouseClicked

    private void btnBillNoTable7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBillNoTable7MouseClicked

        JButton buttonClicked = (JButton) evt.getComponent();
        // System.out.println("Button Clicked"+buttonClicked);
        funAddToBillNoMap(buttonClicked);
    }//GEN-LAST:event_btnBillNoTable7MouseClicked

    private void btnBillNoTable8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBillNoTable8MouseClicked

        JButton buttonClicked = (JButton) evt.getComponent();
        //  System.out.println("Button Clicked"+buttonClicked);
        funAddToBillNoMap(buttonClicked);
    }//GEN-LAST:event_btnBillNoTable8MouseClicked

    private void btnBillNoTable9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBillNoTable9MouseClicked

        JButton buttonClicked = (JButton) evt.getComponent();
        // System.out.println("Button Clicked"+buttonClicked);
        funAddToBillNoMap(buttonClicked);
    }//GEN-LAST:event_btnBillNoTable9MouseClicked

    private void btnBillNoTable10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBillNoTable10MouseClicked

        JButton buttonClicked = (JButton) evt.getComponent();
        // System.out.println("Button Clicked"+buttonClicked);
        funAddToBillNoMap(buttonClicked);
    }//GEN-LAST:event_btnBillNoTable10MouseClicked

    private void btnBillNoTable11MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBillNoTable11MouseClicked

        JButton buttonClicked = (JButton) evt.getComponent();
        //System.out.println("Button Clicked"+buttonClicked);
        funAddToBillNoMap(buttonClicked);
    }//GEN-LAST:event_btnBillNoTable11MouseClicked

    private void btnBillNoTable12MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBillNoTable12MouseClicked

        JButton buttonClicked = (JButton) evt.getComponent();
        // System.out.println("Button Clicked"+buttonClicked);
        funAddToBillNoMap(buttonClicked);
    }//GEN-LAST:event_btnBillNoTable12MouseClicked

    private void btnBillNoTable13MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBillNoTable13MouseClicked

        JButton buttonClicked = (JButton) evt.getComponent();
        // System.out.println("Button Clicked"+buttonClicked);
        funAddToBillNoMap(buttonClicked);
    }//GEN-LAST:event_btnBillNoTable13MouseClicked

    private void btnBillNoTable14MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBillNoTable14MouseClicked

        JButton buttonClicked = (JButton) evt.getComponent();
        // System.out.println("Button Clicked"+buttonClicked);
        funAddToBillNoMap(buttonClicked);
    }//GEN-LAST:event_btnBillNoTable14MouseClicked

    private void btnBillNoTable15MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBillNoTable15MouseClicked

        JButton buttonClicked = (JButton) evt.getComponent();
        // System.out.println("Button Clicked"+buttonClicked);
        funAddToBillNoMap(buttonClicked);
    }//GEN-LAST:event_btnBillNoTable15MouseClicked

    private void btnDelBoyNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelBoyNextActionPerformed

        funDelBoyNextButtonPressed();
    }//GEN-LAST:event_btnDelBoyNextActionPerformed

    private void cmbZoneActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbZoneActionPerformed
    {//GEN-HEADEREND:event_cmbZoneActionPerformed

//                String zoneCode=cmbZone.getSelectedItem().toString().split("!")[1].trim();   
//                funFillComboAreas(zoneCode);

    }//GEN-LAST:event_cmbZoneActionPerformed

    private void cmbAreaActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbAreaActionPerformed
    {//GEN-HEADEREND:event_cmbAreaActionPerformed
//      String areaCode=cmbArea.getSelectedItem().toString().split("!")[1].trim();
//      
//      funFillTableVector();
    }//GEN-LAST:event_cmbAreaActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosed
    {//GEN-HEADEREND:event_formWindowClosed
        clsGlobalVarClass.hmActiveForms.remove("AssignHomeDelivery");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosing
    {//GEN-HEADEREND:event_formWindowClosing
        clsGlobalVarClass.hmActiveForms.remove("AssignHomeDelivery");
    }//GEN-LAST:event_formWindowClosing

    private void txtBillLooseCashAmountMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtBillLooseCashAmountMouseClicked
    {//GEN-HEADEREND:event_txtBillLooseCashAmountMouseClicked
        
        frmNumberKeyPad num = new frmNumberKeyPad(this, true, "Loose Cash Amount");
        num.setVisible(true);        
        if (null != clsGlobalVarClass.gNumerickeyboardValue)
        {
            txtBillLooseCashAmount.setText(String.valueOf(Double.parseDouble(clsGlobalVarClass.gNumerickeyboardValue)));
            clsGlobalVarClass.gNumerickeyboardValue = null;
        }
    }//GEN-LAST:event_txtBillLooseCashAmountMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBillNoNext;
    private javax.swing.JButton btnBillNoPrev;
    private javax.swing.JButton btnBillNoTable1;
    private javax.swing.JButton btnBillNoTable10;
    private javax.swing.JButton btnBillNoTable11;
    private javax.swing.JButton btnBillNoTable12;
    private javax.swing.JButton btnBillNoTable13;
    private javax.swing.JButton btnBillNoTable14;
    private javax.swing.JButton btnBillNoTable15;
    private javax.swing.JButton btnBillNoTable2;
    private javax.swing.JButton btnBillNoTable3;
    private javax.swing.JButton btnBillNoTable4;
    private javax.swing.JButton btnBillNoTable5;
    private javax.swing.JButton btnBillNoTable6;
    private javax.swing.JButton btnBillNoTable7;
    private javax.swing.JButton btnBillNoTable8;
    private javax.swing.JButton btnBillNoTable9;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnDelBoyNext;
    private javax.swing.JButton btnDelBoyPrev;
    private javax.swing.JButton btnDelBoyTable1;
    private javax.swing.JButton btnDelBoyTable10;
    private javax.swing.JButton btnDelBoyTable11;
    private javax.swing.JButton btnDelBoyTable12;
    private javax.swing.JButton btnDelBoyTable13;
    private javax.swing.JButton btnDelBoyTable14;
    private javax.swing.JButton btnDelBoyTable15;
    private javax.swing.JButton btnDelBoyTable2;
    private javax.swing.JButton btnDelBoyTable3;
    private javax.swing.JButton btnDelBoyTable4;
    private javax.swing.JButton btnDelBoyTable5;
    private javax.swing.JButton btnDelBoyTable6;
    private javax.swing.JButton btnDelBoyTable7;
    private javax.swing.JButton btnDelBoyTable8;
    private javax.swing.JButton btnDelBoyTable9;
    private javax.swing.JButton btnSave;
    private javax.swing.JComboBox cmbArea;
    private javax.swing.JComboBox cmbZone;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JLabel lblBillAmountName;
    private javax.swing.JLabel lblBillAmountValue;
    private javax.swing.JLabel lblBillLooseCashName;
    private javax.swing.JLabel lblBillNoList;
    private javax.swing.JLabel lblBillNoNames;
    private javax.swing.JLabel lblBillNos;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblDelBoyList;
    private javax.swing.JLabel lblDelBoyNames;
    private javax.swing.JLabel lblDelBoys;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JPanel panelBillNo;
    private javax.swing.JPanel panelBodt;
    private javax.swing.JPanel panelBodyRoot;
    private javax.swing.JPanel panelBottom;
    private javax.swing.JPanel panelDeliveryBoy;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelLayout;
    private javax.swing.JTextField txtBillLooseCashAmount;
    // End of variables declaration//GEN-END:variables

    private void funFillComboZones()
    {
        try
        {
            String sql = "select a.strZoneCode,a.strZoneName from tblzonemaster a";
            ResultSet resultSet = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            cmbZone.addItem("All Zone                                                     !All");
            while (resultSet.next())
            {
                cmbZone.addItem(resultSet.getString("strZoneName") + "                                                     !" + resultSet.getString("strZoneCode"));
            }
        }
        catch (Exception e)
        {
            clsGlobalVarClass.gLog.error(e);
        }
    }

    private void funFillComboAreas(String zoneCode)
    {
        try
        {
            String sql = "select b.strBuildingCode,b.strBuildingName from tblbuildingmaster b ";
            if (!zoneCode.equalsIgnoreCase("All"))
            {
                sql = sql + " where b.strZoneCode='" + zoneCode + "' ";
            }
            ResultSet resultSet = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            funRemoveAreaItems();
            while (resultSet.next())
            {
                cmbArea.addItem(resultSet.getString("strBuildingName") + "                                                     !" + resultSet.getString("strBuildingCode"));
            }

            funFillBillAndDelBoy();
        }
        catch (Exception e)
        {
            clsGlobalVarClass.gLog.error(e);
        }
    }

    private void funRemoveAreaItems()
    {
        cmbArea.removeAllItems();
        cmbArea.addItem("All Area                                                     !All");
    }

    private void funSetBillAmountAndLooseCash(String billNo)
    {
        try
        {

            lblBillAmountValue.setText("0.00");
            txtBillLooseCashAmount.setText("0.00");

            String sql = "select a.strBillNo,a.dblGrandTotal from tblbillhd a where a.strBillNo='" + billNo + "'; ";
            ResultSet resultSet = clsGlobalVarClass.dbMysql.executeResultSet(sql);
            if (resultSet.next())
            {
                lblBillAmountValue.setText(resultSet.getString(2));
                txtBillLooseCashAmount.setText("0.00");
            }
            resultSet.close();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
