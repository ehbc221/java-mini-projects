package Modele;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Operation implements Serializable {
	
	//Attributs du javabean
	private int idOp;
	private String libOp;
	private String sensOp;
	private String dateOp;
	private int montantOp;
	private String numCompteOp;
	
	public Operation(){}
	
	public Operation(String pLibeleOperation, String pSensOperation, int pMontantOperation, String pNumCompte){
		
		libOp = pLibeleOperation;
		sensOp = pSensOperation.trim();
		montantOp = pMontantOperation;
		numCompteOp = pNumCompte.trim();
	}
	
	public Operation(int pNumOperation, String pLibeleOperation, String pSensOperation, int pMontantOperation, String pDateOperation){
		
		idOp = pNumOperation;
		libOp = pLibeleOperation;
		sensOp = pSensOperation.trim();
		montantOp = pMontantOperation;
		dateOp = pDateOperation.trim();
	}
	
	//Accesseurs et mutateurs
	public int getIdOp() {
		return idOp;
	}
	public void setIdOp(int numOp) {
		this.idOp = numOp;
	}
	public String getLibOp() {
		return libOp;
	}
	public void setLibOp(String libOp) {
		this.libOp = libOp;
	}
	public String getSensOp() {
		return sensOp;
	}
	public void setSensOp(String sensOp) {
		this.sensOp = sensOp;
	}
	public String getDateOp() {
		return dateOp;
	}
	public void setDateOp(String dateOp) {
		this.dateOp = dateOp;
	}
	public int getMontantOp() {
		return montantOp;
	}
	public void setMontantOp(int montantOp) {
		this.montantOp = montantOp;
	}
	public String getNumCompteOp() {
		return numCompteOp;
	}
	public void setNumCompteOp(String numCompteOp) {
		this.numCompteOp = numCompteOp;
	}

}