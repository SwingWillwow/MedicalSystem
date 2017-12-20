/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jsfbean.doctor;

import com.entity.Diagnosis;
import com.entity.Patient;
import java.util.Calendar;
import java.util.Date;
import javax.annotation.PostConstruct;
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
public class JudgeSymptomBean {

    //userTranscation and EntityManager used to change database.
    @PersistenceContext(unitName = "MedicalSystemPU")
    private EntityManager em;
    @Resource
    private UserTransaction utx;
    //properties
    private Patient currentPatient;
    private String symptom;
    private String diagnosisStr;
    private int age;
    private String patientSex;
    private Long diagId;

    /**
     * Creates a new instance of JudgeSymptomBean
     */
    public JudgeSymptomBean() {

    }

    @PostConstruct
    public void initBean() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
        currentPatient = (Patient) session.getAttribute("currentPatient");
        Calendar today = Calendar.getInstance();
        today.setTime(new Date());
        Calendar birthday = Calendar.getInstance();
        birthday.setTime(currentPatient.getBirthday());
        age = getAge(today, birthday);
        initSex(currentPatient);
        diagId = Long.parseLong(session.getAttribute("diagId").toString());
    }

    /**
     *
     * @param today
     * @param birth
     * @return person's age.
     */
    public int getAge(Calendar today, Calendar birth) {
        int y, m, d;
        int by, bm, bd;
        y = today.get(Calendar.YEAR);
        by = birth.get(Calendar.YEAR);
        d = today.get(Calendar.DAY_OF_YEAR);
        bd = birth.get(Calendar.DAY_OF_YEAR);
        int age = y - by;
        if (d > bd) {
            age++;
        }
        return age;
    }

    /**
     *
     * @param p patient
     */
    public void initSex(Patient p) {
        if (p.getSex() == 'M') {
            patientSex = "男";
        } else {
            patientSex = "女";
        }
    }

    /**
     *
     * @return next page url
     */
    public String toDecideMedicine() {
        //save patient and diagnosisId
        FacesContext facesContext = FacesContext.getCurrentInstance();
        //change diagnosis
        try {
            utx.begin();
            Diagnosis diagnosis = em.find(Diagnosis.class, diagId);
            diagnosis.setSymtom(symptom);
            diagnosis.setDiagnosis(diagnosisStr);
            utx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/doctor/decideMedicine";
    }

    /*
        getters and setters
     */
    public Patient getCurrentPatient() {
        return currentPatient;
    }

    public void setCurrentPatient(Patient currentPatient) {
        this.currentPatient = currentPatient;
    }

    public String getSymptom() {
        return symptom;
    }

    public void setSymptom(String symptom) {
        this.symptom = symptom;
    }

    public String getDiagnosisStr() {
        return diagnosisStr;
    }

    public void setDiagnosisStr(String diagnosisStr) {
        this.diagnosisStr = diagnosisStr;
    }

    public Long getDiagId() {
        return diagId;
    }

    public void setDiagId(Long diagId) {
        this.diagId = diagId;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPatientSex() {
        return patientSex;
    }

    public void setPatientSex(String patientSex) {
        this.patientSex = patientSex;
    }

}
