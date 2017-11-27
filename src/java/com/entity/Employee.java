/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author lmh
 * 员工表：根据部门号分辨是前台还是药房,还是管理员
 */
@Entity
@Table(name="Employee")
@Inheritance(strategy = InheritanceType.JOINED)
public class Employee extends Users implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(name="Name",nullable=false)
    protected String name;
    @Column(name="IdCard",unique=true,nullable=false)
    protected String idCard;//身份证
    @Column(name="Sex",nullable=false)
    protected Character sex;//性别 M/F
    @Column(name="Birthday")
    @Temporal(TemporalType.DATE)
    protected Date birthday;//生日
    @OneToOne(fetch= FetchType.LAZY,cascade={CascadeType.ALL},optional=false)
    @JoinColumn(name="DeptId")
    protected Department department;//部门
    @Temporal(TemporalType.TIMESTAMP)
    protected Date createTime;
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdateTime;
    
    public Employee() {
    }

    public Employee(String userName,String password,String name, Department department, Date createTime, Date lastUpdateTime) {
        this.userName=userName;
        this.password=password;
        this.name = name;
        this.department = department;
        this.createTime = createTime;
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
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

    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Employee)) {
            return false;
        }
        Employee other = (Employee) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.Employee[ id=" + id + " ]";
    }
    
}
