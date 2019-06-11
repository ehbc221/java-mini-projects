import java.rmi.Remote;
import java.rmi.RemoteException;


public interface Rem extends Remote{

	public String getMessage() throws RemoteException;
	public int addition(int x, int b) throws RemoteException;
}
