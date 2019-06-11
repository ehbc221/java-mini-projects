package Modeles;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class mannonce {
	
	static Statement St ;
	public ResultSet rs;
	
	public mannonce(){
		
	}
	
	public void mannonceimmobilier(Integer IdAnnonce, Integer IdAI) throws SQLException{
		
		String req = "select * from dbannonce.immobilier,dbannonce.annonce where annonce.IdAnnonce=immobilier.IdAnnonce and immobilier.IdAI="+IdAI+" and annonce.IdAnnonce = "+IdAnnonce+"";
		 
		St = ConnexionDB.Con.createStatement();
		try{
			rs =(ResultSet)St.executeQuery(req);
		}
		catch (Exception e) {
			System.out.println("Erreur dans la requete d'affichage des annnonces immobilier");
		}
	}

}
