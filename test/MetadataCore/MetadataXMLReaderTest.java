/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MetadataCore;

import MetadataCore.MetadataGlobal.AnnotationData;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.StringWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.junit.*;
import static org.junit.Assert.*;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import MetadataObjects.Commit;
import MetadataObjects.ForumPost;
import MetadataObjects.NewForumPost;
import MetadataObjects.Issue;

/**
 *
 * @author Dejan
 */
public class MetadataXMLReaderTest {
        
    public MetadataXMLReaderTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Creates test request XML.
     * @startRealisation  Dejan Milosavljevic 25.01.2012.
     * @finalModification Dejan Milosavljevic 25.01.2012.
     * @param sAddress
     * @param sSender
     * @param sEventName
     * @param sEventId
     * @param sOptions - 0: no tag; 1: not empty tag; 2: empty tag
     * @return 
     */
    private Document CreateTestXML(String sAddress, 
                                   String sSender, 
                                   String sEventName, 
                                   String sEventId, 
                                   String sOptions)
    {
       try
        {
            DocumentBuilderFactory dbfFactory = DocumentBuilderFactory.newInstance();
            dbfFactory.setNamespaceAware(true);
            DocumentBuilder dbBuilder = dbfFactory.newDocumentBuilder();
            Document dDoc = dbBuilder.newDocument();
            
            dDoc.setXmlVersion("1.0");
            
            Element eWSTN = CreateWSNTStructure(dDoc, sAddress);
            
            //Event
            Element eEvent = CreateEventStructure(dDoc);
            eWSTN.appendChild(eEvent);
            
            //    head element
            Element eHead = CreateHeadElementStructure(dDoc, sSender);
            eEvent.appendChild(eHead);

            //    payload element
            Element ePayload = dDoc.createElement("ns1:" + MetadataConstants.c_XMLE_payload);
            eEvent.appendChild(ePayload);
            //        meta element
            Element eMeta = CreateMetaStructure(dDoc, sEventName, sEventId);
            ePayload.appendChild(eMeta);
            
            //        eventData element
            Element eEventData = dDoc.createElement("ns1:" + MetadataConstants.c_XMLE_eventData);
            ePayload.appendChild(eEventData);
            
            if(sEventName.equals(MetadataConstants.c_ET_ALERT_KESI_IssueNew) ||
               sEventName.equals(MetadataConstants.c_ET_ALERT_KESI_IssueUpdate))   //if event type is new issue event
            {
                //            issue element
                Element eIssue = CreateIssueStructure(dDoc, sOptions);
                eEventData.appendChild(eIssue);
            }
            if(sEventName.equals(MetadataConstants.c_ET_ALERT_KESI_CommitNew))
            {
                //            commit element
                Element eCommit = CreateCommitStructure(dDoc, sOptions);
                eEventData.appendChild(eCommit);
            }
//            if(sEventName.equals(MetadataConstants.c_ET_person_requestNew))   //if event type is new person
//            {
//                //NewPerson(dDoc);
//            }
//            if(sEventName.equals(MetadataConstants.c_ET_APICall_request))   //if event type is API Call request
//            {
//                //APICallRequest(dDoc);
//            }
            if(sEventName.equals(MetadataConstants.c_ET_member_request))   //if event type is member request
            {
                //InstanceRequest(dDoc);
            }
            if(sEventName.equals(MetadataConstants.c_ET_ALERT_KEUI_IssueNew_Annotated)) //if event type is new issue annotation
            {
                //            annotation element
                Element eAnnotation = CreateAnnotationStructure(dDoc, 0, sOptions);
                eEventData.appendChild(eAnnotation);
            }
            if(sEventName.equals(MetadataConstants.c_ET_ALERT_KEUI_IssueUpdate_Annotated)) //if event type is new comment annotation
            {
                //            annotation element
                Element eAnnotation = CreateAnnotationStructure(dDoc, 1, sOptions);
                eEventData.appendChild(eAnnotation);
            }
            if(sEventName.equals(MetadataConstants.c_ET_ALERT_KEUI_CommitNew_Annotated)) //if event type is new commit annotation
            {
                //            annotation element
                Element eAnnotation = CreateAnnotationStructure(dDoc, 2, sOptions);
                eEventData.appendChild(eAnnotation);
            }
            if(sEventName.equals(MetadataConstants.c_ET_ALERT_KEUI_ForumPostNew_Annotated)) //if event type is new forum annotation
            {
                //            annotation element
                Element eAnnotation = CreateAnnotationStructure(dDoc, 3, sOptions);
                eEventData.appendChild(eAnnotation);
            }
            if(sEventName.equals(MetadataConstants.c_ET_ALERT_KEUI_WikiPostNew_Annotated)) //if event type is new wiki annotation
            {
                //            annotation element
                Element eAnnotation = CreateAnnotationStructure(dDoc, 4, sOptions);
                eEventData.appendChild(eAnnotation);
            }
            if(sEventName.equals(MetadataConstants.c_ET_ALERT_KEUI_MailNew_Annotated)) //if event type is new mail annotation
            {
                //            annotation element
                Element eAnnotation = CreateAnnotationStructure(dDoc, 5, sOptions);
                eEventData.appendChild(eAnnotation);
            }
            if(sEventName.equals(MetadataConstants.c_ET_ALERT_ForumSensor_ForumPostNew)) //if event type is new forum post
            {
                //            forum element
                Element eForumPost = CreateForumPostStructure(dDoc, sOptions);
                eEventData.appendChild(eForumPost);
                //NewForumPostData(dDoc);
            }

            return dDoc;
        }
        catch (Exception ex)
        {
            //ex.printStackTrace();
            System.out.println("Error: " + ex.getMessage());
            return null;
        }
    }

