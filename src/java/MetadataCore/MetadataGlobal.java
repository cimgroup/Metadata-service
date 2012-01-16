/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MetadataCore;

import com.hp.hpl.jena.ontology.AnnotationProperty;
import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
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

/**
 *
 * @author ivano
 */
public class MetadataGlobal {

    
    
    // <editor-fold desc="Members">
    
    public static DateFormat m_dfFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

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
    
    /**
     * @summary loading OntModel depending on URI
     * @startRealisation Sasa Stojanovic 13.12.2011.
     * @finalModification Sasa Stojanovic 13.12.2011.
     * @param sProductUri - class/member URI
     * @return - OntModel
     */
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
                
                //delete after solving the ontology problem
                //temp code starts
                bNew = true;
                ArrayList <String> sUriId = new ArrayList();
                
                sQuery = "SELECT ?uri WHERE "
                        + "{?uri <" + MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_ID + "> \"" + sID + "\"}";
                rsURI = QueryExecutionFactory.create(sQuery, omModel).execSelect();
                while (rsURI.hasNext())
                {
                    sUriId.add(rsURI.nextSolution().getResource("?uri").getURI());
                }

                sQuery = "SELECT ?uri WHERE "
                        + "{?uri a <" + sClassURI + ">}";
                rsURI = QueryExecutionFactory.create(sQuery, omModel).execSelect();
                while (rsURI.hasNext())
                {
                    sURI = rsURI.nextSolution().getResource("?uri").getURI();
                    if (sUriId.contains(sURI))
                    {
                        bNew = false;
                        break;
                    }
                }
                //temp code ends
            }
            else
                bNew = true;
            
            if (bNew)
            {
                OntClass ocClass = omModel.getOntClass(sClassURI);
                sURI = sClassURI + MetadataGlobal.GetNextId(omModel, ocClass);
                //Resource resMember = omModel.createResource(sURI, ocClass);
                //DatatypeProperty dtpID = omID.getDatatypeProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_ID);
                //resMember.addProperty(dtpID, sID);
                
                Resource resMember = omModel.getResource(sURI);
                DatatypeProperty dtpId = omModel.getDatatypeProperty(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_ID);
                resMember.removeAll(dtpId);
                resMember.addProperty(dtpId, sID);
            }
            
            return sURI;      
        }
        catch (Exception e)
        {
            return "";
        }
    }
    

    
    public static Date GetDateTime(String sDateTime)
    {
        try
        {
            return m_dfFormat.parse(sDateTime);
        }
        catch (Exception e)
        {
            return null;
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
    // </editor-fold>
}
