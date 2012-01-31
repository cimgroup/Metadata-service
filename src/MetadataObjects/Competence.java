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
public class Competence extends MetadataObject {
    
    // <editor-fold desc="Members">
    
    public Attribute m_oHasAttribute;
    
    // </editor-fold>
        
    // <editor-fold desc="Properties">

    public Attribute GetHasAttribute() {
        return m_oHasAttribute;
    }

    public void SetHasAttribute(Attribute oHasAttribute) {
        m_oHasAttribute = oHasAttribute;
    }
    // </editor-fold>
    
    // <editor-fold desc="Constructor">
    
    /**
     * @summary Constructor
     * @startRealisation Sasa Stojanovic 23.08.2011.
     * @finalModification Sasa Stojanovic 23.08.2011.
     */
    public Competence()
    {
        super();
    }
    // </editor-fold>
        
    // <editor-fold desc="Methods">

    // </editor-fold>
    
}