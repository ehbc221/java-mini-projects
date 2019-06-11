package Modeles;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AjouterVehicule {
	static Statement St;
	static Statement St1;
	static Statement St2;
	public String vehicule_ok;

	public AjouterVehicule(String Marque,String Modele, String TypeV, String TxtV, Integer IdUser,String Date, String Titre) throws SQLException{
		St = ConnexionDB.Con.createStatement();
		St1 = ConnexionDB.Con.createStatement();
		St2 = ConnexionDB.Con.createStatement();
		
		ResultSet rs ;
		Integer IdAnnonce=0;
		try{
			
			try{
				St.executeUpdate("Insert into dbannonce.annonce(IdUser,DateAnnonce,TitreAnnonce) values("+IdUser+",'"+Date+"','"+Titre+"')");
			}catch(Exception e){
				System.out.println("probleme d'jout dans la table annonce");
			}
			
			try{
				rs = St1.executeQuery("select max(IdAnnonce) from dbannonce.annonce");
				while(rs.next()){
					IdAnnonce=rs.getInt(1);
				}
				
			}catch(Exception e){
				System.out.println("probleme de requete de selection du dernier IdAnnonce");
			}
			try{
				St2.executeUpdate("insert into dbannonce.vehicule(IdAnnonce,Marque,Modele,TypeV,TxtV) values ("+IdAnnonce+",'"+Marque+"','"+Modele+"','"+TypeV+"','"+TxtV+"')");
				vehicule_ok="L'ajout de l'annonce véhicule est effectuée avec succées";
			}catch(Exception E){
				System.out.println("Probleme d'jout de vehicule");
			}
		}
		catch(Exception E){
			vehicule_ok="L'ajout de l'annonce vehicule n'est pas effectuée a cause d'un problème dans la requete";	
			System.out.println("Erreurs dans la reqeute AjouterVéhicule");
		}
	}

}
