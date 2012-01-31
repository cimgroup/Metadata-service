/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MetadataObjects;

import org.apache.derby.client.am.DateTime;

/**
 *
 * @author ivano
 */
public class TemporalMetric extends Metric {
    
    // <editor-fold desc="Members">
    
    public DateTime m_dtmTime;
    
    // </editor-fold>
        
    // <editor-fold desc="Properties">

    public DateTime GetTime() {
        return m_dtmTime;
    }

    public void SetTime(DateTime dtmTime) {
       m_dtmTime = dtmTime;
    }

    // </editor-fold>
    
    // <editor-fold desc="Constructor">
    
    /**
     * @summary Constructor
     * @startRealisation Sasa Stojanovic 23.08.2011.
     * @finalModification Sasa Stojanovic 23.08.2011.
     */
    public TemporalMetric()
    {
        super();
    }
    // </editor-fold>
        
    // <editor-fold desc="Methods">

    // </editor-fold>
    
}