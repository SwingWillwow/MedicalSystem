/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jsfbean;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import javax.faces.bean.NoneScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import org.captcha.Captcha;

/**
 *
 * @author qiuyukun
 */
@NoneScoped
public class CaptchaBean {

    /**
     * Creates a new instance of Captcha
     */
    private String capPath;
    private String capValue;
    private static String[] savePath = new String[]{
        "resources/test0.png",
        "resources/test1.png",
        "resources/test2.png",
        "resources/test3.png",
        "resources/test4.png",};
    private static int saveId = 0;
    private int id;

    public CaptchaBean() {

    }

    public String getCapPath() throws FileNotFoundException, IOException {
        Map<String, InputStream> captchaMap = Captcha.getCaptcha();
        for (String s : captchaMap.keySet()) {
            capValue = s;
            InputStream is = captchaMap.get(s);
            String realPath;
            ServletContext sc = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
            String filePath = sc.getRealPath("/");

            realPath = filePath + savePath[saveId];
            id = saveId;
            saveId++;
            if (saveId == 5) {
                saveId = 0;
            }
            FileOutputStream os = new FileOutputStream(realPath);
            int i = 0;
            while (i != -1) {
                i = is.read();
                os.write(i);
            }
            is.close();
            os.close();
            capPath = realPath;
        }
        return capPath;
    }

    public void setCapPath(String capPath) {
        this.capPath = "";
    }

    public void setCapValue(String capValue) {
        this.capValue = capValue;
    }

    public int getId() {
        return id;
    }

    public static int getSaveId() {
        return saveId;
    }

    public String getCapValue() {
        return capValue;
    }

}
