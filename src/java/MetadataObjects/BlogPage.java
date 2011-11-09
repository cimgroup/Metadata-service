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
public class BlogPage extends MetadataObject {
    
    // <editor-fold desc="Members">

    public String m_sHasName;
    public String m_sHasURL;
    public BlogPage[] m_oLinksTo;
    public String m_sIsAbout;
    
    // </editor-fold>
        
    // <editor-fold desc="Properties">

    public String GetHasName() {
        return m_sHasName;
    }

    public void SetHasName(String sHasName) {
        m_sHasName = sHasName;
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

    public BlogPage[] GetLinksTo() {
        return m_oLinksTo;
    }

    public void SetLinksTo(BlogPage[] oLinksTo) {
        m_oLinksTo = oLinksTo;
    }

    // </editor-fold>
    
    // <editor-fold desc="Constructor">
    
    /**
     * @summary Constructor
     * @startRealisation Sasa Stojanovic 22.08.2011.
     * @finalModification Sasa Stojanovic 22.08.2011.
     */
    public BlogPage()
    {
        super();
    }
    // </editor-fold>
        
    // <editor-fold desc="Methods">

    // </editor-fold>
    
}
