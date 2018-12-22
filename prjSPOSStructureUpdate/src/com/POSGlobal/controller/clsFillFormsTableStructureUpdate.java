/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSGlobal.controller;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Ajim
 */
public class clsFillFormsTableStructureUpdate
{

    private Map<String, List<String>> mapStructureUpdater;

    public clsFillFormsTableStructureUpdate(Map<String, List<String>> mapStructureUpdater)
    {
	this.mapStructureUpdater = mapStructureUpdater;
    }

    public void funFillFormsTableStructureUpdate()
    {
	try
	{
	    String sql = "DROP TABLE IF EXISTS `tblforms`;";
	    mapStructureUpdater.get("frmStructure").add(sql);

	    sql = "CREATE TABLE IF NOT EXISTS `tblforms` ("
		    + "  `strFormName` varchar(50) NOT NULL,"
		    + "  `strModuleName` varchar(50) NOT NULL,"
		    + "  `strModuleType` varchar(2) NOT NULL,"
		    + "  `strImageName` varchar(100) NOT NULL,"
		    + "  `intSequence` int(11) NOT NULL,"
		    + "  `strColorImageName` varchar(100) NOT NULL,"
		    + "  `strRequestMapping` varchar(100) NOT NULL"
		    + ") ;";
	    mapStructureUpdater.get("frmStructure").add(sql);

	    sql = "DELETE FROM `tblforms`;";
	    mapStructureUpdater.get("frmStructure").add(sql);

	    //M for Masters
	    //T for Transactions
	    //R for Reports
	    //U for Utility
	    //AT for Authentication For Transaction 
	    //AM for Authentication For Masters 
	    //AR for Authentication For Reports 
	    //AU for Authentication For Utility                        
	    if (clsGlobalVarClass.gPOSVerion.equalsIgnoreCase("Lite"))
	    {
		sql = "INSERT INTO `tblforms` (`strFormName`, `strModuleName`, `strModuleType`, `strImageName`, `intSequence`, `strColorImageName`,`strRequestMapping`) VALUES "
			//+ "	('frmAddKOTToBill', 'Add KOT To Bill', 'T', 'imgKOTToBill', 85, 'imgKOTToBill1','frmPOSAddKOTToBill.html'), "
			+ "	('frmAdvanceBooking', 'Advance Order', 'T', 'imgAdvanceOrder', 24, 'imgAdvanceOrder1','frmPOSAdvanceBooking.html'), "
			+ "	('frmAdvanceOrderFlash', 'Advance Order Flash', 'R', 'imgAdvanceOrderFlash', 47, 'imgAdvanceOrderFlash1','frmPOSAdvanceOrderFlash.html'), "
			+ "	('frmAdvanceOrderTypeMaster', 'Advance Order Type Master', 'M', 'imgAdvanceOrderTypeMaster', 86, 'imgAdvanceOrderTypeMaster','frmPOSAdvanceOrderTypeMaster.html'), "
			+ "	('frmAIPB', 'AvgItemPerBill', 'R', 'imgAvgItemsPerBill', 63, 'imgAvgItemsPerBill1','frmPOSAIPB.html'), "
			+ "	('frmAPC', 'AvgPerCover', 'R', 'imgAveragePC', 64, 'imgAveragePC1','frmPOSAPC.html'), "
			+ "	('frmAreaMaster', 'Area Master', 'M', 'imgAreaMaster', 54, 'imgAreaMaster1','frmPOSAreaMaster.html'), "
			//+ "	('frmAssignHomeDelivery', 'AssignHomeDelivery', 'T', 'imgAsignHomeDelivery', 99, 'imgAsignHomeDelivery1','frmPOSAssignHomeDelivery.html'), "
			+ "	('frmATV', 'AvgTicketValue', 'R', 'imgAvgValue', 65, 'imgAvgValue1','frmPOSATV.html'), "
			+ "	('frmAuditFlash', 'Audit Flash', 'R', 'imgAuditFlash', 46, 'imgAuditFlash1','frmPOSAuditFlash.html'), "
			+ "	('frmAuditorReport', 'Auditor Report', 'R', 'imgAuditorReport', 97, 'imgAuditorReport','frmPOSAuditorReport.html'), "
			+ "	('frmAvdBookReceipt', 'Advance Booking Receipt', 'T', 'imgAdvanceBookingReceipt', 31, 'imgAdvanceBookingReceipt1','frmPOSAvdBookReceipt.html'), "
			//+ "	('frmBillFromKOTs', 'BillFromKOTs', 'T', 'imgMakeBillFromKOT', 76, 'imgMakeBillFromKOT1','frmPOSBillFromKOTs.html'), "
			+ "	('frmBillReport', 'Bill Wise Report', 'R', 'imgBillWiseReport', 9, 'imgBillWiseReport','frmPOSBillReport.html'), "
			//+ "	('frmBulkMenuItemPricing', 'Bulk Menu Item Pricing', 'M', 'imgBulkMenuItemPricing', 88, 'imgBulkMenuItemPricing1','frmPOSBulkMenuItemPricing.html'), "
			//+ "	('frmCashManagement', 'Cash Management', 'T', 'imgCashManagement', 43, 'imgCashManagement1','frmPOSCashManagement.html'), "
			//+ "	('frmCashMgmtReport', 'Cash Mgmt Report', 'R', 'imgCashMgmtFlashReport', 44, 'imgCashMgmtFlashReport1','frmPOSCashMgmtReport.html'), "
			//+ "	('frmCloseProductionOrder', 'Close Producion Order', 'T', 'imgProductionOrder', 84, 'imgProductionOrder1','frmPOSCloseProductionOrder.html'), "
			+ "	('frmComplimentarySettlement', 'Complimentary Settlement Report', 'R', 'imgComplimentarySettlementReport', 93, 'imgComplimentarySettlementReport','frmPOSComplimentarySettlement.html'), "
			+ "	('frmCostCenter', 'Cost Center', 'M', 'imgCostCenter', 22, 'imgCostCenter1','frmPOSCostCenter.html'), "
			+ "	('frmCostCenterWiseReport', 'Cost Centre Report', 'R', 'imgCostCenterReport', 39, 'imgCostCenterReport1','frmPOSCostCenterWiseReport.html'), "
			+ "	('frmCounterMaster', 'CounterMaster', 'M', 'imgCounterMaster', 73, 'imgCounterMaster1','frmPOSCounterMaster.html'), "
			+ "	('frmCounterWiseSalesReport', 'Counter Wise Sales Report', 'R', 'imgCounterWiseSalesReport', 94, 'imgCounterWiseSalesReport','frmPOSCounterWiseSalesReport.html'), "
			+ "	('frmCustAreaMaster', 'Customer Area Master', 'M', 'imgCustAreaMaster', 11, 'imgCustAreaMaster1','frmPOSCustAreaMaster.html'), "
			+ "	('frmCustomerMaster', 'Customer Master', 'M', 'imgCustomerMaster', 30, 'imgCustomerMaster1','frmPOSCustomerMaster.html'), "
			+ "	('frmCustomerTypeMaster', 'CustomerTypeMaster', 'M', 'imgCustomerTypeMaster', 68, 'imgCustomerTypeMaster1','frmPOSCustomerTypeMaster.html'), "
			+ "	('frmDayEndFlash', 'Day End Flash', 'R', 'imgDayEndFlash', 49, 'imgDayEndFlash1','frmPOSDayEndFlash.html'), "
			+ "	('frmDayEndProcess', 'Day End', 'T', 'imgDayEnd', 20, 'imgDayEnd1','frmPOSDayEndProcess.html'), "
			+ "	('frmDayEndWithoutDetails', 'DayEndWithoutDetails', 'T', 'imgDayEndwithoutDetails', 80, 'imgDayEndwithoutDetails1','frmPOSDayEndWithoutDetails.html'), "
			//+ "	('frmDebitCardFlashReports', 'DebitCardFlashReports', 'R', 'imgDebitCardFlash', 66, 'imgDebitCardFlash1','frmPOSDebitCardFlashReports.html'), "
			//+ "	('frmDebitCardMaster', 'DebitCardMaster', 'M', 'imgCardTypeMaster', 58, 'imgCardTypeMaster1','frmPOSDebitCardMaster.html'), "
			+ "	('frmDeliveryboyIncentive', 'DeliveryboyIncentive', 'R', 'imgDeliveryboyIncentive', 105, 'imgDeliveryboyIncentive1','frmPOSDeliveryboyIncentive.html'), "
			+ "	('frmDeliveryPersonMaster', 'Home Delivery Person', 'M', 'imgDeliveryBoy', 19, 'imgDeliveryBoy1','frmPOSDeliveryPersonMaster.html'), "
			+ "	('frmDirectBiller', 'Direct Biller', 'T', 'imgDirectBiller', 28, 'imgDirectBiller1','frmPOSDirectBiller.html'), "
			+ "	('frmDiscountReport', 'Discount Report', 'R', 'imgDiscountReport', 89, 'imgDiscountReport1','frmPOSDiscountReport.html'), "
			//+ "	('frmGiftVoucherIssue', 'GiftVoucherIssue', 'T', 'imgGiftVoucherIssue', 78, 'imgGiftVoucherIssue1','frmPOSGiftVoucherIssue.html'), "
			//+ "	('frmGiftVoucherMaster', 'GiftVoucherMaster', 'M', 'imgGiftVoucher', 61, 'imgGiftVoucher1','frmPOSGiftVoucherMaster.html'), "
			+ "	('frmGroup', 'Group', 'M', 'imgGroup', 34, 'imgGroup1','frmGroup.html'), "
			+ "	('frmGroupSubGroupWiseReport', 'Group-SubGroup Wise Report', 'R', 'imgGrpSubGrpWise', 90, 'imgGrpSubGrpWise1','frmPOSGroupSubGroupWiseReport.html'), "
			+ "	('frmGroupWiseReport', 'Group Wise Report', 'R', 'imgGroupWiseReport', 13, 'imgGroupWiseReport','frmPOSGroupWiseReport.html'), "
			+ "	('frmImportExcelFile', 'ImportExcel', 'T', 'imgImportData', 74, 'imgImportData1','frmPOSImportExcelFile.html'), "
			+ "	('frmItemModifier', 'Item Modifier', 'M', 'imgItemModifier', 27, 'imgItemModifier1','frmPOSItemModifier.html'), "
			+ "	('frmItemWiseReport', 'Item Wise Report', 'R', 'imgItemWiseReport', 10, 'imgItemWiseReport1','frmPOSItemWiseReport.html'), "
			//+ "	('frmKitchenDisplaySystem', 'Kitchen System', 'T', 'imgKitchenDisplaySystem', 45, 'imgKitchenDisplaySystem1','frmPOSKitchenDisplaySystem.html'), "
			//+ "	('frmLoyaltyPointMaster', 'LoyaltyPoints', 'M', 'imgLoyaltyMaster', 75, 'imgLoyaltyMaster','frmPOSLoyaltyPointMaster.html'), "
			//+ "	('frmLoyaltyPointReport', 'Loyalty Point Report', 'R', 'imgLoyaltyPointReport', 91, 'imgLoyaltyPointReport1','frmPOSLoyaltyPointReport.html'), "
			//+ "	('frmMakeBill', 'Make Bill', 'T', 'imgMakeBill', 29, 'imgMakeBill1','frmPOSMakeBill.html'), "
			+ "	('frmMenuHead', 'Menu Head', 'M', 'imgMenuHead', 38, 'imgMenuHead1','frmPOSMenuHead.html'), "
			+ "	('frmMenuHeadWiseReport', 'Menu Head Wise', 'R', 'imgMenuHeadWiseReport', 100, 'imgMenuHeadWiseReport','frmPOSMenuHeadWiseReport.html'), "
			+ "	('frmMenuItem', 'Menu Item', 'M', 'imgMenuItem', 23, 'imgMenuItem1','frmPOSMenuItem.html'), "
			+ "	('frmModifierGroupMaster', 'ModifierGroupMaster', 'M', 'imgModifierGroupMaster', 77, 'imgModifierGroupMaster1','frmPOSModifierGroupMaster.html'), "
			+ "	('frmModifyBill', 'Modify Bill', 'T', 'imgModifyBill', 25, 'imgModifyBill1','frmPOSModifyBill.html'), "
			//+ "	('frmMoveKOT', 'Move KOT', 'T', 'imgMoveKOT', 62, 'imgMoveKOT1','frmPOSMoveKOT.html'), "
			//+ "	('frmMoveTable', 'Move Table', 'T', 'imgMoveTable', 53, 'imgMoveTable1','frmPOSMoveTable.html'), "
			//+ "	('frmMultiCostCenterKDS', 'MultiCostCenterKDS', 'T', 'imgMultiCostCenterKDS', 82, 'imgMultiCostCenterKDS1','frmPOSMultiCostCenterKDS.html'), "
			+ "	('frmNonChargableSettlementReport', 'Non Chargable KOT Report', 'R', 'imgNonChargableKOTReport', 95, 'imgNonChargableKOTReport','frmPOSNonChargableSettlementReport.html'), "
			+ "	('frmOperatorWiseReport', 'OperatorWise Report', 'R', 'imgOperatorWiseReport', 15, 'imgOperatorWiseReport','frmPOSOperatorWiseReport.html'), "
			+ "	('frmOrderAnalysisReport', 'Order Analysis Report', 'R', 'imgOrderAnalysisReport', 96, 'imgOrderAnalysisReport','frmPOSOrderAnalysisReport.html'), "
			+ "	('frmPhysicalStkPosting', 'Physical Stock Posting', 'T', 'imgPhysicalStockPosting', 21, 'imgPhysicalStockPosting1','frmPOSPhysicalStkPosting.html'), "
			+ "	('frmPosMaster', 'POS Master', 'M', 'imgPOSMaster', 5, 'imgPOSMaster1','frmPosMaster.html'), "
			//+ "	('frmPostDataToHO', 'Post Sale Data', 'T', 'imgPostPOSDataToHO', 83, 'imgPostPOSDataToHO1','frmPostDataToHO.html'), "
			//+ "	('frmPostPOSDataToCMS', 'Post POS Data To CMS', 'T', 'imgPostPOSDataToCMS', 98, 'imgPostPOSDataToCMS1','frmPostPOSDataToCMS.html'), "
			+ "	('frmPrice', 'Price Menu', 'M', 'imgPriceMenu', 4, 'imgPriceMenu1','frmPOSPrice.html'), "
			+ "	('frmPromationFlash', 'Promotion Flash', 'R', 'imgPromotionReport', 87, 'imgPromotionReport','frmPOSPromationFlash.html'), "
			+ "	('frmPromationMaster', 'Promotion Master', 'M', 'imgPromotionMaster', 81, 'imgPromotionMaster','frmPOSPromationMaster.html'), "
			+ "	('frmPropertySetup', 'Property Setup', 'U', 'imgPropertySetup', 8, 'imgPropertySetup1','frmPOSPropertySetup.html'), "
			+ "	('frmReasonMaster', 'Reason Master', 'M', 'imgReasonMaster', 18, 'imgReasonMaster1','frmPOSReasonMaster.html'), "
			//+ "	('frmRechargeDebitCard', 'RechargeDebitCard', 'T', 'imgRechargeCard', 60, 'imgRechargeCard1','frmPOSRechargeDebitCard.html'), "
			+ "	('frmRecipeMaster', 'RecipeMaster', 'M', 'imgRecipeMaster', 79, 'imgRecipeMaster1','frmPOSRecipeMaster.html'), "
			//+ "	('frmRegisterDebitCard', 'DebitCardRegister', 'M', 'imgRegisterDebitCard', 59, 'imgRegisterDebitCard1','frmPOSRegisterDebitCard.html'), "
			//+ "	('frmReOrderTime', 'ReOrderTime', 'M', 'imgReorderTime', 72, 'imgReorderTime1','frmPOSReOrderTime.html'), "
			+ "	('frmReprint', 'Reprint', 'T', 'imgReprintDocs', 50, 'imgReprintDocs1','frmPOSReprint.html'), "
			+ "	('frmRestaurantBill', 'Make KOT', 'T', 'imgMakeKOT', 26, 'imgMakeKOT1','frmPOSRestaurantBill.html'), "
			+ "	('frmRestaurantDtl', 'SettleBill', 'T', 'imgSettleBill', 42, 'imgSettleBill1','frmPOSRestaurantDtl.html'), "
			+ "	('frmSalesReport', 'Sales Report', 'U', 'imgSalesReport', 6, 'imgSalesReport1','frmPOSSalesReport.html'), "
			+ "	('frmSettlement', 'Settlement', 'M', 'imgSettlement', 3, 'imgSettlement1','frmPOSSettlement.html'), "
			+ "	('frmSettlementWiseReport', 'SettlementWise Report', 'R', 'imgSettlementWiseReport', 16, 'imgSettlementWiseReport','frmPOSSettlementWiseReport.html'), "
			+ "	('frmShiftMaster', 'Shift Master', 'M', 'imgShiftMaster', 56, 'imgShiftMaster1','frmPOSShiftMaster.html'), "
			+ "	('frmSplitBill', 'SplitBill', 'T', 'imgSplitBill', 69, 'imgSplitBill1','frmPOSSplitBill.html'), "
			+ "	('frmStkAdjustment', 'Stock Adujstment', 'T', 'imgStockAdjustment', 40, 'imgStockAdjustment1','frmPOSStkAdjustment.html'), "
			+ "	('frmStkIn', 'Stock In', 'T', 'imgStockIn', 1, 'imgStockIn1','frmPOSStkIn.html'), "
			+ "	('frmStkInOutFlash', 'Stock In Out Flash', 'R', 'imgStockInOutFlash', 48, 'imgStockInOutFlash1','frmPOSStkInOutFlash.html'), "
			+ "	('frmStkOut', 'Stock Out', 'T', 'imgStockOut', 2, 'imgStockOut1','frmPOSStkOut.html'), "
			+ "	('frmStockFlashReport', 'Stock Flash Report', 'U', 'imgStockFlashReport', 17, 'imgStockFlashReport1','frmPOSStockFlashReport.html'), "
			+ "	('frmSubGroup', 'SubGroup', 'M', 'imgSubGroup', 35, 'imgSubGroup1','frmPOSSubGroup.html'), "
			+ "	('frmSubGroupWiseReport', 'SubGroupWise Report', 'R', 'imgSubGroupWiseReport', 14, 'imgSubGroupWiseReport','frmPOSSubGroupWiseReport.html'), "
			+ "	('frmTableMaster', 'Table Master', 'M', 'imgTableMaster', 37, 'imgTableMaster1','frmPOSTableMaster.html'), "
			+ "	('frmTableStatusReport', 'TableStatusReport', 'T', 'imgTableStatusReport', 70, 'imgTableStatusReport1','frmPOSTableStatusReport.html'), "
			+ "	('frmTaxMaster', 'Tax Master', 'M', 'imgTaxMaster', 36, 'imgTaxMaster1','frmPOSTaxMaster.html'), "
			//+ "	('frmTaxRegeneration', 'Tax Regeneration', 'T', 'imgTaxReGeneration', 92, 'imgTaxReGeneration1','frmPOSTaxRegeneration.html'), "
			+ "	('frmTaxWiseReport', 'Tax Wise Report', 'R', 'imgTaxWiseReport', 41, 'imgTaxWiseReport','frmPOSTaxWiseReport.html'), "
			//+ "	('frmTDH', 'TDH', 'M', 'imgTDH', 67, 'imgTDH1','frmPOSTDH.html'), "
			+ "	('frmTools', 'Tools', 'U', 'imgTools', 57, 'imgTools1','frmPOSTools.html'), "
			+ "	('frmUnsettleBill', 'Unsettle Bill', 'T', 'imgUnsettleBill', 55, 'imgUnsettleBill1','frmUnsettleBill.html'), "
			+ "	('frmUserRegistration', 'User Registration', 'M', 'imgUserRegistration', 7, 'imgUserRegistration1','frmPOSUserRegistration.html'), "
			+ "	('frmVoidAdvanceOrder', 'VoidAdvanceOrder', 'T', 'imgVoidAdvOrder', 104, 'imgVoidAdvOrder1','frmPOSVoidAdvanceOrder.html'), "
			+ "	('frmVoidBill', 'Void Bill', 'T', 'imgVoidBill', 12, 'imgVoidBill1','frmPOSVoidBill.html'), "
			+ "	('frmVoidBillReport', 'Void Bill Report', 'R', 'imgVoidBillReport', 32, 'imgVoidBillReport','frmPOSVoidBillReport.html'), "
			+ "	('frmVoidKot', 'VoidKot', 'T', 'imgVoidKOT', 51, 'imgVoidKOT1','frmPOSVoidKot.html'), "
			+ "	('frmVoidStock', 'VoidStock', 'T', 'imgVoidStock', 52, 'imgVoidStock1','frmPOSVoidStock.html'), "
			+ "	('frmWaiterMaster', 'Waiter Master', 'M', 'imgWaiterMaster', 33, 'imgWaiterMaster1','frmPOSWaiterMaster.html'), "
			+ "	('frmWaiterWiseIncentiveReport', 'WaiterWiseIncentivesReport', 'R', 'imgWaiterIncentivesReports', 103, 'imgWaiterIncentivesReports1','frmPOSWaiterWiseIncentiveReport.html'), "
			+ "	('frmWaiterWiseItemReport', 'WaiterWiseItemReport', 'R', 'imgWaiterWiseItemReport', 102, 'imgWaiterWiseItemReport1','frmPOSWaiterWiseItemReport.html'), "
			+ "	('frmZoneMaster', 'Zone Master', 'M', 'imgZoneMaster', 101, 'imgZoneMaster1','frmPOSZoneMaster.html'), "
			+ "	('frmSalesSummaryFlash', 'Sales Summary Flash', 'R', 'imgSalesSummaryFlash', 106, 'imgSalesSummaryFlash1','frmPOSSalesSummaryFlash.html'), "
			+ "	('frmPOSWiseSalesComparison', 'POS Wise Sales', 'R', 'imgPOSWiseSales', 107, 'imgPOSWiseSales1','frmPOSWiseSalesComparison.html'), "
			//+ "	('frmTableReservation', 'Table Reservation', 'T', 'imgTableReservation', 108, 'imgTableReservation1','frmPOSTableReservation.html'), "
			+ "	('frmStatistics', 'Statistics', 'T', 'imgStatistics', 109, 'imgStatistics1','frmPOSStatistics.html'), "
			+ "	('frmDailyCollectionReport', 'Daily Collection Report', 'R', 'imgDailyCollectionReport', 110,'imgDailyCollectionReport1','frmPOSDailyCollectionReport.html'), "
			+ "	('frmDailySalesReport', 'Daily Sales Report', 'R', 'imgDailySalesReport', 111, 'imgDailySalesReport1','frmPOSDailySalesReport.html'), "
			+ "	('frmVoidKOTReport', 'Void KOT Report', 'R', 'imgVoidKOTReport', 112,'imgVoidKOTReport1','frmPOSVoidKOTReport.html'), "
			+ "	('funTaxBreakupSummaryReport', 'Tax Breakup Summary Report', 'R', 'imgTaxBreakupSummaryReport', 98, 'imgTaxBreakupSummaryReport','funPOSTaxBreakupSummaryReport.html'), "
			+ " ('frmShortcutKeySetup', 'Shortcut Key Setup', 'U', 'imgShortcutKeySetup', 113, 'imgShortcutKeySetup','frmPOSShortcutKeySetup.html'), "
			+ " ('frmGuestCreditReport', 'Guest Credit Report', 'R', 'imgGuestCreditReport', 114, 'imgGuestCreditReport1','frmPOSGuestCreditReport.html'),  "
			//+ " ('frmUserCardSwipe', 'UserCardSwipe', 'M', 'imgUserCards', 115, 'imgUserCards','frmPOSUserCardSwipe.html'),  "
			//+ " ('frmChangeCustomerOnBill', 'ChangeCustomerOnBill', 'T', 'imgChangeCustomerOnBill', 116, 'imgChangeCustomerOnBill1','frmPOSChangeCustomerOnBill.html'), "
			//+ " ('frmPostPOSSalesDataToMMS', 'PostPOSSalesDataToMMS', 'T', 'imgPostPOSSalesDataToMMS', 117, 'imgPostPOSSalesDataToMMS','frmPOSPostPOSSalesDataToMMS.html'), "
			+ " ('frmShiftEndProcessConsolidate', 'ShiftEndProcessConsolidate', 'T', 'imgDayEndConsolidate', 118, 'imgDayEndConsolidate','frmPOSShiftEndProcessConsolidate.html'), "
			//+ " ('frmCustomerDisplaySystem', 'CustomerDisplaySystem', 'T', 'imgCustomerDisplaySystem', 119, 'imgCustomerDisplaySystem1','frmPOSCustomerDisplaySystem.html'), "
			//+ " ('frmGenrateMallInterfaceText', 'GenrateMallInterfaceText', 'T', 'imgGenrateMIText', 120, 'imgGenrateMIText1','frmPOSGenrateMallInterfaceText.html'),  "
			//+ " ('frmSendBulkSMS', 'SendBulkSMS', 'T', 'imgSendBulkSMS', 121, 'imgSendBulkSMS1','frmPOSSendBulkSMS.html'),  "
			//+ " ('frmShowCard', 'ShowCard', 'T', 'imgShowCard', 122, 'imgShowCard','frmPOSShowCard.html'), "
			+ " ('frmSubGroupWiseSummaryReport', 'SubGroupWiseSummaryReport', 'R', 'imgSubGroupWiseSummaryReport', 123, 'imgSubGroupWiseSummaryReport1','frmPOSSubGroupWiseSummaryReport.html'),  "
			+ " ('frmArrangeTransaction', 'Arrange Transaction', 'M', 'imgArrangeTransaction', 124, 'imgArrangeTransaction','frmPOSArrangeTransaction.html'), "
			+ " ('frmNCKOT', 'NCKOT', 'T', 'imgNCKOT', 125, 'imgNCKOT1','frmPOSNCKOT.html'), "
			//+ " ('frmKDSBookAndProcess', 'KDSBookAndProcess', 'T', 'imgKDSBookAndProcess', 126, 'imgKDSBookAndProcess','frmPOSKDSBookAndProcess.html'),  "
			//+ " ('frmUnusedCardBalanceReport', 'UnusedCardBalanceReport', 'R', 'imgUnusedCardBalance', 127, 'imgUnusedCardBalanceReport1','frmPOSUnusedCardBalanceReport.html'), "
			+ " ('frmImportDatabase', 'Import Database', 'T', 'imgImportDatabase', 128, 'imgImportDatabase1','frmPOSImportDatabase.html'),  "
			+ " ('frmTakeAway', 'Take Away', 'T', 'imgTakeAway', 129, 'imgTakeAway','frmPOSTakeAway.html'),  "
			+ " ('frmDayWiseSalesSummaryFlash', 'DayWiseSalesSummaryFlash', 'R', 'imgDayWiseSalesSummary', 130, 'imgDayWiseSalesSummary','frmPOSDayWiseSalesSummaryFlash.html'),  "
			+ " ('frmBillWiseSettlementSalesSummaryFlash', 'BillWiseSettlementSalesSummaryFlash', 'R', 'imgBillWiseSttlementSalesSummary', 131, 'imgBillWiseSttlementSalesSummary','frmPOSBillWiseSettlementSalesSummaryFlash.html'),  "
			+ " ('frmRevenueHeadWiseItemSalesReport', 'Revenue Head Wise Item Sales', 'R', 'imgRevenueHeadWiseItemSales', 132, 'imgRevenueHeadWiseItemSales','frmPOSRevenueHeadWiseItemSalesReport.html'),  "
			//+ " ('frmPlaceOrder', 'Place Order', 'T', 'imgPlaceOrder', 133, 'imgPlaceOrder','frmPOSPlaceOrder.html'),  "
			//+ " ('frmPullOrder', 'Pull Order', 'T', 'imgPullOrder', 134, 'imgPullOrder','frmPOSPullOrder.html'), "
			//+ " ('frmCocktailWorldInterface', 'CW Interface', 'T', 'imgCWInterface', 135, '','frmPOSCocktailWorldInterface.html'), "
			//+ " ('frmManagersReport', 'Managers Report', 'R', 'imgManagersReport', 136, 'imgManagersReport','frmPOSManagersReport.html'), "
			+ "	('frmItemWiseConsumption', 'Item Wise Consumption', 'R', 'imgItemWiseConsumption', 137, 'imgItemWiseConsumption','frmPOSItemWiseConsumption.html'), "
			//+ " ('frmOrderMaster', 'Order Master', 'M', 'imgOrderMaster', 138, 'imgOrderMaster','frmPOSOrderMaster.html'), "
			//+ " ('frmCharactersticsMaster', 'Characterstics Master', 'M', 'imgCharactersticsMaster', 139, 'imgCharactersticsMaster','frmPOSCharactersticsMaster.html'), "
			+ " ('frmTableWisePaxReport', 'Table Wise Pax Report', 'R', 'imgTableWisePaxReport', 140,'imgTablewisePaxReport','frmPOSTableWisePaxReport.html'), "
			//+ " ('frmPostingReport', 'Posting Report', 'R', 'imgPostingReport', 141,'imgPostingReport','frmPOSPostingReport.html'), "
			//+ " ('frmPlacedOrderReport', 'Placed Order Report', 'R', 'imgPlacedOrderReport', 142,'imgPlacedOrderReport','frmPOSPlacedOrderReport.html'), "
			+ " ('ComplimentrySettlement', 'Complimentry Settlement', 'T', 'imgPlacedOrderReport', 143,'imgPlacedOrderReport','POSComplimentrySettlement.html'), "
			+ " ('DiscountOnBill', 'Discount On Bill', 'T', 'imgPlacedOrderReport', 144,'imgPlacedOrderReport','POSDiscountOnBill.html'), "
			+ " ('frmAdvanceOrderReport', 'Advance Order Report', 'R', 'imgAdvanceOrderReport', 145, 'imgAdvanceOrderReport1','frmPOSAdvanceOrderReport.html'), "
			+ " ('frmVoidAdvanceOrderReport', 'Void Advance Order Report', 'R', 'imgVoidAdvanceOrderReport', 146, 'imgVoidAdvanceOrderReport1','frmPOSVoidAdvanceOrderReport.html'), "
			+ " ('frmPhysicalStockFlash', 'PhysicalStockFlash', 'R', 'imgPhysicalStockPosting', 147, 'imgPhysicalStockPosting','frmPOSPhysicalStockFlash.html'), "
			+ " ('frmReprintDocsReport', 'Reprint Docs Report', 'R', 'imgReprintDocsReport', 148, 'imgReprintDocsReport1','frmPOSReprintDocsReport.html'), "
			//+ " ('frmMoveKOTItemToTable', 'Move KOT Items', 'T', 'imgMoveKOTItemsToTable', 149, 'imgMoveKOTItemsToTable','frmPOSMoveKOTItemToTable.html'), "
			+ " ('frmPOSWiseItemIncentive', 'Item Wise Incentives', 'M', 'imgPOSWiseItemIncentive', 150, 'imgPOSWiseItemIncentive','frmPOSWiseItemIncentive.html'), "
			+ " ('frmWaiterWiseItemWiseIncentiveReport', 'Waiter Wise Item Wise Incentives Report', 'R', 'imgWaiterWiseItemWiseIncentivesReports', 151, 'imgWaiterWiseItemWiseIncentivesReports','frmPOSWaiterWiseItemWiseIncentiveReport.html'), "
			//+ " ('frmFactoryMaster', 'Factory Master', 'M', 'imgFactoryMaster', 152, 'imgFactoryMaster1','frmFactoryMaster.html'),"
			//+ " ('frmKDSForKOTBookAndProcess', 'KDSForKOTBookAndProcess', 'T', 'imgKDSForKOTBookAndProcess', '153', 'imgKDSForKOTBookAndProcess1','frmPOSKDSForKOTBookAndProcess.html'), "
			+ " ('frmItemMasterListingReport', 'Item Master Listing Report', 'R', 'imgItemMasterListingReport', '154', 'imgItemMasterListingReport','frmPOSItemMasterListingReport.html'),"
			//+ " ('frmLinkupMaster', 'Linkup Master', 'M', 'imgLinkupMaster', '155', 'imgLinkupMaster','frmPOSLinkupMaster.html'),"
			//+ " ('frmTallyLinkupMaster', 'Tally Linkup Master', 'M', 'imgTallyLinkupMaster', '156', 'imgTallyLinkupMaster', 'frmPOSTallyLinkupMaster.html'),"
			//+ " ('frmExportTallyInterface', 'Export Tally Interface', 'U', 'imgExportTallyInterface', '157', 'imgExportTallyInterface', 'frmPOSExportTallyInterface.html'),"
			+ " ('frmManagerSummaryFlash', 'Manager Summary Flash', 'R', 'imgManagersReport', 158, 'imgManagersReport', 'frmManagerSummaryFlash.html'),"
			//+ " ('frmBarcodeGeneration', 'Barcode Generation', 'T', 'imgBarcodeGeneration', 159, 'imgBarcodeGeneration1', 'frmPOSBarcodeGeneration.html'),"
			//+ " ('frmPostPOSSalesDataToExcise', 'PostPOSSalesDataToExcise', 'T', 'imgPostPOSSalesDataToExcise', '160', 'imgPostPOSSalesDataToExcise', 'frmPOSPostPOSSalesDataToExcise.html'),"
			+ " ('OpenItems', 'Open Items', 'AT', 'imgOpenItems', '161', 'imgOpenItems', 'frmPOSOpenItems.html'),"//AT for Authentication For Transaction 
			+ " ('frmMultiBillSettle', 'Multi Bill Settle', 'T', 'imgMultiBillSettle', '162', 'imgMultiBillSettle1', 'frmPOSMultiBillSettle.html') ";

	    }
	    else //Enterprise
	    {
		sql = "INSERT INTO `tblforms` (`strFormName`, `strModuleName`, `strModuleType`, `strImageName`, `intSequence`, `strColorImageName`,`strRequestMapping`) VALUES "
			+ "('frmAddKOTToBill', 'Add KOT To Bill', 'T', 'imgKOTToBill', 85, 'imgKOTToBill1','frmPOSAddKOTToBill.html'), "
			+ "('frmAdvanceBooking', 'Advance Order', 'T', 'imgAdvanceOrder', 24, 'imgAdvanceOrder1','frmPOSAdvanceBooking.html'), "
			+ "('frmAdvanceOrderFlash', 'Advance Order Flash', 'R', 'imgAdvanceOrderFlash', 47, 'imgAdvanceOrderFlash1','frmPOSAdvanceOrderFlash.html'), "
			+ "('frmAdvanceOrderTypeMaster', 'Advance Order Type Master', 'M', 'imgAdvanceOrderTypeMaster', 86, 'imgAdvanceOrderTypeMaster','frmPOSAdvanceOrderTypeMaster.html'), "
			+ "('frmAIPB', 'AvgItemPerBill', 'R', 'imgAvgItemsPerBill', 63, 'imgAvgItemsPerBill1','frmPOSAIPB.html'), "
			+ "('frmAPC', 'AvgPerCover', 'R', 'imgAveragePC', 64, 'imgAveragePC1','frmPOSAPC.html'), "
			+ "('frmAreaMaster', 'Area Master', 'M', 'imgAreaMaster', 54, 'imgAreaMaster1','frmPOSAreaMaster.html'), "
			+ "('frmAssignHomeDelivery', 'AssignHomeDelivery', 'T', 'imgAsignHomeDelivery', 99, 'imgAsignHomeDelivery1','frmPOSAssignHomeDelivery.html'), "
			+ "('frmATV', 'AvgTicketValue', 'R', 'imgAvgValue', 65, 'imgAvgValue1','frmPOSATV.html'), "
			+ "('frmAuditFlash', 'Audit Flash', 'R', 'imgAuditFlash', 46, 'imgAuditFlash1','frmPOSAuditFlash.html'), "
			+ "('frmAuditorReport', 'Auditor Report', 'R', 'imgAuditorReport', 97, 'imgAuditorReport','frmPOSAuditorReport.html'), "
			+ "('frmAvdBookReceipt', 'Advance Booking Receipt', 'T', 'imgAdvanceBookingReceipt', 31, 'imgAdvanceBookingReceipt1','frmPOSAvdBookReceipt.html'), "
			+ "('frmBillFromKOTs', 'BillFromKOTs', 'T', 'imgMakeBillFromKOT', 76, 'imgMakeBillFromKOT1','frmPOSBillFromKOTs.html'), "
			+ "('frmBillReport', 'Bill Wise Report', 'R', 'imgBillWiseReport', 9, 'imgBillWiseReport','frmPOSBillReport.html'), "
			+ "('frmBulkMenuItemPricing', 'Bulk Menu Item Pricing', 'M', 'imgBulkMenuItemPricing', 88, 'imgBulkMenuItemPricing1','frmPOSBulkMenuItemPricing.html'), "
			+ "('frmCashManagement', 'Cash Management', 'T', 'imgCashManagement', 43, 'imgCashManagement1','frmPOSCashManagement.html'), "
			+ "('frmCashMgmtReport', 'Cash Mgmt Report', 'R', 'imgCashMgmtFlashReport', 44, 'imgCashMgmtFlashReport1','frmPOSCashMgmtReport.html'), "
			+ "('frmCloseProductionOrder', 'Close Producion Order', 'T', 'imgProductionOrder', 84, 'imgProductionOrder1','frmPOSCloseProductionOrder.html'), "
			+ "('frmComplimentarySettlement', 'Complimentary Settlement Report', 'R', 'imgComplimentarySettlementReport', 93, 'imgComplimentarySettlementReport','frmPOSComplimentarySettlement.html'), "
			+ "('frmCostCenter', 'Cost Center', 'M', 'imgCostCenter', 22, 'imgCostCenter1','frmPOSCostCenter.html'), "
			+ "('frmCostCenterWiseReport', 'Cost Centre Report', 'R', 'imgCostCenterReport', 39, 'imgCostCenterReport1','frmPOSCostCenterWiseReport.html'), "
			+ "('frmCounterMaster', 'CounterMaster', 'M', 'imgCounterMaster', 73, 'imgCounterMaster1','frmPOSCounterMaster.html'), "
			+ "('frmCounterWiseSalesReport', 'Counter Wise Sales Report', 'R', 'imgCounterWiseSalesReport', 94, 'imgCounterWiseSalesReport','frmPOSCounterWiseSalesReport.html'), "
			+ "('frmCustAreaMaster', 'Customer Area Master', 'M', 'imgCustAreaMaster', 11, 'imgCustAreaMaster1','frmPOSCustAreaMaster.html'), "
			+ "('frmCustomerMaster', 'Customer Master', 'M', 'imgCustomerMaster', 30, 'imgCustomerMaster1','frmPOSCustomerMaster.html'), "
			+ "('frmCustomerTypeMaster', 'CustomerTypeMaster', 'M', 'imgCustomerTypeMaster', 68, 'imgCustomerTypeMaster1','frmPOSCustomerTypeMaster.html'), "
			+ "('frmDayEndFlash', 'Day End Flash', 'R', 'imgDayEndFlash', 49, 'imgDayEndFlash1','frmPOSDayEndFlash.html'), "
			+ "('frmDayEndProcess', 'Day End', 'T', 'imgDayEnd', 20, 'imgDayEnd1','frmPOSDayEndProcess.html'), "
			+ "('frmDayEndWithoutDetails', 'DayEndWithoutDetails', 'T', 'imgDayEndwithoutDetails', 80, 'imgDayEndwithoutDetails1','frmPOSDayEndWithoutDetails.html'), "
			+ "('frmDebitCardFlashReports', 'DebitCardFlashReports', 'R', 'imgDebitCardFlash', 66, 'imgDebitCardFlash1','frmPOSDebitCardFlashReports.html'), "
			+ "('frmDebitCardMaster', 'DebitCardMaster', 'M', 'imgCardTypeMaster', 58, 'imgCardTypeMaster1','frmPOSDebitCardMaster.html'), "
			+ "('frmDeliveryboyIncentive', 'DeliveryboyIncentive', 'R', 'imgDeliveryboyIncentive', 105, 'imgDeliveryboyIncentive1','frmPOSDeliveryboyIncentive.html'), "
			+ "('frmDeliveryPersonMaster', 'Home Delivery Person', 'M', 'imgDeliveryBoy', 19, 'imgDeliveryBoy1','frmPOSDeliveryPersonMaster.html'), "
			+ "('frmDirectBiller', 'Direct Biller', 'T', 'imgDirectBiller', 28, 'imgDirectBiller1','frmPOSDirectBiller.html'), "
			+ "('frmDiscountReport', 'Discount Report', 'R', 'imgDiscountReport', 89, 'imgDiscountReport1','frmPOSDiscountReport.html'), "
			+ "('frmGiftVoucherIssue', 'GiftVoucherIssue', 'T', 'imgGiftVoucherIssue', 78, 'imgGiftVoucherIssue1','frmPOSGiftVoucherIssue.html'), "
			+ "('frmGiftVoucherMaster', 'GiftVoucherMaster', 'M', 'imgGiftVoucher', 61, 'imgGiftVoucher1','frmPOSGiftVoucherMaster.html'), "
			+ "('frmGroup', 'Group', 'M', 'imgGroup', 34, 'imgGroup1','frmGroup.html'), "
			+ "('frmGroupSubGroupWiseReport', 'Group-SubGroup Wise Report', 'R', 'imgGrpSubGrpWise', 90, 'imgGrpSubGrpWise1','frmPOSGroupSubGroupWiseReport.html'), "
			+ "('frmGroupWiseReport', 'Group Wise Report', 'R', 'imgGroupWiseReport', 13, 'imgGroupWiseReport','frmPOSGroupWiseReport.html'), "
			+ "('frmImportExcelFile', 'ImportExcel', 'T', 'imgImportData', 74, 'imgImportData1','frmPOSImportExcelFile.html'), "
			+ "('frmItemModifier', 'Item Modifier', 'M', 'imgItemModifier', 27, 'imgItemModifier1','frmPOSItemModifier.html'), "
			+ "('frmItemWiseReport', 'Item Wise Report', 'R', 'imgItemWiseReport', 10, 'imgItemWiseReport1','frmPOSItemWiseReport.html'), "
			+ "('frmKitchenDisplaySystem', 'Kitchen System', 'T', 'imgKitchenDisplaySystem', 45, 'imgKitchenDisplaySystem1','frmPOSKitchenDisplaySystem.html'), "
			+ "('frmLoyaltyPointMaster', 'LoyaltyPoints', 'M', 'imgLoyaltyMaster', 75, 'imgLoyaltyMaster','frmPOSLoyaltyPointMaster.html'), "
			+ "('frmLoyaltyPointReport', 'Loyalty Point Report', 'R', 'imgLoyaltyPointReport', 91, 'imgLoyaltyPointReport1','frmPOSLoyaltyPointReport.html'), "
			+ "('frmMakeBill', 'Make Bill', 'T', 'imgMakeBill', 29, 'imgMakeBill1','frmPOSMakeBill.html'), "
			+ "('frmMenuHead', 'Menu Head', 'M', 'imgMenuHead', 38, 'imgMenuHead1','frmPOSMenuHead.html'), "
			+ "('frmMenuHeadWiseReport', 'Menu Head Wise', 'R', 'imgMenuHeadWiseReport', 100, 'imgMenuHeadWiseReport','frmPOSMenuHeadWiseReport.html'), "
			+ "('frmMenuItem', 'Menu Item', 'M', 'imgMenuItem', 23, 'imgMenuItem1','frmPOSMenuItem.html'), "
			+ "('frmModifierGroupMaster', 'ModifierGroupMaster', 'M', 'imgModifierGroupMaster', 77, 'imgModifierGroupMaster1','frmPOSModifierGroupMaster.html'), "
			+ "('frmModifyBill', 'Modify Bill', 'T', 'imgModifyBill', 25, 'imgModifyBill1','frmPOSModifyBill.html'), "
			+ "('frmMoveKOT', 'Move KOT', 'T', 'imgMoveKOT', 62, 'imgMoveKOT1','frmPOSMoveKOT.html'), "
			+ "('frmMoveTable', 'Move Table', 'T', 'imgMoveTable', 53, 'imgMoveTable1','frmPOSMoveTable.html'), "
			+ "('frmMultiCostCenterKDS', 'MultiCostCenterKDS', 'T', 'imgMultiCostCenterKDS', 82, 'imgMultiCostCenterKDS1','frmPOSMultiCostCenterKDS.html'), "
			+ "('frmNonChargableSettlementReport', 'Non Chargable KOT Report', 'R', 'imgNonChargableKOTReport', 95, 'imgNonChargableKOTReport','frmPOSNonChargableSettlementReport.html'), "
			+ "('frmOperatorWiseReport', 'OperatorWise Report', 'R', 'imgOperatorWiseReport', 15, 'imgOperatorWiseReport','frmPOSOperatorWiseReport.html'), "
			+ "('frmOrderAnalysisReport', 'Order Analysis Report', 'R', 'imgOrderAnalysisReport', 96, 'imgOrderAnalysisReport','frmPOSOrderAnalysisReport.html'), "
			+ "('frmPhysicalStkPosting', 'Physical Stock Posting', 'T', 'imgPhysicalStockPosting', 21, 'imgPhysicalStockPosting1','frmPOSPhysicalStkPosting.html'), "
			+ "('frmPosMaster', 'POS Master', 'M', 'imgPOSMaster', 5, 'imgPOSMaster1','frmPosMaster.html'), "
			+ "('frmPostDataToHO', 'Post Sale Data', 'T', 'imgPostPOSDataToHO', 83, 'imgPostPOSDataToHO1','frmPostDataToHO.html'), "
			+ "('frmPostPOSDataToCMS', 'Post POS Data To CMS', 'T', 'imgPostPOSDataToCMS', 98, 'imgPostPOSDataToCMS1','frmPostPOSDataToCMS.html'), "
			+ "('frmPrice', 'Price Menu', 'M', 'imgPriceMenu', 4, 'imgPriceMenu1','frmPOSPrice.html'), "
			+ "('frmPromationFlash', 'Promotion Flash', 'R', 'imgPromotionReport', 87, 'imgPromotionReport','frmPOSPromationFlash.html'), "
			+ "('frmPromationMaster', 'Promotion Master', 'M', 'imgPromotionMaster', 81, 'imgPromotionMaster','frmPOSPromationMaster.html'), "
			+ "('frmPropertySetup', 'Property Setup', 'U', 'imgPropertySetup', 8, 'imgPropertySetup1','frmPOSPropertySetup.html'), "
			+ "('frmReasonMaster', 'Reason Master', 'M', 'imgReasonMaster', 18, 'imgReasonMaster1','frmPOSReasonMaster.html'), "
			+ "('frmRechargeDebitCard', 'RechargeDebitCard', 'T', 'imgRechargeCard', 60, 'imgRechargeCard1','frmPOSRechargeDebitCard.html'), "
			+ "('frmRecipeMaster', 'RecipeMaster', 'M', 'imgRecipeMaster', 79, 'imgRecipeMaster1','frmPOSRecipeMaster.html'), "
			+ "('frmRegisterDebitCard', 'DebitCardRegister', 'M', 'imgRegisterDebitCard', 59, 'imgRegisterDebitCard1','frmPOSRegisterDebitCard.html'), "
			+ "('frmReOrderTime', 'ReOrderTime', 'M', 'imgReorderTime', 72, 'imgReorderTime1','frmPOSReOrderTime.html'), "
			+ "('frmReprint', 'Reprint', 'T', 'imgReprintDocs', 50, 'imgReprintDocs1','frmPOSReprint.html'), "
			+ "('frmRestaurantBill', 'Make KOT', 'T', 'imgMakeKOT', 26, 'imgMakeKOT1','frmPOSRestaurantBill.html'), "
			+ "('frmRestaurantDtl', 'SettleBill', 'T', 'imgSettleBill', 42, 'imgSettleBill1','frmPOSRestaurantDtl.html'), "
			+ "('frmSalesReport', 'Sales Report', 'U', 'imgSalesReport', 6, 'imgSalesReport1','frmPOSSalesReport.html'), "
			+ "('frmSettlement', 'Settlement', 'M', 'imgSettlement', 3, 'imgSettlement1','frmPOSSettlement.html'), "
			+ "('frmSettlementWiseReport', 'SettlementWise Report', 'R', 'imgSettlementWiseReport', 16, 'imgSettlementWiseReport','frmPOSSettlementWiseReport.html'), "
			+ "('frmShiftMaster', 'Shift Master', 'M', 'imgShiftMaster', 56, 'imgShiftMaster1','frmPOSShiftMaster.html'), "
			+ "('frmSplitBill', 'SplitBill', 'T', 'imgSplitBill', 69, 'imgSplitBill1','frmPOSSplitBill.html'), "
			+ "('frmStkAdjustment', 'Stock Adujstment', 'T', 'imgStockAdjustment', 40, 'imgStockAdjustment1','frmPOSStkAdjustment.html'), "
			+ "('frmStkIn', 'Stock In', 'T', 'imgStockIn', 1, 'imgStockIn1','frmPOSStkIn.html'), "
			+ "('frmStkInOutFlash', 'Stock In Out Flash', 'R', 'imgStockInOutFlash', 48, 'imgStockInOutFlash1','frmPOSStkInOutFlash.html'), "
			+ "('frmStkOut', 'Stock Out', 'T', 'imgStockOut', 2, 'imgStockOut1','frmPOSStkOut.html'), "
			+ "('frmStockFlashReport', 'Stock Flash Report', 'U', 'imgStockFlashReport', 17, 'imgStockFlashReport1','frmPOSStockFlashReport.html'), "
			+ "('frmSubGroup', 'SubGroup', 'M', 'imgSubGroup', 35, 'imgSubGroup1','frmPOSSubGroup.html'), "
			+ "('frmSubGroupWiseReport', 'SubGroupWise Report', 'R', 'imgSubGroupWiseReport', 14, 'imgSubGroupWiseReport','frmPOSSubGroupWiseReport.html'), "
			+ "('frmTableMaster', 'Table Master', 'M', 'imgTableMaster', 37, 'imgTableMaster1','frmPOSTableMaster.html'), "
			+ "('frmTableStatusReport', 'TableStatusReport', 'T', 'imgTableStatusReport', 70, 'imgTableStatusReport1','frmPOSTableStatusReport.html'), "
			+ "('frmTaxMaster', 'Tax Master', 'M', 'imgTaxMaster', 36, 'imgTaxMaster1','frmPOSTaxMaster.html'), "
			+ "('frmTaxRegeneration', 'Tax Regeneration', 'T', 'imgTaxReGeneration', 92, 'imgTaxReGeneration1','frmPOSTaxRegeneration.html'), "
			+ "('frmTaxWiseReport', 'Tax Wise Report', 'R', 'imgTaxWiseReport', 41, 'imgTaxWiseReport','frmPOSTaxWiseReport.html'), "
			+ "('frmTDH', 'TDH', 'M', 'imgTDH', 67, 'imgTDH1','frmPOSTDH.html'), "
			+ "('frmTools', 'Tools', 'U', 'imgTools', 57, 'imgTools1','frmPOSTools.html'), "
			+ "('frmUnsettleBill', 'Unsettle Bill', 'T', 'imgUnsettleBill', 55, 'imgUnsettleBill1','frmUnsettleBill.html'), "
			+ "('frmUserRegistration', 'User Registration', 'M', 'imgUserRegistration', 7, 'imgUserRegistration1','frmPOSUserRegistration.html'), "
			+ "('frmVoidAdvanceOrder', 'VoidAdvanceOrder', 'T', 'imgVoidAdvOrder', 104, 'imgVoidAdvOrder1','frmPOSVoidAdvanceOrder.html'), "
			+ "('frmVoidBill', 'Void Bill', 'T', 'imgVoidBill', 12, 'imgVoidBill1','frmPOSVoidBill.html'), "
			+ "('frmVoidBillReport', 'Void Bill Report', 'R', 'imgVoidBillReport', 32, 'imgVoidBillReport','frmPOSVoidBillReport.html'), "
			+ "('frmVoidKot', 'VoidKot', 'T', 'imgVoidKOT', 51, 'imgVoidKOT1','frmPOSVoidKot.html'), "
			+ "('frmVoidStock', 'VoidStock', 'T', 'imgVoidStock', 52, 'imgVoidStock1','frmPOSVoidStock.html'), "
			+ "('frmWaiterMaster', 'Waiter Master', 'M', 'imgWaiterMaster', 33, 'imgWaiterMaster1','frmPOSWaiterMaster.html'), "
			+ "('frmWaiterWiseIncentiveReport', 'WaiterWiseIncentivesReport', 'R', 'imgWaiterIncentivesReports', 103, 'imgWaiterIncentivesReports1','frmPOSWaiterWiseIncentiveReport.html'), "
			+ "('frmWaiterWiseItemReport', 'WaiterWiseItemReport', 'R', 'imgWaiterWiseItemReport', 102, 'imgWaiterWiseItemReport1','frmPOSWaiterWiseItemReport.html'), "
			+ "('frmZoneMaster', 'Zone Master', 'M', 'imgZoneMaster', 101, 'imgZoneMaster1','frmPOSZoneMaster.html'), "
			+ "('frmSalesSummaryFlash', 'Sales Summary Flash', 'R', 'imgSalesSummaryFlash', 106, 'imgSalesSummaryFlash1','frmPOSSalesSummaryFlash.html'), "
			+ "('frmPOSWiseSalesComparison', 'POS Wise Sales', 'R', 'imgPOSWiseSales', 107, 'imgPOSWiseSales1','frmPOSWiseSalesComparison.html'), "
			+ "('frmTableReservation', 'Table Reservation', 'T', 'imgTableReservation', 108, 'imgTableReservation1','frmPOSTableReservation.html'), "
			+ "('frmStatistics', 'Statistics', 'T', 'imgStatistics', 109, 'imgStatistics1','frmPOSStatistics.html'), "
			+ "('frmDailyCollectionReport', 'Daily Collection Report', 'R', 'imgDailyCollectionReport', 110,'imgDailyCollectionReport1','frmPOSDailyCollectionReport.html'), "
			+ "('frmDailySalesReport', 'Daily Sales Report', 'R', 'imgDailySalesReport', 111, 'imgDailySalesReport1','frmPOSDailySalesReport.html'), "
			+ "('frmVoidKOTReport', 'Void KOT Report', 'R', 'imgVoidKOTReport', 112,'imgVoidKOTReport1','frmPOSVoidKOTReport.html'), "
			+ "('funTaxBreakupSummaryReport', 'Tax Breakup Summary Report', 'R', 'imgTaxBreakupSummaryReport', 98, 'imgTaxBreakupSummaryReport','funPOSTaxBreakupSummaryReport.html'), "
			+ "('frmShortcutKeySetup', 'Shortcut Key Setup', 'U', 'imgShortcutKeySetup', 113, 'imgShortcutKeySetup','frmPOSShortcutKeySetup.html'), "
			+ "('frmGuestCreditReport', 'Guest Credit Report', 'R', 'imgGuestCreditReport', 114, 'imgGuestCreditReport1','frmPOSGuestCreditReport.html'),  "
			+ "('frmUserCardSwipe', 'UserCardSwipe', 'M', 'imgUserCards', 115, 'imgUserCards','frmPOSUserCardSwipe.html'),  "
			+ "('frmChangeCustomerOnBill', 'ChangeCustomerOnBill', 'T', 'imgChangeCustomerOnBill', 116, 'imgChangeCustomerOnBill1','frmPOSChangeCustomerOnBill.html'), "
			+ "('frmPostPOSSalesDataToMMS', 'PostPOSSalesDataToMMS', 'T', 'imgPostPOSSalesDataToMMS', 117, 'imgPostPOSSalesDataToMMS','frmPOSPostPOSSalesDataToMMS.html'), "
			+ "('frmShiftEndProcessConsolidate', 'ShiftEndProcessConsolidate', 'T', 'imgDayEndConsolidate', 118, 'imgDayEndConsolidate','frmPOSShiftEndProcessConsolidate.html'), "
			+ "('frmCustomerDisplaySystem', 'CustomerDisplaySystem', 'T', 'imgCustomerDisplaySystem', 119, 'imgCustomerDisplaySystem1','frmPOSCustomerDisplaySystem.html'), "
			+ "('frmGenrateMallInterfaceText', 'GenrateMallInterfaceText', 'T', 'imgGenrateMIText', 120, 'imgGenrateMIText1','frmPOSGenrateMallInterfaceText.html'),  "
			+ "('frmSendBulkSMS', 'SendBulkSMS', 'T', 'imgSendBulkSMS', 121, 'imgSendBulkSMS1','frmPOSSendBulkSMS.html'),  "
			+ "('frmShowCard', 'ShowCard', 'T', 'imgShowCard', 122, 'imgShowCard','frmPOSShowCard.html'), "
			+ "('frmSubGroupWiseSummaryReport', 'SubGroupWiseSummaryReport', 'R', 'imgSubGroupWiseSummaryReport', 123, 'imgSubGroupWiseSummaryReport1','frmPOSSubGroupWiseSummaryReport.html'),  "
			+ "('frmArrangeTransaction', 'Arrange Transaction', 'M', 'imgArrangeTransaction', 124, 'imgArrangeTransaction','frmPOSArrangeTransaction.html'), "
			+ "('frmNCKOT', 'NCKOT', 'T', 'imgNCKOT', 125, 'imgNCKOT1','frmPOSNCKOT.html'), "
			+ "('frmKDSBookAndProcess', 'KDSBookAndProcess', 'T', 'imgKDSBookAndProcess', 126, 'imgKDSBookAndProcess','frmPOSKDSBookAndProcess.html'),  "
			+ "('frmUnusedCardBalanceReport', 'UnusedCardBalanceReport', 'R', 'imgUnusedCardBalance', 127, 'imgUnusedCardBalanceReport1','frmPOSUnusedCardBalanceReport.html'), "
			+ "('frmImportDatabase', 'Import Database', 'T', 'imgImportDatabase', 128, 'imgImportDatabase1','frmPOSImportDatabase.html'),  "
			+ "('frmTakeAway', 'Take Away', 'T', 'imgTakeAway', 129, 'imgTakeAway','frmPOSTakeAway.html'),  "
			+ "('frmDayWiseSalesSummaryFlash', 'DayWiseSalesSummaryFlash', 'R', 'imgDayWiseSalesSummary', 130, 'imgDayWiseSalesSummary','frmPOSDayWiseSalesSummaryFlash.html'),  "
			+ "('frmBillWiseSettlementSalesSummaryFlash', 'BillWiseSettlementSalesSummaryFlash', 'R', 'imgBillWiseSttlementSalesSummary', 131, 'imgBillWiseSttlementSalesSummary','frmPOSBillWiseSettlementSalesSummaryFlash.html'),  "
			+ "('frmRevenueHeadWiseItemSalesReport', 'Revenue Head Wise Item Sales', 'R', 'imgRevenueHeadWiseItemSales', 132, 'imgRevenueHeadWiseItemSales','frmPOSRevenueHeadWiseItemSalesReport.html'),  "
			+ "('frmPlaceOrder', 'Place Order', 'T', 'imgPlaceOrder', 133, 'imgPlaceOrder','frmPOSPlaceOrder.html'),  "
			+ "('frmPullOrder', 'Pull Order', 'T', 'imgPullOrder', 134, 'imgPullOrder','frmPOSPullOrder.html'), "
			+ "('frmCocktailWorldInterface', 'CW Interface', 'T', 'imgCWInterface', 135, '','frmPOSCocktailWorldInterface.html'), "
			+ "('frmManagersReport', 'Managers Report', 'R', 'imgManagersReport', 136, 'imgManagersReport','frmPOSManagersReport.html'), "
			+ "('frmItemWiseConsumption', 'Item Wise Consumption', 'R', 'imgItemWiseConsumption', 137, 'imgItemWiseConsumption','frmPOSItemWiseConsumption.html'), "
			+ "('frmOrderMaster', 'Order Master', 'M', 'imgOrderMaster', 138, 'imgOrderMaster','frmPOSOrderMaster.html'), "
			+ "('frmCharactersticsMaster', 'Characterstics Master', 'M', 'imgCharactersticsMaster', 139, 'imgCharactersticsMaster','frmPOSCharactersticsMaster.html'), "
			+ "('frmTableWisePaxReport', 'Table Wise Pax Report', 'R', 'imgTableWisePaxReport', 140,'imgTablewisePaxReport','frmPOSTableWisePaxReport.html'), "
			+ "('frmPostingReport', 'Posting Report', 'R', 'imgPostingReport', 141,'imgPostingReport','frmPOSPostingReport.html'), "
			+ "('frmPlacedOrderReport', 'Placed Order Report', 'R', 'imgPlacedOrderReport', 142,'imgPlacedOrderReport','frmPOSPlacedOrderReport.html'), "
			+ "('ComplimentrySettlement', 'Complimentry Settlement', 'T', 'imgPlacedOrderReport', 143,'imgPlacedOrderReport','POSComplimentrySettlement.html'), "
			+ "('DiscountOnBill', 'Discount On Bill', 'T', 'imgPlacedOrderReport', 144,'imgPlacedOrderReport','POSDiscountOnBill.html'), "
			+ "('frmAdvanceOrderReport', 'Advance Order Report', 'R', 'imgAdvanceOrderReport', 145, 'imgAdvanceOrderReport1','frmPOSAdvanceOrderReport.html'), "
			+ "('frmVoidAdvanceOrderReport', 'Void Advance Order Report', 'R', 'imgVoidAdvanceOrderReport', 146, 'imgVoidAdvanceOrderReport1','frmPOSVoidAdvanceOrderReport.html'), "
			+ "('frmPhysicalStockFlash', 'PhysicalStockFlash', 'R', 'imgPhysicalStockPosting', 147, 'imgPhysicalStockPosting','frmPOSPhysicalStockFlash.html'), "
			+ "('frmReprintDocsReport', 'Reprint Docs Report', 'R', 'imgReprintDocsReport', 148, 'imgReprintDocsReport1','frmPOSReprintDocsReport.html'), "
			+ "('frmMoveKOTItemToTable', 'Move KOT Items', 'T', 'imgMoveKOTItemsToTable', 149, 'imgMoveKOTItemsToTable','frmPOSMoveKOTItemToTable.html'), "
			+ "('frmPOSWiseItemIncentive', 'Item Wise Incentives', 'M', 'imgPOSWiseItemIncentive', 150, 'imgPOSWiseItemIncentive','frmPOSWiseItemIncentive.html'), "
			+ "('frmWaiterWiseItemWiseIncentiveReport', 'Waiter Wise Item Wise Incentives Report', 'R', 'imgWaiterWiseItemWiseIncentivesReports', 151, 'imgWaiterWiseItemWiseIncentivesReports','frmPOSWaiterWiseItemWiseIncentiveReport.html'), "
			+ "('frmFactoryMaster', 'Factory Master', 'M', 'imgFactoryMaster', 152, 'imgFactoryMaster1','frmFactoryMaster.html'),"
			+ "('frmKDSForKOTBookAndProcess', 'KDSForKOTBookAndProcess', 'T', 'imgKDSForKOTBookAndProcess', '153', 'imgKDSForKOTBookAndProcess1','frmPOSKDSForKOTBookAndProcess.html'), "
			+ "('frmItemMasterListingReport', 'Item Master Listing Report', 'R', 'imgItemMasterListingReport', '154', 'imgItemMasterListingReport','frmPOSItemMasterListingReport.html'),"
			+ "('frmLinkupMaster', 'Linkup Master', 'M', 'imgLinkupMaster', '155', 'imgLinkupMaster','frmPOSLinkupMaster.html'),"
			+ "('frmTallyLinkupMaster', 'Tally Linkup Master', 'M', 'imgTallyLinkupMaster', '156', 'imgTallyLinkupMaster', 'frmPOSTallyLinkupMaster.html'),"
			+ "('frmExportTallyInterface', 'Export Tally Interface', 'U', 'imgExportTallyInterface', '157', 'imgExportTallyInterface', 'frmPOSExportTallyInterface.html'),"
			+ "('frmManagerSummaryFlash', 'Manager Summary Flash', 'R', 'imgManagersReport', 158, 'imgManagersReport', 'frmManagerSummaryFlash.html'),"
			+ "('frmBarcodeGeneration', 'Barcode Generation', 'T', 'imgBarcodeGeneration', 159, 'imgBarcodeGeneration1', 'frmPOSBarcodeGeneration.html'),"
			+ "('frmPostPOSSalesDataToExcise', 'PostPOSSalesDataToExcise', 'T', 'imgPostPOSSalesDataToExcise', '160', 'imgPostPOSSalesDataToExcise', 'frmPOSPostPOSSalesDataToExcise.html'),"
			+ "('OpenItems', 'Open Items', 'AT', 'imgOpenItems', '161', 'imgOpenItems', 'frmPOSOpenItems.html'),"//AT for Authentication For Transaction 
			+ "('frmMultiBillSettle', 'Multi Bill Settle', 'T', 'imgMultiBillSettle', '162', 'imgMultiBillSettle1', 'frmPOSMultiBillSettle.html'),"
			+ "('frmCustomerHistoryFlashReport','Customer History Flash Report','R' ,'imgCustomerHistoryFlashReport',163 ,'imgCustomerHistoryFlashReport1','frmPOSCustomerHistoryFlash.html'),"
			+ "('frmDeliveryBoyWiseCashTakenReport', 'Delivery Boy Wise Cash Taken', 'R', 'imgDeliveryBoyWiseCashTaken', '164', 'imgDeliveryBoyWiseCashTaken1', 'frmPOSDeliveryBoyWiseCashTaken.html'),"
			+ "('frmJioMoneyRefund', 'JioMoney Refund', 'T', 'imgJioMoneyRefund', '165', 'imgJioMoneyRefund', 'frmPOSJioMoneyRefund.html'),"
			+ "('frmJioMoneyTransactionFlash', 'JioMoney Transacttion Flash', 'R', 'imgJioMoneyTransactionFlash', '166', 'imgJioMoneyTransactionFlash', 'frmPOSJioMoneyTransactionFlash.html'),"
			+ "('frmMailDayEndReports', 'Mail Day End Reports', 'R', 'imgMailDayEndReports', '167', 'imgMailDayEndReports1', 'frmPOSMailDayEndReports.html'),"
			+ "('frmCreditBillReceipt', 'Credit Bill Receipt', 'T', 'imgCreditBillReceipt', '168', 'imgCreditBillReceipt1', 'frmPOSCreditBillReceipt.html'),"
			+ "('frmCreditBillOutstandingReport', 'Credit Bill Outstanding Report', 'R', 'imgCreditBillOutstandingReport', '169', 'imgCreditBillOutstandingReport1', 'frmPOSCreditBillOutstandingReport.html'),"
			+ "('frmNonAvailableItems', 'Non Available Items', 'T', 'imgNonAvailableItem', '170', 'imgNonAvailableItem1', 'frmPOSNonAvailableItems.html'),"
			+ "('frmChangeSettlement','Change Settlement','T','imgChangeSettlement',171,'imgChangeSettlement1','frmPOSChangeSettlement.html'),"
			+ "('frmPOSDashboard', 'Dashboard', 'R', 'imgDashboard', '172', 'imgDashboard', 'frmPOSDashboard.html'),"
			+ "('frmPOSSaleVSPurchase', 'SaleVSPurchase', 'R', 'imgSaleVSPurchase', '173', 'imgSaleVSPurchase', 'frmPOSSaleVSPurchase.html'),"
			+ "('frmPOSComparisonwiseDashboard', 'Comparisonwise Dashboard', 'R', 'imgComparisonwiseDashboard', '174', 'imgComparisonwiseDashboard', 'frmPOSComparisonwiseDashboard.html'),"
			+ "('frmCustomerOrder', 'Customer Order', 'T', 'imgCustomerOrder', '175', 'imgCustomerOrder', 'frmPOSCustomerOrder.html'),"
			+ "('frmChangePassword', 'Change Password', 'U', 'imgChangePassword', '176', 'imgChangePassword1', 'frmChangePassword.html'),"
			+ "('frmSettlementWiseGroupWiseBreakup', 'Settlement Wise Group Wise Breakup', 'R', 'imgSettlementWiseGroupWiseBreakup', '177', 'imgSettlementWiseGroupWiseBreakup1', 'frmSettlementWiseGroupWiseBreakup.html'), "
			+ "('frmPromotionGroupMaster', 'Promotion Group Master', 'M', 'imgPromotionGroupMaster', '178', 'imgPromotionGroupMaster1', 'frmPromotionGroupMaster.html'),"
			+ "('frmBillWiseSettlementWiseGroupWiseBreakup', 'Bill Wise Settlement Wise Group Wise Breakup', 'R', 'imgBillWiseSettlementWiseGroupWiseBreakup', 179, 'imgBillWiseSettlementWiseGroupWiseBreakup1', 'frmBillWiseSettlementWiseGroupWiseBreakup.html'),"
			+ "('frmSupplierMaster', 'Supplier Master', 'M', 'imgSupplierMaster', '180', 'imgSupplierMaster1', 'frmSupplierMaster.html'),"
			+ "('frmPurchaseOrder', 'Purchase Order', 'T', 'imgPurchaseOrder', 181, 'imgPurchaseOrder1', 'frmPurchaseOrder.html'),"
			+ "('frmMiniMakeKOT', 'Mini Make KOT', 'T', 'imgminimakekot', 182, 'imgminimakekot1', 'frmMiniMakeKOT.html'),"
			+ "('frmPurchaseOrderReport', 'Purchase Order Report', 'R', 'imgManagersReport', 183, 'imgManagersReport1', 'frmPurchaseOrderReport.html'),"
			+ "('frmKPS', 'Kitchen Process System', 'T', 'imgKPS', 184, 'imgKPS1', 'frmKPS.html'),"
			+ "('frmKDSFlash', 'KDS Flash', 'R', 'imgKDSFlash', 185, 'imgKDSFlash1', 'frmKDSFlash.html'),"
			+ "('frmUserGroupRights', 'User Group Rights', 'M', 'imgUserGroupRights', 186, 'imgUserGroupRights1', 'frmUserGroupRights.html'),"
			+ "('frmComplimentaryItems', 'Complimentary Items', 'T', 'imgComplimentaryItems', '187', 'imgComplimentaryItems1', 'frmComplimentaryItems.html'),"
			+ "('frmUomMaster', 'UOM Master', 'M', 'imgUomMaster', 188, 'imgUomMaster1', 'frmUomMaster.html'),"
			+ "('frmFoodCosting', 'Food Costing', 'R', 'imgFoodCosting', 189, 'imgFoodCosting1', 'frmFoodCosting.html'),"
			+ "('frmUnlockTable', 'Unlock Table', 'T', 'imgUnlockTable', 190, 'imgUnlockTable1', 'frmUnlockTable.html') "
			+ ",('frmDiscountMaster', 'Discount Master', 'M', 'imgDiscountMaster', 191, 'imgDiscountMaster1', 'frmDiscountMaster.html')"
			+ ",('frmBillSeriesMaster', 'Bill Series Master', 'M', 'imgBillSeriesMaster', 192, 'imgBillSeriesMaster1', 'frmBillSeriesMaster.html')"
			+ ",('frmPaymentInterfaceMaster', 'Payment Interface Master', 'M', 'imgPaymentInterfaceMaster', 193, 'imgPaymentInterfaceMaster1', 'frmPaymentInterfaceMaster.html')"
			+ ",('frmPlayZonePricingMaster', 'PlayZone Pricing Master', 'M', 'imgPlayZonePricingMaster', 194, 'imgPlayZonePricingMaster1', 'frmPlayZonePricingMaster.html')"
			+ ",('frmBlindSettlementWiseReport', 'Blind Settlement Wise Report', 'R', 'imgBlindSettlementWiseReport', 195, 'imgBlindSettlementWiseReport1', 'frmPOSBlindSettlementWiseReport1.html')"
			+ ",('frmGrossSalesSummary', 'Gross Sales Summary', 'R', 'imgGrossSalesSummary', '196', 'imgGrossSalesSummary1', 'frmGrossSalesSummary.html')"
			+ ",('frmOpenIteWiseAuditReport', 'Open Item Wise Audit Report', 'R', 'imgOpenItemWiseAuditReport', 197, 'imgOpenItemWiseAuditReport1', 'frmOpenItemWiseAuditReport.html')"
			+ ",('frmCallCenter', 'Call Center', 'T', 'imgCallCenter', '198', 'imgCallCenter', 'frmCallCenter.html') "
			+ ",('frmBillSettlementTemp', 'Bill Settlement', 'T', 'imgSettleBill', '199', 'imgSettleBill', 'frmBillSettlementTemp.html')"
			+ ",('frmCallCenterOrderFlash', 'Call Center Order Flash', 'T', 'imgCallCenterOrderFlash', '200', 'imgCallCenterOrderFlash1', 'frmCallCenterOrderFlash.html') "
			+ ",('frmNonSellingItems', 'Non Selling Items', 'R', 'imgNonSellingItems', '201', 'imgNonSellingItems1', 'frmNonSellingItems.html') "
			+ ",('frmMoveItemsToTable', 'Move Items To Table', 'T', 'imgMoveItemsToTable', '202', 'imgMoveItemsToTable1', 'frmNonSellingItems.html') "
			+ ",('frmBillForItems', 'Bill For Items', 'T', 'imgBillForItems', '203', 'imgBillForItems1', 'frmBillForItems.html')"
			+ ",('frmDebtorsAsOnReport', 'Debtors As On', 'R', 'imgDebtorsAsOn', '204', 'imgDebtorsAsOn1', 'frmDebtorsAsOnReport.html') "
			+ ",('frmPaymentReceiptReport', 'Payment Receipt Report', 'R', 'imgPaymentReceiptReport', '205', 'imgPaymentReceiptReport1', 'frmPaymentReceiptReport.html') "
			+ ",('frmCreditReport', 'Credit Report', 'R', 'imgCreditReport', '206', 'imgCreditReport1', 'frmCreditReport.html' )"
			+ ",('frmPrinterSetup', 'Printer Setup', 'M', 'imgPrinterSetup', '207', 'imgPrinterSetup1', 'frmPrinterSetup.html')"
			+ ",('frmRegisterInOutPlayZone', 'RegisterInOutPlayZone', 'T', 'imgRegisterInOutPlayZone', '208', 'imgRegisterInOutPlayZone1', 'frmRegisterInOutPlayZone.html') "
			+ ",('frmConsolidatedDiscountReport', 'Consolidated Discount Report', 'R', 'imgConsolidatedDiscountReport', '209', 'imgConsolidatedDiscountReport1', 'frmConsolidatedDiscountReport.html') "
			+ ",('frmWeraFoodOrders', 'Wera Food Online Orders', 'T', 'imgWeraFoodOrders', '210', 'imgWeraFoodOrders1', 'frmWeraFoodOrders.html')"
			+ ",('frmCustomerLedger', 'Customer Ledger', 'R', 'imgCustomerLedger', '211', 'imgCustomerLedger1', 'frmCustomerLedger.html') ";
	    }
	    mapStructureUpdater.get("frmStructure").add(sql);

	    sql = "ALTER TABLE `tblforms` "
		    + "	ADD COLUMN `strShortName` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strRequestMapping`;";
	    mapStructureUpdater.get("frmStructure").add(sql);

	    sql = "update tblforms  "
		    + "set strShortName=strModuleName "
		    + "where strShortName=''; ";
	    mapStructureUpdater.get("frmStructure").add(sql);

	    sql = "UPDATE tblforms SET `strRequestMapping`='frmPOSFactoryMaster.html' WHERE  `strFormName`='frmFactoryMaster' AND `intSequence`=152 ; ";
	    mapStructureUpdater.get("frmStructure").add(sql);

	    sql = "UPDATE `tblforms` SET `strImageName`='imgManagersSummaryReport', `strColorImageName`='imgManagersSummaryReport1' "
		    + "WHERE `strFormName`='frmManagerSummaryFlash' AND `strModuleName`='Manager Summary Flash' AND `strModuleType`='R' AND `intSequence`=158  "
		    + "LIMIT 1; ";
	    mapStructureUpdater.get("frmStructure").add(sql);

	    //M for Masters
	    //T for Transactions
	    //R for Reports
	    //U for Utility
	    //AT for Authentication For Transaction 
	    //AM for Authentication For Masters 
	    //AR for Authentication For Reports 
	    //AU for Authentication For Utility   
	    //to delete forms which sequence no is greater than '211'
	    sql = "delete  "
		    + "from tblforms "
		    + "where intSequence>211 ";
	    mapStructureUpdater.get("frmStructure").add(sql);

	    String sqlInsertSyntax = "INSERT INTO `tblforms`  "
		    + "(`strFormName`, `strModuleName`, `strModuleType`, `strImageName`, `intSequence`, `strColorImageName`, `strRequestMapping`,`strShortName`)  "
		    + "VALUES ";
	    if (clsGlobalVarClass.gPOSVerion.equalsIgnoreCase("Lite"))
	    {

	    }
	    else//Enterprise
	    {
		sql = sqlInsertSyntax + "('frmAreaWiseGroupWiseSales', 'Area Wise Group Wise Sales', 'R', 'imgAreaWiseGroupWiseSales', '212', 'imgAreaWiseGroupWiseSales'"
			+ ", 'frmCustomerLedger.html','Area Wise Group Wise Sales') ";
		mapStructureUpdater.get("frmStructure").add(sql);

		sql = sqlInsertSyntax + "('frmDebitCardBulkRecharge', 'Debit Card Bulk Recharge', 'T', 'imgDebitCardBulkRecharge', '213', 'imgDebitCardBulkRecharge1' "
			+ ", 'frmDebitCardBulkRecharge.html', 'Bulk Recharge') ";
		mapStructureUpdater.get("frmStructure").add(sql);
		
	    }
	    //strucure update for tblforms
	    sql = "update tblforms set strColorImageName=CONCAT(strImageName,'1') ";
	    mapStructureUpdater.get("frmStructure").add(sql);

	    sql = "UPDATE `tblforms` "
		    + "SET `strFormName`='frmWebPOSBilling', `strModuleName`='Billing',`strRequestMapping`='frmWebPOSBilling.html' , `strShortName`='Billing' "
		    + "WHERE  `strFormName`='frmBillSettlementTemp'  "
		    + "AND `strModuleName`='Bill Settlement'  "
		    + "AND `strModuleType`='T'  "
		    + "AND `intSequence`=199 ; ";
	    mapStructureUpdater.get("frmStructure").add(sql);

	    sql = "UPDATE `tblforms` SET `strShortName`='Move KOT Items To Table' WHERE  `strFormName`='frmMoveKOTItemToTable' AND `strModuleName`='Move KOT Items' ";
	    mapStructureUpdater.get("frmStructure").add(sql);

	    sql = "UPDATE `tblforms` SET `strShortName`='Reprint Documents' WHERE  `strFormName`='frmReprint' AND `strModuleName`='Reprint' ";
	    mapStructureUpdater.get("frmStructure").add(sql);

	    sql = "UPDATE `tblforms` SET `strShortName`='Delivery Boy' WHERE  `strFormName`='frmDeliveryPersonMaster';";
	    mapStructureUpdater.get("frmStructure").add(sql);

	    sql = "UPDATE `tblforms` SET `strShortName`='Import Export Master' WHERE  `strFormName`='frmImportExcelFile';";
	    mapStructureUpdater.get("frmStructure").add(sql);

	    sql = "UPDATE `tblforms` SET `strRequestMapping`='frmMoveItemsToTable.html' WHERE `strFormName`='frmMoveItemsToTable';";
	    mapStructureUpdater.get("frmStructure").add(sql);
	    
	    sql = "UPDATE `tblforms` SET `strShortName`='Consolidate Day End' WHERE `strFormName`='frmShiftendprocessconsolidate';";
	    mapStructureUpdater.get("frmStructure").add(sql);
	    
	    sql = "UPDATE `tblforms` SET `strShortName`='Blank Day End' WHERE `strFormName`='frmDayEndWithoutDetails';";
	    mapStructureUpdater.get("frmStructure").add(sql);
	    
	    sql = "UPDATE `tblforms` SET `strRequestMapping`='frmAreaWiseGroupWiseSales.html' WHERE  `strFormName`='frmAreaWiseGroupWiseSales';";
	    mapStructureUpdater.get("frmStructure").add(sql);
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }
}
