/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MetadataCore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.StringWriter;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;


/**
 *
 * @author ivano
 */
@WebService(serviceName = "MetadataWebService")
public class MetadataWebService {

    /** Operation to request action from Metadata Service */
    @WebMethod(operationName = "XMLRequest")
    public String XMLRequest(@WebParam(name = "sDoc") String sDoc) throws FileNotFoundException
    {
        try
        {
            return MetadataCommunicator.ReceiveXML(sDoc);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return null;
        }
    }
    
    public static String LoadXML(String sLocation)
    {
        Document dDoc;
        try
        {
            DocumentBuilderFactory dbfFactory = DocumentBuilderFactory.newInstance();
            dbfFactory.setNamespaceAware(true);
            DocumentBuilder dbBuilder = dbfFactory.newDocumentBuilder();
            dDoc = dbBuilder.parse(new File(sLocation));
            dDoc.getDocumentElement().normalize();
            
            Source source = new DOMSource(dDoc);
            StringWriter stringWriter = new StringWriter();
            Result result = new StreamResult(stringWriter);
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            transformer.transform(source, result);
            return stringWriter.getBuffer().toString();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return null;
        }
    }
    
  
    public static void main(String[] args)
    {
        try
        {
            String sProjectPath = System.getProperty("user.dir");
            
            //implemented events
            
            //Data Storage events
            String sRequest = LoadXML(sProjectPath + "\\Events\\Data Storage Events\\Metadata.issue\\Metadata.issue.requestNew.xml");
            //String sRequest = LoadXML(sProjectPath + "\\Events\\Data Storage Events\\Metadata.issue\\Metadata.issue.requestUpdate.xml");
            //String sRequest = LoadXML(sProjectPath + "\\Events\\Data Storage Events\\Metadata.commit\\Metadata.commit.requestNew.xml");
            //String sRequest = LoadXML(sProjectPath + "\\Events\\Data Storage Events\\Metadata.forumPost\\Metadata.forumPost.requestNew.xml");
            
            //Annotation events
            //String sRequest = LoadXML(sProjectPath + "\\Events\\Annotation Events\\Metadata.issue.requestAnnotation.xml");
            //String sRequest = LoadXML(sProjectPath + "\\Events\\Annotation Events\\Metadata.commit.requestAnnotation.xml");
            //String sRequest = LoadXML(sProjectPath + "\\Events\\Annotation Events\\Metadata.comment.requestAnnotation.xml");
            //String sRequest = LoadXML(sProjectPath + "\\Events\\Annotation Events\\Metadata.forumPost.requestAnnotation.xml");
            //String sRequest = LoadXML(sProjectPath + "\\Events\\Annotation Events\\Metadata.mail.requestAnnotation.xml");
            //String sRequest = LoadXML(sProjectPath + "\\Events\\Annotation Events\\Metadata.wikiPost.requestAnnotation.xml");
        
            //API Call Events
            //String sRequest = LoadXML(sProjectPath + "\\Events\\API Call Events\\sparql\\APIRequest - sparql.xml");
            //String sRequest = LoadXML(sProjectPath + "\\Events\\API Call Events\\issue.getAllForProduct\\APIRequest - issue.getAllForProduct.xml");
            //String sRequest = LoadXML(sProjectPath + "\\Events\\API Call Events\\issue.getAllForMethod\\APIRequest - issue.getAllForMethod.xml");
            //String sRequest = LoadXML(sProjectPath + "\\Events\\API Call Events\\issue.getAnnotationStatus\\APIRequest - issue.getAnnotationStatus.xml");
            //String sRequest = LoadXML(sProjectPath + "\\Events\\API Call Events\\issue.getInfo\\APIRequest - issue.getInfo.xml");
            //String sRequest = LoadXML(sProjectPath + "\\Events\\API Call Events\\issue.getDuplicates\\APIRequest - issue.getDuplicates.xml");
            
            //String sRequest = LoadXML(sProjectPath + "\\Events\\API Call Events\\person.getInfo\\APIRequest - person.getInfo.xml");
            //String sRequest = LoadXML(sProjectPath + "\\Events\\API Call Events\\person.getAllForEmail\\APIRequest - person.getAllForEmail.xml");
            //String sRequest = LoadXML(sProjectPath + "\\Events\\API Call Events\\identity.getForPerson\\APIRequest - identity.getForPerson.xml");
            //String sRequest = LoadXML(sProjectPath + "\\Events\\API Call Events\\competency.getPersonForIssue\\APIRequest - competency.getPersonForIssue.xml");
            
            //not implemented yet
            //String sRequest = LoadXML(sProjectPath + "\\Events\\API Call Events\\method.getAllForPerson\\APIRequest - method.getAllForPerson.xml");
            //not implemented yet
            //String sRequest = LoadXML(sProjectPath + "\\Events\\API Call Events\\method.getRelatedCode\\APIRequest - method.getRelatedCode.xml");
            
            //String sRequest = LoadXML(sProjectPath + "\\Events\\API Call Events\\issue.getRelatedToKeyword\\APIRequest - issue.getRelatedToKeyword.xml");
            //String sRequest = LoadXML(sProjectPath + "\\Events\\API Call Events\\commit.getRelatedToKeyword\\APIRequest - commit.getRelatedToKeyword.xml");
            //String sRequest = LoadXML(sProjectPath + "\\Events\\API Call Events\\email.getRelatedToKeyword\\APIRequest - email.getRelatedToKeyword.xml");
            //String sRequest = LoadXML(sProjectPath + "\\Events\\API Call Events\\post.getRelatedToKeyword\\APIRequest - post.getRelatedToKeyword.xml");
            //String sRequest = LoadXML(sProjectPath + "\\Events\\API Call Events\\wiki.getRelatedToKeyword\\APIRequest - wiki.getRelatedToKeyword.xml");
            
            //String sRequest = LoadXML(sProjectPath + "\\Events\\API Call Events\\issue.getRelatedToIssue\\APIRequest - issue.getRelatedToIssue.xml");
            //String sRequest = LoadXML(sProjectPath + "\\Events\\API Call Events\\commit.getRelatedToIssue\\APIRequest - commit.getRelatedToIssue.xml");
            //String sRequest = LoadXML(sProjectPath + "\\Events\\API Call Events\\email.getRelatedToIssue\\APIRequest - email.getRelatedToIssue.xml");
            //String sRequest = LoadXML(sProjectPath + "\\Events\\API Call Events\\post.getRelatedToIssue\\APIRequest - post.getRelatedToIssue.xml");
            //String sRequest = LoadXML(sProjectPath + "\\Events\\API Call Events\\wiki.getRelatedToIssue\\APIRequest - wiki.getRelatedToIssue.xml");
                        
            MetadataWebService oServis = new MetadataWebService();
            String sTest = oServis.XMLRequest(sRequest);
            sTest = "a";
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
