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
public class clsFillInternalTableStructureUpdate
{

    private Map<String, List<String>> mapStructureUpdater;

    public clsFillInternalTableStructureUpdate(Map<String, List<String>> mapStructureUpdater)
    {
	this.mapStructureUpdater = mapStructureUpdater;
    }

    public void funFillInternalTableStructureUpdate()
    {
	//funCheckInternalTable();
	mapStructureUpdater.get("internalTableStructure").add("funCheckInternalTable");
    }
}
