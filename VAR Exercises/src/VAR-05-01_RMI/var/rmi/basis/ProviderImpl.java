package var.rmi.basis;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

@SuppressWarnings("serial")
public class ProviderImpl extends UnicastRemoteObject implements Provider {
	private int accessCount;

	protected ProviderImpl() throws RemoteException {
		super();
		accessCount = 0;
	}

	@Override
	public int sum(int a, int b) throws RemoteException {
		accessCount++;
		System.out.println("Amount of times function sum was called: " +accessCount);
		return a + b;
	}
	
	@Override
	public int getAccessCount() throws RemoteException {
		return accessCount;
	}

	public static void main(String[] args) {
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
		try {
			LocateRegistry.createRegistry(1099);
			Naming.rebind("//localhost:1099/Addierer", new ProviderImpl());
		} catch (MalformedURLException | RemoteException e) {
			System.err.println(e);
		}

	}

}
