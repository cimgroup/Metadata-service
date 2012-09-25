/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MetadataCore;

import MetadataCore.MetadataGlobal.AnnotationData;
import MetadataObjects.*;
import com.hp.hpl.jena.ontology.OntModelSpec;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author ivano
 */
public class MetadataModel {


//    public void LoadObject()
//    {
//        
//    }
    
//    public static void SaveObject()
//    {
//        
//    }
    
   
//    /** 
//     * @summary Method for performing search for given IDS
//     * @startRealisation Sasa Stojanovic 24.06.2011.
//     * @finalModification Sasa Stojanovic 24.06.2011.
//     * @param sSearchType - type of search
//     * @param sIDs - list of IDs
//     */
//    public static void SearchForIDs(String sSearchType, ArrayList<String> arIDs) {
//        try
//        {
//            MetadataRDFConverter.SearchForIDs(sSearchType, arIDs);
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//    }
    

    /** 
     * @summary Method for creating response to Keui
     * @startRealisation Sasa Stojanovic 31.08.2011.
     * @finalModification Sasa Stojanovic 31.08.2011.
     */
    public static void CreateNewItemKeuiResponse(String sEventName, String sEventId, ArrayList<String> arResult)
    {
        MetadataXMLCreator.CreateXMLNewItemKeuiResponse(sEventName, sEventId, arResult);
    }
    
    /** 
     * @summary Method for saving new issue
     * @startRealisation  Sasa Stojanovic     31.08.2011.
     * @finalModification Dejan Milosavljevic 20.02.2012.
     * @param sEventId - event id.
     * @param oIssue - issue object.
     * @param bIsUpdate - marks if this is an update.
     */
    static void SaveObjectNewIssue(String sEventId, Element eOriginalData, Issue oIssue, boolean bIsUpdate) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException
    {
        oIssue = MetadataRDFConverter.SaveIssue(oIssue, bIsUpdate);
        if (oIssue != null)
        {
            if (bIsUpdate)
            {
                MetadataXMLCreator.CreateXMLNewItemResponse(MetadataConstants.c_ET_ALERT_Metadata_IssueUpdate_Stored, sEventId, eOriginalData, oIssue);
            }
            else
            {
                MetadataXMLCreator.CreateXMLNewItemResponse(MetadataConstants.c_ET_ALERT_Metadata_IssueNew_Stored, sEventId, eOriginalData, oIssue);
            }
        }
    }
    
    /** 
     * @summary Method for saving new commit
     * @startRealisation Sasa Stojanovic 16.01.2012.
     * @finalModification Sasa Stojanovic 16.01.2012.
     * @param sEventId - event id
     * @param oCommit - commit object
     */
    static void SaveObjectNewCommit(String sEventId, Element eOriginalData, Commit oCommit)
    {
        oCommit = MetadataRDFConverter.SaveCommit(oCommit);
        MetadataXMLCreator.CreateXMLNewItemResponse(MetadataConstants.c_ET_ALERT_Metadata_CommitNew_Stored, sEventId, eOriginalData, oCommit);
    }
    
    /** 
     * @summary Method for saving new mail
     * @startRealisation Sasa Stojanovic 02.02.2012.
     * @finalModification Sasa Stojanovic 02.02.2012.
     * @param sEventId - event id
     * @param oMail - mail object
     */
    static void SaveObjectNewMail(String sEventId, Element eOriginalData, Mail oMail)
    {
        oMail = MetadataRDFConverter.SaveMail(oMail);
        MetadataXMLCreator.CreateXMLNewItemResponse(MetadataConstants.c_ET_ALERT_Metadata_MailNew_Stored, sEventId, eOriginalData, oMail);
    }
    
    /** 
     * @summary Method for saving new wiki page
     * @startRealisation Sasa Stojanovic 08.08.2012.
     * @finalModification Sasa Stojanovic 08.08.2012.
     * @param sEventId - event id
     * @param oWikiPage - wiki page object
     */
    static void SaveObjectNewWikiPage(String sEventId, Element eOriginalData, WikiPage oWikiPage, boolean bIsUpdate)
    {
        oWikiPage = MetadataRDFConverter.SaveWikiPage(oWikiPage, bIsUpdate);
        if (!bIsUpdate)
            MetadataXMLCreator.CreateXMLNewItemResponse(MetadataConstants.c_ET_ALERT_Metadata_ArticleAdded_Stored, sEventId, eOriginalData, oWikiPage);
        else
            MetadataXMLCreator.CreateXMLNewItemResponse(MetadataConstants.c_ET_ALERT_Metadata_ArticleModified_Stored, sEventId, eOriginalData, oWikiPage);
    }
    
