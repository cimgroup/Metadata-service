/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MetadataCore;

import MetadataObjects.Component;
import MetadataObjects.Issue;
import MetadataObjects.MetadataPerson;
import MetadataObjects.foaf_Person;

/**
 *
 * @author ivano
 */
public class MetadataObjectFactory {
    
    /** 
     * @summary Method for creating new bug
     * @startRealisation Sasa Stojanovic 24.06.2011.
     * @finalModification Sasa Stojanovic 24.06.2011.
     * @return Bug object
     */
    public static Issue CreateNewIssue()
    {
        try
        {
            Issue oIssue = new Issue();
            return oIssue;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    /** 
     * @summary Method for creating new person
     * @startRealisation Sasa Stojanovic 24.06.2011.
     * @finalModification Sasa Stojanovic 24.06.2011.
     * @return MetadataPerson object
     */
    public static foaf_Person CreateNewPerson()
    {
        try
        {
            foaf_Person oPerson = new foaf_Person();
            return oPerson;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /** 
     * @summary Method for creating new component
     * @startRealisation Sasa Stojanovic 31.10.2011.
     * @finalModification Sasa Stojanovic 31.10.2011.
     * @return MetadataPerson object
     */
    public static Component CreateNewComponent() {
        try
        {
            Component oComponent = new Component();
            return oComponent;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

}