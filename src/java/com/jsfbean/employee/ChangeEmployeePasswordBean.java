/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jsfbean.employee;

import com.entity.Employee;
import com.jsfbean.SessionManagedBean;
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
 * @author Administrator
 */
public class ChangeEmployeePasswordBean {
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
    @Resource
    private UserTransaction utx;
    @PersistenceContext(unitName = "MedicalSystemPU")
    EntityManager em;

    public String changePassword(){
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        HttpSession session = (HttpSession)externalContext.getSession(true);
        SessionManagedBean sessionManagedBean = (SessionManagedBean)session.getAttribute("sessionManagedBean");
        String encryptOldPassword = PasswordManager.getMD5(oldPassword);
        Employee oldE = (Employee)session.getAttribute("userInfo");
        if(encryptOldPassword.equals(oldE.getPassword())){
            if(newPassword.equals(confirmPassword))
                oldE.setPassword(PasswordManager.getMD5(newPassword));
            else{
                sessionManagedBean.setErrorMessage("两次输入的新密码不一致！");
                return "";
            }
        }
        else{
            sessionManagedBean.setErrorMessage("原密码输入错误！");
            return "";
        }
        try{
            utx.begin();
            em.merge(oldE);
            utx.commit();
        }
        catch(Exception e){
            sessionManagedBean.setErrorMessage(e.getMessage());
            return "";
        }
        sessionManagedBean.setSuccessMessage("修改密码成功！");
        return "/employee/showEmployeeInfo.xhtml";
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
    
    public ChangeEmployeePasswordBean() {
    }
    
}
