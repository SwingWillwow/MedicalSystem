/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jsfbean.patient;

import com.entity.Patient;
import com.entity.PreRegistrationDetail;
import com.jsfbean.SessionManagedBean;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

/**
 *
 * @author qiuyukun
 */
public class PatientPreRegistrationBean {

    /**
     * Creates a new instance of PatientPreRegistrationBean
     */
    List<PreRegistrationDetail> patientRegistDetails;
    @PersistenceContext
    EntityManager em;
    @Resource
    UserTransaction utx;
    public PatientPreRegistrationBean() {
        
    }
    
    @PostConstruct
    public void initPatientRegistDetails(){
        SessionManagedBean sessionManagedBean = SessionManagedBean.getInstance();
        Patient p = (Patient)sessionManagedBean.getSession().getAttribute("userInfo");
        Query query = em.createQuery("SELECT preDetail FROM PreRegistrationDetail preDetail WHERE PREDETAIL.patient=?1");
        query.setParameter(1, p);
        patientRegistDetails = query.getResultList();
    }
    
    public List<PreRegistrationDetail> getPatientRegistDetails() {
        return patientRegistDetails;
    }

    public void setPatientRegistDetails(List<PreRegistrationDetail> patientRegistDetails) {
        this.patientRegistDetails = patientRegistDetails;
    }
    
    
    
}