    /** 
     * @summary Method for removing wiki page
     * @startRealisation Sasa Stojanovic 08.08.2012.
     * @finalModification Sasa Stojanovic 08.08.2012.
     * @param sEventId - event id
     * @param oWikiPage - wiki page object
     */
    static void RemoveWikiPage(String sEventId, Element eOriginalData, WikiPage oWikiPage)
    {
        oWikiPage = MetadataRDFConverter.RemoveWikiPage(oWikiPage);
        MetadataXMLCreator.CreateXMLNewItemResponse(MetadataConstants.c_ET_ALERT_Metadata_ArticleDeleted_Stored, sEventId, eOriginalData, oWikiPage);
    }
            
//    /**
//     * @summary Method for saving new person
//     * @startRealisation Sasa Stojanovic 05.09.2011.
//     * @finalModification Sasa Stojanovic 05.09.2011.
//     * @param sEventId - event id
//     * @param oPerson - person object
//     */
//    static void SaveObjectNewPerson(String sEventId, foaf_Person oPerson)
//    {
//        oPerson = MetadataRDFConverter.SavePerson(oPerson);
//        MetadataXMLCreator.CreateXMLNewItemResponse(MetadataConstants.c_ET_person_replyNewUpdate, sEventId, null, oPerson);
//    }
 
    /**
     * @summary Method for saving new forum post.
     * @startRealisation  Dejan Milosavljevic 17.01.2012.
     * @finalModification Dejan Milosavljevic 18.01.2012.
     * @param sEventId - event id.
     * @param eOriginalData - original data. 
     * @param oForumPost - forum post object.
     */
    static void SaveObjectNewForumPost(String sEventId, Element eOriginalData, ForumPost oForumPost)
    {
        oForumPost = MetadataRDFConverter.SaveForumPost(oForumPost);
        MetadataXMLCreator.CreateXMLNewItemResponse(MetadataConstants.c_ET_ALERT_Metadata_ForumPostNew_Stored, sEventId, eOriginalData, oForumPost);
    }

    /**
     * @summary Method for saving new annotation.
     * @startRealisation  Dejan Milosavljevic 17.01.2012.
     * @finalModification Dejan Milosavljevic 01.02.2012.
     * @param sEventName - event name.
     * @param dDoc - original XML document. 
     * @param oAnnotation - annotation data object.
     */
    static void SaveObjectNewAnnotationData(String sEventName, Document dDoc, AnnotationData oAnnotation)
    {
        //uncomment this after implementing changes
        oAnnotation = MetadataRDFConverter.SaveAnnotationData(oAnnotation);
        MetadataXMLCreator.CreateXMLNewAnnotationResponse(dDoc, sEventName);
    }
    
    /**
     * @summary Method for saving new competence.
     * @startRealisation  Dejan Milosavljevic 02.02.2012.
     * @finalModification Dejan Milosavljevic 20.02.2012.
     * @param sEventId - event id.
     * @param eOriginalData - original data. 
     * @param oCompetence - competence object.
     * @param bIsUpdate - marks if this is an update.
     */
    static void SaveObjectNewCompetence(String sEventId, Element eOriginalData, Competence oCompetence, boolean bIsUpdate)
    {
        oCompetence = MetadataRDFConverter.SaveCompetence(oCompetence);
        if (bIsUpdate)
        {
            MetadataXMLCreator.CreateXMLNewItemResponse(MetadataConstants.c_ET_ALERT_Metadata_CompetencyUpdate_Stored, sEventId, eOriginalData, oCompetence);
        }
        else
        {
            MetadataXMLCreator.CreateXMLNewItemResponse(MetadataConstants.c_ET_ALERT_Metadata_CompetencyNew_Stored, sEventId, eOriginalData, oCompetence);
        }
    }
    
    /** 
     * @summary Method for saving new identity
     * @startRealisation Sasa Stojanovic 04.02.2012.
     * @finalModification Sasa Stojanovic 04.02.2012.
     * @param sEventId - event id
     * @param oIdentities - identity object
     */
    static void SaveObjectNewIdentity(String sEventId, Element eOriginalData, Identity[] oIdentities)
    {
        oIdentities = MetadataRDFConverter.SaveIdentity(oIdentities, false);
        
        //transfering from list to object with a list
        MetadataGlobal.MetadataObjectExt oIdentitiesExt = new MetadataGlobal.MetadataObjectExt();
        oIdentitiesExt.m_sReturnConfig = "YY#o:" + MetadataConstants.c_XMLE_identities + "/";
        oIdentitiesExt.m_oObjects = oIdentities;
        
        MetadataXMLCreator.CreateXMLNewItemResponse(MetadataConstants.c_ET_ALERT_Metadata_IdentityNew_Stored, sEventId, eOriginalData, oIdentitiesExt);
    }
    
