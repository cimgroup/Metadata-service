/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MetadataCore;

import MetadataCore.MetadataGlobal.AnnotationData;
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
import java.util.Iterator;



/**
 *
 * @author ivano
 */
public class MetadataRDFConverter {


    /**
     * @summary Save issue data
     * @startRealisation  Ivan Obradovic  31.08.2011.
     * @finalModification Sasa Stojanovic 28.02.2012.
     * @param oIssue - issue object with data
     * @return issue object with uri-s
     */
    public static Issue SaveIssue(Issue oIssue, boolean bIsUpdate)
    {
        try 
        {
            OntModel oModel = MetadataGlobal.LoadOWL(MetadataConstants.sLocationLoadAlert);
            
            //chech if issue with given id alredy exists
            boolean bNew = MetadataGlobal.IsItNew(oModel, MetadataConstants.c_NS_Alert_Its + MetadataConstants.c_OWLClass_Bug, oIssue.m_sID);
            
            //if it's new issue event and issue with that id already exists, do nothing
            if (bNew || bIsUpdate)
            {
                oIssue.m_sObjectURI = MetadataGlobal.GetObjectURI(oModel, MetadataConstants.c_NS_Alert_Its + MetadataConstants.c_OWLClass_Bug, oIssue.m_sID);
                Resource resBug = oModel.getResource(oIssue.m_sObjectURI);
                oIssue.m_sReturnConfig = "YY#o:" + MetadataConstants.c_XMLE_mdservice + "/o:" + MetadataConstants.c_XMLE_issue + MetadataConstants.c_XMLE_Uri;

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
                                oIssue.m_oBlocks[i].m_sReturnConfig = "YN#o:" + MetadataConstants.c_XMLE_issueBlocks + MetadataConstants.c_XMLE_Uri;
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
                                oIssue.m_oDependsOn[i].m_sReturnConfig = "YN#o:" + MetadataConstants.c_XMLE_issueDependsOn + MetadataConstants.c_XMLE_Uri;
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
                    oIssue.m_oHasReporter.m_sReturnConfig = "YN#o:" + MetadataConstants.c_XMLE_issueAuthor + MetadataConstants.c_XMLE_Uri;
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
                        oIssue.m_oIsIssueOf.m_oIsComponentOf.m_sReturnConfig = "YN#o:" + MetadataConstants.c_XMLE_product + MetadataConstants.c_XMLE_Uri;
                    }

                    resBug.removeAll(opIsIssueOf);
                    resBug.addProperty(opIsIssueOf, resIsIssueOf.asResource());
                    oIssue.m_oIsIssueOf.m_sReturnConfig = "YY#o:" + MetadataConstants.c_XMLE_issueProduct + "/o:" + MetadataConstants.c_XMLE_productComponent + MetadataConstants.c_XMLE_Uri;
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
                    oIssue.m_oHasComputerSystem.m_sReturnConfig = "YN#o:" + MetadataConstants.c_XMLE_issueComputerSystem + "/s:" + MetadataConstants.c_XMLE_issueComputerSystem + MetadataConstants.c_XMLE_Uri;
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
                    oIssue.m_oHasAssignee.m_sReturnConfig = "YN#o:" + MetadataConstants.c_XMLE_issueAssignedTo + MetadataConstants.c_XMLE_Uri;
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
                                oIssue.m_oHasCCPerson[i].m_sReturnConfig = "YN#o:" + MetadataConstants.c_XMLE_issueCCPerson + MetadataConstants.c_XMLE_Uri;
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
                    oIssue.m_oIsDuplicateOf.m_sReturnConfig = "YN#o:" + MetadataConstants.c_XMLE_issueDuplicateOf + MetadataConstants.c_XMLE_Uri;
                }

                //IsMergedInto
                if (!oIssue.m_oIsMergedInto.m_sID.isEmpty())
                {
                    oIssue.m_oIsMergedInto.m_sObjectURI = MetadataGlobal.GetObjectURI(oModel, MetadataConstants.c_NS_Alert_Its + MetadataConstants.c_OWLClass_Bug, oIssue.m_oIsMergedInto.m_sID);
                    ObjectProperty opIsMergedInto = oModel.getObjectProperty(MetadataConstants.c_NS_Alert_Its + MetadataConstants.c_OWLObjectProperty_IsMergedInto);
                    Resource resIsMergedInto = oModel.getResource(oIssue.m_oIsMergedInto.m_sObjectURI);
                    resBug.removeAll(opIsMergedInto);
                    resBug.addProperty(opIsMergedInto, resIsMergedInto.asResource());
                    oIssue.m_oIsMergedInto.m_sReturnConfig = "YN#o:" + MetadataConstants.c_XMLE_issueMergedInto + MetadataConstants.c_XMLE_Uri;
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
                    oIssue.m_oHasMilestone.m_sReturnConfig = "YN#o:" + MetadataConstants.c_XMLE_issueMilestone + "/o:" + MetadataConstants.c_XMLE_issueMilestone + MetadataConstants.c_XMLE_Uri;
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
                                oIssue.m_oHasComment[i].m_oHasCommentor.m_sReturnConfig = "YN#o:" + MetadataConstants.c_XMLE_commentPerson + MetadataConstants.c_XMLE_Uri;
                            }

                            resBug.addProperty(opHasComment, resComment.asResource());
                            oIssue.m_oHasComment[i].m_sReturnConfig = "YY#o:" + MetadataConstants.c_XMLE_issueComment + "/o:" + MetadataConstants.c_XMLE_comment + MetadataConstants.c_XMLE_Uri;
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
                                oIssue.m_oHasAttachment[i].m_oHasCreator.m_sReturnConfig = "YN#o:" + MetadataConstants.c_XMLE_attachmentCreator + MetadataConstants.c_XMLE_Uri;
                            }

                            resBug.addProperty(opHasAttachment, resAttachment.asResource());
                            oIssue.m_oHasAttachment[i].m_sReturnConfig = "YY#o:" + MetadataConstants.c_XMLE_issueAttachment + "/o:" + MetadataConstants.c_XMLE_attachment + MetadataConstants.c_XMLE_Uri;
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
                                oIssue.m_oHasActivity[i].m_oHasInvolvedPerson.m_sReturnConfig = "YN#o:" + MetadataConstants.c_XMLE_activityWho + MetadataConstants.c_XMLE_Uri;
                            }

                            resBug.addProperty(opHasActivity, resActivity.asResource());
                            oIssue.m_oHasActivity[i].m_sReturnConfig = "YY#o:" + MetadataConstants.c_XMLE_issueActivity + "/o:" + MetadataConstants.c_XMLE_activity + MetadataConstants.c_XMLE_Uri;
                        }
                    }
                }
                //end has attachment 

