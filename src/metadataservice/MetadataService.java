/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package metadataservice;

import ActiveMQ.SimpleTopicSubscriber;
import MetadataCore.MetadataGlobal;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author Dejan
 */
public class MetadataService {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        //// TO-DO code application logic here
        MetadataGlobal.ReadConfiguration();
        MetadataGlobal.LoadOntology();
        MetadataGlobal.ExpandOntology();
        MetadataGlobal.StartBackupProcedure();
        SimpleTopicSubscriber.main();
        MetadataGlobal.StartSaveProcedure();
    }
}
