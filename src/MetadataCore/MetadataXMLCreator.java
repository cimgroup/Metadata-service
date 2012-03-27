/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MetadataCore;

import MetadataCore.MetadataGlobal.APIResponseData;
import MetadataCore.MetadataGlobal.OntoProperty;
import MetadataObjects.Bug;
import MetadataObjects.MetadataPerson;
import java.lang.reflect.Array;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;
import java.lang.reflect.Field;

/**
 *
 * @author ivano
 */
public class MetadataXMLCreator {

//    public void CreateXML()
//    {
//        
//    }
    
//    /** 
//     * @summary Method for creating XML for search results
//     * @startRealisation Sasa Stojanovic 24.06.2011.
//     * @finalModification Sasa Stojanovic 24.06.2011.
//     * @param sSearchType - type of search
//     * @param arResult - list of objects
//     */
//    public static void CreateXMLSearch(String sSearchType, ArrayList<ArrayList> arResult)
//    {
//        try
//        {
//                    
//            DocumentBuilderFactory dbfFactory = DocumentBuilderFactory.newInstance();
//            dbfFactory.setNamespaceAware(true);
//            DocumentBuilder dbBuilder = dbfFactory.newDocumentBuilder();
//            Document dDoc = dbBuilder.newDocument();
//            
//            //XML root
//            Element eEvent = dDoc.createElement(MetadataConstants.c_XMLE_event);
//            eEvent.setAttribute("xmlns", "http://www.alert-project.eu/");
//            eEvent.setAttribute("xmlns:onto", "http://www.alert-project.eu/ontologie");
//            dDoc.appendChild(eEvent);
//            Comment cEvent1 = dDoc.createComment(" common information about an event ");
//            eEvent.appendChild(cEvent1);
//            
//            //head element
//            Element eHead = dDoc.createElement(MetadataConstants.c_XMLE_head);
//            eEvent.appendChild(eHead);
//            Comment cHead1 = dDoc.createComment(" string value ");
//            eHead.appendChild(cHead1);
//            Element eTimeStamp = dDoc.createElement(MetadataConstants.c_XMLE_timestamp);
//            eHead.appendChild(eTimeStamp);
//            Comment cHead2 = dDoc.createComment(" by which alert component ");
//            eHead.appendChild(cHead2);
//            Element eSendBy = dDoc.createElement(MetadataConstants.c_XMLE_sentby);
//            eHead.appendChild(eSendBy);
//            
//            Comment cEvent2 = dDoc.createComment(" put the related information into the payload section ");
//            eEvent.appendChild(cEvent2);
//            
//            //payload element
//            Element ePayload = dDoc.createElement(MetadataConstants.c_XMLE_payload);
//            eEvent.appendChild(ePayload);
//            Comment cPayLoad = dDoc.createComment(" query for related issues according to annotated information ");
//            ePayload.appendChild(cPayLoad);
//            
//            //eventType element
//            Element eEventType = dDoc.createElement(MetadataConstants.c_XMLE_eventType);
//            ePayload.appendChild(eEventType);
//            
//            Element eName;
//            Element eTypeOf;
//            Element eOntoProperty;
//            Element eOntoProperty2;
//            Element eOntoProperty3;
//            Element eClass;
//            Element eClass2;
//            Element eValue;
//            
//            Text tText;
//            
//            //name
//            eName = dDoc.createElement(MetadataConstants.c_XMLE_name);
//            eEventType.appendChild(eName);
//            tText = dDoc.createTextNode(sSearchType);
//            eName.appendChild(tText);
//            
//            //typeOf
//            eTypeOf = dDoc.createElement(MetadataConstants.c_XMLE_typeOf);
//            eEventType.appendChild(eTypeOf);
//            tText = dDoc.createTextNode("http://#" + sSearchType);
//            eTypeOf.appendChild(tText);
//            
//            for (int i = 0; i < arResult.size(); i++)
//            {
//                //onto:property
//                eOntoProperty = dDoc.createElement(MetadataConstants.c_XMLE_ontoProperty);
//                eEventType.appendChild(eOntoProperty);
//                
//                    //name
//                    eName = dDoc.createElement(MetadataConstants.c_XMLE_name);
//                    eOntoProperty.appendChild(eName);
//                    tText = dDoc.createTextNode(MetadataConstants.c_XMLE_isRelatedTo);
//                    eName.appendChild(tText);
//
//                    //typeOf
//                    eTypeOf = dDoc.createElement(MetadataConstants.c_XMLE_typeOf);
//                    eOntoProperty.appendChild(eTypeOf);
//                    tText = dDoc.createTextNode(MetadataConstants.c_XMLE_BugType);
//                    eTypeOf.appendChild(tText);
//
//                    //class
//                    eClass = dDoc.createElement(MetadataConstants.c_XMLE_class);
//                    eOntoProperty.appendChild(eClass);
//
//                        //value
//                        eValue = dDoc.createElement(MetadataConstants.c_XMLE_value);
//                        eClass.appendChild(eValue);
//
//                        //onto:property
//                        eOntoProperty2 = dDoc.createElement(MetadataConstants.c_XMLE_ontoProperty);
//                        eClass.appendChild(eOntoProperty2);
//                        
//                            //name
//                            eName = dDoc.createElement(MetadataConstants.c_XMLE_name);
//                            eOntoProperty2.appendChild(eName);
//                            tText = dDoc.createTextNode(MetadataConstants.c_XMLE_NewBugEvent_hasID);
//                            eName.appendChild(tText);
//                            
//                            //value
//                            eValue = dDoc.createElement(MetadataConstants.c_XMLE_value);
//                            eOntoProperty2.appendChild(eValue);
//                            Bug oBug = (Bug) arResult.get(i).get(0);
//                            tText = dDoc.createTextNode(oBug.m_sID);
//                            eValue.appendChild(tText);
//                            
//                            //typeOf
//                            eTypeOf = dDoc.createElement(MetadataConstants.c_XMLE_typeOf);
//                            eOntoProperty2.appendChild(eTypeOf);
//                            tText = dDoc.createTextNode(MetadataConstants.c_XMLE_StringType);
//                            eTypeOf.appendChild(tText);
//                            
//                        //onto:property
//                        eOntoProperty2 = dDoc.createElement(MetadataConstants.c_XMLE_ontoProperty);
//                        eClass.appendChild(eOntoProperty2);
//                        
//                            //name
//                            eName = dDoc.createElement(MetadataConstants.c_XMLE_name);
//                            eOntoProperty2.appendChild(eName);
//                            tText = dDoc.createTextNode(MetadataConstants.c_XMLE_NewBugEvent_hasCommiter);
//                            eName.appendChild(tText);
//                                                        
//                            //typeOf
//                            eTypeOf = dDoc.createElement(MetadataConstants.c_XMLE_typeOf);
//                            eOntoProperty2.appendChild(eTypeOf);
//                            tText = dDoc.createTextNode(MetadataConstants.c_XMLE_StringType);
//                            eTypeOf.appendChild(tText);
//                            
//                            //class
//                            eClass2 = dDoc.createElement(MetadataConstants.c_XMLE_class);
//                            eOntoProperty2.appendChild(eClass2);
//                            
//                                //value
//                                eValue = dDoc.createElement(MetadataConstants.c_XMLE_value);
//                                eClass2.appendChild(eValue);
//                                
//                                for (int j = 1; j < arResult.get(i).size(); j++)
//                                {
//                                    //onto:property
//                                    eOntoProperty3 = dDoc.createElement(MetadataConstants.c_XMLE_ontoProperty);
//                                    eClass2.appendChild(eOntoProperty3);
//
//                                        //name
//                                        eName = dDoc.createElement(MetadataConstants.c_XMLE_name);
//                                        eOntoProperty3.appendChild(eName);
//                                        tText = dDoc.createTextNode(MetadataConstants.c_XMLE_NewBugEvent_hasID);
//                                        eName.appendChild(tText);
//
//                                        //value
//                                        eValue = dDoc.createElement(MetadataConstants.c_XMLE_value);
//                                        eOntoProperty3.appendChild(eValue);
//                                        MetadataPerson oPerson = (MetadataPerson) arResult.get(i).get(j);
//                                        tText = dDoc.createTextNode(oPerson.m_sID);
//                                        eValue.appendChild(tText);
//
//                                        //typeOf
//                                        eTypeOf = dDoc.createElement(MetadataConstants.c_XMLE_typeOf);
//                                        eOntoProperty3.appendChild(eTypeOf);
//                                        tText = dDoc.createTextNode(MetadataConstants.c_XMLE_StringType);
//                                        eTypeOf.appendChild(tText);
//                                }
//            }
//            
//            //Send created XML document
//            MetadataCommunicator.SendXML(dDoc);
//            
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//    }
    
