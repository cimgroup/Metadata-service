/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MetadataCore;

import MetadataCore.MetadataGlobal.APIResponseData;
import MetadataCore.MetadataGlobal.AnnotationData;
import MetadataCore.MetadataGlobal.OntoProperty;
import MetadataObjects.*;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import java.util.ArrayList;
import java.util.Date;
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

    private Issue GetIssueExample() {
        Issue oIssue = new Issue();
        oIssue.m_sID = "184671";
        oIssue.m_oHasReporter = new foaf_Person();
        oIssue.m_oHasReporter.m_sFirstName = "Sander";
        oIssue.m_oHasReporter.m_sLastName = "Pientka";
        oIssue.m_oHasReporter.m_sID = "cumulus0007@gmail.com";
        oIssue.m_oHasReporter.m_sEmail = "cumulus0007@gmail.com";
        oIssue.m_oHasState = new Resolved();
        oIssue.m_oHasResolution = new Fixed();
        oIssue.m_sDescription = "Notify user on hardware changes 2";
        oIssue.m_sKeyword = "keyword";
        oIssue.m_oIsIssueOf = new Component();
        oIssue.m_oIsIssueOf.m_sID = "general";
        oIssue.m_oIsIssueOf.m_oIsComponentOf = new Product();
        oIssue.m_oIsIssueOf.m_oIsComponentOf.m_sID = "solid";
        oIssue.m_oIsIssueOf.m_oIsComponentOf.m_sVersion = "unspecified";
        oIssue.m_oHasComputerSystem = new ComputerSystem();
        oIssue.m_oHasComputerSystem.m_sID = "";
        oIssue.m_oHasComputerSystem.m_sPlatform = "Ubuntu Packages";
        oIssue.m_oHasComputerSystem.m_sOs = "Linux";
        oIssue.m_oHasPriority = new Priority();
        oIssue.m_oHasPriority.m_iPriority = 4;
        oIssue.m_oHasSeverity = new Blocker();
        oIssue.m_oHasAssignee = new foaf_Person();
        oIssue.m_oHasAssignee.m_sFirstName = "Alex";
        oIssue.m_oHasAssignee.m_sLastName = "Fiestas";
        oIssue.m_oHasAssignee.m_sID = "afiestas@kde.org";
        oIssue.m_oHasAssignee.m_sEmail = "afiestas@kde.org";
        oIssue.m_oHasCCPerson = new foaf_Person[1];
        oIssue.m_oHasCCPerson[0] = new foaf_Person();
        oIssue.m_oHasCCPerson[0].m_sFirstName = "";
        oIssue.m_oHasCCPerson[0].m_sLastName = "";
        oIssue.m_oHasCCPerson[0].m_sID = "angel_blue_co2004@yahoo.com";
        oIssue.m_oHasCCPerson[0].m_sEmail = "angel_blue_co2004@yahoo.com";
        oIssue.m_sBugURL = "https://bugs.kde.org/show_bug.cgi?id=184671";
        oIssue.m_oDependsOn = new Issue[2];
        oIssue.m_oDependsOn[0] = new Issue();
        oIssue.m_oDependsOn[0].m_sID = "10001";
        oIssue.m_oDependsOn[1] = new Issue();
        oIssue.m_oDependsOn[1].m_sID = "10002";
        oIssue.m_oBlocks = new Issue[2];
        oIssue.m_oBlocks[0] = new Issue();
        oIssue.m_oBlocks[0].m_sID = "20001";
        oIssue.m_oBlocks[1] = new Issue();
        oIssue.m_oBlocks[1].m_sID = "20002";
        oIssue.m_oIsDuplicateOf = new Issue();
        oIssue.m_oIsDuplicateOf.m_sID = "235223";
        oIssue.m_oIsMergedInto = new Issue();
        oIssue.m_oIsMergedInto.m_sID = "231511";
        oIssue.m_dtmDateOpened = new Date(109, 1, 17, 16, 31);
        oIssue.m_dtmLastModified = new Date(111, 3, 15, 10, 55, 57);
        oIssue.m_oHasMilestone = new Milestone();
        oIssue.m_oHasMilestone.m_sID = "12355";
        oIssue.m_oHasMilestone.m_dtmTarget = new Date(109, 1, 17, 16, 31);
        oIssue.m_oHasComment = new Comment[2];
        oIssue.m_oHasComment[0] = new Comment();
        oIssue.m_oHasComment[0].m_sID = "";
        oIssue.m_oHasComment[0].m_iNumber = 0;
        oIssue.m_oHasComment[0].m_sText = "Version: (using KDE 4.2.0) OS: Linux Installed from: Ubuntu Packages It would be awesome if KDE notifies te user of newly installed hardware, missing drivers, programs to use the connected device, etc.";
        oIssue.m_oHasComment[0].m_oHasCommentor = new foaf_Person();
        oIssue.m_oHasComment[0].m_oHasCommentor.m_sFirstName = "Sander";
        oIssue.m_oHasComment[0].m_oHasCommentor.m_sLastName = "Pientka";
        oIssue.m_oHasComment[0].m_oHasCommentor.m_sID = "cumulus0007@gmail.com";
        oIssue.m_oHasComment[0].m_oHasCommentor.m_sEmail = "cumulus0007@gmail.com";
        oIssue.m_oHasComment[0].m_dtmDate = new Date(109, 1, 17, 16, 31, 37);
        oIssue.m_oHasComment[1] = new Comment();
        oIssue.m_oHasComment[1].m_sID = "";
        oIssue.m_oHasComment[1].m_iNumber = 1;
        oIssue.m_oHasComment[1].m_sText = "If a driver is missing, clicking on the notification should open the package manager (by default) so the user can find a driver";
        oIssue.m_oHasComment[1].m_oHasCommentor = new foaf_Person();
        oIssue.m_oHasComment[1].m_oHasCommentor.m_sFirstName = "Angel";
        oIssue.m_oHasComment[1].m_oHasCommentor.m_sLastName = "Blue";
        oIssue.m_oHasComment[1].m_oHasCommentor.m_sID = "angel_blue_co2004@yahoo.com";
        oIssue.m_oHasComment[1].m_oHasCommentor.m_sEmail = "angel_blue_co2004@yahoo.com";
        oIssue.m_oHasComment[1].m_dtmDate = new Date(109, 2, 4, 18, 11, 19);
        oIssue.m_oHasAttachment = new Attachment[1];
        oIssue.m_oHasAttachment[0] = new Attachment();
        oIssue.m_oHasAttachment[0].m_sID = "3532";
        oIssue.m_oHasAttachment[0].m_sFilename = "name";
        oIssue.m_oHasAttachment[0].m_sType = "doc";
        oIssue.m_oHasAttachment[0].m_oHasCreator = new foaf_Person();
        oIssue.m_oHasAttachment[0].m_oHasCreator.m_sFirstName = "Angel";
        oIssue.m_oHasAttachment[0].m_oHasCreator.m_sLastName = "Blue01";
        oIssue.m_oHasAttachment[0].m_oHasCreator.m_sID = "angel_blue_co2004@yahoo.com";
        oIssue.m_oHasAttachment[0].m_oHasCreator.m_sEmail = "angel_blue_co2004@yahoo.com";
        oIssue.m_oHasActivity = new Activity[3];
        oIssue.m_oHasActivity[0] = new Activity();
        oIssue.m_oHasActivity[0].m_sID = "4125";
        oIssue.m_oHasActivity[0].m_oHasInvolvedPerson = new foaf_Person();
        oIssue.m_oHasActivity[0].m_oHasInvolvedPerson.m_sID = "angel_blue_co2004@yahoo.com";
        oIssue.m_oHasActivity[0].m_dtmWhen = new Date(109, 2, 4, 18, 11, 19);
        oIssue.m_oHasActivity[0].m_sWhat = "Priority";
        oIssue.m_oHasActivity[0].m_sRemoved = "3";
        oIssue.m_oHasActivity[0].m_sAdded = "4";
        oIssue.m_oHasActivity[1] = new Activity();
        oIssue.m_oHasActivity[1].m_sID = "4125";
        oIssue.m_oHasActivity[1].m_oHasInvolvedPerson = new foaf_Person();
        oIssue.m_oHasActivity[1].m_oHasInvolvedPerson.m_sID = "angel_blue_co2004@yahoo.com";
        oIssue.m_oHasActivity[1].m_dtmWhen = new Date(109, 2, 4, 18, 11, 19);
        oIssue.m_oHasActivity[1].m_sWhat = "CC";
        oIssue.m_oHasActivity[1].m_sRemoved = "lcanas@libresoft.es";
        oIssue.m_oHasActivity[1].m_sAdded = "";
        oIssue.m_oHasActivity[2] = new Activity();
        oIssue.m_oHasActivity[2].m_sID = "4125";
        oIssue.m_oHasActivity[2].m_oHasInvolvedPerson = new foaf_Person();
        oIssue.m_oHasActivity[2].m_oHasInvolvedPerson.m_sID = "angel_blue_co2004@yahoo.com";
        oIssue.m_oHasActivity[2].m_dtmWhen = new Date(109, 2, 4, 18, 11, 19);
        oIssue.m_oHasActivity[2].m_sWhat = "Severity";
        oIssue.m_oHasActivity[2].m_sRemoved = "normal";
        oIssue.m_oHasActivity[2].m_sAdded = "blocker";
        return oIssue;
    }

    /**
     * Test of SaveIssue method, of class MetadataRDFConverter.
     */
    @Test
    public void testSaveIssue() {
        System.out.println("* MetadataRDFConverterTest: SaveIssue");
        Issue oIssueNull = null;
        Issue expResult = null;
        Issue resultNull = MetadataRDFConverter.SaveIssue(oIssueNull);
        assertEquals(expResult, resultNull);
        
        Issue oIssue = GetIssueExample();
        Issue result = MetadataRDFConverter.SaveIssue(oIssue);
        assertNotNull(result);
        assertNotSame("", result.m_sObjectURI);
        
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of SaveCommit method, of class MetadataRDFConverter.
     */
    @Test
    public void testSaveCommit() {
        System.out.println("* MetadataRDFConverterTest: SaveCommit");
        Commit oCommitNull = null;
        Commit expResult = null;
        Commit resultNull = MetadataRDFConverter.SaveCommit(oCommitNull);
        assertEquals(expResult, resultNull);
        
        Commit oCommit = new Commit();
        oCommit.m_oIsCommitOfRepository = new Repository();
        oCommit.m_oIsCommitOfRepository.m_sObjectURI = "http://www.alert-project.eu/ontologies/alert_scm.owl#Repository1";
        oCommit.m_sRevisionTag = "1";
        oCommit.m_oHasAuthor = new foaf_Person();
        oCommit.m_oHasAuthor.m_sFirstName = "Sasa";
        oCommit.m_oHasAuthor.m_sLastName = "Stojanovic";
        oCommit.m_oHasAuthor.m_sID = "sasa.stojanovic@cimcollege.rs";
        oCommit.m_oHasAuthor.m_sEmail = "sasa.stojanovic@cimcollege.rs";
        oCommit.m_oHasCommitter = new foaf_Person();
        oCommit.m_oHasCommitter.m_sFirstName = "Ivan";
        oCommit.m_oHasCommitter.m_sLastName = "Obradovic";
        oCommit.m_oHasCommitter.m_sID = "ivan.obradovic@cimcollege.rs";
        oCommit.m_oHasCommitter.m_sEmail = "ivan.obradovic@cimcollege.rs";
        oCommit.m_dtmCommitDate = new Date(112, 0, 16, 16, 31);
        oCommit.m_sCommitMessage = "comment of commit";
               
        Commit result = MetadataRDFConverter.SaveCommit(oCommit);
        assertNotNull(result);
        assertNotSame("", result.m_sObjectURI);
        
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of SavePerson method, of class MetadataRDFConverter.
     */
    @Test
    public void testSavePerson() {
        System.out.println("* MetadataRDFConverterTest: SavePerson");
        foaf_Person oPersonNull = null;
        foaf_Person expResult = null;
        foaf_Person resultNull = MetadataRDFConverter.SavePerson(oPersonNull);
        assertEquals(expResult, resultNull);
        
        foaf_Person oPerson = null;
        oPerson.m_sFirstName = "Ivan";
        oPerson.m_sLastName = "Obradovic";
        oPerson.m_sGender = ""; //???
        oPerson.m_sID = "ivan.obradovic@cimcollege.rs";
        oPerson.m_sEmail = "ivan.obradovic@cimcollege.rs"; //???

        foaf_Person result = MetadataRDFConverter.SavePerson(oPerson);
        assertNotNull(result);
        assertNotSame("", result.m_sObjectURI);
        
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
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
}
