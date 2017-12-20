/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test;

import java.io.IOException;
import java.io.PrintWriter;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

/**
 *
 * @author qiuyukun
 */
@WebServlet(name = "addFakeDataServlet", urlPatterns = {"/addFakeDataServlet"})
public class addFakeDataServlet extends HttpServlet {

    @PersistenceContext(unitName = "MedicalSystemPU")
    private EntityManager em;
    @Resource
    private UserTransaction utx;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
//        Diagnosis diagnosis = new Diagnosis();
//        diagnosis.setCreateTime(new Date());
//        diagnosis.setDiagFee(null);
//        diagnosis.setDiagnosis(null);
//        diagnosis.setDiagnosisDetails(null);
//        diagnosis.setDoctor(em.find(Doctor.class, 13L));
//        diagnosis.setDiagNumber(1);
//        diagnosis.setDiagFee(em.find(Registration.class, 2L).getDiagFee());
//        diagnosis.setFee(null);
//        diagnosis.setLastOperator(em.find(Employee.class, 6L));
//        diagnosis.setLastUpdateTime(new Date());
//        diagnosis.setOperator(em.find(Employee.class, 6L));
//        diagnosis.setPatient(em.find(Patient.class, 17L));
//        diagnosis.setPayType(1);
//        diagnosis.setValid('Y');
//        try {
//            utx.begin();
//            em.persist(diagnosis);
//            utx.commit();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet addFakeDataServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet addFakeDataServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
