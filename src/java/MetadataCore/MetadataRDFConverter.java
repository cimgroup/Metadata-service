/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MetadataCore;

import MetadataCore.MetadataGlobal.OntoProperty;
import MetadataObjects.Assigned;
import MetadataObjects.Blocker;
import MetadataObjects.Bug;
import MetadataObjects.Closed;
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
import MetadataObjects.Remind;
import MetadataObjects.Resolved;
import MetadataObjects.ThirdParty;
import MetadataObjects.Trivial;
import MetadataObjects.Verified;
import MetadataObjects.WontFix;
import MetadataObjects.WorksForMe;
import MetadataObjects.doap_Project;
import MetadataObjects.doap_Version;
import MetadataObjects.foaf_Person;
import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;



/**
 *
 * @author ivano
 */
public class MetadataRDFConverter {


    /**
     * @summary Save issue data
     * @startRealisation Ivan Obradovic 31.08.2011.
     * @finalModification Ivan Obradovic 31.08.2011.
     * @param oIssue 
     */
    public static Issue SaveIssue(Issue oIssue)
    {
        try {

            OntModel oModel = MetadataGlobal.LoadOWL(MetadataConstants.sLocationLoadAlert);
            OntModel oModelIts = MetadataGlobal.LoadOWL(MetadataConstants.sLocationLoadAlertIts);
            
            String sTest = "stest";
            String sTest2 = "dasdasd";
            String sTest3 = "dasdasd";
                        
            oIssue.m_sObjectURI = MetadataGlobal.GetObjectURI(oModelIts, MetadataConstants.c_NS_Alert_Its + MetadataConstants.c_OWLClass_Bug, oIssue.m_sID, oModel);
            Resource resBug = oModel.getResource(oIssue.m_sObjectURI);
            oIssue.m_sReturnConfig = "YY#s:" + MetadataConstants.c_XMLE_issue + "/s:" + MetadataConstants.c_XMLE_issue + MetadataConstants.c_XMLE_Uri;
            
            //OntClass ocBug = oModelIts.getOntClass(MetadataConstants.c_NS_Alert_Its + MetadataConstants.c_OWLClass_Bug);
            //oIssue.m_sObjectURI = MetadataConstants.c_NS_Alert_Its + "Bug" + MetadataGlobal.GetNextId(oModel, ocBug);
            //Resource resBug = oModelIts.createResource(oIssue.m_sObjectURI, ocBug );/*.addProperty(DC.identifier,MetadataConstants.c_NS_Alert + "ID")*/

            //already done in GetObjectURI method
            //bug id
            //if (!oIssue.m_sID.isEmpty())
            //{
            //    DatatypeProperty dtpBugId = oModel.getDatatypeProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_ID);
            //    resBug.addProperty(dtpBugId, oIssue.m_sID);
            //}
            
            //bug url
            if (!oIssue.m_sBugURL.isEmpty())
            {
                DatatypeProperty dtpBugUrl = oModel.getDatatypeProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_URL);
                resBug.removeAll(dtpBugUrl);
                resBug.addProperty(dtpBugUrl, oIssue.m_sBugURL);
            }
            
            //date opened
            if (oIssue.m_dtmDateOpened != null)
            {
                DatatypeProperty dtpBugOpend = oModelIts.getDatatypeProperty(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLDataProperty_DateOpened);
                resBug.removeAll(dtpBugOpend);
                resBug.addProperty(dtpBugOpend, oIssue.m_dtmDateOpened.toString());
            }
            
            //Description
            if (!oIssue.m_sDescription.isEmpty())
            {
                DatatypeProperty dtpBugDescription = oModelIts.getDatatypeProperty(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLDataProperty_Description);
                resBug.removeAll(dtpBugDescription);
                resBug.addProperty(dtpBugDescription, oIssue.m_sDescription);
            }
            
            //Keyword
            if (!oIssue.m_sKeyword.isEmpty())
            {
                DatatypeProperty dtpBugKeyword = oModelIts.getDatatypeProperty(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLDataProperty_Keyword);
                resBug.removeAll(dtpBugKeyword);
                resBug.addProperty(dtpBugKeyword, oIssue.m_sKeyword);
            }
            
            //Last Modified
            if (oIssue.m_dtmLastModified != null)
            {
                DatatypeProperty dtpBugLastModified = oModelIts.getDatatypeProperty(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLDataProperty_LastModified);
                resBug.removeAll(dtpBugLastModified);
                resBug.addProperty(dtpBugLastModified, oIssue.m_dtmLastModified.toString());
            }
            
            //Number
            if (oIssue.m_iNumber != null)
            {
                DatatypeProperty dtpBugNumber = oModelIts.getDatatypeProperty(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLDataProperty_Number);
                resBug.removeAll(dtpBugNumber);
                resBug.addProperty(dtpBugNumber, oIssue.m_iNumber.toString());
            }
            
            //Blocks
            if (!oIssue.m_oBlocks.m_sID.isEmpty())
            {
                oIssue.m_oBlocks.m_sObjectURI = MetadataGlobal.GetObjectURI(oModelIts, MetadataConstants.c_NS_Alert_Its + MetadataConstants.c_OWLClass_Bug, oIssue.m_oBlocks.m_sID, oModel);
                ObjectProperty opBlocks = oModelIts.getObjectProperty(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLObjectProperty_Blocks);
                Resource resBlocks = oModel.getResource(oIssue.m_oBlocks.m_sObjectURI);
                resBug.removeAll(opBlocks);
                resBug.addProperty(opBlocks, resBlocks.asResource());
                oIssue.m_oBlocks.m_sReturnConfig = "YN#s:" + MetadataConstants.c_XMLE_issueBlocks + MetadataConstants.c_XMLE_Uri;
            }

            //DependsOn
            if (!oIssue.m_oDependsOn.m_sID.isEmpty())
            {
                oIssue.m_oDependsOn.m_sObjectURI = MetadataGlobal.GetObjectURI(oModelIts, MetadataConstants.c_NS_Alert_Its + MetadataConstants.c_OWLClass_Bug, oIssue.m_oDependsOn.m_sID, oModel);
                ObjectProperty opDependsOn = oModelIts.getObjectProperty(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLObjectProperty_DependsOn);
                Resource resDependsOn = oModel.getResource(oIssue.m_oDependsOn.m_sObjectURI);
                resBug.removeAll(opDependsOn);
                resBug.addProperty(opDependsOn, resDependsOn.asResource());
                oIssue.m_oDependsOn.m_sReturnConfig = "YN#s:" + MetadataConstants.c_XMLE_issueDependsOn + MetadataConstants.c_XMLE_Uri;
            }
            
            //HasReporter
            if (!oIssue.m_oHasReporter.m_sID.isEmpty())
            {
                SavePersonData(oIssue.m_oHasReporter, oModel);
                ObjectProperty opHasReporter = oModelIts.getObjectProperty(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLObjectProperty_HasReporter);
                Resource resHasReporter = oModel.getResource(oIssue.m_oHasReporter.m_sObjectURI);
                resBug.removeAll(opHasReporter);
                resBug.addProperty(opHasReporter, resHasReporter.asResource());
                oIssue.m_oHasReporter.m_sReturnConfig = "YN#s:" + MetadataConstants.c_XMLE_issueAuthor + MetadataConstants.c_XMLE_Uri;
            }

            //HasState
            if (oIssue.m_oHasState != null)
            {
                ObjectProperty opHasState = oModelIts.getObjectProperty(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLObjectProperty_HasState);
                resBug.removeAll(opHasState);
                
                Class clsHasState = oIssue.m_oHasState.getClass();

                Class clsAssigned = Assigned.class;
                Class clsOpen= Open.class;
                Class clsVerified = Verified.class;
                Class clsResolved = Resolved.class;
                Class clsClosed = Closed.class;

                if (clsHasState.equals(clsAssigned))
                {
                    OntClass ocHasState = oModelIts.getOntClass(MetadataConstants.c_NS_Alert_Its + MetadataConstants.c_OWLClass_Assigned);
                    resBug.addProperty(opHasState, ocHasState.asResource());
                }
                if (clsHasState.equals(clsOpen))
                {
                    OntClass ocHasState = oModelIts.getOntClass(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLClass_Open);
                    resBug.addProperty(opHasState, ocHasState.asResource());
                }
                if (clsHasState.equals(clsVerified))
                {
                    OntClass ocHasState = oModelIts.getOntClass(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLClass_Verified);
                    resBug.addProperty(opHasState, ocHasState.asResource());
                }
                if (clsHasState.equals(clsResolved))
                {
                    OntClass ocHasState = oModelIts.getOntClass(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLClass_Resolved);
                    resBug.addProperty(opHasState, ocHasState.asResource());
                }
                if (clsHasState.equals(clsClosed))
                {
                    OntClass ocHasState = oModelIts.getOntClass(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLClass_Closed);
                    resBug.addProperty(opHasState, ocHasState.asResource());
                }
            }
            //end has state
            
            
            //HasResolution
            if (oIssue.m_oHasResolution != null)
            {
                ObjectProperty opHasResolution = oModelIts.getObjectProperty(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLObjectProperty_HasResolution);
                resBug.removeAll(opHasResolution);
                
                Class clsHasResolution = oIssue.m_oHasResolution.getClass();

                Class clsDuplicate = Duplicate.class;
                Class clsFixed= Fixed.class;
                Class clsInvalid = Invalid.class;
                Class clsThirdParty = ThirdParty.class;
                Class clsWontFix = WontFix.class;
                Class clsWorksForMe = WorksForMe.class;
                Class clsLater = Later.class;
                Class clsRemind = Remind.class;

                if (clsHasResolution.equals(clsDuplicate))
                {
                    OntClass ocHasResolution = oModelIts.getOntClass(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLClass_Duplicate);
                    resBug.addProperty(opHasResolution, ocHasResolution.asResource());
                }
                if (clsHasResolution.equals(clsFixed))
                {
                    OntClass ocHasResolution = oModelIts.getOntClass(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLClass_Fixed);
                    resBug.addProperty(opHasResolution, ocHasResolution.asResource());
                }
                if (clsHasResolution.equals(clsInvalid))
                {
                    OntClass ocHasResolution = oModelIts.getOntClass(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLClass_Invalid);
                    resBug.addProperty(opHasResolution, ocHasResolution.asResource());
                }
                if (clsHasResolution.equals(clsThirdParty))
                {
                    OntClass ocHasResolution = oModelIts.getOntClass(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLClass_ThirdParty);
                    resBug.addProperty(opHasResolution, ocHasResolution.asResource());
                }
                if (clsHasResolution.equals(clsWontFix))
                {
                    OntClass ocHasResolution = oModelIts.getOntClass(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLClass_WontFix);
                    resBug.addProperty(opHasResolution, ocHasResolution.asResource());
                }
                if (clsHasResolution.equals(clsWorksForMe))
                {
                    OntClass ocHasResolution = oModelIts.getOntClass(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLClass_WorksForMe);
                    resBug.addProperty(opHasResolution, ocHasResolution.asResource());
                }
                if (clsHasResolution.equals(clsLater))
                {
                    OntClass ocHasResolution = oModelIts.getOntClass(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLClass_Later);
                    resBug.addProperty(opHasResolution, ocHasResolution.asResource());
                }
                if (clsHasResolution.equals(clsRemind))
                {
                    OntClass ocHasResolution = oModelIts.getOntClass(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLClass_Remind);
                    resBug.addProperty(opHasResolution, ocHasResolution.asResource());
                }
            }
            //end has resolution
            
            //Issue product
            if (oIssue.m_oIsIssueOf != null)
            {
                oIssue.m_oIsIssueOf.m_sObjectURI = MetadataGlobal.GetObjectURI(oModelIts, MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLClass_Component, oIssue.m_oIsIssueOf.m_sID, oModel);
                ObjectProperty opIsIssueOf = oModelIts.getObjectProperty(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLObjectProperty_IsIssueOf);
                Resource resIsIssueOf = oModel.getResource(oIssue.m_oIsIssueOf.m_sObjectURI);
                
                if (oIssue.m_oIsIssueOf.m_oIsComponentOf != null)
                {
                    oIssue.m_oIsIssueOf.m_oIsComponentOf.m_sObjectURI = MetadataGlobal.GetObjectURI(oModelIts, MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLClass_Product, oIssue.m_oIsIssueOf.m_oIsComponentOf.m_sID, oModel);
                    ObjectProperty opIsComponentOf = oModelIts.getObjectProperty(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLObjectProperty_IsComponentOf);
                    Resource resIsComponentOf = oModel.getResource(oIssue.m_oIsIssueOf.m_oIsComponentOf.m_sObjectURI);
                     
                    if (!oIssue.m_oIsIssueOf.m_oIsComponentOf.m_sVersion.isEmpty())
                    {
                        DatatypeProperty dtpVersion = oModelIts.getDatatypeProperty(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLDataProperty_Version);
                        resIsComponentOf.removeAll(dtpVersion);
                        resIsComponentOf.addProperty(dtpVersion, oIssue.m_oIsIssueOf.m_oIsComponentOf.m_sVersion.toString());
                    }
                    
                    resIsIssueOf.removeAll(opIsComponentOf);
                    resIsIssueOf.addProperty(opIsComponentOf, resIsComponentOf.asResource());
                    oIssue.m_oIsIssueOf.m_oIsComponentOf.m_sReturnConfig = "YN#s:" + MetadataConstants.c_XMLE_product + MetadataConstants.c_XMLE_Uri;
                }
                
                resBug.removeAll(opIsIssueOf);
                resBug.addProperty(opIsIssueOf, resIsIssueOf.asResource());
                oIssue.m_oIsIssueOf.m_sReturnConfig = "YY#s:" + MetadataConstants.c_XMLE_issueProduct + "/s:" + MetadataConstants.c_XMLE_productComponent + MetadataConstants.c_XMLE_Uri;
            }

            //HasComputerSystem
            if (oIssue.m_oHasComputerSystem != null && (!oIssue.m_oHasComputerSystem.m_sID.isEmpty() || !oIssue.m_oHasComputerSystem.m_sPlatform.isEmpty() || !oIssue.m_oHasComputerSystem.m_sOs.isEmpty()))
            {
                oIssue.m_oHasComputerSystem.m_sObjectURI = MetadataGlobal.GetObjectURI(oModelIts, MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLClass_ComputerSystem, oIssue.m_oHasComputerSystem.m_sID, oModel);
                ObjectProperty opHasComputerSystem = oModelIts.getObjectProperty(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLObjectProperty_HasComputerSystem);
                Resource resComputerSystem = oModel.getResource(oIssue.m_oHasComputerSystem.m_sObjectURI);
                if (!oIssue.m_oHasComputerSystem.m_sPlatform.isEmpty())
                {
                    DatatypeProperty dtpPlatform = oModelIts.getDatatypeProperty(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLDataProperty_Platform);
                    resComputerSystem.removeAll(dtpPlatform);
                    resComputerSystem.addProperty(dtpPlatform, oIssue.m_oHasComputerSystem.m_sPlatform);
                }
                if (!oIssue.m_oHasComputerSystem.m_sOs.isEmpty())
                {
                    DatatypeProperty dtpOs = oModelIts.getDatatypeProperty(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLDataProperty_Os);
                    resComputerSystem.removeAll(dtpOs);
                    resComputerSystem.addProperty(dtpOs, oIssue.m_oHasComputerSystem.m_sOs);
                }
                resBug.removeAll(opHasComputerSystem);
                resBug.addProperty(opHasComputerSystem, resComputerSystem.asResource());
                oIssue.m_oHasComputerSystem.m_sReturnConfig = "YN#s:" + MetadataConstants.c_XMLE_issueComputerSystem + "/s:" + MetadataConstants.c_XMLE_issueComputerSystem + MetadataConstants.c_XMLE_Uri;
            }
            
            //HasPriority
            if (oIssue.m_oHasPriority.m_iPriority != 0)
            {
                ObjectProperty opHasPriority = oModelIts.getObjectProperty(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLObjectProperty_HasPriority);
                resBug.removeAll(opHasPriority);
                
                if(oIssue.m_oHasPriority.m_iPriority == 1)
                {
                    OntClass ocHasPriority = oModelIts.getOntClass(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLClass_P1);
                    resBug.addProperty(opHasPriority, ocHasPriority.asResource());
                }
                if(oIssue.m_oHasPriority.m_iPriority == 2)
                {
                    OntClass ocHasPriority = oModelIts.getOntClass(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLClass_P2);
                    resBug.addProperty(opHasPriority, ocHasPriority.asResource());
                }
                if(oIssue.m_oHasPriority.m_iPriority == 3)
                {
                    OntClass ocHasPriority = oModelIts.getOntClass(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLClass_P3);
                    resBug.addProperty(opHasPriority, ocHasPriority.asResource());
                }
                if(oIssue.m_oHasPriority.m_iPriority == 4)
                {
                    OntClass ocHasPriority = oModelIts.getOntClass(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLClass_P4);
                    resBug.addProperty(opHasPriority, ocHasPriority.asResource());
                }
                if(oIssue.m_oHasPriority.m_iPriority == 5)
                {
                    OntClass ocHasPriority = oModelIts.getOntClass(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLClass_P5);
                    resBug.addProperty(opHasPriority, ocHasPriority.asResource());
                }
            }
            //end has priority
            
            //HasSeverity
            if (oIssue.m_oHasSeverity != null)
            {
                ObjectProperty opHasSeverity = oModelIts.getObjectProperty(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLObjectProperty_HasSeverity);
                resBug.removeAll(opHasSeverity);
                
                Class clsHasSeverity = oIssue.m_oHasSeverity.getClass();

                Class clsBlocker = Blocker.class;
                Class clsCritical= Critical.class;
                Class clsMajor = Major.class;
                Class clsMinor = Minor.class;
                Class clsTrivial = Trivial.class;

                if (clsHasSeverity.equals(clsBlocker))
                {
                    OntClass ocHasSeverity = oModelIts.getOntClass(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLClass_Blocker);
                    resBug.addProperty(opHasSeverity, ocHasSeverity.asResource());
                }
                if (clsHasSeverity.equals(clsCritical))
                {
                    OntClass ocHasSeverity = oModelIts.getOntClass(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLClass_Critical);
                    resBug.addProperty(opHasSeverity, ocHasSeverity.asResource());
                }
                if (clsHasSeverity.equals(clsMajor))
                {
                    OntClass ocHasSeverity = oModelIts.getOntClass(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLClass_Major);
                    resBug.addProperty(opHasSeverity, ocHasSeverity.asResource());
                }
                if (clsHasSeverity.equals(clsMinor))
                {
                    OntClass ocHasSeverity = oModelIts.getOntClass(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLClass_Minor);
                    resBug.addProperty(opHasSeverity, ocHasSeverity.asResource());
                }
                if (clsHasSeverity.equals(clsTrivial))
                {
                    OntClass ocHasSeverity = oModelIts.getOntClass(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLClass_Trivial);
                    resBug.addProperty(opHasSeverity, ocHasSeverity.asResource());
                }
            }
            //end has severity
                                  
            //HasAssignee
            if (!oIssue.m_oHasAssignee.m_sID.isEmpty())
            {
                SavePersonData(oIssue.m_oHasAssignee, oModel);
                ObjectProperty opHasAssignee = oModelIts.getObjectProperty(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLObjectProperty_HasAssignee);
                Resource resHasAssignee = oModel.getResource(oIssue.m_oHasAssignee.m_sObjectURI);
                resBug.removeAll(opHasAssignee);
                resBug.addProperty(opHasAssignee, resHasAssignee.asResource());
                oIssue.m_oHasAssignee.m_sReturnConfig = "YN#s:" + MetadataConstants.c_XMLE_issueAssignedTo + MetadataConstants.c_XMLE_Uri;
            }
            
            //HasCCPerson
            if (oIssue.m_oHasCCPerson != null)
            {
                ObjectProperty opHasCCPerson = oModelIts.getObjectProperty(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLObjectProperty_HasCCPerson);
                for (int i = 0; i < oIssue.m_oHasCCPerson.length; i++)
                {
                    if (oIssue.m_oHasCCPerson[i] != null && !oIssue.m_oHasCCPerson[i].m_sID.isEmpty())
                    {
                        //oIssue.m_oHasCCPerson[i].m_sObjectURI = MetadataGlobal.GetObjectURI(oModel, MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLClass_Person, oIssue.m_oHasCCPerson[i].m_sID, oModel);
                        SavePersonData(oIssue.m_oHasCCPerson[i], oModel);
                        Resource resHasCCPerson = oModel.getResource(oIssue.m_oHasCCPerson[i].m_sObjectURI);
                        resBug.addProperty(opHasCCPerson, resHasCCPerson.asResource());
                        oIssue.m_oHasCCPerson[i].m_sReturnConfig = "YN#s:" + MetadataConstants.c_XMLE_issueCCPerson + MetadataConstants.c_XMLE_Uri;
                    }
                }
            }
                       
            //IsDuplicateOf
            if (!oIssue.m_oIsDuplicateOf.m_sID.isEmpty())
            {
                oIssue.m_oIsDuplicateOf.m_sObjectURI = MetadataGlobal.GetObjectURI(oModelIts, MetadataConstants.c_NS_Alert_Its + MetadataConstants.c_OWLClass_Bug, oIssue.m_oIsDuplicateOf.m_sID, oModel);
                ObjectProperty opIsDuplicateOf = oModelIts.getObjectProperty(MetadataConstants.c_NS_Alert_Its + MetadataConstants.c_OWLObjectProperty_IsDuplicateOf);
                Resource resIsDuplicateOf = oModel.getResource(oIssue.m_oIsDuplicateOf.m_sObjectURI);
                resBug.removeAll(opIsDuplicateOf);
                resBug.addProperty(opIsDuplicateOf, resIsDuplicateOf.asResource());
                oIssue.m_oIsDuplicateOf.m_sReturnConfig = "YN#s:" + MetadataConstants.c_XMLE_issueDuplicateOf + MetadataConstants.c_XMLE_Uri;
            }
            
            //IsMergedInto
            if (!oIssue.m_oIsMergedInto.m_sID.isEmpty())
            {
                oIssue.m_oIsMergedInto.m_sObjectURI = MetadataGlobal.GetObjectURI(oModelIts, MetadataConstants.c_NS_Alert_Its + MetadataConstants.c_OWLClass_Bug, oIssue.m_oIsMergedInto.m_sID, oModel);
                ObjectProperty opIsMergedInto = oModelIts.getObjectProperty(MetadataConstants.c_NS_Alert_Its + MetadataConstants.c_OWLObjectProperty_IsMergedInto);
                Resource resIsMergedInto = oModel.getResource(oIssue.m_oIsMergedInto.m_sObjectURI);
                resBug.removeAll(opIsMergedInto);
                resBug.addProperty(opIsMergedInto, resIsMergedInto.asResource());
                oIssue.m_oIsMergedInto.m_sReturnConfig = "YN#s:" + MetadataConstants.c_XMLE_issueMergedInto + MetadataConstants.c_XMLE_Uri;
            }
            
            //HasMilestone
            if (oIssue.m_oHasMilestone != null && (!oIssue.m_oHasMilestone.m_sID.isEmpty() || oIssue.m_oHasMilestone.m_dtmTarget != null))
            {
                oIssue.m_oHasMilestone.m_sObjectURI = MetadataGlobal.GetObjectURI(oModelIts, MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLClass_Milestone, oIssue.m_oHasMilestone.m_sID, oModel);
                ObjectProperty opHasMilestone = oModelIts.getObjectProperty(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLObjectProperty_HasMilestone);
                Resource resMilestone = oModel.getResource(oIssue.m_oHasMilestone.m_sObjectURI);
                if (oIssue.m_oHasMilestone.m_dtmTarget != null)
                {
                    DatatypeProperty dtpMilestone= oModelIts.getDatatypeProperty(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLDataProperty_Target);
                    resMilestone.removeAll(dtpMilestone);
                    resMilestone.addProperty(dtpMilestone, oIssue.m_oHasMilestone.m_dtmTarget.toString());
                }
                resBug.removeAll(opHasMilestone);
                resBug.addProperty(opHasMilestone, resMilestone.asResource());
                oIssue.m_oHasMilestone.m_sReturnConfig = "YN#s:" + MetadataConstants.c_XMLE_issueMilestone + "/s:" + MetadataConstants.c_XMLE_issueMilestone + MetadataConstants.c_XMLE_Uri;
            }
            
            //HasComment
            if (oIssue.m_oHasComment != null)
            {
                ObjectProperty opHasComment = oModelIts.getObjectProperty(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLObjectProperty_HasComment);
                for (int i = 0; i < oIssue.m_oHasComment.length; i++)
                {
                    if (oIssue.m_oHasComment[i] != null && (!oIssue.m_oHasComment[i].m_sID.isEmpty() || !oIssue.m_oHasComment[i].m_sText.isEmpty()))
                    {
                        oIssue.m_oHasComment[i].m_sObjectURI = MetadataGlobal.GetObjectURI(oModelIts, MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLClass_Comment, oIssue.m_oHasComment[i].m_sID, oModel);
                        Resource resComment = oModel.getResource(oIssue.m_oHasComment[i].m_sObjectURI);

                        if (oIssue.m_oHasComment[i].m_iNumber != null)
                        {
                            DatatypeProperty dtpCommentNumber= oModelIts.getDatatypeProperty(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLDataProperty_Number);
                            resComment.removeAll(dtpCommentNumber);
                            resComment.addProperty(dtpCommentNumber, oIssue.m_oHasComment[i].m_iNumber.toString());
                        }

                        if (oIssue.m_oHasComment[i].m_dtmDate != null)
                        {
                            DatatypeProperty dtpCommentDate= oModelIts.getDatatypeProperty( MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLDataProperty_Date );
                            resComment.removeAll(dtpCommentDate);
                            resComment.addProperty(dtpCommentDate, oIssue.m_oHasComment[i].m_dtmDate.toString());
                        }

                        if (!oIssue.m_oHasComment[i].m_sText.isEmpty())
                        {
                            DatatypeProperty dtpCommentText= oModelIts.getDatatypeProperty(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLDataProperty_Text);
                            resComment.removeAll(dtpCommentText);
                            resComment.addProperty(dtpCommentText, oIssue.m_oHasComment[i].m_sText);
                        }

                        if (oIssue.m_oHasComment[i].m_oHasCommentor != null && oIssue.m_oHasComment[i].m_oHasCommentor.m_sID != null && !oIssue.m_oHasComment[i].m_oHasCommentor.m_sID.isEmpty())
                        {
                            //oIssue.m_oHasComment[i].m_oHasCommentor.m_sObjectURI = MetadataGlobal.GetObjectURI(oModel, MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLClass_Person, oIssue.m_oHasComment[i].m_oHasCommentor.m_sID, oModel);
                            SavePersonData(oIssue.m_oHasComment[i].m_oHasCommentor, oModel);
                            ObjectProperty opHasCommentor = oModelIts.getObjectProperty(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLObjectProperty_HasCommentor);
                            Resource resHasCommentor = oModel.getResource(oIssue.m_oHasComment[i].m_oHasCommentor.m_sObjectURI);
                            resComment.removeAll(opHasCommentor);
                            resComment.addProperty(opHasCommentor, resHasCommentor.asResource());
                            oIssue.m_oHasComment[i].m_oHasCommentor.m_sReturnConfig = "YN#s:" + MetadataConstants.c_XMLE_commentPerson + MetadataConstants.c_XMLE_Uri;
                        }

                        resBug.addProperty(opHasComment, resComment.asResource());
                        oIssue.m_oHasComment[i].m_sReturnConfig = "YY#s:" + MetadataConstants.c_XMLE_issueComment + "/s:" + MetadataConstants.c_XMLE_comment + MetadataConstants.c_XMLE_Uri;
                    }
                }
            }
            //end has comment 
            
            //HasAttachment
            if (oIssue.m_oHasAttachment != null)
            {
                ObjectProperty opHasAttachment = oModelIts.getObjectProperty(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLObjectProperty_HasAttachment);
                for (int i = 0; i < oIssue.m_oHasAttachment.length; i++)
                {
                    if (oIssue.m_oHasAttachment[i] != null && (!oIssue.m_oHasAttachment[i].m_sID.isEmpty() || !oIssue.m_oHasAttachment[i].m_sFilename.isEmpty()))
                    {
                        oIssue.m_oHasAttachment[i].m_sObjectURI = MetadataGlobal.GetObjectURI(oModelIts, MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLClass_Attachment, oIssue.m_oHasAttachment[i].m_sID, oModel);
                        Resource resAttachment = oModel.getResource(oIssue.m_oHasAttachment[i].m_sObjectURI);

                        if (!oIssue.m_oHasAttachment[i].m_sFilename.isEmpty())
                        {
                            DatatypeProperty dtpAttachmentFileName= oModelIts.getDatatypeProperty(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLDataProperty_FileName);
                            resAttachment.removeAll(dtpAttachmentFileName);
                            resAttachment.addProperty(dtpAttachmentFileName, oIssue.m_oHasAttachment[i].m_sFilename);
                        }

                        if (!oIssue.m_oHasAttachment[i].m_sType.isEmpty())
                        {
                            DatatypeProperty dtpAttachmentType= oModelIts.getDatatypeProperty(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLDataProperty_Type);
                            resAttachment.removeAll(dtpAttachmentType);
                            resAttachment.addProperty(dtpAttachmentType, oIssue.m_oHasAttachment[i].m_sType);
                        }

                        if (oIssue.m_oHasAttachment[i].m_oHasCreator != null && oIssue.m_oHasAttachment[i].m_oHasCreator.m_sID != null && !oIssue.m_oHasAttachment[i].m_oHasCreator.m_sID.isEmpty())
                        {
                            //oIssue.m_oHasAttachment[i].m_oHasCreator.m_sObjectURI = MetadataGlobal.GetObjectURI(oModel, MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLClass_Person, oIssue.m_oHasAttachment[i].m_oHasCreator.m_sID, oModel);
                            SavePersonData(oIssue.m_oHasAttachment[i].m_oHasCreator, oModel);
                            ObjectProperty opHasCreator = oModelIts.getObjectProperty(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLObjectProperty_HasCreator);
                            Resource resHasCreator = oModel.getResource(oIssue.m_oHasAttachment[i].m_oHasCreator.m_sObjectURI);
                            resAttachment.removeAll(opHasCreator);
                            resAttachment.addProperty(opHasCreator, resHasCreator.asResource());
                            oIssue.m_oHasComment[i].m_oHasCreator.m_sReturnConfig = "YN#s:" + MetadataConstants.c_XMLE_attachmentCreator + MetadataConstants.c_XMLE_Uri;
                        }

                        resBug.addProperty(opHasAttachment, resAttachment.asResource());
                        oIssue.m_oHasComment[i].m_sReturnConfig = "YY#s:" + MetadataConstants.c_XMLE_issueAttachment + "/s:" + MetadataConstants.c_XMLE_attachment + MetadataConstants.c_XMLE_Uri;
                    }
                }
            }
            //end has attachment 
            
            //HasActivity
            if (oIssue.m_oHasActivity != null)
            {
                ObjectProperty opHasActivity = oModelIts.getObjectProperty(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLObjectProperty_HasActivity);
                for (int i = 0; i < oIssue.m_oHasActivity.length; i++)
                {
                    if (oIssue.m_oHasActivity[i] != null && (!oIssue.m_oHasActivity[i].m_sID.isEmpty() || !oIssue.m_oHasActivity[i].m_sWhat.isEmpty()))
                    {
                        oIssue.m_oHasActivity[i].m_sObjectURI = MetadataGlobal.GetObjectURI(oModelIts, MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLClass_Activity, oIssue.m_oHasActivity[i].m_sID, oModel);
                        Resource resActivity = oModel.getResource(oIssue.m_oHasActivity[i].m_sObjectURI);

                        if (!oIssue.m_oHasActivity[i].m_sWhat.isEmpty())
                        {
                            DatatypeProperty dtpActivityWhat = oModelIts.getDatatypeProperty(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLDataProperty_What);
                            resActivity.removeAll(dtpActivityWhat);
                            resActivity.addProperty(dtpActivityWhat, oIssue.m_oHasActivity[i].m_sWhat);
                        }
                        
                        if (!oIssue.m_oHasActivity[i].m_sRemoved.isEmpty())
                        {
                            DatatypeProperty dtpActivityRemoved = oModelIts.getDatatypeProperty(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLDataProperty_Removed);
                            resActivity.removeAll(dtpActivityRemoved);
                            resActivity.addProperty(dtpActivityRemoved, oIssue.m_oHasActivity[i].m_sRemoved);
                        }
                        
                        if (!oIssue.m_oHasActivity[i].m_sAdded.isEmpty())
                        {
                            DatatypeProperty dtpActivityAdded = oModelIts.getDatatypeProperty(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLDataProperty_Added);
                            resActivity.removeAll(dtpActivityAdded);
                            resActivity.addProperty(dtpActivityAdded, oIssue.m_oHasActivity[i].m_sAdded);
                        }
                        
                        if (oIssue.m_oHasActivity[i].m_dtmWhen != null)
                        {
                            DatatypeProperty dtpActivityPerformed = oModelIts.getDatatypeProperty(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLDataProperty_Performed);
                            resActivity.removeAll(dtpActivityPerformed);
                            resActivity.addProperty(dtpActivityPerformed, oIssue.m_oHasActivity[i].m_dtmWhen.toString());
                        }
                        
                        if (oIssue.m_oHasActivity[i].m_oHasInvolvedPerson != null && oIssue.m_oHasActivity[i].m_oHasInvolvedPerson.m_sID != null && !oIssue.m_oHasActivity[i].m_oHasInvolvedPerson.m_sID.isEmpty())
                        {
                            oIssue.m_oHasActivity[i].m_oHasInvolvedPerson.m_sObjectURI = MetadataGlobal.GetObjectURI(oModel, MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLClass_Person, oIssue.m_oHasActivity[i].m_oHasInvolvedPerson.m_sID, oModel);
                            ObjectProperty opHasInvolvedPerson = oModelIts.getObjectProperty(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLObjectProperty_HasInvolvedPerson);
                            Resource resHasInvolvedPerson = oModel.getResource(oIssue.m_oHasActivity[i].m_oHasInvolvedPerson.m_sObjectURI);
                            resActivity.removeAll(opHasInvolvedPerson);
                            resActivity.addProperty(opHasInvolvedPerson, resHasInvolvedPerson.asResource());
                            oIssue.m_oHasActivity[i].m_oHasInvolvedPerson.m_sReturnConfig = "YN#s:" + MetadataConstants.c_XMLE_activityWho + MetadataConstants.c_XMLE_Uri;
                        }

                        resBug.addProperty(opHasActivity, resActivity.asResource());
                        oIssue.m_oHasActivity[i].m_sReturnConfig = "YY#s:" + MetadataConstants.c_XMLE_issueActivity + "/s:" + MetadataConstants.c_XMLE_activity + MetadataConstants.c_XMLE_Uri;
                    }
                }
            }
            //end has attachment 
            
            //save data
            MetadataGlobal.SaveOWL(oModel, MetadataConstants.sLocationSaveAlert);
            MetadataGlobal.SaveOWL(oModelIts, MetadataConstants.sLocationSaveAlertIts);
            
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return oIssue;
    }
    
