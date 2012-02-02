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
public class Attribute extends MetadataObject {
    
    // <editor-fold desc="Members">
    
    public Metric[] m_oHasMetric;
    
    // </editor-fold>
        
    // <editor-fold desc="Properties">

    public Metric[] GetHasMetric() {
        return m_oHasMetric;
    }

    public void SetHasMetric(Metric[] oHasMetric) {
        m_oHasMetric = oHasMetric;
    }

    // </editor-fold>
    
    // <editor-fold desc="Constructor">
    
    /**
     * @summary Constructor
     * @startRealisation Sasa Stojanovic 23.08.2011.
     * @finalModification Sasa Stojanovic 23.08.2011.
     */
    public Attribute()
    {
        super();
    }
    // </editor-fold>
        
    // <editor-fold desc="Methods">

    // </editor-fold>
    
}