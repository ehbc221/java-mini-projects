package Modeles;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AnnonceUser {
	
	static Statement St ;
	public ResultSet rs_imm;
	
	public AnnonceUser(Integer IdUser) throws SQLException{
		St=ConnexionDB.Con.createStatement();
		try{
			rs_imm=St.executeQuery("select * from  dbannonce.immobilier,dbannonce.annonce where annonce.IdAnnonce=immobilier.IdAnnonce and annonce.IdUser="+IdUser+"");
			
		}catch(Exception E){
			
		}
	}

}
