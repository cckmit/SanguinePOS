/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSTransaction.view;

import com.POSGlobal.controller.clsBillDtl;
import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.view.frmOkPopUp;
import com.POSReport.controller.clsCostCenterBean;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.TextAttribute;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
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
import sun.audio.AudioData;
import sun.audio.AudioDataStream;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import sun.audio.ContinuousAudioDataStream;

public class frmKDSForKOT1366x768Resolution extends javax.swing.JFrame
{

    private JScrollPane scrollPaneArray[], scrollPaneArrayForMenuHead[];
    private JList listViewArray[], listViewArrayForMenuHead[];
    private LinkedHashMap<String, ArrayList<clsBillDtl>> mapKOTHd;
    private LinkedHashMap<String, Map<String, clsBillDtl>> mapMenuHd;
    private LinkedHashMap<String, ArrayList<clsBillDtl>> mapCountKOTSize;
    private ArrayList<ArrayList<clsBillDtl>> listOfKOTs;
    private ArrayList<ArrayList<clsBillDtl>> listOfMenus;
    private int navigatorNew = 0;
    private int navigator = 0, navigatorForMenuHead = 0;
    //private String gBillNo="";    
    private int startIndex = 0;
    private int endIndex = 0;
    private ArrayList<String> listOfKOTsToBeProcess;
    //private String gBillDateTime="";
    //private final JLabel lblBillNoArray[];
    private final JLabel[] lblKOTDelayArray;
    private final JLabel[] lblTableAndKOTNoArray, lblTableAndKOTNoArrayForMenuHead;
    private int selectedIndexForItemProcessed = -1;
    private Map<String, String> mapSelectedKOTs;
    private Map<String, String> mapSelectedItems;

    private List<clsCostCenterBean> listOfSelectedCostCenters;
    private final Timer refreshTimer;
    private int gITEMCOUNTER;
    private Timer delayTimer;

    public frmKDSForKOT1366x768Resolution()
    {
	////////////////////////////
	initComponents();

	scrollPaneArray = new JScrollPane[]
	{
	    scrollPane15, scrollPane14, scrollPane13, scrollPane12, scrollPane11, scrollPane10, scrollPane9, scrollPane8, scrollPane7, scrollPane6, scrollPane5, scrollPane4, scrollPane3, scrollPane2, scrollPane1
	};
	listViewArray = new JList[]
	{
	    list15, list14, list13, list12, list11, list10, list9, list8, list7, list6, list5, list4, list3, list2, list1
	};

	lblKOTDelayArray = new JLabel[]
	{
	    lblBillDelay15, lblBillDelay14, lblBillDelay13, lblBillDelay12, lblBillDelay11, lblBillDelay10, lblBillDelay9, lblBillDelay8, lblBillDelay7, lblBillDelay6, lblBillDelay5, lblBillDelay4, lblBillDelay3, lblBillDelay2, lblBillDelay1
	};

	/**
	 * table and kot no lables
	 */
	lblTableAndKOTNoArray = new JLabel[]
	{
	    lblTableAndKOTNo15, lblTableAndKOTNo14, lblTableAndKOTNo13, lblTableAndKOTNo12, lblTableAndKOTNo11, lblTableAndKOTNo10, lblTableAndKOTNo9, lblTableAndKOTNo8, lblTableAndKOTNo7, lblTableAndKOTNo6, lblTableAndKOTNo5, lblTableAndKOTNo4, lblTableAndKOTNo3, lblTableAndKOTNo2, lblTableAndKOTNo1
	};

	/**
	 * menu head
	 */
	scrollPaneArrayForMenuHead = new JScrollPane[]
	{
	    scrollPane16, scrollPane17, scrollPane18, scrollPane19, scrollPane20, scrollPane21, scrollPane22, scrollPane23, scrollPane24, scrollPane25
	};
	listViewArrayForMenuHead = new JList[]
	{
	    list16, list17, list18, list19, list20, list21, list22, list23, list24, list25
	};

	/**
	 * table and kot no lables
	 */
	lblTableAndKOTNoArrayForMenuHead = new JLabel[]
	{
	    lblTableAndKOTNo16, lblTableAndKOTNo17, lblTableAndKOTNo18, lblTableAndKOTNo19, lblTableAndKOTNo20, lblTableAndKOTNo21, lblTableAndKOTNo22, lblTableAndKOTNo23, lblTableAndKOTNo24, lblTableAndKOTNo25
	};
	/**
	 * end for menu head
	 */

	mapKOTHd = new LinkedHashMap();
	mapMenuHd = new LinkedHashMap();
	mapCountKOTSize = new LinkedHashMap();
	listOfKOTs = new ArrayList<ArrayList<clsBillDtl>>();
	listOfMenus = new ArrayList<ArrayList<clsBillDtl>>();
	listOfKOTsToBeProcess = new ArrayList<String>();
	mapSelectedKOTs = new HashMap<>();
	mapSelectedItems = new HashMap<>();

	funRefreshForm();
	funSetBillDelayTimer();

	refreshTimer = new Timer(3000, new ActionListener()
	{
	    @Override
	    public void actionPerformed(ActionEvent e)
	    {
		int oldBillSize = mapKOTHd.size();

//		if (tabbedPaneKDS.getSelectedIndex() == 0)
//		{
//		    tabbedPaneKDS.setSelectedIndex(1);
//		}
//		else
//		{
//		    tabbedPaneKDS.setSelectedIndex(0);
//		}
		funRefreshForm();
	    }
	});
	refreshTimer.setRepeats(true);
	refreshTimer.setCoalesce(true);
	refreshTimer.setInitialDelay(0);
	refreshTimer.start();

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
	endIndex = listOfKOTs.size() - (navigator * 15) - 1;
	if ((listOfKOTs.size() - (navigator * 15) - 1) == 0)
	{
	    btnOld.setEnabled(false);
	}
	if (endIndex > 14)
	{
	    funLoadScrollPanes(0, 14);
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
	    refreshTimer.start();
	    btnNew.setEnabled(false);
	}

	funLoadScrollPanes(0, 14);
    }

