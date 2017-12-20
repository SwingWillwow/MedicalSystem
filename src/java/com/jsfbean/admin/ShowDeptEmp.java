/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jsfbean.admin;

import com.entity.Department;
import com.entity.Employee;
import com.util.ParamUtil;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.inject.Named;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

/**
 *
 * @author qiuyukun
 */
@Named(value = "showDeptEmp")
@ViewScoped
@ManagedBean
public class ShowDeptEmp {

    /**
     * Creates a new instance of showDeptEmp
     */
    @PersistenceContext(unitName = "MedicalSystemPU")
    private EntityManager em;
    @Resource
    private UserTransaction utx;
    private List<Employee> allDeptEmps;
    private Long id;

    public ShowDeptEmp() {

    }

    @PostConstruct
    private void init() {
        String sid = ParamUtil.getFlashParamByName(FacesContext.getCurrentInstance(), "deptId");
        id = Long.parseLong(sid);
        Query query = em.createQuery("SELECT e FROM Employee e WHERE e.department = ?1");
        query.setParameter(1, em.find(Department.class, id));
        allDeptEmps = query.getResultList();
    }

    public List<Employee> getAllDeptEmps() {
        return allDeptEmps;
    }

    public void setAllDeptEmps(List<Employee> allDeptEmps) {
        this.allDeptEmps = allDeptEmps;
    }

}
