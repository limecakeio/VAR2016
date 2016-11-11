package var.mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class Sensor {
	public static String pubid = Conf.ID + Thread.currentThread().hashCode();
	public static void main(String[] args) {
		MqttClient client;
		String topic = Conf.TOPIC2 + "/" + args[0] + "/" + args[1] + "/" + args[2];
		double measurement = new Double(args[3]);
		while(true) {
		if(Math.random() > 0.5) 
			measurement += 0.1;
		else
			measurement -= 0.1;
		
		try {
			client = new MqttClient(Conf.BROKER, pubid);
			client.connect();

			MqttMessage message = new MqttMessage();
			String m = "" + measurement;
			message.setPayload(m.getBytes());

			client.publish(topic, message);
			client.disconnect();
		} catch (MqttException e) {
			e.printStackTrace();
		}
		}
	}
}