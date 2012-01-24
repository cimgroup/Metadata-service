/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MetadataCore;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import java.util.Date;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Dejan
 */
public class MetadataGlobalTest {
    
    public MetadataGlobalTest() {
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
     * Test of LoadOWL method, of class MetadataGlobal.
     */
    @Test
    public void testLoadOWL() throws Exception {
        System.out.println("* MetadataGlobalTest: LoadOWL");
        String sLocation = MetadataConstants.sLocationLoadAlert;
        //OntModel expResult = null;
        OntModel result = MetadataGlobal.LoadOWL(sLocation);
        assertNotNull(result);
        //assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of LoadOWLWithModelSpec method, of class MetadataGlobal.
     */
    @Test
    public void testLoadOWLWithModelSpec() throws Exception {
        System.out.println("* MetadataGlobalTest: LoadOWLWithModelSpec");
        String sLocation = MetadataConstants.sLocationLoadAlert;
        OntModelSpec oModelSpec = OntModelSpec.OWL_MEM_MICRO_RULE_INF;
        //OntModel expResult = null;
        OntModel result = MetadataGlobal.LoadOWLWithModelSpec(sLocation, oModelSpec);
        assertNotNull(result);
        //assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of SaveOWL method, of class MetadataGlobal.
     */
    @Ignore
    @Test
    public void testSaveOWL() {
        System.out.println("* MetadataGlobalTest: SaveOWL");
        OntModel omModel = null;
        String sLocation = "";
        boolean expResult = false;
        boolean result = MetadataGlobal.SaveOWL(omModel, sLocation);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of GetNextId method, of class MetadataGlobal.
     */
    @Ignore
    @Test
    public void testGetNextId() {
        System.out.println("* MetadataGlobalTest: GetNextId");
        OntModel omModelNull = null;
        OntClass ocClassNull = null;
        int expResult = 0;
        int resultNull = MetadataGlobal.GetNextId(omModelNull, ocClassNull);
        assertEquals(expResult, resultNull);
        
        try
        {
            String sLocation = MetadataConstants.sLocationLoadAlert;
            OntModel omModel = MetadataGlobal.LoadOWL(sLocation);
            String sClassName = MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_Bug;
            OntClass ocClass = omModel.getOntClass(sClassName);
            int resultFirst = MetadataGlobal.GetNextId(omModel, ocClass);
//            String sClassURI = MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_Bug;
//            String sBugUri = MetadataGlobal.GetObjectURI(omModel, sClassURI, "184695");
//            boolean bOK = MetadataGlobal.SaveOWL(omModel, MetadataConstants.sLocationSaveAlert);
            int resultSecond = MetadataGlobal.GetNextId(omModel, ocClass);
            assertEquals(resultFirst + 1, resultSecond);
        }
        catch (Exception ex)
        {
            fail("Error:" + ex.getMessage());
        }
        //// TO-DO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of GetObjectURI method, of class MetadataGlobal.
     */
    @Test
    public void testGetObjectURI() {
        System.out.println("* MetadataGlobalTest: GetObjectURI");
        OntModel omModelNull = null;
        String sClassURINull = "";
        String sIDNull = "";
        String expResult = "";
        String resultNull = MetadataGlobal.GetObjectURI(omModelNull, sClassURINull, sIDNull);
        assertEquals(expResult, resultNull);
        
        try
        {
            String sLocation = MetadataConstants.sLocationLoadAlert;
            OntModel omModel = MetadataGlobal.LoadOWL(sLocation);
            String sClassURI = MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_Bug;
            String sID = "184671";
            String result = MetadataGlobal.GetObjectURI(omModel, sClassURI, sID);
            assertEquals(result.isEmpty(), false);
        }
        catch (Exception ex)
        {
            fail("Error:" + ex.getMessage());
        }
        //// TO-DO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of GetDateTime method, of class MetadataGlobal.
     */
    @Test
    public void testGetDateTime() {
        System.out.println("* MetadataGlobalTest: GetDateTime");
        
        String sDateTime1 = "2009-02-17 16:31";
        Date expResult1 = new Date(109, 1, 17, 16, 31); //year - 1900; month - 1
        Date result1 = MetadataGlobal.GetDateTime(sDateTime1);
        assertEquals(expResult1, result1);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
        
        String sDateTime2 = "2011-04-15 10:55:57";
        Date expResult2 = new Date(111, 3, 15, 10, 55, 57); //year - 1900; month - 1
        Date result2 = MetadataGlobal.GetDateTime(sDateTime2);
        assertEquals(expResult2, result2);
    }

    /**
     * Test of ExpandOntology method, of class MetadataGlobal.
     */
    @Test
    public void testExpandOntology() {
        System.out.println("* MetadataGlobalTest: ExpandOntology");
        MetadataGlobal.ExpandOntology();
        
        try
        {
            String sAnnotationClass = MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_Annotation;
            String sConceptClass = MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_AnnotationConcept;
            OntModel omModel = MetadataGlobal.LoadOWL(MetadataConstants.sLocationLoadAlert);
            OntClass ocAnnotation = omModel.getOntClass(sAnnotationClass);
            OntClass ocConcept = omModel.getOntClass(sConceptClass);
            assertNotNull(ocAnnotation);
            assertNotNull(ocConcept);
        }
        catch (Exception ex)
        {
            fail("Error:" + ex.getMessage());
        }
        //// TO-DO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
}
