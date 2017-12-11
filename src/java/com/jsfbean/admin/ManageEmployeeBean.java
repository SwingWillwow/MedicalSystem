/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jsfbean.admin;

import com.entity.Employee;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.UserTransaction;

/**
 *
 * @author qiuyukun
 */
public class ManageEmployeeBean implements Serializable{

    //properties
    private List<Employee> employeeList;
    @PersistenceContext(unitName = "MedicalSystemPU")
    private EntityManager em;
    @Resource
    private UserTransaction utx;
    /**
     * Creates a new instance of manageEmployee
     */
    public ManageEmployeeBean() {
        
    }
    /*
        初始化数据
    */
    @PostConstruct
    public void initBean(){
        //查询所有的员工
        Query query = em.createQuery("SELECT e FROM Employee e");
        employeeList =  query.getResultList();
    }

    /*
        getters and setters
    */
    public List<Employee> getEmployeeList() {
        return employeeList;
    }

    public void setEmployeeList(List<Employee> employeeList) {
        this.employeeList = employeeList;
    }
    
    public String toChangeEmployee(){
        Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        Long employeeId = Long.parseLong(request.getParameter("employeeId"));
        flash.put("employeeId", employeeId);
        return "/admin/changeEmployeeInfo";
    }
    
    
}