    /** 
     * @summary Method for updating identity
     * @startRealisation Sasa Stojanovic 04.02.2012.
     * @finalModification Sasa Stojanovic 04.02.2012.
     * @param sEventId - event id
     * @param oIdentities - identity object
     */
    static void UpdateObjectIdentity(String sEventId, Element eOriginalData, Identity[] oIdentities)
    {
        oIdentities = MetadataRDFConverter.SaveIdentity(oIdentities, true);
        
        //transfering from list to object with a list
        MetadataGlobal.MetadataObjectExt oIdentitiesExt = new MetadataGlobal.MetadataObjectExt();
        oIdentitiesExt.m_sReturnConfig = "YY#o:" + MetadataConstants.c_XMLE_identities + "/";
        oIdentitiesExt.m_oObjects = oIdentities;
        
        MetadataXMLCreator.CreateXMLNewItemResponse(MetadataConstants.c_ET_ALERT_Metadata_IdentityUpdate_Stored, sEventId, eOriginalData, oIdentitiesExt);
    }
    
    /** 
     * @summary Method for removing identity
     * @startRealisation Sasa Stojanovic 04.02.2012.
     * @finalModification Sasa Stojanovic 04.02.2012.
     * @param sEventId - event id
     * @param oIdentities - identity object
     */
    static void RemoveObjectIdentity(String sEventId, Element eOriginalData, Identity[] oIdentities)
    {
        oIdentities = MetadataRDFConverter.SaveIdentity(oIdentities, false);
        
        //transfering from list to object with a list
        MetadataGlobal.MetadataObjectExt oIdentitiesExt = new MetadataGlobal.MetadataObjectExt();
        oIdentitiesExt.m_sReturnConfig = "YY#o:" + MetadataConstants.c_XMLE_identities + "/";
        oIdentitiesExt.m_oObjects = oIdentities;
        
        MetadataXMLCreator.CreateXMLNewItemResponse(MetadataConstants.c_ET_ALERT_Metadata_IdentityRemove_Stored, sEventId, eOriginalData, oIdentitiesExt);
    }
    
    /** 
     * @summary Method for saving new concept
     * @startRealisation Sasa Stojanovic 17.05.2012.
     * @finalModification Sasa Stojanovic 17.05.2012.
     * @param sEventId - event id
     * @param oConcepts - concepts object
     */
    static void SaveObjectNewConcept(String sEventId, Element eOriginalData, Concept[] oConcepts)
    {
        oConcepts = MetadataRDFConverter.SaveConcept(oConcepts);
        
        //transfering from list to object with a list
        MetadataGlobal.MetadataObjectExt oConceptsExt = new MetadataGlobal.MetadataObjectExt();
        oConceptsExt.m_sReturnConfig = "YY#o:" + MetadataConstants.c_XMLE_mdservice + "/o:" + MetadataConstants.c_XMLE_concepts + "/";
        oConceptsExt.m_oObjects = oConcepts;
        
        MetadataXMLCreator.CreateXMLNewItemResponse(MetadataConstants.c_ET_ALERT_Metadata_ConceptNew_Stored, sEventId, eOriginalData, oConceptsExt);
    }
    
    /**
     * @summary API Call Method for executing custom SPARQL query
     * @startRealisation Sasa Stojanovic 15.12.2011.
     * @finalModification Sasa Stojanovic 15.12.2011.
     * @param sEventId - event id
     * @param sSPARQL - sparql query
     * @param sOntModelSpec - specification of ontology model
     */
    static void ac_sparql(String sEventId, String sSPARQL, String sOntModelSpec)
    {
        OntModelSpec oOntModelSpec = OntModelSpec.OWL_MEM;
        
        if (sOntModelSpec.equalsIgnoreCase("OWL_MEM_MICRO_RULE_INF"))
            oOntModelSpec = OntModelSpec.OWL_MEM_MICRO_RULE_INF;
        if (sOntModelSpec.equalsIgnoreCase("OWL_MEM_MINI_RULE_INF"))
            oOntModelSpec = OntModelSpec.OWL_MEM_MINI_RULE_INF;
        if (sOntModelSpec.equalsIgnoreCase("OWL_MEM_RDFS_INF"))
            oOntModelSpec = OntModelSpec.OWL_MEM_RDFS_INF;
        if (sOntModelSpec.equalsIgnoreCase("OWL_MEM_RULE_INF"))
            oOntModelSpec = OntModelSpec.OWL_MEM_RULE_INF;
        if (sOntModelSpec.equalsIgnoreCase("OWL_MEM_TRANS_INF"))
            oOntModelSpec = OntModelSpec.OWL_MEM_TRANS_INF;
                
        MetadataGlobal.APIResponseData oData = MetadataRDFConverter.ac_sparql(sSPARQL, oOntModelSpec);       
        MetadataXMLCreator.CreateXMLAPIResponse(MetadataConstants.c_XMLAC_sparql, sEventId, oData);
    }
    
