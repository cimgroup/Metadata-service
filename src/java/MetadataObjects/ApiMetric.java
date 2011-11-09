/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MetadataObjects;

/**
 *
 * @author ivano
 */
public class ApiMetric extends QuantitativeMetric {
    
    // <editor-fold desc="Members">
    
    public Method m_oHasMethod;
    
    // </editor-fold>
        
    // <editor-fold desc="Properties">

    public Method GetHasMethod() {
        return m_oHasMethod;
    }

    public void SetHasMethod(Method oHasMethod) {
        m_oHasMethod = oHasMethod;
    }

    // </editor-fold>
    
    // <editor-fold desc="Constructor">
    
    /**
     * @summary Constructor
     * @startRealisation Sasa Stojanovic 23.08.2011.
     * @finalModification Sasa Stojanovic 23.08.2011.
     */
    public ApiMetric()
    {
        super();
    }
    // </editor-fold>
        
    // <editor-fold desc="Methods">

    // </editor-fold>
    
}