package var;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Supplier implements MessageListener {
	private Connection mConnection;
	private MessageProducer mMessageProducer;
	private MessageConsumer mMessageConsumer;
	private Session mSession;
	
	public Supplier() throws NamingException, JMSException {
		//Create JNDI Context
		Context ctx = new InitialContext();
		
		//Get the ConnectionFactory from the NameService
		ConnectionFactory factory = (ConnectionFactory) ctx.lookup("ConnectionFactory");
		
		//Get the Order-Queue Destination from the NameService
		Destination orderQueue = (Destination) ctx.lookup(Conf.QUEUE2);
		
		//Get the Reply-Queue Destination from the NameService
		Destination replyQueue = (Destination) ctx.lookupLink(Conf.QUEUE3);
		
		
		//Establish the connection
		mConnection = factory.createConnection();
		
		//Create the session
		mSession = mConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
		//Create sender
		mMessageProducer = mSession.createProducer(replyQueue);
		
		//Create receiver
		mMessageConsumer = mSession.createConsumer(orderQueue);
		
		//Start listenining to MOM
		mMessageConsumer.setMessageListener(this);
		mConnection.start();
	}
	
	/**
	 * Generates and sends a message to the MOM-Server
	 * @throws JMSException 
	 * */
	public void sendMessage(String text) throws JMSException {
		TextMessage message = mSession.createTextMessage();
		message.setText(text);
		mMessageProducer.send(message);
	}
	
	/**
	 * @param message - the message passed through by the MOM-Server
	 * Received a message from the MOM-Server and attempts to print it to the console.
	 * */
	public void onMessage(Message message) {
		try {
			if (message instanceof TextMessage) {
				TextMessage textMessage = (TextMessage) message;
				String incoming = textMessage.getText();
				if(incoming.length()%2 == 0) {
					sendMessage("Auf Lager.");
				}
				else {
					sendMessage("Ausgegangen.");
				}
			}
		} catch (JMSException e) {
			System.err.println(e);
		}
	}
	
	/**
	 * Frees up all resources
	 * */
	public void close() {
		try {
			if(mMessageProducer != null) {
				mMessageProducer.close();
			}
			if(mMessageConsumer != null) {
				mMessageConsumer.close();
			}
			if(mSession != null) {
				mSession.close();
			}
			if(mConnection != null) {
				mConnection.close();
			}
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
		
		Supplier supplier = null; //Due to finally clause
		
		try {
			//Setup producer to send inquiry to supplier
			supplier = new Supplier();
			//Receiving reply based on initial timeout setting in arguments
			long wait = Long.parseLong(args[0]);
			Thread.sleep(wait);
			
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (JMSException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
		if (supplier != null)
		supplier.close();
	}	
	}
}
