package com.POSGlobal.controller;

import com.POSGlobal.view.frmOkPopUp;
import java.sql.ResultSet;

/**
 * This class is no longer used.
 * Instead of this, we are using {@link clsStructureUpdater} . 
 * Don't add your structure Updated queries to this class.
 * @author Ajim
 * @deprecated
 * 
 *  
 */
@Deprecated
public class clsStructureUpdateDeprecated 
{

    private int uc = 121;

    public int structup()
    {
	String sql = null;
	int i = 0;
	try
	{

	    sql = "CREATE TABLE IF NOT EXISTS `tblpromotionmaster` ( "
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
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblareawisedc` CHANGE COLUMN `dblBillAmount` `dblBillAmount` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `strSymbol`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblareawisedc` ADD COLUMN `dblBillAmount1` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `dblBillAmount`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblcustomermaster` ADD COLUMN `strOfficeBuildingCode` VARCHAR(10) NOT NULL DEFAULT '' AFTER `longAlternateMobileNo`, CHANGE COLUMN `strOfficeBuilding` `strOfficeBuildingName` VARCHAR(200) NOT NULL DEFAULT '' AFTER `strOfficeBuildingCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblcustomermaster` ALTER `strClientCode` DROP DEFAULT;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblcustomermaster` CHANGE COLUMN `strClientCode` `strClientCode` VARCHAR(15) NOT NULL AFTER `strDataPostFlag`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbillhd` ADD COLUMN `dblDeliveryCharges` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `strCounterCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbillhd` ADD COLUMN `strCounterCode` VARCHAR(10) NOT NULL DEFAULT 'NA' AFTER `dteSettleDate`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbillhd` ADD COLUMN `dblDeliveryCharges` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `strCounterCode`;";
	    i = ExecuteQuery(sql);

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

	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbillsettlementdtl` DROP COLUMN `intID`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbillsettlementdtl` DROP COLUMN `intID`;";
	    i = ExecuteQuery(sql);

	    sql = "DROP VIEW `vcustomeroffaddress`;";
	    i = ExecuteQuery(sql);

	    sql = "create view vcustomeroffaddress as "
		    + "SELECT * from tblcustomermaster  "
		    + "WHERE strOfficeAddress='Y' ;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblhomedelivery` "
		    + "ALTER `strCustomerCode` DROP DEFAULT, "
		    + "ALTER `strClientCode` DROP DEFAULT; ";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblhomedelivery` CHANGE COLUMN `strCustomerCode` `strCustomerCode` VARCHAR(50) NOT NULL AFTER `strBillNo`, "
		    + "	CHANGE COLUMN `dteDate` `dteDate` DATE NULL DEFAULT NULL AFTER `strDPCode`, "
		    + "	CHANGE COLUMN `strCustAddressLine1` `strCustAddressLine1` VARCHAR(200) NOT NULL DEFAULT '' AFTER `strPOSCode`, "
		    + "	CHANGE COLUMN `strCustAddressLine2` `strCustAddressLine2` VARCHAR(200) NOT NULL DEFAULT '' AFTER `strCustAddressLine1`, "
		    + "	CHANGE COLUMN `strCustAddressLine3` `strCustAddressLine3` VARCHAR(200) NOT NULL DEFAULT '' AFTER `strCustAddressLine2`, "
		    + "	CHANGE COLUMN `strCustAddressLine4` `strCustAddressLine4` VARCHAR(200) NOT NULL DEFAULT '' AFTER `strCustAddressLine3`, "
		    + "	CHANGE COLUMN `strCustCity` `strCustCity` VARCHAR(30) NOT NULL DEFAULT '' AFTER `strCustAddressLine4`, "
		    + "	CHANGE COLUMN `strClientCode` `strClientCode` VARCHAR(15) NOT NULL AFTER `strDataPostFlag`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` "
		    + "                ADD COLUMN `strEditHomeDelivery` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strPrintType`, "
		    + "                ADD COLUMN `strSlabBasedHDCharges` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strEditHomeDelivery`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbuildingmaster` "
		    + "	ALTER `strBuildingName` DROP DEFAULT, "
		    + "	ALTER `strAddress` DROP DEFAULT, "
		    + "	ALTER `strUserCreated` DROP DEFAULT, "
		    + "	ALTER `strUserEdited` DROP DEFAULT, "
		    + "	ALTER `dteDateCreated` DROP DEFAULT, "
		    + "	ALTER `dteDateEdited` DROP DEFAULT;";
	    i = ExecuteQuery(sql);

	    sql = "       ALTER TABLE `tblbuildingmaster` "
		    + "	CHANGE COLUMN `strBuildingName` `strBuildingName` VARCHAR(200) NOT NULL AFTER `strBuildingCode`, "
		    + "	CHANGE COLUMN `strAddress` `strAddress` VARCHAR(200) NOT NULL AFTER `strBuildingName`, "
		    + "	CHANGE COLUMN `strUserCreated` `strUserCreated` VARCHAR(10) NOT NULL AFTER `strAddress`, "
		    + "	CHANGE COLUMN `strUserEdited` `strUserEdited` VARCHAR(10) NOT NULL AFTER `strUserCreated`, "
		    + "	CHANGE COLUMN `dteDateCreated` `dteDateCreated` DATETIME NOT NULL AFTER `strUserEdited`, "
		    + "	CHANGE COLUMN `dteDateEdited` `dteDateEdited` DATETIME NOT NULL AFTER `dteDateCreated`, "
		    + "	CHANGE COLUMN `dblHomeDeliCharge` `dblHomeDeliCharge` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `dteDateEdited`; "
		    + "";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsubgrouphd` DROP FOREIGN KEY `tblSubGrouphd_ibfk_1`;";
	    i = ExecuteQuery(sql);

	    sql = "update tblvoidbilldtl set strItemCode='' where strItemCode is NULL;";
	    i = ExecuteQuery(sql);

	    sql = "update tblvoidbilldtl set strReasonCode='' where strReasonCode is NULL;";
	    i = ExecuteQuery(sql);

	    sql = "update tblvoidbilldtl set strReasonName='' where strReasonName is NULL;";
	    i = ExecuteQuery(sql);

	    sql = "update tblvoidbilldtl set strItemName='' where strItemName is NULL;";
	    i = ExecuteQuery(sql);

	    sql = "update tblvoidbilldtl set strBillNo='' where strBillNo is NULL;";
	    i = ExecuteQuery(sql);

	    sql = "update tblvoidbilldtl set intQuantity=0 where intQuantity is NULL;";
	    i = ExecuteQuery(sql);

	    sql = "update tblvoidbilldtl set dblAmount=0.00 where dblAmount is NULL;";
	    i = ExecuteQuery(sql);

	    sql = "update tblvoidbilldtl set dblTaxAmount=0.00 where dblTaxAmount is NULL;";
	    i = ExecuteQuery(sql);

	    sql = "update tblvoidbilldtl set strTransType='' where strTransType is NULL;";
	    i = ExecuteQuery(sql);

	    sql = "update tblvoidbilldtl set strSettlementCode='' where strSettlementCode is NULL;";
	    i = ExecuteQuery(sql);

	    sql = "update tblvoidbilldtl set dblSettlementAmt=0.00 where dblSettlementAmt is NULL;";
	    i = ExecuteQuery(sql);

	    sql = "update tblvoidbilldtl set dblPaidAmt=0.00 where dblPaidAmt is NULL;";
	    i = ExecuteQuery(sql);

	    sql = "update tblvoidbilldtl set strTableNo='' where strTableNo is NULL;";
	    i = ExecuteQuery(sql);

	    sql = "update tblvoidbilldtl set strWaiterNo='' where strWaiterNo is NULL;";
	    i = ExecuteQuery(sql);

	    sql = "update tblvoidbilldtl set strUserCreated='' where strUserCreated is NULL;";
	    i = ExecuteQuery(sql);

	    sql = "update tblvoidbilldtl set strKOTNo='' where strKOTNo is NULL;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblvoidbilldtl` "
		    + "	ALTER `strItemCode` DROP DEFAULT, "
		    + "	ALTER `strItemName` DROP DEFAULT, "
		    + "	ALTER `strBillNo` DROP DEFAULT, "
		    + "	ALTER `dteBillDate` DROP DEFAULT, "
		    + "	ALTER `strTransType` DROP DEFAULT, "
		    + "	ALTER `dteModifyVoidBill` DROP DEFAULT, "
		    + "	ALTER `strSettlementCode` DROP DEFAULT, "
		    + "	ALTER `strUserCreated` DROP DEFAULT;";
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

	    sql = "update tblvoidbillhd set strReasonCode='' where strReasonCode is NULL;";
	    i = ExecuteQuery(sql);

	    sql = "update tblvoidbillhd set strReasonCode='' where strReasonCode is NULL;";
	    i = ExecuteQuery(sql);

	    sql = "update tblvoidbillhd set strTransType='' where strTransType is NULL;";
	    i = ExecuteQuery(sql);

	    sql = "update tblvoidbillhd set intShiftCode=0 where intShiftCode is NULL;";
	    i = ExecuteQuery(sql);

	    sql = "update tblvoidbillhd set strUserCreated='' where strUserCreated is NULL;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblvoidbillhd` "
		    + "	ALTER `dteBillDate` DROP DEFAULT, "
		    + "	ALTER `dteModifyVoidBill` DROP DEFAULT, "
		    + "	ALTER `strUserCreated` DROP DEFAULT;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblvoidbillhd` "
		    + "	CHANGE COLUMN `strReasonCode` `strReasonCode` VARCHAR(10) NOT NULL DEFAULT '' AFTER `strPosCode`, "
		    + "	CHANGE COLUMN `strReasonName` `strReasonName` VARCHAR(50) NOT NULL DEFAULT '' AFTER `strReasonCode`, "
		    + "	CHANGE COLUMN `dteBillDate` `dteBillDate` DATETIME NOT NULL AFTER `dblModifiedAmount`, "
		    + "	CHANGE COLUMN `strTransType` `strTransType` CHAR(10) NOT NULL DEFAULT '' AFTER `dteBillDate`, "
		    + "	CHANGE COLUMN `dteModifyVoidBill` `dteModifyVoidBill` DATETIME NOT NULL AFTER `strTransType`, "
		    + "	CHANGE COLUMN `intShiftCode` `intShiftCode` INT(11) NOT NULL DEFAULT '0' AFTER `strWaiterNo`, "
		    + "	CHANGE COLUMN `strUserCreated` `strUserCreated` VARCHAR(50) NOT NULL AFTER `intShiftCode`;";
	    i = ExecuteQuery(sql);

	    sql = "update tblvoidkot set strType='' where strType is NULL;";
	    i = ExecuteQuery(sql);

	    sql = "update tblvoidkot set strReasonCode='' where strReasonCode is NULL;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblvoidkot` "
		    + "	CHANGE COLUMN `strType` `strType` VARCHAR(50) NOT NULL DEFAULT '' AFTER `intPaxNo`, "
		    + "	CHANGE COLUMN `strReasonCode` `strReasonCode` VARCHAR(50) NOT NULL DEFAULT '' AFTER `strType`;";
	    i = ExecuteQuery(sql);

	    sql = "update tblvoidbillsettlementdtl set dblPaidAmt=0.00 where dblPaidAmt is NULL;";
	    i = ExecuteQuery(sql);

	    sql = "update tblvoidbillsettlementdtl set strExpiryDate='' where strExpiryDate is NULL;";
	    i = ExecuteQuery(sql);

	    sql = "update tblvoidbillsettlementdtl set strCustomerCode='' where strCustomerCode is NULL;";
	    i = ExecuteQuery(sql);

	    sql = "update tblvoidbillsettlementdtl set dblActualAmt=0.00 where dblActualAmt is NULL;";
	    i = ExecuteQuery(sql);

	    sql = "update tblvoidbillsettlementdtl set dblRefundAmt=0.00 where dblRefundAmt is NULL;";
	    i = ExecuteQuery(sql);

	    sql = "update tblvoidbillsettlementdtl set strGiftVoucherCode='' where strGiftVoucherCode is NULL;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblvoidbillsettlementdtl` "
		    + "	CHANGE COLUMN `dblPaidAmt` `dblPaidAmt` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `dblSettlementAmt`, "
		    + "	CHANGE COLUMN `strExpiryDate` `strExpiryDate` VARCHAR(7) NOT NULL DEFAULT '' AFTER `dblPaidAmt`, "
		    + "	CHANGE COLUMN `strCustomerCode` `strCustomerCode` VARCHAR(10) NOT NULL DEFAULT '' AFTER `strClientCode`, "
		    + "	CHANGE COLUMN `dblActualAmt` `dblActualAmt` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `strCustomerCode`, "
		    + "	CHANGE COLUMN `dblRefundAmt` `dblRefundAmt` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `dblActualAmt`, "
		    + "	CHANGE COLUMN `strGiftVoucherCode` `strGiftVoucherCode` VARCHAR(50) NOT NULL DEFAULT '' AFTER `dblRefundAmt`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblcustomermaster` "
		    + "	ADD COLUMN `strOfficeBuildingCode` VARCHAR(15) NOT NULL DEFAULT '' AFTER `longAlternateMobileNo`, "
		    + "	CHANGE COLUMN `strOfficeBuilding` `strOfficeBuildingName` VARCHAR(200) NOT NULL DEFAULT '' AFTER `strOfficeBuildingCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblhomedelivery` "
		    + "	CHANGE COLUMN `strCustAddressLine1` `strCustAddressLine1` VARCHAR(200) NOT NULL DEFAULT '' AFTER `strPOSCode`, "
		    + "	CHANGE COLUMN `strCustAddressLine2` `strCustAddressLine2` VARCHAR(200) NOT NULL DEFAULT '' AFTER `strCustAddressLine1`, "
		    + "	CHANGE COLUMN `strCustAddressLine3` `strCustAddressLine3` VARCHAR(200) NOT NULL DEFAULT '' AFTER `strCustAddressLine2`, "
		    + "	CHANGE COLUMN `strCustAddressLine4` `strCustAddressLine4` VARCHAR(200) NOT NULL DEFAULT '' AFTER `strCustAddressLine3`, "
		    + "	CHANGE COLUMN `strCustCity` `strCustCity` VARCHAR(30) NOT NULL DEFAULT '' AFTER `strCustAddressLine4`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblitemmaster` "
		    + "	CHANGE COLUMN `strItemImage` `strItemImage` VARCHAR(50) NOT NULL DEFAULT '' AFTER `strSubGroupCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblareawisedc` "
		    + "	CHANGE COLUMN `strSymbol` `strSymbol` VARCHAR(3) NOT NULL DEFAULT '' AFTER `dblKilometers`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblvoidbilldtl` CHANGE COLUMN `strSettlementCode` `strSettlementCode` VARCHAR(10) NOT NULL DEFAULT '' AFTER `dteModifyVoidBill`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblcustomermaster` ADD COLUMN `strGender` VARCHAR(5) NOT NULL DEFAULT 'Male' AFTER `dteDOB`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblcustomermaster` ADD COLUMN `dteAnniversary` VARCHAR(50) NOT NULL DEFAULT '' AFTER `strGender`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblimportexcel` "
		    + "	ADD COLUMN `strCounterName` VARCHAR(50) NOT NULL DEFAULT '' AFTER `dblPriceSaturday`, "
		    + "	ADD COLUMN `strCounterCode` VARCHAR(10) NOT NULL DEFAULT '' AFTER `strCounterName`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblloyaltypoints`"
		    + " ADD COLUMN `dblLoyaltyPoints1` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `dblLoyaltyPoints`;";
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblpointsonbill` "
		    + "	ADD COLUMN `dteBillDate` DATETIME NOT NULL AFTER `strBillNo`;";
	    i = ExecuteQuery(sql);

	    sql = "select count(strAreaCode) from tblareamaster where strAreaName='All'";
	    ResultSet rsArea = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rsArea.next())
	    {
		if (rsArea.getInt(1) == 0)
		{
		    funCreateAreaForAll();
		}
	    }
	    rsArea.close();

	    sql = "select strAreaCode from tblareamaster where strAreaName='All'";
	    rsArea = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rsArea.next())
	    {
		String areaCode = rsArea.getString(1);
		sql = "update tblmenuitempricingdtl set strAreaCode='" + areaCode + "' where strAreaCode=''";
		clsGlobalVarClass.dbMysql.execute(sql);
	    }
	    rsArea.close();

	    sql = "ALTER TABLE `tblpromotionmaster` "
		    + "	DROP COLUMN `strBuyPropmoItemCode`, "
		    + "	DROP COLUMN `strBuyOperator`, "
		    + "	DROP COLUMN `dblBuyQty`, "
		    + "	DROP COLUMN `dblBuyDiscount`, "
		    + "	DROP COLUMN `strGetPromoCode`, "
		    + "	DROP COLUMN `strGetOperator`, "
		    + "	DROP COLUMN `dblGetQty`, "
		    + "	DROP COLUMN `dblGetDiscount`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblpromotionmaster` ADD COLUMN `strPromoItemCode` VARCHAR(10) NOT NULL AFTER `strPromoCode`;";
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

	    sql = "delete from tblhomedelivery where strbillno is null;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` ADD COLUMN `strSkipWaiterAndPax` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strSlabBasedHDCharges`;";
	    i = ExecuteQuery(sql);

	    sql = "update tbladvbookbillhd set strUserEdited=strUserCreated where strUserEdited is NULL;";
	    i = ExecuteQuery(sql);

	    sql = "update tbladvbookbillhd set dteDateEdited=dteDateCreated where dteDateEdited is NULL;";
	    i = ExecuteQuery(sql);

	    sql = "update tbladvbookbillhd set intShiftCode=0 where intShiftCode is NULL;";
	    i = ExecuteQuery(sql);

	    sql = "update tbladvancereceiptdtl set dteCheque='0000-00-00 00:00:00';";
	    i = ExecuteQuery(sql);

	    sql = "update tbladvancereceipthd set strUserEdited=strUserCreated where strUserEdited is NULL;";
	    i = ExecuteQuery(sql);

	    sql = "update tbladvancereceipthd set dtDateEdited=dtDateCreated where dtDateEdited is NULL;";
	    i = ExecuteQuery(sql);

	    sql = "update tbladvancereceipthd set intShiftNo=0 where intShiftNo is NULL;";
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbladvbooktaxdtl` "
		    + "	ALTER `strAdvBookingNo` DROP DEFAULT, "
		    + "	ALTER `strTaxCode` DROP DEFAULT, "
		    + "	ALTER `dblTaxableAmount` DROP DEFAULT, "
		    + "	ALTER `dblTaxAmount` DROP DEFAULT, "
		    + "	ALTER `strClientCode` DROP DEFAULT;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbladvbooktaxdtl` "
		    + "	CHANGE COLUMN `strAdvBookingNo` `strAdvBookingNo` VARCHAR(15) NOT NULL FIRST, "
		    + "	CHANGE COLUMN `strTaxCode` `strTaxCode` VARCHAR(15) NOT NULL AFTER `strAdvBookingNo`, "
		    + "	CHANGE COLUMN `dblTaxableAmount` `dblTaxableAmount` DECIMAL(18,2) NOT NULL AFTER `strTaxCode`, "
		    + "	CHANGE COLUMN `dblTaxAmount` `dblTaxAmount` DECIMAL(18,2) NOT NULL AFTER `dblTaxableAmount`, "
		    + "	CHANGE COLUMN `strClientCode` `strClientCode` VARCHAR(15) NOT NULL AFTER `dblTaxAmount`, "
		    + "	ADD COLUMN `strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strClientCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbladvbooktaxtemp` "
		    + "	ALTER `strTaxCode` DROP DEFAULT, "
		    + "	ALTER `strTaxName` DROP DEFAULT, "
		    + "	ALTER `dblTaxableAmt` DROP DEFAULT, "
		    + "	ALTER `dblTaxAmt` DROP DEFAULT, "
		    + "	ALTER `strTaxCal` DROP DEFAULT, "
		    + "	ALTER `strItemName` DROP DEFAULT;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbladvbooktaxtemp` "
		    + "	CHANGE COLUMN `strTaxCode` `strTaxCode` VARCHAR(50) NOT NULL FIRST, "
		    + "	CHANGE COLUMN `strTaxName` `strTaxName` VARCHAR(50) NOT NULL AFTER `strTaxCode`, "
		    + "	CHANGE COLUMN `dblTaxableAmt` `dblTaxableAmt` DECIMAL(18,2) NOT NULL AFTER `strTaxName`, "
		    + "	CHANGE COLUMN `dblTaxAmt` `dblTaxAmt` DECIMAL(18,2) NOT NULL AFTER `dblTaxableAmt`, "
		    + "	CHANGE COLUMN `strTaxCal` `strTaxCal` VARCHAR(50) NOT NULL AFTER `dblTaxAmt`, "
		    + "	CHANGE COLUMN `strItemName` `strItemName` VARCHAR(50) NOT NULL AFTER `strTaxCal`;";
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbladvbookbillhd` "
		    + "	ADD COLUMN `strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `intShiftCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbladvbookbilldtl` "
		    + "	ALTER `strItemCode` DROP DEFAULT, "
		    + "	ALTER `strItemName` DROP DEFAULT, "
		    + "	ALTER `strAdvBookingNo` DROP DEFAULT, "
		    + "	ALTER `strClientCode` DROP DEFAULT, "
		    + "	ALTER `strCustomerCode` DROP DEFAULT;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbladvbookbilldtl` "
		    + "	CHANGE COLUMN `strItemCode` `strItemCode` VARCHAR(10) NOT NULL FIRST, "
		    + "	CHANGE COLUMN `strItemName` `strItemName` VARCHAR(100) NOT NULL AFTER `strItemCode`, "
		    + "	CHANGE COLUMN `strAdvBookingNo` `strAdvBookingNo` VARCHAR(15) NOT NULL AFTER `strItemName`, "
		    + "	CHANGE COLUMN `strClientCode` `strClientCode` VARCHAR(10) NOT NULL AFTER `dteOrderFor`, "
		    + "	CHANGE COLUMN `strCustomerCode` `strCustomerCode` VARCHAR(10) NOT NULL AFTER `strClientCode`, "
		    + "	ADD COLUMN `strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strCustomerCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbladvbookbillhd` "
		    + "	ALTER `strAdvBookingNo` DROP DEFAULT, "
		    + "	ALTER `strPOSCode` DROP DEFAULT, "
		    + "	ALTER `strSettelmentMode` DROP DEFAULT, "
		    + "	ALTER `strUserCreated` DROP DEFAULT, "
		    + "	ALTER `strUserEdited` DROP DEFAULT, "
		    + "	ALTER `strClientCode` DROP DEFAULT, "
		    + "	ALTER `strCustomerCode` DROP DEFAULT;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbladvbookbillhd` "
		    + "	CHANGE COLUMN `strAdvBookingNo` `strAdvBookingNo` VARCHAR(15) NOT NULL FIRST, "
		    + "	CHANGE COLUMN `strPOSCode` `strPOSCode` VARCHAR(10) NOT NULL AFTER `dteOrderFor`, "
		    + "	CHANGE COLUMN `strSettelmentMode` `strSettelmentMode` VARCHAR(10) NOT NULL AFTER `strPOSCode`, "
		    + "	CHANGE COLUMN `strUserCreated` `strUserCreated` VARCHAR(10) NOT NULL AFTER `dblGrandTotal`, "
		    + "	CHANGE COLUMN `strUserEdited` `strUserEdited` VARCHAR(10) NOT NULL AFTER `strUserCreated`, "
		    + "	CHANGE COLUMN `strClientCode` `strClientCode` VARCHAR(10) NOT NULL AFTER `dteDateEdited`, "
		    + "	CHANGE COLUMN `strCustomerCode` `strCustomerCode` VARCHAR(10) NOT NULL AFTER `strClientCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbladvancereceiptdtl` "
		    + "	ALTER `strSettlementCode` DROP DEFAULT, "
		    + "	ALTER `strCardNo` DROP DEFAULT, "
		    + "	ALTER `strExpirydate` DROP DEFAULT, "
		    + "	ALTER `strChequeNo` DROP DEFAULT, "
		    + "	ALTER `dteCheque` DROP DEFAULT, "
		    + "	ALTER `strBankName` DROP DEFAULT, "
		    + "	ALTER `strRemark` DROP DEFAULT;";
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbladvancereceipthd` "
		    + "	ADD COLUMN `strClientCode` VARCHAR(10) NOT NULL AFTER `dtDateEdited`, "
		    + "	ADD COLUMN `strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strClientCode`;";
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbladvbookbillhd` "
		    + "	ADD COLUMN `strMessage` VARCHAR(150) NOT NULL DEFAULT '' AFTER `strCustomerCode`, "
		    + "	ADD COLUMN `strShape` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strMessage`, "
		    + "	ADD COLUMN `strNote` VARCHAR(300) NOT NULL DEFAULT '' AFTER `strShape`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` ADD COLUMN `strDirectKOTPrintMakeKOT` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strSkipWaiterAndPax`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblitemrtemp` "
		    + "	ADD COLUMN `strDelBoyCode` VARCHAR(10) NOT NULL DEFAULT '' AFTER `tdhComboItemYN`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblstkinhd` "
		    + "	ADD COLUMN `strClientCode` VARCHAR(10) NOT NULL AFTER `dteDateEdited`, "
		    + "	ADD COLUMN `strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strClientCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblstkouthd` "
		    + "	ADD COLUMN `strClientCode` VARCHAR(10) NOT NULL AFTER `dteDateEdited`, "
		    + "	ADD COLUMN `strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strClientCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblstkindtl` "
		    + "	ADD COLUMN `strClientCode` VARCHAR(10) NOT NULL AFTER `dblAmount`, "
		    + "	ADD COLUMN `strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strClientCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblstkoutdtl` "
		    + " ADD COLUMN `strClientCode` VARCHAR(10) NOT NULL AFTER  "
		    + "`dblAmount`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbladvancereceipthd` "
		    + "CHANGE COLUMN `strSettelmentMode` `strSettelmentMode` VARCHAR(15) NOT NULL AFTER `strPOSCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblmodifiermaster` ADD COLUMN `strModifierGroupCode` VARCHAR(12) NOT NULL DEFAULT 'NA' AFTER `strDataPostFlag`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbladvbookitemtemp` "
		    + "CHANGE COLUMN `strItemName` `strItemName` VARCHAR(50) NULL DEFAULT NULL AFTER `strItemCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbladvancereceiptdtl` "
		    + "ADD COLUMN `dteInstallment` DATETIME NOT NULL AFTER `strDataPostFlag`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbladvbookbillhd` "
		    + "ADD COLUMN `strDeliveryTime` VARCHAR(10) NOT NULL AFTER `strDataPostFlag`;";
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` ADD COLUMN `strSkipPax` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strDirectKOTPrintMakeKOT`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblcustomermaster` ADD COLUMN `dteDOB` VARCHAR(50) NOT NULL DEFAULT '' AFTER `strCustomerType`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbltablemaster` ADD COLUMN `intSequence` INT NOT NULL DEFAULT '0' AFTER `strDataPostFlag`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblmodifiergrouphd` CHANGE COLUMN `strApplyItemLimit` `strApplyMaxItemLimit` VARCHAR(1) NOT NULL AFTER `strModifierGroupShortName`;	";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblmodifiergrouphd` CHANGE COLUMN `intItemLimit` `intItemMaxLimit` DOUBLE NOT NULL AFTER `strApplyMaxItemLimit`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblmodifiergrouphd` ADD COLUMN `strApplyMinItemLimit` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strDataPostFlag`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblmodifiergrouphd` ADD COLUMN `intItemMinLimit` DOUBLE NOT NULL AFTER `strApplyMinItemLimit`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblmodifiergrouphd` ADD COLUMN `intSequenceNo` INT NOT NULL DEFAULT '0' AFTER `intItemMinLimit`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbladvbookbillhd`	ADD COLUMN `strWaiterNo` VARCHAR(10) NOT NULL DEFAULT '' AFTER `strDeliveryTime`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblcustomermaster` ADD COLUMN `strEmailId` VARCHAR(50) NOT NULL DEFAULT '' AFTER `dteAnniversary`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblimportexcel` "
		    + "	ADD COLUMN `strCustName` VARCHAR(50) NOT NULL DEFAULT '' AFTER `strCounterCode`, "
		    + "	ADD COLUMN `strCustCode` VARCHAR(10) NOT NULL DEFAULT '' AFTER `strCustName`, "
		    + "	ADD COLUMN `strBuildName` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strCustCode`, "
		    + "	ADD COLUMN `strBuildCode` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strBuildName`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblimportexcel` "
		    + "	ADD COLUMN `strBuildingArea` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strBuildCode`, "
		    + "	ADD COLUMN `strTelephoneNo` VARCHAR(40) NOT NULL DEFAULT '' AFTER `strBuildingArea`, "
		    + "	ADD COLUMN `strEmail` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strTelephoneNo`, "
		    + "	ADD COLUMN `strDOB` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strEmail`; ";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbuildingmaster` "
		    + "	ADD COLUMN `strClientCode` VARCHAR(10) NOT NULL AFTER `dblHomeDeliCharge`, "
		    + "	ADD COLUMN `strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strClientCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbuildingmaster` "
		    + "	CHANGE COLUMN `strAddress` `strAddress` VARCHAR(200) NOT NULL DEFAULT '' AFTER `strBuildingName`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbladvbookbillhd` "
		    + " ADD COLUMN `strHomeDelivery` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strWaiterNo`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbladvbookbillhd` "
		    + " ADD COLUMN `dblHomeDelCharges` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `strHomeDelivery`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` "
		    + "	ADD COLUMN `strLoyaltyOption` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strSkipPax`, "
		    + "	ADD COLUMN `strGetWebserviceURL` VARCHAR(300) NOT NULL DEFAULT '' AFTER `strLoyaltyOption`, "
		    + "	ADD COLUMN `strPostWebserviceURL` VARCHAR(300) NOT NULL DEFAULT '' AFTER `strGetWebserviceURL`, "
		    + "	ADD COLUMN `strOutletUID` VARCHAR(50) NOT NULL DEFAULT '' AFTER `strPostWebserviceURL`, "
		    + "	ADD COLUMN `strPOSID` VARCHAR(15) NOT NULL DEFAULT '' AFTER `strOutletUID`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` "
		    + "	CHANGE COLUMN `strLoyaltyOption` `strSQYCRNIntf` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strSkipPax`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` "
		    + "	CHANGE COLUMN `strSQYCRNIntf` `strCRMInterface` VARCHAR(50) NOT NULL DEFAULT '' AFTER `strSkipPax`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup`	ADD COLUMN `strStockInOption` VARCHAR(15) NOT NULL DEFAULT '' AFTER `strPOSID`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblstkoutdtl` ADD COLUMN `strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strClientCode`;";
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblcrmpoints` "
		    + "	ADD COLUMN `longCustMobileNo` VARCHAR(15) NOT NULL AFTER `dblRedeemedAmt`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup`	ADD COLUMN `longCustSeries` BIGINT NOT NULL DEFAULT '1' AFTER `strStockInOption`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup`	ALTER `longCustSeries` DROP DEFAULT;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup`	CHANGE COLUMN `longCustSeries` `longCustSeries` BIGINT(20) NOT NULL AFTER `strStockInOption`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblareawisedc` "
		    + "	ADD COLUMN `strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strClientCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbuildingmaster` "
		    + "	ALTER `strClientCode` DROP DEFAULT;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbuildingmaster` "
		    + "	CHANGE COLUMN `strClientCode` `strClientCode` VARCHAR(15) NOT NULL AFTER `dblHomeDeliCharge`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup`	DROP COLUMN `strHOPOS`;";
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

	    sql = "CREATE TABLE `tblrecipedtl` ( "
		    + "	`strRecipeCode` VARCHAR(15) NOT NULL, "
		    + "	`strChildItemCode` VARCHAR(10) NOT NULL, "
		    + "	`dblQuantity` DECIMAL(18,2) NOT NULL DEFAULT '0.00', "
		    + "	`strClientCode` VARCHAR(15) NOT NULL, "
		    + "	`strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' "
		    + ") "
		    + "COLLATE='latin1_swedish_ci' "
		    + "ENGINE=InnoDB;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblloyaltypoints`	ADD COLUMN `dteFromDate` "
		    + "DATETIME NOT NULL AFTER `strDataPostFlag`, "
		    + "ADD COLUMN `dteToDate` DATETIME NOT NULL AFTER `dteFromDate`;";
	    i = ExecuteQuery(sql);

	    sql = "CREATE TABLE `tblloyaltypointposdtl` ( "
		    + "	`strLoyaltyCode` VARCHAR(15) NOT NULL, "
		    + "	`strPOSCode` VARCHAR(15) NOT NULL, "
		    + "	`strClientCode` VARCHAR(15) NOT NULL, "
		    + "	`strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' "
		    + ") "
		    + "COLLATE='latin1_swedish_ci' "
		    + "ENGINE=InnoDB;";
	    i = ExecuteQuery(sql);

	    sql = "CREATE TABLE `tblloyaltypointmenuhddtl` (`strLoyaltyCode` VARCHAR(15) NOT NULL, "
		    + "	`strMenuCode` VARCHAR(15) NOT NULL, "
		    + "	`strClientCode` VARCHAR(15) NOT NULL, "
		    + "	`strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N') "
		    + "	COLLATE='latin1_swedish_ci' ENGINE=InnoDB;";
	    i = ExecuteQuery(sql);

	    sql = "CREATE TABLE `tblloyaltypointsubgroupdtl` (	`strLoyaltyCode` VARCHAR(15) NOT NULL, "
		    + "	`strCode` VARCHAR(15) NOT NULL, "
		    + "	`strClientCode` VARCHAR(15) NOT NULL, "
		    + "	`strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N') "
		    + "	COLLATE='latin1_swedish_ci' ENGINE=InnoDB;";
	    i = ExecuteQuery(sql);

	    sql = "CREATE TABLE `tblloyaltypointcustomerdtl` (	`strLoyaltyCode` VARCHAR(15) NOT NULL, "
		    + "	`strCustomerTypeCode` VARCHAR(15) NOT NULL, "
		    + "	`strClientCode` VARCHAR(15) NOT NULL, "
		    + "	`strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N') "
		    + "	COLLATE='latin1_swedish_ci' ENGINE=InnoDB;";
	    i = ExecuteQuery(sql);

//            sql = "ALTER TABLE `tbltablemaster` ENGINE=MyISAM;";
//            i = ExecuteQuery(sql);
//
//            sql = "ALTER TABLE `tblitemrtemp` ENGINE=MyISAM;";
//            i = ExecuteQuery(sql);
	    sql = "ALTER TABLE `tblsetup` "
		    + "CHANGE COLUMN `dteHOServerDate` `dteHOServerDate` VARCHAR(25) "
		    + "NOT NULL DEFAULT '2013-01-01 16:49:15' AFTER `strDataSendFrequency`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblcrmpoints` "
		    + "	ADD COLUMN `dblValue` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `strDataPostFlag`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbldeliverypersonmaster` ALTER `strDPCode` DROP DEFAULT;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbldeliverypersonmaster` "
		    + "	CHANGE COLUMN `strDPCode` `strDPCode` VARCHAR(10) NOT NULL FIRST;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbldeliverypersonmaster` "
		    + "	ALTER `strDPName` DROP DEFAULT;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbldeliverypersonmaster` "
		    + "	CHANGE COLUMN `strDPName` `strDPName` VARCHAR(100) NOT NULL AFTER `strDPCode`;";
	    i = ExecuteQuery(sql);

	    sql = "update tbldeliverypersonmaster set strAddressLine1='' "
		    + "where strAddressLine1 is NULL;";
	    i = ExecuteQuery(sql);

	    sql = "update tbldeliverypersonmaster set strAddressLine2='' "
		    + "where strAddressLine2 is NULL;";
	    i = ExecuteQuery(sql);

	    sql = "update tbldeliverypersonmaster set strAddressLine3='' "
		    + "where strAddressLine3 is NULL;";
	    i = ExecuteQuery(sql);

	    sql = "update tbldeliverypersonmaster set strCity='' "
		    + "where strCity is NULL;";
	    i = ExecuteQuery(sql);

	    sql = "update tbldeliverypersonmaster set strState='' "
		    + "where strState is NULL;";
	    i = ExecuteQuery(sql);

	    sql = "update tbldeliverypersonmaster set intPinCode=0 "
		    + "where intPinCode is NULL;";
	    i = ExecuteQuery(sql);

	    sql = "update tbldeliverypersonmaster set intMobileNo=0 "
		    + "where intMobileNo is NULL;";
	    i = ExecuteQuery(sql);

	    sql = "update tbldeliverypersonmaster set strDeliveryArea='' "
		    + "where strDeliveryArea is NULL;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbldeliverypersonmaster` "
		    + "	CHANGE COLUMN `strAddressLine1` `strAddressLine1` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strDPName`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbldeliverypersonmaster` "
		    + "	CHANGE COLUMN `strAddressLine2` `strAddressLine2` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strAddressLine1`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbldeliverypersonmaster` "
		    + "	CHANGE COLUMN `strAddressLine3` `strAddressLine3` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strAddressLine2`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbldeliverypersonmaster` "
		    + "	CHANGE COLUMN `strCity` `strCity` VARCHAR(20) NOT NULL DEFAULT '' AFTER `strAddressLine3`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbldeliverypersonmaster` "
		    + "	CHANGE COLUMN `strState` `strState` VARCHAR(50) NOT NULL DEFAULT '' AFTER `strCity`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbldeliverypersonmaster` "
		    + "	CHANGE COLUMN `intPinCode` `intPinCode` BIGINT(20) NOT NULL DEFAULT '0' AFTER `strState`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbldeliverypersonmaster` "
		    + "	CHANGE COLUMN `intMobileNo` `intMobileNo` BIGINT(20) NOT NULL DEFAULT '0' AFTER `intPinCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbldeliverypersonmaster` "
		    + "	ADD COLUMN `strClientCode` VARCHAR(10) NOT NULL AFTER `strOperational`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbldeliverypersonmaster` "
		    + "	ADD COLUMN `strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strClientCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbldeliverypersonmaster` "
		    + "	CHANGE COLUMN `dtDateCreated` `dteDateCreated` DATETIME NULL DEFAULT NULL AFTER `strUserEdited`, "
		    + "	CHANGE COLUMN `dtDateEdited` `dteDateEdited` DATETIME NULL DEFAULT NULL AFTER `dteDateCreated`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblitemmaster` ADD COLUMN `strRawMaterial` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `intProcDay`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblitemcurrentstk` "
		    + " ADD COLUMN `strGroupName` VARCHAR(100) NOT NULL DEFAULT '' FIRST, "
		    + " ADD COLUMN `strSubgroupName` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strGroupName`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblposmaster` "
		    + " ADD COLUMN `strDelayedSettlementForDB` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strCounterWiseBilling`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` "
		    + "	CHANGE COLUMN `strOutletUID` `strOutletUID` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strPostWebserviceURL`, "
		    + "	CHANGE COLUMN `strPOSID` `strPOSID` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strOutletUID`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblcustomermaster` "
		    + "	ADD COLUMN `strCRMId` VARCHAR(50) NOT NULL DEFAULT '' AFTER `strEmailId`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblcrmpoints` "
		    + "	ADD COLUMN `strCustomerId` VARCHAR(50) NOT NULL DEFAULT '0.00' AFTER `dblValue`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblcrmpoints` "
		    + "	CHANGE COLUMN `strCustomerId` `strCustomerId` VARCHAR(50) NOT NULL DEFAULT '' AFTER `dblValue`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblimportexcel` "
		    + "	ADD COLUMN `strSubMenuHeadName` VARCHAR(50) NOT NULL DEFAULT '' AFTER `strSubGroupCode`, "
		    + "	ADD COLUMN `strSubMenuHeadCode` VARCHAR(10) NOT NULL DEFAULT '' AFTER `strSubMenuHeadName`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbltaxhd` "
		    + "	ADD COLUMN `strAreaCode` VARCHAR(100) NOT NULL DEFAULT '' AFTER `dteDateEdited`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbltaxhd` "
		    + "	ADD COLUMN `strOperationType` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strAreaCode`, "
		    + "	ADD COLUMN `strItemType` VARCHAR(20) NOT NULL DEFAULT '' AFTER `strOperationType`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup`	ALTER `strSenderEmailId` DROP DEFAULT;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup`	CHANGE COLUMN `strSenderEmailId` `strSenderEmailId` VARCHAR(40) NOT NULL AFTER `strMenuItemDispSeq`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblvoidkot` ADD COLUMN `strPrintKOT` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strManualKOTNo`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` "
		    + "	ADD COLUMN `intAdvReceiptPrintCount` INT NOT NULL DEFAULT '0' AFTER `longCustSeries`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` "
		    + " ADD COLUMN `strHomeDeliverySMS` VARCHAR(300) NOT NULL AFTER `intAdvReceiptPrintCount`, "
		    + " ADD COLUMN `strBillStettlementSMS` VARCHAR(300) NOT NULL AFTER `strHomeDeliverySMS`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` "
		    + "	ADD COLUMN `strBillFormatType` VARCHAR(30) NOT NULL DEFAULT '' AFTER `strBillStettlementSMS`;";
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` "
		    + "	ADD COLUMN `strActivePromotions` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strBillFormatType`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblpromotionmaster` "
		    + "	ADD COLUMN `strPromotionOn` VARCHAR(30) NOT NULL AFTER `strPromoCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblpromotiondtl` "
		    + "	ADD COLUMN `strPromotionOn` VARCHAR(30) NOT NULL AFTER `strPromoCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbltempvoidkot` ALTER `strItemCode` DROP DEFAULT;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbltempvoidkot` "
		    + "	CHANGE COLUMN `strItemCode` `strItemCode` VARCHAR(20) NOT NULL AFTER `strItemName`;";
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblitemrtemp` DROP COLUMN `NcKotYN`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblitemrtemp` "
		    + " ADD COLUMN `strNCKotYN` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strDelBoyCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblnonchargablekot` ADD COLUMN `strReasonCode` VARCHAR(10) NOT NULL AFTER `strDataPostFlag`; ";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblnonchargablekot` ADD COLUMN `strRemark` VARCHAR(50) NOT NULL AFTER `strReasonCode`; ";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` "
		    + "	ADD COLUMN `strSendHomeDelSMS` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strActivePromotions`, "
		    + "	ADD COLUMN `strSendBillSettlementSMS` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strSendHomeDelSMS`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbilldtl` "
		    + "	ADD COLUMN `dblRate` DECIMAL(18,2) NOT NULL AFTER `strAdvBookingNo`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbilldtl` "
		    + "	ADD COLUMN `dblRate` DECIMAL(18,2) NOT NULL AFTER `strAdvBookingNo`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbillmodifierdtl` "
		    + "	ADD COLUMN `dblRate` DECIMAL(18,2) NOT NULL AFTER `strModifierName`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbillmodifierdtl` "
		    + "	ADD COLUMN `dblRate` DECIMAL(18,2) NOT NULL AFTER `strModifierName`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbladvbookitemtemp` ALTER `dblItemQuantity` DROP DEFAULT;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbladvbookitemtemp` "
		    + "	CHANGE COLUMN `dblItemQuantity` `dblItemQuantity` DECIMAL(18,2) NOT NULL AFTER `strItemName`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblreasonmaster` "
		    + "	ADD COLUMN `strNCKOT` VARCHAR(1) NOT NULL AFTER `strDataPostFlag`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblnonchargablekot` "
		    + "	ADD COLUMN `dteNCKOTDate` DATETIME NOT NULL AFTER `strRemark`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblnonchargablekot` "
		    + "	ADD COLUMN `strUserCreated` DATETIME NOT NULL AFTER `dteNCKOTDate`, "
		    + "	ADD COLUMN `strUserEdited` DATETIME NOT NULL AFTER `strUserCreated`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` "
		    + "	ADD COLUMN `strSMSType` VARCHAR(50) NOT NULL AFTER `strSendBillSettlementSMS`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` "
		    + "	ADD COLUMN `strPrintShortNameOnKOT` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strSMSType`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblpspdtl` "
		    + "	DROP INDEX `strItemCode`, "
		    + "	DROP INDEX `strPSPCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblpsphd` "
		    + "	DROP PRIMARY KEY, "
		    + "	DROP INDEX `strPOSCode`, "
		    + "	DROP INDEX `strStkInCode`, "
		    + "	DROP INDEX `strStkOutCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblpsphd` "
		    + "	ADD COLUMN `strClientCode` VARCHAR(20) NOT NULL AFTER `dteDateEdited`, "
		    + "	ADD COLUMN `strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strClientCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblpspdtl` "
		    + "	ADD COLUMN `strClientCode` VARCHAR(20) NOT NULL AFTER `dblVairanceAmt`, "
		    + "	ADD COLUMN `strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strClientCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblpspdtl` ALTER `strPSPCode` DROP DEFAULT;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblpspdtl` CHANGE COLUMN `strPSPCode` `strPSPCode` VARCHAR(20) NOT NULL FIRST;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblpsphd` ALTER `strPSPCode` DROP DEFAULT;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblpsphd` CHANGE COLUMN `strPSPCode` `strPSPCode` VARCHAR(20) NOT NULL FIRST;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` "
		    + "	ADD COLUMN `strShowCustHelp` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strPrintShortNameOnKOT`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblreasonmaster` "
		    + "	CHANGE COLUMN `strNCKOT` `strNCKOT` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strDataPostFlag`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblrecipedtl` "
		    + "	ADD COLUMN `strPOSCode` VARCHAR(20) NOT NULL AFTER `dblQuantity`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblrecipehd` "
		    + "	ADD COLUMN `strPOSCode` VARCHAR(20) NOT NULL AFTER `dteToDate`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` "
		    + "	ADD COLUMN `strPrintOnVoidBill` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strShowCustHelp`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblstkouthd` ALTER `dtePurchaseBillDate` DROP DEFAULT;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblstkouthd` "
		    + "	CHANGE COLUMN `strPurchaseBillNo` `strPurchaseBillNo` VARCHAR(50) NOT NULL DEFAULT '' AFTER `strReasonCode`, "
		    + "	CHANGE COLUMN `dtePurchaseBillDate` `dtePurchaseBillDate` DATETIME NOT NULL AFTER `strPurchaseBillNo`;";
	    i = ExecuteQuery(sql);

	    sql = "update tblstkouthd set strPurchaseBillNo='' where strPurchaseBillNo is NULL;";
	    i = ExecuteQuery(sql);

	    sql = "update tblstkouthd set dtePurchaseBillDate=dteStkOutDate where dtePurchaseBillDate is NULL;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblitemmaster` "
		    + "	CHANGE COLUMN `strShortName` `strShortName` VARCHAR(20) NOT NULL DEFAULT '' AFTER `strDiscountApply`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` "
		    + "	ADD COLUMN `strPostSalesDataToMMS` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strPrintOnVoidBill`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbillsettlementdtl` ADD PRIMARY KEY (`strBillNo`, `strSettlementCode`);";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbilltaxdtl` ADD PRIMARY KEY (`strBillNo`, `strTaxCode`);";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbillhd` "
		    + " CHANGE COLUMN `strAdvBookingNo` `strAdvBookingNo` VARCHAR(10) NOT NULL DEFAULT '' AFTER `strBillNo`;";
	    i = ExecuteQuery(sql);

	    sql = "update tblbillhd set strAdvBookingNo='' where strAdvBookingNo='0';";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` "
		    + " ADD COLUMN `strCustAreaMasterCompulsory` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strPostSalesDataToMMS`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbladvbookbillhd` "
		    + "	ADD COLUMN `strOrderType` VARCHAR(10) NOT NULL AFTER `dblHomeDelCharges`;";
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblproductionhd` "
		    + "CHANGE COLUMN `dtrDataPostFlag` `strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strClientCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblproductionhd`	ADD PRIMARY KEY (`strProductionCode`, `strClientCode`);";
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblproductiondtl`	ALTER `dblReorderQty` DROP DEFAULT;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblproductiondtl`	CHANGE COLUMN `dblReorderQty` `dblOrderQty` DECIMAL(18,4) NOT NULL AFTER `dblStock`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblproductionhd`	ADD COLUMN `strRemarks` VARCHAR(200) NOT NULL AFTER `strClose`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblitemmaster` ADD COLUMN `dblItemRate` DECIMAL(18,4) NOT NULL DEFAULT '0.00' AFTER `strRawMaterial`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblitemmaster` DROP COLUMN `dblItemRate`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblitemmaster` "
		    + "	ADD COLUMN `dblSalePrice` DECIMAL(18,4) NOT NULL DEFAULT '0.0000' AFTER `strRawMaterial`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` "
		    + "	ADD COLUMN `strPriceFrom` VARCHAR(20) NOT NULL AFTER `strCustAreaMasterCompulsory`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblitemmaster` "
		    + "	CHANGE COLUMN `strExternalCode` `strExternalCode` VARCHAR(100) NOT NULL DEFAULT '0.00' AFTER `intProcTimeMin`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` ADD COLUMN `strShowPrinterErrorMessage` VARCHAR(1) NOT NULL DEFAULT 'Y' AFTER `strPriceFrom`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` "
		    + "	CHANGE COLUMN `strPriceFrom` `strPriceFrom` VARCHAR(20) NOT NULL DEFAULT 'Menu Pricing' AFTER `strCustAreaMasterCompulsory`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup`	ADD COLUMN `strTouchScreenMode` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strShowPrinterErrorMessage`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` "
		    + "	CHANGE COLUMN `strTouchScreenMode` `strTouchScreenMode` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strShowPrinterErrorMessage`;";
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbladvanceordertypemaster` ADD COLUMN `strPOSCode` VARCHAR(10) NOT NULL AFTER `strOperational`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblcustomertypemaster` "
		    + "	ADD COLUMN `dblDiscPer` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `strCustType`, "
		    + "	ADD COLUMN `strClientCode` VARCHAR(50) NOT NULL AFTER `dteDateEdited`, "
		    + "	ADD COLUMN `strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strClientCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblitemmaster` ADD COLUMN `strItemForSale` VARCHAR(1) NOT NULL DEFAULT 'Y' AFTER `dblSalePrice`;";
	    i = ExecuteQuery(sql);

	    sql = "update tbladvancereceiptdtl set dteCheque='1980-01-01' where dteCheque='0000-00-00';";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblpromotiondtl` "
		    + "	CHANGE COLUMN `dblQuantity` `dblGetQty` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `strOperator`, "
		    + "	DROP COLUMN `dblValue`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblpromotiondtl` "
		    + "	ADD COLUMN `strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strClientCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblpromotionmaster` "
		    + "	ADD COLUMN `dblBuyQty` DECIMAL(18,2) NOT NULL AFTER `strPromoItemCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbilldtl` ADD COLUMN `strPromoCode` VARCHAR(20) NOT NULL AFTER `tdhYN`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbilldtl` ADD COLUMN `strPromoCode` VARCHAR(20) NOT NULL AFTER `tdhYN`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbilldtl` CHANGE COLUMN `strPromoCode` `strPromoCode` VARCHAR(20) NOT NULL DEFAULT '' AFTER `tdhYN`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbilldtl` CHANGE COLUMN `strPromoCode` `strPromoCode` VARCHAR(20) NOT NULL DEFAULT '' AFTER `tdhYN`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblcustomermaster` ENGINE=MyISAM;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbillhd` ENGINE=MyISAM;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblpromotionmaster` ADD COLUMN `strOperator` VARCHAR(3) NOT NULL AFTER `strPromoItemCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblpromotiondtl` DROP COLUMN `strOperator`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblpromotionmaster` ADD COLUMN `strPromoName` VARCHAR(200) NOT NULL AFTER `strPromoCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblpromotionmaster` ADD COLUMN `strType` VARCHAR(10) NOT NULL AFTER `strDays`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblpromotiondtl` DROP COLUMN `strType`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbltaxhd`	ALTER `strTaxOnTaxCode` DROP DEFAULT;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbltaxhd` "
		    + "	CHANGE COLUMN `strTaxOnTaxCode` `strTaxOnTaxCode` VARCHAR(100) NOT NULL AFTER `strTaxOnTax`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbldebitcardtype` ADD COLUMN `strCustomerCompulsory` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `dblRedemptionLimitValue`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbldebitcardmaster` CHANGE COLUMN `intPassword` `intPassword` INT(4) NOT NULL DEFAULT '0' AFTER `strCustomerCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbldebitcardtype` "
		    + "	ADD COLUMN `strClientCode` VARCHAR(20) NOT NULL AFTER `strCustomerCompulsory`, "
		    + "	ADD COLUMN `strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strClientCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbldebitcardsettlementdtl` ALTER `strApplicable` DROP DEFAULT;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbldebitcardsettlementdtl` "
		    + "CHANGE COLUMN `strApplicable` `strApplicable` VARCHAR(15) NOT NULL AFTER `strSettlementCode`, "
		    + "ADD COLUMN `strClientCode` VARCHAR(15) NOT NULL AFTER `strApplicable`,"
		    + "ADD COLUMN `strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strClientCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbldebitcardmaster` DROP PRIMARY KEY;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbldebitcardmaster` ADD COLUMN `strClientCode` VARCHAR(20) NOT NULL AFTER `strDataPostFlag`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbldebitcardrecharge` ADD COLUMN `strClientCode` VARCHAR(20) NOT NULL AFTER `strDataPostFlag`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbldebitcardrecharge` "
		    + "	CHANGE COLUMN `dblRedeemableAmount` `dblRedeemableAmount` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `dblRechargeAmount`, "
		    + "	CHANGE COLUMN `strRechargeSettlmentMode` `strRechargeSettlmentMode` VARCHAR(10) NOT NULL DEFAULT '' AFTER `dblRedeemableAmount`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblcrmpoints` ADD COLUMN `dteBillDate` DATETIME NOT NULL AFTER `strCustomerId`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbillhd` ADD COLUMN `strCouponCode` VARCHAR(100) NOT NULL DEFAULT '' AFTER `dblDeliveryCharges`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbillhd` ADD COLUMN `strCouponCode` VARCHAR(100) NOT NULL DEFAULT '' AFTER `dblDeliveryCharges`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbluserhd` DROP COLUMN `dtCreatedDate` ;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` ADD COLUMN `strCardInterfaceType` VARCHAR(50) NOT NULL AFTER `strTouchScreenMode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` ADD COLUMN `strCMSIntegrationYN` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strCardInterfaceType`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` ADD COLUMN `strCMSWebServiceURL` VARCHAR(255) NOT NULL DEFAULT '' AFTER `strCMSIntegrationYN`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` "
		    + "	ADD COLUMN `strChangeQtyForExternalCode` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strCMSWebServiceURL`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` "
		    + "	ADD COLUMN `strPointsOnBillPrint` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strChangeQtyForExternalCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup`	ADD COLUMN `strCMSPOSCode` VARCHAR(20) NOT NULL DEFAULT '' AFTER `strPointsOnBillPrint`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblitemrtemp` ADD COLUMN `strCustomerName` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strNCKotYN`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblitemrtemp` "
		    + "	ADD COLUMN `strActiveYN` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strCustomerName`, "
		    + "	ADD COLUMN `dblBalance` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `strActiveYN`, "
		    + "	ADD COLUMN `dblCreditLimit` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `dblBalance`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbltablemaster`	ADD COLUMN `strPOSCode` VARCHAR(20) NOT NULL AFTER `intSequence`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbltablemaster`	CHANGE COLUMN `strPOSCode` `strPOSCode` VARCHAR(20) NOT NULL DEFAULT 'P01' AFTER `intSequence`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbladvbookbillhd`	ADD PRIMARY KEY (`strAdvBookingNo`);";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblcustomermaster` ADD PRIMARY KEY (`strCustomerCode`);";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbldeliverypersonmaster` ADD PRIMARY KEY (`strDPCode`);";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblstorelastbill`	ADD PRIMARY KEY (`strPosCode`);";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblareamaster` ADD COLUMN `strPOSCode` VARCHAR(20) NOT NULL DEFAULT 'P01' AFTER `strDataPostFlag`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbladvbookbillhd`	ADD COLUMN `strManualAdvOrderNo` VARCHAR(20) NOT NULL DEFAULT '' AFTER `strOrderType`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup`	ADD COLUMN `strManualAdvOrderNoCompulsory` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strCMSPOSCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup`	ADD COLUMN `strPrintManualAdvOrderNoOnBill` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strManualAdvOrderNoCompulsory`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbilldtl` CHANGE COLUMN `dblRate` `dblRate` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `strAdvBookingNo`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbillmodifierdtl` "
		    + "	CHANGE COLUMN `dblRate` `dblRate` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `strModifierName`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbillpromotiondtl`	CHANGE COLUMN `dblRate` `dblRate` DECIMAL(18,4) NOT NULL DEFAULT '0.00' AFTER `dblQuantity`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbilldtl` CHANGE COLUMN `dblRate` `dblRate` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `strAdvBookingNo`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbillmodifierdtl` CHANGE COLUMN `dblRate` `dblRate` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `strModifierName`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblnonchargablekot` ADD COLUMN `strPOSCode` VARCHAR(10) NOT NULL AFTER `strUserEdited`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblitemmaster` ADD COLUMN `strRevenueHead` VARCHAR(50) NOT NULL DEFAULT 'Food' AFTER `strItemForSale`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup`	ADD COLUMN `strPrintModifierQtyOnKOT` VARCHAR(1) NOT NULL DEFAULT 'Y' AFTER `strPrintManualAdvOrderNoOnBill`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblposmaster` "
		    + "ADD COLUMN `strBillPrinterPort` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strDelayedSettlementForDB`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbillmodifierdtl` CHANGE COLUMN `strModifierName` `strModifierName` VARCHAR(100) NOT NULL AFTER `strModifierCode`";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup`	ADD COLUMN `strNoOfLinesInKOTPrint` VARCHAR(100) NOT NULL DEFAULT '5' AFTER `strPrintModifierQtyOnKOT`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblitemrtemp`	ALTER `strTableNo` DROP DEFAULT;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblitemrtemp`	"
		    + "CHANGE COLUMN `strTableNo` `strTableNo` VARCHAR(10) NOT NULL AFTER `strSerialNo`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbillhd` CHANGE COLUMN `strTableNo` `strTableNo` VARCHAR(10) NOT NULL DEFAULT '' AFTER `strClientCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbillhd` CHANGE COLUMN `strTableNo` `strTableNo` VARCHAR(10) NOT NULL DEFAULT '' AFTER `strClientCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblvoidkot` 	ALTER `strTableNo` DROP DEFAULT;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblvoidkot`	CHANGE COLUMN `strTableNo` `strTableNo` VARCHAR(10) NOT NULL FIRST;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` ADD COLUMN `strMultipleKOTPrintYN` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strNoOfLinesInKOTPrint`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbltempvoidkot`	ALTER `strTableNo` DROP DEFAULT;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbltempvoidkot`	CHANGE COLUMN `strTableNo` `strTableNo` VARCHAR(10) NOT NULL AFTER `dblItemQuantity`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblvoidbillhd` CHANGE COLUMN `strTableNo` `strTableNo` VARCHAR(10) NOT NULL DEFAULT '' AFTER `dteModifyVoidBill`;";
	    i = ExecuteQuery(sql);

	    sql = "grant all on *.* to 'root'@'%' identified by 'root' with grant option;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbladvbookbillhd` "
		    + "	ADD COLUMN `strImageName` VARCHAR(50) NOT NULL DEFAULT '' AFTER `strManualAdvOrderNo`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbladvbookbillhd` "
		    + "ADD COLUMN `strSpecialsymbolImage` VARCHAR(50) NOT NULL DEFAULT '' AFTER `strImageName`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbluserhd` "
		    + " CHANGE COLUMN `strPOSAccess` `strPOSAccess` VARCHAR(255) NOT NULL AFTER `dteValidDate`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` "
		    + " ADD COLUMN `strItemQtyNumpad` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strMultipleKOTPrintYN`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbldayendprocess` "
		    + "	ADD COLUMN `intNoOfNCKOT` INT(50) NOT NULL DEFAULT '0' AFTER `strDataPostFlag`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbldayendprocess` "
		    + "	ADD COLUMN `intNoOfComplimentaryKOT` INT(50) NOT NULL DEFAULT '0' AFTER `intNoOfNCKOT`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbldayendprocess` "
		    + "	ADD COLUMN `intNoOfVoidKOT` INT(50) NOT NULL DEFAULT '0' AFTER `intNoOfComplimentaryKOT`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblpsphd` "
		    + "	CHANGE COLUMN `strStkInCode` `strStkInCode` VARCHAR(9) NOT NULL AFTER `strPOSCode`, "
		    + "	CHANGE COLUMN `strStkOutCode` `strStkOutCode` VARCHAR(9) NOT NULL AFTER `strStkInCode`; "
		    + "";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup`	"
		    + "ADD COLUMN `strTreatMemberAsTable` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strItemQtyNumpad`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` "
		    + " ADD COLUMN `strKOTToLocalPrinter` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strTreatMemberAsTable`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` "
		    + "	ADD COLUMN `blobReportImage` LONGBLOB NULL DEFAULT NULL AFTER `strKOTToLocalPrinter`;";
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbldeliverypersonmaster` "
		    + " ADD COLUMN `dblIncentiveAmt` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `strDataPostFlag`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbldeliverypersonmaster` "
		    + "	ADD COLUMN `strDBCategoryCode` VARCHAR(10) NOT NULL AFTER `dblIncentiveAmt`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbldeliveryboycategorymaster` "
		    + "	CHANGE COLUMN `strDelBoyCategoryCode` `strDelBoyCategoryCode` VARCHAR(20) NOT NULL FIRST;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblareawisedc` "
		    + "	ADD COLUMN `strDelBoyCategoryCode` VARCHAR(10) NOT NULL DEFAULT '' AFTER `strDataPostFlag`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbldebitcardbilldetails`	DROP COLUMN `strPSPCode`;";
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsubgrouphd` "
		    + "	ADD COLUMN `strIncentives` DECIMAL(18,4) NOT NULL DEFAULT '0.00' AFTER `strDataPostFlag`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` "
		    + "	ADD COLUMN `strSettleBtnForDirectBillerBill` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `blobReportImage`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` "
		    + "	ADD COLUMN `strDelBoySelCompulsoryOnDirectBiller` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strSettleBtnForDirectBillerBill`;";
	    i = ExecuteQuery(sql);

	    sql = "Create table IF NOT EXISTS tblitempricingauditdtl like tblmenuitempricingdtl;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbldeliverypersonmaster` "
		    + " CHANGE COLUMN `strDBCategoryCode` `strDBCategoryCode` VARCHAR(10) NOT NULL DEFAULT '' AFTER `dblIncentiveAmt`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsubgrouphd` "
		    + " ADD COLUMN `strIncentives` VARCHAR(10) NOT NULL AFTER `strGroupCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblitemcurrentstk` "
		    + " CHANGE COLUMN `intOpening` `intOpening` DECIMAL(18,2) NOT NULL DEFAULT '0' AFTER `strPOSCode`, "
		    + " CHANGE COLUMN `intOut` `intOut` DECIMAL(18,2) NOT NULL DEFAULT '0' AFTER `intOpening`, "
		    + " CHANGE COLUMN `intIn` `intIn` DECIMAL(18,2) NOT NULL DEFAULT '0' AFTER `intOut`, "
		    + " CHANGE COLUMN `intSale` `intSale` DECIMAL(18,2) NOT NULL DEFAULT '0' AFTER `intIn`, "
		    + " CHANGE COLUMN `intBalance` `intBalance` DECIMAL(18,2) NOT NULL DEFAULT '0' AFTER `intSale`;";
	    i = ExecuteQuery(sql);

	    sql = "Create table IF NOT EXISTS tblqadvancereceiptdtl like tbladvancereceiptdtl; ";
	    i = ExecuteQuery(sql);

	    sql = "Create table IF NOT EXISTS tblqadvancereceipthd like tbladvancereceipthd;";
	    i = ExecuteQuery(sql);

	    sql = "Create table IF NOT EXISTS tblqadvbookbilldtl like tbladvbookbilldtl;";
	    i = ExecuteQuery(sql);

	    sql = "Create table IF NOT EXISTS tblqadvbookbillhd like tbladvbookbillhd;";
	    i = ExecuteQuery(sql);

	    sql = "CHANGE COLUMN `strItemDetails` `strItemDetails` VARCHAR(200) NOT NULL DEFAULT '0.00' AFTER `strExternalCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblareawisedc`	DROP COLUMN `strDelBoyCategoryCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblareawisedc` "
		    + "	ADD COLUMN `strCustAreaTypeCode` VARCHAR(10) NOT NULL DEFAULT '' AFTER `strDataPostFlag`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` "
		    + "	ADD COLUMN `strCMSMemberForKOTJPOS` VARCHAR(1) NOT NULL DEFAULT 'Y' AFTER `strDelBoySelCompulsoryOnDirectBiller`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` "
		    + "	ADD COLUMN `strCMSMemberForKOTMPOS` VARCHAR(1) NOT NULL DEFAULT 'Y' AFTER `strCMSMemberForKOTJPOS`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblareawisedc` "
		    + "	ADD COLUMN `strCustTypeCode` VARCHAR(10) NOT NULL DEFAULT '' AFTER `strCustAreaTypeCode`;";
	    i = ExecuteQuery(sql);

	    sql = "update tbltablemaster set strPOSCode='P01' "
		    + "where strPOSCode=''";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbillhd` "
		    + " ADD COLUMN `strAreaCode` VARCHAR(10) NOT NULL DEFAULT '' AFTER `strCouponCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbillhd` "
		    + " ADD COLUMN `strAreaCode` VARCHAR(10) NOT NULL DEFAULT '' AFTER `strCouponCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblreasonmaster`	ADD COLUMN `strVoidAdvOrder` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strNCKOT`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblcustomermaster` "
		    + "	CHANGE COLUMN `strGender` `strGender` VARCHAR(7) NOT NULL DEFAULT 'Male' AFTER `dteDOB`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbltempsalesflash` "
		    + "	ADD COLUMN `dblRate` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `struser`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbluserhd` "
		    + "	ADD COLUMN `imgUserIcon` LONGBLOB NOT NULL AFTER `strDataPostFlag`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbluserhd` "
		    + "	ADD COLUMN `strImgUserIconPath` VARCHAR(500) NOT NULL AFTER `imgUserIcon`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbltempsalesflash` ALTER `strcode` DROP DEFAULT;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbltempsalesflash` CHANGE COLUMN `strcode` `strcode` VARCHAR(50) NOT NULL FIRST;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblitemrtemp`	ADD COLUMN `strCounterCode` VARCHAR(3) NOT NULL DEFAULT '' AFTER `dblCreditLimit`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbilldtl`	ADD COLUMN `strCounterCode` VARCHAR(3) NOT NULL DEFAULT '' AFTER `strPromoCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbilldtl`	ADD COLUMN `strCounterCode` VARCHAR(3) NOT NULL DEFAULT '' AFTER `strPromoCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbuildingmaster` "
		    + "	ADD COLUMN `dblDeliveryBoyPayOut` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `strDataPostFlag`, "
		    + "	ADD COLUMN `dblHelperPayOut` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `dblDeliveryBoyPayOut`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblitempricingauditdtl` DROP PRIMARY KEY;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblitempricingauditdtl` ALTER `longPricingId` DROP DEFAULT;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblitempricingauditdtl` "
		    + "	CHANGE COLUMN `longPricingId` `longPricingId` BIGINT(20) NOT NULL AFTER `strHourlyPricing`, "
		    + "	DROP PRIMARY KEY;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblcustareatypemaster` ALTER `strCustAreaTypeCode` DROP DEFAULT,"
		    + "ALTER `strCustAreaTypeName` DROP DEFAULT;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblcustareatypemaster` "
		    + "CHANGE COLUMN `strCustAreaTypeCode` `strZoneCode` VARCHAR(10) NOT NULL FIRST, "
		    + "CHANGE COLUMN `strCustAreaTypeName` `strZoneName` VARCHAR(100) NOT NULL AFTER `strZoneCode`;";
	    i = ExecuteQuery(sql);

	    sql = "RENAME TABLE `tblcustareatypemaster` TO `tblzonemaster`;";
	    i = ExecuteQuery(sql);

	    sql = "DELETE FROM `jpos`.`tblinternal` WHERE  `strTransactionType`='CustAreaType' AND `dblLastNo`=0 LIMIT 1;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbuildingmaster` ADD COLUMN `strZoneCode` VARCHAR(10) NOT NULL AFTER `dblHelperPayOut`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblareawisedc` DROP COLUMN `strCustAreaTypeCode`;";
	    i = ExecuteQuery(sql);

	    sql = "CREATE TABLE IF NOT EXISTS `tblhomedeldtl` ( "
		    + "	`strBillNo` VARCHAR(10) NOT NULL, "
		    + "	`strDPCode` VARCHAR(10) NOT NULL, "
		    + "	`strClientCode` VARCHAR(10) NOT NULL, "
		    + "	`strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' "
		    + ") "
		    + "COLLATE='latin1_swedish_ci' "
		    + "ENGINE=InnoDB "
		    + ";";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` "
		    + "	ADD COLUMN `strDontShowAdvOrderInOtherPOS` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strCMSMemberForKOTMPOS`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` "
		    + "	ADD COLUMN `strPrintZeroAmtModifierInBill` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strDontShowAdvOrderInOtherPOS`;";
	    i = ExecuteQuery(sql);

	    sql = "Create table IF NOT EXISTS tblqadvordermodifierdtl like tbladvordermodifierdtl;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbilldtl` "
		    + "	ADD COLUMN `strWaiterNo` VARCHAR(10) NOT NULL DEFAULT '' AFTER `strCounterCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbilldtl` "
		    + "	ADD COLUMN `strWaiterNo` VARCHAR(10) NOT NULL DEFAULT '' AFTER `strCounterCode`;";
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbltempadvorderflash`"
		    + "ALTER `dblGrandTotal` DROP DEFAULT,"
		    + "ALTER `dblAdvDeposite` DROP DEFAULT,ALTER `dblBalance` DROP DEFAULT,"
		    + "ALTER `dblSettlementAmt` DROP DEFAULT;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbltempadvorderflash` "
		    + "	CHANGE COLUMN `dblGrandTotal` `dblGrandTotal` VARCHAR(50) NOT NULL AFTER `strSettlementMode`, "
		    + "	CHANGE COLUMN `dblAdvDeposite` `dblAdvDeposite` VARCHAR(50) NOT NULL AFTER `dblGrandTotal`, "
		    + "	CHANGE COLUMN `dblBalance` `dblBalance` VARCHAR(50) NOT NULL AFTER `dblAdvDeposite`, "
		    + "	CHANGE COLUMN `dblSettlementAmt` `dblSettlementAmt` VARCHAR(50) NOT NULL AFTER `strBillNo`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblposmaster`"
		    + "	ADD COLUMN `strAdvReceiptPrinterPort` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strBillPrinterPort`;";
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblreservation` "
		    + "	ADD COLUMN `strSmoking` VARCHAR(5) NOT NULL DEFAULT 'N' AFTER `intPax`, "
		    + "	ADD COLUMN `strTableNo` VARCHAR(10) NOT NULL DEFAULT '' AFTER `strSpecialInfo`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblcostcentermaster` "
		    + " ADD COLUMN `strSecondaryPrinterPort` VARCHAR(100) NOT NULL AFTER `strPrinterPort`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup`"
		    + " ADD COLUMN `strPrintKOTYN` VARCHAR(1) NOT NULL DEFAULT 'Y' AFTER `strPrintZeroAmtModifierInBill`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` "
		    + "	ADD COLUMN `strCreditCardSlipNoCompulsoryYN` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strPrintKOTYN`, "
		    + "	ADD COLUMN `strCreditCardExpiryDateCompulsoryYN` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strCreditCardSlipNoCompulsoryYN`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblwaitermaster` "
		    + "	ADD COLUMN `strOperational` VARCHAR(1) NOT NULL DEFAULT 'Y' AFTER `strStatus`, "
		    + "	ADD COLUMN `strDebitCardString` VARCHAR(200) NOT NULL DEFAULT '' AFTER `strOperational`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbillhd` ENGINE=InnoDB;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblitemrtemp` "
		    + "	ADD COLUMN `strPromoCode` VARCHAR(10) NOT NULL DEFAULT '' AFTER `strCounterCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbillhd` "
		    + " ADD COLUMN `strDiscountRemark` VARCHAR(200) NOT NULL DEFAULT '' AFTER `strAreaCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbillhd` "
		    + " ADD COLUMN `strDiscountRemark` VARCHAR(200) NOT NULL DEFAULT '' AFTER `strAreaCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblvoidkot` "
		    + "	ADD COLUMN `strRemark` VARCHAR(200) NOT NULL DEFAULT 'N' AFTER `strPrintKOT`;";
	    i = ExecuteQuery(sql);

	    // sql = "ALTER TABLE `tblsetup` DROP COLUMN `strSelectWaiterFromCardSwipe`;";
	    // i = ExecuteQuery(sql);
	    sql = "ALTER TABLE `tblsetup` "
		    + "	ADD COLUMN `strSelectWaiterFromCardSwipe` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strCreditCardExpiryDateCompulsoryYN`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup`	"
		    + " ADD COLUMN `strMultiWaiterSelectionOnMakeKOT` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strSelectWaiterFromCardSwipe`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` "
		    + "	ADD COLUMN `strMoveTableToOtherPOS` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strMultiWaiterSelectionOnMakeKOT`, "
		    + "	ADD COLUMN `strMoveKOTToOtherPOS` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strMoveTableToOtherPOS`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblreservation` "
		    + "ADD COLUMN `strAMPM` VARCHAR(2) NOT NULL AFTER `tmeResTime`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblvoidbillhd` "
		    + " ADD COLUMN `strRemark` VARCHAR(200) NOT NULL DEFAULT '' AFTER `strDataPostFlag`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` "
		    + "	ADD COLUMN `strCalculateTaxOnMakeKOT` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strMoveKOTToOtherPOS`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbillmodifierdtl` "
		    + "	ALTER `strModifierName` DROP DEFAULT;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbillmodifierdtl`"
		    + "	CHANGE COLUMN `strModifierName` `strModifierName` VARCHAR(100) NOT NULL AFTER `strModifierCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup`	ADD COLUMN `strReceiverEmailId` VARCHAR(500) NOT NULL DEFAULT '' AFTER `strCalculateTaxOnMakeKOT`;";
	    i = ExecuteQuery(sql);

	    sql = "  ALTER TABLE `tblcostcentermaster` "
		    + "	ADD COLUMN `strPrintOnBothPrinters` VARCHAR(5) NOT NULL DEFAULT 'N' AFTER `strSecondaryPrinterPort`;";
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbltempsalesflash1` "
		    + "	ADD COLUMN `strUser` VARCHAR(100) NOT NULL AFTER `dbltipamt`;";
	    i = ExecuteQuery(sql);

	    sql = "CREATE TABLE `tbltempsalesflashtotals1` ( "
		    + "	`dblsubtotal` VARCHAR(100) NOT NULL, "
		    + "	`dbltaxamt` VARCHAR(100) NOT NULL, "
		    + "	`dbldiscamt` VARCHAR(100) NOT NULL, "
		    + "	`dblsettlementamt` VARCHAR(100) NOT NULL "
		    + ") "
		    + "COLLATE='latin1_swedish_ci' "
		    + "ENGINE=InnoDB "
		    + ";";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbltempsalesflashtotals1` "
		    + "	ADD COLUMN `strUser` VARCHAR(100) NOT NULL AFTER `dblsettlementamt`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbillhd` ADD INDEX `strBillNo` (`strBillNo`);";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbillhd` ADD INDEX `strBillNo` (`strBillNo`);";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbillsettlementdtl` ADD PRIMARY KEY (`strBillNo`, `strSettlementCode`);";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbillsettlementdtl` ADD PRIMARY KEY (`strBillNo`, `strSettlementCode`);";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbillmodifierdtl` ADD INDEX `strBillNo` (`strBillNo`);";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbilltaxdtl` ADD PRIMARY KEY (`strBillNo`, `strTaxCode`);";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbilltaxdtl`	ADD PRIMARY KEY (`strBillNo`, `strTaxCode`);";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbillmodifierdtl` ADD INDEX `strBillNo` (`strBillNo`);";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbltempsalesflash1` "
		    + "	CHANGE COLUMN `strbillno` `strbillno` VARCHAR(100) NOT NULL DEFAULT '' FIRST, "
		    + "	CHANGE COLUMN `dtebilldate` `dtebilldate` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strbillno`, "
		    + "	CHANGE COLUMN `tmebilltime` `tmebilltime` VARCHAR(100) NOT NULL DEFAULT '' AFTER `dtebilldate`, "
		    + "	CHANGE COLUMN `strtablename` `strtablename` VARCHAR(100) NOT NULL DEFAULT '' AFTER `tmebilltime`, "
		    + "	CHANGE COLUMN `strposcode` `strposcode` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strtablename`, "
		    + "	CHANGE COLUMN `strpaymode` `strpaymode` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strposcode`, "
		    + "	CHANGE COLUMN `dblsubtotal` `dblsubtotal` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strpaymode`, "
		    + "	CHANGE COLUMN `dbldiscper` `dbldiscper` VARCHAR(100) NOT NULL DEFAULT '' AFTER `dblsubtotal`;";
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbltempsalesflash1` "
		    + "	CHANGE COLUMN `dbldeliverycharges` `dbldeliverycharges` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strcustomercode`, "
		    + "	CHANGE COLUMN `strremarks` `strremarks` VARCHAR(200) NOT NULL DEFAULT '' AFTER `dbldeliverycharges`, "
		    + "	CHANGE COLUMN `strcustomername` `strcustomername` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strremarks`, "
		    + "	CHANGE COLUMN `dbltipamt` `dbltipamt` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strcustomername`;";
	    i = ExecuteQuery(sql);

	    sql = "CREATE TABLE `tblshortcutkeysetup` ( "
		    + "	`strShortcutKey` VARCHAR(10) NOT NULL, "
		    + "	`strModuleName` VARCHAR(500) NOT NULL, "
		    + "	`strModuleType` VARCHAR(5) NOT NULL "
		    + ") "
		    + "COLLATE='utf8_general_ci' "
		    + "ENGINE=InnoDB "
		    + ";";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbltempsalesflash1` ADD COLUMN `strdiscremarks` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strUser`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblvoidbilldtl` ADD COLUMN `strRemarks` VARCHAR(500) NOT NULL DEFAULT 'NA' AFTER `strKOTNo`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblvoidmodifierdtl` ADD COLUMN `strRemarks` VARCHAR(500) NOT NULL DEFAULT 'NA' AFTER `strDataPostFlag`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblvoidmodifierdtl` ADD COLUMN `strReasonCode` VARCHAR(10) NOT NULL AFTER `strRemarks`;";
	    i = ExecuteQuery(sql);

	    sql = "CREATE TABLE `tbltruncationdtl` "
		    + "(`strUser` VARCHAR(50) NOT NULL, "
		    + "	`strTruncateForms` VARCHAR(200) NOT NULL DEFAULT '', "
		    + "	`dteDate` DATETIME NOT NULL, "
		    + "	`strModuleType` VARCHAR(50) NOT NULL "
		    + ") "
		    + "COLLATE='utf8_general_ci' "
		    + "ENGINE=InnoDB "
		    + ";";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbilldtl` ADD COLUMN `dblDiscountAmt` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `strWaiterNo`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbilldtl` ADD COLUMN `dblDiscountAmt` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `strWaiterNo`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` "
		    + "	ADD COLUMN `strCalculateDiscItemWise` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strReceiverEmailId`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbltempsalesflash` "
		    + "	ALTER `strposcode` DROP DEFAULT; ";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbltempsalesflash` "
		    + " CHANGE COLUMN `strposcode` `strposcode` VARCHAR(100) NOT NULL AFTER `dblamount`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbltempadvorderflash` "
		    + " CHANGE COLUMN `strSettlementMode` `strSettlementMode` VARCHAR(100) NOT NULL AFTER `strPOSCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` "
		    + " ADD COLUMN `strTakewayCustomerSelection` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strCalculateDiscItemWise`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbluserhd` "
		    + " ADD COLUMN `strDebitCardString` VARCHAR(50) NOT NULL DEFAULT '' AFTER `strImgUserIconPath`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbltempsalesflash` "
		    + "	ADD COLUMN `dblsubtotal` VARCHAR(100) NOT NULL AFTER `dblRate`, "
		    + "	ADD COLUMN `dbldiscamt` VARCHAR(100) NOT NULL AFTER `dblsubtotal`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblitemrtemp` ADD COLUMN `dblRate` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `strPromoCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` "
		    + "	ADD COLUMN `StrShowItemStkColumnInDB` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strTakewayCustomerSelection`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblhomedeldtl` "
		    + "	ADD COLUMN `strSettleYN` VARCHAR(5) NOT NULL DEFAULT 'N' AFTER `strDataPostFlag`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbillhd` "
		    + "	ADD COLUMN `strTakeAwayRemarks` VARCHAR(200) NOT NULL DEFAULT '' AFTER `strDiscountRemark`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbillhd` "
		    + "	ADD COLUMN `strTakeAwayRemarks` VARCHAR(200) NOT NULL DEFAULT '' AFTER `strDiscountRemark`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup`	ADD COLUMN `strItemType` VARCHAR(10) NOT NULL DEFAULT 'Both' AFTER `StrShowItemStkColumnInDB`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` "
		    + "ADD COLUMN `strAllowNewAreaMasterFromCustMaster` VARCHAR(5) NOT NULL DEFAULT 'N' AFTER `strItemType`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` ADD COLUMN `strCustAddressSelectionForBill` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strAllowNewAreaMasterFromCustMaster`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblposmaster` "
		    + "	ADD COLUMN `strOperationalYN` VARCHAR(5) NOT NULL DEFAULT 'N' AFTER `strAdvReceiptPrinterPort`;";
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbldayendprocess` "
		    + "CHANGE COLUMN `strPOSCode` `strPOSCode` VARCHAR(5) NOT NULL FIRST ";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` "
		    + "	ADD COLUMN `strGenrateMI` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strCustAddressSelectionForBill`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` 	ADD COLUMN `strFTPAddress` VARCHAR(50) NOT NULL DEFAULT '' AFTER `strGenrateMI`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` "
		    + "ADD COLUMN `strFTPServerUserName` VARCHAR(50) NOT NULL DEFAULT '' AFTER `strFTPAddress`, "
		    + "ADD COLUMN `strFTPServerPass` VARCHAR(50) NOT NULL DEFAULT '' AFTER `strFTPServerUserName`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblitemmaster` "
		    + "ADD COLUMN `strItemWeight` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `strRevenueHead`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` "
		    + "ADD COLUMN `strAllowToCalculateItemWeight` VARCHAR(5) NOT NULL DEFAULT 'N' AFTER `strFTPServerPass`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbldebitcardmaster` ADD COLUMN `intCardNo` BIGINT NOT NULL AFTER `strClientCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbldebitcardmaster` ALTER `intCardNo` DROP DEFAULT;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbldebitcardmaster` CHANGE COLUMN `intCardNo` `intCardNo` VARCHAR(50) NOT NULL AFTER `strClientCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` "
		    + "ADD COLUMN `strShowBillsDtlType` VARCHAR(20) NOT NULL AFTER `strAllowToCalculateItemWeight`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` "
		    + "ADD COLUMN `strPrintTaxInvoiceOnBill` VARCHAR(5) NOT NULL DEFAULT 'Y' AFTER `strShowBillsDtlType`, "
		    + "ADD COLUMN `strPrintInclusiveOfAllTaxesOnBill` VARCHAR(5) NOT NULL DEFAULT 'Y' AFTER `strPrintTaxInvoiceOnBill`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblitemrtemp` CHANGE COLUMN `strCounterCode` `strCounterCode` VARCHAR(4) NOT NULL DEFAULT '' AFTER `dblCreditLimit`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbilldtl` CHANGE COLUMN `strCounterCode` `strCounterCode` VARCHAR(5) NOT NULL DEFAULT '' AFTER `strPromoCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbilldtl` CHANGE COLUMN `strCounterCode` `strCounterCode` VARCHAR(5) NOT NULL DEFAULT '' AFTER `strPromoCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup`	ADD COLUMN `strApplyDiscountOn` VARCHAR(20) NOT NULL DEFAULT 'SubTotal' AFTER `strPrintInclusiveOfAllTaxesOnBill`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbldebitcardtype`	ADD COLUMN `strCashCard` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strDataPostFlag`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbldebitcardmaster` ADD COLUMN `strReachrgeRemark` VARCHAR(50) NOT NULL DEFAULT '' AFTER `intCardNo`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbillhd` ADD COLUMN `strDiscountOn` VARCHAR(20) NOT NULL DEFAULT 'All' AFTER `strTakeAwayRemarks`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbillhd` ADD COLUMN `strDiscountOn` VARCHAR(20) NOT NULL DEFAULT 'All' AFTER `strTakeAwayRemarks`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbilldtl` ADD COLUMN `dblDiscountPer` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `dblDiscountAmt`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbilldtl` ADD COLUMN `dblDiscountPer` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `dblDiscountAmt`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbillhd` DROP PRIMARY KEY, ADD PRIMARY KEY (`strBillNo`, `strClientCode`);";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbillhd` DROP PRIMARY KEY, ADD PRIMARY KEY (`strBillNo`, `strClientCode`);";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbillsettlementdtl` DROP PRIMARY KEY, ADD PRIMARY KEY (`strBillNo`, `strSettlementCode`, `strClientCode`);";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbillsettlementdtl` DROP PRIMARY KEY, ADD PRIMARY KEY (`strBillNo`, `strSettlementCode`, `strClientCode`);";
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbilltaxdtl` DROP PRIMARY KEY,	ADD PRIMARY KEY (`strBillNo`, `strTaxCode`, `strClientCode`);";
	    i = ExecuteQuery(sql);

	    /*
             * sql="ALTER TABLE `tbladvancereceiptdtl` ADD PRIMARY KEY
             * (`strReceiptNo`, `strSettlementCode`, `strClientCode`);"; i =
             * ExecuteQuery(sql);
             *
             * sql="ALTER TABLE `tblqadvancereceiptdtl` ADD PRIMARY KEY
             * (`strReceiptNo`, `strSettlementCode`, `strClientCode`);"; i = ExecuteQuery(sql);
	     */
	    sql = "ALTER TABLE `tbladvancereceipthd` DROP PRIMARY KEY, ADD PRIMARY KEY (`strReceiptNo`, `strAdvBookingNo`, `strPOSCode`, `strClientCode`);";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqadvancereceipthd` DROP PRIMARY KEY, ADD PRIMARY KEY (`strReceiptNo`, `strAdvBookingNo`, `strPOSCode`, `strClientCode`);";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbladvbookbilldtl` ADD PRIMARY KEY (`strItemCode`, `strAdvBookingNo`, `strClientCode`);";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqadvbookbilldtl` ADD PRIMARY KEY (`strItemCode`, `strAdvBookingNo`, `strClientCode`);";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbladvbookbillhd` DROP PRIMARY KEY, ADD PRIMARY KEY (`strAdvBookingNo`, `strPOSCode`, `strClientCode`);";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqadvbookbillhd` DROP PRIMARY KEY, ADD PRIMARY KEY (`strAdvBookingNo`, `strPOSCode`, `strClientCode`);";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbladvordermodifierdtl` ALTER `strModifierCode` DROP DEFAULT;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbladvordermodifierdtl` CHANGE COLUMN `strModifierCode` `strModifierCode` VARCHAR(4) NOT NULL "
		    + "AFTER `strItemCode`, ADD PRIMARY KEY (`strAdvOrderNo`, `strItemCode`, `strModifierCode`, `strClientCode`);";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqadvordermodifierdtl` ALTER `strModifierCode` DROP DEFAULT;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqadvordermodifierdtl` CHANGE COLUMN `strModifierCode` `strModifierCode` VARCHAR(4) NOT NULL "
		    + "AFTER `strItemCode`, ADD PRIMARY KEY (`strAdvOrderNo`, `strItemCode`, `strModifierCode`, `strClientCode`);";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblimportexcel` ALTER `strPOSName` DROP DEFAULT;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblimportexcel` CHANGE COLUMN `strPOSName` `strPOSName` VARCHAR(50) NOT NULL AFTER `strMenuHeadCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblimportexcel` ALTER `strMenuHeadName` DROP DEFAULT;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblimportexcel` CHANGE COLUMN `strMenuHeadName` `strMenuHeadName` VARCHAR(50) NOT NULL AFTER `strShortName`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblimportexcel` ALTER `strSubGroupName` DROP DEFAULT, "
		    + "ALTER `strGroupName` DROP DEFAULT, ALTER `strCostCenterName` DROP DEFAULT;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblimportexcel` "
		    + "CHANGE COLUMN `strSubGroupName` `strSubGroupName` VARCHAR(50) NOT NULL AFTER `strPOSCode`, "
		    + "CHANGE COLUMN `strGroupName` `strGroupName` VARCHAR(50) NOT NULL AFTER `strSubMenuHeadCode`, "
		    + "CHANGE COLUMN `strCostCenterName` `strCostCenterName` VARCHAR(50) NOT NULL AFTER `strGroupCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup`	ADD COLUMN `strMemberCodeForKotInMposByCardSwipe` VARCHAR(20) NOT NULL DEFAULT 'N' AFTER `strApplyDiscountOn`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup`ADD COLUMN `strPrintBillYN` VARCHAR(1) NOT NULL DEFAULT 'Y' AFTER `strMemberCodeForKotInMposByCardSwipe`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblposmaster` "
		    + "	ADD COLUMN `strPrintVatNo` VARCHAR(1) NOT NULL DEFAULT 'Y' AFTER `strOperationalYN`, "
		    + "	ADD COLUMN `strPrintServiceTaxNo` VARCHAR(1) NOT NULL DEFAULT 'Y' AFTER `strPrintVatNo`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` ADD COLUMN `strVatAndServiceTaxFromPos` VARCHAR(1) NOT NULL DEFAULT 'Y' AFTER `strPrintBillYN`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblposmaster` "
		    + "     ADD COLUMN `strVatNo` VARCHAR(20) NOT NULL AFTER `strPrintServiceTaxNo`, "
		    + "     ADD COLUMN `strServiceTaxNo` VARCHAR(20) NOT NULL AFTER `strVatNo`;";
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

	    sql = "drop table if exists tblarrangetransaction";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblarrangetransaction` "
		    + "ADD COLUMN `strClientCode` VARCHAR(20) NULL DEFAULT NULL AFTER `strTransType`, "
		    + "ADD COLUMN `strPosCode` VARCHAR(20) NULL DEFAULT NULL AFTER `strClientCode`, "
		    + "ADD COLUMN `strPosFlag` VARCHAR(20) NULL DEFAULT 'N' AFTER `strPosCode`;";
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` ADD COLUMN `strMemberCodeForMakeBillInMPOS` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strVatAndServiceTaxFromPos`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblposmaster` "
		    + " CHANGE COLUMN `strPrintVatNo` `strPrintVatNo` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strOperationalYN`, "
		    + " CHANGE COLUMN `strPrintServiceTaxNo` `strPrintServiceTaxNo` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strPrintVatNo`, "
		    + " CHANGE COLUMN `strVatNo` `strVatNo` VARCHAR(20) NOT NULL DEFAULT '' AFTER `strPrintServiceTaxNo`, "
		    + " CHANGE COLUMN `strServiceTaxNo` `strServiceTaxNo` VARCHAR(20) NOT NULL DEFAULT '' AFTER `strVatNo`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbldebitcardmaster` ALTER `intCardNo` DROP DEFAULT;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbldebitcardmaster` CHANGE COLUMN `dblRedeemAmt` `dblRedeemAmt` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `strCardNo`, "
		    + "CHANGE COLUMN `intCardNo` `strCardString` VARCHAR(50) NOT NULL AFTER `strClientCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbldebitcardrecharge`	ALTER `strCardNo` DROP DEFAULT;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbldebitcardrecharge` "
		    + "CHANGE COLUMN `strCardNo` `strCardString` VARCHAR(50) NOT NULL AFTER `strCardTypeCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblpromotionmaster` ADD COLUMN `strPOSCode` VARCHAR(10) NOT NULL AFTER `strDataPostFlag`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblitemmaster` ADD COLUMN `strOpenItem` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strItemWeight`;";
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbldebitcardtype` "
		    + "ADD COLUMN `strAuthorizeMemberCard` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strCashCard`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbldebitcardrecharge` ADD COLUMN `strMemberCode` VARCHAR(50) NOT NULL DEFAULT '' AFTER `strClientCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbltempsalesflash1` "
		    + "	CHANGE COLUMN `strUser` `strUser` VARCHAR(100) NOT NULL DEFAULT '' AFTER `dbltipamt`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbldebitcardmaster` ADD COLUMN `strRefMemberCode` VARCHAR(50) NOT NULL DEFAULT '' AFTER `strReachrgeRemark`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` "
		    + " CHANGE COLUMN `strVatNo` `strVatNo` VARCHAR(50) NOT NULL AFTER `strPrintVatNo`,"
		    + " CHANGE COLUMN `strServiceTaxNo` `strServiceTaxNo` VARCHAR(50) NOT NULL AFTER `strPrintServiceTaxNo`;";
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbltempsalesflash1` ADD COLUMN `strReasonDesc` VARCHAR(500) NOT NULL DEFAULT '' AFTER `strdiscremarks`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbldebitcardrefundamt`	ADD COLUMN `strRefundSlipNo` VARCHAR(10) NOT NULL AFTER `strDataPostFlag`;";
	    i = ExecuteQuery(sql);

	    //sql="set global max_allowed_packet=32*1024*1024;";
	    //i = ExecuteQuery(sql);
	    sql = "ALTER TABLE `tblnonchargablekot` "
		    + "	CHANGE COLUMN `strUserCreated` `strUserCreated` VARCHAR(50) NOT NULL DEFAULT '' AFTER `dteNCKOTDate`, "
		    + "	CHANGE COLUMN `strUserEdited` `strUserEdited` VARCHAR(50) NOT NULL DEFAULT '' AFTER `strUserCreated`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblitemmodofier` "
		    + " ADD COLUMN `strDefaultModifier` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strApplicable`;";
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbltempsalesflash` "
		    + " CHANGE COLUMN `struser` `struser` VARCHAR(20) NOT NULL DEFAULT '' AFTER `strposcode`; ";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbldebitcardmaster` DROP COLUMN `intCardNo`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbldebitcardrecharge` "
		    + "ADD COLUMN `strRechargeSlipNo` VARCHAR(10) NOT NULL DEFAULT '' AFTER `strMemberCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbldebitcardrefundamt` ADD COLUMN `strPOSCode` VARCHAR(10) NOT NULL AFTER `strRefundSlipNo`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbldebitcardbilldetails`	ADD COLUMN `strTransactionType` VARCHAR(10) NOT NULL AFTER `dteBillDate`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbillmodifierdtl` "
		    + "ADD COLUMN `strDefaultModifierDeselectedYN` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strMMSDataPostFlag`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbillmodifierdtl` "
		    + "ADD COLUMN `strDefaultModifierDeselectedYN` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strMMSDataPostFlag`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbldebitcardrecharge` ADD COLUMN `strCardNo` VARCHAR(10) NOT NULL AFTER `strRechargeSlipNo`;";
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbldcrechargesettlementdtl` ADD COLUMN `strCardNo` VARCHAR(10) NOT NULL AFTER `dblRechargeAmt`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbldcrechargesettlementdtl` ADD COLUMN `strType` VARCHAR(20) NOT NULL AFTER `strCardNo`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsubgrouphd` ADD COLUMN `strAccountCode` VARCHAR(10) NOT NULL AFTER `strIncentives`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsubgrouphd`	CHANGE COLUMN `strAccountCode` `strAccountCode` VARCHAR(10) NOT NULL DEFAULT 'NA' AFTER `strIncentives`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsettelmenthd`	ADD COLUMN `strAccountCode` VARCHAR(10) NOT NULL AFTER `strDataPostFlag`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbltaxhd`	ADD COLUMN `strAccountCode` VARCHAR(10) NOT NULL AFTER `strDataPostFlag`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblposmaster` ADD COLUMN `strRoundOff` VARCHAR(20) NOT NULL DEFAULT '' AFTER `strServiceTaxNo`, "
		    + "ADD COLUMN `strTip` VARCHAR(20) NOT NULL DEFAULT '' AFTER `strRoundOff`, "
		    + "ADD COLUMN `strDiscount` VARCHAR(20) NOT NULL DEFAULT '' AFTER `strTip`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbilldiscdtl`	ADD COLUMN `strClientCode` VARCHAR(10) NOT NULL AFTER `dteDateEdited`,"
		    + "	ADD COLUMN `strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strClientCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbilldiscdtl`	ADD COLUMN `strClientCode` VARCHAR(10) NOT NULL AFTER `dteDateEdited`,"
		    + "	ADD COLUMN `strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strClientCode`;";
	    i = ExecuteQuery(sql);

	    sql = " ALTER TABLE `tblitemmaster` "
		    + " ADD COLUMN `strItemWiseKOTYN` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strOpenItem`;";
	    i = ExecuteQuery(sql);

	    sql = " ALTER TABLE `tblsetup` "
		    + " ADD COLUMN `strItemWiseKOTYN` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strMemberCodeForMakeBillInMPOS`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbltruncationdtl`	ADD COLUMN `dteFromDate` DATETIME NOT NULL AFTER `strModuleType`"
		    + ", ADD COLUMN `dteToDate` DATETIME NOT NULL AFTER `dteFromDate`"
		    + ", ADD COLUMN `strPOSCode` VARCHAR(10) NOT NULL AFTER `dteToDate`"
		    + ", ADD COLUMN `strClientCode` VARCHAR(10) NOT NULL AFTER `strPOSCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbltempadvorderflash` ADD COLUMN `strManualAdvOrderNo` VARCHAR(100) NOT NULL AFTER `strClientCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblhomedelivery`	DROP PRIMARY KEY, ADD PRIMARY KEY (`strBillNo`, `strClientCode`);";
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

	    sql = " ALTER TABLE `tblimportexcel` ADD COLUMN `strRevenueHead` VARCHAR(20) NOT NULL DEFAULT '' "
		    + "AFTER `strMenuHeadCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblitemrtemp` ADD COLUMN `strCardType` VARCHAR(50) NOT NULL DEFAULT '' AFTER `intId`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbillhd` ADD COLUMN `strCardNo` VARCHAR(50) NOT NULL DEFAULT '' AFTER `strDiscountOn`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbillhd` ADD COLUMN `strCardNo` VARCHAR(50) NOT NULL DEFAULT '' AFTER `strDiscountOn`;";
	    i = ExecuteQuery(sql);

	    sql = " ALTER TABLE `tblitemmodofier` "
		    + " CHANGE COLUMN `strDefaultModifier` `strDefaultModifier` VARCHAR(5) NOT NULL DEFAULT 'N' AFTER `strApplicable`;";
	    i = ExecuteQuery(sql);

	    sql = "CREATE TABLE IF NOT EXISTS `tblbuypromotiondtl` ("
		    + "	`strPromoCode` VARCHAR(10) NOT NULL,"
		    + "	`strBuyPromoItemCode` VARCHAR(10) NOT NULL,"
		    + "	`dblBuyItemQty` DECIMAL(18,4) NOT NULL,"
		    + "	`strOperator` VARCHAR(3) NOT NULL,"
		    + "	`strClientCode` VARCHAR(10) NOT NULL,"
		    + "	`strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N') "
		    + "COLLATE='utf8_general_ci' "
		    + "ENGINE=InnoDB;";
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblpromotionmaster` ADD COLUMN `strGetItemCode` VARCHAR(10) NOT NULL AFTER `strPOSCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblpromotionmaster` ADD COLUMN `strGetPromoOn` VARCHAR(10) NOT NULL AFTER `strGetItemCode`;";
	    i = ExecuteQuery(sql);

	    sql = "CREATE TABLE IF NOT EXISTS `tbldebitcardrevenue` (`strCardNo` VARCHAR(50) NOT NULL"
		    + " ,`dblCardAmt` DECIMAL(18,4) NOT NULL"
		    + " ,`strPOSCode` VARCHAR(10) NOT NULL"
		    + " ,`dtePOSDate` DATETIME NOT NULL"
		    + " ,`dteDate` DATETIME NOT NULL"
		    + " ,`strClientCode` VARCHAR(10) NOT NULL"
		    + " ,`strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N')"
		    + "  COLLATE='utf8_general_ci'  ENGINE=InnoDB;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup`	ADD COLUMN `strLastPOSForDayEnd` VARCHAR(10) NOT NULL AFTER `strItemWiseKOTYN`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbldebitcardrevenue` ADD COLUMN `strUserCreated` VARCHAR(10) NOT NULL AFTER `strDataPostFlag`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` ADD COLUMN `strCMSPostingType` VARCHAR(20) NOT NULL AFTER `strLastPOSForDayEnd`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbillpromotiondtl` ADD COLUMN `strPromoType` VARCHAR(20) NOT NULL AFTER `strDataPostFlag`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbillpromotiondtl` ADD COLUMN `dblAmount` DECIMAL(18,4) NOT NULL AFTER `strPromoType`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbltdhcomboitemdtl` ADD COLUMN `strSubItemMenuCode` VARCHAR(15) NOT NULL AFTER `strDefaultYN`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup`	ADD COLUMN `strPopUpToApplyPromotionsOnBill` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strCMSPostingType`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblimportexcel` CHANGE COLUMN `strRevenueHead` `strRevenueHead` VARCHAR(50) NOT NULL DEFAULT '' AFTER `strMenuHeadCode`;";
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbilldtl` "
		    + "ADD COLUMN `strSequenceNo` VARCHAR(50) NOT NULL DEFAULT '0.00' AFTER `dblDiscountPer`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbilldtl` "
		    + "ADD COLUMN `strSequenceNo` VARCHAR(10) NOT NULL DEFAULT '0.00' AFTER `dblDiscountPer`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbillmodifierdtl` "
		    + "ADD COLUMN `strSequenceNo` VARCHAR(50) NOT NULL DEFAULT '0.00' AFTER `strDefaultModifierDeselectedYN`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbillmodifierdtl` "
		    + "ADD COLUMN `strSequenceNo` VARCHAR(10) NOT NULL DEFAULT '0.00' AFTER `strDefaultModifierDeselectedYN`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbillcomplementrydtl` "
		    + "ADD COLUMN `strSequenceNo` VARCHAR(10) NOT NULL DEFAULT '0.00' AFTER `dblDiscountPer`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbillcomplementrydtl` "
		    + "ADD COLUMN `strSequenceNo` VARCHAR(10) NOT NULL DEFAULT '0.00' AFTER `dblDiscountPer`;";
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblorderanalysis`	ADD COLUMN `strField14` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strField13`;";
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

	    sql = " ALTER TABLE `tblsetup` ADD COLUMN `strSelectCustomerCodeFromCardSwipe` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strPopUpToApplyPromotionsOnBill`; ";
	    i = ExecuteQuery(sql);

	    sql = " ALTER TABLE `tblitemmaster` ADD COLUMN `strWSProdCode` VARCHAR(10) NOT NULL DEFAULT ' NA' AFTER `strItemWiseKOTYN`; ";
	    i = ExecuteQuery(sql);

	    sql = " ALTER TABLE `tblitemmaster` ADD COLUMN `strExciseBrandCode` VARCHAR(10) NOT NULL DEFAULT '' AFTER `strWSProdCode`; ";
	    i = ExecuteQuery(sql);

	    sql = " ALTER TABLE `tblposmaster` ADD COLUMN `strWSLocationCode` VARCHAR(10) NOT NULL DEFAULT '' AFTER `strDiscount`; ";
	    i = ExecuteQuery(sql);

	    sql = " ALTER TABLE `tblposmaster` ADD COLUMN `strExciseLicenceCode` VARCHAR(10) NOT NULL DEFAULT '' AFTER `strWSLocationCode`; ";
	    i = ExecuteQuery(sql);

	    sql = "CREATE TABLE IF NOT EXISTS `tblitemmasterlinkupdtl` ( "
		    + "  `strItemCode` varchar(20) NOT NULL, "
		    + "  `strPOSCode` varchar(20) NOT NULL, "
		    + "  `strWSProductCode` varchar(20) NOT NULL, "
		    + "  `strClientCode` varchar(20) NOT NULL, "
		    + "  `strDataPostFlag` varchar(1) NOT NULL "
		    + ") ENGINE=InnoDB DEFAULT CHARSET=latin1;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbillpromotiondtl` "
		    + "	ALTER `dblQuantity` DROP DEFAULT;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbillpromotiondtl` "
		    + "CHANGE COLUMN `dblQuantity` `dblQuantity` DECIMAL(18,2) NOT NULL AFTER `strPromotionCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbillpromotiondtl` "
		    + "	ALTER `dblQuantity` DROP DEFAULT;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbillpromotiondtl` "
		    + "CHANGE COLUMN `dblQuantity` `dblQuantity` DECIMAL(18,2) NOT NULL AFTER `strPromotionCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbldcrechargesettlementdtl` "
		    + "DROP PRIMARY KEY, ADD PRIMARY KEY (`strRechargeNo`, `strClientCode`, `strSettlementCode`);";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup`	ADD COLUMN `strCheckDebitCardBalOnTransactions` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strSelectCustomerCodeFromCardSwipe`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblitemrtemp` "
		    + "ADD COLUMN `dblTaxAmt` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `strCardType`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbldayendprocess`	ADD COLUMN `dblUsedDebitCardBalance` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `intNoOfVoidKOT`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbldayendprocess`	ADD COLUMN `dblUnusedDebitCardBalance` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `dblUsedDebitCardBalance`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbldayendprocess` ADD COLUMN `strWSStockAdjustmentNo` VARCHAR(20) NOT NULL DEFAULT '' AFTER `dblUnusedDebitCardBalance`;";
	    i = ExecuteQuery(sql);

	    sql = "CREATE TABLE IF NOT EXISTS `tblkottaxdtl` ( "
		    + "	`strTableNo` VARCHAR(10) NOT NULL, "
		    + "	`strKOTNo` VARCHAR(10) NOT NULL, "
		    + "	`dblAmount` DECIMAL(18,2) NOT NULL, "
		    + "	`dblTaxAmt` DECIMAL(18,2) NOT NULL "
		    + ") "
		    + "COLLATE='utf8_general_ci' "
		    + "ENGINE=InnoDB "
		    + ";";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblmenuitempricingdtl` "
		    + "CHANGE COLUMN `longPricingId` `longPricingId` BIGINT(20) NOT NULL DEFAULT '0' AFTER `strHourlyPricing`, "
		    + "DROP PRIMARY KEY;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblhomedeldtl` ADD COLUMN `dblDBIncentives` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `strSettleYN`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbillmodifierdtl` "
		    + "ADD COLUMN `dblDiscPer` DECIMAL(18,2) NOT NULL AFTER `strSequenceNo`, "
		    + "ADD COLUMN `dblDiscAmt` DECIMAL(18,2) NOT NULL AFTER `dblDiscPer`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbillmodifierdtl` "
		    + "ADD COLUMN `dblDiscPer` DECIMAL(18,2) NOT NULL AFTER `strSequenceNo`, "
		    + "ADD COLUMN `dblDiscAmt` DECIMAL(18,2) NOT NULL AFTER `dblDiscPer`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup`	ADD COLUMN `strSettlementsFromPOSMaster` VARCHAR(1) NOT NULL DEFAULT "
		    + " 'N' AFTER `strCheckDebitCardBalOnTransactions`;";
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblplaceorderhd`	ADD PRIMARY KEY (`strSOCode`, `strClientCode`);";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblplaceorderhd`	ADD COLUMN `strDCCode` VARCHAR(20) NOT NULL AFTER `strCloseSO`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbldebitcardrecharge` ADD COLUMN `strTransferBalance` VARCHAR(1) NOT NULL AFTER `strCardNo`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbldebitcardrecharge`"
		    + "	CHANGE COLUMN `strTransferBalance` `strTransferBalance` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strCardNo`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblplaceorderhd`"
		    + "	ADD COLUMN `strOrderCode` VARCHAR(20) NOT NULL FIRST,"
		    + "	DROP PRIMARY KEY,"
		    + "	ADD PRIMARY KEY (`strOrderCode`, `strClientCode`);";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblplaceorderdtl`"
		    + "	ADD COLUMN `strOrderCode` VARCHAR(20) NOT NULL FIRST,"
		    + "	DROP COLUMN `strSOCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblplaceorderhd` ALTER `dteFulmtDate` DROP DEFAULT;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblplaceorderhd`	CHANGE COLUMN `dteFulmtDate` `dteOrderDate` DATETIME "
		    + " NOT NULL AFTER `dteSODate`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsettelmenthd`	ADD COLUMN `strBillPrintOnSettlement` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strAccountCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblorderanalysis` ADD COLUMN `strField15` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strField14`";
	    i = ExecuteQuery(sql);

	    //Queries for Old Complimentary Settlement bills
	    sql = "update tblbillhd a,tblbilldtl b,tblbillsettlementdtl c,tblsettelmenthd d "
		    + "set a.dblSubTotal=0.00,a.dblGrandTotal=0.00,a.dblDiscountAmt=0.00,a.dblDiscountPer=0.00 "
		    + ",a.dblTaxAmt=0.00,b.dblAmount=0.00,b.dblTaxAmount=0.00,b.dblDiscountAmt=0.00,b.dblDiscountPer=0.00 "
		    + ",c.dblSettlementAmt=0.00,c.dblPaidAmt=0.00 "
		    + "where a.strBillNo=b.strBillNo "
		    + "and a.strBillNo=c.strBillNo "
		    + "and c.strSettlementCode=d.strSettelmentCode "
		    + "and d.strSettelmentType='Complementary';";
	    i = ExecuteQuery(sql);

	    sql = "update tblbillmodifierdtl a,tblbillsettlementdtl b,tblsettelmenthd c "
		    + "set a.dblAmount=0.00 "
		    + "where a.strBillNo=b.strBillNo "
		    + "and b.strSettlementCode=c.strSettelmentCode "
		    + "and c.strSettelmentType='Complementary';";
	    i = ExecuteQuery(sql);

	    sql = "update tblbilltaxdtl a,tblbillsettlementdtl b,tblsettelmenthd c "
		    + "set a.dblTaxableAmount=0.00,a.dblTaxAmount=0.00 "
		    + "where a.strBillNo=b.strBillNo "
		    + "and b.strSettlementCode=c.strSettelmentCode "
		    + "and c.strSettelmentType='Complementary';";
	    i = ExecuteQuery(sql);

	    sql = "update tblqbillhd a,tblqbilldtl b,tblqbillsettlementdtl c,tblsettelmenthd d "
		    + "set a.dblSubTotal=0.00,a.dblGrandTotal=0.00,a.dblDiscountAmt=0.00,a.dblDiscountPer=0.00 "
		    + ",a.dblTaxAmt=0.00,b.dblAmount=0.00,b.dblTaxAmount=0.00,b.dblDiscountAmt=0.00,b.dblDiscountPer=0.00 "
		    + ",c.dblSettlementAmt=0.00,c.dblPaidAmt=0.00 "
		    + "where a.strBillNo=b.strBillNo "
		    + "and a.strBillNo=c.strBillNo "
		    + "and c.strSettlementCode=d.strSettelmentCode "
		    + "and d.strSettelmentType='Complementary';";
	    i = ExecuteQuery(sql);

	    sql = "update tblqbillmodifierdtl a,tblqbillsettlementdtl b,tblsettelmenthd c "
		    + "set a.dblAmount=0.00 "
		    + "where a.strBillNo=b.strBillNo "
		    + "and b.strSettlementCode=c.strSettelmentCode "
		    + "and c.strSettelmentType='Complementary';";
	    i = ExecuteQuery(sql);

	    sql = "update tblqbilltaxdtl a,tblqbillsettlementdtl b,tblsettelmenthd c "
		    + "set a.dblTaxableAmount=0.00,a.dblTaxAmount=0.00 "
		    + "where a.strBillNo=b.strBillNo "
		    + "and b.strSettlementCode=c.strSettelmentCode "
		    + "and c.strSettelmentType='Complementary';";
	    i = ExecuteQuery(sql);

	    sql = "delete from tblbilldiscdtl  "
		    + "where strBillNo IN (select strBillNo from tblbillhd  where dblGrandTotal=0);";
	    i = ExecuteQuery(sql);

	    sql = "delete from tblqbilldiscdtl  "
		    + "where strBillNo IN (select strBillNo from tblqbillhd  where dblGrandTotal=0);";
	    i = ExecuteQuery(sql);

	    //end Queries for Old Complimentary Settlement bills
	    sql = "update tblbillmodifierdtl set strItemCode=CONCAT(strItemCode,strModifierCode)   where LENGTH(strItemCode)=7 ; ";
	    i = ExecuteQuery(sql);

	    sql = "update tblqbillmodifierdtl set strItemCode=CONCAT(strItemCode,strModifierCode)   where LENGTH(strItemCode)=7 ; ";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup`ADD COLUMN `strShiftWiseDayEndYN` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strSettlementsFromPOSMaster` ";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` ADD COLUMN `strProductionLinkup` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strShiftWiseDayEndYN`; ";
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbillseries` ADD COLUMN `strPOSCode` VARCHAR(15) NOT NULL FIRST, "
		    + "ADD COLUMN `intLastNo` BIGINT(20) NOT NULL AFTER `strBillSeries`";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblwaitermaster` 	ADD COLUMN `strPOSCode` VARCHAR(10) NOT NULL DEFAULT 'All' AFTER `strDataPostFlag`; ";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblwaitermaster` 	ADD COLUMN `strPOSCode` VARCHAR(10) NOT NULL DEFAULT 'All' AFTER `strDataPostFlag`; ";
	    i = ExecuteQuery(sql);

	    sql = " ALTER TABLE `tblitemmaster` "
		    + " ADD COLUMN `strNoDeliveryDays` VARCHAR(200) NOT NULL DEFAULT '' AFTER `strExciseBrandCode`, "
		    + " ADD COLUMN `intDeliveryDays` INT(10) NOT NULL DEFAULT '0' AFTER `strNoDeliveryDays`; ";
	    i = ExecuteQuery(sql);

	    sql = " ALTER TABLE `tblitemmaster` "
		    + " ADD COLUMN `strIncrementalWeight` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `intDeliveryDays`, "
		    + " ADD COLUMN `strMinWeight` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `strIncrementalWeight`; ";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblitemmaster` CHANGE COLUMN `strIncrementalWeight` `dblIncrementalWeight` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `intDeliveryDays`,"
		    + " CHANGE COLUMN `strMinWeight` `dblMinWeight` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `dblIncrementalWeight`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbltaxhd`	ADD COLUMN `strTaxShortName` VARCHAR(20) NOT NULL DEFAULT '' AFTER `strAccountCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblposmaster` ADD COLUMN `strEnableShift` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strExciseLicenceCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblitemmaster`	DROP COLUMN `strIncrementalWeight`,	DROP COLUMN `strMinWeight`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` ADD COLUMN `strLockDataOnShift` VARCHAR(1) NOT NULL DEFAULT 'Y' AFTER `strProductionLinkup`;";
	    i = ExecuteQuery(sql);

	    sql = "drop table IF EXISTS tbldayendreport;";
	    i = ExecuteQuery(sql);

	    sql = " ALTER TABLE `tbladvbookbilldtl` "
		    + " ADD COLUMN `dblWeight` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `dblQuantity`; ";
	    i = ExecuteQuery(sql);

	    sql = " ALTER TABLE `tbladvbookitemtemp` "
		    + " ADD COLUMN `dblWeight` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `dblItemQuantity`; ";
	    i = ExecuteQuery(sql);

	    sql = " ALTER TABLE `tblqadvbookbilldtl` "
		    + " ADD COLUMN `dblWeight` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `dblQuantity` ";
	    i = ExecuteQuery(sql);

	    sql = " ALTER TABLE `tblitemmasterlinkupdtl` "
		    + " ADD COLUMN `strWSProductName` VARCHAR(200) NOT NULL AFTER `strWSProductCode`; ";
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

	    sql = " CREATE TABLE IF NOT EXISTS `tblitemorderingdtl` (  "
		    + " `strItemCode` VARCHAR(20) NOT NULL, "
		    + " `strPOSCode` VARCHAR(20) NOT NULL, "
		    + " `strOrderCode` VARCHAR(20) NOT NULL, "
		    + " `strClientCode` VARCHAR(20) NOT NULL, "
		    + " `strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' "
		    + " )COLLATE='utf8_general_ci' "
		    + " ENGINE=InnoDB; ";
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

	    sql = " ALTER TABLE `tblplaceorderhd` "
		    + " ADD COLUMN `strOrderTypeCode` VARCHAR(20) NOT NULL DEFAULT '' AFTER `strDCCode`; ";
	    i = ExecuteQuery(sql);

	    sql = " ALTER TABLE `tblplaceorderhd` "
		    + " ADD COLUMN `strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strOrderTypeCode`;";
	    i = ExecuteQuery(sql);

	    sql = " ALTER TABLE `tblplaceorderdtl` "
		    + " ADD COLUMN `strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strClientCode`;";
	    i = ExecuteQuery(sql);

	    sql = " ALTER TABLE `tblsetup` "
		    + " ADD COLUMN `strWSClientCode` VARCHAR(11) NOT NULL DEFAULT '' AFTER `strLockDataOnShift`; ";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` "
		    + "ADD COLUMN `strPOSCode` VARCHAR(10) NOT NULL DEFAULT 'All' AFTER `strWSClientCode`; ";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` "
		    + "DROP PRIMARY KEY, "
		    + "ADD PRIMARY KEY (`strClientCode`, `strPOSCode`);";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` "
		    + "CHANGE COLUMN `strShowBillsDtlType` `strShowBillsDtlType` VARCHAR(30) NOT NULL AFTER `strAllowToCalculateItemWeight`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` "
		    + "ADD COLUMN `strEnableBillSeries` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strPOSCode`;";
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` "
		    + "ADD COLUMN `strEnablePMSIntegrationYN` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strEnableBillSeries`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbillsettlementdtl` "
		    + "ADD COLUMN `strFolioNo` VARCHAR(10) NOT NULL DEFAULT '' AFTER `strDataPostFlag`,"
		    + "ADD COLUMN `strRoomNo` VARCHAR(10) NOT NULL DEFAULT '' AFTER `strFolioNo`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` "
		    + "ADD COLUMN `strEnablePMSIntegrationYN` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strEnableBillSeries`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbillsettlementdtl` ADD COLUMN `strFolioNo` VARCHAR(10) NOT NULL DEFAULT '' AFTER `strDataPostFlag`"
		    + ",ADD COLUMN `strRoomNo` VARCHAR(10) NOT NULL DEFAULT '' AFTER `strFolioNo`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbillsettlementdtl` ADD COLUMN `strFolioNo` VARCHAR(10) NOT NULL DEFAULT '' AFTER `strDataPostFlag`"
		    + ",ADD COLUMN `strRoomNo` VARCHAR(10) NOT NULL DEFAULT '' AFTER `strFolioNo`;";
	    i = ExecuteQuery(sql);

	    sql = " ALTER TABLE `tblitemmaster`ADD COLUMN `strUrgentOrder` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `dblMinWeight`; ";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblreasonmaster` "
		    + "ADD COLUMN `strReprint` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strVoidAdvOrder`;";
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblplaceorderhd`	ADD COLUMN `strOrderType` VARCHAR(15) NOT NULL DEFAULT 'Normal' AFTER `strDataPostFlag`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup`"
		    + "ADD COLUMN `strPrintTimeOnBill` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strEnablePMSIntegrationYN`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` "
		    + "CHANGE COLUMN `strBillFooter` `strBillFooter` VARCHAR(2000) NOT NULL AFTER `strEmail`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup`	ADD COLUMN `strPrintTDHItemsInBill` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strPrintTimeOnBill`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblplaceorderhd`	ADD COLUMN `strAdvOrderNo` VARCHAR(20) NOT NULL DEFAULT '' AFTER `strOrderType`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbladvbookitemtemp` ADD COLUMN `dblPrice` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `dblWeight`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbladvbookbillhd`	ADD COLUMN `strUrgentOrder` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strSpecialsymbolImage`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqadvbookbillhd` ADD COLUMN `strUrgentOrder` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strSpecialsymbolImage`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblplaceorderhd`	DROP COLUMN `strAdvOrderNo`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblplaceorderdtl`	ADD COLUMN `strAdvOrderNo` VARCHAR(20) NOT NULL DEFAULT '' AFTER `strDataPostFlag`;";
	    i = ExecuteQuery(sql);

	    sql = "CREATE TABLE if not exists `tblplaceorderadvorderdtl` ( "
		    + "	`strAdvOrderNo` VARCHAR(20) NOT NULL, "
		    + "	`dteOrderDate` DATETIME NOT NULL, "
		    + "	`strClientCode` VARCHAR(10) NOT NULL, "
		    + "	`strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' "
		    + ") "
		    + "COLLATE='utf8_general_ci' "
		    + "ENGINE=InnoDB "
		    + ";";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblplaceorderadvorderdtl` ADD COLUMN `strOrderType` VARCHAR(20) NOT NULL AFTER `strDataPostFlag`;";
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbillseries` "
		    + "ADD COLUMN `strPrintGTOfOtherBills` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strPropertyCode`; ";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqadvbookbilldtl` CHANGE COLUMN `dblWeight` `dblWeight` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `strCustomerCode`;";
	    i = ExecuteQuery(sql);

	    sql = "CREATE TABLE IF NOT EXISTS `tbladvbookbillimgdtl` ( "
		    + "`strItemCode` VARCHAR(15) NOT NULL, "
		    + "`strAdvBookingNo` VARCHAR(20) NOT NULL, "
		    + "`blobCakeImage` LONGBLOB NOT NULL, "
		    + "`strClientCode` VARCHAR(11) NOT NULL, "
		    + "`strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N', "
		    + "PRIMARY KEY (`strItemCode`,`strAdvBookingNo`,`strClientCode`) "
		    + ")COLLATE='utf8_general_ci' "
		    + "ENGINE=InnoDB; ";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblcounterhd` ADD COLUMN `strUserCode` VARCHAR(20) NOT NULL DEFAULT '' AFTER `strOperational`;";
	    i = ExecuteQuery(sql);

	    sql = "update tblgrouphd set strGroupCode=CONCAT('G','0',right(strGroupCode,6)) "
		    + " where length(strGroupCode)<8";
	    i = ExecuteQuery(sql);

	    sql = "update tblsubgrouphd set strSubGroupCode=CONCAT('SG','0',right(strSubGroupCode,6)) "
		    + " where length(strSubGroupCode)<9";
	    i = ExecuteQuery(sql);

	    sql = " ALTER TABLE `tblsetup` "
		    + " ADD COLUMN `strPrintRemarkAndReasonForReprint` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strPrintTDHItemsInBill`; ";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbldayendprocess` "
		    + "ADD COLUMN `dblTipAmt` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `strWSStockAdjustmentNo`; ";
	    i = ExecuteQuery(sql);

	    sql = "update tblitemmaster set strSubGroupCode=CONCAT('SG','0',right(strSubGroupCode,6)) "
		    + " where length(strSubGroupCode)<9";
	    i = ExecuteQuery(sql);

	    sql = "update tblsubgrouphd set strGroupCode=CONCAT('G','0',right(strGroupCode,6)) "
		    + " where length(strGroupCode)<8;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblstkinhd` ADD COLUMN `strInvoiceCode` VARCHAR(20) NOT NULL DEFAULT '' AFTER `strDataPostFlag`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblcashmanagement` ADD COLUMN `strAgainst` VARCHAR(20) NOT NULL DEFAULT 'Direct' AFTER `intShiftCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblcashmanagement` ADD COLUMN `dblRollingAmt` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `strAgainst`;";
	    i = ExecuteQuery(sql);

	    sql = " ALTER TABLE `tblgrouphd` "
		    + " ADD COLUMN `strOperationalYN` VARCHAR(1) NOT NULL DEFAULT 'Y' AFTER `strDataPostFlag`; ";
	    i = ExecuteQuery(sql);

	    sql = " ALTER TABLE `tblpsphd` "
		    + " ADD COLUMN `strReasonCode` VARCHAR(10) NOT NULL AFTER `strDataPostFlag`, "
		    + " ADD COLUMN `strRemarks` VARCHAR(50) NOT NULL AFTER `strReasonCode`; ";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup`	ADD COLUMN `intDaysBeforeOrderToCancel` INT NOT NULL DEFAULT '0' AFTER `strPrintRemarkAndReasonForReprint`;";
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

	    sql = "CREATE TABLE IF NOT EXISTS `tblvoidadvbookbillchardtl` ( "
		    + "  `strItemCode` varchar(15) NOT NULL, "
		    + "  `strAdvBookingNo` varchar(20) NOT NULL, "
		    + "  `strCharCode` varchar(15) NOT NULL, "
		    + "  `strCharValues` varchar(200) NOT NULL, "
		    + "  `strClientCode` varchar(11) NOT NULL, "
		    + "  `strDataPostFlag` varchar(1) NOT NULL DEFAULT 'N', "
		    + "  PRIMARY KEY (`strItemCode`,`strAdvBookingNo`,`strCharCode`,`strClientCode`) "
		    + ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblvoidadvbookbillhd` "
		    + "	ADD COLUMN `strReasonCode` VARCHAR(10) NOT NULL AFTER `strUrgentOrder`, "
		    + "	ADD COLUMN `strRemark` VARCHAR(100) NOT NULL AFTER `strReasonCode`;";
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` "
		    + "	ADD COLUMN `intNoOfDelDaysForAdvOrder` INT(11) NOT NULL DEFAULT '0' AFTER `intDaysBeforeOrderToCancel`, "
		    + "	ADD COLUMN `intNoOfDelDaysForUrgentOrder` INT(11) NOT NULL DEFAULT '0' AFTER `intNoOfDelDaysForAdvOrder`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` "
		    + "	ADD COLUMN `strSetUpToTimeForAdvOrder` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `intNoOfDelDaysForUrgentOrder`, "
		    + "	ADD COLUMN `strSetUpToTimeForUrgentOrder` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strSetUpToTimeForAdvOrder`, "
		    + "	ADD COLUMN `strUpToTimeForAdvOrder` VARCHAR(20) NOT NULL DEFAULT '00:00 AM' AFTER `strSetUpToTimeForUrgentOrder`, "
		    + "	ADD COLUMN `strUpToTimeForUrgentOrder` VARCHAR(20) NOT NULL DEFAULT '00:00 AM' AFTER `strUpToTimeForAdvOrder`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup`	ADD COLUMN `strEnableBothPrintAndSettleBtnForDB` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strUpToTimeForUrgentOrder`;";
	    i = ExecuteQuery(sql);

	    sql = "CREATE TABLE IF NOT EXISTS `tblqadvbookbillchardtl` ( "
		    + "  `strItemCode` varchar(15) NOT NULL, "
		    + "  `strAdvBookingNo` varchar(20) NOT NULL, "
		    + "  `strCharCode` varchar(15) NOT NULL, "
		    + "  `strCharValues` varchar(200) NOT NULL, "
		    + "  `strClientCode` varchar(11) NOT NULL, "
		    + "  `strDataPostFlag` varchar(1) NOT NULL DEFAULT 'N', "
		    + "  PRIMARY KEY (`strItemCode`,`strAdvBookingNo`,`strCharCode`,`strClientCode`) "
		    + ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbltablemaster` ALTER `strTableNo` DROP DEFAULT;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbltablemaster` CHANGE COLUMN `strTableNo` `strTableNo` VARCHAR(10) NOT NULL FIRST;";
	    i = ExecuteQuery(sql);

	    sql = "update tbltablemaster set strTableNo=CONCAT('TB','0000',right(strTableNo,3)) "
		    + " where length(strTableNo)<9 and length(strTableNo) > 0;";
	    i = ExecuteQuery(sql);

	    sql = "update tblbillhd set strTableNo=CONCAT('TB','0000',right(strTableNo,3)) "
		    + " where length(strTableNo)<9 and length(strTableNo) > 0;";
	    i = ExecuteQuery(sql);

	    sql = "update tblqbillhd set strTableNo=CONCAT('TB','0000',right(strTableNo,3)) "
		    + " where length(strTableNo)<9 and length(strTableNo) > 0;";
	    i = ExecuteQuery(sql);

	    sql = "update tblitemrtemp set strTableNo=CONCAT('TB','0000',right(strTableNo,3)) "
		    + " where length(strTableNo)<9 and length(strTableNo) > 0;";
	    i = ExecuteQuery(sql);

	    sql = "update tblnonchargablekot set strTableNo=CONCAT('TB','0000',right(strTableNo,3)) "
		    + " where length(strTableNo)<9 and length(strTableNo) > 0;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup`"
		    + "ADD COLUMN `strInrestoPOSIntegrationYN` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strEnableBothPrintAndSettleBtnForDB`,"
		    + "ADD COLUMN `strInrestoPOSWebServiceURL` VARCHAR(255) NOT NULL DEFAULT '' AFTER `strInrestoPOSIntegrationYN`,"
		    + "ADD COLUMN `strInrestoPOSId` VARCHAR(50) NOT NULL DEFAULT '' AFTER `strInrestoPOSWebServiceURL`,"
		    + "ADD COLUMN `strInrestoPOSKey` VARCHAR(50) NOT NULL DEFAULT '' AFTER `strInrestoPOSId`;";
	    i = ExecuteQuery(sql);

	    //customer code updation
	    sql = "ALTER TABLE `tblcustomermaster` "
		    + "	CHANGE COLUMN `strCustomerCode` `strCustomerCode` VARCHAR(15) NOT NULL FIRST;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbillhd` "
		    + "	CHANGE COLUMN `strCustomerCode` `strCustomerCode` VARCHAR(15);";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbillhd` "
		    + "	CHANGE COLUMN `strCustomerCode` `strCustomerCode` VARCHAR(15);";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbilldtl` "
		    + "	CHANGE COLUMN `strCustomerCode` `strCustomerCode` VARCHAR(15);";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbilldtl` "
		    + "	CHANGE COLUMN `strCustomerCode` `strCustomerCode` VARCHAR(15);";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbillsettlementdtl` "
		    + "	CHANGE COLUMN `strCustomerCode` `strCustomerCode` VARCHAR(15);";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbillsettlementdtl` "
		    + "	CHANGE COLUMN `strCustomerCode` `strCustomerCode` VARCHAR(15);";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbladvbookbillhd` "
		    + "	CHANGE COLUMN `strCustomerCode` `strCustomerCode` VARCHAR(15);";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqadvbookbillhd` "
		    + "	CHANGE COLUMN `strCustomerCode` `strCustomerCode` VARCHAR(15);";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbladvbookbilldtl` "
		    + "	CHANGE COLUMN `strCustomerCode` `strCustomerCode` VARCHAR(15);";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqadvbookbilldtl` "
		    + "	CHANGE COLUMN `strCustomerCode` `strCustomerCode` VARCHAR(15);";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbldebitcardmaster` "
		    + "	CHANGE COLUMN `strCustomerCode` `strCustomerCode` VARCHAR(15);";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblhomedelivery` "
		    + "	CHANGE COLUMN `strCustomerCode` `strCustomerCode` VARCHAR(15);";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblreservation` "
		    + "	CHANGE COLUMN `strCustomerCode` `strCustomerCode` VARCHAR(15);";
	    i = ExecuteQuery(sql);

	    sql = "update tblcustomermaster a "
		    + "set a.strCustomerCode=CONCAT(right(a.strClientCode,3),a.strCustomerCode) "
		    + "where LENGTH(a.strCustomerCode)=8;";
	    i = ExecuteQuery(sql);

	    sql = "update tblbillhd a "
		    + "set a.strCustomerCode=CONCAT(right(a.strClientCode,3),a.strCustomerCode) "
		    + "where LENGTH(a.strCustomerCode)=8;";
	    i = ExecuteQuery(sql);

	    sql = "update tblqbillhd a "
		    + "set a.strCustomerCode=CONCAT(right(a.strClientCode,3),a.strCustomerCode) "
		    + "where LENGTH(a.strCustomerCode)=8;";
	    i = ExecuteQuery(sql);

	    sql = "update tbladvbookbillhd a "
		    + "set a.strCustomerCode=CONCAT(right(a.strClientCode,3),a.strCustomerCode) "
		    + "where LENGTH(a.strCustomerCode)=8;";
	    i = ExecuteQuery(sql);

	    sql = "update tblqadvbookbillhd a "
		    + "set a.strCustomerCode=CONCAT(right(a.strClientCode,3),a.strCustomerCode) "
		    + "where LENGTH(a.strCustomerCode)=8;";
	    i = ExecuteQuery(sql);

	    sql = "update tbladvbookbilldtl a "
		    + "set a.strCustomerCode=CONCAT(right(a.strClientCode,3),a.strCustomerCode) "
		    + "where LENGTH(a.strCustomerCode)=8;";
	    i = ExecuteQuery(sql);

	    sql = "update tblqadvbookbilldtl a "
		    + "set a.strCustomerCode=CONCAT(right(a.strClientCode,3),a.strCustomerCode) "
		    + "where LENGTH(a.strCustomerCode)=8;";
	    i = ExecuteQuery(sql);

	    sql = "update tbldebitcardmaster a "
		    + "set a.strCustomerCode=CONCAT(right(a.strClientCode,3),a.strCustomerCode) "
		    + "where LENGTH(a.strCustomerCode)=8;";
	    i = ExecuteQuery(sql);

	    sql = "update tblhomedelivery a "
		    + "set a.strCustomerCode=CONCAT(right(a.strClientCode,3),a.strCustomerCode) "
		    + "where LENGTH(a.strCustomerCode)=8;";
	    i = ExecuteQuery(sql);

	    sql = "update tblreservation a "
		    + "set a.strCustomerCode=CONCAT(right(a.strClientCode,3),a.strCustomerCode) "
		    + "where LENGTH(a.strCustomerCode)=8;";
	    i = ExecuteQuery(sql);

	    sql = "update tblbillsettlementdtl a "
		    + "set a.strCustomerCode=CONCAT(right(a.strClientCode,3),a.strCustomerCode) "
		    + "where LENGTH(a.strCustomerCode)=8;";
	    i = ExecuteQuery(sql);

	    sql = "update tblqbillsettlementdtl a "
		    + "set a.strCustomerCode=CONCAT(right(a.strClientCode,3),a.strCustomerCode) "
		    + "where LENGTH(a.strCustomerCode)=8;";
	    i = ExecuteQuery(sql);

	    sql = "update tblbilldtl a "
		    + "set a.strCustomerCode=CONCAT(right(a.strClientCode,3),a.strCustomerCode) "
		    + "where LENGTH(a.strCustomerCode)=8;";
	    i = ExecuteQuery(sql);

	    sql = "update tblqbilldtl a "
		    + "set a.strCustomerCode=CONCAT(right(a.strClientCode,3),a.strCustomerCode) "
		    + "where LENGTH(a.strCustomerCode)=8;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblvoidadvbookbillhd` ALTER `strCustomerCode` DROP DEFAULT;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblvoidadvbookbillhd` "
		    + " CHANGE COLUMN `strCustomerCode` `strCustomerCode` VARCHAR(15) NOT NULL AFTER `strClientCode`";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` "
		    + "ADD COLUMN `strCarryForwardFloatAmtToNextDay` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strInrestoPOSKey`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbillcomplementrydtl` "
		    + " CHANGE COLUMN `strCustomerCode` `strCustomerCode` VARCHAR(10) NULL DEFAULT '' AFTER `strClientCode`";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbillcomplementrydtl` "
		    + " CHANGE COLUMN `strCustomerCode` `strCustomerCode` VARCHAR(10) NULL DEFAULT '' AFTER `strClientCode`";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbillmodifierdtl` CHANGE COLUMN `strCustomerCode` `strCustomerCode` VARCHAR(20) NOT NULL DEFAULT '' AFTER `strClientCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbillmodifierdtl` CHANGE COLUMN `strCustomerCode` `strCustomerCode` VARCHAR(20) NOT NULL DEFAULT '' AFTER `strClientCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblcashmanagement` ADD COLUMN `strClientCode` VARCHAR(10) NOT NULL AFTER `dblRollingAmt`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblcashmanagement` ADD COLUMN `strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strClientCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblcashmanagement` ADD PRIMARY KEY (`strTransID`, `strClientCode`);";
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE tblsetup "
		    + "ADD COLUMN strOpenCashDrawerAfterBillPrintYN VARCHAR(1) NOT NULL DEFAULT 'N' AFTER strCarryForwardFloatAmtToNextDay ";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbillhd` ADD COLUMN `strTransactionType` VARCHAR(300) NOT NULL DEFAULT '' AFTER `strCardNo`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbillhd` ADD COLUMN `strTransactionType` VARCHAR(300) NOT NULL DEFAULT '' AFTER `strCardNo`;";
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsubgrouphd` "
		    + "ADD COLUMN `strFactoryCode` VARCHAR(10) NOT NULL DEFAULT '' AFTER `strAccountCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsubgrouphd` DROP COLUMN `strMMSPropertyCode`";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsubgrouphd` DROP COLUMN `strWSLocationCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbillpromotiondtl` "
		    + " ADD COLUMN `dblDiscountPer` DECIMAL(18,4) NOT NULL DEFAULT '0' AFTER `dblAmount`,"
		    + " ADD COLUMN `dblDiscountAmt` DECIMAL(18,4) NOT NULL DEFAULT '0' AFTER `dblDiscountPer`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbillpromotiondtl` "
		    + " ADD COLUMN `dblDiscountPer` DECIMAL(18,4) NOT NULL DEFAULT '0' AFTER `dblAmount`,"
		    + " ADD COLUMN `dblDiscountAmt` DECIMAL(18,4) NOT NULL DEFAULT '0' AFTER `dblDiscountPer`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` "
		    + "ADD COLUMN `strPropertyWiseSalesOrderYN` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strOpenCashDrawerAfterBillPrintYN` ";
	    i = ExecuteQuery(sql);

	    sql = "CREATE TABLE IF NOT EXISTS `tblmasteroperationstatus` ( "
		    + "	`strTableName` VARCHAR(50) NOT NULL, "
		    + "	`dteDateEdited` DATETIME NOT NULL, "
		    + "	`strClientCode` VARCHAR(10) NOT NULL, "
		    + "	PRIMARY KEY (`strTableName`, `strClientCode`) "
		    + ") "
		    + "COLLATE='utf8_general_ci' "
		    + "ENGINE=InnoDB "
		    + ";";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbillseries` "
		    + " ADD COLUMN `strPrintInclusiveOfTaxOnBill` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strPrintGTOfOtherBills`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbillseries` DROP PRIMARY KEY, ADD PRIMARY KEY (`strPOSCode`, `strBillSeries`, `strClientCode`);";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` "
		    + " ADD COLUMN `strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strPropertyWiseSalesOrderYN`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblkdsprocess` "
		    + "CHANGE COLUMN `strBillNo` `strDocNo` VARCHAR(20) NOT NULL FIRST, "
		    + "ADD COLUMN `strKDSName` VARCHAR(10) NOT NULL AFTER `strUserEdited` ";
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblpmspostingbilldtl` "
		    + "	ADD COLUMN `dteBillDate` DATETIME NOT NULL AFTER `strPOSCode`, "
		    + "	ADD COLUMN `dblSettleAmt` DECIMAL(18,2) NOT NULL AFTER `dteBillDate`, "
		    + "	ADD COLUMN `strRoomNo` VARCHAR(15) NOT NULL AFTER `strGuestCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblpmspostingbilldtl` "
		    + "	ADD COLUMN `strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strClientCode`, "
		    + "	ADD COLUMN `strPMSDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strDataPostFlag`;";
	    i = ExecuteQuery(sql);

	    sql = " ALTER TABLE `tblposwiseitemwiseincentives` "
		    + " ADD COLUMN `dteDateCreated` DATETIME NOT NULL AFTER `strDataPostFlag`, "
		    + " ADD COLUMN `dteDateEdited` DATETIME NOT NULL AFTER `dteDateCreated`; ";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbladvordermodifierdtl` ALTER `strCustomerCode` DROP DEFAULT;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbladvordermodifierdtl` "
		    + "	CHANGE COLUMN `strCustomerCode` `strCustomerCode` VARCHAR(15) NOT NULL AFTER `strClientCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqadvordermodifierdtl` ALTER `strCustomerCode` DROP DEFAULT;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqadvordermodifierdtl` "
		    + "	CHANGE COLUMN `strCustomerCode` `strCustomerCode` VARCHAR(15) NOT NULL AFTER `strClientCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbltruncationdtl` "
		    + "CHANGE COLUMN `strTruncateForms` `strTruncateForms` VARCHAR(500) NOT NULL DEFAULT '' AFTER `strUser` ";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblcustomermaster` "
		    + "ADD COLUMN `strCustAddress` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strCRMId`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblcostcentermaster` "
		    + "ADD COLUMN `strLabelOnKOT` VARCHAR(10) NOT NULL DEFAULT 'KOT' AFTER `strDataPostFlag`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE tblsetup "
		    + " DROP COLUMN strEnableAuthorisationForTransactionYN ;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblcustomermaster` "
		    + "CHANGE COLUMN `strCustAddress` `strCustAddress` VARCHAR(200) NOT NULL DEFAULT '' AFTER `strCRMId`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsuperuserdtl` ADD COLUMN `strTLA` VARCHAR(5) NOT NULL DEFAULT 'false' AFTER `strGrant`";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsuperuserdtl` ADD COLUMN `strAuditing` VARCHAR(5) NOT NULL DEFAULT 'true' AFTER `strTLA`";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbluserdtl` ADD COLUMN `strTLA` VARCHAR(5) NOT NULL DEFAULT 'false' AFTER `strGrant`";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbluserdtl` ADD COLUMN `strAuditing` VARCHAR(5) NOT NULL DEFAULT 'true' AFTER `strTLA`";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblitemrtemp`	ENGINE=InnoDB;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblitemrtemp` "
		    + "ADD COLUMN `strReason` VARCHAR(50) NOT NULL DEFAULT 'No' AFTER `dblTaxAmt`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblitemrtemp` 	ALTER `strKOTNo` DROP DEFAULT;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblitemrtemp` "
		    + "ADD PRIMARY KEY (`strTableNo`, `strItemCode`, `strKOTNo`,`strItemName`);";
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblitemrtemp` "
		    + "CHANGE COLUMN `intId` `intId` BIGINT(20) NULL AUTO_INCREMENT AFTER `dblRate`, "
		    + "ADD INDEX `intId` (`intId`);";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblitemrtemp_bck` "
		    + "CHANGE COLUMN `intId` `intId` BIGINT(20) NULL AUTO_INCREMENT AFTER `dblRate`, "
		    + "ADD INDEX `intId` (`intId`);";
	    i = ExecuteQuery(sql);

	    funStructureUpdateForTblItemrTemp();

	    sql = "ALTER TABLE `tbldebitcardtabletemp` "
		    + "CHANGE COLUMN `strTableNo` `strTableNo` VARCHAR(10) NOT NULL FIRST;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblitemrtemp` "
		    + "DROP PRIMARY KEY, "
		    + "ADD PRIMARY KEY (`strTableNo`, `strItemCode`, `strKOTNo`, `strItemName`, `intId`);";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblitemrtemp_bck` "
		    + "DROP PRIMARY KEY, "
		    + "ADD PRIMARY KEY (`strTableNo`, `strItemCode`, `strKOTNo`, `strItemName`, `intId`);";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblitemrtemp` "
		    + "CHANGE COLUMN `intId` `intId` BIGINT(20) NULL DEFAULT NULL AFTER `dblRate`, "
		    + "DROP PRIMARY KEY, "
		    + "ADD PRIMARY KEY (`strTableNo`, `strItemCode`, `strKOTNo`, `strItemName`, `strSerialNo`);";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblitemrtemp_bck` "
		    + "CHANGE COLUMN `intId` `intId` BIGINT(20) NULL DEFAULT NULL AFTER `dblRate`, "
		    + "DROP PRIMARY KEY, "
		    + "ADD PRIMARY KEY (`strTableNo`, `strItemCode`, `strKOTNo`, `strItemName`, `strSerialNo`);";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblitemrtemp` "
		    + " DROP INDEX  `intId` ;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblitemrtemp_bck` "
		    + " DROP INDEX  `intId` ;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblreservation` "
		    + "ADD COLUMN `strPosCode` VARCHAR(10) NOT NULL AFTER `strDataPostFlag`;";
	    i = ExecuteQuery(sql);

	    sql = " ALTER TABLE `tblsetup` ADD COLUMN `strShowItemDetailsGrid` VARCHAR(1) NOT NULL DEFAULT 'Y' AFTER `strDataPostFlag` ";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblforms`	ADD COLUMN `strRequestMapping` VARCHAR(100) NOT NULL AFTER `strColorImageName`;";
	    i = ExecuteQuery(sql);

	    sql = " CREATE TABLE if not exists `tblaccountmaster` ( "
		    + " `strWBAccountCode` VARCHAR(20) NOT NULL, "
		    + " `strWBAccountName` VARCHAR(200) NOT NULL, "
		    + " `strClientCode` VARCHAR(20) NOT NULL, "
		    + " PRIMARY KEY (`strWBAccountCode`, `strClientCode`) "
		    + " ) "
		    + " COLLATE='latin1_swedish_ci' "
		    + " ENGINE=InnoDB ";
	    i = ExecuteQuery(sql);

	    sql = " ALTER TABLE `tblposmaster` "
		    + " ADD COLUMN `strWSLocationName` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strEnableShift`";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` "
		    + "ADD COLUMN `strShowPopUpForNextItemQuantity` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strShowItemDetailsGrid` ";
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblmenuitempricingdtl` "
		    + "	CHANGE COLUMN `longPricingId` `longPricingId` BIGINT(20) NOT NULL AUTO_INCREMENT AFTER `strHourlyPricing`, "
		    + "	ADD PRIMARY KEY (`longPricingId`);";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbilldtl` "
		    + "CHANGE COLUMN `dblQuantity` `dblQuantity` DECIMAL(18,3) NOT NULL AFTER `dblRate`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbilldtl` "
		    + "CHANGE COLUMN `dblQuantity` `dblQuantity` DECIMAL(18,3) NOT NULL AFTER `dblRate`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblitemmaster` "
		    + "ADD COLUMN `strUOM` VARCHAR(5) NOT NULL DEFAULT '' AFTER `strUrgentOrder`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblcustomermaster` "
		    + "ENGINE=InnoDB";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` "
		    + "ADD COLUMN `strJioMoneyIntegration` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strShowPopUpForNextItemQuantity`,"
		    + "ADD COLUMN `strJioWebServiceUrl` VARCHAR(255) NOT NULL DEFAULT '' AFTER `strJioMoneyIntegration`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE tblbillhd	CHANGE COLUMN `strBillNo` `strBillNo` VARCHAR(15);";
	    i = ExecuteQuery(sql);
	    sql = "ALTER TABLE tblbilldtl	CHANGE COLUMN `strBillNo` `strBillNo` VARCHAR(15);";
	    i = ExecuteQuery(sql);
	    sql = "ALTER TABLE tblbillmodifierdtl	CHANGE COLUMN `strBillNo` `strBillNo` VARCHAR(15);";
	    i = ExecuteQuery(sql);
	    sql = "ALTER TABLE tblbilldiscdtl	CHANGE COLUMN `strBillNo` `strBillNo` VARCHAR(15);";
	    i = ExecuteQuery(sql);
	    sql = "ALTER TABLE tblbillpromotiondtl	CHANGE COLUMN `strBillNo` `strBillNo` VARCHAR(15);";
	    i = ExecuteQuery(sql);
	    sql = "ALTER TABLE tblbillcomplementrydtl	CHANGE COLUMN `strBillNo` `strBillNo` VARCHAR(15);";
	    i = ExecuteQuery(sql);
	    sql = "ALTER TABLE tblbillseriesbilldtl	CHANGE COLUMN `strHdBillNo` `strHdBillNo` VARCHAR(15);";
	    i = ExecuteQuery(sql);
	    sql = "ALTER TABLE tblbillseriesbilldtl	CHANGE COLUMN `strDtlBillNos` `strDtlBillNos` VARCHAR(200);";
	    i = ExecuteQuery(sql);
	    sql = "ALTER TABLE tblbillsettlementdtl	CHANGE COLUMN `strBillNo` `strBillNo` VARCHAR(15);";
	    i = ExecuteQuery(sql);
	    sql = "ALTER TABLE tblbilltaxdtl	CHANGE COLUMN `strBillNo` `strBillNo` VARCHAR(15);";
	    i = ExecuteQuery(sql);
	    sql = "ALTER TABLE tblqbillhd	CHANGE COLUMN `strBillNo` `strBillNo` VARCHAR(15);";
	    i = ExecuteQuery(sql);
	    sql = "ALTER TABLE tblqbilldtl	CHANGE COLUMN `strBillNo` `strBillNo` VARCHAR(15);";
	    i = ExecuteQuery(sql);
	    sql = "ALTER TABLE tblqbillmodifierdtl	CHANGE COLUMN `strBillNo` `strBillNo` VARCHAR(15);";
	    i = ExecuteQuery(sql);
	    sql = "ALTER TABLE tblqbilldiscdtl	CHANGE COLUMN `strBillNo` `strBillNo` VARCHAR(15);";
	    i = ExecuteQuery(sql);
	    sql = "ALTER TABLE tblqbillpromotiondtl	CHANGE COLUMN `strBillNo` `strBillNo` VARCHAR(15);";
	    i = ExecuteQuery(sql);
	    sql = "ALTER TABLE tblqbillcomplementrydtl	CHANGE COLUMN `strBillNo` `strBillNo` VARCHAR(15);";
	    i = ExecuteQuery(sql);
	    sql = "ALTER TABLE tblqbillsettlementdtl	CHANGE COLUMN `strBillNo` `strBillNo` VARCHAR(15);";
	    i = ExecuteQuery(sql);
	    sql = "ALTER TABLE tblqbilltaxdtl	CHANGE COLUMN `strBillNo` `strBillNo` VARCHAR(15);";
	    i = ExecuteQuery(sql);
	    sql = "ALTER TABLE tblvoidbill	CHANGE COLUMN `strBillNo` `strBillNo` VARCHAR(15);";
	    i = ExecuteQuery(sql);
	    sql = "ALTER TABLE tblvoidbilldtl	CHANGE COLUMN `strBillNo` `strBillNo` VARCHAR(15);";
	    i = ExecuteQuery(sql);
	    sql = "ALTER TABLE tblvoidbillhd	CHANGE COLUMN `strBillNo` `strBillNo` VARCHAR(15);";
	    i = ExecuteQuery(sql);
	    sql = "ALTER TABLE tblvoidbillsettlementdtl	CHANGE COLUMN `strBillNo` `strBillNo` VARCHAR(15);";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblitemmasterlinkupdtl` "
		    + "ADD COLUMN `strExciseBrandCode` VARCHAR(20) NOT NULL DEFAULT '' AFTER `strWSProductName`, "
		    + "ADD COLUMN `strExciseBrandName` VARCHAR(200) NOT NULL DEFAULT '' AFTER `strExciseBrandCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblposmaster` "
		    + "	ADD COLUMN `strExciseLicenceName` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strWSLocationName`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbldayendprocess`ADD COLUMN `strExciseBillGeneration` VARCHAR(20) NOT NULL DEFAULT '' AFTER `dblTipAmt`";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblcustomermaster` "
		    + "	ADD COLUMN `strTempAddress` VARCHAR(200) NOT NULL DEFAULT '' AFTER `strCustAddress`, "
		    + "	ADD COLUMN `strTempStreet` VARCHAR(200) NOT NULL DEFAULT '' AFTER `strTempAddress`, "
		    + "	ADD COLUMN `strTempLandmark` VARCHAR(200) NOT NULL DEFAULT '' AFTER `strTempStreet`; ";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblimportexcel` "
		    + "ADD COLUMN `strUOM` VARCHAR(5) NOT NULL DEFAULT '' AFTER `strDOB`;";
	    i = ExecuteQuery(sql);

	    sql = "update tblmodifiermaster set strModifierName= TRIM(strModifierName) ;";
	    i = ExecuteQuery(sql);

	    sql = " ALTER TABLE `tblsetup` ADD COLUMN `strJioMID` VARCHAR(15) NOT NULL AFTER `strJioWebServiceUrl`,"
		    + " ADD COLUMN `strJioTID` VARCHAR(8) NOT NULL AFTER `strJioMID`,ADD COLUMN `strJioActivationCode` VARCHAR(16) NOT NULL AFTER `strJioTID`; ";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` "
		    + "ADD COLUMN `strJioDeviceID` VARCHAR(20) NOT NULL AFTER `strJioActivationCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` "
		    + "ADD COLUMN `strNewBillSeriesForNewDay` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strJioDeviceID`, "
		    + "ADD COLUMN `strShowReportsPOSWise` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strNewBillSeriesForNewDay`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblitemmasterlinkupdtl` "
		    + "ADD PRIMARY KEY (`strItemCode`, `strPOSCode`, `strClientCode`);";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbillseriesbilldtl` "
		    + "CHANGE COLUMN `strHdBillNo` `strHdBillNo` VARCHAR(15) NOT NULL AFTER `strBillSeries`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbillseriesbilldtl` "
		    + "ADD COLUMN `dteBillDate` DATETIME NOT NULL AFTER `dteEditedDate`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbillseriesbilldtl` "
		    + "ADD PRIMARY KEY (`strPOSCode`, `strHdBillNo`, `strClientCode`, `dteBillDate`);";
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblhomedelivery` "
		    + "ADD COLUMN `dblLooseCashAmt` DOUBLE(18,2) NOT NULL DEFAULT '0.00' AFTER `dblHomeDeliCharge`;";
	    i = ExecuteQuery(sql);

	    sql = " ALTER TABLE `tblbillhd` "
		    + "ADD COLUMN `strJioMoneyRRefNo` VARCHAR(20) NOT NULL DEFAULT '' AFTER `strTransactionType`;";
	    i = ExecuteQuery(sql);

	    sql = " ALTER TABLE `tblqbillhd` "
		    + "ADD COLUMN `strJioMoneyRRefNo` VARCHAR(20) NOT NULL DEFAULT '' AFTER `strTransactionType`;";
	    i = ExecuteQuery(sql);

	    //             sql ="ALTER TABLE `tblsetup` " 
//                 +"DROP COLUMN `strEnableDineIn`, "
//                 +"DROP COLUMN `strAutoAreaSelectionInMakeKOT`, "
//                 +"DROP COLUMN `strConsolidatedKOTPrinterPort`;";
//            i = ExecuteQuery(sql);
	    sql = "ALTER TABLE `tblsetup` "
		    + "ADD COLUMN `strEnableDineIn` VARCHAR(1) NOT NULL DEFAULT 'Y' AFTER `strShowReportsPOSWise`; ";
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

	    funForButtonSequenceStrucureUpdate();

	    sql = "ALTER TABLE `tblgrouphd` "
		    + "ADD COLUMN `strGroupShortName` VARCHAR(50) NOT NULL DEFAULT '' AFTER `strOperationalYN`;";
	    i = ExecuteQuery(sql);

	    sql = "update tbltablemaster set strPOSCode='P01' where strPOSCode='All';";
	    i = ExecuteQuery(sql);

	    sql = "update tblitemrtemp set strPOSCode='P01' where strPOSCode='All';";
	    i = ExecuteQuery(sql);

	    sql = "update tblitemrtemp_bck set strPOSCode='P01' where strPOSCode='All';";
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

	    funInsertTaxObGroupData();

	    sql = "ALTER TABLE `tblitemmasterlinkupdtl` DROP PRIMARY KEY;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblitemmasterlinkupdtl` "
		    + "ADD INDEX `strItemCode_strPOSCode_strWSProductCode_strClientCode` (`strItemCode`, `strPOSCode`, `strWSProductCode`, `strClientCode`);";
	    i = ExecuteQuery(sql);

	    sql = " ALTER TABLE `tblbillhd` "
		    + " ADD COLUMN `strJioMoneyAuthCode` VARCHAR(10) NOT NULL DEFAULT '' AFTER `strJioMoneyRRefNo`, "
		    + " ADD COLUMN `strJioMoneyTxnId` VARCHAR(25) NOT NULL DEFAULT '' AFTER `strJioMoneyAuthCode`, "
		    + " ADD COLUMN `strJioMoneyTxnDateTime` VARCHAR(20) NOT NULL DEFAULT '' AFTER `strJioMoneyTxnId`,"
		    + " ADD COLUMN `strJioMoneyCardNo` VARCHAR(16) NOT NULL DEFAULT '' AFTER `strJioMoneyTxnDateTime`,"
		    + " ADD COLUMN `strJioMoneyCardType` VARCHAR(40) NOT NULL DEFAULT '' AFTER `strJioMoneyCardNo` ;";
	    i = ExecuteQuery(sql);

	    sql = " ALTER TABLE `tblqbillhd` "
		    + " ADD COLUMN `strJioMoneyAuthCode` VARCHAR(10) NOT NULL DEFAULT '' AFTER `strJioMoneyRRefNo`, "
		    + " ADD COLUMN `strJioMoneyTxnId` VARCHAR(25) NOT NULL DEFAULT '' AFTER `strJioMoneyAuthCode`, "
		    + " ADD COLUMN `strJioMoneyTxnDateTime` VARCHAR(20) NOT NULL DEFAULT '' AFTER `strJioMoneyTxnId`,"
		    + " ADD COLUMN `strJioMoneyCardNo` VARCHAR(16) NOT NULL DEFAULT '' AFTER `strJioMoneyTxnDateTime`,"
		    + " ADD COLUMN `strJioMoneyCardType` VARCHAR(40) NOT NULL DEFAULT '' AFTER `strJioMoneyCardNo` ;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` "
		    + "ADD COLUMN `strAutoAreaSelectionInMakeKOT` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strEnableDineIn`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` "
		    + "ADD COLUMN `strConsolidatedKOTPrinterPort` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strAutoAreaSelectionInMakeKOT`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblareamaster` "
		    + "ADD COLUMN `strMACAddress` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strPOSCode`;";
	    i = ExecuteQuery(sql);

	    sql = "CREATE TABLE if not exists `tblnonavailableitems` ( "
		    + "`strItemCode` VARCHAR(10) NOT NULL, "
		    + "`strItemName` VARCHAR(100) NOT NULL, "
		    + "`strClientCode` VARCHAR(10) NOT NULL, "
		    + "`dteDate` DATETIME NOT NULL, "
		    + "`strPOSCode` VARCHAR(10) NOT NULL, "
		    + "PRIMARY KEY (`strItemCode`,`strPOSCode`,`strClientCode`) "
		    + ")COLLATE='utf8_general_ci' "
		    + "ENGINE=InnoDB ;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblmenuhd` "
		    + "ADD COLUMN `strImagePath` VARCHAR(500) NOT NULL DEFAULT '' AFTER `strOperational`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblmenuhd` "
		    + "ADD COLUMN `imgImage` LONGBLOB NOT NULL AFTER `strImagePath`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblitemmaster` "
		    + "ADD COLUMN `imgImage` LONGBLOB NOT NULL AFTER `strUOM`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblitemmaster` "
		    + "CHANGE COLUMN `strItemImage` `strItemImage` VARCHAR(200) NOT NULL DEFAULT '' AFTER `strSubGroupCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbilldtl` "
		    + "CHANGE COLUMN `strItemName` `strItemName` VARCHAR(200) NOT NULL AFTER `strItemCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbilldtl` "
		    + "CHANGE COLUMN `strItemName` `strItemName` VARCHAR(200) NOT NULL AFTER `strItemCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbilldiscdtl` "
		    + "CHANGE COLUMN `strDiscOnValue` `strDiscOnValue` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strDiscOnType`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbillcomplementrydtl` "
		    + "CHANGE COLUMN `strItemName` `strItemName` VARCHAR(200) NOT NULL AFTER `strItemCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbillcomplementrydtl` "
		    + "CHANGE COLUMN `strItemName` `strItemName` VARCHAR(200) NOT NULL AFTER `strItemCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblvoidbilldtl` "
		    + "CHANGE COLUMN `strItemName` `strItemName` VARCHAR(200) NOT NULL AFTER `strItemCode`;";
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsuperuserdtl` "
		    + "DROP COLUMN `strClientCode`;";
	    i = ExecuteQuery(sql);

	    sql = "update tblsetup set strBillFormatType='Text 5' where strBillFormatType='Format 5';";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbltempsalesflash` "
		    + "CHANGE COLUMN `strname` `strname` VARCHAR(200) NOT NULL DEFAULT '' AFTER `strcode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbilldiscdtl` "
		    + "ADD COLUMN `dteBillDate` DATETIME NOT NULL AFTER `strDataPostFlag`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbilldiscdtl` "
		    + "ADD COLUMN `dteBillDate` DATETIME NOT NULL AFTER `strDataPostFlag`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbillmodifierdtl` "
		    + "ADD COLUMN `dteBillDate` DATETIME NOT NULL AFTER `dblDiscAmt`;  ";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbillmodifierdtl` "
		    + "ADD COLUMN `dteBillDate` DATETIME NOT NULL AFTER `dblDiscAmt`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbillpromotiondtl` "
		    + "ADD COLUMN `dteBillDate` DATETIME NOT NULL AFTER `dblDiscountAmt`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbillpromotiondtl` "
		    + "ADD COLUMN `dteBillDate` DATETIME NOT NULL AFTER `dblDiscountAmt`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbillsettlementdtl` "
		    + "DROP PRIMARY KEY;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbillsettlementdtl` "
		    + "ADD INDEX `strBillNo` (`strBillNo`);";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbillsettlementdtl` "
		    + "ADD COLUMN `dteBillDate` DATETIME NOT NULL AFTER `strRoomNo`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbillsettlementdtl` "
		    + "DROP PRIMARY KEY;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbillsettlementdtl` "
		    + "ADD INDEX `strBillNo` (`strBillNo`);";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbillsettlementdtl` "
		    + "ADD COLUMN `dteBillDate` DATETIME NOT NULL AFTER `strRoomNo`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbilltaxdtl` "
		    + "DROP PRIMARY KEY;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbilltaxdtl` "
		    + "ADD INDEX `strBillNo` (`strBillNo`);";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbilltaxdtl` "
		    + "ADD COLUMN `dteBillDate` DATETIME NOT NULL AFTER `strDataPostFlag`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbilltaxdtl` "
		    + "DROP PRIMARY KEY;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbilltaxdtl` "
		    + "ADD INDEX `strBillNo` (`strBillNo`);";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbilltaxdtl` "
		    + "ADD COLUMN `dteBillDate` DATETIME NOT NULL AFTER `strDataPostFlag`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblhomedeldtl` "
		    + "ADD COLUMN `dteBillDate` DATETIME NOT NULL AFTER `dblDBIncentives`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblvoidmodifierdtl` "
		    + "ADD COLUMN `dteBillDate` DATETIME NOT NULL AFTER `strReasonCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblvoidbillsettlementdtl` "
		    + "ADD COLUMN `dteBillDate` DATETIME NOT NULL AFTER `strDataPostFlag`;";
	    i = ExecuteQuery(sql);

	    //add indexing.....
	    funAddIndexing();

	    //updating billdate to old data
	    funUpdateBillDate();

	    sql = "ALTER TABLE `tblbillhd` "
		    + "	DROP PRIMARY KEY, "
		    + "	DROP INDEX `intVouchno`, "
		    + "	DROP INDEX `strBillNo`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbillhd` "
		    + "	ADD INDEX `strBillNo` (`strBillNo`); "
		    + "	";
	    i = ExecuteQuery(sql);

	    sql = "	ALTER TABLE `tblqbillhd` "
		    + "	DROP PRIMARY KEY, "
		    + "	DROP INDEX `strBillNo`;";
	    i = ExecuteQuery(sql);

	    sql = "	ALTER TABLE `tblqbillhd` "
		    + "	ADD INDEX `strBillNo` (`strBillNo`);";
	    i = ExecuteQuery(sql);

	    sql = "	ALTER TABLE `tbladvbookbillhd` "
		    + "	DROP PRIMARY KEY;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbladvbookbillhd` "
		    + "	ADD INDEX `strAdvBookingNo` (`strAdvBookingNo`);";
	    i = ExecuteQuery(sql);

	    sql = "	ALTER TABLE `tblqadvbookbillhd` "
		    + "	DROP PRIMARY KEY;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqadvbookbillhd` "
		    + "	ADD INDEX `strAdvBookingNo` (`strAdvBookingNo`);";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblpmspostingbilldtl` "
		    + "  DROP PRIMARY KEY;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblpmspostingbilldtl` "
		    + "  ADD INDEX `strBillNo` (`strBillNo`);";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblpmspostingbilldtl` "
		    + "ADD COLUMN `strBillType` VARCHAR(10) NOT NULL AFTER `strPMSDataPostFlag`;";
	    i = ExecuteQuery(sql);

	    sql = "update tblbillhd set strOperationType='DineIn' where strOperationType='Dine In';";
	    i = ExecuteQuery(sql);
	    sql = "update tblbillhd set strOperationType='TakeAway' where strOperationType='Take Away';";
	    i = ExecuteQuery(sql);
	    sql = "update tblbillhd set strOperationType='HomeDelivery' where strOperationType='Home Delivery';";
	    i = ExecuteQuery(sql);
	    sql = "update tblbillhd set strOperationType='DirectBiller' where strOperationType='Direct Biller';";
	    i = ExecuteQuery(sql);

	    sql = "update tblqbillhd set strOperationType='DineIn' where strOperationType='Dine In';";
	    i = ExecuteQuery(sql);
	    sql = "update tblqbillhd set strOperationType='TakeAway' where strOperationType='Take Away';";
	    i = ExecuteQuery(sql);
	    sql = "update tblqbillhd set strOperationType='HomeDelivery' where strOperationType='Home Delivery';";
	    i = ExecuteQuery(sql);
	    sql = "update tblqbillhd set strOperationType='DirectBiller' where strOperationType='Direct Biller';";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblitemmodofier` "
		    + "ADD PRIMARY KEY (`strItemCode`, `strModifierCode`);";
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsmssetup` "
		    + "CHANGE COLUMN `longMobileNo` `longMobileNo` VARCHAR(100) NOT NULL AFTER `strSendSMSYN`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` "
		    + "ADD COLUMN `dblRoundOff` DECIMAL(18,2) NOT NULL DEFAULT '1.00' AFTER `strConsolidatedKOTPrinterPort`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbillhd` "
		    + "	ADD COLUMN `dblRoundOff` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `strJioMoneyCardType`;";
	    i = ExecuteQuery(sql);
	    sql = "ALTER TABLE `tblqbillhd` "
		    + "	ADD COLUMN `dblRoundOff` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `strJioMoneyCardType`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblposmaster` "
		    + "	ADD COLUMN `strClientCode` VARCHAR(11) NOT NULL DEFAULT '' AFTER `strExciseLicenceName`;";
	    i = ExecuteQuery(sql);
	    sql = "ALTER TABLE `tblsettlementtax` "
		    + "	ADD COLUMN `strClientCode` VARCHAR(11) NOT NULL DEFAULT '' AFTER `dteDateEdited`;";
	    i = ExecuteQuery(sql);
	    sql = "ALTER TABLE `tbltaxposdtl` "
		    + "	ADD COLUMN `strClientCode` VARCHAR(11) NOT NULL DEFAULT '' AFTER `strTaxDesc`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblshiftmaster` "
		    + "	DROP COLUMN `strClientCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbltdhcomboitemdtl` "
		    + "	DROP COLUMN `strClientCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbltdhcomboitemdtl` "
		    + "	ADD COLUMN `strClientCode` VARCHAR(11) NOT NULL DEFAULT '' AFTER `strSubItemMenuCode`;";
	    i = ExecuteQuery(sql);
	    sql = "ALTER TABLE `tblshiftmaster` "
		    + "	ADD COLUMN `strClientCode` VARCHAR(11) NOT NULL DEFAULT '' AFTER `dteDateEdited`;";
	    i = ExecuteQuery(sql);

	    sql = "update tblqbillhd  "
		    + "set dblRoundOff=(dblGrandTotal-(dblSubTotal-dblDiscountAmt+dblTaxAmt)) "
		    + "where dblRoundOff=0.00;";
	    i = ExecuteQuery(sql);

	    sql = "update tblbillhd  "
		    + "set dblRoundOff=(dblGrandTotal-(dblSubTotal-dblDiscountAmt+dblTaxAmt)) "
		    + "where dblRoundOff=0.00;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` "
		    + "	ADD COLUMN `strShowUnSettlementForm` VARCHAR(1) NOT NULL DEFAULT 'Y' AFTER `dblRoundOff`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbltaxhd` "
		    + "	ADD COLUMN `strBillNote` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strTaxShortName`;";
	    i = ExecuteQuery(sql);

	    //update billNote-Service Tax no OR VAT no in tax master
	    funUpdateBillNote();

	    sql = "ALTER TABLE `tblsetup` "
		    + "	ADD COLUMN `strPrintOpenItemsOnBill` VARCHAR(1) NOT NULL DEFAULT 'Y' AFTER `strShowUnSettlementForm`;";
	    i = ExecuteQuery(sql);

	    sql = "update tblitemmaster set strTaxIndicator='' ";
	    i = ExecuteQuery(sql);

	    sql = "update tbltaxhd set strTaxIndicator='' ";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblpromotionmaster` ADD COLUMN `strAreaCode` VARCHAR(10) NOT NULL AFTER `strGetPromoOn`;";
	    i = ExecuteQuery(sql);

	    sql = "update tblpromotionmaster set strAreaCode=(select strAreaCode from tblareamaster where strAreaName='All') "
		    + "where strAreaCode='';";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblcustomermaster` "
		    + "	CHANGE COLUMN `longMobileNo` `longMobileNo` VARCHAR(100) NOT NULL DEFAULT '' AFTER `intPinCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` "
		    + "	ADD COLUMN `strPrintHomeDeliveryYN` VARCHAR(1) NOT NULL DEFAULT 'Y' AFTER `strPrintOpenItemsOnBill`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsettelmenthd` "
		    + "	ADD COLUMN `strCreditReceiptYN` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strBillPrintOnSettlement`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblcreditbillreceipthd` "
		    + "	DROP PRIMARY KEY;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblcreditbillreceipthd` "
		    + "	ADD COLUMN `dteBillDate` DATETIME NOT NULL AFTER `strDataPostFlag`, "
		    + "	ADD COLUMN `strSettlementCode` VARCHAR(10) NOT NULL AFTER `dteBillDate`, "
		    + "	ADD COLUMN `strSettlementName` VARCHAR(50) NOT NULL AFTER `strSettlementCode`, "
		    + "	ADD COLUMN `strChequeNo` VARCHAR(50) NOT NULL DEFAULT '' AFTER `strSettlementName`, "
		    + "	ADD COLUMN `strBankName` VARCHAR(50) NOT NULL DEFAULT '' AFTER `strChequeNo`, "
		    + "	ADD COLUMN `dteChequeDate` DATETIME NOT NULL DEFAULT '1990-01-01 00:00:00' AFTER `strBankName`, "
		    + "	ADD COLUMN `strRemarks` VARCHAR(50) NOT NULL DEFAULT '' AFTER `dteChequeDate`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblcreditbillreceipthd` "
		    + "	ADD INDEX `strReceiptNo_strBillNo_strPOSCode_dteBillDate` (`strReceiptNo`, `strBillNo`, `strPOSCode`, `dteBillDate`); ";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqcreditbillreceipthd` "
		    + "	DROP PRIMARY KEY;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqcreditbillreceipthd` "
		    + "	ADD COLUMN `dteBillDate` DATETIME NOT NULL AFTER `strDataPostFlag`, "
		    + "	ADD COLUMN `strSettlementCode` VARCHAR(10) NOT NULL AFTER `dteBillDate`, "
		    + "	ADD COLUMN `strSettlementName` VARCHAR(50) NOT NULL AFTER `strSettlementCode`, "
		    + "	ADD COLUMN `strChequeNo` VARCHAR(50) NOT NULL DEFAULT '' AFTER `strSettlementName`, "
		    + "	ADD COLUMN `strBankName` VARCHAR(50) NOT NULL DEFAULT '' AFTER `strChequeNo`, "
		    + "	ADD COLUMN `dteChequeDate` DATETIME NOT NULL DEFAULT '1990-01-01 00:00:00' AFTER `strBankName`, "
		    + "	ADD COLUMN `strRemarks` VARCHAR(50) NOT NULL DEFAULT '' AFTER `dteChequeDate`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqcreditbillreceipthd` "
		    + "	ADD INDEX `strReceiptNo_strBillNo_strPOSCode_dteBillDate` (`strReceiptNo`, `strBillNo`, `strPOSCode`, `dteBillDate`); ";
	    i = ExecuteQuery(sql);

	    sql = "CREATE TABLE if not exists `tblpromogroupmaster` (`strPromoGroupCode` VARCHAR(10) NOT NULL,"
		    + "	`strPromoGroupName` VARCHAR(100) NOT NULL,`strClientCode` VARCHAR(10) NOT NULL,"
		    + "	PRIMARY KEY (`strPromoGroupCode`, `strClientCode`)) "
		    + " COLLATE='utf8_general_ci' ENGINE=InnoDB;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblpromogroupmaster` "
		    + "	ADD COLUMN `strUserCreated` VARCHAR(10) NOT NULL AFTER `strClientCode`, "
		    + "	ADD COLUMN `strUserEdited` VARCHAR(10) NOT NULL AFTER `strUserCreated`, "
		    + "	ADD COLUMN `dteDateCreated` DATETIME NOT NULL AFTER `strUserEdited`, "
		    + "	ADD COLUMN `dteDateEdited` DATETIME NOT NULL AFTER `dteDateCreated`, "
		    + "	ADD COLUMN `strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `dteDateEdited`;";
	    i = ExecuteQuery(sql);

	    sql = "CREATE TABLE if not exists `tblpromogroupdtl` ( "
		    + "	`strPromoGroupCode` VARCHAR(10) NOT NULL, "
		    + "	`strItemCode` VARCHAR(10) NOT NULL, "
		    + "	`strMenuCode` VARCHAR(10) NOT NULL, "
		    + "	`strClientCode` VARCHAR(10) NOT NULL "
		    + ") "
		    + "COLLATE='utf8_general_ci' "
		    + "ENGINE=InnoDB "
		    + ";";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblpromogroupdtl` "
		    + "	ADD COLUMN `strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strClientCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblpromotionmaster` "
		    + "	ADD COLUMN `strPromoGroupType` VARCHAR(50) NOT NULL DEFAULT '' AFTER `strAreaCode`, "
		    + "	ADD COLUMN `longKOTTimeBound` INT NOT NULL DEFAULT '0' AFTER `strPromoGroupType`;";
	    i = ExecuteQuery(sql);

	    sql = " ALTER TABLE `tblsetup` ADD COLUMN `strScanQRYN` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strPrintHomeDeliveryYN`;";
	    i = ExecuteQuery(sql);

	    sql = " ALTER TABLE `tblmenuitempricingdtl` ADD COLUMN `strClientCode` VARCHAR(15) NOT NULL AFTER `longPricingId`; ";
	    i = ExecuteQuery(sql);

	    sql = " 	ALTER TABLE `tblitempricingauditdtl` "
		    + "	CHANGE COLUMN `strClientCode` `strClientCode` VARCHAR(15) NOT NULL DEFAULT '' AFTER `longPricingId`;";
	    i = ExecuteQuery(sql);

	    sql = "update tblmenuitempricingdtl set strClientCode='" + clsGlobalVarClass.gClientCode + "' where strClientCode='' ";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblitempricingauditdtl` "
		    + "	ADD COLUMN `strClientCode` VARCHAR(15) NOT NULL AFTER `longPricingId`;";
	    i = ExecuteQuery(sql);

	    sql = " 	ALTER TABLE `tblitempricingauditdtl` "
		    + "	CHANGE COLUMN `strClientCode` `strClientCode` VARCHAR(15) NOT NULL DEFAULT '' AFTER `longPricingId`;";
	    i = ExecuteQuery(sql);

	    sql = "update tblitempricingauditdtl set strClientCode='" + clsGlobalVarClass.gClientCode + "' where strClientCode='' ";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblitemrtemp` "
		    + "	ADD COLUMN `strItemProcessed` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strReason`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblitemrtemp_bck` "
		    + "	ADD COLUMN `strItemProcessed` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strReason`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup`	ADD COLUMN `strAreaWisePromotions` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strScanQRYN`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbldayendprocess` "
		    + "	ADD COLUMN `dblNetSale` DECIMAL(18,2) NULL DEFAULT '0.00' AFTER `strExciseBillGeneration`, "
		    + "	ADD COLUMN `dblGrossSale` DECIMAL(18,2) NULL DEFAULT '0.00' AFTER `dblNetSale`, "
		    + "	ADD COLUMN `dblAPC` DECIMAL(18,2) NULL DEFAULT '0.00' AFTER `dblGrossSale`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` "
		    + "	ADD COLUMN `strPrintItemsOnMoveKOTMoveTable` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strAreaWisePromotions`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbillcomplementrydtl`	ADD COLUMN `strType` VARCHAR(20) NOT NULL DEFAULT 'Complimentary' AFTER `strSequenceNo`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbillcomplementrydtl`	ADD COLUMN `strType` VARCHAR(20) NOT NULL DEFAULT 'Complimentary' AFTER `strSequenceNo`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblitemmaster` "
		    + "	CHANGE COLUMN `intProcTimeMin` `intProcTimeMin` BIGINT NOT NULL DEFAULT '0' AFTER `dblPurchaseRate`;";
	    i = ExecuteQuery(sql);

	    sql = "	ALTER TABLE `tblitemmaster` "
		    + "	ADD COLUMN `tmeTargetMiss` BIGINT NOT NULL DEFAULT '0' AFTER `imgImage`;";
	    i = ExecuteQuery(sql);

	    sql = "	ALTER TABLE `tblbillhd` "
		    + "	ADD COLUMN `intBillSeriesPaxNo` INT NOT NULL DEFAULT '0' AFTER `dblRoundOff`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbillhd` "
		    + "	ADD COLUMN `intBillSeriesPaxNo` INT NOT NULL DEFAULT '0' AFTER `dblRoundOff`;";
	    i = ExecuteQuery(sql);

	    sql = "update tblbillhd "
		    + "set intBillSeriesPaxNo=intPaxNo "
		    + "where intPaxNo>0 "
		    + "and intBillSeriesPaxNo=0 ;";
	    i = ExecuteQuery(sql);

	    sql = "update tblqbillhd "
		    + "set intBillSeriesPaxNo=intPaxNo "
		    + "where intPaxNo>0 "
		    + "and intBillSeriesPaxNo=0 ;";
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsuppliermaster` "
		    + "	ADD COLUMN `intMobileNumber` VARCHAR(50) NOT NULL AFTER `strDataPostFlag`, "
		    + "	ADD COLUMN `strEmailId` VARCHAR(50) NOT NULL AFTER `intMobileNumber`, "
		    + "	ADD COLUMN `strGSTNo` VARCHAR(50) NOT NULL AFTER `strEmailId`, "
		    + "	ADD COLUMN `strContactPerson` VARCHAR(100) NOT NULL AFTER `strGstNo`;";
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

	    sql = "RENAME TABLE `tblpurchaseorder` TO `tblpurchaseorderhd`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblpurchaseorderhd` "
		    + "	ADD COLUMN `dblSubTotal` DECIMAL(18,2) NOT NULL AFTER `strSupplierCode`, "
		    + "	ADD COLUMN `dblTaxAmt` DECIMAL(18,2) NOT NULL AFTER `dblSubTotal`, "
		    + "	ADD COLUMN `dblExtraAmt` DECIMAL(18,2) NOT NULL AFTER `dblTaxAmt`, "
		    + "	ADD COLUMN `dblGrandTotal` DECIMAL(18,2) NOT NULL AFTER `dblExtraAmt`;";
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblpurchaseorderdtl`	DROP COLUMN `strReasonCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` ADD COLUMN `strShowPurRateInDirectBiller` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strPrintItemsOnMoveKOTMoveTable`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblpurchaseorderhd` ADD COLUMN `dteDeliveryDate` DATETIME NOT NULL AFTER `strDataPostFlag`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbltablemaster` "
		    + "	ADD COLUMN `strNCTable` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strPOSCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblpurchaseorderhd` ADD COLUMN `strClosePO` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `dteDeliveryDate`;";
	    i = ExecuteQuery(sql);

//            // transection Table Primery Key Set Start          
	    sql = " ALTER TABLE `tblbillhd`  CHANGE COLUMN `strBillNo` `strBillNo` VARCHAR(15) NOT NULL FIRST, "
		    + " ADD PRIMARY KEY (`strBillNo`, `dteBillDate`, `strClientCode`); ";
	    i = ExecuteQuery(sql);

	    sql = " ALTER TABLE `tblqbillhd`  CHANGE COLUMN `strBillNo` `strBillNo` VARCHAR(15) NOT NULL FIRST, "
		    + " ADD PRIMARY KEY (`strBillNo`, `dteBillDate`, `strClientCode`); ";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbilldtl` "
		    + "	DROP PRIMARY KEY;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbilldtl` "
		    + "	DROP PRIMARY KEY;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbilldiscdtl` "
		    + "ALTER `strBillNo` DROP DEFAULT;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbilldiscdtl` "
		    + "	DROP PRIMARY KEY;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbilldiscdtl` "
		    + "ALTER `strBillNo` DROP DEFAULT;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbilldiscdtl` "
		    + "	DROP PRIMARY KEY;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbillmodifierdtl` "
		    + "ALTER `strBillNo` DROP DEFAULT;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbillmodifierdtl` "
		    + "	DROP PRIMARY KEY;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbillmodifierdtl` "
		    + "ALTER `strBillNo` DROP DEFAULT;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbillmodifierdtl` "
		    + "	DROP PRIMARY KEY;";
	    i = ExecuteQuery(sql);

	    sql = " ALTER TABLE `tblbilltaxdtl`  "
		    + "	ADD PRIMARY KEY (`strBillNo`, `strTaxCode`, `dteBillDate`, `strClientCode`); ";
	    i = ExecuteQuery(sql);

	    sql = " ALTER TABLE `tblqbilltaxdtl`  "
		    + "	ADD PRIMARY KEY (`strBillNo`, `strTaxCode`, `dteBillDate`, `strClientCode`); ";
	    i = ExecuteQuery(sql);

	    sql = " ALTER TABLE `tblbillsettlementdtl`  "
		    + "	ADD PRIMARY KEY (`strBillNo`, `strSettlementCode`, `strClientCode`, `dteBillDate`); ";
	    i = ExecuteQuery(sql);

	    sql = " ALTER TABLE `tblqbillsettlementdtl`  "
		    + "	ADD PRIMARY KEY (`strBillNo`, `strSettlementCode`, `strClientCode`, `dteBillDate`); ";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbillpromotiondtl` "
		    + "	DROP PRIMARY KEY;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbillpromotiondtl` "
		    + "	DROP PRIMARY KEY;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbillseries` "
		    + "ADD PRIMARY KEY (`strPOSCode`, `strType`, `strBillSeries`, `strClientCode`);";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblcreditbillreceipthd` "
		    + "ADD PRIMARY KEY (`strReceiptNo`, `strBillNo`, `strClientCode`, `dteBillDate`);";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqcreditbillreceipthd` "
		    + "ADD PRIMARY KEY (`strReceiptNo`, `strBillNo`, `strClientCode`, `dteBillDate`);";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblcashiermanagement` "
		    + "ADD PRIMARY KEY (`strTransactionId`, `dtDateEdited`);";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbldayendprocess` "
		    + "	DROP PRIMARY KEY;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbldayendprocess` "
		    + "	ADD PRIMARY KEY (`strPOSCode`, `dtePOSDate`, `intShiftCode`);";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblforms` "
		    + "ADD PRIMARY KEY (`strFormName`);";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblforms` "
		    + "ADD INDEX `strModuleName` (`strModuleName`);";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsettlementtax` "
		    + "ADD PRIMARY KEY (`strTaxCode`, `strSettlementCode`, `strClientCode`);";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblshiftmaster` "
		    + "ADD PRIMARY KEY (`intShiftCode`, `strPOSCode`, `strClientCode`);";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbluserdtl` "
		    + "ADD PRIMARY KEY (`strUserCode`, `strFormName`);";
	    i = ExecuteQuery(sql);
//            
//             // transection Table Primery Key Set End

	    sql = "insert into tblqcreditbillreceipthd(select * from tblcreditbillreceipthd);";
	    i = ExecuteQuery(sql);

	    sql = "delete from tblcreditbillreceipthd;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblvoidkot` "
		    + "	ADD COLUMN `strItemProcessed` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strRemark`;";
	    i = ExecuteQuery(sql);

	    /**
	     * update day end pax column
	     */
	    funUpdateDayEndPAX();

	    sql = "update tblinternal  "
		    + "set dblLastNo=(select Count(*) from tblcustomertypemaster) "
		    + "where strTransactionType='custtype' ";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblkdsprocess` "
		    + "	ADD COLUMN `strItemCode` VARCHAR(20) NOT NULL AFTER `strKDSName`, "
		    + "	ADD COLUMN `strCostCenterCode` VARCHAR(10) NOT NULL AFTER `strItemCode`, "
		    + "	ADD COLUMN `strWaiterNo` VARCHAR(20) NOT NULL AFTER `strCostCenterCode`, "
		    + "	ADD COLUMN `dteKOTDateAndTime` DATETIME NOT NULL AFTER `strWaiterNo`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblkdsprocess` DROP PRIMARY KEY;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblkdsprocess` "
		    + "	ADD INDEX `strDocNo` (`strDocNo`), "
		    + "	ADD INDEX `strItemCode` (`strItemCode`), "
		    + "	ADD INDEX `strCostCenterCode` (`strCostCenterCode`), "
		    + "	ADD INDEX `strWaiterNo` (`strWaiterNo`);";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblvoidkot` "
		    + "	ADD COLUMN `strItemProcessed` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strRemark`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbillcomplementrydtl` "
		    + "	DROP PRIMARY KEY;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbillcomplementrydtl` "
		    + "	DROP PRIMARY KEY;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblkdsprocess` "
		    + " ADD COLUMN `strItemCode` VARCHAR(20) NOT NULL AFTER `strKDSName`, "
		    + " ADD COLUMN `strCostCenterCode` VARCHAR(10) NOT NULL AFTER `strItemCode`, "
		    + " ADD COLUMN `strWaiterNo` VARCHAR(20) NOT NULL AFTER `strCostCenterCode`, "
		    + " ADD COLUMN `dteKOTDateAndTime` DATETIME NOT NULL AFTER `strWaiterNo`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblkdsprocess` DROP PRIMARY KEY;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblkdsprocess` "
		    + " ADD INDEX `strDocNo` (`strDocNo`), "
		    + " ADD INDEX `strItemCode` (`strItemCode`), "
		    + " ADD INDEX `strCostCenterCode` (`strCostCenterCode`), "
		    + " ADD INDEX `strWaiterNo` (`strWaiterNo`);";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblvoidkot` "
		    + " ADD COLUMN `strItemProcessed` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strRemark`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbillhd`  ADD COLUMN `dtBillDate` DATE NOT NULL AFTER `intBillSeriesPaxNo`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbillhd`  DROP PRIMARY KEY, ADD PRIMARY KEY (`strBillNo`, `strClientCode`, `dtBillDate`);";
	    i = ExecuteQuery(sql);

	    sql = "update tblbillhd set dtBillDate=date(dteBillDate) where dtBillDate='0000-00-00';";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbillhd` ADD COLUMN `dtBillDate` DATE NOT NULL AFTER `intBillSeriesPaxNo`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbillhd` DROP PRIMARY KEY, ADD PRIMARY KEY (`strBillNo`, `strClientCode`, `dtBillDate`);";
	    i = ExecuteQuery(sql);

	    sql = "update tblqbillhd set dtBillDate=date(dteBillDate) where dtBillDate='0000-00-00';";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbilldtl` ADD COLUMN `dtBillDate` DATE NOT NULL AFTER `strSequenceNo`;";
	    i = ExecuteQuery(sql);

	    sql = "update tblbilldtl set dtBillDate=date(dteBillDate) where dtBillDate='0000-00-00';";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbilldtl`    ADD COLUMN `dtBillDate` DATE NOT NULL AFTER `strSequenceNo`;";
	    i = ExecuteQuery(sql);

	    sql = "update tblqbilldtl set dtBillDate=date(dteBillDate) where dtBillDate='0000-00-00';";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbillseriesbilldtl`   ALTER `dteBillDate` DROP DEFAULT;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbillseriesbilldtl`   CHANGE COLUMN `dteBillDate` `dteBillDate` DATE NOT NULL AFTER `dteEditedDate`;";
	    i = ExecuteQuery(sql);

	    sql = " ALTER TABLE `tblbillsettlementdtl`    DROP PRIMARY KEY";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbillsettlementdtl`   ALTER `dteBillDate` DROP DEFAULT;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbillsettlementdtl`   CHANGE COLUMN `dteBillDate` `dteBillDate` DATE NOT NULL AFTER `strRoomNo`;";
	    i = ExecuteQuery(sql);

	    sql = " ALTER TABLE `tblbillsettlementdtl`     ADD PRIMARY KEY (`strBillNo`, `strClientCode`, `dteBillDate`,`strSettlementCode`)";
	    i = ExecuteQuery(sql);

	    sql = " ALTER TABLE `tblqbillsettlementdtl`   DROP PRIMARY KEY";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbillsettlementdtl`  ALTER `dteBillDate` DROP DEFAULT;";
	    i = ExecuteQuery(sql);

	    sql = " ALTER TABLE `tblqbillsettlementdtl`  DROP PRIMARY KEY";
	    i = ExecuteQuery(sql);

	    sql = " ALTER TABLE `tblqbillsettlementdtl`   ADD PRIMARY KEY (`strBillNo`, `strClientCode`, `dteBillDate`,`strSettlementCode`)";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbillsettlementdtl`  ALTER `dteBillDate` DROP DEFAULT;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbillsettlementdtl`  CHANGE COLUMN `dteBillDate` `dteBillDate` DATE NOT NULL AFTER `strRoomNo`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbillsettlementdtl` ALTER `dteBillDate` DROP DEFAULT;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbilltaxdtl`  ALTER `dteBillDate` DROP DEFAULT;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbilltaxdtl`  CHANGE COLUMN `dteBillDate` `dteBillDate` DATE NOT NULL AFTER `strDataPostFlag`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbilltaxdtl` ALTER `dteBillDate` DROP DEFAULT;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbilltaxdtl` CHANGE COLUMN `dteBillDate` `dteBillDate` DATE NOT NULL AFTER `strDataPostFlag`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbillpromotiondtl`    ALTER `dteBillDate` DROP DEFAULT;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbillpromotiondtl`    CHANGE COLUMN `dteBillDate` `dteBillDate` DATE NOT NULL AFTER `dblDiscountAmt`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbillpromotiondtl`   ALTER `dteBillDate` DROP DEFAULT;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbillpromotiondtl`   CHANGE COLUMN `dteBillDate` `dteBillDate` DATE NOT NULL AFTER `dblDiscountAmt`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbillcomplementrydtl` ALTER `dteBillDate` DROP DEFAULT;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbillcomplementrydtl` CHANGE COLUMN `dteBillDate` `dteBillDate` DATE NOT NULL AFTER `dblTaxAmount`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbillcomplementrydtl`    ALTER `dteBillDate` DROP DEFAULT;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbillcomplementrydtl`    CHANGE COLUMN `dteBillDate` `dteBillDate` DATE NOT NULL AFTER `dblTaxAmount`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbilldiscdtl` ALTER `dteBillDate` DROP DEFAULT;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbilldiscdtl` CHANGE COLUMN `dteBillDate` `dteBillDate` DATE NOT NULL AFTER `strDataPostFlag`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbilldiscdtl`    ALTER `dteBillDate` DROP DEFAULT;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbilldiscdtl`    CHANGE COLUMN `dteBillDate` `dteBillDate` DATE NOT NULL AFTER `strDataPostFlag`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbillmodifierdtl` ALTER `dteBillDate` DROP DEFAULT;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbillmodifierdtl` CHANGE COLUMN `dteBillDate` `dteBillDate` DATE NOT NULL AFTER `dblDiscAmt`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbillmodifierdtl`    ALTER `dteBillDate` DROP DEFAULT;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbillmodifierdtl`    CHANGE COLUMN `dteBillDate` `dteBillDate` DATE NOT NULL AFTER `dblDiscAmt`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbilldtl`"
		    + "ADD COLUMN `tmeOrderPickup` TIME NOT NULL DEFAULT '00:00:00' AFTER `dtBillDate`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblitemrtemp`"
		    + "ADD COLUMN `tmeOrderProcessing` TIME NOT NULL DEFAULT '00:00:00' AFTER `strItemProcessed`,"
		    + "ADD COLUMN `tmeOrderPickup` TIME NOT NULL DEFAULT '00:00:00' AFTER `tmeOrderProcessing`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbilldtl`"
		    + "ADD COLUMN `tmeOrderPickup` TIME NOT NULL DEFAULT '00:00:00' AFTER `dtBillDate`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblitemrtemp_bck`"
		    + "ADD COLUMN `tmeOrderProcessing` TIME NOT NULL DEFAULT '00:00:00' AFTER `strItemProcessed`,"
		    + "ADD COLUMN `tmeOrderPickup` TIME NOT NULL DEFAULT '00:00:00' AFTER `tmeOrderProcessing`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblitemrtemp` ADD COLUMN `strItemPickedUp` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `tmeOrderPickup`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblitemrtemp_bck` ADD COLUMN `strItemPickedUp` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `tmeOrderPickup`;";
	    i = ExecuteQuery(sql);

	    sql = "	ALTER TABLE `tblbillcomplementrydtl` "
		    + "	ADD COLUMN `dtBillDate` DATE NOT NULL AFTER `strType`;";
	    i = ExecuteQuery(sql);

	    sql = "	ALTER TABLE `tblbillcomplementrydtl` "
		    + "	ADD COLUMN `tmeOrderPickup` TIME NOT NULL DEFAULT '00:00:00' AFTER `dtBillDate`;";
	    i = ExecuteQuery(sql);

	    sql = "	ALTER TABLE `tblqbillcomplementrydtl` "
		    + "	ADD COLUMN `dtBillDate` DATE NOT NULL AFTER `strType`;";
	    i = ExecuteQuery(sql);

	    sql = "	ALTER TABLE `tblqbillcomplementrydtl` "
		    + "	ADD COLUMN `tmeOrderPickup` TIME NOT NULL DEFAULT '00:00:00' AFTER `dtBillDate`;";
	    i = ExecuteQuery(sql);

	    sql = "update tblqbillhd set dteDateEdited=DATE_ADD(dteDateEdited,INTERVAL 1 DAY) "
		    + " where TIMESTAMPDIFF(second,dteBillDate,concat(date(dteDateEdited),concat(' ',time(dteSettledate)))) < 0;";
	    i = ExecuteQuery(sql);

	    sql = "update tblbillhd set dteDateEdited=DATE_ADD(dteDateEdited,INTERVAL 1 DAY) "
		    + " where TIMESTAMPDIFF(second,dteBillDate,concat(date(dteDateEdited),concat(' ',time(dteSettledate)))) < 0;";
	    i = ExecuteQuery(sql);

	    sql = " ALTER TABLE `tblsetup` "
		    + " ADD COLUMN `strEnableTableReservationForCustomer` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strShowPurRateInDirectBiller`; ";

	    i = ExecuteQuery(sql);

	    sql = " ALTER TABLE `tblvoidkot` "
		    + " ADD COLUMN `strVoidBillType` VARCHAR(50) NOT NULL DEFAULT 'N' AFTER `strItemProcessed`;";

	    i = ExecuteQuery(sql);

	    sql = " ALTER TABLE `tblvoidbillhd`"
		    + " ADD COLUMN `strVoidBillType` VARCHAR(50) NOT NULL AFTER `strRemark`;";
	    i = ExecuteQuery(sql);

	    //to update old data for Item Void or Bill Void
	    sql = "update tblvoidbillhd d "
		    + "join (select a.strBillNo,a.dteBillDate from tblbillhd a ,tblvoidbillhd b where a.strBillNo=b.strBillNo and date(a.dteBillDate)=date(b.dteBillDate) )c "
		    + "set d.strVoidBillType='Item Void' "
		    + "where d.strVoidBillType=''"
		    + "and d.strTransType='VB' "
		    + "and d.strBillNo=c.strBillNo "
		    + "and date(d.dteBillDate)=date(c.dteBillDate);";
	    i = ExecuteQuery(sql);

	    sql = "update tblvoidbillhd d "
		    + "join (select a.strBillNo,a.dteBillDate from tblqbillhd a ,tblvoidbillhd b where a.strBillNo=b.strBillNo and date(a.dteBillDate)=date(b.dteBillDate) )c "
		    + "set d.strVoidBillType='Item Void' "
		    + "where d.strVoidBillType='' "
		    + "and d.strTransType='VB'  "
		    + "and d.strBillNo=c.strBillNo "
		    + "and date(d.dteBillDate)=date(c.dteBillDate);";
	    i = ExecuteQuery(sql);

	    sql = "update tblvoidbillhd  set strVoidBillType='Bill Void' "
		    + "where strVoidBillType='';";
	    i = ExecuteQuery(sql);

	    sql = "CREATE TABLE `tblpropertyimage` ( "
		    + " `strPOSCode` VARCHAR(10) NOT NULL, "
		    + " `blobReportImage` LONGBLOB NULL, "
		    + " `strClientCode` VARCHAR(20) NOT NULL, "
		    + " PRIMARY KEY (`strPOSCode`, `strClientCode`) "
		    + " ) "
		    + " COLLATE='utf8_general_ci' "
		    + " ENGINE=InnoDB "
		    + ";";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblvoidbillhd` "
		    + "	CHANGE COLUMN `strVoidBillType` `strVoidBillType` VARCHAR(50) NOT NULL DEFAULT '' AFTER `strRemark`;";
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblvoidbilltaxdtl` "
		    + "	DROP PRIMARY KEY;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblcustomermaster` "
		    + "	CHANGE COLUMN `strEmailId` `strEmailId` VARCHAR(100) NOT NULL DEFAULT '' AFTER `dteAnniversary`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbluserhd` "
		    + " ADD COLUMN `strWaiterNo` VARCHAR(10) NOT NULL DEFAULT '' AFTER `strDebitCardString`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblcostcentermaster` "
		    + "	ADD COLUMN `strWSLocationCode` VARCHAR(15) NOT NULL DEFAULT '' AFTER `strLabelOnKOT`, "
		    + "	ADD COLUMN `strWSLocationName` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strWSLocationCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbldayendprocess` "
		    + "	CHANGE COLUMN `strWSStockAdjustmentNo` `strWSStockAdjustmentNo` VARCHAR(500) NOT NULL DEFAULT '' AFTER `dblUnusedDebitCardBalance`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbillhd` "
		    + "	ADD COLUMN `intOrderNo` INT NOT NULL DEFAULT '0' AFTER `dtBillDate`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbillhd` "
		    + "	ADD COLUMN `intOrderNo` INT NOT NULL DEFAULT '0' AFTER `dtBillDate`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblitemrtemp` "
		    + "	ADD COLUMN `strTableStatus` VARCHAR(20) NOT NULL DEFAULT 'Normal' AFTER `strItemPickedUp`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblitemrtemp_bck` "
		    + "	ADD COLUMN `strTableStatus` VARCHAR(20) NOT NULL DEFAULT 'Normal' AFTER `strItemPickedUp`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblstkinhd` "
		    + "	ALTER `strReasonCode` DROP DEFAULT;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblstkinhd` "
		    + "	CHANGE COLUMN `strReasonCode` `strReasonCode` VARCHAR(5) NOT NULL AFTER `dteStkInDate`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblstkouthd` "
		    + " ALTER `strReasonCode` DROP DEFAULT;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblstkouthd` "
		    + " CHANGE COLUMN `strReasonCode` `strReasonCode` VARCHAR(5) NOT NULL AFTER `dteStkOutDate`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblatvreport` "
		    + "	ADD COLUMN `strWaiterName` VARCHAR(50) NULL DEFAULT '' AFTER `dblTAAvg`; ";

	    i = ExecuteQuery(sql);

	    sql = "update tblmenuitempricingdtl a "
		    + "set a.tmeTimeFrom=trim(a.tmeTimeFrom) "
		    + ",a.strAMPMFrom=TRIM(a.strAMPMFrom) "
		    + ",a.tmeTimeTo=trim(a.tmeTimeTo) "
		    + ",a.strAMPMTo=TRIM(a.strAMPMTo);";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblcustomermaster` "
		    + "	ADD COLUMN `strGSTNo` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strTempLandmark`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` "
		    + "	ADD COLUMN `strAutoShowPopItems` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strEnableTableReservationForCustomer`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` "
		    + "	ADD COLUMN `intShowPopItemsOfDays` INT NOT NULL DEFAULT '1' AFTER `strAutoShowPopItems`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` "
		    + "	ADD COLUMN `strPostSalesCostOrLoc` VARCHAR(20) NOT NULL DEFAULT 'Cost Center' AFTER `intShowPopItemsOfDays`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsettelmenthd` "
		    + "	ADD COLUMN `dblThirdPartyComission` DECIMAL(5,2) NOT NULL DEFAULT '0.00' AFTER `strCreditReceiptYN`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblitemrtemp` "
		    + "	ADD COLUMN `strDeviceMACAdd` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strTableStatus`, "
		    + "	ADD COLUMN `strDeviceId` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strDeviceMACAdd`; ";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblitemrtemp_bck` "
		    + "	ADD COLUMN `strDeviceMACAdd` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strTableStatus`, "
		    + "	ADD COLUMN `strDeviceId` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strDeviceMACAdd`; ";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblreasonmaster` ADD COLUMN `strMoveKOT` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strReprint`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsettelmenthd` "
		    + "ADD COLUMN `strComissionType` VARCHAR(5) NOT NULL DEFAULT 'Per' AFTER `dblThirdPartyComission`, "
		    + "ADD COLUMN `strComissionOn` VARCHAR(50) NOT NULL DEFAULT 'Net Amount' AFTER `strComissionType`; ";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbladvbookbillhd` "
		    + "	CHANGE COLUMN `strSettelmentMode` `strSettelmentMode` VARCHAR(50) NOT NULL AFTER `strPOSCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbillhd` "
		    + "	ADD COLUMN `strCRMRewardId` VARCHAR(50) NOT NULL DEFAULT '' AFTER `intOrderNo`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbillhd` "
		    + "	ADD COLUMN `strCRMRewardId` VARCHAR(50) NOT NULL DEFAULT '' AFTER `intOrderNo`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` "
		    + "	ADD COLUMN `strEffectOfSales` VARCHAR(20) NOT NULL DEFAULT '' AFTER `strPostSalesCostOrLoc`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblitemmaster` "
		    + "	ADD COLUMN `strRecipeUOM` VARCHAR(5) NOT NULL DEFAULT '' AFTER `tmeTargetMiss`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblitemmaster` "
		    + "	ADD COLUMN `strReceivedConversion` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `strRecipeUOM`, "
		    + "	ADD COLUMN `strRecipeConversion` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `strReceivedConversion`;";
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbillcomplementrydtl` "
		    + "	CHANGE COLUMN `dteBillDate` `dteBillDate` DATETIME NOT NULL AFTER `dblTaxAmount`;";
	    i = ExecuteQuery(sql);

	    sql = "	ALTER TABLE `tblqbillcomplementrydtl` "
		    + "	CHANGE COLUMN `dteBillDate` `dteBillDate` DATETIME NOT NULL AFTER `dblTaxAmount`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` "
		    + "	ADD COLUMN `strPOSWiseItemToMMSProductLinkUpYN` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strEffectOfSales`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblimportexcel` "
		    + "	ADD COLUMN `strRawMaterial` VARCHAR(5) NOT NULL DEFAULT 'N' AFTER `strUOM`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbladvbookbillhd` "
		    + "	CHANGE COLUMN `strSettelmentMode` `strSettelmentMode` VARCHAR(50) NOT NULL AFTER `strPOSCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqadvbookbillhd` "
		    + "	CHANGE COLUMN `strSettelmentMode` `strSettelmentMode` VARCHAR(50) NOT NULL AFTER `strPOSCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblitemmaster` "
		    + "	CHANGE COLUMN `strUOM` `strUOM` VARCHAR(50) NOT NULL DEFAULT '' AFTER `strUrgentOrder`, "
		    + "	CHANGE COLUMN `strRecipeUOM` `strRecipeUOM` VARCHAR(50) NOT NULL DEFAULT '' AFTER `tmeTargetMiss`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblitemmaster` "
		    + "	CHANGE COLUMN `strReceivedConversion` `dblReceivedConversion` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `strRecipeUOM`, "
		    + "	CHANGE COLUMN `strRecipeConversion` `dblRecipeConversion` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `dblReceivedConversion`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblstkouthd` "
		    + "	ADD COLUMN `strNarration` VARCHAR(300) NOT NULL DEFAULT '' AFTER `strDataPostFlag`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblstkoutdtl` "
		    + "	ADD COLUMN `strParentCode` VARCHAR(20) NOT NULL AFTER `strDataPostFlag`, "
		    + "	ADD COLUMN `strRemark` VARCHAR(100) NOT NULL AFTER `strParentCode`, "
		    + "	ADD COLUMN `strDisplayQty` VARCHAR(100) NOT NULL AFTER `strRemark`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblimportexcel` "
		    + "	ADD COLUMN `strRecipeUOM` VARCHAR(50) NOT NULL DEFAULT '' AFTER `strRawMaterial`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblstkoutdtl` "
		    + "	CHANGE COLUMN `strParentCode` `strParentCode` VARCHAR(20) NOT NULL DEFAULT '' AFTER `strDataPostFlag`, "
		    + "	CHANGE COLUMN `strRemark` `strRemark` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strParentCode`, "
		    + "	CHANGE COLUMN `strDisplayQty` `strDisplayQty` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strRemark`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblitemmaster` "
		    + "	DROP COLUMN `strReceivedConversion`, "
		    + "	DROP COLUMN `strRecipeConversion`;";
	    i = ExecuteQuery(sql);

	    sql = "update tbldayendprocess  "
		    + "set dteDayEndDateTime=dteDateCreated "
		    + "where dteDayEndDateTime IS NULL ;";
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblpurchaseordertaxdtl` "
		    + "	ADD COLUMN `strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strClientCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblstkinhd` "
		    + "	ADD COLUMN `dblTaxAmt` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `strInvoiceCode`, "
		    + "	ADD COLUMN `dblExtraAmt` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `dblTaxAmt`, "
		    + "	ADD COLUMN `dblGrandTotal` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `dblExtraAmt`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblstkouthd` "
		    + "	ADD COLUMN `dblTaxAmt` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `strNarration`, "
		    + "	ADD COLUMN `dblExtraAmt` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `dblTaxAmt`, "
		    + "	ADD COLUMN `dblGrandTotal` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `dblExtraAmt`;";
	    i = ExecuteQuery(sql);

	    sql = "CREATE TABLE IF NOT EXISTS `tblstkintaxdtl` ( "
		    + "	`strStkInCode` VARCHAR(10) NOT NULL, "
		    + "	`strTaxCode` VARCHAR(10) NOT NULL, "
		    + "	`dblTaxableAmt` DECIMAL(18,2) NOT NULL "
		    + ") "
		    + "COLLATE='utf8_general_ci' "
		    + "ENGINE=InnoDB "
		    + ";";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblstkintaxdtl` "
		    + "	CHANGE COLUMN `dblTaxableAmt` `dblTaxableAmt` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `strTaxCode`, "
		    + "	ADD COLUMN `dblTaxAmt` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `dblTaxableAmt`, "
		    + "	ADD COLUMN `strClientCode` VARCHAR(10) NOT NULL DEFAULT '0.00' AFTER `dblTaxAmt`, "
		    + "	ADD PRIMARY KEY (`strStkInCode`, `strTaxCode`, `strClientCode`);";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblstkintaxdtl` "
		    + "	ADD COLUMN `strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strClientCode`;";
	    i = ExecuteQuery(sql);

	    sql = "CREATE TABLE IF NOT EXISTS `tblstkouttaxdtl` ( "
		    + "	`strStkOutCode` VARCHAR(10) NOT NULL, "
		    + "	`strTaxCode` VARCHAR(10) NOT NULL, "
		    + "	`dblTaxableAmt` DECIMAL(18,2) NOT NULL "
		    + ") "
		    + "COLLATE='utf8_general_ci' "
		    + "ENGINE=InnoDB "
		    + ";";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblstkouttaxdtl` "
		    + "	CHANGE COLUMN `dblTaxableAmt` `dblTaxableAmt` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `strTaxCode`, "
		    + "	ADD COLUMN `dblTaxAmt` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `dblTaxableAmt`, "
		    + "	ADD COLUMN `strClientCode` VARCHAR(10) NOT NULL DEFAULT '0.00' AFTER `dblTaxAmt`, "
		    + "	ADD PRIMARY KEY (`strStkOutCode`, `strTaxCode`, `strClientCode`);";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblstkouttaxdtl` "
		    + "	ADD COLUMN `strDataPostFlag` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strClientCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblstkinhd` ADD COLUMN `strSupplierCode` VARCHAR(10) NOT NULL AFTER `dblGrandTotal`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblstkouthd` ADD COLUMN `strSupplierCode` VARCHAR(10) NOT NULL AFTER `dblGrandTotal`;";
	    i = ExecuteQuery(sql);

	    sql = "drop table if exists tbldiscountmaster;";
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

	    sql = "delete from tblrecipehd where length(strItemCode)=0 ";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` "
		    + "	ADD COLUMN `strEnableMasterDiscount` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strPOSWiseItemToMMSProductLinkUpYN`;";
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblitemmaster` "
		    + "ADD COLUMN `strHSNNo` VARCHAR(30) NOT NULL DEFAULT '' AFTER `dblRecipeConversion`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblposmaster`	ADD COLUMN `strPlayZonePOS` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strClientCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblplayzonepricinghd` "
		    + "	ADD COLUMN `intGracePeriod` INT(4) NOT NULL AFTER `intTimeStamp`;";
	    i = ExecuteQuery(sql);

	    sql = " ALTER TABLE `tblplayzonepricingdtl` "
		    + "	ALTER `strClientCode` DROP DEFAULT;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblplayzonepricingdtl` "
		    + "	CHANGE COLUMN `strClientCode` `strClientCode` VARCHAR(10) NOT NULL AFTER `dblGuestPriceSunday`;";
	    i = ExecuteQuery(sql);

	    sql = "update tblplayzonepricingdtl a "
		    + "set strClientCode=(select strClientCode from tblsetup limit 1)";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblplayzonepricingdtl` "
		    + "	ADD PRIMARY KEY (`strPlayZonePricingCode`, `dteFromTime`, `dteToTime`, `strClientCode`);";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblplayzonepricinghd` "
		    + "	ADD PRIMARY KEY (`strPlayZonePricingCode`, `strPosCode`, `strClientCode`);";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblcustomertypemaster` "
		    + "	ADD COLUMN `strPlayZoneCustType` VARCHAR(10) NOT NULL DEFAULT 'Member' AFTER `strDataPostFlag`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblhomedelivery` "
		    + "	CHANGE COLUMN `strDPCode` `strDPCode` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strCustomerCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblimportexcel` "
		    + "	ALTER `strShortName` DROP DEFAULT;";
	    i = ExecuteQuery(sql);

	    sql = "update tblitemmaster set dblReceivedConversion=1.00 "
		    + "where dblReceivedConversion=0.00;";
	    i = ExecuteQuery(sql);

	    sql = "update tblitemmaster set dblRecipeConversion=1.00 "
		    + "where  dblRecipeConversion=0.00;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblplayzonepricinghd` "
		    + "	ADD COLUMN `strAreaCode` VARCHAR(5) NOT NULL AFTER `strUserCreated`;";
	    i = ExecuteQuery(sql);

	    sql = "update tblplayzonepricinghd  "
		    + "set strAreaCode=(select strAreaCode from tblareamaster where strAreaName='All') "
		    + "where strAreaCode='' ";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblstkoutdtl` "
		    + "	ADD COLUMN `dblParentItemQty` DECIMAL(18,2) NOT NULL DEFAULT '0' AFTER `strDisplayQty`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblstkoutdtl` "
		    + "	ADD COLUMN `dblParentItemRate` DECIMAL(18,2) NOT NULL DEFAULT '0' AFTER `dblParentItemQty`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` "
		    + "	ADD COLUMN `strEnableNFCInterface` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strEnableMasterDiscount`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblreasonmaster` "
		    + "	ADD COLUMN `strHashTagLoyalty` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strMoveKOT`, "
		    + "	ADD COLUMN `strOperational` VARCHAR(1) NOT NULL DEFAULT 'Y' AFTER `strHashTagLoyalty`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` "
		    + " ADD COLUMN `strBenowIntegrationYN` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strEnableNFCInterface`, "
		    + " ADD COLUMN `strXEmail` VARCHAR(50) NOT NULL AFTER `strBenowIntegrationYN`, "
		    + " ADD COLUMN `strMerchantCode` VARCHAR(15) NOT NULL AFTER `strXEmail`, "
		    + " ADD COLUMN `strAuthenticationKey` VARCHAR(500) NOT NULL AFTER `strMerchantCode`, "
		    + " ADD COLUMN `strSalt` VARCHAR(20) NOT NULL AFTER `strAuthenticationKey`;";
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbluserhd` "
		    + "	ADD COLUMN `strUserType` VARCHAR(10) NOT NULL DEFAULT '' AFTER `strWaiterNo`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` "
		    + "	ADD COLUMN `strEnableLockTable` VARCHAR(1) NOT NULL DEFAULT 'Y' AFTER `strSalt`;";
	    i = ExecuteQuery(sql);

	    sql = "update tblsuppliermaster  "
		    + "set strSupplierCode=CONCAT('S','0000',right(strSupplierCode,2))  "
		    + "where length(strSupplierCode)<4 and length(strSupplierCode) > 0;";
	    i = ExecuteQuery(sql);

	    sql = "update tblsuppliermaster  "
		    + "set strSupplierCode=CONCAT('S','000',right(strSupplierCode,3))  "
		    + "where length(strSupplierCode)<5 and length(strSupplierCode) > 0;";
	    i = ExecuteQuery(sql);

	    sql = "update tblstkinhd  "
		    + "set strSupplierCode=CONCAT('S','0000',right(strSupplierCode,2))  "
		    + "where length(strSupplierCode)<4 and length(strSupplierCode) > 0;";
	    i = ExecuteQuery(sql);

	    sql = "update tblstkinhd  "
		    + "set strSupplierCode=CONCAT('S','000',right(strSupplierCode,3))  "
		    + "where length(strSupplierCode)<5 and length(strSupplierCode) > 0;";
	    i = ExecuteQuery(sql);

	    sql = "update tblstkouthd  "
		    + "set strSupplierCode=CONCAT('S','0000',right(strSupplierCode,2))  "
		    + "where length(strSupplierCode)<4 and length(strSupplierCode) > 0;";
	    i = ExecuteQuery(sql);

	    sql = "update tblstkouthd  "
		    + "set strSupplierCode=CONCAT('S','000',right(strSupplierCode,3))  "
		    + "where length(strSupplierCode)<5 and length(strSupplierCode) > 0;";
	    i = ExecuteQuery(sql);

	    sql = " update tblmenuitempricingdtl "
		    + "set strCostCenterCode=TRIM(strCostCenterCode);";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` "
		    + "	ADD COLUMN `strHomeDeliveryAreaForDirectBiller` VARCHAR(5) NOT NULL DEFAULT 'A001' AFTER `strEnableLockTable`, "
		    + "	ADD COLUMN `strTakeAwayAreaForDirectBiller` VARCHAR(5) NOT NULL DEFAULT 'A001' AFTER `strHomeDeliveryAreaForDirectBiller` ";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbilldiscdtl` "
		    + "	CHANGE COLUMN `strDiscOnValue` `strDiscOnValue` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strDiscOnType`; ";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblorderhd` "
		    + "	ADD COLUMN `strBillNo` VARCHAR(15) NOT NULL DEFAULT '' AFTER `dblLooseCashAmt`, "
		    + "	ADD COLUMN `strMobileNo` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strBillNo`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblorderhd` "
		    + "	ADD COLUMN `strCustomerName` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strMobileNo`;";
	    i = ExecuteQuery(sql);

	    sql = "CREATE TABLE if not exists `tblstorelastorderno` ( "
		    + "	`strPosCode` VARCHAR(5) NOT NULL, "
		    + "	`longOrderNo` BIGINT NOT NULL, "
		    + "	PRIMARY KEY (`strPosCode`) "
		    + ") "
		    + "COLLATE='utf8_general_ci' "
		    + "ENGINE=InnoDB "
		    + ";";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblcustomermaster` "
		    + "	CHANGE COLUMN `strOfficeAddress` `strOfficeAddress` VARCHAR(100) NOT NULL DEFAULT 'N' AFTER `strClientCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbillhd` "
		    + "	ALTER `dblDiscountAmt` DROP DEFAULT, "
		    + "	ALTER `dblDiscountPer` DROP DEFAULT, "
		    + "	ALTER `dblTaxAmt` DROP DEFAULT, "
		    + "	ALTER `dblSubTotal` DROP DEFAULT, "
		    + "	ALTER `dblGrandTotal` DROP DEFAULT;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbillhd` "
		    + "	CHANGE COLUMN `dblDiscountAmt` `dblDiscountAmt` DECIMAL(18,4) NOT NULL AFTER `strSettelmentMode`, "
		    + "	CHANGE COLUMN `dblDiscountPer` `dblDiscountPer` DECIMAL(10,4) NOT NULL AFTER `dblDiscountAmt`, "
		    + "	CHANGE COLUMN `dblTaxAmt` `dblTaxAmt` DECIMAL(18,4) NOT NULL AFTER `dblDiscountPer`, "
		    + "	CHANGE COLUMN `dblSubTotal` `dblSubTotal` DECIMAL(18,4) NOT NULL AFTER `dblTaxAmt`, "
		    + "	CHANGE COLUMN `dblGrandTotal` `dblGrandTotal` DECIMAL(18,4) NOT NULL AFTER `dblSubTotal`, "
		    + "	CHANGE COLUMN `dblTipAmount` `dblTipAmount` DECIMAL(18,4) NOT NULL DEFAULT '0.00' AFTER `strRemarks`, "
		    + "	CHANGE COLUMN `dblDeliveryCharges` `dblDeliveryCharges` DECIMAL(18,4) NOT NULL DEFAULT '0.00' AFTER `strCounterCode`, "
		    + "	CHANGE COLUMN `dblRoundOff` `dblRoundOff` DECIMAL(18,4) NOT NULL DEFAULT '0.00' AFTER `strJioMoneyCardType`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbillhd` "
		    + "	ALTER `dblDiscountAmt` DROP DEFAULT, "
		    + "	ALTER `dblDiscountPer` DROP DEFAULT, "
		    + "	ALTER `dblTaxAmt` DROP DEFAULT, "
		    + "	ALTER `dblSubTotal` DROP DEFAULT, "
		    + "	ALTER `dblGrandTotal` DROP DEFAULT;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbillhd` "
		    + "	CHANGE COLUMN `dblDiscountAmt` `dblDiscountAmt` DECIMAL(18,4) NOT NULL AFTER `strSettelmentMode`, "
		    + "	CHANGE COLUMN `dblDiscountPer` `dblDiscountPer` DECIMAL(10,4) NOT NULL AFTER `dblDiscountAmt`, "
		    + "	CHANGE COLUMN `dblTaxAmt` `dblTaxAmt` DECIMAL(18,4) NOT NULL AFTER `dblDiscountPer`, "
		    + "	CHANGE COLUMN `dblSubTotal` `dblSubTotal` DECIMAL(18,4) NOT NULL AFTER `dblTaxAmt`, "
		    + "	CHANGE COLUMN `dblGrandTotal` `dblGrandTotal` DECIMAL(18,4) NOT NULL AFTER `dblSubTotal`, "
		    + "	CHANGE COLUMN `dblTipAmount` `dblTipAmount` DECIMAL(18,4) NOT NULL DEFAULT '0.00' AFTER `strRemarks`, "
		    + "	CHANGE COLUMN `dblDeliveryCharges` `dblDeliveryCharges` DECIMAL(18,4) NOT NULL DEFAULT '0.00' AFTER `strCounterCode`, "
		    + "	CHANGE COLUMN `dblRoundOff` `dblRoundOff` DECIMAL(18,4) NOT NULL DEFAULT '0.00' AFTER `strJioMoneyCardType`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbilldtl` "
		    + "	ALTER `dblQuantity` DROP DEFAULT, "
		    + "	ALTER `dblAmount` DROP DEFAULT, "
		    + "	ALTER `dblTaxAmount` DROP DEFAULT;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbilldtl` "
		    + "	CHANGE COLUMN `dblRate` `dblRate` DECIMAL(18,4) NOT NULL DEFAULT '0.00' AFTER `strAdvBookingNo`, "
		    + "	CHANGE COLUMN `dblQuantity` `dblQuantity` DECIMAL(18,4) NOT NULL AFTER `dblRate`, "
		    + "	CHANGE COLUMN `dblAmount` `dblAmount` DECIMAL(18,4) NOT NULL AFTER `dblQuantity`, "
		    + "	CHANGE COLUMN `dblTaxAmount` `dblTaxAmount` DECIMAL(18,4) NOT NULL AFTER `dblAmount`, "
		    + "	CHANGE COLUMN `dblDiscountAmt` `dblDiscountAmt` DECIMAL(18,4) NOT NULL DEFAULT '0.00' AFTER `strWaiterNo`, "
		    + "	CHANGE COLUMN `dblDiscountPer` `dblDiscountPer` DECIMAL(18,4) NOT NULL DEFAULT '0.00' AFTER `dblDiscountAmt`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbilldtl` "
		    + "	ALTER `dblQuantity` DROP DEFAULT, "
		    + "	ALTER `dblAmount` DROP DEFAULT, "
		    + "	ALTER `dblTaxAmount` DROP DEFAULT;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbilldtl` "
		    + "	CHANGE COLUMN `dblRate` `dblRate` DECIMAL(18,4) NOT NULL DEFAULT '0.00' AFTER `strAdvBookingNo`, "
		    + "	CHANGE COLUMN `dblQuantity` `dblQuantity` DECIMAL(18,4) NOT NULL AFTER `dblRate`, "
		    + "	CHANGE COLUMN `dblAmount` `dblAmount` DECIMAL(18,4) NOT NULL AFTER `dblQuantity`, "
		    + "	CHANGE COLUMN `dblTaxAmount` `dblTaxAmount` DECIMAL(18,4) NOT NULL AFTER `dblAmount`, "
		    + "	CHANGE COLUMN `dblDiscountAmt` `dblDiscountAmt` DECIMAL(18,4) NOT NULL DEFAULT '0.00' AFTER `strWaiterNo`, "
		    + "	CHANGE COLUMN `dblDiscountPer` `dblDiscountPer` DECIMAL(18,4) NOT NULL DEFAULT '0.00' AFTER `dblDiscountAmt`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbilldiscdtl` "
		    + "	ALTER `dblDiscAmt` DROP DEFAULT, "
		    + "	ALTER `dblDiscPer` DROP DEFAULT, "
		    + "	ALTER `dblDiscOnAmt` DROP DEFAULT;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbilldiscdtl` "
		    + "	CHANGE COLUMN `dblDiscAmt` `dblDiscAmt` DECIMAL(18,4) NOT NULL AFTER `strPOSCode`, "
		    + "	CHANGE COLUMN `dblDiscPer` `dblDiscPer` DECIMAL(18,4) NOT NULL AFTER `dblDiscAmt`, "
		    + "	CHANGE COLUMN `dblDiscOnAmt` `dblDiscOnAmt` DECIMAL(18,4) NOT NULL AFTER `dblDiscPer`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbillmodifierdtl` "
		    + "	ALTER `dblQuantity` DROP DEFAULT, "
		    + "	ALTER `dblAmount` DROP DEFAULT, "
		    + "	ALTER `dblDiscPer` DROP DEFAULT, "
		    + "	ALTER `dblDiscAmt` DROP DEFAULT;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbillmodifierdtl` "
		    + "	CHANGE COLUMN `dblRate` `dblRate` DECIMAL(18,4) NOT NULL DEFAULT '0.00' AFTER `strModifierName`, "
		    + "	CHANGE COLUMN `dblQuantity` `dblQuantity` DECIMAL(18,4) NOT NULL AFTER `dblRate`, "
		    + "	CHANGE COLUMN `dblAmount` `dblAmount` DECIMAL(18,4) NOT NULL AFTER `dblQuantity`, "
		    + "	CHANGE COLUMN `dblDiscPer` `dblDiscPer` DECIMAL(18,4) NOT NULL AFTER `strSequenceNo`, "
		    + "	CHANGE COLUMN `dblDiscAmt` `dblDiscAmt` DECIMAL(18,4) NOT NULL AFTER `dblDiscPer`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbillmodifierdtl` "
		    + "	ALTER `dblQuantity` DROP DEFAULT, "
		    + "	ALTER `dblAmount` DROP DEFAULT, "
		    + "	ALTER `dblDiscPer` DROP DEFAULT, "
		    + "	ALTER `dblDiscAmt` DROP DEFAULT;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbillmodifierdtl` "
		    + "	CHANGE COLUMN `dblRate` `dblRate` DECIMAL(18,4) NOT NULL DEFAULT '0.00' AFTER `strModifierName`, "
		    + "	CHANGE COLUMN `dblQuantity` `dblQuantity` DECIMAL(18,4) NOT NULL AFTER `dblRate`, "
		    + "	CHANGE COLUMN `dblAmount` `dblAmount` DECIMAL(18,4) NOT NULL AFTER `dblQuantity`, "
		    + "	CHANGE COLUMN `dblDiscPer` `dblDiscPer` DECIMAL(18,4) NOT NULL AFTER `strSequenceNo`, "
		    + "	CHANGE COLUMN `dblDiscAmt` `dblDiscAmt` DECIMAL(18,4) NOT NULL AFTER `dblDiscPer`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbillpromotiondtl` "
		    + "	ALTER `dblQuantity` DROP DEFAULT;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbillpromotiondtl` "
		    + "	CHANGE COLUMN `dblQuantity` `dblQuantity` DECIMAL(18,4) NOT NULL AFTER `strPromotionCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbillsettlementdtl` "
		    + "	ALTER `dblSettlementAmt` DROP DEFAULT;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbillsettlementdtl` "
		    + "	CHANGE COLUMN `dblSettlementAmt` `dblSettlementAmt` DECIMAL(18,4) NOT NULL AFTER `strSettlementCode`, "
		    + "	CHANGE COLUMN `dblPaidAmt` `dblPaidAmt` DECIMAL(18,4) NULL DEFAULT '0.00' AFTER `dblSettlementAmt`, "
		    + "	CHANGE COLUMN `dblActualAmt` `dblActualAmt` DECIMAL(18,4) NOT NULL DEFAULT '0.00' AFTER `strCustomerCode`, "
		    + "	CHANGE COLUMN `dblRefundAmt` `dblRefundAmt` DECIMAL(18,4) NOT NULL DEFAULT '0.00' AFTER `dblActualAmt`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbillsettlementdtl` "
		    + "	ALTER `dblSettlementAmt` DROP DEFAULT;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbillsettlementdtl` "
		    + "	CHANGE COLUMN `dblSettlementAmt` `dblSettlementAmt` DECIMAL(18,4) NOT NULL AFTER `strSettlementCode`, "
		    + "	CHANGE COLUMN `dblPaidAmt` `dblPaidAmt` DECIMAL(18,4) NULL DEFAULT '0.00' AFTER `dblSettlementAmt`, "
		    + "	CHANGE COLUMN `dblActualAmt` `dblActualAmt` DECIMAL(18,4) NOT NULL DEFAULT '0.00' AFTER `strCustomerCode`, "
		    + "	CHANGE COLUMN `dblRefundAmt` `dblRefundAmt` DECIMAL(18,4) NOT NULL DEFAULT '0.00' AFTER `dblActualAmt`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbilltaxdtl` "
		    + "	ALTER `dblTaxableAmount` DROP DEFAULT, "
		    + "	ALTER `dblTaxAmount` DROP DEFAULT;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbilltaxdtl` "
		    + "	CHANGE COLUMN `dblTaxableAmount` `dblTaxableAmount` DECIMAL(18,4) NOT NULL AFTER `strTaxCode`, "
		    + "	CHANGE COLUMN `dblTaxAmount` `dblTaxAmount` DECIMAL(18,4) NOT NULL AFTER `dblTaxableAmount`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbilltaxdtl` "
		    + "	ALTER `dblTaxableAmount` DROP DEFAULT, "
		    + "	ALTER `dblTaxAmount` DROP DEFAULT;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbilltaxdtl` "
		    + "	CHANGE COLUMN `dblTaxableAmount` `dblTaxableAmount` DECIMAL(18,4) NOT NULL AFTER `strTaxCode`, "
		    + "	CHANGE COLUMN `dblTaxAmount` `dblTaxAmount` DECIMAL(18,4) NOT NULL AFTER `dblTaxableAmount`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` "
		    + "	ADD COLUMN `strRoundOffBillFinalAmt` VARCHAR(5) NOT NULL DEFAULT 'Y' AFTER `strTakeAwayAreaForDirectBiller`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` "
		    + "	ADD COLUMN `dblNoOfDecimalPlace` INT(2) NOT NULL DEFAULT '2' AFTER `strRoundOffBillFinalAmt`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbilldiscdtl`   DROP COLUMN `dblAmount`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblcustomermaster` "
		    + "	ADD COLUMN `strDebtorCode` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strGSTNo`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbldayendprocess`"
		    + "	ADD COLUMN `strJVNo` VARCHAR(50) NOT NULL DEFAULT '' AFTER `dblAPC`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblitemrtemp` "
		    + "	ADD COLUMN `strCRMRewardId` VARCHAR(50) NOT NULL DEFAULT '' AFTER `strDeviceId`; ";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblitemrtemp_bck` "
		    + "	ADD COLUMN `strCRMRewardId` VARCHAR(50) NOT NULL DEFAULT '' AFTER `strDeviceId`;";
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbilldtl` "
		    + "	DROP COLUMN `sequenceNo`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbillmodifierdtl` "
		    + "	DROP COLUMN `sequenceNo`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbilldiscdtl` "
		    + "	DROP COLUMN `dblAmount`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblitemcurrentstk` "
		    + "	ADD COLUMN `dblPurchaseRate` DECIMAL(18,4) NOT NULL DEFAULT '0.00' AFTER `intBalance`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbldayendprocess` "
		    + "	ADD COLUMN `strClientCode` VARCHAR(10) NOT NULL DEFAULT '' AFTER `strJVNo`;";
	    i = ExecuteQuery(sql);

	    sql = "update tbldayendprocess set strClientCode='" + clsGlobalVarClass.gClientCode + "' where strClientCode='' ";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` "
		    + "	ADD COLUMN `strSendDBBackupOnClientMail` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `dblNoOfDecimalPlace`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbldayendprocess` "
		    + "	ADD COLUMN `dteDayEndReportsDateTime` DATETIME NULL DEFAULT NULL AFTER `strClientCode`; ";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tbluserhd` "
		    + "	ADD COLUMN `intNoOfDaysReportsView` SMALLINT NOT NULL DEFAULT '0' AFTER `strUserType`; ";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbenowsettlementdtl` "
		    + "	CHANGE COLUMN `strQRString` `strQRString` VARCHAR(500) NOT NULL AFTER `strBillNo`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` "
		    + "	ADD COLUMN `strPrintOrderNoOnBillYN` VARCHAR(1) NOT NULL DEFAULT 'Y' AFTER `strSendDBBackupOnClientMail`, "
		    + "	ADD COLUMN `strPrintDeviceAndUserDtlOnKOTYN` VARCHAR(1) NOT NULL DEFAULT 'Y' AFTER `strPrintOrderNoOnBillYN`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbillseries` "
		    + "	ADD COLUMN `strBillNote` VARCHAR(200) NOT NULL DEFAULT '' AFTER `strPrintInclusiveOfTaxOnBill`;";
	    i = ExecuteQuery(sql);

	    sql = " ALTER TABLE `tblsetup` "
		    + "	ADD COLUMN `strRemoveSCTaxCode` VARCHAR(10) NOT NULL DEFAULT '' AFTER `strPrintDeviceAndUserDtlOnKOTYN`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbillhd` "
		    + "	ADD COLUMN `strNSCTax` VARCHAR(10) NOT NULL DEFAULT '' AFTER `strCRMRewardId`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbillhd` "
		    + "	ADD COLUMN `strNSCTax` VARCHAR(10) NOT NULL DEFAULT '' AFTER `strCRMRewardId`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblvoidkot` "
		    + "	ADD COLUMN `strAreaCode` VARCHAR(10) NOT NULL DEFAULT '' AFTER `strVoidBillType`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` "
		    + "	ADD COLUMN `strAutoAddKOTToBill` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strRemoveSCTaxCode`;";
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` "
		    + "	ADD COLUMN `strAreaWiseCostCenterKOTPrintingYN` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strAutoAddKOTToBill`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblitemmaster` "
		    + "	CHANGE COLUMN `strItemType` `strItemType` VARCHAR(20) NOT NULL DEFAULT 'Food' AFTER `strDataPostFlag`;";
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` "
		    + "	ADD COLUMN `strWERAOnlineOrderIntegration` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strAreaWiseCostCenterKOTPrintingYN`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` "
		    + "	ADD COLUMN `strWERAMerchantOutletId` VARCHAR(10) NOT NULL DEFAULT '' AFTER `strWERAOnlineOrderIntegration`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblsetup` "
		    + "	ADD COLUMN `strWERAAuthenticationAPIKey` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strWERAMerchantOutletId`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblbillhd` "
		    + "	CHANGE COLUMN `strRemarks` `strRemarks` VARCHAR(200) NOT NULL DEFAULT '' AFTER `strReasonCode`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbillhd` "
		    + "	CHANGE COLUMN `strRemarks` `strRemarks` VARCHAR(200) NOT NULL DEFAULT '' AFTER `strReasonCode`;";
	    i = ExecuteQuery(sql);

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
	    i = ExecuteQuery(sql);
	    
	    sql = "ALTER TABLE `tblsetup` "
		    + "	ADD COLUMN `strFireCommunication` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strWERAAuthenticationAPIKey`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblitemrtemp` "
		    + "	ADD COLUMN `dblFiredQty` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `strCRMRewardId`, "
		    + " ADD COLUMN `dblPrintQty` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `dblFiredQty`,"
		    + "	ADD COLUMN `strBillNote` VARCHAR(100) NOT NULL DEFAULT '' AFTER `dblPrintQty`;";
	    i = ExecuteQuery(sql);
	    	    
	    
	     sql = "ALTER TABLE `tblitemrtemp_bck` "
		    + "	ADD COLUMN `dblFiredQty` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `strCRMRewardId`, "
		    + " ADD COLUMN `dblPrintQty` DECIMAL(18,2) NOT NULL DEFAULT '0.00' AFTER `dblFiredQty`,"
		    + "	ADD COLUMN `strBillNote` VARCHAR(100) NOT NULL DEFAULT '' AFTER `dblPrintQty`;";
	    i = ExecuteQuery(sql);
	    	  
	    sql = "ALTER TABLE `tblbillhd` "
		    + "	ADD COLUMN `strKOTToBillNote` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strNSCTax`;";
	    i = ExecuteQuery(sql);

	    sql = "ALTER TABLE `tblqbillhd` "
		    + "	ADD COLUMN `strKOTToBillNote` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strNSCTax`;";
	    i = ExecuteQuery(sql);
	    
	    
	    sql = "ALTER TABLE `tblsetup` "
		    + "	ADD COLUMN `dblUSDConverionRate` DECIMAL(10,2) NOT NULL DEFAULT '0.00' AFTER `strFireCommunication`;";
	    i = ExecuteQuery(sql);

	    sql = "	ALTER TABLE `tblbillhd` "
		    + "	ADD COLUMN `dblUSDConverionRate` DECIMAL(10,2) NOT NULL DEFAULT '0.00' AFTER `strKOTToBillNote`;";
	    i = ExecuteQuery(sql);

	    sql = "	ALTER TABLE `tblqbillhd` "
		    + "	ADD COLUMN `dblUSDConverionRate` DECIMAL(10,2) NOT NULL DEFAULT '0.00' AFTER `strKOTToBillNote`;";
	    i = ExecuteQuery(sql);
	    
	    sql = "ALTER TABLE `tbldebitcardtype` "
		    + "	ADD COLUMN `strSetExpiryTime` VARCHAR(1) NOT NULL DEFAULT 'N' AFTER `strAuthorizeMemberCard`, "
		    + "	ADD COLUMN `intExpiryTime` INT NOT NULL DEFAULT '0' AFTER `strSetExpiryTime`;";
	    i = ExecuteQuery(sql);
	    
	    sql = "ALTER TABLE `tblitemmaster` "
		    + "ADD COLUMN `strOperationalYN` VARCHAR(1) NOT NULL DEFAULT 'Y' AFTER `strHSNNo`;";
	    i = ExecuteQuery(sql);
	    
	    sql ="ALTER TABLE `tblsetup` "
		+"ADD COLUMN `strDBBackupMailReceiver` VARCHAR(500) NOT NULL DEFAULT '' AFTER `dblUSDConverionRate`;";
	    i = ExecuteQuery(sql);
	    
	    sql ="ALTER TABLE `tblnonchargablekot` "
		+"ADD COLUMN `strItemName` VARCHAR(200) NOT NULL AFTER `strPOSCode`, "
		+"ADD COLUMN `strBillNote` VARCHAR(200) NOT NULL DEFAULT '' AFTER `strItemName` ";
	    i = ExecuteQuery(sql);
	    
	   
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    

	    /**
	     * updating tblForms table tblforms structure update
	     */
	    funUpdateTblForms();

	    sql = "CREATE TABLE IF NOT EXISTS `tblsuperuserdtl` ( "
		    + "`strUserCode` varchar(10) NOT NULL, "
		    + "`strFormName` varchar(50) NOT NULL, "
		    + "`strButtonName` varchar(50) NOT NULL, "
		    + "`intSequence` int(11) NOT NULL, "
		    + "`strAdd` char(10) NOT NULL, "
		    + "`strEdit` char(10) NOT NULL, "
		    + "`strDelete` char(10) NOT NULL, "
		    + "`strView` char(10) NOT NULL, "
		    + "`strPrint` char(10) NOT NULL, "
		    + "`strSave` varchar(5) NOT NULL, "
		    + "`strGrant` varchar(5) NOT NULL "
		    + ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
	    i = ExecuteQuery(sql);

	    String sql_Super = "select count(strusercode) from tblsuperuserdtl where strUserCode='SUPER' or strUserCode='super'";
	    ResultSet rsSuper = clsGlobalVarClass.dbMysql.executeResultSet(sql_Super);
	    if (rsSuper.next())
	    {
		if (rsSuper.getInt(1) == 0)
		{
		    sql = "INSERT INTO `tblsuperuserdtl` (`strUserCode`, `strFormName`, `strButtonName`, `intSequence`, `strAdd`, `strEdit`, `strDelete`, `strView`, `strPrint`, `strSave`, `strGrant`) VALUES "
			    + "	('super', 'Settlement', 'Settlement copy', 2, 'true', 'true', 'true', 'true', 'true', 'true', 'true'), "
			    + "	('super', 'Price Menu', 'Price Menu-01', 3, 'true', 'true', 'true', 'true', 'true', 'true', 'true'), "
			    + "	('super', 'User Registration', 'User Registration copy', 4, 'true', 'true', 'true', 'true', 'true', 'true', 'true'), "
			    + "	('super', 'Property Setup', 'Property Setup-01 copy', 5, 'true', 'true', 'true', 'true', 'true', 'true', 'true'), "
			    + "	('super', 'Building Master', 'BuildingMaster', 6, 'true', 'true', 'true', 'true', 'true', 'true', 'true'), "
			    + "	('super', 'Reason Master', 'Reason Master copy', 7, 'true', 'true', 'true', 'true', 'true', 'true', 'true'), "
			    + "	('super', 'Home Delivery Person', 'DeliveryBoy', 8, 'true', 'true', 'true', 'true', 'true', 'true', 'true'), "
			    + "	('super', 'Assign Home Delivery', 'AsignHomeDelivery', 9, 'true', 'true', 'true', 'true', 'true', 'true', 'true'), "
			    + "	('super', 'Cost Center', 'Cost Center', 10, 'true', 'true', 'true', 'true', 'true', 'true', 'true'), "
			    + "	('super', 'Menu Item', 'Menu Item-01 copy', 11, 'true', 'true', 'true', 'true', 'true', 'true', 'true'), "
			    + "	('super', 'Item Modifier', 'Item Modifier', 12, 'true', 'true', 'true', 'true', 'true', 'true', 'true'), "
			    + "	('super', 'Customer Master', 'CustomerMaster', 13, 'true', 'true', 'true', 'true', 'true', 'true', 'true'), "
			    + "	('super', 'Waiter Master', 'WaiterMaster', 14, 'true', 'true', 'true', 'true', 'true', 'true', 'true'), "
			    + "	('super', 'Group', 'Group copy', 15, 'true', 'true', 'true', 'true', 'true', 'true', 'true'), "
			    + "	('super', 'SubGroup', 'Sub Group copy', 16, 'true', 'true', 'true', 'true', 'true', 'true', 'true'), "
			    + "	('super', 'Tax Master', 'Tax Master copy', 17, 'true', 'true', 'true', 'true', 'true', 'true', 'true'), "
			    + "	('super', 'Table Master', 'TableMaster', 18, 'true', 'true', 'true', 'true', 'true', 'true', 'true'), "
			    + "	('super', 'Menu Head', 'Menu Head copy', 19, 'true', 'true', 'true', 'true', 'true', 'true', 'true'), "
			    + "	('super', 'Area Master', 'AreaMaster', 20, 'true', 'true', 'true', 'true', 'true', 'true', 'true'), "
			    + "	('super', 'Shift Master', 'ShiftMaster', 21, 'true', 'true', 'true', 'true', 'true', 'true', 'true'), "
			    + "	('super', 'DebitCardMaster', 'CardTypeMaster', 22, 'true', 'true', 'true', 'true', 'true', 'true', 'true'), "
			    + "	('super', 'DebitCardRegister', 'RegisterDebitCard', 23, 'true', 'true', 'true', 'true', 'true', 'true', 'true'), "
			    + "	('super', 'RechargeDebitCard', 'RechargeCard', 24, 'true', 'true', 'true', 'true', 'true', 'true', 'true'), "
			    + "	('super', 'GiftVoucherMaster', 'giftvoucher', 25, 'true', 'true', 'true', 'true', 'true', 'true', 'true'), "
			    + "	('super', 'TDH', 'TDH', 26, 'true', 'true', 'true', 'true', 'true', 'true', 'true'), "
			    + "	('super', 'CustomerTypeMaster', 'CustomerTypeMaster', 27, 'true', 'true', 'true', 'true', 'true', 'true', 'true'), "
			    + "	('super', 'ReOrderTime', 'reordertime', 28, 'true', 'true', 'true', 'true', 'true', 'true', 'true'), "
			    + "	('super', 'CounterMaster', 'CounterMaster', 29, 'true', 'true', 'true', 'true', 'true', 'true', 'true'), "
			    + "	('super', 'Stock In', 'Settlement copy', 62, 'true', 'true', 'true', 'true', 'true', 'true', 'true'), "
			    + "	('super', 'Stock Out', 'Price Menu-01', 61, 'true', 'true', 'true', 'true', 'true', 'true', 'true'), "
			    + "	('super', 'POS Master', 'User Registration copy', 60, 'true', 'true', 'true', 'true', 'true', 'true', 'true'), "
			    + "	('super', 'Void Bill', 'Property Setup-01 copy', 59, 'true', 'true', 'true', 'true', 'true', 'true', 'true'), "
			    + "	('super', 'Physical Stock Posting', 'BuildingMaster', 58, 'true', 'true', 'true', 'true', 'true', 'true', 'true'), "
			    + "	('super', 'Advance Order', 'Reason Master copy', 57, 'true', 'true', 'true', 'true', 'true', 'true', 'true'), "
			    + "	('super', 'Modify Bill', 'DeliveryBoy', 56, 'true', 'true', 'true', 'true', 'true', 'true', 'true'), "
			    + "	('super', 'Make KOT', 'AsignHomeDelivery', 55, 'true', 'true', 'true', 'true', 'true', 'true', 'true'), "
			    + "	('super', 'Direct Biller', 'Cost Center', 1, 'true', 'true', 'true', 'true', 'true', 'true', 'true'), "
			    + "	('super', 'Make Bill', 'Menu Item-01 copy', 54, 'true', 'true', 'true', 'true', 'true', 'true', 'true'), "
			    + "	('super', 'Advance Booking Receipt', 'Item Modifier', 63, 'true', 'true', 'true', 'true', 'true', 'true', 'true'), "
			    + "	('super', 'Stock Adujstment', 'CustomerMaster', 64, 'true', 'true', 'true', 'true', 'true', 'true', 'true'), "
			    + "	('super', 'SettleBill', 'WaiterMaster', 74, 'true', 'true', 'true', 'true', 'true', 'true', 'true'), "
			    + "	('super', 'Cash Management', 'Group copy', 73, 'true', 'true', 'true', 'true', 'true', 'true', 'true'), "
			    + "	('super', 'Kitchen System', 'Sub Group copy', 72, 'true', 'true', 'true', 'true', 'true', 'true', 'true'), "
			    + "	('super', 'VoidKot', 'Tax Master copy', 71, 'true', 'true', 'true', 'true', 'true', 'true', 'true'), "
			    + "	('super', 'VoidStock', 'TableMaster', 70, 'true', 'true', 'true', 'true', 'true', 'true', 'true'), "
			    + "	('super', 'Move Table', 'Menu Head copy', 69, 'true', 'true', 'true', 'true', 'true', 'true', 'true'), "
			    + "	('super', 'Unsettle Bill', 'AreaMaster', 68, 'true', 'true', 'true', 'true', 'true', 'true', 'true'), "
			    + "	('super', 'StructureUpdate', 'ShiftMaster', 67, 'true', 'true', 'true', 'true', 'true', 'true', 'true'), "
			    + "	('super', 'Move KOT', 'CardTypeMaster', 66, 'true', 'true', 'true', 'true', 'true', 'true', 'true'), "
			    + "	('super', 'SplitBill', 'RegisterDebitCard', 65, 'true', 'true', 'true', 'true', 'true', 'true', 'true'), "
			    + "	('super', 'ReOrder', 'RechargeCard', 53, 'true', 'true', 'true', 'true', 'true', 'true', 'true'), "
			    + "	('super', 'Sales Report', 'Settlement copy', 52, 'true', 'true', 'true', 'true', 'true', 'true', 'true'), "
			    + "	('super', 'Stock Flash Report', 'Price Menu-01', 39, 'true', 'true', 'true', 'true', 'true', 'true', 'true'), "
			    + "	('super', 'Day End', 'User Registration copy', 38, 'true', 'true', 'true', 'true', 'true', 'true', 'true'), "
			    + "	('super', 'Advance Order Flash', 'Property Setup-01 copy', 37, 'true', 'true', 'true', 'true', 'true', 'true', 'true'), "
			    + "	('super', 'Stock In Out Flash', 'BuildingMaster', 36, 'true', 'true', 'true', 'true', 'true', 'true', 'true'), "
			    + "	('super', 'Day End Flash', 'Reason Master copy', 35, 'true', 'true', 'true', 'true', 'true', 'true', 'true'), "
			    + "	('super', 'Reprint', 'DeliveryBoy', 34, 'true', 'true', 'true', 'true', 'true', 'true', 'true'), "
			    + "	('super', 'Bill Wise Report', 'Settlement copy', 33, 'true', 'true', 'true', 'true', 'true', 'true', 'true'), "
			    + "	('super', 'Item Wise Report', 'Price Menu-01', 32, 'true', 'true', 'true', 'true', 'true', 'true', 'true'), "
			    + "	('super', 'Group Wise Report', 'User Registration copy', 31, 'true', 'true', 'true', 'true', 'true', 'true', 'true'), "
			    + "	('super', 'SubGroupWise Report', 'Property Setup-01 copy', 30, 'true', 'true', 'true', 'true', 'true', 'true', 'true'), "
			    + "	('super', 'OperatorWise Report', 'BuildingMaster', 40, 'true', 'true', 'true', 'true', 'true', 'true', 'true'), "
			    + "	('super', 'SettlementWise Report', 'Reason Master copy', 41, 'true', 'true', 'true', 'true', 'true', 'true', 'true'), "
			    + "	('super', 'Void Bill Report', 'DeliveryBoy', 51, 'true', 'true', 'true', 'true', 'true', 'true', 'true'), "
			    + "	('super', 'Cost Centre Report', 'AsignHomeDelivery', 50, 'true', 'true', 'true', 'true', 'true', 'true', 'true'), "
			    + "	('super', 'Tax Wise Report', 'Cost Center', 49, 'true', 'true', 'true', 'true', 'true', 'true', 'true'), "
			    + "	('super', 'Cash Mgmt Report', 'Menu Item-01 copy', 48, 'true', 'true', 'true', 'true', 'true', 'true', 'true'), "
			    + "	('super', 'Audit Flash', 'Item Modifier', 47, 'true', 'true', 'true', 'true', 'true', 'true', 'true'), "
			    + "	('super', 'AvgItemPerBill', 'CustomerMaster', 46, 'true', 'true', 'true', 'true', 'true', 'true', 'true'), "
			    + "	('super', 'AvgPerCover', 'WaiterMaster', 45, 'true', 'true', 'true', 'true', 'true', 'true', 'true'), "
			    + "	('super', 'AvgTicketValue', 'Group copy', 44, 'true', 'true', 'true', 'true', 'true', 'true', 'true'), "
			    + "	('super', 'DebitCardFlashReports', 'Sub Group copy', 43, 'true', 'true', 'true', 'true', 'true', 'true', 'true'), "
			    + "	('super', 'TableStatusReport', 'Tax Master copy', 42, 'true', 'true', 'true', 'true', 'true', 'true', 'true'), "
			    + "	('super', 'ImportExcel', 'ImportExcel', 75, 'true', 'true', 'true', 'true', 'true', 'true', 'true'), "
			    + "	('super', 'PromotionGroupMaster', 'Promotion Group Master', 75, 'true', 'true', 'true', 'true', 'true', 'true', 'true');";

		    i = ExecuteQuery(sql);
		}
	    }
	    rsSuper.close();

	    //update super user detail
	    String sqlSuperUsers = "select a.strUserCode from tbluserhd a where a.strSuperType='super';";
	    ResultSet rsSuperUsers = clsGlobalVarClass.dbMysql.executeResultSet(sqlSuperUsers);
	    StringBuilder sqlInsertQuery = new StringBuilder("INSERT INTO tblsuperuserdtl(strUserCode,strFormName,strButtonName,intSequence,strAdd,strEdit,strDelete,strView,strPrint,strSave,strGrant) VALUES ");
	    boolean flag = true;
	    while (rsSuperUsers.next())
	    {
		String userCode = rsSuperUsers.getString("strUserCode");

		String userForms = "select a.strModuleName,a.strImageName,a.intSequence "
			+ "from tblforms a where a.strModuleName NOT IN (select b.strFormName  from tblsuperuserdtl b where strUserCode='" + userCode + "'  ) ";
		ResultSet rsUserForms = clsGlobalVarClass.dbMysql.executeResultSet(userForms);
		while (rsUserForms.next())
		{
		    if (flag)
		    {
			flag = false;
			sqlInsertQuery.append("('" + userCode + "', '" + rsUserForms.getString("strModuleName") + "','" + rsUserForms.getString("strImageName") + "','" + rsUserForms.getString("intSequence") + "','true', 'true', 'true', 'true', 'true', 'true', 'true') ");
		    }
		    else
		    {
			sqlInsertQuery.append(",('" + userCode + "', '" + rsUserForms.getString("strModuleName") + "','" + rsUserForms.getString("strImageName") + "','" + rsUserForms.getString("intSequence") + "','true', 'true', 'true', 'true', 'true', 'true', 'true') ");
		    }
		}
	    }
	    System.out.println("super->" + sqlInsertQuery);
	    if (sqlInsertQuery.length() > 150)
	    {
		clsGlobalVarClass.dbMysql.execute(sqlInsertQuery.toString());
	    }
	    //updated super user detail

	    sql = "DROP table `vqbillhd`;";
	    i = ExecuteQuery(sql);

	    sql = "DROP table `vqbilldtl`;";
	    i = ExecuteQuery(sql);

	    sql = "DROP table `vqbillsettlementdtl`;";
	    i = ExecuteQuery(sql);

	    sql = "DROP table `vqbillmodifierdtl`;";
	    i = ExecuteQuery(sql);

	    sql = "DROP table `vqbilltaxdtl`;";
	    i = ExecuteQuery(sql);

	    sql = "DROP table `vqbillhddtl`;";
	    i = ExecuteQuery(sql);

	    sql = "DROP table `vqbillhdsettlementdtl`;";
	    i = ExecuteQuery(sql);

	    /*
             * Create View Query
	     */
	    sql = "DROP VIEW `vqbillhd`;";
	    i = ExecuteQuery(sql);

	    sql = "create view vqbillhd as "
		    + "select * from tblbillhd  "
		    + "union all  "
		    + "select * from tblqbillhd;";
	    i = ExecuteQuery(sql);

	    sql = "DROP VIEW `vqbilldtl`;";
	    i = ExecuteQuery(sql);

	    sql = "create view vqbilldtl as "
		    + "select * from tblbilldtl  "
		    + "union  "
		    + "select * from tblqbilldtl;";
	    i = ExecuteQuery(sql);

	    sql = "DROP VIEW `vqbillsettlementdtl`;";
	    i = ExecuteQuery(sql);

	    sql = "create view vqbillsettlementdtl as "
		    + "select * from tblbillsettlementdtl "
		    + "union all  "
		    + "select * from tblqbillsettlementdtl;";
	    i = ExecuteQuery(sql);

	    sql = "DROP VIEW `vqbillmodifierdtl`;";
	    i = ExecuteQuery(sql);

	    sql = "create view vqbillmodifierdtl as "
		    + "select * from tblbillmodifierdtl "
		    + "union  "
		    + "select * from tblqbillmodifierdtl;";
	    i = ExecuteQuery(sql);

	    sql = "DROP VIEW `vqbilltaxdtl`;";
	    i = ExecuteQuery(sql);

	    sql = "create view vqbilltaxdtl as "
		    + "select * from tblbilltaxdtl "
		    + "union  "
		    + "select * from tblqbilltaxdtl;";
	    i = ExecuteQuery(sql);

	    sql = "DROP VIEW `vqbillhddtl`;";
	    i = ExecuteQuery(sql);

	    sql = "create view vqbillhddtl as "
		    + "select a.strBillNo,a.dteBillDate,a.strPOSCode,a.strCustomerCode,b.stritemcode "
		    + ", b.strItemName,b.dblRate, b.dblquantity, b.dblamount,b.dblTaxAmount "
		    + ",a.strOperationType,b.strKOTNo,a.strUserCreated,b.strCounterCode,b.strWaiterNo "
		    + ",a.strAdvBookingNo,b.dblDiscountAmt "
		    + "from tblbillhd a, tblbilldtl b "
		    + "where a.strBillNo = b.strBillNo "
		    + "Union  "
		    + "select a.strBillNo,a.dteBillDate,a.strPOSCode,a.strCustomerCode,b.stritemcode "
		    + ", b.strItemName,b.dblRate, b.dblquantity, b.dblamount,b.dblTaxAmount "
		    + ",a.strOperationType,b.strKOTNo,a.strUserCreated,b.strCounterCode,b.strWaiterNo "
		    + ",a.strAdvBookingNo,b.dblDiscountAmt "
		    + "from tblqbillhd a, tblqbilldtl b "
		    + "where a.strBillNo = b.strBillNo ";

	    i = ExecuteQuery(sql);

	    sql = "DROP VIEW `vqbillhdsettlementdtl`;";
	    i = ExecuteQuery(sql);

	    sql = "create view vqbillhdsettlementdtl as  "
		    + "select a.strBillNo,a.dteBillDate,a.strPOSCode,b.strSettlementCode,b.dblSettlementAmt "
		    + ",a.strUserCreated,a.dteDateCreated,a.strOperationType,a.dblGrandTotal,a.dblDiscountAmt"
		    + ",a.strCustomerCode   "
		    + "from tblbillhd a, tblbillsettlementdtl b  "
		    + "where a.strBillNo = b.strBillNo  "
		    + "Union  "
		    + "select a.strBillNo,a.dteBillDate,a.strPOSCode,b.strSettlementCode,b.dblSettlementAmt "
		    + ",a.strUserCreated,a.dteDateCreated,a.strOperationType,a.dblGrandTotal,a.dblDiscountAmt  "
		    + ",a.strCustomerCode   "
		    + "from tblqbillhd a, tblqbillsettlementdtl b  "
		    + "where a.strBillNo = b.strBillNo ";
	    i = ExecuteQuery(sql);

	    sql = "DROP VIEW `vqadvbookdtl`;";
	    i = ExecuteQuery(sql);

	    sql = "create view vqadvbookdtl as "
		    + "select * from tbladvbookbilldtl  "
		    + "union  "
		    + "select * from tblqadvbookbilldtl";
	    i = ExecuteQuery(sql);

	    sql = "DROP VIEW `vqadvbookhd`;";
	    i = ExecuteQuery(sql);

	    sql = "create view vqadvbookhd as "
		    + "select * from tbladvbookbillhd  "
		    + "union "
		    + "select * from tblqadvbookbillhd;";
	    i = ExecuteQuery(sql);

	    sql = "DROP VIEW `vqadvreceiptdtl`;";
	    i = ExecuteQuery(sql);

	    sql = "create view vqadvreceiptdtl as "
		    + "select * from tbladvancereceiptdtl  "
		    + "union "
		    + "select * from tblqadvancereceiptdtl;";
	    i = ExecuteQuery(sql);

	    sql = "DROP VIEW `vqadvreceipthd`;";
	    i = ExecuteQuery(sql);

	    sql = "create view vqadvreceipthd as "
		    + "select * from tbladvancereceipthd  "
		    + "union "
		    + "select * from tblqadvancereceipthd;";
	    i = ExecuteQuery(sql);

	    sql = "create view vqadvordermodifierdtl as "
		    + "select * from tbladvordermodifierdtl  "
		    + "union  "
		    + "select * from tblqadvordermodifierdtl ";
	    i = ExecuteQuery(sql);

	    sql = "UPDATE tblforms SET strImageName='imgSalesReport', "
		    + "strColorImageName='imgSalesReport1' WHERE  strFormName='frmSalesReport';";
	    i = ExecuteQuery(sql);

	    funCheckInternalTable();
	    funCheckMasterTableStatus();

	    //JOptionPane.showMessageDialog(null, "Update Successfully");                      
	    frmOkPopUp objOkPopUp = new frmOkPopUp(null, "Updated Successfully", "Success!!", 2);
	    objOkPopUp.setVisible(true);

	}
	catch (Exception s)
	{
	    frmOkPopUp objOkPopUp = new frmOkPopUp(null, "Error In Strucure Update", "Error!!", 2);
	    objOkPopUp.setVisible(true);

	    s.printStackTrace();
	}
	finally
	{
	    return 1;
	}
    }

