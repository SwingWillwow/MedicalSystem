/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jsfbean.admin;

import com.entity.Doctor;
import com.entity.Sections;
import com.jsfbean.SessionManagedBean;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

/**
 *
 * @author qiuyukun
 */
public class AddSectionBean {

    /**
     * Creates a new instance of AddSectionBean
     */
    @PersistenceContext(unitName = "MedicalSystemPU")
    private EntityManager em;
    @Resource
    private UserTransaction utx;
    private Sections newSec;
    private List<Doctor> AllDoctor;

    public AddSectionBean() {
    }

    @PostConstruct
    private void init() {
        newSec = new Sections();
        newSec.setManager(new Doctor());
        Query query = em.createQuery("SELECT d FROM Doctor d");
        AllDoctor = query.getResultList();
        query = em.createQuery("SELECT s FROM Sections s");
        List<Sections> sections = query.getResultList();
        for (Sections s : sections) {
            if (AllDoctor.contains(s.getManager())) {
                AllDoctor.remove(s.getManager());
            }
        }
    }

    public String addSec() {
        try {
            utx.begin();
            newSec.setCreateTime(new Date());
            Doctor manager = em.find(Doctor.class, newSec.getManager().getId());
            newSec.setManager(manager);
            newSec.setLastUpdateTime(new Date());
            newSec.setNumber(1);
            em.persist(newSec);
            Sections oldSec = em.find(Sections.class, manager.getSections().getId());
            oldSec.setNumber(oldSec.getNumber() - 1);
            manager.setSections(newSec);
            utx.commit();
        } catch (Exception e) {
            SessionManagedBean.getInstance().setErrorMessage("新增科室失败");
            return "/admin/manageSection";
        }
        SessionManagedBean.getInstance().setSuccessMessage("新增科室成功");
        return "/admin/manageSection";
    }

    public Sections getNewSec() {
        return newSec;
    }

    public void setNewSec(Sections newSec) {
        this.newSec = newSec;
    }

    public List<Doctor> getAllDoctor() {
        return AllDoctor;
    }

    public void setAllDoctor(List<Doctor> AllDoctor) {
        this.AllDoctor = AllDoctor;
    }

}
