/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jsfbean.admin;

import com.entity.Department;
import com.entity.Doctor;
import com.entity.Employee;
import com.entity.Registration;
import com.entity.Sections;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

/**
 *
 * @author qiuyukun
 */
public class ChangeEmployeeInfoBean implements Serializable {

    //properties
    private Employee currentEmployee;
    @PersistenceContext(unitName = "MedicalSystemPU")
    private EntityManager em;
    @Resource
    private UserTransaction utx;
    private List<Department> allDepart;
    private Registration registration;
    private List<Registration> allRegistrations;
    private List<Sections> allSections;
    private Long departId;
    private Long secId;

    /**
     * Creates a new instance of changeEmployeeInfoBean
     */
    public ChangeEmployeeInfoBean() {

    }

    /*
        初始化方法
     */
    @PostConstruct
    public void init() {
        //通过传入的id来定位对应的Employee
        Long employeeId;
        Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
        employeeId = Long.parseLong(flash.get("employeeId").toString());
        currentEmployee = em.find(Employee.class, employeeId);
        Query query = em.createQuery("SELECT dept FROM Department dept");
        departId = currentEmployee.getDepartment().getId();
        allDepart = query.getResultList();
        if (currentEmployee.getDepartment().getId() == 4L) {
            Doctor d = em.find(Doctor.class, currentEmployee.getId());
            secId = d.getSections().getId();
        }
        query = em.createQuery("SELECT s FROM Sections s");
        allSections = query.getResultList();
        if (currentEmployee.getDepartment().getId() == 4L) {
            Doctor doc = em.find(Doctor.class, currentEmployee.getId());
            registration = doc.getRegistration();
        }
        query = em.createQuery("SELECT re FROM Registration re");
        allRegistrations = query.getResultList();
    }

    /**
     *
     * @return managedPage
     */
    public String changeInfo() {
        try {
            utx.begin();
            Department oldDepartment = em.find(Department.class, currentEmployee.getDepartment().getId());
            Department selectedDepartment = em.find(Department.class, departId);
            oldDepartment.setNumber(oldDepartment.getNumber() - 1);
            selectedDepartment.setNumber(selectedDepartment.getNumber() + 1);
            currentEmployee.setDepartment(selectedDepartment);
            em.merge(currentEmployee);
            if (currentEmployee.getDepartment().getId() == 4L) {
                Doctor d = em.find(Doctor.class, currentEmployee.getId());
                registration = em.find(Registration.class, registration.getId());
                Sections section = em.find(Sections.class, secId);
                Sections oldSection = em.find(Sections.class, d.getSections().getId());
                oldSection.setNumber(oldSection.getNumber() - 1);
                section.setNumber(section.getNumber() + 1);
                d.setSections(section);
                d.setRegistration(registration);
                em.merge(d);
            }
            utx.commit();
        } catch (Exception e) {

        }
        return "/admin/manageEmployee";
    }

    public Employee getCurrentEmployee() {
        return currentEmployee;
    }

    public void setCurrentEmployee(Employee currentEmployee) {
        this.currentEmployee = currentEmployee;
    }

    public List<Department> getAllDepart() {
        return allDepart;
    }

    public void setAllDepart(List<Department> allDepart) {
        this.allDepart = allDepart;
    }

    public Registration getRegistration() {
        return registration;
    }

    public void setRegistration(Registration registration) {
        this.registration = registration;
    }

    public List<Registration> getAllRegistrations() {
        return allRegistrations;
    }

    public void setAllRegistrations(List<Registration> allRegistrations) {
        this.allRegistrations = allRegistrations;
    }

    public Long getDepartId() {
        return departId;
    }

    public void setDepartId(Long departId) {
        this.departId = departId;
    }

    public List<Sections> getAllSections() {
        return allSections;
    }

    public void setAllSections(List<Sections> allSections) {
        this.allSections = allSections;
    }

    public Long getSecId() {
        return secId;
    }

    public void setSecId(Long secId) {
        this.secId = secId;
    }

}