    private void funCheckInternalTable()
    {
	funCheckMasterEntry("KOTNo");
	funCheckMasterEntry("stockInNo");
	funCheckMasterEntry("stockOutNo");
	funCheckMasterEntry("Area");
	funCheckMasterEntry("GiftVoucher");
	funCheckMasterEntry("Counter");
	funCheckMasterEntry("custtype");
	funCheckMasterEntry("Recipe");
	funCheckMasterEntry("LoyaltyCode");
	funCheckMasterEntry("Physicalstock");
	funCheckMasterEntry("Production");
	funCheckMasterEntry("AdvOrderType");
	funCheckMasterEntry("DebitCard");
	funCheckMasterEntry("DelBoyCategory");
	funCheckMasterEntry("Zone");
	funCheckMasterEntry("AdvReceipt");
	funCheckMasterEntry("RechargeNo");
	funCheckMasterEntry("RedeemNo");
	funCheckMasterEntry("CardNo");
	funCheckMasterEntry("MIReceiptNo");
	funCheckMasterEntry("PlaceOrder");
	funCheckMasterEntry("CreditReceipt");
	funCheckMasterEntry("PromoGroup");
	funCheckMasterEntry("LiquorBillCustomer");
	funCheckMasterEntry("OrderNo");
	funCheckMasterEntry("PlayZonePricing");

    }

