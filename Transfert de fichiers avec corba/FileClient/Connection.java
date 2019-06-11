import org.omg.CosNaming.*;
//import org.omg.CosNaming.NamingContextPackage.*;
import org.omg.CORBA.*;

public class Connection
{
    static FileInterfaceServer myReference;
	ORB orb = null;

    public Connection()
    {}

    public FileInterfaceServer OpenConnection(String args[])
    {
        try
        {
           orb=ORB.init(args, null);
           org.omg.CORBA.Object objRef=orb.resolve_initial_references("NameService");
           NamingContextExt ncRef=NamingContextExtHelper.narrow(objRef);

           String name="FileTransfertServer";

           myReference=FileInterfaceServerHelper.narrow(ncRef.resolve_str(name));
        }

        catch(Exception e)
        {
            //System.out.println("ERROR : "+e.getMessage());
            //e.printStackTrace(System.out);
        }

        return myReference;
    }
}