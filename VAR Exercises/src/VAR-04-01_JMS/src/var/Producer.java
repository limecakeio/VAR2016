package var;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Producer {
	public static void main(String[] args) {
		String[] dictionary = {"Alpha", "Beta", "Gromek", "Runner", "Climber", "Jumper"};
		for(int i = 0; i < 100; i++) {
			Producer producer = null;
			Random random = new Random();
			int dice = random.nextInt(5);
			
			DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
			String currentDaytime = dateFormat.format(new Date());
		try {
			String text = "Generated word: " + dictionary[dice] + ". At: " + currentDaytime;
			String priority = args[1];
			producer = new Producer();
			producer.sendMessage(text, priority);
			//System.out.println("Produced message " + i + ". Message reads: " + text + ".");
			} catch (Exception e) {
				System.err.println(e);
			} finally {
				if (producer != null)
				producer.close();
			}
	}
	}

	private Connection mConnection;
	private MessageProducer mMessageProducer;

	private Session mSession;

	public Producer() throws NamingException, JMSException {
		Context ctx = new InitialContext();
		ConnectionFactory factory = (ConnectionFactory) ctx.lookup("ConnectionFactory");
		Destination queue = (Destination) ctx.lookup(Conf.QUEUE);
		mConnection = factory.createConnection();
		mSession = mConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		mMessageProducer = mSession.createProducer(queue);
	}

	public void close() {
		try {
			if (mMessageProducer != null)
				mMessageProducer.close();
			if (mSession != null)
				mSession.close();
			if (mConnection != null)
				mConnection.close();
		} catch (JMSException e) {
			System.err.println(e);
		}
	}

	public void sendMessage(String text, String priority) throws JMSException {
		TextMessage message = mSession.createTextMessage();
		message.setText(text);
		message.setStringProperty("Priority", priority);
		mMessageProducer.send(message);
	}
}
