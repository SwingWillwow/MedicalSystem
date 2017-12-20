/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jsfbean.patient;

import com.entity.Diagnosis;
import com.entity.DiagnosisDetail;
import com.entity.Patient;
import java.util.List;
import javax.annotation.Resource;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

/**
 *
 * @author qiuyukun
 */
public class DiagnosisBean {

    /**
     * Creates a new instance of DiagnosisBean
     */
    private List<Diagnosis> userDiagnosis;
    private List<DiagnosisDetail> userDiagnosisDetails;
    @PersistenceContext(unitName = "MedicalSystemPU")
    private EntityManager em;
    @Resource
    private UserTransaction utx;

    public DiagnosisBean() {

    }

    public List<Diagnosis> getUserDiagnosis() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(true);
        Patient p = (Patient) session.getAttribute("userInfo");
        Query query = em.createNamedQuery("getDiagnosisByPatientId");
        query.setParameter(1, p);
        userDiagnosis = query.getResultList();
        return userDiagnosis;
    }

    public List<DiagnosisDetail> getUserDiagnosisDetails() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
        if (request.getParameter("diagnosisId") == null) {
            return userDiagnosisDetails;
        }
        Long diagnosisId = Long.parseLong(request.getParameter("diagnosisId"));
        Query query = em.createNamedQuery("getDiagnosisDetialByDiagnosisId");
        query.setParameter(1, diagnosisId);
        Diagnosis diagnosis = (Diagnosis) query.getSingleResult();
        userDiagnosisDetails = diagnosis.getDiagnosisDetails();
        return userDiagnosisDetails;
    }

    public String showDiagnosisDetail() {
        return "/patient/userDiagnosisDetail";
    }

}
