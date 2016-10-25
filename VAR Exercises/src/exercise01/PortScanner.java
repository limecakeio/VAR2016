package exercise01;

import java.net.InetAddress;
import java.net.DatagramSocket;
import java.net.SocketAddress;

public class PortScanner {
	
	PortScanner() {
		
		for(int i = 0; i <= 10000; i++) {
			try(DatagramSocket socket = new DatagramSocket(0)) {
				
				int currentPort = socket.getLocalPort();
				System.out.println("The initial port is: " + currentPort);
				
				InetAddress currentAddress = socket.getLocalAddress();
				System.out.println("The initial Address is: " +currentAddress);
				System.out.println("The host Address is: " + currentAddress.getHostAddress());	
				
			} catch (Exception E) {
				System.err.println("Failed connecting to port: " + i);
			}	
			
		}
	}
	
}
