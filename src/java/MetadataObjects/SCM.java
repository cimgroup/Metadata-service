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
public class SCM extends Repository {
    
    // <editor-fold desc="Members">
    
    public Commit m_oHasCommit;
    public String m_sHasFile;

     // </editor-fold>
    
     // <editor-fold desc="Properties">
    
    public Commit GetHasCommit() {
        return m_oHasCommit;
    }

    public void SetHasCommit(Commit oHasCommit) {
        m_oHasCommit = oHasCommit;
    }

    public String GetHasFile() {
        return m_sHasFile;
    }

    public void SetHasFile(String sHasFile) {
        m_sHasFile = sHasFile;
    }
    
     // </editor-fold>
    
    // <editor-fold desc="Methods">
    
    /**
     * @summary Constructor
     * @startRealisation Ivan Obradovic 23.08.2011.
     * @finalModification Ivan Obradovic 23.08.2011.
     */
    public SCM()
    {
        super();
    }
    
     // </editor-fold>
}
