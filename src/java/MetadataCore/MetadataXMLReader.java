/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MetadataCore;

import MetadataCore.MetadataGlobal.AnnotationData;
import MetadataObjects.*;
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
     * @finalModification Dejan Milosavljevic 17.01.2012.
     * @param sEventName  - event type
     * @param dDoc - input XML document to read
     */
    private static void CallAction(String sEventName, Document dDoc)
    {
        try
        {
            if(sEventName.equals(MetadataConstants.c_ET_issue_requestNew) ||
               sEventName.equals(MetadataConstants.c_ET_issue_requestUpdate))   //if event type is new issue event
            {
                NewUpdateIssue(dDoc);
            }
            if(sEventName.equals(MetadataConstants.c_ET_commit_requestNew))
            {
                NewCommit(dDoc);
            }
            if(sEventName.equals(MetadataConstants.c_ET_person_requestNew))   //if event type is new person
            {
                NewPerson(dDoc);
            }
            if(sEventName.equals(MetadataConstants.c_ET_APICall_request))   //if event type is API Call request
            {
                APICallRequest(dDoc);
            }
            if(sEventName.equals(MetadataConstants.c_ET_member_request))   //if event type is member request
            {
                InstanceRequest(dDoc);
            }
            if(sEventName.equals(MetadataConstants.c_ET_issue_requestAnnotation)) //if event type is new issue annotation
            {
                NewIssueAnnotation(dDoc);
            }
            if(sEventName.equals(MetadataConstants.c_ET_comment_requestAnnotation)) //if event type is new comment annotation
            {
                NewCommentAnnotation(dDoc);
            }
            if(sEventName.equals(MetadataConstants.c_ET_commit_requestAnnotation)) //if event type is new commit annotation
            {
                NewCommitAnnotation(dDoc);
            }
            if(sEventName.equals(MetadataConstants.c_ET_forumPost_requestAnnotation) ||
               sEventName.equals(MetadataConstants.c_ET_wikiPost_requestAnnotation)) //if event type is new forum or wiki annotation
            {
                NewFWPostAnnotation(dDoc);
            }
            if(sEventName.equals(MetadataConstants.c_ET_mail_requestAnnotation)) //if event type is new mail annotation
            {
                NewMailAnnotation(dDoc);
            }
            if(sEventName.equals(MetadataConstants.c_ET_forumPost_requestNew)) //if event type is new forum post
            {
                NewForumPostData(dDoc);
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
     * @startRealisation Sasa Stojanovic 01.09.2011.
     * @finalModification Sasa Stojanovic 01.09.2011.
     * @param dDoc - input XML document to read
     */
    private static void NewUpdateIssue(Document dDoc)
    {
        try
        {
            String sEventId = GetEventId(dDoc);

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
                    oIssue.m_oHasReporter = GetPersonObject("s:", eReporter);
                    //oIssue.m_oHasReporter.m_oIsReporterOf = new Issue[1];
                    //oIssue.m_oHasReporter.m_oIsReporterOf[0] = oIssue;
                }           
                
                String sIssueState = GetValue(eIssue, "s:" + MetadataConstants.c_XMLE_issueStatus);
                if (sIssueState.equals("Assigned"))
                    oIssue.m_oHasState = new Assigned();
                if (sIssueState.equals("Open"))
                    oIssue.m_oHasState = new Open();
                if (sIssueState.equals("Verified"))
                    oIssue.m_oHasState = new Verified();
                if (sIssueState.equals("Resolved"))
                    oIssue.m_oHasState = new Resolved();
                if (sIssueState.equals("Closed"))
                    oIssue.m_oHasState = new Closed();

                //if (oIssue.m_oHasState != null)
                //    oIssue.m_oHasState.m_oIsStateOf = oIssue;

                String sIssueResolution = GetValue(eIssue, "s:" + MetadataConstants.c_XMLE_issueResolution);
                if (sIssueResolution.equals("Duplicate"))
                    oIssue.m_oHasResolution = new Duplicate();
                if (sIssueResolution.equals("Fixed"))
                    oIssue.m_oHasResolution = new Fixed();
                if (sIssueResolution.equals("Invalid"))
                    oIssue.m_oHasResolution = new Invalid();
                if (sIssueResolution.equals("ThirdParty"))
                    oIssue.m_oHasResolution = new ThirdParty();
                if (sIssueResolution.equals("WontFix"))
                    oIssue.m_oHasResolution = new WontFix();
                if (sIssueResolution.equals("WorksForMe"))
                    oIssue.m_oHasResolution = new WorksForMe();
                if (sIssueResolution.equals("Later"))
                    oIssue.m_oHasResolution = new Later();
                if (sIssueResolution.equals("Remind"))
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
                if (sIssueSeverity.equals("Blocker"))
                    oIssue.m_oHasSeverity = new Blocker();
                if (sIssueSeverity.equals("Critical"))
                    oIssue.m_oHasSeverity = new Critical();
                if (sIssueSeverity.equals("Major"))
                    oIssue.m_oHasSeverity = new Major();
                if (sIssueSeverity.equals("Minor"))
                    oIssue.m_oHasSeverity = new Minor();
                if (sIssueSeverity.equals("Trivial"))
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

            MetadataModel.SaveObjectNewIssue(sEventId, oIssue);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * @summary Method for reading new commit event from XML
     * @startRealisation Sasa Stojanovic 16.01.2012.
     * @finalModification Sasa Stojanovic 16.01.2012.
     * @param dDoc - input XML document to read
     */
    private static void NewCommit(Document dDoc)
    {
        try
        {
            String sEventId = GetEventId(dDoc);

            Commit oCommit = MetadataObjectFactory.CreateNewCommit();

            NodeList nlCommit = dDoc.getElementsByTagName("s:" + MetadataConstants.c_XMLE_commit);   //getting node for tag commit

            if (nlCommit != null && nlCommit.getLength() > 0)
            {
                Element eIssue = (Element) nlCommit.item(0);

                oCommit.m_oIsCommitOfRepository = new Repository();
                oCommit.m_oIsCommitOfRepository.m_sObjectURI = GetValue(eIssue, "s:" + MetadataConstants.c_XMLE_commitRepository + MetadataConstants.c_XMLE_Uri);
                
                oCommit.m_sRevisionTag = GetValue(eIssue, "s:" + MetadataConstants.c_XMLE_commitRevisionTag);
                
                NodeList nlAuthor = eIssue.getElementsByTagName("s:" + MetadataConstants.c_XMLE_commitAuthor);
                if (nlAuthor != null && nlAuthor.getLength() > 0)
                {
                    Element eAuthor = (Element) nlAuthor.item(0);
                    oCommit.m_oHasAuthor = GetPersonObject("s:", eAuthor);
                }
                
                NodeList nlCommiter = eIssue.getElementsByTagName("s:" + MetadataConstants.c_XMLE_commitCommtter);
                if (nlCommiter != null && nlCommiter.getLength() > 0)
                {
                    Element eCommiter = (Element) nlCommiter.item(0);
                    oCommit.m_oHasCommitter = GetPersonObject("s:", eCommiter);
                }

                oCommit.m_dtmCommitDate = MetadataGlobal.GetDateTime(GetValue(eIssue, "s:" + MetadataConstants.c_XMLE_commitDate));
                
                oCommit.m_sCommitMessage = GetValue(eIssue, "s:" + MetadataConstants.c_XMLE_commitMessageLog);
            }

            MetadataModel.SaveObjectNewCommit(sEventId, oCommit);

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
     * @finalModification Sasa Stojanovic 16.12.2011.
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
                
                ///////////////////////////////// issue_getDuplicates /////////////////////////////////
                if (sAPICall.equals(MetadataConstants.c_XMLAC_issue_getDuplicates))
                {
                    String sIssueDuplicatesSPARQL = "";
                            
                    NodeList nlInputParameter = dDoc.getElementsByTagName("s2:" + MetadataConstants.c_XMLE_inputParameter);   //getting node for apirequest

                    if (nlInputParameter != null && nlInputParameter.getLength() > 0)
                    {
                        for (int i = 0; i < nlInputParameter.getLength(); i++)
                        {
                            Element eInputParameter = (Element) nlInputParameter.item(i);
                            String sParamName = GetValue(eInputParameter, "s2:" + MetadataConstants.c_XMLE_name);
                            if (sParamName.equals(MetadataConstants.c_XMLV_issueDuplicatesSPARQL))
                                sIssueDuplicatesSPARQL = GetValue(eInputParameter, "s2:" + MetadataConstants.c_XMLE_value);
                        }
                    }
                    
                    MetadataModel.ac_issue_getDuplicates(sEventId, sIssueDuplicatesSPARQL);
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

    /**
     * @summary Method for reading new issue annotation event from XML.
     * @startRealisation  Dejan Milosavljevic 16.01.2012.
     * @finalModification Dejan Milosavljevic 18.01.2012.
     * @param dDoc - input XML document to read
     */
    private static void NewIssueAnnotation(Document dDoc)
    {
        try
        {
            String sEventId = GetEventId(dDoc);

            AnnotationData oAnnotation = MetadataObjectFactory.CreateNewAnnotation();

            NodeList nlAnnotation = dDoc.getElementsByTagName("s1:" + MetadataConstants.c_XMLE_annotation);   //getting node for tag annotation

            if (nlAnnotation != null && nlAnnotation.getLength() > 0)
            {
                Element eAnnotation = (Element) nlAnnotation.item(0);
            
                //URI
                oAnnotation.m_sObjectURI = GetValue(eAnnotation, "s1:" + MetadataConstants.c_XMLE_itemUri);
                
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
                            oAnnotation.oAnnotated[i].sValue = eSubjectAnnotated.getTextContent();
                        }
                    }
                    if (iDescLength > 0)
                    {
                        for (int i = 0; i < iDescLength; i++)
                        {
                            Element eDescAnnotated = (Element)nlDescAnnotated.item(i);
                            oAnnotation.oAnnotated[i + iSubjectLength] = new MetadataGlobal.AnnotationProp();
                            oAnnotation.oAnnotated[i + iSubjectLength].sName = MetadataConstants.c_XMLE_descriptionAnnotated;
                            oAnnotation.oAnnotated[i + iSubjectLength].sValue = eDescAnnotated.getTextContent();
                        }
                    }
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
                            oAnnotation.oConcepts[i].sId = GetValue(eSConcept, "s1:" + MetadataConstants.c_XMLE_id);//nlCID.item(0).getNodeValue();
                            oAnnotation.oConcepts[i].sCount = GetValue(eSConcept, "s1:" + MetadataConstants.c_XMLE_count);//nlCCount.item(0).getNodeValue();
                        }
                        for (int i = 0; i < iDescLength; i++)
                        {
                            Element eDConcept = (Element)nlDescConcepts.item(i);
                            oAnnotation.oConcepts[i + iSubjectLength] = new MetadataGlobal.ConceptProp();
                            oAnnotation.oConcepts[i + iSubjectLength].sName = MetadataConstants.c_XMLE_descriptionConcepts;
                            oAnnotation.oConcepts[i + iSubjectLength].sId = GetValue(eDConcept, "s1:" + MetadataConstants.c_XMLE_id);//nlCID.item(0).getNodeValue();
                            oAnnotation.oConcepts[i + iSubjectLength].sCount = GetValue(eDConcept, "s1:" + MetadataConstants.c_XMLE_count);//nlCCount.item(0).getNodeValue();
                        }
                    }
                }
            }

            MetadataModel.SaveObjectNewAnnotationData(sEventId, oAnnotation);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * @summary Method for reading new comment annotation event from XML.
     * @startRealisation  Dejan Milosavljevic 17.01.2012.
     * @finalModification Dejan Milosavljevic 18.01.2012.
     * @param dDoc - input XML document to read
     */
    private static void NewCommentAnnotation(Document dDoc)
    {
        try
        {
            String sEventId = GetEventId(dDoc);

            AnnotationData oAnnotation = MetadataObjectFactory.CreateNewAnnotation();

            NodeList nlAnnotation = dDoc.getElementsByTagName("s1:" + MetadataConstants.c_XMLE_annotation);   //getting node for tag annotation

            if (nlAnnotation != null && nlAnnotation.getLength() > 0)
            {
                Element eAnnotation = (Element) nlAnnotation.item(0);
            
                //URI
                oAnnotation.m_sObjectURI = GetValue(eAnnotation, "s1:" + MetadataConstants.c_XMLE_itemUri);
                
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
                            oAnnotation.oAnnotated[i].sValue = eCommentAnnotated.getTextContent();
                        }
                    }
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
                            oAnnotation.oConcepts[i].sId = GetValue(eCConcept, "s1:" + MetadataConstants.c_XMLE_id);
                            oAnnotation.oConcepts[i].sCount = GetValue(eCConcept, "s1:" + MetadataConstants.c_XMLE_count);
                        }
                    }
                }
            }

            MetadataModel.SaveObjectNewAnnotationData(sEventId, oAnnotation);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * @summary Method for reading new commit annotation event from XML.
     * @startRealisation  Dejan Milosavljevic 17.01.2012.
     * @finalModification Dejan Milosavljevic 18.01.2012.
     * @param dDoc - input XML document to read
     */
    private static void NewCommitAnnotation(Document dDoc)
    {
        try
        {
            String sEventId = GetEventId(dDoc);

            AnnotationData oAnnotation = MetadataObjectFactory.CreateNewAnnotation();

            NodeList nlAnnotation = dDoc.getElementsByTagName("s1:" + MetadataConstants.c_XMLE_annotation);   //getting node for tag annotation

            if (nlAnnotation != null && nlAnnotation.getLength() > 0)
            {
                Element eAnnotation = (Element) nlAnnotation.item(0);
            
                //URI
                oAnnotation.m_sObjectURI = GetValue(eAnnotation, "s1:" + MetadataConstants.c_XMLE_itemUri);
                
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
                            oAnnotation.oAnnotated[i].sValue = eCommitAnnotated.getTextContent();
                        }
                    }
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
                            oAnnotation.oConcepts[i].sId = GetValue(eCConcept, "s1:" + MetadataConstants.c_XMLE_id);
                            oAnnotation.oConcepts[i].sCount = GetValue(eCConcept, "s1:" + MetadataConstants.c_XMLE_count);
                        }
                    }
                }
            }

            MetadataModel.SaveObjectNewAnnotationData(sEventId, oAnnotation);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * @summary Method for reading new forum or wiki post annotation event from XML.
     * @startRealisation  Dejan Milosavljevic 17.01.2012.
     * @finalModification Dejan Milosavljevic 18.01.2012.
     * @param dDoc - input XML document to read
     */
    private static void NewFWPostAnnotation(Document dDoc)
    {
        try
        {
            String sEventId = GetEventId(dDoc);

            AnnotationData oAnnotation = MetadataObjectFactory.CreateNewAnnotation();

            NodeList nlAnnotation = dDoc.getElementsByTagName("s1:" + MetadataConstants.c_XMLE_annotation);   //getting node for tag annotation

            if (nlAnnotation != null && nlAnnotation.getLength() > 0)
            {
                Element eAnnotation = (Element) nlAnnotation.item(0);
            
                //URI
                oAnnotation.m_sObjectURI = GetValue(eAnnotation, "s1:" + MetadataConstants.c_XMLE_itemUri);
                
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
                            oAnnotation.oAnnotated[i].sValue = eTitleAnnotated.getTextContent();
                        }
                    }
                    if (iBodyLength > 0)
                    {
                        for (int i = 0; i < iBodyLength; i++)
                        {
                            Element eBodyAnnotated = (Element)nlBodyAnnotated.item(i);
                            oAnnotation.oAnnotated[i + iTitleLength] = new MetadataGlobal.AnnotationProp();
                            oAnnotation.oAnnotated[i + iTitleLength].sName = MetadataConstants.c_XMLE_bodyAnnotated;
                            oAnnotation.oAnnotated[i + iTitleLength].sValue = eBodyAnnotated.getTextContent();
                        }
                    }
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
                            oAnnotation.oConcepts[i].sId = GetValue(eTConcept, "s1:" + MetadataConstants.c_XMLE_id);
                            oAnnotation.oConcepts[i].sCount = GetValue(eTConcept, "s1:" + MetadataConstants.c_XMLE_count);
                        }
                        for (int i = 0; i < iBodyLength; i++)
                        {
                            Element eBConcept = (Element)nlBodyConcepts.item(i);
                            oAnnotation.oConcepts[i + iTitleLength] = new MetadataGlobal.ConceptProp();
                            oAnnotation.oConcepts[i + iTitleLength].sName = MetadataConstants.c_XMLE_bodyConcepts;
                            oAnnotation.oConcepts[i + iTitleLength].sId = GetValue(eBConcept, "s1:" + MetadataConstants.c_XMLE_id);
                            oAnnotation.oConcepts[i + iTitleLength].sCount = GetValue(eBConcept, "s1:" + MetadataConstants.c_XMLE_count);
                        }
                    }
                }
            }

            MetadataModel.SaveObjectNewAnnotationData(sEventId, oAnnotation);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * @summary Method for reading new mail annotation event from XML.
     * @startRealisation  Dejan Milosavljevic 17.01.2012.
     * @finalModification Dejan Milosavljevic 18.01.2012.
     * @param dDoc - input XML document to read
     */
    private static void NewMailAnnotation(Document dDoc)
    {
        try
        {
            String sEventId = GetEventId(dDoc);

            AnnotationData oAnnotation = MetadataObjectFactory.CreateNewAnnotation();

            NodeList nlAnnotation = dDoc.getElementsByTagName("s1:" + MetadataConstants.c_XMLE_annotation);   //getting node for tag annotation

            if (nlAnnotation != null && nlAnnotation.getLength() > 0)
            {
                Element eAnnotation = (Element) nlAnnotation.item(0);
            
                //URI
                oAnnotation.m_sObjectURI = GetValue(eAnnotation, "s1:" + MetadataConstants.c_XMLE_itemUri);
                
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
                            oAnnotation.oAnnotated[i].sValue = eSubjectAnnotated.getTextContent();
                        }
                    }
                    if (iBodyLength > 0)
                    {
                        for (int i = 0; i < iBodyLength; i++)
                        {
                            Element eBodyAnnotated = (Element)nlBodyAnnotated.item(i);
                            oAnnotation.oAnnotated[i + iSubjectLength] = new MetadataGlobal.AnnotationProp();
                            oAnnotation.oAnnotated[i + iSubjectLength].sName = MetadataConstants.c_XMLE_bodyAnnotated;
                            oAnnotation.oAnnotated[i + iSubjectLength].sValue = eBodyAnnotated.getTextContent();
                        }
                    }
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
                            oAnnotation.oConcepts[i].sId = GetValue(eSConcept, "s1:" + MetadataConstants.c_XMLE_id);
                            oAnnotation.oConcepts[i].sCount = GetValue(eSConcept, "s1:" + MetadataConstants.c_XMLE_count);
                        }
                        for (int i = 0; i < iBodyLength; i++)
                        {
                            Element eBConcept = (Element)nlBodyConcepts.item(i);
                            oAnnotation.oConcepts[i + iSubjectLength] = new MetadataGlobal.ConceptProp();
                            oAnnotation.oConcepts[i + iSubjectLength].sName = MetadataConstants.c_XMLE_bodyConcepts;
                            oAnnotation.oConcepts[i + iSubjectLength].sId = GetValue(eBConcept, "s1:" + MetadataConstants.c_XMLE_id);
                            oAnnotation.oConcepts[i + iSubjectLength].sCount = GetValue(eBConcept, "s1:" + MetadataConstants.c_XMLE_count);
                        }
                    }
                }
            }

            MetadataModel.SaveObjectNewAnnotationData(sEventId, oAnnotation);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * @summary Method for reading new forum post event from XML.
     * @startRealisation  Dejan Milosavljevic 17.01.2012.
     * @finalModification Dejan Milosavljevic 17.01.2012.
     * @param dDoc - input XML document to read
     */
    private static void NewForumPostData(Document dDoc)
    {
        try
        {
            String sEventId = GetEventId(dDoc);

            NewForumPost oForumPost = MetadataObjectFactory.CreateNewForumPost();

            NodeList nlForum = dDoc.getElementsByTagName("s1:" + MetadataConstants.c_XMLE_forum);   //getting node for tag forum

            if (nlForum != null && nlForum.getLength() > 0)
            {
                Element eForum = (Element) nlForum.item(0);
            
                //forumID
                oForumPost.m_sForum = new ForumEvent();
                oForumPost.m_sForum.m_sID = GetValue(eForum, "s1:" + MetadataConstants.c_XMLE_forumId);
                //oForumPost.m_sForumID = GetValue(eForum, "s1:" + MetadataConstants.c_XMLE_forumId);

                //threadID
                oForumPost.m_sForumThread = new NewForumThread();
                oForumPost.m_sForumThread.m_sID = GetValue(eForum, "s1:" + MetadataConstants.c_XMLE_threadId);
                //oForumPost.m_sThreadID = GetValue(eForum, "s1:" + MetadataConstants.c_XMLE_threadId);

                //postID
                oForumPost.m_sID = GetValue(eForum, "s1:" + MetadataConstants.c_XMLE_postId);

                ////time
                //oForumPost.m_dtmTime = MetadataGlobal.GetDateTime(GetValue(eForum, "s1:" + MetadataConstants.c_XMLE_time));

                //subject
                oForumPost.m_sSubject = GetValue(eForum, "s1:" + MetadataConstants.c_XMLE_subject);

                ////body
                oForumPost.m_sBody = GetValue(eForum, "s1:" + MetadataConstants.c_XMLE_body);

                //author
                NodeList nlAuthor = eForum.getElementsByTagName("s1:" + MetadataConstants.c_XMLE_author);
                if (nlAuthor != null && nlAuthor.getLength() > 0)
                {
                    Element eAuthor = (Element) nlAuthor.item(0);
                    oForumPost.m_oHasAuthor = GetPersonObject("s1:", eAuthor);
                }
                
                ////category
                //oForumPost.m_sCategory = GetValue(eForum, "s1:" + MetadataConstants.c_XMLE_category);
            }

            MetadataModel.SaveObjectNewForumPost(sEventId, oForumPost);
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
