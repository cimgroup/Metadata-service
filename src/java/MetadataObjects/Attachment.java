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
public class Attachment extends MetadataObject {
    
    // <editor-fold desc="Members">
    
    public String m_sFilename;
    public String m_sType;
    public foaf_Person m_oHasCreator;
    public Issue m_oIsAttachmentOf;

     // </editor-fold>

    // <editor-fold desc="Properties">
    
    public Issue GetIsAttachmentOf() {
        return m_oIsAttachmentOf;
    }

    public void SetIsAttachmentOf(Issue oIsAttachmentOf) {
        m_oIsAttachmentOf = oIsAttachmentOf;
    }

    public String GetFilename() {
        return m_sFilename;
    }

    public void SetFilename(String sFilename) {
        m_sFilename = sFilename;
    }

    public String GetType() {
        return m_sType;
    }

    public void SetType(String sType) {
        m_sType = sType;
    }
    
    public foaf_Person GetHasCreator() {
        return m_oHasCreator;
    }

    public void SetHasCreator(foaf_Person oHasCreator) {
        m_oHasCreator = oHasCreator;
    }
    
    // </editor-fold>
        
    // <editor-fold desc="Methods">
    
    /**
     * @summary Constructor
     * @startRealisation Ivan Obradovic 22.08.2011.
     * @finalModification Ivan Obradovic 22.08.2011.
     */
    public Attachment()
    {
        super();
    }
    
     // </editor-fold>
}
