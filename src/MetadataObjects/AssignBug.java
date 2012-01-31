/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MetadataObjects;

/**
 *
 * @author Sasa.Stojanovic
 */
public class AssignBug extends BugOperation {
    
    // <editor-fold desc="Members">

    public foaf_Person m_oAssignedTo;

    // </editor-fold>
        
    // <editor-fold desc="Properties">

    public foaf_Person GetAssignedTo() {
        return m_oAssignedTo;
    }

    public void SetAssignedTo(foaf_Person oAssignedTo) {
        m_oAssignedTo = oAssignedTo;
    }

    // </editor-fold>
    
    // <editor-fold desc="Constructor">
    
    /**
     * @summary Constructor
     * @startRealisation Sasa Stojanovic 22.08.2011.
     * @finalModification Sasa Stojanovic 22.08.2011.
     */
    public AssignBug()
    {
        super();
    }
    // </editor-fold>
        
    // <editor-fold desc="Methods">

    // </editor-fold>
    
}