    /**
     * Creates WSNT structure element.
     * @startRealisation  Dejan Milosavljevic 25.01.2012.
     * @finalModification Dejan Milosavljevic 25.01.2012.
     * @param dDoc
     * @param sAddress
     * @return 
     */
    private Element CreateWSNTStructure(Document dDoc, String sAddress)
    {
        try
        {
            //envelope element
            Element eEnvelope = dDoc.createElement("s:Envelope");
            eEnvelope.setAttribute("xmlns:s", "http://www.w3.org/2003/05/soap-envelope");
            eEnvelope.setAttribute("xmlns:wsnt", "http://docs.oasis-open.org/wsn/b-2");
            eEnvelope.setAttribute("xmlns:wsa", "http://www.w3.org/2005/08/addressing");
            dDoc.appendChild(eEnvelope);
            
            Text tText;
            
            //header element
            Element eHeader = dDoc.createElement("s:Header");
            eEnvelope.appendChild(eHeader);
            
            //body element
            Element eBody = dDoc.createElement("s:Body");
            eEnvelope.appendChild(eBody);
            
            //    notify element
            Element eNotify = dDoc.createElement("wsnt:Notify");
            eBody.appendChild(eNotify);
                    
            //        notification message element
            Element eNotificationMessage = dDoc.createElement("wsnt:NotificationMessage");
            eNotify.appendChild(eNotificationMessage);
                        
            //            topic element
            Element eTopic = dDoc.createElement("wsnt:Topic");
            eNotificationMessage.appendChild(eTopic);
                        
            //            producer reference element
            Element eProducerReference = dDoc.createElement("wsnt:ProducerReference");
            eNotificationMessage.appendChild(eProducerReference);
                            
            //                address element
            Element eAddress = dDoc.createElement("wsa:Address");
            eProducerReference.appendChild(eAddress);
            tText = dDoc.createTextNode(sAddress);//MetadataConstants.c_XMLV_metadataserviceaddress);
            eAddress.appendChild(tText);

            //            message element
            Element eMessage = dDoc.createElement("wsnt:Message");
            eNotificationMessage.appendChild(eMessage);
                        
            return eMessage;
                    
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Creates event structure element.
     * @startRealisation  Dejan Milosavljevic 25.01.2012.
     * @finalModification Dejan Milosavljevic 25.01.2012.
     * @param dDoc
     * @return 
     */
    private Element CreateEventStructure(Document dDoc)
    {
        //event element
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
        return eEvent;
    }
 
    /**
     * Creates head structure element.
     * @startRealisation  Dejan Milosavljevic 25.01.2012.
     * @finalModification Dejan Milosavljevic 25.01.2012.
     * @param dDoc
     * @param sSender
     * @return 
     */
    private Element CreateHeadElementStructure(Document dDoc, String sSender)
    {
        Text tText;
            
        //head element
        Element eHead = dDoc.createElement("ns1:" + MetadataConstants.c_XMLE_head);

        //    sender
        Element eSender = dDoc.createElement("ns1:" + MetadataConstants.c_XMLE_sender);
        eHead.appendChild(eSender);
        tText = dDoc.createTextNode(sSender);//MetadataConstants.c_XMLV_metadataservice);
        eSender.appendChild(tText);

        //    timestamp
        Element eTimeStamp = dDoc.createElement("ns1:" + MetadataConstants.c_XMLE_timestamp);
        eHead.appendChild(eTimeStamp);
        tText = dDoc.createTextNode("10000");
        eTimeStamp.appendChild(tText);

        //    sequencenumber
        Element eSequenceNumber = dDoc.createElement("ns1:" + MetadataConstants.c_XMLE_sequencenumber);
        eHead.appendChild(eSequenceNumber);
        tText = dDoc.createTextNode("1");
        eSequenceNumber.appendChild(tText);

        return eHead;
    }
    
    /**
     * Creates meta structure element.
     * @startRealisation  Dejan Milosavljevic 25.01.2012.
     * @finalModification Dejan Milosavljevic 25.01.2012.
     * @param dDoc
     * @param sEventName
     * @param sEventId
     * @return 
     */
    private Element CreateMetaStructure(Document dDoc, String sEventName, String sEventId)
    {
        Text tText;
        //meta element
        Element eMeta = dDoc.createElement("ns1:" + MetadataConstants.c_XMLE_meta);
        
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
        tText = dDoc.createTextNode(sEventName);//MetadataConstants.c_ET_APICall_reply);
        eEventName.appendChild(tText);
        //eventId
        Element eEventId = dDoc.createElement("ns1:" + MetadataConstants.c_XMLE_eventId);
        eMeta.appendChild(eEventId);
        tText = dDoc.createTextNode(sEventId);
        eEventId.appendChild(tText);
        //eventType
        Element eEventType = dDoc.createElement("ns1:" + MetadataConstants.c_XMLE_eventType);
        eMeta.appendChild(eEventType);
        tText = dDoc.createTextNode(MetadataConstants.c_XMLV_request);
        eEventType.appendChild(tText);
        
        return eMeta;
    }
    
    /**
     * Creates issue structure element.
     * @startRealisation  Dejan Milosavljevic 25.01.2012.
     * @finalModification Dejan Milosavljevic 25.01.2012.
     * @param dDoc
     * @param sOptions - 0: no tag; 1: not empty tag; 2: empty tag
     * @return 
     */
    private Element CreateIssueStructure(Document dDoc, String sOptions)
    {
        //issue element
        Element eIssue = dDoc.createElement("s:" + MetadataConstants.c_XMLE_issue);

        //    issueId
        if (sOptions.charAt(0) == '1')
        {
            eIssue.appendChild(CreateElement(dDoc, "s:" + "issueId", "184671"));
        }
        else if (sOptions.charAt(0) == '2')
        {
            eIssue.appendChild(CreateElement(dDoc, "s:" + "issueId", ""));
        }
        
        //    issueAuthor
        if (sOptions.charAt(1) == '1')
        {
            Element eAuthor = CreatePersonStructure(dDoc, "s:", MetadataConstants.c_XMLE_issueAuthor,
                                    "Sander Pientka", "cumulus0007@gmail.com", "cumulus0007@gmail.com");
            eIssue.appendChild(eAuthor);
        }
        else if (sOptions.charAt(1) == '2')
        {
            Element eAuthor = CreatePersonStructure(dDoc, "s:", MetadataConstants.c_XMLE_issueAuthor, "", "", "");
            eIssue.appendChild(eAuthor);  
        }
        
        //    issueStatus
        if (sOptions.charAt(2) == '1')
        {
            eIssue.appendChild(CreateElement(dDoc, "s:" + "issueStatus", "Resolved"));
        }
        else if (sOptions.charAt(2) == '2')
        {
            eIssue.appendChild(CreateElement(dDoc, "s:" + "issueStatus", ""));
        }
        
        //    issueResolution
        if (sOptions.charAt(3) == '1')
        {
            eIssue.appendChild(CreateElement(dDoc, "s:" + "issueResolution", "Fixed"));
        }
        else if (sOptions.charAt(3) == '2')
        {
            eIssue.appendChild(CreateElement(dDoc, "s:" + "issueResolution", ""));
        }
        
        //    issueDescription
        if (sOptions.charAt(4) == '1')
        {
            eIssue.appendChild(CreateElement(dDoc, "s:" + "issueDescription", "Notify user on hardware changes 2"));
        }
        else if (sOptions.charAt(4) == '2')
        {
            eIssue.appendChild(CreateElement(dDoc, "s:" + "issueDescription", ""));
        }
        
        //    issueKeyword
        if (sOptions.charAt(5) == '1')
        {
            eIssue.appendChild(CreateElement(dDoc, "s:" + "issueKeyword", "keyword"));
        }
        else if (sOptions.charAt(5) == '2')
        {
            eIssue.appendChild(CreateElement(dDoc, "s:" + "issueKeyword", ""));
        }
        
        //    issueProduct
        if (sOptions.charAt(6) == '1')
        {
            Element eIProduct = dDoc.createElement("s:" + "issueProduct");
            eIssue.appendChild(eIProduct);
            
            //    productId
            eIProduct.appendChild(CreateElement(dDoc, "s:" + "productId", "solid"));
            
            //    productComponentId
            eIProduct.appendChild(CreateElement(dDoc, "s:" + "productComponentId", "general"));
            
            //    productVersion
            eIProduct.appendChild(CreateElement(dDoc, "s:" + "productVersion", "unspecified"));
        }
        else if (sOptions.charAt(6) == '2')
        {
            Element eIProduct = dDoc.createElement("s:" + "issueProduct");
            eIssue.appendChild(eIProduct);
            
            //    productId
            eIProduct.appendChild(CreateElement(dDoc, "s:" + "productId", ""));
            
            //    productComponentId
            eIProduct.appendChild(CreateElement(dDoc, "s:" + "productComponentId", ""));
            
            //    productVersion
            eIProduct.appendChild(CreateElement(dDoc, "s:" + "productVersion", ""));
        }
        
        //    issueComputerSystem
        if (sOptions.charAt(7) == '1')
        {
            Element eICSystem = dDoc.createElement("s:" + "issueComputerSystem");
            eIssue.appendChild(eICSystem);
            
            //    computerSystemId
            eICSystem.appendChild(CreateElement(dDoc, "s:" + "computerSystemId", ""));
            
            //    computerSystemPlatform
            eICSystem.appendChild(CreateElement(dDoc, "s:" + "computerSystemPlatform", "Ubuntu Packages"));
            
            //    computerSystemOS
            eICSystem.appendChild(CreateElement(dDoc, "s:" + "computerSystemOS", "Linux"));
        }
        else if (sOptions.charAt(7) == '2')
        {
            Element eICSystem = dDoc.createElement("s:" + "issueComputerSystem");
            eIssue.appendChild(eICSystem);
            
            //    computerSystemId
            eICSystem.appendChild(CreateElement(dDoc, "s:" + "computerSystemId", ""));
            
            //    computerSystemPlatform
            eICSystem.appendChild(CreateElement(dDoc, "s:" + "computerSystemPlatform", ""));
            
            //    computerSystemOS
            eICSystem.appendChild(CreateElement(dDoc, "s:" + "computerSystemOS", ""));
        }
        
        //    issuePriority
        if (sOptions.charAt(8) == '1')
        {
            eIssue.appendChild(CreateElement(dDoc, "s:" + "issuePriority", "4"));
        }
        else if (sOptions.charAt(8) == '2')
        {
            eIssue.appendChild(CreateElement(dDoc, "s:" + "issuePriority", ""));
        }
        
        //    issueSeverity
        if (sOptions.charAt(9) == '1')
        {
            eIssue.appendChild(CreateElement(dDoc, "s:" + "issueSeverity", "Blocker"));
        }
        else if (sOptions.charAt(9) == '2')
        {
            eIssue.appendChild(CreateElement(dDoc, "s:" + "issueSeverity", ""));
        }
        
        //    issueAssignedTo
        if (sOptions.charAt(10) == '1')
        {
            Element eIAssignedTo = CreatePersonStructure(dDoc, "s:", MetadataConstants.c_XMLE_issueAssignedTo,
                                    "Alex Fiestas", "afiestas@kde.org", "afiestas@kde.org");
            eIssue.appendChild(eIAssignedTo);
        }
        else if (sOptions.charAt(10) == '2')
        {
            Element eIAssignedTo = CreatePersonStructure(dDoc, "s:", MetadataConstants.c_XMLE_issueAssignedTo, "", "", "");
            eIssue.appendChild(eIAssignedTo);  
        }
        
        //    issueCCPerson
        if (sOptions.charAt(11) == '1')
        {
            Element eICCPerson = CreatePersonStructure(dDoc, "s:", MetadataConstants.c_XMLE_issueCCPerson,
                                    "", "angel_blue_co2004@yahoo.com", "angel_blue_co2004@yahoo.com");
            eIssue.appendChild(eICCPerson);
        }
        else if (sOptions.charAt(11) == '2')
        {
            Element eICCPerson = CreatePersonStructure(dDoc, "s:", MetadataConstants.c_XMLE_issueCCPerson, "", "", "");
            eIssue.appendChild(eICCPerson);  
        }
        
        //    issueUrl
        if (sOptions.charAt(12) == '1')
        {
            eIssue.appendChild(CreateElement(dDoc, "s:" + "issueUrl", "https://bugs.kde.org/show_bug.cgi?id=184671"));
        }
        else if (sOptions.charAt(12) == '2')
        {
            eIssue.appendChild(CreateElement(dDoc, "s:" + "issueUrl", ""));
        }
        
        //    issueDependsOnId
        if (sOptions.charAt(13) == '1')
        {
            eIssue.appendChild(CreateElement(dDoc, "s:" + "issueDependsOnId", "10001"));
        }
        else if (sOptions.charAt(13) == '2')
        {
            eIssue.appendChild(CreateElement(dDoc, "s:" + "issueDependsOnId", ""));
        }
        
        //    issueBlocksId
        if (sOptions.charAt(14) == '1')
        {
            eIssue.appendChild(CreateElement(dDoc, "s:" + "issueBlocksId", "20001"));
        }
        else if (sOptions.charAt(14) == '2')
        {
            eIssue.appendChild(CreateElement(dDoc, "s:" + "issueBlocksId", ""));
        }
        
        //    issueDuplicateOfId
        if (sOptions.charAt(15) == '1')
        {
            eIssue.appendChild(CreateElement(dDoc, "s:" + "issueDuplicateOfId", "235223"));
        }
        else if (sOptions.charAt(15) == '2')
        {
            eIssue.appendChild(CreateElement(dDoc, "s:" + "issueDuplicateOfId", ""));
        }
        
        //    issueMergedIntoId
        if (sOptions.charAt(16) == '1')
        {
            eIssue.appendChild(CreateElement(dDoc, "s:" + "issueMergedIntoId", "231511"));
        }
        else if (sOptions.charAt(16) == '2')
        {
            eIssue.appendChild(CreateElement(dDoc, "s:" + "issueMergedIntoId", ""));
        }
        
        //    issueDateOpened
        if (sOptions.charAt(17) == '1')
        {
            eIssue.appendChild(CreateElement(dDoc, "s:" + "issueDateOpened", "2009-02-17 16:31"));
        }
        else if (sOptions.charAt(17) == '2')
        {
            eIssue.appendChild(CreateElement(dDoc, "s:" + "issueDateOpened", ""));
        }
        
        //    issueLastModified
        if (sOptions.charAt(18) == '1')
        {
            eIssue.appendChild(CreateElement(dDoc, "s:" + "issueLastModified", "2011-04-15 10:55:57"));
        }
        else if (sOptions.charAt(18) == '2')
        {
            eIssue.appendChild(CreateElement(dDoc, "s:" + "issueLastModified", ""));
        }
        
        //    issueMilestone
        if (sOptions.charAt(19) == '1')
        {
            Element eIMilestone = dDoc.createElement("s:" + "issueMilestone");
            eIssue.appendChild(eIMilestone);
            
            //    milestoneId
            eIMilestone.appendChild(CreateElement(dDoc, "s:" + "milestoneId", "12355"));
            
            //    milestoneTarget
            eIMilestone.appendChild(CreateElement(dDoc, "s:" + "milestoneTarget", "2009-02-17 16:31"));
        }
        else if (sOptions.charAt(19) == '2')
        {
            Element eIMilestone = dDoc.createElement("s:" + "issueMilestone");
            eIssue.appendChild(eIMilestone);
            
            //    milestoneId
            eIMilestone.appendChild(CreateElement(dDoc, "s:" + "milestoneId", ""));
            
            //    milestoneTarget
            eIMilestone.appendChild(CreateElement(dDoc, "s:" + "milestoneTarget", ""));
        }
        
        //    issueComment
        if (sOptions.charAt(20) == '1')
        {
            Element eIComment = dDoc.createElement("s:" + "issueComment");
            eIssue.appendChild(eIComment);
            
            //    commentId
            eIComment.appendChild(CreateElement(dDoc, "s:" + "commentId", ""));
            
            //    commentNumber
            eIComment.appendChild(CreateElement(dDoc, "s:" + "commentNumber", "0"));
            
            //    commentText
            eIComment.appendChild(CreateElement(dDoc, "s:" + "commentText",
                    "Version: (using KDE 4.2.0) OS: Linux Installed from: Ubuntu Packages It would be awesome if KDE notifies te user of newly installed hardware, missing drivers, programs to use the connected device, etc."));
            
            //    commentPerson
            Element eCPerson = CreatePersonStructure(dDoc, "s:", MetadataConstants.c_XMLE_commentPerson,
                                    "Sander Pientka", "cumulus0007@gmail.com", "cumulus0007@gmail.com");
            eIComment.appendChild(eCPerson);
            
            //    commentDate
            eIComment.appendChild(CreateElement(dDoc, "s:" + "commentDate", "2009-02-17 16:31:37"));
        }
        else if (sOptions.charAt(20) == '2')
        {
            Element eIComment = dDoc.createElement("s:" + "issueComment");
            eIssue.appendChild(eIComment);
            
            //    commentId
            eIComment.appendChild(CreateElement(dDoc, "s:" + "commentId", ""));
            
            //    commentNumber
            eIComment.appendChild(CreateElement(dDoc, "s:" + "commentNumber", ""));
            
            //    commentText
            eIComment.appendChild(CreateElement(dDoc, "s:" + "commentText", ""));
            
            //    commentPerson
            Element eCPerson = CreatePersonStructure(dDoc, "s:", MetadataConstants.c_XMLE_commentPerson, "", "", "");
            eIComment.appendChild(eCPerson);
            
            //    commentDate
            eIComment.appendChild(CreateElement(dDoc, "s:" + "commentDate", ""));
        }
        
        //    issueAttachment
        if (sOptions.charAt(21) == '1')
        {
            Element eIAttachment = dDoc.createElement("s:" + "issueAttachment");
            eIssue.appendChild(eIAttachment);
            
            //    attachmentId
            eIAttachment.appendChild(CreateElement(dDoc, "s:" + "attachmentId", "3532"));
            
            //    attachmentFilename
            eIAttachment.appendChild(CreateElement(dDoc, "s:" + "attachmentFilename", "name"));
            
            //    attachmentType
            eIAttachment.appendChild(CreateElement(dDoc, "s:" + "attachmentType", "doc"));
            
            //    attachmentCreator
            Element eACreator = CreatePersonStructure(dDoc, "s:", MetadataConstants.c_XMLE_attachmentCreator,
                                    "Angel Blue01", "angel_blue_co2004@yahoo.com", "angel_blue_co2004@yahoo.com");
            eIAttachment.appendChild(eACreator);
        }
        else if (sOptions.charAt(21) == '2')
        {
            Element eIAttachment = dDoc.createElement("s:" + "issueAttachment");
            eIssue.appendChild(eIAttachment);
            
            //    attachmentId
            eIAttachment.appendChild(CreateElement(dDoc, "s:" + "attachmentId", ""));
            
            //    attachmentFilename
            eIAttachment.appendChild(CreateElement(dDoc, "s:" + "attachmentFilename", ""));
            
            //    attachmentType
            eIAttachment.appendChild(CreateElement(dDoc, "s:" + "attachmentType", ""));
            
            //    attachmentCreator
            Element eACreator = CreatePersonStructure(dDoc, "s:", MetadataConstants.c_XMLE_attachmentCreator, "", "", "");
            eIAttachment.appendChild(eACreator);
        }
        
        //    issueActivity
        if (sOptions.charAt(22) == '1')
        {
            Element eIActivity = dDoc.createElement("s:" + "issueActivity");
            eIssue.appendChild(eIActivity);
            
            //    activityId
            eIActivity.appendChild(CreateElement(dDoc, "s:" + "activityId", "3631"));
            
            //    activityWho
            eIActivity.appendChild(CreateElement(dDoc, "s:" + "activityWho", "ervin@kde.org"));
            
            //    activityWhen
            eIActivity.appendChild(CreateElement(dDoc, "s:" + "activityWhen", "2011-04-15 10:55:57"));
            
            //    activityWRA
            Element eActivityWRA = dDoc.createElement("s:" + "activityWRA");
            eIActivity.appendChild(eActivityWRA);
            
            //        activityWhat
            eActivityWRA.appendChild(CreateElement(dDoc, "s:" + "activityWhat", "AssignedTo"));
            
            //        activityRemoved
            eActivityWRA.appendChild(CreateElement(dDoc, "s:" + "activityRemoved", "ervin@kde.org"));
            
            //        activityAdded
            eActivityWRA.appendChild(CreateElement(dDoc, "s:" + "activityAdded", "afiestas@kde.org"));
        }
        else if (sOptions.charAt(22) == '2')
        {
            Element eIActivity = dDoc.createElement("s:" + "issueAttachment");
            eIssue.appendChild(eIActivity);
            
            //    activityId
            eIActivity.appendChild(CreateElement(dDoc, "s:" + "activityId", ""));
            
            //    activityWho
            eIActivity.appendChild(CreateElement(dDoc, "s:" + "activityWho", ""));
            
            //    activityWhen
            eIActivity.appendChild(CreateElement(dDoc, "s:" + "activityWhen", ""));
            
            //    activityWRA
            Element eActivityWRA = dDoc.createElement("s:" + "activityWRA");
            eIActivity.appendChild(eActivityWRA);
            
            //        activityWhat
            eActivityWRA.appendChild(CreateElement(dDoc, "s:" + "activityWhat", ""));
            
            //        activityRemoved
            eActivityWRA.appendChild(CreateElement(dDoc, "s:" + "activityRemoved", ""));
            
            //        activityAdded
            eActivityWRA.appendChild(CreateElement(dDoc, "s:" + "activityAdded", ""));
        }
        
        //    issueTracker
        if (sOptions.charAt(23) == '1')
        {
            Element eITracker = dDoc.createElement("s:" + "issueTracker");
            eIssue.appendChild(eITracker);
            
            //    issueTrackerId
            eITracker.appendChild(CreateElement(dDoc, "s:" + "issueTrackerId", "241"));
            
            //    issueTrackerType
            eITracker.appendChild(CreateElement(dDoc, "s:" + "issueTrackerType", "bugzilla"));
            
            //    issueTrackerURL
            eITracker.appendChild(CreateElement(dDoc, "s:" + "issueTrackerURL", "https://bugs.kde.org/"));
        }
        else if (sOptions.charAt(23) == '2')
        {
            Element eITracker = dDoc.createElement("s:" + "issueTracker");
            eIssue.appendChild(eITracker);
            
            //    issueTrackerId
            eITracker.appendChild(CreateElement(dDoc, "s:" + "issueTrackerId", ""));
            
            //    issueTrackerType
            eITracker.appendChild(CreateElement(dDoc, "s:" + "issueTrackerType", ""));
            
            //    issueTrackerURL
            eITracker.appendChild(CreateElement(dDoc, "s:" + "issueTrackerURL", ""));
        }
        
        return eIssue;
    }
    
    /**
     * Creates commit structure element.
     * @startRealisation  Dejan Milosavljevic 25.01.2012.
     * @finalModification Dejan Milosavljevic 25.01.2012.
     * @param dDoc
     * @param sOptions - 0: no tag; 1: not empty tag; 2: empty tag
     * @return 
     */
    private Element CreateCommitStructure(Document dDoc, String sOptions)
    {
        //commit element
        Element eCommit = dDoc.createElement("s:" + MetadataConstants.c_XMLE_commit);

        //    commitRepositoryUri
        if (sOptions.charAt(0) == '1')
        {
            eCommit.appendChild(CreateElement(dDoc, "s:" + "commitRepositoryUri",
                    "http://www.alert-project.eu/ontologies/alert_scm.owl#Repository1"));
        }
        else if (sOptions.charAt(0) == '2')
        {
            eCommit.appendChild(CreateElement(dDoc, "s:" + "commitRepositoryUri", ""));
        }

        //    commitRevisionTag
        if (sOptions.charAt(1) == '1')
        {
            eCommit.appendChild(CreateElement(dDoc, "s:" + "commitRevisionTag", "1"));
        }
        else if (sOptions.charAt(1) == '2')
        {
            eCommit.appendChild(CreateElement(dDoc, "s:" + "commitRevisionTag", ""));
        }
        
        //    commitAuthor
        if (sOptions.charAt(2) == '1')
        {
            Element eCommitAuthor = CreatePersonStructure(dDoc, "s:", MetadataConstants.c_XMLE_commitAuthor,
                                    "Sasa Stojanovic", "sasa.stojanovic@cimcollege.rs", "sasa.stojanovic@cimcollege.rs");
            eCommit.appendChild(eCommitAuthor);
        }
        else if (sOptions.charAt(2) == '2')
        {
            Element eCommitAuthor = CreatePersonStructure(dDoc, "s:", MetadataConstants.c_XMLE_commitAuthor, "", "", "");
            eCommit.appendChild(eCommitAuthor);  
        }
        
        //    commitCommitter
        if (sOptions.charAt(3) == '1')
        {
            Element eCommitCommitter = CreatePersonStructure(dDoc, "s:", MetadataConstants.c_XMLE_commitAuthor, 
                                    "Ivan Obradovic", "ivan.obradovic@cimcollege.rs", "ivan.obradovic@cimcollege.rs");
            eCommit.appendChild(eCommitCommitter);
        }
        else if (sOptions.charAt(3) == '2')
        {
            Element eCommitCommitter = CreatePersonStructure(dDoc, "s:", MetadataConstants.c_XMLE_commitAuthor, "", "", "");
            eCommit.appendChild(eCommitCommitter);
        }
        
        //    commitDate
        if (sOptions.charAt(4) == '1')
        {
            eCommit.appendChild(CreateElement(dDoc, "s:" + "commitDate", "2012-01-16 16:31"));
        }
        else if (sOptions.charAt(4) == '2')
        {
            eCommit.appendChild(CreateElement(dDoc, "s:" + "commitDate", ""));
        }
        
        //    commitMessageLog
        if (sOptions.charAt(5) == '1')
        {
            eCommit.appendChild(CreateElement(dDoc, "s:" + "commitMessageLog", "comment of commit"));
        }
        else if (sOptions.charAt(5) == '2')
        {
            eCommit.appendChild(CreateElement(dDoc, "s:" + "commitMessageLog", ""));
        }
        
        //commitFile
        if (sOptions.charAt(6) == '1')
        {
            Element eCFile = dDoc.createElement("s:" + "commitFile");
            eCommit.appendChild(eCFile);
            
            //    fileAction
            eCFile.appendChild(CreateElement(dDoc, "s:" + "fileAction", ""));
            
            //    fileBranch
            eCFile.appendChild(CreateElement(dDoc, "s:" + "fileBranch", ""));
            
            //    fileModules
            Element eCFileModules = dDoc.createElement("s:" + "fileModules");
            eCFile.appendChild(eCFileModules);
            
            //        moduleName
            eCFileModules.appendChild(CreateElement(dDoc, "s:" + "moduleName", ""));
            
            //        moduleStartLine
            eCFileModules.appendChild(CreateElement(dDoc, "s:" + "moduleStartLine", ""));
            
            //        moduleEndLine
            eCFileModules.appendChild(CreateElement(dDoc, "s:" + "moduleEndLine", ""));
            
            //        moduleMethods
            Element eCModuleMethods = dDoc.createElement("s:" + "moduleMethods");
            eCFileModules.appendChild(eCModuleMethods);
            
            //            methodName
            eCModuleMethods.appendChild(CreateElement(dDoc, "s:" + "methodName", ""));
            
            //            methodStartLine
            eCModuleMethods.appendChild(CreateElement(dDoc, "s:" + "methodStartLine", ""));
            
            //            methodEndLine
            eCModuleMethods.appendChild(CreateElement(dDoc, "s:" + "methodEndLine", ""));
        }
        else if (sOptions.charAt(6) == '2')
        {
            Element eCFile = dDoc.createElement("s:" + "commitFile");
            eCommit.appendChild(eCFile);
            
            //    fileAction
            eCFile.appendChild(CreateElement(dDoc, "s:" + "fileAction", ""));
            
            //    fileBranch
            eCFile.appendChild(CreateElement(dDoc, "s:" + "fileBranch", ""));
            
            //    fileModules
            Element eCFileModules = dDoc.createElement("s:" + "fileModules");
            eCFile.appendChild(eCFileModules);
            
            //        moduleName
            eCFileModules.appendChild(CreateElement(dDoc, "s:" + "moduleName", ""));
            
            //        moduleStartLine
            eCFileModules.appendChild(CreateElement(dDoc, "s:" + "moduleStartLine", ""));
            
            //        moduleEndLine
            eCFileModules.appendChild(CreateElement(dDoc, "s:" + "moduleEndLine", ""));
            
            //        moduleMethods
            Element eCModuleMethods = dDoc.createElement("s:" + "moduleMethods");
            eCFileModules.appendChild(eCModuleMethods);
            
            //            methodName
            eCModuleMethods.appendChild(CreateElement(dDoc, "s:" + "methodName", ""));
            
            //            methodStartLine
            eCModuleMethods.appendChild(CreateElement(dDoc, "s:" + "methodStartLine", ""));
            
            //            methodEndLine
            eCModuleMethods.appendChild(CreateElement(dDoc, "s:" + "methodEndLine", ""));
        }
        
        return eCommit;
    }
    
    /**
     * Creates post structure element.
     * @startRealisation  Dejan Milosavljevic 25.01.2012.
     * @finalModification Dejan Milosavljevic 25.01.2012.
     * @param dDoc
     * @param sOptions - 0: no tag; 1: not empty tag; 2: empty tag
     * @return 
     */
    private Element CreateForumPostStructure(Document dDoc, String sOptions)
    {
        //forum element
        Element eForumPost = dDoc.createElement("s1:" + MetadataConstants.c_XMLE_forum);

        //    forumItemId
        if (sOptions.charAt(0) == '1')
        {
            eForumPost.appendChild(CreateElement(dDoc, "s1:" + "forumItemId", "3557"));
        }
        else if (sOptions.charAt(0) == '2')
        {
            eForumPost.appendChild(CreateElement(dDoc, "s1:" + "forumItemId", ""));
        }

        //    forumId
        if (sOptions.charAt(1) == '1')
        {
            eForumPost.appendChild(CreateElement(dDoc, "s1:" + "forumId", "3558"));
        }
        else if (sOptions.charAt(1) == '2')
        {
            eForumPost.appendChild(CreateElement(dDoc, "s1:" + "forumId", ""));
        }
        
        //    threadId
        if (sOptions.charAt(2) == '1')
        {
            eForumPost.appendChild(CreateElement(dDoc, "s1:" + "threadId", "3559"));
        }
        else if (sOptions.charAt(2) == '2')
        {
            eForumPost.appendChild(CreateElement(dDoc, "s1:" + "threadId", ""));
        }
        
        //    postId
        if (sOptions.charAt(3) == '1')
        {
            eForumPost.appendChild(CreateElement(dDoc, "s1:" + "postId", "3560"));
        }
        else if (sOptions.charAt(3) == '2')
        {
            eForumPost.appendChild(CreateElement(dDoc, "s1:" + "postId", ""));
        }
        
        //    time
        if (sOptions.charAt(4) == '1')
        {
            eForumPost.appendChild(CreateElement(dDoc, "s1:" + "time", "2012-01-16 16:31"));
        }
        else if (sOptions.charAt(4) == '2')
        {
            eForumPost.appendChild(CreateElement(dDoc, "s1:" + "time", ""));
        }
        
        //    subject
        if (sOptions.charAt(5) == '1')
        {
            eForumPost.appendChild(CreateElement(dDoc, "s1:" + "subject", "This is a test subject for post"));
        }
        else if (sOptions.charAt(5) == '2')
        {
            eForumPost.appendChild(CreateElement(dDoc, "s1:" + "subject", ""));
        }
        
        //    body
        if (sOptions.charAt(6) == '1')
        {
            eForumPost.appendChild(CreateElement(dDoc, "s1:" + "body", "This is a test body for post"));
        }
        else if (sOptions.charAt(6) == '2')
        {
            eForumPost.appendChild(CreateElement(dDoc, "s1:" + "body", ""));
        }
        
        //    author
        if (sOptions.charAt(7) == '1')
        {
            Element eAuthor = CreatePersonStructure(dDoc, "s1:", MetadataConstants.c_XMLE_author,
                                    "Angel Blue", "angel_blue_co2004@yahoo.com", "angel_blue_co2004@yahoo.com");
            eForumPost.appendChild(eAuthor);
        }
        else if (sOptions.charAt(7) == '2')
        {
            Element eAuthor = CreatePersonStructure(dDoc, "s1:", MetadataConstants.c_XMLE_author, "", "", "");
            eForumPost.appendChild(eAuthor);  
        }
        
        //    category
        if (sOptions.charAt(8) == '1')
        {
            eForumPost.appendChild(CreateElement(dDoc, "s1:" + "category", "xxxxxx"));
        }
        else if (sOptions.charAt(8) == '2')
        {
            eForumPost.appendChild(CreateElement(dDoc, "s1:" + "category", ""));
        }
        
        return eForumPost;
    }
    
        /**
     * Creates annotation structure element.
     * @startRealisation  Dejan Milosavljevic 25.01.2012.
     * @finalModification Dejan Milosavljevic 25.01.2012.
     * @param dDoc
     * @param iType - 0: Issue; 1: Comment; 2: Commit; 3: Forum post; 4: Wiki post; 5: Mail post
     * @param sOptions - 0: no tag; 1: not empty tag; 2: empty tag
     * @return 
     */
    private Element CreateAnnotationStructure(Document dDoc, int iType, String sOptions)
    {
        String sItemUri = "";
        String sFirstAnnotated = "";
        String sSecondAnnotated = "";
        String sFirstConcept = "";
        String sSecondConcept = "";
        String sFirstAnnotation = "";
        String sSecondAnnotation = "";
        String sFirstConceptUri = "";
        String sSecondConceptUri = "";
        String sFirstConceptCount = "";
        String sSecondConceptCount = "";
        
        switch (iType)
        {
            case 0: //Issue
                sItemUri = "http://www.alert-project.eu/ontologies/alert_its.owl#Bug1";
                sFirstAnnotated = "subjectAnnotated";
                sSecondAnnotated = "descriptionAnnotated";
                sFirstConcept = "subjectConcepts";
                sSecondConcept = "descriptionConcepts";
                sFirstAnnotation = "![CDATA[This is test <concept uri=\"http://www.alert-project.eu/ontologies/alert_its.owl#Bug1\">annotation</concept> for subject]]";
                sSecondAnnotation = "![CDATA[This is test <concept uri=\"http://www.alert-project.eu/ontologies/alert_its.owl#Bug1\">annotation</concept> for description]]";
                sFirstConceptUri = "http://www.alert-project.eu/ontologies/alert_its.owl#Bug1";
                sSecondConceptUri = "http://www.alert-project.eu/ontologies/alert_its.owl#Bug1";
                sFirstConceptCount = "1";
                sSecondConceptCount = "1";
                break;
            case 1: //Comment
                sItemUri = "http://www.ifi.uzh.ch/ddis/evoont/2008/11/bom#Comment1";
                sFirstAnnotated = "commentAnnotated";
                sSecondAnnotated = "";
                sFirstConcept = "commentConcepts";
                sSecondConcept = "";
                sFirstAnnotation = "![CDATA[This is test <concept uri=\"http://www.alert-project.eu/ontologies/alert_its.owl#Bug1\">annotation</concept> for comment]]";
                sSecondAnnotation = "";
                sFirstConceptUri = "http://www.alert-project.eu/ontologies/alert_its.owl#Bug1";
                sSecondConceptUri = "";
                sFirstConceptCount = "1";
                sSecondConceptCount = "";
                break;
            case 2: //Commit
                sItemUri = "http://www.alert-project.eu/ontologies/alert_scm.owl#Commit1";
                sFirstAnnotated = "commitAnnotated";
                sSecondAnnotated = "";
                sFirstConcept = "commitConcepts";
                sSecondConcept = "";
                sFirstAnnotation = "![CDATA[This is test <concept uri=\"http://www.alert-project.eu/ontologies/alert_its.owl#Bug1\">annotation</concept> for commit]]";
                sSecondAnnotation = "";
                sFirstConceptUri = "http://www.alert-project.eu/ontologies/alert_its.owl#Bug1";
                sSecondConceptUri = "";
                sFirstConceptCount = "1";
                sSecondConceptCount = "";
                break;
            case 3: //Forum post
                sItemUri = ""; //???
                sFirstAnnotated = "titleAnnotated";
                sSecondAnnotated = "bodyAnnotated";
                sFirstConcept = "titleConcepts";
                sSecondConcept = "bodyConcepts";
                sFirstAnnotation = "![CDATA[This is test <concept uri=\"http://www.alert-project.eu/ontologies/alert_its.owl#Bug1\">annotation</concept> for post title]]";
                sSecondAnnotation = "![CDATA[This is test <concept uri=\"http://www.alert-project.eu/ontologies/alert_its.owl#Bug1\">annotation</concept> for post body]]";
                sFirstConceptUri = "http://www.alert-project.eu/ontologies/alert_its.owl#Bug1";
                sSecondConceptUri = "http://www.alert-project.eu/ontologies/alert_its.owl#Bug1";
                sFirstConceptCount = "1";
                sSecondConceptCount = "1";
                break;
            case 4: //Wiki post
                sItemUri = ""; //???
                sFirstAnnotated = "titleAnnotated";
                sSecondAnnotated = "bodyAnnotated";
                sFirstConcept = "titleConcepts";
                sSecondConcept = "bodyConcepts";
                sFirstAnnotation = "![CDATA[This is test <concept uri=\"http://www.alert-project.eu/ontologies/alert_its.owl#Bug1\">annotation</concept> for wiki post title]]";
                sSecondAnnotation = "![CDATA[This is test <concept uri=\"http://www.alert-project.eu/ontologies/alert_its.owl#Bug1\">annotation</concept> for wiki post body]]";
                sFirstConceptUri = "http://www.alert-project.eu/ontologies/alert_its.owl#Bug1";
                sSecondConceptUri = "http://www.alert-project.eu/ontologies/alert_its.owl#Bug1";
                sFirstConceptCount = "1";
                sSecondConceptCount = "1";
                break;
            case 5: //Mail post
                sItemUri = ""; //???
                sFirstAnnotated = "subjectAnnotated";
                sSecondAnnotated = "bodyAnnotated";
                sFirstConcept = "titleConcepts";
                sSecondConcept = "bodyConcepts";
                sFirstAnnotation = "![CDATA[This is test <concept uri=\"http://www.alert-project.eu/ontologies/alert_its.owl#Bug1\">annotation</concept> for mail subject]]";
                sSecondAnnotation = "![CDATA[This is test <concept uri=\"http://www.alert-project.eu/ontologies/alert_its.owl#Bug1\">annotation</concept> for mail body]]";
                sFirstConceptUri = "http://www.alert-project.eu/ontologies/alert_its.owl#Bug1";
                sSecondConceptUri = "http://www.alert-project.eu/ontologies/alert_its.owl#Bug1";
                sFirstConceptCount = "1";
                sSecondConceptCount = "1";
                break;
        }
        
        //annotation element
        Element eAnnotation = dDoc.createElement("s1:" + MetadataConstants.c_XMLE_annotation);

        //    itemUri
        if (sOptions.charAt(0) == '1')
        {
            eAnnotation.appendChild(CreateElement(dDoc, "s1:" + "itemUri", sItemUri));
        }
        else if (sOptions.charAt(0) == '2')
        {
            eAnnotation.appendChild(CreateElement(dDoc, "s1:" + "itemUri", ""));
        }

        //    xxxAnnotated
        if (sOptions.charAt(1) == '1')
        {
            eAnnotation.appendChild(CreateElement(dDoc, "s1:" + sFirstAnnotated, sFirstAnnotation));
        }
        else if (sOptions.charAt(1) == '2')
        {
            eAnnotation.appendChild(CreateElement(dDoc, "s1:" + sFirstAnnotated, ""));
        }
        
        //    xxxConcepts
        if (sOptions.charAt(2) == '1')
        {
            Element eFConcepts = dDoc.createElement("s:" + sFirstConcept);
            eAnnotation.appendChild(eFConcepts);
            
            //    uri
            eFConcepts.appendChild(CreateElement(dDoc, "s:" + "uri", sFirstConceptUri));
            
            //    count
            eFConcepts.appendChild(CreateElement(dDoc, "s:" + "count", sFirstConceptCount));
        }
        else if (sOptions.charAt(2) == '2')
        {
            Element eFConcepts = dDoc.createElement("s:" + sFirstConcept);
            eAnnotation.appendChild(eFConcepts);
            
            //    uri
            eFConcepts.appendChild(CreateElement(dDoc, "s:" + "uri", ""));
            
            //    count
            eFConcepts.appendChild(CreateElement(dDoc, "s:" + "count", ""));
        }
        
        if (!sSecondAnnotated.isEmpty())
        {
            //    yyyAnnotated
            if (sOptions.charAt(3) == '1')
            {
                eAnnotation.appendChild(CreateElement(dDoc, "s1:" + sSecondAnnotated, sSecondAnnotation));
            }
            else if (sOptions.charAt(3) == '2')
            {
                eAnnotation.appendChild(CreateElement(dDoc, "s1:" + sSecondAnnotated, ""));
            }
        
            //    yyyConcepts
            if (sOptions.charAt(4) == '1')
            {
                Element eSConcepts = dDoc.createElement("s:" + sSecondConcept);
                eAnnotation.appendChild(eSConcepts);
            
                //    uri
                eSConcepts.appendChild(CreateElement(dDoc, "s:" + "uri", sSecondConceptUri));
            
                //    count
                eSConcepts.appendChild(CreateElement(dDoc, "s:" + "count", sSecondConceptCount));
            }
            else if (sOptions.charAt(4) == '2')
            {
                Element eSConcepts = dDoc.createElement("s:" + sSecondConcept);
                eAnnotation.appendChild(eSConcepts);
            
                //    uri
                eSConcepts.appendChild(CreateElement(dDoc, "s:" + "uri", ""));
            
                //    count
                eSConcepts.appendChild(CreateElement(dDoc, "s:" + "count", ""));
            }
        }
        
        return eAnnotation;
    }
    
    /**
     * Creates person structure element.
     * @startRealisation  Dejan Milosavljevic 25.01.2012.
     * @finalModification Dejan Milosavljevic 25.01.2012.
     * @param dDoc
     * @param sPrefix
     * @param sElementName
     * @param sPersonName
     * @param sID
     * @param sEmail
     * @return 
     */
    private Element CreatePersonStructure(Document dDoc, 
                                          String sPrefix, 
                                          String sElementName, 
                                          String sPersonName, 
                                          String sID, 
                                          String sEmail)
    {
        //Text tText;
        
        //person element
        Element ePerson = dDoc.createElement(sPrefix + sElementName);
        
        //    name
        ePerson.appendChild(CreateElement(dDoc, sPrefix + MetadataConstants.c_XMLE_name, sPersonName));
//        Element eName = dDoc.createElement(sPrefix + MetadataConstants.c_XMLE_name);
//        ePerson.appendChild(eName);
//        tText = dDoc.createTextNode(sPersonName);
//        eName.appendChild(tText);
        
        //    id
        ePerson.appendChild(CreateElement(dDoc, sPrefix + MetadataConstants.c_XMLE_Id, sID));
//        Element eId = dDoc.createElement(sPrefix + MetadataConstants.c_XMLE_Id);
//        ePerson.appendChild(eId);
//        tText = dDoc.createTextNode(sID);
//        eId.appendChild(tText);
        
        //    email
        ePerson.appendChild(CreateElement(dDoc, sPrefix + MetadataConstants.c_XMLE_email, sEmail));
//        Element eEmail = dDoc.createElement(sPrefix + MetadataConstants.c_XMLE_email);
//        ePerson.appendChild(eEmail);
//        tText = dDoc.createTextNode(sEmail);
//        eEmail.appendChild(tText);
        
        return ePerson;
    }
    
    /**
     * Creates Element.
     * @param dDoc
     * @param sElementName
     * @param sElementValue
     * @return 
     */
    private Element CreateElement(Document dDoc, String sElementName, String sElementValue)
    {
        Text tText;
        Element eElement = dDoc.createElement(sElementName);
        tText = dDoc.createTextNode(sElementValue);
        eElement.appendChild(tText);
        return eElement;
    }
    
    /**
     * Test of ReadXML method, of class MetadataXMLReader.
     */
    @Ignore
    @Test
    public void testReadXML() {
        System.out.println("* MetadataXMLReaderTest: ReadXML");
        //Document dDoc = null;
        String sProjectPath = System.getProperty("user.dir");
        String sDocPath = sProjectPath + "\\Events\\Data Storage Events\\Metadata.issue\\Metadata.issue.requestNew.xml";
        DocumentBuilderFactory dbfFactory = DocumentBuilderFactory.newInstance();
        dbfFactory.setNamespaceAware(true);
        try
        {
            DocumentBuilder dbBuilder = dbfFactory.newDocumentBuilder();
            Document dDoc = dbBuilder.parse(new File(sDocPath));
            dDoc.getDocumentElement().normalize();
            
            MetadataXMLReader.ReadXML(dDoc); //Result cannot be checked!!!
            
            // TODO review the generated test code and remove the default call to fail.
            //fail("The test case is a prototype.");
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            fail("The test case generated exception.");
        }
    }
    
    /**
     * Test of NewUpdateIssue method, of class MetadataXMLReader.
     */
    @Test
    public void testNewUpdateIssue() {
        System.out.println("* MetadataXMLReaderTest: NewUpdateIssue");

        //no tags
        Document dDoc = CreateTestXML("http://www.alert-project.eu/kesi",
                                      "KESI", "Metadata.issue.requestNew",
                                      "5748", "000000000000000000000000");
        Issue resultNull = MetadataXMLReader.NewUpdateIssue(dDoc, false);
        assertNotNull(resultNull);
        
        //empty tags
        dDoc = CreateTestXML("http://www.alert-project.eu/kesi",
                             "KESI", "Metadata.issue.requestNew",
                             "5748", "222222222222222222222222");
        Issue resultEmpty = MetadataXMLReader.NewUpdateIssue(dDoc, false);
        assertNotNull(resultEmpty);
            
        //not empty tags
        dDoc = CreateTestXML("http://www.alert-project.eu/kesi",
                             "KESI", "Metadata.issue.requestNew",
                             "5748", "111111111111111111111111");
        Issue result = MetadataXMLReader.NewUpdateIssue(dDoc, false);
        assertNotNull(result);
        
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
    /**
     * Test of NewCommit method, of class MetadataXMLReader.
     */
    @Test
    public void testNewCommit() {
        System.out.println("* MetadataXMLReaderTest: NewCommit");

        //no tags
        Document dDoc = CreateTestXML("http://www.alert-project.eu/kesi",
                                      "KESI", "Metadata.commit.requestNew",
                                      "5748", "0000000");
        Commit resultNull = MetadataXMLReader.NewCommit(dDoc);
        assertNotNull(resultNull);
        
        //empty tags
        dDoc = CreateTestXML("http://www.alert-project.eu/kesi",
                             "KESI", "Metadata.commit.requestNew",
                             "5748", "2222222");
        Commit resultEmpty = MetadataXMLReader.NewCommit(dDoc);
        assertNotNull(resultEmpty);
            
        //not empty tags
        dDoc = CreateTestXML("http://www.alert-project.eu/kesi",
                             "KESI", "Metadata.commit.requestNew",
                             "5748", "1111111");
        Commit result = MetadataXMLReader.NewCommit(dDoc);
        assertNotNull(result);
        
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
    /**
     * Test of NewForumPostData method, of class MetadataXMLReader.
     */
    @Test
    public void testNewForumPostData() {
        System.out.println("* MetadataXMLReaderTest: NewForumPostData");

        //no tags
        Document dDoc = CreateTestXML("http://www.alert-project.eu/keui",
                                      "KEUI", "Metadata.forumPost.requestNew",
                                      "5748", "000000000");
        ForumPost resultNull = MetadataXMLReader.NewForumPostData(dDoc);
        assertNotNull(resultNull);
        
        //empty tags
        dDoc = CreateTestXML("http://www.alert-project.eu/keui",
                             "KEUI", "Metadata.forumPost.requestNew",
                             "5748", "222222222");
        ForumPost resultEmpty = MetadataXMLReader.NewForumPostData(dDoc);
        assertNotNull(resultEmpty);
            
        //not empty tags
        dDoc = CreateTestXML("http://www.alert-project.eu/keui",
                             "KEUI", "Metadata.forumPost.requestNew",
                             "5748", "111111111");
        ForumPost result = MetadataXMLReader.NewForumPostData(dDoc);
        assertNotNull(result);
        
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
    /**
     * Test of NewUpdateIssueAnnotation method, of class MetadataXMLReader.
     */
    @Test
    public void testNewIssueAnnotation() {
        System.out.println("* MetadataXMLReaderTest: NewIssueAnnotation");

        //no tags
        Document dDoc = CreateTestXML("http://www.alert-project.eu/keui",
                                      "KEUI", "Metadata.issue.requestAnnotation",
                                      "5748", "00000");
        AnnotationData resultNull = MetadataXMLReader.NewUpdateIssueAnnotation(dDoc, true);
        assertNotNull(resultNull);
        
        //empty tags
        dDoc = CreateTestXML("http://www.alert-project.eu/keui",
                             "KEUI", "Metadata.issue.requestAnnotation",
                             "5748", "22222");
        AnnotationData resultEmpty = MetadataXMLReader.NewUpdateIssueAnnotation(dDoc, true);
        assertNotNull(resultEmpty);
            
        //not empty tags
        dDoc = CreateTestXML("http://www.alert-project.eu/keui",
                             "KEUI", "Metadata.issue.requestAnnotation",
                             "5748", "11111");
        AnnotationData result = MetadataXMLReader.NewUpdateIssueAnnotation(dDoc, true);
        assertNotNull(result);
        
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
    /**
     * Test of NewCommentAnnotation method, of class MetadataXMLReader.
     */
    @Test
    public void testNewCommentAnnotation() {
        System.out.println("* MetadataXMLReaderTest: NewCommentAnnotation");

        //no tags
        Document dDoc = CreateTestXML("http://www.alert-project.eu/keui",
                                      "KEUI", "Metadata.comment.requestAnnotation",
                                      "5748", "00000");
        AnnotationData resultNull = MetadataXMLReader.NewUpdateIssueAnnotation(dDoc, false);
        assertNotNull(resultNull);
        
        //empty tags
        dDoc = CreateTestXML("http://www.alert-project.eu/keui",
                             "KEUI", "Metadata.comment.requestAnnotation",
                             "5748", "22222");
        AnnotationData resultEmpty = MetadataXMLReader.NewUpdateIssueAnnotation(dDoc, false);
        assertNotNull(resultEmpty);
            
        //not empty tags
        dDoc = CreateTestXML("http://www.alert-project.eu/keui",
                             "KEUI", "Metadata.comment.requestAnnotation",
                             "5748", "11111");
        AnnotationData result = MetadataXMLReader.NewUpdateIssueAnnotation(dDoc, false);
        assertNotNull(result);
        
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
    /**
     * Test of NewCommitAnnotation method, of class MetadataXMLReader.
     */
    @Test
    public void testNewCommitAnnotation() {
        System.out.println("* MetadataXMLReaderTest: NewCommitAnnotation");

        //no tags
        Document dDoc = CreateTestXML("http://www.alert-project.eu/keui",
                                      "KEUI", "Metadata.commit.requestAnnotation",
                                      "5748", "00000");
        AnnotationData resultNull = MetadataXMLReader.NewCommitAnnotation(dDoc);
        assertNotNull(resultNull);
        
        //empty tags
        dDoc = CreateTestXML("http://www.alert-project.eu/keui",
                             "KEUI", "Metadata.commit.requestAnnotation",
                             "5748", "22222");
        AnnotationData resultEmpty = MetadataXMLReader.NewCommitAnnotation(dDoc);
        assertNotNull(resultEmpty);
            
        //not empty tags
        dDoc = CreateTestXML("http://www.alert-project.eu/keui",
                             "KEUI", "Metadata.commit.requestAnnotation",
                             "5748", "11111");
        AnnotationData result = MetadataXMLReader.NewCommitAnnotation(dDoc);
        assertNotNull(result);
        
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
    /**
     * Test of NewForumPostAnnotation method, of class MetadataXMLReader.
     */
    @Ignore
    @Test
    public void testNewForumPostAnnotation() {
        System.out.println("* MetadataXMLReaderTest: NewFWPostAnnotation");

        //Forum post
        //no tags
        Document dDoc = CreateTestXML("http://www.alert-project.eu/keui",
                                      "KEUI", "Metadata.forumPost.requestAnnotation",
                                      "5748", "00000");
        AnnotationData resultNull = MetadataXMLReader.NewForumPostAnnotation(dDoc);
        assertNotNull(resultNull);
        
        //empty tags
        dDoc = CreateTestXML("http://www.alert-project.eu/keui",
                             "KEUI", "Metadata.forumPost.requestAnnotation",
                             "5748", "22222");
        AnnotationData resultEmpty = MetadataXMLReader.NewForumPostAnnotation(dDoc);
        assertNotNull(resultEmpty);
            
        //not empty tags
        dDoc = CreateTestXML("http://www.alert-project.eu/keui",
                             "KEUI", "Metadata.forumPost.requestAnnotation",
                             "5748", "11111");
        AnnotationData result = MetadataXMLReader.NewForumPostAnnotation(dDoc);
        assertNotNull(result);

        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
    /**
     * Test of NewMailAnnotation method, of class MetadataXMLReader.
     */
    @Ignore
    @Test
    public void NewMailAnnotation() {
        System.out.println("* MetadataXMLReaderTest: NewMailAnnotation");

        //no tags
        Document dDoc = CreateTestXML("http://www.alert-project.eu/keui",
                                      "KEUI", "Metadata.mail.requestAnnotation",
                                      "5748", "00000");
        AnnotationData resultNull = MetadataXMLReader.NewMailAnnotation(dDoc);
        assertNotNull(resultNull);
        
        //empty tags
        dDoc = CreateTestXML("http://www.alert-project.eu/keui",
                             "KEUI", "Metadata.mail.requestAnnotation",
                             "5748", "22222");
        AnnotationData resultEmpty = MetadataXMLReader.NewMailAnnotation(dDoc);
        assertNotNull(resultEmpty);
            
        //not empty tags
        dDoc = CreateTestXML("http://www.alert-project.eu/keui",
                             "KEUI", "Metadata.mail.requestAnnotation",
                             "5748", "11111");
        AnnotationData result = MetadataXMLReader.NewMailAnnotation(dDoc);
        assertNotNull(result);
        
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
}
