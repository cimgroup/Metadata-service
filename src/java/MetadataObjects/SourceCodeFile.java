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
public class SourceCodeFile extends SourceCode {
    
    // <editor-fold desc="Members">
    
    public SourceCode m_oContains;
    

     // </editor-fold>

    public SourceCode GetContains() {
        return m_oContains;
    }

    public void SetContains(SourceCode oContains) {
        m_oContains = oContains;
    }

   
    // <editor-fold desc="Methods">
    
    /**
     * @summary Constructor
     * @startRealisation Ivan Obradovic 23.08.2011.
     * @finalModification Ivan Obradovic 23.08.2011.
     */
    public SourceCodeFile()
    {
        super();
    }
    
     // </editor-fold>
}
