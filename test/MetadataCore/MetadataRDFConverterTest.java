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

    /**
     * Creates example Isue.
     * @startRealisation  Dejan Milosavljevic 24.01.2012.
     * @finalModification Dejan Milosavljevic 24.01.2012.
     * @return 
     */
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
        Issue resultNull = MetadataRDFConverter.SaveIssue(oIssueNull, true);
        assertEquals(expResult, resultNull);
        
        Issue oIssue = GetIssueExample();
        Issue result = MetadataRDFConverter.SaveIssue(oIssue, true);
        assertNotNull(result);
        assertEquals(result.m_sObjectURI.isEmpty(), false);
        assertEquals(result.m_oHasReporter.m_sObjectURI.isEmpty(), false);
        assertEquals(result.m_oIsIssueOf.m_sObjectURI.isEmpty(), false);
        assertEquals(result.m_oHasComputerSystem.m_sObjectURI.isEmpty(), false);
        assertEquals(result.m_oHasAssignee.m_sObjectURI.isEmpty(), false);
        assertEquals(result.m_oHasCCPerson[0].m_sObjectURI.isEmpty(), false);
        assertEquals(result.m_oDependsOn[0].m_sObjectURI.isEmpty(), false);
        assertEquals(result.m_oDependsOn[1].m_sObjectURI.isEmpty(), false);
        assertEquals(result.m_oBlocks[0].m_sObjectURI.isEmpty(), false);
        assertEquals(result.m_oBlocks[1].m_sObjectURI.isEmpty(), false);
        assertEquals(result.m_oIsDuplicateOf.m_sObjectURI.isEmpty(), false);
        assertEquals(result.m_oIsMergedInto.m_sObjectURI.isEmpty(), false);
        assertEquals(result.m_oHasMilestone.m_sObjectURI.isEmpty(), false);
        assertEquals(result.m_oHasComment[0].m_sObjectURI.isEmpty(), false);
        assertEquals(result.m_oHasComment[0].m_oHasCommentor.m_sObjectURI.isEmpty(), false);
        assertEquals(result.m_oHasComment[1].m_sObjectURI.isEmpty(), false);
        assertEquals(result.m_oHasComment[1].m_oHasCommentor.m_sObjectURI.isEmpty(), false);
        assertEquals(result.m_oHasActivity[0].m_sObjectURI.isEmpty(), false);
        assertEquals(result.m_oHasActivity[1].m_sObjectURI.isEmpty(), false);
        assertEquals(result.m_oHasActivity[2].m_sObjectURI.isEmpty(), false);
       //assertNotSame("", result.m_sObjectURI);
        
        //// TO-DO review the generated test code and remove the default call to fail.
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
        assertEquals(result.m_sObjectURI.isEmpty(), false);
        assertEquals(result.m_oHasAuthor.m_sObjectURI.isEmpty(), false);
        assertEquals(result.m_oHasCommitter.m_sObjectURI.isEmpty(), false);
        //assertNotSame("", result.m_sObjectURI);
        
        //// TO-DO review the generated test code and remove the default call to fail.
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
        
        foaf_Person oPerson = new foaf_Person();
        oPerson.m_sFirstName = "Ivan";
        oPerson.m_sLastName = "Obradovic";
        oPerson.m_sGender = ""; //???
        oPerson.m_sID = "ivan.obradovic@cimcollege.rs";
        //oPerson.m_sEmail = "ivan.obradovic@cimcollege.rs"; //???

        foaf_Person result = MetadataRDFConverter.SavePerson(oPerson);
        assertNotNull(result);
        assertEquals(result.m_sObjectURI.isEmpty(), false);
        //assertNotSame("", result.m_sObjectURI);
        
        //// TO-DO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of SavePersonData method, of class MetadataRDFConverter.
     */
    @Test
    public void testSavePersonData() {
        System.out.println("* MetadataRDFConverterTest: SavePersonData");
        try
        {
            foaf_Person oPersonNull = null;
            OntModel oModel = MetadataGlobal.LoadOWL(MetadataConstants.sLocationLoadAlert);
            MetadataRDFConverter.SavePersonData(oPersonNull, oModel);
            MetadataGlobal.SaveOWL(oModel, MetadataConstants.sLocationSaveAlert);
 
            foaf_Person oPerson = new foaf_Person();
            oPerson.m_sFirstName = "Ivan";
            oPerson.m_sLastName = "Obradovic";
            //oPerson.m_sGender = ""; //???
            oPerson.m_sID = "ivan.obradovic@cimcollege.rs";
            oPerson.m_sEmail = "ivan.obradovic@cimcollege.rs"; //???
            MetadataRDFConverter.SavePersonData(oPerson, oModel);
            MetadataGlobal.SaveOWL(oModel, MetadataConstants.sLocationSaveAlert);
            assertNotNull(oPerson);
            assertEquals(oPerson.m_sObjectURI.isEmpty(), false);
            
            //// TO-DO review the generated test code and remove the default call to fail.
            //fail("The test case is a prototype.");
        }
        catch (Exception ex)
        {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    /**
     * Test of SaveAnnotationData method, of class MetadataRDFConverter.
     */
    @Test
    public void testSaveAnnotationData() {
        System.out.println("* MetadataRDFConverterTest: SaveAnnotationData");
        AnnotationData oAnnotationNull = null;
        AnnotationData expResult = null;
        AnnotationData resultNull = MetadataRDFConverter.SaveAnnotationData(oAnnotationNull);
        assertEquals(expResult, resultNull);
        
        AnnotationData oAnnotation = new AnnotationData();
        oAnnotation.m_sObjectURI = "http://www.alert-project.eu/ontologies/alert_its.owl#Bug1";
        oAnnotation.oAnnotated = new MetadataGlobal.AnnotationProp[2];
        oAnnotation.oAnnotated[0] = new MetadataGlobal.AnnotationProp();
        oAnnotation.oAnnotated[0].sName = MetadataConstants.c_XMLE_issueDescriptionAnnotated;
        oAnnotation.oAnnotated[0].sValue = "This is test <concept uri=\"http://www.alert-project.eu/ontologies/alert_its.owl#Bug1\">annotation</concept> for subject";
        oAnnotation.oAnnotated[1] = new MetadataGlobal.AnnotationProp();
        oAnnotation.oAnnotated[1].sName = MetadataConstants.c_XMLE_commentTextAnnotated;
        oAnnotation.oAnnotated[1].sValue = "This is test <concept uri=\"http://www.alert-project.eu/ontologies/alert_its.owl#Bug1\">annotation</concept> for description";
        oAnnotation.SetKeywords();
//        oAnnotation.oConcepts = new MetadataGlobal.ConceptProp[2];
//        oAnnotation.oConcepts[0] = new MetadataGlobal.ConceptProp();
//        oAnnotation.oConcepts[0].sName = MetadataConstants.c_XMLE_issueDescriptionConcepts;
//        oAnnotation.oConcepts[0].sUri = "http://www.alert-project.eu/ontologies/alert_its.owl#Bug1";
//        oAnnotation.oConcepts[0].sWeight = "1";
//        oAnnotation.oConcepts[1] = new MetadataGlobal.ConceptProp();
//        oAnnotation.oConcepts[1].sName = MetadataConstants.c_XMLE_commentTextConcepts;
//        oAnnotation.oConcepts[1].sUri = "http://www.alert-project.eu/ontologies/alert_its.owl#Bug1";
//        oAnnotation.oConcepts[1].sWeight = "1";
//        AnnotationData result = MetadataRDFConverter.SaveAnnotationData(oAnnotation);
//        assertNotNull(result);
//        assertNotNull(result.oAnnotated);
//        assertNotNull(result.oConcepts);
//        assertEquals(result.oAnnotated[0].m_sObjectURI.isEmpty(), false);
//        assertEquals(result.oAnnotated[1].m_sObjectURI.isEmpty(), false);
//        assertEquals(result.oConcepts[0].m_sObjectURI.isEmpty(), false);
//        assertEquals(result.oConcepts[1].m_sObjectURI.isEmpty(), false);
        oAnnotation.oAnnotated[0].oConcepts = new MetadataGlobal.ConceptProp[1];
        oAnnotation.oAnnotated[0].oConcepts[0] = new MetadataGlobal.ConceptProp();
        oAnnotation.oAnnotated[0].oConcepts[0].sName = MetadataConstants.c_XMLE_issueDescriptionConcepts;
        oAnnotation.oAnnotated[0].oConcepts[0].sUri = "http://www.alert-project.eu/ontologies/alert_its.owl#Bug1";
        oAnnotation.oAnnotated[0].oConcepts[0].sWeight = "1";
        oAnnotation.oAnnotated[1].oConcepts = new MetadataGlobal.ConceptProp[1];
        oAnnotation.oAnnotated[1].oConcepts[0] = new MetadataGlobal.ConceptProp();
        oAnnotation.oAnnotated[1].oConcepts[0].sName = MetadataConstants.c_XMLE_commentTextConcepts;
        oAnnotation.oAnnotated[1].oConcepts[0].sUri = "http://www.alert-project.eu/ontologies/alert_its.owl#Bug1";
        oAnnotation.oAnnotated[1].oConcepts[0].sWeight = "1";
        AnnotationData result = MetadataRDFConverter.SaveAnnotationData(oAnnotation);
        assertNotNull(result);
        assertNotNull(result.oAnnotated);
        assertNotNull(result.oAnnotated[0].oConcepts);
        assertNotNull(result.oAnnotated[1].oConcepts);
        assertEquals(result.oAnnotated[0].m_sObjectURI.isEmpty(), false);
        assertEquals(result.oAnnotated[1].m_sObjectURI.isEmpty(), false);
        assertEquals(result.oAnnotated[0].oConcepts[0].m_sObjectURI.isEmpty(), false);
        assertEquals(result.oAnnotated[1].oConcepts[0].m_sObjectURI.isEmpty(), false);
        //// TO-DO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of SaveForumPost method, of class MetadataRDFConverter.
     */
    @Test
    public void testSaveForumPost() {
        System.out.println("* MetadataRDFConverterTest: SaveForumPost");
        ForumPost oForumPostNull = null;
        ForumPost expResult = null;
        ForumPost resultNull = MetadataRDFConverter.SaveForumPost(oForumPostNull);
        assertEquals(expResult, resultNull);
        
        ForumPost oForumPost = new ForumPost();
        oForumPost.m_sForumItemID = "3557";
        oForumPost.m_oInForumThread = new ForumThread();
        oForumPost.m_oInForumThread.m_sID = "3559";
        oForumPost.m_oInForumThread.m_oInForum = new Forum();
        oForumPost.m_oInForumThread.m_oInForum.m_sID = "3558";
        oForumPost.m_sID = "3560";
        oForumPost.m_dtmTime = new Date(112, 0, 16, 16, 31);
        oForumPost.m_sSubject = "This is a test subject for post";
        oForumPost.m_sBody = "This is a test body for post";
        oForumPost.m_oAuthor = new foaf_Person();
        oForumPost.m_oAuthor.m_sFirstName = "Angel";
        oForumPost.m_oAuthor.m_sLastName = "Blue";
        oForumPost.m_oAuthor.m_sID = "angel_blue_co2004@yahoo.com";
        oForumPost.m_oAuthor.m_sEmail = "angel_blue_co2004@yahoo.com";
        oForumPost.m_sCategory = "xxxxxx";
        ForumPost result = MetadataRDFConverter.SaveForumPost(oForumPost);
        assertNotNull(result);
        assertEquals(result.m_sObjectURI.isEmpty(), false);
        assertEquals(result.m_oInForumThread.m_oInForum.m_sObjectURI.isEmpty(), false);
        assertEquals(result.m_oInForumThread.m_sObjectURI.isEmpty(), false);
        //assertEquals(result.m_oHasAuthor.m_sObjectURI.isEmpty(), false);
        //assertEquals(expResult, result);
        
        //// TO-DO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of ac_issue_getAllForProduct method, of class MetadataRDFConverter.
     */
    @Test
    public void testAc_issue_getAllForProduct() {
        System.out.println("* MetadataRDFConverterTest: ac_issue_getAllForProduct");
        String sProductUriEmpty = "";
        Date dtmFromDate = new Date();
        //APIResponseData expResult = null;
        APIResponseData resultNull = MetadataRDFConverter.ac_issue_getAllForProduct(sProductUriEmpty, dtmFromDate);
        //assertEquals(expResult, resultNull);
        assertNotNull(resultNull);
        assertNotNull(resultNull.oData);
        assertEquals(resultNull.oData.size(), 0);
        
        String sProductUri = "http://www.ifi.uzh.ch/ddis/evoont/2008/11/bom#Product1";
        APIResponseData result = MetadataRDFConverter.ac_issue_getAllForProduct(sProductUri, dtmFromDate);
        assertNotNull(result);
        assertNotNull(result.oData);
        assertEquals(result.oData.size() > 0, true);
        //assertEquals(expResult, result);
        
        //// TO-DO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of ac_issue_getAllForMethod method, of class MetadataRDFConverter.
     */
    @Test
    public void testAc_issue_getAllForMethod() {
        System.out.println("* MetadataRDFConverterTest: ac_issue_getAllForMethod");
        ArrayList<String> sMethodUriNull = null;
        //APIResponseData expResult = null;
        APIResponseData resultNull = MetadataRDFConverter.ac_issue_getAllForMethod(sMethodUriNull);
        //assertEquals(expResult, resultNull);
        assertNotNull(resultNull);
        assertNotNull(resultNull.oData);
        assertEquals(resultNull.oData.size(), 0);
        
        ArrayList<String> sMethodUriString = new ArrayList<String>();
        sMethodUriString.add(null);
        APIResponseData resultStringNull = MetadataRDFConverter.ac_issue_getAllForMethod(sMethodUriString);
        //assertEquals(expResult, resultStringNull);
        assertNotNull(resultStringNull);
        assertNotNull(resultStringNull.oData);
        assertEquals(resultStringNull.oData.size(), 0);
        
        ArrayList<String> sMethodUriEmpty = new ArrayList<String>();
        sMethodUriEmpty.add("");
        APIResponseData resultStringEmpty = MetadataRDFConverter.ac_issue_getAllForMethod(sMethodUriEmpty);
        //assertEquals(expResult, resultStringEmpty);
        assertNotNull(resultStringEmpty);
        assertNotNull(resultStringEmpty.oData);
        assertEquals(resultStringEmpty.oData.size(), 0);
        
        ArrayList<String> sMethodUri = new ArrayList<String>();
        sMethodUri.add("http://www.alert-project.eu/ontologies/alert.owl#Method1");
        sMethodUri.add("http://www.alert-project.eu/ontologies/alert.owl#Method2");
        sMethodUri.add("http://www.alert-project.eu/ontologies/alert.owl#Method3");
        APIResponseData result = MetadataRDFConverter.ac_issue_getAllForMethod(sMethodUri);
        assertNotNull(result);
        assertNotNull(result.oData);
        assertEquals(result.oData.size() > 0, true);
        //assertEquals(expResult, result);
        
        //// TO-DO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of ac_issue_getAnnotationStatus method, of class MetadataRDFConverter.
     */
    @Test
    public void testAc_issue_getAnnotationStatus() {
        System.out.println("* MetadataRDFConverterTest: ac_issue_getAnnotationStatus");
        String sIssueUriNull = "";
        //APIResponseData expResult = null;
        APIResponseData resultNull = MetadataRDFConverter.ac_issue_getAnnotationStatus(sIssueUriNull);
        //assertEquals(expResult, resultNull);
        assertNotNull(resultNull);
        assertNotNull(resultNull.oData);
        assertEquals(resultNull.oData.size(), 0);
        
        String sIssueUri = "http://www.alert-project.eu/ontologies/alert_its.owl#Bug1";
        APIResponseData result = MetadataRDFConverter.ac_issue_getAnnotationStatus(sIssueUri);
        assertNotNull(result);
        assertNotNull(result.oData);
        assertEquals(result.oData.size() > 0, true);
        //assertEquals(expResult, result);
        
        //// TO-DO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of ac_issue_getInfo method, of class MetadataRDFConverter.
     */
    @Test
    public void testAc_issue_getInfo() {
        System.out.println("* MetadataRDFConverterTest: ac_issue_getInfo");
        String sIssueUriNull = "";
        //APIResponseData expResult = null;
        APIResponseData resultNull = MetadataRDFConverter.ac_issue_getInfo(sIssueUriNull);
        //assertEquals(expResult, resultNull);
        assertNotNull(resultNull);
        assertNotNull(resultNull.oData);
        assertEquals(resultNull.oData.size(), 0);
        
        String sIssueUri = "http://www.alert-project.eu/ontologies/alert_its.owl#Bug1";
        APIResponseData result = MetadataRDFConverter.ac_issue_getInfo(sIssueUri);
        assertNotNull(result);
        assertNotNull(result.oData);
        assertEquals(result.oData.size() > 0, true);
        
        //// TO-DO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of ac_issue_getExplicitDuplicates method, of class MetadataRDFConverter.
     */
    @Test
    public void testAc_issue_getDuplicates() {
        System.out.println("* MetadataRDFConverterTest: ac_issue_getDuplicates");
        String sIssueDuplicatesSPARQLNull = "";
        //APIResponseData expResult = null;
        APIResponseData resultNull = MetadataRDFConverter.ac_issue_getExplicitDuplicates(sIssueDuplicatesSPARQLNull);
        //assertEquals(expResult, resultNull);
        assertNotNull(resultNull);
        assertNotNull(resultNull.oData);
        assertEquals(resultNull.oData.size(), 0);
        
        String sIssueDuplicatesSPARQL = " ?issueUri <http://www.ifi.uzh.ch/ddis/evoont/2008/11/bom#keyword> ?keyword . <http://www.alert-project.eu/ontologies/alert_its.owl#Bug1> <http://www.ifi.uzh.ch/ddis/evoont/2008/11/bom#keyword> ?keyword ";
        APIResponseData result = MetadataRDFConverter.ac_issue_getExplicitDuplicates(sIssueDuplicatesSPARQL);
        assertNotNull(result);
        assertNotNull(result.oData);
        assertEquals(result.oData.size() > 0, true);
        
        //// TO-DO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of ac_person_getInfo method, of class MetadataRDFConverter.
     */
    @Test
    public void testAc_person_getInfo() {
        System.out.println("* MetadataRDFConverterTest: ac_person_getInfo");
        String sPersonUriNull = "";
        //APIResponseData expResult = null;
        APIResponseData resultNull = MetadataRDFConverter.ac_person_getInfo(sPersonUriNull);
        //assertEquals(expResult, resultNull);
        assertNotNull(resultNull);
        assertNotNull(resultNull.oData);
        assertEquals(resultNull.oData.size(), 0);
        
        String sPersonUri = "http://www.alert-project.eu/ontologies/alert_scm.owl#Person5";
        APIResponseData result = MetadataRDFConverter.ac_person_getInfo(sPersonUri);
        assertNotNull(result);
        assertNotNull(result.oData);
        assertEquals(result.oData.size() > 0, true);
        
        //// TO-DO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of ac_person_getAllForEmail method, of class MetadataRDFConverter.
     */
    @Test
    public void testAc_person_getAllForEmail() {
        System.out.println("* MetadataRDFConverterTest: ac_person_getAllForEmail");
        String sEmailNull = "";
        //APIResponseData expResult = null;
        APIResponseData resultNull = MetadataRDFConverter.ac_person_getAllForEmail(sEmailNull);
        //assertEquals(expResult, resultNull);
        assertNotNull(resultNull);
        assertNotNull(resultNull.oData);
        assertEquals(resultNull.oData.size(), 0);
        
        String sEmail = "sasa.stojanovic@cimcollege.rs";
        APIResponseData result = MetadataRDFConverter.ac_person_getAllForEmail(sEmail);
        assertNotNull(result);
        assertNotNull(result.oData);
        assertEquals(result.oData.size() > 0, true);
        
        //// TO-DO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of ac_identity_getForPerson method, of class MetadataRDFConverter.
     */
    @Test
    public void testAc_identity_getForPerson() {
        System.out.println("* MetadataRDFConverterTest: ac_identity_getForPerson");
        String sPersonUriNull = "";
        //APIResponseData expResult = null;
        APIResponseData resultNull = MetadataRDFConverter.ac_identity_getForPerson(sPersonUriNull);
        //assertEquals(expResult, resultNull);
        assertNotNull(resultNull);
        assertNotNull(resultNull.oData);
        assertEquals(resultNull.oData.size(), 0);
        
        String sPersonUri = "http://www.alert-project.eu/ontologies/alert_scm.owl#Person1";
        APIResponseData result = MetadataRDFConverter.ac_identity_getForPerson(sPersonUri);
        assertNotNull(result);
        assertNotNull(result.oData);
        assertEquals(result.oData.size() > 0, true);
        
        //// TO-DO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of ac_competency_getPersonForIssue method, of class MetadataRDFConverter.
     */
    @Test
    public void testAc_competency_getPersonForIssue() {
        System.out.println("* MetadataRDFConverterTest: ac_competency_getPersonForIssue");
        String sPersonForIssueSPARQLNull = "";
        //APIResponseData expResult = null;
        APIResponseData resultNull = MetadataRDFConverter.ac_competency_getPersonForIssue(sPersonForIssueSPARQLNull);
        //assertEquals(expResult, resultNull);
        assertNotNull(resultNull);
        assertNotNull(resultNull.oData);
        assertEquals(resultNull.oData.size(), 0);
        
        String sPersonForIssueSPARQL = " <http://www.alert-project.eu/ontologies/alert_its.owl#Bug1> <http://www.ifi.uzh.ch/ddis/evoont/2008/11/bom#hasAssignee> ?personUri ";
        APIResponseData result = MetadataRDFConverter.ac_competency_getPersonForIssue(sPersonForIssueSPARQL);
        assertNotNull(result);
        assertNotNull(result.oData);
        assertEquals(result.oData.size() > 0, true);
        
        //// TO-DO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of ac_method_getAllForIdentity method, of class MetadataRDFConverter.
     */
    @Test
    public void testAc_method_getAllForPerson() {
        System.out.println("* MetadataRDFConverterTest: ac_method_getAllForPerson");
        String sPersonUriNull = "";
        //APIResponseData expResult = null;
        APIResponseData resultNull = MetadataRDFConverter.ac_method_getAllForIdentity(sPersonUriNull);
        //assertEquals(expResult, resultNull);
        assertNotNull(resultNull);
        assertNotNull(resultNull.oData);
        assertEquals(resultNull.oData.size(), 0);
        
        String sPersonUri = "http://www.alert-project.eu/ontologies/alert_scm.owl#Person1";
        APIResponseData result = MetadataRDFConverter.ac_method_getAllForIdentity(sPersonUri);
        assertNotNull(result);
        assertNotNull(result.oData);
        assertEquals(result.oData.size() > 0, true);
        
        //// TO-DO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of ac_method_getRelatedCode method, of class MetadataRDFConverter.
     */
    @Test
    public void testAc_method_getRelatedCode() {
        System.out.println("* MetadataRDFConverterTest: ac_method_getRelatedCode");
        String sPersonUriNull = "";
        String sProductUriNull = "";
        //APIResponseData expResult = null;
        APIResponseData resultNull = MetadataRDFConverter.ac_method_getRelatedCode(sPersonUriNull, sProductUriNull);
        //assertEquals(expResult, resultNull);
        assertNotNull(resultNull);
        assertNotNull(resultNull.oData);
        assertEquals(resultNull.oData.size(), 0);
        
        String sPersonUri = "http://www.alert-project.eu/ontologies/alert_scm.owl#Person1";
        String sProductUri = "http://www.ifi.uzh.ch/ddis/evoont/2008/11/bom#Product1";
        APIResponseData result = MetadataRDFConverter.ac_method_getRelatedCode(sPersonUri, sProductUri);
        assertNotNull(result);
        assertNotNull(result.oData);
        assertEquals(result.oData.size() > 0, true);
        
        //// TO-DO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of ac_issue_getRelatedToKeyword method, of class MetadataRDFConverter.
     */
    @Test
    public void testAc_issue_getRelatedToKeyword() {
        System.out.println("* MetadataRDFConverterTest: ac_issue_getRelatedToKeyword");
        String sKeywordNull = "";
        //APIResponseData expResult = null;
        APIResponseData resultNull = MetadataRDFConverter.ac_issue_getRelatedToKeyword(sKeywordNull);
        //assertEquals(expResult, resultNull);
        assertNotNull(resultNull);
        assertNotNull(resultNull.oData);
        assertEquals(resultNull.oData.size(), 0);
        
        String sKeyword = "desc";
        APIResponseData result = MetadataRDFConverter.ac_issue_getRelatedToKeyword(sKeyword);
        assertNotNull(result);
        assertNotNull(result.oData);
        assertEquals(result.oData.size() > 0, true);
        
        //// TO-DO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of ac_commit_getRelatedToKeyword method, of class MetadataRDFConverter.
     */
    @Test
    public void testAc_commit_getRelatedToKeyword() {
        System.out.println("* MetadataRDFConverterTest: ac_commit_getRelatedToKeyword");
        String sKeywordNull = "";
        //APIResponseData expResult = null;
        APIResponseData resultNull = MetadataRDFConverter.ac_commit_getRelatedToKeyword(sKeywordNull);
        //assertEquals(expResult, resultNull);
        assertNotNull(resultNull);
        assertNotNull(resultNull.oData);
        assertEquals(resultNull.oData.size(), 0);
        
        String sKeyword = "zajednicka";
        APIResponseData result = MetadataRDFConverter.ac_commit_getRelatedToKeyword(sKeyword);
        assertNotNull(result);
        assertNotNull(result.oData);
        assertEquals(result.oData.size() > 0, true);
        
        //// TO-DO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of ac_email_getRelatedToKeyword method, of class MetadataRDFConverter.
     */
    @Test
    public void testAc_email_getRelatedToKeyword() {
        System.out.println("* MetadataRDFConverterTest: ac_email_getRelatedToKeyword");
        String sKeywordNull = "";
        //APIResponseData expResult = null;
        APIResponseData resultNull = MetadataRDFConverter.ac_email_getRelatedToKeyword(sKeywordNull);
        //assertEquals(expResult, resultNull);
        assertNotNull(resultNull);
        assertNotNull(resultNull.oData);
        assertEquals(resultNull.oData.size(), 0);
        
        String sKeyword = "test";
        APIResponseData result = MetadataRDFConverter.ac_email_getRelatedToKeyword(sKeyword);
        assertNotNull(result);
        assertNotNull(result.oData);
        assertEquals(result.oData.size() > 0, true);
        
        //// TO-DO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of ac_post_getRelatedToKeyword method, of class MetadataRDFConverter.
     */
    @Test
    public void testAc_post_getRelatedToKeyword() {
        System.out.println("* MetadataRDFConverterTest: ac_post_getRelatedToKeyword");
        String sKeywordNull = "";
        //APIResponseData expResult = null;
        APIResponseData resultNull = MetadataRDFConverter.ac_post_getRelatedToKeyword(sKeywordNull);
        //assertEquals(expResult, resultNull);
        assertNotNull(resultNull);
        assertNotNull(resultNull.oData);
        assertEquals(resultNull.oData.size(), 0);
        
        String sKeyword = "test";
        APIResponseData result = MetadataRDFConverter.ac_post_getRelatedToKeyword(sKeyword);
        assertNotNull(result);
        assertNotNull(result.oData);
        assertEquals(result.oData.size() > 0, true);
        
        //// TO-DO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of ac_wiki_getRelatedToKeyword method, of class MetadataRDFConverter.
     */
    @Test
    public void testAc_wiki_getRelatedToKeyword() {
        System.out.println("* MetadataRDFConverterTest: ac_wiki_getRelatedToKeyword");
        String sKeywordNull = "";
        //APIResponseData expResult = null;
        APIResponseData resultNull = MetadataRDFConverter.ac_wiki_getRelatedToKeyword(sKeywordNull);
        //assertEquals(expResult, resultNull);
        assertNotNull(resultNull);
        assertNotNull(resultNull.oData);
        assertEquals(resultNull.oData.size(), 0);
        
        String sKeyword = "test";
        APIResponseData result = MetadataRDFConverter.ac_wiki_getRelatedToKeyword(sKeyword);
        assertNotNull(result);
        assertNotNull(result.oData);
        assertEquals(result.oData.size() > 0, true);
        
        //// TO-DO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of ac_issue_getRelatedToIssue method, of class MetadataRDFConverter.
     */
    @Test
    public void testAc_issue_getRelatedToIssue() {
        System.out.println("* MetadataRDFConverterTest: ac_issue_getRelatedToIssue");
        String sIssueUriNull = "";
        //APIResponseData expResult = null;
        APIResponseData resultNull = MetadataRDFConverter.ac_issue_getRelatedToIssue(sIssueUriNull);
        //assertEquals(expResult, resultNull);
        assertNotNull(resultNull);
        assertNotNull(resultNull.oData);
        assertEquals(resultNull.oData.size(), 0);
        
        String sIssueUri = "http://www.alert-project.eu/ontologies/alert_its.owl#Bug1";
        APIResponseData result = MetadataRDFConverter.ac_issue_getRelatedToIssue(sIssueUri);
        assertNotNull(result);
        assertNotNull(result.oData);
        assertEquals(result.oData.size() > 0, true);
        
        //// TO-DO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of ac_commit_getRelatedToIssue method, of class MetadataRDFConverter.
     */
    @Test
    public void testAc_commit_getRelatedToIssue() {
        System.out.println("* MetadataRDFConverterTest: ac_commit_getRelatedToIssue");
        String sIssueUriNull = "";
        //APIResponseData expResult = null;
        APIResponseData resultNull = MetadataRDFConverter.ac_commit_getRelatedToIssue(sIssueUriNull);
        //assertEquals(expResult, resultNull);
        assertNotNull(resultNull);
        assertNotNull(resultNull.oData);
        assertEquals(resultNull.oData.size(), 0);
        
        String sIssueUri = "http://www.alert-project.eu/ontologies/alert_its.owl#Bug1";
        APIResponseData result = MetadataRDFConverter.ac_commit_getRelatedToIssue(sIssueUri);
        assertNotNull(result);
        assertNotNull(result.oData);
        assertEquals(result.oData.size() > 0, true);
        
        //// TO-DO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of ac_email_getRelatedToIssue method, of class MetadataRDFConverter.
     */
    @Test
    public void testAc_email_getRelatedToIssue() {
        System.out.println("* MetadataRDFConverterTest: ac_email_getRelatedToIssue");
        String sIssueUriNull = "";
        //APIResponseData expResult = null;
        APIResponseData resultNull = MetadataRDFConverter.ac_email_getRelatedToIssue(sIssueUriNull);
        //assertEquals(expResult, resultNull);
        assertNotNull(resultNull);
        assertNotNull(resultNull.oData);
        assertEquals(resultNull.oData.size(), 0);
        
        String sIssueUri = "http://www.alert-project.eu/ontologies/alert_its.owl#Bug1";
        APIResponseData result = MetadataRDFConverter.ac_email_getRelatedToIssue(sIssueUri);
        assertNotNull(result);
        assertNotNull(result.oData);
        assertEquals(result.oData.size() > 0, true);
        
        //// TO-DO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of ac_post_getRelatedToIssue method, of class MetadataRDFConverter.
     */
    @Test
    public void testAc_post_getRelatedToIssue() {
        System.out.println("* MetadataRDFConverterTest: ac_post_getRelatedToIssue");
        String sIssueUriNull = "";
        //APIResponseData expResult = null;
        APIResponseData resultNull = MetadataRDFConverter.ac_post_getRelatedToIssue(sIssueUriNull);
        //assertEquals(expResult, resultNull);
        assertNotNull(resultNull);
        assertNotNull(resultNull.oData);
        assertEquals(resultNull.oData.size(), 0);
        
        String sIssueUri = "http://www.alert-project.eu/ontologies/alert_its.owl#Bug1";
        APIResponseData result = MetadataRDFConverter.ac_post_getRelatedToIssue(sIssueUri);
        assertNotNull(result);
        assertNotNull(result.oData);
        assertEquals(result.oData.size() > 0, true);
        
        //// TO-DO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of ac_wiki_getRelatedToIssue method, of class MetadataRDFConverter.
     */
    @Test
    public void testAc_wiki_getRelatedToIssue() {
        System.out.println("* MetadataRDFConverterTest: ac_wiki_getRelatedToIssue");
        String sIssueUriNull = "";
        //APIResponseData expResult = null;
        APIResponseData resultNull = MetadataRDFConverter.ac_wiki_getRelatedToIssue(sIssueUriNull);
        //assertEquals(expResult, resultNull);
        assertNotNull(resultNull);
        assertNotNull(resultNull.oData);
        assertEquals(resultNull.oData.size(), 0);
        
        String sIssueUri = "http://www.alert-project.eu/ontologies/alert_its.owl#Bug1";
        APIResponseData result = MetadataRDFConverter.ac_wiki_getRelatedToIssue(sIssueUri);
        assertNotNull(result);
        assertNotNull(result.oData);
        assertEquals(result.oData.size() > 0, true);
        
        //// TO-DO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of ac_sparql method, of class MetadataRDFConverter.
     */
    @Test
    public void testAc_sparql() {
        System.out.println("* MetadataRDFConverterTest: ac_sparql");
        String sSPARQLNull = "";
        OntModelSpec oOntModelSpecNull = null;
        //APIResponseData expResult = null;
        APIResponseData resultNull = MetadataRDFConverter.ac_sparql(sSPARQLNull, oOntModelSpecNull);
        //assertEquals(expResult, resultNull);
        assertNotNull(resultNull);
        assertNotNull(resultNull.oData);
        assertEquals(resultNull.oData.size(), 0);
        
        String sSPARQL = "SELECT ?issueUri ?issueId ?issueDescription WHERE {?issueUri <http://www.ifi.uzh.ch/ddis/evoont/2008/11/bom#isIssueOf> ?componentUri . ?componentUri <http://www.ifi.uzh.ch/ddis/evoont/2008/11/bom#isComponentOf> <http://www.ifi.uzh.ch/ddis/evoont/2008/11/bom#Product1> . ?issueUri <http://www.alert-project.eu/ontologies/alert.owl#ID> ?issueId . ?issueUri <http://www.ifi.uzh.ch/ddis/evoont/2008/11/bom#description> ?issueDescription}";
        OntModelSpec oOntModelSpec = OntModelSpec.OWL_MEM_RULE_INF;
        APIResponseData result = MetadataRDFConverter.ac_sparql(sSPARQL, oOntModelSpec);
        assertNotNull(result);
        assertNotNull(result.oData);
        assertEquals(result.oData.size() > 0, true);
        
        //// TO-DO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of GetAllMembers method, of class MetadataRDFConverter.
     */
    @Test
    public void testGetAllMembers() {
        System.out.println("* MetadataRDFConverterTest: GetAllMembers");
        String sOntClassNull = "";
        //APIResponseData expResult = null;
        APIResponseData resultNull = MetadataRDFConverter.GetAllMembers(sOntClassNull);
        //assertEquals(expResult, resultNull);
        assertNotNull(resultNull);
        assertNotNull(resultNull.oData);
        assertEquals(resultNull.oData.size(), 0);
        
        String sOntClass = MetadataConstants.c_NS_Alert + MetadataConstants.c_OWLClass_Bug;
        APIResponseData result = MetadataRDFConverter.GetAllMembers(sOntClass);
        assertNotNull(result);
        assertNotNull(result.oData);
        assertEquals(result.oData.size() > 0, true);
        
        //// TO-DO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of GetMember method, of class MetadataRDFConverter.
     */
    @Test
    public void testGetMember() {
        System.out.println("* MetadataRDFConverterTest: GetMember");
        String sMemberURLNull = "";
        //OntoProperty expResult = null;
        OntoProperty resultNull = MetadataRDFConverter.GetMember(sMemberURLNull);
        //assertEquals(expResult, resultNull);
        assertNotNull(resultNull);
        assertNotNull(resultNull.oProperties);
        assertEquals(resultNull.oProperties.size(), 0);
                
        String sMemberURL = "http://www.alert-project.eu/ontologies/alert_its.owl#Bug1";
        OntoProperty result = MetadataRDFConverter.GetMember(sMemberURL);
        assertNotNull(result);
        assertNotNull(result.oProperties);
        assertEquals(result.oProperties.size() > 0, true);
        
        //// TO-DO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
}
