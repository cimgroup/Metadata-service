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
public class Action extends MetadataObject {
    
     // <editor-fold desc="Members">
    
    public Commit m_oIsActionOf;
    public String m_sOnBranch;

     // </editor-fold>
    
    // <editor-fold desc="Properties">
    
    public Commit GetIsActionOf() {
        return m_oIsActionOf;
    }

    public void SetIsActionOf(Commit oIsActionOf) {
        m_oIsActionOf = oIsActionOf;
    }

    public String GetOnBranch() {
        return m_sOnBranch;
    }

    public void SetOnBranch(String sOnBranch) {
        m_sOnBranch = sOnBranch;
    }
    
     // </editor-fold>
    
    // <editor-fold desc="Methods">
    
    /**
     * @summary Constructor
     * @startRealisation Ivan Obradovic 23.08.2011.
     * @finalModification Ivan Obradovic 23.08.2011.
     */
    public Action()
    {
        super();
    }
    
     // </editor-fold>
}
