package Modele;

import java.io.Serializable;


@SuppressWarnings("serial")
public class Client implements Serializable {
	
	/**
	 * 
	 */
//	private static final long serialVersionUID = 1L;
	//Attributs du Javabeen
	private int numClient; 
	private String nom;
	private String prenoms;
	private int codeAgence;
	private String nomAgence;
	private String adresseCli;
	
	public Client(){}
	
	public Client(int pNum, String pNom, String pPrenom, String pAdresse, String pNomAgence){
		
		numClient = pNum;
		nom = pNom.trim();
		prenoms = pPrenom.trim();
		adresseCli = pAdresse.trim();
		nomAgence = pNomAgence.trim();
	}
	
	public String getAdresseCli() {
		return adresseCli;
	}
	public void setAdresseCli(String adresseCli) {
		this.adresseCli = adresseCli;
	}
	public String getNomAgence() {
		return nomAgence;
	}
	public void setNomAgence(String nomAgence) {
		this.nomAgence = nomAgence;
	}
	//Accesseurs et mutateurs
	public int getNumClient() {
		return numClient;
	}
	public void setNumClient(int numClient) {
		this.numClient = numClient;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getPrenoms() {
		return prenoms;
	}
	public void setPrenoms(String prenom) {
		this.prenoms = prenom;
	}
	public int getCodeAgence() {
		return codeAgence;
	}
	public void setCodeAgence(int codeAgence) {
		this.codeAgence = codeAgence;
	}
}
