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
 * @author lmh
 * 药费表
 */
@Entity
@Table(name="Fee")
public class Fee implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="TotalSum",nullable=false,precision=10,scale=2)
    private Double totalSum;//诊单药品总价
    @Column(name="Status",nullable=false)
    private int status;//账单状态（1-待交、2-已交、3-未领药、4-已领药）
    @Column(name="PayType")
    private int payType;//支付手段（1-现金，2-支付宝，3-微信，4-银行卡）
    @OneToOne(fetch= FetchType.LAZY,cascade={CascadeType.ALL})
    @JoinColumn(name="FeeOperatorId")
    private Users feeOperator;//外键收钱人ID
    @OneToOne(fetch= FetchType.LAZY,cascade={CascadeType.ALL})
    @JoinColumn(name="MedicineOperatorId")
    private Users medicineOperator;//外键捡药人ID
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdateTime;

    public Fee() {
    }

    public Fee(Double totalSum, int status,int payType, Users feeOperator, Users medicineOperator, Date createTime, Date lastUpdateTime) {
        this.totalSum = totalSum;
        this.status = status;
        this.payType=payType;
        this.feeOperator = feeOperator;
        this.medicineOperator = medicineOperator;
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

    public Double getTotalSum() {
        return totalSum;
    }

    public void setTotalSum(Double totalSum) {
        this.totalSum = totalSum;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Users getFeeOperator() {
        return feeOperator;
    }

    public void setFeeOperator(Users feeOperator) {
        this.feeOperator = feeOperator;
    }

    public Users getMedicineOperator() {
        return medicineOperator;
    }

    public void setMedicineOperator(Users medicineOperator) {
        this.medicineOperator = medicineOperator;
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
        if (!(object instanceof Fee)) {
            return false;
        }
        Fee other = (Fee) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.Fee[ id=" + id + " ]";
    }
    
}
