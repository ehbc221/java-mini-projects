package com.pack.entity;

public class Entete {
	private String hierarchieLevel1 = "Ministere des Enseignements Secondaires";
	private String hierarchieLevel2 = "Delegation Regionale du Centre";
	private String hierarchieLevel3 = "Delegation Departementale du Nyong et So'o";
	private String school = "Lycée Bilingue de Mbalmayo";
	
	private String country = "Republique du Cameroun";
	private String device = "Paix-Travail-Patrie";
	private String statusHeadmaster = "Proviseur";

	private String linkLogo = "";
	private String linkFiligrane = "";
	
	private String bp = "159";
	private String anneeScolaire = "2012/2013";
	private String numero = "(+237)22 56 33 23";
	

	
	public String getBp() {
		return bp;
	}
	public Entete setBp(String bp) {
		this.bp = bp;
		return this;
	}
	public String getAnneeScolaire() {
		return anneeScolaire;
	}
	public Entete setAnneeScolaire(String anneeScolaire) {
		this.anneeScolaire = anneeScolaire;
		return this;
	}
	public String getNumero() {
		return numero;
	}
	public Entete setNumero(String numero) {
		this.numero = numero;
		return this;
	}
	public String getLinkFiligrane() {
		return linkFiligrane;
	}
	public Entete setLinkFiligrane(String linkFiligrane) {
		this.linkFiligrane = linkFiligrane;
		return this;
	}
	public String getLinkLogo() {
		return linkLogo;
	}
	public Entete setLinkLogo(String linkLogo) {
		this.linkLogo = linkLogo;
		return this;
	}
	
	public String getHierarchieLevel1() {
		return hierarchieLevel1;
	}
	public Entete setHierarchieLevel1(String hierarchieLevel1) {
		this.hierarchieLevel1 = hierarchieLevel1;
		return this;
	}
	public String getHierarchieLevel2() {
		return hierarchieLevel2;
	}
	public Entete setHierarchieLevel2(String hierarchieLevel2) {
		this.hierarchieLevel2 = hierarchieLevel2;
		return this;
	}
	public String getHierarchieLevel3() {
		return hierarchieLevel3;
	}
	public Entete setHierarchieLevel3(String hierarchieLevel3) {
		this.hierarchieLevel3 = hierarchieLevel3;
		return this;
	}
	public String getSchool() {
		return school;
	}
	public Entete setSchool(String school) {
		this.school = school;
		return this;
	}
	public String getCountry() {
		return country;
	}
	public Entete setCountry(String country) {
		this.country = country;
		return this;
	}
	public String getDevice() {
		return device;
	}
	public Entete setDevice(String device) {
		this.device = device;
		return this;
	}
	public String getStatusHeadmaster() {
		return statusHeadmaster;
	}
	public Entete setStatusHeadmaster(String statusHeadmaster) {
		this.statusHeadmaster = statusHeadmaster;
		return this;
	}
	
	
}
