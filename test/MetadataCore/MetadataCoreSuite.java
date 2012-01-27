/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MetadataCore;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author Dejan
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({MetadataCore.MetadataObjectFactoryTest.class, 
                     MetadataCore.MetadataGlobalTest.class,
                     MetadataCore.MetadataRDFConverterTest.class, 
                     MetadataCore.MetadataXMLCreatorTest.class, 
                     MetadataCore.MetadataXMLReaderTest.class, 
                     MetadataCore.MetadataCommunicatorTest.class, 
                     MetadataCore.MetadataModelTest.class})
public class MetadataCoreSuite {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }
    
}
