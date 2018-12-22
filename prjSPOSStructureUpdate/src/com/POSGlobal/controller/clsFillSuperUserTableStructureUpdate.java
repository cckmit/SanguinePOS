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
public class clsFillSuperUserTableStructureUpdate
{
     private Map<String, List<String>> mapStructureUpdater;

    public clsFillSuperUserTableStructureUpdate(Map<String, List<String>> mapStructureUpdater)
    {
	this.mapStructureUpdater = mapStructureUpdater;
    }
    
     public void funFillSuperUserTableStructureUpdate()
    {
	String sql = "CREATE TABLE IF NOT EXISTS `tblsuperuserdtl` ( "
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
	mapStructureUpdater.get("superUserStructure").add(sql);

	//update super users forms
	//funUpdateSuperUserForms();
	mapStructureUpdater.get("superUserStructure").add("funUpdateSuperUserForms");
    }
}
