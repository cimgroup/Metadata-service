/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MetadataCore;

import MetadataCore.MetadataGlobal.AnnotationData;
import MetadataObjects.*;
import MetadataObjects.Comment;
//import java.net.URI;
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
//import java.util.Formatter;
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
                System.out.println("Event type: New issue event");
                MetadataConstants.sOutputFolderName = MetadataConstants.c_ET_ALERT_KESI_IssueNew;
                MetadataGlobal.BackupProcedure(dDoc);
                NewUpdateIssue(dDoc, false);
            }
            if(sEventName.equals(MetadataConstants.c_ET_ALERT_KESI_IssueUpdate))   //if event type is update issue event
            {
                System.out.println("Event type: Update issue event");
                MetadataConstants.sOutputFolderName = MetadataConstants.c_ET_ALERT_KESI_IssueUpdate;
                MetadataGlobal.BackupProcedure(dDoc);
                NewUpdateIssue(dDoc, true);
            }
            if(sEventName.equals(MetadataConstants.c_ET_ALERT_KESI_CommitNew))
            {
                System.out.println("Event type: New commit event");
                MetadataConstants.sOutputFolderName = MetadataConstants.c_ET_ALERT_KESI_CommitNew;
                MetadataGlobal.BackupProcedure(dDoc);
                NewCommit(dDoc);
            }
            if(sEventName.equals(MetadataConstants.c_ET_ALERT_MLSensor_MailNew))
            {
                System.out.println("Event type: New mail event");
                MetadataConstants.sOutputFolderName = MetadataConstants.c_ET_ALERT_MLSensor_MailNew;
                MetadataGlobal.BackupProcedure(dDoc);
                NewMailLight(dDoc);
            }
            if(sEventName.equals(MetadataConstants.c_ET_ALERT_WikiSensor_ArticleAdded))
            {
                System.out.println("Event type: New wiki page event");
                MetadataConstants.sOutputFolderName = MetadataConstants.c_ET_ALERT_WikiSensor_ArticleAdded;
                MetadataGlobal.BackupProcedure(dDoc);
                NewUpdateWikiPage(dDoc, false);
            }
            if(sEventName.equals(MetadataConstants.c_ET_ALERT_WikiSensor_ArticleModified))
            {
                System.out.println("Event type: Update wiki page event");
                MetadataConstants.sOutputFolderName = MetadataConstants.c_ET_ALERT_WikiSensor_ArticleModified;
                MetadataGlobal.BackupProcedure(dDoc);
                NewUpdateWikiPage(dDoc, true);
            }
            if(sEventName.equals(MetadataConstants.c_ET_ALERT_WikiSensor_ArticleDeleted))
            {
                System.out.println("Event type: Remove wiki page event");
                MetadataConstants.sOutputFolderName = MetadataConstants.c_ET_ALERT_WikiSensor_ArticleDeleted;
                MetadataGlobal.BackupProcedure(dDoc);
                RemoveWikiPage(dDoc);
            }
//            if(sEventName.equals(MetadataConstants.c_ET_person_requestNew))   //if event type is new person
//            {
//                NewPerson(dDoc);
//            }
//            if(sEventName.equals(MetadataConstants.c_ET_APICall_request))   //if event type is API Call request
            if(sEventName.equals(MetadataConstants.c_ET_ALERT_KESI_APICallRequest) ||   //if event type is API Call request
               sEventName.equals(MetadataConstants.c_ET_ALERT_KEUI_APICallRequest) ||
               sEventName.equals(MetadataConstants.c_ET_ALERT_STARDOM_APICallRequest) ||
               sEventName.equals(MetadataConstants.c_ET_ALERT_Panteon_APICallRequest) ||
               sEventName.equals(MetadataConstants.c_ET_ALERT_UI_APICallRequest) ||
               sEventName.equals(MetadataConstants.c_ET_ALERT_Search_APICallRequest))
            {
                System.out.print("Event type: API Call event; ");
                MetadataConstants.sOutputFolderName = MetadataConstants.c_ET_ALERT_UI_APICallRequest;
                APICallRequest(dDoc);
            }