    /**
     * @summary API Call Method for getting all issues for product
     * @startRealisation Sasa Stojanovic 13.12.2011.
     * @finalModification Sasa Stojanovic 13.12.2011.
     * @param sEventId - event id
     * @param sProductID - product id
     * @param dtmFromDate - date from filter
     */
    static void ac_issue_getAllForProduct(String sEventId, String sProductID, Date dtmFromDate)
    {
        MetadataGlobal.APIResponseData oData = MetadataRDFConverter.ac_issue_getAllForProduct(sProductID, dtmFromDate);       
        MetadataXMLCreator.CreateXMLAPIResponse(MetadataConstants.c_XMLAC_issue_getAllForProduct, sEventId, oData);
    }
    
    /**
     * @summary API Call Method for getting method for person
     * @startRealisation Sasa Stojanovic 18.01.2012.
     * @finalModification Sasa Stojanovic 23.05.2012.
     * @param sEventId - event id
     * @param sIdentityId - identity id
     */
    static void ac_issue_getAllForIdentity(String sEventId, String sIdentityId)
    {
        MetadataGlobal.APIResponseData oData = MetadataRDFConverter.ac_issue_getAllForIdentity(sIdentityId);       
        MetadataXMLCreator.CreateXMLAPIResponse(MetadataConstants.c_XMLAC_issue_getAllForIdentity, sEventId, oData);
    }
    
    /**
     * @summary API Call Method for getting all issues for method
     * @startRealisation Sasa Stojanovic 14.12.2011.
     * @finalModification Sasa Stojanovic 14.12.2011.
     * @param sEventId - event id
     * @param sMethodUri - methods' URIs
     */
    static void ac_issue_getAllForMethod(String sEventId, ArrayList <String> sMethodUri)
    {
        MetadataGlobal.APIResponseData oData = MetadataRDFConverter.ac_issue_getAllForMethod(sMethodUri);       
        MetadataXMLCreator.CreateXMLAPIResponse(MetadataConstants.c_XMLAC_issue_getAllForMethod, sEventId, oData);
    }
    
    /**
     * @summary API Call Method for getting issue annotation status
     * @startRealisation Sasa Stojanovic 14.12.2011.
     * @finalModification Sasa Stojanovic 14.12.2011.
     * @param sEventId - event id
     * @param sIssueUri - issue uri
     */
    static void ac_issue_getAnnotationStatus(String sEventId, String sIssueUri)
    {
        MetadataGlobal.APIResponseData oData = MetadataRDFConverter.ac_issue_getAnnotationStatus(sIssueUri);       
        MetadataXMLCreator.CreateXMLAPIResponse(MetadataConstants.c_XMLAC_issue_getAnnotationStatus, sEventId, oData);
    }
    
    /**
     * @summary API Call Method for getting issue info
     * @startRealisation Sasa Stojanovic 14.12.2011.
     * @finalModification Sasa Stojanovic 14.12.2011.
     * @param sEventId - event id
     * @param sIssueID - issue id
     */
    static void ac_issue_getInfo(String sEventId, String sIssueID)
    {
        MetadataGlobal.APIResponseData oData = MetadataRDFConverter.ac_issue_getInfo(sIssueID);       
        MetadataXMLCreator.CreateXMLAPIResponse(MetadataConstants.c_XMLAC_issue_getInfo, sEventId, oData);
    }
    
    /**
     * @summary API Call Method for getting duplicates for issue
     * @startRealisation Sasa Stojanovic 15.12.2011.
     * @finalModification Sasa Stojanovic 15.12.2011.
     * @param sEventId - event id
     * @param sIssueUri - issue uri
     */
    static void ac_issue_getExplicitDuplicates(String sEventId, String sIssueUri)
    {
        MetadataGlobal.APIResponseData oData = MetadataRDFConverter.ac_issue_getExplicitDuplicates(sIssueUri);       
        MetadataXMLCreator.CreateXMLAPIResponse(MetadataConstants.c_XMLAC_issue_getExplicitDuplicates, sEventId, oData);
    }
    
