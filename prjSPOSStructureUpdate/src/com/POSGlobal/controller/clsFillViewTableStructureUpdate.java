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
public class clsFillViewTableStructureUpdate
{

    private Map<String, List<String>> mapStructureUpdater;

    public clsFillViewTableStructureUpdate(Map<String, List<String>> mapStructureUpdater)
    {
	this.mapStructureUpdater = mapStructureUpdater;
    }

    public void funFillViewTableStructureUpdate()
    {
	String sql = "DROP table `vqbillhd`;";
	mapStructureUpdater.get("viewStructure").add(sql);

	sql = "DROP table `vqbilldtl`;";
	mapStructureUpdater.get("viewStructure").add(sql);

	sql = "DROP table `vqbillsettlementdtl`;";
	mapStructureUpdater.get("viewStructure").add(sql);

	sql = "DROP table `vqbillmodifierdtl`;";
	mapStructureUpdater.get("viewStructure").add(sql);

	sql = "DROP table `vqbilltaxdtl`;";
	mapStructureUpdater.get("viewStructure").add(sql);

	sql = "DROP table `vqbillhddtl`;";
	mapStructureUpdater.get("viewStructure").add(sql);

	sql = "DROP table `vqbillhdsettlementdtl`;";
	mapStructureUpdater.get("viewStructure").add(sql);

	/*
             * Create View Query
	 */
	sql = "DROP VIEW `vqbillhd`;";
	mapStructureUpdater.get("viewStructure").add(sql);

	sql = "create view vqbillhd as "
		+ "select * from tblbillhd  "
		+ "union all  "
		+ "select * from tblqbillhd;";
	mapStructureUpdater.get("viewStructure").add(sql);

	sql = "DROP VIEW `vqbilldtl`;";
	mapStructureUpdater.get("viewStructure").add(sql);

	sql = "create view vqbilldtl as "
		+ "select * from tblbilldtl  "
		+ "union  "
		+ "select * from tblqbilldtl;";
	mapStructureUpdater.get("viewStructure").add(sql);

	sql = "DROP VIEW `vqbillsettlementdtl`;";
	mapStructureUpdater.get("viewStructure").add(sql);

	sql = "create view vqbillsettlementdtl as "
		+ "select * from tblbillsettlementdtl "
		+ "union all  "
		+ "select * from tblqbillsettlementdtl;";
	mapStructureUpdater.get("viewStructure").add(sql);

	sql = "DROP VIEW `vqbillmodifierdtl`;";
	mapStructureUpdater.get("viewStructure").add(sql);

	sql = "create view vqbillmodifierdtl as "
		+ "select * from tblbillmodifierdtl "
		+ "union  "
		+ "select * from tblqbillmodifierdtl;";
	mapStructureUpdater.get("viewStructure").add(sql);

	sql = "DROP VIEW `vqbilltaxdtl`;";
	mapStructureUpdater.get("viewStructure").add(sql);

	sql = "create view vqbilltaxdtl as "
		+ "select * from tblbilltaxdtl "
		+ "union  "
		+ "select * from tblqbilltaxdtl;";
	mapStructureUpdater.get("viewStructure").add(sql);

	sql = "DROP VIEW `vqbillhddtl`;";
	mapStructureUpdater.get("viewStructure").add(sql);

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
	mapStructureUpdater.get("viewStructure").add(sql);

	sql = "DROP VIEW `vqbillhdsettlementdtl`;";
	mapStructureUpdater.get("viewStructure").add(sql);

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
	mapStructureUpdater.get("viewStructure").add(sql);

	sql = "DROP VIEW `vqadvbookdtl`;";
	mapStructureUpdater.get("viewStructure").add(sql);

	sql = "create view vqadvbookdtl as "
		+ "select * from tbladvbookbilldtl  "
		+ "union  "
		+ "select * from tblqadvbookbilldtl";
	mapStructureUpdater.get("viewStructure").add(sql);

	sql = "DROP VIEW `vqadvbookhd`;";
	mapStructureUpdater.get("viewStructure").add(sql);

	sql = "create view vqadvbookhd as "
		+ "select * from tbladvbookbillhd  "
		+ "union "
		+ "select * from tblqadvbookbillhd;";
	mapStructureUpdater.get("viewStructure").add(sql);

	sql = "DROP VIEW `vqadvreceiptdtl`;";
	mapStructureUpdater.get("viewStructure").add(sql);

	sql = "create view vqadvreceiptdtl as "
		+ "select * from tbladvancereceiptdtl  "
		+ "union "
		+ "select * from tblqadvancereceiptdtl;";
	mapStructureUpdater.get("viewStructure").add(sql);

	sql = "DROP VIEW `vqadvreceipthd`;";
	mapStructureUpdater.get("viewStructure").add(sql);

	sql = "create view vqadvreceipthd as "
		+ "select * from tbladvancereceipthd  "
		+ "union "
		+ "select * from tblqadvancereceipthd;";
	mapStructureUpdater.get("viewStructure").add(sql);

	sql = "create view vqadvordermodifierdtl as "
		+ "select * from tbladvordermodifierdtl  "
		+ "union  "
		+ "select * from tblqadvordermodifierdtl ";
	mapStructureUpdater.get("viewStructure").add(sql);

	sql = "UPDATE tblforms SET strImageName='imgSalesReport', "
		+ "strColorImageName='imgSalesReport1' WHERE  strFormName='frmSalesReport';";
	mapStructureUpdater.get("viewStructure").add(sql);
    }
}