//                //AlertEvent
//                String sEventUri;
//                if (bIsUpdate)
//                {
//                    sEventUri = MetadataGlobal.GetObjectURI(oModel, MetadataConstants.c_NS_icep + MetadataConstants.c_OWLClass_ModifiedBug, "");
//                }
//                else
//                {
//                    sEventUri = MetadataGlobal.GetObjectURI(oModel, MetadataConstants.c_NS_icep + MetadataConstants.c_OWLClass_NewBug, "");
//                }
//                Resource resEvent = oModel.getResource(sEventUri);
//                ObjectProperty opIsRelatedToBug = oModel.getObjectProperty(MetadataConstants.c_NS_icep + MetadataConstants.c_OWLObjectProperty_IsRelatedToBug);
//                resEvent.addProperty(opIsRelatedToBug, resBug.asResource());

                //save data
                MetadataGlobal.SaveOWL(oModel, MetadataConstants.sLocationSaveAlert);
            }
        }
        catch (Exception ex)
        {
        }
        return oIssue;
    }
    
    /**
     * @summary Save commit data
     * @startRealisation Sasa Stojanovic 16.01.2012.
     * @finalModification Sasa Stojanovic 16.01.2012.
     * @param oCommit - commit object with data
     * @return commit object with uri-s
     */
    public static Commit SaveCommit(Commit oCommit)
    {
        try {
            
            OntModel oModel = MetadataGlobal.LoadOWL(MetadataConstants.sLocationLoadAlert);
            
            oCommit.m_sObjectURI = MetadataGlobal.GetObjectURI(oModel, MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLClass_Commit, oCommit.m_sID);
            Resource resCommit = oModel.getResource(oCommit.m_sObjectURI);
            oCommit.m_sReturnConfig = "YY#o:" + MetadataConstants.c_XMLE_mdservice + "/o:" + MetadataConstants.c_XMLE_commit + MetadataConstants.c_XMLE_Uri;
            
            //commitRepository
            if (oCommit.m_oIsCommitOfRepository != null && !oCommit.m_oIsCommitOfRepository.m_sObjectURI.isEmpty())
            {
                ObjectProperty opIsCommitOfRepository = oModel.getObjectProperty(MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLObjectProperty_IsCommitOfRepository);
                Resource resIsCommitOfRepository = oModel.getResource(oCommit.m_oIsCommitOfRepository.m_sObjectURI);
                resCommit.removeAll(opIsCommitOfRepository);
                resCommit.addProperty(opIsCommitOfRepository, resIsCommitOfRepository.asResource());
                oCommit.m_oIsCommitOfRepository.m_sReturnConfig = "YN#o:" + MetadataConstants.c_XMLE_commitRepository + MetadataConstants.c_XMLE_Uri;
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
                oCommit.m_oHasAuthor.m_sReturnConfig = "YN#o:" + MetadataConstants.c_XMLE_commitAuthor + MetadataConstants.c_XMLE_Uri;
            }
            
            //commitCommitter
            if (oCommit.m_oHasCommitter != null && !oCommit.m_oHasCommitter.m_sID.isEmpty())
            {
                SavePersonData(oCommit.m_oHasCommitter, oModel);
                ObjectProperty opHasCommitter = oModel.getObjectProperty(MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLObjectProperty_HasCommitter);
                Resource resHasCommitter = oModel.getResource(oCommit.m_oHasCommitter.m_sObjectURI);
                resCommit.removeAll(opHasCommitter);
                resCommit.addProperty(opHasCommitter, resHasCommitter.asResource());
                oCommit.m_oHasCommitter.m_sReturnConfig = "YN#o:" + MetadataConstants.c_XMLE_commitCommtter + MetadataConstants.c_XMLE_Uri;
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
            
            
            //HasFile
            if (oCommit.m_oHasFile != null)
            {
                ObjectProperty opHasFile = oModel.getObjectProperty(MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLObjectProperty_HasFile);
                for (int i = 0; i < oCommit.m_oHasFile.length; i++)
                {
                    if (oCommit.m_oHasFile[i] != null)
                    {
                        oCommit.m_oHasFile[i].m_sObjectURI = MetadataGlobal.GetObjectURI(oModel, MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLClass_File, oCommit.m_oHasFile[i].m_sID);
                        Resource resFile = oModel.getResource(oCommit.m_oHasFile[i].m_sObjectURI);

                        //HasAction
                        if (oCommit.m_oHasFile[i].m_oHasAction != null)
                        {
                            ObjectProperty opHasAction = oModel.getObjectProperty(MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLObjectProperty_HasAction);
                            resFile.removeAll(opHasAction);
                            
                            Class clsHasAction = oCommit.m_oHasFile[i].m_oHasAction.getClass();
                            Class clsAddFile = AddFile.class;
                            Class clsCopyFile = CopyFile.class;
                            Class clsDeleteFile = DeleteFile.class;
                            Class clsModifyFile = ModifyFile.class;
                            Class clsRenameFile = RenameFile.class;
                            Class clsReplaceFile = ReplaceFile.class;

                            if (clsHasAction.equals(clsAddFile))
                            {
                                OntClass ocHasAction = oModel.getOntClass(MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLClass_Add);
                                resFile.addProperty(opHasAction, ocHasAction.asResource());
                            }
                            if (clsHasAction.equals(clsCopyFile))
                            {
                                OntClass ocHasAction = oModel.getOntClass(MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLClass_Copy);
                                resFile.addProperty(opHasAction, ocHasAction.asResource());
                            }
                            if (clsHasAction.equals(clsDeleteFile))
                            {
                                OntClass ocHasAction = oModel.getOntClass(MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLClass_Delete);
                                resFile.addProperty(opHasAction, ocHasAction.asResource());
                            }
                            if (clsHasAction.equals(clsModifyFile))
                            {
                                OntClass ocHasAction = oModel.getOntClass(MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLClass_Modify);
                                resFile.addProperty(opHasAction, ocHasAction.asResource());
                            }
                            if (clsHasAction.equals(clsRenameFile))
                            {
                                OntClass ocHasAction = oModel.getOntClass(MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLClass_Rename);
                                resFile.addProperty(opHasAction, ocHasAction.asResource());
                            }
                            if (clsHasAction.equals(clsReplaceFile))
                            {
                                OntClass ocHasAction = oModel.getOntClass(MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLClass_Replace);
                                resFile.addProperty(opHasAction, ocHasAction.asResource());
                            }
                        }

                        //branch
                        if (!oCommit.m_oHasFile[i].m_sBranch.isEmpty())
                        {
                            DatatypeProperty dtpOnBranch = oModel.getDatatypeProperty(MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLDataProperty_OnBranch);
                            resFile.removeAll(dtpOnBranch);
                            resFile.addProperty(dtpOnBranch, oCommit.m_oHasFile[i].m_sBranch);
                        }
                        
                        if (oCommit.m_oHasFile[i].m_oHasModule != null)
                        {
                            ObjectProperty opHasModule = oModel.getObjectProperty(MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLObjectProperty_HasModules);
                            for (int j = 0; j < oCommit.m_oHasFile[i].m_oHasModule.length; j++)
                            {
                                if (oCommit.m_oHasFile[i].m_oHasModule[j] != null)
                                {
                                    oCommit.m_oHasFile[i].m_oHasModule[j].m_sObjectURI = MetadataGlobal.GetObjectURI(oModel, MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLClass_Module, oCommit.m_oHasFile[i].m_oHasModule[j].m_sID);
                                    Resource resModule = oModel.getResource(oCommit.m_oHasFile[i].m_oHasModule[j].m_sObjectURI);
                                    
                                    //module name
                                    if (!oCommit.m_oHasFile[i].m_oHasModule[j].m_sName.isEmpty())
                                    {
                                        DatatypeProperty dtpName = oModel.getDatatypeProperty(MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLDataProperty_Name);
                                        resModule.removeAll(dtpName);
                                        resModule.addProperty(dtpName, oCommit.m_oHasFile[i].m_oHasModule[j].m_sName);
                                    }
                                    
                                    //module start line
                                    if (oCommit.m_oHasFile[i].m_oHasModule[j].m_iStartLine != -1)
                                    {
                                        DatatypeProperty dtpStartLine = oModel.getDatatypeProperty(MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLDataProperty_StartLine);
                                        resModule.removeAll(dtpStartLine);
                                        resModule.addProperty(dtpStartLine, String.valueOf(oCommit.m_oHasFile[i].m_oHasModule[j].m_iStartLine));
                                    }
                                    
                                    //module end line
                                    if (oCommit.m_oHasFile[i].m_oHasModule[j].m_iEndLine != -1)
                                    {
                                        DatatypeProperty dtpEndLine = oModel.getDatatypeProperty(MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLDataProperty_EndLine);
                                        resModule.removeAll(dtpEndLine);
                                        resModule.addProperty(dtpEndLine, String.valueOf(oCommit.m_oHasFile[i].m_oHasModule[j].m_iEndLine));
                                    }
                                    
                                    ObjectProperty opHasMethod = oModel.getObjectProperty(MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLObjectProperty_HasMethods);
                                    for (int k = 0; k < oCommit.m_oHasFile[i].m_oHasModule[j].m_oHasMethod.length; k++)
                                    {
                                        if (oCommit.m_oHasFile[i].m_oHasModule[j].m_oHasMethod[k] != null)
                                        {
                                            oCommit.m_oHasFile[i].m_oHasModule[j].m_oHasMethod[k].m_sObjectURI = MetadataGlobal.GetObjectURI(oModel, MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_Method, oCommit.m_oHasFile[i].m_oHasModule[j].m_oHasMethod[k].m_sID);
                                            Resource resMethod = oModel.getResource(oCommit.m_oHasFile[i].m_oHasModule[j].m_oHasMethod[k].m_sObjectURI);

                                            //method name
                                            if (!oCommit.m_oHasFile[i].m_oHasModule[j].m_oHasMethod[k].m_sName.isEmpty())
                                            {
                                                DatatypeProperty dtpName = oModel.getDatatypeProperty(MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLDataProperty_Name);
                                                resMethod.removeAll(dtpName);
                                                resMethod.addProperty(dtpName, oCommit.m_oHasFile[i].m_oHasModule[j].m_oHasMethod[k].m_sName);
                                            }

                                            //method start line
                                            if (oCommit.m_oHasFile[i].m_oHasModule[j].m_oHasMethod[k].m_iStartLine != -1)
                                            {
                                                DatatypeProperty dtpStartLine = oModel.getDatatypeProperty(MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLDataProperty_StartLine);
                                                resMethod.removeAll(dtpStartLine);
                                                resMethod.addProperty(dtpStartLine, String.valueOf(oCommit.m_oHasFile[i].m_oHasModule[j].m_oHasMethod[k].m_iStartLine));
                                            }

                                            //method end line
                                            if (oCommit.m_oHasFile[i].m_oHasModule[j].m_oHasMethod[k].m_iEndLine != -1)
                                            {
                                                DatatypeProperty dtpEndLine = oModel.getDatatypeProperty(MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLDataProperty_EndLine);
                                                resMethod.removeAll(dtpEndLine);
                                                resMethod.addProperty(dtpEndLine, String.valueOf(oCommit.m_oHasFile[i].m_oHasModule[j].m_oHasMethod[k].m_iEndLine));
                                            }

                                            resModule.addProperty(opHasMethod, resMethod.asResource());
                                            oCommit.m_oHasFile[i].m_oHasModule[j].m_oHasMethod[k].m_sReturnConfig = "YN#o:" + MetadataConstants.c_XMLE_moduleMethods + "/o:" + MetadataConstants.c_XMLE_method + MetadataConstants.c_XMLE_Uri;
                                        }
                                    }
                                    
                                    resFile.addProperty(opHasModule, resModule.asResource());
                                    oCommit.m_oHasFile[i].m_oHasModule[j].m_sReturnConfig = "YY#o:" + MetadataConstants.c_XMLE_fileModules + "/o:" + MetadataConstants.c_XMLE_module + MetadataConstants.c_XMLE_Uri;
                                }
                            }
                        }
                            
                        resCommit.addProperty(opHasFile, resFile.asResource());
                        oCommit.m_oHasFile[i].m_sReturnConfig = "YY#o:" + MetadataConstants.c_XMLE_commitFile + "/o:" + MetadataConstants.c_XMLE_file + MetadataConstants.c_XMLE_Uri;
                    }
                }
            }
            
//            //AlertEvent
//            String sEventUri = MetadataGlobal.GetObjectURI(oModel, MetadataConstants.c_NS_icep + MetadataConstants.c_OWLClass_CommitEvent, "");
//            Resource resEvent = oModel.getResource(sEventUri);
//            ObjectProperty opIsRelatedToBug = oModel.getObjectProperty(MetadataConstants.c_NS_icep + MetadataConstants.c_OWLObjectProperty_IsRelatedToCommit);
//            resEvent.addProperty(opIsRelatedToBug, resCommit.asResource());
            
            //save data
            MetadataGlobal.SaveOWL(oModel, MetadataConstants.sLocationSaveAlert);
        }
        catch (Exception ex)
        {
        }
        return oCommit;
    }
    
    /**
     * @summary Save mail data
     * @startRealisation Sasa Stojanovic 02.02.2012.
     * @finalModification Sasa Stojanovic 02.02.2012.
     * @param oMail - mail object with data
     * @return mail object with uri-s
     */
    public static Mail SaveMail(Mail oMail)
    {
        try {
            
            OntModel oModel = MetadataGlobal.LoadOWL(MetadataConstants.sLocationLoadAlert);
            
            oMail.m_sObjectURI = MetadataGlobal.GetObjectURI(oModel, MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_Email, oMail.m_sID);
            Resource resMail = oModel.getResource(oMail.m_sObjectURI);
            oMail.m_sReturnConfig = "YY#o:" + MetadataConstants.c_XMLE_mdservice + "/o:" + MetadataConstants.c_XMLE_email + MetadataConstants.c_XMLE_Uri;
            
            //messageId
            if (!oMail.m_sMessageId.isEmpty())
            {
                DatatypeProperty dtpMessageId = oModel.getDatatypeProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_MessageId);
                resMail.addProperty(dtpMessageId, oMail.m_sMessageId);
            }
            
            //from
            if (oMail.m_oFrom != null && !oMail.m_oFrom.m_sID.isEmpty())
            {
                SavePersonData(oMail.m_oFrom, oModel);
                ObjectProperty opFrom = oModel.getObjectProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLObjectProperty_From);
                Resource resFrom = oModel.getResource(oMail.m_oFrom.m_sObjectURI);
                resMail.addProperty(opFrom, resFrom.asResource());
                oMail.m_oFrom.m_sReturnConfig = "YN#o:" + MetadataConstants.c_XMLE_from + MetadataConstants.c_XMLE_Uri;
            }
            
            //creation date
            if (oMail.m_dtmHasCreationDate != null)
            {
                DatatypeProperty dtpHasCreationDate = oModel.getDatatypeProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_HasCreationDate);
                resMail.addProperty(dtpHasCreationDate, oMail.m_dtmHasCreationDate.toString());
            }
            
            //subject
            if (!oMail.m_sSubject.isEmpty())
            {
                DatatypeProperty dtpSubject = oModel.getDatatypeProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_Subject);
                resMail.addProperty(dtpSubject, oMail.m_sSubject);
            }
            
            //in reply to
            if (oMail.m_oInReplyTo != null && !oMail.m_oInReplyTo.m_sID.isEmpty())
            {
                oMail.m_oInReplyTo.m_sObjectURI = MetadataGlobal.GetObjectURI(oModel, MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_Email, oMail.m_oInReplyTo.m_sID);
                ObjectProperty opInReplyTo = oModel.getObjectProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLObjectProperty_InReplyTo);
                Resource resInReplyTo = oModel.getResource(oMail.m_oInReplyTo.m_sObjectURI);
                resMail.addProperty(opInReplyTo, resInReplyTo.asResource());
                oMail.m_oInReplyTo.m_sReturnConfig = "YN#o:" + MetadataConstants.c_XMLE_inReplyTo + MetadataConstants.c_XMLE_Uri;
            }

            //references
            if (oMail.m_oReferences != null)
            {
                ObjectProperty opReferences = oModel.getObjectProperty(MetadataConstants.c_NS_purl + MetadataConstants.c_OWLObjectProperty_References);
                for (int i = 0; i < oMail.m_oReferences.length; i++)
                {
                    if (oMail.m_oReferences[i] != null && !oMail.m_oReferences[i].m_sID.isEmpty())
                    {
                        oMail.m_oReferences[i].m_sObjectURI = MetadataGlobal.GetObjectURI(oModel, MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_Email, oMail.m_oReferences[i].m_sID);
                        Resource resReferences = oModel.getResource(oMail.m_oReferences[i].m_sObjectURI);
                        resMail.addProperty(opReferences, resReferences.asResource());
                        oMail.m_oReferences[i].m_sReturnConfig = "YN#o:" + MetadataConstants.c_XMLE_references + MetadataConstants.c_XMLE_Uri;
                    }
                }
            }
            
            //attachments
            if (oMail.m_sAttachment != null)
            {
                DatatypeProperty dtpAttachment = oModel.getDatatypeProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_Attachment);
                for (int i = 0; i < oMail.m_sAttachment.length; i++)
                {
                    if (oMail.m_sAttachment[i] != null && !oMail.m_sAttachment[i].isEmpty())
                    {
                        resMail.addProperty(dtpAttachment, oMail.m_sAttachment[i]);
                    }
                }
            }
            
            //content
            if (!oMail.m_sContent.isEmpty())
            {
                DatatypeProperty dtpBody = oModel.getDatatypeProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_Body);
                resMail.addProperty(dtpBody, oMail.m_sContent);
            }
            
