/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jsfbean.clerk;

import com.entity.Diagnosis;
import com.entity.Doctor;
import com.entity.Employee;
import com.entity.Fee;
import com.entity.Patient;
import com.entity.PreRegistration;
import com.entity.PreRegistrationDetail;
import com.entity.Sections;
import com.entity.TransactionRecord;
import com.jsfbean.SessionManagedBean;
import com.util.DateOperator;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

/**
 *
 * @author lmh
 */
public class ClerkOperateBean {

    @PersistenceContext(unitName = "MedicalSystemPU")
    private EntityManager em;
    @Resource
    private UserTransaction utx;
    private String payType = "1";
//    private Long preDiagnosisId;
    private String idCard;
    PreRegistration pr;//用在liveRegistrationInformation中对应的医生挂号信息
    boolean canRegistration;//判断该医生是否已经挂满号
    private List<Sections> section;//用在Ajax的医生部门，生命周期开始就初始化
    private List<Doctor> doctor;
    private String selectedDoctor; //选定的医生ID
    private String selectedSections;//选定的医生部门ID

    /*
        liveRegistration.xhtml所用
     */
    //初始化Sections，跳转liveRegistrationInformation.xhtml时把参数也初始化
    @PostConstruct
    public void initClerkOperateBean() {
        Query query = em.createQuery("SELECT s FROM Sections s");
        section = query.getResultList();
        if (null != FacesContext.getCurrentInstance().getExternalContext().getFlash().get("patientCardId")) {
            idCard = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("patientCardId").toString();
        }
        if (null != FacesContext.getCurrentInstance().getExternalContext().getFlash().get("preRegistration")) {
            pr = (PreRegistration) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("preRegistration");
        }
        if (null != FacesContext.getCurrentInstance().getExternalContext().getFlash().get("canRegistration")) {
            canRegistration = (boolean) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("canRegistration");
        }
    }

    //触发Ajax初始化医生列表
    public void initDoctor(AjaxBehaviorEvent event) {
        if (!(selectedSections == null)) {
            Long id = Long.parseLong(selectedSections);
            if (id == 0) {
                doctor = null;
                return;
            }
            Sections sections = em.find(Sections.class, id);
            doctor = sections.getDoctors();
        }
    }

