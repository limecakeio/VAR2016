package var.rmi.currency;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface BankInterface extends Remote {
	
	/**
	 * Registers a new Bank Account (Konto)
	 * @param accountHolder the name of the person who own the account as a String.
	 * @param konto the reference to the account as a KontoInterface instance.
	 * @throws NotBoundException 
	 * @throws RemoteException 
	 * @throws MalformedURLException 
	 * */
	public void registerKonto(String accountHolder, KontoInterface konto) throws MalformedURLException, RemoteException, NotBoundException;
	
	/**
	 * Transfers money from one account to another.
	 * @param amount - the amount to transfer as an Integer Object.
	 * @param currency  - the currency the amount is in as a Waehrung enumeration.
	 * @param sender - the person who the amount is being withdrawn from as a String.
	 * @param receiver - the person who the amount will be credited to as a String.
	 * */
	public void transferMoney(Integer amount, Waehrung currency, String sender, String receiver) throws RemoteException;

}
