/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MetadataCore;

import MetadataObjects.*;
import com.hp.hpl.jena.ontology.OntModelSpec;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.net.URI;
import java.util.ArrayList;

/**
 *
 * @author ivano
 */
public class MetadataModel {


    public void LoadObject()
    {
        
    }
    
    public static void SaveObject()
    {
        
    }
    
   
    /** 
     * @summary Method for performing search for given IDS
     * @startRealisation Sasa Stojanovic 24.06.2011.
     * @finalModification Sasa Stojanovic 24.06.2011.
     * @param sSearchType - type of search
     * @param sIDs - list of IDs
     */
    public static void SearchForIDs(String sSearchType, ArrayList<String> arIDs) {
        try
        {
            MetadataRDFConverter.SearchForIDs(sSearchType, arIDs);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    

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
     * @startRealisation Sasa Stojanovic 31.08.2011.
     * @finalModification Sasa Stojanovic 31.08.2011.
     * @param sEventId - event id
     * @param oIssue - issue object
     */
    static void SaveObjectNewIssue(String sEventId, Issue oIssue) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException
    {
        oIssue = MetadataRDFConverter.SaveIssue(oIssue);
        MetadataXMLCreator.CreateXMLNewItemResponse(MetadataConstants.c_ET_issue_replyNewUpdate, sEventId, oIssue);
    }
    
    /** 
     * @summary Method for saving new commit
     * @startRealisation Sasa Stojanovic 16.01.2012.
     * @finalModification Sasa Stojanovic 16.01.2012.
     * @param sEventId - event id
     * @param oCommit - issue object
     */
    static void SaveObjectNewCommit(String sEventId, Commit oCommit) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException
    {
        oCommit = MetadataRDFConverter.SaveCommit(oCommit);
        MetadataXMLCreator.CreateXMLNewItemResponse(MetadataConstants.c_ET_commit_replyNew, sEventId, oCommit);
    }
    
    /**
     * @summary Method for saving new person
     * @startRealisation Sasa Stojanovic 05.09.2011.
     * @finalModification Sasa Stojanovic 05.09.2011.
     * @param sEventId - event id
     * @param oPerson - person object
     */
    static void SaveObjectNewPerson(String sEventId, foaf_Person oPerson)
    {
        oPerson = MetadataRDFConverter.SavePerson(oPerson);
        MetadataXMLCreator.CreateXMLNewItemResponse(MetadataConstants.c_ET_person_replyNewUpdate, sEventId, oPerson);
    }
    
    /**
     * @summary API Call Method for getting all individuals
     * @startRealisation Sasa Stojanovic 15.12.2011.
     * @finalModification Sasa Stojanovic 15.12.2011.
     * @param sEventId - event id
     * @param sSPARQL - sparql query
     * @param sOntModelSpec - specification of ontology model
     */
    static void ac_sparql(String sEventId, String sSPARQL, String sOntModelSpec)
    {
        OntModelSpec oOntModelSpec = OntModelSpec.OWL_MEM;
        
        if (sOntModelSpec.equals("OWL_MEM_MICRO_RULE_INF"))
            oOntModelSpec = OntModelSpec.OWL_MEM_MICRO_RULE_INF;
        if (sOntModelSpec.equals("OWL_MEM_MINI_RULE_INF"))
            oOntModelSpec = OntModelSpec.OWL_MEM_MINI_RULE_INF;
        if (sOntModelSpec.equals("OWL_MEM_RDFS_INF"))
            oOntModelSpec = OntModelSpec.OWL_MEM_RDFS_INF;
        if (sOntModelSpec.equals("OWL_MEM_RULE_INF"))
            oOntModelSpec = OntModelSpec.OWL_MEM_RULE_INF;
        if (sOntModelSpec.equals("OWL_MEM_TRANS_INF"))
            oOntModelSpec = OntModelSpec.OWL_MEM_TRANS_INF;
                
        MetadataGlobal.APIResponseData oData = MetadataRDFConverter.ac_sparql(sSPARQL, oOntModelSpec);       
        MetadataXMLCreator.CreateXMLAPIResponse(MetadataConstants.c_XMLAC_sparql, sEventId, oData);
    }
    
    /**
     * @summary API Call Method for getting all individuals
     * @startRealisation Sasa Stojanovic 13.12.2011.
     * @finalModification Sasa Stojanovic 13.12.2011.
     * @param sEventId - event id
     * @param sProductUri - product URI
     */
    static void ac_issue_getAllForProduct(String sEventId, String sProductUri)
    {
        MetadataGlobal.APIResponseData oData = MetadataRDFConverter.ac_issue_getAllForProduct(sProductUri);       
        MetadataXMLCreator.CreateXMLAPIResponse(MetadataConstants.c_XMLAC_issue_getAllForProduct, sEventId, oData);
    }
    
    /**
     * @summary API Call Method for getting all individuals
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
     * @summary API Call Method for getting all individuals
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
     * @summary API Call Method for getting all individuals
     * @startRealisation Sasa Stojanovic 14.12.2011.
     * @finalModification Sasa Stojanovic 14.12.2011.
     * @param sEventId - event id
     * @param sIssueUri - issue uri
     */
    static void ac_issue_getInfo(String sEventId, String sIssueUri)
    {
        MetadataGlobal.APIResponseData oData = MetadataRDFConverter.ac_issue_getInfo(sIssueUri);       
        MetadataXMLCreator.CreateXMLAPIResponse(MetadataConstants.c_XMLAC_issue_getInfo, sEventId, oData);
    }
    
    /**
     * @summary API Call Method for getting all individuals
     * @startRealisation Sasa Stojanovic 15.12.2011.
     * @finalModification Sasa Stojanovic 15.12.2011.
     * @param sEventId - event id
     * @param sIssueDuplicatesSPARQL - SPARQL for issue duplicates
     */
    static void ac_issue_getDuplicates(String sEventId, String sIssueDuplicatesSPARQL)
    {
        MetadataGlobal.APIResponseData oData = MetadataRDFConverter.ac_issue_getDuplicates(sIssueDuplicatesSPARQL);       
        MetadataXMLCreator.CreateXMLAPIResponse(MetadataConstants.c_XMLAC_issue_getDuplicates, sEventId, oData);
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
        //MetadataGlobal.APIResponseData oData = MetadataRDFConverter.ac_commit_getRelatedToIssue(sIssueUri);       
        //MetadataXMLCreator.CreateXMLAPIResponse(MetadataConstants.c_XMLAC_commit_getRelatedToIssue, sEventId, oData);
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
        //MetadataGlobal.APIResponseData oData = MetadataRDFConverter.ac_email_getRelatedToIssue(sIssueUri);       
        //MetadataXMLCreator.CreateXMLAPIResponse(MetadataConstants.c_XMLAC_email_getRelatedToIssue, sEventId, oData);
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
        //MetadataGlobal.APIResponseData oData = MetadataRDFConverter.ac_post_getRelatedToIssue(sIssueUri);       
        //MetadataXMLCreator.CreateXMLAPIResponse(MetadataConstants.c_XMLAC_post_getRelatedToIssue, sEventId, oData);
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
        //MetadataGlobal.APIResponseData oData = MetadataRDFConverter.ac_wiki_getRelatedToIssue(sIssueUri);       
        //MetadataXMLCreator.CreateXMLAPIResponse(MetadataConstants.c_XMLAC_wiki_getRelatedToIssue, sEventId, oData);
    }
    
    /**
     * @summary Method for getting instance
     * @startRealisation Sasa Stojanovic 06.09.2011.
     * @finalModification Sasa Stojanovic 06.09.2011.
     * @param sEventId - event id
     * @param sOntClass - class url
     */
    static void GetInstance(String sEventId, String sInstanceURL)
    {
        MetadataGlobal.OntoProperty oProjeprty = MetadataRDFConverter.GetMember(sInstanceURL);
        MetadataXMLCreator.CreateXMLInstanceResponse(sEventId, oProjeprty);
    }
}