    /**
     * @summary API Call Method for getting subject for issue.
     * @startRealisation  Dejan Milosavljevic 22.02.2012.
     * @finalModification Dejan Milosavljevic 22.02.2012.
     * @param sEventId - event id
     * @param sIssueUri - issue uri
     */
    static void ac_issue_getSubjectAreas(String sEventId, String sIssueUri)
    {
        MetadataGlobal.APIResponseData oData = MetadataRDFConverter.ac_issue_getSubjectAreas(sIssueUri);       
        MetadataXMLCreator.CreateXMLAPIResponse(MetadataConstants.c_XMLAC_issue_getSubjectAreas, sEventId, oData);
    }
    
    /**
     * @summary API Call Method for getting subjects for all open issues for product.
     * @startRealisation  Dejan Milosavljevic 22.02.2012.
     * @finalModification Dejan Milosavljevic 22.02.2012.
     * @param sEventId - event id
     * @param sProductUri - product uri
     */
    static void ac_issue_getSubjectAreasForOpen(String sEventId, String sProductUri)
    {
        MetadataGlobal.APIResponseData oData = MetadataRDFConverter.ac_issue_getSubjectAreasForOpen(sProductUri);       
        MetadataXMLCreator.CreateXMLAPIResponse(MetadataConstants.c_XMLAC_issue_getSubjectAreasForOpen, sEventId, oData);
    }
    
    /**
     * @summary API Call Method for getting subjects for all open issues for product.
     * @startRealisation  Sasa Stojanovic 12.03.2012.
     * @finalModification Sasa Stojanovic 12.03.2012.
     * @param sEventId - event id
     * @param sProductID - product id
     */
    static void ac_issue_getOpen(String sEventId, String sProductID)
    {
        MetadataGlobal.APIResponseData oData = MetadataRDFConverter.ac_issue_getOpen(sProductID);       
        MetadataXMLCreator.CreateXMLAPIResponse(MetadataConstants.c_XMLAC_issue_getOpen, sEventId, oData);
    }
    
    /**
     * @summary API Call Method for getting person info
     * @startRealisation Sasa Stojanovic 18.01.2012.
     * @finalModification Sasa Stojanovic 18.01.2012.
     * @param sEventId - event id
     * @param sPersonUri - person uri
     */
    static void ac_person_getInfo(String sEventId, String sPersonUri)
    {
        MetadataGlobal.APIResponseData oData = MetadataRDFConverter.ac_person_getInfo(sPersonUri);       
        MetadataXMLCreator.CreateXMLAPIResponse(MetadataConstants.c_XMLAC_person_getInfo, sEventId, oData);
    }
    
    /**
     * @summary API Call Method for getting persons for email
     * @startRealisation Sasa Stojanovic 18.01.2012.
     * @finalModification Sasa Stojanovic 18.01.2012.
     * @param sEventId - event id
     * @param eMail - person email
     */
    static void ac_person_getAllForEmail(String sEventId, String eMail)
    {
        MetadataGlobal.APIResponseData oData = MetadataRDFConverter.ac_person_getAllForEmail(eMail);       
        MetadataXMLCreator.CreateXMLAPIResponse(MetadataConstants.c_XMLAC_person_getAllForEmail, sEventId, oData);
    }
    
    /**
     * @summary API Call Method for getting identity for provided person information
     * @startRealisation Sasa Stojanovic 18.01.2012.
     * @finalModification Sasa Stojanovic 25.09.2012.
     * @param sEventId - event id
     * @param sPersonUri - person uri
     */
    static void ac_identity_getForPerson(String sEventId, String sPersonUri)
    {
        MetadataGlobal.APIResponseData oData = MetadataRDFConverter.ac_identity_getForPerson(sPersonUri);
        MetadataXMLCreator.CreateXMLAPIResponse(MetadataConstants.c_XMLAC_identity_getForPerson, sEventId, oData);
    }

    /**
     * @summary API Call Method for getting competency for person
     * @startRealisation  Dejan Milosavljevic 04.02.2012.
     * @finalModification Dejan Milosavljevic 04.02.2012.
     * @param sEventId - event id
     * @param sPersonUri - person uri
     */
    static void ac_competency_getForPerson(String sEventId, String sPersonUri)
    {
        MetadataGlobal.APIResponseData oData = MetadataRDFConverter.ac_competency_getForPerson(sPersonUri);       
        MetadataXMLCreator.CreateXMLAPIResponse(MetadataConstants.c_XMLAC_competency_getForPerson, sEventId, oData);
    }
    
