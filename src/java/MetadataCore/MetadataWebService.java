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
            //implemented events
            //String sRequest = LoadXML("D:\\Sasa.Stojanovic\\Alert\\XML\\2011_12 Events\\Metadata.issue.requestNew.xml");
            //String sRequest = LoadXML("D:\\Sasa.Stojanovic\\Alert\\XML\\2011_12 Events\\Metadata.issue.requestUpdate.xml");
            //String sRequest = LoadXML("D:\\Sasa.Stojanovic\\Alert\\XML\\2011_12 Events\\Metadata.commit.requestNew.xml");
            //API Calls
            //String sRequest = LoadXML("D:\\Sasa.Stojanovic\\Alert\\XML\\2011_12 Events\\Defined API Calls\\sparql\\APIRequest - sparql.xml");
            //String sRequest = LoadXML("D:\\Sasa.Stojanovic\\Alert\\XML\\2011_12 Events\\Defined API Calls\\issue.getAllForProduct\\APIRequest - issue.getAllForProduct.xml");
            //String sRequest = LoadXML("D:\\Sasa.Stojanovic\\Alert\\XML\\2011_12 Events\\Defined API Calls\\issue.getAllForMethod\\APIRequest - issue.getAllForMethod.xml");
            //String sRequest = LoadXML("D:\\Sasa.Stojanovic\\Alert\\XML\\2011_12 Events\\Defined API Calls\\issue.getAnnotationStatus\\APIRequest - issue.getAnnotationStatus.xml");
            //String sRequest = LoadXML("D:\\Sasa.Stojanovic\\Alert\\XML\\2011_12 Events\\Defined API Calls\\issue.getInfo\\APIRequest - issue.getInfo.xml");
            //String sRequest = LoadXML("D:\\Sasa.Stojanovic\\Alert\\XML\\2011_12 Events\\Defined API Calls\\issue.getDuplicates\\APIRequest - issue.getDuplicates.xml");
            //String sRequest = LoadXML("D:\\Sasa.Stojanovic\\Alert\\XML\\2011_12 Events\\Defined API Calls\\issue.getRelatedToKeyword\\APIRequest - issue.getRelatedToKeyword.xml");
            //String sRequest = LoadXML("D:\\Sasa.Stojanovic\\Alert\\XML\\2011_12 Events\\Defined API Calls\\commit.getRelatedToKeyword\\APIRequest - commit.getRelatedToKeyword.xml");
            //String sRequest = LoadXML("D:\\Sasa.Stojanovic\\Alert\\XML\\2011_12 Events\\Defined API Calls\\email.getRelatedToKeyword\\APIRequest - email.getRelatedToKeyword.xml");
            //String sRequest = LoadXML("D:\\Sasa.Stojanovic\\Alert\\XML\\2011_12 Events\\Defined API Calls\\post.getRelatedToKeyword\\APIRequest - post.getRelatedToKeyword.xml");
            String sRequest = LoadXML("D:\\Sasa.Stojanovic\\Alert\\XML\\2011_12 Events\\Defined API Calls\\wiki.getRelatedToKeyword\\APIRequest - wiki.getRelatedToKeyword.xml");
            
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
