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
    
    public Attribute[] m_oHasAttribute;
    public String m_sLevel;
    public Identity m_oIdentity;
    
    // </editor-fold>
        
    // <editor-fold desc="Properties">

    public Attribute[] GetHasAttributes() {
        return m_oHasAttribute;
    }

    public void SetHasAttributes(Attribute[] oHasAttribute) {
        m_oHasAttribute = oHasAttribute;
    }
    
    public String GetLevel() {
        return m_sLevel;
    }

    public void SetLevel(String sLevel) {
        m_sLevel = sLevel;
    }
    
    public Identity GetIdentity() {
        return m_oIdentity;
    }

    public void SetIdentity(Identity oIdentity) {
        m_oIdentity = oIdentity;
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