    /**
     * @summary API Call Method for getting person for issue
     * @startRealisation Sasa Stojanovic 18.01.2012.
     * @finalModification Sasa Stojanovic 18.01.2012.
     * @param sEventId - event id
     * @param sPersonForIssueSPARQL - SPARQL for person for issue
     */
    static void ac_competency_getPersonForIssue(String sEventId, String sPersonForIssueSPARQL)
    {
        MetadataGlobal.APIResponseData oData = MetadataRDFConverter.ac_competency_getPersonForIssue(sPersonForIssueSPARQL);       
        MetadataXMLCreator.CreateXMLAPIResponse(MetadataConstants.c_XMLAC_competency_getPersonForIssue, sEventId, oData);
    }
    
    /**
     * @summary API Call Method for getting method for person
     * @startRealisation Sasa Stojanovic 18.01.2012.
     * @finalModification Sasa Stojanovic 23.05.2012.
     * @param sEventId - event id
     * @param sIdentityId - identity id
     */
    static void ac_method_getAllForIdentity(String sEventId, String sIdentityId)
    {
        MetadataGlobal.APIResponseData oData = MetadataRDFConverter.ac_method_getAllForIdentity(sIdentityId);       
        MetadataXMLCreator.CreateXMLAPIResponse(MetadataConstants.c_XMLAC_method_getAllForIdentity, sEventId, oData);
    }
    
    
    /**
     * @summary API Call Method for getting method for person and product
     * @startRealisation Sasa Stojanovic 18.01.2012.
     * @finalModification Sasa Stojanovic 18.01.2012.
     * @param sEventId - event id
     * @param sPersonUri - person uri
     * @param sProductUri - product uri
     */
    static void ac_method_getRelatedCode(String sEventId, String sPersonUri, String sProductUri)
    {
        MetadataGlobal.APIResponseData oData = MetadataRDFConverter.ac_method_getRelatedCode(sPersonUri, sProductUri);       
        MetadataXMLCreator.CreateXMLAPIResponse(MetadataConstants.c_XMLAC_method_getRelatedCode, sEventId, oData);
    }
    
    /**
     * @summary API Call Method for getting issues related to keyword
     * @startRealisation Sasa Stojanovic 17.01.2012.
     * @finalModification Sasa Stojanovic 17.01.2012.
     * @param sEventId - event id
     * @param sKeyword - keyword
     */
    static void ac_issue_getRelatedToKeyword(String sEventId, String sKeyword)
    {
        MetadataGlobal.APIResponseData oData = MetadataRDFConverter.ac_issue_getRelatedToKeyword(sKeyword);       
        MetadataXMLCreator.CreateXMLAPIResponse(MetadataConstants.c_XMLAC_issue_getRelatedToKeyword, sEventId, oData);
    }
    
    /**
     * @summary API Call Method for getting commits related to keyword
     * @startRealisation Sasa Stojanovic 17.01.2012.
     * @finalModification Sasa Stojanovic 17.01.2012.
     * @param sEventId - event id
     * @param sKeyword - keyword
     */
    static void ac_commit_getRelatedToKeyword(String sEventId, String sKeyword)
    {
        MetadataGlobal.APIResponseData oData = MetadataRDFConverter.ac_commit_getRelatedToKeyword(sKeyword);       
        MetadataXMLCreator.CreateXMLAPIResponse(MetadataConstants.c_XMLAC_commit_getRelatedToKeyword, sEventId, oData);
    }
    
    /**
     * @summary API Call Method for getting all commits for product
     * @startRealisation  Dejan Milosavljevic 15.03.2012.
     * @finalModification Dejan Milosavljevic 15.03.2012.
     * @param sEventId - event id
     * @param sProductID - product id
     * @param dtmFromDate - date from filter
     */
    static void ac_commit_getAllForProduct(String sEventId, String sProductID, Date dtmFromDate)
    {
        MetadataGlobal.APIResponseData oData = MetadataRDFConverter.ac_commit_getAllForProduct(sProductID, dtmFromDate);       
        MetadataXMLCreator.CreateXMLAPIResponse(MetadataConstants.c_XMLAC_commit_getAllForProduct, sEventId, oData);
    }
    
