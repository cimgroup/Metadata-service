/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MetadataObjects;

/**
 *
 * @author Sasa.Stojanovic
 */
public class FileOperationEvent extends AlertEvent {
    
    // <editor-fold desc="Members">

    public String m_sSourceFile;

    // </editor-fold>
        
    // <editor-fold desc="Properties">

    public String GetSourceFile() {
        return m_sSourceFile;
    }

    public void SetSourceFile(String sSourceFile) {
        m_sSourceFile = sSourceFile;
    }

    // </editor-fold>
    
    // <editor-fold desc="Constructor">
    
    /**
     * @summary Constructor
     * @startRealisation Sasa Stojanovic 22.08.2011.
     * @finalModification Sasa Stojanovic 22.08.2011.
     */
    public FileOperationEvent()
    {
        super();
    }
    // </editor-fold>
        
    // <editor-fold desc="Methods">

    // </editor-fold>
    
}