    /** 
     * @summary Method for creating XML for API response
     * @startRealisation  Sasa Stojanovic     31.08.2011.
     * @finalModification Dejan Milosavljevic 05.03.2012.
     * @param sEventName - name of event
     * @param iEventID - id of event
     * @param arResult - list of objects
     */
    public static Document CreateXMLAPIResponse(String sAPICall, String sEventId, MetadataGlobal.APIResponseData oData)
    {
        try
        {
                    
            DocumentBuilderFactory dbfFactory = DocumentBuilderFactory.newInstance();
            dbfFactory.setNamespaceAware(true);
            DocumentBuilder dbBuilder = dbfFactory.newDocumentBuilder();
            Document dDoc = dbBuilder.newDocument();
            
            dDoc.setXmlVersion("1.0");
            
            Element eWSTN = CreateWSNTStructure(dDoc);
            
            //XML root
            Element eEvent = dDoc.createElement("ns1:" + MetadataConstants.c_XMLE_event);
            eEvent.setAttribute("xmlns:ns1", "http://www.alert-project.eu/");
            eEvent.setAttribute("xmlns:o", "http://www.alert-project.eu/ontoevents-mdservice");
            eEvent.setAttribute("xmlns:r", "http://www.alert-project.eu/rawevents-forum");
            eEvent.setAttribute("xmlns:r1", "http://www.alert-project.eu/rawevents-mailinglist");
            eEvent.setAttribute("xmlns:r2", "http://www.alert-project.eu/rawevents-wiki");
            eEvent.setAttribute("xmlns:s", "http://www.alert-project.eu/strevents-kesi");
            eEvent.setAttribute("xmlns:s1", "http://www.alert-project.eu/strevents-keui");
            eEvent.setAttribute("xmlns:s2", "http://www.alert-project.eu/APIcall-request");
            eEvent.setAttribute("xmlns:s3", "http://www.alert-project.eu/APIcall-response");
            eEvent.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
            eEvent.setAttribute("xsi:schemaLocation", "http://www.alert-project.eu/alert-root.xsd");
            eWSTN.appendChild(eEvent);
            
            Text tText;
            
            //head element
            Element eHead = dDoc.createElement("ns1:" + MetadataConstants.c_XMLE_head);
            eEvent.appendChild(eHead);
            
                //sender
                Element eSender = dDoc.createElement("ns1:" + MetadataConstants.c_XMLE_sender);
                eHead.appendChild(eSender);
                tText = dDoc.createTextNode(MetadataConstants.c_XMLV_metadataservice);
                eSender.appendChild(tText);

                //timestamp
                Element eTimeStamp = dDoc.createElement("ns1:" + MetadataConstants.c_XMLE_timestamp);
                eHead.appendChild(eTimeStamp);
                tText = dDoc.createTextNode("10000");
                eTimeStamp.appendChild(tText);

                //sequencenumber
                Element eSequenceNumber = dDoc.createElement("ns1:" + MetadataConstants.c_XMLE_sequencenumber);
                eHead.appendChild(eSequenceNumber);
                tText = dDoc.createTextNode("1");
                eSequenceNumber.appendChild(tText);
            
           
            //payload element
            Element ePayload = dDoc.createElement("ns1:" + MetadataConstants.c_XMLE_payload);
            eEvent.appendChild(ePayload);
            
                //meta element
                Element eMeta = dDoc.createElement("ns1:" + MetadataConstants.c_XMLE_meta);
                ePayload.appendChild(eMeta);
                
                    //startTime
                    Element eStartTime = dDoc.createElement("ns1:" + MetadataConstants.c_XMLE_startTime);
                    eMeta.appendChild(eStartTime);
                    tText = dDoc.createTextNode("10010");
                    eStartTime.appendChild(tText);

                    //endTime
                    Element eEndTime = dDoc.createElement("ns1:" + MetadataConstants.c_XMLE_endTime);
                    eMeta.appendChild(eEndTime);
                    tText = dDoc.createTextNode("10020");
                    eEndTime.appendChild(tText);

                    //eventName
                    Element eEventName = dDoc.createElement("ns1:" + MetadataConstants.c_XMLE_eventName);
                    eMeta.appendChild(eEventName);
                    tText = dDoc.createTextNode(MetadataConstants.c_ET_ALERT_Metadata_APICallResponse);
                    eEventName.appendChild(tText);
                    
                    //eventId
                    Element eEventId = dDoc.createElement("ns1:" + MetadataConstants.c_XMLE_eventId);
                    eMeta.appendChild(eEventId);
                    tText = dDoc.createTextNode(sEventId);
                    eEventId.appendChild(tText);
                    
                    //eventType
                    Element eEventType = dDoc.createElement("ns1:" + MetadataConstants.c_XMLE_eventType);
                    eMeta.appendChild(eEventType);
                    tText = dDoc.createTextNode(MetadataConstants.c_XMLV_reply);
                    eEventType.appendChild(tText);
                    
                    
                //eventData element
                Element eEventData = dDoc.createElement("ns1:" + MetadataConstants.c_XMLE_eventData);
                ePayload.appendChild(eEventData);
                    
                    //apiresponse
                    Element eAPIResponse = dDoc.createElement("ns1:" + MetadataConstants.c_XMLE_apiResponse);
                    eEventData.appendChild(eAPIResponse);
                    
                        //apicall
                        Element eAPICall = dDoc.createElement("s3:" + MetadataConstants.c_XMLE_apiCall);
                        eAPIResponse.appendChild(eAPICall);
                        tText = dDoc.createTextNode(sAPICall);
                        eAPICall.appendChild(tText);
                                                    
                        //responseData
                        Element eResponseData = dDoc.createElement("s3:" + MetadataConstants.c_XMLE_responseData);
                        eAPIResponse.appendChild(eResponseData);
                        
                        CreateAPIResponseStructure(dDoc, eResponseData, oData);

            //Send created XML document
            MetadataCommunicator.SendXML(dDoc, MetadataConstants.c_ET_ALERT_Metadata_APICallResponse);
            
            return dDoc;
            
        }
        catch (Exception e)
        {
            return null;
        }
    }
    
