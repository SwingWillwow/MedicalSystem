/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jsfbean.admin;

import com.entity.MedicineRecord;
import com.entity.TransactionRecord;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.UserTransaction;

/**
 *
 * @author qiuyukun
 */
public class ShowMedicineRecordBean {

    /**
     * Creates a new instance of ShowTransactionBean
     */
     @PersistenceContext(unitName = "MedicalSystemPU")
    private EntityManager em;
    @Resource
    private UserTransaction utx;
    private List<MedicineRecord> medicineRecords;
    static final int PAGESIZE = 10;//每页的数据数目，根据实际情况设置
    int pageCount;
    int currentPage;
    List<String> pageNumber = new ArrayList<>();
    public ShowMedicineRecordBean() {
        
    }
    @PostConstruct
    private void init(){
        Query query = em.createQuery("SELECT medicineRecords FROM MedicineRecord medicineRecords");
        medicineRecords = query.getResultList();
        pageCount = (medicineRecords.size()+PAGESIZE-1)/PAGESIZE;
        currentPage=1;
        medicineRecords.clear();
        medicineRecords = query.setMaxResults(PAGESIZE).setFirstResult(PAGESIZE*(currentPage-1)).getResultList();
        pageNumber.clear();
        for(Integer i=1;i<=pageCount;i++){
            pageNumber.add(i.toString());
        }
        
    }

    public String changePage(){
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        currentPage = Integer.parseInt(request.getParameter("currentPage"));
        Query query = em.createQuery("SELECT medicineRecords FROM MedicineRecord medicineRecords");
        medicineRecords.clear();
        medicineRecords = query.setMaxResults(PAGESIZE).setFirstResult(PAGESIZE*(currentPage-1)).getResultList();
        return "";
    }

    public List<MedicineRecord> getMedicineRecords() {
        return medicineRecords;
    }

    public void setMedicineRecords(List<MedicineRecord> medicineRecords) {
        this.medicineRecords = medicineRecords;
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

    
}
