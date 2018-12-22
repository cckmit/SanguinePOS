/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSTransaction.view;

import com.POSGlobal.controller.clsBillDtl;
import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.view.frmOkPopUp;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.List;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.font.TextAttribute;
import java.io.InputStream;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.print.attribute.AttributeSet;
import javax.swing.AbstractListModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.Popup;
import javax.swing.Timer;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListDataListener;
import javax.swing.event.PopupMenuEvent;
import sun.audio.AudioData;
import sun.audio.AudioDataStream;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import sun.audio.ContinuousAudioDataStream;

public class frmKDSForKOTBookAndProcess extends javax.swing.JFrame
{

    private JScrollPane scrollPaneArray[];
    private JList listViewArray[];
    private LinkedHashMap<String, ArrayList<clsBillDtl>> mapKOTHd;
    private LinkedHashMap<String, ArrayList<clsBillDtl>> mapCountKOTSize;
    private ArrayList<ArrayList<clsBillDtl>> listOfKOTs;
    private int navigatorNew = 0;
    private int navigator = 0;
    //private String gBillNo="";    
    private int startIndex = 0;
    private int endIndex = 0;
    private ArrayList<String> listOfKOTsToBeProcess;
    //private String gBillDateTime="";
    //private final JLabel lblBillNoArray[];
    private final JLabel[] lblKOTDelayArray;
    private final JLabel[] lblTableAndKOTNoArray;
    private int selectedIndexForItemProcessed = -1;
    private Map<String, String> mapSelectedKOTs;
    private Map<String, String> mapSelectedItems;
    private String costCenterCode, costCenterName;
    private int gITEMCOUNTER;

