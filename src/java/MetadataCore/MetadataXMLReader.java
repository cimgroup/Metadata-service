/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MetadataCore;

import MetadataObjects.Activity;
import MetadataObjects.Assigned;
import MetadataObjects.Attachment;
import MetadataObjects.Blocker;
import MetadataObjects.Bug;
import MetadataObjects.Closed;
import MetadataObjects.Comment;
import MetadataObjects.Component;
import MetadataObjects.ComputerSystem;
import MetadataObjects.Critical;
import MetadataObjects.Duplicate;
import MetadataObjects.Fixed;
import MetadataObjects.Invalid;
import MetadataObjects.Issue;
import MetadataObjects.Later;
import MetadataObjects.Major;
import MetadataObjects.Milestone;
import MetadataObjects.Minor;
import MetadataObjects.Open;
import MetadataObjects.Priority;
import MetadataObjects.Product;
import MetadataObjects.Remind;
import MetadataObjects.Resolved;
import MetadataObjects.ThirdParty;
import MetadataObjects.Trivial;
import MetadataObjects.Verified;
import MetadataObjects.WontFix;
import MetadataObjects.WorksForMe;
import MetadataObjects.doap_Project;
import MetadataObjects.foaf_Person;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author ivano
 */
public class MetadataXMLReader {

    // <editor-fold desc="Members">

    // </editor-fold>

    // <editor-fold desc="Methods">

