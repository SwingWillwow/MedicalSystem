/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jsfbean.admin;

import com.entity.Department;
import com.entity.Employee;
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
public class AddDepartBean {

    /**
     * Creates a new instance of AddDepartBean
     */
    @PersistenceContext(unitName = "MedicalSystemPU")
    private EntityManager em;
    @Resource
    private UserTransaction utx;
    private Department newDept;
    private List<Employee> allEmployees;

    public AddDepartBean() {

    }

    @PostConstruct
    private void init() {
        newDept = new Department();
        newDept.setManager(new Employee());
        Query query = em.createQuery("SELECT e FROM Employee e");
        allEmployees = query.getResultList();
        query = em.createQuery("SELECT d FROM Department d");
        List<Department> departments = query.getResultList();
        for (Department d : departments) {
            if (allEmployees.contains(d.getManager())) {
                allEmployees.remove(d.getManager());
            }
        }
    }

    public String addDept() {
        try {
            utx.begin();
            newDept.setCreateTime(new Date());
            Employee manager = em.find(Employee.class, newDept.getManager().getId());
            newDept.setManager(manager);
            newDept.setLastUpdateTime(new Date());
            newDept.setNumber(1);
            em.persist(newDept);
            Department oldDepart = em.find(Department.class, manager.getDepartment().getId());
            oldDepart.setNumber(oldDepart.getNumber() - 1);
            manager.setDepartment(newDept);
            utx.commit();
        } catch (Exception e) {
            SessionManagedBean.getInstance().setErrorMessage("新增部门失败");
            return "/admin/manageDepartment";
        }
        SessionManagedBean.getInstance().setSuccessMessage("新增部门成功");
        return "/admin/manageDepartment";
    }

    public Department getNewDept() {
        return newDept;
    }

    public void setNewDept(Department newDept) {
        this.newDept = newDept;
    }

    public List<Employee> getAllEmployees() {
        return allEmployees;
    }

    public void setAllEmployees(List<Employee> allEmployees) {
        this.allEmployees = allEmployees;
    }

}
