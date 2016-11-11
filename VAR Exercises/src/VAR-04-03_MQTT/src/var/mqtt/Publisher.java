package var.mqtt;

import java.util.Date;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class Publisher {
	public static String pubid = Conf.ID + "Pub" + Thread.currentThread().hashCode();
	public static void main(String[] args) {
		MqttClient client;
		try {
			client = new MqttClient(Conf.BROKER, pubid);
			client.connect();

			MqttMessage message = new MqttMessage();
			String m = Thread.currentThread().hashCode() + "/" + pubid + "/" + (new Date()).toString();
			message.setPayload(m.getBytes());

			client.publish(Conf.TOPIC, message);
			client.disconnect();
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}
}
