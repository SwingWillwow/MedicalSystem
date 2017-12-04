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
import javax.annotation.Resource;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
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
    private int selectedDoctor;
    private int selectedSections;

    public SectionBean() {

    }

    //valueChangeListener
    public void initDoctor(ValueChangeEvent event) {
        String sid = event.getNewValue().toString();
        Long id = Long.parseLong(sid);
        Sections sections = em.find(Sections.class, id);
        doctor = sections.getDoctors();
    }

    public String toPatientRegistrationInformation() {
        SessionManagedBean sessionManagedBean;
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        if (session.getAttribute("sessionManagedBean") == null) {
                sessionManagedBean = new SessionManagedBean();
            } else {
                sessionManagedBean = (SessionManagedBean) session.getAttribute("sessionManagedBean");
            }
        if (selectedDoctor == 0 || selectedSections == 0) {
                        
            sessionManagedBean.setErrorMessage("请选择要预约的科室和医生");
            return "";
        }
        Date today = new Date();
        List<PreRegistration> pre;
        Query query = em.createQuery("SELECT pre FROM PreRegistration pre WHERE PRE.doctor.id=?1 ORDER BY pre.preTime DESC");
        query.setParameter(1, selectedDoctor);
        pre = query.getResultList();
        Iterator iterator = pre.iterator();
        // has preRegistration record
        if (iterator.hasNext()) {
            PreRegistration preRegistration = (PreRegistration) iterator.next();
            Date lastPreTime = preRegistration.getPreTime();
            // add at most five record at once
            if (DateOperator.isAfterDate(DateOperator.addDay(today, 5), lastPreTime)) {
                for (int i = 0; i <= 5; i++) {
                    if(DateOperator.isBeforeDate(DateOperator.addDay(today, i), lastPreTime)){
                        continue;
                    }
                    PreRegistration newPreRegistration = new PreRegistration();
                    Doctor doc = preRegistration.getDoctor();
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
        return "patientRegistrationInformation";
    }

    /*
        getter and setter
     */
    public List<Sections> getSection() {

        Query query = em.createQuery("SELECT s FROM Sections s");
        section = query.getResultList();
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

    public int getSelectedDoctor() {
        return selectedDoctor;
    }

    public void setSelectedDoctor(int selectedDoctor) {
        this.selectedDoctor = selectedDoctor;
    }

    public int getSelectedSections() {
        return selectedSections;
    }

    public void setSelectedSections(int selectedSections) {
        this.selectedSections = selectedSections;
    }

}
