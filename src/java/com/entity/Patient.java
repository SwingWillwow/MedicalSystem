/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entity;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author qiuyukun
 */
@Entity
@Table(name="Patient")
public class Patient extends Users implements Serializable {
    @Column(name="PatientName",nullable=false)
    private String name;
    @Column(name="Sex",nullable=false)
    private char sex;
    @Column(name="Birthday",nullable=false)//计算年龄，不能空
    @Temporal(TemporalType.DATE)
    private Date birthday;
    @Column(name="IdCard",unique=true,nullable=false)
    private String idCard;
    @Column(name="Phone",nullable=false)
    private long phone;
    @Column(name="EmergencyName")
    private String emergencyName;
    @Column(name="EmergencyPhone")
    private long emergencyPhone;
    @Column(name="Address",nullable=false)
    private String address;
    @Column(name="Description")
    private String description;//病人过往病史
    @Column(name="Count")
    private int count=0;//看病次数，看情况给个vip好了
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdateTime;

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

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }
    
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
