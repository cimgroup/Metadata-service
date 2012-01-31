/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MetadataObjects;

/**
 *
 * @author ivano
 */
public class QuantitativeMetric extends Metric {
    
    // <editor-fold desc="Members">
    
    public int m_iHasLevel;
    
    // </editor-fold>
        
    // <editor-fold desc="Properties">

    public int GetHasLevel() {
        return m_iHasLevel;
    }

    public void SetHasLevel(int iHasLevel) {
        m_iHasLevel = iHasLevel;
    }
    
    // </editor-fold>
    
    // <editor-fold desc="Constructor">
    
    /**
     * @summary Constructor
     * @startRealisation Sasa Stojanovic 23.08.2011.
     * @finalModification Sasa Stojanovic 23.08.2011.
     */
    public QuantitativeMetric()
    {
        super();
    }
    // </editor-fold>
        
    // <editor-fold desc="Methods">

    // </editor-fold>
    
}