/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MetadataObjects;

import MetadataCore.MetadataConstants.PersonRole;

/**
 *
 * @author ivano
 */
public class foaf_Person extends foaf_Agent {
    
    // <editor-fold desc="Members">
    
    public String m_sFirstName;
    public String m_sLastName;
    public String m_sGender;
    public String m_sEmail;
    public String m_sUsername;
    public String m_sWikiEditCount;
    public SourceCode[] m_oHasExpertiseInSourceCode;
    public PersonRole m_eHasRole;
    public Issue[] m_oIsRelatedToIssue;
    public Competence[] m_oHasCompetences;
    public Comment[] m_oIsCreatorOf;
    public Activity[] m_oIsInvolvedPersonOf;
    public Comment[] m_oIsCommentorOf;
    public Issue[] m_oIsReporterOf;
    public Issue[] m_oIsAssigneeOf;
    public Issue[] m_oIsCcPersonOf;
    
    // </editor-fold>
        
    // <editor-fold desc="Properties">

    // </editor-fold>
    
    // <editor-fold desc="Constructor">
    
    /**
     * @summary Constructor
     * @startRealisation Sasa Stojanovic 23.08.2011.
     * @finalModification Sasa Stojanovic 23.08.2011.
     */
    public foaf_Person()
    {
        super();
    }
    // </editor-fold>
        
    // <editor-fold desc="Methods">

    // </editor-fold>
    
}