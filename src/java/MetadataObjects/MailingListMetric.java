/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MetadataObjects;

/**
 *
 * @author ivano
 */
public class MailingListMetric extends InterestMetric {
    
    // <editor-fold desc="Members">
    
    public Mail m_oInMessage;
    
    // </editor-fold>
        
    // <editor-fold desc="Properties">

    public Mail GetInMessage() {
        return m_oInMessage;
    }

    public void SetInMessage(Mail oInMessage) {
        m_oInMessage = oInMessage;
    }
    
    // </editor-fold>
    
    // <editor-fold desc="Constructor">
    
    /**
     * @summary Constructor
     * @startRealisation Sasa Stojanovic 23.08.2011.
     * @finalModification Sasa Stojanovic 23.08.2011.
     */
    public MailingListMetric()
    {
        super();
    }
    // </editor-fold>
        
    // <editor-fold desc="Methods">

    // </editor-fold>
    
}