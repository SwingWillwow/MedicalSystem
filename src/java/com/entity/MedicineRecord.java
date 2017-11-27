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
 * 药物使用流水表
 */
@Entity
@Table(name="MedicineRecord")
public class MedicineRecord implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(fetch= FetchType.LAZY,cascade={CascadeType.ALL},optional=false)
    @JoinColumn(name="MedicineId")
    private Medicine medicine;//药物
    @OneToOne(fetch= FetchType.LAZY,cascade={CascadeType.ALL},optional=false)
    @JoinColumn(name="DiagId")
    private Diagnosis diagnosis;//对应诊单
    @Column(name="Dosage",nullable=false)
    private int dosage;//用量
    @OneToOne(fetch= FetchType.LAZY,cascade={CascadeType.ALL},optional=false)
    @JoinColumn(name="MedicinekOperatorId")
    private Users medicineOperator;//外键药房捡药小姐姐,在Fee表中
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;//外键Fee表中药物状态变为“3-已捡药状态”的时间

    public MedicineRecord() {
    }

    public MedicineRecord(Medicine medicine, Diagnosis diagnosis,int dosage, Users medicineOperator, Date createTime) {
        this.medicine = medicine;
        this.diagnosis = diagnosis;
        this.dosage=dosage;
        this.medicineOperator = medicineOperator;
        this.createTime = createTime;
    }

    public int getDosage() {
        return dosage;
    }

    public void setDosage(int dosage) {
        this.dosage = dosage;
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

    public Diagnosis getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(Diagnosis diagnosis) {
        this.diagnosis = diagnosis;
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
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MedicineRecord)) {
            return false;
        }
        MedicineRecord other = (MedicineRecord) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.MedicineRecord[ id=" + id + " ]";
    }
    
}
