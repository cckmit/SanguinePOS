/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.POSMaster.controller;

/**
 *
 * @author Padalkar Vinayak
 */
public class clsUserGroupRightsBean {
    
    private String strFormName;
    
    private boolean grant;
    private boolean tranAuthentication;
    private boolean enableAuditing;

    public String getStrFormName() {
        return strFormName;
    }

    public void setStrFormName(String strFormName) {
        this.strFormName = strFormName;
    }

    public boolean isGrant() {
        return grant;
    }

    public void setGrant(boolean grant) {
        this.grant = grant;
    }

    public boolean isTranAuthentication() {
        return tranAuthentication;
    }

    public void setTranAuthentication(boolean tranAuthentication) {
        this.tranAuthentication = tranAuthentication;
    }

    public boolean isEnableAuditing() {
        return enableAuditing;
    }

    public void setEnableAuditing(boolean enableAuditing) {
        this.enableAuditing = enableAuditing;
    }
    
    
}
