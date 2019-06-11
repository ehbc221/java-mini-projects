import java.io.*;
import java.util.*;

public class FileServant extends FileInterfaceServerPOA
{
   private Hashtable users = new Hashtable();
   private static Vector UsersList=new Vector();

   public byte[] downloadFile(String fileName)
   {
      File file = new File(fileName);
      byte buffer[] = new byte[(int)file.length()];
      try
      {
         BufferedInputStream input = new
           BufferedInputStream(new FileInputStream(fileName));
         input.read(buffer,0,buffer.length);
         input.close();
      }
      catch(Exception e)
      {
         System.out.println("FileServant Error: "+e.getMessage());
         e.printStackTrace();
      }
      return(buffer);
   }

   public String[] getUserList ()
   {
		//Enumeration e = users.elements();
		String[] userlist=new String[UsersList.size()];
		//int ind=0;
		
		for(int ind=0; ind<UsersList.size(); ind++)
	 	{
		 	userlist[ind] = UsersList.elementAt(ind).toString();
		 	//ind++;
	 	}
		return userlist;
   }

   public String getUserString(String hostName)
   {
       return (String)users.get(hostName);
   }

   public void subscribe (String userString, String hostName)
   {
       System.out.println("New user connected !!\n\tUserName : "+hostName+"\n");
       users.put(hostName, userString);
       UsersList.addElement(hostName);
   }

    public void unsubscribe (String hostname)
    {
        System.out.println("User disconnected !!\n\tUserName : "+hostname+"\n");
        users.remove(hostname);
        UsersList.remove(hostname);
    }
}