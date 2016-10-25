package exercise01;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

public class EchoClient {
	private static final String HOST = "localhost";
	private static final int PORT = 4711;
	private static final int BUFSIZE = 508;
	private static final int TIMEOUT = 2000;

	public static void main(String[] args) {
		String hello = "Hello World";
		byte[] data = hello.getBytes();
		
		try (DatagramSocket socket = new DatagramSocket()){
			// Zeit in ms, für wie lange ein read() auf socket blockiert. 
			//Bei timeout is java.net.SocketTimeoutException (TIMEOUT == 0 => blockiert für immer)
			PortScanner pScan = new PortScanner();
			socket.setSoTimeout(TIMEOUT);
			InetAddress addr = InetAddress.getByName(HOST);
			
			DatagramPacket packetOut = new DatagramPacket(data, data.length, addr, PORT);
			socket.send(packetOut);
			
			DatagramPacket packetIn = new DatagramPacket(new byte[BUFSIZE], BUFSIZE);
			socket.receive(packetIn);
			
			String received = new String(packetIn.getData(), 0, packetIn.getLength());
			System.out.println("Received: " + received);
		
		} catch (SocketTimeoutException e) {
			System.err.println("Timeout: " + e.getMessage());
		} catch (Exception e) {
			System.err.println(e);
		}
	}
}