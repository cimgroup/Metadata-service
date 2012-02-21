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
public class ForumPost extends Forum {
    
    // <editor-fold desc="Members">
    
    //m_sID - post ID
    public ForumThread m_oInForumThread;

    public Date m_dtmTime; // not in ontology
    public String m_sBody; // in ontology as description
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
    public ForumPost()
    {
        super();
    }
    // </editor-fold>
        
    // <editor-fold desc="Methods">

    // </editor-fold>
}
