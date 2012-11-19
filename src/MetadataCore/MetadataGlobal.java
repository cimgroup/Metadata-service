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
import java.io.*;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Element;
import org.w3c.dom.*;
import org.xml.sax.InputSource;


/**
 *
 * @author ivano
 */
public class MetadataGlobal {

    
    
    // <editor-fold desc="Members">
    
    public static DateFormat m_dfFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public static DateFormat m_dfFormatSS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static DateFormat m_dfFormatTZ = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
    public static DateFormat m_dfFormatSave = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

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
     * @startRealisation Sasa Stojanovic 10.04.2012.
     * @finalModification Sasa Stojanovic 10.04.2012.
     */
    public static void LoadOntology() throws FileNotFoundException, IOException
    {
        try
        {
            MetadataConstants.omModel = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM );
            MetadataConstants.omModel.read(MetadataConstants.sLocationLoadAlert, "RDF/XML" );
            
            //delete this code when annotation ontology gets integrated
            MetadataConstants.omAnnotation = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM );
            MetadataConstants.omAnnotation.read(MetadataConstants.sLocationLoadAlert.replace("alert.owl", "AnnotationOntology.rdf"), "RDF/XML" );
        }
        catch (Exception e)
        {
        }
    }
    
    /**
     * @summary Save data to owl file
     * @startRealisation Sasa Stojanovic 10.04.2012.
     * @finalModification Sasa Stojanovic 10.04.2012.
     */
    public static void SaveOntology()
    {
        try
        {
            File destinationFile = new File(MetadataConstants.sLocationSaveAlert);
            FileOutputStream fosRemove;
            fosRemove = new FileOutputStream(destinationFile, false);
            MetadataConstants.omModel.write(fosRemove, null);
            fosRemove.flush();
            
            //delete this code when annotation ontology gets integrated
            File destinationAnnotationFile = new File(MetadataConstants.sLocationSaveAlert.replace("alert.owl", "AnnotationOntology.rdf"));
            FileOutputStream fosAnnotationRemove;
            fosAnnotationRemove = new FileOutputStream(destinationAnnotationFile, false);
            MetadataConstants.omAnnotation.write(fosAnnotationRemove, null);
            fosAnnotationRemove.flush();
        }
        catch (Exception e)
        {
        }
    }
    
    public static void SaveOntologyAlternative()
    {
        try
        {
            File destinationFile = new File(MetadataConstants.sLocationSaveAlert);
            FileWriter writer = new FileWriter(destinationFile);
            MetadataConstants.omModel.write(writer);
            writer.flush();
            
            //delete this code when annotation ontology gets integrated
            File destinationAnnotationFile = new File(MetadataConstants.sLocationSaveAlert.replace("alert.owl", "AnnotationOntology.rdf"));
            FileWriter writerAnnotation = new FileWriter(destinationAnnotationFile);
            MetadataConstants.omAnnotation.write(writerAnnotation);
            writerAnnotation.flush();
        }
        catch (Exception e)
        {
        }
    }
    
    /**
     * @summary Backup procedure
     * @startRealisation Sasa Stojanovic 10.04.2012.
     * @finalModification Sasa Stojanovic 10.04.2012.
     */
    public static void BackupProcedure(Document dDoc)
    {
        try
        {
            if (!MetadataConstants.bSilentMode)
            {
                    MetadataConstants.iBackupEventNumber++;
                    SaveBackupFile(dDoc);
            }
        }
        catch (Exception e)
        {
        }
    }
    
    public static void SaveBackupFile(Document dDoc)
    {
        try
        {
            TransformerFactory tFactory = TransformerFactory.newInstance();
            tFactory.setAttribute("indent-number", 2);
            Transformer tTransformer = tFactory.newTransformer();
            tTransformer.setOutputProperty(OutputKeys.METHOD, "xml");           
            tTransformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            tTransformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            tTransformer.setOutputProperty(OutputKeys.INDENT, "yes");
            tTransformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            Source srcDocument = new DOMSource(dDoc);
            java.util.Date dtmNow = new java.util.Date();
            String sFileName = MetadataConstants.sBackupFilesLocation + "E" + MetadataConstants.iBackupEventNumber.toString() + "_" + (new Timestamp(dtmNow.getTime())).toString().replace(" ","_").replace("-","_").replace(":","_").replace(".","_") + "_" + UUID.randomUUID().toString() + "_Request.xml";
            Result rsLocation = new StreamResult(new File(sFileName));
            tTransformer.transform(srcDocument, rsLocation);
        }
        catch (Exception e)
        {
        }
    }
    
    /**
     * @summary Backup procedure
     * @startRealisation Sasa Stojanovic 10.04.2012.
     * @finalModification Sasa Stojanovic 10.04.2012.
     */
    public static void StartSaveProcedure()
    {
        try
        {
            System.out.println("######################################################################");
            System.out.println("Backup procedure started, saving data into ontology files...");
            
            SaveOntologyAlternative();
            
            File fBackupFolder = new File(MetadataConstants.sBackupFilesLocation);
            File[] fBackupFiles = fBackupFolder.listFiles();
            for (int i = 0; i < fBackupFiles.length; i++)
            {
                if (fBackupFiles[i].isFile())
                {
                    fBackupFiles[i].delete();
                }
            }            
            MetadataConstants.iBackupEventNumber = 1;
            
            System.out.println("Backup procedure finished, data is stored into files.");
            System.out.println("######################################################################");
            
        }
        catch (Exception e)
        {
        }
    }
    
    /**
     * @summary Backup procedure
     * @startRealisation Sasa Stojanovic 10.04.2012.
     * @finalModification Sasa Stojanovic 10.04.2012.
     */
    public static void StartBackupProcedure()
    {
        MetadataConstants.bSilentMode = true;
        try
        {
            File fBackupFolder = new File(MetadataConstants.sBackupFilesLocation);
            File[] fBackupFiles = fBackupFolder.listFiles();
            Arrays.sort(fBackupFiles, new WindowsExplorerFileNameComparator());
            
            if (fBackupFiles.length > 0)
            {
                System.out.println("######################################################################");
                System.out.println("Backup procedure started, loading events:");
            }    

            for (int i = 0; i < fBackupFiles.length; i++)
            {
                if (fBackupFiles[i].isFile())
                {
                    Integer iEventNumber = i;
                    try
                    {
                        Document dDoc;
                        DocumentBuilderFactory dbfFactory = DocumentBuilderFactory.newInstance();
                        dbfFactory.setNamespaceAware(true);
                        DocumentBuilder dbBuilder = dbfFactory.newDocumentBuilder();
                        //dDoc = dbBuilder.parse(fBackupFiles[i]);
                        //new UTF-8 file reading code start
                        InputStream inputStream= new FileInputStream(fBackupFiles[i]);
                        Reader rReader = new InputStreamReader(inputStream,"UTF-8");
                        InputSource isSource = new InputSource(rReader);
                        dDoc = dbBuilder.parse(isSource);
                        //new UTF-8 file reading code end
                        dDoc.getDocumentElement().normalize();
                        java.util.Date dtmNow = new java.util.Date();
                        System.out.print("Event " + iEventNumber.toString() + ". start processing at: " + (new Timestamp(dtmNow.getTime())).toString() + " / ");
                        MetadataXMLReader.ReadXML(dDoc);
                        fBackupFiles[i].delete();
                    }
                    catch (Exception e)
                    {
                        System.out.print("Event " + iEventNumber.toString() + ". failed to process.");
                    }
                }
            }
            
            if (fBackupFiles.length > 0)
            {
                System.out.println("Saving into ontology files...");
                SaveOntologyAlternative();
                System.out.println("Backup procedure finished.");
            }

            MetadataConstants.iBackupEventNumber = 0;
            
            //Creating new thread for online backup procedure
            Runnable rListener = new Runnable() {
                @Override
                public void run() {
                    while (true)
                    {
                        try
                        {
                            Calendar cldBackup = Calendar.getInstance();
                            Date dtmNow = new Date();
                            cldBackup.setTime(dtmNow);
                            cldBackup.set(Calendar.HOUR_OF_DAY, 6);
                            cldBackup.set(Calendar.MINUTE, 0);
                            cldBackup.set(Calendar.SECOND, 0);
                            if (dtmNow.getHours() > 6)
                                cldBackup.add(Calendar.DATE,1);
                            Long iSleep = cldBackup.getTimeInMillis() - dtmNow.getTime();
                            Thread.sleep(iSleep);
                            
                            if (MetadataConstants.iBackupEventNumber >= MetadataConstants.iBackupEventNumberLimit)
                            {
                                MetadataConstants.bBackupProcedure = true;
                                StartSaveProcedure();
                                MetadataConstants.bBackupProcedure = false;
                            }
                        }
                        catch (Exception ex)
                        {
                        }
                    }
                }
            };
            Thread thrListener = new Thread(rListener);
            thrListener.start();
        }
        catch (Exception e)
        {
        }
        MetadataConstants.bSilentMode = false;
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
        if (oDate == null)
        {
            try
            {
                int iIndex = sDateTime.lastIndexOf(":");
                if (iIndex != -1)
                {
                    String sDateNew = String.format("%s%s", sDateTime.substring(0, iIndex),
                                                    sDateTime.substring(iIndex + 1, sDateTime.length()));
                    oDate = m_dfFormatSave.parse(sDateNew);
                }
            }
            catch (Exception e)
            {
                oDate = null;
            }
        }
        return oDate;
    }
    
    /**
     * @summary get string from date for saving
     * @startRealisation  Sasa Stojanovic 06.04.2012.
     * @finalModification Sasa Stojanovic 06.04.2012.
     * @param dtmDateTime - date
     * @return date in string format for saving
     */
    public static String FormatDateForSaving(Date dtmDate)
    {
        String sDateTime;
        try
        {
            sDateTime = MetadataGlobal.m_dfFormatSave.format(dtmDate);
            sDateTime = sDateTime.substring(0, sDateTime.length() - 2).concat(":").concat(sDateTime.substring(sDateTime.length() - 2));
        }
        catch (Exception e)
        {
            sDateTime = "";
        }
        return sDateTime;
    }
    
    /**
     * @summary get string from date for saving
     * @startRealisation  Sasa Stojanovic 06.04.2012.
     * @finalModification Sasa Stojanovic 06.04.2012.
     * @param sInputDate - date in string format for saving
     * @return date in string format for publish
     */
    public static String FormatDateFromSavingToPublish(String sDateTime)
    {
        try
        {
            sDateTime = sDateTime.substring(0, sDateTime.indexOf("^^"));
            sDateTime = sDateTime.substring(0, sDateTime.length() - 3).concat(sDateTime.substring(sDateTime.length() - 2));
            Date dtmDate = m_dfFormatSave.parse(sDateTime);
            sDateTime = m_dfFormatTZ.format(dtmDate);
        }
        catch (Exception e)
        {
            sDateTime = "";
        }
        return sDateTime;
    }
    
    /**
     * @summary Method for reading Metadata config
     * @startRealisation  Sasa Stojanovic 23.03.2012.
     * @finalModification Sasa Stojanovic 23.03.2012.
     */
    public static void ReadConfiguration()
    {
        String sLocation = System.getProperty("user.dir") + "/MetadataConfig.xml";
        Document dDoc = MetadataCommunicator.LoadXML(sLocation);
        NodeList nlMetadataConfig = dDoc.getElementsByTagName("MetadataConfig");

        if (nlMetadataConfig != null && nlMetadataConfig.getLength() > 0)
        {
            Element eMetadataConfig = (Element) nlMetadataConfig.item(0);
            
            String sActiveMQAddress = MetadataXMLReader.GetValue(eMetadataConfig, "ActiveMQAddress");
            if (!sActiveMQAddress.isEmpty())
                MetadataConstants.sActiveMQAddress = sActiveMQAddress;
            
            String sOntologyLocation = MetadataXMLReader.GetValue(eMetadataConfig, "OntologyLocation");
            if (!sOntologyLocation.isEmpty())
            {
                //MetadataConstants.sLocationSaveAlert = sOntologyLocation.replace("/", "\\");
                //MetadataConstants.sLocationLoadAlert = "file:" + sOntologyLocation;
                //next two lines works on FZI server
                MetadataConstants.sLocationLoadAlert = "file:" + sOntologyLocation;
                MetadataConstants.sLocationSaveAlert = sOntologyLocation;
            }
            MetadataConstants.sBackupFilesLocation = MetadataXMLReader.GetValue(eMetadataConfig, "BackupFilesLocation");
            MetadataConstants.sLogFilesLocation = MetadataXMLReader.GetValue(eMetadataConfig, "LogFilesLocation");
            MetadataConstants.iBackupEventNumberLimit = Integer.parseInt(MetadataXMLReader.GetValue(eMetadataConfig, "BackupAfertEvents"));
            if (MetadataXMLReader.GetValue(eMetadataConfig, "OnlyOutputEventLog").equalsIgnoreCase("true"))
            {
                MetadataConstants.bOnlyOutputEventLog = true;
            }
            else
            {
                MetadataConstants.bOnlyOutputEventLog = false;
            }
            if (MetadataXMLReader.GetValue(eMetadataConfig, "StoreOutputEventsInSeparateFolders").equalsIgnoreCase("true"))
            {
                MetadataConstants.bStoreOutputEventsInSeparateFolders = true;
            }
            else
            {
                MetadataConstants.bStoreOutputEventsInSeparateFolders = false;
            }
            System.out.println("########################## METADATA SERVICE ##########################");
            System.out.println("Config file loaded from location: " + sLocation);
            System.out.println("######################################################################");
            System.out.println("ActiveMQ address: " + MetadataConstants.sActiveMQAddress);
            System.out.print("Ontology location: " + MetadataConstants.sLocationSaveAlert.replace("\\", "/"));
            
            if (new File(MetadataConstants.sLocationSaveAlert).exists())
                System.out.println(". Ontology file was found");
            else
                System.out.println(". WARNING: Ontology file was not found!");
            
            System.out.print("Backup files location: " + MetadataConstants.sBackupFilesLocation);
                if (new File(MetadataConstants.sBackupFilesLocation).exists())
                    System.out.println(". Directory was found");
                else
                    System.out.println(". WARNING: Directory was not found!");
            
            if (MetadataConstants.sLogFilesLocation.isEmpty())
                System.out.println("Log files won't be created");
            else
            {
                System.out.print("Log files location: " + MetadataConstants.sLogFilesLocation);
                if (new File(MetadataConstants.sLogFilesLocation).exists())
                    System.out.println(". Directory was found");
                else
                    System.out.println(". WARNING: Directory was not found!");
            }
            
            System.out.println("Backup event limit: " + MetadataConstants.iBackupEventNumberLimit.toString());
            System.out.println("######################################################################");
        }
    }
    
    /**
     * @summary Method for expanding ontology
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
            String sWeightDataProperty = MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_Weight;
            String sTextDataProperty = MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_Text;
            String sForumItemIDDataProperty = MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_ForumItemID;
            String sPostTimeDataProperty = MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_PostTime;
            String sBodyDataProperty = MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_Body;
            String sCategoryDataProperty = MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_Category;
            String sHasConceptObjectProperty = MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLObjectProperty_HasConcepts;
            String sKeywordAnnotationProperty = MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLAnnotationProperty_apKeyword;
            String sHasObjectObjectProperty = MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLObjectProperty_HasObject;
            String sIsCommitOf = MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLObjectProperty_IsCommitOf;
            String sIsIssueOfTracker = MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLObjectProperty_IsIssueOfTracker;
            String sHasReferenceTo = MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLObjectProperty_HasReferenceTo;

            //Sasa Stojanovic
            String sAttachmentDataProperty = MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_Attachment;
            String sIsPerson = MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLObjectProperty_IsPerson;
            String sIsntPerson = MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLObjectProperty_IsntPerson;
            String sUsername = MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_Username;
            String sSource = MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_Source;
            String sRawText = MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_RawText;
            String sWikiEditCount = MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_WikiEditCount;
            String sComment = MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_Comment;
            String sIsMinor = MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLDataProperty_IsMinor;
                       
            //Creating Annotation class if not exist
            OntClass ocAnnotation = MetadataConstants.omModel.getOntClass(sAnnotationClass);
            if (ocAnnotation == null)
            {
                ocAnnotation = MetadataConstants.omModel.createClass(sAnnotationClass);
            }
            
            //Creating AnnotationConcept class if not exist
            OntClass ocConcept = MetadataConstants.omModel.getOntClass(sConceptClass);
            if (ocConcept == null)
            {
                ocConcept = MetadataConstants.omModel.createClass(sConceptClass);
            }
            
            //Creating uri DataProperty
            DatatypeProperty dtpUri = MetadataConstants.omModel.getDatatypeProperty(sUriDataProperty);
            if (dtpUri == null)
            {
                dtpUri = MetadataConstants.omModel.createDatatypeProperty(sUriDataProperty);
            }
            
            //Creating count DataProperty
            DatatypeProperty dtpWeight = MetadataConstants.omModel.getDatatypeProperty(sWeightDataProperty);
            if (dtpWeight == null)
            {
                dtpWeight = MetadataConstants.omModel.createDatatypeProperty(sWeightDataProperty);
            }
            
            //Creating text DataProperty
            DatatypeProperty dtpText = MetadataConstants.omModel.getDatatypeProperty(sTextDataProperty);
            if (dtpText == null)
            {
                dtpText = MetadataConstants.omModel.createDatatypeProperty(sTextDataProperty);
            }

            //Creating forumItemId DataProperty
            DatatypeProperty dtpForumItemID = MetadataConstants.omModel.getDatatypeProperty(sForumItemIDDataProperty);
            if (dtpForumItemID == null)
            {
                dtpForumItemID = MetadataConstants.omModel.createDatatypeProperty(sForumItemIDDataProperty);
            }
            
            //Creating postTime DataProperty
            DatatypeProperty dtpPostTime = MetadataConstants.omModel.getDatatypeProperty(sPostTimeDataProperty);
            if (dtpPostTime == null)
            {
                dtpPostTime = MetadataConstants.omModel.createDatatypeProperty(sPostTimeDataProperty);
            }
            
            //Creating body DataProperty
            DatatypeProperty dtpBody = MetadataConstants.omModel.getDatatypeProperty(sBodyDataProperty);
            if (dtpBody == null)
            {
                dtpBody = MetadataConstants.omModel.createDatatypeProperty(sBodyDataProperty);
            }

            //Creating category DataProperty
            DatatypeProperty dtpCategory = MetadataConstants.omModel.getDatatypeProperty(sCategoryDataProperty);
            if (dtpCategory == null)
            {
                dtpCategory = MetadataConstants.omModel.createDatatypeProperty(sCategoryDataProperty);
            }
            
            //Creating source DataProperty
            DatatypeProperty dtpSource = MetadataConstants.omModel.getDatatypeProperty(sSource);
            if (dtpSource == null)
            {
                dtpSource = MetadataConstants.omModel.createDatatypeProperty(sSource);
            }
            
            //Creating raw text DataProperty
            DatatypeProperty dtpRawText = MetadataConstants.omModel.getDatatypeProperty(sRawText);
            if (dtpRawText == null)
            {
                dtpRawText = MetadataConstants.omModel.createDatatypeProperty(sRawText);
            }
            
            //Creating wiki edit count DataProperty
            DatatypeProperty dtpWikiEditCount = MetadataConstants.omModel.getDatatypeProperty(sWikiEditCount);
            if (dtpWikiEditCount == null)
            {
                dtpWikiEditCount = MetadataConstants.omModel.createDatatypeProperty(sWikiEditCount);
            }
            
            //Creating comment DataProperty
            DatatypeProperty dtpComment = MetadataConstants.omModel.getDatatypeProperty(sComment);
            if (dtpComment == null)
            {
                dtpComment = MetadataConstants.omModel.createDatatypeProperty(sComment);
            }
            
            //Creating is minor DataProperty
            DatatypeProperty dtpIsMinor = MetadataConstants.omModel.getDatatypeProperty(sIsMinor);
            if (dtpIsMinor == null)
            {
                dtpIsMinor = MetadataConstants.omModel.createDatatypeProperty(sIsMinor);
            }

            //Creating hasConcepts ObjectProperty
            ObjectProperty opHasConcepts = MetadataConstants.omModel.getObjectProperty(sHasConceptObjectProperty);
            if (opHasConcepts == null)
            {
                opHasConcepts = MetadataConstants.omModel.createObjectProperty(sHasConceptObjectProperty);
            }
            
            //Creating hasObject ObjectProperty
            ObjectProperty opHasObject = MetadataConstants.omModel.getObjectProperty(sHasObjectObjectProperty);
            if (opHasObject == null)
            {
                opHasObject = MetadataConstants.omModel.createObjectProperty(sHasObjectObjectProperty);
            }
            
            //Creating apKeyword AnnotationProperty
            AnnotationProperty apKeyword = MetadataConstants.omModel.getAnnotationProperty(sKeywordAnnotationProperty);
            if (apKeyword == null)
            {
                apKeyword = MetadataConstants.omModel.createAnnotationProperty(sKeywordAnnotationProperty);
            }
            
            //Creating attachment DataProperty
            DatatypeProperty dtpAttachment = MetadataConstants.omModel.getDatatypeProperty(sAttachmentDataProperty);
            if (dtpAttachment == null)
            {
                dtpAttachment = MetadataConstants.omModel.createDatatypeProperty(sAttachmentDataProperty);
            }
            
            //Creating isPerson ObjectProperty
            ObjectProperty opIsPerson = MetadataConstants.omModel.getObjectProperty(sIsPerson);
            if (opIsPerson == null)
            {
                opIsPerson = MetadataConstants.omModel.createObjectProperty(sIsPerson);
                OntClass ocIdentity = MetadataConstants.omModel.getOntClass(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_Identity);
                opIsPerson.addDomain(ocIdentity);
                OntClass ocPerson = MetadataConstants.omModel.getOntClass(MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLClass_Person);
                opIsPerson.addRange(ocPerson);
            }
            
            //Creating username DataProperty
            DatatypeProperty dtpUsername = MetadataConstants.omModel.getDatatypeProperty(sUsername);
            if (dtpUsername == null)
            {
                dtpUsername = MetadataConstants.omModel.createDatatypeProperty(sUsername);
            }
            
            //Creating isntPerson ObjectProperty
            ObjectProperty opIsntPerson = MetadataConstants.omModel.getObjectProperty(sIsntPerson);
            if (opIsntPerson == null)
            {
                opIsntPerson = MetadataConstants.omModel.createObjectProperty(sIsntPerson);
                OntClass ocIdentity = MetadataConstants.omModel.getOntClass(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_Identity);
                opIsntPerson.addDomain(ocIdentity);
                OntClass ocPerson = MetadataConstants.omModel.getOntClass(MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLClass_Person);
                opIsntPerson.addRange(ocPerson);
            }
            
            //Creating isIssueOfTracker ObjectProperty
            ObjectProperty opIsIssueOfTracker = MetadataConstants.omModel.getObjectProperty(sIsIssueOfTracker);
            if (opIsIssueOfTracker == null)
            {
                opIsIssueOfTracker = MetadataConstants.omModel.createObjectProperty(sIsIssueOfTracker);
                OntClass ocIssue = MetadataConstants.omModel.getOntClass(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLClass_Issue);
                opIsIssueOfTracker.addDomain(ocIssue);
                OntClass ocIssueTracker = MetadataConstants.omModel.getOntClass(MetadataConstants.c_NS_Alert_Its + MetadataConstants.c_OWLClass_IssueTracker);
                opIsIssueOfTracker.addRange(ocIssueTracker);
            }
            
            //Creating isCommitOf ObjectProperty
            ObjectProperty opIsCommitOf = MetadataConstants.omModel.getObjectProperty(sIsCommitOf);
            if (opIsCommitOf == null)
            {
                opIsCommitOf = MetadataConstants.omModel.createObjectProperty(sIsCommitOf);
                OntClass ocCommit = MetadataConstants.omModel.getOntClass(MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLClass_Commit);
                opIsCommitOf.addDomain(ocCommit);
                OntClass ocComponent = MetadataConstants.omModel.getOntClass(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLClass_Component);
                opIsCommitOf.addRange(ocComponent);
            }
            
            //Creating hasReferenceTo ObjectProperty
            ObjectProperty opHasReferenceTo = MetadataConstants.omModel.getObjectProperty(sHasReferenceTo);
            if (opHasReferenceTo == null)
            {
                opHasReferenceTo = MetadataConstants.omModel.createObjectProperty(sHasReferenceTo);
                OntClass ocIssue = MetadataConstants.omModel.getOntClass(MetadataConstants.c_NS_Ifi + MetadataConstants.c_OWLClass_Issue);
                opHasReferenceTo.addRange(ocIssue);
            }
            
            //Creating annotation properties for new concepts in annotation ontology
            AnnotationProperty apLabel = MetadataConstants.omAnnotation.getAnnotationProperty(MetadataConstants.c_NS_w3_rdf_schema + MetadataConstants.c_OWLAnnotationProperty_Label);
            if (apLabel == null)
            {
                apLabel = MetadataConstants.omAnnotation.createAnnotationProperty(MetadataConstants.c_NS_w3_rdf_schema + MetadataConstants.c_OWLAnnotationProperty_Label);
            }  
            AnnotationProperty apComment = MetadataConstants.omAnnotation.getAnnotationProperty(MetadataConstants.c_NS_w3_rdf_schema + MetadataConstants.c_OWLAnnotationProperty_Comment);
            if (apComment == null)
            {
                apComment = MetadataConstants.omAnnotation.createAnnotationProperty(MetadataConstants.c_NS_w3_rdf_schema + MetadataConstants.c_OWLAnnotationProperty_Comment);
            }
            AnnotationProperty apSameAs = MetadataConstants.omAnnotation.getAnnotationProperty(MetadataConstants.c_NS_w3_owl + MetadataConstants.c_OWLAnnotationProperty_SameAs);
            if (apSameAs == null)
            {
                apSameAs = MetadataConstants.omAnnotation.createAnnotationProperty(MetadataConstants.c_NS_w3_owl + MetadataConstants.c_OWLAnnotationProperty_SameAs);
            }
            AnnotationProperty apLinksTo = MetadataConstants.omAnnotation.getAnnotationProperty(MetadataConstants.c_NS_ijs_predicate + MetadataConstants.c_OWLAnnotationProperty_LinksTo);
            if (apLinksTo == null)
            {
                apLinksTo = MetadataConstants.omAnnotation.createAnnotationProperty(MetadataConstants.c_NS_ijs_predicate + MetadataConstants.c_OWLAnnotationProperty_LinksTo);
            }
            
            
            MetadataGlobal.SaveOWL(MetadataConstants.omModel, MetadataConstants.sLocationSaveAlert);
            
            //delete this code when annotation ontology gets integrated
            MetadataGlobal.SaveOWL(MetadataConstants.omAnnotation, MetadataConstants.sLocationSaveAlert.replace("alert.owl", "AnnotationOntology.rdf"));
        }
        catch (Exception e)
        {
        }
    }
    
    /**
     * @summary check if concept is related with code
     * @startRealisation  Sasa Stojanovic 22.10.2012.
     * @finalModification Sasa Stojanovic 22.10.2012.
     * @param sConceptUri - URI of concept
     * @return true if concept is related with code
     */
    public static boolean CheckConcept(String sConceptUri)
    {
        boolean bCodeRelated = false;
        try
        {
            if (sConceptUri.startsWith(MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLClass_Commit) ||
                sConceptUri.startsWith(MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLClass_File) ||
                sConceptUri.startsWith(MetadataConstants.c_NS_Alert_Scm + MetadataConstants.c_OWLClass_Module) ||
                sConceptUri.startsWith(MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_Method))
                bCodeRelated = true;
        }
        catch (Exception e)
        {
        }
        return bCodeRelated;
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
     * @finalModification Dejan Milosavljevic 19.04.2012.
     */
    public static class AnnotationData extends MetadataObject
    {
        //m_sObjectURI - from MetadataObject (Ne koristiti) Dejan Milosavljevic 17.04.2012.
        AnnotationProp[] oAnnotated;
        //ConceptProp[] oConcepts; //Dejan Milosavljevic 17.04.2012.
        String[] oKeywords;
        //String sHasObjectUri;  //Dejan Milosavljevic 17.04.2012.
        ArrayList <String> oReferences = new ArrayList();    //Dejan Milosavljevic 18.04.2012.
        Integer iItemId;
        Integer iThreadId;
        
        /**
         * @summary Method for creating keywords from annotatons.
         * @startRealisation  Dejan Milosavljevic 19.01.2012.
         * @finalModification Dejan Milosavljevic 19.04.2012.
         */
        public void SetKeywords()
        {
            ArrayList oKw = new ArrayList();
            if (oAnnotated != null && oAnnotated.length > 0)
            {
                for (int i = 0; i < oAnnotated.length; i++)
                {
                    //String sValue = oAnnotated[i].sValue;
                    //if (sValue != null && !sValue.isEmpty())
                    if (oAnnotated[i].sValue != null && !oAnnotated[i].sValue.isEmpty())
                    {
                        String sValue = oAnnotated[i].sValue;
                        int iIndexS = sValue.indexOf("<concept id=");
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
                            iIndexS = sValue.indexOf("<concept id=");
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
     * @finalModification Dejan Milosavljevic 17.04.2012.
     */
    public static class AnnotationProp extends MetadataObject
    {
        String sName;
        //m_sObjectURI - from MetadataObject
        String sValue;
        Integer iItemId; //Dejan Milosavljevic 17.04.2012.
        String sHasObject; //(URI of object wich is being anotated) Dejan Milosavljevic 17.04.2012.
        ConceptProp[] oConcepts; //Dejan Milosavljevic 17.04.2012.
        
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
        String sWeight;
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
