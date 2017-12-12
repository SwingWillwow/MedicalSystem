/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jsfbean.admin;

import com.entity.Department;
import com.entity.Doctor;
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
public class ManageSectionBean {

    /**
     * Creates a new instance of ManageSectionBean
     */
    @PersistenceContext(unitName = "MedicalSystemPU")
    private EntityManager em;
    @Resource
    private UserTransaction utx;
    private List<Sections> allSections;
    public ManageSectionBean() {
        
    }
    @PostConstruct
    private void init(){
        Query query = em.createQuery("SELECT s FROM Sections s");
        allSections = query.getResultList();
    }

    public List<Sections> getAllSections() {
        return allSections;
    }

    public void setAllSections(List<Sections> allSections) {
        this.allSections = allSections;
    }
    
    public String toChangeSection(){
        String sectionId = ParamUtil.getParamByName(FacesContext.getCurrentInstance(), "sectionId");
        Long id = Long.parseLong(sectionId);
        ParamUtil.setParamIntoFlash(FacesContext.getCurrentInstance(), "sectionId", id);
        return "/admin/changeSection.xhtml";
    }
    
    public String toShowSectionEmp(){
        String sectionId = ParamUtil.getParamByName(FacesContext.getCurrentInstance(), "sectionId");
        Long id = Long.parseLong(sectionId);
        ParamUtil.setParamIntoFlash(FacesContext.getCurrentInstance(), "sectionId", id);
        return "/admin/showSecEmp.xhtml";
    }
    public String deleteSection(){
        String sectionId = ParamUtil.getParamByName(FacesContext.getCurrentInstance(), "sectionId");
        Long id = Long.parseLong(sectionId);
        if(tryDeleteSectionById(id)){
            SessionManagedBean.getInstance().setSuccessMessage("成功删除");
        }
        else{
            SessionManagedBean.getInstance().setErrorMessage(SessionManagedBean.getInstance().getErrorMessage()+"删除失败");
        }
        return "/admin/manageSection";
    }
    private boolean tryDeleteSectionById(Long id){
        try {
            utx.begin();
            Sections section = em.find(Sections.class, id);
            List<Doctor> doctors=section.getDoctors();
            Sections tmpSection = em.find(Sections.class, 9L);
            for(Doctor d:doctors){
                d.setSections(tmpSection);
                tmpSection.setNumber(tmpSection.getNumber()+1);
                em.merge(d);
            }
            em.remove(section);
            utx.commit();
        } catch (Exception e) {
        }
        return true;
    }
}
