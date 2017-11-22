/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.JSFBean;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author qiuyukun
 */
public class SessionManagedBean {

    /**
     * Creates a new instance of SessionManagedBean
     * WARNING!!!!!!!!!!!!
     * this bean may be lead to some mistake please be carefully.
     * if meet some bugs about session, check this bean first.
     */
    private HttpSession session;
    public SessionManagedBean() {
        session = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    }
    public boolean isLogin(){
        if(session.getAttribute("userName")!=null){
            return true;
        }
        else{
            return false;
        }
    }
    public HttpSession getSession(){
        return session;
    }
    public boolean userLogin(String userName){
        if(session.getAttribute("userName")!=null){
            return false;
        }
        else{
            session.setAttribute("userName", userName);
            return true;
        }
    }
    public void setErrorMessage(String message){
        session.setAttribute("errorMessage", message);
    }
}
