package Modele;

import java.sql.SQLException;
import java.util.ArrayList;

public class Dcompte extends Connexion{
	public Dcompte(){
		super();
	}
	
	public boolean AddCompte(Compte c){		
		try{
			super.st = super.con.prepareStatement("Insert into Compte values(?,?,?,?)");
			super.st.setString(1, String.valueOf(c.getNumCompte()));
			super.st.setString(3,c.getSensCompte());
			super.st.setDouble(2,Double.valueOf(c.getSoldeCompte()));
			super.st.setInt(4,c.getNumClient());
			super.st.executeUpdate();
			return true;
		}
		catch (SQLException e) {
//			System.out.println(e.getMessage());
			return false;
		}		
	}

	public ArrayList<Compte> getCompteList(){
		ArrayList<Compte> listeCpt = new ArrayList<Compte>();
		try {
			super.st = super.con.prepareStatement("Select * from Compte");
			super.rs = super.st.executeQuery();
			while (super.rs.next()){
				Compte cpt = new Compte();
				cpt.setNumComte(super.rs.getString("num_cpt"));
				cpt.setSoldeCompte(super.rs.getInt("solde"));
				cpt.setSensCompte(super.rs.getString("sens"));
				cpt.setNumClient(super.rs.getInt("num_cli"));
				listeCpt.add(cpt);
			}
		} catch (Exception e) {
//			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return listeCpt;
	}
	
	public ArrayList<Compte> getCompteList(int numCli){
		ArrayList<Compte> listeCpt = new ArrayList<Compte>();
		try {
			super.st = super.con.prepareStatement("Select * from Compte where num_cli=?");
			super.st.setInt(1, numCli);
			super.rs = super.st.executeQuery();
			while (super.rs.next()){
				Compte cpt = new Compte();
				cpt.setNumComte(super.rs.getString("num_cpt"));
				cpt.setSoldeCompte(super.rs.getInt("solde"));
				cpt.setSensCompte(super.rs.getString("sens"));
				cpt.setNumClient(super.rs.getInt("num_cli"));
				listeCpt.add(cpt);
			}
		} catch (Exception e) {
//			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return listeCpt;
	}

	public Compte FindCompte(String numCpt){
		Compte cpt = null;
		if(this.Exists(numCpt)){
			try {
				super.st = super.con.prepareStatement("Select * from compte where num_cpt=?");
				super.st.setString(1, numCpt);
				super.rs = super.st.executeQuery();
				if (super.rs.next()){
					cpt = new Compte();
					cpt.setNumComte(super.rs.getString("num_cpt"));
					cpt.setSensCompte(super.rs.getString("sens"));
					cpt.setSoldeCompte(super.rs.getInt("solde"));
					cpt.setNumClient(super.rs.getInt("num_cli"));				
				}
			} catch (Exception e) {
				//			System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
		return cpt;
	}

	public boolean Exists(String numCpt){
		boolean res = false;
		try {
			super.st = super.con.prepareStatement("Select * from Compte where num_cpt=?");
			super.st.setString(1, numCpt);
			super.rs = super.st.executeQuery();
			if (super.rs.next()){
				res=true;
			}
		} catch (Exception e) {
//			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return res;
	}

	public double GetSolde(int numCpt){
		double solde=0;
		try{
			super.st = super.con.prepareStatement("Select solde from Compte where num_cpt=?");
			super.st.setInt(1, numCpt);
			super.rs = super.st.executeQuery();
			if (super.rs.next())
				solde=rs.getDouble("solde");			
		}
		catch (SQLException e) {
//			System.out.println(e.getMessage());
			e.printStackTrace();
		}			
		return solde;
	}		
	
	public boolean updateCompte(Compte compte){
		
		try {
			super.st = super.con.prepareStatement("update compte set solde=?, sens=? where num_cpt=?");
			super.st.setInt(1, compte.getSoldeCompte());
			super.st.setString(2, compte.getSensCompte());
			super.st.setString(3, compte.getNumCompte());
			super.st.executeUpdate();
			return true;
		}catch(Exception ex){
//			ex.printStackTrace();
			return false;
		}
	}
}

	

