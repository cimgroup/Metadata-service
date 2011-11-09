/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MetadataObjects;

import MetadataCore.MetadataObject;
import org.apache.derby.client.am.DateTime;

/**
 *
 * @author Sasa.Stojanovic
 */
public class Mail extends MetadataObject {
    
    // <editor-fold desc="Members">

    public String m_sHasMessageId;
    public foaf_Person[] m_oFrom;
    public foaf_Person[] m_oTo;
    public foaf_Person[] m_oCc;
    public String[] m_sInReplyTo;
    public String m_sHasSubject;    
    public String m_sIsAbout;
    public String m_sRelatedToSourceCode;
    public DateTime m_dtmHasOrigDate;
        
    // </editor-fold>
        
    // <editor-fold desc="Properties">

    public String GetHasMessageId()
    {
        return m_sHasMessageId;
    }

    public void SetHasMessageId(String sHasMessageId) 
    { 
        m_sHasMessageId = sHasMessageId; 
    }
    
    public foaf_Person[] GetFrom()
    {
        return m_oFrom;
    }

    public void SetFrom(foaf_Person[] oFrom) 
    { 
        m_oFrom = oFrom;
    }
    
    public String[] GetInReplyTo()
    {
        return m_sInReplyTo;
    }

    public void SetInReplyTo(String[] eInReplyTo) {
        m_sInReplyTo = eInReplyTo;
    }

    public foaf_Person[] GetTo() {
        return m_oTo;
    }

    public void SetTo(foaf_Person[] oTo) {
        m_oTo = oTo;
    }

    public DateTime GetHasOrigDate() {
        return m_dtmHasOrigDate;
    }

    public void SetHasOrigDate(DateTime dtmHasOrigDate) {
        m_dtmHasOrigDate = dtmHasOrigDate;
    }

    public String GetHasSubject() {
        return m_sHasSubject;
    }

    public void SetHasSubject(String sHasSubject) {
        m_sHasSubject = sHasSubject;
    }

    public String GetIsAbout() {
        return m_sIsAbout;
    }

    public void SetIsAbout(String sIsAbout) {
        m_sIsAbout = sIsAbout;
    }

    public String GetRelatedToSourceCode() {
        return m_sRelatedToSourceCode;
    }

    public void SetRelatedToSourceCode(String sRelatedToSourceCode) {
        m_sRelatedToSourceCode = sRelatedToSourceCode;
    }

    // </editor-fold>
    
    // <editor-fold desc="Constructor">
    
    /**
     * @summary Constructor
     * @startRealisation Sasa Stojanovic 22.08.2011.
     * @finalModification Sasa Stojanovic 22.08.2011.
     */
    public Mail()
    {
        super();
    }
    // </editor-fold>
        
    // <editor-fold desc="Methods">

    // </editor-fold>
}
