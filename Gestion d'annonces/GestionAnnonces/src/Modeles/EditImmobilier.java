package Modeles;

import java.io.PrintStream;
import java.sql.*;
import java.util.Set;

import javax.print.attribute.standard.PrinterInfo;

import Modeles.ConnexionDB;

public class EditImmobilier {
	
	static Statement St;
	static Statement St1;
	public String Edit_immobilier_ok;

	public EditImmobilier(Integer IdAnnonce,Integer IdAI,String Type,String TxtI,String AdresseI,String VilleI,String RegionI,String PaysI,String Date,String Titre) throws SQLException{
		St = ConnexionDB.Con.createStatement();
		St1 = ConnexionDB.Con.createStatement();
		
	
		try{
			
			try{
				St.executeUpdate("Update dbannonce.annonce set annonce.DateAnnonce='"+Date+"', annonce.TitreAnnonce='"+Titre+"' where annonce.IdAnnonce ="+IdAnnonce+"");
			}catch(Exception e){
				System.out.println("probleme de modification dans la table annonce");
			}
			
			
			
			try{				
				St1.executeUpdate("Update dbannonce.immobilier set TypeI='"+Type+"',TxtI='"+TxtI+"',Adresse='"+AdresseI+"',Ville='"+VilleI+"',Region='"+RegionI+"',Pays='"+PaysI+"' where IdAI = "+IdAI+"");
				Edit_immobilier_ok="La modification de l'annonce immobilier est effectuée avec succées";
			}catch(Exception E){
				System.out.println("Probleme de modification d'immobilier");
			}
		}
		catch(Exception E){
			Edit_immobilier_ok="La modification de l'annonce immobilier n'est pas effectuée a cause d'un problème dans la requete";	
			System.out.println("Erreurs dans la reqeute AjouterImmobilier");
		}
	}
}
