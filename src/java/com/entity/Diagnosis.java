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
 * 就是挂号登记单+诊疗结果
 */
@Entity
@Table(name="Diagnosis")
public class Diagnosis implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(fetch= FetchType.LAZY,cascade={CascadeType.ALL},optional=false)
    @JoinColumn(name="PatientId")
    private Patient patient;
    @OneToOne(fetch= FetchType.LAZY,cascade={CascadeType.ALL},optional=false)
    @JoinColumn(name="DocId")
    private Doctor doctor;
    @Column(name="DiagFee",nullable=false)
    private int diagFee;//挂号费
    @Column(name="PayType",nullable=false)
    private int payType;//支付手段（1-现金，2-支付宝，3-微信，4-银行卡）
    @Column(name="DiagNumber",nullable=false)
    private int diagNumber;//排号（是今天对应医生的第几个病人，查预约人数登记表）
    @OneToOne(fetch= FetchType.LAZY,cascade={CascadeType.ALL},optional=false)
    @JoinColumn(name="OperatorId")
    private Users operator;//挂号创建人，某前台小姐姐
    @Column(name="Valid",nullable=false)
    private char valid;//该挂号是否有效
    @Column(name="Symtom")
    private String symtom;//症状描述
    @Column(name="Diagnosis")
    private String diagnosis;//诊断结果
    @OneToMany(fetch= FetchType.LAZY,cascade={CascadeType.ALL})
    @JoinColumn(name="DiagId")
    private List<DiagnosisDetail> diagnosisDetails;//药单
    @OneToOne(fetch= FetchType.LAZY,cascade={CascadeType.ALL})
    @JoinColumn(name="FeeId")
    private Fee fee;//药费单
    @OneToOne(fetch= FetchType.LAZY,cascade={CascadeType.ALL})
    private Users lastOperator;//最后操作人Id，比如把挂号单取消（当然不退钱），收账。
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdateTime;

    public Diagnosis() {
    }

    public Diagnosis(Patient patient, Doctor doctor, int diagFee, int payType,int diagNumber, Users operator, char valid, String symtom, String diagnosis, List<DiagnosisDetail> diagnosisDetails, Fee fee, Users lastOperator, Date createTime, Date lastUpdateTime) {
        this.patient = patient;
        this.doctor = doctor;
        this.diagFee = diagFee;
        this.payType=payType;
        this.diagNumber = diagNumber;
        this.operator = operator;
        this.valid = valid;
        this.symtom = symtom;
        this.diagnosis = diagnosis;
        this.diagnosisDetails = diagnosisDetails;
        this.fee = fee;
        this.lastOperator = lastOperator;
        this.createTime = createTime;
        this.lastUpdateTime = lastUpdateTime;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public int getDiagFee() {
        return diagFee;
    }

    public void setDiagFee(int diagFee) {
        this.diagFee = diagFee;
    }

    public int getDiagNumber() {
        return diagNumber;
    }

    public void setDiagNumber(int diagNumber) {
        this.diagNumber = diagNumber;
    }

    public Users getOperator() {
        return operator;
    }

    public void setOperator(Users operator) {
        this.operator = operator;
    }

    public char getValid() {
        return valid;
    }

    public void setValid(char valid) {
        this.valid = valid;
    }

    public String getSymtom() {
        return symtom;
    }

    public void setSymtom(String symtom) {
        this.symtom = symtom;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public List<DiagnosisDetail> getDiagnosisDetails() {
        return diagnosisDetails;
    }

    public void setDiagnosisDetails(List<DiagnosisDetail> diagnosisDetails) {
        this.diagnosisDetails = diagnosisDetails;
    }

    public Fee getFee() {
        return fee;
    }

    public void setFee(Fee fee) {
        this.fee = fee;
    }

    public Users getLastOperator() {
        return lastOperator;
    }

    public void setLastOperator(Users lastOperator) {
        this.lastOperator = lastOperator;
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
        if (!(object instanceof Diagnosis)) {
            return false;
        }
        Diagnosis other = (Diagnosis) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.Diagnosis[ id=" + id + " ]";
    }
    
}