    /**
     * @summary API Call Method for getting commit info
     * @startRealisation Sasa Stojanovic 18.05.2012.
     * @finalModification Sasa Stojanovic 18.05.2012.
     * @param sEventId - event id
     * @param sCommitUri - issue id
     */
    static void ac_commit_getInfo(String sEventId, String sCommitUri)
    {
        MetadataGlobal.APIResponseData oData = MetadataRDFConverter.ac_commit_getInfo(sCommitUri);       
        MetadataXMLCreator.CreateXMLAPIResponse(MetadataConstants.c_XMLAC_commit_getInfo, sEventId, oData);
    }
    
    /**
     * @summary API Call Method for getting all mails for product
     * @startRealisation  Sasa Stojanovic 06.04.2012.
     * @finalModification Sasa Stojanovic 06.04.2012.
     * @param sEventId - event id
     * @param dtmFromDate - date from filter
     */
    static void ac_mail_getAllForProduct(String sEventId, Date dtmFromDate)
    {
        MetadataGlobal.APIResponseData oData = MetadataRDFConverter.ac_mail_getAllForProduct(dtmFromDate);       
        MetadataXMLCreator.CreateXMLAPIResponse(MetadataConstants.c_XMLAC_mail_getAllForProduct, sEventId, oData);
    }
    
    /**
     * @summary API Call Method for getting all forum posts for product
     * @startRealisation  Sasa Stojanovic 06.04.2012.
     * @finalModification Sasa Stojanovic 06.04.2012.
     * @param sEventId - event id
     * @param dtmFromDate - date from filter
     */
    static void ac_forumPost_getAllForProduct(String sEventId, Date dtmFromDate)
    {
        MetadataGlobal.APIResponseData oData = MetadataRDFConverter.ac_forumPost_getAllForProduct(dtmFromDate);       
        MetadataXMLCreator.CreateXMLAPIResponse(MetadataConstants.c_XMLAC_mail_getAllForProduct, sEventId, oData);
    }
    
    /**
     * @summary API Call Method for getting all file data
     * @startRealisation  Dejan Milosavljevic 23.02.2012.
     * @finalModification Dejan Milosavljevic 23.02.2012.
     * @param sEventId - event id
     * @param sOffset - offset for the file list
     * @param sCount - number of files from ofset position
     */
    static void ac_file_getAll(String sEventId, String sOffset, String sCount)
    {
        MetadataGlobal.APIResponseData oData = MetadataRDFConverter.ac_file_getAll(sOffset, sCount);       
        MetadataXMLCreator.CreateXMLAPIResponse(MetadataConstants.c_XMLAC_file_getAll, sEventId, oData);
    }
    
    /**
     * @summary API Call Method for getting emails related to keyword
     * @startRealisation Sasa Stojanovic 17.01.2012.
     * @finalModification Sasa Stojanovic 17.01.2012.
     * @param sEventId - event id
     * @param sKeyword - keyword
     */
    static void ac_email_getRelatedToKeyword(String sEventId, String sKeyword)
    {
        MetadataGlobal.APIResponseData oData = MetadataRDFConverter.ac_email_getRelatedToKeyword(sKeyword);       
        MetadataXMLCreator.CreateXMLAPIResponse(MetadataConstants.c_XMLAC_email_getRelatedToKeyword, sEventId, oData);
    }
    
    /**
     * @summary API Call Method for getting posts related to keyword
     * @startRealisation Sasa Stojanovic 17.01.2012.
     * @finalModification Sasa Stojanovic 17.01.2012.
     * @param sEventId - event id
     * @param sKeyword - keyword
     */
    static void ac_post_getRelatedToKeyword(String sEventId, String sKeyword)
    {
        MetadataGlobal.APIResponseData oData = MetadataRDFConverter.ac_post_getRelatedToKeyword(sKeyword);       
        MetadataXMLCreator.CreateXMLAPIResponse(MetadataConstants.c_XMLAC_post_getRelatedToKeyword, sEventId, oData);
    }
    
    /**
     * @summary API Call Method for getting wiki pages related to keyword
     * @startRealisation Sasa Stojanovic 17.01.2012.
     * @finalModification Sasa Stojanovic 17.01.2012.
     * @param sEventId - event id
     * @param sKeyword - keyword
     */
    static void ac_wiki_getRelatedToKeyword(String sEventId, String sKeyword)
    {
        MetadataGlobal.APIResponseData oData = MetadataRDFConverter.ac_wiki_getRelatedToKeyword(sKeyword);       
        MetadataXMLCreator.CreateXMLAPIResponse(MetadataConstants.c_XMLAC_wiki_getRelatedToKeyword, sEventId, oData);
    }
    
