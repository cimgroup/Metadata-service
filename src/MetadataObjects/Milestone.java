/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MetadataObjects;

import MetadataCore.MetadataObject;
import java.util.Date;

/**
 *
 * @author ivano
 */
public class Milestone extends MetadataObject {
    
    // <editor-fold desc="Members">
    
    public Date m_dtmTarget;
    public Issue m_oIsMilestoneOf;

    // </editor-fold>

    // <editor-fold desc="Properties">
    
    public Date GetTarget() {
        return m_dtmTarget;
    }

    public void SetTarget(Date dtmTarget) {
        m_dtmTarget = dtmTarget;
    }
    
    public Issue GetIsMilestoneOf() {
        return m_oIsMilestoneOf;
    }

    public void SetIsMilestoneOf(Issue oIsMilestoneOf) {
        m_oIsMilestoneOf = oIsMilestoneOf;
    }
    
    // </editor-fold>
    
    // <editor-fold desc="Methods">
    
    /**
     * @summary Constructor
     * @startRealisation Ivan Obradovic 22.08.2011.
     * @finalModification Ivan Obradovic 22.08.2011.
     */
    public Milestone()
    {
        super();
    }
    
     // </editor-fold>
}
