package com.pack.entity;

import java.util.ArrayList;

public class InfoGroupMatiere {
	private ArrayList<InfoMatiere> matieres = new ArrayList<InfoMatiere>();
	private double moyenne = 0.0;
	private double moySeq1 = 0;
	private double moySeq2 = 0;
	private double moySeq3 = 0;
	private double moySeq4 = 0;
	private double moySeq5 = 0;
	private double moySeq6 = 0;
	private int rang = 0;
	public ArrayList<InfoMatiere> getMatieres() {
		return matieres;
	}
	public InfoGroupMatiere setMatieres(ArrayList<InfoMatiere> matieres) {
		this.matieres = matieres;
		return this;
	}
	public double getMoyenne() {
		return moyenne;
	}
	public InfoGroupMatiere setMoyenne(double moyenne) {
		this.moyenne = moyenne;
		return this;
	}
	public double getMoySeq1() {
		return moySeq1;
	}
	public InfoGroupMatiere setMoySeq1(double moySeq1) {
		this.moySeq1 = moySeq1;
		return this;
	}
	public double getMoySeq2() {
		return moySeq2;
	}
	public InfoGroupMatiere setMoySeq2(double moySeq2) {
		this.moySeq2 = moySeq2;
		return this;
	}
	public double getMoySeq3() {
		return moySeq3;
	}
	public InfoGroupMatiere setMoySeq3(double moySeq3) {
		this.moySeq3 = moySeq3;
		return this;
	}
	public double getMoySeq4() {
		return moySeq4;
	}
	public InfoGroupMatiere setMoySeq4(double moySeq4) {
		this.moySeq4 = moySeq4;
		return this;
	}
	public double getMoySeq5() {
		return moySeq5;
	}
	public InfoGroupMatiere setMoySeq5(double moySeq5) {
		this.moySeq5 = moySeq5;
		return this;
	}
	public double getMoySeq6() {
		return moySeq6;
	}
	public InfoGroupMatiere setMoySeq6(double moySeq6) {
		this.moySeq6 = moySeq6;
		return this;
	}
	public int getRang() {
		return rang;
	}
	public InfoGroupMatiere setRang(int rang) {
		this.rang = rang;
		return this;
	}
}
