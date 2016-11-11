package var;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class ConsumerFiltered implements MessageListener {

	public static void main(String[] args) {
		ConsumerFiltered consumer = null;
		try {
			long wait = Long.parseLong(args[0]);
			consumer = new ConsumerFiltered();
			Thread.sleep(wait);
		} catch (Exception e) {
			System.err.println(e);
		} finally {
			if (consumer != null)
				consumer.close();
		}
	}

	protected Connection mConnection;
	protected MessageConsumer mMessageConsumer;
	protected Destination mQueue;

	protected Session mSession;

	public ConsumerFiltered() throws NamingException, JMSException {
		Context ctx = new InitialContext();
		ConnectionFactory factory = (ConnectionFactory) ctx.lookup("ConnectionFactory");
		mQueue = (Destination) ctx.lookup(Conf.QUEUE);
		mConnection = factory.createConnection();
		mSession = mConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		mMessageConsumer = mSession.createConsumer(mQueue, "Priority='high'");
		mMessageConsumer.setMessageListener(this);
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

	@Override
	public void onMessage(Message message) {
		try {
			if (message instanceof TextMessage) {
				TextMessage textMessage = (TextMessage) message;
				System.out.println(
						textMessage.getText() + " [Priority=" + textMessage.getStringProperty("Priority") + "]");
			}
		} catch (JMSException e) {
			System.err.println(e);
		}

	}
}
