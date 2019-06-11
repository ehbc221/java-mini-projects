import java.util.ArrayList;

import javax.swing.JOptionPane;

public class Arbitre{
	
	/*la classe abitre est unique donc static
	 * elle comporte les méthodes suivantes
	 * 1)détermine le donneur
	 * 2)
	 * 3)
	 */
	
	private Arbitre(){}
	
	public static int quidonne(int donne){
		//affiche avant le traitement qui doit donner le jeux
		switch(donne){
			case 0: donne = 1;
				break;
			case 1: donne = 2;
				break;
			case 2: donne = 3;
				break;
			case 3 : donne = 0;
				break;
		}
		return donne;
	}

	//======================================
	// renvois la valeur d'une carte	
	public static int rangPli(String couleurJouee,Carte x,String atout){
		int point1=0;
		String vcouleur=x.getCouleur();
		//vfigure=fig2(x);
		if (atout.equals(vcouleur)){
			point1=x.figure.valeurAtout;	
		}
		else{
			if (couleurJouee.equals(vcouleur)){
				point1=x.figure.valeur;	
			}else{
				point1=0;	
			}
		}
		return point1;
	}
	//renvois le nombre de points d'une carte	
	public static int Points(Carte x,String y){
		int point1=0;
		String vcouleur;
		vcouleur=x.couleur.nom;
		//vfigure=fig2(x);
		if (y==vcouleur){
			point1=x.figure.pointAtout;
		}else{
			point1=x.figure.point;	
		}
		return point1;
	}
	//=====================================================
	//gagne la manche
	public static boolean Gagnemanche(int equipeSudNord,int equipeEstouest,int y){
		boolean v= false;
		if (y==1 || y==3){
			if(equipeSudNord<equipeEstouest){
				v=true;
			}else{
				v=false;
			}
		}else{
			if(equipeSudNord>equipeEstouest){
				v=true;
			}else{
				v=false;
			}
		}
		return v;
	}

	//==============================================
	//test carte jouer
	public static boolean testcartejouee(ArrayList<Carte> main,Carte joue,String couleurJouee,String atout){
		boolean valide=false;
		int taille=main.size();
		System.out.println(couleurJouee);
		if(couleurJouee.equals("blanc")) valide=true;
		testc :	if(valide){

		}else{
			if(joue.getCouleur().equals(couleurJouee)) valide=true;//test si la carte est de la couleur du jeux 
			else{
				//test si une des cartes est de la couleur du jeux 
				for(int i=0 ; i<taille ;i++){
					if(main.get(i).getCouleur().equals(couleurJouee)){
						valide=false;
						JOptionPane.showMessageDialog(null, "Jouer une carte de la couleur demander, "+couleurJouee, "Jouer une carte de la couleur demander "+couleurJouee, JOptionPane.PLAIN_MESSAGE);
						break testc;
					}
				}
				//Test si la carte est de la couleur de l'atout
				if (joue.getCouleur().equals(atout)){
					valide=true;
					break testc;		
				}else{
					// test si au moins une carte est de la couleur de l'atout
					for(int i=0 ; i<taille ;i++){
						if(main.get(i).getCouleur().equals(atout)){
							valide=false;
							JOptionPane.showMessageDialog(null, "Jouer une carte d'atout, "+atout,"Jouer une carte d'atout, "+atout, JOptionPane.PLAIN_MESSAGE);
							break testc;
						}
					}
					valide=true;
					break testc;
				}
				
			}
		}
		return valide;
	}
	//========================================================
	//test pour jeux nieme
	public static int testcartejouee2(ArrayList<Carte> main,String couleurJouee,String atout){
		int taille=main.size();
		int v=0;
		boolean test=false;
		if(!(couleurJouee.equals("Blanc"))){
		testc :	if(test==false){
				for(int i=0 ; i<taille ;i++){
					test=main.get(i).getCouleur().equals(couleurJouee);
					if(test==true){
						v=i;
						break testc ;
					}	
				}
				for(int i=0 ; i<taille ;i++){
					test=main.get(i).getCouleur().equals(atout);
					if(test==true){
						v=i;
						break testc ;
					}	
				}
				v=0;
			}
		}
		return v;
	}
}