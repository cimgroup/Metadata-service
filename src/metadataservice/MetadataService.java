/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package metadataservice;

import ActiveMQ.SimpleTopicSubscriber;
import MetadataCore.MetadataGlobal;

/**
 *
 * @author Dejan
 */
public class MetadataService {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //// TO-DO code application logic here
        MetadataGlobal.ExpandOntology();
        SimpleTopicSubscriber.main();
    }
}
