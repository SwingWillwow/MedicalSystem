/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jsfbean;

import com.entity.Diagnosis;
import com.entity.Doctor;
import com.entity.Patient;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContexts;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

/**
 *
 * @author qiuyukun
 */
public class ShowPatientBean {

    /**
     * Creates a new instance of ShowPatientBean
     */
    ArrayList<Patient> patients = new ArrayList<>();
    @PersistenceContext(unitName = "MedicalSystemPU")
    EntityManager em;
    @Resource
    UserTransaction utx;
    public ShowPatientBean() {
    }
    
    /*
        初始化方法
    */
    @PostConstruct
    public void initShowPatientBean(){
        HttpSession session = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        Doctor doctor = (Doctor)session.getAttribute("userInfo");
        Query query = em.createQuery("SELECT diag FROM Diagnosis diag WHERE diag.doctor=?1 AND DIAG.valid=?2 AND diag.createTime BETWEEN ?3 AND ?4");
        query.setParameter(1, doctor);
        query.setParameter(2, 'Y');
        query.setParameter(3, new Date(),TemporalType.DATE);
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, 1);
        query.setParameter(4, c ,TemporalType.DATE);
        List<Diagnosis> diagnosises = query.getResultList();
        for(Diagnosis diag : diagnosises){
            patients.add(diag.getPatient());
        }
        return;
    }

    public ArrayList<Patient> getPatients() {
        return patients;
    }

    public void setPatients(ArrayList<Patient> patients) {
        this.patients = patients;
    }
    
}
