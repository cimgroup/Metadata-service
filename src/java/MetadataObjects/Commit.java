/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MetadataObjects;

import MetadataCore.MetadataObject;
import org.apache.derby.client.am.DateTime;

/**
 *
 * @author ivano
 */
public class Commit  extends MetadataObject {
    
    // <editor-fold desc="Members">
    
    public Author m_oHasAuthor;
    public foaf_Person m_oHasCommiter;
    public Repository m_oIsCommitOfRepository;
    public DateTime m_dtCommitDate;
    public String m_sCommitMessage;
    public Object m_oRevisionTag;

     // </editor-fold>
    
    // <editor-fold desc="Properties">
    
    public Repository GetIsCommitOfRepository() {
        return m_oIsCommitOfRepository;
    }

    public void SetIsCommitOfRepository(Repository oIsCommitOfRepository) {
        m_oIsCommitOfRepository = oIsCommitOfRepository;
    }

    public DateTime GetCommitDate() {
        return m_dtCommitDate;
    }

    public void SetCommitDate(DateTime dtCommitDate) {
        m_dtCommitDate = dtCommitDate;
    }

    public Object GetRevisionTag() {
        return m_oRevisionTag;
    }

    public void SetRevisionTag(Object oRevisionTag) {
        m_oRevisionTag = oRevisionTag;
    }

    public String GetCommitMessage() {
        return m_sCommitMessage;
    }

    public void SetCommitMessage(String sCommitMessage) {
        m_sCommitMessage = sCommitMessage;
    }

    public Author GetHasAuthor() {
        return m_oHasAuthor;
    }

    public void SetHasAuthor(Author oHasAuthor) {
        m_oHasAuthor = oHasAuthor;
    }

    public foaf_Person GetHasCommiter() {
        return m_oHasCommiter;
    }

    public void SetHasCommiter(foaf_Person oHasCommiter) {
        m_oHasCommiter = oHasCommiter;
    }
    
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
