package com.pack.impression;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.pack.entity.InfoGroupMatiere;
import com.pack.entity.InfoMatiere;
import com.pack.gui.WindowBox;
import com.pack.report.Report;
import com.pack.report.ReportsCards;

public class Impression implements Printable {
	private ReportsCards reports = new ReportsCards();
	private String period = "";
	private double produits1 = 0,produits2 = 0,produits3 = 0;
	private int coefs1 = 0,coefs2 = 0,coefs3 = 0;
	private WindowBox alert = new WindowBox(new JFrame()).setMsg("Chargement en cours.....").setPanel().addProgressBar().animate();
	public Impression imprimer(){
        PrinterJob pj = PrinterJob.getPrinterJob();	
        pj.setPrintable(Impression.this);	
        pj.printDialog();	
        try {
            pj.setJobName("Reports cards");
            PageFormat pf = pj.defaultPage();
            Paper p = pf.getPaper();
            p.setImageableArea(1, 1, 1024,1024);
            pf.setPaper(p);
            pf.setOrientation(PageFormat.PORTRAIT);
            System.out.println("pf.getHeight()="+pf.getHeight()+"  pf.getWidth()="+pf.getWidth());
            pj.setPrintable(this,pf);
            alert.setProgress(0).display();
            pj.print();
        } catch (Exception PrintException) {
            JOptionPane.showMessageDialog(null, "Erreur lors de l'impression \n[ motif : "+PrintException.getMessage()+" ]");
            PrintException.printStackTrace();
        }
        return this;
    }
	
	@Override
	public int print(Graphics g, PageFormat pageFormat, int pageIndex) throws PrinterException {
		if(pageIndex<reports.getListReports().size()){
			produits1 = 0;produits3 = 0;produits2 = 0;
			coefs1 = 0;coefs2 = 0;coefs3 = 0;
			alert.setProgress(100*pageIndex/reports.getListReports().size());
			
			Graphics2D g2 = (Graphics2D)g;
			
			Report report = reports.getListReports().get(pageIndex);
				
	        g2.setColor(Color.black);
	        g2.setFont(new Font(Font.MONOSPACED,Font.BOLD,8));
	        
	        g2.drawImage((new ImageIcon(report.getEntete().getLinkFiligrane())).getImage(), 150, 300,300,300, null);
	        g2.drawString(report.getEntete().getHierarchieLevel1(), 20, 10); g2.drawString(report.getEntete().getCountry(), 460, 10);
	        g2.drawString(report.getEntete().getHierarchieLevel2(), 20, 20); g2.drawString(report.getEntete().getDevice(), 460, 20);
	        g2.drawString(report.getEntete().getHierarchieLevel3(), 20, 30);
	        g2.drawString(report.getEntete().getSchool(), 20, 40);
	        g2.drawImage((new ImageIcon(report.getEntete().getLinkLogo())).getImage(), 250, 10,70, 50, null);
	        g2.drawString("Annee Scolaire : "+report.getEntete().getAnneeScolaire(), 20, 50);
	        g2.drawString("BP "+report.getEntete().getBp()+"   Tel : "+report.getEntete().getNumero(), 20, 60);
	        g2.drawString("Date : "+new SimpleDateFormat("EEEE, dd MMMM yyyy").format(new Date()), 20, 70);
	        g2.drawLine(1, 80, 600, 80);
	        
	        /*TITRE*/     
            g2.setFont(new Font(Font.MONOSPACED,Font.BOLD,16));
	        int posX = 245 - ((this.getPeriod().length()/2)-espaceWord(this.getPeriod()))*8;
	        g2.drawString(this.getPeriod().toUpperCase(), posX, 95);
	        

	        int positionY = 110,positionX=20;
	        /*profile*/
	        g2.setFont(new Font(Font.MONOSPACED,Font.BOLD,6));
	        g2.drawRect(positionX, positionY, 550, 45);
	        positionY+=10;
	        g2.drawString("Matricule : "+report.getProfile().getMatricule(), positionX+10, positionY);
	        g2.drawString("Nom : "+report.getProfile().getNom().toUpperCase(), positionX+250, positionY);
	        positionY+=15;
	        g2.drawString("sexe : "+report.getProfile().getSexe(), positionX+10, positionY);
	        g2.drawString("Date et lieu de naissance : "+report.getProfile().getDate()+" à "+report.getProfile().getLieu(), positionX+250, positionY);
	        positionY+=15;
	        g2.drawString("classe : "+report.getProfile().getClasse(), positionX+10, positionY);
	        int effectif = report.getProfile().getEffectif();
	        if(effectif==0) effectif = reports.getListReports().size();
	        g2.drawString("effectif : "+effectif, positionX+250, positionY);
	        
	        /*Report Group1*/
	        positionY+=15;
	        positionY = drawCaseEntete(g2, positionX, positionY);
	        
	        if(getPeriod().toLowerCase().contains("annuel")){positionY = drawCaseAnGroup(g2, positionX+100, positionY);}
	        else if(getPeriod().toLowerCase().contains("trimestre")){positionY = drawCaseTrimGroup(g2, positionX+100, positionY);}
	        else if(getPeriod().toLowerCase().contains("sequence")){positionY = drawCaseSeqGroup(g2, positionX+100, positionY);}
	        
	        positionY = drawCaseGroup(g2, positionX, positionY,report,1,reports.getMoyenneGeneral());
	        positionY = drawCaseGroup(g2, positionX, positionY,report,2,reports.getMoyenneGeneral());
	        positionY = drawCaseGroup(g2, positionX, positionY,report,3,reports.getMoyenneGeneral());
	        positionY = drawCaseDiscAppreciation(g2,positionX,positionY,report);
	        positionY = drawCaseObservation(g2, positionX, positionY);
	        System.out.println("positionY="+positionY);
        	
	        try {Thread.sleep(10);} catch (InterruptedException e) {e.printStackTrace();}
	        
	        return PAGE_EXISTS;
		}
        alert.setProgress(100);
        alert.close();

        return NO_SUCH_PAGE;
	}
	
