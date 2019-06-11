import org.omg.CosNaming.*;
import org.omg.CORBA.*;
import org.omg.PortableServer.*;

public class FileServer
{
   public static void main(String args[])
   {
		try
		{
			 ORB orb=ORB.init(args, null);
			 POA rootpoa=POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
			 rootpoa.the_POAManager().activate();
			 FileServant FileServantVar=new FileServant();
	
			 org.omg.CORBA.Object ref=rootpoa.servant_to_reference(FileServantVar);
			 FileInterfaceServer href=FileInterfaceServerHelper.narrow(ref);
	
			 org.omg.CORBA.Object objRef=orb.resolve_initial_references("NameService");
			 NamingContextExt ncRef=NamingContextExtHelper.narrow(objRef);
			 String name="FileTransfertServer";
			 NameComponent path[]=ncRef.to_name(name);
			 ncRef.rebind(path, href);
	
			 System.out.println("\nServer 'FileTransfertServer' is waiting ...\n");
	
			 orb.run();
		}
	
		catch(Exception e)
		{
			System.err.println("ERROR : "+e);
			e.printStackTrace(System.out);
		}
	
		System.out.println("\nServer 'FileTransfertServer' is not working !!");
   }
}