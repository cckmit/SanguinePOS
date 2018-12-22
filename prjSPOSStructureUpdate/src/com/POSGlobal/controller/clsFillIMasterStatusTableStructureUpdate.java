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
public class clsFillIMasterStatusTableStructureUpdate
{

    private Map<String, List<String>> mapStructureUpdater;

    public clsFillIMasterStatusTableStructureUpdate(Map<String, List<String>> mapStructureUpdater)
    {
	this.mapStructureUpdater = mapStructureUpdater;
    }

    public void funFillIMasterStatusTableStructureUpdate()
    {
	//funCheckMasterTableStatus();
	mapStructureUpdater.get("masterTableStatusStructure").add("funCheckMasterTableStatus");
    }
}
