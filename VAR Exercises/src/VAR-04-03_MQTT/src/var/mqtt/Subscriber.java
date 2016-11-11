package var.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class Subscriber {
	public static MqttClient client;
	public static String subid = Conf.ID + "Sub" + Thread.currentThread().hashCode();

	public static void main(String[] args) throws MqttException {
		try {
			client = new MqttClient(Conf.BROKER, subid);
			client.setCallback(new MqttCallback() {
				
				@Override
				public void messageArrived(String topic, MqttMessage m) throws Exception {
					System.out.println(topic + ": " + m.toString());
				}
				
				@Override
				public void deliveryComplete(IMqttDeliveryToken arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void connectionLost(Throwable arg0) {
					// TODO Auto-generated method stub
					
				}
			});
			client.connect();
			
			client.subscribe(Conf.TOPIC);
			while(true) {
				Thread.sleep(1000);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			client.disconnect();
		}
	}
}
