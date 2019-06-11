package Modeles;

import java.sql.SQLException;
import java.sql.Statement;



public class DeleteToutImmobilier {

	static Statement St;
	static Statement St1;
	static Statement St2;
	public DeleteToutImmobilier(Integer IdAnnonce, Integer IdAI) throws SQLException{
		
		St = ConnexionDB.Con.createStatement();
		St1 = ConnexionDB.Con.createStatement();
		St2 = ConnexionDB.Con.createStatement();
		
		try{
			St.executeUpdate("delete from dbannonce.Immobilier where IdAI='"+IdAI+"'");
			St1.executeUpdate("delete from dbannonce.annonce where IdAnnonce='"+IdAnnonce+"'");
			
		}catch(Exception E){
			System.out.println("Impossible de supprimer l'annonce immobilier");
		}
	}
}
