package Modele;

import java.sql.*;
import java.sql.Date;
import java.util.*;

public class Doperation extends Connexion{
	
	private String dn="";
	
	//constructeur par défaut
	public Doperation(){
		super();
	}
	public String MysqlToString(String source){
		String res=null;
		res=source.substring(8)+"-"+source.substring(5,7)+"-"+source.substring(0,4);
		return res;
	}
	//methodes de conversion de chaines en date et inversément
	public String StringToMysql(String source){
		String res=null;
		res=source.substring(6)+"-"+source.substring(3,5)+"-"+source.substring(0,2);
		return res;
	}
		
	public boolean addOperation(Operation op){
		try{
			super.st = super.con.prepareStatement("Insert into operation(libele,montant,sens,date_op,num_cpt) values(?,?,?,CURDATE(),?)");			
			super.st.setString(1, op.getLibOp());
			super.st.setString(3, op.getSensOp());							
			super.st.setInt(2, op.getMontantOp());
			super.st.setString(4, op.getNumCompteOp());
			super.st.executeUpdate();
			return true;
			//:TODO updatecompte
		}
		catch (SQLException e) {
//			System.out.println(e.getMessage());
//			e.printStackTrace();
			return false;
		}
	}
	
	public ArrayList<Operation> getReleveCompte(String numCompte){
		ArrayList<Operation> listeop = new ArrayList<Operation>();		
		try {
			super.st = super.con.prepareStatement("Select * from Operation where num_cpt=?");
			super.st.setString(1, numCompte);
			super.rs = super.st.executeQuery();
			while (super.rs.next()){
				Operation op = new Operation();				
				op.setIdOp(super.rs.getInt("num_op"));
				op.setLibOp(super.rs.getString("libele"));
				op.setSensOp(super.rs.getString("sens"));
				op.setMontantOp(super.rs.getInt("montant"));	
				op.setDateOp(MysqlToString(super.rs.getString("date_op")));
				op.setNumCompteOp(super.rs.getString("num_cpt"));
				listeop.add(op);
			}
		} catch (SQLException e) {
//			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return listeop;
	}
}
