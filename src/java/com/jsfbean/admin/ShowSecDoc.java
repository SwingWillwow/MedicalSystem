/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jsfbean.admin;

import com.entity.Doctor;
import com.entity.Sections;
import com.util.ParamUtil;
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
public class ShowSecDoc {

    /**
     * Creates a new instance of ShowSecDoc
     */
    @PersistenceContext(unitName = "MedicalSystemPU")
    private EntityManager em;
    @Resource
    private UserTransaction utx;
    private List<Doctor> allSecDocs;
    private Long id;

    public ShowSecDoc() {
    }

    @PostConstruct
    private void init() {
        String sid = ParamUtil.getFlashParamByName(FacesContext.getCurrentInstance(), "sectionId");
        id = Long.parseLong(sid);
        Query query = em.createQuery("SELECT d FROM Doctor d WHERE d.sections = ?1");
        query.setParameter(1, em.find(Sections.class, id));
        allSecDocs = query.getResultList();
    }

    public List<Doctor> getAllSecDocs() {
        return allSecDocs;
    }

    public void setAllSecDocs(List<Doctor> allSecDocs) {
        this.allSecDocs = allSecDocs;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