	/**
	  * Cette méthode permet d'imprimé les notes des sequences pour un bulletin sequentiel
	  * @param g2 objet permettant de dessiner
	  * @param positionX coordonnée de l'abcisse X
	  * @param positionY coordonnée de l'ordonnée Y
	  * @param matiere objet contenant les informations sur les notes, coefficient de la matiere
	  */
	public void drawCaseNoteSeqGroup(Graphics2D g2,int positionX, int positionY,InfoMatiere matiere){
		g2.drawRect(positionX, positionY, 200, 15);
		if(getPeriod().contains("troisième")) g2.drawString(trimValue(""+matiere.getNoteSeq3()), positionX+92, positionY+10);
		else if(getPeriod().contains("deuxième")) g2.drawString(trimValue(""+matiere.getNoteSeq2()), positionX+92, positionY+10);
		else if(getPeriod().contains("première")) g2.drawString(trimValue(""+matiere.getNoteSeq1()), positionX+92, positionY+10);
		else if(getPeriod().contains("quatrième"))g2.drawString(trimValue(""+matiere.getNoteSeq4()), positionX+92, positionY+10);
		else if(getPeriod().contains("cinquième"))g2.drawString(trimValue(""+matiere.getNoteSeq5()), positionX+92, positionY+10);
		else if(getPeriod().contains("sixième"))g2.drawString(trimValue(""+matiere.getNoteSeq6()), positionX+92, positionY+10);
        
	}
	
	/**
	  * Cette méthode permet d'imprimé les notes des sequences pour un bulletin trimestriel
	  * @param g2 objet permettant de dessiner
	  * @param positionX coordonnée de l'abcisse X
	  * @param positionY coordonnée de l'ordonnée Y
	  * @param matiere objet contenant les informations sur les notes, coefficient de la matiere
	  */
	public void drawCaseNoteTrimGroup(Graphics2D g2,int positionX, int positionY,InfoMatiere matiere){
		for(int i=0;i!=2;i++){
			g2.drawRect(positionX, positionY, 100, 15);
			if(getPeriod().contains("troisième")) {
				if(i==0) g2.drawString(trimValue(""+matiere.getNoteSeq5()), positionX+42, positionY+10);
				else if(i==1) g2.drawString(trimValue(""+matiere.getNoteSeq6()), positionX+42, positionY+10);
			}else if(getPeriod().contains("deuxième")){
				if(i==0) g2.drawString(trimValue(""+matiere.getNoteSeq3()), positionX+42, positionY+10);
				else if(i==1) g2.drawString(trimValue(""+matiere.getNoteSeq4()), positionX+42, positionY+10);
			}else if(getPeriod().contains("premier")){
				if(i==0) g2.drawString(trimValue(""+matiere.getNoteSeq1()), positionX+42, positionY+10);
				else if(i==1) g2.drawString(trimValue(""+matiere.getNoteSeq2()), positionX+42, positionY+10);
			}
				
	        positionX+=100;
		}
	}

	/**
	  * Cette méthode permet d'imprimé les notes des sequences pour un bulletin annuel
	  * @param g2 objet permettant de dessiner
	  * @param positionX coordonnée de l'abcisse X
	  * @param positionY coordonnée de l'ordonnée Y
	  * @param matiere objet contenant les informations sur les notes, coefficient de la matiere
	  */
	public void drawCaseNoteAnGroup(Graphics2D g2,int positionX, int positionY,InfoMatiere matiere){
		for(int i=0;i!=6;i++){
			if((i+1)%3==0)g2.drawRect(positionX, positionY, 34, 15);
			else g2.drawRect(positionX, positionY, 33, 15);
			if(i==0) g2.drawString(trimValue(""+matiere.getNoteSeq1()), positionX+10, positionY+10);
			else if(i==1) g2.drawString(trimValue(""+matiere.getNoteSeq2()), positionX+10, positionY+10);
			else if(i==2) g2.drawString(trimValue(""+matiere.getNoteSeq3()), positionX+10, positionY+10);
			else if(i==3) g2.drawString(trimValue(""+matiere.getNoteSeq4()), positionX+10, positionY+10);
			else if(i==4) g2.drawString(trimValue(""+matiere.getNoteSeq5()), positionX+10, positionY+10);
			else if(i==5) g2.drawString(trimValue(""+matiere.getNoteSeq6()), positionX+10, positionY+10);
	        if((i+1)%3==0)positionX+=34;
	        else positionX+=33;
		}
		
	}
	
	/**
	  * Cette méthode permet d'imprimé les nombres de colonnes des sequences pour un bulletin sequentiel
	  * @param g2 objet permettant de dessiner
	  * @param positionX coordonnée de l'abcisse X
	  * @param positionY coordonnée de l'ordonnée Y
	  */
	public int drawCaseSeqGroup(Graphics2D g2,int positionX, int positionY){
		int index = 0;
		if(getPeriod().contains("troisième")) index = 3;
		else if(getPeriod().contains("deuxième")) index = 2;
		else if(getPeriod().contains("première")) index = 1;
		else if(getPeriod().contains("quatrième")) index = 4;
		else if(getPeriod().contains("cinquième")) index = 5;
		else if(getPeriod().contains("sixième")) index = 6;
		g2.setColor(Color.LIGHT_GRAY);
        g2.fillRect(positionX, positionY, 200, 15);
        g2.setColor(Color.black);
        g2.drawRect(positionX, positionY, 200, 15);
        g2.drawString(""+index, positionX+97, positionY+10);
        return positionY+15;
	}
	
	/**
	  * Cette méthode permet d'imprimé les nombres de colonnes des sequences pour un bulletin trimestriel
	  * @param g2 objet permettant de dessiner
	  * @param positionX coordonnée de l'abcisse X
	  * @param positionY coordonnée de l'ordonnée Y
	  */
	public int drawCaseTrimGroup(Graphics2D g2,int positionX, int positionY){
		int index = 0;
		if(getPeriod().contains("troisième")) index = 4;
		else if(getPeriod().contains("deuxième")) index = 2;
		else if(getPeriod().contains("premier")) index = 0;
		for(int i=index;i!=index+2;i++){
			g2.setColor(Color.LIGHT_GRAY);
	        g2.fillRect(positionX, positionY, 100, 15);
	        g2.setColor(Color.black);
	        g2.drawRect(positionX, positionY, 100, 15);
	        g2.drawString(""+(i+1), positionX+47, positionY+10);
	        positionX+=100;
		}
		return positionY+15;
	}
	

	/**
	  * Cette méthode permet d'imprimé les nombres de colonnes des sequences pour un bulletin annuel
	  * @param g2 objet permettant de dessiner
	  * @param positionX coordonnée de l'abcisse X
	  * @param positionY coordonnée de l'ordonnée Y
	  */
	public int drawCaseAnGroup(Graphics2D g2,int positionX, int positionY){
		for(int i=0;i!=6;i++){
			g2.setColor(Color.LIGHT_GRAY);
			g2.fillRect(positionX, positionY, 35, 15);
	        g2.setColor(Color.black);
	        g2.drawRect(positionX, positionY, 35, 15);
	        g2.drawString(""+(i+1), positionX+14, positionY+10);
	        positionX+=33;
		}
		return positionY+15;
	}
	

