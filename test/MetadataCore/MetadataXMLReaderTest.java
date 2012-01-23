/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MetadataCore;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.StringWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.junit.*;
import static org.junit.Assert.*;
import org.w3c.dom.Document;

/**
 *
 * @author Dejan
 */
public class MetadataXMLReaderTest {
        
    public MetadataXMLReaderTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of ReadXML method, of class MetadataXMLReader.
     */
    @Ignore
    @Test
    public void testReadXML() {
        System.out.println("* MetadataXMLReaderTest: ReadXML");
        //Document dDoc = null;
        String sProjectPath = System.getProperty("user.dir");
        String sDocPath = sProjectPath + "\\Events\\Data Storage Events\\Metadata.issue\\Metadata.issue.requestNew.xml";
        DocumentBuilderFactory dbfFactory = DocumentBuilderFactory.newInstance();
        dbfFactory.setNamespaceAware(true);
        try
        {
            DocumentBuilder dbBuilder = dbfFactory.newDocumentBuilder();
            Document dDoc = dbBuilder.parse(new File(sDocPath));
            dDoc.getDocumentElement().normalize();
            
            MetadataXMLReader.ReadXML(dDoc); //Result cannot be checked!!!
            
            // TODO review the generated test code and remove the default call to fail.
            //fail("The test case is a prototype.");
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            fail("The test case generated exception.");
        }
    }
}
