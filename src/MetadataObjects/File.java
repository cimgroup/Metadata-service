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
public class File  extends MetadataObject {
    
     // <editor-fold desc="Members">
    
    public Repository m_oIsFileOfRepository;
    public String m_sFileID;
    public String m_sFilePath;
    public String m_sRevisionID;

     // </editor-fold>
    
    // <editor-fold desc="Properties">
    
    public Repository GetIsFileOfRepository() {
        return m_oIsFileOfRepository;
    }

    public void SetIsFileOfRepository(Repository oIsFileOfRepository) {
        m_oIsFileOfRepository = oIsFileOfRepository;
    }

    public String GetFileID() {
        return m_sFileID;
    }

    public void SetFileID(String sFileID) {
        m_sFileID = sFileID;
    }

    public String GetFilePath() {
        return m_sFilePath;
    }

    public void SetFilePath(String sFilePath) {
        m_sFilePath = sFilePath;
    }

    public String GetRevisionID() {
        return m_sRevisionID;
    }

    public void SetRevisionID(String sRevisionID) {
        m_sRevisionID = sRevisionID;
    }
    
     // </editor-fold>
    
    // <editor-fold desc="Methods">
    
    /**
     * @summary Constructor
     * @startRealisation Ivan Obradovic 23.08.2011.
     * @finalModification Ivan Obradovic 23.08.2011.
     */
    public File()
    {
        super();
    }
    
     // </editor-fold>
}
