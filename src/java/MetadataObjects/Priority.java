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
public class Priority extends MetadataObject {
    
    // <editor-fold desc="Members">
    
    public int m_iPriority;
    public Issue m_oIsPriorityOf;
    public Priority m_oIsMoreImportant;

    // </editor-fold>

    // <editor-fold desc="Properties">
    
    public Priority GetIsMoreImportant() {
        return m_oIsMoreImportant;
    }

    public void SetIsMoreImportant(Priority oIsMoreImportant) {
        m_oIsMoreImportant = oIsMoreImportant;
    }
    
    public Issue GetIsPriorityOf() {
        return m_oIsPriorityOf;
    }

    public void SetIsPriorityOf(Issue oIsPriorityOf) {
        m_oIsPriorityOf = oIsPriorityOf;
    }

    // </editor-fold>
    
    // <editor-fold desc="Methods">
    
    /**
     * @summary Constructor
     * @startRealisation Ivan Obradovic 22.08.2011.
     * @finalModification Ivan Obradovic 22.08.2011.
     */
    public Priority()
    {
        super();
    }
    
     // </editor-fold>
}
