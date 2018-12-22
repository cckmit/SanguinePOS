/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.POSMaster.controller.nfc;

import com.POSMaster.controller.nfc.Acr122Manager;
import java.sql.SQLWarning;


/**
 *
 * @author PRASHANT
 */
public class ReaderThread implements Runnable {
    
    public void run()
    {

        try
        {
            
                      
            String[] args ={};
            String data=Acr122Manager.dumpCards(args);
            System.out.println("Data In Thread= "+data);
            if(!data.isEmpty())
            {
                System.out.println("Data read");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }	
    }
    
}
