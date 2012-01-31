/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MetadataObjects;

/**
 *
 * @author Sasa.Stojanovic
 */
public class MailEvent extends AlertEvent {
    
    // <editor-fold desc="Members">

    public String m_sHasRecipient;

    // </editor-fold>
        
    // <editor-fold desc="Properties">

    public String GetHasRecipient() {
        return m_sHasRecipient;
    }

    public void SetHasRecipient(String sHasRecipient) {
        m_sHasRecipient = sHasRecipient;
    }

    // </editor-fold>
    
    // <editor-fold desc="Constructor">
    
    /**
     * @summary Constructor
     * @startRealisation Sasa Stojanovic 22.08.2011.
     * @finalModification Sasa Stojanovic 22.08.2011.
     */
    public MailEvent()
    {
        super();
    }
    // </editor-fold>
        
    // <editor-fold desc="Methods">

    // </editor-fold>
    
}
