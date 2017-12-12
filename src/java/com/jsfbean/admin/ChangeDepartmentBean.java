/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jsfbean.admin;

import com.entity.Department;
import com.entity.Employee;
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
public class ChangeDepartmentBean {

    /**
     * Creates a new instance of ChangeDepartmentBean
     */
     @PersistenceContext(unitName = "MedicalSystemPU")
    private EntityManager em;
    @Resource
    private UserTransaction utx;
    private Department currentDept;
    private List<Employee> allEmployees;
    public ChangeDepartmentBean() {
        
    }
    @PostConstruct
    private void init(){
        String deptId = ParamUtil.getFlashParamByName(FacesContext.getCurrentInstance(), "deptId");
        Long id = Long.parseLong(deptId);
        currentDept = em.find(Department.class, id);
        currentDept.setManager(currentDept.getManager());
        Query query = em.createQuery("SELECT e FROM Employee e WHERE e.department = ?1");
        query.setParameter(1, currentDept);
        allEmployees = query.getResultList();
        
    }

    public String changeDeptInfo(){
        try {
            utx.begin();
            currentDept.setManager(em.find(Employee.class, currentDept.getManager().getId()));
            em.merge(currentDept);
            utx.commit();
        } catch (Exception e) {
            SessionManagedBean.getInstance().setErrorMessage("修改部门信息失败");
            return "/admin/manageDepartment";
        }
        SessionManagedBean.getInstance().setSuccessMessage("修改成功");
        return "/admin/manageDepartment";
    }
    
    /*
        getters and setters
     */
    
    public Department getCurrentDept() {
        return currentDept;
    }

    public void setCurrentDept(Department currentDept) {
        this.currentDept = currentDept;
    }

    public List<Employee> getAllEmployees() {
        return allEmployees;
    }

    public void setAllEmployees(List<Employee> allEmployees) {
        this.allEmployees = allEmployees;
    }
    
}
