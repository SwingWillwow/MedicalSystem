/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jsfbean.pharmacy;

import com.entity.Diagnosis;
import com.entity.DiagnosisDetail;
import com.entity.Employee;
import com.entity.Fee;
import com.entity.Medicine;
import com.entity.MedicineRecord;
import com.jsfbean.SessionManagedBean;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

/**
 *
 * @author lmh
 */
//药房小姐姐操作bean
public class PharmacyOperateBean {
    @PersistenceContext(unitName = "MedicalSystemPU")
    private EntityManager em;
    @Resource
    private UserTransaction utx;
    String diagnosisId;
    boolean canClick=true;

    public String getDiagnosisId() {
        return diagnosisId;
    }

    public void setDiagnosisId(String diagnosisId) {
        this.diagnosisId = diagnosisId;
    }

    public boolean isCanClick() {
        return canClick;
    }

    public void setCanClick(boolean canClick) {
        this.canClick = canClick;
    }
    
    public PharmacyOperateBean() {
    }
    /*
        patientMedicineList.xhtml中使用
    */
    //返回已结账的诊单
    public List<Diagnosis> userDiagnosis(){
        List<Diagnosis> diagnosises;
        Query query=em.createQuery("SELECT diag FROM Diagnosis diag where diag.fee.status=?1");
        query.setParameter(1,2);
        diagnosises=query.getResultList();
        return diagnosises;
    }
    //跳转详细药单页面
    public String showDiagnosisDetail(){
        ExternalContext externalContext=FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request=(HttpServletRequest)externalContext.getRequest();
        diagnosisId=request.getParameter("diagnosisId");
        externalContext.getFlash().put("diagnosisId", diagnosisId);
        return "/pharmacy/patientMedicineDetail";
    }
    /*
        patientMedicineDetail.xhtml使用
    */
    //返回对应用户的药单详情
    public List<DiagnosisDetail> userDiagnosisDetails(){
        List<DiagnosisDetail> dds=null;
        if(null!=FacesContext.getCurrentInstance().getExternalContext().getFlash().get("diagnosisId")){
            diagnosisId=FacesContext.getCurrentInstance().getExternalContext().getFlash().get("diagnosisId").toString();
            Diagnosis diagnosis=em.find(Diagnosis.class, Long.parseLong(diagnosisId));
            dds=diagnosis.getDiagnosisDetails();
        }
            
        return dds;
    }
    //确认拣药完毕
    public String medicineFinish(){
        SessionManagedBean sessionManagedBean;
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        if (session.getAttribute("sessionManagedBean") == null) {
            sessionManagedBean = new SessionManagedBean();
        } else {
            sessionManagedBean = (SessionManagedBean) session.getAttribute("sessionManagedBean");
        }
        Employee medicineOperator = (Employee) session.getAttribute("userInfo");
        //Employee medicineOperator=em.find(Employee.class, 3L);
        Diagnosis diagnosis=em.find(Diagnosis.class, Long.parseLong(diagnosisId));
        Fee fee=diagnosis.getFee();
        fee.setLastUpdateTime(new Date());
        fee.setMedicineOperator(medicineOperator);
        fee.setStatus(3);//已拣药
        MedicineRecord mr;
        try {
            utx.begin();
            em.merge(fee);
            //添加药物流水
            List<DiagnosisDetail> dds=diagnosis.getDiagnosisDetails();
            for(DiagnosisDetail dd:dds){
                mr=new MedicineRecord(dd.getMedicine(), diagnosis, dd.getCount(), fee.getMedicineOperator(), fee.getLastUpdateTime());
                Medicine medicine=dd.getMedicine();
                medicine.setInventory(medicine.getInventory()-dd.getCount());//药物库存
                medicine.setLastUpdateTime(fee.getLastUpdateTime());
                em.merge(medicine);
                em.persist(mr);
            }
            utx.commit();
        } catch (Exception e) {
            sessionManagedBean.setErrorMessage(e.getMessage());
            return "";
        }
        sessionManagedBean.setSuccessMessage("药已拣好！");
        canClick=false;
        return "patientMedicineList";
    }
    //等待病人领药
    public List<Diagnosis> waitPatientList(){
        List<Diagnosis> diagnosises;
        Query query=em.createQuery("SELECT diag FROM Diagnosis diag where diag.fee.status=?1");
        query.setParameter(1, 3);
        diagnosises=query.getResultList();
        return diagnosises;
    }
    //病人确认领药
    public String commitFinish(){
        SessionManagedBean sessionManagedBean;
        ExternalContext externalContext=FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession)externalContext.getSession(true);
        if (session.getAttribute("sessionManagedBean") == null) {
            sessionManagedBean = new SessionManagedBean();
        } else {
            sessionManagedBean = (SessionManagedBean) session.getAttribute("sessionManagedBean");
        }
        HttpServletRequest request=(HttpServletRequest)externalContext.getRequest();
        String feeId=request.getParameter("feeId");
        Fee fee=em.find(Fee.class, Long.parseLong(feeId));
        fee.setLastUpdateTime(new Date());
        fee.setStatus(4);
        try {
            utx.begin();
            em.merge(fee);
            utx.commit();
        } catch (Exception e) {
            sessionManagedBean.setErrorMessage(e.getMessage());
            return "";
        }
        sessionManagedBean.setSuccessMessage("领药成功！");
        return "";
    }
}
