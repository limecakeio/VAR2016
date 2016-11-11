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