//            //AlertEvent
//            String sEventUri = MetadataGlobal.GetObjectURI(oModel, MetadataConstants.c_NS_icep + MetadataConstants.c_OWLClass_MailEvent, "");
//            Resource resEvent = oModel.getResource(sEventUri);
//            ObjectProperty opIsRelatedToBug = oModel.getObjectProperty(MetadataConstants.c_NS_icep + MetadataConstants.c_OWLObjectProperty_IsRelatedToMail);
//            resEvent.addProperty(opIsRelatedToBug, resMail.asResource());
            
            //save data
            MetadataGlobal.SaveOWL(oModel, MetadataConstants.sLocationSaveAlert);
        }
        catch (Exception ex)
        {
        }
        return oMail;
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
            
            if (oPerson.m_sFirstName != null && !oPerson.m_sFirstName.isEmpty())
            {
                DatatypeProperty dtpFirstName = oModel.getDatatypeProperty(MetadataConstants.c_NS_foaf + MetadataConstants.c_OWLDataProperty_FirstName);
                resPerson.removeAll(dtpFirstName);
                resPerson.addProperty(dtpFirstName, oPerson.m_sFirstName);
            }

            if (oPerson.m_sLastName != null && !oPerson.m_sLastName.isEmpty())
            {
                DatatypeProperty dtpLastName = oModel.getDatatypeProperty(MetadataConstants.c_NS_foaf + MetadataConstants.c_OWLDataProperty_LastName);
                resPerson.removeAll(dtpLastName);
                resPerson.addProperty(dtpLastName, oPerson.m_sLastName);
            }
            
            if (oPerson.m_sEmail != null && !oPerson.m_sEmail.isEmpty())
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
    
    /**
     * @summary Save annotation data.
     * @startRealisation  Dejan Milosavljevic 17.01.2012.
     * @finalModification Dejan Milosavljevic 17.01.2012.
     * @param oAnnotation - AnnotationData object
     * @return - same AnnotationData object with filled m_sObjectURI
     */
    public static AnnotationData SaveAnnotationData(AnnotationData oAnnotation)
    {
        try
        {
            OntModel omModel = MetadataGlobal.LoadOWL(MetadataConstants.sLocationLoadAlert);
            
            Resource resObject = omModel.getResource(oAnnotation.m_sObjectURI);
            if (resObject != null && oAnnotation.oAnnotated != null)
            {
                //Anotation concepts
                String sConceptClass = MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_AnnotationConcept;
                if (oAnnotation.oConcepts != null)
                {
                    int iCCount = oAnnotation.oConcepts.length;
                    for (int i = 0; i < iCCount; i++)
                    {
                        OntClass ocConcept = omModel.getOntClass(sConceptClass);
            
                        oAnnotation.oConcepts[i].m_sObjectURI = sConceptClass + MetadataGlobal.GetNextId(omModel, ocConcept);
                        
                        Resource resConcept = omModel.createResource(oAnnotation.oConcepts[i].m_sObjectURI, ocConcept);
                        oAnnotation.oConcepts[i].m_sReturnConfig = "YN#s1:" + oAnnotation.oConcepts[i].sName + MetadataConstants.c_XMLE_Uri;
                        
                        //Uri
                        DatatypeProperty dtpUri = omModel.getDatatypeProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_Uri);
                        //if (dtpUri == null)
                        //{
                        //    dtpUri = omModel.createDatatypeProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_Uri);
                        //}
                        resConcept.addProperty(dtpUri, oAnnotation.oConcepts[i].sUri);
                        
                        //Count
                        DatatypeProperty dtpCount = omModel.getDatatypeProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_Count);
                        //if (dtpCount == null)
                        //{
                        //    dtpCount = omModel.createDatatypeProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_Count);
                        //}
                        resConcept.addProperty(dtpCount, oAnnotation.oConcepts[i].sCount);
                    }
                }
                
                //Annotations
                String sAnnotationClass = MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_Annotation;
                int iACount = oAnnotation.oAnnotated.length;
                for (int i = 0; i < iACount; i++)
                {
                    OntClass ocAnnotation = omModel.getOntClass(sAnnotationClass);
                    oAnnotation.oAnnotated[i].m_sObjectURI = sAnnotationClass + MetadataGlobal.GetNextId(omModel, ocAnnotation);
                    Resource resAnnotation = omModel.createResource(oAnnotation.oAnnotated[i].m_sObjectURI, ocAnnotation);
                    oAnnotation.oAnnotated[i].m_sReturnConfig = "YY#s1:" + oAnnotation.oAnnotated[i].sName + MetadataConstants.c_XMLE_Uri;
                    
                    //Name
                    DatatypeProperty dtpName = omModel.getDatatypeProperty(MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLDataProperty_Name);
                    //if (dtpName == null)
                    //{
                    //    dtpName = omModel.createDatatypeProperty(MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLDataProperty_Name);
                    //}
                    resAnnotation.addProperty(dtpName, oAnnotation.oAnnotated[i].sName);
                    
                    //Text
                    DatatypeProperty dtpText = omModel.getDatatypeProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_Text);
                    //if (dtpText == null)
                    //{
                    //    dtpText = omModel.createDatatypeProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_Text);
                    //}
                    resAnnotation.addProperty(dtpText, oAnnotation.oAnnotated[i].sValue);
                        
                    //HasConcept
                    for (int j = 0; j < oAnnotation.oConcepts.length; j++)
                    {
                        String sConceptName = GetConceptName(oAnnotation.oAnnotated[i].sName);
                        if (!sConceptName.isEmpty() && sConceptName.equals(oAnnotation.oConcepts[j].sName))
                        {
                            Resource resConcept = omModel.getResource(oAnnotation.oConcepts[j].m_sObjectURI);
                            //oAnnotation.oConcepts[j].m_sReturnConfig = "YN#s1:" + oAnnotation.oConcepts[j].sName + MetadataConstants.c_XMLE_Uri;
                            ObjectProperty opHasConcepts = omModel.getObjectProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLObjectProperty_HasConcepts);
                            //if (opHasConcepts == null)
                            //{
                            //    opHasConcepts = omModel.createObjectProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLObjectProperty_HasConcepts);
                            //}
                            resAnnotation.addProperty(opHasConcepts, resConcept.asResource());
                        }
                    }
                    
                    //HasObject
                    ObjectProperty opHasObject = omModel.getObjectProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLObjectProperty_HasObject);
                    resAnnotation.addProperty(opHasObject, resObject.asResource());
                    
                    ////Old code
                    //String sOWLDataProperty = GetAnnotationPropName(oAnnotation.oAnnotated[i].sName);
                    //if (!sOWLDataProperty.isEmpty())
                    //{
                    //    String sOWLFullPropertyName = MetadataConstants.c_NS_Alert + sOWLDataProperty;
                    //    AnnotationProperty apAnnotation = omModel.getAnnotationProperty(sOWLFullPropertyName);
                    //    if (apAnnotation == null)
                    //    {
                    //        apAnnotation = omModel.createAnnotationProperty(sOWLFullPropertyName);
                    //    }
                    //    resObject.addProperty(apAnnotation, oAnnotation.oAnnotated[i].sValue);
                    //}
                }
                
                //Kewords
                if (oAnnotation.oKeywords != null)
                {
                    String sKeywordName =  MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLAnnotationProperty_apKeyword;
                    for (int i = 0; i < oAnnotation.oKeywords.length; i++)
                    {
                        AnnotationProperty apKeyword = omModel.getAnnotationProperty(sKeywordName);
                        //if (apKeyword == null)
                        //{
                        //    apKeyword = omModel.createAnnotationProperty(sKeywordName);
                        //}
                        resObject.addProperty(apKeyword, oAnnotation.oKeywords[i]);
                    }
                }
            }
            
            MetadataGlobal.SaveOWL(omModel, MetadataConstants.sLocationSaveAlert);
        }
        catch (Exception e)
        {
        }
        return oAnnotation;
    }
    
    /**
     * @summary Get concept name for annotation.
     * @startRealisation  Dejan Milosavljevic 17.01.2012.
     * @finalModification Dejan Milosavljevic 17.01.2012.
     * @param sAnnotationName - annotation name.
     * @return - concept name for annotation.
     */
    private static String GetConceptName(String sAnnotationName)
    {
        String sConceptName = "";
        
        if (sAnnotationName.equals(MetadataConstants.c_XMLE_subjectAnnotated))
        {
            sConceptName = MetadataConstants.c_XMLE_subjectConcepts;
        }
        else if (sAnnotationName.equals(MetadataConstants.c_XMLE_descriptionAnnotated))
        {
            sConceptName = MetadataConstants.c_XMLE_descriptionConcepts;
        }
        else if (sAnnotationName.equals(MetadataConstants.c_XMLE_commentAnnotated))
        {
            sConceptName = MetadataConstants.c_XMLE_commentConcepts;
        }
        else if (sAnnotationName.equals(MetadataConstants.c_XMLE_commitAnnotated))
        {
            sConceptName = MetadataConstants.c_XMLE_commitConcepts;
        }
        else if (sAnnotationName.equals(MetadataConstants.c_XMLE_titleAnnotated))
        {
            sConceptName = MetadataConstants.c_XMLE_titleConcepts;
        }
        else if (sAnnotationName.equals(MetadataConstants.c_XMLE_bodyAnnotated))
        {
            sConceptName = MetadataConstants.c_XMLE_bodyConcepts;
        }
                
        return sConceptName;
    }
    
    /**
     * @summary Get annotation property name for annotation.
     * @startRealisation  Dejan Milosavljevic 17.01.2012.
     * @finalModification Dejan Milosavljevic 17.01.2012.
     * @param sAnnotationName - annotation name.
     * @return - data property name for annotation.
     */
    private static String GetAnnotationPropName(String sAnnotationName)
    {
        String sAnnotationPropertyName = "";
        
        if (sAnnotationName.equals(MetadataConstants.c_XMLE_subjectAnnotated))
        {
            sAnnotationPropertyName = MetadataConstants.c_OWLAnnotationProperty_apSubject;
        }
        else if (sAnnotationName.equals(MetadataConstants.c_XMLE_descriptionAnnotated))
        {
            sAnnotationPropertyName = MetadataConstants.c_OWLAnnotationProperty_apDescription;
        }
        else if (sAnnotationName.equals(MetadataConstants.c_XMLE_commentAnnotated))
        {
            sAnnotationPropertyName = MetadataConstants.c_OWLAnnotationProperty_apComment;
        }
        else if (sAnnotationName.equals(MetadataConstants.c_XMLE_commitAnnotated))
        {
            sAnnotationPropertyName = MetadataConstants.c_OWLAnnotationProperty_apCommit;
        }
        else if (sAnnotationName.equals(MetadataConstants.c_XMLE_titleAnnotated))
        {
            sAnnotationPropertyName = MetadataConstants.c_OWLAnnotationProperty_apTitle;
        }
        else if (sAnnotationName.equals(MetadataConstants.c_XMLE_bodyAnnotated))
        {
            sAnnotationPropertyName = MetadataConstants.c_OWLAnnotationProperty_apBody;
        }
       
        return sAnnotationPropertyName;
    }
    
    /**
     * @summary Save forum post data.
     * @startRealisation  Dejan Milosavljevic 17.01.2012.
     * @finalModification Sasa Stojanovic 21.02.2012.
     * @param oForumPost - ForumPost object
     */
    public static ForumPost SaveForumPost(ForumPost oForumPost)
    {
        try
        {
            OntModel omModel = MetadataGlobal.LoadOWL(MetadataConstants.sLocationLoadAlert);
            
            oForumPost.m_sObjectURI = MetadataGlobal.GetObjectURI(omModel, MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_post, oForumPost.m_sID);
            Resource resPost = omModel.getResource(oForumPost.m_sObjectURI);
            oForumPost.m_sReturnConfig = "YY#o:" + MetadataConstants.c_XMLE_mdservice + "/o:" + MetadataConstants.c_XMLE_post + MetadataConstants.c_XMLE_Uri;
            
            //ForumItemID
            if (!oForumPost.m_sForumItemID.isEmpty())
            {
                DatatypeProperty dtpForumItemID = omModel.getDatatypeProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_ForumItemID);
                resPost.removeAll(dtpForumItemID);
                resPost.addProperty(dtpForumItemID, oForumPost.m_sForumItemID);
            }
            
            //Time
            if (oForumPost.m_dtmTime != null)
            {
                DatatypeProperty dtpTime = omModel.getDatatypeProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_PostTime);
                resPost.removeAll(dtpTime);
                resPost.addProperty(dtpTime, oForumPost.m_dtmTime.toString());
            }
            
            //Subject
            if (!oForumPost.m_sSubject.isEmpty())
            {
                DatatypeProperty dtpSubject = omModel.getDatatypeProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_Subject);
                resPost.removeAll(dtpSubject);
                resPost.addProperty(dtpSubject, oForumPost.m_sSubject);
            }

            //Body
            if (!oForumPost.m_sBody.isEmpty())
            {
                DatatypeProperty dtpBody = omModel.getDatatypeProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_Body);
                resPost.removeAll(dtpBody);
                resPost.addProperty(dtpBody, oForumPost.m_sBody);
            }
            
            //isAuthorOf
            if (oForumPost.m_oAuthor != null && !oForumPost.m_oAuthor.m_sID.isEmpty())
            {
                SavePersonData(oForumPost.m_oAuthor, omModel);
                ObjectProperty opHasAuthor = omModel.getObjectProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLObjectProperty_Author);
                Resource resHasAuthor = omModel.getResource(oForumPost.m_oAuthor.m_sObjectURI);
                resPost.removeAll(opHasAuthor);
                resPost.addProperty(opHasAuthor, resHasAuthor.asResource());
                oForumPost.m_oAuthor.m_sReturnConfig = "YN#o:" + MetadataConstants.c_XMLE_author + MetadataConstants.c_XMLE_Uri;
            }
            
            //Category
            if (!oForumPost.m_sCategory.isEmpty())
            {
                DatatypeProperty dtpCategory = omModel.getDatatypeProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_Category);
                resPost.removeAll(dtpCategory);
                resPost.addProperty(dtpCategory, oForumPost.m_sCategory);
            }
            
            //HasPost - thread
            if (oForumPost.m_oInForumThread != null)
            {
                oForumPost.m_oInForumThread.m_sObjectURI = MetadataGlobal.GetObjectURI(omModel, MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_threads, oForumPost.m_oInForumThread.m_sID);
                Resource resThread = omModel.getResource(oForumPost.m_oInForumThread.m_sObjectURI);
                oForumPost.m_oInForumThread.m_sReturnConfig = "YY#o:" + MetadataConstants.c_XMLE_thread + MetadataConstants.c_XMLE_Uri;
                ObjectProperty opHasPosts = omModel.getObjectProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLObjectProperty_HasPosts);
                resThread.addProperty(opHasPosts, resPost.asResource());
            
                //HasThread - forum
                if (oForumPost.m_oInForumThread.m_oInForum != null)
                {
                    oForumPost.m_oInForumThread.m_oInForum.m_sObjectURI = MetadataGlobal.GetObjectURI(omModel, MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_forum, oForumPost.m_oInForumThread.m_oInForum.m_sID);
                    Resource resForum = omModel.getResource(oForumPost.m_oInForumThread.m_oInForum.m_sObjectURI);
                    oForumPost.m_oInForumThread.m_oInForum.m_sReturnConfig = "YN#o:" + MetadataConstants.c_XMLE_forum + MetadataConstants.c_XMLE_Uri;
                    ObjectProperty opHasThreads = omModel.getObjectProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLObjectProperty_HasThreads);
                    resForum.addProperty(opHasThreads, resThread.asResource());
                    
                    //forum name
                    if (!oForumPost.m_oInForumThread.m_oInForum.m_sName.isEmpty())
                    {
                        DatatypeProperty dtpForumName = omModel.getDatatypeProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_Name);
                        resPost.removeAll(dtpForumName);
                        resPost.addProperty(dtpForumName, oForumPost.m_oInForumThread.m_oInForum.m_sName);
                    }
                }
            }
            
