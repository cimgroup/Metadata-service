/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MetadataCore;

import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Dejan
 */
public class MetadataWebServiceTest {
    
    public MetadataWebServiceTest() {
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
     * Test of XMLRequest method, of class MetadataWebService.
     */
    @Ignore
    @Test
    public void testXMLRequest() throws Exception {
        System.out.println("* MetadataWebServiceTest: XMLRequest");
        String sDoc = "";
        MetadataWebService instance = new MetadataWebService();
        String expResult = "";
        String result = instance.XMLRequest(sDoc);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of LoadXML method, of class MetadataWebService.
     */
    @Ignore
    @Test
    public void testLoadXML() {
        System.out.println("* MetadataWebServiceTest: LoadXML");
        String sLocation = "";
        String expResult = "";
        String result = MetadataWebService.LoadXML(sLocation);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of main method, of class MetadataWebService.
     */
    @Ignore
    @Test
    public void testMain() {
        System.out.println("* MetadataWebServiceTest: main");
        String[] args = null;
        MetadataWebService.main(args);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
