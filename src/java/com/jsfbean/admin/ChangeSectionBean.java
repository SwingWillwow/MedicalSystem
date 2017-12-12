/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jsfbean.admin;

import com.entity.Department;
import com.entity.Doctor;
import com.entity.Employee;
import com.entity.Sections;
import com.jsfbean.SessionManagedBean;
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
public class ChangeSectionBean {

    /**
     * Creates a new instance of ChangeSectionBean
     */
     @PersistenceContext(unitName = "MedicalSystemPU")
    private EntityManager em;
    @Resource
    private UserTransaction utx;
    private Sections currentSection;
    private List<Doctor> allDoctors;
    
    public ChangeSectionBean() {
    }
    @PostConstruct
    private void init(){
        String sectionId = ParamUtil.getFlashParamByName(FacesContext.getCurrentInstance(), "sectionId");
        Long id = Long.parseLong(sectionId);
        currentSection = em.find(Sections.class, id);
        currentSection.setManager(currentSection.getManager());
        Query query = em.createQuery("SELECT d FROM Doctor d WHERE d.sections=?1");
        query.setParameter(1, currentSection);
        allDoctors = query.getResultList();
    }

    public String changeSectionInfo(){
        try {
            utx.begin();
            currentSection.setManager(em.find(Doctor.class, currentSection.getManager().getId()));
            em.merge(currentSection);
            utx.commit();
        } catch (Exception e) {
            SessionManagedBean.getInstance().setErrorMessage("修改部门信息失败");
            return "/admin/manageSection";
        }
        SessionManagedBean.getInstance().setSuccessMessage("修改成功");
        return "/admin/manageSection";
    }
    
    public Sections getCurrentSection() {
        return currentSection;
    }

    public void setCurrentSection(Sections currentSection) {
        this.currentSection = currentSection;
    }

    public List<Doctor> getAllDoctors() {
        return allDoctors;
    }

    public void setAllDoctors(List<Doctor> allDoctors) {
        this.allDoctors = allDoctors;
    }
    
}
