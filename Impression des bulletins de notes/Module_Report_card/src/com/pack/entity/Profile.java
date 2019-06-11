package com.pack.entity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Profile {
	private String matricule = "/";
	private String nom = "/";
	private Date date = null;
	private String lieu = "/";
	private int effectif = 0;
	private String classe = "/";
	private String sexe = "/";
	
	
	public String getMatricule() {
		return matricule;
	}
	public Profile setMatricule(String matricule) {
		this.matricule = matricule;
		return this;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getNom() {
		return nom;
	}
	public Profile setNom(String nom) {
		this.nom = nom;
		return this;
	}
	public String getDate() {	
		if(this.date==null) return "/";
		return new SimpleDateFormat("dd-MM-yyyy").format(date);
	}
	
	/*
	 * @param dateValue : Exp: 10-05-2013 ou 10/05/2013
	 * */
	public Profile setDate(String dateValue) {
		this.date = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");        
        try {this.date = (Date) simpleDateFormat.parse(dateValue.replace("/", "-"));}
        catch (ParseException e) {e.printStackTrace();}
		return this;
	}
	public String getLieu() {
		return lieu;
	}
	public Profile setLieu(String lieu) {
		this.lieu = lieu;
		return this;
	}
	public int getEffectif() {
		return effectif;
	}
	public Profile setEffectif(int effectif) {
		this.effectif = effectif;
		return this;
	}
	public String getClasse() {
		return classe;
	}
	public Profile setClasse(String classe) {
		this.classe = classe;
		return this;
	}
	public String getSexe() {
		return sexe;
	}
	public Profile setSexe(String sexe) {
		this.sexe = sexe;
		return this;
	}
	
	
}
