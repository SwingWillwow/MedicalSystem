/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jsfbean.admin;

import com.entity.Employee;
import com.entity.Medicine;
import com.jsfbean.SessionManagedBean;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

/**
 *
 * @author Administrator
 */
public class AddMedicine {
    @PersistenceContext(unitName = "MedicalSystemPU")
    private EntityManager em;
    @Resource
    private UserTransaction utx;
    private Medicine medicine;
    public AddMedicine() {
        medicine=new Medicine();
        medicine.setInventory(0);
    }

    public String addMedicine(){
        SessionManagedBean sessionManagedBean;
        HttpSession session=(HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        if (session.getAttribute("sessionManagedBean") == null) {
            sessionManagedBean = new SessionManagedBean();
        } else {
            sessionManagedBean = (SessionManagedBean) session.getAttribute("sessionManagedBean");
        }
        Query query=em.createQuery("SELECT mdc FROM Medicine mdc where mdc.name=?1");
        query.setParameter(1, medicine.getName());
        List<Medicine> temps=query.getResultList();
        if(!temps.isEmpty()){
            sessionManagedBean.setErrorMessage("该药名已存在！");
            return "";
        }
        Employee employee=(Employee)session.getAttribute("userInfo");
        medicine.setOperator(employee);
        Date today=new Date();
        medicine.setCreateTime(today);
        medicine.setLastUpdateTime(today);
        try {
            utx.begin();
            em.persist(medicine);
            utx.commit();
        } catch (Exception e) {
            sessionManagedBean.setErrorMessage(e.getMessage());
            return "";
        }
        sessionManagedBean.setSuccessMessage("添加药品："+medicine.getName()+"成功！");
        return "";
    }
    
    public Medicine getMedicine() {
        return medicine;
    }

    public void setMedicine(Medicine medicine) {
        this.medicine = medicine;
    }
   
}
