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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name="TransactionRecord")
public class TransactionRecord implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="Type",nullable=false)
    private int type;//支付种类（1-挂号费、2-药费）
    @OneToOne(fetch= FetchType.LAZY,cascade={CascadeType.ALL},optional=false)
    @JoinColumn(name="DiagId")
    private Diagnosis diagnosis;//直接对应诊单，诊单中可查到挂号费，也可查到Fee表中的费用
    @Column(name="Money",nullable=false,precision=10,scale=2)
    private Double money;//金额
    @Column(name="PayType",nullable=false)
    private int payType;//支付手段（1-现金，2-支付宝，3-微信，4-银行卡）
    @OneToOne(fetch= FetchType.LAZY,cascade={CascadeType.ALL},optional=false)
    @JoinColumn(name="PatientId")
    private Patient patient;//病人
    @OneToOne(fetch= FetchType.LAZY,cascade={CascadeType.ALL},optional=false)
    @JoinColumn(name="OperatorId")
    private Employee operator;//账单处理人
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;//外键Diagnosis表的创建时间和Fee表的缴费时间

    public TransactionRecord() {
    }

    public TransactionRecord(int type, Diagnosis diagnosis, Double money, int payType, Patient patient, Employee operator, Date createTime) {
        this.type = type;
        this.diagnosis = diagnosis;
        this.money = money;
        this.payType = payType;
        this.patient = patient;
        this.operator = operator;
        this.createTime = createTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Diagnosis getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(Diagnosis diagnosis) {
        this.diagnosis = diagnosis;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Employee getOperator() {
        return operator;
    }

    public void setOperator(Employee operator) {
        this.operator = operator;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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
        if (!(object instanceof TransactionRecord)) {
            return false;
        }
        TransactionRecord other = (TransactionRecord) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.TransactionRecord[ id=" + id + " ]";
    }
    
}
