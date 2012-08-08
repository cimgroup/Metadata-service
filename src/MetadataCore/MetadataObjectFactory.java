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
     * @summary Method for creating new issue
     * @startRealisation Sasa Stojanovic 24.06.2011.
     * @finalModification Sasa Stojanovic 24.06.2011.
     * @return Issue object
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
     * @return Component object
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
     * @return AnnotationData object
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
     * @finalModification Sasa Stojanovic 21.02.2012
     * @return ForumPost object
     */
    public static ForumPost CreateNewForumPost()
    {
        try
        {
            ForumPost oForumPost = new ForumPost();
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
     * @return Competence object
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
    
    
    /** 
     * @summary Method for creating new identity
     * @startRealisation Sasa Stojanovic 04.02.2012.
     * @finalModification Sasa Stojanovic 04.02.2012.
     * @return Identity object
     */
    public static Identity CreateNewIdentity()
    {
        try
        {
            Identity oIdentity = new Identity();
            return oIdentity;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    
    /** 
     * @summary Method for creating new concept
     * @startRealisation Sasa Stojanovic 17.05.2012.
     * @finalModification Sasa Stojanovic 17.05.2012.
     * @return Concept object
     */
    public static Concept CreateNewConcept()
    {
        try
        {
            Concept oConcept = new Concept();
            return oConcept;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    /** 
     * @summary Method for creating new wiki page
     * @startRealisation Sasa Stojanovic 08.08.2012.
     * @finalModification Sasa Stojanovic 08.08.2012.
     * @return WikiPage object
     */
    public static WikiPage CreateNewWikiPage()
    {
        try
        {
            WikiPage oWikiPage = new WikiPage();
            return oWikiPage;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
