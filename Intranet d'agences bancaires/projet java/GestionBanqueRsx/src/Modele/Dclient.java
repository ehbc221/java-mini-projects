package Modele;

import java.sql.SQLException;
import java.util.ArrayList;

public class Dclient extends Connexion{
	public Dclient(){
		super();
	}
	
	public boolean AddClient(Client client){
		
		try{
			super.st = super.con.prepareStatement("Insert into client(nom,prenom,adresse,num_agence) values(?,?,?,?)");
			super.st.setString(1, client.getNom());
			super.st.setString(2,client.getPrenoms());
			super.st.setString(3, client.getAdresseCli());
			super.st.setInt(4,client.getCodeAgence());
			super.st.executeUpdate();
			return true;
		}
		catch (SQLException e) {
			return false;
		}		
	}
	
	public ArrayList<Client> getClientList(){
		ArrayList<Client> listeCl = new ArrayList<Client>();
		try {
			super.st = super.con.prepareStatement("Select c.num_cli,c.nom,c.prenom,c.adresse,a.libele,a.num_agence from client c inner join agence a on a.num_agence=c.num_agence");
			super.rs = super.st.executeQuery();
			while (super.rs.next()){
				Client cl1 = new Client();
				cl1.setNumClient(super.rs.getInt("num_cli")); 
				cl1.setNom(super.rs.getString("nom"));
				cl1.setPrenoms(super.rs.getString("prenom"));
				cl1.setAdresseCli(super.rs.getString("adresse"));
				cl1.setNomAgence(super.rs.getString("libele"));
				cl1.setCodeAgence(super.rs.getInt("num_agence"));
				listeCl.add(cl1);
			}
		} catch (Exception e) {
//			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return listeCl;
	}
	
	public ArrayList<Client> getClientList(int codeAg){
		ArrayList<Client> listeCl = new ArrayList<Client>();
		try {
			super.st = super.con.prepareStatement("Select * from Client where num_agence=? order by nom");
			super.st.setInt(1, codeAg);
			super.rs = super.st.executeQuery();
			while (super.rs.next()){
				Client cl1 = new Client();
				cl1.setNumClient(super.rs.getInt("num_cli"));
				cl1.setNom(super.rs.getString("nom"));
				cl1.setPrenoms(super.rs.getString("prenom"));
				cl1.setAdresseCli(super.rs.getString("adresse"));
				cl1.setCodeAgence(super.rs.getInt("num_agence"));
				listeCl.add(cl1);
			}
		} catch (Exception e) {
//			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return listeCl;
	}
	
	public ArrayList<Client> getClientList(String nomAgence){
		ArrayList<Client> listeCl = new ArrayList<Client>();
		try {
			super.st = super.con.prepareStatement("Select c.* from Client c inner join agence a on c.num_agence=a.num_agence where libele=? order by c.nom");
			super.st.setString(1, nomAgence);
			super.rs = super.st.executeQuery();
			while (super.rs.next()){
				Client cl1 = new Client();
				cl1.setNumClient(super.rs.getInt("num_cli"));
				cl1.setNom(super.rs.getString("nom"));
				cl1.setPrenoms(super.rs.getString("prenom"));
				cl1.setAdresseCli(super.rs.getString("adresse"));
				cl1.setCodeAgence(super.rs.getInt("num_agence"));
				listeCl.add(cl1);
			}
		} catch (Exception e) {
//			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return listeCl;
	}
	
	//methodes pour rechercher un Client (+ 1e surcharge);
	public Client FindClient(int numCli){
		Client cl1 = null;
		try {
			super.st = super.con.prepareStatement("Select * from Client where num_cli=?");
			super.st.setInt(1, numCli);
			super.rs = super.st.executeQuery();
			if (super.rs.next()){
				cl1 = new Client();
				cl1.setNumClient(super.rs.getInt("num_cli"));
				cl1.setNom(super.rs.getString("nom"));
				cl1.setPrenoms(super.rs.getString("prenom"));
				cl1.setAdresseCli(super.rs.getString("adresse"));
				cl1.setCodeAgence(super.rs.getInt("num_agence"));				
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return cl1;
	}
	
	public void UpdateClient(Client cln){
		try{
			super.st = super.con.prepareStatement("Update client set nom=?, prenom=?, num_agence=? where num_cli=?");
			super.st.setString(1, cln.getNom());
			super.st.setString(2, cln.getPrenoms());
			super.st.setInt(3, cln.getCodeAgence());
			super.st.setInt(4,cln.getNumClient());
			super.st.executeUpdate();			
		}
		catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public ArrayList<Compte> FindCompteByCli(int numCli){
		ArrayList<Compte> listecpt = new ArrayList<Compte>();
			try {
				super.st = super.con.prepareStatement("Select * from Compte where num_cli=?");
				super.st.setInt(1, numCli);
				super.rs = super.st.executeQuery();
				while (super.rs.next()){
					Compte cpt = new Compte();						
					cpt.setNumComte(super.rs.getString("num_cpt"));
					cpt.setSensCompte(super.rs.getString("sens"));
					cpt.setSoldeCompte(super.rs.getInt("solde"));
					cpt.setNumClient(super.rs.getInt("num_cli"));
					listecpt.add(cpt);
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			return listecpt;
		}
	
	public int GetMaxId(){
		int maxid=0;
		try {
			super.st = super.con.prepareStatement("Select max(num_cli) as maxid from client");
			super.rs = super.st.executeQuery();
			if (super.rs.next()){
				maxid = super.rs.getInt("maxid");					
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return maxid;
	}
}
