package Modeles;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AfficheVehicule{
	
	static Statement St;
	public ResultSet rs;

	public AfficheVehicule(Integer num, Integer nbr) throws SQLException{
		String req = "select * from dbannonce.vehicule,dbannonce.annonce where annonce.IdAnnonce=vehicule.IdAnnonce limit "+num+","+nbr+"";
		
		St = ConnexionDB.Con.createStatement();
		try{
			rs =(ResultSet)St.executeQuery(req);
		}
		catch (Exception e) {
			System.out.println("Erreur dans la requete d'affichage des annnonces vehicule");
		}
	}
}
