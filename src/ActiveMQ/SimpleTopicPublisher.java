/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ActiveMQ;

import MetadataCore.MetadataConstants;
import javax.jms.*;
import javax.naming.*;
import java.util.*;
import org.apache.activemq.jndi.*;
import java.sql.Timestamp;


/**
 *
 * @author dusan.marjanovic
 */
public class SimpleTopicPublisher {
    
 
    public static void publish(String topicName, String xml){
        
        Context jndiContext = null;
        TopicConnectionFactory topicConnectionFactory = null;
        TopicConnection topicConnection = null;
        TopicSession topicSession = null;
        Topic topic = null;
        TopicPublisher topicPublisher = null;
        TextMessage message = null;
        final int NUM_MSGS = 1;
        
//  *  Create a JNDI API InitialContext object if none exists yet.
        
        try {
            Properties env = new Properties( );
            env.setProperty(Context.INITIAL_CONTEXT_FACTORY,"org.apache.activemq.jndi.ActiveMQInitialContextFactory");
            env.setProperty(Context.PROVIDER_URL, MetadataConstants.sActiveMQAddress);
            env.setProperty("topic." + topicName, topicName);
         
                    
            jndiContext = new InitialContext(env);
            }catch (NamingException e){
                System.out.println("Could not create JNDI API " + "context: " + e.toString());
                e.printStackTrace();
                System.exit(1);
            }
        
//  *  Look up connection factory and topic. If either does not exist, exit.
        
        try {


            topicConnectionFactory = (TopicConnectionFactory) jndiContext.lookup("TopicConnectionFactory");
            topic = (Topic) jndiContext.lookup(topicName);
            }catch (NamingException e) {
                System.out.println("JNDI API lookup failed: " +  e.toString());
                e.printStackTrace();
                System.exit(1);
            }
/*
    * Create connection.
    * Create session from connection; false means session is not transacted.
    * Create publisher and text message.
    * Send messages, varying text slightly.
    * Finally, close connection.
*/
        boolean bEventSend = false;
        while (!bEventSend)
        {
            try {
                topicConnection = topicConnectionFactory.createTopicConnection();
                topicSession = topicConnection.createTopicSession(false,Session.AUTO_ACKNOWLEDGE);
                topicPublisher = topicSession.createPublisher(topic);
                message = topicSession.createTextMessage();
                for (int i = 0; i < NUM_MSGS; i++) {
                    message.setText(xml);
                    topicPublisher.publish(message);
                    java.util.Date date= new java.util.Date();
                    System.out.println("Publishing message at: " + new Timestamp(date.getTime()));
                    bEventSend = true;
                }
            } catch (JMSException e){
                System.out.println("Exception occurred: " + e.toString());
                System.out.println("Resending event...");
            } finally {
                if(topicConnection != null){
                    try{
                        topicConnection.close();
                    }catch (JMSException e){}
                }
            }
        }
    }
}
