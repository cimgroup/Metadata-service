/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MetadataCore;

import MetadataCore.MetadataGlobal.AnnotationData;
import MetadataCore.MetadataGlobal.OntoProperty;
import MetadataObjects.*;
import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
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
            OntModel oModel = MetadataConstants.omModel;
            
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
                    resBug.addProperty(dtpBugOpend, MetadataGlobal.FormatDateForSaving(oIssue.m_dtmDateOpened), XSDDatatype.XSDdateTime);
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
                    resBug.addProperty(dtpBugLastModified, MetadataGlobal.FormatDateForSaving(oIssue.m_dtmLastModified), XSDDatatype.XSDdateTime);
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
                if (oIssue.m_oHasReporter != null && (!oIssue.m_oHasReporter.m_sID.isEmpty() || !oIssue.m_oHasReporter.m_sEmail.isEmpty()))
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
                    oIssue.m_oHasComputerSystem.m_sReturnConfig = "YN#o:" + MetadataConstants.c_XMLE_issueComputerSystem + "/o:" + MetadataConstants.c_XMLE_issueComputerSystem + MetadataConstants.c_XMLE_Uri;
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
                if (oIssue.m_oHasAssignee != null && (!oIssue.m_oHasAssignee.m_sID.isEmpty() || !oIssue.m_oHasAssignee.m_sEmail.isEmpty()))
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
                        if (oIssue.m_oHasCCPerson[i] != null && (!oIssue.m_oHasCCPerson[i].m_sID.isEmpty() || !oIssue.m_oHasCCPerson[i].m_sEmail.isEmpty()))
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
                        resMilestone.addProperty(dtpMilestone, MetadataGlobal.FormatDateForSaving(oIssue.m_oHasMilestone.m_dtmTarget), XSDDatatype.XSDdateTime);
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
                        if (oIssue.m_oHasComment[i] != null)
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
                                DatatypeProperty dtpCommentDate= oModel.getDatatypeProperty( MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLDataProperty_Date);
                                resComment.removeAll(dtpCommentDate);
                                resComment.addProperty(dtpCommentDate, MetadataGlobal.FormatDateForSaving(oIssue.m_oHasComment[i].m_dtmDate), XSDDatatype.XSDdateTime);
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
                                resActivity.addProperty(dtpActivityPerformed, MetadataGlobal.FormatDateForSaving(oIssue.m_oHasActivity[i].m_dtmWhen), XSDDatatype.XSDdateTime);
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
                
                //tracker
                if (oIssue.m_oHasTracker != null && !oIssue.m_oHasTracker.m_sID.isEmpty())
                {
                    ObjectProperty opHasTracker = oModel.getObjectProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLObjectProperty_IsIssueOfTracker);

                    oIssue.m_oHasTracker.m_sObjectURI = MetadataGlobal.GetObjectURI(oModel, MetadataConstants.c_NS_Alert_Its + MetadataConstants.c_OWLClass_IssueTracker, oIssue.m_oHasTracker.m_sID);
                    Resource resTracker = oModel.getResource(oIssue.m_oHasTracker.m_sObjectURI);
                    
                    DatatypeProperty dtpTrackerURL = oModel.getDatatypeProperty(MetadataConstants.c_NS_Alert_Its + MetadataConstants.c_OWLDataProperty_IssueTrackerURL);
                    resTracker.removeAll(dtpTrackerURL);
                    resTracker.addProperty(dtpTrackerURL, oIssue.m_oHasTracker.m_sURL);
                    
                    DatatypeProperty dtpTrackerType = oModel.getDatatypeProperty(MetadataConstants.c_NS_Alert_Its + MetadataConstants.c_OWLDataProperty_IssueTrackerType);
                    resTracker.removeAll(dtpTrackerType);
                    resTracker.addProperty(dtpTrackerType, oIssue.m_oHasTracker.m_sType);
                    
                    resBug.removeAll(opHasTracker);
                    resBug.addProperty(opHasTracker, resTracker.asResource());
                    oIssue.m_oHasTracker.m_sReturnConfig = "YN#o:" + MetadataConstants.c_XMLE_issueTracker + "/o:" + MetadataConstants.c_XMLE_issueTracker + MetadataConstants.c_XMLE_Uri;
                }
                //end tracker 
                
                //AlertEvent
                String sEventUri;
                if (bIsUpdate)
                {
                    sEventUri = MetadataGlobal.GetObjectURI(oModel, MetadataConstants.c_NS_icep + MetadataConstants.c_OWLClass_ModifiedBug, "");
                }
                else
                {
                    sEventUri = MetadataGlobal.GetObjectURI(oModel, MetadataConstants.c_NS_icep + MetadataConstants.c_OWLClass_NewBug, "");
                }
                Resource resEvent = oModel.getResource(sEventUri);
                ObjectProperty opIsRelatedToBug = oModel.getObjectProperty(MetadataConstants.c_NS_icep + MetadataConstants.c_OWLObjectProperty_IsRelatedToBug);
                resEvent.addProperty(opIsRelatedToBug, resBug.asResource());

                //save data
                //MetadataGlobal.SaveOWL(oModel, MetadataConstants.sLocationSaveAlert);
                
                return oIssue;
            }
            else
            {
                System.out.println("Issue already stored.");
                return null;
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return null;
        }
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
            
            OntModel oModel = MetadataConstants.omModel;
            
            oCommit.m_sObjectURI = MetadataGlobal.GetObjectURI(oModel, MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLClass_Commit, oCommit.m_sID);
            Resource resCommit = oModel.getResource(oCommit.m_sObjectURI);
            oCommit.m_sReturnConfig = "YY#o:" + MetadataConstants.c_XMLE_mdservice + "/o:" + MetadataConstants.c_XMLE_commit + MetadataConstants.c_XMLE_Uri;
            
            //commitRepository
            if (oCommit.m_oIsCommitOfRepository != null && !oCommit.m_oIsCommitOfRepository.m_sID.isEmpty())
            {
                oCommit.m_oIsCommitOfRepository.m_sObjectURI = MetadataGlobal.GetObjectURI(oModel, MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLClass_Repository, oCommit.m_oIsCommitOfRepository.m_sID);
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
                resCommit.addProperty(dtpCommitDate, MetadataGlobal.FormatDateForSaving(oCommit.m_dtmCommitDate), XSDDatatype.XSDdateTime);
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
                            ObjectProperty opHasCommit = oModel.getObjectProperty(MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLObjectProperty_HasCommit);
                            
                            Resource resAction = oModel.getResource(MetadataGlobal.GetObjectURI(oModel, MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLClass_Action, ""));
                            resAction.addProperty(opHasCommit, resCommit.asResource());
                            resFile.addProperty(opHasAction, resAction.asResource());
                            
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
                                resAction.addProperty(opHasAction, ocHasAction.asResource());
                            }
                            if (clsHasAction.equals(clsCopyFile))
                            {
                                OntClass ocHasAction = oModel.getOntClass(MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLClass_Copy);
                                resAction.addProperty(opHasAction, ocHasAction.asResource());
                            }
                            if (clsHasAction.equals(clsDeleteFile))
                            {
                                OntClass ocHasAction = oModel.getOntClass(MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLClass_Delete);
                                resAction.addProperty(opHasAction, ocHasAction.asResource());
                            }
                            if (clsHasAction.equals(clsModifyFile))
                            {
                                OntClass ocHasAction = oModel.getOntClass(MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLClass_Modify);
                                resAction.addProperty(opHasAction, ocHasAction.asResource());
                            }
                            if (clsHasAction.equals(clsRenameFile))
                            {
                                OntClass ocHasAction = oModel.getOntClass(MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLClass_Rename);
                                resAction.addProperty(opHasAction, ocHasAction.asResource());
                            }
                            if (clsHasAction.equals(clsReplaceFile))
                            {
                                OntClass ocHasAction = oModel.getOntClass(MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLClass_Replace);
                                resAction.addProperty(opHasAction, ocHasAction.asResource());
                            }
                        }

                        //branch
                        if (!oCommit.m_oHasFile[i].m_sBranch.isEmpty())
                        {
                            DatatypeProperty dtpOnBranch = oModel.getDatatypeProperty(MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLDataProperty_OnBranch);
                            resFile.removeAll(dtpOnBranch);
                            resFile.addProperty(dtpOnBranch, oCommit.m_oHasFile[i].m_sBranch);
                        }
                        
                        //name
                        if (!oCommit.m_oHasFile[i].m_sName.isEmpty())
                        {
                            DatatypeProperty dtpName = oModel.getDatatypeProperty(MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLDataProperty_Name);
                            resFile.removeAll(dtpName);
                            resFile.addProperty(dtpName, oCommit.m_oHasFile[i].m_sName);
                        }
                        
                        //module
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
                                    
                                    if (oCommit.m_oHasFile[i].m_oHasModule[j].m_oHasMethod != null)
                                    {
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
                                                resCommit.addProperty(opHasMethod, resMethod.asResource());
                                                oCommit.m_oHasFile[i].m_oHasModule[j].m_oHasMethod[k].m_sReturnConfig = "YN#o:" + MetadataConstants.c_XMLE_moduleMethods + "/o:" + MetadataConstants.c_XMLE_method + MetadataConstants.c_XMLE_Uri;
                                            }
                                        }
                                    }
                                    
                                    resFile.addProperty(opHasModule, resModule.asResource());
                                    resCommit.addProperty(opHasModule, resModule.asResource());
                                    oCommit.m_oHasFile[i].m_oHasModule[j].m_sReturnConfig = "YY#o:" + MetadataConstants.c_XMLE_fileModules + "/o:" + MetadataConstants.c_XMLE_module + MetadataConstants.c_XMLE_Uri;
                                }
                            }
                        }
                            
                        resCommit.addProperty(opHasFile, resFile.asResource());
                        oCommit.m_oHasFile[i].m_sReturnConfig = "YY#o:" + MetadataConstants.c_XMLE_commitFile + "/o:" + MetadataConstants.c_XMLE_file + MetadataConstants.c_XMLE_Uri;
                    }
                }
            }
            
            //Commit product
            if (oCommit.m_oIsCommitOf != null)
            {
                oCommit.m_oIsCommitOf.m_sObjectURI = MetadataGlobal.GetObjectURI(oModel, MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLClass_Component, oCommit.m_oIsCommitOf.m_sID);
                ObjectProperty opIsCommitOf = oModel.getObjectProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLObjectProperty_IsCommitOf);
                Resource resIsIssueOf = oModel.getResource(oCommit.m_oIsCommitOf.m_sObjectURI);

                if (oCommit.m_oIsCommitOf.m_oIsComponentOf != null)
                {
                    oCommit.m_oIsCommitOf.m_oIsComponentOf.m_sObjectURI = MetadataGlobal.GetObjectURI(oModel, MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLClass_Product, oCommit.m_oIsCommitOf.m_oIsComponentOf.m_sID);
                    ObjectProperty opIsComponentOf = oModel.getObjectProperty(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLObjectProperty_IsComponentOf);
                    Resource resIsComponentOf = oModel.getResource(oCommit.m_oIsCommitOf.m_oIsComponentOf.m_sObjectURI);

                    if (!oCommit.m_oIsCommitOf.m_oIsComponentOf.m_sVersion.isEmpty())
                    {
                        DatatypeProperty dtpVersion = oModel.getDatatypeProperty(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLDataProperty_Version);
                        resIsComponentOf.removeAll(dtpVersion);
                        resIsComponentOf.addProperty(dtpVersion, oCommit.m_oIsCommitOf.m_oIsComponentOf.m_sVersion.toString());
                    }

                    resIsIssueOf.removeAll(opIsComponentOf);
                    resIsIssueOf.addProperty(opIsComponentOf, resIsComponentOf.asResource());
                    oCommit.m_oIsCommitOf.m_oIsComponentOf.m_sReturnConfig = "YN#o:" + MetadataConstants.c_XMLE_product + MetadataConstants.c_XMLE_Uri;
                }

                resCommit.removeAll(opIsCommitOf);
                resCommit.addProperty(opIsCommitOf, resIsIssueOf.asResource());
                oCommit.m_oIsCommitOf.m_sReturnConfig = "YY#o:" + MetadataConstants.c_XMLE_commitProduct + "/o:" + MetadataConstants.c_XMLE_productComponent + MetadataConstants.c_XMLE_Uri;
            }
            
            //AlertEvent
            String sEventUri = MetadataGlobal.GetObjectURI(oModel, MetadataConstants.c_NS_icep + MetadataConstants.c_OWLClass_CommitEvent, "");
            Resource resEvent = oModel.getResource(sEventUri);
            ObjectProperty opIsRelatedToBug = oModel.getObjectProperty(MetadataConstants.c_NS_icep + MetadataConstants.c_OWLObjectProperty_IsRelatedToCommit);
            resEvent.addProperty(opIsRelatedToBug, resCommit.asResource());
            
            //save data
            //MetadataGlobal.SaveOWL(oModel, MetadataConstants.sLocationSaveAlert);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
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
            
            OntModel oModel = MetadataConstants.omModel;
            
            oMail.m_sObjectURI = MetadataGlobal.GetObjectURI(oModel, MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_Email, oMail.m_sID);
            Resource resMail = oModel.getResource(oMail.m_sObjectURI);
            oMail.m_sReturnConfig = "YY#o:" + MetadataConstants.c_XMLE_mdservice + "/o:" + MetadataConstants.c_XMLE_email + MetadataConstants.c_XMLE_Uri;
            
            //messageId
            if (oMail.m_sMessageId != null && !oMail.m_sMessageId.isEmpty())
            {
                DatatypeProperty dtpMessageId = oModel.getDatatypeProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_MessageId);
                resMail.addProperty(dtpMessageId, oMail.m_sMessageId);
            }
            
            //from
            if (oMail.m_oFrom != null && oMail.m_oFrom.m_sID != null && !oMail.m_oFrom.m_sID.isEmpty())
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
                resMail.addProperty(dtpHasCreationDate, MetadataGlobal.FormatDateForSaving(oMail.m_dtmHasCreationDate), XSDDatatype.XSDdateTime);
            }
            
            //subject
            if (oMail.m_sSubject != null && !oMail.m_sSubject.isEmpty())
            {
                DatatypeProperty dtpSubject = oModel.getDatatypeProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_Subject);
                resMail.addProperty(dtpSubject, oMail.m_sSubject);
            }
            
            //in reply to
            if (oMail.m_oInReplyTo != null && oMail.m_oInReplyTo.m_sID != null && !oMail.m_oInReplyTo.m_sID.isEmpty())
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
                    if (oMail.m_oReferences[i] != null && oMail.m_oReferences[i].m_sID != null && !oMail.m_oReferences[i].m_sID.isEmpty())
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
            if (oMail.m_sContent != null && !oMail.m_sContent.isEmpty())
            {
                DatatypeProperty dtpBody = oModel.getDatatypeProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_Body);
                resMail.addProperty(dtpBody, oMail.m_sContent);
            }
            
            //AlertEvent
            String sEventUri = MetadataGlobal.GetObjectURI(oModel, MetadataConstants.c_NS_icep + MetadataConstants.c_OWLClass_MailEvent, "");
            Resource resEvent = oModel.getResource(sEventUri);
            ObjectProperty opIsRelatedToBug = oModel.getObjectProperty(MetadataConstants.c_NS_icep + MetadataConstants.c_OWLObjectProperty_IsRelatedToMail);
            resEvent.addProperty(opIsRelatedToBug, resMail.asResource());
            
            //save data
            //MetadataGlobal.SaveOWL(oModel, MetadataConstants.sLocationSaveAlert);
        }
        catch (Exception ex)
        {
        }
        return oMail;
    }
    
    /**
     * @summary Save wiki page data
     * @startRealisation Sasa Stojanovic 08.08.2012.
     * @finalModification Sasa Stojanovic 08.08.2012.
     * @param oWikiPage - wiki page object with data
     * @return wiki page object with uri-s
     */
    public static WikiPage SaveWikiPage(WikiPage oWikiPage, boolean bIsUpdate)
    {
        try {
            
            OntModel oModel = MetadataConstants.omModel;
            
            //chech if issue with given id alredy exists
            boolean bNew = MetadataGlobal.IsItNew(oModel, MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_WikiPage, oWikiPage.m_sID);
            
            //if it's new issue event and issue with that id already exists, do nothing
            if (bNew || bIsUpdate)
            {
                oWikiPage.m_sObjectURI = MetadataGlobal.GetObjectURI(oModel, MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_WikiPage, oWikiPage.m_sID);
                Resource resWikiPage = oModel.getResource(oWikiPage.m_sObjectURI);
                oWikiPage.m_sReturnConfig = "YY#o:" + MetadataConstants.c_XMLE_mdservice + "/o:" + MetadataConstants.c_XMLE_wikiPage + MetadataConstants.c_XMLE_Uri;

                //source
                if (!oWikiPage.m_sSource.isEmpty())
                {
                    DatatypeProperty dtpSource = oModel.getDatatypeProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_Source);
                    resWikiPage.removeAll(dtpSource);
                    resWikiPage.addProperty(dtpSource, oWikiPage.m_sSource);
                }

                //url
                if (!oWikiPage.m_sURL.isEmpty())
                {
                    DatatypeProperty dtpURL = oModel.getDatatypeProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_URL);
                    resWikiPage.removeAll(dtpURL);
                    resWikiPage.addProperty(dtpURL, oWikiPage.m_sURL);
                }

                //title
                if (!oWikiPage.m_sTitle.isEmpty())
                {
                    DatatypeProperty dtpSubject = oModel.getDatatypeProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_Subject);
                    resWikiPage.removeAll(dtpSubject);
                    resWikiPage.addProperty(dtpSubject, oWikiPage.m_sTitle);
                }

                //raw text
                if (!oWikiPage.m_sRawText.isEmpty())
                {
                    DatatypeProperty dtpRawText = oModel.getDatatypeProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_RawText);
                    resWikiPage.removeAll(dtpRawText);
                    resWikiPage.addProperty(dtpRawText, oWikiPage.m_sRawText);
                }

                //author
                if (oWikiPage.m_oAuthor != null && !oWikiPage.m_oAuthor.m_sID.isEmpty())
                {
                    SavePersonData(oWikiPage.m_oAuthor, oModel);
                    ObjectProperty opAuthor = null;
                    if (!bIsUpdate)
                        opAuthor = oModel.getObjectProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLObjectProperty_Author);
                    else
                        opAuthor = oModel.getObjectProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLObjectProperty_HasContributors);
                    Resource resAuthor = oModel.getResource(oWikiPage.m_oAuthor.m_sObjectURI);
                    resWikiPage.addProperty(opAuthor, resAuthor.asResource());
                    oWikiPage.m_oAuthor.m_sReturnConfig = "YN#o:" + MetadataConstants.c_XMLE_user + MetadataConstants.c_XMLE_Uri;
                }

                //AlertEvent
                String sEventUri = "";
                if (bIsUpdate)
                    sEventUri = MetadataGlobal.GetObjectURI(oModel, MetadataConstants.c_NS_icep + MetadataConstants.c_OWLClass_ArticleModified, "");
                else
                    sEventUri = MetadataGlobal.GetObjectURI(oModel, MetadataConstants.c_NS_icep + MetadataConstants.c_OWLClass_NewArticle, "");
                
                Resource resEvent = oModel.getResource(sEventUri);
                ObjectProperty opIsRelatedToWikiArticle = oModel.getObjectProperty(MetadataConstants.c_NS_icep + MetadataConstants.c_OWLObjectProperty_IsRelatedToWikiArticle);
                resEvent.addProperty(opIsRelatedToWikiArticle, resWikiPage.asResource());
                //edit comment
                if (!oWikiPage.m_sEditComment.isEmpty())
                {
                    DatatypeProperty dtpComment = oModel.getDatatypeProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_Comment);
                    resEvent.addProperty(dtpComment, oWikiPage.m_sEditComment);
                }
                //is minor
                if (oWikiPage.m_bIsMinor != null)
                {
                    DatatypeProperty dtpIsMinor = oModel.getDatatypeProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_IsMinor);
                    resEvent.addProperty(dtpIsMinor, oWikiPage.m_bIsMinor.toString());
                }
                
                return oWikiPage;
            }
            else
            {
                System.out.println("Wiki page already stored.");
                return null;
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return null;
        } 
    }
    
    /**
     * @summary Remove wiki page
     * @startRealisation Sasa Stojanovic 08.08.2012.
     * @finalModification Sasa Stojanovic 08.08.2012.
     * @param oWikiPage - wiki page object with data
     * @return wiki page object with uri-s
     */
    public static WikiPage RemoveWikiPage(WikiPage oWikiPage)
    {
        try {
            
            OntModel oModel = MetadataConstants.omModel;
           
            oWikiPage.m_sObjectURI = MetadataGlobal.GetObjectURI(oModel, MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_WikiPage, oWikiPage.m_sID);
            Resource resWikiPage = oModel.getResource(oWikiPage.m_sObjectURI);
            oWikiPage.m_sReturnConfig = "YY#o:" + MetadataConstants.c_XMLE_mdservice + "/o:" + MetadataConstants.c_XMLE_wikiPage + MetadataConstants.c_XMLE_Uri;
            
            //author data change
            if (oWikiPage.m_oAuthor != null && !oWikiPage.m_oAuthor.m_sID.isEmpty())
            {
                SavePersonData(oWikiPage.m_oAuthor, oModel);
                oWikiPage.m_oAuthor.m_sReturnConfig = "YN#o:" + MetadataConstants.c_XMLE_user + MetadataConstants.c_XMLE_Uri;
            }
            
            resWikiPage.removeProperties();

            //AlertEvent
            String sEventUri = MetadataGlobal.GetObjectURI(oModel, MetadataConstants.c_NS_icep + MetadataConstants.c_OWLClass_ArticleDeleted, "");
            Resource resEvent = oModel.getResource(sEventUri);
            ObjectProperty opIsRelatedToWikiArticle = oModel.getObjectProperty(MetadataConstants.c_NS_icep + MetadataConstants.c_OWLObjectProperty_IsRelatedToWikiArticle);
            resEvent.addProperty(opIsRelatedToWikiArticle, resWikiPage.asResource());
            //edit comment
            if (!oWikiPage.m_sEditComment.isEmpty())
            {
                DatatypeProperty dtpComment = oModel.getDatatypeProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_Comment);
                resEvent.addProperty(dtpComment, oWikiPage.m_sEditComment);
            }

            return oWikiPage;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return null;
        } 
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
            OntModel omModel = MetadataConstants.omModel;
            
            OntClass ocPerson = omModel.getOntClass(MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLClass_Person);
            
            oPerson.m_sObjectURI = MetadataConstants.c_NS_Alert + "Person" + MetadataGlobal.GetNextId(omModel, ocPerson);
            
            Individual inPerson = omModel.createIndividual(oPerson.m_sObjectURI, ocPerson );
            DatatypeProperty dtpFirstName = omModel.getDatatypeProperty(MetadataConstants.c_NS_foaf + "firstName");
            inPerson.addProperty(dtpFirstName, oPerson.m_sFirstName);
            DatatypeProperty dtpLastName = omModel.getDatatypeProperty(MetadataConstants.c_NS_foaf + "lastName");
            inPerson.addProperty(dtpLastName, oPerson.m_sLastName);
            DatatypeProperty dtpGender = omModel.getDatatypeProperty(MetadataConstants.c_NS_foaf + "gender");
            inPerson.addProperty(dtpGender, oPerson.m_sGender);
            
            //MetadataGlobal.SaveOWL(omModel, MetadataConstants.sLocationSaveAlert);
        }
        catch (Exception e)
        {
        }
        return oPerson;
    }
    
    /**
     * @summary Save person data
     * @startRealisation Sasa Stojanovic 02.09.2011.
     * @finalModification Sasa Stojanovic 19.12.2012.
     * @param oPerson - foaf_person object
     * @return - same foaf_person object with filled m_sObjectURI
     */
    public static foaf_Person SavePersonData(foaf_Person oPerson, OntModel oModel)
    {
        try
        {
            boolean bSaveId = false;
            
            if (oPerson.m_sObjectURI != null && !oPerson.m_sObjectURI.isEmpty())
            {
                bSaveId = true;
            }
            else
            {
                oPerson.m_sObjectURI = MetadataGlobal.GetObjectURI(oModel, MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLClass_Person, oPerson.m_sID);
            }
            
            Resource resPerson = oModel.getResource(oPerson.m_sObjectURI);
            
            //if there is no ID provided, id will take a value of email or username
            if (bSaveId && (oPerson.m_sID == null || oPerson.m_sID.isEmpty()))
            {
                if (oPerson.m_sEmail != null && !oPerson.m_sEmail.isEmpty())
                    oPerson.m_sID = oPerson.m_sEmail;
                else if (oPerson.m_sUsername != null && !oPerson.m_sUsername.isEmpty())
                    oPerson.m_sID = oPerson.m_sUsername;
            }
            
            if (bSaveId && oPerson.m_sID != null && !oPerson.m_sID.isEmpty())
            {
                DatatypeProperty dtpId = oModel.getDatatypeProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_ID);
                resPerson.removeAll(dtpId);
                resPerson.addProperty(dtpId, oPerson.m_sID);
            }
            
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
            
            if (oPerson.m_sUsername != null && !oPerson.m_sUsername.isEmpty())
            {
                DatatypeProperty dtpUsername = oModel.getDatatypeProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_Username);
                resPerson.removeAll(dtpUsername);
                resPerson.addProperty(dtpUsername, oPerson.m_sUsername);
            }
            
            if (oPerson.m_sWikiEditCount != null && !oPerson.m_sWikiEditCount.isEmpty())
            {
                DatatypeProperty dtpWikiEditCount = oModel.getDatatypeProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_WikiEditCount);
                resPerson.removeAll(dtpWikiEditCount);
                resPerson.addProperty(dtpWikiEditCount, oPerson.m_sWikiEditCount);
            }
        }
        catch (Exception e)
        {
        }
        return oPerson;
    }
    
    /**
     * @summary Save annotation data.
     * @startRealisation  Dejan Milosavljevic 17.01.2012.
     * @finalModification Sasa Stojanovic 23.05.2012.
     * @param oAnnotation - AnnotationData object
     * @return - same AnnotationData object with filled m_sObjectURI
     */
    public static AnnotationData SaveAnnotationData(AnnotationData oAnnotation)
    {
        try
        {
            OntModel omModel = MetadataConstants.omModel;
            
            if (oAnnotation.oAnnotated != null)
            {
                if (oAnnotation.m_sObjectURI != null) //Not for Issue
                {
                    Resource resObject = omModel.getResource(oAnnotation.m_sObjectURI);
                    
                    //itemId
                    if (oAnnotation.iItemId != null)
                    {
                        DatatypeProperty dtpItemId = omModel.getDatatypeProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_KeuiItemId);
                        resObject.removeAll(dtpItemId);
                        resObject.addProperty(dtpItemId, oAnnotation.iItemId.toString());
                    }

                    //threadId
                    if (oAnnotation.iThreadId != null)
                    {
                        DatatypeProperty dtpThreadId = omModel.getDatatypeProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_KeuiThreadId);
                        resObject.removeAll(dtpThreadId);
                        resObject.addProperty(dtpThreadId, oAnnotation.iThreadId.toString());
                    }
                    
                    //Kewords
                    if (oAnnotation.oKeywords != null)
                    {
                        String sKeywordName =  MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLAnnotationProperty_apKeyword;
                        for (int i = 0; i < oAnnotation.oKeywords.length; i++)
                        {
                            AnnotationProperty apKeyword = omModel.getAnnotationProperty(sKeywordName);

                            resObject.addProperty(apKeyword, oAnnotation.oKeywords[i]);
                        }
                    }
                    
                    //References
                    if (oAnnotation.oReferences != null)
                    {
                        for (int i = 0; i < oAnnotation.oReferences.size(); i++)
                        {
                            Resource resReferencedObject = omModel.getResource(oAnnotation.oReferences.get(i));
                            if (resReferencedObject != null)
                            {
                                ObjectProperty opHasReferenceTo = omModel.getObjectProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLObjectProperty_HasReferenceTo);
                                resObject.addProperty(opHasReferenceTo, resReferencedObject.asResource());
                                resReferencedObject.addProperty(opHasReferenceTo, resObject.asResource());
                            }
                        }
                    }
                }
                else if (oAnnotation.oAnnotated.length > 0) //For Issue
                {
                    Resource resObject = omModel.getResource(oAnnotation.oAnnotated[0].sHasObject);

                    //threadId
                    if (oAnnotation.iThreadId != null)
                    {
                        DatatypeProperty dtpThreadId = omModel.getDatatypeProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_KeuiThreadId);
                        resObject.removeAll(dtpThreadId);
                        resObject.addProperty(dtpThreadId, oAnnotation.iThreadId.toString());
                    }
                    
                    //Kewords
                    if (oAnnotation.oKeywords != null)
                    {
                        String sKeywordName =  MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLAnnotationProperty_apKeyword;
                        for (int i = 0; i < oAnnotation.oKeywords.length; i++)
                        {
                            AnnotationProperty apKeyword = omModel.getAnnotationProperty(sKeywordName);

                            resObject.addProperty(apKeyword, oAnnotation.oKeywords[i]);
                        }
                    }
                    
                    //References
                    if (oAnnotation.oReferences != null)
                    {
                        for (int i = 0; i < oAnnotation.oReferences.size(); i++)
                        {
                            Resource resReferencedObject = omModel.getResource(oAnnotation.oReferences.get(i));
                            if (resReferencedObject != null)
                            {
                                ObjectProperty opHasReferenceTo = omModel.getObjectProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLObjectProperty_HasReferenceTo);
                                resObject.addProperty(opHasReferenceTo, resReferencedObject.asResource());
                                resReferencedObject.addProperty(opHasReferenceTo, resObject.asResource());
                            }
                        }
                    }
                }
                
                //Annotations
                String sAnnotationClass = MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_Annotation;
                int iACount = oAnnotation.oAnnotated.length;
                for (int i = 0; i < iACount; i++)
                {
                    if (oAnnotation.oAnnotated[i].sValue != null)
                    {
                        OntClass ocAnnotation = omModel.getOntClass(sAnnotationClass);
                        oAnnotation.oAnnotated[i].m_sObjectURI = sAnnotationClass + MetadataGlobal.GetNextId(omModel, ocAnnotation);
                        Resource resAnnotation = omModel.createResource(oAnnotation.oAnnotated[i].m_sObjectURI, ocAnnotation);
                        oAnnotation.oAnnotated[i].m_sReturnConfig = "YN#s1:" + oAnnotation.oAnnotated[i].sName + MetadataConstants.c_XMLE_Uri;

                        //HasObject
                        if (oAnnotation.m_sObjectURI != null) //Not for Issue
                        {
                            Resource resObject = omModel.getResource(oAnnotation.m_sObjectURI);

                            ObjectProperty opHasObject = omModel.getObjectProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLObjectProperty_HasObject);
                            resAnnotation.addProperty(opHasObject, resObject.asResource());
                        }
                        else                                  //For Issue
                        {
                            Resource resObject = omModel.getResource(oAnnotation.oAnnotated[i].sHasObject);

                            ObjectProperty opHasObject = omModel.getObjectProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLObjectProperty_HasObject);
                            resAnnotation.addProperty(opHasObject, resObject.asResource());
                        }

                        //Name
                        DatatypeProperty dtpName = omModel.getDatatypeProperty(MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLDataProperty_Name);
                        resAnnotation.addProperty(dtpName, oAnnotation.oAnnotated[i].sName);

                        //Text
                        DatatypeProperty dtpText = omModel.getDatatypeProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_Text);
                        resAnnotation.addProperty(dtpText, oAnnotation.oAnnotated[i].sValue);

                        //For Issue
                        if (oAnnotation.oAnnotated[i].iItemId != null)
                        {
                            Resource resObject = omModel.getResource(oAnnotation.oAnnotated[i].sHasObject);

                            //itemId
                            DatatypeProperty dtpItemId = omModel.getDatatypeProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_KeuiItemId);
                            resObject.removeAll(dtpItemId);
                            resObject.addProperty(dtpItemId, oAnnotation.oAnnotated[i].iItemId.toString());
                        }

                        //Anotation concepts
                        String sConceptClass = MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_AnnotationConcept;
                        if (oAnnotation.oAnnotated[i].oConcepts != null)
                        {
                            int iCCount = oAnnotation.oAnnotated[i].oConcepts.length;
                            for (int j = 0; j < iCCount; j++)
                            {
                                OntClass ocConcept = omModel.getOntClass(sConceptClass);

                                oAnnotation.oAnnotated[i].oConcepts[j].m_sObjectURI = sConceptClass + MetadataGlobal.GetNextId(omModel, ocConcept);

                                Resource resConcept = omModel.createResource(oAnnotation.oAnnotated[i].oConcepts[j].m_sObjectURI, ocConcept);
                                oAnnotation.oAnnotated[i].oConcepts[j].m_sReturnConfig = "YY#s1:" + oAnnotation.oAnnotated[i].oConcepts[j].sName + MetadataConstants.c_XMLE_Uri;

                                //Uri
                                DatatypeProperty dtpUri = omModel.getDatatypeProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_Uri);
                                //if (dtpUri == null)
                                //{
                                //    dtpUri = omModel.createDatatypeProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_Uri);
                                //}
                                resConcept.addProperty(dtpUri, oAnnotation.oAnnotated[i].oConcepts[j].sUri);

                                //Count
                                DatatypeProperty dtpCount = omModel.getDatatypeProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_Weight);
                                resConcept.addProperty(dtpCount, oAnnotation.oAnnotated[i].oConcepts[j].sWeight);

                                ////HasConcept (annotation)
                                ObjectProperty opHasConcepts = omModel.getObjectProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLObjectProperty_HasConcepts);
                                resAnnotation.addProperty(opHasConcepts, resConcept.asResource());
                            }
                        }
                    }
                }
            }
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
        
        if (sAnnotationName.equals(MetadataConstants.c_XMLE_issueDescriptionAnnotated))
        {
            sConceptName = MetadataConstants.c_XMLE_issueDescriptionConcepts;
        }
        else if (sAnnotationName.equals(MetadataConstants.c_XMLE_commentTextAnnotated))
        {
            sConceptName = MetadataConstants.c_XMLE_commentTextConcepts;
        }
        else if (sAnnotationName.equals(MetadataConstants.c_XMLE_commitMessageLogAnnotated))
        {
            sConceptName = MetadataConstants.c_XMLE_commitMessageLogConcepts;
        }
        else if (sAnnotationName.equals(MetadataConstants.c_XMLE_subjectAnnotated))
        {
            sConceptName = MetadataConstants.c_XMLE_subjectConcepts;
        }
        else if (sAnnotationName.equals(MetadataConstants.c_XMLE_bodyAnnotated))
        {
            sConceptName = MetadataConstants.c_XMLE_bodyConcepts;
        }
        else if (sAnnotationName.equals(MetadataConstants.c_XMLE_contentAnnotated))
        {
            sConceptName = MetadataConstants.c_XMLE_contentConcepts;
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
        
        if (sAnnotationName.equals(MetadataConstants.c_XMLE_issueDescriptionAnnotated))
        {
            sAnnotationPropertyName = MetadataConstants.c_OWLAnnotationProperty_apDescription;
        }
        else if (sAnnotationName.equals(MetadataConstants.c_XMLE_commentTextAnnotated))
        {
            sAnnotationPropertyName = MetadataConstants.c_OWLAnnotationProperty_apComment;
        }
        else if (sAnnotationName.equals(MetadataConstants.c_XMLE_commitMessageLogAnnotated))
        {
            sAnnotationPropertyName = MetadataConstants.c_OWLAnnotationProperty_apCommit;
        }
        else if (sAnnotationName.equals(MetadataConstants.c_XMLE_subjectAnnotated))
        {
            sAnnotationPropertyName = MetadataConstants.c_OWLAnnotationProperty_apSubject;
        }
        else if (sAnnotationName.equals(MetadataConstants.c_XMLE_bodyAnnotated))
        {
            sAnnotationPropertyName = MetadataConstants.c_OWLAnnotationProperty_apBody;
        }
        else if (sAnnotationName.equals(MetadataConstants.c_XMLE_contentAnnotated))
        {
            sAnnotationPropertyName = MetadataConstants.c_OWLAnnotationProperty_apContent;
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
            OntModel omModel = MetadataConstants.omModel;
            
            oForumPost.m_sObjectURI = MetadataGlobal.GetObjectURI(omModel, MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_post, oForumPost.m_sID);
            Resource resPost = omModel.getResource(oForumPost.m_sObjectURI);
            oForumPost.m_sReturnConfig = "YY#o:" + MetadataConstants.c_XMLE_mdservice + "/o:" + MetadataConstants.c_XMLE_post + MetadataConstants.c_XMLE_Uri;
            
            //ForumItemID
            if (oForumPost.m_sForumItemID != null && !oForumPost.m_sForumItemID.isEmpty())
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
                resPost.addProperty(dtpTime, MetadataGlobal.FormatDateForSaving(oForumPost.m_dtmTime), XSDDatatype.XSDdateTime);
            }
            
            //Subject
            if (oForumPost.m_sSubject != null && !oForumPost.m_sSubject.isEmpty())
            {
                DatatypeProperty dtpSubject = omModel.getDatatypeProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_Subject);
                resPost.removeAll(dtpSubject);
                resPost.addProperty(dtpSubject, oForumPost.m_sSubject);
            }

            //Body
            if (oForumPost.m_sBody != null && !oForumPost.m_sBody.isEmpty())
            {
                DatatypeProperty dtpBody = omModel.getDatatypeProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_Body);
                resPost.removeAll(dtpBody);
                resPost.addProperty(dtpBody, oForumPost.m_sBody);
            }
            
            //isAuthorOf
            if (oForumPost.m_oAuthor != null && oForumPost.m_oAuthor.m_sUsername != null && !oForumPost.m_oAuthor.m_sUsername.isEmpty())
            {
                SavePersonData(oForumPost.m_oAuthor, omModel);
                ObjectProperty opHasAuthor = omModel.getObjectProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLObjectProperty_Author);
                Resource resHasAuthor = omModel.getResource(oForumPost.m_oAuthor.m_sObjectURI);
                resPost.removeAll(opHasAuthor);
                resPost.addProperty(opHasAuthor, resHasAuthor.asResource());
                oForumPost.m_oAuthor.m_sReturnConfig = "YN#o:" + MetadataConstants.c_XMLE_author + MetadataConstants.c_XMLE_Uri;
            }
            
            //Category
            if (oForumPost.m_sCategory != null && !oForumPost.m_sCategory.isEmpty())
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
                    if (oForumPost.m_oInForumThread.m_oInForum.m_sName != null && !oForumPost.m_oInForumThread.m_oInForum.m_sName.isEmpty())
                    {
                        DatatypeProperty dtpForumName = omModel.getDatatypeProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_Name);
                        resForum.removeAll(dtpForumName);
                        resForum.addProperty(dtpForumName, oForumPost.m_oInForumThread.m_oInForum.m_sName);
                    }
                }
            }
            
            //AlertEvent
            String sEventUri = MetadataGlobal.GetObjectURI(omModel, MetadataConstants.c_NS_icep + MetadataConstants.c_OWLClass_PostEvent, "");
            Resource resEvent = omModel.getResource(sEventUri);
            ObjectProperty opIsRelatedToBug = omModel.getObjectProperty(MetadataConstants.c_NS_icep + MetadataConstants.c_OWLObjectProperty_IsRelatedToPost);
            resEvent.addProperty(opIsRelatedToBug, resPost.asResource());
            
            //MetadataGlobal.SaveOWL(omModel, MetadataConstants.sLocationSaveAlert);
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
            OntModel omModel = MetadataConstants.omModel;
            
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
                            ObjectProperty opRecency = omModel.getObjectProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLObjectProperty_HasAttribute);
                            Resource resRecency = omModel.getResource(oAttribute.m_sObjectURI);
                            oAttribute.m_sReturnConfig = "YY#o:" + MetadataConstants.c_XMLE_recency + MetadataConstants.c_XMLE_Uri;
                            resCompetence.addProperty(opRecency, resRecency.asResource());
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
                                        resMetric.addProperty(dtpValue, MetadataGlobal.FormatDateForSaving(MetadataGlobal.GetDateTime(oMetric.m_sMetricValue)), XSDDatatype.XSDdateTime);
                                        
                                        resRecency.addProperty(opMetric, resMetric.asResource());
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
                                        resMetric.addProperty(dtpValue, MetadataGlobal.FormatDateForSaving(MetadataGlobal.GetDateTime(oMetric.m_sMetricValue)), XSDDatatype.XSDdateTime);
                                        
                                        resRecency.addProperty(opMetric, resMetric.asResource());
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
                                        resMetric.addProperty(dtpValue, MetadataGlobal.FormatDateForSaving(MetadataGlobal.GetDateTime(oMetric.m_sMetricValue)), XSDDatatype.XSDdateTime);
                                        
                                        resRecency.addProperty(opMetric, resMetric.asResource());
                                    }
                                }
                            }
                        }
                    }
                }
            }

            //MetadataGlobal.SaveOWL(omModel, MetadataConstants.sLocationSaveAlert);
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
    public static Identity[] SaveIdentity(Identity[] oIdentities, boolean bIsUpdate)
    {
        try 
        {
            OntModel oModel = MetadataConstants.omModel;
            
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
                        if (oIdentities[i].m_oIs[j] != null)
                        {
                            if (!oIdentities[i].m_oIs[j].m_bRemoved)
                            {
                                oIdentities[i].m_oIs[j] = SavePersonData(oIdentities[i].m_oIs[j], oModel);
                                Resource resIsPerson = oModel.getResource(oIdentities[i].m_oIs[j].m_sObjectURI);
                                resIdentity.addProperty(opIsPerson, resIsPerson.asResource());
                                if (!bIsUpdate)
                                    oIdentities[i].m_oIs[j].m_sReturnConfig = "YN#o:personIsUri";
                                else
                                    oIdentities[i].m_oIs[j].m_sReturnConfig = "YN#o:addPersonIsUri";
                            }
                            else
                            {
                                oIdentities[i].m_oIs[j] = SavePersonData(oIdentities[i].m_oIs[j], oModel);
                                Resource resIsRemove = oModel.getResource(oIdentities[i].m_oIs[j].m_sObjectURI);
                                oModel.removeAll(resIdentity, opIsPerson, resIsRemove);
                                oIdentities[i].m_oIs[j].m_sReturnConfig = "YN#o:removePersonIsUri";
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
                        if (oIdentities[i].m_oIsnt[j] != null)
                        {
                            if (!oIdentities[i].m_oIsnt[j].m_bRemoved)
                            {
                                oIdentities[i].m_oIsnt[j] = SavePersonData(oIdentities[i].m_oIsnt[j], oModel);
                                Resource resIsPerson = oModel.getResource(oIdentities[i].m_oIsnt[j].m_sObjectURI);
                                resIdentity.addProperty(opIsntPerson, resIsPerson.asResource());
                                if (!bIsUpdate)
                                    oIdentities[i].m_oIsnt[j].m_sReturnConfig = "YN#o:personIsntUri";
                                else
                                    oIdentities[i].m_oIsnt[j].m_sReturnConfig = "YN#o:addPersonIsntUri";
                            }
                            else
                            {
                                oIdentities[i].m_oIsnt[j] = SavePersonData(oIdentities[i].m_oIsnt[j], oModel);
                                Resource resIsRemove = oModel.getResource(oIdentities[i].m_oIsnt[j].m_sObjectURI);
                                oModel.removeAll(resIdentity, opIsntPerson, resIsRemove);
                                oIdentities[i].m_oIsnt[j].m_sReturnConfig = "YN#o:removePersonIsntUri";
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
            //MetadataGlobal.SaveOWL(oModel, MetadataConstants.sLocationSaveAlert);
            
        }
        catch (Exception ex)
        {
        }
        return oIdentities;
    }
    
    /**
     * @summary Save concept data
     * @startRealisation Sasa Stojanovic 17.05.2012.
     * @finalModification Sasa Stojanovic 17.05.2012.
     * @param oConcepts - identity objects with data
     * @return identity objects with uri-s
     */
    public static Concept[] SaveConcept(Concept[] oConcepts)
    {
        try 
        {
            OntModel oModel = MetadataConstants.omAnnotation;
            
            for (int i = 0; i < oConcepts.length; i++)
            {
                if (oConcepts[i].m_sObjectURI != null && !oConcepts[i].m_sObjectURI.isEmpty())
                {
                    OntClass ocConcept = oModel.getOntClass(oConcepts[i].m_sObjectURI);
                    if (ocConcept == null)
                    {
                        ocConcept = oModel.createClass(oConcepts[i].m_sObjectURI);
                    }
                    oConcepts[i].m_sReturnConfig = "YY#o:" + MetadataConstants.c_XMLE_concept + MetadataConstants.c_XMLE_Uri;
                    
                    if (oConcepts[i].m_sLabel != null && !oConcepts[i].m_sLabel.isEmpty())
                    {
                        AnnotationProperty apLabel = oModel.getAnnotationProperty(MetadataConstants.c_NS_w3_rdf_schema + MetadataConstants.c_OWLAnnotationProperty_Label);
                        ocConcept.addProperty(apLabel, oConcepts[i].m_sLabel);
                    }
                    
                    if (oConcepts[i].m_sComment != null && !oConcepts[i].m_sComment.isEmpty())
                    {
                        AnnotationProperty apComment = oModel.getAnnotationProperty(MetadataConstants.c_NS_w3_rdf_schema + MetadataConstants.c_OWLAnnotationProperty_Comment);
                        ocConcept.addProperty(apComment, oConcepts[i].m_sComment);
                    }
                    
                    if (oConcepts[i].m_sSameAs != null)
                    {
                        AnnotationProperty apSameAs = oModel.getAnnotationProperty(MetadataConstants.c_NS_w3_owl + MetadataConstants.c_OWLAnnotationProperty_SameAs);
                        for (int j = 0; j < oConcepts[i].m_sSameAs.length; j++)
                        {
                            ocConcept.addProperty(apSameAs, oConcepts[i].m_sSameAs[j]);
                        }
                    }
                    
                    if (oConcepts[i].m_sLinksTo != null)
                    {
                        AnnotationProperty apLinksTo = oModel.getAnnotationProperty(MetadataConstants.c_NS_ijs_predicate + MetadataConstants.c_OWLAnnotationProperty_LinksTo);
                        for (int j = 0; j < oConcepts[i].m_sLinksTo.length; j++)
                        {
                            ocConcept.addProperty(apLinksTo, oConcepts[i].m_sLinksTo[j]);
                        }
                    }
                    
                    if (oConcepts[i].m_sSubClassOf != null)
                    {
                        for (int j = 0; j < oConcepts[i].m_sSubClassOf.length; j++)
                        {
                            if (!oConcepts[i].m_sSubClassOf[j].isEmpty())
                            {
                                OntClass ocSuper = oModel.getOntClass(oConcepts[i].m_sSubClassOf[j]);
                                if (ocSuper == null)
                                {
                                    ocSuper = oModel.createClass(oConcepts[i].m_sSubClassOf[j]);
                                }
                                ocConcept.addSuperClass(ocSuper);
                            }
                        }
                    }
                    
                    if (oConcepts[i].m_sSuperClassOf != null)
                    {
                        for (int j = 0; j < oConcepts[i].m_sSuperClassOf.length; j++)
                        {
                            if (!oConcepts[i].m_sSuperClassOf[j].isEmpty())
                            {
                                OntClass ocSub = oModel.getOntClass(oConcepts[i].m_sSuperClassOf[j]);
                                if (ocSub == null)
                                {
                                    ocSub = oModel.createClass(oConcepts[i].m_sSuperClassOf[j]);
                                }
                                ocConcept.addSubClass(ocSub);
                            }
                        }
                    }
                }
            }
            
            //save data
            //MetadataGlobal.SaveOWL(oModel, MetadataConstants.sLocationSaveAlert);
            
        }
        catch (Exception ex)
        {
        }
        return oConcepts;
    }
    
    // <editor-fold desc="API Calls">
    
    /**
     * @summary issue_getAllForProduct
     * @startRealisation Sasa Stojanovic 13.12.2011.
     * @finalModification Sasa Stojanovic 06.04.2012.
     * @param sProductID - product id
     * @param dtmFromDate - date from filter
     * @return - APIResponseData object with results
     */
    public static MetadataGlobal.APIResponseData ac_issue_getAllForProduct(String sProductID, Date dtmFromDate)
    {
        MetadataGlobal.APIResponseData oData = new MetadataGlobal.APIResponseData();
        try
        {
            OntModel oModel = MetadataConstants.omModel;
                        
            String sProductUri = MetadataGlobal.GetObjectURINoCreate(oModel, MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLClass_Product, sProductID);

            String sQuery = "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> "
                    + "SELECT ?issueUri ?issueId ?issueDescription WHERE "
                    + "{?issueUri <" + MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLObjectProperty_IsIssueOf + ">  ?componentUri ."
                    + " ?componentUri  <" + MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLObjectProperty_IsComponentOf + "> <" + sProductUri + "> ."
                    + " ?issueUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_ID + "> ?issueId ."
                    + " ?issueUri <" + MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLDataProperty_Description + "> ?issueDescription ."
                    + " OPTIONAL { ?issueUri <" + MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLDataProperty_DateOpened + "> ?issueDateOpened . FILTER ( ?issueDateOpened > \"" + MetadataGlobal.FormatDateForSaving(dtmFromDate) + "\"^^xsd:dateTime )} ."
                    + " OPTIONAL { ?issueUri <" + MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLDataProperty_LastModified + "> ?issueLastModified . FILTER ( ?issueLastModified > \"" + MetadataGlobal.FormatDateForSaving(dtmFromDate) + "\"^^xsd:dateTime )}"
                    + "}";

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
            e.printStackTrace();
        }
        return oData;
    }
    
    /**
     * @summary issue_getAllForPerson
     * @startRealisation Sasa Stojanovic 08.08.2012.
     * @finalModification Sasa Stojanovic 08.08.2012.
     * @param sIdentityId - identity id
     * @return - APIResponseData object with results
     */
    public static MetadataGlobal.APIResponseData ac_issue_getAllForIdentity(String sIdentityId)
    {
        MetadataGlobal.APIResponseData oData = new MetadataGlobal.APIResponseData();
        
        try
        {
            OntModel oModel = MetadataConstants.omModel;

            String sQuery = "SELECT DISTINCT ?issueId WHERE "
                    + "{ ?personUri a <" + MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLClass_Person + "> . "
                    + "?identityUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLObjectProperty_IsPerson + "> ?personUri . "
                    + "?identityUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_ID + "> \"" + sIdentityId + "\" . "
                    + "?commitUri <" + MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLObjectProperty_HasCommitter + "> ?personUri . "
                    + "?commitUri <" + MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLObjectProperty_HasMethods + "> ?methodUri . "
                    + "?methodUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLObjectProperty_HasReferenceTo + "> ?issueUri . "
                    + "?issueUri a <" + MetadataConstants.c_NS_Alert_Its + MetadataConstants.c_OWLClass_Bug + "> . "
                    + "?issueUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_ID + "> ?issueId }";
                            
            ResultSet rsIssue = QueryExecutionFactory.create(sQuery, oModel).execSelect();
            while (rsIssue.hasNext())
            {
                QuerySolution qsIssue = rsIssue.nextSolution();
                MetadataGlobal.APIResponseData oIssueId = new MetadataGlobal.APIResponseData();
                oIssueId.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_issue + MetadataConstants.c_XMLE_Id;
                oIssueId.sData = qsIssue.get("?issueId").toString();
                oData.oData.add(oIssueId);
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
            OntModel oModel = MetadataConstants.omModel;
            
            for (int i = 0; i < sMethodUri.size(); i++)
            {
                MetadataGlobal.APIResponseData oMethod = new MetadataGlobal.APIResponseData();
                oMethod.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_method + "/";
                
                MetadataGlobal.APIResponseData oMethodUri = new MetadataGlobal.APIResponseData();
                oMethodUri.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_method + MetadataConstants.c_XMLE_Uri;
                oMethodUri.sData = sMethodUri.get(i);
                oMethod.oData.add(oMethodUri);
                
                //direct relations with issues
                String sQuery1 = "SELECT ?issueUri WHERE {<" + sMethodUri.get(i) + "> <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLObjectProperty_HasReferenceTo + "> ?issueUri . "
                        + "?issueUri a <" + MetadataConstants.c_NS_Alert_Its + MetadataConstants.c_OWLClass_Bug + ">}";
                ResultSet rsIssue1 = QueryExecutionFactory.create(sQuery1, oModel).execSelect();
                while (rsIssue1.hasNext())
                {
                    MetadataGlobal.APIResponseData oMethodIssue = new MetadataGlobal.APIResponseData();
                    oMethodIssue.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_methodIssue + "/";
                    
                    QuerySolution qsIssue = rsIssue1.nextSolution();
                    MetadataGlobal.APIResponseData oIssueUri = new MetadataGlobal.APIResponseData();
                    oIssueUri.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_issue + MetadataConstants.c_XMLE_Uri;
                    oIssueUri.sData = qsIssue.get("?issueUri").toString();
                    oMethodIssue.oData.add(oIssueUri);
                    MetadataGlobal.APIResponseData oRelationLevel = new MetadataGlobal.APIResponseData();
                    oRelationLevel.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_relationLevel;
                    oRelationLevel.sData = "1";
                    oMethodIssue.oData.add(oRelationLevel);
                    
                    oMethod.oData.add(oMethodIssue);
                }
                
                //relations through modules
                String sQuery2 = "SELECT ?issueUri WHERE "
                        + "{?moduleUri <" + MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLObjectProperty_HasMethods + "> <" + sMethodUri.get(i) + "> . "
                        + "?moduleUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLObjectProperty_HasReferenceTo + "> ?issueUri . "
                        + "?issueUri a <" + MetadataConstants.c_NS_Alert_Its + MetadataConstants.c_OWLClass_Bug + "> . "
                        + "?moduleUri a <" + MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLClass_Module + ">}";
                ResultSet rsIssue2 = QueryExecutionFactory.create(sQuery2, oModel).execSelect();
                while (rsIssue2.hasNext())
                {
                    MetadataGlobal.APIResponseData oMethodIssue = new MetadataGlobal.APIResponseData();
                    oMethodIssue.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_methodIssue + "/";
                    
                    QuerySolution qsIssue = rsIssue2.nextSolution();
                    MetadataGlobal.APIResponseData oIssueUri = new MetadataGlobal.APIResponseData();
                    oIssueUri.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_issue + MetadataConstants.c_XMLE_Uri;
                    oIssueUri.sData = qsIssue.get("?issueUri").toString();
                    oMethodIssue.oData.add(oIssueUri);
                    MetadataGlobal.APIResponseData oRelationLevel = new MetadataGlobal.APIResponseData();
                    oRelationLevel.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_relationLevel;
                    oRelationLevel.sData = "2";
                    oMethodIssue.oData.add(oRelationLevel);
                    
                    oMethod.oData.add(oMethodIssue);
                }
                
                //relations through files
                String sQuery3 = "SELECT ?issueUri WHERE "
                        + "{?fileUri <" + MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLObjectProperty_HasModules + "> ?moduleUri . "
                        + "?moduleUri <" + MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLObjectProperty_HasMethods + "> <" + sMethodUri.get(i) + "> . "
                        + "?fileUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLObjectProperty_HasReferenceTo + "> ?issueUri . "
                        + "?issueUri a <" + MetadataConstants.c_NS_Alert_Its + MetadataConstants.c_OWLClass_Bug + "> . "
                        + "?fileUri a <" + MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLClass_File + "> . "
                        + "?moduleUri a <" + MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLClass_Module + ">}";
                ResultSet rsIssue3 = QueryExecutionFactory.create(sQuery3, oModel).execSelect();
                while (rsIssue3.hasNext())
                {
                    MetadataGlobal.APIResponseData oMethodIssue = new MetadataGlobal.APIResponseData();
                    oMethodIssue.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_methodIssue + "/";
                    
                    QuerySolution qsIssue = rsIssue3.nextSolution();
                    MetadataGlobal.APIResponseData oIssueUri = new MetadataGlobal.APIResponseData();
                    oIssueUri.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_issue + MetadataConstants.c_XMLE_Uri;
                    oIssueUri.sData = qsIssue.get("?issueUri").toString();
                    oMethodIssue.oData.add(oIssueUri);
                    MetadataGlobal.APIResponseData oRelationLevel = new MetadataGlobal.APIResponseData();
                    oRelationLevel.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_relationLevel;
                    oRelationLevel.sData = "3";
                    oMethodIssue.oData.add(oRelationLevel);
                    
                    oMethod.oData.add(oMethodIssue);
                }
                
                //relations through commits
                String sQuery4 = "SELECT ?issueUri WHERE "
                        + "{?commitUri <" + MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLObjectProperty_HasMethods + "> <" + sMethodUri.get(i) + "> . "
                        + "?commitUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLObjectProperty_HasReferenceTo + "> ?issueUri . "
                        + "?issueUri a <" + MetadataConstants.c_NS_Alert_Its + MetadataConstants.c_OWLClass_Bug + "> . "
                        + "?commitUri a <" + MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLClass_Commit + ">}";
                ResultSet rsIssue4 = QueryExecutionFactory.create(sQuery4, oModel).execSelect();
                while (rsIssue4.hasNext())
                {
                    MetadataGlobal.APIResponseData oMethodIssue = new MetadataGlobal.APIResponseData();
                    oMethodIssue.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_methodIssue + "/";
                    
                    QuerySolution qsIssue = rsIssue4.nextSolution();
                    MetadataGlobal.APIResponseData oIssueUri = new MetadataGlobal.APIResponseData();
                    oIssueUri.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_issue + MetadataConstants.c_XMLE_Uri;
                    oIssueUri.sData = qsIssue.get("?issueUri").toString();
                    oMethodIssue.oData.add(oIssueUri);
                    MetadataGlobal.APIResponseData oRelationLevel = new MetadataGlobal.APIResponseData();
                    oRelationLevel.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_relationLevel;
                    oRelationLevel.sData = "4";
                    oMethodIssue.oData.add(oRelationLevel);
                    
                    oMethod.oData.add(oMethodIssue);
                }
                
                oData.oData.add(oMethod);
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
            OntModel oModel = MetadataConstants.omModel;
            OntResource resIssue = oModel.getOntResource(sIssueUri);
            StmtIterator siProperties = resIssue.listProperties();
            
            String sIssueAnnotationStatus = "false";
            while (siProperties.hasNext())
            {
                //if annotation property comment exists
                String sPropertyURI = siProperties.next().getPredicate().getURI();
                if (sPropertyURI.equals(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLAnnotationProperty_apDescription)
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
     * @finalModification Dejan Milosavljevic 18.04.2012.
     * @param sIssueID - issue ID
     * @return - APIResponseData object with results
     */
    public static MetadataGlobal.APIResponseData ac_issue_getInfo(String sIssueID)
    {
        MetadataGlobal.APIResponseData oData = new MetadataGlobal.APIResponseData();
        ArrayList<String> sCommentUri = new ArrayList(); //Dejan Milosavljevic 17.04.2012.
        
        MetadataGlobal.APIResponseData oAnnotations = new MetadataGlobal.APIResponseData();
        oAnnotations.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_annotations + "/";
        
        MetadataGlobal.APIResponseData oReferences = new MetadataGlobal.APIResponseData();
        oReferences.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_references + "/";
        
        try
        {
            OntModel oModel = MetadataConstants.omModel;
            
            String sIssueUri = MetadataGlobal.GetObjectURINoCreate(oModel, MetadataConstants.c_NS_Alert_Its + MetadataConstants.c_OWLClass_Bug, sIssueID);
            
            OntResource resBug = oModel.getOntResource(sIssueUri);
            StmtIterator siProperties = resBug.listProperties();
            while (siProperties.hasNext())
            {
                Statement sStatement = siProperties.next();
                String sProperty = sStatement.getPredicate().getURI();
                
                MetadataGlobal.APIResponseData oIssueData = new MetadataGlobal.APIResponseData();
                
                if (sProperty.equals(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_ID))
                {
                    oIssueData.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_issue + MetadataConstants.c_XMLE_Id;
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
                    oIssueData.sData = MetadataGlobal.FormatDateFromSavingToPublish(sStatement.getObject().toString());
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
                    oIssueData.sData = MetadataGlobal.FormatDateFromSavingToPublish(sStatement.getObject().toString());
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
                    oData.oData.add(GetIdAPIResponse(oIssueData.sData, MetadataConstants.c_XMLE_issueBlocks));
                }
                if (sProperty.equals(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLObjectProperty_DependsOn))
                {
                    oIssueData.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_issueDependsOn + MetadataConstants.c_XMLE_Uri;
                    oIssueData.sData = sStatement.getObject().toString();
                    oData.oData.add(GetIdAPIResponse(oIssueData.sData, MetadataConstants.c_XMLE_issueDependsOn));
                }
                if (sProperty.equals(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLObjectProperty_HasReporter))
                {
                    oIssueData.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_issueAuthor + "/";
                    oIssueData.oData.add(GetPersonAPIResponse(sStatement.getObject().toString()));
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
                    oIssueData.oData.add(GetIdAPIResponse(oComponentUri.sData, MetadataConstants.c_XMLE_productComponent));
                    
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
                        oIssueData.oData.add(GetIdAPIResponse(oProductUri.sData, MetadataConstants.c_XMLE_product));
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
                    oIssueData.oData.add(GetIdAPIResponse(oComputerSystemUri.sData, MetadataConstants.c_XMLE_computerSystem));
                    
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
                    oIssueData.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_issueAssignedTo + "/";
                    oIssueData.oData.add(GetPersonAPIResponse(sStatement.getObject().toString()));
                }
                if (sProperty.equals(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLObjectProperty_HasCCPerson))
                {
                    oIssueData.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_issueCCPerson + "/";
                    oIssueData.oData.add(GetPersonAPIResponse(sStatement.getObject().toString()));
                }
                if (sProperty.equals(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLObjectProperty_IsDuplicateOf))
                {
                    oIssueData.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_issueDuplicateOf + MetadataConstants.c_XMLE_Uri;
                    oIssueData.sData = sStatement.getObject().toString();
                    oData.oData.add(GetIdAPIResponse(oIssueData.sData, MetadataConstants.c_XMLE_issueDuplicateOf));
                }
                if (sProperty.equals(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLObjectProperty_IsMergedInto))
                {
                    oIssueData.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_issueMergedInto + MetadataConstants.c_XMLE_Uri;
                    oIssueData.sData = sStatement.getObject().toString();
                    oData.oData.add(GetIdAPIResponse(oIssueData.sData, MetadataConstants.c_XMLE_issueMergedInto));
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
                    oIssueData.oData.add(GetIdAPIResponse(oMilestoneUri.sData, MetadataConstants.c_XMLE_milestone));
                    
                    String sQuery = "SELECT ?milestoneTarget WHERE "
                    + "{<" + oMilestoneUri.sData + "> <" + MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLDataProperty_Target + "> ?milestoneTarget}";

                    ResultSet rsProduct = QueryExecutionFactory.create(sQuery, oModel).execSelect();

                    if (rsProduct.hasNext())
                    {
                        QuerySolution qsIssue = rsProduct.nextSolution();

                        oMilestoneTarget.sData = MetadataGlobal.FormatDateFromSavingToPublish(qsIssue.get("?milestoneTarget").toString());
                        oIssueData.oData.add(oMilestoneTarget);
                    }
                }
                if (sProperty.equals(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLObjectProperty_HasComment))
                {
                    oIssueData.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_issueComment + "/";
                    oIssueData.oData.add(GetCommentAPIResponse(sStatement.getObject().toString()));
                    sCommentUri.add(sStatement.getObject().toString()); //Dejan Milosavljevic 17.04.2012.
                }
                if (sProperty.equals(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLObjectProperty_HasAttachment))
                {
                    oIssueData.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_issueAttachment + "/";
                    oIssueData.oData.add(GetAttachmentAPIResponse(sStatement.getObject().toString()));
                }
                if (sProperty.equals(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLObjectProperty_HasActivity))
                {
                    oIssueData.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_issueActivity + "/";
                    oIssueData.oData.add(GetActivityAPIResponse(sStatement.getObject().toString()));
                }
                
                //KEUI id-s (it is in comments now)
//                if (sProperty.equals(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_KeuiItemId))
//                {
//                    MetadataGlobal.APIResponseData oKeuiItemId = new MetadataGlobal.APIResponseData();
//                    oKeuiItemId.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_itemId;
//                    oKeuiItemId.sData = sStatement.getObject().toString();
//                    oAnnotations.oData.add(oKeuiItemId);
//                }
                
                //Thread id
                if (sProperty.equals(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_KeuiThreadId))
                {
                    MetadataGlobal.APIResponseData oKeuiThreadId = new MetadataGlobal.APIResponseData();
                    oKeuiThreadId.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_threadId;
                    oKeuiThreadId.sData = sStatement.getObject().toString();
                    oAnnotations.oData.add(oKeuiThreadId);
                }
                
                if (oIssueData.sReturnConfig != null)
                    oData.oData.add(oIssueData);
            }
            
            //Annotations
            //  Issue annotations
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
                + " ?conceptUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_Weight + "> ?conceptDataCount}";
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
                    oConceptDataCount.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_weight;
                    oConceptDataCount.sData = sConceptDataCount;
                    oConceptXml.oData.add(oConceptDataCount);

                    oConcept.oData.add(oConceptXml);
                }
            }
                        
            //  Comment annotations
            if (sCommentUri != null && sCommentUri.size() > 0)
            {
                for (String sComment: sCommentUri)
                {
                    //Find annotations for comment
                    String sACQuery = "SELECT ?annotationUri ?annotationName ?annotationText ?itemID WHERE "
                        + "{?annotationUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLObjectProperty_HasObject + "> <" + sComment + "> ."
                        + " ?annotationUri <" + MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLDataProperty_Name + "> ?annotationName ."
                        + " ?annotationUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_Text + "> ?annotationText ."
                        + " <" + sComment + "> <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_KeuiItemId + "> ?itemID}";
                    ResultSet rsCAnnotations = QueryExecutionFactory.create(sACQuery, oModel).execSelect();

                    while (rsCAnnotations.hasNext())
                    {
                        QuerySolution qsCAnnotation = rsCAnnotations.nextSolution();

                        String sCAnnotationUri = qsCAnnotation.get("?annotationUri").toString();
                        String sCAnnotationName = qsCAnnotation.get("?annotationName").toString();
                        String sCAnnotationText = qsCAnnotation.get("?annotationText").toString();
                        String sCItemID = qsCAnnotation.get("?itemID").toString();
                        String sCConceptName = GetConceptName(sCAnnotationName);

                        MetadataGlobal.APIResponseData oCommentTextXml = new MetadataGlobal.APIResponseData();
                        oCommentTextXml.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_issueComment + "/";
                        MetadataGlobal.APIResponseData oCommentTextAnnotatedXml = new MetadataGlobal.APIResponseData();
                        oCommentTextAnnotatedXml.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_commentTextAnnotated;
                        oCommentTextAnnotatedXml.sData = String.format("<![CDATA[%s]]>", sCAnnotationText);
                        oCommentTextXml.oData.add(oCommentTextAnnotatedXml);
                        
                        //Concepts
                        String sCCQuery = "SELECT ?conceptUri ?conceptDataUri ?conceptDataCount WHERE "
                        + "{<" + sCAnnotationUri + "> <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLObjectProperty_HasConcepts + "> ?conceptUri ."
                        + " ?conceptUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_Uri + "> ?conceptDataUri ."
                        + " ?conceptUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_Weight + "> ?conceptDataCount}";
                        ResultSet rsCConcepts = QueryExecutionFactory.create(sCCQuery, oModel).execSelect();

                        MetadataGlobal.APIResponseData oCConcept = new MetadataGlobal.APIResponseData();
                        oCConcept.sReturnConfig = "s3:" + sCConceptName + "/";
                        oCommentTextXml.oData.add(oCConcept);
                        
                        while (rsCConcepts.hasNext())
                        {
                            QuerySolution qsCConcept = rsCConcepts.nextSolution();

                            //String sConceptUri = qsCConcept.get("?conceptUri").toString();
                            String sConceptDataUri = qsCConcept.get("?conceptDataUri").toString();
                            String sConceptDataCount = qsCConcept.get("?conceptDataCount").toString();

                            MetadataGlobal.APIResponseData oConceptXml = new MetadataGlobal.APIResponseData();
                            oConceptXml.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_concept + "/";

                            MetadataGlobal.APIResponseData oConceptDataUri = new MetadataGlobal.APIResponseData();
                            oConceptDataUri.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_uri;
                            oConceptDataUri.sData = sConceptDataUri;
                            oConceptXml.oData.add(oConceptDataUri);

                            MetadataGlobal.APIResponseData oConceptDataCount = new MetadataGlobal.APIResponseData();
                            oConceptDataCount.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_weight;
                            oConceptDataCount.sData = sConceptDataCount;
                            oConceptXml.oData.add(oConceptDataCount);

                            oCConcept.oData.add(oConceptXml);
                        }

                        MetadataGlobal.APIResponseData oItemID = new MetadataGlobal.APIResponseData();
                        oItemID.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_itemId;
                        oItemID.sData = sCItemID;
                        oCommentTextXml.oData.add(oItemID);
                    
                        oAnnotations.oData.add(oCommentTextXml);
                    }
                }
            }
            
            //References
            //  issue
            String sRefIssueQuery = "SELECT ?issueUri ?issueDescription ?threadId WHERE "
                    + "{?issueUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLObjectProperty_HasReferenceTo + "> <" + sIssueUri + "> ."
                    + " ?issueUri a <" + MetadataConstants.c_NS_Alert_Its + MetadataConstants.c_OWLClass_Bug + "> ."
                    + " ?issueUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_KeuiThreadId + "> ?threadId ."
                    + " OPTIONAL {?issueUri <" + MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLDataProperty_Description + "> ?issueDescription}}";
            ResultSet rsRefIssue = QueryExecutionFactory.create(sRefIssueQuery, oModel).execSelect();
                           
            while (rsRefIssue.hasNext())
            {
                MetadataGlobal.APIResponseData oRefIssue = new MetadataGlobal.APIResponseData();
                oRefIssue.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_issue + "/";

                QuerySolution qsRefIssue = rsRefIssue.nextSolution();

                String sRefIssueUri = qsRefIssue.get("?issueUri").toString();
                String sRefIssueDescription = "";
                String sRefThreadId = "";
                if (qsRefIssue.get("?issueDescription") != null)
                    sRefIssueDescription = qsRefIssue.get("?issueDescription").toString();
                if (qsRefIssue.get("?threadId") != null)
                    sRefThreadId = qsRefIssue.get("?threadId").toString();
                
                MetadataGlobal.APIResponseData oRefIssueUri = new MetadataGlobal.APIResponseData();
                oRefIssueUri.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_issue + MetadataConstants.c_XMLE_Uri;
                oRefIssueUri.sData = sRefIssueUri;
                oRefIssue.oData.add(oRefIssueUri);
                
                MetadataGlobal.APIResponseData oRefIssueDesc = new MetadataGlobal.APIResponseData();
                oRefIssueDesc.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_issueDescription;
                oRefIssueDesc.sData = sRefIssueDescription;
                oRefIssue.oData.add(oRefIssueDesc);
                
                MetadataGlobal.APIResponseData oRefThreadId = new MetadataGlobal.APIResponseData();
                oRefThreadId.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_threadId;
                oRefThreadId.sData = sRefThreadId;
                oRefIssue.oData.add(oRefThreadId);
                
                oReferences.oData.add(oRefIssue);
            }
  
            //  commit
            String sRefCommitQuery = "SELECT ?commitUri ?commitMessageLog ?itemId WHERE "
                    + "{?commitUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLObjectProperty_HasReferenceTo + "> <" + sIssueUri + "> ."
                    + " ?commitUri a <" + MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLClass_Commit + "> ."
                    + " ?commitUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_KeuiItemId + "> ?itemId ."
                    + " OPTIONAL {?commitUri <" + MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLDataProperty_CommitMessage + "> ?commitMessageLog}}";
            ResultSet rsRefCommit = QueryExecutionFactory.create(sRefCommitQuery, oModel).execSelect();
                           
            while (rsRefCommit.hasNext())
            {
                MetadataGlobal.APIResponseData oRefCommit = new MetadataGlobal.APIResponseData();
                oRefCommit.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_commit + "/";

                QuerySolution qsRefCommit = rsRefCommit.nextSolution();

                String sRefCommitUri = qsRefCommit.get("?commitUri").toString();
                String sRefCommitMessageLog = "";
                String sRefItemId = "";
                if (qsRefCommit.get("?commitMessageLog") != null)
                    sRefCommitMessageLog = qsRefCommit.get("?commitMessageLog").toString();
                if (qsRefCommit.get("?itemId") != null)
                    sRefItemId = qsRefCommit.get("?itemId").toString();
                
                MetadataGlobal.APIResponseData oRefCommitUri = new MetadataGlobal.APIResponseData();
                oRefCommitUri.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_commit + MetadataConstants.c_XMLE_Uri;
                oRefCommitUri.sData = sRefCommitUri;
                oRefCommit.oData.add(oRefCommitUri);
                
                MetadataGlobal.APIResponseData oRefCommitMLog = new MetadataGlobal.APIResponseData();
                oRefCommitMLog.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_commitMessageLog;
                oRefCommitMLog.sData = sRefCommitMessageLog;
                oRefCommit.oData.add(oRefCommitMLog);
                
                MetadataGlobal.APIResponseData oRefItemId = new MetadataGlobal.APIResponseData();
                oRefItemId.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_itemId;
                oRefItemId.sData = sRefItemId;
                oRefCommit.oData.add(oRefItemId);
                
                oReferences.oData.add(oRefCommit);
            }
            
            //  email
            String sRefEmailQuery = "SELECT ?emailUri ?emailSubject ?itemId WHERE "
                    + "{?emailUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLObjectProperty_HasReferenceTo + "> <" + sIssueUri + "> ."
                    + " ?emailUri a <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_Email + "> ."
                    + " ?emailUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_KeuiItemId + "> ?itemId ."
                    + " OPTIONAL {?emailUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_Subject + "> ?emailSubject}}";
            ResultSet rsRefEmail = QueryExecutionFactory.create(sRefEmailQuery, oModel).execSelect();
                           
            while (rsRefEmail.hasNext())
            {
                MetadataGlobal.APIResponseData oRefEmail = new MetadataGlobal.APIResponseData();
                oRefEmail.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_email + "/";

                QuerySolution qsRefEmail = rsRefEmail.nextSolution();

                String sRefEmailUri = qsRefEmail.get("?emailUri").toString();
                String sRefEmailSubject = "";
                String sRefItemId = "";
                if (qsRefEmail.get("?emailSubject") != null)
                    sRefEmailSubject = qsRefEmail.get("?emailSubject").toString();
                if (qsRefEmail.get("?itemId") != null)
                    sRefItemId = qsRefEmail.get("?itemId").toString();
                
                MetadataGlobal.APIResponseData oRefEmailUri = new MetadataGlobal.APIResponseData();
                oRefEmailUri.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_email + MetadataConstants.c_XMLE_Uri;
                oRefEmailUri.sData = sRefEmailUri;
                oRefEmail.oData.add(oRefEmailUri);
                
                MetadataGlobal.APIResponseData oRefEmailSubject = new MetadataGlobal.APIResponseData();
                oRefEmailSubject.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_subject;
                oRefEmailSubject.sData = sRefEmailSubject;
                oRefEmail.oData.add(oRefEmailSubject);
                
                MetadataGlobal.APIResponseData oRefItemId = new MetadataGlobal.APIResponseData();
                oRefItemId.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_itemId;
                oRefItemId.sData = sRefItemId;
                oRefEmail.oData.add(oRefItemId);
                
                oReferences.oData.add(oRefEmail);
            }
            
            //  forumPost
            String sRefPostQuery = "SELECT ?forumPostUri ?forumPostSubject ?itemId WHERE "
                    + "{?forumPostUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLObjectProperty_HasReferenceTo + "> <" + sIssueUri + "> ."
                    + " ?forumPostUri a <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_post + "> ."
                    + " ?forumPostUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_KeuiItemId + "> ?itemId ."
                    + " OPTIONAL {?forumPostUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_Subject + "> ?forumPostSubject}}";
            ResultSet rsRefPost = QueryExecutionFactory.create(sRefPostQuery, oModel).execSelect();
                           
            while (rsRefPost.hasNext())
            {
                MetadataGlobal.APIResponseData oRefPost = new MetadataGlobal.APIResponseData();
                oRefPost.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_forumPost + "/";

                QuerySolution qsRefpost = rsRefPost.nextSolution();

                String sRefPostUri = qsRefpost.get("?forumPostUri").toString();
                String sRefPostSubject = "";
                String sRefItemId = "";
                if (qsRefpost.get("?forumPostSubject") != null)
                    sRefPostSubject = qsRefpost.get("?forumPostSubject").toString();
                if (qsRefpost.get("?itemId") != null)
                    sRefItemId = qsRefpost.get("?itemId").toString();
                
                MetadataGlobal.APIResponseData oRefPostUri = new MetadataGlobal.APIResponseData();
                oRefPostUri.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_post + MetadataConstants.c_XMLE_Uri;
                oRefPostUri.sData = sRefPostUri;
                oRefPost.oData.add(oRefPostUri);
                
                MetadataGlobal.APIResponseData oRefPostSubject = new MetadataGlobal.APIResponseData();
                oRefPostSubject.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_subject;
                oRefPostSubject.sData = sRefPostSubject;
                oRefPost.oData.add(oRefPostSubject);
                
                MetadataGlobal.APIResponseData oRefItemId = new MetadataGlobal.APIResponseData();
                oRefItemId.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_itemId;
                oRefItemId.sData = sRefItemId;
                oRefPost.oData.add(oRefItemId);
                
                oReferences.oData.add(oRefPost);
            }
            
            //  wikiPost
            String sRefWikiQuery = "SELECT ?wikiPageUri ?wikiPageRawText ?itemId WHERE "
                    + "{?wikiPageUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLObjectProperty_HasReferenceTo + "> <" + sIssueUri + "> ."
                    + " ?wikiPageUri a <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_WikiPage + "> ."
                    + " ?wikiPageUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_KeuiItemId + "> ?itemId ."
                    + " OPTIONAL {?wikiPageUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_RawText + "> ?wikiPageRawText}}";
            ResultSet rsRefWiki = QueryExecutionFactory.create(sRefWikiQuery, oModel).execSelect();
                           
            while (rsRefWiki.hasNext())
            {
                MetadataGlobal.APIResponseData oRefWiki = new MetadataGlobal.APIResponseData();
                oRefWiki.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_wikiPage + "/";

                QuerySolution qsRefWiki = rsRefWiki.nextSolution();

                String sRefWikiUri = qsRefWiki.get("?wikiPageUri").toString();
                String sRefWikiRawText = "";
                String sRefItemId = "";
                if (qsRefWiki.get("?wikiPageRawText") != null)
                    sRefWikiRawText = qsRefWiki.get("?wikiPageRawText").toString();
                if (qsRefWiki.get("?itemId") != null)
                    sRefItemId = qsRefWiki.get("?itemId").toString();
                
                MetadataGlobal.APIResponseData oRefWikiUri = new MetadataGlobal.APIResponseData();
                oRefWikiUri.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_wikiPage + MetadataConstants.c_XMLE_Uri;
                oRefWikiUri.sData = sRefWikiUri;
                oRefWiki.oData.add(oRefWikiUri);
                
                MetadataGlobal.APIResponseData oRefWikiTextRaw = new MetadataGlobal.APIResponseData();
                oRefWikiTextRaw.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_rawText;
                oRefWikiTextRaw.sData = sRefWikiRawText;
                oRefWiki.oData.add(oRefWikiTextRaw);
                
                MetadataGlobal.APIResponseData oRefItemId = new MetadataGlobal.APIResponseData();
                oRefItemId.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_itemId;
                oRefItemId.sData = sRefItemId;
                oRefWiki.oData.add(oRefItemId);
                
                oReferences.oData.add(oRefWiki);
            }
            
            //oIssueData.oData.add(oAnnotations);
            if (oAnnotations.sReturnConfig != null)
                oData.oData.add(oAnnotations);
            if (oReferences.sReturnConfig != null)
                oData.oData.add(oReferences);
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
            OntModel oModel = MetadataConstants.omModel;
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
            OntModel oModel = MetadataConstants.omModel;
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
     * @summary issue_getOpen
     * @startRealisation  Sasa Stojanovic 12.03.2012.
     * @finalModification Sasa Stojanovic 12.03.2012.
     * @param sProductID - product id
     * @return - APIResponseData object with results
     */
    public static MetadataGlobal.APIResponseData ac_issue_getOpen(String sProductID)
    {
        MetadataGlobal.APIResponseData oData = new MetadataGlobal.APIResponseData();
        try
        {
            OntModel oModel = MetadataConstants.omModel;
            String sStateOpenUri = MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLClass_Open;
            
            String sProductUri = MetadataGlobal.GetObjectURINoCreate(oModel, MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLClass_Product, sProductID);
     
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
            OntModel oModel = MetadataConstants.omModel;
            OntResource resPerson = oModel.getOntResource(sPersonUri);
            StmtIterator siProperties = resPerson.listProperties();
            String sFirstName = "";
            String sLastName = "";
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
                    sFirstName = sStatement.getObject().toString();
                }
                if (sProperty.equals(MetadataConstants.c_NS_foaf + MetadataConstants.c_OWLDataProperty_LastName))
                {
                    sLastName = sStatement.getObject().toString();
                }
                
                if (oPersonData.sReturnConfig != null)
                    oData.oData.add(oPersonData);
            }
            
            //spajanje imena i prezimena u jedan objekat
            String sName = sFirstName + " " + sLastName;

            if (!sName.equalsIgnoreCase(" "))
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
            OntModel oModel = MetadataConstants.omModel;
                        
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
     * @summary identity_getForPerson
     * @startRealisation Sasa Stojanovic 18.01.2012.
     * @finalModification Sasa Stojanovic 25.09.2012.
     * @param sPersonUri - person uri
     * @return - APIResponseData object with results
     */
    public static MetadataGlobal.APIResponseData ac_identity_getForPerson(String sPersonUri)
    {
        MetadataGlobal.APIResponseData oData = new MetadataGlobal.APIResponseData();
        try
        {
            OntModel oModel = MetadataConstants.omModel;
            
            if (!sPersonUri.isEmpty())
            {
                String sQuery = "SELECT ?uuid WHERE {"
                        + "?identityUri a <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_Identity + "> . "
                        + "?identityUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_ID + "> ?uuid . "
                        + "?identityUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLObjectProperty_IsPerson + "> <" + sPersonUri + ">";
                sQuery += "}";

                ResultSet rsPerson = QueryExecutionFactory.create(sQuery, oModel).execSelect();

                while (rsPerson.hasNext())
                {
                    QuerySolution qsPerson = rsPerson.nextSolution();

                    MetadataGlobal.APIResponseData oPersonUri = new MetadataGlobal.APIResponseData();
                    oPersonUri.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_uuid;
                    oPersonUri.sData = qsPerson.get("?uuid").toString();

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
            OntModel omModel = MetadataConstants.omModel;
                        
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
                            oAttribute.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_contribution + "/";
                        }
                        if (oClass.hasURI(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_Effectiveness))
                        {
                            oAttribute.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_effectiveness + "/";
                        }
                        if (oClass.hasURI(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_Activity_Availability))
                        {
                            oAttribute.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_recency + "/";
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
                                    oMetric.sData = MetadataGlobal.FormatDateFromSavingToPublish(qsMetric.get("?time").toString());
                                    
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
     * @finalModification Sasa Stojanovic 24.05.2012.
     * @param sIdentityId - identity id
     * @return - APIResponseData object with results
     */
    public static MetadataGlobal.APIResponseData ac_method_getAllForIdentity(String sIdentityId)
    {
        MetadataGlobal.APIResponseData oData = new MetadataGlobal.APIResponseData();
        try
        {
            OntModel oModel = MetadataConstants.omModel;
            
            //get list of persons for identity
            ArrayList <String> sPersons = new ArrayList<String>();
            String sQuery = "SELECT DISTINCT ?personUri WHERE "
                    + "{ ?personUri a <" + MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLClass_Person + "> . "
                    + "?identityUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLObjectProperty_IsPerson + "> ?personUri . "
                    + "?identityUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_ID + "> \"" + sIdentityId + "\" }";
                            
            ResultSet rsPersons = QueryExecutionFactory.create(sQuery, oModel).execSelect();
            while (rsPersons.hasNext())
            {
                QuerySolution qsPersons = rsPersons.nextSolution();
                sPersons.add(qsPersons.get("?personUri").toString());
            }
            
            //get list of commits related to identity persons
            ArrayList <String> sCommits = new ArrayList<String>();
            String sCommitQuery = "SELECT DISTINCT ?commitUri WHERE {"
                    + "?commitUri a <" + MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLClass_Commit + "> . ";
            for (int i = 0; i < sPersons.size(); i++)
            {
                if (i > 0)
                {
                    sCommitQuery += " UNION ";
                }
                sCommitQuery += "{ ?commitUri <" + MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLObjectProperty_HasAuthor + "> <" + sPersons.get(i) + "> }"
                    + " UNION "
                    + "{ ?commitUri <" + MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLObjectProperty_HasCommitter + "> <" + sPersons.get(i) + "> }";
            }
            sCommitQuery += "}";
            
            ResultSet rsCommits = QueryExecutionFactory.create(sCommitQuery, oModel).execSelect();
            while (rsCommits.hasNext())
            {
                QuerySolution qsCommits = rsCommits.nextSolution();
                sCommits.add(qsCommits.get("?commitUri").toString());
            }
            
            //get files from commits
            String sFilesQuery = "SELECT DISTINCT ?fileUri ?fileName ?fileBranch WHERE {"
                    + "?fileUri a <" + MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLClass_File + "> . "
                    + "?fileUri <" + MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLDataProperty_Name + "> ?fileName . "
                    + "?fileUri <" + MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLDataProperty_OnBranch + "> ?fileBranch . ";
            for (int i = 0; i < sCommits.size(); i++)
            {
                if (i > 0)
                {
                    sFilesQuery += " UNION ";
                }
                sFilesQuery += "{ <" + sCommits.get(i) + "> <" + MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLObjectProperty_HasFile + "> ?fileUri }";
            }
            sFilesQuery += "}";
                            
            ResultSet rsFiles = QueryExecutionFactory.create(sFilesQuery, oModel).execSelect();
            while (rsFiles.hasNext())
            {
                MetadataGlobal.APIResponseData oFile = new MetadataGlobal.APIResponseData();
                oFile.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_file + "/";
                
                QuerySolution qsFiles = rsFiles.nextSolution();
                
                MetadataGlobal.APIResponseData oFileUri = new MetadataGlobal.APIResponseData();
                oFileUri.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_file + MetadataConstants.c_XMLE_Uri;
                oFileUri.sData = qsFiles.get("?fileUri").toString();
                oFile.oData.add(oFileUri);
                
                MetadataGlobal.APIResponseData oFileName = new MetadataGlobal.APIResponseData();
                oFileName.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_fileName;
                oFileName.sData = qsFiles.get("?fileName").toString();
                oFile.oData.add(oFileName);
                
                MetadataGlobal.APIResponseData oFileBranch = new MetadataGlobal.APIResponseData();
                oFileBranch.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_fileBranch;
                oFileBranch.sData = qsFiles.get("?fileBranch").toString();
                oFile.oData.add(oFileBranch);
                
                //get get modules from file and commits
                String sModulesQuery = "SELECT DISTINCT ?moduleUri ?moduleName WHERE {"
                        + "?moduleUri a <" + MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLClass_Module + "> . "
                        + "?moduleUri <" + MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLDataProperty_Name + "> ?moduleName . "
                        + "<" + oFileUri.sData + "> <" + MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLObjectProperty_HasModules + "> ?moduleUri . ";
                for (int i = 0; i < sCommits.size(); i++)
                {
                    if (i > 0)
                    {
                        sModulesQuery += " UNION ";
                    }
                    sModulesQuery += "{ <" + sCommits.get(i) + "> <" + MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLObjectProperty_HasModules + "> ?moduleUri }";
                }
                sModulesQuery += "}";
                
                ResultSet rsModules = QueryExecutionFactory.create(sModulesQuery, oModel).execSelect();
                while (rsModules.hasNext())
                {
                    MetadataGlobal.APIResponseData oModule = new MetadataGlobal.APIResponseData();
                    oModule.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_fileModules + "/";

                    QuerySolution qsModule = rsModules.nextSolution();

                    MetadataGlobal.APIResponseData oModuleUri = new MetadataGlobal.APIResponseData();
                    oModuleUri.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_module + MetadataConstants.c_XMLE_Uri;
                    oModuleUri.sData = qsModule.get("?moduleUri").toString();
                    oModule.oData.add(oModuleUri);

                    MetadataGlobal.APIResponseData oModuleName = new MetadataGlobal.APIResponseData();
                    oModuleName.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_moduleName;
                    oModuleName.sData = qsModule.get("?moduleName").toString();
                    oModule.oData.add(oModuleName);
                    
                    //get get methods from module and commits
                    String sMethodsQuery = "SELECT DISTINCT ?methodUri ?methodName WHERE {"
                            + "?methodUri a <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_Method + "> . "
                            + "?methodUri <" + MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLDataProperty_Name + "> ?methodName . "
                            + "<" + oModuleUri.sData + "> <" + MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLObjectProperty_HasMethods + "> ?methodUri . ";
                    for (int i = 0; i < sCommits.size(); i++)
                    {
                        if (i > 0)
                        {
                            sMethodsQuery += " UNION ";
                        }
                        sMethodsQuery += "{ <" + sCommits.get(i) + "> <" + MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLObjectProperty_HasMethods + "> ?methodUri }";
                    }
                    sMethodsQuery += "}";

                    ResultSet rsMethods = QueryExecutionFactory.create(sMethodsQuery, oModel).execSelect();
                    while (rsMethods.hasNext())
                    {
                        MetadataGlobal.APIResponseData oMethod = new MetadataGlobal.APIResponseData();
                        oMethod.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_moduleMethods + "/";

                        QuerySolution qsMethod = rsMethods.nextSolution();

                        MetadataGlobal.APIResponseData oMethodUri = new MetadataGlobal.APIResponseData();
                        oMethodUri.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_method + MetadataConstants.c_XMLE_Uri;
                        oMethodUri.sData = qsMethod.get("?methodUri").toString();
                        oMethod.oData.add(oMethodUri);

                        MetadataGlobal.APIResponseData oMethodName = new MetadataGlobal.APIResponseData();
                        oMethodName.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_methodName;
                        oMethodName.sData = qsMethod.get("?methodName").toString();
                        oMethod.oData.add(oMethodName);
                        
                        oModule.oData.add(oMethod);
                    }
                    oFile.oData.add(oModule);
                }
                oData.oData.add(oFile);
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
            OntModel oModel = MetadataConstants.omModel;
                        
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
                    + "?issueUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLAnnotationProperty_apDescription + "> ?subject . FILTER regex(?subject, \"" + sKeyword + "\", \"i\")}"
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
            OntModel oModel = MetadataConstants.omModel;
            
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
     * @summary commit_getAllForProduct
     * @startRealisation  Dejan Milosavljevic 15.03.2012.
     * @finalModification Sasa Stojanovic 06.04.2012.
     * @param sProductID - product id
     * @param dtmFromDate - date from filter
     * @return - APIResponseData object with results
     */
    public static MetadataGlobal.APIResponseData ac_commit_getAllForProduct(String sProductID, Date dtmFromDate)
    {
        MetadataGlobal.APIResponseData oData = new MetadataGlobal.APIResponseData();
        try
        {
            OntModel oModel = MetadataConstants.omModel;
                      
            String sProductUri = MetadataGlobal.GetObjectURINoCreate(oModel, MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLClass_Product, sProductID);

            String sQuery = "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> "
                    + "SELECT ?commitUri ?commitMessageLog ?commitDate ?commitAuthorUri WHERE "
                    + "{?commitUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLObjectProperty_IsCommitOf + ">  ?componentUri ."
                    + " ?componentUri  <" + MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLObjectProperty_IsComponentOf + "> <" + sProductUri + "> ."
                    + " ?commitUri <" + MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLDataProperty_CommitMessage + "> ?commitMessageLog ."
                    + " ?commitUri <" + MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLDataProperty_CommitDate + "> ?commitDate ."
                    + " ?commitUri <" + MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLObjectProperty_HasAuthor + "> ?commitAuthorUri ."
                    + " FILTER ( ?commitDate > \"" + MetadataGlobal.FormatDateForSaving(dtmFromDate) + "\"^^xsd:dateTime )}";

            ResultSet rsCommit = QueryExecutionFactory.create(sQuery, oModel).execSelect();
            
            while (rsCommit.hasNext())
            {
                MetadataGlobal.APIResponseData oCommit = new MetadataGlobal.APIResponseData();
                oCommit.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_commit + "/";
            
                QuerySolution qsCommit = rsCommit.nextSolution();
                
                MetadataGlobal.APIResponseData oCommitUri = new MetadataGlobal.APIResponseData();
                MetadataGlobal.APIResponseData oCommitMessageLog = new MetadataGlobal.APIResponseData();
                MetadataGlobal.APIResponseData oCommitDate = new MetadataGlobal.APIResponseData();
                MetadataGlobal.APIResponseData oCommitAuthor = new MetadataGlobal.APIResponseData();
                
                oCommitUri.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_commit + MetadataConstants.c_XMLE_Uri;
                oCommitMessageLog.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_commitMessageLog;
                oCommitDate.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_commitDate;
                oCommitAuthor.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_commitCommtter + "/";
                
                oCommitUri.sData = qsCommit.get("?commitUri").toString();
                oCommitMessageLog.sData = qsCommit.get("?commitMessageLog").toString();
                oCommitDate.sData = MetadataGlobal.FormatDateFromSavingToPublish(qsCommit.get("?commitDate").toString());
                oCommitAuthor.oData.add(GetPersonAPIResponse(qsCommit.get("?commitAuthorUri").toString()));
                
                oCommit.oData.add(oCommitUri);
                oCommit.oData.add(oCommitMessageLog);
                oCommit.oData.add(oCommitDate);
                oCommit.oData.add(oCommitAuthor);
                
                String sQueryNumber = "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> "
                    + "SELECT (count(?commitFile) AS ?commitFileNumber) WHERE "
                    + "{<" + oCommitUri.sData + "> <" + MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLObjectProperty_HasFile + "> ?commitFile ."
                    + " <" + oCommitUri.sData + "> <" + MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLDataProperty_CommitDate + "> ?commitDate ."
                    + " FILTER ( ?commitDate > \"" + MetadataGlobal.FormatDateForSaving(dtmFromDate) + "\"^^xsd:dateTime )}";

                ResultSet rsCommitFileNumber = QueryExecutionFactory.create(sQueryNumber, oModel).execSelect();

                while (rsCommitFileNumber.hasNext())
                {
                    QuerySolution qsCommitFile = rsCommitFileNumber.nextSolution();
                    MetadataGlobal.APIResponseData oCommitFileNumber = new MetadataGlobal.APIResponseData();
                    oCommitFileNumber.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_commitFileNumber;
                    String sFileNumber = qsCommitFile.get("?commitFileNumber").toString();
                    sFileNumber = sFileNumber.substring(0, sFileNumber.indexOf("^^"));
                    oCommitFileNumber.sData = sFileNumber;
                    oCommit.oData.add(oCommitFileNumber);
                }

                oData.oData.add(oCommit);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return oData;
    }
    
    /**
     * @summary issue_getInfo
     * @startRealisation Sasa Stojanovic 18.05.2012.
     * @finalModification Sasa Stojanovic 18.05.2012.
     * @param sCommitUri - commit URI
     * @return - APIResponseData object with results
     */
    public static MetadataGlobal.APIResponseData ac_commit_getInfo(String sCommitUri)
    {
        MetadataGlobal.APIResponseData oData = new MetadataGlobal.APIResponseData();
        
        try
        {
            OntModel oModel = MetadataConstants.omModel;
            
            OntResource resCommit = oModel.getOntResource(sCommitUri);
            StmtIterator siCommitProperties = resCommit.listProperties();
            while (siCommitProperties.hasNext())
            {
                Statement sStatement = siCommitProperties.next();
                String sCommitProperty = sStatement.getPredicate().getURI();
                
                MetadataGlobal.APIResponseData oCommitData = new MetadataGlobal.APIResponseData();
                
                if (sCommitProperty.equals(MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLObjectProperty_IsCommitOfRepository))
                {
                    oCommitData.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_commitRepository + MetadataConstants.c_XMLE_Uri;
                    oCommitData.sData = sStatement.getObject().toString();
                }
                
                if (sCommitProperty.equals(MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLDataProperty_RevisionTag))
                {
                    oCommitData.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_commitRevisionTag;
                    oCommitData.sData = sStatement.getObject().toString();
                }
                
                if (sCommitProperty.equals(MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLObjectProperty_HasAuthor))
                {
                    oCommitData.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_commitAuthor + "/";
                    oCommitData.oData.add(GetPersonAPIResponse(sStatement.getObject().toString()));
                }
                
                if (sCommitProperty.equals(MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLObjectProperty_HasCommitter))
                {
                    oCommitData.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_commitCommtter + "/";
                    oCommitData.oData.add(GetPersonAPIResponse(sStatement.getObject().toString()));
                }
                
                if (sCommitProperty.equals(MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLDataProperty_CommitDate))
                {
                    oCommitData.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_commitDate;
                    oCommitData.sData = MetadataGlobal.FormatDateFromSavingToPublish(sStatement.getObject().toString());
                }
                
                if (sCommitProperty.equals(MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLDataProperty_CommitMessage))
                {
                    oCommitData.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_commitMessageLog;
                    oCommitData.sData = sStatement.getObject().toString();
                }
                
                if (sCommitProperty.equals(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLObjectProperty_IsCommitOf))
                {
                    oCommitData.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_commitProduct + "/";
                    
                    MetadataGlobal.APIResponseData oProductUri = new MetadataGlobal.APIResponseData();
                    MetadataGlobal.APIResponseData oComponentUri = new MetadataGlobal.APIResponseData();
                    MetadataGlobal.APIResponseData oProductVersion = new MetadataGlobal.APIResponseData();
                    
                    oProductUri.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_product + MetadataConstants.c_XMLE_Uri;
                    oComponentUri.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_productComponent + MetadataConstants.c_XMLE_Uri;
                    oProductVersion.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_productVersion;
                    
                    oComponentUri.sData = sStatement.getObject().toString();
                    oCommitData.oData.add(oComponentUri);
                    oCommitData.oData.add(GetIdAPIResponse(oComponentUri.sData, MetadataConstants.c_XMLE_productComponent));
                    
                    String sQuery = "SELECT ?productUri ?productVersion WHERE "
                    + "{<" + oComponentUri.sData + ">  <" + MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLObjectProperty_IsComponentOf + "> ?productUri ."
                    + " ?productUri <" + MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLDataProperty_Version + "> ?productVersion}";

                    ResultSet rsProduct = QueryExecutionFactory.create(sQuery, oModel).execSelect();

                    if (rsProduct.hasNext())
                    {
                        QuerySolution qsIssue = rsProduct.nextSolution();

                        oProductUri.sData = qsIssue.get("?productUri").toString();
                        oProductVersion.sData = qsIssue.get("?productVersion").toString();

                        oCommitData.oData.add(oProductUri);
                        oCommitData.oData.add(GetIdAPIResponse(oProductUri.sData, MetadataConstants.c_XMLE_product));
                        oCommitData.oData.add(oProductVersion);
                    }
                }
                
                if (sCommitProperty.equals(MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLObjectProperty_HasFile))
                {
                    oCommitData.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_commitFile + "/";

                    //Load file data
                    String sFileUri = sStatement.getObject().toString();
                    OntResource resFile = oModel.getOntResource(sFileUri);

                    MetadataGlobal.APIResponseData oFUri = new MetadataGlobal.APIResponseData();
                    oFUri.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_file + MetadataConstants.c_XMLE_Uri;
                    oFUri.sData = sFileUri;
                    oCommitData.oData.add(oFUri);

                    StmtIterator siFileProperties = resFile.listProperties();
                    while (siFileProperties.hasNext())
                    {
                        Statement sFileStatement = siFileProperties.next();
                        String sProperty = sFileStatement.getPredicate().getURI();
                        
                        MetadataGlobal.APIResponseData oFileData = new MetadataGlobal.APIResponseData();
                        
                        //File id
                        if (sProperty.equals(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_ID))
                        {
                            oFileData.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_file + MetadataConstants.c_XMLE_Id;
                            oFileData.sData = sFileStatement.getObject().toString();
                        }
                        
                        //File name
                        if (sProperty.equals(MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLDataProperty_Name))
                        {
                            oFileData.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_fileName;
                            oFileData.sData = sFileStatement.getObject().toString();
                        }
                        
                        //File branch
                        if (sProperty.equals(MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLDataProperty_OnBranch))
                        {
                            oFileData.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_fileBranch;
                            oFileData.sData = sFileStatement.getObject().toString();
                        }
                        
                        //File action
                        if (sProperty.equals(MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLObjectProperty_HasAction))
                        {
                            oFileData.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_fileAction;
                            
                            String sQuery = "SELECT ?actionType WHERE "
                            + "{<" + sFileStatement.getObject().toString() + "> <" + MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLObjectProperty_HasAction + "> ?actionType ."
                            + " <" + sFileStatement.getObject().toString() + "> <" + MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLObjectProperty_HasCommit + "> <" + sCommitUri + ">}";
                            ResultSet rsAction = QueryExecutionFactory.create(sQuery, oModel).execSelect();
                            if (rsAction.hasNext())
                            {
                                QuerySolution qsIssue = rsAction.nextSolution();
                                oFileData.sData = qsIssue.get("?actionType").toString().replace(MetadataConstants.c_NS_Alert_Scm, "");
                            }
                        }

                        //Modules
                        if (sProperty.equals(MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLObjectProperty_HasModules))
                        {
                            String sModuleUri = sFileStatement.getObject().toString();
                            
                            String sModuleQuery = "SELECT (count(*) AS ?number) WHERE "
                                    + "{<" + sCommitUri + "> <" + MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLObjectProperty_HasModules + "> <" + sModuleUri + ">}";

                            ResultSet rsModuleNumber = QueryExecutionFactory.create(sModuleQuery, oModel).execSelect();

                            while (rsModuleNumber.hasNext())
                            {
                                QuerySolution qsModuleNumber = rsModuleNumber.nextSolution();
                                String sModuleNumber = qsModuleNumber.get("?number").toString();
                                sModuleNumber = sModuleNumber.substring(0, sModuleNumber.indexOf("^^"));
                                
                                if (sModuleNumber.equals("1"))
                                {
                                    oFileData.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_fileModules + "/";
                                    OntResource resModule = oModel.getOntResource(sModuleUri);

                                    MetadataGlobal.APIResponseData oMUri = new MetadataGlobal.APIResponseData();
                                    oMUri.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_module + MetadataConstants.c_XMLE_Uri;
                                    oMUri.sData = sModuleUri;
                                    oFileData.oData.add(oMUri);

                                    StmtIterator siModuleProps = resModule.listProperties();
                                    while (siModuleProps.hasNext())
                                    {
                                        Statement sMStatement = siModuleProps.next();
                                        String sMProperty = sMStatement.getPredicate().getURI();

                                        MetadataGlobal.APIResponseData oModuleData = new MetadataGlobal.APIResponseData();

                                        //ID
                                        if (sMProperty.equals(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_ID))
                                        {
                                            oModuleData.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_module + MetadataConstants.c_XMLE_Id;
                                            oModuleData.sData = sMStatement.getObject().toString();
                                        }

                                        //Name
                                        if (sMProperty.equals(MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLDataProperty_Name))
                                        {
                                            oModuleData.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_moduleName;
                                            oModuleData.sData = sMStatement.getObject().toString();
                                        }

                                        //Start line
                                        if (sMProperty.equals(MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLDataProperty_StartLine))
                                        {
                                            oModuleData.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_moduleStartLine;
                                            oModuleData.sData = sMStatement.getObject().toString();
                                        }

                                        //End line
                                        if (sMProperty.equals(MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLDataProperty_EndLine))
                                        {
                                            oModuleData.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_moduleEndLine;
                                            oModuleData.sData = sMStatement.getObject().toString();
                                        }

                                        //Methods
                                        if (sMProperty.equals(MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLObjectProperty_HasMethods))
                                        {
                                            String sMethodUri = sMStatement.getObject().toString();
                                            
                                            String sMethodQuery = "SELECT (count(*) AS ?number) WHERE "
                                                    + "{<" + sCommitUri + "> <" + MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLObjectProperty_HasMethods + "> <" + sMethodUri + ">}";

                                            ResultSet rsMethodNumber = QueryExecutionFactory.create(sMethodQuery, oModel).execSelect();

                                            while (rsMethodNumber.hasNext())
                                            {
                                                QuerySolution qsMethodNumber = rsMethodNumber.nextSolution();
                                                String sMethodNumber = qsMethodNumber.get("?number").toString();
                                                sMethodNumber = sMethodNumber.substring(0, sMethodNumber.indexOf("^^"));

                                                if (sMethodNumber.equals("1"))
                                                {
                                                    oModuleData.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_moduleMethods + "/";
                                                    OntResource resMethod = oModel.getOntResource(sMethodUri);

                                                    MetadataGlobal.APIResponseData oMetUri = new MetadataGlobal.APIResponseData();
                                                    oMetUri.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_method + MetadataConstants.c_XMLE_Uri;
                                                    oMetUri.sData = sMethodUri;
                                                    oModuleData.oData.add(oMetUri);

                                                    StmtIterator siMethodProps = resMethod.listProperties();
                                                    while (siMethodProps.hasNext())
                                                    {
                                                        Statement sMetStatement = siMethodProps.next();
                                                        String sMetProperty = sMetStatement.getPredicate().getURI();

                                                        MetadataGlobal.APIResponseData oMethodData = new MetadataGlobal.APIResponseData();

                                                        //ID
                                                        if (sMetProperty.equals(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_ID))
                                                        {
                                                            oMethodData.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_method + MetadataConstants.c_XMLE_Id;
                                                            oMethodData.sData = sMetStatement.getObject().toString();
                                                        }

                                                        //Name
                                                        if (sMetProperty.equals(MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLDataProperty_Name))
                                                        {
                                                            oMethodData.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_methodName;
                                                            oMethodData.sData = sMetStatement.getObject().toString();
                                                        }

                                                        //Start line
                                                        if (sMetProperty.equals(MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLDataProperty_StartLine))
                                                        {
                                                            oMethodData.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_methodStartLine;
                                                            oMethodData.sData = sMetStatement.getObject().toString();
                                                        }

                                                        //End line
                                                        if (sMetProperty.equals(MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLDataProperty_EndLine))
                                                        {
                                                            oMethodData.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_methodEndLine;
                                                            oMethodData.sData = sMetStatement.getObject().toString();
                                                        }
                                                        oModuleData.oData.add(oMethodData);
                                                    }
                                                }
                                            }
                                        }
                                        oFileData.oData.add(oModuleData);
                                    }
                                }
                            }
                        }
                        oCommitData.oData.add(oFileData);
                    }
                }
                oData.oData.add(oCommitData);
            }
            
            //References with issues
            MetadataGlobal.APIResponseData oReferences = new MetadataGlobal.APIResponseData();
            oReferences.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_references + "/";
        
            String sRefIssueQuery = "SELECT ?issueUri ?issueDescription WHERE "
                    + "{ <" + sCommitUri + "> <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLObjectProperty_HasReferenceTo + "> ?issueUri ."
                    + " ?issueUri a <" + MetadataConstants.c_NS_Alert_Its + MetadataConstants.c_OWLClass_Bug + "> ."
                    + " OPTIONAL {?issueUri <" + MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLDataProperty_Description + "> ?issueDescription}}";
            ResultSet rsRefIssue = QueryExecutionFactory.create(sRefIssueQuery, oModel).execSelect();
                           
            while (rsRefIssue.hasNext())
            {
                MetadataGlobal.APIResponseData oRefIssue = new MetadataGlobal.APIResponseData();
                oRefIssue.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_issue + "/";

                QuerySolution qsRefIssue = rsRefIssue.nextSolution();

                String sRefIssueUri = qsRefIssue.get("?issueUri").toString();
                String sRefIssueDescription = "";
                if (qsRefIssue.get("?issueDescription") != null)
                    sRefIssueDescription = qsRefIssue.get("?issueDescription").toString();
                
                MetadataGlobal.APIResponseData oRefIssueUri = new MetadataGlobal.APIResponseData();
                oRefIssueUri.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_issue + MetadataConstants.c_XMLE_Uri;
                oRefIssueUri.sData = sRefIssueUri;
                oRefIssue.oData.add(oRefIssueUri);
                
                MetadataGlobal.APIResponseData oRefIssueDesc = new MetadataGlobal.APIResponseData();
                oRefIssueDesc.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_issueDescription;
                oRefIssueDesc.sData = sRefIssueDescription;
                oRefIssue.oData.add(oRefIssueDesc);
                
                oReferences.oData.add(oRefIssue);
            }
            
            if (oReferences.sReturnConfig != null)
                oData.oData.add(oReferences);
        }
        catch (Exception e)
        {
        }
        return oData;
    }
    
    /**
     * @summary mail_getAllForProduct
     * @startRealisation  Sasa Stojanovic 06.04.2012.
     * @finalModification Sasa Stojanovic 06.04.2012.
     * @param dtmFromDate - date from filter
     * @return - APIResponseData object with results
     */
    public static MetadataGlobal.APIResponseData ac_mail_getAllForProduct(Date dtmFromDate)
    {
        MetadataGlobal.APIResponseData oData = new MetadataGlobal.APIResponseData();
        try
        {
            OntModel oModel = MetadataConstants.omModel;

            String sQuery = "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> "
                    + "SELECT ?emailUri ?emailSubject ?emailFrom ?emailDate WHERE "
                    + "{?emailUri a <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_Email + "> ."
                    + " ?emailUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_Subject + "> ?emailSubject ."
                    + " ?emailUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLObjectProperty_From + "> ?emailFromPerson ."
                    + " ?emailFromPerson <" + MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLDataProperty_Email + "> ?emailFrom ."
                    + " ?emailUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_HasCreationDate + "> ?emailDate ."
                    + " FILTER ( ?emailDate > \"" + MetadataGlobal.FormatDateForSaving(dtmFromDate) + "\"^^xsd:dateTime )}";

            ResultSet rsMail = QueryExecutionFactory.create(sQuery, oModel).execSelect();
            
            while (rsMail.hasNext())
            {
                QuerySolution qsMail = rsMail.nextSolution();
                
                MetadataGlobal.APIResponseData oMail = new MetadataGlobal.APIResponseData();
                MetadataGlobal.APIResponseData oMailUri = new MetadataGlobal.APIResponseData();
                MetadataGlobal.APIResponseData oMailSubject = new MetadataGlobal.APIResponseData();
                MetadataGlobal.APIResponseData oMailFrom = new MetadataGlobal.APIResponseData();
                MetadataGlobal.APIResponseData oMailDate = new MetadataGlobal.APIResponseData();
                
                oMail.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_email + "/";
                oMailUri.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_email + MetadataConstants.c_XMLE_Uri;
                oMailSubject.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_emailSubject;
                oMailFrom.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_emailFrom;
                oMailDate.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_emailDate;
                
                oMailUri.sData = qsMail.get("?emailUri").toString();
                oMailSubject.sData = "<![CDATA[" + qsMail.get("?emailSubject").toString() + "]]>";
                oMailFrom.sData = qsMail.get("?emailFrom").toString();
                oMailDate.sData = MetadataGlobal.FormatDateFromSavingToPublish(qsMail.get("?emailDate").toString());
                
                oMail.oData.add(oMailUri);
                oMail.oData.add(oMailSubject);
                oMail.oData.add(oMailFrom);
                oMail.oData.add(oMailDate);
                oData.oData.add(oMail);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return oData;
    }
    
    /**
     * @summary forumPost_getAllForProduct
     * @startRealisation  Sasa Stojanovic 06.04.2012.
     * @finalModification Sasa Stojanovic 06.04.2012.
     * @param dtmFromDate - date from filter
     * @return - APIResponseData object with results
     */
    public static MetadataGlobal.APIResponseData ac_forumPost_getAllForProduct(Date dtmFromDate)
    {
        MetadataGlobal.APIResponseData oData = new MetadataGlobal.APIResponseData();
        try
        {
            OntModel oModel = MetadataConstants.omModel;

            String sQuery = "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> "
                    + "SELECT ?forumPostUri ?forumPostSubject ?forumPostAuthor ?forumPostTime ?forumPostCategory WHERE "
                    + "{?forumPostUri a <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_post + "> ."
                    + " ?forumPostUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_Subject + "> ?forumPostSubject ."
                    + " ?forumPostUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLObjectProperty_Author + "> ?forumPostAuthorPerson ."
                    + " ?forumPostAuthorPerson <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_Username + "> ?forumPostAuthor ."
                    + " ?forumPostUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_PostTime + "> ?forumPostTime ."
                    + " ?forumPostUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_Category + "> ?forumPostCategory ."
                    + " FILTER ( ?forumPostTime > \"" + MetadataGlobal.FormatDateForSaving(dtmFromDate) + "\"^^xsd:dateTime )}";

            ResultSet rsForumPost = QueryExecutionFactory.create(sQuery, oModel).execSelect();
            
            while (rsForumPost.hasNext())
            {
                QuerySolution qsForumPost = rsForumPost.nextSolution();
                
                MetadataGlobal.APIResponseData oForumPost = new MetadataGlobal.APIResponseData();
                MetadataGlobal.APIResponseData oForumPostUri = new MetadataGlobal.APIResponseData();
                MetadataGlobal.APIResponseData oForumPostSubject = new MetadataGlobal.APIResponseData();
                MetadataGlobal.APIResponseData oForumPostAuthor = new MetadataGlobal.APIResponseData();
                MetadataGlobal.APIResponseData oForumPostTime = new MetadataGlobal.APIResponseData();
                MetadataGlobal.APIResponseData oForumPostCategory = new MetadataGlobal.APIResponseData();
                
                oForumPost.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_forumPost + "/";
                oForumPostUri.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_forumPost + MetadataConstants.c_XMLE_Uri;
                oForumPostSubject.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_forumPostSubject;
                oForumPostAuthor.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_forumPostAuthor;
                oForumPostTime.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_forumPostTime;
                oForumPostCategory.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_forumPostCategory;
                
                oForumPostUri.sData = qsForumPost.get("?forumPostUri").toString();
                oForumPostSubject.sData = "<![CDATA[" + qsForumPost.get("?forumPostSubject").toString() + "]]>";
                oForumPostAuthor.sData = qsForumPost.get("?forumPostAuthor").toString();
                oForumPostTime.sData = MetadataGlobal.FormatDateFromSavingToPublish(qsForumPost.get("?forumPostTime").toString());
                oForumPostCategory.sData = qsForumPost.get("?forumPostCategory").toString();
                
                oForumPost.oData.add(oForumPostUri);
                oForumPost.oData.add(oForumPostSubject);
                oForumPost.oData.add(oForumPostAuthor);
                oForumPost.oData.add(oForumPostTime);
                oForumPost.oData.add(oForumPostCategory);
                oData.oData.add(oForumPost);
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
            OntModel oModel = MetadataConstants.omModel;
            
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
            OntModel oModel = MetadataConstants.omModel;
            
            //if keyword exists in subject/body annotation or in mail subject
            String sQuery = "SELECT ?emailUri WHERE {"
                    + "{?emailUri a <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_Email + "> . "
                    + "?emailUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLAnnotationProperty_apDescription + "> ?subject . FILTER regex(?subject, \"" + sKeyword + "\", \"i\")}"
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
            OntModel oModel = MetadataConstants.omModel;
            
            //if keyword exists in title/body annotation or in mail subject
            String sQuery = "SELECT ?postUri WHERE {"
                    + "{?postUri a <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_post + "> . "
                    + "?postUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLAnnotationProperty_apSubject + "> ?title . FILTER regex(?title, \"" + sKeyword + "\", \"i\")}"
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
            OntModel oModel = MetadataConstants.omModel;
            
            //if keyword exists in title/body annotation or in mail subject
            String sQuery = "SELECT ?wikiPageUri WHERE {"
                    + "{?wikiPageUri a <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_WikiPage + "> . "
                    + "?wikiPageUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLAnnotationProperty_apSubject + "> ?title . FILTER regex(?title, \"" + sKeyword + "\", \"i\")}"
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
                    + "?issueUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLAnnotationProperty_apDescription + "> ?subject . "
                    + "<" + sIssueUri + "> <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLAnnotationProperty_apDescription + "> ?subject . "
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
            OntModel oModel = MetadataConstants.omModel;
            
            //if issue has same description/subject annotation as commit commit annotation
            String sQuery = "SELECT ?commitUri WHERE {"
                    + "{?commitUri a <" + MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLClass_Commit + "> . "
                    + "?commitUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLAnnotationProperty_apCommit + "> ?description . "
                    + "<" + sIssueUri + "> <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLAnnotationProperty_apDescription + "> ?description}"
                    + " UNION "
                    + "{?commitUri a <" + MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLClass_Commit + "> . "
                    + "?commitUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLAnnotationProperty_apCommit + "> ?subject . "
                    + "<" + sIssueUri + "> <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLAnnotationProperty_apDescription + "> ?subject}"
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
            OntModel oModel = MetadataConstants.omModel;
            
            //if issue has same description/subject annotation as email body/subject annotation
            String sQuery = "SELECT ?emailUri WHERE {"
                    + "{?emailUri a <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_Email + "> . "
                    + "?emailUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLAnnotationProperty_apBody + "> ?description . "
                    + "<" + sIssueUri + "> <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLAnnotationProperty_apDescription + "> ?description}"
                    + " UNION "
                    + "{?emailUri a <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_Email + "> . "
                    + "?emailUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLAnnotationProperty_apDescription + "> ?subject . "
                    + "<" + sIssueUri + "> <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLAnnotationProperty_apDescription + "> ?subject}"
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
            OntModel oModel = MetadataConstants.omModel;
            
            //if issue has same description/subject annotation as post body/title annotation
            String sQuery = "SELECT ?postUri WHERE {"
                    + "{?postUri a <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_post + "> . "
                    + "?postUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLAnnotationProperty_apBody + "> ?description . "
                    + "<" + sIssueUri + "> <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLAnnotationProperty_apDescription + "> ?description}"
                    + " UNION "
                    + "{?postUri a <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_post + "> . "
                    + "?postUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLAnnotationProperty_apSubject + "> ?subject . "
                    + "<" + sIssueUri + "> <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLAnnotationProperty_apDescription + "> ?subject}"
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
            OntModel oModel = MetadataConstants.omModel;
            
            //if issue has same description/subject annotation as wiki page body/title annotation
            String sQuery = "SELECT ?wikiPageUri WHERE {"
                    + "{?wikiPageUri a <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_WikiPage + "> . "
                    + "?wikiPageUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLAnnotationProperty_apBody + "> ?description . "
                    + "<" + sIssueUri + "> <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLAnnotationProperty_apDescription + "> ?description}"
                    + " UNION "
                    + "{?wikiPageUri a <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_WikiPage + "> . "
                    + "?wikiPageUri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLAnnotationProperty_apSubject + "> ?subject . "
                    + "<" + sIssueUri + "> <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLAnnotationProperty_apDescription + "> ?subject}"
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
     * @finalModification Sasa Stojanovic 12.03.2012.
     * @param sConceptUri - concept URIs
     * @return - APIResponseData object with results
     */
    public static MetadataGlobal.APIResponseData ac_instance_getAllForConcept(ArrayList <String> sConceptUri)
    {
        MetadataGlobal.APIResponseData oData = new MetadataGlobal.APIResponseData();
        try
        {
            OntModel omModel = MetadataConstants.omModel;
                        
            for (int i = 0; i < sConceptUri.size(); i++)
            {
                //It can be anything (issue, person...)
                OntResource resConcept = omModel.getOntResource(sConceptUri.get(i));
                MetadataGlobal.APIResponseData oMembers = new MetadataGlobal.APIResponseData();
                OntClass ocMember = omModel.getOntClass(sConceptUri.get(i));
                
                //Get individuals
                ExtendedIterator iIterator = ocMember.listInstances();
                while(iIterator.hasNext())
                {               
                    MetadataGlobal.APIResponseData oSubData = new MetadataGlobal.APIResponseData();
                    oSubData.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_item;
                    oSubData.sData = iIterator.next().toString();
                    oMembers.oData.add(oSubData);
                }

                OntClass ocConcept = omModel.getOntClass(sConceptUri.get(i));
                OntClass ocSuperConcept = ocConcept.getSuperClass();

                MetadataGlobal.APIResponseData oConcept = new MetadataGlobal.APIResponseData();
                oConcept.sReturnConfig = "s3:concept/";
                MetadataGlobal.APIResponseData oCName = new MetadataGlobal.APIResponseData();
                oCName.sReturnConfig = "s3:name";
                oCName.sData = resConcept.getLocalName();
                MetadataGlobal.APIResponseData oCUri = new MetadataGlobal.APIResponseData();
                oCUri.sReturnConfig = "s3:uri";
                oCUri.sData = sConceptUri.get(i);
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
                        if (resInstance != null)
                        {
                            MetadataGlobal.APIResponseData oInstance = new MetadataGlobal.APIResponseData();
                            oInstance.sReturnConfig = "s3:instance/";
                            MetadataGlobal.APIResponseData oIName = new MetadataGlobal.APIResponseData();
                            oIName.sReturnConfig = "s3:name";
                            oIName.sData = resInstance.getLocalName();
                            MetadataGlobal.APIResponseData oIUri = new MetadataGlobal.APIResponseData();
                            oIUri.sReturnConfig = "s3:uri";
                            oIUri.sData = oMember.sData;

                            oInstance.oData.add(oIName);
                            oInstance.oData.add(oIUri);
                            //oInstance.oData.add(oProperties);
                            oInstances.oData.add(oInstance);
                        }
                    }
                }
                
                //Properties
                MetadataGlobal.APIResponseData oProperties = new MetadataGlobal.APIResponseData();
                oProperties.sReturnConfig = "s3:properties/";

                //we take only direct associations (true)
                ExtendedIterator iProperties = ocConcept.listDeclaredProperties(true);
                while(iProperties.hasNext())
                {
                    OntProperty pProperty = (OntProperty)iProperties.next();               
                    
                    MetadataGlobal.APIResponseData oProperty = new MetadataGlobal.APIResponseData();
                    oProperty.sReturnConfig = "s3:property/";

                    MetadataGlobal.APIResponseData oPName = new MetadataGlobal.APIResponseData();
                    oPName.sReturnConfig = "s3:name";
                    oPName.sData = pProperty.getLocalName();
                    MetadataGlobal.APIResponseData oPUri = new MetadataGlobal.APIResponseData();
                    oPUri.sReturnConfig = "s3:uri";
                    oPUri.sData = pProperty.getURI();
                    MetadataGlobal.APIResponseData oPRange = new MetadataGlobal.APIResponseData();
                    oPRange.sReturnConfig = "s3:range";
                    if (pProperty.getRange() != null)
                        oPRange.sData = pProperty.getRange().toString();
                    else
                        oPRange.sData = "";

                    oProperty.oData.add(oPName);
                    oProperty.oData.add(oPUri);
                    oProperty.oData.add(oPRange);
                    oProperties.oData.add(oProperty);
                }
                
                oConcept.oData.add(oCName);
                oConcept.oData.add(oCUri);
                oConcept.oData.add(oSuperConcept);
                oConcept.oData.add(oInstances);
                oConcept.oData.add(oProperties);
                oData.oData.add(oConcept);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
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
            OntModel oModel = MetadataConstants.omModel;
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
            
            OntModel omModel = MetadataConstants.omModel;

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
    
    /**
     * @summary get api response data for person
     * @startRealisation Sasa Stojanovic 13.03.2012.
     * @finalModification Sasa Stojanovic 13.03.2012.
     * @param sPersonURI - person URI
     * @return - person api response data
     */
    public static MetadataGlobal.APIResponseData GetPersonAPIResponse(String sPersonUri)
    {
        MetadataGlobal.APIResponseData oPerson = new MetadataGlobal.APIResponseData();
        try
        {
            OntModel oModel = MetadataConstants.omModel;
            OntResource resPerson = oModel.getOntResource(sPersonUri);
            StmtIterator siProperties = resPerson.listProperties();
            String sFirstName = "";
            String sLastName = "";
            
            MetadataGlobal.APIResponseData oPersonUri = new MetadataGlobal.APIResponseData();
            oPersonUri.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_uri;
            oPersonUri.sData = sPersonUri;
            oPerson.oData.add(oPersonUri);
            
            while (siProperties.hasNext())
            {
                Statement sStatement = siProperties.next();
                String sProperty = sStatement.getPredicate().getURI();
                
                MetadataGlobal.APIResponseData oPersonData = new MetadataGlobal.APIResponseData();
                
                if (sProperty.equals(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_ID))
                {
                    oPersonData.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_id;
                    oPersonData.sData = sStatement.getObject().toString();
                }
                if (sProperty.equals(MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLDataProperty_Email))
                {
                    oPersonData.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_email;
                    oPersonData.sData = sStatement.getObject().toString();
                }
                if (sProperty.equals(MetadataConstants.c_NS_foaf + MetadataConstants.c_OWLDataProperty_FirstName))
                {
                    sFirstName = sStatement.getObject().toString();
                }
                if (sProperty.equals(MetadataConstants.c_NS_foaf + MetadataConstants.c_OWLDataProperty_LastName))
                {
                    sLastName = sStatement.getObject().toString();
                }
                
                if (oPersonData.sReturnConfig != null)
                    oPerson.oData.add(oPersonData);
            }
            
            //spajanje imena i prezimena u jedan objekat
            String sName = sFirstName + " " + sLastName;

            if (!sName.equalsIgnoreCase(" "))
            {
                MetadataGlobal.APIResponseData oName = new MetadataGlobal.APIResponseData();
                oName.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_name;
                oName.sData = sName;
                oPerson.oData.add(oName);
            }
        }
        catch (Exception e)
        {
        }
        return oPerson;
        
    }
    
    /**
     * @summary get api response data for issue comment
     * @startRealisation Sasa Stojanovic 13.03.2012.
     * @finalModification Sasa Stojanovic 13.03.2012.
     * @param sCommentUri - comment URI
     * @return - comment api response data
     */
    public static MetadataGlobal.APIResponseData GetCommentAPIResponse(String sCommentUri)
    {
        MetadataGlobal.APIResponseData oComment = new MetadataGlobal.APIResponseData();
        try
        {
            OntModel oModel = MetadataConstants.omModel;
            OntResource resComment = oModel.getOntResource(sCommentUri);
            StmtIterator siProperties = resComment.listProperties();
            
            MetadataGlobal.APIResponseData oCommentUri = new MetadataGlobal.APIResponseData();
            oCommentUri.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_comment + MetadataConstants.c_XMLE_Uri;
            oCommentUri.sData = sCommentUri;
            oComment.oData.add(oCommentUri);
            
            while (siProperties.hasNext())
            {
                Statement sStatement = siProperties.next();
                String sProperty = sStatement.getPredicate().getURI();
                
                MetadataGlobal.APIResponseData oCommentData = new MetadataGlobal.APIResponseData();
                
                if (sProperty.equals(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_ID))
                {
                    oCommentData.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_comment + MetadataConstants.c_XMLE_Id;
                    oCommentData.sData = sStatement.getObject().toString();
                }
                if (sProperty.equals(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLDataProperty_Number))
                {
                    oCommentData.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_commentNumber;
                    oCommentData.sData = sStatement.getObject().toString();
                }
                if (sProperty.equals(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLDataProperty_Text))
                {
                    oCommentData.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_commentText;
                    oCommentData.sData = sStatement.getObject().toString();
                }
                if (sProperty.equals(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLObjectProperty_HasCommentor))
                {
                    oCommentData.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_commentPerson + "/";
                    oCommentData.oData.add(GetPersonAPIResponse(sStatement.getObject().toString()));
                }
                if (sProperty.equals(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLDataProperty_Date))
                {
                    oCommentData.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_commentDate;
                    oCommentData.sData = MetadataGlobal.FormatDateFromSavingToPublish(sStatement.getObject().toString());
                }
                
                if (oCommentData.sReturnConfig != null)
                    oComment.oData.add(oCommentData);
            }
        }
        catch (Exception e)
        {
        }
        return oComment;
        
    }
    
    /**
     * @summary get api response data for issue attachment
     * @startRealisation Sasa Stojanovic 13.03.2012.
     * @finalModification Sasa Stojanovic 13.03.2012.
     * @param sAttachmentUri - attachment URI
     * @return - attachment api response data
     */
    public static MetadataGlobal.APIResponseData GetAttachmentAPIResponse(String sAttachmentUri)
    {
        MetadataGlobal.APIResponseData oAttachment = new MetadataGlobal.APIResponseData();
        try
        {
            OntModel oModel = MetadataConstants.omModel;
            OntResource resAttachment = oModel.getOntResource(sAttachmentUri);
            StmtIterator siProperties = resAttachment.listProperties();
            
            MetadataGlobal.APIResponseData oAttachmentUri = new MetadataGlobal.APIResponseData();
            oAttachmentUri.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_attachment + MetadataConstants.c_XMLE_Uri;
            oAttachmentUri.sData = sAttachmentUri;
            oAttachment.oData.add(oAttachmentUri);
            
            while (siProperties.hasNext())
            {
                Statement sStatement = siProperties.next();
                String sProperty = sStatement.getPredicate().getURI();
                
                MetadataGlobal.APIResponseData oAttachmentData = new MetadataGlobal.APIResponseData();
                
                if (sProperty.equals(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_ID))
                {
                    oAttachmentData.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_attachment + MetadataConstants.c_XMLE_Id;
                    oAttachmentData.sData = sStatement.getObject().toString();
                }
                if (sProperty.equals(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLDataProperty_FileName))
                {
                    oAttachmentData.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_attachmentFilename;
                    oAttachmentData.sData = sStatement.getObject().toString();
                }
                if (sProperty.equals(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLDataProperty_Type))
                {
                    oAttachmentData.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_attachmentType;
                    oAttachmentData.sData = sStatement.getObject().toString();
                }
                if (sProperty.equals(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLObjectProperty_HasCreator))
                {
                    oAttachmentData.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_attachmentCreator + "/";
                    oAttachmentData.oData.add(GetPersonAPIResponse(sStatement.getObject().toString()));
                }
                
                if (oAttachmentData.sReturnConfig != null)
                    oAttachment.oData.add(oAttachmentData);
            }
        }
        catch (Exception e)
        {
        }
        return oAttachment;
        
    }
    
    /**
     * @summary get api response data for issue activity
     * @startRealisation Sasa Stojanovic 13.03.2012.
     * @finalModification Sasa Stojanovic 13.03.2012.
     * @param sActivityUri - activity URI
     * @return - activity api response data
     */
    public static MetadataGlobal.APIResponseData GetActivityAPIResponse(String sActivityUri)
    {
        MetadataGlobal.APIResponseData oActivity = new MetadataGlobal.APIResponseData();
        try
        {
            OntModel oModel = MetadataConstants.omModel;
            OntResource resActivity = oModel.getOntResource(sActivityUri);
            StmtIterator siProperties = resActivity.listProperties();
            
            MetadataGlobal.APIResponseData oActivityUri = new MetadataGlobal.APIResponseData();
            oActivityUri.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_activity + MetadataConstants.c_XMLE_Uri;
            oActivityUri.sData = sActivityUri;
            oActivity.oData.add(oActivityUri);
            
            //MetadataGlobal.APIResponseData oActivityWRA = new MetadataGlobal.APIResponseData();
            //oActivityWRA.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_activityWRA + "/";
            
            while (siProperties.hasNext())
            {
                Statement sStatement = siProperties.next();
                String sProperty = sStatement.getPredicate().getURI();
                
                MetadataGlobal.APIResponseData oActivityData = new MetadataGlobal.APIResponseData();
                
                if (sProperty.equals(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_ID))
                {
                    oActivityData.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_activity + MetadataConstants.c_XMLE_Id;
                    oActivityData.sData = sStatement.getObject().toString();
                }
                if (sProperty.equals(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLObjectProperty_HasInvolvedPerson))
                {
                    oActivityData.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_activityWho;
                    oActivityData.sData = sStatement.getObject().toString();
                }
                if (sProperty.equals(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLDataProperty_Performed))
                {
                    oActivityData.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_activityWhen;
                    oActivityData.sData = MetadataGlobal.FormatDateFromSavingToPublish(sStatement.getObject().toString());
                }
                if (sProperty.equals(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLDataProperty_What))
                {
                    oActivityData.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_activityWhat;
                    oActivityData.sData = sStatement.getObject().toString();
                }
                if (sProperty.equals(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLDataProperty_Removed))
                {
                    oActivityData.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_activityRemoved;
                    oActivityData.sData = sStatement.getObject().toString();
                }
                if (sProperty.equals(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLDataProperty_Added))
                {
                    oActivityData.sReturnConfig = "s3:" + MetadataConstants.c_XMLE_activityAdded;
                    oActivityData.sData = sStatement.getObject().toString();
                }
                
                if (oActivityData.sReturnConfig != null)
                    oActivity.oData.add(oActivityData);
            }
        }
        catch (Exception e)
        {
        }
        return oActivity;
    }
    
    /**
     * @summary get api response data for person
     * @startRealisation Sasa Stojanovic 13.03.2012.
     * @finalModification Sasa Stojanovic 13.03.2012.
     * @param sPersonURI - person URI
     * @return - person api response data
     */
    public static MetadataGlobal.APIResponseData GetIdAPIResponse(String sUri, String sName)
    {
        MetadataGlobal.APIResponseData oData = new MetadataGlobal.APIResponseData();
        try
        {
            OntModel oModel = MetadataConstants.omModel;
            OntResource resData = oModel.getOntResource(sUri);
            StmtIterator siProperties = resData.listProperties();
            
            while (siProperties.hasNext())
            {
                Statement sStatement = siProperties.next();
                String sProperty = sStatement.getPredicate().getURI();
                
                if (sProperty.equals(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_ID))
                {
                    MetadataGlobal.APIResponseData oId = new MetadataGlobal.APIResponseData();
                    oId.sReturnConfig = "s3:" + sName + MetadataConstants.c_XMLE_id;
                    oId.sData = sStatement.getObject().toString();
                    oData.oData.add(oId);
                }
            }
        }
        catch (Exception e)
        {
        }
        return oData;
        
    }
    
//    MetadataGlobal.APIResponseData oPerson = new MetadataGlobal.APIResponseData();
//        try
//        {
//            MetadataGlobal.APIResponseData oName = new MetadataGlobal.APIResponseData();
//            MetadataGlobal.APIResponseData oID = new MetadataGlobal.APIResponseData();
//            MetadataGlobal.APIResponseData oEmail = new MetadataGlobal.APIResponseData();
//            
//            OntModel omModel = MetadataGlobal.LoadOWL(MetadataConstants.sLocationLoadAlert);
//            OntResource resPerson = omModel.getOntResource(sPersonURI);
//            
//            oName.sData = "";
//            
//            DatatypeProperty dtpFirstName = omModel.getDatatypeProperty(MetadataConstants.c_NS_foaf + MetadataConstants.c_OWLDataProperty_FirstName);
//            Statement stFirstName = resPerson.getProperty(dtpFirstName);
//            if (stFirstName != null)
//                oName.sData = stFirstName.getObject().toString();
//                
//            DatatypeProperty dtpLastName = omModel.getDatatypeProperty(MetadataConstants.c_NS_foaf + MetadataConstants.c_OWLDataProperty_LastName);
//            Statement stLastName = resPerson.getProperty(dtpLastName);
//            if (stLastName != null)
//                oName.sData += " " + stLastName.getObject().toString();
//            
//            DatatypeProperty dtpEmail = omModel.getDatatypeProperty(MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLDataProperty_Email);
//            String sEmail = resPerson.getProperty(dtpEmail).asTriple().getObject().toString();
//            
//        }
//        catch (Exception e)
//        {
//        }
//        return oPerson;
    
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
