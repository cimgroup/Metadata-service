/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MetadataCore;

import MetadataCore.MetadataGlobal.AnnotationData;
import MetadataObjects.*;
import java.util.ArrayList;
import java.util.Date;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Dejan
 */
public class MetadataModelTest {
    
    public MetadataModelTest() {
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
     * Test of CreateNewItemKeuiResponse method, of class MetadataModel.
     */
    @Ignore
    @Test
    public void testCreateNewItemKeuiResponse() {
        System.out.println("* MetadataModelTest: CreateNewItemKeuiResponse");
        String sEventName = "";
        String sEventId = "";
        ArrayList<String> arResult = null;
        MetadataModel.CreateNewItemKeuiResponse(sEventName, sEventId, arResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of SaveObjectNewIssue method, of class MetadataModel.
     */
    @Ignore
    @Test
    public void testSaveObjectNewIssue() throws Exception {
        System.out.println("* MetadataModelTest: SaveObjectNewIssue");
        String sEventId = "";
        Issue oIssue = null;
        MetadataModel.SaveObjectNewIssue(sEventId, null, oIssue, false);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of SaveObjectNewCommit method, of class MetadataModel.
     */
    @Ignore
    @Test
    public void testSaveObjectNewCommit() throws Exception {
        System.out.println("* MetadataModelTest: SaveObjectNewCommit");
        String sEventId = "";
        Commit oCommit = null;
        MetadataModel.SaveObjectNewCommit(sEventId, null, oCommit);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

//    /**
//     * Test of SaveObjectNewPerson method, of class MetadataModel.
//     */
//    @Ignore
//    @Test
//    public void testSaveObjectNewPerson() {
//        System.out.println("* MetadataModelTest: SaveObjectNewPerson");
//        String sEventId = "";
//        foaf_Person oPerson = null;
//        MetadataModel.SaveObjectNewPerson(sEventId, oPerson);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of SaveObjectNewForumPost method, of class MetadataModel.
     */
    @Ignore
    @Test
    public void testSaveObjectNewForumPost() {
        System.out.println("* MetadataModelTest: SaveObjectNewForumPost");
        String sEventId = "";
        ForumPost oForumPost = null;
        MetadataModel.SaveObjectNewForumPost(sEventId, null, oForumPost);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of SaveObjectNewAnnotationData method, of class MetadataModel.
     */
    @Ignore
    @Test
    public void testSaveObjectNewAnnotationData() {
        System.out.println("* MetadataModelTest: SaveObjectNewAnnotationData");
        //String sEventId = "";
        String sEventName = "";
        AnnotationData oAnnotation = null;
        //MetadataModel.SaveObjectNewAnnotationData(sEventId, oAnnotation);
        MetadataModel.SaveObjectNewAnnotationData(sEventName, null, oAnnotation);

        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of ac_sparql method, of class MetadataModel.
     */
    @Ignore
    @Test
    public void testAc_sparql() {
        System.out.println("* MetadataModelTest: ac_sparql");
        String sEventId = "";
        String sSPARQL = "";
        String sOntModelSpec = "";
        MetadataModel.ac_sparql(sEventId, sSPARQL, sOntModelSpec);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of ac_issue_getAllForProduct method, of class MetadataModel.
     */
    @Ignore
    @Test
    public void testAc_issue_getAllForProduct() {
        System.out.println("* MetadataModelTest: ac_issue_getAllForProduct");
        String sEventId = "";
        String sProductUri = "";
        Date dtmFromDate = new Date();
        MetadataModel.ac_issue_getAllForProduct(sEventId, sProductUri, dtmFromDate);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of ac_issue_getAllForMethod method, of class MetadataModel.
     */
    @Ignore
    @Test
    public void testAc_issue_getAllForMethod() {
        System.out.println("* MetadataModelTest: ac_issue_getAllForMethod");
        String sEventId = "";
        ArrayList<String> sMethodUri = null;
        MetadataModel.ac_issue_getAllForMethod(sEventId, sMethodUri);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of ac_issue_getAnnotationStatus method, of class MetadataModel.
     */
    @Ignore
    @Test
    public void testAc_issue_getAnnotationStatus() {
        System.out.println("* MetadataModelTest: ac_issue_getAnnotationStatus");
        String sEventId = "";
        String sIssueUri = "";
        MetadataModel.ac_issue_getAnnotationStatus(sEventId, sIssueUri);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of ac_issue_getInfo method, of class MetadataModel.
     */
    @Ignore
    @Test
    public void testAc_issue_getInfo() {
        System.out.println("* MetadataModelTest: ac_issue_getInfo");
        String sEventId = "";
        String sIssueUri = "";
        MetadataModel.ac_issue_getInfo(sEventId, sIssueUri);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of ac_issue_getExplicitDuplicates method, of class MetadataModel.
     */
    @Ignore
    @Test
    public void testAc_issue_getDuplicates() {
        System.out.println("* MetadataModelTest: ac_issue_getDuplicates");
        String sEventId = "";
        String sIssueDuplicatesSPARQL = "";
        MetadataModel.ac_issue_getExplicitDuplicates(sEventId, sIssueDuplicatesSPARQL);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of ac_person_getInfo method, of class MetadataModel.
     */
    @Ignore
    @Test
    public void testAc_person_getInfo() {
        System.out.println("* MetadataModelTest: ac_person_getInfo");
        String sEventId = "";
        String sPersonUri = "";
        MetadataModel.ac_person_getInfo(sEventId, sPersonUri);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of ac_person_getAllForEmail method, of class MetadataModel.
     */
    @Ignore
    @Test
    public void testAc_person_getAllForEmail() {
        System.out.println("* MetadataModelTest: ac_person_getAllForEmail");
        String sEventId = "";
        String eMail = "";
        MetadataModel.ac_person_getAllForEmail(sEventId, eMail);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of ac_identity_getForPerson method, of class MetadataModel.
     */
    @Ignore
    @Test
    public void testAc_identity_getForPerson() {
        System.out.println("* MetadataModelTest: ac_identity_getForPerson");
        String sEventId = "";
        String sPersonUri = "";
        MetadataModel.ac_identity_getForPerson(sEventId, sPersonUri);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of ac_competency_getPersonForIssue method, of class MetadataModel.
     */
    @Ignore
    @Test
    public void testAc_competency_getPersonForIssue() {
        System.out.println("* MetadataModelTest: ac_competency_getPersonForIssue");
        String sEventId = "";
        String sPersonForIssueSPARQL = "";
        MetadataModel.ac_competency_getPersonForIssue(sEventId, sPersonForIssueSPARQL);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of ac_method_getAllForIdentity method, of class MetadataModel.
     */
    @Ignore
    @Test
    public void testAc_method_getAllForPerson() {
        System.out.println("* MetadataModelTest: ac_method_getAllForPerson");
        String sEventId = "";
        String sPersonUri = "";
        MetadataModel.ac_method_getAllForIdentity(sEventId, sPersonUri);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of ac_method_getRelatedCode method, of class MetadataModel.
     */
    @Ignore
    @Test
    public void testAc_method_getRelatedCode() {
        System.out.println("* MetadataModelTest: ac_method_getRelatedCode");
        String sEventId = "";
        String sPersonUri = "";
        String sProductUri = "";
        MetadataModel.ac_method_getRelatedCode(sEventId, sPersonUri, sProductUri);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of ac_issue_getRelatedToKeyword method, of class MetadataModel.
     */
    @Ignore
    @Test
    public void testAc_issue_getRelatedToKeyword() {
        System.out.println("* MetadataModelTest: ac_issue_getRelatedToKeyword");
        String sEventId = "";
        String sKeyword = "";
        MetadataModel.ac_issue_getRelatedToKeyword(sEventId, sKeyword);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of ac_commit_getRelatedToKeyword method, of class MetadataModel.
     */
    @Ignore
    @Test
    public void testAc_commit_getRelatedToKeyword() {
        System.out.println("* MetadataModelTest: ac_commit_getRelatedToKeyword");
        String sEventId = "";
        String sKeyword = "";
        MetadataModel.ac_commit_getRelatedToKeyword(sEventId, sKeyword);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of ac_email_getRelatedToKeyword method, of class MetadataModel.
     */
    @Ignore
    @Test
    public void testAc_email_getRelatedToKeyword() {
        System.out.println("* MetadataModelTest: ac_email_getRelatedToKeyword");
        String sEventId = "";
        String sKeyword = "";
        MetadataModel.ac_email_getRelatedToKeyword(sEventId, sKeyword);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of ac_post_getRelatedToKeyword method, of class MetadataModel.
     */
    @Ignore
    @Test
    public void testAc_post_getRelatedToKeyword() {
        System.out.println("* MetadataModelTest: ac_post_getRelatedToKeyword");
        String sEventId = "";
        String sKeyword = "";
        MetadataModel.ac_post_getRelatedToKeyword(sEventId, sKeyword);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of ac_wiki_getRelatedToKeyword method, of class MetadataModel.
     */
    @Ignore
    @Test
    public void testAc_wiki_getRelatedToKeyword() {
        System.out.println("* MetadataModelTest: ac_wiki_getRelatedToKeyword");
        String sEventId = "";
        String sKeyword = "";
        MetadataModel.ac_wiki_getRelatedToKeyword(sEventId, sKeyword);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of ac_issue_getRelatedToIssue method, of class MetadataModel.
     */
    @Ignore
    @Test
    public void testAc_issue_getRelatedToIssue() {
        System.out.println("* MetadataModelTest: ac_issue_getRelatedToIssue");
        String sEventId = "";
        String sIssueUri = "";
        MetadataModel.ac_issue_getRelatedToIssue(sEventId, sIssueUri);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of ac_commit_getRelatedToIssue method, of class MetadataModel.
     */
    @Ignore
    @Test
    public void testAc_commit_getRelatedToIssue() {
        System.out.println("* MetadataModelTest: ac_commit_getRelatedToIssue");
        String sEventId = "";
        String sIssueUri = "";
        MetadataModel.ac_commit_getRelatedToIssue(sEventId, sIssueUri);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of ac_email_getRelatedToIssue method, of class MetadataModel.
     */
    @Ignore
    @Test
    public void testAc_email_getRelatedToIssue() {
        System.out.println("* MetadataModelTest: ac_email_getRelatedToIssue");
        String sEventId = "";
        String sIssueUri = "";
        MetadataModel.ac_email_getRelatedToIssue(sEventId, sIssueUri);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of ac_post_getRelatedToIssue method, of class MetadataModel.
     */
    @Ignore
    @Test
    public void testAc_post_getRelatedToIssue() {
        System.out.println("* MetadataModelTest: ac_post_getRelatedToIssue");
        String sEventId = "";
        String sIssueUri = "";
        MetadataModel.ac_post_getRelatedToIssue(sEventId, sIssueUri);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of ac_wiki_getRelatedToIssue method, of class MetadataModel.
     */
    @Ignore
    @Test
    public void testAc_wiki_getRelatedToIssue() {
        System.out.println("* MetadataModelTest: ac_wiki_getRelatedToIssue");
        String sEventId = "";
        String sIssueUri = "";
        MetadataModel.ac_wiki_getRelatedToIssue(sEventId, sIssueUri);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of GetInstance method, of class MetadataModel.
     */
    @Ignore
    @Test
    public void testGetInstance() {
        System.out.println("* MetadataModelTest: GetInstance");
        String sEventId = "";
        String sInstanceURL = "";
        //MetadataModel.GetInstance(sEventId, sInstanceURL);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