    /** 
     * @summary Method for creating API Response structure
     * @startRealisation  Sasa Stojanovic     01.11.2011.
     * @finalModification Dejan Milosavljevic 05.03.2012.
     * @param dDoc - XML document
     * @param eElement - current XML element
     * @param oData - object with data
     */
    private static void CreateAPIResponseStructure(Document dDoc, Element eElement, APIResponseData oData)
    {
        try
        {
            if (oData.sReturnConfig != null && oData.sReturnConfig.length() > 0)
            {
                while (oData.sReturnConfig.indexOf("/") != -1)
                {
                    String sElementName = oData.sReturnConfig.substring(0, oData.sReturnConfig.indexOf("/"));
                    if (sElementName.length() > 0)
                    {
                        Element eSubElement = dDoc.createElement(sElementName);     //create a subelement
                        eElement.appendChild(eSubElement);      //add it to element
                        eElement = eSubElement;             //subelement is now element
                    }
                    oData.sReturnConfig = oData.sReturnConfig.substring(oData.sReturnConfig.indexOf("/") + 1);
                }

                if (oData.sReturnConfig.length() > 0) //if there is tag for data
                {
                    Element eValueElement = dDoc.createElement(oData.sReturnConfig);      //create a element for URI
                    eElement.appendChild(eValueElement);                                //add it to current element
                    if (oData.sData.startsWith("<![CDATA[", 0)) //Dejan Milosavljevic 05.03.2012.
                    {
                        CDATASection cdValue = dDoc.createCDATASection(oData.sData.substring(9, oData.sData.length() - 3));
                        eValueElement.appendChild(cdValue);
                    }
                    else
                    {
                        Text tValue = dDoc.createTextNode(oData.sData.toString());        //create text with value
                        eValueElement.appendChild(tValue);                              //add text to element
                    }
                }
            }
            
            for (int i = 0; i < oData.oData.size(); i++)
            {
                CreateAPIResponseStructure(dDoc, eElement, oData.oData.get(i)); //create structure for object
            }
        }
        catch (Exception e)
        {
        }
    }
    
