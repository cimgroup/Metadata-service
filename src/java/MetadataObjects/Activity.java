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
public class Activity extends MetadataObject {
    
    // <editor-fold desc="Members">
    
    public String m_sAdded;
    public String m_sPerformed;
    public String m_sRemoved;
    public String m_sWhat;
    public foaf_Person m_oHasInvolvedPerson;
    public Date m_dtmWhen;
    public Issue m_oIsActivityOf;

     // </editor-fold>
        
    // <editor-fold desc="Properties">
    
    // </editor-fold>
        
    // <editor-fold desc="Methods">
    
    /**
     * @summary Constructor
     * @startRealisation Ivan Obradovic 22.08.2011.
     * @finalModification Ivan Obradovic 22.08.2011.
     */
    public Activity()
    {
        super();
    }
    
     // </editor-fold>
}
