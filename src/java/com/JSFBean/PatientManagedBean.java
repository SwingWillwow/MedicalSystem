/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.JSFBean;

import com.Entity.Patient;
import java.time.Instant;
import java.util.Date;
import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;
import javax.transaction.UserTransaction;

/**
 *
 * @author qiuyukun
 */
public class PatientManagedBean {

    /**
     * Creates a new instance of PatientManagedBean
     */
    @PersistenceContext(unitName = "MedicalSystemPU")
    private EntityManager em;
    @Resource
    private UserTransaction utx;
    private String userName;
    private String password;
    private char sex;
    private Date birthday;
    private String idCard;
    private long phone;
    private String emergencyName;
    private long emergencyPhone;
    private String address;
    private String description;
    private int count=0;
    private Date createTime;
    private Date lastUpdateTime;
    private Patient patient;
    private String name;
    private String captcha;
    public PatientManagedBean() {
        
    }

    public String newPatient(){
        //inital SessionManagedBean
        SessionManagedBean sessionManagedBean;
            if((sessionManagedBean=(SessionManagedBean)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessionManagedBean"))==null){
                sessionManagedBean = new SessionManagedBean();
            }
        //may lead to mistake add try-catch later 
        lastUpdateTime = new Date();
        createTime = new Date();
        patient = new Patient(userName,password,name,sex,birthday,idCard,phone,emergencyName,emergencyPhone,address,description,count,createTime,lastUpdateTime);
        System.out.println(name);
        try{
           //em.getTransaction();
           utx.begin();
           em.persist(patient);
           utx.commit();
           
            sessionManagedBean.userLogin(userName);
            sessionManagedBean.setErrorMessage(null);
            return "index";
        }
        catch(Exception e){
            e.printStackTrace();
            sessionManagedBean.setErrorMessage(e.getMessage());
            return "index";
        }finally{
            
        }
    }
    /*
        check captcha
    */
    public boolean checkCaptcha(){
        return false;
    }
    
    
    /*
    There're all the getter and setter's
    */
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public char getSex() {
        return sex;
    }

    public void setSex(char sex) {
        this.sex = sex;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }

    public String getEmergencyName() {
        return emergencyName;
    }

    public void setEmergencyName(String emergencyName) {
        this.emergencyName = emergencyName;
    }

    public long getEmergencyPhone() {
        return emergencyPhone;
    }

    public void setEmergencyPhone(long emergencyPhone) {
        this.emergencyPhone = emergencyPhone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }
    
    
}
