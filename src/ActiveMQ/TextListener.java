/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ActiveMQ;

import javax.jms.*;
import MetadataCore.*;
import java.sql.Timestamp;

/**
 *
 * @author dusan.marjanovic
 */
public class TextListener implements MessageListener{
    
    Integer iNumMess = 0;
    
    @Override
    public void onMessage(Message message) {
    TextMessage msg = null;
    try{
        while(MetadataConstants.bBackupProcedure)
        {
            Thread.sleep(1000);
        }
        if (message instanceof TextMessage) {
            msg = (TextMessage) message;
            java.util.Date dtmNow = new java.util.Date();
            iNumMess++;
            System.out.println("######################################################################");
            System.out.println("Receiving message " + iNumMess.toString() + ". at: " + new Timestamp(dtmNow.getTime()));
            MetadataCommunicator.ReceiveXML(msg.getText());
        
     //   System.out.println("Reading message: " + msg.getText());
        } else if (message instanceof BytesMessage) {
            BytesMessage msgb = (BytesMessage) message;
            java.util.Date dtmNow = new java.util.Date();
            iNumMess++;
            System.out.println("######################################################################");
            System.out.println("Receiving message " + iNumMess.toString() + ". at: " + new Timestamp(dtmNow.getTime()));
            MetadataCommunicator.ReceiveXML(msgb.readUTF());
        }
        else {
            System.out.println("Message of wrong type: " + message.getClass().getName());
        }
    }catch (JMSException e){
        System.out.println("JMSException in onMessage(): " + e.toString());
    }catch (Throwable t){
        System.out.println("Exception in onMessage():" + t.getMessage());
    }
    
    } 
}
