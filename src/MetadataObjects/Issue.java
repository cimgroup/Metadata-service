/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MetadataObjects;

//import MetadataCore.MetadataObject;
import java.util.Date;
import org.apache.derby.client.am.DateTime;

/**
 *
 * @author ivano
 */
public class Issue extends Task {
    
     // <editor-fold desc="Members">
    
    public String m_sBugURL;
    public Date m_dtmDateOpened;
    public String m_sDescription;
    public String m_sKeyword;
    public String m_sIssueURL;
    public Date m_dtmLastModified;
    public Integer m_iNumber;
    public Issue[] m_oBlocks;
    public Issue[] m_oDependsOn;
    public doap_Version m_oFixes;
    public Activity[] m_oHasActivity;
    public Attachment[] m_oHasAttachment;
    public foaf_Person[] m_oHasCCPerson;
    public Comment[] m_oHasComment;
    public ComputerSystem m_oHasComputerSystem;
    public Milestone m_oHasMilestone;
    public Priority m_oHasPriority;
    public State m_oHasState;
    public Issue m_oHasSubIssue;
    public Issue m_oIsSubIssueOf;
    public doap_Project m_oInProject;
    public foaf_Person m_oHasReporter;
    public Resolution m_oHasResolution;
    public Severity m_oHasSeverity;
    public foaf_Person m_oHasAssignee;
    public Issue m_oIsDuplicateOf;
    public Issue m_oIsMergedInto;
    public Component m_oIsIssueOf;

     // </editor-fold>

    // <editor-fold desc="Properties">

    public Date GetDateOpened() {
        return m_dtmDateOpened;
    }

    public void SetDateOpened(Date dtmDateOpened) {
        m_dtmDateOpened = dtmDateOpened;
    }

    public Date GetLastModified() {
        return m_dtmLastModified;
    }

    public void SetLastModified(Date dtmLastModified) {
        m_dtmLastModified = dtmLastModified;
    }

    public Integer GetNumber() {
        return m_iNumber;
    }

    public void SetNumber(Integer iNumber) {
        m_iNumber = iNumber;
    }

    public Issue[] GetBlocks() {
        return m_oBlocks;
    }

    public void SetBlocks(Issue[] oBlocks) {
        m_oBlocks = oBlocks;
    }

    public Issue[] GetDependsOn() {
        return m_oDependsOn;
    }

    public void SetDependsOn(Issue[] oDependsOn) {
        m_oDependsOn = oDependsOn;
    }

    public doap_Version GetFixes() {
        return m_oFixes;
    }

    public void SetFixes(doap_Version oFixes) {
        m_oFixes = oFixes;
    }

    public Activity[] GetHasActivity() {
        return m_oHasActivity;
    }

    public void SetHasActivity(Activity[] oHasActivity) {
        m_oHasActivity = oHasActivity;
    }

    public foaf_Person GetHasAssignee() {
        return m_oHasAssignee;
    }

    public void SetHasAssignee(foaf_Person oHasAssignee) {
        m_oHasAssignee = oHasAssignee;
    }

    public Attachment[] GetHasAttachment() {
        return m_oHasAttachment;
    }

    public void SetHasAttachment(Attachment[] oHasAttachment) {
        m_oHasAttachment = oHasAttachment;
    }

    public foaf_Person[] GetHasCCPerson() {
        return m_oHasCCPerson;
    }

    public void SetHasCCPerson(foaf_Person[] oHasCCPerson) {
        m_oHasCCPerson = oHasCCPerson;
    }

    public Comment[] GetHasComment() {
        return m_oHasComment;
    }

    public void SetHasComment(Comment[] oHasComment) {
        m_oHasComment = oHasComment;
    }

    public ComputerSystem GetHasComputerSystem() {
        return m_oHasComputerSystem;
    }

    public void SetHasComputerSystem(ComputerSystem oHasComputerSystem) {
        m_oHasComputerSystem = oHasComputerSystem;
    }

    public Milestone GetHasMilestone() {
        return m_oHasMilestone;
    }

    public void SetHasMilestone(Milestone oHasMilestone) {
        m_oHasMilestone = oHasMilestone;
    }

    public Priority GetHasPriority() {
        return m_oHasPriority;
    }

    public void SetHasPriority(Priority oHasPriority) {
        m_oHasPriority = oHasPriority;
    }

    public foaf_Person GetHasReporter() {
        return m_oHasReporter;
    }

    public void SetHasReporter(foaf_Person oHasReporter) {
        m_oHasReporter = oHasReporter;
    }

    public Resolution GetHasResolution() {
        return m_oHasResolution;
    }

    public void SetHasResolution(Resolution oHasResolution) {
        m_oHasResolution = oHasResolution;
    }

    public Severity GetHasSeverity() {
        return m_oHasSeverity;
    }

    public void SetHasSeverity(Severity oHasSeverity) {
        m_oHasSeverity = oHasSeverity;
    }

    public State GetHasState() {
        return m_oHasState;
    }

    public void SetHasState(State oHasState) {
        m_oHasState = oHasState;
    }

    public Issue GetHasSubIssue() {
        return m_oHasSubIssue;
    }

    public void SetHasSubIssue(Issue oHasSubIssue) {
        m_oHasSubIssue = oHasSubIssue;
    }

    public doap_Project GetInProject() {
        return m_oInProject;
    }

    public void SetInProject(doap_Project oInProject) {
        m_oInProject = oInProject;
    }

    public Issue GetIsDuplicateOf() {
        return m_oIsDuplicateOf;
    }

    public void SetIsDuplicateOf(Issue oIsDuplicateOf) {
        m_oIsDuplicateOf = oIsDuplicateOf;
    }

    public Issue GetIsMergedInto() {
        return m_oIsMergedInto;
    }

    public void SetIsMergedInto(Issue oIsMergedInto) {
        m_oIsMergedInto = oIsMergedInto;
    }

    public Issue GetIsSubIssueOf() {
        return m_oIsSubIssueOf;
    }

    public void SetIsSubIssueOf(Issue oIsSubIssueOf) {
        m_oIsSubIssueOf = oIsSubIssueOf;
    }

    public String GetBugURL() {
        return m_sBugURL;
    }

    public void SetBugURL(String sBugURL) {
        m_sBugURL = sBugURL;
    }

    public String GetDescription() {
        return m_sDescription;
    }

    public void SetDescription(String sDescription) {
        m_sDescription = sDescription;
    }

    public String GetIssueURL() {
        return m_sIssueURL;
    }

    public void SetIssueURL(String sIssueURL) {
        m_sIssueURL = sIssueURL;
    }

    public String GetKeyword() {
        return m_sKeyword;
    }

    public void SetKeyword(String sKeyword) {
        m_sKeyword = sKeyword;
    }
    
    // </editor-fold>
    
    // <editor-fold desc="Methods">
    
    /**
     * @summary Constructor
     * @startRealisation Ivan Obradovic 22.08.2011.
     * @finalModification Ivan Obradovic 22.08.2011.
     */
    public Issue()
    {
        super();
    }
    
     // </editor-fold>
}
