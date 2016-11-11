package var;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.DestinationDoesNotExistException;

public class FileServer {
	private static final String destination = "files";
	private String root = "";
	private boolean active = true;
	private Connection connection;
	private MessageProducer messageProducer;
	private MessageConsumer messageConsumer;
	private Session session;
	
	/**
	 * @author Richard Vladimirskij
	 * 
	 * FileServer
	 * 
	 * Collects TextMessages from the queue specifying a file to be served
	 * from the server's root.
	 * 
	 * If the requested filename exists, the file is returned via a BytesMessage along with a 
	 * "true" BooleanProperty "status".
	 * 
	 * If the requested filename does not exist or is a directory, an empty BytesMessage is returned
	 * along with with a "false" BooleanProperty "status". 
	 * @throws IOException 
	 * 
	 * */
	public FileServer() throws NamingException, JMSException, IOException {
		//Create JNDI Context
		Context ctx = new InitialContext();
		
		//Get the ConnectionFactory from the NameService
		ConnectionFactory factory = (ConnectionFactory) ctx.lookup("ConnectionFactory");
		
		//Get the Order-Queue Destination from the NameService
		Destination fileQueue = (Destination) ctx.lookup(destination);
		
		//Establish the connection
		connection = factory.createConnection();
		
		//Create the session
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
		//Create receiver
		messageConsumer = session.createConsumer(fileQueue);
		
		connection.start();
		
		System.out.println("File server started...");	
	}
	
	/**
	 * Synchronously collects TextMessages from the MOM-Server expecting them to 
	 * contain a filename. 
	 * 
	 * If the requested file exists and is not a directory the server returns a 
	 * BytesMessage containing a property "status" reading true.
	 * 
	 * If the requested file is a directory or does not exist, the server returns an empty 
	 * BytesMessage containing a property "status" reading false.
	 * 
	 * @throws JMSException 
	 * @throws IOException 
	 * */
	public void processMessages() throws JMSException, IOException {
		while(active) {
			Message message = messageConsumer.receive();
			
	
			if (message instanceof TextMessage) {
				TextMessage textMessage = (TextMessage) message;
				String filename = root + "/" + textMessage.getText();
				
				//Returning a BytesMessage whether request valid or not
				BytesMessage response = session.createBytesMessage();
				boolean status = false;
				
				//Testing via File for existence and type
				File file = new File(filename);
				if(file.exists() && !file.isDirectory() && file.canRead()) {
					status = true;
					InputStream input = new FileInputStream(file);
					int character;
					while((character = input.read()) != -1) {
						response.writeByte((byte) character);
					}
					input.close();
				}
				
				//Send the response to the private queue as initiated by client
				try {
					Destination replyQueue = (Destination) message.getJMSReplyTo();
					messageProducer = session.createProducer(replyQueue);
					response.setBooleanProperty("status", status);
					messageProducer.send(response);
				} catch(DestinationDoesNotExistException e) {
					System.out.println("Could not send message to originating client. Client is offline.");
					continue;
				}
				messageProducer.close();
			}
		}
	}
	
	/**
	 * Frees up all resources
	 * */
	private void close() {
		try {
			if(messageProducer != null) {
				messageProducer.close();
			}
			if(messageConsumer != null) {
				messageConsumer.close();
			}
			if(session != null) {
				session.close();
			}
			if(connection != null) {
				connection.close();
			}
			System.out.println("File server has stopped");
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Stops the server
	 * */
	public void stopServer() {
		System.out.println("Server is being shut down.");
		this.active = false;
	}
	
	/**
	 * @param root - expects a String containing the root path without trailing slash
	 * and sets the document root
	 * */
	public void setRoot(String root) {
		this.root = root;
	}
	
	public static void main(String[] args) {
		FileServer fs = null;
			try {
				fs = new FileServer();
				
				if(args[0] != null) {
					fs.setRoot(args[0]);
				}
				
				fs.processMessages();
			} catch (NamingException e) {
				e.printStackTrace();
			} catch (JMSException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
			if (fs != null)
				fs.close();
			}
	}
}