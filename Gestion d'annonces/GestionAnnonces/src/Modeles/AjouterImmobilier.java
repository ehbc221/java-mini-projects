package Modeles;

import java.io.PrintStream;
import java.sql.*;
import java.util.Set;

import javax.print.attribute.standard.PrinterInfo;

import Modeles.ConnexionDB;

public class AjouterImmobilier {
	
	static Statement St;
	static Statement St1;
	static Statement St2;
	public String immobilier_ok;

	public AjouterImmobilier(String Type,String TxtI,String AdresseI,String VilleI,String RegionI,String PaysI,Integer IdUser,String Date,String Titre) throws SQLException{
		St = ConnexionDB.Con.createStatement();
		St1 = ConnexionDB.Con.createStatement();
		St2 = ConnexionDB.Con.createStatement();
		
		ResultSet rs ;
		Integer IdAnnonce=0;
		try{
			//ajouter dans la table annonce
			try{
				St.executeUpdate("Insert into dbannonce.annonce(IdUser,DateAnnonce,TitreAnnonce) values("+IdUser+",'"+Date+"','"+Titre+"')");
			}catch(Exception e){
				System.out.println("probleme d'jout dans la table annonce");
			}
			//recuperer l'Id de l'annonce
			try{
				rs = St1.executeQuery("select max(IdAnnonce) from dbannonce.annonce");
				while(rs.next()){
					IdAnnonce=rs.getInt(1);
				}				
			}catch(Exception e){
				System.out.println("probleme de requete de selection du dernier IdAnnonce");
			}
			
			//ajouter l'annonce dans la table immobilier
			try{				
				St2.executeUpdate("insert into dbannonce.immobilier(IdAnnonce,TypeI,TxtI,Adresse,Ville,Region,Pays) values ("+IdAnnonce+",'"+Type+"','"+TxtI+"','"+AdresseI+"','"+VilleI+"','"+RegionI+"','"+PaysI+"')");
				immobilier_ok="L'ajout de l'annonce immobilier est effectuée avec succées";
			}catch(Exception E){
				System.out.println("Probleme d'jout d'immobilier");
			}
		}
		catch(Exception E){
			immobilier_ok="L'ajout de l'annonce immobilier n'est pas effectuée a cause d'un problème dans la requete";	
			System.out.println("Erreurs dans la reqeute AjouterImmobilier");
		}
	}
}
