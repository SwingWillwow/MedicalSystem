/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author lmh
 *
 * TODO
 */
@Entity
@Table(name = "Doctor")
public class Doctor extends Employee implements Serializable {

    private static final long serialVersionUID = 1L;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "SectionsId")
    private Sections sections;
    @Column(name = "Age")
    private int age;//医龄
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "RegistrationId")
    private Registration registration;//医生对应挂号费
    @Column(name = "Description")
    private String description;//医生事迹
    @Column(name = "ByInternet", nullable = false)
    private int byInternet;//医生每天可网上预约人数
    @Column(name = "ByLive", nullable = false)
    private int byLive;//医生每天不预约挂号人数

    public Doctor() {
    }

    public Doctor(String userName, String password, String name, String idCard, Character sex, Date birthday, Department department, Sections sections, int age, Registration registration, String description, int byInternet, int byLive, Date createTime, Date lastUpdateTime) {
        this.age = age;
        this.registration = registration;
        this.description = description;
        this.byInternet = byInternet;
        this.byLive = byLive;
        this.userName = userName;
        this.password = password;
        this.name = name;
        this.idCard = idCard;
        this.sex = sex;
        this.birthday = birthday;
        this.department = department;
        this.sections = sections;
        this.createTime = createTime;
        this.lastUpdateTime = lastUpdateTime;
    }

    public Sections getSections() {
        return sections;
    }

    public void setSections(Sections sections) {
        this.sections = sections;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Registration getRegistration() {
        return registration;
    }

    public void setRegistration(Registration registration) {
        this.registration = registration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getByInternet() {
        return byInternet;
    }

    public void setByInternet(int byInternet) {
        this.byInternet = byInternet;
    }

    public int getByLive() {
        return byLive;
    }

    public void setByLive(int byLive) {
        this.byLive = byLive;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Doctor)) {
            return false;
        }
        Doctor other = (Doctor) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.Doctor[ id=" + id + " ]";
    }

}