    /** 
     * @summary Method for creating XML for API response
     * @startRealisation  Sasa Stojanovic     31.08.2011.
     * @finalModification Dejan Milosavljevic 05.03.2012.
     * @param sEventName - name of event
     * @param iEventID - id of event
     * @param arResult - list of objects
     */
    public static void CreateXMLNewItemKeuiResponse(String sEventName, String sEventId, ArrayList<String> arResult)
    {
        try
        {
                    
            DocumentBuilderFactory dbfFactory = DocumentBuilderFactory.newInstance();
            dbfFactory.setNamespaceAware(true);
            DocumentBuilder dbBuilder = dbfFactory.newDocumentBuilder();
            Document dDoc = dbBuilder.newDocument();
            
            Element eWSTN = CreateWSNTStructure(dDoc);
            
            //XML root
            Element eEvent = dDoc.createElement("ns1:" + MetadataConstants.c_XMLE_event);
            eEvent.setAttribute("xmlns:ns1", "http://www.alert-project.eu/");
            eEvent.setAttribute("xmlns:o", "http://www.alert-project.eu/ontoevents-mdservice");
            eEvent.setAttribute("xmlns:r", "http://www.alert-project.eu/rawevents-forum");
            eEvent.setAttribute("xmlns:r1", "http://www.alert-project.eu/rawevents-mailinglist");
            eEvent.setAttribute("xmlns:r2", "http://www.alert-project.eu/rawevents-wiki");
            eEvent.setAttribute("xmlns:s", "http://www.alert-project.eu/strevents-kesi");
            eEvent.setAttribute("xmlns:s1", "http://www.alert-project.eu/strevents-keui");
            eEvent.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
            eEvent.setAttribute("xsi:schemaLocation", "http://www.alert-project.eu/alert-root.xsd");
            eWSTN.appendChild(eEvent);
            
            Text tText;
            
            //head element
            Element eHead = dDoc.createElement("ns1:" + MetadataConstants.c_XMLE_head);
            eEvent.appendChild(eHead);
            
                //sender
                Element eSender = dDoc.createElement("ns1:" + MetadataConstants.c_XMLE_sender);
                eHead.appendChild(eSender);
                tText = dDoc.createTextNode(MetadataConstants.c_XMLV_metadataservice);
                eSender.appendChild(tText);

                //timestamp
                Element eTimeStamp = dDoc.createElement("ns1:" + MetadataConstants.c_XMLE_timestamp);
                eHead.appendChild(eTimeStamp);
                tText = dDoc.createTextNode("10003");
                eTimeStamp.appendChild(tText);

                //sequencenumber
                Element eSequenceNumber = dDoc.createElement("ns1:" + MetadataConstants.c_XMLE_sequencenumber);
                eHead.appendChild(eSequenceNumber);
                tText = dDoc.createTextNode("1");
                eSequenceNumber.appendChild(tText);
            
           
            //payload element
            Element ePayload = dDoc.createElement("ns1:" + MetadataConstants.c_XMLE_payload);
            eEvent.appendChild(ePayload);
            
                //meta element
                Element eMeta = dDoc.createElement("ns1:" + MetadataConstants.c_XMLE_meta);
                ePayload.appendChild(eMeta);
                
                    //startTime
                    Element eStartTime = dDoc.createElement("ns1:" + MetadataConstants.c_XMLE_startTime);
                    eMeta.appendChild(eStartTime);
                    tText = dDoc.createTextNode("10010");
                    eStartTime.appendChild(tText);

                    //endTime
                    Element eEndTime = dDoc.createElement("ns1:" + MetadataConstants.c_XMLE_endTime);
                    eMeta.appendChild(eEndTime);
                    tText = dDoc.createTextNode("10020");
                    eEndTime.appendChild(tText);

                    //eventName
                    Element eEventName = dDoc.createElement("ns1:" + MetadataConstants.c_XMLE_eventName);
                    eMeta.appendChild(eEventName);
                    tText = dDoc.createTextNode(sEventName);
                    eEventName.appendChild(tText);
                    
                    //eventId
                    Element eEventId = dDoc.createElement("ns1:" + MetadataConstants.c_XMLE_eventId);
                    eMeta.appendChild(eEventId);
                    tText = dDoc.createTextNode(sEventId);
                    eEventId.appendChild(tText);
                    
                    //eventType
                    Element eEventType = dDoc.createElement("ns1:" + MetadataConstants.c_XMLE_eventType);
                    eMeta.appendChild(eEventType);
                    tText = dDoc.createTextNode(MetadataConstants.c_XMLV_reply);
                    eEventType.appendChild(tText);
                    
                    
                //eventData element
                Element eEventData = dDoc.createElement("ns1:" + MetadataConstants.c_XMLE_eventData);
                ePayload.appendChild(eEventData);
                    
                    //keui
                    Element eKeui = dDoc.createElement("ns1:" + MetadataConstants.c_XMLE_keui);
                    eEventData.appendChild(eKeui);
                    
                        //rawTextualData
                        Element eRawTextualData = dDoc.createElement("s1:" + MetadataConstants.c_XMLE_rawTextualData);
                        eKeui.appendChild(eRawTextualData);
                        
                            //relatedOntologyRef
                            Element eRelatedOntologyRef = dDoc.createElement("s1:" + MetadataConstants.c_XMLE_relatedOntologyRef);
                            eRawTextualData.appendChild(eRelatedOntologyRef);
                            tText = dDoc.createTextNode(arResult.get(0).toString());
                            eRelatedOntologyRef.appendChild(tText);
                            
                            //content
                            Element eContent = dDoc.createElement("s1:" + MetadataConstants.c_XMLE_content);
                            eRawTextualData.appendChild(eContent);
                            tText = dDoc.createTextNode(arResult.get(1).toString());
                            eContent.appendChild(tText);
                            

            //Send created XML document
            MetadataCommunicator.SendXML(dDoc, sEventName);
            
        }
        catch (Exception e)
        {
        }
    }
    
//    static void CreateXMLNewItemResponse(String sEventName, String sEventId, String sObjectURI)
//    {
//        try
//        {
//                    
//            DocumentBuilderFactory dbfFactory = DocumentBuilderFactory.newInstance();
//            dbfFactory.setNamespaceAware(true);
//            DocumentBuilder dbBuilder = dbfFactory.newDocumentBuilder();
//            Document dDoc = dbBuilder.newDocument();
//            
//            //XML root
//            Element eEvent = dDoc.createElement("ns1:" + MetadataConstants.c_XMLE_event);
//            eEvent.setAttribute("xmlns:ns1", "http://www.alert-project.eu/");
//            eEvent.setAttribute("xmlns:o", "http://www.alert-project.eu/ontoevents-mdservice");
//            eEvent.setAttribute("xmlns:r", "http://www.alert-project.eu/rawevents-forum");
//            eEvent.setAttribute("xmlns:r1", "http://www.alert-project.eu/rawevents-mailinglist");
//            eEvent.setAttribute("xmlns:r2", "http://www.alert-project.eu/rawevents-wiki");
//            eEvent.setAttribute("xmlns:s", "http://www.alert-project.eu/strevents-kesi");
//            eEvent.setAttribute("xmlns:s1", "http://www.alert-project.eu/strevents-keui");
//            eEvent.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
//            eEvent.setAttribute("xsi:schemaLocation", "http://www.alert-project.eu/alert-root.xsd");
//            dDoc.appendChild(eEvent);
//            
//            Text tText;
//            
//            //head element
//            Element eHead = dDoc.createElement("ns1:" + MetadataConstants.c_XMLE_head);
//            eEvent.appendChild(eHead);
//            
//                //sender
//                Element eSender = dDoc.createElement("ns1:" + MetadataConstants.c_XMLE_sender);
//                eHead.appendChild(eSender);
//                tText = dDoc.createTextNode(MetadataConstants.c_XMLV_metadataservice);
//                eSender.appendChild(tText);
//
//                //timestamp
//                Element eTimeStamp = dDoc.createElement("ns1:" + MetadataConstants.c_XMLE_timestamp);
//                eHead.appendChild(eTimeStamp);
//                tText = dDoc.createTextNode("10003");
//                eTimeStamp.appendChild(tText);
//
//                //sequencenumber
//                Element eSequenceNumber = dDoc.createElement("ns1:" + MetadataConstants.c_XMLE_sequencenumber);
//                eHead.appendChild(eSequenceNumber);
//                tText = dDoc.createTextNode("1");
//                eSequenceNumber.appendChild(tText);
//            
//            //payload element
//            Element ePayload = dDoc.createElement("ns1:" + MetadataConstants.c_XMLE_payload);
//            eEvent.appendChild(ePayload);
//            
//                //meta element
//                Element eMeta = dDoc.createElement("ns1:" + MetadataConstants.c_XMLE_meta);
//                ePayload.appendChild(eMeta);
//                
//                    //startTime
//                    Element eStartTime = dDoc.createElement("ns1:" + MetadataConstants.c_XMLE_startTime);
//                    eMeta.appendChild(eStartTime);
//                    tText = dDoc.createTextNode("10010");
//                    eStartTime.appendChild(tText);
//
//                    //endTime
//                    Element eEndTime = dDoc.createElement("ns1:" + MetadataConstants.c_XMLE_endTime);
//                    eMeta.appendChild(eEndTime);
//                    tText = dDoc.createTextNode("10020");
//                    eEndTime.appendChild(tText);
//
//                    //eventName
//                    Element eEventName = dDoc.createElement("ns1:" + MetadataConstants.c_XMLE_eventName);
//                    eMeta.appendChild(eEventName);
//                    tText = dDoc.createTextNode(sEventName);
//                    eEventName.appendChild(tText);
//                    
//                    //eventId
//                    Element eEventId = dDoc.createElement("ns1:" + MetadataConstants.c_XMLE_eventId);
//                    eMeta.appendChild(eEventId);
//                    tText = dDoc.createTextNode(sEventId + "Response");
//                    eEventId.appendChild(tText);
//                    
//                //eventData element
//                Element eEventData = dDoc.createElement("ns1:" + MetadataConstants.c_XMLE_eventData);
//                ePayload.appendChild(eEventData);
//                    
//                    //elatedOntologyRef
//                    Element eRelatedOntologyRef = dDoc.createElement("ns1:" + MetadataConstants.c_XMLE_relatedOntologyRef);
//                    eEventData.appendChild(eRelatedOntologyRef);
//                    tText = dDoc.createTextNode(sObjectURI);
//                    eRelatedOntologyRef.appendChild(tText);
//
//            //Send created XML document
//            MetadataCommunicator.SendXML(dDoc);
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//    }
    
