/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSTransaction.controller;



import com.POSGlobal.controller.clsBenowIntegration;
import com.POSGlobal.controller.clsBillDtl;
import com.POSGlobal.controller.clsBillItemDtl;
import com.POSGlobal.controller.clsCRMInterface;
import com.POSGlobal.controller.clsGlobalVarClass;
import com.POSGlobal.controller.clsRewards;
import com.POSGlobal.controller.clsUtility;
import com.POSGlobal.controller.clsUtility2;
import com.POSGlobal.view.frmAlfaNumericKeyBoard;
import com.POSGlobal.view.frmOkPopUp;
import com.POSGlobal.view.frmSearchFormDialog;
import com.POSTransaction.view.frmAdvanceReceipt;
import com.POSTransaction.view.frmBillSettlement;
import com.POSTransaction.view.frmDirectBiller;
import java.awt.Image;
import java.io.File;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author Ajim
 */
public class clsCalculateBillDiscount
{

    private frmBillSettlement objFrmBillSettlement;
    private clsUtility objUtility;
    private clsUtility2 objUtility2;
    private final DecimalFormat gDecimalFormat = clsGlobalVarClass.funGetGlobalDecimalFormatter();
    private clsBillSettlementUtility objBillSettlementUtility;

    public clsCalculateBillDiscount()
    {
	objUtility = new clsUtility();
	objUtility2 = new clsUtility2();
	objBillSettlementUtility = new clsBillSettlementUtility(objFrmBillSettlement);
    }

    public clsCalculateBillDiscount(frmBillSettlement objFrmBillSettlement)
    {
	this.objFrmBillSettlement = objFrmBillSettlement;
	objUtility = new clsUtility();
	objUtility2 = new clsUtility2();
	objBillSettlementUtility = new clsBillSettlementUtility(objFrmBillSettlement);
    }

