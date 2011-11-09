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
public class Interface extends SourceCode {
    
    // <editor-fold desc="Members">
    
    public String m_sContains;

     // </editor-fold>

    public String GetContains() {
        return m_sContains;
    }

    public void SetContains(String sContains) {
        m_sContains = sContains;
    }

    // <editor-fold desc="Methods">
    
    /**
     * @summary Constructor
     * @startRealisation Ivan Obradovic 23.08.2011.
     * @finalModification Ivan Obradovic 23.08.2011.
     */
    public Interface()
    {
        super();
    }
    
     // </editor-fold>
    
}
