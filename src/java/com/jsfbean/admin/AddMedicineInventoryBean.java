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
import javax.annotation.PostConstruct;
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
public class AddMedicineInventoryBean {
    @PersistenceContext(unitName = "MedicalSystemPU")
    private EntityManager em;
    @Resource
    private UserTransaction utx;
    SessionManagedBean sessionManagedBean;
    Medicine medicine;
    private int addingInventory;

    public AddMedicineInventoryBean() {
    }
    
    @PostConstruct
    public void initAddMedicineInventoryBean(){
        ExternalContext externalContext=FacesContext.getCurrentInstance().getExternalContext();
        if(externalContext.getFlash().get("MedicineId")!=null){
            String medicineId=externalContext.getFlash().get("MedicineId").toString();
            medicine=em.find(Medicine.class, Long.parseLong(medicineId));
        }
    }
    public String addMedicineInventory(){
        ExternalContext externalContext=FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session=(HttpSession)externalContext.getSession(true);
        if (session.getAttribute("sessionManagedBean") == null) {
            sessionManagedBean = new SessionManagedBean();
        } else {
            sessionManagedBean = (SessionManagedBean) session.getAttribute("sessionManagedBean");
        }
        Employee operator=(Employee)session.getAttribute("userInfo");
        try {
            medicine.setInventory(medicine.getInventory()+addingInventory);
            medicine.setLastUpdateTime(new Date());
            medicine.setOperator(operator);
            utx.begin();
            em.merge(medicine);
            utx.commit();   
        } catch (Exception e) {
            sessionManagedBean.setErrorMessage(e.getMessage());
        }
        String tip="修改成功:药物【"+medicine.getName()+"】库存变为"+medicine.getInventory();
        sessionManagedBean.setSuccessMessage(tip);
        return "/admin/editMedicine";
    }

    public int getAddingInventory() {
        return addingInventory;
    }

    public void setAddingInventory(int addingInventory) {
        this.addingInventory = addingInventory;
    }

    
    public Medicine getMedicine() {
        return medicine;
    }

    public void setMedicine(Medicine medicine) {
        this.medicine = medicine;
    }
     
}