    public String funGetExcludePromoItemsForDiscount()
    {
	StringBuilder sqlBuilder = new StringBuilder(" (");
	try
	{
	    int i = 0;
	    for (String promoItemCode : objFrmBillSettlement.getHmPromoItem().keySet())
	    {
		if (!objFrmBillSettlement.getMapPromoItemDisc().containsKey(promoItemCode))//if item not in promo discount then ignore it for discount
		{
		    if (i == 0)
		    {
			sqlBuilder.append("'" + promoItemCode + "'");
		    }
		    else
		    {
			sqlBuilder.append(",'" + promoItemCode + "'");
		    }
		    i++;
		}
	    }
	    sqlBuilder.append(") ");
	    if (i == 0)
	    {
		sqlBuilder.setLength(0);
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	    objUtility.funWriteErrorLog(e);
	}
	finally
	{
	    return sqlBuilder.toString();
	}
    }

    public void funShowComplimentaryItemsButtonYN()
    {
	try
	{
	    if (clsGlobalVarClass.gUserType.equalsIgnoreCase("super"))
	    {
		objFrmBillSettlement.getBtnShowCompliItems().setVisible(true);
	    }
	    else
	    {

		String formName = "Complimentary Items";
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("select a.strUserCode,a.strFormName,a.strGrant,a.strTLA "
			+ "from tbluserdtl a "
			+ "where a.strFormName='" + formName + "' "
			+ "and (a.strGrant='true' or a.strTLA='true' ) "
			+ "and a.strUserCode='" + clsGlobalVarClass.gUserCode + "' ");
		ResultSet rsTLA = clsGlobalVarClass.dbMysql.executeResultSet(sqlBuilder.toString());
		if (rsTLA.next())
		{
		    objFrmBillSettlement.getBtnShowCompliItems().setVisible(true);
		}
		else
		{
		    objFrmBillSettlement.getBtnShowCompliItems().setVisible(false);
		}
		rsTLA.close();
	    }
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    e.printStackTrace();
	}
    }

    public void funShowRewardsButtonClicked()
    {
	List<clsRewards> listOfRewards = new ArrayList<>();
	try
	{
	    if (clsGlobalVarClass.gCustomerCode == null || clsGlobalVarClass.gCustomerCode.isEmpty())
	    {
		return;
	    }

	    clsCRMInterface objCRMInterface = new clsCRMInterface();

	    String sql_CustMb = "select longMobileNo,strCustomerCode,strCustomerName from tblcustomermaster "
		    + "where strCustomerCode='" + clsGlobalVarClass.gCustomerCode + "'";
	    ResultSet rsCust = clsGlobalVarClass.dbMysql.executeResultSet(sql_CustMb);
	    if (rsCust.next())
	    {
		listOfRewards = objCRMInterface.funGetCustomerRewards(rsCust.getString(1));

//                frmShowRewards objShowRewards = new frmShowRewards(this, rsCust.getString(2), rsCust.getString(3), rsCust.getString(1), listOfRewards);
//                objShowRewards.setVisible(true);
//                objShowRewards.setLocationRelativeTo(this);
	    }
	    rsCust.close();

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	    objUtility.funWriteErrorLog(e);
	}
    }

    public void funSetCustomerRewards(clsRewards objCustomerRewards)
    {
	objFrmBillSettlement.setRewardId(objCustomerRewards.getStrRewardId());

	if (objCustomerRewards.getStrRewardCategory().equalsIgnoreCase("Percentage Off"))//for % discount
	{
	    if (objCustomerRewards.getStrPoints().equalsIgnoreCase("NA"))
	    {
		System.out.println("No Points");
	    }
	    else
	    {
		double points = Double.parseDouble(objCustomerRewards.getStrPoints());
		if (points > 0)
		{
		    double discPercentage = points;
		    if (objCustomerRewards.isItemOff())
		    {
			String rewardPOSItemCode = objCustomerRewards.getStrRewardPOSItemCode();
			System.out.println("Reward POS Item Code=" + rewardPOSItemCode);
			//rewardPOSItemCode = "I000470";
			if (objFrmBillSettlement.getHmBillItemDtl().containsKey(rewardPOSItemCode))
			{
			    objFrmBillSettlement.getRdbItemWise().setSelected(true);
			    funFillItemList();
			    clsBillItemDtl objItem = objFrmBillSettlement.getHmBillItemDtl().get(rewardPOSItemCode);
			    String itemName = objItem.getItemName();
			    System.out.println("Reward POS Item Name=" + itemName);
			    objFrmBillSettlement.getCmbItemCategory().setSelectedItem(itemName);

			    objFrmBillSettlement.funDiscPercentageMouseClicked();
			    objFrmBillSettlement.getTxtDiscountPer().setText(String.valueOf(discPercentage));
			    funDiscountOKButtonPressed("Manual");
			}
			else
			{
			    System.out.println("No Item Found for this item code->" + rewardPOSItemCode);
			}
		    }
		    else
		    {
			objFrmBillSettlement.getRdbAll().setSelected(true);

			objFrmBillSettlement.funDiscPercentageMouseClicked();
			objFrmBillSettlement.getTxtDiscountPer().setText(String.valueOf(discPercentage));
			funDiscountOKButtonPressed("Manual");
		    }
		}
		else
		{
		    System.out.println("No Points");
		}
	    }
	}
	else if (objCustomerRewards.getStrRewardCategory().equalsIgnoreCase("Rupees Off"))//for amt discount
	{
	    if (objCustomerRewards.getStrPoints().equalsIgnoreCase("NA"))
	    {
		System.out.println("No Points");
	    }
	    else
	    {
		double points = Double.parseDouble(objCustomerRewards.getStrPoints());
		if (points > 0)
		{
		    double discAmtount = points;

		    if (objCustomerRewards.isItemOff())
		    {
			String rewardPOSItemCode = objCustomerRewards.getStrRewardPOSItemCode();
			System.out.println("Reward POS Item Code=" + rewardPOSItemCode);
			//rewardPOSItemCode = "I000470";
			if (objFrmBillSettlement.getHmBillItemDtl().containsKey(rewardPOSItemCode))
			{
			    objFrmBillSettlement.getRdbItemWise().setSelected(true);
			    funFillItemList();
			    clsBillItemDtl objItem = objFrmBillSettlement.getHmBillItemDtl().get(rewardPOSItemCode);
			    String itemName = objItem.getItemName();
			    System.out.println("Reward POS Item Name=" + itemName);
			    objFrmBillSettlement.getCmbItemCategory().setSelectedItem(itemName);

			    objFrmBillSettlement.funDiscAmountMouseClicked();
			    objFrmBillSettlement.getTxtDiscountAmt().setText(String.valueOf(discAmtount));
			    funDiscountOKButtonPressed("Manual");
			}
			else
			{
			    System.out.println("No Item Found for this item code->" + rewardPOSItemCode);
			}
		    }
		    else
		    {
			objFrmBillSettlement.getRdbAll().setSelected(true);

			objFrmBillSettlement.funDiscAmountMouseClicked();
			objFrmBillSettlement.getTxtDiscountAmt().setText(String.valueOf(discAmtount));
			funDiscountOKButtonPressed("Manual");
		    }
		}
		else
		{
		    System.out.println("No Points");
		}
	    }
	}
	else if (objCustomerRewards.getStrRewardCategory().equalsIgnoreCase("Free Item"))
	{
	    String freeItemCode = objCustomerRewards.getStrRewardPOSItemCode();

	    Map<String, clsBillItemDtl> hmSelectedItems = new HashMap<>();

	    clsBillItemDtl objFreeItem = new clsBillItemDtl();
	    objFreeItem.setItemCode(freeItemCode);
	    objFreeItem.setQuantity(1.00);

	    hmSelectedItems.put(freeItemCode, objFreeItem);

	    funSetComplimentaryItems(hmSelectedItems);

	}
    }

    public void funSetComplimentaryItems(Map<String, clsBillItemDtl> hmSelectedItems)
    {
	System.out.println("In Bill Settlement");

	if (null != hmSelectedItems && hmSelectedItems.size() > 0)
	{
	    objFrmBillSettlement.getHmComplimentaryBillItemDtl().clear();

	    for (Map.Entry<String, clsBillItemDtl> entry : objFrmBillSettlement.getHmBillItemDtl().entrySet())
	    {
//                System.out.println("Item= " + entry.getValue().getItemName());
		String key = entry.getKey().substring(0, 7);
		clsBillItemDtl objBillItemDtl = entry.getValue();
		if (hmSelectedItems.containsKey(key))
		{
		    double amt = hmSelectedItems.get(key).getQuantity() * objBillItemDtl.getRate();

		    objFrmBillSettlement.getHmBillItemDtl().get(entry.getKey()).setDiscountAmount(0);
		    objFrmBillSettlement.getHmBillItemDtl().get(entry.getKey()).setDiscountPercentage(0);

		    if (!entry.getKey().contains("M"))
		    {
			objFrmBillSettlement.getMapBeforeComplimentory().put(key, objFrmBillSettlement.getHmBillItemDtl().get(entry.getKey()).getAmount());
			objFrmBillSettlement.getHmBillItemDtl().get(entry.getKey()).setAmount(objFrmBillSettlement.getHmBillItemDtl().get(entry.getKey()).getAmount() - amt);
			// hmBillItemDtl.get(entry.getKey()).setQuantity(hmBillItemDtl.get(entry.getKey()).getQuantity()- hmSelectedItems.get(key).getQuantity());
			clsBillDtl objBillDtl = new clsBillDtl();
			objBillDtl.setStrItemCode(entry.getValue().getItemCode());
			objBillDtl.setStrItemName(entry.getValue().getItemName());
			objBillDtl.setDblQuantity(hmSelectedItems.get(key).getQuantity());
			objBillDtl.setDblAmount(amt);
			objBillDtl.setDblRate(objBillItemDtl.getRate());
			objBillDtl.setDblComplQty(hmSelectedItems.get(key).getQuantity());
			objFrmBillSettlement.getHmComplimentaryBillItemDtl().put(entry.getValue().getItemCode(), objBillDtl);

		    }
		}
	    }

	    double _subTotal = 0;
	    for (Map.Entry<String, clsBillItemDtl> entry : objFrmBillSettlement.getHmBillItemDtl().entrySet())
	    {
		_subTotal += entry.getValue().getAmount();
	    }
	    objFrmBillSettlement.setSubTotal(_subTotal);

	    if (objFrmBillSettlement.getHmComplimentaryBillItemDtl().size() > 0)
	    {
		if (objFrmBillSettlement.getBillType().equals("Direct Biller"))
		{
		    objFrmBillSettlement.getPanelRemaks().setLocation(objFrmBillSettlement.getPanelCheque().getLocation());
		    objFrmBillSettlement.getPanelRemaks().setVisible(true);
		}
		else
		{
		    objFrmBillSettlement.getPanelRemaks().setLocation(objFrmBillSettlement.getPanelAmt().getLocation());
		    objFrmBillSettlement.getPanelRemaks().setVisible(true);
		}
		if (clsGlobalVarClass.gTouchScreenMode)
		{
		    new frmAlfaNumericKeyBoard(objFrmBillSettlement, true, "1", "Enter Complimentary Remarks").setVisible(true);
		    objFrmBillSettlement.getTxtAreaRemark().setText(clsGlobalVarClass.gKeyboardValue);
		    clsGlobalVarClass.gKeyboardValue = "";
		}
		else
		{
		    String complRemarks = JOptionPane.showInputDialog(null, "Enter Complimentary Remarks");
		    objFrmBillSettlement.getTxtAreaRemark().setText(complRemarks);
		}

		if (objFrmBillSettlement.getvComplReasonCode().size() == 0)
		{
		    JOptionPane.showMessageDialog(objFrmBillSettlement, "No complementary reasons are created");
		    return;
		}
		else
		{
		    Object[] arrObjReasonName = objFrmBillSettlement.getvComplReasonName().toArray();
		    String selectedReason = (String) JOptionPane.showInputDialog(objFrmBillSettlement, "Please Select Reason?", "Reason", JOptionPane.QUESTION_MESSAGE, null, arrObjReasonName, arrObjReasonName[0]);
		    if (null == selectedReason)
		    {
			JOptionPane.showMessageDialog(objFrmBillSettlement, "Please Select Reason");
			return;
		    }
		    else
		    {
			for (int cntReason = 0; cntReason < objFrmBillSettlement.getvComplReasonCode().size(); cntReason++)
			{
			    if (objFrmBillSettlement.getvComplReasonName().elementAt(cntReason).toString().equals(selectedReason))
			    {
				String selectedReasonCode = objFrmBillSettlement.getvComplReasonCode().elementAt(cntReason).toString();
				objFrmBillSettlement.setSelectedReasonCode(selectedReasonCode);
				break;
			    }
			}
		    }
		}
	    }
	    objFrmBillSettlement.funRefreshItemTable();
	}
    }

    /**
     * 
     * @see funNormalDiscount
     */
    public void funDiscountOKButtonPressed(String type)
    {
	try
	{
	    if (type.equalsIgnoreCase("Manual") && clsGlobalVarClass.gTransactionType.equalsIgnoreCase("ModifyBill") && (objFrmBillSettlement.getTxtDiscountPer().getText().length() <= 0 || objFrmBillSettlement.getTxtDiscountAmt().getText().length() <= 0 || (Double.parseDouble(objFrmBillSettlement.getTxtDiscountPer().getText()) < 0 && Double.parseDouble(objFrmBillSettlement.getTxtDiscountAmt().getText()) < 0)))
	    {
		JOptionPane.showMessageDialog(objFrmBillSettlement, "Please Enter Discount.");
		return;
	    }
	    else if (type.equalsIgnoreCase("Manual") && !clsGlobalVarClass.gTransactionType.equalsIgnoreCase("ModifyBill") && (objFrmBillSettlement.getTxtDiscountPer().getText().length() <= 0 || objFrmBillSettlement.getTxtDiscountAmt().getText().length() <= 0 || (Double.parseDouble(objFrmBillSettlement.getTxtDiscountPer().getText()) <= 0 && Double.parseDouble(objFrmBillSettlement.getTxtDiscountAmt().getText()) <= 0)))
	    {
		JOptionPane.showMessageDialog(objFrmBillSettlement, "Please Enter Discount.");
		return;
	    }

	    if (clsGlobalVarClass.gTransactionType.equalsIgnoreCase("ModifyBill"))
	    {
		Iterator<String> it = objFrmBillSettlement.getMapBillDiscDtl().keySet().iterator();
		if (it.hasNext())
		{
		    String key = it.next();
		    String discType = key.split("!")[0];
		    String discValue = key.split("!")[1];
		    if (objFrmBillSettlement.getRdbSubGroupWise().isSelected() && discType.equalsIgnoreCase("SubGroupWise"))
		    {
			funNormalDiscount();
		    }
		    else if (objFrmBillSettlement.getRdbGroupWise().isSelected() && discType.equalsIgnoreCase("GroupWise"))
		    {
			funNormalDiscount();
		    }
		    else if (type.equalsIgnoreCase("Manual") && objFrmBillSettlement.getRdbItemWise().isSelected() && discType.equalsIgnoreCase("ItemWise"))
		    {
			String itemCode = objFrmBillSettlement.getHmItemList().get(objFrmBillSettlement.getCmbItemCategory().getSelectedItem().toString());
			clsBillItemDtl objBillItemDtl = objFrmBillSettlement.getHmBillItemDtl().get(itemCode);
			double itemAmt = objBillItemDtl.getAmount();
			if (itemAmt < 1)
			{
			    JOptionPane.showMessageDialog(objFrmBillSettlement, "Discount Is Not Applicable On Zero Amount.");
			    return;
			}

			double discountAmt = Double.parseDouble(objFrmBillSettlement.getTxtDiscountAmt().getText());
			double discountPer = Double.parseDouble(objFrmBillSettlement.getTxtDiscountPer().getText());

			if (discountAmt > 0)
			{
			    if (discountAmt > itemAmt)
			    {
				JOptionPane.showMessageDialog(objFrmBillSettlement, "Discount Is Greater Than Item Amount.");
				return;
			    }
			}
			else if (discountPer > 0)
			{
			    discountAmt = (discountPer / 100) * itemAmt;
			    if (discountAmt > itemAmt)
			    {
				JOptionPane.showMessageDialog(objFrmBillSettlement, "Discount Is Greater Than Item Amount.");
				return;
			    }
			}
			funNormalDiscount();
		    }
		    else if (objFrmBillSettlement.getRdbAll().isSelected() && discType.equalsIgnoreCase("Total"))
		    {
			funNormalDiscount();
		    }
		    else
		    {
			objFrmBillSettlement.getMapBillDiscDtl().clear();
			for (clsBillItemDtl objBillItemDtl : objFrmBillSettlement.getHmBillItemDtl().values())
			{
			    String keyCode = (objBillItemDtl.getItemCode().contains("M") ? objBillItemDtl.getItemCode() + "!" + objBillItemDtl.getItemName() : objBillItemDtl.getItemCode());

			    objFrmBillSettlement.getHmBillItemDtl().get(keyCode).setDiscountPercentage(0.00);
			    objFrmBillSettlement.getHmBillItemDtl().get(keyCode).setDiscountAmount(0.00);
			}
			funNormalDiscount();
		    }
		}
		else
		{
		    funNormalDiscount();
		}
	    }
	    if (objFrmBillSettlement.getRdbItemWise().isSelected() && type.equalsIgnoreCase("Manual"))
	    {
		String itemCode = objFrmBillSettlement.getHmItemList().get(objFrmBillSettlement.getCmbItemCategory().getSelectedItem().toString());
		clsBillItemDtl objBillItemDtl = objFrmBillSettlement.getHmBillItemDtl().get(itemCode);
		double itemAmt = objBillItemDtl.getAmount();
		if (itemAmt < 1)
		{
		    JOptionPane.showMessageDialog(objFrmBillSettlement, "Discount Is Not Applicable On Zero Amount.");
		    return;
		}

		double discountAmt = Double.parseDouble(objFrmBillSettlement.getTxtDiscountAmt().getText());
		double discountPer = Double.parseDouble(objFrmBillSettlement.getTxtDiscountPer().getText());

		if (discountAmt > 0)
		{
		    if (discountAmt > itemAmt)
		    {
			JOptionPane.showMessageDialog(objFrmBillSettlement, "Discount Is Greater Than Item Amount.");
			return;
		    }
		}
		else if (discountPer > 0)
		{
		    discountAmt = (discountPer / 100) * itemAmt;
		    if (discountAmt > itemAmt)
		    {
			JOptionPane.showMessageDialog(objFrmBillSettlement, "Discount Is Greater Than Item Amount.");
			return;
		    }
		}
	    }

	    if (objFrmBillSettlement.getMapBillDiscDtl().size() > 0 && clsGlobalVarClass.gTransactionType != ("ModifyBill"))
	    {
		Iterator<String> it = objFrmBillSettlement.getMapBillDiscDtl().keySet().iterator();
		if (it.hasNext())
		{
		    String key = it.next();
		    String discType = key.split("!")[0];
		    String discValue = key.split("!")[1];
		    if (objFrmBillSettlement.getRdbSubGroupWise().isSelected() && discType.equalsIgnoreCase("SubGroupWise"))
		    {
			funNormalDiscount();
		    }
		    else if (objFrmBillSettlement.getRdbGroupWise().isSelected() && discType.equalsIgnoreCase("GroupWise"))
		    {
			funNormalDiscount();
		    }
		    else if (type.equalsIgnoreCase("Manual") && objFrmBillSettlement.getRdbItemWise().isSelected() && discType.equalsIgnoreCase("ItemWise"))
		    {
			String itemCode = objFrmBillSettlement.getHmItemList().get(objFrmBillSettlement.getCmbItemCategory().getSelectedItem().toString());
			clsBillItemDtl objBillItemDtl = objFrmBillSettlement.getHmBillItemDtl().get(itemCode);
			double itemAmt = objBillItemDtl.getAmount();

			double discountAmt = Double.parseDouble(objFrmBillSettlement.getTxtDiscountAmt().getText());
			double discountPer = Double.parseDouble(objFrmBillSettlement.getTxtDiscountPer().getText());

			if (discountAmt > 0)
			{
			    if (discountAmt > itemAmt)
			    {
				JOptionPane.showMessageDialog(objFrmBillSettlement, "Discount Is Greater Than Item Amount.");
				return;
			    }
			}
			else if (discountPer > 0)
			{
			    discountAmt = (discountPer / 100) * itemAmt;
			    if (discountAmt > itemAmt)
			    {
				JOptionPane.showMessageDialog(objFrmBillSettlement, "Discount Is Greater Than Item Amount.");
				return;
			    }
			}
			funNormalDiscount();
		    }
		    else if (objFrmBillSettlement.getRdbAll().isSelected() && discType.equalsIgnoreCase("Total"))
		    {
			funNormalDiscount();
		    }
		    else
		    {
			objFrmBillSettlement.getMapBillDiscDtl().clear();
			for (clsBillItemDtl objBillItemDtl : objFrmBillSettlement.getHmBillItemDtl().values())
			{
			    String keyCode = (objBillItemDtl.getItemCode().contains("M") ? objBillItemDtl.getItemCode() + "!" + objBillItemDtl.getItemName() : objBillItemDtl.getItemCode());

			    objFrmBillSettlement.getHmBillItemDtl().get(keyCode).setDiscountPercentage(0.00);
			    objFrmBillSettlement.getHmBillItemDtl().get(keyCode).setDiscountAmount(0.00);
			}
			funNormalDiscount();
		    }
		}
	    }
	    else
	    {
		if (clsGlobalVarClass.gTransactionType != ("ModifyBill"))
		{
		    funNormalDiscount();
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
     * @see funApplyDiscountOnBill
     */
    private void funNormalDiscount()
    {
	if (objFrmBillSettlement.getRdbSubGroupWise().isSelected())
	{
	    //code to apply discount on selected SubGroup Only
	    if (objFrmBillSettlement.getCmbItemCategory().getSelectedIndex() == 0)
	    {
		JOptionPane.showMessageDialog(objFrmBillSettlement, "Please select subGroup");
		objFrmBillSettlement.getCmbItemCategory().requestFocus();
		return;
	    }
	    funApplyDiscountOnBill();
	}
	else if (objFrmBillSettlement.getRdbGroupWise().isSelected())
	{
	    //code to apply discount on group only
	    if (objFrmBillSettlement.getCmbItemCategory().getSelectedIndex() == 0)
	    {
		JOptionPane.showMessageDialog(objFrmBillSettlement, "Please select Group");
		objFrmBillSettlement.getCmbItemCategory().requestFocus();
		return;
	    }
	    funApplyDiscountOnBill();
	}
	else if (clsGlobalVarClass.gFlgPoints.equals("DiscountPoints"))
	{
	    double discAmount = Double.parseDouble(objFrmBillSettlement.getTxtDiscountAmt().getText());
	    if (objFrmBillSettlement.getObjDirectBiller().getObjData().getRedeemed_amt() < discAmount)
	    {
		JOptionPane.showMessageDialog(objFrmBillSettlement, "Edited discount amount can not be greater than requested discount amount!");
		return;
	    }
	    JOptionPane.showMessageDialog(objFrmBillSettlement, "Customer has a valid discount request of " + discAmount);
	    funApplyDiscountOnBill();
	}
	else
	{
	    funApplyDiscountOnBill();//apply Discount on whole Bill
	}
    }

      /**
     * @see funCalculateDiscount
     */
    private void funApplyDiscountOnBill()
    {
	try
	{
	    if (objFrmBillSettlement.getBillType().equals("Direct Biller"))
	    {
		objFrmBillSettlement.getPanelRemaks().setLocation(objFrmBillSettlement.getPanelCheque().getLocation());
		objFrmBillSettlement.getPanelRemaks().setVisible(true);
	    }
	    else if (objFrmBillSettlement.getBillType().equals("Bill From KOTs"))
	    {
		objFrmBillSettlement.getPanelRemaks().setLocation(objFrmBillSettlement.getPanelCoupen().getLocation());
		objFrmBillSettlement.getPanelRemaks().setVisible(true);
	    }
	    else
	    {
		objFrmBillSettlement.getPanelRemaks().setLocation(objFrmBillSettlement.getPanelAmt().getLocation());
		objFrmBillSettlement.getPanelRemaks().setVisible(true);
	    }

	    String selectedReasonCode = "";
	    String discountRemarks = "";
	    boolean needToSelectReasonRemark = true;
	    if (clsGlobalVarClass.gCRMInterface.equalsIgnoreCase("HASH TAG CRM Interface") && objFrmBillSettlement.getRewardId() != null && objFrmBillSettlement.getRewardId().trim().length() > 0)
	    {
		String reasonCode = objUtility2.funGetDefaultReasonCode("strHashTagLoyalty");
		if (reasonCode.trim().length() <= 0)
		{
		    JOptionPane.showMessageDialog(objFrmBillSettlement, "No Hash Tag reasons are created.");
		    return;
		}
		else
		{
		    selectedReasonCode = reasonCode;
		    discountRemarks = "Hashtag Loyalty Redemption";
		    needToSelectReasonRemark = false;
		}
	    }
	    else
	    {
		needToSelectReasonRemark = true;
	    }

	    if (needToSelectReasonRemark)
	    {
		if (clsGlobalVarClass.gTouchScreenMode)
		{
		    new frmAlfaNumericKeyBoard(objFrmBillSettlement, true, "1", "Enter Discount Remark.").setVisible(true);
		    objFrmBillSettlement.getTxtAreaRemark().setText(clsGlobalVarClass.gKeyboardValue);
		    discountRemarks = objUtility.funCheckSpecialCharacters(clsGlobalVarClass.gKeyboardValue);
		}
		else
		{
		    discountRemarks = JOptionPane.showInputDialog(null, "Enter Discount Remarks");
		    discountRemarks = objUtility.funCheckSpecialCharacters(discountRemarks);
		    objFrmBillSettlement.getTxtAreaRemark().setText(discountRemarks);
		}
		if (objFrmBillSettlement.getvReasonCodeForDiscount().size() == 0)
		{
		    JOptionPane.showMessageDialog(objFrmBillSettlement, "No Discount reasons are created");
		    return;
		}
		else
		{
		    Object[] arrObjReasonCode = objFrmBillSettlement.getvReasonCodeForDiscount().toArray();
		    Object[] arrObjReasonName = objFrmBillSettlement.getvReasonNameForDiscount().toArray();
		    String selectedReason = (String) JOptionPane.showInputDialog(objFrmBillSettlement, "Please Select Reason?", "Reason", JOptionPane.QUESTION_MESSAGE, null, arrObjReasonName, arrObjReasonName[0]);
		    if (null == selectedReason)
		    {
			JOptionPane.showMessageDialog(objFrmBillSettlement, "Please Select Reason");
			return;
		    }
		    else
		    {
			for (int cntReason = 0; cntReason < objFrmBillSettlement.getvReasonCodeForDiscount().size(); cntReason++)
			{
			    if (objFrmBillSettlement.getvReasonNameForDiscount().elementAt(cntReason).toString().equals(selectedReason))
			    {
				selectedReasonCode = objFrmBillSettlement.getvReasonCodeForDiscount().elementAt(cntReason).toString();
				break;
			    }
			}
		    }
		}
	    }

	    objFrmBillSettlement.setSelectedReasonCode(selectedReasonCode);
	    objFrmBillSettlement.setDiscountRemarks(discountRemarks);

	    if (objFrmBillSettlement.getChkDiscFromMaster().isSelected() && !objFrmBillSettlement.getDiscountCode().isEmpty())
	    {
		String sql = "select a.strDiscCode,b.strDiscOn,a.strDiscOnCode,a.strDiscOnName,a.strDiscountType,a.dblDiscountValue  "
			+ "from tbldiscdtl a,tbldischd b "
			+ "where a.strDiscCode=b.strDiscCode "
			+ "and a.strDiscCode='" + objFrmBillSettlement.getDiscountCode() + "' ";
		ResultSet rsMasterDisc = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		while (rsMasterDisc.next())
		{
		    String discOn = rsMasterDisc.getString(2);
		    String discOnCode = rsMasterDisc.getString(3);
		    String discOnName = rsMasterDisc.getString(4);
		    String discTypePerAmt = rsMasterDisc.getString(5);
		    String discValue = rsMasterDisc.getString(6);

		    funCalculateDiscount("Master", discOn, discOnCode, discOnName, discTypePerAmt, discValue);
		}
		rsMasterDisc.close();
	    }
	    else
	    {
		funCalculateDiscount("Manual", "", "", "", "", "");
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    public void funDiscountFromMasterCheckBoxClicked()
    {
	try
	{
	    objFrmBillSettlement.setDiscountCode("");
	    String operationType=objFrmBillSettlement.getOperationTypeForTax();
	    
	    clsUtility obj = new clsUtility();
	    
	    obj.funCallForSearchForm("DiscountMaster",operationType,"","");	    
	    new frmSearchFormDialog(objFrmBillSettlement, true).setVisible(true);
	    
	    if (clsGlobalVarClass.gSearchItemClicked)
	    {
		Object[] data = clsGlobalVarClass.gArrListSearchData.toArray();
		funSetDiscountMasterData(data);
		clsGlobalVarClass.gSearchItemClicked = false;
	    }
	    else
	    {
		objFrmBillSettlement.getChkDiscFromMaster().setSelected(false);
		objFrmBillSettlement.setDiscountCode("");
	    }

	    
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	    objUtility.funWriteErrorLog(e);
	}
    }

    public void funSetDiscountMasterData(Object[] data)
    {
	try
	{
	    String discCode = data[0].toString();
	    String discName = data[1].toString();
	    String posCode = data[2].toString();
	    String discOn = data[3].toString();

	    funSetSelectedDiscRadioButtons(discCode, discName, discOn);
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	    objUtility.funWriteErrorLog(e);
	    objFrmBillSettlement.getChkDiscFromMaster().setSelected(false);
	}
    }

    public void funSetSelectedDiscRadioButtons(String discCode, String discName, String discOn)
    {
	try
	{
	    String discountCode = discCode;
	    objFrmBillSettlement.setDiscountCode(discountCode);

	    if (discOn.equalsIgnoreCase("All"))
	    {
		objFrmBillSettlement.getRdbAll().setSelected(true);

		String sql = "select a.strDiscCode,a.strDiscOnCode,a.strDiscOnName,a.strDiscountType,a.dblDiscountValue  "
			+ "from tbldiscdtl a  "
			+ "where a.strDiscCode='" + discCode + "' ";
		ResultSet rsMasterDisc = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		if (rsMasterDisc.next())
		{
		    String discType = rsMasterDisc.getString(4);
		    String discValue = rsMasterDisc.getString(5);
		    if (discType.equalsIgnoreCase("Percentage"))//%
		    {
			objFrmBillSettlement.funDiscPercentageMouseClicked();

			objFrmBillSettlement.getTxtDiscountPer().setText(discValue);

		    }
		    else//Amount
		    {
			objFrmBillSettlement.funDiscAmountMouseClicked();

			objFrmBillSettlement.getTxtDiscountAmt().setText(discValue);
		    }

		    funDiscountOKButtonPressed("Master");
		}
		rsMasterDisc.close();
	    }
	    else if (discOn.equalsIgnoreCase("Group"))
	    {
		objFrmBillSettlement.getRdbGroupWise().setSelected(true);

		funDiscountOKButtonPressed("Master");
	    }
	    else if (discOn.equalsIgnoreCase("SubGroup"))
	    {
		objFrmBillSettlement.getRdbSubGroupWise().setSelected(true);

		funDiscountOKButtonPressed("Master");
	    }
	    else if (discOn.equalsIgnoreCase("Item"))
	    {
		objFrmBillSettlement.getRdbItemWise().setSelected(true);

		funDiscountOKButtonPressed("Master");
	    }

	    objFrmBillSettlement.setDiscountCode(discountCode);

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	    objUtility.funWriteErrorLog(e);
	    objFrmBillSettlement.getChkDiscFromMaster().setSelected(false);
	}
    }

    /**
     * This methos is responsible for to calculate discount for a bill
     * 
     * @param type  'type' may be Master or Manual(Master means discount is selected from discount master and manual means manually given discount)
     * @param discOn discOn may be one of the from 'All or Total,Group,SubGroup and Item'
     * @param discOnCode document code of discOnName 
     * @param discOnName if discOn is Group then discOnName will be GroupName (eg.FOOD) ,if discOn is SubGroup then discOnName will be SubGroupName,if discOn is Item then discOnName will be ItemName,if discOn is All/Total then discOnName will be All/Total
     * @param discTypePerAmt whether discount is discount percentage or discount value amount
     * @param discValue discount % number or discount value amount number
     * 
     * @see  funGetDiscountApplicableItemList for more details,which is called from this method itself.
     * 
     * This method will calculate discount and will update the item grid.
     */
    public void funCalculateDiscount(String type, String discOn, String discOnCode, String discOnName, String discTypePerAmt, String discValue)
    {

	if (type.equalsIgnoreCase("Master"))
	{
	    if (discTypePerAmt.equalsIgnoreCase("Percentage"))
	    {
		objFrmBillSettlement.funDiscPercentageMouseClicked();
		objFrmBillSettlement.getTxtDiscountPer().setText(discValue);

	    }
	    else
	    {
		objFrmBillSettlement.funDiscAmountMouseClicked();
		objFrmBillSettlement.getTxtDiscountAmt().setText(discValue);
	    }
	}

	List<clsBillItemDtl> listOfDiscApplicableItems = null;
	double dblDiscountOnAmt = 0.00;
	String discountOnType = "", discontOnValue = "";

	double amtDisc = Double.parseDouble(objFrmBillSettlement.getTxtDiscountAmt().getText());
	double perDisc = Double.parseDouble(objFrmBillSettlement.getTxtDiscountPer().getText());

	if (objFrmBillSettlement.getRdbAll().isSelected()) // All Item(Total)
	{
	    listOfDiscApplicableItems = funGetDiscountApplicableItemList(clsGlobalVarClass.gTransactionType, "Total", objFrmBillSettlement.getTableNo(), type, discOnCode);
	    discountOnType = "Total";
	    discontOnValue = "Total";
	}
	else if (objFrmBillSettlement.getRdbGroupWise().isSelected())//for Group
	{

	    listOfDiscApplicableItems = funGetDiscountApplicableItemList(clsGlobalVarClass.gTransactionType, "Group", objFrmBillSettlement.getTableNo(), type, discOnCode);
	    discountOnType = "GroupWise";
	    if (type.equalsIgnoreCase("Master"))
	    {
		discontOnValue = discOnName;
	    }
	    else
	    {
		if (objFrmBillSettlement.getCmbItemCategory() != null)
		{
		    discontOnValue = objFrmBillSettlement.getCmbItemCategory().getSelectedItem().toString();
		}
	    }
	}
	else if (objFrmBillSettlement.getRdbSubGroupWise().isSelected())//for SubGroup
	{
	    listOfDiscApplicableItems = funGetDiscountApplicableItemList(clsGlobalVarClass.gTransactionType, "SubGroup", objFrmBillSettlement.getTableNo(), type, discOnCode);
	    discountOnType = "SubGroupWise";
	    if (type.equalsIgnoreCase("Master"))
	    {
		discontOnValue = discOnName;
	    }
	    else
	    {
		if (objFrmBillSettlement.getCmbItemCategory() != null)
		{
		    discontOnValue = objFrmBillSettlement.getCmbItemCategory().getSelectedItem().toString();
		}
	    }
	}
	else if (objFrmBillSettlement.getRdbItemWise().isSelected())//for Item
	{
	    listOfDiscApplicableItems = funGetDiscountApplicableItemList(clsGlobalVarClass.gTransactionType, "Item", objFrmBillSettlement.getTableNo(), type, discOnCode);
	    discountOnType = "ItemWise";
	    if (type.equalsIgnoreCase("Master"))
	    {
		discontOnValue = discOnName;
	    }
	    else
	    {
		if (objFrmBillSettlement.getCmbItemCategory() != null)
		{
		    discontOnValue = objFrmBillSettlement.getCmbItemCategory().getSelectedItem().toString();
		}
	    }
	}
	if (listOfDiscApplicableItems.size() > 0)
	{
	    double totamt = new clsUtility().funGetTotalDiscOnAmt(listOfDiscApplicableItems);
	    if (clsGlobalVarClass.gApplyDiscountOn.equals("SubTotalTax"))
	    {
		totamt += objFrmBillSettlement.getDblTotalTaxAmt();
	    }
	    double sumGlobalAmt = (totamt * clsGlobalVarClass.gMaxDiscount) / 100;
	    if (totamt > 0 && (perDisc == 100.0 || clsGlobalVarClass.gMaxDiscount >= perDisc) && amtDisc <= totamt)
	    {
		if (amtDisc >= 0 && perDisc >= 0 && perDisc <= 100)
		{
		    if (listOfDiscApplicableItems.size() > 0)
		    {
			dblDiscountOnAmt = new clsUtility().funGetTotalDiscOnAmt(listOfDiscApplicableItems);
			for (int cnt = 0; cnt < listOfDiscApplicableItems.size(); cnt++)
			{
			    clsBillItemDtl objDiscItem = listOfDiscApplicableItems.get(cnt);
			    String key = objDiscItem.getItemCode();
			    if (objDiscItem.isIsModifier())
			    {
				key = key + "!" + objDiscItem.getItemName().toUpperCase();
			    }

			    clsBillItemDtl objBillItemDtl = objFrmBillSettlement.getHmBillItemDtl().get(key);

			    if (objFrmBillSettlement.getDiscountType().equals("Percent"))
			    {
				double discPer = Double.parseDouble(objFrmBillSettlement.getTxtDiscountPer().getText());

				objFrmBillSettlement.setDblDiscountPer(discPer);
				double discAmt = objBillItemDtl.getAmount() * (discPer / 100);

				discAmt = discAmt / objBillItemDtl.getQuantity();
				objBillItemDtl.setDiscountAmount(Double.parseDouble(gDecimalFormat.format(discAmt)));
				objBillItemDtl.setDiscountPercentage(Double.parseDouble(gDecimalFormat.format(discPer)));
			    }
			    else
			    {
				double discAmt = Double.parseDouble(objFrmBillSettlement.getTxtDiscountAmt().getText());
				double discPer = (discAmt / dblDiscountOnAmt) * 100;
				double discAmtForItem = objBillItemDtl.getAmount() * (discPer / 100);

				objFrmBillSettlement.getTxtDiscountPer().setText(gDecimalFormat.format(discPer));
				discAmtForItem = discAmtForItem / objBillItemDtl.getQuantity();
				objBillItemDtl.setDiscountAmount(Double.parseDouble(gDecimalFormat.format(discAmtForItem)));
				objBillItemDtl.setDiscountPercentage(Double.parseDouble(gDecimalFormat.format(discPer)));
			    }
			    objFrmBillSettlement.getHmBillItemDtl().put(key, objBillItemDtl);
			}
			if (objFrmBillSettlement.getDiscountType().equals("Percent"))
			{
			    double discPer = Double.parseDouble(objFrmBillSettlement.getTxtDiscountPer().getText());
			    double discAmt = dblDiscountOnAmt * (discPer / 100);
			    objFrmBillSettlement.getMapBillDiscDtl().put(discountOnType + "!" + discontOnValue, new clsBillDiscountDtl(objFrmBillSettlement.getDiscountRemarks(), objFrmBillSettlement.getSelectedReasonCode(), discPer, discAmt, dblDiscountOnAmt));
			}
			else
			{
			    double discAmt = Double.parseDouble(objFrmBillSettlement.getTxtDiscountAmt().getText());
			    double discPer = (discAmt / dblDiscountOnAmt) * 100;
			    objFrmBillSettlement.getMapBillDiscDtl().put(discountOnType + "!" + discontOnValue, new clsBillDiscountDtl(objFrmBillSettlement.getDiscountRemarks(), objFrmBillSettlement.getSelectedReasonCode(), discPer, discAmt, dblDiscountOnAmt));
			}
		    }

		    objFrmBillSettlement.funRefreshItemTable();
		    objFrmBillSettlement.getTxtAmount().setText(String.valueOf(gDecimalFormat.format(objFrmBillSettlement.getGrandTotal())));
		    objFrmBillSettlement.getTxtPaidAmt().setText(String.valueOf(gDecimalFormat.format(objFrmBillSettlement.getGrandTotal())));
		    objFrmBillSettlement.setAmountBox("PaidAmount");
		    objFrmBillSettlement.setTextValue2("0.00");
		}
		else
		{
		    objFrmBillSettlement.getTxtDiscountAmt().setText("0.00");
		    objFrmBillSettlement.getTxtDiscountPer().setText("0");
		    new frmOkPopUp(null, "Invalid Discount Amount", "Warning", 1).setVisible(true);
		    objFrmBillSettlement.getTxtDiscountAmt().requestFocus();
		}
	    }
	    else
	    {
		new frmOkPopUp(objFrmBillSettlement, "Discount is Ristricted", "Error", 0).setVisible(true);
	    }
	}
    }

    public void funShowDiscountPanel(boolean gEnableMasterDiscount)
    {
	objFrmBillSettlement.getLblDisc().setVisible(!gEnableMasterDiscount);
	objFrmBillSettlement.getTxtDiscountPer().setVisible(!gEnableMasterDiscount);
	objFrmBillSettlement.getLblDiscAmt().setVisible(!gEnableMasterDiscount);
	objFrmBillSettlement.getTxtDiscountAmt().setVisible(!gEnableMasterDiscount);
	objFrmBillSettlement.getRdbSubGroupWise().setVisible(!gEnableMasterDiscount);
	objFrmBillSettlement.getRdbGroupWise().setVisible(!gEnableMasterDiscount);
	objFrmBillSettlement.getRdbItemWise().setVisible(!gEnableMasterDiscount);
	objFrmBillSettlement.getRdbAll().setVisible(!gEnableMasterDiscount);
	objFrmBillSettlement.getCmbItemCategory().setVisible(!gEnableMasterDiscount);
	objFrmBillSettlement.getBtnDiscOk().setVisible(!gEnableMasterDiscount);

    }

    public void funGenerateQrCode(String QRString)
    {
	try
	{
//                    String filePath = "QRCode.png";
//                    String charset = "UTF-8"; // or "ISO-8859-1"
//                    Map hintMap = new HashMap();
//                    hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
//                    createQRCode(QRString, filePath, charset, hintMap, 135, 135);
//                    System.out.println("QR Code image created successfully!");
//                    System.out.println("Data read from QR Code: "
//                                    + readQRCode(filePath, charset, hintMap));
//                    funCopyImageIfPresent(new File(filePath));
//                    Image image = ImageIO.read(new File(filePath));
//                    ImageIcon icon = new ImageIcon(image);
//                    lblBenowQRCode.setIcon(icon);
	    String filePath = new clsBenowIntegration().funGenerateQrCode(QRString);
	    Image image = ImageIO.read(new File(filePath));
	    ImageIcon icon = new ImageIcon(image);
	    objFrmBillSettlement.getLblBenowQRCode().setIcon(icon);

	    if (clsGlobalVarClass.gBenowIntegrationYN)
	    {
		if (objFrmBillSettlement.getBillPrintOnSettlement().equalsIgnoreCase("Y"))
		{
		    objFrmBillSettlement.setBillPrintOnSettlement("N");
		    objBillSettlementUtility.funSendBillToPrint(objFrmBillSettlement.getVoucherNo(), objUtility.funGetOnlyPOSDateForTransaction());
		}
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	    objUtility.funWriteErrorLog(e);
	}
    }

    public void funFillItemList()
    {
	DefaultComboBoxModel dm = (DefaultComboBoxModel) objFrmBillSettlement.getCmbItemCategory().getModel();
	dm.removeAllElements();
	for (Map.Entry<String, String> entry : objFrmBillSettlement.getHmItemList().entrySet())
	{
	    String itemName = entry.getKey();
	    if (!itemName.contains("-->"))
	    {
		dm.addElement(itemName);
	    }
	}
	objFrmBillSettlement.getCmbItemCategory().setModel(dm);
	objFrmBillSettlement.getCmbItemCategory().setEnabled(true);
    }

    /**
     * This method is responsible for to identify applicable items for discount
     * 
     * @param transactionType This is a form name which user is using for billing.eg. Make KOT,Make Bill,Direct Biller,Bill For Items,Add KOT To Bill,etc. 
     * @param discountOn It is one of the from Total,Group,SubGroup or Item.
     * @param tableNo Table no if  transactionType id Make KOT or Make Bill
     * @param type Type will identify whether discount is from Discount Master or Manual Discount.
     * @param discOnCode It is a document code of discountOneg. GroupCode,SubGroupCode,ItemCode.
     * @return list of items which are applicable for this discount
     */
    private List<clsBillItemDtl> funGetDiscountApplicableItemList(String transactionType, String discountOn, String tableNo, String type, String discOnCode)
    {
	List<clsBillItemDtl> listOfDiscApplicableItems = new ArrayList<>();
	StringBuilder sqlBillDtlBuilder = new StringBuilder();
	StringBuilder sqlBillModifierBuilder = new StringBuilder();
	try
	{
	    if (transactionType.equalsIgnoreCase("Make KOT") || transactionType.equalsIgnoreCase("Make Bill"))
	    {
		sqlBillDtlBuilder.setLength(0);
		sqlBillDtlBuilder.append("select a.strItemCode,UPPER(a.strItemName),sum(a.dblAmount),sum(a.dblItemQuantity) "
			+ "from tblitemrtemp a,tblitemmaster b,tblsubgrouphd c,tblgrouphd d "
			+ "where (a.strItemCode=b.strItemCode or LEFT(a.strItemCode,7)=b.strItemCode) "
			+ "and b.strSubGroupCode=c.strSubGroupCode "
			+ "and c.strGroupCode=d.strGroupCode  "
			+ "and a.strTableNo='" + tableNo + "' "
			+ "and a.tdhComboItemYN='N' "
			+ "and a.strNCKotYN='N' "
			+ "and b.strDiscountApply='Y' ");
		if (clsGlobalVarClass.gActivePromotions)
		{
		    if (objFrmBillSettlement.getHmPromoItem()!=null && objFrmBillSettlement.getHmPromoItem().size() > 0)
		    {
			String ignoreForPromoItems = funGetExcludePromoItemsForDiscount();
			if (ignoreForPromoItems.length() > 0)
			{
			    sqlBillDtlBuilder.append(" and LEFT(a.strItemCode,7) not in " + ignoreForPromoItems);
			}
		    }
		}
		if (discountOn.equalsIgnoreCase("Total"))
		{
		    sqlBillDtlBuilder.append(" ");
		}
		if (discountOn.equalsIgnoreCase("Group"))
		{
		    if (type.equalsIgnoreCase("Master"))
		    {
			sqlBillDtlBuilder.append("and c.strGroupCode='" + discOnCode + "' ");
		    }
		    else
		    {
			sqlBillDtlBuilder.append("and c.strGroupCode='" + objFrmBillSettlement.getListGroupCode().get(objFrmBillSettlement.getCmbItemCategory().getSelectedIndex()) + "' ");
		    }
		}
		if (discountOn.equalsIgnoreCase("SubGroup"))
		{
		    if (type.equalsIgnoreCase("Master"))
		    {
			sqlBillDtlBuilder.append("and b.strSubGroupCode='" + discOnCode + "'  ");
		    }
		    else
		    {
			sqlBillDtlBuilder.append("and b.strSubGroupCode='" + objFrmBillSettlement.getListSubGroupCode().get(objFrmBillSettlement.getCmbItemCategory().getSelectedIndex()) + "'  ");
		    }
		}
		if (discountOn.equalsIgnoreCase("Item"))
		{
		    if (type.equalsIgnoreCase("Master"))
		    {
			sqlBillDtlBuilder.append("AND left(a.strItemCode,7)='" + discOnCode + "' ");
		    }
		    else
		    {
			sqlBillDtlBuilder.append("AND left(a.strItemCode,7)='" + objFrmBillSettlement.getHmItemList().get(objFrmBillSettlement.getCmbItemCategory().getSelectedItem().toString()) + "' ");
		    }
		}
		sqlBillDtlBuilder.append("group by a.strItemCode,a.strItemName ");

		ResultSet rsDiscItems = clsGlobalVarClass.dbMysql.executeResultSet(sqlBillDtlBuilder.toString());
		while (rsDiscItems.next())
		{
		    clsBillItemDtl objItemDtl = new clsBillItemDtl();
		    objItemDtl.setItemCode(rsDiscItems.getString(1));
		    objItemDtl.setItemName(rsDiscItems.getString(2));
		    objItemDtl.setQuantity(rsDiscItems.getDouble(4));
		    if (rsDiscItems.getString(1).contains("M"))
		    {
			objItemDtl.setIsModifier(true);
			objItemDtl.setAmount(objFrmBillSettlement.getHmBillItemDtl().get(rsDiscItems.getString(1) + "!" + rsDiscItems.getString(2)).getAmount());
		    }
		    else
		    {
			objItemDtl.setIsModifier(false);
			objItemDtl.setAmount(objFrmBillSettlement.getHmBillItemDtl().get(rsDiscItems.getString(1)).getAmount());
		    }
		    listOfDiscApplicableItems.add(objItemDtl);
		}

	    }
	    if (transactionType.equalsIgnoreCase("Bill For Items"))
	    {
		sqlBillDtlBuilder.setLength(0);
		sqlBillDtlBuilder.append("select a.strItemCode,UPPER(a.strItemName),sum(a.dblAmount),sum(a.dblItemQuantity) "
			+ "from tblitemrtemp a,tblitemmaster b,tblsubgrouphd c,tblgrouphd d "
			+ "where (a.strItemCode=b.strItemCode or LEFT(a.strItemCode,7)=b.strItemCode) "
			+ "and b.strSubGroupCode=c.strSubGroupCode "
			+ "and c.strGroupCode=d.strGroupCode  "
			+ "and a.strTableNo='" + tableNo + "' "
			+ "and a.tdhComboItemYN='N' "
			+ "and a.strNCKotYN='N' "
			+ "and b.strDiscountApply='Y' "
			+ "and a.strItemCode in " + objBillSettlementUtility.funGetItemCodeList() + "  ");
		if (clsGlobalVarClass.gActivePromotions)
		{
		    if (objFrmBillSettlement.getHmPromoItem()!=null && objFrmBillSettlement.getHmPromoItem().size() > 0)
		    {
			String ignoreForPromoItems = funGetExcludePromoItemsForDiscount();
			if (ignoreForPromoItems.length() > 0)
			{
			    sqlBillDtlBuilder.append(" and LEFT(a.strItemCode,7) not in " + ignoreForPromoItems);
			}
		    }
		}
		if (discountOn.equalsIgnoreCase("Total"))
		{
		    sqlBillDtlBuilder.append(" ");
		}
		if (discountOn.equalsIgnoreCase("Group"))
		{
		    if (type.equalsIgnoreCase("Master"))
		    {
			sqlBillDtlBuilder.append("and c.strGroupCode='" + discOnCode + "' ");
		    }
		    else
		    {
			sqlBillDtlBuilder.append("and c.strGroupCode='" + objFrmBillSettlement.getListGroupCode().get(objFrmBillSettlement.getCmbItemCategory().getSelectedIndex()) + "' ");
		    }
		}
		if (discountOn.equalsIgnoreCase("SubGroup"))
		{
		    if (type.equalsIgnoreCase("Master"))
		    {
			sqlBillDtlBuilder.append("and b.strSubGroupCode='" + discOnCode + "'  ");
		    }
		    else
		    {
			sqlBillDtlBuilder.append("and b.strSubGroupCode='" + objFrmBillSettlement.getListSubGroupCode().get(objFrmBillSettlement.getCmbItemCategory().getSelectedIndex()) + "'  ");
		    }
		}
		if (discountOn.equalsIgnoreCase("Item"))
		{
		    if (type.equalsIgnoreCase("Master"))
		    {
			sqlBillDtlBuilder.append("AND left(a.strItemCode,7)='" + discOnCode + "' ");
		    }
		    else
		    {
			sqlBillDtlBuilder.append("AND left(a.strItemCode,7)='" + objFrmBillSettlement.getHmItemList().get(objFrmBillSettlement.getCmbItemCategory().getSelectedItem().toString()) + "' ");
		    }
		}
		sqlBillDtlBuilder.append("group by a.strItemCode,a.strItemName ");

		ResultSet rsDiscItems = clsGlobalVarClass.dbMysql.executeResultSet(sqlBillDtlBuilder.toString());
		while (rsDiscItems.next())
		{
		    clsBillItemDtl objItemDtl = new clsBillItemDtl();
		    objItemDtl.setItemCode(rsDiscItems.getString(1));
		    objItemDtl.setItemName(rsDiscItems.getString(2));
		    
		    if (rsDiscItems.getString(1).contains("M"))
		    {
			objItemDtl.setIsModifier(true);
			objItemDtl.setQuantity(objFrmBillSettlement.getHmBillItemDtl().get(rsDiscItems.getString(1) + "!" + rsDiscItems.getString(2)).getQuantity());
			objItemDtl.setAmount(objFrmBillSettlement.getHmBillItemDtl().get(rsDiscItems.getString(1) + "!" + rsDiscItems.getString(2)).getAmount());
		    }
		    else
		    {
			objItemDtl.setIsModifier(false);
			objItemDtl.setQuantity(objFrmBillSettlement.getHmBillItemDtl().get(rsDiscItems.getString(1)).getQuantity());
			objItemDtl.setAmount(objFrmBillSettlement.getHmBillItemDtl().get(rsDiscItems.getString(1)).getAmount());
		    }
		    listOfDiscApplicableItems.add(objItemDtl);
		}

	    }
	    else if (transactionType.equalsIgnoreCase("Direct Biller"))
	    {
		for (clsDirectBillerItemDtl objDirectBill : objFrmBillSettlement.getObjListDirectBillerItemDtl())
		{
		    sqlBillDtlBuilder.setLength(0);
		    sqlBillDtlBuilder.append("select a.strItemCode,a.strItemName "
			    + " from tblitemmaster a,tblsubgrouphd b,tblgrouphd c "
			    + " where a.strSubGroupCode=b.strSubGroupCode "
			    + " and b.strGroupCode=c.strGroupCode "
			    + " and a.strDiscountApply='Y' ");
		    if (clsGlobalVarClass.gActivePromotions)
		    {
			if (objFrmBillSettlement.getHmPromoItem()!=null && objFrmBillSettlement.getHmPromoItem().size() > 0)
			{
			    String ignoreForPromoItems = objFrmBillSettlement.getObjBillSettlementUtility().funGetExcludePromoItemsForDiscount();
			    if (ignoreForPromoItems.length() > 0)
			    {
				sqlBillDtlBuilder.append(" and LEFT(a.strItemCode,7) not in " + ignoreForPromoItems);
			    }
			}
		    }

		    if (discountOn.equalsIgnoreCase("Total"))
		    {
			sqlBillDtlBuilder.append(" and a.strItemCode='" + objDirectBill.getItemCode().substring(0, 7) + "' ");
		    }
		    if (discountOn.equalsIgnoreCase("Group"))
		    {
			if (type.equalsIgnoreCase("Master"))
			{
			    sqlBillDtlBuilder.append(" and a.strItemCode='" + objDirectBill.getItemCode().substring(0, 7) + "' ");
			    sqlBillDtlBuilder.append(" and c.strGroupCode='" + discOnCode + "' ");
			}
			else
			{
			    sqlBillDtlBuilder.append(" and a.strItemCode='" + objDirectBill.getItemCode().substring(0, 7) + "' ");
			    sqlBillDtlBuilder.append(" and c.strGroupCode='" + objFrmBillSettlement.getListGroupCode().get(objFrmBillSettlement.getCmbItemCategory().getSelectedIndex()) + "' ");
			}
		    }
		    if (discountOn.equalsIgnoreCase("SubGroup"))
		    {
			if (type.equalsIgnoreCase("Master"))
			{
			    sqlBillDtlBuilder.append(" and a.strItemCode='" + objDirectBill.getItemCode().substring(0, 7) + "' ");
			    sqlBillDtlBuilder.append(" and b.strSubGroupCode='" + discOnCode + "' ");
			}
			else
			{
			    sqlBillDtlBuilder.append(" and a.strItemCode='" + objDirectBill.getItemCode().substring(0, 7) + "' ");
			    sqlBillDtlBuilder.append(" and b.strSubGroupCode='" + objFrmBillSettlement.getListSubGroupCode().get(objFrmBillSettlement.getCmbItemCategory().getSelectedIndex()) + "' ");
			}
		    }
		    if (discountOn.equalsIgnoreCase("Item"))
		    {
			if (type.equalsIgnoreCase("Master"))
			{
			    sqlBillDtlBuilder.append(" and a.strItemCode='" + discOnCode + "' ");
			    sqlBillDtlBuilder.append(" and a.strItemCode='" + objDirectBill.getItemCode().substring(0, 7) + "' ");
			}
			else
			{
			    sqlBillDtlBuilder.append(" and a.strItemCode='" + objFrmBillSettlement.getHmItemList().get(objFrmBillSettlement.getCmbItemCategory().getSelectedItem().toString()) + "' ");
			    sqlBillDtlBuilder.append(" and a.strItemCode='" + objDirectBill.getItemCode().substring(0, 7) + "' ");
			}
		    }
		    sqlBillDtlBuilder.append("group by a.strItemCode,a.strItemName ");

		    ResultSet rsDiscItems = clsGlobalVarClass.dbMysql.executeResultSet(sqlBillDtlBuilder.toString());
		    while (rsDiscItems.next())
		    {
			clsBillItemDtl objItemDtl = new clsBillItemDtl();
			objItemDtl.setItemCode(objDirectBill.getItemCode());
			objItemDtl.setItemName(objDirectBill.getItemName());
			objItemDtl.setAmount(objDirectBill.getAmt());
			objItemDtl.setQuantity(objDirectBill.getQty());
			if (objDirectBill.getItemCode().contains("M"))
			{
			    objItemDtl.setIsModifier(true);
			}
			else
			{
			    objItemDtl.setIsModifier(false);
			}
			listOfDiscApplicableItems.add(objItemDtl);
		    }
		}
	    }
	    else if (transactionType.equalsIgnoreCase("Advance Order"))
	    {
		for (clsDirectBillerItemDtl objDirectBill : objFrmBillSettlement.getObjListDirectBillerItemDtl())
		{
		    sqlBillDtlBuilder.setLength(0);
		    sqlBillDtlBuilder.append("select a.strItemCode,a.strItemName "
			    + " from tblitemmaster a,tblsubgrouphd b,tblgrouphd c "
			    + " where a.strSubGroupCode=b.strSubGroupCode "
			    + " and b.strGroupCode=c.strGroupCode "
			    + " and a.strDiscountApply='Y' ");
		    if (discountOn.equalsIgnoreCase("Total"))
		    {
			sqlBillDtlBuilder.append(" and a.strItemCode='" + objDirectBill.getItemCode().substring(0, 7) + "' ");
		    }
		    if (discountOn.equalsIgnoreCase("Group"))
		    {
			if (type.equalsIgnoreCase("Master"))
			{
			    sqlBillDtlBuilder.append(" and a.strItemCode='" + objDirectBill.getItemCode().substring(0, 7) + "' ");
			    sqlBillDtlBuilder.append(" and c.strGroupCode='" + discOnCode + "' ");
			}
			else
			{
			    sqlBillDtlBuilder.append(" and a.strItemCode='" + objDirectBill.getItemCode().substring(0, 7) + "' ");
			    sqlBillDtlBuilder.append(" and c.strGroupCode='" + objFrmBillSettlement.getListGroupCode().get(objFrmBillSettlement.getCmbItemCategory().getSelectedIndex()) + "' ");
			}
		    }
		    if (discountOn.equalsIgnoreCase("SubGroup"))
		    {
			if (type.equalsIgnoreCase("Master"))
			{
			    sqlBillDtlBuilder.append(" and a.strItemCode='" + objDirectBill.getItemCode().substring(0, 7) + "' ");
			    sqlBillDtlBuilder.append(" and b.strSubGroupCode='" + discOnCode + "' ");
			}
			else
			{
			    sqlBillDtlBuilder.append(" and a.strItemCode='" + objDirectBill.getItemCode().substring(0, 7) + "' ");
			    sqlBillDtlBuilder.append(" and b.strSubGroupCode='" + objFrmBillSettlement.getListSubGroupCode().get(objFrmBillSettlement.getCmbItemCategory().getSelectedIndex()) + "' ");
			}
		    }
		    if (discountOn.equalsIgnoreCase("Item"))
		    {
			if (type.equalsIgnoreCase("Master"))
			{
			    sqlBillDtlBuilder.append(" and a.strItemCode='" + discOnCode + "' ");
			    sqlBillDtlBuilder.append(" and a.strItemCode='" + objDirectBill.getItemCode().substring(0, 7) + "' ");
			}
			else
			{
			    sqlBillDtlBuilder.append(" and a.strItemCode='" + objFrmBillSettlement.getHmItemList().get(objFrmBillSettlement.getCmbItemCategory().getSelectedItem().toString()) + "' ");
			    sqlBillDtlBuilder.append(" and a.strItemCode='" + objDirectBill.getItemCode().substring(0, 7) + "' ");
			}
		    }
		    sqlBillDtlBuilder.append("group by a.strItemCode,a.strItemName ");

		    ResultSet rsDiscItems = clsGlobalVarClass.dbMysql.executeResultSet(sqlBillDtlBuilder.toString());
		    while (rsDiscItems.next())
		    {
			clsBillItemDtl objItemDtl = new clsBillItemDtl();
			objItemDtl.setItemCode(objDirectBill.getItemCode());
			objItemDtl.setItemName(objDirectBill.getItemName());
			objItemDtl.setAmount(objDirectBill.getAmt());
			objItemDtl.setQuantity(objDirectBill.getQty());
			if (objDirectBill.getItemCode().contains("M"))
			{
			    objItemDtl.setIsModifier(true);
			}
			else
			{
			    objItemDtl.setIsModifier(false);
			}
			listOfDiscApplicableItems.add(objItemDtl);
		    }
		}
	    }
	    else if (transactionType.equalsIgnoreCase("ModifyBill"))
	    {
		sqlBillDtlBuilder.setLength(0);
		sqlBillDtlBuilder.append("select a.strItemCode,UPPER(a.strItemName),sum(a.dblAmount),sum(a.dblQuantity),"
			+ "b.strSubGroupCode,c.strSubGroupName,c.strGroupCode,d.strGroupName "
			+ "from tblbilldtl a,tblitemmaster b,tblsubgrouphd c,tblgrouphd d "
			+ "where a.strBillNo='" + objFrmBillSettlement.getVoucherNo() + "' "
			+ " and date(a.dteBillDate)='" + clsGlobalVarClass.getOnlyPOSDateForTransaction() + "' "
			+ "and a.tdhYN='N' "
			+ "and a.strItemCode=b.strItemCode "
			+ "and b.strSubGroupCode=c.strSubGroupCode "
			+ "and c.strGroupCode=d.strGroupCode "
			+ "and b.strDiscountApply='Y' ");
		if (clsGlobalVarClass.gActivePromotions)
		{
		    if (objFrmBillSettlement.getHmPromoItem() != null && objFrmBillSettlement.getHmPromoItem().size() > 0)
		    {
			String ignoreForPromoItems = funGetExcludePromoItemsForDiscount();
			if (ignoreForPromoItems.length() > 0)
			{
			    sqlBillDtlBuilder.append(" and LEFT(a.strItemCode,7) not in " + ignoreForPromoItems);
			}
		    }
		}
		if (discountOn.equalsIgnoreCase("Total"))
		{
		    sqlBillDtlBuilder.append(" ");
		}
		if (discountOn.equalsIgnoreCase("Group"))
		{
		    if (type.equalsIgnoreCase("Master"))
		    {
			sqlBillDtlBuilder.append(" and c.strGroupCode='" + discOnCode + "' ");
		    }
		    else
		    {
			sqlBillDtlBuilder.append(" and c.strGroupCode='" + objFrmBillSettlement.getListGroupCode().get(objFrmBillSettlement.getCmbItemCategory().getSelectedIndex()) + "' ");
		    }
		}
		if (discountOn.equalsIgnoreCase("SubGroup"))
		{
		    if (type.equalsIgnoreCase("Master"))
		    {
			sqlBillDtlBuilder.append(" and b.strSubGroupCode='" + discOnCode + "' ");
		    }
		    else
		    {
			sqlBillDtlBuilder.append(" and b.strSubGroupCode='" + objFrmBillSettlement.getListSubGroupCode().get(objFrmBillSettlement.getCmbItemCategory().getSelectedIndex()) + "' ");
		    }
		}
		if (discountOn.equalsIgnoreCase("Item"))
		{
		    if (type.equalsIgnoreCase("Master"))
		    {
			sqlBillDtlBuilder.append(" and a.strItemCode='" + discOnCode + "' ");
		    }
		    else
		    {
			sqlBillDtlBuilder.append(" and a.strItemCode='" + objFrmBillSettlement.getHmItemList().get(objFrmBillSettlement.getCmbItemCategory().getSelectedItem().toString()) + "' ");
		    }
		}
		sqlBillDtlBuilder.append("group by a.strItemCode,a.strItemName ");

		ResultSet rsDiscItems = clsGlobalVarClass.dbMysql.executeResultSet(sqlBillDtlBuilder.toString());
		while (rsDiscItems.next())
		{
		    clsBillItemDtl objItemDtl = new clsBillItemDtl();
		    objItemDtl.setItemCode(rsDiscItems.getString(1));
		    objItemDtl.setItemName(rsDiscItems.getString(2));
		    objItemDtl.setAmount(objFrmBillSettlement.getHmBillItemDtl().get(rsDiscItems.getString(1)).getAmount());
		    //objItemDtl.setAmount(rsDiscItems.getDouble(3));
		    objItemDtl.setQuantity(rsDiscItems.getDouble(4));
		    objItemDtl.setIsModifier(false);

		    listOfDiscApplicableItems.add(objItemDtl);//added bill items

		    //add modifiers item
		    sqlBillModifierBuilder.setLength(0);
		    sqlBillModifierBuilder.append("SELECT strItemCode,strModifierName,sum(dblAmount),sum(dblQuantity) "
			    + "FROM tblbillmodifierdtl "
			    + "WHERE strBillNo='" + objFrmBillSettlement.getVoucherNo() + "' "
			    + "and date(dteBillDate)='" + clsGlobalVarClass.getOnlyPOSDateForTransaction() + "' "
			    + "AND LEFT(strItemCode,7)='" + rsDiscItems.getString(1) + "' ");
		    if (clsGlobalVarClass.gActivePromotions)
		    {
			if (objFrmBillSettlement.getHmPromoItem() != null && objFrmBillSettlement.getHmPromoItem().size() > 0)
			{
			    String ignoreForPromoItems = funGetExcludePromoItemsForDiscount();
			    if (ignoreForPromoItems.length() > 0)
			    {
				sqlBillModifierBuilder.append(" and LEFT(a.strItemCode,7) not in " + ignoreForPromoItems);
			    }
			}
		    }
		    sqlBillModifierBuilder.append(" group by strItemCode,strModifierName ");

		    ResultSet rsModiDiscItems = clsGlobalVarClass.dbMysql.executeResultSet(sqlBillModifierBuilder.toString());
		    while (rsModiDiscItems.next())
		    {
			clsBillItemDtl objModiItemDtl = new clsBillItemDtl();
			objModiItemDtl.setItemCode(rsModiDiscItems.getString(1));
			objModiItemDtl.setItemName(rsModiDiscItems.getString(2));

			String key = rsModiDiscItems.getString(1) + "!" + rsModiDiscItems.getString(2);

			objModiItemDtl.setAmount(objFrmBillSettlement.getHmBillItemDtl().get(key.toUpperCase()).getAmount());
			objModiItemDtl.setQuantity(rsModiDiscItems.getDouble(4));
			objModiItemDtl.setIsModifier(true);

			listOfDiscApplicableItems.add(objModiItemDtl);//added bill modifiers items
		    }
		}
	    }
	    else if (transactionType.equalsIgnoreCase("Bill From KOT"))
	    {
		String kots = "";
		for (int i = 0; i < objFrmBillSettlement.getListBillFromKOT().size(); i++)
		{
		    if (i == 0)
		    {
			kots = "('" + objFrmBillSettlement.getListBillFromKOT().get(i) + "' ";
		    }
		    else
		    {
			kots = kots + ",'" + objFrmBillSettlement.getListBillFromKOT().get(i) + "' ";
		    }
		}
		kots = kots + ")";

		sqlBillDtlBuilder.setLength(0);
		sqlBillDtlBuilder.append("select a.strItemCode,UPPER(a.strItemName),sum(a.dblAmount),sum(a.dblItemQuantity) "
			+ " from tblitemrtemp a,tblitemmaster b,tblsubgrouphd c,tblgrouphd d "
			+ " where  (a.strItemCode=b.strItemCode or LEFT(a.strItemCode,7)=b.strItemCode) and b.strSubGroupCode=c.strSubGroupCode "
			+ " and c.strGroupCode=d.strGroupCode "
			+ " and a.strKOTNo in " + kots + " "
			+ " and a.tdhComboItemYN='N' "
			+ " and a.strNCKotYN='N' "
			+ " and b.strDiscountApply='Y' ");
		if (clsGlobalVarClass.gActivePromotions)
		{
		    if (objFrmBillSettlement.getHmPromoItem() != null && objFrmBillSettlement.getHmPromoItem().size() > 0)
		    {
			String ignoreForPromoItems = funGetExcludePromoItemsForDiscount();
			if (ignoreForPromoItems.length() > 0)
			{
			    sqlBillDtlBuilder.append(" and LEFT(a.strItemCode,7) not in " + ignoreForPromoItems);
			}
		    }
		}

		if (discountOn.equalsIgnoreCase("Total"))
		{
		    sqlBillDtlBuilder.append(" ");
		}
		if (discountOn.equalsIgnoreCase("Group"))
		{
		    if (type.equalsIgnoreCase("Master"))
		    {
			sqlBillDtlBuilder.append(" and c.strGroupCode='" + discOnCode + "' ");
		    }
		    else
		    {
			sqlBillDtlBuilder.append(" and c.strGroupCode='" + objFrmBillSettlement.getListGroupCode().get(objFrmBillSettlement.getCmbItemCategory().getSelectedIndex()) + "' ");
		    }
		}
		if (discountOn.equalsIgnoreCase("SubGroup"))
		{
		    if (type.equalsIgnoreCase("Master"))
		    {
			sqlBillDtlBuilder.append(" and b.strSubGroupCode='" + discOnCode + "' ");
		    }
		    else
		    {
			sqlBillDtlBuilder.append(" and b.strSubGroupCode='" + objFrmBillSettlement.getListSubGroupCode().get(objFrmBillSettlement.getCmbItemCategory().getSelectedIndex()) + "' ");
		    }
		}
		if (discountOn.equalsIgnoreCase("Item"))
		{
		    if (type.equalsIgnoreCase("Master"))
		    {
			sqlBillDtlBuilder.append(" and left(a.strItemCode,7)='" + discOnCode + "' ");
		    }
		    else
		    {
			sqlBillDtlBuilder.append(" and left(a.strItemCode,7)='" + objFrmBillSettlement.getHmItemList().get(objFrmBillSettlement.getCmbItemCategory().getSelectedItem().toString()) + "' ");
		    }
		}
		sqlBillDtlBuilder.append(" group by a.strItemCode,a.strItemName ");
		ResultSet rsDiscItems = clsGlobalVarClass.dbMysql.executeResultSet(sqlBillDtlBuilder.toString());
		while (rsDiscItems.next())
		{
		    clsBillItemDtl objItemDtl = new clsBillItemDtl();
		    objItemDtl.setItemCode(rsDiscItems.getString(1));
		    objItemDtl.setItemName(rsDiscItems.getString(2));
		    objItemDtl.setQuantity(rsDiscItems.getDouble(4));
		    if (rsDiscItems.getString(1).contains("M"))
		    {
			objItemDtl.setIsModifier(true);
			String key = rsDiscItems.getString(1) + "!" + rsDiscItems.getString(2);
			objItemDtl.setAmount(objFrmBillSettlement.getHmBillItemDtl().get(key.toUpperCase()).getAmount());
		    }
		    else
		    {
			objItemDtl.setIsModifier(false);
			String key = rsDiscItems.getString(1);
			objItemDtl.setAmount(objFrmBillSettlement.getHmBillItemDtl().get(key.toUpperCase()).getAmount());
		    }
		    listOfDiscApplicableItems.add(objItemDtl);
		}
	    }
	    else if (clsGlobalVarClass.gTransactionType.equalsIgnoreCase("AddKOTToBill"))
	    {

		String sqlAppendForBillFromKOTS = "";
		List<String> listKOTNos = objFrmBillSettlement.getObjAddKOTToBill().getList_Selected_KOTs();
		if (!listKOTNos.isEmpty())
		{
		    boolean first = true;
		    for (String kot : listKOTNos)
		    {
			if (first)
			{
			    sqlAppendForBillFromKOTS += "( strKOTNo='" + kot + "'";
			    first = false;
			}
			else
			{
			    sqlAppendForBillFromKOTS += " or ".concat(" strKOTNo='" + kot + "' ");
			}
		    }
		}
		sqlAppendForBillFromKOTS += " )";
		//add kot details
		sqlBillDtlBuilder.setLength(0);
		sqlBillDtlBuilder.append("select a.strItemCode,UPPER(a.strItemName),sum(a.dblAmount) "
			+ " from tblitemrtemp a,tblitemmaster b,tblsubgrouphd c "
			+ " where (a.strItemCode=b.strItemCode or LEFT(a.strItemCode,7)=b.strItemCode) "
			+ " and b.strSubGroupCode=c.strSubGroupCode "
			+ " and " + sqlAppendForBillFromKOTS + " "
			+ " and a.tdhComboItemYN='N' "
			+ " and a.strNCKotYN='N' "
			+ " and b.strDiscountApply='Y' ");
		if (clsGlobalVarClass.gActivePromotions)
		{
		    if (objFrmBillSettlement.getHmPromoItem() != null && objFrmBillSettlement.getHmPromoItem().size() > 0)
		    {
			String ignoreForPromoItems = funGetExcludePromoItemsForDiscount();
			if (ignoreForPromoItems.length() > 0)
			{
			    sqlBillDtlBuilder.append(" and LEFT(a.strItemCode,7) not in " + ignoreForPromoItems);
			}
		    }
		}

		if (discountOn.equalsIgnoreCase("Total"))
		{
		    sqlBillDtlBuilder.append(" ");
		}
		if (discountOn.equalsIgnoreCase("Group"))
		{
		    if (type.equalsIgnoreCase("Master"))
		    {
			sqlBillDtlBuilder.append(" and c.strGroupCode='" + discOnCode + "' ");
		    }
		    else
		    {
			sqlBillDtlBuilder.append(" and c.strGroupCode='" + objFrmBillSettlement.getListGroupCode().get(objFrmBillSettlement.getCmbItemCategory().getSelectedIndex()) + "' ");
		    }
		}
		if (discountOn.equalsIgnoreCase("SubGroup"))
		{
		    if (type.equalsIgnoreCase("Master"))
		    {
			sqlBillDtlBuilder.append(" and b.strSubGroupCode='" + discOnCode + "' ");
		    }
		    else
		    {
			sqlBillDtlBuilder.append(" and b.strSubGroupCode='" + objFrmBillSettlement.getListSubGroupCode().get(objFrmBillSettlement.getCmbItemCategory().getSelectedIndex()) + "' ");
		    }
		}
		if (discountOn.equalsIgnoreCase("Item"))
		{
		    if (type.equalsIgnoreCase("Master"))
		    {
			sqlBillDtlBuilder.append(" and left(a.strItemCode,7)='" + discOnCode + "' ");
		    }
		    else
		    {
			sqlBillDtlBuilder.append(" and left(a.strItemCode,7)='" + objFrmBillSettlement.getHmItemList().get(objFrmBillSettlement.getCmbItemCategory().getSelectedItem().toString()) + "' ");
		    }
		}
		sqlBillDtlBuilder.append(" group by a.strItemCode,a.strItemName ");
		ResultSet rsDiscItems = clsGlobalVarClass.dbMysql.executeResultSet(sqlBillDtlBuilder.toString());
		while (rsDiscItems.next())
		{
		    clsBillItemDtl objItemDtl = new clsBillItemDtl();
		    objItemDtl.setItemCode(rsDiscItems.getString(1));
		    objItemDtl.setItemName(rsDiscItems.getString(2));
		    //objItemDtl.setAmount(rsDiscItems.getDouble(3));
		    boolean isItemExists = false;
		    if (listOfDiscApplicableItems != null && listOfDiscApplicableItems.size() > 0)
		    {
			for (clsBillItemDtl objTempItemDtl : listOfDiscApplicableItems)
			{
			    if (objTempItemDtl.getItemCode().equals(rsDiscItems.getString(1)) && objTempItemDtl.getItemName().equals(rsDiscItems.getString(2)))//itemcCode
			    {
				isItemExists = true;
				break;
			    }
			}
		    }
		    if (isItemExists)
		    {

		    }
		    else
		    {
			if (rsDiscItems.getString(1).contains("M"))
			{
			    objItemDtl.setIsModifier(true);
			    String key = rsDiscItems.getString(1) + "!" + rsDiscItems.getString(2);
			    objItemDtl.setAmount(objFrmBillSettlement.getHmBillItemDtl().get(key.toUpperCase()).getAmount());
			}
			else
			{
			    objItemDtl.setIsModifier(false);
			    String key = rsDiscItems.getString(1).toUpperCase();
			    objItemDtl.setAmount(objFrmBillSettlement.getHmBillItemDtl().get(key).getAmount());
			}

			listOfDiscApplicableItems.add(objItemDtl);
		    }
		}

		//add bill details
		sqlBillDtlBuilder.setLength(0);
		sqlBillDtlBuilder.append("select a.strItemCode,UPPER(a.strItemName),sum(a.dblAmount)  "
			+ "from tblbilldtl a "
			+ "left outer join tblitemmaster b on a.strItemCode=b.strItemCode "
			+ "left outer join tblsubgrouphd c on b.strSubGroupCode=c.strSubGroupCode "
			+ "left outer join tblgrouphd d on c.strGroupCode=d.strGroupCode "
			+ "where b.strDiscountApply='Y' "
			+ "and a.strBillNo='" + objFrmBillSettlement.getVoucherNo() + "'  "
			+ "and date(a.dteBillDate)='" + clsGlobalVarClass.getOnlyPOSDateForTransaction() + "' ");
		if (clsGlobalVarClass.gActivePromotions)
		{
		    if (objFrmBillSettlement.getHmPromoItem() != null && objFrmBillSettlement.getHmPromoItem().size() > 0)
		    {
			String ignoreForPromoItems = funGetExcludePromoItemsForDiscount();
			if (ignoreForPromoItems.length() > 0)
			{
			    sqlBillDtlBuilder.append(" and LEFT(a.strItemCode,7) not in " + ignoreForPromoItems);
			}
		    }
		}

		if (discountOn.equalsIgnoreCase("Total"))
		{
		    sqlBillDtlBuilder.append(" ");
		}
		if (discountOn.equalsIgnoreCase("Group"))
		{
		    if (type.equalsIgnoreCase("Master"))
		    {
			sqlBillDtlBuilder.append(" and d.strGroupCode='" + discOnCode + "' ");
		    }
		    else
		    {
			sqlBillDtlBuilder.append(" and d.strGroupCode='" + objFrmBillSettlement.getListGroupCode().get(objFrmBillSettlement.getCmbItemCategory().getSelectedIndex()) + "' ");
		    }
		}
		if (discountOn.equalsIgnoreCase("SubGroup"))
		{
		    if (type.equalsIgnoreCase("Master"))
		    {
			sqlBillDtlBuilder.append(" and c.strSubGroupCode='" + discOnCode + "'  ");
		    }
		    else
		    {
			sqlBillDtlBuilder.append(" and c.strSubGroupCode='" + objFrmBillSettlement.getListSubGroupCode().get(objFrmBillSettlement.getCmbItemCategory().getSelectedIndex()) + "'  ");
		    }
		}
		if (discountOn.equalsIgnoreCase("Item"))
		{
		    if (type.equalsIgnoreCase("Master"))
		    {
			sqlBillDtlBuilder.append(" and a.strItemCode='" + discOnCode + "' ");
		    }
		    else
		    {
			sqlBillDtlBuilder.append(" and a.strItemCode='" + objFrmBillSettlement.getHmItemList().get(objFrmBillSettlement.getCmbItemCategory().getSelectedItem().toString()) + "' ");
		    }
		}

		sqlBillDtlBuilder.append(" group by a.strItemCode,a.strItemName ");
		rsDiscItems = clsGlobalVarClass.dbMysql.executeResultSet(sqlBillDtlBuilder.toString());
		while (rsDiscItems.next())
		{
		    clsBillItemDtl objItemDtl = new clsBillItemDtl();
		    objItemDtl.setItemCode(rsDiscItems.getString(1));
		    objItemDtl.setItemName(rsDiscItems.getString(2));
		    objItemDtl.setIsModifier(false);
		    //objItemDtl.setAmount(rsDiscItems.getDouble(3));
		    boolean isItemExists = false;
		    if (listOfDiscApplicableItems != null && listOfDiscApplicableItems.size() > 0)
		    {
			for (clsBillItemDtl objTempItemDtl : listOfDiscApplicableItems)
			{
			    if (objTempItemDtl.getItemCode().equals(rsDiscItems.getString(1)))//itemcCode
			    {
				isItemExists = true;
				break;
			    }
			}
		    }
		    if (isItemExists)
		    {

		    }
		    else
		    {
			String key = rsDiscItems.getString(1).toUpperCase();
			objItemDtl.setAmount(objFrmBillSettlement.getHmBillItemDtl().get(key).getAmount());
			listOfDiscApplicableItems.add(objItemDtl);
		    }

		    //add modifiers item
		    sqlBillModifierBuilder.setLength(0);
		    sqlBillModifierBuilder.append("SELECT strItemCode,strModifierName,sum(dblAmount),sum(dblQuantity) "
			    + "FROM tblbillmodifierdtl "
			    + "WHERE strBillNo='" + objFrmBillSettlement.getVoucherNo() + "' "
			    + "and date(dteBillDate)='" + clsGlobalVarClass.getOnlyPOSDateForTransaction() + "' "
			    + "AND LEFT(strItemCode,7)='" + rsDiscItems.getString(1) + "' ");
		    if (clsGlobalVarClass.gActivePromotions)
		    {
			if (objFrmBillSettlement.getHmPromoItem() != null && objFrmBillSettlement.getHmPromoItem().size() > 0)
			{
			    String ignoreForPromoItems = funGetExcludePromoItemsForDiscount();
			    if (ignoreForPromoItems.length() > 0)
			    {
				sqlBillModifierBuilder.append(" and LEFT(a.strItemCode,7) not in " + ignoreForPromoItems);
			    }
			}
		    }
		    sqlBillModifierBuilder.append("group by strItemCode,strModifierName ");

		    ResultSet rsModiDiscItems = clsGlobalVarClass.dbMysql.executeResultSet(sqlBillModifierBuilder.toString());
		    while (rsModiDiscItems.next())
		    {
			clsBillItemDtl objModiItemDtl = new clsBillItemDtl();
			objModiItemDtl.setItemCode(rsModiDiscItems.getString(1));
			objModiItemDtl.setItemName(rsModiDiscItems.getString(2));
			objModiItemDtl.setQuantity(rsModiDiscItems.getDouble(4));
			objModiItemDtl.setIsModifier(true);
			//objModiItemDtl.setAmount(rsModiDiscItems.getDouble(3));
			isItemExists = false;
			if (listOfDiscApplicableItems != null && listOfDiscApplicableItems.size() > 0)
			{
			    for (clsBillItemDtl objTempItemDtl : listOfDiscApplicableItems)
			    {
				if (objTempItemDtl.getItemCode().equals(rsModiDiscItems.getString(1)) && objTempItemDtl.getItemName().equals(rsModiDiscItems.getString(2)))//itemcCode
				{
				    isItemExists = true;
				    break;
				}
			    }
			}
			if (isItemExists)
			{

			}
			else
			{
			    String key = rsModiDiscItems.getString(1) + "!" + rsModiDiscItems.getString(2);
			    objModiItemDtl.setAmount(objFrmBillSettlement.getHmBillItemDtl().get(key.toUpperCase()).getAmount());
			    listOfDiscApplicableItems.add(objModiItemDtl);
			}
		    }
		}
	    }
	}
	catch (Exception e)
	{
	    objUtility.funWriteErrorLog(e);
	    JOptionPane.showMessageDialog(objFrmBillSettlement, e.getMessage(), "Error Code: BS-79", JOptionPane.ERROR_MESSAGE);
	    e.printStackTrace();
	}
	finally
	{
	    return listOfDiscApplicableItems;
	}
    }

}
