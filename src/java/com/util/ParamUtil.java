/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.util;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author qiuyukun
 */
public class ParamUtil {
    public static String getParamByName(FacesContext facesContext,String paramName){
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        String param = request.getParameter(paramName);
        return param;
    }
    public static String getFlashParamByName(FacesContext facesContext,String paramName){
        Flash flash = facesContext.getExternalContext().getFlash();
        return flash.get(paramName).toString();
    }
    public static String getComponentAttrByName(UIComponent component,String paramName){
        return component.getAttributes().get(paramName).toString();
    }
}
