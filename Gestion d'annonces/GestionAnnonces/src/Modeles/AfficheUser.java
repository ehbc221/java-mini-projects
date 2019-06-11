package Modeles;

import java.sql.*;

import Modeles.ConnexionDB;
public class AfficheUser {
	
	
	static Statement St ;
	public ResultSet rs;

	public AfficheUser(Integer num,Integer nbr) throws SQLException{
		String req = "select * from dbannonce.user limit "+num+","+nbr+"";
		St = ConnexionDB.Con.createStatement();
		try{
			rs =(ResultSet)St.executeQuery(req);
		}
		catch (Exception e) {
			System.out.println("Erreur dans la requete d'affichage des utilisateurs");
		}
		
	}
}
