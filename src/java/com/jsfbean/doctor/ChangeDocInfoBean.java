/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jsfbean.doctor;

import com.entity.Doctor;
import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

/**
 *
 * @author qiuyukun
 */
public class ChangeDocInfoBean {

    //properties
    @PersistenceContext(unitName = "MedicalSystemPU")
    private EntityManager em;
    @Resource
    private UserTransaction utx;

    /**
     * Creates a new instance of ChangeDocInfoBean
     */
    public ChangeDocInfoBean() {
    }

    public String changeInformation() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        Doctor doctor = (Doctor) session.getAttribute("userInfo");
        try {
            utx.begin();
            em.merge(doctor);
            utx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/doctor/showDocInfo.xhtml";
    }
}
