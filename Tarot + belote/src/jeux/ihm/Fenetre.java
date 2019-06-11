package jeux.ihm;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import jeux.cartes.*;
import jeux.*;
import jeux.parties.*;
import jeux.encheres.*;
import jeux.enumerations.*;
import jeux.ihm.dialogues.*;
import jeux.ihm.etiquettes.*;
import jeux.ihm.panneaux.*;
import jeux.mains.*;
import jeux.plis.*;
/**@author Francois Mercier des Rochettes<br/>
 *Fenetre principale non redimensionnable
 *Au premier lancement, il y une barre de menus et quatre boutons de jeu*/
public class Fenetre extends JFrame {
	private static final long serialVersionUID = 1L;
	/**Jeu auquel on veut jouer*/
	private Jeu jeu;
	/**Mode de jeu auquel on veut jouer*/
	private String mode;
	/**Parametres de lancement, de jouerie et d'informations sur
	 * des pseudonymes*/
	private Parametres param;
	/**Est vrai si et seulement si une partie est en cours
	 * Utilise pour le declenchement de la sauvegarde lors d'une tentative de fermeture
	 * ou d'un declenchement d'une autre partie*/
	private boolean enCoursDePartie;
	/**La partie actuellement jouee*/
	private Partie par;
	/**Est vrai si et seulement si l'animation a lieu au debut d'un pli*/
	private boolean debutPli;
	/**Renvoie tous les scores de toutes les parties non solitaires*/
	private Vector<Vector<Long>> scores=new Vector<Vector<Long>>();
	/**Ecarts types des parties cumule&eacute;es*/
	private Vector<Double> sigmas=new Vector<Double>();
	/**Sommes des scores des joueurs*/
	private Vector<Long> sommes=new Vector<Long>();
	/**Maximum des valeurs absolues des scores centr&eacute;s par rapport &agrave; la moyenne*/
	private double max_absolu_score;
	private boolean thread_anime;
	private boolean a_joue_carte;
	private boolean pause;
	/**Est vrai si et seulement si une partie vient d'etre sauvegardee*/
	private boolean partie_sauvegardee;
	/**Est vrai si et seulement si on vient de charger une partie*/
	private boolean charge;
	/**Vrai si et seulement si au moins une partie aleatoire a ete jouee depuis le dernier passage dans le menu principal*/
	private boolean partie_aleatoire_jouee;
	/**Carte survol&eacute;e par la souris*/
	private Carte carte_survolee;
	private boolean carte_entree;
	private boolean carte_sortie;
	public static final char quote=Lettre.quote;
	public static final char quote2=Lettre.quote2;
	public static final char pt=Lettre.pt;
	public static final char pv=Lettre.pv;
	public static final char vi=Lettre.vi;
	public static final char pe=Lettre.pe;
	public static final char pi=Lettre.pi;
	public static final char tt=Lettre.tt;
	public static final char et=Lettre.et;
	public static final char ps=Lettre.ps;
	public static final char pd=Lettre.pd;
	public static final char pg=Lettre.pg;
	public static final char cd=Lettre.cd;
	public static final char cg=Lettre.cg;
	public static final char in=Lettre.in;
	public static final char ega=Lettre.ega;
	public static final char su=Lettre.su;
	public static final char sl=Lettre.sl;
	public static final char as=Lettre.as;
	public static final char ch=Lettre.ch;
	public static final char pp=Lettre.pp;
	public static final char pc=Lettre.pc;
	public static final char _=Lettre._;
	public static final char c0=Lettre.c0;
	public static final char c1=Lettre.c1;
	public static final char c2=Lettre.c2;
	public static final char c3=Lettre.c3;
	public static final char c4=Lettre.c4;
	public static final char c5=Lettre.c5;
	public static final char c6=Lettre.c6;
	public static final char c7=Lettre.c7;
	public static final char c8=Lettre.c8;
	public static final char c9=Lettre.c9;
	public static final char A=Lettre.A;
	public static final char a=Lettre.a;
	public static final char ac=Lettre.ac;
	public static final char ag=Lettre.ag;
	public static final char B=Lettre.B;
	public static final char b=Lettre.b;
	public static final char C=Lettre.C;
	public static final char c=Lettre.c;
	public static final char cc=Lettre.cc;
	public static final char D=Lettre.D;
	public static final char d=Lettre.d;
	public static final char E=Lettre.E;
	public static final char e=Lettre.e;
	public static final char ec=Lettre.ec;
	public static final char eg=Lettre.eg;
	public static final char F=Lettre.F;
	public static final char f=Lettre.f;
	public static final char G=Lettre.G;
	public static final char g=Lettre.g;
	public static final char H=Lettre.H;
	public static final char h=Lettre.h;
	public static final char I=Lettre.I;
	public static final char i=Lettre.i;
	public static final char ic=Lettre.ic;
	public static final char J=Lettre.J;
	public static final char j=Lettre.j;
	public static final char K=Lettre.K;
	public static final char k=Lettre.k;
	public static final char L=Lettre.L;
	public static final char l=Lettre.l;
	public static final char M=Lettre.M;
	public static final char m=Lettre.m;
	public static final char N=Lettre.N;
	public static final char n=Lettre.n;
	public static final char O=Lettre.O;
	public static final char o=Lettre.o;
	public static final char oc=Lettre.oc;
	public static final char P=Lettre.P;
	public static final char p=Lettre.p;
	public static final char Q=Lettre.Q;
	public static final char q=Lettre.q;
	public static final char R=Lettre.R;
	public static final char r=Lettre.r;
	public static final char S=Lettre.S;
	public static final char s=Lettre.s;
	public static final char T=Lettre.T;
	public static final char t=Lettre.t;
	public static final char U=Lettre.U;
	public static final char u=Lettre.u;
	public static final char uc=Lettre.uc;
	public static final char ug=Lettre.ug;
	public static final char V=Lettre.V;
	public static final char v=Lettre.v;
	public static final char W=Lettre.W;
	public static final char w=Lettre.w;
	public static final char X=Lettre.X;
	public static final char x=Lettre.x;
	public static final char Y=Lettre.Y;
	public static final char y=Lettre.y;
	public static final char Z=Lettre.Z;
	public static final char z=Lettre.z;
	private static final String ch_v=Lettre.ch_v;
	private static final char st=Lettre.st;
	private static final char p2=Lettre.p2;
	private static final String tb=ch_v+Lettre.tb;
	private static final char ea=Lettre.ea;
	private static final char es=Lettre.es;
	private static final String aide_au_jeu;
	static{
		aide_au_jeu="Aide au jeu";
	}
	private static final String aide_generale="Aide g"+ea+"n"+ea+"rale";
	private static final String retour="Revenir au menu joueurs";
	/**Donne les raisons pour lesquelles on ne peut pas jouer une ou plusieurs cartes
	 * (Les numeros juste avant les chaines representent les indices correspondant aux chaines.)
	 * <ol><li>0 "C'est la fin d'un pli"</li>
	 * <li>1 "Vous avez deja joue une carte"</li>
	 * <li>2 "Attendez votre tour"</li></ol>*/
	private static String[] raisons={"C'est la fin d'un pli","Vous avez deja joue une carte","Attendez votre tour"};
	private String raison_courante=ch_v;
	private AnimationChargement anim_chargement;
	private AnimationSimulation animationSimulation;
	private AnimationCarte anim_carte;
	private boolean premier_pli_fait;
	private AnimationContrat anim_contrat;
	/**Est vrai si et seulement si le jeu est en pause*/
	private boolean passe;
	/**Indique les noms des menus utilis&eacute;s pour la barre des menus du logiciel
	 * <ol>
	 * <li>Fichier</li>
	 * <li>Partie</li>
	 * <li>Parametres</li>
	 * <li>Aide</li>
	 * <li>A propos</li>
	 * </ol>*/
	public static final String[] menus_utilises={"Fichier","Partie","Parametres","Aide","A propos"};
	/**Indique les noms des menus utilis&eacute;s pour le menu Fichier
	 * <ol>
	 * <li>Charger une partie</li>
	 * <li>Sauvegarder une partie</li>
	 * <li>Changer de mode</li>
	 * <li>Changer de jeu</li>
	 * <li>Supprimer une ou des parties</li>
	 * <li>Quitter</li>
	 * </ol>*/
	public static final String[] sous_menus_utilises1={"Charger une partie","Sauvegarder une partie","Changer de mode","Changer de jeu","Supprimer une ou des parties","Quitter"};
	/**Indique les noms des menus utilis&eacute;s pour le menu Partie
	 * <ol>
	 * <li>Conseil</li>
	 * <li>Pause</li>
	 * <li>Aide au jeu</li>
	 * <li>Editer</li>
	 * <li>D&eacute;mo</li>
	 * <li>Entrainement</li>
	 * </ol>*/
	public static final String[] sous_menus_utilises2={"Conseil","Pause",aide_au_jeu,"Editer","D"+ea+"mo","Entrainement au "+Jeu.Tarot};
	/**Indique les noms des menus utilis&eacute;s sauf le noms des jeux pour le menu Param&egrave;tres
	 * <ol>
	 * <li>Joueurs</li>
	 * <li>Option de lancement</li>
	 * <li>Temporisation du jeu</li>
	 * <li>Interactions avec les cartes</li>
	 * </ol>*/
	public static final String[] sous_menus_utilises3={"Joueurs","Option de lancement","Temporisation du jeu","Interactions avec les cartes"};
	/**Indique les noms des menus utilis&eacute;s pour les menus Aide et A propos
	 * <ol>
	 * <li>Aide g&eacute;n&eacute;rale</li>
	 * <li>Auteur</li>
	 * <li>Logiciel</li>
	 * </ol>*/
	public static final String[] sous_menus_utilises4={aide_generale,"Auteur","Logiciel"};
	private Contrat contrat_utilisateur;
	/**Vrai si et seulement si une partie aleatoire a ete cree*/
	private boolean changer_pile_fin;
	private boolean arret_demo;
	/**Charge une partie de cartes d'un fichier
	 * @param fichier le nom du fichier a charger
	 * @exception Exception si il y a un probleme de lecture du fichier et dans
	 * ce cas un message d'erreur de lecture apparait*/
	private void chargerPartie(String fichier) throws Exception
	{
		ObjectInputStream ois=new ObjectInputStream(new BufferedInputStream(new FileInputStream(new File(fichier))));
		par=(Partie)ois.readObject();
		ois.close();
	}
	/**Met en place l'ihm pour l'utilisateur lorsqu'une partie est editee ou chargee d'un fichier*/
	private void chargerPartie()
	{
		//Activer le menu Fichier/Sauvegarder
		getJMenuBar().getMenu(0).getItem(1).setEnabled(true);
		//Activer le menu Fichier/Changer de mode
		getJMenuBar().getMenu(0).getItem(3).setEnabled(true);
		//Desactiver le menu Partie/Demo
		getJMenuBar().getMenu(1).getItem(4).setEnabled(false);
		premier_pli_fait=false;
		passe=false;
		//Desactiver le menu Partie/Pause
		getJMenuBar().getMenu(1).getItem(1).setEnabled(false);
		enCoursDePartie=true;
		charge=true;
		changer_pile_fin=false;
		a_joue_carte=false;
		setTitle(ch_v+par.jeu()+es+par.getMode());
		byte nombreDeJoueurs=par.getNombreDeJoueurs();
		if(par instanceof PartieBelote)
		{
			if(((Levable)par).getPliEnCours()==null)
			{
				Pli.nombreTotal=0;
			}
			else
			{
				Pli.nombreTotal=(byte)(((PartieBelote)par).unionPlis().size()+1);
			}
		}
		else
		{
			Pli pliEncours=((Levable)par).getPliEnCours();
			Vector<Pli> plis_faits=((PartieTarot)par).unionPlis();
			if(pliEncours==null)
			{
				Pli.nombreTotal=0;
			}
			else if(!plis_faits.isEmpty()&&plis_faits.lastElement().getNumero()+1==pliEncours.getNumero())
			{
				Pli.nombreTotal=(byte)(plis_faits.size()+1);
			}
			else
			{
				Pli.nombreTotal=(byte)plis_faits.size();
			}
		}
		String Revoir_pli_precedent=Boutons.Revoir_le_pli_precedent.toString();
		String Pli_suivant=Boutons.Pli_suivant.toString();
		String Passe_au_jeu_de_la_carte=Boutons.Passe_au_jeu_de_la_carte.toString();
		String Fin_de_partie=Boutons.Fin_de_partie.toString();
		String Revoir_chien=Boutons.Revoir_le_chien.toString();
		String Voir_chien=Boutons.Voir_le_chien.toString();
		String Prendre_les_cartes_du_chien=Boutons.Prendre_les_cartes_du_chien.toString();
		String Valider_chien=Boutons.Valider_le_chien.toString();
		if(par instanceof PartieBelote)
		{
			byte donneur=par.getDistribution().getDonneur();
			placer_levable();
			Vector<String> pseudos=pseudos();
			if(par.getEtat()==Etat.Contrat)
			{
				Contrat contrat=((PartieBelote)par).getContrat();
				synchronized (this) {
					afficherMainUtilisateur(false);
					//Activer les conseils
					getJMenuBar().getMenu(1).getItem(0).setEnabled(((PartieBelote)par).getFinEnchere());
					for(int indice=0;indice<((PartieBelote)par).taille_contrats();indice++)
					{
						String texte=((PartieBelote)par).contrat(indice).force()!=1?((PartieBelote)par).contrat(indice).toString():((PartieBelote)par).getCarteAppelee().toString().split(" ")[2];
						ajouterTexteDansZone(pseudos.get((indice+donneur+1)%nombreDeJoueurs)+":"+texte+st);
						ajouterTexteDansPanneau(texte,(byte)((indice+donneur+1)%nombreDeJoueurs));
					}
					pack();
				}
				byte debut=(byte) ((donneur+1+((PartieBelote)par).taille_contrats())%par.getNombreDeJoueurs());
				if(debut>0)
				{
					anim_contrat=new AnimationContrat(pseudos,donneur,debut);
					anim_contrat.start();
				}
				else if(((PartieBelote)par).taille_contrats()>=par.getNombreDeJoueurs()&&((PartieBelote)par).taille_contrats()<2*par.getNombreDeJoueurs()-par.getDistribution().getDonneur())
				{/*L'utilisateur annonce pour le deuxieme tour un contrat*/
					byte couleur=((CarteGraphique)((JPanel)((JPanel)getContentPane().getComponent(1)).getComponent(4)).getComponent(0)).getCarte().couleur();
					if(((PartieBelote)par).getContrat().force()<((Levable)par).max_contrat())
					{
						String passe_contrat=EncheresBelote.Passe.toString();
						ajouterBoutonJeu(passe_contrat,passe_contrat,true);
						String Autre_couleur=EncheresBelote.Autre_couleur.toString();
						for(Couleur coul:Couleur.values())
						{
							if(coul!=Couleur.Atout&&!Carte.chaine_couleur(couleur).equals(coul.toString()))
							{//On recupere les couleurs autre que celle proposee
								ajouterBoutonJeu(coul.toString(),Autre_couleur+coul.position(),new Contrat(Autre_couleur).estDemandable(contrat));
							}
						}
						if(((PartieBelote)par).avecSurContrat())
						{
							String sans_atout=EncheresBelote.Sans_atout.toString();
							String tout_atout=EncheresBelote.Tout_atout.toString();
							ajouterBoutonJeu(sans_atout,sans_atout,new Contrat(sans_atout).estDemandable(contrat));
							ajouterBoutonJeu(tout_atout,tout_atout,true);
						}
					}
					else
					{
						ajouterBoutonJeu(Passe_au_jeu_de_la_carte,Pli_suivant,true);
					}
				}
				else if(((PartieBelote)par).taille_contrats()<par.getNombreDeJoueurs())
				{
					Donne donne=par.getDistribution();
					if(((PartieBelote)par).getContrat().force()<((Levable)par).max_contrat())
					{
						String passe_contrat=EncheresBelote.Passe.toString();
						ajouterBoutonJeu(passe_contrat,passe_contrat,true);
						//L'utilisateur doit monter les encheres pour pouvoir prendre
						String couleur=EncheresBelote.Couleur.toString();
						ajouterBoutonJeu(donne.derniereMain().carte(0).toString().split(" ")[2],couleur,new Contrat(couleur).estDemandable(contrat));
						//L'utilisateur doit monter les encheres pour pouvoir prendre
						if(((PartieBelote)par).avecSurContrat())
						{
							String sans_atout=EncheresBelote.Sans_atout.toString();
							String tout_atout=EncheresBelote.Tout_atout.toString();
							ajouterBoutonJeu(sans_atout,sans_atout,new Contrat(sans_atout).estDemandable(contrat));
							ajouterBoutonJeu(tout_atout,tout_atout,true);
						}
					}
					else
					{
						ajouterBoutonJeu(Passe_au_jeu_de_la_carte,Pli_suivant,true);
					}
				}
				else
				{
					/*On passe au jeu de la carte s'il existe un preneur, a la fin de la partie sinon*/
					if(((PartieBelote)par).getContrat().force()==0)
					{
						ajouterBoutonJeu(Fin_de_partie,Fin_de_partie,true);
					}
					else
					{
						ajouterBoutonJeu(Passe_au_jeu_de_la_carte,Pli_suivant,true);
					}
				}
			}
			else
			{
				getJMenuBar().getMenu(1).getItem(2).setEnabled(true);
				premier_pli_fait=true;
				Pli pliEnCours=((Levable)par).getPliEnCours();
				Carte carte_utilisateur=pliEnCours.carteDuJoueur((byte)0,nombreDeJoueurs,null);
				Main cartes=pliEnCours.getCartes();
				Vector<Byte> joueurs=new Vector<Byte>();
				for(Carte carte:cartes)
				{
					joueurs.addElement(pliEnCours.joueurAyantJoue(carte, nombreDeJoueurs, null));
				}
				byte entameur=pliEnCours.getEntameur();
				int total=pliEnCours.total();
				/*dernier_joueur represente le prochain joueur qui doit jouer*/
				byte dernier_joueur=(byte)((total+entameur)%nombreDeJoueurs);
				for(byte indice_joueur=0;indice_joueur<joueurs.size();indice_joueur++)
				{
					((Tapis)getContentPane().getComponent(1)).setCarte(joueurs.get(indice_joueur), nombreDeJoueurs,cartes.carte(indice_joueur));
				}
				if(carte_utilisateur==null)
				{/*Si l utilisateur n avait pas joue*/
					synchronized (this) {
						afficherMainUtilisateur(true);
						if(((Levable)par).premierTour())
						{
							Vector<Annonce> annonces=((Annoncable)par).getAnnoncesPossibles((byte)0);
							Vector<Annonce> va=new Vector<Annonce>();
							va.addElement(new Annonce(Annonce.belote_rebelote));
							for(Annonce annonce:va)
							{
								ajouterBoutonJeu(annonce.toString(),annonce.toString(),annonces.contains(annonce));
							}
						}
						else
						{
							ajouterBoutonJeu(Revoir_pli_precedent,Revoir_pli_precedent,true);
						}
						if(((Levable)par).getCarteAppelee()!=null)
						{
							((Tapis)getContentPane().getComponent(1)).ajouterTexteDansPanneau(((Levable)par).getCarteAppelee().toString().split(" ")[2],((Levable)par).getPreneur(),par.getNombreDeJoueurs());
						}
						else
						{
							((Tapis)getContentPane().getComponent(1)).ajouterTexteDansPanneau(((Levable)par).getContrat()+ch_v,((Levable)par).getPreneur(),par.getNombreDeJoueurs());
						}
						pack();
						debutPli=true;
					}
					if(dernier_joueur>0)
					{
						anim_carte=new AnimationCarte(dernier_joueur,par.getNombreDeJoueurs(),pseudos,((Levable)par).premierTour());
						anim_carte.start();
					}
					else
					{
						raison_courante=ch_v;
						getJMenuBar().getMenu(1).getItem(0).setEnabled(true);
					}
				}
				else
				{
					synchronized (this) {
						afficherMainUtilisateur(true);
						pack();
						debutPli=false;
					}
					if(((Levable)par).getEntameur()>0)
					{
						anim_carte=new AnimationCarte(dernier_joueur,((Levable)par).getEntameur(),pseudos,((Levable)par).premierTour());
					}
					else
					{
						anim_carte=new AnimationCarte(dernier_joueur,par.getNombreDeJoueurs(),pseudos,((Levable)par).premierTour());
					}
					anim_carte.start();
				}
			}
		}
		else
		{
			String chelem=Contrat.chelem;
			String Chelem=Boutons._Chelem.name();
			byte donneur=par.getDistribution().getDonneur();
			placer_levable();
			Vector<String> pseudos=pseudos();
			if(par.getEtat()==Etat.Contrat)
			{
				Contrat contrat=((Levable)par).getContrat();
				synchronized (this) {
					afficherMainUtilisateur(false);
					//Activer les conseils
					getJMenuBar().getMenu(1).getItem(0).setEnabled(((PartieTarot)par).getFinEnchere());
					for(int indice=0;indice<((PartieTarot)par).contrats();indice++)
					{
						String texte=((PartieTarot)par).contrat(indice).toString();
						ajouterTexteDansZone(pseudos.get((indice+donneur+1)%nombreDeJoueurs)+":"+texte+st);
						ajouterTexteDansPanneau(texte,(byte)((indice+donneur+1)%nombreDeJoueurs));
					}
					pack();
				}
				byte debut=(byte) ((donneur+1+((PartieTarot)par).contrats())%par.getNombreDeJoueurs());
				if(debut>0)
				{
					anim_contrat=new AnimationContrat(pseudos,donneur,debut);
					anim_contrat.start();
				}
				else
				{
					for(EncheresTarot ench:EncheresTarot.values())
					{
						ajouterBoutonJeu(ench.toString(),ench.toString(),new Contrat(ench).estDemandable(contrat));
					}
					if(((PartieTarot)par).chelemContrat())
					{
						ajouterBoutonJeu(chelem,chelem,true);
					}
					pack();
				}
				return;
			}
			else if(par.getEtat()==Etat.Appel)
			{
				getJMenuBar().getMenu(1).getItem(0).setEnabled(true);
				for(int indice=0;indice<((PartieTarot)par).contrats();indice++)
				{
					ajouterTexteDansPanneau(((PartieTarot)par).contrat(indice).toString(),(byte)((indice+donneur+1)%nombreDeJoueurs));
				}
				afficherMainUtilisateur(false);
				getJMenuBar().getMenu(1).getItem(0).setEnabled(true);
				placerBoutonsAppel();
			}
			else if(par.getEtat()==Etat.Chien_Vu)
			{
				for(int indice=0;indice<((PartieTarot)par).contrats();indice++)
				{
					ajouterTexteDansPanneau(((PartieTarot)par).contrat(indice).toString(),(byte)((indice+donneur+1)%nombreDeJoueurs));
				}
				if(((PartieTarot)par).getCarteAppelee()!=null)
				{
					ajouterTexteDansPanneau(((PartieTarot)par).getCarteAppelee().toString(),((Levable)par).getPreneur());
				}
				afficherMainUtilisateur(false);
				ajouterBoutonJeu(Passe_au_jeu_de_la_carte,Pli_suivant,true);
			}
			else if(par.getEtat()==Etat.Avant_Ecart)
			{
				for(int indice=0;indice<((PartieTarot)par).contrats();indice++)
				{
					ajouterTexteDansPanneau(((PartieTarot)par).contrat(indice).toString(),(byte)((indice+donneur+1)%nombreDeJoueurs));
				}
				if(((PartieTarot)par).getCarteAppelee()!=null)
				{
					ajouterTexteDansPanneau(((PartieTarot)par).getCarteAppelee().toString(),((Levable)par).getPreneur());
				}
				afficherMainUtilisateur(false);
				ajouterBoutonJeu(Voir_chien,Voir_chien,true);
			}
			else if(par.getEtat()==Etat.Ecart)
			{
				Pli ecart=null;
				if(((Levable)par).getPreneur()==0)
				{
					ecart=((Levable)par).getPliEnCours();
					if(ecart==null)
					{
						getJMenuBar().getMenu(1).getItem(0).setEnabled(false);
						ajouterBoutonJeu(Prendre_les_cartes_du_chien,Prendre_les_cartes_du_chien,true);
					}
					else
					{
						getJMenuBar().getMenu(1).getItem(0).setEnabled(ecart.estVide());
						setChien((MainTarot)ecart.getCartes(),true);
						ajouterBoutonJeu(Passe_au_jeu_de_la_carte,Valider_chien,ecart.total()==par.getDistribution().derniereMain().total());
					}
				}
				else
				{
					getJMenuBar().getMenu(1).getItem(0).setEnabled(false);
					ajouterBoutonJeu(Passe_au_jeu_de_la_carte,Pli_suivant,true);
				}
				afficherMainUtilisateur(((Levable)par).getPreneur()==0&&ecart!=null);
				pack();
			}
			else if(par.getEtat()==Etat.Avant_Jeu2)
			{
				afficherMainUtilisateur(false);
				ajouterBoutonJeu(Passe_au_jeu_de_la_carte,Pli_suivant,true);
			}
			else if(par.getEtat()==Etat.Avant_Jeu)
			{
				afficherMainUtilisateur(false);
				ajouterBoutonJeu(chelem,Chelem,true);
				ajouterBoutonJeu(Passe_au_jeu_de_la_carte,Pli_suivant,true);
			}
			else
			{
				premier_pli_fait=true;
				Pli pliEnCours=((Levable)par).getPliEnCours();
				Carte carte_utilisateur=pliEnCours.carteDuJoueur((byte)0,nombreDeJoueurs,null);
				byte entameur=pliEnCours.getEntameur();
				int total=pliEnCours.total();
				/*dernier_joueur represente le prochain joueur qui doit jouer*/
				byte dernier_joueur=(byte)((total+entameur)%nombreDeJoueurs);
				Vector<Byte> joueurs=new Vector<Byte>();
				Main cartes=pliEnCours.getCartes();
				for(Carte carte:cartes)
				{
					joueurs.addElement(pliEnCours.joueurAyantJoue(carte, nombreDeJoueurs, null));
				}
				for(byte indice_joueur=0;indice_joueur<joueurs.size();indice_joueur++)
				{
					((Tapis)getContentPane().getComponent(1)).setCarte(joueurs.get(indice_joueur), nombreDeJoueurs,cartes.carte(indice_joueur));
				}
				if(carte_utilisateur==null)
				{/*Si l utilisateur n avait pas joue*/
					synchronized (this) {
						afficherMainUtilisateur(true);
						if(((Levable)par).premierTour())
						{
							Vector<Annonce> annonces;
							Vector<Annonce> va=new Vector<Annonce>();
							annonces=((Annoncable)par).getAnnoncesPossibles((byte)0);
							String[] ss;
							for(int indice=0;indice<2;indice++)
							{
								if(par.getInfos().get(4).split(";")[indice].charAt(par.getInfos().get(4).split(";")[indice].length()-1)!=p2)
									ss=par.getInfos().get(4).split(";")[indice].split(":")[1].split(Parametres.separateur_tiret_slash);
								else
									ss=new String[]{};
								for(String chaine:ss)
									va.addElement(new Annonce(chaine));
							}
							ajouterBoutonJeu(Revoir_chien,Revoir_chien,((PartieTarot)par).getContrat().force()<3);
							for(Annonce annonce:va)
								ajouterBoutonJeu(annonce.toString(),annonce.toString(),annonces.contains(annonce));
						}
						else
						{
							ajouterBoutonJeu(Revoir_pli_precedent,Revoir_pli_precedent,true);
						}
						pack();
						debutPli=true;
					}
					if(dernier_joueur>0)
					{
						anim_carte=new AnimationCarte(dernier_joueur,par.getNombreDeJoueurs(),pseudos,((Levable)par).premierTour());
						anim_carte.start();
					}
					else
					{
						raison_courante=ch_v;
						getJMenuBar().getMenu(1).getItem(0).setEnabled(true);
					}
				}
				else
				{
					synchronized (this) {
						afficherMainUtilisateur(true);
						pack();
						debutPli=false;
					}
					if(((Levable)par).getEntameur()>0)
					{
						anim_carte=new AnimationCarte(dernier_joueur,((Levable)par).getEntameur(),pseudos,((Levable)par).premierTour());
					}
					else
					{
						anim_carte=new AnimationCarte(dernier_joueur,par.getNombreDeJoueurs(),pseudos,((Levable)par).premierTour());
					}
					anim_carte.start();
				}
			}
		}
		pack();
	}
	private void afficherCartesDuPli()
	{
		Vector<Pli> unionPli=((Levable)par).unionPlis();
		if(unionPli.lastElement().total()==par.getNombreDeJoueurs())
		{
			for(byte joueur=0;joueur<par.getNombreDeJoueurs();joueur++)
			{
				((Tapis)getContentPane().getComponent(1)).setCarte(joueur,par.getNombreDeJoueurs(),unionPli.lastElement().carteDuJoueur(joueur,par.getNombreDeJoueurs(),unionPli.lastElement()));
			}
		}
		else
		{
			for(byte joueur=0;joueur<par.getNombreDeJoueurs();joueur++)
			{
				((Tapis)getContentPane().getComponent(1)).setCarte(joueur,par.getNombreDeJoueurs(),unionPli.get(unionPli.size()-2).carteDuJoueur(joueur,par.getNombreDeJoueurs(),unionPli.lastElement()));
			}
		}
	}
	private void ajouterBoutonJeu(JPanel panneau,String texte,String action,boolean apte)
	{
		JButton bouton=new JButton(texte);
		bouton.addActionListener(new EcouteurBoutonJeu(action));
		bouton.setEnabled(apte);
		panneau.add(bouton);
	}
	private void ajouterBoutonJeu(String texte,String action,boolean apte)
	{
		JPanel panneau=(JPanel)((JPanel)getContentPane().getComponent(3)).getComponent(1);
		JButton bouton=new JButton(texte);
		bouton.addActionListener(new EcouteurBoutonJeu(action));
		bouton.setEnabled(apte);
		panneau.add(bouton);
	}
	private void ajouterBoutonPrincipal(String texte,Jeu nom_jeu,Container container)
	{
		JButton bouton=new JButton(texte);
		bouton.addActionListener(new EcouteurBoutonPrincipal(bouton,nom_jeu));
		container.add(bouton);
	}
	private void ajouterBoutonPrincipal(String texte,String mode2,Container container)
	{
		JButton bouton=new JButton(texte);
		bouton.addActionListener(new EcouteurBoutonPrincipal(bouton,mode2));
		container.add(bouton);
	}
	private void ajouterBoutonPrincipal(String texte,Jeu nom_jeu,String mode2,Container container)
	{
		JButton bouton=new JButton(texte);
		bouton.addActionListener(new EcouteurBoutonPrincipal(bouton,nom_jeu,mode2));
		container.add(bouton);
	}
	private void ajouterBoutonFinDePartie(JPanel panneau,String texte)
	{
		JButton bouton=new JButton(texte);
		bouton.addActionListener(new EcouteurFinDePartie(texte));
		panneau.add(bouton);
	}
	public Partie getPartie()
	{
		return par;
	}
	public Vector<Vector<String>> getInfos()
	{
		return param.getInfos();
	}
	/**Tout lancement de logiciel passe par la*/
	public void init(Parametres pparam,int[] coordonnees)
	{
		param=pparam;
		try {/*Permet d avoir une application graphique comme si c etait Windows*/
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			SwingUtilities.updateComponentTreeUI(this);
			} catch (InstantiationException exc) {
			} catch (ClassNotFoundException exc) {
			} catch (UnsupportedLookAndFeelException exc) {
			} catch (IllegalAccessException exc) {}
		/*Parametre de lancement*/
		String lancement=getInfos().get(0).get(0);
		initMenus();
		setResizable(false);
		if(lancement.contains(Lancements.Bienvenue_dans_les_jeux_de_cartes.toString()))
		{
			menuPrincipal();
		}
		else if(lancement.indexOf('\\')<0)
		{
			modifierJeu(lancement.split(":")[1]);
		}
		else
		{
			String[] jeu_mode=lancement.split(":")[1].split("\\\\");
			jeu=Jeu.valueOf(jeu_mode[0].replace(ea,Lettre.e));
			mode=jeu_mode[1];
			if(jeu==Jeu.Belote)
			{
				modifierJeuBelote();
			}
			else
			{
				if(jeu_mode.length==2)
				{
					enCoursDePartie=false;
					par=null;
					setTitle(jeu.toString()+es+mode);
					Container container=new Container();
					container.setLayout(new GridLayout(0,1));
					//Active le menu Fichier/Changer de jeu
					getJMenuBar().getMenu(0).getItem(4).setEnabled(true);
					//Activer le menu Partie/Demo
					getJMenuBar().getMenu(1).getItem(4).setEnabled(true);
					for(String choix:Jouer.modes2)
					{
						ajouterBoutonPrincipal(choix.replace(Lettre._,Lettre.es),choix.replace(Lettre._,Lettre.es),container);
					}
					ajouterBoutonPrincipal(retour,retour,container);
					container.add(new JLabel("Pour utiliser l'aide allez dans le menu aide ou appuyer sur F3",JLabel.CENTER));
					setContentPane(container);
					pack();
				}
				else
				{
					param.getInfos().get(Jeu.Tarot.ordinal()+1).setElementAt("Mode:"+jeu_mode[2],7);
					modifierJeuTarot();
				}
			}
		}
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new FermetureFenetre());
		pack();
		setLocation(coordonnees[0],coordonnees[1]);
		setVisible(true);
	}
	private void conseil()
	{
		String[] raison=new String[3];
		if(par instanceof PartieBelote)
		{
			if(par.getEtat()==Etat.Contrat)
			{
				((PartieBelote)par).conseil_contrat(raison);
				String message="Je vous conseille ";
				if(new Contrat(raison[2]).force()!=1)
				{
					message+="le contrat "+raison[1]+st;
				}
				else
				{
					message+="de prendre a la couleur "+raison[1]+st;
				}
				message+=raison[0];
				JOptionPane.showMessageDialog(this,message,"Conseil",JOptionPane.INFORMATION_MESSAGE);
			}
			else
			{
				String message="Je vous conseille de jouer "+st+((PartieBelote)par).strategieJeuCarteUnique(raison)+st;
				message+=raison[0];
				JOptionPane.showMessageDialog(this,message,"Conseil",JOptionPane.INFORMATION_MESSAGE);
			}
		}
		else
		{
			if(par.getEtat()==Etat.Contrat)
			{
				String message="Je vous conseille le contrat "+((Levable)par).strategieContrat(raison)+st;
				message+=raison[0];
				JOptionPane.showMessageDialog(this,message,"Conseil",JOptionPane.INFORMATION_MESSAGE);
			}
			else if(par.getEtat()==Etat.Appel)
			{
				String message="Je vous conseille d'appeler "+((PartieTarot)par).strategieAppel(raison)+st;
				message+=raison[0];
				JOptionPane.showMessageDialog(this,message,"Conseil",JOptionPane.INFORMATION_MESSAGE);
			}
			else if(par.getEtat()==Etat.Ecart)
			{
				String message="Je vous conseille l'ecart "+st+((PartieTarot)par).strategieEcart(raison)+st;
				message+=raison[0];
				JOptionPane.showMessageDialog(this,message,"Conseil",JOptionPane.INFORMATION_MESSAGE);
			}
			else if(par.getEtat()==Etat.Jeu)
			{
				String message="Je vous conseille de jouer "+st+((PartieTarot)par).strategieJeuCarteUnique(raison)+st;
				message+=raison[0];
				JOptionPane.showMessageDialog(this,message,"Conseil",JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}
	private class FermetureFenetre extends WindowAdapter{
		public void windowClosing(WindowEvent event) {
			quitter();
		}
	}
	private void quitter()
	{
		if(!new File(Parametres.fichier).exists())
		{
			param.sauvegarder();
		}
		/*Si l'utilisateur a supprime le fichier de configurations alors a la fin
		 * de l'execution le fichier de configuration sera recree*/
		if(enCoursDePartie&&!partie_sauvegardee)
		{
			int choix=choixSauvegarder();
			if(choix!=JOptionPane.CANCEL_OPTION)
			{
				if(choix==JOptionPane.YES_OPTION)
				{
					dialogueFichier();
				}
				changerNombreDeParties();
				ecrireCoordonnees();
				System.exit(0);
			}
		}
		else
		{
			changerNombreDeParties();
			ecrireCoordonnees();
			System.exit(0);
		}
	}
	private int choixSauvegarder()
	{
		return JOptionPane.showConfirmDialog(this,"Voulez vous sauvegarder la partie en cours ?","Partie en cours",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE);
	}
	private void changerNombreDeParties()
	{
		Vector<Long> vl=new Vector<Long>();
		File fichier=new File(Fichier.dossier_paquets+File.separator+Fichier.fichier_paquet);
		int total=2;
		try {
			BufferedReader br=new BufferedReader(new FileReader(fichier));
			for(int indice=0;indice<total;indice++)
				vl.addElement(Long.parseLong(br.readLine()));
			br.close();
		} catch (Exception exc) {
			vl=new Vector<Long>();
			for (int indice= 0; indice < 5; indice++)
				vl.addElement((long)0);
		}
		//Si l'action de battre les cartes est faite a chaque lancement
		//de logiciel alors le nombre de parties est remis a zero lors
		//d'une fermeture de logiciel
		if(getInfos().get(Jeu.Belote.ordinal()+1).get(1).endsWith(ChoixBattreCartes.a_chaque_lancement.toString()))
			vl.setElementAt((long)0,0);
		if(getInfos().get(Jeu.Tarot.ordinal()+1).get(1).endsWith(ChoixBattreCartes.a_chaque_lancement.toString()))
			vl.setElementAt((long)0,total-1);
		try {
			BufferedWriter bw=new BufferedWriter(new FileWriter(fichier));
			for(int indice=0;indice<total;indice++)
			{
				bw.write(vl.get(indice).toString());
				bw.newLine();
			}
			bw.close();
		} catch (Exception exc) {}
	}
	private void ecrireCoordonnees()
	{
		Point point=getLocation();
		try {
			ObjectOutputStream oos=new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(new File(Fichier.coordonnees_fenetre))));
			oos.writeObject(new int[]{point.x,point.y});
			oos.close();
		} catch (Exception exc)
		{
			exc.printStackTrace();
		}
	}
	private void menuPrincipal()
	{
		//Activer le menu Partie/Demo
		getJMenuBar().getMenu(1).getItem(4).setEnabled(true);
		//desactiver le menu Partie/aide au jeu
		getJMenuBar().getMenu(1).getItem(2).setEnabled(false);
		//desactiver le menu Partie/conseil
		getJMenuBar().getMenu(1).getItem(0).setEnabled(false);
		//Desactiver le menu Partie/Pause
		getJMenuBar().getMenu(1).getItem(1).setEnabled(false);
		enCoursDePartie=false;
		changer_pile_fin=false;
		partie_aleatoire_jouee=false;
		par=null;
		setTitle(Lancements.Bienvenue_dans_les_jeux_de_cartes.toString());
		Container container=new Container();
		container.setLayout(new GridLayout(0,1));
		/*Pour montrer qu'on a de l'attention a l'utilisateur*/
		container.add(new JLabel("Bonjour "+pseudo(),JLabel.CENTER));
		/*Cree les boutons de jeu*/
		for (Jeu jeu_2:Jeu.values())
		{
			ajouterBoutonPrincipal(jeu_2.toString(),(Jeu)null,container);
		}
		//Ajout d'une etiquette pour indiquer ou aller pour avoir de l'aide
		container.add(new JLabel("Pour utiliser l'aide allez dans le menu aide ou appuyer sur F3",JLabel.CENTER));
		getJMenuBar().getMenu(0).getItem(1).setEnabled(false);
		getJMenuBar().getMenu(0).getItem(3).setEnabled(false);
		getJMenuBar().getMenu(0).getItem(4).setEnabled(false);
		setContentPane(container);
		pack();
	}
	/**Initialise la barre de menus*/
	private void initMenus()
	{
		setJMenuBar(new JMenuBar());
		/* Fichier */
		JMenu menu=new JMenu(menus_utilises[0]);//menu
		menu.setMnemonic(menus_utilises[0].charAt(0));
		/* Fichier/Charger "accessible n'importe quand"*/
		JMenuItem sous_menu=new JMenuItem(sous_menus_utilises1[0]);//sous_menu
		sous_menu.setMnemonic(sous_menus_utilises1[0].charAt(0));
		sous_menu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK));
		sous_menu.addActionListener(new EcouteurMenu(sous_menu));
		menu.add(sous_menu);
		/* Fichier/Sauvegarder "accessible que lorsqu'on joue une partie de cartes"*/
		sous_menu=new JMenuItem(sous_menus_utilises1[1]);
		sous_menu.setMnemonic(sous_menus_utilises1[1].charAt(0));
		sous_menu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
		sous_menu.addActionListener(new EcouteurMenu(sous_menu));
		menu.add(sous_menu);
		menu.addSeparator();
		/* Fichier/Changer de mode "accessible en cours de jeu,
		 * on continue de jouer au meme jeu"*/
		sous_menu=new JMenuItem(sous_menus_utilises1[2]);
		sous_menu.addActionListener(new EcouteurMenu(sous_menu));
		sous_menu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M,KeyEvent.CTRL_DOWN_MASK));
		menu.add(sous_menu);
		/* Fichier/Changer de jeu "accessible n'importe quand sauf au menu principal,
		 * on y revient lorsque c'est accessible*/
		sous_menu=new JMenuItem(sous_menus_utilises1[3]);
		sous_menu.addActionListener(new EcouteurMenu(sous_menu));
		sous_menu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_J,KeyEvent.CTRL_DOWN_MASK));
		menu.add(sous_menu);
		menu.addSeparator();
		sous_menu=new JMenuItem(sous_menus_utilises1[4]);
		sous_menu.addActionListener(new EcouteurMenu(sous_menu));
		sous_menu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D,KeyEvent.CTRL_DOWN_MASK));
		menu.add(sous_menu);
		menu.addSeparator();
		sous_menu=new JMenuItem(sous_menus_utilises1[5]);
		sous_menu.addActionListener(new EcouteurMenu(sous_menu));
		sous_menu.setAccelerator(KeyStroke.getKeyStroke((char)KeyEvent.VK_ESCAPE));
		menu.add(sous_menu);
		getJMenuBar().add(menu);
		/* Partie */
		menu=new JMenu(menus_utilises[1]);
		/* Partie/Conseil "accessible uniquement en cours de partie et
		 * dans les jeux non solitaires"*/
		sous_menu=new JMenuItem(sous_menus_utilises2[0]);
		sous_menu.setMnemonic(sous_menus_utilises2[0].charAt(0));
		sous_menu.setAccelerator(KeyStroke.getKeyStroke("F1"));
		sous_menu.addActionListener(new EcouteurMenu(sous_menu));
		menu.add(sous_menu);
		/* Partie/Pause Permet de mettre le jeu en pause*/
		sous_menu=new JCheckBoxMenuItem(sous_menus_utilises2[1]);
		sous_menu.setMnemonic(sous_menus_utilises2[1].charAt(0));
		sous_menu.setAccelerator(KeyStroke.getKeyStroke("PAUSE"));
		sous_menu.addActionListener(new EcouteurMenu(sous_menu));
		menu.add(sous_menu);
		/* Partie/Pause Permet d avoir de l aide*/
		sous_menu=new JMenuItem(aide_au_jeu);
		sous_menu.setMnemonic(aide_au_jeu.charAt(0));
		sous_menu.setAccelerator(KeyStroke.getKeyStroke("F2"));
		sous_menu.addActionListener(new EcouteurMenu(sous_menu));
		menu.add(sous_menu);
		/* Partie/Editer "Permet d'editer n'importe quelle partie de cartes et accessible n'importe quand"*/
		sous_menu=new JMenuItem(sous_menus_utilises2[3]);
		sous_menu.setMnemonic(sous_menus_utilises2[3].charAt(0));
		sous_menu.addActionListener(new EcouteurMenu(sous_menu));
		sous_menu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,KeyEvent.CTRL_DOWN_MASK));
		menu.add(sous_menu);
		/* Partie/Demo "Permet de voir la demostration d une partie"*/
		JMenu menu_jeu=new JMenu(sous_menus_utilises2[4]);
		sous_menu=new JMenuItem(Jeu.Belote.toString());
		sous_menu.setMnemonic(Jeu.Belote.toString().charAt(0));
		sous_menu.addActionListener(new EcouteurMenu(sous_menus_utilises2[4],sous_menu));
		sous_menu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B,KeyEvent.CTRL_DOWN_MASK+KeyEvent.SHIFT_DOWN_MASK));
		menu_jeu.add(sous_menu);
		JMenu sous_jeu;
		sous_menu=new JMenuItem(Jeu.Tarot.toString());
		sous_menu.setMnemonic(Jeu.Tarot.toString().charAt(0));
		sous_menu.addActionListener(new EcouteurMenu(sous_menus_utilises2[4],sous_menu));
		sous_menu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T,KeyEvent.CTRL_DOWN_MASK+KeyEvent.SHIFT_DOWN_MASK));
		menu_jeu.add(sous_menu);
		menu.add(menu_jeu);
		/* Partie/Entrainement "accessible n'importe quand pour pouvoir s'entrainer"*/
		JMenu entrainement=new JMenu(sous_menus_utilises2[5]);
		entrainement.setMnemonic('n');
		/* Partie/Entrainement au Tarot*/
		for (int indice = 3; indice < 6; indice++) {
			/* Partie/Entrainement au Tarot/(3-4-5) joueurs*/
			sous_jeu=new JMenu(indice+" joueurs");
			/* Mise en place des choix d'entrainement pour le tarot*/
			for (ChoixTarot ct:ChoixTarot.values()) {
				sous_menu=new JMenuItem(ct.toString());
				sous_menu.addActionListener(new EcouteurMenu(indice+" joueurs",sous_menu));
				sous_jeu.add(sous_menu);
			}
			entrainement.add(sous_jeu);
		}
		menu.add(entrainement);
		menu.setMnemonic(menus_utilises[1].charAt(2));
		getJMenuBar().add(menu);
		/* Parametres */
		menu=new JMenu(menus_utilises[2]);
		sous_menu=new JMenuItem(Jeu.Belote.toString());
		sous_menu.addActionListener(new EcouteurMenu(menus_utilises[2],sous_menu));
		sous_menu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B,KeyEvent.SHIFT_DOWN_MASK));
		menu.add(sous_menu);
		sous_menu=new JMenuItem(Jeu.Tarot.toString());
		sous_menu.addActionListener(new EcouteurMenu(menus_utilises[2],sous_menu));
		sous_menu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T,KeyEvent.SHIFT_DOWN_MASK));
		menu.add(sous_menu);
		sous_menu=new JMenuItem(sous_menus_utilises3[0]);
		sous_menu.addActionListener(new EcouteurMenu(menus_utilises[2],sous_menu));
		sous_menu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_J,KeyEvent.CTRL_DOWN_MASK+KeyEvent.ALT_DOWN_MASK));
		menu.add(sous_menu);
		sous_menu=new JMenuItem(sous_menus_utilises3[1]);
		sous_menu.addActionListener(new EcouteurMenu(menus_utilises[2],sous_menu));
		sous_menu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,KeyEvent.CTRL_DOWN_MASK));
		menu.add(sous_menu);
		sous_menu=new JMenuItem(sous_menus_utilises3[2]);
		sous_menu.addActionListener(new EcouteurMenu(menus_utilises[2],sous_menu));
		sous_menu.setAccelerator(KeyStroke.getKeyStroke("F4"));
		menu.add(sous_menu);
		sous_menu=new JMenuItem(sous_menus_utilises3[3]);
		sous_menu.addActionListener(new EcouteurMenu(menus_utilises[2],sous_menu));
		sous_menu.setAccelerator(KeyStroke.getKeyStroke("F5"));
		menu.add(sous_menu);
		menu.setMnemonic(menus_utilises[2].charAt(0));
		getJMenuBar().add(menu);
		/* Aide */
		menu=new JMenu(menus_utilises[3]);
		/* Aide/Aide generale Explication du fonctionnement du logiciel et des regles utilisables*/
		sous_menu=new JMenuItem(aide_generale);
		sous_menu.addActionListener(new EcouteurMenu(aide_generale,sous_menu));
		sous_menu.setAccelerator(KeyStroke.getKeyStroke("F3"));
		sous_menu.setMnemonic(aide_generale.charAt(0));
		menu.setMnemonic(menus_utilises[3].charAt(0));
		menu.add(sous_menu);
		getJMenuBar().add(menu);
		/* A propos "Descriptif du logiciel"*/
		menu=new JMenu(menus_utilises[4]);
		sous_menu=new JMenuItem(sous_menus_utilises4[1]);//La personnalite de l auteur
		sous_menu.addActionListener(new EcouteurMenu(sous_menu));
		sous_menu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_DOWN_MASK));
		menu.add(sous_menu);
		sous_menu=new JMenuItem(sous_menus_utilises4[2]);//Droits et interdictions
		sous_menu.addActionListener(new EcouteurMenu(sous_menu));
		sous_menu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, KeyEvent.CTRL_DOWN_MASK));
		menu.add(sous_menu);
		menu.setMnemonic(menus_utilises[4].charAt(4));
		getJMenuBar().add(menu);
	}
	private int sauvegarde(String message,String titre)
	{
		return JOptionPane.showConfirmDialog(this,message,titre,JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE);
	}
	/**Sauvegarder une partie dans un fichier*/
	private String dialogueFichier()
	{
		String[] dossiers;
		if(!(par instanceof PartieTarot))
		{
			dossiers=new String[]{par.jeu().name(),par.getMode().replace(ea,Lettre.e),par.getType().toString()};
		}
		else
		{
			dossiers=new String[]{par.jeu().name(),par.getMode(),par.getInfos().get(7).split(":")[1],par.getType().toString()};
		}
		return new DialogueFichiers(this,DialogueFichiers.titre_sauvegarde,dossiers).fichier();
	}
	/**Charger ou supprimer un fichier*/
	private DialogueFichiers dialogueFichier(String titre)
	{
		return new DialogueFichiers(this,titre);
	}
	private DialogueJeux dialogueJeux(String titre)
	{
		return new DialogueJeux(titre,this);
	}
	/**Editer une partie*/
	private Editeur editeur()
	{
		return new Editeur(this);
	}
	private void erreurDeChargement(String[] txt)
	{
		JOptionPane.showMessageDialog(this,txt[0]+File.separator+txt[1]+" n'a pas pu etre charge", "Erreur de chargement", JOptionPane.ERROR_MESSAGE);
	}
	private class EcouteurMenu implements ActionListener {
		private String jm;
		private String menu=ch_v;
		private EcouteurMenu(JMenuItem pjm)
		{
			this.jm=pjm.getText();
		}
		private EcouteurMenu(String pmenu,JMenuItem pjm)
		{
			this.jm=pjm.getText();
			this.menu=pmenu;
		}
		public void actionPerformed(ActionEvent event) {
			if(jm.equals(sous_menus_utilises1[0]))
			{
				if(enCoursDePartie&&!partie_sauvegardee)
				{
					if(passe||!thread_anime)
					{
						int choix=sauvegarde("Une partie est en cours\nvoulez-vous la sauvegarder?","Partie en cours");
						if(choix!=JOptionPane.CANCEL_OPTION)
						{
							if(choix==JOptionPane.YES_OPTION)
							{
								String fichier=dialogueFichier();
								if(!fichier.isEmpty())
								{
									partie_sauvegardee=true;
									DialogueFichiers f2=dialogueFichier(DialogueFichiers.titre_chargement);
									String[] txt=f2.getPartie();
									try {
										chargerPartie(txt[0]+File.separator+txt[1]);
										chargerPartie();
										partie_sauvegardee=false;
									}
									catch (Exception e1)
									{
										e1.printStackTrace();
										if(txt[1]!=null&&!txt[1].equals(ch_v))
										{
											erreurDeChargement(txt);
										}
									}
								}
							}
							else
							{
								partie_sauvegardee=true;
								DialogueFichiers f2=dialogueFichier(DialogueFichiers.titre_chargement);
								String[] txt=f2.getPartie();
								try {
									chargerPartie(txt[0]+File.separator+txt[1]);
									chargerPartie();
									partie_sauvegardee=false;
								}
								catch (Exception e1)
								{
									e1.printStackTrace();
									if(txt[1]!=null&&!txt[1].equals(ch_v))
									{
										erreurDeChargement(txt);
									}
								}
							}
						}
					}
				}
				else if(passe||!thread_anime)
				{
					DialogueFichiers f2=dialogueFichier(DialogueFichiers.titre_chargement);
					String[] txt=f2.getPartie();
					try
					{/*Si l'utilisateur a ouvert la fenetre de chargement de fichier sans charger un fichier, une exception se leve et l'eventuelle partie en cours n'est pas affectee*/
						if(new File(txt[0]+File.separator+txt[1]).exists())
						{
							for(;animationSimulation!=null&&animationSimulation.isAlive();)
							{
								arret_demo=true;
							}
						}
						chargerPartie(txt[0]+File.separator+txt[1]);
						chargerPartie();
						partie_sauvegardee=false;
					}
					catch (Exception e1)
					{
						e1.printStackTrace();
						if(txt[1]!=null&&!txt[1].equals(ch_v))
						{
							erreurDeChargement(txt);
						}
					}
				}
			}
			else  if(jm.equals(sous_menus_utilises1[1]))
			{
				if(passe||!thread_anime)
				{
					String fichier=dialogueFichier();
					if(!fichier.isEmpty())
					{
						partie_sauvegardee=true;
					}
				}
			}
			else if(jm.equals(sous_menus_utilises1[2]))
			{
				if(!partie_sauvegardee)
				{
					if(passe||!thread_anime)
					{
						int choix=sauvegarde("Une partie est en cours\nvoulez-vous la sauvegarder?","Partie en cours");
						if(choix!=JOptionPane.CANCEL_OPTION)
						{
							if(choix==JOptionPane.YES_OPTION)
							{
								String fichier=dialogueFichier();
								if(!fichier.isEmpty())
								{
									modifierJeu(par.jeu().toString());
								}
							}
							else
							{
								modifierJeu(par.jeu().toString());
							}
						}
					}
				}
				else if(passe||!thread_anime)
				{
					modifierJeu(par.jeu().toString());
				}
			}
			else if(jm.equals(sous_menus_utilises1[3]))
			{
				if(enCoursDePartie&&!partie_sauvegardee)
				{
					if(passe||!thread_anime)
					{
						int choix=sauvegarde("Voulez vous sauvegarder la partie en cours ?","Partie en cours");
						if(choix!=JOptionPane.CANCEL_OPTION)
						{
							if(choix==JOptionPane.YES_OPTION)
							{
								String fichier=dialogueFichier();
								if(!fichier.isEmpty())
								{
									menuPrincipal();
								}
							}
							else
							{
								menuPrincipal();
							}
						}
					}
				}
				else if(passe||!thread_anime)
				{
					menuPrincipal();
				}
			}
			else if(jm.equals(sous_menus_utilises1[4]))
			{
				if(passe||!thread_anime)
				{
					dialogueFichier(jm);
				}
			}
			else if(jm.equals(sous_menus_utilises1[5]))
			{
				quitter();
			}
			else if(jm.equals(sous_menus_utilises2[0]))
			{
				if(!thread_anime)
				{
					conseil();
				}
			}
			else if(jm.equals(sous_menus_utilises2[1]))
			{/*Pour que l'utilisateur utilise la pause*/
				passe=!passe;
			}
			else if(jm.equals(aide_au_jeu))
			{
				if(par!=null&&par instanceof Levable&&enCoursDePartie)
				{
					aide_au_jeu();
				}
			}
			else if(jm.equals(sous_menus_utilises2[3]))
			{
				Editeur ed=editeur();
				ed.setDialogue();
				Partie partie_editee=ed.getPartie();
				if(partie_editee!=null)
				{//Cas ou l'utilisateur veut jouer une partie editee
					if(par!=null&&enCoursDePartie&&!partie_sauvegardee)
					{
						if(passe||!thread_anime)
						{
							int choix=sauvegarde("Voulez vous sauvegarder la partie en cours ?","Partie en cours");
							if(choix!=JOptionPane.CANCEL_OPTION)
							{
								if(choix==JOptionPane.YES_OPTION)
								{
									String fichier=dialogueFichier();
									if(!fichier.isEmpty())
									{
										//desactiver le menu Partie/aide au jeu
										getJMenuBar().getMenu(1).getItem(2).setEnabled(false);
										passe=false;
										//Desactiver le menu Partie/Pause
										getJMenuBar().getMenu(1).getItem(1).setEnabled(false);
										charge=false;
										a_joue_carte=false;
										partie_sauvegardee=false;
										par=partie_editee;
										//Desactiver le menu Partie/Demo
										getJMenuBar().getMenu(1).getItem(4).setEnabled(false);
										if(par instanceof PartieBelote)
											editerBelote();
										else
											editerTarot();
									}
								}
								else
								{
									//desactiver le menu Partie/aide au jeu
									getJMenuBar().getMenu(1).getItem(2).setEnabled(false);
									passe=false;
									//Desactiver le menu Partie/Pause
									getJMenuBar().getMenu(1).getItem(1).setEnabled(false);
									charge=false;
									a_joue_carte=false;
									partie_sauvegardee=false;
									par=partie_editee;
									//Desactiver le menu Partie/Demo
									getJMenuBar().getMenu(1).getItem(4).setEnabled(false);
									if(par instanceof PartieBelote)
										editerBelote();
									else
										editerTarot();
								}
							}
						}
					}
					else
					{
						for(;animationSimulation!=null&&animationSimulation.isAlive();)
						{
							arret_demo=true;
						}
						passe=false;
						//Desactiver le menu Partie/Pause
						getJMenuBar().getMenu(1).getItem(1).setEnabled(false);
						charge=false;
						a_joue_carte=false;
						enCoursDePartie=true;
						partie_sauvegardee=false;
						par=partie_editee;
						//Desactiver le menu Partie/Demo
						getJMenuBar().getMenu(1).getItem(4).setEnabled(false);
						if(par instanceof PartieBelote)
							editerBelote();
						else
							editerTarot();
					}
				}
			}
			else if(menu.equals(sous_menus_utilises2[4]))
			{/*Aucune partie n est en train d etre jouee.*/
				anim_chargement=new AnimationChargement();
				animationSimulation=new AnimationSimulation(jm);
				anim_chargement.start();
				animationSimulation.start();
			}
			else if(menu.equals(menus_utilises[2]))
			{
				//Nom du choix de menu dans le menu parametres
				DialogueJeux dj=dialogueJeux(jm);
				dj.setDialogue();
				Vector<String> infos=dj.getInfos();
				if(jm.equals(sous_menus_utilises3[0]))
				{
					param.changerGroupe(Jeu.values().length+1, infos);
				}
				else if(jm.equals(sous_menus_utilises3[1])||jm.equals(sous_menus_utilises3[2])||jm.equals(sous_menus_utilises3[3]))
				{
					param.changerGroupe(0, infos);
				}
				else
				{
					param.changerGroupe(Jeu.valueOf(jm).ordinal()+1,infos);
				}
				param.sauvegarder();
			}
			else if(menu.equals(aide_generale))
			{//On indique a l utilisatteur comment utiliser le logiciel et jouer aux cartes
				DialogueAideGenerale aide=new DialogueAideGenerale(aide_generale,Fenetre.this,true);
				aide.setDialogue();
				aide.voir();
			}
			else if(menu.endsWith("joueurs"))
			{
				/*Si l'utilisateur a supprime le fichier de configurations alors a la fin
				 * de l'execution le fichier de configuration sera recree*/
				if(enCoursDePartie&&!partie_sauvegardee)
				{
					int choix=choixSauvegarder();
					if(choix!=JOptionPane.CANCEL_OPTION)
					{
						if(choix==JOptionPane.YES_OPTION)
						{
							dialogueFichier();
						}
						jouer_donne_entrainement();
					}
				}
				else
				{
					jouer_donne_entrainement();
				}
			}
			else if(jm.equals(sous_menus_utilises4[1]))
			{
				JOptionPane.showMessageDialog(Fenetre.this,"Fran"+Lettre.cc+"ois Mercier des Rochettes","Auteur",JOptionPane.INFORMATION_MESSAGE);
			}
			else
			{/*License*/
				String info=ch_v+L+i+c+e+n+s+e+es+d+e+s+es+j+e+u+x+es+d+e+es+c+a+r+t+e+s+p2+st;
				info+=ch_v+st;
				info+=ch_v+C+e+es+q+u+e+es+v+o+u+s+es+p+o+u+v+e+z+es+f+a+i+r+e+es+a+v+e+c+es+l+e+es+l+o+g+i+c+i+e+l+st;
				info+=ch_v+tb+c1+pd+J+o+u+e+r+es+g+r+a+t+u+i+t+e+m+e+n+t+es+e+n+es+s+o+l+o+es+ag+es+t+o+u+s+es+l+e+s+es+j+e+u+x+es+d+e+es+c+a+r+t+e+s+es+ea+c+r+i+t+s+es+i+n+t+ea+g+r+a+l+e+m+e+n+t+es+e+n+es+F+r+a+n+cc+a+i+s+st;
				info+=ch_v+tb+c2+pd+T+ea+l+ea+c+h+a+r+g+e+r+es+e+t+es+c+o+p+i+e+r+es+l+e+es+l+o+g+i+c+i+e+l+es+ag+es+v+o+l+o+n+t+ea+st;
				info+=ch_v+tb+c3+pd+L+i+r+e+es+l+quote+a+i+d+e+es+s+u+r+es+l+e+s+es+j+e+u+x+es+d+e+es+c+a+r+t+e+s+st;
				info+=ch_v+tb+c4+pd+U+t+i+l+i+s+e+r+es+l+e+s+es+c+o+n+s+e+i+l+s+es+d+e+es+j+o+u+e+r+i+e+es+p+o+u+r+es+p+r+o+g+r+e+s+s+e+r+st;
				info+=ch_v+tb+c5+pd+S+a+u+v+e+g+a+r+d+e+r+vi+es+c+h+a+r+g+e+r+es+d+e+s+es+p+a+r+t+i+e+s+es+ea+d+i+t+ea+e+s+es+o+u+es+n+o+n+st;
				info+=ch_v+tb+c6+pd+C+h+a+n+g+e+r+es+l+e+s+es+o+p+t+i+o+n+s+es+d+e+es+j+e+u+x+es+pg+p+a+r+a+m+eg+t+r+e+s+es+d+e+es+t+r+i+vi+es+v+a+r+i+a+n+t+e+s+es+d+e+es+r+eg+g+l+e+s+vi+es+l+a+n+c+e+m+e+n+t+es+d+u+es+l+o+g+i+c+i+e+l+pd+st;
				info+=ch_v+tb+c7+pd+R+e+g+a+r+d+e+r+es+l+e+es+d+e+r+n+i+e+r+es+p+l+i+st;
				info+=ch_v+tb+c8+pd+V+i+s+u+a+l+i+s+e+r+es+l+a+es+p+a+r+t+i+e+es+f+i+n+i+e+st;
				info+=ch_v+tb+c9+pd+C+o+n+n+a+ic+t+r+e+es+l+e+s+es+s+c+o+r+e+s+st;
				info+=ch_v+st;
				info+=ch_v+C+e+es+q+u+e+es+v+o+u+s+es+n+e+es+p+o+u+v+e+z+es+p+a+s+es+f+a+i+r+e+es+a+v+e+c+es+l+e+es+l+o+g+i+c+i+e+l+st;
				info+=ch_v+tb+c1+pd+T+r+u+q+u+e+r+es+l+e+s+es+d+o+n+n+e+s+es+a+l+ea+a+t+o+i+r+e+s+st;
				info+=ch_v+tb+c2+pd+M+o+d+i+f+i+e+r+es+l+e+es+c+o+d+e+es+s+o+u+r+c+e+st;
				info+=ch_v+tb+c3+pd+T+r+i+c+h+e+r+es+e+n+es+c+o+n+s+u+l+t+a+n+t+es+l+e+s+es+m+a+i+n+s+es+d+e+s+es+a+u+t+r+e+s+es+j+o+u+e+u+r+s+st;
				info+=ch_v+tb+c4+pd+J+o+u+e+r+es+e+n+es+m+u+l+t+i+j+o+u+e+u+r+st;
				info+=ch_v+tb+c5+pd+J+o+u+e+r+es+d+a+n+s+es+u+n+e+es+a+u+t+r+e+es+l+a+n+g+u+e+st;
				info+=ch_v+tb+c6+pd+M+a+l+t+r+a+i+t+e+r+es+l+quote+o+r+d+i+n+a+t+e+u+r+st;
				info+=ch_v+tb+c7+pd+R+e+g+a+r+d+e+r+es+t+o+u+s+es+l+e+s+es+p+l+i+s+es+l+o+r+s+q+u+e+es+l+a+es+p+a+r+t+i+e+es+n+quote+e+s+t+es+p+a+s+es+f+i+n+i+e+st;
				info+=ch_v+tb+c8+pd+P+r+o+g+r+a+m+m+e+r+es+l+quote+i+n+t+e+l+l+i+g+e+n+c+e+es+a+r+t+i+f+i+c+i+e+l+l+e+es+p+a+r+es+l+e+es+l+o+g+i+c+i+e+l+st;
				info+=ch_v+st;
				info+=ch_v+C+o+p+y+r+i+g+h+t+es+f+a+i+t+es+e+n+es+F+r+a+n+c+e+st;
				JOptionPane.showMessageDialog(Fenetre.this,info,"Licence",JOptionPane.INFORMATION_MESSAGE);
			}
		}
		private void jouer_donne_entrainement()
		{
			//desactiver le menu Partie/aide au jeu
			getJMenuBar().getMenu(1).getItem(2).setEnabled(false);
			changer_pile_fin=false;
			for(;animationSimulation!=null&&animationSimulation.isAlive();)
			{
				arret_demo=true;
			}
			Partie.chargement_simulation=0;
			anim_chargement=new AnimationChargement();
			anim_chargement.start();
			passe=false;
			//Desactiver le menu Partie/Pause
			getJMenuBar().getMenu(1).getItem(1).setEnabled(false);
			charge=false;
			a_joue_carte=false;
			enCoursDePartie=true;
			byte nombre_joueurs=Byte.parseByte(menu.split(es+ch_v)[0]);
			byte nombre_cartes;
			int tarot=Jeu.Tarot.ordinal()+1;
			if(nombre_joueurs==5)
			{
				if(param.getInfos().get(tarot).lastElement().endsWith(Repartition5Joueurs.c1_vs_4.toString()))
				{
					nombre_cartes=14;
				}
				else
				{
					nombre_cartes=15;
				}
			}
			else if(nombre_joueurs==4)
			{
				nombre_cartes=18;
			}
			else
			{
				nombre_cartes=24;
			}
			Donne donne=new Donne(menu,param.getInfos().get(tarot),0L);
			donne.initDonne(ChoixTarot.valueOf(jm.replace(Lettre.ag,Lettre.a).replace(es,Lettre._)),nombre_cartes,nombre_joueurs);
			par=new PartieTarot(Type.Entrainement,donne);
			jeux.parties.Partie.chargement_simulation=100;
			mettre_en_place_ihm_tarot();
		}
	}
	private void aide_au_jeu()
	{
		if(par instanceof PartieBelote)
		{
			MainBelote main_utilisateur=(MainBelote)par.getDistribution().main();
			Contrat contrat=((Levable)par).getContrat();
			byte couleur_atout=((PartieBelote)par).couleur_atout();
			Vector<MainBelote> repartition=main_utilisateur.couleurs(couleur_atout,contrat);
			MainBelote cartesJouees=((PartieBelote)par).cartesJouees();
			cartesJouees.ajouterCartes(((Levable)par).getPliEnCours().getCartes());
			Vector<MainBelote> repartitionCartesJouees=cartesJouees.couleurs(couleur_atout,contrat);
			DialogueAideAuJeu dialogue=new DialogueAideAuJeu(aide_au_jeu+Lettre.es+Lettre.ag+Lettre.es+Lettre.l+Lettre.a+Lettre.es+Jeu.Belote,this,true);
			Vector<Vector<MainBelote>> cartes_possibles=((PartieBelote)par).cartesPossibles(repartitionCartesJouees,((Levable)par).unionPlis(),repartition,(byte)0,couleur_atout);
			Vector<Vector<MainBelote>> cartes_certaines=((PartieBelote)par).cartesCertaines(cartes_possibles);
			byte couleur_demandee=((Levable)par).getPliEnCours().estVide()?couleur_atout:((Levable)par).getPliEnCours().couleurDemandee();
			dialogue.setDialogue2(cartes_possibles,cartes_certaines,repartitionCartesJouees,couleur_demandee,couleur_atout,contrat,pseudos());
			dialogue.voir();
		}
		else//cas du Tarot
		{
			MainTarot main_utilisateur=(MainTarot)par.getDistribution().main();
			Vector<MainTarot> repartition=main_utilisateur.couleurs();
			MainTarot cartesJouees=((PartieTarot)par).cartesJouees((byte)0);
			cartesJouees.ajouterCartes(((Levable)par).getPliEnCours().getCartes());
			Vector<MainTarot> repartitionCartesJouees=cartesJouees.couleurs();
			Vector<Vector<MainTarot>> cartes_possibles=((PartieTarot)par).cartesPossibles(!repartitionCartesJouees.get(0).estVide(),repartitionCartesJouees,((Levable)par).unionPlis(),!repartition.get(0).estVide(),repartition,(byte)0);
			DialogueAideAuJeu dialogue=new DialogueAideAuJeu(aide_au_jeu+Lettre.es+Lettre.a+Lettre.u+Lettre.es+Jeu.Tarot,this,true);
			Vector<Vector<MainTarot>> cartes_certaines=((PartieTarot)par).cartesCertaines(cartes_possibles);
			dialogue.setDialogue(cartes_possibles,cartes_certaines,repartitionCartesJouees,pseudos());
			dialogue.voir();
		}
	}
	private class AnimationSimulation extends Thread
	{
		private AnimationSimulation(String pjeu)
		{
			Partie.chargement_simulation=0;
			Vector<String> infos=new Vector<String>();
			String info=ch_v;
			if(pjeu.equals(Jeu.Belote.toString()))
			{
				int indice=0;
				for(Couleur couleur:Couleur.values())
				{
					if(couleur!=Couleur.Atout)
					{
						if(indice>0)
						{
							info+=Parametres.separateur_tiret;
						}
						info+=couleur;
						indice++;
					}
				}
				infos.addElement("Belote:");
				infos.addElement("Battre les cartes:"+ChoixBattreCartes.jamais);
				infos.addElement("Sens de distribution:"+Sens.Anti_horaire);
				infos.addElement("Tri au debut:"+info+";"+Monotonie.Decroissant);
				infos.addElement("Ordre avant enchere:"+Ordre.Atout);
				infos.addElement("Annonces:");
				infos.addElement("Obligation de sous-couper adversaire:non");
				//aucun ou sur-couper uniquement et sous-couper ou sur-couper
				infos.addElement("Obligation avec le partenaire:"+BeloteCoupePartenaire.Ni_sur_coupe_ni_sous_coupe_obligatoire);
				infos.addElement("Calcul des scores a la fin:classique");
				MainBelote pile=new MainBelote();
				for(byte carte=0;carte<32;carte++)
				{
					pile.ajouter(new CarteBelote(carte));
				}
				Donne donne=new Donne("Sans surcontrat",infos,0l,pile);
				donne.initDonneur((byte)1);
				donne.initDonne();
				par=new PartieBelote(Type.Editer,donne);
			}
			else
			{
				for(Couleur couleur:Couleur.values())
				{
					if(couleur.ordinal()>0)
					{
						info+=Parametres.separateur_tiret;
					}
					info+=couleur;
				}
				infos.addElement("Tarot:");
				infos.addElement("Battre les cartes:"+ChoixBattreCartes.jamais);
				infos.addElement("Sens de distribution:"+Sens.Anti_horaire);
				infos.addElement("Tri:"+info+";"+Monotonie.Decroissant);
				info=ch_v;
				for(Poignees poignee:Poignees.values())
				{
					if(poignee.ordinal()>0)
					{
						info+=Parametres.separateur_tiret;
					}
					info+=poignee;
				}
				infos.addElement("Poignees:"+info+";Miseres:");
				infos.addElement("Chelem contrat:non");
				infos.addElement("Regle du demi-point:classique");
				infos.addElement("Mode:Normal");
				infos.addElement("4 joueurs:1 vs 3");
				infos.addElement("5 joueurs:"+Repartition5Joueurs.c2_vs_3_appel_au_roi);//1 vs 4 2 vs 3, appel au roi
				MainTarot pile=new MainTarot();
				for(byte carte=0;carte<78;carte++)
				{
					pile.ajouter(new CarteTarot(carte));
				}
				Donne donne=new Donne("5 joueurs",infos,0l,pile);
				donne.initDonneur((byte)1);
				donne.initDonne();
				par=new PartieTarot(Type.Editer,donne);
			}
		}
		public void run()
		{
			//desactiver le menu Partie/aide au jeu
			getJMenuBar().getMenu(1).getItem(2).setEnabled(false);
			//desactiver le menu Partie/Demo
			getJMenuBar().getMenu(1).getItem(4).setEnabled(false);
			byte indice_main_depart;
			Pli.nombreTotal=0;
			((Levable)par).simuler();
			//Activer le menu Partie/Pause
			getJMenuBar().getMenu(1).getItem(1).setEnabled(true);
			Vector<MainTriable> mains_utilisateurs=new Vector<MainTriable>();
			byte nombre_joueurs=par.getNombreDeJoueurs();
			Pli pli_petit=null;
			Vector<Pli> plis_faits=((Levable)par).unionPlis();
			if(!plis_faits.isEmpty())
			{
				if(par instanceof PartieTarot)
				{
					pli_petit=((PartieTarot)par).pliLePlusPetit(plis_faits);
					mains_utilisateurs.add(0,new MainTarot());
					mains_utilisateurs.get(0).ajouter(plis_faits.get(plis_faits.size()-1).carteDuJoueur((byte)0, nombre_joueurs, pli_petit));
					for(int indice_pli=plis_faits.size()-2;indice_pli>-1;indice_pli--)
					{
						Pli pli=plis_faits.get(indice_pli);
						if(pli.getNumero()>0&&pli.total()>=nombre_joueurs-1)
						{
							mains_utilisateurs.add(0,new MainTarot());
							mains_utilisateurs.get(0).ajouterCartes(mains_utilisateurs.get(1));
							mains_utilisateurs.get(0).ajouter(pli.carteDuJoueur((byte)0, nombre_joueurs, pli_petit));
							mains_utilisateurs.get(0).trier(par.getInfos().get(3),par.getInfos().get(3));
						}
					}
					if(((Levable)par).getPreneur()==0&&((Levable)par).getContrat().force()<3)
					{
						mains_utilisateurs.add(0,new MainTarot());
						mains_utilisateurs.get(0).ajouterCartes(mains_utilisateurs.get(1));
						mains_utilisateurs.get(0).ajouterCartes(plis_faits.get(0).getCartes());
						mains_utilisateurs.get(0).trier(par.getInfos().get(3),par.getInfos().get(3));
						mains_utilisateurs.add(0,new MainTarot());
						mains_utilisateurs.get(0).ajouterCartes(mains_utilisateurs.get(1));
						mains_utilisateurs.get(0).supprimerCartes(par.getDistribution().derniereMain());
						mains_utilisateurs.get(0).trier(par.getInfos().get(3),par.getInfos().get(3));
					}
				}
				else
				{
					mains_utilisateurs.add(0,new MainBelote());
					mains_utilisateurs.get(0).ajouter(plis_faits.get(plis_faits.size()-1).carteDuJoueur((byte)0, nombre_joueurs, pli_petit));
					for(int indice_pli=plis_faits.size()-2;indice_pli>-1;indice_pli--)
					{
						Pli pli=plis_faits.get(indice_pli);
						mains_utilisateurs.add(0,new MainBelote());
						mains_utilisateurs.get(0).ajouterCartes(mains_utilisateurs.get(1));
						mains_utilisateurs.get(0).ajouter(pli.carteDuJoueur((byte)0, nombre_joueurs, pli_petit));
						((MainBelote)mains_utilisateurs.get(0)).setOrdre(Ordre.valueOf(par.getInfos().get(4).split(":")[1]));
						mains_utilisateurs.get(0).trier(par.getInfos().get(3),par.getInfos().get(3));
					}
					mains_utilisateurs.add(0,new MainBelote());
					mains_utilisateurs.get(0).ajouterCartes(mains_utilisateurs.get(1));
					mains_utilisateurs.get(0).supprimerCartes(par.getDistribution().derniereMain());
					((MainBelote)mains_utilisateurs.get(0)).setOrdre(Ordre.valueOf(par.getInfos().get(4).split(":")[1]));
					mains_utilisateurs.get(0).trier(par.getInfos().get(3),par.getInfos().get(3));
				}
			}
			else
			{
				mains_utilisateurs.addElement((MainTriable)par.getDistribution().main());
				if(par instanceof PartieBelote)
				{
					((MainBelote)mains_utilisateurs.get(0)).setOrdre(Ordre.valueOf(par.getInfos().get(4).split(":")[1]));
				}
				mains_utilisateurs.get(0).trier(par.getInfos().get(3),par.getInfos().get(3));
			}
			placer_ihm_levable();
			JPanel panneau=(JPanel)((JPanel)getContentPane().getComponent(3)).getComponent(1);
			JButton bouton=new JButton("Arreter");
			bouton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent event)
				{
					arret_demo=true;
				}
			});
			panneau.add(bouton);
			afficherMainUtilisateur(mains_utilisateurs.get(0));
			pack();
			Partie.chargement_simulation=100;
			try {
				Thread.sleep(500);
			} catch (Exception exception) {
				exception.printStackTrace();
			}
			ajouterTexteDansZone("Les joueurs se mettent autour de la table."+st);
			pause();
			byte preneur=((Levable)par).getPreneur();
			if(par instanceof PartieTarot)
			{
				indice_main_depart=(byte)(preneur==0&&((Levable)par).getContrat().force()<3?3:1);
			}
			else
			{
				indice_main_depart=2;
			}
			Vector<Contrat> contrats=((Levable)par).tous_contrats();
			int taille_contrat=contrats.size();
			byte donneur=par.getDistribution().getDonneur();
			byte entameur=(byte)((donneur+1)%nombre_joueurs);
			Vector<String> pseudos=pseudos();
			boolean belote=par instanceof PartieBelote;
			for(int indice_contrat=0;indice_contrat<taille_contrat;indice_contrat++)
			{
				byte joueur=(byte)((entameur+indice_contrat)%nombre_joueurs);
				ajouterTexteDansZone(pseudos.get(joueur)+" reflechit pour annoncer son contrat."+st);
				try
				{
					Thread.sleep(1000);
				}
				catch(Exception exception)
				{
					exception.printStackTrace();
				}
				if(belote)
				{
					if(contrats.get(indice_contrat).force()!=1)
					{
						ajouterTexteDansZone(pseudos.get(joueur)+":"+contrats.get(indice_contrat)+st);
						ajouterTexteDansPanneau(contrats.get(indice_contrat).toString(),joueur);
					}
					else
					{
						ajouterTexteDansZone(pseudos.get(joueur)+":"+((PartieBelote)par).getCarteAppelee().toString().split(es+ch_v)[2]+st);
						ajouterTexteDansPanneau(((PartieBelote)par).getCarteAppelee().toString().split(es+ch_v)[2],joueur);
					}
				}
				else
				{
					ajouterTexteDansZone(pseudos.get(joueur)+":"+contrats.get(indice_contrat)+st);
					ajouterTexteDansPanneau(contrats.get(indice_contrat).toString(),joueur);
				}
				ajouterTexteDansZone(ch_v+st);
				pause();
				if(arret_demo)
				{
					arret_demo();
					return;
				}
			}
			if(((Levable)par).getContrat().force()==0)
			{
				panneau=new JPanel();
				JPanel sous_panneau=new JPanel(new GridLayout(0,1));
				sous_panneau.setBorder(BorderFactory.createTitledBorder("Resultats"));
				sous_panneau.add(new JLabel("Donneur: "+pseudos.get(par.getDistribution().getDonneur())));
				sous_panneau.add(new JLabel("Personne n a pris."));
				Vector<Short> scores_partie=new Vector<Short>();
				for(byte joueur=0;joueur<nombre_joueurs;joueur++)
				{
					scores_partie.addElement((short)0);
				}
				panneau.add(sous_panneau);
				getContentPane().remove(0);
				getContentPane().remove(0);
				getContentPane().remove(0);
				getContentPane().add(panneau,BorderLayout.CENTER,0);
				for(;;)
				{
					if(arret_demo)
					{
						arret_demo();
						return;
					}
				}
			}
			if(par instanceof PartieTarot)
			{
				byte appele=((PartieTarot)par).getAppele();
				if(((PartieTarot)par).getCarteAppelee()!=null)
				{
					ajouterTexteDansZone("Le preneur reflechit pour choisir la carte appelee qui l arrange le plus."+st);
					ajouterTexteDansZone("Il doit faire attention de ne pas s appeler"+st);
					ajouterTexteDansZone(ch_v+st);
					try
					{
						Thread.sleep(1000);
					}
					catch(Exception exception)
					{
						exception.printStackTrace();
					}
					pause();
					if(arret_demo)
					{
						arret_demo();
						return;
					}
					ajouterTexteDansZone(pseudos.get(preneur)+":"+((PartieTarot)par).getCarteAppelee()+st);
					ajouterTexteDansZone("Le joueur ayant la carte "+((PartieTarot)par).getCarteAppelee()+" est le partenaire du preneur (il est l appele)."+st);
					ajouterTexteDansZone("Mais l appele ne doit pas dire qu il fait equipe avec le preneur."+st);
					ajouterTexteDansZone(ch_v+st);
					ajouterTexteDansPanneau(((PartieTarot)par).getCarteAppelee().toString(),preneur);
				}
				else
				{
					if(appele>-1)
					{
						ajouterTexteDansZone("Le preneur fait equipe avec "+pseudos.get(appele)+"."+st);
					}
				}
				appel_ecart(preneur, appele, mains_utilisateurs);
				ajouterTexteDansZone(ch_v+st);
			}
			else
			{
				ajouterTexteDansZone("Le preneur ("+pseudos.get(preneur)+") fait equipe avec "+pseudos.get((preneur+2)%nombre_joueurs)+"."+st);
				ajouterTexteDansZone(pseudos.get(preneur)+" prend la carte du dessus."+st);
				ajouterTexteDansZone("Le preneur prend la carte du dessus."+st);
				ajouterTexteDansZone("Le donneur ("+pseudos.get(donneur)+") va distribuer les dernieres cartes:"+st);
				for(int indice_joueur=0;indice_joueur<nombre_joueurs;indice_joueur++)
				{
					byte joueur=(byte)((indice_joueur+entameur)%nombre_joueurs);
					if(joueur==preneur)
					{
						ajouterTexteDansZone(tb+(indice_joueur+1)+". deux cartes pour "+pseudos.get(joueur)+"."+st);
					}
					else
					{
						ajouterTexteDansZone(tb+(indice_joueur+1)+". trois cartes pour "+pseudos.get(joueur)+"."+st);
					}
				}
				ajouterTexteDansZone(ch_v+st);
				try
				{
					Thread.sleep(3000);
				}
				catch(Exception exc)
				{
					exc.printStackTrace();
				}
				((Tapis)getContentPane().getComponent(1)).retirerCartes();
				pause();
				if(arret_demo)
				{
					arret_demo();
					return;
				}
				afficherMainUtilisateur(mains_utilisateurs.get(1));
				pack();
				try
				{
					Thread.sleep(4000);
				}
				catch(Exception exc)
				{
					exc.printStackTrace();
				}
				pause();
				if(arret_demo)
				{
					arret_demo();
					return;
				}
			}
			int indice_pli=par instanceof PartieTarot?1:0;
			boolean passe_boucle=false;
			for(;indice_pli<plis_faits.size();indice_pli++)
			{
				Pli pli=plis_faits.get(indice_pli);
				if(pli.total()>nombre_joueurs-2)
				{
					entameur=pli.getEntameur();
					for(byte indice_carte=0;indice_carte<nombre_joueurs;indice_carte++)
					{
						byte joueur=(byte)((entameur+indice_carte)%nombre_joueurs);
						Carte carte=pli.carteDuJoueur(joueur, nombre_joueurs, pli_petit);
						if(joueur==entameur)
						{
							ajouterTexteDansZone(pseudos.get(joueur)+" reflechit pour le choix de la carte d entame."+st);
						}
						else
						{
							ajouterTexteDansZone(pseudos.get(joueur)+" reflechit pour le choix de la carte a jouer en fonction du pli."+st);
						}
						try
						{
							Thread.sleep(1000);
						}
						catch(Exception exc)
						{
							exc.printStackTrace();
						}
						pause();
						Vector<Annonce> annonces_joueur=((Annoncable)par).getAnnonces(joueur);
						if(indice_pli==(par instanceof PartieTarot?1:0))
						{
							if(par instanceof PartieTarot)
							{
								if(((PartieTarot)par).pas_jeu_misere()||((PartieTarot)par).getContrat()!=null)
								{
									for(Annonce annonce:annonces_joueur)
									{
										if(!annonce.toString().equals(Annonce.petit_au_bout))
										{
											ajouterTexteDansZone(pseudos.get(joueur)+":"+annonce+st);
											ajouterTexteDansPanneau(annonce.toString()+st,joueur);
										}
									}
									MainTarot poignee=((PartieTarot)par).getPoignee(joueur);
									if(!poignee.estVide())
									{
										int indexe=annonces_joueur.indexOf(new Annonce(Poignees.Poignee));
										if(indexe<0)
										{
											indexe=annonces_joueur.indexOf(new Annonce(Poignees.Double_Poignee));
										}
										if(indexe<0)
										{
											indexe=annonces_joueur.indexOf(new Annonce(Poignees.Triple_Poignee));
										}
										ajouterTexteDansZone(pseudos.get(joueur)+":"+annonces_joueur.get(indexe)+st);
										ajouterTexteDansZone(pseudos.get(joueur)+":"+poignee+st);
									}
								}
							}
							else
							{
								byte couleur_atout=((PartieBelote)par).couleur_atout();
								for(Annonce annonce:annonces_joueur)
								{
									if(!annonce.toString().equals(Annonce.dix_de_der))
									{
										if(annonce.toString().equals(Annonce.belote_rebelote))
										{
											if(carte.couleur()==couleur_atout&&carte.valeur()>11)
											{
												ajouterTexteDansZone(pseudos.get(joueur)+":"+annonce+st);
												ajouterTexteDansPanneau(annonce.toString()+st,joueur);
											}
										}
										else
										{
											ajouterTexteDansZone(pseudos.get(joueur)+":"+annonce+st);
											ajouterTexteDansPanneau(annonce.toString()+st,joueur);
										}
									}
								}
							}
						}
						else if(par instanceof PartieBelote)
						{
							if(carte.couleur()==((PartieBelote)par).couleur_atout()&&carte.valeur()>11)
							{
								ajouterTexteDansZone(pseudos.get(joueur)+":"+Annonce.belote_rebelote+st);
								ajouterTexteDansPanneau(Annonce.belote_rebelote+st,joueur);
							}
						}
						if(par instanceof PartieTarot)
						{
							if(((PartieTarot)par).getCarteAppelee()!=null&&((PartieTarot)par).getCarteAppelee().equals(carte))
							{
								ajouterTexteDansPanneau(Statut.Appele.toString(),joueur);
								ajouterTexteDansZone(pseudos.get(joueur)+":"+Statut.Appele.toString());
							}
						}
						((Tapis)getContentPane().getComponent(1)).setCarte(joueur,nombre_joueurs,carte);
						if(joueur==0)
						{
							if(indice_pli<plis_faits.size()-1)
							{
								if(par instanceof PartieTarot)
								{
									if(!passe_boucle)
									{
										afficherMainUtilisateur(mains_utilisateurs.get(indice_main_depart+indice_pli-1));
									}
									else
									{
										afficherMainUtilisateur(mains_utilisateurs.get(indice_main_depart+indice_pli-2));
									}
								}
								else
								{
									afficherMainUtilisateur(mains_utilisateurs.get(indice_main_depart+indice_pli));
								}
							}
							else if(par instanceof PartieTarot)
							{
								afficherMainUtilisateur(new MainTarot());
							}
							else
							{
								afficherMainUtilisateur(new MainBelote());
							}
							pack();
						}
						if(arret_demo)
						{
							arret_demo();
							return;
						}
					}
					byte ramasseur=((Levable)par).ramasseur(plis_faits,(byte)indice_pli);
					ajouterTexteDansZone("Le ramasseur est "+pseudos.get(ramasseur)+"."+st);
					ajouterTexteDansZone(ch_v+st);
					if(indice_pli==plis_faits.size()-1)
					{
						if(((Annoncable)par).getAnnonces(ramasseur).contains(new Annonce(Annonce.petit_au_bout)))
						{
							ajouterTexteDansZone(pseudos.get(ramasseur)+" gagne la prime du "+Annonce.petit_au_bout+"."+st);
						}
						else if(((Annoncable)par).getAnnonces(ramasseur).contains(new Annonce(Annonce.dix_de_der)))
						{
							ajouterTexteDansZone(pseudos.get(ramasseur)+" gagne la prime du "+Annonce.dix_de_der+"."+st);
						}
					}
					try
					{
						Thread.sleep(4000);
					}
					catch(Exception exc)
					{
						exc.printStackTrace();
					}
					pause();
					((Tapis)getContentPane().getComponent(1)).setCartes(nombre_joueurs,par.jeu());
					if(arret_demo)
					{
						arret_demo();
						return;
					}
				}
				else
				{
					passe_boucle=true;
				}
			}
			Vector<Short> scores_partie=new Vector<Short>();
			if(par instanceof PartieBelote)
			{
				panneau=new JPanel();
				JPanel sous_panneau=new JPanel(new GridLayout(0,1));
				sous_panneau.setBorder(BorderFactory.createTitledBorder("Resultats"));
				Vector<Vector<Short>> annonces_defense=new Vector<Vector<Short>>();
				Vector<Vector<Short>> annonces_attaque=new Vector<Vector<Short>>();
				Contrat contrat=((PartieBelote)par).getContrat();
				byte appele=(byte)((preneur+2)%nombre_joueurs);
				byte defenseur1=(byte)((preneur+1)%nombre_joueurs);
				byte defenseur2=(byte)((preneur+3)%nombre_joueurs);
				int capot_attaque=((PartieBelote)par).valeurCapot();
				int pointsAttaqueSansPrime=((PartieBelote)par).pointsAttaqueSansPrime();
				int pointsAttaqueTemporaire=pointsAttaqueSansPrime;
				int pointsAttaqueDefinitif=0;
				int pointsDefenseSansPrime=152-pointsAttaqueSansPrime;
				int pointsDefenseTemporaire=pointsDefenseSansPrime;
				int pointsDefenseDefinitif=0;
				short difference_score;
				sous_panneau.add(new JLabel("Donneur: "+pseudos.get(par.getDistribution().getDonneur())));
				annonces_attaque.addElement(((PartieBelote)par).pointsAnnoncesPrimes(preneur));
				annonces_attaque.addElement(((PartieBelote)par).pointsAnnoncesPrimes(appele));
				annonces_defense.addElement(((PartieBelote)par).pointsAnnoncesPrimes(defenseur1));
				annonces_defense.addElement(((PartieBelote)par).pointsAnnoncesPrimes(defenseur2));
				for(Vector<Short> points_annonces_joueur:annonces_attaque)
				{
					for(short points_annonce:points_annonces_joueur)
					{
						pointsAttaqueTemporaire+=points_annonce;
					}
				}
				pointsAttaqueTemporaire+=capot_attaque;
				for(Vector<Short> points_annonces_joueur:annonces_defense)
				{
					for(short points_annonce:points_annonces_joueur)
					{
						pointsDefenseTemporaire+=points_annonce;
					}
				}
				sous_panneau.add(new JLabel("Preneur: "+pseudos.get(preneur)));
				sous_panneau.add(new JLabel("Partenaire du preneur: "+pseudos.get(appele)));
				sous_panneau.add(new JLabel("Points realises en attaque sans les primes: "+pointsAttaqueSansPrime));
				sous_panneau.add(new JLabel("Points realises en defense sans les primes: "+pointsDefenseSansPrime));
				sous_panneau.add(new JLabel("Points realises en attaque avec les primes: "+pointsAttaqueTemporaire));
				sous_panneau.add(new JLabel("Points realises en defense avec les primes: "+pointsDefenseTemporaire));
				pointsAttaqueDefinitif=((PartieBelote)par).scoreDefinitifAttaque(pointsAttaqueTemporaire, pointsDefenseTemporaire);
				pointsDefenseDefinitif=((PartieBelote)par).scoreDefinitifDefense(pointsAttaqueDefinitif);
				sous_panneau.add(new JLabel("Score en attaque d"+ea+"finitif: "+pointsAttaqueDefinitif));
				sous_panneau.add(new JLabel("Score en defense d"+ea+"finitif: "+pointsDefenseDefinitif));
				((PartieBelote)par).scores(pointsAttaqueDefinitif, pointsDefenseDefinitif);
				byte gagne_nul_perd=((PartieBelote)par).gagne_nul_perd();
				scores_partie=par.getScores();
				if(gagne_nul_perd==-1)
				{
					sous_panneau.add(new JLabel("Vous avez perdu"));
				}
				else if(gagne_nul_perd==0)
				{
					sous_panneau.add(new JLabel("Match nul"));
				}
				else
				{
					sous_panneau.add(new JLabel("Vous avez gagne"));
				}
				difference_score=(short) (scores_partie.get(preneur)-scores_partie.get((preneur+1)%nombre_joueurs));
				if(difference_score>0)
				{
					if(contrat.force()==1)
					{
						sous_panneau.add(new JLabel("Le contrat a la couleur "+((PartieBelote)par).getCarteAppelee().toString().split(" ")[2]+" est reussi de "+difference_score));
					}
					else
					{
						sous_panneau.add(new JLabel("Le contrat "+contrat+" est reussi de "+difference_score));
					}
				}
				else if(difference_score<0)
				{
					if(contrat.force()==1)
					{
						sous_panneau.add(new JLabel("Le contrat a la couleur "+((PartieBelote)par).getCarteAppelee().toString().split(" ")[2]+" est chute de "+-difference_score));
					}
					else
					{
						sous_panneau.add(new JLabel("Le contrat "+contrat+" est chute de "+-difference_score));
					}
				}
				if(capot_attaque>0)
				{
					sous_panneau.add(new JLabel("Capot reussi en attaque"));
				}
				panneau.add(sous_panneau);
				getContentPane().remove(0);
				getContentPane().remove(0);
				getContentPane().remove(0);
				getContentPane().add(panneau,BorderLayout.CENTER,0);
				pack();
			}
			else
			{
				Contrat contrat=((PartieTarot)par).getContrat();
				byte appele=((PartieTarot)par).getAppele();
				panneau=new JPanel();
				JPanel sous_panneau=new JPanel(new GridLayout(0,1));
				short base=0;
				short difference_score_preneur=0;
				sous_panneau.setBorder(BorderFactory.createTitledBorder("R"+ea+"sultats"));
				sous_panneau.add(new JLabel("Donneur: "+pseudos.get(par.getDistribution().getDonneur())));
				Vector<Vector<Annonce>> annonces_sans_petit_bout=new Vector<Vector<Annonce>>();
				Vector<Vector<Short>> calcul_annonces_score_preneur=new Vector<Vector<Short>>();
				Vector<Vector<Vector<Short>>> points_annonces=new Vector<Vector<Vector<Short>>>();
				byte[][] coefficients_repartition=new byte[1][1];
				short[] primes_supplementaires=new short[2];
				short score_preneur_sans_annonces=0;
				short[] scores_joueurs_plis_double=new short[nombre_joueurs];
				short[] scores_necessaires_joueurs=new short[nombre_joueurs];
				short[] differences_joueurs_double=new short[nombre_joueurs];
				byte[] positions=new byte[nombre_joueurs];
				byte[] coefficients=new byte[nombre_joueurs];
				short[] prime_supplementaire=new short[nombre_joueurs];
				short difference_max_double=0;
				short difference_max=0;
				byte position_initiale_utilisateur=0;
				if(contrat.force()>0)
				{
					byte premier_defenseur=-1;
					for(byte joueur=0;joueur<nombre_joueurs;joueur++)
					{
						if(joueur!=preneur&&joueur!=appele)
						{
							premier_defenseur=joueur;
							break;
						}
					}
					short score_preneur_plis_double=((PartieTarot)par).score_preneur_plis_double(pli_petit);
					byte nombre_bouts_preneur=((PartieTarot)par).nombre_bouts_preneur();
					short score_necessaire_preneur=((PartieTarot)par).score_necessaire_preneur();
					short score_preneur_plis=((PartieTarot)par).score_preneur_plis(score_preneur_plis_double, score_necessaire_preneur);
					difference_score_preneur=(short) (score_preneur_plis-score_necessaire_preneur);
					base=((PartieTarot)par).base(score_preneur_plis_double,difference_score_preneur);
					score_preneur_sans_annonces=((PartieTarot)par).score_preneur_sans_annonces(difference_score_preneur,base);
					annonces_sans_petit_bout=((PartieTarot)par).annonces_sans_petit_bout();
					calcul_annonces_score_preneur=((PartieTarot)par).calcul_annonces_score_preneur(score_preneur_sans_annonces,annonces_sans_petit_bout);
					primes_supplementaires=((PartieTarot)par).primes_supplementaires();
					short somme_temporaire=((PartieTarot)par).somme_temporaire(score_preneur_sans_annonces,calcul_annonces_score_preneur,primes_supplementaires);
					coefficients_repartition=((PartieTarot)par).coefficients_repartition();
					byte gagne_nul_perd=((PartieTarot)par).gagne_nul_perd(score_preneur_sans_annonces);
					sous_panneau.add(new JLabel("nombre de Bouts en attaque dans les plis: "+nombre_bouts_preneur));
					sous_panneau.add(new JLabel("Nombre de points n"+ea+"cessaires pour que le preneur gagne: "+score_necessaire_preneur));
					sous_panneau.add(new JLabel("nombre de points en attaque dans les plis: "+score_preneur_plis_double/2.0));
					sous_panneau.add(new JLabel("Preneur:"+pseudos.get(preneur)));
					if(appele>-1)
					{
						sous_panneau.add(new JLabel("Appele:"+pseudos.get(appele)));
						Carte carteAppelee=((PartieTarot)par).getCarteAppelee();
						if(carteAppelee!=null)
						{
							sous_panneau.add(new JLabel("Carte appelee:"+carteAppelee));
						}
					}
					if(gagne_nul_perd==-1)
					{
						sous_panneau.add(new JLabel("Vous avez perdu"));
					}
					else if(gagne_nul_perd==0)
					{
						sous_panneau.add(new JLabel("Match nul"));
					}
					else
					{
						sous_panneau.add(new JLabel("Vous avez gagne"));
					}
					if(score_preneur_sans_annonces>0)
					{
						sous_panneau.add(new JLabel("Le contrat "+contrat+" est reussi de "+difference_score_preneur));
					}
					else if(score_preneur_sans_annonces<0)
					{
						sous_panneau.add(new JLabel("Le contrat "+contrat+" est chute de "+-difference_score_preneur));
					}
					if(primes_supplementaires[0]==new Annonce(Contrat.chelem).points())
					{
						sous_panneau.add(new JLabel("Chelem d attaque reussi et demande"));
					}
					else if(primes_supplementaires[0]==new Annonce(Contrat.chelem).points()/2)
					{
						sous_panneau.add(new JLabel("Chelem d attaque reussi et non demande"));
					}
					else if(primes_supplementaires[0]<0)
					{
						sous_panneau.add(new JLabel("Chelem d attaque chute et demande"));
					}
					if(primes_supplementaires[1]>0)
					{
						sous_panneau.add(new JLabel("Chelem de defense reussi"));
					}
					((PartieTarot)par).calculer_scores(coefficients_repartition, somme_temporaire, score_preneur_sans_annonces);
					scores_partie=par.getScores();
					sous_panneau.add(new JLabel("Score du preneur: "+scores_partie.get(preneur)+" points"));
					if(appele>-1)
					{
						sous_panneau.add(new JLabel("Score de l appele: "+scores_partie.get(appele)+" points"));
					}
					sous_panneau.add(new JLabel("Score d un defenseur: "+scores_partie.get(premier_defenseur)+" points"));
				}
				else
				{
					boolean pas_jeu_misere=((PartieTarot)par).pas_jeu_misere();
					if(pas_jeu_misere)
					{
						for(byte joueur=0;joueur<nombre_joueurs;joueur++)
						{
							scores_joueurs_plis_double[joueur]=((PartieTarot)par).score_joueur_plis_double(pli_petit, joueur);
							scores_necessaires_joueurs[joueur]=((PartieTarot)par).score_necessaire_joueur(joueur);
							differences_joueurs_double[joueur]=((PartieTarot)par).difference_joueur_double(scores_necessaires_joueurs[joueur],scores_joueurs_plis_double[joueur]);
							difference_max_double=(short)Math.max(difference_max_double,differences_joueurs_double[joueur]);
							points_annonces.addElement(((PartieTarot)par).calcul_annonces_score_joueur(joueur));
							prime_supplementaire[joueur]=((PartieTarot)par).prime_supplementaire(joueur);
						}
						difference_max=((PartieTarot)par).difference_max(difference_max_double);
						sous_panneau.add(new JLabel("Difference la plus grande: "+difference_max+" points"));
						positions=((PartieTarot)par).positions_difference(differences_joueurs_double);
						position_initiale_utilisateur=positions[0];
						sous_panneau.add(new JLabel("Votre position avant departage des joueurs: "+position_initiale_utilisateur));
						((PartieTarot)par).changer_positions1(positions,pas_jeu_misere);
						((PartieTarot)par).changer_positions2(positions,pas_jeu_misere);
						((PartieTarot)par).changer_positions3(positions,pas_jeu_misere);
						((PartieTarot)par).changer_positions4(positions,pas_jeu_misere);
						sous_panneau.add(new JLabel("Votre position apres departage des joueurs: "+positions[0]));
						coefficients=((PartieTarot)par).coefficients(positions);
						((PartieTarot)par).calculer_scores_joueurs(coefficients, points_annonces, difference_max_double, prime_supplementaire);
						scores_partie=par.getScores();
					}
					else
					{
						for(byte joueur=0;joueur<nombre_joueurs;joueur++)
						{
							scores_joueurs_plis_double[joueur]=((PartieTarot)par).score_joueur_plis_double(pli_petit, joueur);
							scores_necessaires_joueurs[joueur]=((PartieTarot)par).score_necessaire_joueur(joueur);
							differences_joueurs_double[joueur]=((PartieTarot)par).difference_joueur_double_misere(scores_necessaires_joueurs[joueur],scores_joueurs_plis_double[joueur]);
							difference_max_double=(short)Math.max(difference_max_double,differences_joueurs_double[joueur]);
						}
						difference_max=((PartieTarot)par).difference_max(difference_max_double);
						sous_panneau.add(new JLabel("Difference la plus grande: "+difference_max+" points"));
						positions=((PartieTarot)par).positions_difference(differences_joueurs_double);
						position_initiale_utilisateur=positions[0];
						sous_panneau.add(new JLabel("Votre position avant departage des joueurs: "+position_initiale_utilisateur));
						((PartieTarot)par).changer_positions1(positions,pas_jeu_misere);
						((PartieTarot)par).changer_positions2(positions,pas_jeu_misere);
						((PartieTarot)par).changer_positions3(positions,pas_jeu_misere);
						((PartieTarot)par).changer_positions4(positions,pas_jeu_misere);
						sous_panneau.add(new JLabel("Votre position apres departage des joueurs: "+positions[0]));
						coefficients=((PartieTarot)par).coefficients_misere(positions);
						((PartieTarot)par).calculer_scores_joueurs(coefficients,difference_max_double);
						scores_partie=par.getScores();
					}
				}
				panneau.add(sous_panneau);
				getContentPane().remove(0);
				getContentPane().remove(0);
				getContentPane().remove(0);
				getContentPane().add(panneau,BorderLayout.CENTER,0);
				pack();
			}
			try
			{
				Thread.sleep(5000);
			}
			catch(Exception exc)
			{
				exc.printStackTrace();
			}
			pause();
			arret_demo();
		}
		private void arret_demo()
		{
			if(jeu==null)
			{
				menuPrincipal();
			}
			else
			{
				modifierJeu(jeu.toString());
			}
		}
		private void appel_ecart(byte preneur,byte appele,Vector<MainTriable> mains_utilisateurs)
		{
			if(((Levable)par).getContrat().force()<3)
			{
				ajouterTexteDansZone("Le chien va etre retourne."+st);
				try
				{
					Thread.sleep(1000);
				}
				catch(Exception exc)
				{
					exc.printStackTrace();
				}
				setChien((MainTarot)par.getDistribution().derniereMain(),false);
				ajouterTexteDansZone("Les joueurs regardent le chien."+st);
				ajouterTexteDansZone(ch_v+st);
				pack();
				pause();
				if(arret_demo)
				{
					arret_demo();
					return;
				}
				try
				{
					Thread.sleep(1000);
				}
				catch(Exception exc)
				{
					exc.printStackTrace();
				}
				if(appele==preneur)
				{
					ajouterTexteDansZone("Ah! dur pour le preneur, il est tout seul contre les autres joueurs."+st);
					ajouterTexteDansZone(ch_v+st);
				}
				pack();
				pause();
				if(arret_demo)
				{
					arret_demo();
					return;
				}
				ajouterTexteDansZone("Le preneur prend le chien."+st);
				ajouterTexteDansZone(ch_v+st);
				((Tapis)getContentPane().getComponent(1)).retirerCartes();;
				if(preneur==0)
				{
					afficherMainUtilisateur(mains_utilisateurs.get(1));
				}
				try
				{
					Thread.sleep(1000);
				}
				catch(Exception exc)
				{
					exc.printStackTrace();
				}
				pack();
				pause();
				if(arret_demo)
				{
					arret_demo();
					return;
				}
				ajouterTexteDansZone("Le preneur ecarte "+par.getDistribution().derniereMain().total()+" cartes."+st);
				ajouterTexteDansZone(ch_v+st);
				((Tapis)getContentPane().getComponent(1)).setEcart(par.getDistribution().derniereMain(),par.jeu());
				if(preneur==0)
				{
					afficherMainUtilisateur(mains_utilisateurs.get(2));
				}
				try
				{
					Thread.sleep(1000);
				}
				catch(Exception exc)
				{
					exc.printStackTrace();
				}
				pack();
				pause();
				if(arret_demo)
				{
					arret_demo();
					return;
				}
				if(((Annoncable)par).getAnnonces(preneur).contains(new Annonce(Primes.Chelem)))
				{
					ajouterTexteDansZone("Apres ecart, le preneur annonce un Chelem."+st);
					ajouterTexteDansZone(ch_v+st);
				}
			}
			else
			{
				if(((Annoncable)par).getAnnonces(preneur).contains(new Annonce(Primes.Chelem)))
				{
					ajouterTexteDansZone("Le preneur annonce un Chelem."+st);
					ajouterTexteDansZone(ch_v+st);
				}
			}
			ajouterTexteDansZone("Le jeu des cartes va commencer."+st);
			try
			{
				Thread.sleep(2000);
			}
			catch(Exception exc)
			{
				exc.printStackTrace();
			}
			pack();
			pause();
			if(arret_demo)
			{
				arret_demo();
				return;
			}
		}
	}
	private class AnimationChargement extends Thread
	{
		public void run()
		{
			JProgressBar barre_progression=new JProgressBar();
			barre_progression.setValue(0);
			barre_progression.setPreferredSize(new Dimension(200,50));
			Container container=new Container();
			container.setLayout(new GridLayout(0,1));
			JLabel etiquette=new JLabel("Chargement a "+barre_progression.getValue()+" %");
			container.add(etiquette);
			container.add(barre_progression);
			JWindow fenetre2=new JWindow(Fenetre.this);
			fenetre2.setLocationRelativeTo(Fenetre.this);
			fenetre2.setContentPane(container);
			fenetre2.pack();
			fenetre2.setVisible(true);
			int max=barre_progression.getMaximum();
			for(;barre_progression.getValue()<max;)
			{
				barre_progression.setValue(Partie.chargement_simulation);
				etiquette.setText("Chargement a "+barre_progression.getValue()+" %");
			}
			fenetre2.dispose();
		}
	}
	/**On ecoute les boutons du menu principal et des menus jeux*/
	private class EcouteurBoutonPrincipal implements ActionListener {
		private String bouton;
		private Jeu jeu_bouton;
		private String mode2;
		private EcouteurBoutonPrincipal(JButton pb)
		{
			bouton=pb.getText();
		}
		private EcouteurBoutonPrincipal(JButton pb,Jeu pj)
		{
			bouton=pb.getText();
			jeu_bouton=pj;
		}
		private EcouteurBoutonPrincipal(JButton pb,String pmode2)
		{
			bouton=pb.getText();
			mode2=pmode2;
		}
		private EcouteurBoutonPrincipal(JButton pb,Jeu pj,String pmode2)
		{
			jeu_bouton=pj;
			bouton=pb.getText();
			mode2=pmode2;
		}
		public void actionPerformed(ActionEvent event) {
			if(jeu_bouton==null)
			{
				modifierJeu(bouton);
			}
			else
			{
				if(jeu_bouton==Jeu.Belote)
				{
					scores=new Vector<Vector<Long>>();
					max_absolu_score=0;
					sommes=new Vector<Long>();
					sigmas=new Vector<Double>();
					mode=bouton;
					changer_pile_fin=true;
					modifierJeuBelote();
				}
				else if(mode2!=null)
				{
					if(mode2.equals(retour))
					{
						modifierJeu(Jeu.Tarot.toString());
					}
					else
					{
						scores=new Vector<Vector<Long>>();
						max_absolu_score=0;
						sommes=new Vector<Long>();
						sigmas=new Vector<Double>();
						param.getInfos().get(Jeu.Tarot.ordinal()+1).setElementAt("Mode:"+mode2,7);
						changer_pile_fin=true;
						modifierJeuTarot();
					}
				}
				else
				{
					mode=bouton;
					changer_pile_fin=false;
					enCoursDePartie=false;
					par=null;
					setTitle(jeu_bouton.toString()+es+mode);
					Container container=new Container();
					container.setLayout(new GridLayout(0,1));
					//Active le menu Fichier/Changer de jeu
					getJMenuBar().getMenu(0).getItem(4).setEnabled(true);
					//Activer le menu Partie/Demo
					getJMenuBar().getMenu(1).getItem(4).setEnabled(true);
					for(String choix:Jouer.modes2)
					{
						ajouterBoutonPrincipal(choix.replace(Lettre._,Lettre.es),Jeu.Tarot,choix.replace(Lettre._,Lettre.es),container);
					}
					ajouterBoutonPrincipal(retour,Jeu.Tarot,retour,container);
					container.add(new JLabel("Pour utiliser l'aide allez dans le menu aide",JLabel.CENTER));
					setContentPane(container);
					pack();
				}
			}
		}
	}
	/**On ecoute les boutons de fin de partie*/
	private class EcouteurFinDePartie implements ActionListener{
		private String bouton;
		private EcouteurFinDePartie(String pbouton)
		{
			bouton=pbouton;
		}
		public void actionPerformed(ActionEvent event) {
			if(bouton.equals("Continuer sur le jeu actuel"))
			{
				changer_pile_fin=true;
				if(par instanceof PartieBelote)
				{
					modifierJeuBelote();
				}
				else
				{
					modifierJeuTarot();
				}
			}
			else if(bouton.equals("Rejouer donne"))
			{
				if(par instanceof Levable)
				{
					Pli.nombreTotal=0;
					Vector<Pli> plis_faits=((Levable)par).unionPlis();
					if(par instanceof PartieTarot)
					{
						byte nombre_excuse=0;
						for(byte indice_pli=(byte)(plis_faits.size()-1);indice_pli>0;indice_pli--)
						{
							Pli pli=plis_faits.get(indice_pli);
							if(pli.contient(new CarteTarot((byte)0)))
							{
								nombre_excuse++;
								if(nombre_excuse>1)
								{
									pli.getCartes().jouer(new CarteTarot((byte)0));
								}
							}
						}
					}
					((Levable)par).restituerMainsDepartRejouerDonne(plis_faits, par.getNombreDeJoueurs());
					((Levable)par).initPartie();
					if(par instanceof PartieBelote)
					{
						mettre_en_place_ihm_belote();
					}
					else
					{
						mettre_en_place_ihm_tarot();
					}
				}
			}
			else if(bouton.equals("Arreter"))
			{
				if(partie_aleatoire_jouee)
				{
					modifierJeu(par.jeu().toString());
				}
				else
				{
					menuPrincipal();
				}
			}
			else
			{
				par.setNombre();
				Main main=((Levable)par).empiler();
				if(par instanceof PartieTarot)
				{
					byte nombre_excuse=0;
					byte indice_carte=-1;
					byte indice=0;
					for(Carte carte:main)
					{
						if(carte.couleur()==0)
						{
							nombre_excuse++;
							if(nombre_excuse>1)
							{
								indice_carte=indice;
							}
						}
						indice++;
					}
					if(indice_carte>0)
					{
						main.jouer(indice_carte);
					}
				}
				Donne donne=new Donne(par.getMode(),par.getInfos(),0L,main);
				donne.donneurSuivant(par.getDistribution().getDonneur());
				donne.initDonne();
				if(par instanceof PartieBelote)
				{
					par=new PartieBelote(Type.Editer,donne);
					mettre_en_place_ihm_belote();
				}
				else
				{
					par=new PartieTarot(Type.Editer,donne);
					mettre_en_place_ihm_tarot();
				}
			}
		}
	}
	private void modifierJeu(String chaine_jeu)
	{
		enCoursDePartie=false;
		par=null;
		changer_pile_fin=false;
		setTitle(chaine_jeu);
		Container container=new Container();
		jeu=Jeu.valueOf(chaine_jeu.replace(ea,Lettre.e));
		container.setLayout(new GridLayout(0,1));
		//Active le menu Fichier/Changer de jeu
		getJMenuBar().getMenu(0).getItem(4).setEnabled(true);
		//Activer le menu Partie/Demo
		getJMenuBar().getMenu(1).getItem(4).setEnabled(true);
		//desactiver le menu Partie/aide au jeu
		getJMenuBar().getMenu(1).getItem(2).setEnabled(false);
		//desactiver le menu Partie/conseil
		getJMenuBar().getMenu(1).getItem(0).setEnabled(false);
		//Desactiver le menu Partie/Pause
		getJMenuBar().getMenu(1).getItem(1).setEnabled(false);
		if(jeu==Jeu.Tarot)
		{
			for(String choix:Jouer.modes1.get(jeu.ordinal()))
			{
				ajouterBoutonPrincipal(choix.replace(Lettre._,Lettre.es),jeu,container);
			}
		}
		else
		{
			for(String choix:Jouer.modes1.get(jeu.ordinal()))
			{
				ajouterBoutonPrincipal(ModeBelote.valueOf(choix).toString(),jeu,container);
			}
		}
		container.add(new JLabel("Pour utiliser l'aide allez dans le menu aide",JLabel.CENTER));
		setContentPane(container);
		pack();
	}
	private void setChien(MainTarot main,boolean ecouteur)
	{
		JPanel panneau=(JPanel)((JPanel)getContentPane().getComponent(1)).getComponent(4);
		panneau.removeAll();
		panneau.repaint();
		panneau.setBackground(new Color(0,125,0));
		panneau.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
		for(byte indice=0;indice<main.total();indice++)
		{
			CarteGraphique carte=new Place(main.carte(indice),JLabel.RIGHT,indice!=0);
			if(indice==0)
			{
				carte.setPreferredSize(new Dimension(100,150));
			}
			else
			{
				carte.setPreferredSize(new Dimension(25,150));
			}
			if(ecouteur)
			{
				carte.addMouseListener(new EcouteurCarte(main.carte(indice)));
			}
			panneau.add(carte);
		}
	}
	private void placerBoutonsAvantJeuUtilisateur(boolean premierTour)
	{
		String Revoir_pli_precedent=Boutons.Revoir_le_pli_precedent.toString();
		String Revoir_chien=Boutons.Revoir_le_chien.toString();
		raison_courante=ch_v;
		if(premierTour)
		{
			Vector<Annonce> annonces=((Annoncable)par).getAnnoncesPossibles((byte)0);
			Vector<Annonce> va=new Vector<Annonce>();
			if(par instanceof PartieBelote)
			{
				va.addElement(new Annonce(Annonce.belote_rebelote));
			}
			else
			{
				String[] ss;
				for(int indice=0;indice<2;indice++)
				{
					if(par.getInfos().get(4).split(";")[indice].charAt(par.getInfos().get(4).split(";")[indice].length()-1)!=p2)
						ss=par.getInfos().get(4).split(";")[indice].split(":")[1].split(Parametres.separateur_tiret_slash);
					else
						ss=new String[]{};
					for(String chaine:ss)
						va.addElement(new Annonce(chaine));
				}
				ajouterBoutonJeu(Revoir_chien,Revoir_chien,((PartieTarot)par).getContrat().force()<3);
			}
			for(Annonce annonce:va)
			{
				ajouterBoutonJeu(annonce.toString(),annonce.toString(),annonces.contains(annonce));
			}
		}
		else
		{
			if(par instanceof PartieBelote)
			{
				ajouterBoutonJeu(Annonce.belote_rebelote,Annonce.belote_rebelote,((Annoncable)par).autorise_annonce(new Annonce(Annonce.belote_rebelote),(byte)0));
			}
			ajouterBoutonJeu(Revoir_pli_precedent,Revoir_pli_precedent,true);
		}
		pack();
	}
	private void placerBoutonsFinPliUtilisateur()
	{
		Main main_utilisateur=par.getDistribution().main();
		if(main_utilisateur.estVide())
		{
			((Levable)par).ajouterPliEnCours();
		}
		if(par instanceof PartieTarot)
		{
			if(main_utilisateur.estVide()&&((Levable)par).getPliEnCours().contient(new CarteTarot((byte)1,(byte)1)))
			{/*Le Petit est mene au bout*/
				Vector<Annonce> va=new Vector<Annonce>();
				va.addElement(new Annonce(Annonce.petit_au_bout));
				((Levable)par).ajouterAnnonces(((Levable)par).getRamasseur(),va);
			}
			else if(main_utilisateur.total()==1&&((Levable)par).getPliEnCours().contient(new CarteTarot((byte)1,(byte)1)))
			{
				Vector<Byte> partenaires=((PartieTarot)par).coequipiers(((Levable)par).getRamasseur());
				boolean possedeExcuseMemeEquipe=false;
				for(byte partenaire:partenaires)
					possedeExcuseMemeEquipe|=par.getDistribution().main(partenaire).contient(new CarteTarot((byte)0));
				if(par.getDistribution().main(((Levable)par).getRamasseur()).contient(new CarteTarot((byte)0))||possedeExcuseMemeEquipe)
				{
					if(!((PartieTarot)par).adversaireAFaitPlis(((Levable)par).getRamasseur()))
					{
						Vector<Annonce> va=new Vector<Annonce>();
						va.addElement(new Annonce(Annonce.petit_au_bout));
						((Levable)par).ajouterAnnonces(((Levable)par).getRamasseur(),va);
					}
				}
			}
		}
		else if(main_utilisateur.estVide())
		{
			Vector<Annonce> va=new Vector<Annonce>();
			va.addElement(new Annonce(Annonce.dix_de_der));
			((Levable)par).ajouterAnnonces(((Levable)par).getRamasseur(),va);
		}
		if(main_utilisateur.estVide())
			ajouterBoutonJeu(Boutons.Fin_de_partie.toString(),Boutons.Fin_de_partie.toString(),true);
		else
			ajouterBoutonJeu(Boutons.Pli_suivant.toString(),Boutons.Pli_suivant.toString(),true);
		pack();
	}
	private void pause()
	{
		if(passe)
		{
			pause=!pause;
		}
		for(;pause;)
		{
			if(!passe)
			{
				pause=!pause;
			}
		}
	}
	private void placerBoutonsAppel()
	{
		par.setEtat(Etat.Appel);
		if(par.getNombreDeJoueurs()==4?par.getInfos().get(8).contains("roi"):par.getInfos().lastElement().contains("roi"))
		{
			if(((MainTarot)par.getDistribution().main()).tailleRois()<4)
			{
				for(Couleur couleur:Couleur.values())
				{
					if(couleur!=Couleur.Atout)
					{
						ajouterBoutonJeu(couleur.toString(),"appel-14-"+(couleur.ordinal()+1),true);
					}
				}
			}
			else if(((MainTarot)par.getDistribution().main()).tailleDames()<4)
			{
				for(Couleur couleur:Couleur.values())
				{
					if(couleur!=Couleur.Atout)
					{
						ajouterBoutonJeu(couleur.toString(),"appel-13-"+(couleur.ordinal()+1),true);
					}
				}
			}
			else if(((MainTarot)par.getDistribution().main()).tailleCavaliers()<4)
			{
				for(Couleur couleur:Couleur.values())
				{
					if(couleur!=Couleur.Atout)
					{
						ajouterBoutonJeu(couleur.toString(),"appel-12-"+(couleur.ordinal()+1),true);
					}
				}
			}
			else if(((MainTarot)par.getDistribution().main()).tailleValets()<4)
			{
				for(Couleur couleur:Couleur.values())
				{
					if(couleur!=Couleur.Atout)
					{
						ajouterBoutonJeu(couleur.toString(),"appel-11-"+(couleur.ordinal()+1),true);
					}
				}
			}
			else
			{
				ajouterBoutonJeu("Petit","appel-1-1",true);
				ajouterBoutonJeu("21","appel-21-1",true);
				ajouterBoutonJeu("Excuse","appel-8-0",true);
			}
		}
		else
		{
			for(byte valeur=11;valeur<15;valeur++)
				for(byte couleur=2;couleur<6;couleur++)
					ajouterBoutonJeu(new CarteTarot(valeur,couleur).toString(),"appel-"+valeur+"-"+couleur,true);
			ajouterBoutonJeu("Petit","appel-1-1",true);
			ajouterBoutonJeu("21","appel-21-1",true);
			ajouterBoutonJeu("Excuse","appel-8-0",true);
		}
	}
	private class AnimationContrat extends Thread{
		private Vector<String> pseudos;
		private byte donneur;
		private byte debut;
		private AnimationContrat(Vector<String> ppseudos,byte pdonneur,byte pdebut)
		{
			pseudos=ppseudos;
			donneur=pdonneur;
			debut=pdebut;
		}
		public void run()
		{
			thread_anime=true;
			Container container=getContentPane();
			JTextArea jta=(JTextArea)((JScrollPane)((JPanel)container.getComponent(3)).getComponent(0)).getViewport().getComponent(0);
			String[] raison=new String[1];
			int indice;
			boolean utilisateur_parle=false;
			Contrat contrat;
			long delai_contrat;
			String Pli_suivant=Boutons.Pli_suivant.toString();
			String Passe_au_jeu_de_la_carte=Boutons.Passe_au_jeu_de_la_carte.toString();
			String Fin_de_partie=Boutons.Fin_de_partie.toString();
			if(par instanceof PartieBelote)
			{
				contrat=new Contrat(EncheresBelote.Passe);
				delai_contrat=Long.parseLong(param.getInfos().get(0).get(1).split(":")[1]);
				if(!((PartieBelote)par).getFinEnchere()&&!((PartieBelote)par).getFinEncherePremierTour())
				{/*Debut premier tour de contrat commencant par l'entameur jusqu'a l'utilisateur*/
					((PartieBelote)par).setContrat(contrat);
					//Les eventuels joueurs places avant l'utilisateur commencent a parler
					//Activer le menu Partie/Pause
					getJMenuBar().getMenu(1).getItem(1).setEnabled(true);
					for(indice=debut;indice!=0&&contrat.force()<((Levable)par).max_contrat();indice=miseAjour(indice, 4))
					{//Les "robots" precedant l'utilisateur annoncent leur contrat
						try
						{
							Thread.sleep(delai_contrat);
						}
						catch(Exception exc)
						{
							exc.printStackTrace();
						}
						contrat=((PartieBelote)par).strategieContrat(raison);
						if(!contrat.equals(new Contrat(EncheresBelote.Couleur)))
							jta.append(pseudos.get(indice)+":"+contrat.toString()+st);
						else
							jta.append(pseudos.get(indice)+":"+par.getDistribution().derniereMain().carte(0).toString().split(" ")[2]+st);
						((PartieBelote)par).ajouterContrat(contrat);
						ajouterTexteDansPanneau(!contrat.equals(new Contrat(EncheresBelote.Couleur))?contrat.toString():par.getDistribution().derniereMain().carte(0).toString().split(" ")[2],(byte)indice);
						if(contrat.force()>0)
						{
							((PartieBelote)par).setContrat(contrat);
							((PartieBelote)par).setPreneur((byte)indice);
						}
						pause();
					}
					//Desactiver le menu Partie/Pause
					getJMenuBar().getMenu(1).getItem(1).setEnabled(false);
					((PartieBelote)par).finEnchere();
				}
				else if(!((PartieBelote)par).getFinEncherePremierTour())
				{/*Fin premier tour de contrat commencant par le joueur situe apres l'utilisateur et debut deuxieme tour de contrat jusqu'a l'utilisateur*/
					contrat=contrat_utilisateur;
					if(contrat!=null)
					{
						ajouterTexteDansPanneau(!contrat.equals(new Contrat(EncheresBelote.Couleur))?contrat.toString():par.getDistribution().derniereMain().carte(0).toString().split(" ")[2],(byte)0);
						((PartieBelote)par).ajouterContrat(contrat);
					}
					//Activer le menu Partie/Pause
					getJMenuBar().getMenu(1).getItem(1).setEnabled(true);
					/*Si l'utilisateur a demande le plus grand contrat possible*/
					if(contrat!=null&&contrat.force()==((Levable)par).max_contrat())
					{
						((PartieBelote)par).setPreneur((byte) 0);
						((PartieBelote)par).setContrat(contrat);
					}
					for(indice=debut;indice<donneur+1&&contrat!=null&&contrat.force()<((Levable)par).max_contrat();indice++)
					{
						try
						{
							Thread.sleep(delai_contrat);
						}
						catch(Exception exc)
						{
							exc.printStackTrace();
						}
						contrat=((Levable)par).strategieContrat(raison);
						if(!contrat.equals(new Contrat(EncheresBelote.Couleur)))
							jta.append(pseudos.get(indice)+":"+contrat+st);
						else
							jta.append(pseudos.get(indice)+":"+par.getDistribution().derniereMain().carte(0).toString().split(" ")[2]+st);
						((PartieBelote)par).ajouterContrat(contrat);
						ajouterTexteDansPanneau(!contrat.equals(new Contrat(EncheresBelote.Couleur))?contrat.toString():par.getDistribution().derniereMain().carte(0).toString().split(" ")[2],(byte)indice);
						if(contrat.force()>0)
						{
							((Levable)par).setContrat(contrat);
							((Levable)par).setPreneur((byte)indice);
						}
						pause();
					}
					indice%=par.getNombreDeJoueurs();
					((PartieBelote)par).finEncherePremierTour();
					for(;indice!=0&&contrat!=null&&contrat.force()<((Levable)par).max_contrat();indice=miseAjour(indice, 4))
					{//Les "robots" precedant l'utilisateur annoncent leur contrat
						try
						{
							Thread.sleep(delai_contrat);
						}
						catch(Exception exc)
						{
							exc.printStackTrace();
						}
						contrat=((Levable)par).strategieContrat(raison);
						if(!contrat.equals(new Contrat(EncheresBelote.Autre_couleur)))
							jta.append(pseudos.get(indice)+":"+contrat.toString()+st);
						else
							jta.append(pseudos.get(indice)+":"+((PartieBelote)par).getCarteAppelee().toString().split(" ")[2]+st);
						((PartieBelote)par).ajouterContrat(contrat);
						ajouterTexteDansPanneau(!contrat.equals(new Contrat(EncheresBelote.Autre_couleur))?contrat.toString():((PartieBelote)par).getCarteAppelee().toString().split(" ")[2],(byte)indice);
						if(contrat.force()>0)
						{
							((Levable)par).setContrat(contrat);
							((Levable)par).setPreneur((byte)indice);
						}
						pause();
					}
					if(contrat!=null)
					{
						utilisateur_parle=contrat.force()<((Levable)par).max_contrat();
					}
					//Desactiver le menu Partie/Pause
					getJMenuBar().getMenu(1).getItem(1).setEnabled(false);
				}
				else if(charge&&((PartieBelote)par).taille_contrats()<par.getNombreDeJoueurs()+((par.getDistribution().getDonneur()==par.getNombreDeJoueurs()-1)?1:par.getNombreDeJoueurs()-par.getDistribution().getDonneur()))
				{/*Si le joueur qui est le donneur est le numero 3, alors l'utilisateur est l'entameur et on a, dans le sens trigonometrique:
				 		2
				 	3		1
				 		0
				 		et il faut que tout le monde ait parle pour le premier tour et que l'utilisateur n'ait pas parle pour le deuxieme tour
				 sinon on a, par exemple, dans le sens trigonometrique:
				 		2 donneur
				 	3		1
				 		0
				 		et il faut que par cet exemple, le joueur numero 3 ait parle pour les deux tours et les autres joueurs n'aient parle que pour le premier tour*/
					contrat=((PartieBelote)par).getContrat();
					//Activer le menu Partie/Pause
					getJMenuBar().getMenu(1).getItem(1).setEnabled(true);
					for(indice=debut;indice!=0&&contrat.force()<((Levable)par).max_contrat();indice=miseAjour(indice, 4))
					{
						try
						{
							Thread.sleep(delai_contrat);
						}
						catch(Exception exc)
						{
							exc.printStackTrace();
						}
						contrat=((Levable)par).strategieContrat(raison);
						if(!contrat.equals(new Contrat(EncheresBelote.Autre_couleur)))
							jta.append(pseudos.get(indice)+":"+contrat.toString()+st);
						else
							jta.append(pseudos.get(indice)+":"+((PartieBelote)par).getCarteAppelee().toString().split(" ")[2]+st);
						((PartieBelote)par).ajouterContrat(contrat);
						ajouterTexteDansPanneau(!contrat.equals(new Contrat(EncheresBelote.Autre_couleur))?contrat.toString():((PartieBelote)par).getCarteAppelee().toString().split(" ")[2],(byte)indice);
						if(contrat.force()>0)
						{
							((Levable)par).setContrat(contrat);
							((Levable)par).setPreneur((byte)indice);
						}
						pause();
					}
					//Desactiver le menu Partie/Pause
					getJMenuBar().getMenu(1).getItem(1).setEnabled(false);
					utilisateur_parle=true;
				}
				else
				{/*Fin deuxieme tour de contrat commencant par le joueur situe apres l'utilisateur jusqu'au donneur*/
					contrat=contrat_utilisateur;
					if(contrat!=null)
					{
						ajouterTexteDansPanneau(!contrat.equals(new Contrat(EncheresBelote.Autre_couleur))?contrat.toString():par.getDistribution().derniereMain().carte(0).toString().split(" ")[2],(byte)0);
						((PartieBelote)par).ajouterContrat(contrat);
					}
					//Activer le menu Partie/Pause
					getJMenuBar().getMenu(1).getItem(1).setEnabled(true);
					if(contrat!=null&&contrat.force()==((Levable)par).max_contrat())
					{
						((PartieBelote)par).setPreneur((byte) 0);
						((PartieBelote)par).setContrat(contrat);
					}
					for(indice=debut;indice<donneur+1&&contrat!=null&&contrat.force()<((Levable)par).max_contrat();indice++)
					{
						try
						{
							Thread.sleep(delai_contrat);
						}
						catch(Exception exc)
						{
							exc.printStackTrace();
						}
						contrat=((Levable)par).strategieContrat(raison);
						if(!contrat.equals(new Contrat(EncheresBelote.Autre_couleur)))
							jta.append(pseudos.get(indice)+":"+contrat.toString()+st);
						else
							jta.append(pseudos.get(indice)+":"+((PartieBelote)par).getCarteAppelee().toString().split(" ")[2]+st);
						ajouterTexteDansPanneau(!contrat.equals(new Contrat(EncheresBelote.Autre_couleur))?contrat.toString():((PartieBelote)par).getCarteAppelee().toString().split(" ")[2],(byte)indice);
						((PartieBelote)par).ajouterContrat(contrat);
						if(contrat.force()>0)
						{
							((Levable)par).setContrat(contrat);
							((Levable)par).setPreneur((byte)indice);
						}
						pause();
					}
					//Desactiver le menu Partie/Pause
					getJMenuBar().getMenu(1).getItem(1).setEnabled(false);
				}
				JPanel panneau=(JPanel)((JPanel)getContentPane().getComponent(3)).getComponent(1);
				panneau.removeAll();
				Donne donne=par.getDistribution();
				String passe_contrat=EncheresBelote.Passe.toString();
				String sans_atout=EncheresBelote.Sans_atout.toString();
				String tout_atout=EncheresBelote.Tout_atout.toString();
				Contrat contrat_def=((PartieBelote)par).getContrat();
				if(((PartieBelote)par).getFinEnchere()&&!((PartieBelote)par).getFinEncherePremierTour())
				{/*L'utilisateur annonce pour le premier tour un contrat*/
					if(((PartieBelote)par).getContrat().force()<((Levable)par).max_contrat())
					{
						ajouterBoutonJeu(panneau,passe_contrat,passe_contrat,true);
						String couleur=EncheresBelote.Couleur.toString();
						//L'utilisateur doit monter les encheres pour pouvoir prendre
						ajouterBoutonJeu(panneau,donne.derniereMain().carte(0).toString().split(" ")[2],couleur,new Contrat(couleur).estDemandable(contrat_def));
						//L'utilisateur doit monter les encheres pour pouvoir prendre
						if(((PartieBelote)par).avecSurContrat())
						{
							ajouterBoutonJeu(panneau,sans_atout,sans_atout,new Contrat(sans_atout).estDemandable(contrat_def));
							ajouterBoutonJeu(panneau,tout_atout,tout_atout,true);
						}
					}
					else
					{
						ajouterBoutonJeu(panneau,Passe_au_jeu_de_la_carte,Pli_suivant,true);
					}
				}
				else if(utilisateur_parle)
				{/*L'utilisateur annonce pour le deuxieme tour un contrat*/
					byte couleur=((CarteGraphique)((JPanel)((JPanel)container.getComponent(1)).getComponent(4)).getComponent(0)).getCarte().couleur();
					if(((PartieBelote)par).getContrat().force()<((Levable)par).max_contrat())
					{
						ajouterBoutonJeu(panneau,passe_contrat,passe_contrat,true);
						String Autre_couleur=EncheresBelote.Autre_couleur.toString();
						for(Couleur coul:Couleur.values())
						{
							if(coul!=Couleur.Atout&&!Carte.chaine_couleur(couleur).equals(coul.toString()))
							{//On recupere les couleurs autre que celle proposee
								ajouterBoutonJeu(panneau,coul.toString(),Autre_couleur+coul.position(),new Contrat(Autre_couleur).estDemandable(contrat_def));
							}
						}
						if(((PartieBelote)par).avecSurContrat())
						{
							ajouterBoutonJeu(panneau,sans_atout,sans_atout,new Contrat(sans_atout).estDemandable(contrat_def));
							ajouterBoutonJeu(panneau,tout_atout,tout_atout,true);
						}
					}
					else
					{
						ajouterBoutonJeu(panneau,Passe_au_jeu_de_la_carte,Pli_suivant,true);
					}
				}
				else
				{/*On passe au jeu de la carte s'il existe un preneur, a la fin de la partie sinon*/
					if(((PartieBelote)par).getContrat().equals(new Contrat(EncheresBelote.Passe)))
					{
						ajouterBoutonJeu(panneau,Fin_de_partie,Fin_de_partie,true);
					}
					else
					{
						ajouterBoutonJeu(panneau,Passe_au_jeu_de_la_carte,Pli_suivant,true);
					}
				}
			}
			else//Tarot
			{
				contrat=new Contrat(EncheresTarot.Passe);
				JPanel panneau=(JPanel)((JPanel)getContentPane().getComponent(3)).getComponent(1);
				panneau.removeAll();
				delai_contrat=Long.parseLong(param.getInfos().get(0).get(4).split(":")[1]);
				String chelem=Contrat.chelem;
				if(!((PartieTarot)par).getFinEnchere())
				{
					((Levable)par).setContrat(contrat);
					//Activer le menu Partie/Pause
					getJMenuBar().getMenu(1).getItem(1).setEnabled(true);
					for(indice=debut;indice!=0&&contrat.force()<((Levable)par).max_contrat();indice=miseAjour(indice,par.getNombreDeJoueurs()))
					{
						try
						{
							Thread.sleep(delai_contrat);
						}
						catch(Exception exc)
						{
							exc.printStackTrace();
						}
						contrat=((Levable)par).strategieContrat(raison);
						jta.append(pseudos.get(indice)+":"+contrat+st);
						ajouterTexteDansPanneau(contrat.toString(),(byte)indice);
						if(contrat.force()>0)
						{
							((Levable)par).setContrat(contrat);
							((Levable)par).setPreneur((byte)indice);
						}
						((PartieTarot)par).ajouterContrat(contrat);
						pause();
					}
					charge=false;
					//Desactiver le menu Partie/Pause
					getJMenuBar().getMenu(1).getItem(1).setEnabled(false);
					if(contrat.force()==((Levable)par).max_contrat())
					{
						if(par.getNombreDeJoueurs()==3||par.getNombreDeJoueurs()==4&&par.getInfos().get(8).contains("sans")||par.getNombreDeJoueurs()==5&&par.getInfos().lastElement().contains("1"))
						{
							if(par.getNombreDeJoueurs()==4&&par.getInfos().get(8).contains("sans"))
							{
								((PartieTarot)par).setAppele((byte)((indice+2)%par.getNombreDeJoueurs()));
							}
						}
						else
						{
							((Levable)par).setCarteAppelee(((PartieTarot)par).strategieAppel(raison));
							ajouterTexteDansZone(pseudos.get(indice)+":"+((Levable)par).getCarteAppelee()+st);
							byte appele=((PartieTarot)par).joueurAyantCarteAppelee();
							if(((Levable)par).getPreneur()!=appele)
							{
								((PartieTarot)par).setAppele(appele);
								((PartieTarot)par).faireConfiance(appele,((Levable)par).getPreneur());
							}
						}
						par.setEtat(Etat.Avant_Jeu2);
						ajouterBoutonJeu(panneau,Passe_au_jeu_de_la_carte,Pli_suivant,true);
						((Levable)par).setEntameur(((Levable)par).getPreneur());
						((Levable)par).setPliEnCours();
						for(Carte carte:par.getDistribution().derniereMain())
						{
							((Levable)par).ajouterUneCarteDansPliEnCours(carte);
						}
						((PartieTarot)par).getPlisDefense().addElement(((Levable)par).getPliEnCours());
						thread_anime=false;
						pack();
						return;
					}
					((PartieTarot)par).finEnchere();
					utilisateur_parle=true;
				}
				else
				{
					if(!charge)
					{
						contrat=contrat_utilisateur;
						ajouterTexteDansPanneau(contrat.toString(),(byte)0);
						((PartieTarot)par).ajouterContrat(contrat);
					}
					else
					{
						if(par.getNombreDeJoueurs()-par.getDistribution().getDonneur()-1<((PartieTarot)par).contrats())
						{
							ajouterTexteDansPanneau(((PartieTarot)par).contrat(par.getNombreDeJoueurs()-par.getDistribution().getDonneur()-1).toString(),(byte)0);
						}
						charge=false;
					}
					//Activer le menu Partie/Pause
					getJMenuBar().getMenu(1).getItem(1).setEnabled(true);
					for(indice=debut;indice<=par.getDistribution().getDonneur()&&contrat.force()<((Levable)par).max_contrat();indice++)
					{
						try
						{
							Thread.sleep(delai_contrat);
						}
						catch(Exception exc)
						{
							exc.printStackTrace();
						}
						contrat=((Levable)par).strategieContrat(raison);
						ajouterTexteDansZone(pseudos.get(indice)+":"+contrat+st);
						ajouterTexteDansPanneau(contrat.toString(),(byte)indice);
						((PartieTarot)par).ajouterContrat(contrat);
						if(contrat.force()>0)
						{
							((Levable)par).setContrat(contrat);
							((Levable)par).setPreneur((byte)indice);
						}
						pause();
					}
					//Desactiver le menu Partie/Pause
					getJMenuBar().getMenu(1).getItem(1).setEnabled(false);
				}
				if(utilisateur_parle)
				{
					for(EncheresTarot enchere:EncheresTarot.values())
					{
						ajouterBoutonJeu(panneau,enchere.toString(),enchere.toString(),new Contrat(enchere).estDemandable(((Levable)par).getContrat()));
					}
					if(((PartieTarot)par).chelemContrat())
					{
						ajouterBoutonJeu(panneau,chelem,chelem,true);
					}
				}
				else/*Tout le monde a parle*/
				{
					if(((Levable)par).getContrat().equals(new Contrat(EncheresTarot.Passe)))
					{
						if(((PartieTarot)par).pas_jeu_apres_passe())
						{
							ajouterBoutonJeu(Fin_de_partie,Fin_de_partie,true);
						}
						else
						{
							par.setEtat(Etat.Avant_Jeu2);
							ajouterBoutonJeu(Passe_au_jeu_de_la_carte,Pli_suivant,true);
						}
					}
					else if(par.getNombreDeJoueurs()==4)
					{
						if(par.getInfos().get(8).contains("sans")||par.getInfos().get(8).contains("1"))
						{
							if(par.getInfos().get(8).contains("sans"))
							{
								indice=((Levable)par).getPreneur();
								((PartieTarot)par).setAppele((byte)((indice+2)%par.getNombreDeJoueurs()));
								((PartieTarot)par).faireConfiance((byte)((indice+2)%par.getNombreDeJoueurs()),(byte)indice);
								((PartieTarot)par).faireConfiance((byte)indice,(byte)((indice+2)%par.getNombreDeJoueurs()));
								((PartieTarot)par).faireConfiance((byte)((indice+3)%par.getNombreDeJoueurs()),(byte)((indice+1)%par.getNombreDeJoueurs()));
								((PartieTarot)par).faireConfiance((byte)((indice+1)%par.getNombreDeJoueurs()),(byte)((indice+3)%par.getNombreDeJoueurs()));
							}
							cas_sans_appel();
						}
						else
						{
							cas_avec_appel();
						}
					}
					else if(par.getNombreDeJoueurs()==5)
					{
						if(par.getInfos().lastElement().contains("1"))
						{
							cas_sans_appel();
						}
						else
						{
							cas_avec_appel();
						}
					}
					else
					{
						cas_sans_appel();
					}
				}
			}
			pack();
			thread_anime=false;
		}
		private void cas_sans_appel()
		{
			String Pli_suivant=Boutons.Pli_suivant.toString();
			String Passe_au_jeu_de_la_carte=Boutons.Passe_au_jeu_de_la_carte.toString();
			String Voir_chien=Boutons.Voir_le_chien.toString();
			if(((Levable)par).getContrat().force()<3)
			{
				ajouterBoutonJeu(Voir_chien,Voir_chien,true);
				par.setEtat(Etat.Avant_Ecart);
			}
			else
			{
				String chelem=Contrat.chelem;
				((Levable)par).setEntameur(((Levable)par).getPreneur());
				((Levable)par).setPliEnCours();
				for(Carte carte:par.getDistribution().derniereMain())
				{
					((Levable)par).ajouterUneCarteDansPliEnCours(carte);
				}
				if(((Levable)par).getContrat().force()<4)
				{
					((PartieTarot)par).getPlisAttaque().addElement(((Levable)par).getPliEnCours());
				}
				else
				{
					((PartieTarot)par).getPlisDefense().addElement(((Levable)par).getPliEnCours());
				}
				if(((Levable)par).getPreneur()==0&&!((Levable)par).getContrat().equals(new Contrat(chelem)))
				{
					ajouterBoutonJeu(chelem,Boutons._Chelem.name(),true);
					par.setEtat(Etat.Avant_Jeu);
				}
				else
				{
					par.setEtat(Etat.Avant_Jeu2);
				}
				ajouterBoutonJeu(Passe_au_jeu_de_la_carte,Pli_suivant,true);
			}
		}
		private void cas_avec_appel()
		{
			String Pli_suivant=Boutons.Pli_suivant.toString();
			String Passe_au_jeu_de_la_carte=Boutons.Passe_au_jeu_de_la_carte.toString();
			String Voir_chien=Boutons.Voir_le_chien.toString();
			if(((Levable)par).getPreneur()==0)
			{
				placerBoutonsAppel();
			}
			else
			{
				((Levable)par).setCarteAppelee(((PartieTarot)par).strategieAppel(new String[]{ch_v}));
				ajouterTexteDansPanneau(((Levable)par).getCarteAppelee().toString(),((Levable)par).getPreneur());
				ajouterTexteDansZone(pseudos.get(((Levable)par).getPreneur())+":"+((Levable)par).getCarteAppelee()+st);
				byte appele=((PartieTarot)par).joueurAyantCarteAppelee();
				if(((Levable)par).getPreneur()!=appele)
				{
					((PartieTarot)par).setAppele(appele);
					((PartieTarot)par).faireConfiance(appele,((Levable)par).getPreneur());
				}
				if(((Levable)par).getContrat().force()<3)
				{
					ajouterBoutonJeu(Voir_chien,Voir_chien,true);
					par.setEtat(Etat.Avant_Ecart);
				}
				else
				{
					((Levable)par).setEntameur(((Levable)par).getPreneur());
					((Levable)par).setPliEnCours();
					for(Carte carte:par.getDistribution().derniereMain())
					{
						((Levable)par).ajouterUneCarteDansPliEnCours(carte);
					}
					if(((Levable)par).getContrat().force()<4)
					{
						((PartieTarot)par).getPlisAttaque().addElement(((Levable)par).getPliEnCours());
					}
					else
					{
						((PartieTarot)par).getPlisDefense().addElement(((Levable)par).getPliEnCours());
					}
					par.setEtat(Etat.Avant_Jeu2);
					ajouterBoutonJeu(Passe_au_jeu_de_la_carte,Pli_suivant,true);
				}
			}
		}
	}
	private class FinPliCarte extends Thread{
		private boolean premierTour;
		private FinPliCarte(boolean ppremierTour)
		{
			premierTour=ppremierTour;
		}
		public void run()
		{
			//Activer le sous-menu conseil
			getJMenuBar().getMenu(1).getItem(0).setEnabled(true);
			long delai_pli=par instanceof PartieTarot?Long.parseLong(param.getInfos().get(0).get(6).split(":")[1]):Long.parseLong(param.getInfos().get(0).get(3).split(":")[1]);
			try {
				Thread.sleep(delai_pli);//Le joueur reflechit pendant 0.5 s
			} catch (Exception exc) {
				exc.printStackTrace();
			}
			((Tapis)getContentPane().getComponent(1)).setCartes(par.getNombreDeJoueurs(),par.jeu());
			thread_anime=false;
			placerBoutonsAvantJeuUtilisateur(premierTour);
			pack();
		}
	}
	private class AnimationCarte extends Thread{
		private byte debut;
		private byte fin;
		private Vector<String> pseudos;
		private boolean premierTour;
		private AnimationCarte(byte pdebut,byte pfin,Vector<String> ppseudos,boolean ppremierTour)
		{
			debut=pdebut;
			fin=pfin;
			pseudos=ppseudos;
			premierTour=ppremierTour;
		}
		private AnimationCarte(byte pdebut,Vector<String> ppseudos,boolean ppremierTour)
		{
			debut=pdebut;
			fin=par.getNombreDeJoueurs();
			pseudos=ppseudos;
			premierTour=ppremierTour;
		}
		public void run()
		{
			thread_anime=true;
			long delai_carte;
			if(par instanceof PartieBelote)
			{
				delai_carte=Long.parseLong(param.getInfos().get(0).get(2).split(":")[1]);
			}
			else
			{
				delai_carte=Long.parseLong(param.getInfos().get(0).get(5).split(":")[1]);
			}
			if(((Levable)par).getEntameur()==debut)
			{
				long delai_pli;
				if(par instanceof PartieBelote)
				{
					if(param.getInfos().get(0).get(7).split(";")[0].endsWith(Reponse.non.toString()))
					{
						delai_pli=Long.parseLong(param.getInfos().get(0).get(3).split(":")[1]);
						try {
							sleep(delai_pli);//Le joueur reflechit pendant 0.5 s
						} catch (Exception exc) {
							exc.printStackTrace();
						}
						((Tapis)getContentPane().getComponent(1)).setCartes(par.getNombreDeJoueurs(),par.jeu());
					}
				}
				else
				{
					if(param.getInfos().get(0).get(7).split(";")[1].endsWith(Reponse.non.toString()))
					{
						delai_pli=Long.parseLong(param.getInfos().get(0).get(6).split(":")[1]);
						try {
							sleep(delai_pli);//Le joueur reflechit pendant 0.5 s
						} catch (Exception exc) {
							exc.printStackTrace();
						}
						par.setEtat(Etat.Jeu);
						((Tapis)getContentPane().getComponent(1)).setEcart(par.getDistribution().derniereMain(),par.jeu());
						((Tapis)getContentPane().getComponent(1)).setCartes(par.getNombreDeJoueurs(),par.jeu());
					}
				}
			}
			//Activer le menu Partie/Pause
			getJMenuBar().getMenu(1).getItem(1).setEnabled(true);
			for(byte joueur=debut;joueur<fin;joueur++)
			{
				try {
					sleep(delai_carte);//Le joueur reflechit pendant 0.5 s
				} catch (Exception exc) {
					exc.printStackTrace();
				}
				jouer(joueur,pseudos.get(joueur),premierTour);
				pause();
			}
			//Desactiver le menu Partie/Pause
			getJMenuBar().getMenu(1).getItem(1).setEnabled(false);
			if(debutPli)
			{
				thread_anime=false;
				placerBoutonsAvantJeuUtilisateur(premierTour);
			}
			else
			{
				if(par instanceof PartieBelote)
				{
					if(param.getInfos().get(0).get(7).split(";")[0].endsWith(Reponse.non.toString()))
					{
						if(par.getDistribution().main().estVide())
						{
							((Levable)par).ajouterPliEnCours();
							Vector<Annonce> va=new Vector<Annonce>();
							va.addElement(new Annonce(Annonce.dix_de_der));
							((Levable)par).ajouterAnnonces(((Levable)par).getRamasseur(),va);
							finPartie();
						}
						else
						{
							debutPli();
						}
					}
					else
					{
						thread_anime=false;
						placerBoutonsFinPliUtilisateur();
					}
				}
				else
				{
					if(param.getInfos().get(0).get(7).split(";")[1].endsWith(Reponse.non.toString()))
					{
						if(par.getDistribution().main().total()==1&&((Levable)par).getPliEnCours().contient(new CarteTarot((byte)1,(byte)1)))
						{
							Vector<Byte> partenaires=((PartieTarot)par).coequipiers(((Levable)par).getRamasseur());
							boolean possedeExcuseMemeEquipe=false;
							for(byte partenaire:partenaires)
								possedeExcuseMemeEquipe|=par.getDistribution().main(partenaire).contient(new CarteTarot((byte)0));
							if(par.getDistribution().main(((Levable)par).getRamasseur()).contient(new CarteTarot((byte)0))||possedeExcuseMemeEquipe)
							{
								if(!((PartieTarot)par).adversaireAFaitPlis(((Levable)par).getRamasseur()))
								{
									Vector<Annonce> va=new Vector<Annonce>();
									va.addElement(new Annonce(Annonce.petit_au_bout));
									((Levable)par).ajouterAnnonces(((Levable)par).getRamasseur(),va);
								}
							}
						}
						if(par.getDistribution().main().estVide())
						{
							((Levable)par).ajouterPliEnCours();
							if(((Levable)par).getPliEnCours().contient(new CarteTarot((byte)1,(byte)1)))
							{/*Le Petit est mene au bout*/
								Vector<Annonce> va=new Vector<Annonce>();
								va.addElement(new Annonce(Annonce.petit_au_bout));
								((Levable)par).ajouterAnnonces(((Levable)par).getRamasseur(),va);
							}
							finPartie();
						}
						else
						{
							debutPli();
						}
					}
					else
					{
						thread_anime=false;
						placerBoutonsFinPliUtilisateur();
					}
				}
			}
			pack();
		}
	}
	private void jouer(byte joueur,String pseudo,boolean premierTour)
	{
		String[] raison=new String[1];	
		Carte ct=((Levable)par).strategieJeuCarteUnique(raison);
		if(premierTour)
		{
			if(par instanceof PartieTarot)
			{
				if(((PartieTarot)par).pas_jeu_misere()||((PartieTarot)par).getContrat()!=null)
				{
					Vector<Annonce> va=((PartieTarot)par).strategieAnnonces(joueur,raison);
					((Levable)par).setAnnonces(joueur,va);
					MainTarot poignee=((PartieTarot)par).strategiePoignee(joueur,raison);
					((PartieTarot)par).ajouterPoignee(poignee,joueur);
					for(Annonce annonce:va)
					{
						ajouterTexteDansZone(pseudo+":"+annonce+st);
						ajouterTexteDansPanneau(annonce.toString()+st,joueur);
					}
					if(!poignee.estVide())
					{
						int indexe=va.indexOf(new Annonce(Poignees.Poignee));
						if(indexe<0)
							indexe=va.indexOf(new Annonce(Poignees.Double_Poignee));
						if(indexe<0)
							indexe=va.indexOf(new Annonce(Poignees.Triple_Poignee));
						JPanel panneau1=new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
						for(byte indice=0;indice<poignee.total();indice++)
						{
							CarteGraphique carte=new CarteGraphique(poignee.carte(indice),JLabel.RIGHT,indice!=0);
							if(indice==0)
								carte.setPreferredSize(new Dimension(100,150));
							else
								carte.setPreferredSize(new Dimension(25,150));
							panneau1.add(carte);
						}
						JOptionPane.showMessageDialog(Fenetre.this,panneau1,pseudo+":"+va.get(indexe),JOptionPane.INFORMATION_MESSAGE);
					}
				}
			}
			else
			{
				Vector<Annonce> va=((PartieBelote)par).strategieAnnonces(joueur,raison,ct,premierTour);
				((Levable)par).setAnnonces(joueur,va);
				for(Annonce annonce:va)
				{
					ajouterTexteDansZone(pseudo+":"+annonce+st);
					ajouterTexteDansPanneau(annonce.toString()+st,joueur);
				}
			}
		}
		else if(par instanceof PartieBelote)
		{
			Vector<Annonce> va=((PartieBelote)par).strategieAnnonces(joueur,raison,ct,premierTour);
			((Levable)par).ajouterAnnonces(joueur,va);
			for(Annonce annonce:va)
			{
				ajouterTexteDansZone(pseudo+":"+annonce+st);
				ajouterTexteDansPanneau(annonce.toString()+st,joueur);
			}
		}
		if(par instanceof PartieTarot)
		{
			if(((PartieTarot)par).getCarteAppelee()!=null&&((PartieTarot)par).getCarteAppelee().equals(ct))
			{
				ajouterTexteDansPanneau(Statut.Appele.toString(),joueur);
				ajouterTexteDansZone(pseudo+":"+Statut.Appele.toString());
			}
		}
		par.getDistribution().main(joueur).jouer(ct);
		((Levable)par).ajouterUneCarteDansPliEnCours(ct);
		((Tapis)getContentPane().getComponent(1)).setCarte(joueur,par.getNombreDeJoueurs(),ct);
	}
	private class EcouteurCarte extends MouseAdapter {
		private Carte carte;
		private EcouteurCarte(Carte pcarte)
		{
			carte=pcarte;
		}
		public void mouseEntered(MouseEvent event)
		{
			if(!clic_carte())
			{
				carte_entree=true;
				carte_sortie=false;
				carte_survolee=carte;
				testEntreeSortie();
			}
		}
		public void mouseExited(MouseEvent event)
		{
			if(!clic_carte())
			{
				carte_sortie=true;
				carte_entree=false;
				carte_survolee=carte;
				testEntreeSortie();
			}
		}
		private void testEntreeSortie()
		{
			if(!thread_anime&&carte_sortie&&!carte_entree)
			{
				jeu_carte(carte_survolee);
			}
		}
		public void mouseReleased(MouseEvent event)
		{
			if(a_joue_carte&&clic_carte())
			{
				raison_courante=raisons[1];
				a_joue_carte=false;
			}
		}
		public void mousePressed(MouseEvent event)
		{
			if(clic_carte()&&carte!=null)
			{
				jeu_carte(carte);
			}
		}
		private boolean clic_carte()
		{
			Vector<String> infos=param.getInfos().get(0);
			return infos.get(infos.size()-2).split(";")[par.jeu().ordinal()].endsWith(Reponse.oui.toString());
		}
		private void jeu_carte(Carte carte)
		{
			String[] raison={ch_v};
			if(carte instanceof CarteTarot)
			{
				if(par.getEtat()==Etat.Jeu)
				{
					testAnimationCarte(carte);
				}
				else
				{
					if(par.getDistribution().main().contient(carte))
					{
						if(((PartieTarot)par).autorise_ecart_de(carte,raison))
						{
							MainTarot mt=(MainTarot)par.getDistribution().main();
							if(carte.couleur()==1&&PartieTarot.atoutsAvecExcuse(mt.couleurs())+mt.tailleRois()!=mt.total())
							{//On n'ecarte de l'atout que si aucune cartes basses ou figures differente du roi ne peut etre ecartee.
								JOptionPane.showMessageDialog(Fenetre.this,"Vous ne pouvez pas ecarter la carte suivante:"+st+carte+", car il faut ecarter d'autres cartes avant.","Erreur de jouerie",JOptionPane.ERROR_MESSAGE);
							}
							else
							{
								charge=false;
								ajouterUneCarteAuChien(carte);
							}
						}
						else
						{
							JOptionPane.showMessageDialog(Fenetre.this,"Vous ne pouvez pas ecarter la carte suivante:"+st+carte+" pour la raison suivante: "+raison[0],"Erreur de jouerie",JOptionPane.ERROR_MESSAGE);
						}
					}
					else
					{
						charge=false;
						retirerUneCarteDuChien(carte);
					}
				}
			}
			else
			{
				testAnimationCarte(carte);
			}
		}
		private void testAnimationCarte(Carte carte)
		{
			if(anim_carte!=null)
			{
				if(!anim_carte.isAlive())
				{
					verifierRegles(carte);
				}
				else
				{
					JOptionPane.showMessageDialog(Fenetre.this,"Attendez votre tour, sinon vous tricherez","Patience",JOptionPane.ERROR_MESSAGE);
				}
			}
			else
			{
				verifierRegles(carte);
			}
		}
		private void verifierRegles(Carte carte)
		{
			String[] raison={ch_v};
			if(raison_courante.equals(ch_v))
			{
				if(((Levable)par).autorise(carte, raison))
				{
					charge=false;
					a_joue_carte=true;
					finPli(carte);
				}
				else
				{
					JOptionPane.showMessageDialog(Fenetre.this,"Vous ne pouvez pas jouer la carte suivante:"+st+carte+"."+st+raison[0],"Erreur de jouerie",JOptionPane.ERROR_MESSAGE);
				}
			}
			else
			{
				JOptionPane.showMessageDialog(Fenetre.this,"Vous ne pouvez pas jouer car "+raison_courante,"Abus de jeu",JOptionPane.ERROR_MESSAGE);
			}
		}

	}
	private class EcouteurBoutonJeu implements ActionListener {
		private String texte;
		private EcouteurBoutonJeu(String texteBouton) {
			texte=texteBouton;
		}
		public void actionPerformed(ActionEvent event) {
			charge=false;
			String Revoir_pli_precedent=Boutons.Revoir_le_pli_precedent.toString();
			String Pli_suivant=Boutons.Pli_suivant.toString();
			String Passe_au_jeu_de_la_carte=Boutons.Passe_au_jeu_de_la_carte.toString();
			String Revenir_au_pli_actuel=Boutons.Revenir_au_pli_actuel.toString();
			String Fin_de_partie=Boutons.Fin_de_partie.toString();
			String Revoir_chien=Boutons.Revoir_le_chien.toString();
			String Voir_chien=Boutons.Voir_le_chien.toString();
			String Prendre_les_cartes_du_chien=Boutons.Prendre_les_cartes_du_chien.toString();
			String Valider_chien=Boutons.Valider_le_chien.toString();
			if(par instanceof PartieBelote)
			{
				if(new Contrat(texte).force()>0||texte.equals(EncheresBelote.Passe.toString())||texte.startsWith(EncheresBelote.Autre_couleur.toString()))
				{
					synchronized (Fenetre.this) {
						if(texte.startsWith(EncheresBelote.Autre_couleur.toString()))
						{
							((Levable)par).setCarteAppelee(new CarteBelote((byte)11,Byte.parseByte(texte.substring(texte.length()-1))));
							texte=EncheresBelote.Autre_couleur.toString();
						}
						else if(texte.equals(EncheresBelote.Couleur.toString()))
						{
							((Levable)par).setCarteAppelee(new CarteBelote((byte)11,par.getDistribution().derniereMain().carte(0).couleur()));
						}
						contrat_utilisateur=new Contrat(texte);
					}
					anim_contrat=new AnimationContrat(pseudos(),par.getDistribution().getDonneur(),(byte)1);
					anim_contrat.start();
				}
				else if(texte.equals(Revoir_pli_precedent))
				{
					if(!thread_anime)
					{
						pliPrecedent();
					}
				}
				else if(texte.equals(Pli_suivant))
				{
					par.setEtat(Etat.Jeu);
					((Tapis)getContentPane().getComponent(1)).setCartes(par.getNombreDeJoueurs(),Jeu.Belote);
					debutPli();
				}
				else if(texte.equals(Revenir_au_pli_actuel))
				{
					revenirAuPliActuel();
				}
				else if(texte.equals(Fin_de_partie))
				{
					finPartie();
				}
				else
				{
					if(texte.equals(Annonce.belote_rebelote))
					{
						CarteBelote carte;
						if(((Levable)par).getAnnonces((byte)0).contains(new Annonce(Annonce.belote_rebelote)))
						{
							byte couleur=((Levable)par).getCarteAppelee().couleur();
							int pos=par.getDistribution().main().position(new CarteBelote((byte)14,couleur));
							if(pos<0)
							{
								pos=par.getDistribution().main().position(new CarteBelote((byte)13,couleur));
							}
							carte=(CarteBelote)par.getDistribution().main().carte(pos);
							ajouterTexteDansZone(pseudo()+":"+texte+st);
							Vector<Annonce> an = new Vector<Annonce>();
							an.addElement(new Annonce(Annonce.belote_rebelote));
							((Levable)par).ajouterAnnonces((byte)0,an);
							finPli(carte);
						}
						else
						{
							DialogueCartes dc=new DialogueCartes("Choisissez votre carte pour jouer pour l'annonce "+Annonce.belote_rebelote,Fenetre.this);
							dc.setDialogue();
							carte=dc.getCarte();
							if(carte!=null)
							{
								ajouterTexteDansZone(pseudo()+":"+texte+st);
								Vector<Annonce> an = new Vector<Annonce>();
								an.addElement(new Annonce(Annonce.belote_rebelote));
								((Levable)par).ajouterAnnonces((byte)0,an);
								finPli(carte);							
							}
						}
					}
					else
					{
						annonce(new Annonce(texte));
					}
				}
			}
			else
			{
				if(texte.equals(EncheresTarot.Passe.toString()))
				{
					synchronized (Fenetre.this) {
						contrat_utilisateur=new Contrat(texte);
					}
					anim_contrat=new AnimationContrat(pseudos(),par.getDistribution().getDonneur(),(byte)1);
					anim_contrat.start();
				}
				else if(new Contrat(texte).force()>0)
				{
					synchronized (Fenetre.this) {
						contrat_utilisateur=new Contrat(texte);
						((Levable)par).setPreneur((byte)0);
						((Levable)par).setContrat(contrat_utilisateur);
					}
					anim_contrat=new AnimationContrat(pseudos(),par.getDistribution().getDonneur(),(byte)1);
					anim_contrat.start();
				}
				else if(texte.contains("-"))
				{
					String[] carte=texte.split("-");
					byte couleur=Byte.parseByte(carte[2]);
					byte valeur=Byte.parseByte(carte[1]);
					((Levable)par).setCarteAppelee(new CarteTarot(valeur,couleur));
					ajouterTexteDansPanneau(((Levable)par).getCarteAppelee().toString(),(byte)0);
					ajouterTexteDansZone(pseudo()+":"+((Levable)par).getCarteAppelee()+st);
					byte appele=((PartieTarot)par).joueurAyantCarteAppelee();
					if(appele!=0)
					{
						((PartieTarot)par).setAppele(appele);
						((PartieTarot)par).faireConfiance(appele,(byte)0);
					}
					if(((Levable)par).getContrat().force()<3)
					{
						voirChien();
					}
					else
					{
						((Levable)par).setEntameur((byte)0);
						((Levable)par).setPliEnCours();
						for(Carte carte_chien:par.getDistribution().derniereMain())
						{
							((Levable)par).ajouterUneCarteDansPliEnCours(carte_chien);
						}
						if(((Levable)par).getContrat().force()<4)
							((PartieTarot)par).getPlisAttaque().addElement(((Levable)par).getPliEnCours());
						else
							((PartieTarot)par).getPlisDefense().addElement(((Levable)par).getPliEnCours());
						((JPanel)((JPanel)getContentPane().getComponent(3)).getComponent(1)).removeAll();
						String chelem=Contrat.chelem;
						if(!((Levable)par).getContrat().equals(new Contrat(chelem)))
						{
							ajouterBoutonJeu(chelem,Boutons._Chelem.name(),true);
							par.setEtat(Etat.Avant_Jeu);
							ajouterBoutonJeu(Passe_au_jeu_de_la_carte,Pli_suivant,true);
							pack();
						}
						else
						{
							par.setEtat(Etat.Jeu);
							debutPli();
						}
					}
				}
				else if(texte.equals(Revoir_chien))
				{
					if(!thread_anime)
					{
						revoirChien();
					}
				}
				else if(texte.equals(Voir_chien))
				{
					voirChien();
				}
				else if(texte.equals(Prendre_les_cartes_du_chien))
				{
					prendreCartesChien();
				}
				else if(texte.equals(Valider_chien))
				{
					((Levable)par).getPlisAttaque().addElement(((Levable)par).getPliEnCours());
					((Tapis)getContentPane().getComponent(1)).setEcart(par.getDistribution().derniereMain(),par.jeu());
					((Tapis)getContentPane().getComponent(1)).setCartes(par.getNombreDeJoueurs(),par.jeu());
					par.setEtat(Etat.Jeu);
					debutPli();
				}
				else if(texte.equals(Fin_de_partie))
				{
					finPartie();
				}
				else if(texte.equals(Revenir_au_pli_actuel))
				{
					revenirAuPliActuel();
				}
				else if(texte.equals(Revoir_pli_precedent))
				{
					if(!thread_anime)
					{
						pliPrecedent();
					}
				}
				else if(texte.equals(Pli_suivant))
				{
					par.setEtat(Etat.Jeu);
					if(!premier_pli_fait)
					{
						((Tapis)getContentPane().getComponent(1)).setEcart(par.getDistribution().derniereMain(),par.jeu());
					}
					((Tapis)getContentPane().getComponent(1)).setCartes(par.getNombreDeJoueurs(),par.jeu());
					debutPli();
				}
				else
				{
					if(texte.equals(Boutons._Chelem.name()))
						texte=Primes.Chelem.toString();
					annonce(new Annonce(texte));
				}
			}
		}
	}
	/**Permet de charger une main de distribution
	 * a partir d'un fichier*/
	private Main chargerPile() throws Exception
	{
		Main pile;
		if(jeu==Jeu.Belote)
		{
			ObjectInputStream ois;
			File fe=new File(Fichier.dossier_paquets+File.separator+Jeu.Belote+Fichier.extension_paquet);
			ois=new ObjectInputStream(new BufferedInputStream(new FileInputStream(fe)));
			pile=(MainBelote)ois.readObject();
			ois.close();
		}
		else
		{
			ObjectInputStream ois;
			File fe=new File(Fichier.dossier_paquets+File.separator+Jeu.Tarot+Fichier.extension_paquet);
			ois=new ObjectInputStream(new BufferedInputStream(new FileInputStream(fe)));
			pile=(MainTarot)ois.readObject();
			ois.close();
		}
		return pile;
	}
	private long chargerNombreDeParties() throws Exception {
		long nb;
		File fe=new File(Fichier.dossier_paquets+File.separator+Fichier.fichier_paquet);
		if(jeu==Jeu.Belote)
		{
			BufferedReader br=new BufferedReader(new FileReader(fe));
			nb=Long.parseLong(br.readLine());
			br.close();
		}
		else
		{
			BufferedReader br=new BufferedReader(new FileReader(fe));
			String chaine=ch_v;
			for(byte indice=0;indice<2;indice++)
				chaine=br.readLine();
			nb=Long.parseLong(chaine);
			br.close();
		}
		return nb;
	}
	private static int miseAjour(int indice,int nbJ)
	{
		indice++;//Joueur suivant
		indice%=nbJ;//Si necessaire on remet i a 0 pour entamer une nouvelle boucle
		return indice;
	}
	private void editerBelote()
	{
		changer_pile_fin=false;
		mettre_en_place_ihm_belote();
	}
	private void placer_levable()
	{
		premier_pli_fait=false;
		enCoursDePartie=true;
		//Activer le menu Fichier/Sauvegarder
		getJMenuBar().getMenu(0).getItem(1).setEnabled(true);
		//Activer le menu Fichier/Changer de mode
		getJMenuBar().getMenu(0).getItem(3).setEnabled(true);
		//Activer les conseils
		getJMenuBar().getMenu(1).getItem(0).setEnabled(true);
		//Desactiver le menu Partie/Demo
		getJMenuBar().getMenu(1).getItem(4).setEnabled(false);
		placer_ihm_levable();
	}
	private void placer_ihm_levable()
	{
		Container container=new Container();
		container.setLayout(new BorderLayout());
		container.add(new JLabel("Pour plus d'aide, allez dans le menu aide",JLabel.CENTER),BorderLayout.NORTH);
		container.add(new Tapis(par.jeu(),par.getNombreDeJoueurs(),par.getInfos(),pseudos(),par.getDistribution().derniereMain().total()),BorderLayout.CENTER);
		JPanel panneau=new JPanel();
		panneau.setBackground(Color.BLUE);
		panneau.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
		container.add(panneau,BorderLayout.SOUTH);
		panneau=new JPanel(new GridLayout(0,1));
		JTextArea jta=new JTextArea(ch_v,8, 30);
		jta.setEditable(false);
		jta.setFocusable(false);
		panneau.add(new JScrollPane(jta));
		JPanel sous_panneau=new JPanel(new GridLayout(0,1));
		panneau.add(sous_panneau);
		container.add(panneau,BorderLayout.EAST);
		((Tapis)container.getComponent(1)).setTalon(par.getDistribution().derniereMain(),par.jeu());
		setContentPane(container);
	}
	private void mettre_en_place_ihm_belote()
	{
		byte donneur=-1;
		synchronized (this) {
			placer_levable();
			donneur=par.getDistribution().getDonneur();
			afficherMainUtilisateur(false);
			pack();
		}
		anim_contrat=new AnimationContrat(pseudos(),donneur,(byte)((donneur+1)%4));
		anim_contrat.start();
	}
	private void modifierJeuBelote()
	{
		enCoursDePartie=true;
		//Activer le menu Fichier/Sauvegarder
		getJMenuBar().getMenu(0).getItem(1).setEnabled(true);
		//Activer le menu Fichier/Changer de mode
		getJMenuBar().getMenu(0).getItem(3).setEnabled(true);
		//Activer les conseils
		getJMenuBar().getMenu(1).getItem(0).setEnabled(true);
		Pli.nombreTotal=0;
		MainBelote pile;
		/*Chargement de la pile de cartes depuis un fichier sinon on la cree*/
		try {
			pile = (MainBelote) chargerPile();
		} catch (Exception exc) {
			exc.printStackTrace();
			pile=new MainBelote();
			for (byte carte = 0; carte < 32; carte++) {
				pile.ajouter(new CarteBelote(carte));
			}
		}
		/*Chargement du nombre de parties jouees depuis le lancement du logiciel*/
		long nb;
		try {
			nb=chargerNombreDeParties();
		} catch (Exception exc) {
			exc.printStackTrace();
			nb=0;
		}
		Donne donne;
		if(nb==0||par==null)
		{
			donne=new Donne(mode,param.getInfos().get(Jeu.Belote.ordinal()+1),nb,pile);
			donne.initDonneur();
		}
		else
		{
			donne=new Donne(par.getMode(),param.getInfos().get(Jeu.Belote.ordinal()+1),nb,((PartieBelote)par).empiler());
			donne.donneurSuivant(par.getDistribution().getDonneur());
		}
		donne.initDonne();
		par=new PartieBelote(Type.Aleatoire,donne);
		mettre_en_place_ihm_belote();
	}
	private void debutPli()
	{
		byte donneur=par.getDistribution().getDonneur();
		//Activer le sous-menu conseil
		getJMenuBar().getMenu(1).getItem(0).setEnabled(true);
		//Activer le sous-menu aide au jeu
		getJMenuBar().getMenu(1).getItem(2).setEnabled(true);
		Vector<String> pseudos=pseudos();
		debutPli=true;
		/*Si on n'a pas encore fait de pli a la belote*/
		boolean premierTour=((Levable)par).premierTour();
		if(premierTour&&!premier_pli_fait)
		{
			if(par instanceof PartieBelote)
			{
				MainBelote talon=new MainBelote();
				talon.ajouterCartes(par.getDistribution().derniereMain());/*Copie du talon original pour donner des cartes aux joueurs*/
				par.getDistribution().main(((PartieBelote)par).getPreneur()).ajouter(talon.jouer(0));//Le preneur prend la carte du dessus
				for(int joueur=(donneur+1)%4;joueur!=donneur;joueur=miseAjour(joueur,4))
				{
					for(int indice=0;indice<2;indice++)
					{
						par.getDistribution().main((byte)joueur).ajouter(talon.jouer(0));
					}
					if(joueur!=((Levable)par).getPreneur())
					{
						par.getDistribution().main((byte)joueur).ajouter(talon.jouer(0));
					}
				}
				int nombreDeCartesRestantes=talon.total();
				//Le donneur prend les dernieres cartes
				for(int indice=0;indice<nombreDeCartesRestantes;indice++)
				{
					par.getDistribution().main(donneur).ajouter(talon.jouer(0));
				}
				/*L'entameur suit le donneur pour le premier pli*/
				((Levable)par).setEntameur((byte)((donneur+1)%4));
				((Tapis)getContentPane().getComponent(1)).supprimerCarteTalon();
				if(((Levable)par).getContrat().force()==1)
				{
					byte couleur_atout=((PartieBelote)par).couleur_atout();
					ajouterTexteDansZone(pseudos.get(((Levable)par).getPreneur())+":"+Carte.chaine_couleur(couleur_atout)+st);
					((Tapis)getContentPane().getComponent(1)).ajouterTexteDansPanneau(Carte.chaine_couleur(couleur_atout),((Levable)par).getPreneur(),par.getNombreDeJoueurs());
				}
				else
				{
					ajouterTexteDansZone(pseudos.get(((Levable)par).getPreneur())+":"+((Levable)par).getContrat()+st);
					((Tapis)getContentPane().getComponent(1)).ajouterTexteDansPanneau(((Levable)par).getContrat()+ch_v,((Levable)par).getPreneur(),par.getNombreDeJoueurs());
				}
			}
			else
			{
				if(!((PartieTarot)par).annoncesInitialisees())
				{/*Si un joueur n'a pas annonce de Chelem on initialise l'entameur du premier pli*/
					((Levable)par).setEntameur((byte)((donneur+1)%par.getNombreDeJoueurs()));
				}
				if(((Levable)par).getCarteAppelee()!=null)
				{
					((PartieTarot)par).faireConfiance(((PartieTarot)par).joueurAyantCarteAppelee(),((PartieTarot)par).getPreneur());
				}
				byte nombre_joueurs=par.getNombreDeJoueurs();
				byte preneur=((Levable)par).getPreneur();
				if(((PartieTarot)par).getAppele()==-1)
				{
					for(byte joueur=0;joueur<nombre_joueurs;joueur++)
					{
						if(joueur!=preneur)
						{
							for(byte joueur2=0;joueur2<nombre_joueurs;joueur2++)
							{
								if(joueur2!=preneur&&joueur2!=joueur)
								{
									((PartieTarot)par).faireConfiance(joueur,joueur2);
								}
							}
						}
					}
				}
			}
			afficherMainUtilisateur(true);
			premier_pli_fait=true;
		}
		else
		{
			((Levable)par).ajouterPliEnCours();
			((Levable)par).setEntameur();
			premierTour=((Levable)par).premierTour();
			if(par.getDistribution().main().total()==1&&((Levable)par).getPliEnCours().contient(new CarteTarot((byte)1,(byte)1)))
			{
				byte ramasseur=((Levable)par).getRamasseur();
				Vector<Byte> partenaires=((PartieTarot)par).coequipiers(ramasseur);
				boolean possedeExcuseMemeEquipe=false;
				for(byte joueur:partenaires)
				{
					possedeExcuseMemeEquipe|=par.getDistribution().main(joueur).contient(new CarteTarot((byte)0));
				}
				if(par.getDistribution().main(ramasseur).contient(new CarteTarot((byte)0))||possedeExcuseMemeEquipe)
				{
					if(!((PartieTarot)par).adversaireAFaitPlis(ramasseur))
					{
						Vector<Annonce> va=new Vector<Annonce>();
						va.addElement(new Annonce(Annonce.petit_au_bout));
						((Levable)par).ajouterAnnonces(ramasseur,va);
					}
				}
			}
		}
		((Levable)par).setPliEnCours();
		anim_carte=((Levable)par).getEntameur()>0?new AnimationCarte(((Levable)par).getEntameur(),par.getNombreDeJoueurs(),pseudos,premierTour):null;
		synchronized (this) {
			if(par instanceof PartieTarot)
			{
				MainTarot main_utilisateur=(MainTarot)par.getDistribution().main();
				Vector<MainTarot> repartition=main_utilisateur.couleurs();
				MainTarot cartesJouees=((PartieTarot)par).cartesJouees((byte)0);
				cartesJouees.ajouterCartes(((Levable)par).getPliEnCours().getCartes());
				Vector<MainTarot> repartitionCartesJouees=cartesJouees.couleurs();
				Vector<Vector<MainTarot>> cartes_possibles=((PartieTarot)par).cartesPossibles(!repartitionCartesJouees.get(0).estVide(),repartitionCartesJouees,((Levable)par).unionPlis(),!repartition.get(0).estVide(),repartition,(byte)0);
				Vector<Vector<MainTarot>> cartes_certaines=((PartieTarot)par).cartesCertaines(cartes_possibles);
				((PartieTarot)par).changerConfiance(((PartieTarot)par).unionPlis(),(byte)0,cartes_possibles,cartes_certaines,((PartieTarot)par).carteAppeleeJouee());
			}
			/*On affiche la main de l'utilisateur avec des ecouteurs sur les cartes et on supprime tous les boutons de l'ihm places a droite avant d'executer un eventuel Thread*/
			JPanel panneau=(JPanel)((JPanel)getContentPane().getComponent(3)).getComponent(1);
			panneau.removeAll();
			panneau.repaint();
			pack();
		}
		if(anim_carte!=null)
		{
			raison_courante=raisons[2];
			thread_anime=true;
			anim_carte.start();
		}
		else
		{
			new FinPliCarte(((Levable)par).premierTour()).start();
		}
	}
	private void annonce(Annonce annonce)
	{
		JPanel boutons=(JPanel)((JPanel)getContentPane().getComponent(3)).getComponent(1);
		if(par instanceof PartieBelote)
		{
			Vector<Annonce> an=new Vector<Annonce>();
			an.addElement(annonce);
			for(byte indice=0;indice<boutons.getComponentCount();)
				if(((JButton)boutons.getComponent(indice)).getText().equals(annonce.toString()))
					boutons.remove(indice);
				else
					indice++;
			((Levable)par).ajouterAnnonces((byte)0,an);
			ajouterTexteDansZone(pseudo()+":"+annonce);
		}
		else if(annonce.toString().endsWith(Annonce.poignee))
		{
			String pseudo=pseudo();
			Vector<Annonce> an=new Vector<Annonce>();
			an.addElement(annonce);
			((Levable)par).ajouterAnnonces((byte)0,an);
			for(byte indice=0;indice<boutons.getComponentCount();)
				if(((JButton)boutons.getComponent(indice)).getText().endsWith(Annonce.poignee))
					boutons.remove(indice);
				else
					indice++;
			MainTarot atouts=PartieTarot.atouts(((MainTarot)par.getDistribution().main()).couleurs());
			DialoguePoignees dp;
			byte nombre_atouts_poignee;
			if(annonce.toString().startsWith(Poignees.Double_Poignee.toString()))
			{
				nombre_atouts_poignee=((PartieTarot)par).poignees()[1];
			}
			else if(annonce.toString().startsWith(Poignees.Triple_Poignee.toString()))
			{
				nombre_atouts_poignee=((PartieTarot)par).poignees()[2];
			}
			else
			{
				nombre_atouts_poignee=((PartieTarot)par).poignees()[0];
			}
			dp=new DialoguePoignees(this,"Retirez "+(atouts.total()-nombre_atouts_poignee)+" atouts que vous ne voulez pas montrer dans la "+annonce,atouts,nombre_atouts_poignee);
			dp.setDialogue();
			MainTarot poignee=dp.getPoignee();/*On ordonne la poignee d'atouts*/
			poignee=PartieTarot.atouts(poignee.couleurs());
			JPanel panneau=new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
			for(byte indice=0;indice<poignee.total();indice++)
			{
				CarteGraphique carte=new CarteGraphique(poignee.carte(indice),JLabel.RIGHT,indice!=0);
				if(indice==0)
					carte.setPreferredSize(new Dimension(100,150));
				else
					carte.setPreferredSize(new Dimension(25,150));
				panneau.add(carte);
			}
			JOptionPane.showMessageDialog(this,panneau,pseudo+":"+annonce,JOptionPane.INFORMATION_MESSAGE);
			((PartieTarot)par).ajouterPoignee(poignee,(byte)0);
			ajouterTexteDansZone(pseudo+":"+annonce+st);
		}
		else if(!annonce.toString().endsWith(Contrat.chelem))
		{
			Vector<Annonce> an=new Vector<Annonce>();
			an.addElement(annonce);
			((Levable)par).ajouterAnnonces((byte)0,an);
			for(byte indice=0;indice<boutons.getComponentCount();)
				if(((JButton)boutons.getComponent(indice)).getText().equals(annonce))
					boutons.remove(indice);
				else
					indice++;
			ajouterTexteDansZone(pseudo()+":"+annonce);
		}
		else
		{
			if(!((PartieTarot)par).annoncesInitialisees())
			{
				int choix=JOptionPane.showConfirmDialog(this,"Etes-vous sur de vouloir demander le grand chelem","Demande de Chelem",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
				if(choix==JOptionPane.YES_OPTION)
				{
					Vector<Annonce> an=new Vector<Annonce>();
					an.addElement(annonce);
					((Levable)par).ajouterAnnonces((byte)0,an);
					for(byte indice=0;indice<boutons.getComponentCount();)
						if(((JButton)boutons.getComponent(indice)).getText().equals(annonce))
							boutons.remove(indice);
						else
							indice++;
					((Levable)par).setEntameur((byte)0);
					ajouterTexteDansZone(pseudo()+":Annonce d'un Chelem"+st);
				}
				else
					return;
			}
		}
		pack();
	}
	private void pliPrecedent()
	{
		afficherMainUtilisateur(false);
		afficherCartesDuPli();
		JPanel boutons=(JPanel)((JPanel)getContentPane().getComponent(3)).getComponent(1);
		boutons.removeAll();
		String Revenir_au_pli_actuel=Boutons.Revenir_au_pli_actuel.toString();
		ajouterBoutonJeu(Revenir_au_pli_actuel,Revenir_au_pli_actuel,true);
		pack();
	}
	private void revenirAuPliActuel()
	{
		afficherMainUtilisateur(true);
		String Revoir_pli_precedent=Boutons.Revoir_le_pli_precedent.toString();
		String Revoir_chien=Boutons.Revoir_le_chien.toString();
		((Tapis)getContentPane().getComponent(1)).setCartes(par.getNombreDeJoueurs(),par.jeu());
		if(par instanceof PartieTarot)
		{
			((Tapis)getContentPane().getComponent(1)).setEcart(par.getDistribution().derniereMain(),par.jeu());
			if(!((Levable)par).getPliEnCours().estVide())
			{
				for(byte joueur=((Levable)par).getEntameur();joueur<par.getNombreDeJoueurs();joueur++)
				{
					((Tapis)getContentPane().getComponent(1)).setCarte(joueur,par.getNombreDeJoueurs(),((Levable)par).getPliEnCours().carte(joueur-((Levable)par).getEntameur()));
				}
			}
			else
			{
				((Tapis)getContentPane().getComponent(1)).setCartes(par.getNombreDeJoueurs(),par.jeu());
			}
			JPanel boutons=(JPanel)((JPanel)getContentPane().getComponent(3)).getComponent(1);
			boutons.removeAll();
			if(((Levable)par).unionPlis().size()==1)
			{
				Vector<Annonce> annonces=((Annoncable)par).getAnnoncesPossibles((byte)0);
				Vector<Annonce> va=new Vector<Annonce>();
				String[] ss;
				for(int indice=0;indice<2;indice++)
				{
					String[]eclats=par.getInfos().get(4).split(";");
					if(eclats[indice].charAt(eclats[indice].length()-1)!=p2)
						ss=eclats[indice].split(":")[1].split(Parametres.separateur_tiret_slash);
					else
						ss=new String[]{};
					for(String chaine:ss)
						va.addElement(new Annonce(chaine));
				}
				for(Annonce annonce:va)
				{
					ajouterBoutonJeu(annonce.toString(),annonce.toString(),annonces.contains(annonce));
				}
				ajouterBoutonJeu(Revoir_chien,Revoir_chien,true);
			}
			else
			{
				ajouterBoutonJeu(Revoir_pli_precedent,Revoir_pli_precedent,true);
			}
		}
		else
		{
			if(!((Levable)par).getPliEnCours().estVide())
			{
				for(byte joueur=((Levable)par).getEntameur();joueur<par.getNombreDeJoueurs();joueur++)
				{
					((Tapis)getContentPane().getComponent(1)).setCarte(joueur,par.getNombreDeJoueurs(),((Levable)par).getPliEnCours().carte(joueur-((Levable)par).getEntameur()));
				}
			}
			else
			{
				((Tapis)getContentPane().getComponent(1)).setCartes(par.getNombreDeJoueurs(),par.jeu());
			}
			JPanel boutons=(JPanel)((JPanel)getContentPane().getComponent(3)).getComponent(1);
			boutons.removeAll();
			ajouterBoutonJeu(Annonce.belote_rebelote,Annonce.belote_rebelote,((Annoncable)par).autorise_annonce(new Annonce(Annonce.belote_rebelote),(byte)0));
			ajouterBoutonJeu(Revoir_pli_precedent,Revoir_pli_precedent,true);
		}
		pack();
	}
	private void afficherMainUtilisateur(boolean ecouteur)
	{
		JPanel panneau1=(JPanel)getContentPane().getComponent(2);
		panneau1.removeAll();
		panneau1.repaint();
		MainTriable main_utilisateur;
		if(par instanceof PartieBelote)
		{
			main_utilisateur=new MainBelote();
			for(Carte ct:par.getDistribution().main())
			{
				main_utilisateur.ajouter(ct);
			}
			if(((Levable)par).getContrat().force()==0)
			{
				((MainBelote)main_utilisateur).setOrdre(Ordre.valueOf(par.getInfos().get(4).split(":")[1]));
				main_utilisateur.trier(par.getInfos().get(3),par.getInfos().get(3));
			}
			else if(((Levable)par).getContrat().force()==1)
			{
				((MainBelote)main_utilisateur).trier(par.getInfos().get(3),par.getInfos().get(3),((Levable)par).getCarteAppelee());
			}
			else
			{
				if(((Levable)par).getContrat().force()==3)
				{
					((MainBelote)main_utilisateur).setOrdre(Ordre.Couleur);
				}
				main_utilisateur.trier(par.getInfos().get(3),par.getInfos().get(3));
			}
		}
		else
		{
			main_utilisateur=new MainTarot();
			for(Carte ct:par.getDistribution().main())
			{
				main_utilisateur.ajouter(ct);
			}
			main_utilisateur.trier(par.getInfos().get(3),par.getInfos().get(3));
		}
		/*On place les cartes de l'utilisateur*/
		for(byte indice=0;indice<main_utilisateur.total();indice++)
		{
			CarteGraphique carte=new CarteGraphique(main_utilisateur.carte(indice),JLabel.RIGHT,indice!=0);
			if(indice==0)
			{
				carte.setPreferredSize(new Dimension(100,150));
			}
			else
			{
				carte.setPreferredSize(new Dimension(25,150));
			}
			if(ecouteur)
			{
				carte.addMouseListener(new EcouteurCarte(carte.getCarte()));
			}
			panneau1.add(carte);
		}
	}
	private void afficherMainUtilisateur(MainTriable main_utilisateur)
	{
		JPanel panneau1=(JPanel)getContentPane().getComponent(2);
		panneau1.removeAll();
		panneau1.repaint();
		/*On place les cartes de l'utilisateur*/
		for(byte indice=0;indice<main_utilisateur.total();indice++)
		{
			CarteGraphique carte=new CarteGraphique(main_utilisateur.carte(indice),JLabel.RIGHT,indice!=0);
			if(indice==0)
			{
				carte.setPreferredSize(new Dimension(100,150));
			}
			else
			{
				carte.setPreferredSize(new Dimension(25,150));
			}
			panneau1.add(carte);
		}
	}
	private void finPli(Carte carte_jouee)
	{
		debutPli=false;
		//Activer le menu Partie/Pause
		getJMenuBar().getMenu(1).getItem(1).setEnabled(true);
		Vector<String> pseudos=pseudos();
		//Desactiver le sous-menu conseil
		getJMenuBar().getMenu(1).getItem(0).setEnabled(false);
		JPanel panneau1=new JPanel();
		panneau1.setBackground(Color.BLUE);
		panneau1.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
		if(par instanceof PartieTarot)
		{
			if(((PartieTarot)par).getCarteAppelee()!=null&&((PartieTarot)par).getCarteAppelee().equals(carte_jouee))
			{
				ajouterTexteDansPanneau(Statut.Appele.toString(),(byte)0);
				ajouterTexteDansZone(pseudo()+":"+Statut.Appele);
			}
		}
		/*L'utilisateur joue sa carte*/
		par.getDistribution().main().jouer(carte_jouee);
		boolean premierTour;
		if(par instanceof PartieBelote)
			premierTour=((Levable)par).unionPlis().size()==0;
		else
			premierTour=((Levable)par).unionPlis().size()==1;
		((Levable)par).ajouterUneCarteDansPliEnCours(carte_jouee);
		synchronized (this) {//Pour ne pas a avoir a faire disparaitre un instant de temps la main de l'utilisateur
			//Il ne se rendra pas compte que la main est repeinte entierement
			raison_courante=raisons[0];
			afficherMainUtilisateur(true);
			((Tapis)getContentPane().getComponent(1)).setCarte((byte)0,par.getNombreDeJoueurs(),carte_jouee);
			pause();
			//Desactiver le menu Partie/Pause
			getJMenuBar().getMenu(1).getItem(1).setEnabled(false);
			((JPanel)((JPanel)getContentPane().getComponent(3)).getComponent(1)).removeAll();
			((JPanel)((JPanel)getContentPane().getComponent(3)).getComponent(1)).repaint();
			pack();
		}
		if(((Levable)par).getEntameur()==0)
			anim_carte=new AnimationCarte((byte)1,par.getNombreDeJoueurs(),pseudos,premierTour);
		else if(((Levable)par).getEntameur()!=1)
			anim_carte=new AnimationCarte((byte)1,((Levable)par).getEntameur(),pseudos,premierTour);
		else
			anim_carte=null;
		if(anim_carte!=null)
		{
			anim_carte.start();
			thread_anime=true;
		}
		else
		{/*L'utilisateur a joue sa carte et est le dernier a jouer*/
			if(par instanceof PartieBelote)
			{
				if(param.getInfos().get(0).get(7).split(";")[0].endsWith("non"))
				{
					((Levable)par).ajouterPliEnCours();
					if(par.getDistribution().main().estVide())
					{
						Vector<Annonce> va=new Vector<Annonce>();
						va.addElement(new Annonce(Annonce.dix_de_der));
						((Levable)par).ajouterAnnonces(((Levable)par).getRamasseur(),va);
						finPartie();
					}
					else
					{
						synchronized (this) {
							getJMenuBar().getMenu(1).getItem(0).setEnabled(true);
							((Levable)par).setEntameur();
							((Levable)par).setPliEnCours();
							debutPli=((Levable)par).getEntameur()>0;
							premierTour=((Levable)par).unionPlis().size()==0;
						}
						if(((Levable)par).getEntameur()>0)
						{
							thread_anime=true;
							anim_carte=new AnimationCarte(((Levable)par).getEntameur(),par.getNombreDeJoueurs(),pseudos,premierTour);
							anim_carte.start();
						}
						else
						{
							new FinPliCarte(premierTour).start();
						}
					}
				}
				else
				{
					thread_anime=false;
					placerBoutonsFinPliUtilisateur();
					pack();
				}
			}
			else
			{
				if(param.getInfos().get(0).get(7).split(";")[1].endsWith("non"))
				{
					((Levable)par).ajouterPliEnCours();
					if(par.getDistribution().main().total()==1&&((Levable)par).getPliEnCours().contient(new CarteTarot((byte)1,(byte)1)))
					{
						Vector<Byte> partenaires=((PartieTarot)par).coequipiers(((Levable)par).getRamasseur());
						boolean possedeExcuseMemeEquipe=false;
						for(byte partenaire:partenaires)
							possedeExcuseMemeEquipe|=par.getDistribution().main(partenaire).contient(new CarteTarot((byte)0));
						if(par.getDistribution().main(((Levable)par).getRamasseur()).contient(new CarteTarot((byte)0))||possedeExcuseMemeEquipe)
						{
							if(!((PartieTarot)par).adversaireAFaitPlis(((Levable)par).getRamasseur()))
							{
								Vector<Annonce> va=new Vector<Annonce>();
								va.addElement(new Annonce(Annonce.petit_au_bout));
								((Levable)par).ajouterAnnonces(((Levable)par).getRamasseur(),va);
							}
						}
					}
					if(par.getDistribution().main().estVide())
					{
						if(((Levable)par).getPliEnCours().contient(new CarteTarot((byte)1,(byte)1)))
						{/*Le Petit est mene au bout*/
							Vector<Annonce> va=new Vector<Annonce>();
							va.addElement(new Annonce(Annonce.petit_au_bout));
							((Levable)par).ajouterAnnonces(((Levable)par).getRamasseur(),va);
						}
						finPartie();
					}
					else
					{
						synchronized (this) {
							getJMenuBar().getMenu(1).getItem(0).setEnabled(true);
							debutPli=true;
							((Levable)par).setEntameur();
							((Levable)par).setPliEnCours();
							par.setEtat(Etat.Jeu);
							premierTour=((Levable)par).unionPlis().size()==1;
						}
						if(((Levable)par).getEntameur()>0)
						{
							thread_anime=true;
							anim_carte=new AnimationCarte(((Levable)par).getEntameur(),par.getNombreDeJoueurs(),pseudos,premierTour);
							anim_carte.start();
						}
						else
						{
							new FinPliCarte(premierTour).start();
						}
					}
				}
				else
				{
					thread_anime=false;
					placerBoutonsFinPliUtilisateur();
					pack();
				}
			}
		}
	}
	private void finPartie()
	{
		/*Descativer aide au jeu*/
		getJMenuBar().getMenu(1).getItem(2).setEnabled(false);
		Container container=new Container();
		JScrollPane ascenseur;
		container.setLayout(new BorderLayout());
		JPanel panneau=new JPanel();
		JLabel etiquette;
		JPanel sous_panneau=new JPanel();
		JPanel sous_panneau2=new JPanel();
		if(changer_pile_fin)
		{
			((Levable)par).empiler().sauvegarder(Fichier.dossier_paquets+File.separator+par.jeu()+Fichier.extension_paquet);
		}
		/*Le nombre de parties jouees depuis le lancement du logiciel*/
		enCoursDePartie=false;
		thread_anime=false;
		final byte nombre_joueurs=par.getNombreDeJoueurs();
		byte gagne_nul_perd;
		Vector<String> pseudos=pseudos();
		if(par instanceof Levable)
		{
			if(par.getType()==Type.Aleatoire)
			{
				partie_aleatoire_jouee=true;
				if(changer_pile_fin)
				{
					changerNombreDeParties2();
				}
			}
			long variance_9=0;
			long esperance=0;
			final Vector<Pli> plis_faits=new Vector<Pli>();
			final Pli pli_petit;
			JTabbedPane onglets=new JTabbedPane();
			Vector<Short> scores_partie = new Vector<Short>();
			if(par instanceof PartieBelote)
			{
				final int belote=Jeu.Belote.ordinal()+1;
				pli_petit=null;
				panneau=new JPanel();
				sous_panneau=new JPanel(new GridLayout(0,1));
				sous_panneau.setBorder(BorderFactory.createTitledBorder("Resultats"));
				Vector<Vector<Short>> annonces_defense=new Vector<Vector<Short>>();
				Vector<Vector<Short>> annonces_attaque=new Vector<Vector<Short>>();
				Contrat contrat=((PartieBelote)par).getContrat();
				byte preneur=((PartieBelote)par).getPreneur();
				byte appele=(byte)((preneur+2)%nombre_joueurs);
				byte defenseur1=(byte)((preneur+1)%nombre_joueurs);
				byte defenseur2=(byte)((preneur+3)%nombre_joueurs);
				int capot_attaque=((PartieBelote)par).valeurCapot();
				int pointsAttaqueSansPrime=((PartieBelote)par).pointsAttaqueSansPrime();
				int pointsAttaqueTemporaire=pointsAttaqueSansPrime;
				int pointsAttaqueDefinitif=0;
				int pointsDefenseSansPrime=152-pointsAttaqueSansPrime;
				int pointsDefenseTemporaire=pointsDefenseSansPrime;
				int pointsDefenseDefinitif=0;
				short difference_score;
				sous_panneau.add(new JLabel("Donneur: "+pseudos.get(par.getDistribution().getDonneur())));
				if(par.getEtat()!=Etat.Contrat)
				{
					plis_faits.addAll(((PartieBelote)par).unionPlis());
					annonces_attaque.addElement(((PartieBelote)par).pointsAnnoncesPrimes(preneur));
					annonces_attaque.addElement(((PartieBelote)par).pointsAnnoncesPrimes(appele));
					annonces_defense.addElement(((PartieBelote)par).pointsAnnoncesPrimes(defenseur1));
					annonces_defense.addElement(((PartieBelote)par).pointsAnnoncesPrimes(defenseur2));
					for(Vector<Short> points_annonces_joueur:annonces_attaque)
					{
						for(short points_annonce:points_annonces_joueur)
						{
							pointsAttaqueTemporaire+=points_annonce;
						}
					}
					pointsAttaqueTemporaire+=capot_attaque;
					for(Vector<Short> points_annonces_joueur:annonces_defense)
					{
						for(short points_annonce:points_annonces_joueur)
						{
							pointsDefenseTemporaire+=points_annonce;
						}
					}
					sous_panneau.add(new JLabel("Preneur: "+pseudos.get(preneur)));
					sous_panneau.add(new JLabel("Partenaire du preneur: "+pseudos.get(appele)));
					sous_panneau.add(new JLabel("Points realises en attaque sans les primes: "+pointsAttaqueSansPrime));
					sous_panneau.add(new JLabel("Points realises en defense sans les primes: "+pointsDefenseSansPrime));
					sous_panneau.add(new JLabel("Points realises en attaque avec les primes: "+pointsAttaqueTemporaire));
					sous_panneau.add(new JLabel("Points realises en defense avec les primes: "+pointsDefenseTemporaire));
					pointsAttaqueDefinitif=((PartieBelote)par).scoreDefinitifAttaque(pointsAttaqueTemporaire, pointsDefenseTemporaire);
					pointsDefenseDefinitif=((PartieBelote)par).scoreDefinitifDefense(pointsAttaqueDefinitif);
					sous_panneau.add(new JLabel("Score en attaque d"+ea+"finitif: "+pointsAttaqueDefinitif));
					sous_panneau.add(new JLabel("Score en defense d"+ea+"finitif: "+pointsDefenseDefinitif));
					((PartieBelote)par).scores(pointsAttaqueDefinitif, pointsDefenseDefinitif);
					gagne_nul_perd=((PartieBelote)par).gagne_nul_perd();
					scores_partie=par.getScores();
					if(gagne_nul_perd==-1)
					{
						sous_panneau.add(new JLabel("Vous avez perdu"));
					}
					else if(gagne_nul_perd==0)
					{
						sous_panneau.add(new JLabel("Match nul"));
					}
					else
					{
						sous_panneau.add(new JLabel("Vous avez gagne"));
					}
					difference_score=(short) (scores_partie.get(preneur)-scores_partie.get((preneur+1)%nombre_joueurs));
					if(difference_score>0)
					{
						if(contrat.force()==1)
						{
							sous_panneau.add(new JLabel("Le contrat a la couleur "+((PartieBelote)par).getCarteAppelee().toString().split(" ")[2]+" est reussi de "+difference_score));
						}
						else
						{
							sous_panneau.add(new JLabel("Le contrat "+contrat+" est reussi de "+difference_score));
						}
					}
					else if(difference_score<0)
					{
						if(contrat.force()==1)
						{
							sous_panneau.add(new JLabel("Le contrat a la couleur "+((PartieBelote)par).getCarteAppelee().toString().split(" ")[2]+" est chute de "+-difference_score));
						}
						else
						{
							sous_panneau.add(new JLabel("Le contrat "+contrat+" est chute de "+-difference_score));
						}
					}
					if(capot_attaque>0)
					{
						sous_panneau.add(new JLabel("Capot reussi en attaque"));
					}
				}
				else
				{
					sous_panneau.add(new JLabel("Personne n a pris."));
					for(byte joueur=0;joueur<nombre_joueurs;joueur++)
					{
						scores_partie.addElement((short)0);
					}
				}
				panneau.add(sous_panneau);
				if(par.getEtat()!=Etat.Contrat)
				{
					String chaines[]= param.getInfos().get(belote).get(3).split(";");
					((PartieBelote)par).restituerMainsDepart(plis_faits, pli_petit, nombre_joueurs, chaines[0].split(":")[1], chaines[1]);
				}
				if(par.getType()==Type.Aleatoire&&par.getNombre()==0)
				{
					scores.addElement(new Vector<Long>());
					if(scores.size()==1)
					{
						for(short score:par.getScores())
						{
							scores.lastElement().addElement(new Long(score));
						}
					}
					else
					{
						byte indice=0;
						for(short score:par.getScores())
						{
							scores.lastElement().addElement(score+scores.get(scores.size()-2).get(indice));
							indice++;
						}
					}
					for(long score:scores.lastElement())
					{
						esperance+=score;
					}/*Somme des scores*/
					variance_9+=3*esperance;/*Somme des scores fois trois*/
					variance_9*=variance_9;/*Carre de la somme des scores fois trois (Le carre comprend le fois trois)*/
					variance_9=-variance_9;/*Oppose du carre de la somme des scores fois trois*/
					for(long score:scores.lastElement())
					{
						variance_9+=score*score*9*nombre_joueurs;
					}/*variance_9 vaut neuf fois la variance des scores fois le carre du nombre de joueurs*/
					sigmas.addElement(Math.sqrt(variance_9/(double)(nombre_joueurs*nombre_joueurs)));
					sommes.addElement(esperance);
				}
				sous_panneau=new JPanel(new GridLayout(0,nombre_joueurs+1,2,2));
				sous_panneau.setOpaque(true);
				sous_panneau.setBackground(Color.BLACK);
				etiquette=new JLabel();
				etiquette.setOpaque(true);
				etiquette.setBackground(Color.GRAY);
				sous_panneau.add(etiquette);
				for(byte joueur=0;joueur<nombre_joueurs;joueur++)
				{
					etiquette=new JLabel(pseudos.get(joueur));
					etiquette.setHorizontalAlignment(JLabel.CENTER);
					etiquette.setOpaque(true);
					etiquette.setBackground(Color.GRAY);
					sous_panneau.add(etiquette);
				}
				for(int indice_partie=0;indice_partie<scores.size();indice_partie++)
				{
					etiquette=new JLabel(indice_partie+ch_v);
					etiquette.setHorizontalAlignment(JLabel.CENTER);
					etiquette.setOpaque(true);
					etiquette.setBackground(Color.GRAY);
					sous_panneau.add(etiquette);
					for(byte joueur=0;joueur<nombre_joueurs;joueur++)
					{
						etiquette=new JLabel(scores.get(indice_partie).get(joueur).toString());
						etiquette.setHorizontalAlignment(JLabel.CENTER);
						etiquette.setOpaque(true);
						etiquette.setBackground(Color.WHITE);
						sous_panneau.add(etiquette);
					}
				}
				ascenseur=new JScrollPane(sous_panneau);
				int limite=((JPanel)panneau.getComponent(0)).getComponentCount();
				if(scores.size()>limite&&scores.size()>10)
				{
					ascenseur.setPreferredSize(new Dimension(300,500));
				}
				panneau.add(ascenseur);
				onglets.add("Resultats de cette partie",panneau);
				if(par.getEtat()!=Etat.Contrat)
				{
					panneau=new JPanel(new GridLayout(0,1));
					panneau.setBorder(BorderFactory.createTitledBorder("Annonces"));
					sous_panneau=new JPanel(new GridLayout(0,1));
					sous_panneau.setBorder(BorderFactory.createTitledBorder("Annonces de "+pseudos.get(preneur)+" ("+((PartieBelote)par).statut_de(preneur)+")"));
					for(byte indice_annonce=0;indice_annonce<annonces_attaque.get(0).size();indice_annonce++)
					{
						sous_panneau.add(new JLabel(((PartieBelote)par).getAnnonces(preneur).get(indice_annonce)+": "+annonces_attaque.get(0).get(indice_annonce)));
					}
					panneau.add(sous_panneau);
					sous_panneau=new JPanel(new GridLayout(0,1));
					sous_panneau.setBorder(BorderFactory.createTitledBorder("Annonces de "+pseudos.get(appele)+" ("+((PartieBelote)par).statut_de(appele)+")"));
					for(byte indice_annonce=0;indice_annonce<annonces_attaque.get(1).size();indice_annonce++)
					{
						sous_panneau.add(new JLabel(((PartieBelote)par).getAnnonces(appele).get(indice_annonce)+": "+annonces_attaque.get(1).get(indice_annonce)));
					}
					panneau.add(sous_panneau);
					sous_panneau=new JPanel(new GridLayout(0,1));
					sous_panneau.setBorder(BorderFactory.createTitledBorder("Annonces de "+pseudos.get(defenseur1)+" ("+((PartieBelote)par).statut_de(defenseur1)+")"));
					for(byte indice_annonce=0;indice_annonce<annonces_defense.get(0).size();indice_annonce++)
					{
						sous_panneau.add(new JLabel(((PartieBelote)par).getAnnonces(defenseur1).get(indice_annonce)+": "+annonces_defense.get(0).get(indice_annonce)));
					}
					panneau.add(sous_panneau);
					sous_panneau=new JPanel(new GridLayout(0,1));
					sous_panneau.setBorder(BorderFactory.createTitledBorder("Annonces de "+pseudos.get(defenseur2)+" ("+((PartieBelote)par).statut_de(defenseur2)+")"));
					for(byte indice_annonce=0;indice_annonce<annonces_defense.get(1).size();indice_annonce++)
					{
						sous_panneau.add(new JLabel(((PartieBelote)par).getAnnonces(defenseur2).get(indice_annonce)+": "+annonces_defense.get(1).get(indice_annonce)));
					}
					panneau.add(sous_panneau);
					ascenseur=new JScrollPane(panneau);
					ascenseur.setPreferredSize(new Dimension(700,700));
					onglets.add("Calcul des scores",ascenseur);
				}
				if(par.getType()==Type.Aleatoire)
				{
					Vector<Color> couleurs=new Vector<Color>();
					couleurs.addElement(Color.RED);
					couleurs.addElement(Color.GREEN);
					couleurs.addElement(Color.BLUE);
					if(nombre_joueurs>3)
					{
						couleurs.addElement(Color.YELLOW);
					}
					if(nombre_joueurs>4)
					{
						couleurs.addElement(Color.MAGENTA);
					}
					if(nombre_joueurs>5)
					{
						couleurs.addElement(Color.CYAN);
					}
					if(nombre_joueurs>6)
					{
						couleurs.addElement(Color.ORANGE);
					}
					if(nombre_joueurs>7)
					{
						couleurs.addElement(new Color(128,64,0));
					}
					if(nombre_joueurs>8)
					{
						couleurs.addElement(new Color(128,128,0));
					}
					if(nombre_joueurs>9)
					{
						couleurs.addElement(new Color(128,0,255));
					}
					Graphique graphique=new Graphique(scores,sommes,sigmas,couleurs);
					double derniere_moyenne=sommes.lastElement()/(double)nombre_joueurs;
					Vector<Double> scores_centres_moyenne=new Vector<Double>();
					for(byte joueur=0;joueur<nombre_joueurs;joueur++)
					{
						scores_centres_moyenne.addElement(scores.lastElement().get(joueur)-derniere_moyenne);
					}
					scores_centres_moyenne.addElement(3*sigmas.lastElement());
					for(double maximum:scores_centres_moyenne)
					{
						max_absolu_score=Math.max(Math.abs(maximum),max_absolu_score);
					}
					int dimy=(int)max_absolu_score;
					graphique.setPreferredSize(new Dimension(2000,dimy));
					ascenseur=new JScrollPane(graphique);
					graphique.setLocation(0,(600-dimy)/2);
					ascenseur.setPreferredSize(new Dimension(700,600));
					panneau=new JPanel(new BorderLayout());
					panneau.add(new JLabel("Evolution des scores centr"+ea+"s par rapport a la moyenne et des ecarts types multiplies par plus ou moins trois en fonction du temps",JLabel.CENTER),BorderLayout.NORTH);
					panneau.add(ascenseur,BorderLayout.CENTER);
					Legende legende=new Legende(pseudos,couleurs);
					legende.setPreferredSize(new Dimension(300,15*(nombre_joueurs+1)));
					ascenseur=new JScrollPane(legende);
					ascenseur.setPreferredSize(new Dimension(300,100));
					panneau.add(ascenseur,BorderLayout.SOUTH);
					onglets.add("Courbes temporelles",panneau);
				}
				panneau=new JPanel(new BorderLayout());
				sous_panneau=new JPanel();
				sous_panneau.add(new JPanel(new GridLayout(0,1)));
				sous_panneau.add(new JPanel(new GridLayout(0,1)));
				sous_panneau2=new JPanel(new GridLayout(0,1));
				JPanel sous_panneau3;
				for(byte joueur=0;joueur<nombre_joueurs;joueur++)
				{
					sous_panneau3=new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
					for(byte indice=0;indice<par.getDistribution().main(joueur).total();indice++)
					{
						CarteGraphique carte_graphique=new CarteGraphique(par.getDistribution().main(joueur).carte(indice),JLabel.RIGHT,indice!=0);
						if(indice>0)
						{
							carte_graphique.setPreferredSize(new Dimension(25,150));
						}
						else
						{
							carte_graphique.setPreferredSize(new Dimension(100,150));
						}
						sous_panneau3.add(carte_graphique);
					}
					sous_panneau2.add(sous_panneau3);
				}
				for(byte joueur=0;joueur<nombre_joueurs-1;joueur++)
				{
					sous_panneau2.add(new JPanel(new FlowLayout(FlowLayout.LEFT,0,0)));
				}
				sous_panneau.add(sous_panneau2);
				sous_panneau2=new JPanel(new GridLayout(0,1));
				sous_panneau3=new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
				for(byte indice=0;indice<par.getDistribution().derniereMain().total();indice++)
				{
					CarteGraphique carte_graphique=new CarteGraphique(par.getDistribution().derniereMain().carte(indice),JLabel.RIGHT,indice!=0);
					if(indice>0)
					{
						carte_graphique.setPreferredSize(new Dimension(25,150));
					}
					else
					{
						carte_graphique.setPreferredSize(new Dimension(100,150));
					}
					sous_panneau3.add(carte_graphique);
				}
				sous_panneau2.add(sous_panneau3);
				sous_panneau.add(sous_panneau2);
				ascenseur=new JScrollPane(sous_panneau);
				ascenseur.setPreferredSize(new Dimension(700,700));
				panneau.add(ascenseur,BorderLayout.CENTER);
				sous_panneau=new JPanel();
				sous_panneau.add(new JLabel("Pli n°"));
				Object[] numeros_plis=new Object[plis_faits.size()+(par.getEtat()==Etat.Contrat?1:2)];
				for(byte indice_pli=0;indice_pli<numeros_plis.length;indice_pli++)
				{
					numeros_plis[indice_pli]=indice_pli-1;
				}
				JComboBox liste=new JComboBox(numeros_plis);
				liste.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent event) {
						JTabbedPane onglets2=(JTabbedPane)getContentPane().getComponent(0);
						JPanel onglet=(JPanel)onglets2.getComponentAt(onglets2.getComponentCount()-1);
						JPanel cartes=(JPanel)((JScrollPane)onglet.getComponent(0)).getViewport().getComponent(0);
						byte numero_pli=Byte.parseByte(((JComboBox)((JPanel)onglet.getComponent(1)).getComponent(1)).getSelectedItem().toString());
						String chaines[]= param.getInfos().get(belote).get(3).split(";");
						((PartieBelote)par).restituerMains(plis_faits, pli_petit, nombre_joueurs,chaines[0].split(":")[1],chaines[1], numero_pli);
						JPanel mains_graphiques=(JPanel)cartes.getComponent(2);
						mains_graphiques.removeAll();
						mains_graphiques.setLayout(new GridLayout(0,1));
						for(byte joueur=0;joueur<nombre_joueurs;joueur++)
						{
							JPanel sous_panneau4=new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
							for(byte indice=0;indice<par.getDistribution().main(joueur).total();indice++)
							{
								CarteGraphique carte_graphique=new CarteGraphique(par.getDistribution().main(joueur).carte(indice),JLabel.RIGHT,indice!=0);
								if(indice>0)
								{
									carte_graphique.setPreferredSize(new Dimension(25,150));
								}
								else
								{
									carte_graphique.setPreferredSize(new Dimension(100,150));
								}
								sous_panneau4.add(carte_graphique);
							}
							mains_graphiques.add(sous_panneau4);
						}
						for(byte joueur=0;joueur<nombre_joueurs-1;joueur++)
						{
							mains_graphiques.add(new JPanel(new FlowLayout(FlowLayout.LEFT,0,0)));
						}
						mains_graphiques=(JPanel)cartes.getComponent(1);
						mains_graphiques.removeAll();
						if(numero_pli>0)
						{
							mains_graphiques.setLayout(new GridLayout(0,1));
							byte entameur=plis_faits.get(numero_pli-1).getEntameur();
							byte indice=0;
							for(;indice<entameur;indice++)
							{
								JLabel etiquette2=new JLabel(ch_v+indice);
								etiquette2.setHorizontalAlignment(JLabel.CENTER);
								etiquette2.setFont(new Font("Default",Font.BOLD,50));
								etiquette2.setOpaque(true);
								etiquette2.setBackground(Color.WHITE);
								mains_graphiques.add(etiquette2);
							}
							for(Carte carte:plis_faits.get(numero_pli-1))
							{
								CarteGraphique carte_graphique_2=new CarteGraphique(carte,JLabel.RIGHT,false);
								carte_graphique_2.setPreferredSize(new Dimension(100,150));
								mains_graphiques.add(carte_graphique_2);
								indice++;
							}
							for(;indice<2*nombre_joueurs-1;indice++)
							{
								JLabel etiquette2=new JLabel(ch_v+(indice-nombre_joueurs));
								etiquette2.setHorizontalAlignment(JLabel.CENTER);
								etiquette2.setFont(new Font("Default",Font.BOLD,50));
								etiquette2.setOpaque(true);
								etiquette2.setBackground(Color.WHITE);
								mains_graphiques.add(etiquette2);
							}
						}
						mains_graphiques=(JPanel)cartes.getComponent(0);
						mains_graphiques.removeAll();
						if(numero_pli>1)
						{
							mains_graphiques.setLayout(new GridLayout(0,numero_pli-1));
						}
						for(byte indice_pli=1;indice_pli<numero_pli;indice_pli++)
						{
							byte entameur=plis_faits.get(indice_pli-1).getEntameur();
							byte indice=0;
							for(indice=0;indice<entameur;indice++)
							{
								JLabel etiquette2=new JLabel(ch_v+indice);
								etiquette2.setHorizontalAlignment(JLabel.CENTER);
								etiquette2.setFont(new Font("Default",Font.BOLD,50));
								etiquette2.setOpaque(true);
								etiquette2.setBackground(Color.WHITE);
								mains_graphiques.add(etiquette2,indice_pli*(indice+1)-1);
							}
							for(Carte carte:plis_faits.get(indice_pli-1))
							{
								CarteGraphique carte_graphique_2=new CarteGraphique(carte,JLabel.RIGHT,false);
								carte_graphique_2.setPreferredSize(new Dimension(100,150));
								mains_graphiques.add(carte_graphique_2,indice_pli*(indice+1)-1);
								indice++;
							}
							for(;indice<nombre_joueurs*2-1;indice++)
							{
								JLabel etiquette2=new JLabel(ch_v+(indice-nombre_joueurs));
								etiquette2.setHorizontalAlignment(JLabel.CENTER);
								etiquette2.setFont(new Font("Default",Font.BOLD,50));
								etiquette2.setOpaque(true);
								etiquette2.setBackground(Color.WHITE);
								mains_graphiques.add(etiquette2,indice_pli*(indice+1)-1);
							}
						}
						pack();
					}
				}
				);
				sous_panneau.add(liste);
				sous_panneau.add(new JLabel("Carte n°"));
				Object[] numeros_joueurs=new Object[nombre_joueurs];
				for(byte indice_joueur=0;indice_joueur<numeros_joueurs.length;indice_joueur++)
				{
					numeros_joueurs[indice_joueur]=indice_joueur+1;
				}
				liste=new JComboBox(numeros_joueurs);
				liste.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent event) {
						JTabbedPane onglets2=(JTabbedPane)getContentPane().getComponent(0);
						JPanel onglet=(JPanel)onglets2.getComponentAt(onglets2.getComponentCount()-1);
						JPanel panneau_bas=(JPanel)onglet.getComponent(1);
						byte numero_pli=Byte.parseByte(((JComboBox)panneau_bas.getComponent(1)).getSelectedItem().toString());
						if(numero_pli<1)
						{
							return;
						}
						JPanel cartes=(JPanel)((JScrollPane)onglet.getComponent(0)).getViewport().getComponent(0);
						byte numero_carte=Byte.parseByte(((JComboBox)panneau_bas.getComponent(3)).getSelectedItem().toString());
						numero_carte--;
						String chaines[]= param.getInfos().get(belote).get(3).split(";");
						((PartieBelote)par).restituerMains(plis_faits, pli_petit, nombre_joueurs,chaines[0].split(":")[1],chaines[1], numero_pli,numero_carte);
						JPanel mains_graphiques=(JPanel)cartes.getComponent(2);
						mains_graphiques.removeAll();
						mains_graphiques.setLayout(new GridLayout(0,1));
						for(byte joueur=0;joueur<nombre_joueurs;joueur++)
						{
							JPanel sous_panneau4=new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
							for(byte indice=0;indice<par.getDistribution().main(joueur).total();indice++)
							{
								CarteGraphique carte_graphique=new CarteGraphique(par.getDistribution().main(joueur).carte(indice),JLabel.RIGHT,indice!=0);
								if(indice>0)
								{
									carte_graphique.setPreferredSize(new Dimension(25,150));
								}
								else
								{
									carte_graphique.setPreferredSize(new Dimension(100,150));
								}
								sous_panneau4.add(carte_graphique);
							}
							mains_graphiques.add(sous_panneau4);
						}
						for(byte joueur=0;joueur<nombre_joueurs-1;joueur++)
						{
							mains_graphiques.add(new JPanel(new FlowLayout(FlowLayout.LEFT,0,0)));
						}
						mains_graphiques=(JPanel)cartes.getComponent(1);
						mains_graphiques.removeAll();
						if(numero_pli>0)
						{
							mains_graphiques.setLayout(new GridLayout(0,1));
							byte entameur=plis_faits.get(numero_pli-1).getEntameur();
							byte indice=0;
							byte indice2=0;
							for(;indice<entameur;indice++)
							{
								JLabel etiquette2=new JLabel(ch_v+indice);
								etiquette2.setHorizontalAlignment(JLabel.CENTER);
								etiquette2.setFont(new Font("Default",Font.BOLD,50));
								etiquette2.setOpaque(true);
								etiquette2.setBackground(Color.WHITE);
								mains_graphiques.add(etiquette2);
							}
							for(Carte carte:plis_faits.get(numero_pli-1))
							{
								if(indice2<=numero_carte)
								{
									CarteGraphique carte_graphique_2=new CarteGraphique(carte,JLabel.RIGHT,false);
									carte_graphique_2.setPreferredSize(new Dimension(100,150));
									mains_graphiques.add(carte_graphique_2);
									indice++;
									indice2++;
								}
								else
								{
									break;
								}
							}
							for(;indice<2*nombre_joueurs-1;indice++)
							{
								JLabel etiquette2=new JLabel(ch_v+(indice-nombre_joueurs));
								etiquette2.setHorizontalAlignment(JLabel.CENTER);
								etiquette2.setFont(new Font("Default",Font.BOLD,50));
								etiquette2.setOpaque(true);
								etiquette2.setBackground(Color.WHITE);
								mains_graphiques.add(etiquette2);
							}
						}
						pack();
					}
				}
				);
				sous_panneau.add(liste);
				panneau.add(sous_panneau,BorderLayout.SOUTH);
				onglets.add("Mains et plis",panneau);
			}
			else
			{
				final int tarot=Jeu.Tarot.ordinal()+1;
				Contrat contrat=((PartieTarot)par).getContrat();
				plis_faits.addAll(((PartieTarot)par).unionPlis());
				pli_petit=((PartieTarot)par).pliLePlusPetit(plis_faits);
				byte appele=((PartieTarot)par).getAppele();
				byte preneur=((PartieTarot)par).getPreneur();
				panneau=new JPanel();
				sous_panneau=new JPanel(new GridLayout(0,1));
				short base=0;
				short difference_score_preneur=0;
				sous_panneau.setBorder(BorderFactory.createTitledBorder("R"+ea+"sultats"));
				sous_panneau.add(new JLabel("Donneur: "+pseudos.get(par.getDistribution().getDonneur())));
				Vector<Vector<Annonce>> annonces_sans_petit_bout=new Vector<Vector<Annonce>>();
				Vector<Vector<Short>> calcul_annonces_score_preneur=new Vector<Vector<Short>>();
				Vector<Vector<Vector<Short>>> points_annonces=new Vector<Vector<Vector<Short>>>();
				byte[][] coefficients_repartition=new byte[1][1];
				short[] primes_supplementaires=new short[2];
				short score_preneur_sans_annonces=0;
				short[] scores_joueurs_plis_double=new short[nombre_joueurs];
				short[] scores_necessaires_joueurs=new short[nombre_joueurs];
				short[] differences_joueurs_double=new short[nombre_joueurs];
				byte[] positions=new byte[nombre_joueurs];
				byte[] positions1=new byte[nombre_joueurs];
				byte[] positions2=new byte[nombre_joueurs];
				byte[] positions3=new byte[nombre_joueurs];
				byte[] positions4=new byte[nombre_joueurs];
				byte[] coefficients=new byte[nombre_joueurs];
				short[] prime_supplementaire=new short[nombre_joueurs];
				short difference_max_double=0;
				short difference_max=0;
				byte position_initiale_utilisateur=0;
				if(contrat.force()>0)
				{
					byte premier_defenseur=-1;
					for(byte joueur=0;joueur<nombre_joueurs;joueur++)
					{
						if(joueur!=preneur&&joueur!=appele)
						{
							premier_defenseur=joueur;
							break;
						}
					}
					short score_preneur_plis_double=((PartieTarot)par).score_preneur_plis_double(pli_petit);
					byte nombre_bouts_preneur=((PartieTarot)par).nombre_bouts_preneur();
					short score_necessaire_preneur=((PartieTarot)par).score_necessaire_preneur();
					short score_preneur_plis=((PartieTarot)par).score_preneur_plis(score_preneur_plis_double, score_necessaire_preneur);
					difference_score_preneur=(short) (score_preneur_plis-score_necessaire_preneur);
					base=((PartieTarot)par).base(score_preneur_plis_double,difference_score_preneur);
					score_preneur_sans_annonces=((PartieTarot)par).score_preneur_sans_annonces(difference_score_preneur,base);
					annonces_sans_petit_bout=((PartieTarot)par).annonces_sans_petit_bout();
					calcul_annonces_score_preneur=((PartieTarot)par).calcul_annonces_score_preneur(score_preneur_sans_annonces,annonces_sans_petit_bout);
					primes_supplementaires=((PartieTarot)par).primes_supplementaires();
					short somme_temporaire=((PartieTarot)par).somme_temporaire(score_preneur_sans_annonces,calcul_annonces_score_preneur,primes_supplementaires);
					coefficients_repartition=((PartieTarot)par).coefficients_repartition();
					gagne_nul_perd=((PartieTarot)par).gagne_nul_perd(score_preneur_sans_annonces);
					sous_panneau.add(new JLabel("nombre de Bouts en attaque dans les plis: "+nombre_bouts_preneur));
					sous_panneau.add(new JLabel("Nombre de points n"+ea+"cessaires pour que le preneur gagne: "+score_necessaire_preneur));
					sous_panneau.add(new JLabel("nombre de points en attaque dans les plis: "+score_preneur_plis_double/2.0));
					sous_panneau.add(new JLabel("Preneur:"+pseudos.get(preneur)));
					if(appele>-1)
					{
						sous_panneau.add(new JLabel("Appele:"+pseudos.get(appele)));
						Carte carteAppelee=((PartieTarot)par).getCarteAppelee();
						if(carteAppelee!=null)
						{
							sous_panneau.add(new JLabel("Carte appelee:"+carteAppelee));
						}
					}
					if(gagne_nul_perd==-1)
					{
						sous_panneau.add(new JLabel("Vous avez perdu"));
					}
					else if(gagne_nul_perd==0)
					{
						sous_panneau.add(new JLabel("Match nul"));
					}
					else
					{
						sous_panneau.add(new JLabel("Vous avez gagne"));
					}
					if(score_preneur_sans_annonces>0)
					{
						sous_panneau.add(new JLabel("Le contrat "+contrat+" est reussi de "+difference_score_preneur));
					}
					else if(score_preneur_sans_annonces<0)
					{
						sous_panneau.add(new JLabel("Le contrat "+contrat+" est chute de "+-difference_score_preneur));
					}
					if(primes_supplementaires[0]==new Annonce(Contrat.chelem).points())
					{
						sous_panneau.add(new JLabel("Chelem d attaque reussi et demande"));
					}
					else if(primes_supplementaires[0]==new Annonce(Contrat.chelem).points()/2)
					{
						sous_panneau.add(new JLabel("Chelem d attaque reussi et non demande"));
					}
					else if(primes_supplementaires[0]<0)
					{
						sous_panneau.add(new JLabel("Chelem d attaque chute et demande"));
					}
					if(primes_supplementaires[1]>0)
					{
						sous_panneau.add(new JLabel("Chelem de defense reussi"));
					}
					((PartieTarot)par).calculer_scores(coefficients_repartition, somme_temporaire, score_preneur_sans_annonces);
					scores_partie=par.getScores();
					sous_panneau.add(new JLabel("Score du preneur: "+scores_partie.get(preneur)+" points"));
					if(appele>-1)
					{
						sous_panneau.add(new JLabel("Score de l appele: "+scores_partie.get(appele)+" points"));
					}
					sous_panneau.add(new JLabel("Score d un defenseur: "+scores_partie.get(premier_defenseur)+" points"));
				}
				else if(par.getEtat()!=Etat.Contrat)
				{
					boolean pas_jeu_misere=((PartieTarot)par).pas_jeu_misere();
					if(pas_jeu_misere)
					{
						for(byte joueur=0;joueur<nombre_joueurs;joueur++)
						{
							scores_joueurs_plis_double[joueur]=((PartieTarot)par).score_joueur_plis_double(pli_petit, joueur);
							scores_necessaires_joueurs[joueur]=((PartieTarot)par).score_necessaire_joueur(joueur);
							differences_joueurs_double[joueur]=((PartieTarot)par).difference_joueur_double(scores_necessaires_joueurs[joueur],scores_joueurs_plis_double[joueur]);
							difference_max_double=(short)Math.max(difference_max_double,differences_joueurs_double[joueur]);
							points_annonces.addElement(((PartieTarot)par).calcul_annonces_score_joueur(joueur));
							prime_supplementaire[joueur]=((PartieTarot)par).prime_supplementaire(joueur);
						}
						difference_max=((PartieTarot)par).difference_max(difference_max_double);
						sous_panneau.add(new JLabel("Difference la plus grande: "+difference_max+" points"));
						positions=((PartieTarot)par).positions_difference(differences_joueurs_double);
						position_initiale_utilisateur=positions[0];
						sous_panneau.add(new JLabel("Votre position avant departage des joueurs: "+position_initiale_utilisateur));
						positions1=positions;
						((PartieTarot)par).changer_positions1(positions,pas_jeu_misere);
						positions2=positions;
						((PartieTarot)par).changer_positions2(positions,pas_jeu_misere);
						positions3=positions;
						((PartieTarot)par).changer_positions3(positions,pas_jeu_misere);
						positions4=positions;
						((PartieTarot)par).changer_positions4(positions,pas_jeu_misere);
						sous_panneau.add(new JLabel("Votre position apres departage des joueurs: "+positions[0]));
						coefficients=((PartieTarot)par).coefficients(positions);
						((PartieTarot)par).calculer_scores_joueurs(coefficients, points_annonces, difference_max_double, prime_supplementaire);
						scores_partie=par.getScores();
					}
					else
					{
						for(byte joueur=0;joueur<nombre_joueurs;joueur++)
						{
							scores_joueurs_plis_double[joueur]=((PartieTarot)par).score_joueur_plis_double(pli_petit, joueur);
							scores_necessaires_joueurs[joueur]=((PartieTarot)par).score_necessaire_joueur(joueur);
							differences_joueurs_double[joueur]=((PartieTarot)par).difference_joueur_double_misere(scores_necessaires_joueurs[joueur],scores_joueurs_plis_double[joueur]);
							difference_max_double=(short)Math.max(difference_max_double,differences_joueurs_double[joueur]);
						}
						difference_max=((PartieTarot)par).difference_max(difference_max_double);
						sous_panneau.add(new JLabel("Difference la plus grande: "+difference_max+" points"));
						positions=((PartieTarot)par).positions_difference(differences_joueurs_double);
						position_initiale_utilisateur=positions[0];
						sous_panneau.add(new JLabel("Votre position avant departage des joueurs: "+position_initiale_utilisateur));
						positions1=positions;
						((PartieTarot)par).changer_positions1(positions,pas_jeu_misere);
						positions2=positions;
						((PartieTarot)par).changer_positions2(positions,pas_jeu_misere);
						positions3=positions;
						((PartieTarot)par).changer_positions3(positions,pas_jeu_misere);
						positions4=positions;
						((PartieTarot)par).changer_positions4(positions,pas_jeu_misere);
						sous_panneau.add(new JLabel("Votre position apres departage des joueurs: "+positions[0]));
						coefficients=((PartieTarot)par).coefficients_misere(positions);
						((PartieTarot)par).calculer_scores_joueurs(coefficients,difference_max_double);
						scores_partie=par.getScores();
					}
				}
				else
				{
					byte joueur_petit_sec=((PartieTarot)par).joueurAyantPetitSec();
					if(joueur_petit_sec>-1)
					{
						sous_panneau.add(new JLabel("Joueur ayant le Petit Sec: "+pseudos.get(joueur_petit_sec)));
					}
					else
					{
						sous_panneau.add(new JLabel("Personne n a pris."));
					}
					for(byte joueur=0;joueur<nombre_joueurs;joueur++)
					{
						scores_partie.addElement((short)0);
					}
				}
				if(par.getEtat()!=Etat.Contrat)
				{
					String chaines[]=param.getInfos().get(tarot).get(3).split(";");
					((PartieTarot)par).restituerMainsDepart(plis_faits, pli_petit, nombre_joueurs,chaines[0].split(":")[1],chaines[1]);
				}
				panneau.add(sous_panneau);
				if(par.getType()==Type.Aleatoire&&par.getNombre()==0)
				{
					scores.addElement(new Vector<Long>());
					if(scores.size()==1)
					{
						for(short score:scores_partie)
						{
							scores.lastElement().addElement(new Long(score));
						}
					}
					else
					{
						byte indice=0;
						for(short score:scores_partie)
						{
							scores.lastElement().addElement(score+scores.get(scores.size()-2).get(indice));
							indice++;
						}
					}
					for(long score:scores.lastElement())
					{
						esperance+=score;
					}/*Somme des scores*/
					variance_9+=3*esperance;/*Somme des scores fois trois*/
					variance_9*=variance_9;/*Carre de la somme des scores fois trois (Le carre comprend le fois trois)*/
					variance_9=-variance_9;/*Oppose du carre de la somme des scores fois trois*/
					for(long score:scores.lastElement())
					{
						variance_9+=score*score*9*nombre_joueurs;
					}/*variance_9 vaut neuf fois la variance des scores fois le carre du nombre de joueurs*/
					sigmas.addElement(Math.sqrt(variance_9/(double)(nombre_joueurs*nombre_joueurs)));
					sommes.addElement(esperance);
				}
				sous_panneau=new JPanel(new GridLayout(0,nombre_joueurs+1,2,2));
				sous_panneau.setOpaque(true);
				sous_panneau.setBackground(Color.BLACK);
				etiquette=new JLabel();
				etiquette.setOpaque(true);
				etiquette.setBackground(Color.GRAY);
				sous_panneau.add(etiquette);
				for(String pseudo:pseudos)
				{
					etiquette=new JLabel(pseudo);
					etiquette.setHorizontalAlignment(JLabel.CENTER);
					etiquette.setOpaque(true);
					etiquette.setBackground(Color.GRAY);
					sous_panneau.add(etiquette);
				}
				for(int indice_partie=0;indice_partie<scores.size();indice_partie++)
				{
					etiquette=new JLabel(indice_partie+ch_v);
					etiquette.setHorizontalAlignment(JLabel.CENTER);
					etiquette.setOpaque(true);
					etiquette.setBackground(Color.GRAY);
					sous_panneau.add(etiquette);
					for(byte joueur=0;joueur<nombre_joueurs;joueur++)
					{
						etiquette=new JLabel(scores.get(indice_partie).get(joueur).toString());
						etiquette.setHorizontalAlignment(JLabel.CENTER);
						etiquette.setOpaque(true);
						etiquette.setBackground(Color.WHITE);
						sous_panneau.add(etiquette);
					}
				}
				ascenseur=new JScrollPane(sous_panneau);
				int limite=((JPanel)panneau.getComponent(0)).getComponentCount();
				if(scores.size()>limite&&scores.size()>10)
				{
					ascenseur.setPreferredSize(new Dimension(300,500));
				}
				panneau.add(ascenseur);
				onglets.add("R"+ea+"sultats de cette partie",panneau);
				if(contrat.force()>0)
				{
					panneau=new JPanel(new GridLayout(0,1));
					panneau.setBorder(BorderFactory.createTitledBorder("D"+ea+"tail des calculs"));
					sous_panneau=new JPanel(new GridLayout(0,1));
					sous_panneau.setBorder(BorderFactory.createTitledBorder("El"+ea+"ments du contrat"));
					String resultat="Score du preneur sans annonces: ( "+base+" ";
					sous_panneau.add(new JLabel("Base du contrat: "+base));
					byte joueur_petit_au_bout=((PartieTarot)par).joueur_petit_au_bout();
					if(joueur_petit_au_bout>-1)
					{
						sous_panneau.add(new JLabel(Annonce.petit_au_bout+": "+pseudos.get(joueur_petit_au_bout)));
						if(difference_score_preneur!=0)
						{
							if(((PartieTarot)par).a_pour_defenseur(joueur_petit_au_bout))
							{
								resultat+="+ (-"+new Annonce(Annonce.petit_au_bout).points()+") ";
							}
							else
							{
								resultat+="+ "+new Annonce(Annonce.petit_au_bout).points()+" ";
							}
						}
					}
					sous_panneau.add(new JLabel("Diff"+ea+"rence entre les points n"+ea+"cessaires et les points r"+ea+"alis"+ea+"s: "+difference_score_preneur));
					if(difference_score_preneur<0)
					{
						resultat+="+ ("+difference_score_preneur+") ) * ";
					}
					else
					{
						resultat+="+ "+difference_score_preneur+" ) * ";
					}
					sous_panneau.add(new JLabel("Coefficient du contrat: "+contrat.coefficients()));
					resultat+=contrat.coefficients()+" = "+score_preneur_sans_annonces*contrat.coefficients()+" points";
					sous_panneau.add(new JLabel(resultat));
					panneau.add(sous_panneau);
					boolean existe_annonce=false;
					for(Vector<Annonce> annonces_joueur:annonces_sans_petit_bout)
					{
						existe_annonce|=!annonces_joueur.isEmpty();
					}
					if(existe_annonce)
					{
						sous_panneau=new JPanel(new GridLayout(0,1));
						sous_panneau.setBorder(BorderFactory.createTitledBorder("Annonces"));
						byte joueur=0;
						byte indice=0;
						short points_annonces_joueur=0;
						short somme_points_annonces=0;
						for(Vector<Annonce> annonces_joueur:annonces_sans_petit_bout)
						{
							indice=0;
							points_annonces_joueur=0;
							sous_panneau2=new JPanel(new GridLayout(0,1));
							sous_panneau2.setBorder(BorderFactory.createTitledBorder("Annonces de "+pseudos.get(joueur)));
							for(Annonce annonce:annonces_joueur)
							{
								points_annonces_joueur+=calcul_annonces_score_preneur.get(joueur).get(indice);
								sous_panneau2.add(new JLabel("Valeur de "+annonce+" de "+pseudos.get(joueur)+" ("+((PartieTarot)par).statut_de(joueur)+"): "+calcul_annonces_score_preneur.get(joueur).get(indice)));
								indice++;
							}
							somme_points_annonces+=points_annonces_joueur;
							sous_panneau2.add(new JLabel("Somme des annonces de "+pseudos.get(joueur)+" ("+((PartieTarot)par).statut_de(joueur)+"): "+points_annonces_joueur));
							sous_panneau.add(sous_panneau2);
							joueur++;
						}
						sous_panneau.add(new JLabel("Somme des annonces des joueurs :"+somme_points_annonces));
						panneau.add(sous_panneau);
					}
					if(primes_supplementaires[0]!=0||primes_supplementaires[1]!=0)
					{
						sous_panneau=new JPanel(new GridLayout(0,1));
						sous_panneau.setBorder(BorderFactory.createTitledBorder("Primes suppl"+ea+"mentaires"));
						if(primes_supplementaires[0]==new Annonce(Contrat.chelem).points())
						{
							sous_panneau.add(new JLabel("Chelem d attaque reussi et demande: "+primes_supplementaires[0]+" points"));
						}
						else if(primes_supplementaires[0]==new Annonce(Contrat.chelem).points()/2)
						{
							sous_panneau.add(new JLabel("Chelem d attaque reussi et non demande: "+primes_supplementaires[0]+" points"));
						}
						else if(primes_supplementaires[0]<0)
						{
							sous_panneau.add(new JLabel("Chelem d attaque chute et demande: "+primes_supplementaires[0]+" points"));
						}
						if(primes_supplementaires[1]>0)
						{
							sous_panneau.add(new JLabel("Chelem de defense reussi: "+-primes_supplementaires[1]+" points"));
						}
						sous_panneau.add(new JLabel("Somme des primes supplementaires des joueurs :"+(primes_supplementaires[0]-primes_supplementaires[1])));
						panneau.add(sous_panneau);
					}
					sous_panneau=new JPanel(new GridLayout(0,1));
					sous_panneau.setBorder(BorderFactory.createTitledBorder("Coefficient de score pour chaque joueur"));
					if(coefficients_repartition[0].length>1&&coefficients_repartition[0][1]==2)
					{
						for(byte joueur=0;joueur<nombre_joueurs;joueur++)
						{
							if(joueur==preneur)
							{
								sous_panneau.add(new JLabel("Coefficient de r"+ea+"partition de "+pseudos.get(joueur)+": "+coefficients_repartition[0][0]/(double)coefficients_repartition[0][1]));
							}
							else if(joueur==appele)
							{
								sous_panneau.add(new JLabel("Coefficient de r"+ea+"partition de "+pseudos.get(joueur)+": "+coefficients_repartition[1][0]/(double)coefficients_repartition[0][1]));
							}
							else
							{
								sous_panneau.add(new JLabel("Coefficient de r"+ea+"partition de "+pseudos.get(joueur)+": "+coefficients_repartition[coefficients_repartition.length-1][0]/(double)coefficients_repartition[0][1]));
							}
						}
					}
					else
					{
						for(byte joueur=0;joueur<nombre_joueurs;joueur++)
						{
							if(joueur==preneur)
							{
								sous_panneau.add(new JLabel("Coefficient de r"+ea+"partition de "+pseudos.get(joueur)+": "+coefficients_repartition[0][0]));
							}
							else if(joueur==appele)
							{
								sous_panneau.add(new JLabel("Coefficient de r"+ea+"partition de "+pseudos.get(joueur)+": "+coefficients_repartition[1][0]));
							}
							else
							{
								sous_panneau.add(new JLabel("Coefficient de r"+ea+"partition de "+pseudos.get(joueur)+": "+coefficients_repartition[coefficients_repartition.length-1][0]));
							}
						}
					}
					panneau.add(sous_panneau);
					sous_panneau=new JPanel(new GridLayout(0,1));
					sous_panneau.setBorder(BorderFactory.createTitledBorder("Score pour chaque joueur"));
					byte joueur=0;
					for(short score:scores_partie)
					{
						sous_panneau.add(new JLabel("Score de "+pseudos.get(joueur)+": "+score));
						joueur++;
					}
					panneau.add(sous_panneau);
					ascenseur=new JScrollPane(panneau);
					ascenseur.setPreferredSize(new Dimension(700,700));
					onglets.add("Calcul des scores",ascenseur);
				}
				else if(par.getEtat()!=Etat.Contrat)
				{
					panneau=new JPanel(new GridLayout(0,1));
					panneau.setBorder(BorderFactory.createTitledBorder("D"+ea+"tail des calculs"));
					sous_panneau=new JPanel(new GridLayout(0,1));
					sous_panneau.setBorder(BorderFactory.createTitledBorder("Positions des joueurs liees aux differences de scores"));
					for(byte joueur=0;joueur<nombre_joueurs;joueur++)
					{
						sous_panneau.add(new JLabel("Position de "+pseudos.get(joueur)+": "+positions1[joueur]));
					}
					panneau.add(sous_panneau);
					sous_panneau=new JPanel(new GridLayout(0,1));
					sous_panneau.setBorder(BorderFactory.createTitledBorder("Positions des joueurs liees au nombre de bouts"));
					for(byte joueur=0;joueur<nombre_joueurs;joueur++)
					{
						sous_panneau.add(new JLabel("Position de "+pseudos.get(joueur)+": "+positions2[joueur]));
					}
					panneau.add(sous_panneau);
					sous_panneau=new JPanel(new GridLayout(0,1));
					sous_panneau.setBorder(BorderFactory.createTitledBorder("Positions des joueurs liees au nombre de figures"));
					for(byte joueur=0;joueur<nombre_joueurs;joueur++)
					{
						sous_panneau.add(new JLabel("Position de "+pseudos.get(joueur)+": "+positions3[joueur]));
					}
					panneau.add(sous_panneau);
					sous_panneau=new JPanel(new GridLayout(0,1));
					sous_panneau.setBorder(BorderFactory.createTitledBorder("Positions des joueurs liees a la hauteur des figures"));
					for(byte joueur=0;joueur<nombre_joueurs;joueur++)
					{
						sous_panneau.add(new JLabel("Position de "+pseudos.get(joueur)+": "+positions4[joueur]));
					}
					panneau.add(sous_panneau);
					sous_panneau=new JPanel(new GridLayout(0,1));
					sous_panneau.setBorder(BorderFactory.createTitledBorder("Positions des joueurs finales"));
					for(byte joueur=0;joueur<nombre_joueurs;joueur++)
					{
						sous_panneau.add(new JLabel("Position de "+pseudos.get(joueur)+": "+positions[joueur]));
					}
					panneau.add(sous_panneau);
					sous_panneau=new JPanel(new GridLayout(0,1));
					sous_panneau.setBorder(BorderFactory.createTitledBorder("Difference de scores"));
					for(byte joueur=0;joueur<nombre_joueurs;joueur++)
					{
						sous_panneau.add(new JLabel("Points marques dans les plis de "+pseudos.get(joueur)+": "+scores_joueurs_plis_double[joueur]/(double)2));
					}
					panneau.add(sous_panneau);
					sous_panneau=new JPanel(new GridLayout(0,1));
					sous_panneau.setBorder(BorderFactory.createTitledBorder("Scores limites pour chaque joueur"));
					for(byte joueur=0;joueur<nombre_joueurs;joueur++)
					{
						sous_panneau.add(new JLabel("Score limite de "+pseudos.get(joueur)+": "+scores_necessaires_joueurs[joueur]));
					}
					panneau.add(sous_panneau);
					sous_panneau=new JPanel(new GridLayout(0,1));
					sous_panneau.setBorder(BorderFactory.createTitledBorder("Difference de scores"));
					for(byte joueur=0;joueur<nombre_joueurs;joueur++)
					{
						sous_panneau.add(new JLabel("Difference de score de "+pseudos.get(joueur)+": "+differences_joueurs_double[joueur]/(double)2));
					}
					panneau.add(sous_panneau);
					sous_panneau=new JPanel(new GridLayout(0,1));
					sous_panneau.setBorder(BorderFactory.createTitledBorder("Coefficients"));
					for(byte joueur=0;joueur<nombre_joueurs;joueur++)
					{
						sous_panneau.add(new JLabel("Coefficient de "+pseudos.get(joueur)+": "+coefficients[joueur]));
					}
					panneau.add(sous_panneau);
					if(((PartieTarot)par).pas_jeu_misere())
					{
						sous_panneau=new JPanel(new GridLayout(0,1));
						sous_panneau.setBorder(BorderFactory.createTitledBorder("Annonces et primes"));
						for(byte joueur=0;joueur<nombre_joueurs;joueur++)
						{
							sous_panneau2=new JPanel(new GridLayout(0,1));
							sous_panneau2.setBorder(BorderFactory.createTitledBorder("Annonces de "+pseudos.get(joueur)));
							byte indice=0;
							for(short point_annonce:points_annonces.get(joueur).get(joueur))
							{
								sous_panneau2.add(new JLabel("Valeur de "+((PartieTarot)par).getAnnonces(joueur).get(indice)+": "+point_annonce));
								indice++;
							}
							sous_panneau.add(sous_panneau2);
						}
						panneau.add(sous_panneau);
						sous_panneau=new JPanel(new GridLayout(0,1));
						sous_panneau.setBorder(BorderFactory.createTitledBorder("Primes supplementaires"));
						for(byte joueur=0;joueur<nombre_joueurs;joueur++)
						{
							sous_panneau.add(new JLabel("Prime supplementaire de "+pseudos.get(joueur)+": "+prime_supplementaire[joueur]));
						}
						panneau.add(sous_panneau);
					}
					sous_panneau=new JPanel(new GridLayout(0,1));
					sous_panneau.setBorder(BorderFactory.createTitledBorder("Score pour chaque joueur"));
					byte joueur=0;
					for(short score:scores_partie)
					{
						sous_panneau.add(new JLabel("Score de "+pseudos.get(joueur)+": "+score));
						joueur++;
					}
					panneau.add(sous_panneau);
					ascenseur=new JScrollPane(panneau);
					ascenseur.setPreferredSize(new Dimension(700,700));
					onglets.add("Calcul des scores",ascenseur);
				}
				if(par.getType()==Type.Aleatoire)
				{
					Vector<Color> couleurs=new Vector<Color>();
					couleurs.addElement(Color.RED);
					couleurs.addElement(Color.GREEN);
					couleurs.addElement(Color.BLUE);
					if(nombre_joueurs>3)
					{
						couleurs.addElement(Color.YELLOW);
					}
					if(nombre_joueurs>4)
					{
						couleurs.addElement(Color.MAGENTA);
					}
					if(nombre_joueurs>5)
					{
						couleurs.addElement(Color.CYAN);
					}
					if(nombre_joueurs>6)
					{
						couleurs.addElement(Color.ORANGE);
					}
					if(nombre_joueurs>7)
					{
						couleurs.addElement(new Color(128,64,0));
					}
					if(nombre_joueurs>8)
					{
						couleurs.addElement(new Color(128,128,0));
					}
					if(nombre_joueurs>9)
					{
						couleurs.addElement(new Color(128,0,255));
					}
					Graphique graphique=new Graphique(scores,sommes,sigmas,couleurs);
					double derniere_moyenne=sommes.lastElement()/(double)nombre_joueurs;
					Vector<Double> scores_centres_moyenne=new Vector<Double>();
					for(byte joueur=0;joueur<nombre_joueurs;joueur++)
					{
						scores_centres_moyenne.addElement(scores.lastElement().get(joueur)-derniere_moyenne);
					}
					scores_centres_moyenne.addElement(3*sigmas.lastElement());
					for(double maximum:scores_centres_moyenne)
					{
						max_absolu_score=Math.max(Math.abs(maximum),max_absolu_score);
					}
					int dimy=(int)max_absolu_score;
					graphique.setPreferredSize(new Dimension(2000,dimy));
					ascenseur=new JScrollPane(graphique);
					graphique.setLocation(0,(600-dimy)/2);
					ascenseur.setPreferredSize(new Dimension(700,600));
					panneau=new JPanel(new BorderLayout());
					panneau.add(new JLabel("Evolution des scores centr"+ea+"s par rapport a la moyenne et des ecarts types multiplies par plus ou moins trois en fonction du temps",JLabel.CENTER),BorderLayout.NORTH);
					panneau.add(ascenseur,BorderLayout.CENTER);
					Legende legende=new Legende(pseudos,couleurs);
					legende.setPreferredSize(new Dimension(300,15*(nombre_joueurs+1)));
					ascenseur=new JScrollPane(legende);
					ascenseur.setPreferredSize(new Dimension(300,100));
					panneau.add(ascenseur,BorderLayout.SOUTH);
					onglets.add("Courbes temporelles",panneau);
				}
				panneau=new JPanel(new BorderLayout());
				sous_panneau=new JPanel();
				sous_panneau.add(new JPanel(new GridLayout(0,1)));
				sous_panneau.add(new JPanel(new GridLayout(0,1)));
				sous_panneau2=new JPanel(new GridLayout(0,1));
				JPanel sous_panneau3;
				for(byte joueur=0;joueur<nombre_joueurs;joueur++)
				{
					sous_panneau3=new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
					for(byte indice=0;indice<par.getDistribution().main(joueur).total();indice++)
					{
						CarteGraphique carte_graphique=new CarteGraphique(par.getDistribution().main(joueur).carte(indice),JLabel.RIGHT,indice!=0);
						if(indice>0)
						{
							carte_graphique.setPreferredSize(new Dimension(25,150));
						}
						else
						{
							carte_graphique.setPreferredSize(new Dimension(100,150));
						}
						sous_panneau3.add(carte_graphique);
					}
					sous_panneau2.add(sous_panneau3);
				}
				for(byte joueur=0;joueur<nombre_joueurs-1;joueur++)
				{
					sous_panneau2.add(new JPanel(new FlowLayout(FlowLayout.LEFT,0,0)));
				}
				sous_panneau.add(sous_panneau2);
				sous_panneau2=new JPanel(new GridLayout(0,1));
				sous_panneau3=new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
				for(byte indice=0;indice<par.getDistribution().derniereMain().total();indice++)
				{
					CarteGraphique carte_graphique=new CarteGraphique(par.getDistribution().derniereMain().carte(indice),JLabel.RIGHT,indice!=0);
					if(indice>0)
					{
						carte_graphique.setPreferredSize(new Dimension(25,150));
					}
					else
					{
						carte_graphique.setPreferredSize(new Dimension(100,150));
					}
					sous_panneau3.add(carte_graphique);
				}
				sous_panneau2.add(sous_panneau3);
				sous_panneau3=new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
				for(byte indice=0;indice<plis_faits.get(0).getCartes().total();indice++)
				{
					CarteGraphique carte_graphique=new CarteGraphique(plis_faits.get(0).getCartes().carte(indice),JLabel.RIGHT,indice!=0);
					if(indice>0)
					{
						carte_graphique.setPreferredSize(new Dimension(25,150));
					}
					else
					{
						carte_graphique.setPreferredSize(new Dimension(100,150));
					}
					sous_panneau3.add(carte_graphique);
				}
				sous_panneau2.add(sous_panneau3);
				sous_panneau.add(sous_panneau2);
				ascenseur=new JScrollPane(sous_panneau);
				ascenseur.setPreferredSize(new Dimension(700,700));
				panneau.add(ascenseur,BorderLayout.CENTER);
				sous_panneau=new JPanel();
				sous_panneau.add(new JLabel("Pli n°"));
				Object[] numeros_plis;
				if(pli_petit!=null&&pli_petit.total()==1)
				{
					numeros_plis=new Object[plis_faits.size()];
					for(byte indice_pli=0;indice_pli<numeros_plis.length;indice_pli++)
					{
						numeros_plis[indice_pli]=indice_pli-1;
					}
				}
				else
				{
					numeros_plis=new Object[plis_faits.size()+1];
					for(byte indice_pli=0;indice_pli<numeros_plis.length;indice_pli++)
					{
						numeros_plis[indice_pli]=indice_pli-1;
					}
				}
				JComboBox liste=new JComboBox(numeros_plis);
				liste.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent event)
					{
						JTabbedPane onglets2=(JTabbedPane)getContentPane().getComponent(0);
						JPanel onglet=(JPanel)onglets2.getComponentAt(onglets2.getComponentCount()-1);
						JPanel cartes=(JPanel)((JScrollPane)onglet.getComponent(0)).getViewport().getComponent(0);
						byte numero_selectionne=Byte.parseByte(((JComboBox)((JPanel)onglet.getComponent(1)).getComponent(1)).getSelectedItem().toString());
						byte numero_pli=((PartieTarot)par).numero_pli(pli_petit, numero_selectionne);
						if(par.getEtat()!=Etat.Contrat)
						{
							String chaines[]=param.getInfos().get(tarot).get(3).split(";");
							((PartieTarot)par).restituerMains(plis_faits, pli_petit, nombre_joueurs,chaines[0].split(":")[1],chaines[1], numero_pli);
						}
						byte nombre_excuse=0;
						for(byte indice_pli=(byte)(plis_faits.size()-1);indice_pli>0;indice_pli--)
						{
							Pli pli=plis_faits.get(indice_pli);
							if(pli.contient(new CarteTarot((byte)0)))
							{
								nombre_excuse++;
								if(nombre_excuse>1)
								{
									pli.getCartes().jouer(new CarteTarot((byte)0));
								}
							}
						}
						JPanel mains_graphiques=(JPanel)cartes.getComponent(2);
						mains_graphiques.removeAll();
						mains_graphiques.setLayout(new GridLayout(0,1));
						for(byte joueur=0;joueur<nombre_joueurs;joueur++)
						{
							JPanel sous_panneau4=new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
							for(byte indice=0;indice<par.getDistribution().main(joueur).total();indice++)
							{
								CarteGraphique carte_graphique=new CarteGraphique(par.getDistribution().main(joueur).carte(indice),JLabel.RIGHT,indice!=0);
								if(indice>0)
								{
									carte_graphique.setPreferredSize(new Dimension(25,150));
								}
								else
								{
									carte_graphique.setPreferredSize(new Dimension(100,150));
								}
								sous_panneau4.add(carte_graphique);
							}
							mains_graphiques.add(sous_panneau4);
						}
						for(byte joueur=0;joueur<nombre_joueurs-1;joueur++)
						{
							mains_graphiques.add(new JPanel(new FlowLayout(FlowLayout.LEFT,0,0)));
						}
						mains_graphiques=(JPanel)cartes.getComponent(1);
						mains_graphiques.removeAll();
						if(numero_pli>0)
						{
							mains_graphiques.setLayout(new GridLayout(0,1));
							byte entameur=plis_faits.get(numero_pli).getEntameur();
							byte indice=0;
							for(;indice<entameur;indice++)
							{
								JLabel etiquette2=new JLabel(ch_v+indice);
								etiquette2.setHorizontalAlignment(JLabel.CENTER);
								etiquette2.setFont(new Font("Default",Font.BOLD,50));
								etiquette2.setOpaque(true);
								etiquette2.setBackground(Color.WHITE);
								mains_graphiques.add(etiquette2);
							}
							for(Carte carte:plis_faits.get(numero_pli))
							{
								CarteGraphique carte_graphique_2=new CarteGraphique(carte,JLabel.RIGHT,false);
								carte_graphique_2.setPreferredSize(new Dimension(100,150));
								mains_graphiques.add(carte_graphique_2);
								indice++;
							}
							if(plis_faits.get(numero_pli).total()==nombre_joueurs-1)
							{
								byte entameur2=plis_faits.get(numero_pli+1).getEntameur();
								if(entameur2>=entameur)
								{
									CarteGraphique carte_graphique_2=new CarteGraphique(plis_faits.get(numero_pli+1).carte(0),JLabel.RIGHT,false);
									carte_graphique_2.setPreferredSize(new Dimension(100,150));
									mains_graphiques.add(carte_graphique_2,entameur2);
								}
								else
								{
									CarteGraphique carte_graphique_2=new CarteGraphique(plis_faits.get(numero_pli+1).carte(0),JLabel.RIGHT,false);
									carte_graphique_2.setPreferredSize(new Dimension(100,150));
									mains_graphiques.add(carte_graphique_2,entameur2+nombre_joueurs);
								}
								indice++;
							}
							for(;indice<2*nombre_joueurs-1;indice++)
							{
								JLabel etiquette2=new JLabel(ch_v+(indice-nombre_joueurs));
								etiquette2.setHorizontalAlignment(JLabel.CENTER);
								etiquette2.setFont(new Font("Default",Font.BOLD,50));
								etiquette2.setOpaque(true);
								etiquette2.setBackground(Color.WHITE);
								mains_graphiques.add(etiquette2);
							}
						}
						mains_graphiques=(JPanel)cartes.getComponent(0);
						mains_graphiques.removeAll();
						if(numero_pli>1)
						{
							if(numero_pli==numero_selectionne)
							{
								mains_graphiques.setLayout(new GridLayout(0,numero_pli-1));
							}
							else if(numero_pli==numero_selectionne+1&&numero_selectionne>1)
							{
								mains_graphiques.setLayout(new GridLayout(0,numero_selectionne-1));
							}
						}
						boolean passe2=false;
						for(byte indice_pli=1;indice_pli<numero_pli;indice_pli++)
						{
							byte entameur=plis_faits.get(indice_pli).getEntameur();
							byte indice=0;
							if(plis_faits.get(indice_pli).total()==nombre_joueurs-1)
							{
								for(indice=0;indice<entameur;indice++)
								{
									JLabel etiquette2=new JLabel(ch_v+indice);
									etiquette2.setHorizontalAlignment(JLabel.CENTER);
									etiquette2.setFont(new Font("Default",Font.BOLD,50));
									etiquette2.setOpaque(true);
									etiquette2.setBackground(Color.WHITE);
									mains_graphiques.add(etiquette2,indice_pli*(indice+1)-1);
								}
								Pli pli_excuse=new Vector<Pli>(plis_faits).get(indice_pli);
								byte entameur2=plis_faits.get(indice_pli+1).getEntameur();
								Carte excuse=plis_faits.get(indice_pli+1).carte(0);
								if(entameur2>=entameur)
								{
									pli_excuse.getCartes().ajouter(excuse,entameur2-entameur);
								}
								else
								{
									pli_excuse.getCartes().ajouter(excuse,entameur2-entameur+nombre_joueurs);
								}
								for(Carte carte:pli_excuse)
								{
									CarteGraphique carte_graphique_2=new CarteGraphique(carte,JLabel.RIGHT,false);
									carte_graphique_2.setPreferredSize(new Dimension(100,150));
									mains_graphiques.add(carte_graphique_2,indice_pli*(indice+1)-1);
									indice++;
								}
								for(;indice<nombre_joueurs*2-1;indice++)
								{
									JLabel etiquette2=new JLabel(ch_v+(indice-nombre_joueurs));
									etiquette2.setHorizontalAlignment(JLabel.CENTER);
									etiquette2.setFont(new Font("Default",Font.BOLD,50));
									etiquette2.setOpaque(true);
									etiquette2.setBackground(Color.WHITE);
									mains_graphiques.add(etiquette2,indice_pli*(indice+1)-1);
								}
								passe2=true;
							}
							else if(plis_faits.get(indice_pli).total()==nombre_joueurs)
							{
								if(!passe2)
								{
									for(indice=0;indice<entameur;indice++)
									{
										JLabel etiquette2=new JLabel(ch_v+indice);
										etiquette2.setHorizontalAlignment(JLabel.CENTER);
										etiquette2.setFont(new Font("Default",Font.BOLD,50));
										etiquette2.setOpaque(true);
										etiquette2.setBackground(Color.WHITE);
										mains_graphiques.add(etiquette2,indice_pli*(indice+1)-1);
									}
									for(Carte carte:plis_faits.get(indice_pli))
									{
										CarteGraphique carte_graphique_2=new CarteGraphique(carte,JLabel.RIGHT,false);
										carte_graphique_2.setPreferredSize(new Dimension(100,150));
										mains_graphiques.add(carte_graphique_2,indice_pli*(indice+1)-1);
										indice++;
									}
									for(;indice<nombre_joueurs*2-1;indice++)
									{
										JLabel etiquette2=new JLabel(ch_v+(indice-nombre_joueurs));
										etiquette2.setHorizontalAlignment(JLabel.CENTER);
										etiquette2.setFont(new Font("Default",Font.BOLD,50));
										etiquette2.setOpaque(true);
										etiquette2.setBackground(Color.WHITE);
										mains_graphiques.add(etiquette2,indice_pli*(indice+1)-1);
									}
								}
								else
								{
									for(indice=0;indice<entameur;indice++)
									{
										JLabel etiquette2=new JLabel(ch_v+indice);
										etiquette2.setHorizontalAlignment(JLabel.CENTER);
										etiquette2.setFont(new Font("Default",Font.BOLD,50));
										etiquette2.setOpaque(true);
										etiquette2.setBackground(Color.WHITE);
										mains_graphiques.add(etiquette2,(indice_pli-1)*(indice+1)-1);
									}
									for(Carte carte:plis_faits.get(indice_pli))
									{
										CarteGraphique carte_graphique_2=new CarteGraphique(carte,JLabel.RIGHT,false);
										carte_graphique_2.setPreferredSize(new Dimension(100,150));
										mains_graphiques.add(carte_graphique_2,(indice_pli-1)*(indice+1)-1);
										indice++;
									}
									for(;indice<nombre_joueurs*2-1;indice++)
									{
										JLabel etiquette2=new JLabel(ch_v+(indice-nombre_joueurs));
										etiquette2.setHorizontalAlignment(JLabel.CENTER);
										etiquette2.setFont(new Font("Default",Font.BOLD,50));
										etiquette2.setOpaque(true);
										etiquette2.setBackground(Color.WHITE);
										mains_graphiques.add(etiquette2,(indice_pli-1)*(indice+1)-1);
									}
								}
							}
						}
						pack();
					}
				}
				);
				sous_panneau.add(liste);
				sous_panneau.add(new JLabel("Carte n°"));
				Object[] numeros_joueurs=new Object[nombre_joueurs];
				for(byte indice_joueur=0;indice_joueur<numeros_joueurs.length;indice_joueur++)
				{
					numeros_joueurs[indice_joueur]=indice_joueur+1;
				}
				liste=new JComboBox(numeros_joueurs);
				liste.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent event)
					{
						JTabbedPane onglets2=(JTabbedPane)getContentPane().getComponent(0);
						JPanel onglet=(JPanel)onglets2.getComponentAt(onglets2.getComponentCount()-1);
						JPanel cartes=(JPanel)((JScrollPane)onglet.getComponent(0)).getViewport().getComponent(0);
						JPanel panneau_bas=(JPanel)onglet.getComponent(1);
						byte numero_selectionne=Byte.parseByte(((JComboBox)panneau_bas.getComponent(1)).getSelectedItem().toString());
						byte numero_pli=((PartieTarot)par).numero_pli(pli_petit, numero_selectionne);
						if(numero_pli<1)
						{
							return;
						}
						byte numero_carte=Byte.parseByte(((JComboBox)panneau_bas.getComponent(3)).getSelectedItem().toString());
						numero_carte--;
						String chaines[]=param.getInfos().get(tarot).get(3).split(";");
						((PartieTarot)par).restituerMains(plis_faits, pli_petit, nombre_joueurs,chaines[0].split(":")[1],chaines[1], numero_pli,numero_carte);
						byte nombre_excuse=0;
						for(byte indice_pli=(byte)(plis_faits.size()-1);indice_pli>0;indice_pli--)
						{
							Pli pli=plis_faits.get(indice_pli);
							if(pli.contient(new CarteTarot((byte)0)))
							{
								nombre_excuse++;
								if(nombre_excuse>1)
								{
									pli.getCartes().jouer(new CarteTarot((byte)0));
								}
							}
						}
						JPanel mains_graphiques=(JPanel)cartes.getComponent(2);
						mains_graphiques.removeAll();
						mains_graphiques.setLayout(new GridLayout(0,1));
						for(byte joueur=0;joueur<nombre_joueurs;joueur++)
						{
							JPanel sous_panneau4=new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
							for(byte indice=0;indice<par.getDistribution().main(joueur).total();indice++)
							{
								CarteGraphique carte_graphique=new CarteGraphique(par.getDistribution().main(joueur).carte(indice),JLabel.RIGHT,indice!=0);
								if(indice>0)
								{
									carte_graphique.setPreferredSize(new Dimension(25,150));
								}
								else
								{
									carte_graphique.setPreferredSize(new Dimension(100,150));
								}
								sous_panneau4.add(carte_graphique);
							}
							mains_graphiques.add(sous_panneau4);
						}
						for(byte joueur=0;joueur<nombre_joueurs-1;joueur++)
						{
							mains_graphiques.add(new JPanel(new FlowLayout(FlowLayout.LEFT,0,0)));
						}
						mains_graphiques=(JPanel)cartes.getComponent(1);
						mains_graphiques.removeAll();
						if(numero_pli>0)
						{
							mains_graphiques.setLayout(new GridLayout(0,1));
							byte entameur=plis_faits.get(numero_pli).getEntameur();
							byte indice=0;
							byte indice2=0;
							for(;indice<entameur;indice++)
							{
								JLabel etiquette2=new JLabel(ch_v+indice);
								etiquette2.setHorizontalAlignment(JLabel.CENTER);
								etiquette2.setFont(new Font("Default",Font.BOLD,50));
								etiquette2.setOpaque(true);
								etiquette2.setBackground(Color.WHITE);
								mains_graphiques.add(etiquette2);
							}
							if(plis_faits.get(numero_pli).total()==nombre_joueurs)
							{
								for(Carte carte:plis_faits.get(numero_pli))
								{
									if(indice2<=numero_carte)
									{
										CarteGraphique carte_graphique_2=new CarteGraphique(carte,JLabel.RIGHT,false);
										carte_graphique_2.setPreferredSize(new Dimension(100,150));
										mains_graphiques.add(carte_graphique_2);
										indice++;
										indice2++;
									}
									else
									{
										break;
									}
								}
							}
							else
							{
								Pli pli_excuse=new Vector<Pli>(plis_faits).get(numero_pli);
								byte entameur2=plis_faits.get(numero_pli+1).getEntameur();
								Carte excuse=plis_faits.get(numero_pli+1).carte(0);
								if(entameur2>=entameur)
								{
									pli_excuse.getCartes().ajouter(excuse,entameur2-entameur);
								}
								else
								{
									pli_excuse.getCartes().ajouter(excuse,entameur2-entameur+nombre_joueurs);
								}
								for(Carte carte:pli_excuse)
								{
									if(indice2<=numero_carte)
									{
										CarteGraphique carte_graphique_2=new CarteGraphique(carte,JLabel.RIGHT,false);
										carte_graphique_2.setPreferredSize(new Dimension(100,150));
										mains_graphiques.add(carte_graphique_2);
										indice++;
										indice2++;
									}
									else
									{
										break;
									}
								}
							}
							for(;indice<2*nombre_joueurs-1;indice++)
							{
								JLabel etiquette2=new JLabel(ch_v+(indice-nombre_joueurs));
								etiquette2.setHorizontalAlignment(JLabel.CENTER);
								etiquette2.setFont(new Font("Default",Font.BOLD,50));
								etiquette2.setOpaque(true);
								etiquette2.setBackground(Color.WHITE);
								mains_graphiques.add(etiquette2);
							}
						}
						pack();
					}
				}
				);
				sous_panneau.add(liste);
				panneau.add(sous_panneau,BorderLayout.SOUTH);
				onglets.add("Mains et plis",panneau);
			}
			container.add(onglets,BorderLayout.CENTER);
			panneau=new JPanel();
			if(par.getType()==Type.Editer&&par.getNombre()<Byte.parseByte(par.getInfos().get(1).split(":")[1]))
			{
				ajouterBoutonFinDePartie(panneau,"Continuer de jouer les parties editees");
			}
			else if(par.getType()==Type.Editer&&par.getNombre()==Byte.parseByte(par.getInfos().get(1).split(":")[1])&&partie_aleatoire_jouee||par.getType()==Type.Aleatoire)
			{
				ajouterBoutonFinDePartie(panneau,"Continuer sur le jeu actuel");
			}
			ajouterBoutonFinDePartie(panneau,"Rejouer donne");
			ajouterBoutonFinDePartie(panneau,"Arreter");
			container.add(panneau,BorderLayout.SOUTH);
		}
		if(par.getType()!=Type.Editer)
		{
			par.setNombre();
		}
		setContentPane(container);
		pack();
	}
	private void changerNombreDeParties2() {
		Vector<Long> vl=new Vector<Long>();
		File fichier=new File(Fichier.dossier_paquets+File.separator+Fichier.fichier_paquet);
		int total=2;
		try {
			BufferedReader br=new BufferedReader(new FileReader(fichier));
			for(int indice=0;indice<total;indice++)
				vl.addElement(Long.parseLong(br.readLine()));
			br.close();
		} catch (Exception exc) {
			vl=new Vector<Long>();
			for (int indice = 0; indice < total; indice++)
				vl.addElement((long)0);
		}
		//Si l'action de battre les cartes est faite a chaque lancement
		//de logiciel alors le nombre de parties est remis a zero lors
		//d'une fermeture de logiciel
		if(par instanceof PartieBelote)
		{
			vl.setElementAt(par.getDistribution().getNombreDeParties()+1,0);
		}
		if(par instanceof PartieTarot)
		{
			vl.setElementAt(par.getDistribution().getNombreDeParties()+1,total-1);
		}
		try {
			BufferedWriter bw=new BufferedWriter(new FileWriter(fichier));
			for(int indice=0;indice<total;indice++)
			{
				bw.write(vl.get(indice).toString());
				bw.newLine();
			}
			bw.close();
		} catch (Exception exc) {}
	}
	private void editerTarot()
	{
		changer_pile_fin=false;
		mettre_en_place_ihm_tarot();
	}
	private void modifierJeuTarot()
	{
		enCoursDePartie=true;
		Pli.nombreTotal=0;
		MainTarot pile;
		/*Chargement de la pile de cartes depuis un fichier sinon on la cree*/
		try {
			pile = (MainTarot) chargerPile();
		} catch (Exception exc) {
			exc.printStackTrace();
			pile=new MainTarot();
			for (byte carte = 0; carte < 78; carte++) {
				pile.ajouter(new CarteTarot(carte));
			}
		}
		/*Chargement du nombre de parties jouees depuis le lancement du logiciel*/
		long nb;
		try {
			nb=chargerNombreDeParties();
		} catch (Exception exc) {
			nb=0;
			exc.printStackTrace();
		}
		Donne donne;
		if(nb==0||par==null)
		{
			donne=new Donne(mode,param.getInfos().get(Jeu.Tarot.ordinal()+1),nb,pile);
			donne.initDonneur();
		}
		else
		{
			donne=new Donne(par.getMode(),param.getInfos().get(Jeu.Tarot.ordinal()+1),nb,((Levable)par).empiler());
			donne.donneurSuivant(par.getDistribution().getDonneur());
		}
		donne.initDonne();
		par=new PartieTarot(Type.Aleatoire,donne);
		mettre_en_place_ihm_tarot();
	}
	private void mettre_en_place_ihm_tarot()
	{
		placer_levable();
		afficherMainUtilisateur(false);
		pack();
		byte sec=((PartieTarot)par).joueurAyantPetitSec();
		if(sec>-1)
		{
			JOptionPane.showMessageDialog(this,pseudos().get(sec)+" a le Petit sec","Petit sec",JOptionPane.INFORMATION_MESSAGE);
			finPartie();
		}
		else
		{
			if(par.getEtat()==Etat.Contrat)
			{
				anim_contrat=new AnimationContrat(pseudos(),par.getDistribution().getDonneur(),(byte)((par.getDistribution().getDonneur()+1)%par.getNombreDeJoueurs()));
				anim_contrat.start();
			}
			else
			{
				debutPli();
			}
		}
	}
	/**Pseudos utilis&eacute;s*/
	private Vector<String> pseudos()
	{
		Vector<String> pseudos=new Vector<String>();
		byte nombre_joueurs=par.getNombreDeJoueurs();
		pseudos.addElement(pseudo());
		String[] noms;
		if(par instanceof PartieTarot)
			noms=param.getInfos().lastElement().lastElement().split(tb);
		else
			noms=param.getInfos().lastElement().get(4).split(tb);
		for (int indice = 0; indice < nombre_joueurs-1; indice++)
			pseudos.addElement(noms[indice]);
		return pseudos;
	}
	private String pseudo()
	{
		return param.getInfos().lastElement().get(1).substring(param.getInfos().lastElement().get(1).indexOf(p2)+1);
	}
	private void ajouterTexteDansZone(String texte)
	{
		((JTextArea)((JScrollPane)((JPanel)getContentPane().getComponent(3)).getComponent(0)).getViewport().getComponent(0)).append(texte);
	}
	private void ajouterTexteDansPanneau(String texte,byte joueur)
	{
		((Tapis)getContentPane().getComponent(1)).ajouterTexteDansPanneau(texte,joueur,par.getNombreDeJoueurs());
	}
	private void voirChien()
	{
		setChien((MainTarot)par.getDistribution().derniereMain(),false);
		JPanel boutons=(JPanel)((JPanel)getContentPane().getComponent(3)).getComponent(1);
		boutons.removeAll();
		if(((Levable)par).getPreneur()==0)
		{
			par.setEtat(Etat.Ecart);
			String Prendre_les_cartes_du_chien=Boutons.Prendre_les_cartes_du_chien.toString();
			ajouterBoutonJeu(Prendre_les_cartes_du_chien,Prendre_les_cartes_du_chien,true);
		}
		else
		{
			par.setEtat(Etat.Chien_Vu);
			par.getDistribution().main(((Levable)par).getPreneur()).ajouterCartes(par.getDistribution().derniereMain());
			//On ajoute les cartes du chien au preneur pour en ecarter d'autres
			String[] raison = new String[1];
			MainTarot mt=((PartieTarot)par).strategieEcart(raison);
			MainTarot atouts=PartieTarot.atouts(mt.couleurs());
			if(!atouts.estVide())
			{
				JPanel panneau1=new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
				for(byte indice=0;indice<atouts.total();indice++)
				{
					CarteGraphique carte=new CarteGraphique(atouts.carte(indice),JLabel.RIGHT,indice!=0);
					if(indice==0)
						carte.setPreferredSize(new Dimension(100,150));
					else
						carte.setPreferredSize(new Dimension(25,150));
					panneau1.add(carte);
				}
				JOptionPane.showMessageDialog(Fenetre.this,panneau1,"Atouts ecartes de "+pseudos().get(((Levable)par).getPreneur())+":",JOptionPane.INFORMATION_MESSAGE);
			}
			//Le preneur ecarte les cartes qu'il veut
			par.getDistribution().main(((Levable)par).getPreneur()).supprimerCartes(mt);
			Vector<Annonce> va=((PartieTarot)par).strategieAnnonces(((Levable)par).getPreneur(),raison);
			if(!va.isEmpty())
			{
				((Levable)par).ajouterAnnonces(((Levable)par).getPreneur(),va);
				((Levable)par).setEntameur(((Levable)par).getPreneur());
			}
			Pli pli=new Pli(mt,((Levable)par).getPreneur());
			((PartieTarot)par).getPlisAttaque().addElement(pli);
			ajouterBoutonJeu(Boutons.Passe_au_jeu_de_la_carte.toString(),Boutons.Pli_suivant.toString(),true);
		}
		pack();
	}
	private void revoirChien()
	{
		String Revenir_au_pli_actuel=Boutons.Revenir_au_pli_actuel.toString();
		setChien((MainTarot)((PartieTarot)par).getDistribution().derniereMain(),false);
		afficherMainUtilisateur(false);
		JPanel boutons=(JPanel)((JPanel)getContentPane().getComponent(3)).getComponent(1);
		boutons.removeAll();
		ajouterBoutonJeu(Revenir_au_pli_actuel,Revenir_au_pli_actuel,true);
		pack();
	}
	private void prendreCartesChien()
	{
		String Passe_au_jeu_de_la_carte=Boutons.Passe_au_jeu_de_la_carte.toString();
		String Valider_chien=Boutons.Valider_le_chien.toString();
		getJMenuBar().getMenu(1).getItem(0).setEnabled(true);
		((Levable)par).setEntameur((byte)0);
		((Levable)par).setPliEnCours();
		par.getDistribution().main().ajouterCartes(par.getDistribution().derniereMain());
		((Tapis)getContentPane().getComponent(1)).retirerCartes();
		afficherMainUtilisateur(true);
		JPanel boutons=(JPanel)((JPanel)getContentPane().getComponent(3)).getComponent(1);
		boutons.removeAll();
		ajouterBoutonJeu(Passe_au_jeu_de_la_carte,Valider_chien,false);
		pack();
	}
	private void ajouterUneCarteAuChien(Carte ct)
	{
		getJMenuBar().getMenu(1).getItem(0).setEnabled(false);
		if(((Levable)par).getPliEnCours().total()<par.getDistribution().derniereMain().total())
		{
			((Levable)par).ajouterUneCarteDansPliEnCours(ct);
			par.getDistribution().main().jouer(ct);
		}
		else
		{
			JOptionPane.showMessageDialog(this,"Le nombre maximal de cartes a mettre au chien est atteint","Impossible de rajouter des cartes",JOptionPane.ERROR_MESSAGE);
		}
		afficherMainUtilisateur(true);
		setChien((MainTarot)((Levable)par).getPliEnCours().getCartes(),true);
		JPanel boutons=(JPanel)((JPanel)getContentPane().getComponent(3)).getComponent(1);
		JButton valide=(JButton)boutons.getComponent(0);
		valide.setEnabled(((Levable)par).getPliEnCours().total()==par.getDistribution().derniereMain().total());
		if(((PartieTarot)par).getPliEnCours().total()==par.getDistribution().derniereMain().total())
		{
			ajouterBoutonJeu(Contrat.chelem,Boutons._Chelem.name(),true);
		}
		pack();
	}
	private void retirerUneCarteDuChien(Carte ct)
	{
		((PartieTarot)par).getPliEnCours().getCartes().jouer(ct);
		getJMenuBar().getMenu(1).getItem(0).setEnabled(((Levable)par).getPliEnCours().estVide());
		par.getDistribution().main().ajouter(ct);
		afficherMainUtilisateur(true);
		setChien((MainTarot)((Levable)par).getPliEnCours().getCartes(),true);
		JPanel boutons=(JPanel)((JPanel)getContentPane().getComponent(3)).getComponent(1);
		JButton valide=(JButton)boutons.getComponent(0);
		valide.setEnabled(((Levable)par).getPliEnCours().total()==par.getDistribution().derniereMain().total());
		if(boutons.getComponentCount()==2)
		{
			boutons.remove(1);
		}
		pack();
	}
}