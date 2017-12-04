/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jsfbean;

import com.entity.PreRegistration;
import com.util.DateOperator;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

/**
 *
 * @author qiuyukun
 */
public class patientRegistrationBean {

    /**
     * Creates a new instance of patientRegistrationBean
     */
    
    private List<PreRegistration> preList;
    @PersistenceContext(unitName = "MedicalSystemPU")
    private EntityManager em;
    @Resource
    private UserTransaction utx;
    public patientRegistrationBean() {
        
    }
    
    public String preRegist(){
        return "";
    }

    public List<PreRegistration> getPreList() {
        Query query = em.createQuery("SELECT pre FROM PreRegistration pre WHERE pre.preTime BETWEEN ?1 AND ?2");
        query.setParameter(1, DateOperator.addDay(new Date(), 1));
        query.setParameter(2, DateOperator.addDay(new Date(), 5));
        preList = query.getResultList();
        return preList;
    }

    public void setPreList(List<PreRegistration> preList) {
        this.preList = preList;
    }
    
    
}
