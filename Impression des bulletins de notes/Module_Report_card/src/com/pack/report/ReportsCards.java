package com.pack.report;

import java.util.ArrayList;

public class ReportsCards {
	private ArrayList<Report> listReports = new ArrayList<Report>();
	private String moyenneGeneral = "/";
	public ArrayList<Report> getListReports() {
		return listReports;
	}
	public ReportsCards setListReports(ArrayList<Report> listReports) {
		this.listReports = listReports;
		return this;
	}
	public String getMoyenneGeneral() {
		return moyenneGeneral;
	}
	public ReportsCards setMoyenneGeneral(String moyenneGeneral) {
		this.moyenneGeneral = moyenneGeneral;
		return this;
	}
	
	public ReportsCards add(Report report){
		this.listReports.add(report);
		return this;
	}
	
	public ReportsCards addCollection(ReportsCards reports){
		this.listReports.addAll(reports.getListReports());
		return this;
	}
}
