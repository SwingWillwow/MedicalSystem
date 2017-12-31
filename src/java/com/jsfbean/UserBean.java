/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jsfbean;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author qiuyukun
 */
public class UserBean {

    /**
     * Creates a new instance of UserBean
     */
    private String userName;
    private String password;
    private CaptchaBean captchaBean;
    private String captcha;

    public UserBean() {
        captchaBean = new CaptchaBean();
    }

    public String dealLogin() {
        SessionManagedBean sessionManagedBean = null;
        if ((sessionManagedBean = (SessionManagedBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessionManagedBean")) == null) {
            sessionManagedBean = new SessionManagedBean();
        }
        if(!validateCaptcha()){
            sessionManagedBean.setErrorMessage("验证码错误");
            return "";
        }
        if (sessionManagedBean.userLogin(userName, password)) {
            return "index";
        } else {
            sessionManagedBean.setErrorMessage("登录失败。");
            return "";
        }
    }

    /*
    *  validateCaptcha
     */
    private boolean validateCaptcha(){
        if(!captcha.equals(captchaBean.getLastCapValue())){
            return false;
        }
        else{
            return true;
        }
    }
//    public void validateCaptcha(FacesContext context, UIComponent toValidate, Object value) throws ValidatorException {
//        String val = value.toString();
//        if (!val.equals(captchaBean.getLastCapValue())) {
//            FacesMessage facesMessage = new FacesMessage("验证码错误");
//            try {
//            } catch (Exception e) {
//            }
//            throw new ValidatorException(facesMessage);
//        }
//    }

    /*
        check captcha
     */
//    public boolean checkCaptcha() {
//        if(captcha == null){
//            return false;
//        }
//        return (captcha.equals(captchaBean.getLastCapValue()));
//    }

    /*
    getter and setter
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

    public CaptchaBean getCaptchaBean() {
        return captchaBean;
    }

    public void setCaptchaBean(CaptchaBean captchaBean) {
        this.captchaBean = captchaBean;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

}
