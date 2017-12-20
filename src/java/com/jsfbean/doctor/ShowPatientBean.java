/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jsfbean.doctor;

import com.entity.Diagnosis;
import com.entity.Doctor;
import com.entity.Patient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

/**
 *
 * @author qiuyukun
 */
public class ShowPatientBean implements Serializable {

    /**
     * Creates a new instance of ShowPatientBean
     */
    private ArrayList<Patient> patients = new ArrayList<>();
    @PersistenceContext(unitName = "MedicalSystemPU")
    private EntityManager em;
    @Resource
    private UserTransaction utx;
    private Long diagId;
    private boolean hasPatient;

    public ShowPatientBean() {
    }

    /*
        初始化方法
     */
    @PostConstruct
    public void initShowPatientBean() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        Doctor doctor = (Doctor) session.getAttribute("userInfo");
        Query query = em.createQuery("SELECT diag FROM Diagnosis diag WHERE diag.doctor=?1 AND DIAG.valid=?2 AND diag.createTime BETWEEN ?3 AND ?4 ORDER BY DIAG.createTime ASC");
        query.setParameter(1, doctor);
        query.setParameter(2, 'Y');
        query.setParameter(3, new Date(), TemporalType.DATE);
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, 1);
        query.setParameter(4, c, TemporalType.DATE);
        List<Diagnosis> diagnosises = query.getResultList();
        if (diagnosises.size() == 0) {
            hasPatient = false;
            return;
        }
        diagId = diagnosises.get(0).getId();
        for (Diagnosis diag : diagnosises) {
            patients.add(diag.getPatient());
        }
        hasPatient = true;
        return;
    }

    public Long getDiagId() {
        return diagId;
    }

    public void setDiagId(Long diagId) {
        this.diagId = diagId;
    }

    public boolean isHasPatient() {
        return hasPatient;
    }

    public void setHasPatient(boolean hasPatient) {
        this.hasPatient = hasPatient;
    }

    public ArrayList<Patient> getPatients() {
        return patients;
    }

    public void setPatients(ArrayList<Patient> patients) {
        this.patients = patients;
    }

    //跳转到诊断病症界面
    public String toJudgeSymptom() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
        session.setAttribute("currentPatient", patients.get(0));//patients.get(0)表示最早挂号的病人
        session.setAttribute("diagId", diagId);
        return "/doctor/judgeSymptom";
    }

}