    /**
     * @summary Save person data
     * @startRealisation Sasa Stojanovic 02.09.2011.
     * @finalModification Sasa Stojanovic 06.09.2011.
     * @param oPerson - foaf_person object
     * @return - same foaf_person object with filled m_sObjectURI
     */
    public static foaf_Person SavePerson(foaf_Person oPerson)
    {
        try
        {
            OntModel omModel = MetadataGlobal.LoadOWL(MetadataConstants.sLocationLoadAlert);
            
            OntClass ocPerson = omModel.getOntClass(MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLClass_Person);
            
            oPerson.m_sObjectURI = MetadataConstants.c_NS_Alert + "Person" + MetadataGlobal.GetNextId(omModel, ocPerson);
            
            Individual inPerson = omModel.createIndividual(oPerson.m_sObjectURI, ocPerson );
            DatatypeProperty dtpFirstName = omModel.getDatatypeProperty(MetadataConstants.c_NS_foaf + "firstName");
            inPerson.addProperty(dtpFirstName, oPerson.m_sFirstName);
            DatatypeProperty dtpLastName = omModel.getDatatypeProperty(MetadataConstants.c_NS_foaf + "lastName");
            inPerson.addProperty(dtpLastName, oPerson.m_sLastName);
            DatatypeProperty dtpGender = omModel.getDatatypeProperty(MetadataConstants.c_NS_foaf + "gender");
            inPerson.addProperty(dtpGender, oPerson.m_sGender);
            
            MetadataGlobal.SaveOWL(omModel, MetadataConstants.sLocationSaveAlert);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return oPerson;
    }
    
    /**
     * @summary Save person data
     * @startRealisation Sasa Stojanovic 02.09.2011.
     * @finalModification Sasa Stojanovic 06.09.2011.
     * @param oPerson - foaf_person object
     * @return - same foaf_person object with filled m_sObjectURI
     */
    public static void SavePersonData(foaf_Person oPerson, OntModel oModel)
    {
        try
        {
            oPerson.m_sObjectURI = MetadataGlobal.GetObjectURI(oModel, MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLClass_Person, oPerson.m_sID, oModel);
            Resource resPerson = oModel.getResource(oPerson.m_sObjectURI);
            
            if (!oPerson.m_sFirstName.isEmpty())
            {
                DatatypeProperty dtpFirstName = oModel.getDatatypeProperty(MetadataConstants.c_NS_foaf + MetadataConstants.c_OWLDataProperty_FirstName);
                resPerson.removeAll(dtpFirstName);
                resPerson.addProperty(dtpFirstName, oPerson.m_sFirstName);
            }

            if (!oPerson.m_sLastName.isEmpty())
            {
                DatatypeProperty dtpLastName = oModel.getDatatypeProperty(MetadataConstants.c_NS_foaf + MetadataConstants.c_OWLDataProperty_LastName);
                resPerson.removeAll(dtpLastName);
                resPerson.addProperty(dtpLastName, oPerson.m_sLastName);
            }
            
            OntModel oModelScm = MetadataGlobal.LoadOWL(MetadataConstants.sLocationLoadAlertScm);
            if (!oPerson.m_sEmail.isEmpty())
            {
                DatatypeProperty dtpEmail = oModelScm.getDatatypeProperty(MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLDataProperty_Email);
                resPerson.removeAll(dtpEmail);
                resPerson.addProperty(dtpEmail, oPerson.m_sEmail);
            }
            MetadataGlobal.SaveOWL(oModelScm, MetadataConstants.sLocationSaveAlertScm);
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    
    /**
     * @summary get all members
     * @startRealisation Sasa Stojanovic 06.09.2011.
     * @finalModification Sasa Stojanovic 06.09.2011.
     * @param sOntClass - ont class URL
     * @return - ArrayList of members URLs
     */
    public static MetadataGlobal.APIResponseData GetAllMembers(String sOntClass)
    {
        MetadataGlobal.APIResponseData oData = new MetadataGlobal.APIResponseData();
        try
        {
            OntModel oModel;
            if (sOntClass.contains(MetadataConstants.c_NS_Alert_Its) || sOntClass.contains(MetadataConstants.c_NS_Ifi))
                oModel = MetadataGlobal.LoadOWL(MetadataConstants.sLocationLoadAlertIts);
            else
                oModel = MetadataGlobal.LoadOWL(MetadataConstants.sLocationLoadAlert);
            OntClass ocMember = oModel.getOntClass(sOntClass);
            
            ExtendedIterator iIterator = ocMember.listInstances();

            while(iIterator.hasNext())
            {               
                MetadataGlobal.APIResponseData oSubData = new MetadataGlobal.APIResponseData();
                oSubData.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_item;
                oSubData.sData = iIterator.next().toString();
                oData.oData.add(oSubData);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return oData;
    }
    
    /**
     * @summary get specific member data
     * @startRealisation Sasa Stojanovic 07.09.2011.
     * @finalModification Sasa Stojanovic 07.09.2011.
     * @param sMemberURL - member URL
     * @return - filled OntoProperty object
     */
    public static OntoProperty GetMember(String sMemberURL)
    {
        OntoProperty oProjeprty = new OntoProperty();
        try
        {
            oProjeprty.sName = "";
            oProjeprty.sValue = sMemberURL;
            
            OntModel omModel = MetadataGlobal.LoadOWL(MetadataConstants.sLocationLoadAlert);
            OntModel omModelIts = MetadataGlobal.LoadOWL(MetadataConstants.sLocationLoadAlertIts);
            //omModel.add(omModelIts);
            
            Individual inMember;
            if (sMemberURL.startsWith(MetadataConstants.c_NS_Alert_Its) || sMemberURL.startsWith(MetadataConstants.c_NS_Ifi))
                inMember = omModelIts.getIndividual(sMemberURL);
            else
                inMember = omModel.getIndividual(sMemberURL);
            StmtIterator iIterator = inMember.listProperties();

            while(iIterator.hasNext())
            {
                Statement stExpresion = iIterator.next();
                Property pProperty = stExpresion.getPredicate();
                String sName = pProperty.getLocalName();
                String sTypeOf = pProperty.getURI();
                String sNameSpace = pProperty.getNameSpace();
                
                RDFNode nObject = stExpresion.getObject();
                String sValue = nObject.toString();
                
//                if (nObject.isResource())
//                if (nObject.isLiteral())
                
                if (sTypeOf.equals("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"))
                {
                    oProjeprty.sTypeOf = sValue;
                }
                else
                {
                    OntProperty opProperty;
                    if (sNameSpace.equals(MetadataConstants.c_NS_Alert_Its) || sNameSpace.equals(MetadataConstants.c_NS_Ifi))
                        opProperty = omModelIts.getOntProperty(sTypeOf);
                    else
                        opProperty = omModel.getOntProperty(sTypeOf);
                    OntResource orResource = opProperty.getRange();
                    if (orResource != null)
                        sTypeOf = orResource.toString();
                    else
                        sTypeOf = "";
                    
                    OntoProperty oSubProjeprty = new OntoProperty();
                    oSubProjeprty.sName = sName;
                    oSubProjeprty.sTypeOf = sTypeOf;
                    oSubProjeprty.sValue = sValue;
                    oProjeprty.oProperties.add(oSubProjeprty);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return oProjeprty;
    }
    
    
    public static void CreateQuery() throws FileNotFoundException, IOException
    {
//       try {
//         OntModel ontologyModel = LoadOWL();
//         CreateNewQuery(ontologyModel);
//         
//         //SaveNewDataInOWL();
//         
//         CreateTriples();
//         RemoveOWLClass();
//         } catch (Exception ex) {
//            ex.printStackTrace();
//  
//         }
    }
    
    private static void CreateNewQuery(OntModel ontologyModel)
    {
//        String queryString = "Select * Where {?s ?p ?o}";
//         // Execute the query and obtain results
//        QueryExecution qe = QueryExecutionFactory.create(queryString, ontologyModel);
//        try {
//        ResultSet results = qe.execSelect() ;
//        for ( ; results.hasNext() ; )
//        {
//          QuerySolution soln1 = results.next() ;
//            QuerySolution soln = results.nextSolution() ;
//          RDFNode x = soln.get("Subject") ;       // Get a result variable by name.
//          Resource r = soln.getResource("Predict") ; // Get a result variable - must be a resource
//          Literal l = soln.getLiteral("Object") ;   // Get a result variable - must be a literal
//        }
//      } finally { qe.close() ; }
    }
    
    
public static void CreateTriples()
    {
//        try {
//
// 
//            OntModel model = ModelFactory.createOntologyModel();
//            Resource r;
//            ResIterator resourceIter;
// 
//             model.read(MetadataConstants.sLocationFile);  
//             String NS = MetadataConstants.sOWLrdf;
//             OntClass place = model.getOntClass( NS + "Property" );
//             String anchor = "rdf:Bug#";
//
//
//             Resource S2 = model.createResource( anchor + "BugR" );
//             Resource S = model.createResource( anchor + "sBug" );
//             Property P = model.createProperty( anchor + "pBug" );
//             RDFNode O = model.createLiteral( anchor + "oBug" );
//             Statement SPO = model.createStatement( S, P, O );
//             Statement SPO2 = model.createStatement( S2, P, O );
//
//
//       File destinationFile = new File(MetadataConstants.sLocation);
//       FileOutputStream fosOWL;
//
//       fosOWL = new FileOutputStream(destinationFile,false);
//
//       model.write(fosOWL ,"RDF/XML-ABBREV");
//
//       fosOWL.flush();
//
//       fosOWL.close();
//
//
//        } catch (Exception ex) {
//      ex.printStackTrace();
//    }
  }

    public static void SaveNewDataInOWL(Bug oBug) {
        
//         try {
//
//            OntModel model = ModelFactory.createOntologyModel();
//            Resource r;
//            ResIterator resourceIter;
// 
//             model.read(MetadataConstants.sLocationFile);  
//             String NS = MetadataConstants.sOWLrdf;
//             OntClass place = model.getOntClass( NS + "Property" );
//             String anchor = "rdf:Bug#";
//
//             Resource S2 = model.createResource( anchor + oBug.GetName() );
//             Resource S = model.createResource( anchor + oBug.GetName() );
//             Property P = model.createProperty( anchor + oBug.GetName());
//             RDFNode O = model.createLiteral( anchor + oBug.GetName() );
//             Statement SPO = model.createStatement( S, P, O );
//             Statement SPO2 = model.createStatement( S2, P, O );
//
//
//       File destinationFile = new File(MetadataConstants.sLocation);
//       FileOutputStream fosOWL;
//
//       fosOWL = new FileOutputStream(destinationFile,false);
//
//       model.write(fosOWL ,"RDF/XML-ABBREV");
//
//       fosOWL.flush();
//
//       fosOWL.close();
//
//
//        } catch (Exception ex) {
//      ex.printStackTrace();
//        }
    }

    public static void SaveNewDataInOWL1(Bug oBug) {
        
//         try {
//             
//        
//        Document dDoc = MetadataCommunicator.LoadXML(MetadataConstants.sLocation);
//        
//        
//        Node nBug = dDoc.createElement(oBug.GetName());
//        nBug.setTextContent(oBug.GetName());
//        String sNS = "owl:Class rdf:ID=\"User\"";
//        String sNS1 = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
//        Node x = dDoc.getElementsByTagName(sNS).item(0);//doc.getElementsByTagName("owl:Class").item(0);//doc.getElementsByTagName(sNS).item(0);//doc.getElementsByTagNameNS(sNS, "ID").item(0);//getElementsByTagName("owl:Class").item(0);
//        NodeList x1 = dDoc.getElementsByTagNameNS(sNS1, "relatedIssues");//doc.getElementsByTagName("owl:Class").item(0);//doc.getElementsByTagName(sNS).item(0);//doc.getElementsByTagNameNS(sNS, "ID").item(0);//getElementsByTagName("owl:Class").item(0);
//        Node x2 = dDoc.getElementsByTagNameNS(sNS1, "*").item(0);//doc.getElementsByTagName("owl:Class").item(0);//doc.getElementsByTagName(sNS).item(0);//doc.getElementsByTagNameNS(sNS, "ID").item(0);//getElementsByTagName("owl:Class").item(0);
//
//        for(int i = 0; i < x1.getLength(); i++) 
//            {
//                Node childNode = x1.item(i);
//                System.out.println("data in child number "+
//                    i+": "+childNode.getTextContent());
//            }
// 
//        x.appendChild(nBug);
// 
//
//
//       Transformer transformer = TransformerFactory.newInstance().newTransformer();
//       transformer.setOutputProperty(OutputKeys.INDENT, "yes");
//
//       //initialize StreamResult with File object to save to file
//        DOMSource source = new DOMSource(dDoc);
//         StreamResult result =  new StreamResult(new File(MetadataConstants.sLocation));
//
//        transformer.transform(source, result);
//
//         } catch (Exception ex) {
//            ex.printStackTrace();
//  
//         }
    }
    
    public static void RemoveOWLClass()
    {
//        try {
//             String owlBaseFile = MetadataConstants.sLocationFile;
//
//              OntModel model = ModelFactory.createOntologyModel();
//              Resource r;
//              ResIterator resourceIter;
// 
//              model.read(owlBaseFile); 
//    
//            String NS = MetadataConstants.sOWLrdf;
//            OntClass place = model.getOntClass( NS + "Property" );
//
//            place.remove();
//
//            File destinationFile = new File(MetadataConstants.sLocation);
//            FileOutputStream fosRemove;
//
//            fosRemove = new FileOutputStream(destinationFile,false);
//
//            model.write(fosRemove ,"RDF/XML-ABBREV");
//
//            fosRemove.flush();
//
//            fosRemove.close();
//           } catch (Exception ex) {
//      ex.printStackTrace();
//    }
  } 
    
    public static void SearchForIDs(String sSearchType, ArrayList<String> sIDs)  throws FileNotFoundException, IOException {
//        try {
//            
//         OntModel ontologyModel = LoadOWL();
//         ArrayList arCollection = new ArrayList();
//         
//         for (int i=0;i<sIDs.size();i++)
//         {
//        
//        String queryString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +
//                "PREFIX foaf: <http://xmlns.com/foaf/0.1/>" + 
//               "SELECT ?name" +
//                              "WHERE {" +
//                 "?person rdf:hasCommitter <http://www.alert-project.eu/ontologies> . ";
//        
//         // Execute the query and obtain results
//        QueryExecution qe = QueryExecutionFactory.create(queryString, ontologyModel);
//        ArrayList arPersons = new ArrayList();
//        ResultSet results = qe.execSelect() ;
//        arPersons.add(sIDs.get(i));
//        for ( ; results.hasNext() ; )
//        {
//            QuerySolution soln = results.nextSolution() ;
//            Literal lPerson = soln.getLiteral("person") ;   // Get a result variable - must be a literal
//            arPersons.add(lPerson);
//        }
//        
//          qe.close();
//           arCollection.add(arPersons);
//         }
//        
//        MetadataModel.ReturnForIDs(sSearchType,arCollection);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//  
//         }
    }

    

    

}
