/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MetadataObjects;

import MetadataCore.MetadataObject;

/**
 *
 * @author Dejan
 */
public class Identity extends MetadataObject {
    
    // <editor-fold desc="Members">
    
    public foaf_Person[] m_oIs;
    public foaf_Person[] m_oIsnt;
    public Boolean m_bIsRemoveAll = false;
    public Boolean m_bIsntRemoveAll = false;
    
    // </editor-fold>
        
    // <editor-fold desc="Properties">

    // </editor-fold>
    
    // <editor-fold desc="Constructor">
    
    /**
     * @summary Constructor
     * @startRealisation Sasa Stojanovic 04.02.2012.
     * @finalModification Sasa Stojanovic 04.02.2012.
     */
    public Identity()
    {
        super();
    }
    // </editor-fold>
        
    // <editor-fold desc="Methods">

    // </editor-fold>
    
}
