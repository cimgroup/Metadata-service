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
public class Forum extends MetadataObject {
    
    // <editor-fold desc="Members">
    
    public String m_sFirstPost;
    public String m_sURL;
    public String m_sName;
    public String m_sSubject;
    
    public String m_sForumItemID;
    
    public ForumThread m_oHasThreads;
    public foaf_Person m_oHasMember;
    public foaf_Person m_oAuthor;
    public Object m_oIsAbout;
    
    // </editor-fold>
        
    // <editor-fold desc="Properties">

    // </editor-fold>
    
    // <editor-fold desc="Constructor">
    
    /**
     * @summary Constructor
     * @startRealisation Sasa Stojanovic 21.02.2012.
     * @finalModification Sasa Stojanovic 21.02.2012.
     */
    public Forum()
    {
        super();
    }
    // </editor-fold>
        
    // <editor-fold desc="Methods">

    // </editor-fold>
    
}
