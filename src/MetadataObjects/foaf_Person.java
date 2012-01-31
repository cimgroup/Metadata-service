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

    public PersonRole GetHasRole() {
        return m_eHasRole;
    }

    public void SetHasRole(PersonRole eHasRole) {
        m_eHasRole = eHasRole;
    }

    public Competence[] GetHasCompetences() {
        return m_oHasCompetences;
    }

    public void SetHasCompetences(Competence[] oHasCompetences) {
        m_oHasCompetences = oHasCompetences;
    }

    public SourceCode[] GetHasExpertiseInSourceCode() {
        return m_oHasExpertiseInSourceCode;
    }

    public void SetHasExpertiseInSourceCode(SourceCode[] oHasExpertiseInSourceCode) {
        m_oHasExpertiseInSourceCode = oHasExpertiseInSourceCode;
    }

    public Issue[] GetIsRelatedToIssue() {
        return m_oIsRelatedToIssue;
    }

    public void SetIsRelatedToIssue(Issue[] oIsRelatedToIssue) {
        m_oIsRelatedToIssue = oIsRelatedToIssue;
    }
    
    public Comment[] GetIsCreatorOf() {
        return m_oIsCreatorOf;
    }

    public void SetIsCreatorOf(Comment[] oIsCreatorOf) {
        m_oIsCreatorOf = oIsCreatorOf;
    }
    
    public Activity[] GetIsInvolvedPersonOf() {
        return m_oIsInvolvedPersonOf;
    }

    public void SetIsInvolvedPersonOf(Activity[] oIsInvolvedPersonOf) {
        m_oIsInvolvedPersonOf = oIsInvolvedPersonOf;
    }
    
    public Comment[] GetIsCommentorOf() {
        return m_oIsCommentorOf;
    }

    public void SetIsCommentorOf(Comment[] oIsCommentorOf) {
        m_oIsCommentorOf = oIsCommentorOf;
    }

    public Issue[] GetIsReporterOf() {
        return m_oIsReporterOf;
    }

    public void SetIsReporterOf(Issue[] oIsReporterOf) {
        m_oIsReporterOf = oIsReporterOf;
    }
    
    public Issue[] GetIsAssigneeOf() {
        return m_oIsAssigneeOf;
    }

    public void SetIsAssigneeOf(Issue[] oIsAssigneeOf) {
        m_oIsAssigneeOf = oIsAssigneeOf;
    }
    
    public Issue[] GetIsCcPersonOf() {
        return m_oIsCcPersonOf;
    }

    public void SetIsCcPersonOf(Issue[] oIsCcPersonOf) {
        m_oIsCcPersonOf = oIsCcPersonOf;
    }
    
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