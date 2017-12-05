/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jsfbean;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.ViewScoped;

/**
 *
 * @author qiuyukun
 */
@ManagedBean
@ViewScoped
public class guessNumber implements Serializable{

    /**
     * Creates a new instance of guessNumber
     */
    
    private String result;
    private String named;
    public guessNumber() {
        
    }
    
    public void namedChanged(AjaxBehaviorEvent event) {
    result = "Hello, you entered " + named;
}

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getNamed() {
        return named;
    }

    public void setNamed(String named) {
        this.named = named;
    }
    
}
