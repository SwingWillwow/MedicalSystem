/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jsfbean;

import com.entity.Doctor;
import com.entity.Employee;
import com.entity.Patient;
import com.entity.Users;
import com.util.PasswordManager;
import javax.annotation.Resource;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

/**
 *
 * @author qiuyukun
 */
@SessionScoped
public class SessionManagedBean {

    /**
     * Creates a new instance of SessionManagedBean
     * WARNING!!!!!!!!!!!!
     * this bean may be lead to some mistake please be carefully.
     * if meet some bugs about session, check this bean first.
     */
    private String hello = "hello world!";
    private HttpSession session;
    @PersistenceContext(unitName = "MedicalSystemPU")
    private EntityManager em;
    @Resource
    private UserTransaction utx;
    private String errorMessage;

    
    public SessionManagedBean() {
        initalSession();
        
    }
    
    public boolean isLogin(){
        initalSession();
        if(session.getAttribute("userName")!=null){
            return true;
        }
        else{
            return false;
        }
    }
    public HttpSession getSession(){
        return session;
    }
    
    public String userLogout(){
        session.invalidate();
        return "index";
    }
    
    // 用户登录（传入账号和密码）
    public boolean userLogin(String userName,String password){
        initalSession();
        if(session.getAttribute("userName")!=null){
            return false;
        }
        else{
            Query query=em.createNamedQuery("getUserByName");
            query.setParameter(1, userName);
            Users u=(Users)query.getSingleResult();
            if(u!=null){
                try {
                    if (PasswordManager.checkPassword(password, u.getPassword())) {
                        session.setAttribute("userName", userName);
                        session.setAttribute("id", u.getId());
                        setInfo(u.getId());
                        return true;
                    }
                    else{
                        return false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else{
                return false;
            }
            
        }
        return false;
    }
    
    public void setInfo(long id){
        if(isPaitent(id))return;
        if(isDoctor(id)) return;
        isEmpolyee(id);
    }
    
    //判断是不是病人，如果是设置它的信息
    public boolean isPaitent(long id){
        Patient p=em.find(Patient.class, id);
        if (p!=null) {
            session.setAttribute("userInfo", p);
            return true;
        }
        else{
            return false;
        }
    }
    //判断是不是医生，如果是设置它的信息
    public boolean isDoctor(long id){
        Doctor d = em.find(Doctor.class, id);
        if(d!=null){
            session.setAttribute("userInfo", d);
            return true;
        }
        else{
            return false;
        }
    }
    //判断是不是其他类型的雇员，如果是，设置它的信息
    public boolean isEmpolyee(long id){
        Employee e = em.find(Employee.class, id);
        if(e!=null){
            session.setAttribute("userInfo", e);
            return true;
        }
        else{
            return false;
        }
    }
    
    public boolean hasErrorMessage(){
        if(errorMessage!=null){
            return true;
        }
        else{
            return false;
        }
    }
    
    public String getErrorMessage() {
        String oldStr = errorMessage;
        errorMessage=null;
        return oldStr;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    public void initalSession(){
        if(session == null)session = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    }

    public String getHello() {
        return hello;
    }

    public void setHello(String hello) {
        hello = hello;
    }
    
    
}