    private int funCheckMasterEntry(String masterName)
    {

	try
	{
	    String sql = "select count(*) from tblinternal "
		    + "where strTransactionType='" + masterName + "'";
	    ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rs.next())
	    {
		if (rs.getInt(1) == 0)
		{
		    String sql_Insert = "insert into tblinternal(strTransactionType,dblLastNo) "
			    + "values('" + masterName + "',0)";
		    clsGlobalVarClass.dbMysql.execute(sql_Insert);
		    if (clsGlobalVarClass.gClientCode.equals("024.001"))
		    {
			clsGlobalVarClass.dbMysql.execute("update tblinternal set dblLastNo=9856 where strTransactionType='AdvReceipt'");
		    }
		}
	    }
	    rs.close();

	}
	catch (Exception ex)
	{
	    ex.printStackTrace();
	}
	return 1;
    }

    private void funCheckMasterTableStatus()
    {
	funCheckMasterTableEntry("Area");
	funCheckMasterTableEntry("AreaWiseDC");
	funCheckMasterTableEntry("Building");
	funCheckMasterTableEntry("Counter");
	funCheckMasterTableEntry("MenuItemPricing");
	funCheckMasterTableEntry("MenuItem");
	funCheckMasterTableEntry("Menu");
	funCheckMasterTableEntry("Modifier");
	funCheckMasterTableEntry("Group");
	funCheckMasterTableEntry("SubGroup");
	funCheckMasterTableEntry("CostCenter");
	funCheckMasterTableEntry("Settlement");
	funCheckMasterTableEntry("Tax");
	funCheckMasterTableEntry("Table");
	funCheckMasterTableEntry("Waiter");
	funCheckMasterTableEntry("Reason");
	funCheckMasterTableEntry("Customer");
	funCheckMasterTableEntry("CustomerType");
	funCheckMasterTableEntry("DeliveryBoy");
	funCheckMasterTableEntry("DelBoyCat");
	funCheckMasterTableEntry("AdvanceOrderType");
	funCheckMasterTableEntry("User");
	funCheckMasterTableEntry("Promotion");
	funCheckMasterTableEntry("Order");
	funCheckMasterTableEntry("Characteristics");
	funCheckMasterTableEntry("Factory");
	funCheckMasterTableEntry("PosWiseItemWiseIncentive");
	funCheckMasterTableEntry("PromoGroup");
	funCheckMasterTableEntry("DiscountMaster");
    }

    private int funCheckMasterTableEntry(String tableName)
    {
	try
	{
	    String sql = "select count(*) from tblmasteroperationstatus "
		    + " where strTableName='" + tableName + "' ";
	    ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    if (rs.next())
	    {
		if (rs.getInt(1) == 0)
		{
		    String sqlInsert = "insert into tblmasteroperationstatus(strTableName,dteDateEdited,strClientCode) "
			    + "values('" + tableName + "','" + clsGlobalVarClass.gPOSDateForTransaction + "','" + clsGlobalVarClass.gClientCode + "')";
		    clsGlobalVarClass.dbMysql.execute(sqlInsert);
		}
	    }
	    rs.close();

	}
	catch (Exception ex)
	{
	    ex.printStackTrace();
	}
	return 1;
    }

    private String funGenerateAreaCode()
    {
	String areaCode = "";
	try
	{
	    String sql_Area = "select count(dblLastNo) from tblinternal where strTransactionType='Area'";
	    ResultSet rsAreaCode = clsGlobalVarClass.dbMysql.executeResultSet(sql_Area);
	    rsAreaCode.next();
	    int areaCodeCnt = rsAreaCode.getInt(1);
	    rsAreaCode.close();
	    if (areaCodeCnt > 0)
	    {
		sql_Area = "select dblLastNo from tblinternal where strTransactionType='Area'";
		rsAreaCode = clsGlobalVarClass.dbMysql.executeResultSet(sql_Area);
		rsAreaCode.next();
		long code = rsAreaCode.getLong(1);
		code = code + 1;
		areaCode = "A" + String.format("%03d", code);
		sql_Area = "update tblinternal set dblLastNo='" + code + "' where strTransactionType='Area'";
		clsGlobalVarClass.dbMysql.execute(sql_Area);
	    }
	    else
	    {
		areaCode = "A001";
		sql_Area = "insert into tblinternal values('Area'," + 1 + ")";
		clsGlobalVarClass.dbMysql.execute(sql_Area);
	    }
	    //System.out.println("A Code="+areaCode);
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	return areaCode;
    }

    private void funCreateAreaForAll() throws Exception
    {
	String areaCode = funGenerateAreaCode();

	ResultSet rsArea = clsGlobalVarClass.dbMysql.executeResultSet("select count(strAreaCode) from tblareamaster where strAreaName=''");
	if (rsArea.next())
	{
	    if (rsArea.getInt(1) > 0)
	    {
		clsGlobalVarClass.dbMysql.execute("delete from tblareamaster where strAreaName=''");
	    }
	}
	rsArea.close();

	String sql_Area = "insert into tblareamaster (strAreaCode,strAreaName,strUserCreated,strUserEdited,dteDateCreated,dteDateEdited)values('" + areaCode + "'"
		+ ",'All','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.gUserCode + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "')";
	//System.out.println(sql);
	clsGlobalVarClass.dbMysql.execute(sql_Area);
    }

    public final int ExecuteQuery(String Query)
    {
	try
	{
	    clsGlobalVarClass.dbMysql.execute(Query);
	}
	catch (Exception e)
	{
	    // e.printStackTrace();
	}
	return 0;
    }

    public int upcode()
    {
	return uc;
    }

    private void funStructureUpdateForTblItemrTemp()
    {
	try
	{
	    String sql = "select strSerialNo from tblitemrtemp ";
	    ResultSet rsIsExistsTable = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    //            
	    //insert itemrtemp  data to itemrtempbackup
	    sql = "truncate tblitemrtemp_bck";
	    ExecuteQuery(sql);
	    //insert itemrtemp  data to itemrtempbackup
	    sql = "insert into tblitemrtemp_bck(select * from tblitemrtemp)";
	    ExecuteQuery(sql);

	}
	catch (Exception e)
	{
	    //
	    String sql = "CREATE TABLE if not exists `tblitemrtemp` ( "
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
		    + "	`dblTaxAmt` DECIMAL(18,2) NOT NULL DEFAULT '0.00',"
		    + " `strReason` VARCHAR(50) NOT NULL DEFAULT 'No', "
		    + "	PRIMARY KEY (`strTableNo`, `strItemCode`, `strKOTNo`,`strItemName`) "
		    + " ) "
		    + "COLLATE='utf8_general_ci' "
		    + "ENGINE=InnoDB "
		    + ";";
	    int i = ExecuteQuery(sql);
	    //            
	    //insert itemrtempbackup data to itemrtemp
	    sql = "insert into tblitemrtemp(select * from tblitemrtemp_bck)";
	    i = ExecuteQuery(sql);
	    //            
	}
	finally
	{

	}
    }

    private void funForButtonSequenceStrucureUpdate() throws Exception
    {
	String sql = "DROP TABLE IF EXISTS `tbltransbtnsequence`;";
	int i = ExecuteQuery(sql);

	sql = "CREATE TABLE IF NOT EXISTS `tblbuttonsequence` ( "
		+ "  `strPOSCode` varchar(15) NOT NULL, "
		+ "  `strTransactionName` varchar(15) NOT NULL, "
		+ "  `strButtonName` varchar(50) NOT NULL, "
		+ "  `intSeqNo` int(11) NOT NULL, "
		+ "  `strUserCreated` varchar(10) NOT NULL, "
		+ "  `strUserEdited` varchar(10) NOT NULL, "
		+ "  `dteDateCreated` datetime NOT NULL, "
		+ "  `dteDateEdited` datetime NOT NULL, "
		+ "  `strClientCode` varchar(10) NOT NULL, "
		+ "  `strDataPostFlag` varchar(1) NOT NULL DEFAULT 'N', "
		+ "  PRIMARY KEY (`strPOSCode`,`strTransactionName`,`strButtonName`,`strClientCode`) "
		+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
	i = ExecuteQuery(sql);

	sql = "select count(*) FROM tblbuttonsequence";
	ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	int count = 0;
	if (rs.next())
	{
	    rs.getInt(1);
	}
	rs.close();
	if (count == 0)
	{
	    sql = "INSERT INTO `tblbuttonsequence` (`strPOSCode`, `strTransactionName`, `strButtonName`, `intSeqNo`, `strUserCreated`, `strUserEdited`, `dteDateCreated`, `dteDateEdited`, `strClientCode`, `strDataPostFlag`) "
		    + "VALUES "
		    + "('All', 'DirectBiller', 'Customer History', 2, '" + clsGlobalVarClass.gUserCode + "', '" + clsGlobalVarClass.gUserCode + "', '" + clsGlobalVarClass.getCurrentDateTime() + "', '" + clsGlobalVarClass.getCurrentDateTime() + "', '" + clsGlobalVarClass.gClientCode + "', 'N'), "
		    + "('All', 'DirectBiller', 'Delivery Boy', 4, '" + clsGlobalVarClass.gUserCode + "', '" + clsGlobalVarClass.gUserCode + "', '" + clsGlobalVarClass.getCurrentDateTime() + "', '" + clsGlobalVarClass.getCurrentDateTime() + "', '" + clsGlobalVarClass.gClientCode + "', 'N'), "
		    + "('All', 'DirectBiller', 'Done', 6, '" + clsGlobalVarClass.gUserCode + "', '" + clsGlobalVarClass.gUserCode + "', '" + clsGlobalVarClass.getCurrentDateTime() + "', '" + clsGlobalVarClass.getCurrentDateTime() + "', '" + clsGlobalVarClass.gClientCode + "', 'N'), "
		    + "('All', 'DirectBiller', 'Home', 1, '" + clsGlobalVarClass.gUserCode + "', '" + clsGlobalVarClass.gUserCode + "', '" + clsGlobalVarClass.getCurrentDateTime() + "', '" + clsGlobalVarClass.getCurrentDateTime() + "', '" + clsGlobalVarClass.gClientCode + "', 'N'), "
		    + "('All', 'DirectBiller', 'Home Delivery', 3, '" + clsGlobalVarClass.gUserCode + "', '" + clsGlobalVarClass.gUserCode + "', '" + clsGlobalVarClass.getCurrentDateTime() + "', '" + clsGlobalVarClass.getCurrentDateTime() + "', '" + clsGlobalVarClass.gClientCode + "', 'N'), "
		    + "('All', 'DirectBiller', 'Take Away', 5, '" + clsGlobalVarClass.gUserCode + "', '" + clsGlobalVarClass.gUserCode + "', '" + clsGlobalVarClass.getCurrentDateTime() + "', '" + clsGlobalVarClass.getCurrentDateTime() + "', '" + clsGlobalVarClass.gClientCode + "', 'N'), "
		    + "('All', 'MakeKOT', 'Home', 1, '" + clsGlobalVarClass.gUserCode + "', '" + clsGlobalVarClass.gUserCode + "', '" + clsGlobalVarClass.getCurrentDateTime() + "', '" + clsGlobalVarClass.getCurrentDateTime() + "', '" + clsGlobalVarClass.gClientCode + "', 'N'), "
		    + "('All', 'MakeKOT', 'Settle Bill', 2, '" + clsGlobalVarClass.gUserCode + "', '" + clsGlobalVarClass.gUserCode + "', '" + clsGlobalVarClass.getCurrentDateTime() + "', '" + clsGlobalVarClass.getCurrentDateTime() + "', '" + clsGlobalVarClass.gClientCode + "', 'N'), "
		    + "('All', 'MakeKOT', 'Home Delivery', 3, '" + clsGlobalVarClass.gUserCode + "', '" + clsGlobalVarClass.gUserCode + "', '" + clsGlobalVarClass.getCurrentDateTime() + "', '" + clsGlobalVarClass.getCurrentDateTime() + "', '" + clsGlobalVarClass.gClientCode + "', 'N'), "
		    + "('All', 'MakeKOT', 'Delivery Boy', 4, '" + clsGlobalVarClass.gUserCode + "', '" + clsGlobalVarClass.gUserCode + "', '" + clsGlobalVarClass.getCurrentDateTime() + "', '" + clsGlobalVarClass.getCurrentDateTime() + "', '" + clsGlobalVarClass.gClientCode + "', 'N'), "
		    + "('All', 'MakeKOT', 'Check KOT', 5, '" + clsGlobalVarClass.gUserCode + "', '" + clsGlobalVarClass.gUserCode + "', '" + clsGlobalVarClass.getCurrentDateTime() + "', '" + clsGlobalVarClass.getCurrentDateTime() + "', '" + clsGlobalVarClass.gClientCode + "', 'N'), "
		    + "('All', 'MakeKOT', 'NC KOT', 6, '" + clsGlobalVarClass.gUserCode + "', '" + clsGlobalVarClass.gUserCode + "', '" + clsGlobalVarClass.getCurrentDateTime() + "', '" + clsGlobalVarClass.getCurrentDateTime() + "', '" + clsGlobalVarClass.gClientCode + "', 'N'), "
		    + "('All', 'MakeKOT', 'Customer', 7, '" + clsGlobalVarClass.gUserCode + "', '" + clsGlobalVarClass.gUserCode + "', '" + clsGlobalVarClass.getCurrentDateTime() + "', '" + clsGlobalVarClass.getCurrentDateTime() + "', '" + clsGlobalVarClass.gClientCode + "', 'N'), "
		    + "('All', 'MakeKOT', 'Done', 8, '" + clsGlobalVarClass.gUserCode + "', '" + clsGlobalVarClass.gUserCode + "', '" + clsGlobalVarClass.getCurrentDateTime() + "', '" + clsGlobalVarClass.getCurrentDateTime() + "', '" + clsGlobalVarClass.gClientCode + "', 'N'), "
		    + "('All', 'MakeKOT', 'Make Bill', 9, '" + clsGlobalVarClass.gUserCode + "', '" + clsGlobalVarClass.gUserCode + "', '" + clsGlobalVarClass.getCurrentDateTime() + "', '" + clsGlobalVarClass.getCurrentDateTime() + "', '" + clsGlobalVarClass.gClientCode + "', 'N'); ";

	    i = ExecuteQuery(sql);
	}
	else
	{
	    //don't do strucure update
	}

    }

    private void funInsertTaxObGroupData() throws Exception
    {
	String sql = "select count(*) FROM tbltaxongroup";
	ResultSet rs = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	int count = 0;
	if (rs.next())
	{
	    count = rs.getInt(1);
	}
	rs.close();
	if (count == 0)
	{

	    sql = "select a.strTaxCode,a.strTaxDesc,a.dteValidFrom,a.dteValidTo from tbltaxhd a ; ";
	    ResultSet rsTaxes = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rsTaxes.next())
	    {
		boolean isFirst = true;
		String taxCode = rsTaxes.getString(1);
		String fromDate = rsTaxes.getString(3);
		String toDate = rsTaxes.getString(4);

		String insertSql = "INSERT INTO tbltaxongroup "
			+ "(`strTaxCode`, `strGroupCode`, `strGroupName`, `strApplicable`, `dteFrom`, `dteTo` "
			+ ", `strUserCreated`, `strUserEdited`, `dteDateCreated`, `dteDateEdited`, `strClientCode`) "
			+ " VALUES ";

		sql = "select a.strGroupCode,a.strGroupName "
			+ "from tblgrouphd a ";

		ResultSet rsGroups = clsGlobalVarClass.dbMysql.executeResultSet(sql);
		while (rsGroups.next())
		{
		    String groupCode = rsGroups.getString(1);
		    String groupName = rsGroups.getString(2);
		    if (isFirst)
		    {
			insertSql += "('" + taxCode + "', '" + groupCode + "', '" + groupName + "', 'true', '" + fromDate + "', '" + toDate + "', '" + clsGlobalVarClass.gUserCode + "'"
				+ ", '" + clsGlobalVarClass.gUserCode + "' "
				+ ",'" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.gClientCode + "' )";

			isFirst = false;
		    }
		    else
		    {
			insertSql += ",('" + taxCode + "', '" + groupCode + "', '" + groupName + "', 'true', '" + fromDate + "', '" + toDate + "', '" + clsGlobalVarClass.gUserCode + "'"
				+ ", '" + clsGlobalVarClass.gUserCode + "' "
				+ ",'" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.getCurrentDateTime() + "','" + clsGlobalVarClass.gClientCode + "' )";

		    }
		}
		rsGroups.close();
		if (!isFirst)
		{
		    ExecuteQuery(insertSql);
		}
	    }
	    rsTaxes.close();
	}
	else
	{
	    //don't do strucure update
	}
    }

    private void funAddIndexing()
    {
	String sql = "ALTER TABLE `tblbillcomplementrydtl` "
		+ "	ADD INDEX `strBillNo` (`strBillNo`);";
	int i = ExecuteQuery(sql);

	sql = "	ALTER TABLE `tblqbillcomplementrydtl` "
		+ "	ADD INDEX `strBillNo` (`strBillNo`);";
	i = ExecuteQuery(sql);

	sql = "	ALTER TABLE `tblbilldiscdtl` "
		+ "	ADD INDEX `strBillNo` (`strBillNo`);";
	i = ExecuteQuery(sql);

	sql = "ALTER TABLE `tblqbilldiscdtl` "
		+ "	ADD INDEX `strBillNo` (`strBillNo`);";
	i = ExecuteQuery(sql);

	sql = "	ALTER TABLE `tblbilldtl` "
		+ "	ADD INDEX `strBillNo` (`strBillNo`);";
	i = ExecuteQuery(sql);

	sql = "	ALTER TABLE `tblqbilldtl` "
		+ "	ADD INDEX `strBillNo` (`strBillNo`);";
	i = ExecuteQuery(sql);

	sql = "	ALTER TABLE `tblbillpromotiondtl` "
		+ "	ADD INDEX `strBillNo` (`strBillNo`);";
	i = ExecuteQuery(sql);

	sql = "ALTER TABLE `tblqbillpromotiondtl` "
		+ "	ADD INDEX `strBillNo` (`strBillNo`);";
	i = ExecuteQuery(sql);

	sql = "	ALTER TABLE `tblbillseries` "
		+ "	ADD INDEX `strBillSeries` (`strBillSeries`);";
	i = ExecuteQuery(sql);

	sql = "	ALTER TABLE `tblvoidbillhd` "
		+ "	ADD INDEX `strBillNo` (`strBillNo`);";
	i = ExecuteQuery(sql);

	sql = "	ALTER TABLE `tblvoidbilldtl` "
		+ "	ADD INDEX `strBillNo` (`strBillNo`);";
	i = ExecuteQuery(sql);

	sql = "	ALTER TABLE `tblvoidbillsettlementdtl` "
		+ "	ADD INDEX `strBillNo` (`strBillNo`);";
	i = ExecuteQuery(sql);

	sql = "	ALTER TABLE `tblhomedeldtl` "
		+ "	ADD INDEX `strBillNo` (`strBillNo`);";
	i = ExecuteQuery(sql);

	sql = "	ALTER TABLE `tbladvancereceiptdtl` "
		+ "	ADD INDEX `strReceiptNo` (`strReceiptNo`);";
	i = ExecuteQuery(sql);

	sql = "	ALTER TABLE `tbladvbooktaxdtl` "
		+ "	ADD INDEX `strAdvBookingNo` (`strAdvBookingNo`);";
	i = ExecuteQuery(sql);

	sql = "ALTER TABLE `tbltempomedelv` "
		+ "	ADD INDEX `strCustomerCode` (`strCustomerCode`);";
	i = ExecuteQuery(sql);

	sql = "	ALTER TABLE `tbladvancebookingtemp` "
		+ "	ADD INDEX `strCustomerCode` (`strCustomerCode`);";
	i = ExecuteQuery(sql);

	sql = "	ALTER TABLE `tbladvbooktaxtemp` "
		+ "	ADD INDEX `strTaxCode` (`strTaxCode`); "
		+ "	";
	i = ExecuteQuery(sql);

	sql = "	ALTER TABLE `tblareawisedelboywisecharges` "
		+ "	ADD INDEX `strCustAreaCode` (`strCustAreaCode`);";
	i = ExecuteQuery(sql);

	sql = "	ALTER TABLE `tblatvreport` "
		+ "	ADD INDEX `strPosCode` (`strPosCode`); "
		+ "	";
	i = ExecuteQuery(sql);

	sql = "	ALTER TABLE `tblaudit` "
		+ "	ADD INDEX `strDocNo` (`strDocNo`);";
	i = ExecuteQuery(sql);

	sql = "	ALTER TABLE `tblbuypromotiondtl` "
		+ "	ADD INDEX `strPromoCode` (`strPromoCode`);";
	i = ExecuteQuery(sql);

	sql = "	ALTER TABLE `tblcashiermanagement` "
		+ "	ADD INDEX `strTransactionId` (`strTransactionId`);";
	i = ExecuteQuery(sql);

	sql = "ALTER TABLE `tblcharactersticsmaster` "
		+ "	ADD INDEX `strCharCode` (`strCharCode`);";
	i = ExecuteQuery(sql);

	sql = "	ALTER TABLE `tblcharvalue` "
		+ "	ADD INDEX `strCharCode` (`strCharCode`);";
	i = ExecuteQuery(sql);

	sql = "	ALTER TABLE `tbldayendprocess` "
		+ "	ADD INDEX `strPOSCode_dtePOSDate` (`strPOSCode`, `dtePOSDate`);";
	i = ExecuteQuery(sql);

	sql = "	ALTER TABLE `tbldebitcardbilldetails` "
		+ "	ADD INDEX `strBillNo_strCardNo` (`strBillNo`, `strCardNo`);";
	i = ExecuteQuery(sql);

	sql = "ALTER TABLE `tbldebitcardmaster` "
		+ "	ADD INDEX `strCardTypeCode_strCardNo` (`strCardTypeCode`, `strCardNo`);";
	i = ExecuteQuery(sql);

	sql = "	ALTER TABLE `tbldebitcardrevenue` "
		+ "	ADD INDEX `strCardNo` (`strCardNo`);";
	i = ExecuteQuery(sql);

	sql = "ALTER TABLE `tbldebitcardsettlementdtl` "
		+ "	ADD INDEX `strSettlementCode` (`strSettlementCode`);";
	i = ExecuteQuery(sql);

	sql = "	ALTER TABLE `tbldebitcardtabletemp` "
		+ "	ADD INDEX `strCardNo` (`strCardNo`);";
	i = ExecuteQuery(sql);

	sql = "ALTER TABLE `tbldebitcardtype` "
		+ "	ADD INDEX `strCardTypeCode` (`strCardTypeCode`);";
	i = ExecuteQuery(sql);

	sql = "	ALTER TABLE `tbldeliveryboycategorymaster` "
		+ "	ADD INDEX `strDelBoyCategoryCode` (`strDelBoyCategoryCode`);";
	i = ExecuteQuery(sql);

	sql = "ALTER TABLE `tblforms` "
		+ "	ADD INDEX `strFormName` (`strFormName`);";
	i = ExecuteQuery(sql);

	sql = "ALTER TABLE `tblimportexcel` "
		+ "	ADD INDEX `strItemName` (`strItemName`);";
	i = ExecuteQuery(sql);

	sql = "ALTER TABLE `tblinternal` "
		+ "	ADD INDEX `strTransactionType` (`strTransactionType`);";
	i = ExecuteQuery(sql);

	sql = "ALTER TABLE `tblkottaxdtl` "
		+ "	ADD INDEX `strKOTNo` (`strKOTNo`);";
	i = ExecuteQuery(sql);

	sql = "	ALTER TABLE `tbllaststoreadvbookingbill` "
		+ "	ADD INDEX `strPosCode` (`strPosCode`);";
	i = ExecuteQuery(sql);

	sql = "ALTER TABLE `tbllinevoid` "
		+ "	ADD INDEX `strPosCode` (`strPosCode`);";
	i = ExecuteQuery(sql);

	sql = "ALTER TABLE `tblorderanalysis` "
		+ "	ADD INDEX `strField1` (`strField1`);";
	i = ExecuteQuery(sql);

	sql = "	ALTER TABLE `tbltempsalesflash` "
		+ "	ADD INDEX `strcode` (`strcode`);";
	i = ExecuteQuery(sql);

	sql = "ALTER TABLE `tbltempsalesflash1` "
		+ "	ADD INDEX `strbillno` (`strbillno`);";
	i = ExecuteQuery(sql);

    }

    private void funUpdateBillDate()
    {
	String sql = "  update tblbilldiscdtl a  "
		+ " join tblbillhd b on a.strBillNo=b.strBillNo and a.strClientCode=b.strClientCode  "
		+ " set a.dteBillDate=b.dteBillDate "
		+ " where a.dteBillDate='0000-00-00 00:00:00' ;";
	int i = ExecuteQuery(sql);

	sql = "  update tblbillmodifierdtl a  "
		+ " join tblbillhd b on a.strBillNo=b.strBillNo and a.strClientCode=b.strClientCode  "
		+ " set a.dteBillDate=b.dteBillDate "
		+ " where a.dteBillDate='0000-00-00 00:00:00' ;";
	i = ExecuteQuery(sql);

	sql = "  update tblbillpromotiondtl a  "
		+ " join tblbillhd b on a.strBillNo=b.strBillNo and a.strClientCode=b.strClientCode  "
		+ " set a.dteBillDate=b.dteBillDate "
		+ " where a.dteBillDate='0000-00-00 00:00:00' ;";
	i = ExecuteQuery(sql);

	sql = "  update tblbilltaxdtl a  "
		+ " join tblbillhd b on a.strBillNo=b.strBillNo and a.strClientCode=b.strClientCode  "
		+ " set a.dteBillDate=b.dteBillDate "
		+ " where a.dteBillDate='0000-00-00 00:00:00' ;";
	i = ExecuteQuery(sql);

	sql = "  update tblbillsettlementdtl a  "
		+ " join tblbillhd b on a.strBillNo=b.strBillNo and a.strClientCode=b.strClientCode  "
		+ " set a.dteBillDate=b.dteBillDate "
		+ " where a.dteBillDate='0000-00-00 00:00:00' ;";
	i = ExecuteQuery(sql);

	sql = "  update tblhomedeldtl a  "
		+ " join tblbillhd b on a.strBillNo=b.strBillNo and a.strClientCode=b.strClientCode  "
		+ " set a.dteBillDate=b.dteBillDate "
		+ " where a.dteBillDate='0000-00-00 00:00:00' ;";
	i = ExecuteQuery(sql);

	sql = "update tblqbilldiscdtl a  "
		+ " join tblqbillhd b on a.strBillNo=b.strBillNo and a.strClientCode=b.strClientCode  "
		+ " set a.dteBillDate=b.dteBillDate "
		+ " where a.dteBillDate='0000-00-00 00:00:00' ;";
	i = ExecuteQuery(sql);

	sql = "  update tblqbillmodifierdtl a  "
		+ " join tblqbillhd b on a.strBillNo=b.strBillNo and a.strClientCode=b.strClientCode  "
		+ " set a.dteBillDate=b.dteBillDate "
		+ " where a.dteBillDate='0000-00-00 00:00:00' ;";
	i = ExecuteQuery(sql);

	sql = " update tblqbillpromotiondtl a  "
		+ " join tblqbillhd b on a.strBillNo=b.strBillNo and a.strClientCode=b.strClientCode  "
		+ " set a.dteBillDate=b.dteBillDate "
		+ " where a.dteBillDate='0000-00-00 00:00:00' ;";
	i = ExecuteQuery(sql);

	sql = "  update tblqbilltaxdtl a  "
		+ " join tblqbillhd b on a.strBillNo=b.strBillNo and a.strClientCode=b.strClientCode  "
		+ " set a.dteBillDate=b.dteBillDate "
		+ " where a.dteBillDate='0000-00-00 00:00:00' ;";
	i = ExecuteQuery(sql);

	sql = "  update tblqbillsettlementdtl a  "
		+ " join tblqbillhd b on a.strBillNo=b.strBillNo and a.strClientCode=b.strClientCode  "
		+ " set a.dteBillDate=b.dteBillDate "
		+ " where a.dteBillDate='0000-00-00 00:00:00' ;";
	i = ExecuteQuery(sql);

	sql = "  update tblhomedeldtl a  "
		+ " join tblqbillhd b on a.strBillNo=b.strBillNo and a.strClientCode=b.strClientCode  "
		+ " set a.dteBillDate=b.dteBillDate "
		+ " where a.dteBillDate='0000-00-00 00:00:00' ;";
	i = ExecuteQuery(sql);

	sql = "  update tblvoidmodifierdtl a  "
		+ " join tblvoidbillhd b on a.strBillNo=b.strBillNo and a.strClientCode=b.strClientCode  "
		+ " set a.dteBillDate=b.dteBillDate "
		+ " where a.dteBillDate='0000-00-00 00:00:00' ;";
	i = ExecuteQuery(sql);

	sql = "   update tblvoidbillsettlementdtl a  "
		+ " join tblvoidbillhd b on a.strBillNo=b.strBillNo and a.strClientCode=b.strClientCode  "
		+ " set a.dteBillDate=b.dteBillDate "
		+ " where a.dteBillDate='0000-00-00 00:00:00' ;";
	i = ExecuteQuery(sql);
    }

    private void funUpdateBillNote()
    {
	try
	{
	    String billNote = "";
	    if (clsGlobalVarClass.gUseVatAndServiceTaxFromPos)
	    {
		if (clsGlobalVarClass.gPrintVatNoPOS.equals("Y"))
		{
		    billNote += "  Vat No. : " + clsGlobalVarClass.gPOSVatNo;
		    billNote += " ";
		}
		if (clsGlobalVarClass.gPrintServiceTaxNoPOS.equals("Y"))
		{
		    billNote += "  Service Tax No. : " + clsGlobalVarClass.gPOSServiceTaxNo;
		    billNote += " ";
		}
	    }
	    else
	    {
		if (clsGlobalVarClass.gPrintVatNo)
		{
		    billNote += "  Vat No. : " + clsGlobalVarClass.gVatNo;
		    billNote += " ";
		}
		if (clsGlobalVarClass.gPrintServiceTaxNo)
		{
		    billNote += "  Service Tax No. : " + clsGlobalVarClass.gServiceTaxNo;
		    billNote += " ";
		}
	    }

	    String sql = "select  a.strTaxCode,a.strTaxDesc,a.dteValidFrom,a.dteValidTo,a.strBillNote "
		    + "from tbltaxhd a "
		    + "where a.strBillNote='' ";
	    ResultSet rsBlankBillNote = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rsBlankBillNote.next())
	    {
		clsGlobalVarClass.dbMysql.execute("update tbltaxhd "
			+ "set strBillNote='" + billNote + "' "
			+ "where strTaxCode='" + rsBlankBillNote.getString(1) + "' ");
	    }
	    rsBlankBillNote.close();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private void funUpdateDayEndPAX()
    {
	try
	{
	    String sql = "select a.strPOSCode,date(a.dtePOSDate),a.intShiftCode,a.strDayEnd,a.strShiftEnd,a.intTotalPax "
		    + "from tbldayendprocess a "
		    + "where a.intTotalPax=0 "
		    + "and a.strDayEnd='Y' "
		    + "and a.strShiftEnd='Y' ";
	    ResultSet rsDayendPax = clsGlobalVarClass.dbMysql.executeResultSet(sql);
	    while (rsDayendPax.next())
	    {
		String posCode = rsDayendPax.getString(1);//posCode
		String dayEndDate = rsDayendPax.getString(2);//dayend date/bill date
		String shiftCode = rsDayendPax.getString(3);//shift

		sql = "update tbldayendprocess set intTotalPax=IFNULL((select sum(intBillSeriesPaxNo) "
			+ "from tblqbillhd  "
			+ "where date(dteBillDate ) ='" + dayEndDate + "'  "
			+ "and intShiftCode=" + shiftCode + " "
			+ "and strPOSCode='" + posCode + "'),0) "
			+ "where date(dtePOSDate)='" + dayEndDate + "'  "
			+ "and strPOSCode='" + posCode + "' "
			+ "and intShiftCode=" + shiftCode + " ";
		int i = ExecuteQuery(sql);
	    }
	    rsDayendPax.close();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    public static void funUpdateTblForms()
    {
	try
	{
	    String sql = "DROP TABLE IF EXISTS `tblforms`;";
	    int i = executeUpdateQuery(sql);

	    sql = "CREATE TABLE IF NOT EXISTS `tblforms` ("
		    + "  `strFormName` varchar(50) NOT NULL,"
		    + "  `strModuleName` varchar(50) NOT NULL,"
		    + "  `strModuleType` varchar(2) NOT NULL,"
		    + "  `strImageName` varchar(100) NOT NULL,"
		    + "  `intSequence` int(11) NOT NULL,"
		    + "  `strColorImageName` varchar(100) NOT NULL,"
		    + "  `strRequestMapping` varchar(100) NOT NULL"
		    + ") ;";
	    i = executeUpdateQuery(sql);

	    sql = "DELETE FROM `tblforms`;";
	    i = executeUpdateQuery(sql);

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
			+ ",('frmCustomerLedger', 'Customer Ledger', 'R', 'imgCustomerLedger', '211', 'imgCustomerLedger1', 'frmCustomerLedger.html') "
			+ ",('frmAreaWiseGroupWiseSales', 'Area Wise Group Wise Sales', 'R', 'imgAreaWiseGroupWiseSales', '212', 'imgAreaWiseGroupWiseSales', 'frmCustomerLedger.html') ";
	    }
	    i = executeUpdateQuery(sql);

	    //strucure update for tblforms
	    sql = "update tblforms set strColorImageName=CONCAT(strImageName,'1') ";
	    i = executeUpdateQuery(sql);

	    sql = "UPDATE tblforms SET `strRequestMapping`='frmPOSFactoryMaster.html' WHERE  `strFormName`='frmFactoryMaster' AND `intSequence`=152 ; ";
	    i = executeUpdateQuery(sql);

	    sql = "UPDATE `tblforms` SET `strImageName`='imgManagersSummaryReport', `strColorImageName`='imgManagersSummaryReport1' "
		    + "WHERE `strFormName`='frmManagerSummaryFlash' AND `strModuleName`='Manager Summary Flash' AND `strModuleType`='R' AND `intSequence`=158  "
		    + "LIMIT 1; ";
	    i = executeUpdateQuery(sql);

	    sql = "ALTER TABLE `tblforms` "
		    + "	ADD COLUMN `strShortName` VARCHAR(100) NOT NULL DEFAULT '' AFTER `strRequestMapping`;";
	    i = executeUpdateQuery(sql);

	    sql = "update tblforms  "
		    + "set strShortName=strModuleName "
		    + "where strShortName=''; ";
	    i = executeUpdateQuery(sql);
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    public final static int executeUpdateQuery(String Query)
    {
	try
	{
	    clsGlobalVarClass.dbMysql.execute(Query);
	}
	catch (Exception e)
	{
	    //e.printStackTrace();
	}
	return 0;
    }
}