    private void funButtonOrderProcessClicked()
    {
	try
	{
	    StringBuilder sqlBillOrderProcess = new StringBuilder();
	    sqlBillOrderProcess.setLength(0);
	    //  sqlBillOrderProcess.append("insert into tblkdsprocess values");

	    if (mapKOTHd.size() > 0)
	    {
		String selectedDOCNo = lblSelectedKOT.getText();
		if (mapKOTHd.containsKey(selectedDOCNo))
		{
		    ArrayList<clsBillDtl> arrSelectedKotItemList = mapKOTHd.get(selectedDOCNo);

		    for (int cnt = 0; cnt < arrSelectedKotItemList.size(); cnt++)
		    {
			clsBillDtl objBillDtl = arrSelectedKotItemList.get(cnt);

			if (selectedDOCNo.startsWith("KT"))
			{
			    String[] currentDateTime = clsGlobalVarClass.getCurrentDateTime().split(" ");

			    String updateSql = "";
			    if (objBillDtl.getStrRemark().equals("Void"))
			    {
				updateSql = "update tblvoidkot  set strItemProcessed='Y' "
					+ "where strKOTNo='" + lblSelectedKOT.getText() + "' and strItemCode='" + objBillDtl.getStrItemCode() + "' ";
			    }
			    else
			    {
				updateSql = "update tblitemrtemp  set strItemProcessed='Y',tmeOrderProcessing='" + currentDateTime[1] + "' "
					+ "where strKOTNo='" + lblSelectedKOT.getText() + "' and strItemCode='" + objBillDtl.getStrItemCode() + "' ";
			    }
			    clsGlobalVarClass.dbMysql.execute(updateSql);
			}
			else
			{
			    String deleteQuery = " delete from tblkdsprocess where strKDSName='BILL' and "
				    + " strDocNo='" + lblSelectedKOT.getText() + "' and strItemCode='" + objBillDtl.getStrItemCode() + "' ";
			    clsGlobalVarClass.dbMysql.execute(deleteQuery);

			    if (cnt == 0)
			    {
				sqlBillOrderProcess.append("('" + lblSelectedKOT.getText() + "','P','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.gUserCode + "','BILL','" + objBillDtl.getStrItemCode() + "','','','" + objBillDtl.getDteBillDate() + "')");
			    }
			    else
			    {
				sqlBillOrderProcess.append(",('" + lblSelectedKOT.getText() + "','P','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.gUserCode + "','BILL','" + objBillDtl.getStrItemCode() + "','','','" + objBillDtl.getDteBillDate() + "')");
			    }
			}
		    }
		    if (!selectedDOCNo.startsWith("KT") && sqlBillOrderProcess.length() > 0)//bill			
		    {
			clsGlobalVarClass.dbMysql.execute(" insert into tblkdsprocess values " + sqlBillOrderProcess.toString());
		    }
		}

		if (selectedDOCNo.startsWith("KT") && sqlBillOrderProcess.length() > 0)//bill			
		{
		    new frmOkPopUp(null, "KOT Process Successfully.", "Successfull", 3).setVisible(true);
		}
		else
		{
		    new frmOkPopUp(null, "Bill Process Successfully.", "Successfull", 3).setVisible(true);
		}
	    }

	    listOfKOTsToBeProcess.clear();

	    mapSelectedKOTs.clear();
	    for (int i = 0; i < lblTableAndKOTNoArray.length; i++)
	    {
		lblTableAndKOTNoArray[i].setForeground(Color.BLACK);

		btnKOTProcess.setEnabled(false);
		lblSelectedKOT.setText("KOT");
	    }

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
	fumLoadMapKOTlHd();
	funLoadBillArrayList();
	if (mapKOTHd.size() > 14)
	{
	    if (mapKOTHd.size() > 15)
	    {
		btnOld.setEnabled(true);
	    }
	    funLoadScrollPanes(0, 14);
	}
	else if (mapKOTHd.size() > 0)
	{
	    funLoadScrollPanes(0, mapKOTHd.size() - 1);
	}

	System.gc();
    }

    private void funPlayNewOrderNotificationAlert()
    {
	try
	{
	    AudioPlayer audioPlayer = AudioPlayer.player;
	    String path = getClass().getResource("/com/POSTransaction/images/notificationXperiaForNewOrder.wav").getPath();

	    //FileInputStream fis = new FileInputStream(new File(System.getProperty("user.dir")+"//src//com//spos//images//notificationXperiaForNewOrder.wav"));
	    InputStream is = frmWeraFoodOrders.class.getResourceAsStream("/com/POSTransaction/images/notificationXperiaForNewOrder.wav");

	    //FileInputStream fis = new FileInputStream(new File(path));
	    AudioStream as = new AudioStream(is); // header plus audio data
	    AudioData ad = as.getData(); // audio data only, no header
	    AudioDataStream audioDataStream = new AudioDataStream(ad);
	    ContinuousAudioDataStream continuousAudioDataStream = new ContinuousAudioDataStream(ad);

	    audioPlayer.start(audioDataStream);
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funPlayProcessNotificationAlert()
    {
	try
	{
	    AudioPlayer audioPlayer = AudioPlayer.player;

	    //FileInputStream fis = new FileInputStream(new File(System.getProperty("user.dir")+"//src//com//spos//images//notificationXperiaForNewOrder.wav"));
	    InputStream is = frmWeraFoodOrders.class.getResourceAsStream("/com/POSTransaction/images/notificationAlert2.wav");

	    //FileInputStream fis = new FileInputStream(new File(path));
	    AudioStream as = new AudioStream(is); // header plus audio data
	    AudioData ad = as.getData(); // audio data only, no header
	    AudioDataStream audioDataStream = new AudioDataStream(ad);
	    ContinuousAudioDataStream continuousAudioDataStream = new ContinuousAudioDataStream(ad);

	    audioPlayer.start(audioDataStream);
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
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

	delayTimer = new Timer(1000, new ActionListener()
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

		    for (int i = 0; i < 15; i++)
		    {
			JScrollPane scrollPane = scrollPaneArray[i];
			if (scrollPane.isVisible())
			{

			    Date delay = df.parse(listOfKOTs.get((navigator * 15) + i).get(0).getDteNCKOTDate());
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
			    if (mm > 0)
			    {
				if (mm < 10)
				{
				    displayDelayTime.append("0" + mm + ":");
				}
				else
				{
				    displayDelayTime.append(mm + ":");
				}

			    }
			    if (mm == 0)
			    {
				displayDelayTime.append("00:");
			    }
			    if (ss > 0)
			    {
				if (ss < 10)
				{
				    displayDelayTime.append("0" + ss);
				}
				else
				{
				    displayDelayTime.append(ss);
				}

			    }
			    if (ss == 0)
			    {
				displayDelayTime.append("00");
			    }
			    //displayDelayTime.append(ss);

			    lblKOTDelayArray[i].setText(displayDelayTime.toString());
			}
		    }
		}
		catch (ParseException pe)
		{
		    pe.printStackTrace();
		}
	    }
	});
	delayTimer.setRepeats(true);
	delayTimer.setCoalesce(true);
	delayTimer.setInitialDelay(0);
	delayTimer.start();
    }

    private void funScrollPaneListClicked(int scrollPaneIndex)
    {
	selectedIndexForItemProcessed = scrollPaneIndex;

	JScrollPane selectedScrollPane = scrollPaneArray[scrollPaneIndex];
	DefaultListModel listModel = (DefaultListModel) listViewArray[scrollPaneIndex].getModel();

	JList list = listViewArray[scrollPaneIndex];
	int[] arrIndices = list.getSelectedIndices();

	StringBuilder itemsBuilder = new StringBuilder();
	for (int i = 0; i < arrIndices.length; i++)
	{
	    StringBuilder itemNameBuilder = new StringBuilder();
	    String object = list.getModel().getElementAt(arrIndices[i]).toString();
	    String[] obj = (String[]) object.split(" ");
	    for (int j = 1; j < obj.length; j++)
	    {
		itemNameBuilder.append(obj[j] + " ");
	    }
	    itemNameBuilder.toString().trim();
	    itemsBuilder.append(",'" + itemNameBuilder + "'");

	}
	String kotNo = listOfKOTs.get(scrollPaneIndex).get(0).getStrKOTNo();
	String items = itemsBuilder.substring(2, itemsBuilder.length());

	if (mapSelectedItems.size() > 0 && !mapSelectedItems.containsKey(items))
	{
	    mapSelectedItems.clear();

	    btnItemProcessed.setEnabled(false);
	    lblSelectedItem.setText("ITEM");

	    btnOld.setEnabled(true);
	    btnNew.setEnabled(true);

	}

	btnItemProcessed.setEnabled(true);

	String[] arrItem = items.split("!");
	if (arrItem[2].contains("Void"))
	{
	    lblSelectedItem.setText("Voided Item: " + arrItem[0]);
	}
	else
	{
	    lblSelectedItem.setText("Selected Items: " + arrItem[0]);
	}

	mapSelectedItems.put(items, kotNo);

	btnOld.setEnabled(false);
	btnNew.setEnabled(false);

    }

    private boolean funProcessItem(int scrollPaneIndex)
    {
	try
	{
	    for (Map.Entry<String, String> entry : mapSelectedItems.entrySet())
	    {
		String item = entry.getKey();
		String kotNo = entry.getValue();
		String itemType = "", waiterNo = "", kotDateTime = "", itemCode = "";

		String[] arrItem = item.split("!");
		item = arrItem[0];
		itemType = arrItem[2];
		waiterNo = arrItem[3];
		kotDateTime = arrItem[4];
		itemCode = arrItem[5];
		String[] currentDateTime = clsGlobalVarClass.getCurrentDateTime().split(" ");

		if (kotNo.startsWith("KT"))
		{
		    String updateSql = "";
		    if (itemType.equals("Void"))
		    {
			updateSql = "update tblvoidkot  set strItemProcessed='Y' "
				+ "where strKOTNo='" + kotNo + "' and strItemCode='" + itemCode + "'  ";
		    }
		    else
		    {
			updateSql = "update tblitemrtemp  set strItemProcessed='Y',tmeOrderProcessing='" + currentDateTime[1] + "' "
				+ "where strKOTNo='" + kotNo + "' and strItemCode='" + itemCode + "'   ";
		    }
		    clsGlobalVarClass.dbMysql.execute(updateSql);
		}
		else
		{
		    StringBuilder sqlBillOrderProcess = new StringBuilder();
		    sqlBillOrderProcess.setLength(0);
		    sqlBillOrderProcess.append("insert into tblkdsprocess values");

		    String deleteQuery = "delete from tblkdsprocess  where strKDSName='BILL' and "
			    + " strDocNo='" + kotNo + "' and strItemCode='" + itemCode + "' ";
		    clsGlobalVarClass.dbMysql.execute(deleteQuery);

		    sqlBillOrderProcess.append("('" + kotNo + "','P','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.gUserCode + "','BILL','" + itemCode + "','','','" + clsGlobalVarClass.getCurrentDateTime() + "')");
		    clsGlobalVarClass.dbMysql.execute(sqlBillOrderProcess.toString());

		}
	    }

	    mapSelectedItems.clear();
	    lblSelectedItem.setText("ITEM");
	    btnItemProcessed.setEnabled(false);
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}

	return true;
    }

    private void funTableAndKOTLabelClicked(int index)
    {
	String kotNo = listOfKOTs.get((navigator * 15) + index).get(0).getStrKOTNo();

	if (kotNo.startsWith("KT"))
	{
	    btnKOTProcess.setText("KOT Process");
	}
	else
	{
	    btnKOTProcess.setText("Bill Process");
	}

	if (mapSelectedKOTs.size() > 0 && !mapSelectedKOTs.containsKey(kotNo))
	{
	    mapSelectedKOTs.clear();
	    for (int i = 0; i < lblTableAndKOTNoArray.length; i++)
	    {
		lblTableAndKOTNoArray[i].setForeground(Color.BLACK);

		btnKOTProcess.setEnabled(false);
		lblSelectedKOT.setText("KOT");

		btnOld.setEnabled(true);
		btnNew.setEnabled(true);
	    }
	}

	if (lblTableAndKOTNoArray[index].getForeground() == Color.BLACK)
	{
	    lblTableAndKOTNoArray[index].setForeground(Color.BLUE);
	    btnKOTProcess.setEnabled(true);
	    lblSelectedKOT.setText(kotNo);

	    mapSelectedKOTs.put(kotNo, kotNo);

	    btnOld.setEnabled(false);
	    btnNew.setEnabled(false);
	}
	else
	{
	    lblTableAndKOTNoArray[index].setForeground(Color.BLACK);
	    btnKOTProcess.setEnabled(false);
	    lblSelectedKOT.setText("KOT");

	    mapSelectedKOTs.remove(kotNo);

	    btnOld.setEnabled(true);
	    btnNew.setEnabled(true);
	}
    }

    private void funCloseKDS()
    {
	dispose();
	clsGlobalVarClass.hmActiveForms.remove("KDSForKOTBookAndProcess");
	refreshTimer.stop();
	delayTimer.stop();
    }

    private void funTabbChanged()
    {
	/**
	 * fill items menuhead wise
	 */
	if (tabbedPaneKDS.getSelectedIndex() == 1)//Quantity tab
	{

	    mapMenuHd.clear();
	    listOfMenus.clear();
	    navigatorForMenuHead = 0;

	    try
	    {

		String posDate = clsGlobalVarClass.gPOSDateForTransaction.split(" ")[0];

		String sqlBillDtl = "(\n"
			+ "SELECT d.strMenuCode,f.strMenuName,a.strItemCode,a.strItemName, SUM(a.dblItemQuantity)dblItemQuantity\n"
			+ "FROM tblitemrtemp a,tbltablemaster b,tblitemmaster c,tblmenuitempricingdtl d,tblcostcentermaster e,tblmenuhd f\n"
			+ "WHERE\n"
			+ "LEFT(a.strItemCode,7)=c.strItemCode  \n"
			+ "AND a.strNCKotYN='N' AND a.tdhComboItemYN='N' \n"
			+ "AND a.strTableNo=b.strTableNo \n"
			+ "AND a.strItemProcessed='N' \n"
			+ "AND c.strItemCode=d.strItemCode \n"
			+ "AND a.strPOSCode=d.strPosCode \n"
			+ "AND (d.strPosCode=a.strPosCode OR d.strPosCode='All') \n"
			+ "AND d.strCostCenterCode=e.strCostCenterCode \n"
			+ "and d.strMenuCode=f.strMenuCode\n"
			+ "AND e.strCostCenterCode IN " + funGetCostCenterCodes() + " "
			+ "and a.strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
			+ "GROUP BY f.strMenuName,a.strItemCode,a.strItemName\n"
			+ "ORDER BY f.strMenuName)\n"
			+ "UNION ALL\n"
			+ "(\n"
			+ "SELECT  d.strMenuCode,f.strMenuName,a.strItemCode,a.strItemName, SUM(a.dblItemQuantity)dblItemQuantity\n"
			+ "FROM tblvoidkot a,tbltablemaster b,tblitemmaster c,tblmenuitempricingdtl d,tblcostcentermaster e,tblmenuhd f\n"
			+ "WHERE\n"
			+ "LEFT(a.strItemCode,7)=c.strItemCode \n"
			+ "AND a.strTableNo=b.strTableNo \n"
			+ "AND c.strItemCode=d.strItemCode \n"
			+ "AND a.strPOSCode=d.strPosCode \n"
			+ "AND (d.strPosCode=a.strPosCode OR d.strPosCode='All') \n"
			+ "AND d.strCostCenterCode=e.strCostCenterCode \n"
			+ "and d.strMenuCode=f.strMenuCode\n"
			+ "AND e.strCostCenterCode IN " + funGetCostCenterCodes() + " "
			+ "and a.strPOSCode='" + clsGlobalVarClass.gPOSCode + "' "
			+ "AND DATE(a.dteVoidedDate)='" + posDate + "' \n"
			+ "AND a.strItemProcessed='N'\n"
			+ "GROUP BY f.strMenuName,a.strItemCode\n"
			+ "ORDER BY f.strMenuName)\n"
			+ "ORDER BY strMenuName ,strItemName ";
		if (funGetCostCenterCodes().length() > 2)
		{
		    ResultSet resultSet = clsGlobalVarClass.dbMysql.executeResultSet(sqlBillDtl);
		    while (resultSet.next())
		    {
			clsBillDtl objKOTDtl = new clsBillDtl();

			String menuName = resultSet.getString(2);
			String itemCode = resultSet.getString(3);
			objKOTDtl.setStrKOTNo(menuName);
			objKOTDtl.setStrItemCode(resultSet.getString(3));
			objKOTDtl.setStrItemName(resultSet.getString(4));
			objKOTDtl.setDblRate(0);
			objKOTDtl.setDblQuantity(resultSet.getDouble(5));
			objKOTDtl.setDblAmount(0);
			objKOTDtl.setDteNCKOTDate("");
			objKOTDtl.setStrTableName("");
			objKOTDtl.setStrRemark("");
			objKOTDtl.setStrWaiterNo("");
			objKOTDtl.setDteBillDate("");

			if (mapMenuHd.containsKey(menuName))
			{
			    Map<String, clsBillDtl> mapMenuHead = mapMenuHd.get(menuName);
			    if (mapMenuHead.containsKey(itemCode))
			    {
				clsBillDtl objOldKOTDtl = mapMenuHead.get(itemCode);
				objOldKOTDtl.setDblQuantity(objOldKOTDtl.getDblQuantity() + objKOTDtl.getDblQuantity());
			    }
			    else
			    {
				mapMenuHead.put(itemCode, objKOTDtl);
			    }
			}
			else
			{

			    Map<String, clsBillDtl> mapMenuHead = new HashMap<>();

			    mapMenuHead.put(itemCode, objKOTDtl);

			    mapMenuHd.put(menuName, mapMenuHead);
			}
		    }
		    resultSet.close();
		}

		/**
		 * For Direct Biller
		 */
		sqlBillDtl = " (SELECT d.strMenuCode,e.strMenuName,a.strItemCode,a.strItemName, SUM(a.dblQuantity)\n"
			+ "FROM tblbilldtl a\n"
			+ "JOIN tblbillhd b ON a.strBillNo=b.strBillNo\n"
			+ "JOIN tblmenuitempricingdtl d ON a.strItemCode=d.strItemCode\n"
			+ "join tblmenuhd e on d.strMenuCode=e.strMenuCode\n"
			+ "LEFT OUTER JOIN tblkdsprocess c ON a.strBillNo=c.strDocNo AND a.strItemCode=c.strItemCode\n"
			+ "WHERE b.strOperationType!='DineIn' \n"
			+ "AND c.strItemCode IS NULL \n"
			+ "AND (b.strPOSCode=d.strPosCode OR d.strPosCode='All') \n"
			+ "AND d.strCostCenterCode IN " + funGetCostCenterCodes() + " "
			+ "and b.strPOSCode='" + clsGlobalVarClass.gPOSCode + "' \n"
			+ "GROUP BY e.strMenuName,a.strItemName\n"
			+ "ORDER BY e.strMenuName,a.strItemName\n"
			+ ")\n"
			+ "union all\n"
			+ "(\n"
			+ "\n"
			+ "SELECT d.strMenuCode,e.strMenuName,a.strItemCode,a.strModifierName, SUM(a.dblQuantity)\n"
			+ "FROM tblbillmodifierdtl a\n"
			+ "JOIN tblbillhd b ON a.strBillNo=b.strBillNo\n"
			+ "JOIN tblmenuitempricingdtl d ON left(a.strItemCode,7)=d.strItemCode\n"
			+ "join tblmenuhd e on d.strMenuCode=e.strMenuCode\n"
			+ "LEFT OUTER JOIN tblkdsprocess c ON a.strBillNo=c.strDocNo AND a.strItemCode=c.strItemCode\n"
			+ "WHERE b.strOperationType!='DineIn' \n"
			+ "AND c.strItemCode IS NULL \n"
			+ "AND (b.strPOSCode=d.strPosCode OR d.strPosCode='All') \n"
			+ "AND d.strCostCenterCode IN " + funGetCostCenterCodes() + " "
			+ "and b.strPOSCode='" + clsGlobalVarClass.gPOSCode + "' \n"
			+ "GROUP BY e.strMenuName,a.strModifierName\n"
			+ "ORDER BY e.strMenuName,a.strModifierName\n"
			+ ")\n"
			+ "ORDER BY strMenuName,strItemName  ";
		if (funGetCostCenterCodes().length() > 2)
		{
		    ResultSet resultSet = clsGlobalVarClass.dbMysql.executeResultSet(sqlBillDtl);
		    while (resultSet.next())
		    {
			clsBillDtl billItemDtl = new clsBillDtl();

			String menuName = resultSet.getString(2);
			String itemCode = resultSet.getString(3);

			billItemDtl.setStrKOTNo(menuName);
			billItemDtl.setStrItemCode(resultSet.getString(3));
			billItemDtl.setStrItemName(resultSet.getString(4));
			billItemDtl.setDblRate(0);
			billItemDtl.setDblQuantity(resultSet.getDouble(5));
			billItemDtl.setDblAmount(0);
			billItemDtl.setDteNCKOTDate("");
			billItemDtl.setStrTableName("");
			billItemDtl.setStrRemark("");
			billItemDtl.setStrWaiterNo("");
			billItemDtl.setDteBillDate("");

			if (mapMenuHd.containsKey(menuName))
			{
			    Map<String, clsBillDtl> mapMenuHead = mapMenuHd.get(menuName);
			    if (mapMenuHead.containsKey(itemCode))
			    {
				clsBillDtl objOldKOTDtl = mapMenuHead.get(itemCode);
				objOldKOTDtl.setDblQuantity(objOldKOTDtl.getDblQuantity() + billItemDtl.getDblQuantity());
			    }
			    else
			    {
				mapMenuHead.put(itemCode, billItemDtl);
			    }
			}
			else
			{

			    Map<String, clsBillDtl> mapMenuHead = new HashMap<>();

			    mapMenuHead.put(itemCode, billItemDtl);

			    mapMenuHd.put(menuName, mapMenuHead);
			}

		    }
		}
		/**
		 * End for Direct Biller
		 */

	    }
	    catch (Exception e)
	    {
		e.printStackTrace();
	    }

	    funLoadArrayListForMenuHead();
	    if (mapMenuHd.size() > 14)
	    {
		if (mapMenuHd.size() > 15)
		{

		}
		funLoadScrollPanesForMenu(0, 14);
	    }
	    else if (mapMenuHd.size() > 0)
	    {
		funLoadScrollPanesForMenu(0, mapMenuHd.size() - 1);
	    }

	    System.gc();
	}

    }

    private class MyCellRenderer extends DefaultListCellRenderer
    {

//        final static ImageIcon longIcon = new ImageIcon("long.gif");
//        final static ImageIcon shortIcon = new ImageIcon("short.gif");

	/*
         * This is the only method defined by ListCellRenderer. We just
         * reconfigure the Jlabel each time we're called.
	 */
	public Component getListCellRendererComponent(
		JList list,
		Object value, // value to display
		int index, // cell index
		boolean iss, // is the cell selected
		boolean chf)    // the list and the cell have the focus
	{
	    /*
             * The DefaultListCellRenderer class will take care of the JLabels
             * text property, it's foreground and background colors, and so on.
	     */
	    String item = value.toString();
	    String[] arrItem = item.split("!");
	    value = arrItem[0];
	    super.getListCellRendererComponent(list, value, index, iss, chf);

	    /*
             * We additionally set the JLabels icon property here.
	     */
 /*
             * if (item.contains("-->")) { setForeground(Color.RED); } else {
             * setForeground(Color.BLUE); }
	     */
	    if (arrItem[2].equals("Void"))
	    {
		Font font = list.getFont();
		Map attributes = font.getAttributes();
		attributes.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
		setFont(font.deriveFont(attributes));
		setForeground(Color.yellow);

	    }
	    else
	    {
		if (arrItem[1].equals("RED"))
		{
		    setForeground(Color.RED);
		}
		else if (arrItem[1].equals("ORANGE"))
		{
		    setForeground(Color.CYAN);
		}
		else
		{
		    setForeground(Color.WHITE);
		}
	    }

	    setToolTipText(arrItem[0]);
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
    private void initComponents() {

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
        tabbedPaneKDS = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        scrollPane9 = new javax.swing.JScrollPane();
        list9 = new javax.swing.JList();
        lblTableAndKOTNo9 = new javax.swing.JLabel();
        lblTableAndKOTNo5 = new javax.swing.JLabel();
        lblBillDelay14 = new javax.swing.JLabel();
        btnItemProcessed = new javax.swing.JButton();
        scrollPane13 = new javax.swing.JScrollPane();
        list13 = new javax.swing.JList();
        lblBillDelay15 = new javax.swing.JLabel();
        lblTableAndKOTNo4 = new javax.swing.JLabel();
        lblBillDelay13 = new javax.swing.JLabel();
        lblTableAndKOTNo2 = new javax.swing.JLabel();
        btnKOTProcess = new javax.swing.JButton();
        scrollPane12 = new javax.swing.JScrollPane();
        list12 = new javax.swing.JList();
        scrollPane11 = new javax.swing.JScrollPane();
        list11 = new javax.swing.JList();
        lblTableAndKOTNo1 = new javax.swing.JLabel();
        lblSelectedKOT = new javax.swing.JLabel();
        scrollPane5 = new javax.swing.JScrollPane();
        list5 = new javax.swing.JList();
        scrollPane4 = new javax.swing.JScrollPane();
        list4 = new javax.swing.JList();
        lblTableAndKOTNo10 = new javax.swing.JLabel();
        scrollPane6 = new javax.swing.JScrollPane();
        list6 = new javax.swing.JList();
        scrollPane8 = new javax.swing.JScrollPane();
        list8 = new javax.swing.JList();
        lblBillDelay10 = new javax.swing.JLabel();
        btnNew = new javax.swing.JButton();
        lblBillDelay4 = new javax.swing.JLabel();
        lblBillDelay3 = new javax.swing.JLabel();
        btnOld = new javax.swing.JButton();
        lblBillDelay2 = new javax.swing.JLabel();
        lblTableAndKOTNo12 = new javax.swing.JLabel();
        lblBillDelay5 = new javax.swing.JLabel();
        lblTableAndKOTNo8 = new javax.swing.JLabel();
        scrollPane10 = new javax.swing.JScrollPane();
        list10 = new javax.swing.JList();
        lblBillDelay7 = new javax.swing.JLabel();
        lblBillDelay8 = new javax.swing.JLabel();
        scrollPane15 = new javax.swing.JScrollPane();
        list15 = new javax.swing.JList();
        btnClose = new javax.swing.JButton();
        lblBillDelay6 = new javax.swing.JLabel();
        lblTableAndKOTNo3 = new javax.swing.JLabel();
        lblBillDelay11 = new javax.swing.JLabel();
        scrollPane1 = new javax.swing.JScrollPane();
        list1 = new javax.swing.JList();
        lblTableAndKOTNo11 = new javax.swing.JLabel();
        lblTableAndKOTNo7 = new javax.swing.JLabel();
        scrollPane7 = new javax.swing.JScrollPane();
        list7 = new javax.swing.JList();
        scrollPane3 = new javax.swing.JScrollPane();
        list3 = new javax.swing.JList();
        lblBillDelay12 = new javax.swing.JLabel();
        scrollPane2 = new javax.swing.JScrollPane();
        list2 = new javax.swing.JList();
        lblTableAndKOTNo15 = new javax.swing.JLabel();
        lblTableAndKOTNo14 = new javax.swing.JLabel();
        lblBillDelay9 = new javax.swing.JLabel();
        lblTableAndKOTNo6 = new javax.swing.JLabel();
        scrollPane14 = new javax.swing.JScrollPane();
        list14 = new javax.swing.JList();
        lblBillDelay1 = new javax.swing.JLabel();
        lblSelectedItem = new javax.swing.JLabel();
        lblTableAndKOTNo13 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        lblTableAndKOTNo16 = new javax.swing.JLabel();
        scrollPane16 = new javax.swing.JScrollPane();
        list16 = new javax.swing.JList();
        lblTableAndKOTNo17 = new javax.swing.JLabel();
        scrollPane17 = new javax.swing.JScrollPane();
        list17 = new javax.swing.JList();
        lblTableAndKOTNo18 = new javax.swing.JLabel();
        scrollPane18 = new javax.swing.JScrollPane();
        list18 = new javax.swing.JList();
        lblTableAndKOTNo19 = new javax.swing.JLabel();
        scrollPane19 = new javax.swing.JScrollPane();
        list19 = new javax.swing.JList();
        lblTableAndKOTNo20 = new javax.swing.JLabel();
        scrollPane20 = new javax.swing.JScrollPane();
        list20 = new javax.swing.JList();
        lblTableAndKOTNo21 = new javax.swing.JLabel();
        scrollPane21 = new javax.swing.JScrollPane();
        list21 = new javax.swing.JList();
        lblTableAndKOTNo22 = new javax.swing.JLabel();
        scrollPane22 = new javax.swing.JScrollPane();
        list22 = new javax.swing.JList();
        lblTableAndKOTNo23 = new javax.swing.JLabel();
        scrollPane23 = new javax.swing.JScrollPane();
        list23 = new javax.swing.JList();
        lblTableAndKOTNo24 = new javax.swing.JLabel();
        scrollPane24 = new javax.swing.JScrollPane();
        list24 = new javax.swing.JList();
        lblTableAndKOTNo25 = new javax.swing.JLabel();
        scrollPane25 = new javax.swing.JScrollPane();
        list25 = new javax.swing.JList();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setExtendedState(MAXIMIZED_BOTH);
        setMinimumSize(new java.awt.Dimension(1366, 765));
        setUndecorated(true);
        setPreferredSize(new java.awt.Dimension(1366, 765));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        panelHeader.setBackground(new java.awt.Color(69, 164, 238));
        panelHeader.setLayout(new javax.swing.BoxLayout(panelHeader, javax.swing.BoxLayout.LINE_AXIS));

        lblProductName.setFont(new java.awt.Font("Trebuchet MS", 1, 14)); // NOI18N
        lblProductName.setForeground(new java.awt.Color(255, 255, 255));
        lblProductName.setText("SPOS -  ");
        lblProductName.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblProductNameMouseClicked(evt);
            }
        });
        panelHeader.add(lblProductName);

        lblModuleName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblModuleName.setForeground(new java.awt.Color(255, 255, 255));
        panelHeader.add(lblModuleName);

        lblformName.setFont(new java.awt.Font("Trebuchet MS", 1, 14)); // NOI18N
        lblformName.setForeground(new java.awt.Color(255, 255, 255));
        lblformName.setText("- KDS For KOT With Book And Process");
        lblformName.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
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
        lblPosName.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblPosNameMouseClicked(evt);
            }
        });
        panelHeader.add(lblPosName);

        lblUserCode.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblUserCode.setForeground(new java.awt.Color(255, 255, 255));
        lblUserCode.setMaximumSize(new java.awt.Dimension(90, 30));
        lblUserCode.setMinimumSize(new java.awt.Dimension(90, 30));
        lblUserCode.setPreferredSize(new java.awt.Dimension(90, 30));
        lblUserCode.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
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
        lblDate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblDateMouseClicked(evt);
            }
        });
        panelHeader.add(lblDate);

        lblHOSign.setMaximumSize(new java.awt.Dimension(34, 30));
        lblHOSign.setMinimumSize(new java.awt.Dimension(34, 30));
        lblHOSign.setPreferredSize(new java.awt.Dimension(34, 30));
        lblHOSign.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblHOSignMouseClicked(evt);
            }
        });
        panelHeader.add(lblHOSign);

        getContentPane().add(panelHeader, java.awt.BorderLayout.PAGE_START);

        panelMain.setMinimumSize(new java.awt.Dimension(800, 570));
        panelMain.setOpaque(false);
        panelMain.setPreferredSize(new java.awt.Dimension(832, 565));
        panelMain.setLayout(new java.awt.GridBagLayout());

        panelBody.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        panelBody.setMaximumSize(new java.awt.Dimension(1366, 765));
        panelBody.setMinimumSize(new java.awt.Dimension(1366, 765));
        panelBody.setOpaque(false);
        panelBody.setPreferredSize(new java.awt.Dimension(1366, 765));
        panelBody.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tabbedPaneKDS.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tabbedPaneKDSStateChanged(evt);
            }
        });

        jPanel1.setOpaque(false);
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        scrollPane9.setBorder(null);
        scrollPane9.setViewportBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        scrollPane9.setMinimumSize(new java.awt.Dimension(55, 160));
        scrollPane9.setPreferredSize(new java.awt.Dimension(55, 160));
        scrollPane9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                scrollPane9MouseClicked(evt);
            }
        });

        list9.setBackground(new java.awt.Color(0, 0, 0));
        list9.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        list9.setForeground(new java.awt.Color(255, 255, 255));
        list9.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        list9.setFixedCellHeight(35);
        list9.setFixedCellWidth(150);
        list9.setSelectionBackground(new java.awt.Color(0, 153, 255));
        list9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                list9MouseClicked(evt);
            }
        });
        scrollPane9.setViewportView(list9);

        jPanel1.add(scrollPane9, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 250, 255, 200));

        lblTableAndKOTNo9.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        lblTableAndKOTNo9.setText("00:00:00");
        lblTableAndKOTNo9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblTableAndKOTNo9MouseClicked(evt);
            }
        });
        jPanel1.add(lblTableAndKOTNo9, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 230, 180, 20));

        lblTableAndKOTNo5.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        lblTableAndKOTNo5.setText("00:00:00");
        lblTableAndKOTNo5.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        lblTableAndKOTNo5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblTableAndKOTNo5MouseClicked(evt);
            }
        });
        jPanel1.add(lblTableAndKOTNo5, new org.netbeans.lib.awtextra.AbsoluteConstraints(1100, 10, 180, 20));

        lblBillDelay14.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        lblBillDelay14.setForeground(new java.awt.Color(255, 51, 0));
        lblBillDelay14.setText("00:00");
        jPanel1.add(lblBillDelay14, new org.netbeans.lib.awtextra.AbsoluteConstraints(1020, 450, 50, 20));

        btnItemProcessed.setFont(new java.awt.Font("Trebuchet MS", 1, 14)); // NOI18N
        btnItemProcessed.setForeground(new java.awt.Color(255, 255, 255));
        btnItemProcessed.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgOrderProcessed.png"))); // NOI18N
        btnItemProcessed.setText("Item Process");
        btnItemProcessed.setEnabled(false);
        btnItemProcessed.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnItemProcessed.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgOrderProcessed1.png"))); // NOI18N
        btnItemProcessed.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnItemProcessedMouseClicked(evt);
            }
        });
        jPanel1.add(btnItemProcessed, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 680, 150, 40));

        scrollPane13.setBorder(null);
        scrollPane13.setViewportBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        scrollPane13.setMinimumSize(new java.awt.Dimension(55, 160));
        scrollPane13.setPreferredSize(new java.awt.Dimension(55, 160));
        scrollPane13.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                scrollPane13MouseClicked(evt);
            }
        });

        list13.setBackground(new java.awt.Color(0, 0, 0));
        list13.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        list13.setForeground(new java.awt.Color(255, 255, 255));
        list13.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        list13.setFixedCellHeight(35);
        list13.setFixedCellWidth(150);
        list13.setSelectionBackground(new java.awt.Color(0, 153, 255));
        list13.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                list13MouseClicked(evt);
            }
        });
        scrollPane13.setViewportView(list13);

        jPanel1.add(scrollPane13, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 470, 255, 200));

        lblBillDelay15.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        lblBillDelay15.setForeground(new java.awt.Color(255, 51, 0));
        lblBillDelay15.setText("00:00");
        jPanel1.add(lblBillDelay15, new org.netbeans.lib.awtextra.AbsoluteConstraints(1300, 450, 50, 20));

        lblTableAndKOTNo4.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        lblTableAndKOTNo4.setText("00:00:00");
        lblTableAndKOTNo4.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        lblTableAndKOTNo4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblTableAndKOTNo4MouseClicked(evt);
            }
        });
        jPanel1.add(lblTableAndKOTNo4, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 10, 170, 20));

        lblBillDelay13.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        lblBillDelay13.setForeground(new java.awt.Color(255, 51, 0));
        lblBillDelay13.setText("00:00");
        jPanel1.add(lblBillDelay13, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 450, -1, 20));

        lblTableAndKOTNo2.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        lblTableAndKOTNo2.setText("00:00:00");
        lblTableAndKOTNo2.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        lblTableAndKOTNo2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblTableAndKOTNo2MouseClicked(evt);
            }
        });
        jPanel1.add(lblTableAndKOTNo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 10, 180, 20));

        btnKOTProcess.setFont(new java.awt.Font("Trebuchet MS", 1, 14)); // NOI18N
        btnKOTProcess.setForeground(new java.awt.Color(255, 255, 255));
        btnKOTProcess.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgOrderProcessed.png"))); // NOI18N
        btnKOTProcess.setText("KOT Process");
        btnKOTProcess.setEnabled(false);
        btnKOTProcess.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnKOTProcess.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgOrderProcessed1.png"))); // NOI18N
        btnKOTProcess.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnKOTProcessMouseClicked(evt);
            }
        });
        jPanel1.add(btnKOTProcess, new org.netbeans.lib.awtextra.AbsoluteConstraints(1010, 680, 130, 40));

        scrollPane12.setBorder(null);
        scrollPane12.setViewportBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        scrollPane12.setMinimumSize(new java.awt.Dimension(55, 160));
        scrollPane12.setPreferredSize(new java.awt.Dimension(55, 160));
        scrollPane12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                scrollPane12MouseClicked(evt);
            }
        });

        list12.setBackground(new java.awt.Color(0, 0, 0));
        list12.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        list12.setForeground(new java.awt.Color(255, 255, 255));
        list12.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        list12.setFixedCellHeight(35);
        list12.setFixedCellWidth(150);
        list12.setSelectionBackground(new java.awt.Color(0, 153, 255));
        list12.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                list12MouseMoved(evt);
            }
        });
        list12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                list12MouseClicked(evt);
            }
        });
        scrollPane12.setViewportView(list12);

        jPanel1.add(scrollPane12, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 470, 255, 200));

        scrollPane11.setBorder(null);
        scrollPane11.setViewportBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        scrollPane11.setMinimumSize(new java.awt.Dimension(55, 160));
        scrollPane11.setPreferredSize(new java.awt.Dimension(55, 160));
        scrollPane11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                scrollPane11MouseClicked(evt);
            }
        });

        list11.setBackground(new java.awt.Color(0, 0, 0));
        list11.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        list11.setForeground(new java.awt.Color(255, 255, 255));
        list11.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        list11.setFixedCellHeight(35);
        list11.setFixedCellWidth(150);
        list11.setSelectionBackground(new java.awt.Color(0, 153, 255));
        list11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                list11MouseClicked(evt);
            }
        });
        scrollPane11.setViewportView(list11);

        jPanel1.add(scrollPane11, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 470, 255, 200));

        lblTableAndKOTNo1.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        lblTableAndKOTNo1.setText("00:00:00");
        lblTableAndKOTNo1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        lblTableAndKOTNo1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblTableAndKOTNo1MouseClicked(evt);
            }
        });
        jPanel1.add(lblTableAndKOTNo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 160, 20));

        lblSelectedKOT.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        lblSelectedKOT.setForeground(new java.awt.Color(0, 0, 204));
        lblSelectedKOT.setText("KOT");
        lblSelectedKOT.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        lblSelectedKOT.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblSelectedKOTMouseClicked(evt);
            }
        });
        jPanel1.add(lblSelectedKOT, new org.netbeans.lib.awtextra.AbsoluteConstraints(1140, 680, 110, 40));

        scrollPane5.setBorder(null);
        scrollPane5.setViewportBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        scrollPane5.setMinimumSize(new java.awt.Dimension(55, 160));
        scrollPane5.setPreferredSize(new java.awt.Dimension(55, 160));
        scrollPane5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                scrollPane5MouseClicked(evt);
            }
        });

        list5.setBackground(new java.awt.Color(0, 0, 0));
        list5.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        list5.setForeground(new java.awt.Color(255, 255, 255));
        list5.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        list5.setFixedCellHeight(35);
        list5.setFixedCellWidth(150);
        list5.setSelectionBackground(new java.awt.Color(0, 153, 255));
        list5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                list5MouseClicked(evt);
            }
        });
        scrollPane5.setViewportView(list5);

        jPanel1.add(scrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(1100, 30, 255, 200));

        scrollPane4.setBorder(null);
        scrollPane4.setViewportBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        scrollPane4.setMinimumSize(new java.awt.Dimension(55, 160));
        scrollPane4.setPreferredSize(new java.awt.Dimension(55, 160));
        scrollPane4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                scrollPane4MouseClicked(evt);
            }
        });

        list4.setBackground(new java.awt.Color(0, 0, 0));
        list4.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        list4.setForeground(new java.awt.Color(255, 255, 255));
        list4.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        list4.setFixedCellHeight(35);
        list4.setFixedCellWidth(150);
        list4.setSelectionBackground(new java.awt.Color(0, 153, 255));
        list4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                list4MouseClicked(evt);
            }
        });
        scrollPane4.setViewportView(list4);

        jPanel1.add(scrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 30, 255, 200));

        lblTableAndKOTNo10.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        lblTableAndKOTNo10.setText("00:00:00");
        lblTableAndKOTNo10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblTableAndKOTNo10MouseClicked(evt);
            }
        });
        jPanel1.add(lblTableAndKOTNo10, new org.netbeans.lib.awtextra.AbsoluteConstraints(1100, 230, 170, 20));

        scrollPane6.setBorder(null);
        scrollPane6.setViewportBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        scrollPane6.setMinimumSize(new java.awt.Dimension(55, 160));
        scrollPane6.setPreferredSize(new java.awt.Dimension(55, 160));
        scrollPane6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                scrollPane6MouseClicked(evt);
            }
        });

        list6.setBackground(new java.awt.Color(0, 0, 0));
        list6.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        list6.setForeground(new java.awt.Color(255, 255, 255));
        list6.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        list6.setFixedCellHeight(35);
        list6.setFixedCellWidth(150);
        list6.setSelectionBackground(new java.awt.Color(0, 153, 255));
        list6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                list6MouseClicked(evt);
            }
        });
        scrollPane6.setViewportView(list6);

        jPanel1.add(scrollPane6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 250, 255, 200));

        scrollPane8.setBorder(null);
        scrollPane8.setViewportBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        scrollPane8.setMinimumSize(new java.awt.Dimension(55, 160));
        scrollPane8.setPreferredSize(new java.awt.Dimension(55, 160));
        scrollPane8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                scrollPane8MouseClicked(evt);
            }
        });

        list8.setBackground(new java.awt.Color(0, 0, 0));
        list8.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        list8.setForeground(new java.awt.Color(255, 255, 255));
        list8.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        list8.setFixedCellHeight(35);
        list8.setFixedCellWidth(150);
        list8.setSelectionBackground(new java.awt.Color(0, 153, 255));
        list8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                list8MouseClicked(evt);
            }
        });
        scrollPane8.setViewportView(list8);

        jPanel1.add(scrollPane8, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 250, 255, 200));

        lblBillDelay10.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        lblBillDelay10.setForeground(new java.awt.Color(255, 51, 0));
        lblBillDelay10.setText("00:00");
        jPanel1.add(lblBillDelay10, new org.netbeans.lib.awtextra.AbsoluteConstraints(1290, 230, -1, 20));

        btnNew.setFont(new java.awt.Font("Trebuchet MS", 1, 14)); // NOI18N
        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgNewButton1.png"))); // NOI18N
        btnNew.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgNewButton2.png"))); // NOI18N
        btnNew.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnNewMouseClicked(evt);
            }
        });
        jPanel1.add(btnNew, new org.netbeans.lib.awtextra.AbsoluteConstraints(1250, 680, 100, 40));

        lblBillDelay4.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        lblBillDelay4.setForeground(new java.awt.Color(255, 51, 0));
        lblBillDelay4.setText("00:00");
        lblBillDelay4.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jPanel1.add(lblBillDelay4, new org.netbeans.lib.awtextra.AbsoluteConstraints(1010, 10, -1, 20));

        lblBillDelay3.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        lblBillDelay3.setForeground(new java.awt.Color(255, 51, 0));
        lblBillDelay3.setText("00:00");
        lblBillDelay3.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jPanel1.add(lblBillDelay3, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 10, 50, 20));

        btnOld.setFont(new java.awt.Font("Trebuchet MS", 1, 14)); // NOI18N
        btnOld.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgOldButton1.png"))); // NOI18N
        btnOld.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgOldButton2.png"))); // NOI18N
        btnOld.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnOldMouseClicked(evt);
            }
        });
        jPanel1.add(btnOld, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 680, 100, 40));

        lblBillDelay2.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        lblBillDelay2.setForeground(new java.awt.Color(255, 51, 0));
        lblBillDelay2.setText("00:00");
        lblBillDelay2.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jPanel1.add(lblBillDelay2, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 10, -1, 20));

        lblTableAndKOTNo12.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        lblTableAndKOTNo12.setText("00:00:00");
        lblTableAndKOTNo12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblTableAndKOTNo12MouseClicked(evt);
            }
        });
        jPanel1.add(lblTableAndKOTNo12, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 450, 180, 20));

        lblBillDelay5.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        lblBillDelay5.setForeground(new java.awt.Color(255, 51, 0));
        lblBillDelay5.setText("00:00");
        lblBillDelay5.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jPanel1.add(lblBillDelay5, new org.netbeans.lib.awtextra.AbsoluteConstraints(1290, 10, 50, 20));

        lblTableAndKOTNo8.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        lblTableAndKOTNo8.setText("00:00:00");
        lblTableAndKOTNo8.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        lblTableAndKOTNo8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblTableAndKOTNo8MouseClicked(evt);
            }
        });
        jPanel1.add(lblTableAndKOTNo8, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 230, 180, 20));

        scrollPane10.setBorder(null);
        scrollPane10.setViewportBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        scrollPane10.setMinimumSize(new java.awt.Dimension(55, 160));
        scrollPane10.setPreferredSize(new java.awt.Dimension(55, 160));
        scrollPane10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                scrollPane10MouseClicked(evt);
            }
        });

        list10.setBackground(new java.awt.Color(0, 0, 0));
        list10.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        list10.setForeground(new java.awt.Color(255, 255, 255));
        list10.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        list10.setFixedCellHeight(35);
        list10.setFixedCellWidth(150);
        list10.setSelectionBackground(new java.awt.Color(0, 153, 255));
        list10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                list10MouseClicked(evt);
            }
        });
        scrollPane10.setViewportView(list10);

        jPanel1.add(scrollPane10, new org.netbeans.lib.awtextra.AbsoluteConstraints(1100, 250, 255, 200));

        lblBillDelay7.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        lblBillDelay7.setForeground(new java.awt.Color(255, 51, 0));
        lblBillDelay7.setText("00:00");
        lblBillDelay7.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jPanel1.add(lblBillDelay7, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 230, 50, 20));

        lblBillDelay8.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        lblBillDelay8.setForeground(new java.awt.Color(255, 51, 0));
        lblBillDelay8.setText("00:00");
        lblBillDelay8.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jPanel1.add(lblBillDelay8, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 230, 50, 20));

        scrollPane15.setBorder(null);
        scrollPane15.setViewportBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        scrollPane15.setMinimumSize(new java.awt.Dimension(55, 160));
        scrollPane15.setPreferredSize(new java.awt.Dimension(55, 160));
        scrollPane15.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                scrollPane15MouseClicked(evt);
            }
        });

        list15.setBackground(new java.awt.Color(0, 0, 0));
        list15.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        list15.setForeground(new java.awt.Color(255, 255, 255));
        list15.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        list15.setFixedCellHeight(35);
        list15.setFixedCellWidth(200);
        list15.setSelectionBackground(new java.awt.Color(0, 153, 255));
        list15.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                list15MouseClicked(evt);
            }
        });
        scrollPane15.setViewportView(list15);

        jPanel1.add(scrollPane15, new org.netbeans.lib.awtextra.AbsoluteConstraints(1100, 470, 255, 200));

        btnClose.setFont(new java.awt.Font("Trebuchet MS", 1, 14)); // NOI18N
        btnClose.setForeground(new java.awt.Color(255, 255, 255));
        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn1.png"))); // NOI18N
        btnClose.setText("CLOSE");
        btnClose.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnClose.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgCommonBtn2.png"))); // NOI18N
        btnClose.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCloseMouseClicked(evt);
            }
        });
        jPanel1.add(btnClose, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 680, 80, 40));

        lblBillDelay6.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        lblBillDelay6.setForeground(new java.awt.Color(255, 51, 0));
        lblBillDelay6.setText("00:00");
        lblBillDelay6.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jPanel1.add(lblBillDelay6, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 230, -1, 20));

        lblTableAndKOTNo3.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        lblTableAndKOTNo3.setText("00:00:00");
        lblTableAndKOTNo3.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        lblTableAndKOTNo3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblTableAndKOTNo3MouseClicked(evt);
            }
        });
        jPanel1.add(lblTableAndKOTNo3, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 10, 180, 20));

        lblBillDelay11.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        lblBillDelay11.setForeground(new java.awt.Color(255, 51, 0));
        lblBillDelay11.setText("00:00");
        jPanel1.add(lblBillDelay11, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 450, 50, 20));

        scrollPane1.setBorder(null);
        scrollPane1.setViewportBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        scrollPane1.setMinimumSize(new java.awt.Dimension(55, 160));
        scrollPane1.setPreferredSize(new java.awt.Dimension(55, 160));
        scrollPane1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                scrollPane1MouseClicked(evt);
            }
        });

        list1.setBackground(new java.awt.Color(0, 0, 0));
        list1.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        list1.setForeground(new java.awt.Color(255, 255, 255));
        list1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        list1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        list1.setFixedCellHeight(35);
        list1.setFixedCellWidth(100);
        list1.setMaximumSize(new java.awt.Dimension(100, 100));
        list1.setMinimumSize(new java.awt.Dimension(100, 100));
        list1.setName(""); // NOI18N
        list1.setPreferredSize(new java.awt.Dimension(100, 100));
        list1.setSelectionBackground(new java.awt.Color(0, 153, 255));
        list1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                list1MouseClicked(evt);
            }
        });
        scrollPane1.setViewportView(list1);

        jPanel1.add(scrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 255, 200));

        lblTableAndKOTNo11.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        lblTableAndKOTNo11.setText("00:00:00");
        lblTableAndKOTNo11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblTableAndKOTNo11MouseClicked(evt);
            }
        });
        jPanel1.add(lblTableAndKOTNo11, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 450, 180, 20));

        lblTableAndKOTNo7.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        lblTableAndKOTNo7.setText("00:00:00");
        lblTableAndKOTNo7.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        lblTableAndKOTNo7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblTableAndKOTNo7MouseClicked(evt);
            }
        });
        jPanel1.add(lblTableAndKOTNo7, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 230, 180, 20));

        scrollPane7.setBorder(null);
        scrollPane7.setViewportBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        scrollPane7.setMinimumSize(new java.awt.Dimension(55, 160));
        scrollPane7.setPreferredSize(new java.awt.Dimension(55, 160));
        scrollPane7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                scrollPane7MouseClicked(evt);
            }
        });

        list7.setBackground(new java.awt.Color(0, 0, 0));
        list7.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        list7.setForeground(new java.awt.Color(255, 255, 255));
        list7.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        list7.setFixedCellHeight(35);
        list7.setFixedCellWidth(200);
        list7.setSelectionBackground(new java.awt.Color(0, 153, 255));
        list7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                list7MouseClicked(evt);
            }
        });
        scrollPane7.setViewportView(list7);

        jPanel1.add(scrollPane7, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 250, 255, 200));

        scrollPane3.setBorder(null);
        scrollPane3.setViewportBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        scrollPane3.setMinimumSize(new java.awt.Dimension(55, 160));
        scrollPane3.setPreferredSize(new java.awt.Dimension(55, 160));
        scrollPane3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                scrollPane3MouseClicked(evt);
            }
        });

        list3.setBackground(new java.awt.Color(0, 0, 0));
        list3.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        list3.setForeground(new java.awt.Color(255, 255, 255));
        list3.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        list3.setFixedCellHeight(35);
        list3.setFixedCellWidth(150);
        list3.setSelectionBackground(new java.awt.Color(0, 153, 255));
        list3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                list3MouseClicked(evt);
            }
        });
        scrollPane3.setViewportView(list3);

        jPanel1.add(scrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 30, 255, 200));

        lblBillDelay12.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        lblBillDelay12.setForeground(new java.awt.Color(255, 51, 0));
        lblBillDelay12.setText("00:00");
        jPanel1.add(lblBillDelay12, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 450, 50, -1));

        scrollPane2.setBorder(null);
        scrollPane2.setViewportBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        scrollPane2.setMinimumSize(new java.awt.Dimension(55, 160));
        scrollPane2.setPreferredSize(new java.awt.Dimension(55, 160));
        scrollPane2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                scrollPane2MouseClicked(evt);
            }
        });

        list2.setBackground(new java.awt.Color(0, 0, 0));
        list2.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        list2.setForeground(new java.awt.Color(255, 255, 255));
        list2.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        list2.setFixedCellHeight(35);
        list2.setFixedCellWidth(150);
        list2.setSelectionBackground(new java.awt.Color(0, 153, 255));
        list2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                list2MouseClicked(evt);
            }
        });
        scrollPane2.setViewportView(list2);

        jPanel1.add(scrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 30, 255, 200));

        lblTableAndKOTNo15.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        lblTableAndKOTNo15.setText("00:00:00");
        lblTableAndKOTNo15.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblTableAndKOTNo15MouseClicked(evt);
            }
        });
        jPanel1.add(lblTableAndKOTNo15, new org.netbeans.lib.awtextra.AbsoluteConstraints(1100, 450, 180, 20));

        lblTableAndKOTNo14.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        lblTableAndKOTNo14.setText("00:00:00");
        lblTableAndKOTNo14.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblTableAndKOTNo14MouseClicked(evt);
            }
        });
        jPanel1.add(lblTableAndKOTNo14, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 450, 180, 20));

        lblBillDelay9.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        lblBillDelay9.setForeground(new java.awt.Color(255, 51, 0));
        lblBillDelay9.setText("00:00");
        jPanel1.add(lblBillDelay9, new org.netbeans.lib.awtextra.AbsoluteConstraints(1020, 230, 50, 20));

        lblTableAndKOTNo6.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        lblTableAndKOTNo6.setText("00:00:00");
        lblTableAndKOTNo6.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        lblTableAndKOTNo6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblTableAndKOTNo6MouseClicked(evt);
            }
        });
        jPanel1.add(lblTableAndKOTNo6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 230, 170, 20));

        scrollPane14.setBorder(null);
        scrollPane14.setViewportBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        scrollPane14.setMinimumSize(new java.awt.Dimension(55, 160));
        scrollPane14.setPreferredSize(new java.awt.Dimension(55, 160));
        scrollPane14.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                scrollPane14MouseClicked(evt);
            }
        });

        list14.setBackground(new java.awt.Color(0, 0, 0));
        list14.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        list14.setForeground(new java.awt.Color(255, 255, 255));
        list14.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        list14.setFixedCellHeight(35);
        list14.setFixedCellWidth(200);
        list14.setSelectionBackground(new java.awt.Color(0, 153, 255));
        list14.setValueIsAdjusting(true);
        list14.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                list14MouseMoved(evt);
            }
        });
        list14.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                list14MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                list14MouseEntered(evt);
            }
        });
        scrollPane14.setViewportView(list14);

        jPanel1.add(scrollPane14, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 470, 255, 200));

        lblBillDelay1.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        lblBillDelay1.setForeground(new java.awt.Color(255, 51, 0));
        lblBillDelay1.setText("00:00");
        lblBillDelay1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jPanel1.add(lblBillDelay1, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 10, 50, 20));

        lblSelectedItem.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        lblSelectedItem.setForeground(new java.awt.Color(0, 0, 153));
        lblSelectedItem.setText("ITEM");
        lblSelectedItem.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        lblSelectedItem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblSelectedItemMouseClicked(evt);
            }
        });
        jPanel1.add(lblSelectedItem, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 680, 620, 40));

        lblTableAndKOTNo13.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        lblTableAndKOTNo13.setText("00:00:00");
        lblTableAndKOTNo13.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblTableAndKOTNo13MouseClicked(evt);
            }
        });
        jPanel1.add(lblTableAndKOTNo13, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 450, 180, 20));

        tabbedPaneKDS.addTab("KDS", jPanel1);

        jPanel2.setOpaque(false);
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTableAndKOTNo16.setFont(new java.awt.Font("Trebuchet MS", 1, 20)); // NOI18N
        lblTableAndKOTNo16.setText("00:00:00");
        lblTableAndKOTNo16.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        lblTableAndKOTNo16.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblTableAndKOTNo16MouseClicked(evt);
            }
        });
        jPanel2.add(lblTableAndKOTNo16, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, 250, 30));

        scrollPane16.setBorder(null);
        scrollPane16.setViewportBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        scrollPane16.setMinimumSize(new java.awt.Dimension(55, 160));
        scrollPane16.setPreferredSize(new java.awt.Dimension(55, 160));
        scrollPane16.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                scrollPane16MouseClicked(evt);
            }
        });

        list16.setBackground(new java.awt.Color(0, 0, 0));
        list16.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        list16.setForeground(new java.awt.Color(255, 255, 255));
        list16.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        list16.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        list16.setFixedCellHeight(35);
        list16.setFixedCellWidth(100);
        list16.setMaximumSize(new java.awt.Dimension(100, 100));
        list16.setMinimumSize(new java.awt.Dimension(100, 100));
        list16.setName(""); // NOI18N
        list16.setPreferredSize(new java.awt.Dimension(100, 100));
        list16.setSelectionBackground(new java.awt.Color(0, 153, 255));
        list16.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                list16MouseClicked(evt);
            }
        });
        scrollPane16.setViewportView(list16);

        jPanel2.add(scrollPane16, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 255, 330));

        lblTableAndKOTNo17.setFont(new java.awt.Font("Trebuchet MS", 1, 20)); // NOI18N
        lblTableAndKOTNo17.setText("00:00:00");
        lblTableAndKOTNo17.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        lblTableAndKOTNo17.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblTableAndKOTNo17MouseClicked(evt);
            }
        });
        jPanel2.add(lblTableAndKOTNo17, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 0, 270, 30));

        scrollPane17.setBorder(null);
        scrollPane17.setViewportBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        scrollPane17.setMinimumSize(new java.awt.Dimension(55, 160));
        scrollPane17.setPreferredSize(new java.awt.Dimension(55, 160));
        scrollPane17.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                scrollPane17MouseClicked(evt);
            }
        });

        list17.setBackground(new java.awt.Color(0, 0, 0));
        list17.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        list17.setForeground(new java.awt.Color(255, 255, 255));
        list17.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        list17.setFixedCellHeight(35);
        list17.setFixedCellWidth(150);
        list17.setSelectionBackground(new java.awt.Color(0, 153, 255));
        list17.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                list17MouseClicked(evt);
            }
        });
        scrollPane17.setViewportView(list17);

        jPanel2.add(scrollPane17, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 30, 255, 330));

        lblTableAndKOTNo18.setFont(new java.awt.Font("Trebuchet MS", 1, 20)); // NOI18N
        lblTableAndKOTNo18.setText("00:00:00");
        lblTableAndKOTNo18.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        lblTableAndKOTNo18.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblTableAndKOTNo18MouseClicked(evt);
            }
        });
        jPanel2.add(lblTableAndKOTNo18, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 0, 270, 30));

        scrollPane18.setBorder(null);
        scrollPane18.setViewportBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        scrollPane18.setMinimumSize(new java.awt.Dimension(55, 160));
        scrollPane18.setPreferredSize(new java.awt.Dimension(55, 160));
        scrollPane18.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                scrollPane18MouseClicked(evt);
            }
        });

        list18.setBackground(new java.awt.Color(0, 0, 0));
        list18.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        list18.setForeground(new java.awt.Color(255, 255, 255));
        list18.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        list18.setFixedCellHeight(35);
        list18.setFixedCellWidth(150);
        list18.setSelectionBackground(new java.awt.Color(0, 153, 255));
        list18.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                list18MouseClicked(evt);
            }
        });
        scrollPane18.setViewportView(list18);

        jPanel2.add(scrollPane18, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 30, 255, 330));

        lblTableAndKOTNo19.setFont(new java.awt.Font("Trebuchet MS", 1, 20)); // NOI18N
        lblTableAndKOTNo19.setText("00:00:00");
        lblTableAndKOTNo19.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        lblTableAndKOTNo19.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblTableAndKOTNo19MouseClicked(evt);
            }
        });
        jPanel2.add(lblTableAndKOTNo19, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 0, 260, 30));

        scrollPane19.setBorder(null);
        scrollPane19.setViewportBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        scrollPane19.setMinimumSize(new java.awt.Dimension(55, 160));
        scrollPane19.setPreferredSize(new java.awt.Dimension(55, 160));
        scrollPane19.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                scrollPane19MouseClicked(evt);
            }
        });

        list19.setBackground(new java.awt.Color(0, 0, 0));
        list19.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        list19.setForeground(new java.awt.Color(255, 255, 255));
        list19.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        list19.setFixedCellHeight(35);
        list19.setFixedCellWidth(150);
        list19.setSelectionBackground(new java.awt.Color(0, 153, 255));
        list19.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                list19MouseClicked(evt);
            }
        });
        scrollPane19.setViewportView(list19);

        jPanel2.add(scrollPane19, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 30, 255, 330));

        lblTableAndKOTNo20.setFont(new java.awt.Font("Trebuchet MS", 1, 20)); // NOI18N
        lblTableAndKOTNo20.setText("00:00:00");
        lblTableAndKOTNo20.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        lblTableAndKOTNo20.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblTableAndKOTNo20MouseClicked(evt);
            }
        });
        jPanel2.add(lblTableAndKOTNo20, new org.netbeans.lib.awtextra.AbsoluteConstraints(1100, 0, 270, 30));

        scrollPane20.setBorder(null);
        scrollPane20.setViewportBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        scrollPane20.setMinimumSize(new java.awt.Dimension(55, 160));
        scrollPane20.setPreferredSize(new java.awt.Dimension(55, 160));
        scrollPane20.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                scrollPane20MouseClicked(evt);
            }
        });

        list20.setBackground(new java.awt.Color(0, 0, 0));
        list20.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        list20.setForeground(new java.awt.Color(255, 255, 255));
        list20.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        list20.setFixedCellHeight(35);
        list20.setFixedCellWidth(150);
        list20.setSelectionBackground(new java.awt.Color(0, 153, 255));
        list20.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                list20MouseClicked(evt);
            }
        });
        scrollPane20.setViewportView(list20);

        jPanel2.add(scrollPane20, new org.netbeans.lib.awtextra.AbsoluteConstraints(1100, 30, 255, 330));

        lblTableAndKOTNo21.setFont(new java.awt.Font("Trebuchet MS", 1, 20)); // NOI18N
        lblTableAndKOTNo21.setText("00:00:00");
        lblTableAndKOTNo21.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        lblTableAndKOTNo21.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblTableAndKOTNo21MouseClicked(evt);
            }
        });
        jPanel2.add(lblTableAndKOTNo21, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 360, 260, 30));

        scrollPane21.setBorder(null);
        scrollPane21.setViewportBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        scrollPane21.setMinimumSize(new java.awt.Dimension(55, 160));
        scrollPane21.setPreferredSize(new java.awt.Dimension(55, 160));
        scrollPane21.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                scrollPane21MouseClicked(evt);
            }
        });

        list21.setBackground(new java.awt.Color(0, 0, 0));
        list21.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        list21.setForeground(new java.awt.Color(255, 255, 255));
        list21.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        list21.setFixedCellHeight(35);
        list21.setFixedCellWidth(150);
        list21.setSelectionBackground(new java.awt.Color(0, 153, 255));
        list21.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                list21MouseClicked(evt);
            }
        });
        scrollPane21.setViewportView(list21);

        jPanel2.add(scrollPane21, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 390, 255, 330));

        lblTableAndKOTNo22.setFont(new java.awt.Font("Trebuchet MS", 1, 20)); // NOI18N
        lblTableAndKOTNo22.setText("00:00:00");
        lblTableAndKOTNo22.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        lblTableAndKOTNo22.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblTableAndKOTNo22MouseClicked(evt);
            }
        });
        jPanel2.add(lblTableAndKOTNo22, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 360, 270, 30));

        scrollPane22.setBorder(null);
        scrollPane22.setViewportBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        scrollPane22.setMinimumSize(new java.awt.Dimension(55, 160));
        scrollPane22.setPreferredSize(new java.awt.Dimension(55, 160));
        scrollPane22.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                scrollPane22MouseClicked(evt);
            }
        });

        list22.setBackground(new java.awt.Color(0, 0, 0));
        list22.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        list22.setForeground(new java.awt.Color(255, 255, 255));
        list22.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        list22.setFixedCellHeight(35);
        list22.setFixedCellWidth(200);
        list22.setSelectionBackground(new java.awt.Color(0, 153, 255));
        list22.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                list22MouseClicked(evt);
            }
        });
        scrollPane22.setViewportView(list22);

        jPanel2.add(scrollPane22, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 390, 255, 330));

        lblTableAndKOTNo23.setFont(new java.awt.Font("Trebuchet MS", 1, 20)); // NOI18N
        lblTableAndKOTNo23.setText("00:00:00");
        lblTableAndKOTNo23.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        lblTableAndKOTNo23.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblTableAndKOTNo23MouseClicked(evt);
            }
        });
        jPanel2.add(lblTableAndKOTNo23, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 360, 270, 30));

        scrollPane23.setBorder(null);
        scrollPane23.setViewportBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        scrollPane23.setMinimumSize(new java.awt.Dimension(55, 160));
        scrollPane23.setPreferredSize(new java.awt.Dimension(55, 160));
        scrollPane23.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                scrollPane23MouseClicked(evt);
            }
        });

        list23.setBackground(new java.awt.Color(0, 0, 0));
        list23.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        list23.setForeground(new java.awt.Color(255, 255, 255));
        list23.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        list23.setFixedCellHeight(35);
        list23.setFixedCellWidth(150);
        list23.setSelectionBackground(new java.awt.Color(0, 153, 255));
        list23.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                list23MouseClicked(evt);
            }
        });
        scrollPane23.setViewportView(list23);

        jPanel2.add(scrollPane23, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 390, 255, 330));

        lblTableAndKOTNo24.setFont(new java.awt.Font("Trebuchet MS", 1, 20)); // NOI18N
        lblTableAndKOTNo24.setText("00:00:00");
        lblTableAndKOTNo24.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblTableAndKOTNo24MouseClicked(evt);
            }
        });
        jPanel2.add(lblTableAndKOTNo24, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 360, 270, 30));

        scrollPane24.setBorder(null);
        scrollPane24.setViewportBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        scrollPane24.setMinimumSize(new java.awt.Dimension(55, 160));
        scrollPane24.setPreferredSize(new java.awt.Dimension(55, 160));
        scrollPane24.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                scrollPane24MouseClicked(evt);
            }
        });

        list24.setBackground(new java.awt.Color(0, 0, 0));
        list24.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        list24.setForeground(new java.awt.Color(255, 255, 255));
        list24.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        list24.setFixedCellHeight(35);
        list24.setFixedCellWidth(150);
        list24.setSelectionBackground(new java.awt.Color(0, 153, 255));
        list24.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                list24MouseClicked(evt);
            }
        });
        scrollPane24.setViewportView(list24);

        jPanel2.add(scrollPane24, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 390, 255, 330));

        lblTableAndKOTNo25.setFont(new java.awt.Font("Trebuchet MS", 1, 20)); // NOI18N
        lblTableAndKOTNo25.setText("00:00:00");
        lblTableAndKOTNo25.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblTableAndKOTNo25MouseClicked(evt);
            }
        });
        jPanel2.add(lblTableAndKOTNo25, new org.netbeans.lib.awtextra.AbsoluteConstraints(1100, 360, 260, 30));

        scrollPane25.setBorder(null);
        scrollPane25.setViewportBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        scrollPane25.setMinimumSize(new java.awt.Dimension(55, 160));
        scrollPane25.setPreferredSize(new java.awt.Dimension(55, 160));
        scrollPane25.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                scrollPane25MouseClicked(evt);
            }
        });

        list25.setBackground(new java.awt.Color(0, 0, 0));
        list25.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        list25.setForeground(new java.awt.Color(255, 255, 255));
        list25.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        list25.setFixedCellHeight(35);
        list25.setFixedCellWidth(150);
        list25.setSelectionBackground(new java.awt.Color(0, 153, 255));
        list25.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                list25MouseClicked(evt);
            }
        });
        scrollPane25.setViewportView(list25);

        jPanel2.add(scrollPane25, new org.netbeans.lib.awtextra.AbsoluteConstraints(1100, 390, 255, 330));

        tabbedPaneKDS.addTab("QUANTITY", jPanel2);

        panelBody.add(tabbedPaneKDS, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1360, 760));

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

    private void formWindowClosed(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosed
    {//GEN-HEADEREND:event_formWindowClosed
	clsGlobalVarClass.hmActiveForms.remove("KDSForKOTBookAndProcess");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosing
    {//GEN-HEADEREND:event_formWindowClosing
	clsGlobalVarClass.hmActiveForms.remove("KDSForKOTBookAndProcess");
    }//GEN-LAST:event_formWindowClosing

    private void scrollPane15MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_scrollPane15MouseClicked
	// TODO add your handling code here:
	funScrollPaneListClicked(0);
    }//GEN-LAST:event_scrollPane15MouseClicked

    private void list15MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_list15MouseClicked
	// TODO add your handling code here:
	funScrollPaneListClicked(0);
    }//GEN-LAST:event_list15MouseClicked

    private void scrollPane14MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_scrollPane14MouseClicked
	// TODO add your handling code here:
	funScrollPaneListClicked(1);
    }//GEN-LAST:event_scrollPane14MouseClicked

    private void list14MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_list14MouseClicked
	// TODO add your handling code here:
	funScrollPaneListClicked(1);
    }//GEN-LAST:event_list14MouseClicked

    private void scrollPane13MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_scrollPane13MouseClicked
	// TODO add your handling code here:
	funScrollPaneListClicked(2);
    }//GEN-LAST:event_scrollPane13MouseClicked

    private void list13MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_list13MouseClicked
	// TODO add your handling code here:
	funScrollPaneListClicked(2);
    }//GEN-LAST:event_list13MouseClicked

    private void scrollPane12MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_scrollPane12MouseClicked
	// TODO add your handling code here:
	funScrollPaneListClicked(3);
    }//GEN-LAST:event_scrollPane12MouseClicked

    private void list12MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_list12MouseClicked
	// TODO add your handling code here:
	funScrollPaneListClicked(3);
    }//GEN-LAST:event_list12MouseClicked

    private void scrollPane11MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_scrollPane11MouseClicked
	// TODO add your handling code here:
	funScrollPaneListClicked(4);
    }//GEN-LAST:event_scrollPane11MouseClicked

    private void list11MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_list11MouseClicked
	// TODO add your handling code here:
	funScrollPaneListClicked(4);
    }//GEN-LAST:event_list11MouseClicked

    private void scrollPane10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_scrollPane10MouseClicked
	// TODO add your handling code here:
	funScrollPaneListClicked(5);
    }//GEN-LAST:event_scrollPane10MouseClicked

    private void list10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_list10MouseClicked
	// TODO add your handling code here:
	funScrollPaneListClicked(5);
    }//GEN-LAST:event_list10MouseClicked

    private void scrollPane9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_scrollPane9MouseClicked
	// TODO add your handling code here:
	funScrollPaneListClicked(6);
    }//GEN-LAST:event_scrollPane9MouseClicked

    private void list9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_list9MouseClicked
	// TODO add your handling code here:
	funScrollPaneListClicked(6);
    }//GEN-LAST:event_list9MouseClicked

    private void lblSelectedItemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblSelectedItemMouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_lblSelectedItemMouseClicked

    private void lblSelectedKOTMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblSelectedKOTMouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_lblSelectedKOTMouseClicked

    private void lblTableAndKOTNo6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblTableAndKOTNo6MouseClicked
	funTableAndKOTLabelClicked(9);
    }//GEN-LAST:event_lblTableAndKOTNo6MouseClicked

    private void lblTableAndKOTNo5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblTableAndKOTNo5MouseClicked
	funTableAndKOTLabelClicked(10);
    }//GEN-LAST:event_lblTableAndKOTNo5MouseClicked

    private void lblTableAndKOTNo4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblTableAndKOTNo4MouseClicked
	funTableAndKOTLabelClicked(11);
    }//GEN-LAST:event_lblTableAndKOTNo4MouseClicked

    private void lblTableAndKOTNo3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblTableAndKOTNo3MouseClicked
	funTableAndKOTLabelClicked(12);
    }//GEN-LAST:event_lblTableAndKOTNo3MouseClicked

    private void lblTableAndKOTNo2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblTableAndKOTNo2MouseClicked
	funTableAndKOTLabelClicked(13);
    }//GEN-LAST:event_lblTableAndKOTNo2MouseClicked

    private void lblTableAndKOTNo1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblTableAndKOTNo1MouseClicked
	funTableAndKOTLabelClicked(14);
    }//GEN-LAST:event_lblTableAndKOTNo1MouseClicked

    private void lblTableAndKOTNo7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblTableAndKOTNo7MouseClicked
	funTableAndKOTLabelClicked(8);
    }//GEN-LAST:event_lblTableAndKOTNo7MouseClicked

    private void lblTableAndKOTNo8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblTableAndKOTNo8MouseClicked
	funTableAndKOTLabelClicked(7);
    }//GEN-LAST:event_lblTableAndKOTNo8MouseClicked

    private void btnKOTProcessMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnKOTProcessMouseClicked
	if (btnKOTProcess.isEnabled())
	{
	    if (mapSelectedKOTs.size() > 0)
	    {
		funButtonOrderProcessClicked();
	    }
	    else
	    {
		JOptionPane.showMessageDialog(null, "Please Select The KOT.");
		return;
	    }
	}
    }//GEN-LAST:event_btnKOTProcessMouseClicked

    private void btnItemProcessedMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnItemProcessedMouseClicked
	if (btnItemProcessed.isEnabled() && mapSelectedItems.size() > 0)
	{
	    if (funProcessItem(selectedIndexForItemProcessed))
	    {
		funRefreshForm();

		selectedIndexForItemProcessed = -1;
		btnItemProcessed.setEnabled(false);
	    }
	}
	else
	{
	    JOptionPane.showMessageDialog(null, "Please Select Item.");
	    return;
	}
    }//GEN-LAST:event_btnItemProcessedMouseClicked

    private void scrollPane8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_scrollPane8MouseClicked
	funScrollPaneMouseClicked(7);
    }//GEN-LAST:event_scrollPane8MouseClicked

    private void list8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_list8MouseClicked
	funScrollPaneListClicked(7);
    }//GEN-LAST:event_list8MouseClicked

    private void scrollPane7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_scrollPane7MouseClicked
	funScrollPaneMouseClicked(8);
    }//GEN-LAST:event_scrollPane7MouseClicked

    private void list7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_list7MouseClicked
	funScrollPaneListClicked(8);
    }//GEN-LAST:event_list7MouseClicked

    private void scrollPane6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_scrollPane6MouseClicked
	funScrollPaneMouseClicked(9);
    }//GEN-LAST:event_scrollPane6MouseClicked

    private void list6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_list6MouseClicked
	funScrollPaneListClicked(9);
    }//GEN-LAST:event_list6MouseClicked

    private void scrollPane5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_scrollPane5MouseClicked
	funScrollPaneMouseClicked(10);
    }//GEN-LAST:event_scrollPane5MouseClicked

    private void list5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_list5MouseClicked
	funScrollPaneListClicked(10);
    }//GEN-LAST:event_list5MouseClicked

    private void scrollPane4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_scrollPane4MouseClicked
	funScrollPaneMouseClicked(11);
    }//GEN-LAST:event_scrollPane4MouseClicked

    private void list4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_list4MouseClicked
	funScrollPaneListClicked(11);
    }//GEN-LAST:event_list4MouseClicked

    private void scrollPane3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_scrollPane3MouseClicked
	funScrollPaneMouseClicked(12);
    }//GEN-LAST:event_scrollPane3MouseClicked

    private void list3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_list3MouseClicked
	funScrollPaneListClicked(12);
    }//GEN-LAST:event_list3MouseClicked

    private void scrollPane2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_scrollPane2MouseClicked
	funScrollPaneMouseClicked(13);
    }//GEN-LAST:event_scrollPane2MouseClicked

    private void list2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_list2MouseClicked
	funScrollPaneListClicked(13);
    }//GEN-LAST:event_list2MouseClicked

    private void scrollPane1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_scrollPane1MouseClicked
	funScrollPaneMouseClicked(14);
    }//GEN-LAST:event_scrollPane1MouseClicked

    private void list1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_list1MouseClicked
	funScrollPaneListClicked(14);
    }//GEN-LAST:event_list1MouseClicked

    private void btnCloseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCloseMouseClicked

	funCloseKDS();
    }//GEN-LAST:event_btnCloseMouseClicked

    private void btnNewMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNewMouseClicked
	if (btnNew.isEnabled())
	{
	    funNewButtonClicked();
	}
    }//GEN-LAST:event_btnNewMouseClicked

    private void btnOldMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOldMouseClicked
	if (btnOld.isEnabled())
	{
	    refreshTimer.stop();
	    funOldButtonClicked();
	}
    }//GEN-LAST:event_btnOldMouseClicked

    private void lblTableAndKOTNo15MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblTableAndKOTNo15MouseClicked
	// TODO add your handling code here:
	funTableAndKOTLabelClicked(0);
    }//GEN-LAST:event_lblTableAndKOTNo15MouseClicked

    private void lblTableAndKOTNo14MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblTableAndKOTNo14MouseClicked
	// TODO add your handling code here:
	funTableAndKOTLabelClicked(1);
    }//GEN-LAST:event_lblTableAndKOTNo14MouseClicked

    private void lblTableAndKOTNo13MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblTableAndKOTNo13MouseClicked
	// TODO add your handling code here:
	funTableAndKOTLabelClicked(2);
    }//GEN-LAST:event_lblTableAndKOTNo13MouseClicked

    private void lblTableAndKOTNo12MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblTableAndKOTNo12MouseClicked
	// TODO add your handling code here:
	funTableAndKOTLabelClicked(3);
    }//GEN-LAST:event_lblTableAndKOTNo12MouseClicked

    private void lblTableAndKOTNo11MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblTableAndKOTNo11MouseClicked
	// TODO add your handling code here:
	funTableAndKOTLabelClicked(4);
    }//GEN-LAST:event_lblTableAndKOTNo11MouseClicked

    private void lblTableAndKOTNo10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblTableAndKOTNo10MouseClicked
	// TODO add your handling code here:
	funTableAndKOTLabelClicked(5);
    }//GEN-LAST:event_lblTableAndKOTNo10MouseClicked

    private void lblTableAndKOTNo9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblTableAndKOTNo9MouseClicked
	// TODO add your handling code here:
	funTableAndKOTLabelClicked(6);
    }//GEN-LAST:event_lblTableAndKOTNo9MouseClicked

    private void list12MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_list12MouseMoved
	// TODO add your handling code here:

    }//GEN-LAST:event_list12MouseMoved

    private void list14MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_list14MouseMoved
	// TODO add your handling code here:


    }//GEN-LAST:event_list14MouseMoved

    private void list14MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_list14MouseEntered
	// TODO add your handling code here:
	//funScrollPaneListMouseMoved(1);
    }//GEN-LAST:event_list14MouseEntered

    private void lblTableAndKOTNo16MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblTableAndKOTNo16MouseClicked
    {//GEN-HEADEREND:event_lblTableAndKOTNo16MouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_lblTableAndKOTNo16MouseClicked

    private void list16MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_list16MouseClicked
    {//GEN-HEADEREND:event_list16MouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_list16MouseClicked

    private void scrollPane16MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_scrollPane16MouseClicked
    {//GEN-HEADEREND:event_scrollPane16MouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_scrollPane16MouseClicked

    private void lblTableAndKOTNo17MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblTableAndKOTNo17MouseClicked
    {//GEN-HEADEREND:event_lblTableAndKOTNo17MouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_lblTableAndKOTNo17MouseClicked

    private void list17MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_list17MouseClicked
    {//GEN-HEADEREND:event_list17MouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_list17MouseClicked

    private void scrollPane17MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_scrollPane17MouseClicked
    {//GEN-HEADEREND:event_scrollPane17MouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_scrollPane17MouseClicked

    private void lblTableAndKOTNo18MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblTableAndKOTNo18MouseClicked
    {//GEN-HEADEREND:event_lblTableAndKOTNo18MouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_lblTableAndKOTNo18MouseClicked

    private void list18MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_list18MouseClicked
    {//GEN-HEADEREND:event_list18MouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_list18MouseClicked

    private void scrollPane18MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_scrollPane18MouseClicked
    {//GEN-HEADEREND:event_scrollPane18MouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_scrollPane18MouseClicked

    private void lblTableAndKOTNo19MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblTableAndKOTNo19MouseClicked
    {//GEN-HEADEREND:event_lblTableAndKOTNo19MouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_lblTableAndKOTNo19MouseClicked

    private void list19MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_list19MouseClicked
    {//GEN-HEADEREND:event_list19MouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_list19MouseClicked

    private void scrollPane19MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_scrollPane19MouseClicked
    {//GEN-HEADEREND:event_scrollPane19MouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_scrollPane19MouseClicked

    private void lblTableAndKOTNo20MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblTableAndKOTNo20MouseClicked
    {//GEN-HEADEREND:event_lblTableAndKOTNo20MouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_lblTableAndKOTNo20MouseClicked

    private void list20MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_list20MouseClicked
    {//GEN-HEADEREND:event_list20MouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_list20MouseClicked

    private void scrollPane20MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_scrollPane20MouseClicked
    {//GEN-HEADEREND:event_scrollPane20MouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_scrollPane20MouseClicked

    private void lblTableAndKOTNo21MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblTableAndKOTNo21MouseClicked
    {//GEN-HEADEREND:event_lblTableAndKOTNo21MouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_lblTableAndKOTNo21MouseClicked

    private void list21MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_list21MouseClicked
    {//GEN-HEADEREND:event_list21MouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_list21MouseClicked

    private void scrollPane21MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_scrollPane21MouseClicked
    {//GEN-HEADEREND:event_scrollPane21MouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_scrollPane21MouseClicked

    private void lblTableAndKOTNo22MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblTableAndKOTNo22MouseClicked
    {//GEN-HEADEREND:event_lblTableAndKOTNo22MouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_lblTableAndKOTNo22MouseClicked

    private void list22MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_list22MouseClicked
    {//GEN-HEADEREND:event_list22MouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_list22MouseClicked

    private void scrollPane22MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_scrollPane22MouseClicked
    {//GEN-HEADEREND:event_scrollPane22MouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_scrollPane22MouseClicked

    private void lblTableAndKOTNo23MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblTableAndKOTNo23MouseClicked
    {//GEN-HEADEREND:event_lblTableAndKOTNo23MouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_lblTableAndKOTNo23MouseClicked

    private void list23MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_list23MouseClicked
    {//GEN-HEADEREND:event_list23MouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_list23MouseClicked

    private void scrollPane23MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_scrollPane23MouseClicked
    {//GEN-HEADEREND:event_scrollPane23MouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_scrollPane23MouseClicked

    private void lblTableAndKOTNo24MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblTableAndKOTNo24MouseClicked
    {//GEN-HEADEREND:event_lblTableAndKOTNo24MouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_lblTableAndKOTNo24MouseClicked

    private void list24MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_list24MouseClicked
    {//GEN-HEADEREND:event_list24MouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_list24MouseClicked

    private void scrollPane24MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_scrollPane24MouseClicked
    {//GEN-HEADEREND:event_scrollPane24MouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_scrollPane24MouseClicked

    private void lblTableAndKOTNo25MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lblTableAndKOTNo25MouseClicked
    {//GEN-HEADEREND:event_lblTableAndKOTNo25MouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_lblTableAndKOTNo25MouseClicked

    private void list25MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_list25MouseClicked
    {//GEN-HEADEREND:event_list25MouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_list25MouseClicked

    private void scrollPane25MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_scrollPane25MouseClicked
    {//GEN-HEADEREND:event_scrollPane25MouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_scrollPane25MouseClicked

    private void tabbedPaneKDSStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_tabbedPaneKDSStateChanged
    {//GEN-HEADEREND:event_tabbedPaneKDSStateChanged
	funTabbChanged();
    }//GEN-LAST:event_tabbedPaneKDSStateChanged

    /**
     * @param args the command line arguments
     */
    public static void main(String args[])
    {
	/*
         * Set the Nimbus look and feel
	 */
	//<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
	/*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
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
	    java.util.logging.Logger.getLogger(frmKDSForKOT1366x768Resolution.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	}
	catch (InstantiationException ex)
	{
	    java.util.logging.Logger.getLogger(frmKDSForKOT1366x768Resolution.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	}
	catch (IllegalAccessException ex)
	{
	    java.util.logging.Logger.getLogger(frmKDSForKOT1366x768Resolution.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	}
	catch (javax.swing.UnsupportedLookAndFeelException ex)
	{
	    java.util.logging.Logger.getLogger(frmKDSForKOT1366x768Resolution.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	}
	//</editor-fold>
	//</editor-fold>
	//</editor-fold>
	//</editor-fold>
	//</editor-fold>
	//</editor-fold>
	//</editor-fold>
	//</editor-fold>
	//</editor-fold>
	//</editor-fold>
	//</editor-fold>
	//</editor-fold>
	//</editor-fold>
	//</editor-fold>
	//</editor-fold>
	//</editor-fold>

	/*
         * Create and display the form
	 */
//        java.awt.EventQueue.invokeLater(new Runnable()
//        {
//            public void run()
//            {
//                new frmKDSForKOTBookAndProcess().setVisible(true);
//            }
//        });
	//</editor-fold>
	//</editor-fold>
	//</editor-fold>
	//</editor-fold>
	//</editor-fold>
	//</editor-fold>
	//</editor-fold>
	//</editor-fold>
	//</editor-fold>
	//</editor-fold>
	//</editor-fold>
	//</editor-fold>
	//</editor-fold>
	//</editor-fold>
	//</editor-fold>
	//</editor-fold>

	/*
         * Create and display the form
	 */
//        java.awt.EventQueue.invokeLater(new Runnable()
//        {
//            public void run()
//            {
//                new frmKDSForKOTBookAndProcess().setVisible(true);
//            }
//        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnItemProcessed;
    private javax.swing.JButton btnKOTProcess;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnOld;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lblBillDelay1;
    private javax.swing.JLabel lblBillDelay10;
    private javax.swing.JLabel lblBillDelay11;
    private javax.swing.JLabel lblBillDelay12;
    private javax.swing.JLabel lblBillDelay13;
    private javax.swing.JLabel lblBillDelay14;
    private javax.swing.JLabel lblBillDelay15;
    private javax.swing.JLabel lblBillDelay2;
    private javax.swing.JLabel lblBillDelay3;
    private javax.swing.JLabel lblBillDelay4;
    private javax.swing.JLabel lblBillDelay5;
    private javax.swing.JLabel lblBillDelay6;
    private javax.swing.JLabel lblBillDelay7;
    private javax.swing.JLabel lblBillDelay8;
    private javax.swing.JLabel lblBillDelay9;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblHOSign;
    private javax.swing.JLabel lblModuleName;
    private javax.swing.JLabel lblPosName;
    private javax.swing.JLabel lblProductName;
    private javax.swing.JLabel lblSelectedItem;
    private javax.swing.JLabel lblSelectedKOT;
    private javax.swing.JLabel lblTableAndKOTNo1;
    private javax.swing.JLabel lblTableAndKOTNo10;
    private javax.swing.JLabel lblTableAndKOTNo11;
    private javax.swing.JLabel lblTableAndKOTNo12;
    private javax.swing.JLabel lblTableAndKOTNo13;
    private javax.swing.JLabel lblTableAndKOTNo14;
    private javax.swing.JLabel lblTableAndKOTNo15;
    private javax.swing.JLabel lblTableAndKOTNo16;
    private javax.swing.JLabel lblTableAndKOTNo17;
    private javax.swing.JLabel lblTableAndKOTNo18;
    private javax.swing.JLabel lblTableAndKOTNo19;
    private javax.swing.JLabel lblTableAndKOTNo2;
    private javax.swing.JLabel lblTableAndKOTNo20;
    private javax.swing.JLabel lblTableAndKOTNo21;
    private javax.swing.JLabel lblTableAndKOTNo22;
    private javax.swing.JLabel lblTableAndKOTNo23;
    private javax.swing.JLabel lblTableAndKOTNo24;
    private javax.swing.JLabel lblTableAndKOTNo25;
    private javax.swing.JLabel lblTableAndKOTNo3;
    private javax.swing.JLabel lblTableAndKOTNo4;
    private javax.swing.JLabel lblTableAndKOTNo5;
    private javax.swing.JLabel lblTableAndKOTNo6;
    private javax.swing.JLabel lblTableAndKOTNo7;
    private javax.swing.JLabel lblTableAndKOTNo8;
    private javax.swing.JLabel lblTableAndKOTNo9;
    private javax.swing.JLabel lblUserCode;
    private javax.swing.JLabel lblformName;
    private javax.swing.JList list1;
    private javax.swing.JList list10;
    private javax.swing.JList list11;
    private javax.swing.JList list12;
    private javax.swing.JList list13;
    private javax.swing.JList list14;
    private javax.swing.JList list15;
    private javax.swing.JList list16;
    private javax.swing.JList list17;
    private javax.swing.JList list18;
    private javax.swing.JList list19;
    private javax.swing.JList list2;
    private javax.swing.JList list20;
    private javax.swing.JList list21;
    private javax.swing.JList list22;
    private javax.swing.JList list23;
    private javax.swing.JList list24;
    private javax.swing.JList list25;
    private javax.swing.JList list3;
    private javax.swing.JList list4;
    private javax.swing.JList list5;
    private javax.swing.JList list6;
    private javax.swing.JList list7;
    private javax.swing.JList list8;
    private javax.swing.JList list9;
    private javax.swing.JPanel panelBody;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelMain;
    private javax.swing.JScrollPane scrollPane1;
    private javax.swing.JScrollPane scrollPane10;
    private javax.swing.JScrollPane scrollPane11;
    private javax.swing.JScrollPane scrollPane12;
    private javax.swing.JScrollPane scrollPane13;
    private javax.swing.JScrollPane scrollPane14;
    private javax.swing.JScrollPane scrollPane15;
    private javax.swing.JScrollPane scrollPane16;
    private javax.swing.JScrollPane scrollPane17;
    private javax.swing.JScrollPane scrollPane18;
    private javax.swing.JScrollPane scrollPane19;
    private javax.swing.JScrollPane scrollPane2;
    private javax.swing.JScrollPane scrollPane20;
    private javax.swing.JScrollPane scrollPane21;
    private javax.swing.JScrollPane scrollPane22;
    private javax.swing.JScrollPane scrollPane23;
    private javax.swing.JScrollPane scrollPane24;
    private javax.swing.JScrollPane scrollPane25;
    private javax.swing.JScrollPane scrollPane3;
    private javax.swing.JScrollPane scrollPane4;
    private javax.swing.JScrollPane scrollPane5;
    private javax.swing.JScrollPane scrollPane6;
    private javax.swing.JScrollPane scrollPane7;
    private javax.swing.JScrollPane scrollPane8;
    private javax.swing.JScrollPane scrollPane9;
    private javax.swing.JTabbedPane tabbedPaneKDS;
    // End of variables declaration//GEN-END:variables

    private void funScrollPaneMouseClicked(int index)
    {
//        boolean isSelected = funCheckSelectedOrDeselected(index);
//        if (isSelected)
//        {
//            funDeSelectScrollPane(index);
//        }
//        else
//        {
//            funSelectScrollPane(index);
//        }
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

	listOfKOTsToBeProcess.remove(listOfKOTs.get((navigator * 15) + index).get(0).getStrKOTNo());

//        if (listOfKOTsToBeProcess.size() > 0)
//        {
//            btnOrderProcess.setEnabled(true);
//        }
//        else
//        {
//            btnOrderProcess.setEnabled(false);
//        }
    }

    private void funSelectScrollPane(int index)
    {
	scrollPaneArray[index].setBorder(new BevelBorder(BevelBorder.LOWERED, Color.RED, Color.RED));

	listOfKOTsToBeProcess.add(listOfKOTs.get((navigator * 15) + index).get(0).getStrKOTNo());

//        if (listOfKOTsToBeProcess.size() > 0)
//        {
//            btnOrderProcess.setEnabled(true);
//        }
//        else
//        {
//            btnOrderProcess.setEnabled(false);
//        }
    }

    private void funResetDefault()
    {
	btnNew.setEnabled(false);
	btnOld.setEnabled(false);
	navigatorNew = 0;
	navigator = 0;
	navigatorForMenuHead = 0;
	funSetScrollPanesVisisble(false);
	funSetCustomListCellRenderer();
	mapKOTHd.clear();
	listOfKOTs.clear();
	listOfKOTsToBeProcess.clear();
    }

    private void fumLoadMapKOTlHd()
    {
	try
	{

	    String posDate = clsGlobalVarClass.gPOSDateForTransaction.split(" ")[0];

	    String sqlBillDtl = "(SELECT a.strKOTNo,a.strItemCode,a.strItemName,a.dblRate, SUM(a.dblItemQuantity), SUM(a.dblAmount) "
		    + ", DATE_FORMAT(DATE(a.dteDateCreated),'%d-%m-%Y') AS dteKOTDate, TIME(a.dteDateCreated) AS tmeKOTTime "
		    + ",a.strTableNo,b.strTableName, IF(TIME_TO_SEC(TIMEDIFF(CURRENT_TIME(), TIME(a.dteDateCreated)))>(c.intProcTimeMin*60), IF(TIME_TO_SEC(TIMEDIFF(CURRENT_TIME(), TIME(a.dteDateCreated)))>(c.tmeTargetMiss*60),'RED','ORANGE'),'BLACK') "
		    + ",'Order',a.strWaiterNo "
		    + "FROM tblitemrtemp a "
		    + ",tbltablemaster b "
		    + ",tblitemmaster c,tblmenuitempricingdtl d,tblcostcentermaster e "
		    + "WHERE LEFT(a.strItemCode,7)=c.strItemCode AND a.strNCKotYN='N' "
		    + "AND a.tdhComboItemYN='N' AND a.strTableNo=b.strTableNo "
		    + "AND a.strItemProcessed='N' AND c.strItemCode=d.strItemCode "
		    + "AND a.strPOSCode=d.strPosCode AND (d.strPosCode='" + clsGlobalVarClass.gPOSCode + "' OR d.strPosCode='All') "
		    + "AND d.strCostCenterCode=e.strCostCenterCode AND e.strCostCenterCode in " + funGetCostCenterCodes() + " "
		    + "GROUP BY a.strTableNo,a.strKOTNo,a.strItemCode,a.strItemName "
		    + "ORDER BY a.dteDateCreated DESC, TIME(a.dteDateCreated) DESC) "
		    + "union all( "
		    + "SELECT a.strKOTNo,a.strItemCode,a.strItemName,SUM(a.dblAmount)/SUM(a.dblItemQuantity), SUM(a.dblItemQuantity), SUM(a.dblAmount) "
		    + ", DATE_FORMAT(DATE(a.dteDateCreated),'%d-%m-%Y') AS dteKOTDate, TIME(a.dteDateCreated) AS tmeKOTTime "
		    + ",a.strTableNo,b.strTableName, IF(TIME_TO_SEC(TIMEDIFF(CURRENT_TIME(), TIME(a.dteDateCreated)))>(c.intProcTimeMin*60), IF(TIME_TO_SEC(TIMEDIFF(CURRENT_TIME(), TIME(a.dteDateCreated)))>(c.tmeTargetMiss*60),'RED','ORANGE'),'BLACK') "
		    + ",'Void',a.strWaiterNo "
		    + "FROM tblvoidkot a "
		    + ",tbltablemaster b,tblitemmaster c,tblmenuitempricingdtl d,tblcostcentermaster e "
		    + "WHERE LEFT(a.strItemCode,7)=c.strItemCode AND a.strTableNo=b.strTableNo "
		    + "AND c.strItemCode=d.strItemCode AND a.strPOSCode=d.strPosCode "
		    + "AND (d.strPosCode='" + clsGlobalVarClass.gPOSCode + "' OR d.strPosCode='All') AND d.strCostCenterCode=e.strCostCenterCode "
		    + "AND e.strCostCenterCode in " + funGetCostCenterCodes() + " And date(a.dteVoidedDate)='" + posDate + "'  "
		    + "AND a.strItemProcessed='N'  "
		    + "GROUP BY a.strTableNo,a.strKOTNo,a.strItemCode,a.strItemName "
		    + "ORDER BY a.dteDateCreated DESC, TIME(a.dteDateCreated) DESC"
		    + ")ORDER BY strKOTNo DESC,dteKOTDate DESC ";
	    int ITEMCOUNTER = 0;
	    if (funGetCostCenterCodes().length() > 2)
	    {
		ResultSet resultSet = clsGlobalVarClass.dbMysql.executeResultSet(sqlBillDtl);
		while (resultSet.next())
		{
		    clsBillDtl objKOTDtl = new clsBillDtl();
		    String kotNo = resultSet.getString(1);

		    String[] kotDate = resultSet.getString(7).split("-");
		    objKOTDtl.setStrKOTNo(kotNo);
		    objKOTDtl.setStrItemCode(resultSet.getString(2));
		    objKOTDtl.setStrItemName(resultSet.getString(3) + "!" + resultSet.getString(11));
		    objKOTDtl.setDblRate(resultSet.getDouble(4));
		    objKOTDtl.setDblQuantity(resultSet.getDouble(5));
		    objKOTDtl.setDblAmount(resultSet.getDouble(6));
		    objKOTDtl.setDteNCKOTDate(resultSet.getString(8));
		    objKOTDtl.setStrTableName(resultSet.getString(10));
		    objKOTDtl.setStrRemark(resultSet.getString(12));
		    objKOTDtl.setStrWaiterNo(resultSet.getString(13));
		    objKOTDtl.setDteBillDate(kotDate[2] + "-" + kotDate[1] + "-" + kotDate[0] + " " + resultSet.getString(8));

		    ITEMCOUNTER++;

		    if (mapKOTHd.containsKey(kotNo))
		    {
			mapKOTHd.get(kotNo).add(objKOTDtl);
		    }
		    else
		    {
			ArrayList<clsBillDtl> lisKOTItemDtl = new ArrayList<clsBillDtl>();
			lisKOTItemDtl.add(objKOTDtl);
			mapKOTHd.put(kotNo, lisKOTItemDtl);
		    }
		}
		resultSet.close();
	    }

	    /**
	     * For Direct Biller
	     */
	    sqlBillDtl = " SELECT a.strBillNo,a.strItemCode,a.strItemName,a.dblRate,sum(a.dblQuantity),sum(a.dblAmount),a.tmeOrderProcessing,time(a.dteBillDate) "
		    + ",b.strOperationType,a.dteBillDate "
		    + "FROM tblbilldtl a "
		    + "join tblbillhd b on a.strBillNo=b.strBillNo "
		    + "join tblmenuitempricingdtl d on a.strItemCode=d.strItemCode "
		    + "LEFT OUTER JOIN tblkdsprocess c ON a.strBillNo=c.strDocNo AND a.strItemCode=c.strItemCode "
		    + "WHERE b.strOperationType!='DineIn'  "
		    + "AND c.strItemCode IS NULL "
		    + "and (b.strPOSCode=d.strPosCode or d.strPosCode='All') "
		    + "and d.strCostCenterCode in " + funGetCostCenterCodes() + " "
		    + "GROUP BY a.strBillNo,a.strKOTNo,a.strItemCode "
		    + "ORDER BY a.dteBillDate DESC, TIME(a.dteBillDate) DESC  ";
	    if (funGetCostCenterCodes().length() > 2)
	    {
		ResultSet resultSet = clsGlobalVarClass.dbMysql.executeResultSet(sqlBillDtl);
		while (resultSet.next())
		{
		    clsBillDtl billItemDtl = new clsBillDtl();

		    String billNo = resultSet.getString(1);
		    billItemDtl.setStrKOTNo(billNo);
		    billItemDtl.setStrItemCode(resultSet.getString(2));
		    billItemDtl.setStrItemName(resultSet.getString(3) + "!" + "WHITE");
		    billItemDtl.setDblRate(resultSet.getDouble(4));
		    billItemDtl.setDblQuantity(resultSet.getDouble(5));
		    billItemDtl.setDblAmount(resultSet.getDouble(6));
		    billItemDtl.setDteNCKOTDate(resultSet.getString(8));

		    String tableName = "DB";
		    if (resultSet.getString(9).equalsIgnoreCase("DirectBiller"))
		    {
			tableName = "DB";
		    }
		    else if (resultSet.getString(9).equalsIgnoreCase("HomeDelivery"))
		    {
			tableName = "HD";
		    }
		    else if (resultSet.getString(9).equalsIgnoreCase("TakeAway"))
		    {
			tableName = "TA";
		    }
		    billItemDtl.setStrTableName(tableName);

		    billItemDtl.setStrRemark("");
		    billItemDtl.setStrWaiterNo("");
		    billItemDtl.setDteBillDate(resultSet.getString(10));

		    ITEMCOUNTER++;

		    if (mapKOTHd.containsKey(billNo))
		    {
			mapKOTHd.get(billNo).add(billItemDtl);

			String sqlModifierDtl = " SELECT b.strModifierCode,b.strModifierName,sum(b.dblQuantity),sum(b.dblAmount),a.strDefaultModifier,b.strDefaultModifierDeselectedYN  "
				+ " FROM tblbillmodifierdtl b,tblitemmodofier a "
				+ " WHERE "
				+ " a.strItemCode=left(b.strItemCode,7) "
				+ " and a.strModifierCode=b.strModifierCode "
				+ " and b.strBillNo=? AND LEFT(b.strItemCode,7)=? "
				+ " group by b.strBillNo,b.strItemCode ";
			PreparedStatement prst = clsGlobalVarClass.conPrepareStatement.prepareStatement(sqlModifierDtl);
			prst.setString(1, billNo);
			prst.setString(2, resultSet.getString(2));
			ResultSet modiResultSet = prst.executeQuery();
			while (modiResultSet.next())
			{
			    if (modiResultSet.getString(5).equalsIgnoreCase("N"))
			    {
				clsBillDtl billItemModiDtl = new clsBillDtl();

				billItemModiDtl.setStrItemCode(modiResultSet.getString(1));
				billItemModiDtl.setStrItemName(modiResultSet.getString(2) + "!" + "WHITE");
				billItemModiDtl.setDblQuantity(modiResultSet.getDouble(3));

				mapKOTHd.get(billNo).add(billItemModiDtl);

				ITEMCOUNTER++;
			    }
			    else if (modiResultSet.getString(5).equalsIgnoreCase("Y") && modiResultSet.getString(6).equalsIgnoreCase("Y"))
			    {
				clsBillDtl billItemModiDtl = new clsBillDtl();

				billItemModiDtl.setStrItemCode(modiResultSet.getString(1));
				billItemModiDtl.setStrItemName("No" + modiResultSet.getString(2) + "!" + "WHITE");
				billItemModiDtl.setDblQuantity(modiResultSet.getDouble(3));

				mapKOTHd.get(billNo).add(billItemModiDtl);

				ITEMCOUNTER++;
			    }
			}
		    }
		    else
		    {
			ArrayList<clsBillDtl> listBillItemDtl = new ArrayList<clsBillDtl>();

			listBillItemDtl.add(billItemDtl);

			mapKOTHd.put(billNo, listBillItemDtl);

			String sqlModifierDtl = " SELECT b.strModifierCode,b.strModifierName,sum(b.dblQuantity),sum(b.dblAmount),a.strDefaultModifier,b.strDefaultModifierDeselectedYN  "
				+ " FROM tblbillmodifierdtl b,tblitemmodofier a "
				+ " WHERE "
				+ " a.strItemCode=left(b.strItemCode,7) "
				+ " and a.strModifierCode=b.strModifierCode "
				+ " and b.strBillNo=? AND LEFT(b.strItemCode,7)=? "
				+ " group by b.strBillNo,b.strItemCode ";
			PreparedStatement prst = clsGlobalVarClass.conPrepareStatement.prepareStatement(sqlModifierDtl);
			prst.setString(1, billNo);
			prst.setString(2, resultSet.getString(2));
			ResultSet modiResultSet = prst.executeQuery();
			while (modiResultSet.next())
			{
			    if (!modiResultSet.getString(5).equalsIgnoreCase("Y"))
			    {
				clsBillDtl billItemModiDtl = new clsBillDtl();

				billItemModiDtl.setStrItemCode(modiResultSet.getString(1));
				billItemModiDtl.setStrItemName(modiResultSet.getString(2) + "!" + "WHITE");
				billItemModiDtl.setDblQuantity(modiResultSet.getDouble(3));

				mapKOTHd.get(billNo).add(billItemModiDtl);

				ITEMCOUNTER++;
			    }
			    else if (modiResultSet.getString(5).equalsIgnoreCase("Y") && modiResultSet.getString(6).equalsIgnoreCase("Y"))
			    {
				clsBillDtl billItemModiDtl = new clsBillDtl();

				billItemModiDtl.setStrItemCode(modiResultSet.getString(1));
				billItemModiDtl.setStrItemName("No" + modiResultSet.getString(2));
				billItemModiDtl.setDblQuantity(modiResultSet.getDouble(3));

				mapKOTHd.get(billNo).add(billItemModiDtl);

				ITEMCOUNTER++;
			    }
			}
		    }
		}
	    }
	    /**
	     * End for Direct Biller
	     */

	    if (ITEMCOUNTER > gITEMCOUNTER)
	    {
		tabbedPaneKDS.setSelectedIndex(0);
		funPlayNewOrderNotificationAlert();
	    }
	    if (ITEMCOUNTER < gITEMCOUNTER)
	    {
		tabbedPaneKDS.setSelectedIndex(0);
		funPlayProcessNotificationAlert();
	    }

	    gITEMCOUNTER = ITEMCOUNTER;
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funSetScrollPaneData(int index)//index of scrollPane
    {
	final String[] billItemList = funGetListDtl((navigator * 15) + index);//index of bill in list

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

	    @Override
	    public void add(int index, Object element)
	    {
		super.add(index, element); //To change body of generated methods, choose Tools | Templates.
	    }

	    @Override
	    public void addElement(Object element)
	    {
		super.addElement(element); //To change body of generated methods, choose Tools | Templates.
	    }

	};

	listViewArray[index].setModel(listModel);

	funDeSelectScrollPane(index);
	scrollPaneArray[index].setVisible(true);

	lblKOTDelayArray[index].setVisible(true);
	clsBillDtl objKOTDtl = listOfKOTs.get((navigator * 15) + index).get(0);
	//   ((TitledBorder) scrollPaneArray[index].getViewportBorder()).setTitle(objKOTDtl.getStrTableName() + "   " + objKOTDtl.getStrKOTNo());

	lblTableAndKOTNoArray[index].setText(objKOTDtl.getStrTableName() + "   " + objKOTDtl.getStrKOTNo());
	lblTableAndKOTNoArray[index].setVisible(true);
	if (lblSelectedKOT.equals(objKOTDtl.getStrKOTNo()))
	{
	    lblTableAndKOTNoArray[index].setForeground(Color.red);
	}

    }

    private void funSetScrollPaneDataForMenuHead(int index)//index of scrollPane
    {
	final String[] billItemList = funGetListDtlForMenuHeaad((navigatorForMenuHead * 15) + index);//index of bill in list

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

	    @Override
	    public void add(int index, Object element)
	    {
		super.add(index, element); //To change body of generated methods, choose Tools | Templates.
	    }

	    @Override
	    public void addElement(Object element)
	    {
		super.addElement(element); //To change body of generated methods, choose Tools | Templates.
	    }

	};

	listViewArrayForMenuHead[index].setModel(listModel);

	//funDeSelectScrollPane(index);
	scrollPaneArrayForMenuHead[index].setVisible(true);

	//lblKOTDelayArray[index].setVisible(true);
	clsBillDtl objKOTDtl = listOfMenus.get((navigatorForMenuHead * 15) + index).get(0);
	//   ((TitledBorder) scrollPaneArray[index].getViewportBorder()).setTitle(objKOTDtl.getStrTableName() + "   " + objKOTDtl.getStrKOTNo());

	lblTableAndKOTNoArrayForMenuHead[index].setText(objKOTDtl.getStrKOTNo());
	lblTableAndKOTNoArrayForMenuHead[index].setVisible(true);
//	if (lblSelectedKOT.equals(objKOTDtl.getStrKOTNo()))
//	{
//	    lblTableAndKOTNoArrayForMenuHead[index].setForeground(Color.red);
//	}

    }

    private String[] funGetListDtl(int billIndex)
    {
	ArrayList<clsBillDtl> listBillItemDtl = listOfKOTs.get(billIndex);
	String[] modelList = new String[listBillItemDtl.size()];
	int itemIndex = 0;
	for (int i = 0; i < listBillItemDtl.size(); i++)
	{
	    clsBillDtl objBillItemDtl = listBillItemDtl.get(i);
	    if (objBillItemDtl.getStrItemName().startsWith("-->"))
	    {
		modelList[itemIndex++] = " " + " " + objBillItemDtl.getStrItemName() + "!" + objBillItemDtl.getStrRemark() + "!" + objBillItemDtl.getStrWaiterNo() + "!" + objBillItemDtl.getDteBillDate() + "!" + objBillItemDtl.getStrItemCode() + "!" + objBillItemDtl.getStrItemCode();
	    }
	    else
	    {
		modelList[itemIndex++] = objBillItemDtl.getDblQuantity() + " " + objBillItemDtl.getStrItemName() + "!" + objBillItemDtl.getStrRemark() + "!" + objBillItemDtl.getStrWaiterNo() + "!" + objBillItemDtl.getDteBillDate() + "!" + objBillItemDtl.getStrItemCode() + "!" + objBillItemDtl.getStrItemCode();
	    }

	    //modifiers could be added here but check the flow of coding.
	}

	return modelList;
    }

    private String[] funGetListDtlForMenuHeaad(int billIndex)
    {
	ArrayList<clsBillDtl> listBillItemDtl = listOfMenus.get(billIndex);
	String[] modelList = new String[listBillItemDtl.size()];
	int itemIndex = 0;
	for (int i = 0; i < listBillItemDtl.size(); i++)
	{
	    clsBillDtl objBillItemDtl = listBillItemDtl.get(i);
	    if (objBillItemDtl.getStrItemName().startsWith("-->"))
	    {
		modelList[itemIndex++] = " " + " " + objBillItemDtl.getStrItemName();
	    }
	    else
	    {
		modelList[itemIndex++] = objBillItemDtl.getDblQuantity() + " " + objBillItemDtl.getStrItemName();
	    }

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

    private void funLoadScrollPanesForMenu(int startIndex, int endIndex)
    {
	funSetScrollPanesVisisbleForMenuHead(false);

	for (int i = startIndex; i <= endIndex; i++)
	{
	    funSetScrollPaneDataForMenuHead(i);
	}
    }

    private void funSetScrollPanesVisisble(boolean flag)
    {
	for (int i = 0; i < 15; i++)
	{
	    scrollPaneArray[i].setColumnHeader(null);
	    scrollPaneArray[i].setColumnHeaderView(null);
	    scrollPaneArray[i].setVisible(flag);

	    //lblKOTDelayArray[i].setText("00:00:00");
	    lblKOTDelayArray[i].setVisible(flag);

	    lblTableAndKOTNoArray[i].setVisible(flag);
	}
    }

    private void funSetScrollPanesVisisbleForMenuHead(boolean flag)
    {
	for (int i = 0; i < 10; i++)
	{
	    scrollPaneArrayForMenuHead[i].setColumnHeader(null);
	    scrollPaneArrayForMenuHead[i].setColumnHeaderView(null);
	    scrollPaneArrayForMenuHead[i].setVisible(flag);

//	    //lblKOTDelayArray[i].setText("00:00:00");
//	    lblKOTDelayArray[i].setVisible(flag);
	    lblTableAndKOTNoArrayForMenuHead[i].setVisible(flag);
	}
    }

    private void funLoadBillArrayList()
    {
	Iterator<Map.Entry<String, ArrayList<clsBillDtl>>> it = mapKOTHd.entrySet().iterator();
	while (it.hasNext())
	{
	    Map.Entry<String, ArrayList<clsBillDtl>> entry = it.next();
	    listOfKOTs.add(entry.getValue());
	}
    }

    private void funLoadArrayListForMenuHead()
    {
	Iterator<Map.Entry<String, Map<String, clsBillDtl>>> it = mapMenuHd.entrySet().iterator();
	while (it.hasNext())
	{
	    Map.Entry<String, Map<String, clsBillDtl>> entry = it.next();
	    Map<String, clsBillDtl> mapMenuHeadItems = entry.getValue();

	    ArrayList<clsBillDtl> listOfItems = new ArrayList<>();
	    for (clsBillDtl objBillDtl : mapMenuHeadItems.values())
	    {
		listOfItems.add(objBillDtl);
	    }
	    listOfMenus.add(listOfItems);
	}
    }

    public void funSetSelectedCostCenter(List<clsCostCenterBean> listOfSelectedCostCenters)
    {
	this.listOfSelectedCostCenters = listOfSelectedCostCenters;
    }

    private String funGetCostCenterCodes()
    {
	StringBuilder stringBuilder = new StringBuilder("(");
	for (int i = 0; this.listOfSelectedCostCenters != null && i < this.listOfSelectedCostCenters.size(); i++)
	{
	    if (i == 0)
	    {
		stringBuilder.append("'" + listOfSelectedCostCenters.get(i).getStrCostCenterCode() + "'");
	    }
	    else
	    {
		stringBuilder.append(",'" + listOfSelectedCostCenters.get(i).getStrCostCenterCode() + "'");
	    }
	}

	stringBuilder.append(")");

	lblformName.setText("KDS FOR " + funGetCostCenterNames());

	return stringBuilder.toString();
    }

    private String funGetCostCenterNames()
    {
	StringBuilder stringBuilder = new StringBuilder("");
	for (int i = 0; this.listOfSelectedCostCenters != null && i < this.listOfSelectedCostCenters.size(); i++)
	{
	    if (i == 0)
	    {
		stringBuilder.append("'" + listOfSelectedCostCenters.get(i).getStrCostCenterName() + "'");
	    }
	    else
	    {
		stringBuilder.append(",'" + listOfSelectedCostCenters.get(i).getStrCostCenterName() + "'");
	    }
	}

	stringBuilder.append("");

	return stringBuilder.toString();
    }

}
