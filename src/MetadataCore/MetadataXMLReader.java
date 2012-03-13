/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MetadataCore;

import MetadataCore.MetadataGlobal.AnnotationData;
import MetadataObjects.*;
import MetadataObjects.Comment;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import org.w3c.dom.*;

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
     * @startRealisation  Sasa Stojanovic     23.06.2011.
     * @finalModification Dejan Milosavljevic 20.02.2012.
     * @param sEventName  - event type
     * @param dDoc - input XML document to read
     */
    private static void CallAction(String sEventName, Document dDoc)
    {
        try
        {
            if(sEventName.equals(MetadataConstants.c_ET_ALERT_KESI_IssueNew))   //if event type is new issue event
            {
                NewUpdateIssue(dDoc, false);
            }
            if(sEventName.equals(MetadataConstants.c_ET_ALERT_KESI_IssueUpdate))   //if event type is update issue event
            {
                NewUpdateIssue(dDoc, true);
            }
            if(sEventName.equals(MetadataConstants.c_ET_ALERT_KESI_CommitNew))
            {
                NewCommit(dDoc);
            }
            if(sEventName.equals(MetadataConstants.c_ET_ALERT_MLSensor_MailNew))
            {
                NewMail(dDoc);
            }
//            if(sEventName.equals(MetadataConstants.c_ET_person_requestNew))   //if event type is new person
//            {
//                NewPerson(dDoc);
//            }
//            if(sEventName.equals(MetadataConstants.c_ET_APICall_request))   //if event type is API Call request
            if(sEventName.equals(MetadataConstants.c_ET_ALERT_KESI_APICallRequest) ||   //if event type is API Call request
               sEventName.equals(MetadataConstants.c_ET_ALERT_KEUI_APICallRequest) ||
               sEventName.equals(MetadataConstants.c_ET_ALERT_Stardom_APICallRequest) ||
               sEventName.equals(MetadataConstants.c_ET_ALERT_Panteon_APICallRequest))
            {
                APICallRequest(dDoc);
            }
//            if(sEventName.equals(MetadataConstants.c_ET_member_request))   //if event type is member request
//            {
//                InstanceRequest(dDoc);
//            }
            if(sEventName.equals(MetadataConstants.c_ET_ALERT_KEUI_IssueNew_Annotated)) //if event type is new issue annotation
            {
                NewIssueAnnotation(dDoc);
            }
            if(sEventName.equals(MetadataConstants.c_ET_ALERT_KEUI_CommentNew_Annotated)) //if event type is new comment annotation
            {
                NewCommentAnnotation(dDoc);
            }
            if(sEventName.equals(MetadataConstants.c_ET_ALERT_KEUI_CommitNew_Annotated)) //if event type is new commit annotation
            {
                NewCommitAnnotation(dDoc);
            }
            if(sEventName.equals(MetadataConstants.c_ET_ALERT_KEUI_ForumPostNew_Annotated)) //if event type is new forum post annotation
            {
                NewFWPostAnnotation(dDoc, true);
            }
            if(sEventName.equals(MetadataConstants.c_ET_ALERT_KEUI_WikiPostNew_Annotated)) //if event type is new wiki post annotation
            {
                NewFWPostAnnotation(dDoc, false);
            }
            if(sEventName.equals(MetadataConstants.c_ET_ALERT_KEUI_MailNew_Annotated)) //if event type is new mail annotation
            {
                NewMailAnnotation(dDoc);
            }
            if(sEventName.equals(MetadataConstants.c_ET_ALERT_ForumSensor_ForumPostNew)) //if event type is new forum post
            {
                NewForumPostData(dDoc);
            }
            if(sEventName.equals(MetadataConstants.c_ET_ALERT_Stardom_CompetencyNew))   //if event type is new competence event
            {
                NewUpdateCompetence(dDoc, false);
            }
            if(sEventName.equals(MetadataConstants.c_ET_ALERT_Stardom_CompetencyUpdate))   //if event type is update competence event
            {
                NewUpdateCompetence(dDoc, true);
            }
            if(sEventName.equals(MetadataConstants.c_ET_ALERT_Stardom_IdentityNew))   //if event type is new identity event
            {
                NewIdentity(dDoc);
            }
            if(sEventName.equals(MetadataConstants.c_ET_ALERT_Stardom_IdentityUpdate))   //if event type is update identity event
            {
                UpdateIdentity(dDoc);
            }
            if(sEventName.equals(MetadataConstants.c_ET_ALERT_Stardom_IdentityRemove))   //if event type is remove identity event
            {
                RemoveIdentity(dDoc);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    // <editor-fold desc="Event handler methods">


    /**
     * @summary Method for reading new/update issue event from XML
     * @startRealisation  Sasa Stojanovic     01.09.2011.
     * @finalModification Dejan Milosavljevic 20.02.2012.
     * @param dDoc - input XML document to read
     * @param bIsUpdate - marks if this is an update
     * @return - returns Issue object
     */
    public static Issue NewUpdateIssue(Document dDoc, boolean bIsUpdate)
    {
        try
        {
            String sEventId = GetEventId(dDoc);
            Element eOriginalData = null;  //element for original data

            Issue oIssue = MetadataObjectFactory.CreateNewIssue();

            NodeList nlIssue = dDoc.getElementsByTagName("s:" + MetadataConstants.c_XMLE_issue);   //getting node for tag issue

            if (nlIssue != null && nlIssue.getLength() > 0)
            {
                Element eIssue = (Element) nlIssue.item(0);
                eOriginalData = eIssue;

                oIssue.m_sID = GetValue(eIssue, "s:" + MetadataConstants.c_XMLE_issue + MetadataConstants.c_XMLE_Id);

                NodeList nlReporter = eIssue.getElementsByTagName("s:" + MetadataConstants.c_XMLE_issueAuthor);   //getting node for tag issueAuthor
                if (nlReporter != null && nlReporter.getLength() > 0)
                {
                    Element eReporter = (Element) nlReporter.item(0);
                    oIssue.m_oHasReporter = GetPersonObject("s:", eReporter);
                    //oIssue.m_oHasReporter.m_oIsReporterOf = new Issue[1];
                    //oIssue.m_oHasReporter.m_oIsReporterOf[0] = oIssue;
                }           
                
                String sIssueState = GetValue(eIssue, "s:" + MetadataConstants.c_XMLE_issueStatus);
                if (sIssueState.equalsIgnoreCase("Assigned"))
                    oIssue.m_oHasState = new Assigned();
                if (sIssueState.equalsIgnoreCase("Open"))
                    oIssue.m_oHasState = new Open();
                if (sIssueState.equalsIgnoreCase("Verified"))
                    oIssue.m_oHasState = new Verified();
                if (sIssueState.equalsIgnoreCase("Resolved"))
                    oIssue.m_oHasState = new Resolved();
                if (sIssueState.equalsIgnoreCase("Closed"))
                    oIssue.m_oHasState = new Closed();

                //if (oIssue.m_oHasState != null)
                //    oIssue.m_oHasState.m_oIsStateOf = oIssue;

                String sIssueResolution = GetValue(eIssue, "s:" + MetadataConstants.c_XMLE_issueResolution);
                if (sIssueResolution.equalsIgnoreCase("Duplicate"))
                    oIssue.m_oHasResolution = new Duplicate();
                if (sIssueResolution.equalsIgnoreCase("Fixed"))
                    oIssue.m_oHasResolution = new Fixed();
                if (sIssueResolution.equalsIgnoreCase("Invalid"))
                    oIssue.m_oHasResolution = new Invalid();
                if (sIssueResolution.equalsIgnoreCase("ThirdParty"))
                    oIssue.m_oHasResolution = new ThirdParty();
                if (sIssueResolution.equalsIgnoreCase("WontFix"))
                    oIssue.m_oHasResolution = new WontFix();
                if (sIssueResolution.equalsIgnoreCase("WorksForMe"))
                    oIssue.m_oHasResolution = new WorksForMe();
                if (sIssueResolution.equalsIgnoreCase("Later"))
                    oIssue.m_oHasResolution = new Later();
                if (sIssueResolution.equalsIgnoreCase("Remind"))
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


                String sIssueSeverity = GetValue(eIssue, "s:" + MetadataConstants.c_XMLE_issueSeverity);
                if (sIssueSeverity.equalsIgnoreCase("Blocker"))
                    oIssue.m_oHasSeverity = new Blocker();
                if (sIssueSeverity.equalsIgnoreCase("Critical"))
                    oIssue.m_oHasSeverity = new Critical();
                if (sIssueSeverity.equalsIgnoreCase("Major"))
                    oIssue.m_oHasSeverity = new Major();
                if (sIssueSeverity.equalsIgnoreCase("Minor"))
                    oIssue.m_oHasSeverity = new Minor();
                if (sIssueSeverity.equalsIgnoreCase("Trivial"))
                    oIssue.m_oHasSeverity = new Trivial();

                NodeList nlAssignee = eIssue.getElementsByTagName("s:" + MetadataConstants.c_XMLE_issueAssignedTo);   //getting node for tag issueAssignedTo
                if (nlAssignee != null && nlAssignee.getLength() > 0)
                {
                    Element eAssignee = (Element) nlAssignee.item(0);
                    oIssue.m_oHasAssignee = GetPersonObject("s:", eAssignee);
                    //oIssue.m_oHasAssignee.m_oIsAssigneeOf = new Issue[1];
                    //oIssue.m_oHasAssignee.m_oIsAssigneeOf[0] = oIssue;
                }

                NodeList nlCCPerson = eIssue.getElementsByTagName("s:" + MetadataConstants.c_XMLE_issueCCPerson);
                NodeList nlCCPersonRemoved = eIssue.getElementsByTagName("s:" + MetadataConstants.c_XMLE_issueCCPerson + MetadataConstants.c_XMLE_Removed);
                if (nlCCPerson != null && nlCCPersonRemoved != null)
                {
                    oIssue.m_oHasCCPerson = new foaf_Person[nlCCPerson.getLength() + nlCCPersonRemoved.getLength()];
                    if (nlCCPerson.getLength() > 0)
                    {
                        for (int i = 0; i < nlCCPerson.getLength(); i++)
                        {
                            Element eCCPerson = (Element)nlCCPerson.item(i);
                            oIssue.m_oHasCCPerson[i] = GetPersonObject("s:", eCCPerson);
                            //oIssue.m_oHasCCPerson[i].m_oIsCcPersonOf = new Issue[1];
                            //oIssue.m_oHasCCPerson[i].m_oIsCcPersonOf[0] = oIssue;
                        }
                    }
                    if (nlCCPersonRemoved.getLength() > 0)
                    {
                        for (int i = 0; i < nlCCPersonRemoved.getLength(); i++)
                        {
                            Element eCCPersonRemoved = (Element)nlCCPersonRemoved.item(i);
                            oIssue.m_oHasCCPerson[i + nlCCPerson.getLength()] = GetPersonObject("s:", eCCPersonRemoved);
                            oIssue.m_oHasCCPerson[i + nlCCPerson.getLength()].m_bRemoved = true;
                        }
                    }
                }

                oIssue.m_sBugURL = GetValue(eIssue, "s:" + MetadataConstants.c_XMLE_issueUrl);

                NodeList nlDependsOn = eIssue.getElementsByTagName("s:" + MetadataConstants.c_XMLE_issueDependsOn + MetadataConstants.c_XMLE_Id);
                NodeList nlDependsOnRemoved = eIssue.getElementsByTagName("s:" + MetadataConstants.c_XMLE_issueDependsOn + MetadataConstants.c_XMLE_Removed + MetadataConstants.c_XMLE_Id);
                if (nlDependsOn != null && nlDependsOnRemoved != null)
                {
                    oIssue.m_oDependsOn = new Issue[nlDependsOn.getLength() + nlDependsOnRemoved.getLength()];
                    if (nlDependsOn.getLength() > 0)
                    {
                        for (int i = 0; i < nlDependsOn.getLength(); i++)
                        {
                            Element eDependsOn = (Element)nlDependsOn.item(i);
                            oIssue.m_oDependsOn[i] = new Issue();
                            oIssue.m_oDependsOn[i].m_sID = eDependsOn.getFirstChild().getNodeValue();
                        }
                    }
                    if (nlDependsOnRemoved.getLength() > 0)
                    {
                        for (int i = 0; i < nlDependsOnRemoved.getLength(); i++)
                        {
                            Element eDependsOnRemoved = (Element)nlDependsOnRemoved.item(i);
                            oIssue.m_oDependsOn[i + nlDependsOn.getLength()] = new Issue();
                            oIssue.m_oDependsOn[i + nlDependsOn.getLength()].m_sID = eDependsOnRemoved.getFirstChild().getNodeValue();
                            oIssue.m_oDependsOn[i + nlDependsOn.getLength()].m_bRemoved = true;
                        }
                    }
                }
                
                NodeList nlBlocks = eIssue.getElementsByTagName("s:" + MetadataConstants.c_XMLE_issueBlocks + MetadataConstants.c_XMLE_Id);
                NodeList nlBlocksRemoved = eIssue.getElementsByTagName("s:" + MetadataConstants.c_XMLE_issueBlocks + MetadataConstants.c_XMLE_Removed + MetadataConstants.c_XMLE_Id);
                if (nlBlocks != null && nlBlocksRemoved != null)
                {
                    oIssue.m_oBlocks = new Issue[nlBlocks.getLength() + nlBlocksRemoved.getLength()];
                    if (nlBlocks.getLength() > 0)
                    {
                        for (int i = 0; i < nlBlocks.getLength(); i++)
                        {
                            Element eBlocks = (Element)nlBlocks.item(i);
                            oIssue.m_oBlocks[i] = new Issue();
                            oIssue.m_oBlocks[i].m_sID = eBlocks.getFirstChild().getNodeValue();
                        }
                    }
                    if (nlBlocksRemoved.getLength() > 0)
                    {
                        for (int i = 0; i < nlBlocksRemoved.getLength(); i++)
                        {
                            Element eBlocksRemoved = (Element)nlBlocksRemoved.item(i);
                            oIssue.m_oBlocks[i + nlBlocks.getLength()] = new Issue();
                            oIssue.m_oBlocks[i + nlBlocks.getLength()].m_sID = eBlocksRemoved.getFirstChild().getNodeValue();
                            oIssue.m_oBlocks[i + nlBlocks.getLength()].m_bRemoved = true;
                        }
                    }
                }

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
                            oIssue.m_oHasComment[i].m_oHasCommentor = GetPersonObject("s:", eCommentor);
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
                            oIssue.m_oHasAttachment[i].m_oHasCreator = GetPersonObject("s:", eCreator);
                        }
                        
                        //oIssue.m_oHasAttachment[i].m_oIsAttachmentOf = oIssue;
                    }
                }

                NodeList nlActivity = eIssue.getElementsByTagName("s:" + MetadataConstants.c_XMLE_issueActivity);
                NodeList nlActivityWRA = eIssue.getElementsByTagName("s:" + MetadataConstants.c_XMLE_activityWRA);
                if (nlActivityWRA != null && nlActivityWRA.getLength() > 0)
                {
                    oIssue.m_oHasActivity = new Activity[nlActivityWRA.getLength()];
                    int iNum = 0;
                    for (int i = 0; i < nlActivity.getLength(); i++)
                    {
                        Element eActivity = (Element)nlActivity.item(i);
                        nlActivityWRA = eActivity.getElementsByTagName("s:" + MetadataConstants.c_XMLE_activityWRA);
                        for (int j = 0; j < nlActivityWRA.getLength(); j++)
                        {
                            Element eActivityWRA = (Element)nlActivityWRA.item(j);
                            oIssue.m_oHasActivity[iNum] = new Activity();
                            oIssue.m_oHasActivity[iNum].m_sID = GetValue(eActivity, "s:" + MetadataConstants.c_XMLE_activity + MetadataConstants.c_XMLE_Id);
                            oIssue.m_oHasActivity[iNum].m_oHasInvolvedPerson = new foaf_Person();
                            oIssue.m_oHasActivity[iNum].m_oHasInvolvedPerson.m_sID = GetValue(eActivity, "s:" + MetadataConstants.c_XMLE_activityWho);
                            oIssue.m_oHasActivity[iNum].m_dtmWhen = MetadataGlobal.GetDateTime(GetValue(eActivity, "s:" + MetadataConstants.c_XMLE_activityWhen));
                            oIssue.m_oHasActivity[iNum].m_sWhat = GetValue(eActivityWRA, "s:" + MetadataConstants.c_XMLE_activityWhat);
                            oIssue.m_oHasActivity[iNum].m_sRemoved = GetValue(eActivityWRA, "s:" + MetadataConstants.c_XMLE_activityRemoved);
                            oIssue.m_oHasActivity[iNum].m_sAdded = GetValue(eActivityWRA, "s:" + MetadataConstants.c_XMLE_activityAdded);
                            //oIssue.m_oHasActivity[i].m_oIsActivityOf = oIssue;
                            iNum++;
                        }
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

            eOriginalData = ChangeElementTagName(dDoc, eOriginalData, "s:" + MetadataConstants.c_XMLE_kesi);
            MetadataModel.SaveObjectNewIssue(sEventId, eOriginalData, oIssue, bIsUpdate);
            
            return oIssue;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * @summary Method for reading new commit event from XML
     * @startRealisation Sasa Stojanovic 16.01.2012.
     * @finalModification Sasa Stojanovic 16.01.2012.
     * @param dDoc - input XML document to read
     * @return - returns Commit object
     */
    public static Commit NewCommit(Document dDoc)
    {
        try
        {
            String sEventId = GetEventId(dDoc);
            Element eOriginalData = null;  //element for original data

            Commit oCommit = MetadataObjectFactory.CreateNewCommit();

            NodeList nlCommit = dDoc.getElementsByTagName("s:" + MetadataConstants.c_XMLE_commit);   //getting node for tag commit

            if (nlCommit != null && nlCommit.getLength() > 0)
            {
                Element eCommit = (Element) nlCommit.item(0);
                eOriginalData = eCommit;

                oCommit.m_oIsCommitOfRepository = new Repository();
                oCommit.m_oIsCommitOfRepository.m_sObjectURI = GetValue(eCommit, "s:" + MetadataConstants.c_XMLE_commitRepository + MetadataConstants.c_XMLE_Uri);
                
                oCommit.m_sRevisionTag = GetValue(eCommit, "s:" + MetadataConstants.c_XMLE_commitRevisionTag);
                
                NodeList nlAuthor = eCommit.getElementsByTagName("s:" + MetadataConstants.c_XMLE_commitAuthor);
                if (nlAuthor != null && nlAuthor.getLength() > 0)
                {
                    Element eAuthor = (Element) nlAuthor.item(0);
                    oCommit.m_oHasAuthor = GetPersonObject("s:", eAuthor);
                }
                
                NodeList nlCommiter = eCommit.getElementsByTagName("s:" + MetadataConstants.c_XMLE_commitCommtter);
                if (nlCommiter != null && nlCommiter.getLength() > 0)
                {
                    Element eCommiter = (Element) nlCommiter.item(0);
                    oCommit.m_oHasCommitter = GetPersonObject("s:", eCommiter);
                }

                oCommit.m_dtmCommitDate = MetadataGlobal.GetDateTime(GetValue(eCommit, "s:" + MetadataConstants.c_XMLE_commitDate));
                
                oCommit.m_sCommitMessage = GetValue(eCommit, "s:" + MetadataConstants.c_XMLE_commitMessageLog);
                
                NodeList nlFile = eCommit.getElementsByTagName("s:" + MetadataConstants.c_XMLE_commitFile);
                if (nlFile != null && nlFile.getLength() > 0)
                {
                    oCommit.m_oHasFile = new File[nlFile.getLength()];
                    for (int i = 0; i < nlFile.getLength(); i++)
                    {
                        Element eFile = (Element)nlFile.item(i);
                        oCommit.m_oHasFile[i] = new File();
                        
                        oCommit.m_oHasFile[i].m_sID = GetValue(eFile, "s:" + MetadataConstants.c_XMLE_file + MetadataConstants.c_XMLE_Id);
                        
                        String sFileAction = GetValue(eFile, "s:" + MetadataConstants.c_XMLE_fileAction);
                        if (sFileAction.equalsIgnoreCase("Add"))
                            oCommit.m_oHasFile[i].m_oHasAction = new AddFile();
                        if (sFileAction.equalsIgnoreCase("Copy"))
                            oCommit.m_oHasFile[i].m_oHasAction = new CopyFile();
                        if (sFileAction.equalsIgnoreCase("Delete"))
                            oCommit.m_oHasFile[i].m_oHasAction = new DeleteFile();
                        if (sFileAction.equalsIgnoreCase("Modify"))
                            oCommit.m_oHasFile[i].m_oHasAction = new ModifyFile();
                        if (sFileAction.equalsIgnoreCase("Rename"))
                            oCommit.m_oHasFile[i].m_oHasAction = new RenameFile();
                        if (sFileAction.equalsIgnoreCase("Replace"))
                            oCommit.m_oHasFile[i].m_oHasAction = new ReplaceFile();
                        
                        oCommit.m_oHasFile[i].m_sBranch = GetValue(eFile, "s:" + MetadataConstants.c_XMLE_fileBranch);
                        
                        NodeList nlModule = eFile.getElementsByTagName("s:" + MetadataConstants.c_XMLE_fileModules);
                        if (nlModule != null && nlModule.getLength() > 0)
                        {
                            oCommit.m_oHasFile[i].m_oHasModule = new Module[nlModule.getLength()];
                            for (int j = 0; j < nlModule.getLength(); j++)
                            {
                                Element eModule = (Element)nlModule.item(j);
                                oCommit.m_oHasFile[i].m_oHasModule[j] = new Module();

                                oCommit.m_oHasFile[i].m_oHasModule[j].m_sID = GetValue(eModule, "s:" + MetadataConstants.c_XMLE_module + MetadataConstants.c_XMLE_Id);
                                oCommit.m_oHasFile[i].m_oHasModule[j].m_sName = GetValue(eModule, "s:" + MetadataConstants.c_XMLE_moduleName);
                                oCommit.m_oHasFile[i].m_oHasModule[j].m_iStartLine = Integer.parseInt(GetValue(eModule, "s:" + MetadataConstants.c_XMLE_moduleStartLine));
                                oCommit.m_oHasFile[i].m_oHasModule[j].m_iEndLine = Integer.parseInt(GetValue(eModule, "s:" + MetadataConstants.c_XMLE_moduleEndLine));

                                NodeList nlMethod = eModule.getElementsByTagName("s:" + MetadataConstants.c_XMLE_moduleMethods);
                                if (nlMethod != null && nlMethod.getLength() > 0)
                                {
                                    oCommit.m_oHasFile[i].m_oHasModule[j].m_oHasMethod = new Method[nlMethod.getLength()];
                                    for (int k = 0; k < nlMethod.getLength(); k++)
                                    {
                                        Element eMethod = (Element)nlMethod.item(k);
                                        oCommit.m_oHasFile[i].m_oHasModule[j].m_oHasMethod[k] = new Method();

                                        oCommit.m_oHasFile[i].m_oHasModule[j].m_oHasMethod[k].m_sID = GetValue(eMethod, "s:" + MetadataConstants.c_XMLE_method + MetadataConstants.c_XMLE_Id);
                                        oCommit.m_oHasFile[i].m_oHasModule[j].m_oHasMethod[k].m_sName = GetValue(eMethod, "s:" + MetadataConstants.c_XMLE_methodName);
                                        oCommit.m_oHasFile[i].m_oHasModule[j].m_oHasMethod[k].m_iStartLine = Integer.parseInt(GetValue(eMethod, "s:" + MetadataConstants.c_XMLE_methodStartLine));
                                        oCommit.m_oHasFile[i].m_oHasModule[j].m_oHasMethod[k].m_iEndLine = Integer.parseInt(GetValue(eMethod, "s:" + MetadataConstants.c_XMLE_methodEndLine));
                                    }
                                }
                            }
                        }
                    }
                }
            }
            
            eOriginalData = ChangeElementTagName(dDoc, eOriginalData, "s:" + MetadataConstants.c_XMLE_kesi);
            MetadataModel.SaveObjectNewCommit(sEventId, eOriginalData, oCommit);
            
            return oCommit;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * @summary Method for reading new mail event from XML
     * @startRealisation Sasa Stojanovic 02.02.2012.
     * @finalModification Sasa Stojanovic 02.02.2012.
     * @param dDoc - input XML document to read
     * @return - returns Mail object
     */
    public static Mail NewMail(Document dDoc)
    {
        try
        {
            String sEventId = GetEventId(dDoc);
            Element eOriginalData = null;  //element for original data

            Mail oMail = MetadataObjectFactory.CreateNewMail();

            NodeList nlMail = dDoc.getElementsByTagName("r1:" + MetadataConstants.c_XMLE_message);   //getting node for tag message

            if (nlMail != null && nlMail.getLength() > 0)
            {
                Element eMail = (Element) nlMail.item(0);
                eOriginalData = eMail;
                
                oMail.m_sID = GetValue(eMail, "r1:" + MetadataConstants.c_XMLE_message + MetadataConstants.c_XMLE_Id);
                oMail.m_sMessageId = GetValue(eMail, "r1:" + MetadataConstants.c_XMLE_message + MetadataConstants.c_XMLE_Id);

                oMail.m_oFrom = new foaf_Person();
                oMail.m_oFrom.m_sID = GetValue(eMail, "r1:" + MetadataConstants.c_XMLE_from);
                oMail.m_oFrom.m_sEmail = GetValue(eMail, "r1:" + MetadataConstants.c_XMLE_from);
                
                oMail.m_dtmHasCreationDate = MetadataGlobal.GetDateTime(GetValue(eMail, "r1:" + MetadataConstants.c_XMLE_date));
                
                oMail.m_sSubject = GetValue(eMail, "r1:" + MetadataConstants.c_XMLE_subject);
                
                oMail.m_oInReplyTo = new Mail();
                oMail.m_oInReplyTo.m_sID = GetValue(eMail, "r1:" + MetadataConstants.c_XMLE_inReplyTo);
                
                String sReferences = GetValue(eMail, "r1:" + MetadataConstants.c_XMLE_references);
                int iReferencesCount = sReferences.replaceAll("[^>]", "").length(); //get number of references
                oMail.m_oReferences = new Mail[iReferencesCount];
                for (int i = 0; i < iReferencesCount; i++)
                {
                    oMail.m_oReferences[i] = new Mail();
                    oMail.m_oReferences[i].m_sID = sReferences.substring(0, sReferences.indexOf(">") + 1);
                    sReferences = sReferences.substring(sReferences.indexOf(">") + 1);
                }
                
                NodeList nlAttachments = eMail.getElementsByTagName("r1:" + MetadataConstants.c_XMLE_attachments);
                if (nlAttachments != null && nlAttachments.getLength() > 0)
                {
                    oMail.m_sAttachment = new String[nlAttachments.getLength()];
                    for (int i = 0; i < nlAttachments.getLength(); i++)
                    {
                        Element eAttachment = (Element)nlAttachments.item(i);
                        oMail.m_sAttachment[i] = GetValue(eAttachment, "r1:" + MetadataConstants.c_XMLE_attachment);
                    }
                }
                
                oMail.m_sContent = GetValue(eMail, "r1:" + MetadataConstants.c_XMLE_content);
                
            }
            
            eOriginalData = ChangeElementTagName(dDoc, eOriginalData, "r1:" + MetadataConstants.c_XMLE_mlsensor);
            MetadataModel.SaveObjectNewMail(sEventId, eOriginalData, oMail);
            
            return oMail;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

//    /**
//     * @summary Method for reading new person event from XML
//     * @startRealisation Sasa Stojanovic 05.09.2011.
//     * @finalModification Sasa Stojanovic 05.09.2011.
//     * @param dDoc - input XML document to read
//     */
//    private static void NewPerson(Document dDoc)
//    {
//        try
//        {
//            String sEventId = GetEventId(dDoc);
//
//            foaf_Person oPerson = MetadataObjectFactory.CreateNewPerson();
//
//            NodeList nlPerson = dDoc.getElementsByTagName("ns1:" + MetadataConstants.c_XMLE_person);   //getting node for tag person
//
//            if (nlPerson != null && nlPerson.getLength() > 0)
//            {
//                Element ePerson = (Element) nlPerson.item(0);
//
//                oPerson.m_sFirstName = GetValue(ePerson, "ns1:" + MetadataConstants.c_XMLE_personFirstName);
//                oPerson.m_sLastName = GetValue(ePerson, "ns1:" + MetadataConstants.c_XMLE_personLastName);
//                oPerson.m_sGender = GetValue(ePerson, "ns1:" + MetadataConstants.c_XMLE_personGender);
//            }
//
//            MetadataModel.SaveObjectNewPerson(sEventId, oPerson);
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//    }

    /**
     * @summary Method for reading new api request event from XML
     * @startRealisation  Sasa Stojanovic     06.09.2011.
     * @finalModification Dejan Milosavljevic 04.02.2012.
     * @param dDoc - input XML document to read
     */
    private static void APICallRequest(Document dDoc) {
        try
        {
            String sEventId = GetEventId(dDoc);

            NodeList nlAPIRequest = dDoc.getElementsByTagName("ns1:" + MetadataConstants.c_XMLE_apiRequest);   //getting node for apirequest

            if (nlAPIRequest != null && nlAPIRequest.getLength() > 0)
            {
                Element eAPIRequest = (Element) nlAPIRequest.item(0);

                String sAPICall = GetValue(eAPIRequest, "s2:" + MetadataConstants.c_XMLE_apiCall);

                ///////////////////////////////// sparql /////////////////////////////////
                if (sAPICall.equals(MetadataConstants.c_XMLAC_sparql))
                {
                    String sSPARQL = "";
                    String sOntModelSpec = "";
                            
                    NodeList nlInputParameter = dDoc.getElementsByTagName("s2:" + MetadataConstants.c_XMLE_inputParameter);   //getting node for apirequest

                    if (nlInputParameter != null && nlInputParameter.getLength() > 0)
                    {
                        for (int i = 0; i < nlInputParameter.getLength(); i++)
                        {
                            Element eInputParameter = (Element) nlInputParameter.item(i);
                            String sParamName = GetValue(eInputParameter, "s2:" + MetadataConstants.c_XMLE_name);
                            if (sParamName.equals(MetadataConstants.c_XMLV_ontModelSpec))
                                sOntModelSpec = GetValue(eInputParameter, "s2:" + MetadataConstants.c_XMLE_value);
                            if (sParamName.equals(MetadataConstants.c_XMLV_sparql))
                                sSPARQL = GetValue(eInputParameter, "s2:" + MetadataConstants.c_XMLE_value);
                        }
                    }
                    
                    MetadataModel.ac_sparql(sEventId, sSPARQL, sOntModelSpec);
                }
                
                ///////////////////////////////// issue_getAllForProduct /////////////////////////////////
                if (sAPICall.equals(MetadataConstants.c_XMLAC_issue_getAllForProduct))
                {
                    String sProductUri = "";
                            
                    NodeList nlInputParameter = dDoc.getElementsByTagName("s2:" + MetadataConstants.c_XMLE_inputParameter);   //getting node for apirequest

                    if (nlInputParameter != null && nlInputParameter.getLength() > 0)
                    {
                        for (int i = 0; i < nlInputParameter.getLength(); i++)
                        {
                            Element eInputParameter = (Element) nlInputParameter.item(i);
                            String sParamName = GetValue(eInputParameter, "s2:" + MetadataConstants.c_XMLE_name);
                            if (sParamName.equals(MetadataConstants.c_XMLV_productUri))
                                sProductUri = GetValue(eInputParameter, "s2:" + MetadataConstants.c_XMLE_value);
                        }
                    }
                    
                    MetadataModel.ac_issue_getAllForProduct(sEventId, sProductUri);
                }
                
                ///////////////////////////////// issue_getAllForMethod /////////////////////////////////
                if (sAPICall.equals(MetadataConstants.c_XMLAC_issue_getAllForMethod))
                {
                    ArrayList <String> sMethodUri = new ArrayList();
                            
                    NodeList nlInputParameter = dDoc.getElementsByTagName("s2:" + MetadataConstants.c_XMLE_inputParameter);   //getting node for apirequest

                    if (nlInputParameter != null && nlInputParameter.getLength() > 0)
                    {
                        for (int i = 0; i < nlInputParameter.getLength(); i++)
                        {
                            Element eInputParameter = (Element) nlInputParameter.item(i);
                            String sParamName = GetValue(eInputParameter, "s2:" + MetadataConstants.c_XMLE_name);
                            if (sParamName.equals(MetadataConstants.c_XMLV_methodUri))
                            {
                                NodeList nlValue = eInputParameter.getElementsByTagName("s2:" + MetadataConstants.c_XMLE_value);
                                if (nlValue != null && nlValue.getLength() > 0)
                                {
                                    for (int j = 0; j < nlValue.getLength(); j++)
                                    {
                                        Element eValue = (Element)nlValue.item(j);
                                        sMethodUri.add(eValue.getFirstChild().getNodeValue());
                                    }
                                }
                            }
                        }
                    }
                    
                    MetadataModel.ac_issue_getAllForMethod(sEventId, sMethodUri);
                }
                
                ///////////////////////////////// issue_getAnnotationStatus /////////////////////////////////
                if (sAPICall.equals(MetadataConstants.c_XMLAC_issue_getAnnotationStatus))
                {
                    String sIssueUri = "";
                            
                    NodeList nlInputParameter = dDoc.getElementsByTagName("s2:" + MetadataConstants.c_XMLE_inputParameter);   //getting node for apirequest

                    if (nlInputParameter != null && nlInputParameter.getLength() > 0)
                    {
                        for (int i = 0; i < nlInputParameter.getLength(); i++)
                        {
                            Element eInputParameter = (Element) nlInputParameter.item(i);
                            String sParamName = GetValue(eInputParameter, "s2:" + MetadataConstants.c_XMLE_name);
                            if (sParamName.equals(MetadataConstants.c_XMLV_issueUri))
                                sIssueUri = GetValue(eInputParameter, "s2:" + MetadataConstants.c_XMLE_value);
                        }
                    }
                    
                    MetadataModel.ac_issue_getAnnotationStatus(sEventId, sIssueUri);
                }
                
                ///////////////////////////////// issue_getInfo /////////////////////////////////
                if (sAPICall.equals(MetadataConstants.c_XMLAC_issue_getInfo))
                {
                    String sIssueUri = "";
                            
                    NodeList nlInputParameter = dDoc.getElementsByTagName("s2:" + MetadataConstants.c_XMLE_inputParameter);   //getting node for apirequest

                    if (nlInputParameter != null && nlInputParameter.getLength() > 0)
                    {
                        for (int i = 0; i < nlInputParameter.getLength(); i++)
                        {
                            Element eInputParameter = (Element) nlInputParameter.item(i);
                            String sParamName = GetValue(eInputParameter, "s2:" + MetadataConstants.c_XMLE_name);
                            if (sParamName.equals(MetadataConstants.c_XMLV_issueUri))
                                sIssueUri = GetValue(eInputParameter, "s2:" + MetadataConstants.c_XMLE_value);
                        }
                    }
                    
                    MetadataModel.ac_issue_getInfo(sEventId, sIssueUri);
                }
                
                ///////////////////////////////// issue_getExplicitDuplicates /////////////////////////////////
                if (sAPICall.equals(MetadataConstants.c_XMLAC_issue_getExplicitDuplicates))
                {
                    String sIssueUri = "";
                            
                    NodeList nlInputParameter = dDoc.getElementsByTagName("s2:" + MetadataConstants.c_XMLE_inputParameter);   //getting node for apirequest

                    if (nlInputParameter != null && nlInputParameter.getLength() > 0)
                    {
                        for (int i = 0; i < nlInputParameter.getLength(); i++)
                        {
                            Element eInputParameter = (Element) nlInputParameter.item(i);
                            String sParamName = GetValue(eInputParameter, "s2:" + MetadataConstants.c_XMLE_name);
                            if (sParamName.equals(MetadataConstants.c_XMLV_issueUri))
                                sIssueUri = GetValue(eInputParameter, "s2:" + MetadataConstants.c_XMLE_value);
                        }
                    }
                    
                    MetadataModel.ac_issue_getExplicitDuplicates(sEventId, sIssueUri);
                }
                
                ///////////////////////////////// issue_getSubjectAreas /////////////////////////////////
                if (sAPICall.equals(MetadataConstants.c_XMLAC_issue_getSubjectAreas))
                {
                    String sIssueUri = "";
                            
                    NodeList nlInputParameter = dDoc.getElementsByTagName("s2:" + MetadataConstants.c_XMLE_inputParameter);   //getting node for apirequest

                    if (nlInputParameter != null && nlInputParameter.getLength() > 0)
                    {
                        for (int i = 0; i < nlInputParameter.getLength(); i++)
                        {
                            Element eInputParameter = (Element) nlInputParameter.item(i);
                            String sParamName = GetValue(eInputParameter, "s2:" + MetadataConstants.c_XMLE_name);
                            if (sParamName.equals(MetadataConstants.c_XMLV_issueUri))
                                sIssueUri = GetValue(eInputParameter, "s2:" + MetadataConstants.c_XMLE_value);
                        }
                    }
                    
                    MetadataModel.ac_issue_getSubjectAreas(sEventId, sIssueUri);
                }
                
                ///////////////////////////////// issue_getSubjectAreasForOpen /////////////////////////////////
                if (sAPICall.equals(MetadataConstants.c_XMLAC_issue_getSubjectAreasForOpen))
                {
                    String sProductUri = "";
                            
                    NodeList nlInputParameter = dDoc.getElementsByTagName("s2:" + MetadataConstants.c_XMLE_inputParameter);   //getting node for apirequest

                    if (nlInputParameter != null && nlInputParameter.getLength() > 0)
                    {
                        for (int i = 0; i < nlInputParameter.getLength(); i++)
                        {
                            Element eInputParameter = (Element) nlInputParameter.item(i);
                            String sParamName = GetValue(eInputParameter, "s2:" + MetadataConstants.c_XMLE_name);
                            if (sParamName.equals(MetadataConstants.c_XMLV_productUri))
                                sProductUri = GetValue(eInputParameter, "s2:" + MetadataConstants.c_XMLE_value);
                        }
                    }
                    
                    MetadataModel.ac_issue_getSubjectAreasForOpen(sEventId, sProductUri);
                }
                
                ///////////////////////////////// issue_getOpen /////////////////////////////////
                if (sAPICall.equals(MetadataConstants.c_XMLAC_issue_getOpen))
                {
                    String sProductUri = "";
                            
                    NodeList nlInputParameter = dDoc.getElementsByTagName("s2:" + MetadataConstants.c_XMLE_inputParameter);   //getting node for apirequest

                    if (nlInputParameter != null && nlInputParameter.getLength() > 0)
                    {
                        for (int i = 0; i < nlInputParameter.getLength(); i++)
                        {
                            Element eInputParameter = (Element) nlInputParameter.item(i);
                            String sParamName = GetValue(eInputParameter, "s2:" + MetadataConstants.c_XMLE_name);
                            if (sParamName.equals(MetadataConstants.c_XMLV_productUri))
                                sProductUri = GetValue(eInputParameter, "s2:" + MetadataConstants.c_XMLE_value);
                        }
                    }
                    
                    MetadataModel.ac_issue_getOpen(sEventId, sProductUri);
                }
                
                ///////////////////////////////// person_getInfo /////////////////////////////////
                if (sAPICall.equals(MetadataConstants.c_XMLAC_person_getInfo))
                {
                    String sPersonUri = "";
                            
                    NodeList nlInputParameter = dDoc.getElementsByTagName("s2:" + MetadataConstants.c_XMLE_inputParameter);   //getting node for apirequest

                    if (nlInputParameter != null && nlInputParameter.getLength() > 0)
                    {
                        for (int i = 0; i < nlInputParameter.getLength(); i++)
                        {
                            Element eInputParameter = (Element) nlInputParameter.item(i);
                            String sParamName = GetValue(eInputParameter, "s2:" + MetadataConstants.c_XMLE_name);
                            if (sParamName.equals(MetadataConstants.c_XMLV_personUri))
                                sPersonUri = GetValue(eInputParameter, "s2:" + MetadataConstants.c_XMLE_value);
                        }
                    }
                    
                    MetadataModel.ac_person_getInfo(sEventId, sPersonUri);
                }
                
                ///////////////////////////////// person_getAllForEmail /////////////////////////////////
                if (sAPICall.equals(MetadataConstants.c_XMLAC_person_getAllForEmail))
                {
                    String sEmail = "";
                            
                    NodeList nlInputParameter = dDoc.getElementsByTagName("s2:" + MetadataConstants.c_XMLE_inputParameter);   //getting node for apirequest

                    if (nlInputParameter != null && nlInputParameter.getLength() > 0)
                    {
                        for (int i = 0; i < nlInputParameter.getLength(); i++)
                        {
                            Element eInputParameter = (Element) nlInputParameter.item(i);
                            String sParamName = GetValue(eInputParameter, "s2:" + MetadataConstants.c_XMLE_name);
                            if (sParamName.equals(MetadataConstants.c_XMLV_email))
                                sEmail = GetValue(eInputParameter, "s2:" + MetadataConstants.c_XMLE_value);
                        }
                    }
                    
                    MetadataModel.ac_person_getAllForEmail(sEventId, sEmail);
                }
                
                ///////////////////////////////// identity_getForPerson /////////////////////////////////
                if (sAPICall.equals(MetadataConstants.c_XMLAC_identity_getForPerson))
                {
                    String sFirstName = "";
                    String sLastName = "";
                    String sEmail = "";
                            
                    NodeList nlInputParameter = dDoc.getElementsByTagName("s2:" + MetadataConstants.c_XMLE_inputParameter);   //getting node for apirequest

                    if (nlInputParameter != null && nlInputParameter.getLength() > 0)
                    {
                        for (int i = 0; i < nlInputParameter.getLength(); i++)
                        {
                            Element eInputParameter = (Element) nlInputParameter.item(i);
                            String sParamName = GetValue(eInputParameter, "s2:" + MetadataConstants.c_XMLE_name);
                            if (sParamName.equals(MetadataConstants.c_XMLV_firstName))
                                sFirstName = GetValue(eInputParameter, "s2:" + MetadataConstants.c_XMLE_value);
                            if (sParamName.equals(MetadataConstants.c_XMLV_lastName))
                                sLastName = GetValue(eInputParameter, "s2:" + MetadataConstants.c_XMLE_value);
                            if (sParamName.equals(MetadataConstants.c_XMLV_email))
                                sEmail = GetValue(eInputParameter, "s2:" + MetadataConstants.c_XMLE_value);
                        }
                    }
                    
                    MetadataModel.ac_identity_getForPerson(sEventId, sFirstName, sLastName, sEmail);
                }
                
                ///////////////////////////////// competency_getForPerson /////////////////////////////////
                if (sAPICall.equals(MetadataConstants.c_XMLAC_competency_getForPerson))
                {
                    String sPersonUri = "";
                            
                    NodeList nlInputParameter = dDoc.getElementsByTagName("s2:" + MetadataConstants.c_XMLE_inputParameter);   //getting node for apirequest

                    if (nlInputParameter != null && nlInputParameter.getLength() > 0)
                    {
                        for (int i = 0; i < nlInputParameter.getLength(); i++)
                        {
                            Element eInputParameter = (Element) nlInputParameter.item(i);
                            String sParamName = GetValue(eInputParameter, "s2:" + MetadataConstants.c_XMLE_name);
                            if (sParamName.equals(MetadataConstants.c_XMLV_personUri))
                                sPersonUri = GetValue(eInputParameter, "s2:" + MetadataConstants.c_XMLE_value);
                        }
                    }
                    
                    MetadataModel.ac_competency_getForPerson(sEventId, sPersonUri);
                }
                
                ///////////////////////////////// competency_getPersonForIssue /////////////////////////////////
                if (sAPICall.equals(MetadataConstants.c_XMLAC_competency_getPersonForIssue))
                {
                    String sPersonForIssueSPARQL = "";
                            
                    NodeList nlInputParameter = dDoc.getElementsByTagName("s2:" + MetadataConstants.c_XMLE_inputParameter);   //getting node for apirequest

                    if (nlInputParameter != null && nlInputParameter.getLength() > 0)
                    {
                        for (int i = 0; i < nlInputParameter.getLength(); i++)
                        {
                            Element eInputParameter = (Element) nlInputParameter.item(i);
                            String sParamName = GetValue(eInputParameter, "s2:" + MetadataConstants.c_XMLE_name);
                            if (sParamName.equals(MetadataConstants.c_XMLV_personForIssueSPARQL))
                                sPersonForIssueSPARQL = GetValue(eInputParameter, "s2:" + MetadataConstants.c_XMLE_value);
                        }
                    }
                    
                    MetadataModel.ac_competency_getPersonForIssue(sEventId, sPersonForIssueSPARQL);
                }
                
                ///////////////////////////////// method_getAllForPerson /////////////////////////////////
                if (sAPICall.equals(MetadataConstants.c_XMLAC_method_getAllForPerson))
                {
                    String sPersonUri = "";
                            
                    NodeList nlInputParameter = dDoc.getElementsByTagName("s2:" + MetadataConstants.c_XMLE_inputParameter);   //getting node for apirequest

                    if (nlInputParameter != null && nlInputParameter.getLength() > 0)
                    {
                        for (int i = 0; i < nlInputParameter.getLength(); i++)
                        {
                            Element eInputParameter = (Element) nlInputParameter.item(i);
                            String sParamName = GetValue(eInputParameter, "s2:" + MetadataConstants.c_XMLE_name);
                            if (sParamName.equals(MetadataConstants.c_XMLV_personUri))
                                sPersonUri = GetValue(eInputParameter, "s2:" + MetadataConstants.c_XMLE_value);
                        }
                    }
                    
                    MetadataModel.ac_method_getAllForPerson(sEventId, sPersonUri);
                }
                
                ///////////////////////////////// method_getRelatedCode /////////////////////////////////
                if (sAPICall.equals(MetadataConstants.c_XMLAC_method_getRelatedCode))
                {
                    String sPersonUri = "";
                    String sProductUri = "";
                            
                    NodeList nlInputParameter = dDoc.getElementsByTagName("s2:" + MetadataConstants.c_XMLE_inputParameter);   //getting node for apirequest

                    if (nlInputParameter != null && nlInputParameter.getLength() > 0)
                    {
                        for (int i = 0; i < nlInputParameter.getLength(); i++)
                        {
                            Element eInputParameter = (Element) nlInputParameter.item(i);
                            String sParamName = GetValue(eInputParameter, "s2:" + MetadataConstants.c_XMLE_name);
                            if (sParamName.equals(MetadataConstants.c_XMLV_personUri))
                                sPersonUri = GetValue(eInputParameter, "s2:" + MetadataConstants.c_XMLE_value);
                            if (sParamName.equals(MetadataConstants.c_XMLV_productUri))
                                sProductUri = GetValue(eInputParameter, "s2:" + MetadataConstants.c_XMLE_value);
                        }
                    }
                    
                    MetadataModel.ac_method_getRelatedCode(sEventId, sPersonUri, sProductUri);
                }
                
                ///////////////////////////////// issue_getRelatedToKeyword /////////////////////////////////
                if (sAPICall.equals(MetadataConstants.c_XMLAC_issue_getRelatedToKeyword))
                {
                    String sKeyword = "";
                            
                    NodeList nlInputParameter = dDoc.getElementsByTagName("s2:" + MetadataConstants.c_XMLE_inputParameter);   //getting node for apirequest

                    if (nlInputParameter != null && nlInputParameter.getLength() > 0)
                    {
                        for (int i = 0; i < nlInputParameter.getLength(); i++)
                        {
                            Element eInputParameter = (Element) nlInputParameter.item(i);
                            String sParamName = GetValue(eInputParameter, "s2:" + MetadataConstants.c_XMLE_name);
                            if (sParamName.equals(MetadataConstants.c_XMLV_keyword))
                                sKeyword = GetValue(eInputParameter, "s2:" + MetadataConstants.c_XMLE_value);
                        }
                    }
                    
                    MetadataModel.ac_issue_getRelatedToKeyword(sEventId, sKeyword);
                }
                
                ///////////////////////////////// commit_getRelatedToKeyword /////////////////////////////////
                if (sAPICall.equals(MetadataConstants.c_XMLAC_commit_getRelatedToKeyword))
                {
                    String sKeyword = "";
                            
                    NodeList nlInputParameter = dDoc.getElementsByTagName("s2:" + MetadataConstants.c_XMLE_inputParameter);   //getting node for apirequest

                    if (nlInputParameter != null && nlInputParameter.getLength() > 0)
                    {
                        for (int i = 0; i < nlInputParameter.getLength(); i++)
                        {
                            Element eInputParameter = (Element) nlInputParameter.item(i);
                            String sParamName = GetValue(eInputParameter, "s2:" + MetadataConstants.c_XMLE_name);
                            if (sParamName.equals(MetadataConstants.c_XMLV_keyword))
                                sKeyword = GetValue(eInputParameter, "s2:" + MetadataConstants.c_XMLE_value);
                        }
                    }
                    
                    MetadataModel.ac_commit_getRelatedToKeyword(sEventId, sKeyword);
                }
                
                ///////////////////////////////// file_getAll /////////////////////////////////
                if (sAPICall.equals(MetadataConstants.c_XMLAC_file_getAll))
                {
                    String sOffset = "";
                    String sCount = "";
                            
                    NodeList nlInputParameter = dDoc.getElementsByTagName("s2:" + MetadataConstants.c_XMLE_inputParameter);   //getting node for apirequest

                    if (nlInputParameter != null && nlInputParameter.getLength() > 0)
                    {
                        for (int i = 0; i < nlInputParameter.getLength(); i++)
                        {
                            Element eInputParameter = (Element) nlInputParameter.item(i);
                            String sParamName = GetValue(eInputParameter, "s2:" + MetadataConstants.c_XMLE_name);
                            if (sParamName.equals(MetadataConstants.c_XMLV_offset))
                                sOffset = GetValue(eInputParameter, "s2:" + MetadataConstants.c_XMLE_value);
                            if (sParamName.equals(MetadataConstants.c_XMLV_count))
                                sCount = GetValue(eInputParameter, "s2:" + MetadataConstants.c_XMLE_value);
                        }
                    }
                    
                    MetadataModel.ac_file_getAll(sEventId, sOffset, sCount);
                }
                
                ///////////////////////////////// email_getRelatedToKeyword /////////////////////////////////
                if (sAPICall.equals(MetadataConstants.c_XMLAC_email_getRelatedToKeyword))
                {
                    String sKeyword = "";
                            
                    NodeList nlInputParameter = dDoc.getElementsByTagName("s2:" + MetadataConstants.c_XMLE_inputParameter);   //getting node for apirequest

                    if (nlInputParameter != null && nlInputParameter.getLength() > 0)
                    {
                        for (int i = 0; i < nlInputParameter.getLength(); i++)
                        {
                            Element eInputParameter = (Element) nlInputParameter.item(i);
                            String sParamName = GetValue(eInputParameter, "s2:" + MetadataConstants.c_XMLE_name);
                            if (sParamName.equals(MetadataConstants.c_XMLV_keyword))
                                sKeyword = GetValue(eInputParameter, "s2:" + MetadataConstants.c_XMLE_value);
                        }
                    }
                    
                    MetadataModel.ac_email_getRelatedToKeyword(sEventId, sKeyword);
                }
                
                ///////////////////////////////// post_getRelatedToKeyword /////////////////////////////////
                if (sAPICall.equals(MetadataConstants.c_XMLAC_post_getRelatedToKeyword))
                {
                    String sKeyword = "";
                            
                    NodeList nlInputParameter = dDoc.getElementsByTagName("s2:" + MetadataConstants.c_XMLE_inputParameter);   //getting node for apirequest

                    if (nlInputParameter != null && nlInputParameter.getLength() > 0)
                    {
                        for (int i = 0; i < nlInputParameter.getLength(); i++)
                        {
                            Element eInputParameter = (Element) nlInputParameter.item(i);
                            String sParamName = GetValue(eInputParameter, "s2:" + MetadataConstants.c_XMLE_name);
                            if (sParamName.equals(MetadataConstants.c_XMLV_keyword))
                                sKeyword = GetValue(eInputParameter, "s2:" + MetadataConstants.c_XMLE_value);
                        }
                    }
                    
                    MetadataModel.ac_post_getRelatedToKeyword(sEventId, sKeyword);
                }
                
                ///////////////////////////////// wiki_getRelatedToKeyword /////////////////////////////////
                if (sAPICall.equals(MetadataConstants.c_XMLAC_wiki_getRelatedToKeyword))
                {
                    String sKeyword = "";
                            
                    NodeList nlInputParameter = dDoc.getElementsByTagName("s2:" + MetadataConstants.c_XMLE_inputParameter);   //getting node for apirequest

                    if (nlInputParameter != null && nlInputParameter.getLength() > 0)
                    {
                        for (int i = 0; i < nlInputParameter.getLength(); i++)
                        {
                            Element eInputParameter = (Element) nlInputParameter.item(i);
                            String sParamName = GetValue(eInputParameter, "s2:" + MetadataConstants.c_XMLE_name);
                            if (sParamName.equals(MetadataConstants.c_XMLV_keyword))
                                sKeyword = GetValue(eInputParameter, "s2:" + MetadataConstants.c_XMLE_value);
                        }
                    }
                    
                    MetadataModel.ac_wiki_getRelatedToKeyword(sEventId, sKeyword);
                }
                
                ///////////////////////////////// issue_getRelatedToIssue /////////////////////////////////
                if (sAPICall.equals(MetadataConstants.c_XMLAC_issue_getRelatedToIssue))
                {
                    String sIssueUri = "";
                            
                    NodeList nlInputParameter = dDoc.getElementsByTagName("s2:" + MetadataConstants.c_XMLE_inputParameter);   //getting node for apirequest

                    if (nlInputParameter != null && nlInputParameter.getLength() > 0)
                    {
                        for (int i = 0; i < nlInputParameter.getLength(); i++)
                        {
                            Element eInputParameter = (Element) nlInputParameter.item(i);
                            String sParamName = GetValue(eInputParameter, "s2:" + MetadataConstants.c_XMLE_name);
                            if (sParamName.equals(MetadataConstants.c_XMLV_issueUri))
                                sIssueUri = GetValue(eInputParameter, "s2:" + MetadataConstants.c_XMLE_value);
                        }
                    }
                    
                    MetadataModel.ac_issue_getRelatedToIssue(sEventId, sIssueUri);
                }
                
                ///////////////////////////////// commit_getRelatedToIssue /////////////////////////////////
                if (sAPICall.equals(MetadataConstants.c_XMLAC_commit_getRelatedToIssue))
                {
                    String sIssueUri = "";
                            
                    NodeList nlInputParameter = dDoc.getElementsByTagName("s2:" + MetadataConstants.c_XMLE_inputParameter);   //getting node for apirequest

                    if (nlInputParameter != null && nlInputParameter.getLength() > 0)
                    {
                        for (int i = 0; i < nlInputParameter.getLength(); i++)
                        {
                            Element eInputParameter = (Element) nlInputParameter.item(i);
                            String sParamName = GetValue(eInputParameter, "s2:" + MetadataConstants.c_XMLE_name);
                            if (sParamName.equals(MetadataConstants.c_XMLV_issueUri))
                                sIssueUri = GetValue(eInputParameter, "s2:" + MetadataConstants.c_XMLE_value);
                        }
                    }
                    
                    MetadataModel.ac_commit_getRelatedToIssue(sEventId, sIssueUri);
                }
                
                ///////////////////////////////// email_getRelatedToIssue /////////////////////////////////
                if (sAPICall.equals(MetadataConstants.c_XMLAC_email_getRelatedToIssue))
                {
                    String sIssueUri = "";
                            
                    NodeList nlInputParameter = dDoc.getElementsByTagName("s2:" + MetadataConstants.c_XMLE_inputParameter);   //getting node for apirequest

                    if (nlInputParameter != null && nlInputParameter.getLength() > 0)
                    {
                        for (int i = 0; i < nlInputParameter.getLength(); i++)
                        {
                            Element eInputParameter = (Element) nlInputParameter.item(i);
                            String sParamName = GetValue(eInputParameter, "s2:" + MetadataConstants.c_XMLE_name);
                            if (sParamName.equals(MetadataConstants.c_XMLV_issueUri))
                                sIssueUri = GetValue(eInputParameter, "s2:" + MetadataConstants.c_XMLE_value);
                        }
                    }
                    
                    MetadataModel.ac_email_getRelatedToIssue(sEventId, sIssueUri);
                }
                
                ///////////////////////////////// post_getRelatedToIssue /////////////////////////////////
                if (sAPICall.equals(MetadataConstants.c_XMLAC_post_getRelatedToIssue))
                {
                    String sIssueUri = "";
                            
                    NodeList nlInputParameter = dDoc.getElementsByTagName("s2:" + MetadataConstants.c_XMLE_inputParameter);   //getting node for apirequest

                    if (nlInputParameter != null && nlInputParameter.getLength() > 0)
                    {
                        for (int i = 0; i < nlInputParameter.getLength(); i++)
                        {
                            Element eInputParameter = (Element) nlInputParameter.item(i);
                            String sParamName = GetValue(eInputParameter, "s2:" + MetadataConstants.c_XMLE_name);
                            if (sParamName.equals(MetadataConstants.c_XMLV_issueUri))
                                sIssueUri = GetValue(eInputParameter, "s2:" + MetadataConstants.c_XMLE_value);
                        }
                    }
                    
                    MetadataModel.ac_post_getRelatedToIssue(sEventId, sIssueUri);
                }
                
                ///////////////////////////////// wiki_getRelatedToIssue /////////////////////////////////
                if (sAPICall.equals(MetadataConstants.c_XMLAC_wiki_getRelatedToIssue))
                {
                    String sIssueUri = "";
                            
                    NodeList nlInputParameter = dDoc.getElementsByTagName("s2:" + MetadataConstants.c_XMLE_inputParameter);   //getting node for apirequest

                    if (nlInputParameter != null && nlInputParameter.getLength() > 0)
                    {
                        for (int i = 0; i < nlInputParameter.getLength(); i++)
                        {
                            Element eInputParameter = (Element) nlInputParameter.item(i);
                            String sParamName = GetValue(eInputParameter, "s2:" + MetadataConstants.c_XMLE_name);
                            if (sParamName.equals(MetadataConstants.c_XMLV_issueUri))
                                sIssueUri = GetValue(eInputParameter, "s2:" + MetadataConstants.c_XMLE_value);
                        }
                    }
                    
                    MetadataModel.ac_wiki_getRelatedToIssue(sEventId, sIssueUri);
                }
                
                ///////////////////////////////// instance_getAllForConcept /////////////////////////////////
                if (sAPICall.equals(MetadataConstants.c_XMLAC_instance_getAllForConcept))
                {
                    ArrayList <String> sConceptUri = new ArrayList();
                            
                    NodeList nlInputParameter = dDoc.getElementsByTagName("s2:" + MetadataConstants.c_XMLE_inputParameter);   //getting node for apirequest

                    if (nlInputParameter != null && nlInputParameter.getLength() > 0)
                    {
                        for (int i = 0; i < nlInputParameter.getLength(); i++)
                        {                          
                            Element eInputParameter = (Element) nlInputParameter.item(i);
                            String sParamName = GetValue(eInputParameter, "s2:" + MetadataConstants.c_XMLE_name);
                            if (sParamName.equals(MetadataConstants.c_XMLV_conceptUri))
                            {
                                NodeList nlValue = eInputParameter.getElementsByTagName("s2:" + MetadataConstants.c_XMLE_value);
                                if (nlValue != null && nlValue.getLength() > 0)
                                {
                                    for (int j = 0; j < nlValue.getLength(); j++)
                                    {
                                        Element eValue = (Element)nlValue.item(j);
                                        sConceptUri.add(eValue.getFirstChild().getNodeValue());
                                    }
                                }
                            }
                        }
                    }
                    
                    MetadataModel.ac_instance_getAllForConcept(sEventId, sConceptUri);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
//    /**
//     * @summary Method for reading new instance request event from XML
//     * @startRealisation Sasa Stojanovic 06.09.2011.
//     * @finalModification Sasa Stojanovic 06.09.2011.
//     * @param dDoc - input XML document to read
//     */
//    private static void InstanceRequest(Document dDoc) {
//        try
//        {
//            String sEventId = GetEventId(dDoc);
//            sEventId = sEventId.replaceFirst("Request", "");    //deleting Request string from id
//            
//            String sMemberURL = "";
//            
//            NodeList nlInputParameter = dDoc.getElementsByTagName("o:" + MetadataConstants.c_XMLE_inputParameter);   //getting node for inputParameter
//
//            if (nlInputParameter != null && nlInputParameter.getLength() > 0)
//            {
//                for (int i = 0; i < nlInputParameter.getLength(); i++)
//                {
//                    Element eInputParameter = (Element) nlInputParameter.item(i);
//                    String sParamName = GetValue(eInputParameter, "o:" + MetadataConstants.c_XMLE_name);
//                    if (sParamName.equals(MetadataConstants.c_XMLV_MemberURL))
//                        sMemberURL = GetValue(eInputParameter, "o:" + MetadataConstants.c_XMLE_value);
//                }
//            }
//
//            MetadataModel.GetInstance(sEventId, sMemberURL);
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//    }

    /**
     * @summary Method for reading new issue annotation event from XML.
     * @startRealisation  Dejan Milosavljevic 16.01.2012.
     * @finalModification Dejan Milosavljevic 18.01.2012.
     * @param dDoc - input XML document to read
     * @return - returns AnnotationData object
     */
    public static AnnotationData NewIssueAnnotation(Document dDoc)
    {
        try
        {
            //String sEventId = GetEventId(dDoc);

            AnnotationData oAnnotation = MetadataObjectFactory.CreateNewAnnotation();
            
            NodeList nlMdservice = dDoc.getElementsByTagName("o:" + MetadataConstants.c_XMLE_mdservice);
            if (nlMdservice != null && nlMdservice.getLength() > 0)
            {
                Element eMdservice = (Element) nlMdservice.item(0);
                String sTag = "o:" + MetadataConstants.c_XMLE_issue + MetadataConstants.c_XMLE_Uri;
                
                //URI
                oAnnotation.m_sObjectURI = GetValue(eMdservice, sTag);
            }

            NodeList nlAnnotation = dDoc.getElementsByTagName("s1:" + MetadataConstants.c_XMLE_keui);   //getting node for tag keui

            if (nlAnnotation != null && nlAnnotation.getLength() > 0)
            {
                Element eAnnotation = (Element) nlAnnotation.item(0);
            
//                //URI
//                oAnnotation.m_sObjectURI = GetValue(eAnnotation, "s1:" + MetadataConstants.c_XMLE_itemUri);
                
                //Annotations
                NodeList nlSubjectAnnotated = eAnnotation.getElementsByTagName("s1:" + MetadataConstants.c_XMLE_subjectAnnotated);
                NodeList nlDescAnnotated = eAnnotation.getElementsByTagName("s1:" + MetadataConstants.c_XMLE_descriptionAnnotated);
                if (nlSubjectAnnotated != null && nlDescAnnotated != null)
                {
                    int iSubjectLength = nlSubjectAnnotated.getLength();
                    int iDescLength = nlDescAnnotated.getLength();
                    oAnnotation.oAnnotated = new MetadataGlobal.AnnotationProp[iSubjectLength + iDescLength];
                    if (iSubjectLength > 0)
                    {
                        for (int i = 0; i < iSubjectLength; i++)
                        {
                            Element eSubjectAnnotated = (Element)nlSubjectAnnotated.item(i);
                            oAnnotation.oAnnotated[i] = new MetadataGlobal.AnnotationProp();
                            oAnnotation.oAnnotated[i].sName = MetadataConstants.c_XMLE_subjectAnnotated;
                            //oAnnotation.oAnnotated[i].sValue = eSubjectAnnotated.getTextContent();
                            oAnnotation.oAnnotated[i].SetAnnotationText(eSubjectAnnotated);
                        }
                    }
                    if (iDescLength > 0)
                    {
                        for (int i = 0; i < iDescLength; i++)
                        {
                            Element eDescAnnotated = (Element)nlDescAnnotated.item(i);
                            oAnnotation.oAnnotated[i + iSubjectLength] = new MetadataGlobal.AnnotationProp();
                            oAnnotation.oAnnotated[i + iSubjectLength].sName = MetadataConstants.c_XMLE_descriptionAnnotated;
                            //oAnnotation.oAnnotated[i + iSubjectLength].sValue = eDescAnnotated.getTextContent();
                            oAnnotation.oAnnotated[i + iSubjectLength].SetAnnotationText(eDescAnnotated);
                        }
                    }
                    oAnnotation.SetKeywords();
                }
                
                //Concepts
                NodeList nlSConcepts = eAnnotation.getElementsByTagName("s1:" + MetadataConstants.c_XMLE_subjectConcepts);
                NodeList nlDConcepts = eAnnotation.getElementsByTagName("s1:" + MetadataConstants.c_XMLE_descriptionConcepts);
                if (nlSConcepts != null && nlDConcepts != null &&
                    nlSConcepts.getLength() > 0 && nlDConcepts.getLength() > 0)
                {
                    Element eSubjectConcepts = (Element) nlSConcepts.item(0);
                    Element eDescConcepts = (Element) nlDConcepts.item(0);
                    NodeList nlDescConcepts = eDescConcepts.getElementsByTagName("s1:" + MetadataConstants.c_XMLE_concept);
                    NodeList nlSubjectConcepts = eSubjectConcepts.getElementsByTagName("s1:" + MetadataConstants.c_XMLE_concept);
                    if (nlSubjectConcepts != null && nlDescConcepts != null)
                    {
                        int iSubjectLength = nlSubjectConcepts.getLength();
                        int iDescLength = nlDescConcepts.getLength();
                        oAnnotation.oConcepts = new MetadataGlobal.ConceptProp[iSubjectLength + iDescLength];
                        for (int i = 0; i < iSubjectLength; i++)
                        {
                            Element eSConcept = (Element)nlSubjectConcepts.item(i);
                            oAnnotation.oConcepts[i] = new MetadataGlobal.ConceptProp();
                            oAnnotation.oConcepts[i].sName = MetadataConstants.c_XMLE_subjectConcepts;
                            oAnnotation.oConcepts[i].sUri = GetValue(eSConcept, "s1:" + MetadataConstants.c_XMLE_uri);//nlCID.item(0).getNodeValue();
                            oAnnotation.oConcepts[i].sCount = GetValue(eSConcept, "s1:" + MetadataConstants.c_XMLE_count);//nlCCount.item(0).getNodeValue();
                        }
                        for (int i = 0; i < iDescLength; i++)
                        {
                            Element eDConcept = (Element)nlDescConcepts.item(i);
                            oAnnotation.oConcepts[i + iSubjectLength] = new MetadataGlobal.ConceptProp();
                            oAnnotation.oConcepts[i + iSubjectLength].sName = MetadataConstants.c_XMLE_descriptionConcepts;
                            oAnnotation.oConcepts[i + iSubjectLength].sUri = GetValue(eDConcept, "s1:" + MetadataConstants.c_XMLE_uri);//nlCID.item(0).getNodeValue();
                            oAnnotation.oConcepts[i + iSubjectLength].sCount = GetValue(eDConcept, "s1:" + MetadataConstants.c_XMLE_count);//nlCCount.item(0).getNodeValue();
                        }
                    }
                }
            }

            MetadataModel.SaveObjectNewAnnotationData(MetadataConstants.c_ET_ALERT_Metadata_IssueNew_Updated, dDoc, oAnnotation);
            
            return oAnnotation;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * @summary Method for reading new comment annotation event from XML.
     * @startRealisation  Dejan Milosavljevic 17.01.2012.
     * @finalModification Dejan Milosavljevic 18.01.2012.
     * @param dDoc - input XML document to read
     * @return - returns AnnotationData object
     */
    public static AnnotationData NewCommentAnnotation(Document dDoc)
    {
        try
        {
            //String sEventId = GetEventId(dDoc);

            AnnotationData oAnnotation = MetadataObjectFactory.CreateNewAnnotation();
            
            NodeList nlMdservice = dDoc.getElementsByTagName("o:" + MetadataConstants.c_XMLE_mdservice);
            if (nlMdservice != null && nlMdservice.getLength() > 0)
            {
                Element eMdservice = (Element) nlMdservice.item(0);
                String sTag = "o:" + MetadataConstants.c_XMLE_comment + MetadataConstants.c_XMLE_Uri;
                
                //URI
                oAnnotation.m_sObjectURI = GetValue(eMdservice, sTag);
            }
            
            NodeList nlAnnotation = dDoc.getElementsByTagName("s1:" + MetadataConstants.c_XMLE_keui);   //getting node for tag keui

            if (nlAnnotation != null && nlAnnotation.getLength() > 0)
            {
                Element eAnnotation = (Element) nlAnnotation.item(0);
            
//                //URI
//                oAnnotation.m_sObjectURI = GetValue(eAnnotation, "s1:" + MetadataConstants.c_XMLE_itemUri);
                
                //Annotations
                NodeList nlCommentAnnotated = eAnnotation.getElementsByTagName("s1:" + MetadataConstants.c_XMLE_commentAnnotated);
                if (nlCommentAnnotated != null)
                {
                    int iCommentLength = nlCommentAnnotated.getLength();
                    oAnnotation.oAnnotated = new MetadataGlobal.AnnotationProp[iCommentLength];
                    if (iCommentLength > 0)
                    {
                        for (int i = 0; i < iCommentLength; i++)
                        {
                            Element eCommentAnnotated = (Element)nlCommentAnnotated.item(i);
                            oAnnotation.oAnnotated[i] = new MetadataGlobal.AnnotationProp();
                            oAnnotation.oAnnotated[i].sName = MetadataConstants.c_XMLE_commentAnnotated;
                            //oAnnotation.oAnnotated[i].sValue = eCommentAnnotated.getTextContent();
                            oAnnotation.oAnnotated[i].SetAnnotationText(eCommentAnnotated);
                        }
                    }
                    oAnnotation.SetKeywords();
                }
                
                //Concepts
                NodeList nlCConcepts = eAnnotation.getElementsByTagName("s1:" + MetadataConstants.c_XMLE_commentConcepts);
                if (nlCConcepts != null && nlCConcepts.getLength() > 0)
                {
                    Element eCommConcepts = (Element) nlCConcepts.item(0);
                    NodeList nlCommConcepts = eCommConcepts.getElementsByTagName("s1:" + MetadataConstants.c_XMLE_concept);
                    if (nlCommConcepts != null)
                    {
                        int iCommentLength = nlCommConcepts.getLength();
                        oAnnotation.oConcepts = new MetadataGlobal.ConceptProp[iCommentLength];
                        for (int i = 0; i < iCommentLength; i++)
                        {
                            Element eCConcept = (Element)nlCommConcepts.item(i);
                            oAnnotation.oConcepts[i] = new MetadataGlobal.ConceptProp();
                            oAnnotation.oConcepts[i].sName = MetadataConstants.c_XMLE_commentConcepts;
                            oAnnotation.oConcepts[i].sUri = GetValue(eCConcept, "s1:" + MetadataConstants.c_XMLE_uri);
                            oAnnotation.oConcepts[i].sCount = GetValue(eCConcept, "s1:" + MetadataConstants.c_XMLE_count);
                        }
                    }
                }
            }

            MetadataModel.SaveObjectNewAnnotationData(MetadataConstants.c_ET_ALERT_Metadata_CommentNew_Updated, dDoc, oAnnotation);
            
            return oAnnotation;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * @summary Method for reading new commit annotation event from XML.
     * @startRealisation  Dejan Milosavljevic 17.01.2012.
     * @finalModification Dejan Milosavljevic 18.01.2012.
     * @param dDoc - input XML document to read
     * @return - returns AnnotationData object
     */
    public static AnnotationData NewCommitAnnotation(Document dDoc)
    {
        try
        {
            //String sEventId = GetEventId(dDoc);

            AnnotationData oAnnotation = MetadataObjectFactory.CreateNewAnnotation();

            NodeList nlMdservice = dDoc.getElementsByTagName("o:" + MetadataConstants.c_XMLE_mdservice);
            if (nlMdservice != null && nlMdservice.getLength() > 0)
            {
                Element eMdservice = (Element) nlMdservice.item(0);
                String sTag = "o:" + MetadataConstants.c_XMLE_commit + MetadataConstants.c_XMLE_Uri;
                
                //URI
                oAnnotation.m_sObjectURI = GetValue(eMdservice, sTag);
            }
            
            NodeList nlAnnotation = dDoc.getElementsByTagName("s1:" + MetadataConstants.c_XMLE_keui);   //getting node for tag keui

            if (nlAnnotation != null && nlAnnotation.getLength() > 0)
            {
                Element eAnnotation = (Element) nlAnnotation.item(0);
            
//                //URI
//                oAnnotation.m_sObjectURI = GetValue(eAnnotation, "s1:" + MetadataConstants.c_XMLE_itemUri);
                
                //Annotations
                NodeList nlCommitAnnotated = eAnnotation.getElementsByTagName("s1:" + MetadataConstants.c_XMLE_commitAnnotated);
                if (nlCommitAnnotated != null)
                {
                    int iCommitLength = nlCommitAnnotated.getLength();
                    oAnnotation.oAnnotated = new MetadataGlobal.AnnotationProp[iCommitLength];
                    if (iCommitLength > 0)
                    {
                        for (int i = 0; i < iCommitLength; i++)
                        {
                            Element eCommitAnnotated = (Element)nlCommitAnnotated.item(i);
                            oAnnotation.oAnnotated[i] = new MetadataGlobal.AnnotationProp();
                            oAnnotation.oAnnotated[i].sName = MetadataConstants.c_XMLE_commitAnnotated;
                            //oAnnotation.oAnnotated[i].sValue = eCommitAnnotated.getTextContent();
                            oAnnotation.oAnnotated[i].SetAnnotationText(eCommitAnnotated);
                        }
                    }
                    oAnnotation.SetKeywords();
                }
                
                //Concepts
                NodeList nlCConcepts = eAnnotation.getElementsByTagName("s1:" + MetadataConstants.c_XMLE_commitConcepts);
                if (nlCConcepts != null && nlCConcepts.getLength() > 0)
                {
                    Element eCommConcepts = (Element) nlCConcepts.item(0);
                    NodeList nlCommConcepts = eCommConcepts.getElementsByTagName("s1:" + MetadataConstants.c_XMLE_concept);
                    if (nlCommConcepts != null)
                    {
                        int iCommentLength = nlCommConcepts.getLength();
                        oAnnotation.oConcepts = new MetadataGlobal.ConceptProp[iCommentLength];
                        for (int i = 0; i < iCommentLength; i++)
                        {
                            Element eCConcept = (Element)nlCommConcepts.item(i);
                            oAnnotation.oConcepts[i] = new MetadataGlobal.ConceptProp();
                            oAnnotation.oConcepts[i].sName = MetadataConstants.c_XMLE_commitConcepts;
                            oAnnotation.oConcepts[i].sUri = GetValue(eCConcept, "s1:" + MetadataConstants.c_XMLE_uri);
                            oAnnotation.oConcepts[i].sCount = GetValue(eCConcept, "s1:" + MetadataConstants.c_XMLE_count);
                        }
                    }
                }
            }

            MetadataModel.SaveObjectNewAnnotationData(MetadataConstants.c_ET_ALERT_Metadata_CommitNew_Updated, dDoc, oAnnotation);
            
            return oAnnotation;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * @summary Method for reading new forum or wiki post annotation event from XML.
     * @startRealisation  Dejan Milosavljevic 17.01.2012.
     * @finalModification Dejan Milosavljevic 18.01.2012.
     * @param dDoc - input XML document to read
     * @return - returns AnnotationData object
     */
    public static AnnotationData NewFWPostAnnotation(Document dDoc, boolean bIsForum)
    {
        try
        {
            //String sEventId = GetEventId(dDoc);
            String sMainTag = "";
            String sEventName;
            if (bIsForum)
            {
                sMainTag = MetadataConstants.c_XMLE_forumPost + MetadataConstants.c_XMLE_Uri;
                sEventName = MetadataConstants.c_ET_ALERT_Metadata_ForumPostNew_Updated;
            }
            else
            {
                //sMainTag = MetadataConstants.c_XMLE_wikiPost + MetadataConstants.c_XMLE_Uri;
                sEventName = MetadataConstants.c_ET_wikiPost_New;
            }

            AnnotationData oAnnotation = MetadataObjectFactory.CreateNewAnnotation();
            
            NodeList nlMdservice = dDoc.getElementsByTagName("o:" + MetadataConstants.c_XMLE_mdservice);
            if (nlMdservice != null && nlMdservice.getLength() > 0)
            {
                Element eMdservice = (Element) nlMdservice.item(0);
                String sTag = "o:" + sMainTag;
                
                //URI
                oAnnotation.m_sObjectURI = GetValue(eMdservice, sTag);
            }
            
            NodeList nlAnnotation = dDoc.getElementsByTagName("s1:" + MetadataConstants.c_XMLE_keui);   //getting node for tag annotation

            if (nlAnnotation != null && nlAnnotation.getLength() > 0)
            {
                Element eAnnotation = (Element) nlAnnotation.item(0);
            
//                //URI
//                oAnnotation.m_sObjectURI = GetValue(eAnnotation, "s1:" + MetadataConstants.c_XMLE_itemUri);
                
                //Annotations
                NodeList nlTitleAnnotated = eAnnotation.getElementsByTagName("s1:" + MetadataConstants.c_XMLE_titleAnnotated);
                NodeList nlBodyAnnotated = eAnnotation.getElementsByTagName("s1:" + MetadataConstants.c_XMLE_bodyAnnotated);
                if (nlTitleAnnotated != null && nlBodyAnnotated != null)
                {
                    int iTitleLength = nlTitleAnnotated.getLength();
                    int iBodyLength = nlBodyAnnotated.getLength();
                    oAnnotation.oAnnotated = new MetadataGlobal.AnnotationProp[iTitleLength + iBodyLength];
                    if (iTitleLength > 0)
                    {
                        for (int i = 0; i < iTitleLength; i++)
                        {
                            Element eTitleAnnotated = (Element)nlTitleAnnotated.item(i);
                            oAnnotation.oAnnotated[i] = new MetadataGlobal.AnnotationProp();
                            oAnnotation.oAnnotated[i].sName = MetadataConstants.c_XMLE_titleAnnotated;
                            //oAnnotation.oAnnotated[i].sValue = eTitleAnnotated.getTextContent();
                            oAnnotation.oAnnotated[i].SetAnnotationText(eTitleAnnotated);
                        }
                    }
                    if (iBodyLength > 0)
                    {
                        for (int i = 0; i < iBodyLength; i++)
                        {
                            Element eBodyAnnotated = (Element)nlBodyAnnotated.item(i);
                            oAnnotation.oAnnotated[i + iTitleLength] = new MetadataGlobal.AnnotationProp();
                            oAnnotation.oAnnotated[i + iTitleLength].sName = MetadataConstants.c_XMLE_bodyAnnotated;
                            //oAnnotation.oAnnotated[i + iTitleLength].sValue = eBodyAnnotated.getTextContent();
                            oAnnotation.oAnnotated[i].SetAnnotationText(eBodyAnnotated);
                        }
                    }
                    oAnnotation.SetKeywords();
                }
                
                //Concepts
                NodeList nlTConcepts = eAnnotation.getElementsByTagName("s1:" + MetadataConstants.c_XMLE_titleConcepts);
                NodeList nlBConcepts = eAnnotation.getElementsByTagName("s1:" + MetadataConstants.c_XMLE_bodyConcepts);
                if (nlTConcepts != null && nlBConcepts != null &&
                    nlTConcepts.getLength() > 0 && nlBConcepts.getLength() > 0)
                {
                    Element eTitleConcepts = (Element) nlTConcepts.item(0);
                    Element eBodyConcepts = (Element) nlBConcepts.item(0);
                    NodeList nlTitleConcepts = eTitleConcepts.getElementsByTagName("s1:" + MetadataConstants.c_XMLE_concept);
                    NodeList nlBodyConcepts = eBodyConcepts.getElementsByTagName("s1:" + MetadataConstants.c_XMLE_concept);
                    if (nlTitleConcepts != null && nlBodyConcepts != null)
                    {
                        int iTitleLength = nlTitleConcepts.getLength();
                        int iBodyLength = nlBodyConcepts.getLength();
                        oAnnotation.oConcepts = new MetadataGlobal.ConceptProp[iTitleLength + iBodyLength];
                        for (int i = 0; i < iTitleLength; i++)
                        {
                            Element eTConcept = (Element)nlTitleConcepts.item(i);
                            oAnnotation.oConcepts[i] = new MetadataGlobal.ConceptProp();
                            oAnnotation.oConcepts[i].sName = MetadataConstants.c_XMLE_titleConcepts;
                            oAnnotation.oConcepts[i].sUri = GetValue(eTConcept, "s1:" + MetadataConstants.c_XMLE_uri);
                            oAnnotation.oConcepts[i].sCount = GetValue(eTConcept, "s1:" + MetadataConstants.c_XMLE_count);
                        }
                        for (int i = 0; i < iBodyLength; i++)
                        {
                            Element eBConcept = (Element)nlBodyConcepts.item(i);
                            oAnnotation.oConcepts[i + iTitleLength] = new MetadataGlobal.ConceptProp();
                            oAnnotation.oConcepts[i + iTitleLength].sName = MetadataConstants.c_XMLE_bodyConcepts;
                            oAnnotation.oConcepts[i + iTitleLength].sUri = GetValue(eBConcept, "s1:" + MetadataConstants.c_XMLE_uri);
                            oAnnotation.oConcepts[i + iTitleLength].sCount = GetValue(eBConcept, "s1:" + MetadataConstants.c_XMLE_count);
                        }
                    }
                }
            }

            MetadataModel.SaveObjectNewAnnotationData(sEventName, dDoc, oAnnotation);
            
            return oAnnotation;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * @summary Method for reading new mail annotation event from XML.
     * @startRealisation  Dejan Milosavljevic 17.01.2012.
     * @finalModification Dejan Milosavljevic 18.01.2012.
     * @param dDoc - input XML document to read
     * @return - returns AnnotationData object
     */
    public static AnnotationData NewMailAnnotation(Document dDoc)
    {
        try
        {
            //String sEventId = GetEventId(dDoc);

            AnnotationData oAnnotation = MetadataObjectFactory.CreateNewAnnotation();
            
            NodeList nlMdservice = dDoc.getElementsByTagName("o:" + MetadataConstants.c_XMLE_mdservice);
            if (nlMdservice != null && nlMdservice.getLength() > 0)
            {
                Element eMdservice = (Element) nlMdservice.item(0);
                String sTag = "o:" + MetadataConstants.c_XMLE_email + MetadataConstants.c_XMLE_Uri;
                
                //URI
                oAnnotation.m_sObjectURI = GetValue(eMdservice, sTag);
            }
            
            NodeList nlAnnotation = dDoc.getElementsByTagName("s1:" + MetadataConstants.c_XMLE_keui);   //getting node for tag annotation

            if (nlAnnotation != null && nlAnnotation.getLength() > 0)
            {
                Element eAnnotation = (Element) nlAnnotation.item(0);
            
//                //URI
//                oAnnotation.m_sObjectURI = GetValue(eAnnotation, "s1:" + MetadataConstants.c_XMLE_itemUri);
                
                //Annotations
                NodeList nlSubjectAnnotated = eAnnotation.getElementsByTagName("s1:" + MetadataConstants.c_XMLE_subjectAnnotated);
                NodeList nlBodyAnnotated = eAnnotation.getElementsByTagName("s1:" + MetadataConstants.c_XMLE_bodyAnnotated);
                if (nlSubjectAnnotated != null && nlBodyAnnotated != null)
                {
                    int iSubjectLength = nlSubjectAnnotated.getLength();
                    int iBodyLength = nlBodyAnnotated.getLength();
                    oAnnotation.oAnnotated = new MetadataGlobal.AnnotationProp[iSubjectLength + iBodyLength];
                    if (iSubjectLength > 0)
                    {
                        for (int i = 0; i < iSubjectLength; i++)
                        {
                            Element eSubjectAnnotated = (Element)nlSubjectAnnotated.item(i);
                            oAnnotation.oAnnotated[i] = new MetadataGlobal.AnnotationProp();
                            oAnnotation.oAnnotated[i].sName = MetadataConstants.c_XMLE_subjectAnnotated;
                            //oAnnotation.oAnnotated[i].sValue = eSubjectAnnotated.getTextContent();
                            oAnnotation.oAnnotated[i].SetAnnotationText(eSubjectAnnotated);
                        }
                    }
                    if (iBodyLength > 0)
                    {
                        for (int i = 0; i < iBodyLength; i++)
                        {
                            Element eBodyAnnotated = (Element)nlBodyAnnotated.item(i);
                            oAnnotation.oAnnotated[i + iSubjectLength] = new MetadataGlobal.AnnotationProp();
                            oAnnotation.oAnnotated[i + iSubjectLength].sName = MetadataConstants.c_XMLE_bodyAnnotated;
                            //oAnnotation.oAnnotated[i + iSubjectLength].sValue = eBodyAnnotated.getTextContent();
                            oAnnotation.oAnnotated[i].SetAnnotationText(eBodyAnnotated);
                        }
                    }
                    oAnnotation.SetKeywords();
                }
                
                //Concepts
                NodeList nlSConcepts = eAnnotation.getElementsByTagName("s1:" + MetadataConstants.c_XMLE_subjectConcepts);
                NodeList nlBConcepts = eAnnotation.getElementsByTagName("s1:" + MetadataConstants.c_XMLE_bodyConcepts);
                if (nlSConcepts != null && nlBConcepts != null &&
                    nlSConcepts.getLength() > 0 && nlBConcepts.getLength() > 0)
                {
                    Element eSubjectConcepts = (Element) nlSConcepts.item(0);
                    Element eBodyConcepts = (Element) nlBConcepts.item(0);
                    NodeList nlSubjectConcepts = eSubjectConcepts.getElementsByTagName("s1:" + MetadataConstants.c_XMLE_concept);
                    NodeList nlBodyConcepts = eBodyConcepts.getElementsByTagName("s1:" + MetadataConstants.c_XMLE_concept);
                    if (nlSubjectConcepts != null && nlBodyConcepts != null)
                    {
                        int iSubjectLength = nlSubjectConcepts.getLength();
                        int iBodyLength = nlBodyConcepts.getLength();
                        oAnnotation.oConcepts = new MetadataGlobal.ConceptProp[iSubjectLength + iBodyLength];
                        for (int i = 0; i < iSubjectLength; i++)
                        {
                            Element eSConcept = (Element)nlSubjectConcepts.item(i);
                            oAnnotation.oConcepts[i] = new MetadataGlobal.ConceptProp();
                            oAnnotation.oConcepts[i].sName = MetadataConstants.c_XMLE_subjectConcepts;
                            oAnnotation.oConcepts[i].sUri = GetValue(eSConcept, "s1:" + MetadataConstants.c_XMLE_uri);
                            oAnnotation.oConcepts[i].sCount = GetValue(eSConcept, "s1:" + MetadataConstants.c_XMLE_count);
                        }
                        for (int i = 0; i < iBodyLength; i++)
                        {
                            Element eBConcept = (Element)nlBodyConcepts.item(i);
                            oAnnotation.oConcepts[i + iSubjectLength] = new MetadataGlobal.ConceptProp();
                            oAnnotation.oConcepts[i + iSubjectLength].sName = MetadataConstants.c_XMLE_bodyConcepts;
                            oAnnotation.oConcepts[i + iSubjectLength].sUri = GetValue(eBConcept, "s1:" + MetadataConstants.c_XMLE_uri);
                            oAnnotation.oConcepts[i + iSubjectLength].sCount = GetValue(eBConcept, "s1:" + MetadataConstants.c_XMLE_count);
                        }
                    }
                }
            }

            MetadataModel.SaveObjectNewAnnotationData(MetadataConstants.c_ET_ALERT_Metadata_MailNew_Updated, dDoc, oAnnotation);
            
            return oAnnotation;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * @summary Method for reading new forum post event from XML.
     * @startRealisation  Dejan Milosavljevic 17.01.2012.
     * @finalModification Sasa Stojanovic 21.02.2012.
     * @param dDoc - input XML document to read
     * @return - returns ForumPost object
     */
    public static ForumPost NewForumPostData(Document dDoc)
    {
        try
        {
            String sEventId = GetEventId(dDoc);
            Element eOriginalData = null;  //element for original data
            
            ForumPost oForumPost = MetadataObjectFactory.CreateNewForumPost();

            NodeList nlForum = dDoc.getElementsByTagName("r:" + MetadataConstants.c_XMLE_forumSensor);   //getting node for tag forum

            if (nlForum != null && nlForum.getLength() > 0)
            {
                Element eForum = (Element) nlForum.item(0);
                eOriginalData = eForum;
                
                //forumItemID
                oForumPost.m_sForumItemID = GetValue(eForum, "r:" + MetadataConstants.c_XMLE_forumItemId);
                
                //forumID
                oForumPost.m_oInForumThread = new ForumThread();
                oForumPost.m_oInForumThread.m_oInForum = new Forum();
                oForumPost.m_oInForumThread.m_oInForum.m_sID = GetValue(eForum, "r:" + MetadataConstants.c_XMLE_forumId);
                
                //forumName
                oForumPost.m_oInForumThread.m_oInForum.m_sName = GetValue(eForum, "r:" + MetadataConstants.c_XMLE_forumName);

                //threadID
                oForumPost.m_oInForumThread.m_sID = GetValue(eForum, "r:" + MetadataConstants.c_XMLE_threadId);

                //postID
                oForumPost.m_sID = GetValue(eForum, "r:" + MetadataConstants.c_XMLE_postId);

                //time
                oForumPost.m_dtmTime = MetadataGlobal.GetDateTime(GetValue(eForum, "r:" + MetadataConstants.c_XMLE_time));

                //subject
                oForumPost.m_sSubject = GetValue(eForum, "r:" + MetadataConstants.c_XMLE_subject);

                //body
                oForumPost.m_sBody = GetValue(eForum, "r:" + MetadataConstants.c_XMLE_body);

                //author
                NodeList nlAuthor = eForum.getElementsByTagName("r:" + MetadataConstants.c_XMLE_author);
                if (nlAuthor != null && nlAuthor.getLength() > 0)
                {
                    Element eAuthor = (Element) nlAuthor.item(0);
                    oForumPost.m_oAuthor = GetPersonObject("r:", eAuthor);
                }
                
                //category
                oForumPost.m_sCategory = GetValue(eForum, "r:" + MetadataConstants.c_XMLE_category);
            }

            eOriginalData = ChangeElementTagName(dDoc, eOriginalData, "r:" + MetadataConstants.c_XMLE_forumSensor);
            MetadataModel.SaveObjectNewForumPost(sEventId, eOriginalData, oForumPost);
            
            return oForumPost;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

        /**
     * @summary Method for reading new/update competence event from XML
     * @startRealisation  Dejan Milosavljevic 02.02.2012.
     * @finalModification Dejan Milosavljevic 20.02.2012.
     * @param dDoc - input XML document to read
     * @param bIsUpdate - marks if this is an update
     * @return - returns Competence object
     */
    public static Competence NewUpdateCompetence(Document dDoc, boolean bIsUpdate)
    {
        try
        {
            String sEventId = GetEventId(dDoc);
            Element eOriginalData = null;  //element for original data

            Competence oCompetence = MetadataObjectFactory.CreateNewCompetence();
            
            NodeList nlCompetence = dDoc.getElementsByTagName("sm:" + MetadataConstants.c_XMLE_competency);   //getting node for tag competency

            if (nlCompetence != null && nlCompetence.getLength() > 0)
            {
                Element eCompetence = (Element) nlCompetence.item(0);
                eOriginalData = eCompetence;

                //Identity ID
                oCompetence.m_oIdentity = new Identity();
                oCompetence.m_oIdentity.m_sID = GetValue(eCompetence, "sm:" + MetadataConstants.c_XMLE_uuid);
                
                //HasLevel (index)
                oCompetence.m_sLevel = GetValue(eCompetence, "sm:" + MetadataConstants.c_XMLE_index);
                
                //Attributs
                //ArrayList<Attribute> oAttributes = new ArrayList<Attribute>();
                oCompetence.m_oHasAttribute = new Attribute[4];
                //    Fluency
                NodeList nlFluency = dDoc.getElementsByTagName("sm:" + MetadataConstants.c_XMLE_fluency);
                if (nlFluency != null && nlFluency.getLength() > 0)
                {
                    Element eFluency = (Element) nlFluency.item(0);
                    Attribute oFluency = new Attribute();
                    oFluency.m_sName = MetadataConstants.c_XMLE_fluency;
                    
                    oFluency.m_oHasMetric = new Metric[3];
                    oFluency.m_oHasMetric[0] = new Metric();
                    oFluency.m_oHasMetric[0].m_sMetricName = "apiCount";
                    oFluency.m_oHasMetric[0].m_sMetricValue = GetValue(eFluency, "sm:" + "apiCount");
                    oFluency.m_oHasMetric[1] = new Metric();
                    oFluency.m_oHasMetric[1].m_sMetricName = "apiIntroduced";
                    oFluency.m_oHasMetric[1].m_sMetricValue = GetValue(eFluency, "sm:" + "apiIntroduced");
                    oFluency.m_oHasMetric[2] = new Metric();
                    oFluency.m_oHasMetric[2].m_sMetricName = "staticAnalysis";
                    oFluency.m_oHasMetric[2].m_sMetricValue = GetValue(eFluency, "sm:" + "staticAnalysis");

                    //oAttributes.add(oFluency);
                    oCompetence.m_oHasAttribute[0] = oFluency;
                }
                
                //    Contribution
                NodeList nlContribution = dDoc.getElementsByTagName("sm:" + MetadataConstants.c_XMLE_contribution);
                if (nlContribution != null && nlContribution.getLength() > 0)
                {
                    Element eContribution = (Element) nlContribution.item(0);
                    Attribute oContribution = new Attribute();
                    oContribution.m_sName = MetadataConstants.c_XMLE_contribution;
                    
                    oContribution.m_oHasMetric = new Metric[3];
                    oContribution.m_oHasMetric[0] = new Metric();
                    oContribution.m_oHasMetric[0].m_sMetricName = "mailingListActivity";
                    oContribution.m_oHasMetric[0].m_sMetricValue = GetValue(eContribution, "sm:" + "mailingListActivity");
                    oContribution.m_oHasMetric[1] = new Metric();
                    oContribution.m_oHasMetric[1].m_sMetricName = "itsActivity";
                    oContribution.m_oHasMetric[1].m_sMetricValue = GetValue(eContribution, "sm:" + "itsActivity");
                    oContribution.m_oHasMetric[2] = new Metric();
                    oContribution.m_oHasMetric[2].m_sMetricName = "sloc";
                    oContribution.m_oHasMetric[2].m_sMetricValue = GetValue(eContribution, "sm:" + "sloc");
                    
                    //oAttributes.add(oContribution);
                    oCompetence.m_oHasAttribute[1] = oContribution;
                }
                
                //    Effectiveness
                NodeList nlEffectiveness = dDoc.getElementsByTagName("sm:" + MetadataConstants.c_XMLE_effectiveness);
                if (nlEffectiveness != null && nlEffectiveness.getLength() > 0)
                {
                    Element eEffectiveness = (Element) nlEffectiveness.item(0);
                    Attribute oEffectiveness  = new Attribute();
                    oEffectiveness .m_sName = MetadataConstants.c_XMLE_effectiveness;
                    
                    oEffectiveness.m_oHasMetric = new Metric[3];
                    oEffectiveness.m_oHasMetric[0] = new Metric();
                    oEffectiveness.m_oHasMetric[0].m_sMetricName = "noOfIssuesFixed";
                    oEffectiveness.m_oHasMetric[0].m_sMetricValue = GetValue(eEffectiveness, "sm:" + "noOfIssuesFixed");
                    oEffectiveness.m_oHasMetric[1] = new Metric();
                    oEffectiveness.m_oHasMetric[1].m_sMetricName = "noOfCommitsCausedIssue";
                    oEffectiveness.m_oHasMetric[1].m_sMetricValue = GetValue(eEffectiveness, "sm:" + "noOfCommitsCausedIssue");
                    oEffectiveness.m_oHasMetric[2] = new Metric();
                    oEffectiveness.m_oHasMetric[2].m_sMetricName = "noOfCommitFixedIssue";
                    oEffectiveness.m_oHasMetric[2].m_sMetricValue = GetValue(eEffectiveness, "sm:" + "noOfCommitFixedIssue");
                    
                    //oAttributes.add(oEffectiveness );
                    oCompetence.m_oHasAttribute[2] = oEffectiveness;
                }
                
                //    Recency
                NodeList nlRecency = dDoc.getElementsByTagName("sm:" + MetadataConstants.c_XMLE_recency);
                if (nlRecency != null && nlRecency.getLength() > 0)
                {
                    Element eRecency = (Element) nlRecency.item(0);
                    Attribute oRecency = new Attribute();
                    oRecency.m_sName = MetadataConstants.c_XMLE_recency;
                    
                    oRecency.m_oHasMetric = new Metric[3];
                    oRecency.m_oHasMetric[0] = new Metric();
                    oRecency.m_oHasMetric[0].m_sMetricName = "timeSinceSCMaction";
                    oRecency.m_oHasMetric[0].m_sMetricValue = GetValue(eRecency, "sm:" + "timeSinceSCMaction");
                    oRecency.m_oHasMetric[1] = new Metric();
                    oRecency.m_oHasMetric[1].m_sMetricName = "timeSinceITSaction";
                    oRecency.m_oHasMetric[1].m_sMetricValue = GetValue(eRecency, "sm:" + "timeSinceITSaction");
                    oRecency.m_oHasMetric[2] = new Metric();
                    oRecency.m_oHasMetric[2].m_sMetricName = "timeSinceMLaction";
                    oRecency.m_oHasMetric[2].m_sMetricValue = GetValue(eRecency, "sm:" + "timeSinceMLaction");
                    
                    //oAttributes.add(oRecency);
                    oCompetence.m_oHasAttribute[3] = oRecency;
                }
            }
            
            eOriginalData = ChangeElementTagName(dDoc, eOriginalData, MetadataConstants.c_XMLE_keui);
            MetadataModel.SaveObjectNewCompetence(sEventId, eOriginalData, oCompetence, bIsUpdate);
            
            return oCompetence;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * @summary Method for reading new identity event from XML
     * @startRealisation Sasa Stojanovic 04.02.2012.
     * @finalModification Sasa Stojanovic 04.02.2012.
     * @param dDoc - input XML document to read
     * @return - returns Identity objects
     */
    public static Identity[] NewIdentity(Document dDoc)
    {
        try
        {
            String sEventId = GetEventId(dDoc);
            Element eOriginalData = null;  //element for original data

            NodeList nlIdentities = dDoc.getElementsByTagName("sm:" + MetadataConstants.c_XMLE_identities);   //getting node for tag identity

            if (nlIdentities != null && nlIdentities.getLength() > 0)
            {
                Element eIdentities = (Element) nlIdentities.item(0);
                eOriginalData = eIdentities;

                NodeList nlIdentity = eIdentities.getElementsByTagName("sm:" + MetadataConstants.c_XMLE_identity);
                if (nlIdentity != null && nlIdentity.getLength() > 0)
                {
                    Identity[] oIdentities = new Identity[nlIdentity.getLength()];
                    for (int i = 0; i < nlIdentity.getLength(); i++)
                    {
                        Element eIdentity = (Element)nlIdentity.item(i);
                        
                        Identity oIdentity = MetadataObjectFactory.CreateNewIdentity();
                        oIdentity.m_sID = GetValue(eIdentity, "sm:" + MetadataConstants.c_XMLE_uuid);
                        
                        NodeList nlPersons = eIdentity.getElementsByTagName("sm:" + MetadataConstants.c_XMLE_persons);
                        if (nlPersons != null && nlPersons.getLength() > 0)
                        {
                            Element ePersons = (Element)nlPersons.item(0);
                            
                            NodeList nlIs = ePersons.getElementsByTagName("sm:" + MetadataConstants.c_XMLE_is);
                            if (nlIs != null && nlIs.getLength() > 0)
                            {
                                Element eIs = (Element)nlIs.item(0);
                                NodeList nlPerson = eIs.getElementsByTagName("o:" + MetadataConstants.c_XMLE_person);
                                oIdentity.m_oIs = new foaf_Person[nlPerson.getLength()];
                                for (int j = 0; j < nlPerson.getLength(); j++)
                                {
                                    Element ePerson = (Element)nlPerson.item(j);
                                    oIdentity.m_oIs[j] = new foaf_Person();
                                    oIdentity.m_oIs[j].m_sObjectURI = ePerson.getFirstChild().getNodeValue();
                                }
                            }

                            NodeList nlIsnt = ePersons.getElementsByTagName("sm:" + MetadataConstants.c_XMLE_isnt);
                            if (nlIsnt != null && nlIsnt.getLength() > 0)
                            {
                                Element eIsnt = (Element)nlIsnt.item(0);
                                NodeList nlPerson = eIsnt.getElementsByTagName("o:" + MetadataConstants.c_XMLE_person);
                                oIdentity.m_oIsnt = new foaf_Person[nlPerson.getLength()];
                                for (int j = 0; j < nlPerson.getLength(); j++)
                                {
                                    Element ePerson = (Element)nlPerson.item(j);
                                    oIdentity.m_oIsnt[j] = new foaf_Person();
                                    oIdentity.m_oIsnt[j].m_sObjectURI = ePerson.getFirstChild().getNodeValue();
                                }
                            }
                        }
                        
                        oIdentities[i] = oIdentity;
                    }
                    
                    //eOriginalData = ChangeElementTagName(dDoc, eOriginalData, "s:" + MetadataConstants.c_XMLE_stardom);
                    MetadataModel.SaveObjectNewIdentity(sEventId, eOriginalData, oIdentities);
                    
                    return oIdentities;
                }
            }
            return null;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * @summary Method for reading update identity event from XML
     * @startRealisation Sasa Stojanovic 04.02.2012.
     * @finalModification Sasa Stojanovic 04.02.2012.
     * @param dDoc - input XML document to read
     * @return - returns Identity objects
     */
    public static Identity[] UpdateIdentity(Document dDoc)
    {
        try
        {
            String sEventId = GetEventId(dDoc);
            Element eOriginalData = null;  //element for original data

            NodeList nlIdentities = dDoc.getElementsByTagName("sm:" + MetadataConstants.c_XMLE_identities);   //getting node for tag identity

            if (nlIdentities != null && nlIdentities.getLength() > 0)
            {
                Element eIdentities = (Element) nlIdentities.item(0);
                eOriginalData = eIdentities;

                NodeList nlIdentity = eIdentities.getElementsByTagName("sm:" + MetadataConstants.c_XMLE_identity);
                if (nlIdentity != null && nlIdentity.getLength() > 0)
                {
                    Identity[] oIdentities = new Identity[nlIdentity.getLength()];
                    for (int i = 0; i < nlIdentity.getLength(); i++)
                    {
                        Element eIdentity = (Element)nlIdentity.item(i);
                        
                        Identity oIdentity = MetadataObjectFactory.CreateNewIdentity();
                        oIdentity.m_sID = GetValue(eIdentity, "sm:" + MetadataConstants.c_XMLE_uuid);
                        
                        int iIsCount = 0;
                        int iIsntCount = 0;
                        NodeList nlAdd = eIdentity.getElementsByTagName("sm:" + MetadataConstants.c_XMLE_add);
                        NodeList nlRemove = eIdentity.getElementsByTagName("sm:" + MetadataConstants.c_XMLE_remove);
                        if (nlAdd != null && nlRemove != null)
                        {
                            //---------------- counting a number of is and isnt tags ----------------
                            if (nlAdd.getLength() > 0)
                            {
                                Element eAdd = (Element)nlAdd.item(0);
                                NodeList nlAddIs = eAdd.getElementsByTagName("sm:" + MetadataConstants.c_XMLE_is);
                                if (nlAddIs != null)
                                {
                                    Element eAddIs = (Element)nlAddIs.item(0);
                                    NodeList nlPerson = eAddIs.getElementsByTagName("o:" + MetadataConstants.c_XMLE_person);
                                    if (nlPerson != null)
                                    {
                                        iIsCount += nlPerson.getLength();
                                    }
                                }
                                NodeList nlAddIsnt = eAdd.getElementsByTagName("sm:" + MetadataConstants.c_XMLE_isnt);
                                if (nlAddIsnt != null)
                                {
                                    Element eAddIs = (Element)nlAddIsnt.item(0);
                                    NodeList nlPerson = eAddIs.getElementsByTagName("o:" + MetadataConstants.c_XMLE_person);
                                    if (nlPerson != null)
                                    {
                                        iIsntCount += nlPerson.getLength();
                                    }
                                }
                            }
                            if (nlRemove.getLength() > 0)
                            {
                                Element eRemove = (Element)nlRemove.item(0);
                                NodeList nlRemoveIs = eRemove.getElementsByTagName("sm:" + MetadataConstants.c_XMLE_is);
                                if (nlRemoveIs != null && nlRemoveIs.getLength() > 0)
                                {
                                    Element eRemoveIs = (Element)nlRemoveIs.item(0);
                                    NodeList nlAllPerson = eRemoveIs.getElementsByTagName("o:" + MetadataConstants.c_XMLE_allPerson);
                                    if (nlAllPerson != null && nlAllPerson.getLength() > 0)
                                    {
                                        oIdentity.m_bIsRemoveAll = true;
                                    }
                                    else
                                    {
                                        NodeList nlPerson = eRemoveIs.getElementsByTagName("o:" + MetadataConstants.c_XMLE_person);
                                        if (nlPerson != null)
                                        {
                                            iIsCount += nlPerson.getLength();
                                        }
                                    }
                                }
                                NodeList nlRemoveIsnt = eRemove.getElementsByTagName("sm:" + MetadataConstants.c_XMLE_isnt);
                                if (nlRemoveIsnt != null)
                                {
                                    Element eRemoveIsnt = (Element)nlRemoveIsnt.item(0);
                                    NodeList nlAllPerson = eRemoveIsnt.getElementsByTagName("o:" + MetadataConstants.c_XMLE_allPerson);
                                    if (nlAllPerson != null && nlAllPerson.getLength() > 0)
                                    {
                                        oIdentity.m_bIsntRemoveAll = true;
                                    }
                                    else
                                    {
                                        NodeList nlPerson = eRemoveIsnt.getElementsByTagName("o:" + MetadataConstants.c_XMLE_person);
                                        if (nlPerson != null)
                                        {
                                            iIsntCount += nlPerson.getLength();
                                        }
                                    }
                                }
                            }
                            
                            //---------------- creating is and isnt intances ----------------
                            oIdentity.m_oIs = new foaf_Person[iIsCount];
                            oIdentity.m_oIsnt = new foaf_Person[iIsntCount];
                            
                            //---------------- for adding ----------------
                            int iAddIsCount = 0;
                            int iAddIsntCount = 0;
                            if (nlAdd.getLength() > 0)
                            {
                                Element eAdd = (Element)nlAdd.item(0);
                                
                                NodeList nlAddIs = eAdd.getElementsByTagName("sm:" + MetadataConstants.c_XMLE_is);
                                if (nlAddIs != null && nlAddIs.getLength() > 0)
                                {
                                    Element eAddIs = (Element)nlAddIs.item(0);
                                    NodeList nlPerson = eAddIs.getElementsByTagName("o:" + MetadataConstants.c_XMLE_person);
                                    iAddIsCount = nlPerson.getLength();
                                    for (int j = 0; j < nlPerson.getLength(); j++)
                                    {
                                        Element ePerson = (Element)nlPerson.item(j);
                                        oIdentity.m_oIs[j] = new foaf_Person();
                                        oIdentity.m_oIs[j].m_sObjectURI = ePerson.getFirstChild().getNodeValue();
                                    }
                                }

                                NodeList nlAddIsnt = eAdd.getElementsByTagName("sm:" + MetadataConstants.c_XMLE_isnt);
                                if (nlAddIsnt != null && nlAddIsnt.getLength() > 0)
                                {
                                    Element eAddIsnt = (Element)nlAddIsnt.item(0);
                                    NodeList nlPerson = eAddIsnt.getElementsByTagName("o:" + MetadataConstants.c_XMLE_person);
                                    iAddIsntCount = nlPerson.getLength();
                                    for (int j = 0; j < nlPerson.getLength(); j++)
                                    {
                                        Element ePerson = (Element)nlPerson.item(j);
                                        oIdentity.m_oIsnt[j] = new foaf_Person();
                                        oIdentity.m_oIsnt[j].m_sObjectURI = ePerson.getFirstChild().getNodeValue();
                                    }
                                }
                            }
                            
                            //---------------- for removing ----------------
                            if (nlRemove.getLength() > 0)
                            {
                                Element eRemove = (Element)nlRemove.item(0);
                                
                                if (!oIdentity.m_bIsRemoveAll)
                                {
                                    NodeList nlRemoveIs = eRemove.getElementsByTagName("sm:" + MetadataConstants.c_XMLE_is);
                                    if (nlRemoveIs != null && nlRemoveIs.getLength() > 0)
                                    {
                                        Element eRemoveIs = (Element)nlRemoveIs.item(0);
                                        NodeList nlPerson = eRemoveIs.getElementsByTagName("o:" + MetadataConstants.c_XMLE_person);
                                        for (int j = 0; j < nlPerson.getLength(); j++)
                                        {
                                            Element ePerson = (Element)nlPerson.item(j);
                                            oIdentity.m_oIs[j + iAddIsCount] = new foaf_Person();
                                            oIdentity.m_oIs[j + iAddIsCount].m_sObjectURI = ePerson.getFirstChild().getNodeValue();
                                            oIdentity.m_oIs[j + iAddIsCount].m_bRemoved = true;
                                        }
                                    }
                                }
                                
                                if (!oIdentity.m_bIsntRemoveAll)
                                {
                                    NodeList nlRemoveIsnt = eRemove.getElementsByTagName("sm:" + MetadataConstants.c_XMLE_isnt);
                                    if (nlRemoveIsnt != null && nlRemoveIsnt.getLength() > 0)
                                    {
                                        Element eRemoveIsnt = (Element)nlRemoveIsnt.item(0);
                                        NodeList nlPerson = eRemoveIsnt.getElementsByTagName("o:" + MetadataConstants.c_XMLE_person);
                                        iAddIsntCount = nlPerson.getLength();
                                        for (int j = 0; j < nlPerson.getLength(); j++)
                                        {
                                            Element ePerson = (Element)nlPerson.item(j);
                                            oIdentity.m_oIsnt[j + iAddIsntCount] = new foaf_Person();
                                            oIdentity.m_oIsnt[j + iAddIsntCount].m_sObjectURI = ePerson.getFirstChild().getNodeValue();
                                            oIdentity.m_oIsnt[j + iAddIsntCount].m_bRemoved = true;
                                        }
                                    }
                                }
                            }
                        }
                        oIdentities[i] = oIdentity;
                    }
                    
                    //eOriginalData = ChangeElementTagName(dDoc, eOriginalData, "s:" + MetadataConstants.c_XMLE_stardom);
                    MetadataModel.UpdateObjectIdentity(sEventId, eOriginalData, oIdentities);
                    
                    return oIdentities;
                }
            }
            return null;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * @summary Method for reading remove identity event from XML
     * @startRealisation Sasa Stojanovic 04.02.2012.
     * @finalModification Sasa Stojanovic 04.02.2012.
     * @param dDoc - input XML document to read
     * @return - returns Identity objects
     */
    public static Identity[] RemoveIdentity(Document dDoc)
    {
        try
        {
            String sEventId = GetEventId(dDoc);
            Element eOriginalData = null;  //element for original data

            NodeList nlIdentities = dDoc.getElementsByTagName("sm:" + MetadataConstants.c_XMLE_identities);   //getting node for tag identity

            if (nlIdentities != null && nlIdentities.getLength() > 0)
            {
                Element eIdentities = (Element) nlIdentities.item(0);
                eOriginalData = eIdentities;

                NodeList nlIdentity = eIdentities.getElementsByTagName("sm:" + MetadataConstants.c_XMLE_identity);
                if (nlIdentity != null && nlIdentity.getLength() > 0)
                {
                    Identity[] oIdentities = new Identity[nlIdentity.getLength()];
                    for (int i = 0; i < nlIdentity.getLength(); i++)
                    {
                        Element eIdentity = (Element)nlIdentity.item(i);
                        
                        Identity oIdentity = MetadataObjectFactory.CreateNewIdentity();
                        oIdentity.m_sID = GetValue(eIdentity, "sm:" + MetadataConstants.c_XMLE_uuid);
                        oIdentity.m_bRemoved = true;
                        
                        oIdentities[i] = oIdentity;
                    }
                    
                    //eOriginalData = ChangeElementTagName(dDoc, eOriginalData, "s:" + MetadataConstants.c_XMLE_stardom);
                    MetadataModel.RemoveObjectIdentity(sEventId, eOriginalData, oIdentities);
                    
                    return oIdentities;
                }
            }
            return null;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
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
     * @param sPrefix - input prefix (s:)
     * @param ePerson - input XML element for person
     * @return - foaf_Person object
     */
    private static foaf_Person GetPersonObject(String sPrefix, Element ePerson)
    {
        foaf_Person oPerson = new foaf_Person();
        try
        {
            oPerson.m_sID = GetValue(ePerson, sPrefix + MetadataConstants.c_XMLE_id);
            String sName = GetValue(ePerson, sPrefix + MetadataConstants.c_XMLE_name);
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
            oPerson.m_sEmail = GetValue(ePerson, sPrefix + MetadataConstants.c_XMLE_email);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return oPerson;
    }

    /**
     * @summary Method for changing element tag name
     * @startRealisation Sasa Stojanovic 01.02.2012.
     * @finalModification Sasa Stojanovic 01.02.2012.
     * @param eElement - element to change tag name
     * @param sTagName - new tag name
     * @return - element with changed tag name
     */
    private static Element ChangeElementTagName(Document dDoc, Element eElement, String sTagName)
    {
        Element oElementNN = dDoc.createElement(sTagName);
        try
        {
            // Copy the attributes to the new element
            NamedNodeMap nnmAttributes = eElement.getAttributes();
            for (int i = 0; i < nnmAttributes.getLength(); i++)
            {
                Attr atAttributesNN = (Attr)dDoc.importNode(nnmAttributes.item(i), true);
                oElementNN.getAttributes().setNamedItem(atAttributesNN);
            }

            // Move all the children
            while (eElement.hasChildNodes())
            {
                oElementNN.appendChild(eElement.getFirstChild());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return oElementNN;
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
