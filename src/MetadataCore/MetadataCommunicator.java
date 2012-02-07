/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MetadataCore;

import ActiveMQ.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;

import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import java.io.*;
import org.apache.xerces.dom.DocumentImpl;

/**
 *
 * @author ivano
 */
public class MetadataCommunicator {
    
    // <editor-fold desc="Members">
    
    public static String m_sXML = "";
    
    // </editor-fold>
    
    // <editor-fold desc="Methods">
    
    /**
     * @summary Method for receiving XML file from web service
     * @startRealisation Sasa Stojanovic  07.09.2011.
     * @finalModification Sasa Stojanovic  07.09.2011.
     */
    public static String ReceiveXML(String sDoc)
    {
        Document dDoc = LoadXMLFromString(sDoc);
        MetadataXMLReader.ReadXML(dDoc);
        return m_sXML;
    }
       
    /**
     * @summary Method for sending XML file
     * @startRealisation Ivan Obradovic  17.06.2011.
     * @finalModification Sasa Stojanovic  23.06.2011.
     */
    public static void SendXML(Document dDoc)
    {
        try
        {
            dDoc.setXmlStandalone(true); //needed for OutputKeys.STANDALONE to have an effect
            Source source = new DOMSource(dDoc);
            StringWriter stringWriter = new StringWriter();
            Result result = new StreamResult(stringWriter);
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");           
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            //transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            
            transformer.transform(source, result);
            m_sXML = stringWriter.getBuffer().toString();
            SimpleTopicPublisher.publish("MetadataOut", m_sXML);
          
            
            
            //Create transformer
            //Transformer tTransformer = TransformerFactory.newInstance().newTransformer();
            TransformerFactory tFactory = TransformerFactory.newInstance();
            tFactory.setAttribute("indent-number", 2);
            Transformer tTransformer = tFactory.newTransformer();
            //Output Types (text/xml/html)
            tTransformer.setOutputProperty(OutputKeys.METHOD, "xml");           
            tTransformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            tTransformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            //tTransformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
            tTransformer.setOutputProperty(OutputKeys.INDENT, "yes");
            tTransformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            //Write the document to a file
            Source srcDocument = new DOMSource(dDoc);
            Result rsLocation = new StreamResult(new File("D:\\Sasa.Stojanovic\\Response.xml"));
            tTransformer.transform(srcDocument, rsLocation);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * @summary Method for load XML file from local location
     * @startRealisation Ivan Obradovic  17.06.2011.
     * @finalModification Sasa Stojanovic  23.06.2011.
     * @param sLocation - location on hard disk
     * @return loaded document
     */
    public static Document LoadXML(String sLocation)
    {
        Document dDoc;
        try
        {
            DocumentBuilderFactory dbfFactory = DocumentBuilderFactory.newInstance();
            dbfFactory.setNamespaceAware(true);
            DocumentBuilder dbBuilder = dbfFactory.newDocumentBuilder();
            dDoc = dbBuilder.parse(new File(sLocation));
            dDoc.getDocumentElement().normalize();
            return dDoc;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return null;
        }
    }
    
    public static Document LoadXMLFromString(String sDoc)
    {
        Document dDoc;
        try
        {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();

            return builder.parse(new ByteArrayInputStream(sDoc.getBytes()));
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return null;
        }
    }
    
    // </editor-fold>
    
//    public static void main(String[] args)
//    {
//        ReceiveXMLLocal();
//    }
}