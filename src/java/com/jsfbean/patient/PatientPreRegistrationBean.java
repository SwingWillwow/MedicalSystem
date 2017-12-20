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
import com.util.ParamUtil;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

/**
 *
 * @author qiuyukun
 */
public class PatientPreRegistrationBean implements Serializable {

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
    public void initPatientRegistDetails() {
        SessionManagedBean sessionManagedBean = SessionManagedBean.getInstance();
        Patient p = (Patient) sessionManagedBean.getSession().getAttribute("userInfo");
        Query query = em.createQuery("SELECT preDetail FROM PreRegistrationDetail preDetail WHERE PREDETAIL.patient=?1");
        query.setParameter(1, p);
        patientRegistDetails = query.getResultList();
    }

    //取消预约
    public String cancelRegist() {
        SessionManagedBean sessionManagedBean = SessionManagedBean.getInstance();
        Long preRegistId = Long.parseLong(ParamUtil.getParamByName(FacesContext.getCurrentInstance(), "preRegistId"));
        PreRegistrationDetail detail = em.find(PreRegistrationDetail.class, preRegistId);
        if (detail.getValid() == 'N') {
            sessionManagedBean.setErrorMessage("过期记录，无法取消");
            return "";
        }
        if (deleteRegistRecord(detail)) {
            sessionManagedBean.setSuccessMessage("取消成功");
            initPatientRegistDetails();
        } else {
            sessionManagedBean.setErrorMessage("取消失败");
        }
        return "";
    }

    //删除预约记录
    private boolean deleteRegistRecord(PreRegistrationDetail detail) {
        PreRegistration pre;
        pre = em.find(PreRegistration.class, detail.getPreRegistrationId().getId());
        if (pre == null || !pre.getPreResgistrationDetails().contains(detail)) {
            return false;
        } else {
            List<PreRegistrationDetail> list = pre.getPreResgistrationDetails();
            list.remove(detail);
            try {
                utx.begin();
                pre = em.merge(pre);
                pre.setPreResgistrationDetails(list);
                pre.setByInternetReal(pre.getByInternetReal() - 1);
                detail = em.merge(detail);
                em.remove(detail);
                utx.commit();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }
    }

    public List<PreRegistrationDetail> getPatientRegistDetails() {
        return patientRegistDetails;
    }

    public void setPatientRegistDetails(List<PreRegistrationDetail> patientRegistDetails) {
        this.patientRegistDetails = patientRegistDetails;
    }

}