    /**
     * @summary Method for reading event type ftom XML file
     * @startRealisation Ivan Obradovic 17.06.2011.
     * @finalModification Sasa Stojanovic 01.09.2011.
     * @param dDoc - input XML document to read
     */
    public static void ReadXML(Document dDoc)
    {
        try
        {
            String sEventName = "";

            NodeList nlMeta = dDoc.getElementsByTagName("ns1:" + MetadataConstants.c_XMLE_meta);   //getting node for tag meta

            if (nlMeta != null && nlMeta.getLength() > 0)
            {
                Element eMeta = (Element) nlMeta.item(0);
                sEventName = GetValue(eMeta, "ns1:" + MetadataConstants.c_XMLE_eventName);   //load name of eventName
            }

            CallAction(sEventName, dDoc);   //call method which will deside what to do based on event name
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * @summary Method for calling appropriate method based on event type
     * @startRealisation Sasa Stojanovic 23.06.2011.
     * @finalModification Sasa Stojanovic 23.06.2011.
     * @param sEventName  - event type
     * @param dDoc - input XML document to read
     */
    private static void CallAction(String sEventName, Document dDoc)
    {
        try
        {
            if(sEventName.equals(MetadataConstants.c_ET_newIssueRequest))   //if event type is new bug event
            {
                NewIssue(dDoc);
            }
            if(sEventName.equals(MetadataConstants.c_ET_newPersonRequest))   //if event type is new person
            {
                NewPerson(dDoc);
            }
            if(sEventName.equals(MetadataConstants.c_ET_APICallRequest))   //if event type is new person
            {
                APICallRequest(dDoc);
            }
            if(sEventName.equals(MetadataConstants.c_ET_MemberRequest))   //if event type is new person
            {
                InstanceRequest(dDoc);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    // <editor-fold desc="Event handler methods">

//    /**
//     * @summary Method for reading new bug event from XML
//     * @startRealisation Sasa Stojanovic 23.06.2011.
//     * @finalModification Sasa Stojanovic 23.06.2011.
//     * @param dDoc - input XML document to read
//     */
//    private static void NewBugEvent(Document dDoc)
//    {
//        try
//        {
//            String sBugID = "";
//            String sBugName = "";
//            URI uriBugURI = URI.create("");
//            String sBugStatusDescription = "";
//            String sBugIsAboutDescription = "";
//            int iBugIsAboutImportance = -1;
//            String sBugIsAboutSeverity = "";
//
//            NodeList nlEventType = dDoc.getElementsByTagName(MetadataConstants.c_XMLE_eventType);   //getting node for tag eventType
//
//            if (nlEventType != null && nlEventType.getLength() > 0)
//            {
//                Element eEventType = (Element) nlEventType.item(0);
//
//                NodeList nlProperty = eEventType.getElementsByTagName(MetadataConstants.c_XMLE_ontoProperty);   //getting node for tag <onto:property>
//
//                if (nlProperty != null && nlProperty.getLength() > 0)
//                {
//                    Element eProperty = (Element) nlProperty.item(0);
//                    OntoProperty oBug = ReadOntoProperty(eProperty);    //read all XML data to oBug object
//
//                    uriBugURI = URI.create(oBug.oClass.sValue);
//
//                    for (int i = 0; i < oBug.oClass.oProperties.size(); i++)
//                    {
//                        if (oBug.oClass.oProperties.get(i).sName.equals(MetadataConstants.c_XMLE_NewBugEvent_hasID))
//                        {
//                            sBugID = oBug.oClass.oProperties.get(i).sValue;
//                        }
//                        if (oBug.oClass.oProperties.get(i).sName.equals(MetadataConstants.c_XMLE_NewBugEvent_hasName))
//                        {
//                            sBugName = oBug.oClass.oProperties.get(i).sValue;
//                        }
//                        if (oBug.oClass.oProperties.get(i).sName.equals(MetadataConstants.c_XMLE_NewBugEvent_hasStatus))
//                        {
//                            if (oBug.oClass.oProperties.get(i).oClass.oProperties.size() > 0
//                                && oBug.oClass.oProperties.get(i).oClass.oProperties.get(0).sName.equals(MetadataConstants.c_XMLE_NewBugEvent_hasDescription))
//                            {
//                                sBugStatusDescription = oBug.oClass.oProperties.get(i).oClass.oProperties.get(0).sValue;
//                            }
//                        }
//                        if (oBug.oClass.oProperties.get(i).sName.equals(MetadataConstants.c_XMLE_NewBugEvent_isAbout))
//                        {
//                            for (int j = 0; j < oBug.oClass.oProperties.get(i).oClass.oProperties.size(); j++)
//                            {
//                                if (oBug.oClass.oProperties.get(i).oClass.oProperties.get(j).sName.equals(MetadataConstants.c_XMLE_NewBugEvent_hasDescription))
//                                {
//                                    sBugIsAboutDescription = oBug.oClass.oProperties.get(i).oClass.oProperties.get(j).sValue;
//                                }
//                                if (oBug.oClass.oProperties.get(i).oClass.oProperties.get(j).sName.equals(MetadataConstants.c_XMLE_NewBugEvent_hasImportance))
//                                {
//                                    iBugIsAboutImportance = Integer.parseInt(oBug.oClass.oProperties.get(i).oClass.oProperties.get(j).sValue);
//                                }
//                                if (oBug.oClass.oProperties.get(i).oClass.oProperties.get(j).sName.equals(MetadataConstants.c_XMLE_NewBugEvent_hasSeverity))
//                                {
//                                    sBugIsAboutSeverity = oBug.oClass.oProperties.get(i).oClass.oProperties.get(j).sValue;
//                                }
//                            }
//                        }
//                    }
//                    MetadataModel.SaveObjectNewIssue(sBugID, sBugName, uriBugURI, sBugStatusDescription, sBugIsAboutDescription, iBugIsAboutImportance, sBugIsAboutSeverity);
//                }
//            }
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//    }

//    /**
//     * @summary Method for reading query bug person event from XML
//     * @startRealisation Sasa Stojanovic 23.06.2011.
//     * @finalModification Sasa Stojanovic 23.06.2011.
//     * @param dDoc - input XML document to read
//     */
//    private static void QueryBugPerson(Document dDoc) {
//        try
//        {
//            ArrayList <String> arBugIDs = new ArrayList<String>();
//
//            NodeList nlEventType = dDoc.getElementsByTagName(MetadataConstants.c_XMLE_eventType);   //getting node for tag eventType
//
//            if (nlEventType != null && nlEventType.getLength() > 0)
//            {
//                Element eEventType = (Element) nlEventType.item(0);
//
//                NodeList nlProperty = eEventType.getElementsByTagName(MetadataConstants.c_XMLE_ontoProperty);   //getting node for tag <onto:property>
//
//                if (nlProperty != null)
//                {
//                    for (int i = 0; i < nlProperty.getLength(); i++)
//                    {
//                        Node nEventType = nlEventType.item(0);
//                        Node nProperty = nlProperty.item(i);
//
//                        if (ChildNode(nEventType, nProperty))
//                        {
//                            Element eProperty = (Element) nlProperty.item(i);
//                            OntoProperty oQuery = ReadOntoProperty(eProperty);    //read all XML data to oBug object
//
//                            for (int j = 0; j < oQuery.oClass.oProperties.size(); j++)
//                            {
//                                if (oQuery.oClass.oProperties.get(j).sName.equals(MetadataConstants.c_XMLE_QueryBugPerson_hasID))
//                                {
//                                    arBugIDs.add(oQuery.oClass.oProperties.get(j).sValue);
//                                }
//                            }
//                        }
//                    }
//                    MetadataModel.SearchForIDs(MetadataConstants.c_ET_QueryBugPerson, arBugIDs);
//                }
//            }
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//    }

    /**
     * @summary Method for reading new bug event from XML
     * @startRealisation Sasa Stojanovic 01.09.2011.
     * @finalModification Sasa Stojanovic 01.09.2011.
     * @param dDoc - input XML document to read
     */
    private static void NewIssue(Document dDoc)
    {
        try
        {
            String sEventId = GetEventId(dDoc);
            sEventId = sEventId.replaceFirst("Request", "");    //deleting Request string from id

            Issue oIssue = MetadataObjectFactory.CreateNewIssue();

            NodeList nlIssue = dDoc.getElementsByTagName("s:" + MetadataConstants.c_XMLE_issue);   //getting node for tag issue

            if (nlIssue != null && nlIssue.getLength() > 0)
            {
                Element eIssue = (Element) nlIssue.item(0);

                oIssue.m_sID = GetValue(eIssue, "s:" + MetadataConstants.c_XMLE_issue + MetadataConstants.c_XMLE_Id);

                NodeList nlReporter = eIssue.getElementsByTagName("s:" + MetadataConstants.c_XMLE_issueAuthor);   //getting node for tag issueAuthor
                if (nlReporter != null && nlReporter.getLength() > 0)
                {
                    Element eReporter = (Element) nlReporter.item(0);
                    oIssue.m_oHasReporter = GetPersonObject(eReporter);
                    //oIssue.m_oHasReporter.m_oIsReporterOf = new Issue[1];
                    //oIssue.m_oHasReporter.m_oIsReporterOf[0] = oIssue;
                }
                
                String sBugState = GetValue(eIssue, "s:" + MetadataConstants.c_XMLE_issueStatus);
                if (sBugState.equals("Assigned"))
                    oIssue.m_oHasState = new Assigned();
                if (sBugState.equals("Open"))
                    oIssue.m_oHasState = new Open();
                if (sBugState.equals("Verified"))
                    oIssue.m_oHasState = new Verified();
                if (sBugState.equals("Resolved"))
                    oIssue.m_oHasState = new Resolved();
                if (sBugState.equals("Closed"))
                    oIssue.m_oHasState = new Closed();

                //if (oIssue.m_oHasState != null)
                //    oIssue.m_oHasState.m_oIsStateOf = oIssue;

                String sBugResolution = GetValue(eIssue, "s:" + MetadataConstants.c_XMLE_issueResolution);
                if (sBugResolution.equals("Duplicate"))
                    oIssue.m_oHasResolution = new Duplicate();
                if (sBugResolution.equals("Fixed"))
                    oIssue.m_oHasResolution = new Fixed();
                if (sBugResolution.equals("Invalid"))
                    oIssue.m_oHasResolution = new Invalid();
                if (sBugResolution.equals("ThirdParty"))
                    oIssue.m_oHasResolution = new ThirdParty();
                if (sBugResolution.equals("WontFix"))
                    oIssue.m_oHasResolution = new WontFix();
                if (sBugResolution.equals("WorksForMe"))
                    oIssue.m_oHasResolution = new WorksForMe();
                if (sBugResolution.equals("Later"))
                    oIssue.m_oHasResolution = new Later();
                if (sBugResolution.equals("Remind"))
                    oIssue.m_oHasResolution = new Remind();

                oIssue.m_sDescription = GetValue(eIssue, "s:" + MetadataConstants.c_XMLE_issueDescription);

                oIssue.m_sKeyword = GetValue(eIssue, "s:" + MetadataConstants.c_XMLE_issueKeyword);

                NodeList nlProduct = eIssue.getElementsByTagName("s:" + MetadataConstants.c_XMLE_issueProduct);   //getting node for tag bugProduct
                if (nlProduct != null && nlProduct.getLength() > 0)
                {
                    Element eProduct = (Element) nlProduct.item(0);

                    oIssue.m_oIsIssueOf = new Component();
                    oIssue.m_oIsIssueOf.m_sID = GetValue(eProduct, "s:" + MetadataConstants.c_XMLE_productComponent + MetadataConstants.c_XMLE_Id);
                    oIssue.m_oIsIssueOf.m_oIsComponentOf = new Product();
                    oIssue.m_oIsIssueOf.m_oIsComponentOf.m_sID = GetValue(eProduct, "s:" + MetadataConstants.c_XMLE_product + MetadataConstants.c_XMLE_Id);
                    oIssue.m_oIsIssueOf.m_oIsComponentOf.m_sVersion = GetValue(eProduct, "s:" + MetadataConstants.c_XMLE_productVersion);
                }

                NodeList nlComputerSystem = eIssue.getElementsByTagName("s:" + MetadataConstants.c_XMLE_issueComputerSystem);   //getting node for tag bugComputerSystem
                if (nlComputerSystem != null && nlComputerSystem.getLength() > 0)
                {
                    Element eComputerSystem = (Element) nlComputerSystem.item(0);

                    oIssue.m_oHasComputerSystem = new ComputerSystem();
                    oIssue.m_oHasComputerSystem.m_sID = GetValue(eComputerSystem, "s:" + MetadataConstants.c_XMLE_computerSystem + MetadataConstants.c_XMLE_Id);
                    oIssue.m_oHasComputerSystem.m_sPlatform = GetValue(eComputerSystem, "s:" + MetadataConstants.c_XMLE_computerSystemPlatform);
                    oIssue.m_oHasComputerSystem.m_sOs = GetValue(eComputerSystem, "s:" + MetadataConstants.c_XMLE_computerSystemOS);
                    //oIssue.m_oHasComputerSystem.m_oIsComputerSystemOf = new Issue[1];
                    //oIssue.m_oHasComputerSystem.m_oIsComputerSystemOf[0] = oIssue;
                }

                oIssue.m_oHasPriority = new Priority();
                if (!GetValue(eIssue, "s:" + MetadataConstants.c_XMLE_issuePriority).isEmpty())
                    oIssue.m_oHasPriority.m_iPriority = Integer.parseInt(GetValue(eIssue, "s:" + MetadataConstants.c_XMLE_issuePriority));


                String sBugSeverity = GetValue(eIssue, "s:" + MetadataConstants.c_XMLE_issueSeverity);
                if (sBugSeverity.equals("Blocker"))
                    oIssue.m_oHasSeverity = new Blocker();
                if (sBugSeverity.equals("Critical"))
                    oIssue.m_oHasSeverity = new Critical();
                if (sBugSeverity.equals("Major"))
                    oIssue.m_oHasSeverity = new Major();
                if (sBugSeverity.equals("Minor"))
                    oIssue.m_oHasSeverity = new Minor();
                if (sBugSeverity.equals("Trivial"))
                    oIssue.m_oHasSeverity = new Trivial();

                NodeList nlAssignee = eIssue.getElementsByTagName("s:" + MetadataConstants.c_XMLE_issueAssignedTo);   //getting node for tag issueAssignedTo
                if (nlAssignee != null && nlAssignee.getLength() > 0)
                {
                    Element eAssignee = (Element) nlAssignee.item(0);
                    oIssue.m_oHasAssignee = GetPersonObject(eAssignee);
                    //oIssue.m_oHasAssignee.m_oIsAssigneeOf = new Issue[1];
                    //oIssue.m_oHasAssignee.m_oIsAssigneeOf[0] = oIssue;
                }

                NodeList nlCCPerson = eIssue.getElementsByTagName("s:" + MetadataConstants.c_XMLE_issueCCPerson);
                if (nlCCPerson != null && nlCCPerson.getLength() > 0)
                {
                    oIssue.m_oHasCCPerson = new foaf_Person[nlCCPerson.getLength()];
                    for (int i = 0; i < nlCCPerson.getLength(); i++)
                    {
                        Element eCCPerson = (Element)nlCCPerson.item(i);
                        oIssue.m_oHasCCPerson[i] = GetPersonObject(eCCPerson);
                        //oIssue.m_oHasCCPerson[i].m_oIsCcPersonOf = new Issue[1];
                        //oIssue.m_oHasCCPerson[i].m_oIsCcPersonOf[0] = oIssue;
                    }
                }

                oIssue.m_sBugURL = GetValue(eIssue, "s:" + MetadataConstants.c_XMLE_issueUrl);

                oIssue.m_oDependsOn = new Issue();
                oIssue.m_oDependsOn.m_sID = GetValue(eIssue, "s:" + MetadataConstants.c_XMLE_issueDependsOn + MetadataConstants.c_XMLE_Id);

                oIssue.m_oBlocks = new Issue();
                oIssue.m_oBlocks.m_sID = GetValue(eIssue, "s:" + MetadataConstants.c_XMLE_issueBlocks + MetadataConstants.c_XMLE_Id);

                oIssue.m_oIsDuplicateOf = new Issue();
                oIssue.m_oIsDuplicateOf.m_sID = GetValue(eIssue, "s:" + MetadataConstants.c_XMLE_issueDuplicateOf + MetadataConstants.c_XMLE_Id);

                oIssue.m_oIsMergedInto = new Issue();
                oIssue.m_oIsMergedInto.m_sID = GetValue(eIssue, "s:" + MetadataConstants.c_XMLE_issueMergedInto + MetadataConstants.c_XMLE_Id);

                oIssue.m_dtmDateOpened = MetadataGlobal.GetDateTime(GetValue(eIssue, "s:" + MetadataConstants.c_XMLE_issueDateOpened));

                oIssue.m_dtmLastModified = MetadataGlobal.GetDateTime(GetValue(eIssue, "s:" + MetadataConstants.c_XMLE_issueLastModified));

                NodeList nlMilestone = eIssue.getElementsByTagName("s:" + MetadataConstants.c_XMLE_issueMilestone);   //getting node for tag bugMilestone
                if (nlMilestone != null && nlMilestone.getLength() > 0)
                {
                    Element eMilestone = (Element) nlMilestone.item(0);
                    oIssue.m_oHasMilestone = new Milestone();
                    oIssue.m_oHasMilestone.m_sID = GetValue(eMilestone, "s:" + MetadataConstants.c_XMLE_milestone + MetadataConstants.c_XMLE_Id);
                    oIssue.m_oHasMilestone.m_dtmTarget = MetadataGlobal.GetDateTime(GetValue(eMilestone, "s:" + MetadataConstants.c_XMLE_milestoneTarget));
                    //oIssue.m_oHasMilestone.m_oIsMilestoneOf = oIssue;
                }

                NodeList nlComment = eIssue.getElementsByTagName("s:" + MetadataConstants.c_XMLE_issueComment);
                if (nlComment != null && nlComment.getLength() > 0)
                {
                    oIssue.m_oHasComment = new Comment[nlComment.getLength()];
                    for (int i = 0; i < nlComment.getLength(); i++)
                    {
                        Element eComment = (Element)nlComment.item(i);
                        oIssue.m_oHasComment[i] = new Comment();
                        oIssue.m_oHasComment[i].m_sID = GetValue(eComment, "s:" + MetadataConstants.c_XMLE_comment + MetadataConstants.c_XMLE_Id);
                        if (!GetValue(eComment, "s:" + MetadataConstants.c_XMLE_commentNumber).isEmpty())
                            oIssue.m_oHasComment[i].m_iNumber = Integer.parseInt(GetValue(eComment, "s:" + MetadataConstants.c_XMLE_commentNumber));
                        oIssue.m_oHasComment[i].m_sText = GetValue(eComment, "s:" + MetadataConstants.c_XMLE_commentText);
                        
                        NodeList nlCommentor = eComment.getElementsByTagName("s:" + MetadataConstants.c_XMLE_commentPerson);   //getting node for tag issueAuthor
                        if (nlCommentor != null && nlCommentor.getLength() > 0)
                        {
                            Element eCommentor = (Element) nlCommentor.item(0);
                            oIssue.m_oHasComment[i].m_oHasCommentor = GetPersonObject(eCommentor);
                            //oIssue.m_oHasComment[i].m_oHasCommentor.m_oIsCommentorOf = new Comment[1];
                            //oIssue.m_oHasComment[i].m_oHasCommentor.m_oIsCommentorOf[0] = oIssue.m_oHasComment[i];
                        }
                        
                        oIssue.m_oHasComment[i].m_dtmDate = MetadataGlobal.GetDateTime(GetValue(eComment, "s:" + MetadataConstants.c_XMLE_commentDate));
                        //oIssue.m_oHasComment[i].m_oIsCommentOf = oIssue;
                    }
                }

                NodeList nlAttachment = eIssue.getElementsByTagName("s:" + MetadataConstants.c_XMLE_issueAttachment);
                if (nlAttachment != null && nlAttachment.getLength() > 0)
                {
                    oIssue.m_oHasAttachment = new Attachment[nlAttachment.getLength()];
                    for (int i = 0; i < nlAttachment.getLength(); i++)
                    {
                        Element eAttachment = (Element)nlAttachment.item(i);
                        oIssue.m_oHasAttachment[i] = new Attachment();
                        oIssue.m_oHasAttachment[i].m_sID = GetValue(eAttachment, "s:" + MetadataConstants.c_XMLE_attachment + MetadataConstants.c_XMLE_Id);
                        oIssue.m_oHasAttachment[i].m_sFilename = GetValue(eAttachment, "s:" + MetadataConstants.c_XMLE_attachmentFilename);
                        oIssue.m_oHasAttachment[i].m_sType = GetValue(eAttachment, "s:" + MetadataConstants.c_XMLE_attachmentType);
                        
                        NodeList nlCreator = eAttachment.getElementsByTagName("s:" + MetadataConstants.c_XMLE_attachmentCreator);   //getting node for tag attachmentCreator
                        if (nlCreator != null && nlCreator.getLength() > 0)
                        {
                            Element eCreator = (Element) nlCreator.item(0);
                            oIssue.m_oHasAttachment[i].m_oHasCreator = GetPersonObject(eCreator);
                        }
                        
                        //oIssue.m_oHasAttachment[i].m_oIsAttachmentOf = oIssue;
                    }
                }

                NodeList nlActivity = eIssue.getElementsByTagName("s:" + MetadataConstants.c_XMLE_issueActivity);
                if (nlActivity != null && nlActivity.getLength() > 0)
                {
                    oIssue.m_oHasActivity = new Activity[nlActivity.getLength()];
                    for (int i = 0; i < nlActivity.getLength(); i++)
                    {
                        Element eActivity = (Element)nlActivity.item(i);
                        oIssue.m_oHasActivity[i] = new Activity();
                        oIssue.m_oHasActivity[i].m_sID = GetValue(eActivity, "s:" + MetadataConstants.c_XMLE_activity + MetadataConstants.c_XMLE_Id);
                        oIssue.m_oHasActivity[i].m_oHasInvolvedPerson = new foaf_Person();
                        oIssue.m_oHasActivity[i].m_oHasInvolvedPerson.m_sID = GetValue(eActivity, "s:" + MetadataConstants.c_XMLE_activityWho);
                        oIssue.m_oHasActivity[i].m_dtmWhen = MetadataGlobal.GetDateTime(GetValue(eActivity, "s:" + MetadataConstants.c_XMLE_activityWhen));
                        oIssue.m_oHasActivity[i].m_sWhat = GetValue(eActivity, "s:" + MetadataConstants.c_XMLE_activityWhat);
                        oIssue.m_oHasActivity[i].m_sRemoved = GetValue(eActivity, "s:" + MetadataConstants.c_XMLE_activityRemoved);
                        oIssue.m_oHasActivity[i].m_sAdded = GetValue(eActivity, "s:" + MetadataConstants.c_XMLE_activityAdded);
                        //oIssue.m_oHasActivity[i].m_oIsActivityOf = oIssue;
                    }
                }

                NodeList nlIssueTracker = eIssue.getElementsByTagName("s:" + MetadataConstants.c_XMLE_issueTracker);
                if (nlIssueTracker != null && nlIssueTracker.getLength() > 0)
                {
                    for (int i = 0; i < nlIssueTracker.getLength(); i++)
                    {
                        Element eIssueTracker = (Element)nlIssueTracker.item(i);
                        //dodati snimanje za issuetracker
                    }
                }
            }

            MetadataModel.SaveObjectNewIssue(sEventId, oIssue);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * @summary Method for reading new person event from XML
     * @startRealisation Sasa Stojanovic 05.09.2011.
     * @finalModification Sasa Stojanovic 05.09.2011.
     * @param dDoc - input XML document to read
     */
    private static void NewPerson(Document dDoc)
    {
        try
        {
            String sEventId = GetEventId(dDoc);
            sEventId = sEventId.replaceFirst("Request", "");    //deleting Request string from id

            foaf_Person oPerson = MetadataObjectFactory.CreateNewPerson();

            NodeList nlPerson = dDoc.getElementsByTagName("ns1:" + MetadataConstants.c_XMLE_person);   //getting node for tag person

            if (nlPerson != null && nlPerson.getLength() > 0)
            {
                Element ePerson = (Element) nlPerson.item(0);

                oPerson.m_sFirstName = GetValue(ePerson, "ns1:" + MetadataConstants.c_XMLE_personFirstName);
                oPerson.m_sLastName = GetValue(ePerson, "ns1:" + MetadataConstants.c_XMLE_personLastName);
                oPerson.m_sGender = GetValue(ePerson, "ns1:" + MetadataConstants.c_XMLE_personGender);
            }

            MetadataModel.SaveObjectNewPerson(sEventId, oPerson);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * @summary Method for reading new api request event from XML
     * @startRealisation Sasa Stojanovic 06.09.2011.
     * @finalModification Sasa Stojanovic 06.09.2011.
     * @param dDoc - input XML document to read
     */
    private static void APICallRequest(Document dDoc) {
        try
        {
            String sEventId = GetEventId(dDoc);
            sEventId = sEventId.replaceFirst("Request", "");    //deleting Request string from id


            NodeList nlAPIRequest = dDoc.getElementsByTagName("ns1:" + MetadataConstants.c_XMLE_apirequest);   //getting node for apirequest

            if (nlAPIRequest != null && nlAPIRequest.getLength() > 0)
            {
                Element eAPIRequest = (Element) nlAPIRequest.item(0);

                String sAPICall = GetValue(eAPIRequest, "s2:" + MetadataConstants.c_XMLE_apiCall);

                if (sAPICall.equals(MetadataConstants.c_XMLAC_getAllMembers))
                {
                    String sOntClass = "";
                            
                    NodeList nlInputParameter = dDoc.getElementsByTagName("s2:" + MetadataConstants.c_XMLE_inputParameter);   //getting node for apirequest

                    if (nlInputParameter != null && nlInputParameter.getLength() > 0)
                    {
                        for (int i = 0; i < nlInputParameter.getLength(); i++)
                        {
                            Element eInputParameter = (Element) nlInputParameter.item(i);
                            String sParamName = GetValue(eInputParameter, "s2:" + MetadataConstants.c_XMLE_name);
                            if (sParamName.equals(MetadataConstants.c_XMLV_OntClass))
                                sOntClass = GetValue(eInputParameter, "s2:" + MetadataConstants.c_XMLE_value);
                        }
                    }
                    
                    MetadataModel.ac_GetAllIndividuals(sEventId, sOntClass);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    
    /**
     * @summary Method for reading new instance request event from XML
     * @startRealisation Sasa Stojanovic 06.09.2011.
     * @finalModification Sasa Stojanovic 06.09.2011.
     * @param dDoc - input XML document to read
     */
    private static void InstanceRequest(Document dDoc) {
        try
        {
            String sEventId = GetEventId(dDoc);
            sEventId = sEventId.replaceFirst("Request", "");    //deleting Request string from id
            
            String sMemberURL = "";
            
            NodeList nlInputParameter = dDoc.getElementsByTagName("o:" + MetadataConstants.c_XMLE_inputParameter);   //getting node for inputParameter

            if (nlInputParameter != null && nlInputParameter.getLength() > 0)
            {
                for (int i = 0; i < nlInputParameter.getLength(); i++)
                {
                    Element eInputParameter = (Element) nlInputParameter.item(i);
                    String sParamName = GetValue(eInputParameter, "o:" + MetadataConstants.c_XMLE_name);
                    if (sParamName.equals(MetadataConstants.c_XMLV_MemberURL))
                        sMemberURL = GetValue(eInputParameter, "o:" + MetadataConstants.c_XMLE_value);
                }
            }

            MetadataModel.GetInstance(sEventId, sMemberURL);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    // </editor-fold>

    // <editor-fold desc="XML reading methods">

    /**
     * @summary Method for reading <onto:property> tag into class
     * @startRealisation Sasa Stojanovic 23.06.2011.
     * @finalModification Sasa Stojanovic 23.06.2011.
     * @param eElement - <onto:property> element to store to class
     * @return OntoProperty object
     */
    private static OntoProperty ReadOntoProperty(Element eOntoProperty)
    {
        OntoProperty oOntoProperty = new OntoProperty();
        try
        {
            oOntoProperty.sName = GetValue(eOntoProperty, MetadataConstants.c_XMLE_name);
            oOntoProperty.sTypeOf = GetValue(eOntoProperty, MetadataConstants.c_XMLE_typeOf);
            oOntoProperty.sValue = GetValue(eOntoProperty, MetadataConstants.c_XMLE_value);

            NodeList nlClass = eOntoProperty.getElementsByTagName(MetadataConstants.c_XMLE_class);

            if (nlClass != null && nlClass.getLength() > 0)
            {
                Element eClass = (Element) nlClass.item(0);

                ArrayList<OntoProperty> oProperties = new ArrayList<OntoProperty>();

                NodeList nlProperties = eClass.getElementsByTagName(MetadataConstants.c_XMLE_ontoProperty);

                for (int i = 0; i < nlProperties.getLength(); i++)
                {
                    Node nClass = nlClass.item(0);
                    Node nProperties = nlProperties.item(i);
                    if (ChildNode(nClass, nProperties))
                    {
                        Element eProperties = (Element) nlProperties.item(i);
                        oProperties.add(ReadOntoProperty(eProperties));
                    }
                }

                oOntoProperty.oClass.oProperties = oProperties;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return oOntoProperty;
    }

    /**
     * @summary Method for cheking if the node is the first level child of other node
     * @startRealisation Sasa Stojanovic 23.06.2011.
     * @finalModification Sasa Stojanovic 23.06.2011.
     * @param nParent - parent node
     * @param nChild - child node
     * @return true if node is first level child of other node
     */
    private static boolean ChildNode(Node nParent, Node nChild)
    {
        boolean bIsChild = false;
        try
        {
            NodeList nlChilds = nParent.getChildNodes();
            for (int i = 0; i < nlChilds.getLength(); i++)
            {
                Node nTestChild = nlChilds.item(i);
                if (nTestChild.equals(nChild))
                    bIsChild = true;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return bIsChild;
    }

    /**
     * @summary Method for returning tag value of element
     * @startRealisation Sasa Stojanovic 23.06.2011.
     * @finalModification Sasa Stojanovic 23.06.2011.
     * @param eElement - element to read
     * @param sTag - tag to read
     * @return value of element
     */
    private static String GetValue(Element eElement, String sTag)
    {
        String sElementName = "";

        ////this version doesn't require tag to be first level child
        //NodeList nlName = eElement.getElementsByTagName(sTag).item(0).getChildNodes();
        //if (nlName.getLength() > 0)
        //{
        //    Node nValue = (Node) nlName.item(0);
        //    sElementName = nValue.getNodeValue();
        //}

        NodeList nlName = eElement.getElementsByTagName(sTag);
	if (nlName != null && nlName.getLength() > 0)
        {
            Node nElement = (Node) eElement;
            Node nValue = (Node) nlName.item(0);
            if (ChildNode(nElement, nValue))
            {
                Element el = (Element)nlName.item(0);
                if (el.hasChildNodes())
                    sElementName = el.getFirstChild().getNodeValue();
            }
	}

        return sElementName;
    }

    /**
     * @summary Method for returning eventId
     * @startRealisation Sasa Stojanovic 01.09.2011.
     * @finalModification Sasa Stojanovic 01.09.2011.
     * @param dDoc - input XML document to read
     * @return - eventId
     */
    private static String GetEventId(Document dDoc)
    {
        String sEventId = "";
        NodeList nlMeta = dDoc.getElementsByTagName("ns1:" + MetadataConstants.c_XMLE_meta);   //getting node for tag meta

        if (nlMeta != null && nlMeta.getLength() > 0)
        {
            Element eMeta = (Element) nlMeta.item(0);
            sEventId = GetValue(eMeta, "ns1:" + MetadataConstants.c_XMLE_eventId);
        }

        return sEventId;
    }
    
    /**
     * @summary Method for storing person data into java object
     * @startRealisation Sasa Stojanovic 08.11.2011.
     * @finalModification Sasa Stojanovic 08.11.2011.
     * @param ePerson - input XML element for person
     * @return - foaf_Person object
     */
    private static foaf_Person GetPersonObject(Element ePerson)
    {
        foaf_Person oPerson = new foaf_Person();
        try
        {
            oPerson.m_sID = GetValue(ePerson, "s:" + MetadataConstants.c_XMLE_id);
            String sName = GetValue(ePerson, "s:" + MetadataConstants.c_XMLE_name);
            if (!sName.isEmpty() && sName.indexOf(" ") != -1)
            {
                oPerson.m_sFirstName = sName.substring(0, sName.indexOf(" "));
                oPerson.m_sLastName = sName.substring(sName.indexOf(" ") + 1);
            }
            else
            {
                oPerson.m_sFirstName = sName;
                oPerson.m_sLastName = "";
            }
            oPerson.m_sEmail = GetValue(ePerson, "s:" + MetadataConstants.c_XMLE_email);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return oPerson;
    }


    // </editor-fold>

    // </editor-fold>

    // <editor-fold desc="Classes">

    /**
     * @summary Class for storing information from <onto:property> element
     * @startRealisation Sasa Stojanovic 23.06.2011.
     * @finalModification Sasa Stojanovic 23.06.2011.
     */
    private static class OntoProperty
    {
        String sName;
        String sTypeOf;
        String sValue;
        OntoPropertyClass oClass = new OntoPropertyClass();
    }

    /**
     * @summary Class for storing class information about <onto:property> element
     * @startRealisation Sasa Stojanovic 23.06.2011.
     * @finalModification Sasa Stojanovic 23.06.2011.
     */
    private static class OntoPropertyClass
    {
        ArrayList <OntoProperty> oProperties;
    }

    // </editor-fold>
}
