/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MetadataObjects;

import MetadataCore.MetadataObject;
import java.util.Date;

/**
 *
 * @author Sasa.Stojanovic
 */
public class Mail extends MetadataObject {
    
    // <editor-fold desc="Members">

    public String m_sMessageId;
    public foaf_Person m_oFrom;
    public foaf_Person[] m_oTo;
    public foaf_Person[] m_oCc;
    public Mail m_oInReplyTo;
    public Mail[] m_oReferences;
    public String m_sSubject;    
    public MetadataObject m_oIsAbout;
    public String[] m_sAttachment;
    public SourceCode m_oRelatedToSourceCode;
    public Date m_dtmHasCreationDate;
    public String m_sContent;
        
    // </editor-fold>
        
    // <editor-fold desc="Properties">

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
