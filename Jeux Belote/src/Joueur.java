import java.util.ArrayList;

import javax.swing.JFrame;

import javax.swing.JOptionPane;

public class Joueur {
	
	ArrayList<Carte> main = new ArrayList<Carte>();
	boolean prend=false;
	String choixatout="blanc";
	JFrame uneFenetre;
	
	Joueur(){
		main = new ArrayList<Carte>();
	}
	
	Joueur(JFrame fenetre){
		main = new ArrayList<Carte>();
		uneFenetre=fenetre;
	}
	public boolean prendpremier(int y,Carte ret){
		int reponse=0;
		int totalpoint=0;
		int points=0;
		
		switch(y){
			case 0 :
				reponse=JOptionPane.showConfirmDialog(null, 
						"Vous voulez prendre aux premier tour ?", "Vous voulez prendre aux premier tour ?", JOptionPane.YES_NO_OPTION);
				if(reponse==0){
					prend=true;
				}else{
					prend=false;	
				}
				break;
			default :
				for (int v=0 ; v<5; v++){
					points=Arbitre.Points(main.get(v),ret.couleur.nom);
					totalpoint=totalpoint+points;
				}
				if (totalpoint>=40){
					prend=true;
				}else{
					prend=false;	
				}
				break;
		}
		return prend;
		
	}
	
	public boolean prenddeuxieme(int y,Carte ret){
		int reponse=0;
		int totalpoint=0;
		int points=0;
		String[] couleurAtout={"Coeur","Pique","Trefle","Carreau"};		
		int Pointint=0;
		// En fonction du joueur
		switch(y){
			case 0 :
				reponse=JOptionPane.showConfirmDialog(null, 
						"Vous voulez prendre aux deuxieme tour ?", "Vous voulez prendre aux deuxieme tour ?", JOptionPane.YES_NO_OPTION);
				if(reponse==0){
					ChoixAtout ledialog=null;
					choixatout="";
					ledialog=new ChoixAtout(uneFenetre,choixatout);
					ledialog.dispose();
					choixatout=ledialog.getTexte();
					prend=true;
				}else{
					prend=false;	
				}
				break;
			default :
				// boucle de teste sur les 4 couleurs pour l'ordinateur
				for(int u=0 ; u<4; u++){
				totalpoint=0;		
					for (int v=0 ; v<5; v++){
						points=Arbitre.Points(main.get(v),couleurAtout[u]);
						totalpoint=totalpoint+points;
					}
					totalpoint=totalpoint+Arbitre.Points(ret,couleurAtout[u]);
					if (Pointint<totalpoint){
						Pointint=totalpoint;
						choixatout=couleurAtout[u];
					}
				}
			//test pour la prise
			if (Pointint>=40){
				prend=true;	
			}else{
				prend=false;
			}		
			break;				
		}
		return prend;
	}
	
	public void recoit(Carte lacarte){
		main.add(lacarte);
	}
	
	
	public Carte sudJoue(int reponse,String couleurJouee, String atout){
		Carte joue=null;
		boolean test;
		test=false;
		while (test!=true){
			joue=main.get(reponse).clone();
			if(couleurJouee.equals("Blanc")){
				test=true;
			}
			else test=Arbitre.testcartejouee(main,joue,couleurJouee,atout);
			if(test==true){
				main.remove(reponse);
			}	
		}
		return joue;
		
	}
	
	public Carte ordiJoue(String couleurJouee, String atout){
		Carte renvoijouee;
		int rep;
		rep=Arbitre.testcartejouee2(main,couleurJouee,atout);
		renvoijouee=main.get(rep).clone();
		main.remove(rep);
		return renvoijouee;
	}
	
	public void trijeux(){
		boolean recup;
		ArrayList<Carte> Paquettampon= new ArrayList<Carte>(8);
		String[]couleurtri={"Coeur","Trefle","Carreau","Pique"};
		for (int x= 0 ; x<4 ; x++){
			for (int y = 0 ; y<8 ;y++){	
				recup=couleurtri[x].equals(main.get(y).getCouleur());
				if (recup == true){
					Paquettampon.add(main.get(y));
				}
			}
		}
		main.clear();
		main.addAll(Paquettampon);
	}
}


