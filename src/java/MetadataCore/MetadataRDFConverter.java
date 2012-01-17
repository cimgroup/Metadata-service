/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MetadataCore;

import MetadataCore.MetadataGlobal.OntoProperty;
import MetadataObjects.*;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.ontology.AnnotationProperty;
import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.ontology.impl.AnnotationPropertyImpl;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.Class;
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
     * @finalModification Sasa Stojanovic 16.01.2012.
     * @param oIssue 
     */
    public static Issue SaveIssue(Issue oIssue)
    {
        try {
            
            OntModel oModel = MetadataGlobal.LoadOWL(MetadataConstants.sLocationLoadAlert);
            
            oIssue.m_sObjectURI = MetadataGlobal.GetObjectURI(oModel, MetadataConstants.c_NS_Alert_Its + MetadataConstants.c_OWLClass_Bug, oIssue.m_sID);
            Resource resBug = oModel.getResource(oIssue.m_sObjectURI);
            oIssue.m_sReturnConfig = "YY#s:" + MetadataConstants.c_XMLE_issue + "/s:" + MetadataConstants.c_XMLE_issue + MetadataConstants.c_XMLE_Uri;
            
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
                DatatypeProperty dtpBugOpend = oModel.getDatatypeProperty(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLDataProperty_DateOpened);
                resBug.removeAll(dtpBugOpend);
                resBug.addProperty(dtpBugOpend, oIssue.m_dtmDateOpened.toString());
            }
            
            //Description
            if (!oIssue.m_sDescription.isEmpty())
            {
                DatatypeProperty dtpBugDescription = oModel.getDatatypeProperty(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLDataProperty_Description);
                resBug.removeAll(dtpBugDescription);
                resBug.addProperty(dtpBugDescription, oIssue.m_sDescription);
            }
            
            //Keyword
            if (!oIssue.m_sKeyword.isEmpty())
            {
                DatatypeProperty dtpBugKeyword = oModel.getDatatypeProperty(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLDataProperty_Keyword);
                resBug.removeAll(dtpBugKeyword);
                resBug.addProperty(dtpBugKeyword, oIssue.m_sKeyword);
            }
            
            //Last Modified
            if (oIssue.m_dtmLastModified != null)
            {
                DatatypeProperty dtpBugLastModified = oModel.getDatatypeProperty(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLDataProperty_LastModified);
                resBug.removeAll(dtpBugLastModified);
                resBug.addProperty(dtpBugLastModified, oIssue.m_dtmLastModified.toString());
            }
            
            //Number
            if (oIssue.m_iNumber != null)
            {
                DatatypeProperty dtpBugNumber = oModel.getDatatypeProperty(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLDataProperty_Number);
                resBug.removeAll(dtpBugNumber);
                resBug.addProperty(dtpBugNumber, oIssue.m_iNumber.toString());
            }
            
            //Blocks
            if (oIssue.m_oBlocks != null)
            {
                ObjectProperty opBlocks = oModel.getObjectProperty(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLObjectProperty_Blocks);
                for (int i = 0; i < oIssue.m_oBlocks.length; i++)
                {
                    if (oIssue.m_oBlocks[i] != null && !oIssue.m_oBlocks[i].m_sID.isEmpty())
                    {
                        if (!oIssue.m_oBlocks[i].m_bRemoved)
                        {
                            oIssue.m_oBlocks[i].m_sObjectURI = MetadataGlobal.GetObjectURI(oModel, MetadataConstants.c_NS_Alert_Its + MetadataConstants.c_OWLClass_Bug, oIssue.m_oBlocks[i].m_sID);
                            Resource resBlocks = oModel.getResource(oIssue.m_oBlocks[i].m_sObjectURI);
                            resBug.addProperty(opBlocks, resBlocks.asResource());
                            oIssue.m_oBlocks[i].m_sReturnConfig = "YN#s:" + MetadataConstants.c_XMLE_issueBlocks + MetadataConstants.c_XMLE_Uri;
                        }
                        else
                        {
                            String sBlocksRemoveURI = MetadataGlobal.GetObjectURI(oModel, MetadataConstants.c_NS_Alert_Its + MetadataConstants.c_OWLClass_Bug, oIssue.m_oBlocks[i].m_sID);
                            Resource resBlocksRemove = oModel.getResource(sBlocksRemoveURI);
                            oModel.removeAll(resBug, opBlocks, resBlocksRemove);
                        }
                    }
                }
            }

            //DependsOn
            if (oIssue.m_oDependsOn != null)
            {
                ObjectProperty opDependsOn = oModel.getObjectProperty(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLObjectProperty_DependsOn);
                for (int i = 0; i < oIssue.m_oDependsOn.length; i++)
                {
                    if (oIssue.m_oDependsOn[i] != null && !oIssue.m_oDependsOn[i].m_sID.isEmpty())
                    {
                        if (!oIssue.m_oDependsOn[i].m_bRemoved)
                        {
                            oIssue.m_oDependsOn[i].m_sObjectURI = MetadataGlobal.GetObjectURI(oModel, MetadataConstants.c_NS_Alert_Its + MetadataConstants.c_OWLClass_Bug, oIssue.m_oDependsOn[i].m_sID);
                            Resource resDependsOn = oModel.getResource(oIssue.m_oDependsOn[i].m_sObjectURI);
                            resBug.addProperty(opDependsOn, resDependsOn.asResource());
                            oIssue.m_oDependsOn[i].m_sReturnConfig = "YN#s:" + MetadataConstants.c_XMLE_issueDependsOn + MetadataConstants.c_XMLE_Uri;
                        }
                        else
                        {
                            String sDependsOnRemoveURI = MetadataGlobal.GetObjectURI(oModel, MetadataConstants.c_NS_Alert_Its + MetadataConstants.c_OWLClass_Bug, oIssue.m_oDependsOn[i].m_sID);
                            Resource resDependsOnRemove = oModel.getResource(sDependsOnRemoveURI);
                            oModel.removeAll(resBug, opDependsOn, resDependsOnRemove);
                        }
                    }
                }
            }
            
            //HasReporter
            if (oIssue.m_oHasReporter != null && !oIssue.m_oHasReporter.m_sID.isEmpty())
            {
                SavePersonData(oIssue.m_oHasReporter, oModel);
                ObjectProperty opHasReporter = oModel.getObjectProperty(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLObjectProperty_HasReporter);
                Resource resHasReporter = oModel.getResource(oIssue.m_oHasReporter.m_sObjectURI);
                resBug.removeAll(opHasReporter);
                resBug.addProperty(opHasReporter, resHasReporter.asResource());
                oIssue.m_oHasReporter.m_sReturnConfig = "YN#s:" + MetadataConstants.c_XMLE_issueAuthor + MetadataConstants.c_XMLE_Uri;
            }

            //HasState
            if (oIssue.m_oHasState != null)
            {
                ObjectProperty opHasState = oModel.getObjectProperty(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLObjectProperty_HasState);
                resBug.removeAll(opHasState);
                
                Class clsHasState = oIssue.m_oHasState.getClass();

                Class clsAssigned = Assigned.class;
                Class clsOpen= Open.class;
                Class clsVerified = Verified.class;
                Class clsResolved = Resolved.class;
                Class clsClosed = Closed.class;

                if (clsHasState.equals(clsAssigned))
                {
                    OntClass ocHasState = oModel.getOntClass(MetadataConstants.c_NS_Alert_Its + MetadataConstants.c_OWLClass_Assigned);
                    resBug.addProperty(opHasState, ocHasState.asResource());
                }
                if (clsHasState.equals(clsOpen))
                {
                    OntClass ocHasState = oModel.getOntClass(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLClass_Open);
                    resBug.addProperty(opHasState, ocHasState.asResource());
                }
                if (clsHasState.equals(clsVerified))
                {
                    OntClass ocHasState = oModel.getOntClass(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLClass_Verified);
                    resBug.addProperty(opHasState, ocHasState.asResource());
                }
                if (clsHasState.equals(clsResolved))
                {
                    OntClass ocHasState = oModel.getOntClass(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLClass_Resolved);
                    resBug.addProperty(opHasState, ocHasState.asResource());
                }
                if (clsHasState.equals(clsClosed))
                {
                    OntClass ocHasState = oModel.getOntClass(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLClass_Closed);
                    resBug.addProperty(opHasState, ocHasState.asResource());
                }
            }
            //end has state
            
            
            //HasResolution
            if (oIssue.m_oHasResolution != null)
            {
                ObjectProperty opHasResolution = oModel.getObjectProperty(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLObjectProperty_HasResolution);
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
                    OntClass ocHasResolution = oModel.getOntClass(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLClass_Duplicate);
                    resBug.addProperty(opHasResolution, ocHasResolution.asResource());
                }
                if (clsHasResolution.equals(clsFixed))
                {
                    OntClass ocHasResolution = oModel.getOntClass(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLClass_Fixed);
                    resBug.addProperty(opHasResolution, ocHasResolution.asResource());
                }
                if (clsHasResolution.equals(clsInvalid))
                {
                    OntClass ocHasResolution = oModel.getOntClass(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLClass_Invalid);
                    resBug.addProperty(opHasResolution, ocHasResolution.asResource());
                }
                if (clsHasResolution.equals(clsThirdParty))
                {
                    OntClass ocHasResolution = oModel.getOntClass(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLClass_ThirdParty);
                    resBug.addProperty(opHasResolution, ocHasResolution.asResource());
                }
                if (clsHasResolution.equals(clsWontFix))
                {
                    OntClass ocHasResolution = oModel.getOntClass(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLClass_WontFix);
                    resBug.addProperty(opHasResolution, ocHasResolution.asResource());
                }
                if (clsHasResolution.equals(clsWorksForMe))
                {
                    OntClass ocHasResolution = oModel.getOntClass(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLClass_WorksForMe);
                    resBug.addProperty(opHasResolution, ocHasResolution.asResource());
                }
                if (clsHasResolution.equals(clsLater))
                {
                    OntClass ocHasResolution = oModel.getOntClass(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLClass_Later);
                    resBug.addProperty(opHasResolution, ocHasResolution.asResource());
                }
                if (clsHasResolution.equals(clsRemind))
                {
                    OntClass ocHasResolution = oModel.getOntClass(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLClass_Remind);
                    resBug.addProperty(opHasResolution, ocHasResolution.asResource());
                }
            }
            //end has resolution
            
            //Issue product
            if (oIssue.m_oIsIssueOf != null)
            {
                oIssue.m_oIsIssueOf.m_sObjectURI = MetadataGlobal.GetObjectURI(oModel, MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLClass_Component, oIssue.m_oIsIssueOf.m_sID);
                ObjectProperty opIsIssueOf = oModel.getObjectProperty(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLObjectProperty_IsIssueOf);
                Resource resIsIssueOf = oModel.getResource(oIssue.m_oIsIssueOf.m_sObjectURI);
                
                if (oIssue.m_oIsIssueOf.m_oIsComponentOf != null)
                {
                    oIssue.m_oIsIssueOf.m_oIsComponentOf.m_sObjectURI = MetadataGlobal.GetObjectURI(oModel, MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLClass_Product, oIssue.m_oIsIssueOf.m_oIsComponentOf.m_sID);
                    ObjectProperty opIsComponentOf = oModel.getObjectProperty(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLObjectProperty_IsComponentOf);
                    Resource resIsComponentOf = oModel.getResource(oIssue.m_oIsIssueOf.m_oIsComponentOf.m_sObjectURI);
                     
                    if (!oIssue.m_oIsIssueOf.m_oIsComponentOf.m_sVersion.isEmpty())
                    {
                        DatatypeProperty dtpVersion = oModel.getDatatypeProperty(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLDataProperty_Version);
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
                oIssue.m_oHasComputerSystem.m_sObjectURI = MetadataGlobal.GetObjectURI(oModel, MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLClass_ComputerSystem, oIssue.m_oHasComputerSystem.m_sID);
                ObjectProperty opHasComputerSystem = oModel.getObjectProperty(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLObjectProperty_HasComputerSystem);
                Resource resComputerSystem = oModel.getResource(oIssue.m_oHasComputerSystem.m_sObjectURI);
                if (!oIssue.m_oHasComputerSystem.m_sPlatform.isEmpty())
                {
                    DatatypeProperty dtpPlatform = oModel.getDatatypeProperty(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLDataProperty_Platform);
                    resComputerSystem.removeAll(dtpPlatform);
                    resComputerSystem.addProperty(dtpPlatform, oIssue.m_oHasComputerSystem.m_sPlatform);
                }
                if (!oIssue.m_oHasComputerSystem.m_sOs.isEmpty())
                {
                    DatatypeProperty dtpOs = oModel.getDatatypeProperty(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLDataProperty_Os);
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
                ObjectProperty opHasPriority = oModel.getObjectProperty(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLObjectProperty_HasPriority);
                resBug.removeAll(opHasPriority);
                
                if(oIssue.m_oHasPriority.m_iPriority == 1)
                {
                    OntClass ocHasPriority = oModel.getOntClass(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLClass_P1);
                    resBug.addProperty(opHasPriority, ocHasPriority.asResource());
                }
                if(oIssue.m_oHasPriority.m_iPriority == 2)
                {
                    OntClass ocHasPriority = oModel.getOntClass(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLClass_P2);
                    resBug.addProperty(opHasPriority, ocHasPriority.asResource());
                }
                if(oIssue.m_oHasPriority.m_iPriority == 3)
                {
                    OntClass ocHasPriority = oModel.getOntClass(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLClass_P3);
                    resBug.addProperty(opHasPriority, ocHasPriority.asResource());
                }
                if(oIssue.m_oHasPriority.m_iPriority == 4)
                {
                    OntClass ocHasPriority = oModel.getOntClass(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLClass_P4);
                    resBug.addProperty(opHasPriority, ocHasPriority.asResource());
                }
                if(oIssue.m_oHasPriority.m_iPriority == 5)
                {
                    OntClass ocHasPriority = oModel.getOntClass(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLClass_P5);
                    resBug.addProperty(opHasPriority, ocHasPriority.asResource());
                }
            }
            //end has priority
            
            //HasSeverity
            if (oIssue.m_oHasSeverity != null)
            {
                ObjectProperty opHasSeverity = oModel.getObjectProperty(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLObjectProperty_HasSeverity);
                resBug.removeAll(opHasSeverity);
                
                Class clsHasSeverity = oIssue.m_oHasSeverity.getClass();

                Class clsBlocker = Blocker.class;
                Class clsCritical= Critical.class;
                Class clsMajor = Major.class;
                Class clsMinor = Minor.class;
                Class clsTrivial = Trivial.class;

                if (clsHasSeverity.equals(clsBlocker))
                {
                    OntClass ocHasSeverity = oModel.getOntClass(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLClass_Blocker);
                    resBug.addProperty(opHasSeverity, ocHasSeverity.asResource());
                }
                if (clsHasSeverity.equals(clsCritical))
                {
                    OntClass ocHasSeverity = oModel.getOntClass(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLClass_Critical);
                    resBug.addProperty(opHasSeverity, ocHasSeverity.asResource());
                }
                if (clsHasSeverity.equals(clsMajor))
                {
                    OntClass ocHasSeverity = oModel.getOntClass(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLClass_Major);
                    resBug.addProperty(opHasSeverity, ocHasSeverity.asResource());
                }
                if (clsHasSeverity.equals(clsMinor))
                {
                    OntClass ocHasSeverity = oModel.getOntClass(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLClass_Minor);
                    resBug.addProperty(opHasSeverity, ocHasSeverity.asResource());
                }
                if (clsHasSeverity.equals(clsTrivial))
                {
                    OntClass ocHasSeverity = oModel.getOntClass(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLClass_Trivial);
                    resBug.addProperty(opHasSeverity, ocHasSeverity.asResource());
                }
            }
            //end has severity
                                  
            //HasAssignee
            if (oIssue.m_oHasAssignee != null && !oIssue.m_oHasAssignee.m_sID.isEmpty())
            {
                SavePersonData(oIssue.m_oHasAssignee, oModel);
                ObjectProperty opHasAssignee = oModel.getObjectProperty(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLObjectProperty_HasAssignee);
                Resource resHasAssignee = oModel.getResource(oIssue.m_oHasAssignee.m_sObjectURI);
                resBug.removeAll(opHasAssignee);
                resBug.addProperty(opHasAssignee, resHasAssignee.asResource());
                oIssue.m_oHasAssignee.m_sReturnConfig = "YN#s:" + MetadataConstants.c_XMLE_issueAssignedTo + MetadataConstants.c_XMLE_Uri;
            }
            
            //HasCCPerson
            if (oIssue.m_oHasCCPerson != null)
            {
                ObjectProperty opHasCCPerson = oModel.getObjectProperty(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLObjectProperty_HasCCPerson);
                for (int i = 0; i < oIssue.m_oHasCCPerson.length; i++)
                {
                    if (oIssue.m_oHasCCPerson[i] != null && !oIssue.m_oHasCCPerson[i].m_sID.isEmpty())
                    {
                        if (!oIssue.m_oHasCCPerson[i].m_bRemoved)
                        {
                            SavePersonData(oIssue.m_oHasCCPerson[i], oModel);
                            Resource resHasCCPerson = oModel.getResource(oIssue.m_oHasCCPerson[i].m_sObjectURI);
                            resBug.addProperty(opHasCCPerson, resHasCCPerson.asResource());
                            oIssue.m_oHasCCPerson[i].m_sReturnConfig = "YN#s:" + MetadataConstants.c_XMLE_issueCCPerson + MetadataConstants.c_XMLE_Uri;
                        }
                        else
                        {
                            String sCCPersonRemoveURI = MetadataGlobal.GetObjectURI(oModel, MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLClass_Person, oIssue.m_oHasCCPerson[i].m_sID);
                            Resource resCCPersonRemove = oModel.getResource(sCCPersonRemoveURI);
                            oModel.removeAll(resBug, opHasCCPerson, resCCPersonRemove);
                        }
                    }
                }
            }
                       
            //IsDuplicateOf
            if (!oIssue.m_oIsDuplicateOf.m_sID.isEmpty())
            {
                oIssue.m_oIsDuplicateOf.m_sObjectURI = MetadataGlobal.GetObjectURI(oModel, MetadataConstants.c_NS_Alert_Its + MetadataConstants.c_OWLClass_Bug, oIssue.m_oIsDuplicateOf.m_sID);
                ObjectProperty opIsDuplicateOf = oModel.getObjectProperty(MetadataConstants.c_NS_Alert_Its + MetadataConstants.c_OWLObjectProperty_IsDuplicateOf);
                Resource resIsDuplicateOf = oModel.getResource(oIssue.m_oIsDuplicateOf.m_sObjectURI);
                resBug.removeAll(opIsDuplicateOf);
                resBug.addProperty(opIsDuplicateOf, resIsDuplicateOf.asResource());
                oIssue.m_oIsDuplicateOf.m_sReturnConfig = "YN#s:" + MetadataConstants.c_XMLE_issueDuplicateOf + MetadataConstants.c_XMLE_Uri;
            }
            
            //IsMergedInto
            if (!oIssue.m_oIsMergedInto.m_sID.isEmpty())
            {
                oIssue.m_oIsMergedInto.m_sObjectURI = MetadataGlobal.GetObjectURI(oModel, MetadataConstants.c_NS_Alert_Its + MetadataConstants.c_OWLClass_Bug, oIssue.m_oIsMergedInto.m_sID);
                ObjectProperty opIsMergedInto = oModel.getObjectProperty(MetadataConstants.c_NS_Alert_Its + MetadataConstants.c_OWLObjectProperty_IsMergedInto);
                Resource resIsMergedInto = oModel.getResource(oIssue.m_oIsMergedInto.m_sObjectURI);
                resBug.removeAll(opIsMergedInto);
                resBug.addProperty(opIsMergedInto, resIsMergedInto.asResource());
                oIssue.m_oIsMergedInto.m_sReturnConfig = "YN#s:" + MetadataConstants.c_XMLE_issueMergedInto + MetadataConstants.c_XMLE_Uri;
            }
            
            //HasMilestone
            if (oIssue.m_oHasMilestone != null && (!oIssue.m_oHasMilestone.m_sID.isEmpty() || oIssue.m_oHasMilestone.m_dtmTarget != null))
            {
                oIssue.m_oHasMilestone.m_sObjectURI = MetadataGlobal.GetObjectURI(oModel, MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLClass_Milestone, oIssue.m_oHasMilestone.m_sID);
                ObjectProperty opHasMilestone = oModel.getObjectProperty(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLObjectProperty_HasMilestone);
                Resource resMilestone = oModel.getResource(oIssue.m_oHasMilestone.m_sObjectURI);
                if (oIssue.m_oHasMilestone.m_dtmTarget != null)
                {
                    DatatypeProperty dtpMilestone= oModel.getDatatypeProperty(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLDataProperty_Target);
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
                ObjectProperty opHasComment = oModel.getObjectProperty(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLObjectProperty_HasComment);
                for (int i = 0; i < oIssue.m_oHasComment.length; i++)
                {
                    if (oIssue.m_oHasComment[i] != null && (!oIssue.m_oHasComment[i].m_sID.isEmpty() || !oIssue.m_oHasComment[i].m_sText.isEmpty()))
                    {
                        oIssue.m_oHasComment[i].m_sObjectURI = MetadataGlobal.GetObjectURI(oModel, MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLClass_Comment, oIssue.m_oHasComment[i].m_sID);
                        Resource resComment = oModel.getResource(oIssue.m_oHasComment[i].m_sObjectURI);

                        if (oIssue.m_oHasComment[i].m_iNumber != null)
                        {
                            DatatypeProperty dtpCommentNumber= oModel.getDatatypeProperty(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLDataProperty_Number);
                            resComment.removeAll(dtpCommentNumber);
                            resComment.addProperty(dtpCommentNumber, oIssue.m_oHasComment[i].m_iNumber.toString());
                        }

                        if (oIssue.m_oHasComment[i].m_dtmDate != null)
                        {
                            DatatypeProperty dtpCommentDate= oModel.getDatatypeProperty( MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLDataProperty_Date );
                            resComment.removeAll(dtpCommentDate);
                            resComment.addProperty(dtpCommentDate, oIssue.m_oHasComment[i].m_dtmDate.toString());
                        }

                        if (!oIssue.m_oHasComment[i].m_sText.isEmpty())
                        {
                            DatatypeProperty dtpCommentText= oModel.getDatatypeProperty(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLDataProperty_Text);
                            resComment.removeAll(dtpCommentText);
                            resComment.addProperty(dtpCommentText, oIssue.m_oHasComment[i].m_sText);
                        }

                        if (oIssue.m_oHasComment[i].m_oHasCommentor != null && oIssue.m_oHasComment[i].m_oHasCommentor.m_sID != null && !oIssue.m_oHasComment[i].m_oHasCommentor.m_sID.isEmpty())
                        {
                            //oIssue.m_oHasComment[i].m_oHasCommentor.m_sObjectURI = MetadataGlobal.GetObjectURI(oModel, MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLClass_Person, oIssue.m_oHasComment[i].m_oHasCommentor.m_sID);
                            SavePersonData(oIssue.m_oHasComment[i].m_oHasCommentor, oModel);
                            ObjectProperty opHasCommentor = oModel.getObjectProperty(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLObjectProperty_HasCommentor);
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
                ObjectProperty opHasAttachment = oModel.getObjectProperty(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLObjectProperty_HasAttachment);
                for (int i = 0; i < oIssue.m_oHasAttachment.length; i++)
                {
                    if (oIssue.m_oHasAttachment[i] != null && (!oIssue.m_oHasAttachment[i].m_sID.isEmpty() || !oIssue.m_oHasAttachment[i].m_sFilename.isEmpty()))
                    {
                        oIssue.m_oHasAttachment[i].m_sObjectURI = MetadataGlobal.GetObjectURI(oModel, MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLClass_Attachment, oIssue.m_oHasAttachment[i].m_sID);
                        Resource resAttachment = oModel.getResource(oIssue.m_oHasAttachment[i].m_sObjectURI);

                        if (!oIssue.m_oHasAttachment[i].m_sFilename.isEmpty())
                        {
                            DatatypeProperty dtpAttachmentFileName= oModel.getDatatypeProperty(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLDataProperty_FileName);
                            resAttachment.removeAll(dtpAttachmentFileName);
                            resAttachment.addProperty(dtpAttachmentFileName, oIssue.m_oHasAttachment[i].m_sFilename);
                        }

                        if (!oIssue.m_oHasAttachment[i].m_sType.isEmpty())
                        {
                            DatatypeProperty dtpAttachmentType= oModel.getDatatypeProperty(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLDataProperty_Type);
                            resAttachment.removeAll(dtpAttachmentType);
                            resAttachment.addProperty(dtpAttachmentType, oIssue.m_oHasAttachment[i].m_sType);
                        }

                        if (oIssue.m_oHasAttachment[i].m_oHasCreator != null && oIssue.m_oHasAttachment[i].m_oHasCreator.m_sID != null && !oIssue.m_oHasAttachment[i].m_oHasCreator.m_sID.isEmpty())
                        {
                            //oIssue.m_oHasAttachment[i].m_oHasCreator.m_sObjectURI = MetadataGlobal.GetObjectURI(oModel, MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLClass_Person, oIssue.m_oHasAttachment[i].m_oHasCreator.m_sID);
                            SavePersonData(oIssue.m_oHasAttachment[i].m_oHasCreator, oModel);
                            ObjectProperty opHasCreator = oModel.getObjectProperty(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLObjectProperty_HasCreator);
                            Resource resHasCreator = oModel.getResource(oIssue.m_oHasAttachment[i].m_oHasCreator.m_sObjectURI);
                            resAttachment.removeAll(opHasCreator);
                            resAttachment.addProperty(opHasCreator, resHasCreator.asResource());
                            oIssue.m_oHasAttachment[i].m_oHasCreator.m_sReturnConfig = "YN#s:" + MetadataConstants.c_XMLE_attachmentCreator + MetadataConstants.c_XMLE_Uri;
                        }

                        resBug.addProperty(opHasAttachment, resAttachment.asResource());
                        oIssue.m_oHasAttachment[i].m_sReturnConfig = "YY#s:" + MetadataConstants.c_XMLE_issueAttachment + "/s:" + MetadataConstants.c_XMLE_attachment + MetadataConstants.c_XMLE_Uri;
                    }
                }
            }
            //end has attachment 
            
            //HasActivity
            if (oIssue.m_oHasActivity != null)
            {
                ObjectProperty opHasActivity = oModel.getObjectProperty(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLObjectProperty_HasActivity);
                for (int i = 0; i < oIssue.m_oHasActivity.length; i++)
                {
                    if (oIssue.m_oHasActivity[i] != null && (!oIssue.m_oHasActivity[i].m_sID.isEmpty() || oIssue.m_oHasActivity[i].m_oHasInvolvedPerson != null))
                    {
                        oIssue.m_oHasActivity[i].m_sObjectURI = MetadataGlobal.GetObjectURI(oModel, MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLClass_Activity, "");
                        Resource resActivity = oModel.getResource(oIssue.m_oHasActivity[i].m_sObjectURI);

                        //adding ID to activitiy (it wasn't added with GetObjectURI method because two or more activities can have the same id)
                        DatatypeProperty dtpId = oModel.getDatatypeProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_ID);
                        resActivity.removeAll(dtpId);
                        resActivity.addProperty(dtpId, oIssue.m_oHasActivity[i].m_sID);

                        if (!oIssue.m_oHasActivity[i].m_sWhat.isEmpty())
                        {
                            DatatypeProperty dtpActivityWhat = oModel.getDatatypeProperty(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLDataProperty_What);
                            resActivity.removeAll(dtpActivityWhat);
                            resActivity.addProperty(dtpActivityWhat, oIssue.m_oHasActivity[i].m_sWhat);
                        }
                        
                        if (!oIssue.m_oHasActivity[i].m_sRemoved.isEmpty())
                        {
                            DatatypeProperty dtpActivityRemoved = oModel.getDatatypeProperty(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLDataProperty_Removed);
                            resActivity.removeAll(dtpActivityRemoved);
                            resActivity.addProperty(dtpActivityRemoved, oIssue.m_oHasActivity[i].m_sRemoved);
                        }
                        
                        if (!oIssue.m_oHasActivity[i].m_sAdded.isEmpty())
                        {
                            DatatypeProperty dtpActivityAdded = oModel.getDatatypeProperty(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLDataProperty_Added);
                            resActivity.removeAll(dtpActivityAdded);
                            resActivity.addProperty(dtpActivityAdded, oIssue.m_oHasActivity[i].m_sAdded);
                        }
                        
                        if (oIssue.m_oHasActivity[i].m_dtmWhen != null)
                        {
                            DatatypeProperty dtpActivityPerformed = oModel.getDatatypeProperty(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLDataProperty_Performed);
                            resActivity.removeAll(dtpActivityPerformed);
                            resActivity.addProperty(dtpActivityPerformed, oIssue.m_oHasActivity[i].m_dtmWhen.toString());
                        }
                        
                        if (oIssue.m_oHasActivity[i].m_oHasInvolvedPerson != null && oIssue.m_oHasActivity[i].m_oHasInvolvedPerson.m_sID != null && !oIssue.m_oHasActivity[i].m_oHasInvolvedPerson.m_sID.isEmpty())
                        {
                            oIssue.m_oHasActivity[i].m_oHasInvolvedPerson.m_sObjectURI = MetadataGlobal.GetObjectURI(oModel, MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLClass_Person, oIssue.m_oHasActivity[i].m_oHasInvolvedPerson.m_sID);
                            ObjectProperty opHasInvolvedPerson = oModel.getObjectProperty(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLObjectProperty_HasInvolvedPerson);
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
            
        }
        catch (Exception ex)
        {
        }
        return oIssue;
    }
    
    /**
     * @summary Save issue data
     * @startRealisation Sasa Stojanovic 16.01.2012.
     * @finalModification Sasa Stojanovic 16.01.2012.
     * @param oCommit 
     */
    public static Commit SaveCommit(Commit oCommit)
    {
        try {
            
            OntModel oModel = MetadataGlobal.LoadOWL(MetadataConstants.sLocationLoadAlert);
            
            oCommit.m_sObjectURI = MetadataGlobal.GetObjectURI(oModel, MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLClass_Commit, oCommit.m_sID);
            Resource resCommit = oModel.getResource(oCommit.m_sObjectURI);
            oCommit.m_sReturnConfig = "YY#s:" + MetadataConstants.c_XMLE_commit + "/s:" + MetadataConstants.c_XMLE_commit + MetadataConstants.c_XMLE_Uri;
            
            //commitRepository
            if (oCommit.m_oIsCommitOfRepository != null && !oCommit.m_oIsCommitOfRepository.m_sObjectURI.isEmpty())
            {
                ObjectProperty opIsCommitOfRepository = oModel.getObjectProperty(MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLObjectProperty_IsCommitOfRepository);
                Resource resIsCommitOfRepository = oModel.getResource(oCommit.m_oIsCommitOfRepository.m_sObjectURI);
                resCommit.removeAll(opIsCommitOfRepository);
                resCommit.addProperty(opIsCommitOfRepository, resIsCommitOfRepository.asResource());
                oCommit.m_oIsCommitOfRepository.m_sReturnConfig = "YN#s:" + MetadataConstants.c_XMLE_commitRepository + MetadataConstants.c_XMLE_Uri;
            }
            
            //revisionTag
            if (!oCommit.m_sRevisionTag.isEmpty())
            {
                DatatypeProperty dtpRevisionTag = oModel.getDatatypeProperty(MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLDataProperty_RevisionTag);
                resCommit.removeAll(dtpRevisionTag);
                resCommit.addProperty(dtpRevisionTag, oCommit.m_sRevisionTag);
            }
            
            //commitAuthor
            if (oCommit.m_oHasAuthor != null && !oCommit.m_oHasAuthor.m_sID.isEmpty())
            {
                SavePersonData(oCommit.m_oHasAuthor, oModel);
                ObjectProperty opHasAuthor = oModel.getObjectProperty(MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLObjectProperty_HasAuthor);
                Resource resHasAuthor = oModel.getResource(oCommit.m_oHasAuthor.m_sObjectURI);
                resCommit.removeAll(opHasAuthor);
                resCommit.addProperty(opHasAuthor, resHasAuthor.asResource());
                oCommit.m_oHasAuthor.m_sReturnConfig = "YN#s:" + MetadataConstants.c_XMLE_commitAuthor + MetadataConstants.c_XMLE_Uri;
            }
            
            //commitCommitter
            if (oCommit.m_oHasCommitter != null && !oCommit.m_oHasCommitter.m_sID.isEmpty())
            {
                SavePersonData(oCommit.m_oHasCommitter, oModel);
                ObjectProperty opHasCommitter = oModel.getObjectProperty(MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLObjectProperty_HasCommitter);
                Resource resHasCommitter = oModel.getResource(oCommit.m_oHasCommitter.m_sObjectURI);
                resCommit.removeAll(opHasCommitter);
                resCommit.addProperty(opHasCommitter, resHasCommitter.asResource());
                oCommit.m_oHasCommitter.m_sReturnConfig = "YN#s:" + MetadataConstants.c_XMLE_commitCommtter + MetadataConstants.c_XMLE_Uri;
            }
            
            //commitDate
            if (oCommit.m_dtmCommitDate != null)
            {
                DatatypeProperty dtpCommitDate = oModel.getDatatypeProperty(MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLDataProperty_CommitDate);
                resCommit.removeAll(dtpCommitDate);
                resCommit.addProperty(dtpCommitDate, oCommit.m_dtmCommitDate.toString());
            }
            
            //commitMessageLog
            if (!oCommit.m_sCommitMessage.isEmpty())
            {
                DatatypeProperty dtpCommitMessage = oModel.getDatatypeProperty(MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLDataProperty_CommitMessage);
                resCommit.removeAll(dtpCommitMessage);
                resCommit.addProperty(dtpCommitMessage, oCommit.m_sCommitMessage);
            }
            
            //save data
            MetadataGlobal.SaveOWL(oModel, MetadataConstants.sLocationSaveAlert);
            
        }
        catch (Exception ex)
        {
        }
        return oCommit;
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
            oPerson.m_sObjectURI = MetadataGlobal.GetObjectURI(oModel, MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLClass_Person, oPerson.m_sID);
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
            
            if (!oPerson.m_sEmail.isEmpty())
            {
                DatatypeProperty dtpEmail = oModel.getDatatypeProperty(MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLDataProperty_Email);
                resPerson.removeAll(dtpEmail);
                resPerson.addProperty(dtpEmail, oPerson.m_sEmail);
            }
        }
        catch (Exception e)
        {
        }
    }
    
    // <editor-fold desc="API Calls">
    
    /**
     * @summary issue_getAllForProduct
     * @startRealisation Sasa Stojanovic 13.12.2011.
     * @finalModification Sasa Stojanovic 13.12.2011.
     * @param sProductUri - product URI
     * @return - APIResponseData object with results
     */
    public static MetadataGlobal.APIResponseData ac_issue_getAllForProduct(String sProductUri)
    {
        MetadataGlobal.APIResponseData oData = new MetadataGlobal.APIResponseData();
        try
        {
            OntModel oModel = MetadataGlobal.LoadOWL(MetadataConstants.sLocationLoadAlert);
                        
            String sQuery = "SELECT ?issueUri ?issueId ?issueDescription WHERE "
                    + "{?issueUri <" + MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLObjectProperty_IsIssueOf + ">  ?componentUri ."
                    + " ?componentUri  <" + MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLObjectProperty_IsComponentOf + "> <" + sProductUri + "> ."
                    + " ?issueUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_ID + "> ?issueId ."
                    + " ?issueUri <" + MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLDataProperty_Description + "> ?issueDescription . }";

            ResultSet rsIssue = QueryExecutionFactory.create(sQuery, oModel).execSelect();
            
            while (rsIssue.hasNext())
            {
                QuerySolution qsIssue = rsIssue.nextSolution();
                
                MetadataGlobal.APIResponseData oIssue = new MetadataGlobal.APIResponseData();
                MetadataGlobal.APIResponseData oIssueUri = new MetadataGlobal.APIResponseData();
                MetadataGlobal.APIResponseData oIssueId = new MetadataGlobal.APIResponseData();
                MetadataGlobal.APIResponseData oIssueDescription = new MetadataGlobal.APIResponseData();
                
                oIssue.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_issue + "/";
                oIssueUri.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_issue + MetadataConstants.c_XMLE_Uri;
                oIssueId.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_issue + MetadataConstants.c_XMLE_Id;
                oIssueDescription.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_issueDescription;
                
                oIssueUri.sData = qsIssue.get("?issueUri").toString();
                oIssueId.sData = qsIssue.get("?issueId").toString();
                oIssueDescription.sData = qsIssue.get("?issueDescription").toString();
                
                oIssue.oData.add(oIssueUri);
                oIssue.oData.add(oIssueId);
                oIssue.oData.add(oIssueDescription);   
                oData.oData.add(oIssue);
            }

        }
        catch (Exception e)
        {
        }
        return oData;
    }
    
    /**
     * @summary issue_getAllForMethod
     * @startRealisation Sasa Stojanovic 14.12.2011.
     * @finalModification Sasa Stojanovic 14.12.2011.
     * @param sMethodUri - methods' URIs
     * @return - APIResponseData object with results
     */
    public static MetadataGlobal.APIResponseData ac_issue_getAllForMethod(ArrayList <String> sMethodUri)
    {
        MetadataGlobal.APIResponseData oData = new MetadataGlobal.APIResponseData();
        try
        {
            OntModel oModel = MetadataGlobal.LoadOWL(MetadataConstants.sLocationLoadAlert);
                        
            String sQuery = "SELECT ?issueUri WHERE {";
            for (int i = 0; i < sMethodUri.size(); i++)
            {
                if (i > 0)
                    sQuery += " UNION ";
                sQuery += "{?issueUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLObjectProperty_RelatedToSourceCode + "> <" + sMethodUri.get(i) + ">}";
            }
            sQuery += "}";
                
            ResultSet rsIssue = QueryExecutionFactory.create(sQuery, oModel).execSelect();
            
            while (rsIssue.hasNext())
            {
                QuerySolution qsIssue = rsIssue.nextSolution();
                
                MetadataGlobal.APIResponseData oIssueUri = new MetadataGlobal.APIResponseData();
                oIssueUri.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_issue + MetadataConstants.c_XMLE_Uri;
                oIssueUri.sData = qsIssue.get("?issueUri").toString();

                oData.oData.add(oIssueUri);
            }
        }
        catch (Exception e)
        {
        }
        return oData;
    }
    
    /**
     * @summary issue_getAnnotationStatus
     * @startRealisation Sasa Stojanovic 14.12.2011.
     * @finalModification Sasa Stojanovic 14.12.2011.
     * @param sIssueUri - issue URI
     * @return - APIResponseData object with results
     */
    public static MetadataGlobal.APIResponseData ac_issue_getAnnotationStatus(String sIssueUri)
    {
        MetadataGlobal.APIResponseData oData = new MetadataGlobal.APIResponseData();
        try
        {
            OntModel oModel = MetadataGlobal.LoadOWL(MetadataConstants.sLocationLoadAlert);
            OntResource resBug = oModel.getOntResource(sIssueUri);
            StmtIterator siProperties = resBug.listProperties();
            
            String sIssueAnnotationStatus = "false";
            while (siProperties.hasNext())
            {
                //if annotation property comment exists
                if (siProperties.next().getPredicate().getURI().equals(MetadataConstants.c_OWLAnnotationProperty_comment))
                {
                    sIssueAnnotationStatus = "true";
                    break;
                }
            }

            MetadataGlobal.APIResponseData oIssueAnnotationStatus = new MetadataGlobal.APIResponseData();
            oIssueAnnotationStatus.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_issueAnnotationStatus;
            oIssueAnnotationStatus.sData = sIssueAnnotationStatus;

            oData.oData.add(oIssueAnnotationStatus);

        }
        catch (Exception e)
        {
        }
        return oData;
    }
    
    /**
     * @summary issue_getAnnotationStatus
     * @startRealisation Sasa Stojanovic 14.12.2011.
     * @finalModification Sasa Stojanovic 14.12.2011.
     * @param sIssueUri - issue URI
     * @return - APIResponseData object with results
     */
    public static MetadataGlobal.APIResponseData ac_issue_getInfo(String sIssueUri)
    {
        MetadataGlobal.APIResponseData oData = new MetadataGlobal.APIResponseData();
        try
        {
            OntModel oModel = MetadataGlobal.LoadOWL(MetadataConstants.sLocationLoadAlert);
            OntResource resBug = oModel.getOntResource(sIssueUri);
            StmtIterator siProperties = resBug.listProperties();
            while (siProperties.hasNext())
            {
                Statement sStatement = siProperties.next();
                String sProperty = sStatement.getPredicate().getURI();
                
                MetadataGlobal.APIResponseData oIssueData = new MetadataGlobal.APIResponseData();
                
                if (sProperty.equals(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_ID))
                {
                    oIssueData.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_issue + MetadataConstants.c_XMLE_id;
                    oIssueData.sData = sStatement.getObject().toString();
                }
                if (sProperty.equals(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_URL))
                {
                    oIssueData.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_issueUrl;
                    oIssueData.sData = sStatement.getObject().toString();
                }
                if (sProperty.equals(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLDataProperty_DateOpened))
                {
                    oIssueData.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_issueDateOpened;
                    oIssueData.sData = sStatement.getObject().toString();
                }
                if (sProperty.equals(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLDataProperty_Description))
                {
                    oIssueData.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_issueDescription;
                    oIssueData.sData = sStatement.getObject().toString();
                }
                if (sProperty.equals(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLDataProperty_Keyword))
                {
                    oIssueData.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_issueKeyword;
                    oIssueData.sData = sStatement.getObject().toString();
                }
                if (sProperty.equals(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLDataProperty_LastModified))
                {
                    oIssueData.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_issueLastModified;
                    oIssueData.sData = sStatement.getObject().toString();
                }
                if (sProperty.equals(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLDataProperty_Number))
                {
                    oIssueData.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_issueNumber;
                    oIssueData.sData = sStatement.getObject().toString();
                }
                if (sProperty.equals(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLObjectProperty_Blocks))
                {
                    oIssueData.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_issueBlocks + MetadataConstants.c_XMLE_Uri;
                    oIssueData.sData = sStatement.getObject().toString();
                }
                if (sProperty.equals(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLObjectProperty_DependsOn))
                {
                    oIssueData.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_issueDependsOn + MetadataConstants.c_XMLE_Uri;
                    oIssueData.sData = sStatement.getObject().toString();
                }
                if (sProperty.equals(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLObjectProperty_HasReporter))
                {
                    oIssueData.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_issueAuthor + MetadataConstants.c_XMLE_Uri;
                    oIssueData.sData = sStatement.getObject().toString();
                }
                if (sProperty.equals(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLObjectProperty_HasState))
                {
                    oIssueData.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_issueStatus;
                    if (sStatement.getObject().toString().equals(MetadataConstants.c_NS_Alert_Its + MetadataConstants.c_OWLClass_Assigned))
                        oIssueData.sData = "Assigned";
                    if (sStatement.getObject().toString().equals(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLClass_Open))
                        oIssueData.sData = "Open";
                    if (sStatement.getObject().toString().equals(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLClass_Verified))
                        oIssueData.sData = "Verified";
                    if (sStatement.getObject().toString().equals(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLClass_Resolved))
                        oIssueData.sData = "Resolved";
                    if (sStatement.getObject().toString().equals(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLClass_Closed))
                        oIssueData.sData = "Closed";
                }
                if (sProperty.equals(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLObjectProperty_HasResolution))
                {
                    oIssueData.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_issueResolution;
                    if (sStatement.getObject().toString().equals(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLClass_Duplicate))
                        oIssueData.sData = "Duplicate";
                    if (sStatement.getObject().toString().equals(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLClass_Fixed))
                        oIssueData.sData = "Fixed";
                    if (sStatement.getObject().toString().equals(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLClass_Invalid))
                        oIssueData.sData = "Invalid";
                    if (sStatement.getObject().toString().equals(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLClass_ThirdParty))
                        oIssueData.sData = "ThirdParty";
                    if (sStatement.getObject().toString().equals(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLClass_WontFix))
                        oIssueData.sData = "WontFix";
                    if (sStatement.getObject().toString().equals(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLClass_WorksForMe))
                        oIssueData.sData = "WorksForMe";
                    if (sStatement.getObject().toString().equals(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLClass_Later))
                        oIssueData.sData = "Later";
                    if (sStatement.getObject().toString().equals(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLClass_Remind))
                        oIssueData.sData = "Remind";
                }
                if (sProperty.equals(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLObjectProperty_IsIssueOf))
                {
                    oIssueData.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_issueProduct + "/";
                    
                    MetadataGlobal.APIResponseData oProductUri = new MetadataGlobal.APIResponseData();
                    MetadataGlobal.APIResponseData oComponentUri = new MetadataGlobal.APIResponseData();
                    MetadataGlobal.APIResponseData oProductVersion = new MetadataGlobal.APIResponseData();
                    
                    oProductUri.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_product + MetadataConstants.c_XMLE_Uri;
                    oComponentUri.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_productComponent + MetadataConstants.c_XMLE_Uri;
                    oProductVersion.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_productVersion;
                    
                    oComponentUri.sData = sStatement.getObject().toString();
                    oIssueData.oData.add(oComponentUri);
                    
                    String sQuery = "SELECT ?productUri ?productVersion WHERE "
                    + "{<" + oComponentUri.sData + ">  <" + MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLObjectProperty_IsComponentOf + "> ?productUri ."
                    + " ?productUri <" + MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLDataProperty_Version + "> ?productVersion}";

                    ResultSet rsProduct = QueryExecutionFactory.create(sQuery, oModel).execSelect();

                    if (rsProduct.hasNext())
                    {
                        QuerySolution qsIssue = rsProduct.nextSolution();

                        oProductUri.sData = qsIssue.get("?productUri").toString();
                        oProductVersion.sData = qsIssue.get("?productVersion").toString();

                        oIssueData.oData.add(oProductUri);
                        oIssueData.oData.add(oProductVersion);
                    }
                }
                if (sProperty.equals(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLObjectProperty_HasComputerSystem))
                {
                    oIssueData.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_issueComputerSystem + "/";
                    
                    MetadataGlobal.APIResponseData oComputerSystemUri = new MetadataGlobal.APIResponseData();
                    MetadataGlobal.APIResponseData oComputerSystemPlatform = new MetadataGlobal.APIResponseData();
                    MetadataGlobal.APIResponseData oComputerSystemOS = new MetadataGlobal.APIResponseData();
                    
                    oComputerSystemUri.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_computerSystem + MetadataConstants.c_XMLE_Uri;
                    oComputerSystemPlatform.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_computerSystemPlatform;
                    oComputerSystemOS.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_computerSystemOS;
                    
                    oComputerSystemUri.sData = sStatement.getObject().toString();
                    oIssueData.oData.add(oComputerSystemUri);
                    
                    String sQuery = "SELECT ?computerSystemPlatform ?computerSystemOS WHERE "
                    + "{<" + oComputerSystemUri.sData + "> <" + MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLDataProperty_Platform + "> ?computerSystemPlatform ."
                    + " <" + oComputerSystemUri.sData + "> <" + MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLDataProperty_Os + "> ?computerSystemOS}";

                    ResultSet rsProduct = QueryExecutionFactory.create(sQuery, oModel).execSelect();

                    if (rsProduct.hasNext())
                    {
                        QuerySolution qsIssue = rsProduct.nextSolution();

                        oComputerSystemPlatform.sData = qsIssue.get("?computerSystemPlatform").toString();
                        oComputerSystemOS.sData = qsIssue.get("?computerSystemOS").toString();

                        oIssueData.oData.add(oComputerSystemPlatform);
                        oIssueData.oData.add(oComputerSystemOS);
                    }
                }
                if (sProperty.equals(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLObjectProperty_HasAssignee))
                {
                    oIssueData.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_issueAssignedTo + MetadataConstants.c_XMLE_Uri;
                    oIssueData.sData = sStatement.getObject().toString();
                }
                if (sProperty.equals(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLObjectProperty_HasCCPerson))
                {
                    oIssueData.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_issueCCPerson + MetadataConstants.c_XMLE_Uri;
                    oIssueData.sData = sStatement.getObject().toString();
                }
                if (sProperty.equals(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLObjectProperty_IsDuplicateOf))
                {
                    oIssueData.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_issueDuplicateOf + MetadataConstants.c_XMLE_Uri;
                    oIssueData.sData = sStatement.getObject().toString();
                }
                if (sProperty.equals(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLObjectProperty_IsMergedInto))
                {
                    oIssueData.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_issueMergedInto + MetadataConstants.c_XMLE_Uri;
                    oIssueData.sData = sStatement.getObject().toString();
                }
                if (sProperty.equals(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLObjectProperty_HasMilestone))
                {
                    oIssueData.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_issueMilestone + "/";
                    
                    MetadataGlobal.APIResponseData oMilestoneUri = new MetadataGlobal.APIResponseData();
                    MetadataGlobal.APIResponseData oMilestoneTarget = new MetadataGlobal.APIResponseData();
                    
                    oMilestoneUri.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_milestone + MetadataConstants.c_XMLE_Uri;
                    oMilestoneTarget.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_milestoneTarget;
                    
                    oMilestoneUri.sData = sStatement.getObject().toString();
                    oIssueData.oData.add(oMilestoneUri);
                    
                    String sQuery = "SELECT ?milestoneTarget WHERE "
                    + "{<" + oMilestoneUri.sData + "> <" + MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLDataProperty_Target + "> ?milestoneTarget}";

                    ResultSet rsProduct = QueryExecutionFactory.create(sQuery, oModel).execSelect();

                    if (rsProduct.hasNext())
                    {
                        QuerySolution qsIssue = rsProduct.nextSolution();

                        oMilestoneTarget.sData = qsIssue.get("?milestoneTarget").toString();
                        oIssueData.oData.add(oMilestoneTarget);
                    }
                }
                if (sProperty.equals(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLObjectProperty_HasComment))
                {
                    oIssueData.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_issueComment + MetadataConstants.c_XMLE_Uri;
                    oIssueData.sData = sStatement.getObject().toString();
                }
                if (sProperty.equals(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLObjectProperty_HasAttachment))
                {
                    oIssueData.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_issueAttachment + MetadataConstants.c_XMLE_Uri;
                    oIssueData.sData = sStatement.getObject().toString();
                }
                
                if (oIssueData.sReturnConfig != null)
                    oData.oData.add(oIssueData);
            }
        }
        catch (Exception e)
        {
        }
        return oData;
    }
    
    /**
     * @summary issue_getAllForMethod
     * @startRealisation Sasa Stojanovic 15.12.2011.
     * @finalModification Sasa Stojanovic 15.12.2011.
     * @param sMethodUri - methods' URIs
     * @return - APIResponseData object with results
     */
    public static MetadataGlobal.APIResponseData ac_issue_getDuplicates(String sIssueDuplicatesSPARQL)
    {
        MetadataGlobal.APIResponseData oData = new MetadataGlobal.APIResponseData();
        try
        {
            OntModel oModel = MetadataGlobal.LoadOWLWithModelSpec(MetadataConstants.sLocationLoadAlert, OntModelSpec.OWL_MEM_MICRO_RULE_INF);
                        
            String sQuery = "SELECT ?issueUri WHERE {?issueUri a <" + MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLClass_Issue + "> . " + sIssueDuplicatesSPARQL + "}";
                            
            ResultSet rsIssue = QueryExecutionFactory.create(sQuery, oModel).execSelect();
            
            while (rsIssue.hasNext())
            {
                QuerySolution qsIssue = rsIssue.nextSolution();
                
                MetadataGlobal.APIResponseData oIssueUri = new MetadataGlobal.APIResponseData();
                oIssueUri.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_issue + MetadataConstants.c_XMLE_Uri;
                oIssueUri.sData = qsIssue.get("?issueUri").toString();

                oData.oData.add(oIssueUri);
            }
        }
        catch (Exception e)
        {
        }
        return oData;
    }
    
    /**
     * @summary commit_getRelatedToKeyword
     * @startRealisation Sasa Stojanovic 17.01.2012.
     * @finalModification Sasa Stojanovic 17.01.2012.
     * @param sKeyword - keyword to search for
     * @return - APIResponseData object with results
     */
    public static MetadataGlobal.APIResponseData ac_commit_getRelatedToKeyword(String sKeyword)
    {
        MetadataGlobal.APIResponseData oData = new MetadataGlobal.APIResponseData();
        try
        {
            OntModel oModel = MetadataGlobal.LoadOWL(MetadataConstants.sLocationLoadAlert);
            
            //if keyword exists in comment annotation or in commit message
            String sQuery = "SELECT ?commitUri WHERE {"
                    + "{?commitUri a <" + MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLClass_Commit + "> . "
                    + "?commitUri <" + MetadataConstants.c_OWLAnnotationProperty_comment + "> ?comment . FILTER regex(?comment, \"" + sKeyword + "\", \"i\")}"
                    + " UNION "
                    + "{?commitUri a <" + MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLClass_Commit + "> . "
                    + "?commitUri <" + MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLDataProperty_CommitMessage + "> ?commitMessage . FILTER regex(?commitMessage, \"" + sKeyword + "\", \"i\")}"
                    + "}";
                            
            ResultSet rsCommit = QueryExecutionFactory.create(sQuery, oModel).execSelect();
            
            while (rsCommit.hasNext())
            {
                QuerySolution qsCommit = rsCommit.nextSolution();
                
                MetadataGlobal.APIResponseData oCommitUri = new MetadataGlobal.APIResponseData();
                oCommitUri.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_commit + MetadataConstants.c_XMLE_Uri;
                oCommitUri.sData = qsCommit.get("?commitUri").toString();

                oData.oData.add(oCommitUri);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return oData;
    }
    
    /**
     * @summary issue_getAllForMethod
     * @startRealisation Sasa Stojanovic 15.12.2011.
     * @finalModification Sasa Stojanovic 15.12.2011.
     * @param sMethodUri - methods' URIs
     * @return - APIResponseData object with results
     */
    public static MetadataGlobal.APIResponseData ac_sparql(String sSPARQL, OntModelSpec oOntModelSpec)
    {
        MetadataGlobal.APIResponseData oData = new MetadataGlobal.APIResponseData();
        try
        {
            String sData = sSPARQL.substring(sSPARQL.indexOf("SELECT") + 7, sSPARQL.indexOf("WHERE") - 1);
                       
            ArrayList <String> sParamName = new ArrayList<String>();
            while (sData.length() > 0)
            {               
                if (sData.indexOf("?", 1) != -1)
                {
                    sParamName.add(sData.substring(1, sData.indexOf("?", 1) - 1));
                    sData = sData.substring(sData.indexOf("?", 1));
                }
                else
                {
                    sParamName.add(sData.substring(1));
                    sData = "";
                }
            }
            
            OntModel oModel = MetadataGlobal.LoadOWLWithModelSpec(MetadataConstants.sLocationLoadAlert, oOntModelSpec);
            ResultSet rsResults = QueryExecutionFactory.create(sSPARQL, oModel).execSelect();
            
            while (rsResults.hasNext())
            {
                QuerySolution qsIssue = rsResults.nextSolution();
                MetadataGlobal.APIResponseData oResult = new MetadataGlobal.APIResponseData();
                oResult.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_result + "/";
                
                for (int i = 0; i < sParamName.size(); i++)
                {
                    MetadataGlobal.APIResponseData oParam = new MetadataGlobal.APIResponseData();
                    oParam.sReturnConfig = "s3:" + sParamName.get(i).toString();
                    oParam.sData = qsIssue.get("?" + sParamName.get(i).toString()).toString();
                    oResult.oData.add(oParam);
                }

                oData.oData.add(oResult);
            }
        }
        catch (Exception e)
        {
        }
        return oData;
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
        }
        return oData;
    }
    
    // </editor-fold>
    
    
    
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

            Individual inMember = omModel.getIndividual(sMemberURL);
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
                    OntProperty opProperty = omModel.getOntProperty(sTypeOf);
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
//             String NS = MetadataConstants.c_NS_w3_rdf_syntax;
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
