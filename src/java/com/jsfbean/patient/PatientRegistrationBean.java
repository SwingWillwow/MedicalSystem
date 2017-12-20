/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jsfbean.patient;

import com.entity.Patient;
import com.entity.PreRegistration;
import com.entity.PreRegistrationDetail;
import com.jsfbean.SessionManagedBean;
import com.util.DateOperator;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

/**
 *
 * @author qiuyukun
 */
public class PatientRegistrationBean implements Serializable {

    /**
     * Creates a new instance of PatientRegistrationBean
     */
    private List<PreRegistration> preList;
    @PersistenceContext(unitName = "MedicalSystemPU")
    private EntityManager em;
    @Resource
    private UserTransaction utx;

    public PatientRegistrationBean() {

    }

//    @PostConstruct
//    public void initPreList(){
//        
//    }
    public String preRegist() {
        SessionManagedBean sessionManagedBean = SessionManagedBean.getInstance();
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        Long preId = Long.parseLong(request.getParameter("preId"));
        PreRegistration pre = em.find(PreRegistration.class, preId);
        //判断预约人数，即能否预约
        if (pre.getByInternetReal() >= pre.getByInternet()) {
            sessionManagedBean.setErrorMessage("预约人数过多，预约失败。");
            return "";
        }
        Patient p = (Patient) sessionManagedBean.getSession().getAttribute("userInfo");
        List<PreRegistrationDetail> preDetailList = pre.getPreResgistrationDetails();
        if (isPreRegist(p, preDetailList)) {
            sessionManagedBean.setErrorMessage("您已经预约过这个时段了，请不要重复预约");
            return "";
        }
        if (!isPreLessThanThree(p)) {
            sessionManagedBean.setErrorMessage("您同时预约超过三次，无法预约");
            return "";
        }
        pre.setByInternetReal(pre.getByInternetReal() + 1);
        PreRegistrationDetail preRegistrationDetail = new PreRegistrationDetail();
        preRegistrationDetail.setCreateTime(new Date());
        preRegistrationDetail.setLastUpdateTime(new Date());
        preRegistrationDetail.setPatient(p);
        preRegistrationDetail.setValid('Y');
        preRegistrationDetail.setPreRegistrationId(pre);
        preDetailList.add(preRegistrationDetail);
        pre.setPreResgistrationDetails(preDetailList);
        try {
            utx.begin();
            em.persist(preRegistrationDetail);
            em.merge(pre);
            utx.commit();
        } catch (IllegalStateException | SecurityException | HeuristicMixedException | HeuristicRollbackException | NotSupportedException | RollbackException | SystemException e) {
            sessionManagedBean.setErrorMessage(e.getMessage());
        }
        sessionManagedBean.setSuccessMessage("预约成功");
        return "/index";
    }

    public List<PreRegistration> getPreList() {
        String sid = null;
        Long docId = null;
        if (FacesContext.getCurrentInstance().getExternalContext().getFlash().get("docId") != null) {
            sid = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("docId").toString();
            docId = Long.parseLong(sid);
        }
        if (docId == null) {
            return preList;
        }
        Query query = em.createQuery("SELECT pre FROM PreRegistration pre WHERE pre.doctor.id = ?1 AND pre.preTime BETWEEN ?2 AND ?3");
        query.setParameter(1, docId);
        query.setParameter(2, DateOperator.addDay(new Date(), 0));
        //query.setParameter(2, DateOperator.addDay(new Date(), 1));
        query.setParameter(3, DateOperator.addDay(new Date(), 5));
        preList = query.getResultList();
        return preList;
    }

    public void setPreList(List<PreRegistration> preList) {
        this.preList = preList;
    }

    //判断该病人的预约次数是否小于三
    private boolean isPreLessThanThree(Patient p) {
        List<PreRegistrationDetail> patientDetails;
        Query query2 = em.createQuery("SELECT pre FROM PreRegistrationDetail pre WHERE pre.patient = ?1 AND pre.valid=?2");
        query2.setParameter(1, p);
        query2.setParameter(2, 'Y');
        patientDetails = query2.getResultList();
        return patientDetails.size() < 3;
    }

    //判断是否预约过了
    private boolean isPreRegist(Patient p, List<PreRegistrationDetail> preDetailList) {
        for (PreRegistrationDetail detail : preDetailList) {
            if (detail.getPatient().equals(p)) {
                return true;
            }
        }
        return false;
    }
}
