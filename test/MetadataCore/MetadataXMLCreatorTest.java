/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MetadataCore;

import MetadataCore.MetadataGlobal.APIResponseData;
import MetadataCore.MetadataGlobal.OntoProperty;
import MetadataObjects.*;
import java.util.ArrayList;
import java.util.Date;
import org.junit.*;
import static org.junit.Assert.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
     * Creates example Isue.
     * @startRealisation  Dejan Milosavljevic 24.01.2012.
     * @finalModification Dejan Milosavljevic 24.01.2012.
     * @return 
     */
    private Issue GetIssueExample() {
        Issue oIssue = new Issue();
        oIssue.m_sID = "184671";
        
        oIssue.m_sObjectURI = "http://www.alert-project.eu/ontologies/alert_its.owl#Bug1";
        oIssue.m_sReturnConfig = "YY#s:" + MetadataConstants.c_XMLE_issue + "/s:" + MetadataConstants.c_XMLE_issue + MetadataConstants.c_XMLE_Uri;
        
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
     * @summary Method for returning tag value of element
     * @startRealisation Sasa Stojanovic 23.06.2011.
     * @finalModification Sasa Stojanovic 23.06.2011.
     * @param eElement - element to read
     * @param sTag - tag to read
     * @return value of element
     */
    private String GetValue(Element eElement, String sTag)
    {
        String sElementName = "";

        NodeList nlName = eElement.getElementsByTagName(sTag);
	if (nlName != null && nlName.getLength() > 0)
        {
            Node nElement = (Node) eElement;
            Node nValue = (Node) nlName.item(0);
            if (ChildNode(nElement, nValue))
            {
                Element el = (Element)nlName.item(0);
                if (el.hasChildNodes())
                    sElementName = el.getFirstChild().getNodeValue();
            }
	}

        return sElementName;
    }
    
    /**
     * @summary Method for cheking if the node is the first level child of other node
     * @startRealisation Sasa Stojanovic 23.06.2011.
     * @finalModification Sasa Stojanovic 23.06.2011.
     * @param nParent - parent node
     * @param nChild - child node
     * @return true if node is first level child of other node
     */
    private static boolean ChildNode(Node nParent, Node nChild)
    {
        boolean bIsChild = false;
        try
        {
            NodeList nlChilds = nParent.getChildNodes();
            for (int i = 0; i < nlChilds.getLength(); i++)
            {
                Node nTestChild = nlChilds.item(i);
                if (nTestChild.equals(nChild))
                    bIsChild = true;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return bIsChild;
    }
    
    /**
     * Test of CreateXMLAPIResponse method, of class MetadataXMLCreator. 
     */
    @Test
    public void testCreateXMLAPIResponse() {
        System.out.println("* MetadataXMLCreatorTest: CreateXMLAPIResponse");
        String sAPICallNull = "";
        String sEventIdNull = "";
        APIResponseData oDataNull = null;
        Document resultNull = MetadataXMLCreator.CreateXMLAPIResponse(sAPICallNull, sEventIdNull, oDataNull);
        assertNotNull(resultNull);
        
        //issue.getInfo
        String sAPICallEmpty = "";
        String sEventIdEmpty = "";
        APIResponseData oDataEmpty = new APIResponseData();
        Document resultEmpty = MetadataXMLCreator.CreateXMLAPIResponse(sAPICallEmpty, sEventIdEmpty, oDataEmpty);
        assertNotNull(resultEmpty);
        NodeList nlIssueNull = resultEmpty.getElementsByTagName("s:" + MetadataConstants.c_XMLE_issue);
        assertNotNull(nlIssueNull);
        assertEquals(nlIssueNull.getLength(), 0);
        //Element eIssueNull = (Element) nlIssueNull.item(0);
        //String sIssueIDNull = GetValue(eIssueNull, "s:" + "issueId");
        //assertEquals(sIssueIDNull.isEmpty(), true);
        
        String sAPICall = MetadataConstants.c_XMLAC_issue_getInfo;
        String sEventId = "9856";
        APIResponseData oData = MetadataRDFConverter.ac_issue_getInfo("http://www.alert-project.eu/ontologies/alert_its.owl#Bug1");
        Document result = MetadataXMLCreator.CreateXMLAPIResponse(sAPICall, sEventId, oData);
        assertNotNull(result);
        NodeList nlIssue = result.getElementsByTagName("s:" + MetadataConstants.c_XMLE_issue);
        assertNotNull(nlIssue);
        Element eIssue = (Element) nlIssue.item(0);
        String sIssueID = GetValue(eIssue, "s:" + "issueId");
        assertEquals(sIssueID.isEmpty(), true);
        
        //// TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
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
    @Test
    public void testCreateXMLNewItemResponse() {
        System.out.println("* MetadataXMLCreatorTest: CreateXMLNewItemResponse");
        String sEventNameNull = "";
        String sEventIdNull = "";
        Object oObjectNull = null;
        Document resultNull = MetadataXMLCreator.CreateXMLNewItemResponse(sEventNameNull, sEventIdNull, null, oObjectNull);
        assertNotNull(resultNull);
        
        //Issue
        String sEventNameEmpty = MetadataConstants.c_ET_ALERT_Metadata_IssueNew_Stored;
        String sEventIdEmpty = "5748";
        Issue oObjectEmpty = new Issue();
        Document resultEmpty = MetadataXMLCreator.CreateXMLNewItemResponse(sEventNameEmpty, sEventIdEmpty, null, oObjectEmpty);
        assertNotNull(resultEmpty);
        NodeList nlIssueNull = resultEmpty.getElementsByTagName("s:" + MetadataConstants.c_XMLE_issue);
        assertNotNull(nlIssueNull);
        assertEquals(nlIssueNull.getLength(), 0);
        
        String sEventName = MetadataConstants.c_ET_ALERT_Metadata_IssueNew_Stored;
        String sEventId = "5748";
        Issue oObject = GetIssueExample();
        Document result = MetadataXMLCreator.CreateXMLNewItemResponse(sEventName, sEventId, null, oObject);
        assertNotNull(result);
        NodeList nlIssue = result.getElementsByTagName("s:" + MetadataConstants.c_XMLE_issue);
        assertNotNull(nlIssue);
        Element eIssue = (Element) nlIssue.item(0);
        String sIssueUri = GetValue(eIssue, "s:" + "issueUri");
        assertEquals(sIssueUri.isEmpty(), false);
        
        //// TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
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
        //MetadataXMLCreator.CreateXMLInstanceResponse(sEventId, oProperty);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
