/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MetadataObjects;

/**
 *
 * @author Sasa.Stojanovic
 */
public class CopyFile extends FileOperationEvent {
    
    // <editor-fold desc="Members">

    public String m_sTargetFile;

    // </editor-fold>
        
    // <editor-fold desc="Properties">

    public String GetTargetFile() {
        return m_sTargetFile;
    }

    public void SetTargetFile(String sTargetFile) {
        m_sTargetFile = sTargetFile;
    }

    // </editor-fold>
    
    // <editor-fold desc="Constructor">
    
    /**
     * @summary Constructor
     * @startRealisation Sasa Stojanovic 22.08.2011.
     * @finalModification Sasa Stojanovic 22.08.2011.
     */
    public CopyFile()
    {
        super();
    }
    // </editor-fold>
        
    // <editor-fold desc="Methods">

    // </editor-fold>
    
}