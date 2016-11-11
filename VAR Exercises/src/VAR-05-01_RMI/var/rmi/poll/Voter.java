package var.rmi.poll;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Random;

public class Voter implements Runnable {
	private static String service = "//localhost:1099/umfrage4711";
	private boolean pollingBoothOpen = true;
	private String name;
	private BallotBoxInterface bBox;
	
	/**
	 * Creates a voter that repeatedly casts a random vote from a given amount of choices.
	 * @param name - the name of the voter.
	 * */
	Voter(String name) throws MalformedURLException, RemoteException, NotBoundException {
		this.name = name;
		bBox = getBallotBox(service);
	}

	
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		try {
			Voter voter1 = new Voter("Voter 1");
			voter1.run();
			Voter voter2 = new Voter("Voter 2");
			voter2.run();
			Voter voter3 = new Voter("Voter 3");
			Voter voter4 = new Voter("Voter 4");
			Voter voter5 = new Voter("Voter 5");
			Voter voter6 = new Voter("Voter 6");
			Voter voter7 = new Voter("Voter 7");
			Voter voter8 = new Voter("Voter 8");
			Voter voter9 = new Voter("Voter 9");
			Voter voter10 = new Voter("Voter 10");
			Voter voter11 = new Voter("Voter 11");
			Voter voter12 = new Voter("Voter 12");
			Voter voter13 = new Voter("Voter 13");
			Voter voter14 = new Voter("Voter 14");
			
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			e.printStackTrace();
		}
		

	}
	private BallotBoxInterface getBallotBox(String destination) throws MalformedURLException, RemoteException, NotBoundException {
		return (BallotBoxInterface) Naming.lookup(destination);
	}
	
	@Override
	public void run() {
		while(pollingBoothOpen){
			try {
				Thread.currentThread().setName(name);
				System.out.println(Thread.currentThread().getName() + " is casting a vote");
				bBox.vote(Choice.values()[new Random().nextInt(Choice.values().length)]);
				Thread.sleep(10000);
			} catch (RemoteException | InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void closePollingBooth() {
		pollingBoothOpen = false;
	}
	
}
