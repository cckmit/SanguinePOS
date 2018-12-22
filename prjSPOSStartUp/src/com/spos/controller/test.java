/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spos.controller;

import ch.qos.logback.classic.util.ContextInitializer;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.apache.poi.ss.usermodel.ExcelStyleDateFormatter;


class Parent
{
    public Object fun() throws Exception
    {
	System.out.println("fun of parent");
	
	return "Test";
    }
}
	
/**
 *
 * @author sainguine
 */
public class test extends Parent
{

    List list;
    String abc = "";

    test()
    {
	abc = "ABC";
    }

    public Object[] fun() 
    {
	fun2();
	System.out.println(abc);
	abc = "DEF";
	
	return new Object[]{};
    }
    
     public void fun(int a)
    {
	fun2();
	System.out.println(abc);
	abc = "DEF";
    }
    
    
    public static void main(String args[]) throws FileNotFoundException, Exception
    {

	
	Parent t=new test();
	t.fun();
	
	try
	{

	    BufferedWriter KotOut = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("printing.txt"), "UTF-8"));
	    KotOut.write("बाल्टि गोश्त");
	    KotOut.close();

	    String result = null;
	    Process p = Runtime.getRuntime().exec("wmic baseboard get serialnumber");
	    BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
	    String line;
	    while ((line = input.readLine()) != null)
	    {
		result += line;
	    }
	    if (result.equalsIgnoreCase(" "))
	    {
		System.out.println("Result is empty");
	    }
	    else
	    {
		System.out.println("baseboard serial no->" + result);
	    }
	    input.close();
	}
	catch (IOException ex)
	{
	    ex.printStackTrace();
	}

	StringBuffer output = new StringBuffer();
	Process process;
	String[] cmd =
	{
	    "wmic csproduct get UUID"
	};
	try
	{
	    process = Runtime.getRuntime().exec(cmd);
	    process.waitFor();
	    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
	    String line = "";
	    while ((line = reader.readLine()) != null)
	    {
		output.append(line + "\n");
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}

	System.out.println("UUID from CMD=" + output);

	UUID uuid = UUID.randomUUID();
	String randomUUIDString = uuid.toString();

	System.out.println("Random UUID String = " + randomUUIDString);
	System.out.println("UUID version       = " + uuid.version());
	System.out.println("UUID variant       = " + uuid.variant());

	String s = "CASHCARDGUESTCREDIT";
	System.out.println("" + s.indexOf("GUEST"));

	Date d = new Date();

	SimpleDateFormat dateFormat = new SimpleDateFormat("E dd-MMM-yyyy");

	String date = dateFormat.format(d);
	System.out.println("date->" + date);

	String str = "\"abc\"";
	str = str.replaceAll("\"", "");
	System.out.println("-->" + str);

	System.out.println("27->" + Character.toString((char) 27));
	System.out.println("27->" + Character.toString((char) 33));
	System.out.println("27->" + Character.toString((char) 48));

	int a = 1;

	DecimalFormat decimalFormat = new DecimalFormat("#.##");
	System.out.println("a-->" + String.format("%01d", a));

	Integer n1 = 127;
	Integer n2 = 127;

	System.out.println("n1=" + n1);
	System.out.println("n2=" + n2);
	System.out.println("n1==n2" + (n1 == n2));

	Integer n3 = 128;
	Integer n4 = 128;

	System.out.println("n3=" + n3);
	System.out.println("n4=" + n4);
	System.out.println("n3==n4" + (n3 == n4));

	System.getProperties().list(System.out);

	System.out.println("" + System.getProperty("os.name").split(" ")[0]);

    }

    private void fun2()
    {
	int a = 10;
	int b = 20;
	int c = a + b;
	abc = "ZZZ";
    }

//    @Override
//    public void run()
//    {
//	System.out.println("In thread.......");
//	for (int i = 0; i < 1000; i++)
//	{
//	    System.out.println("i-->" + i);
//	}
//
//    }

}

