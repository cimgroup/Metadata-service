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
public class ComputerSystem extends MetadataObject {
    
     // <editor-fold desc="Members">
    
    public String m_sOs;
    public String m_sPlatform;
    public Issue[] m_oIsComputerSystemOf;

     // </editor-fold>
    
    // <editor-fold desc="Properties">
    
    public String GetOs() {
        return m_sOs;
    }

    public void SetOs(String sOs) {
        m_sOs = sOs;
    }

    public String GetPlatform() {
        return m_sPlatform;
    }

    public void SetPlatform(String sPlatform) {
        m_sPlatform = sPlatform;
    }
    
    public Issue[] GetIsComputerSystemOf() {
        return m_oIsComputerSystemOf;
    }

    public void SetIsComputerSystemOf(Issue[] oIsComputerSystemOf) {
        m_oIsComputerSystemOf = oIsComputerSystemOf;
    }
    
    // </editor-fold>
    
    // <editor-fold desc="Methods">
    
    /**
     * @summary Constructor
     * @startRealisation Ivan Obradovic 22.08.2011.
     * @finalModification Ivan Obradovic 22.08.2011.
     */
    public ComputerSystem()
    {
        super();
    }
    
     // </editor-fold>
}
