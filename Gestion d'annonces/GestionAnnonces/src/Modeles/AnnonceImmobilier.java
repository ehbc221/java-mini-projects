package Modeles;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AnnonceImmobilier {
	
	static Statement St ;
	public ResultSet rs;
	
	public AnnonceImmobilier(Integer IdAI) throws SQLException{
		St=ConnexionDB.Con.createStatement();
		try{
			rs=St.executeQuery("select * from  dbannonce.immobilier,dbannonce.annonce where annonce.IdAnnonce=immobilier.IdAnnonce and immobilier.IdAI="+IdAI+"");
			
		}catch(Exception E){
			
		}
	}

}
