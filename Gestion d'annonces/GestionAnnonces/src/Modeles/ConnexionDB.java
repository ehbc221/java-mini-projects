package Modeles;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import sun.applet.resources.MsgAppletViewer;

public class ConnexionDB {
	
	public static Connection Con ;
	static Statement St ;
	
	

	public ConnexionDB(){
		
		String driver="com.mysql.jdbc.Driver";
		String url="jdbc:mysql://localhost:3306/dbannonce";
		String login="root";
		String pswd="root";
		        
		try
		   {
		       	Class.forName(driver);
		       	System.out.println(" Driver OK");
		   }
		catch(ClassNotFoundException E)
	    	{
				System.out.println(" Problème de chargement de driver");
	    	}
		try
		    {
		        Con = DriverManager.getConnection(url,login ,pswd );
		        System.out.println("Connexion établie");
		    }
		catch(SQLException E)
		    {
		        System.out.println(" Problème de connexion");
		    }
				
	}
	

	
	
	

}
