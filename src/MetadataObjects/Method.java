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
public class Method extends SourceCode {
    
    // <editor-fold desc="Members">
    
    public String m_sContains;
    public String m_sName;
    public int m_iStartLine;
    public int m_iEndLine;

     // </editor-fold>

    // <editor-fold desc="Methods">
    
    /**
     * @summary Constructor
     * @startRealisation Ivan Obradovic 23.08.2011.
     * @finalModification Ivan Obradovic 23.08.2011.
     */
    public Method()
    {
        super();
    }
    
     // </editor-fold>
}
