/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jsfbean;

import com.entity.Medicine;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.UserTransaction;

/**
 *
 * @author qiuyukun
 */
public class doctorHealBean {

    /**
     * Creates a new instance of doctorHealBean
     */
    
    @PersistenceContext(unitName = "MedicalSystemPU")
    EntityManager em;
    @Resource
    UserTransaction utx;
    
    List<Medicine> allMedicines;
    static int pageSize = 2;//每页的数据数目，根据实际情况设置
    int pageCount;
    int currentPage;
    List<String> pageNumber = new ArrayList<>();
    String searchInfo;
    @PostConstruct
    public void initDocHealBean(){
        Query query = em.createQuery("SELECT m FROM Medicine m");
        allMedicines = query.getResultList();
        pageCount = (allMedicines.size()+pageSize-1)/pageSize;
        currentPage=1;
        allMedicines.clear();
        allMedicines = query.setMaxResults(pageSize).setFirstResult(pageSize*(currentPage-1)).getResultList();
        for(Integer i=1;i<=pageCount;i++){
            
            pageNumber.add(i.toString());
        }
    }
    
    public doctorHealBean() {
        
    }

    
    public void searchMedicine(AjaxBehaviorEvent event){
        Query query = em.createQuery("SELECT m FROM Medicine m WHERE m.name LIKE ?1");
        UIComponent uIComponent = event.getComponent();
        String medicineInfo = uIComponent.getAttributes().get("value").toString();
        medicineInfo = "%"+medicineInfo+"%";
        query.setParameter(1, medicineInfo);
        allMedicines = query.getResultList();
        pageCount = (allMedicines.size()+pageSize-1)/pageSize;
        currentPage=1;
        allMedicines.clear();
        allMedicines = query.setMaxResults(pageSize).setFirstResult(pageSize*(currentPage-1)).getResultList();
        for(Integer i=1;i<=pageCount;i++){
            
            pageNumber.add(i.toString());
        }
        return;
    }
    
    public String changePage(){
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        Integer tmp = Integer.parseInt(request.getParameter("currentPage"));
        currentPage = tmp;
        Query query = em.createQuery("SELECT m FROM Medicine m");
        allMedicines.clear();
        allMedicines = query.setMaxResults(pageSize).setFirstResult(pageSize*(currentPage-1)).getResultList();
        return "";
    }
    
    
    public List<Medicine> getAllMedicines() {
        return allMedicines;
    }

    public void setAllMedicines(List<Medicine> allMedicines) {
        this.allMedicines = allMedicines;
    }

    public static int getPageSize() {
        return pageSize;
    }

    public static void setPageSize(int pageSize) {
        doctorHealBean.pageSize = pageSize;
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
