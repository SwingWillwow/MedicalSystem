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
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.component.UIComponent;
import javax.faces.component.behavior.AjaxBehavior;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

/**
 *
 * @author qiuyukun
 */
public class AddEmployeeBean {

    /**
     * Creates a new instance of AddEmployeeBean
     */
    @PersistenceContext(unitName = "MedicalSystemPU")
    private EntityManager em;
    @Resource
    private UserTransaction utx;
    private Doctor doctor;
    private Employee employee;
    private List<Department> allDepts;
    private List<Sections> sections;
    private List<Registration> registrations;
    private int deptId;
    public AddEmployeeBean() {
        
    }

    @PostConstruct
    private void init(){
        Query query = em.createQuery("SELECT dept FROM Department dept");
        allDepts = query.getResultList();
        employee = new Employee();
        doctor = new Doctor();
        employee.setDepartment(new Department());
        doctor.setRegistration(new Registration());
        doctor.setSections(new Sections());
        query = em.createQuery("SELECT s FROM Sections s");
        sections = query.getResultList();
        query = em.createQuery("SELECT reg FROM Registration reg");
        registrations = query.getResultList();
    }
    
    
    //新增雇员
    public String newEmployee(){
        if(employee.getDepartment().getId()==4){
            addDoctor();
        }
        else{
            addEmployee();
        }
        return "/index.xhtml";
    }
    private void addDoctor(){
        setDoctorBasedOnEmployee();
        try {
            utx.begin();
            em.persist(doctor);
            Sections section = em.find(Sections.class, doctor.getSections().getId());
            section.setNumber(section.getNumber()+1);
//            List<Doctor> doctors = section.getDoctors();
//            doctors.add(doctor);
//            section.setDoctors(doctors);
            Department dept = em.find(Department.class, doctor.getDepartment().getId());
            dept.setNumber(dept.getNumber()+1);
//            List<Employee> employees = dept.getEmployees();
//            basicSetForEmployee();
//            employees.add(employee);
//            dept.setEmployees(employees);
            utx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void setDoctorBasedOnEmployee(){
        doctor.setUserName(employee.getUserName());
        doctor.setPassword(employee.getPassword());
        doctor.setName(employee.getName());
        doctor.setIdCard(employee.getIdCard());
        doctor.setSex(employee.getSex());
        doctor.setBirthday(employee.getBirthday());
        doctor.setDepartment(em.find(Department.class, employee.getDepartment().getId()));
        doctor.setCreateTime(new Date());
        doctor.setLastUpdateTime(new Date());
        doctor.setSections(em.find(Sections.class, doctor.getSections().getId()));
        doctor.setRegistration(em.find(Registration.class, doctor.getRegistration().getId()));
    }
    private void addEmployee(){
        basicSetForEmployee();
        try {
            utx.begin();
            em.persist(employee);
            Department dept = em.find(Department.class, employee.getDepartment().getId());
            dept.setNumber(dept.getNumber()+1);
//            List<Employee> employees = dept.getEmployees();
//            employees.add(employee);
//            dept.setEmployees(employees);
            utx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void basicSetForEmployee(){
        employee.setDepartment(em.find(Department.class, employee.getDepartment().getId()));
        employee.setCreateTime(new Date());
        employee.setLastUpdateTime(new Date());
    }
    //Ajax
    public void changeDepart(AjaxBehaviorEvent event){
        System.out.println(deptId);
    }
    
    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public List<Department> getAllDepts() {
        return allDepts;
    }

    public void setAllDepts(List<Department> allDepts) {
        this.allDepts = allDepts;
    }

    public int getDeptId() {
        return deptId;
    }

    public void setDeptId(int deptId) {
        this.deptId = deptId;
    }

    public List<Sections> getSections() {
        return sections;
    }

    public void setSections(List<Sections> sections) {
        this.sections = sections;
    }

    public List<Registration> getRegistrations() {
        return registrations;
    }

    public void setRegistrations(List<Registration> registrations) {
        this.registrations = registrations;
    }
    
}
