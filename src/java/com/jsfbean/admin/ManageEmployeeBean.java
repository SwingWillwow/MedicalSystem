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
import java.io.Serializable;
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
public class ManageEmployeeBean implements Serializable {

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
    public void initBean() {
        //查询所有的员工
        Query query = em.createQuery("SELECT e FROM Employee e");
        employeeList = query.getResultList();
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

    public String toChangeEmployee() {
        Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        Long employeeId = Long.parseLong(request.getParameter("employeeId"));
        flash.put("employeeId", employeeId);
        return "/admin/changeEmployeeInfo";
    }

    public String deleteEmployee() {
        SessionManagedBean sessionManagedBean = SessionManagedBean.getInstance();
        String sid = ParamUtil.getParamByName(FacesContext.getCurrentInstance(), "employeeId");
        Long id = Long.parseLong(sid);
        if (!deleteEmployeeById(id)) {
            sessionManagedBean.setErrorMessage(sessionManagedBean.getErrorMessage() + "删除失败");
            return "/admin/manageEmployee.xhtml";
        }
        sessionManagedBean.setSuccessMessage("成功删除");
        initBean();
        return "/admin/manageEmployee.xhtml";
    }

    private boolean deleteEmployeeById(Long id) {
        try {
            if (em.find(Doctor.class, id) != null) {
                return deleteDoctorById(id);
            }
            utx.begin();
            Employee employee = em.find(Employee.class, id);
            if (isManager(employee)) {
                SessionManagedBean.getInstance().setErrorMessage("是部门负责人，不能删除。");
                return false;
            }
            Department dept = em.find(Department.class, employee.getDepartment().getId());
            dept.setNumber(dept.getNumber() - 1);
            em.remove(employee);
            utx.commit();
        } catch (Exception e) {
            SessionManagedBean.getInstance().setErrorMessage(e.getMessage());
            return false;
        }
        return true;
    }

    private boolean deleteDoctorById(Long id) {
        try {
            utx.begin();
            Doctor doctor = em.find(Doctor.class, id);
            if (isManager((Employee) doctor)) {
                SessionManagedBean.getInstance().setErrorMessage("是部门负责人，不能删除");
                return false;
            }
            if (isSectionManger(doctor)) {
                SessionManagedBean.getInstance().setErrorMessage("是科室负责人，不能删除");
                return false;
            }
            Department dept = em.find(Department.class, doctor.getDepartment().getId());
            dept.setNumber(dept.getNumber() - 1);
            Sections section = em.find(Sections.class, doctor.getSections().getId());
            section.setNumber(section.getNumber() - 1);
            em.remove(doctor);
            utx.commit();
        } catch (Exception e) {
        }
        return true;
    }

    private boolean isSectionManger(Doctor doctor) {
        Query query = em.createQuery("SELECT sec FROM Sections sec");
        List<Sections> sections = query.getResultList();
        for (Sections section : sections) {
            if (section.getManager().equals(doctor)) {
                return true;
            }
        }
        return false;
    }

    private boolean isManager(Employee employee) {
        Query query = em.createQuery("SELECT dept FROM Department dept");
        List<Department> alldept = query.getResultList();
        for (Department dept : alldept) {
            if (dept.getManager().equals(employee)) {
                return true;
            }
        }
        return false;
    }

}
