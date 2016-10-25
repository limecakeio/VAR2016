package exercise01;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class MesswertServer {
	
	private static final int PORT = 4713;
	private static final int BUFSIZE = 508;

	public static void main(String[] args) {
		try (DatagramSocket socket = new DatagramSocket(PORT)) {
			DatagramPacket packetIn = new DatagramPacket(new byte[BUFSIZE], BUFSIZE);

			System.out.println("Messwert server is running...");

			while (true) {
				socket.receive(packetIn);
				
				String message = new String(packetIn.getData(), 0, packetIn.getLength());
				
				System.out.print(packetIn.getAddress() + " ");
				
				for(int i = 0; i < message.length(); i++) {
					if(message.charAt(i) == '|') {
						System.out.print(" ");
					} else {
						System.out.print(message.charAt(i));
					}
				}
				System.out.print("\n");
					
			}
		} catch (IOException e) {
			System.err.println(e);
		}

	}

}
