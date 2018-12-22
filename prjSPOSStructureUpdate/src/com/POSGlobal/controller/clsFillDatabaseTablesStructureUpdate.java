/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.POSGlobal.controller;

import static com.POSGlobal.controller.clsGlobalVarClass.gSystemDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Add all your table level structure update quries here. @see
 * clsFillFormsTableStructureUpdate ,to add new forms (forms structure update).
 *
 * @author Ajim
 *
 */
public class clsFillDatabaseTablesStructureUpdate
{

    private Map<String, List<String>> mapStructureUpdater;

    public clsFillDatabaseTablesStructureUpdate(Map<String, List<String>> mapStructureUpdater)
    {
	this.mapStructureUpdater = mapStructureUpdater;
    }

    /**
     * Add all your table level structure update quries here. @see
     * clsFillFormsTableStructureUpdate ,to add new forms (forms structure
     * update).
     *
     * @author Ajim
     *
     */
    public void funFillDatabaseTablesStructureUpdate()
    {
	String sql = "CREATE TABLE IF NOT EXISTS `tblpromotionmaster` ( "
		+ "	`strPromoCode` VARCHAR(10) NOT NULL, "
		+ "	`strBuyPropmoItemCode` VARCHAR(10) NOT NULL, "
		+ "	`strBuyOperator` VARCHAR(3) NOT NULL, "
		+ "	`dblBuyQty` DECIMAL(18,2) NOT NULL DEFAULT '0.00', "
		+ "	`dblBuyDiscount` DECIMAL(18,2) NOT NULL DEFAULT '0.00', "
		+ "	`strGetPromoCode` VARCHAR(10) NOT NULL, "
		+ "	`strGetOperator` VARCHAR(3) NOT NULL, "
		+ "	`dblGetQty` DECIMAL(18,2) NOT NULL DEFAULT '0.00', "
		+ "	`dblGetDiscount` DECIMAL(18,2) NOT NULL DEFAULT '0.00', "
		+ "	`dteFromDate` DATETIME NOT NULL, "
		+ "	`dteToDate` DATETIME NOT NULL, "
		+ "	`tmeFromTime` VARCHAR(15) NOT NULL, "
		+ "	`tmeToTime` VARCHAR(15) NOT NULL, "
		+ "	`strDays` VARCHAR(200) NOT NULL, "
		+ "	`strPromoNote` VARCHAR(200) NOT NULL DEFAULT '', "
		+ "	`strUserCreated` VARCHAR(10) NOT NULL, "
		+ "	`strUserEdited` VARCHAR(10) NOT NULL, "
		+ "	`dteDateCreated` DATETIME NOT NULL, "
		+ "	`dteDateEdited` DATETIME NOT NULL, "
		+ "	`strClientCode` VARCHAR(10) NOT NULL, "
		+ "	`strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N', "
		+ "	INDEX `strPromoCode_strClientCode` (`strPromoCode`, `strClientCode`) "
		+ ") "
		+ "COLLATE='latin1_swedish_ci' "
		+ "ENGINE=InnoDB;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE IF NOT EXISTS `tblloyaltypoints` ( "
		+ "	`strLoyaltyCode` VARCHAR(10) NOT NULL, "
		+ "	`dblAmount` DECIMAL(18,2) NOT NULL DEFAULT '0.00', "
		+ "	`dblLoyaltyPoints` DECIMAL(18,2) NOT NULL DEFAULT '0.00', "
		+ "	`dblLoyaltyValue` DECIMAL(18,2) NOT NULL DEFAULT '0.00', "
		+ "	`strUserCreated` VARCHAR(10) NOT NULL, "
		+ "	`strUserEdited` VARCHAR(10) NOT NULL, "
		+ "	`dteDateCreated` DATETIME NOT NULL, "
		+ "	`dteDateEdited` DATETIME NOT NULL, "
		+ "	`strClientCode` VARCHAR(10) NOT NULL, "
		+ "	`strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N', "
		+ "	PRIMARY KEY (`strLoyaltyCode`, `strClientCode`) "
		+ ") "
		+ "COLLATE='latin1_swedish_ci' "
		+ "ENGINE=InnoDB;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE IF NOT EXISTS `tblareawisedc` ( "
		+ "  `strBuildingCode` varchar(15) NOT NULL, "
		+ "  `dblKilometers` decimal(18,2) NOT NULL DEFAULT '0.00', "
		+ "  `strSymbol` varchar(3) NOT NULL, "
		+ "  `dblBillAmount` decimal(18,2) NOT NULL DEFAULT '0.00', "
		+ "  `dblBillAmount1` decimal(18,2) NOT NULL DEFAULT '0.00', "
		+ "  `dblDeliveryCharges` decimal(18,2) NOT NULL DEFAULT '0.00', "
		+ "  `strUserCreated` varchar(10) NOT NULL, "
		+ "  `strUserEdited` varchar(10) NOT NULL, "
		+ "  `dteDateCreated` datetime NOT NULL, "
		+ "  `dteDateEdited` datetime NOT NULL, "
		+ "  `strClientCode` varchar(10) NOT NULL "
		+ ") ENGINE=InnoDB DEFAULT CHARSET=latin1;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblareawisedc` CHANGE COLUMN `dblBillAmount` `dblBillAmount` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `strSymbol`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblareawisedc` ADD COLUMN `dblBillAmount1` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `dblBillAmount`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblcustomermaster` ADD COLUMN `strOfficeBuildingCode` VARCHAR(10) NOT NULL DEFAULT '' AFTER `longAlternateMobileNo`, CHANGE COLUMN `strOfficeBuilding` `strOfficeBuildingName` VARCHAR(200) NOT NULL DEFAULT '' AFTER `strOfficeBuildingCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblcustomermaster` ALTER `strClientCode` DROP DEFAULT;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblcustomermaster` CHANGE COLUMN `strClientCode` `strClientCode` VARCHAR(15) NOT NULL AFTER `strDataPostFlag`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbillhd` ADD COLUMN `dblDeliveryCharges` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `strCounterCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbillhd` ADD COLUMN `strCounterCode` VARCHAR(10) NOT NULL DEFAULT 'NA' AFTER `dteSettleDate`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbillhd` ADD COLUMN `dblDeliveryCharges` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `strCounterCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE IF NOT EXISTS `tbltempsalesflash` ( "
		+ "	`strcode` VARCHAR(10) NOT NULL, "
		+ "	`strname` VARCHAR(50) NOT NULL, "
		+ "	`dblquantity` DECIMAL(18,2) NOT NULL, "
		+ "	`dblamount` DECIMAL(18,2) NOT NULL, "
		+ "	`strposcode` VARCHAR(10) NOT NULL, "
		+ "	`struser` VARCHAR(10) NOT NULL "
		+ ") "
		+ "COLLATE='latin1_swedish_ci' "
		+ "ENGINE=InnoDB;";

	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbillsettlementdtl` DROP COLUMN `intID`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbillsettlementdtl` DROP COLUMN `intID`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "DROP VIEW `vcustomeroffaddress`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "create view vcustomeroffaddress as "
		+ "SELECT * from tblcustomermaster  "
		+ "WHERE strOfficeAddress='Y' ;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblhomedelivery` "
		+ "ALTER `strCustomerCode` DROP DEFAULT, "
		+ "ALTER `strClientCode` DROP DEFAULT; ";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblhomedelivery` CHANGE COLUMN `strCustomerCode` `strCustomerCode` VARCHAR(50) NOT NULL AFTER `strBillNo`, "
		+ "	CHANGE COLUMN `dteDate` `dteDate` DATE NULL DEFAULT NULL AFTER `strDPCode`, "
		+ "	CHANGE COLUMN `strCustAddressLine1` `strCustAddressLine1` VARCHAR(200) NOT NULL DEFAULT '' AFTER `strPOSCode`, "
		+ "	CHANGE COLUMN `strCustAddressLine2` `strCustAddressLine2` VARCHAR(200) NOT NULL DEFAULT '' AFTER `strCustAddressLine1`, "
		+ "	CHANGE COLUMN `strCustAddressLine3` `strCustAddressLine3` VARCHAR(200) NOT NULL DEFAULT '' AFTER `strCustAddressLine2`, "
		+ "	CHANGE COLUMN `strCustAddressLine4` `strCustAddressLine4` VARCHAR(200) NOT NULL DEFAULT '' AFTER `strCustAddressLine3`, "
		+ "	CHANGE COLUMN `strCustCity` `strCustCity` VARCHAR(30) NOT NULL DEFAULT '' AFTER `strCustAddressLine4`, "
		+ "	CHANGE COLUMN `strClientCode` `strClientCode` VARCHAR(15) NOT NULL AFTER `strDataPostFlag`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ "                ADD COLUMN `strEditHomeDelivery` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strPrintType`, "
		+ "                ADD COLUMN `strSlabBasedHDCharges` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strEditHomeDelivery`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbuildingmaster` "
		+ "	ALTER `strBuildingName` DROP DEFAULT, "
		+ "	ALTER `strAddress` DROP DEFAULT, "
		+ "	ALTER `strUserCreated` DROP DEFAULT, "
		+ "	ALTER `strUserEdited` DROP DEFAULT, "
		+ "	ALTER `dteDateCreated` DROP DEFAULT, "
		+ "	ALTER `dteDateEdited` DROP DEFAULT;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "       ALTER TABLE `tblbuildingmaster` "
		+ "	CHANGE COLUMN `strBuildingName` `strBuildingName` VARCHAR(200) NOT NULL AFTER `strBuildingCode`, "
		+ "	CHANGE COLUMN `strAddress` `strAddress` VARCHAR(200) NOT NULL AFTER `strBuildingName`, "
		+ "	CHANGE COLUMN `strUserCreated` `strUserCreated` VARCHAR(10) NOT NULL AFTER `strAddress`, "
		+ "	CHANGE COLUMN `strUserEdited` `strUserEdited` VARCHAR(10) NOT NULL AFTER `strUserCreated`, "
		+ "	CHANGE COLUMN `dteDateCreated` `dteDateCreated` DATETIME NOT NULL AFTER `strUserEdited`, "
		+ "	CHANGE COLUMN `dteDateEdited` `dteDateEdited` DATETIME NOT NULL AFTER `dteDateCreated`, "
		+ "	CHANGE COLUMN `dblHomeDeliCharge` `dblHomeDeliCharge` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `dteDateEdited`; "
		+ "";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsubgrouphd` DROP FOREIGN KEY `tblSubGrouphd_ibfk_1`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblvoidbilldtl set strItemCode='' where strItemCode is NULL;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblvoidbilldtl set strReasonCode='' where strReasonCode is NULL;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblvoidbilldtl set strReasonName='' where strReasonName is NULL;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblvoidbilldtl set strItemName='' where strItemName is NULL;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblvoidbilldtl set strBillNo='' where strBillNo is NULL;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblvoidbilldtl set intQuantity=0 where intQuantity is NULL;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblvoidbilldtl set dblAmount=0.00 where dblAmount is NULL;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblvoidbilldtl set dblTaxAmount=0.00 where dblTaxAmount is NULL;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblvoidbilldtl set strTransType='' where strTransType is NULL;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblvoidbilldtl set strSettlementCode='' where strSettlementCode is NULL;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblvoidbilldtl set dblSettlementAmt=0.00 where dblSettlementAmt is NULL;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblvoidbilldtl set dblPaidAmt=0.00 where dblPaidAmt is NULL;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblvoidbilldtl set strTableNo='' where strTableNo is NULL;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblvoidbilldtl set strWaiterNo='' where strWaiterNo is NULL;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblvoidbilldtl set strUserCreated='' where strUserCreated is NULL;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblvoidbilldtl set strKOTNo='' where strKOTNo is NULL;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblvoidbilldtl` "
		+ "	ALTER `strItemCode` DROP DEFAULT, "
		+ "	ALTER `strItemName` DROP DEFAULT, "
		+ "	ALTER `strBillNo` DROP DEFAULT, "
		+ "	ALTER `dteBillDate` DROP DEFAULT, "
		+ "	ALTER `strTransType` DROP DEFAULT, "
		+ "	ALTER `dteModifyVoidBill` DROP DEFAULT, "
		+ "	ALTER `strSettlementCode` DROP DEFAULT, "
		+ "	ALTER `strUserCreated` DROP DEFAULT;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblvoidbilldtl` "
		+ "	CHANGE COLUMN `strReasonCode` `strReasonCode` VARCHAR(10) NOT NULL DEFAULT '' AFTER `strPosCode`, "
		+ "	CHANGE COLUMN `strReasonName` `strReasonName` VARCHAR(50) NOT NULL DEFAULT '' AFTER `strReasonCode`, "
		+ "	CHANGE COLUMN `strItemCode` `strItemCode` VARCHAR(10) NOT NULL AFTER `strReasonName`, "
		+ "	CHANGE COLUMN `strItemName` `strItemName` VARCHAR(50) NOT NULL AFTER `strItemCode`, "
		+ "	CHANGE COLUMN `strBillNo` `strBillNo` VARCHAR(10) NOT NULL AFTER `strItemName`, "
		+ "	CHANGE COLUMN `intQuantity` `intQuantity` INT(11) NOT NULL DEFAULT '0' AFTER `strBillNo`, "
		+ "	CHANGE COLUMN `dblAmount` `dblAmount` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `intQuantity`, "
		+ "	CHANGE COLUMN `dteBillDate` `dteBillDate` DATETIME NOT NULL AFTER `dblTaxAmount`, "
		+ "	CHANGE COLUMN `strTransType` `strTransType` CHAR(10) NOT NULL AFTER `dteBillDate`, "
		+ "	CHANGE COLUMN `dteModifyVoidBill` `dteModifyVoidBill` DATETIME NOT NULL AFTER `strTransType`, "
		+ "	CHANGE COLUMN `strSettlementCode` `strSettlementCode` VARCHAR(10) NOT NULL AFTER `dteModifyVoidBill`, "
		+ "	CHANGE COLUMN `dblSettlementAmt` `dblSettlementAmt` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `strSettlementCode`, "
		+ "	CHANGE COLUMN `dblPaidAmt` `dblPaidAmt` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `dblSettlementAmt`, "
		+ "	CHANGE COLUMN `strTableNo` `strTableNo` VARCHAR(50) NOT NULL DEFAULT 'NA' AFTER `dblPaidAmt`, "
		+ "	CHANGE COLUMN `strWaiterNo` `strWaiterNo` VARCHAR(50) NOT NULL DEFAULT 'NA' AFTER `strTableNo`, "
		+ "	CHANGE COLUMN `strUserCreated` `strUserCreated` VARCHAR(10) NOT NULL AFTER `intShiftCode`, "
		+ "	CHANGE COLUMN `strKOTNo` `strKOTNo` VARCHAR(10) NOT NULL DEFAULT 'N' AFTER `strDataPostFlag`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblvoidbillhd set strReasonCode='' where strReasonCode is NULL;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblvoidbillhd set strReasonCode='' where strReasonCode is NULL;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblvoidbillhd set strTransType='' where strTransType is NULL;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblvoidbillhd set intShiftCode=0 where intShiftCode is NULL;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblvoidbillhd set strUserCreated='' where strUserCreated is NULL;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblvoidbillhd` "
		+ "	ALTER `dteBillDate` DROP DEFAULT, "
		+ "	ALTER `dteModifyVoidBill` DROP DEFAULT, "
		+ "	ALTER `strUserCreated` DROP DEFAULT;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblvoidbillhd` "
		+ "	CHANGE COLUMN `strReasonCode` `strReasonCode` VARCHAR(10) NOT NULL DEFAULT '' AFTER `strPosCode`, "
		+ "	CHANGE COLUMN `strReasonName` `strReasonName` VARCHAR(50) NOT NULL DEFAULT '' AFTER `strReasonCode`, "
		+ "	CHANGE COLUMN `dteBillDate` `dteBillDate` DATETIME NOT NULL AFTER `dblModifiedAmount`, "
		+ "	CHANGE COLUMN `strTransType` `strTransType` CHAR(10) NOT NULL DEFAULT '' AFTER `dteBillDate`, "
		+ "	CHANGE COLUMN `dteModifyVoidBill` `dteModifyVoidBill` DATETIME NOT NULL AFTER `strTransType`, "
		+ "	CHANGE COLUMN `intShiftCode` `intShiftCode` INT(11) NOT NULL DEFAULT '0' AFTER `strWaiterNo`, "
		+ "	CHANGE COLUMN `strUserCreated` `strUserCreated` VARCHAR(50) NOT NULL AFTER `intShiftCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblvoidkot set strType='' where strType is NULL;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblvoidkot set strReasonCode='' where strReasonCode is NULL;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblvoidkot` "
		+ "	CHANGE COLUMN `strType` `strType` VARCHAR(50) NOT NULL DEFAULT '' AFTER `intPaxNo`, "
		+ "	CHANGE COLUMN `strReasonCode` `strReasonCode` VARCHAR(50) NOT NULL DEFAULT '' AFTER `strType`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblvoidbillsettlementdtl set dblPaidAmt=0.00 where dblPaidAmt is NULL;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblvoidbillsettlementdtl set strExpiryDate='' where strExpiryDate is NULL;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblvoidbillsettlementdtl set strCustomerCode='' where strCustomerCode is NULL;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblvoidbillsettlementdtl set dblActualAmt=0.00 where dblActualAmt is NULL;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblvoidbillsettlementdtl set dblRefundAmt=0.00 where dblRefundAmt is NULL;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblvoidbillsettlementdtl set strGiftVoucherCode='' where strGiftVoucherCode is NULL;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblvoidbillsettlementdtl` "
		+ "	CHANGE COLUMN `dblPaidAmt` `dblPaidAmt` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `dblSettlementAmt`, "
		+ "	CHANGE COLUMN `strExpiryDate` `strExpiryDate` VARCHAR(7) NOT NULL DEFAULT '' AFTER `dblPaidAmt`, "
		+ "	CHANGE COLUMN `strCustomerCode` `strCustomerCode` VARCHAR(10) NOT NULL DEFAULT '' AFTER `strClientCode`, "
		+ "	CHANGE COLUMN `dblActualAmt` `dblActualAmt` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `strCustomerCode`, "
		+ "	CHANGE COLUMN `dblRefundAmt` `dblRefundAmt` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `dblActualAmt`, "
		+ "	CHANGE COLUMN `strGiftVoucherCode` `strGiftVoucherCode` VARCHAR(50) NOT NULL DEFAULT '' AFTER `dblRefundAmt`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblcustomermaster` "
		+ "	ADD COLUMN `strOfficeBuildingCode` VARCHAR(15) NOT NULL DEFAULT '' AFTER `longAlternateMobileNo`, "
		+ "	CHANGE COLUMN `strOfficeBuilding` `strOfficeBuildingName` VARCHAR(200) NOT NULL DEFAULT '' AFTER `strOfficeBuildingCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblhomedelivery` "
		+ "	CHANGE COLUMN `strCustAddressLine1` `strCustAddressLine1` VARCHAR(200) NOT NULL DEFAULT '' AFTER `strPOSCode`, "
		+ "	CHANGE COLUMN `strCustAddressLine2` `strCustAddressLine2` VARCHAR(200) NOT NULL DEFAULT '' AFTER `strCustAddressLine1`, "
		+ "	CHANGE COLUMN `strCustAddressLine3` `strCustAddressLine3` VARCHAR(200) NOT NULL DEFAULT '' AFTER `strCustAddressLine2`, "
		+ "	CHANGE COLUMN `strCustAddressLine4` `strCustAddressLine4` VARCHAR(200) NOT NULL DEFAULT '' AFTER `strCustAddressLine3`, "
		+ "	CHANGE COLUMN `strCustCity` `strCustCity` VARCHAR(30) NOT NULL DEFAULT '' AFTER `strCustAddressLine4`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblitemmaster` "
		+ "	CHANGE COLUMN `strItemImage` `strItemImage` VARCHAR(50) NOT NULL DEFAULT '' AFTER `strSubGroupCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblareawisedc` "
		+ "	CHANGE COLUMN `strSymbol` `strSymbol` VARCHAR(3) NOT NULL DEFAULT '' AFTER `dblKilometers`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblvoidbilldtl` CHANGE COLUMN `strSettlementCode` `strSettlementCode` VARCHAR(10) NOT NULL DEFAULT '' AFTER `dteModifyVoidBill`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblcustomermaster` ADD COLUMN `strGender` VARCHAR(5) NOT NULL DEFAULT 'Male' AFTER `dteDOB`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblcustomermaster` ADD COLUMN `dteAnniversary` VARCHAR(50) NOT NULL DEFAULT '' AFTER `strGender`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblimportexcel` "
		+ "	ADD COLUMN `strCounterName` VARCHAR(50) NOT NULL DEFAULT '' AFTER `dblPriceSaturday`, "
		+ "	ADD COLUMN `strCounterCode` VARCHAR(10) NOT NULL DEFAULT '' AFTER `strCounterName`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblloyaltypoints`"
		+ " ADD COLUMN `dblLoyaltyPoints1` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `dblLoyaltyPoints`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE IF NOT EXISTS `tblpointsonbill` ( "
		+ "	`strBillNo` VARCHAR(10) NOT NULL, "
		+ "	`dblBillAmount` DECIMAL(18,2) NOT NULL, "
		+ "	`dblPointsEarned` DECIMAL(18,2) NOT NULL DEFAULT '0.00', "
		+ "	`strCustomerCode` VARCHAR(10) NOT NULL DEFAULT '', "
		+ "	`strUserCreated` VARCHAR(10) NOT NULL, "
		+ "	`strUserEdited` VARCHAR(10) NOT NULL, "
		+ "	`dteDateCreated` DATETIME NOT NULL, "
		+ "	`dteDateEdited` DATETIME NOT NULL, "
		+ "	`strClientCode` VARCHAR(10) NOT NULL, "
		+ "	`strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N', "
		+ "		PRIMARY KEY (`strBillNo`) "
		+ "	) "
		+ "	COLLATE='latin1_swedish_ci' "
		+ "	ENGINE=InnoDB;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblpointsonbill` "
		+ "	ADD COLUMN `dteBillDate` DATETIME NOT NULL AFTER `strBillNo`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	//create area ALL if not exists
	//funCreateAreaForAll();
	mapStructureUpdater.get("tblStructure").add("funCreateAreaForAll");

	//update area to All where no area exists
	//funUpdateAreaForAll();
	mapStructureUpdater.get("tblStructure").add("funUpdateAreaForAll");

	sql = "ALTER TABLE `tblpromotionmaster` "
		+ "	DROP COLUMN `strBuyPropmoItemCode`, "
		+ "	DROP COLUMN `strBuyOperator`, "
		+ "	DROP COLUMN `dblBuyQty`, "
		+ "	DROP COLUMN `dblBuyDiscount`, "
		+ "	DROP COLUMN `strGetPromoCode`, "
		+ "	DROP COLUMN `strGetOperator`, "
		+ "	DROP COLUMN `dblGetQty`, "
		+ "	DROP COLUMN `dblGetDiscount`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblpromotionmaster` ADD COLUMN `strPromoItemCode` VARCHAR(10) NOT NULL AFTER `strPromoCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE IF NOT EXISTS `tblpromotiondtl` ( "
		+ "	`strPromoCode` VARCHAR(10) NOT NULL, "
		+ "	`strPromoItemCode` VARCHAR(10) NOT NULL, "
		+ "	`strType` VARCHAR(10) NOT NULL, "
		+ "	`strOperator` VARCHAR(10) NOT NULL, "
		+ "	`dblValue` DECIMAL(18,2) NOT NULL DEFAULT '0.00', "
		+ "	`dblQuantity` DECIMAL(18,2) NOT NULL DEFAULT '0.00', "
		+ "	`strDiscountType` VARCHAR(7) NOT NULL DEFAULT 'Percent', "
		+ "	`dblDiscount` DECIMAL(18,2) NOT NULL DEFAULT '0.00', "
		+ "	`strClientCode` VARCHAR(10) NOT NULL "
		+ ") "
		+ "COLLATE='latin1_swedish_ci' "
		+ "ENGINE=InnoDB;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "delete from tblhomedelivery where strbillno is null;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` ADD COLUMN `strSkipWaiterAndPax` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strSlabBasedHDCharges`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tbladvbookbillhd set strUserEdited=strUserCreated where strUserEdited is NULL;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tbladvbookbillhd set dteDateEdited=dteDateCreated where dteDateEdited is NULL;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tbladvbookbillhd set intShiftCode=0 where intShiftCode is NULL;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tbladvancereceiptdtl set dteCheque='0000-00-00 00:00:00';";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tbladvancereceipthd set strUserEdited=strUserCreated where strUserEdited is NULL;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tbladvancereceipthd set dtDateEdited=dtDateCreated where dtDateEdited is NULL;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tbladvancereceipthd set intShiftNo=0 where intShiftNo is NULL;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbladvbookbillhd` "
		+ "	ALTER `strAdvBookingNo` DROP DEFAULT, "
		+ "	ALTER `dteAdvBookingDate` DROP DEFAULT, "
		+ "	ALTER `dteOrderFor` DROP DEFAULT, "
		+ "	ALTER `strPOSCode` DROP DEFAULT, "
		+ "	ALTER `strSettelmentMode` DROP DEFAULT, "
		+ "	ALTER `dblDiscountAmt` DROP DEFAULT, "
		+ "	ALTER `dblDiscountPer` DROP DEFAULT, "
		+ "	ALTER `dblTaxAmt` DROP DEFAULT, "
		+ "	ALTER `dblSubTotal` DROP DEFAULT, "
		+ "	ALTER `dblGrandTotal` DROP DEFAULT, "
		+ "	ALTER `strUserCreated` DROP DEFAULT, "
		+ "	ALTER `strUserEdited` DROP DEFAULT, "
		+ "	ALTER `dteDateCreated` DROP DEFAULT, "
		+ "	ALTER `dteDateEdited` DROP DEFAULT, "
		+ "	ALTER `strClientCode` DROP DEFAULT, "
		+ "	ALTER `strCustomerCode` DROP DEFAULT, "
		+ "	ALTER `intShiftCode` DROP DEFAULT;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbladvbookbillhd` "
		+ "	CHANGE COLUMN `strAdvBookingNo` `strAdvBookingNo` VARCHAR(50) NOT NULL FIRST, "
		+ "	CHANGE COLUMN `dteAdvBookingDate` `dteAdvBookingDate` DATETIME NOT NULL AFTER `strAdvBookingNo`, "
		+ "	CHANGE COLUMN `dteOrderFor` `dteOrderFor` DATETIME NOT NULL AFTER `dteAdvBookingDate`, "
		+ "	CHANGE COLUMN `strPOSCode` `strPOSCode` VARCHAR(50) NOT NULL AFTER `dteOrderFor`, "
		+ "	CHANGE COLUMN `strSettelmentMode` `strSettelmentMode` VARCHAR(50) NOT NULL AFTER `strPOSCode`, "
		+ "	CHANGE COLUMN `dblDiscountAmt` `dblDiscountAmt` DECIMAL(18,2) NOT NULL AFTER `strSettelmentMode`, "
		+ "	CHANGE COLUMN `dblDiscountPer` `dblDiscountPer` DECIMAL(18,2) NOT NULL AFTER `dblDiscountAmt`, "
		+ "	CHANGE COLUMN `dblTaxAmt` `dblTaxAmt` DECIMAL(18,2) NOT NULL AFTER `dblDiscountPer`, "
		+ "	CHANGE COLUMN `dblSubTotal` `dblSubTotal` DECIMAL(18,2) NOT NULL AFTER `dblTaxAmt`, "
		+ "	CHANGE COLUMN `dblGrandTotal` `dblGrandTotal` DECIMAL(18,2) NOT NULL AFTER `dblSubTotal`, "
		+ "	CHANGE COLUMN `strUserCreated` `strUserCreated` VARCHAR(50) NOT NULL AFTER `dblGrandTotal`, "
		+ "	CHANGE COLUMN `strUserEdited` `strUserEdited` VARCHAR(50) NOT NULL AFTER `strUserCreated`, "
		+ "	CHANGE COLUMN `dteDateCreated` `dteDateCreated` DATETIME NOT NULL AFTER `strUserEdited`, "
		+ "	CHANGE COLUMN `dteDateEdited` `dteDateEdited` DATETIME NOT NULL AFTER `dteDateCreated`, "
		+ "	CHANGE COLUMN `strClientCode` `strClientCode` VARCHAR(50) NOT NULL AFTER `dteDateEdited`, "
		+ "	CHANGE COLUMN `strCustomerCode` `strCustomerCode` VARCHAR(50) NOT NULL AFTER `strClientCode`, "
		+ "	CHANGE COLUMN `intShiftCode` `intShiftCode` INT(11) NOT NULL AFTER `strCustomerCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbladvbookbilldtl` "
		+ "	ALTER `strItemCode` DROP DEFAULT, "
		+ "	ALTER `strItemName` DROP DEFAULT, "
		+ "	ALTER `strAdvBookingNo` DROP DEFAULT, "
		+ "	ALTER `dblQuantity` DROP DEFAULT, "
		+ "	ALTER `dblAmount` DROP DEFAULT, "
		+ "	ALTER `dblTaxAmount` DROP DEFAULT, "
		+ "	ALTER `dteAdvBookingDate` DROP DEFAULT, "
		+ "	ALTER `dteOrderFor` DROP DEFAULT, "
		+ "	ALTER `strClientCode` DROP DEFAULT, "
		+ "	ALTER `strCustomerCode` DROP DEFAULT;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbladvbookbilldtl` "
		+ "	CHANGE COLUMN `strItemCode` `strItemCode` VARCHAR(50) NOT NULL FIRST, "
		+ "	CHANGE COLUMN `strItemName` `strItemName` VARCHAR(50) NOT NULL AFTER `strItemCode`, "
		+ "	CHANGE COLUMN `strAdvBookingNo` `strAdvBookingNo` VARCHAR(50) NOT NULL AFTER `strItemName`, "
		+ "	CHANGE COLUMN `dblQuantity` `dblQuantity` DECIMAL(18,2) NOT NULL AFTER `strAdvBookingNo`, "
		+ "	CHANGE COLUMN `dblAmount` `dblAmount` DECIMAL(18,2) NOT NULL AFTER `dblQuantity`, "
		+ "	CHANGE COLUMN `dblTaxAmount` `dblTaxAmount` DECIMAL(18,2) NOT NULL AFTER `dblAmount`, "
		+ "	CHANGE COLUMN `dteAdvBookingDate` `dteAdvBookingDate` DATETIME NOT NULL AFTER `dblTaxAmount`, "
		+ "	CHANGE COLUMN `dteOrderFor` `dteOrderFor` DATE NOT NULL AFTER `dteAdvBookingDate`, "
		+ "	CHANGE COLUMN `strClientCode` `strClientCode` VARCHAR(50) NOT NULL AFTER `dteOrderFor`, "
		+ "	CHANGE COLUMN `strCustomerCode` `strCustomerCode` VARCHAR(50) NOT NULL AFTER `strClientCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbladvbookitemtemp` "
		+ "	ALTER `strSerialno` DROP DEFAULT, "
		+ "	ALTER `strPosCode` DROP DEFAULT, "
		+ "	ALTER `strItemCode` DROP DEFAULT, "
		+ "	ALTER `strItemName` DROP DEFAULT, "
		+ "	ALTER `dblItemQuantity` DROP DEFAULT, "
		+ "	ALTER `dblAmount` DROP DEFAULT, "
		+ "	ALTER `strUserCreated` DROP DEFAULT, "
		+ "	ALTER `strUserEdited` DROP DEFAULT, "
		+ "	ALTER `dteDateCreated` DROP DEFAULT, "
		+ "	ALTER `dteDateEdited` DROP DEFAULT;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbladvbookitemtemp` "
		+ "	CHANGE COLUMN `strSerialno` `strSerialno` DECIMAL(10,0) NOT NULL FIRST, "
		+ "	CHANGE COLUMN `strPosCode` `strPosCode` VARCHAR(50) NOT NULL AFTER `strSerialno`, "
		+ "	CHANGE COLUMN `strItemCode` `strItemCode` VARCHAR(15) NOT NULL AFTER `strPosCode`, "
		+ "	CHANGE COLUMN `strItemName` `strItemName` VARCHAR(50) NOT NULL AFTER `strItemCode`, "
		+ "	CHANGE COLUMN `dblItemQuantity` `dblItemQuantity` VARCHAR(200) NOT NULL AFTER `strItemName`, "
		+ "	CHANGE COLUMN `dblAmount` `dblAmount` DECIMAL(18,2) NOT NULL AFTER `dblItemQuantity`, "
		+ "	CHANGE COLUMN `strUserCreated` `strUserCreated` VARCHAR(50) NOT NULL AFTER `dblAmount`, "
		+ "	CHANGE COLUMN `strUserEdited` `strUserEdited` VARCHAR(50) NOT NULL AFTER `strUserCreated`, "
		+ "	CHANGE COLUMN `dteDateCreated` `dteDateCreated` DATETIME NOT NULL AFTER `strUserEdited`, "
		+ "	CHANGE COLUMN `dteDateEdited` `dteDateEdited` DATETIME NOT NULL AFTER `dteDateCreated`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbladvbooktaxdtl` "
		+ "	ALTER `strAdvBookingNo` DROP DEFAULT, "
		+ "	ALTER `strTaxCode` DROP DEFAULT, "
		+ "	ALTER `dblTaxableAmount` DROP DEFAULT, "
		+ "	ALTER `dblTaxAmount` DROP DEFAULT, "
		+ "	ALTER `strClientCode` DROP DEFAULT;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbladvbooktaxdtl` "
		+ "	CHANGE COLUMN `strAdvBookingNo` `strAdvBookingNo` VARCHAR(15) NOT NULL FIRST, "
		+ "	CHANGE COLUMN `strTaxCode` `strTaxCode` VARCHAR(15) NOT NULL AFTER `strAdvBookingNo`, "
		+ "	CHANGE COLUMN `dblTaxableAmount` `dblTaxableAmount` DECIMAL(18,2) NOT NULL AFTER `strTaxCode`, "
		+ "	CHANGE COLUMN `dblTaxAmount` `dblTaxAmount` DECIMAL(18,2) NOT NULL AFTER `dblTaxableAmount`, "
		+ "	CHANGE COLUMN `strClientCode` `strClientCode` VARCHAR(15) NOT NULL AFTER `dblTaxAmount`, "
		+ "	ADD COLUMN `strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strClientCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbladvbooktaxtemp` "
		+ "	ALTER `strTaxCode` DROP DEFAULT, "
		+ "	ALTER `strTaxName` DROP DEFAULT, "
		+ "	ALTER `dblTaxableAmt` DROP DEFAULT, "
		+ "	ALTER `dblTaxAmt` DROP DEFAULT, "
		+ "	ALTER `strTaxCal` DROP DEFAULT, "
		+ "	ALTER `strItemName` DROP DEFAULT;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbladvbooktaxtemp` "
		+ "	CHANGE COLUMN `strTaxCode` `strTaxCode` VARCHAR(50) NOT NULL FIRST, "
		+ "	CHANGE COLUMN `strTaxName` `strTaxName` VARCHAR(50) NOT NULL AFTER `strTaxCode`, "
		+ "	CHANGE COLUMN `dblTaxableAmt` `dblTaxableAmt` DECIMAL(18,2) NOT NULL AFTER `strTaxName`, "
		+ "	CHANGE COLUMN `dblTaxAmt` `dblTaxAmt` DECIMAL(18,2) NOT NULL AFTER `dblTaxableAmt`, "
		+ "	CHANGE COLUMN `strTaxCal` `strTaxCal` VARCHAR(50) NOT NULL AFTER `dblTaxAmt`, "
		+ "	CHANGE COLUMN `strItemName` `strItemName` VARCHAR(50) NOT NULL AFTER `strTaxCal`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbladvordermodifierdtl` "
		+ "	ALTER `strAdvOrderNo` DROP DEFAULT, "
		+ "	ALTER `strItemCode` DROP DEFAULT, "
		+ "	ALTER `strModifierCode` DROP DEFAULT, "
		+ "	ALTER `strModifierName` DROP DEFAULT, "
		+ "	ALTER `dblQuantity` DROP DEFAULT, "
		+ "	ALTER `dblAmount` DROP DEFAULT, "
		+ "	ALTER `strClientCode` DROP DEFAULT, "
		+ "	ALTER `strCustomerCode` DROP DEFAULT, "
		+ "	ALTER `strUserCreated` DROP DEFAULT, "
		+ "	ALTER `strUserEdited` DROP DEFAULT, "
		+ "	ALTER `dteDateCreated` DROP DEFAULT, "
		+ "	ALTER `dteDateEdited` DROP DEFAULT;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbladvordermodifierdtl` "
		+ "	CHANGE COLUMN `strAdvOrderNo` `strAdvOrderNo` VARCHAR(15) NOT NULL FIRST, "
		+ "	CHANGE COLUMN `strItemCode` `strItemCode` VARCHAR(10) NOT NULL AFTER `strAdvOrderNo`, "
		+ "	CHANGE COLUMN `strModifierCode` `strModifierCode` VARCHAR(4) NOT NULL AFTER `strItemCode`, "
		+ "	CHANGE COLUMN `strModifierName` `strModifierName` VARCHAR(50) NOT NULL AFTER `strModifierCode`, "
		+ "	CHANGE COLUMN `dblQuantity` `dblQuantity` DECIMAL(18,2) NOT NULL AFTER `strModifierName`, "
		+ "	CHANGE COLUMN `dblAmount` `dblAmount` DECIMAL(18,2) NOT NULL AFTER `dblQuantity`, "
		+ "	CHANGE COLUMN `strClientCode` `strClientCode` VARCHAR(10) NOT NULL AFTER `dblAmount`, "
		+ "	CHANGE COLUMN `strCustomerCode` `strCustomerCode` VARCHAR(10) NOT NULL AFTER `strClientCode`, "
		+ "	CHANGE COLUMN `strUserCreated` `strUserCreated` VARCHAR(10) NOT NULL AFTER `strCustomerCode`, "
		+ "	CHANGE COLUMN `strUserEdited` `strUserEdited` VARCHAR(10) NOT NULL AFTER `strUserCreated`, "
		+ "	CHANGE COLUMN `dteDateCreated` `dteDateCreated` DATETIME NOT NULL AFTER `strUserEdited`, "
		+ "	CHANGE COLUMN `dteDateEdited` `dteDateEdited` DATETIME NOT NULL AFTER `dteDateCreated`, "
		+ "	ADD COLUMN `strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `dteDateEdited`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbladvbookbillhd` "
		+ "	ADD COLUMN `strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `intShiftCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbladvbookbilldtl` "
		+ "	ALTER `strItemCode` DROP DEFAULT, "
		+ "	ALTER `strItemName` DROP DEFAULT, "
		+ "	ALTER `strAdvBookingNo` DROP DEFAULT, "
		+ "	ALTER `strClientCode` DROP DEFAULT, "
		+ "	ALTER `strCustomerCode` DROP DEFAULT;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbladvbookbilldtl` "
		+ "	CHANGE COLUMN `strItemCode` `strItemCode` VARCHAR(10) NOT NULL FIRST, "
		+ "	CHANGE COLUMN `strItemName` `strItemName` VARCHAR(100) NOT NULL AFTER `strItemCode`, "
		+ "	CHANGE COLUMN `strAdvBookingNo` `strAdvBookingNo` VARCHAR(15) NOT NULL AFTER `strItemName`, "
		+ "	CHANGE COLUMN `strClientCode` `strClientCode` VARCHAR(10) NOT NULL AFTER `dteOrderFor`, "
		+ "	CHANGE COLUMN `strCustomerCode` `strCustomerCode` VARCHAR(10) NOT NULL AFTER `strClientCode`, "
		+ "	ADD COLUMN `strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strCustomerCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbladvbookbillhd` "
		+ "	ALTER `strAdvBookingNo` DROP DEFAULT, "
		+ "	ALTER `strPOSCode` DROP DEFAULT, "
		+ "	ALTER `strSettelmentMode` DROP DEFAULT, "
		+ "	ALTER `strUserCreated` DROP DEFAULT, "
		+ "	ALTER `strUserEdited` DROP DEFAULT, "
		+ "	ALTER `strClientCode` DROP DEFAULT, "
		+ "	ALTER `strCustomerCode` DROP DEFAULT;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbladvbookbillhd` "
		+ "	CHANGE COLUMN `strAdvBookingNo` `strAdvBookingNo` VARCHAR(15) NOT NULL FIRST, "
		+ "	CHANGE COLUMN `strPOSCode` `strPOSCode` VARCHAR(10) NOT NULL AFTER `dteOrderFor`, "
		+ "	CHANGE COLUMN `strSettelmentMode` `strSettelmentMode` VARCHAR(10) NOT NULL AFTER `strPOSCode`, "
		+ "	CHANGE COLUMN `strUserCreated` `strUserCreated` VARCHAR(10) NOT NULL AFTER `dblGrandTotal`, "
		+ "	CHANGE COLUMN `strUserEdited` `strUserEdited` VARCHAR(10) NOT NULL AFTER `strUserCreated`, "
		+ "	CHANGE COLUMN `strClientCode` `strClientCode` VARCHAR(10) NOT NULL AFTER `dteDateEdited`, "
		+ "	CHANGE COLUMN `strCustomerCode` `strCustomerCode` VARCHAR(10) NOT NULL AFTER `strClientCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbladvancereceiptdtl` "
		+ "	ALTER `strSettlementCode` DROP DEFAULT, "
		+ "	ALTER `strCardNo` DROP DEFAULT, "
		+ "	ALTER `strExpirydate` DROP DEFAULT, "
		+ "	ALTER `strChequeNo` DROP DEFAULT, "
		+ "	ALTER `dteCheque` DROP DEFAULT, "
		+ "	ALTER `strBankName` DROP DEFAULT, "
		+ "	ALTER `strRemark` DROP DEFAULT;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbladvancereceiptdtl` "
		+ "	CHANGE COLUMN `strSettlementCode` `strSettlementCode` VARCHAR(10) NOT NULL AFTER `strReceiptNo`, "
		+ "	CHANGE COLUMN `strCardNo` `strCardNo` VARCHAR(50) NOT NULL AFTER `strSettlementCode`, "
		+ "	CHANGE COLUMN `strExpirydate` `strExpirydate` VARCHAR(50) NOT NULL AFTER `strCardNo`, "
		+ "	CHANGE COLUMN `strChequeNo` `strChequeNo` VARCHAR(50) NOT NULL AFTER `strExpirydate`, "
		+ "	CHANGE COLUMN `dteCheque` `dteCheque` DATE NOT NULL AFTER `strChequeNo`, "
		+ "	CHANGE COLUMN `strBankName` `strBankName` VARCHAR(50) NOT NULL AFTER `dteCheque`, "
		+ "	CHANGE COLUMN `dblAdvDepositesettleAmt` `dblAdvDepositesettleAmt` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `strBankName`, "
		+ "	CHANGE COLUMN `strRemark` `strRemark` VARCHAR(50) NOT NULL AFTER `dblAdvDepositesettleAmt`, "
		+ "	ADD COLUMN `strClientCode` VARCHAR(10) NOT NULL AFTER `dblPaidAmt`, "
		+ "	ADD COLUMN `strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strClientCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbladvancereceipthd` "
		+ "	ALTER `strReceiptNo` DROP DEFAULT, "
		+ "	ALTER `strAdvBookingNo` DROP DEFAULT, "
		+ "	ALTER `strPOSCode` DROP DEFAULT, "
		+ "	ALTER `strSettelmentMode` DROP DEFAULT, "
		+ "	ALTER `dtReceiptDate` DROP DEFAULT, "
		+ "	ALTER `intShiftCode` DROP DEFAULT, "
		+ "	ALTER `strUserCreated` DROP DEFAULT, "
		+ "	ALTER `strUserEdited` DROP DEFAULT, "
		+ "	ALTER `dtDateCreated` DROP DEFAULT, "
		+ "	ALTER `dtDateEdited` DROP DEFAULT;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbladvancereceipthd` "
		+ "	CHANGE COLUMN `strReceiptNo` `strReceiptNo` VARCHAR(15) NOT NULL FIRST, "
		+ "	CHANGE COLUMN `strAdvBookingNo` `strAdvBookingNo` VARCHAR(15) NOT NULL AFTER `strReceiptNo`, "
		+ "	CHANGE COLUMN `strPOSCode` `strPOSCode` VARCHAR(10) NOT NULL AFTER `strAdvBookingNo`, "
		+ "	CHANGE COLUMN `strSettelmentMode` `strSettelmentMode` VARCHAR(10) NOT NULL AFTER `strPOSCode`, "
		+ "	CHANGE COLUMN `dtReceiptDate` `dtReceiptDate` DATE NOT NULL AFTER `strSettelmentMode`, "
		+ "	CHANGE COLUMN `dblAdvDeposite` `dblAdvDeposite` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `dtReceiptDate`, "
		+ "	CHANGE COLUMN `intShiftCode` `intShiftCode` INT(11) NOT NULL AFTER `dblAdvDeposite`, "
		+ "	CHANGE COLUMN `strUserCreated` `strUserCreated` VARCHAR(10) NOT NULL AFTER `intShiftCode`, "
		+ "	CHANGE COLUMN `strUserEdited` `strUserEdited` VARCHAR(10) NOT NULL AFTER `strUserCreated`, "
		+ "	CHANGE COLUMN `dtDateCreated` `dtDateCreated` DATETIME NOT NULL AFTER `strUserEdited`, "
		+ "	CHANGE COLUMN `dtDateEdited` `dtDateEdited` DATETIME NOT NULL AFTER `dtDateCreated`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbladvancereceipthd` "
		+ "	ADD COLUMN `strClientCode` VARCHAR(10) NOT NULL AFTER `dtDateEdited`, "
		+ "	ADD COLUMN `strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strClientCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbladvancebookingtemp` "
		+ "	ALTER `strCustomerCode` DROP DEFAULT, "
		+ "	ALTER `strCustomerName` DROP DEFAULT, "
		+ "	ALTER `strAddrLine1` DROP DEFAULT, "
		+ "	ALTER `strAddrLine2` DROP DEFAULT, "
		+ "	ALTER `strAddrLine3` DROP DEFAULT, "
		+ "	ALTER `strCity` DROP DEFAULT, "
		+ "	ALTER `strState` DROP DEFAULT, "
		+ "	ALTER `intPinCode` DROP DEFAULT, "
		+ "	ALTER `intMobileNo` DROP DEFAULT, "
		+ "	ALTER `strUserCreated` DROP DEFAULT, "
		+ "	ALTER `strUserEdited` DROP DEFAULT, "
		+ "	ALTER `dteUserCreated` DROP DEFAULT, "
		+ "	ALTER `dteUserEdited` DROP DEFAULT;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbladvancebookingtemp` "
		+ "	CHANGE COLUMN `strCustomerCode` `strCustomerCode` VARCHAR(10) NOT NULL FIRST, "
		+ "	CHANGE COLUMN `strCustomerName` `strCustomerName` VARCHAR(100) NOT NULL AFTER `strCustomerCode`, "
		+ "	CHANGE COLUMN `strAddrLine1` `strAddrLine1` VARCHAR(50) NOT NULL AFTER `strCustomerName`, "
		+ "	CHANGE COLUMN `strAddrLine2` `strAddrLine2` VARCHAR(50) NOT NULL AFTER `strAddrLine1`, "
		+ "	CHANGE COLUMN `strAddrLine3` `strAddrLine3` VARCHAR(50) NOT NULL AFTER `strAddrLine2`, "
		+ "	CHANGE COLUMN `strCity` `strCity` VARCHAR(50) NOT NULL AFTER `strAddrLine3`, "
		+ "	CHANGE COLUMN `strState` `strState` VARCHAR(50) NOT NULL AFTER `strCity`, "
		+ "	CHANGE COLUMN `intPinCode` `intPinCode` VARCHAR(50) NOT NULL AFTER `strState`, "
		+ "	CHANGE COLUMN `intMobileNo` `intMobileNo` VARCHAR(50) NOT NULL AFTER `intPinCode`, "
		+ "	CHANGE COLUMN `strUserCreated` `strUserCreated` VARCHAR(10) NOT NULL AFTER `intMobileNo`, "
		+ "	CHANGE COLUMN `strUserEdited` `strUserEdited` VARCHAR(10) NOT NULL AFTER `strUserCreated`, "
		+ "	CHANGE COLUMN `dteUserCreated` `dteUserCreated` DATETIME NOT NULL AFTER `strUserEdited`, "
		+ "	CHANGE COLUMN `dteUserEdited` `dteUserEdited` DATETIME NOT NULL AFTER `dteUserCreated`, "
		+ "	ADD COLUMN `strClientCode` VARCHAR(10) NOT NULL AFTER `dteUserEdited`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbladvbookbillhd` "
		+ "	ADD COLUMN `strMessage` VARCHAR(150) NOT NULL DEFAULT '' AFTER `strCustomerCode`, "
		+ "	ADD COLUMN `strShape` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strMessage`, "
		+ "	ADD COLUMN `strNote` VARCHAR(300) NOT NULL DEFAULT '' AFTER `strShape`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` ADD COLUMN `strDirectKOTPrintMakeKOT` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strSkipWaiterAndPax`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblitemrtemp` "
		+ "	ADD COLUMN `strDelBoyCode` VARCHAR(10) NOT NULL DEFAULT '' AFTER `tdhComboItemYN`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblstkinhd` "
		+ "	ADD COLUMN `strClientCode` VARCHAR(10) NOT NULL AFTER `dteDateEdited`, "
		+ "	ADD COLUMN `strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strClientCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblstkouthd` "
		+ "	ADD COLUMN `strClientCode` VARCHAR(10) NOT NULL AFTER `dteDateEdited`, "
		+ "	ADD COLUMN `strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strClientCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblstkindtl` "
		+ "	ADD COLUMN `strClientCode` VARCHAR(10) NOT NULL AFTER `dblAmount`, "
		+ "	ADD COLUMN `strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strClientCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblstkoutdtl` "
		+ " ADD COLUMN `strClientCode` VARCHAR(10) NOT NULL AFTER  "
		+ "`dblAmount`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbladvancereceipthd` "
		+ "CHANGE COLUMN `strSettelmentMode` `strSettelmentMode` VARCHAR(15) NOT NULL AFTER `strPOSCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblmodifiermaster` ADD COLUMN `strModifierGroupCode` VARCHAR(12) NOT NULL DEFAULT 'NA' AFTER `strDataPostFlag`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbladvbookitemtemp` "
		+ "CHANGE COLUMN `strItemName` `strItemName` VARCHAR(50) NULL DEFAULT NULL AFTER `strItemCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbladvancereceiptdtl` "
		+ "ADD COLUMN `dteInstallment` DATETIME NOT NULL AFTER `strDataPostFlag`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbladvbookbillhd` "
		+ "ADD COLUMN `strDeliveryTime` VARCHAR(10) NOT NULL AFTER `strDataPostFlag`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE IF NOT EXISTS `tblmodifiergrouphd` ( "
		+ "  `strModifierGroupCode` varchar(12) NOT NULL, "
		+ "  `strModifierGroupName` varchar(50) NOT NULL, "
		+ "  `strModifierGroupShortName` varchar(14) NOT NULL, "
		+ "  `strApplyItemLimit` varchar(1) NOT NULL, "
		+ "  `intItemLimit` double NOT NULL, "
		+ "  `strOperational` varchar(3) NOT NULL DEFAULT 'YES', "
		+ "  `strUserCreated` varchar(50) NOT NULL, "
		+ "  `strUserEdited` varchar(50) NOT NULL, "
		+ "  `dteDateCreated` datetime NOT NULL, "
		+ "  `dteDateEdited` datetime NOT NULL, "
		+ "  `strClientCode` varchar(11) NOT NULL DEFAULT '', "
		+ "  `strDataPostFlag` varchar(1) NOT NULL DEFAULT 'N', "
		+ "  PRIMARY KEY (`strModifierGroupCode`,`strClientCode`) "
		+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` ADD COLUMN `strSkipPax` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strDirectKOTPrintMakeKOT`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblcustomermaster` ADD COLUMN `dteDOB` VARCHAR(50) NOT NULL DEFAULT '' AFTER `strCustomerType`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbltablemaster` ADD COLUMN `intSequence` INT NOT NULL DEFAULT '0' AFTER `strDataPostFlag`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblmodifiergrouphd` CHANGE COLUMN `strApplyItemLimit` `strApplyMaxItemLimit` VARCHAR(1) NOT NULL AFTER `strModifierGroupShortName`;	";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblmodifiergrouphd` CHANGE COLUMN `intItemLimit` `intItemMaxLimit` DOUBLE NOT NULL AFTER `strApplyMaxItemLimit`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblmodifiergrouphd` ADD COLUMN `strApplyMinItemLimit` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strDataPostFlag`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblmodifiergrouphd` ADD COLUMN `intItemMinLimit` DOUBLE NOT NULL AFTER `strApplyMinItemLimit`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblmodifiergrouphd` ADD COLUMN `intSequenceNo` INT NOT NULL DEFAULT '0' AFTER `intItemMinLimit`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbladvbookbillhd`	ADD COLUMN `strWaiterNo` VARCHAR(10) NOT NULL DEFAULT '' AFTER `strDeliveryTime`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblcustomermaster` ADD COLUMN `strEmailId` VARCHAR(50) NOT NULL DEFAULT '' AFTER `dteAnniversary`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblimportexcel` "
		+ "	ADD COLUMN `strCustName` VARCHAR(50) NOT NULL DEFAULT '' AFTER `strCounterCode`, "
		+ "	ADD COLUMN `strCustCode` VARCHAR(10) NOT NULL DEFAULT '' AFTER `strCustName`, "
		+ "	ADD COLUMN `strBuildName` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strCustCode`, "
		+ "	ADD COLUMN `strBuildCode` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strBuildName`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblimportexcel` "
		+ "	ADD COLUMN `strBuildingArea` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strBuildCode`, "
		+ "	ADD COLUMN `strTelephoneNo` VARCHAR(40) NOT NULL DEFAULT '' AFTER `strBuildingArea`, "
		+ "	ADD COLUMN `strEmail` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strTelephoneNo`, "
		+ "	ADD COLUMN `strDOB` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strEmail`; ";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbuildingmaster` "
		+ "	ADD COLUMN `strClientCode` VARCHAR(10) NOT NULL AFTER `dblHomeDeliCharge`, "
		+ "	ADD COLUMN `strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strClientCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbuildingmaster` "
		+ "	CHANGE COLUMN `strAddress` `strAddress` VARCHAR(200) NOT NULL DEFAULT '' AFTER `strBuildingName`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbladvbookbillhd` "
		+ " ADD COLUMN `strHomeDelivery` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strWaiterNo`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbladvbookbillhd` "
		+ " ADD COLUMN `dblHomeDelCharges` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `strHomeDelivery`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ "	ADD COLUMN `strLoyaltyOption` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strSkipPax`, "
		+ "	ADD COLUMN `strGetWebserviceURL` VARCHAR(300) NOT NULL DEFAULT '' AFTER `strLoyaltyOption`, "
		+ "	ADD COLUMN `strPostWebserviceURL` VARCHAR(300) NOT NULL DEFAULT '' AFTER `strGetWebserviceURL`, "
		+ "	ADD COLUMN `strOutletUID` VARCHAR(50) NOT NULL DEFAULT '' AFTER `strPostWebserviceURL`, "
		+ "	ADD COLUMN `strPOSID` VARCHAR(15) NOT NULL DEFAULT '' AFTER `strOutletUID`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ "	CHANGE COLUMN `strLoyaltyOption` `strSQYCRNIntf` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strSkipPax`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ "	CHANGE COLUMN `strSQYCRNIntf` `strCRMInterface` VARCHAR(50) NOT NULL DEFAULT '' AFTER `strSkipPax`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup`	ADD COLUMN `strStockInOption` VARCHAR(15) NOT NULL DEFAULT '' AFTER `strPOSID`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblstkoutdtl` ADD COLUMN `strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strClientCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE `tblcrmpoints` ( "
		+ "	`strBillNo` VARCHAR(15) NOT NULL, "
		+ "	`dblPoints` DECIMAL(18,2) NOT NULL DEFAULT '0.00', "
		+ "	`strTransactionId` VARCHAR(100) NOT NULL DEFAULT '', "
		+ "	`strOutletUID` VARCHAR(100) NOT NULL DEFAULT '', "
		+ "	`dblRedeemedAmt` DECIMAL(18,2) NOT NULL DEFAULT '0.00', "
		+ "	`strClientCode` VARCHAR(15) NOT NULL, "
		+ "	`strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' "
		+ ") "
		+ "COLLATE='latin1_swedish_ci' "
		+ "ENGINE=InnoDB;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblcrmpoints` "
		+ "	ADD COLUMN `longCustMobileNo` VARCHAR(15) NOT NULL AFTER `dblRedeemedAmt`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup`	ADD COLUMN `longCustSeries` BIGINT NOT NULL DEFAULT '1' AFTER `strStockInOption`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup`	ALTER `longCustSeries` DROP DEFAULT;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup`	CHANGE COLUMN `longCustSeries` `longCustSeries` BIGINT(20) NOT NULL AFTER `strStockInOption`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblareawisedc` "
		+ "	ADD COLUMN `strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strClientCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbuildingmaster` "
		+ "	ALTER `strClientCode` DROP DEFAULT;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbuildingmaster` "
		+ "	CHANGE COLUMN `strClientCode` `strClientCode` VARCHAR(15) NOT NULL AFTER `dblHomeDeliCharge`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup`	DROP COLUMN `strHOPOS`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE `tblrecipehd` ( "
		+ "	`strRecipeCode` VARCHAR(15) NOT NULL, "
		+ "	`strItemCode` VARCHAR(15) NOT NULL, "
		+ "	`dteFromDate` DATETIME NOT NULL, "
		+ "	`dteToDate` DATETIME NOT NULL, "
		+ "	`strUserCreated` VARCHAR(10) NOT NULL, "
		+ "	`strUserEdited` VARCHAR(10) NOT NULL, "
		+ "	`dteDateCreated` DATETIME NOT NULL, "
		+ "	`dteDateEdited` DATETIME NOT NULL, "
		+ "	`strClientCode` VARCHAR(15) NOT NULL, "
		+ "	`strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' "
		+ ") "
		+ "COLLATE='latin1_swedish_ci' "
		+ "ENGINE=InnoDB;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE `tblrecipedtl` ( "
		+ "	`strRecipeCode` VARCHAR(15) NOT NULL, "
		+ "	`strChildItemCode` VARCHAR(10) NOT NULL, "
		+ "	`dblQuantity` DECIMAL(18,2) NOT NULL DEFAULT '0.00', "
		+ "	`strClientCode` VARCHAR(15) NOT NULL, "
		+ "	`strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' "
		+ ") "
		+ "COLLATE='latin1_swedish_ci' "
		+ "ENGINE=InnoDB;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblloyaltypoints`	ADD COLUMN `dteFromDate` "
		+ "DATETIME NOT NULL AFTER `strDataPostFlag`, "
		+ "ADD COLUMN `dteToDate` DATETIME NOT NULL AFTER `dteFromDate`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE `tblloyaltypointposdtl` ( "
		+ "	`strLoyaltyCode` VARCHAR(15) NOT NULL, "
		+ "	`strPOSCode` VARCHAR(15) NOT NULL, "
		+ "	`strClientCode` VARCHAR(15) NOT NULL, "
		+ "	`strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' "
		+ ") "
		+ "COLLATE='latin1_swedish_ci' "
		+ "ENGINE=InnoDB;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE `tblloyaltypointmenuhddtl` (`strLoyaltyCode` VARCHAR(15) NOT NULL, "
		+ "	`strMenuCode` VARCHAR(15) NOT NULL, "
		+ "	`strClientCode` VARCHAR(15) NOT NULL, "
		+ "	`strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N') "
		+ "	COLLATE='latin1_swedish_ci' ENGINE=InnoDB;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE `tblloyaltypointsubgroupdtl` (	`strLoyaltyCode` VARCHAR(15) NOT NULL, "
		+ "	`strCode` VARCHAR(15) NOT NULL, "
		+ "	`strClientCode` VARCHAR(15) NOT NULL, "
		+ "	`strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N') "
		+ "	COLLATE='latin1_swedish_ci' ENGINE=InnoDB;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE `tblloyaltypointcustomerdtl` (	`strLoyaltyCode` VARCHAR(15) NOT NULL, "
		+ "	`strCustomerTypeCode` VARCHAR(15) NOT NULL, "
		+ "	`strClientCode` VARCHAR(15) NOT NULL, "
		+ "	`strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N') "
		+ "	COLLATE='latin1_swedish_ci' ENGINE=InnoDB;";
	mapStructureUpdater.get("tblStructure").add(sql);

//            sql = "ALTER TABLE `tbltablemaster` ENGINE=MyISAM;";
//            i = ExecuteQuery(sql);
//
//            sql = "ALTER TABLE `tblitemrtemp` ENGINE=MyISAM;";
//            i = ExecuteQuery(sql);
	sql = "ALTER TABLE `tblsetup` "
		+ "CHANGE COLUMN `dteHOServerDate` `dteHOServerDate` VARCHAR(25) "
		+ "NOT NULL DEFAULT '2013-01-01 16:49:15' AFTER `strDataSendFrequency`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblcrmpoints` "
		+ "	ADD COLUMN `dblValue` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `strDataPostFlag`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbldeliverypersonmaster` ALTER `strDPCode` DROP DEFAULT;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbldeliverypersonmaster` "
		+ "	CHANGE COLUMN `strDPCode` `strDPCode` VARCHAR(10) NOT NULL FIRST;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbldeliverypersonmaster` "
		+ "	ALTER `strDPName` DROP DEFAULT;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbldeliverypersonmaster` "
		+ "	CHANGE COLUMN `strDPName` `strDPName` VARCHAR(100) NOT NULL AFTER `strDPCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tbldeliverypersonmaster set strAddressLine1='' "
		+ "where strAddressLine1 is NULL;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tbldeliverypersonmaster set strAddressLine2='' "
		+ "where strAddressLine2 is NULL;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tbldeliverypersonmaster set strAddressLine3='' "
		+ "where strAddressLine3 is NULL;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tbldeliverypersonmaster set strCity='' "
		+ "where strCity is NULL;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tbldeliverypersonmaster set strState='' "
		+ "where strState is NULL;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tbldeliverypersonmaster set intPinCode=0 "
		+ "where intPinCode is NULL;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tbldeliverypersonmaster set intMobileNo=0 "
		+ "where intMobileNo is NULL;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tbldeliverypersonmaster set strDeliveryArea='' "
		+ "where strDeliveryArea is NULL;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbldeliverypersonmaster` "
		+ "	CHANGE COLUMN `strAddressLine1` `strAddressLine1` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strDPName`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbldeliverypersonmaster` "
		+ "	CHANGE COLUMN `strAddressLine2` `strAddressLine2` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strAddressLine1`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbldeliverypersonmaster` "
		+ "	CHANGE COLUMN `strAddressLine3` `strAddressLine3` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strAddressLine2`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbldeliverypersonmaster` "
		+ "	CHANGE COLUMN `strCity` `strCity` VARCHAR(20) NOT NULL DEFAULT '' AFTER `strAddressLine3`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbldeliverypersonmaster` "
		+ "	CHANGE COLUMN `strState` `strState` VARCHAR(50) NOT NULL DEFAULT '' AFTER `strCity`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbldeliverypersonmaster` "
		+ "	CHANGE COLUMN `intPinCode` `intPinCode` BIGINT(20) NOT NULL DEFAULT '0' AFTER `strState`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbldeliverypersonmaster` "
		+ "	CHANGE COLUMN `intMobileNo` `intMobileNo` BIGINT(20) NOT NULL DEFAULT '0' AFTER `intPinCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbldeliverypersonmaster` "
		+ "	ADD COLUMN `strClientCode` VARCHAR(10) NOT NULL AFTER `strOperational`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbldeliverypersonmaster` "
		+ "	ADD COLUMN `strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strClientCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbldeliverypersonmaster` "
		+ "	CHANGE COLUMN `dtDateCreated` `dteDateCreated` DATETIME NULL DEFAULT NULL AFTER `strUserEdited`, "
		+ "	CHANGE COLUMN `dtDateEdited` `dteDateEdited` DATETIME NULL DEFAULT NULL AFTER `dteDateCreated`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblitemmaster` ADD COLUMN `strRawMaterial` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `intProcDay`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblitemcurrentstk` "
		+ " ADD COLUMN `strGroupName` VARCHAR(100) NOT NULL DEFAULT '' FIRST, "
		+ " ADD COLUMN `strSubgroupName` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strGroupName`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblposmaster` "
		+ " ADD COLUMN `strDelayedSettlementForDB` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strCounterWiseBilling`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ "	CHANGE COLUMN `strOutletUID` `strOutletUID` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strPostWebserviceURL`, "
		+ "	CHANGE COLUMN `strPOSID` `strPOSID` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strOutletUID`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblcustomermaster` "
		+ "	ADD COLUMN `strCRMId` VARCHAR(50) NOT NULL DEFAULT '' AFTER `strEmailId`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblcrmpoints` "
		+ "	ADD COLUMN `strCustomerId` VARCHAR(50) NOT NULL DEFAULT '0.00' AFTER `dblValue`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblcrmpoints` "
		+ "	CHANGE COLUMN `strCustomerId` `strCustomerId` VARCHAR(50) NOT NULL DEFAULT '' AFTER `dblValue`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblimportexcel` "
		+ "	ADD COLUMN `strSubMenuHeadName` VARCHAR(50) NOT NULL DEFAULT '' AFTER `strSubGroupCode`, "
		+ "	ADD COLUMN `strSubMenuHeadCode` VARCHAR(10) NOT NULL DEFAULT '' AFTER `strSubMenuHeadName`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbltaxhd` "
		+ "	ADD COLUMN `strAreaCode` VARCHAR(100) NOT NULL DEFAULT '' AFTER `dteDateEdited`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbltaxhd` "
		+ "	ADD COLUMN `strOperationType` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strAreaCode`, "
		+ "	ADD COLUMN `strItemType` VARCHAR(20) NOT NULL DEFAULT '' AFTER `strOperationType`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup`	ALTER `strSenderEmailId` DROP DEFAULT;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup`	CHANGE COLUMN `strSenderEmailId` `strSenderEmailId` VARCHAR(40) NOT NULL AFTER `strMenuItemDispSeq`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblvoidkot` ADD COLUMN `strPrintKOT` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strManualKOTNo`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ "	ADD COLUMN `intAdvReceiptPrintCount` INT NOT NULL DEFAULT '0' AFTER `longCustSeries`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ " ADD COLUMN `strHomeDeliverySMS` VARCHAR(300) NOT NULL AFTER `intAdvReceiptPrintCount`, "
		+ " ADD COLUMN `strBillStettlementSMS` VARCHAR(300) NOT NULL AFTER `strHomeDeliverySMS`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ "	ADD COLUMN `strBillFormatType` VARCHAR(30) NOT NULL DEFAULT '' AFTER `strBillStettlementSMS`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE `tblbillpromotiondtl` ( "
		+ "	`strBillNo` VARCHAR(20) NOT NULL, "
		+ "	`strItemCode` VARCHAR(15) NOT NULL, "
		+ "	`strPromotionCode` VARCHAR(20) NOT NULL, "
		+ "	`dblQuantity` DECIMAL(18,4) NOT NULL, "
		+ "	`dblRate` DECIMAL(18,4) NOT NULL, "
		+ "	`strClientCode` VARCHAR(20) NOT NULL, "
		+ "	`strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' "
		+ ") "
		+ "COLLATE='latin1_swedish_ci' "
		+ "ENGINE=InnoDB;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ "	ADD COLUMN `strActivePromotions` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strBillFormatType`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblpromotionmaster` "
		+ "	ADD COLUMN `strPromotionOn` VARCHAR(30) NOT NULL AFTER `strPromoCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblpromotiondtl` "
		+ "	ADD COLUMN `strPromotionOn` VARCHAR(30) NOT NULL AFTER `strPromoCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbltempvoidkot` ALTER `strItemCode` DROP DEFAULT;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbltempvoidkot` "
		+ "	CHANGE COLUMN `strItemCode` `strItemCode` VARCHAR(20) NOT NULL AFTER `strItemName`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE `tblnonchargablekot` ( "
		+ "	`strTableNo` VARCHAR(10) NOT NULL, "
		+ "	`strItemCode` VARCHAR(20) NOT NULL, "
		+ "	`dblQuantity` DECIMAL(18,4) NOT NULL, "
		+ "	`dblRate` DECIMAL(18,4) NOT NULL, "
		+ "	`strKOTNo` VARCHAR(15) NOT NULL, "
		+ "	`strEligibleForVoid` VARCHAR(1) NOT NULL DEFAULT 'Y', "
		+ "	`strClientCode` VARCHAR(15) NOT NULL, "
		+ "	`strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' "
		+ ") "
		+ "COLLATE='utf8_general_ci' "
		+ "ENGINE=InnoDB;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblitemrtemp` DROP COLUMN `NcKotYN`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblitemrtemp` "
		+ " ADD COLUMN `strNCKotYN` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strDelBoyCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblnonchargablekot` ADD COLUMN `strReasonCode` VARCHAR(10) NOT NULL AFTER `strDataPostFlag`; ";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblnonchargablekot` ADD COLUMN `strRemark` VARCHAR(50) NOT NULL AFTER `strReasonCode`; ";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ "	ADD COLUMN `strSendHomeDelSMS` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strActivePromotions`, "
		+ "	ADD COLUMN `strSendBillSettlementSMS` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strSendHomeDelSMS`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbilldtl` "
		+ "	ADD COLUMN `dblRate` DECIMAL(18,2) NOT NULL AFTER `strAdvBookingNo`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbilldtl` "
		+ "	ADD COLUMN `dblRate` DECIMAL(18,2) NOT NULL AFTER `strAdvBookingNo`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbillmodifierdtl` "
		+ "	ADD COLUMN `dblRate` DECIMAL(18,2) NOT NULL AFTER `strModifierName`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbillmodifierdtl` "
		+ "	ADD COLUMN `dblRate` DECIMAL(18,2) NOT NULL AFTER `strModifierName`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbladvbookitemtemp` ALTER `dblItemQuantity` DROP DEFAULT;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbladvbookitemtemp` "
		+ "	CHANGE COLUMN `dblItemQuantity` `dblItemQuantity` DECIMAL(18,2) NOT NULL AFTER `strItemName`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblreasonmaster` "
		+ "	ADD COLUMN `strNCKOT` VARCHAR(1) NOT NULL AFTER `strDataPostFlag`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblnonchargablekot` "
		+ "	ADD COLUMN `dteNCKOTDate` DATETIME NOT NULL AFTER `strRemark`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblnonchargablekot` "
		+ "	ADD COLUMN `strUserCreated` DATETIME NOT NULL AFTER `dteNCKOTDate`, "
		+ "	ADD COLUMN `strUserEdited` DATETIME NOT NULL AFTER `strUserCreated`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ "	ADD COLUMN `strSMSType` VARCHAR(50) NOT NULL AFTER `strSendBillSettlementSMS`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ "	ADD COLUMN `strPrintShortNameOnKOT` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strSMSType`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblpspdtl` "
		+ "	DROP INDEX `strItemCode`, "
		+ "	DROP INDEX `strPSPCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblpsphd` "
		+ "	DROP PRIMARY KEY, "
		+ "	DROP INDEX `strPOSCode`, "
		+ "	DROP INDEX `strStkInCode`, "
		+ "	DROP INDEX `strStkOutCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblpsphd` "
		+ "	ADD COLUMN `strClientCode` VARCHAR(20) NOT NULL AFTER `dteDateEdited`, "
		+ "	ADD COLUMN `strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strClientCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblpspdtl` "
		+ "	ADD COLUMN `strClientCode` VARCHAR(20) NOT NULL AFTER `dblVairanceAmt`, "
		+ "	ADD COLUMN `strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strClientCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblpspdtl` ALTER `strPSPCode` DROP DEFAULT;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblpspdtl` CHANGE COLUMN `strPSPCode` `strPSPCode` VARCHAR(20) NOT NULL FIRST;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblpsphd` ALTER `strPSPCode` DROP DEFAULT;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblpsphd` CHANGE COLUMN `strPSPCode` `strPSPCode` VARCHAR(20) NOT NULL FIRST;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ "	ADD COLUMN `strShowCustHelp` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strPrintShortNameOnKOT`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblreasonmaster` "
		+ "	CHANGE COLUMN `strNCKOT` `strNCKOT` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strDataPostFlag`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblrecipedtl` "
		+ "	ADD COLUMN `strPOSCode` VARCHAR(20) NOT NULL AFTER `dblQuantity`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblrecipehd` "
		+ "	ADD COLUMN `strPOSCode` VARCHAR(20) NOT NULL AFTER `dteToDate`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ "	ADD COLUMN `strPrintOnVoidBill` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strShowCustHelp`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblstkouthd` ALTER `dtePurchaseBillDate` DROP DEFAULT;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblstkouthd` "
		+ "	CHANGE COLUMN `strPurchaseBillNo` `strPurchaseBillNo` VARCHAR(50) NOT NULL DEFAULT '' AFTER `strReasonCode`, "
		+ "	CHANGE COLUMN `dtePurchaseBillDate` `dtePurchaseBillDate` DATETIME NOT NULL AFTER `strPurchaseBillNo`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblstkouthd set strPurchaseBillNo='' where strPurchaseBillNo is NULL;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblstkouthd set dtePurchaseBillDate=dteStkOutDate where dtePurchaseBillDate is NULL;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblitemmaster` "
		+ "	CHANGE COLUMN `strShortName` `strShortName` VARCHAR(20) NOT NULL DEFAULT '' AFTER `strDiscountApply`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ "	ADD COLUMN `strPostSalesDataToMMS` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strPrintOnVoidBill`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbillsettlementdtl` ADD PRIMARY KEY (`strBillNo`, `strSettlementCode`);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbilltaxdtl` ADD PRIMARY KEY (`strBillNo`, `strTaxCode`);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbillhd` "
		+ " CHANGE COLUMN `strAdvBookingNo` `strAdvBookingNo` VARCHAR(10) NOT NULL DEFAULT '' AFTER `strBillNo`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblbillhd set strAdvBookingNo='' where strAdvBookingNo='0';";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ " ADD COLUMN `strCustAreaMasterCompulsory` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strPostSalesDataToMMS`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbladvbookbillhd` "
		+ "	ADD COLUMN `strOrderType` VARCHAR(10) NOT NULL AFTER `dblHomeDelCharges`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE IF NOT EXISTS `tblproductionhd` ( "
		+ "	`strProductionCode` VARCHAR(20) NOT NULL, "
		+ "	`strPOSCode` VARCHAR(10) NOT NULL, "
		+ "	`dteProductionDate` DATETIME NOT NULL, "
		+ "	`strClose` VARCHAR(1) NOT NULL, "
		+ "	`strUserCreated` VARCHAR(10) NOT NULL, "
		+ "	`strUserEdited` VARCHAR(10) NOT NULL, "
		+ "	`dteDateCreated` DATETIME NOT NULL, "
		+ "	`dteDateEdited` DATETIME NOT NULL, "
		+ "	`strClientCode` VARCHAR(20) NOT NULL, "
		+ "	`dtrDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' "
		+ ") "
		+ "COLLATE='latin1_swedish_ci' "
		+ "ENGINE=InnoDB;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblproductionhd` "
		+ "CHANGE COLUMN `dtrDataPostFlag` `strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strClientCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblproductionhd`	ADD PRIMARY KEY (`strProductionCode`, `strClientCode`);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE IF NOT EXISTS `tblproductiondtl` ( "
		+ "	`strProductionCode` VARCHAR(20) NOT NULL, "
		+ "	`strItemCode` VARCHAR(10) NOT NULL, "
		+ "	`strItemName` VARCHAR(200) NOT NULL, "
		+ "	`dblStock` DECIMAL(18,4) NOT NULL, "
		+ "	`dblReorderQty` DECIMAL(18,4) NOT NULL, "
		+ "	`strClientCode` VARCHAR(20) NOT NULL, "
		+ "	`strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' "
		+ ") "
		+ "COLLATE='latin1_swedish_ci' "
		+ "ENGINE=InnoDB;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblproductiondtl`	ALTER `dblReorderQty` DROP DEFAULT;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblproductiondtl`	CHANGE COLUMN `dblReorderQty` `dblOrderQty` DECIMAL(18,4) NOT NULL AFTER `dblStock`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblproductionhd`	ADD COLUMN `strRemarks` VARCHAR(200) NOT NULL AFTER `strClose`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblitemmaster` ADD COLUMN `dblItemRate` DECIMAL(18,4) NOT NULL DEFAULT '0.00' AFTER `strRawMaterial`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblitemmaster` DROP COLUMN `dblItemRate`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblitemmaster` "
		+ "	ADD COLUMN `dblSalePrice` DECIMAL(18,4) NOT NULL DEFAULT '0.0000' AFTER `strRawMaterial`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ "	ADD COLUMN `strPriceFrom` VARCHAR(20) NOT NULL AFTER `strCustAreaMasterCompulsory`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblitemmaster` "
		+ "	CHANGE COLUMN `strExternalCode` `strExternalCode` VARCHAR(100) NOT NULL DEFAULT '0.00' AFTER `intProcTimeMin`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` ADD COLUMN `strShowPrinterErrorMessage` VARCHAR(1) NOT NULL DEFAULT 'Y' AFTER `strPriceFrom`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ "	CHANGE COLUMN `strPriceFrom` `strPriceFrom` VARCHAR(20) NOT NULL DEFAULT 'Menu Pricing' AFTER `strCustAreaMasterCompulsory`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup`	ADD COLUMN `strTouchScreenMode` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strShowPrinterErrorMessage`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ "	CHANGE COLUMN `strTouchScreenMode` `strTouchScreenMode` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strShowPrinterErrorMessage`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE `tbladvanceordertypemaster` ( "
		+ "	`strAdvOrderTypeCode` VARCHAR(15) NOT NULL, "
		+ "	`strAdvOrderTypeName` VARCHAR(200) NOT NULL, "
		+ "	`strOperational` VARCHAR(3) NOT NULL DEFAULT 'Yes', "
		+ "	`strUserCreated` VARCHAR(10) NOT NULL, "
		+ "	`strUserEdited` VARCHAR(10) NOT NULL, "
		+ "	`dteDateCreated` DATETIME NOT NULL, "
		+ "	`dteDateEdited` DATETIME NOT NULL, "
		+ "	`strClientCode` VARCHAR(20) NOT NULL, "
		+ "	`strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N', "
		+ "	PRIMARY KEY (`strAdvOrderTypeCode`, `strClientCode`) "
		+ ") "
		+ "COLLATE='latin1_swedish_ci' "
		+ "ENGINE=InnoDB;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbladvanceordertypemaster` ADD COLUMN `strPOSCode` VARCHAR(10) NOT NULL AFTER `strOperational`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblcustomertypemaster` "
		+ "	ADD COLUMN `dblDiscPer` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `strCustType`, "
		+ "	ADD COLUMN `strClientCode` VARCHAR(50) NOT NULL AFTER `dteDateEdited`, "
		+ "	ADD COLUMN `strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strClientCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblitemmaster` ADD COLUMN `strItemForSale` VARCHAR(1) NOT NULL DEFAULT 'Y' AFTER `dblSalePrice`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tbladvancereceiptdtl set dteCheque='1980-01-01' where dteCheque='0000-00-00';";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblpromotiondtl` "
		+ "	CHANGE COLUMN `dblQuantity` `dblGetQty` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `strOperator`, "
		+ "	DROP COLUMN `dblValue`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblpromotiondtl` "
		+ "	ADD COLUMN `strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strClientCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblpromotionmaster` "
		+ "	ADD COLUMN `dblBuyQty` DECIMAL(18,2) NOT NULL AFTER `strPromoItemCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbilldtl` ADD COLUMN `strPromoCode` VARCHAR(20) NOT NULL AFTER `tdhYN`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbilldtl` ADD COLUMN `strPromoCode` VARCHAR(20) NOT NULL AFTER `tdhYN`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbilldtl` CHANGE COLUMN `strPromoCode` `strPromoCode` VARCHAR(20) NOT NULL DEFAULT '' AFTER `tdhYN`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbilldtl` CHANGE COLUMN `strPromoCode` `strPromoCode` VARCHAR(20) NOT NULL DEFAULT '' AFTER `tdhYN`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblcustomermaster` ENGINE=MyISAM;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbillhd` ENGINE=MyISAM;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblpromotionmaster` ADD COLUMN `strOperator` VARCHAR(3) NOT NULL AFTER `strPromoItemCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblpromotiondtl` DROP COLUMN `strOperator`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblpromotionmaster` ADD COLUMN `strPromoName` VARCHAR(200) NOT NULL AFTER `strPromoCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblpromotionmaster` ADD COLUMN `strType` VARCHAR(10) NOT NULL AFTER `strDays`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblpromotiondtl` DROP COLUMN `strType`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbltaxhd`	ALTER `strTaxOnTaxCode` DROP DEFAULT;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbltaxhd` "
		+ "	CHANGE COLUMN `strTaxOnTaxCode` `strTaxOnTaxCode` VARCHAR(100) NOT NULL AFTER `strTaxOnTax`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbldebitcardtype` ADD COLUMN `strCustomerCompulsory` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `dblRedemptionLimitValue`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbldebitcardmaster` CHANGE COLUMN `intPassword` `intPassword` INT(4) NOT NULL DEFAULT '0' AFTER `strCustomerCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbldebitcardtype` "
		+ "	ADD COLUMN `strClientCode` VARCHAR(20) NOT NULL AFTER `strCustomerCompulsory`, "
		+ "	ADD COLUMN `strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strClientCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbldebitcardsettlementdtl` ALTER `strApplicable` DROP DEFAULT;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbldebitcardsettlementdtl` "
		+ "CHANGE COLUMN `strApplicable` `strApplicable` VARCHAR(15) NOT NULL AFTER `strSettlementCode`, "
		+ "ADD COLUMN `strClientCode` VARCHAR(15) NOT NULL AFTER `strApplicable`,"
		+ "ADD COLUMN `strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strClientCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbldebitcardmaster` DROP PRIMARY KEY;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbldebitcardmaster` ADD COLUMN `strClientCode` VARCHAR(20) NOT NULL AFTER `strDataPostFlag`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbldebitcardrecharge` ADD COLUMN `strClientCode` VARCHAR(20) NOT NULL AFTER `strDataPostFlag`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbldebitcardrecharge` "
		+ "	CHANGE COLUMN `dblRedeemableAmount` `dblRedeemableAmount` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `dblRechargeAmount`, "
		+ "	CHANGE COLUMN `strRechargeSettlmentMode` `strRechargeSettlmentMode` VARCHAR(10) NOT NULL DEFAULT '' AFTER `dblRedeemableAmount`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblcrmpoints` ADD COLUMN `dteBillDate` DATETIME NOT NULL AFTER `strCustomerId`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbillhd` ADD COLUMN `strCouponCode` VARCHAR(100) NOT NULL DEFAULT '' AFTER `dblDeliveryCharges`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbillhd` ADD COLUMN `strCouponCode` VARCHAR(100) NOT NULL DEFAULT '' AFTER `dblDeliveryCharges`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbluserhd` DROP COLUMN `dtCreatedDate` ;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` ADD COLUMN `strCardInterfaceType` VARCHAR(50) NOT NULL AFTER `strTouchScreenMode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` ADD COLUMN `strCMSIntegrationYN` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strCardInterfaceType`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` ADD COLUMN `strCMSWebServiceURL` VARCHAR(255) NOT NULL DEFAULT '' AFTER `strCMSIntegrationYN`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ "	ADD COLUMN `strChangeQtyForExternalCode` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strCMSWebServiceURL`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ "	ADD COLUMN `strPointsOnBillPrint` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strChangeQtyForExternalCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup`	ADD COLUMN `strCMSPOSCode` VARCHAR(20) NOT NULL DEFAULT '' AFTER `strPointsOnBillPrint`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblitemrtemp` ADD COLUMN `strCustomerName` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strNCKotYN`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblitemrtemp` "
		+ "	ADD COLUMN `strActiveYN` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strCustomerName`, "
		+ "	ADD COLUMN `dblBalance` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `strActiveYN`, "
		+ "	ADD COLUMN `dblCreditLimit` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `dblBalance`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbltablemaster`	ADD COLUMN `strPOSCode` VARCHAR(20) NOT NULL AFTER `intSequence`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbltablemaster`	CHANGE COLUMN `strPOSCode` `strPOSCode` VARCHAR(20) NOT NULL DEFAULT 'P01' AFTER `intSequence`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbladvbookbillhd`	ADD PRIMARY KEY (`strAdvBookingNo`);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblcustomermaster` ADD PRIMARY KEY (`strCustomerCode`);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbldeliverypersonmaster` ADD PRIMARY KEY (`strDPCode`);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblstorelastbill`	ADD PRIMARY KEY (`strPosCode`);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblareamaster` ADD COLUMN `strPOSCode` VARCHAR(20) NOT NULL DEFAULT 'P01' AFTER `strDataPostFlag`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbladvbookbillhd`	ADD COLUMN `strManualAdvOrderNo` VARCHAR(20) NOT NULL DEFAULT '' AFTER `strOrderType`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup`	ADD COLUMN `strManualAdvOrderNoCompulsory` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strCMSPOSCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup`	ADD COLUMN `strPrintManualAdvOrderNoOnBill` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strManualAdvOrderNoCompulsory`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbilldtl` CHANGE COLUMN `dblRate` `dblRate` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `strAdvBookingNo`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbillmodifierdtl` "
		+ "	CHANGE COLUMN `dblRate` `dblRate` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `strModifierName`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbillpromotiondtl`	CHANGE COLUMN `dblRate` `dblRate` DECIMAL(18,4) NOT NULL DEFAULT '0.00' AFTER `dblQuantity`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbilldtl` CHANGE COLUMN `dblRate` `dblRate` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `strAdvBookingNo`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbillmodifierdtl` CHANGE COLUMN `dblRate` `dblRate` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `strModifierName`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblnonchargablekot` ADD COLUMN `strPOSCode` VARCHAR(10) NOT NULL AFTER `strUserEdited`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblitemmaster` ADD COLUMN `strRevenueHead` VARCHAR(50) NOT NULL DEFAULT 'Food' AFTER `strItemForSale`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup`	ADD COLUMN `strPrintModifierQtyOnKOT` VARCHAR(1) NOT NULL DEFAULT 'Y' AFTER `strPrintManualAdvOrderNoOnBill`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblposmaster` "
		+ "ADD COLUMN `strBillPrinterPort` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strDelayedSettlementForDB`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbillmodifierdtl` CHANGE COLUMN `strModifierName` `strModifierName` VARCHAR(100) NOT NULL AFTER `strModifierCode`";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup`	ADD COLUMN `strNoOfLinesInKOTPrint` VARCHAR(100) NOT NULL DEFAULT '5' AFTER `strPrintModifierQtyOnKOT`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblitemrtemp`	ALTER `strTableNo` DROP DEFAULT;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblitemrtemp`	"
		+ "CHANGE COLUMN `strTableNo` `strTableNo` VARCHAR(10) NOT NULL AFTER `strSerialNo`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbillhd` CHANGE COLUMN `strTableNo` `strTableNo` VARCHAR(10) NOT NULL DEFAULT '' AFTER `strClientCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbillhd` CHANGE COLUMN `strTableNo` `strTableNo` VARCHAR(10) NOT NULL DEFAULT '' AFTER `strClientCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblvoidkot` 	ALTER `strTableNo` DROP DEFAULT;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblvoidkot`	CHANGE COLUMN `strTableNo` `strTableNo` VARCHAR(10) NOT NULL FIRST;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` ADD COLUMN `strMultipleKOTPrintYN` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strNoOfLinesInKOTPrint`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbltempvoidkot`	ALTER `strTableNo` DROP DEFAULT;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbltempvoidkot`	CHANGE COLUMN `strTableNo` `strTableNo` VARCHAR(10) NOT NULL AFTER `dblItemQuantity`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblvoidbillhd` CHANGE COLUMN `strTableNo` `strTableNo` VARCHAR(10) NOT NULL DEFAULT '' AFTER `dteModifyVoidBill`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "grant all on *.* to 'root'@'%' identified by 'root' with grant option;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbladvbookbillhd` "
		+ "	ADD COLUMN `strImageName` VARCHAR(50) NOT NULL DEFAULT '' AFTER `strManualAdvOrderNo`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbladvbookbillhd` "
		+ "ADD COLUMN `strSpecialsymbolImage` VARCHAR(50) NOT NULL DEFAULT '' AFTER `strImageName`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbluserhd` "
		+ " CHANGE COLUMN `strPOSAccess` `strPOSAccess` VARCHAR(255) NOT NULL AFTER `dteValidDate`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ " ADD COLUMN `strItemQtyNumpad` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strMultipleKOTPrintYN`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbldayendprocess` "
		+ "	ADD COLUMN `intNoOfNCKOT` INT(50) NOT NULL DEFAULT '0' AFTER `strDataPostFlag`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbldayendprocess` "
		+ "	ADD COLUMN `intNoOfComplimentaryKOT` INT(50) NOT NULL DEFAULT '0' AFTER `intNoOfNCKOT`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbldayendprocess` "
		+ "	ADD COLUMN `intNoOfVoidKOT` INT(50) NOT NULL DEFAULT '0' AFTER `intNoOfComplimentaryKOT`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblpsphd` "
		+ "	CHANGE COLUMN `strStkInCode` `strStkInCode` VARCHAR(9) NOT NULL AFTER `strPOSCode`, "
		+ "	CHANGE COLUMN `strStkOutCode` `strStkOutCode` VARCHAR(9) NOT NULL AFTER `strStkInCode`; "
		+ "";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup`	"
		+ "ADD COLUMN `strTreatMemberAsTable` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strItemQtyNumpad`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ " ADD COLUMN `strKOTToLocalPrinter` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strTreatMemberAsTable`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ "	ADD COLUMN `blobReportImage` LONGBLOB NULL DEFAULT NULL AFTER `strKOTToLocalPrinter`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE `tblareawisedelboywisecharges` ( "
		+ "	`strCustAreaCode` VARCHAR(20) NOT NULL, "
		+ "	`strDeliveryBoyCode` VARCHAR(20) NOT NULL, "
		+ "	`dblValue` DECIMAL(18,2) NOT NULL, "
		+ "	`strUserCreated` VARCHAR(10) NOT NULL, "
		+ "	`strUserEdited` VARCHAR(10) NOT NULL, "
		+ "	`dteDateCreated` DATETIME NOT NULL, "
		+ "	`dteDateEdited` DATETIME NOT NULL, "
		+ "	`strClientCode` VARCHAR(10) NOT NULL, "
		+ "	`strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' "
		+ ") "
		+ "COLLATE='utf8_general_ci' "
		+ "ENGINE=InnoDB "
		+ ";";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE `tbldeliveryboycategorymaster` ( "
		+ "	`strDelBoyCategoryCode` VARCHAR(10) NOT NULL, "
		+ "	`strDelBoyCategoryName` VARCHAR(100) NOT NULL, "
		+ "	`strUserCreated` VARCHAR(10) NOT NULL, "
		+ "	`strUserEdited` VARCHAR(10) NOT NULL, "
		+ "	`dteDateCreated` DATETIME NOT NULL, "
		+ "	`dteDateEdited` DATETIME NOT NULL, "
		+ "	`strClientCode` VARCHAR(10) NOT NULL, "
		+ "	`strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' "
		+ ") "
		+ "COLLATE='utf8_general_ci' "
		+ "ENGINE=InnoDB "
		+ ";";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbldeliverypersonmaster` "
		+ " ADD COLUMN `dblIncentiveAmt` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `strDataPostFlag`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbldeliverypersonmaster` "
		+ "	ADD COLUMN `strDBCategoryCode` VARCHAR(10) NOT NULL AFTER `dblIncentiveAmt`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbldeliveryboycategorymaster` "
		+ "	CHANGE COLUMN `strDelBoyCategoryCode` `strDelBoyCategoryCode` VARCHAR(20) NOT NULL FIRST;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblareawisedc` "
		+ "	ADD COLUMN `strDelBoyCategoryCode` VARCHAR(10) NOT NULL DEFAULT '' AFTER `strDataPostFlag`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbldebitcardbilldetails`	DROP COLUMN `strPSPCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE `tblcustareatypemaster` ( "
		+ "	`strCustAreaTypeCode` VARCHAR(10) NOT NULL, "
		+ "	`strCustAreaTypeName` VARCHAR(100) NOT NULL, "
		+ "	`strUserCreated` VARCHAR(10) NOT NULL, "
		+ "	`strUserEdited` VARCHAR(10) NOT NULL, "
		+ "	`dteDateCreated` DATETIME NOT NULL, "
		+ "	`dteDateEdited` DATETIME NOT NULL, "
		+ "	`strClientCode` VARCHAR(10) NOT NULL, "
		+ "	`strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' "
		+ ") "
		+ "COLLATE='utf8_general_ci' "
		+ "ENGINE=InnoDB "
		+ ";";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsubgrouphd` "
		+ "	ADD COLUMN `strIncentives` DECIMAL(18,4) NOT NULL DEFAULT '0.00' AFTER `strDataPostFlag`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ "	ADD COLUMN `strSettleBtnForDirectBillerBill` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `blobReportImage`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ "	ADD COLUMN `strDelBoySelCompulsoryOnDirectBiller` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strSettleBtnForDirectBillerBill`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "Create table IF NOT EXISTS tblitempricingauditdtl like tblmenuitempricingdtl;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbldeliverypersonmaster` "
		+ " CHANGE COLUMN `strDBCategoryCode` `strDBCategoryCode` VARCHAR(10) NOT NULL DEFAULT '' AFTER `dblIncentiveAmt`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsubgrouphd` "
		+ " ADD COLUMN `strIncentives` VARCHAR(10) NOT NULL AFTER `strGroupCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblitemcurrentstk` "
		+ " CHANGE COLUMN `intOpening` `intOpening` DECIMAL(18,2) NOT NULL DEFAULT '0' AFTER `strPOSCode`, "
		+ " CHANGE COLUMN `intOut` `intOut` DECIMAL(18,2) NOT NULL DEFAULT '0' AFTER `intOpening`, "
		+ " CHANGE COLUMN `intIn` `intIn` DECIMAL(18,2) NOT NULL DEFAULT '0' AFTER `intOut`, "
		+ " CHANGE COLUMN `intSale` `intSale` DECIMAL(18,2) NOT NULL DEFAULT '0' AFTER `intIn`, "
		+ " CHANGE COLUMN `intBalance` `intBalance` DECIMAL(18,2) NOT NULL DEFAULT '0' AFTER `intSale`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "Create table IF NOT EXISTS tblqadvancereceiptdtl like tbladvancereceiptdtl; ";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "Create table IF NOT EXISTS tblqadvancereceipthd like tbladvancereceipthd;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "Create table IF NOT EXISTS tblqadvbookbilldtl like tbladvbookbilldtl;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "Create table IF NOT EXISTS tblqadvbookbillhd like tbladvbookbillhd;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CHANGE COLUMN `strItemDetails` `strItemDetails` VARCHAR(200) NOT NULL DEFAULT '0.00' AFTER `strExternalCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblareawisedc`	DROP COLUMN `strDelBoyCategoryCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblareawisedc` "
		+ "	ADD COLUMN `strCustAreaTypeCode` VARCHAR(10) NOT NULL DEFAULT '' AFTER `strDataPostFlag`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ "	ADD COLUMN `strCMSMemberForKOTJPOS` VARCHAR(1) NOT NULL DEFAULT 'Y' AFTER `strDelBoySelCompulsoryOnDirectBiller`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ "	ADD COLUMN `strCMSMemberForKOTMPOS` VARCHAR(1) NOT NULL DEFAULT 'Y' AFTER `strCMSMemberForKOTJPOS`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblareawisedc` "
		+ "	ADD COLUMN `strCustTypeCode` VARCHAR(10) NOT NULL DEFAULT '' AFTER `strCustAreaTypeCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tbltablemaster set strPOSCode='P01' "
		+ "where strPOSCode=''";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbillhd` "
		+ " ADD COLUMN `strAreaCode` VARCHAR(10) NOT NULL DEFAULT '' AFTER `strCouponCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbillhd` "
		+ " ADD COLUMN `strAreaCode` VARCHAR(10) NOT NULL DEFAULT '' AFTER `strCouponCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblreasonmaster`	ADD COLUMN `strVoidAdvOrder` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strNCKOT`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblcustomermaster` "
		+ "	CHANGE COLUMN `strGender` `strGender` VARCHAR(7) NOT NULL DEFAULT 'Male' AFTER `dteDOB`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbltempsalesflash` "
		+ "	ADD COLUMN `dblRate` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `struser`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbluserhd` "
		+ "	ADD COLUMN `imgUserIcon` LONGBLOB NOT NULL AFTER `strDataPostFlag`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbluserhd` "
		+ "	ADD COLUMN `strImgUserIconPath` VARCHAR(500) NOT NULL AFTER `imgUserIcon`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbltempsalesflash` ALTER `strcode` DROP DEFAULT;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbltempsalesflash` CHANGE COLUMN `strcode` `strcode` VARCHAR(50) NOT NULL FIRST;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblitemrtemp`	ADD COLUMN `strCounterCode` VARCHAR(3) NOT NULL DEFAULT '' AFTER `dblCreditLimit`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbilldtl`	ADD COLUMN `strCounterCode` VARCHAR(3) NOT NULL DEFAULT '' AFTER `strPromoCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbilldtl`	ADD COLUMN `strCounterCode` VARCHAR(3) NOT NULL DEFAULT '' AFTER `strPromoCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbuildingmaster` "
		+ "	ADD COLUMN `dblDeliveryBoyPayOut` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `strDataPostFlag`, "
		+ "	ADD COLUMN `dblHelperPayOut` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `dblDeliveryBoyPayOut`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblitempricingauditdtl` DROP PRIMARY KEY;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblitempricingauditdtl` ALTER `longPricingId` DROP DEFAULT;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblitempricingauditdtl` "
		+ "	CHANGE COLUMN `longPricingId` `longPricingId` BIGINT(20) NOT NULL AFTER `strHourlyPricing`, "
		+ "	DROP PRIMARY KEY;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblcustareatypemaster` ALTER `strCustAreaTypeCode` DROP DEFAULT,"
		+ "ALTER `strCustAreaTypeName` DROP DEFAULT;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblcustareatypemaster` "
		+ "CHANGE COLUMN `strCustAreaTypeCode` `strZoneCode` VARCHAR(10) NOT NULL FIRST, "
		+ "CHANGE COLUMN `strCustAreaTypeName` `strZoneName` VARCHAR(100) NOT NULL AFTER `strZoneCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "RENAME TABLE `tblcustareatypemaster` TO `tblzonemaster`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "DELETE FROM `jpos`.`tblinternal` WHERE  `strTransactionType`='CustAreaType' AND `dblLastNo`=0 LIMIT 1;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbuildingmaster` ADD COLUMN `strZoneCode` VARCHAR(10) NOT NULL AFTER `dblHelperPayOut`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblareawisedc` DROP COLUMN `strCustAreaTypeCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE IF NOT EXISTS `tblhomedeldtl` ( "
		+ "	`strBillNo` VARCHAR(10) NOT NULL, "
		+ "	`strDPCode` VARCHAR(10) NOT NULL, "
		+ "	`strClientCode` VARCHAR(10) NOT NULL, "
		+ "	`strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' "
		+ ") "
		+ "COLLATE='latin1_swedish_ci' "
		+ "ENGINE=InnoDB "
		+ ";";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ "	ADD COLUMN `strDontShowAdvOrderInOtherPOS` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strCMSMemberForKOTMPOS`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ "	ADD COLUMN `strPrintZeroAmtModifierInBill` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strDontShowAdvOrderInOtherPOS`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "Create table IF NOT EXISTS tblqadvordermodifierdtl like tbladvordermodifierdtl;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbilldtl` "
		+ "	ADD COLUMN `strWaiterNo` VARCHAR(10) NOT NULL DEFAULT '' AFTER `strCounterCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbilldtl` "
		+ "	ADD COLUMN `strWaiterNo` VARCHAR(10) NOT NULL DEFAULT '' AFTER `strCounterCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE IF NOT EXISTS `tbltempadvorderflash` ( "
		+ "	`strAdvBookingNo` VARCHAR(10) NOT NULL, "
		+ "	`dteAdvBookingDate` DATETIME NOT NULL, "
		+ "	`dteOrderForDate` DATETIME NOT NULL, "
		+ "	`strPOSCode` VARCHAR(10) NOT NULL, "
		+ "	`strSettlementMode` VARCHAR(10) NOT NULL, "
		+ "	`dblGrandTotal` DECIMAL(18,2) NOT NULL, "
		+ "	`dblAdvDeposite` DECIMAL(18,2) NOT NULL, "
		+ "	`dblBalance` DECIMAL(18,2) NOT NULL, "
		+ "	`strWaiterName` VARCHAR(50) NOT NULL, "
		+ "	`strAdvOrderType` VARCHAR(50) NOT NULL, "
		+ "	`strOperationType` VARCHAR(50) NOT NULL, "
		+ "	`strBillNo` VARCHAR(20) NOT NULL, "
		+ "	`dblSettlementAmt` DECIMAL(18,2) NOT NULL, "
		+ "	`strUserCode` VARCHAR(10) NOT NULL, "
		+ "	`strClientCode` VARCHAR(10) NOT NULL "
		+ ") "
		+ "COLLATE='latin1_swedish_ci' "
		+ "ENGINE=InnoDB "
		+ ";";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbltempadvorderflash`"
		+ "ALTER `dblGrandTotal` DROP DEFAULT,"
		+ "ALTER `dblAdvDeposite` DROP DEFAULT,ALTER `dblBalance` DROP DEFAULT,"
		+ "ALTER `dblSettlementAmt` DROP DEFAULT;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbltempadvorderflash` "
		+ "	CHANGE COLUMN `dblGrandTotal` `dblGrandTotal` VARCHAR(50) NOT NULL AFTER `strSettlementMode`, "
		+ "	CHANGE COLUMN `dblAdvDeposite` `dblAdvDeposite` VARCHAR(50) NOT NULL AFTER `dblGrandTotal`, "
		+ "	CHANGE COLUMN `dblBalance` `dblBalance` VARCHAR(50) NOT NULL AFTER `dblAdvDeposite`, "
		+ "	CHANGE COLUMN `dblSettlementAmt` `dblSettlementAmt` VARCHAR(50) NOT NULL AFTER `strBillNo`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblposmaster`"
		+ "	ADD COLUMN `strAdvReceiptPrinterPort` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strBillPrinterPort`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE IF NOT EXISTS `tblreservation` ( "
		+ "	`strResCode` VARCHAR(10) NOT NULL, "
		+ "	`strCustomerCode` VARCHAR(10) NOT NULL, "
		+ "	`intPax` INT NOT NULL DEFAULT '0', "
		+ "	`dteResDate` DATE NOT NULL, "
		+ "	`tmeResTime` TIME NOT NULL, "
		+ "	`strSpecialInfo` VARCHAR(500) NOT NULL DEFAULT '', "
		+ "	`strUserCreated` VARCHAR(10) NOT NULL, "
		+ "	`strUserEdited` VARCHAR(10) NOT NULL, "
		+ "	`dteDateCreated` DATETIME NOT NULL, "
		+ "	`dteDateEdited` DATETIME NOT NULL, "
		+ "	`strClientCode` VARCHAR(10) NOT NULL, "
		+ "	`strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N', "
		+ "	PRIMARY KEY (`strResCode`, `strClientCode`), "
		+ "	INDEX `strCustomerCode` (`strCustomerCode`) "
		+ ") "
		+ "COLLATE='utf8_general_ci' "
		+ "ENGINE=InnoDB "
		+ ";";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblreservation` "
		+ "	ADD COLUMN `strSmoking` VARCHAR(5) NOT NULL DEFAULT 'N' AFTER `intPax`, "
		+ "	ADD COLUMN `strTableNo` VARCHAR(10) NOT NULL DEFAULT '' AFTER `strSpecialInfo`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblcostcentermaster` "
		+ " ADD COLUMN `strSecondaryPrinterPort` VARCHAR(100) NOT NULL AFTER `strPrinterPort`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup`"
		+ " ADD COLUMN `strPrintKOTYN` VARCHAR(1) NOT NULL DEFAULT 'Y' AFTER `strPrintZeroAmtModifierInBill`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ "	ADD COLUMN `strCreditCardSlipNoCompulsoryYN` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strPrintKOTYN`, "
		+ "	ADD COLUMN `strCreditCardExpiryDateCompulsoryYN` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strCreditCardSlipNoCompulsoryYN`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblwaitermaster` "
		+ "	ADD COLUMN `strOperational` VARCHAR(1) NOT NULL DEFAULT 'Y' AFTER `strStatus`, "
		+ "	ADD COLUMN `strDebitCardString` VARCHAR(200) NOT NULL DEFAULT '' AFTER `strOperational`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbillhd` ENGINE=InnoDB;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblitemrtemp` "
		+ "	ADD COLUMN `strPromoCode` VARCHAR(10) NOT NULL DEFAULT '' AFTER `strCounterCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbillhd` "
		+ " ADD COLUMN `strDiscountRemark` VARCHAR(200) NOT NULL DEFAULT '' AFTER `strAreaCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbillhd` "
		+ " ADD COLUMN `strDiscountRemark` VARCHAR(200) NOT NULL DEFAULT '' AFTER `strAreaCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblvoidkot` "
		+ "	ADD COLUMN `strRemark` VARCHAR(200) NOT NULL DEFAULT 'N' AFTER `strPrintKOT`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	// sql = "ALTER TABLE `tblsetup` DROP COLUMN `strSelectWaiterFromCardSwipe`;";
	// i = ExecuteQuery(sql);
	sql = "ALTER TABLE `tblsetup` "
		+ "	ADD COLUMN `strSelectWaiterFromCardSwipe` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strCreditCardExpiryDateCompulsoryYN`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup`	"
		+ " ADD COLUMN `strMultiWaiterSelectionOnMakeKOT` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strSelectWaiterFromCardSwipe`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ "	ADD COLUMN `strMoveTableToOtherPOS` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strMultiWaiterSelectionOnMakeKOT`, "
		+ "	ADD COLUMN `strMoveKOTToOtherPOS` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strMoveTableToOtherPOS`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblreservation` "
		+ "ADD COLUMN `strAMPM` VARCHAR(2) NOT NULL AFTER `tmeResTime`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblvoidbillhd` "
		+ " ADD COLUMN `strRemark` VARCHAR(200) NOT NULL DEFAULT '' AFTER `strDataPostFlag`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ "	ADD COLUMN `strCalculateTaxOnMakeKOT` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strMoveKOTToOtherPOS`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbillmodifierdtl` "
		+ "	ALTER `strModifierName` DROP DEFAULT;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbillmodifierdtl`"
		+ "	CHANGE COLUMN `strModifierName` `strModifierName` VARCHAR(100) NOT NULL AFTER `strModifierCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup`	ADD COLUMN `strReceiverEmailId` VARCHAR(500) NOT NULL DEFAULT '' AFTER `strCalculateTaxOnMakeKOT`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "  ALTER TABLE `tblcostcentermaster` "
		+ "	ADD COLUMN `strPrintOnBothPrinters` VARCHAR(5) NOT NULL DEFAULT 'N' AFTER `strSecondaryPrinterPort`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE `tbltempsalesflash1` ( "
		+ "	`strbillno` VARCHAR(100) NOT NULL, "
		+ "	`dtebilldate` VARCHAR(100) NOT NULL, "
		+ "	`tmebilltime` VARCHAR(100) NOT NULL, "
		+ "	`strtablename` VARCHAR(100) NOT NULL, "
		+ "	`strposcode` VARCHAR(100) NOT NULL, "
		+ "	`strpaymode` VARCHAR(100) NOT NULL, "
		+ "	`dblsubtotal` VARCHAR(100) NOT NULL, "
		+ "	`dbldiscper` VARCHAR(100) NOT NULL, "
		+ "	`dbldiscamt` VARCHAR(100) NOT NULL, "
		+ "	`dbltaxamt` VARCHAR(100) NOT NULL, "
		+ "	`dblsettlementamt` VARCHAR(100) NOT NULL, "
		+ "	`strusercreated` VARCHAR(100) NOT NULL, "
		+ "	`struseredited` VARCHAR(100) NOT NULL, "
		+ "	`dtedatecreated` VARCHAR(100) NOT NULL, "
		+ "	`dtedateedited` VARCHAR(100) NOT NULL, "
		+ "	`strclientcode` VARCHAR(100) NOT NULL, "
		+ "	`strwaiterno` VARCHAR(100) NOT NULL, "
		+ "	`strcustomercode` VARCHAR(100) NOT NULL, "
		+ "	`dbldeliverycharges` VARCHAR(100) NOT NULL, "
		+ "	`strremarks` VARCHAR(200) NOT NULL, "
		+ "	`strcustomername` VARCHAR(100) NOT NULL, "
		+ "	`dbltipamt` VARCHAR(100) NOT NULL "
		+ ") "
		+ "COLLATE='utf8_general_ci' "
		+ "ENGINE=InnoDB "
		+ ";";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbltempsalesflash1` "
		+ "	ADD COLUMN `strUser` VARCHAR(100) NOT NULL AFTER `dbltipamt`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE `tbltempsalesflashtotals1` ( "
		+ "	`dblsubtotal` VARCHAR(100) NOT NULL, "
		+ "	`dbltaxamt` VARCHAR(100) NOT NULL, "
		+ "	`dbldiscamt` VARCHAR(100) NOT NULL, "
		+ "	`dblsettlementamt` VARCHAR(100) NOT NULL "
		+ ") "
		+ "COLLATE='latin1_swedish_ci' "
		+ "ENGINE=InnoDB "
		+ ";";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbltempsalesflashtotals1` "
		+ "	ADD COLUMN `strUser` VARCHAR(100) NOT NULL AFTER `dblsettlementamt`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbillhd` ADD INDEX `strBillNo` (`strBillNo`);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbillhd` ADD INDEX `strBillNo` (`strBillNo`);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbillsettlementdtl` ADD PRIMARY KEY (`strBillNo`, `strSettlementCode`);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbillsettlementdtl` ADD PRIMARY KEY (`strBillNo`, `strSettlementCode`);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbillmodifierdtl` ADD INDEX `strBillNo` (`strBillNo`);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbilltaxdtl` ADD PRIMARY KEY (`strBillNo`, `strTaxCode`);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbilltaxdtl`	ADD PRIMARY KEY (`strBillNo`, `strTaxCode`);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbillmodifierdtl` ADD INDEX `strBillNo` (`strBillNo`);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbltempsalesflash1` "
		+ "	CHANGE COLUMN `strbillno` `strbillno` VARCHAR(100) NOT NULL DEFAULT '' FIRST, "
		+ "	CHANGE COLUMN `dtebilldate` `dtebilldate` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strbillno`, "
		+ "	CHANGE COLUMN `tmebilltime` `tmebilltime` VARCHAR(100) NOT NULL DEFAULT '' AFTER `dtebilldate`, "
		+ "	CHANGE COLUMN `strtablename` `strtablename` VARCHAR(100) NOT NULL DEFAULT '' AFTER `tmebilltime`, "
		+ "	CHANGE COLUMN `strposcode` `strposcode` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strtablename`, "
		+ "	CHANGE COLUMN `strpaymode` `strpaymode` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strposcode`, "
		+ "	CHANGE COLUMN `dblsubtotal` `dblsubtotal` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strpaymode`, "
		+ "	CHANGE COLUMN `dbldiscper` `dbldiscper` VARCHAR(100) NOT NULL DEFAULT '' AFTER `dblsubtotal`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbltempsalesflash1` "
		+ "	CHANGE COLUMN `dbldiscamt` `dbldiscamt` VARCHAR(100) NOT NULL DEFAULT '' AFTER `dbldiscper`, "
		+ "	CHANGE COLUMN `dbltaxamt` `dbltaxamt` VARCHAR(100) NOT NULL DEFAULT '' AFTER `dbldiscamt`, "
		+ "	CHANGE COLUMN `dblsettlementamt` `dblsettlementamt` VARCHAR(100) NOT NULL DEFAULT '' AFTER `dbltaxamt`, "
		+ "	CHANGE COLUMN `strusercreated` `strusercreated` VARCHAR(100) NOT NULL DEFAULT '' AFTER `dblsettlementamt`, "
		+ "	CHANGE COLUMN `struseredited` `struseredited` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strusercreated`, "
		+ "	CHANGE COLUMN `dtedatecreated` `dtedatecreated` VARCHAR(100) NOT NULL DEFAULT '' AFTER `struseredited`, "
		+ "	CHANGE COLUMN `dtedateedited` `dtedateedited` VARCHAR(100) NOT NULL DEFAULT '' AFTER `dtedatecreated`, "
		+ "	CHANGE COLUMN `strclientcode` `strclientcode` VARCHAR(100) NOT NULL DEFAULT '' AFTER `dtedateedited`, "
		+ "	CHANGE COLUMN `strwaiterno` `strwaiterno` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strclientcode`, "
		+ "	CHANGE COLUMN `strcustomercode` `strcustomercode` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strwaiterno`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbltempsalesflash1` "
		+ "	CHANGE COLUMN `dbldeliverycharges` `dbldeliverycharges` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strcustomercode`, "
		+ "	CHANGE COLUMN `strremarks` `strremarks` VARCHAR(200) NOT NULL DEFAULT '' AFTER `dbldeliverycharges`, "
		+ "	CHANGE COLUMN `strcustomername` `strcustomername` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strremarks`, "
		+ "	CHANGE COLUMN `dbltipamt` `dbltipamt` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strcustomername`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE `tblshortcutkeysetup` ( "
		+ "	`strShortcutKey` VARCHAR(10) NOT NULL, "
		+ "	`strModuleName` VARCHAR(500) NOT NULL, "
		+ "	`strModuleType` VARCHAR(5) NOT NULL "
		+ ") "
		+ "COLLATE='utf8_general_ci' "
		+ "ENGINE=InnoDB "
		+ ";";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbltempsalesflash1` ADD COLUMN `strdiscremarks` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strUser`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblvoidbilldtl` ADD COLUMN `strRemarks` VARCHAR(500) NOT NULL DEFAULT 'NA' AFTER `strKOTNo`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblvoidmodifierdtl` ADD COLUMN `strRemarks` VARCHAR(500) NOT NULL DEFAULT 'NA' AFTER `strDataPostFlag`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblvoidmodifierdtl` ADD COLUMN `strReasonCode` VARCHAR(10) NOT NULL AFTER `strRemarks`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE `tbltruncationdtl` "
		+ "(`strUser` VARCHAR(50) NOT NULL, "
		+ "	`strTruncateForms` VARCHAR(200) NOT NULL DEFAULT '', "
		+ "	`dteDate` DATETIME NOT NULL, "
		+ "	`strModuleType` VARCHAR(50) NOT NULL "
		+ ") "
		+ "COLLATE='utf8_general_ci' "
		+ "ENGINE=InnoDB "
		+ ";";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbilldtl` ADD COLUMN `dblDiscountAmt` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `strWaiterNo`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbilldtl` ADD COLUMN `dblDiscountAmt` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `strWaiterNo`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ "	ADD COLUMN `strCalculateDiscItemWise` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strReceiverEmailId`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbltempsalesflash` "
		+ "	ALTER `strposcode` DROP DEFAULT; ";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbltempsalesflash` "
		+ " CHANGE COLUMN `strposcode` `strposcode` VARCHAR(100) NOT NULL AFTER `dblamount`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbltempadvorderflash` "
		+ " CHANGE COLUMN `strSettlementMode` `strSettlementMode` VARCHAR(100) NOT NULL AFTER `strPOSCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ " ADD COLUMN `strTakewayCustomerSelection` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strCalculateDiscItemWise`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbluserhd` "
		+ " ADD COLUMN `strDebitCardString` VARCHAR(50) NOT NULL DEFAULT '' AFTER `strImgUserIconPath`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbltempsalesflash` "
		+ "	ADD COLUMN `dblsubtotal` VARCHAR(100) NOT NULL AFTER `dblRate`, "
		+ "	ADD COLUMN `dbldiscamt` VARCHAR(100) NOT NULL AFTER `dblsubtotal`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblitemrtemp` ADD COLUMN `dblRate` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `strPromoCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ "	ADD COLUMN `StrShowItemStkColumnInDB` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strTakewayCustomerSelection`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblhomedeldtl` "
		+ "	ADD COLUMN `strSettleYN` VARCHAR(5) NOT NULL DEFAULT 'N' AFTER `strDataPostFlag`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbillhd` "
		+ "	ADD COLUMN `strTakeAwayRemarks` VARCHAR(200) NOT NULL DEFAULT '' AFTER `strDiscountRemark`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbillhd` "
		+ "	ADD COLUMN `strTakeAwayRemarks` VARCHAR(200) NOT NULL DEFAULT '' AFTER `strDiscountRemark`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup`	ADD COLUMN `strItemType` VARCHAR(10) NOT NULL DEFAULT 'Both' AFTER `StrShowItemStkColumnInDB`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ "ADD COLUMN `strAllowNewAreaMasterFromCustMaster` VARCHAR(5) NOT NULL DEFAULT 'N' AFTER `strItemType`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` ADD COLUMN `strCustAddressSelectionForBill` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strAllowNewAreaMasterFromCustMaster`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblposmaster` "
		+ "	ADD COLUMN `strOperationalYN` VARCHAR(5) NOT NULL DEFAULT 'N' AFTER `strAdvReceiptPrinterPort`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE `tblpossettlementdtl` ( "
		+ "	`strPOSCode` VARCHAR(10) NOT NULL, "
		+ "	`strSettlementCode` VARCHAR(10) NOT NULL, "
		+ "	`strSettlementDesc` VARCHAR(100) NOT NULL, "
		+ "	`strClientCode` VARCHAR(10) NOT NULL, "
		+ "	`strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' "
		+ ") "
		+ "COLLATE='utf8_general_ci' "
		+ "ENGINE=InnoDB "
		+ ";";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbldayendprocess` "
		+ "CHANGE COLUMN `strPOSCode` `strPOSCode` VARCHAR(5) NOT NULL FIRST ";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ "	ADD COLUMN `strGenrateMI` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strCustAddressSelectionForBill`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` 	ADD COLUMN `strFTPAddress` VARCHAR(50) NOT NULL DEFAULT '' AFTER `strGenrateMI`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ "ADD COLUMN `strFTPServerUserName` VARCHAR(50) NOT NULL DEFAULT '' AFTER `strFTPAddress`, "
		+ "ADD COLUMN `strFTPServerPass` VARCHAR(50) NOT NULL DEFAULT '' AFTER `strFTPServerUserName`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblitemmaster` "
		+ "ADD COLUMN `strItemWeight` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `strRevenueHead`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ "ADD COLUMN `strAllowToCalculateItemWeight` VARCHAR(5) NOT NULL DEFAULT 'N' AFTER `strFTPServerPass`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbldebitcardmaster` ADD COLUMN `intCardNo` BIGINT NOT NULL AFTER `strClientCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbldebitcardmaster` ALTER `intCardNo` DROP DEFAULT;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbldebitcardmaster` CHANGE COLUMN `intCardNo` `intCardNo` VARCHAR(50) NOT NULL AFTER `strClientCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ "ADD COLUMN `strShowBillsDtlType` VARCHAR(20) NOT NULL AFTER `strAllowToCalculateItemWeight`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ "ADD COLUMN `strPrintTaxInvoiceOnBill` VARCHAR(5) NOT NULL DEFAULT 'Y' AFTER `strShowBillsDtlType`, "
		+ "ADD COLUMN `strPrintInclusiveOfAllTaxesOnBill` VARCHAR(5) NOT NULL DEFAULT 'Y' AFTER `strPrintTaxInvoiceOnBill`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblitemrtemp` CHANGE COLUMN `strCounterCode` `strCounterCode` VARCHAR(4) NOT NULL DEFAULT '' AFTER `dblCreditLimit`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbilldtl` CHANGE COLUMN `strCounterCode` `strCounterCode` VARCHAR(5) NOT NULL DEFAULT '' AFTER `strPromoCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbilldtl` CHANGE COLUMN `strCounterCode` `strCounterCode` VARCHAR(5) NOT NULL DEFAULT '' AFTER `strPromoCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup`	ADD COLUMN `strApplyDiscountOn` VARCHAR(20) NOT NULL DEFAULT 'SubTotal' AFTER `strPrintInclusiveOfAllTaxesOnBill`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbldebitcardtype`	ADD COLUMN `strCashCard` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strDataPostFlag`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbldebitcardmaster` ADD COLUMN `strReachrgeRemark` VARCHAR(50) NOT NULL DEFAULT '' AFTER `intCardNo`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbillhd` ADD COLUMN `strDiscountOn` VARCHAR(20) NOT NULL DEFAULT 'All' AFTER `strTakeAwayRemarks`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbillhd` ADD COLUMN `strDiscountOn` VARCHAR(20) NOT NULL DEFAULT 'All' AFTER `strTakeAwayRemarks`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbilldtl` ADD COLUMN `dblDiscountPer` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `dblDiscountAmt`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbilldtl` ADD COLUMN `dblDiscountPer` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `dblDiscountAmt`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbillhd` DROP PRIMARY KEY, ADD PRIMARY KEY (`strBillNo`, `strClientCode`);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbillhd` DROP PRIMARY KEY, ADD PRIMARY KEY (`strBillNo`, `strClientCode`);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbillsettlementdtl` DROP PRIMARY KEY, ADD PRIMARY KEY (`strBillNo`, `strSettlementCode`, `strClientCode`);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbillsettlementdtl` DROP PRIMARY KEY, ADD PRIMARY KEY (`strBillNo`, `strSettlementCode`, `strClientCode`);";
	mapStructureUpdater.get("tblStructure").add(sql);

	/*
             * sql="ALTER TABLE `tblbilldtl` ADD PRIMARY KEY (`strItemCode`,
             * `strBillNo`, `strClientCode`);"; i = ExecuteQuery(sql);
             * sql="ALTER TABLE `tblqbilldtl` ADD PRIMARY KEY (`strItemCode`,
             * `strBillNo`, `strClientCode`);"; i = ExecuteQuery(sql);
             *
             * sql="ALTER TABLE `tblbillmodifierdtl` ADD PRIMARY KEY
             * (`strBillNo`, `strItemCode`, `strModifierCode`,
             * `strClientCode`);"; i = ExecuteQuery(sql); sql="ALTER TABLE
             * `tblqbillmodifierdtl` ADD PRIMARY KEY (`strBillNo`,
             * `strItemCode`, `strModifierCode`, `strClientCode`);"; i = ExecuteQuery(sql);
	 */
	sql = "ALTER TABLE `tblbilltaxdtl` DROP PRIMARY KEY,	ADD PRIMARY KEY (`strBillNo`, `strTaxCode`, `strClientCode`);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbilltaxdtl` DROP PRIMARY KEY,	ADD PRIMARY KEY (`strBillNo`, `strTaxCode`, `strClientCode`);";
	mapStructureUpdater.get("tblStructure").add(sql);

	/*
             * sql="ALTER TABLE `tbladvancereceiptdtl` ADD PRIMARY KEY
             * (`strReceiptNo`, `strSettlementCode`, `strClientCode`);"; i =
             * ExecuteQuery(sql);
             *
             * sql="ALTER TABLE `tblqadvancereceiptdtl` ADD PRIMARY KEY
             * (`strReceiptNo`, `strSettlementCode`, `strClientCode`);"; i = ExecuteQuery(sql);
	 */
	sql = "ALTER TABLE `tbladvancereceipthd` DROP PRIMARY KEY, ADD PRIMARY KEY (`strReceiptNo`, `strAdvBookingNo`, `strPOSCode`, `strClientCode`);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqadvancereceipthd` DROP PRIMARY KEY, ADD PRIMARY KEY (`strReceiptNo`, `strAdvBookingNo`, `strPOSCode`, `strClientCode`);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbladvbookbilldtl` ADD PRIMARY KEY (`strItemCode`, `strAdvBookingNo`, `strClientCode`);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqadvbookbilldtl` ADD PRIMARY KEY (`strItemCode`, `strAdvBookingNo`, `strClientCode`);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbladvbookbillhd` DROP PRIMARY KEY, ADD PRIMARY KEY (`strAdvBookingNo`, `strPOSCode`, `strClientCode`);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqadvbookbillhd` DROP PRIMARY KEY, ADD PRIMARY KEY (`strAdvBookingNo`, `strPOSCode`, `strClientCode`);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbladvordermodifierdtl` ALTER `strModifierCode` DROP DEFAULT;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbladvordermodifierdtl` CHANGE COLUMN `strModifierCode` `strModifierCode` VARCHAR(4) NOT NULL "
		+ "AFTER `strItemCode`, ADD PRIMARY KEY (`strAdvOrderNo`, `strItemCode`, `strModifierCode`, `strClientCode`);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqadvordermodifierdtl` ALTER `strModifierCode` DROP DEFAULT;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqadvordermodifierdtl` CHANGE COLUMN `strModifierCode` `strModifierCode` VARCHAR(4) NOT NULL "
		+ "AFTER `strItemCode`, ADD PRIMARY KEY (`strAdvOrderNo`, `strItemCode`, `strModifierCode`, `strClientCode`);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblimportexcel` ALTER `strPOSName` DROP DEFAULT;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblimportexcel` CHANGE COLUMN `strPOSName` `strPOSName` VARCHAR(50) NOT NULL AFTER `strMenuHeadCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblimportexcel` ALTER `strMenuHeadName` DROP DEFAULT;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblimportexcel` CHANGE COLUMN `strMenuHeadName` `strMenuHeadName` VARCHAR(50) NOT NULL AFTER `strShortName`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblimportexcel` ALTER `strSubGroupName` DROP DEFAULT, "
		+ "ALTER `strGroupName` DROP DEFAULT, ALTER `strCostCenterName` DROP DEFAULT;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblimportexcel` "
		+ "CHANGE COLUMN `strSubGroupName` `strSubGroupName` VARCHAR(50) NOT NULL AFTER `strPOSCode`, "
		+ "CHANGE COLUMN `strGroupName` `strGroupName` VARCHAR(50) NOT NULL AFTER `strSubMenuHeadCode`, "
		+ "CHANGE COLUMN `strCostCenterName` `strCostCenterName` VARCHAR(50) NOT NULL AFTER `strGroupCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup`	ADD COLUMN `strMemberCodeForKotInMposByCardSwipe` VARCHAR(20) NOT NULL DEFAULT 'N' AFTER `strApplyDiscountOn`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup`ADD COLUMN `strPrintBillYN` VARCHAR(1) NOT NULL DEFAULT 'Y' AFTER `strMemberCodeForKotInMposByCardSwipe`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblposmaster` "
		+ "	ADD COLUMN `strPrintVatNo` VARCHAR(1) NOT NULL DEFAULT 'Y' AFTER `strOperationalYN`, "
		+ "	ADD COLUMN `strPrintServiceTaxNo` VARCHAR(1) NOT NULL DEFAULT 'Y' AFTER `strPrintVatNo`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` ADD COLUMN `strVatAndServiceTaxFromPos` VARCHAR(1) NOT NULL DEFAULT 'Y' AFTER `strPrintBillYN`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblposmaster` "
		+ "     ADD COLUMN `strVatNo` VARCHAR(20) NOT NULL AFTER `strPrintServiceTaxNo`, "
		+ "     ADD COLUMN `strServiceTaxNo` VARCHAR(20) NOT NULL AFTER `strVatNo`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE IF NOT EXISTS `tblarrangetransaction` ( "
		+ " `strModifierDB` VARCHAR(20) NULL, "
		+ "`strPLUDB` VARCHAR(20) NULL, "
		+ "`strHomeDeliveryDB` VARCHAR(20) NULL, "
		+ "`strTakeAwayDB` VARCHAR(20) NULL, "
		+ "`strsettleBillKot` VARCHAR(20) NULL, "
		+ "`strPLUKot` VARCHAR(20) NULL, "
		+ "`strModifierKot` VARCHAR(20) NULL, "
		+ "`strHomeDeliveryKot` VARCHAR(20) NULL, "
		+ "`strTransType` VARCHAR(20) NULL "
		+ ") "
		+ "COLLATE='utf8_general_ci' "
		+ "ENGINE=InnoDB "
		+ ";";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "drop table if exists tblarrangetransaction";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblarrangetransaction` "
		+ "ADD COLUMN `strClientCode` VARCHAR(20) NULL DEFAULT NULL AFTER `strTransType`, "
		+ "ADD COLUMN `strPosCode` VARCHAR(20) NULL DEFAULT NULL AFTER `strClientCode`, "
		+ "ADD COLUMN `strPosFlag` VARCHAR(20) NULL DEFAULT 'N' AFTER `strPosCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE IF NOT EXISTS `tblbillcomplementrydtl` ( "
		+ "  `strItemCode` varchar(10) NOT NULL, "
		+ "  `strItemName` varchar(50) NOT NULL, "
		+ "  `strBillNo` varchar(10) NOT NULL, "
		+ "  `strAdvBookingNo` varchar(10) NOT NULL DEFAULT '', "
		+ "  `dblRate` decimal(18,2) NOT NULL DEFAULT '0.00', "
		+ "  `dblQuantity` decimal(18,2) NOT NULL, "
		+ "  `dblAmount` decimal(18,2) NOT NULL, "
		+ "  `dblTaxAmount` decimal(18,2) NOT NULL, "
		+ "  `dteBillDate` datetime NOT NULL, "
		+ "  `strKOTNo` varchar(10) NOT NULL DEFAULT '', "
		+ "  `strClientCode` varchar(10) NOT NULL, "
		+ "  `strCustomerCode` varchar(10) NOT NULL DEFAULT '', "
		+ "  `tmeOrderProcessing` time NOT NULL DEFAULT '00:00:00', "
		+ "  `strDataPostFlag` varchar(1) NOT NULL DEFAULT 'N', "
		+ "  `strMMSDataPostFlag` varchar(1) NOT NULL DEFAULT 'N', "
		+ "  `strManualKOTNo` varchar(10) NOT NULL DEFAULT '', "
		+ "  `tdhYN` varchar(1) NOT NULL DEFAULT 'N', "
		+ "  `strPromoCode` varchar(20) NOT NULL DEFAULT '', "
		+ "  `strCounterCode` varchar(5) NOT NULL DEFAULT '', "
		+ "  `strWaiterNo` varchar(10) NOT NULL DEFAULT '', "
		+ "  `dblDiscountAmt` decimal(18,2) NOT NULL DEFAULT '0.00', "
		+ "  `dblDiscountPer` decimal(18,2) NOT NULL DEFAULT '0.00' "
		+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE IF NOT EXISTS `tbltransbtnsequence` ( "
		+ "	`strPLUButton` INT NOT NULL, "
		+ "	`strSettleBillButton` INT NOT NULL, "
		+ "	`strDeliveryBoyButton` INT NOT NULL, "
		+ "	`strHomeDeliveryButton` INT NOT NULL, "
		+ "	`strTakeAwayButton` INT NOT NULL, "
		+ "	`strCheckKOTButton` INT NOT NULL, "
		+ "	`strModifierButton` INT NOT NULL, "
		+ "	`strNCKOTButton` INT NOT NULL, "
		+ "	`strTransactionName` VARCHAR(50) NOT NULL, "
		+ "	`strClientCode` VARCHAR(20) NOT NULL, "
		+ "	`strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' "
		+ ") "
		+ "COLLATE='utf8_general_ci' "
		+ "ENGINE=InnoDB "
		+ ";";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` ADD COLUMN `strMemberCodeForMakeBillInMPOS` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strVatAndServiceTaxFromPos`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblposmaster` "
		+ " CHANGE COLUMN `strPrintVatNo` `strPrintVatNo` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strOperationalYN`, "
		+ " CHANGE COLUMN `strPrintServiceTaxNo` `strPrintServiceTaxNo` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strPrintVatNo`, "
		+ " CHANGE COLUMN `strVatNo` `strVatNo` VARCHAR(20) NOT NULL DEFAULT '' AFTER `strPrintServiceTaxNo`, "
		+ " CHANGE COLUMN `strServiceTaxNo` `strServiceTaxNo` VARCHAR(20) NOT NULL DEFAULT '' AFTER `strVatNo`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbldebitcardmaster` ALTER `intCardNo` DROP DEFAULT;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbldebitcardmaster` CHANGE COLUMN `dblRedeemAmt` `dblRedeemAmt` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `strCardNo`, "
		+ "CHANGE COLUMN `intCardNo` `strCardString` VARCHAR(50) NOT NULL AFTER `strClientCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbldebitcardrecharge`	ALTER `strCardNo` DROP DEFAULT;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbldebitcardrecharge` "
		+ "CHANGE COLUMN `strCardNo` `strCardString` VARCHAR(50) NOT NULL AFTER `strCardTypeCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblpromotionmaster` ADD COLUMN `strPOSCode` VARCHAR(10) NOT NULL AFTER `strDataPostFlag`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblitemmaster` ADD COLUMN `strOpenItem` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strItemWeight`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE `tblbilldiscdtl` ( "
		+ "	`strBillNo` VARCHAR(10) NOT NULL, "
		+ "	`strPOSCode` VARCHAR(5) NOT NULL, "
		+ "	`dblDiscAmt` DECIMAL(18,2) NOT NULL, "
		+ "	`dblDiscPer` DECIMAL(10,2) NOT NULL, "
		+ "	`dblDiscOnAmt` DECIMAL(18,2) NOT NULL, "
		+ "	`strDiscOnType` VARCHAR(50) NOT NULL DEFAULT '', "
		+ "	`strDiscOnValue` VARCHAR(50) NOT NULL DEFAULT '', "
		+ "	`strDiscReasonCode` VARCHAR(10) NOT NULL DEFAULT '', "
		+ "	`strDiscRemarks` VARCHAR(100) NOT NULL DEFAULT '', "
		+ "	`strUserCreated` VARCHAR(50) NOT NULL, "
		+ "	`strUserEdited` VARCHAR(50) NOT NULL DEFAULT '', "
		+ "	`dteDateCreated` DATETIME NOT NULL, "
		+ "	`dteDateEdited` DATETIME NOT NULL "
		+ ") "
		+ "COLLATE='utf8_general_ci' "
		+ "ENGINE=InnoDB "
		+ "; ";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE `tblqbilldiscdtl` ( "
		+ "	`strBillNo` VARCHAR(10) NOT NULL, "
		+ "	`strPOSCode` VARCHAR(5) NOT NULL, "
		+ "	`dblDiscAmt` DECIMAL(18,2) NOT NULL, "
		+ "	`dblDiscPer` DECIMAL(10,2) NOT NULL, "
		+ "	`dblDiscOnAmt` DECIMAL(18,2) NOT NULL, "
		+ "	`strDiscOnType` VARCHAR(50) NOT NULL DEFAULT '', "
		+ "	`strDiscOnValue` VARCHAR(50) NOT NULL DEFAULT '', "
		+ "	`strDiscReasonCode` VARCHAR(10) NOT NULL DEFAULT '', "
		+ "	`strDiscRemarks` VARCHAR(100) NOT NULL DEFAULT '', "
		+ "	`strUserCreated` VARCHAR(50) NOT NULL, "
		+ "	`strUserEdited` VARCHAR(50) NOT NULL DEFAULT '', "
		+ "	`dteDateCreated` DATETIME NOT NULL, "
		+ "	`dteDateEdited` DATETIME NOT NULL "
		+ ") "
		+ "COLLATE='utf8_general_ci' "
		+ "ENGINE=InnoDB "
		+ "; ";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbldebitcardtype` "
		+ "ADD COLUMN `strAuthorizeMemberCard` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strCashCard`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbldebitcardrecharge` ADD COLUMN `strMemberCode` VARCHAR(50) NOT NULL DEFAULT '' AFTER `strClientCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbltempsalesflash1` "
		+ "	CHANGE COLUMN `strUser` `strUser` VARCHAR(100) NOT NULL DEFAULT '' AFTER `dbltipamt`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbldebitcardmaster` ADD COLUMN `strRefMemberCode` VARCHAR(50) NOT NULL DEFAULT '' AFTER `strReachrgeRemark`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ " CHANGE COLUMN `strVatNo` `strVatNo` VARCHAR(50) NOT NULL AFTER `strPrintVatNo`,"
		+ " CHANGE COLUMN `strServiceTaxNo` `strServiceTaxNo` VARCHAR(50) NOT NULL AFTER `strPrintServiceTaxNo`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE `tbldebitcardrefundamt` ( "
		+ "	`strRefundNo` VARCHAR(10) NOT NULL, "
		+ "	`strCardTypeCode` VARCHAR(10) NOT NULL, "
		+ "	`strCardString` VARCHAR(50) NOT NULL, "
		+ "	`strCardNo` VARCHAR(50) NOT NULL, "
		+ "	`dblCardBalance` DECIMAL(18,4) NOT NULL, "
		+ "	`dblRefundAmt` DECIMAL(18,4) NOT NULL, "
		+ "	`strUserCreated` VARCHAR(10) NOT NULL, "
		+ "	`dteDateCreated` DATETIME NOT NULL, "
		+ "	`strClientCode` VARCHAR(10) NOT NULL, "
		+ "	`strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N', "
		+ "	PRIMARY KEY (`strRefundNo`, `strClientCode`) "
		+ ") "
		+ "COLLATE='utf8_general_ci' "
		+ "ENGINE=InnoDB;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbltempsalesflash1` ADD COLUMN `strReasonDesc` VARCHAR(500) NOT NULL DEFAULT '' AFTER `strdiscremarks`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbldebitcardrefundamt`	ADD COLUMN `strRefundSlipNo` VARCHAR(10) NOT NULL AFTER `strDataPostFlag`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	//sql="set global max_allowed_packet=32*1024*1024;";
	//i = ExecuteQuery(sql);
	sql = "ALTER TABLE `tblnonchargablekot` "
		+ "	CHANGE COLUMN `strUserCreated` `strUserCreated` VARCHAR(50) NOT NULL DEFAULT '' AFTER `dteNCKOTDate`, "
		+ "	CHANGE COLUMN `strUserEdited` `strUserEdited` VARCHAR(50) NOT NULL DEFAULT '' AFTER `strUserCreated`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblitemmodofier` "
		+ " ADD COLUMN `strDefaultModifier` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strApplicable`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbltempsalesflash` "
		+ " CHANGE COLUMN `strcode` `strcode` VARCHAR(50) NOT NULL DEFAULT '' FIRST, "
		+ " CHANGE COLUMN `strname` `strname` VARCHAR(50) NOT NULL DEFAULT '' AFTER `strcode`, "
		+ " CHANGE COLUMN `dblquantity` `dblquantity` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `strname`, "
		+ " CHANGE COLUMN `dblamount` `dblamount` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `dblquantity`, "
		+ " CHANGE COLUMN `strposcode` `strposcode` VARCHAR(100) NOT NULL DEFAULT '' AFTER `dblamount`, "
		+ " CHANGE COLUMN `struser` `struser` VARCHAR(10) NOT NULL DEFAULT '' AFTER `strposcode`, "
		+ " CHANGE COLUMN `dblRate` `dblRate` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `struser`, "
		+ " CHANGE COLUMN `dblsubtotal` `dblsubtotal` VARCHAR(100) NOT NULL DEFAULT '' AFTER `dblRate`, "
		+ " CHANGE COLUMN `dbldiscamt` `dbldiscamt` VARCHAR(100) NOT NULL DEFAULT '' AFTER `dblsubtotal`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbltempsalesflash` "
		+ " CHANGE COLUMN `struser` `struser` VARCHAR(20) NOT NULL DEFAULT '' AFTER `strposcode`; ";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbldebitcardmaster` DROP COLUMN `intCardNo`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbldebitcardrecharge` "
		+ "ADD COLUMN `strRechargeSlipNo` VARCHAR(10) NOT NULL DEFAULT '' AFTER `strMemberCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbldebitcardrefundamt` ADD COLUMN `strPOSCode` VARCHAR(10) NOT NULL AFTER `strRefundSlipNo`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbldebitcardbilldetails`	ADD COLUMN `strTransactionType` VARCHAR(10) NOT NULL AFTER `dteBillDate`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbillmodifierdtl` "
		+ "ADD COLUMN `strDefaultModifierDeselectedYN` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strMMSDataPostFlag`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbillmodifierdtl` "
		+ "ADD COLUMN `strDefaultModifierDeselectedYN` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strMMSDataPostFlag`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbldebitcardrecharge` ADD COLUMN `strCardNo` VARCHAR(10) NOT NULL AFTER `strRechargeSlipNo`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE `tbldcrechargesettlementdtl` ( "
		+ "	`strRechargeNo` VARCHAR(10) NOT NULL, "
		+ "	`strSettlementCode` VARCHAR(5) NOT NULL, "
		+ "	`dblRechargeAmt` DECIMAL(18,4) NOT NULL, "
		+ "	`strClientCode` VARCHAR(10) NOT NULL, "
		+ "	`strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N', "
		+ "	PRIMARY KEY (`strRechargeNo`, `strClientCode`) "
		+ ") "
		+ "COLLATE='utf8_general_ci' "
		+ "ENGINE=InnoDB;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbldcrechargesettlementdtl` ADD COLUMN `strCardNo` VARCHAR(10) NOT NULL AFTER `dblRechargeAmt`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbldcrechargesettlementdtl` ADD COLUMN `strType` VARCHAR(20) NOT NULL AFTER `strCardNo`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsubgrouphd` ADD COLUMN `strAccountCode` VARCHAR(10) NOT NULL AFTER `strIncentives`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsubgrouphd`	CHANGE COLUMN `strAccountCode` `strAccountCode` VARCHAR(10) NOT NULL DEFAULT 'NA' AFTER `strIncentives`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsettelmenthd`	ADD COLUMN `strAccountCode` VARCHAR(10) NOT NULL AFTER `strDataPostFlag`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbltaxhd`	ADD COLUMN `strAccountCode` VARCHAR(10) NOT NULL AFTER `strDataPostFlag`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblposmaster` ADD COLUMN `strRoundOff` VARCHAR(20) NOT NULL DEFAULT '' AFTER `strServiceTaxNo`, "
		+ "ADD COLUMN `strTip` VARCHAR(20) NOT NULL DEFAULT '' AFTER `strRoundOff`, "
		+ "ADD COLUMN `strDiscount` VARCHAR(20) NOT NULL DEFAULT '' AFTER `strTip`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbilldiscdtl`	ADD COLUMN `strClientCode` VARCHAR(10) NOT NULL AFTER `dteDateEdited`,"
		+ "	ADD COLUMN `strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strClientCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbilldiscdtl`	ADD COLUMN `strClientCode` VARCHAR(10) NOT NULL AFTER `dteDateEdited`,"
		+ "	ADD COLUMN `strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strClientCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = " ALTER TABLE `tblitemmaster` "
		+ " ADD COLUMN `strItemWiseKOTYN` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strOpenItem`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = " ALTER TABLE `tblsetup` "
		+ " ADD COLUMN `strItemWiseKOTYN` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strMemberCodeForMakeBillInMPOS`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbltruncationdtl`	ADD COLUMN `dteFromDate` DATETIME NOT NULL AFTER `strModuleType`"
		+ ", ADD COLUMN `dteToDate` DATETIME NOT NULL AFTER `dteFromDate`"
		+ ", ADD COLUMN `strPOSCode` VARCHAR(10) NOT NULL AFTER `dteToDate`"
		+ ", ADD COLUMN `strClientCode` VARCHAR(10) NOT NULL AFTER `strPOSCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbltempadvorderflash` ADD COLUMN `strManualAdvOrderNo` VARCHAR(100) NOT NULL AFTER `strClientCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblhomedelivery`	DROP PRIMARY KEY, ADD PRIMARY KEY (`strBillNo`, `strClientCode`);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE `tblkdsprocess`  "
		+ "( "
		+ "`strBillNo` VARCHAR(20) NOT NULL, "
		+ "`strBP` CHAR(1) NOT NULL, "
		+ "`dteBookProcessTime` DATETIME NOT NULL, "
		+ "`dteUserCreated` DATETIME NOT NULL, "
		+ "`strUserCreated` VARCHAR(50) NOT NULL, "
		+ "`dteUserEdited` DATETIME NOT NULL, "
		+ "`strUserEdited` VARCHAR(50) NOT NULL, "
		+ " PRIMARY KEY (`strBillNo`) "
		+ ") "
		+ "COLLATE='utf8_general_ci' "
		+ "ENGINE=InnoDB "
		+ "                ;;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = " ALTER TABLE `tblimportexcel` ADD COLUMN `strRevenueHead` VARCHAR(20) NOT NULL DEFAULT '' "
		+ "AFTER `strMenuHeadCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblitemrtemp` ADD COLUMN `strCardType` VARCHAR(50) NOT NULL DEFAULT '' AFTER `intId`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbillhd` ADD COLUMN `strCardNo` VARCHAR(50) NOT NULL DEFAULT '' AFTER `strDiscountOn`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbillhd` ADD COLUMN `strCardNo` VARCHAR(50) NOT NULL DEFAULT '' AFTER `strDiscountOn`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = " ALTER TABLE `tblitemmodofier` "
		+ " CHANGE COLUMN `strDefaultModifier` `strDefaultModifier` VARCHAR(5) NOT NULL DEFAULT 'N' AFTER `strApplicable`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE IF NOT EXISTS `tblbuypromotiondtl` ("
		+ "	`strPromoCode` VARCHAR(10) NOT NULL,"
		+ "	`strBuyPromoItemCode` VARCHAR(10) NOT NULL,"
		+ "	`dblBuyItemQty` DECIMAL(18,4) NOT NULL,"
		+ "	`strOperator` VARCHAR(3) NOT NULL,"
		+ "	`strClientCode` VARCHAR(10) NOT NULL,"
		+ "	`strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N') "
		+ "COLLATE='utf8_general_ci' "
		+ "ENGINE=InnoDB;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE IF NOT EXISTS `tblPrinterSetup` ( "
		+ "`strCostCenterCode` VARCHAR(4) NOT NULL, "
		+ "`strCostCenterName` VARCHAR(100) NOT NULL, "
		+ "`strPrimaryPrinterPort` VARCHAR(100) NOT NULL, "
		+ "`strSecondaryPrinterPort` VARCHAR(100) NOT NULL, "
		+ "`strPrintOnBothPrintersYN` VARCHAR(5) NOT NULL DEFAULT 'N', "
		+ "`strUserCreated` VARCHAR(10) NOT NULL, "
		+ "`strUserEdited` VARCHAR(10) NOT NULL, "
		+ "`dteDateCreated` DATETIME NOT NULL, "
		+ "`dteDateEdited` DATETIME NOT NULL, "
		+ "`strClientCode` VARCHAR(11) NOT NULL DEFAULT '', "
		+ "`strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N', "
		+ "PRIMARY KEY (`strCostCenterCode`, `strClientCode`) "
		+ ") "
		+ "COLLATE='utf8_general_ci' "
		+ "ENGINE=InnoDB "
		+ ";;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblpromotionmaster` ADD COLUMN `strGetItemCode` VARCHAR(10) NOT NULL AFTER `strPOSCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblpromotionmaster` ADD COLUMN `strGetPromoOn` VARCHAR(10) NOT NULL AFTER `strGetItemCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE IF NOT EXISTS `tbldebitcardrevenue` (`strCardNo` VARCHAR(50) NOT NULL"
		+ " ,`dblCardAmt` DECIMAL(18,4) NOT NULL"
		+ " ,`strPOSCode` VARCHAR(10) NOT NULL"
		+ " ,`dtePOSDate` DATETIME NOT NULL"
		+ " ,`dteDate` DATETIME NOT NULL"
		+ " ,`strClientCode` VARCHAR(10) NOT NULL"
		+ " ,`strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N')"
		+ "  COLLATE='utf8_general_ci'  ENGINE=InnoDB;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup`	ADD COLUMN `strLastPOSForDayEnd` VARCHAR(10) NOT NULL AFTER `strItemWiseKOTYN`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbldebitcardrevenue` ADD COLUMN `strUserCreated` VARCHAR(10) NOT NULL AFTER `strDataPostFlag`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` ADD COLUMN `strCMSPostingType` VARCHAR(20) NOT NULL AFTER `strLastPOSForDayEnd`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbillpromotiondtl` ADD COLUMN `strPromoType` VARCHAR(20) NOT NULL AFTER `strDataPostFlag`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbillpromotiondtl` ADD COLUMN `dblAmount` DECIMAL(18,4) NOT NULL AFTER `strPromoType`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbltdhcomboitemdtl` ADD COLUMN `strSubItemMenuCode` VARCHAR(15) NOT NULL AFTER `strDefaultYN`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup`	ADD COLUMN `strPopUpToApplyPromotionsOnBill` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strCMSPostingType`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblimportexcel` CHANGE COLUMN `strRevenueHead` `strRevenueHead` VARCHAR(50) NOT NULL DEFAULT '' AFTER `strMenuHeadCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = " CREATE TABLE IF NOT EXISTS `tblqbillpromotiondtl` ( "
		+ " `strBillNo` VARCHAR(20) NOT NULL, "
		+ " `strItemCode` VARCHAR(15) NOT NULL, "
		+ " `strPromotionCode` VARCHAR(20) NOT NULL, "
		+ " `dblQuantity` DECIMAL(18,4) NOT NULL, "
		+ " `dblRate` DECIMAL(18,4) NOT NULL, "
		+ " `strClientCode` VARCHAR(20) NOT NULL, "
		+ " `strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N', "
		+ " `strPromoType` VARCHAR(20) NOT NULL, "
		+ " `dblAmount` DECIMAL(18,4) NOT NULL ) "
		+ "COLLATE='utf8_general_ci'ENGINE=InnoDB; ";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = " CREATE TABLE IF NOT EXISTS `tblqbillcomplementrydtl` ( "
		+ " `strItemCode` VARCHAR(10) NOT NULL, "
		+ " `strItemName` VARCHAR(50) NOT NULL, "
		+ " `strBillNo` VARCHAR(10) NOT NULL,   "
		+ " `strAdvBookingNo` VARCHAR(10) NOT NULL DEFAULT '',"
		+ " `dblRate` DECIMAL(18,2) NOT NULL DEFAULT '0.00', "
		+ " `dblQuantity` DECIMAL(18,2) NOT NULL, "
		+ " `dblAmount` DECIMAL(18,2) NOT NULL, "
		+ " `dblTaxAmount` DECIMAL(18,2) NOT NULL, "
		+ " `dteBillDate` DATETIME NOT NULL, "
		+ " `strKOTNo` VARCHAR(20) NOT NULL DEFAULT '', "
		+ " `strClientCode` VARCHAR(10) NOT NULL, "
		+ " `strCustomerCode` VARCHAR(10) NOT NULL DEFAULT '',"
		+ " `tmeOrderProcessing` TIME NOT NULL DEFAULT '00:00:00',"
		+ " `strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N',"
		+ " `strMMSDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N',"
		+ " `strManualKOTNo` VARCHAR(10) NOT NULL DEFAULT '',"
		+ " `tdhYN` VARCHAR(1) NOT NULL DEFAULT 'N',"
		+ " `strPromoCode` VARCHAR(20) NOT NULL DEFAULT '',"
		+ " `strCounterCode` VARCHAR(5) NOT NULL DEFAULT '',"
		+ " `strWaiterNo` VARCHAR(10) NOT NULL DEFAULT '',"
		+ " `dblDiscountAmt` DECIMAL(18,2) NOT NULL DEFAULT '0.00',"
		+ " `dblDiscountPer` DECIMAL(18,2) NOT NULL DEFAULT '0.00' ) "
		+ " COLLATE='utf8_general_ci' "
		+ " ENGINE=InnoDB;  ";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbilldtl` "
		+ "ADD COLUMN `strSequenceNo` VARCHAR(50) NOT NULL DEFAULT '0.00' AFTER `dblDiscountPer`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbilldtl` "
		+ "ADD COLUMN `strSequenceNo` VARCHAR(10) NOT NULL DEFAULT '0.00' AFTER `dblDiscountPer`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbillmodifierdtl` "
		+ "ADD COLUMN `strSequenceNo` VARCHAR(50) NOT NULL DEFAULT '0.00' AFTER `strDefaultModifierDeselectedYN`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbillmodifierdtl` "
		+ "ADD COLUMN `strSequenceNo` VARCHAR(10) NOT NULL DEFAULT '0.00' AFTER `strDefaultModifierDeselectedYN`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbillcomplementrydtl` "
		+ "ADD COLUMN `strSequenceNo` VARCHAR(10) NOT NULL DEFAULT '0.00' AFTER `dblDiscountPer`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbillcomplementrydtl` "
		+ "ADD COLUMN `strSequenceNo` VARCHAR(10) NOT NULL DEFAULT '0.00' AFTER `dblDiscountPer`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE IF NOT EXISTS `tblorderanalysis` ( "
		+ "  `strField1` varchar(100) NOT NULL DEFAULT '', "
		+ "  `strField2` varchar(100) NOT NULL DEFAULT '', "
		+ "  `strField3` varchar(100) NOT NULL DEFAULT '', "
		+ "  `strField4` varchar(100) NOT NULL DEFAULT '', "
		+ "  `strField5` varchar(100) NOT NULL DEFAULT '', "
		+ "  `strField6` varchar(100) NOT NULL DEFAULT '', "
		+ "  `strField7` varchar(100) NOT NULL DEFAULT '', "
		+ "  `strField8` varchar(100) NOT NULL DEFAULT '', "
		+ "  `strField9` varchar(100) NOT NULL DEFAULT '', "
		+ "  `strField10` varchar(100) NOT NULL DEFAULT '', "
		+ "  `strField11` varchar(100) NOT NULL DEFAULT '', "
		+ "  `strField12` varchar(100) NOT NULL DEFAULT '', "
		+ "  `strField13` varchar(100) NOT NULL DEFAULT '' "
		+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblorderanalysis`	ADD COLUMN `strField14` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strField13`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE IF NOT EXISTS `tblpromotiondaytimedtl` ( "
		+ "	`strPromoCode` VARCHAR(10) NOT NULL, "
		+ "	`strDay` VARCHAR(10) NOT NULL, "
		+ "	`tmeFromTime` VARCHAR(10) NOT NULL, "
		+ "	`tmeToTime` VARCHAR(10) NOT NULL, "
		+ "	`strClientCode` VARCHAR(10) NOT NULL, "
		+ "	`strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' "
		+ ") "
		+ "COLLATE='utf8_general_ci' "
		+ "ENGINE=InnoDB;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = " ALTER TABLE `tblsetup` ADD COLUMN `strSelectCustomerCodeFromCardSwipe` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strPopUpToApplyPromotionsOnBill`; ";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = " ALTER TABLE `tblitemmaster` ADD COLUMN `strWSProdCode` VARCHAR(10) NOT NULL DEFAULT ' NA' AFTER `strItemWiseKOTYN`; ";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = " ALTER TABLE `tblitemmaster` ADD COLUMN `strExciseBrandCode` VARCHAR(10) NOT NULL DEFAULT '' AFTER `strWSProdCode`; ";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = " ALTER TABLE `tblposmaster` ADD COLUMN `strWSLocationCode` VARCHAR(10) NOT NULL DEFAULT '' AFTER `strDiscount`; ";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = " ALTER TABLE `tblposmaster` ADD COLUMN `strExciseLicenceCode` VARCHAR(10) NOT NULL DEFAULT '' AFTER `strWSLocationCode`; ";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE IF NOT EXISTS `tblitemmasterlinkupdtl` ( "
		+ "  `strItemCode` varchar(20) NOT NULL, "
		+ "  `strPOSCode` varchar(20) NOT NULL, "
		+ "  `strWSProductCode` varchar(20) NOT NULL, "
		+ "  `strClientCode` varchar(20) NOT NULL, "
		+ "  `strDataPostFlag` varchar(1) NOT NULL "
		+ ") ENGINE=InnoDB DEFAULT CHARSET=latin1;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbillpromotiondtl` "
		+ "	ALTER `dblQuantity` DROP DEFAULT;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbillpromotiondtl` "
		+ "CHANGE COLUMN `dblQuantity` `dblQuantity` DECIMAL(18,2) NOT NULL AFTER `strPromotionCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbillpromotiondtl` "
		+ "	ALTER `dblQuantity` DROP DEFAULT;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbillpromotiondtl` "
		+ "CHANGE COLUMN `dblQuantity` `dblQuantity` DECIMAL(18,2) NOT NULL AFTER `strPromotionCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbldcrechargesettlementdtl` "
		+ "DROP PRIMARY KEY, ADD PRIMARY KEY (`strRechargeNo`, `strClientCode`, `strSettlementCode`);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup`	ADD COLUMN `strCheckDebitCardBalOnTransactions` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strSelectCustomerCodeFromCardSwipe`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblitemrtemp` "
		+ "ADD COLUMN `dblTaxAmt` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `strCardType`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbldayendprocess`	ADD COLUMN `dblUsedDebitCardBalance` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `intNoOfVoidKOT`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbldayendprocess`	ADD COLUMN `dblUnusedDebitCardBalance` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `dblUsedDebitCardBalance`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbldayendprocess` ADD COLUMN `strWSStockAdjustmentNo` VARCHAR(20) NOT NULL DEFAULT '' AFTER `dblUnusedDebitCardBalance`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE IF NOT EXISTS `tblkottaxdtl` ( "
		+ "	`strTableNo` VARCHAR(10) NOT NULL, "
		+ "	`strKOTNo` VARCHAR(10) NOT NULL, "
		+ "	`dblAmount` DECIMAL(18,2) NOT NULL, "
		+ "	`dblTaxAmt` DECIMAL(18,2) NOT NULL "
		+ ") "
		+ "COLLATE='utf8_general_ci' "
		+ "ENGINE=InnoDB "
		+ ";";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblmenuitempricingdtl` "
		+ "CHANGE COLUMN `longPricingId` `longPricingId` BIGINT(20) NOT NULL DEFAULT '0' AFTER `strHourlyPricing`, "
		+ "DROP PRIMARY KEY;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblhomedeldtl` ADD COLUMN `dblDBIncentives` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `strSettleYN`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbillmodifierdtl` "
		+ "ADD COLUMN `dblDiscPer` DECIMAL(18,2) NOT NULL AFTER `strSequenceNo`, "
		+ "ADD COLUMN `dblDiscAmt` DECIMAL(18,2) NOT NULL AFTER `dblDiscPer`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbillmodifierdtl` "
		+ "ADD COLUMN `dblDiscPer` DECIMAL(18,2) NOT NULL AFTER `strSequenceNo`, "
		+ "ADD COLUMN `dblDiscAmt` DECIMAL(18,2) NOT NULL AFTER `dblDiscPer`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup`	ADD COLUMN `strSettlementsFromPOSMaster` VARCHAR(1) NOT NULL DEFAULT "
		+ " 'N' AFTER `strCheckDebitCardBalOnTransactions`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE IF NOT EXISTS `tblplaceorderhd` ( "
		+ "	`strSOCode` VARCHAR(20) NOT NULL, "
		+ "	`dteSODate` DATETIME NOT NULL, "
		+ "	`dteFulmtDate` DATETIME NOT NULL, "
		+ "	`strUserCreated` VARCHAR(10) NOT NULL, "
		+ "	`dteDateCreated` DATETIME NOT NULL, "
		+ "	`strClientCode` VARCHAR(10) NOT NULL, "
		+ "	`strCloseSO` VARCHAR(1) NOT NULL "
		+ ") "
		+ "COLLATE='utf8_general_ci' "
		+ "ENGINE=InnoDB "
		+ ";";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE IF NOT EXISTS `tblplaceorderdtl` ( "
		+ "	`strSOCode` VARCHAR(20) NOT NULL, "
		+ "	`strProductCode` VARCHAR(20) NOT NULL, "
		+ "	`strItemCode` VARCHAR(20) NOT NULL, "
		+ "	`dblQty` DECIMAL(18,2) NOT NULL, "
		+ "	`dblStockQty` DECIMAL(18,2) NOT NULL, "
		+ "	`strClientCode` VARCHAR(10) NOT NULL "
		+ ") "
		+ "COLLATE='utf8_general_ci' "
		+ "ENGINE=InnoDB;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblplaceorderhd`	ADD PRIMARY KEY (`strSOCode`, `strClientCode`);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblplaceorderhd`	ADD COLUMN `strDCCode` VARCHAR(20) NOT NULL AFTER `strCloseSO`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbldebitcardrecharge` ADD COLUMN `strTransferBalance` VARCHAR(1) NOT NULL AFTER `strCardNo`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbldebitcardrecharge`"
		+ "	CHANGE COLUMN `strTransferBalance` `strTransferBalance` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strCardNo`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblplaceorderhd`"
		+ "	ADD COLUMN `strOrderCode` VARCHAR(20) NOT NULL FIRST,"
		+ "	DROP PRIMARY KEY,"
		+ "	ADD PRIMARY KEY (`strOrderCode`, `strClientCode`);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblplaceorderdtl`"
		+ "	ADD COLUMN `strOrderCode` VARCHAR(20) NOT NULL FIRST,"
		+ "	DROP COLUMN `strSOCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblplaceorderhd` ALTER `dteFulmtDate` DROP DEFAULT;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblplaceorderhd`	CHANGE COLUMN `dteFulmtDate` `dteOrderDate` DATETIME "
		+ " NOT NULL AFTER `dteSODate`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsettelmenthd`	ADD COLUMN `strBillPrintOnSettlement` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strAccountCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblorderanalysis` ADD COLUMN `strField15` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strField14`";
	mapStructureUpdater.get("tblStructure").add(sql);

	//Queries for Old Complimentary Settlement bills
	sql = "update tblbillhd a,tblbilldtl b,tblbillsettlementdtl c,tblsettelmenthd d "
		+ "set a.dblSubTotal=0.00,a.dblGrandTotal=0.00,a.dblDiscountAmt=0.00,a.dblDiscountPer=0.00 "
		+ ",a.dblTaxAmt=0.00,b.dblAmount=0.00,b.dblTaxAmount=0.00,b.dblDiscountAmt=0.00,b.dblDiscountPer=0.00 "
		+ ",c.dblSettlementAmt=0.00,c.dblPaidAmt=0.00 "
		+ "where a.strBillNo=b.strBillNo "
		+ "and a.strBillNo=c.strBillNo "
		+ "and c.strSettlementCode=d.strSettelmentCode "
		+ "and d.strSettelmentType='Complementary';";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblbillmodifierdtl a,tblbillsettlementdtl b,tblsettelmenthd c "
		+ "set a.dblAmount=0.00 "
		+ "where a.strBillNo=b.strBillNo "
		+ "and b.strSettlementCode=c.strSettelmentCode "
		+ "and c.strSettelmentType='Complementary';";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblbilltaxdtl a,tblbillsettlementdtl b,tblsettelmenthd c "
		+ "set a.dblTaxableAmount=0.00,a.dblTaxAmount=0.00 "
		+ "where a.strBillNo=b.strBillNo "
		+ "and b.strSettlementCode=c.strSettelmentCode "
		+ "and c.strSettelmentType='Complementary';";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblqbillhd a,tblqbilldtl b,tblqbillsettlementdtl c,tblsettelmenthd d "
		+ "set a.dblSubTotal=0.00,a.dblGrandTotal=0.00,a.dblDiscountAmt=0.00,a.dblDiscountPer=0.00 "
		+ ",a.dblTaxAmt=0.00,b.dblAmount=0.00,b.dblTaxAmount=0.00,b.dblDiscountAmt=0.00,b.dblDiscountPer=0.00 "
		+ ",c.dblSettlementAmt=0.00,c.dblPaidAmt=0.00 "
		+ "where a.strBillNo=b.strBillNo "
		+ "and a.strBillNo=c.strBillNo "
		+ "and c.strSettlementCode=d.strSettelmentCode "
		+ "and d.strSettelmentType='Complementary';";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblqbillmodifierdtl a,tblqbillsettlementdtl b,tblsettelmenthd c "
		+ "set a.dblAmount=0.00 "
		+ "where a.strBillNo=b.strBillNo "
		+ "and b.strSettlementCode=c.strSettelmentCode "
		+ "and c.strSettelmentType='Complementary';";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblqbilltaxdtl a,tblqbillsettlementdtl b,tblsettelmenthd c "
		+ "set a.dblTaxableAmount=0.00,a.dblTaxAmount=0.00 "
		+ "where a.strBillNo=b.strBillNo "
		+ "and b.strSettlementCode=c.strSettelmentCode "
		+ "and c.strSettelmentType='Complementary';";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "delete from tblbilldiscdtl  "
		+ "where strBillNo IN (select strBillNo from tblbillhd  where dblGrandTotal=0);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "delete from tblqbilldiscdtl  "
		+ "where strBillNo IN (select strBillNo from tblqbillhd  where dblGrandTotal=0);";
	mapStructureUpdater.get("tblStructure").add(sql);

	//end Queries for Old Complimentary Settlement bills
	sql = "update tblbillmodifierdtl set strItemCode=CONCAT(strItemCode,strModifierCode)   where LENGTH(strItemCode)=7 ; ";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblqbillmodifierdtl set strItemCode=CONCAT(strItemCode,strModifierCode)   where LENGTH(strItemCode)=7 ; ";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup`ADD COLUMN `strShiftWiseDayEndYN` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strSettlementsFromPOSMaster` ";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` ADD COLUMN `strProductionLinkup` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strShiftWiseDayEndYN`; ";
	mapStructureUpdater.get("tblStructure").add(sql);

	//bill series
	sql = "CREATE TABLE IF NOT EXISTS `tblbillseries` ("
		+ "	`strType` VARCHAR(15) NOT NULL,"
		+ "	`strBillSeries` VARCHAR(15) NOT NULL,"
		+ "	`strCodes` VARCHAR(500) NOT NULL,"
		+ "	`strNames` VARCHAR(500) NOT NULL,"
		+ "	`strUserCreated` VARCHAR(20) NOT NULL,"
		+ "	`strUserEdited` VARCHAR(20) NOT NULL,"
		+ "	`dteCreatedDate` DATETIME NOT NULL,"
		+ "	`dteEditedDate` DATETIME NOT NULL,"
		+ "	`strDataPostFlag` VARCHAR(2) NOT NULL DEFAULT 'N',"
		+ "	`strClientCode` VARCHAR(10) NOT NULL,"
		+ "	`strPropertyCode` VARCHAR(15) NOT NULL "
		+ ") "
		+ "COLLATE='utf8_general_ci' "
		+ "ENGINE=InnoDB "
		+ "; "
		+ " ";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbillseries` ADD COLUMN `strPOSCode` VARCHAR(15) NOT NULL FIRST, "
		+ "ADD COLUMN `intLastNo` BIGINT(20) NOT NULL AFTER `strBillSeries`";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblwaitermaster` 	ADD COLUMN `strPOSCode` VARCHAR(10) NOT NULL DEFAULT 'All' AFTER `strDataPostFlag`; ";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblwaitermaster` 	ADD COLUMN `strPOSCode` VARCHAR(10) NOT NULL DEFAULT 'All' AFTER `strDataPostFlag`; ";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = " ALTER TABLE `tblitemmaster` "
		+ " ADD COLUMN `strNoDeliveryDays` VARCHAR(200) NOT NULL DEFAULT '' AFTER `strExciseBrandCode`, "
		+ " ADD COLUMN `intDeliveryDays` INT(10) NOT NULL DEFAULT '0' AFTER `strNoDeliveryDays`; ";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = " ALTER TABLE `tblitemmaster` "
		+ " ADD COLUMN `strIncrementalWeight` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `intDeliveryDays`, "
		+ " ADD COLUMN `strMinWeight` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `strIncrementalWeight`; ";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblitemmaster` CHANGE COLUMN `strIncrementalWeight` `dblIncrementalWeight` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `intDeliveryDays`,"
		+ " CHANGE COLUMN `strMinWeight` `dblMinWeight` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `dblIncrementalWeight`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbltaxhd`	ADD COLUMN `strTaxShortName` VARCHAR(20) NOT NULL DEFAULT '' AFTER `strAccountCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblposmaster` ADD COLUMN `strEnableShift` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strExciseLicenceCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblitemmaster`	DROP COLUMN `strIncrementalWeight`,	DROP COLUMN `strMinWeight`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` ADD COLUMN `strLockDataOnShift` VARCHAR(1) NOT NULL DEFAULT 'Y' AFTER `strProductionLinkup`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "drop table IF EXISTS tbldayendreport;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = " ALTER TABLE `tbladvbookbilldtl` "
		+ " ADD COLUMN `dblWeight` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `dblQuantity`; ";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = " ALTER TABLE `tbladvbookitemtemp` "
		+ " ADD COLUMN `dblWeight` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `dblItemQuantity`; ";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = " ALTER TABLE `tblqadvbookbilldtl` "
		+ " ADD COLUMN `dblWeight` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `dblQuantity` ";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = " ALTER TABLE `tblitemmasterlinkupdtl` "
		+ " ADD COLUMN `strWSProductName` VARCHAR(200) NOT NULL AFTER `strWSProductCode`; ";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = " CREATE TABLE IF NOT EXISTS `tblcharactersticsmaster` "
		+ " (`strCharCode` VARCHAR(10) NOT NULL, "
		+ " `strCharName` VARCHAR(100) NOT NULL, "
		+ " `strCharType` VARCHAR(50) NOT NULL, "
		+ " `strWSCharCode` VARCHAR(10) NOT NULL, "
		+ " `strValue` VARCHAR(100) NOT NULL, "
		+ " `strUserCreated` VARCHAR(10) NOT NULL, "
		+ " `strUserEdited` VARCHAR(10) NOT NULL, "
		+ " `dteDateCreated` DATETIME NOT NULL, "
		+ " `dteDateEdited` DATETIME NOT NULL, "
		+ " `strClientCode` VARCHAR(11) NOT NULL DEFAULT '', "
		+ " `strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N', "
		+ " `strPOSCode` VARCHAR(10) NOT NULL ) "
		+ " COLLATE='utf8_general_ci' ENGINE=InnoDB; ";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = " CREATE TABLE IF NOT EXISTS `tblitemorderingdtl` (  "
		+ " `strItemCode` VARCHAR(20) NOT NULL, "
		+ " `strPOSCode` VARCHAR(20) NOT NULL, "
		+ " `strOrderCode` VARCHAR(20) NOT NULL, "
		+ " `strClientCode` VARCHAR(20) NOT NULL, "
		+ " `strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' "
		+ " )COLLATE='utf8_general_ci' "
		+ " ENGINE=InnoDB; ";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = " CREATE TABLE IF NOT EXISTS `tblcharvalue` ( "
		+ " `strCharCode` VARCHAR(10) NOT NULL, "
		+ " `strCharName` VARCHAR(50) NOT NULL, "
		+ " `strCharValues` VARCHAR(50) NOT NULL, "
		+ " `strClientCode` VARCHAR(11) NOT NULL DEFAULT '', "
		+ " `strPOScode` VARCHAR(10) NOT NULL, "
		+ " `strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' "
		+ " ) "
		+ " COLLATE='utf8_general_ci' "
		+ " ENGINE=InnoDB; ";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = " CREATE TABLE IF NOT EXISTS `tblitemcharctersticslinkupdtl` ( "
		+ " `strItemCode` VARCHAR(10) NOT NULL, "
		+ " `strCharCode` VARCHAR(10) NOT NULL, "
		+ " `strCharValue` VARCHAR(100) NOT NULL, "
		+ " `strPOSCode` VARCHAR(10) NOT NULL, "
		+ " `strClientCode` VARCHAR(11) NOT NULL DEFAULT '', "
		+ " `strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' "
		+ " ) "
		+ " COLLATE='utf8_general_ci' "
		+ " ENGINE=InnoDB;  ";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = " CREATE TABLE IF NOT EXISTS `tbladvbookbillchardtl` ( "
		+ " `strItemCode` VARCHAR(15) NOT NULL, "
		+ " `strAdvBookingNo` VARCHAR(20) NOT NULL, "
		+ " `strCharCode` VARCHAR(15) NOT NULL, "
		+ " `strCharValues` VARCHAR(200) NOT NULL, "
		+ " `strClientCode` VARCHAR(11) NOT NULL, "
		+ " `strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N', "
		+ " PRIMARY KEY (`strItemCode`,`strAdvBookingNo`,`strCharCode`,`strClientCode`) "
		+ " )COLLATE='utf8_general_ci' "
		+ " ENGINE=InnoDB; ";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = " ALTER TABLE `tblplaceorderhd` "
		+ " ADD COLUMN `strOrderTypeCode` VARCHAR(20) NOT NULL DEFAULT '' AFTER `strDCCode`; ";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = " ALTER TABLE `tblplaceorderhd` "
		+ " ADD COLUMN `strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strOrderTypeCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = " ALTER TABLE `tblplaceorderdtl` "
		+ " ADD COLUMN `strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strClientCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = " ALTER TABLE `tblsetup` "
		+ " ADD COLUMN `strWSClientCode` VARCHAR(11) NOT NULL DEFAULT '' AFTER `strLockDataOnShift`; ";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ "ADD COLUMN `strPOSCode` VARCHAR(10) NOT NULL DEFAULT 'All' AFTER `strWSClientCode`; ";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ "DROP PRIMARY KEY, "
		+ "ADD PRIMARY KEY (`strClientCode`, `strPOSCode`);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ "CHANGE COLUMN `strShowBillsDtlType` `strShowBillsDtlType` VARCHAR(30) NOT NULL AFTER `strAllowToCalculateItemWeight`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ "ADD COLUMN `strEnableBillSeries` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strPOSCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE IF NOT EXISTS `tblordermaster` ( "
		+ " `strOrderCode` VARCHAR(10) NOT NULL, "
		+ " `strOrderDesc` VARCHAR(200) NOT NULL, "
		+ " `tmeUpToTime` VARCHAR(15) NOT NULL, "
		+ " `strUserCreated` VARCHAR(10) NOT NULL, "
		+ " `strUserEdited` VARCHAR(10) NOT NULL, "
		+ " `dteDateCreated` datetime NOT NULL, "
		+ " `dteDateEdited` datetime NOT NULL, "
		+ " `strClientCode` VARCHAR(11) NOT NULL DEFAULT '', "
		+ " `strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N', "
		+ " `strPOSCode` VARCHAR(10) NOT NULL ) "
		+ " COLLATE='utf8_general_ci' "
		+ " ENGINE=InnoDB; ";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ "ADD COLUMN `strEnablePMSIntegrationYN` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strEnableBillSeries`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbillsettlementdtl` "
		+ "ADD COLUMN `strFolioNo` VARCHAR(10) NOT NULL DEFAULT '' AFTER `strDataPostFlag`,"
		+ "ADD COLUMN `strRoomNo` VARCHAR(10) NOT NULL DEFAULT '' AFTER `strFolioNo`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ "ADD COLUMN `strEnablePMSIntegrationYN` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strEnableBillSeries`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbillsettlementdtl` ADD COLUMN `strFolioNo` VARCHAR(10) NOT NULL DEFAULT '' AFTER `strDataPostFlag`"
		+ ",ADD COLUMN `strRoomNo` VARCHAR(10) NOT NULL DEFAULT '' AFTER `strFolioNo`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbillsettlementdtl` ADD COLUMN `strFolioNo` VARCHAR(10) NOT NULL DEFAULT '' AFTER `strDataPostFlag`"
		+ ",ADD COLUMN `strRoomNo` VARCHAR(10) NOT NULL DEFAULT '' AFTER `strFolioNo`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = " ALTER TABLE `tblitemmaster`ADD COLUMN `strUrgentOrder` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `dblMinWeight`; ";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblreasonmaster` "
		+ "ADD COLUMN `strReprint` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strVoidAdvOrder`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE if not exists `tblAudit` ( "
		+ "	`strDocNo` VARCHAR(10) NOT NULL, "
		+ "	`strFormName` VARCHAR(20) NOT NULL, "
		+ "	`strTransactionName` VARCHAR(20) NOT NULL, "
		+ "	`strReasonCode` VARCHAR(10) NOT NULL, "
		+ "	`strRemarks` VARCHAR(50) NOT NULL, "
		+ "	`dtePOSDate` DATETIME NOT NULL, "
		+ "	`dteCreatedDate` DATETIME NOT NULL, "
		+ "	`strUserCreated` VARCHAR(10) NOT NULL, "
		+ "	`strClientCode` VARCHAR(10) NOT NULL, "
		+ "	`strDataPostFlag` VARCHAR(10) NOT NULL DEFAULT 'N' "
		+ ") "
		+ "COLLATE='utf8_general_ci' "
		+ "ENGINE=InnoDB "
		+ ";";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblplaceorderhd`	ADD COLUMN `strOrderType` VARCHAR(15) NOT NULL DEFAULT 'Normal' AFTER `strDataPostFlag`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup`"
		+ "ADD COLUMN `strPrintTimeOnBill` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strEnablePMSIntegrationYN`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ "CHANGE COLUMN `strBillFooter` `strBillFooter` VARCHAR(2000) NOT NULL AFTER `strEmail`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup`	ADD COLUMN `strPrintTDHItemsInBill` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strPrintTimeOnBill`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblplaceorderhd`	ADD COLUMN `strAdvOrderNo` VARCHAR(20) NOT NULL DEFAULT '' AFTER `strOrderType`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbladvbookitemtemp` ADD COLUMN `dblPrice` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `dblWeight`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbladvbookbillhd`	ADD COLUMN `strUrgentOrder` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strSpecialsymbolImage`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqadvbookbillhd` ADD COLUMN `strUrgentOrder` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strSpecialsymbolImage`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblplaceorderhd`	DROP COLUMN `strAdvOrderNo`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblplaceorderdtl`	ADD COLUMN `strAdvOrderNo` VARCHAR(20) NOT NULL DEFAULT '' AFTER `strDataPostFlag`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE if not exists `tblplaceorderadvorderdtl` ( "
		+ "	`strAdvOrderNo` VARCHAR(20) NOT NULL, "
		+ "	`dteOrderDate` DATETIME NOT NULL, "
		+ "	`strClientCode` VARCHAR(10) NOT NULL, "
		+ "	`strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' "
		+ ") "
		+ "COLLATE='utf8_general_ci' "
		+ "ENGINE=InnoDB "
		+ ";";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblplaceorderadvorderdtl` ADD COLUMN `strOrderType` VARCHAR(20) NOT NULL AFTER `strDataPostFlag`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE if not exists `tblbillseriesbilldtl` ("
		+ "`strPOSCode` VARCHAR(10) NOT NULL, "
		+ "`strBillSeries` VARCHAR(10) NOT NULL, "
		+ "`strHdBillNo` VARCHAR(15) NOT NULL, "
		+ "`strDtlBillNos` VARCHAR(50) NOT NULL, "
		+ "`dblGrandTotal` DECIMAL(18,2) NOT NULL, "
		+ "`strClientCode` VARCHAR(10) NOT NULL, "
		+ "`strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N', "
		+ "`strUserCreated` VARCHAR(10) NOT NULL, "
		+ "`dteCreatedDate` DATETIME NOT NULL, "
		+ "`strUserEdited` VARCHAR(10) NOT NULL, "
		+ "`dteEditedDate` DATETIME NOT NULL "
		+ ") "
		+ "COLLATE='utf8_general_ci' "
		+ "ENGINE=InnoDB "
		+ "; ";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbillseries` "
		+ "ADD COLUMN `strPrintGTOfOtherBills` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strPropertyCode`; ";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqadvbookbilldtl` CHANGE COLUMN `dblWeight` `dblWeight` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `strCustomerCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE IF NOT EXISTS `tbladvbookbillimgdtl` ( "
		+ "`strItemCode` VARCHAR(15) NOT NULL, "
		+ "`strAdvBookingNo` VARCHAR(20) NOT NULL, "
		+ "`blobCakeImage` LONGBLOB NOT NULL, "
		+ "`strClientCode` VARCHAR(11) NOT NULL, "
		+ "`strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N', "
		+ "PRIMARY KEY (`strItemCode`,`strAdvBookingNo`,`strClientCode`) "
		+ ")COLLATE='utf8_general_ci' "
		+ "ENGINE=InnoDB; ";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblcounterhd` ADD COLUMN `strUserCode` VARCHAR(20) NOT NULL DEFAULT '' AFTER `strOperational`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblgrouphd set strGroupCode=CONCAT('G','0',right(strGroupCode,6)) "
		+ " where length(strGroupCode)<8";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblsubgrouphd set strSubGroupCode=CONCAT('SG','0',right(strSubGroupCode,6)) "
		+ " where length(strSubGroupCode)<9";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = " ALTER TABLE `tblsetup` "
		+ " ADD COLUMN `strPrintRemarkAndReasonForReprint` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strPrintTDHItemsInBill`; ";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbldayendprocess` "
		+ "ADD COLUMN `dblTipAmt` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `strWSStockAdjustmentNo`; ";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblitemmaster set strSubGroupCode=CONCAT('SG','0',right(strSubGroupCode,6)) "
		+ " where length(strSubGroupCode)<9";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblsubgrouphd set strGroupCode=CONCAT('G','0',right(strGroupCode,6)) "
		+ " where length(strGroupCode)<8;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblstkinhd` ADD COLUMN `strInvoiceCode` VARCHAR(20) NOT NULL DEFAULT '' AFTER `strDataPostFlag`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblcashmanagement` ADD COLUMN `strAgainst` VARCHAR(20) NOT NULL DEFAULT 'Direct' AFTER `intShiftCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblcashmanagement` ADD COLUMN `dblRollingAmt` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `strAgainst`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = " ALTER TABLE `tblgrouphd` "
		+ " ADD COLUMN `strOperationalYN` VARCHAR(1) NOT NULL DEFAULT 'Y' AFTER `strDataPostFlag`; ";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = " ALTER TABLE `tblpsphd` "
		+ " ADD COLUMN `strReasonCode` VARCHAR(10) NOT NULL AFTER `strDataPostFlag`, "
		+ " ADD COLUMN `strRemarks` VARCHAR(50) NOT NULL AFTER `strReasonCode`; ";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup`	ADD COLUMN `intDaysBeforeOrderToCancel` INT NOT NULL DEFAULT '0' AFTER `strPrintRemarkAndReasonForReprint`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = " CREATE TABLE IF NOT EXISTS `tblvoidadvancereceiptdtl` ( "
		+ "  `strReceiptNo` varchar(10) NOT NULL, "
		+ "  `strSettlementCode` varchar(10) NOT NULL, "
		+ "  `strCardNo` varchar(50) NOT NULL, "
		+ "  `strExpirydate` varchar(50) NOT NULL, "
		+ "  `strChequeNo` varchar(50) NOT NULL, "
		+ "  `dteCheque` date NOT NULL, "
		+ "  `strBankName` varchar(50) NOT NULL, "
		+ "  `dblAdvDepositesettleAmt` decimal(18,2) NOT NULL DEFAULT '0.00', "
		+ "  `strRemark` varchar(50) NOT NULL, "
		+ "  `dblPaidAmt` decimal(18,2) NOT NULL DEFAULT '0.00', "
		+ "  `strClientCode` varchar(10) NOT NULL, "
		+ "  `strDataPostFlag` varchar(1) NOT NULL DEFAULT 'N', "
		+ "  `dteInstallment` datetime NOT NULL "
		+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE IF NOT EXISTS `tblvoidadvancereceipthd` ( "
		+ "  `strReceiptNo` varchar(15) NOT NULL, "
		+ "  `strAdvBookingNo` varchar(15) NOT NULL, "
		+ "  `strPOSCode` varchar(10) NOT NULL, "
		+ "  `strSettelmentMode` varchar(15) NOT NULL, "
		+ "  `dtReceiptDate` date NOT NULL, "
		+ "  `dblAdvDeposite` decimal(18,2) NOT NULL DEFAULT '0.00', "
		+ "  `intShiftCode` int(11) NOT NULL, "
		+ "  `strUserCreated` varchar(10) NOT NULL, "
		+ "  `strUserEdited` varchar(10) NOT NULL, "
		+ "  `dtDateCreated` datetime NOT NULL, "
		+ "  `dtDateEdited` datetime NOT NULL, "
		+ "  `strClientCode` varchar(10) NOT NULL, "
		+ "  `strDataPostFlag` varchar(1) NOT NULL DEFAULT 'N', "
		+ "  PRIMARY KEY (`strReceiptNo`,`strAdvBookingNo`,`strPOSCode`,`strClientCode`) "
		+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE IF NOT EXISTS `tblvoidadvbookbillchardtl` ( "
		+ "  `strItemCode` varchar(15) NOT NULL, "
		+ "  `strAdvBookingNo` varchar(20) NOT NULL, "
		+ "  `strCharCode` varchar(15) NOT NULL, "
		+ "  `strCharValues` varchar(200) NOT NULL, "
		+ "  `strClientCode` varchar(11) NOT NULL, "
		+ "  `strDataPostFlag` varchar(1) NOT NULL DEFAULT 'N', "
		+ "  PRIMARY KEY (`strItemCode`,`strAdvBookingNo`,`strCharCode`,`strClientCode`) "
		+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE IF NOT EXISTS `tblvoidadvbookbilldtl` ( "
		+ "  `strItemCode` varchar(50) NOT NULL, "
		+ "  `strItemName` varchar(50) NOT NULL, "
		+ "  `strAdvBookingNo` varchar(50) NOT NULL, "
		+ "  `dblQuantity` decimal(18,2) NOT NULL, "
		+ "  `dblAmount` decimal(18,2) NOT NULL, "
		+ "  `dblTaxAmount` decimal(18,2) NOT NULL, "
		+ "  `dteAdvBookingDate` datetime NOT NULL, "
		+ "  `dteOrderFor` date NOT NULL, "
		+ "  `strClientCode` varchar(50) NOT NULL, "
		+ "  `strCustomerCode` varchar(50) NOT NULL, "
		+ "  `dblWeight` decimal(18,2) NOT NULL DEFAULT '0.00', "
		+ "  `strDataPostFlag` varchar(1) NOT NULL DEFAULT 'N', "
		+ "  PRIMARY KEY (`strItemCode`,`strAdvBookingNo`,`strClientCode`) "
		+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE IF NOT EXISTS `tblvoidadvbookbillhd` ( "
		+ "  `strAdvBookingNo` varchar(15) NOT NULL, "
		+ "  `dteAdvBookingDate` datetime NOT NULL, "
		+ "  `dteOrderFor` datetime NOT NULL, "
		+ "  `strPOSCode` varchar(10) NOT NULL, "
		+ "  `strSettelmentMode` varchar(10) NOT NULL, "
		+ "  `dblDiscountAmt` decimal(18,2) NOT NULL, "
		+ "  `dblDiscountPer` decimal(18,2) NOT NULL, "
		+ "  `dblTaxAmt` decimal(18,2) NOT NULL, "
		+ "  `dblSubTotal` decimal(18,2) NOT NULL, "
		+ "  `dblGrandTotal` decimal(18,2) NOT NULL, "
		+ "  `strUserCreated` varchar(10) NOT NULL, "
		+ "  `strUserEdited` varchar(10) NOT NULL, "
		+ "  `dteDateCreated` datetime NOT NULL, "
		+ "  `dteDateEdited` datetime NOT NULL, "
		+ "  `strClientCode` varchar(10) NOT NULL, "
		+ "  `strCustomerCode` varchar(10) NOT NULL, "
		+ "  `intShiftCode` int(11) NOT NULL, "
		+ "  `strMessage` varchar(150) NOT NULL DEFAULT '', "
		+ "  `strShape` varchar(100) NOT NULL DEFAULT '', "
		+ "  `strNote` varchar(300) NOT NULL DEFAULT '', "
		+ "  `strDataPostFlag` varchar(1) NOT NULL DEFAULT 'N', "
		+ "  `strDeliveryTime` varchar(10) NOT NULL, "
		+ "  `strWaiterNo` varchar(10) NOT NULL DEFAULT '', "
		+ "  `strHomeDelivery` varchar(1) NOT NULL DEFAULT 'N', "
		+ "  `dblHomeDelCharges` decimal(18,2) NOT NULL DEFAULT '0.00', "
		+ "  `strOrderType` varchar(10) NOT NULL, "
		+ "  `strManualAdvOrderNo` varchar(20) NOT NULL DEFAULT '', "
		+ "  `strImageName` varchar(50) NOT NULL DEFAULT '', "
		+ "  `strSpecialsymbolImage` varchar(50) NOT NULL DEFAULT '', "
		+ "  `strUrgentOrder` varchar(1) NOT NULL DEFAULT 'N', "
		+ "  PRIMARY KEY (`strAdvBookingNo`,`strPOSCode`,`strClientCode`) "
		+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblvoidadvbookbillhd` "
		+ "	ADD COLUMN `strReasonCode` VARCHAR(10) NOT NULL AFTER `strUrgentOrder`, "
		+ "	ADD COLUMN `strRemark` VARCHAR(100) NOT NULL AFTER `strReasonCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE IF NOT EXISTS `tblvoidadvordermodifierdtl` ( "
		+ "  `strAdvOrderNo` varchar(15) NOT NULL, "
		+ "  `strItemCode` varchar(10) NOT NULL, "
		+ "  `strModifierCode` varchar(4) NOT NULL, "
		+ "  `strModifierName` varchar(50) NOT NULL, "
		+ "  `dblQuantity` decimal(18,2) NOT NULL, "
		+ "  `dblAmount` decimal(18,2) NOT NULL, "
		+ "  `strClientCode` varchar(10) NOT NULL, "
		+ "  `strCustomerCode` varchar(10) NOT NULL, "
		+ "  `strUserCreated` varchar(10) NOT NULL, "
		+ "  `strUserEdited` varchar(10) NOT NULL, "
		+ "  `dteDateCreated` datetime NOT NULL, "
		+ "  `dteDateEdited` datetime NOT NULL, "
		+ "  `strDataPostFlag` varchar(1) NOT NULL DEFAULT 'N', "
		+ "  PRIMARY KEY (`strAdvOrderNo`,`strItemCode`,`strModifierCode`,`strClientCode`) "
		+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ "	ADD COLUMN `intNoOfDelDaysForAdvOrder` INT(11) NOT NULL DEFAULT '0' AFTER `intDaysBeforeOrderToCancel`, "
		+ "	ADD COLUMN `intNoOfDelDaysForUrgentOrder` INT(11) NOT NULL DEFAULT '0' AFTER `intNoOfDelDaysForAdvOrder`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ "	ADD COLUMN `strSetUpToTimeForAdvOrder` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `intNoOfDelDaysForUrgentOrder`, "
		+ "	ADD COLUMN `strSetUpToTimeForUrgentOrder` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strSetUpToTimeForAdvOrder`, "
		+ "	ADD COLUMN `strUpToTimeForAdvOrder` VARCHAR(20) NOT NULL DEFAULT '00:00 AM' AFTER `strSetUpToTimeForUrgentOrder`, "
		+ "	ADD COLUMN `strUpToTimeForUrgentOrder` VARCHAR(20) NOT NULL DEFAULT '00:00 AM' AFTER `strUpToTimeForAdvOrder`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup`	ADD COLUMN `strEnableBothPrintAndSettleBtnForDB` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strUpToTimeForUrgentOrder`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE IF NOT EXISTS `tblqadvbookbillchardtl` ( "
		+ "  `strItemCode` varchar(15) NOT NULL, "
		+ "  `strAdvBookingNo` varchar(20) NOT NULL, "
		+ "  `strCharCode` varchar(15) NOT NULL, "
		+ "  `strCharValues` varchar(200) NOT NULL, "
		+ "  `strClientCode` varchar(11) NOT NULL, "
		+ "  `strDataPostFlag` varchar(1) NOT NULL DEFAULT 'N', "
		+ "  PRIMARY KEY (`strItemCode`,`strAdvBookingNo`,`strCharCode`,`strClientCode`) "
		+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbltablemaster` ALTER `strTableNo` DROP DEFAULT;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbltablemaster` CHANGE COLUMN `strTableNo` `strTableNo` VARCHAR(10) NOT NULL FIRST;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tbltablemaster set strTableNo=CONCAT('TB','0000',right(strTableNo,3)) "
		+ " where length(strTableNo)<9 and length(strTableNo) > 0;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblbillhd set strTableNo=CONCAT('TB','0000',right(strTableNo,3)) "
		+ " where length(strTableNo)<9 and length(strTableNo) > 0;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblqbillhd set strTableNo=CONCAT('TB','0000',right(strTableNo,3)) "
		+ " where length(strTableNo)<9 and length(strTableNo) > 0;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblitemrtemp set strTableNo=CONCAT('TB','0000',right(strTableNo,3)) "
		+ " where length(strTableNo)<9 and length(strTableNo) > 0;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblnonchargablekot set strTableNo=CONCAT('TB','0000',right(strTableNo,3)) "
		+ " where length(strTableNo)<9 and length(strTableNo) > 0;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup`"
		+ "ADD COLUMN `strInrestoPOSIntegrationYN` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strEnableBothPrintAndSettleBtnForDB`,"
		+ "ADD COLUMN `strInrestoPOSWebServiceURL` VARCHAR(255) NOT NULL DEFAULT '' AFTER `strInrestoPOSIntegrationYN`,"
		+ "ADD COLUMN `strInrestoPOSId` VARCHAR(50) NOT NULL DEFAULT '' AFTER `strInrestoPOSWebServiceURL`,"
		+ "ADD COLUMN `strInrestoPOSKey` VARCHAR(50) NOT NULL DEFAULT '' AFTER `strInrestoPOSId`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	//customer code updation
	sql = "ALTER TABLE `tblcustomermaster` "
		+ "	CHANGE COLUMN `strCustomerCode` `strCustomerCode` VARCHAR(15) NOT NULL FIRST;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbillhd` "
		+ "	CHANGE COLUMN `strCustomerCode` `strCustomerCode` VARCHAR(15);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbillhd` "
		+ "	CHANGE COLUMN `strCustomerCode` `strCustomerCode` VARCHAR(15);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbilldtl` "
		+ "	CHANGE COLUMN `strCustomerCode` `strCustomerCode` VARCHAR(15);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbilldtl` "
		+ "	CHANGE COLUMN `strCustomerCode` `strCustomerCode` VARCHAR(15);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbillsettlementdtl` "
		+ "	CHANGE COLUMN `strCustomerCode` `strCustomerCode` VARCHAR(15);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbillsettlementdtl` "
		+ "	CHANGE COLUMN `strCustomerCode` `strCustomerCode` VARCHAR(15);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbladvbookbillhd` "
		+ "	CHANGE COLUMN `strCustomerCode` `strCustomerCode` VARCHAR(15);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqadvbookbillhd` "
		+ "	CHANGE COLUMN `strCustomerCode` `strCustomerCode` VARCHAR(15);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbladvbookbilldtl` "
		+ "	CHANGE COLUMN `strCustomerCode` `strCustomerCode` VARCHAR(15);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqadvbookbilldtl` "
		+ "	CHANGE COLUMN `strCustomerCode` `strCustomerCode` VARCHAR(15);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbldebitcardmaster` "
		+ "	CHANGE COLUMN `strCustomerCode` `strCustomerCode` VARCHAR(15);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblhomedelivery` "
		+ "	CHANGE COLUMN `strCustomerCode` `strCustomerCode` VARCHAR(15);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblreservation` "
		+ "	CHANGE COLUMN `strCustomerCode` `strCustomerCode` VARCHAR(15);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblcustomermaster a "
		+ "set a.strCustomerCode=CONCAT(right(a.strClientCode,3),a.strCustomerCode) "
		+ "where LENGTH(a.strCustomerCode)=8;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblbillhd a "
		+ "set a.strCustomerCode=CONCAT(right(a.strClientCode,3),a.strCustomerCode) "
		+ "where LENGTH(a.strCustomerCode)=8;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblqbillhd a "
		+ "set a.strCustomerCode=CONCAT(right(a.strClientCode,3),a.strCustomerCode) "
		+ "where LENGTH(a.strCustomerCode)=8;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tbladvbookbillhd a "
		+ "set a.strCustomerCode=CONCAT(right(a.strClientCode,3),a.strCustomerCode) "
		+ "where LENGTH(a.strCustomerCode)=8;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblqadvbookbillhd a "
		+ "set a.strCustomerCode=CONCAT(right(a.strClientCode,3),a.strCustomerCode) "
		+ "where LENGTH(a.strCustomerCode)=8;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tbladvbookbilldtl a "
		+ "set a.strCustomerCode=CONCAT(right(a.strClientCode,3),a.strCustomerCode) "
		+ "where LENGTH(a.strCustomerCode)=8;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblqadvbookbilldtl a "
		+ "set a.strCustomerCode=CONCAT(right(a.strClientCode,3),a.strCustomerCode) "
		+ "where LENGTH(a.strCustomerCode)=8;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tbldebitcardmaster a "
		+ "set a.strCustomerCode=CONCAT(right(a.strClientCode,3),a.strCustomerCode) "
		+ "where LENGTH(a.strCustomerCode)=8;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblhomedelivery a "
		+ "set a.strCustomerCode=CONCAT(right(a.strClientCode,3),a.strCustomerCode) "
		+ "where LENGTH(a.strCustomerCode)=8;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblreservation a "
		+ "set a.strCustomerCode=CONCAT(right(a.strClientCode,3),a.strCustomerCode) "
		+ "where LENGTH(a.strCustomerCode)=8;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblbillsettlementdtl a "
		+ "set a.strCustomerCode=CONCAT(right(a.strClientCode,3),a.strCustomerCode) "
		+ "where LENGTH(a.strCustomerCode)=8;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblqbillsettlementdtl a "
		+ "set a.strCustomerCode=CONCAT(right(a.strClientCode,3),a.strCustomerCode) "
		+ "where LENGTH(a.strCustomerCode)=8;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblbilldtl a "
		+ "set a.strCustomerCode=CONCAT(right(a.strClientCode,3),a.strCustomerCode) "
		+ "where LENGTH(a.strCustomerCode)=8;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblqbilldtl a "
		+ "set a.strCustomerCode=CONCAT(right(a.strClientCode,3),a.strCustomerCode) "
		+ "where LENGTH(a.strCustomerCode)=8;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblvoidadvbookbillhd` ALTER `strCustomerCode` DROP DEFAULT;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblvoidadvbookbillhd` "
		+ " CHANGE COLUMN `strCustomerCode` `strCustomerCode` VARCHAR(15) NOT NULL AFTER `strClientCode`";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ "ADD COLUMN `strCarryForwardFloatAmtToNextDay` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strInrestoPOSKey`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbillcomplementrydtl` "
		+ " CHANGE COLUMN `strCustomerCode` `strCustomerCode` VARCHAR(10) NULL DEFAULT '' AFTER `strClientCode`";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbillcomplementrydtl` "
		+ " CHANGE COLUMN `strCustomerCode` `strCustomerCode` VARCHAR(10) NULL DEFAULT '' AFTER `strClientCode`";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbillmodifierdtl` CHANGE COLUMN `strCustomerCode` `strCustomerCode` VARCHAR(20) NOT NULL DEFAULT '' AFTER `strClientCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbillmodifierdtl` CHANGE COLUMN `strCustomerCode` `strCustomerCode` VARCHAR(20) NOT NULL DEFAULT '' AFTER `strClientCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblcashmanagement` ADD COLUMN `strClientCode` VARCHAR(10) NOT NULL AFTER `dblRollingAmt`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblcashmanagement` ADD COLUMN `strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strClientCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblcashmanagement` ADD PRIMARY KEY (`strTransID`, `strClientCode`);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE IF NOT EXISTS `tblposwiseitemwiseincentives` ( "
		+ " `strPOSCode` VARCHAR(10) NOT NULL, "
		+ " `strItemCode` VARCHAR(10) NOT NULL, "
		+ " `strItemName` VARCHAR(200) NOT NULL, "
		+ " `strIncentiveType` VARCHAR(15) NOT NULL, "
		+ " `dblIncentiveValue` DECIMAL(18,2) NOT NULL DEFAULT '0.00', "
		+ " `strClientCode` VARCHAR(11) NOT NULL DEFAULT '', "
		+ " `strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' "
		+ " )COLLATE='utf8_general_ci' "
		+ " ENGINE=InnoDB; ";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE tblsetup "
		+ "ADD COLUMN strOpenCashDrawerAfterBillPrintYN VARCHAR(1) NOT NULL DEFAULT 'N' AFTER strCarryForwardFloatAmtToNextDay ";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbillhd` ADD COLUMN `strTransactionType` VARCHAR(300) NOT NULL DEFAULT '' AFTER `strCardNo`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbillhd` ADD COLUMN `strTransactionType` VARCHAR(300) NOT NULL DEFAULT '' AFTER `strCardNo`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE IF NOT EXISTS `tblfactorymaster` (`strFactoryCode` VARCHAR(10) NOT NULL, "
		+ "	`strFactoryName` VARCHAR(200) NOT NULL, "
		+ "	`strUserCreated` VARCHAR(10) NOT NULL, "
		+ "	`strUserEdited` VARCHAR(10) NOT NULL, "
		+ "	`dteDateCreated` DATETIME NOT NULL, "
		+ "	`dteDateEdited` DATETIME NOT NULL, "
		+ "	`strClientCode` VARCHAR(11) NOT NULL, "
		+ "	`strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N', "
		+ "	PRIMARY KEY (`strFactoryCode`) "
		+ ") "
		+ "COLLATE='utf8_general_ci' "
		+ "ENGINE=InnoDB "
		+ ";";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsubgrouphd` "
		+ "ADD COLUMN `strFactoryCode` VARCHAR(10) NOT NULL DEFAULT '' AFTER `strAccountCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsubgrouphd` DROP COLUMN `strMMSPropertyCode`";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsubgrouphd` DROP COLUMN `strWSLocationCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbillpromotiondtl` "
		+ " ADD COLUMN `dblDiscountPer` DECIMAL(18,4) NOT NULL DEFAULT '0' AFTER `dblAmount`,"
		+ " ADD COLUMN `dblDiscountAmt` DECIMAL(18,4) NOT NULL DEFAULT '0' AFTER `dblDiscountPer`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbillpromotiondtl` "
		+ " ADD COLUMN `dblDiscountPer` DECIMAL(18,4) NOT NULL DEFAULT '0' AFTER `dblAmount`,"
		+ " ADD COLUMN `dblDiscountAmt` DECIMAL(18,4) NOT NULL DEFAULT '0' AFTER `dblDiscountPer`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ "ADD COLUMN `strPropertyWiseSalesOrderYN` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strOpenCashDrawerAfterBillPrintYN` ";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE IF NOT EXISTS `tblmasteroperationstatus` ( "
		+ "	`strTableName` VARCHAR(50) NOT NULL, "
		+ "	`dteDateEdited` DATETIME NOT NULL, "
		+ "	`strClientCode` VARCHAR(10) NOT NULL, "
		+ "	PRIMARY KEY (`strTableName`, `strClientCode`) "
		+ ") "
		+ "COLLATE='utf8_general_ci' "
		+ "ENGINE=InnoDB "
		+ ";";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbillseries` "
		+ " ADD COLUMN `strPrintInclusiveOfTaxOnBill` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strPrintGTOfOtherBills`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbillseries` DROP PRIMARY KEY, ADD PRIMARY KEY (`strPOSCode`, `strBillSeries`, `strClientCode`);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ " ADD COLUMN `strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strPropertyWiseSalesOrderYN`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblkdsprocess` "
		+ "CHANGE COLUMN `strBillNo` `strDocNo` VARCHAR(20) NOT NULL FIRST, "
		+ "ADD COLUMN `strKDSName` VARCHAR(10) NOT NULL AFTER `strUserEdited` ";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE `tblpmspostingbilldtl` ( "
		+ "	`strBillNo` VARCHAR(50) NOT NULL, "
		+ "	`strPOSCode` VARCHAR(10) NOT NULL, "
		+ "	`strFolioNo` VARCHAR(10) NOT NULL, "
		+ "	`strGuestCode` VARCHAR(15) NOT NULL, "
		+ "	`strClientCode` VARCHAR(10) NOT NULL, "
		+ "	PRIMARY KEY (`strBillNo`, `strPOSCode`, `strClientCode`) "
		+ ") "
		+ "COLLATE='latin1_swedish_ci' "
		+ "ENGINE=InnoDB "
		+ ";";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblpmspostingbilldtl` "
		+ "	ADD COLUMN `dteBillDate` DATETIME NOT NULL AFTER `strPOSCode`, "
		+ "	ADD COLUMN `dblSettleAmt` DECIMAL(18,2) NOT NULL AFTER `dteBillDate`, "
		+ "	ADD COLUMN `strRoomNo` VARCHAR(15) NOT NULL AFTER `strGuestCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblpmspostingbilldtl` "
		+ "	ADD COLUMN `strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strClientCode`, "
		+ "	ADD COLUMN `strPMSDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strDataPostFlag`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = " ALTER TABLE `tblposwiseitemwiseincentives` "
		+ " ADD COLUMN `dteDateCreated` DATETIME NOT NULL AFTER `strDataPostFlag`, "
		+ " ADD COLUMN `dteDateEdited` DATETIME NOT NULL AFTER `dteDateCreated`; ";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbladvordermodifierdtl` ALTER `strCustomerCode` DROP DEFAULT;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbladvordermodifierdtl` "
		+ "	CHANGE COLUMN `strCustomerCode` `strCustomerCode` VARCHAR(15) NOT NULL AFTER `strClientCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqadvordermodifierdtl` ALTER `strCustomerCode` DROP DEFAULT;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqadvordermodifierdtl` "
		+ "	CHANGE COLUMN `strCustomerCode` `strCustomerCode` VARCHAR(15) NOT NULL AFTER `strClientCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbltruncationdtl` "
		+ "CHANGE COLUMN `strTruncateForms` `strTruncateForms` VARCHAR(500) NOT NULL DEFAULT '' AFTER `strUser` ";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblcustomermaster` "
		+ "ADD COLUMN `strCustAddress` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strCRMId`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblcostcentermaster` "
		+ "ADD COLUMN `strLabelOnKOT` VARCHAR(10) NOT NULL DEFAULT 'KOT' AFTER `strDataPostFlag`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE tblsetup "
		+ " DROP COLUMN strEnableAuthorisationForTransactionYN ;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblcustomermaster` "
		+ "CHANGE COLUMN `strCustAddress` `strCustAddress` VARCHAR(200) NOT NULL DEFAULT '' AFTER `strCRMId`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsuperuserdtl` ADD COLUMN `strTLA` VARCHAR(5) NOT NULL DEFAULT 'false' AFTER `strGrant`";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsuperuserdtl` ADD COLUMN `strAuditing` VARCHAR(5) NOT NULL DEFAULT 'true' AFTER `strTLA`";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbluserdtl` ADD COLUMN `strTLA` VARCHAR(5) NOT NULL DEFAULT 'false' AFTER `strGrant`";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbluserdtl` ADD COLUMN `strAuditing` VARCHAR(5) NOT NULL DEFAULT 'true' AFTER `strTLA`";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblitemrtemp`	ENGINE=InnoDB;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblitemrtemp` "
		+ "ADD COLUMN `strReason` VARCHAR(50) NOT NULL DEFAULT 'No' AFTER `dblTaxAmt`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblitemrtemp` 	ALTER `strKOTNo` DROP DEFAULT;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblitemrtemp` "
		+ "ADD PRIMARY KEY (`strTableNo`, `strItemCode`, `strKOTNo`,`strItemName`);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE if not exists `tblitemrtemp_bck` ( "
		+ "	`strSerialNo` VARCHAR(10) NOT NULL, "
		+ "	`strTableNo` VARCHAR(10) NOT NULL, "
		+ "	`strCardNo` VARCHAR(50) NULL DEFAULT NULL, "
		+ "	`dblRedeemAmt` DECIMAL(18,2) NULL DEFAULT NULL, "
		+ "	`strHomeDelivery` VARCHAR(50) NULL DEFAULT 'No', "
		+ "	`strCustomerCode` VARCHAR(50) NULL DEFAULT NULL, "
		+ "	`strPOSCode` VARCHAR(3) NOT NULL, "
		+ "	`strItemCode` VARCHAR(50) NOT NULL, "
		+ "	`strItemName` VARCHAR(200) NOT NULL, "
		+ "	`dblItemQuantity` DECIMAL(18,2) NOT NULL, "
		+ "	`dblAmount` DECIMAL(18,2) NOT NULL, "
		+ "	`strWaiterNo` VARCHAR(10) NOT NULL, "
		+ "	`strKOTNo` VARCHAR(10) NULL , "
		+ "	`intPaxNo` INT(11) NOT NULL, "
		+ "	`strPrintYN` CHAR(1) NULL DEFAULT NULL, "
		+ "	`strManualKOTNo` VARCHAR(10) NOT NULL DEFAULT '', "
		+ "	`strUserCreated` VARCHAR(10) NOT NULL, "
		+ "	`strUserEdited` VARCHAR(10) NOT NULL, "
		+ "	`dteDateCreated` DATETIME NOT NULL, "
		+ "	`dteDateEdited` DATETIME NOT NULL, "
		+ "	`strOrderBefore` VARCHAR(10) NOT NULL DEFAULT 'No', "
		+ "	`strTakeAwayYesNo` VARCHAR(10) NOT NULL DEFAULT 'No', "
		+ "	`tdhComboItemYN` VARCHAR(1) NOT NULL DEFAULT 'N', "
		+ "	`strDelBoyCode` VARCHAR(10) NOT NULL DEFAULT '', "
		+ "	`strNCKotYN` VARCHAR(1) NOT NULL DEFAULT 'N', "
		+ "	`strCustomerName` VARCHAR(100) NOT NULL DEFAULT '', "
		+ "	`strActiveYN` VARCHAR(1) NOT NULL DEFAULT 'N', "
		+ "	`dblBalance` DECIMAL(18,2) NOT NULL DEFAULT '0.00', "
		+ "	`dblCreditLimit` DECIMAL(18,2) NOT NULL DEFAULT '0.00', "
		+ "	`strCounterCode` VARCHAR(4) NOT NULL DEFAULT '', "
		+ "	`strPromoCode` VARCHAR(10) NOT NULL DEFAULT '', "
		+ "	`dblRate` DECIMAL(18,2) NOT NULL DEFAULT '0.00', "
		+ "	`intId` BIGINT(20) NULL DEFAULT NULL, "
		+ "	`strCardType` VARCHAR(50) NOT NULL DEFAULT '', "
		+ "	`dblTaxAmt` DECIMAL(18,2) NOT NULL DEFAULT '0.00', "
		+ " `strReason` VARCHAR(50) NOT NULL DEFAULT 'No', "
		+ " PRIMARY KEY (`strTableNo`, `strItemCode`, `strKOTNo`,`strItemName`) "
		+ " ) "
		+ "COLLATE='utf8_general_ci' "
		+ "ENGINE=InnoDB "
		+ ";";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblitemrtemp` "
		+ "CHANGE COLUMN `intId` `intId` BIGINT(20) NULL AUTO_INCREMENT AFTER `dblRate`, "
		+ "ADD INDEX `intId` (`intId`);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblitemrtemp_bck` "
		+ "CHANGE COLUMN `intId` `intId` BIGINT(20) NULL AUTO_INCREMENT AFTER `dblRate`, "
		+ "ADD INDEX `intId` (`intId`);";
	mapStructureUpdater.get("tblStructure").add(sql);

	//structure update for rtemp
	//funStructureUpdateForTblItemrTemp();
	mapStructureUpdater.get("tblStructure").add("funStructureUpdateForTblItemrTemp");

	sql = "ALTER TABLE `tbldebitcardtabletemp` "
		+ "CHANGE COLUMN `strTableNo` `strTableNo` VARCHAR(10) NOT NULL FIRST;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblitemrtemp` "
		+ "DROP PRIMARY KEY, "
		+ "ADD PRIMARY KEY (`strTableNo`, `strItemCode`, `strKOTNo`, `strItemName`, `intId`);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblitemrtemp_bck` "
		+ "DROP PRIMARY KEY, "
		+ "ADD PRIMARY KEY (`strTableNo`, `strItemCode`, `strKOTNo`, `strItemName`, `intId`);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblitemrtemp` "
		+ "CHANGE COLUMN `intId` `intId` BIGINT(20) NULL DEFAULT NULL AFTER `dblRate`, "
		+ "DROP PRIMARY KEY, "
		+ "ADD PRIMARY KEY (`strTableNo`, `strItemCode`, `strKOTNo`, `strItemName`, `strSerialNo`);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblitemrtemp_bck` "
		+ "CHANGE COLUMN `intId` `intId` BIGINT(20) NULL DEFAULT NULL AFTER `dblRate`, "
		+ "DROP PRIMARY KEY, "
		+ "ADD PRIMARY KEY (`strTableNo`, `strItemCode`, `strKOTNo`, `strItemName`, `strSerialNo`);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblitemrtemp` "
		+ " DROP INDEX  `intId` ;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblitemrtemp_bck` "
		+ " DROP INDEX  `intId` ;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblreservation` "
		+ "ADD COLUMN `strPosCode` VARCHAR(10) NOT NULL AFTER `strDataPostFlag`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = " ALTER TABLE `tblsetup` ADD COLUMN `strShowItemDetailsGrid` VARCHAR(1) NOT NULL DEFAULT 'Y' AFTER `strDataPostFlag` ";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblforms`	ADD COLUMN `strRequestMapping` VARCHAR(100) NOT NULL AFTER `strColorImageName`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = " CREATE TABLE if not exists `tblaccountmaster` ( "
		+ " `strWBAccountCode` VARCHAR(20) NOT NULL, "
		+ " `strWBAccountName` VARCHAR(200) NOT NULL, "
		+ " `strClientCode` VARCHAR(20) NOT NULL, "
		+ " PRIMARY KEY (`strWBAccountCode`, `strClientCode`) "
		+ " ) "
		+ " COLLATE='latin1_swedish_ci' "
		+ " ENGINE=InnoDB ";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = " ALTER TABLE `tblposmaster` "
		+ " ADD COLUMN `strWSLocationName` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strEnableShift`";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ "ADD COLUMN `strShowPopUpForNextItemQuantity` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strShowItemDetailsGrid` ";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE if not exists `tbltallylinkup` ( "
		+ "	`strPOSCode` VARCHAR(10) NOT NULL, "
		+ "	`strLinkupCode` VARCHAR(20) NOT NULL, "
		+ "	`strLinkupName` VARCHAR(100) NOT NULL, "
		+ "	`strTallyAliasCode` VARCHAR(10) NOT NULL,"
		+ "  `strDiscTallyAliasCode` VARCHAR(10) NOT NULL DEFAULT '', "
		+ "	`strTipTallyAliasCode` VARCHAR(10) NOT NULL DEFAULT '', "
		+ "	`strUserCreated` VARCHAR(50) NOT NULL, "
		+ "	`strUserEdited` VARCHAR(50) NOT NULL, "
		+ "	`dteDateCreated` DATETIME NOT NULL, "
		+ "	`dteDateEdited` DATETIME NOT NULL, "
		+ "	`strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N', "
		+ "	`strClientCode` VARCHAR(10) NOT NULL, "
		+ "	PRIMARY KEY (`strPOSCode`, `strLinkupCode`, `strClientCode`) "
		+ ") "
		+ "COLLATE='utf8_general_ci' "
		+ "ENGINE=InnoDB "
		+ ";";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblmenuitempricingdtl` "
		+ "	CHANGE COLUMN `longPricingId` `longPricingId` BIGINT(20) NOT NULL AUTO_INCREMENT AFTER `strHourlyPricing`, "
		+ "	ADD PRIMARY KEY (`longPricingId`);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbilldtl` "
		+ "CHANGE COLUMN `dblQuantity` `dblQuantity` DECIMAL(18,3) NOT NULL AFTER `dblRate`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbilldtl` "
		+ "CHANGE COLUMN `dblQuantity` `dblQuantity` DECIMAL(18,3) NOT NULL AFTER `dblRate`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblitemmaster` "
		+ "ADD COLUMN `strUOM` VARCHAR(5) NOT NULL DEFAULT '' AFTER `strUrgentOrder`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblcustomermaster` "
		+ "ENGINE=InnoDB";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ "ADD COLUMN `strJioMoneyIntegration` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strShowPopUpForNextItemQuantity`,"
		+ "ADD COLUMN `strJioWebServiceUrl` VARCHAR(255) NOT NULL DEFAULT '' AFTER `strJioMoneyIntegration`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE tblbillhd	CHANGE COLUMN `strBillNo` `strBillNo` VARCHAR(15);";
	mapStructureUpdater.get("tblStructure").add(sql);
	sql = "ALTER TABLE tblbilldtl	CHANGE COLUMN `strBillNo` `strBillNo` VARCHAR(15);";
	mapStructureUpdater.get("tblStructure").add(sql);
	sql = "ALTER TABLE tblbillmodifierdtl	CHANGE COLUMN `strBillNo` `strBillNo` VARCHAR(15);";
	mapStructureUpdater.get("tblStructure").add(sql);
	sql = "ALTER TABLE tblbilldiscdtl	CHANGE COLUMN `strBillNo` `strBillNo` VARCHAR(15);";
	mapStructureUpdater.get("tblStructure").add(sql);
	sql = "ALTER TABLE tblbillpromotiondtl	CHANGE COLUMN `strBillNo` `strBillNo` VARCHAR(15);";
	mapStructureUpdater.get("tblStructure").add(sql);
	sql = "ALTER TABLE tblbillcomplementrydtl	CHANGE COLUMN `strBillNo` `strBillNo` VARCHAR(15);";
	mapStructureUpdater.get("tblStructure").add(sql);
	sql = "ALTER TABLE tblbillseriesbilldtl	CHANGE COLUMN `strHdBillNo` `strHdBillNo` VARCHAR(15);";
	mapStructureUpdater.get("tblStructure").add(sql);
	sql = "ALTER TABLE tblbillseriesbilldtl	CHANGE COLUMN `strDtlBillNos` `strDtlBillNos` VARCHAR(200);";
	mapStructureUpdater.get("tblStructure").add(sql);
	sql = "ALTER TABLE tblbillsettlementdtl	CHANGE COLUMN `strBillNo` `strBillNo` VARCHAR(15);";
	mapStructureUpdater.get("tblStructure").add(sql);
	sql = "ALTER TABLE tblbilltaxdtl	CHANGE COLUMN `strBillNo` `strBillNo` VARCHAR(15);";
	mapStructureUpdater.get("tblStructure").add(sql);
	sql = "ALTER TABLE tblqbillhd	CHANGE COLUMN `strBillNo` `strBillNo` VARCHAR(15);";
	mapStructureUpdater.get("tblStructure").add(sql);
	sql = "ALTER TABLE tblqbilldtl	CHANGE COLUMN `strBillNo` `strBillNo` VARCHAR(15);";
	mapStructureUpdater.get("tblStructure").add(sql);
	sql = "ALTER TABLE tblqbillmodifierdtl	CHANGE COLUMN `strBillNo` `strBillNo` VARCHAR(15);";
	mapStructureUpdater.get("tblStructure").add(sql);
	sql = "ALTER TABLE tblqbilldiscdtl	CHANGE COLUMN `strBillNo` `strBillNo` VARCHAR(15);";
	mapStructureUpdater.get("tblStructure").add(sql);
	sql = "ALTER TABLE tblqbillpromotiondtl	CHANGE COLUMN `strBillNo` `strBillNo` VARCHAR(15);";
	mapStructureUpdater.get("tblStructure").add(sql);
	sql = "ALTER TABLE tblqbillcomplementrydtl	CHANGE COLUMN `strBillNo` `strBillNo` VARCHAR(15);";
	mapStructureUpdater.get("tblStructure").add(sql);
	sql = "ALTER TABLE tblqbillsettlementdtl	CHANGE COLUMN `strBillNo` `strBillNo` VARCHAR(15);";
	mapStructureUpdater.get("tblStructure").add(sql);
	sql = "ALTER TABLE tblqbilltaxdtl	CHANGE COLUMN `strBillNo` `strBillNo` VARCHAR(15);";
	mapStructureUpdater.get("tblStructure").add(sql);
	sql = "ALTER TABLE tblvoidbill	CHANGE COLUMN `strBillNo` `strBillNo` VARCHAR(15);";
	mapStructureUpdater.get("tblStructure").add(sql);
	sql = "ALTER TABLE tblvoidbilldtl	CHANGE COLUMN `strBillNo` `strBillNo` VARCHAR(15);";
	mapStructureUpdater.get("tblStructure").add(sql);
	sql = "ALTER TABLE tblvoidbillhd	CHANGE COLUMN `strBillNo` `strBillNo` VARCHAR(15);";
	mapStructureUpdater.get("tblStructure").add(sql);
	sql = "ALTER TABLE tblvoidbillsettlementdtl	CHANGE COLUMN `strBillNo` `strBillNo` VARCHAR(15);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblitemmasterlinkupdtl` "
		+ "ADD COLUMN `strExciseBrandCode` VARCHAR(20) NOT NULL DEFAULT '' AFTER `strWSProductName`, "
		+ "ADD COLUMN `strExciseBrandName` VARCHAR(200) NOT NULL DEFAULT '' AFTER `strExciseBrandCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblposmaster` "
		+ "	ADD COLUMN `strExciseLicenceName` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strWSLocationName`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbldayendprocess`ADD COLUMN `strExciseBillGeneration` VARCHAR(20) NOT NULL DEFAULT '' AFTER `dblTipAmt`";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblcustomermaster` "
		+ "	ADD COLUMN `strTempAddress` VARCHAR(200) NOT NULL DEFAULT '' AFTER `strCustAddress`, "
		+ "	ADD COLUMN `strTempStreet` VARCHAR(200) NOT NULL DEFAULT '' AFTER `strTempAddress`, "
		+ "	ADD COLUMN `strTempLandmark` VARCHAR(200) NOT NULL DEFAULT '' AFTER `strTempStreet`; ";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblimportexcel` "
		+ "ADD COLUMN `strUOM` VARCHAR(5) NOT NULL DEFAULT '' AFTER `strDOB`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblmodifiermaster set strModifierName= TRIM(strModifierName) ;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = " ALTER TABLE `tblsetup` ADD COLUMN `strJioMID` VARCHAR(15) NOT NULL AFTER `strJioWebServiceUrl`,"
		+ " ADD COLUMN `strJioTID` VARCHAR(8) NOT NULL AFTER `strJioMID`,ADD COLUMN `strJioActivationCode` VARCHAR(16) NOT NULL AFTER `strJioTID`; ";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ "ADD COLUMN `strJioDeviceID` VARCHAR(20) NOT NULL AFTER `strJioActivationCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ "ADD COLUMN `strNewBillSeriesForNewDay` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strJioDeviceID`, "
		+ "ADD COLUMN `strShowReportsPOSWise` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strNewBillSeriesForNewDay`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblitemmasterlinkupdtl` "
		+ "ADD PRIMARY KEY (`strItemCode`, `strPOSCode`, `strClientCode`);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbillseriesbilldtl` "
		+ "CHANGE COLUMN `strHdBillNo` `strHdBillNo` VARCHAR(15) NOT NULL AFTER `strBillSeries`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbillseriesbilldtl` "
		+ "ADD COLUMN `dteBillDate` DATETIME NOT NULL AFTER `dteEditedDate`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbillseriesbilldtl` "
		+ "ADD PRIMARY KEY (`strPOSCode`, `strHdBillNo`, `strClientCode`, `dteBillDate`);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE if not exists `tblregisterterminal` ( "
		+ "	`strClientCode` VARCHAR(20) NOT NULL, "
		+ "	`strHOSTName` VARCHAR(30) NOT NULL, "
		+ "	`strMACAddress` VARCHAR(20) NOT NULL, "
		+ " `strTerminalName` VARCHAR(10) NOT NULL, "
		+ " `strUserCreated` VARCHAR(10) NOT NULL, "
		+ "	`dteDateCreated` DATETIME NOT NULL, "
		+ "	`strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N', "
		+ "	PRIMARY KEY (`strClientCode`, `strMACAddress`) "
		+ ") "
		+ "COLLATE='utf8_general_ci' "
		+ "ENGINE=InnoDB "
		+ ";";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE if not exists `tbldayendreports` ( "
		+ "	`strPOSCode` VARCHAR(20) NOT NULL , "
		+ "	`strClientCode` VARCHAR(11) NOT NULL , "
		+ "	`strReportName` VARCHAR(200) NOT NULL, "
		+ "	`dtePOSDate` DATETIME NOT NULL, "
		+ "	`strUserCreated` VARCHAR(10) NOT NULL, "
		+ "	`strUserEdited` VARCHAR(10) NOT NULL, "
		+ "	`dteDateCreated` DATETIME NOT NULL, "
		+ "	`dteDateEdited` DATETIME NOT NULL, "
		+ "	`strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N', "
		+ "	PRIMARY KEY (`strPOSCode`,`strClientCode`,`strReportName`) "
		+ ") "
		+ "COLLATE='utf8_general_ci' "
		+ "ENGINE=InnoDB "
		+ ";";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE if not exists `tblconfig` ("
		+ " `strServer` VARCHAR(100) NOT NULL DEFAULT 'mysql', "
		+ "`strDBName` VARCHAR(100) NOT NULL DEFAULT 'jpos', "
		+ "`strUserID` VARCHAR(100) NOT NULL DEFAULT 'root', "
		+ "`strPassword` VARCHAR(100) NOT NULL DEFAULT 'root', "
		+ "`strIPAddress` VARCHAR(100) NOT NULL DEFAULT 'localhost', "
		+ "`strPort` VARCHAR(100) NOT NULL DEFAULT '3306', "
		+ "`strBackupPath` VARCHAR(500) NOT NULL DEFAULT '', "
		+ "`strExportPath` VARCHAR(500) NOT NULL DEFAULT '', "
		+ "`strImagePath` VARCHAR(500) NOT NULL DEFAULT '', "
		+ "`strHOWebServiceUrl` VARCHAR(255) NOT NULL DEFAULT '', "
		+ "`strMMSWebServiceUrl` VARCHAR(255) NOT NULL DEFAULT '', "
		+ "`strOS` VARCHAR(100) NOT NULL DEFAULT 'windows', "
		+ "`strDefaultPrinter` VARCHAR(100) NOT NULL DEFAULT '', "
		+ "`strPrinterType` VARCHAR(100) NOT NULL DEFAULT 'Inbuild', "
		+ "`strTouchScreenMode` VARCHAR(100) NOT NULL DEFAULT 'false', "
		+ "`strServerFilePath` VARCHAR(500) NOT NULL DEFAULT '', "
		+ "`strSelectWaiterFromCardSwipe` VARCHAR(100) NOT NULL DEFAULT 'false', "
		+ "`strMySQBackupFilePath` VARCHAR(500) NOT NULL DEFAULT '', "
		+ "`strHOCommunication` VARCHAR(100) NOT NULL DEFAULT 'true', "
		+ "`strAdvReceiptPrinter` VARCHAR(100) NOT NULL DEFAULT '', "
		+ "`strClientCode` VARCHAR(10) NOT NULL DEFAULT '', "
		+ "PRIMARY KEY (`strClientCode`) "
		+ ") "
		+ "COLLATE='utf8_general_ci' "
		+ "ENGINE=InnoDB "
		+ ";";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblhomedelivery` "
		+ "ADD COLUMN `dblLooseCashAmt` DOUBLE(18,2) NOT NULL DEFAULT '0.00' AFTER `dblHomeDeliCharge`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = " ALTER TABLE `tblbillhd` "
		+ "ADD COLUMN `strJioMoneyRRefNo` VARCHAR(20) NOT NULL DEFAULT '' AFTER `strTransactionType`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = " ALTER TABLE `tblqbillhd` "
		+ "ADD COLUMN `strJioMoneyRRefNo` VARCHAR(20) NOT NULL DEFAULT '' AFTER `strTransactionType`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	//             sql ="ALTER TABLE `tblsetup` " 
//                 +"DROP COLUMN `strEnableDineIn`, "
//                 +"DROP COLUMN `strAutoAreaSelectionInMakeKOT`, "
//                 +"DROP COLUMN `strConsolidatedKOTPrinterPort`;";
//            i = ExecuteQuery(sql);
	sql = "ALTER TABLE `tblsetup` "
		+ "ADD COLUMN `strEnableDineIn` VARCHAR(1) NOT NULL DEFAULT 'Y' AFTER `strShowReportsPOSWise`; ";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE if not exists `tblcreditbillreceipthd` ( "
		+ "`strReceiptNo` VARCHAR(15) NOT NULL, "
		+ "`strBillNo` VARCHAR(15) NOT NULL, "
		+ "`strPOSCode` VARCHAR(10) NOT NULL, "
		+ "`dteReceiptDate` DATE NOT NULL, "
		+ "`dblReceiptAmt` DECIMAL(18,2) NOT NULL DEFAULT '0.00', "
		+ "`intShiftCode` INT(11) NOT NULL, "
		+ "`strUserCreated` VARCHAR(10) NOT NULL, "
		+ "`strUserEdited` VARCHAR(10) NOT NULL, "
		+ "`dteDateCreated` DATETIME NOT NULL, "
		+ "`dteDateEdited` DATETIME NOT NULL, "
		+ "`strClientCode` VARCHAR(10) NOT NULL, "
		+ "`strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N', "
		+ "PRIMARY KEY (`strReceiptNo`, `strBillNo`, `strPOSCode`, `strClientCode`) "
		+ ") "
		+ "COLLATE='utf8_general_ci' "
		+ "ENGINE=InnoDB "
		+ ";";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE if not exists `tblqcreditbillreceipthd` ( "
		+ "`strReceiptNo` VARCHAR(15) NOT NULL, "
		+ "`strBillNo` VARCHAR(15) NOT NULL, "
		+ "`strPOSCode` VARCHAR(10) NOT NULL, "
		+ "`dteReceiptDate` DATE NOT NULL, "
		+ "`dblReceiptAmt` DECIMAL(18,2) NOT NULL DEFAULT '0.00', "
		+ "`intShiftCode` INT(11) NOT NULL, "
		+ "`strUserCreated` VARCHAR(10) NOT NULL, "
		+ "`strUserEdited` VARCHAR(10) NOT NULL, "
		+ "`dteDateCreated` DATETIME NOT NULL, "
		+ "`dteDateEdited` DATETIME NOT NULL, "
		+ "`strClientCode` VARCHAR(10) NOT NULL, "
		+ "`strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N', "
		+ "PRIMARY KEY (`strReceiptNo`, `strBillNo`, `strPOSCode`, `strClientCode`) "
		+ ") "
		+ "COLLATE='utf8_general_ci' "
		+ "ENGINE=InnoDB "
		+ ";";
	mapStructureUpdater.get("tblStructure").add(sql);

	//funForButtonSequenceStrucureUpdate();
	mapStructureUpdater.get("tblStructure").add("funForButtonSequenceStrucureUpdate");

	sql = "ALTER TABLE `tblgrouphd` "
		+ "ADD COLUMN `strGroupShortName` VARCHAR(50) NOT NULL DEFAULT '' AFTER `strOperationalYN`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tbltablemaster set strPOSCode='P01' where strPOSCode='All';";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblitemrtemp set strPOSCode='P01' where strPOSCode='All';";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblitemrtemp_bck set strPOSCode='P01' where strPOSCode='All';";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE if not exists `tbltaxongroup` (`strTaxCode` VARCHAR(10) NOT NULL, "
		+ "`strGroupCode` VARCHAR(10) NOT NULL, "
		+ "`strGroupName` VARCHAR(50) NOT NULL, "
		+ "`strApplicable` VARCHAR(5) NOT NULL, "
		+ "`dteFrom` DATETIME NOT NULL, "
		+ "`dteTo` DATETIME NOT NULL, "
		+ "`strUserCreated` VARCHAR(10) NOT NULL, "
		+ "`strUserEdited` VARCHAR(10) NOT NULL, "
		+ "`dteDateCreated` DATETIME NOT NULL, "
		+ "`dteDateEdited` DATETIME NOT NULL, "
		+ "`strClientCode` VARCHAR(11) NOT NULL , "
		+ "INDEX `strTaxCode_strGroupCode_strClientCode` (`strTaxCode`, `strGroupCode`, `strClientCode`) "
		+ ") "
		+ "COLLATE='utf8_general_ci' "
		+ "ENGINE=InnoDB "
		+ ";";
	mapStructureUpdater.get("tblStructure").add(sql);

	//funInsertTaxObGroupData();
	mapStructureUpdater.get("tblStructure").add("funInsertTaxObGroupData");

	sql = "ALTER TABLE `tblitemmasterlinkupdtl` DROP PRIMARY KEY;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblitemmasterlinkupdtl` "
		+ "ADD INDEX `strItemCode_strPOSCode_strWSProductCode_strClientCode` (`strItemCode`, `strPOSCode`, `strWSProductCode`, `strClientCode`);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = " ALTER TABLE `tblbillhd` "
		+ " ADD COLUMN `strJioMoneyAuthCode` VARCHAR(10) NOT NULL DEFAULT '' AFTER `strJioMoneyRRefNo`, "
		+ " ADD COLUMN `strJioMoneyTxnId` VARCHAR(25) NOT NULL DEFAULT '' AFTER `strJioMoneyAuthCode`, "
		+ " ADD COLUMN `strJioMoneyTxnDateTime` VARCHAR(20) NOT NULL DEFAULT '' AFTER `strJioMoneyTxnId`,"
		+ " ADD COLUMN `strJioMoneyCardNo` VARCHAR(16) NOT NULL DEFAULT '' AFTER `strJioMoneyTxnDateTime`,"
		+ " ADD COLUMN `strJioMoneyCardType` VARCHAR(40) NOT NULL DEFAULT '' AFTER `strJioMoneyCardNo` ;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = " ALTER TABLE `tblqbillhd` "
		+ " ADD COLUMN `strJioMoneyAuthCode` VARCHAR(10) NOT NULL DEFAULT '' AFTER `strJioMoneyRRefNo`, "
		+ " ADD COLUMN `strJioMoneyTxnId` VARCHAR(25) NOT NULL DEFAULT '' AFTER `strJioMoneyAuthCode`, "
		+ " ADD COLUMN `strJioMoneyTxnDateTime` VARCHAR(20) NOT NULL DEFAULT '' AFTER `strJioMoneyTxnId`,"
		+ " ADD COLUMN `strJioMoneyCardNo` VARCHAR(16) NOT NULL DEFAULT '' AFTER `strJioMoneyTxnDateTime`,"
		+ " ADD COLUMN `strJioMoneyCardType` VARCHAR(40) NOT NULL DEFAULT '' AFTER `strJioMoneyCardNo` ;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ "ADD COLUMN `strAutoAreaSelectionInMakeKOT` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strEnableDineIn`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ "ADD COLUMN `strConsolidatedKOTPrinterPort` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strAutoAreaSelectionInMakeKOT`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblareamaster` "
		+ "ADD COLUMN `strMACAddress` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strPOSCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE if not exists `tblnonavailableitems` ( "
		+ "`strItemCode` VARCHAR(10) NOT NULL, "
		+ "`strItemName` VARCHAR(100) NOT NULL, "
		+ "`strClientCode` VARCHAR(10) NOT NULL, "
		+ "`dteDate` DATETIME NOT NULL, "
		+ "`strPOSCode` VARCHAR(10) NOT NULL, "
		+ "PRIMARY KEY (`strItemCode`,`strPOSCode`,`strClientCode`) "
		+ ")COLLATE='utf8_general_ci' "
		+ "ENGINE=InnoDB ;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblmenuhd` "
		+ "ADD COLUMN `strImagePath` VARCHAR(500) NOT NULL DEFAULT '' AFTER `strOperational`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblmenuhd` "
		+ "ADD COLUMN `imgImage` LONGBLOB NOT NULL AFTER `strImagePath`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblitemmaster` "
		+ "ADD COLUMN `imgImage` LONGBLOB NOT NULL AFTER `strUOM`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblitemmaster` "
		+ "CHANGE COLUMN `strItemImage` `strItemImage` VARCHAR(200) NOT NULL DEFAULT '' AFTER `strSubGroupCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbilldtl` "
		+ "CHANGE COLUMN `strItemName` `strItemName` VARCHAR(200) NOT NULL AFTER `strItemCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbilldtl` "
		+ "CHANGE COLUMN `strItemName` `strItemName` VARCHAR(200) NOT NULL AFTER `strItemCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbilldiscdtl` "
		+ "CHANGE COLUMN `strDiscOnValue` `strDiscOnValue` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strDiscOnType`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbillcomplementrydtl` "
		+ "CHANGE COLUMN `strItemName` `strItemName` VARCHAR(200) NOT NULL AFTER `strItemCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbillcomplementrydtl` "
		+ "CHANGE COLUMN `strItemName` `strItemName` VARCHAR(200) NOT NULL AFTER `strItemCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblvoidbilldtl` "
		+ "CHANGE COLUMN `strItemName` `strItemName` VARCHAR(200) NOT NULL AFTER `strItemCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbluserdtl` "
		+ "DROP COLUMN `strClientCode`, "
		+ "DROP COLUMN `dteDateCreated`, "
		+ "DROP COLUMN `dteDateEdited`, "
		+ "DROP COLUMN `strDataPostFlag`, "
		+ "DROP COLUMN `strDebitCardString`, "
		+ "DROP COLUMN `strOperationalYN`, "
		+ "DROP COLUMN `strSuperType`, "
		+ "DROP COLUMN `strUserCreated`, "
		+ "DROP COLUMN `strUserEdited`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsuperuserdtl` "
		+ "DROP COLUMN `strClientCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblsetup set strBillFormatType='Text 5' where strBillFormatType='Format 5';";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbltempsalesflash` "
		+ "CHANGE COLUMN `strname` `strname` VARCHAR(200) NOT NULL DEFAULT '' AFTER `strcode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbilldiscdtl` "
		+ "ADD COLUMN `dteBillDate` DATETIME NOT NULL AFTER `strDataPostFlag`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbilldiscdtl` "
		+ "ADD COLUMN `dteBillDate` DATETIME NOT NULL AFTER `strDataPostFlag`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbillmodifierdtl` "
		+ "ADD COLUMN `dteBillDate` DATETIME NOT NULL AFTER `dblDiscAmt`;  ";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbillmodifierdtl` "
		+ "ADD COLUMN `dteBillDate` DATETIME NOT NULL AFTER `dblDiscAmt`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbillpromotiondtl` "
		+ "ADD COLUMN `dteBillDate` DATETIME NOT NULL AFTER `dblDiscountAmt`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbillpromotiondtl` "
		+ "ADD COLUMN `dteBillDate` DATETIME NOT NULL AFTER `dblDiscountAmt`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbillsettlementdtl` "
		+ "DROP PRIMARY KEY;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbillsettlementdtl` "
		+ "ADD INDEX `strBillNo` (`strBillNo`);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbillsettlementdtl` "
		+ "ADD COLUMN `dteBillDate` DATETIME NOT NULL AFTER `strRoomNo`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbillsettlementdtl` "
		+ "DROP PRIMARY KEY;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbillsettlementdtl` "
		+ "ADD INDEX `strBillNo` (`strBillNo`);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbillsettlementdtl` "
		+ "ADD COLUMN `dteBillDate` DATETIME NOT NULL AFTER `strRoomNo`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbilltaxdtl` "
		+ "DROP PRIMARY KEY;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbilltaxdtl` "
		+ "ADD INDEX `strBillNo` (`strBillNo`);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbilltaxdtl` "
		+ "ADD COLUMN `dteBillDate` DATETIME NOT NULL AFTER `strDataPostFlag`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbilltaxdtl` "
		+ "DROP PRIMARY KEY;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbilltaxdtl` "
		+ "ADD INDEX `strBillNo` (`strBillNo`);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbilltaxdtl` "
		+ "ADD COLUMN `dteBillDate` DATETIME NOT NULL AFTER `strDataPostFlag`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblhomedeldtl` "
		+ "ADD COLUMN `dteBillDate` DATETIME NOT NULL AFTER `dblDBIncentives`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblvoidmodifierdtl` "
		+ "ADD COLUMN `dteBillDate` DATETIME NOT NULL AFTER `strReasonCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblvoidbillsettlementdtl` "
		+ "ADD COLUMN `dteBillDate` DATETIME NOT NULL AFTER `strDataPostFlag`;";
	mapStructureUpdater.get("tblStructure").add(sql);
	//add indexing.....
	//funAddIndexing();
	mapStructureUpdater.get("tblStructure").add("funAddIndexing");

	//updating billdate to old data
	//funUpdateBillDate();
	mapStructureUpdater.get("tblStructure").add("funUpdateBillDate");

	sql = "ALTER TABLE `tblbillhd` "
		+ "	DROP PRIMARY KEY, "
		+ "	DROP INDEX `intVouchno`, "
		+ "	DROP INDEX `strBillNo`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbillhd` "
		+ "	ADD INDEX `strBillNo` (`strBillNo`); "
		+ "	";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "	ALTER TABLE `tblqbillhd` "
		+ "	DROP PRIMARY KEY, "
		+ "	DROP INDEX `strBillNo`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "	ALTER TABLE `tblqbillhd` "
		+ "	ADD INDEX `strBillNo` (`strBillNo`);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "	ALTER TABLE `tbladvbookbillhd` "
		+ "	DROP PRIMARY KEY;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbladvbookbillhd` "
		+ "	ADD INDEX `strAdvBookingNo` (`strAdvBookingNo`);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "	ALTER TABLE `tblqadvbookbillhd` "
		+ "	DROP PRIMARY KEY;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqadvbookbillhd` "
		+ "	ADD INDEX `strAdvBookingNo` (`strAdvBookingNo`);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblpmspostingbilldtl` "
		+ "  DROP PRIMARY KEY;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblpmspostingbilldtl` "
		+ "  ADD INDEX `strBillNo` (`strBillNo`);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblpmspostingbilldtl` "
		+ "ADD COLUMN `strBillType` VARCHAR(10) NOT NULL AFTER `strPMSDataPostFlag`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblbillhd set strOperationType='DineIn' where strOperationType='Dine In';";
	mapStructureUpdater.get("tblStructure").add(sql);
	sql = "update tblbillhd set strOperationType='TakeAway' where strOperationType='Take Away';";
	mapStructureUpdater.get("tblStructure").add(sql);
	sql = "update tblbillhd set strOperationType='HomeDelivery' where strOperationType='Home Delivery';";
	mapStructureUpdater.get("tblStructure").add(sql);
	sql = "update tblbillhd set strOperationType='DirectBiller' where strOperationType='Direct Biller';";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblqbillhd set strOperationType='DineIn' where strOperationType='Dine In';";
	mapStructureUpdater.get("tblStructure").add(sql);
	sql = "update tblqbillhd set strOperationType='TakeAway' where strOperationType='Take Away';";
	mapStructureUpdater.get("tblStructure").add(sql);
	sql = "update tblqbillhd set strOperationType='HomeDelivery' where strOperationType='Home Delivery';";
	mapStructureUpdater.get("tblStructure").add(sql);
	sql = "update tblqbillhd set strOperationType='DirectBiller' where strOperationType='Direct Biller';";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblitemmodofier` "
		+ "ADD PRIMARY KEY (`strItemCode`, `strModifierCode`);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE if not exists `tblsmssetup` ( "
		+ " `strPOSCode` VARCHAR(10) NOT NULL, "
		+ " `strTransactionName` VARCHAR(50) NOT NULL, "
		+ " `strSendSMSYN` VARCHAR(5) NOT NULL DEFAULT 'N', "
		+ " `longMobileNo` BIGINT(20) NOT NULL , "
		+ " `strUserCreated` VARCHAR(10) NOT NULL, "
		+ " `strUserEdited` VARCHAR(10) NOT NULL, "
		+ " `dteDateCreated` DATETIME NOT NULL, "
		+ " `dteDateEdited` DATETIME NOT NULL, "
		+ " `strClientCode` VARCHAR(10) NOT NULL , "
		+ " `strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N', "
		+ " INDEX `strTransactionName` (`strTransactionName`) "
		+ ") "
		+ "COLLATE='utf8_general_ci' "
		+ "ENGINE=InnoDB "
		+ ";";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsmssetup` "
		+ "CHANGE COLUMN `longMobileNo` `longMobileNo` VARCHAR(100) NOT NULL AFTER `strSendSMSYN`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ "ADD COLUMN `dblRoundOff` DECIMAL(18,2) NOT NULL DEFAULT '1.00' AFTER `strConsolidatedKOTPrinterPort`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbillhd` "
		+ "	ADD COLUMN `dblRoundOff` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `strJioMoneyCardType`;";
	mapStructureUpdater.get("tblStructure").add(sql);
	sql = "ALTER TABLE `tblqbillhd` "
		+ "	ADD COLUMN `dblRoundOff` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `strJioMoneyCardType`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblposmaster` "
		+ "	ADD COLUMN `strClientCode` VARCHAR(11) NOT NULL DEFAULT '' AFTER `strExciseLicenceName`;";
	mapStructureUpdater.get("tblStructure").add(sql);
	sql = "ALTER TABLE `tblsettlementtax` "
		+ "	ADD COLUMN `strClientCode` VARCHAR(11) NOT NULL DEFAULT '' AFTER `dteDateEdited`;";
	mapStructureUpdater.get("tblStructure").add(sql);
	sql = "ALTER TABLE `tbltaxposdtl` "
		+ "	ADD COLUMN `strClientCode` VARCHAR(11) NOT NULL DEFAULT '' AFTER `strTaxDesc`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblshiftmaster` "
		+ "	DROP COLUMN `strClientCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbltdhcomboitemdtl` "
		+ "	DROP COLUMN `strClientCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbltdhcomboitemdtl` "
		+ "	ADD COLUMN `strClientCode` VARCHAR(11) NOT NULL DEFAULT '' AFTER `strSubItemMenuCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);
	sql = "ALTER TABLE `tblshiftmaster` "
		+ "	ADD COLUMN `strClientCode` VARCHAR(11) NOT NULL DEFAULT '' AFTER `dteDateEdited`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblqbillhd  "
		+ "set dblRoundOff=(dblGrandTotal-(dblSubTotal-dblDiscountAmt+dblTaxAmt)) "
		+ "where dblRoundOff=0.00;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblbillhd  "
		+ "set dblRoundOff=(dblGrandTotal-(dblSubTotal-dblDiscountAmt+dblTaxAmt)) "
		+ "where dblRoundOff=0.00;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ "	ADD COLUMN `strShowUnSettlementForm` VARCHAR(1) NOT NULL DEFAULT 'Y' AFTER `dblRoundOff`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbltaxhd` "
		+ "	ADD COLUMN `strBillNote` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strTaxShortName`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	//update billNote-Service Tax no OR VAT no in tax master
	//funUpdateBillNote();
	mapStructureUpdater.get("tblStructure").add("funUpdateBillNote");

	sql = "ALTER TABLE `tblsetup` "
		+ "	ADD COLUMN `strPrintOpenItemsOnBill` VARCHAR(1) NOT NULL DEFAULT 'Y' AFTER `strShowUnSettlementForm`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblitemmaster set strTaxIndicator='' ";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tbltaxhd set strTaxIndicator='' ";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblpromotionmaster` ADD COLUMN `strAreaCode` VARCHAR(10) NOT NULL AFTER `strGetPromoOn`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblpromotionmaster set strAreaCode=(select strAreaCode from tblareamaster where strAreaName='All') "
		+ "where strAreaCode='';";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblcustomermaster` "
		+ "	CHANGE COLUMN `longMobileNo` `longMobileNo` VARCHAR(100) NOT NULL DEFAULT '' AFTER `intPinCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ "	ADD COLUMN `strPrintHomeDeliveryYN` VARCHAR(1) NOT NULL DEFAULT 'Y' AFTER `strPrintOpenItemsOnBill`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsettelmenthd` "
		+ "	ADD COLUMN `strCreditReceiptYN` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strBillPrintOnSettlement`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblcreditbillreceipthd` "
		+ "	DROP PRIMARY KEY;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblcreditbillreceipthd` "
		+ "	ADD COLUMN `dteBillDate` DATETIME NOT NULL AFTER `strDataPostFlag`, "
		+ "	ADD COLUMN `strSettlementCode` VARCHAR(10) NOT NULL AFTER `dteBillDate`, "
		+ "	ADD COLUMN `strSettlementName` VARCHAR(50) NOT NULL AFTER `strSettlementCode`, "
		+ "	ADD COLUMN `strChequeNo` VARCHAR(50) NOT NULL DEFAULT '' AFTER `strSettlementName`, "
		+ "	ADD COLUMN `strBankName` VARCHAR(50) NOT NULL DEFAULT '' AFTER `strChequeNo`, "
		+ "	ADD COLUMN `dteChequeDate` DATETIME NOT NULL DEFAULT '1990-01-01 00:00:00' AFTER `strBankName`, "
		+ "	ADD COLUMN `strRemarks` VARCHAR(50) NOT NULL DEFAULT '' AFTER `dteChequeDate`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblcreditbillreceipthd` "
		+ "	ADD INDEX `strReceiptNo_strBillNo_strPOSCode_dteBillDate` (`strReceiptNo`, `strBillNo`, `strPOSCode`, `dteBillDate`); ";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqcreditbillreceipthd` "
		+ "	DROP PRIMARY KEY;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqcreditbillreceipthd` "
		+ "	ADD COLUMN `dteBillDate` DATETIME NOT NULL AFTER `strDataPostFlag`, "
		+ "	ADD COLUMN `strSettlementCode` VARCHAR(10) NOT NULL AFTER `dteBillDate`, "
		+ "	ADD COLUMN `strSettlementName` VARCHAR(50) NOT NULL AFTER `strSettlementCode`, "
		+ "	ADD COLUMN `strChequeNo` VARCHAR(50) NOT NULL DEFAULT '' AFTER `strSettlementName`, "
		+ "	ADD COLUMN `strBankName` VARCHAR(50) NOT NULL DEFAULT '' AFTER `strChequeNo`, "
		+ "	ADD COLUMN `dteChequeDate` DATETIME NOT NULL DEFAULT '1990-01-01 00:00:00' AFTER `strBankName`, "
		+ "	ADD COLUMN `strRemarks` VARCHAR(50) NOT NULL DEFAULT '' AFTER `dteChequeDate`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqcreditbillreceipthd` "
		+ "	ADD INDEX `strReceiptNo_strBillNo_strPOSCode_dteBillDate` (`strReceiptNo`, `strBillNo`, `strPOSCode`, `dteBillDate`); ";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE if not exists `tblpromogroupmaster` (`strPromoGroupCode` VARCHAR(10) NOT NULL,"
		+ "	`strPromoGroupName` VARCHAR(100) NOT NULL,`strClientCode` VARCHAR(10) NOT NULL,"
		+ "	PRIMARY KEY (`strPromoGroupCode`, `strClientCode`)) "
		+ " COLLATE='utf8_general_ci' ENGINE=InnoDB;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblpromogroupmaster` "
		+ "	ADD COLUMN `strUserCreated` VARCHAR(10) NOT NULL AFTER `strClientCode`, "
		+ "	ADD COLUMN `strUserEdited` VARCHAR(10) NOT NULL AFTER `strUserCreated`, "
		+ "	ADD COLUMN `dteDateCreated` DATETIME NOT NULL AFTER `strUserEdited`, "
		+ "	ADD COLUMN `dteDateEdited` DATETIME NOT NULL AFTER `dteDateCreated`, "
		+ "	ADD COLUMN `strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `dteDateEdited`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE if not exists `tblpromogroupdtl` ( "
		+ "	`strPromoGroupCode` VARCHAR(10) NOT NULL, "
		+ "	`strItemCode` VARCHAR(10) NOT NULL, "
		+ "	`strMenuCode` VARCHAR(10) NOT NULL, "
		+ "	`strClientCode` VARCHAR(10) NOT NULL "
		+ ") "
		+ "COLLATE='utf8_general_ci' "
		+ "ENGINE=InnoDB "
		+ ";";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblpromogroupdtl` "
		+ "	ADD COLUMN `strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strClientCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblpromotionmaster` "
		+ "	ADD COLUMN `strPromoGroupType` VARCHAR(50) NOT NULL DEFAULT '' AFTER `strAreaCode`, "
		+ "	ADD COLUMN `longKOTTimeBound` INT NOT NULL DEFAULT '0' AFTER `strPromoGroupType`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = " ALTER TABLE `tblsetup` ADD COLUMN `strScanQRYN` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strPrintHomeDeliveryYN`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = " ALTER TABLE `tblmenuitempricingdtl` ADD COLUMN `strClientCode` VARCHAR(15) NOT NULL AFTER `longPricingId`; ";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = " 	ALTER TABLE `tblitempricingauditdtl` "
		+ "	CHANGE COLUMN `strClientCode` `strClientCode` VARCHAR(15) NOT NULL DEFAULT '' AFTER `longPricingId`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblmenuitempricingdtl set strClientCode='" + clsGlobalVarClass.gClientCode + "' where strClientCode='' ";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblitempricingauditdtl` "
		+ "	ADD COLUMN `strClientCode` VARCHAR(15) NOT NULL AFTER `longPricingId`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = " 	ALTER TABLE `tblitempricingauditdtl` "
		+ "	CHANGE COLUMN `strClientCode` `strClientCode` VARCHAR(15) NOT NULL DEFAULT '' AFTER `longPricingId`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblitempricingauditdtl set strClientCode='" + clsGlobalVarClass.gClientCode + "' where strClientCode='' ";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblitemrtemp` "
		+ "	ADD COLUMN `strItemProcessed` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strReason`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblitemrtemp_bck` "
		+ "	ADD COLUMN `strItemProcessed` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strReason`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup`	ADD COLUMN `strAreaWisePromotions` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strScanQRYN`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbldayendprocess` "
		+ "	ADD COLUMN `dblNetSale` DECIMAL(18,2) NULL DEFAULT '0.00' AFTER `strExciseBillGeneration`, "
		+ "	ADD COLUMN `dblGrossSale` DECIMAL(18,2) NULL DEFAULT '0.00' AFTER `dblNetSale`, "
		+ "	ADD COLUMN `dblAPC` DECIMAL(18,2) NULL DEFAULT '0.00' AFTER `dblGrossSale`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ "	ADD COLUMN `strPrintItemsOnMoveKOTMoveTable` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strAreaWisePromotions`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbillcomplementrydtl`	ADD COLUMN `strType` VARCHAR(20) NOT NULL DEFAULT 'Complimentary' AFTER `strSequenceNo`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbillcomplementrydtl`	ADD COLUMN `strType` VARCHAR(20) NOT NULL DEFAULT 'Complimentary' AFTER `strSequenceNo`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblitemmaster` "
		+ "	CHANGE COLUMN `intProcTimeMin` `intProcTimeMin` BIGINT NOT NULL DEFAULT '0' AFTER `dblPurchaseRate`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "	ALTER TABLE `tblitemmaster` "
		+ "	ADD COLUMN `tmeTargetMiss` BIGINT NOT NULL DEFAULT '0' AFTER `imgImage`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "	ALTER TABLE `tblbillhd` "
		+ "	ADD COLUMN `intBillSeriesPaxNo` INT NOT NULL DEFAULT '0' AFTER `dblRoundOff`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbillhd` "
		+ "	ADD COLUMN `intBillSeriesPaxNo` INT NOT NULL DEFAULT '0' AFTER `dblRoundOff`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblbillhd "
		+ "set intBillSeriesPaxNo=intPaxNo "
		+ "where intPaxNo>0 "
		+ "and intBillSeriesPaxNo=0 ;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblqbillhd "
		+ "set intBillSeriesPaxNo=intPaxNo "
		+ "where intPaxNo>0 "
		+ "and intBillSeriesPaxNo=0 ;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE `tblsuppliermaster` ( "
		+ "	`strSupplierCode` VARCHAR(10) NOT NULL , "
		+ "	`strSupplierName` VARCHAR(50) NOT NULL, "
		+ "	`strAddress1` VARCHAR(100) NOT NULL, "
		+ "	`strAddress2` VARCHAR(100) NOT NULL, "
		+ "	`strUserCreated` VARCHAR(10) NOT NULL, "
		+ "	`strUserEdited` VARCHAR(10) NOT NULL, "
		+ "	`dteDateCreated` DATETIME NOT NULL, "
		+ "	`dteDateEdited` DATETIME  NOT NULL, "
		+ "	`strClientCode` VARCHAR(10) NOT NULL, "
		+ "	`strDataPostFlag` VARCHAR(1) NOT NULL, "
		+ "		PRIMARY KEY (`strSupplierCode`, `strClientCode`) "
		+ ") "
		+ "COLLATE='utf8_general_ci' "
		+ "ENGINE=InnoDB "
		+ ";";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsuppliermaster` "
		+ "	ADD COLUMN `intMobileNumber` VARCHAR(50) NOT NULL AFTER `strDataPostFlag`, "
		+ "	ADD COLUMN `strEmailId` VARCHAR(50) NOT NULL AFTER `intMobileNumber`, "
		+ "	ADD COLUMN `strGSTNo` VARCHAR(50) NOT NULL AFTER `strEmailId`, "
		+ "	ADD COLUMN `strContactPerson` VARCHAR(100) NOT NULL AFTER `strGstNo`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE `tblpurchaseorder` ( "
		+ "	`strPOCode` VARCHAR(10) NOT NULL, "
		+ "	`dtePODate` DATETIME NOT NULL, "
		+ "	`strSupplierCode` VARCHAR(10) NOT NULL, "
		+ "	`strUserCreated` VARCHAR(10) NOT NULL, "
		+ "	`strUserEdited` VARCHAR(10) NOT NULL, "
		+ "	`dteDateCreated` DATETIME NOT NULL, "
		+ "	`dteDateEdited` DATETIME NOT NULL, "
		+ "	`strClientCode` VARCHAR(10) NOT NULL, "
		+ "	`strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N', "
		+ "	PRIMARY KEY (`strPOCode`, `strClientCode`) "
		+ ") "
		+ "COLLATE='utf8_general_ci' "
		+ "ENGINE=InnoDB "
		+ ";";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "RENAME TABLE `tblpurchaseorder` TO `tblpurchaseorderhd`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblpurchaseorderhd` "
		+ "	ADD COLUMN `dblSubTotal` DECIMAL(18,2) NOT NULL AFTER `strSupplierCode`, "
		+ "	ADD COLUMN `dblTaxAmt` DECIMAL(18,2) NOT NULL AFTER `dblSubTotal`, "
		+ "	ADD COLUMN `dblExtraAmt` DECIMAL(18,2) NOT NULL AFTER `dblTaxAmt`, "
		+ "	ADD COLUMN `dblGrandTotal` DECIMAL(18,2) NOT NULL AFTER `dblExtraAmt`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE `tblpurchaseorderdtl` ( "
		+ "	`strPOCode` VARCHAR(10) NOT NULL, "
		+ "	`strItemCode` VARCHAR(10) NOT NULL, "
		+ "	`dblOrderQty` DECIMAL(18,2) NOT NULL, "
		+ "	`dblPurchaseRate` DECIMAL(18,2) NOT NULL, "
		+ "	`dblAmount` DECIMAL(18,2) NOT NULL, "
		+ "	`strRemarks` VARCHAR(100) NOT NULL, "
		+ "	`strReasonCode` VARCHAR(10) NOT NULL, "
		+ "	`strClientCode` VARCHAR(10) NOT NULL, "
		+ "	`strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N', "
		+ "	PRIMARY KEY (`strPOCode`, `strItemCode`, `strClientCode`) "
		+ ") "
		+ "COLLATE='utf8_general_ci' "
		+ "ENGINE=InnoDB "
		+ ";";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE `tblsuppliermaster` ( "
		+ "	`strSupplierCode` VARCHAR(10) NOT NULL, "
		+ "	`strSupplierName` VARCHAR(50) NOT NULL, "
		+ "	`strAddress1` VARCHAR(100) NOT NULL, "
		+ "	`strAddress2` VARCHAR(100) NOT NULL, "
		+ "	`strUserCreated` VARCHAR(10) NOT NULL, "
		+ "	`strUserEdited` VARCHAR(10) NOT NULL, "
		+ "	`dteDateCreated` DATETIME NOT NULL, "
		+ "	`dteDateEdited` DATETIME NOT NULL, "
		+ "	`strClientCode` VARCHAR(10) NOT NULL, "
		+ "	`strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N', "
		+ "	PRIMARY KEY (`strSupplierCode`, `strClientCode`) "
		+ ") "
		+ "COLLATE='utf8_general_ci' "
		+ "ENGINE=InnoDB "
		+ ";";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblpurchaseorderdtl`	DROP COLUMN `strReasonCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` ADD COLUMN `strShowPurRateInDirectBiller` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strPrintItemsOnMoveKOTMoveTable`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblpurchaseorderhd` ADD COLUMN `dteDeliveryDate` DATETIME NOT NULL AFTER `strDataPostFlag`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbltablemaster` "
		+ "	ADD COLUMN `strNCTable` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strPOSCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblpurchaseorderhd` ADD COLUMN `strClosePO` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `dteDeliveryDate`;";
	mapStructureUpdater.get("tblStructure").add(sql);

//            // transection Table Primery Key Set Start          
	sql = " ALTER TABLE `tblbillhd`  CHANGE COLUMN `strBillNo` `strBillNo` VARCHAR(15) NOT NULL FIRST, "
		+ " ADD PRIMARY KEY (`strBillNo`, `dteBillDate`, `strClientCode`); ";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = " ALTER TABLE `tblqbillhd`  CHANGE COLUMN `strBillNo` `strBillNo` VARCHAR(15) NOT NULL FIRST, "
		+ " ADD PRIMARY KEY (`strBillNo`, `dteBillDate`, `strClientCode`); ";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbilldtl` "
		+ "	DROP PRIMARY KEY;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbilldtl` "
		+ "	DROP PRIMARY KEY;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbilldiscdtl` "
		+ "ALTER `strBillNo` DROP DEFAULT;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbilldiscdtl` "
		+ "	DROP PRIMARY KEY;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbilldiscdtl` "
		+ "ALTER `strBillNo` DROP DEFAULT;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbilldiscdtl` "
		+ "	DROP PRIMARY KEY;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbillmodifierdtl` "
		+ "ALTER `strBillNo` DROP DEFAULT;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbillmodifierdtl` "
		+ "	DROP PRIMARY KEY;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbillmodifierdtl` "
		+ "ALTER `strBillNo` DROP DEFAULT;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbillmodifierdtl` "
		+ "	DROP PRIMARY KEY;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = " ALTER TABLE `tblbilltaxdtl`  "
		+ "	ADD PRIMARY KEY (`strBillNo`, `strTaxCode`, `dteBillDate`, `strClientCode`); ";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = " ALTER TABLE `tblqbilltaxdtl`  "
		+ "	ADD PRIMARY KEY (`strBillNo`, `strTaxCode`, `dteBillDate`, `strClientCode`); ";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = " ALTER TABLE `tblbillsettlementdtl`  "
		+ "	ADD PRIMARY KEY (`strBillNo`, `strSettlementCode`, `strClientCode`, `dteBillDate`); ";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = " ALTER TABLE `tblqbillsettlementdtl`  "
		+ "	ADD PRIMARY KEY (`strBillNo`, `strSettlementCode`, `strClientCode`, `dteBillDate`); ";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbillpromotiondtl` "
		+ "	DROP PRIMARY KEY;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbillpromotiondtl` "
		+ "	DROP PRIMARY KEY;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbillseries` "
		+ "ADD PRIMARY KEY (`strPOSCode`, `strType`, `strBillSeries`, `strClientCode`);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblcreditbillreceipthd` "
		+ "ADD PRIMARY KEY (`strReceiptNo`, `strBillNo`, `strClientCode`, `dteBillDate`);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqcreditbillreceipthd` "
		+ "ADD PRIMARY KEY (`strReceiptNo`, `strBillNo`, `strClientCode`, `dteBillDate`);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblcashiermanagement` "
		+ "ADD PRIMARY KEY (`strTransactionId`, `dtDateEdited`);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbldayendprocess` "
		+ "	DROP PRIMARY KEY;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbldayendprocess` "
		+ "	ADD PRIMARY KEY (`strPOSCode`, `dtePOSDate`, `intShiftCode`);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblforms` "
		+ "ADD PRIMARY KEY (`strFormName`);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblforms` "
		+ "ADD INDEX `strModuleName` (`strModuleName`);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsettlementtax` "
		+ "ADD PRIMARY KEY (`strTaxCode`, `strSettlementCode`, `strClientCode`);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblshiftmaster` "
		+ "ADD PRIMARY KEY (`intShiftCode`, `strPOSCode`, `strClientCode`);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbluserdtl` "
		+ "ADD PRIMARY KEY (`strUserCode`, `strFormName`);";
	mapStructureUpdater.get("tblStructure").add(sql);
//            
//             // transection Table Primery Key Set End

	sql = "insert into tblqcreditbillreceipthd(select * from tblcreditbillreceipthd);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "delete from tblcreditbillreceipthd;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblvoidkot` "
		+ "	ADD COLUMN `strItemProcessed` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strRemark`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	/**
	 * update day end pax column
	 */
	//funUpdateDayEndPAX();
	mapStructureUpdater.get("tblStructure").add("funUpdateDayEndPAX");

	sql = "update tblinternal  "
		+ "set dblLastNo=(select Count(*) from tblcustomertypemaster) "
		+ "where strTransactionType='custtype' ";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblkdsprocess` "
		+ "	ADD COLUMN `strItemCode` VARCHAR(20) NOT NULL AFTER `strKDSName`, "
		+ "	ADD COLUMN `strCostCenterCode` VARCHAR(10) NOT NULL AFTER `strItemCode`, "
		+ "	ADD COLUMN `strWaiterNo` VARCHAR(20) NOT NULL AFTER `strCostCenterCode`, "
		+ "	ADD COLUMN `dteKOTDateAndTime` DATETIME NOT NULL AFTER `strWaiterNo`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblkdsprocess` DROP PRIMARY KEY;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblkdsprocess` "
		+ "	ADD INDEX `strDocNo` (`strDocNo`), "
		+ "	ADD INDEX `strItemCode` (`strItemCode`), "
		+ "	ADD INDEX `strCostCenterCode` (`strCostCenterCode`), "
		+ "	ADD INDEX `strWaiterNo` (`strWaiterNo`);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblvoidkot` "
		+ "	ADD COLUMN `strItemProcessed` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strRemark`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbillcomplementrydtl` "
		+ "	DROP PRIMARY KEY;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbillcomplementrydtl` "
		+ "	DROP PRIMARY KEY;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblkdsprocess` "
		+ " ADD COLUMN `strItemCode` VARCHAR(20) NOT NULL AFTER `strKDSName`, "
		+ " ADD COLUMN `strCostCenterCode` VARCHAR(10) NOT NULL AFTER `strItemCode`, "
		+ " ADD COLUMN `strWaiterNo` VARCHAR(20) NOT NULL AFTER `strCostCenterCode`, "
		+ " ADD COLUMN `dteKOTDateAndTime` DATETIME NOT NULL AFTER `strWaiterNo`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblkdsprocess` DROP PRIMARY KEY;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblkdsprocess` "
		+ " ADD INDEX `strDocNo` (`strDocNo`), "
		+ " ADD INDEX `strItemCode` (`strItemCode`), "
		+ " ADD INDEX `strCostCenterCode` (`strCostCenterCode`), "
		+ " ADD INDEX `strWaiterNo` (`strWaiterNo`);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblvoidkot` "
		+ " ADD COLUMN `strItemProcessed` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strRemark`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbillhd`  ADD COLUMN `dtBillDate` DATE NOT NULL AFTER `intBillSeriesPaxNo`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbillhd`  DROP PRIMARY KEY, ADD PRIMARY KEY (`strBillNo`, `strClientCode`, `dtBillDate`);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblbillhd set dtBillDate=date(dteBillDate) where dtBillDate='0000-00-00';";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbillhd` ADD COLUMN `dtBillDate` DATE NOT NULL AFTER `intBillSeriesPaxNo`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbillhd` DROP PRIMARY KEY, ADD PRIMARY KEY (`strBillNo`, `strClientCode`, `dtBillDate`);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblqbillhd set dtBillDate=date(dteBillDate) where dtBillDate='0000-00-00';";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbilldtl` ADD COLUMN `dtBillDate` DATE NOT NULL AFTER `strSequenceNo`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblbilldtl set dtBillDate=date(dteBillDate) where dtBillDate='0000-00-00';";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbilldtl`    ADD COLUMN `dtBillDate` DATE NOT NULL AFTER `strSequenceNo`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblqbilldtl set dtBillDate=date(dteBillDate) where dtBillDate='0000-00-00';";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbillseriesbilldtl`   ALTER `dteBillDate` DROP DEFAULT;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbillseriesbilldtl`   CHANGE COLUMN `dteBillDate` `dteBillDate` DATE NOT NULL AFTER `dteEditedDate`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = " ALTER TABLE `tblbillsettlementdtl`    DROP PRIMARY KEY";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbillsettlementdtl`   ALTER `dteBillDate` DROP DEFAULT;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbillsettlementdtl`   CHANGE COLUMN `dteBillDate` `dteBillDate` DATE NOT NULL AFTER `strRoomNo`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = " ALTER TABLE `tblbillsettlementdtl`     ADD PRIMARY KEY (`strBillNo`, `strClientCode`, `dteBillDate`,`strSettlementCode`)";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = " ALTER TABLE `tblqbillsettlementdtl`   DROP PRIMARY KEY";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbillsettlementdtl`  ALTER `dteBillDate` DROP DEFAULT;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = " ALTER TABLE `tblqbillsettlementdtl`  DROP PRIMARY KEY";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = " ALTER TABLE `tblqbillsettlementdtl`   ADD PRIMARY KEY (`strBillNo`, `strClientCode`, `dteBillDate`,`strSettlementCode`)";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbillsettlementdtl`  ALTER `dteBillDate` DROP DEFAULT;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbillsettlementdtl`  CHANGE COLUMN `dteBillDate` `dteBillDate` DATE NOT NULL AFTER `strRoomNo`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbillsettlementdtl` ALTER `dteBillDate` DROP DEFAULT;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbilltaxdtl`  ALTER `dteBillDate` DROP DEFAULT;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbilltaxdtl`  CHANGE COLUMN `dteBillDate` `dteBillDate` DATE NOT NULL AFTER `strDataPostFlag`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbilltaxdtl` ALTER `dteBillDate` DROP DEFAULT;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbilltaxdtl` CHANGE COLUMN `dteBillDate` `dteBillDate` DATE NOT NULL AFTER `strDataPostFlag`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbillpromotiondtl`    ALTER `dteBillDate` DROP DEFAULT;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbillpromotiondtl`    CHANGE COLUMN `dteBillDate` `dteBillDate` DATE NOT NULL AFTER `dblDiscountAmt`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbillpromotiondtl`   ALTER `dteBillDate` DROP DEFAULT;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbillpromotiondtl`   CHANGE COLUMN `dteBillDate` `dteBillDate` DATE NOT NULL AFTER `dblDiscountAmt`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbillcomplementrydtl` ALTER `dteBillDate` DROP DEFAULT;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbillcomplementrydtl` CHANGE COLUMN `dteBillDate` `dteBillDate` DATE NOT NULL AFTER `dblTaxAmount`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbillcomplementrydtl`    ALTER `dteBillDate` DROP DEFAULT;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbillcomplementrydtl`    CHANGE COLUMN `dteBillDate` `dteBillDate` DATE NOT NULL AFTER `dblTaxAmount`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbilldiscdtl` ALTER `dteBillDate` DROP DEFAULT;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbilldiscdtl` CHANGE COLUMN `dteBillDate` `dteBillDate` DATE NOT NULL AFTER `strDataPostFlag`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbilldiscdtl`    ALTER `dteBillDate` DROP DEFAULT;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbilldiscdtl`    CHANGE COLUMN `dteBillDate` `dteBillDate` DATE NOT NULL AFTER `strDataPostFlag`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbillmodifierdtl` ALTER `dteBillDate` DROP DEFAULT;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbillmodifierdtl` CHANGE COLUMN `dteBillDate` `dteBillDate` DATE NOT NULL AFTER `dblDiscAmt`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbillmodifierdtl`    ALTER `dteBillDate` DROP DEFAULT;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbillmodifierdtl`    CHANGE COLUMN `dteBillDate` `dteBillDate` DATE NOT NULL AFTER `dblDiscAmt`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbilldtl`"
		+ "ADD COLUMN `tmeOrderPickup` TIME NOT NULL DEFAULT '00:00:00' AFTER `dtBillDate`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblitemrtemp`"
		+ "ADD COLUMN `tmeOrderProcessing` TIME NOT NULL DEFAULT '00:00:00' AFTER `strItemProcessed`,"
		+ "ADD COLUMN `tmeOrderPickup` TIME NOT NULL DEFAULT '00:00:00' AFTER `tmeOrderProcessing`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbilldtl`"
		+ "ADD COLUMN `tmeOrderPickup` TIME NOT NULL DEFAULT '00:00:00' AFTER `dtBillDate`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblitemrtemp_bck`"
		+ "ADD COLUMN `tmeOrderProcessing` TIME NOT NULL DEFAULT '00:00:00' AFTER `strItemProcessed`,"
		+ "ADD COLUMN `tmeOrderPickup` TIME NOT NULL DEFAULT '00:00:00' AFTER `tmeOrderProcessing`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblitemrtemp` ADD COLUMN `strItemPickedUp` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `tmeOrderPickup`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblitemrtemp_bck` ADD COLUMN `strItemPickedUp` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `tmeOrderPickup`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "	ALTER TABLE `tblbillcomplementrydtl` "
		+ "	ADD COLUMN `dtBillDate` DATE NOT NULL AFTER `strType`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "	ALTER TABLE `tblbillcomplementrydtl` "
		+ "	ADD COLUMN `tmeOrderPickup` TIME NOT NULL DEFAULT '00:00:00' AFTER `dtBillDate`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "	ALTER TABLE `tblqbillcomplementrydtl` "
		+ "	ADD COLUMN `dtBillDate` DATE NOT NULL AFTER `strType`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "	ALTER TABLE `tblqbillcomplementrydtl` "
		+ "	ADD COLUMN `tmeOrderPickup` TIME NOT NULL DEFAULT '00:00:00' AFTER `dtBillDate`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblqbillhd set dteDateEdited=DATE_ADD(dteDateEdited,INTERVAL 1 DAY) "
		+ " where TIMESTAMPDIFF(second,dteBillDate,concat(date(dteDateEdited),concat(' ',time(dteSettledate)))) < 0;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblbillhd set dteDateEdited=DATE_ADD(dteDateEdited,INTERVAL 1 DAY) "
		+ " where TIMESTAMPDIFF(second,dteBillDate,concat(date(dteDateEdited),concat(' ',time(dteSettledate)))) < 0;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = " ALTER TABLE `tblsetup` "
		+ " ADD COLUMN `strEnableTableReservationForCustomer` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strShowPurRateInDirectBiller`; ";

	mapStructureUpdater.get("tblStructure").add(sql);

	sql = " ALTER TABLE `tblvoidkot` "
		+ " ADD COLUMN `strVoidBillType` VARCHAR(50) NOT NULL DEFAULT 'N' AFTER `strItemProcessed`;";

	mapStructureUpdater.get("tblStructure").add(sql);

	sql = " ALTER TABLE `tblvoidbillhd`"
		+ " ADD COLUMN `strVoidBillType` VARCHAR(50) NOT NULL AFTER `strRemark`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	//to update old data for Item Void or Bill Void
	sql = "update tblvoidbillhd d "
		+ "join (select a.strBillNo,a.dteBillDate from tblbillhd a ,tblvoidbillhd b where a.strBillNo=b.strBillNo and date(a.dteBillDate)=date(b.dteBillDate) )c "
		+ "set d.strVoidBillType='Item Void' "
		+ "where d.strVoidBillType=''"
		+ "and d.strTransType='VB' "
		+ "and d.strBillNo=c.strBillNo "
		+ "and date(d.dteBillDate)=date(c.dteBillDate);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblvoidbillhd d "
		+ "join (select a.strBillNo,a.dteBillDate from tblqbillhd a ,tblvoidbillhd b where a.strBillNo=b.strBillNo and date(a.dteBillDate)=date(b.dteBillDate) )c "
		+ "set d.strVoidBillType='Item Void' "
		+ "where d.strVoidBillType='' "
		+ "and d.strTransType='VB'  "
		+ "and d.strBillNo=c.strBillNo "
		+ "and date(d.dteBillDate)=date(c.dteBillDate);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblvoidbillhd  set strVoidBillType='Bill Void' "
		+ "where strVoidBillType='';";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE `tblpropertyimage` ( "
		+ " `strPOSCode` VARCHAR(10) NOT NULL, "
		+ " `blobReportImage` LONGBLOB NULL, "
		+ " `strClientCode` VARCHAR(20) NOT NULL, "
		+ " PRIMARY KEY (`strPOSCode`, `strClientCode`) "
		+ " ) "
		+ " COLLATE='utf8_general_ci' "
		+ " ENGINE=InnoDB "
		+ ";";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblvoidbillhd` "
		+ "	CHANGE COLUMN `strVoidBillType` `strVoidBillType` VARCHAR(50) NOT NULL DEFAULT '' AFTER `strRemark`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE `tblvoidbilldiscdtl` ( "
		+ "	`strBillNo` VARCHAR(15) NULL DEFAULT NULL, "
		+ "	`strPOSCode` VARCHAR(5) NOT NULL, "
		+ "	`dblDiscAmt` DECIMAL(18,2) NOT NULL, "
		+ "	`dblDiscPer` DECIMAL(10,2) NOT NULL, "
		+ "	`dblDiscOnAmt` DECIMAL(18,2) NOT NULL, "
		+ "	`strDiscOnType` VARCHAR(50) NOT NULL DEFAULT '', "
		+ "	`strDiscOnValue` VARCHAR(100) NOT NULL DEFAULT '', "
		+ "	`strDiscReasonCode` VARCHAR(10) NOT NULL DEFAULT '', "
		+ "	`strDiscRemarks` VARCHAR(100) NOT NULL DEFAULT '', "
		+ "	`strUserCreated` VARCHAR(50) NOT NULL, "
		+ "	`strUserEdited` VARCHAR(50) NOT NULL DEFAULT '', "
		+ "	`dteDateCreated` DATETIME NOT NULL, "
		+ "	`dteDateEdited` DATETIME NOT NULL, "
		+ "	`strClientCode` VARCHAR(10) NOT NULL, "
		+ "	`strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N', "
		+ "	`dteBillDate` DATE NOT NULL, "
		+ "	`strTransType` CHAR(10) NOT NULL DEFAULT '', "
		+ "	INDEX `strBillNo` (`strBillNo`) "
		+ ") "
		+ "COLLATE='utf8_general_ci' "
		+ "ENGINE=InnoDB "
		+ ";";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE `tblvoidbilltaxdtl` ( "
		+ "	`strBillNo` VARCHAR(15) NOT NULL DEFAULT '', "
		+ "	`strTaxCode` VARCHAR(10) NOT NULL, "
		+ "	`dblTaxableAmount` DECIMAL(18,2) NOT NULL, "
		+ "	`dblTaxAmount` DECIMAL(18,2) NOT NULL, "
		+ "	`strClientCode` VARCHAR(10) NOT NULL, "
		+ "	`strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N', "
		+ "	`dteBillDate` DATE NOT NULL, "
		+ "	`strTransType` CHAR(10) NOT NULL DEFAULT '', "
		+ "	PRIMARY KEY (`strBillNo`, `strTaxCode`, `dteBillDate`, `strClientCode`), "
		+ "	INDEX `strBillNo` (`strBillNo`) "
		+ ") "
		+ "COLLATE='utf8_general_ci' "
		+ "ENGINE=InnoDB "
		+ ";";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblvoidbilltaxdtl` "
		+ "	DROP PRIMARY KEY;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblcustomermaster` "
		+ "	CHANGE COLUMN `strEmailId` `strEmailId` VARCHAR(100) NOT NULL DEFAULT '' AFTER `dteAnniversary`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbluserhd` "
		+ " ADD COLUMN `strWaiterNo` VARCHAR(10) NOT NULL DEFAULT '' AFTER `strDebitCardString`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblcostcentermaster` "
		+ "	ADD COLUMN `strWSLocationCode` VARCHAR(15) NOT NULL DEFAULT '' AFTER `strLabelOnKOT`, "
		+ "	ADD COLUMN `strWSLocationName` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strWSLocationCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbldayendprocess` "
		+ "	CHANGE COLUMN `strWSStockAdjustmentNo` `strWSStockAdjustmentNo` VARCHAR(500) NOT NULL DEFAULT '' AFTER `dblUnusedDebitCardBalance`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbillhd` "
		+ "	ADD COLUMN `intOrderNo` INT NOT NULL DEFAULT '0' AFTER `dtBillDate`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbillhd` "
		+ "	ADD COLUMN `intOrderNo` INT NOT NULL DEFAULT '0' AFTER `dtBillDate`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblitemrtemp` "
		+ "	ADD COLUMN `strTableStatus` VARCHAR(20) NOT NULL DEFAULT 'Normal' AFTER `strItemPickedUp`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblitemrtemp_bck` "
		+ "	ADD COLUMN `strTableStatus` VARCHAR(20) NOT NULL DEFAULT 'Normal' AFTER `strItemPickedUp`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblstkinhd` "
		+ "	ALTER `strReasonCode` DROP DEFAULT;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblstkinhd` "
		+ "	CHANGE COLUMN `strReasonCode` `strReasonCode` VARCHAR(5) NOT NULL AFTER `dteStkInDate`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblstkouthd` "
		+ " ALTER `strReasonCode` DROP DEFAULT;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblstkouthd` "
		+ " CHANGE COLUMN `strReasonCode` `strReasonCode` VARCHAR(5) NOT NULL AFTER `dteStkOutDate`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblatvreport` "
		+ "	ADD COLUMN `strWaiterName` VARCHAR(50) NULL DEFAULT '' AFTER `dblTAAvg`; ";

	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblmenuitempricingdtl a "
		+ "set a.tmeTimeFrom=trim(a.tmeTimeFrom) "
		+ ",a.strAMPMFrom=TRIM(a.strAMPMFrom) "
		+ ",a.tmeTimeTo=trim(a.tmeTimeTo) "
		+ ",a.strAMPMTo=TRIM(a.strAMPMTo);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblcustomermaster` "
		+ "	ADD COLUMN `strGSTNo` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strTempLandmark`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ "	ADD COLUMN `strAutoShowPopItems` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strEnableTableReservationForCustomer`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ "	ADD COLUMN `intShowPopItemsOfDays` INT NOT NULL DEFAULT '1' AFTER `strAutoShowPopItems`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ "	ADD COLUMN `strPostSalesCostOrLoc` VARCHAR(20) NOT NULL DEFAULT 'Cost Center' AFTER `intShowPopItemsOfDays`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsettelmenthd` "
		+ "	ADD COLUMN `dblThirdPartyComission` DECIMAL(5,2) NOT NULL DEFAULT '0.00' AFTER `strCreditReceiptYN`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblitemrtemp` "
		+ "	ADD COLUMN `strDeviceMACAdd` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strTableStatus`, "
		+ "	ADD COLUMN `strDeviceId` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strDeviceMACAdd`; ";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblitemrtemp_bck` "
		+ "	ADD COLUMN `strDeviceMACAdd` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strTableStatus`, "
		+ "	ADD COLUMN `strDeviceId` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strDeviceMACAdd`; ";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblreasonmaster` ADD COLUMN `strMoveKOT` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strReprint`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsettelmenthd` "
		+ "ADD COLUMN `strComissionType` VARCHAR(5) NOT NULL DEFAULT 'Per' AFTER `dblThirdPartyComission`, "
		+ "ADD COLUMN `strComissionOn` VARCHAR(50) NOT NULL DEFAULT 'Net Amount' AFTER `strComissionType`; ";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbladvbookbillhd` "
		+ "	CHANGE COLUMN `strSettelmentMode` `strSettelmentMode` VARCHAR(50) NOT NULL AFTER `strPOSCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbillhd` "
		+ "	ADD COLUMN `strCRMRewardId` VARCHAR(50) NOT NULL DEFAULT '' AFTER `intOrderNo`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbillhd` "
		+ "	ADD COLUMN `strCRMRewardId` VARCHAR(50) NOT NULL DEFAULT '' AFTER `intOrderNo`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ "	ADD COLUMN `strEffectOfSales` VARCHAR(20) NOT NULL DEFAULT '' AFTER `strPostSalesCostOrLoc`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblitemmaster` "
		+ "	ADD COLUMN `strRecipeUOM` VARCHAR(5) NOT NULL DEFAULT '' AFTER `tmeTargetMiss`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblitemmaster` "
		+ "	ADD COLUMN `strReceivedConversion` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `strRecipeUOM`, "
		+ "	ADD COLUMN `strRecipeConversion` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `strReceivedConversion`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE `tbluommaster` ( "
		+ "	`strUomName` VARCHAR(50) NOT NULL, "
		+ "	`strUserCreated` VARCHAR(50) NOT NULL, "
		+ "	`strUserEdited` VARCHAR(50) NOT NULL, "
		+ "	`dteDateCreated` DATETIME NOT NULL, "
		+ "	`dteDateEdited` DATETIME NOT NULL, "
		+ "	`strClientCode` VARCHAR(11) NOT NULL DEFAULT '', "
		+ "	`strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N', "
		+ "	`strPosCode` VARCHAR(10) NOT NULL, "
		+ "	PRIMARY KEY (`strUomName`, `strClientCode`), "
		+ "	INDEX `intUomName` (`strUomName`) "
		+ ") "
		+ "COLLATE='utf8_general_ci' "
		+ "ENGINE=InnoDB "
		+ ";";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbillcomplementrydtl` "
		+ "	CHANGE COLUMN `dteBillDate` `dteBillDate` DATETIME NOT NULL AFTER `dblTaxAmount`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "	ALTER TABLE `tblqbillcomplementrydtl` "
		+ "	CHANGE COLUMN `dteBillDate` `dteBillDate` DATETIME NOT NULL AFTER `dblTaxAmount`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ "	ADD COLUMN `strPOSWiseItemToMMSProductLinkUpYN` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strEffectOfSales`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblimportexcel` "
		+ "	ADD COLUMN `strRawMaterial` VARCHAR(5) NOT NULL DEFAULT 'N' AFTER `strUOM`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbladvbookbillhd` "
		+ "	CHANGE COLUMN `strSettelmentMode` `strSettelmentMode` VARCHAR(50) NOT NULL AFTER `strPOSCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqadvbookbillhd` "
		+ "	CHANGE COLUMN `strSettelmentMode` `strSettelmentMode` VARCHAR(50) NOT NULL AFTER `strPOSCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblitemmaster` "
		+ "	CHANGE COLUMN `strUOM` `strUOM` VARCHAR(50) NOT NULL DEFAULT '' AFTER `strUrgentOrder`, "
		+ "	CHANGE COLUMN `strRecipeUOM` `strRecipeUOM` VARCHAR(50) NOT NULL DEFAULT '' AFTER `tmeTargetMiss`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblitemmaster` "
		+ "	CHANGE COLUMN `strReceivedConversion` `dblReceivedConversion` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `strRecipeUOM`, "
		+ "	CHANGE COLUMN `strRecipeConversion` `dblRecipeConversion` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `dblReceivedConversion`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblstkouthd` "
		+ "	ADD COLUMN `strNarration` VARCHAR(300) NOT NULL DEFAULT '' AFTER `strDataPostFlag`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblstkoutdtl` "
		+ "	ADD COLUMN `strParentCode` VARCHAR(20) NOT NULL AFTER `strDataPostFlag`, "
		+ "	ADD COLUMN `strRemark` VARCHAR(100) NOT NULL AFTER `strParentCode`, "
		+ "	ADD COLUMN `strDisplayQty` VARCHAR(100) NOT NULL AFTER `strRemark`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblimportexcel` "
		+ "	ADD COLUMN `strRecipeUOM` VARCHAR(50) NOT NULL DEFAULT '' AFTER `strRawMaterial`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblstkoutdtl` "
		+ "	CHANGE COLUMN `strParentCode` `strParentCode` VARCHAR(20) NOT NULL DEFAULT '' AFTER `strDataPostFlag`, "
		+ "	CHANGE COLUMN `strRemark` `strRemark` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strParentCode`, "
		+ "	CHANGE COLUMN `strDisplayQty` `strDisplayQty` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strRemark`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblitemmaster` "
		+ "	DROP COLUMN `strReceivedConversion`, "
		+ "	DROP COLUMN `strRecipeConversion`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tbldayendprocess  "
		+ "set dteDayEndDateTime=dteDateCreated "
		+ "where dteDayEndDateTime IS NULL ;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE IF NOT EXISTS `tblpurchaseordertaxdtl` (`strPOCode` VARCHAR(20) NOT NULL, "
		+ "	`strTaxCode` VARCHAR(10) NOT NULL, "
		+ "	`dblTaxableAmount` DECIMAL(18,4) NOT NULL, "
		+ "	`dblTaxAmount` DECIMAL(18,4) NOT NULL, "
		+ "	`strClientCode` VARCHAR(10) NOT NULL, "
		+ "	PRIMARY KEY (`strPOCode`, `strTaxCode`, `strClientCode`) "
		+ ") "
		+ "COLLATE='utf8_general_ci' "
		+ "ENGINE=InnoDB "
		+ ";";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblpurchaseordertaxdtl` "
		+ "	ADD COLUMN `strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strClientCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblstkinhd` "
		+ "	ADD COLUMN `dblTaxAmt` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `strInvoiceCode`, "
		+ "	ADD COLUMN `dblExtraAmt` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `dblTaxAmt`, "
		+ "	ADD COLUMN `dblGrandTotal` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `dblExtraAmt`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblstkouthd` "
		+ "	ADD COLUMN `dblTaxAmt` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `strNarration`, "
		+ "	ADD COLUMN `dblExtraAmt` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `dblTaxAmt`, "
		+ "	ADD COLUMN `dblGrandTotal` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `dblExtraAmt`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE IF NOT EXISTS `tblstkintaxdtl` ( "
		+ "	`strStkInCode` VARCHAR(10) NOT NULL, "
		+ "	`strTaxCode` VARCHAR(10) NOT NULL, "
		+ "	`dblTaxableAmt` DECIMAL(18,2) NOT NULL "
		+ ") "
		+ "COLLATE='utf8_general_ci' "
		+ "ENGINE=InnoDB "
		+ ";";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblstkintaxdtl` "
		+ "	CHANGE COLUMN `dblTaxableAmt` `dblTaxableAmt` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `strTaxCode`, "
		+ "	ADD COLUMN `dblTaxAmt` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `dblTaxableAmt`, "
		+ "	ADD COLUMN `strClientCode` VARCHAR(10) NOT NULL DEFAULT '0.00' AFTER `dblTaxAmt`, "
		+ "	ADD PRIMARY KEY (`strStkInCode`, `strTaxCode`, `strClientCode`);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblstkintaxdtl` "
		+ "	ADD COLUMN `strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strClientCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE IF NOT EXISTS `tblstkouttaxdtl` ( "
		+ "	`strStkOutCode` VARCHAR(10) NOT NULL, "
		+ "	`strTaxCode` VARCHAR(10) NOT NULL, "
		+ "	`dblTaxableAmt` DECIMAL(18,2) NOT NULL "
		+ ") "
		+ "COLLATE='utf8_general_ci' "
		+ "ENGINE=InnoDB "
		+ ";";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblstkouttaxdtl` "
		+ "	CHANGE COLUMN `dblTaxableAmt` `dblTaxableAmt` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `strTaxCode`, "
		+ "	ADD COLUMN `dblTaxAmt` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `dblTaxableAmt`, "
		+ "	ADD COLUMN `strClientCode` VARCHAR(10) NOT NULL DEFAULT '0.00' AFTER `dblTaxAmt`, "
		+ "	ADD PRIMARY KEY (`strStkOutCode`, `strTaxCode`, `strClientCode`);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblstkouttaxdtl` "
		+ "	ADD COLUMN `strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strClientCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblstkinhd` ADD COLUMN `strSupplierCode` VARCHAR(10) NOT NULL AFTER `dblGrandTotal`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblstkouthd` ADD COLUMN `strSupplierCode` VARCHAR(10) NOT NULL AFTER `dblGrandTotal`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "drop table if exists tbldiscountmaster;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE `tblDiscHd` ("
		+ "	`strDiscCode` VARCHAR(10) NOT NULL, "
		+ "	`strDiscName` VARCHAR(200) NOT NULL, "
		+ "	`strPOSCode` VARCHAR(15) NOT NULL DEFAULT '', "
		+ "	`strClientCode` VARCHAR(11) NOT NULL DEFAULT '', "
		+ "	`strDiscOn` VARCHAR(50) NOT NULL,	 "
		+ "	`dteFromDate` DATE NOT NULL, "
		+ "	`dteToDate` DATE NOT NULL, "
		+ "	`strUserCreated` VARCHAR(50) NOT NULL, "
		+ "	`strUserEdited` VARCHAR(50) NOT NULL, "
		+ "	`dteDateCreated` DATETIME NOT NULL, "
		+ "	`dteDateEdited` DATETIME NOT NULL, "
		+ "	`strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N', "
		+ "	PRIMARY KEY (`strDiscCode`,`strPOSCode`,`strClientCode`) "
		+ ") "
		+ "COLLATE='utf8_general_ci' "
		+ "ENGINE=InnoDB "
		+ ";";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE `tblDiscDtl` ( "
		+ "	`strDiscCode` VARCHAR(10) NOT NULL, "
		+ "   `strDiscOnCode` VARCHAR(50) NOT NULL, "
		+ "   `strDiscOnName` VARCHAR(100) NOT NULL, "
		+ "   `strDiscountType` VARCHAR(10) NOT NULL DEFAULT '', "
		+ "	`dblDiscountValue` DECIMAL(10,2) NOT NULL DEFAULT '0.00', "
		+ "	`strClientCode` VARCHAR(11) NOT NULL DEFAULT '', "
		+ "	`strUserCreated` VARCHAR(50) NOT NULL, "
		+ "	`strUserEdited` VARCHAR(50) NOT NULL, "
		+ "	`dteDateCreated` DATETIME NOT NULL, "
		+ "	`dteDateEdited` DATETIME NOT NULL, "
		+ "	`strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N', "
		+ "	PRIMARY KEY (`strDiscCode`,`strDiscOnCode`,`strClientCode`) "
		+ ") "
		+ "COLLATE='utf8_general_ci' "
		+ "ENGINE=InnoDB "
		+ ";";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "delete from tblrecipehd where length(strItemCode)=0 ";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ "	ADD COLUMN `strEnableMasterDiscount` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strPOSWiseItemToMMSProductLinkUpYN`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE if not exists `tblonlinepaymentconfighd` ("
		+ "`strPGCode` VARCHAR(20) NOT NULL,"
		+ "	`strPGName` VARCHAR(100) NOT NULL,"
		+ "`strClientCode` VARCHAR(10) NOT NULL,"
		+ "`strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N',"
		+ "`strPosCode` VARCHAR(10) NOT NULL ,"
		+ "PRIMARY KEY (`strPGCode`, `strClientCode`)"
		+ ")"
		+ "COLLATE='utf8_general_ci'"
		+ "ENGINE=InnoDB";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE if not exists `tblonlinepaymentconfigdtl` ("
		+ "`strPGCode` VARCHAR(20) NOT NULL,"
		+ "`strFieldDesc` VARCHAR(100) NOT NULL,"
		+ "`strFieldValue` VARCHAR(100) NOT NULL,"
		+ "`strClientCode` VARCHAR(10) NOT NULL,"
		+ "`strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N',"
		+ "PRIMARY KEY (`strPGCode`, `strFieldDesc`, `strClientCode`)"
		+ ")"
		+ "COLLATE='utf8_general_ci'"
		+ "ENGINE=InnoDB";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE `tblplayzonepricinghd` ( "
		+ "	`strPlayZonePricingCode` VARCHAR(20) NOT NULL, "
		+ "	`strPosCode` VARCHAR(3) NOT NULL, "
		+ "	`strItemCode` VARCHAR(10) NOT NULL, "
		+ "	`strMenuCode` VARCHAR(10) NOT NULL, "
		+ "	`strCostCenterCode` VARCHAR(10) NOT NULL, "
		+ "	`strClientCode` VARCHAR(15) NOT NULL, "
		+ "	`intTimeStamp` INT NOT NULL, "
		+ "	`dteFromDate` DATETIME NOT NULL, "
		+ "	`dteToDate` DATETIME NOT NULL, "
		+ "	`strUserCreated` VARCHAR(50) NOT NULL "
		+ ") "
		+ "COLLATE='utf8_general_ci' "
		+ "ENGINE=InnoDB "
		+ ";";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE `tblplayzonepricingdtl` ( "
		+ "	`strPlayZonePricingCode` VARCHAR(20) NOT NULL, "
		+ "	`dteFromTime` TIME NOT NULL, "
		+ "	`dteToTime` TIME NOT NULL, "
		+ "	`dblMemberPriceMonday` DECIMAL(18,2) NOT NULL, "
		+ "	`dblMemberPriceTuesday` DECIMAL(18,2) NOT NULL, "
		+ "	`dblMemberPriceWednesday` DECIMAL(18,2) NOT NULL, "
		+ "	`dblMemberPriceThursday` DECIMAL(18,2) NOT NULL, "
		+ "	`dblMemberPriceFriday` DECIMAL(18,2) NOT NULL, "
		+ "	`dblMemberPriceSaturday` DECIMAL(18,2) NOT NULL, "
		+ "	`dblMemberPriceSunday` DECIMAL(18,2) NOT NULL,"
		+ "	`dblGuestPriceMonday` DECIMAL(18,2) NOT NULL, "
		+ "	`dblGuestPriceTuesday` DECIMAL(18,2) NOT NULL, "
		+ "	`dblGuestPriceWednesday` DECIMAL(18,2) NOT NULL, "
		+ "	`dblGuestPriceThursday` DECIMAL(18,2) NOT NULL, "
		+ "	`dblGuestPriceFriday` DECIMAL(18,2) NOT NULL, "
		+ "	`dblGuestPriceSaturday` DECIMAL(18,2) NOT NULL, "
		+ "	`dblGuestPriceSunday` DECIMAL(18,2) NOT NULL, "
		+ "	`strClientCode` DECIMAL(18,2) NOT NULL "
		+ ") "
		+ "COLLATE='utf8_general_ci' "
		+ "ENGINE=InnoDB "
		+ ";";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblitemmaster` "
		+ "ADD COLUMN `strHSNNo` VARCHAR(30) NOT NULL DEFAULT '' AFTER `dblRecipeConversion`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblposmaster`	ADD COLUMN `strPlayZonePOS` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strClientCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblplayzonepricinghd` "
		+ "	ADD COLUMN `intGracePeriod` INT(4) NOT NULL AFTER `intTimeStamp`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = " ALTER TABLE `tblplayzonepricingdtl` "
		+ "	ALTER `strClientCode` DROP DEFAULT;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblplayzonepricingdtl` "
		+ "	CHANGE COLUMN `strClientCode` `strClientCode` VARCHAR(10) NOT NULL AFTER `dblGuestPriceSunday`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblplayzonepricingdtl a "
		+ "set strClientCode=(select strClientCode from tblsetup limit 1)";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblplayzonepricingdtl` "
		+ "	ADD PRIMARY KEY (`strPlayZonePricingCode`, `dteFromTime`, `dteToTime`, `strClientCode`);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblplayzonepricinghd` "
		+ "	ADD PRIMARY KEY (`strPlayZonePricingCode`, `strPosCode`, `strClientCode`);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblcustomertypemaster` "
		+ "	ADD COLUMN `strPlayZoneCustType` VARCHAR(10) NOT NULL DEFAULT 'Member' AFTER `strDataPostFlag`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblhomedelivery` "
		+ "	CHANGE COLUMN `strDPCode` `strDPCode` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strCustomerCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblimportexcel` "
		+ "	ALTER `strShortName` DROP DEFAULT;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblitemmaster set dblReceivedConversion=1.00 "
		+ "where dblReceivedConversion=0.00;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblitemmaster set dblRecipeConversion=1.00 "
		+ "where  dblRecipeConversion=0.00;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblplayzonepricinghd` "
		+ "	ADD COLUMN `strAreaCode` VARCHAR(5) NOT NULL AFTER `strUserCreated`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblplayzonepricinghd  "
		+ "set strAreaCode=(select strAreaCode from tblareamaster where strAreaName='All') "
		+ "where strAreaCode='' ";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblstkoutdtl` "
		+ "	ADD COLUMN `dblParentItemQty` DECIMAL(18,2) NOT NULL DEFAULT '0' AFTER `strDisplayQty`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblstkoutdtl` "
		+ "	ADD COLUMN `dblParentItemRate` DECIMAL(18,2) NOT NULL DEFAULT '0' AFTER `dblParentItemQty`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ "	ADD COLUMN `strEnableNFCInterface` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strEnableMasterDiscount`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblreasonmaster` "
		+ "	ADD COLUMN `strHashTagLoyalty` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strMoveKOT`, "
		+ "	ADD COLUMN `strOperational` VARCHAR(1) NOT NULL DEFAULT 'Y' AFTER `strHashTagLoyalty`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ " ADD COLUMN `strBenowIntegrationYN` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strEnableNFCInterface`, "
		+ " ADD COLUMN `strXEmail` VARCHAR(50) NOT NULL AFTER `strBenowIntegrationYN`, "
		+ " ADD COLUMN `strMerchantCode` VARCHAR(15) NOT NULL AFTER `strXEmail`, "
		+ " ADD COLUMN `strAuthenticationKey` VARCHAR(500) NOT NULL AFTER `strMerchantCode`, "
		+ " ADD COLUMN `strSalt` VARCHAR(20) NOT NULL AFTER `strAuthenticationKey`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE `tblbenowsettlementdtl` "
		+ " (`strBillNo` VARCHAR(15) NOT NULL DEFAULT '',"
		+ " `strQRString` VARCHAR(200) NOT NULL,"
		+ " `dblSettlementAmount` DECIMAL(18,2) NOT NULL,"
		+ " `strTransID` VARCHAR(20) NOT NULL,"
		+ " `strTransStatus` VARCHAR(20) NOT NULL,"
		+ " `dteTransDate` DATE NOT NULL,"
		+ " `strMerchantCode` VARCHAR(15) NOT NULL,"
		+ " PRIMARY KEY (`strBillNo`, `dteTransDate`, `strMerchantCode`),"
		+ " INDEX `strBillNo` (`strBillNo`)"
		+ " )"
		+ " COLLATE='utf8_general_ci'"
		+ " ENGINE=InnoDB;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbluserhd` "
		+ "	ADD COLUMN `strUserType` VARCHAR(10) NOT NULL DEFAULT '' AFTER `strWaiterNo`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ "	ADD COLUMN `strEnableLockTable` VARCHAR(1) NOT NULL DEFAULT 'Y' AFTER `strSalt`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblsuppliermaster  "
		+ "set strSupplierCode=CONCAT('S','0000',right(strSupplierCode,2))  "
		+ "where length(strSupplierCode)<4 and length(strSupplierCode) > 0;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblsuppliermaster  "
		+ "set strSupplierCode=CONCAT('S','000',right(strSupplierCode,3))  "
		+ "where length(strSupplierCode)<5 and length(strSupplierCode) > 0;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblstkinhd  "
		+ "set strSupplierCode=CONCAT('S','0000',right(strSupplierCode,2))  "
		+ "where length(strSupplierCode)<4 and length(strSupplierCode) > 0;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblstkinhd  "
		+ "set strSupplierCode=CONCAT('S','000',right(strSupplierCode,3))  "
		+ "where length(strSupplierCode)<5 and length(strSupplierCode) > 0;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblstkouthd  "
		+ "set strSupplierCode=CONCAT('S','0000',right(strSupplierCode,2))  "
		+ "where length(strSupplierCode)<4 and length(strSupplierCode) > 0;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblstkouthd  "
		+ "set strSupplierCode=CONCAT('S','000',right(strSupplierCode,3))  "
		+ "where length(strSupplierCode)<5 and length(strSupplierCode) > 0;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = " update tblmenuitempricingdtl "
		+ "set strCostCenterCode=TRIM(strCostCenterCode);";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ "	ADD COLUMN `strHomeDeliveryAreaForDirectBiller` VARCHAR(5) NOT NULL DEFAULT 'A001' AFTER `strEnableLockTable`, "
		+ "	ADD COLUMN `strTakeAwayAreaForDirectBiller` VARCHAR(5) NOT NULL DEFAULT 'A001' AFTER `strHomeDeliveryAreaForDirectBiller` ";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbilldiscdtl` "
		+ "	CHANGE COLUMN `strDiscOnValue` `strDiscOnValue` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strDiscOnType`; ";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblorderhd` "
		+ "	ADD COLUMN `strBillNo` VARCHAR(15) NOT NULL DEFAULT '' AFTER `dblLooseCashAmt`, "
		+ "	ADD COLUMN `strMobileNo` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strBillNo`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblorderhd` "
		+ "	ADD COLUMN `strCustomerName` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strMobileNo`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE if not exists `tblstorelastorderno` ( "
		+ "	`strPosCode` VARCHAR(5) NOT NULL, "
		+ "	`longOrderNo` BIGINT NOT NULL, "
		+ "	PRIMARY KEY (`strPosCode`) "
		+ ") "
		+ "COLLATE='utf8_general_ci' "
		+ "ENGINE=InnoDB "
		+ ";";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblcustomermaster` "
		+ "	CHANGE COLUMN `strOfficeAddress` `strOfficeAddress` VARCHAR(100) NOT NULL DEFAULT 'N' AFTER `strClientCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbillhd` "
		+ "	ALTER `dblDiscountAmt` DROP DEFAULT, "
		+ "	ALTER `dblDiscountPer` DROP DEFAULT, "
		+ "	ALTER `dblTaxAmt` DROP DEFAULT, "
		+ "	ALTER `dblSubTotal` DROP DEFAULT, "
		+ "	ALTER `dblGrandTotal` DROP DEFAULT;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbillhd` "
		+ "	CHANGE COLUMN `dblDiscountAmt` `dblDiscountAmt` DECIMAL(18,4) NOT NULL AFTER `strSettelmentMode`, "
		+ "	CHANGE COLUMN `dblDiscountPer` `dblDiscountPer` DECIMAL(10,4) NOT NULL AFTER `dblDiscountAmt`, "
		+ "	CHANGE COLUMN `dblTaxAmt` `dblTaxAmt` DECIMAL(18,4) NOT NULL AFTER `dblDiscountPer`, "
		+ "	CHANGE COLUMN `dblSubTotal` `dblSubTotal` DECIMAL(18,4) NOT NULL AFTER `dblTaxAmt`, "
		+ "	CHANGE COLUMN `dblGrandTotal` `dblGrandTotal` DECIMAL(18,4) NOT NULL AFTER `dblSubTotal`, "
		+ "	CHANGE COLUMN `dblTipAmount` `dblTipAmount` DECIMAL(18,4) NOT NULL DEFAULT '0.00' AFTER `strRemarks`, "
		+ "	CHANGE COLUMN `dblDeliveryCharges` `dblDeliveryCharges` DECIMAL(18,4) NOT NULL DEFAULT '0.00' AFTER `strCounterCode`, "
		+ "	CHANGE COLUMN `dblRoundOff` `dblRoundOff` DECIMAL(18,4) NOT NULL DEFAULT '0.00' AFTER `strJioMoneyCardType`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbillhd` "
		+ "	ALTER `dblDiscountAmt` DROP DEFAULT, "
		+ "	ALTER `dblDiscountPer` DROP DEFAULT, "
		+ "	ALTER `dblTaxAmt` DROP DEFAULT, "
		+ "	ALTER `dblSubTotal` DROP DEFAULT, "
		+ "	ALTER `dblGrandTotal` DROP DEFAULT;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbillhd` "
		+ "	CHANGE COLUMN `dblDiscountAmt` `dblDiscountAmt` DECIMAL(18,4) NOT NULL AFTER `strSettelmentMode`, "
		+ "	CHANGE COLUMN `dblDiscountPer` `dblDiscountPer` DECIMAL(10,4) NOT NULL AFTER `dblDiscountAmt`, "
		+ "	CHANGE COLUMN `dblTaxAmt` `dblTaxAmt` DECIMAL(18,4) NOT NULL AFTER `dblDiscountPer`, "
		+ "	CHANGE COLUMN `dblSubTotal` `dblSubTotal` DECIMAL(18,4) NOT NULL AFTER `dblTaxAmt`, "
		+ "	CHANGE COLUMN `dblGrandTotal` `dblGrandTotal` DECIMAL(18,4) NOT NULL AFTER `dblSubTotal`, "
		+ "	CHANGE COLUMN `dblTipAmount` `dblTipAmount` DECIMAL(18,4) NOT NULL DEFAULT '0.00' AFTER `strRemarks`, "
		+ "	CHANGE COLUMN `dblDeliveryCharges` `dblDeliveryCharges` DECIMAL(18,4) NOT NULL DEFAULT '0.00' AFTER `strCounterCode`, "
		+ "	CHANGE COLUMN `dblRoundOff` `dblRoundOff` DECIMAL(18,4) NOT NULL DEFAULT '0.00' AFTER `strJioMoneyCardType`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbilldtl` "
		+ "	ALTER `dblQuantity` DROP DEFAULT, "
		+ "	ALTER `dblAmount` DROP DEFAULT, "
		+ "	ALTER `dblTaxAmount` DROP DEFAULT;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbilldtl` "
		+ "	CHANGE COLUMN `dblRate` `dblRate` DECIMAL(18,4) NOT NULL DEFAULT '0.00' AFTER `strAdvBookingNo`, "
		+ "	CHANGE COLUMN `dblQuantity` `dblQuantity` DECIMAL(18,4) NOT NULL AFTER `dblRate`, "
		+ "	CHANGE COLUMN `dblAmount` `dblAmount` DECIMAL(18,4) NOT NULL AFTER `dblQuantity`, "
		+ "	CHANGE COLUMN `dblTaxAmount` `dblTaxAmount` DECIMAL(18,4) NOT NULL AFTER `dblAmount`, "
		+ "	CHANGE COLUMN `dblDiscountAmt` `dblDiscountAmt` DECIMAL(18,4) NOT NULL DEFAULT '0.00' AFTER `strWaiterNo`, "
		+ "	CHANGE COLUMN `dblDiscountPer` `dblDiscountPer` DECIMAL(18,4) NOT NULL DEFAULT '0.00' AFTER `dblDiscountAmt`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbilldtl` "
		+ "	ALTER `dblQuantity` DROP DEFAULT, "
		+ "	ALTER `dblAmount` DROP DEFAULT, "
		+ "	ALTER `dblTaxAmount` DROP DEFAULT;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbilldtl` "
		+ "	CHANGE COLUMN `dblRate` `dblRate` DECIMAL(18,4) NOT NULL DEFAULT '0.00' AFTER `strAdvBookingNo`, "
		+ "	CHANGE COLUMN `dblQuantity` `dblQuantity` DECIMAL(18,4) NOT NULL AFTER `dblRate`, "
		+ "	CHANGE COLUMN `dblAmount` `dblAmount` DECIMAL(18,4) NOT NULL AFTER `dblQuantity`, "
		+ "	CHANGE COLUMN `dblTaxAmount` `dblTaxAmount` DECIMAL(18,4) NOT NULL AFTER `dblAmount`, "
		+ "	CHANGE COLUMN `dblDiscountAmt` `dblDiscountAmt` DECIMAL(18,4) NOT NULL DEFAULT '0.00' AFTER `strWaiterNo`, "
		+ "	CHANGE COLUMN `dblDiscountPer` `dblDiscountPer` DECIMAL(18,4) NOT NULL DEFAULT '0.00' AFTER `dblDiscountAmt`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbilldiscdtl` "
		+ "	ALTER `dblDiscAmt` DROP DEFAULT, "
		+ "	ALTER `dblDiscPer` DROP DEFAULT, "
		+ "	ALTER `dblDiscOnAmt` DROP DEFAULT;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbilldiscdtl` "
		+ "	CHANGE COLUMN `dblDiscAmt` `dblDiscAmt` DECIMAL(18,4) NOT NULL AFTER `strPOSCode`, "
		+ "	CHANGE COLUMN `dblDiscPer` `dblDiscPer` DECIMAL(18,4) NOT NULL AFTER `dblDiscAmt`, "
		+ "	CHANGE COLUMN `dblDiscOnAmt` `dblDiscOnAmt` DECIMAL(18,4) NOT NULL AFTER `dblDiscPer`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbillmodifierdtl` "
		+ "	ALTER `dblQuantity` DROP DEFAULT, "
		+ "	ALTER `dblAmount` DROP DEFAULT, "
		+ "	ALTER `dblDiscPer` DROP DEFAULT, "
		+ "	ALTER `dblDiscAmt` DROP DEFAULT;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbillmodifierdtl` "
		+ "	CHANGE COLUMN `dblRate` `dblRate` DECIMAL(18,4) NOT NULL DEFAULT '0.00' AFTER `strModifierName`, "
		+ "	CHANGE COLUMN `dblQuantity` `dblQuantity` DECIMAL(18,4) NOT NULL AFTER `dblRate`, "
		+ "	CHANGE COLUMN `dblAmount` `dblAmount` DECIMAL(18,4) NOT NULL AFTER `dblQuantity`, "
		+ "	CHANGE COLUMN `dblDiscPer` `dblDiscPer` DECIMAL(18,4) NOT NULL AFTER `strSequenceNo`, "
		+ "	CHANGE COLUMN `dblDiscAmt` `dblDiscAmt` DECIMAL(18,4) NOT NULL AFTER `dblDiscPer`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbillmodifierdtl` "
		+ "	ALTER `dblQuantity` DROP DEFAULT, "
		+ "	ALTER `dblAmount` DROP DEFAULT, "
		+ "	ALTER `dblDiscPer` DROP DEFAULT, "
		+ "	ALTER `dblDiscAmt` DROP DEFAULT;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbillmodifierdtl` "
		+ "	CHANGE COLUMN `dblRate` `dblRate` DECIMAL(18,4) NOT NULL DEFAULT '0.00' AFTER `strModifierName`, "
		+ "	CHANGE COLUMN `dblQuantity` `dblQuantity` DECIMAL(18,4) NOT NULL AFTER `dblRate`, "
		+ "	CHANGE COLUMN `dblAmount` `dblAmount` DECIMAL(18,4) NOT NULL AFTER `dblQuantity`, "
		+ "	CHANGE COLUMN `dblDiscPer` `dblDiscPer` DECIMAL(18,4) NOT NULL AFTER `strSequenceNo`, "
		+ "	CHANGE COLUMN `dblDiscAmt` `dblDiscAmt` DECIMAL(18,4) NOT NULL AFTER `dblDiscPer`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbillpromotiondtl` "
		+ "	ALTER `dblQuantity` DROP DEFAULT;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbillpromotiondtl` "
		+ "	CHANGE COLUMN `dblQuantity` `dblQuantity` DECIMAL(18,4) NOT NULL AFTER `strPromotionCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbillsettlementdtl` "
		+ "	ALTER `dblSettlementAmt` DROP DEFAULT;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbillsettlementdtl` "
		+ "	CHANGE COLUMN `dblSettlementAmt` `dblSettlementAmt` DECIMAL(18,4) NOT NULL AFTER `strSettlementCode`, "
		+ "	CHANGE COLUMN `dblPaidAmt` `dblPaidAmt` DECIMAL(18,4) NULL DEFAULT '0.00' AFTER `dblSettlementAmt`, "
		+ "	CHANGE COLUMN `dblActualAmt` `dblActualAmt` DECIMAL(18,4) NOT NULL DEFAULT '0.00' AFTER `strCustomerCode`, "
		+ "	CHANGE COLUMN `dblRefundAmt` `dblRefundAmt` DECIMAL(18,4) NOT NULL DEFAULT '0.00' AFTER `dblActualAmt`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbillsettlementdtl` "
		+ "	ALTER `dblSettlementAmt` DROP DEFAULT;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbillsettlementdtl` "
		+ "	CHANGE COLUMN `dblSettlementAmt` `dblSettlementAmt` DECIMAL(18,4) NOT NULL AFTER `strSettlementCode`, "
		+ "	CHANGE COLUMN `dblPaidAmt` `dblPaidAmt` DECIMAL(18,4) NULL DEFAULT '0.00' AFTER `dblSettlementAmt`, "
		+ "	CHANGE COLUMN `dblActualAmt` `dblActualAmt` DECIMAL(18,4) NOT NULL DEFAULT '0.00' AFTER `strCustomerCode`, "
		+ "	CHANGE COLUMN `dblRefundAmt` `dblRefundAmt` DECIMAL(18,4) NOT NULL DEFAULT '0.00' AFTER `dblActualAmt`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbilltaxdtl` "
		+ "	ALTER `dblTaxableAmount` DROP DEFAULT, "
		+ "	ALTER `dblTaxAmount` DROP DEFAULT;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbilltaxdtl` "
		+ "	CHANGE COLUMN `dblTaxableAmount` `dblTaxableAmount` DECIMAL(18,4) NOT NULL AFTER `strTaxCode`, "
		+ "	CHANGE COLUMN `dblTaxAmount` `dblTaxAmount` DECIMAL(18,4) NOT NULL AFTER `dblTaxableAmount`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbilltaxdtl` "
		+ "	ALTER `dblTaxableAmount` DROP DEFAULT, "
		+ "	ALTER `dblTaxAmount` DROP DEFAULT;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbilltaxdtl` "
		+ "	CHANGE COLUMN `dblTaxableAmount` `dblTaxableAmount` DECIMAL(18,4) NOT NULL AFTER `strTaxCode`, "
		+ "	CHANGE COLUMN `dblTaxAmount` `dblTaxAmount` DECIMAL(18,4) NOT NULL AFTER `dblTaxableAmount`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ "	ADD COLUMN `strRoundOffBillFinalAmt` VARCHAR(5) NOT NULL DEFAULT 'Y' AFTER `strTakeAwayAreaForDirectBiller`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ "	ADD COLUMN `dblNoOfDecimalPlace` INT(2) NOT NULL DEFAULT '2' AFTER `strRoundOffBillFinalAmt`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbilldiscdtl`   DROP COLUMN `dblAmount`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblcustomermaster` "
		+ "	ADD COLUMN `strDebtorCode` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strGSTNo`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbldayendprocess`"
		+ "	ADD COLUMN `strJVNo` VARCHAR(50) NOT NULL DEFAULT '' AFTER `dblAPC`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblitemrtemp` "
		+ "	ADD COLUMN `strCRMRewardId` VARCHAR(50) NOT NULL DEFAULT '' AFTER `strDeviceId`; ";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblitemrtemp_bck` "
		+ "	ADD COLUMN `strCRMRewardId` VARCHAR(50) NOT NULL DEFAULT '' AFTER `strDeviceId`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbldayendprocess` "
		+ "	CHANGE COLUMN `dblTotalSale` `dblTotalSale` DECIMAL(18,4) NULL DEFAULT '0.00' AFTER `strDayEnd`, "
		+ "	CHANGE COLUMN `dblNoOfBill` `dblNoOfBill` DECIMAL(18,4) NULL DEFAULT '0.00' AFTER `dblTotalSale`, "
		+ "	CHANGE COLUMN `dblNoOfVoidedBill` `dblNoOfVoidedBill` DECIMAL(18,4) NULL DEFAULT '0.00' AFTER `dblNoOfBill`,"
		+ "	CHANGE COLUMN `dblNoOfModifyBill` `dblNoOfModifyBill` DECIMAL(18,4) NULL DEFAULT '0.00' AFTER `dblNoOfVoidedBill`, "
		+ "	CHANGE COLUMN `dblHDAmt` `dblHDAmt` DECIMAL(18,4) NULL DEFAULT '0.00' AFTER `dblNoOfModifyBill`, "
		+ "	CHANGE COLUMN `dblDiningAmt` `dblDiningAmt` DECIMAL(18,4) NULL DEFAULT '0.00' AFTER `dblHDAmt`, "
		+ "	CHANGE COLUMN `dblTakeAway` `dblTakeAway` DECIMAL(18,4) NULL DEFAULT '0.00' AFTER `dblDiningAmt`, "
		+ "	CHANGE COLUMN `dblFloat` `dblFloat` DECIMAL(18,4) NULL DEFAULT '0.00' AFTER `dblTakeAway`, "
		+ "	CHANGE COLUMN `dblCash` `dblCash` DECIMAL(18,4) NULL DEFAULT '0.00' AFTER `dblFloat`, "
		+ "	CHANGE COLUMN `dblAdvance` `dblAdvance` DECIMAL(18,4) NULL DEFAULT '0.00' AFTER `dblCash`, "
		+ "	CHANGE COLUMN `dblTransferIn` `dblTransferIn` DECIMAL(18,4) NULL DEFAULT '0.00' AFTER `dblAdvance`, "
		+ "	CHANGE COLUMN `dblTotalReceipt` `dblTotalReceipt` DECIMAL(18,4) NULL DEFAULT '0.00' AFTER `dblTransferIn`, "
		+ "	CHANGE COLUMN `dblPayments` `dblPayments` DECIMAL(18,4) NULL DEFAULT '0.00' AFTER `dblTotalReceipt`, "
		+ "	CHANGE COLUMN `dblWithdrawal` `dblWithdrawal` DECIMAL(18,4) NULL DEFAULT '0.00' AFTER `dblPayments`, "
		+ "	CHANGE COLUMN `dblTransferOut` `dblTransferOut` DECIMAL(18,4) NULL DEFAULT '0.00' AFTER `dblWithdrawal`, "
		+ "	CHANGE COLUMN `dblTotalPay` `dblTotalPay` DECIMAL(18,4) NULL DEFAULT '0.00' AFTER `dblTransferOut`, "
		+ "	CHANGE COLUMN `dblCashInHand` `dblCashInHand` DECIMAL(18,4) NULL DEFAULT '0.00' AFTER `dblTotalPay`, "
		+ "	CHANGE COLUMN `dblRefund` `dblRefund` DECIMAL(18,4) NULL DEFAULT '0.00' AFTER `dblCashInHand`, "
		+ "	CHANGE COLUMN `dblTotalDiscount` `dblTotalDiscount` DECIMAL(18,4) NULL DEFAULT '0.00' AFTER `dblRefund`,	 "
		+ "	CHANGE COLUMN `dblNoOfDiscountedBill` `dblNoOfDiscountedBill` DECIMAL(18,4) NULL DEFAULT '0.00' AFTER `dblTotalDiscount`, "
		+ "	CHANGE COLUMN `dblUsedDebitCardBalance` `dblUsedDebitCardBalance` DECIMAL(18,4) NOT NULL DEFAULT '0.00' AFTER `intNoOfVoidKOT`, "
		+ "	CHANGE COLUMN `dblUnusedDebitCardBalance` `dblUnusedDebitCardBalance` DECIMAL(18,4) NOT NULL DEFAULT '0.00' AFTER `dblUsedDebitCardBalance`, "
		+ "	CHANGE COLUMN `dblTipAmt` `dblTipAmt` DECIMAL(18,4) NOT NULL DEFAULT '0.00' AFTER `strWSStockAdjustmentNo`, "
		+ "	CHANGE COLUMN `dblNetSale` `dblNetSale` DECIMAL(18,4) NULL DEFAULT '0.00' AFTER `strExciseBillGeneration`, "
		+ "	CHANGE COLUMN `dblGrossSale` `dblGrossSale` DECIMAL(18,4) NULL DEFAULT '0.00' AFTER `dblNetSale`, "
		+ "	CHANGE COLUMN `dblAPC` `dblAPC` DECIMAL(18,4) NULL DEFAULT '0.00' AFTER `dblGrossSale`; "
		+ " ";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbilldtl` "
		+ "	DROP COLUMN `sequenceNo`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbillmodifierdtl` "
		+ "	DROP COLUMN `sequenceNo`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbilldiscdtl` "
		+ "	DROP COLUMN `dblAmount`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblitemcurrentstk` "
		+ "	ADD COLUMN `dblPurchaseRate` DECIMAL(18,4) NOT NULL DEFAULT '0.00' AFTER `intBalance`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbldayendprocess` "
		+ "	ADD COLUMN `strClientCode` VARCHAR(10) NOT NULL DEFAULT '' AFTER `strJVNo`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tbldayendprocess set strClientCode='" + clsGlobalVarClass.gClientCode + "' where strClientCode='' ";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ "	ADD COLUMN `strSendDBBackupOnClientMail` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `dblNoOfDecimalPlace`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbldayendprocess` "
		+ "	ADD COLUMN `dteDayEndReportsDateTime` DATETIME NULL DEFAULT NULL AFTER `strClientCode`; ";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbluserhd` "
		+ "	ADD COLUMN `intNoOfDaysReportsView` SMALLINT NOT NULL DEFAULT '0' AFTER `strUserType`; ";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbenowsettlementdtl` "
		+ "	CHANGE COLUMN `strQRString` `strQRString` VARCHAR(500) NOT NULL AFTER `strBillNo`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ "	ADD COLUMN `strPrintOrderNoOnBillYN` VARCHAR(1) NOT NULL DEFAULT 'Y' AFTER `strSendDBBackupOnClientMail`, "
		+ "	ADD COLUMN `strPrintDeviceAndUserDtlOnKOTYN` VARCHAR(1) NOT NULL DEFAULT 'Y' AFTER `strPrintOrderNoOnBillYN`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbillseries` "
		+ "	ADD COLUMN `strBillNote` VARCHAR(200) NOT NULL DEFAULT '' AFTER `strPrintInclusiveOfTaxOnBill`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = " ALTER TABLE `tblsetup` "
		+ "	ADD COLUMN `strRemoveSCTaxCode` VARCHAR(10) NOT NULL DEFAULT '' AFTER `strPrintDeviceAndUserDtlOnKOTYN`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbillhd` "
		+ "	ADD COLUMN `strNSCTax` VARCHAR(10) NOT NULL DEFAULT '' AFTER `strCRMRewardId`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbillhd` "
		+ "	ADD COLUMN `strNSCTax` VARCHAR(10) NOT NULL DEFAULT '' AFTER `strCRMRewardId`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblvoidkot` "
		+ "	ADD COLUMN `strAreaCode` VARCHAR(10) NOT NULL DEFAULT '' AFTER `strVoidBillType`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ "	ADD COLUMN `strAutoAddKOTToBill` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strRemoveSCTaxCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE IF NOT EXISTS `tblprintersetupmaster` ( "
		+ "	`strPOSCode` VARCHAR(10) NOT NULL, "
		+ "	`strAreaCode` VARCHAR(4) NOT NULL, "
		+ "	`strCostCenterCode` VARCHAR(4) NOT NULL, "
		+ "	`strPrinterType` VARCHAR(100) NOT NULL DEFAULT 'COST CENTER' , "
		+ "	`strPrimaryPrinterPort` VARCHAR(100) NOT NULL, "
		+ "	`strSecondaryPrinterPort` VARCHAR(100) NOT NULL, "
		+ "	`strPrintOnBothPrintersYN` VARCHAR(5) NOT NULL DEFAULT 'N', "
		+ "	`strUserCreated` VARCHAR(10) NOT NULL, "
		+ "	`strUserEdited` VARCHAR(10) NOT NULL, "
		+ "	`dteDateCreated` DATETIME NOT NULL, "
		+ "	`dteDateEdited` DATETIME NOT NULL, "
		+ "	`strClientCode` VARCHAR(11) NOT NULL DEFAULT '', "
		+ "	`strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N', "
		+ "	PRIMARY KEY (`strPosCode`,`strAreaCode`,`strCostCenterCode`, `strClientCode`) "
		+ ") "
		+ "COLLATE='utf8_general_ci' "
		+ "ENGINE=InnoDB "
		+ ";";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ "	ADD COLUMN `strAreaWiseCostCenterKOTPrintingYN` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strAutoAddKOTToBill`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblitemmaster` "
		+ "	CHANGE COLUMN `strItemType` `strItemType` VARCHAR(20) NOT NULL DEFAULT 'Food' AFTER `strDataPostFlag`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE table if not exists `tblregisterinoutplayzone` ( "
		+ "	`strRegisterCode` VARCHAR(10) NOT NULL, "
		+ "	`strPOSCode` VARCHAR(10) NOT NULL DEFAULT 'P01', "
		+ "	`strMemberCode` VARCHAR(50) NOT NULL, "
		+ "	`strCardNo` VARCHAR(50) NOT NULL, "
		+ "	`strMemberName` VARCHAR(100) NOT NULL, "
		+ "	`intMembers` INT(11) NOT NULL DEFAULT '1', "
		+ "	`intExtraGuests` INT(11) NOT NULL DEFAULT '0', "
		+ "	`strPhoneNo` VARCHAR(100) NOT NULL DEFAULT '', "
		+ "	`strMobileNo` VARCHAR(100) NOT NULL DEFAULT '', "
		+ "	`strEmailId` VARCHAR(100) NOT NULL DEFAULT '', "
		+ "	`dteDOB` DATE NOT NULL DEFAULT '1900-01-01', "
		+ "	`dteDOJ` DATE NOT NULL DEFAULT '1900-01-01', "
		+ "	`dteMembershipExpiry` DATE NOT NULL DEFAULT '1900-01-01', "
		+ "	`strGender` VARCHAR(7) NOT NULL DEFAULT 'M', "
		+ "	`strParents` VARCHAR(50) NOT NULL DEFAULT '', "
		+ "	`strRemarks` VARCHAR(200) NOT NULL DEFAULT '', "
		+ "	`strIn` VARCHAR(2) NOT NULL DEFAULT 'Y', "
		+ "	`dteInDateTime` DATETIME NOT NULL, "
		+ "	`strOut` VARCHAR(2) NOT NULL DEFAULT 'N', "
		+ "	`dteOutDateTime` DATETIME NOT NULL, "
		+ "	`strBillNo` VARCHAR(15) NOT NULL DEFAULT '', "
		+ "	`strUserCreated` VARCHAR(10) NOT NULL, "
		+ "	`strUserEdited` VARCHAR(10) NOT NULL, "
		+ "	`dteDateCreated` DATETIME NOT NULL, "
		+ "	`dteDateEdited` DATETIME NOT NULL, "
		+ "	`strClientCode` VARCHAR(11) NOT NULL DEFAULT '', "
		+ "	`strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N', "
		+ "	PRIMARY KEY (`strRegisterCode`) "
		+ ")"
		+ "COLLATE='utf8_general_ci' "
		+ "ENGINE=InnoDB "
		+ ";";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ "	ADD COLUMN `strWERAOnlineOrderIntegration` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strAreaWiseCostCenterKOTPrintingYN`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ "	ADD COLUMN `strWERAMerchantOutletId` VARCHAR(10) NOT NULL DEFAULT '' AFTER `strWERAOnlineOrderIntegration`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ "	ADD COLUMN `strWERAAuthenticationAPIKey` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strWERAMerchantOutletId`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbillhd` "
		+ "	CHANGE COLUMN `strRemarks` `strRemarks` VARCHAR(200) NOT NULL DEFAULT '' AFTER `strReasonCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbillhd` "
		+ "	CHANGE COLUMN `strRemarks` `strRemarks` VARCHAR(200) NOT NULL DEFAULT '' AFTER `strReasonCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "CREATE TABLE `tblonlinemenuimport` (  "
		+ "	`strItemName` VARCHAR(100) NOT NULL DEFAULT '',  "
		+ "	`strItemCode` VARCHAR(10) NOT NULL DEFAULT '',  "
		+ "	`strIsModifier` VARCHAR(5) NOT NULL DEFAULT 'N',  "
		+ "	`strModifierOnItemName` VARCHAR(100) NOT NULL DEFAULT '',  "
		+ "	`strModifierCode` VARCHAR(10) NOT NULL DEFAULT '',  "
		+ "	`strModifierGroupName` VARCHAR(100) NOT NULL DEFAULT '',  "
		+ "	`strModifierGroupCode` VARCHAR(100) NOT NULL DEFAULT '',  "
		+ "	`strGroupName` VARCHAR(50) NOT NULL DEFAULT '',  "
		+ "	`strGroupCode` VARCHAR(10) NOT NULL DEFAULT '',  "
		+ "	`strCostCenterName` VARCHAR(50) NOT NULL DEFAULT '',  "
		+ "	`strCostCenterCode` VARCHAR(10) NOT NULL DEFAULT '',  "
		+ "	`strMenuHeadName` VARCHAR(50) NOT NULL DEFAULT '',  "
		+ "	`strMenuHeadCode` VARCHAR(10) NOT NULL DEFAULT '',  "
		+ "	`strPOSCode` VARCHAR(10) NOT NULL DEFAULT '',  "
		+ "	`strSubGroupName` VARCHAR(50) NOT NULL DEFAULT '',  "
		+ "	`strSubGroupCode` VARCHAR(10) NOT NULL DEFAULT '',  "
		+ "	`strAreaName` VARCHAR(30) NOT NULL DEFAULT '',  "
		+ "	`strAreaCode` VARCHAR(10) NOT NULL DEFAULT '',  "
		+ "	`strSubMenuHeadName` VARCHAR(30) NOT NULL DEFAULT '',  "
		+ "	`strSubMenuHeadCode` VARCHAR(10) NOT NULL DEFAULT '',  "
		+ "	`strExternalCode` VARCHAR(30) NOT NULL DEFAULT '',  "
		+ "	`dblRate` DECIMAL(18,2) NOT NULL DEFAULT '0.00',  "
		+ "	`min` INT(10) NOT NULL DEFAULT '0',  "
		+ "	`max` INT(10) NOT NULL DEFAULT '0',  "
		+ "	INDEX `strItemName` (`strItemName`, `strItemCode`)  "
		+ ")  "
		+ "COLLATE='latin1_swedish_ci'  "
		+ "ENGINE=InnoDB  "
		+ ";";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ "	ADD COLUMN `strFireCommunication` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strWERAAuthenticationAPIKey`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblitemrtemp` "
		+ "	ADD COLUMN `dblFiredQty` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `strCRMRewardId`, "
		+ " ADD COLUMN `dblPrintQty` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `dblFiredQty`,"
		+ "	ADD COLUMN `strBillNote` VARCHAR(100) NOT NULL DEFAULT '' AFTER `dblPrintQty`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblitemrtemp_bck` "
		+ "	ADD COLUMN `dblFiredQty` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `strCRMRewardId`, "
		+ " ADD COLUMN `dblPrintQty` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `dblFiredQty`,"
		+ "	ADD COLUMN `strBillNote` VARCHAR(100) NOT NULL DEFAULT '' AFTER `dblPrintQty`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblbillhd` "
		+ "	ADD COLUMN `strKOTToBillNote` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strNSCTax`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblqbillhd` "
		+ "	ADD COLUMN `strKOTToBillNote` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strNSCTax`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ "	ADD COLUMN `dblUSDConverionRate` DECIMAL(10,2) NOT NULL DEFAULT '0.00' AFTER `strFireCommunication`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "	ALTER TABLE `tblbillhd` "
		+ "	ADD COLUMN `dblUSDConverionRate` DECIMAL(10,2) NOT NULL DEFAULT '0.00' AFTER `strKOTToBillNote`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "	ALTER TABLE `tblqbillhd` "
		+ "	ADD COLUMN `dblUSDConverionRate` DECIMAL(10,2) NOT NULL DEFAULT '0.00' AFTER `strKOTToBillNote`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbldebitcardtype` "
		+ "	ADD COLUMN `strSetExpiryTime` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strAuthorizeMemberCard`, "
		+ "	ADD COLUMN `intExpiryTime` INT NOT NULL DEFAULT '0' AFTER `strSetExpiryTime`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblitemmaster` "
		+ "ADD COLUMN `strOperationalYN` VARCHAR(1) NOT NULL DEFAULT 'Y' AFTER `strHSNNo`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ "ADD COLUMN `strDBBackupMailReceiver` VARCHAR(500) NOT NULL DEFAULT '' AFTER `dblUSDConverionRate`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblnonchargablekot` "
		+ "ADD COLUMN `strItemName` VARCHAR(200) NOT NULL AFTER `strPOSCode`, "
		+ "ADD COLUMN `strBillNote` VARCHAR(200) NOT NULL DEFAULT '' AFTER `strItemName` ";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblimportexcel`"
		+ "	ADD COLUMN `strHourlyPricing` VARCHAR(3) NOT NULL DEFAULT 'No' AFTER `strRecipeUOM`, "
		+ "	ADD COLUMN `tmeTimeFrom` VARCHAR(50) NOT NULL DEFAULT 'HH:MM:S' AFTER `strHourlyPricing`, "
		+ "	ADD COLUMN `tmeTimeTo` VARCHAR(50) NOT NULL DEFAULT 'HH:MM:S' AFTER `tmeTimeFrom`; ";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup`"
		+ "	ADD COLUMN `strPrintMoveTableMoveKOTYN` VARCHAR(1) NOT NULL DEFAULT 'Y' AFTER `strDBBackupMailReceiver`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ "	ADD COLUMN `strPrintQtyTotal` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strPrintMoveTableMoveKOTYN`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblmenuitempricingdtl "
		+ "set tmeTimeFrom='HH:MM:S',tmeTimeTo='HH:MM:S' "
		+ "where tmeTimeFrom='HH:MM' or tmeTimeTo='HH:MM';";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update tblmenuitempricingdtl "
		+ "set tmeTimeFrom=CONCAT(tmeTimeFrom,':','00'),tmeTimeTo=CONCAT(tmeTimeTo,':','00') "
		+ "where length(tmeTimeFrom)<7 or length(tmeTimeTo)<7;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblmenuitempricingdtl` "
		+ "	CHANGE COLUMN `strPriceMonday` `strPriceMonday` DECIMAL(18,4) NOT NULL AFTER `strPopular`, "
		+ "	CHANGE COLUMN `strPriceTuesday` `strPriceTuesday` DECIMAL(18,4) NOT NULL AFTER `strPriceMonday`, "
		+ "	CHANGE COLUMN `strPriceWednesday` `strPriceWednesday` DECIMAL(18,4) NOT NULL AFTER `strPriceTuesday`, "
		+ "	CHANGE COLUMN `strPriceThursday` `strPriceThursday` DECIMAL(18,4) NOT NULL AFTER `strPriceWednesday`, "
		+ "	CHANGE COLUMN `strPriceFriday` `strPriceFriday` DECIMAL(18,4) NOT NULL AFTER `strPriceThursday`, "
		+ "	CHANGE COLUMN `strPriceSaturday` `strPriceSaturday` DECIMAL(18,4) NOT NULL AFTER `strPriceFriday`, "
		+ "	CHANGE COLUMN `strPriceSunday` `strPriceSunday` DECIMAL(18,4) NOT NULL AFTER `strPriceSaturday`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "	ALTER TABLE `tblitemmodofier` "
		+ "	CHANGE COLUMN `dblRate` `dblRate` DECIMAL(18,4) NOT NULL AFTER `strChargable`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ "	ADD COLUMN `strShowReportsInUSD` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strPrintQtyTotal`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ "	CHANGE COLUMN `strShowReportsInUSD` `strShowReportsInCurrency` VARCHAR(20) NOT NULL DEFAULT 'BASE' AFTER `strPrintQtyTotal`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ "	ADD COLUMN `strPOSToMMSPostingCurrency` VARCHAR(20) NOT NULL DEFAULT 'BASE' AFTER `strShowReportsInCurrency`, "
		+ "	ADD COLUMN `strPOSToWebBooksPostingCurrency` VARCHAR(20) NOT NULL DEFAULT 'BASE' AFTER `strPOSToMMSPostingCurrency`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update  "
		+ "tblbillsettlementdtl a "
		+ "join tblbillhd b on a.strBillNo=b.strBillNo and date(a.dteBillDate)=date(b.dteBillDate) and a.strClientCode=b.strClientCode "
		+ "set a.strRemark=b.strRemarks "
		+ "where a.strRemark='';";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "update  "
		+ "tblqbillsettlementdtl a "
		+ "join tblqbillhd b on a.strBillNo=b.strBillNo and date(a.dteBillDate)=date(b.dteBillDate) and a.strClientCode=b.strClientCode "
		+ "set a.strRemark=b.strRemarks "
		+ "where a.strRemark='';";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblsetup` "
		+ "ADD COLUMN `strLockTableForWaiter` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strPOSToWebBooksPostingCurrency`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblreservation` "
		+ "ADD COLUMN `strCancelReservation` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strPosCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbldebitcardmaster` "
		+ "ADD COLUMN `strManualNo` VARCHAR(100) NOT NULL DEFAULT 'NA' AFTER `strRefMemberCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbldischd` "
		+ "ADD COLUMN `strDineIn` VARCHAR(1) NOT NULL DEFAULT 'Y' AFTER `strDataPostFlag`, "
		+ "ADD COLUMN `strHomeDelivery` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strDineIn`, "
		+ "ADD COLUMN `strTakeAway` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strHomeDelivery` ";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tbldebitcardmaster` "
		+ "CHANGE COLUMN `strReachrgeRemark` `strReachrgeRemark` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strCardString`;";
	mapStructureUpdater.get("tblStructure").add(sql);

	sql = "ALTER TABLE `tblreservation` " 
	    + "	ADD COLUMN `strReservationType` VARCHAR(50) NOT NULL DEFAULT '' AFTER `strCancelReservation`;";
	mapStructureUpdater.get("tblStructure").add(sql);
	
	sql = "ALTER TABLE `tblcostcentermaster`" 
	    + " ADD COLUMN `intCostCenterWiseNoOfCopies` INT(11) NOT NULL DEFAULT '0' AFTER `strWSLocationName`;";
	mapStructureUpdater.get("tblStructure").add(sql);
	
	sql = "ALTER TABLE `tblsetup`" 
	    + " ADD COLUMN `strReprintOnSettleBill` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strLockTableForWaiter`;";
	mapStructureUpdater.get("tblStructure").add(sql);
	
	sql = "update tblcostcentermaster  set intCostCenterWiseNoOfCopies=1;";
	mapStructureUpdater.get("tblStructure").add(sql);
	
	sql ="ALTER TABLE `tblcostcentermaster` " 
		+ " CHANGE COLUMN `intCostCenterWiseNoOfCopies` `intPrimaryPrinterNoOfCopies` INT(11) NOT NULL DEFAULT '1' AFTER `strWSLocationName`," 
		+ " ADD COLUMN `intSecondaryPrinterNoOfCopies` INT(11) NOT NULL DEFAULT '0' AFTER `intPrimaryPrinterNoOfCopies`; ";
	mapStructureUpdater.get("tblStructure").add(sql);
	
	sql= " ALTER TABLE `tblitemmaster` " +
	" ADD COLUMN `strItemVoiceCaptureText` VARCHAR(500) NOT NULL DEFAULT '' AFTER `strOperationalYN`;";
	mapStructureUpdater.get("tblStructure").add(sql);
	
	sql = " ALTER TABLE `tblvoidbillhd` DROP PRIMARY KEY; " ;
	mapStructureUpdater.get("tblStructure").add(sql);
	
	sql = "ALTER TABLE `tblcustomermaster` " 
	    + "	ADD COLUMN `strAccountCode` VARCHAR(20) NOT NULL DEFAULT 'NA' AFTER `strDebtorCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);
	
	sql = "ALTER TABLE `tbltaxhd` " 
	    + "	CHANGE COLUMN `strAccountCode` `strAccountCode` VARCHAR(20) NOT NULL AFTER `strDataPostFlag`;";
	mapStructureUpdater.get("tblStructure").add(sql);
	
	sql = "ALTER TABLE `tblsubgrouphd` " 
	    + "	CHANGE COLUMN `strAccountCode` `strAccountCode` VARCHAR(20) NOT NULL DEFAULT 'NA' AFTER `strIncentives`;";
	mapStructureUpdater.get("tblStructure").add(sql);
	
	sql = "ALTER TABLE `tblsettelmenthd` " 
	    + "	CHANGE COLUMN `strAccountCode` `strAccountCode` VARCHAR(20) NOT NULL AFTER `strDataPostFlag`;";
	mapStructureUpdater.get("tblStructure").add(sql);
	
	sql = "ALTER TABLE `tblsettelmenthd` " 
	    + " ADD COLUMN `strCustomerSelectionOnBillSettlement` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strComissionOn`;";
	mapStructureUpdater.get("tblStructure").add(sql);
	
	sql="CREATE TABLE `tblsubgroupmasterlinkupdtl` (" 
	    + "	`strSubGrooupCode` VARCHAR(20) NOT NULL," 
	    + "	`strWSSubGroupCode` VARCHAR(20) NOT NULL," 
	    + "	`strWSSubGroupName` VARCHAR(20) NOT NULL," 
	    + "	`strClientCode` VARCHAR(20) NOT NULL," 
	    + "	`strDataPostFlag` VARCHAR(1) NOT NULL," 
	    + "	INDEX `strSubGrooupCode_strWSSubGroupCode_strClientCode` (`strSubGrooupCode`,`strWSSubGroupCode`, `strClientCode`)" 
	    + ")" 
	    + "COLLATE='latin1_swedish_ci'" 
	    + "ENGINE=InnoDB ;";
	mapStructureUpdater.get("tblStructure").add(sql);
	
	sql="ALTER TABLE `tblsetup` " 
	   +" ADD COLUMN `strTableReservationSMS` VARCHAR(300) NOT NULL AFTER `strReprintOnSettleBill`";
	mapStructureUpdater.get("tblStructure").add(sql);
	
	sql="ALTER TABLE `tblsetup` "
	    +" ADD COLUMN `strSendTableReservationSMS` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strTableReservationSMS`";
	mapStructureUpdater.get("tblStructure").add(sql);
	
	sql="update tblsmssetup a" 
	    + " set a.strTransactionName='SettleBill'" 
	    + " where a.strTransactionName='UnsettleBill'";
	mapStructureUpdater.get("tblStructure").add(sql);
	
	sql="ALTER TABLE `tblsetup` DROP COLUMN `strShowReportsInUSD`;";
	mapStructureUpdater.get("tblStructure").add(sql);
	
	sql="ALTER TABLE `tblbillmodifierdtl` CHANGE COLUMN "
		+ "`strModifierName` `strModifierName` VARCHAR(200) NOT NULL AFTER `strModifierCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);
	
	sql="ALTER TABLE `tblmodifiermaster` " +
	" CHANGE COLUMN `strModifierName` `strModifierName` VARCHAR(200) NOT NULL AFTER `strModifierCode`;";
	mapStructureUpdater.get("tblStructure").add(sql);
	
	sql="ALTER TABLE `tblsetup` " +
	" ADD COLUMN `strMergeAllKOTSToBill` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strSendTableReservationSMS`;";
	mapStructureUpdater.get("tblStructure").add(sql);
    }

}
