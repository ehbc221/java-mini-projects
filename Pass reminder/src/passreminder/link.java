package passreminder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class link 
{
    private static String url = "jdbc:hsqldb:file:MobileBd"; //vous pouvez changer MobileBd et mettre le nom que vous voulez
    private static String user = "cervo"; //vous pouvez changer à votre guise
    private static String pass = "cervo"; //vous pouvez changer à votre guise
    private static Connection connect;
    
    
     public static Connection getInstance()
     {
        if(connect == null)
        {
           try {
                 connect = DriverManager.getConnection(url, user, pass);
               }
           catch (SQLException e) 
           {}
        }  
  
         return connect; 
     } 
    
}
