package var;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class ConsumerPull {
	public static void main(String[] args) {
		ConsumerPull consumer = null;
		System.out.println("Started PULL CONSUMER...");
		try {
			long timeout = Long.parseLong(args[0]);
			consumer = new ConsumerPull(timeout);
			consumer.receiveMessage();
		} catch (Exception e) {
			System.err.println(e);
		} finally {
			if (consumer != null)
				consumer.close();
			System.out.println("Finished PULL CONSUMER");
		}
	}

	protected Connection mConnection;
	protected MessageConsumer mMessageConsumer;
	protected Destination mQueue;
	protected Session mSession;

	protected long mTimeout;

	public ConsumerPull(long timeout) throws NamingException, JMSException {
		mTimeout = timeout;
		Context ctx = new InitialContext();
		ConnectionFactory factory = (ConnectionFactory) ctx.lookup("ConnectionFactory");
		mQueue = (Destination) ctx.lookup(Conf.QUEUE);
		mConnection = factory.createConnection();
		mSession = mConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		mMessageConsumer = mSession.createConsumer(mQueue);
		mConnection.start();
	}

	public void close() {
		try {
			if (mMessageConsumer != null)
				mMessageConsumer.close();
			if (mSession != null)
				mSession.close();
			if (mConnection != null)
				mConnection.close();
		} catch (JMSException e) {
			System.err.println(e);
		}
	}

	public void receiveMessage() throws JMSException {
		Message message;
		while ((message = mMessageConsumer.receive(mTimeout)) != null) {
			if (message instanceof TextMessage) {
				TextMessage textMessage = (TextMessage) message;
				System.out.println(textMessage.getText());
			}
		}
	}
}