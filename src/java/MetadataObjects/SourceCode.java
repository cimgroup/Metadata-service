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
public class SourceCode extends MetadataObject {
    
    // <editor-fold desc="Members">
    
    public String m_sIsAbout;
    public SourceCodeFile[] m_oContainedIn;
    
     // </editor-fold>

     // <editor-fold desc="Properties">
    
    public String GetIsAbout() {
        return m_sIsAbout;
    }

    public void SetIsAbout(String sIsAbout) {
        m_sIsAbout = sIsAbout;
    }
    
     public SourceCodeFile[] GetContainedIn() {
        return m_oContainedIn;
    }

    public void SetsContainedIn(SourceCodeFile[] oContainedIn) {
        m_oContainedIn = oContainedIn;
    }
    
    // </editor-fold>
    
    // <editor-fold desc="Methods">
    
    /**
     * @summary Constructor
     * @startRealisation Ivan Obradovic 23.08.2011.
     * @finalModification Ivan Obradovic 23.08.2011.
     */
    public SourceCode()
    {
        super();
    }
    
     // </editor-fold>
}
