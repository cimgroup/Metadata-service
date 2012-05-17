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
public class Concept extends MetadataObject {
    
     // <editor-fold desc="Members">
    
    public String m_sLabel;
    public String m_sComment;
    public String[] m_sSameAs;
    public String[] m_sLinksTo;
    public String[] m_sSubClassOf;
    public String[] m_sSuperClassOf;


     // </editor-fold>
    
    // <editor-fold desc="Properties">

    // </editor-fold>
        
    // <editor-fold desc="Methods">
    
    /**
     * @summary Constructor
     * @startRealisation Ivan Obradovic 22.08.2011.
     * @finalModification Ivan Obradovic 22.08.2011.
     */
    public Concept()
    {
        super();
    }
    
     // </editor-fold>
}
