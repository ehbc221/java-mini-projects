package Modele;

import java.io.Serializable;


@SuppressWarnings("serial")
public class Agence implements Serializable {
	
	
	/**
	 * 
	 */
//	private static final long serialVersionUID = 1L;
	//attributs du javabean
	private String codeAgence;
	private String libAgence;
	private String adresseAgence;
	
	public Agence(){}
	
	public Agence(String pLibAgence, String pAdresseAgence){
		this.libAgence = pLibAgence;
		this.adresseAgence = pAdresseAgence;
	}
	
	public Agence(String pCodeAgence, String pLibAgence, String pAdresseAgence){
		this.codeAgence = pCodeAgence;
		this.libAgence = pLibAgence;
		this.adresseAgence = pAdresseAgence;
	}
	
	//Accesseurs et mutateurs
	public String getCodeAgence() {
		return codeAgence;
	}
	public void setCodeAgence(String codeAgence) {
		this.codeAgence = codeAgence;
	}
	public String getLibAgence() {
		return libAgence;
	}
	public void setLibAgence(String libAgence) {
		this.libAgence = libAgence;
	}
	public String getAdresseAgence() {
		return adresseAgence;
	}
	public void setAdresseAgence(String adresseAgence) {
		this.adresseAgence = adresseAgence;
	}	
}
