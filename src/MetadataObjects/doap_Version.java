/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MetadataObjects;

import MetadataCore.MetadataObject;

/**
 *
 * @author Sasa.Stojanovic
 */
public class doap_Version extends MetadataObject {
    
    // <editor-fold desc="Members">
    
    public Issue m_bIsFixedBy;
    public String m_sDescription;
    
    // </editor-fold>
        
    // <editor-fold desc="Properties">

    public Issue GetIsFixedBy() {
        return m_bIsFixedBy;
    }

    public void SetIsFixedBy(Issue bIsFixedBy) {
        m_bIsFixedBy = bIsFixedBy;
    }

    public String GetVersion() {
        return m_sDescription;
    }

    public void SetVersion(String sDescription) {
        m_sDescription = sDescription;
    }
    
    // </editor-fold>
    
    // <editor-fold desc="Constructor">
    
    /**
     * @summary Constructor
     * @startRealisation Sasa Stojanovic 22.08.2011.
     * @finalModification Sasa Stojanovic 22.08.2011.
     */
    public doap_Version()
    {
        super();
    }
    // </editor-fold>
        
    // <editor-fold desc="Methods">

    // </editor-fold>
    
}