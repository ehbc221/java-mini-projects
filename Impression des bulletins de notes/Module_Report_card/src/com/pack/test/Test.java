package com.pack.test;

import com.pack.entity.Entete;
import com.pack.entity.InfoMatiere;
import com.pack.entity.Profile;
import com.pack.impression.Impression;
import com.pack.report.Report;
import com.pack.report.ReportsCards;

/**
 * Exemple d'utilisation
 * public static void main(String arg[]) throws Exception{
 * 		ReportsCards reports = new ReportsCards();
 * 			Report report = new Report().setEntete(new Entete().setAnneeScolaire("2012/2013")
 * 																.setBp("145")
 * 																.setCountry("Cameroun")
 * 																.setDevice("Paix-Travail-Patrie")
 * 																.setHierarchieLevel1("MINSTERE DES ENSEIGNEMENTS SECONDAIRES")
 * 																.setHierarchieLevel2("DELEGATION REDIONALE DE L'EDUCATION")
 * 																.setHierarchieLevel3("DELEGATION DEPARTEMENTALE DE L'EDUCATION")
 * 																.setLinkFiligrane("./images/filigrane.png")
 * 																.setLinkLogo("./images/logo.png")
 * 																.setNumero("(+237 22 34 25 67)")
 * 																.setSchool("Lycée Bilingue de Mbalmayo")
 * 																.setStatusHeadmaster("Proviseur"))
 * 										.setDecision("Admis en 3eme Allemand")
 * 										.setMoyenne("12.45")
 * 										.setRang("3")
 * 										.setProfile(new Profile().setClasse("4eme Allemand")
 * 																  .setDate("12-04-1998")
 * 																  .setEffectif(35)
 * 																  .setLieu("Mbanga")
 * 																  .setNom("Ebanda Ondoua Jean Kevin")
 * 																  .setMatricule("2013U5563")
 * 																  .setSexe("Masculin"));
 * 			
 * 				report.getGroup1().getMatieres().add(new InfoMatiere().setCoef(2)
 * 																		.setEnseignant("Mr Tameu Boris")
 * 																		.setMatiere("Anglais")
 * 																		.setNoteSeq1(12)
 * 																		.setNoteSeq2(13)
 * 																		.setRang("5"));
 * 				report.getGroup1().setMoySeq1(14.6)
 * 									.setMoySeq2(10.6)
 * 									.setRang(12);
 * 				
 * 				
 * 				report.getGroup2().getMatieres().add(new InfoMatiere().setCoef(2)
 * 																		.setEnseignant("Mr Melingui")
 * 																		.setMatiere("S.V.T.")
 * 																		.setNoteSeq1(12)
 * 																		.setNoteSeq2(13)
 * 																		.setRang("3"));
 * 				report.getGroup2().setMoySeq1(14.6)
 * 									.setMoySeq2(10.6)
 * 									.setRang(12);
 * 				
 * 				report.getGroup3().getMatieres().add(new InfoMatiere().setCoef(2)
 * 																		.setEnseignant("Mr Tales")
 * 																		.setMatiere("E.P.S.")
 * 																		.setNoteSeq1(12)
 * 																		.setNoteSeq2(13)
 * 																		.setRang("2"));
 * 				report.getGroup3().setMoySeq1(14.6)
 * 									.setMoySeq2(10.6)
 * 									.setRang(12);
 * 				report.getGroup3().getMatieres().add(new InfoMatiere().setCoef(2)
 * 																		.setEnseignant("Mr Tales")
 * 																		.setMatiere("E.P.S.")
 * 																		.setNoteSeq1(12)
 * 																		.setNoteSeq2(13)
 * 																		.setRang("2"));
 * 				report.getGroup3().setMoySeq1(14.6)
 * 									.setMoySeq2(10.6)
 * 									.setRang(12);
 * 				
 * 				
 * 			reports.getListReports().add(report);	
 * 			reports.setMoyenneGeneral("15.56");
 * 		
 * 		try {new Impression().setPeriod("trimestre1").setReports(reports).imprimer();} catch (Exception e) {e.printStackTrace();}
 * 		
 * 		System.exit(0);
 * 	}
 * 
 */
public class Test {
	public static void main(String arg[]) throws Exception{
		ReportsCards reports = new ReportsCards();
			Report report = new Report().setEntete(new Entete().setAnneeScolaire("2012/2013")
																.setBp("145")
																.setCountry("Cameroun")
																.setDevice("Paix-Travail-Patrie")
																.setHierarchieLevel1("MINSTERE DES ENSEIGNEMENTS SECONDAIRES")
																.setHierarchieLevel2("DELEGATION REDIONALE DE L'EDUCATION")
																.setHierarchieLevel3("DELEGATION DEPARTEMENTALE DE L'EDUCATION")
																.setLinkFiligrane("./images/filigrane.png")
																.setLinkLogo("./images/logo.png")
																.setNumero("(+237 22 34 25 67)")
																.setSchool("Lycée Bilingue de Mbalmayo")
																.setStatusHeadmaster("Proviseur"))
										.setDecision("Admis en 3eme Allemand")
										.setMoyenne("12.45")
										.setRang("3")
										.setProfile(new Profile().setClasse("4eme Allemand")
																  .setDate("12-04-1998")
																  .setEffectif(35)
																  .setLieu("Mbanga")
																  .setNom("Ebanda Ondoua Jean Kevin")
																  .setMatricule("2013U5563")
																  .setSexe("Masculin"));
			
				report.getGroup1().getMatieres().add(new InfoMatiere().setCoef(2)
																		.setEnseignant("Mr Tameu Boris")
																		.setMatiere("Anglais")
																		.setNoteSeq1(12)
																		.setNoteSeq2(13)
																		.setRang("5"));
				report.getGroup1().setMoySeq1(14.6)
									.setMoySeq2(10.6)
									.setRang(12);
				
				
				report.getGroup2().getMatieres().add(new InfoMatiere().setCoef(2)
																		.setEnseignant("Mr Melingui")
																		.setMatiere("S.V.T.")
																		.setNoteSeq1(12)
																		.setNoteSeq2(13)
																		.setRang("3"));
				report.getGroup2().setMoySeq1(14.6)
									.setMoySeq2(10.6)
									.setRang(12);
				
				report.getGroup3().getMatieres().add(new InfoMatiere().setCoef(2)
																		.setEnseignant("Mr Tales")
																		.setMatiere("E.P.S.")
																		.setNoteSeq1(12)
																		.setNoteSeq2(13)
																		.setRang("2"));
				report.getGroup3().setMoySeq1(14.6)
									.setMoySeq2(10.6)
									.setRang(12);
				report.getGroup3().getMatieres().add(new InfoMatiere().setCoef(2)
																		.setEnseignant("Mr Tales")
																		.setMatiere("E.P.S.")
																		.setNoteSeq1(12)
																		.setNoteSeq2(13)
																		.setRang("2"));
				report.getGroup3().setMoySeq1(14.6)
									.setMoySeq2(10.6)
									.setRang(12);
				
				
			reports.getListReports().add(report);	
			reports.setMoyenneGeneral("15.56");
		
		try {new Impression().setPeriod("trimestre1").setReports(reports).imprimer();} catch (Exception e) {e.printStackTrace();}
		
		System.exit(0);
	}
}
