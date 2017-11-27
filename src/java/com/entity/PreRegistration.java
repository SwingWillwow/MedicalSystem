/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author lmh
 * 预约人数登记表，记录每天每位医生的可预约、可挂号人数与实际挂号人数
 */
@Entity
@Table(name="PreRegistration")
public class PreRegistration implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="PreTime",nullable=false)
    @Temporal(TemporalType.DATE)
    private Date preTime;//预约的日期
    @OneToOne(fetch= FetchType.LAZY,cascade={CascadeType.ALL},optional=false)
    @JoinColumn(name="DocId")
    private Doctor doctor;//所预约医生
    @Column(name="ByInternet",nullable=false)
    private int byInternet;//可预约人数
    @Column(name="ByInternetReal")
    private int byInternetReal;//已预约人数
    @OneToMany(fetch= FetchType.LAZY,cascade={CascadeType.ALL})
    @JoinColumn(name="PreRegistrationId")
    private List<PreRegistrationDetail> preResgistrationDetails;//预约的详细人
    @Column(name="ByLive",nullable=false)
    private int byLive;//未预约可挂号人数
    @Column(name="ByLiveReal")
    private int byLiveReal;//未预约前台已挂号人数
    @Column(name="Count",nullable=false)
    private int count;//该天该医生已挂号人数，初始为0，同时为病人提供排号依据
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdateTime;

    public PreRegistration() {
    }

    public PreRegistration(Date preTime, Doctor doctor, int byInternet, int byInternetReal, List<PreRegistrationDetail> preResgistrationDetails, int byLive, int byLiveReal, int count, Date createTime, Date lastUpdateTime) {
        this.preTime = preTime;
        this.doctor = doctor;
        this.byInternet = byInternet;
        this.byInternetReal = byInternetReal;
        this.preResgistrationDetails = preResgistrationDetails;
        this.byLive = byLive;
        this.byLiveReal = byLiveReal;
        this.count = count;
        this.createTime = createTime;
        this.lastUpdateTime = lastUpdateTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getPreTime() {
        return preTime;
    }

    public void setPreTime(Date preTime) {
        this.preTime = preTime;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public int getByInternet() {
        return byInternet;
    }

    public void setByInternet(int byInternet) {
        this.byInternet = byInternet;
    }

    public int getByInternetReal() {
        return byInternetReal;
    }

    public void setByInternetReal(int byInternetReal) {
        this.byInternetReal = byInternetReal;
    }

    public List<PreRegistrationDetail> getPreResgistrationDetails() {
        return preResgistrationDetails;
    }

    public void setPreResgistrationDetails(List<PreRegistrationDetail> preResgistrationDetails) {
        this.preResgistrationDetails = preResgistrationDetails;
    }

    public int getByLive() {
        return byLive;
    }

    public void setByLive(int byLive) {
        this.byLive = byLive;
    }

    public int getByLiveReal() {
        return byLiveReal;
    }

    public void setByLiveReal(int byLiveReal) {
        this.byLiveReal = byLiveReal;
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


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PreRegistration)) {
            return false;
        }
        PreRegistration other = (PreRegistration) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.PreRegistration[ id=" + id + " ]";
    }
    
}
