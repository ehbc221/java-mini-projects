package jeux.parties;
import java.util.*;
import jeux.*;
import jeux.cartes.*;
import jeux.encheres.*;
import jeux.enumerations.*;
import jeux.mains.*;
import jeux.plis.*;
/**
 *
 */
public class PartieBelote extends Partie implements Annoncable{
	private static final long serialVersionUID = 1L;
	/**Au debut on a besoin d'un variable preneur pour stocker le joueur ayant annonce temporairement le plus haut contrat
	 * De plus, le choix par defaut de -1 pour le preneur sert pour le tarot lorsque
	 * personne ne prend un contrat et chacun joue pour soi*/
	private byte preneur=-1;
	/** Ce sont les primes, miseres ou poignees annoncees par le(s) joueur(s)*/
	private Vector<Vector<Annonce>> annonces=new Vector<Vector<Annonce>>();
	/**Le contrat permet de dire quel va etre le deroulement
	 * de la partie*/
	private Contrat contrat=new Contrat(EncheresBelote.Passe);
	/**Ce sont les plis faits par les joueurs*/
	private Vector<Vector<Pli>> plis=new Vector<Vector<Pli>>();
	/**Fin du premier tour d'enchere a la belote<br/>
	 * est vrai si et seulement si c'est la fin des encheres de premier tour*/
	private boolean finEncheresPremierTour;
	/**Pli en cours d'etre joue*/
	private Pli pliEnCours;
	/**la carte appelee represente la couleur d'atout eventuellement dominante pour la partie*/
	private Carte carteAppelee;
	/**Entameur du pli qui est en cours d'etre joue*/
	private byte entameur;
	/**Ensembe des contrats annonces*/
	private Vector<Contrat> contrats=new Vector<Contrat>();
	private Vector<Vector<MainBelote>> mains_annonces=new Vector<Vector<MainBelote>>();
	/**Ramasseur du pli qui vient d'etre joue*/
	private byte ramasseur;
	/**utilise pour dire si l'utilisateur a annonce un contrat ou non au chargement d'une partie*/
	private boolean finEncheres;
	public PartieBelote(Type type, Donne donne) {
		super(type, donne);
		byte nombre_joueurs=getNombreDeJoueurs();
		//A la belote le jeu se deroule avec deux equipes de deux joueurs
		//Chaque joueur fait equipe avec celui qui est en face
		/*Initialise les annonces*/
		for(int joueur=0;joueur<nombre_joueurs;joueur++)
		{
			annonces.addElement(new Vector<Annonce>());
			mains_annonces.addElement(new Vector<MainBelote>());
		}
		for (int equipe = 0; equipe < 2; equipe++)
			plis.addElement(new Vector<Pli>());
	}
	public void initPartie()
	{
		preneur=-1;
		contrats=new Vector<Contrat>();
		finEncheres=false;
		finEncheresPremierTour=false;
		contrat=new Contrat(EncheresBelote.Passe);
		pliEnCours=null;
		carteAppelee=null;
		setEtat(Etat.Contrat);
		Vector<Short> scores=getScores();
		byte nombre_joueurs=getNombreDeJoueurs();	
		for(int joueur=0;joueur<nombre_joueurs;joueur++)
		{/*Initialise les annonces*/
			scores.setElementAt((short) 0,joueur);
			annonces.setElementAt(new Vector<Annonce>(),joueur);
			mains_annonces.setElementAt(new Vector<MainBelote>(),joueur);
		}
		for (int equipe = 0; equipe < 2; equipe++)
			plis.setElementAt(new Vector<Pli>(),equipe);
	}
	public void simuler()
	{
		long debut=System.currentTimeMillis();
		byte nombre_joueurs=getNombreDeJoueurs();
		boolean passe=false;
		byte[] joueurs=new byte[2*nombre_joueurs];
		String[] raison={""};
		byte donneur=getDistribution().getDonneur();
		Contrat contrat_tmp;
		for(byte joueur=0;joueur<joueurs.length;joueur++)
		{
			joueurs[joueur]=(byte)((donneur+joueur+1)%getNombreDeJoueurs());
		}
		boolean sur_contrat=avecSurContrat();
		int parle=0;
		for(byte joueur:joueurs)
		{
			if(parle==4)
			{
				Partie.chargement_simulation+=10;
				finEncheresPremierTour=!finEncheresPremierTour;
			}
			contrat_tmp=strategieContrat(raison);
			ajouterContrat(contrat_tmp);
			if(sur_contrat&&contrat_tmp.force()>3)
			{
				preneur=joueur;
				contrat=contrat_tmp;
				break;
			}
			if(!sur_contrat&&contrat_tmp.force()>0)
			{
				preneur=joueur;
				contrat=contrat_tmp;
				break;
			}
			if(contrat_tmp.force()>0)
			{
				preneur=joueur;
				contrat=contrat_tmp;
			}
			parle++;
		}
		Partie.chargement_simulation+=10;
		if(contrat.force()<1)
		{
			Partie.chargement_simulation=100;
			return;
		}
		MainBelote talon=new MainBelote();
		talon.ajouterCartes(getDistribution().derniereMain());/*Copie du talon original pour donner des cartes aux joueurs*/
		getDistribution().main(preneur).ajouter(talon.jouer(0));//Le preneur prend la carte du dessus
		joueurs=new byte[nombre_joueurs];
		entameur=(byte)((donneur+1)%nombre_joueurs);
		for(byte joueur=0;joueur<joueurs.length;joueur++)
		{
			joueurs[joueur]=(byte)((entameur+joueur)%nombre_joueurs);
		}
		for(byte joueur:joueurs)
		{
			for(int j=0;j<2;j++)
			{
				getDistribution().main(joueur).ajouter(talon.jouer(0));
			}
			if(joueur!=preneur)
			{
				getDistribution().main(joueur).ajouter(talon.jouer(0));
			}
		}
		setPliEnCours();
		for(;;)
		{
			if(passe)
			{
				ajouterPliEnCours();
				setEntameur();
				setPliEnCours();
			}
			for(byte joueur=0;joueur<nombre_joueurs;joueur++)
			{
				joueurs[joueur]=(byte)((entameur+joueur)%nombre_joueurs);
			}
			for(byte joueur:joueurs)
			{
				Carte ct=strategieJeuCarteUnique(raison);
				Vector<Annonce> va=strategieAnnonces(joueur,raison,ct,!passe);
				ajouterAnnonces(joueur,va);
				if(!passe)
				{
					passe=true;
				}
				getDistribution().main(joueur).jouer(ct);
				ajouterUneCarteDansPliEnCours(ct);
			}
			if(getDistribution().main().estVide())
			{/*Il y a dix de der*/
				ajouterPliEnCours();
				Vector<Annonce> va=new Vector<Annonce>();
				va.addElement(new Annonce(Annonce.dix_de_der));
				ajouterAnnonces(ramasseur,va);
				break;
			}
			Partie.chargement_simulation+=10;
		}
		System.out.println("temps="+(System.currentTimeMillis()-debut));
	}
	public boolean avecSurContrat()
	{
		return getMode().startsWith("Avec");
	}
	public Jeu jeu()
	{
		return Jeu.Belote;
	}
	private boolean sur_coupe_obligatoire_partenaire()
	{
		return sous_coupe_obligatoire_partenaire()||getInfos().get(6).contains(BeloteCoupePartenaire.Sur_couper_uniquement.toString());
	}
	private boolean sous_coupe_obligatoire_partenaire()
	{
		return getInfos().get(6).contains(BeloteCoupePartenaire.Sous_couper_sur_couper.toString());
	}
	private boolean sous_coupe_obligatoire_adversaire()
	{
		return getInfos().get(5).endsWith(Reponse.oui.toString());
	}
	public boolean autorise(Carte c,String[] raison)
	{
		/*Ensemble des cartes jouees sur ce pli*/
		Main m=pliEnCours.getCartes();
		if(pliEnCours.estVide())
		{
			//L'entame est libre a la belote
			return true;
		}
		byte nombre_joueurs=getNombreDeJoueurs();
		byte numero=(byte)((pliEnCours.total()+entameur)%nombre_joueurs);
		byte couleurDemandee=pliEnCours.couleurDemandee();
		byte couleurAtout=couleur_atout();
		byte ramasseur_virtuel=pliEnCours.getRamasseurBelote(nombre_joueurs, contrat, couleurAtout);
		//Main du joueur
		MainBelote main=(MainBelote)getDistribution().main(numero);
		Carte carte_forte=pliEnCours.carteDuJoueur(ramasseur_virtuel,nombre_joueurs,null);
		if(contrat.force()==1)
		{
			int maxAtout=((MainBelote)m).maxAtout(couleurAtout,couleurDemandee);
			int maxAtout2=main.maxAtout(couleurAtout,couleurDemandee);
			if(couleurAtout==couleurDemandee)
			{
				//Nombre d'atouts dans la main du joueur
				if(main.tailleCouleur(couleurDemandee)==0)
				{
					return true;
				}
				//On compare les maximum d'atouts entre le pli et la main du joueur
				if(maxAtout2<maxAtout)
				{
					if(c.couleur()!=couleurDemandee)
					{
						raison[0]="Vous devez jouer "+Couleur.values()[couleurDemandee-1];
					}
					return c.couleur()==couleurDemandee;
				}
				byte force=((CarteBelote)c).force(couleurAtout,couleurDemandee);
				if(force<=maxAtout)
				{
					raison[0]="Vous devez monter sur "+carte_forte;
				}
				return force>maxAtout;
			}
			//nombreCartes est le nombre de cartes a la couleur demandee
			if(main.tailleCouleur(couleurDemandee)>0)
			{
				if(c.couleur()!=couleurDemandee)
				{
					raison[0]="Vous devez jouer "+Couleur.values()[couleurDemandee-1];
				}
				//Le joueur possede de la couleur demandee
				return c.couleur()==couleurDemandee;
			}
			//Le joueur ne possede pas de la couleur demandee et une seule carte est jouee
			if(m.total()==1)
			{
				//Nombre d'atouts dans la main du joueur
				if(main.tailleCouleur(couleurAtout)>0)
				{
					if(c.couleur()!=couleurAtout)
					{
						raison[0]="Vous devez couper avec une carte "+Couleur.values()[couleurAtout-1];
					}
					//Le joueur doit couper s'il possede de l'atout
					//car la carte de couleur jouee est celle d'un joueur adverse
					return c.couleur()==couleurAtout;
				}
				return true;
				//Le joueur se defausse sinon
			}
			if(m.total()==2)
			{
				//Le partenaire a entame le pli
				CarteBelote cartePartenaire=(CarteBelote)m.carte(0);
				CarteBelote deuxiemeCarte=(CarteBelote)m.carte(1);/*Carte de l'adversaire*/
				//Le joueur se defausse s'il n'a ni de la couleur demandee ni de l'atout
				if(main.tailleCouleur(couleurAtout)==0)
				{
					return true;
				}
				if(cartePartenaire.force(couleurAtout,couleurDemandee)>deuxiemeCarte.force(couleurAtout,couleurDemandee))
				{
					if(!sur_coupe_obligatoire_partenaire())
					{//Le premier adversaire du joueur n'a pas coupe, le joueur peut jouer ce qu'il veut
						return true;
					}
					raison[0]="Vous devez couper sur votre partenaire";
					return c.couleur()==couleurAtout;
				}
				//Pour surcouper un adversaire, il faut monter
				if(maxAtout2>deuxiemeCarte.force(couleurAtout,couleurDemandee))
				{
					byte force=((CarteBelote)c).force(couleurAtout,couleurDemandee);
					if(force<=deuxiemeCarte.force(couleurAtout,couleurDemandee))
					{
						if(deuxiemeCarte.couleur()!=couleurAtout)
						{
							raison[0]="Vous devez couper sur l'adversaire avec "+Couleur.values()[couleurAtout-1];
						}
						else
						{
							raison[0]="Vous devez monter sur "+carte_forte;
						}
					}
					return force>deuxiemeCarte.force(couleurAtout,couleurDemandee);
				}
				if(sous_coupe_obligatoire_adversaire())
				{
					raison[0]="Vous devez sous couper sur l'adversaire";
					return c.couleur()==couleurAtout;
				}
				return true;
				//Le joueur se defausse sinon
			}
			CarteBelote premiereCarte=(CarteBelote)m.carte(0);
			//Premiere carte jouee par l'equipe adverse
			CarteBelote cartePartenaire=(CarteBelote)m.carte(1);
			//Carte jouee par le partenaire
			CarteBelote deuxiemeCarte=(CarteBelote)m.carte(2);
			//Deuxieme carte jouee par l'equipe adverse
			if(main.tailleCouleur(couleurAtout)==0)
			{
				return true;
			}
			if(cartePartenaire.force(couleurAtout,couleurDemandee)>Math.max(deuxiemeCarte.force(couleurAtout,couleurDemandee),premiereCarte.force(couleurAtout,couleurDemandee)))
			{
				if(!sur_coupe_obligatoire_partenaire())
				{//Le joueur peut jouer ce qu'il veut lorsque son partenaire est maitre
					return true;
				}
				if(maxAtout2>cartePartenaire.force(couleurAtout,couleurDemandee))
				{
					if(((CarteBelote)c).force(couleurAtout,couleurDemandee)<=cartePartenaire.force(couleurAtout,couleurDemandee))
					{
						if(c.couleur()!=couleurAtout)
						{
							raison[0]="Vous devez surcouper sur votre partenaire avec "+Couleur.values()[couleurAtout-1];
						}
						else
						{
							raison[0]="Vous devez monter sur "+carte_forte;
						}
					}
					return ((CarteBelote)c).force(couleurAtout,couleurDemandee)>cartePartenaire.force(couleurAtout,couleurDemandee);
				}
				if(sous_coupe_obligatoire_partenaire())
				{
					raison[0]="Vous devez sous-couper sur votre partenaire";
					return c.couleur()==couleurAtout;
				}
				return true;
			}
			//Le joueur se defausse s'il n'a ni de la couleur demandee ni de l'atout
			//Pour surcouper un adversaire, il faut monter
			if(maxAtout2>deuxiemeCarte.force(couleurAtout,couleurDemandee))
			{
				byte force=((CarteBelote)c).force(couleurAtout,couleurDemandee);
				if(force<=deuxiemeCarte.force(couleurAtout,couleurDemandee))
				{
					if(deuxiemeCarte.couleur()!=couleurAtout)
					{
						raison[0]="Vous devez couper sur l'adversaire avec "+Couleur.values()[couleurAtout-1];
					}
					else
					{
						raison[0]="Vous devez monter sur "+carte_forte;
					}
				}
				return force>deuxiemeCarte.force(couleurAtout,couleurDemandee);
			}
			if(sous_coupe_obligatoire_adversaire())
			{
				raison[0]="Vous devez sous couper sur l'adversaire";
				return c.couleur()==couleurAtout;
			}
			return true;
		}
		else if(contrat.force()<4)
		{
			if(main.tailleCouleur(couleurDemandee)==0)
			{
				return true;
			}
			if(c.couleur()!=couleurDemandee)
			{
				raison[0]="Vous devez jouer "+Couleur.values()[couleurDemandee-1];
			}
			return c.couleur()==couleurDemandee;
		}
		CarteBelote cb=(CarteBelote)pliEnCours.carte(0);
		if(main.tailleCouleur(couleurDemandee)==0)
		{
			return true;
		}
		if(main.maxAtout(couleurDemandee,couleurDemandee)<cb.force(couleurDemandee,couleurDemandee))
		{
			if(c.couleur()!=couleurDemandee)
			{
				raison[0]="Vous devez jouer "+Couleur.values()[couleurDemandee-1];
			}
			return c.couleur()==couleurDemandee;
		}
		byte force=((CarteBelote)c).force(couleurDemandee,couleurDemandee);
		if(force<cb.force(couleurDemandee,couleurDemandee))
		{
			raison[0]="Vous devez monter sur "+carte_forte;
		}
		return force>cb.force(couleurDemandee,couleurDemandee);
	}
	public boolean autorise_annonce(Annonce annonce,byte numero)
	{
		MainBelote main_joueur=(MainBelote)getDistribution().main(numero);
		byte couleur_atout=couleur_atout();
		Vector<MainBelote> couleurs=main_joueur.couleurs(couleur_atout,contrat);
		if(annonce.equals(new Annonce(Annonce.belote_rebelote)))
		{/*Il est necessaire que le contrat ne soit pas sans at ni tout at pour pouvoir annoncer belote rebelote*/
			if(contrat.force()!=1)
			{
				return false;
			}
			String[] raison=new String[1];
			if(main(couleurs,couleur_atout).contient(new CarteBelote((byte)13,couleur_atout)))
			{
				if(autorise(new CarteBelote((byte)13,couleur_atout),raison))
				{
					if(!main(couleurs,couleur_atout).contient(new CarteBelote((byte)14,couleur_atout)))
					{
						return getAnnonces(numero).contains(new Annonce(Annonce.belote_rebelote));
					}
					return true;
				}
			}
			if(main(couleurs,couleur_atout).contient(new CarteBelote((byte)14,couleur_atout)))
			{
				if(autorise(new CarteBelote((byte)14,couleur_atout),raison))
				{
					if(!main(couleurs,couleur_atout).contient(new CarteBelote((byte)13,couleur_atout)))
					{
						return getAnnonces(numero).contains(new Annonce(Annonce.belote_rebelote));
					}
					return true;
				}
			}
		}
		return false;
	}
	public Contrat strategieContrat(String[] raison)
	{
		Contrat contrat_joueur=contrat(raison);
		if(contrat_joueur.estDemandable(contrat))
		{
			return contrat_joueur;
		}
		return new Contrat(EncheresBelote.Passe);
	}
	public void conseil_contrat(String[] raison)
	{
		Contrat contrat_joueur=contrat(raison);
		raison[2]=contrat_joueur.toString();
		if(contrat_joueur.estDemandable(contrat))
		{
			if(contrat_joueur.force()!=1)
			{
				raison[1]=contrat_joueur.toString();
			}
		}
		else
		{
			if(contrat_joueur.force()>1)
			{
				raison[0]+="Votre main valait pour un contrat de "+contrat_joueur+" mais";
				raison[0]+="Vous ne pouvez pas surencherir avec le contrat "+contrat_joueur+" sur le contrat "+contrat+",\n";
				raison[0]+="il faut annoncer un contrat plus eleve que celui precedemment annonce ou passer.";
			}
			else
			{
				raison[0]+="Votre main valait pour un contrat a la couleur "+raison[1]+" mais";
				raison[0]+="Vous ne pouvez pas surencherir avec le contrat a la couleur "+raison[1]+" sur le contrat "+contrat+",\n";
				raison[0]+="il faut annoncer un contrat plus eleve que celui precedemment annonce ou passer.";
			}
			raison[1]=EncheresBelote.Passe.toString();
		}
	}
	public Contrat contrat(String[] raison)
	{
		byte numero=(byte)((getDistribution().getDonneur()+1+contrats.size())%getNombreDeJoueurs());
		MainBelote mj=(MainBelote)getDistribution().main(numero);
		MainBelote reunion=new MainBelote();
		reunion.ajouterCartes(mj);
		String mode=getMode();//Le mode a la belote est Avec surcontrat/Sans surcontrat
		CarteBelote carteAuDessus=(CarteBelote)getDistribution().derniereMain().carte(0);
		reunion.ajouter(carteAuDessus);
		Vector<MainBelote> repartition_atouts=reunion.couleurs(new Contrat("Couleur"));
		Vector<MainBelote> repartition_atouts_2=new Vector<MainBelote>(repartition_atouts);
		Vector<MainBelote> repartition_couleurs=reunion.couleurs(new Contrat("Sans atout"));
		Vector<MainBelote> couleurs_candidates;
		byte nombre_couple_as_dix=0;
		byte nombre_as=0;
		byte couleur_longue;
		byte couleur_atout=carteAuDessus.couleur();
		Vector<Byte> couleurs_a_prendre=new Vector<Byte>();
		byte meilleure_couleur=0;
		if(mode.startsWith(ModeBelote.Avec_surcontrat.toString()))
		{
			if(mj.tailleValeur((byte)11)>2)
			{
				return new Contrat("Tout atout");
			}
			if(mj.tailleValeur((byte)1)>2)
			{
				return new Contrat("Sans atout");
			}
		}
		byte[][] repartition_possibles=new byte[][]{new byte[]{6,0,0,0},new byte[]{5,1,0,0},new byte[]{4,2,0,0},new byte[]{4,1,1,0},new byte[]{3,3,0,0},new byte[]{3,2,1,0},new byte[]{3,1,1,1},new byte[]{2,2,2,0},new byte[]{2,2,1,1}};
		couleurs_candidates=couleurs_assurant_un_pli(repartition_atouts);
		trier_couleurs_longueurs(repartition_atouts_2);
		if(finEncheresPremierTour)
		{/*Deuxieme tour d'enchere*/
			for(byte couleur=2;couleur<6;couleur++)
			{
				if(couleur!=couleur_atout)
				{
					couleurs_a_prendre.addElement(couleur);
				}
			}
			if(egalite_repartition(repartition_atouts_2,repartition_possibles[0]))
			{/*Six cartes d'une meme couleur*/
				if(numero==0)
				{
					raison[0]="Vous aurez trop de mal a prendre maintenant dans une couleur ou vous ne possedez pas d'atout.";
				}
			}
			if(egalite_repartition(repartition_atouts_2,repartition_possibles[1]))
			{/*Cinq cartes d'une meme couleur et une autre d'une meme couleur*/
				meilleure_couleur=repartition_atouts_2.get(0).carte(0).couleur();
				if(numero==0)
				{
					if(meilleure_couleur!=couleur_atout)
					{
						raison[0]="Avec cinq cartes de meme couleur, vous assurez au moins "+repartition_atouts_2.get(0).nombre_plis_assures_min_atout()+" plis a la couleur "+Couleur.values()[meilleure_couleur-1]+".";
						raison[1]=Couleur.values()[meilleure_couleur-1].toString();
					}
					else
					{
						raison[0]="Vous ne pouvez plus prendre la couleur contient cinq cartes";
					}
				}
				else
				{
					if(meilleure_couleur!=couleur_atout)
					{
						carteAppelee=new CarteBelote((byte)11,meilleure_couleur);
					}
				}
				if(meilleure_couleur!=couleur_atout)
				{
					return new Contrat("Autre couleur");
				}
				return new Contrat("Passe");
			}
			Vector<Byte> couleurs_avec_valet_sec=couleurs_avec_valet_sec(repartition_atouts_2);
			if(!couleurs_avec_valet_sec.isEmpty())
			{
				Vector<Byte> couleurs_as_dix=new Vector<Byte>();
				for(byte couleur=2;couleur<6;couleur++)
				{
					if(!main(repartition_couleurs,couleur).estVide()&&main(repartition_couleurs,couleur).carte(0).valeur()==1)
					{
						nombre_as++;
						if(main(repartition_couleurs,couleur).total()>1&&main(repartition_couleurs,couleur).carte(1).valeur()==10)
						{
							nombre_couple_as_dix++;
							couleurs_as_dix.addElement(couleur);
						}
					}
				}
				if(nombre_as>2||nombre_couple_as_dix>1)
				{
					Vector<Byte> couleurs_a_prendre_avec_valet_sec=new Vector<Byte>();
					for(byte couleur:couleurs_avec_valet_sec)
					{
						if(couleurs_a_prendre.contains(couleur))
						{
							couleurs_a_prendre_avec_valet_sec.addElement(couleur);
						}
					}
					if(!couleurs_a_prendre_avec_valet_sec.isEmpty())
					{
						if(numero==0)
						{
							raison[0]="";
							if(nombre_as>2)
							{
								raison[0]+="Vous avez trois as dans les autres couleurs que "+Couleur.values()[couleurs_a_prendre_avec_valet_sec.get(0)-1]+" pour peut-etre assurer des plis en plus du valet sec.\n";
							}
							if(nombre_couple_as_dix>1)
							{
								raison[0]+="Vous avez "+nombre_couple_as_dix+" couples as - dix de meme couleur dans les couleurs ";
								for(byte couleur:couleurs_as_dix)
								{
									raison[0]+=Couleur.values()[couleur-1]+",";
								}
								raison[0]+=" pour peut-etre assurer des plis en plus du valet sec.\n";
							}
							raison[1]=Couleur.values()[couleurs_a_prendre_avec_valet_sec.get(0)-1].toString();
						}
						else
						{
							carteAppelee=new CarteBelote((byte)11,couleurs_a_prendre_avec_valet_sec.get(0));
						}
						return new Contrat("Autre couleur");
					}
					if(numero==0)
					{
						raison[0]="Vous n'avez au plus deux as et au plus un couple as - dix de meme couleur, pour suivre ce valet sec";
					}
					return new Contrat("Passe");
				}
			}
			if(repartition_atouts_2.get(0).total()==4)
			{
				couleur_longue=repartition_atouts_2.get(0).carte(0).couleur();
				if(repartition_atouts_2.get(0).nombre_plis_assures_min_atout()>2)
				{
					if(couleurs_a_prendre.contains(couleur_longue))
					{
						if(numero==0)
						{
							raison[0]="Vous avez quatre atouts a "+Couleur.values()[couleur_longue-1]+" et "+repartition_atouts_2.get(0).nombre_plis_assures_min_atout()+" plis a cette couleur.";
							raison[1]=Couleur.values()[couleur_longue-1].toString();
						}
						else
						{
							carteAppelee=new CarteBelote((byte)11,couleur_longue);
						}
						return new Contrat("Autre couleur");
					}
					return new Contrat("Passe");
				}
				if(egalite_repartition(repartition_atouts_2,repartition_possibles[2]))
				{/*Quatre cartes d'une meme couleur et deux autres d'une meme couleur*/
					byte autre_couleur=repartition_atouts_2.get(1).carte(0).couleur();
					if(main(repartition_couleurs,autre_couleur).carte(1).valeur()==10)
					{/*Un couple as-dix de meme couleur*/
						if(couleurs_a_prendre.contains(couleur_longue))
						{
							if(numero==0)
							{
								raison[0]="Vous avez quatre atouts a "+Couleur.values()[couleur_longue-1]+" et un as et un dix a "+Couleur.values()[autre_couleur-1]+".";
								raison[1]=Couleur.values()[couleur_longue-1].toString();
							}
							else
							{
								carteAppelee=new CarteBelote((byte)11,couleur_longue);
							}
							return new Contrat("Autre couleur");
						}
						return new Contrat("Passe");
					}
					if(repartition_atouts_2.get(0).nombre_plis_assures_min_atout()==2)
					{
						if(main(repartition_couleurs,autre_couleur).carte(0).valeur()==1)
						{
							if(couleurs_a_prendre.contains(couleur_longue))
							{
								if(numero==0)
								{
									raison[0]="Vous avez quatre atouts a "+Couleur.values()[couleur_longue-1]+" et un as a "+Couleur.values()[autre_couleur-1]+".";
									raison[1]=Couleur.values()[couleur_longue-1].toString();
								}
								else
								{
									carteAppelee=new CarteBelote((byte)11,couleur_longue);
								}
								return new Contrat("Autre couleur");
							}
							return new Contrat("Passe");
						}
					}
					if(repartition_atouts_2.get(1).carte(1).valeur()==9)
					{
						if(repartition_atouts_2.get(0).carte(0).valeur()==1)
						{
							if(couleurs_a_prendre.contains(autre_couleur))
							{
								if(numero==0)
								{
									raison[0]="";
									raison[1]=Couleur.values()[autre_couleur-1].toString();
								}
								else
								{
									carteAppelee=new CarteBelote((byte)11,autre_couleur);
								}
								return new Contrat("Autre couleur");
							}
							return new Contrat("Passe");
						}
					}
				}
				if(egalite_repartition(repartition_atouts_2,repartition_possibles[3]))
				{/*Quatre cartes d'une meme couleur et deux autres de couleur differentes*/
					byte autre_couleur=repartition_atouts_2.get(1).carte(0).couleur();
					byte autre_couleur_2=repartition_atouts_2.get(2).carte(0).couleur();
					if(main(repartition_couleurs,autre_couleur).carte(0).valeur()==1&&main(repartition_couleurs,autre_couleur_2).carte(0).valeur()==1)
					{/*Deux as*/
						if(couleurs_a_prendre.contains(couleur_longue))
						{
							if(numero==0)
							{
								raison[0]="Vous avez quatre atouts a "+Couleur.values()[couleur_longue-1]+" et un as a "+Couleur.values()[autre_couleur-1]+" et a "+Couleur.values()[autre_couleur_2-1]+".";
								raison[1]=Couleur.values()[couleur_longue-1].toString();
							}
							else
							{
								carteAppelee=new CarteBelote((byte)11,couleur_longue);
							}
							return new Contrat("Autre couleur");
						}
						return new Contrat("Passe");
					}
					if(repartition_atouts_2.get(0).nombre_plis_assures_min_atout()==2)
					{
						if(main(repartition_couleurs,autre_couleur).carte(0).valeur()==1||main(repartition_couleurs,autre_couleur_2).carte(0).valeur()==1)
						{
							if(couleurs_a_prendre.contains(couleur_longue))
							{
								if(numero==0)
								{
									raison[0]="Vous avez quatre atouts a "+Couleur.values()[couleur_longue-1]+" et un as ";
									String[] chaines={"a "+Couleur.values()[autre_couleur-1],"a "+Couleur.values()[autre_couleur_2-1]};
									if(main(repartition_couleurs,autre_couleur).carte(0).valeur()==1)
									{
										raison[0]+=chaines[0];
										if(main(repartition_couleurs,autre_couleur_2).carte(0).valeur()==1)
										{
											raison[0]+=" et "+chaines[1]+".";
										}
										else
										{
											raison[0]+=".";
										}
									}
									else
									{
										raison[0]+=chaines[1]+".";
									}
									raison[1]=Couleur.values()[couleur_longue-1].toString();
								}
								else
								{
									carteAppelee=new CarteBelote((byte)11,couleur_longue);
								}
								return new Contrat("Autre couleur");
							}
							return new Contrat("Passe");
						}
					}
				}
				if(repartition_atouts_2.get(0).nombre_plis_assures_min_atout()<2)
				{
					if(numero==0)
					{
						raison[0]="";
					}
				}
				return new Contrat("Passe");
			}
			trier_couleurs_candidates(couleurs_candidates);
			Vector<Byte> autres_couleurs=new Vector<Byte>();
			if(egalite_repartition(repartition_atouts_2,repartition_possibles[4]))
			{/*Trois cartes d'une meme couleur et trois autres d'une meme couleur*/
				for(MainBelote couleur_candidate:couleurs_candidates)
				{
					byte numero_couleur_candidate=couleur_candidate.carte(0).couleur();
					autres_couleurs=new Vector<Byte>();
					for(byte couleur=2;couleur<6;couleur++)
					{
						if(numero_couleur_candidate!=couleur&&!main(repartition_atouts,couleur).estVide())
						{
							autres_couleurs.addElement(couleur);
						}
					}
					if(main(repartition_couleurs,autres_couleurs.get(0)).carte(1).valeur()==10&&couleurs_a_prendre.contains(numero_couleur_candidate))
					{/*Un couple as-dix pour accompagner*/
						meilleure_couleur=numero_couleur_candidate;
						break;
					}
					if(couleur_candidate.carte(1).valeur()==9)
					{/*Un as pour accompagner*/
						if(main(repartition_couleurs,autres_couleurs.get(0)).carte(0).valeur()==1&&couleurs_a_prendre.contains(numero_couleur_candidate))
						{
							meilleure_couleur=numero_couleur_candidate;
							break;
						}
					}
					if(couleur_candidate.carte(2).valeur()==1&&couleurs_a_prendre.contains(numero_couleur_candidate))
					{
						meilleure_couleur=numero_couleur_candidate;
						break;
					}
				}
				if(meilleure_couleur>0)
				{
					if(couleurs_a_prendre.contains(meilleure_couleur))
					{
						if(numero==0)
						{
							raison[0]="";
							raison[1]=Couleur.values()[meilleure_couleur-1].toString();
						}
						else
						{
							carteAppelee=new CarteBelote((byte)11,meilleure_couleur);
						}
						return new Contrat("Autre couleur");
					}
				}
				return new Contrat("Passe");
			}
			if(egalite_repartition(repartition_atouts_2,repartition_possibles[5]))
			{/*Trois cartes d'une meme couleur et deux autres d'une meme couleur et une d'une derniere couleur*/
				for(MainBelote couleur_candidate:couleurs_candidates)
				{
					byte numero_couleur_candidate=couleur_candidate.carte(0).couleur();
					autres_couleurs=new Vector<Byte>();
					for(byte couleur=2;couleur<6;couleur++)
					{
						if(numero_couleur_candidate!=couleur&&!main(repartition_atouts,couleur).estVide())
						{
							autres_couleurs.addElement(couleur);
						}
					}
					if(couleur_candidate.total()==3)
					{
						nombre_as=0;
						nombre_couple_as_dix=0;
						for(byte couleur:autres_couleurs)
						{
							if(main(repartition_couleurs,couleur).carte(0).valeur()==1)
							{
								nombre_as++;
								if(main(repartition_couleurs,couleur).total()>1&&main(repartition_couleurs,couleur).carte(1).valeur()==10)
								{
									nombre_couple_as_dix++;
								}
							}
						}
						if(nombre_as>1&&nombre_couple_as_dix>0&&couleurs_a_prendre.contains(numero_couleur_candidate))
						{
							meilleure_couleur=numero_couleur_candidate;
							break;
						}
						if(couleur_candidate.nombre_plis_assures_min_atout()>1&&couleurs_a_prendre.contains(numero_couleur_candidate))
						{
							if(nombre_as>1)
							{
								meilleure_couleur=numero_couleur_candidate;
								break;
							}
							if(nombre_couple_as_dix>0)
							{
								meilleure_couleur=numero_couleur_candidate;
								break;
							}
						}
						if(couleur_candidate.carte(1).valeur()==9&&couleurs_a_prendre.contains(numero_couleur_candidate))
						{
							if(nombre_as>0)
							{
								meilleure_couleur=numero_couleur_candidate;
								break;
							}
						}
						if(couleur_candidate.carte(2).valeur()==1)
						{
							if(couleurs_a_prendre.contains(numero_couleur_candidate))
							{
								meilleure_couleur=numero_couleur_candidate;
								break;
							}
						}
					}
					else if(couleur_candidate.total()==2)
					{
						nombre_as=0;
						nombre_couple_as_dix=0;
						for(byte couleur:autres_couleurs)
						{
							if(main(repartition_couleurs,couleur).carte(0).valeur()==1)
							{
								nombre_as++;
								if(main(repartition_couleurs,couleur).total()>1&&main(repartition_couleurs,couleur).carte(1).valeur()==10)
								{
									nombre_couple_as_dix++;
								}
							}
						}
						if(nombre_as>1&&nombre_couple_as_dix>0&&couleurs_a_prendre.contains(numero_couleur_candidate))
						{
							meilleure_couleur=numero_couleur_candidate;
							break;
						}
						if(couleur_candidate.carte(1).valeur()==9&&couleurs_a_prendre.contains(numero_couleur_candidate))
						{
							if(nombre_as>0)
							{
								meilleure_couleur=numero_couleur_candidate;
								break;
							}
						}
					}
				}
				if(meilleure_couleur>0)
				{
					if(couleurs_a_prendre.contains(meilleure_couleur))
					{
						if(numero==0)
						{
							raison[0]="";
							raison[1]=Couleur.values()[meilleure_couleur-1].toString();
						}
						else
						{
							carteAppelee=new CarteBelote((byte)11,meilleure_couleur);
						}
						return new Contrat("Autre couleur");
					}
				}
				return new Contrat("Passe");
			}
			if(egalite_repartition(repartition_atouts_2,repartition_possibles[6]))
			{/*Trois cartes d'une meme couleur et trois singlettes*/
				meilleure_couleur=repartition_atouts_2.get(0).carte(0).couleur();
				if(repartition_atouts_2.get(0).nombre_plis_assures_min_atout()>0&&repartition_atouts_2.get(1).carte(0).valeur()==1&&repartition_atouts_2.get(2).carte(0).valeur()==1&&repartition_atouts_2.get(3).carte(0).valeur()==1)
				{
					if(couleurs_a_prendre.contains(meilleure_couleur))
					{
						if(numero==0)
						{
							raison[0]="";
							raison[1]=Couleur.values()[meilleure_couleur-1].toString();
						}
						else
						{
							carteAppelee=new CarteBelote((byte)11,meilleure_couleur);
						}
						return new Contrat("Autre couleur");
					}
				}
				if(repartition_atouts_2.get(0).carte(0).valeur()==9)
				{
					if(repartition_atouts_2.get(1).carte(0).valeur()==1||repartition_atouts_2.get(2).carte(0).valeur()==1||repartition_atouts_2.get(3).carte(0).valeur()==1)
					{
						if(couleurs_a_prendre.contains(meilleure_couleur))
						{
							if(numero==0)
							{
								raison[0]="";
								raison[1]=Couleur.values()[meilleure_couleur-1].toString();
							}
							else
							{
								carteAppelee=new CarteBelote((byte)11,meilleure_couleur);
							}
							return new Contrat("Autre couleur");
						}
					}
				}
				if(repartition_atouts_2.get(0).nombre_plis_assures_min_atout()>1)
				{
					nombre_as=0;
					for(byte indice_couleur=1;indice_couleur<4;indice_couleur++)
					{
						if(repartition_atouts_2.get(indice_couleur).carte(0).valeur()==1)
						{
							nombre_as++;
						}
					}
					if(nombre_as>1)
					{
						if(couleurs_a_prendre.contains(meilleure_couleur))
						{
							if(numero==0)
							{
								raison[0]="";
								raison[1]=Couleur.values()[meilleure_couleur-1].toString();
							}
							else
							{
								carteAppelee=new CarteBelote((byte)11,meilleure_couleur);
							}
							return new Contrat("Autre couleur");
						}
					}
				}
				return new Contrat("Passe");
			}
			if(egalite_repartition(repartition_atouts_2,repartition_possibles[7]))
			{/*Deux cartes par couleur, il y en a trois differentes dans la main*/
				for(MainBelote couleur_candidate:couleurs_candidates)
				{
					byte numero_couleur_candidate=couleur_candidate.carte(0).couleur();
					autres_couleurs=new Vector<Byte>();
					for(byte couleur=2;couleur<6;couleur++)
					{
						if(numero_couleur_candidate!=couleur&&!main(repartition_atouts,couleur).estVide())
						{
							autres_couleurs.addElement(couleur);
						}
					}
					if(couleurs_a_prendre.contains(numero_couleur_candidate))
					{
						if(couleur_candidate.carte(0).valeur()==11)
						{
							if(main(repartition_couleurs,autres_couleurs.get(0)).carte(0).valeur()==1&&main(repartition_couleurs,autres_couleurs.get(1)).carte(0).valeur()==1)
							{
								if(main(repartition_couleurs,autres_couleurs.get(0)).carte(1).valeur()==10||main(repartition_couleurs,autres_couleurs.get(1)).carte(1).valeur()==10)
								{
									meilleure_couleur=numero_couleur_candidate;
									break;
								}
							}
						}
						else
						{
							if(main(repartition_couleurs,autres_couleurs.get(0)).carte(1).valeur()==10&&main(repartition_couleurs,autres_couleurs.get(1)).carte(1).valeur()==10)
							{
								meilleure_couleur=numero_couleur_candidate;
								break;
							}
						}
					}
				}
				if(couleurs_a_prendre.contains(meilleure_couleur))
				{
					if(meilleure_couleur>0)
					{
						if(numero==0)
						{
							raison[0]="";
							raison[1]=Couleur.values()[meilleure_couleur-1].toString();
						}
						else
						{
							carteAppelee=new CarteBelote((byte)11,meilleure_couleur);
						}
						return new Contrat("Autre couleur");
					}
				}
			}
			if(egalite_repartition(repartition_atouts_2,repartition_possibles[8]))
			{/*Deux couleurs avec deux cartes chacune et deux singlettes*/
				for(MainBelote couleur_candidate:couleurs_candidates)
				{
					byte numero_couleur_candidate=couleur_candidate.carte(0).couleur();
					autres_couleurs=new Vector<Byte>();
					for(byte couleur=2;couleur<6;couleur++)
					{
						if(numero_couleur_candidate!=couleur&&!main(repartition_atouts,couleur).estVide())
						{
							autres_couleurs.addElement(couleur);
						}
					}
					if(couleurs_a_prendre.contains(numero_couleur_candidate))
					{
						nombre_as=0;
						nombre_couple_as_dix=0;
						for(byte couleur:autres_couleurs)
						{
							if(main(repartition_couleurs,couleur).carte(0).valeur()==1)
							{
								nombre_as++;
								if(main(repartition_couleurs,couleur).total()>1&&main(repartition_couleurs,couleur).carte(1).valeur()==10)
								{
									nombre_couple_as_dix++;
								}
							}
						}
						if(nombre_as>2)
						{
							meilleure_couleur=numero_couleur_candidate;
							break;
						}
						if(couleur_candidate.carte(0).valeur()==11)
						{
							if(nombre_as==2)
							{
								if(nombre_couple_as_dix>0)
								{
									meilleure_couleur=numero_couleur_candidate;
									break;
								}
							}
						}
					}
				}
				if(couleurs_a_prendre.contains(meilleure_couleur))
				{
					if(meilleure_couleur>0)
					{
						if(numero==0)
						{
							raison[0]="";
							raison[1]=Couleur.values()[meilleure_couleur-1].toString();
						}
						else
						{
							carteAppelee=new CarteBelote((byte)11,meilleure_couleur);
						}
						return new Contrat("Autre couleur");
					}
				}
			}
		}
		else
		{/*Premier tour d'enchere*/
			if(egalite_repartition(repartition_atouts_2,repartition_possibles[0]))
			{/*Six cartes d'une meme couleur*/
				if(numero==0)
				{
					raison[0]="Avec six cartes de meme couleur, vous assurez au moins "+repartition_atouts_2.get(0).nombre_plis_assures_min_atout()+" plis.";
					raison[1]=Couleur.values()[couleur_atout-1].toString();
				}
				else
				{
					carteAppelee=new CarteBelote((byte)11,couleur_atout);
				}
				return new Contrat("Couleur");
			}
			if(egalite_repartition(repartition_atouts_2,repartition_possibles[1]))
			{/*Cinq cartes d'une meme couleur et une autre d'une meme couleur*/
				meilleure_couleur=repartition_atouts_2.get(0).carte(0).couleur();
				if(numero==0)
				{
					if(meilleure_couleur==couleur_atout)
					{
						raison[0]="Avec cinq cartes de meme couleur, vous assurez au moins "+repartition_atouts_2.get(0).nombre_plis_assures_min_atout()+" plis.";
						raison[1]=Couleur.values()[couleur_atout-1].toString();
					}
					else
					{
						raison[0]="Attendez le tour suivant, pour prendre car avec cinq cartes de meme couleur, vous assurez au moins "+repartition_atouts_2.get(0).nombre_plis_assures_min_atout()+" plis a la couleur "+Couleur.values()[meilleure_couleur-1]+".";
					}
				}
				else
				{
					if(meilleure_couleur==couleur_atout)
					{
						carteAppelee=new CarteBelote((byte)11,meilleure_couleur);
					}
				}
				if(meilleure_couleur==couleur_atout)
				{
					return new Contrat("Couleur");
				}
				return new Contrat("Passe");
			}
			Vector<Byte> couleurs_avec_valet_sec=couleurs_avec_valet_sec(repartition_atouts_2);
			if(!couleurs_avec_valet_sec.isEmpty())
			{
				Vector<Byte> couleurs_as_dix=new Vector<Byte>();
				for(byte couleur=2;couleur<6;couleur++)
				{
					if(!main(repartition_couleurs,couleur).estVide())
					{
						if(main(repartition_couleurs,couleur).carte(0).valeur()==1)
						{
							nombre_as++;
							if(main(repartition_couleurs,couleur).total()>1&&main(repartition_couleurs,couleur).carte(1).valeur()==10)
							{
								nombre_couple_as_dix++;
								couleurs_as_dix.addElement(couleur);
							}
						}
					}
				}
				if(nombre_as>2||nombre_couple_as_dix>1)
				{
					if(couleurs_avec_valet_sec.contains(couleur_atout))
					{
						if(numero==0)
						{
							raison[0]="";
							if(nombre_as>2)
							{
								raison[0]+="Vous avez trois as dans les autres couleurs que "+Couleur.values()[couleur_atout-1]+" pour peut-etre assurer des plis en plus du valet sec.\n";
							}
							if(nombre_couple_as_dix>1)
							{
								raison[0]+="Vous avez "+nombre_couple_as_dix+" couples as - dix de meme couleur dans les couleurs ";
								for(byte couleur:couleurs_as_dix)
								{
									raison[0]+=Couleur.values()[couleur-1]+",";
								}
								raison[0]+=" pour peut-etre assurer des plis en plus du valet sec.\n";
							}
							raison[1]=Couleur.values()[couleur_atout-1].toString();
						}
						else
						{
							carteAppelee=new CarteBelote((byte)11,couleur_atout);
						}
						return new Contrat("Couleur");
					}
					return new Contrat("Passe");
				}
			}
			if(repartition_atouts_2.get(0).total()==4)
			{
				couleur_longue=repartition_atouts_2.get(0).carte(0).couleur();
				if(repartition_atouts_2.get(0).nombre_plis_assures_min_atout()>2)
				{
					if(couleur_atout==couleur_longue)
					{
						if(numero==0)
						{
							raison[0]="Vous avez quatre atouts a "+Couleur.values()[couleur_longue-1]+" et "+repartition_atouts_2.get(0).nombre_plis_assures_min_atout()+" a cette couleur.";
							raison[1]=Couleur.values()[couleur_atout-1].toString();
						}
						else
						{
							carteAppelee=new CarteBelote((byte)11,couleur_longue);
						}
						return new Contrat("Couleur");
					}
					return new Contrat("Passe");
				}
				if(egalite_repartition(repartition_atouts_2,repartition_possibles[2]))
				{/*Quatre cartes d'une meme couleur et deux autres d'une meme couleur*/
					byte autre_couleur=repartition_atouts_2.get(1).carte(0).couleur();
					if(main(repartition_couleurs,autre_couleur).carte(1).valeur()==10)
					{
						if(couleur_atout==couleur_longue)
						{
							if(numero==0)
							{
								raison[0]="Vous avez quatre atouts a "+Couleur.values()[couleur_longue-1]+" et un as et un dix a "+Couleur.values()[autre_couleur-1]+".";
								raison[1]=Couleur.values()[couleur_atout-1].toString();
							}
							else
							{
								carteAppelee=new CarteBelote((byte)11,couleur_longue);
							}
							return new Contrat("Couleur");
						}
						return new Contrat("Passe");
					}
					if(repartition_atouts_2.get(0).nombre_plis_assures_min_atout()==2)
					{
						if(main(repartition_couleurs,autre_couleur).carte(0).valeur()==1)
						{
							if(couleur_atout==couleur_longue)
							{
								if(numero==0)
								{
									raison[0]="Vous avez quatre atouts a "+Couleur.values()[couleur_longue-1]+" et un as a "+Couleur.values()[autre_couleur-1]+".";
									raison[1]=Couleur.values()[couleur_atout-1].toString();
								}
								else
								{
									carteAppelee=new CarteBelote((byte)11,couleur_longue);
								}
								return new Contrat("Couleur");
							}
							return new Contrat("Passe");
						}
					}
					if(repartition_atouts_2.get(1).carte(1).valeur()==9)
					{
						if(repartition_atouts_2.get(0).carte(0).valeur()==1)
						{
							if(couleur_atout==autre_couleur)
							{
								if(numero==0)
								{
									raison[0]="";
									raison[1]=Couleur.values()[couleur_atout-1].toString();
								}
								else
								{
									carteAppelee=new CarteBelote((byte)11,autre_couleur);
								}
								return new Contrat("Couleur");
							}
							return new Contrat("Passe");
						}
					}
				}
				if(egalite_repartition(repartition_atouts_2,repartition_possibles[3]))
				{/*Quatre cartes d'une meme couleur et deux autres de couleur differentes*/
					byte autre_couleur=repartition_atouts_2.get(1).carte(0).couleur();
					byte autre_couleur_2=repartition_atouts_2.get(2).carte(0).couleur();
					if(main(repartition_couleurs,autre_couleur).carte(0).valeur()==1&&main(repartition_couleurs,autre_couleur_2).carte(0).valeur()==1)
					{/*Deux as dans les autres couleurs*/
						if(couleur_atout==couleur_longue)
						{
							if(numero==0)
							{
								raison[0]="Vous avez quatre atouts a "+Couleur.values()[couleur_longue-1]+" et un as ";
								String[] chaines={"a "+Couleur.values()[autre_couleur-1],"a "+Couleur.values()[autre_couleur_2-1]};
								if(main(repartition_couleurs,autre_couleur).carte(0).valeur()==1)
								{
									raison[0]+=chaines[0];
									if(main(repartition_couleurs,autre_couleur_2).carte(0).valeur()==1)
									{
										raison[0]+=" et "+chaines[1]+".";
									}
									else
									{
										raison[0]+=".";
									}
								}
								else
								{
									raison[0]+=chaines[1]+".";
								}
								raison[1]=Couleur.values()[couleur_atout-1].toString();
							}
							else
							{
								carteAppelee=new CarteBelote((byte)11,couleur_longue);
							}
							return new Contrat("Couleur");
						}
						return new Contrat("Passe");
					}
					if(repartition_atouts_2.get(0).nombre_plis_assures_min_atout()>1)
					{
						if(main(repartition_couleurs,autre_couleur).carte(0).valeur()==1||main(repartition_couleurs,autre_couleur_2).carte(0).valeur()==1)
						{/*Deux as dans les autres couleurs*/
							if(couleur_atout==couleur_longue)
							{
								if(numero==0)
								{
									raison[0]="Vous avez quatre atouts a "+Couleur.values()[couleur_longue-1]+" et un as ";
									String[] chaines={"a "+Couleur.values()[autre_couleur-1],"a "+Couleur.values()[autre_couleur_2-1]};
									if(main(repartition_couleurs,autre_couleur).carte(0).valeur()==1)
									{
										raison[0]+=chaines[0];
										if(main(repartition_couleurs,autre_couleur_2).carte(0).valeur()==1)
										{
											raison[0]+=" et "+chaines[1]+".";
										}
										else
										{
											raison[0]+=".";
										}
									}
									else
									{
										raison[0]+=chaines[1]+".";
									}
									raison[1]=Couleur.values()[couleur_atout-1].toString();
								}
								else
								{
									carteAppelee=new CarteBelote((byte)11,couleur_longue);
								}
								return new Contrat("Couleur");
							}
							return new Contrat("Passe");
						}
					}
				}
				if(repartition_atouts_2.get(0).nombre_plis_assures_min_atout()<2)
				{
					if(numero==0)
					{
						raison[0]="";
					}
				}
				return new Contrat("Passe");
			}
			trier_couleurs_candidates(couleurs_candidates);
			Vector<Byte> autres_couleurs=new Vector<Byte>();
			Vector<Byte> couleurs_potables=new Vector<Byte>();
			if(egalite_repartition(repartition_atouts_2,repartition_possibles[4]))
			{/*Trois cartes d'une meme couleur et trois autres d'une meme couleur*/
				for(MainBelote couleur_candidate:couleurs_candidates)
				{
					byte numero_couleur_candidate=couleur_candidate.carte(0).couleur();
					autres_couleurs=new Vector<Byte>();
					for(byte couleur=2;couleur<6;couleur++)
					{
						if(numero_couleur_candidate!=couleur&&!main(repartition_atouts,couleur).estVide())
						{
							autres_couleurs.addElement(couleur);
						}
					}
					if(main(repartition_couleurs,autres_couleurs.get(0)).carte(1).valeur()==10)
					{
						couleurs_potables.addElement(numero_couleur_candidate);
					}
					else if(couleur_candidate.carte(1).valeur()==9)
					{
						if(main(repartition_couleurs,autres_couleurs.get(0)).carte(0).valeur()==1)
						{
							couleurs_potables.addElement(numero_couleur_candidate);
						}
					}
				}
				if(!couleurs_potables.isEmpty())
				{
					if(couleur_atout==couleurs_potables.get(0))
					{
						if(numero==0)
						{
							raison[0]="La couleur "+Couleur.values()[couleurs_potables.get(0)-1]+" est la meilleure pour prendre";
							raison[1]=Couleur.values()[couleur_atout-1].toString();
						}
						else
						{
							carteAppelee=new CarteBelote((byte)11,couleurs_potables.get(0));
						}
						return new Contrat("Couleur");
					}
					if(numero==0)
					{
						raison[0]="Prendre a la premiere couleur qui est "+Couleur.values()[couleur_atout-1]+" est possible pour gagner,\n";
						raison[0]+=" mais il existe une autre couleur plus forte qui est "+Couleur.values()[couleurs_potables.get(0)-1];
					}
				}
				return new Contrat("Passe");
			}
			if(egalite_repartition(repartition_atouts_2,repartition_possibles[5]))
			{/*Trois cartes d'une meme couleur et deux autres d'une meme couleur et une d'une derniere couleur*/
				for(MainBelote couleur_candidate:couleurs_candidates)
				{
					byte numero_couleur_candidate=couleur_candidate.carte(0).couleur();
					autres_couleurs=new Vector<Byte>();
					for(byte couleur=2;couleur<6;couleur++)
					{
						if(numero_couleur_candidate!=couleur&&!main(repartition_atouts,couleur).estVide())
						{
							autres_couleurs.addElement(couleur);
						}
					}
					if(couleur_candidate.total()==3)
					{
						nombre_as=0;
						nombre_couple_as_dix=0;
						for(byte couleur:autres_couleurs)
						{
							if(main(repartition_couleurs,couleur).carte(0).valeur()==1)
							{
								nombre_as++;
								if(main(repartition_couleurs,couleur).total()>1&&main(repartition_couleurs,couleur).carte(1).valeur()==10)
								{
									nombre_couple_as_dix++;
								}
							}
						}
						if(nombre_as>1&&nombre_couple_as_dix>0)
						{
							couleurs_potables.addElement(numero_couleur_candidate);
						}
						else if(couleur_candidate.nombre_plis_assures_min_atout()>1)
						{
							if(nombre_as>1)
							{
								couleurs_potables.addElement(numero_couleur_candidate);
							}
							else if(nombre_couple_as_dix>0)
							{
								couleurs_potables.addElement(numero_couleur_candidate);
							}
						}
						else if(couleur_candidate.carte(1).valeur()==9)
						{
							if(nombre_as>0)
							{
								couleurs_potables.addElement(numero_couleur_candidate);
							}
						}
					}
					else if(couleur_candidate.total()==2)
					{
						nombre_as=0;
						nombre_couple_as_dix=0;
						for(byte couleur:autres_couleurs)
						{
							if(main(repartition_couleurs,couleur).carte(0).valeur()==1)
							{
								nombre_as++;
								if(main(repartition_couleurs,couleur).total()>1&&main(repartition_couleurs,couleur).carte(1).valeur()==10)
								{
									nombre_couple_as_dix++;
								}
							}
						}
						if(nombre_as>1&&nombre_couple_as_dix>0)
						{
							couleurs_potables.addElement(numero_couleur_candidate);
						}
						else if(couleur_candidate.carte(1).valeur()==9)
						{
							if(nombre_as>0)
							{
								couleurs_potables.addElement(numero_couleur_candidate);;
							}
						}
					}
				}
				if(!couleurs_potables.isEmpty())
				{
					if(couleur_atout==couleurs_potables.get(0))
					{
						if(numero==0)
						{
							raison[0]="La couleur "+Couleur.values()[couleur_atout-1]+" est la meilleure pour prendre";
							raison[1]=Couleur.values()[couleur_atout-1].toString();
						}
						else
						{
							carteAppelee=new CarteBelote((byte)11,couleur_atout);
						}
						return new Contrat("Couleur");
					}
					if(numero==0)
					{
						raison[0]="Prendre a la premiere couleur qui est "+Couleur.values()[couleur_atout-1]+" est possible pour gagner mais il existe une autre couleur plus forte qui est "+couleurs_potables.get(0);
					}
				}
				if(numero==0)
				{
					raison[0]="Vous n'avez aucune couleur d'atout ou vous avez du jeu dans les autres couleurs (ax -dix)";
				}
				return new Contrat("Passe");
			}
			if(egalite_repartition(repartition_atouts_2,repartition_possibles[6]))
			{/*Trois cartes d'une meme couleur et trois singlettes*/
				meilleure_couleur=repartition_atouts_2.get(0).carte(0).couleur();
				if(repartition_atouts_2.get(0).nombre_plis_assures_min_atout()>0&&repartition_atouts_2.get(1).carte(0).valeur()==1&&repartition_atouts_2.get(2).carte(0).valeur()==1&&repartition_atouts_2.get(3).carte(0).valeur()==1)
				{
					if(couleur_atout==meilleure_couleur)
					{
						if(numero==0)
						{
							raison[0]="";
							raison[1]=Couleur.values()[couleur_atout-1].toString();
						}
						else
						{
							carteAppelee=new CarteBelote((byte)11,meilleure_couleur);
						}
						return new Contrat("Couleur");
					}
				}
				if(repartition_atouts_2.get(0).carte(0).valeur()==9)
				{
					if(repartition_atouts_2.get(1).carte(0).valeur()==1||repartition_atouts_2.get(2).carte(0).valeur()==1||repartition_atouts_2.get(3).carte(0).valeur()==1)
					{
						if(couleur_atout==meilleure_couleur)
						{
							if(numero==0)
							{
								raison[0]="";
								raison[1]=Couleur.values()[couleur_atout-1].toString();
							}
							else
							{
								carteAppelee=new CarteBelote((byte)11,meilleure_couleur);
							}
							return new Contrat("Couleur");
						}
					}
				}
				if(repartition_atouts_2.get(0).nombre_plis_assures_min_atout()>1)
				{
					nombre_as=0;
					for(byte indice_couleur=1;indice_couleur<4;indice_couleur++)
					{
						if(repartition_atouts_2.get(indice_couleur).carte(0).valeur()==1)
						{
							nombre_as++;
						}
					}
					if(nombre_as>1)
					{
						if(couleur_atout==meilleure_couleur)
						{
							if(numero==0)
							{
								raison[0]="";
								raison[1]=Couleur.values()[couleur_atout-1].toString();
							}
							else
							{
								carteAppelee=new CarteBelote((byte)11,meilleure_couleur);
							}
							return new Contrat("Couleur");
						}
					}
				}
				return new Contrat("Passe");
			}
			if(egalite_repartition(repartition_atouts_2,repartition_possibles[7]))
			{/*Deux cartes par couleur, il y en a trois differentes dans la main*/
				for(MainBelote couleur_candidate:couleurs_candidates)
				{
					byte numero_couleur_candidate=couleur_candidate.carte(0).couleur();
					autres_couleurs=new Vector<Byte>();
					for(byte couleur=2;couleur<6;couleur++)
					{
						if(numero_couleur_candidate!=couleur&&!main(repartition_atouts,couleur).estVide())
						{
							autres_couleurs.addElement(couleur);
						}
					}
					if(couleur_atout==numero_couleur_candidate)
					{
						if(couleur_candidate.carte(0).valeur()==11)
						{
							if(main(repartition_couleurs,autres_couleurs.get(0)).carte(0).valeur()==1&&main(repartition_couleurs,autres_couleurs.get(1)).carte(0).valeur()==1)
							{
								if(main(repartition_couleurs,autres_couleurs.get(0)).carte(1).valeur()==10||main(repartition_couleurs,autres_couleurs.get(1)).carte(1).valeur()==10)
								{
									couleurs_potables.addElement(numero_couleur_candidate);
								}
							}
						}
						else
						{
							if(main(repartition_couleurs,autres_couleurs.get(0)).carte(1).valeur()==10&&main(repartition_couleurs,autres_couleurs.get(1)).carte(1).valeur()==10)
							{
								couleurs_potables.addElement(numero_couleur_candidate);
							}
						}
					}
				}
				if(!couleurs_potables.isEmpty())
				{
					if(couleur_atout==couleurs_potables.get(0))
					{
						if(numero==0)
						{
							raison[0]="La couleur "+Couleur.values()[couleur_atout-1]+" est la meilleure pour prendre";
							raison[1]=Couleur.values()[couleur_atout-1].toString();
						}
						else
						{
							carteAppelee=new CarteBelote((byte)11,couleur_atout);
						}
						return new Contrat("Couleur");
					}
					if(numero==0)
					{
						raison[0]="Prendre a la premiere couleur qui est "+Couleur.values()[couleur_atout-1]+" est possible pour gagner mais il existe une autre couleur plus forte qui est "+Couleur.values()[couleurs_potables.get(0)-1];
					}
				}
				return new Contrat("Passe");
			}
			if(egalite_repartition(repartition_atouts_2,repartition_possibles[8]))
			{/*Deux couleurs avec deux cartes chacune et deux singlettes*/
				for(MainBelote couleur_candidate:couleurs_candidates)
				{
					byte numero_couleur_candidate=couleur_candidate.carte(0).couleur();
					autres_couleurs=new Vector<Byte>();
					for(byte couleur=2;couleur<6;couleur++)
					{
						if(numero_couleur_candidate!=couleur&&!main(repartition_atouts,couleur).estVide())
						{
							autres_couleurs.addElement(couleur);
						}
					}
					if(couleur_atout==numero_couleur_candidate)
					{
						nombre_as=0;
						nombre_couple_as_dix=0;
						for(byte couleur:autres_couleurs)
						{
							if(main(repartition_couleurs,couleur).carte(0).valeur()==1)
							{
								nombre_as++;
								if(main(repartition_couleurs,couleur).total()>1&&main(repartition_couleurs,couleur).carte(1).valeur()==10)
								{
									nombre_couple_as_dix++;
								}
							}
						}
						if(nombre_as>2)
						{
							couleurs_potables.addElement(numero_couleur_candidate);
						}
						if(couleur_candidate.carte(0).valeur()==11)
						{
							if(nombre_as==2)
							{
								if(nombre_couple_as_dix>0)
								{
									couleurs_potables.addElement(numero_couleur_candidate);
								}
							}
						}
					}
				}
				if(!couleurs_potables.isEmpty())
				{
					if(couleur_atout==couleurs_potables.get(0))
					{
						if(numero==0)
						{
							raison[0]="La couleur "+Couleur.values()[couleurs_potables.get(0)-1]+" est la meilleure pour prendre";
							raison[1]=Couleur.values()[couleur_atout-1].toString();
						}
						else
						{
							carteAppelee=new CarteBelote((byte)11,couleurs_potables.get(0));
						}
						return new Contrat("Couleur");
					}
					if(numero==0)
					{
						raison[0]="Prendre a la premiere couleur qui est "+Couleur.values()[couleurs_potables.get(0)-1]+" est possible pour gagner mais il existe une autre couleur plus forte qui est "+couleurs_potables.get(0);
					}
				}
				return new Contrat("Passe");
			}
		}
		return new Contrat("Passe");
	}
	private Vector<MainBelote> couleurs_assurant_un_pli(Vector<MainBelote> repartition)
	{
		Vector<MainBelote> couleurs_assurant_un_pli=new Vector<MainBelote>();
		for(MainBelote couleur:repartition)
		{
			if(couleur.nombre_plis_assures_min_atout()>0)
			{
				couleurs_assurant_un_pli.addElement(couleur);
			}
		}
		return couleurs_assurant_un_pli;
	}
	private void trier_couleurs_longueurs(Vector<MainBelote> repartition)
	{
		for(int indice_couleur=0;indice_couleur<repartition.size();indice_couleur++)
		{
			for(int indice_couleur_2=indice_couleur+1;indice_couleur_2<repartition.size();indice_couleur_2++)
			{
				if(repartition.get(indice_couleur).total()<repartition.get(indice_couleur_2).total())
				{
					repartition.setElementAt(repartition.set(indice_couleur,repartition.get(indice_couleur_2)),indice_couleur_2);
				}
			}
		}
	}
	/**On ne trie que les couleurs candidates pour prendre avec deux ou trois cartes d'une meme couleur*/
	private void trier_couleurs_candidates(Vector<MainBelote> repartition)
	{
		for(int indice_couleur=0;indice_couleur<repartition.size();indice_couleur++)
		{
			for(int indice_couleur_2=indice_couleur+1;indice_couleur_2<repartition.size();indice_couleur_2++)
			{
				boolean egal=true;
				byte couleur_1=repartition.get(indice_couleur).carte(0).couleur();
				byte couleur_2=repartition.get(indice_couleur_2).carte(0).couleur();
				int min_taille=Math.min(repartition.get(indice_couleur).total(),repartition.get(indice_couleur_2).total());
				for(byte indice_carte=0;indice_carte<min_taille;indice_carte++)
				{
					if(((CarteBelote)repartition.get(indice_couleur).carte(indice_carte)).force(couleur_1,couleur_1)<((CarteBelote)repartition.get(indice_couleur_2).carte(indice_carte)).force(couleur_2,couleur_2))
					{
						repartition.setElementAt(repartition.set(indice_couleur,repartition.get(indice_couleur_2)),indice_couleur_2);
						egal=false;
						break;
					}
					else if(((CarteBelote)repartition.get(indice_couleur).carte(indice_carte)).force(couleur_1,couleur_1)>((CarteBelote)repartition.get(indice_couleur_2).carte(indice_carte)).force(couleur_2,couleur_2))
					{
						egal=false;
						break;
					}
				}
				if(egal&&repartition.get(indice_couleur).total()<repartition.get(indice_couleur_2).total())
				{
					repartition.setElementAt(repartition.set(indice_couleur,repartition.get(indice_couleur_2)),indice_couleur_2);
				}
			}
		}
	}
	private boolean egalite_repartition(Vector<MainBelote> repartition_couleurs_tries_longueurs,byte[] totaux)
	{
		byte indice=0;
		for(MainBelote couleur:repartition_couleurs_tries_longueurs)
		{
			if(couleur.total()!=totaux[indice])
			{
				return false;
			}
			indice++;
		}
		return true;
	}
	private Vector<Byte> couleurs_avec_valet_sec(Vector<MainBelote> repartition_couleurs_tries_longueurs)
	{
		Vector<Byte> couleurs_avec_valet_sec=new Vector<Byte>();
		for(MainBelote couleur:repartition_couleurs_tries_longueurs)
		{
			if(couleur.total()==1&&couleur.carte(0).valeur()==11)
			{
				couleurs_avec_valet_sec.addElement(couleur.carte(0).couleur());
			}
		}
		return couleurs_avec_valet_sec;
	}
	public boolean getFinEncherePremierTour()
	{
		return finEncheresPremierTour;
	}
	public void finEncherePremierTour()
	{
		finEncheresPremierTour=!finEncheresPremierTour;
	}
	public void finEnchere()
	{
		finEncheres=true;
	}
	public void debutEnchere()
	{
		finEncheres=false;
	}
	public boolean getFinEnchere()
	{
		return finEncheres;
	}
	public void ajouterContrat(Contrat c)
	{
		contrats.addElement(c);
	}
	public int taille_contrats()
	{
		return contrats.size();
	}
	public Contrat contrat(int i)
	{
		return contrats.get(i);
	}
	public Vector<Contrat> tous_contrats()
	{
		return contrats;
	}
	public Contrat getContrat()
	{
		return contrat;
	}
	public void setContrat(Contrat pcontrat)
	{
		contrat=pcontrat;
	}
	public int max_contrat()
	{
		if(avecSurContrat())
		{
			return new Contrat(EncheresBelote.Tout_atout).force();
		}
		return new Contrat(EncheresBelote.Couleur).force();
	}
	public void setPreneur(byte ppreneur)
	{
		preneur=ppreneur;
	}
	public byte getPreneur()
	{
		return preneur;
	}
	public Carte getCarteAppelee() {
		return carteAppelee;
	}
	public void setCarteAppelee(Carte c)
	{
		carteAppelee=c;
	}
	/**Appelee au debut d'une partie*/
	public void setEntameur(byte i)
	{
		entameur=i;
	}
	public boolean premierTour()
	{
		int total_plis=0;
		for(Vector<Pli> plis_equipe:plis)
		{
			total_plis+=plis_equipe.size();
		}
		return total_plis==0;
	}
	/**Appele au debut d'un pli mais pas d'une partie*/
	public void setEntameur()
	{
		entameur=ramasseur;
	}
	public byte getEntameur()
	{
		return entameur;
	}
	public void setPliEnCours()
	{
		pliEnCours=new Pli(new MainBelote(),entameur);
	}
	public Pli getPliEnCours()
	{
		return pliEnCours;
	}
	public Vector<Annonce> strategieAnnonces(byte numeroJoueur, String[] raison,Carte ct,boolean premier_tour)
	{
		Vector<Annonce> annonces_possibles=getAnnoncesPossibles(numeroJoueur);
		Vector<Annonce> vinter=new Vector<Annonce>();
		if(contrat.force()==1)
		{
			if(ct.couleur()==carteAppelee.couleur()&&ct.valeur()>12&&annonces_possibles.contains(new Annonce(Annonce.belote_rebelote)))
			{
				raison[0]="Pour jouer la "+Annonce.belote_rebelote;
				vinter.addElement(new Annonce(Annonce.belote_rebelote));
			}
		}
		return vinter;
	}
	public Vector<Annonce> getAnnoncesPossibles(byte numero)
	{
		Vector<Annonce> annonces_possibles=new Vector<Annonce>();
		if(autorise_annonce(new Annonce(Annonce.belote_rebelote),numero))
		{
			annonces_possibles.addElement(new Annonce(Annonce.belote_rebelote));
		}
		return annonces_possibles;
	}
	public void setAnnonces(byte b,Vector<Annonce> ann)
	{
		annonces.setElementAt(ann,b);
	}
	public Vector<Annonce> getAnnonces(byte numero)
	{
		return annonces.get(numero);
	}
	public void ajouterAnnonces(byte b,Vector<Annonce> ann)
	{
		annonces.get(b).addAll(ann);
	}
	public Carte strategieJeuCarteUnique(String[] raison)
	{
		if(pliEnCours.estVide())
			return entame(raison);
		else if(pliEnCours.total()<getNombreDeJoueurs()-1)
			return en_cours(raison);
		else
			return fin(raison);
	}
	private Carte entame(String[] raison)
	{
		byte numero=pliEnCours.getEntameur();
		MainBelote main_joueur=(MainBelote)getDistribution().main(numero);
		byte couleur_atout=couleur_atout();
		Vector<MainBelote> repartition=main_joueur.couleurs(couleur_atout,contrat);
		MainBelote cartes_jouables=cartes_jouables(repartition,numero);
		MainBelote cartesJouees=cartesJouees();
		Vector<MainBelote> repartitionCartesJouees=cartesJouees.couleurs(couleur_atout,contrat);
		Vector<Vector<MainBelote>> suites=new Vector<Vector<MainBelote>>();
		Vector<Pli> plisFaits=unionPlis();
		if(cartes_jouables.total()==1)
		{
			return cartes_jouables.carte(0);
		}
		for(byte couleur=2;couleur<6;couleur++)
		{
			suites.addElement(main(repartition,couleur).eclater(repartitionCartesJouees,couleur_atout));
		}
		Vector<Vector<MainBelote>> cartes_possibles=cartesPossibles(repartitionCartesJouees,plisFaits,repartition, numero,couleur_atout);
		Vector<Vector<MainBelote>> cartes_certaines=cartesCertaines(cartes_possibles);
		boolean strict_maitre_atout;
		Vector<Byte> couleurs_maitres=CouleursMaitres(suites, repartitionCartesJouees, cartes_possibles,numero);
		byte indice_couleur_jouer=0;
		Vector<MainBelote> cartes_maitresses=cartesMaitresses(repartition,repartitionCartesJouees);
		byte appele=(byte)((preneur+2)%4);
		byte partenaire=(byte)((numero+2)%4);
		byte[] adversaire=new byte[]{(byte)((numero+1)%getNombreDeJoueurs()),(byte)((numero+3)%getNombreDeJoueurs())};
		boolean maitre_jeu;
		if(contrat.force()==1)
		{
			strict_maitre_atout=StrictMaitreAtout(cartes_possibles,numero,suite(suites,couleur_atout),repartitionCartesJouees);
			maitre_jeu=strict_maitre_atout&&couleurs_maitres.size()==3;
			Vector<Byte> couleurs_non_atouts=couleurs_non_atouts();
			if(maitre_jeu)
			{
				if(!suite(suites,couleur_atout).isEmpty())
				{
					raison[0]="Vous allez ramasser d'abord tous les atouts des autres joueurs, puis vous jouerez dans les couleurs.";
					return main(suites,couleur_atout,0).carte(0);
				}
				for(byte couleur:couleurs_maitres)
				{
					if(!main(repartition,couleur).estVide())
					{
						indice_couleur_jouer=couleur;
						break;
					}
				}
				raison[0]="Vous allez maintenant ramasser les plis de toutes les couleurs en commencant par les cartes maitresses.";
				return main(repartition,indice_couleur_jouer).carte(0);
			}
			if(main(repartition,couleur_atout).total()==main_joueur.total())
			{/*Si le joueur ne possede que de l'atout*/
				if(suite(suites,couleur_atout).size()==1)
				{
					if(main(suites,couleur_atout,0).total()==main(cartes_maitresses,couleur_atout).total())
					{/*Si le joueur n'a que des atouts maitres*/
						return main(repartition,couleur_atout).carte(0);
					}
					return main(repartition,couleur_atout).derniereCarte();
				}
				if(main(cartes_maitresses,couleur_atout).estVide())
				{
					return main(repartition,couleur_atout).derniereCarte();
				}
				if(main_joueur.total()<=2*main(cartes_maitresses,couleur_atout).total())
				{/*La main du joueur contient plus d atouts maitres que des atouts non maitres*/
					return main(suites,couleur_atout,0).carte(0);
				}
				return main(repartition,couleur_atout).derniereCarte();
			}
			if(main(repartition,couleur_atout).total()+1==main_joueur.total())
			{
				for(byte couleur:couleurs_non_atouts)
				{/*On cherche la couleur autre que l'atout non vide*/
					if(!main(repartition,couleur).estVide())
					{
						indice_couleur_jouer=couleur;
						break;
					}
				}
				if(!main(cartes_maitresses,indice_couleur_jouer).estVide())
				{
					if(main(suites,couleur_atout,0).total()==main(cartes_maitresses,couleur_atout).total())
					{/*Si le joueur n'a que des atouts maitres*/
						return main(repartition,couleur_atout).carte(0);
					}
					if(((CarteBelote)main(repartition,indice_couleur_jouer).carte(0)).points(contrat, carteAppelee)==0)
					{
						return main(repartition,indice_couleur_jouer).carte(0);
					}
					if(main_joueur.total()==2)
					{
						return main(repartition,indice_couleur_jouer).carte(0);
					}
					if(main(cartes_maitresses,couleur_atout).estVide())
					{
						return main(repartition,indice_couleur_jouer).carte(0);
					}
					return main(repartition,couleur_atout).carte(0);
				}
				if(peut_couper(indice_couleur_jouer,partenaire,cartes_possibles))
				{
					return main(repartition,indice_couleur_jouer).carte(0);
				}
				if(strict_maitre_atout)
				{
					return main(repartition,couleur_atout).carte(0);
				}
				if(main(repartition,couleur_atout).total()==main(cartes_maitresses,couleur_atout).total())
				{
					boolean coupe=true;
					for(byte joueur:adversaire)
					{
						coupe&=peut_couper(indice_couleur_jouer,joueur,cartes_possibles);
					}
					if(coupe)
					{
						return main(repartition,couleur_atout).carte(0);
					}
				}
				return main(repartition,indice_couleur_jouer).carte(0);
			}
			if(numero==preneur)
			{
				boolean trois_couleurs_carte_maitre=true;
				for(byte couleur:couleurs_non_atouts)
				{
					if(!main(repartition,couleur).estVide())
					{
						trois_couleurs_carte_maitre&=!main(cartes_maitresses,couleur).estVide()&&((CarteBelote)main(cartes_maitresses,couleur).carte(0)).points(contrat,carteAppelee)>0;
					}
				}
				Vector<Byte> couleurs=new Vector<Byte>();
				if(trois_couleurs_carte_maitre)
				{
					boolean ne_peut_pas_couper=true;
					for(byte joueur:adversaire)
					{
						ne_peut_pas_couper&=main(cartes_possibles,couleur_atout,joueur).estVide();
					}
					if(ne_peut_pas_couper)
					{
						Vector<Byte> couleurs_non_vides=new Vector<Byte>();
						for(byte couleur:couleurs_non_atouts)
						{
							if(!main(repartition,couleur).estVide())
							{
								couleurs_non_vides.addElement(couleur);
							}
						}
						if(!couleurs_non_vides.isEmpty())
						{/*Si il existe une carte de couleur autre que l'atout*/
							return carte_maitresse(couleurs_non_vides, cartes_maitresses, repartition, repartitionCartesJouees, cartes_possibles, numero, suites);
						}
					}
					if(strict_maitre_atout)
					{
						return main(repartition,couleur_atout).carte(0);
					}
					boolean peut_couper=false;
					for(byte couleur:couleurs_non_atouts)
					{
						peut_couper=false;
						for(byte joueur:adversaire)
						{
							peut_couper|=peut_couper(couleur,joueur,cartes_possibles);
						}
						if(peut_couper&&!main(repartition,couleur).estVide())
						{
							couleurs.addElement(couleur);
						}
					}
					if(!couleurs.isEmpty())
					{
						return faire_couper_adv(couleurs,repartition,repartitionCartesJouees);
					}
					for(byte couleur:couleurs_non_atouts)
					{
						if(peut_couper(couleur, appele, cartes_possibles)&&!main(repartition,couleur).estVide())
						{
							couleurs.addElement(couleur);
						}
					}
					if(!couleurs.isEmpty())
					{
						return faire_couper_appele(couleurs,repartition,repartitionCartesJouees);
					}
					for(byte couleur:couleurs_non_atouts)
					{
						if(!tours(couleur, plisFaits).isEmpty()&&!main(repartition,couleur).estVide())
						{
							couleurs.addElement(couleur);
						}
					}
					if(!couleurs.isEmpty())
					{
						return ouvrir_couleur(couleurs,repartition);
					}
					return ouvrir_couleur(couleurs_non_atouts,repartition);
				}
				for(byte couleur:couleurs_non_atouts)
				{
					if(!main(repartition,couleur).estVide()&&main(cartes_maitresses,couleur).estVide())
					{
						couleurs.addElement(couleur);
					}
				}
				if(!couleurs.isEmpty())
				{
					return faire_couper_adv(couleurs, repartition, repartitionCartesJouees);
				}
				for(byte couleur:couleurs_non_atouts)
				{
					if(!main(repartition,couleur).estVide())
					{
						couleurs.addElement(couleur);
					}
				}
				return faire_couper_adv(couleurs, repartition, repartitionCartesJouees);
			}
			Vector<Byte> couleurs=new Vector<Byte>();
			if(numero==appele)
			{
				if(!main(cartes_certaines,couleur_atout,preneur).estVide()&&((CarteBelote)main(cartes_certaines,couleur_atout,preneur).carte(0)).force(couleur_atout,couleur_atout,contrat)==8)
				{
					if(!main(repartition,couleur_atout).estVide())
					{
						boolean trois_couleurs_carte_maitre=true;
						for(byte couleur:couleurs_non_atouts)
						{
							trois_couleurs_carte_maitre&=!main(cartes_maitresses,couleur).estVide();
						}
						if(trois_couleurs_carte_maitre||((CarteBelote)main(repartition,couleur_atout).carte(0)).force(couleur_atout,couleur_atout,contrat)<7)
						{
							return main(repartition,couleur_atout).carte(0);
						}
					}
				}
				if(!main(repartition,couleur_atout).estVide()&&((CarteBelote)main(repartition,couleur_atout).derniereCarte()).force(couleur_atout,couleur_atout,contrat)<5)
				{
					return main(repartition,couleur_atout).derniereCarte();
				}
				CarteBelote carte_dessus=(CarteBelote)getDistribution().derniereMain().carte(0);
				byte couleur_dessus=carte_dessus.couleur();
				if(couleur_dessus!=couleur_atout&&carte_dessus.force(couleur_atout,couleur_dessus,contrat)==8)
				{
					if(!main(cartes_certaines,couleur_dessus,preneur).estVide()&&((CarteBelote)main(cartes_certaines,couleur_dessus,preneur).carte(0)).force(couleur_atout,couleur_dessus,contrat)==8)
					{
						if(tours(couleur_dessus, plisFaits).isEmpty()&&!main(repartition,couleur_dessus).estVide()&&main(repartition,couleur_atout).estVide())
						{
							return main(repartition,couleur_dessus).carte(0);
						}
					}
				}
				/*On considere que l'appele est place apres le preneur*/
				for(byte couleur:couleurs_non_atouts)
				{
					if(!main(repartition,couleur).estVide()&&tours(couleur, plisFaits).isEmpty())
					{
						couleurs.addElement(couleur);
					}
				}
				if(!couleurs.isEmpty())
				{
					return ouvrir(couleurs, repartition, repartitionCartesJouees, cartes_maitresses);
				}
				for(byte couleur:couleurs_non_atouts)
				{
					if(!main(repartition,couleur).estVide()&&peut_couper(couleur, preneur, cartes_possibles))
					{
						if(((CarteBelote)main(repartition,couleur).carte(0)).points(contrat, carteAppelee)>0)
						{
							couleurs.addElement(couleur);
						}
					}
				}
				if(!couleurs.isEmpty())
				{
					return faire_couper_preneur_figure(couleurs,repartition);
				}
				for(byte couleur:couleurs_non_atouts)
				{
					if(!main(repartition,couleur).estVide()&&peut_couper(couleur, preneur, cartes_possibles))
					{
						if(nombre_cartes_points(repartitionCartesJouees,couleur)<5)
						{
							couleurs.addElement(couleur);
						}
					}
				}
				if(!couleurs.isEmpty())
				{
					return faire_tomber_points_pour_preneur(couleurs,repartition,repartitionCartesJouees);
				}
				for(byte couleur:couleurs_non_atouts)
				{
					boolean defausse=defausse(couleur,adversaire[0],cartes_possibles)||defausse(couleur,adversaire[1],cartes_possibles);
					if(!main(repartition,couleur).estVide()&&defausse&&!(peut_couper(couleur,adversaire[0],cartes_possibles)||peut_couper(couleur,adversaire[1],cartes_possibles)))
					{
						couleurs.addElement(couleur);
					}
				}
				if(!couleurs.isEmpty())
				{
					return ouvrir(couleurs,repartition,repartitionCartesJouees,cartes_maitresses);
				}
				for(byte couleur:couleurs_non_atouts)
				{
					if(!main(repartition,couleur).estVide()&&(peut_couper(couleur,adversaire[0],cartes_possibles)||peut_couper(couleur,adversaire[1],cartes_possibles)))
					{
						couleurs.addElement(couleur);
					}
				}
				if(!couleurs.isEmpty())
				{
					return faire_couper_adv(couleurs,repartition,repartitionCartesJouees);
				}
				for(byte couleur:couleurs_non_atouts)
				{
					if(!main(repartition,couleur).estVide())
					{
						couleurs.addElement(couleur);
					}
				}
				return faire_couper_adv(couleurs,repartition,repartitionCartesJouees);
			}
			/*Cas d'un defenseur*/
			for(byte couleur:couleurs_non_atouts)
			{
				if(!main(repartition,couleur).estVide()&&tours(couleur, plisFaits).isEmpty())
				{
					couleurs.addElement(couleur);
				}
			}
			if(!couleurs.isEmpty())
			{
				return ouvrir(couleurs, repartition, repartitionCartesJouees, cartes_maitresses);
			}
			for(byte couleur:couleurs_non_atouts)
			{
				if(!main(repartition,couleur).estVide()&&(peut_couper(couleur,adversaire[0],cartes_possibles)||peut_couper(couleur,adversaire[1],cartes_possibles)))
				{
					couleurs.addElement(couleur);
				}
			}
			if(!couleurs.isEmpty())
			{
				return faire_couper_adv(couleurs,repartition,repartitionCartesJouees);
			}
			for(byte couleur:couleurs_non_atouts)
			{
				boolean defausse=defausse(couleur,adversaire[0],cartes_possibles)||defausse(couleur,adversaire[1],cartes_possibles);
				if(!main(repartition,couleur).estVide()&&defausse&&!(peut_couper(couleur,adversaire[0],cartes_possibles)||peut_couper(couleur,adversaire[1],cartes_possibles)))
				{
					couleurs.addElement(couleur);
				}
			}
			if(!couleurs.isEmpty())
			{
				return ouvrir(couleurs,repartition,repartitionCartesJouees,cartes_maitresses);
			}
			for(byte couleur:couleurs_non_atouts)
			{
				if(!main(repartition,couleur).estVide())
				{
					couleurs.addElement(couleur);
				}
			}
			return faire_couper_adv(couleurs,repartition,repartitionCartesJouees);
		}
		/*Jeu sans atout ou tout atout*/
		maitre_jeu=couleurs_maitres.size()==4;
		Vector<Byte> couleurs_non_atouts=couleurs_non_atouts();
		if(maitre_jeu)
		{
			for(byte couleur:couleurs_maitres)
			{
				if(!main(repartition,couleur).estVide())
				{
					indice_couleur_jouer=couleur;
					break;
				}
			}
			raison[0]="Vous allez maintenant ramasser les plis de toutes les couleurs en commencant par les cartes maitresses.";
			return main(repartition,indice_couleur_jouer).carte(0);
		}
		if(numero==preneur)
		{
			boolean quatre_couleurs_carte_maitre=true;
			for(byte couleur:couleurs_non_atouts)
			{
				if(!main(repartition,couleur).estVide())
				{
					quatre_couleurs_carte_maitre&=!main(cartes_maitresses,couleur).estVide()&&((CarteBelote)main(cartes_maitresses,couleur).carte(0)).points(contrat,carteAppelee)>0;
				}
			}
			Vector<Byte> couleurs=new Vector<Byte>();
			if(quatre_couleurs_carte_maitre)
			{
				Vector<Byte> couleurs_non_vides=new Vector<Byte>();
				for(byte couleur:couleurs_non_atouts)
				{
					if(!main(repartition,couleur).estVide())
					{
						couleurs_non_vides.addElement(couleur);
					}
				}
				/*Il existe une carte de couleur autre que l'atout*/
				return carte_maitresse(couleurs_non_vides, cartes_maitresses, repartition, repartitionCartesJouees,cartes_possibles , numero, suites);
			}
			for(byte couleur:couleurs_non_atouts)
			{
				if(!main(repartition,couleur).estVide()&&main(cartes_maitresses,couleur).estVide())
				{
					couleurs.addElement(couleur);
				}
			}
			if(!couleurs.isEmpty())
			{
				return faire_couper_adv(couleurs, repartition, repartitionCartesJouees);
			}
			for(byte couleur:couleurs_non_atouts)
			{
				if(!main(repartition,couleur).estVide())
				{
					couleurs.addElement(couleur);
				}
			}
			return faire_couper_adv(couleurs, repartition, repartitionCartesJouees);
		}
		Vector<Byte> couleurs=new Vector<Byte>();
		if(numero==appele)
		{
			byte couleur_dessus=getDistribution().derniereMain().carte(0).couleur();
			if(((CarteBelote) getDistribution().derniereMain().carte(0)).force(couleur_atout,couleur_dessus,contrat)==8)
			{
				if(!main(cartes_certaines,couleur_dessus,preneur).estVide()&&((CarteBelote)main(cartes_certaines,couleur_dessus,preneur).carte(0)).force(couleur_atout,couleur_dessus,contrat)==8)
				{
					if(tours(couleur_dessus, plisFaits).isEmpty()&&!main(repartition,couleur_dessus).estVide())
					{
						return main(repartition,couleur_dessus).carte(0);
					}
				}
			}
			/*On considere que l'appele est place apres le preneur*/
			for(byte couleur:couleurs_non_atouts)
			{
				if(!main(repartition,couleur).estVide()&&tours(couleur, plisFaits).isEmpty())
				{
					couleurs.addElement(couleur);
				}
			}
			if(!couleurs.isEmpty())
			{
				return ouvrir(couleurs, repartition, repartitionCartesJouees, cartes_maitresses);
			}
			for(byte couleur:couleurs_non_atouts)
			{
				if(!main(repartition,couleur).estVide()&&(defausse(couleur,adversaire[0],cartes_possibles)||defausse(couleur,adversaire[1],cartes_possibles)))
				{
					couleurs.addElement(couleur);
				}
			}
			if(!couleurs.isEmpty())
			{
				return ouvrir(couleurs,repartition,repartitionCartesJouees,cartes_maitresses);
			}
			for(byte couleur:couleurs_non_atouts)
			{
				if(!main(repartition,couleur).estVide())
				{
					couleurs.addElement(couleur);
				}
			}
			return faire_couper_adv(couleurs,repartition,repartitionCartesJouees);
		}
		/*Cas d'un defenseur*/
		for(byte couleur:couleurs_non_atouts)
		{
			if(!main(repartition,couleur).estVide()&&tours(couleur, plisFaits).isEmpty())
			{
				couleurs.addElement(couleur);
			}
		}
		if(!couleurs.isEmpty())
		{
			return ouvrir(couleurs, repartition, repartitionCartesJouees, cartes_maitresses);
		}
		for(byte couleur:couleurs_non_atouts)
		{
			if(!main(repartition,couleur).estVide()&&(defausse(couleur,adversaire[0],cartes_possibles)||defausse(couleur,adversaire[1],cartes_possibles)))
			{
				couleurs.addElement(couleur);
			}
		}
		if(!couleurs.isEmpty())
		{
			return ouvrir(couleurs,repartition,repartitionCartesJouees,cartes_maitresses);
		}
		for(byte couleur:couleurs_non_atouts)
		{
			if(!main(repartition,couleur).estVide())
			{
				couleurs.addElement(couleur);
			}
		}
		return faire_couper_adv(couleurs,repartition,repartitionCartesJouees);
	}
	private byte nombre_cartes_points(Vector<MainBelote> repartition,byte couleur)
	{
		byte nombre=0;
		for(byte indice_carte=0;indice_carte<main(repartition,couleur).total();indice_carte++)
		{
			if(((CarteBelote)main(repartition,couleur).carte(indice_carte)).points(contrat, carteAppelee)>0)
			{
				nombre++;
			}
			else
			{
				break;
			}
		}
		return nombre;
	}
	private Carte faire_tomber_points_pour_preneur(Vector<Byte> couleurs,Vector<MainBelote> repartition,Vector<MainBelote> repartition_cartes_jouees)
	{
		byte couleur_atout=carteAppelee.couleur();
		for(byte indice_couleur=1;indice_couleur<couleurs.size();indice_couleur++)
		{
			byte couleur1=couleurs.get(0);
			byte couleur2=couleurs.get(indice_couleur);
			if(main(repartition_cartes_jouees,couleur1).total()<main(repartition_cartes_jouees,couleur2).total())
			{
				couleurs.setElementAt(couleurs.set(0,couleur2),indice_couleur);
			}
			else if(main(repartition_cartes_jouees,couleur1).total()==main(repartition_cartes_jouees,couleur2).total())
			{
				if(main(repartition,couleur1).total()<main(repartition,couleur2).total())
				{
					couleurs.setElementAt(couleurs.set(0,couleur2),indice_couleur);
				}
				else if(main(repartition,couleur1).total()==main(repartition,couleur2).total())
				{
					for(byte indice_carte=0;indice_carte<main(repartition,couleur1).total();indice_carte++)
					{
						if(((CarteBelote)main(repartition,couleur1).carte(indice_carte)).force(couleur_atout,couleur1)<((CarteBelote)main(repartition,couleur2).carte(indice_carte)).force(couleur_atout,couleur2))
						{
							break;
						}
						else if(((CarteBelote)main(repartition,couleur1).carte(indice_carte)).force(couleur_atout,couleur1)>((CarteBelote)main(repartition,couleur2).carte(indice_carte)).force(couleur_atout,couleur2))
						{
							couleurs.setElementAt(couleurs.set(0,couleur2),indice_couleur);
							break;
						}
					}
				}
			}
		}
		return main(repartition,couleurs.get(0)).derniereCarte();
	}
	private Carte faire_couper_preneur_figure(Vector<Byte> couleurs,Vector<MainBelote> repartition)
	{
		byte nombre_cartes_points,nombre_cartes_points_2;
		for(byte indice_couleur=1;indice_couleur<couleurs.size();indice_couleur++)
		{
			byte couleur1=couleurs.get(0);
			byte couleur2=couleurs.get(indice_couleur);
			if(main(repartition,couleur1).total()<main(repartition,couleur2).total())
			{
				couleurs.setElementAt(couleurs.set(0,couleur2),indice_couleur);
			}
			else if(main(repartition,couleur1).total()==main(repartition,couleur2).total())
			{
				nombre_cartes_points=nombre_cartes_points(repartition,couleur1);
				nombre_cartes_points_2=nombre_cartes_points(repartition,couleur2);
				if(nombre_cartes_points>nombre_cartes_points_2)
				{
					couleurs.setElementAt(couleurs.set(0,couleur2),indice_couleur);
				}
			}
		}
		return main(repartition,couleurs.get(0)).carte(0);
	}
	private Carte ouvrir(Vector<Byte> couleurs,Vector<MainBelote> repartition,Vector<MainBelote> repartition_cartes_jouees,Vector<MainBelote> cartes_maitresses)
	{
		byte couleur_atout=carteAppelee.couleur();
		for(byte indice_couleur=1;indice_couleur<couleurs.size();indice_couleur++)
		{
			byte couleur1=couleurs.get(0);
			byte couleur2=couleurs.get(indice_couleur);
			if(main(cartes_maitresses,couleur1).total()<main(cartes_maitresses,couleur2).total())
			{
				couleurs.setElementAt(couleurs.set(0,couleur2),indice_couleur);
			}
			else if(main(cartes_maitresses,couleur1).total()==main(cartes_maitresses,couleur2).total())
			{
				if(main(repartition_cartes_jouees,couleur1).total()>main(repartition_cartes_jouees,couleur2).total())
				{
					couleurs.setElementAt(couleurs.set(0,couleur2),indice_couleur);
				}
				else if(main(repartition_cartes_jouees,couleur1).total()==main(repartition_cartes_jouees,couleur2).total())
				{
					if(main(repartition,couleur1).total()<main(repartition,couleur2).total())
					{
						couleurs.setElementAt(couleurs.set(0,couleur2),indice_couleur);
					}
					else if(main(repartition,couleur1).total()==main(repartition,couleur2).total())
					{
						for(byte indice_carte=0;indice_carte<main(repartition,couleur1).total();indice_carte++)
						{
							if(((CarteBelote)main(repartition,couleur1).carte(indice_carte)).force(couleur_atout,couleur1)<((CarteBelote)main(repartition,couleur2).carte(indice_carte)).force(couleur_atout,couleur2))
							{
								break;
							}
							else if(((CarteBelote)main(repartition,couleur1).carte(indice_carte)).force(couleur_atout,couleur1)>((CarteBelote)main(repartition,couleur2).carte(indice_carte)).force(couleur_atout,couleur2))
							{
								couleurs.setElementAt(couleurs.set(0,couleur2),indice_couleur);
								break;
							}
						}
					}
				}
			}
		}
		if(!main(cartes_maitresses,couleurs.get(0)).estVide())
		{
			return main(repartition,couleurs.get(0)).carte(0);
		}
		return main(repartition,couleurs.get(0)).derniereCarte();
	}
	private Carte ouvrir_couleur(Vector<Byte> couleurs,Vector<MainBelote> repartition)
	{
		byte couleur_atout=carteAppelee.couleur();
		for(byte indice_couleur=1;indice_couleur<couleurs.size();indice_couleur++)
		{
			byte couleur1=couleurs.get(0);
			byte couleur2=couleurs.get(indice_couleur);
			if(main(repartition,couleur1).total()<main(repartition,couleur2).total())
			{
				couleurs.setElementAt(couleurs.set(0,couleur2),indice_couleur);
			}
			else if(main(repartition,couleur1).total()==main(repartition,couleur2).total())
			{
				for(byte indice_carte=0;indice_carte<main(repartition,couleur1).total();indice_carte++)
				{
					if(((CarteBelote)main(repartition,couleur1).carte(indice_carte)).force(couleur_atout,couleur1)<((CarteBelote)main(repartition,couleur2).carte(indice_carte)).force(couleur_atout,couleur2))
					{
						break;
					}
					else if(((CarteBelote)main(repartition,couleur1).carte(indice_carte)).force(couleur_atout,couleur1)>((CarteBelote)main(repartition,couleur2).carte(indice_carte)).force(couleur_atout,couleur2))
					{
						couleurs.setElementAt(couleurs.set(0,couleur2),indice_couleur);
						break;
					}
				}
			}
		}
		return main(repartition,couleurs.get(0)).carte(0);
	}
	private Carte carte_maitresse(Vector<Byte> couleurs,Vector<MainBelote> cartes_maitresses,Vector<MainBelote> repartition,Vector<MainBelote> repartition_cartes_jouees,Vector<Vector<MainBelote>> cartes_possibles,byte numero,Vector<Vector<MainBelote>> suites)
	{
		Vector<Byte> couleurs_maitres=couleurs_maitres_sans_reprise(suites,couleurs,repartition,cartes_possibles,repartition_cartes_jouees,numero);
		int indice_1,indice_2;
		for(byte indice_couleur=1;indice_couleur<couleurs.size();indice_couleur++)
		{
			byte couleur1=couleurs.get(0);
			byte couleur2=couleurs.get(indice_couleur);
			indice_1=couleurs_maitres.indexOf(couleur1);
			indice_2=couleurs_maitres.indexOf(couleur2);
			if(indice_1<0&&indice_2>-1)
			{
				couleurs.setElementAt(couleurs.set(0,couleur2),indice_couleur);
			}
			else if(indice_2>-1)
			{
				if(indice_1>indice_2)
				{
					couleurs.setElementAt(couleurs.set(0,couleur2),indice_couleur);
				}
			}
			else if(main(cartes_maitresses,couleur1).total()<main(cartes_maitresses,couleur2).total())
			{
				couleurs.setElementAt(couleurs.set(0,couleur2),indice_couleur);
			}
			else if(main(cartes_maitresses,couleur1).total()==main(cartes_maitresses,couleur2).total())
			{
				if(main(repartition_cartes_jouees,couleur1).total()<main(repartition_cartes_jouees,couleur2).total())
				{
					couleurs.setElementAt(couleurs.set(0,couleur2),indice_couleur);
				}
				else if(main(repartition_cartes_jouees,couleur1).total()==main(repartition_cartes_jouees,couleur2).total())
				{
					if(main(repartition,couleur1).total()<main(repartition,couleur2).total())
					{
						couleurs.setElementAt(couleurs.set(0,couleur2),indice_couleur);
					}
				}
			}
		}
		return main(repartition,couleurs.get(0)).carte(0);
	}
	private Vector<Byte> couleurs_maitres_sans_reprise(Vector<Vector<MainBelote>> suites,Vector<Byte> couleurs,Vector<MainBelote> repartition,Vector<Vector<MainBelote>> cartes_possibles,Vector<MainBelote> cartesJouees,byte numero)
	{
		Vector<Byte> couleurs_maitres=new Vector<Byte>();
		int max=0;
		for(byte couleur:couleurs)
		{
			for(byte joueur=0;joueur<getNombreDeJoueurs();joueur++)
			{
				if(joueur!=numero)
				{
					if(main(cartes_possibles,couleur,joueur).total()>max)
					{
						max=main(cartes_possibles,couleur,joueur).total();
					}
				}
			}
			if(main(suites,couleur,0).total()>=max)
			{
				couleurs_maitres.addElement(couleur);
			}
		}
		for(int j=0;j<couleurs_maitres.size();j++)
		{
			for(int i=j+1;i<couleurs_maitres.size();i++)
			{
				if(main(repartition,couleurs_maitres.get(j)).total()<main(repartition,couleurs_maitres.get(i)).total())
				{
					couleurs_maitres.setElementAt(couleurs_maitres.set(j,couleurs_maitres.get(i)),i);
				}
				else if(main(repartition,couleurs_maitres.get(j)).total()==main(repartition,couleurs_maitres.get(i)).total())
				{
					if(main(cartesJouees,couleurs_maitres.get(j)).total()<main(cartesJouees,couleurs_maitres.get(i)).total())
					{
						couleurs_maitres.setElementAt(couleurs_maitres.set(j,couleurs_maitres.get(i)),i);
					}
				}
			}
		}
		return couleurs_maitres;
	}
	private Carte faire_couper_adv(Vector<Byte> couleurs,Vector<MainBelote> repartition,Vector<MainBelote> repartition_cartes_jouees)
	{
		byte couleur_atout=carteAppelee.couleur();
		for(byte indice_couleur=1;indice_couleur<couleurs.size();indice_couleur++)
		{
			if(main(repartition_cartes_jouees,couleurs.get(0)).total()<main(repartition_cartes_jouees,couleurs.get(indice_couleur)).total())
			{
				couleurs.setElementAt(couleurs.set(0,couleurs.get(indice_couleur)),indice_couleur);
			}
			else if(main(repartition_cartes_jouees,couleurs.get(0)).total()==main(repartition_cartes_jouees,couleurs.get(indice_couleur)).total())
			{
				if(main(repartition,couleurs.get(0)).total()<main(repartition,couleurs.get(indice_couleur)).total())
				{
					couleurs.setElementAt(couleurs.set(0,couleurs.get(indice_couleur)),indice_couleur);
				}
				else if(main(repartition,couleurs.get(0)).total()==main(repartition,couleurs.get(indice_couleur)).total())
				{
					for(byte indice_carte=0;indice_carte<main(repartition,couleurs.get(0)).total();indice_carte++)
					{
						if(((CarteBelote)main(repartition,couleurs.get(0)).carte(indice_carte)).force(couleur_atout,couleurs.get(0))<((CarteBelote)main(repartition,couleurs.get(indice_couleur)).carte(indice_carte)).force(couleur_atout,couleurs.get(0)))
						{
							break;
						}
						else if(((CarteBelote)main(repartition,couleurs.get(0)).carte(indice_carte)).force(couleur_atout,couleurs.get(0))>((CarteBelote)main(repartition,couleurs.get(indice_couleur)).carte(indice_carte)).force(couleur_atout,couleurs.get(0)))
						{
							couleurs.setElementAt(couleurs.set(0,couleurs.get(indice_couleur)),indice_couleur);
							break;
						}
					}
				}
			}
		}
		return main(repartition,couleurs.get(0)).derniereCarte();
	}
	private Carte faire_couper_appele(Vector<Byte> couleurs,Vector<MainBelote> repartition,Vector<MainBelote> repartition_cartes_jouees)
	{
		for(byte indice_couleur=1;indice_couleur<couleurs.size();indice_couleur++)
		{
			if(main(repartition_cartes_jouees,couleurs.get(0)).total()>main(repartition_cartes_jouees,couleurs.get(indice_couleur)).total())
			{
				couleurs.setElementAt(couleurs.set(0,couleurs.get(indice_couleur)),indice_couleur);
			}
			else if(main(repartition_cartes_jouees,couleurs.get(0)).total()==main(repartition_cartes_jouees,couleurs.get(indice_couleur)).total())
			{
				if(main(repartition,couleurs.get(0)).total()>main(repartition,couleurs.get(indice_couleur)).total())
				{
					couleurs.setElementAt(couleurs.set(0,couleurs.get(indice_couleur)),indice_couleur);
				}
			}
		}
		return main(repartition,couleurs.get(0)).derniereCarte();
	}
	/**Retourne l'ensemble des joueurs n'ayant pas joue autre que le joueur numero*/
	private Vector<Byte> joueursNAyantPasJoue(byte numero)
	{
		Vector<Byte> joueursNAyantPasJoue=new Vector<Byte>();
		byte nombre_joueurs=getNombreDeJoueurs();
		for(byte joueur=0;joueur<nombre_joueurs;joueur++)
			if(joueur!=numero&&pliEnCours.carteDuJoueur(joueur,nombre_joueurs,null)==null)
				joueursNAyantPasJoue.addElement(joueur);
		return joueursNAyantPasJoue;
	}
	private Carte en_cours(String[] raison)
	{
		byte nombre_joueurs=getNombreDeJoueurs();
		byte numero=(byte)((pliEnCours.getEntameur()+pliEnCours.total())%nombre_joueurs);
		MainBelote main_joueur=(MainBelote)getDistribution().main(numero);
		byte couleur_atout=couleur_atout();
		Vector<MainBelote> repartition=main_joueur.couleurs(couleur_atout,contrat);
		MainBelote cartes_jouables=cartes_jouables(repartition,numero);
		if(cartes_jouables.total()==1)
		{
			raison[0]="C'est la seule carte a jouer";
			return cartes_jouables.carte(0);
		}
		MainBelote cartesJouees=cartesJouees();
		cartesJouees.ajouterCartes(pliEnCours.getCartes());
		Vector<MainBelote> repartitionCartesJouees=cartesJouees.couleurs(couleur_atout,contrat);
		Vector<Vector<MainBelote>> suites=new Vector<Vector<MainBelote>>();
		Vector<Pli> plisFaits=unionPlis();
		for(MainBelote main:repartition)
		{
			suites.addElement(main.eclater(repartitionCartesJouees,couleur_atout));
		}
		Vector<Vector<MainBelote>> cartes_possibles=cartesPossibles(repartitionCartesJouees,plisFaits,repartition, numero,couleur_atout);
		Vector<Vector<MainBelote>> cartes_certaines=cartesCertaines(cartes_possibles);
		boolean strict_maitre_atout=contrat.force()==1?StrictMaitreAtout(cartes_possibles, numero,suite(suites,couleur_atout),repartitionCartesJouees):true;
		Vector<Byte> couleurs_maitres=CouleursMaitres(suites, repartitionCartesJouees, cartes_possibles,numero);
		Vector<MainBelote> cartes_maitresses=cartesMaitresses(repartition,repartitionCartesJouees);
		Vector<Byte> couleurs_strictement_maitresses=StrictCouleursMaitres(suites,repartitionCartesJouees,cartes_possibles,numero);
		byte appele=(byte)((preneur+2)%nombre_joueurs);
		byte partenaire=(byte)((numero+2)%nombre_joueurs);
		byte adversaire_non_joue=(byte)((numero+1)%nombre_joueurs);
		byte[] adversaire=new byte[]{(byte)((numero+1)%nombre_joueurs),(byte)((numero+3)%nombre_joueurs)};
		boolean maitre_jeu=contrat.force()==1?strict_maitre_atout&&couleurs_maitres.size()==3:couleurs_maitres.size()==4;
		byte ramasseur_virtuel=pliEnCours.getRamasseurBelote(nombre_joueurs,contrat,couleur_atout);
		CarteBelote carte_forte=(CarteBelote) pliEnCours.carteDuJoueur(ramasseur_virtuel,nombre_joueurs,null);
		byte couleurDemandee=pliEnCours.couleurDemandee();
		Vector<Byte> joueurs_non_joue=joueursNAyantPasJoue(numero);
		byte ramasseur_certain=equipe_qui_va_faire_pli(cartes_possibles, cartes_certaines, ramasseur_virtuel, carte_forte, joueurs_non_joue, numero);
		boolean carte_maitresse;
		byte max;
		Vector<MainBelote> repartition_jouables=cartes_jouables.couleurs(couleur_atout,contrat);
		if(ramasseur_certain==0)
		{
			raison[0]="Un adversaire va ramasser le pli.\n";
			if(couleurDemandee!=couleur_atout)
			{
				if(cartes_jouables.carte(0).couleur()==couleurDemandee)
				{
					return main(repartition,couleurDemandee).derniereCarte();
				}
				if(couleur_atout>0&&main(repartition_jouables,couleur_atout).total()==cartes_jouables.total())
				{/*Si le joueur est oblige de couper la couleur demandee*/
					return main(repartition_jouables,couleur_atout).derniereCarte();
				}
				/*Si le joueur se defausse*/
				return defausse_couleur_demandee_sur_adversaire(repartitionCartesJouees, repartition, cartes_maitresses, couleurDemandee, couleur_atout, couleurs_strictement_maitresses);
			}
			/*La couleur demandee est atout*/
			if(cartes_jouables.carte(0).couleur()==couleur_atout)
			{
				return main(repartition,couleur_atout).derniereCarte();
			}
			/*Maintenant le joueur se defausse sur demande d'atout*/
			return defausse_atout_sur_adversaire(repartitionCartesJouees, repartition, cartes_maitresses, couleur_atout, couleurs_strictement_maitresses);
		}
		if(ramasseur_certain==1)
		{
			raison[0]="Le partenaire va ramasser le pli.\n";
			if(couleurDemandee!=couleur_atout)
			{
				if(cartes_jouables.carte(0).couleur()==couleurDemandee)
				{
					for(byte joueur:adversaire)
					{
						if(!main(cartes_possibles,couleurDemandee,joueur).estVide())
						{
							if(((CarteBelote)main(cartes_possibles,couleurDemandee,joueur).carte(0)).force(couleur_atout,couleurDemandee,contrat)>((CarteBelote)main(repartition_jouables,couleurDemandee).carte(0)).force(couleur_atout,couleurDemandee,contrat))
							{
								return main(repartition_jouables,couleurDemandee).carte(0);
							}
						}
					}
					if(main(repartition_jouables,couleurDemandee).nombreCartesPoints(carte_forte, contrat)>1)
					{
						return carte_plus_petite_points(suite(suites,couleurDemandee));
					}
					return main(repartition_jouables,couleurDemandee).derniereCarte();
				}
				if(couleur_atout>0&&main(repartition_jouables,couleur_atout).total()==cartes_jouables.total())
				{
					if(suite(suites,couleur_atout).size()==1)
					{
						return main(repartition_jouables,couleur_atout).carte(0);
					}
					return main(repartition_jouables,couleur_atout).derniereCarte();
				}
				return defausse_couleur_demandee_sur_partenaire(repartitionCartesJouees, repartition, cartes_maitresses, couleurDemandee, couleur_atout, couleurs_strictement_maitresses);
			}
			if(cartes_jouables.carte(0).couleur()==couleur_atout)
			{
				if(suite(suites,couleur_atout).size()==1)
				{
					return main(repartition_jouables,couleur_atout).carte(0);
				}
				for(byte joueur:adversaire)
				{
					if(!main(cartes_possibles,couleur_atout,joueur).estVide())
					{
						if(((CarteBelote)main(cartes_possibles,couleur_atout,joueur).carte(0)).force(couleur_atout,couleurDemandee,contrat)>((CarteBelote)main(repartition_jouables,couleur_atout).carte(0)).force(couleur_atout,couleurDemandee,contrat))
						{
							if(!joueurs_non_joue.contains(partenaire))
							{
								return main(repartition_jouables,couleur_atout).carte(0);
							}
							return carte_plus_petite_points(suite(suites,couleur_atout));
						}
					}
				}
				return main(repartition_jouables,couleur_atout).derniereCarte();
			}
			/*Maintenant le joueur se defausse*/
			return defausse_atout_sur_partenaire(repartitionCartesJouees, repartition, cartes_maitresses, couleur_atout, couleurs_strictement_maitresses);
		}
		/*Fin test sur ramasseur certain*/
		Pli dernier_pli;
		Vector<MainBelote> cartes_rel_maitres;
		Vector<Byte> tours=tours(couleurDemandee, plisFaits),dernieres_coupes,dernieres_defausses;
		Vector<Byte> joueurs_susceptibles_de_couper=new Vector<Byte>();
		if(contrat.force()<4&&couleurDemandee!=couleur_atout)
		{
			if(cartes_jouables.carte(0).couleur()==couleurDemandee)
			{//Si le joueur ne coupe pas et ne se defause pas sur la couleur demandee
				cartes_rel_maitres=cartesRelativementMaitre(suite(suites,couleurDemandee), cartes_possibles, joueurs_non_joue, couleurDemandee, couleurDemandee,couleur_atout, cartes_certaines,carte_forte);
				if(((CarteBelote)main(repartition_jouables,couleurDemandee).carte(0)).force(couleur_atout,couleurDemandee,contrat)<carte_forte.force(couleur_atout,couleurDemandee,contrat)||carte_forte.couleur()==couleur_atout)
				{
					/*Si le joueur ne peut pas prendre la main*/
					if(((CarteBelote)main(repartition_jouables,couleurDemandee).carte(0)).points(contrat,carteAppelee)<1||maitre_jeu)
					{/*Si le joueur ne possede pas de figure ou est maitre du jeu*/
						return main(repartition,couleurDemandee).derniereCarte();
					}
					/*Le joueur possede au moins une figure*/
					if(tours.isEmpty())
					{/*Si cette couleur est entamee pour la premiere fois*/
						if(ramasseur_virtuel==partenaire)
						{
							if(!main(cartes_possibles,couleurDemandee,adversaire_non_joue).estVide())
							{
								max=((CarteBelote)main(cartes_possibles,couleurDemandee,adversaire_non_joue).carte(0)).force(couleur_atout,couleurDemandee,contrat);
								if(carte_forte.force(couleur_atout,couleurDemandee,contrat)>max)
								{
									return main(repartition_jouables,couleurDemandee).carte(0);
								}
							}
						}
						return main(repartition,couleurDemandee).derniereCarte();
					}
					dernier_pli=plisFaits.get(tours.lastElement());
					dernieres_coupes=dernier_pli.joueurs_coupes(nombre_joueurs,null,couleur_atout);
					dernieres_defausses=dernier_pli.joueurs_defausses(nombre_joueurs,null,couleur_atout);
					/*Maintenant on aborde au moins le deuxieme tour*/
					if(dernieres_coupes.isEmpty())
					{/*Si le dernier pli n'est pas coupe a cette couleur*/
						if(partenaire==ramasseur_virtuel)
						{
							if(carte_forte.couleur()==couleur_atout)
							{/*L'espoir fait vivre*/
								return main(repartition_jouables,couleurDemandee).carte(0);
							}
							if(!main(cartes_possibles,couleurDemandee,adversaire_non_joue).estVide())
							{
								max=((CarteBelote)main(cartes_possibles,couleurDemandee,adversaire_non_joue).carte(0)).force(couleur_atout,couleurDemandee,contrat);
								if(carte_forte.force(couleur_atout,couleurDemandee,contrat)>max)
								{
									return main(repartition_jouables,couleurDemandee).carte(0);
								}
							}
						}
						return main(repartition,couleurDemandee).derniereCarte();
					}
					/*Maintenant on sait qu'au dernier tour le pli a ete coupe*/
					if(partenaire==ramasseur(plisFaits,tours.lastElement()))
					{
						if(carte_forte.couleur()==couleur_atout)
						{/*L'espoir fait vivre*/
							return main(repartition_jouables,couleurDemandee).carte(0);
						}
						if(!main(cartes_possibles,couleurDemandee,adversaire_non_joue).estVide())
						{
							max=((CarteBelote)main(cartes_possibles,couleurDemandee,adversaire_non_joue).carte(0)).force(couleur_atout,couleurDemandee,contrat);
							if(carte_forte.force(couleur_atout,couleurDemandee,contrat)>max)
							{
								return main(repartition_jouables,couleurDemandee).carte(0);
							}
						}
					}
					return main(repartition,couleurDemandee).derniereCarte();
				}
				/*Maintenant on sait que le joueur peut prendre la main*/
				if(((CarteBelote)main(repartition_jouables,couleurDemandee).carte(0)).points(contrat,carteAppelee)<1)
				{
					if(maitre_jeu||defausse(couleurDemandee, adversaire_non_joue, cartes_possibles)||ne_peut_pas_avoir_figures(cartes_possibles, adversaire_non_joue, couleurDemandee))
					{
						if(!cartes_rel_maitres.isEmpty())
						{
							return cartes_rel_maitres.lastElement().carte(0);
						}
					}
					if(tours.isEmpty())
					{
						return main(repartition_jouables,couleurDemandee).carte(0);
					}
					return main(repartition,couleurDemandee).derniereCarte();
				}
				/*Maintenant le joueur peut prendre la main avec une figure a la couleur demandee*/
				if(numero==preneur)
				{
					if(tours.isEmpty())
					{
						if(maitre_jeu)
						{
							return cartes_rel_maitres.lastElement().carte(0);
						}
						if(!main(cartes_maitresses,couleurDemandee).estVide())
						{
							if(suite(suites,couleurDemandee).size()==1||((CarteBelote)main(suites,couleurDemandee,1).carte(0)).points(contrat,carteAppelee)<1)
							{
								return main(repartition_jouables,couleurDemandee).carte(0);
							}
							if((defausse(couleurDemandee, adversaire_non_joue, cartes_possibles)||ne_peut_pas_avoir_figures(cartes_possibles, adversaire_non_joue, couleurDemandee))&&(carte_forte.points(contrat,carteAppelee)<1||partenaire==ramasseur_virtuel))
							{
								return jeuFigureHauteDePlusFaibleSuite(suite(suites,couleurDemandee));
							}
							if(carte_forte.points(contrat, carteAppelee)>0)
							{
								if(suite(suites,couleurDemandee).size()==1||((CarteBelote)main(suites,couleurDemandee,1).carte(0)).points(contrat,carteAppelee)<1||ramasseur_virtuel!=partenaire||!partenaire_bat_adversaire_non_joue(adversaire_non_joue, couleurDemandee, couleur_atout, cartes_possibles, carte_forte))
								{
									return main(repartition_jouables,couleurDemandee).carte(0);
								}
								return jeuFigureHauteDePlusFaibleSuite(suite(suites,couleurDemandee));
							}
							if(ramasseur_virtuel==partenaire&&partenaire_bat_adversaire_non_joue(adversaire_non_joue, couleurDemandee, couleur_atout, cartes_possibles, carte_forte))
							{
								return jeuFigureHauteDePlusFaibleSuite(suite(suites,couleurDemandee));
							}
							return main(repartition_jouables,couleurDemandee).carte(0);
						}
						/*Le joueur n'a aucune cartes maitresses*/
						if(defausse(couleurDemandee, adversaire_non_joue, cartes_possibles)||ne_peut_pas_avoir_figures(cartes_possibles, adversaire_non_joue, couleurDemandee))
						{
							return main(repartition_jouables,couleurDemandee).carte(0);
						}
						return main(repartition,couleurDemandee).derniereCarte();
					}
					/*C'est au moins le deuxieme tour*/
					dernier_pli=plisFaits.get(tours.lastElement());
					dernieres_coupes=dernier_pli.joueurs_coupes(nombre_joueurs, null,couleur_atout);
					dernieres_defausses=dernier_pli.joueurs_defausses(nombre_joueurs, null,couleur_atout);
					if(couleur_atout>1)
					{
						for(byte joueur:joueurs_non_joue)
						{
							if(peut_couper(couleurDemandee, joueur, cartes_possibles))
							{
								joueurs_susceptibles_de_couper.addElement(joueur);
							}
						}
					}
					if(!joueurs_susceptibles_de_couper.isEmpty())
					{
						for(byte joueur:adversaire)
						{
							if(joueurs_susceptibles_de_couper.contains(joueur))
							{
								return main(repartition,couleurDemandee).derniereCarte();
							}
						}
						if(maitre_jeu)
						{
							max=0;
							for(byte joueur=0;joueur<nombre_joueurs;joueur++)
							{
								if(joueur!=numero)
								{
									max=(byte)Math.max(main(cartes_possibles,couleurDemandee,joueur).total(),max);
								}
							}
							if(main(suites,couleurDemandee,0).total()>max)
							{
								return main(repartition_jouables,couleurDemandee).carte(0);
							}
							return main(repartition,couleurDemandee).derniereCarte();
						}
						return jeuFigureHauteDePlusFaibleSuite(suite(suites,couleurDemandee));
					}
					/*Si la coupe semble improbable*/
					if(!dernieres_defausses.isEmpty()&&tours.size()==1)
					{
						if(maitre_jeu)
						{
							return cartes_rel_maitres.lastElement().carte(0);
						}
						if(!main(cartes_maitresses,couleurDemandee).estVide())
						{
							if(suite(suites,couleurDemandee).size()==1||((CarteBelote)main(suites,couleurDemandee,1).carte(0)).points(contrat,carteAppelee)<1)
							{
								return main(repartition_jouables,couleurDemandee).carte(0);
							}
							if((defausse(couleurDemandee, adversaire_non_joue, cartes_possibles)||ne_peut_pas_avoir_figures(cartes_possibles, adversaire_non_joue, couleurDemandee))&&(carte_forte.points(contrat,carteAppelee)<1||partenaire==ramasseur_virtuel))
							{
								return jeuFigureHauteDePlusFaibleSuite(suite(suites,couleurDemandee));
							}
							if(carte_forte.points(contrat, carteAppelee)>0)
							{
								if(suite(suites,couleurDemandee).size()==1||((CarteBelote)main(suites,couleurDemandee,1).carte(0)).points(contrat,carteAppelee)<1||ramasseur_virtuel!=partenaire||!partenaire_bat_adversaire_non_joue(adversaire_non_joue, couleurDemandee, couleur_atout, cartes_possibles, carte_forte))
								{
									return main(repartition_jouables,couleurDemandee).carte(0);
								}
								return jeuFigureHauteDePlusFaibleSuite(suite(suites,couleurDemandee));
							}
							if(ramasseur_virtuel==partenaire&&partenaire_bat_adversaire_non_joue(adversaire_non_joue, couleurDemandee, couleur_atout, cartes_possibles, carte_forte))
							{
								return jeuFigureHauteDePlusFaibleSuite(suite(suites,couleurDemandee));
							}
							return main(repartition_jouables,couleurDemandee).carte(0);
						}
						/*Le joueur n'a aucune cartes maitresses*/
						if(defausse(couleurDemandee, adversaire_non_joue, cartes_possibles)||ne_peut_pas_avoir_figures(cartes_possibles, adversaire_non_joue, couleurDemandee))
						{
							return main(repartition_jouables,couleurDemandee).carte(0);
						}
						return main(repartition,couleurDemandee).derniereCarte();
					}
					/*Le pli d'avant n'est pas defausse ou c'est au moins le troisieme tour*/
					if(!main(cartes_maitresses,couleurDemandee).estVide())
					{
						return main(repartition_jouables,couleurDemandee).carte(0);
					}
					if(!cartes_rel_maitres.isEmpty())
					{
						return cartes_rel_maitres.lastElement().carte(0);
					}
					return main(repartition,couleurDemandee).derniereCarte();
				}
				/*Appele*/
				if(numero==appele)
				{
					if(tours.isEmpty())
					{
						if(maitre_jeu)
						{
							return cartes_rel_maitres.lastElement().carte(0);
						}
						CarteBelote carte_dessus=(CarteBelote)getDistribution().derniereMain().carte(0);
						if(!joueurs_non_joue.contains(preneur))
						{/*Si l'appele joue apres le preneur*/
							if(couleur_atout<2||!peut_couper(couleurDemandee,adversaire_non_joue,cartes_possibles))
							{
								carte_maitresse=true;
								if(!main(cartes_possibles,couleurDemandee,adversaire_non_joue).estVide())
								{
									carte_maitresse&=carte_dessus.force(couleur_atout,couleurDemandee,contrat)>((CarteBelote)main(cartes_possibles,couleurDemandee,adversaire_non_joue).carte(0)).force(couleur_atout,couleurDemandee,contrat);
								}
								if(carte_maitresse&&!cartes_rel_maitres.isEmpty())
								{
									if(cartes_rel_maitres.size()==1||((CarteBelote) cartes_rel_maitres.get(1).carte(0)).points(contrat,carteAppelee)<1)
									{
										return cartes_rel_maitres.get(0).carte(0);
									}
									return cartes_rel_maitres.get(1).carte(0);
								}
							}
						}
						if(carte_dessus.points(contrat,carteAppelee)>0)
						{/*Si l'appele joue avant le preneur et la carte du dessus vaut des points.*/
							if(couleur_atout<2||!peut_couper(couleurDemandee,adversaire_non_joue,cartes_possibles))
							{
								carte_maitresse=true;
								if(!main(cartes_possibles,couleurDemandee,adversaire_non_joue).estVide())
								{
									carte_maitresse&=carte_dessus.force(couleur_atout,couleurDemandee,contrat)>((CarteBelote)main(cartes_possibles,couleurDemandee,adversaire_non_joue).carte(0)).force(couleur_atout,couleurDemandee,contrat);
								}
								if(carte_maitresse)
								{
									return jeuFigureHauteDePlusFaibleSuite(suite(suites,couleurDemandee));
								}
							}
						}
						if(!main(cartes_maitresses,couleurDemandee).estVide())
						{
							if(suite(suites,couleurDemandee).size()==1||((CarteBelote)main(suites,couleurDemandee,1).carte(0)).points(contrat,carteAppelee)<1)
							{
								return main(repartition_jouables,couleurDemandee).carte(0);
							}
							if((defausse(couleurDemandee, adversaire_non_joue, cartes_possibles)||ne_peut_pas_avoir_figures(cartes_possibles, adversaire_non_joue, couleurDemandee))&&(carte_forte.points(contrat,carteAppelee)<1||partenaire==ramasseur_virtuel))
							{
								return jeuFigureHauteDePlusFaibleSuite(suite(suites,couleurDemandee));
							}
							if(carte_forte.points(contrat, carteAppelee)>0)
							{
								if(suite(suites,couleurDemandee).size()==1||((CarteBelote)main(suites,couleurDemandee,1).carte(0)).points(contrat,carteAppelee)<1||ramasseur_virtuel!=partenaire||!partenaire_bat_adversaire_non_joue(adversaire_non_joue, couleurDemandee, couleur_atout, cartes_possibles, carte_forte))
								{
									return main(repartition_jouables,couleurDemandee).carte(0);
								}
								return jeuFigureHauteDePlusFaibleSuite(suite(suites,couleurDemandee));
							}
							if(ramasseur_virtuel==partenaire&&partenaire_bat_adversaire_non_joue(adversaire_non_joue, couleurDemandee, couleur_atout, cartes_possibles, carte_forte))
							{
								return jeuFigureHauteDePlusFaibleSuite(suite(suites,couleurDemandee));
							}
							return main(repartition_jouables,couleurDemandee).carte(0);
						}
						/*Le joueur n'a aucune cartes maitresses*/
						if(defausse(couleurDemandee, adversaire_non_joue, cartes_possibles)||ne_peut_pas_avoir_figures(cartes_possibles, adversaire_non_joue, couleurDemandee))
						{
							return main(repartition_jouables,couleurDemandee).carte(0);
						}
						return main(repartition,couleurDemandee).derniereCarte();
					}
					dernier_pli=plisFaits.get(tours.lastElement());
					dernieres_coupes=dernier_pli.joueurs_coupes(nombre_joueurs, null,couleur_atout);
					dernieres_defausses=dernier_pli.joueurs_defausses(nombre_joueurs, null,couleur_atout);
					/*Deuxieme tour pour un appele ne coupant pas la couleur demandee differente de l'atout*/
					if(couleur_atout>1)
					{
						for(byte joueur:joueurs_non_joue)
						{
							if(peut_couper(couleurDemandee, joueur, cartes_possibles))
							{
								joueurs_susceptibles_de_couper.addElement(joueur);
							}
						}
					}
					if(!joueurs_susceptibles_de_couper.isEmpty())
					{
						for(byte joueur:adversaire)
						{
							if(joueurs_susceptibles_de_couper.contains(joueur))
							{
								return main(repartition,couleurDemandee).derniereCarte();
							}
						}
						if(maitre_jeu)
						{
							max=0;
							for(byte joueur=0;joueur<nombre_joueurs;joueur++)
							{
								if(joueur!=numero)
								{
									max=(byte)Math.max(main(cartes_possibles,couleurDemandee,joueur).total(),max);
								}
							}
							if(main(suites,couleurDemandee,0).total()>max)
							{
								return main(repartition_jouables,couleurDemandee).carte(0);
							}
							return main(repartition,couleurDemandee).derniereCarte();
						}
						return jeuFigureHauteDePlusFaibleSuite(suite(suites,couleurDemandee));
					}
					/*Si la coupe semble improbable*/
					if(!dernieres_defausses.isEmpty()&&tours.size()==1)
					{
						if(maitre_jeu)
						{
							return cartes_rel_maitres.lastElement().carte(0);
						}
						if(!main(cartes_maitresses,couleurDemandee).estVide())
						{
							if(suite(suites,couleurDemandee).size()==1||((CarteBelote)main(suites,couleurDemandee,1).carte(0)).points(contrat,carteAppelee)<1)
							{
								return main(repartition_jouables,couleurDemandee).carte(0);
							}
							if((defausse(couleurDemandee, adversaire_non_joue, cartes_possibles)||ne_peut_pas_avoir_figures(cartes_possibles, adversaire_non_joue, couleurDemandee))&&(carte_forte.points(contrat,carteAppelee)<1||partenaire==ramasseur_virtuel))
							{
								return jeuFigureHauteDePlusFaibleSuite(suite(suites,couleurDemandee));
							}
							if(carte_forte.points(contrat, carteAppelee)>0)
							{
								if(suite(suites,couleurDemandee).size()==1||((CarteBelote)main(suites,couleurDemandee,1).carte(0)).points(contrat,carteAppelee)<1||ramasseur_virtuel!=partenaire||!partenaire_bat_adversaire_non_joue(adversaire_non_joue, couleurDemandee, couleur_atout, cartes_possibles, carte_forte))
								{
									return main(repartition_jouables,couleurDemandee).carte(0);
								}
								return jeuFigureHauteDePlusFaibleSuite(suite(suites,couleurDemandee));
							}
							if(ramasseur_virtuel==partenaire&&partenaire_bat_adversaire_non_joue(adversaire_non_joue, couleurDemandee, couleur_atout, cartes_possibles, carte_forte))
							{
								return jeuFigureHauteDePlusFaibleSuite(suite(suites,couleurDemandee));
							}
							return main(repartition_jouables,couleurDemandee).carte(0);
						}
						/*Le joueur n'a aucune cartes maitresses*/
						if(defausse(couleurDemandee, adversaire_non_joue, cartes_possibles)||ne_peut_pas_avoir_figures(cartes_possibles, adversaire_non_joue, couleurDemandee))
						{
							return main(repartition_jouables,couleurDemandee).carte(0);
						}
						return main(repartition,couleurDemandee).derniereCarte();
					}
					/*Le pli d'avant n'est pas defausse ou c'est au moins le troisieme tour*/
					if(!main(cartes_maitresses,couleurDemandee).estVide())
					{
						return main(repartition_jouables,couleurDemandee).carte(0);
					}
					if(!cartes_rel_maitres.isEmpty())
					{
						return cartes_rel_maitres.lastElement().carte(0);
					}
					return main(repartition,couleurDemandee).derniereCarte();
				}
				/*Defenseur*/
				if(maitre_jeu)
				{
					return cartes_rel_maitres.lastElement().carte(0);
				}
				if(pas_atout(adversaire_non_joue, cartes_possibles, couleur_atout))
				{
					return sauve_qui_peut_figure(cartes_possibles,suite(suites,couleurDemandee), cartes_rel_maitres, adversaire_non_joue, couleurDemandee);
				}
				if(tours.isEmpty())
				{
					if(!joueurs_non_joue.contains(preneur)||carte_forte.points(contrat,carteAppelee)>0)
					{/*Si le joueur (defenseur) va jouer apres le preneur et il reste des joueurs susceptibles d'etre l'appele ou il existe une figure que peut prendre le joueur*/
						if(!cartes_rel_maitres.isEmpty())
						{
							if(cartes_rel_maitres.size()==1||((CarteBelote) cartes_rel_maitres.get(1).carte(0)).points(contrat,carteAppelee)<1)
								return main(suites,couleurDemandee,0).carte(0);
							return cartes_rel_maitres.get(1).carte(0);
						}
						return main(repartition,couleurDemandee).derniereCarte();
					}
					if(!cartes_rel_maitres.isEmpty()&&cartes_rel_maitres.get(0).total()==1&&main(repartition_jouables,couleurDemandee).total()==2)
					{
						if(suite(suites,couleurDemandee).size()==1)
						{
							return main(repartition_jouables,couleurDemandee).carte(0);
						}
						if(partenaire==ramasseur_virtuel)
						{
							boolean local=defausse(couleurDemandee,adversaire_non_joue,cartes_possibles);
							if(!main(cartes_possibles,couleurDemandee,adversaire_non_joue).estVide())
							{
								local|=((CarteBelote)main(cartes_possibles,couleurDemandee,adversaire_non_joue).carte(0)).force(couleur_atout,couleurDemandee,contrat)<carte_forte.force(couleur_atout,couleurDemandee,contrat);
							}
							if(local)
							{
								return main(repartition_jouables,couleurDemandee).carte(1);
							}
						}
						return main(repartition_jouables,couleurDemandee).carte(0);
					}
					return main(repartition,couleurDemandee).derniereCarte();
				}
				/*Maintenant on est au moins au deuxieme tour*/
				return main(repartition,couleurDemandee).derniereCarte();
			}
			/*Le joueur ne peut pas fournir a la couleur demandee*/
			if(couleur_atout>0)
			{
				if(!main(repartition_jouables,couleur_atout).estVide())
				{
					cartes_rel_maitres=cartesRelativementMaitre(suite(suites,couleur_atout), cartes_possibles, joueurs_non_joue, couleurDemandee, couleur_atout, couleur_atout, cartes_certaines,carte_forte);
					if(main(repartition_jouables,couleur_atout).total()==cartes_jouables.total())
					{/*Coupe obligatoire*/
						return coupe(repartition_jouables,cartes_possibles,cartes_maitresses,suites,adversaire_non_joue,couleur_atout,couleurDemandee,tours,carte_forte,cartes_rel_maitres,maitre_jeu);
					}
					/*Coupe possible non obligatoire*/
					if(((CarteBelote)main(repartition_jouables,couleur_atout).carte(0)).force(couleur_atout, couleurDemandee, contrat)>carte_forte.force(couleur_atout, couleurDemandee, contrat))
					{
						if(maitre_jeu)
						{
							return cartes_rel_maitres.lastElement().carte(0);
						}
						if(suite(suites,couleur_atout).size()==1&&couleurs_maitres.size()==3&&((CarteBelote)main(repartition_jouables,couleur_atout).carte(0)).points(contrat,carteAppelee)>0)
						{
							if(ne_peut_couper(couleurDemandee, adversaire_non_joue, cartes_possibles, cartes_certaines)||((CarteBelote)main(cartes_possibles,couleur_atout,adversaire_non_joue).carte(0)).force(couleur_atout,couleurDemandee)<((CarteBelote)main(repartition_jouables,couleur_atout).carte(0)).force(couleur_atout,couleurDemandee))
							{
								return main(repartition_jouables,couleur_atout).carte(0);
							}
						}
						if(!partenaire_bat_adversaire_non_joue(adversaire_non_joue, couleurDemandee, couleur_atout, cartes_possibles, carte_forte))
						{
							return coupe(repartition_jouables,cartes_possibles,cartes_maitresses,suites,adversaire_non_joue,couleur_atout,couleurDemandee,tours,carte_forte,cartes_rel_maitres,maitre_jeu);
						}
						return defausse_couleur_demandee_sur_partenaire(repartitionCartesJouees, repartition, cartes_maitresses, couleurDemandee, couleur_atout, couleurs_strictement_maitresses);
					}
					if(maitre_jeu&&((CarteBelote)main(repartition_jouables,couleur_atout).derniereCarte()).points(contrat,carteAppelee)<10)
					{
						carte_maitresse=true;
						for(byte couleur:couleurs_non_atouts())
						{
							if(!main(repartition,couleur).estVide())
							{
								carte_maitresse&=((CarteBelote)main(repartition,couleur).derniereCarte()).points(contrat, carteAppelee)>0;
							}
						}
						if(carte_maitresse)
						{
							return main(repartition_jouables,couleur_atout).derniereCarte();
						}
					}
					return defausse_couleur_demandee_sur_partenaire(repartitionCartesJouees, repartition, cartes_maitresses, couleurDemandee, couleur_atout, couleurs_strictement_maitresses);
				}
			}
			if(tours.isEmpty())
			{
				if(ramasseur_virtuel==partenaire&&partenaire_bat_adversaire_non_joue(adversaire_non_joue, couleurDemandee, couleur_atout, cartes_possibles, carte_forte))
				{
					return defausse_couleur_demandee_sur_partenaire(repartitionCartesJouees, repartition, cartes_maitresses, couleurDemandee, couleur_atout, couleurs_strictement_maitresses);
				}
			}
			return defausse_couleur_demandee_sur_adversaire(repartitionCartesJouees, repartition, cartes_maitresses, couleurDemandee, couleur_atout, couleurs_strictement_maitresses);
		}
		if(!main(repartition_jouables,couleurDemandee).estVide())
		{
			cartes_rel_maitres=cartesRelativementMaitre(suite(suites,couleurDemandee), cartes_possibles, joueurs_non_joue, couleurDemandee, couleurDemandee,couleurDemandee, cartes_certaines,carte_forte);
			if(((CarteBelote)main(repartition_jouables,couleurDemandee).carte(0)).force(couleur_atout,couleurDemandee,contrat)<carte_forte.force(couleur_atout,couleurDemandee,contrat))
			{
				return main(repartition_jouables,couleurDemandee).derniereCarte();
			}
			if(maitre_jeu)
			{
				return cartes_rel_maitres.lastElement().carte(0);
			}
			if(main(cartes_possibles,couleurDemandee,adversaire_non_joue).estVide()||((CarteBelote)main(cartes_possibles,couleurDemandee,adversaire_non_joue).carte(0)).force(couleur_atout,couleurDemandee,contrat)<carte_forte.force(couleur_atout,couleurDemandee,contrat))
			{
				return cartes_rel_maitres.lastElement().carte(0);
			}
			if(!main(cartes_maitresses,couleurDemandee).estVide())
			{
				byte min=-1;
				byte force_min=((CarteBelote)main(cartes_possibles,couleurDemandee,adversaire_non_joue).carte(0)).force(couleur_atout,couleurDemandee,contrat);
				for(MainBelote suite:suite(suites,couleurDemandee))
				{
					if(((CarteBelote)suite.carte(0)).force(couleur_atout,couleurDemandee,contrat)>force_min)
					{
						min++;
					}
					else
					{
						break;
					}
				}
				return main(suites,couleurDemandee,min).carte(0);
			}
			return main(repartition_jouables,couleurDemandee).derniereCarte();
		}
		return defausse_atout_sur_adversaire(repartitionCartesJouees, repartition, cartes_maitresses, couleur_atout, couleurs_strictement_maitresses);
	}
	/**Retourne vrai si et seulement si le joueur ne peut pas fournir la couleur donnee et peut couper avec un atout*/
	private boolean peut_couper(byte couleur,byte numero,Vector<Vector<MainBelote>> cartes_possibles)
	{
		return main(cartes_possibles,couleur,numero).estVide()&&!main(cartes_possibles,carteAppelee.couleur(),numero).estVide();
	}
	/**Renvoie un entier 0 si joueur de non confiance qui va faire le pli 1 si joueur de confiance va faire le pli et -1 sinon
	 * @param cartes_possibles l'ensemble des cartes probablement possedees par les joueurs
	 * @param cartes_certaines l'ensemble des cartes surement possedees par les joueurs
	 * @param ramasseur_virtuel le joueur, qui sans les cartes jouees par les derniers joueurs du pli est ramasseur
	 * @param carte_forte la carte qui est en train de dominer le pli
	 * @param joueurs_non_joue l'ensemble des joueurs n'ayant pas encore joue leur carte
	 * @param numero le numero du joueur qui va jouer*/
	private byte equipe_qui_va_faire_pli(Vector<Vector<MainBelote>> cartes_possibles,Vector<Vector<MainBelote>> cartes_certaines,byte ramasseur_virtuel,CarteBelote carte_forte,Vector<Byte> joueurs_non_joue,byte numero)
	{
		Vector<Byte> joueurs_confiance=new Vector<Byte>();
		byte partenaire=(byte)((numero+2)%getNombreDeJoueurs());
		Vector<Byte> joueurs_non_confiance=new Vector<Byte>();
		joueurs_non_confiance.addElement((byte)((numero+1)%getNombreDeJoueurs()));
		joueurs_non_confiance.addElement((byte)((numero+3)%getNombreDeJoueurs()));
		byte couleur_demandee=pliEnCours.couleurDemandee();
		byte couleur_atout=couleur_atout();
		boolean ramasseur_virtuel_egal_certain=false;
		Vector<Byte> joueurs_non_confiance_non_joue=new Vector<Byte>();
		Vector<Byte> joueurs_confiance_non_joue=new Vector<Byte>();
		Vector<Byte> joueurs_joue=new Vector<Byte>();
		for(byte joueur=0;joueur<getNombreDeJoueurs();joueur++)
		{
			if(!joueurs_non_joue.contains(joueur))
				joueurs_joue.addElement(joueur);
		}
		for(byte joueur:joueurs_non_confiance)
		{
			if(joueurs_non_joue.contains(joueur))
			{
				joueurs_non_confiance_non_joue.addElement(joueur);
			}
		}
		if(joueurs_non_joue.contains(partenaire))
		{
			joueurs_confiance_non_joue.addElement(partenaire);
		}
		if(contrat.force()>1)
		{
			if(contrat.force()==3)
			{
				/*La couleur demandee n'est pas de l'atout et le pli n'est pas coupe*/
				ramasseur_virtuel_egal_certain=false;
				if(!main(cartes_possibles,couleur_demandee,numero).estVide()&&((CarteBelote)main(cartes_possibles,couleur_demandee,numero).carte(0)).force(couleur_atout,couleur_demandee)>carte_forte.force(couleur_atout,couleur_demandee))
				{/*Si le joueur numero peut prendre la main sans couper*/
					/*On ne sait pas si un joueur n'ayant pas joue va couper le pli ou non*/
					if(partenaire==ramasseur_virtuel)
					{
						if(ramasseur_bat_ss_cpr_adv(joueurs_non_confiance_non_joue, couleur_demandee, carte_forte, cartes_possibles, cartes_certaines))
						{
							return 1;
						}
						return-1;
					}/*Fin joueurs_de_confiance.contains(ramasseur_virtuel)*/
					return-1;
				}/*Fin si le joueur numero peut prendre la main sans couper*/
				/*Le joueur numero ne peut pas prendre la main*/
				if(partenaire==ramasseur_virtuel)
				{
					if(ramasseur_bat_ss_cpr_adv(joueurs_non_confiance_non_joue, couleur_demandee, carte_forte, cartes_possibles, cartes_certaines))
					{
						return 1;
					}
					return-1;
				}/*Fin joueurs_de_confiance.contains(ramasseur_virtuel)*/
				/*Maintenant le ramasseur virtuel n'est pas un joueur de confiance*/
				if(ramasseur_bat_ss_cpr_adv(joueurs_confiance_non_joue, couleur_demandee, carte_forte, cartes_possibles, cartes_certaines))
				{
					return 0;
				}
				return-1;
			}
			/*Le pli n'est pas coupe et la couleur demandee est l'atout*/
			if(main(cartes_certaines,couleur_demandee,numero).estVide()||((CarteBelote)main(cartes_certaines,couleur_demandee,numero).carte(0)).force(couleur_demandee,couleur_demandee,contrat)<carte_forte.force(couleur_demandee,couleur_demandee,contrat))
			{/*Si le joueur numero ne peut pas prendre la main sur demande d'atout*/
				if(partenaire==ramasseur_virtuel)
				{
					/*Si le ramasseur virtuel (de confiance, ici) domine certainement les joueurs de non confiance n'ayant pas joue*/
					if(ramasseur_bat_adv_demat(joueurs_non_confiance_non_joue, couleur_demandee, carte_forte, cartes_possibles))
					{
						return 1;
					}
					/*On cherche les joueurs de confiance battant de maniere certaine les joueurs de non confiance n'ayant pas joue ou possedant des cartes que les joueurs ayant joue n'ont pas ainsi que les joueurs de non confiance n'ayant pas joue*/
					if(existe_jou_bat_adv_demat(joueurs_non_confiance_non_joue, joueurs_confiance_non_joue, couleur_demandee, cartes_possibles, cartes_certaines))
					{
						return 1;
					}
					/*On cherche les joueurs de confiance battant de maniere certaine les joueurs de non confiance n'ayant pas joue ou possedant des cartes que les joueurs ayant joue n'ont pas ainsi que les joueurs de non confiance n'ayant pas joue*/
					if(existe_jou_bat_ptm_demat(joueurs_non_confiance_non_joue,joueurs_confiance_non_joue,joueurs_joue,cartes_possibles, couleur_demandee))
					{
						return 1;
					}
					/*On cherche les joueurs de non confiance battant de maniere certaine les joueurs de confiance n'ayant pas joue ou possedant des cartes que les joueurs ayant joue n'ont pas ainsi que les joueurs de non confiance n'ayant pas joue*/
					if(existe_jou_bat_adv_sur_demat(joueurs_confiance_non_joue, joueurs_non_confiance_non_joue, carte_forte, couleur_demandee, cartes_possibles, cartes_certaines))
					{
						return 0;
					}
					/*On cherche les joueurs de confiance battant de maniere certaine les joueurs de non confiance n'ayant pas joue ou possedant des cartes que les joueurs ayant joue n'ont pas ainsi que les joueurs de non confiance n'ayant pas joue*/
					if(existe_jou_bat_ptm_sur_demat(joueurs_confiance_non_joue, joueurs_non_confiance_non_joue, joueurs_joue, carte_forte, cartes_possibles, couleur_demandee))
					{
						return 0;
					}
					return-1;
				}
				/*ramasseur_virtuel n'est pas un joueur de confiance pour le joueur numero*/
				/*Si le ramasseur virtuel (de non confiance, ici) domine certainement les joueurs de non confiance n'ayant pas joue*/
				if(ramasseur_bat_adv_demat(joueurs_confiance_non_joue, couleur_demandee, carte_forte, cartes_possibles))
				{
					return 0;
				}
				/*On cherche les joueurs de non confiance battant de maniere certaine les joueurs de confiance n'ayant pas joue ou possedant des cartes que les joueurs ayant joue n'ont pas ainsi que les joueurs de non confiance n'ayant pas joue*/
				if(existe_jou_bat_adv_demat(joueurs_confiance_non_joue, joueurs_non_confiance_non_joue, couleur_demandee, cartes_possibles, cartes_certaines))
				{
					return 0;
				}
				/*On cherche les joueurs de confiance battant de maniere certaine les joueurs de non confiance n'ayant pas joue ou possedant des cartes que les joueurs ayant joue n'ont pas ainsi que les joueurs de non confiance n'ayant pas joue*/
				if(existe_jou_bat_ptm_demat(joueurs_confiance_non_joue, joueurs_non_confiance_non_joue, joueurs_joue, cartes_possibles, couleur_demandee))
				{
					return 0;
				}
				/*On cherche les joueurs de confiance battant de maniere certaine les joueurs de non confiance n'ayant pas joue ou possedant des cartes que les joueurs ayant joue n'ont pas ainsi que les joueurs de non confiance n'ayant pas joue*/
				if(existe_jou_bat_adv_sur_demat(joueurs_non_confiance_non_joue, joueurs_confiance_non_joue, carte_forte, couleur_demandee, cartes_possibles, cartes_certaines))
				{
					return 1;
				}
				/*On cherche les joueurs de confiance battant de maniere certaine les joueurs de non confiance n'ayant pas joue ou possedant des cartes que les joueurs ayant joue n'ont pas ainsi que les joueurs de non confiance n'ayant pas joue*/
				if(existe_jou_bat_ptm_sur_demat(joueurs_non_confiance_non_joue, joueurs_confiance_non_joue, joueurs_joue, carte_forte, cartes_possibles, couleur_demandee))
				{
					return 1;
				}
				return-1;
				/*Fin joueur_de_confiance.contains(ramasseur_virtuel)*/
			}/*Fin !cartes_certaines.get(couleur_demandee).get(numero).estVide()||cartes_certaines.get(1).get(numero).estVide()||cartes_certaines.get(1).get(numero).carte(0).getValeur()<carte_forte.getValeur()
				(fin test de possibilite pour le joueur numero de prendre le pli)*/
			/*Le joueur numero peut prendre la main en utilisant un atout sur demande d'atout*/
			/*On cherche les joueurs de confiance battant de maniere certaine les joueurs de non confiance n'ayant pas joue ou possedant des cartes que les joueurs ayant joue n'ont pas ainsi que les joueurs de non confiance n'ayant pas joue*/
			if(existe_jou_bat_adv_num_demat(joueurs_non_confiance_non_joue,joueurs_confiance_non_joue,numero,cartes_possibles,cartes_certaines, couleur_demandee))
			{
				return 1;
			}
			/*On cherche les joueurs de confiance battant de maniere certaine les joueurs de non confiance n'ayant pas joue ou possedant des cartes que les joueurs ayant joue n'ont pas ainsi que les joueurs de non confiance n'ayant pas joue*/
			if(existe_jou_bat_ptm_num_demat(joueurs_non_confiance_non_joue,joueurs_confiance_non_joue,joueurs_joue,numero,cartes_possibles, couleur_demandee))
			{
				return 1;
			}
			/*On cherche les joueurs de non confiance battant de maniere certaine les joueurs de confiance n'ayant pas joue ou possedant des cartes que les joueurs ayant joue n'ont pas ainsi que les joueurs de non confiance n'ayant pas joue*/
			if(existe_jou_bat_adv_num_demat(joueurs_confiance_non_joue,joueurs_non_confiance_non_joue, numero, cartes_possibles, cartes_certaines, couleur_demandee))
			{
				return 0;
			}
			/*On cherche les joueurs de confiance battant de maniere certaine les joueurs de non confiance n'ayant pas joue ou possedant des cartes que les joueurs ayant joue n'ont pas ainsi que les joueurs de non confiance n'ayant pas joue*/
			if(existe_jou_bat_ptm_num_demat(joueurs_confiance_non_joue,joueurs_non_confiance_non_joue,joueurs_joue,numero,cartes_possibles, couleur_demandee))
			{
				return 0;
			}
			return-1;
		}
		/*Le contrat n est ni sans-atout ni tout atout.*/
		if(carte_forte.couleur()==couleur_atout&&couleur_demandee!=couleur_atout)
		{/*Le pli est coupe*/
			if(!main(cartes_certaines,couleur_demandee,numero).estVide()||main(cartes_certaines,couleur_atout,numero).estVide()||((CarteBelote)main(cartes_certaines,couleur_atout,numero).carte(0)).force(couleur_atout,couleur_demandee)<carte_forte.force(couleur_atout,couleur_demandee))
			{/*Le joueur numero ne peut pas prendre la main*/
				if(partenaire==ramasseur_virtuel)
				{
					/*On cherche a savoir si le ramasseur virtuel (joueur de confiance) va avec sa coupe sur la couleur demandee dominer tous les atouts des joueurs de non confiance eventuels*/
					if(ramasseur_bat_adv_sur(joueurs_non_confiance_non_joue, couleur_demandee, carte_forte, cartes_possibles, cartes_certaines))
					{
						return 1;
					}
					/*On cherche les joueurs de confiance battant de maniere certaine les joueurs de non confiance n'ayant pas joue ou possedant des cartes que les joueurs ayant joue n'ont pas ainsi que les joueurs de non confiance n'ayant pas joue*/
					if(existe_joueur_non_joue_battant_adv(joueurs_non_confiance_non_joue, joueurs_confiance_non_joue, couleur_demandee, cartes_possibles, cartes_certaines))
					{
						return 1;
					}
					/*On cherche les joueurs de confiance battant de maniere certaine les joueurs de non confiance n'ayant pas joue ou possedant des cartes que les joueurs ayant joue n'ont pas ainsi que les joueurs de non confiance n'ayant pas joue*/
					if(existe_joueur_non_joue_battant_ptm(joueurs_non_confiance_non_joue, joueurs_confiance_non_joue, joueurs_joue, couleur_demandee, cartes_possibles, cartes_certaines))
					{
						return 1;
					}
					/*On cherche les joueurs de non confiance battant de maniere certaine les joueurs de confiance n'ayant pas joue ou possedant des cartes que les joueurs ayant joue n'ont pas ainsi que les joueurs de non confiance n'ayant pas joue*/
					if(existe_joueur_adv_ram_bat_adv_sur(joueurs_confiance_non_joue, joueurs_non_confiance_non_joue, couleur_demandee, carte_forte, cartes_possibles, cartes_certaines))
					{
						return 0;
					}
					/*On cherche les joueurs de confiance battant de maniere certaine les joueurs de non confiance n'ayant pas joue ou possedant des cartes que les joueurs ayant joue n'ont pas ainsi que les joueurs de non confiance n'ayant pas joue*/
					if(existe_joueur_adv_ram_bat_ptm_sur(joueurs_confiance_non_joue, joueurs_non_confiance_non_joue, joueurs_joue, couleur_demandee, cartes_possibles, cartes_certaines))
					{
						return 0;
					}
					return-1;
				}
				/*ramasseur_virtuel n'est pas un joueur de confiance pour le joueur numero*/
				/*On cherche a savoir si le ramasseur virtuel (joueur de non confiance) bat tous les joueurs de confiance n'ayant pas joue*/
				if(ramasseur_bat_adv_sur(joueurs_confiance_non_joue, couleur_demandee, carte_forte, cartes_possibles, cartes_certaines))
				{
					return 0;
				}
				/*On cherche les joueurs de non confiance battant de maniere certaine les joueurs de confiance n'ayant pas joue ou possedant des cartes que les joueurs ayant joue n'ont pas ainsi que les joueurs de non confiance n'ayant pas joue*/
				if(existe_joueur_non_joue_battant_adv(joueurs_confiance_non_joue, joueurs_non_confiance_non_joue, couleur_demandee, cartes_possibles, cartes_certaines))
				{
					return 0;
				}
				/*On cherche les joueurs de confiance battant de maniere certaine les joueurs de non confiance n'ayant pas joue ou possedant des cartes que les joueurs ayant joue n'ont pas ainsi que les joueurs de non confiance n'ayant pas joue*/
				if(existe_joueur_non_joue_battant_ptm(joueurs_confiance_non_joue, joueurs_non_confiance_non_joue, joueurs_joue, couleur_demandee, cartes_possibles, cartes_certaines))
				{
					return 0;
				}
				/*On cherche les joueurs de confiance battant de maniere certaine les joueurs de non confiance n'ayant pas joue ou possedant des cartes que les joueurs ayant joue n'ont pas ainsi que les joueurs de non confiance n'ayant pas joue*/
				if(existe_joueur_adv_ram_bat_adv_sur(joueurs_non_confiance_non_joue, joueurs_confiance_non_joue, couleur_demandee, carte_forte, cartes_possibles, cartes_certaines))
				{
					return 1;
				}
				/*On cherche les joueurs de confiance battant de maniere certaine les joueurs de non confiance n'ayant pas joue ou possedant des cartes que les joueurs ayant joue n'ont pas ainsi que les joueurs de non confiance n'ayant pas joue*/
				if(existe_joueur_adv_ram_bat_ptm_sur(joueurs_non_confiance_non_joue, joueurs_confiance_non_joue, joueurs_joue, couleur_demandee, cartes_possibles, cartes_certaines))
				{
					return 1;
				}
				return-1;
				/*Fin joueur_de_confiance.contains(ramasseur_virtuel)*/
			}/*Fin !cartes_certaines.get(couleur_demandee).get(numero).estVide()||cartes_certaines.get(1).get(numero).estVide()||cartes_certaines.get(1).get(numero).carte(0).getValeur()<carte_forte.getValeur()
 			(fin test de possibilite pour le joueur numero de prendre le pli)*/
			/*Le joueur numero peut prendre la main en surcoupant le ramasseur virtuel*/
			/*On cherche les joueurs de confiance battant de maniere certaine les joueurs de non confiance n'ayant pas joue ou possedant des cartes que les joueurs ayant joue n'ont pas ainsi que les joueurs de non confiance n'ayant pas joue*/
			if(existe_joueur_bat_adv_num(joueurs_non_confiance_non_joue, joueurs_confiance_non_joue, numero, couleur_demandee, cartes_possibles, cartes_certaines))
			{
				return 1;
			}
			/*On cherche les joueurs de confiance battant de maniere certaine les joueurs de non confiance n'ayant pas joue ou possedant des cartes que les joueurs ayant joue n'ont pas ainsi que les joueurs de non confiance n'ayant pas joue*/
			if(existe_joueur_bat_ptm_num(joueurs_non_confiance_non_joue, joueurs_confiance_non_joue, joueurs_joue, numero, couleur_demandee, cartes_possibles, cartes_certaines))
			{
				return 1;
			}
			/*On cherche les joueurs de non confiance battant de maniere certaine les joueurs de confiance n'ayant pas joue ou possedant des cartes que les joueurs ayant joue n'ont pas ainsi que les joueurs de non confiance n'ayant pas joue*/
			if(existe_joueur_bat_adv_num(joueurs_confiance_non_joue, joueurs_non_confiance_non_joue, numero, couleur_demandee, cartes_possibles, cartes_certaines))
			{
				return 0;
			}
			/*On cherche les joueurs de confiance battant de maniere certaine les joueurs de non confiance n'ayant pas joue ou possedant des cartes que les joueurs ayant joue n'ont pas ainsi que les joueurs de non confiance n'ayant pas joue*/
			if(existe_joueur_bat_ptm_num(joueurs_confiance_non_joue, joueurs_non_confiance_non_joue,joueurs_joue, numero, couleur_demandee, cartes_possibles, cartes_certaines))
			{
				return 0;
			}
			return-1;
		}
		if(carte_forte.couleur()==couleur_demandee&&couleur_demandee!=couleur_atout)
		{/*La couleur demandee n'est pas de l'atout et le pli n'est pas coupe*/
			ramasseur_virtuel_egal_certain=false;
			for(byte joueur:joueurs_confiance_non_joue)
			{
				ramasseur_virtuel_egal_certain|=va_couper(couleur_demandee, joueur, cartes_possibles, cartes_certaines);
			}
			if(ramasseur_virtuel_egal_certain)
			{
				ramasseur_virtuel_egal_certain=true;
				for(byte joueur:joueurs_non_confiance_non_joue)
				{
					ramasseur_virtuel_egal_certain&=ne_peut_couper(couleur_demandee, joueur, cartes_possibles, cartes_certaines);
				}
				if(ramasseur_virtuel_egal_certain)
				{
					return 1;
				}
				if(existe_joueur_non_joue_battant_adv(joueurs_non_confiance_non_joue,joueurs_confiance_non_joue,couleur_demandee,cartes_possibles,cartes_certaines))
				{
					return 1;
				}
				if(existe_joueur_non_joue_battant_ptm(joueurs_non_confiance_non_joue,joueurs_confiance_non_joue,joueurs_joue,couleur_demandee,cartes_possibles,cartes_certaines))
				{
					return 1;
				}
				if(existe_joueur_non_joue_battant_adv(joueurs_confiance_non_joue,joueurs_non_confiance_non_joue,couleur_demandee,cartes_possibles,cartes_certaines))
				{
					return 0;
				}
				if(existe_joueur_non_joue_battant_ptm(joueurs_confiance_non_joue,joueurs_non_confiance_non_joue,joueurs_joue,couleur_demandee,cartes_possibles,cartes_certaines))
				{
					return 0;
				}
				return-1;
			}
			ramasseur_virtuel_egal_certain=false;
			for(byte joueur:joueurs_non_confiance_non_joue)
			{
				ramasseur_virtuel_egal_certain|=va_couper(couleur_demandee, joueur, cartes_possibles, cartes_certaines);
			}
			if(ramasseur_virtuel_egal_certain)
			{
				for(byte joueur:joueurs_confiance_non_joue)
				{
					ramasseur_virtuel_egal_certain&=ne_peut_couper(couleur_demandee, joueur, cartes_possibles, cartes_certaines);
				}
				if(ramasseur_virtuel_egal_certain)
				{
					return 0;
				}
				if(existe_joueur_non_joue_battant_adv(joueurs_confiance_non_joue,joueurs_non_confiance_non_joue,couleur_demandee,cartes_possibles,cartes_certaines))
				{
					return 0;
				}
				if(existe_joueur_non_joue_battant_ptm(joueurs_confiance_non_joue,joueurs_non_confiance_non_joue,joueurs_joue,couleur_demandee,cartes_possibles,cartes_certaines))
				{
					return 0;
				}
				if(existe_joueur_non_joue_battant_adv(joueurs_non_confiance_non_joue,joueurs_confiance_non_joue,couleur_demandee,cartes_possibles,cartes_certaines))
				{
					return 1;
				}
				if(existe_joueur_non_joue_battant_ptm(joueurs_non_confiance_non_joue,joueurs_confiance_non_joue,joueurs_joue,couleur_demandee,cartes_possibles,cartes_certaines))
				{
					return 1;
				}
				return-1;
			}
			if(!main(cartes_possibles,couleur_demandee,numero).estVide()&&((CarteBelote)main(cartes_possibles,couleur_demandee,numero).carte(0)).force(couleur_atout,couleur_demandee)>carte_forte.force(couleur_atout,couleur_demandee))
			{/*Si le joueur numero peut prendre la main sans couper*/
				/*On ne sait pas si un joueur n'ayant pas joue va couper le pli ou non*/
				if(partenaire==ramasseur_virtuel)
				{
					if(ramasseur_bat_ss_cpr_adv(joueurs_non_confiance_non_joue, couleur_demandee, carte_forte, cartes_possibles, cartes_certaines))
					{
						return 1;
					}
					return-1;
				}/*Fin joueurs_de_confiance.contains(ramasseur_virtuel)*/
				return-1;
			}/*Fin si le joueur numero peut prendre la main sans couper*/
			if(peut_couper(couleur_demandee,numero,cartes_possibles))
			{/*Si le joueur numero peut prendre la main en coupant*/
				/*On cherche les joueurs de confiance battant de maniere certaine les joueurs de non confiance n'ayant pas joue ou possedant des cartes que les joueurs ayant joue n'ont pas ainsi que les joueurs de non confiance n'ayant pas joue*/
				if(existe_joueur_bat_adv_num(joueurs_non_confiance_non_joue, joueurs_confiance_non_joue, numero, couleur_demandee, cartes_possibles, cartes_certaines))
				{
					return 1;
				}
				/*On cherche les joueurs de confiance battant de maniere certaine les joueurs de non confiance n'ayant pas joue ou possedant des cartes que les joueurs ayant joue n'ont pas ainsi que les joueurs de non confiance n'ayant pas joue*/
				if(existe_joueur_bat_ptm_num(joueurs_non_confiance_non_joue, joueurs_confiance_non_joue, joueurs_joue, numero, couleur_demandee, cartes_possibles, cartes_certaines))
				{
					return 1;
				}
				/*On cherche les joueurs de non confiance battant de maniere certaine les joueurs de confiance n'ayant pas joue ou possedant des cartes que les joueurs ayant joue n'ont pas ainsi que les joueurs de non confiance n'ayant pas joue*/
				if(existe_joueur_bat_adv_num(joueurs_confiance_non_joue, joueurs_non_confiance_non_joue, numero, couleur_demandee, cartes_possibles, cartes_certaines))
				{
					return 0;
				}
				/*On cherche les joueurs de confiance battant de maniere certaine les joueurs de non confiance n'ayant pas joue ou possedant des cartes que les joueurs ayant joue n'ont pas ainsi que les joueurs de non confiance n'ayant pas joue*/
				if(existe_joueur_bat_ptm_num(joueurs_confiance, joueurs_non_confiance_non_joue, joueurs_joue, numero, couleur_demandee, cartes_possibles, cartes_certaines))
				{
					return 0;
				}
				return-1;
			}
			/*Le joueur numero ne peut pas prendre la main*/
			if(partenaire==ramasseur_virtuel)
			{
				if(ramasseur_bat_ss_cpr_adv(joueurs_non_confiance_non_joue, couleur_demandee, carte_forte, cartes_possibles, cartes_certaines))
				{
					return 1;
				}
				return-1;
			}/*Fin joueurs_de_confiance.contains(ramasseur_virtuel)*/
			/*Maintenant le ramasseur virtuel n'est pas un joueur de confiance*/
			if(ramasseur_bat_ss_cpr_adv(joueurs_confiance_non_joue, couleur_demandee, carte_forte, cartes_possibles, cartes_certaines))
			{
				return 0;
			}
			return-1;
		}
		/*Le pli n'est pas coupe et la couleur demandee est l'atout*/
		if(main(cartes_certaines,couleur_atout,numero).estVide()||((CarteBelote)main(cartes_certaines,couleur_atout,numero).carte(0)).force(couleur_atout,couleur_demandee)<carte_forte.force(couleur_atout,couleur_demandee))
		{/*Si le joueur numero ne peut pas prendre la main sur demande d'atout*/
			if(partenaire==ramasseur_virtuel)
			{
				/*Si le ramasseur virtuel (de confiance, ici) domine certainement les joueurs de non confiance n'ayant pas joue*/
				if(ramasseur_bat_adv_demat(joueurs_non_confiance_non_joue, couleur_atout, carte_forte, cartes_possibles))
				{
					return 1;
				}
				/*On cherche les joueurs de confiance battant de maniere certaine les joueurs de non confiance n'ayant pas joue ou possedant des cartes que les joueurs ayant joue n'ont pas ainsi que les joueurs de non confiance n'ayant pas joue*/
				if(existe_jou_bat_adv_demat(joueurs_non_confiance_non_joue, joueurs_confiance_non_joue, couleur_atout, cartes_possibles, cartes_certaines))
				{
					return 1;
				}
				/*On cherche les joueurs de confiance battant de maniere certaine les joueurs de non confiance n'ayant pas joue ou possedant des cartes que les joueurs ayant joue n'ont pas ainsi que les joueurs de non confiance n'ayant pas joue*/
				if(existe_jou_bat_ptm_demat(joueurs_non_confiance_non_joue,joueurs_confiance_non_joue,joueurs_joue,cartes_possibles, couleur_atout))
				{
					return 1;
				}
				/*On cherche les joueurs de non confiance battant de maniere certaine les joueurs de confiance n'ayant pas joue ou possedant des cartes que les joueurs ayant joue n'ont pas ainsi que les joueurs de non confiance n'ayant pas joue*/
				if(existe_jou_bat_adv_sur_demat(joueurs_confiance_non_joue, joueurs_non_confiance_non_joue, carte_forte, couleur_atout, cartes_possibles, cartes_certaines))
				{
					return 0;
				}
				/*On cherche les joueurs de confiance battant de maniere certaine les joueurs de non confiance n'ayant pas joue ou possedant des cartes que les joueurs ayant joue n'ont pas ainsi que les joueurs de non confiance n'ayant pas joue*/
				if(existe_jou_bat_ptm_sur_demat(joueurs_confiance_non_joue, joueurs_non_confiance_non_joue, joueurs_joue, carte_forte, cartes_possibles, couleur_atout))
				{
					return 0;
				}
				return-1;
			}
			/*ramasseur_virtuel n'est pas un joueur de confiance pour le joueur numero*/
			/*Si le ramasseur virtuel (de non confiance, ici) domine certainement les joueurs de non confiance n'ayant pas joue*/
			if(ramasseur_bat_adv_demat(joueurs_confiance_non_joue, couleur_atout, carte_forte, cartes_possibles))
			{
				return 0;
			}
			/*On cherche les joueurs de non confiance battant de maniere certaine les joueurs de confiance n'ayant pas joue ou possedant des cartes que les joueurs ayant joue n'ont pas ainsi que les joueurs de non confiance n'ayant pas joue*/
			if(existe_jou_bat_adv_demat(joueurs_confiance_non_joue, joueurs_non_confiance_non_joue, couleur_atout, cartes_possibles, cartes_certaines))
			{
				return 0;
			}
			/*On cherche les joueurs de confiance battant de maniere certaine les joueurs de non confiance n'ayant pas joue ou possedant des cartes que les joueurs ayant joue n'ont pas ainsi que les joueurs de non confiance n'ayant pas joue*/
			if(existe_jou_bat_ptm_demat(joueurs_confiance_non_joue, joueurs_non_confiance_non_joue, joueurs_joue, cartes_possibles, couleur_atout))
			{
				return 0;
			}
			/*On cherche les joueurs de confiance battant de maniere certaine les joueurs de non confiance n'ayant pas joue ou possedant des cartes que les joueurs ayant joue n'ont pas ainsi que les joueurs de non confiance n'ayant pas joue*/
			if(existe_jou_bat_adv_sur_demat(joueurs_non_confiance_non_joue, joueurs_confiance_non_joue, carte_forte, couleur_atout, cartes_possibles, cartes_certaines))
			{
				return 1;
			}
			/*On cherche les joueurs de confiance battant de maniere certaine les joueurs de non confiance n'ayant pas joue ou possedant des cartes que les joueurs ayant joue n'ont pas ainsi que les joueurs de non confiance n'ayant pas joue*/
			if(existe_jou_bat_ptm_sur_demat(joueurs_non_confiance_non_joue, joueurs_confiance_non_joue, joueurs_joue, carte_forte, cartes_possibles, couleur_atout))
			{
				return 1;
			}
			return-1;
			/*Fin joueur_de_confiance.contains(ramasseur_virtuel)*/
		}/*Fin !cartes_certaines.get(couleur_demandee).get(numero).estVide()||cartes_certaines.get(1).get(numero).estVide()||cartes_certaines.get(1).get(numero).carte(0).getValeur()<carte_forte.getValeur()
			(fin test de possibilite pour le joueur numero de prendre le pli)*/
		/*Le joueur numero peut prendre la main en utilisant un atout sur demande d'atout*/
		/*On cherche les joueurs de confiance battant de maniere certaine les joueurs de non confiance n'ayant pas joue ou possedant des cartes que les joueurs ayant joue n'ont pas ainsi que les joueurs de non confiance n'ayant pas joue*/
		if(existe_jou_bat_adv_num_demat(joueurs_non_confiance_non_joue,joueurs_confiance_non_joue,numero,cartes_possibles,cartes_certaines, couleur_atout))
		{
			return 1;
		}
		/*On cherche les joueurs de confiance battant de maniere certaine les joueurs de non confiance n'ayant pas joue ou possedant des cartes que les joueurs ayant joue n'ont pas ainsi que les joueurs de non confiance n'ayant pas joue*/
		if(existe_jou_bat_ptm_num_demat(joueurs_non_confiance_non_joue,joueurs_confiance_non_joue,joueurs_joue,numero,cartes_possibles, couleur_atout))
		{
			return 1;
		}
		/*On cherche les joueurs de non confiance battant de maniere certaine les joueurs de confiance n'ayant pas joue ou possedant des cartes que les joueurs ayant joue n'ont pas ainsi que les joueurs de non confiance n'ayant pas joue*/
		if(existe_jou_bat_adv_num_demat(joueurs_confiance_non_joue,joueurs_non_confiance_non_joue, numero, cartes_possibles, cartes_certaines, couleur_atout))
		{
			return 0;
		}
		/*On cherche les joueurs de confiance battant de maniere certaine les joueurs de non confiance n'ayant pas joue ou possedant des cartes que les joueurs ayant joue n'ont pas ainsi que les joueurs de non confiance n'ayant pas joue*/
		if(existe_jou_bat_ptm_num_demat(joueurs_confiance_non_joue,joueurs_non_confiance_non_joue,joueurs_joue,numero,cartes_possibles, couleur_atout))
		{
			return 0;
		}
		return-1;
	}
	/**Retourne vrai si et seulement si le joueur ne peut pas couper avec un atout, car il possede encore de la couleur demandee ou ne possede pas d atout*/
	private boolean ne_peut_couper(byte couleur,byte numero,Vector<Vector<MainBelote>> cartes_possibles,Vector<Vector<MainBelote>> cartes_certaines)
	{
		return main(cartes_possibles,carteAppelee.couleur(),numero).estVide()||!main(cartes_certaines,couleur,numero).estVide();
	}
	private boolean existe_jou_bat_ptm_num_demat(Vector<Byte> equipe_a_battre,Vector<Byte> equipe_dom,Vector<Byte> joueurs_joue,byte numero,Vector<Vector<MainBelote>> cartes_possibles, byte couleur_demandee)
	{
		byte couleur_atout=couleur_atout();
		boolean ramasseur_deter=false;
		boolean ramasseur_virtuel_egal_certain=false;
		for(byte joueur:equipe_dom)
		{
			boolean joueur_bat_adversaire=true;
			if(!main(cartes_possibles,couleur_demandee,joueur).estVide())
			{
				byte max_force=((CarteBelote)main(cartes_possibles,couleur_demandee,joueur).carte(0)).force(couleur_atout,couleur_demandee,contrat);
				for(byte joueur2:equipe_a_battre)
				{
					ramasseur_virtuel_egal_certain=main(cartes_possibles,couleur_demandee,joueur2).estVide();
					if(!ramasseur_virtuel_egal_certain)
					{
						ramasseur_virtuel_egal_certain|=max_force>((CarteBelote)main(cartes_possibles,couleur_demandee,joueur2).carte(0)).force(couleur_atout,couleur_demandee,contrat);
					}
					joueur_bat_adversaire&=ramasseur_virtuel_egal_certain;
				}
				for(byte joueur2:joueurs_joue)
				{
					ramasseur_virtuel_egal_certain=main(cartes_possibles,couleur_demandee,joueur2).estVide();
					if(!ramasseur_virtuel_egal_certain)
					{
						ramasseur_virtuel_egal_certain|=max_force>((CarteBelote)main(cartes_possibles,couleur_demandee,joueur2).carte(0)).force(couleur_atout,couleur_demandee,contrat);
					}
					joueur_bat_adversaire&=ramasseur_virtuel_egal_certain;
				}
				joueur_bat_adversaire&=max_force>((CarteBelote)main(cartes_possibles,couleur_demandee,numero).carte(0)).force(couleur_atout,couleur_demandee,contrat);
				ramasseur_deter|=joueur_bat_adversaire;
			}
		}
		return ramasseur_deter;
	}
	private boolean existe_jou_bat_adv_num_demat(Vector<Byte> equipe_a_battre,Vector<Byte> equipe_dom,byte numero,Vector<Vector<MainBelote>> cartes_possibles,Vector<Vector<MainBelote>> cartes_certaines, byte couleur_demandee)
	{
		byte couleur_atout=couleur_atout();
		boolean ramasseur_deter=false;
		boolean ramasseur_virtuel_egal_certain=false;
		for(byte joueur:equipe_dom)
		{
			boolean joueur_bat_adversaire=true;
			if(!main(cartes_certaines,couleur_demandee,joueur).estVide())
			{
				byte max_force=((CarteBelote)main(cartes_certaines,couleur_demandee,joueur).carte(0)).force(couleur_atout,couleur_demandee,contrat);
				for(byte joueur2:equipe_a_battre)
				{
					ramasseur_virtuel_egal_certain=main(cartes_possibles,couleur_demandee,joueur2).estVide();
					if(!ramasseur_virtuel_egal_certain)
					{
						ramasseur_virtuel_egal_certain|=max_force>((CarteBelote)main(cartes_possibles,couleur_demandee,joueur2).carte(0)).force(couleur_atout,couleur_demandee,contrat);
					}
					joueur_bat_adversaire&=ramasseur_virtuel_egal_certain;
				}
				joueur_bat_adversaire&=max_force>((CarteBelote)main(cartes_possibles,couleur_demandee,numero).carte(0)).force(couleur_atout,couleur_demandee,contrat);
				ramasseur_deter|=joueur_bat_adversaire;
			}
		}
		return ramasseur_deter;
	}
	private boolean existe_jou_bat_ptm_sur_demat(Vector<Byte> equipe_a_battre,Vector<Byte> equipe_dom,Vector<Byte> joueurs_joue,CarteBelote carte_forte,Vector<Vector<MainBelote>> cartes_possibles, byte couleur_demandee)
	{
		byte couleur_atout=couleur_atout();
		boolean ramasseur_deter=false;
		boolean ramasseur_virtuel_egal_certain=false;
		for(byte joueur:equipe_dom)
		{
			boolean joueur_bat_adversaire=true;
			if(!main(cartes_possibles,couleur_demandee,joueur).estVide())
			{
				byte max_force=((CarteBelote)main(cartes_possibles,couleur_demandee,joueur).carte(0)).force(couleur_atout,couleur_demandee,contrat);
				for(byte joueur2:equipe_a_battre)
				{
					ramasseur_virtuel_egal_certain=main(cartes_possibles,couleur_demandee,joueur2).estVide();
					if(!ramasseur_virtuel_egal_certain)
					{
						ramasseur_virtuel_egal_certain|=max_force>((CarteBelote)main(cartes_possibles,couleur_demandee,joueur2).carte(0)).force(couleur_atout,couleur_demandee,contrat);
					}
					joueur_bat_adversaire&=ramasseur_virtuel_egal_certain;
				}
				for(byte joueur2:joueurs_joue)
				{
					ramasseur_virtuel_egal_certain=main(cartes_possibles,couleur_demandee,joueur2).estVide();
					if(!ramasseur_virtuel_egal_certain)
					{
						ramasseur_virtuel_egal_certain|=max_force>((CarteBelote)main(cartes_possibles,couleur_demandee,joueur2).carte(0)).force(couleur_atout,couleur_demandee,contrat);
					}
					joueur_bat_adversaire&=ramasseur_virtuel_egal_certain;
				}
				joueur_bat_adversaire&=max_force>carte_forte.force(couleur_atout,couleur_demandee,contrat);
				ramasseur_deter|=joueur_bat_adversaire;
			}
		}
		return ramasseur_deter;
	}
	private boolean existe_jou_bat_adv_sur_demat(Vector<Byte> equipe_a_battre,Vector<Byte> equipe_dom,CarteBelote carte_forte,byte couleur_demadee,Vector<Vector<MainBelote>> cartes_possibles, Vector<Vector<MainBelote>> cartes_certaines)
	{
		byte couleur_atout=couleur_atout();
		boolean ramasseur_deter=false;
		boolean ramasseur_virtuel_egal_certain=false;
		for(byte joueur:equipe_dom)
		{
			boolean joueur_bat_adversaire=true;
			if(!main(cartes_certaines,couleur_demadee,joueur).estVide())
			{
				byte max_force=((CarteBelote)main(cartes_certaines,couleur_demadee,joueur).carte(0)).force(couleur_atout,couleur_demadee,contrat);
				for(byte joueur2:equipe_a_battre)
				{
					ramasseur_virtuel_egal_certain=main(cartes_possibles,couleur_demadee,joueur2).estVide();
					if(!ramasseur_virtuel_egal_certain)
					{
						ramasseur_virtuel_egal_certain|=max_force>((CarteBelote)main(cartes_possibles,couleur_demadee,joueur2).carte(0)).force(couleur_atout,couleur_demadee,contrat);
					}
					joueur_bat_adversaire&=ramasseur_virtuel_egal_certain;
				}
				joueur_bat_adversaire&=max_force>carte_forte.force(couleur_atout,couleur_demadee,contrat);
				ramasseur_deter|=joueur_bat_adversaire;
			}
		}
		return ramasseur_deter;
	}
	private boolean existe_jou_bat_ptm_demat(Vector<Byte> equipe_a_battre,Vector<Byte> equipe_dom,Vector<Byte> joueurs_joue,Vector<Vector<MainBelote>> cartes_possibles, byte couleur_demandee)
	{
		byte couleur_atout=couleur_atout();
		boolean ramasseur_deter=false;
		boolean ramasseur_virtuel_egal_certain=false;
		for(byte joueur:equipe_dom)
		{
			boolean joueur_bat_adversaire=true;
			if(!main(cartes_possibles,couleur_demandee,joueur).estVide())
			{
				byte max_force=((CarteBelote)main(cartes_possibles,couleur_demandee,joueur).carte(0)).force(couleur_atout,couleur_demandee,contrat);
				for(byte joueur2:equipe_a_battre)
				{
					ramasseur_virtuel_egal_certain=main(cartes_possibles,couleur_demandee,joueur2).estVide();
					if(!ramasseur_virtuel_egal_certain)
					{
						ramasseur_virtuel_egal_certain|=max_force>((CarteBelote)main(cartes_possibles,couleur_demandee,joueur2).carte(0)).force(couleur_atout,couleur_demandee,contrat);
					}
					joueur_bat_adversaire&=ramasseur_virtuel_egal_certain;
				}
				for(byte joueur2:joueurs_joue)
				{
					ramasseur_virtuel_egal_certain=main(cartes_possibles,couleur_demandee,joueur2).estVide();
					if(!ramasseur_virtuel_egal_certain)
					{
						ramasseur_virtuel_egal_certain|=max_force>((CarteBelote)main(cartes_possibles,couleur_demandee,joueur2).carte(0)).force(couleur_atout,couleur_demandee,contrat);
					}
					joueur_bat_adversaire&=ramasseur_virtuel_egal_certain;
				}
				ramasseur_deter|=joueur_bat_adversaire;
			}
		}
		return ramasseur_deter;
	}
	private boolean existe_jou_bat_adv_demat(Vector<Byte> equipe_a_battre,Vector<Byte> equipe_dom,byte couleur_demandee,Vector<Vector<MainBelote>> cartes_possibles, Vector<Vector<MainBelote>> cartes_certaines)
	{
		byte couleur_atout=couleur_atout();
		boolean ramasseur_deter=false;
		boolean ramasseur_virtuel_egal_certain=false;
		for(byte joueur:equipe_dom)
		{
			boolean joueur_bat_adversaire=true;
			if(!main(cartes_certaines,couleur_atout,joueur).estVide())
			{
				byte max_force=((CarteBelote)main(cartes_certaines,couleur_demandee,joueur).carte(0)).force(couleur_atout,couleur_demandee,contrat);
				for(byte joueur2:equipe_a_battre)
				{
					ramasseur_virtuel_egal_certain=main(cartes_possibles,couleur_demandee,joueur2).estVide();
					if(!ramasseur_virtuel_egal_certain)
					{
						ramasseur_virtuel_egal_certain|=max_force>((CarteBelote)main(cartes_possibles,couleur_demandee,joueur2).carte(0)).force(couleur_atout,couleur_demandee,contrat);
					}
					joueur_bat_adversaire&=ramasseur_virtuel_egal_certain;
				}
				ramasseur_deter|=joueur_bat_adversaire;
			}
		}
		return ramasseur_deter;
	}
	private boolean ramasseur_bat_adv_demat(Vector<Byte> equipe_a_battre,byte couleur_demandee,CarteBelote carte_forte, Vector<Vector<MainBelote>> cartes_possibles)
	{
		byte couleur_atout=couleur_atout();
		boolean ramasseur_deter=true;
		boolean ramasseur_virtuel_egal_certain=false;
		byte max_force=carte_forte.force(couleur_atout,couleur_demandee,contrat);
		for(byte joueur:equipe_a_battre)
		{
			ramasseur_virtuel_egal_certain=main(cartes_possibles,couleur_demandee,joueur).estVide();
			if(!ramasseur_virtuel_egal_certain)
			{
				ramasseur_virtuel_egal_certain|=max_force>((CarteBelote)main(cartes_possibles,couleur_demandee,joueur).carte(0)).force(couleur_atout,couleur_demandee,contrat);
			}
			ramasseur_deter&=ramasseur_virtuel_egal_certain;
		}
		return ramasseur_deter;
	}
	private boolean existe_joueur_bat_ptm_num(Vector<Byte> equipe_a_battre,Vector<Byte> equipe_dom,Vector<Byte> joueurs_joue,byte numero,byte couleur_demandee,Vector<Vector<MainBelote>> cartes_possibles,Vector<Vector<MainBelote>> cartes_certaines)
	{
		byte couleur_atout=carteAppelee.couleur();
		boolean ramasseur_deter=false;
		boolean ramasseur_virtuel_egal_certain=false;
		for(byte joueur:equipe_dom)
		{
			boolean joueur_bat_adversaire=true;
			if(va_couper(couleur_demandee, joueur, cartes_possibles, cartes_certaines))
			{
				byte max_force=((CarteBelote)main(cartes_possibles,couleur_atout,joueur).carte(0)).force(couleur_atout,couleur_demandee);
				for(byte joueur2:equipe_a_battre)
				{
					ramasseur_virtuel_egal_certain=main(cartes_possibles,couleur_atout,joueur2).estVide();
					if(!ramasseur_virtuel_egal_certain)
					{
						ramasseur_virtuel_egal_certain|=max_force>((CarteBelote)main(cartes_possibles,couleur_atout,joueur2).carte(0)).force(couleur_atout,couleur_demandee);
					}
					ramasseur_virtuel_egal_certain|=!main(cartes_certaines,couleur_demandee,joueur2).estVide();
					joueur_bat_adversaire&=ramasseur_virtuel_egal_certain;
				}
				for(byte joueur2:joueurs_joue)
				{
					ramasseur_virtuel_egal_certain=main(cartes_possibles,couleur_atout,joueur2).estVide();
					if(!ramasseur_virtuel_egal_certain)
					{
						ramasseur_virtuel_egal_certain|=max_force>((CarteBelote)main(cartes_possibles,couleur_atout,joueur2).carte(0)).force(couleur_atout,couleur_demandee);
					}
					joueur_bat_adversaire&=ramasseur_virtuel_egal_certain;
				}
				joueur_bat_adversaire&=max_force>((CarteBelote)main(cartes_certaines,couleur_atout,numero).carte(0)).force(couleur_atout,couleur_demandee);
				ramasseur_deter|=joueur_bat_adversaire;
			}
		}
		return ramasseur_deter;
	}
	private boolean existe_joueur_bat_adv_num(Vector<Byte> equipe_a_battre,Vector<Byte> equipe_dom,byte numero,byte couleur_demandee,Vector<Vector<MainBelote>> cartes_possibles,Vector<Vector<MainBelote>> cartes_certaines)
	{
		byte couleur_atout=carteAppelee.couleur();
		boolean ramasseur_deter=false;
		boolean ramasseur_virtuel_egal_certain=false;
		for(byte joueur:equipe_dom)
		{
			boolean joueur_bat_adversaire=true;
			if(va_couper(couleur_demandee, joueur, cartes_possibles, cartes_certaines))
			{
				byte max_force=((CarteBelote)main(cartes_certaines,couleur_atout,joueur).carte(0)).force(couleur_atout,couleur_demandee);
				for(byte joueur2:equipe_a_battre)
				{
					ramasseur_virtuel_egal_certain=main(cartes_possibles,couleur_atout,joueur2).estVide();
					if(!ramasseur_virtuel_egal_certain)
					{
						ramasseur_virtuel_egal_certain|=max_force>((CarteBelote)main(cartes_possibles,couleur_atout,joueur2).carte(0)).force(couleur_atout,couleur_demandee);
					}
					ramasseur_virtuel_egal_certain|=!main(cartes_certaines,couleur_demandee,joueur2).estVide();
					joueur_bat_adversaire&=ramasseur_virtuel_egal_certain;
				}
				joueur_bat_adversaire&=max_force>((CarteBelote)main(cartes_certaines,couleur_atout,numero).carte(0)).force(couleur_atout,couleur_demandee);
				ramasseur_deter|=joueur_bat_adversaire;
			}
		}
		return ramasseur_deter;
	}
	private boolean existe_joueur_adv_ram_bat_ptm_sur(Vector<Byte> equipe_a_battre,Vector<Byte> equipe_dom,Vector<Byte> joueurs_joue,byte couleur_demandee,Vector<Vector<MainBelote>> cartes_possibles,Vector<Vector<MainBelote>> cartes_certaines)
	{
		byte couleur_atout=carteAppelee.couleur();
		boolean ramasseur_deter=false;
		boolean ramasseur_virtuel_egal_certain=false;
		for(byte joueur:equipe_dom)
		{
			boolean joueur_bat_adversaire=true;
			if(va_couper(couleur_demandee, joueur, cartes_possibles, cartes_certaines))
			{
				byte max_force=((CarteBelote)main(cartes_possibles,couleur_atout,joueur).carte(0)).force(couleur_atout,couleur_demandee);
				for(byte joueur2:equipe_a_battre)
				{
					ramasseur_virtuel_egal_certain=main(cartes_possibles,couleur_atout,joueur2).estVide();
					if(!ramasseur_virtuel_egal_certain)
					{
						ramasseur_virtuel_egal_certain|=max_force>((CarteBelote)main(cartes_possibles,couleur_atout,joueur2).carte(0)).force(couleur_atout,couleur_demandee);
					}
					ramasseur_virtuel_egal_certain|=!main(cartes_certaines,couleur_demandee,joueur2).estVide();
					joueur_bat_adversaire&=ramasseur_virtuel_egal_certain;
				}
				for(byte joueur2:joueurs_joue)
				{
					ramasseur_virtuel_egal_certain=main(cartes_possibles,couleur_atout,joueur2).estVide();
					if(!ramasseur_virtuel_egal_certain)
					{
						ramasseur_virtuel_egal_certain|=max_force>((CarteBelote)main(cartes_possibles,couleur_atout,joueur2).carte(0)).force(couleur_atout,couleur_demandee);
					}
					joueur_bat_adversaire&=ramasseur_virtuel_egal_certain;
				}
				ramasseur_deter|=joueur_bat_adversaire;
			}
		}
		return ramasseur_deter;
	}
	private boolean existe_joueur_adv_ram_bat_adv_sur(Vector<Byte> equipe_a_battre,Vector<Byte> equipe_dom,byte couleur_demandee,CarteBelote carte_forte,Vector<Vector<MainBelote>> cartes_possibles,Vector<Vector<MainBelote>> cartes_certaines)
	{
		byte couleur_atout=carteAppelee.couleur();
		boolean ramasseur_deter=false;
		boolean ramasseur_virtuel_egal_certain=false;
		for(byte joueur:equipe_dom)
		{
			boolean joueur_bat_adversaire=true;
			if(va_couper(couleur_demandee, joueur, cartes_possibles, cartes_certaines))
			{
				for(byte joueur2:equipe_a_battre)
				{
					ramasseur_virtuel_egal_certain=main(cartes_possibles,couleur_atout,joueur2).estVide();
					if(!ramasseur_virtuel_egal_certain)
					{
						ramasseur_virtuel_egal_certain|=((CarteBelote)main(cartes_certaines,couleur_atout,joueur).carte(0)).force(couleur_atout,couleur_demandee)>((CarteBelote)main(cartes_possibles,couleur_atout,joueur2).carte(0)).force(couleur_atout,couleur_demandee);
					}
					ramasseur_virtuel_egal_certain|=!main(cartes_certaines,couleur_demandee,joueur2).estVide();
					joueur_bat_adversaire&=ramasseur_virtuel_egal_certain;
				}
				joueur_bat_adversaire&=((CarteBelote)main(cartes_certaines,couleur_atout,joueur).carte(0)).force(couleur_atout,couleur_demandee)>carte_forte.force(couleur_atout,couleur_demandee);
				ramasseur_deter|=joueur_bat_adversaire;
			}
		}
		return ramasseur_deter;
	}
	private boolean existe_joueur_non_joue_battant_ptm(Vector<Byte> equipe_a_battre,Vector<Byte> equipe_dom,Vector<Byte> joueurs_joue,byte couleur_demandee,Vector<Vector<MainBelote>> cartes_possibles,Vector<Vector<MainBelote>> cartes_certaines)
	{
		byte couleur_atout=carteAppelee.couleur();
		boolean ramasseur_deter=false;
		boolean ramasseur_virtuel_egal_certain=false;
		for(byte joueur:equipe_dom)
		{
			boolean joueur_bat_adversaire=true;
			if(va_couper(couleur_demandee, joueur, cartes_possibles, cartes_certaines))
			{
				byte max_force=((CarteBelote)main(cartes_possibles,couleur_atout,joueur).carte(0)).force(couleur_atout,couleur_demandee);
				for(byte joueur2:equipe_a_battre)
				{
					ramasseur_virtuel_egal_certain=main(cartes_possibles,couleur_atout,joueur2).estVide();
					if(!ramasseur_virtuel_egal_certain)
					{
						ramasseur_virtuel_egal_certain|=max_force>((CarteBelote)main(cartes_possibles,couleur_atout,joueur2).carte(0)).force(couleur_atout,couleur_demandee);
					}
					ramasseur_virtuel_egal_certain|=!main(cartes_certaines,couleur_demandee,joueur2).estVide();
					joueur_bat_adversaire&=ramasseur_virtuel_egal_certain;
				}
				for(byte joueur2:joueurs_joue)
				{
					ramasseur_virtuel_egal_certain=main(cartes_possibles,couleur_atout,joueur2).estVide();
					if(!ramasseur_virtuel_egal_certain)
					{
						ramasseur_virtuel_egal_certain|=max_force>((CarteBelote)main(cartes_possibles,couleur_atout,joueur2).carte(0)).force(couleur_atout,couleur_demandee);
					}
					joueur_bat_adversaire&=ramasseur_virtuel_egal_certain;
				}
				ramasseur_deter|=joueur_bat_adversaire;
			}
		}
		return ramasseur_deter;
	}
	private boolean existe_joueur_non_joue_battant_adv(Vector<Byte> equipe_a_battre,Vector<Byte> equipe_dom,byte couleur_demandee,Vector<Vector<MainBelote>> cartes_possibles,Vector<Vector<MainBelote>> cartes_certaines)
	{
		byte couleur_atout=carteAppelee.couleur();
		boolean ramasseur_deter=false;
		boolean ramasseur_virtuel_egal_certain=false;
		for(byte joueur:equipe_dom)
		{/*On cherche les joueurs de confiance battant de maniere certaine les joueurs de non confiance n'ayant pas joue ou possedant des cartes que les joueurs ayant joue n'ont pas ainsi que les joueurs de non confiance n'ayant pas joue*/
			boolean joueur_bat_adversaire = true;
			if(va_couper(couleur_demandee,joueur,cartes_possibles,cartes_certaines))
			{
				for(byte joueur2:equipe_a_battre)
				{
					ramasseur_virtuel_egal_certain=main(cartes_possibles,couleur_atout,joueur2).estVide();
					if(!ramasseur_virtuel_egal_certain)
					{
						ramasseur_virtuel_egal_certain|=((CarteBelote)main(cartes_certaines,couleur_atout,joueur).carte(0)).force(couleur_atout,couleur_demandee)>((CarteBelote)main(cartes_possibles,couleur_atout,joueur2).carte(0)).force(couleur_atout,couleur_demandee);
					}
					ramasseur_virtuel_egal_certain|=!main(cartes_certaines,couleur_demandee,joueur2).estVide();
					joueur_bat_adversaire&=ramasseur_virtuel_egal_certain;
				}
				ramasseur_deter|=joueur_bat_adversaire;
			}
		}
		return ramasseur_deter;
	}
	private boolean ramasseur_bat_adv_sur(Vector<Byte> equipe_a_battre,byte couleur_demandee,CarteBelote carte_forte,Vector<Vector<MainBelote>> cartes_possibles,Vector<Vector<MainBelote>> cartes_certaines)
	{
		byte couleur_atout=couleur_atout();
		boolean ramasseur_virtuel_egal_certain=false;
		boolean ramasseur_deter=true;
		byte max_force=carte_forte.force(couleur_atout,couleur_demandee);
		for(byte joueur:equipe_a_battre)
		{
			ramasseur_virtuel_egal_certain=main(cartes_possibles,couleur_atout,joueur).estVide();
			if(!ramasseur_virtuel_egal_certain)
			{
				ramasseur_virtuel_egal_certain|=max_force>((CarteBelote)main(cartes_possibles,couleur_atout,joueur).carte(0)).force(couleur_atout,couleur_demandee);
			}
			ramasseur_virtuel_egal_certain|=!main(cartes_certaines,couleur_demandee,joueur).estVide();
			ramasseur_deter&=ramasseur_virtuel_egal_certain;
		}
		return ramasseur_deter;
	}
	private boolean ramasseur_bat_ss_cpr_adv(Vector<Byte> equipe_a_battre,byte couleur_demandee,CarteBelote carte_forte,Vector<Vector<MainBelote>> cartes_possibles,Vector<Vector<MainBelote>> cartes_certaines)
	{
		byte couleur_atout=couleur_atout();
		boolean ramasseur_virtuel_egal_certain=false;
		boolean ramasseur_deter=true;
		byte max_force=carte_forte.force(couleur_atout,couleur_demandee,contrat);
		for(byte joueur:equipe_a_battre)
		{
			ramasseur_virtuel_egal_certain=!main(cartes_certaines,couleur_demandee,joueur).estVide();
			if(ramasseur_virtuel_egal_certain)
			{
				ramasseur_virtuel_egal_certain&=max_force>((CarteBelote)main(cartes_possibles,couleur_demandee,joueur).carte(0)).force(couleur_atout,couleur_demandee,contrat);
			}
			ramasseur_virtuel_egal_certain|=defausse(couleur_demandee, joueur, cartes_possibles);
			ramasseur_deter&=ramasseur_virtuel_egal_certain;
		}
		return ramasseur_deter;
	}
	private boolean defausse(byte couleur,byte joueur,Vector<Vector<MainBelote>> cartes_possibles)
	{
		return(contrat.force()==1)?main(cartes_possibles,carteAppelee.couleur(),joueur).estVide()&&main(cartes_possibles,couleur,joueur).estVide():main(cartes_possibles,couleur,joueur).estVide();
	}
	/**Est vrai si et seulement si on est sur que le joueur va couper le pli a la couleur demandee*/
	private boolean va_couper(byte couleur,byte joueur,Vector<Vector<MainBelote>> cartes_possibles,Vector<Vector<MainBelote>> cartes_certaines)
	{
		return main(cartes_possibles,couleur,joueur).estVide()&&!main(cartes_certaines,carteAppelee.couleur(),joueur).estVide();
	}
	private Carte coupe(Vector<MainBelote> repartition_jouables, Vector<Vector<MainBelote>> cartes_possibles, Vector<MainBelote> cartes_maitresses, Vector<Vector<MainBelote>> suites, byte adversaire_non_joue,byte couleur_atout, byte couleurDemandee,Vector<Byte> tours,CarteBelote carte_forte,Vector<MainBelote> cartes_rel_maitres,boolean maitre_jeu)
	{
		byte nombre_points_pli;
		if(((CarteBelote)main(repartition_jouables,couleur_atout).carte(0)).force(couleur_atout, couleurDemandee, contrat)<carte_forte.force(couleur_atout, couleurDemandee, contrat))
		{
			return main(repartition_jouables,couleur_atout).derniereCarte();
		}
		if(maitre_jeu)
		{
			return cartes_rel_maitres.lastElement().carte(0);
		}
		/*Deuxieme tour ou plus si surcoupe possible*/
		if(!tours.isEmpty()&&main(cartes_possibles,couleurDemandee,adversaire_non_joue).estVide()&&!main(cartes_possibles,couleur_atout,adversaire_non_joue).estVide())
		{
			if(suite(suites,couleur_atout).size()==1||main(cartes_maitresses,couleur_atout).estVide())
			{
				return main(repartition_jouables,couleur_atout).derniereCarte();
			}
			nombre_points_pli=0;
			for(Carte carte:pliEnCours)
			{
				nombre_points_pli+=((CarteBelote)carte).points(contrat, carteAppelee);
			}
			if(nombre_points_pli>=10&&!cartes_rel_maitres.isEmpty())
			{
				return cartes_rel_maitres.lastElement().carte(0);
			}
			return main(repartition_jouables,couleur_atout).derniereCarte();
		}
		if(suite(suites,couleur_atout).size()==1)
		{
			return main(repartition_jouables,couleur_atout).carte(0);
		}
		if(!main(cartes_maitresses,couleur_atout).estVide())
		{
			return suite(suites,couleur_atout).lastElement().carte(0);
		}
		if(((CarteBelote)main(repartition_jouables,couleur_atout).carte(0)).points(contrat,carteAppelee)>0)
		{
			return main(repartition_jouables,couleur_atout).carte(0);
		}
		return main(repartition_jouables,couleur_atout).derniereCarte();
	}
	/**Est vrai si et seulement si le partenaire du joueur qui va jouer domine l'adversaire n'ayant pas joue (uniquement si le partenaire est maitre temporairement du pli)*/
	private boolean partenaire_bat_adversaire_non_joue(byte adversaire_non_joue,byte couleur_demandee,byte couleur_atout,Vector<Vector<MainBelote>> cartes_possibles,CarteBelote carte_forte)
	{
		return main(cartes_possibles,couleur_demandee,adversaire_non_joue).estVide()||carte_forte.force(couleur_atout,couleur_demandee, contrat)>((CarteBelote)main(cartes_possibles,couleur_demandee,adversaire_non_joue).carte(0)).force(couleur_atout,couleur_demandee, contrat);
	}
	private boolean pas_atout(byte adversaire_non_joue,Vector<Vector<MainBelote>> cartes_possibles,byte couleur_atout)
	{
		return couleur_atout<1||main(cartes_possibles,couleur_atout,adversaire_non_joue).estVide();
	}
	private Carte sauve_qui_peut_figure(Vector<Vector<MainBelote>> cartes_possibles, Vector<MainBelote> suites,Vector<MainBelote> cartes_rel_maitres,byte adversaire_non_joue, byte couleurDemandee)
	{
		if(main(cartes_possibles,couleurDemandee,adversaire_non_joue).estVide())
		{
			return jeuFigureHauteDePlusFaibleSuite(suites);
		}
		if(((CarteBelote)main(cartes_possibles,couleurDemandee,adversaire_non_joue).carte(0)).points(contrat,carteAppelee)<1)
		{
			return jeuFigureHauteDePlusFaibleSuite(suites);
		}
		if(!cartes_rel_maitres.isEmpty())
		{
			if(cartes_rel_maitres.size()==1||((CarteBelote) cartes_rel_maitres.get(1).carte(0)).points(contrat,carteAppelee)<1)
				return suites.get(0).carte(0);
			return cartes_rel_maitres.get(1).carte(0);
		}
		return suites.lastElement().derniereCarte();
	}
	private boolean ne_peut_pas_avoir_figures(Vector<Vector<MainBelote>> cartes_possibles,byte adversaire_non_joue, byte couleurDemandee)
	{
		return main(cartes_possibles,couleurDemandee,adversaire_non_joue).estVide()||((CarteBelote)main(cartes_possibles,couleurDemandee,adversaire_non_joue).carte(0)).points(contrat,carteAppelee)==0;
	}
	private Vector<MainBelote> cartesRelativementMaitre(Vector<MainBelote> suites,Vector<Vector<MainBelote>> cartes_possibles,Vector<Byte> joueurs_non_joue, byte couleurDemandee,byte couleur_joueur,byte couleur_atout, Vector<Vector<MainBelote>> cartes_certaines,CarteBelote carte_forte)
	{
		byte max_valeur=0;
		Vector<MainBelote> suites_bis=new Vector<MainBelote>();
		if(couleur_joueur==couleur_atout&&couleurDemandee!=couleur_atout)
		{
			for(byte joueur:joueurs_non_joue)
			{
				if(!main(cartes_possibles,couleur_joueur,joueur).estVide()&&main(cartes_certaines,couleurDemandee,joueur).estVide())
				{
					max_valeur=(byte)Math.max(((CarteBelote)main(cartes_possibles,couleur_joueur,joueur).carte(0)).force(couleur_atout,couleurDemandee,contrat),max_valeur);
				}
			}
		}
		else
		{
			for(byte joueur:joueurs_non_joue)
			{
				if(!main(cartes_possibles,couleur_joueur,joueur).estVide())
				{
					max_valeur=(byte)Math.max(((CarteBelote)main(cartes_possibles,couleur_joueur,joueur).carte(0)).force(couleur_atout,couleurDemandee,contrat),max_valeur);
				}
			}
		}
		max_valeur=(byte)Math.max(max_valeur,carte_forte.force(couleur_atout,couleurDemandee,contrat));
		for(MainBelote suite:suites)
		{
			if(((CarteBelote) suite.carte(0)).force(couleur_atout,couleurDemandee,contrat)>max_valeur)
			{
				suites_bis.addElement(suite);
			}
			else
			{
				break;
			}
		}
		return suites_bis;
	}
	private Carte fin(String[] raison)
	{
		byte nombre_joueurs=getNombreDeJoueurs();
		byte numero=(byte)((pliEnCours.getEntameur()+pliEnCours.total())%nombre_joueurs);
		MainBelote main_joueur=(MainBelote)getDistribution().main(numero);
		byte couleur_atout=couleur_atout();
		Vector<MainBelote> repartition=(contrat.force()==1)?main_joueur.couleurs(carteAppelee.couleur(),contrat):main_joueur.couleurs(contrat);
		MainBelote cartes_jouables=cartes_jouables(repartition,numero);
		if(cartes_jouables.total()==1)
		{
			raison[0]="C est la seule carte a jouer";
			return cartes_jouables.carte(0);
		}
		MainBelote cartesJouees=cartesJouees();
		cartesJouees.ajouterCartes(pliEnCours.getCartes());
		Vector<MainBelote> repartitionCartesJouees=cartesJouees.couleurs(couleur_atout,contrat);
		Vector<Vector<MainBelote>> suites=new Vector<Vector<MainBelote>>();
		Vector<Pli> plisFaits=unionPlis();
		for(Main main:repartition)
		{
			suites.addElement(((MainBelote)main).eclater(repartitionCartesJouees,couleur_atout));
		}
		Vector<Vector<MainBelote>> cartes_possibles=cartesPossibles(repartitionCartesJouees,plisFaits,repartition, numero,couleur_atout);
		cartesCertaines(cartes_possibles);
		boolean strict_maitre_atout=contrat.force()==1?StrictMaitreAtout(cartes_possibles, numero, suites.get(couleur_atout-2),repartitionCartesJouees):true;
		Vector<Byte> couleurs_maitres=CouleursMaitres(suites, repartitionCartesJouees, cartes_possibles,numero);
		Vector<MainBelote> cartes_maitresses=cartesMaitresses(repartition,repartitionCartesJouees);
		Vector<Byte> couleurs_strictement_maitresses=StrictCouleursMaitres(suites,repartitionCartesJouees,cartes_possibles,numero);
		byte appele=(byte)((preneur+2)%4);
		byte partenaire=(byte)((numero+2)%4);
		boolean maitre_jeu=contrat.force()==1?strict_maitre_atout&&couleurs_maitres.size()==3:couleurs_maitres.size()==4;
		byte ramasseur_virtuel=pliEnCours.getRamasseurBelote(nombre_joueurs,contrat,couleur_atout);
		CarteBelote carte_forte=(CarteBelote) pliEnCours.carteDuJoueur(ramasseur_virtuel,nombre_joueurs,null);
		byte couleurDemandee=pliEnCours.couleurDemandee();
		boolean carte_maitresse;
		Vector<MainBelote> repartition_jouables=cartes_jouables.couleurs(couleur_atout,contrat);
		Vector<MainBelote> cartes_rel_maitres;
		if(contrat.force()<4&&couleurDemandee!=couleur_atout)
		{
			Pli dernier_pli;
			Vector<Byte> tours=tours(couleurDemandee, plisFaits),dernieres_defausses;
			if(cartes_jouables.carte(0).couleur()==couleurDemandee)
			{
				cartes_rel_maitres=cartesRelativementMaitreFin(suite(suites,couleurDemandee), couleurDemandee,  couleur_atout, carte_forte);
				if(((CarteBelote)main(repartition_jouables,couleurDemandee).carte(0)).force(couleur_atout,couleurDemandee,contrat)<carte_forte.force(couleur_atout,couleurDemandee,contrat)||carte_forte.couleur()==couleur_atout)
				{
					if(ramasseur_virtuel==partenaire)
					{
						if(main(repartition_jouables,couleurDemandee).nombreCartesPoints(carte_forte, contrat)>0)
						{
							if(!main(cartes_maitresses,couleurDemandee).estVide())
							{
								return carte_plus_petite_points(suite(suites,couleurDemandee));
							}
							return main(repartition_jouables,couleurDemandee).carte(0);
						}
					}
					return main(repartition_jouables,couleurDemandee).derniereCarte();
				}
				/*Maintenant on sait le joueur peut prendre la main*/
				if(((CarteBelote)main(repartition_jouables,couleurDemandee).carte(0)).points(contrat,carteAppelee)<1)
				{
					return main(repartition_jouables,couleurDemandee).carte(0);
				}
				/*Maintenant le joueur peut prendre la main avec une figure*/
				if(numero==preneur)
				{
					if(tours.isEmpty())
					{
						if(maitre_jeu)
						{
							return cartes_rel_maitres.lastElement().carte(0);
						}
						if(!main(cartes_maitresses,couleurDemandee).estVide())
						{
							if(suite(suites,couleurDemandee).size()==1||((CarteBelote)main(suites,couleurDemandee,1).carte(0)).points(contrat,carteAppelee)<1)
							{
								return main(repartition_jouables,couleurDemandee).carte(0);
							}
							if(carte_forte.points(contrat,carteAppelee)<1||partenaire==ramasseur_virtuel)
							{
								return jeuFigureHauteDePlusFaibleSuite(suite(suites,couleurDemandee));
							}
							return main(repartition_jouables,couleurDemandee).carte(0);
						}
						/*Le joueur n'a aucune cartes maitresses*/
						return main(repartition_jouables,couleurDemandee).carte(0);
					}
					/*C'est au moins le deuxieme tour*/
					dernier_pli=plisFaits.get(tours.lastElement());
					dernieres_defausses=dernier_pli.joueurs_defausses(nombre_joueurs, null,couleur_atout);
					if(!dernieres_defausses.isEmpty()&&tours.size()==1)
					{
						if(maitre_jeu)
						{
							return cartes_rel_maitres.lastElement().carte(0);
						}
						if(!main(cartes_maitresses,couleurDemandee).estVide())
						{
							if(suite(suites,couleurDemandee).size()==1||((CarteBelote)main(suites,couleurDemandee,1).carte(0)).points(contrat,carteAppelee)<1)
							{
								return main(repartition_jouables,couleurDemandee).carte(0);
							}
							if(carte_forte.points(contrat,carteAppelee)<1||partenaire==ramasseur_virtuel)
							{
								return jeuFigureHauteDePlusFaibleSuite(suite(suites,couleurDemandee));
							}
							return main(repartition_jouables,couleurDemandee).carte(0);
						}
						/*Le joueur n'a aucune cartes maitresses*/
						return main(repartition_jouables,couleurDemandee).carte(0);
					}
					/*Le pli d'avant n'est pas defausse ou c'est au moins le troisieme tour*/
					if(!main(cartes_maitresses,couleurDemandee).estVide())
					{
						return main(repartition_jouables,couleurDemandee).carte(0);
					}
					return cartes_rel_maitres.lastElement().carte(0);
				}
				/*Appele*/
				if(numero==appele)
				{
					if(tours.isEmpty())
					{
						if(maitre_jeu)
						{
							return cartes_rel_maitres.lastElement().carte(0);
						}
						/*Si l'appele joue avant le preneur*/
						if(cartes_rel_maitres.size()==1||((CarteBelote) cartes_rel_maitres.get(1).carte(0)).points(contrat,carteAppelee)<1)
						{
							return cartes_rel_maitres.get(0).carte(0);
						}
						return cartes_rel_maitres.get(1).carte(0);
					}
					/*Deuxieme tour pour un appele ne coupant pas la couleur demandee differente de l'atout*/
					/*Le pli d'avant n'est pas defausse ou c'est au moins le troisieme tour*/
					if(!main(cartes_maitresses,couleurDemandee).estVide())
					{
						return main(repartition_jouables,couleurDemandee).carte(0);
					}
					return cartes_rel_maitres.lastElement().carte(0);
				}
				/*Defenseur*/
				if(maitre_jeu)
				{
					return cartes_rel_maitres.lastElement().carte(0);
				}
				return jeuFigureHauteDePlusFaibleSuite(cartes_rel_maitres);
			}
			/*Le joueur ne peut pas fournir a la couleur demandee*/
			if(couleur_atout>0)
			{
				if(main(repartition_jouables,couleur_atout).total()==cartes_jouables.total())
				{
					if(((CarteBelote)main(repartition_jouables,couleur_atout).carte(0)).force(couleur_atout,couleurDemandee,contrat)<carte_forte.force(couleur_atout,couleurDemandee,contrat))
					{
						if(ramasseur_virtuel==partenaire)
						{
							if(suite(suites,couleur_atout).size()==1)
							{
								return main(repartition_jouables,couleur_atout).carte(0);
							}
						}
						return main(repartition_jouables,couleur_atout).derniereCarte();
					}
				}
				if(!main(repartition_jouables,couleur_atout).estVide())
				{
					cartes_rel_maitres=cartesRelativementMaitreFin(suite(suites,couleur_atout), couleurDemandee, couleur_atout, carte_forte);
					if(main(repartition_jouables,couleur_atout).total()==cartes_jouables.total())
					{/*Coupe obligatoire*/
						return coupeFin(repartition_jouables,cartes_maitresses,suites,couleur_atout,couleurDemandee,carte_forte,cartes_rel_maitres,maitre_jeu);
					}
					/*Coupe possible non obligatoire*/
					if(((CarteBelote)main(repartition_jouables,couleur_atout).carte(0)).force(couleur_atout, couleurDemandee, contrat)>carte_forte.force(couleur_atout, couleurDemandee, contrat))
					{
						if(maitre_jeu)
						{
							return cartes_rel_maitres.lastElement().carte(0);
						}
						if(suite(suites,couleur_atout).size()==1&&couleurs_maitres.size()==3&&((CarteBelote)main(repartition_jouables,couleur_atout).carte(0)).points(contrat,carteAppelee)>0)
						{
							return main(repartition_jouables,couleur_atout).carte(0);
						}
						return defausse_couleur_demandee_sur_partenaire(repartitionCartesJouees, repartition, cartes_maitresses, couleurDemandee, couleur_atout, couleurs_strictement_maitresses);
					}
					/*Si le joueur peut jouer ce qu'il veut et possede un atout*/
					if(ramasseur_virtuel==partenaire)
					{/*Si le partenaire a coupe le pli et le joueur ne peut pas surcouper le pli*/
						if(maitre_jeu)
						{
							return defausse_couleur_demandee_sur_partenaire(repartitionCartesJouees, repartition, cartes_maitresses, couleurDemandee, couleur_atout, couleurs_strictement_maitresses);
						}
						if(suite(suites,couleur_atout).size()==1&&couleurs_maitres.size()==3&&((CarteBelote)main(repartition_jouables,couleur_atout).carte(0)).points(contrat,carteAppelee)>0)
						{
							return main(repartition_jouables,couleur_atout).carte(0);
						}
						return defausse_couleur_demandee_sur_partenaire(repartitionCartesJouees, repartition, cartes_maitresses, couleurDemandee, couleur_atout, couleurs_strictement_maitresses);
					}
					/*Un adversaire a coupe ou sur coupe le pli et le joueur ne peut pas surcouper le pli*/
					/*Si le joueur peut couper sans obligation*/
					if(maitre_jeu&&((CarteBelote)main(repartition_jouables,couleur_atout).derniereCarte()).points(contrat,carteAppelee)<10)
					{
						carte_maitresse=true;
						for(byte couleur:couleurs_non_atouts())
						{
							if(!main(repartition,couleur).estVide())
							{
								carte_maitresse&=((CarteBelote)main(repartition,couleur).derniereCarte()).points(contrat, carteAppelee)>0;
							}
						}
						if(carte_maitresse)
						{
							return main(repartition_jouables,couleur_atout).derniereCarte();
						}
					}
					return defausse_couleur_demandee_sur_adversaire(repartitionCartesJouees, repartition, cartes_maitresses, couleurDemandee, couleur_atout, couleurs_strictement_maitresses);
				}
				/*Le joueur ne peut pas fournir a la couleur demandee et ne peut pas jouer un atout*/
				if(ramasseur_virtuel==partenaire)
				{
					return defausse_couleur_demandee_sur_partenaire(repartitionCartesJouees, repartition, cartes_maitresses, couleurDemandee, couleur_atout, couleurs_strictement_maitresses);
				}
				return defausse_couleur_demandee_sur_adversaire(repartitionCartesJouees, repartition, cartes_maitresses, couleurDemandee, couleur_atout, couleurs_strictement_maitresses);
			}
			if(ramasseur_virtuel==partenaire)
			{
				return defausse_couleur_demandee_sur_partenaire(repartitionCartesJouees, repartition, cartes_maitresses, couleurDemandee, couleur_atout, couleurs_strictement_maitresses);
			}
			return defausse_couleur_demandee_sur_adversaire(repartitionCartesJouees, repartition, cartes_maitresses, couleurDemandee, couleur_atout, couleurs_strictement_maitresses);
		}
		/*Fin if(contrat.force()<4&&couleurDemandee!=couleur_atout)*/
		/*Demande atout*/
		if(!main(repartition_jouables,couleurDemandee).estVide())
		{
			if(((CarteBelote)main(repartition_jouables,couleurDemandee).carte(0)).force(couleur_atout,couleurDemandee,contrat)<carte_forte.force(couleur_atout,couleurDemandee,contrat))
			{
				if(ramasseur_virtuel==partenaire)
				{
					if(suite(suites,couleurDemandee).size()==1)
					{
						return main(repartition_jouables,couleurDemandee).carte(0);
					}
					for(byte joueur:new byte[]{(byte)((numero+1)%nombre_joueurs),(byte)((numero+3)%nombre_joueurs)})
					{
						if(!main(cartes_possibles,couleurDemandee,joueur).estVide())
						{
							if(((CarteBelote)main(cartes_possibles,couleurDemandee,joueur).carte(0)).force(couleur_atout,couleurDemandee,contrat)>((CarteBelote)main(repartition_jouables,couleurDemandee).carte(0)).force(couleur_atout,couleurDemandee,contrat))
							{
								return main(repartition_jouables,couleurDemandee).carte(0);
							}
						}
					}
					return main(repartition_jouables,couleurDemandee).derniereCarte();
				}
				return main(repartition_jouables,couleurDemandee).derniereCarte();
			}
			cartes_rel_maitres=cartesRelativementMaitreFin(suite(suites,couleurDemandee), couleurDemandee,couleurDemandee, carte_forte);
			if(maitre_jeu)
			{
				return cartes_rel_maitres.lastElement().carte(0);
			}
			if(!main(cartes_maitresses,couleurDemandee).estVide())
			{
				return cartes_rel_maitres.lastElement().carte(0);
			}
			for(byte joueur:new byte[]{(byte)((numero+1)%nombre_joueurs),(byte)((numero+3)%nombre_joueurs)})
			{
				if(!main(cartes_possibles,couleurDemandee,joueur).estVide())
				{
					if(((CarteBelote)main(cartes_possibles,couleurDemandee,joueur).carte(0)).force(couleur_atout,couleurDemandee,contrat)>((CarteBelote)main(repartition_jouables,couleurDemandee).carte(0)).force(couleur_atout,couleurDemandee,contrat))
					{
						return main(repartition_jouables,couleurDemandee).carte(0);
					}
				}
			}
			return cartes_rel_maitres.lastElement().carte(0);
		}
		if(ramasseur_virtuel==partenaire)
		{
			return defausse_atout_sur_partenaire(repartitionCartesJouees, repartition, cartes_maitresses, couleur_atout, couleurs_strictement_maitresses);
		}
		return defausse_atout_sur_adversaire(repartitionCartesJouees, repartition, cartes_maitresses, couleur_atout, couleurs_strictement_maitresses);
	}
	private MainBelote cartes_jouables(Vector<MainBelote> repartition_main,byte numero)
	{
		/*Ensemble des cartes jouees sur ce pli*/
		MainBelote m=(MainBelote)pliEnCours.getCartes();
		MainBelote cartes_jouables=new MainBelote();
		if(pliEnCours.estVide())
		{
			//L'entame est libre a la belote
			for(MainBelote couleur:repartition_main)
			{
				cartes_jouables.ajouterCartes(couleur);
			}
			return cartes_jouables;
		}
		byte couleurDemandee=pliEnCours.couleurDemandee();
		byte couleurAtout=couleur_atout();
		byte ramasseur_virtuel=pliEnCours.getRamasseurBelote(getNombreDeJoueurs(), contrat, couleurAtout);
		CarteBelote carte_forte=(CarteBelote)pliEnCours.carteDuJoueur(ramasseur_virtuel,getNombreDeJoueurs(),null);
		if(contrat.force()==1)
		{
			int nombre_atouts_pli=main(m.couleurs(couleurAtout, contrat),couleurAtout).total();
			int nombreAtouts=main(repartition_main,couleurAtout).total();
			byte valeur_forte=carte_forte.force(couleurAtout,couleurDemandee,contrat);
			if(couleurAtout==couleurDemandee)
			{
				//Nombre d'atouts dans la main du joueur
				if(nombreAtouts==0)
				{
					for(MainBelote couleur:repartition_main)
					{
						cartes_jouables.ajouterCartes(couleur);
					}
					return cartes_jouables;
				}
				if(((CarteBelote)main(repartition_main,couleurAtout).derniereCarte()).force(couleurAtout,couleurDemandee,contrat)>valeur_forte)
				{
					cartes_jouables.ajouterCartes(main(repartition_main,couleurAtout));
					return cartes_jouables;
				}
				if(((CarteBelote)main(repartition_main,couleurAtout).carte(0)).force(couleurAtout,couleurDemandee,contrat)<valeur_forte)
				{
					cartes_jouables.ajouterCartes(main(repartition_main,couleurAtout));
					return cartes_jouables;
				}
				for(byte indice_carte=0;((CarteBelote)main(repartition_main,couleurAtout).carte(indice_carte)).force(couleurAtout,couleurDemandee,contrat)>valeur_forte;indice_carte++)
				{
					cartes_jouables.ajouter(main(repartition_main,couleurAtout).carte(indice_carte));
				}
				return cartes_jouables;
			}
			if(!main(repartition_main,couleurDemandee).estVide())
			{
				cartes_jouables.ajouterCartes(main(repartition_main,couleurDemandee));
				return cartes_jouables;
			}
			if(nombreAtouts==0)
			{
				for(MainBelote couleur:repartition_main)
				{
					cartes_jouables.ajouterCartes(couleur);
				}
				return cartes_jouables;
			}
			if(meme_equipe(ramasseur_virtuel, numero))
			{/*Le partenaire est maitre temporairement*/
				if(!sous_coupe_obligatoire_partenaire())
				{
					for(MainBelote couleur:repartition_main)
					{
						cartes_jouables.ajouterCartes(couleur);
					}
					return cartes_jouables;
				}
				if(sur_coupe_obligatoire_partenaire())
				{
					if(((CarteBelote)main(repartition_main,couleurAtout).derniereCarte()).force(couleurAtout,couleurDemandee,contrat)>valeur_forte)
					{
						cartes_jouables.ajouterCartes(main(repartition_main,couleurAtout));
						return cartes_jouables;
					}
					if(((CarteBelote)main(repartition_main,couleurAtout).carte(0)).force(couleurAtout,couleurDemandee,contrat)>valeur_forte)
					{
						for(byte indice_carte=0;((CarteBelote)main(repartition_main,couleurAtout).carte(indice_carte)).force(couleurAtout,couleurDemandee,contrat)>valeur_forte;indice_carte++)
						{
							cartes_jouables.ajouter(main(repartition_main,couleurAtout).carte(indice_carte));
						}
						return cartes_jouables;
					}
				}
				if(((CarteBelote)main(repartition_main,couleurAtout).carte(0)).force(couleurAtout,couleurDemandee,contrat)<valeur_forte)
				{
					cartes_jouables.ajouterCartes(main(repartition_main,couleurAtout));
					return cartes_jouables;
				}
				for(MainBelote couleur:repartition_main)
				{
					cartes_jouables.ajouterCartes(couleur);
				}
				return cartes_jouables;
			}
			if(nombre_atouts_pli==0)
			{/*Pli non coupe*/
				cartes_jouables.ajouterCartes(main(repartition_main,couleurAtout));
				return cartes_jouables;
			}
			/*Pli coupe par un adversaire*/
			if(((CarteBelote)main(repartition_main,couleurAtout).derniereCarte()).force(couleurAtout,couleurDemandee,contrat)>valeur_forte)
			{
				cartes_jouables.ajouterCartes(main(repartition_main,couleurAtout));
				return cartes_jouables;
			}
			if(((CarteBelote)main(repartition_main,couleurAtout).carte(0)).force(couleurAtout,couleurDemandee,contrat)>valeur_forte)
			{
				for(byte indice_carte=0;((CarteBelote)main(repartition_main,couleurAtout).carte(indice_carte)).force(couleurAtout,couleurDemandee,contrat)>valeur_forte;indice_carte++)
				{
					cartes_jouables.ajouter(main(repartition_main,couleurAtout).carte(indice_carte));
				}
				return cartes_jouables;
			}
			if(!sous_coupe_obligatoire_adversaire())
			{
				for(MainBelote couleur:repartition_main)
				{
					cartes_jouables.ajouterCartes(couleur);
				}
				return cartes_jouables;
			}
			cartes_jouables.ajouterCartes(main(repartition_main,couleurAtout));
			return cartes_jouables;
		}
		if(contrat.force()<4)
		{
			if(main(repartition_main,couleurDemandee).estVide())
			{
				for(MainBelote couleur:repartition_main)
				{
					cartes_jouables.ajouterCartes(couleur);
				}
				return cartes_jouables;
			}
			cartes_jouables.ajouterCartes(main(repartition_main,couleurDemandee));
			return cartes_jouables;
		}
		if(main(repartition_main,couleurDemandee).estVide())
		{
			for(MainBelote couleur:repartition_main)
			{
				cartes_jouables.ajouterCartes(couleur);
			}
			return cartes_jouables;
		}
		byte valeur_forte=carte_forte.force(couleurDemandee,couleurDemandee,contrat);
		if(((CarteBelote)main(repartition_main,couleurDemandee).derniereCarte()).force(couleurDemandee,couleurDemandee,contrat)>valeur_forte||((CarteBelote)main(repartition_main,couleurDemandee).carte(0)).force(couleurDemandee,couleurDemandee,contrat)<valeur_forte)
		{
			cartes_jouables.ajouterCartes(main(repartition_main,couleurDemandee));
			return cartes_jouables;
		}
		for(byte indice_carte=0;((CarteBelote)main(repartition_main,couleurDemandee).carte(indice_carte)).force(couleurDemandee,couleurDemandee,contrat)>valeur_forte;indice_carte++)
		{
			cartes_jouables.ajouter(main(repartition_main,couleurDemandee).carte(indice_carte));
		}
		return cartes_jouables;
	}
	public MainBelote cartesJouees()
	{
		MainBelote cartes_jouees=new MainBelote();
		for(Vector<Pli> plis_equipe:plis)
		{
			for(Pli pli:plis_equipe)
			{
				cartes_jouees.ajouterCartes(pli.getCartes());
			}
		}
		return cartes_jouees;
	}
	private Vector<Byte> CouleursMaitres(Vector<Vector<MainBelote>> suites,Vector<MainBelote> cartesJouees,Vector<Vector<MainBelote>> cartes_possibles, byte numero)
	{
		byte couleur_atout=couleur_atout();
		Vector<Byte> couleurs=new Vector<Byte>();
		if(contrat.force()>1)
		{
			if(contrat.force()<4)
			{
				for(byte couleur=2;couleur<6;couleur++)
				{
					if(main(cartesJouees,couleur).total()==8||suite(suites,couleur).isEmpty())
						couleurs.addElement(couleur);
					else
					{
						int max=0;/*max designe le nombre maximal de cartes probablement possedees par un joueur a une couleur donnee*/
						for(int joueur=0;joueur<suite(cartes_possibles,couleur).size();joueur++)
							if(joueur!=numero)
								max=Math.max(main(cartes_possibles,couleur,joueur).total(),max);
						boolean existe_carte_maitresse=true;
						CarteBelote c=(CarteBelote)main(suites,couleur,0).carte(0);
						for(byte valeur:CarteBelote.cartesCouleur)
						{
							if(new CarteBelote(valeur,couleur).force(couleur_atout,couleur,contrat)<=c.force(couleur_atout,couleur,contrat))
							{
								break;
							}
							if(!main(cartesJouees,couleur).contient(new CarteBelote(valeur,couleur))&&!main(suites,couleur,0).contient(new CarteBelote(valeur,couleur)))
							{
								existe_carte_maitresse=false;
								break;
							}
						}
						int maitres=(existe_carte_maitresse)?main(suites,couleur,0).total():0;
						if(maitres>=max||maitres>0&&suite(suites,couleur).size()==1)
							couleurs.addElement(couleur);
					}
				}
			}
			else
			{
				for(byte couleur=2;couleur<6;couleur++)
				{
					if(main(cartesJouees,couleur).total()==8||suite(suites,couleur).isEmpty())
						couleurs.addElement(couleur);
					else
					{
						int max=0;/*max designe le nombre maximal de cartes probablement possedees par un joueur a une couleur donnee*/
						for(int joueur=0;joueur<suite(cartes_possibles,couleur).size();joueur++)
							if(joueur!=numero)
								max=Math.max(main(cartes_possibles,couleur,joueur).total(),max);
						boolean existe_carte_maitresse=true;
						CarteBelote c=(CarteBelote)main(suites,couleur,0).carte(0);
						for(byte valeur:CarteBelote.cartesAtout)
						{
							if(new CarteBelote(valeur,couleur).force(couleur_atout,couleur,contrat)<=c.force(couleur_atout,couleur,contrat))
							{
								break;
							}
							if(!main(cartesJouees,couleur).contient(new CarteBelote(valeur,couleur))&&!main(suites,couleur,0).contient(new CarteBelote(valeur,couleur)))
							{
								existe_carte_maitresse=false;
								break;
							}
						}
						int maitres=(existe_carte_maitresse)?main(suites,couleur,0).total():0;
						if(maitres>=max||maitres>0&&suite(suites,couleur).size()==1)
							couleurs.addElement(couleur);
					}
				}
			}
		}
		else
		{
			for(byte couleur:couleurs_non_atouts())
			{
				if(main(cartesJouees,couleur).total()==8||suite(suites,couleur).isEmpty())
					couleurs.addElement(couleur);
				else
				{
					int max=0;/*max designe le nombre maximal de cartes probablement possedees par un joueur a une couleur donnee*/
					for(int joueur=0;joueur<suite(cartes_possibles,couleur).size();joueur++)
						if(joueur!=numero)
							max=Math.max(main(cartes_possibles,couleur,joueur).total(),max);
					boolean existe_carte_maitresse=true;
					CarteBelote c=(CarteBelote)main(suites,couleur,0).carte(0);
					for(byte valeur:CarteBelote.cartesCouleur)
					{
						if(new CarteBelote(valeur,couleur).force(couleur_atout,couleur,contrat)<=c.force(couleur_atout,couleur,contrat))
						{
							break;
						}
						if(!main(cartesJouees,couleur).contient(new CarteBelote(valeur,couleur))&&!main(suites,couleur,0).contient(new CarteBelote(valeur,couleur)))
						{
							existe_carte_maitresse=false;
							break;
						}
					}
					int maitres=(existe_carte_maitresse)?main(suites,couleur,0).total():0;
					if(maitres>=max||maitres>0&&suite(suites,couleur).size()==1)
						couleurs.addElement(couleur);
				}
			}
		}
		return couleurs;
	}
	private Vector<Byte> StrictCouleursMaitres(Vector<Vector<MainBelote>> suites,Vector<MainBelote> cartesJouees,Vector<Vector<MainBelote>> cartes_possibles, byte numero)
	{
		byte couleur_atout=couleur_atout();
		Vector<Byte> couleurs=new Vector<Byte>();
		if(contrat.force()>1)
		{
			if(contrat.force()<4)
			{
				for(byte couleur=2;couleur<6;couleur++)
				{
					if(main(cartesJouees,couleur).total()==8)
						couleurs.addElement(couleur);
					else if(!suite(suites,couleur).isEmpty())
					{
						int max=0;/*max designe le nombre maximal de cartes probablement possedees par un joueur a une couleur donnee*/
						for(int joueur=0;joueur<suite(cartes_possibles,couleur).size();joueur++)
							if(joueur!=numero)
								max=Math.max(main(cartes_possibles,couleur,joueur).total(),max);
						boolean existe_carte_maitresse=true;
						CarteBelote c=(CarteBelote)main(suites,couleur,0).carte(0);
						for(byte valeur:CarteBelote.cartesCouleur)
						{
							if(new CarteBelote(valeur,couleur).force(couleur_atout,couleur,contrat)<=c.force(couleur_atout,couleur,contrat))
							{
								break;
							}
							if(!main(cartesJouees,couleur).contient(new CarteBelote(valeur,couleur))&&!main(suites,couleur,0).contient(new CarteBelote(valeur,couleur)))
							{
								existe_carte_maitresse=false;
								break;
							}
						}
						int maitres=(existe_carte_maitresse)?main(suites,couleur,0).total():0;
						if(maitres>=max||maitres>0&&suite(suites,couleur).size()==1)
							couleurs.addElement(couleur);
					}
				}
			}
			else
			{
				for(byte couleur=2;couleur<6;couleur++)
				{
					if(main(cartesJouees,couleur).total()==8)
						couleurs.addElement(couleur);
					else if(!suite(suites,couleur).isEmpty())
					{
						int max=0;/*max designe le nombre maximal de cartes probablement possedees par un joueur a une couleur donnee*/
						for(int joueur=0;joueur<suite(cartes_possibles,couleur).size();joueur++)
							if(joueur!=numero)
								max=Math.max(main(cartes_possibles,couleur,joueur).total(),max);
						boolean existe_carte_maitresse=true;
						CarteBelote c=(CarteBelote)main(suites,couleur,0).carte(0);
						for(byte valeur:CarteBelote.cartesAtout)
						{
							if(new CarteBelote(valeur,couleur).force(couleur_atout,couleur,contrat)<=c.force(couleur_atout,couleur,contrat))
							{
								break;
							}
							if(!main(cartesJouees,couleur).contient(new CarteBelote(valeur,couleur))&&!main(suites,couleur,0).contient(new CarteBelote(valeur,couleur)))
							{
								existe_carte_maitresse=false;
								break;
							}
						}
						int maitres=(existe_carte_maitresse)?main(suites,couleur,0).total():0;
						if(maitres>=max||maitres>0&&suite(suites,couleur).size()==1)
							couleurs.addElement(couleur);
					}
				}
			}
		}
		else
		{
			for(byte couleur:couleurs_non_atouts())
			{
				if(main(cartesJouees,couleur).total()==8)
					couleurs.addElement(couleur);
				else if(!suite(suites,couleur).isEmpty())
				{
					int max=0;/*max designe le nombre maximal de cartes probablement possedees par un joueur a une couleur donnee*/
					for(int joueur=0;joueur<suite(cartes_possibles,couleur).size();joueur++)
						if(joueur!=numero)
							max=Math.max(main(cartes_possibles,couleur,joueur).total(),max);
					boolean existe_carte_maitresse=true;
					CarteBelote c=(CarteBelote)main(suites,couleur,0).carte(0);
					for(byte valeur:CarteBelote.cartesCouleur)
					{
						if(new CarteBelote(valeur,couleur).force(couleur_atout,couleur,contrat)<=c.force(couleur_atout,couleur,contrat))
						{
							break;
						}
						if(!main(cartesJouees,couleur).contient(new CarteBelote(valeur,couleur))&&!main(suites,couleur,0).contient(new CarteBelote(valeur,couleur)))
						{
							existe_carte_maitresse=false;
							break;
						}
					}
					int maitres=(existe_carte_maitresse)?main(suites,couleur,0).total():0;
					if(maitres>=max||maitres>0&&suite(suites,couleur).size()==1)
						couleurs.addElement(couleur);
				}
			}
		}
		return couleurs;
	}
	/**Retourne l'ensemble des cartes maitresses dans leur propre couleur.
	 * Pour recuperer la couleur n° i il faut ecrire cartesMaitresses.get(i-2)*/
	private Vector<MainBelote> cartesMaitresses(Vector<MainBelote> couleurs,Vector<MainBelote> cartesJouees)
	{
		byte couleur_atout=couleur_atout();
		Vector<MainBelote> nb=new Vector<MainBelote>();
		for (byte couleur=2;couleur<6;couleur++) {
			Main cartes=main(couleurs,couleur);
			MainBelote suite=new MainBelote();
			MainBelote union=new MainBelote();
			union.ajouterCartes(main(cartesJouees,couleur));//C'est la reunion des cartes jouees dans le jeu et de celles du joueur
			for(int j=0;j<cartes.total();j++)
			{
				CarteBelote c=(CarteBelote)cartes.carte(j);
				if(union.estVide()||((CarteBelote)union.derniereCarte()).force(couleur_atout,couleur,contrat)>c.force(couleur_atout,couleur,contrat))
				{
					union.ajouter(c);
				}
				else
				{
					int k=0;
					for(;((CarteBelote)union.carte(k)).force(couleur_atout,couleur,contrat)>c.force(couleur_atout,couleur,contrat);k++);
					union.ajouter(c,k);
				}
			}
			for(int j=0;j<union.total();j++)
			{
				if(j+((CarteBelote)union.carte(j)).force(couleur_atout,couleur,contrat)==8)
				{
					if(cartes.contient(union.carte(j)))
					{
						suite.ajouter(union.carte(j));
					}
				}
				else
				{
					break;
				}
			}
			nb.addElement(suite);
		}
		return nb;
	}
	/**Utilise pour dire si le joueur de carte est strictement maitre a l'atout (c'est-a-dire s'il peut ramasser tous les atouts en ayant la main) uniquement pour un jeu ou une couleur domine les autres*/
	private boolean StrictMaitreAtout(Vector<Vector<MainBelote>> cartes_possibles, byte numero,Vector<MainBelote> suites,Vector<MainBelote> cartesJouees)
	{
		byte couleur_atout=carteAppelee.couleur();
		int max=0;/*max designe le nombre maximal de cartes probablement possedees par un joueur*/
		for(int joueur=0;joueur<suite(cartes_possibles,couleur_atout).size();joueur++)//La taille de cartes_possibles correspond au nombre de joueurs
			if(joueur!=numero)
				max=Math.max(main(cartes_possibles,couleur_atout,joueur).total(),max);
		/*Fin for int joueur=0;joueur<cartes_possibles.get(1).size();joueur++
		 *(Fin boucle sur le calcul de la valeur maximale possible des atouts*/
		if(max==0)/*S'il est impossible que les autres joueurs aient de l'atout*/
			return true;
		if(main(cartesJouees,couleur_atout).total()==8)
			return true;
		if(suites.isEmpty())
			return false;
		boolean existeAtoutMaitre=true;
		CarteBelote atout_petit_suite_haute=(CarteBelote)suites.get(0).carte(0);
		for(byte valeur:CarteBelote.cartesAtout)
		{
			if(new CarteBelote(valeur,couleur_atout).force(couleur_atout,couleur_atout)<=atout_petit_suite_haute.force(couleur_atout,couleur_atout))
			{
				break;
			}
			if(!main(cartesJouees,couleur_atout).contient(new CarteBelote(valeur,couleur_atout))&&!suites.get(0).contient(new CarteBelote(valeur,couleur_atout)))
			{
				existeAtoutMaitre=false;
				break;
			}
		}
		return existeAtoutMaitre&&suites.get(0).total()>=max;
	}
	private Carte coupeFin(Vector<MainBelote> repartition_jouables, Vector<MainBelote> cartes_maitresses, Vector<Vector<MainBelote>> suites, byte couleur_atout, byte couleurDemandee,CarteBelote carte_forte,Vector<MainBelote> cartes_rel_maitres,boolean maitre_jeu)
	{
		if(((CarteBelote)main(repartition_jouables,couleur_atout).carte(0)).force(couleur_atout, couleurDemandee, contrat)<carte_forte.force(couleur_atout, couleurDemandee, contrat))
		{
			return main(repartition_jouables,couleur_atout).derniereCarte();
		}
		if(maitre_jeu)
		{
			return cartes_rel_maitres.lastElement().carte(0);
		}
		/*Deuxieme tour ou plus si surcoupe possible*/
		if(suite(suites,couleur_atout).size()==1)
		{
			return main(repartition_jouables,couleur_atout).carte(0);
		}
		if(!main(cartes_maitresses,couleur_atout).estVide())
		{
			return cartes_rel_maitres.lastElement().carte(0);
		}
		if(((CarteBelote)main(repartition_jouables,couleur_atout).carte(0)).points(contrat,carteAppelee)>0)
		{
			return main(repartition_jouables,couleur_atout).carte(0);
		}
		return main(repartition_jouables,couleur_atout).derniereCarte();
	}
	private Carte jeuFigureHauteDePlusFaibleSuite(Vector<MainBelote> suites)
	{
		if(suites.size()==1)
			return suites.get(0).carte(0);
		return carte_plus_petite_points(suites);
	}
	private Carte carte_plus_petite_points(Vector<MainBelote> suites)
	{
		byte indice_suite_joue=-1;
		for(MainBelote suite:suites)
		{
			if(((CarteBelote)suite.carte(0)).points(contrat, carteAppelee)>0)
			{
				indice_suite_joue++;
			}
			else
			{
				break;
			}
		}
		return suites.get(indice_suite_joue).carte(0);
	}
	private Vector<MainBelote> cartesRelativementMaitreFin(Vector<MainBelote> suites,byte couleurDemandee, byte couleur_atout,CarteBelote carte_forte)
	{
		byte max_valeur=carte_forte.force(couleur_atout,couleurDemandee,contrat);
		Vector<MainBelote> suites_bis=new Vector<MainBelote>();
		for(MainBelote suite:suites)
		{
			if(((CarteBelote) suite.carte(0)).force(couleur_atout,couleurDemandee,contrat)>max_valeur)
			{
				suites_bis.addElement(suite);
			}
			else
			{
				break;
			}
		}
		return suites_bis;
	}
	private Carte defausse_atout_sur_adversaire(Vector<MainBelote> repartitionCartesJouees,Vector<MainBelote> repartition,Vector<MainBelote> cartes_maitresses,byte couleur_atout,Vector<Byte> couleurs_strictement_maitresses)
	{
		if(couleur_atout<1||main(repartitionCartesJouees,couleur_atout).total()>5)
		{
			if(couleurs_strictement_maitresses.size()==3)
			{
				Vector<Byte> couleurs=new Vector<Byte>();
				for(byte couleur:couleurs_strictement_maitresses)
				{
					if(!main(repartition,couleur).estVide())
					{
						couleurs.addElement(couleur);
					}
				}
				return jeu_petite_defausse_maitre(cartes_maitresses, repartition, couleurs, couleur_atout);
			}
			Vector<Byte> couleurs=new Vector<Byte>();
			for(byte couleur=2;couleur<6;couleur++)
			{
				if(!main(repartition,couleur).estVide()&&((CarteBelote)main(repartition,couleur).carte(0)).points(contrat,carteAppelee)==0)
				{
					couleurs.addElement(couleur);
				}
			}
			if(!couleurs.isEmpty())
			{
				return jouer_petite_carte_defausse(couleurs,repartition,repartitionCartesJouees,couleur_atout);
			}
			for(byte couleur=2;couleur<6;couleur++)
			{
				if(!main(repartition,couleur).estVide())
				{
					couleurs.addElement(couleur);
				}
			}
			return jeu_petite_carte_couleur_figure(couleurs, repartition, repartitionCartesJouees);
		}
		/*Moins de 6 atouts sont joues*/
		Vector<Byte> couleurs=new Vector<Byte>();
		for(byte couleur=2;couleur<6;couleur++)
		{
			if(!main(repartition,couleur).estVide()&&((CarteBelote)main(repartition,couleur).carte(0)).points(contrat,carteAppelee)==0)
			{
				couleurs.addElement(couleur);
			}
		}
		if(!couleurs.isEmpty())
		{
			return jouer_petite_carte_defausse(couleurs,repartition,repartitionCartesJouees,couleur_atout);
		}
		for(byte couleur=2;couleur<6;couleur++)
		{
			if(!main(repartition,couleur).estVide())
			{
				couleurs.addElement(couleur);
			}
		}
		return jeu_petite_carte_couleur_figure(couleurs, repartition, repartitionCartesJouees);
	}
	private Carte defausse_atout_sur_partenaire(Vector<MainBelote> repartitionCartesJouees,Vector<MainBelote> repartition,Vector<MainBelote> cartes_maitresses,byte couleur_atout,Vector<Byte> couleurs_strictement_maitresses)
	{
		boolean carte_maitresse;
		if(couleur_atout<1||main(repartitionCartesJouees,couleur_atout).total()>5)
		{
			if(couleurs_strictement_maitresses.size()==3)
			{
				carte_maitresse=true;
				for(byte couleur:couleurs_strictement_maitresses)
				{
					carte_maitresse&=main(cartes_maitresses,couleur).total()==main(repartition,couleur).total();
				}
				if(carte_maitresse)
				{
					Vector<Byte> couleurs_figures=new Vector<Byte>();
					for(byte couleur:couleurs_strictement_maitresses)
					{
						if(!main(repartition,couleur).estVide()&&((CarteBelote)main(repartition,couleur).carte(0)).points(contrat,carteAppelee)>0)
						{
							couleurs_figures.addElement(couleur);
						}
					}
					if(!couleurs_figures.isEmpty())
					{
						return jeu_grande_carte_defausse_maitre(couleurs_figures, repartition, couleur_atout);
					}
				}
				Vector<Byte> couleurs=new Vector<Byte>();
				for(byte couleur:couleurs_strictement_maitresses)
				{
					if(!main(repartition,couleur).estVide())
					{
						couleurs.addElement(couleur);
					}
				}
				if(!couleurs.isEmpty())
				{
					return jeu_petite_defausse_maitre(cartes_maitresses, repartition, couleurs, couleur_atout);
				}
			}
			/*Il peut exister une couleur pour se defausser non strictement maitresse*/
			Vector<Byte> couleurs=new Vector<Byte>();
			for(byte couleur=2;couleur<6;couleur++)
			{
				if(!main(cartes_maitresses,couleur).estVide())
				{
					couleurs.addElement(couleur);
				}
			}
			if(!couleurs.isEmpty())
			{
				return jeu_petite_defausse_maitre(cartes_maitresses, repartition, couleurs, couleur_atout);
			}
			for(byte couleur=2;couleur<6;couleur++)
			{
				if(!main(repartition,couleur).estVide()&&((CarteBelote)main(repartition,couleur).carte(0)).points(contrat,carteAppelee)>0)
				{
					couleurs.addElement(couleur);
				}
			}
			if(!couleurs.isEmpty())
			{
				return sauver_figures_defausse(couleurs,repartition,repartitionCartesJouees, couleur_atout);
			}
			for(byte couleur=2;couleur<6;couleur++)
			{
				if(!main(repartition,couleur).estVide())
				{
					couleurs.addElement(couleur);
				}
			}
			return jouer_petite_carte_defausse(couleurs,repartition,repartitionCartesJouees,couleur_atout);
		}
		/*Moins de 20 atouts sont joues ou moins de 13 cartes de la couleur demandee sont jouees*/
		if(couleurs_strictement_maitresses.size()==3)
		{
			carte_maitresse=true;
			for(byte couleur:couleurs_strictement_maitresses)
			{
				carte_maitresse&=main(cartes_maitresses,couleur).total()==main(repartition,couleur).total();
			}
			if(carte_maitresse)
			{
				Vector<Byte> couleurs_figures=new Vector<Byte>();
				for(byte couleur:couleurs_strictement_maitresses)
				{
					if(!main(repartition,couleur).estVide()&&((CarteBelote)main(repartition,couleur).carte(0)).points(contrat,carteAppelee)>0)
					{
						couleurs_figures.addElement(couleur);
					}
				}
				if(!couleurs_figures.isEmpty())
				{
					return jeu_grande_carte_defausse_maitre(couleurs_figures, repartition, couleur_atout);
				}
			}
		}
		Vector<Byte> couleurs=new Vector<Byte>();
		for(byte couleur=2;couleur<6;couleur++)
		{
			if(!main(repartition,couleur).estVide()&&((CarteBelote)main(repartition,couleur).carte(0)).points(contrat,carteAppelee)>0)
			{
				couleurs.addElement(couleur);
			}
		}
		if(!couleurs.isEmpty())
		{
			return sauver_figures_defausse(couleurs,repartition,repartitionCartesJouees, couleur_atout);
		}
		for(byte couleur=2;couleur<6;couleur++)
		{
			if(!main(repartition,couleur).estVide())
			{
				couleurs.addElement(couleur);
			}
		}
		return jouer_petite_carte_defausse(couleurs,repartition,repartitionCartesJouees,couleur_atout);
	}
	private Carte defausse_couleur_demandee_sur_adversaire(Vector<MainBelote> repartitionCartesJouees,Vector<MainBelote> repartition,Vector<MainBelote> cartes_maitresses,byte couleurDemandee,byte couleur_atout,Vector<Byte> couleurs_strictement_maitresses)
	{
		if((couleur_atout<1||main(repartitionCartesJouees,couleur_atout).total()>6)&&main(repartitionCartesJouees,couleurDemandee).total()>6)
		{
			if(couleur_atout>0&&couleurs_strictement_maitresses.size()==2||couleur_atout<1&&couleurs_strictement_maitresses.size()==3)
			{
				Vector<Byte> couleurs=new Vector<Byte>();
				for(byte couleur:couleurs_strictement_maitresses)
				{
					if(!main(repartition,couleur).estVide()&&couleur_atout!=couleur)
					{
						couleurs.addElement(couleur);
					}
				}
				if(!couleurs.isEmpty())
				{
					return jeu_petite_defausse_maitre(cartes_maitresses, repartition, couleurs, couleur_atout);
				}
			}
			Vector<Byte> couleurs=new Vector<Byte>();
			for(byte couleur:couleurs_non_atouts())
			{
				if(!main(repartition,couleur).estVide()&&((CarteBelote)main(repartition,couleur).carte(0)).points(contrat,carteAppelee)==0)
				{
					couleurs.addElement(couleur);
				}
			}
			if(!couleurs.isEmpty())
			{
				return jouer_petite_carte_defausse(couleurs,repartition,repartitionCartesJouees,couleur_atout);
			}
			for(byte couleur=2;couleur<6;couleur++)
			{
				if(!main(repartition,couleur).estVide())
				{
					couleurs.addElement(couleur);
				}
			}
			return jeu_petite_carte_couleur_figure(couleurs, repartition, repartitionCartesJouees);
		}
		/*Moins de 7 atouts sont joues ou moins de 7 cartes de la couleur demandee sont jouees*/
		Vector<Byte> couleurs=new Vector<Byte>();
		for(byte couleur:couleurs_non_atouts())
		{
			if(!main(repartition,couleur).estVide()&&((CarteBelote)main(repartition,couleur).carte(0)).points(contrat,carteAppelee)==0)
			{
				couleurs.addElement(couleur);
			}
		}
		if(!couleurs.isEmpty())
		{
			return jouer_petite_carte_defausse(couleurs,repartition,repartitionCartesJouees,couleur_atout);
		}
		for(byte couleur:couleurs_non_atouts())
		{
			if(!main(repartition,couleur).estVide())
			{
				couleurs.addElement(couleur);
			}
		}
		if(!couleurs.isEmpty())
		{
			return jeu_petite_carte_couleur_figure(couleurs, repartition, repartitionCartesJouees);
		}
		for(byte couleur=2;couleur<6;couleur++)
		{
			if(!main(repartition,couleur).estVide())
			{
				couleurs.addElement(couleur);
			}
		}
		return jeu_petite_carte_couleur_figure(couleurs, repartition, repartitionCartesJouees);
	}
	private Carte defausse_couleur_demandee_sur_partenaire(Vector<MainBelote> repartitionCartesJouees,Vector<MainBelote> repartition,Vector<MainBelote> cartes_maitresses,byte couleurDemandee,byte couleur_atout,Vector<Byte> couleurs_strictement_maitresses)
	{
		boolean carte_maitresse;
		if((couleur_atout<1||main(repartitionCartesJouees,couleur_atout).total()>6)&&main(repartitionCartesJouees,couleurDemandee).total()>6)
		{
			if(couleur_atout>0&&couleurs_strictement_maitresses.size()==2||couleur_atout<1&&couleurs_strictement_maitresses.size()==3)
			{
				carte_maitresse=true;
				for(byte couleur:couleurs_strictement_maitresses)
				{
					carte_maitresse&=main(cartes_maitresses,couleur).total()==main(repartition,couleur).total()&&couleur_atout!=couleur;
				}
				if(carte_maitresse)
				{
					Vector<Byte> couleurs_figures=new Vector<Byte>();
					for(byte couleur:couleurs_strictement_maitresses)
					{
						if(!main(repartition,couleur).estVide()&&((CarteBelote)main(repartition,couleur).carte(0)).points(contrat,carteAppelee)>0)
						{
							couleurs_figures.addElement(couleur);
						}
					}
					if(!couleurs_figures.isEmpty())
					{
						return jeu_grande_carte_defausse_maitre(couleurs_figures, repartition, couleur_atout);
					}
				}
				Vector<Byte> couleurs=new Vector<Byte>();
				for(byte couleur:couleurs_strictement_maitresses)
				{
					if(!main(repartition,couleur).estVide()&&couleur_atout!=couleur)
					{
						couleurs.addElement(couleur);
					}
				}
				if(!couleurs.isEmpty())
				{
					return jeu_petite_defausse_maitre(cartes_maitresses, repartition, couleurs, couleur_atout);
				}
				for(byte couleur:couleurs_strictement_maitresses)
				{
					if(!main(repartition,couleur).estVide())
					{
						couleurs.addElement(couleur);
					}
				}
				if(!couleurs.isEmpty())
				{
					return main(repartition,couleur_atout).derniereCarte();
				}
			}
			/*Il peut exister une couleur non strictement maitresse pour se defausser*/
			Vector<Byte> couleurs=new Vector<Byte>();
			for(byte couleur:couleurs_non_atouts())
			{
				if(!main(cartes_maitresses,couleur).estVide())
				{
					couleurs.addElement(couleur);
				}
			}
			if(!couleurs.isEmpty())
			{
				return jeu_petite_defausse_maitre(cartes_maitresses, repartition, couleurs, couleur_atout);
			}
			for(byte couleur:couleurs_non_atouts())
			{
				if(!main(repartition,couleur).estVide()&&((CarteBelote)main(repartition,couleur).carte(0)).points(contrat,carteAppelee)>0)
				{
					couleurs.addElement(couleur);
				}
			}
			if(!couleurs.isEmpty())
			{
				return sauver_figures_defausse(couleurs,repartition,repartitionCartesJouees, couleur_atout);
			}
			for(byte couleur:couleurs_non_atouts())
			{
				if(!main(repartition,couleur).estVide())
				{
					couleurs.addElement(couleur);
				}
			}
			if(!couleurs.isEmpty())
			{
				return jouer_petite_carte_defausse(couleurs,repartition,repartitionCartesJouees,couleur_atout);
			}
			return main(repartition,couleur_atout).derniereCarte();
		}
		/*Moins de 20 atouts sont joues ou moins de 13 cartes de la couleur demandee sont jouees*/
		if(couleur_atout>0&&couleurs_strictement_maitresses.size()==2||couleur_atout<1&&couleurs_strictement_maitresses.size()==3)
		{
			carte_maitresse=true;
			for(byte couleur:couleurs_strictement_maitresses)
			{
				carte_maitresse&=main(cartes_maitresses,couleur).total()==main(repartition,couleur).total()&&couleur_atout!=couleur;
			}
			if(carte_maitresse)
			{
				Vector<Byte> couleurs_figures=new Vector<Byte>();
				for(byte couleur:couleurs_strictement_maitresses)
				{
					if(!main(repartition,couleur).estVide()&&couleur_atout!=couleur&&((CarteBelote)main(repartition,couleur).carte(0)).points(contrat,carteAppelee)>0)
					{
						couleurs_figures.addElement(couleur);
					}
				}
				if(!couleurs_figures.isEmpty())
				{
					return jeu_grande_carte_defausse_maitre(couleurs_figures, repartition, couleur_atout);
				}
			}
		}
		Vector<Byte> couleurs=new Vector<Byte>();
		for(byte couleur:couleurs_non_atouts())
		{
			if(!main(repartition,couleur).estVide()&&((CarteBelote)main(repartition,couleur).carte(0)).points(contrat,carteAppelee)>0)
			{
				couleurs.addElement(couleur);
			}
		}
		if(!couleurs.isEmpty())
		{
			return sauver_figures_defausse(couleurs,repartition,repartitionCartesJouees, couleur_atout);
		}
		for(byte couleur:couleurs_non_atouts())
		{
			if(!main(repartition,couleur).estVide())
			{
				couleurs.addElement(couleur);
			}
		}
		if(!couleurs.isEmpty())
		{
			return jouer_petite_carte_defausse(couleurs,repartition,repartitionCartesJouees,couleur_atout);
		}
		return main(repartition,couleur_atout).derniereCarte();
	}
	private Vector<Byte> couleurs_non_atouts()
	{
		Vector<Byte> couleurs_non_atouts=new Vector<Byte>();
		if(contrat.force()==1)
		{
			for(byte couleur=2;couleur<6;couleur++)
			{
				if(couleur!=carteAppelee.couleur())
				{
					couleurs_non_atouts.addElement(couleur);
				}
			}
			return couleurs_non_atouts;
		}
		return couleurs();
	}
	private Vector<Byte> couleurs()
	{
		Vector<Byte> couleurs=new Vector<Byte>();
		for(byte couleur=2;couleur<6;couleur++)
		{
			couleurs.addElement(couleur);
		}
		return couleurs;
	}
	private Carte sauver_figures_defausse(Vector<Byte> couleurs_figures,Vector<MainBelote> repartition,Vector<MainBelote> repartitionCartesJouees,byte couleur_atout)
	{
		for(byte indice_couleur=1;indice_couleur<couleurs_figures.size();indice_couleur++)
		{
			byte couleur1=couleurs_figures.get(0);
			byte couleur2=couleurs_figures.get(indice_couleur);
			if(main(repartition,couleur1).nombreCartesPoints(carteAppelee, contrat)<1&&main(repartition,couleur2).nombreCartesPoints(carteAppelee, contrat)>0)
			{
				couleurs_figures.setElementAt(couleurs_figures.set(0,couleur2),indice_couleur);
			}
			else if(main(repartition,couleur1).total()>main(repartition,couleur2).total())
			{
				couleurs_figures.setElementAt(couleurs_figures.set(0,couleur2),indice_couleur);
			}
			else if(main(repartition,couleur1).total()==main(repartition,couleur2).total())
			{
				if(main(repartitionCartesJouees,couleur1).total()<main(repartitionCartesJouees,couleur2).total())
				{
					couleurs_figures.setElementAt(couleurs_figures.set(0,couleur2),indice_couleur);
				}
				else if(main(repartitionCartesJouees,couleur1).total()==main(repartitionCartesJouees,couleur2).total())
				{
					if(((CarteBelote)main(repartition,couleur1).carte(0)).force(couleur_atout,couleur1, contrat)<((CarteBelote)main(repartition,couleur2).carte(0)).force(couleur_atout,couleur2, contrat))
					{
						couleurs_figures.setElementAt(couleurs_figures.set(0,couleur2),indice_couleur);
					}
				}
			}
		}
		return main(repartition,couleurs_figures.get(0)).carte(0);
	}
	private Carte jeu_petite_carte_couleur_figure(Vector<Byte> couleurs, Vector<MainBelote> repartition,Vector<MainBelote> repartitionCartesJouees)
	{
		for(byte indice_couleur=1;indice_couleur<couleurs.size();indice_couleur++)
		{
			byte couleur1=couleurs.get(0);
			byte couleur2=couleurs.get(indice_couleur);
			if(((CarteBelote)main(repartition,couleur1).derniereCarte()).force(couleur_atout(),couleur1, contrat)>((CarteBelote)main(repartition,couleur2).derniereCarte()).force(couleur_atout(),couleur2, contrat))
			{
				couleurs.setElementAt(couleurs.set(0,couleur2),indice_couleur);
			}
			else if(((CarteBelote)main(repartition,couleur1).derniereCarte()).force(couleur_atout(),couleur1, contrat)==((CarteBelote)main(repartition,couleur2).derniereCarte()).force(couleur_atout(),couleur2, contrat))
			{
				if(main(repartition,couleur1).total()>main(repartition,couleur2).total())
				{
					couleurs.setElementAt(couleurs.set(0,couleur2),indice_couleur);
				}
				else if(main(repartition,couleur1).total()==main(repartition,couleur2).total())
				{
					if(main(repartition,couleur1).nombreCartesPoints(carteAppelee,contrat)>main(repartition,couleur2).nombreCartesPoints(carteAppelee, contrat))
					{
						couleurs.setElementAt(couleurs.set(0,couleur2),indice_couleur);
					}
					else if(main(repartition,couleur1).nombreCartesPoints(carteAppelee,contrat)==main(repartition,couleur2).nombreCartesPoints(carteAppelee, contrat))
					{
						if(main(repartitionCartesJouees,couleur1).total()>main(repartitionCartesJouees,couleur2).total())
						{
							couleurs.setElementAt(couleurs.set(0,couleur2),indice_couleur);
						}
					}
				}
			}
		}
		return main(repartition,couleurs.get(0)).derniereCarte();
	}
	private Carte jeu_grande_carte_defausse_maitre(Vector<Byte> couleurs_figures, Vector<MainBelote> repartition,byte couleur_atout)
	{
		for(byte indice_couleur=1;indice_couleur<couleurs_figures.size();indice_couleur++)
		{
			byte couleur1=couleurs_figures.get(0);
			byte couleur2=couleurs_figures.get(indice_couleur);
			if(main(repartition,couleur1).total()<main(repartition,couleur2).total())
			{
				couleurs_figures.setElementAt(couleurs_figures.set(0,couleur2),indice_couleur);
			}
			else if(main(repartition,couleur1).total()==main(repartition,couleur2).total())
			{
				boolean egal=true;
				for(byte indice_carte=0;indice_carte<main(repartition,couleur1).total();indice_carte++)
				{
					if(((CarteBelote)main(repartition,couleur1).carte(indice_carte)).force(couleur_atout,couleur1, contrat)>((CarteBelote)main(repartition,couleur2).carte(indice_carte)).force(couleur_atout,couleur2, contrat))
					{
						egal=false;
						break;
					}
					else if(((CarteBelote)main(repartition,couleur1).carte(indice_carte)).force(couleur_atout,couleur1, contrat)<((CarteBelote)main(repartition,couleur2).carte(indice_carte)).force(couleur_atout,couleur2, contrat))
					{
						couleurs_figures.setElementAt(couleurs_figures.set(0,couleur2),indice_couleur);
						egal=false;
						break;
					}
				}
				if(egal&&main(repartition,couleur1).nombreCartesPoints(carteAppelee,contrat)<main(repartition,couleur2).nombreCartesPoints(carteAppelee,contrat))
				{
					couleurs_figures.setElementAt(couleurs_figures.set(0,couleur2),indice_couleur);
				}
			}
		}
		return main(repartition,couleurs_figures.get(0)).carte(0);
	}
	private Carte jouer_petite_carte_defausse(Vector<Byte> couleurs, Vector<MainBelote> repartition,Vector<MainBelote> repartitionCartesJouees,byte couleur_atout)
	{
		for(byte indice_couleur=1;indice_couleur<couleurs.size();indice_couleur++)
		{
			byte couleur1=couleurs.get(0);
			byte couleur2=couleurs.get(indice_couleur);
			if(main(repartition,couleur1).total()>main(repartition,couleur2).total())
			{
				couleurs.setElementAt(couleurs.set(0,couleur2),indice_couleur);
			}
			else if(main(repartition,couleur1).total()==main(repartition,couleur2).total())
			{
				if(main(repartitionCartesJouees,couleur1).total()>main(repartitionCartesJouees,couleur2).total())
				{
					couleurs.setElementAt(couleurs.set(0,couleur2),indice_couleur);
				}
				else if(main(repartitionCartesJouees,couleur1).total()==main(repartitionCartesJouees,couleur2).total())
				{
					for(byte indice_carte=0;indice_carte<main(repartitionCartesJouees,couleur1).total();indice_carte++)
					{
						if(((CarteBelote)main(repartition,couleur1).carte(indice_carte)).force(couleur_atout,couleur1, contrat)>((CarteBelote)main(repartition,couleur2).carte(indice_carte)).force(couleur_atout,couleur2, contrat))
						{
							couleurs.setElementAt(couleurs.set(0,couleur2),indice_couleur);
							break;
						}
						else if(((CarteBelote)main(repartition,couleur1).carte(indice_carte)).force(couleur_atout,couleur1, contrat)<((CarteBelote)main(repartition,couleur2).carte(indice_carte)).force(couleur_atout,couleur2, contrat))
						{
							break;
						}
					}
				}
			}
		}
		return main(repartition,couleurs.get(0)).derniereCarte();
	}
	private Carte jeu_petite_defausse_maitre(Vector<MainBelote> cartes_maitresses,Vector<MainBelote> repartition,Vector<Byte> couleurs, byte couleur_atout)
	{
		for(byte indice_couleur=1;indice_couleur<couleurs.size();indice_couleur++)
		{
			byte couleur1=couleurs.get(0);
			byte couleur2=couleurs.get(indice_couleur);
			if(main(repartition,couleur1).total()<main(repartition,couleur2).total())
			{
				couleurs.setElementAt(couleurs.set(0,couleur2),indice_couleur);
			}
			else if(main(repartition,couleur1).total()==main(repartition,couleur2).total())
			{
				if(main(cartes_maitresses,couleur1).total()>main(cartes_maitresses,couleur2).total())
				{
					couleurs.setElementAt(couleurs.set(0,couleur2),indice_couleur);
				}
				else if(main(cartes_maitresses,couleur1).total()==main(cartes_maitresses,couleur2).total())
				{
					boolean egal=true;
					for(byte indice_carte=0;indice_carte<main(repartition,couleur1).total();indice_carte++)
					{
						if(((CarteBelote)main(repartition,couleur1).carte(indice_carte)).force(couleur_atout,couleur1,contrat)<((CarteBelote)main(repartition,couleur2).carte(indice_carte)).force(couleur_atout,couleur2, contrat))
						{
							egal=false;
							break;
						}
						else if(((CarteBelote)main(repartition,couleur1).carte(indice_carte)).force(couleur_atout,couleur1,contrat)>((CarteBelote)main(repartition,couleur2).carte(indice_carte)).force(couleur_atout,couleur2, contrat))
						{
							couleurs.setElementAt(couleurs.set(0,couleur2),indice_couleur);
							egal=false;
							break;
						}
					}
					if(egal&&main(repartition,couleur1).nombreCartesPoints(carteAppelee,contrat)>main(repartition,couleur2).nombreCartesPoints(carteAppelee,contrat))
					{
						couleurs.setElementAt(couleurs.set(0,couleur2),indice_couleur);
					}
				}
			}
		}
		return main(repartition,couleurs.get(0)).derniereCarte();
	}
	public Vector<Vector<MainBelote>> cartesPossibles(Vector<MainBelote> repartitionCartesJouees,Vector<Pli> plisFaits, Vector<MainBelote> repartition,byte numero,byte couleur_atout)
	{
		Vector<Vector<MainBelote>> m=new Vector<Vector<MainBelote>>();
		for(byte couleur=2;couleur<6;couleur++)//On fait une boucle sur les couleurs autres que l'atout
		{
			if(couleur_atout!=couleur&&contrat.force()<4)
			{
				m.addElement(cartesPossibles(couleur,main(repartitionCartesJouees,couleur),plisFaits,main(repartition,couleur),numero));
			}
			else
			{
				m.addElement(atoutsPossibles(couleur,main(repartitionCartesJouees,couleur),plisFaits,main(repartition,couleur),numero));
			}
		}
		return m;
	}
	public Vector<Vector<MainBelote>> cartesCertaines(Vector<Vector<MainBelote>> cartes_possibles)
	{
		byte couleur_atout=couleur_atout();
		Vector<Byte> joueurs_repartition_connue=new Vector<Byte>(),joueurs_repartition_connue_2=new Vector<Byte>(),joueurs_repartition_connue_memo=new Vector<Byte>();
		Vector<Byte> joueurs_repartition_inconnue=new Vector<Byte>();
		Vector<Vector<MainBelote>> cartes_certaines=new Vector<Vector<MainBelote>>();
		byte nombre_joueurs=getNombreDeJoueurs();
		int nombre_d_apparition_carte=0;/*Indique le nombre de mains pour les cartes possibles ou apparait la carte*/
		for(byte couleur=2;couleur<6;couleur++)
		{
			cartes_certaines.addElement(new Vector<MainBelote>());
			for(byte joueur=0;joueur<nombre_joueurs;joueur++)
			{
				suite(cartes_certaines,couleur).addElement(new MainBelote());
			}
		}
		int nombre_cartes_possibles_joueur=0;
		for(byte joueur=0;joueur<nombre_joueurs;joueur++)
		{
			nombre_cartes_possibles_joueur=0;
			for(byte couleur=2;couleur<6;couleur++)
			{
				nombre_cartes_possibles_joueur+=main(cartes_possibles,couleur,joueur).total();
			}
			if(nombre_cartes_possibles_joueur==getDistribution().main(joueur).total())
			{/*L'ensemble des cartes d'un joueur reellement possedees est inclus dans l'ensemble des cartes probablement possedees par ce joueur*/
				for(byte couleur=2;couleur<6;couleur++)
				{
					main(cartes_certaines,couleur,joueur).ajouterCartes(main(cartes_possibles,couleur,joueur));
				}
				joueurs_repartition_connue.addElement(joueur);
				joueurs_repartition_connue_memo.addElement(joueur);
			}
		}
		/*La condition !joueurs_repartition_connue.isEmpty() est vraie car la methode est appelee pour un point de vue particulier de joueur*/
		for(;!joueurs_repartition_connue.isEmpty();)
		{/*Tant qu'on arrive a deduire la repartition exacte des joueurs on boucle sur l'ensemble des joueurs dont la
		repartition vient juste d'etre connue pour eliminer les cartes impossibles d'etre possedees par les joueurs*/
			for(byte joueur:joueurs_repartition_connue)
			{
				for(byte joueur2=0;joueur2<nombre_joueurs;joueur2++)
				{
					if(!joueurs_repartition_connue_memo.contains(joueur2))
					{
						for(byte couleur=2;couleur<6;couleur++)
						{
							main(cartes_possibles,couleur,joueur2).supprimerCartes(main(cartes_certaines,couleur,joueur));
						}
					}
					nombre_cartes_possibles_joueur=0;
					for(byte couleur=2;couleur<6;couleur++)
					{
						nombre_cartes_possibles_joueur+=main(cartes_possibles,couleur,joueur2).total();
					}
					if(nombre_cartes_possibles_joueur==getDistribution().main(joueur2).total()&&!joueurs_repartition_connue_memo.contains(joueur2))
					{
						for(byte couleur=2;couleur<6;couleur++)
						{
							main(cartes_certaines,couleur,joueur2).supprimerCartes();
							main(cartes_certaines,couleur,joueur2).ajouterCartes(main(cartes_possibles,couleur,joueur2));
						}
						joueurs_repartition_connue_2.addElement(joueur2);
						joueurs_repartition_connue_memo.addElement(joueur2);
					}
				}
			}
			for(byte joueur=0;joueur<nombre_joueurs;joueur++)
			{
				if(!joueurs_repartition_connue_memo.contains(joueur))
				{
					joueurs_repartition_inconnue.addElement(joueur);
				}
			}
			for(byte joueur:joueurs_repartition_inconnue)
			{
				for(byte couleur=2;couleur<6;couleur++)
				{
					for(Carte carte:main(cartes_possibles,couleur,joueur))
					{
						nombre_d_apparition_carte=0;
						for(byte joueur2=0;joueur2<nombre_joueurs;joueur2++)
						{
							if(main(cartes_possibles,couleur,joueur2).contient(carte))
							{
								nombre_d_apparition_carte++;
							}
						}
						if(nombre_d_apparition_carte==1&&!main(cartes_certaines,couleur,joueur).contient(carte))
						{/*Si une carte n'apparait que dans une main ou il est possible qu'elle apparaisse alors elle ne peut etre que dans cette main*/
							main(cartes_certaines,couleur,joueur).ajouter(carte);
						}
					}
				}
				nombre_cartes_possibles_joueur=0;
				for(byte couleur=2;couleur<6;couleur++)
				{
					nombre_cartes_possibles_joueur+=main(cartes_certaines,couleur,joueur).total();
				}
				if(nombre_cartes_possibles_joueur==getDistribution().main(joueur).total()&&!joueurs_repartition_connue_memo.contains(joueur))
				{
					for(byte couleur=2;couleur<6;couleur++)
					{
						main(cartes_possibles,couleur,joueur).supprimerCartes();
						main(cartes_possibles,couleur,joueur).ajouterCartes(main(cartes_certaines,couleur,joueur));
						if(contrat.force()==1)
						{
							if(couleur!=couleur_atout)
							{
								main(cartes_possibles,couleur,joueur).setOrdre(Ordre.Couleur);
							}
						}
						else if(contrat.force()==3)
						{
							main(cartes_possibles,couleur,joueur).setOrdre(Ordre.Couleur);
						}
						main(cartes_possibles,couleur,joueur).trier();
					}
					joueurs_repartition_connue_memo.addElement(joueur);
					joueurs_repartition_connue_2.addElement(joueur);
				}
			}
			joueurs_repartition_inconnue.removeAllElements();
			joueurs_repartition_connue.removeAllElements();
			joueurs_repartition_connue.addAll(joueurs_repartition_connue_2);
			joueurs_repartition_connue_2.removeAllElements();
		}
		for(byte joueur=0;joueur<nombre_joueurs;joueur++)
		{
			if(!joueurs_repartition_connue_memo.contains(joueur))
			{
				joueurs_repartition_inconnue.addElement(joueur);
			}
		}
		for(byte joueur:joueurs_repartition_inconnue)
		{
			for(byte couleur=2;couleur<6;couleur++)
			{
				if(contrat.force()==1)
				{
					if(couleur!=couleur_atout)
					{
						main(cartes_certaines,couleur,joueur).setOrdre(Ordre.Couleur);
					}
				}
				else if(contrat.force()==3)
				{
					main(cartes_certaines,couleur,joueur).setOrdre(Ordre.Couleur);
				}
				main(cartes_certaines,couleur,joueur).trier();
			}
		}
		return cartes_certaines;
	}
	/**Retourne l'ensemble des atouts probablement possedes par les autres joueurs
	 * @param numero*/
	private Vector<MainBelote> atoutsPossibles(byte couleur_atout,MainBelote atoutsJoues,Vector<Pli> plisFaits,MainBelote atoutsJoueur, byte numero)
	{
		Vector<MainBelote> m=new Vector<MainBelote>();
		byte joueur=0;
		boolean defausse=false;
		byte nombre_joueurs=getNombreDeJoueurs();
		for(;joueur<numero;joueur++)
		{
			m.addElement(new MainBelote());
			for(byte valeur:CarteBelote.cartesAtout)
			{
				if(!atoutsJoues.contient(new CarteBelote(valeur,couleur_atout))&&!atoutsJoueur.contient(new CarteBelote(valeur,couleur_atout))&&(!getDistribution().derniereMain().carte(0).equals(new CarteBelote(valeur,couleur_atout))||joueur==preneur))
				{
					m.lastElement().ajouter(new CarteBelote(valeur,couleur_atout));
				}
			}
			if(contrat.force()==1)
			{
				defausse=false;
				for(byte couleur=2;couleur<6;couleur++)
				{
					defausse|=defausseBelote(couleur,joueur,plisFaits);
				}
				if(defausse)
				{//Les joueurs se defaussant sur atout ou couleur demandee ne peuvent pas avoir de l'atout
					m.get(joueur).supprimerCartes();
				}
			}
			else
			{
				if(neFournitPas(couleur_atout, joueur, plisFaits))
				{
					m.get(joueur).supprimerCartes();
				}
			}
		}
		m.addElement(new MainBelote());
		m.lastElement().ajouterCartes(atoutsJoueur);
		for(joueur++;joueur<nombre_joueurs;joueur++)
		{
			m.addElement(new MainBelote());
			for(byte valeur:CarteBelote.cartesAtout)
			{
				if(!atoutsJoues.contient(new CarteBelote(valeur,couleur_atout))&&!atoutsJoueur.contient(new CarteBelote(valeur,couleur_atout))&&(!getDistribution().derniereMain().carte(0).equals(new CarteBelote(valeur,couleur_atout))||joueur==preneur))
				{
					m.lastElement().ajouter(new CarteBelote(valeur,couleur_atout));
				}
			}
			if(contrat.force()==1)
			{
				defausse=false;
				for(byte couleur=2;couleur<6;couleur++)
				{
					defausse|=defausseBelote(couleur,joueur,plisFaits);
				}
				if(defausse)
				{//Les joueurs se defaussant sur atout ou couleur demandee ne peuvent pas avoir de l'atout
					m.get(joueur).supprimerCartes();
				}
			}
			else
			{
				if(neFournitPas(couleur_atout, joueur, plisFaits))
				{
					m.get(joueur).supprimerCartes();
				}
			}
		}
		Vector<Byte> numerosPlisAvec1Atouts=new Vector<Byte>();
		if(contrat.force()==1)
		{
			for(byte indice_pli=0;indice_pli<plisFaits.size();indice_pli++)
			{
				if(plisFaits.get(indice_pli).getCartes().tailleCouleur(couleur_atout)>0)
				{
					numerosPlisAvec1Atouts.addElement(indice_pli);
				}
			}
			/*Si cet indice ne vaut pas -1 alors il vaut le joueur ayant annonce belote_rebelote*/
			byte indice_belote_rebelote=-1;
			for(joueur=0;joueur<nombre_joueurs;joueur++)
			{
				if(annonces.get(joueur).indexOf(new Annonce(Annonce.belote_rebelote))>-1)
				{
					indice_belote_rebelote=joueur;
					break;
				}
			}
			if(indice_belote_rebelote>-1)
			{
				byte indice_roi_dame=-1;
				for(byte indice_carte=0;indice_carte<atoutsJoues.total();indice_carte++)
				{
					if(atoutsJoues.carte(indice_carte).valeur()>12)
					{
						indice_roi_dame=indice_carte;
						break;
					}
				}
				if(indice_roi_dame>-1)
				{
					byte valeur_autre_carte=atoutsJoues.carte(indice_roi_dame).valeur()==13?(byte)14:(byte)13;
					if(indice_roi_dame==atoutsJoues.total()-1||atoutsJoues.carte(indice_roi_dame+1).valeur()<12)
					{/*Cela implique que l'autre carte de la belote rebelotte n'a pas ete jouee (Une seule des deux cartes d'atout Roi ou Dame est jouee)*/
						for(joueur=0;joueur<nombre_joueurs;joueur++)
						{/*Pour que l'autre carte de la belote rebelote soit possedee par un joueur il faut et il suffit qu'il ait annonce belote_rebelote*/
							if(joueur!=indice_belote_rebelote&&m.get(joueur).contient(new CarteBelote(valeur_autre_carte,couleur_atout)))
							{
								m.get(joueur).jouer(new CarteBelote(valeur_autre_carte,couleur_atout));
							}
						}
					}
				}
			}
		}
		else
		{
			for(byte indice_pli=0;indice_pli<plisFaits.size();indice_pli++)
			{
				if(plisFaits.get(indice_pli).couleurDemandee()==couleur_atout&&plisFaits.get(indice_pli).getCartes().tailleCouleur(couleur_atout)>0)
				{
					numerosPlisAvec1Atouts.addElement(indice_pli);
				}
			}
		}
		boolean sur_coupe_oblig_part=sur_coupe_obligatoire_partenaire();
		boolean sous_coupe_oblig_part=sous_coupe_obligatoire_partenaire();
		boolean sous_coupe_oblig_adv=sous_coupe_obligatoire_adversaire();
		for(byte numero_pli:numerosPlisAvec1Atouts)
		{
			Pli pli=plisFaits.get(numero_pli);
			byte couleur_demande=pli.couleurDemandee();
			if(couleur_demande==couleur_atout)
			{
				for(int indice_carte=0;indice_carte<pli.total();indice_carte++)
				{
					/*On parcourt les atouts joues dans le pli pour eliminer des possibilites de possession d'atouts par obligation de monter
					 * quand on peut*/
					CarteBelote maxAtout=(CarteBelote) pli.carte(indice_carte);
					if(maxAtout.couleur()==couleur_atout)
					{
						boolean max=true;//max represente l'assertion tout atout n°j est superieur a ceux qui sont deja joues
						for(int indice_carte_jouee_avant=0;indice_carte_jouee_avant<indice_carte;indice_carte_jouee_avant++)
						{
							CarteBelote c=(CarteBelote)pli.carte(indice_carte_jouee_avant);
							if(c.couleur()==couleur_atout)
							{
								max&=c.force(couleur_atout,couleur_atout,contrat)<((CarteBelote)pli.carte(indice_carte)).force(couleur_atout,couleur_atout,contrat);
								if(maxAtout.equals(pli.carte(indice_carte))||maxAtout.force(couleur_atout,couleur_atout,contrat)<c.force(couleur_atout,couleur_atout,contrat))
								{
									maxAtout=c;
								}
							}
						}
						if(!max)
						{/*Si max est faux alors maxAtout ne vaut pas null*/
							joueur=pli.joueurAyantJoue(pli.carte(indice_carte),nombre_joueurs,null);
							for(int indice_carte_possible=0;indice_carte_possible<m.get(joueur).total();)
							{
								if(((CarteBelote)m.get(joueur).carte(indice_carte_possible)).force(couleur_atout,couleur_atout,contrat)>maxAtout.force(couleur_atout,couleur_atout,contrat))
								{
									m.get(joueur).jouer(m.get(joueur).carte(indice_carte_possible));
								}
								else
								{
									indice_carte_possible++;
								}
							}
						}
					}
				}
			}
			else
			{/*La couleur demandee n'est pas atout*/
				byte ramasseur_pli=ramasseur(plisFaits,numero_pli);
				for(int indice_carte=0;indice_carte<pli.total();indice_carte++)
				{
					/*On parcourt les atouts joues dans le pli pour eliminer des possibilites de possession d'atouts par obligation de monter
					 * quand on peut*/
					CarteBelote maxAtout=(CarteBelote) pli.carte(indice_carte);
					if(maxAtout.couleur()==couleur_atout)
					{
						boolean max=true;//max represente l'assertion tout atout n°j est superieur a ceux qui sont deja joues
						for(int indice_carte_jouee_avant=0;indice_carte_jouee_avant<indice_carte;indice_carte_jouee_avant++)
						{
							CarteBelote c=(CarteBelote)pli.carte(indice_carte_jouee_avant);
							if(c.couleur()==couleur_atout)
							{
								max&=c.force(couleur_atout,couleur_demande,contrat)<((CarteBelote)pli.carte(indice_carte)).force(couleur_atout,couleur_demande,contrat);
								if(maxAtout.equals(pli.carte(indice_carte))||maxAtout.force(couleur_atout,couleur_demande,contrat)<c.force(couleur_atout,couleur_demande,contrat))
								{
									maxAtout=c;
								}
							}
						}
						if(!max)
						{/*Si max est faux alors maxAtout ne vaut pas null*/
							joueur=pli.joueurAyantJoue(pli.carte(indice_carte),nombre_joueurs,null);
							if(!meme_equipe(ramasseur_pli,joueur)||sur_coupe_oblig_part)
							{/*Le joueur joueur ne fait pas equipe avec le ramasseur d'un pli ou la couleur demandee n'est pas atout donc il ne peut pas monter dessus*/
								for(int indice_carte_possible=0;indice_carte_possible<m.get(joueur).total();)
								{
									if(((CarteBelote)m.get(joueur).carte(indice_carte_possible)).force(couleur_atout,couleur_demande,contrat)>maxAtout.force(couleur_atout,couleur_demande,contrat))
									{
										m.get(joueur).jouer(m.get(joueur).carte(indice_carte_possible));
									}
									else
									{
										indice_carte_possible++;
									}
								}
							}
						}
					}
					else if(maxAtout.couleur()!=couleur_demande)
					{/*Defausse sur couleur demandee*/
						boolean max=true;//max represente l'assertion tout atout n°j est superieur a ceux qui sont deja joues
						for(int indice_carte_jouee_avant=0;indice_carte_jouee_avant<indice_carte;indice_carte_jouee_avant++)
						{
							CarteBelote c=(CarteBelote)pli.carte(indice_carte_jouee_avant);
							if(c.couleur()==couleur_atout)
							{
								max&=c.force(couleur_atout,couleur_demande,contrat)<((CarteBelote)pli.carte(indice_carte)).force(couleur_atout,couleur_demande,contrat);
								if(maxAtout.equals(pli.carte(indice_carte))||maxAtout.force(couleur_atout,couleur_demande,contrat)<c.force(couleur_atout,couleur_demande,contrat))
								{
									maxAtout=c;
								}
							}
						}
						if(!max)
						{/*Si max est faux alors maxAtout ne vaut pas null*/
							joueur=pli.joueurAyantJoue(pli.carte(indice_carte),nombre_joueurs,null);
							if(!meme_equipe(ramasseur_pli,joueur)||sur_coupe_oblig_part)
							{/*Le joueur ne fait pas equipe avec le ramasseur d'un pli ou la couleur demandee n'est pas atout donc il ne peut pas monter dessus*/
								if(!meme_equipe(ramasseur_pli,joueur))
								{
									if(!sous_coupe_oblig_adv)
									{
										for(int indice_carte_possible=0;indice_carte_possible<m.get(joueur).total();)
										{
											if(((CarteBelote)m.get(joueur).carte(indice_carte_possible)).force(couleur_atout,couleur_demande,contrat)>maxAtout.force(couleur_atout,couleur_demande,contrat))
											{
												m.get(joueur).jouer(m.get(joueur).carte(indice_carte_possible));
											}
											else
											{
												indice_carte_possible++;
											}
										}
									}
									else
									{
										m.get(joueur).supprimerCartes();
									}
								}
								else if(sous_coupe_oblig_part)
								{
									m.get(joueur).supprimerCartes();
								}
								else
								{
									for(int indice_carte_possible=0;indice_carte_possible<m.get(joueur).total();)
									{
										if(((CarteBelote)m.get(joueur).carte(indice_carte_possible)).force(couleur_atout,couleur_demande,contrat)>maxAtout.force(couleur_atout,couleur_demande,contrat))
										{
											m.get(joueur).jouer(m.get(joueur).carte(indice_carte_possible));
										}
										else
										{
											indice_carte_possible++;
										}
									}
								}
							}
						}
					}
				}
			}
		}
		/*Fin for sur les surcoupe et sous-coupes*/
		if((contrat.force()==1||pliEnCours.couleurDemandee()==couleur_atout)&&pliEnCours.getCartes().tailleCouleur(couleur_atout)>0)
		{
			byte couleur_demande=pliEnCours.couleurDemandee();
			if(couleur_demande==couleur_atout)
			{
				for(int indice_carte=0;indice_carte<pliEnCours.total();indice_carte++)
				{
					/*On parcourt les atouts joues dans le pli pour eliminer des possibilites de possession d'atouts par obligation de monter
					 * quand on peut*/
					CarteBelote maxAtout=(CarteBelote) pliEnCours.carte(indice_carte);
					if(maxAtout.couleur()==couleur_atout)
					{
						boolean max=true;//max represente l'assertion tout atout n°j est superieur a ceux qui sont deja joues
						for(int indice_carte_jouee_avant=0;indice_carte_jouee_avant<indice_carte;indice_carte_jouee_avant++)
						{
							CarteBelote c=(CarteBelote)pliEnCours.carte(indice_carte_jouee_avant);
							if(c.couleur()==couleur_atout)
							{
								max&=c.force(couleur_atout,couleur_atout,contrat)<((CarteBelote)pliEnCours.carte(indice_carte)).force(couleur_atout,couleur_atout,contrat);
								if(maxAtout.equals(pliEnCours.carte(indice_carte))||maxAtout.force(couleur_atout,couleur_atout,contrat)<c.force(couleur_atout,couleur_atout,contrat))
								{
									maxAtout=c;
								}
							}
						}
						if(!max)
						{/*Si max est faux alors maxAtout ne vaut pas null*/
							joueur=pliEnCours.joueurAyantJoue(pliEnCours.carte(indice_carte),nombre_joueurs,null);
							for(int indice_carte_possible=0;indice_carte_possible<m.get(joueur).total();)
							{
								if(((CarteBelote)m.get(joueur).carte(indice_carte_possible)).force(couleur_atout,couleur_atout,contrat)>maxAtout.force(couleur_atout,couleur_atout,contrat))
								{
									m.get(joueur).jouer(m.get(joueur).carte(indice_carte_possible));
								}
								else
								{
									indice_carte_possible++;
								}
							}
						}
					}
				}
			}
			else
			{/*La couleur demandee n'est pas atout*/
				byte ramasseur_pli=pliEnCours.getRamasseurBelote(nombre_joueurs, contrat, couleur_atout);
				for(int indice_carte=0;indice_carte<pliEnCours.total();indice_carte++)
				{
					/*On parcourt les atouts joues dans le pli pour eliminer des possibilites de possession d'atouts par obligation de monter
					 * quand on peut*/
					CarteBelote maxAtout=(CarteBelote) pliEnCours.carte(indice_carte);
					if(maxAtout.couleur()==couleur_atout)
					{
						boolean max=true;//max represente l'assertion tout atout n°j est superieur a ceux qui sont deja joues
						for(int indice_carte_jouee_avant=0;indice_carte_jouee_avant<indice_carte;indice_carte_jouee_avant++)
						{
							CarteBelote c=(CarteBelote)pliEnCours.carte(indice_carte_jouee_avant);
							if(c.couleur()==couleur_atout)
							{
								max&=c.force(couleur_atout,couleur_demande,contrat)<((CarteBelote)pliEnCours.carte(indice_carte)).force(couleur_atout,couleur_demande,contrat);
								if(maxAtout.equals(pliEnCours.carte(indice_carte))||maxAtout.force(couleur_atout,couleur_demande,contrat)<c.force(couleur_atout,couleur_demande,contrat))
								{
									maxAtout=c;
								}
							}
						}
						if(!max)
						{/*Si max est faux alors maxAtout ne vaut pas null*/
							joueur=pliEnCours.joueurAyantJoue(pliEnCours.carte(indice_carte),nombre_joueurs,null);
							if(!meme_equipe(ramasseur_pli,joueur)||sur_coupe_oblig_part)
							{/*Le joueur joueur ne fait pas equipe avec le ramasseur d'un pli ou la couleur demandee n'est pas atout donc il ne peut pas monter dessus*/
								for(int indice_carte_possible=0;indice_carte_possible<m.get(joueur).total();)
								{
									if(((CarteBelote)m.get(joueur).carte(indice_carte_possible)).force(couleur_atout,couleur_demande,contrat)>maxAtout.force(couleur_atout,couleur_demande,contrat))
									{
										m.get(joueur).jouer(m.get(joueur).carte(indice_carte_possible));
									}
									else
									{
										indice_carte_possible++;
									}
								}
							}
						}
					}
					else if(maxAtout.couleur()!=couleur_demande)
					{/*Defausse sur couleur demandee*/
						boolean max=true;//max represente l'assertion tout atout n°j est superieur a ceux qui sont deja joues
						for(int indice_carte_jouee_avant=0;indice_carte_jouee_avant<indice_carte;indice_carte_jouee_avant++)
						{
							CarteBelote c=(CarteBelote)pliEnCours.carte(indice_carte_jouee_avant);
							if(c.couleur()==couleur_atout)
							{
								max&=c.force(couleur_atout,couleur_demande,contrat)<((CarteBelote)pliEnCours.carte(indice_carte)).force(couleur_atout,couleur_demande,contrat);
								if(maxAtout.equals(pliEnCours.carte(indice_carte))||maxAtout.force(couleur_atout,couleur_demande,contrat)<c.force(couleur_atout,couleur_demande,contrat))
								{
									maxAtout=c;
								}
							}
						}
						if(!max)
						{/*Si max est faux alors maxAtout ne vaut pas null*/
							joueur=pliEnCours.joueurAyantJoue(pliEnCours.carte(indice_carte),nombre_joueurs,null);
							if(!meme_equipe(ramasseur_pli,joueur)||sur_coupe_oblig_part)
							{/*Le joueur ne fait pas equipe avec le ramasseur d'un pli ou la couleur demandee n'est pas atout donc il ne peut pas monter dessus*/
								if(!meme_equipe(ramasseur_pli,joueur))
								{
									if(!sous_coupe_oblig_adv)
									{
										for(int indice_carte_possible=0;indice_carte_possible<m.get(joueur).total();)
										{
											if(((CarteBelote)m.get(joueur).carte(indice_carte_possible)).force(couleur_atout,couleur_demande,contrat)>maxAtout.force(couleur_atout,couleur_demande,contrat))
											{
												m.get(joueur).jouer(m.get(joueur).carte(indice_carte_possible));
											}
											else
											{
												indice_carte_possible++;
											}
										}
									}
									else
									{
										m.get(joueur).supprimerCartes();
									}
								}
								else if(sous_coupe_oblig_part)
								{
									m.get(joueur).supprimerCartes();
								}
								else
								{
									for(int indice_carte_possible=0;indice_carte_possible<m.get(joueur).total();)
									{
										if(((CarteBelote)m.get(joueur).carte(indice_carte_possible)).force(couleur_atout,couleur_demande,contrat)>maxAtout.force(couleur_atout,couleur_demande,contrat))
										{
											m.get(joueur).jouer(m.get(joueur).carte(indice_carte_possible));
										}
										else
										{
											indice_carte_possible++;
										}
									}
								}
							}
						}
					}
				}
			}
		}
		/*Fin for sur les surcoupe et sous-coupes*/
		for(joueur=0;joueur<nombre_joueurs;joueur++)
		{
			for(Carte carte:pliEnCours)
			{
				if(m.get(joueur).contient(carte))
				{
					m.get(joueur).jouer(carte);
				}
			}
			if(!pliEnCours.estVide())
			{
				Carte carte_du_joueur=pliEnCours.carteDuJoueur(joueur,nombre_joueurs,null);
				byte couleur_demandee=pliEnCours.couleurDemandee();
				byte ramasseur_pli=pliEnCours.getRamasseurBelote(nombre_joueurs, contrat, couleur_atout);
				Carte carte_ramasseur=pliEnCours.carteDuJoueur(ramasseur_pli,nombre_joueurs,null);
				if(carte_du_joueur!=null&&carte_du_joueur.couleur()!=couleur_demandee)
				{
					if(!meme_equipe(ramasseur_pli,joueur)&&carte_ramasseur.couleur()==couleur_demandee||couleur_demandee==couleur_atout)
					{
						if(carte_du_joueur.couleur()!=couleur_atout)
						{/*Si le joueur a joue une carte autre que l'atout et que la couleur demandee sur demande d'atout ou pour une coupe sur un adversaire temporairement maitre alors il se defausse*/
							m.get(joueur).supprimerCartes();
						}
					}
					if(!meme_equipe(ramasseur_pli,joueur)&&carte_ramasseur.couleur()==couleur_atout&&couleur_demandee!=couleur_atout&&contrat.force()==1)
					{
						if(carte_du_joueur.couleur()!=couleur_atout&&sous_coupe_oblig_adv)
						{/*Si sous-coupe obligatoire adversaire impossible alors defausse sure*/
							m.get(joueur).supprimerCartes();
						}
					}
					if(meme_equipe(ramasseur_pli,joueur)&&carte_ramasseur.couleur()==couleur_demandee&&couleur_demandee!=couleur_atout&&contrat.force()==1)
					{
						if(carte_du_joueur.couleur()!=couleur_atout&&sur_coupe_oblig_part)
						{/*Si sur-coupe obligatoire partenaire impossible alors defausse sure*/
							m.get(joueur).supprimerCartes();
						}
					}
					if(meme_equipe(ramasseur_pli,joueur)&&carte_ramasseur.couleur()==couleur_atout&&couleur_demandee!=couleur_atout&&contrat.force()==1)
					{
						if(carte_du_joueur.couleur()!=couleur_atout&&sous_coupe_oblig_part)
						{/*Si sous-coupe obligatoire partenaire impossible alors defausse sure*/
							m.get(joueur).supprimerCartes();
						}
						if(carte_du_joueur.couleur()==couleur_atout&&sur_coupe_oblig_part)
						{
							if(((CarteBelote) carte_du_joueur).force(couleur_atout,couleur_demandee,contrat)<((CarteBelote) carte_ramasseur).force(couleur_atout,couleur_demandee,contrat))
							{
								for(byte indice_carte=0;indice_carte<m.get(joueur).total();)
								{
									if(((CarteBelote) m.get(joueur).carte(indice_carte)).force(couleur_atout,couleur_demandee,contrat)<((CarteBelote) carte_ramasseur).force(couleur_atout,couleur_demandee,contrat))
									{
										m.get(joueur).jouer(m.get(joueur).carte(indice_carte));
									}
									else
									{
										indice_carte++;
									}
								}
							}
						}
					}
				}
			}
		}
		return m;
	}
	private boolean meme_equipe(byte numero1,byte numero2)
	{
		return a_pour_defenseur(numero1)==a_pour_defenseur(numero2);
	}
	/**Retourne l'ensemble des cartes d'une meme couleur autre que l'atout probablement possedees par les autres joueurs on tient compte du pli en cours
	 * @param numero*/
	private Vector<MainBelote> cartesPossibles(byte couleur,MainBelote cartes_jouees,Vector<Pli> plisFaits,MainBelote cartes_joueur, byte numero)
	{
		Vector<MainBelote> m=new Vector<MainBelote>();
		byte joueur=0;
		byte nombre_joueurs=getNombreDeJoueurs();
		for(;joueur<numero;joueur++)
		{
			m.addElement(new MainBelote());
			for(byte valeur:CarteBelote.cartesCouleur)
			{
				if(!cartes_jouees.contient(new CarteBelote(valeur,couleur))&&!cartes_joueur.contient(new CarteBelote(valeur,couleur))&&(!getDistribution().derniereMain().carte(0).equals(new CarteBelote(valeur,couleur))||joueur==preneur))
				{
					m.lastElement().ajouter(new CarteBelote(valeur,couleur));
				}
			}
			if(neFournitPas(couleur, joueur, plisFaits))
			{//Les joueurs se defaussant sur atout ou couleur demandee ne peuvent pas avoir de l'atout
				m.get(joueur).supprimerCartes();
			}
		}
		m.addElement(new MainBelote());/*Pour le joueur numero*/
		m.lastElement().ajouterCartes(cartes_joueur);
		for(joueur++;joueur<nombre_joueurs;joueur++)
		{
			m.addElement(new MainBelote());
			for(byte valeur:CarteBelote.cartesCouleur)
			{
				if(!cartes_jouees.contient(new CarteBelote(valeur,couleur))&&!cartes_joueur.contient(new CarteBelote(valeur,couleur))&&(!getDistribution().derniereMain().carte(0).equals(new CarteBelote(valeur,couleur))||joueur==preneur))
				{
					m.lastElement().ajouter(new CarteBelote(valeur,couleur));
				}
			}
			if(neFournitPas(couleur, joueur, plisFaits))
			{//Les joueurs se defaussant sur atout ou couleur demandee ne peuvent pas avoir de l'atout
				m.get(joueur).supprimerCartes();
			}
		}
		/*Les cartes jouees dans le pli en cours ne peuvent pas (ou plus) etre possedees par les joueurs*/
		for(joueur=0;joueur<nombre_joueurs;joueur++)
		{
			for(Carte carte:pliEnCours)
			{
				if(m.get(joueur).contient(carte))
				{
					m.get(joueur).jouer(carte);
				}
			}
		}
		if(pliEnCours.couleurDemandee()==couleur)
		{
			for(joueur=0;joueur<nombre_joueurs;joueur++)
			{
				Carte carte_jouee=pliEnCours.carteDuJoueur(joueur,nombre_joueurs,null);
				if(carte_jouee!=null&&carte_jouee.couleur()!=couleur)
				{/*Si un joueur a joue une carte autre que l'Excuse et pas de la couleur demandee dans le pli en cours, alors il coupe ou se defausse*/
					m.get(joueur).supprimerCartes();
				}
			}
		}
		return m;
	}
	private boolean neFournitPas(byte couleur, byte joueur,Vector<Pli> plisFaits)
	{
		byte nombre_joueurs=getNombreDeJoueurs();
		for(int indice_pli=plisFaits.size()-1;indice_pli>-1;indice_pli--)
		{
			Pli pli=plisFaits.get(indice_pli);
			if(pli.couleurDemandee()==couleur)
			{
				if(pli.carteDuJoueur(joueur,nombre_joueurs,null).couleur()!=couleur)
				{
					return true;
				}
			}
		}
		return false;
	}
	/**Retourne vrai si et seulement si le joueur ne peut pas jouer atout sur demande d'atout ou couper quand il le faut (sur un adversaire ayant joue une carte de la couleur demandee forte virtuellement)*/
	private boolean defausseBelote(byte couleur, byte joueur,Vector<Pli> plisFaits)
	{
		byte nombre_joueurs=getNombreDeJoueurs();
		byte couleur_atout=couleur_atout();
		boolean sur_coupe_oblig_part=sur_coupe_obligatoire_partenaire();
		boolean sous_coupe_oblig_part=sous_coupe_obligatoire_partenaire();
		boolean sous_coupe_oblig_adv=sous_coupe_obligatoire_adversaire();
		if(contrat.force()==1)
		{
			if(couleur==couleur_atout)
			{/*Si la couleur demandee est celle de l'atout*/
				for(int indice_pli=plisFaits.size()-1;indice_pli>-1;indice_pli--)
				{
					Pli pli=plisFaits.get(indice_pli);
					if(pli.couleurDemandee()==couleur)
					{
						if(pli.carteDuJoueur(joueur, nombre_joueurs,null).couleur()!=couleur)
						{
							return true;
						}
					}
				}
			}
			else
			{
				for(int indice_pli=plisFaits.size()-1;indice_pli>-1;indice_pli--)
				{
					Pli pli=plisFaits.get(indice_pli);
					if(pli.couleurDemandee()==couleur)
					{
						Vector<Byte> joueurs=pli.joueursAyantJoueAvant(joueur,nombre_joueurs);
						byte couleur_joueur=pli.carteDuJoueur(joueur,nombre_joueurs,null).couleur();
						if(couleur_joueur!=couleur&&couleur_joueur!=couleur_atout)
						{
							if(joueurs.size()==1)
							{
								return true;
							}
							if(joueurs.size()==2)
							{
								if(((CarteBelote)pli.carte(1)).force(couleur_atout,couleur,contrat)<((CarteBelote)pli.carte(0)).force(couleur_atout,couleur,contrat))
								{
									if(sur_coupe_oblig_part)
									{
										return true;
									}
								}
								if(((CarteBelote)pli.carte(1)).force(couleur_atout,couleur,contrat)>((CarteBelote)pli.carte(0)).force(couleur_atout,couleur,contrat))
								{
									if(sous_coupe_oblig_adv&&pli.carte(1).couleur()==couleur_atout)
									{
										return true;
									}
								}
								if(pli.carte(1).couleur()==couleur&&((CarteBelote)pli.carte(1)).force(couleur_atout,couleur,contrat)>((CarteBelote)pli.carte(0)).force(couleur_atout,couleur,contrat))
								{
									return true;
								}
							}
							if(joueurs.size()==3)
							{
								int max=Math.max(((CarteBelote)pli.carte(2)).force(couleur_atout,couleur,contrat),((CarteBelote)pli.carte(0)).force(couleur_atout,couleur,contrat));
								if(max<((CarteBelote)pli.carte(1)).force(couleur_atout,couleur,contrat))
								{/*Si le partenaire est maitre sur l'equipe adverse*/
									if(pli.carte(1).couleur()==couleur_atout)
									{
										if(sous_coupe_oblig_part)
										{
											return true;
										}
									}
									else
									{
										if(sur_coupe_oblig_part)
										{
											return true;
										}
									}
								}
								if(pli.carte(2).couleur()==couleur_atout&&max>((CarteBelote)pli.carte(1)).force(couleur_atout,couleur,contrat))
								{
									if(sous_coupe_oblig_adv)
									{
										return true;
									}
								}
								if(pli.carte(2).couleur()==couleur&&max>((CarteBelote)pli.carte(1)).force(couleur_atout,couleur,contrat))
								{
									return true;
								}
							}
						}
					}
				}
			}
		}
		else
		{
			for(int indice_pli=plisFaits.size()-1;indice_pli>-1;indice_pli--)
			{
				Pli pli=plisFaits.get(indice_pli);
				if(pli.couleurDemandee()==couleur)
				{
					byte couleur_joue_joueur=pli.carteDuJoueur(joueur, nombre_joueurs,null).couleur();
					if(couleur_joue_joueur!=couleur)
					{
						return true;
					}
				}
			}
		}
		return false;
	}
	private Vector<Byte> tours(byte couleur,Vector<Pli> plis_faits) {
		Vector<Byte> nb=new Vector<Byte>();
		for(Pli pli:plis_faits)
			if(pli.couleurDemandee()==couleur)
				nb.addElement(pli.getNumero());
		return nb;
	}
	/**A la fin d'un pli on ramasse les cartes
	 * et on les ajoute dans des tas*/
	public void ajouterUneCarteDansPliEnCours(Carte c)
	{
		pliEnCours.getCartes().ajouter(c);
	}
	private MainBelote main(Vector<MainBelote> mains,byte couleur)
	{
		return mains.get(couleur-2);
	}
	private Vector<MainBelote> suite(Vector<Vector<MainBelote>> mains,byte couleur)
	{
		return mains.get(couleur-2);
	}
	private MainBelote main(Vector<Vector<MainBelote>> mains,byte couleur,int indice2)
	{
		return mains.get(couleur-2).get(indice2);
	}
	public void ajouterPliEnCours()
	{
		ramasseur=pliEnCours.getRamasseurBelote(getNombreDeJoueurs(), contrat,couleur_atout());
		if(a_pour_defenseur(ramasseur))
			getPlisDefense().addElement(pliEnCours);
		else
			getPlisAttaque().addElement(pliEnCours);
	}
	public byte couleur_atout()
	{
		return contrat.force()==1?carteAppelee.couleur():-1;
	}
	public byte getRamasseur()
	{
		return ramasseur;
	}
	/**Inclut tous les plis sauf celui qui est en cours*/
	public Vector<Pli> unionPlis()
	{
		Vector<Pli> unionPlis=new Vector<Pli>();
		unionPlis.addAll(plis.get(0));
		for(byte b=1;b<plis.size();b++)
		{
			for(Pli pli:plis.get(b))
			{
				if(unionPlis.isEmpty()||pli.getNumero()>unionPlis.lastElement().getNumero())
				{
					unionPlis.addElement(pli);
				}
				else
				{
					byte d=0;
					for(;pli.getNumero()>unionPlis.get(d).getNumero();d++);
					unionPlis.add(d,pli);
				}
			}
		}
		return unionPlis;
	}
	public byte ramasseur(Vector<Pli> plis_faits,byte numero_pli)
	{
		if(numero_pli<plis_faits.size()-1)
		{
			return plis_faits.get(numero_pli+1).getEntameur();
		}
		return ramasseur;
	}
	public int pointsAttaqueSansPrime()
	{
		int nbPointsAtt=0;
		for (Pli pli:getPlisAttaque())
		{
			for(Carte carte:pli)
			{
				nbPointsAtt+=((CarteBelote)carte).points(contrat,carteAppelee);
			}
		}
		return nbPointsAtt;
	}
	public Vector<Short> pointsAnnoncesPrimes(byte joueur)
	{
		Vector<Short> totaux=new Vector<Short>();
		Vector<Annonce> annonces_joueur=getAnnonces(joueur);
		/*On ne veut pas qu il y ait deux annonces belote rebelote*/
		annonces_joueur.remove(new Annonce(Annonce.belote_rebelote));
		for(Annonce annonce:annonces_joueur)
		{
			totaux.addElement(annonce.points());
		}
		return totaux;
	}
	public int valeurCapot()
	{
		if(getPlisDefense().isEmpty())
		{/*Le capot n est fait que si l attaque a fait tous les plis*/
			return 100;
		}
		return 0;
	}
	public int scoreDefinitifAttaque(int score_tmp_attaque,int score_tmp_defense)
	{
		if(score_tmp_attaque>=score_tmp_defense||!getInfos().lastElement().endsWith("classique"))
		{
			return score_tmp_attaque;
		}
		/*L attaque est dedans*/
		return 0;
	}
	public int scoreDefinitifDefense(int score_definitif_attaque)
	{
		boolean belote_rebelote_annonce=false;
		for(Vector<Annonce> annonces_joueur:annonces)
		{
			belote_rebelote_annonce|=annonces_joueur.contains(new Annonce(Annonce.belote_rebelote));
		}
		if(belote_rebelote_annonce)
		{
			return 182-score_definitif_attaque;
		}
		return 162-score_definitif_attaque;
	}
	public void scores(int score_definitif_attaque,int score_definitif_defense)
	{
		byte nombre_joueurs=getNombreDeJoueurs();
		Vector<Short> scores=getScores();
		for(byte joueur=0;joueur<nombre_joueurs;joueur++)
		{
			if(!a_pour_defenseur(joueur))
			{
				scores.setElementAt((short)score_definitif_attaque,joueur);
			}
			else
			{
				scores.setElementAt((short)score_definitif_defense,joueur);
			}
		}
	}
	/**Renvoie 1, si l utilisateur gagne la partie,<br>
	 * 0, s il y a match nul,<br>
	 * -1, sinon*/
	public byte gagne_nul_perd()
	{
		Vector<Short> scores=getScores();
		if(scores.get(0)>scores.get(1))
		{
			return 1;
		}
		if(scores.get(0)==scores.get(1))
		{
			return 0;
		}
		return -1;
	}
	public MainBelote empiler()
	{
		MainBelote m=new MainBelote();
		if(getEtat()==Etat.Contrat)
		{
			for(Main main:getDistribution())
			{
				m.ajouterCartes(main);
			}
		}
		else
		{
			for(Vector<Pli> plis_equipe:plis)
			{
				for(Pli pli:plis_equipe)
				{
					m.ajouterCartes(pli.getCartes());
				}
			}
		}
		return m;
	}
	public void restituerMainsDepart(Vector<Pli> plis_faits,Pli pli_petit,byte nombre_joueurs,String couleurs,String sens)
	{
		byte joueur;
		for(Pli pli:plis_faits)
		{
			for(Carte carte:pli)
			{
				getDistribution().main(pli.joueurAyantJoue(carte,nombre_joueurs,pli_petit)).ajouter(carte);
			}
		}
		for(joueur=0;joueur<nombre_joueurs;joueur++)
		{
			getDistribution().main(joueur).supprimerCartes(getDistribution().derniereMain());
		}
		if(contrat.force()==1)
		{
			for(joueur=0;joueur<nombre_joueurs;joueur++)
			{
				((MainBelote)getDistribution().main(joueur)).trier(couleurs,sens,carteAppelee);
			}
		}
		else if(contrat.force()==3)
		{
			for(joueur=0;joueur<nombre_joueurs;joueur++)
			{
				((MainBelote)getDistribution().main(joueur)).setOrdre(Ordre.Couleur);
				((MainBelote)getDistribution().main(joueur)).trier(couleurs,sens);
			}
		}
		else if(contrat.force()==4)
		{
			for(joueur=0;joueur<nombre_joueurs;joueur++)
			{
				((MainBelote)getDistribution().main(joueur)).setOrdre(Ordre.Atout);
				((MainBelote)getDistribution().main(joueur)).trier(couleurs,sens);
			}
		}
	}
	public void restituerMainsDepartRejouerDonne(Vector<Pli> plis_faits,byte nombre_joueurs)
	{
		byte joueur;
		Pli pli_petit=null;
		for(joueur=0;joueur<nombre_joueurs;joueur++)
		{
			getDistribution().main(joueur).supprimerCartes();
		}
		for(Pli pli:plis_faits)
		{
			for(Carte carte:pli)
			{
				getDistribution().main(pli.joueurAyantJoue(carte,nombre_joueurs,pli_petit)).ajouter(carte);
			}
		}
		for(joueur=0;joueur<nombre_joueurs;joueur++)
		{
			getDistribution().main(joueur).supprimerCartes(getDistribution().derniereMain());
		}
	}
	public void restituerMains(Vector<Pli> plis_faits,Pli pli_petit,byte nombre_joueurs,String couleurs,String sens,byte numero_pli)
	{
		byte joueur;
		for(joueur=0;joueur<nombre_joueurs;joueur++)
		{
			getDistribution().main(joueur).supprimerCartes();
		}
		for(Pli pli:plis_faits)
		{
			if(pli.getNumero()>numero_pli-1)
			{
				for(Carte carte:pli)
				{
					getDistribution().main(pli.joueurAyantJoue(carte,nombre_joueurs,pli_petit)).ajouter(carte);
				}
			}
		}
		if(numero_pli<0)
		{
			for(joueur=0;joueur<nombre_joueurs;joueur++)
			{
				getDistribution().main(joueur).supprimerCartes(getDistribution().derniereMain());
			}
		}
		if(contrat.force()==1)
		{
			for(joueur=0;joueur<nombre_joueurs;joueur++)
			{
				((MainBelote)getDistribution().main(joueur)).trier(couleurs,sens,carteAppelee);
			}
		}
		else if(contrat.force()==3)
		{
			for(joueur=0;joueur<nombre_joueurs;joueur++)
			{
				((MainBelote)getDistribution().main(joueur)).trier(couleurs,sens);
			}
		}
		else if(contrat.force()==4)
		{
			for(joueur=0;joueur<nombre_joueurs;joueur++)
			{
				((MainBelote)getDistribution().main(joueur)).trier(couleurs,sens);
			}
		}
	}
	public void restituerMains(Vector<Pli> plis_faits,Pli pli_petit,byte nombre_joueurs,String couleurs,String sens,byte numero_pli,byte numero_carte)
	{
		byte joueur;
		for(joueur=0;joueur<nombre_joueurs;joueur++)
		{
			getDistribution().main(joueur).supprimerCartes();
		}
		for(Pli pli:plis_faits)
		{
			if(pli.getNumero()>numero_pli-1)
			{
				for(Carte carte:pli)
				{
					getDistribution().main(pli.joueurAyantJoue(carte,nombre_joueurs,pli_petit)).ajouter(carte);
				}
			}
			else if(pli.getNumero()==numero_pli-1)
			{
				byte indice=0;
				for(Carte carte:pli)
				{
					if(indice>numero_carte)
					{
						getDistribution().main(pli.joueurAyantJoue(carte,nombre_joueurs,pli_petit)).ajouter(carte);
					}
					indice++;
				}
			}
		}
		if(numero_pli<0)
		{
			for(joueur=0;joueur<nombre_joueurs;joueur++)
			{
				getDistribution().main(joueur).supprimerCartes(getDistribution().derniereMain());
			}
		}
		if(contrat.force()==1)
		{
			for(joueur=0;joueur<nombre_joueurs;joueur++)
			{
				((MainBelote)getDistribution().main(joueur)).trier(couleurs,sens,carteAppelee);
			}
		}
		else if(contrat.force()<4)
		{
			for(joueur=0;joueur<nombre_joueurs;joueur++)
			{
				((MainBelote)getDistribution().main(joueur)).trier(couleurs,sens);
			}
		}
		else
		{
			for(joueur=0;joueur<nombre_joueurs;joueur++)
			{
				((MainBelote)getDistribution().main(joueur)).trier(couleurs,sens);
			}
		}
	}
	public Vector<Pli> getPlisAttaque()
	{
		return plis.get(0);
	}
	public Vector<Pli> getPlisDefense()
	{
		return plis.get(1);
	}
	public boolean a_pour_defenseur(byte numero)
	{
		return numero!=preneur&&numero!=(preneur+2)%getNombreDeJoueurs();
	}
	public Statut statut_de(byte numero)
	{
		if(numero==preneur)
		{
			return Statut.Preneur;
		}
		if(numero==(preneur+2)%getNombreDeJoueurs())
		{
			return Statut.Appele;
		}
		return Statut.Defenseur;
	}
}