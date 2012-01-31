/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MetadataObjects;

import MetadataCore.MetadataObject;

/**
 *
 * @author Sasa.Stojanovic
 */
public class Module extends MetadataObject {
    // <editor-fold desc="Members">
    
    
    public String m_sName;
    public int m_iStartLine;
    public int m_iEndLine;
    public Method[] m_oHasMethod;

     // </editor-fold>
    
    // <editor-fold desc="Properties">
       
    // </editor-fold>
    
    // <editor-fold desc="Methods">
    
    /**
     * @summary Constructor
     * @startRealisation Ivan Obradovic 23.08.2011.
     * @finalModification Ivan Obradovic 23.08.2011.
     */
    public Module()
    {
        super();
    }
    
     // </editor-fold>
}