    public frmKDSForKOTBookAndProcess(String costCenterCode, String costCenterName)
    {
	////////////////////////////
	initComponents();

	this.costCenterCode = costCenterCode;
	this.costCenterName = costCenterName;

	lblformName.setText("KDS FOR " + costCenterName);

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

	mapKOTHd = new LinkedHashMap();
	mapCountKOTSize = new LinkedHashMap();
	listOfKOTs = new ArrayList<ArrayList<clsBillDtl>>();
	listOfKOTsToBeProcess = new ArrayList<String>();
	mapSelectedKOTs = new HashMap<>();
	mapSelectedItems = new HashMap<>();

	funRefreshForm();
	funSetBillDelayTimer();

	Timer timer = new Timer(1000, new ActionListener()
	{
	    @Override
	    public void actionPerformed(ActionEvent e)
	    {
		int oldBillSize = mapKOTHd.size();
		/*
                 * int newBillSize = funGetNewBillSize(); if (oldBillSize !=
                 * newBillSize) { funRefreshForm(); }
		 */
		funRefreshForm();
	    }

	    /* private int funGetNewBillSize()
            {
                try
                {
                    String sqlBillDtl = "select a.strKOTNo,a.strItemCode,a.strItemName,a.dblRate,sum(a.dblItemQuantity),sum(a.dblAmount) "
                            + " ,DATE_FORMAT(date(a.dteDateCreated),'%d-%m-%Y') as dteKOTDate,time(a.dteDateCreated) as tmeKOTTime ,a.strTableNo,b.strTableName"
                            + " ,IF(TIME_TO_SEC(TIMEDIFF(CURRENT_TIME(),time(a.dteDateCreated)))>(c.intProcTimeMin*60)"
                            + " ,if(TIME_TO_SEC(TIMEDIFF(CURRENT_TIME(),time(a.dteDateCreated)))>(c.tmeTargetMiss*60),'RED','ORANGE'),'BLACK') "
                            + " from tblitemrtemp a ,tbltablemaster b,tblitemmaster c "
                            + " where left(a.strItemCode,7)=c.strItemCode and a.strNCKotYN='N' and a.tdhComboItemYN='N' "
                            + " and a.strTableNo=b.strTableNo and a.strItemProcessed='N' "
                            + " and a.strKOTNo not in(select strDocNo from tblkdsprocess where strBP='P' and strKDSName='KOT' ) "
                            + " group by a.strTableNo,a.strKOTNo,a.strItemCode,a.strItemName "
                            + " ORDER BY a.dteDateCreated desc,time(a.dteDateCreated) desc ";
                    ResultSet resultSet = clsGlobalVarClass.dbMysql.executeResultSet(sqlBillDtl);

                    mapCountKOTSize.clear();
                    while (resultSet.next())
                    {
                        clsBillDtl objKOTDtl = new clsBillDtl();
                        String kotNo = resultSet.getString(1);

                        objKOTDtl.setStrKOTNo(kotNo);
                        objKOTDtl.setStrItemCode(resultSet.getString(2));
                        objKOTDtl.setStrItemName(resultSet.getString(3) + "!" + resultSet.getString(11));
                        objKOTDtl.setDblRate(resultSet.getDouble(4));
                        objKOTDtl.setDblQuantity(resultSet.getDouble(5));
                        objKOTDtl.setDblAmount(resultSet.getDouble(6));
                        objKOTDtl.setDteNCKOTDate(resultSet.getString(8));
                        objKOTDtl.setStrTableName(resultSet.getString(10));

                        if (mapCountKOTSize.containsKey(kotNo))
                        {
                            mapCountKOTSize.get(kotNo).add(objKOTDtl);
                        }
                        else
                        {
                            ArrayList<clsBillDtl> listBillItemDtl = new ArrayList<clsBillDtl>();

                            listBillItemDtl.add(objKOTDtl);

                            mapCountKOTSize.put(kotNo, listBillItemDtl);
                        }
                    }
                    resultSet.close();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                return mapCountKOTSize.size();
            }
	     */
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
		if (mapKOTHd.containsKey(lblSelectedKOT.getText()))
		{
		    ArrayList<clsBillDtl> arrSelectedKotItemList = mapKOTHd.get(lblSelectedKOT.getText());

		    for (int cnt = 0; cnt < arrSelectedKotItemList.size(); cnt++)
		    {
			clsBillDtl objBillDtl = arrSelectedKotItemList.get(cnt);

			/*  String deleteQuery = " delete from tblkdsprocess where strKDSName='KOT' and "
                                + " strDocNo='" + lblSelectedKOT.getText() + "' and strItemCode='"+objBillDtl.getStrItemCode()+"' ";
                        clsGlobalVarClass.dbMysql.execute(deleteQuery);
                        
                        
                        if (cnt == 0)
                        {
                            sqlBillOrderProcess.append("('" + lblSelectedKOT.getText() + "','P','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.gUserCode + "','KOT','"+objBillDtl.getStrItemCode()+"','"+costCenterCode+"','"+objBillDtl.getStrWaiterNo()+"','"+objBillDtl.getDteBillDate()+"')");
                        }
                        else
                        {
                            sqlBillOrderProcess.append(",('" + lblSelectedKOT.getText() + "','P','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.gUserCode + "','KOT','"+objBillDtl.getStrItemCode()+"','"+costCenterCode+"','"+objBillDtl.getStrWaiterNo()+"','"+objBillDtl.getDteBillDate()+"')");
                        }
			 */
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
		}
	    }


	    /*    listOfKOTsToBeProcess.clear();
            listOfKOTsToBeProcess.add(lblSelectedKOT.getText());
            

            sqlBillOrderProcess.append("delete from tblkdsprocess "
                    + "where strKDSName='KOT' "
                    + "and strDocNo IN ");
            for (int i = 0; i < listOfKOTsToBeProcess.size(); i++)
            {
                if (i == 0)
                {
                    sqlBillOrderProcess.append("('" + listOfKOTsToBeProcess.get(i) + "'");
                }
                else
                {
                    sqlBillOrderProcess.append(",'" + listOfKOTsToBeProcess.get(i) + "'");
                }
            }
            sqlBillOrderProcess.append(")");
            clsGlobalVarClass.dbMysql.execute(sqlBillOrderProcess.toString());

            sqlBillOrderProcess.setLength(0);
            sqlBillOrderProcess.append("insert into tblkdsprocess values");
            for (int i = 0; i < listOfKOTsToBeProcess.size(); i++)
            {
                if (i == 0)
                {
                    sqlBillOrderProcess.append("('" + listOfKOTsToBeProcess.get(i) + "','P','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.gUserCode + "','KOT')");
                }
                else
                {
                    sqlBillOrderProcess.append(",('" + listOfKOTsToBeProcess.get(i) + "','P','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.gUserCode + "','KOT')");
                }
            }
            
	     */
	    //  clsGlobalVarClass.dbMysql.execute(sqlBillOrderProcess.toString());
	    new frmOkPopUp(null, "KOT Process Successfully.", "Successfull", 3).setVisible(true);

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
	timer.setRepeats(true);
	timer.setCoalesce(true);
	timer.setInitialDelay(0);
	timer.start();
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
	    lblSelectedItem.setText("Ordered Item: " + arrItem[0]);
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

		/*  StringBuilder sqlBillOrderProcess = new StringBuilder();
                sqlBillOrderProcess.setLength(0);
                sqlBillOrderProcess.append("insert into tblkdsprocess values");

                String deleteQuery = "delete from tblkdsprocess  where strKDSName='KOT' and "
                                    + " strDocNo='" + kotNo + "' and strItemCode='"+itemCode+"' ";
                clsGlobalVarClass.dbMysql.execute(deleteQuery);

                sqlBillOrderProcess.append("('" + kotNo + "','P','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.gUserCode + "','KOT','"+itemCode+"','"+costCenterCode+"','"+waiterNo+"','"+kotDateTime+"')");  
                clsGlobalVarClass.dbMysql.execute(sqlBillOrderProcess.toString());
		 */
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
        btnItemProcessed = new javax.swing.JButton();
        btnKOTProcess = new javax.swing.JButton();
        lblBillDelay8 = new javax.swing.JLabel();
        lblBillDelay7 = new javax.swing.JLabel();
        lblBillDelay6 = new javax.swing.JLabel();
        lblBillDelay5 = new javax.swing.JLabel();
        lblBillDelay4 = new javax.swing.JLabel();
        lblBillDelay3 = new javax.swing.JLabel();
        lblBillDelay2 = new javax.swing.JLabel();
        lblBillDelay1 = new javax.swing.JLabel();
        lblTableAndKOTNo8 = new javax.swing.JLabel();
        lblTableAndKOTNo7 = new javax.swing.JLabel();
        lblTableAndKOTNo1 = new javax.swing.JLabel();
        lblTableAndKOTNo2 = new javax.swing.JLabel();
        lblTableAndKOTNo3 = new javax.swing.JLabel();
        lblTableAndKOTNo4 = new javax.swing.JLabel();
        lblTableAndKOTNo5 = new javax.swing.JLabel();
        lblTableAndKOTNo6 = new javax.swing.JLabel();
        lblSelectedKOT = new javax.swing.JLabel();
        lblSelectedItem = new javax.swing.JLabel();
        scrollPane9 = new javax.swing.JScrollPane();
        list9 = new javax.swing.JList();
        scrollPane10 = new javax.swing.JScrollPane();
        list10 = new javax.swing.JList();
        scrollPane11 = new javax.swing.JScrollPane();
        list11 = new javax.swing.JList();
        scrollPane12 = new javax.swing.JScrollPane();
        list12 = new javax.swing.JList();
        scrollPane13 = new javax.swing.JScrollPane();
        list13 = new javax.swing.JList();
        scrollPane14 = new javax.swing.JScrollPane();
        list14 = new javax.swing.JList();
        scrollPane15 = new javax.swing.JScrollPane();
        list15 = new javax.swing.JList();
        lblTableAndKOTNo9 = new javax.swing.JLabel();
        lblBillDelay9 = new javax.swing.JLabel();
        lblTableAndKOTNo10 = new javax.swing.JLabel();
        lblTableAndKOTNo11 = new javax.swing.JLabel();
        lblTableAndKOTNo12 = new javax.swing.JLabel();
        lblTableAndKOTNo13 = new javax.swing.JLabel();
        lblTableAndKOTNo14 = new javax.swing.JLabel();
        lblTableAndKOTNo15 = new javax.swing.JLabel();
        lblBillDelay10 = new javax.swing.JLabel();
        lblBillDelay11 = new javax.swing.JLabel();
        lblBillDelay12 = new javax.swing.JLabel();
        lblBillDelay13 = new javax.swing.JLabel();
        lblBillDelay14 = new javax.swing.JLabel();
        lblBillDelay15 = new javax.swing.JLabel();

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

        panelMain.setMinimumSize(new java.awt.Dimension(800, 570));
        panelMain.setOpaque(false);
        panelMain.setPreferredSize(new java.awt.Dimension(832, 565));
        panelMain.setLayout(new java.awt.GridBagLayout());

        panelBody.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        panelBody.setMaximumSize(new java.awt.Dimension(1080, 960));
        panelBody.setMinimumSize(new java.awt.Dimension(1080, 960));
        panelBody.setOpaque(false);
        panelBody.setPreferredSize(new java.awt.Dimension(1080, 960));
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
        panelBody.add(btnOld, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 880, 100, 40));

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgNewButton1.png"))); // NOI18N
        btnNew.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgNewButton2.png"))); // NOI18N
        btnNew.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnNewMouseClicked(evt);
            }
        });
        panelBody.add(btnNew, new org.netbeans.lib.awtextra.AbsoluteConstraints(972, 880, 100, 40));

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
        panelBody.add(btnClose, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 880, 80, 40));

        scrollPane1.setBorder(null);
        scrollPane1.setViewportBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        scrollPane1.setMinimumSize(new java.awt.Dimension(55, 160));
        scrollPane1.setPreferredSize(new java.awt.Dimension(55, 160));
        scrollPane1.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                scrollPane1MouseClicked(evt);
            }
        });

        list1.setBackground(new java.awt.Color(0, 0, 0));
        list1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        list1.setForeground(new java.awt.Color(255, 255, 255));
        list1.setModel(new javax.swing.AbstractListModel()
        {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        list1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        list1.setFixedCellHeight(25);
        list1.setFixedCellWidth(100);
        list1.setMaximumSize(new java.awt.Dimension(100, 100));
        list1.setMinimumSize(new java.awt.Dimension(100, 100));
        list1.setName(""); // NOI18N
        list1.setPreferredSize(new java.awt.Dimension(100, 100));
        list1.setSelectionBackground(new java.awt.Color(0, 153, 255));
        list1.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                list1MouseClicked(evt);
            }
        });
        scrollPane1.setViewportView(list1);

        panelBody.add(scrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 210, 250));

        scrollPane2.setBorder(null);
        scrollPane2.setViewportBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        scrollPane2.setMinimumSize(new java.awt.Dimension(55, 160));
        scrollPane2.setPreferredSize(new java.awt.Dimension(55, 160));
        scrollPane2.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                scrollPane2MouseClicked(evt);
            }
        });

        list2.setBackground(new java.awt.Color(0, 0, 0));
        list2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        list2.setForeground(new java.awt.Color(255, 255, 255));
        list2.setModel(new javax.swing.AbstractListModel()
        {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        list2.setFixedCellHeight(25);
        list2.setFixedCellWidth(150);
        list2.setSelectionBackground(new java.awt.Color(0, 153, 255));
        list2.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                list2MouseClicked(evt);
            }
        });
        scrollPane2.setViewportView(list2);

        panelBody.add(scrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 40, 210, 250));

        scrollPane3.setBorder(null);
        scrollPane3.setViewportBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        scrollPane3.setMinimumSize(new java.awt.Dimension(55, 160));
        scrollPane3.setPreferredSize(new java.awt.Dimension(55, 160));
        scrollPane3.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                scrollPane3MouseClicked(evt);
            }
        });

        list3.setBackground(new java.awt.Color(0, 0, 0));
        list3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        list3.setForeground(new java.awt.Color(255, 255, 255));
        list3.setModel(new javax.swing.AbstractListModel()
        {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        list3.setFixedCellHeight(25);
        list3.setFixedCellWidth(150);
        list3.setSelectionBackground(new java.awt.Color(0, 153, 255));
        list3.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                list3MouseClicked(evt);
            }
        });
        scrollPane3.setViewportView(list3);

        panelBody.add(scrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 40, 210, 250));

        scrollPane4.setBorder(null);
        scrollPane4.setViewportBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        scrollPane4.setMinimumSize(new java.awt.Dimension(55, 160));
        scrollPane4.setPreferredSize(new java.awt.Dimension(55, 160));
        scrollPane4.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                scrollPane4MouseClicked(evt);
            }
        });

        list4.setBackground(new java.awt.Color(0, 0, 0));
        list4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        list4.setForeground(new java.awt.Color(255, 255, 255));
        list4.setModel(new javax.swing.AbstractListModel()
        {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        list4.setFixedCellHeight(25);
        list4.setFixedCellWidth(150);
        list4.setSelectionBackground(new java.awt.Color(0, 153, 255));
        list4.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                list4MouseClicked(evt);
            }
        });
        scrollPane4.setViewportView(list4);

        panelBody.add(scrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 40, 210, 250));

        scrollPane5.setBorder(null);
        scrollPane5.setViewportBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        scrollPane5.setMinimumSize(new java.awt.Dimension(55, 160));
        scrollPane5.setPreferredSize(new java.awt.Dimension(55, 160));
        scrollPane5.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                scrollPane5MouseClicked(evt);
            }
        });

        list5.setBackground(new java.awt.Color(0, 0, 0));
        list5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        list5.setForeground(new java.awt.Color(255, 255, 255));
        list5.setModel(new javax.swing.AbstractListModel()
        {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        list5.setFixedCellHeight(25);
        list5.setFixedCellWidth(150);
        list5.setSelectionBackground(new java.awt.Color(0, 153, 255));
        list5.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                list5MouseClicked(evt);
            }
        });
        scrollPane5.setViewportView(list5);

        panelBody.add(scrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 40, 220, 250));

        scrollPane6.setBorder(null);
        scrollPane6.setViewportBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        scrollPane6.setMinimumSize(new java.awt.Dimension(55, 160));
        scrollPane6.setPreferredSize(new java.awt.Dimension(55, 160));
        scrollPane6.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                scrollPane6MouseClicked(evt);
            }
        });

        list6.setBackground(new java.awt.Color(0, 0, 0));
        list6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        list6.setForeground(new java.awt.Color(255, 255, 255));
        list6.setModel(new javax.swing.AbstractListModel()
        {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        list6.setFixedCellHeight(25);
        list6.setFixedCellWidth(150);
        list6.setSelectionBackground(new java.awt.Color(0, 153, 255));
        list6.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                list6MouseClicked(evt);
            }
        });
        scrollPane6.setViewportView(list6);

        panelBody.add(scrollPane6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 330, 210, 250));

        scrollPane7.setBorder(null);
        scrollPane7.setViewportBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        scrollPane7.setMinimumSize(new java.awt.Dimension(55, 160));
        scrollPane7.setPreferredSize(new java.awt.Dimension(55, 160));
        scrollPane7.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                scrollPane7MouseClicked(evt);
            }
        });

        list7.setBackground(new java.awt.Color(0, 0, 0));
        list7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        list7.setForeground(new java.awt.Color(255, 255, 255));
        list7.setModel(new javax.swing.AbstractListModel()
        {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        list7.setFixedCellHeight(25);
        list7.setFixedCellWidth(200);
        list7.setSelectionBackground(new java.awt.Color(0, 153, 255));
        list7.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                list7MouseClicked(evt);
            }
        });
        scrollPane7.setViewportView(list7);

        panelBody.add(scrollPane7, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 330, 210, 250));

        scrollPane8.setBorder(null);
        scrollPane8.setViewportBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        scrollPane8.setMinimumSize(new java.awt.Dimension(55, 160));
        scrollPane8.setPreferredSize(new java.awt.Dimension(55, 160));
        scrollPane8.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                scrollPane8MouseClicked(evt);
            }
        });

        list8.setBackground(new java.awt.Color(0, 0, 0));
        list8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        list8.setForeground(new java.awt.Color(255, 255, 255));
        list8.setModel(new javax.swing.AbstractListModel()
        {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        list8.setFixedCellHeight(25);
        list8.setFixedCellWidth(150);
        list8.setSelectionBackground(new java.awt.Color(0, 153, 255));
        list8.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                list8MouseClicked(evt);
            }
        });
        scrollPane8.setViewportView(list8);

        panelBody.add(scrollPane8, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 330, 210, 250));

        btnItemProcessed.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        btnItemProcessed.setForeground(new java.awt.Color(255, 255, 255));
        btnItemProcessed.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgOrderProcessed.png"))); // NOI18N
        btnItemProcessed.setText("Item Process");
        btnItemProcessed.setEnabled(false);
        btnItemProcessed.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnItemProcessed.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgOrderProcessed1.png"))); // NOI18N
        btnItemProcessed.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnItemProcessedMouseClicked(evt);
            }
        });
        panelBody.add(btnItemProcessed, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 880, 150, 40));

        btnKOTProcess.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        btnKOTProcess.setForeground(new java.awt.Color(255, 255, 255));
        btnKOTProcess.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgOrderProcessed.png"))); // NOI18N
        btnKOTProcess.setText("KOT Process");
        btnKOTProcess.setEnabled(false);
        btnKOTProcess.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnKOTProcess.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSTransaction/images/imgOrderProcessed1.png"))); // NOI18N
        btnKOTProcess.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                btnKOTProcessMouseClicked(evt);
            }
        });
        panelBody.add(btnKOTProcess, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 880, 130, 40));

        lblBillDelay8.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lblBillDelay8.setForeground(new java.awt.Color(255, 51, 0));
        lblBillDelay8.setText("00:00");
        lblBillDelay8.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        panelBody.add(lblBillDelay8, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 300, 50, 20));

        lblBillDelay7.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lblBillDelay7.setForeground(new java.awt.Color(255, 51, 0));
        lblBillDelay7.setText("00:00");
        lblBillDelay7.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        panelBody.add(lblBillDelay7, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 300, 50, 20));

        lblBillDelay6.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lblBillDelay6.setForeground(new java.awt.Color(255, 51, 0));
        lblBillDelay6.setText("00:00");
        lblBillDelay6.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        panelBody.add(lblBillDelay6, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 300, -1, 20));

        lblBillDelay5.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lblBillDelay5.setForeground(new java.awt.Color(255, 51, 0));
        lblBillDelay5.setText("00:00");
        lblBillDelay5.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        panelBody.add(lblBillDelay5, new org.netbeans.lib.awtextra.AbsoluteConstraints(1010, 10, 50, 20));

        lblBillDelay4.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lblBillDelay4.setForeground(new java.awt.Color(255, 51, 0));
        lblBillDelay4.setText("00:00");
        lblBillDelay4.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        panelBody.add(lblBillDelay4, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 10, -1, 20));

        lblBillDelay3.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lblBillDelay3.setForeground(new java.awt.Color(255, 51, 0));
        lblBillDelay3.setText("00:00");
        lblBillDelay3.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        panelBody.add(lblBillDelay3, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 10, 50, 20));

        lblBillDelay2.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lblBillDelay2.setForeground(new java.awt.Color(255, 51, 0));
        lblBillDelay2.setText("00:00");
        lblBillDelay2.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        panelBody.add(lblBillDelay2, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 10, -1, 20));

        lblBillDelay1.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lblBillDelay1.setForeground(new java.awt.Color(255, 51, 0));
        lblBillDelay1.setText("00:00");
        lblBillDelay1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        panelBody.add(lblBillDelay1, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 10, 50, 20));

        lblTableAndKOTNo8.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lblTableAndKOTNo8.setText("00:00:00");
        lblTableAndKOTNo8.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        lblTableAndKOTNo8.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                lblTableAndKOTNo8MouseClicked(evt);
            }
        });
        panelBody.add(lblTableAndKOTNo8, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 300, 160, 20));

        lblTableAndKOTNo7.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lblTableAndKOTNo7.setText("00:00:00");
        lblTableAndKOTNo7.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        lblTableAndKOTNo7.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                lblTableAndKOTNo7MouseClicked(evt);
            }
        });
        panelBody.add(lblTableAndKOTNo7, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 300, 160, 20));

        lblTableAndKOTNo1.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lblTableAndKOTNo1.setText("00:00:00");
        lblTableAndKOTNo1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        lblTableAndKOTNo1.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                lblTableAndKOTNo1MouseClicked(evt);
            }
        });
        panelBody.add(lblTableAndKOTNo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 140, 20));

        lblTableAndKOTNo2.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lblTableAndKOTNo2.setText("00:00:00");
        lblTableAndKOTNo2.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        lblTableAndKOTNo2.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                lblTableAndKOTNo2MouseClicked(evt);
            }
        });
        panelBody.add(lblTableAndKOTNo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 10, 160, 20));

        lblTableAndKOTNo3.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lblTableAndKOTNo3.setText("00:00:00");
        lblTableAndKOTNo3.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        lblTableAndKOTNo3.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                lblTableAndKOTNo3MouseClicked(evt);
            }
        });
        panelBody.add(lblTableAndKOTNo3, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 10, 160, 20));

        lblTableAndKOTNo4.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lblTableAndKOTNo4.setText("00:00:00");
        lblTableAndKOTNo4.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        lblTableAndKOTNo4.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                lblTableAndKOTNo4MouseClicked(evt);
            }
        });
        panelBody.add(lblTableAndKOTNo4, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 10, 160, 20));

        lblTableAndKOTNo5.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lblTableAndKOTNo5.setText("00:00:00");
        lblTableAndKOTNo5.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        lblTableAndKOTNo5.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                lblTableAndKOTNo5MouseClicked(evt);
            }
        });
        panelBody.add(lblTableAndKOTNo5, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 10, 160, 20));

        lblTableAndKOTNo6.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lblTableAndKOTNo6.setText("00:00:00");
        lblTableAndKOTNo6.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        lblTableAndKOTNo6.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                lblTableAndKOTNo6MouseClicked(evt);
            }
        });
        panelBody.add(lblTableAndKOTNo6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 300, 150, 20));

        lblSelectedKOT.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblSelectedKOT.setText("KOT");
        lblSelectedKOT.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        lblSelectedKOT.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                lblSelectedKOTMouseClicked(evt);
            }
        });
        panelBody.add(lblSelectedKOT, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 880, 110, 40));

        lblSelectedItem.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblSelectedItem.setText("ITEM");
        lblSelectedItem.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        lblSelectedItem.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                lblSelectedItemMouseClicked(evt);
            }
        });
        panelBody.add(lblSelectedItem, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 880, 370, 40));

        scrollPane9.setBorder(null);
        scrollPane9.setViewportBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        scrollPane9.setMinimumSize(new java.awt.Dimension(55, 160));
        scrollPane9.setPreferredSize(new java.awt.Dimension(55, 160));
        scrollPane9.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                scrollPane9MouseClicked(evt);
            }
        });

        list9.setBackground(new java.awt.Color(0, 0, 0));
        list9.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        list9.setForeground(new java.awt.Color(255, 255, 255));
        list9.setModel(new javax.swing.AbstractListModel()
        {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        list9.setFixedCellHeight(25);
        list9.setFixedCellWidth(150);
        list9.setSelectionBackground(new java.awt.Color(0, 153, 255));
        list9.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                list9MouseClicked(evt);
            }
        });
        scrollPane9.setViewportView(list9);

        panelBody.add(scrollPane9, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 330, 210, 250));

        scrollPane10.setBorder(null);
        scrollPane10.setViewportBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        scrollPane10.setMinimumSize(new java.awt.Dimension(55, 160));
        scrollPane10.setPreferredSize(new java.awt.Dimension(55, 160));
        scrollPane10.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                scrollPane10MouseClicked(evt);
            }
        });

        list10.setBackground(new java.awt.Color(0, 0, 0));
        list10.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        list10.setForeground(new java.awt.Color(255, 255, 255));
        list10.setModel(new javax.swing.AbstractListModel()
        {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        list10.setFixedCellHeight(25);
        list10.setFixedCellWidth(150);
        list10.setSelectionBackground(new java.awt.Color(0, 153, 255));
        list10.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                list10MouseClicked(evt);
            }
        });
        scrollPane10.setViewportView(list10);

        panelBody.add(scrollPane10, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 330, 220, 250));

        scrollPane11.setBorder(null);
        scrollPane11.setViewportBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        scrollPane11.setMinimumSize(new java.awt.Dimension(55, 160));
        scrollPane11.setPreferredSize(new java.awt.Dimension(55, 160));
        scrollPane11.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                scrollPane11MouseClicked(evt);
            }
        });

        list11.setBackground(new java.awt.Color(0, 0, 0));
        list11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        list11.setForeground(new java.awt.Color(255, 255, 255));
        list11.setModel(new javax.swing.AbstractListModel()
        {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        list11.setFixedCellHeight(25);
        list11.setFixedCellWidth(150);
        list11.setSelectionBackground(new java.awt.Color(0, 153, 255));
        list11.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                list11MouseClicked(evt);
            }
        });
        scrollPane11.setViewportView(list11);

        panelBody.add(scrollPane11, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 620, 210, 250));

        scrollPane12.setBorder(null);
        scrollPane12.setViewportBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        scrollPane12.setMinimumSize(new java.awt.Dimension(55, 160));
        scrollPane12.setPreferredSize(new java.awt.Dimension(55, 160));
        scrollPane12.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                scrollPane12MouseClicked(evt);
            }
        });

        list12.setBackground(new java.awt.Color(0, 0, 0));
        list12.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        list12.setForeground(new java.awt.Color(255, 255, 255));
        list12.setModel(new javax.swing.AbstractListModel()
        {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        list12.setFixedCellHeight(25);
        list12.setFixedCellWidth(150);
        list12.setSelectionBackground(new java.awt.Color(0, 153, 255));
        list12.addMouseMotionListener(new java.awt.event.MouseMotionAdapter()
        {
            public void mouseMoved(java.awt.event.MouseEvent evt)
            {
                list12MouseMoved(evt);
            }
        });
        list12.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                list12MouseClicked(evt);
            }
        });
        scrollPane12.setViewportView(list12);

        panelBody.add(scrollPane12, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 620, 210, 250));

        scrollPane13.setBorder(null);
        scrollPane13.setViewportBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        scrollPane13.setMinimumSize(new java.awt.Dimension(55, 160));
        scrollPane13.setPreferredSize(new java.awt.Dimension(55, 160));
        scrollPane13.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                scrollPane13MouseClicked(evt);
            }
        });

        list13.setBackground(new java.awt.Color(0, 0, 0));
        list13.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        list13.setForeground(new java.awt.Color(255, 255, 255));
        list13.setModel(new javax.swing.AbstractListModel()
        {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        list13.setFixedCellHeight(25);
        list13.setFixedCellWidth(150);
        list13.setSelectionBackground(new java.awt.Color(0, 153, 255));
        list13.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                list13MouseClicked(evt);
            }
        });
        scrollPane13.setViewportView(list13);

        panelBody.add(scrollPane13, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 620, 210, 250));

        scrollPane14.setBorder(null);
        scrollPane14.setViewportBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        scrollPane14.setMinimumSize(new java.awt.Dimension(55, 160));
        scrollPane14.setPreferredSize(new java.awt.Dimension(55, 160));
        scrollPane14.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                scrollPane14MouseClicked(evt);
            }
        });

        list14.setBackground(new java.awt.Color(0, 0, 0));
        list14.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        list14.setForeground(new java.awt.Color(255, 255, 255));
        list14.setModel(new javax.swing.AbstractListModel()
        {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        list14.setFixedCellHeight(25);
        list14.setFixedCellWidth(200);
        list14.setSelectionBackground(new java.awt.Color(0, 153, 255));
        list14.setValueIsAdjusting(true);
        list14.addMouseMotionListener(new java.awt.event.MouseMotionAdapter()
        {
            public void mouseMoved(java.awt.event.MouseEvent evt)
            {
                list14MouseMoved(evt);
            }
        });
        list14.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                list14MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt)
            {
                list14MouseEntered(evt);
            }
        });
        scrollPane14.setViewportView(list14);

        panelBody.add(scrollPane14, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 620, 210, 250));

        scrollPane15.setBorder(null);
        scrollPane15.setViewportBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        scrollPane15.setMinimumSize(new java.awt.Dimension(55, 160));
        scrollPane15.setPreferredSize(new java.awt.Dimension(55, 160));
        scrollPane15.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                scrollPane15MouseClicked(evt);
            }
        });

        list15.setBackground(new java.awt.Color(0, 0, 0));
        list15.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        list15.setForeground(new java.awt.Color(255, 255, 255));
        list15.setModel(new javax.swing.AbstractListModel()
        {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        list15.setFixedCellHeight(25);
        list15.setFixedCellWidth(200);
        list15.setSelectionBackground(new java.awt.Color(0, 153, 255));
        list15.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                list15MouseClicked(evt);
            }
        });
        scrollPane15.setViewportView(list15);

        panelBody.add(scrollPane15, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 620, 220, 250));

        lblTableAndKOTNo9.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lblTableAndKOTNo9.setText("00:00:00");
        lblTableAndKOTNo9.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                lblTableAndKOTNo9MouseClicked(evt);
            }
        });
        panelBody.add(lblTableAndKOTNo9, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 300, 160, 20));

        lblBillDelay9.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lblBillDelay9.setForeground(new java.awt.Color(255, 51, 0));
        lblBillDelay9.setText("00:00");
        panelBody.add(lblBillDelay9, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 300, 50, 20));

        lblTableAndKOTNo10.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lblTableAndKOTNo10.setText("00:00:00");
        lblTableAndKOTNo10.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                lblTableAndKOTNo10MouseClicked(evt);
            }
        });
        panelBody.add(lblTableAndKOTNo10, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 300, 150, 20));

        lblTableAndKOTNo11.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lblTableAndKOTNo11.setText("00:00:00");
        lblTableAndKOTNo11.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                lblTableAndKOTNo11MouseClicked(evt);
            }
        });
        panelBody.add(lblTableAndKOTNo11, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 590, 160, 20));

        lblTableAndKOTNo12.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lblTableAndKOTNo12.setText("00:00:00");
        lblTableAndKOTNo12.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                lblTableAndKOTNo12MouseClicked(evt);
            }
        });
        panelBody.add(lblTableAndKOTNo12, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 590, 160, 20));

        lblTableAndKOTNo13.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lblTableAndKOTNo13.setText("00:00:00");
        lblTableAndKOTNo13.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                lblTableAndKOTNo13MouseClicked(evt);
            }
        });
        panelBody.add(lblTableAndKOTNo13, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 590, 160, 20));

        lblTableAndKOTNo14.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lblTableAndKOTNo14.setText("00:00:00");
        lblTableAndKOTNo14.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                lblTableAndKOTNo14MouseClicked(evt);
            }
        });
        panelBody.add(lblTableAndKOTNo14, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 590, 160, 20));

        lblTableAndKOTNo15.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lblTableAndKOTNo15.setText("00:00:00");
        lblTableAndKOTNo15.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                lblTableAndKOTNo15MouseClicked(evt);
            }
        });
        panelBody.add(lblTableAndKOTNo15, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 590, 160, 20));

        lblBillDelay10.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lblBillDelay10.setForeground(new java.awt.Color(255, 51, 0));
        lblBillDelay10.setText("00:00");
        panelBody.add(lblBillDelay10, new org.netbeans.lib.awtextra.AbsoluteConstraints(1010, 300, -1, 20));

        lblBillDelay11.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lblBillDelay11.setForeground(new java.awt.Color(255, 51, 0));
        lblBillDelay11.setText("00:00");
        panelBody.add(lblBillDelay11, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 590, 50, 20));

        lblBillDelay12.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lblBillDelay12.setForeground(new java.awt.Color(255, 51, 0));
        lblBillDelay12.setText("00:00");
        panelBody.add(lblBillDelay12, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 590, 50, -1));

        lblBillDelay13.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lblBillDelay13.setForeground(new java.awt.Color(255, 51, 0));
        lblBillDelay13.setText("00:00");
        panelBody.add(lblBillDelay13, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 590, -1, 20));

        lblBillDelay14.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lblBillDelay14.setForeground(new java.awt.Color(255, 51, 0));
        lblBillDelay14.setText("00:00");
        panelBody.add(lblBillDelay14, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 590, 50, 20));

        lblBillDelay15.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lblBillDelay15.setForeground(new java.awt.Color(255, 51, 0));
        lblBillDelay15.setText("00:00");
        panelBody.add(lblBillDelay15, new org.netbeans.lib.awtextra.AbsoluteConstraints(1020, 590, 50, 20));

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
	dispose();
	clsGlobalVarClass.hmActiveForms.remove("KDSForKOTBookAndProcess");
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
	    java.util.logging.Logger.getLogger(frmKDSForKOTBookAndProcess.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	}
	catch (InstantiationException ex)
	{
	    java.util.logging.Logger.getLogger(frmKDSForKOTBookAndProcess.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	}
	catch (IllegalAccessException ex)
	{
	    java.util.logging.Logger.getLogger(frmKDSForKOTBookAndProcess.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	}
	catch (javax.swing.UnsupportedLookAndFeelException ex)
	{
	    java.util.logging.Logger.getLogger(frmKDSForKOTBookAndProcess.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
    private javax.swing.JLabel lblTableAndKOTNo2;
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
    private javax.swing.JList list2;
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
    private javax.swing.JScrollPane scrollPane2;
    private javax.swing.JScrollPane scrollPane3;
    private javax.swing.JScrollPane scrollPane4;
    private javax.swing.JScrollPane scrollPane5;
    private javax.swing.JScrollPane scrollPane6;
    private javax.swing.JScrollPane scrollPane7;
    private javax.swing.JScrollPane scrollPane8;
    private javax.swing.JScrollPane scrollPane9;
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
//            String sqlBillDtl = "select a.strKOTNo,a.strItemCode,a.strItemName,a.dblRate,sum(a.dblItemQuantity),sum(a.dblAmount) "
//                    + " ,DATE_FORMAT(date(a.dteDateCreated),'%d-%m-%Y') as dteKOTDate,time(a.dteDateCreated) as tmeKOTTime ,a.strTableNo,b.strTableName"
//                    + " ,IF(TIME_TO_SEC(TIMEDIFF(CURRENT_TIME(),time(a.dteDateCreated)))>(c.intProcTimeMin*60)"
//                    + " ,if(TIME_TO_SEC(TIMEDIFF(CURRENT_TIME(),time(a.dteDateCreated)))>(c.tmeTargetMiss*60),'RED','ORANGE'),'BLACK') "
//                    + " from tblitemrtemp a ,tbltablemaster b,tblitemmaster c,tblmenuitempricingdtl d,tblcostcentermaster e "
//                    + " where left(a.strItemCode,7)=c.strItemCode "
//                    + " and a.strNCKotYN='N' "
//                    + " and a.tdhComboItemYN='N' "
//                    + " and a.strTableNo=b.strTableNo "
//                    + " and a.strItemProcessed='N' "
//                    + " and c.strItemCode=d.strItemCode "
//                    + " and a.strPOSCode=d.strPosCode "
//                    + " and (d.strPosCode='"+clsGlobalVarClass.gPOSCode+"' or d.strPosCode='All') "
//                    + " and d.strCostCenterCode=e.strCostCenterCode "
//                    + " and e.strCostCenterCode='"+costCenterCode+"' "
//                    + " and a.strKOTNo not in(select strDocNo from tblkdsprocess where strBP='P' and strKDSName='KOT' ) "
//                    + " group by a.strTableNo,a.strKOTNo,a.strItemCode,a.strItemName "
//                    + " ORDER BY a.dteDateCreated desc,time(a.dteDateCreated) desc ";

	    String posDate = clsGlobalVarClass.gPOSDateForTransaction.split(" ")[0];
	    /*  String sqlBillDtl = "(SELECT a.strKOTNo,a.strItemCode,a.strItemName,a.dblRate, SUM(a.dblItemQuantity), SUM(a.dblAmount) "
                     + ", DATE_FORMAT(DATE(a.dteDateCreated),'%d-%m-%Y') AS dteKOTDate, TIME(a.dteDateCreated) AS tmeKOTTime "
                     + ",a.strTableNo,b.strTableName, IF(TIME_TO_SEC(TIMEDIFF(CURRENT_TIME(), TIME(a.dteDateCreated)))>(c.intProcTimeMin*60), IF(TIME_TO_SEC(TIMEDIFF(CURRENT_TIME(), TIME(a.dteDateCreated)))>(c.tmeTargetMiss*60),'RED','ORANGE'),'BLACK') "
                     + ",'Order',a.strWaiterNo "
                     + "FROM tblitemrtemp a "
                     + ",tbltablemaster b "
                     + ",tblitemmaster c,tblmenuitempricingdtl d,tblcostcentermaster e "
                     + "WHERE LEFT(a.strItemCode,7)=c.strItemCode AND a.strNCKotYN='N' "
                     + "AND a.tdhComboItemYN='N' AND a.strTableNo=b.strTableNo "
                     + "AND a.strItemProcessed='N' AND c.strItemCode=d.strItemCode "
                     + "AND a.strPOSCode=d.strPosCode AND (d.strPosCode='"+clsGlobalVarClass.gPOSCode+"' OR d.strPosCode='All') "
                     + "AND d.strCostCenterCode=e.strCostCenterCode AND e.strCostCenterCode='"+costCenterCode+"' "
                     + "AND a.strItemCode NOT IN(SELECT strItemCode FROM tblkdsprocess WHERE strBP='P' "
                     + "AND strKDSName='KOT' AND strCostCenterCode='"+costCenterCode+"' AND a.strKOTNo=strDocNo)"
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
                     + "AND (d.strPosCode='"+clsGlobalVarClass.gPOSCode+"' OR d.strPosCode='All') AND d.strCostCenterCode=e.strCostCenterCode "
                     + "AND e.strCostCenterCode='"+costCenterCode+"' And date(a.dteVoidedDate)='"+posDate+"'  "
                     + "AND a.strItemProcessed='N' AND a.strItemCode NOT IN(SELECT strItemCode FROM tblkdsprocess WHERE strBP='P' "
                     + "AND strKDSName='KOT' AND strCostCenterCode='"+costCenterCode+"' AND a.strKOTNo=strDocNo) "
                     + "GROUP BY a.strTableNo,a.strKOTNo,a.strItemCode,a.strItemName "
                     + "ORDER BY a.dteDateCreated DESC, TIME(a.dteDateCreated) DESC"
                     + ")ORDER BY strKOTNo DESC,dteKOTDate DESC ";
	     */

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
		    + "AND d.strCostCenterCode=e.strCostCenterCode AND e.strCostCenterCode='" + costCenterCode + "' "
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
		    + "AND e.strCostCenterCode='" + costCenterCode + "' And date(a.dteVoidedDate)='" + posDate + "'  "
		    + "AND a.strItemProcessed='N'  "
		    + "GROUP BY a.strTableNo,a.strKOTNo,a.strItemCode,a.strItemName "
		    + "ORDER BY a.dteDateCreated DESC, TIME(a.dteDateCreated) DESC"
		    + ")ORDER BY strKOTNo DESC,dteKOTDate DESC ";
	    int ITEMCOUNTER = 0;
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

	    if (gITEMCOUNTER != ITEMCOUNTER)
	    {
		funPlayNewOrderNotificationAlert();
		gITEMCOUNTER = ITEMCOUNTER;		
	    }
	    else
	    {
		gITEMCOUNTER = ITEMCOUNTER;
	    }
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

    private String[] funGetListDtl(int billIndex)
    {
	ArrayList<clsBillDtl> listBillItemDtl = listOfKOTs.get(billIndex);
	String[] modelList = new String[listBillItemDtl.size()];
	int itemIndex = 0;
	for (int i = 0; i < listBillItemDtl.size(); i++)
	{
	    clsBillDtl objBillItemDtl = listBillItemDtl.get(i);
	    modelList[itemIndex++] = objBillItemDtl.getDblQuantity() + " " + objBillItemDtl.getStrItemName() + "!" + objBillItemDtl.getStrRemark() + "!" + objBillItemDtl.getStrWaiterNo() + "!" + objBillItemDtl.getDteBillDate() + "!" + objBillItemDtl.getStrItemCode() + "!" + objBillItemDtl.getStrItemCode();

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

    private void funLoadBillArrayList()
    {
	Iterator<Map.Entry<String, ArrayList<clsBillDtl>>> it = mapKOTHd.entrySet().iterator();
	while (it.hasNext())
	{
	    Map.Entry<String, ArrayList<clsBillDtl>> entry = it.next();
	    listOfKOTs.add(entry.getValue());
	}
    }

}
