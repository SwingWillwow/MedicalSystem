/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jsfbean.admin;

import com.entity.Department;
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
public class ManageDepartmentBean {

    /**
     * Creates a new instance of ManageDepartmentBean
     */
    @PersistenceContext(unitName = "MedicalSystemPU")
    private EntityManager em;
    @Resource
    private UserTransaction utx;
    private List<Department> allDepartments;

    public ManageDepartmentBean() {

    }

    @PostConstruct
    private void init() {
        Query query = em.createQuery("SELECT dept FROM Department dept");
        allDepartments = query.getResultList();

    }

    public List<Department> getAllDepartments() {
        return allDepartments;
    }

    public void setAllDepartments(List<Department> allDepartments) {
        this.allDepartments = allDepartments;
    }

    public String tochangedept() {
        String deptId = ParamUtil.getParamByName(FacesContext.getCurrentInstance(), "deptId");
        Long id = Long.parseLong(deptId);
        ParamUtil.setParamIntoFlash(FacesContext.getCurrentInstance(), "deptId", id);
        return "/admin/changeDepartment.xhtml";
    }

    public String showEmployeeList() {
        String deptId = ParamUtil.getParamByName(FacesContext.getCurrentInstance(), "deptId");
        Long id = Long.parseLong(deptId);
        ParamUtil.setParamIntoFlash(FacesContext.getCurrentInstance(), "deptId", id);
        return "/admin/showDeptEmpList.xhtml";
    }
}
