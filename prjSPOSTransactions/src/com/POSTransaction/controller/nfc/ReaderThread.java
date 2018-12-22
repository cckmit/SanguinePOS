/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.POSTransaction.controller.nfc;

import com.POSTransaction.controller.nfc.Acr122Manager;


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