	/**
	  * Cette méthode permet d'imprimé le box des notes et matières appartenant à un groupe grp
	  * @param g2 objet permettant de dessiner
	  * @param positionX coordonnée de l'abcisse X
	  * @param positionY coordonnée de l'ordonnée Y
	  * @param report objet représentant un bulletin de note 
	  * @param grp le groupe des matière
	  * @param moyclasse La moyenne de la classe
	  */
	
	public int drawCaseGroup(Graphics2D g2,int positionX, int positionY,Report report,int grp,String moyclasse){
		int coef = 0;
		float prod = 0;
		InfoGroupMatiere groupeMatiere = new InfoGroupMatiere();
		if(grp==1) groupeMatiere = report.getGroup1();
		else if(grp==2) groupeMatiere = report.getGroup2();
		else if(grp==3) groupeMatiere = report.getGroup3();
		for(InfoMatiere matiere: groupeMatiere.getMatieres()){
			g2.drawRect(positionX, positionY, 100, 15);
	        g2.drawString(trimDiscipline(matiere.getMatiere()), positionX+5, positionY+6);
	        g2.drawString(trimNom(matiere.getEnseignant()), positionX+5, positionY+12);
	        
	        if(getPeriod().toLowerCase().contains("annuel"))
	        	drawCaseNoteAnGroup(g2, positionX+100, positionY,matiere);
	        else if(getPeriod().toLowerCase().contains("trimestre"))
	        	drawCaseNoteTrimGroup(g2, positionX+100, positionY,matiere);
	        else if(getPeriod().toLowerCase().contains("sequence"))
	        	drawCaseNoteSeqGroup(g2, positionX+100, positionY,matiere);
	        
	        g2.drawRect(positionX+100+200, positionY, 40, 15);
	        g2.drawString(""+matiere.getCoef(), positionX+100+200+20, positionY+10);
	        g2.drawRect(positionX+100+200+40, positionY, 40, 15);
	        g2.drawRect(positionX+100+200+40+40, positionY, 40, 15);
	        
	        String observation = "/";
	        if(getPeriod().toLowerCase().contains("annuel")){
	        	int nbre = 0;
	        	float som = 0;
	        	if(matiere.getNoteSeq5()>=0){
	        		som+=matiere.getNoteSeq5();
	        		nbre++;
	        	}
	        	if(matiere.getNoteSeq4()>=0){
	        		som+=matiere.getNoteSeq4();
	        		nbre++;
	        	}
	        	if(matiere.getNoteSeq3()>=0){
	        		som+=matiere.getNoteSeq3();
	        		nbre++;
	        	}
	        	if(matiere.getNoteSeq2()>=0){
	        		som+=matiere.getNoteSeq2();
	        		nbre++;
	        	}
	        	if(matiere.getNoteSeq1()>=0){
	        		som+=matiere.getNoteSeq1();
	        		nbre++;
	        	}
	        	if(nbre!=0) {
	        		prod+=(som/nbre)*matiere.getCoef();
	        		g2.drawString(trimValue(""+(som/nbre)), positionX+100+200+40+10, positionY+10);
	        		g2.drawString(trimValue(""+((som/nbre)*matiere.getCoef())), positionX+100+200+40+40+10, positionY+10);
	        	}
	        	if(matiere.getObservation().equals("/")){
	        		observation = observation(""+((som/nbre)));
	        	}
	        }else if(getPeriod().toLowerCase().contains("trimestre")){
	        	if(getPeriod().contains("troisième")) {
		        	int nbre = 0;
		        	float som = 0;
		        	if(matiere.getNoteSeq5()>=0){
		        		som+=matiere.getNoteSeq5();
		        		nbre++;
		        	}
		        	if(matiere.getNoteSeq6()>=0){
		        		som+=matiere.getNoteSeq6();
		        		nbre++;
		        	}
		        	if(nbre!=0) {
		        		prod+=(som/nbre)*matiere.getCoef();
		        		g2.drawString(trimValue(""+(som/nbre)), positionX+100+200+40+10, positionY+10);
		        		g2.drawString(trimValue(""+((som/nbre)*matiere.getCoef())), positionX+100+200+40+40+10, positionY+10);
		        	}
		        	if(matiere.getObservation().equals("/")){
		        		observation = observation(""+((som/nbre)));
		        	}
		        }
				else if(getPeriod().contains("deuxième")) {
					int nbre = 0;
		        	float som = 0;
		        	if(matiere.getNoteSeq3()>=0){
		        		som+=matiere.getNoteSeq3();
		        		nbre++;
		        		g2.drawString(trimValue(""+matiere.getNoteSeq3()), positionX+100+200+40+10, positionY+10);
		        	}
		        	if(matiere.getNoteSeq4()>=0){
		        		som+=matiere.getNoteSeq4();
		        		nbre++;
		        		g2.drawString(trimValue(""+matiere.getNoteSeq4()), positionX+100+200+40+40+10, positionY+10);
		        	}
		        	if(nbre!=0) {
		        		prod+=(som/nbre)*matiere.getCoef();
		        		g2.drawString(trimValue(""+(som/nbre)), positionX+100+200+40+10, positionY+10);
		        		g2.drawString(trimValue(""+((som/nbre)*matiere.getCoef())), positionX+100+200+40+40+10, positionY+10);
		        	}
		        	if(matiere.getObservation().equals("/")){
		        		observation = observation(""+((som/nbre)));
		        	}
				}
				else if(getPeriod().contains("premier")) {
					int nbre = 0;
		        	float som = 0;
		        	if(matiere.getNoteSeq1()>=0){
		        		som+=matiere.getNoteSeq1();
		        		nbre++;
		        	}
		        	if(matiere.getNoteSeq2()>=0){
		        		som+=matiere.getNoteSeq2();
		        		nbre++;
		        	}
		        	if(nbre!=0) {
		        		prod+=(som/nbre)*matiere.getCoef();
		        		g2.drawString(trimValue(""+(som/nbre)), positionX+100+200+40+10, positionY+10);
		        		g2.drawString(trimValue(""+((som/nbre)*matiere.getCoef())), positionX+100+200+40+40+10, positionY+10);
		        	}
		        	if(matiere.getObservation().equals("/")){
		        		observation = observation(""+((som/nbre)));
		        	}
				}
	        }else if(getPeriod().toLowerCase().contains("sequence")){
	        	int nbre = 0;
	        	float som = 0;
	        	if(getPeriod().contains("troisième")) {
	        		if(matiere.getNoteSeq3()>=0){
		        		som+=matiere.getNoteSeq3();
		        		nbre++;
		        	}
	        	}
	    		else if(getPeriod().contains("deuxième")) {
	    			if(matiere.getNoteSeq2()>=0){
		        		som+=matiere.getNoteSeq2();
		        		nbre++;
		        	}
	    		}
	    		else if(getPeriod().contains("première")) {
	    			if(matiere.getNoteSeq1()>=0){
		        		som+=matiere.getNoteSeq1();
		        		nbre++;
		        	}
	    		}
	    		else if(getPeriod().contains("quatrième")) {
	    			if(matiere.getNoteSeq4()>=0){
		        		som+=matiere.getNoteSeq4();
		        		nbre++;
		        	}
	    		}
	    		else if(getPeriod().contains("cinquième")){
	    			if(matiere.getNoteSeq5()>=0){
		        		som+=matiere.getNoteSeq5();
		        		nbre++;
		        	}
	    		}
	    		else if(getPeriod().contains("sixième")) {
	    			if(matiere.getNoteSeq6()>=0){
		        		som+=matiere.getNoteSeq6();
		        		nbre++;
		        	}
	    		}
	        	if(nbre!=0) {
	        		prod+=(som/nbre)*matiere.getCoef();
	        		g2.drawString(trimValue(""+(som/nbre)), positionX+100+200+40+10, positionY+10);
	        		g2.drawString(trimValue(""+((som/nbre)*matiere.getCoef())), positionX+100+200+40+40+10, positionY+10);
	        	}
	        	if(matiere.getObservation().equals("/")){
	        		observation = observation(""+((som/nbre)));
	        	}
	        }
	        
	        
	        g2.drawRect(positionX+100+200+40+40+40, positionY, 40, 15);
	        g2.drawString(matiere.getRang(), positionX+100+200+40+40+40+15, positionY+10);
	        
	        g2.drawRect(positionX+100+200+40+40+40+40, positionY, 95, 15);
	        g2.drawString(observation, positionX+100+200+40+40+40+40+5, positionY+10);
	        positionY+=15;
	        coef+=matiere.getCoef();
		}
		if(grp==1) {produits1+=prod;coefs1+=coef;}
		else if(grp==2) {produits2+=prod;coefs1+=coef;}
		else if(grp==3) {produits3+=prod;coefs1+=coef;}
		drawTotauxGroup(g2, positionX, positionY,coef,prod,(prod/coef),""+grp);
		if(grp==3){
			positionY+=15;
			String app = report.getAppreciation();
			if(app.equals("/")){app = appreciation(""+(produits1+produits2+produits3)/(coefs1+coefs2+coefs3));}
			drawCaseTotaux(g2, positionX, positionY, coefs1+coefs2+coefs3, produits1+produits2+produits3, 
					(produits1+produits2+produits3)/(coefs1+coefs2+coefs3), report.getRang(), app,moyclasse);
			return positionY+40;
		}
		else return positionY+15;
	}
	

