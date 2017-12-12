/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jsfbean.doctor;

import com.entity.DiagnosisDetail;
import com.entity.Medicine;
import com.entity.Patient;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

/**
 *
 * @author qiuyukun
 */
public class DoctorHealBean {

    /*
        EntityManager and UserTransaction
    */
    @PersistenceContext(unitName = "MedicalSystemPU")
    EntityManager em;
    @Resource
    UserTransaction utx;
    //  properties
    List<Medicine> allMedicines;//所有符合搜索条件的药品
    static final int PAGESIZE = 2;//每页的数据数目，根据实际情况设置
    int pageCount;
    int currentPage;
    List<String> pageNumber = new ArrayList<>();
    String searchInfo;
    ArrayList<DiagnosisDetail> details = new ArrayList<>();//将要生效的药品
    Patient currentPatient;
    private Long diagId;
    @PostConstruct
    public void initDocHealBean(){
        HttpSession session = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        currentPatient = (Patient)session.getAttribute("currentPatient");
        diagId = Long.parseLong(session.getAttribute("diagId").toString());
        Query query = em.createQuery("SELECT m FROM Medicine m");
        allMedicines = query.getResultList();
        pageCount = (allMedicines.size()+PAGESIZE-1)/PAGESIZE;
        currentPage=1;
        allMedicines.clear();
        allMedicines = query.setMaxResults(PAGESIZE).setFirstResult(PAGESIZE*(currentPage-1)).getResultList();
        pageNumber.clear();
        for(Integer i=1;i<=pageCount;i++){
            
            pageNumber.add(i.toString());
        }
    }
       /**
     * Creates a new instance of DoctorHealBean
     */
    public DoctorHealBean() {
        
    }

    /**
     * This method used to get all the medicine fit the condition
     * 
     */
    public void searchMedicine(){
        Query query = em.createQuery("SELECT m FROM Medicine m WHERE m.name LIKE ?1");
//        UIComponent uIComponent = event.getComponent();
//        String medicineInfo = uIComponent.getAttributes().get("value").toString();
//        searchInfo = medicineInfo;
//        medicineInfo = "%"+medicineInfo+"%";
        String medicineInfo = "%"+searchInfo+"%";
        query.setParameter(1, medicineInfo);
        allMedicines = query.getResultList();
        pageCount = (allMedicines.size()+PAGESIZE-1)/PAGESIZE;
        currentPage=1;
        allMedicines.clear();
        allMedicines = query.setMaxResults(PAGESIZE).setFirstResult(PAGESIZE*(currentPage-1)).getResultList();
        pageNumber.clear();
        for(Integer i=1;i<=pageCount;i++){
            
            pageNumber.add(i.toString());
        }
        return;
    }
    //更换页码
    public String changePage(){
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        Integer tmp = Integer.parseInt(request.getParameter("currentPage"));
        currentPage = tmp;
        Query query;
        if(searchInfo==null){
            query = em.createQuery("SELECT m FROM Medicine m");
        }else{
            query = em.createQuery("SELECT m FROM Medicine m WHERE m.name LIKE ?1");
            query.setParameter(1, "%"+searchInfo+"%");
        }
        allMedicines.clear();
        allMedicines = query.setMaxResults(PAGESIZE).setFirstResult(PAGESIZE*(currentPage-1)).getResultList();
        return "";
    }
    
    /*
        添加药品
    */
    public void addMedicine(ValueChangeEvent event){
        //HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        UIComponent component = event.getComponent();
        String id = component.getAttributes().get("MedicineId").toString();
        Long mediId = Long.parseLong(id);
        String mediNum = event.getNewValue().toString();
        Integer medicineNumber = Integer.parseInt(mediNum);
        Medicine medi = em.find(Medicine.class, mediId);
        DiagnosisDetail detail = null;
        boolean hasDetail = false;
        for (DiagnosisDetail singleDetail : details) {
            if(singleDetail.getMedicine().equals(medi)){
                detail = singleDetail;
                details.remove(singleDetail);
                hasDetail = true;
                break;
            }
        }
        if(hasDetail){
            detail.setCount(detail.getCount()+medicineNumber);
            detail.setItemSum(detail.getItemSum()+medicineNumber*medi.getPrice());
            detail.setMedicine(medi);
        }else{
            detail = new DiagnosisDetail();
            detail.setCount(medicineNumber);
            detail.setItemSum(medicineNumber*medi.getPrice());
            detail.setMedicine(medi);
            Integer tmpId = details.size()+1;
            detail.setId(tmpId.longValue());
        }
        details.add(detail);
    }
    
    public String toConfirmMedicine(){
        //pass parameters
        HttpSession session = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        session.setAttribute("diagnosisDetails", details);
        return "/doctor/confirmMedicine";
    }
    
    
    /*
        setters and getters 
    */
    public List<Medicine> getAllMedicines() {
        return allMedicines;
    }

    public void setAllMedicines(List<Medicine> allMedicines) {
        this.allMedicines = allMedicines;
    }

    public int getPAGESIZE() {
        return PAGESIZE;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public List<String> getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(List<String> pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getSearchInfo() {
        return searchInfo;
    }

    public void setSearchInfo(String searchInfo) {
        this.searchInfo = searchInfo;
    }

    
    
    
    
}