//            //AlertEvent
//            String sEventUri = MetadataGlobal.GetObjectURI(omModel, MetadataConstants.c_NS_icep + MetadataConstants.c_OWLClass_PostEvent, "");
//            Resource resEvent = omModel.getResource(sEventUri);
//            ObjectProperty opIsRelatedToBug = omModel.getObjectProperty(MetadataConstants.c_NS_icep + MetadataConstants.c_OWLObjectProperty_IsRelatedToPost);
//            resEvent.addProperty(opIsRelatedToBug, resPost.asResource());
            
            MetadataGlobal.SaveOWL(omModel, MetadataConstants.sLocationSaveAlert);
        }
        catch (Exception e)
        {
        }
        
        return oForumPost;
    }
    
    /**
     * @summary Save competence data.
     * @startRealisation  Dejan Milosavljevic 03.02.2012.
     * @finalModification Dejan Milosavljevic 03.02.2012.
     * @param oCompetence - Competence object
     */
    public static Competence SaveCompetence(Competence oCompetence)
    {
        try
        {
            OntModel omModel = MetadataGlobal.LoadOWL(MetadataConstants.sLocationLoadAlert);
            
            oCompetence.m_sObjectURI = MetadataGlobal.GetObjectURI(omModel, MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_Competence, oCompetence.m_sID);
            Resource resCompetence = omModel.getResource(oCompetence.m_sObjectURI);
            oCompetence.m_sReturnConfig = "YY#o:" + MetadataConstants.c_XMLE_mdservice + "/o:" + MetadataConstants.c_XMLE_competency + MetadataConstants.c_XMLE_Uri;
//            
            //Identity ID
            if (oCompetence.m_oIdentity != null)
            {
                oCompetence.m_oIdentity.m_sObjectURI = MetadataGlobal.GetObjectURI(omModel, MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_Identity, oCompetence.m_oIdentity.m_sID);
                ObjectProperty opIdentity = omModel.getObjectProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLObjectProperty_HasCompetences);
                Resource resIdentity = omModel.getResource(oCompetence.m_oIdentity.m_sObjectURI);
                oCompetence.m_oIdentity.m_sReturnConfig = "YN#o:" + MetadataConstants.c_XMLE_identity + MetadataConstants.c_XMLE_Uri;
                resIdentity.addProperty(opIdentity, resCompetence.asResource());
            }
            //HasLevel (index)
            if (!oCompetence.m_sLevel.isEmpty())
            {
                DatatypeProperty dtpHasLevel = omModel.getDatatypeProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_HasLevel);
                resCompetence.removeAll(dtpHasLevel);
                resCompetence.addProperty(dtpHasLevel, oCompetence.m_sLevel);
            }
            //Attributs
            if (oCompetence.m_oHasAttribute != null && oCompetence.m_oHasAttribute.length > 0)
            {
                ObjectProperty opHasAttribute = omModel.getObjectProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLObjectProperty_HasAttribute);
                //resCompetence.removeAll(opHasAttribute);
                for (Attribute oAttribute: oCompetence.m_oHasAttribute)
                {
                    if (oAttribute != null && oAttribute.m_sName != null)
                    {
                        //    Fluency
                        if (oAttribute.m_sName.equals(MetadataConstants.c_XMLE_fluency))
                        {
                            oAttribute.m_sObjectURI = MetadataGlobal.GetObjectURI(omModel, MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_Fluency, oAttribute.m_sID);
                            ObjectProperty opFluency = omModel.getObjectProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLObjectProperty_HasAttribute);
                            Resource resFluency = omModel.getResource(oAttribute.m_sObjectURI);
                            oAttribute.m_sReturnConfig = "YY#o:" + MetadataConstants.c_XMLE_fluency + MetadataConstants.c_XMLE_Uri;
                            if (oAttribute.m_oHasMetric != null && oAttribute.m_oHasMetric.length > 0)
                            {
                                for (Metric oMetric: oAttribute.m_oHasMetric)
                                {
                                    //    apiCount
                                    if (oMetric.m_sMetricName.equals("apiCount") && !oMetric.m_sMetricValue.isEmpty())
                                    {
                                        oMetric.m_sObjectURI = MetadataGlobal.GetObjectURI(omModel, MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_APIMetric, oMetric.m_sID);
                                        ObjectProperty opMetric = omModel.getObjectProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLObjectProperty_HasMetric);
                                        Resource resMetric = omModel.getResource(oMetric.m_sObjectURI);
                                        oMetric.m_sReturnConfig = "YN#o:apiCountUri";
                                        
                                        DatatypeProperty dtpName = omModel.getDatatypeProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_MetricName);
                                        resMetric.removeAll(dtpName);
                                        resMetric.addProperty(dtpName, "apiCount");
                                        DatatypeProperty dtpValue = omModel.getDatatypeProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_Level);
                                        resMetric.removeAll(dtpValue);
                                        resMetric.addProperty(dtpValue, oMetric.m_sMetricValue);
                                        
                                        resFluency.addProperty(opMetric, resMetric.asResource());
                                    }
                                    //    apiIntroduced
                                    if (oMetric.m_sMetricName.equals("apiIntroduced") && !oMetric.m_sMetricValue.isEmpty())
                                    {
                                        oMetric.m_sObjectURI = MetadataGlobal.GetObjectURI(omModel, MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_APIMetric, oMetric.m_sID);
                                        ObjectProperty opMetric = omModel.getObjectProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLObjectProperty_HasMetric);
                                        Resource resMetric = omModel.getResource(oMetric.m_sObjectURI);
                                        oMetric.m_sReturnConfig = "YN#o:apiIntroducedUri";
                                        
                                        DatatypeProperty dtpName = omModel.getDatatypeProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_MetricName);
                                        resMetric.removeAll(dtpName);
                                        resMetric.addProperty(dtpName, "apiIntroduced");
                                        DatatypeProperty dtpValue = omModel.getDatatypeProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_Level);
                                        resMetric.removeAll(dtpValue);
                                        resMetric.addProperty(dtpValue, oMetric.m_sMetricValue);
                                        
                                        resFluency.addProperty(opMetric, resMetric.asResource());
                                    }
                                    //    staticAnalysis
                                    if (oMetric.m_sMetricName.equals("staticAnalysis") && !oMetric.m_sMetricValue.isEmpty())
                                    {
                                        oMetric.m_sObjectURI = MetadataGlobal.GetObjectURI(omModel, MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_StaticAnalysisMetric, oMetric.m_sID);
                                        ObjectProperty opMetric = omModel.getObjectProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLObjectProperty_HasMetric);
                                        Resource resMetric = omModel.getResource(oMetric.m_sObjectURI);
                                        oMetric.m_sReturnConfig = "YN#o:staticAnalysisUri";
                                        
                                        DatatypeProperty dtpName = omModel.getDatatypeProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_MetricName);
                                        resMetric.removeAll(dtpName);
                                        resMetric.addProperty(dtpName, "staticAnalysis");
                                        DatatypeProperty dtpValue = omModel.getDatatypeProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_Level);
                                        resMetric.removeAll(dtpValue);
                                        resMetric.addProperty(dtpValue, oMetric.m_sMetricValue);
                                        
                                        resFluency.addProperty(opMetric, resMetric.asResource());
                                    }
                                }
                            }
                            resCompetence.addProperty(opFluency, resFluency.asResource());
                        }
                        //    Contribution
                        if (oAttribute.m_sName.equals(MetadataConstants.c_XMLE_contribution))
                        {
                            oAttribute.m_sObjectURI = MetadataGlobal.GetObjectURI(omModel, MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_Contribution, oAttribute.m_sID);
                            ObjectProperty opContribution = omModel.getObjectProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLObjectProperty_HasAttribute);
                            Resource resContribution = omModel.getResource(oAttribute.m_sObjectURI);
                            oAttribute.m_sReturnConfig = "YY#o:" + MetadataConstants.c_XMLE_contribution + MetadataConstants.c_XMLE_Uri;
                            resCompetence.addProperty(opContribution, resContribution.asResource());
                            if (oAttribute.m_oHasMetric != null && oAttribute.m_oHasMetric.length > 0)
                            {
                                for (Metric oMetric: oAttribute.m_oHasMetric)
                                {
                                    //    mailingListActivity
                                    if (oMetric.m_sMetricName.equals("mailingListActivity") && !oMetric.m_sMetricValue.isEmpty())
                                    {
                                        oMetric.m_sObjectURI = MetadataGlobal.GetObjectURI(omModel, MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_MailingListMetric, oMetric.m_sID);
                                        ObjectProperty opMetric = omModel.getObjectProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLObjectProperty_HasMetric);
                                        Resource resMetric = omModel.getResource(oMetric.m_sObjectURI);
                                        oMetric.m_sReturnConfig = "YN#o:mailingListActivityUri";
                                        
                                        DatatypeProperty dtpName = omModel.getDatatypeProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_MetricName);
                                        resMetric.removeAll(dtpName);
                                        resMetric.addProperty(dtpName, "mailingListActivity");
                                        DatatypeProperty dtpValue = omModel.getDatatypeProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_Level);
                                        resMetric.removeAll(dtpValue);
                                        resMetric.addProperty(dtpValue, oMetric.m_sMetricValue);
                                        
                                        resContribution.addProperty(opMetric, resMetric.asResource());
                                    }
                                    //    itsActivity
                                    if (oMetric.m_sMetricName.equals("itsActivity") && !oMetric.m_sMetricValue.isEmpty())
                                    {
                                        oMetric.m_sObjectURI = MetadataGlobal.GetObjectURI(omModel, MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_ITSMetric, oMetric.m_sID);
                                        ObjectProperty opMetric = omModel.getObjectProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLObjectProperty_HasMetric);
                                        Resource resMetric = omModel.getResource(oMetric.m_sObjectURI);
                                        oMetric.m_sReturnConfig = "YN#o:itsActivityUri";
                                        
                                        DatatypeProperty dtpName = omModel.getDatatypeProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_MetricName);
                                        resMetric.removeAll(dtpName);
                                        resMetric.addProperty(dtpName, "itsActivity");
                                        DatatypeProperty dtpValue = omModel.getDatatypeProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_Level);
                                        resMetric.removeAll(dtpValue);
                                        resMetric.addProperty(dtpValue, oMetric.m_sMetricValue);
                                        
                                        resContribution.addProperty(opMetric, resMetric.asResource());
                                    }
                                    //    sloc
                                    if (oMetric.m_sMetricName.equals("sloc") && !oMetric.m_sMetricValue.isEmpty())
                                    {
                                        oMetric.m_sObjectURI = MetadataGlobal.GetObjectURI(omModel, MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_SCMMetric, oMetric.m_sID);
                                        ObjectProperty opMetric = omModel.getObjectProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLObjectProperty_HasMetric);
                                        Resource resMetric = omModel.getResource(oMetric.m_sObjectURI);
                                        oMetric.m_sReturnConfig = "YN#o:slocUri";
                                        
                                        DatatypeProperty dtpName = omModel.getDatatypeProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_MetricName);
                                        resMetric.removeAll(dtpName);
                                        resMetric.addProperty(dtpName, "sloc");
                                        DatatypeProperty dtpValue = omModel.getDatatypeProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_Level);
                                        resMetric.removeAll(dtpValue);
                                        resMetric.addProperty(dtpValue, oMetric.m_sMetricValue);
                                        
                                        resContribution.addProperty(opMetric, resMetric.asResource());
                                    }
                                }
                            }
                        }
                        //    Effectiveness
                        if (oAttribute.m_sName.equals(MetadataConstants.c_XMLE_effectiveness))
                        {
                            oAttribute.m_sObjectURI = MetadataGlobal.GetObjectURI(omModel, MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_Effectiveness, oAttribute.m_sID);
                            ObjectProperty opEffectiveness = omModel.getObjectProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLObjectProperty_HasAttribute);
                            Resource resEffectiveness = omModel.getResource(oAttribute.m_sObjectURI);
                            oAttribute.m_sReturnConfig = "YY#o:" + MetadataConstants.c_XMLE_effectiveness + MetadataConstants.c_XMLE_Uri;
                            resCompetence.addProperty(opEffectiveness, resEffectiveness.asResource());
                            if (oAttribute.m_oHasMetric != null && oAttribute.m_oHasMetric.length > 0)
                            {
                                for (Metric oMetric: oAttribute.m_oHasMetric)
                                {
                                    //    noOfIssuesFixed
                                    if (oMetric.m_sMetricName.equals("noOfIssuesFixed") && !oMetric.m_sMetricValue.isEmpty())
                                    {
                                        oMetric.m_sObjectURI = MetadataGlobal.GetObjectURI(omModel, MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_ITSMetric, oMetric.m_sID);
                                        ObjectProperty opMetric = omModel.getObjectProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLObjectProperty_HasMetric);
                                        Resource resMetric = omModel.getResource(oMetric.m_sObjectURI);
                                        oMetric.m_sReturnConfig = "YN#o:noOfIssuesFixedUri";
                                        
                                        DatatypeProperty dtpName = omModel.getDatatypeProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_MetricName);
                                        resMetric.removeAll(dtpName);
                                        resMetric.addProperty(dtpName, "noOfIssuesFixed");
                                        DatatypeProperty dtpValue = omModel.getDatatypeProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_Level);
                                        resMetric.removeAll(dtpValue);
                                        resMetric.addProperty(dtpValue, oMetric.m_sMetricValue);
                                        
                                        resEffectiveness.addProperty(opMetric, resMetric.asResource());
                                    }
                                    //    noOfCommitsCausedIssue
                                    if (oMetric.m_sMetricName.equals("noOfCommitsCausedIssue") && !oMetric.m_sMetricValue.isEmpty())
                                    {
                                        oMetric.m_sObjectURI = MetadataGlobal.GetObjectURI(omModel, MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_ITSMetric, oMetric.m_sID);
                                        ObjectProperty opMetric = omModel.getObjectProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLObjectProperty_HasMetric);
                                        Resource resMetric = omModel.getResource(oMetric.m_sObjectURI);
                                        oMetric.m_sReturnConfig = "YN#o:noOfCommitsCausedIssueUri";
                                        
                                        DatatypeProperty dtpName = omModel.getDatatypeProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_MetricName);
                                        resMetric.removeAll(dtpName);
                                        resMetric.addProperty(dtpName, "noOfCommitsCausedIssue");
                                        DatatypeProperty dtpValue = omModel.getDatatypeProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_Level);
                                        resMetric.removeAll(dtpValue);
                                        resMetric.addProperty(dtpValue, oMetric.m_sMetricValue);
                                        
                                        resEffectiveness.addProperty(opMetric, resMetric.asResource());
                                    }
                                    //    noOfCommitFixedIssue
                                    if (oMetric.m_sMetricName.equals("noOfCommitFixedIssue") && !oMetric.m_sMetricValue.isEmpty())
                                    {
                                        oMetric.m_sObjectURI = MetadataGlobal.GetObjectURI(omModel, MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_ITSMetric, oMetric.m_sID);
                                        ObjectProperty opMetric = omModel.getObjectProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLObjectProperty_HasMetric);
                                        Resource resMetric = omModel.getResource(oMetric.m_sObjectURI);
                                        oMetric.m_sReturnConfig = "YN#o:noOfCommitFixedIssueUri";
                                        
                                        DatatypeProperty dtpName = omModel.getDatatypeProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_MetricName);
                                        resMetric.removeAll(dtpName);
                                        resMetric.addProperty(dtpName, "noOfCommitFixedIssue");
                                        DatatypeProperty dtpValue = omModel.getDatatypeProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_Level);
                                        resMetric.removeAll(dtpValue);
                                        resMetric.addProperty(dtpValue, oMetric.m_sMetricValue);
                                        
                                        resEffectiveness.addProperty(opMetric, resMetric.asResource());
                                    }
                                }
                            }
                        }
                        //    Recency
                        if (oAttribute.m_sName.equals(MetadataConstants.c_XMLE_recency))
                        {
                            oAttribute.m_sObjectURI = MetadataGlobal.GetObjectURI(omModel, MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_Activity_Availability, oAttribute.m_sID);
                            ObjectProperty opEffectiveness = omModel.getObjectProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLObjectProperty_HasAttribute);
                            Resource resEffectiveness = omModel.getResource(oAttribute.m_sObjectURI);
                            oAttribute.m_sReturnConfig = "YY#o:" + MetadataConstants.c_XMLE_recency + MetadataConstants.c_XMLE_Uri;
                            resCompetence.addProperty(opEffectiveness, resEffectiveness.asResource());
                            if (oAttribute.m_oHasMetric != null && oAttribute.m_oHasMetric.length > 0)
                            {
                                for (Metric oMetric: oAttribute.m_oHasMetric)
                                {
                                    //    timeSinceSCMaction
                                    if (oMetric.m_sMetricName.equals("timeSinceSCMaction") && !oMetric.m_sMetricValue.isEmpty())
                                    {
                                        oMetric.m_sObjectURI = MetadataGlobal.GetObjectURI(omModel, MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_TemporalMetric, oMetric.m_sID);
                                        ObjectProperty opMetric = omModel.getObjectProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLObjectProperty_HasMetric);
                                        Resource resMetric = omModel.getResource(oMetric.m_sObjectURI);
                                        oMetric.m_sReturnConfig = "YN#o:timeSinceSCMactionUri";
                                        
                                        DatatypeProperty dtpName = omModel.getDatatypeProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_MetricName);
                                        resMetric.removeAll(dtpName);
                                        resMetric.addProperty(dtpName, "timeSinceSCMaction");
                                        DatatypeProperty dtpValue = omModel.getDatatypeProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_Time);
                                        resMetric.removeAll(dtpValue);
                                        resMetric.addProperty(dtpValue, MetadataGlobal.GetDateTime(oMetric.m_sMetricValue).toString());
                                        
                                        resEffectiveness.addProperty(opMetric, resMetric.asResource());
                                    }
                                    //    timeSinceITSaction
                                    if (oMetric.m_sMetricName.equals("timeSinceITSaction") && !oMetric.m_sMetricValue.isEmpty())
                                    {
                                        oMetric.m_sObjectURI = MetadataGlobal.GetObjectURI(omModel, MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_TemporalMetric, oMetric.m_sID);
                                        ObjectProperty opMetric = omModel.getObjectProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLObjectProperty_HasMetric);
                                        Resource resMetric = omModel.getResource(oMetric.m_sObjectURI);
                                        oMetric.m_sReturnConfig = "YN#o:timeSinceITSactionUri";
                                        
                                        DatatypeProperty dtpName = omModel.getDatatypeProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_MetricName);
                                        resMetric.removeAll(dtpName);
                                        resMetric.addProperty(dtpName, "timeSinceITSaction");
                                        DatatypeProperty dtpValue = omModel.getDatatypeProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_Time);
                                        resMetric.removeAll(dtpValue);
                                        resMetric.addProperty(dtpValue, MetadataGlobal.GetDateTime(oMetric.m_sMetricValue).toString());
                                        
                                        resEffectiveness.addProperty(opMetric, resMetric.asResource());
                                    }
                                    //    timeSinceMLaction
                                    if (oMetric.m_sMetricName.equals("timeSinceMLaction") && !oMetric.m_sMetricValue.isEmpty())
                                    {
                                        oMetric.m_sObjectURI = MetadataGlobal.GetObjectURI(omModel, MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_TemporalMetric, oMetric.m_sID);
                                        ObjectProperty opMetric = omModel.getObjectProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLObjectProperty_HasMetric);
                                        Resource resMetric = omModel.getResource(oMetric.m_sObjectURI);
                                        oMetric.m_sReturnConfig = "YN#o:timeSinceMLactionUri";
                                        
                                        DatatypeProperty dtpName = omModel.getDatatypeProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_MetricName);
                                        resMetric.removeAll(dtpName);
                                        resMetric.addProperty(dtpName, "timeSinceMLaction");
                                        DatatypeProperty dtpValue = omModel.getDatatypeProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_Time);
                                        resMetric.removeAll(dtpValue);
                                        resMetric.addProperty(dtpValue, MetadataGlobal.GetDateTime(oMetric.m_sMetricValue).toString());
                                        
                                        resEffectiveness.addProperty(opMetric, resMetric.asResource());
                                    }
                                }
                            }
                        }
                    }
                }
            }

            MetadataGlobal.SaveOWL(omModel, MetadataConstants.sLocationSaveAlert);
        }
        catch (Exception e)
        {
        }
        
        return oCompetence;
    }
    
    /**
     * @summary Save identiry data
     * @startRealisation Sasa Stojanovic 04.02.2012.
     * @finalModification Sasa Stojanovic 04.02.2012.
     * @param oIdentities - identity objects with data
     * @return identity objects with uri-s
     */
    public static Identity[] SaveIdentity(Identity[] oIdentities)
    {
        try {
            
            OntModel oModel = MetadataGlobal.LoadOWL(MetadataConstants.sLocationLoadAlert);
            
            for (int i = 0; i < oIdentities.length; i++)
            {
                oIdentities[i].m_sObjectURI = MetadataGlobal.GetObjectURI(oModel, MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_Identity, oIdentities[i].m_sID);
                Resource resIdentity = oModel.getResource(oIdentities[i].m_sObjectURI);
                oIdentities[i].m_sReturnConfig = "YY#o:" + MetadataConstants.c_XMLE_identity + "/o:" + MetadataConstants.c_XMLE_identity + MetadataConstants.c_XMLE_Uri;

                if (oIdentities[i].m_bIsRemoveAll)
                {
                    ObjectProperty opIsPerson = oModel.getObjectProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLObjectProperty_IsPerson);
                    resIdentity.removeAll(opIsPerson);
                }
                
                if (oIdentities[i].m_bIsntRemoveAll)
                {
                    ObjectProperty opIsntPerson = oModel.getObjectProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLObjectProperty_IsntPerson);
                    resIdentity.removeAll(opIsntPerson);
                }
                
                //isPerson
                if (oIdentities[i].m_oIs != null)
                {
                    ObjectProperty opIsPerson = oModel.getObjectProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLObjectProperty_IsPerson);
                    for (int j = 0; j < oIdentities[i].m_oIs.length; j++)
                    {
                        if (oIdentities[i].m_oIs[j] != null && !oIdentities[i].m_oIs[j].m_sObjectURI.isEmpty())
                        {
                            if (!oIdentities[i].m_oIs[j].m_bRemoved)
                            {
                                Resource resIsPerson = oModel.getResource(oIdentities[i].m_oIs[j].m_sObjectURI);
                                resIdentity.addProperty(opIsPerson, resIsPerson.asResource());
                                //oIdentities[i].m_oIs[j].m_sReturnConfig = "YN#o:" + MetadataConstants.c_XMLE_is + MetadataConstants.c_XMLE_Uri;
                            }
                            else
                            {
                                Resource resIsRemove = oModel.getResource(oIdentities[i].m_oIs[j].m_sObjectURI);
                                oModel.removeAll(resIdentity, opIsPerson, resIsRemove);
                                //oIdentities[i].m_oIsnt[j].m_sReturnConfig = "YN#o:" + MetadataConstants.c_XMLE_isRemoved + MetadataConstants.c_XMLE_Uri;
                            }
                        }
                    }
                }
                
                //isntPerson
                if (oIdentities[i].m_oIsnt != null)
                {
                    ObjectProperty opIsntPerson = oModel.getObjectProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLObjectProperty_IsntPerson);
                    for (int j = 0; j < oIdentities[i].m_oIsnt.length; j++)
                    {
                        if (oIdentities[i].m_oIsnt[j] != null && !oIdentities[i].m_oIsnt[j].m_sObjectURI.isEmpty())
                        {
                            if (!oIdentities[i].m_oIsnt[j].m_bRemoved)
                            {
                                Resource resIsPerson = oModel.getResource(oIdentities[i].m_oIsnt[j].m_sObjectURI);
                                resIdentity.addProperty(opIsntPerson, resIsPerson.asResource());
                                //oIdentities[i].m_oIsnt[j].m_sReturnConfig = "YN#o:" + MetadataConstants.c_XMLE_isnt + MetadataConstants.c_XMLE_Uri;
                            }
                            else
                            {
                                Resource resIsRemove = oModel.getResource(oIdentities[i].m_oIsnt[j].m_sObjectURI);
                                oModel.removeAll(resIdentity, opIsntPerson, resIsRemove);
                                //oIdentities[i].m_oIsnt[j].m_sReturnConfig = "YN#o:" + MetadataConstants.c_XMLE_isntRemoved + MetadataConstants.c_XMLE_Uri;
                            }
                        }
                    }
                }
                
                //remove identity
                if (oIdentities[i].m_bRemoved)
                {
                    resIdentity.removeProperties();
                }
            }

            
            //save data
            MetadataGlobal.SaveOWL(oModel, MetadataConstants.sLocationSaveAlert);
            
        }
        catch (Exception ex)
        {
        }
        return oIdentities;
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
            OntResource resIssue = oModel.getOntResource(sIssueUri);
            StmtIterator siProperties = resIssue.listProperties();
            
            String sIssueAnnotationStatus = "false";
            while (siProperties.hasNext())
            {
                //if annotation property comment exists
                String sPropertyURI = siProperties.next().getPredicate().getURI();
                if (sPropertyURI.equals(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLAnnotationProperty_apSubject)
                 || sPropertyURI.equals(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLAnnotationProperty_apDescription))
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
     * @summary issue_getInfo
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
            
            //Annotations
            String sAQuery = "SELECT ?annotationUri ?annotationName ?annotationText WHERE "
                + "{?annotationUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLObjectProperty_HasObject + "> <" + sIssueUri + "> ."
                + " ?annotationUri <" + MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLDataProperty_Name + "> ?annotationName ."
                + " ?annotationUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_Text + "> ?annotationText}";
