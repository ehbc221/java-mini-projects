import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.net.*;

// Taken from Core Web Programming from Prentice Hall and
// Sun Microsystems Press, and may be freely used or adapted.
// Copyright 2000, http://www.corewebprogramming.com/

/**
 * The server creates a RemImpl (which implements the Rem interface), then
 * registers it with the URL Rem, where clients can access it.
 */

public class RemServer {
	public static void main(String[] args) {

		String port = (args.length > 0) ? args[0] : "1099";

		try {
				LocateRegistry.createRegistry(Integer.parseInt(port));
				RemImpl localObject = new RemImpl();
				Naming.rebind("rmi://localhost:" + port + "/Rem", localObject);
				
		} catch (RemoteException re) {
			System.out.println("RemoteException: " + re);
		} catch (MalformedURLException mfe) {
			System.out.println("MalformedURLException: " + mfe);
		}
	}
}
