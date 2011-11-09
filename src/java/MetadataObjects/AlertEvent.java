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
public class AlertEvent extends MetadataObject {
    
    // <editor-fold desc="Members">

    public foaf_Person m_oHasAuthor;
    public String m_sRelatedTo;
    public String m_sIsAbout;

    // </editor-fold>
        
    // <editor-fold desc="Properties">

    public foaf_Person GetHasAuthor() {
        return m_oHasAuthor;
    }

    public void SetHasAuthor(foaf_Person oHasAuthor) {
        m_oHasAuthor = oHasAuthor;
    }

    public String GetIsAbout() {
        return m_sIsAbout;
    }

    public void SetIsAbout(String sIsAbout) {
        m_sIsAbout = sIsAbout;
    }

    public String GetRelatedTo() {
        return m_sRelatedTo;
    }

    public void SetRelatedTo(String sRelatedTo) {
        m_sRelatedTo = sRelatedTo;
    }

    // </editor-fold>
    
    // <editor-fold desc="Constructor">
    
    /**
     * @summary Constructor
     * @startRealisation Sasa Stojanovic 22.08.2011.
     * @finalModification Sasa Stojanovic 22.08.2011.
     */
    public AlertEvent()
    {
        super();
    }
    // </editor-fold>
        
    // <editor-fold desc="Methods">

    // </editor-fold>
    
}
