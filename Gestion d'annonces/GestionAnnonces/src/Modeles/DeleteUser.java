package Modeles;

import java.sql.*;
import java.sql.SQLException;

import com.mysql.jdbc.Statement;

public class DeleteUser {
	
	static Statement St;
	
	public DeleteUser(Integer IdUser) throws SQLException{
		
		St = (Statement) ConnexionDB.Con.createStatement();
		try{
			St.executeUpdate("delete from dbannonce.user where IdUser='"+IdUser+"'");
			
		}catch(Exception E){
			System.out.println("Impossible de supprimer l'utlisateur");
		}
	}

}
