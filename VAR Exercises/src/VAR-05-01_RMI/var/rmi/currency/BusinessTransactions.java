package var.rmi.currency;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class BusinessTransactions {

	public static void main(String[] args) {
		
		try {
			BankInterface bank = (BankInterface) Naming.lookup("//localhost:1099/bank");
			KontoInterface investor = (KontoInterface) Naming.lookup("//localhost:1099/investor");
			KontoInterface shark = (KontoInterface) Naming.lookup("//localhost:1099/shark");
			
			bank.registerKonto("Joe", investor);
			bank.registerKonto("Gordon", shark);
			
			investor.depositGeld(new Geld(200, Waehrung.DKK));
			investor.depositGeld(new Geld(3000, Waehrung.AUD));
			investor.depositGeld(new Geld(1500, Waehrung.NOK));
			System.out.println("Investor account balances: ");
			for(int i = 0; i < investor.getBalances().length; i++)
				System.out.println(investor.getBalances()[i].toString());
			
			
			shark.depositGeld(new Geld(2000000, Waehrung.USD));
			shark.depositGeld(new Geld(4000000, Waehrung.AUD));
			shark.depositGeld(new Geld(17000000, Waehrung.NOK));
			shark.depositGeld(new Geld(300000, Waehrung.EUR));
			shark.depositGeld(new Geld(5000000, Waehrung.DKK));
			System.out.println("Gecko account balances: ");
			for(int i = 0; i < shark.getBalances().length; i++)
				System.out.println(shark.getBalances()[i].toString());
			
			bank.transferMoney(2000, Waehrung.DKK, "Joe", "Gordon");
			
			System.out.println("Investor account balances: ");
			for(int i = 0; i < investor.getBalances().length; i++)
				System.out.println(investor.getBalances()[i].toString());
			
			System.out.println("Gecko account balances: ");
			for(int i = 0; i < shark.getBalances().length; i++)
				System.out.println(shark.getBalances()[i].toString());
			
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			e.printStackTrace();
		}

	}

}
