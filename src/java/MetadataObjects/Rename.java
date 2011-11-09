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
public class Rename extends Action {
    
    // <editor-fold desc="Members">
    
    public File m_oFromFile;
    public File m_oToFile;

     // </editor-fold>
    
    // <editor-fold desc="Properties">
    
    public File GetsFromFile() {
        return m_oFromFile;
    }

    public void SetsFromFile(File oFromFile) {
        m_oFromFile = oFromFile;
    }

    public File GetsToFile() {
        return m_oToFile;
    }

    public void SetsToFile(File oToFile) {
        m_oToFile = oToFile;
    }
    
     // </editor-fold>
    
    // <editor-fold desc="Methods">
    
    /**
     * @summary Constructor
     * @startRealisation Ivan Obradovic 23.08.2011.
     * @finalModification Ivan Obradovic 23.08.2011.
     */
    public Rename()
    {
        super();
    }
    
     // </editor-fold>
}
