/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MetadataCore;

import MetadataCore.MetadataGlobal.APIResponseData;
import MetadataCore.MetadataGlobal.AnnotationData;
import MetadataCore.MetadataGlobal.OntoProperty;
import MetadataObjects.Commit;
import MetadataObjects.Issue;
import MetadataObjects.NewForumPost;
import MetadataObjects.foaf_Person;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import java.util.ArrayList;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Dejan
 */
public class MetadataRDFConverterTest {
    
    public MetadataRDFConverterTest() {
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
     * Test of SaveIssue method, of class MetadataRDFConverter.
     */
    @Ignore
    @Test
    public void testSaveIssue() {
        System.out.println("* MetadataRDFConverterTest: SaveIssue");
        Issue oIssue = null;
        Issue expResult = null;
        Issue result = MetadataRDFConverter.SaveIssue(oIssue);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of SaveCommit method, of class MetadataRDFConverter.
     */
    @Ignore
    @Test
    public void testSaveCommit() {
        System.out.println("* MetadataRDFConverterTest: SaveCommit");
        Commit oCommit = null;
        Commit expResult = null;
        Commit result = MetadataRDFConverter.SaveCommit(oCommit);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of SavePerson method, of class MetadataRDFConverter.
     */
    @Ignore
    @Test
    public void testSavePerson() {
        System.out.println("* MetadataRDFConverterTest: SavePerson");
        foaf_Person oPerson = null;
        foaf_Person expResult = null;
        foaf_Person result = MetadataRDFConverter.SavePerson(oPerson);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of SavePersonData method, of class MetadataRDFConverter.
     */
    @Ignore
    @Test
    public void testSavePersonData() {
        System.out.println("* MetadataRDFConverterTest: SavePersonData");
        foaf_Person oPerson = null;
        OntModel oModel = null;
        MetadataRDFConverter.SavePersonData(oPerson, oModel);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of SaveAnnotationData method, of class MetadataRDFConverter.
     */
    @Ignore
    @Test
    public void testSaveAnnotationData() {
        System.out.println("* MetadataRDFConverterTest: SaveAnnotationData");
        AnnotationData oAnnotation = null;
        MetadataRDFConverter.SaveAnnotationData(oAnnotation);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of SaveForumPost method, of class MetadataRDFConverter.
     */
    @Ignore
    @Test
    public void testSaveForumPost() {
        System.out.println("* MetadataRDFConverterTest: SaveForumPost");
        NewForumPost oForumPost = null;
        NewForumPost expResult = null;
        NewForumPost result = MetadataRDFConverter.SaveForumPost(oForumPost);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of ac_issue_getAllForProduct method, of class MetadataRDFConverter.
     */
    @Ignore
    @Test
    public void testAc_issue_getAllForProduct() {
        System.out.println("* MetadataRDFConverterTest: ac_issue_getAllForProduct");
        String sProductUri = "";
        APIResponseData expResult = null;
        APIResponseData result = MetadataRDFConverter.ac_issue_getAllForProduct(sProductUri);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of ac_issue_getAllForMethod method, of class MetadataRDFConverter.
     */
    @Ignore
    @Test
    public void testAc_issue_getAllForMethod() {
        System.out.println("* MetadataRDFConverterTest: ac_issue_getAllForMethod");
        ArrayList<String> sMethodUri = null;
        APIResponseData expResult = null;
        APIResponseData result = MetadataRDFConverter.ac_issue_getAllForMethod(sMethodUri);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of ac_issue_getAnnotationStatus method, of class MetadataRDFConverter.
     */
    @Ignore
    @Test
    public void testAc_issue_getAnnotationStatus() {
        System.out.println("* MetadataRDFConverterTest: ac_issue_getAnnotationStatus");
        String sIssueUri = "";
        APIResponseData expResult = null;
        APIResponseData result = MetadataRDFConverter.ac_issue_getAnnotationStatus(sIssueUri);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of ac_issue_getInfo method, of class MetadataRDFConverter.
     */
    @Ignore
    @Test
    public void testAc_issue_getInfo() {
        System.out.println("* MetadataRDFConverterTest: ac_issue_getInfo");
        String sIssueUri = "";
        APIResponseData expResult = null;
        APIResponseData result = MetadataRDFConverter.ac_issue_getInfo(sIssueUri);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of ac_issue_getDuplicates method, of class MetadataRDFConverter.
     */
    @Ignore
    @Test
    public void testAc_issue_getDuplicates() {
        System.out.println("* MetadataRDFConverterTest: ac_issue_getDuplicates");
        String sIssueDuplicatesSPARQL = "";
        APIResponseData expResult = null;
        APIResponseData result = MetadataRDFConverter.ac_issue_getDuplicates(sIssueDuplicatesSPARQL);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of ac_person_getInfo method, of class MetadataRDFConverter.
     */
    @Ignore
    @Test
    public void testAc_person_getInfo() {
        System.out.println("* MetadataRDFConverterTest: ac_person_getInfo");
        String sPersonUri = "";
        APIResponseData expResult = null;
        APIResponseData result = MetadataRDFConverter.ac_person_getInfo(sPersonUri);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of ac_person_getAllForEmail method, of class MetadataRDFConverter.
     */
    @Ignore
    @Test
    public void testAc_person_getAllForEmail() {
        System.out.println("* MetadataRDFConverterTest: ac_person_getAllForEmail");
        String sEmail = "";
        APIResponseData expResult = null;
        APIResponseData result = MetadataRDFConverter.ac_person_getAllForEmail(sEmail);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of ac_identity_getForPerson method, of class MetadataRDFConverter.
     */
    @Ignore
    @Test
    public void testAc_identity_getForPerson() {
        System.out.println("* MetadataRDFConverterTest: ac_identity_getForPerson");
        String sFirstName = "";
        String sLastName = "";
        String sEmail = "";
        APIResponseData expResult = null;
        APIResponseData result = MetadataRDFConverter.ac_identity_getForPerson(sFirstName, sLastName, sEmail);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of ac_competency_getPersonForIssue method, of class MetadataRDFConverter.
     */
    @Ignore
    @Test
    public void testAc_competency_getPersonForIssue() {
        System.out.println("* MetadataRDFConverterTest: ac_competency_getPersonForIssue");
        String sPersonForIssueSPARQL = "";
        APIResponseData expResult = null;
        APIResponseData result = MetadataRDFConverter.ac_competency_getPersonForIssue(sPersonForIssueSPARQL);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of ac_method_getAllForPerson method, of class MetadataRDFConverter.
     */
    @Ignore
    @Test
    public void testAc_method_getAllForPerson() {
        System.out.println("* MetadataRDFConverterTest: ac_method_getAllForPerson");
        String sPersonUri = "";
        APIResponseData expResult = null;
        APIResponseData result = MetadataRDFConverter.ac_method_getAllForPerson(sPersonUri);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of ac_method_getRelatedCode method, of class MetadataRDFConverter.
     */
    @Ignore
    @Test
    public void testAc_method_getRelatedCode() {
        System.out.println("* MetadataRDFConverterTest: ac_method_getRelatedCode");
        String sPersonUri = "";
        String sProductUri = "";
        APIResponseData expResult = null;
        APIResponseData result = MetadataRDFConverter.ac_method_getRelatedCode(sPersonUri, sProductUri);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of ac_issue_getRelatedToKeyword method, of class MetadataRDFConverter.
     */
    @Ignore
    @Test
    public void testAc_issue_getRelatedToKeyword() {
        System.out.println("* MetadataRDFConverterTest: ac_issue_getRelatedToKeyword");
        String sKeyword = "";
        APIResponseData expResult = null;
        APIResponseData result = MetadataRDFConverter.ac_issue_getRelatedToKeyword(sKeyword);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of ac_commit_getRelatedToKeyword method, of class MetadataRDFConverter.
     */
    @Ignore
    @Test
    public void testAc_commit_getRelatedToKeyword() {
        System.out.println("* MetadataRDFConverterTest: ac_commit_getRelatedToKeyword");
        String sKeyword = "";
        APIResponseData expResult = null;
        APIResponseData result = MetadataRDFConverter.ac_commit_getRelatedToKeyword(sKeyword);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of ac_email_getRelatedToKeyword method, of class MetadataRDFConverter.
     */
    @Ignore
    @Test
    public void testAc_email_getRelatedToKeyword() {
        System.out.println("* MetadataRDFConverterTest: ac_email_getRelatedToKeyword");
        String sKeyword = "";
        APIResponseData expResult = null;
        APIResponseData result = MetadataRDFConverter.ac_email_getRelatedToKeyword(sKeyword);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of ac_post_getRelatedToKeyword method, of class MetadataRDFConverter.
     */
    @Ignore
    @Test
    public void testAc_post_getRelatedToKeyword() {
        System.out.println("* MetadataRDFConverterTest: ac_post_getRelatedToKeyword");
        String sKeyword = "";
        APIResponseData expResult = null;
        APIResponseData result = MetadataRDFConverter.ac_post_getRelatedToKeyword(sKeyword);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of ac_wiki_getRelatedToKeyword method, of class MetadataRDFConverter.
     */
    @Ignore
    @Test
    public void testAc_wiki_getRelatedToKeyword() {
        System.out.println("* MetadataRDFConverterTest: ac_wiki_getRelatedToKeyword");
        String sKeyword = "";
        APIResponseData expResult = null;
        APIResponseData result = MetadataRDFConverter.ac_wiki_getRelatedToKeyword(sKeyword);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of ac_issue_getRelatedToIssue method, of class MetadataRDFConverter.
     */
    @Ignore
    @Test
    public void testAc_issue_getRelatedToIssue() {
        System.out.println("* MetadataRDFConverterTest: ac_issue_getRelatedToIssue");
        String sIssueUri = "";
        APIResponseData expResult = null;
        APIResponseData result = MetadataRDFConverter.ac_issue_getRelatedToIssue(sIssueUri);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of ac_commit_getRelatedToIssue method, of class MetadataRDFConverter.
     */
    @Ignore
    @Test
    public void testAc_commit_getRelatedToIssue() {
        System.out.println("* MetadataRDFConverterTest: ac_commit_getRelatedToIssue");
        String sIssueUri = "";
        APIResponseData expResult = null;
        APIResponseData result = MetadataRDFConverter.ac_commit_getRelatedToIssue(sIssueUri);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of ac_email_getRelatedToIssue method, of class MetadataRDFConverter.
     */
    @Ignore
    @Test
    public void testAc_email_getRelatedToIssue() {
        System.out.println("* MetadataRDFConverterTest: ac_email_getRelatedToIssue");
        String sIssueUri = "";
        APIResponseData expResult = null;
        APIResponseData result = MetadataRDFConverter.ac_email_getRelatedToIssue(sIssueUri);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of ac_post_getRelatedToIssue method, of class MetadataRDFConverter.
     */
    @Ignore
    @Test
    public void testAc_post_getRelatedToIssue() {
        System.out.println("* MetadataRDFConverterTest: ac_post_getRelatedToIssue");
        String sIssueUri = "";
        APIResponseData expResult = null;
        APIResponseData result = MetadataRDFConverter.ac_post_getRelatedToIssue(sIssueUri);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of ac_wiki_getRelatedToIssue method, of class MetadataRDFConverter.
     */
    @Ignore
    @Test
    public void testAc_wiki_getRelatedToIssue() {
        System.out.println("* MetadataRDFConverterTest: ac_wiki_getRelatedToIssue");
        String sIssueUri = "";
        APIResponseData expResult = null;
        APIResponseData result = MetadataRDFConverter.ac_wiki_getRelatedToIssue(sIssueUri);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of ac_sparql method, of class MetadataRDFConverter.
     */
    @Ignore
    @Test
    public void testAc_sparql() {
        System.out.println("* MetadataRDFConverterTest: ac_sparql");
        String sSPARQL = "";
        OntModelSpec oOntModelSpec = null;
        APIResponseData expResult = null;
        APIResponseData result = MetadataRDFConverter.ac_sparql(sSPARQL, oOntModelSpec);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of GetAllMembers method, of class MetadataRDFConverter.
     */
    @Ignore
    @Test
    public void testGetAllMembers() {
        System.out.println("* MetadataRDFConverterTest: GetAllMembers");
        String sOntClass = "";
        APIResponseData expResult = null;
        APIResponseData result = MetadataRDFConverter.GetAllMembers(sOntClass);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of GetMember method, of class MetadataRDFConverter.
     */
    @Ignore
    @Test
    public void testGetMember() {
        System.out.println("* MetadataRDFConverterTest: GetMember");
        String sMemberURL = "";
        OntoProperty expResult = null;
        OntoProperty result = MetadataRDFConverter.GetMember(sMemberURL);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of CreateQuery method, of class MetadataRDFConverter.
     */
    @Ignore
    @Test
    public void testCreateQuery() throws Exception {
        System.out.println("* MetadataRDFConverterTest: CreateQuery");
        MetadataRDFConverter.CreateQuery();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of CreateTriples method, of class MetadataRDFConverter.
     */
    @Ignore
    @Test
    public void testCreateTriples() {
        System.out.println("* MetadataRDFConverterTest: CreateTriples");
        MetadataRDFConverter.CreateTriples();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of SearchForIDs method, of class MetadataRDFConverter.
     */
    @Ignore
    @Test
    public void testSearchForIDs() throws Exception {
        System.out.println("* MetadataRDFConverterTest: SearchForIDs");
        String sSearchType = "";
        ArrayList<String> sIDs = null;
        MetadataRDFConverter.SearchForIDs(sSearchType, sIDs);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
