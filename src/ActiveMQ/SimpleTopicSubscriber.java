/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ActiveMQ;

import javax.jms.*;
import javax.naming.*;
import java.io.*;
import java.util.Properties;

/**
 *
 * @author dusan.marjanovic
 */
public class SimpleTopicSubscriber {
    public static void main() {
        String topicName = null;
        Context jndiContext = null;
        TopicConnectionFactory topicConnectionFactory = null;
        TopicConnection topicConnection = null;
        TopicSession topicSession = null;
        Topic topic = null;
        TopicSubscriber topicSubscriber = null;
        TextListener topicListener = null;
        TextMessage message = null;
        InputStreamReader inputStreamReader = null;
        char answer = '\0';
        
//*  Read topic name from command line and display it.
        
    /*    if (args.length != 1) {
            System.out.println("Usage: java " + "SimpleTopicSubscriber <topic-name>");
            System.exit(1);
        }*/
        topicName = "MetadataIn";
        System.out.println("Topic name is " + topicName);
        
//*  Create a JNDI API InitialContext object if none exists yet.
        
        try {
            Properties env = new Properties( );
            env.setProperty(Context.INITIAL_CONTEXT_FACTORY,"org.apache.activemq.jndi.ActiveMQInitialContextFactory");
            env.setProperty(Context.PROVIDER_URL,"tcp://dr-03:61616");
            env.setProperty("topic.MetadataIn", topicName);
            jndiContext = new InitialContext(env);
            } catch (NamingException e) {
                System.out.println("Could not create JNDI API " + "context: " + e.toString());
                e.printStackTrace();
                System.exit(1);
        }
        
//*  Look up connection factory and topic. If either does not exist, exit.
        
        try {
            topicConnectionFactory = (TopicConnectionFactory)
            jndiContext.lookup("TopicConnectionFactory");
            topic = (Topic) jndiContext.lookup(topicName);
            } catch (NamingException e) {
                System.out.println("JNDI API lookup failed: " + e.toString());
                e.printStackTrace();
                System.exit(1);
            }
        
/*
    * Create connection.
    * Create session from connection; false means session is
    * not transacted.
    * Create subscriber.
    * Register message listener (TextListener).
    * Receive text messages from topic.
    * When all messages have been received, enter Q to quit.
    * Close connection.
*/     
        
        try{
            topicConnection = topicConnectionFactory.createTopicConnection();
            topicSession = topicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
            topicSubscriber = topicSession.createSubscriber(topic);
            topicListener = new TextListener();
            topicSubscriber.setMessageListener(topicListener);
            topicConnection.start();
            System.out.println("To end program, enter Q or q, " + "then <return>");
            inputStreamReader = new InputStreamReader(System.in);
            while (!((answer == 'q') || (answer == 'Q'))) {
                try {
                    answer = (char) inputStreamReader.read();
                    } catch (IOException e) {
                        System.out.println("I/O exception: " + e.toString());
                    }
            }
        }catch (JMSException e){
            System.out.println("Exception occurred: " + e.toString());
        }finally{
            if (topicConnection != null){
                try{
                    topicConnection.close();
                }catch (JMSException e){}
            }
        }
    }
}
