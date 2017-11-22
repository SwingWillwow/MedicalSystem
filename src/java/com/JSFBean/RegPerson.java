/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.JSFBean;

import com.Entity.Person;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

/**
 *
 * @author qiuyukun
 */
public class RegPerson {

    /**
     * Creates a new instance of RegPerson
     */
    @PersistenceContext(unitName = "MedicalSystemPU")
    private EntityManager em;
    @Resource
    private UserTransaction utx;
    private Person person;
    public RegPerson() {
        
    }
    private String name;
    private String street;
    private String city;
    private String states;
    private String zip;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStates() {
        return states;
    }

    public void setStates(String states) {
        this.states = states;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }
    public String addPerson(){
        person = new Person(name,street,city,states,zip);
        try{
            em.getTransaction();
            utx.begin();
            em.persist(person);
            utx.commit();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }finally{
            if(em!=null){
                //em.close();close会报错
            }
        }
        return "addPerson";
        
    }
}
