package Modeles;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Authentification {

	static Statement St ;
	public ResultSet rs;
	public Authentification(){
		
	}

	public Authentification(String UserN, String PassW) throws SQLException{
		String req = "select UserName,Password,IdCat,IdUser from dbannonce.user  where UserName='"+UserN+"' and Password='"+PassW+"'";
		St = ConnexionDB.Con.createStatement();
		try{
			rs =(ResultSet)St.executeQuery(req);
		}
		catch (Exception e) {
			System.out.println("Erreur dans la requete de l'athentfication");
		}
		
	}
	
	public String Existe_User(String UserName) throws SQLException{
		String req = "select UserName from dbannonce.user  where UserName='"+UserName+"'";
		St = ConnexionDB.Con.createStatement();
		try{
			rs =(ResultSet)St.executeQuery(req);
		}
		catch (Exception e) {
			System.out.println("Erreur dans la requete de l'athentfication");
		}
		return UserName;
		
	}
}
