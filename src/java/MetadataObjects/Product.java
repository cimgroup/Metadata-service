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
public class Product extends MetadataObject {
    
    // <editor-fold desc="Members">
    
    public Issue[] m_oHasIssue;
    public Component[] m_oHasComponent;
    public String m_sVersion;

    // </editor-fold>
    
    // <editor-fold desc="Properties">
    
    // </editor-fold>
        
    // <editor-fold desc="Methods">
    
    /**
     * @summary Constructor
     * @startRealisation Ivan Obradovic 22.08.2011.
     * @finalModification Ivan Obradovic 22.08.2011.
     */
    public Product()
    {
        super();
    }
    
     // </editor-fold>
}
