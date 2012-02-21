/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MetadataCore;

import MetadataCore.MetadataGlobal.AnnotationData;
import MetadataObjects.*;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Dejan
 */
public class MetadataObjectFactoryTest {
    
    public MetadataObjectFactoryTest() {
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
     * Test of CreateNewIssue method, of class MetadataObjectFactory.
     */
    @Test
    public void testCreateNewIssue() {
        System.out.println("* MetadataObjectFactoryTest: CreateNewIssue");
        //Issue expResult = null;
        //Issue expResult = new Issue();
        Issue result = MetadataObjectFactory.CreateNewIssue();
        assertNotNull(result);
        //assertEquals(expResult, result);
        // TO-DO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of CreateNewCommit method, of class MetadataObjectFactory.
     */
    @Test
    public void testCreateNewCommit() {
        System.out.println("* MetadataObjectFactoryTest: CreateNewCommit");
        //Commit expResult = null;
        //Commit expResult = new Commit();
        Commit result = MetadataObjectFactory.CreateNewCommit();
        assertNotNull(result);
        //assertEquals(expResult, result);
        // TO-DO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of CreateNewPerson method, of class MetadataObjectFactory.
     */
    @Test
    public void testCreateNewPerson() {
        System.out.println("* MetadataObjectFactoryTest: CreateNewPerson");
        //foaf_Person expResult = null;
        //foaf_Person expResult = new foaf_Person();
        foaf_Person result = MetadataObjectFactory.CreateNewPerson();
        assertNotNull(result);
        //assertEquals(expResult, result);
        // TO-DO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of CreateNewComponent method, of class MetadataObjectFactory.
     */
    @Test
    public void testCreateNewComponent() {
        System.out.println("* MetadataObjectFactoryTest: CreateNewComponent");
        //Component expResult = null;
        //Component expResult = new Component();
        Component result = MetadataObjectFactory.CreateNewComponent();
        assertNotNull(result);
        //assertEquals(expResult, result);
        // TO-DO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of CreateNewAnnotation method, of class MetadataObjectFactory.
     */
    @Test
    public void testCreateNewAnnotation() {
        System.out.println("* MetadataObjectFactoryTest: CreateNewAnnotation");
        //AnnotationData expResult = null;
        //AnnotationData expResult = new AnnotationData();
        AnnotationData result = MetadataObjectFactory.CreateNewAnnotation();
        assertNotNull(result);
        //assertEquals(expResult, result);
        // TO-DO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of CreateNewForumPost method, of class MetadataObjectFactory.
     */
    @Test
    public void testCreateNewForumPost() {
        System.out.println("* MetadataObjectFactoryTest: CreateNewForumPost");
        //NewForumPost expResult = null;
        //NewForumPost expResult = new NewForumPost();
        ForumPost result = MetadataObjectFactory.CreateNewForumPost();
        assertNotNull(result);
        //assertEquals(expResult, result);
        // TO-DO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
}
