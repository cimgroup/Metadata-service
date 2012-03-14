/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MetadataCore;

import com.hp.hpl.jena.ontology.*;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.vocabulary.XSD;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Element;

/**
 *
 * @author ivano
 */
public class MetadataGlobal {

    
    
    // <editor-fold desc="Members">
    
    public static DateFormat m_dfFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public static DateFormat m_dfFormatSS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static DateFormat m_dfFormatTZ = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.ENGLISH);

    // </editor-fold>
    
    // <editor-fold desc="Properties">
    
    // </editor-fold>
    
    // <editor-fold desc="Methods">
    
    /**
     * @summary Constructor
     * @startRealisation Ivan Obradovic 31.08.2011.
     * @finalModification Ivan Obradovic 31.08.2011.
     */
    public MetadataGlobal()
    {
        super();
    }
    
    /**
     * @summary Load ontology file
     * @startRealisation Ivan Obradovic 31.08.2011.
     * @finalModification Ivan Obradovic 31.08.2011.
     * @param sLocation - location of owl file
     * @return OntModel
     */
    public static OntModel LoadOWL(String sLocation) throws FileNotFoundException, IOException
    {
        OntModel omModel = null;
        try
        {
            omModel = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM ); //OWL_MEM_RULE_INF
            omModel.read(sLocation, "RDF/XML" );
            return omModel;
        }
        catch (Exception e)
        {
        }
        return omModel;
    }
    
    /**
     * @summary Load ontology file with given spec
     * @startRealisation Sasa Stojanovic 15.12.2011.
     * @finalModification Sasa Stojanovic 15.12.2011.
     * @param sLocation - location of owl file
     * @param oModelSpec - specification of model
     * @return OntModel
     */
    public static OntModel LoadOWLWithModelSpec(String sLocation, OntModelSpec oModelSpec) throws FileNotFoundException, IOException
    {
        OntModel omModel = null;
        try
        {
            omModel = ModelFactory.createOntologyModel(oModelSpec);
            omModel.read(sLocation, "RDF/XML" );
            return omModel;
        }
        catch (Exception e)
        {
        }
        return omModel;
    }
    
