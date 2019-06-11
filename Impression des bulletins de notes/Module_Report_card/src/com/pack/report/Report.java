package com.pack.report;

import java.math.BigDecimal;

import com.pack.entity.Absences;
import com.pack.entity.Entete;
import com.pack.entity.InfoGroupMatiere;
import com.pack.entity.Profile;

public class Report {
	private InfoGroupMatiere group1 = new InfoGroupMatiere();
	private InfoGroupMatiere group2 = new InfoGroupMatiere();
	private InfoGroupMatiere group3 = new InfoGroupMatiere();
	private String appreciation = "/";
	private String decision = "/";
	private Absences discipline = new Absences();
	private Profile profile = new Profile();
	private Entete entete = new Entete();
	private String moyenne = "/";
	private String rang = "/";
	
	
	public String getAppreciation() {
		return appreciation;
	}
	public Report setAppreciation(String appreciation) throws Exception{
		String [] pattern = appreciation.split(" ");
		appreciation = "";
		for(String s:pattern){
			if(s.isEmpty()) continue;
			appreciation+=s+" ";
		}
		appreciation = appreciation.trim();
		if(appreciation.length()>((18*3)+2)){throw new Exception("Chaque mot doit avoir au maximum "+((18*3)+2)+" caractères");}	
		this.appreciation = appreciation.toUpperCase();
		return this;
	}
	public String getDecision() {
		return decision;
	}
	
	public Report setDecision(String decision) throws Exception {
		if(decision.length()>54){throw new Exception("Chaque mot doit avoir au maximum 54 caractères");}	
		this.decision = decision.toUpperCase();
		return this;
	}
	public InfoGroupMatiere getGroup1() {
		return group1;
	}
	public Report setGroup1(InfoGroupMatiere group1) {
		this.group1 = group1;
		return this;
	}
	public InfoGroupMatiere getGroup2() {
		return group2;
	}
	public Report setGroup2(InfoGroupMatiere group2) {
		this.group2 = group2;
		return this;
	}
	public InfoGroupMatiere getGroup3() {
		return group3;
	}
	public Report setGroup3(InfoGroupMatiere group3) {
		this.group3 = group3;
		return this;
	}
	public Absences getDiscipline() {
		return discipline;
	}
	public Report setDiscipline(Absences discipline) {
		this.discipline = discipline;
		return this;
	}
	public Profile getProfile() {
		return profile;
	}
	public Report setProfile(Profile profile) {
		this.profile = profile;
		return this;
	}
	public Entete getEntete() {
		return entete;
	}
	public Report setEntete(Entete entete) {
		this.entete = entete;
		return this;
	}
	public String getMoyenne() {
		return moyenne;
	}
	public Report setMoyenne(String moyenne) throws Exception {
		
		try{
			BigDecimal valeur = (new BigDecimal(moyenne)).setScale(2, BigDecimal.ROUND_HALF_DOWN);
			if(valeur.doubleValue()<0||20<valeur.doubleValue()) throw new Exception("La moyenne doit être entre 0 et 20");
			
			if(valeur.doubleValue()<10) this.moyenne =  "0"+valeur.toString();
		    else this.moyenne = valeur.toString();
			
		}catch(NumberFormatException ex){throw new Exception("Le moyenne doit être un nombre réel");}
		return this;
	}
	public String getRang() {
		return rang;
	}
	public Report setRang(String rang)throws Exception {
		try{
			this.rang = Integer.parseInt(rang)+"e";
		}catch(NumberFormatException ex){throw new Exception("Le rang doit être numerique");}
		return this;
	}
}
