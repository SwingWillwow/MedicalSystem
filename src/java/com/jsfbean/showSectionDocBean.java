/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jsfbean;

import com.entity.Doctor;
import com.entity.Sections;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.inject.Named;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

/**
 *
 * @author qiuyukun
 */
@Named(value = "showSectionDocBean")
@ManagedBean
@ViewScoped
public class showSectionDocBean {

    /**
     * Creates a new instance of showSectionDocBean
     */
    //医生列表
    private Sections section;
    private List<Doctor> doctors;
    @PersistenceContext(unitName = "MedicalSystemPU")
    private EntityManager em;
    @Resource
    private UserTransaction utx;

    public showSectionDocBean() {

    }

    @PostConstruct
    private void init() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        Long sectionId = Long.parseLong(session.getAttribute("sectionId").toString());
        section = em.find(Sections.class, sectionId);
        doctors = section.getDoctors();
    }

    public String toDocInfo() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        String docId = request.getParameter("docId");
        Flash flash = facesContext.getExternalContext().getFlash();
        flash.put("docId", docId);
        return "/showDocInfo.xhtml";
    }

    public Sections getSection() {
        return section;
    }

    public void setSection(Sections section) {
        this.section = section;
    }

    public List<Doctor> getDoctors() {
        return doctors;
    }

    public void setDoctors(List<Doctor> doctors) {
        this.doctors = doctors;
    }
}
