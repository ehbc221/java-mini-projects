package Modeles;

import java.io.PrintStream;
import java.sql.*;
import java.util.Set;

import javax.print.attribute.standard.PrinterInfo;

import Modeles.ConnexionDB;

public class inscription {
	
	static Statement St;
	public String ajouter_ok;
	
	public inscription(String nom,String prenom,String numtel,String adresse,String email,String ville,String pays,String region,String sexe, Integer IdCat, String Pseudo, String Passe) throws SQLException{
		St = ConnexionDB.Con.createStatement();
		try{
			St.executeUpdate("insert into dbannonce.user(Nom,Prenom,NumTel,Adresse,Email,Ville,Pays,Region,Sexe,IdCat,UserName,Password) values ('"+nom+"','"+prenom+"','"+numtel+"','"+adresse+"','"+email+"','"+ville+"','"+pays+"','"+region+"','"+sexe+"','"+IdCat+"','"+Pseudo+"','"+Passe+"')");
			ajouter_ok = "L'inscription est effectuée avec succés!!!!";
		}
		catch(Exception E){
			ajouter_ok = "L'inscription n'est effectuée pas a cause d'un problème dans la reqeute!!!!";
			System.out.println("Erreurs dans la reqeute d'inscription");
		}
	}

}
