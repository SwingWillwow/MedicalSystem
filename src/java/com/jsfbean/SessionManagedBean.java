/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jsfbean;

import com.entity.Users;
import com.util.PasswordManager;
import javax.annotation.Resource;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

/**
 *
 * @author qiuyukun
 */
@SessionScoped
public class SessionManagedBean {

    /**
     * Creates a new instance of SessionManagedBean
     * WARNING!!!!!!!!!!!!
     * this bean may be lead to some mistake please be carefully.
     * if meet some bugs about session, check this bean first.
     */
    private HttpSession session;
    @PersistenceContext(unitName = "MedicalSystemPU")
    private EntityManager em;
    @Resource
    private UserTransaction utx;
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
    // 用户登录（传入账号和密码）
    public boolean userLogin(String userName,String password){
        if(session.getAttribute("userName")!=null){
            return false;
        }
        else{
            Query query=em.createNamedQuery("getUserByName");
            query.setParameter(1, userName);
            Users u=(Users)query.getSingleResult();
            if(u!=null){
                try {
                    if (PasswordManager.checkPassword(password, u.getPassword())) {
                        session.setAttribute("userName", userName);
                        return true;
                    }
                    else{
                        return false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else{
                return false;
            }
            
        }
        return false;
    }
    public void setErrorMessage(String message){
        session.setAttribute("errorMessage", message);
    }
}