	/**
	  * Cette méthode permet d'imprimé le box des totaux et moyennes
	  * @param g2 objet permettant de dessiner
	  * @param positionX coordonnée de l'abcisse X
	  * @param positionY coordonnée de l'ordonnée Y
	  * @param coef total des coeficients
	  * @param produit total des produits note x coefficient
	  * @param moyenne la moyenne
	  * @param group valeur du groupe des matiere variant entre 1 et 3
	  */
	
	public void drawTotauxGroup(Graphics2D g2,int positionX, int positionY, int coef,float produit,float moyenne,String group){
		g2.setColor(Color.LIGHT_GRAY);
		g2.fillRect(positionX, positionY, 100, 15);
        g2.setColor(Color.black);
        g2.drawRect(positionX, positionY, 100, 15);
        g2.drawString("TOTAL GROUPE "+group, positionX+5, positionY+10);
        
        g2.setColor(Color.LIGHT_GRAY);
		g2.fillRect(positionX+100+200, positionY, 40, 15);
        g2.setColor(Color.black);
        g2.drawRect(positionX+100+200, positionY, 40, 15);
        g2.drawString(""+coef, positionX+100+200+20, positionY+10);
        
        g2.setColor(Color.LIGHT_GRAY);
		g2.fillRect(positionX+100+200+40, positionY, 40, 15);
        g2.setColor(Color.black);
        g2.drawRect(positionX+100+200+40, positionY, 40, 15);
        g2.drawString(trimValue(""+moyenne), positionX+100+200+40+10, positionY+10);
        
        g2.setColor(Color.LIGHT_GRAY);
		g2.fillRect(positionX+100+200+40+40, positionY, 40, 15);
        g2.setColor(Color.black);
        g2.drawRect(positionX+100+200+40+40, positionY, 40, 15);
        g2.drawString(trimValue(""+produit), positionX+100+200+40+40+10, positionY+10);
        
	}
	

