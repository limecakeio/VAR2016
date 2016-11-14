package var.rmi.currency;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.EnumMap;
import java.util.Map;


@SuppressWarnings("serial")
public class KontoImpl extends UnicastRemoteObject implements KontoInterface {
	private Map<Waehrung, Integer> konto = new EnumMap<Waehrung, Integer>(Waehrung.class);

	protected KontoImpl() throws RemoteException {
		super();
	}
	
	@Override
	public synchronized void depositGeld(Geld geld){
		Waehrung currency = geld.getWaehrung();
		Integer balance = konto.get(currency);
		if(balance == null)
			balance = 0;
		Integer amount = geld.getGeldbetrag();
		konto.put(currency, balance + amount);
	}
	
	@Override
	public synchronized Geld withdrawGeld(Geld geld){
		Waehrung currency = geld.getWaehrung();
		Integer balance = konto.get(currency);
		if(balance == null)
			balance = 0;
		Integer amount = geld.getGeldbetrag();
		konto.put(currency, balance - amount);
		return new Geld(konto.get(currency), currency);
	}
	
	@Override
	public Geld[] getBalances() {
		Geld[] balances = new Geld[Waehrung.values().length];
		for(int i = 0; i < Waehrung.values().length; i++) {
			Integer value = konto.get(Waehrung.values()[i]);
			if(value == null)
				value = 0;
			balances[i] = new Geld(value ,Waehrung.values()[i]);
		}
		return balances;
	}
	
	@Override
	public Geld getBalance(Waehrung currency) {
		Integer value = konto.get(currency);
		if(value == null)
			value = 0;
		return new Geld(value, currency);
	}
	
	public static void main(String[] args) throws RemoteException, MalformedURLException {
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
		//NOTE the registry should be initiated by the bank, accounts cannot exist without a bank
		Naming.rebind("//localhost:1099/investor", new KontoImpl());
		Naming.rebind("//localhost:1099/shark", new KontoImpl());
	}
}
