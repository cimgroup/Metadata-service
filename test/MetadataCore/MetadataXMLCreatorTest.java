/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MetadataCore;

import MetadataCore.MetadataGlobal.APIResponseData;
import MetadataCore.MetadataGlobal.OntoProperty;
import java.util.ArrayList;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Dejan
 */
public class MetadataXMLCreatorTest {
    
    public MetadataXMLCreatorTest() {
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
     * Test of CreateXMLAPIResponse method, of class MetadataXMLCreator.
     */
    @Ignore
    @Test
    public void testCreateXMLAPIResponse() {
        System.out.println("* MetadataXMLCreatorTest: CreateXMLAPIResponse");
        String sAPICall = "";
        String sEventId = "";
        APIResponseData oData = null;
        MetadataXMLCreator.CreateXMLAPIResponse(sAPICall, sEventId, oData);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of CreateXMLNewItemKeuiResponse method, of class MetadataXMLCreator.
     */
    @Ignore
    @Test
    public void testCreateXMLNewItemKeuiResponse() {
        System.out.println("* MetadataXMLCreatorTest: CreateXMLNewItemKeuiResponse");
        String sEventName = "";
        String sEventId = "";
        ArrayList<String> arResult = null;
        MetadataXMLCreator.CreateXMLNewItemKeuiResponse(sEventName, sEventId, arResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of CreateXMLNewItemResponse method, of class MetadataXMLCreator.
     */
    @Ignore
    @Test
    public void testCreateXMLNewItemResponse() {
        System.out.println("* MetadataXMLCreatorTest: CreateXMLNewItemResponse");
        String sEventName = "";
        String sEventId = "";
        Object oObject = null;
        MetadataXMLCreator.CreateXMLNewItemResponse(sEventName, sEventId, oObject);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of CreateXMLInstanceResponse method, of class MetadataXMLCreator.
     */
    @Ignore
    @Test
    public void testCreateXMLInstanceResponse() {
        System.out.println("* MetadataXMLCreatorTest: CreateXMLInstanceResponse");
        String sEventId = "";
        OntoProperty oProperty = null;
        MetadataXMLCreator.CreateXMLInstanceResponse(sEventId, oProperty);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
