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
public class Commit  extends MetadataObject {
    
    // <editor-fold desc="Members">
    
    public foaf_Person m_oHasAuthor;
    public foaf_Person m_oHasCommitter;
    public Repository m_oIsCommitOfRepository;
    public Date m_dtmCommitDate;
    public String m_sCommitMessage;
    public String m_sRevisionTag;
    public File[] m_oHasFile;
    public Component m_oIsCommitOf;

     // </editor-fold>
    
    // <editor-fold desc="Properties">
    
    // </editor-fold>
    
    // <editor-fold desc="Methods">
    
    /**
     * @summary Constructor
     * @startRealisation Ivan Obradovic 23.08.2011.
     * @finalModification Ivan Obradovic 23.08.2011.
     */
    public Commit()
    {
        super();
    }
    
     // </editor-fold>
}
