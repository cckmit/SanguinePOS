/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.spos.controller;

import java.util.List;

/**
 *
 * @author sainguine
 */
public class test {
    
    List list;
    String abc="";
    
    test()
    {
        abc="ABC";
    }
    
    private void fun()
    {
        fun2();
        System.out.println(abc);
        abc="DEF";
    }
    
    public static void main(String args[])
    {
        
        test t=new test();
        t.fun();
    }
    
    
    
    private void fun2()
    {
        int a=10;
        int b=20;
        int c=a+b;
        abc="ZZZ";
    }
    
}

