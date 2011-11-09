/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MetadataObjects;

/**
 *
 * @author ivano
 */
public class InterestMetric extends QuantitativeMetric {
    
    // <editor-fold desc="Members">
    
    public String m_sIsAbout;
    
    // </editor-fold>
        
    // <editor-fold desc="Properties">

    public String GetIsAbout() {
        return m_sIsAbout;
    }

    public void SetIsAbout(String sIsAbout) {
        m_sIsAbout = sIsAbout;
    }

    // </editor-fold>
    
    // <editor-fold desc="Constructor">
    
    /**
     * @summary Constructor
     * @startRealisation Sasa Stojanovic 23.08.2011.
     * @finalModification Sasa Stojanovic 23.08.2011.
     */
    public InterestMetric()
    {
        super();
    }
    // </editor-fold>
        
    // <editor-fold desc="Methods">

    // </editor-fold>
    
}