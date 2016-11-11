package var.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class AlarmSubscriber {
	private static double gasLimit;
	public static MqttClient client;
	public static String subid = Conf.ID + "Sub" + Thread.currentThread().hashCode();

	public static void main(String[] args) throws MqttException {
		try {
			gasLimit = Double.valueOf(args[0]);
			client = new MqttClient(Conf.BROKER, subid);
			client.setCallback(new MqttCallback() {
				
				@Override
				public void messageArrived(String topic, MqttMessage m) throws Exception {
					double measurement = Double.valueOf(m.toString());
					if(measurement > gasLimit){
						System.out.println("ALTERT: The gas is through the roof at: " + measurement);
					}
				}
				
				@Override
				public void deliveryComplete(IMqttDeliveryToken arg0) {
				}
				
				@Override
				public void connectionLost(Throwable arg0) {
				}
			});
			client.connect();
			
			client.subscribe(Conf.TOPIC2 + "/Ground/Toilet/Gas");
			while(true) {
				Thread.sleep(1000);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			client.disconnect();
		}
	}
}
