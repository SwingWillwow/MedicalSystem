/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jsfbean.employee;

import com.entity.Employee;
import com.jsfbean.SessionManagedBean;
import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

/**
 *
 * @author Administrator
 */
public class ChangeEmployeeInfoBean {

    //properties
    @PersistenceContext(unitName = "MedicalSystemPU")
    private EntityManager em;
    @Resource
    private UserTransaction utx;

    /**
     * Creates a new instance of ChangeDocInfoBean
     */
    public String changeInformation() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        Employee employee = (Employee) session.getAttribute("userInfo");
        SessionManagedBean sessionManagedBean = (SessionManagedBean) session.getAttribute("sessionManagedBean");
        try {
            utx.begin();
            em.merge(employee);
            utx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            sessionManagedBean.setErrorMessage(e.getMessage());
        }
        sessionManagedBean.setSuccessMessage("修改个人信息成功！");
        return "/employee/showEmployeeInfo.xhtml";
    }

    public ChangeEmployeeInfoBean() {
    }

}