    /** 
     * @summary Method for creating XML for new item response
     * @startRealisation  Sasa Stojanovic     01.11.2011.
     * @finalModification Dejan Milosavljevic 05.03.2012.
     * @param sEventName - name of event
     * @param iEventID - id of event
     * @param oObject - new item object
     */
    public static Document CreateXMLNewItemResponse(String sEventName, String sEventId, Element eOriginalData, Object oObject)
    {
        try
        {
                    
            DocumentBuilderFactory dbfFactory = DocumentBuilderFactory.newInstance();
            dbfFactory.setNamespaceAware(true);
            DocumentBuilder dbBuilder = dbfFactory.newDocumentBuilder();
            Document dDoc = dbBuilder.newDocument();
                       
            Element eWSTN = CreateWSNTStructure(dDoc);
            
            //XML root
            Element eEvent = dDoc.createElement("ns1:" + MetadataConstants.c_XMLE_event);
            eEvent.setAttribute("xmlns:ns1", "http://www.alert-project.eu/");
            eEvent.setAttribute("xmlns:o", "http://www.alert-project.eu/ontoevents-mdservice");
            eEvent.setAttribute("xmlns:r", "http://www.alert-project.eu/rawevents-forum");
            eEvent.setAttribute("xmlns:r1", "http://www.alert-project.eu/rawevents-mailinglist");
            eEvent.setAttribute("xmlns:r2", "http://www.alert-project.eu/rawevents-wiki");
            eEvent.setAttribute("xmlns:s", "http://www.alert-project.eu/strevents-kesi");
            eEvent.setAttribute("xmlns:s1", "http://www.alert-project.eu/strevents-keui");
            eEvent.setAttribute("xmlns:sm", "http://www.alert-project.eu/stardom");
            eEvent.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
            eEvent.setAttribute("xsi:schemaLocation", "http://www.alert-project.eu/alert-root.xsd");
            eWSTN.appendChild(eEvent);
            
            Text tText;
            
            //head element
            Element eHead = dDoc.createElement("ns1:" + MetadataConstants.c_XMLE_head);
            eEvent.appendChild(eHead);
            
                //sender
                Element eSender = dDoc.createElement("ns1:" + MetadataConstants.c_XMLE_sender);
                eHead.appendChild(eSender);
                tText = dDoc.createTextNode(MetadataConstants.c_XMLV_metadataservice);
                eSender.appendChild(tText);

                //timestamp
                Element eTimeStamp = dDoc.createElement("ns1:" + MetadataConstants.c_XMLE_timestamp);
                eHead.appendChild(eTimeStamp);
                tText = dDoc.createTextNode("10003");
                eTimeStamp.appendChild(tText);

                //sequencenumber
                Element eSequenceNumber = dDoc.createElement("ns1:" + MetadataConstants.c_XMLE_sequencenumber);
                eHead.appendChild(eSequenceNumber);
                tText = dDoc.createTextNode("1");
                eSequenceNumber.appendChild(tText);
            
            //payload element
            Element ePayload = dDoc.createElement("ns1:" + MetadataConstants.c_XMLE_payload);
            eEvent.appendChild(ePayload);
            
                //meta element
                Element eMeta = dDoc.createElement("ns1:" + MetadataConstants.c_XMLE_meta);
                ePayload.appendChild(eMeta);
                
                    //startTime
                    Element eStartTime = dDoc.createElement("ns1:" + MetadataConstants.c_XMLE_startTime);
                    eMeta.appendChild(eStartTime);
                    tText = dDoc.createTextNode("10010");
                    eStartTime.appendChild(tText);

                    //endTime
                    Element eEndTime = dDoc.createElement("ns1:" + MetadataConstants.c_XMLE_endTime);
                    eMeta.appendChild(eEndTime);
                    tText = dDoc.createTextNode("10020");
                    eEndTime.appendChild(tText);

                    //eventName
                    Element eEventName = dDoc.createElement("ns1:" + MetadataConstants.c_XMLE_eventName);
                    eMeta.appendChild(eEventName);
                    tText = dDoc.createTextNode(sEventName);
                    eEventName.appendChild(tText);
                    
                    //eventId
                    Element eEventId = dDoc.createElement("ns1:" + MetadataConstants.c_XMLE_eventId);
                    eMeta.appendChild(eEventId);
                    tText = dDoc.createTextNode(sEventId);
                    eEventId.appendChild(tText);
                    
                    //eventType
                    Element eEventType = dDoc.createElement("ns1:" + MetadataConstants.c_XMLE_eventType);
                    eMeta.appendChild(eEventType);
                    tText = dDoc.createTextNode(MetadataConstants.c_XMLV_reply);
                    eEventType.appendChild(tText);
                    
                //eventData element
                Element eEventData = dDoc.createElement("ns1:" + MetadataConstants.c_XMLE_eventData);
                ePayload.appendChild(eEventData);
                    
                    //original data
                    Element eOriginalDataImported = (Element) dDoc.importNode(eOriginalData, true);
                    eEventData.appendChild(eOriginalDataImported);
                    
                    CreateNewItemResponseStructure(dDoc, eEventData, oObject);

            //Send created XML document
            MetadataCommunicator.SendXML(dDoc, sEventName);
            
            return dDoc;
        }
        catch (Exception e)
        {
            return null;
        }
    }
    
