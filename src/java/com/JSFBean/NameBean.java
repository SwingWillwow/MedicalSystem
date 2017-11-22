/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.JSFBean;


/**
 *
 * @author qiuyukun
 */
public class NameBean {

    private String last;

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }
    private String first;
    /**
     * Creates a new instance of NameBean
     */
    public NameBean() {
    
    }
    public NameBean(String first,String last){
        this.first=first;
        this.last=last;
    }
    
}
