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
public class State  extends MetadataObject {
    
    // <editor-fold desc="Members">
    
    public Issue m_oIsStateOf;

    // </editor-fold>
    
    // <editor-fold desc="Properties">
    
    public Issue GetIsStateOf() {
        return m_oIsStateOf;
    }

    public void SetIsStateOf(Issue oIsStateOf) {
        m_oIsStateOf = oIsStateOf;
    }
    
    // </editor-fold>
    
    // <editor-fold desc="Methods">
    
    /**
     * @summary Constructor
     * @startRealisation Ivan Obradovic 22.08.2011.
     * @finalModification Ivan Obradovic 22.08.2011.
     */
    public State()
    {
        super();
    }
    
     // </editor-fold>
}