    /** 
     * @summary Method for creating XML for new annotation response.
     * @startRealisation  Dejan Milosavljevic 01.02.2012.
     * @finalModification Dejan Milosavljevic 05.03.2012.
     * @param dDoc - original XML document.
     * @param sEventName - name of event.
     */
    public static Document CreateXMLNewAnnotationResponse(Document dDoc, String sEventName)
    {
        try
        {
            //Find ns1:sender tag
            NodeList nlSender = dDoc.getElementsByTagName("ns1:" + MetadataConstants.c_XMLE_sender);
            
            //Find ns1:eventName tag
            NodeList nlEventName = dDoc.getElementsByTagName("ns1:" + MetadataConstants.c_XMLE_eventName);
            
            //Find ns1:eventType tag
            NodeList nlEventType = dDoc.getElementsByTagName("ns1:" + MetadataConstants.c_XMLE_eventType);
            
            if (nlSender != null && nlSender.getLength() > 0 &&
                nlEventName != null && nlSender.getLength() > 0 &&
                nlEventType != null && nlSender.getLength() > 0)
            {
                Element eSender = (Element) nlSender.item(0);
                eSender.setTextContent(MetadataConstants.c_XMLV_metadataservice);
                
                Element eEventName = (Element) nlEventName.item(0);
                eEventName.setTextContent(sEventName);
                
                Element eEventType = (Element) nlEventType.item(0);
                eEventType.setTextContent(MetadataConstants.c_XMLV_reply);
                
                //Send created XML document
                MetadataCommunicator.SendXML(dDoc, sEventName);                
                return dDoc;
            }
            else
            {
                return null;
            }
        }
        catch (Exception e)
        {
            return null;
        }
    }

