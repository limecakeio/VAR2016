package var.rmi.poll;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface BallotBoxInterface extends Remote {
	
	/**Places a vote for a given choice*/
	public void vote(Choice choice) throws RemoteException;

	/**
	 * @return the total amounts of votes placed
	 * */
	public int countVotes() throws RemoteException;
	
	/**
	 * @return the amount of votes placed for a specific choice.
	 * */
	public int getNumberOfVotes(Choice choice) throws RemoteException;
}
