/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MetadataObjects;

import MetadataCore.MetadataObject;

/**
 *
 * @author ivano
 */
public class Metric extends MetadataObject {
    
    // <editor-fold desc="Members">
    
    public String m_sMetricName;
    public String m_sMetricValue;
    public Issue m_oInIssue;
    
    // </editor-fold>
        
    // <editor-fold desc="Properties">

    public Issue GetInIssue() {
        return m_oInIssue;
    }

    public void SetInIssue(Issue oInIssue) {
        m_oInIssue = oInIssue;
    }

    public String GetMetricName() {
        return m_sMetricName;
    }

    public void SetMetricName(String sMetricName) {
        m_sMetricName = sMetricName;
    }
    
    public String GetMetricValue() {
        return m_sMetricValue;
    }

    public void SetMetricValue(String sMetricValue) {
        m_sMetricValue = sMetricValue;
    }
    // </editor-fold>
    
    // <editor-fold desc="Constructor">
    
    /**
     * @summary Constructor
     * @startRealisation Sasa Stojanovic 23.08.2011.
     * @finalModification Sasa Stojanovic 23.08.2011.
     */
    public Metric()
    {
        super();
    }
    // </editor-fold>
        
    // <editor-fold desc="Methods">

    // </editor-fold>
    
}