    /** 
     * @summary Method for creating XML structure
     * @startRealisation Sasa Stojanovic 01.11.2011.
     * @finalModification Sasa Stojanovic 01.11.2011.
     * @param dDoc - XML document
     * @param eElement - current XML element
     * @param oObject - object with data
     */
    private static void CreateNewItemResponseStructure(Document dDoc, Element eElement, Object oObject)
    {
        try
        {
            //sReturnConfig YY#issue/issueUri
            //(Y/N - create structure and insert values, Y/N - create structure for object members, issue/issueUri - XML structure)
            String sReturnConfig = (String) oObject.getClass().getField("m_sReturnConfig").get(oObject);
            if (sReturnConfig.length() > 3)
            {
                Boolean bCreate = sReturnConfig.substring(0, 1).equals("Y");
                Boolean bContinue = sReturnConfig.substring(1, 2).equals("Y");
                String sLocation = sReturnConfig.substring(3);  //get structure for XML

                if (bCreate)    //create structure and insert value
                {
                    while (sLocation.indexOf("/") != -1)
                    {
                        String sElementName = sLocation.substring(0, sLocation.indexOf("/"));
                        Element eSubElement = dDoc.createElement(sElementName);     //create a subelement
                        eElement.appendChild(eSubElement);      //add it to element
                        eElement = eSubElement;             //subelement is now element
                        sLocation = sLocation.substring(sLocation.indexOf("/") + 1);
                    }

                    if (!sLocation.isEmpty())
                    {
                        String sObjectURI = (String) oObject.getClass().getField("m_sObjectURI").get(oObject);  //get object ontology URI value
                        Element eValueElement = dDoc.createElement(sLocation);      //create a element for URI
                        eElement.appendChild(eValueElement);                    //add it to current element
                        Text tValue = dDoc.createTextNode(sObjectURI);      //create text with value
                        eValueElement.appendChild(tValue);              //add text to element
                    }
                }

                if (bContinue)      //create structure for object members
                {
                    Field[] fMembers = oObject.getClass().getFields();  //get all fields for object
                    for (int i = 0; i < fMembers.length; i++)
                    {
                        Object oMember = fMembers[i].get(oObject);  //get field value
                        try
                        {
                            if(oMember.getClass().isArray())        //if it's array
                            {
                                for (int j = 0; j < Array.getLength(oMember); j++)
                                {
                                    Object oSubMember = Array.get(oMember, j);              //get each object
                                    CreateNewItemResponseStructure(dDoc, eElement, oSubMember);    //create stucture of each object
                                }
                            }
                            else
                            {
                                CreateNewItemResponseStructure(dDoc, eElement, oMember);   //create structure for object
                            }
                        }
                        catch (Exception e)
                        {
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
        }
    }
    
    /** 
     * @summary Method for creating WSNT structure
     * @startRealisation Sasa Stojanovic 05.12.2011.
     * @finalModification Sasa Stojanovic 05.12.2011.
     * @param dDoc - XML document
     * @return message element
     */
    private static Element CreateWSNTStructure(Document dDoc) {
        try
        {
            Element eEnvelope = dDoc.createElement("soap:Envelope");
            eEnvelope.setAttribute("xmlns:soap", "http://www.w3.org/2003/05/soap-envelope");
            eEnvelope.setAttribute("xmlns:wsnt", "http://docs.oasis-open.org/wsn/b-2");
            eEnvelope.setAttribute("xmlns:wsa", "http://www.w3.org/2005/08/addressing");
            dDoc.appendChild(eEnvelope);
            
            Text tText;
            
            //header element
            Element eHeader = dDoc.createElement("soap:Header");
            eEnvelope.appendChild(eHeader);
            
            //body element
            Element eBody = dDoc.createElement("soap:Body");
            eEnvelope.appendChild(eBody);
            
                //notify element
                Element eNotify = dDoc.createElement("wsnt:Notify");
                eBody.appendChild(eNotify);
                    
                    //notification message element
                    Element eNotificationMessage = dDoc.createElement("wsnt:NotificationMessage");
                    eNotify.appendChild(eNotificationMessage);
                        
                        //topic element
                        Element eTopic = dDoc.createElement("wsnt:Topic");
                        eNotificationMessage.appendChild(eTopic);
                        
                        //producer reference element
                        Element eProducerReference = dDoc.createElement("wsnt:ProducerReference");
                        eNotificationMessage.appendChild(eProducerReference);
                            
                            //address element
                            Element eAddress = dDoc.createElement("wsa:Address");
                            eProducerReference.appendChild(eAddress);
                            tText = dDoc.createTextNode(MetadataConstants.c_XMLV_metadataserviceaddress);
                            eAddress.appendChild(tText);

                        //message element
                        Element eMessage = dDoc.createElement("wsnt:Message");
                        eNotificationMessage.appendChild(eMessage);
                        
            return eMessage;
                    
        }
        catch (Exception e)
        {
            return null;
        }
    }
    
    /** 
     * @summary Method for creating XML for API response
     * @startRealisation  Sasa Stojanovic     31.08.2011.
     * @finalModification Dejan Milosavljevic 05.03.2012.
     * @param sEventId - id of event
     * @param sEventName - name of event
     * @param oProperty - ontology property
     */
    public static void CreateXMLInstanceResponse(String sEventId, String sEventName, MetadataGlobal.OntoProperty oProperty)
    {
        try
        {
                    
            DocumentBuilderFactory dbfFactory = DocumentBuilderFactory.newInstance();
            dbfFactory.setNamespaceAware(true);
            DocumentBuilder dbBuilder = dbfFactory.newDocumentBuilder();
            Document dDoc = dbBuilder.newDocument();
            
            //XML root
            Element eEvent = dDoc.createElement("ns1:" + MetadataConstants.c_XMLE_event);
            eEvent.setAttribute("xmlns:ns1", "http://www.alert-project.eu/");
            eEvent.setAttribute("xmlns:o", "http://www.alert-project.eu/ontoevents-mdservice");
            eEvent.setAttribute("xmlns:r", "http://www.alert-project.eu/rawevents-forum");
            eEvent.setAttribute("xmlns:r1", "http://www.alert-project.eu/rawevents-mailinglist");
            eEvent.setAttribute("xmlns:r2", "http://www.alert-project.eu/rawevents-wiki");
            eEvent.setAttribute("xmlns:s", "http://www.alert-project.eu/strevents-kesi");
            eEvent.setAttribute("xmlns:s1", "http://www.alert-project.eu/strevents-keui");
            eEvent.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
            eEvent.setAttribute("xsi:schemaLocation", "http://www.alert-project.eu/alert-root.xsd");
            dDoc.appendChild(eEvent);
            
            Text tText;
            
            //head element
            Element eHead = dDoc.createElement("ns1:" + MetadataConstants.c_XMLE_head);
            eEvent.appendChild(eHead);
            
                //sender
                Element eSender = dDoc.createElement("ns1:" + MetadataConstants.c_XMLE_sender);
                eHead.appendChild(eSender);
                tText = dDoc.createTextNode(MetadataConstants.c_XMLV_metadataservice);
                eSender.appendChild(tText);

                //timestamp
                Element eTimeStamp = dDoc.createElement("ns1:" + MetadataConstants.c_XMLE_timestamp);
                eHead.appendChild(eTimeStamp);
                tText = dDoc.createTextNode("10000");
                eTimeStamp.appendChild(tText);

                //sequencenumber
                Element eSequenceNumber = dDoc.createElement("ns1:" + MetadataConstants.c_XMLE_sequencenumber);
                eHead.appendChild(eSequenceNumber);
                tText = dDoc.createTextNode("1");
                eSequenceNumber.appendChild(tText);
            
           
            //payload element
            Element ePayload = dDoc.createElement("ns1:" + MetadataConstants.c_XMLE_payload);
            eEvent.appendChild(ePayload);
            
                //meta element
                Element eMeta = dDoc.createElement("ns1:" + MetadataConstants.c_XMLE_meta);
                ePayload.appendChild(eMeta);
                
                    //startTime
                    Element eStartTime = dDoc.createElement("ns1:" + MetadataConstants.c_XMLE_startTime);
                    eMeta.appendChild(eStartTime);
                    tText = dDoc.createTextNode("10010");
                    eStartTime.appendChild(tText);

                    //endTime
                    Element eEndTime = dDoc.createElement("ns1:" + MetadataConstants.c_XMLE_endTime);
                    eMeta.appendChild(eEndTime);
                    tText = dDoc.createTextNode("10020");
                    eEndTime.appendChild(tText);

                    //eventName
                    Element eEventName = dDoc.createElement("ns1:" + MetadataConstants.c_XMLE_eventName);
                    eMeta.appendChild(eEventName);
                    tText = dDoc.createTextNode(MetadataConstants.c_ET_member_reply);
                    eEventName.appendChild(tText);
                    
                    //eventId
                    Element eEventId = dDoc.createElement("ns1:" + MetadataConstants.c_XMLE_eventId);
                    eMeta.appendChild(eEventId);
                    tText = dDoc.createTextNode(sEventId + "Response");
                    eEventId.appendChild(tText);
                    
                    
                //eventData element
                Element eEventData = dDoc.createElement("ns1:" + MetadataConstants.c_XMLE_eventData);
                ePayload.appendChild(eEventData);
                    
                    //mdservice
                    Element eMDService = dDoc.createElement("ns1:" + MetadataConstants.c_XMLE_mdservice);
                    eEventData.appendChild(eMDService);
                    
                        //eventType
                        Element eEventType = dDoc.createElement("o:" + MetadataConstants.c_XMLE_eventType);
                        eMDService.appendChild(eEventType);
                        
                        CreatePropertyStructure(oProperty, dDoc, eEventType);

            //Send created XML document
            MetadataCommunicator.SendXML(dDoc, sEventName);
            
        }
        catch (Exception e)
        {
        }
    }
    
    private static void CreatePropertyStructure(OntoProperty oProperty, Document dDoc, Element eElement)
    {
        try
        {
            Text tText;
            
            //name
            Element eName = dDoc.createElement("o:" + MetadataConstants.c_XMLE_name);
            eElement.appendChild(eName);
            tText = dDoc.createTextNode(oProperty.sName);
            eName.appendChild(tText);
            
            //sTypeOf
            Element eTypeOf = dDoc.createElement("o:" + MetadataConstants.c_XMLE_typeOf);
            eElement.appendChild(eTypeOf);
            tText = dDoc.createTextNode(oProperty.sTypeOf);
            eTypeOf.appendChild(tText);
            
            //value
            Element eValue = dDoc.createElement("o:" + MetadataConstants.c_XMLE_value);
            eElement.appendChild(eValue);
            tText = dDoc.createTextNode(oProperty.sValue);
            eValue.appendChild(tText);
            
            for (int i = 0; i < oProperty.oProperties.size(); i++)
            {
                //property
                Element eProperty = dDoc.createElement("o:" + MetadataConstants.c_XMLE_property);
                eElement.appendChild(eProperty);
                
                CreatePropertyStructure(oProperty.oProperties.get(i), dDoc, eProperty);
            }
        }
        catch (Exception e)
        {
        }
    }
}

