/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entity;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author lmh
 * 药品项表
 */
@Entity
@Table(name="DiagnosisDetail")
public class DiagnosisDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="MedicineId")
    private Medicine medicine;//药物
    @Column(name="Count",nullable=false)
    private int count;//计量
    @Column(name="ItemSum",nullable=false,precision=10,scale=2)
    private Double itemSum;//单项药品总计

    public DiagnosisDetail() {
    }

    public DiagnosisDetail(Medicine medicine, int count, Double itemSum) {
        this.medicine = medicine;
        this.count = count;
        this.itemSum = itemSum;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Medicine getMedicine() {
        return medicine;
    }

    public void setMedicine(Medicine medicine) {
        this.medicine = medicine;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Double getItemSum() {
        return itemSum;
    }

    public void setItemSum(Double itemSum) {
        this.itemSum = itemSum;
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
        if (!(object instanceof DiagnosisDetail)) {
            return false;
        }
        DiagnosisDetail other = (DiagnosisDetail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.DiagnosisDetail[ id=" + id + " ]";
    }
    
}