	/**
	  * Cette méthode permet d'imprimé le box des totaux et moyennes
	  * @param g2 objet permettant de dessiner
	  * @param positionX coordonnée de l'abcisse X
	  * @param positionY coordonnée de l'ordonnée Y
	  * @param coef total des coeficients
	  * @param prod total des produits note x coefficient
	  * @param moyenne la moyenne
	  * @param rang le rang
	  * @param appreciation l'appréciation
	  * @param moyClasse La moyenne de la classe
	  */
	public int drawCaseTotaux(Graphics2D g2,int positionX, int positionY,int coef,double prod,double moyenne,String rang, String appreciation,String moyClasse){
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRect(positionX+100, positionY, 200, 15);
        g2.setColor(Color.black);
        g2.drawRect(positionX+100, positionY, 200, 15);
        g2.drawString("TOTAUX | MOYENNE "+getStatusPeriod()+"/20", positionX+100+30, positionY+10);
        
        g2.setColor(Color.black);
        g2.fillRect(positionX+100+200, positionY, 40, 15);
        g2.setColor(Color.white);
        g2.drawRect(positionX+100+200, positionY, 40, 15);
        g2.drawString(""+coef, positionX+100+200+20, positionY+10);
        g2.setColor(Color.BLACK);
        g2.fillRect(positionX+100+200+40, positionY, 40, 15);
        g2.setColor(Color.WHITE);
        g2.drawRect(positionX+100+200+40, positionY, 40, 15);
        g2.drawString(trimValue(""+moyenne), positionX+100+200+40+10, positionY+10);
        g2.setColor(Color.black);
        g2.fillRect(positionX+100+200+40+40, positionY, 40, 15);
        g2.setColor(Color.white);
        g2.drawRect(positionX+100+200+40+40, positionY, 40, 15);
        g2.drawString(trimValue(""+prod), positionX+100+200+40+40+10, positionY+10);
        g2.setColor(Color.black);
        g2.fillRect(positionX+100+200+40+40+40, positionY, 40, 15);
        g2.setColor(Color.white);
        g2.drawRect(positionX+100+200+40+40+40, positionY, 40, 15);
        g2.drawString(""+rang, positionX+100+200+40+40+40+15, positionY+10);

        
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRect(positionX+100+200+40+40+40+40, positionY, 95, 40);
        g2.setColor(Color.black);
        g2.drawRect(positionX+100+200+40+40+40+40, positionY, 95, 40);

        g2.setFont(new Font(Font.MONOSPACED,Font.BOLD,8));
        String pattern[] = appreciation.split("<BR>");
        int i = 1;
        for(String motif:pattern){
        	g2.drawString(motif, positionX+100+300+55+10, positionY+(10*i));
        	i++;
        }
        g2.setFont(new Font(Font.MONOSPACED,Font.BOLD,6));
        
        positionY+=15;
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRect(positionX, positionY, 100, 20);
        g2.setColor(Color.black);
        g2.drawRect(positionX, positionY, 100, 20);
        g2.drawString("MOYENNE DE LA CLASSE/20", positionX+5, positionY+10);
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRect(positionX+100, positionY, 100, 20);
        g2.setColor(Color.black);
        g2.drawRect(positionX+100, positionY, 100, 20);
        g2.drawString(trimValue(moyClasse), positionX+100+42, positionY+10);
        
        return positionY+20;
	}
	
	/**
	  * Cette méthode permet d'imprimé le box des appreciations et disciplines
	  * @param g2 objet permettant de dessiner
	  * @param positionX coordonnée de l'abcisse X
	  * @param positionY coordonnée de l'ordonnée Y
	  * @param report objet représentant un bulletin de notes
	  */
	public int drawCaseDiscAppreciation(Graphics2D g2,int positionX, int positionY,Report report){
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRect(positionX, positionY, 276, 15);
        g2.setColor(Color.black);
        g2.drawRect(positionX, positionY, 276, 15);
        g2.drawString("DISCIPLINE", positionX+60, positionY+10);
        
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRect(positionX+276, positionY, 279, 15);
        g2.setColor(Color.black);
        g2.drawRect(positionX+276, positionY, 279, 15);
        g2.drawString("APPRéCIATION TRAVAIL".toUpperCase(), positionX+276+90, positionY+10);
        
        positionY+=15;
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRect(positionX, positionY, 69, 15);
        g2.setColor(Color.black);
        g2.drawRect(positionX, positionY, 69, 15);
        g2.drawString("ABSENCE(heure)", positionX+5, positionY+10);
        g2.drawRect(positionX, positionY+15, 69, 15);
        g2.drawString(""+(calculHours(report)), positionX+33, positionY+15+10);

        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRect(positionX+69, positionY, 69, 15);
        g2.setColor(Color.black);
        g2.drawRect(positionX+69, positionY, 69, 15);
        g2.drawString("AVERTISSEMENT", positionX+69+5, positionY+10);
        g2.drawRect(positionX+69, positionY+15, 69, 15);
        
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRect(positionX+69+69, positionY, 69, 15);
        g2.setColor(Color.black);
        g2.drawRect(positionX+69+69, positionY, 69, 15);
        g2.drawString("BLâME".toUpperCase(), positionX+69+69+15, positionY+10);
        g2.drawRect(positionX+69+69, positionY+15, 69, 15);
        
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRect(positionX+69+69+69, positionY, 69, 15);
        g2.setColor(Color.black);
        g2.drawRect(positionX+69+69+69, positionY, 69, 15);
        g2.drawString("EXCLUSION", positionX+69+69+69+5, positionY+10);
        g2.drawRect(positionX+69+69+69, positionY+15, 69, 15);
        
        g2.drawRect(positionX+276, positionY, 34, 15);
        g2.drawString("TB", positionX+276+15, positionY+10);
        g2.drawRect(positionX+276+34, positionY, 34, 15);
        g2.drawString("B", positionX+276+34+15, positionY+10);
        g2.drawRect(positionX+276+34+34, positionY, 34, 15);
        g2.drawString("AB", positionX+276+34+34+15, positionY+10);
        g2.drawRect(positionX+276+34+34+34, positionY, 35, 15);
        g2.drawString("PASS", positionX+276+34+34+34+10, positionY+10);
        g2.drawRect(positionX+276+34+35+34+34, positionY, 34, 15);
        g2.drawString("MED", positionX+276+35+34+34+34+10, positionY+10);
        g2.drawRect(positionX+276+34+35+34+34+34, positionY, 36, 15);
        g2.drawString("INSU", positionX+276+35+34+34+34+34+10, positionY+10);
        g2.drawRect(positionX+276+34+35+34+34+34+36, positionY, 36, 15);
        g2.drawString("FAIB", positionX+276+35+34+36+34+34+34+10, positionY+10);
        g2.drawRect(positionX+276+34+35+34+34+34+36+36, positionY, 36, 15);
        g2.drawString("NUL", positionX+276+35+34+34+34+34+36+36+10, positionY+10);
        
        g2.drawRect(positionX+276, positionY+15, 55, 15);
        g2.drawString("Encouragement", positionX+276+5, positionY+15+10);
        
        g2.drawRect(positionX+276+55, positionY+15, 55, 15);
        g2.drawString("Félicitations", positionX+276+55+5, positionY+15+10);
        
        g2.drawRect(positionX+276+55+55, positionY+15, 65, 15);
        g2.drawString("Tableau d'honneur", positionX+276+55+55+1, positionY+15+10);

        g2.drawRect(positionX+276+55+55+65, positionY+15, 55, 15);
        g2.drawString("Avertissements", positionX+276+55+55+65+1, positionY+15+10);
        
        g2.drawRect(positionX+276+55+55+65+55, positionY+15, 49, 15);
        g2.drawString("Blâmes", positionX+276+55+55+65+55+5, positionY+15+10);
        
        positionY+=30;
        g2.drawRect(positionX, positionY, 276, 35);
        g2.drawString("MOTIF : ", positionX+5, positionY+10);
        
        if(getPeriod().contains("annuel")){
        	g2.setColor(Color.LIGHT_GRAY);
            g2.fillRect(positionX+276, positionY, 279, 15);
            g2.setColor(Color.black);
            g2.drawRect(positionX+276, positionY, 279, 15);
            g2.drawString("DECISION", positionX+276+95, positionY+10);
            
            g2.drawRect(positionX+276, positionY+15, 279, 20);
            g2.setFont(new Font(Font.MONOSPACED,Font.BOLD,8));
            g2.drawString(report.getDecision(), positionX+276+10, positionY+15+10);
            g2.setFont(new Font(Font.MONOSPACED,Font.BOLD,6));
            return positionY+35;
        }
        
        
        return positionY+35;
	}
	