    //现场选好医生后，跳转医生详细页面准备挂号
    public String toLiveRegistrationInformation() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(true);
        SessionManagedBean sessionManagedBean;
        if (session.getAttribute("sessionManagedBean") == null) {
            sessionManagedBean = new SessionManagedBean();
        } else {
            sessionManagedBean = (SessionManagedBean) session.getAttribute("sessionManagedBean");
        }
        //检查身份证是否存在
        Query query2 = em.createQuery("SELECT p FROM Patient p where p.idCard=?1");
        query2.setParameter(1, idCard);
        List<Patient> temps = query2.getResultList();
        if (temps.isEmpty()) {
            sessionManagedBean.setErrorMessage("该用户未注册！");
            return "";
        }
        //检查是否有选择医生
        Long selectedDoc = 0L, selectedSec = 0L;
        selectedDoc = Long.parseLong(selectedDoctor);
        selectedSec = Long.parseLong(selectedSections);
        if (selectedDoc == 0L || selectedSec == 0L) {
            sessionManagedBean.setErrorMessage("请选择要预约的科室和医生");
            return "";
        }
        //查看preRegistration表中是否是含有未来五天的表项，没有就添加
        Date today = new Date();
        Query query = em.createQuery("SELECT pre FROM PreRegistration pre WHERE pre.doctor.id=?1 ORDER BY pre.preTime DESC");
        query.setParameter(1, selectedDoc);
        List<PreRegistration> pre = query.getResultList();
        Iterator iterator = pre.iterator();
        // 查到有，就把不足未来5天的添加上去
        if (iterator.hasNext()) {
            PreRegistration preRegistration = (PreRegistration) iterator.next();
            Date lastPreTime = preRegistration.getPreTime();
            // 加上今天一共6条记录
            if (DateOperator.isAfterDate(DateOperator.addDay(today, 5), lastPreTime)) {
                for (int i = 0; i <= 5; i++) {
                    if (DateOperator.isBeforeDate(DateOperator.addDay(today, i), lastPreTime)) {
                        continue;
                    }
                    PreRegistration newPreRegistration = new PreRegistration();
                    Doctor doc = em.find(Doctor.class, Long.parseLong(selectedDoctor));
                    newPreRegistration.setByInternet(doc.getByInternet());
                    newPreRegistration.setByInternetReal(0);
                    newPreRegistration.setByLive(doc.getByLive());
                    newPreRegistration.setByLiveReal(0);
                    newPreRegistration.setCount(0);
                    newPreRegistration.setCreateTime(new Date());
                    newPreRegistration.setDoctor(doc);
                    newPreRegistration.setLastUpdateTime(new Date());
                    newPreRegistration.setPreResgistrationDetails(null);
                    newPreRegistration.setPreTime(DateOperator.addDay(today, i));
                    try {
                        utx.begin();
                        em.persist(newPreRegistration);
                        utx.commit();
                    } catch (Exception e) {
                        sessionManagedBean.setErrorMessage(e.getMessage());
                        return "";
                    }
                }
            }
        } else {//如果不存在记录，则直接新增5条记录
            for (int i = 0; i <= 5; i++) {
                PreRegistration preReg2 = new PreRegistration();
                Doctor doc = em.find(Doctor.class, Long.parseLong(selectedDoctor));
                //设置值
                preReg2.setByInternet(doc.getByInternet());
                preReg2.setByInternetReal(0);
                preReg2.setByLive(doc.getByLive());
                preReg2.setByLiveReal(0);
                preReg2.setCount(0);
                preReg2.setCreateTime(new Date());
                preReg2.setDoctor(doc);
                preReg2.setLastUpdateTime(new Date());
                preReg2.setPreResgistrationDetails(null);
                preReg2.setPreTime(DateOperator.addDay(new Date(), i));
                try {
                    utx.begin();
                    em.persist(preReg2);
                    utx.commit();
                } catch (Exception e) {
                    sessionManagedBean.setErrorMessage(e.getMessage());
                    return "";
                }
            }
        }
        //检查此人是否已经挂号
        Query query3 = em.createQuery("SELECT diag FROM Diagnosis diag WHERE diag.patient.idCard=?1 AND diag.doctor.id=?2 AND diag.valid=?3");
        query3.setParameter(1, idCard);
        query3.setParameter(2, selectedDoc);
        query3.setParameter(3, 'Y');
        List<Diagnosis> diagTemps = query3.getResultList();
        if (!diagTemps.isEmpty()) {
            sessionManagedBean.setErrorMessage("该用户已挂号此医生，请不要重复挂号");
            return "";
        }
        //检查该人是否有预约今天的该位医生，如果有，提醒去到预约挂号界面
        Query query1 = em.createQuery("SELECT pr FROM PreRegistration pr WHERE pr.preTime=?1 AND pr.doctor.id=?2");
        query1.setParameter(1, today, TemporalType.DATE);
        query1.setParameter(2, selectedDoc);
        pr = (PreRegistration) query1.getSingleResult();//这里一定能找到
        for (PreRegistrationDetail prd : pr.getPreResgistrationDetails()) {
            if (idCard.equals(prd.getPatient()) && prd.getValid() == 'Y') {
                String tips = "【预约单号:" + prd.getId() + "预约人ID:" + prd.getPatient().getId() + "预约人姓名:" + prd.getPatient().getName() + "】";
                sessionManagedBean.setErrorMessage("该用户已预约该医生，请到预约界面挂号！预约信息:" + tips);
                return "preRegistration";
            }
        }
        //检查该医生是否还能挂号
        if (pr.getByLive() <= pr.getByInternetReal()) {
            canRegistration = false;
        } else {
            canRegistration = true;
        }
        externalContext.getFlash().put("preRegistration", pr);
        externalContext.getFlash().put("canRegistration", canRegistration);
        externalContext.getFlash().put("patientCardId", idCard);
        return "liveRegistrationInformation";
    }

    /*
        liveRegistrationInformation.xhtml所用
        预约人IDcard、对应医生可挂号信息PreRegistration pr、医生是否可预约canRegistration，
        在@PostConstruct中已经初始化
     */
    public String byLiveRegistration() {
        SessionManagedBean sessionManagedBean;
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        if (session.getAttribute("sessionManagedBean") == null) {
            sessionManagedBean = new SessionManagedBean();
        } else {
            sessionManagedBean = (SessionManagedBean) session.getAttribute("sessionManagedBean");
        }
        Date createDate = new Date();
        pr.setByLiveReal(pr.getByLiveReal() + 1);
        pr.setCount(pr.getCount() + 1);
        pr.setLastUpdateTime(createDate);
        Query query = em.createQuery("SELECT p FROM Patient p WHERE p.idCard=?1");
        query.setParameter(1, idCard);
        Patient patient = (Patient) query.getSingleResult();//有过上一步的检查，这里一定查的到
        Doctor doctor = pr.getDoctor();
        Double diagFee = doctor.getRegistration().getDiagFee();
        Employee operator = (Employee) session.getAttribute("userInfo");
//        Employee operator=(Employee)em.find(Employee.class, 2L);
        Diagnosis diagnosis = new Diagnosis(patient, doctor, diagFee, Integer.parseInt(payType), pr.getCount(), operator, 'Y', null, null, null, null, null, createDate, createDate);
        try {
            utx.begin();
            em.persist(diagnosis);
            em.merge(pr);
            utx.commit();
        } catch (Exception e) {
            sessionManagedBean.setErrorMessage(e.getMessage());
            return "";
        }
        sessionManagedBean.setSuccessMessage("挂号成功!");
        return "liveRegistration";
    }

    /**
     * preRegistration.xhtml
     */
    //返回预约了当天的病人列表，包含在List<PreRegistrationDetail>中
    public List<PreRegistrationDetail> preRegistrationDetails() throws ParseException {
        List<PreRegistrationDetail> preRegistrationDetails = new ArrayList<>();
        Query query = em.createQuery("SELECT pr FROM PreRegistration pr where pr.preTime=?1");
        query.setParameter(1, new Date(), TemporalType.DATE);
        List<PreRegistration> pr = query.getResultList();
        for (PreRegistration pr1 : pr) {
            for (PreRegistrationDetail prd : pr1.getPreResgistrationDetails()) {
                if (prd.getValid() == 'Y') {
                    preRegistrationDetails.add(prd);
                }
            }
        }
        return preRegistrationDetails;
    }

    //预约的病人挂号
    public String byInternetRegistration() {
        SessionManagedBean sessionManagedBean;
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(true);
        if (session.getAttribute("sessionManagedBean") == null) {
            sessionManagedBean = new SessionManagedBean();
        } else {
            sessionManagedBean = (SessionManagedBean) session.getAttribute("sessionManagedBean");
        }
        HttpServletRequest httpServletRequest = (HttpServletRequest) externalContext.getRequest();
        String temps = httpServletRequest.getParameter("preRegistrationDetailId");
        Long preRegistrationDetailId = Long.parseLong(temps);
        PreRegistrationDetail prd = em.find(PreRegistrationDetail.class, preRegistrationDetailId);//预约人表
        Date now = new Date();
        prd.setValid('N');//预约人表失效
        prd.setLastUpdateTime(now);//更新预约表最后更新时间
        PreRegistration pr = prd.getPreRegistrationId();//预约表
        pr.setCount(pr.getCount() + 1);//挂号人数+1
        pr.setLastUpdateTime(now);//更新预约表最后更新时间
        Patient patient = prd.getPatient();
        Doctor doctor = pr.getDoctor();
        Double diagFee = doctor.getRegistration().getDiagFee();
        int diagNumber = pr.getCount();
        Employee operator = (Employee) session.getAttribute("userInfo");
        //Employee operator=em.find(Employee.class, 2L);//测试
        Diagnosis diagnosis = new Diagnosis(patient, doctor, diagFee, Integer.parseInt(payType), diagNumber, operator, 'Y', null, null, null, null, null, now, now);
        TransactionRecord tr = new TransactionRecord(1, diagnosis, diagnosis.getDiagFee(), diagnosis.getPayType(), diagnosis.getPatient(), diagnosis.getOperator(), diagnosis.getCreateTime());
        try {
            utx.begin();
            em.merge(prd);
            em.merge(pr);
            em.persist(diagnosis);
            em.persist(tr);
            utx.commit();
        } catch (Exception e) {
            sessionManagedBean.setErrorMessage(e.getMessage());
            return "";
        }
        String successMessageString = "成功挂号！";
        sessionManagedBean.setSuccessMessage(successMessageString);
        return "";
    }

    /*
        payPage.xhtml缴费页面
     */
    //返回待缴费的清单
    public List<Diagnosis> diagWithFee() {
        List<Diagnosis> diagnosises;
        Query query = em.createQuery("SELECT diag FROM Diagnosis diag WHERE diag.fee.status=?1");
        query.setParameter(1, 1);
        diagnosises = query.getResultList();
        return diagnosises;
    }

    //缴费
    public String payFee() {
        SessionManagedBean sessionManagedBean;
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(true);
        if (session.getAttribute("sessionManagedBean") == null) {
            sessionManagedBean = new SessionManagedBean();
        } else {
            sessionManagedBean = (SessionManagedBean) session.getAttribute("sessionManagedBean");
        }
        HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
        Employee operator = (Employee) session.getAttribute("userInfo");//前台小姐姐
//        Employee operator = em.find(Employee.class, 2L);
        String diagnosisId = request.getParameter("diagnosisId");//因为要写资金流水，所以要返回这个，而不是feeID
        Diagnosis diagnosis = em.find(Diagnosis.class, Long.parseLong(diagnosisId));
        Fee fee = diagnosis.getFee();
        fee.setFeeOperator(operator);
        fee.setLastUpdateTime(new Date());
        fee.setPayType(Integer.parseInt(payType));
        fee.setStatus(2);//状态为已结账
        TransactionRecord tr = new TransactionRecord(2, diagnosis, fee.getTotalSum(), fee.getPayType(), diagnosis.getPatient(), fee.getFeeOperator(), fee.getLastUpdateTime());
        try {
            utx.begin();
            em.merge(fee);
            em.persist(tr);
            utx.commit();
        } catch (Exception e) {
            sessionManagedBean.setErrorMessage(e.getMessage());
            return "";
        }
        sessionManagedBean.setSuccessMessage("缴费成功！请凭账单号" + fee.getId() + "取药");
        return "";
    }

    /*
        registrationList.xhtml 今日挂号的所有人
     */
    public List<Diagnosis> diagnosisList() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date beginDate = calendar.getTime();
        Date endDate = DateOperator.addDay(beginDate, 1);
        Query query = em.createQuery("SELECT diagnosis FROM Diagnosis diagnosis WHERE diagnosis.createTime BETWEEN ?1 AND ?2");
        query.setParameter(1, beginDate, TemporalType.TIMESTAMP);
        query.setParameter(2, endDate, TemporalType.TIMESTAMP);
        List<Diagnosis> diagnosisList = query.getResultList();
        return diagnosisList;
    }

    /*
        getter和setter方法，构造方法
     */
    public ClerkOperateBean() {
    }

    public List<Sections> getSection() {
        return section;
    }

    public void setSection(List<Sections> section) {
        this.section = section;
    }

    public List<Doctor> getDoctor() {
        return doctor;
    }

    public void setDoctor(List<Doctor> doctor) {
        this.doctor = doctor;
    }

    public String getSelectedDoctor() {
        return selectedDoctor;
    }

    public void setSelectedDoctor(String selectedDoctor) {
        this.selectedDoctor = selectedDoctor;
    }

    public String getSelectedSections() {
        return selectedSections;
    }

    public void setSelectedSections(String selectedSections) {
        this.selectedSections = selectedSections;
    }

    public boolean isCanRegistration() {
        return canRegistration;
    }

    public void setCanRegistration(boolean canRegistration) {
        this.canRegistration = canRegistration;
    }

    public PreRegistration getPr() {
        return pr;
    }

    public void setPr(PreRegistration pr) {
        this.pr = pr;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }
}
