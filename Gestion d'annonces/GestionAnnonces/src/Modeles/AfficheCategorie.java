package Modeles;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AfficheCategorie {
	static Statement St ;
	public ResultSet rs;
	
	public AfficheCategorie() throws SQLException{
		
	
		
			String req = "Select * from dbannonces.categorie_user";
			St = ConnexionDB.Con.createStatement();
			try{
				rs =(ResultSet)St.executeQuery(req);
			}
			catch (Exception e) {
				System.out.println("Erreur dans la requete dans la requette d'affichage de des categorie");
			}
		}
	}