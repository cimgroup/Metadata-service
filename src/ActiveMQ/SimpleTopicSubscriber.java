/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ActiveMQ;

import MetadataCore.MetadataConstants;
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
        
        MetadataConstants.c_Topics.add("ALERT.*.APICallRequest");
        MetadataConstants.c_Topics.add("ALERT.*.CommitNew");
        MetadataConstants.c_Topics.add("ALERT.*.CommitNew.Annotated");
        MetadataConstants.c_Topics.add("ALERT.*.CommentNew.Annotated");
        MetadataConstants.c_Topics.add("ALERT.*.CompetencyNew");
        MetadataConstants.c_Topics.add("ALERT.*.CompetencyUpdate");
        MetadataConstants.c_Topics.add("ALERT.*.ForumPostNew");
        MetadataConstants.c_Topics.add("ALERT.*.ForumPostNew.Annotated");
        MetadataConstants.c_Topics.add("ALERT.*.IdentityNew");
        MetadataConstants.c_Topics.add("ALERT.*.IdentityUpdate");
        MetadataConstants.c_Topics.add("ALERT.*.IdentityRemove");
        MetadataConstants.c_Topics.add("ALERT.*.IssueNew");
        MetadataConstants.c_Topics.add("ALERT.*.IssueNew.Annotated");
        MetadataConstants.c_Topics.add("ALERT.*.IssueUpdate");
        MetadataConstants.c_Topics.add("ALERT.*.IssueUpdate.Annotated");
        MetadataConstants.c_Topics.add("ALERT.*.MailNew");
        MetadataConstants.c_Topics.add("ALERT.*.MailNew.Annotated");
        MetadataConstants.c_Topics.add("ALERT.*.ArticleAdded");
        //MetadataConstants.c_Topics.add("ALERT.*.ArticleAdded.Annotated");
        MetadataConstants.c_Topics.add("ALERT.*.ArticleModified");
        //MetadataConstants.c_Topics.add("ALERT.*.ArticleModified.Annotated");
        MetadataConstants.c_Topics.add("ALERT.*.ArticleDeleted");
        //MetadataConstants.c_Topics.add("ALERT.*.ArticleDeleted.Annotated");
        MetadataConstants.c_Topics.add("ALERT.*.ConceptNew");
//        MetadataConstants.c_Topics.add("ALERT.Metadata.Test");

        
        Context jndiContext = null;
        TopicConnectionFactory topicConnectionFactory = null;
        TopicConnection topicConnection = null;
        TopicSession topicSession = null;
        Topic[] topics = new Topic[MetadataConstants.c_Topics.size()];
        TopicSubscriber[] topicSubscribers = new TopicSubscriber[MetadataConstants.c_Topics.size()];
        TextListener topicListener = null;
        TextMessage message = null;
        InputStreamReader inputStreamReader = null;
        char answer = '\0';
        
        
        
        
//*  Read topic name from command line and display it.
        

//      System.out.println("Topic name is " + topicName);
        
//*  Create a JNDI API InitialContext object if none exists yet.
        
        try {
            Properties env = new Properties( );
            env.setProperty(Context.INITIAL_CONTEXT_FACTORY,"org.apache.activemq.jndi.ActiveMQInitialContextFactory");
            env.setProperty(Context.PROVIDER_URL, MetadataConstants.sActiveMQAddress);
            for(int i=0; i<MetadataConstants.c_Topics.size(); i++)
            {
                env.setProperty("topic." + MetadataConstants.c_Topics.get(i), MetadataConstants.c_Topics.get(i));
            }
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
            
            for(int i=0; i<MetadataConstants.c_Topics.size(); i++)
            {
                topics[i] = (Topic) jndiContext.lookup(MetadataConstants.c_Topics.get(i));
            }
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
            topicConnection.setClientID("MetadataService");
            topicSession = topicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
            topicListener = new TextListener();
            
            for(int i=0; i<MetadataConstants.c_Topics.size(); i++)
            {
                //topicSubscribers[i] = topicSession.createSubscriber(topics[i]);
                topicSubscribers[i] = topicSession.createDurableSubscriber(topics[i], "Metadata." + topics[i].getTopicName());
                topicSubscribers[i].setMessageListener(topicListener);
            }
            
            
            topicConnection.start();
            System.out.println("######################################################################");
            System.out.println("Metadata service is ready. To end program, enter Q or q, then <return>");
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
