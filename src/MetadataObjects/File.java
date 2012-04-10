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
public class File extends MetadataObject {
    
    // <editor-fold desc="Members">
    
    public Repository m_oIsFileOfRepository;
    public String m_sFileID;
    public String m_sFilePath;
    public String m_sRevisionID;
    
    public FileOperationEvent m_oHasAction;
    public String m_sBranch;
    public Module[] m_oHasModule;
    public String m_sName;

     // </editor-fold>
    
    // <editor-fold desc="Properties">
       
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
