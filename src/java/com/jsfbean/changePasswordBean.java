/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jsfbean;

import com.entity.Patient;
import com.util.PasswordManager;
import javax.annotation.Resource;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

/**
 *
 * @author qiuyukun
 */
public class changePasswordBean {

    /**
     * Creates a new instance of changePasswordBean
     */
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
    @Resource
    private UserTransaction utx;
    @PersistenceContext(unitName = "MedicalSystemPU")
    EntityManager em;
    public changePasswordBean() {
        
    }

    public String changePassword(){
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        HttpSession session = (HttpSession)externalContext.getSession(true);
        SessionManagedBean sessionManagedBean = (SessionManagedBean)session.getAttribute("sessionManagedBean");
        String encryptOldPassword = PasswordManager.getMD5(oldPassword);
        Patient oldP = (Patient)session.getAttribute("userInfo");
        if(encryptOldPassword.equals(oldP.getPassword())){
            oldP.setPassword(PasswordManager.getMD5(newPassword));
        }
        try{
            utx.begin();
            em.merge(oldP);
            utx.commit();
        }
        catch(Exception e){
            sessionManagedBean.setErrorMessage(e.getMessage());
            return "";
        }
        
        return "";
    }
    
    /*
    下面的是getter and setter
    */
    
    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
    
}