//            String sAQuery = "SELECT ?annotationUri ?annotationName ?annotationText WHERE "
//                + "{?annotationUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLObjectProperty_HasObject + "> <" + sIssueUri + ">}";
//            
//            String sAQuery = "SELECT ?annotationUri ?annotationName ?annotationText WHERE "
//                + "{<"+ sIssueUri + "> <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLObjectProperty_HasObject + "> ?annotationUri ."
//                + " ?annotationUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_Name + "> ?annotationName ."
//                + " ?annotationUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_Text + "> ?annotationText}";
            ResultSet rsAnnotations = QueryExecutionFactory.create(sAQuery, oModel).execSelect();

            MetadataGlobal.APIResponseData oAnnotations = new MetadataGlobal.APIResponseData();
            oAnnotations.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_annotations + "/";

            while (rsAnnotations.hasNext())
            {
                QuerySolution qsAnnotation = rsAnnotations.nextSolution();

                String sAnnotationUri = qsAnnotation.get("?annotationUri").toString();
                String sAnnotationName = qsAnnotation.get("?annotationName").toString();
                String sAnnotationText = qsAnnotation.get("?annotationText").toString();
                String sConceptName = GetConceptName(sAnnotationName);

                MetadataGlobal.APIResponseData oAnnotationText = new MetadataGlobal.APIResponseData();
                oAnnotationText.sReturnConfig = "s3:" + sAnnotationName;
                oAnnotationText.sData = String.format("<![CDATA[%s]]>", sAnnotationText);
                oAnnotations.oData.add(oAnnotationText);

                //Concepts
                String sCQuery = "SELECT ?conceptUri ?conceptDataUri ?conceptDataCount WHERE "
                + "{<" + sAnnotationUri + "> <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLObjectProperty_HasConcepts + "> ?conceptUri ."
                + " ?conceptUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_Uri + "> ?conceptDataUri ."
                + " ?conceptUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_Count + "> ?conceptDataCount}";