	/**
	  * Cette méthode permet de calculer le cumul des heures pendant une periode
	  * @param report objet représentant un bulletin
	  */
	
	public String calculHours(Report report){
		
		if(getPeriod().toLowerCase().contains("annuel")){
			return ""+(report.getDiscipline().getNonJustifieSeq1()-report.getDiscipline().getJustifieSeq1()
					+report.getDiscipline().getNonJustifieSeq2()-report.getDiscipline().getJustifieSeq2()
					+report.getDiscipline().getNonJustifieSeq3()-report.getDiscipline().getJustifieSeq3()
					+report.getDiscipline().getNonJustifieSeq4()-report.getDiscipline().getJustifieSeq4()
					+report.getDiscipline().getNonJustifieSeq5()-report.getDiscipline().getJustifieSeq5()
					+report.getDiscipline().getNonJustifieSeq6()-report.getDiscipline().getJustifieSeq6());
		}else if(getPeriod().toLowerCase().contains("trimestre")){
        	if(getPeriod().contains("troisième")) {
        		return ""+(report.getDiscipline().getNonJustifieSeq5()-report.getDiscipline().getJustifieSeq5()
    					+report.getDiscipline().getNonJustifieSeq6()-report.getDiscipline().getJustifieSeq6());
	        }
			else if(getPeriod().contains("deuxième")) {
				return ""+(report.getDiscipline().getNonJustifieSeq3()-report.getDiscipline().getJustifieSeq3()
						+report.getDiscipline().getNonJustifieSeq4()-report.getDiscipline().getJustifieSeq4());
			}
			else if(getPeriod().contains("premier")) {
				return ""+(report.getDiscipline().getNonJustifieSeq1()-report.getDiscipline().getJustifieSeq1()
						+report.getDiscipline().getNonJustifieSeq2()-report.getDiscipline().getJustifieSeq2());
			}
        }else if(getPeriod().toLowerCase().contains("sequence")){
        	if(getPeriod().contains("troisième")) {
        		return ""+(report.getDiscipline().getNonJustifieSeq3()-report.getDiscipline().getJustifieSeq3());
        	}
    		else if(getPeriod().contains("deuxième")) {
    			return ""+(report.getDiscipline().getNonJustifieSeq2()-report.getDiscipline().getJustifieSeq2());
    		}
    		else if(getPeriod().contains("première")) {
    			return ""+(report.getDiscipline().getNonJustifieSeq1()-report.getDiscipline().getJustifieSeq1());
    		}
    		else if(getPeriod().contains("quatrième")) {
    			return ""+(report.getDiscipline().getNonJustifieSeq4()-report.getDiscipline().getJustifieSeq4());
    		}
    		else if(getPeriod().contains("cinquième")){
    			return ""+(report.getDiscipline().getNonJustifieSeq5()-report.getDiscipline().getJustifieSeq5());
    		}
    		else if(getPeriod().contains("sixième")) {
    			return ""+(report.getDiscipline().getNonJustifieSeq6()-report.getDiscipline().getJustifieSeq6());
    		}
        }
		return "/";
	}
	
	/**
	  * Cette méthode permet d'imprimé le box des observations
	  * @param g2 objet permettant de dessiner
	  * @param positionX coordonnée de l'abcisse X
	  * @param positionY coordonnée de l'ordonnée Y
	  */
	
	public int drawCaseObservation(Graphics2D g2,int positionX, int positionY){
		if(period.trim().contains("sequence")) return drawCaseObservationSequence(g2, positionX, positionY);
		else if(period.trim().contains("trimestre")) return drawCaseObservationTrimestre(g2, positionX, positionY);
		else if(period.trim().contains("annuel")) return drawCaseObservationAnnuel(g2, positionX, positionY);
		return positionY;
	}
	
	/**
	  * Cette méthode permet d'imprimé le box des observations trimestrielles
	  * @param g2 objet permettant de dessiner
	  * @param positionX coordonnée de l'abcisse X
	  * @param positionY coordonnée de l'ordonnée Y
	  */
	public int drawCaseObservationTrimestre(Graphics2D g2,int positionX, int positionY){
		g2.setColor(Color.LIGHT_GRAY);
        g2.fillRect(positionX, positionY, 276, 15);
        g2.setColor(Color.black);
        g2.drawRect(positionX, positionY, 276, 15);
        g2.drawString("OBSERVATION ET VISA DU PARENT", positionX+10, positionY+10);
        g2.drawRect(positionX, positionY, 276, 842-(positionY+15));
        
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRect(positionX+276, positionY, 279, 15);
        g2.setColor(Color.black);
        g2.drawRect(positionX+276, positionY, 279, 15);
        g2.drawString("OBSERVATION ET VISA DU CHEF D'ETABLISSEMENT", positionX+276+10, positionY+10);
        g2.drawRect(positionX+276, positionY, 279, 842-(positionY+15));
        
		return positionY+842-(positionY+15);
	}
	
