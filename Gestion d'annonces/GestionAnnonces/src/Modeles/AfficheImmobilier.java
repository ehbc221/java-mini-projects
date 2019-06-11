package Modeles;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AfficheImmobilier {
	
	static Statement St ;
	public ResultSet rs;

	public AfficheImmobilier(Integer num, Integer nbr) throws SQLException{
		String req = "select * from dbannonce.immobilier,dbannonce.annonce where annonce.IdAnnonce=immobilier.IdAnnonce limit "+num+","+nbr+"";
		
		St = ConnexionDB.Con.createStatement();
		try{
			rs =(ResultSet)St.executeQuery(req);
		}
		catch (Exception e) {
			System.out.println("Erreur dans la requete d'affichage des annnonces immobilier");
		}
	}
}
