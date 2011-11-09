/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MetadataObjects;

import MetadataCore.MetadataObject;
import java.util.Date;

/**
 *
 * @author ivano
 */
public class Comment extends MetadataObject {
    
    // <editor-fold desc="Members">
    
    public Date m_dtmDate;
    public Integer m_iNumber;
    public String m_sText;
    public foaf_Person m_oHasCommentor;
    public foaf_Person m_oHasCreator;
    public Issue m_oIsCommentOf;
    
     // </editor-fold>
    
    // <editor-fold desc="Properties">
    
    public Date GetDate() {
        return m_dtmDate;
    }

    public void SetDate(Date dtmDate) {
        m_dtmDate = dtmDate;
    }

    public int GetNumber() {
        return m_iNumber;
    }

    public void SetNumber(int iNumber) {
        m_iNumber = iNumber;
    }

    public String GetText() {
        return m_sText;
    }

    public void SetText(String sText) {
        m_sText = sText;
    }

    public foaf_Person GetHasCommentor() {
        return m_oHasCommentor;
    }

    public void SetHasCommentor(foaf_Person oHasCommentor) {
        m_oHasCommentor = oHasCommentor;
    }
        
    public foaf_Person GetHasCreator() {
        return m_oHasCreator;
    }

    public void SetHasCreator(foaf_Person oHasCreator) {
        m_oHasCreator = oHasCreator;
    }
    
    public Issue GetIsCommentOf() {
        return m_oIsCommentOf;
    }

    public void SetIsCommentOf(Issue oIsCommentOf) {
        m_oIsCommentOf = oIsCommentOf;
    }
    
    // </editor-fold>
        
    // <editor-fold desc="Methods">
    
    /**
     * @summary Constructor
     * @startRealisation Ivan Obradovic 22.08.2011.
     * @finalModification Ivan Obradovic 22.08.2011.
     */
    public Comment()
    {
        super();
    }
    
     // </editor-fold>
    
}
