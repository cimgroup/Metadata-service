/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MetadataCore;

import MetadataObjects.Bug;
import MetadataObjects.Issue;
import MetadataObjects.MetadataPerson;
import MetadataObjects.foaf_Person;
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
     * @summary Method for saving new bug
     * @startRealisation Sasa Stojanovic 24.06.2011.
     * @finalModification Sasa Stojanovic 24.06.2011.
     * @param sID - bug ID
     * @param uriUri - bug URI
     * @param sStatusDescription - bug resolution
     * @param sIsAboutDescription - related topic
     * @param iIsAboutImportance - bug priority
     * @param sIsAboutSeverity - bug severity
     */
    public static void SaveObjectNewBug(String sID, String sName, URI uriURI, String sStatusDescription, String sIsAboutDescription, int iIsAboutImportance, String sIsAboutSeverity) {
        try
        {
            //Issue oBug = MetadataObjectFactory.CreateNewIssue();
            //oBug.m_sID = sID;
            //oBug.m_sName = sName;
            //oBug.m_sObjectURI = uriURI;
            //oBug.m_sResolution = sStatusDescription;
            //oBug.m_sTopic = sIsAboutDescription;
            //oBug.m_iProirity = iIsAboutImportance;
            //oBug.m_sSeverity = sIsAboutSeverity;
            //MetadataRDFConverter.SaveNewDataInOWL(oBug);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
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
     * @summary Method for performing search for given IDS
     * @startRealisation Sasa Stojanovic 24.06.2011.
     * @finalModification Sasa Stojanovic 24.06.2011.
     * @param sSearchType - type of search
     * @param arCollection - list of IDs
     */
//    public static void ReturnForIDs(String sSearchType, ArrayList<ArrayList <String>> arCollection)
//    {
//        try
//        {
//            ArrayList arResult = new ArrayList();
//            for (int i = 0; i < arCollection.size(); i++)
//            {
//                ArrayList arItem = new ArrayList();
//                Issue oBug = MetadataObjectFactory.CreateNewIssue();
//                oBug.m_sID = arCollection.get(i).get(0);
//                arItem.add(oBug);
//                
//                for (int j = 1; j < arCollection.get(i).size(); j++)
//                {
//                    foaf_Person oPerson = MetadataObjectFactory.CreateNewPerson();
//                    oPerson.m_sID = arCollection.get(i).get(j);
//                    arItem.add(oPerson);
//                }
//                
//                arResult.add(arItem);
//            }
//            
//            MetadataXMLCreator.CreateXMLSearch(sSearchType, arResult);
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//    }
    
//    /** 
//     * @summary Method for creating API response
//     * @startRealisation Sasa Stojanovic 31.08.2011.
//     * @finalModification Sasa Stojanovic 31.08.2011.
//     */
//    public static void CreateAPIResponse(String sAPICall, String sEventId, ArrayList<String> arResult)
//    {
//        sEventName = "allPersonIndividuals";
//        sEventId = "9378";
//        
//        ArrayList arItem = new ArrayList();
//        arItem.add("http://alert-project.com/Persons#Person1");
//        arItem.add("http://alert-project.com/Persons#Person2");
//        arItem.add("http://alert-project.com/Persons#Person3");
//        arItem.add("http://alert-project.com/Persons#Person4");
//        
//        MetadataXMLCreator.CreateXMLAPIResponse(sAPICall, sEventId, arItem);
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
     * @summary Method for saving new bug
     * @startRealisation Sasa Stojanovic 31.08.2011.
     * @finalModification Sasa Stojanovic 31.08.2011.
     * @param sEventId - event id
     * @param oPerson - bug object
     */
    static void SaveObjectNewIssue(String sEventId, Issue oIssue) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException
    {
        oIssue = MetadataRDFConverter.SaveIssue(oIssue);
        MetadataXMLCreator.CreateXMLNewItemResponse(MetadataConstants.c_ET_newIssueResponse, sEventId, oIssue);
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
        MetadataXMLCreator.CreateXMLNewItemResponse(MetadataConstants.c_ET_newPersonResponse, sEventId, oPerson);
    }
    
    /**
     * @summary API Call Method for getting all individuals
     * @startRealisation Sasa Stojanovic 06.09.2011.
     * @finalModification Sasa Stojanovic 06.09.2011.
     * @param sEventId - event id
     * @param sOntClass - class url
     */
    static void ac_GetAllIndividuals(String sEventId, String sOntClass)
    {
        MetadataGlobal.APIResponseData oData = MetadataRDFConverter.GetAllMembers(sOntClass);       
        MetadataXMLCreator.CreateXMLAPIResponse(sEventId, oData);
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