    /**
     * @summary API Call Method for getting issues related to issue
     * @startRealisation Sasa Stojanovic 17.01.2012.
     * @finalModification Sasa Stojanovic 17.01.2012.
     * @param sEventId - event id
     * @param sIssueUri - keyword
     */
    static void ac_issue_getRelatedToIssue(String sEventId, String sIssueUri)
    {
        MetadataGlobal.APIResponseData oData = MetadataRDFConverter.ac_issue_getRelatedToIssue(sIssueUri);       
        MetadataXMLCreator.CreateXMLAPIResponse(MetadataConstants.c_XMLAC_issue_getRelatedToIssue, sEventId, oData);
    }
    
    /**
     * @summary API Call Method for getting commits related to issue
     * @startRealisation Sasa Stojanovic 17.01.2012.
     * @finalModification Sasa Stojanovic 17.01.2012.
     * @param sEventId - event id
     * @param sIssueUri - keyword
     */
    static void ac_commit_getRelatedToIssue(String sEventId, String sIssueUri)
    {
        MetadataGlobal.APIResponseData oData = MetadataRDFConverter.ac_commit_getRelatedToIssue(sIssueUri);       
        MetadataXMLCreator.CreateXMLAPIResponse(MetadataConstants.c_XMLAC_commit_getRelatedToIssue, sEventId, oData);
    }
    
    /**
     * @summary API Call Method for getting emails related to issue
     * @startRealisation Sasa Stojanovic 17.01.2012.
     * @finalModification Sasa Stojanovic 17.01.2012.
     * @param sEventId - event id
     * @param sIssueUri - keyword
     */
    static void ac_email_getRelatedToIssue(String sEventId, String sIssueUri)
    {
        MetadataGlobal.APIResponseData oData = MetadataRDFConverter.ac_email_getRelatedToIssue(sIssueUri);       
        MetadataXMLCreator.CreateXMLAPIResponse(MetadataConstants.c_XMLAC_email_getRelatedToIssue, sEventId, oData);
    }
    
    /**
     * @summary API Call Method for getting posts related to issue
     * @startRealisation Sasa Stojanovic 17.01.2012.
     * @finalModification Sasa Stojanovic 17.01.2012.
     * @param sEventId - event id
     * @param sIssueUri - keyword
     */
    static void ac_post_getRelatedToIssue(String sEventId, String sIssueUri)
    {
        MetadataGlobal.APIResponseData oData = MetadataRDFConverter.ac_post_getRelatedToIssue(sIssueUri);       
        MetadataXMLCreator.CreateXMLAPIResponse(MetadataConstants.c_XMLAC_post_getRelatedToIssue, sEventId, oData);
    }
    
    /**
     * @summary API Call Method for getting wiki pages related to issue
     * @startRealisation Sasa Stojanovic 17.01.2012.
     * @finalModification Sasa Stojanovic 17.01.2012.
     * @param sEventId - event id
     * @param sIssueUri - keyword
     */
    static void ac_wiki_getRelatedToIssue(String sEventId, String sIssueUri)
    {
        MetadataGlobal.APIResponseData oData = MetadataRDFConverter.ac_wiki_getRelatedToIssue(sIssueUri);       
        MetadataXMLCreator.CreateXMLAPIResponse(MetadataConstants.c_XMLAC_wiki_getRelatedToIssue, sEventId, oData);
    }
    
    /**
     * @summary API Call Method for getting all for concept (issue, person...).
     * @startRealisation  Dejan Milosavljevic 21.02.2012.
     * @finalModification Dejan Milosavljevic 21.02.2012.
     * @param sEventId - event id.
     * @param sConceptUri - concept uri.
     */
    static void ac_instance_getAllForConcept(String sEventId, ArrayList <String> sConceptUri)
    {
        MetadataGlobal.APIResponseData oData = MetadataRDFConverter.ac_instance_getAllForConcept(sConceptUri);       
        MetadataXMLCreator.CreateXMLAPIResponse(MetadataConstants.c_XMLAC_instance_getAllForConcept, sEventId, oData);
    }
    
    /**
     * @summary Method for getting instance
     * @startRealisation Sasa Stojanovic 06.09.2011.
     * @finalModification Sasa Stojanovic 06.09.2011.
     * @param sEventId - event id
     * @param sEventName - name of event
     * @param sOntClass - class url
     */
    static void GetInstance(String sEventId, String sEventName, String sInstanceURL)
    {
        MetadataGlobal.OntoProperty oProjeprty = MetadataRDFConverter.GetMember(sInstanceURL);
        MetadataXMLCreator.CreateXMLInstanceResponse(sEventId, sEventName, oProjeprty);
    }
}
