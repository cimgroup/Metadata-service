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
public class MetadataCommunicatorTest {
    
    public MetadataCommunicatorTest() {
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
     * Test of ReceiveXML method, of class MetadataCommunicator.
     */
    @Ignore
    @Test
    public void testReceiveXML() {
        System.out.println("* MetadataCommunicatorTest: ReceiveXML");
        String sDoc = "";
        String expResult = "";
        String result = MetadataCommunicator.ReceiveXML(sDoc);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of SendXML method, of class MetadataCommunicator.
     */
    @Ignore
    @Test
    public void testSendXML() {
        System.out.println("* MetadataCommunicatorTest: SendXML");
        Document dDoc = null;
        MetadataCommunicator.SendXML(dDoc, "");
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of LoadXML method, of class MetadataCommunicator.
     */
    @Ignore
    @Test
    public void testLoadXML() {
        System.out.println("* MetadataCommunicatorTest: LoadXML");
        String sLocation = "";
        Document expResult = null;
        Document result = MetadataCommunicator.LoadXML(sLocation);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of LoadXMLFromString method, of class MetadataCommunicator.
     */
    @Ignore
    @Test
    public void testLoadXMLFromString() {
        System.out.println("* MetadataCommunicatorTest: LoadXMLFromString");
        String sDoc = "";
        Document expResult = null;
        Document result = MetadataCommunicator.LoadXMLFromString(sDoc);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
