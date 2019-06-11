package com.pack.entity;



public class InfoMatiere {
	private String matiere="";
	private String enseignant="";
	private int coef = 0;
	private float noteSeq1 = 0;
	private float noteSeq2 = 0;
	private float noteSeq3 = 0;
	private float noteSeq4 = 0;
	private float noteSeq5 = 0;
	private float noteSeq6 = 0;
	private String rang = "/";
	private String observation = "/";
	public String getMatiere() {
		return matiere;
	}
	public InfoMatiere setMatiere(String matiere) {
		this.matiere = matiere;
		return this;
	}
	public String getEnseignant() {
		return enseignant;
	}
	public InfoMatiere setEnseignant(String enseignant) {
		this.enseignant = enseignant;
		return this;
	}
	public int getCoef() {
		return coef;
	}
	public InfoMatiere setCoef(int coef) {
		this.coef = coef;
		return this;
	}
	public float getNoteSeq1() {
		return noteSeq1;
	}
	public InfoMatiere setNoteSeq1(float noteSeq1) {
		this.noteSeq1 = noteSeq1;
		return this;
	}
	public float getNoteSeq2() {
		return noteSeq2;
	}
	public InfoMatiere setNoteSeq2(float noteSeq2) {
		this.noteSeq2 = noteSeq2;
		return this;
	}
	public float getNoteSeq3() {
		return noteSeq3;
	}
	public InfoMatiere setNoteSeq3(float noteSeq3) {
		this.noteSeq3 = noteSeq3;
		return this;
	}
	public float getNoteSeq4() {
		return noteSeq4;
	}
	public InfoMatiere setNoteSeq4(float noteSeq4) {
		this.noteSeq4 = noteSeq4;
		return this;
	}
	public float getNoteSeq5() {
		return noteSeq5;
	}
	public InfoMatiere setNoteSeq5(float noteSeq5) {
		this.noteSeq5 = noteSeq5;
		return this;
	}
	public float getNoteSeq6() {
		return noteSeq6;
	}
	public InfoMatiere setNoteSeq6(float noteSeq6) {
		this.noteSeq6 = noteSeq6;
		return this;
	}
	public String getRang() {
		return rang;
	}
	public InfoMatiere setRang(String rang)throws Exception {
		try{
			this.rang = Integer.parseInt(rang)+"e";
		}catch(NumberFormatException ex){throw new Exception("Le rang doit être numerique");}
		return this;
	}
	public String getObservation() {
		return observation;
	}
	public InfoMatiere setObservation(String observation) {
		this.observation = observation;
		return this;
	}
	
	
	
	
}