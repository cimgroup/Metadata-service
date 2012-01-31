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
public class Component extends MetadataObject {
    
     // <editor-fold desc="Members">
    
    public String m_sVersion;
    public Product m_oIsComponentOf;
    public Issue m_oIsIssueOf;

     // </editor-fold>
    
    // <editor-fold desc="Properties">
    
    public Product GetIsComponentOf() {
        return m_oIsComponentOf;
    }

    public void SetIsComponentOf(Product oIsComponentOf) {
        m_oIsComponentOf = oIsComponentOf;
    }

    public String GetVersion() {
        return m_sVersion;
    }

    public void SetVersion(String sVersion) {
        m_sVersion = sVersion;
    }
    
    public Issue GetIsIssueOf() {
        return m_oIsIssueOf;
    }

    public void SetIsIssueOf(Issue oIsIssueOf) {
        m_oIsIssueOf = oIsIssueOf;
    }
    
    // </editor-fold>
        
    // <editor-fold desc="Methods">
    
    /**
     * @summary Constructor
     * @startRealisation Ivan Obradovic 22.08.2011.
     * @finalModification Ivan Obradovic 22.08.2011.
     */
    public Component()
    {
        super();
    }
    
     // </editor-fold>
}
