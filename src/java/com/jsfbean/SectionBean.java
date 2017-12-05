/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jsfbean;

import com.entity.Doctor;
import com.entity.PreRegistration;
import com.entity.Sections;
import com.util.DateOperator;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
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
@ViewScoped
public class SectionBean {

    /**
     * Creates a new instance of SectionBean
     */
    private List<Sections> section;
    @PersistenceContext(unitName = "MedicalSystemPU")
    private EntityManager em;
    @Resource
    private UserTransaction utx;
    private List<Doctor> doctor;
    private String selectedDoctor;
    private String selectedSections;

    public SectionBean() {

    }

    public void initDoctor(AjaxBehaviorEvent event) {
        if (!(selectedSections==null)) {
            Long id = Long.parseLong(selectedSections);
            if (id == 0) {
                doctor = null;
                return;
            }
            Sections sections = em.find(Sections.class, id);
            doctor = sections.getDoctors();
        }
    }

    public String toPatientRegistrationInformation() {
        SessionManagedBean sessionManagedBean;
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        if (session.getAttribute("sessionManagedBean") == null) {
            sessionManagedBean = new SessionManagedBean();
        } else {
            sessionManagedBean = (SessionManagedBean) session.getAttribute("sessionManagedBean");
        }
        int selectedDoc = 0, selectedSec = 0;
        selectedDoc = Integer.parseInt(selectedDoctor);
        selectedSec = Integer.parseInt(selectedSections);
        if (selectedDoc == 0 || selectedSec == 0) {

            sessionManagedBean.setErrorMessage("请选择要预约的科室和医生");
            return "";
        }
        passDocId();
        Date today = new Date();
        List<PreRegistration> pre;
        Query query = em.createQuery("SELECT pre FROM PreRegistration pre WHERE PRE.doctor.id=?1 ORDER BY pre.preTime DESC");
        query.setParameter(1, selectedDoc);
        pre = query.getResultList();
        Iterator iterator = pre.iterator();
        // has preRegistration record
        if (iterator.hasNext()) {
            PreRegistration preRegistration = (PreRegistration) iterator.next();
            Date lastPreTime = preRegistration.getPreTime();
            // add at most five record at once
            if (DateOperator.isAfterDate(DateOperator.addDay(today, 5), lastPreTime)) {
                for (int i = 0; i <= 5; i++) {
                    if (DateOperator.isBeforeDate(DateOperator.addDay(today, i), lastPreTime)) {
                        continue;
                    }
                    PreRegistration newPreRegistration = new PreRegistration();
                    Doctor doc = em.find(Doctor.class, Long.parseLong(selectedDoctor));
                    newPreRegistration.setByInternet(doc.getByInternet());
                    newPreRegistration.setByInternetReal(0);
                    newPreRegistration.setByLive(doc.getByLive());
                    newPreRegistration.setByLiveReal(0);
                    newPreRegistration.setCount(0);
                    newPreRegistration.setCreateTime(new Date());
                    newPreRegistration.setDoctor(doc);
                    newPreRegistration.setLastUpdateTime(new Date());
                    newPreRegistration.setPreResgistrationDetails(null);
                    newPreRegistration.setPreTime(DateOperator.addDay(today, i));
                    try {
                        utx.begin();
                        em.persist(newPreRegistration);
                        utx.commit();
                    } catch (Exception e) {
                        sessionManagedBean.setErrorMessage(e.getMessage());
                    }
                }
            }
        }
        else{//如果不存在记录，则直接新增5条记录
            for(int i=1;i<=5;i++){
                PreRegistration preReg2 = new PreRegistration();
                Doctor doc = em.find(Doctor.class, Long.parseLong(selectedDoctor));
                //设置值
                preReg2.setByInternet(doc.getByInternet());
                preReg2.setByInternetReal(0);
                preReg2.setByLive(doc.getByLive());
                preReg2.setByLiveReal(0);
                preReg2.setCount(0);
                preReg2.setCreateTime(new Date());
                preReg2.setDoctor(doc);
                preReg2.setLastUpdateTime(new Date());
                preReg2.setPreResgistrationDetails(null);
                preReg2.setPreTime(DateOperator.addDay(new Date(), i));
                try {
                    utx.begin();
                    em.persist(preReg2);
                    utx.commit();
                } catch (Exception e) {
                    sessionManagedBean.setErrorMessage(e.getMessage());
                }
            }
        }
        return "patientRegistrationInformation";
    }

    @PostConstruct
    public void initSection() {
        Query query = em.createQuery("SELECT s FROM Sections s");
        section = query.getResultList();
        //Sections sections = em.find(Sections.class, 1L);
        //doctor = sections.getDoctors();
    }

    //pass docId to next page
    public void passDocId(){
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        externalContext.getFlash().put("docId", selectedDoctor);
    }
    
    
    /*
        getter and setter
     */
    public List<Sections> getSection() {
        return section;
    }

    public void setSection(List<Sections> section) {
        this.section = section;
    }

    public List<Doctor> getDoctor() {
        
        return doctor;
    }

    public void setDoctor(List<Doctor> doctor) {
        this.doctor = doctor;
    }

    public String getSelectedDoctor() {
        return selectedDoctor;
    }

    public void setSelectedDoctor(String selectedDoctor) {
        this.selectedDoctor = selectedDoctor;
    }

    public String getSelectedSections() {
        return selectedSections;
    }

    public void setSelectedSections(String selectedSections) {
        this.selectedSections = selectedSections;
    }

    
}
