/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MetadataCore;

import MetadataCore.MetadataGlobal.AnnotationData;
import MetadataObjects.*;

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
     * @summary Method for creating new commit
     * @startRealisation Sasa Stojanovic 16.01.2012.
     * @finalModification Sasa Stojanovic 16.01.2012.
     * @return Commit object
     */
    public static Commit CreateNewCommit()
    {
        try
        {
            Commit oCommit = new Commit();
            return oCommit;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    /** 
     * @summary Method for creating new mail
     * @startRealisation Sasa Stojanovic 02.02.2012.
     * @finalModification Sasa Stojanovic 02.02.2012.
     * @return Mail object
     */
    public static Mail CreateNewMail()
    {
        try
        {
            Mail oMail = new Mail();
            return oMail;
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
     * @return foaf_Person object
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
    
    /** 
     * @summary Method for creating new annotation.
     * @startRealisation  Dejan Milosavljevic 16.01.2012.
     * @finalModification Dejan Milosavljevic 16.01.2012.
     * @return Bug object
     */
    public static AnnotationData CreateNewAnnotation()
    {
        try
        {
            AnnotationData oAnnotation = new AnnotationData();
            return oAnnotation;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    /** 
     * @summary Method for creating new forum post.
     * @startRealisation  Dejan Milosavljevic 17.01.2012.
     * @finalModification Dejan Milosavljevic 17.01.2012.
     * @return Bug object
     */
    public static NewForumPost CreateNewForumPost()
    {
        try
        {
            NewForumPost oForumPost = new NewForumPost();
            return oForumPost;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    /** 
     * @summary Method for creating new competence.
     * @startRealisation  Dejan Milosavljevic 02.02.2012.
     * @finalModification Dejan Milosavljevic 02.02.2012.
     * @return Bug object
     */
    public static Competence CreateNewCompetence()
    {
        try
        {
            Competence oCompetence = new Competence();
            return oCompetence;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
