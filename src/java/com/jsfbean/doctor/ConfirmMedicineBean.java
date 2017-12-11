/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jsfbean.doctor;

import com.entity.Diagnosis;
import com.entity.DiagnosisDetail;
import com.entity.Doctor;
import com.entity.Employee;
import com.entity.Fee;
import com.entity.Medicine;
import com.entity.Patient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.event.ValueChangeEvent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

/**
 *
 * @author qiuyukun
 */
public class ConfirmMedicineBean implements Serializable{

    
    /*
        utx and em
     */
    @PersistenceContext(unitName = "MedicalSystemPU")
    private EntityManager em;
    @Resource
    private UserTransaction utx;
    //properties
    private Patient currentPatient;
    private ArrayList<DiagnosisDetail> details = new ArrayList<>();
    private Long diagId;

    /**
     * Creates a new instance of ConfirmMedicineBean
     */
    public ConfirmMedicineBean() {

    }

    
    /**
     *  init function
     */
    @PostConstruct
    public void initBean() {
        // init properties from session
        HttpSession session = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        currentPatient = (Patient) session.getAttribute("currentPatient");
        details = (ArrayList<DiagnosisDetail>) session.getAttribute("diagnosisDetails");
        diagId = Long.parseLong(session.getAttribute("diagId").toString());
    }

    /**
     * redirect to showPatientList
     *
     * @return url to that page
     */
    public String toNextPatient() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession)facesContext.getExternalContext().getSession(true);
        try {
            utx.begin();
            Diagnosis diag = em.find(Diagnosis.class, diagId);//找到对应的诊疗记录
            Doctor doctor = (Doctor)session.getAttribute("userInfo");//当前负责治疗的医生
            //把所有的药品费用细节加入到diagnosisDetail表中
            for(DiagnosisDetail detail:details){
                em.persist(detail);
            }
            diag.setDiagnosisDetails(details);
            //插入一条费用记录
            Fee fee = new Fee();
            fee.setCreateTime(new Date());
            fee.setFeeOperator(em.find(Employee.class, doctor.getId()));
            fee.setLastUpdateTime(new Date());
            fee.setStatus(1);//状态设置为未交钱
            Double totalSum = 0.0;
            for(DiagnosisDetail detail :details){
                totalSum+=detail.getItemSum();
            }
            fee.setTotalSum(totalSum);
            em.persist(fee);
            diag.setFee(fee);
            diag.setValid('N');
            utx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //操作全部解释，将所设置的session删除，以免在接下来的使用中产生错误
        session.setAttribute("currentPatient", null);
        session.setAttribute("diagId", null);
        session.setAttribute("diagnosisDetails", null);
        return "showPatientList";
    }
    /**
     * change medicine number. if number equal to 0. delete the item.
     * @param event 
     */
    public void changeMedicine(ValueChangeEvent event){
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        UIComponent component = event.getComponent();
        Long mediId = Long.parseLong(component.getAttributes().get("MedicineId").toString());
        Integer mediNum = Integer.parseInt(event.getNewValue().toString());
//        boolean hasMedicine = false;
        for(DiagnosisDetail detail: details){
            if(detail.getMedicine().getId().equals(mediId)){
                detail.setCount(mediNum);
                detail.setItemSum(mediNum*detail.getMedicine().getPrice());
//                hasMedicine = true;
                break;
            }
        }
//        if(!hasMedicine){
//            Medicine medi = em.find(Medicine.class, mediId);
//            DiagnosisDetail newDetail = new DiagnosisDetail();
//            newDetail.setCount(mediNum);
//            newDetail.setItemSum(mediNum*medi.getPrice());
//            newDetail.setMedicine(medi);
//            Integer tmp = details.size()+1;
//            Long fakeId = tmp.longValue();
//            newDetail.setId(fakeId);
//            details.add(newDetail);
//        }
    }
    /*
        setters and getters
     */
    public Patient getCurrentPatient() {
        return currentPatient;
    }

    public void setCurrentPatient(Patient currentPatient) {
        this.currentPatient = currentPatient;
    }

    public ArrayList<DiagnosisDetail> getDetails() {
        return details;
    }

    public void setDetails(ArrayList<DiagnosisDetail> details) {
        this.details = details;
    }

}
