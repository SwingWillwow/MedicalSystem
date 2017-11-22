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
public class dateBean {

    private final int[] year;
    private final int [] day;
    private final int [] month;

    public int[] getYear() {
        return year;
    }

    public int[] getDay() {
        return day;
    }

    public int[] getMonth() {
        return month;
    }
    /**
     * Creates a new instance of dateBean
     */
    public dateBean() {
        year = new int[118];
        for(int i=1900,j=0;i<=2017;i++,j++){
            year[j]=i;
        }
        month = new int[12];
        for(int i=1,j=0;i<=12;i++,j++){
            month[j]=i;
        }
        day = new int[31];
        for(int i=1,j=0;i<=31;j++,i++){
            day[j]=i;
        }
    }
    
    
}
