/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jsfbean;

import com.entity.Patient;
import com.entity.PreRegistration;
import com.entity.PreRegistrationDetail;
import com.util.DateOperator;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;
import jdk.nashorn.internal.objects.annotations.Constructor;

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
    
//    @PostConstruct
//    public void initPreList(){
//        
//    }
    
    public String preRegist(){
        SessionManagedBean sessionManagedBean;
            HttpSession session = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            if(session.getAttribute("sessionManagedBean")==null){
                sessionManagedBean = new SessionManagedBean();
            }
            else{
                sessionManagedBean=(SessionManagedBean)session.getAttribute("sessionManagedBean");
            }
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        Long preId = Long.parseLong(request.getParameter("preId"));
        PreRegistration pre = em.find(PreRegistration.class, preId);
        //判断预约人数，即能否预约
        if(pre.getByInternetReal()>=pre.getByInternet()){
            sessionManagedBean.setErrorMessage("预约人数过多，预约失败。");
            return "/index";
        }
        pre.setByInternetReal(pre.getByInternetReal()+1);
        pre.setCount(pre.getCount());
        PreRegistrationDetail preRegistrationDetail = new PreRegistrationDetail();
        preRegistrationDetail.setCreateTime(new Date());
        preRegistrationDetail.setLastUpdateTime(new Date());
        Patient p = (Patient)sessionManagedBean.getSession().getAttribute("userInfo");
        preRegistrationDetail.setPatient(p);
        preRegistrationDetail.setValid('Y');
        preRegistrationDetail.setPreRegistrationId(pre);
        List<PreRegistrationDetail> preDetailList = pre.getPreResgistrationDetails();
        preDetailList.add(preRegistrationDetail);
        pre.setPreResgistrationDetails(preDetailList);
        try{
            utx.begin();
            em.persist(preRegistrationDetail);
            em.merge(pre);
            utx.commit();
        }
        catch(Exception e){
            
            sessionManagedBean.setErrorMessage(e.getMessage());
        }
        sessionManagedBean.setSuccessMessage("预约成功");
        return "/index";
    }

    
    
    public List<PreRegistration> getPreList() {
        String sid=null;
        Long docId=null;
        if(FacesContext.getCurrentInstance().getExternalContext().getFlash().get("docId")!=null){
            sid = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("docId").toString();
            docId = Long.parseLong(sid);
        }
        if (docId==null) {
            return preList;
        }
        Query query = em.createQuery("SELECT pre FROM PreRegistration pre WHERE pre.doctor.id = ?1 AND pre.preTime BETWEEN ?2 AND ?3");
        query.setParameter(1, docId);
        query.setParameter(2, DateOperator.addDay(new Date(), 1));
        query.setParameter(3, DateOperator.addDay(new Date(), 5));
        preList = query.getResultList();
        return preList;
    }

    public void setPreList(List<PreRegistration> preList) {
        this.preList = preList;
    }
    
    
}