//            if(sEventName.equals(MetadataConstants.c_ET_member_request))   //if event type is member request
//            {
//                InstanceRequest(dDoc);
//            }
            if(sEventName.equals(MetadataConstants.c_ET_ALERT_KEUI_IssueNew_Annotated)) //if event type is new issue annotation
            {
                System.out.println("Event type: New issue annotation event");
                MetadataConstants.sOutputFolderName = MetadataConstants.c_ET_ALERT_KEUI_IssueNew_Annotated;
                MetadataGlobal.BackupProcedure(dDoc);
                NewUpdateIssueAnnotation(dDoc, true);
            }
            if(sEventName.equals(MetadataConstants.c_ET_ALERT_KEUI_IssueUpdate_Annotated)) //if event type is new comment annotation
            {
                System.out.println("Event type: Update issue annotation event");
                MetadataConstants.sOutputFolderName = MetadataConstants.c_ET_ALERT_KEUI_IssueUpdate_Annotated;
                MetadataGlobal.BackupProcedure(dDoc);
                NewUpdateIssueAnnotation(dDoc, false);
            }
            if(sEventName.equals(MetadataConstants.c_ET_ALERT_KEUI_CommitNew_Annotated)) //if event type is new commit annotation
            {
                System.out.println("Event type: New commit annotation event");
                MetadataConstants.sOutputFolderName = MetadataConstants.c_ET_ALERT_KEUI_CommitNew_Annotated;
                MetadataGlobal.BackupProcedure(dDoc);
                NewCommitAnnotation(dDoc);
            }
            if(sEventName.equals(MetadataConstants.c_ET_ALERT_KEUI_ForumPostNew_Annotated)) //if event type is new forum post annotation
            {
                System.out.println("Event type: New forum post annotation event");
                MetadataConstants.sOutputFolderName = MetadataConstants.c_ET_ALERT_KEUI_ForumPostNew_Annotated;
                MetadataGlobal.BackupProcedure(dDoc);
                NewForumPostAnnotation(dDoc);
            }
            if(sEventName.equals(MetadataConstants.c_ET_ALERT_KEUI_WikiPostNew_Annotated)) //if event type is new wiki post annotation
            {
                System.out.println("Event type: New wiki post annotation event");
                MetadataConstants.sOutputFolderName = MetadataConstants.c_ET_ALERT_KEUI_WikiPostNew_Annotated;
                MetadataGlobal.BackupProcedure(dDoc);
                //NewWikiPostAnnotation(dDoc);
            }
            if(sEventName.equals(MetadataConstants.c_ET_ALERT_KEUI_MailNew_Annotated)) //if event type is new mail annotation
            {
                System.out.println("Event type: New mail annotation event");
                MetadataConstants.sOutputFolderName = MetadataConstants.c_ET_ALERT_KEUI_MailNew_Annotated;
                MetadataGlobal.BackupProcedure(dDoc);
                NewMailAnnotation(dDoc);
            }
            if(sEventName.equals(MetadataConstants.c_ET_ALERT_ForumSensor_ForumPostNew)) //if event type is new forum post
            {
                System.out.println("Event type: New forum post event");
                MetadataConstants.sOutputFolderName = MetadataConstants.c_ET_ALERT_ForumSensor_ForumPostNew;
                MetadataGlobal.BackupProcedure(dDoc);
                NewForumPostDataLight(dDoc);
            }
            if(sEventName.equals(MetadataConstants.c_ET_ALERT_STARDOM_CompetencyNew))   //if event type is new competence event
            {
                System.out.println("Event type: New competency event");
                MetadataConstants.sOutputFolderName = MetadataConstants.c_ET_ALERT_STARDOM_CompetencyNew;
                MetadataGlobal.BackupProcedure(dDoc);
                NewUpdateCompetence(dDoc, false);
            }
            if(sEventName.equals(MetadataConstants.c_ET_ALERT_STARDOM_CompetencyUpdate))   //if event type is update competence event
            {
                System.out.println("Event type: Update competency event");
                MetadataConstants.sOutputFolderName = MetadataConstants.c_ET_ALERT_STARDOM_CompetencyUpdate;
                MetadataGlobal.BackupProcedure(dDoc);
                NewUpdateCompetence(dDoc, true);
            }
            if(sEventName.equals(MetadataConstants.c_ET_ALERT_STARDOM_IdentityNew))   //if event type is new identity event
            {
                System.out.println("Event type: New identity event");
                MetadataConstants.sOutputFolderName = MetadataConstants.c_ET_ALERT_STARDOM_IdentityNew;
                MetadataGlobal.BackupProcedure(dDoc);
                NewIdentity(dDoc);
            }
            if(sEventName.equals(MetadataConstants.c_ET_ALERT_STARDOM_IdentityUpdate))   //if event type is update identity event
            {
                System.out.println("Event type: Update identity event");
                MetadataConstants.sOutputFolderName = MetadataConstants.c_ET_ALERT_STARDOM_IdentityUpdate;
                MetadataGlobal.BackupProcedure(dDoc);
                UpdateIdentity(dDoc);
            }
            if(sEventName.equals(MetadataConstants.c_ET_ALERT_STARDOM_IdentityRemove))   //if event type is remove identity event
            {
                System.out.println("Event type: Remove identity event");
                MetadataConstants.sOutputFolderName = MetadataConstants.c_ET_ALERT_STARDOM_IdentityRemove;
                MetadataGlobal.BackupProcedure(dDoc);
                RemoveIdentity(dDoc);
            }
            if(sEventName.equals(MetadataConstants.c_ET_ALERT_OCELOt_ConceptNew))   //if event type is remove identity event
            {
                System.out.println("Event type: New concept event");
                MetadataConstants.sOutputFolderName = MetadataConstants.c_ET_ALERT_OCELOt_ConceptNew;
                MetadataGlobal.BackupProcedure(dDoc);
                NewConcept(dDoc);
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

                //IssueTracker
                NodeList nlIssueTracker = eIssue.getElementsByTagName("s:" + MetadataConstants.c_XMLE_issueTracker);
                if (nlIssueTracker != null && nlIssueTracker.getLength() > 0)
                {
                    Element eIssueTracker = (Element)nlIssueTracker.item(0);
                    oIssue.m_oHasTracker = new Tracker();

                    oIssue.m_oHasTracker.m_sID = GetValue(eIssueTracker, "s:" + MetadataConstants.c_XMLE_issueTracker + MetadataConstants.c_XMLE_Id);
                    oIssue.m_oHasTracker.m_sURL = GetValue(eIssueTracker, "s:" + MetadataConstants.c_XMLE_issueTrackerURL);
                    oIssue.m_oHasTracker.m_sType = GetValue(eIssueTracker, "s:" + MetadataConstants.c_XMLE_issueTrackerType);
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
     * @finalModification Sasa Stojanovic 22.10.2012.
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
                oCommit.m_oIsCommitOfRepository.m_sID = GetValue(eCommit, "s:" + MetadataConstants.c_XMLE_commitRepository + MetadataConstants.c_XMLE_Uri);
                
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
                        
                        //oCommit.m_oHasFile[i].m_sID = GetValue(eFile, "s:" + MetadataConstants.c_XMLE_file + MetadataConstants.c_XMLE_Id);
                        oCommit.m_oHasFile[i].m_sID = GetValue(eFile, "s:" + MetadataConstants.c_XMLE_fileName);
                        String sFileId = oCommit.m_oHasFile[i].m_sID;
                        
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
                        
                        oCommit.m_oHasFile[i].m_sName = GetValue(eFile, "s:" + MetadataConstants.c_XMLE_fileName);
                        oCommit.m_oHasFile[i].m_sBranch = GetValue(eFile, "s:" + MetadataConstants.c_XMLE_fileBranch);
                        
                        NodeList nlModule = eFile.getElementsByTagName("s:" + MetadataConstants.c_XMLE_fileModules);
                        if (nlModule != null && nlModule.getLength() > 0)
                        {
                            oCommit.m_oHasFile[i].m_oHasModule = new Module[nlModule.getLength()];
                            for (int j = 0; j < nlModule.getLength(); j++)
                            {
                                Element eModule = (Element)nlModule.item(j);
                                oCommit.m_oHasFile[i].m_oHasModule[j] = new Module();

                                //oCommit.m_oHasFile[i].m_oHasModule[j].m_sID = GetValue(eModule, "s:" + MetadataConstants.c_XMLE_module + MetadataConstants.c_XMLE_Id);
                                oCommit.m_oHasFile[i].m_oHasModule[j].m_sID = sFileId + "*" + GetValue(eModule, "s:" + MetadataConstants.c_XMLE_moduleName);
                                String sModuleId = oCommit.m_oHasFile[i].m_oHasModule[j].m_sID;
                                
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

                                        //oCommit.m_oHasFile[i].m_oHasModule[j].m_oHasMethod[k].m_sID = GetValue(eMethod, "s:" + MetadataConstants.c_XMLE_method + MetadataConstants.c_XMLE_Id);
                                        oCommit.m_oHasFile[i].m_oHasModule[j].m_oHasMethod[k].m_sID = sModuleId + "*" + GetValue(eMethod, "s:" + MetadataConstants.c_XMLE_methodName);
                                        
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
            
            NodeList nlProduct = dDoc.getElementsByTagName("s:" + MetadataConstants.c_XMLE_commitProduct);   //getting node for tag commitProduct
            if (nlProduct != null && nlProduct.getLength() > 0)
            {
                Element eProduct = (Element) nlProduct.item(0);

                oCommit.m_oIsCommitOf = new Component();
                oCommit.m_oIsCommitOf.m_sID = GetValue(eProduct, "s:" + MetadataConstants.c_XMLE_productComponent + MetadataConstants.c_XMLE_Id);
                oCommit.m_oIsCommitOf.m_oIsComponentOf = new Product();
                oCommit.m_oIsCommitOf.m_oIsComponentOf.m_sID = GetValue(eProduct, "s:" + MetadataConstants.c_XMLE_product + MetadataConstants.c_XMLE_Id);
                oCommit.m_oIsCommitOf.m_oIsComponentOf.m_sVersion = GetValue(eProduct, "s:" + MetadataConstants.c_XMLE_productVersion);
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
    
    /**
     * @summary Method for reading new mail event from XML (only data that is used by other components)
     * @startRealisation Sasa Stojanovic 19.10.2012.
     * @finalModification Sasa Stojanovic 19.02.2012.
     * @param dDoc - input XML document to read
     * @return - returns Mail object
     */
    public static Mail NewMailLight(Document dDoc)
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

                oMail.m_oFrom = new foaf_Person();
                oMail.m_oFrom.m_sID = GetValue(eMail, "r1:" + MetadataConstants.c_XMLE_from);
                oMail.m_oFrom.m_sEmail = GetValue(eMail, "r1:" + MetadataConstants.c_XMLE_from);
                                
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
    
    /**
     * @summary Method for reading new/update wiki page event from XML
     * @startRealisation Sasa Stojanovic 08.08.2012.
     * @finalModification Sasa Stojanovic 08.08.2012.
     * @param dDoc - input XML document to read
     * @return - returns wiki page object
     */
    public static WikiPage NewUpdateWikiPage(Document dDoc, boolean bIsUpdate)
    {
        try
        {
            String sEventId = GetEventId(dDoc);
            Element eOriginalData = null;  //element for original data

            WikiPage oWikiPage = MetadataObjectFactory.CreateNewWikiPage();

            NodeList nlWikiPage = dDoc.getElementsByTagName("r2:" + MetadataConstants.c_XMLE_wikiSensor);   //getting node for tag commit

            if (nlWikiPage != null && nlWikiPage.getLength() > 0)
            {
                Element eWikiPage = (Element) nlWikiPage.item(0);
                eOriginalData = eWikiPage;
               
                oWikiPage.m_sID = GetValue(eWikiPage, "r2:" + MetadataConstants.c_XMLE_id);
                oWikiPage.m_sSource = GetValue(eWikiPage, "r2:" + MetadataConstants.c_XMLE_source);
                oWikiPage.m_sURL = GetValue(eWikiPage, "r2:" + MetadataConstants.c_XMLE_url);
                oWikiPage.m_sTitle = GetValue(eWikiPage, "r2:" + MetadataConstants.c_XMLE_title);
                oWikiPage.m_sRawText = GetValue(eWikiPage, "r2:" + MetadataConstants.c_XMLE_rawText);
                NodeList nlAuthor = eWikiPage.getElementsByTagName("r2:" + MetadataConstants.c_XMLE_user);
                if (nlAuthor != null && nlAuthor.getLength() > 0)
                {
                    Element eAuthor = (Element) nlAuthor.item(0);
                    oWikiPage.m_oAuthor = GetPersonObject("r2:", eAuthor);
                }
                oWikiPage.m_sEditComment = GetValue(eWikiPage, "r2:" + MetadataConstants.c_XMLE_comment);
                String sIsMinor = GetValue(eWikiPage, "r2:" + MetadataConstants.c_XMLE_isMinor);
                oWikiPage.m_bIsMinor = sIsMinor.equals("true");
            }
            
            MetadataModel.SaveObjectNewWikiPage(sEventId, eOriginalData, oWikiPage, bIsUpdate);
            
            return oWikiPage;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * @summary Method for reading remove wiki page event from XML
     * @startRealisation Sasa Stojanovic 08.08.2012.
     * @finalModification Sasa Stojanovic 08.08.2012.
     * @param dDoc - input XML document to read
     * @return - returns wiki page object
     */
    public static WikiPage RemoveWikiPage(Document dDoc)
    {
        try
        {
            String sEventId = GetEventId(dDoc);
            Element eOriginalData = null;  //element for original data

            WikiPage oWikiPage = MetadataObjectFactory.CreateNewWikiPage();

            NodeList nlWikiPage = dDoc.getElementsByTagName("r2:" + MetadataConstants.c_XMLE_wikiSensor);   //getting node for tag commit

            if (nlWikiPage != null && nlWikiPage.getLength() > 0)
            {
                Element eWikiPage = (Element) nlWikiPage.item(0);
                eOriginalData = eWikiPage;
               
                oWikiPage.m_sID = GetValue(eWikiPage, "r2:" + MetadataConstants.c_XMLE_id);
                oWikiPage.m_sSource = GetValue(eWikiPage, "r2:" + MetadataConstants.c_XMLE_source);
                NodeList nlAuthor = eWikiPage.getElementsByTagName("r2:" + MetadataConstants.c_XMLE_user);
                if (nlAuthor != null && nlAuthor.getLength() > 0)
                {
                    Element eAuthor = (Element) nlAuthor.item(0);
                    oWikiPage.m_oAuthor = GetPersonObject("r2:", eAuthor);
                }
                oWikiPage.m_sEditComment = GetValue(eWikiPage, "r2:" + MetadataConstants.c_XMLE_comment);
            }
            
            MetadataModel.RemoveWikiPage(sEventId, eOriginalData, oWikiPage);
            
            return oWikiPage;
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
     * @finalModification Dejan Milosavljevic 18.05.2012.
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
                    System.out.println("API Call type: sparql");
                    
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
                    System.out.println("API Call type: issue.getAllForProduct");
                    
                    String sProductID = "";
                    Date dtmFromDate = new Date();
                            
                    NodeList nlInputParameter = dDoc.getElementsByTagName("s2:" + MetadataConstants.c_XMLE_inputParameter);   //getting node for apirequest

                    if (nlInputParameter != null && nlInputParameter.getLength() > 0)
                    {
                        for (int i = 0; i < nlInputParameter.getLength(); i++)
                        {
                            Element eInputParameter = (Element) nlInputParameter.item(i);
                            String sParamName = GetValue(eInputParameter, "s2:" + MetadataConstants.c_XMLE_name);
                            if (sParamName.equals(MetadataConstants.c_XMLV_productID))
                                sProductID = GetValue(eInputParameter, "s2:" + MetadataConstants.c_XMLE_value);
                            if (sParamName.equals(MetadataConstants.c_XMLV_fromDate))
                                dtmFromDate = MetadataGlobal.GetDateTime(GetValue(eInputParameter, "s2:" + MetadataConstants.c_XMLE_value));
                        }
                    }
                    
                    MetadataModel.ac_issue_getAllForProduct(sEventId, sProductID, dtmFromDate);
                }
                
                ///////////////////////////////// issue_getAllForIdentity /////////////////////////////////
                if (sAPICall.equals(MetadataConstants.c_XMLAC_issue_getAllForIdentity))
                {
                    System.out.println("API Call type: issue.getAllForIdentity");
                    
                    String sIdentityId = "";
                            
                    NodeList nlInputParameter = dDoc.getElementsByTagName("s2:" + MetadataConstants.c_XMLE_inputParameter);   //getting node for apirequest

                    if (nlInputParameter != null && nlInputParameter.getLength() > 0)
                    {
                        for (int i = 0; i < nlInputParameter.getLength(); i++)
                        {
                            Element eInputParameter = (Element) nlInputParameter.item(i);
                            String sParamName = GetValue(eInputParameter, "s2:" + MetadataConstants.c_XMLE_name);
                            if (sParamName.equals(MetadataConstants.c_XMLV_uuid))
                                sIdentityId = GetValue(eInputParameter, "s2:" + MetadataConstants.c_XMLE_value);
                        }
                    }
                    
                    MetadataModel.ac_issue_getAllForIdentity(sEventId, sIdentityId);
                }
                
                ///////////////////////////////// issue_getAllForMethod /////////////////////////////////
                if (sAPICall.equals(MetadataConstants.c_XMLAC_issue_getAllForMethod))
                {
                    System.out.println("API Call type: issue.getAllForMethod");
                    
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
                    System.out.println("API Call type: issue.getAnnotationStatus");
                    
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
                    System.out.println("API Call type: issue.getInfo");
                    
                    String sIssueID = "";
                            
                    NodeList nlInputParameter = dDoc.getElementsByTagName("s2:" + MetadataConstants.c_XMLE_inputParameter);   //getting node for apirequest

                    if (nlInputParameter != null && nlInputParameter.getLength() > 0)
                    {
                        for (int i = 0; i < nlInputParameter.getLength(); i++)
                        {
                            Element eInputParameter = (Element) nlInputParameter.item(i);
                            String sParamName = GetValue(eInputParameter, "s2:" + MetadataConstants.c_XMLE_name);
                            if (sParamName.equals(MetadataConstants.c_XMLV_issueID))
                                sIssueID = GetValue(eInputParameter, "s2:" + MetadataConstants.c_XMLE_value);
                        }
                    }
                    
                    MetadataModel.ac_issue_getInfo(sEventId, sIssueID);
                }
                
                ///////////////////////////////// issue_getExplicitDuplicates /////////////////////////////////
                if (sAPICall.equals(MetadataConstants.c_XMLAC_issue_getExplicitDuplicates))
                {
                    System.out.println("API Call type: issue.getExplicitDuplicates");
                    
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
                    System.out.println("API Call type: issue.getSubjectAreas");
                    
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
                    System.out.println("API Call type: issue.getSubjectAreasForOpen");
                    
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
                    System.out.println("API Call type: issue.getOpen");
                    
                    String sProductID = "";
                            
                    NodeList nlInputParameter = dDoc.getElementsByTagName("s2:" + MetadataConstants.c_XMLE_inputParameter);   //getting node for apirequest

                    if (nlInputParameter != null && nlInputParameter.getLength() > 0)
                    {
                        for (int i = 0; i < nlInputParameter.getLength(); i++)
                        {
                            Element eInputParameter = (Element) nlInputParameter.item(i);
                            String sParamName = GetValue(eInputParameter, "s2:" + MetadataConstants.c_XMLE_name);
                            if (sParamName.equals(MetadataConstants.c_XMLV_productID))
                                sProductID = GetValue(eInputParameter, "s2:" + MetadataConstants.c_XMLE_value);
                        }
                    }
                    
                    MetadataModel.ac_issue_getOpen(sEventId, sProductID);
                }
                
                ///////////////////////////////// person_getInfo /////////////////////////////////
                if (sAPICall.equals(MetadataConstants.c_XMLAC_person_getInfo))
                {
                    System.out.println("API Call type: person.getInfo");
                    
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
                    System.out.println("API Call type: person.getAllForEmail");
                    
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
                    System.out.println("API Call type: identity.getForPerson");
                    
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
                    
                    MetadataModel.ac_identity_getForPerson(sEventId, sPersonUri);
                }
                                
                ///////////////////////////////// competency_getForPerson /////////////////////////////////
                if (sAPICall.equals(MetadataConstants.c_XMLAC_competency_getForPerson))
                {
                    System.out.println("API Call type: competency.getForPerson");
                    
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
                    System.out.println("API Call type: competency.getPersonForIssue");
                    
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
                
                ///////////////////////////////// method_getAllForIdentity /////////////////////////////////
                if (sAPICall.equals(MetadataConstants.c_XMLAC_method_getAllForIdentity))
                {
                    System.out.println("API Call type: method.getAllForIdentity");
                    
                    String sIdentityId = "";
                            
                    NodeList nlInputParameter = dDoc.getElementsByTagName("s2:" + MetadataConstants.c_XMLE_inputParameter);   //getting node for apirequest

                    if (nlInputParameter != null && nlInputParameter.getLength() > 0)
                    {
                        for (int i = 0; i < nlInputParameter.getLength(); i++)
                        {
                            Element eInputParameter = (Element) nlInputParameter.item(i);
                            String sParamName = GetValue(eInputParameter, "s2:" + MetadataConstants.c_XMLE_name);
                            if (sParamName.equals(MetadataConstants.c_XMLV_uuid))
                                sIdentityId = GetValue(eInputParameter, "s2:" + MetadataConstants.c_XMLE_value);
                        }
                    }
                    
                    MetadataModel.ac_method_getAllForIdentity(sEventId, sIdentityId);
                }
                
                ///////////////////////////////// method_getRelatedCode /////////////////////////////////
                if (sAPICall.equals(MetadataConstants.c_XMLAC_method_getRelatedCode))
                {
                    System.out.println("API Call type: method.getRelatedCode");
                    
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
                    System.out.println("API Call type: issue.getRelatedToKeyword");
                    
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
                    System.out.println("API Call type: commit.getRelatedToKeyword");
                    
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
                
                ///////////////////////////////// commit_getAllForProduct /////////////////////////////////
                if (sAPICall.equals(MetadataConstants.c_XMLAC_commit_getAllForProduct))
                {
                    System.out.println("API Call type: commit.getAllForProduct");
                    
                    String sProductID = "";
                    Date dtmFromDate = new Date();
                            
                    NodeList nlInputParameter = dDoc.getElementsByTagName("s2:" + MetadataConstants.c_XMLE_inputParameter);   //getting node for apirequest

                    if (nlInputParameter != null && nlInputParameter.getLength() > 0)
                    {
                        for (int i = 0; i < nlInputParameter.getLength(); i++)
                        {
                            Element eInputParameter = (Element) nlInputParameter.item(i);
                            String sParamName = GetValue(eInputParameter, "s2:" + MetadataConstants.c_XMLE_name);
                            if (sParamName.equals(MetadataConstants.c_XMLV_productID))
                                sProductID = GetValue(eInputParameter, "s2:" + MetadataConstants.c_XMLE_value);
                            if (sParamName.equals(MetadataConstants.c_XMLV_fromDate))
                                dtmFromDate = MetadataGlobal.GetDateTime(GetValue(eInputParameter, "s2:" + MetadataConstants.c_XMLE_value));
                        }
                    }
                    
                    MetadataModel.ac_commit_getAllForProduct(sEventId, sProductID, dtmFromDate);
                }
                
                ///////////////////////////////// commit_getInfo /////////////////////////////////
                if (sAPICall.equals(MetadataConstants.c_XMLAC_commit_getInfo))
                {
                    System.out.println("API Call type: commit.getInfo");
                    
                    String sCommitUri = "";
                            
                    NodeList nlInputParameter = dDoc.getElementsByTagName("s2:" + MetadataConstants.c_XMLE_inputParameter);   //getting node for apirequest

                    if (nlInputParameter != null && nlInputParameter.getLength() > 0)
                    {
                        for (int i = 0; i < nlInputParameter.getLength(); i++)
                        {
                            Element eInputParameter = (Element) nlInputParameter.item(i);
                            String sParamName = GetValue(eInputParameter, "s2:" + MetadataConstants.c_XMLE_name);
                            if (sParamName.equals(MetadataConstants.c_XMLV_commitUri))
                                sCommitUri = GetValue(eInputParameter, "s2:" + MetadataConstants.c_XMLE_value);
                        }
                    }
                    
                    MetadataModel.ac_commit_getInfo(sEventId, sCommitUri);
                }
                
                ///////////////////////////////// mail_getAllForProduct /////////////////////////////////
                if (sAPICall.equals(MetadataConstants.c_XMLAC_mail_getAllForProduct))
                {
                    System.out.println("API Call type: mail.getAllForProduct");
                    
                    Date dtmFromDate = new Date();
                            
                    NodeList nlInputParameter = dDoc.getElementsByTagName("s2:" + MetadataConstants.c_XMLE_inputParameter);   //getting node for apirequest

                    if (nlInputParameter != null && nlInputParameter.getLength() > 0)
                    {
                        for (int i = 0; i < nlInputParameter.getLength(); i++)
                        {
                            Element eInputParameter = (Element) nlInputParameter.item(i);
                            String sParamName = GetValue(eInputParameter, "s2:" + MetadataConstants.c_XMLE_name);
                            if (sParamName.equals(MetadataConstants.c_XMLV_fromDate))
                                dtmFromDate = MetadataGlobal.GetDateTime(GetValue(eInputParameter, "s2:" + MetadataConstants.c_XMLE_value));
                        }
                    }
                    
                    MetadataModel.ac_mail_getAllForProduct(sEventId, dtmFromDate);
                }
                
                ///////////////////////////////// forumPost_getAllForProduct /////////////////////////////////
                if (sAPICall.equals(MetadataConstants.c_XMLAC_forumPost_getAllForProduct))
                {
                    System.out.println("API Call type: forumPost.getAllForProduct");
                    
                    Date dtmFromDate = new Date();
                            
                    NodeList nlInputParameter = dDoc.getElementsByTagName("s2:" + MetadataConstants.c_XMLE_inputParameter);   //getting node for apirequest

                    if (nlInputParameter != null && nlInputParameter.getLength() > 0)
                    {
                        for (int i = 0; i < nlInputParameter.getLength(); i++)
                        {
                            Element eInputParameter = (Element) nlInputParameter.item(i);
                            String sParamName = GetValue(eInputParameter, "s2:" + MetadataConstants.c_XMLE_name);
                            if (sParamName.equals(MetadataConstants.c_XMLV_fromDate))
                                dtmFromDate = MetadataGlobal.GetDateTime(GetValue(eInputParameter, "s2:" + MetadataConstants.c_XMLE_value));
                        }
                    }
                    
                    MetadataModel.ac_forumPost_getAllForProduct(sEventId, dtmFromDate);
                }
                
                ///////////////////////////////// file_getAll /////////////////////////////////
                if (sAPICall.equals(MetadataConstants.c_XMLAC_file_getAll))
                {
                    System.out.println("API Call type: file.getAll");
                    
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
                    System.out.println("API Call type: email.getRelatedToKeyword");
                    
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
                    System.out.println("API Call type: post.getRelatedToKeyword");
                    
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
                    System.out.println("API Call type: wiki.getRelatedToKeyword");
                    
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
                    System.out.println("API Call type: issue.getRelatedToIssue");
                    
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
                    System.out.println("API Call type: commit.getRelatedToIssue");
                    
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
                    System.out.println("API Call type: email.getRelatedToIssue");
                    
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
                    System.out.println("API Call type: post.getRelatedToIssue");
                    
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
                    System.out.println("API Call type: wiki.getRelatedToIssue");
                    
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
                    System.out.println("API Call type: instance.getAllForConcept");
                    
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
     * @finalModification Dejan Milosavljevic 19.04.2012.
     * @param dDoc - input XML document to read
     * @param bNew - new or update
     * @return - returns AnnotationData object
     */
    public static AnnotationData NewUpdateIssueAnnotation(Document dDoc, boolean bNew)
    {
        try
        {
            //String sEventId = GetEventId(dDoc);

            AnnotationData oAnnotation = MetadataObjectFactory.CreateNewAnnotation();
            String sIssueUri = null;
            String[] sCommentUri = null;
            
            NodeList nlMdservice = dDoc.getElementsByTagName("o:" + MetadataConstants.c_XMLE_mdservice);
            if (nlMdservice != null && nlMdservice.getLength() > 0)
            {
                Element eMdservice = (Element) nlMdservice.item(0);
                //String sTag = "o:" + MetadataConstants.c_XMLE_issue + MetadataConstants.c_XMLE_Uri;
                String sIssueTag = "o:" + MetadataConstants.c_XMLE_issue + MetadataConstants.c_XMLE_Uri;
  
                //URI for Issue //Dejan Milosavljevic 17.04.2012.
                //oAnnotation.m_sObjectURI = GetValue(eMdservice, sTag);
                sIssueUri = GetValue(eMdservice, sIssueTag);
                
                //URIs for Comments //Dejan Milosavljevic 17.04.2012.
                String sCommentTag = "o:" + MetadataConstants.c_XMLE_comment + MetadataConstants.c_XMLE_Uri;
                NodeList nlCommentUri = eMdservice.getElementsByTagName("o:" + MetadataConstants.c_XMLE_issueComment);
                if (nlCommentUri != null && nlCommentUri.getLength() > 0)
                {
                    int iCommentCount = nlCommentUri.getLength();
                    sCommentUri = new String[iCommentCount];
                    for (int i = 0; i < iCommentCount; i++)
                    {
                        Element eCommentUri = (Element)nlCommentUri.item(i);
                        sCommentUri[i] = GetValue(eCommentUri, sCommentTag);
                    }
                }
            }

            NodeList nlAnnotation = dDoc.getElementsByTagName("s1:" + MetadataConstants.c_XMLE_keui);   //getting node for tag keui

            if (nlAnnotation != null && nlAnnotation.getLength() > 0)
            {
                Element eAnnotation = (Element) nlAnnotation.item(0);
            
                //oAnnotation.iItemId = Integer.parseInt(GetValue(eAnnotation, "s1:" + MetadataConstants.c_XMLE_itemId)); //Dejan Milosavljevic 17.04.2012.
                String sTtreadID = GetValue(eAnnotation, "s1:" + MetadataConstants.c_XMLE_threadId); //Dejan Milosavljevic 18.04.2012.
                if (!sTtreadID.isEmpty()) oAnnotation.iThreadId = Integer.parseInt(sTtreadID);       //Dejan Milosavljevic 18.04.2012.
                
//              //URI
//              oAnnotation.m_sObjectURI = GetValue(eAnnotation, "s1:" + MetadataConstants.c_XMLE_itemUri);
                
                //Annotations
                NodeList nlDescriptionAnnotated = eAnnotation.getElementsByTagName("s1:" + MetadataConstants.c_XMLE_issueDescriptionAnnotated);
                NodeList nlCommentText = eAnnotation.getElementsByTagName("s1:" + MetadataConstants.c_XMLE_issueComment);
                //NodeList nlCommentAnnotated = eAnnotation.getElementsByTagName("s1:" + MetadataConstants.c_XMLE_commentTextAnnotated);
                //if (nlDescriptionAnnotated != null && nlCommentAnnotated != null)
                if (nlDescriptionAnnotated != null && nlCommentText != null)
                {
                    int iDescriptionLength = nlDescriptionAnnotated.getLength();
                    //int iCommentLength = nlCommentAnnotated.getLength();
                    int iCommentLength = nlCommentText.getLength();
                    //oAnnotation.oAnnotated = new MetadataGlobal.AnnotationProp[iDescriptionLength + iCommentLength];
                    oAnnotation.oAnnotated = new MetadataGlobal.AnnotationProp[1 + iCommentLength];
                    
                    //Description
                    if (iDescriptionLength == 1)  //Dejan Milosavljevic 17.04.2012.
                    {
                        Element eDescriptionAnnotated = (Element)nlDescriptionAnnotated.item(0);
                        oAnnotation.oAnnotated[0] = new MetadataGlobal.AnnotationProp();
                        oAnnotation.oAnnotated[0].sName = MetadataConstants.c_XMLE_issueDescriptionAnnotated;
                        oAnnotation.oAnnotated[0].SetAnnotationText(eDescriptionAnnotated);
                        oAnnotation.oAnnotated[0].sHasObject = sIssueUri;
                    }
                    else //Dejan Milosavljevic 19.04.2012.
                    {
                        oAnnotation.oAnnotated[0] = new MetadataGlobal.AnnotationProp();
                        oAnnotation.oAnnotated[0].sName = MetadataConstants.c_XMLE_issueDescriptionAnnotated;
                        oAnnotation.oAnnotated[0].sValue = null;
                        oAnnotation.oAnnotated[0].sHasObject = sIssueUri;
                        iDescriptionLength = 1;
                    }

                    //Description concepts
                    NodeList nlDescriptionConcepts = eAnnotation.getElementsByTagName("s1:" + MetadataConstants.c_XMLE_issueDescriptionConcepts);
                    if (nlDescriptionConcepts != null && nlDescriptionConcepts.getLength() > 0)
                    {
                        Element eDescriptionConcepts = (Element) nlDescriptionConcepts.item(0);
                        NodeList nlDescConcepts = eDescriptionConcepts.getElementsByTagName("s1:" + MetadataConstants.c_XMLE_concept);
                        int iDescriptionCLength = nlDescConcepts.getLength();
                        oAnnotation.oAnnotated[0].oConcepts = new MetadataGlobal.ConceptProp[iDescriptionCLength];
                        for (int i = 0; i < iDescriptionCLength; i++)
                        {
                            Element eDescConcept = (Element)nlDescConcepts.item(i);
                            oAnnotation.oAnnotated[0].oConcepts[i] = new MetadataGlobal.ConceptProp();
                            oAnnotation.oAnnotated[0].oConcepts[i].sName = MetadataConstants.c_XMLE_issueDescriptionConcepts;
                            oAnnotation.oAnnotated[0].oConcepts[i].sUri = GetValue(eDescConcept, "s1:" + MetadataConstants.c_XMLE_uri);//nlCID.item(0).getNodeValue();
                            oAnnotation.oAnnotated[0].oConcepts[i].sWeight = GetValue(eDescConcept, "s1:" + MetadataConstants.c_XMLE_weight);//nlCCount.item(0).getNodeValue();
                            
                            //if concept is related with code add direct connection
                            if (MetadataGlobal.CheckConcept(oAnnotation.oAnnotated[0].oConcepts[i].sUri))
                                oAnnotation.oReferences.add(oAnnotation.oAnnotated[0].oConcepts[i].sUri);
                        }
                    }
                    
                    //Comment
                    if (iCommentLength > 0 && sCommentUri != null && sCommentUri.length == iCommentLength) //Dejan Milosavljevic 17.04.2012.
                    {
                        for (int i = 0; i < iCommentLength; i++)
                        {
                            Element eCommentText = (Element)nlCommentText.item(i);
                            NodeList nlCommentAnnotated = eCommentText.getElementsByTagName("s1:" + MetadataConstants.c_XMLE_commentTextAnnotated);
                            Element eCommentAnnotated = (Element)nlCommentAnnotated.item(0);
                            oAnnotation.oAnnotated[i + iDescriptionLength] = new MetadataGlobal.AnnotationProp();
                            oAnnotation.oAnnotated[i + iDescriptionLength].sName = MetadataConstants.c_XMLE_commentTextAnnotated;
                            //oAnnotation.oAnnotated[i + iSubjectLength].sValue = eDescAnnotated.getTextContent();
                            oAnnotation.oAnnotated[i + iDescriptionLength].SetAnnotationText(eCommentAnnotated);
                            oAnnotation.oAnnotated[i + iDescriptionLength].sHasObject = sCommentUri[i]; //Dejan Milosavljevic 17.04.2012.
                            String sItemId = GetValue(eCommentText, "s1:" + MetadataConstants.c_XMLE_itemId); //Dejan Milosavljevic 18.04.2012.
                            if (!sItemId.isEmpty()) oAnnotation.oAnnotated[i + iDescriptionLength].iItemId = Integer.parseInt(sItemId); //Dejan Milosavljevic 18.04.2012.
                            
                             //Comment concepts
                            NodeList nlCommentConcepts = eCommentText.getElementsByTagName("s1:" + MetadataConstants.c_XMLE_concept);
                            if (nlCommentConcepts != null && nlCommentConcepts.getLength() > 0)
                            {
                                int iCommentCLength = nlCommentConcepts.getLength();
                                oAnnotation.oAnnotated[i + iDescriptionLength].oConcepts = new MetadataGlobal.ConceptProp[iCommentCLength];
                                for (int j = 0; j < iCommentCLength; j++)
                                {
                                    Element eCommConcept = (Element)nlCommentConcepts.item(j);
                                    oAnnotation.oAnnotated[i + iDescriptionLength].oConcepts[j] = new MetadataGlobal.ConceptProp();
                                    oAnnotation.oAnnotated[i + iDescriptionLength].oConcepts[j].sName = MetadataConstants.c_XMLE_commentTextConcepts;
                                    oAnnotation.oAnnotated[i + iDescriptionLength].oConcepts[j].sUri = GetValue(eCommConcept, "s1:" + MetadataConstants.c_XMLE_uri);//nlCID.item(0).getNodeValue();
                                    oAnnotation.oAnnotated[i + iDescriptionLength].oConcepts[j].sWeight = GetValue(eCommConcept, "s1:" + MetadataConstants.c_XMLE_weight);//nlCCount.item(0).getNodeValue();
                                    
                                    //if concept is related with code add direct connection
                                    if (MetadataGlobal.CheckConcept(oAnnotation.oAnnotated[i + iDescriptionLength].oConcepts[j].sUri))
                                        oAnnotation.oReferences.add(oAnnotation.oAnnotated[i + iDescriptionLength].oConcepts[j].sUri);
                                }
                            }
                        }
                    }
                    oAnnotation.SetKeywords();
                }
                
                NodeList nlReferences = eAnnotation.getElementsByTagName("s1:" + MetadataConstants.c_XMLE_referenceUri);
                if (nlReferences != null)
                {
                    int iLength = nlReferences.getLength();
                    //oAnnotation.oReferences = new String[iLength];
                    for (int i = 0; i < iLength; i++)
                    {
                        Element eReferenceUri = (Element)nlReferences.item(i);
                        oAnnotation.oReferences.add(eReferenceUri.getFirstChild().getNodeValue());
                        //oAnnotation.oReferences[i] = GetValue(eReferenceUri, "s1:" + MetadataConstants.c_XMLE_referenceUri);
                    }
                }
            }

            if (bNew)
                MetadataModel.SaveObjectNewAnnotationData(MetadataConstants.c_ET_ALERT_Metadata_IssueNew_Updated, dDoc, oAnnotation);
            else
                MetadataModel.SaveObjectNewAnnotationData(MetadataConstants.c_ET_ALERT_Metadata_IssueUpdate_Updated, dDoc, oAnnotation);
            
            return oAnnotation;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
//    /**
//     * @summary Method for reading new comment annotation event from XML.
//     * @startRealisation  Dejan Milosavljevic 17.01.2012.
//     * @finalModification Dejan Milosavljevic 18.04.2012.
//     * @param dDoc - input XML document to read
//     * @return - returns AnnotationData object
//     */
//    public static AnnotationData NewCommentAnnotation(Document dDoc)
//    {
//        try
//        {
//            //String sEventId = GetEventId(dDoc);
//
//            AnnotationData oAnnotation = MetadataObjectFactory.CreateNewAnnotation();
//            
//            NodeList nlMdservice = dDoc.getElementsByTagName("o:" + MetadataConstants.c_XMLE_mdservice);
//            if (nlMdservice != null && nlMdservice.getLength() > 0)
//            {
//                Element eMdservice = (Element) nlMdservice.item(0);
//                
//                NodeList nlIssueComment = eMdservice.getElementsByTagName("o:" + MetadataConstants.c_XMLE_issueComment);
//                if (nlIssueComment != null  && nlIssueComment.getLength() > 0)
//                {
//                    Element eIssueComment = (Element) nlIssueComment.item(0);
//                    
//                    String sTag = "o:" + MetadataConstants.c_XMLE_comment + MetadataConstants.c_XMLE_Uri;
//                
//                    //URI
//                    oAnnotation.m_sObjectURI = GetValue(eIssueComment, sTag);
//                }
//            }
//            
//            NodeList nlAnnotation = dDoc.getElementsByTagName("s1:" + MetadataConstants.c_XMLE_keui);   //getting node for tag keui
//
//            if (nlAnnotation != null && nlAnnotation.getLength() > 0)
//            {
//                Element eAnnotation = (Element) nlAnnotation.item(0);
//                
//                String sItemId = GetValue(eAnnotation, "s1:" + MetadataConstants.c_XMLE_itemId);     //Dejan Milosavljevic 18.04.2012.
//                if (!sItemId.isEmpty()) oAnnotation.iItemId = Integer.parseInt(sItemId);             //Dejan Milosavljevic 18.04.2012.
//                String sTtreadID = GetValue(eAnnotation, "s1:" + MetadataConstants.c_XMLE_threadId); //Dejan Milosavljevic 18.04.2012.
//                if (!sTtreadID.isEmpty()) oAnnotation.iThreadId = Integer.parseInt(sTtreadID);       //Dejan Milosavljevic 18.04.2012.
//            
//                //URI
//                oAnnotation.m_sObjectURI = GetValue(eAnnotation, "s1:" + MetadataConstants.c_XMLE_itemUri);
//                
//                //Annotations
//                NodeList nlCommentAnnotated = eAnnotation.getElementsByTagName("s1:" + MetadataConstants.c_XMLE_commentTextAnnotated);
//                if (nlCommentAnnotated != null)
//                {
//                    int iCommentLength = nlCommentAnnotated.getLength();
//                    oAnnotation.oAnnotated = new MetadataGlobal.AnnotationProp[iCommentLength];
//                    if (iCommentLength > 0)
//                    {
//                        for (int i = 0; i < iCommentLength; i++)
//                        {
//                            Element eCommentAnnotated = (Element)nlCommentAnnotated.item(i);
//                            oAnnotation.oAnnotated[i] = new MetadataGlobal.AnnotationProp();
//                            oAnnotation.oAnnotated[i].sName = MetadataConstants.c_XMLE_commentTextAnnotated;
//                            //oAnnotation.oAnnotated[i].sValue = eCommentAnnotated.getTextContent();
//                            oAnnotation.oAnnotated[i].SetAnnotationText(eCommentAnnotated);
//                        }
//                    }
//                    oAnnotation.SetKeywords();
//                }
//                
//                //Concepts
//                NodeList nlCConcepts = eAnnotation.getElementsByTagName("s1:" + MetadataConstants.c_XMLE_commentTextConcepts);
//                if (nlCConcepts != null && nlCConcepts.getLength() > 0)
//                {
//                    Element eCommConcepts = (Element) nlCConcepts.item(0);
//                    NodeList nlCommConcepts = eCommConcepts.getElementsByTagName("s1:" + MetadataConstants.c_XMLE_concept);
//                    if (nlCommConcepts != null)
//                    {
//                        int iCommentLength = nlCommConcepts.getLength();
//                        oAnnotation.oConcepts = new MetadataGlobal.ConceptProp[iCommentLength];
//                        for (int i = 0; i < iCommentLength; i++)
//                        {
//                            Element eCConcept = (Element)nlCommConcepts.item(i);
//                            oAnnotation.oConcepts[i] = new MetadataGlobal.ConceptProp();
//                            oAnnotation.oConcepts[i].sName = MetadataConstants.c_XMLE_commentTextConcepts;
//                            oAnnotation.oConcepts[i].sUri = GetValue(eCConcept, "s1:" + MetadataConstants.c_XMLE_uri);
//                            oAnnotation.oConcepts[i].sWeight = GetValue(eCConcept, "s1:" + MetadataConstants.c_XMLE_weight);
//                        }
//                    }
//                }
//                
//                NodeList nlCommentAnnotated = eAnnotation.getElementsByTagName("s1:" + MetadataConstants.c_XMLE_commentTextAnnotated);
//                if (nlCommentAnnotated != null)
//                {
//                    int iCommentLength = nlCommentAnnotated.getLength();
//                    oAnnotation.oAnnotated = new MetadataGlobal.AnnotationProp[iCommentLength];
//                    if (iCommentLength == 1)
//                    {
//                        Element eCommentAnnotated = (Element)nlCommentAnnotated.item(0);
//                        oAnnotation.oAnnotated[0] = new MetadataGlobal.AnnotationProp();
//                        oAnnotation.oAnnotated[0].sName = MetadataConstants.c_XMLE_commentTextAnnotated;
//                        oAnnotation.oAnnotated[0].SetAnnotationText(eCommentAnnotated);
//                        
//                        NodeList nlCConcepts = eAnnotation.getElementsByTagName("s1:" + MetadataConstants.c_XMLE_commentTextConcepts);
//                        if (nlCConcepts != null && nlCConcepts.getLength() > 0)
//                        {
//                            Element eCommConcepts = (Element) nlCConcepts.item(0);
//                            NodeList nlCommConcepts = eCommConcepts.getElementsByTagName("s1:" + MetadataConstants.c_XMLE_concept);
//                            if (nlCommConcepts != null)
//                            {
//                                int iCommentCLength = nlCommConcepts.getLength();
//                                oAnnotation.oAnnotated[0].oConcepts = new MetadataGlobal.ConceptProp[iCommentCLength];
//                                for (int i = 0; i < iCommentCLength; i++)
//                                {
//                                    Element eCConcept = (Element)nlCommConcepts.item(i);
//                                    oAnnotation.oAnnotated[0].oConcepts[i] = new MetadataGlobal.ConceptProp();
//                                    oAnnotation.oAnnotated[0].oConcepts[i].sName = MetadataConstants.c_XMLE_commentTextConcepts;
//                                    oAnnotation.oAnnotated[0].oConcepts[i].sUri = GetValue(eCConcept, "s1:" + MetadataConstants.c_XMLE_uri);
//                                    oAnnotation.oAnnotated[0].oConcepts[i].sWeight = GetValue(eCConcept, "s1:" + MetadataConstants.c_XMLE_weight);
//                                }
//                            }
//                        }
//                    }
//                    oAnnotation.SetKeywords();
//                }
//                
//                NodeList nlReferences = eAnnotation.getElementsByTagName("s1:" + MetadataConstants.c_XMLE_referenceUri);
//                if (nlReferences != null)
//                {
//                    int iLength = nlReferences.getLength();
//                    oAnnotation.oReferences = new String[iLength];
//                    for (int i = 0; i < iLength; i++)
//                    {
//                        Element eReferenceUri = (Element)nlReferences.item(i);
//                        oAnnotation.oReferences[i] = eReferenceUri.getFirstChild().getNodeValue();
//                    }
//                }
//            }
//
//            MetadataModel.SaveObjectNewAnnotationData(MetadataConstants.c_ET_ALERT_Metadata_IssueUpdate_Updated, dDoc, oAnnotation);
//            
//            return oAnnotation;
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//            return null;
//        }
//    }
    
    /**
     * @summary Method for reading new commit annotation event from XML.
     * @startRealisation  Dejan Milosavljevic 17.01.2012.
     * @finalModification Dejan Milosavljevic 18.04.2012.
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
            
                String sItemId = GetValue(eAnnotation, "s1:" + MetadataConstants.c_XMLE_itemId);     //Dejan Milosavljevic 18.04.2012.
                if (!sItemId.isEmpty()) oAnnotation.iItemId = Integer.parseInt(sItemId);             //Dejan Milosavljevic 18.04.2012.
                String sTtreadID = GetValue(eAnnotation, "s1:" + MetadataConstants.c_XMLE_threadId); //Dejan Milosavljevic 18.04.2012.
                if (!sTtreadID.isEmpty()) oAnnotation.iThreadId = Integer.parseInt(sTtreadID);       //Dejan Milosavljevic 18.04.2012.

//                //URI
//                oAnnotation.m_sObjectURI = GetValue(eAnnotation, "s1:" + MetadataConstants.c_XMLE_itemUri);
                
                //Annotations
//                NodeList nlCommitAnnotated = eAnnotation.getElementsByTagName("s1:" + MetadataConstants.c_XMLE_commitMessageLogAnnotated);
//                if (nlCommitAnnotated != null)
//                {
//                    int iCommitLength = nlCommitAnnotated.getLength();
//                    oAnnotation.oAnnotated = new MetadataGlobal.AnnotationProp[iCommitLength];
//                    if (iCommitLength > 0)
//                    {
//                        for (int i = 0; i < iCommitLength; i++)
//                        {
//                            Element eCommitAnnotated = (Element)nlCommitAnnotated.item(i);
//                            oAnnotation.oAnnotated[i] = new MetadataGlobal.AnnotationProp();
//                            oAnnotation.oAnnotated[i].sName = MetadataConstants.c_XMLE_commitMessageLogAnnotated;
//                            //oAnnotation.oAnnotated[i].sValue = eCommitAnnotated.getTextContent();
//                            oAnnotation.oAnnotated[i].SetAnnotationText(eCommitAnnotated);
//                        }
//                    }
//                    oAnnotation.SetKeywords();
//                }
//                
//                //Concepts
//                NodeList nlCConcepts = eAnnotation.getElementsByTagName("s1:" + MetadataConstants.c_XMLE_commitMessageLogConcepts);
//                if (nlCConcepts != null && nlCConcepts.getLength() > 0)
//                {
//                    Element eCommConcepts = (Element) nlCConcepts.item(0);
//                    NodeList nlCommConcepts = eCommConcepts.getElementsByTagName("s1:" + MetadataConstants.c_XMLE_concept);
//                    if (nlCommConcepts != null)
//                    {
//                        int iCommentLength = nlCommConcepts.getLength();
//                        oAnnotation.oConcepts = new MetadataGlobal.ConceptProp[iCommentLength];
//                        for (int i = 0; i < iCommentLength; i++)
//                        {
//                            Element eCConcept = (Element)nlCommConcepts.item(i);
//                            oAnnotation.oConcepts[i] = new MetadataGlobal.ConceptProp();
//                            oAnnotation.oConcepts[i].sName = MetadataConstants.c_XMLE_commitMessageLogConcepts;
//                            oAnnotation.oConcepts[i].sUri = GetValue(eCConcept, "s1:" + MetadataConstants.c_XMLE_uri);
//                            oAnnotation.oConcepts[i].sWeight = GetValue(eCConcept, "s1:" + MetadataConstants.c_XMLE_weight);
//                        }
//                    }
//                }
                
                NodeList nlCommitAnnotated = eAnnotation.getElementsByTagName("s1:" + MetadataConstants.c_XMLE_commitMessageLogAnnotated);
                if (nlCommitAnnotated != null)
                {
                    int iCommitLength = nlCommitAnnotated.getLength();
                    oAnnotation.oAnnotated = new MetadataGlobal.AnnotationProp[iCommitLength];
                    if (iCommitLength == 1)
                    {
                        Element eCommitAnnotated = (Element)nlCommitAnnotated.item(0);
                        oAnnotation.oAnnotated[0] = new MetadataGlobal.AnnotationProp();
                        oAnnotation.oAnnotated[0].sName = MetadataConstants.c_XMLE_commitMessageLogAnnotated;
                        oAnnotation.oAnnotated[0].SetAnnotationText(eCommitAnnotated);
                        
                        //Concepts
                        NodeList nlCConcepts = eAnnotation.getElementsByTagName("s1:" + MetadataConstants.c_XMLE_commitMessageLogConcepts);
                        if (nlCConcepts != null && nlCConcepts.getLength() > 0)
                        {
                            Element eCommConcepts = (Element) nlCConcepts.item(0);
                            NodeList nlCommConcepts = eCommConcepts.getElementsByTagName("s1:" + MetadataConstants.c_XMLE_concept);
                            if (nlCommConcepts != null)
                            {
                                int iCommentLength = nlCommConcepts.getLength();
                                oAnnotation.oAnnotated[0].oConcepts = new MetadataGlobal.ConceptProp[iCommentLength];
                                for (int i = 0; i < iCommentLength; i++)
                                {
                                    Element eCConcept = (Element)nlCommConcepts.item(i);
                                    oAnnotation.oAnnotated[0].oConcepts[i] = new MetadataGlobal.ConceptProp();
                                    oAnnotation.oAnnotated[0].oConcepts[i].sName = MetadataConstants.c_XMLE_commitMessageLogConcepts;
                                    oAnnotation.oAnnotated[0].oConcepts[i].sUri = GetValue(eCConcept, "s1:" + MetadataConstants.c_XMLE_uri);
                                    oAnnotation.oAnnotated[0].oConcepts[i].sWeight = GetValue(eCConcept, "s1:" + MetadataConstants.c_XMLE_weight);
                                }
                            }
                        }
                    }
                    oAnnotation.SetKeywords();
                }
                
                NodeList nlReferences = eAnnotation.getElementsByTagName("s1:" + MetadataConstants.c_XMLE_referenceUri);
                if (nlReferences != null)
                {
                    int iLength = nlReferences.getLength();
                    for (int i = 0; i < iLength; i++)
                    {
                        Element eReferenceUri = (Element)nlReferences.item(i);
                        oAnnotation.oReferences.add(eReferenceUri.getFirstChild().getNodeValue());
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
     * @finalModification Dejan Milosavljevic 18.04.2012.
     * @param dDoc - input XML document to read
     * @return - returns AnnotationData object
     */
    public static AnnotationData NewForumPostAnnotation(Document dDoc)
    {
        try
        {
            //String sEventId = GetEventId(dDoc);
            AnnotationData oAnnotation = MetadataObjectFactory.CreateNewAnnotation();
            
            NodeList nlMdservice = dDoc.getElementsByTagName("o:" + MetadataConstants.c_XMLE_mdservice);
            if (nlMdservice != null && nlMdservice.getLength() > 0)
            {
                Element eMdservice = (Element) nlMdservice.item(0);
                String sTag = "o:" + MetadataConstants.c_XMLE_post + MetadataConstants.c_XMLE_Uri;
                
                //URI
                oAnnotation.m_sObjectURI = GetValue(eMdservice, sTag);
            }
            
            NodeList nlAnnotation = dDoc.getElementsByTagName("s1:" + MetadataConstants.c_XMLE_keui);   //getting node for tag annotation

            if (nlAnnotation != null && nlAnnotation.getLength() > 0)
            {
                Element eAnnotation = (Element) nlAnnotation.item(0);
            
                String sItemId = GetValue(eAnnotation, "s1:" + MetadataConstants.c_XMLE_itemId);     //Dejan Milosavljevic 18.04.2012.
                if (!sItemId.isEmpty()) oAnnotation.iItemId = Integer.parseInt(sItemId);             //Dejan Milosavljevic 18.04.2012.
                String sTtreadID = GetValue(eAnnotation, "s1:" + MetadataConstants.c_XMLE_threadId); //Dejan Milosavljevic 18.04.2012.
                if (!sTtreadID.isEmpty()) oAnnotation.iThreadId = Integer.parseInt(sTtreadID);       //Dejan Milosavljevic 18.04.2012.
                
//                //URI
//                oAnnotation.m_sObjectURI = GetValue(eAnnotation, "s1:" + MetadataConstants.c_XMLE_itemUri);
                
                //Annotations
//                NodeList nlTitleAnnotated = eAnnotation.getElementsByTagName("s1:" + MetadataConstants.c_XMLE_subjectAnnotated);
//                NodeList nlBodyAnnotated = eAnnotation.getElementsByTagName("s1:" + MetadataConstants.c_XMLE_bodyAnnotated);
//                if (nlTitleAnnotated != null && nlBodyAnnotated != null)
//                {
//                    int iTitleLength = nlTitleAnnotated.getLength();
//                    int iBodyLength = nlBodyAnnotated.getLength();
//                    oAnnotation.oAnnotated = new MetadataGlobal.AnnotationProp[iTitleLength + iBodyLength];
//                    if (iTitleLength > 0)
//                    {
//                        for (int i = 0; i < iTitleLength; i++)
//                        {
//                            Element eTitleAnnotated = (Element)nlTitleAnnotated.item(i);
//                            oAnnotation.oAnnotated[i] = new MetadataGlobal.AnnotationProp();
//                            oAnnotation.oAnnotated[i].sName = MetadataConstants.c_XMLE_subjectAnnotated;
//                            //oAnnotation.oAnnotated[i].sValue = eTitleAnnotated.getTextContent();
//                            oAnnotation.oAnnotated[i].SetAnnotationText(eTitleAnnotated);
//                        }
//                    }
//                    if (iBodyLength > 0)
//                    {
//                        for (int i = 0; i < iBodyLength; i++)
//                        {
//                            Element eBodyAnnotated = (Element)nlBodyAnnotated.item(i);
//                            oAnnotation.oAnnotated[i + iTitleLength] = new MetadataGlobal.AnnotationProp();
//                            oAnnotation.oAnnotated[i + iTitleLength].sName = MetadataConstants.c_XMLE_bodyAnnotated;
//                            //oAnnotation.oAnnotated[i + iTitleLength].sValue = eBodyAnnotated.getTextContent();
//                            oAnnotation.oAnnotated[i + iTitleLength].SetAnnotationText(eBodyAnnotated);
//                        }
//                    }
//                    oAnnotation.SetKeywords();
//                }
//                
//                //Concepts
//                NodeList nlTConcepts = eAnnotation.getElementsByTagName("s1:" + MetadataConstants.c_XMLE_subjectConcepts);
//                NodeList nlBConcepts = eAnnotation.getElementsByTagName("s1:" + MetadataConstants.c_XMLE_bodyConcepts);
//                if (nlTConcepts != null && nlBConcepts != null &&
//                    nlTConcepts.getLength() > 0 && nlBConcepts.getLength() > 0)
//                {
//                    Element eTitleConcepts = (Element) nlTConcepts.item(0);
//                    Element eBodyConcepts = (Element) nlBConcepts.item(0);
//                    NodeList nlTitleConcepts = eTitleConcepts.getElementsByTagName("s1:" + MetadataConstants.c_XMLE_concept);
//                    NodeList nlBodyConcepts = eBodyConcepts.getElementsByTagName("s1:" + MetadataConstants.c_XMLE_concept);
//                    if (nlTitleConcepts != null && nlBodyConcepts != null)
//                    {
//                        int iTitleLength = nlTitleConcepts.getLength();
//                        int iBodyLength = nlBodyConcepts.getLength();
//                        oAnnotation.oConcepts = new MetadataGlobal.ConceptProp[iTitleLength + iBodyLength];
//                        for (int i = 0; i < iTitleLength; i++)
//                        {
//                            Element eTConcept = (Element)nlTitleConcepts.item(i);
//                            oAnnotation.oConcepts[i] = new MetadataGlobal.ConceptProp();
//                            oAnnotation.oConcepts[i].sName = MetadataConstants.c_XMLE_subjectConcepts;
//                            oAnnotation.oConcepts[i].sUri = GetValue(eTConcept, "s1:" + MetadataConstants.c_XMLE_uri);
//                            oAnnotation.oConcepts[i].sWeight = GetValue(eTConcept, "s1:" + MetadataConstants.c_XMLE_weight);
//                        }
//                        for (int i = 0; i < iBodyLength; i++)
//                        {
//                            Element eBConcept = (Element)nlBodyConcepts.item(i);
//                            oAnnotation.oConcepts[i + iTitleLength] = new MetadataGlobal.ConceptProp();
//                            oAnnotation.oConcepts[i + iTitleLength].sName = MetadataConstants.c_XMLE_bodyConcepts;
//                            oAnnotation.oConcepts[i + iTitleLength].sUri = GetValue(eBConcept, "s1:" + MetadataConstants.c_XMLE_uri);
//                            oAnnotation.oConcepts[i + iTitleLength].sWeight = GetValue(eBConcept, "s1:" + MetadataConstants.c_XMLE_weight);
//                        }
//                    }
//                }
                
                NodeList nlTitleAnnotated = eAnnotation.getElementsByTagName("s1:" + MetadataConstants.c_XMLE_subjectAnnotated);
                NodeList nlBodyAnnotated = eAnnotation.getElementsByTagName("s1:" + MetadataConstants.c_XMLE_bodyAnnotated);
                if (nlTitleAnnotated != null && nlBodyAnnotated != null)
                {
                    int iTitleLength = nlTitleAnnotated.getLength();
                    int iBodyLength = nlBodyAnnotated.getLength();
                    oAnnotation.oAnnotated = new MetadataGlobal.AnnotationProp[iTitleLength + iBodyLength];
                    if (iTitleLength == 1)
                    {
                        Element eTitleAnnotated = (Element)nlTitleAnnotated.item(0);
                        oAnnotation.oAnnotated[0] = new MetadataGlobal.AnnotationProp();
                        oAnnotation.oAnnotated[0].sName = MetadataConstants.c_XMLE_subjectAnnotated;
                        oAnnotation.oAnnotated[0].SetAnnotationText(eTitleAnnotated);
                        
                        NodeList nlTConcepts = eAnnotation.getElementsByTagName("s1:" + MetadataConstants.c_XMLE_subjectConcepts);
                        if (nlTConcepts != null && nlTConcepts.getLength() > 0)
                        {
                            Element eTitleConcepts = (Element) nlTConcepts.item(0);
                            NodeList nlTitleConcepts = eTitleConcepts.getElementsByTagName("s1:" + MetadataConstants.c_XMLE_concept);
                            if (nlTitleConcepts != null)
                            {
                                int iTitleCLength = nlTitleConcepts.getLength();
                                oAnnotation.oAnnotated[0].oConcepts = new MetadataGlobal.ConceptProp[iTitleCLength];
                                for (int i = 0; i < iTitleCLength; i++)
                                {
                                    Element eTConcept = (Element)nlTitleConcepts.item(i);
                                    oAnnotation.oAnnotated[0].oConcepts[i] = new MetadataGlobal.ConceptProp();
                                    oAnnotation.oAnnotated[0].oConcepts[i].sName = MetadataConstants.c_XMLE_subjectConcepts;
                                    oAnnotation.oAnnotated[0].oConcepts[i].sUri = GetValue(eTConcept, "s1:" + MetadataConstants.c_XMLE_uri);
                                    oAnnotation.oAnnotated[0].oConcepts[i].sWeight = GetValue(eTConcept, "s1:" + MetadataConstants.c_XMLE_weight);
                                }
                            }
                        }
                    }
                    if (iBodyLength == 1)
                    {
                        Element eBodyAnnotated = (Element)nlBodyAnnotated.item(0);
                        oAnnotation.oAnnotated[iTitleLength] = new MetadataGlobal.AnnotationProp();
                        oAnnotation.oAnnotated[iTitleLength].sName = MetadataConstants.c_XMLE_bodyAnnotated;
                        oAnnotation.oAnnotated[iTitleLength].SetAnnotationText(eBodyAnnotated);
                        
                        NodeList nlBConcepts = eAnnotation.getElementsByTagName("s1:" + MetadataConstants.c_XMLE_bodyConcepts);
                        if (nlBConcepts != null && nlBConcepts.getLength() > 0)
                        {
                            Element eBodyConcepts = (Element) nlBConcepts.item(0);
                            NodeList nlBodyConcepts = eBodyConcepts.getElementsByTagName("s1:" + MetadataConstants.c_XMLE_concept);
                            if (nlBodyConcepts != null)
                            {
                                int iBodyCLength = nlBodyConcepts.getLength();
                                oAnnotation.oAnnotated[iTitleLength].oConcepts = new MetadataGlobal.ConceptProp[iBodyCLength];
                                for (int i = 0; i < iBodyCLength; i++)
                                {
                                    Element eBConcept = (Element)nlBodyConcepts.item(i);
                                    oAnnotation.oAnnotated[iTitleLength].oConcepts[i] = new MetadataGlobal.ConceptProp();
                                    oAnnotation.oAnnotated[iTitleLength].oConcepts[i].sName = MetadataConstants.c_XMLE_bodyConcepts;
                                    oAnnotation.oAnnotated[iTitleLength].oConcepts[i].sUri = GetValue(eBConcept, "s1:" + MetadataConstants.c_XMLE_uri);
                                    oAnnotation.oAnnotated[iTitleLength].oConcepts[i].sWeight = GetValue(eBConcept, "s1:" + MetadataConstants.c_XMLE_weight);
                                }
                            }
                        }
                    }
                    oAnnotation.SetKeywords();
                }
                
                NodeList nlReferences = eAnnotation.getElementsByTagName("s1:" + MetadataConstants.c_XMLE_referenceUri);
                if (nlReferences != null)
                {
                    int iLength = nlReferences.getLength();
                    for (int i = 0; i < iLength; i++)
                    {
                        Element eReferenceUri = (Element)nlReferences.item(i);
                        oAnnotation.oReferences.add(eReferenceUri.getFirstChild().getNodeValue());
                    }
                }
            }

            MetadataModel.SaveObjectNewAnnotationData(MetadataConstants.c_ET_ALERT_Metadata_ForumPostNew_Updated, dDoc, oAnnotation);
            
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
     * @finalModification Dejan Milosavljevic 18.04.2012.
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
            
                String sItemId = GetValue(eAnnotation, "s1:" + MetadataConstants.c_XMLE_itemId);     //Dejan Milosavljevic 18.04.2012.
                if (!sItemId.isEmpty()) oAnnotation.iItemId = Integer.parseInt(sItemId);             //Dejan Milosavljevic 18.04.2012.
                String sTtreadID = GetValue(eAnnotation, "s1:" + MetadataConstants.c_XMLE_threadId); //Dejan Milosavljevic 18.04.2012.
                if (!sTtreadID.isEmpty()) oAnnotation.iThreadId = Integer.parseInt(sTtreadID);       //Dejan Milosavljevic 18.04.2012.

//                //URI
//                oAnnotation.m_sObjectURI = GetValue(eAnnotation, "s1:" + MetadataConstants.c_XMLE_itemUri);
                
                //Annotations
//                NodeList nlSubjectAnnotated = eAnnotation.getElementsByTagName("s1:" + MetadataConstants.c_XMLE_subjectAnnotated);
//                NodeList nlContentAnnotated = eAnnotation.getElementsByTagName("s1:" + MetadataConstants.c_XMLE_contentAnnotated);
//                if (nlSubjectAnnotated != null && nlContentAnnotated != null)
//                {
//                    int iSubjectLength = nlSubjectAnnotated.getLength();
//                    int iContentLength = nlContentAnnotated.getLength();
//                    oAnnotation.oAnnotated = new MetadataGlobal.AnnotationProp[iSubjectLength + iContentLength];
//                    if (iSubjectLength > 0)
//                    {
//                        for (int i = 0; i < iSubjectLength; i++)
//                        {
//                            Element eSubjectAnnotated = (Element)nlSubjectAnnotated.item(i);
//                            oAnnotation.oAnnotated[i] = new MetadataGlobal.AnnotationProp();
//                            oAnnotation.oAnnotated[i].sName = MetadataConstants.c_XMLE_subjectAnnotated;
//                            //oAnnotation.oAnnotated[i].sValue = eSubjectAnnotated.getTextContent();
//                            oAnnotation.oAnnotated[i].SetAnnotationText(eSubjectAnnotated);
//                        }
//                    }
//                    if (iContentLength > 0)
//                    {
//                        for (int i = 0; i < iContentLength; i++)
//                        {
//                            Element eContentAnnotated = (Element)nlContentAnnotated.item(i);
//                            oAnnotation.oAnnotated[i + iSubjectLength] = new MetadataGlobal.AnnotationProp();
//                            oAnnotation.oAnnotated[i + iSubjectLength].sName = MetadataConstants.c_XMLE_contentAnnotated;
//                            //oAnnotation.oAnnotated[i + iSubjectLength].sValue = eBodyAnnotated.getTextContent();
//                            oAnnotation.oAnnotated[i + iSubjectLength].SetAnnotationText(eContentAnnotated);
//                        }
//                    }
//                    oAnnotation.SetKeywords();
//                }
//                
//                //Concepts
//                NodeList nlSConcepts = eAnnotation.getElementsByTagName("s1:" + MetadataConstants.c_XMLE_subjectConcepts);
//                NodeList nlCConcepts = eAnnotation.getElementsByTagName("s1:" + MetadataConstants.c_XMLE_contentConcepts);
//                if (nlSConcepts != null && nlCConcepts != null &&
//                    nlSConcepts.getLength() > 0 && nlCConcepts.getLength() > 0)
//                {
//                    Element eSubjectConcepts = (Element) nlSConcepts.item(0);
//                    Element eContentConcepts = (Element) nlCConcepts.item(0);
//                    NodeList nlSubjectConcepts = eSubjectConcepts.getElementsByTagName("s1:" + MetadataConstants.c_XMLE_concept);
//                    NodeList nlContentConcepts = eContentConcepts.getElementsByTagName("s1:" + MetadataConstants.c_XMLE_concept);
//                    if (nlSubjectConcepts != null && nlContentConcepts != null)
//                    {
//                        int iSubjectLength = nlSubjectConcepts.getLength();
//                        int iContentLength = nlContentConcepts.getLength();
//                        oAnnotation.oConcepts = new MetadataGlobal.ConceptProp[iSubjectLength + iContentLength];
//                        for (int i = 0; i < iSubjectLength; i++)
//                        {
//                            Element eSConcept = (Element)nlSubjectConcepts.item(i);
//                            oAnnotation.oConcepts[i] = new MetadataGlobal.ConceptProp();
//                            oAnnotation.oConcepts[i].sName = MetadataConstants.c_XMLE_subjectConcepts;
//                            oAnnotation.oConcepts[i].sUri = GetValue(eSConcept, "s1:" + MetadataConstants.c_XMLE_uri);
//                            oAnnotation.oConcepts[i].sWeight = GetValue(eSConcept, "s1:" + MetadataConstants.c_XMLE_weight);
//                        }
//                        for (int i = 0; i < iContentLength; i++)
//                        {
//                            Element eCConcept = (Element)nlContentConcepts.item(i);
//                            oAnnotation.oConcepts[i + iSubjectLength] = new MetadataGlobal.ConceptProp();
//                            oAnnotation.oConcepts[i + iSubjectLength].sName = MetadataConstants.c_XMLE_contentConcepts;
//                            oAnnotation.oConcepts[i + iSubjectLength].sUri = GetValue(eCConcept, "s1:" + MetadataConstants.c_XMLE_uri);
//                            oAnnotation.oConcepts[i + iSubjectLength].sWeight = GetValue(eCConcept, "s1:" + MetadataConstants.c_XMLE_weight);
//                        }
//                    }
//                }
                
                NodeList nlSubjectAnnotated = eAnnotation.getElementsByTagName("s1:" + MetadataConstants.c_XMLE_subjectAnnotated);
                NodeList nlContentAnnotated = eAnnotation.getElementsByTagName("s1:" + MetadataConstants.c_XMLE_contentAnnotated);
                if (nlSubjectAnnotated != null && nlContentAnnotated != null)
                {
                    int iSubjectLength = nlSubjectAnnotated.getLength();
                    int iContentLength = nlContentAnnotated.getLength();
                    oAnnotation.oAnnotated = new MetadataGlobal.AnnotationProp[iSubjectLength + iContentLength];
                    if (iSubjectLength == 1)
                    {
                        Element eSubjectAnnotated = (Element)nlSubjectAnnotated.item(0);
                        oAnnotation.oAnnotated[0] = new MetadataGlobal.AnnotationProp();
                        oAnnotation.oAnnotated[0].sName = MetadataConstants.c_XMLE_subjectAnnotated;
                        oAnnotation.oAnnotated[0].SetAnnotationText(eSubjectAnnotated);
                        
                        NodeList nlSConcepts = eAnnotation.getElementsByTagName("s1:" + MetadataConstants.c_XMLE_subjectConcepts);
                        if (nlSConcepts != null && nlSConcepts.getLength() > 0)
                        {
                            Element eSubjectConcepts = (Element) nlSConcepts.item(0);
                            NodeList nlSubjectConcepts = eSubjectConcepts.getElementsByTagName("s1:" + MetadataConstants.c_XMLE_concept);
                            if (nlSubjectConcepts != null)
                            {
                                int iSubjectCLength = nlSubjectConcepts.getLength();
                                oAnnotation.oAnnotated[0].oConcepts = new MetadataGlobal.ConceptProp[iSubjectCLength];
                                for (int i = 0; i < iSubjectCLength; i++)
                                {
                                    Element eSConcept = (Element)nlSubjectConcepts.item(i);
                                    oAnnotation.oAnnotated[0].oConcepts[i] = new MetadataGlobal.ConceptProp();
                                    oAnnotation.oAnnotated[0].oConcepts[i].sName = MetadataConstants.c_XMLE_subjectConcepts;
                                    oAnnotation.oAnnotated[0].oConcepts[i].sUri = GetValue(eSConcept, "s1:" + MetadataConstants.c_XMLE_uri);
                                    oAnnotation.oAnnotated[0].oConcepts[i].sWeight = GetValue(eSConcept, "s1:" + MetadataConstants.c_XMLE_weight);
                                }
                            }
                        }
                    }
                    if (iContentLength == 1)
                    {
                        Element eContentAnnotated = (Element)nlContentAnnotated.item(0);
                        oAnnotation.oAnnotated[iSubjectLength] = new MetadataGlobal.AnnotationProp();
                        oAnnotation.oAnnotated[iSubjectLength].sName = MetadataConstants.c_XMLE_contentAnnotated;
                        oAnnotation.oAnnotated[iSubjectLength].SetAnnotationText(eContentAnnotated);
                        
                        NodeList nlCConcepts = eAnnotation.getElementsByTagName("s1:" + MetadataConstants.c_XMLE_contentConcepts);
                        if (nlCConcepts != null && nlCConcepts.getLength() > 0)
                        {
                            Element eContentConcepts = (Element) nlCConcepts.item(0);
                            NodeList nlContentConcepts = eContentConcepts.getElementsByTagName("s1:" + MetadataConstants.c_XMLE_concept);
                            if (nlContentConcepts != null)
                            {
                                int iContentCLength = nlContentConcepts.getLength();
                                oAnnotation.oAnnotated[iSubjectLength].oConcepts = new MetadataGlobal.ConceptProp[iContentCLength];
                                for (int i = 0; i < iContentCLength; i++)
                                {
                                    Element eCConcept = (Element)nlContentConcepts.item(i);
                                    oAnnotation.oAnnotated[iSubjectLength].oConcepts[i] = new MetadataGlobal.ConceptProp();
                                    oAnnotation.oAnnotated[iSubjectLength].oConcepts[i].sName = MetadataConstants.c_XMLE_contentConcepts;
                                    oAnnotation.oAnnotated[iSubjectLength].oConcepts[i].sUri = GetValue(eCConcept, "s1:" + MetadataConstants.c_XMLE_uri);
                                    oAnnotation.oAnnotated[iSubjectLength].oConcepts[i].sWeight = GetValue(eCConcept, "s1:" + MetadataConstants.c_XMLE_weight);
                                }
                            }
                        }
                    }
                    oAnnotation.SetKeywords();
                }
                
                NodeList nlReferences = eAnnotation.getElementsByTagName("s1:" + MetadataConstants.c_XMLE_referenceUri);
                if (nlReferences != null)
                {
                    int iLength = nlReferences.getLength();
                    for (int i = 0; i < iLength; i++)
                    {
                        Element eReferenceUri = (Element)nlReferences.item(i);
                        oAnnotation.oReferences.add(eReferenceUri.getFirstChild().getNodeValue());
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
                oForumPost.m_oInForumThread.m_sID = GetValue(eForum, "r:" + MetadataConstants.c_XMLE_forumThreadId);

                //postID
                oForumPost.m_sID = GetValue(eForum, "r:" + MetadataConstants.c_XMLE_postId);

                //time
                oForumPost.m_dtmTime = MetadataGlobal.GetDateTime(GetValue(eForum, "r:" + MetadataConstants.c_XMLE_time));

                //subject
                oForumPost.m_sSubject = GetValue(eForum, "r:" + MetadataConstants.c_XMLE_subject);

                //body
                oForumPost.m_sBody = GetValue(eForum, "r:" + MetadataConstants.c_XMLE_body);

                //author
                //NodeList nlAuthor = eForum.getElementsByTagName("r:" + MetadataConstants.c_XMLE_author);
                //if (nlAuthor != null && nlAuthor.getLength() > 0)
                //{
                //    Element eAuthor = (Element) nlAuthor.item(0);
                //    oForumPost.m_oAuthor = GetPersonObject("r:", eAuthor);
                //}
                oForumPost.m_oAuthor = new foaf_Person();
                oForumPost.m_oAuthor.m_sUsername = GetValue(eForum, "r:" + MetadataConstants.c_XMLE_author);
                
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
     * @summary Method for reading new forum post event from XML (only data that is used by other components)
     * @startRealisation  Dejan Milosavljevic 19.10.2012.
     * @finalModification Sasa Stojanovic 19.10.2012.
     * @param dDoc - input XML document to read
     * @return - returns ForumPost object
     */
    public static ForumPost NewForumPostDataLight(Document dDoc)
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

                //threadID
                oForumPost.m_oInForumThread.m_sID = GetValue(eForum, "r:" + MetadataConstants.c_XMLE_forumThreadId);

                //postID
                oForumPost.m_sID = GetValue(eForum, "r:" + MetadataConstants.c_XMLE_postId);

                //subject
                oForumPost.m_sSubject = GetValue(eForum, "r:" + MetadataConstants.c_XMLE_subject);

                //author
                oForumPost.m_oAuthor = new foaf_Person();
                oForumPost.m_oAuthor.m_sUsername = GetValue(eForum, "r:" + MetadataConstants.c_XMLE_author);
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
                                NodeList nlPerson = eIs.getElementsByTagName("sm:" + MetadataConstants.c_XMLE_person);
                                oIdentity.m_oIs = new foaf_Person[nlPerson.getLength()];
                                for (int j = 0; j < nlPerson.getLength(); j++)
                                {
                                    Element ePerson = (Element)nlPerson.item(j);
                                    //oIdentity.m_oIs[j] = new foaf_Person();
                                    //oIdentity.m_oIs[j].m_sObjectURI = ePerson.getFirstChild().getNodeValue();
                                    oIdentity.m_oIs[j] = GetPersonObject("sm:", ePerson);
                                }
                            }

                            NodeList nlIsnt = ePersons.getElementsByTagName("sm:" + MetadataConstants.c_XMLE_isnt);
                            if (nlIsnt != null && nlIsnt.getLength() > 0)
                            {
                                Element eIsnt = (Element)nlIsnt.item(0);
                                NodeList nlPerson = eIsnt.getElementsByTagName("sm:" + MetadataConstants.c_XMLE_person);
                                oIdentity.m_oIsnt = new foaf_Person[nlPerson.getLength()];
                                for (int j = 0; j < nlPerson.getLength(); j++)
                                {
                                    Element ePerson = (Element)nlPerson.item(j);
                                    //oIdentity.m_oIsnt[j] = new foaf_Person();
                                    //oIdentity.m_oIsnt[j].m_sObjectURI = ePerson.getFirstChild().getNodeValue();
                                    oIdentity.m_oIsnt[j] = GetPersonObject("sm:", ePerson);
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
                                if (eAdd != null)
                                {
                                    NodeList nlAddIs = eAdd.getElementsByTagName("sm:" + MetadataConstants.c_XMLE_is);
                                    if (nlAddIs != null)
                                    {
                                        Element eAddIs = (Element)nlAddIs.item(0);
                                        if (eAddIs != null)
                                        {
                                            NodeList nlPerson = eAddIs.getElementsByTagName("sm:" + MetadataConstants.c_XMLE_person);
                                            if (nlPerson != null)
                                            {
                                                iIsCount += nlPerson.getLength();
                                            }
                                        }
                                    }
                                    NodeList nlAddIsnt = eAdd.getElementsByTagName("sm:" + MetadataConstants.c_XMLE_isnt);
                                    if (nlAddIsnt != null)
                                    {
                                        Element eAddIsnt = (Element)nlAddIsnt.item(0);
                                        if (eAddIsnt != null)
                                        {
                                            NodeList nlPerson = eAddIsnt.getElementsByTagName("sm:" + MetadataConstants.c_XMLE_person);
                                            if (nlPerson != null)
                                            {
                                                iIsntCount += nlPerson.getLength();
                                            }
                                        }
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
                                    if (eRemoveIs != null)
                                    {
                                        NodeList nlAllPerson = eRemoveIs.getElementsByTagName("sm:" + MetadataConstants.c_XMLE_allPerson);
                                        if (nlAllPerson != null && nlAllPerson.getLength() > 0)
                                        {
                                            oIdentity.m_bIsRemoveAll = true;
                                        }
                                        else
                                        {
                                            NodeList nlPerson = eRemoveIs.getElementsByTagName("sm:" + MetadataConstants.c_XMLE_person);
                                            if (nlPerson != null)
                                            {
                                                iIsCount += nlPerson.getLength();
                                            }
                                        }
                                    }
                                }
                                NodeList nlRemoveIsnt = eRemove.getElementsByTagName("sm:" + MetadataConstants.c_XMLE_isnt);
                                if (nlRemoveIsnt != null)
                                {
                                    Element eRemoveIsnt = (Element)nlRemoveIsnt.item(0);
                                    if (eRemoveIsnt != null)
                                    {
                                        NodeList nlAllPerson = eRemoveIsnt.getElementsByTagName("sm:" + MetadataConstants.c_XMLE_allPerson);
                                        if (nlAllPerson != null && nlAllPerson.getLength() > 0)
                                        {
                                            oIdentity.m_bIsntRemoveAll = true;
                                        }
                                        else
                                        {
                                            NodeList nlPerson = eRemoveIsnt.getElementsByTagName("sm:" + MetadataConstants.c_XMLE_person);
                                            if (nlPerson != null)
                                            {
                                                iIsntCount += nlPerson.getLength();
                                            }
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
                                    NodeList nlPerson = eAddIs.getElementsByTagName("sm:" + MetadataConstants.c_XMLE_person);
                                    iAddIsCount = nlPerson.getLength();
                                    for (int j = 0; j < nlPerson.getLength(); j++)
                                    {
                                        Element ePerson = (Element)nlPerson.item(j);
                                        //oIdentity.m_oIs[j] = new foaf_Person();
                                        //oIdentity.m_oIs[j].m_sObjectURI = ePerson.getFirstChild().getNodeValue();
                                        oIdentity.m_oIs[j] = GetPersonObject("sm:", ePerson);
                                    }
                                }

                                NodeList nlAddIsnt = eAdd.getElementsByTagName("sm:" + MetadataConstants.c_XMLE_isnt);
                                if (nlAddIsnt != null && nlAddIsnt.getLength() > 0)
                                {
                                    Element eAddIsnt = (Element)nlAddIsnt.item(0);
                                    NodeList nlPerson = eAddIsnt.getElementsByTagName("sm:" + MetadataConstants.c_XMLE_person);
                                    iAddIsntCount = nlPerson.getLength();
                                    for (int j = 0; j < nlPerson.getLength(); j++)
                                    {
                                        Element ePerson = (Element)nlPerson.item(j);
                                        //oIdentity.m_oIsnt[j] = new foaf_Person();
                                        //oIdentity.m_oIsnt[j].m_sObjectURI = ePerson.getFirstChild().getNodeValue();
                                        oIdentity.m_oIsnt[j] = GetPersonObject("sm:", ePerson);
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
                                        NodeList nlPerson = eRemoveIs.getElementsByTagName("sm:" + MetadataConstants.c_XMLE_person);
                                        for (int j = 0; j < nlPerson.getLength(); j++)
                                        {
                                            Element ePerson = (Element)nlPerson.item(j);
                                            //oIdentity.m_oIs[j + iAddIsCount] = new foaf_Person();
                                            //oIdentity.m_oIs[j + iAddIsCount].m_sObjectURI = ePerson.getFirstChild().getNodeValue();
                                            oIdentity.m_oIs[j + iAddIsCount] = GetPersonObject("sm:", ePerson);
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
                                        NodeList nlPerson = eRemoveIsnt.getElementsByTagName("sm:" + MetadataConstants.c_XMLE_person);
                                        for (int j = 0; j < nlPerson.getLength(); j++)
                                        {
                                            Element ePerson = (Element)nlPerson.item(j);
                                            //oIdentity.m_oIsnt[j + iAddIsntCount] = new foaf_Person();
                                            //oIdentity.m_oIsnt[j + iAddIsntCount].m_sObjectURI = ePerson.getFirstChild().getNodeValue();
                                            oIdentity.m_oIsnt[j + iAddIsntCount] = GetPersonObject("sm:", ePerson);
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
    
    /**
     * @summary Method for reading new concept event from XML
     * @startRealisation Sasa Stojanovic 17.05.2012.
     * @finalModification Sasa Stojanovic 17.05.2012.
     * @param dDoc - input XML document to read
     * @return - returns Concept objects
     */
    public static Concept[] NewConcept(Document dDoc)
    {
        try
        {
            String sEventId = GetEventId(dDoc);
            Element eOriginalData = null;  //element for original data
            
            NodeList nlNewRDFData = dDoc.getElementsByTagName("ns1:" + MetadataConstants.c_XMLE_newRDFData);
            if (nlNewRDFData != null && nlNewRDFData.getLength() > 0)
            {
                eOriginalData = (Element) nlNewRDFData.item(0);
            }

            NodeList nlRDF = dDoc.getElementsByTagName("rdf:" + MetadataConstants.c_XMLE_RDF);

            if (nlRDF != null && nlRDF.getLength() > 0)
            {
                Element eRDF = (Element) nlRDF.item(0);

                NodeList nlConcept = eRDF.getElementsByTagName("rdf:" + MetadataConstants.c_XMLE_Description);
                if (nlConcept != null && nlConcept.getLength() > 0)
                {
                    Concept[] oConcepts = new Concept[nlConcept.getLength()];
                    for (int i = 0; i < nlConcept.getLength(); i++)
                    {
                        Element eConcept = (Element)nlConcept.item(i);
                        
                        Concept oConcept = MetadataObjectFactory.CreateNewConcept();
                        oConcept.m_sObjectURI = eConcept.getAttribute("rdf:" + MetadataConstants.c_XMLEA_about);
                        oConcept.m_sLabel = GetValue(eConcept, "rdfs:" + MetadataConstants.c_XMLE_label);
                        oConcept.m_sComment = GetValue(eConcept, "rdfs:" + MetadataConstants.c_XMLE_comment);
                        
                        NodeList nlSameAs = eConcept.getElementsByTagName("owl:" + MetadataConstants.c_XMLE_sameAs);
                        if (nlSameAs != null && nlSameAs.getLength() > 0)
                        {
                            oConcept.m_sSameAs = new String[nlSameAs.getLength()];
                            for (int j = 0; j < nlSameAs.getLength(); j++)
                            {
                                Element eSameAs = (Element)nlSameAs.item(j);
                                oConcept.m_sSameAs[j] = eSameAs.getAttribute("rdf:" + MetadataConstants.c_XMLEA_resource);
                            }
                        }
                        
                        NodeList nlLinksTo = eConcept.getElementsByTagName("predicate:" + MetadataConstants.c_XMLE_linksTo);
                        if (nlLinksTo != null && nlLinksTo.getLength() > 0)
                        {
                            oConcept.m_sLinksTo = new String[nlLinksTo.getLength()];
                            for (int j = 0; j < nlLinksTo.getLength(); j++)
                            {
                                Element eLinksTo = (Element)nlLinksTo.item(j);
                                oConcept.m_sLinksTo[j] = eLinksTo.getAttribute("rdf:" + MetadataConstants.c_XMLEA_resource);
                            }
                        }
                        
                        NodeList nlSubClassOf = eConcept.getElementsByTagName("rdfs:" + MetadataConstants.c_XMLE_subClassOf);
                        if (nlSubClassOf != null && nlSubClassOf.getLength() > 0)
                        {
                            oConcept.m_sSubClassOf = new String[nlSubClassOf.getLength()];
                            for (int j = 0; j < nlSubClassOf.getLength(); j++)
                            {
                                Element eSubClassOf = (Element)nlSubClassOf.item(j);
                                oConcept.m_sSubClassOf[j] = eSubClassOf.getAttribute("rdf:" + MetadataConstants.c_XMLEA_resource);
                            }
                        }
                        
                        NodeList nlSuperClassOf = eConcept.getElementsByTagName("rdfs:" + MetadataConstants.c_XMLE_superClassOf);
                        if (nlSuperClassOf != null && nlSuperClassOf.getLength() > 0)
                        {
                            oConcept.m_sSuperClassOf = new String[nlSuperClassOf.getLength()];
                            for (int j = 0; j < nlSuperClassOf.getLength(); j++)
                            {
                                Element eSuperClassOf = (Element)nlSuperClassOf.item(j);
                                oConcept.m_sSuperClassOf[j] = eSuperClassOf.getAttribute("rdf:" + MetadataConstants.c_XMLEA_resource);
                            }
                        }
                        
                        oConcepts[i] = oConcept;
                    }
                    
                    MetadataModel.SaveObjectNewConcept(sEventId, eOriginalData, oConcepts);
                    
                    return oConcepts;
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
    public static String GetValue(Element eElement, String sTag)
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
     * @summary Method for returning attribute of element
     * @startRealisation Sasa Stojanovic 17.05.2012.
     * @finalModification Sasa Stojanovic 17.05.2012.
     * @param eElement - element to read
     * @param sAttribute - attribute to read
     * @return attribute of element
     */
    public static String GetAttribute(Element eElement, String sAttribute)
    {
        String sAttributeValue = "";
        sAttributeValue = eElement.getAttribute(sAttribute);
        return sAttributeValue;
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
            oPerson.m_sObjectURI = GetValue(ePerson, sPrefix + MetadataConstants.c_XMLE_uri);
            
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
            
            String sFirstName = GetValue(ePerson, sPrefix + MetadataConstants.c_XMLE_firstname);
            if (!sFirstName.isEmpty())
                oPerson.m_sFirstName = sFirstName;
            
            String sLastName = GetValue(ePerson, sPrefix + MetadataConstants.c_XMLE_lastname);
            if (!sLastName.isEmpty())
                oPerson.m_sLastName = sLastName;
            
            oPerson.m_sEmail = GetValue(ePerson, sPrefix + MetadataConstants.c_XMLE_email);
            
            oPerson.m_sUsername = GetValue(ePerson, sPrefix + MetadataConstants.c_XMLE_username);
            
            oPerson.m_sWikiEditCount = GetValue(ePerson, sPrefix + MetadataConstants.c_XMLE_editCount);
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
