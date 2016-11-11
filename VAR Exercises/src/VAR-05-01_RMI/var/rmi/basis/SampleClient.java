package var.rmi.basis;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class SampleClient {

	public static void main(String[] args) {
		try {
			Provider remoteObject = (Provider) Naming.lookup("//localhost:1099/Addierer");
			System.out.println("1+2=" + remoteObject.sum(1, 2));
			System.out.println("2+7=" + remoteObject.sum(2, 7));
			System.out.println("3+7=" + remoteObject.sum(3, 7));
			System.out.println("22+23=" + remoteObject.sum(22, 23));
			System.out.println("19+12=" + remoteObject.sum(19, 12));
			System.out.println("Called the method sum " + remoteObject.getAccessCount() + " times.");
		} catch (MalformedURLException | RemoteException | NotBoundException ex) {
			System.err.println(ex.toString());
		}
	}

}
