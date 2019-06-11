package Modeles;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ProfilUser {
	
	static Statement St ;
	public ResultSet rs;
	
	public ProfilUser(Integer IdUser) throws SQLException{
		
		String req = "select * from dbannonce.user where IdUser = "+IdUser+"";
		St = ConnexionDB.Con.createStatement();
		try{
			rs =(ResultSet)St.executeQuery(req);
		}
		catch (Exception e) {
			System.out.println("Erreur dans la requete d'affichage des utilisateurs");
		}
		
	}
	
	public void MesAnnoncesImmobilier(Integer IdUser){
		
	}
	
	public void MesAnnoncesVehicule(Integer IdUser){
		
	}
	
	public void MesAnnoncesService(Integer IdUser){
	
	}
	
	public void MesAnnoncesEmploi(Integer IdUser){
		
	}
	
	public void MesAnnoncesAutres (Integer IdUser){
		
	}

}