	/**
	  * Cette méthode permet d'imprimé le box des observations sequentielles
	  * @param g2 objet permettant de dessiner
	  * @param positionX coordonnée de l'abcisse X
	  * @param positionY coordonnée de l'ordonnée Y
	  */
	public int drawCaseObservationSequence(Graphics2D g2,int positionX, int positionY){
		g2.setColor(Color.LIGHT_GRAY);
        g2.fillRect(positionX, positionY, 276, 15);
        g2.setColor(Color.black);
        g2.drawRect(positionX, positionY, 276, 15);
        g2.drawString("OBSERVATION ET VISA DU SURVEILLANT GENERAL", positionX+10, positionY+10);
        g2.drawRect(positionX, positionY, 276, (842-(positionY+15))/2);
        
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRect(positionX, positionY+(842-(positionY+15))/2, 276, 15);
        g2.setColor(Color.black);
        g2.drawRect(positionX, positionY+(842-(positionY+15))/2, 276, 15);
        g2.drawString("OBSERVATION ET VISA DU PARENT", positionX+10, positionY+(842-(positionY+15))/2+10);
        g2.drawRect(positionX, positionY+(842-(positionY+15))/2, 276, (842-(positionY+15))/2);
        
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRect(positionX+276, positionY, 279, 15);
        g2.setColor(Color.black);
        g2.drawRect(positionX+276, positionY, 279, 15);
        g2.drawString("OBSERVATION ET VISA DU PROFESSEUR PRINCIPAL", positionX+276+10, positionY+10);
        g2.drawRect(positionX+276, positionY, 279, 842-(positionY+15));
        
		return positionY+842-(positionY+15);
	}
	
	/**
	  * Cette méthode permet d'imprimé le box des observations annuelles
	  * @param g2 objet permettant de dessiner
	  * @param positionX coordonnée de l'abcisse X
	  * @param positionY coordonnée de l'ordonnée Y
	  */
	
	public int drawCaseObservationAnnuel(Graphics2D g2,int positionX, int positionY){
		g2.setColor(Color.LIGHT_GRAY);
        g2.fillRect(positionX, positionY, 276, 15);
        g2.setColor(Color.black);
        g2.drawRect(positionX, positionY, 276, 15);
        g2.drawString("OBSERVATION ET VISA DU PARENT", positionX+10, positionY+10);
        g2.drawRect(positionX, positionY, 276, 842-(positionY+15));
        
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRect(positionX+276, positionY, 279, 15);
        g2.setColor(Color.black);
        g2.drawRect(positionX+276, positionY, 279, 15);
        g2.drawString("OBSERVATION ET VISA DU CHEF D'ETABLISSEMENT", positionX+276+10, positionY+10);
        g2.drawRect(positionX+276, positionY, 279, 842-(positionY+15));
        
		return positionY+842-(positionY+15);
	}
	
	/**
	  * Cette méthode permet d'imprimé l'entête du tableau des notes et matières
	  * @param g2 objet permettant de dessiner
	  * @param positionX coordonnée de l'abcisse X
	  * @param positionY coordonnée de l'ordonnée Y
	  */
	public int drawCaseEntete(Graphics2D g2,int positionX, int positionY){
		g2.setColor(Color.LIGHT_GRAY);
        g2.fillRect(positionX, positionY, 100, 15);
        g2.setColor(Color.black);
        g2.drawRect(positionX, positionY, 100, 15);
        g2.drawString("MATIERE", positionX+40, positionY+10);
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRect(positionX+100, positionY, 300, 15);
        g2.setColor(Color.black);
        g2.drawRect(positionX+100, positionY, 200, 15);
        g2.drawString("SEQUENCE", positionX+100+80, positionY+10);
        
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRect(positionX+100+200, positionY, 40, 15);
        g2.setColor(Color.black);
        g2.drawRect(positionX+100+200, positionY, 40, 15);
        g2.drawString("COEF", positionX+100+200+15, positionY+10);
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRect(positionX+100+200+40, positionY, 40, 15);
        g2.setColor(Color.black);
        g2.drawRect(positionX+100+200+40, positionY, 40, 15);
        g2.drawString("MOY", positionX+100+200+40+15, positionY+10);
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRect(positionX+100+200+40+40, positionY, 40, 15);
        g2.setColor(Color.black);
        g2.drawRect(positionX+100+200+40+40, positionY, 40, 15);
        g2.drawString("MxC", positionX+100+200+40+40+15, positionY+10);
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRect(positionX+100+200+40+40+40, positionY, 40, 15);
        g2.setColor(Color.black);
        g2.drawRect(positionX+100+200+40+40+40, positionY, 40, 15);
        g2.drawString("RANG", positionX+100+200+40+40+40+15, positionY+10);
        
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRect(positionX+100+200+40+40+40+40, positionY, 90, 15);
        g2.setColor(Color.black);
        g2.drawRect(positionX+100+200+40+40+40+40, positionY, 90, 15);
        g2.drawString("OBSERVATION", positionX+100+300+55+30, positionY+10);
        return positionY+15;
	}
	
	
	public Impression setReports(ReportsCards reports) {
		this.reports = reports;
		return this;
	}

	public String getPeriod() {
		return period;
	}

	
	public String getStatusPeriod() {
		if(period.trim().toLowerCase().contains("sequence")) return "SEQUENTIELLE";
		else if(period.trim().toLowerCase().contains("trimestre")) return "TRIMESTRIELLE";
		else if(period.trim().toLowerCase().contains("annuel")) return "ANNUELLE";
		return "";
	}
	
	/**
	  * Cette méthode permet de configuré le nom à donner au bulletin
	  * @param period la valeur de la period pouvant être 'sequence1','sequence3', 'trimestre3', 'trimestre1' ou 'annuel'
	  */
	
	public Impression setPeriod(String period) throws Exception {
		period = period.trim().toLowerCase();
		if(period.trim().toLowerCase().contains("sequence1"))
			this.period = "Bulletin de la première sequence";
		else if(period.trim().toLowerCase().contains("sequence2"))
			this.period = "Bulletin de la deuxième sequence";
		else if(period.trim().toLowerCase().contains("sequence3"))
			this.period = "Bulletin de la troisième sequence";
		else if(period.trim().toLowerCase().contains("sequence4"))
			this.period = "Bulletin de la quatrième sequence";
		else if(period.trim().toLowerCase().contains("sequence5"))
			this.period = "Bulletin de la cinquième sequence";
		else if(period.trim().toLowerCase().contains("sequence6"))
			this.period = "Bulletin de la sixième sequence";
		else if(period.trim().toLowerCase().contains("trimestre1"))
			this.period = "Bulletin du premier trimestre";
		else if(period.trim().toLowerCase().contains("trimestre2"))
			this.period = "Bulletin du deuxième trimestre";
		else if(period.trim().toLowerCase().contains("trimestre3"))
			this.period = "Bulletin du troisième trimestre";
		else if(period.trim().toLowerCase().contains("annuel"))
			this.period = "Bulletin annuel";
		else throw new Exception("Format incorrect !!!! format acceptable : sequence<num> ou trimestre<num> ou annuel");
		return this;
	}

