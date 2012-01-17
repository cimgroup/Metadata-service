/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MetadataCore;


import javax.jms.*;
import javax.naming.*;
import java.util.*;
//import org.apache.activemq.jndi.*;
/**
 *
 * @author dusan.marjanovic
 */
public class SimpleTopicPublisher {
  
    public static void main(String[] args){
        String topicName = null;
        Context jndiContext = null;
        TopicConnectionFactory topicConnectionFactory = null;
        TopicConnection topicConnection = null;
        TopicSession topicSession = null;
        Topic topic = null;
        TopicPublisher topicPublisher = null;
        TextMessage message = null;
        final int NUM_MSGS;
        
        //if((args.length<1) || (args.length > 2)){
        //    System.out.println("Usage: java " + "SimpleTopicPublisher <topic-name> " + "[<number-of-messages>]");
        //    System.exit(1);
        //}
        //topicName = new String(args[0]);
        topicName = "Test_Topic";
        System.out.println("Topic name is " + topicName);
        if (args.length == 2){
            NUM_MSGS = (new Integer(args[1])).intValue();
        } else {
            NUM_MSGS = 1;
        }
        
//  *  Create a JNDI API InitialContext object if none exists yet.
        
        try{
            Properties env = new Properties( );
            env.setProperty(Context.INITIAL_CONTEXT_FACTORY,"org.apache.activemq.jndi.ActiveMQInitialContextFactory");
            env.setProperty(Context.PROVIDER_URL,"tcp://dr-03:61616");
            javax.naming.Context ctx = new InitialContext(env);
            
            jndiContext = new InitialContext(env);
        }catch (NamingException e){
            System.out.println("Could not create JNDI API " + "context: " + e.toString());
            e.printStackTrace();
            System.exit(1);
        }
        
//  *  Look up connection factory and topic. If either does not exist, exit.
        
        try {


            topicConnectionFactory = (TopicConnectionFactory)
            jndiContext.lookup("TopicConnectionFactory");
            topic = (Topic) jndiContext.lookup(topicName);
        } catch (NamingException e) {
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
        try {
            topicConnection = topicConnectionFactory.createTopicConnection();
            topicSession = topicConnection.createTopicSession(false,Session.AUTO_ACKNOWLEDGE);
            topicPublisher = topicSession.createPublisher(topic);
            message = topicSession.createTextMessage();
            for (int i = 0; i < NUM_MSGS; i++) {
                message.setText("This is message " + (i + 1));
                System.out.println("Publishing message: " + message.getText());
                topicPublisher.publish(message);
            }
        } catch (JMSException e){
            System.out.println("Exception occurred: " + e.toString());
        } finally {
            if(topicConnection != null){
                try{
                    topicConnection.close();
                }catch (JMSException e){}
            }
        }     
    }
}
