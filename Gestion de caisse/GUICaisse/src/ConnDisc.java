
import java.sql.*;
import javax.swing.JOptionPane;
public class ConnDisc {
	private static Connection connexion=null;
	private static String url="jdbc:mysql://localhost:3306/";
	private static String dbName= "Stagiaire";
	private static String driver= "com.mysql.jdbc.Driver";
	private static String userName= "root";
	private static String password= "";
	
	public static void connect(){
		try {
          
            Class.forName(driver);
            connexion= DriverManager.getConnection(url + dbName, userName, password);
		}
		catch(Exception e){
			e.printStackTrace();
			
		} 
	}
	public static void discconnect(){
		try{
			Class.forName(driver);
			connexion.close();
		}
		catch(Exception e){
			JOptionPane.showMessageDialog(null, "Log out impossible","Message d’avertissement", JOptionPane.ERROR_MESSAGE);
		}
    }
	public static Connection getConnexion() {
		return connexion;
	}
}
