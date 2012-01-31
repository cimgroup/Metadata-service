/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MetadataCore;

import java.net.URI;

/**
 *
 * @author ivano
 */
public abstract class MetadataObject {
    public String m_sID;
    public String m_sObjectURI;
    public String m_sName;
    public String m_sReturnConfig;  //YY#path
    public Boolean m_bRemoved = false;

    public MetadataObject() {

    }
    
    /**
     * get the ID of the object
     * @return ID of the object
     */
    public String GetID() {
        return m_sID;
    }

    /**
     * set the new ID for the object
     * @param sID new ID
     */
    public void SetID(String sID) {
        m_sID = sID;
    }

    /**
     * get the URI of the object
     * @return URI of the object
     */
    public String GetURI() {
        return m_sObjectURI;
    }

    /**
     * set the new URI of the object
     * @param uriURI new URI
     */
    public void SetURI(String sObjectURI) {
        m_sObjectURI = sObjectURI;	
    }

    /**
     * get the name of the object
     * @return the name of the object
     */
    public String GetName() {
        return m_sName;
    }

    /**
     * set the new name of the object
     * @param sName the name of the object
     */
    public void SetName(String sName) {
        m_sName = sName;	
    }
}

