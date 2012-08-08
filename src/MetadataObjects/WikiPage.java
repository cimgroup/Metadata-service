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
public class WikiPage extends MetadataObject {
    
    // <editor-fold desc="Members">

    public String m_sSource;
    public String m_sURL;
    public String m_sTitle;
    public String m_sRawText;
    public foaf_Person m_oAuthor;
    public foaf_Person[] m_oHasContributors;
    public String m_sEditComment;
    public Boolean m_bIsMinor;
    
    // </editor-fold>
        
    // <editor-fold desc="Properties">

    // </editor-fold>
    
    // <editor-fold desc="Constructor">
    
    /**
     * @summary Constructor
     * @startRealisation Sasa Stojanovic 22.08.2011.
     * @finalModification Sasa Stojanovic 22.08.2011.
     */
    public WikiPage()
    {
        super();
    }
    // </editor-fold>
        
    // <editor-fold desc="Methods">

    // </editor-fold>
    
}
