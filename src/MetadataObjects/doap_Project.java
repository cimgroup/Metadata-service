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
public class doap_Project extends MetadataObject {
    
    // <editor-fold desc="Members">

    public SourceCode m_oRelatedSourceCode;
    public foaf_Person[] m_oPeopleInvolved;
    public Issue[] m_oHasIssue;

    // </editor-fold>
        
    // <editor-fold desc="Properties">

    public Issue[] GetHasIssue() {
        return m_oHasIssue;
    }

    public void SetHasIssue(Issue[] oHasIssue) {
        m_oHasIssue = oHasIssue;
    }
    
    public foaf_Person[] GetPeopleInvolved() {
        return m_oPeopleInvolved;
    }

    public void SetPeopleInvolved(foaf_Person[] oPeopleInvolved) {
        m_oPeopleInvolved = oPeopleInvolved;
    }

    public SourceCode GetRelatedSourceCode() {
        return m_oRelatedSourceCode;
    }

    public void SetRelatedSourceCode(SourceCode oRelatedSourceCode) {
        m_oRelatedSourceCode = oRelatedSourceCode;
    }

    // </editor-fold>
    
    // <editor-fold desc="Constructor">
    
    /**
     * @summary Constructor
     * @startRealisation Sasa Stojanovic 22.08.2011.
     * @finalModification Sasa Stojanovic 22.08.2011.
     */
    public doap_Project()
    {
        super();
    }
    // </editor-fold>
        
    // <editor-fold desc="Methods">

    // </editor-fold>
    
}