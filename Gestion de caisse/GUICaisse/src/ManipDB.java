import java.sql.*;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
public class ManipDB {
	String query;
	Statement stx;
	ResultSet rset;
	Connection conn;
	public ManipDB(){
		ConnDisc.connect();
		conn=ConnDisc.getConnexion();
		rset=null;
	}
	public ResultSet afficherCaissier(int id){
		try{
			stx=conn.createStatement();
			query="SELECT idCaissier,nomCaissier,passwordCaissier FROM Caissier"+" WHERE idCaissier="+id+"";
			rset=stx.executeQuery(query);
			
		}
		catch(SQLException ex){
			ex.printStackTrace();
			
			
		}
		return rset;
	}
	public DefaultTableModel  afficherStock(){
		DefaultTableModel dt = new DefaultTableModel();
		dt.addColumn("Stock Unitaire");
		dt.addColumn("Stock Lot");
		dt.addColumn("chiffre d'affaire Article");
		dt.addColumn("chiffre d'affaire Lot");
		dt.addColumn("chiffre d'affaire global");
		try{
			
			stx=conn.createStatement();
			query="SELECT * FROM Stock";
			rset=stx.executeQuery(query);
			while(rset.next()){
				Object []tableau={rset.getInt("stockU"),rset.getInt("stockLot"),rset.getDouble("chiffreArticle"),rset.getDouble("chiffreLot"),
						rset.getDouble("chiffreAffaire")};
				dt.addRow(tableau);
			}
		}
		catch(SQLException ex){
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null,"ERROR","Message d’avertissement",JOptionPane.ERROR_MESSAGE);
			
		}
		return dt;
	}
	public void acheterArticle(int nb,String nom){
		try{
			double prixarticle=0.0;
			double chiffreaffaire=0.0;
			int stocku=0;
			
			stx=conn.createStatement();
			query="SELECT prixArticle FROM Article WHERE nomArticle="+nom+" ";
			rset=stx.executeQuery(query);
			
			while(rset.next()){
				prixarticle=rset.getDouble("prixArticle");
			}
			
			query="SELECT stockU,chiffreAffaire FROM Stock";
			rset=stx.executeQuery(query);
			while(rset.next()){
				chiffreaffaire=rset.getDouble("chiffreAffaire");
				stocku=rset.getInt("stockU");
			}
			
			double prixArticle=prixarticle*nb;
			chiffreaffaire+=prixArticle;
			stocku-=nb;
			
			if(stocku>0){
				query="UPDATE Stock SET chiffreAffaire="+chiffreaffaire+",stockU="+stocku+",chiffreArticle="+prixArticle+"";
				stx.executeUpdate(query);
			}
			else{
				JOptionPane.showMessageDialog(null,"stock n'est plus disponible","Message d’avertissement",JOptionPane.ERROR_MESSAGE);
			}
			
		}
		catch(SQLException ex){
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null,"ERROR","Message d’avertissement",JOptionPane.ERROR_MESSAGE);	
		}
	  }
		
	
	public void acheterLot(int nb,String nom){
		try{
			double prixlot=0.0;
			double chiffreaffaire=0.0;
			int stocku=0;
			int stocklot = 0;
			stx=conn.createStatement();
			query="SELECT prixLot FROM Lot"+" WHERE nomLot="+nom+"";
			rset=stx.executeQuery(query);
			while(rset.next()){
				prixlot=rset.getDouble("prixLot");
			}
			query="SELECT stockU,stockLot,chiffreAffaire FROM Stock";
			rset=stx.executeQuery(query);
			while(rset.next()){
				chiffreaffaire=rset.getDouble("chiffreAffaire");
				stocku=rset.getInt("stockU");
				stocklot=rset.getInt("stockLot");
			}
			double prixLot=prixlot*nb;
			chiffreaffaire+=prixLot;
			System.out.println(chiffreaffaire);
			//supposons qu'on a un lot de 3 articles.
			stocku-=(nb*3);
			stocklot-=nb;
			if(stocku>0)
			{
				query="UPDATE Stock SET chiffreAffaire="+chiffreaffaire+",stockU="+stocku+",stockLot="+stocklot+",chiffreLot="+prixLot+"";
				stx.executeUpdate(query);
			}
			else{
				JOptionPane.showMessageDialog(null,"stock n'est plus disponible","Message d’avertissement",JOptionPane.ERROR_MESSAGE);
			}
		}
		catch(SQLException ex){
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null,"stock n'est plus disponible","Message d’avertissement",JOptionPane.ERROR_MESSAGE);
			
		}
		
	}
	public DefaultTableModel afficherProduit(int code){
		DefaultTableModel dt = new DefaultTableModel();
		dt.addColumn("numero");
		dt.addColumn("Reference");
		dt.addColumn("Designation");
		dt.addColumn("Nom");
		dt.addColumn("Prix");
		try{
			
			stx=conn.createStatement();
			query="SELECT * FROM Article WHERE codeArticle="+code+" UNION SELECT * FROM Lot WHERE codeLot="+code+"";;
			rset=stx.executeQuery(query);
			while(rset.next()){
				Object []tableau={rset.getInt(1),rset.getString(2),rset.getString(3),rset.getString(4),rset.getDouble(5)};
				dt.addRow(tableau);
			}
		}
		catch(SQLException ex){
			ex.printStackTrace();
			
		}
		return dt;
		
	}
	
	public boolean verifier(int login,String password){
		boolean test=false;
		int log =0;
		String pass=null;
		try{
			stx=conn.createStatement();
			query="SELECT idCaissier,passwordCaissier FROM caissier";
			rset=stx.executeQuery(query);
			while(rset.next()|| (test==true)){
				log=rset.getInt("idCaissier");
				pass=rset.getString("passwordCaissier");
				if((log==login) && (pass.equals(password)))
					test=true;		
			}
		}
		catch(SQLException ex){
			ex.printStackTrace();
		}
		System.out.println(test);
		System.out.println(log);
		System.out.println(pass);
		return test;

	}
	
	
}
