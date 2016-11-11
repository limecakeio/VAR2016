package var.rmi.currency;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("serial")
public class Bank extends UnicastRemoteObject implements BankInterface {
	private Map<String, KontoInterface> konten = new HashMap<String, KontoInterface>();

	protected Bank() throws RemoteException {
		super();
	}
	
	@Override
	public synchronized void registerKonto(String accountHolder, KontoInterface konto) throws MalformedURLException, RemoteException, NotBoundException {
		konten.put(accountHolder, konto);
	}

	@Override
	public synchronized void transferMoney(Integer amount, Waehrung currency, String sender, String receiver) throws RemoteException {
		System.out.println("Transaction: " + sender + " is transferring " + amount +  " " + currency + " to " + receiver);
		
		KontoInterface payer = konten.get(sender);
		Geld payload = payer.withdrawGeld(new Geld(amount, currency));
		
		KontoInterface payee = konten.get(receiver);
		payee.depositGeld(payload);
	}

	public static void main(String[] args) throws RemoteException, MalformedURLException {
		LocateRegistry.createRegistry(1099);
		Naming.rebind("//localhost:1099/bank", new Bank());
	}

}
