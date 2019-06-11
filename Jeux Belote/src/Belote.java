/*
 * deroulement de la partie
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class Belote extends JFrame implements ActionListener {

	/**
	 * @param args
	 * 
	 * 
	 */

	Joueur joueur[]=new Joueur[4];

	Tapis tapis = new Tapis(joueur);

	int reponse = -1;

	int prend = 4;// variable pour savoir qui prend

	String jeuxAtout = "blanc";// variable pour la couleur d'atout

	// qui donne le jeux
	int donne = 0;

	int n1 = 0;

	int gagne = 0;

	int nJoueur = 0;// nb de joueur

	boolean manchej;

	Carte retourne;

	String cartejoue = "";

	Pli lePli = new Pli();

	String couleurJouee = "Blanc";
	
	String choixatout=null;

	Paquet paquet = new Paquet();

	Carte joue = null;

	String lacoupe;
	
	private JMenuBar barreMenus ;
	
	private JMenuItem fermer;

	Belote() {
		setTitle("Belote");
		setSize(615,660);
		joueur[0]= new Joueur(this);
		joueur[1]= new Joueur();
		joueur[2]= new Joueur();
		joueur[3]= new Joueur();
		getContentPane().add(tapis);
		barreMenus = new JMenuBar();
		setJMenuBar(barreMenus);
		fermer = new JMenuItem ("Fermer");
		barreMenus.add(fermer);
		fermer.addActionListener(this);
		addWindowListener(new WindowAdapter() {
			// ferme la fenetre
			public void windowClosing(WindowEvent e) {
				dispose();
				System.exit(0);
			}
		});
		setVisible(true);
	}
	
	public void actionPerformed (ActionEvent ev){
		System.exit(0);
	}
	
	void jeu() {
		paquet.bat();
		while (tapis.totalEstOuest <1000 && tapis.totalNordSud<1000 ) {
			n1 = donne;
			nJoueur = 0;
			// donne 3 cartes
			while (nJoueur != 4) {
				n1++;
				if (n1 > 3) n1 = 0;
				paquet.distribue(joueur[n1], 3);
				nJoueur++;
			}
			// donne 2 cartes
			n1 = donne;
			nJoueur = 0;
			while (nJoueur != 4) {
				n1++;
				if (n1 > 3) n1 = 0;
				paquet.distribue(joueur[n1], 2);
				nJoueur++;
			}
			retourne = paquet.retourne();
			jeuxAtout = retourne.getCouleur();
			tapis.affiche();
			// affiche le jeux des joueurs
			tapis.affichecarter(retourne, jeuxAtout);
			// prend premier tour
			boolean preneur = false;
			n1 = donne;
			nJoueur = 0;
			while ((!preneur) && (nJoueur != 4)) {
				n1++;
				if (n1 > 3) n1 = 0;
				preneur = joueur[n1].prendpremier(n1, retourne);
				if (preneur) {
					prend = n1;
				}
				nJoueur++;
			}
			// prend deuxieme tour
			if (!preneur) {
				n1 = donne;
				nJoueur = 0;
				while ((!preneur) && (nJoueur != 4)) {
					n1++;
					if (n1 > 3) n1 = 0;
					preneur = joueur[n1].prenddeuxieme(n1, retourne);
					if (preneur) {
						prend = n1;
						jeuxAtout = joueur[n1].choixatout;
						preneur = true;
					}
					nJoueur++;
				}
			}
			if (!preneur) {
				//pas de preneur
				// remet les cartes dans le paquet
				for (int i = 0; i < 4; i++) {
					paquet.lePaquet.addAll(joueur[i].main);
					joueur[i].main.clear();
				}
				paquet.lePaquet.add(retourne.clone());
				retourne=null;
				tapis.affiche();
			} else {
				tapis.joueurpris = "Joueur " + prend;
				// distribution du reste des cartes
				n1 = donne;
				nJoueur = 0;
				while (nJoueur != 4) {
					n1++;
					if (n1 > 3) n1 = 0;
					if (prend == n1) {
						paquet.distribue(joueur[n1], 2);
						joueur[n1].recoit(retourne);
					} else {
						paquet.distribue(joueur[n1], 3);
					}
					nJoueur++;
				}
				joueur[0].trijeux();
				tapis.affichecarter(null, jeuxAtout);
				for (int tour = 0; tour < 8; tour++) {
					if (tour == 0) {
						n1 = donne + 1;
					} else {
						n1 = gagne;
					}
					nJoueur = 0;
					cartejoue = "";
					while (nJoueur != 4) {
						if (n1 > 3)
							n1 = 0;
						if (nJoueur == 0)
							couleurJouee = "blanc";
						if (n1 == 0) {
							boolean test = false;
							while (test != true) {
								reponse = -1;
								tapis.setReponse(reponse);
								while (reponse == -1)
									reponse = tapis.getReponse();
								joue = joueur[n1].main.get(reponse);
								if (couleurJouee.equals("Blanc")) {
									test = true;
								} else
									test = Arbitre.testcartejouee(
											joueur[n1].main, joue,
											couleurJouee, jeuxAtout);
								if (test == true) {
									lePli.cartePli[n1] = joueur[n1].main.get(
											reponse).clone();
									joueur[n1].main.remove(reponse);
								}
							}
						} else {
							lePli.cartePli[n1] = joueur[n1].ordiJoue(
									couleurJouee, jeuxAtout);
						}
						if (nJoueur == 0) {
							couleurJouee = lePli.cartePli[n1].couleur.nom;
						}
						tapis.afficheJoue(lePli.cartePli[n1], n1);
						nJoueur++;
						n1++;
					}
					gagne = lePli.remportePli(couleurJouee, jeuxAtout, tour);
					try {
						new Thread().sleep(2000);
					} catch (Exception e) {
					}
					tapis.efface();
				}
				lePli.gagnePartie();
				if (lePli.pointSudNord == 0 || lePli.pointEstOuest == 0) {
					if (lePli.pointSudNord == 0) {
						tapis.totalEstOuest = tapis.totalEstOuest + 250;
					} else {
						tapis.totalNordSud = tapis.totalNordSud + 250;
					}
				} else {
					manchej = Arbitre.Gagnemanche(lePli.pointSudNord,
							lePli.pointEstOuest, prend);
					if (manchej == true) {
						tapis.totalNordSud = tapis.totalNordSud
								+ lePli.pointSudNord;
						tapis.totalEstOuest = tapis.totalEstOuest
								+ lePli.pointEstOuest;
					} else {
						if (prend == 1 || prend == 3) {
							tapis.totalNordSud = tapis.totalNordSud + 162;
						} else {
							tapis.totalEstOuest = tapis.totalEstOuest + 162;
						}
					}
				}
				System.out.println("totalEstOuest = " + tapis.totalEstOuest);
				System.out.println("totalNordSud = " + tapis.totalNordSud);
				paquet.lePaquet.addAll(lePli.tas);
				lePli.tas.clear();
				tapis.efface();
				tapis.affiche();
				lePli.pointSudNord = 0;
				lePli.pointEstOuest = 0;
			}
			
			if(donne==0){
				int hasard=0;
				do{
					lacoupe = JOptionPane
					.showInputDialog("donner un nombre entre 0 et 31 pour la coupe");
					hasard=Integer.parseInt(lacoupe);
				}
				while(!(hasard >= 1 && hasard <= 30));
				paquet.coupe(hasard);
			}
			else paquet.coupe((int) (Math.random() *29)+1);
			donne = Arbitre.quidonne(donne);
		}
		if(tapis.totalEstOuest>tapis.totalNordSud)tapis.gagnant=("est et ouest gagnent le jeu");
		if(tapis.totalEstOuest<tapis.totalNordSud)tapis.gagnant=("sud et nord gagnent le jeu");
		if(tapis.totalEstOuest==tapis.totalNordSud)tapis.gagnant=("jeu nul");
		tapis.affiche();

	}


	public static void main(String[] args) {
		Belote lejeu=new Belote();
		lejeu.jeu();
	}

}
