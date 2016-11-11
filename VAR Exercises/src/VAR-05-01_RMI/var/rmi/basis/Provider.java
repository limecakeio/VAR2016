package var.rmi.basis;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Provider extends Remote {
	public int sum(int a, int b) throws RemoteException;
	
	/**
	 * @return the amount of times times the method sum was called while the provider is active.
	 * */
	public int getAccessCount() throws RemoteException;
}
