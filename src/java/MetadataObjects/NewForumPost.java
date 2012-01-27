/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MetadataObjects;

import java.util.Date;

/**
 *
 * @author Sasa.Stojanovic
 */
public class NewForumPost extends ForumEvent {
    
    // <editor-fold desc="Members">
    
    //m_sID - post ID
    public NewForumThread m_oForumThread;
    public ForumEvent m_oForum;
    public String m_sForumItemID;
    ////public String m_sForumID;
    ////public String m_sThreadID;
    public Date m_dtmTime; // not in ontology
    public String m_sSubject;
    public String m_sBody; // in ontology as description
    //m_oHasAuthor - author
    public String m_sCategory; // not in ontology
  
    // </editor-fold>
        
    // <editor-fold desc="Properties">

    // </editor-fold>
    
    // <editor-fold desc="Constructor">
    
    /**
     * @summary Constructor
     * @startRealisation Sasa Stojanovic 22.08.2011.
     * @finalModification Sasa Stojanovic 22.08.2011.
     */
    public NewForumPost()
    {
        super();
    }
    // </editor-fold>
        
    // <editor-fold desc="Methods">

    // </editor-fold>
    
}