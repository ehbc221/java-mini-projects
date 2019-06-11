package Modele;

import java.sql.SQLException;
import java.util.*;

public class Dagence extends Connexion {	
	public Dagence(){
		super();
	}
	
	public boolean addAgence(Agence ag){
		
		try{
			super.st=super.con.prepareStatement("Insert into agence(libele,adresse)values(?,?)");
			super.st.setString(1, ag.getLibAgence());
			super.st.setString(2, ag.getAdresseAgence());
			super.st.executeUpdate();
			return true;
		}
		catch (SQLException e) {
			return false;
//			e.printStackTrace();
		}		
	}
	
	public ArrayList<Agence> getAgencesList(){
		ArrayList<Agence> listeAg = new ArrayList<Agence>();
		try{
			super.st=super.con.prepareStatement("Select * from agence");
			super.rs=super.st.executeQuery();
			while (super.rs.next()){
				Agence ag = new Agence();
				ag.setCodeAgence(super.rs.getString("num_agence"));
				ag.setLibAgence(super.rs.getString("libele"));
				ag.setAdresseAgence(super.rs.getString("adresse"));
				listeAg.add(ag);				
			}			
			
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return listeAg;
	}
}


