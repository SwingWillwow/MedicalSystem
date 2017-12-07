/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jsfbean;

import com.entity.Doctor;
import com.util.PasswordManager;
import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

/**
 *
 * @author qiuyukun
 */
public class ChangeDocPwd {

    //properties
    @PersistenceContext(unitName = "MedicalSystemPU")
    private EntityManager em;
    @Resource
    private UserTransaction utx;
    private String oldPassword;
    private String newPassword;
    /**
     * Creates a new instance of ChangeDocPwd
     */
    public ChangeDocPwd() {
    
    }

    public String changePwd(){
        SessionManagedBean sessionManagedBean = SessionManagedBean.getInstance();
        try {
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            Doctor doctor = (Doctor)session.getAttribute("userInfo");
            if(!PasswordManager.checkPassword(oldPassword, doctor.getPassword())){
                sessionManagedBean.setErrorMessage("旧密码错误!");
                return "";
            }
            utx.begin();
            doctor.setPassword(PasswordManager.getMD5(newPassword));
            em.merge(doctor);
            utx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        sessionManagedBean.userLogout();
        return "/index.xhtml";
    }
    
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
    
}