	/**
	  * Cette méthode permet compter le nombre d'espace entre les mots d'un chaine
	  * @param word la chaine à traiter
	  * @return le nombre d'espace
	  */
	
	private int espaceWord(String word){
        int i=0,num=0;
        while(i<word.length()){
            if(word.charAt(i)==' ') num++;
            i++;
        }
        return num;
    }
	
	/**
	  * Cette méthode permet d'arrondir à deux chiffres après la virgule
	  * @param strValeur la valeur réelle a traiter
	  * @return la valeur avec deux chiffres après la virgule
	  */
	private String trimValue(String strValeur){
		try{
			BigDecimal valeur = (new BigDecimal(strValeur)).setScale(2, BigDecimal.ROUND_HALF_DOWN);
			if(valeur.doubleValue()<10) return "0"+valeur.toString();
		    return valeur.toString();
		}catch(NumberFormatException ex){return "/";}   
	}
	
	/**
	  * Cette méthode permet d'arrondir les noms des enseignants des matières
	  * @param name la valeur réelle a traiter
	  * @return le nom arrondi
	  */
	private String trimNom(String name) {
        if ((name == null) || name.trim().isEmpty()) {
            return "M. ";
        }
        name = name.trim();
        if (name.toLowerCase().trim().indexOf("m.") != 0 && name.toLowerCase().trim().indexOf("m ") != 0
                && name.toLowerCase().trim().indexOf("mr ") != 0 && name.toLowerCase().trim().indexOf("mr.") != 0
                && name.toLowerCase().trim().indexOf("abbe ") != 0 && name.toLowerCase().trim().indexOf("abbe.") != 0
                && name.toLowerCase().trim().indexOf("ab.") != 0 && name.toLowerCase().trim().indexOf("ab ") != 0
                && name.toLowerCase().trim().indexOf("pr ") != 0 && name.toLowerCase().trim().indexOf("pr.") != 0
                && name.toLowerCase().trim().indexOf("mm ") != 0 && name.toLowerCase().trim().indexOf("mm.") != 0
                && name.toLowerCase().trim().indexOf("sr.") != 0 && name.toLowerCase().trim().indexOf("sr ") != 0
                && name.toLowerCase().trim().indexOf("mme.") != 0 && name.toLowerCase().trim().indexOf("mme ") != 0
                && name.toLowerCase().trim().indexOf("mgr.") != 0 && name.toLowerCase().trim().indexOf("mgr ") != 0
                && name.toLowerCase().trim().indexOf("père.") != 0 && name.toLowerCase().trim().indexOf("père ") != 0) {
            name = "M. " + name;
        }
        if (name.length() > 22) {
            return name.substring(0, 21) + ".";
        } else {
            return name;
        }
    }
	
	/**
	  * Cette méthode permet d'arrondir les noms des matières
	  * @param name la valeur réelle a traiter
	  * @return le nom arrondi
	  */
	private String trimDiscipline(String arg) {
		if(arg.trim().isEmpty()) return "/";
        if (arg.length() <= 25) {
            return arg;
        }
        String value = "";

        String[] data = arg.split(" ");
        if (data.length < 2) {
            data = arg.split("/");
        }
        if (1 < data.length) {
            for (int i = 0; i != data.length; i++) {
                if (4 < data[i].length()) {
                    value += data[i].substring(0, 3) + ". ";
                }
            }
        }
        return value;
    }
	
	private String observation(String Val) {
        String app = "";
        try {
            if (!Val.trim().equals("")) {
                if ((0 <= Float.parseFloat(Val)) && (Float.parseFloat(Val) < 4)) {
                    app = "nul";
                }
                if ((4 <= Float.parseFloat(Val)) && (Float.parseFloat(Val) < 8)) {
                    app = "Faible";
                }
                if ((8 <= Float.parseFloat(Val)) && (Float.parseFloat(Val) < 10)) {
                    app = "Mediocre";
                }
                if ((10 <= Float.parseFloat(Val)) && (Float.parseFloat(Val) < 12)) {
                    app = "Passable";
                }
                if ((12 <= Float.parseFloat(Val)) && (Float.parseFloat(Val) < 14)) {
                    app = "Assez Bien";
                }
                if ((14 <= Float.parseFloat(Val)) && (Float.parseFloat(Val) < 17)) {
                    app = "Bien";
                }
                if ((17 <= Float.parseFloat(Val)) && (Float.parseFloat(Val) < 20)) {
                    app = "Très Bien";
                }
                if (20 == Float.parseFloat(Val)) {
                    app = "Parfait";
                }
            }
        } catch (NumberFormatException ex) {}
        return app;
    }
	
	public String appreciation(String Val) {
        String app = "";
        try {
            if (!Val.trim().equals("")) {
            	if ((0 <= Float.parseFloat(Val)) && (Float.parseFloat(Val) < 4)) {
                    app = "nul<br>Blâme pour travail";
                }
                if ((4 <= Float.parseFloat(Val)) && (Float.parseFloat(Val) < 7.5)) {
                    app = "Faible<br>Blâme pour travail";
                }
                if ((7.5 <= Float.parseFloat(Val)) && (Float.parseFloat(Val) < 10)) {
                    app = "Mediocre<br>Avertissement travail";
                }
                if ((10 <= Float.parseFloat(Val)) && (Float.parseFloat(Val) < 12)) {
                    app = "Passable<br>Encouragement";
                }
                if ((12 <= Float.parseFloat(Val)) && (Float.parseFloat(Val) < 14)) {
                    app = "Assez Bien<br>Encouragement";
                }
                if ((14 <= Float.parseFloat(Val)) && (Float.parseFloat(Val) < 17)) {
                    app = "Bien<br>Félicitation";
                }
                if ((17 <= Float.parseFloat(Val)) && (Float.parseFloat(Val) < 20)) {
                    app = "Très Bien<br>Excellent";
                }
                if (20 == Float.parseFloat(Val)) {
                    app = "Parfait<br>Excellent";
                }

                if (12<= Float.parseFloat(Val)&&!getPeriod().trim().toLowerCase().contains("sequence")) {
                    app += "<br>Tableau d'honneur";
                }
                app = app.toUpperCase();
            }
        } catch (NumberFormatException ex) {
        }
        return app;
    }
}
