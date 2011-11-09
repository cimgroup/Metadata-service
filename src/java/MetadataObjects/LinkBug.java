/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MetadataObjects;

/**
 *
 * @author Sasa.Stojanovic
 */
public class LinkBug extends BugOperation {
    
    // <editor-fold desc="Members">

    public Bug m_oLinkedToBug;


    // </editor-fold>
        
    // <editor-fold desc="Properties">

    public Bug GetLinkedToBug() {
        return m_oLinkedToBug;
    }

    public void SetLinkedToBug(Bug oLinkedToBug) {
        m_oLinkedToBug = oLinkedToBug;
    }
    
    // <editor-fold desc="Constructor">
    
    /**
     * @summary Constructor
     * @startRealisation Sasa Stojanovic 22.08.2011.
     * @finalModification Sasa Stojanovic 22.08.2011.
     */
    public LinkBug()
    {
        super();
    }
    // </editor-fold>
        
    // <editor-fold desc="Methods">

    // </editor-fold>
    
}
