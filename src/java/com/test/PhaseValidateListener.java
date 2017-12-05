/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test;

import java.util.logging.Logger;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

/**
 *
 * @author qiuyukun
 */
public class PhaseValidateListener implements PhaseListener{
    private static final Logger logger = Logger.getLogger("com.test.MyPhaseListener");
    @Override
    public void afterPhase(PhaseEvent event) {
        logger.info("AFTER"+event.getPhaseId());
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void beforePhase(PhaseEvent event) {
        logger.info("BEFORE"+event.getPhaseId());
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.ANY_PHASE;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
