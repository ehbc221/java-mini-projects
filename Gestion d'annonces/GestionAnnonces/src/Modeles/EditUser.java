package Modeles;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class EditUser {

	static Statement St ;
	public ResultSet rs;
	public String edit_ok;
	
	public EditUser(Integer IdUser, String Nom, String Prenom, String NumTel, String Adresse, String Email, String Ville, String Region, String Pays,String Password) throws SQLException{
		//System.out.println(IdUser);
		String req = "update dbannonce.user set Nom='"+Nom+"',Prenom='"+Prenom+"',NumTel='"+NumTel+"',Adresse='"+Adresse+"',Email='"+Email+"',Ville='"+Ville+"',Region='"+Region+"',Pays='"+Pays+"',Password='"+Password+"' where IdUser="+IdUser+"";
		St = ConnexionDB.Con.createStatement();
		try{
			St.executeUpdate(req);
			edit_ok="la modification est effectu�e avec succ�s";
		}
		catch (Exception e) {
			edit_ok="la modification n'est pas effectu�e a cause d'un probl�me dans la requete";
		}
		
	}
}
