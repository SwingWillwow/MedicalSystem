/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author qiuyukun
 */
@Entity
public class Patient extends Users implements Serializable {
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private char sex;
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date birthday;
    @Column(nullable = false,unique = true)
    private String idCard;
    @Column(nullable = false)
    private long phone;
    private String emergencyName;
    private long emergencyPhone;
    @Column(nullable = false)
    private String address;
    @Column(nullable = false)
    private String description;
    private int count=0;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    public Patient(){
    
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
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdateTime;
    
    public Patient(String userName,String password,String name,char sex, Date birthday, String idCard, long phone, String emergencyName, long emergencyPhone, String address, String description, int count,Date createTime, Date lastUpdateTime) {
        this.userName = userName;
        this.password = password;
        this.name = name;
        this.sex = sex;
        this.birthday = birthday;
        this.idCard = idCard;
        this.phone = phone;
        this.emergencyName = emergencyName;
        this.emergencyPhone = emergencyPhone;
        this.address = address;
        this.description = description;
        this.count=count;
        this.createTime = createTime;
        this.lastUpdateTime = lastUpdateTime;
    }
    
}