//                String sCQuery = "SELECT ?conceptUri ?conceptDataUri ?conceptDataCount WHERE "
//                + "{<" + sAnnotationUri + "> <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLObjectProperty_HasConcepts + "> ?conceptUri}";
                ResultSet rsConcepts = QueryExecutionFactory.create(sCQuery, oModel).execSelect();

                MetadataGlobal.APIResponseData oConcept = new MetadataGlobal.APIResponseData();
                oConcept.sReturnConfig = "s3:" + sConceptName + "/";
                oAnnotations.oData.add(oConcept);

                while (rsConcepts.hasNext())
                {
                    QuerySolution qsConcept = rsConcepts.nextSolution();

                    //String sConceptUri = qsConcept.get("?conceptUri").toString();
                    String sConceptDataUri = qsConcept.get("?conceptDataUri").toString();
                    String sConceptDataCount = qsConcept.get("?conceptDataCount").toString();

                    MetadataGlobal.APIResponseData oConceptXml = new MetadataGlobal.APIResponseData();
                    oConceptXml.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_concept + "/";

                    MetadataGlobal.APIResponseData oConceptDataUri = new MetadataGlobal.APIResponseData();
                    oConceptDataUri.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_uri;
                    oConceptDataUri.sData = sConceptDataUri;
                    oConceptXml.oData.add(oConceptDataUri);

                    MetadataGlobal.APIResponseData oConceptDataCount = new MetadataGlobal.APIResponseData();
                    oConceptDataCount.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_count;
                    oConceptDataCount.sData = sConceptDataCount;
                    oConceptXml.oData.add(oConceptDataCount);

                    oConcept.oData.add(oConceptXml);
                }
            }
            //oIssueData.oData.add(oAnnotations);
            if (oAnnotations.sReturnConfig != null)
                oData.oData.add(oAnnotations);
        }
        catch (Exception e)
        {
        }
        return oData;
    }
    
    /**
     * @summary issue_getExplicitDuplicates
     * @startRealisation Sasa Stojanovic 15.12.2011.
     * @finalModification Sasa Stojanovic 15.12.2011.
     * @param sIssueUri - issue uri
     * @return - APIResponseData object with results
     */
    public static MetadataGlobal.APIResponseData ac_issue_getExplicitDuplicates(String sIssueUri)
    {
        MetadataGlobal.APIResponseData oData = new MetadataGlobal.APIResponseData();
        try
        {
            OntModel oModel = MetadataGlobal.LoadOWLWithModelSpec(MetadataConstants.sLocationLoadAlert, OntModelSpec.OWL_MEM_MICRO_RULE_INF);
                        
            String sQuery = "SELECT ?issueUri WHERE {?issueUri a <" + MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLClass_Issue + "> . "
                    + "?issueUri <" + MetadataConstants.c_NS_Alert_Its + MetadataConstants.c_OWLObjectProperty_IsDuplicateOf + "> <" + sIssueUri + "> }";
                            
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
     * @summary issue_getSubjectAreas
     * @startRealisation  Dejan Milosavljevic 22.02.2012.
     * @finalModification Dejan Milosavljevic 22.02.2012.
     * @param sIssueUri - issue uri
     * @return - APIResponseData object with results
     */
    public static MetadataGlobal.APIResponseData ac_issue_getSubjectAreas(String sIssueUri)
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
                if (sProperty.equals(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_URL))
                {
                    oIssueData.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_issueUrl;
                    oIssueData.sData = sStatement.getObject().toString();
                }
                if (sProperty.equals(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_ID))
                {
                    oIssueData.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_issue + MetadataConstants.c_XMLE_id;
                    oIssueData.sData = sStatement.getObject().toString();
                }
                if (sProperty.equals(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLDataProperty_Description))
                {
                    oIssueData.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_issueDescription;
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
     * @summary issue_getSubjectAreasForOpen
     * @startRealisation  Dejan Milosavljevic 22.02.2012.
     * @finalModification Dejan Milosavljevic 22.02.2012.
     * @param sProductUri - product URI
     * @return - APIResponseData object with results
     */
    public static MetadataGlobal.APIResponseData ac_issue_getSubjectAreasForOpen(String sProductUri)
    {
        MetadataGlobal.APIResponseData oData = new MetadataGlobal.APIResponseData();
        try
        {
            OntModel oModel = MetadataGlobal.LoadOWL(MetadataConstants.sLocationLoadAlert);
            String sStateOpenUri = MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLClass_Open;
                    
            String sQuery = "SELECT ?issueUri ?issueUrl ?issueId ?issueDescription WHERE "
                    + "{?issueUri <" + MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLObjectProperty_IsIssueOf + ">  ?componentUri ."
                    + " ?componentUri  <" + MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLObjectProperty_IsComponentOf + "> <" + sProductUri + "> ."
                    + " ?issueUri <" + MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLObjectProperty_HasState + "> <" + sStateOpenUri + "> ."
                    + " ?issueUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_ID + "> ?issueId ."
                    + " ?issueUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_URL + "> ?issueUrl ."
                    + " ?issueUri <" + MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLDataProperty_Description + "> ?issueDescription . }";
                        
            ResultSet rsIssue = QueryExecutionFactory.create(sQuery, oModel).execSelect();
            
            while (rsIssue.hasNext())
            {
                QuerySolution qsIssue = rsIssue.nextSolution();
                
                MetadataGlobal.APIResponseData oIssue = new MetadataGlobal.APIResponseData();
                MetadataGlobal.APIResponseData oIssueUri = new MetadataGlobal.APIResponseData();
                MetadataGlobal.APIResponseData oIssueUrl = new MetadataGlobal.APIResponseData();
                MetadataGlobal.APIResponseData oIssueId = new MetadataGlobal.APIResponseData();
                MetadataGlobal.APIResponseData oIssueDescription = new MetadataGlobal.APIResponseData();
                
                oIssue.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_issue + "/";
                oIssueUri.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_issue + MetadataConstants.c_XMLE_Uri;
                oIssueUrl.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_issueUrl;
                oIssueId.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_issue + MetadataConstants.c_XMLE_Id;
                oIssueDescription.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_issueDescription;
                
                oIssueUri.sData = qsIssue.get("?issueUri").toString();
                oIssueUrl.sData = qsIssue.get("?issueUrl").toString();
                oIssueId.sData = qsIssue.get("?issueId").toString();
                oIssueDescription.sData = qsIssue.get("?issueDescription").toString();
                
                oIssue.oData.add(oIssueUri);
                oIssue.oData.add(oIssueUrl);
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
     * @summary person_getInfo
     * @startRealisation Sasa Stojanovic 18.01.2012.
     * @finalModification Sasa Stojanovic 18.01.2012.
     * @param sPersonUri - person URI
     * @return - APIResponseData object with results
     */
    public static MetadataGlobal.APIResponseData ac_person_getInfo(String sPersonUri)
    {
        MetadataGlobal.APIResponseData oData = new MetadataGlobal.APIResponseData();
        try
        {
            OntModel oModel = MetadataGlobal.LoadOWL(MetadataConstants.sLocationLoadAlert);
            OntResource resPerson = oModel.getOntResource(sPersonUri);
            StmtIterator siProperties = resPerson.listProperties();
            while (siProperties.hasNext())
            {
                Statement sStatement = siProperties.next();
                String sProperty = sStatement.getPredicate().getURI();
                
                MetadataGlobal.APIResponseData oPersonData = new MetadataGlobal.APIResponseData();
                
                if (sProperty.equals(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_ID))
                {
                    oPersonData.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_person + MetadataConstants.c_XMLE_id;
                    oPersonData.sData = sStatement.getObject().toString();
                }
                if (sProperty.equals(MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLDataProperty_Email))
                {
                    oPersonData.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_personEmail;
                    oPersonData.sData = sStatement.getObject().toString();
                }
                if (sProperty.equals(MetadataConstants.c_NS_foaf + MetadataConstants.c_OWLDataProperty_FirstName))
                {
                    oPersonData.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_personFirstName;
                    oPersonData.sData = sStatement.getObject().toString();
                }
                if (sProperty.equals(MetadataConstants.c_NS_foaf + MetadataConstants.c_OWLDataProperty_LastName))
                {
                    oPersonData.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_personLastName;
                    oPersonData.sData = sStatement.getObject().toString();
                }
                
                if (oPersonData.sReturnConfig != null)
                    oData.oData.add(oPersonData);
            }
            
            //spajanje imena i prezimena u jedan objekat
            String sName = "";
            for (int i = 0; i < oData.oData.size(); i++)
            {
                if (oData.oData.get(i).sReturnConfig.equals("s3:" + MetadataConstants.c_XMLE_personFirstName))
                {
                    sName = oData.oData.get(i).sData + sName;
                    oData.oData.remove(i);
                }
                if (oData.oData.get(i).sReturnConfig.equals("s3:" + MetadataConstants.c_XMLE_personLastName))
                {
                    sName = sName + " " + oData.oData.get(i).sData;
                    oData.oData.remove(i);
                }
            }
            if (!sName.isEmpty())
            {
                MetadataGlobal.APIResponseData oName = new MetadataGlobal.APIResponseData();
                oName.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_personName;
                oName.sData = sName;
                oData.oData.add(oName);
            }
        }
        catch (Exception e)
        {
        }
        return oData;
    }
    
    /**
     * @summary person_getAllForEmail
     * @startRealisation Sasa Stojanovic 18.01.2012.
     * @finalModification Sasa Stojanovic 18.01.2012.
     * @param sEmail - person email
     * @return - APIResponseData object with results
     */
    public static MetadataGlobal.APIResponseData ac_person_getAllForEmail(String sEmail)
    {
        MetadataGlobal.APIResponseData oData = new MetadataGlobal.APIResponseData();
        try
        {
            OntModel oModel = MetadataGlobal.LoadOWL(MetadataConstants.sLocationLoadAlert);
                        
            String sQuery = "SELECT ?personUri ?personFirstName ?personLastName WHERE {?personUri a <" + MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLClass_Person + "> . "
                    + "?personUri <" + MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLDataProperty_Email + "> ?email . "
                    + "FILTER (?email = \"" + sEmail + "\") . "
                    + "?personUri <" + MetadataConstants.c_NS_foaf + MetadataConstants.c_OWLDataProperty_FirstName + "> ?personFirstName . "
                    + "?personUri <" + MetadataConstants.c_NS_foaf + MetadataConstants.c_OWLDataProperty_LastName + "> ?personLastName}";                         
            ResultSet rsPerson = QueryExecutionFactory.create(sQuery, oModel).execSelect();
            
            while (rsPerson.hasNext())
            {
                QuerySolution qsPerson = rsPerson.nextSolution();
                
                MetadataGlobal.APIResponseData oPerson = new MetadataGlobal.APIResponseData();
                oPerson.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_person + "/";
                
                MetadataGlobal.APIResponseData oPersonUri = new MetadataGlobal.APIResponseData();
                oPersonUri.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_person + MetadataConstants.c_XMLE_Uri;
                oPersonUri.sData = qsPerson.get("?personUri").toString();
                oPerson.oData.add(oPersonUri);
                
                MetadataGlobal.APIResponseData oPersonName = new MetadataGlobal.APIResponseData();
                oPersonName.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_personName;
                oPersonName.sData = qsPerson.get("?personFirstName").toString() + " " + qsPerson.get("?personLastName").toString();
                oPerson.oData.add(oPersonName);

                oData.oData.add(oPerson);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return oData;
    }
    
    /**
     * @summary issue_getRelatedToKeyword
     * @startRealisation Sasa Stojanovic 18.01.2012.
     * @finalModification Sasa Stojanovic 18.01.2012.
     * @param sFirstName - person first name
     * @param sLastName - person last name
     * @param sEmail - person email
     * @return - APIResponseData object with results
     */
    public static MetadataGlobal.APIResponseData ac_identity_getForPerson(String sFirstName, String sLastName, String sEmail)
    {
        MetadataGlobal.APIResponseData oData = new MetadataGlobal.APIResponseData();
        try
        {
            OntModel oModel = MetadataGlobal.LoadOWL(MetadataConstants.sLocationLoadAlert);
            
            if (!sFirstName.isEmpty() || !sLastName.isEmpty() || !sEmail.isEmpty())
            {
                //if person has same data as identity
                String sQuery = "SELECT ?personUri WHERE {?personUri a <" + MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLClass_Person + "> . ";
                
                if (!sFirstName.isEmpty())
                    sQuery += "?personUri <" + MetadataConstants.c_NS_foaf + MetadataConstants.c_OWLDataProperty_FirstName + "> ?firstName . FILTER regex(?firstName, \"" + sFirstName + "\", \"i\") . ";
                            
                if (!sLastName.isEmpty())
                    sQuery += "?personUri <" + MetadataConstants.c_NS_foaf + MetadataConstants.c_OWLDataProperty_LastName + "> ?lastName . FILTER regex(?lastName, \"" + sLastName + "\", \"i\") . ";
                
                if (!sEmail.isEmpty())
                    sQuery += "?personUri <" + MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLDataProperty_Email + "> ?email . FILTER regex(?email, \"" + sEmail + "\", \"i\") . ";
                
                sQuery += "}";

                ResultSet rsPerson = QueryExecutionFactory.create(sQuery, oModel).execSelect();

                while (rsPerson.hasNext())
                {
                    QuerySolution qsPerson = rsPerson.nextSolution();

                    MetadataGlobal.APIResponseData oPersonUri = new MetadataGlobal.APIResponseData();
                    oPersonUri.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_person + MetadataConstants.c_XMLE_Uri;
                    oPersonUri.sData = qsPerson.get("?personUri").toString();

                    oData.oData.add(oPersonUri);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return oData;
    }
    
    /**
     * @summary competency_getForPerson
     * @startRealisation  Dejan Milosavljevic 04.02.2012.
     * @finalModification Dejan Milosavljevic 04.02.2012.
     * @param sPersonUri - person URI
     * @return - APIResponseData object with results
     */
    public static MetadataGlobal.APIResponseData ac_competency_getForPerson(String sPersonUri)
    {
        MetadataGlobal.APIResponseData oData = new MetadataGlobal.APIResponseData();
        try
        {
            OntModel omModel = MetadataGlobal.LoadOWL(MetadataConstants.sLocationLoadAlert);
                        
            String sQuery = "SELECT ?competencyUri WHERE "
                    + "{?identityUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLObjectProperty_HasCompetences + ">  ?competencyUri ."
                    + " ?identityUri  <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLObjectProperty_IsPerson + "> <" + sPersonUri + "> . }";

            ResultSet rsCompetence = QueryExecutionFactory.create(sQuery, omModel).execSelect();
            
            while (rsCompetence.hasNext())
            {
                QuerySolution qsCompetence = rsCompetence.nextSolution();
                String sCUri = qsCompetence.get("?competencyUri").toString();
                OntResource resCompetance = omModel.getOntResource(sCUri);
                
                MetadataGlobal.APIResponseData oAreas = new MetadataGlobal.APIResponseData();
                oAreas.sReturnConfig = "s3:areas/";
                MetadataGlobal.APIResponseData oArea = new MetadataGlobal.APIResponseData();
                oArea.sReturnConfig = "s3:area/";
                
                //Annotations
                MetadataGlobal.APIResponseData oAnnotations = new MetadataGlobal.APIResponseData();
                oAnnotations.sReturnConfig = "s3:annotations/";
                oArea.oData.add(oAnnotations);
                
                MetadataGlobal.APIResponseData oAttributes = new MetadataGlobal.APIResponseData();
                oAttributes.sReturnConfig = "s3:attributs/";
                
                StmtIterator siProperties = resCompetance.listProperties();
                while (siProperties.hasNext())
                {
                    Statement sStatement = siProperties.next();
                    String sProperty = sStatement.getPredicate().getURI();
                    boolean bIsLevel = true;
                    //MetadataGlobal.APIResponseData oCompetenceData = new MetadataGlobal.APIResponseData();
                    
                    if (sProperty.equals(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_HasLevel))
                    {
                        MetadataGlobal.APIResponseData oIndex = new MetadataGlobal.APIResponseData();
                        oIndex.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_index;
                        oIndex.sData = sStatement.getObject().toString();
                        oArea.oData.add(oIndex);
                    }
                    if (sProperty.equals(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLObjectProperty_HasAttribute))
                    {
                        MetadataGlobal.APIResponseData oAttribute = new MetadataGlobal.APIResponseData();
                        String sAttributUri = sStatement.getObject().toString();

                        Individual inAttribut = omModel.getIndividual(sAttributUri);
                        OntClass oClass = inAttribut.getOntClass(true);

                        if (oClass.hasURI(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_Fluency))
                        {
                            oAttribute.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_fluency + "/";
                        }
                        if (oClass.hasURI(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_Contribution))
                        {
                            oAttribute.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_fluency + "/";
                        }
                        if (oClass.hasURI(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_Effectiveness))
                        {
                            oAttribute.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_fluency + "/";
                        }
                        if (oClass.hasURI(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_Activity_Availability))
                        {
                            oAttribute.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_fluency + "/";
                            bIsLevel = false;
                        }
                        
                        if (!oAttribute.sReturnConfig.isEmpty())
                        {
                            if (bIsLevel)
                            {
                                String sQueryMet = "SELECT ?metricUri ?metricName ?level WHERE "
                                    + "{<" + sAttributUri + "> <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLObjectProperty_HasMetric + "> ?metricUri . "
                                    + " ?metricUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_MetricName + "> ?metricName . "
                                    + " ?metricUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_Level + "> ?level}";
                                ResultSet rsMetric = QueryExecutionFactory.create(sQueryMet, omModel).execSelect();

                                while (rsMetric.hasNext())
                                {
                                    QuerySolution qsMetric = rsMetric.nextSolution();
                                    MetadataGlobal.APIResponseData oMetric = new MetadataGlobal.APIResponseData();
                                    oMetric.sReturnConfig = "s3:" + qsMetric.get("?metricName").toString();
                                    oMetric.sData = qsMetric.get("?level").toString();
                                    
                                    oAttribute.oData.add(oMetric);
                                }
                            }
                            else
                            {
                                String sQueryMet = "SELECT ?metricUri ?metricName ?time WHERE "
                                    + "{<" + sAttributUri + "> <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLObjectProperty_HasMetric + "> ?metricUri . "
                                    + " ?metricUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_MetricName + "> ?metricName . "
                                    + " ?metricUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_Time + "> ?time}";
                                ResultSet rsMetric = QueryExecutionFactory.create(sQueryMet, omModel).execSelect();

                                while (rsMetric.hasNext())
                                {
                                    QuerySolution qsMetric = rsMetric.nextSolution();
                                    MetadataGlobal.APIResponseData oMetric = new MetadataGlobal.APIResponseData();
                                    oMetric.sReturnConfig = "s3:" + qsMetric.get("?metricName").toString();
                                    oMetric.sData = qsMetric.get("?time").toString();
                                    
                                    oAttribute.oData.add(oMetric);
                                }
                            }
                        }
                    }
                }

                if (oAttributes.sData != null) oArea.oData.add(oAttributes);
                oAreas.oData.add(oArea);
                oData.oData.add(oAreas);
            }
        }
        catch (Exception e)
        {
        }
        return oData;
    }
    
    /**
     * @summary competency_getPersonForIssue
     * @startRealisation Sasa Stojanovic 18.01.2012.
     * @finalModification Sasa Stojanovic 18.01.2012.
     * @param sPersonForIssueSPARQL - SPARQL query
     * @return - APIResponseData object with results
     */
    public static MetadataGlobal.APIResponseData ac_competency_getPersonForIssue(String sPersonForIssueSPARQL)
    {
        MetadataGlobal.APIResponseData oData = new MetadataGlobal.APIResponseData();
        try
        {
            OntModel oModel = MetadataGlobal.LoadOWLWithModelSpec(MetadataConstants.sLocationLoadAlert, OntModelSpec.OWL_MEM_MICRO_RULE_INF);
                        
            String sQuery = "SELECT ?personUri WHERE {?personUri a <" + MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLClass_Person + "> . " + sPersonForIssueSPARQL + "}";
                            
            ResultSet rsPerson = QueryExecutionFactory.create(sQuery, oModel).execSelect();
            
            while (rsPerson.hasNext())
            {
                QuerySolution qsPerson = rsPerson.nextSolution();
                
                MetadataGlobal.APIResponseData oPersonUri = new MetadataGlobal.APIResponseData();
                oPersonUri.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_person + MetadataConstants.c_XMLE_Uri;
                oPersonUri.sData = qsPerson.get("?personUri").toString();

                oData.oData.add(oPersonUri);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return oData;
    }
    
    /**
     * @summary method_getAllForPerson
     * @startRealisation Sasa Stojanovic 18.01.2012.
     * @finalModification Sasa Stojanovic 18.01.2012.
     * @param sPersonUri - person uri
     * @return - APIResponseData object with results
     */
    public static MetadataGlobal.APIResponseData ac_method_getAllForPerson(String sPersonUri)
    {
        MetadataGlobal.APIResponseData oData = new MetadataGlobal.APIResponseData();
        try
        {
            OntModel oModel = MetadataGlobal.LoadOWL(MetadataConstants.sLocationLoadAlert);
                     
            String sQuery = "SELECT DISTINCT ?methodUri WHERE {"
                    + "{ ?methodUri a <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_Method + "> . "
                    + "?moduleUri <" + MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLObjectProperty_HasMethods + "> ?methodUri . "
                    + "?fileUri <" + MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLObjectProperty_HasModules + "> ?moduleUri . "
                    + "?commitUri <" + MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLObjectProperty_HasFile + "> ?fileUri . "
                    + "?commitUri <" + MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLObjectProperty_HasAuthor + "> <" + sPersonUri + "> }"
                    + " UNION "
                    + "{ ?methodUri a <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_Method + "> . "
                    + "?moduleUri <" + MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLObjectProperty_HasMethods + "> ?methodUri . "
                    + "?fileUri <" + MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLObjectProperty_HasModules + "> ?moduleUri . "
                    + "?commitUri <" + MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLObjectProperty_HasFile + "> ?fileUri . "
                    + "?commitUri <" + MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLObjectProperty_HasCommitter + "> <" + sPersonUri + "> }"
                    + "}";
                            
            ResultSet rsMethod = QueryExecutionFactory.create(sQuery, oModel).execSelect();
            
            while (rsMethod.hasNext())
            {
                QuerySolution qsMethod = rsMethod.nextSolution();
                
                MetadataGlobal.APIResponseData oMethodUri = new MetadataGlobal.APIResponseData();
                oMethodUri.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_method + MetadataConstants.c_XMLE_Uri;
                oMethodUri.sData = qsMethod.get("?methodUri").toString();

                oData.oData.add(oMethodUri);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return oData;
    }
    
    /**
     * @summary method_getRelatedCode
     * @startRealisation Sasa Stojanovic 18.01.2012.
     * @finalModification Sasa Stojanovic 18.01.2012.
     * @param sPersonUri - person uri
     * @param sProductUri - product uri
     * @return - APIResponseData object with results
     */
    public static MetadataGlobal.APIResponseData ac_method_getRelatedCode(String sPersonUri, String sProductUri)
    {
        MetadataGlobal.APIResponseData oData = new MetadataGlobal.APIResponseData();
        try
        {
            OntModel oModel = MetadataGlobal.LoadOWL(MetadataConstants.sLocationLoadAlert);
                        
            String sQuery = "SELECT DISTINCT ?methodUri WHERE {"
                    + "{ ?methodUri a <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_Method + "> . "
                    + "?issueUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLObjectProperty_RelatedToSourceCode + "> ?methodUri . "
                    + "?issueUri <" + MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLObjectProperty_IsIssueOf + ">  ?componentUri . "
                    + "?componentUri  <" + MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLObjectProperty_IsComponentOf + "> <" + sProductUri + "> . "
                    + "?moduleUri <" + MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLObjectProperty_HasMethods + "> ?methodUri . "
                    + "?fileUri <" + MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLObjectProperty_HasModules + "> ?moduleUri . "
                    + "?commitUri <" + MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLObjectProperty_HasFile + "> ?fileUri . "
                    + "?commitUri <" + MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLObjectProperty_HasAuthor + "> <" + sPersonUri + "> }"
                    + " UNION "
                    + "{ ?methodUri a <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_Method + "> . "
                    + "?issueUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLObjectProperty_RelatedToSourceCode + "> ?methodUri . "
                    + "?issueUri <" + MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLObjectProperty_IsIssueOf + ">  ?componentUri . "
                    + "?componentUri  <" + MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLObjectProperty_IsComponentOf + "> <" + sProductUri + "> . "
                    + "?moduleUri <" + MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLObjectProperty_HasMethods + "> ?methodUri . "
                    + "?fileUri <" + MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLObjectProperty_HasModules + "> ?moduleUri . "
                    + "?commitUri <" + MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLObjectProperty_HasFile + "> ?fileUri . "
                    + "?commitUri <" + MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLObjectProperty_HasCommitter + "> <" + sPersonUri + "> }"
                    + "}";
                            
            ResultSet rsMethod = QueryExecutionFactory.create(sQuery, oModel).execSelect();
            
            while (rsMethod.hasNext())
            {
                QuerySolution qsMethod = rsMethod.nextSolution();
                
                MetadataGlobal.APIResponseData oMethodUri = new MetadataGlobal.APIResponseData();
                oMethodUri.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_method + MetadataConstants.c_XMLE_Uri;
                oMethodUri.sData = qsMethod.get("?methodUri").toString();

                oData.oData.add(oMethodUri);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return oData;
    }
            
    /**
     * @summary issue_getRelatedToKeyword
     * @startRealisation Sasa Stojanovic 17.01.2012.
     * @finalModification Sasa Stojanovic 17.01.2012.
     * @param sKeyword - keyword to search for
     * @return - APIResponseData object with results
     */
    public static MetadataGlobal.APIResponseData ac_issue_getRelatedToKeyword(String sKeyword)
    {
        MetadataGlobal.APIResponseData oData = new MetadataGlobal.APIResponseData();
        try
        {
            OntModel oModel = MetadataGlobal.LoadOWLWithModelSpec(MetadataConstants.sLocationLoadAlert, OntModelSpec.OWL_MEM_MICRO_RULE_INF);
            
            //if keyword exists in description/subject annotation or in issue keyword
            String sQuery = "SELECT ?issueUri WHERE {"
                    + "{?issueUri a <" + MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLClass_Issue + "> . "
                    + "?issueUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLAnnotationProperty_apDescription + "> ?description . FILTER regex(?description, \"" + sKeyword + "\", \"i\")}"
                    + " UNION "
                    + "{?issueUri a <" + MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLClass_Issue + "> . "
                    + "?issueUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLAnnotationProperty_apSubject + "> ?subject . FILTER regex(?subject, \"" + sKeyword + "\", \"i\")}"
                    + " UNION "
                    + "{?issueUri a <" + MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLClass_Issue + "> . "
                    + "?issueUri <" + MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLDataProperty_Keyword + "> ?keyword . FILTER regex(?keyword, \"" + sKeyword + "\", \"i\")}"
                    + "}";
                            
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
            e.printStackTrace();
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
                    + "?commitUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLAnnotationProperty_apCommit + "> ?comment . FILTER regex(?comment, \"" + sKeyword + "\", \"i\")}"
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
     * @summary file_getAll
     * @startRealisation  Dejan Milosavljevic 23.02.2012.
     * @finalModification Dejan Milosavljevic 23.02.2012.
     * @param sKeyword - keyword to search for
     * @return - APIResponseData object with results
     */
    public static MetadataGlobal.APIResponseData ac_file_getAll(String sOffset, String sCount)
    {
        MetadataGlobal.APIResponseData oData = new MetadataGlobal.APIResponseData();
        try
        {
            OntModel oModel = MetadataGlobal.LoadOWL(MetadataConstants.sLocationLoadAlert);
            
            //if files exist in ontology
            String sQuery = "SELECT ?fileUri WHERE "
                    + "{?fileUri a <" + MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLClass_File + ">} "
                    + "ORDER BY ?fileUri "
                    + "LIMIT " + sCount + " OFFSET " + sOffset;
                            
            ResultSet rsFile = QueryExecutionFactory.create(sQuery, oModel).execSelect();
            
            MetadataGlobal.APIResponseData oFiles = new MetadataGlobal.APIResponseData();
            oFiles.sReturnConfig = "s3:files/";
            
            while (rsFile.hasNext())
            {
                QuerySolution qsFile = rsFile.nextSolution();
                String sFileUri = qsFile.get("?fileUri").toString();
                OntResource resFile = oModel.getOntResource(sFileUri);
                
                MetadataGlobal.APIResponseData oFile = new MetadataGlobal.APIResponseData();
                oFile.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_file + "/";
                
                MetadataGlobal.APIResponseData oFileUri = new MetadataGlobal.APIResponseData();
                oFileUri.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_file + MetadataConstants.c_XMLE_Uri;
                oFileUri.sData = sFileUri;
                oFile.oData.add(oFileUri);
                
                //MetadataGlobal.APIResponseData oModules = new MetadataGlobal.APIResponseData();
                //oModules.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_fileModules + "/";
                
                StmtIterator siProperties = resFile.listProperties();
                while (siProperties.hasNext())
                {
                    Statement sStatement = siProperties.next();
                    String sProperty = sStatement.getPredicate().getURI();
                    
                    //File name
                    if (sProperty.equals(MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLDataProperty_Name))
                    {
                        MetadataGlobal.APIResponseData oFileName = new MetadataGlobal.APIResponseData();
                        oFileName.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_name;
                        oFileName.sData = sStatement.getObject().toString();
                        oFile.oData.add(oFileName);
                    }
                    
                    //Modules
                    if (sProperty.equals(MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLObjectProperty_HasModules))
                    {
                        MetadataGlobal.APIResponseData oModule = new MetadataGlobal.APIResponseData();
                        oModule.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_fileModules + "/";
                        
                        //Load module data
                        String sModuleUri = sStatement.getObject().toString();
                        OntResource resModule = oModel.getOntResource(sModuleUri);
                        
                        MetadataGlobal.APIResponseData oMUri = new MetadataGlobal.APIResponseData();
                        oMUri.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_module + MetadataConstants.c_XMLE_Uri;
                        oMUri.sData = sModuleUri;
                        oModule.oData.add(oMUri);
                                
                        StmtIterator siModuleProps = resModule.listProperties();
                        while (siModuleProps.hasNext())
                        {
                            Statement sMStatement = siModuleProps.next();
                            String sMProperty = sMStatement.getPredicate().getURI();
                            
                            //ID
                            if (sMProperty.equals(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_ID))
                            {
                                MetadataGlobal.APIResponseData oMID = new MetadataGlobal.APIResponseData();
                                oMID.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_module + MetadataConstants.c_XMLE_Id;
                                oMID.sData = sMStatement.getObject().toString();
                                oModule.oData.add(oMID);
                            }
                            
                            //Name
                            if (sMProperty.equals(MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLDataProperty_Name))
                            {
                                MetadataGlobal.APIResponseData oMName = new MetadataGlobal.APIResponseData();
                                oMName.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_moduleName;
                                oMName.sData = sMStatement.getObject().toString();
                                oModule.oData.add(oMName);
                            }
                            
                            //Start line
                            if (sMProperty.equals(MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLDataProperty_StartLine))
                            {
                                MetadataGlobal.APIResponseData oMStartLine = new MetadataGlobal.APIResponseData();
                                oMStartLine.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_moduleStartLine;
                                oMStartLine.sData = sMStatement.getObject().toString();
                                oModule.oData.add(oMStartLine);
                            }
                            
                            //End line
                            if (sMProperty.equals(MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLDataProperty_EndLine))
                            {
                                MetadataGlobal.APIResponseData oMEndtLine = new MetadataGlobal.APIResponseData();
                                oMEndtLine.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_moduleEndLine;
                                oMEndtLine.sData = sMStatement.getObject().toString();
                                oModule.oData.add(oMEndtLine);
                            }
                            
                            //Methods
                            if (sMProperty.equals(MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLObjectProperty_HasMethods))
                            {
                                MetadataGlobal.APIResponseData oMethods = new MetadataGlobal.APIResponseData();
                                oMethods.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_moduleMethods + "/";
                                
                                //Load method data
                                String sMethodUri = sMStatement.getObject().toString();
                                
                                OntResource resMethod = oModel.getOntResource(sMethodUri);
                                MetadataGlobal.APIResponseData oMetUri = new MetadataGlobal.APIResponseData();
                                oMetUri.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_method + MetadataConstants.c_XMLE_Uri;
                                oMetUri.sData = sMethodUri;
                                oMethods.oData.add(oMetUri);
                        
                                StmtIterator siMethodProps = resMethod.listProperties();
                                while (siMethodProps.hasNext())
                                {
                                    Statement sMetStatement = siMethodProps.next();
                                    String sMetProperty = sMetStatement.getPredicate().getURI();
                                    
                                    //ID
                                    if (sMetProperty.equals(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_ID))
                                    {
                                        MetadataGlobal.APIResponseData oMetID = new MetadataGlobal.APIResponseData();
                                        oMetID.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_method + MetadataConstants.c_XMLE_Id;
                                        oMetID.sData = sMetStatement.getObject().toString();
                                        oMethods.oData.add(oMetID);
                                    }
                                    
                                    //Name
                                    if (sMetProperty.equals(MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLDataProperty_Name))
                                    {
                                        MetadataGlobal.APIResponseData oMetName = new MetadataGlobal.APIResponseData();
                                        oMetName.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_methodName;
                                        oMetName.sData = sMetStatement.getObject().toString();
                                        oMethods.oData.add(oMetName);
                                    }
                                    
                                    //Start line
                                    if (sMetProperty.equals(MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLDataProperty_StartLine))
                                    {
                                        MetadataGlobal.APIResponseData oMetStartLine = new MetadataGlobal.APIResponseData();
                                        oMetStartLine.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_methodStartLine;
                                        oMetStartLine.sData = sMetStatement.getObject().toString();
                                        oMethods.oData.add(oMetStartLine);
                                    }

                                    //End line
                                    if (sMetProperty.equals(MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLDataProperty_EndLine))
                                    {
                                        MetadataGlobal.APIResponseData oMetEndtLine = new MetadataGlobal.APIResponseData();
                                        oMetEndtLine.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_methodEndLine;
                                        oMetEndtLine.sData = sMetStatement.getObject().toString();
                                        oMethods.oData.add(oMetEndtLine);
                                    }
                                }
                                
                                oModule.oData.add(oMethods);
                            }
                        }
                        
                        oFile.oData.add(oModule);
                    }
                }
                
                oFiles.oData.add(oFile);
            }
            
            oData.oData.add(oFiles);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return oData;
    }
    
    /**
     * @summary mail_getRelatedToKeyword
     * @startRealisation Sasa Stojanovic 17.01.2012.
     * @finalModification Sasa Stojanovic 17.01.2012.
     * @param sKeyword - keyword to search for
     * @return - APIResponseData object with results
     */
    public static MetadataGlobal.APIResponseData ac_email_getRelatedToKeyword(String sKeyword)
    {
        MetadataGlobal.APIResponseData oData = new MetadataGlobal.APIResponseData();
        try
        {
            OntModel oModel = MetadataGlobal.LoadOWL(MetadataConstants.sLocationLoadAlert);
            
            //if keyword exists in subject/body annotation or in mail subject
            String sQuery = "SELECT ?emailUri WHERE {"
                    + "{?emailUri a <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_Email + "> . "
                    + "?emailUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLAnnotationProperty_apSubject + "> ?subject . FILTER regex(?subject, \"" + sKeyword + "\", \"i\")}"
                    + " UNION "
                    + "{?emailUri a <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_Email + "> . "
                    + "?emailUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLAnnotationProperty_apBody + "> ?body . FILTER regex(?body, \"" + sKeyword + "\", \"i\")}"
                    + " UNION "
                    + "{?emailUri a <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_Email + "> . "
                    + "?emailUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_Subject + "> ?subject . FILTER regex(?subject, \"" + sKeyword + "\", \"i\")}"
                    + "}";
                            
            ResultSet rsEmail = QueryExecutionFactory.create(sQuery, oModel).execSelect();
            
            while (rsEmail.hasNext())
            {
                QuerySolution qsIssue = rsEmail.nextSolution();
                
                MetadataGlobal.APIResponseData oEmailUri = new MetadataGlobal.APIResponseData();
                oEmailUri.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_email + MetadataConstants.c_XMLE_Uri;
                oEmailUri.sData = qsIssue.get("?emailUri").toString();

                oData.oData.add(oEmailUri);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return oData;
    }
    
    /**
     * @summary mail_getRelatedToKeyword
     * @startRealisation Sasa Stojanovic 17.01.2012.
     * @finalModification Sasa Stojanovic 17.01.2012.
     * @param sKeyword - keyword to search for
     * @return - APIResponseData object with results
     */
    public static MetadataGlobal.APIResponseData ac_post_getRelatedToKeyword(String sKeyword)
    {
        MetadataGlobal.APIResponseData oData = new MetadataGlobal.APIResponseData();
        try
        {
            OntModel oModel = MetadataGlobal.LoadOWL(MetadataConstants.sLocationLoadAlert);
            
            //if keyword exists in title/body annotation or in mail subject
            String sQuery = "SELECT ?postUri WHERE {"
                    + "{?postUri a <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_post + "> . "
                    + "?postUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLAnnotationProperty_apTitle + "> ?title . FILTER regex(?title, \"" + sKeyword + "\", \"i\")}"
                    + " UNION "
                    + "{?postUri a <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_post + "> . "
                    + "?postUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLAnnotationProperty_apBody + "> ?body . FILTER regex(?body, \"" + sKeyword + "\", \"i\")}"
                    + " UNION "
                    + "{?postUri a <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_post + "> . "
                    + "?postUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_Subject + "> ?subject . FILTER regex(?subject, \"" + sKeyword + "\", \"i\")}"
                    + "}";
                            
            ResultSet rsPost = QueryExecutionFactory.create(sQuery, oModel).execSelect();
            
            while (rsPost.hasNext())
            {
                QuerySolution qsPost = rsPost.nextSolution();
                
                MetadataGlobal.APIResponseData oPostUri = new MetadataGlobal.APIResponseData();
                oPostUri.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_post + MetadataConstants.c_XMLE_Uri;
                oPostUri.sData = qsPost.get("?postUri").toString();

                oData.oData.add(oPostUri);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return oData;
    }
    
    /**
     * @summary wiki_getRelatedToKeyword
     * @startRealisation Sasa Stojanovic 17.01.2012.
     * @finalModification Sasa Stojanovic 17.01.2012.
     * @param sKeyword - keyword to search for
     * @return - APIResponseData object with results
     */
    public static MetadataGlobal.APIResponseData ac_wiki_getRelatedToKeyword(String sKeyword)
    {
        MetadataGlobal.APIResponseData oData = new MetadataGlobal.APIResponseData();
        try
        {
            OntModel oModel = MetadataGlobal.LoadOWL(MetadataConstants.sLocationLoadAlert);
            
            //if keyword exists in title/body annotation or in mail subject
            String sQuery = "SELECT ?wikiPageUri WHERE {"
                    + "{?wikiPageUri a <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_WikiPage + "> . "
                    + "?wikiPageUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLAnnotationProperty_apTitle + "> ?title . FILTER regex(?title, \"" + sKeyword + "\", \"i\")}"
                    + " UNION "
                    + "{?wikiPageUri a <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_WikiPage + "> . "
                    + "?wikiPagesUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLAnnotationProperty_apBody + "> ?body . FILTER regex(?body, \"" + sKeyword + "\", \"i\")}"
                    + " UNION "
                    + "{?wikiPageUri a <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_WikiPage + "> . "
                    + "?wikiPageUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_Subject + "> ?subject . FILTER regex(?subject, \"" + sKeyword + "\", \"i\")}"
                    + "}";
            
            ResultSet rsWikiPage = QueryExecutionFactory.create(sQuery, oModel).execSelect();
            
            while (rsWikiPage.hasNext())
            {
                QuerySolution qsWikiPage = rsWikiPage.nextSolution();
                
                MetadataGlobal.APIResponseData oWikiPageUri = new MetadataGlobal.APIResponseData();
                oWikiPageUri.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_wikiPage + MetadataConstants.c_XMLE_Uri;
                oWikiPageUri.sData = qsWikiPage.get("?wikiPageUri").toString();

                oData.oData.add(oWikiPageUri);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return oData;
    }
    
    /**
     * @summary issue_getRelatedToIssue
     * @startRealisation Sasa Stojanovic 17.01.2012.
     * @finalModification Sasa Stojanovic 17.01.2012.
     * @param sIssueUri - issueUri which is issue related to
     * @return - APIResponseData object with results
     */
    public static MetadataGlobal.APIResponseData ac_issue_getRelatedToIssue(String sIssueUri)
    {
        MetadataGlobal.APIResponseData oData = new MetadataGlobal.APIResponseData();
        try
        {
            OntModel oModel = MetadataGlobal.LoadOWLWithModelSpec(MetadataConstants.sLocationLoadAlert, OntModelSpec.OWL_MEM_MICRO_RULE_INF);
            
            //if issues have same description/subject annotation
            String sQuery = "SELECT ?issueUri WHERE {"
                    + "{?issueUri a <" + MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLClass_Issue + "> . "
                    + "?issueUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLAnnotationProperty_apDescription + "> ?description . "
                    + "<" + sIssueUri + "> <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLAnnotationProperty_apDescription + "> ?description . "
                    + "FILTER (?issueUri != <" + sIssueUri + ">)}"
                    + " UNION "
                    + "{?issueUri a <" + MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLClass_Issue + "> . "
                    + "?issueUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLAnnotationProperty_apSubject + "> ?subject . "
                    + "<" + sIssueUri + "> <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLAnnotationProperty_apSubject + "> ?subject . "
                    + "FILTER (?issueUri != <" + sIssueUri + ">)}"
                    + "}";
            
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
            e.printStackTrace();
        }
        return oData;
    }
    
    /**
     * @summary commit_getRelatedToIssue
     * @startRealisation Sasa Stojanovic 18.01.2012.
     * @finalModification Sasa Stojanovic 18.01.2012.
     * @param sIssueUri - issueUri which is issue related to
     * @return - APIResponseData object with results
     */
    public static MetadataGlobal.APIResponseData ac_commit_getRelatedToIssue(String sIssueUri)
    {
        MetadataGlobal.APIResponseData oData = new MetadataGlobal.APIResponseData();
        try
        {
            OntModel oModel = MetadataGlobal.LoadOWL(MetadataConstants.sLocationLoadAlert);
            
            //if issue has same description/subject annotation as commit commit annotation
            String sQuery = "SELECT ?commitUri WHERE {"
                    + "{?commitUri a <" + MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLClass_Commit + "> . "
                    + "?commitUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLAnnotationProperty_apCommit + "> ?description . "
                    + "<" + sIssueUri + "> <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLAnnotationProperty_apDescription + "> ?description}"
                    + " UNION "
                    + "{?commitUri a <" + MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLClass_Commit + "> . "
                    + "?commitUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLAnnotationProperty_apCommit + "> ?subject . "
                    + "<" + sIssueUri + "> <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLAnnotationProperty_apSubject + "> ?subject}"
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
     * @summary email_getRelatedToIssue
     * @startRealisation Sasa Stojanovic 18.01.2012.
     * @finalModification Sasa Stojanovic 18.01.2012.
     * @param sIssueUri - issueUri which is issue related to
     * @return - APIResponseData object with results
     */
    public static MetadataGlobal.APIResponseData ac_email_getRelatedToIssue(String sIssueUri)
    {
        MetadataGlobal.APIResponseData oData = new MetadataGlobal.APIResponseData();
        try
        {
            OntModel oModel = MetadataGlobal.LoadOWL(MetadataConstants.sLocationLoadAlert);
            
            //if issue has same description/subject annotation as email body/subject annotation
            String sQuery = "SELECT ?emailUri WHERE {"
                    + "{?emailUri a <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_Email + "> . "
                    + "?emailUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLAnnotationProperty_apBody + "> ?description . "
                    + "<" + sIssueUri + "> <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLAnnotationProperty_apDescription + "> ?description}"
                    + " UNION "
                    + "{?emailUri a <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_Email + "> . "
                    + "?emailUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLAnnotationProperty_apSubject + "> ?subject . "
                    + "<" + sIssueUri + "> <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLAnnotationProperty_apSubject + "> ?subject}"
                    + "}";
            
            ResultSet rsEmail = QueryExecutionFactory.create(sQuery, oModel).execSelect();
            
            while (rsEmail.hasNext())
            {
                QuerySolution qsEmail = rsEmail.nextSolution();
                
                MetadataGlobal.APIResponseData oEmailUri = new MetadataGlobal.APIResponseData();
                oEmailUri.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_email + MetadataConstants.c_XMLE_Uri;
                oEmailUri.sData = qsEmail.get("?emailUri").toString();

                oData.oData.add(oEmailUri);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return oData;
    }
    
    /**
     * @summary commit_getRelatedToIssue
     * @startRealisation Sasa Stojanovic 18.01.2012.
     * @finalModification Sasa Stojanovic 18.01.2012.
     * @param sIssueUri - issueUri which is issue related to
     * @return - APIResponseData object with results
     */
    public static MetadataGlobal.APIResponseData ac_post_getRelatedToIssue(String sIssueUri)
    {
        MetadataGlobal.APIResponseData oData = new MetadataGlobal.APIResponseData();
        try
        {
            OntModel oModel = MetadataGlobal.LoadOWL(MetadataConstants.sLocationLoadAlert);
            
            //if issue has same description/subject annotation as post body/title annotation
            String sQuery = "SELECT ?postUri WHERE {"
                    + "{?postUri a <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_post + "> . "
                    + "?postUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLAnnotationProperty_apBody + "> ?description . "
                    + "<" + sIssueUri + "> <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLAnnotationProperty_apDescription + "> ?description}"
                    + " UNION "
                    + "{?postUri a <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_post + "> . "
                    + "?postUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLAnnotationProperty_apTitle + "> ?subject . "
                    + "<" + sIssueUri + "> <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLAnnotationProperty_apSubject + "> ?subject}"
                    + "}";
            
            ResultSet rsPost = QueryExecutionFactory.create(sQuery, oModel).execSelect();
            
            while (rsPost.hasNext())
            {
                QuerySolution qsPost = rsPost.nextSolution();
                
                MetadataGlobal.APIResponseData oPostUri = new MetadataGlobal.APIResponseData();
                oPostUri.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_post + MetadataConstants.c_XMLE_Uri;
                oPostUri.sData = qsPost.get("?postUri").toString();

                oData.oData.add(oPostUri);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return oData;
    }
    
    /**
     * @summary commit_getRelatedToIssue
     * @startRealisation Sasa Stojanovic 18.01.2012.
     * @finalModification Sasa Stojanovic 18.01.2012.
     * @param sIssueUri - issueUri which is issue related to
     * @return - APIResponseData object with results
     */
    public static MetadataGlobal.APIResponseData ac_wiki_getRelatedToIssue(String sIssueUri)
    {
        MetadataGlobal.APIResponseData oData = new MetadataGlobal.APIResponseData();
        try
        {
            OntModel oModel = MetadataGlobal.LoadOWL(MetadataConstants.sLocationLoadAlert);
            
            //if issue has same description/subject annotation as wiki page body/title annotation
            String sQuery = "SELECT ?wikiPageUri WHERE {"
                    + "{?wikiPageUri a <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_WikiPage + "> . "
                    + "?wikiPageUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLAnnotationProperty_apBody + "> ?description . "
                    + "<" + sIssueUri + "> <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLAnnotationProperty_apDescription + "> ?description}"
                    + " UNION "
                    + "{?wikiPageUri a <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_WikiPage + "> . "
                    + "?wikiPageUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLAnnotationProperty_apTitle + "> ?subject . "
                    + "<" + sIssueUri + "> <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLAnnotationProperty_apSubject + "> ?subject}"
                    + "}";
            
            ResultSet rsWikiPage = QueryExecutionFactory.create(sQuery, oModel).execSelect();
            
            while (rsWikiPage.hasNext())
            {
                QuerySolution qsWikiPage = rsWikiPage.nextSolution();
                
                MetadataGlobal.APIResponseData oWikiPageUri = new MetadataGlobal.APIResponseData();
                oWikiPageUri.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_wikiPage + MetadataConstants.c_XMLE_Uri;
                oWikiPageUri.sData = qsWikiPage.get("?wikiPageUri").toString();

                oData.oData.add(oWikiPageUri);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return oData;
    }
    
    /**
     * @summary instance_getAllForConcept
     * @startRealisation  Dejan Milosavljevic 21.02.2012.
     * @finalModification Dejan Milosavljevic 21.02.2012.
     * @param sConceptUri - concept URI
     * @return - APIResponseData object with results
     */
    public static MetadataGlobal.APIResponseData ac_instance_getAllForConcept(String sConceptUri)
    {
        MetadataGlobal.APIResponseData oData = new MetadataGlobal.APIResponseData();
        try
        {
            OntModel omModel = MetadataGlobal.LoadOWL(MetadataConstants.sLocationLoadAlert);
            
            //It can be anything (issue, person...)
            OntResource resConcept = omModel.getOntResource(sConceptUri);
            MetadataGlobal.APIResponseData oMembers = new MetadataGlobal.APIResponseData();
            OntClass ocMember = omModel.getOntClass(sConceptUri);
            
            //Get individuals
            ExtendedIterator iIterator = ocMember.listInstances();
            while(iIterator.hasNext())
            {               
                MetadataGlobal.APIResponseData oSubData = new MetadataGlobal.APIResponseData();
                oSubData.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_item;
                oSubData.sData = iIterator.next().toString();
                oMembers.oData.add(oSubData);
            }
            
            OntClass ocConcept = omModel.getOntClass(sConceptUri);
            OntClass ocSuperConcept = ocConcept.getSuperClass();
              
            MetadataGlobal.APIResponseData oConcept = new MetadataGlobal.APIResponseData();
            oConcept.sReturnConfig = "s3:concept/";
            MetadataGlobal.APIResponseData oCName = new MetadataGlobal.APIResponseData();
            oCName.sReturnConfig = "s3:name";
            oCName.sData = resConcept.getLocalName();
            MetadataGlobal.APIResponseData oCUri = new MetadataGlobal.APIResponseData();
            oCUri.sReturnConfig = "s3:uri";
            oCUri.sData = sConceptUri;
            MetadataGlobal.APIResponseData oSuperConcept = new MetadataGlobal.APIResponseData();
            oSuperConcept.sReturnConfig = "s3:superConcept";
            if (ocSuperConcept != null)
                oSuperConcept.sData = ocSuperConcept.getURI();
            else
                oSuperConcept.sData = "";
            
            //Instances
            MetadataGlobal.APIResponseData oInstances = new MetadataGlobal.APIResponseData();
            oInstances.sReturnConfig = "s3:instances/";
                
            if (oMembers != null && oMembers.oData != null && oMembers.oData.size() > 0)
            {
                for (MetadataGlobal.APIResponseData oMember : oMembers.oData)
                {
                    OntResource resInstance = omModel.getOntResource(oMember.sData);
                    
                    MetadataGlobal.APIResponseData oInstance = new MetadataGlobal.APIResponseData();
                    oInstance.sReturnConfig = "s3:instance/";
                    MetadataGlobal.APIResponseData oIName = new MetadataGlobal.APIResponseData();
                    oIName.sReturnConfig = "s3:name";
                    oIName.sData = resInstance.getLocalName();
                    MetadataGlobal.APIResponseData oIUri = new MetadataGlobal.APIResponseData();
                    oIUri.sReturnConfig = "s3:uri";
                    oIUri.sData = oMember.sData;
                    
                    //Properties
                    MetadataGlobal.APIResponseData oProperties = new MetadataGlobal.APIResponseData();
                    oProperties.sReturnConfig = "s3:properties/";

                    OntoProperty oProps = GetMember(oMember.sData);
                    
                    for (OntoProperty opProperty : oProps.oProperties)
                    {
                        MetadataGlobal.APIResponseData oProperty = new MetadataGlobal.APIResponseData();
                        oProperty.sReturnConfig = "s3:property/";

                        MetadataGlobal.APIResponseData oPName = new MetadataGlobal.APIResponseData();
                        oPName.sReturnConfig = "s3:name";
                        oPName.sData = opProperty.sName;
                        MetadataGlobal.APIResponseData oPUri = new MetadataGlobal.APIResponseData();
                        oPUri.sReturnConfig = "s3:uri";
                        oPUri.sData = opProperty.sTypeOf;
                        MetadataGlobal.APIResponseData oPValue = new MetadataGlobal.APIResponseData();
                        oPValue.sReturnConfig = "s3:value";
                        oPValue.sData = opProperty.sValue;

                        oProperty.oData.add(oPName);
                        oProperty.oData.add(oPUri);
                        oProperty.oData.add(oPValue);
                        oProperties.oData.add(oProperty);
                    }
                                        
                    oInstance.oData.add(oIName);
                    oInstance.oData.add(oIUri);
                    oInstance.oData.add(oProperties);
                    oInstances.oData.add(oInstance);
                }

                oConcept.oData.add(oCName);
                oConcept.oData.add(oCUri);
                oConcept.oData.add(oSuperConcept);
                oConcept.oData.add(oInstances);
                oData.oData.add(oConcept);
            }
        }
        catch (Exception e)
        {
        }
        return oData;
    }
    
    /**
     * @summary sparql
     * @startRealisation Sasa Stojanovic 15.12.2011.
     * @finalModification Sasa Stojanovic 15.12.2011.
     * @param sSPARQL - SPARQL query
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
                    //OntProperty opProperty = omModel.getOntProperty(sTypeOf);
                    //OntResource orResource = opProperty.getRange();
                    //if (orResource != null)
                    //   sTypeOf = orResource.toString();
                    //else
                    //    sTypeOf = "";
                    
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
    
    
//    public static void CreateQuery() throws FileNotFoundException, IOException
//    {
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
//    }
    
//    private static void CreateNewQuery(OntModel ontologyModel)
//    {
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
//    }
    
    
//    public static void CreateTriples()
//    {
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
//  }

//    public static void SearchForIDs(String sSearchType, ArrayList<String> sIDs)  throws FileNotFoundException, IOException {
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
//    }
}
