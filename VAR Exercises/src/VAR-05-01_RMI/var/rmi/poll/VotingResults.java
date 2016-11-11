package var.rmi.poll;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class VotingResults implements Runnable {
	private static String service = "//localhost:1099/umfrage4711";
	private int updateTime;
	/**
	 * Outputs the total votes submitted as well as for which choices in a given intervall of time
	 * @param updateTime - the intervall in which to report the recorded votes.
	 * 
	 * */
	VotingResults(int updateTime) {
		this.updateTime = updateTime;
		run();
	}
	@Override
	public void run() {
		try {
			BallotBoxInterface bBox = (BallotBoxInterface) Naming.lookup(service);
			while(true){
				for(int i = 0; i < Choice.values().length; i++)
					System.out.println(Choice.values()[i] + ": " + bBox.getNumberOfVotes(Choice.values()[i]));
				System.out.println("-------------------------------------------");
				System.out.println("Total Votes: " + bBox.countVotes());
				Thread.sleep(updateTime);
				
			}
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		VotingResults results = new VotingResults(2000);
	}
	

}
