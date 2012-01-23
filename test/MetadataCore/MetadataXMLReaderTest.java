/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MetadataCore;

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
        Document dDoc = null;
        MetadataXMLReader.ReadXML(dDoc);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
