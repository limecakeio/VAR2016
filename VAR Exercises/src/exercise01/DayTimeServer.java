package exercise01;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DayTimeServer {
	
	private static final int PORT = 4712;
	private static final int BUFSIZE = 508;
	
	public static void main(String[] args) {
		try (DatagramSocket socket = new DatagramSocket(PORT)) {
			DatagramPacket packetIn = new DatagramPacket(new byte[BUFSIZE], BUFSIZE);
			DatagramPacket packetOut = new DatagramPacket(new byte[BUFSIZE], BUFSIZE);

			System.out.println("Daytime server is running...");

			while (true) {
				
				socket.receive(packetIn);
				byte[] data = new byte[0];
				System.out.println("Received: " + packetIn.getLength() + " bytes");
				if(packetIn.getLength() == 0) {
					
					//Get the current time
					DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
					Date date = new Date();
					String currentDaytime = dateFormat.format(date);
					
					//Transform the time to a byte-array
					data = currentDaytime.getBytes();
					
					//Set the packet
					packetOut.setData(data);
					packetOut.setLength(data.length);
					packetOut.setAddress(packetIn.getAddress());
					packetOut.setPort(packetIn.getPort());
					
					socket.send(packetOut);
				}
			}
		} catch (IOException e) {
			System.err.println(e);
		}
	}
}
