package exercise01;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Timer;

public class MesswertClient implements Runnable {
	
	private static final String HOST = "localhost";
	private static final int MESSWERTPORT = 4713;
	private static final int BUFSIZE = 508;
	private static final int TIMEOUT = 2000;
	
	MesswertClient() {
		run();
	}
	
	public static void main(String[] args) {
		MesswertClient currentSession = new MesswertClient();
	}
	
/**
 * Sends a random number and the current time to the MesswertServer
 * */
	private static void sendMesswert() {
		
		try(DatagramSocket socket = new DatagramSocket()) {
			
			InetAddress address = InetAddress.getByName(HOST); 
			socket.setSoTimeout(TIMEOUT);
			
			byte[] timeData = getCurrentTime().getBytes();	
			byte[] randomNumberData = new String("" + getRandomNumber()).getBytes();
			byte[] data = concat(timeData, randomNumberData);
			
			DatagramPacket dataPacket = new DatagramPacket(data, data.length, address, MESSWERTPORT);
			
			socket.send(dataPacket);
			
		} catch (SocketException e) {
			System.err.println("Timeout: " + e.getMessage());
		} catch (UnknownHostException e) {
			System.err.println("Unknown Host: " + e.getMessage());
		} catch (IOException e) {
			System.err.println("IO Exception: " + e.getMessage());
		}
	}
	
/**
 * @return The current system time
 * */
	private static String getCurrentTime() {
		return "" + new SimpleDateFormat("HH:mm:ss").format(new Date());
	}

/**
 * @return A random number as a double 
 * */
private static float getRandomNumber() {
	Random random = new Random();
	return random.nextFloat();
}

/**
 * Concatenates two array and separates them with a "|";
 * */

private static byte[] concat(byte[] b1, byte[] b2) {
	int requiredLength = b1.length + b2.length + 1;
	byte[] byteArray = new byte[requiredLength];
	int p1 = 0;
	int p2 = b1.length + 1;
	
	//Fill the first half of the array
	for(; p1 < b1.length; p1++) {
		byteArray[p1] = b1[p1];
	}
	
	//Add the separator
	byteArray[p1++] = '|';
	
	//Fill the second half of the array
	for(int i = 0; p2 < requiredLength; i++, p2++) {
		byteArray[p2] = b2[i];
	}
	
	return byteArray;
}

/**
 * Thread function
 * */
@Override
public void run() {
	while(true) {
		sendMesswert();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
}