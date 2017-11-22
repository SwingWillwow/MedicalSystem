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
public class TableTest {

    /**
     * Creates a new instance of TableTest
     */
    private static final NameBean[] nameBeans= new NameBean[]{
        new NameBean("Yukun","Qiu"),
        new NameBean("MingHong","Liang"),
        new NameBean("Mingyan","Liu"),
        new NameBean("Weihang","Liu")
    };
    public TableTest() {
        
    }
    public NameBean[] getNameBeans(){
        return nameBeans;
    }
}