//    /**
//     * @summary loading OntModel depending on URI
//     * @startRealisation Sasa Stojanovic 13.12.2011.
//     * @finalModification Sasa Stojanovic 13.12.2011.
//     * @param sProductUri - class/member URI
//     * @return - OntModel
//     */
//    public static OntModel LoadModel(String sURI)
//    {
//        OntModel oModel = null;
//        try
//        {
//            if (sURI.contains(MetadataConstants.c_NS_Alert_Its) || sURI.contains(MetadataConstants.c_NS_Ifi))
//                oModel = LoadOWL(MetadataConstants.sLocationLoadAlertIts);
//            else
//                oModel = LoadOWL(MetadataConstants.sLocationLoadAlert);
//        }
//        catch (Exception e)
//        {
//        }
//        return oModel;
//    }
    
    /**
     * @summary Save data to owl file
     * @startRealisation Ivan Obradovic 31.08.2011.
     * @finalModification Ivan Obradovic 31.08.2011.
     * @param omModel 
     * @return bIsSaved
     */
    public static boolean SaveOWL(OntModel omModel, String sLocation)
    {
        boolean bIsSaved = false;
        try
        {
            File destinationFile = new File(sLocation);
            FileOutputStream fosRemove;

            fosRemove = new FileOutputStream(destinationFile, false);

            omModel.write(fosRemove, null);

            fosRemove.flush();
            return bIsSaved = true;
        }
        catch (Exception e)
        {
            return bIsSaved;
        }
    }
    
    /**
     * @summary Get next id for saving
     * @startRealisation Sasa Stojanovic 06.09.2011.
     * @finalModification Sasa Stojanovic 06.09.2011.
     * @param omModel - ontology model
     * @param ocClass - ontology class
     * @return id for save
     */
    public static int GetNextId(OntModel omModel, OntClass ocClass)
    {
        try
        {
            int iRet = 0;
            AnnotationProperty apCount = omModel.getAnnotationProperty(MetadataConstants.c_NS_Alert + "nextId");
            
            if (apCount == null)
            {
                apCount = omModel.createAnnotationProperty(MetadataConstants.c_NS_Alert + "nextId");
            }
            
            Integer iCount = 1;
            
            if (ocClass.hasProperty(apCount))
            {
                Statement stCount = ocClass.getRequiredProperty(apCount);
                iCount = stCount.getInt();   //load current id
            }
            
            iRet = iCount;
            iCount++;
            
            ocClass.removeAll(apCount);
            ocClass.addProperty(apCount, iCount.toString());    //saving next id
            return iRet;
        }
        catch (Exception e)
        {
            return 0;
        }
    }
    
    /**
     * @summary Get next id for saving
     * @startRealisation Sasa Stojanovic 31.10.2011.
     * @finalModification Sasa Stojanovic 31.10.2011.
     * @param omModel - ontology model
     * @param ocClass - ontology class
     * @param sID - id of member
     * @return object URI
     */
    public static String GetObjectURI(OntModel omModel, String sClassURI, String sID)
    {
        try
        {
            String sURI = "";
            boolean bNew = false;
            
            if (sID != null && !sID.isEmpty())
            {
                String sQuery = "SELECT ?uri WHERE "
                        + "{?uri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_ID + "> \"" + sID + "\" . "
                        + "?uri a <" + sClassURI + ">}";

                QueryExecution qeURI = QueryExecutionFactory.create(sQuery, omModel);
                ResultSet rsURI = qeURI.execSelect();
                              
                if (rsURI.hasNext())
                {
                    QuerySolution qlURI = rsURI.nextSolution();
                    Resource resMember = qlURI.getResource("?uri");
                    sURI = resMember.getURI();
                }
                else
                    bNew = true;
                
                qeURI.close();
            }
            else
                bNew = true;
            
            if (bNew)
            {
                OntClass ocClass = omModel.getOntClass(sClassURI);
                sURI = sClassURI + MetadataGlobal.GetNextId(omModel, ocClass);
                Resource resMember = omModel.createResource(sURI, ocClass);
                //DatatypeProperty dtpID = omID.getDatatypeProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_ID);
                //resMember.addProperty(dtpID, sID);
                
                if (sID != null && !sID.isEmpty())
                {
                    Resource resMemberR = omModel.getResource(sURI);
                    DatatypeProperty dtpId = omModel.getDatatypeProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_ID);
                    resMemberR.removeAll(dtpId);
                    resMemberR.addProperty(dtpId, sID);
                }
            }
            
            return sURI;      
        }
        catch (Exception e)
        {
            return "";
        }
    }
    
    /**
     * @summary Get next id for saving
     * @startRealisation  Dejan Milosavljevic 14.03.2012.
     * @finalModification Dejan Milosavljevic 14.03.2012.
     * @param omModel - ontology model
     * @param ocClass - ontology class
     * @param sID - id of member
     * @return object URI
     */
    public static String GetObjectURINoCreate(OntModel omModel, String sClassURI, String sID)
    {
        try
        {
            String sURI = "";
            
            if (sID != null && !sID.isEmpty())
            {
                String sQuery = "SELECT ?uri WHERE "
                        + "{?uri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_ID + "> \"" + sID + "\" . "
                        + "?uri a <" + sClassURI + ">}";

                QueryExecution qeURI = QueryExecutionFactory.create(sQuery, omModel);
                ResultSet rsURI = qeURI.execSelect();
                              
                if (rsURI.hasNext())
                {
                    QuerySolution qlURI = rsURI.nextSolution();
                    Resource resMember = qlURI.getResource("?uri");
                    sURI = resMember.getURI();
                }
                
                qeURI.close();
            }
            
            return sURI;      
        }
        catch (Exception e)
        {
            return "";
        }
    }
    
    /**
     * @summary Get next id for saving
     * @startRealisation Sasa Stojanovic 31.10.2011.
     * @finalModification Sasa Stojanovic 31.10.2011.
     * @param omModel - ontology model
     * @param ocClass - ontology class
     * @param sID - id of member
     * @return object URI
     */
    public static boolean IsItNew(OntModel omModel, String sClassURI, String sID)
    {
        try
        {
            boolean bNew = false;
            
            if (sID != null && !sID.isEmpty())
            {
                String sQuery = "SELECT ?uri WHERE "
                        + "{?uri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_ID + "> \"" + sID + "\" . "
                        + "?uri a <" + sClassURI + ">}";

                QueryExecution qeURI = QueryExecutionFactory.create(sQuery, omModel);
                ResultSet rsURI = qeURI.execSelect();
                              
                if (!rsURI.hasNext())
                {
                    bNew = true;
                }
                
                qeURI.close();
            }
            else
                bNew = true;
            
            return bNew;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    /**
     * @summary Get Date from String
     * @startRealisation  Sasa Stojanovic     31.10.2011.
     * @finalModification Dejan Milosavljevic 23.01.2012.
     * @return parsed Date
     */
    public static Date GetDateTime(String sDateTime)
    {
        Date oDate;
        try
        {
            oDate = m_dfFormatSS.parse(sDateTime);
        }
        catch (Exception e)
        {
            oDate = null;
        }
        if (oDate == null)
        {
            try
            {
                oDate = m_dfFormat.parse(sDateTime);
            }
            catch (Exception e)
            {
                oDate = null;
            }
        }
        if (oDate == null)
        {
            try
            {
                oDate = m_dfFormatTZ.parse(sDateTime);
            }
            catch (Exception e)
            {
                oDate = null;
            }
        }
        return oDate;
    }
    
    /**
     * @summary Class for storing annotation data.
     * @startRealisation  Dejan Milosavljevic 20.01.2012.
     * @finalModification Sasa Stojanovic 04.02.2012.
     */
    public static void ExpandOntology()
    {
        try
        {
            String sAnnotationClass = MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_Annotation;
            String sConceptClass = MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_AnnotationConcept;
            String sUriDataProperty = MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_Uri;
            String sCountDataProperty = MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_Count;
            String sTextDataProperty = MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_Text;
            String sForumItemIDDataProperty = MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_ForumItemID;
            String sPostTimeDataProperty = MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_PostTime;
            String sBodyDataProperty = MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_Body;
            String sCategoryDataProperty = MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_Category;
            String sHasConceptObjectProperty = MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLObjectProperty_HasConcepts;
            String sKeywordAnnotationProperty = MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLAnnotationProperty_apKeyword;
            String sHasObjectObjectProperty = MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLObjectProperty_HasObject;
            String sTrackerTypeDataProperty = MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_TrackerType;
            String sTrackerUrlDataProperty = MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_TrackerUrl;
            String sIsCommitOf = MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLObjectProperty_IsCommitOf;
            
            //Sasa Stojanovic
            String sAttachmentDataProperty = MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_Attachment;
            String sIsPerson = MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLObjectProperty_IsPerson;
            String sIsntPerson = MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLObjectProperty_IsntPerson;
            
            OntModel omModel = MetadataGlobal.LoadOWL(MetadataConstants.sLocationLoadAlert);
            
            //Creating Annotation class if not exist
            OntClass ocAnnotation = omModel.getOntClass(sAnnotationClass);
            if (ocAnnotation == null)
            {
                ocAnnotation = omModel.createClass(sAnnotationClass);
            }
            
            //Creating AnnotationConcept class if not exist
            OntClass ocConcept = omModel.getOntClass(sConceptClass);
            if (ocConcept == null)
            {
                ocConcept = omModel.createClass(sConceptClass);
            }
            
            //Creating uri DataProperty
            DatatypeProperty dtpUri = omModel.getDatatypeProperty(sUriDataProperty);
            if (dtpUri == null)
            {
                dtpUri = omModel.createDatatypeProperty(sUriDataProperty);
            }
            
            //Creating count DataProperty
            DatatypeProperty dtpCount = omModel.getDatatypeProperty(sCountDataProperty);
            if (dtpCount == null)
            {
                dtpCount = omModel.createDatatypeProperty(sCountDataProperty);
            }
            
            //Creating text DataProperty
            DatatypeProperty dtpText = omModel.getDatatypeProperty(sTextDataProperty);
            if (dtpText == null)
            {
                dtpText = omModel.createDatatypeProperty(sTextDataProperty);
            }

            //Creating forumItemId DataProperty
            DatatypeProperty dtpForumItemID = omModel.getDatatypeProperty(sForumItemIDDataProperty);
            if (dtpForumItemID == null)
            {
                dtpForumItemID = omModel.createDatatypeProperty(sForumItemIDDataProperty);
            }
            
            //Creating postTime DataProperty
            DatatypeProperty dtpPostTime = omModel.getDatatypeProperty(sPostTimeDataProperty);
            if (dtpPostTime == null)
            {
                dtpPostTime = omModel.createDatatypeProperty(sPostTimeDataProperty);
            }
            
            //Creating body DataProperty
            DatatypeProperty dtpBody = omModel.getDatatypeProperty(sBodyDataProperty);
            if (dtpBody == null)
            {
                dtpBody = omModel.createDatatypeProperty(sBodyDataProperty);
            }

            //Creating category DataProperty
            DatatypeProperty dtpCategory = omModel.getDatatypeProperty(sCategoryDataProperty);
            if (dtpCategory == null)
            {
                dtpCategory = omModel.createDatatypeProperty(sCategoryDataProperty);
            }

            //Creating hasConcepts ObjectProperty
            ObjectProperty opHasConcepts = omModel.getObjectProperty(sHasConceptObjectProperty);
            if (opHasConcepts == null)
            {
                opHasConcepts = omModel.createObjectProperty(sHasConceptObjectProperty);
            }
            
            //Creating hasObject ObjectProperty
            ObjectProperty opHasObject = omModel.getObjectProperty(sHasObjectObjectProperty);
            if (opHasObject == null)
            {
                opHasObject = omModel.createObjectProperty(sHasObjectObjectProperty);
            }
            
            //Creating apKeyword AnnotationProperty
            AnnotationProperty apKeyword = omModel.getAnnotationProperty(sKeywordAnnotationProperty);
            if (apKeyword == null)
            {
                apKeyword = omModel.createAnnotationProperty(sKeywordAnnotationProperty);
            }
            
            //Creating attachment DataProperty
            DatatypeProperty dtpAttachment = omModel.getDatatypeProperty(sAttachmentDataProperty);
            if (dtpAttachment == null)
            {
                dtpAttachment = omModel.createDatatypeProperty(sAttachmentDataProperty);
            }
            
            //Creating isPerson ObjectProperty
            ObjectProperty opIsPerson = omModel.getObjectProperty(sIsPerson);
            if (opIsPerson == null)
            {
                opIsPerson = omModel.createObjectProperty(sIsPerson);
                OntClass ocIdentity = omModel.getOntClass(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_Identity);
                opIsPerson.addDomain(ocIdentity);
                OntClass ocPerson = omModel.getOntClass(MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLClass_Person);
                opIsPerson.addRange(ocPerson);
            }
            
            //Creating isntPerson ObjectProperty
            ObjectProperty opIsntPerson = omModel.getObjectProperty(sIsntPerson);
            if (opIsntPerson == null)
            {
                opIsntPerson = omModel.createObjectProperty(sIsntPerson);
                OntClass ocIdentity = omModel.getOntClass(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_Identity);
                opIsntPerson.addDomain(ocIdentity);
                OntClass ocPerson = omModel.getOntClass(MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLClass_Person);
                opIsntPerson.addRange(ocPerson);
            }
            
            //Creating trackerType DataProperty
            DatatypeProperty dtpTrackerType = omModel.getDatatypeProperty(sTrackerTypeDataProperty);
            if (dtpTrackerType == null)
            {
                dtpTrackerType = omModel.createDatatypeProperty(sTrackerTypeDataProperty);
                //dtpTrackerType.addRange(dtpUri);
            }
            
            //Creating trackerUrl DataProperty
            DatatypeProperty dtpTrackerUrl = omModel.getDatatypeProperty(sTrackerUrlDataProperty);
            if (dtpTrackerUrl == null)
            {
                dtpTrackerUrl = omModel.createDatatypeProperty(sTrackerUrlDataProperty);
                //dtpTrackerUrl.addRange(dtpUri);
            }
            
            //Creating isCommitOf ObjectProperty
            ObjectProperty opIsCommitOf = omModel.getObjectProperty(sIsCommitOf);
            if (opIsCommitOf == null)
            {
                opIsCommitOf = omModel.createObjectProperty(sIsntPerson);
                OntClass ocCommit = omModel.getOntClass(MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLClass_Commit);
                opIsCommitOf.addDomain(ocCommit);
                OntClass ocComponent = omModel.getOntClass(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLClass_Component);
                opIsCommitOf.addRange(ocComponent);
            }
            
            MetadataGlobal.SaveOWL(omModel, MetadataConstants.sLocationSaveAlert);
        }
        catch (Exception e)
        {
        }
    }
    
     // </editor-fold>
    
    // <editor-fold desc="Classes">
    
    /**
     * @summary Class for storing information from <onto:property> element
     * @startRealisation Sasa Stojanovic 23.06.2011.
     * @finalModification Sasa Stojanovic 23.06.2011.
     */
    public static class OntoProperty
    {
        String sName;
        String sTypeOf;
        String sValue;
        ArrayList <OntoProperty> oProperties = new ArrayList<OntoProperty>();
    }
    
    /**
     * @summary Class for storing information about api response data
     * @startRealisation Sasa Stojanovic 02.11.2011.
     * @finalModification Sasa Stojanovic 02.11.2011.
     */
    public static class APIResponseData
    {
        String sReturnConfig; //path to store data
        String sData;
        ArrayList <APIResponseData> oData = new ArrayList<APIResponseData>();
    }
    
    /**
     * @summary Class for storing annotation data.
     * @startRealisation  Dejan Milosavljevic 16.01.2012.
     * @finalModification Dejan Milosavljevic 19.01.2012.
     */
    public static class AnnotationData extends MetadataObject
    {
        //m_sObjectURI - from MetadataObject (URI of object wich is being anotated)
        AnnotationProp[] oAnnotated;
        ConceptProp[] oConcepts;
        String[] oKeywords;
        String sHasObjectUri;
        
        /**
         * @summary Method for creating keywords from annotatons.
         * @startRealisation  Dejan Milosavljevic 19.01.2012.
         * @finalModification Dejan Milosavljevic 19.01.2012.
         */
        public void SetKeywords()
        {
            ArrayList oKw = new ArrayList();
            if (oAnnotated != null && oAnnotated.length > 0)
            {
                for (int i = 0; i < oAnnotated.length; i++)
                {
                    String sValue = oAnnotated[i].sValue;
                    if (!sValue.isEmpty())
                    {
                        int iIndexS = sValue.indexOf("<concept uri=");
                        while (iIndexS != -1)
                        {
                            if (iIndexS != -1)
                            {
                                sValue = sValue.substring(iIndexS + 14); //added 1 because of quoutation marks
                                int iIndexKwS = sValue.indexOf(">");
                                if (iIndexKwS != -1)
                                {
                                    sValue = sValue.substring(iIndexKwS + 1);
                                    
                                    int iIndexKwE = sValue.indexOf("</concept>");
                                    if (iIndexKwE != -1)
                                    {
                                        String sKeyword = sValue.substring(0, iIndexKwE);
                                        sValue = sValue.substring(iIndexKwE + 10);
                                        if (!sKeyword.isEmpty()) oKw.add(sKeyword);
                                    }
                                }
                            }
                            iIndexS = sValue.indexOf("<concept uri=");
                        }
                    }
                }
            }

            oKeywords = (String[])oKw.toArray(new String[0]);
        }
    }
    
    /**
     * @summary Class for storing annotation properties.
     * @startRealisation  Dejan Milosavljevic 16.01.2012.
     * @finalModification Dejan Milosavljevic 19.01.2012.
     */
    public static class AnnotationProp extends MetadataObject
    {
        String sName;
        //m_sObjectURI - from MetadataObject
        String sValue;
        
        /**
         * @summary Method for extracting text from xml element.
         * @startRealisation  Dejan Milosavljevic 19.01.2012.
         * @finalModification Dejan Milosavljevic 19.01.2012.
         * @param eAnnotated - xml element
         */
        public void SetAnnotationText(Element eAnnotated)
        {
            String sText = eAnnotated.getTextContent();
            if (sText.startsWith("<![CDATA["))
            {
                sText = sText.substring(9, sText.length() - 3);
            }
            sValue = sText;
        }
    }
    
    /**
     * @summary Class for storing concept properties.
     * @startRealisation  Dejan Milosavljevic 16.01.2012.
     * @finalModification Dejan Milosavljevic 20.01.2012.
     */
    public static class ConceptProp extends MetadataObject
    {
        String sName;
        //m_sObjectURI - from MetadataObject
        String sUri;
        String sCount;
    }
    
    /**
     * @summary Class for expanding MetadataObject class with list
     * @startRealisation Sasa Stojanovic 04.02.2012.
     * @finalModification Sasa Stojanovic 04.02.2012.
     */
    public static class MetadataObjectExt extends MetadataObject
    {
        public Object[] m_oObjects;
    }
    // </editor-fold>
}
