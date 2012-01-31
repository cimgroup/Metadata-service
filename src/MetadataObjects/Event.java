/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MetadataObjects;

import MetadataCore.MetadataObject;
import org.apache.derby.client.am.DateTime;

/**
 *
 * @author Sasa.Stojanovic
 */
public class Event extends MetadataObject {
    
    // <editor-fold desc="Members">

    public String m_sHasEventID;
    public DateTime m_dtmHasStartTime;
    public DateTime m_dtmHasEndTime;
    public String m_sHasEventType;
    public String m_sHasEventSource;

    // </editor-fold>
        
    // <editor-fold desc="Properties">

    public DateTime GetHasEndTime() {
        return m_dtmHasEndTime;
    }

    public void SetHasEndTime(DateTime dtmHasEndTime) {
        m_dtmHasEndTime = dtmHasEndTime;
    }

    public DateTime GetM_dtmHasStartTime() {
        return m_dtmHasStartTime;
    }

    public void SetHasStartTime(DateTime dtmHasStartTime) {
        m_dtmHasStartTime = dtmHasStartTime;
    }

    public String GetHasEventID() {
        return m_sHasEventID;
    }

    public void SetHasEventID(String sHasEventID) {
        m_sHasEventID = sHasEventID;
    }

    public String GetHasEventSource() {
        return m_sHasEventSource;
    }

    public void SetHasEventSource(String sHasEventSource) {
        m_sHasEventSource = sHasEventSource;
    }

    public String GetHasEventType() {
        return m_sHasEventType;
    }

    public void SetHasEventType(String sHasEventType) {
        m_sHasEventType = sHasEventType;
    }

    // </editor-fold>
    
    // <editor-fold desc="Constructor">
    
    /**
     * @summary Constructor
     * @startRealisation Sasa Stojanovic 22.08.2011.
     * @finalModification Sasa Stojanovic 22.08.2011.
     */
    public Event()
    {
        super();
    }
    // </editor-fold>
        
    // <editor-fold desc="Methods">

    // </editor-fold>
    
}
