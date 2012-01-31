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
public class Page extends MetadataObject {
    
    // <editor-fold desc="Members">

    public String m_sHasName;
    public String m_sHasURL;
    public foaf_Person m_oHasAuthor;
    public String m_sHasSubject;
    public String m_sIsAbout;
    
    // </editor-fold>
        
    // <editor-fold desc="Properties">

    public foaf_Person GetHasAuthor() {
        return m_oHasAuthor;
    }

    public void SetHasAuthor(foaf_Person oHasAuthor) {
        m_oHasAuthor = oHasAuthor;
    }

    public String GetHasName() {
        return m_sHasName;
    }

    public void SetHasName(String sHasName) {
        m_sHasName = sHasName;
    }

    public String GetHasSubject() {
        return m_sHasSubject;
    }

    public void SetHasSubject(String sHasSubject) {
        m_sHasSubject = sHasSubject;
    }

    public String GetHasURL() {
        return m_sHasURL;
    }

    public void SetHasURL(String sHasURL) {
        m_sHasURL = sHasURL;
    }

    public String GetIsAbout() {
        return m_sIsAbout;
    }

    public void SetIsAbout(String sIsAbout) {
        m_sIsAbout = sIsAbout;
    }

    // </editor-fold>
    
    // <editor-fold desc="Constructor">
    
    /**
     * @summary Constructor
     * @startRealisation Sasa Stojanovic 22.08.2011.
     * @finalModification Sasa Stojanovic 22.08.2011.
     */
    public Page()
    {
        super();
    }
    // </editor-fold>
        
    // <editor-fold desc="Methods">

    // </editor-fold>
    
}
