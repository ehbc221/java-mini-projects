package Modeles;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CountUser {
	
	static Statement St ;
	public ResultSet rs;
	public Integer nombre;

	public CountUser() throws SQLException{
		String req = "select count(*) from dbannonce.user";
		
		St = ConnexionDB.Con.createStatement();
		try{
			rs =(ResultSet)St.executeQuery(req);
			while(rs.next()){
				nombre=rs.getInt(1);
			}
		}
		catch (Exception e) {
			System.out.println("Erreur dans la requete d'affichage du nombre de lignes des annnonces immobilier");
		}
	}
}
