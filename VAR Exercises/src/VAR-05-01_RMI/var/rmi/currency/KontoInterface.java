package var.rmi.currency;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface KontoInterface extends Remote {
	
	/**
	 * Deposits a Geld Object into an account (Konto).
	 * @param a Geld object which is then added to the konto.
	 * */
	public void depositGeld(Geld geld) throws RemoteException;
	
	/**
	 * Withdraws an amount from an account (Konto)
	 * @param currency the Waehrung to withdraw from as a Waehrung enumaration.
	 * @param amount the Betrag to be withdrawn from the account as an Integer object.
	 * */
	public Geld withdrawGeld(Geld geld) throws RemoteException;
	
	/**
	 * @return The all balances in the current account as an array of Geld
	 * */
	public Geld[] getBalances();
	
	/**
	 * @return - A specific balance based on the Waehrung as a Geld object
	 * */
	public Geld getBalance(Waehrung currency);

}
