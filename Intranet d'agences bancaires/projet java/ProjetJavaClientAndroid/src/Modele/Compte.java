package Modele;

import java.io.Serializable;


@SuppressWarnings("serial")
public class Compte implements Serializable {
	
	/**
	 * 
	 */
//	private static final long serialVersionUID = 1L;
	//attributs du javabean
	private String numCompte;
	private String libeleCompte;
	private String sensCompte;
	private int soldeCompte;
	private int numClient;
	
	public Compte(){}
	
	public Compte(String pNumCompte, String pLibeleCompte, String pSens, int pSolde){
		numCompte = pNumCompte;
		libeleCompte = pLibeleCompte;
		sensCompte = pSens.trim();
		soldeCompte = pSolde;	
	}
	public String getNumCompte() {
		return numCompte;
	}
	public void setNumComte(String numComte) {
		this.numCompte = numComte;
	}
	
	public String getSensCompte() {
		return sensCompte;
	}
	public void setSensCompte(String sensCompte) {
		this.sensCompte = sensCompte;
	}
	
	public String getLibeleCompte() {
		return libeleCompte;
	}
	public void setLibeleCompte(String libeleCompte) {
		this.libeleCompte = libeleCompte;
	}
	public int getNumClient() {
		return numClient;
	}
	public void setNumClient(int numClient) {
		this.numClient = numClient;
	}
	public int getSoldeCompte() {
		return soldeCompte;
	}
	public void setSoldeCompte(int soldeCompte) {
		this.soldeCompte = soldeCompte;
	}
	
}
