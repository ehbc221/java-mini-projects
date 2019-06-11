package jeux.parties;
import java.util.*;
import jeux.plis.*;
import jeux.*;
import jeux.cartes.*;
import jeux.encheres.*;
import jeux.enumerations.*;
import jeux.mains.*;
/**
 *
 */
public class PartieTarot extends Partie implements Annoncable{
	private static final long serialVersionUID = 1L;
	/**Au dbut on a besoin d'un variable preneur pour stocker le joueur ayant annonce temporairement le plus haut contrat
	 * De plus, le choix par defaut de -1 pour le preneur sert pour le tarot lorsque
	 * personne ne prend un contrat et chacun joue pour soi*/
	private byte preneur=-1;
	/** Ce sont les primes, miseres ou poignees annoncees par le(s) joueur(s)*/
	private Vector<Vector<Annonce>> annonces=new Vector<Vector<Annonce>>();
	/**Poignees*/
	private Vector<MainTarot> poignees=new Vector<MainTarot>();
	/**Au tarot lors d'un appel il faut savoir si les joueurs ont confiance ou non en les autres*/
	private Vector<Vector<Boolean>> confiance=new Vector<Vector<Boolean>>();
	/**Le contrat permet de dire quel va etre le deroulement
	 * de la partie*/
	private Contrat contrat=new Contrat(EncheresTarot.Passe);
	/**Ce sont les plis faits par les joueurs*/
	/**Pli en cours d'etre joue*/
	private Pli pliEnCours;
	/**Ensemble des plis faits par les joueurs*/
	private Vector<Vector<Pli>> plis=new Vector<Vector<Pli>>();
	private byte appele=-1;
	/**la carte appelee represente l'alliance entre le preneur et un autre joueur possedant cette carte*/
	private Carte carteAppelee;
	/**Entameur du pli qui est en cours d'etre joue*/
	private byte entameur;
	/**Ensembe des contrats annonces*/
	private Vector<Contrat> contrats=new Vector<Contrat>();
	/**Ramasseur du pli qui vient d'etre joue*/
	private byte ramasseur;
	/**utilise pour dire si l'utilisateur a annonce un contrat ou non au chargement d'une partie*/
	private boolean finEncheres;
	/**Cree une partie de tarot ayant pour parametres
	 * @param type le type de partie aleatoire, editee ou entrainement
	 * @param donne la distribution de cartes entre les joueurs
	 * @param mode2 le mode indiquant le deroulement de la partie*/
	public PartieTarot(Type type, Donne donne) {
		super(type, donne);
		byte nombre_joueurs=getNombreDeJoueurs();
		if(Etat.Contrat==getEtat())
		{
			for (int i = 0; i < 2; i++)
				plis.addElement(new Vector<Pli>());
		}
		else
		{
			for (int i = 0; i < nombre_joueurs+1; i++)
				plis.addElement(new Vector<Pli>());
			plis.lastElement().addElement(new Pli(getDistribution().derniereMain(),(byte)(nombre_joueurs+1)));
		}
		for(int i=0;i<nombre_joueurs;i++)
		{/*Initialise la confiance a un jeu non solitaire*/
			confiance.addElement(new Vector<Boolean>());
			for(int j=0;j<getNombreDeJoueurs();j++)
				confiance.lastElement().addElement(i==j);
		}
		for(int i=0;i<nombre_joueurs;i++)
		{
			annonces.addElement(new Vector<Annonce>());
		}
		//Par default tout le monde est defenseur
		for(byte joueur=0;joueur<nombre_joueurs;joueur++)
		{
			poignees.addElement(new MainTarot());
		}
	}
	public void initPartie()
	{
		preneur=-1;
		appele=-1;
		if(getDistribution().getInfos().get(7).split(":")[1].startsWith("Normal"))
			setEtat(Etat.Contrat);
		else
			setEtat(Etat.Jeu);
		contrats=new Vector<Contrat>();
		carteAppelee=null;
		contrat=new Contrat(EncheresTarot.Passe);
		pliEnCours=null;
		finEncheres=false;
		Vector<Short> scores=getScores();
		byte nombre_joueurs=getNombreDeJoueurs();
		for(int i=0;i<nombre_joueurs;i++)
			scores.setElementAt((short) 0,i);
		if(Etat.Contrat==getEtat())
		{
			for (int i = 0; i < 2; i++)
				plis.setElementAt(new Vector<Pli>(),i);
		}
		else
		{
			for (int i = 0; i < nombre_joueurs+1; i++)
				plis.setElementAt(new Vector<Pli>(),i);
			plis.lastElement().setElementAt(new Pli(getDistribution().derniereMain(),(byte)(nombre_joueurs+1)),nombre_joueurs+1);
		}
		for(int i=0;i<nombre_joueurs;i++)
		{/*Initialise la confiance a un jeu non solitaire*/
			for(int j=0;j<getNombreDeJoueurs();j++)
				confiance.get(i).setElementAt(i==j,j);
		}
		for(int i=0;i<nombre_joueurs;i++)
		{
			annonces.setElementAt(new Vector<Annonce>(),i);
		}
		//Par default tout le monde est defenseur
		for(byte joueur=0;joueur<nombre_joueurs;joueur++)
		{
			poignees.setElementAt(new MainTarot(),joueur);
		}
	}
	public boolean a_pour_defenseur(byte numero)
	{
		return numero!=preneur&&numero!=appele;
	}
	public void setAppele(byte numero)
	{
		appele=numero;
	}
	public byte getAppele()
	{
		return appele;
	}
	/** Status des joueurs pour la partie:<br/>
	 * Les status sont preneur, appele, defenseur.<br/>
	 * Le preneur fait equipe avec l'eventuel appele<br/>
	 * Les defenseurs font equipes entre eux.*/
	public Statut statut_de(byte numero)
	{
		if(numero==preneur)
		{
			return Statut.Preneur;
		}
		if(numero==appele)
		{
			return Statut.Appele;
		}
		return Statut.Defenseur;
	}
	public void simuler()
	{
		long debut=System.currentTimeMillis();
		if(joueurAyantPetitSec()>-1)
		{
			return;
		}
		byte nombre_joueurs=getNombreDeJoueurs();
		boolean passe=false;
		Pli pli;
		MainTarot main;
		byte[] joueurs=new byte[nombre_joueurs];
		String[] raison={""};
		byte donneur=getDistribution().getDonneur();
		if(avec_contrat())
		{
			Contrat contrat_tmp;
			for(byte joueur=0;joueur<nombre_joueurs;joueur++)
			{
				joueurs[joueur]=(byte)((donneur+joueur+1)%nombre_joueurs);
			}
			boolean chelem_contrat=chelemContrat();
			for(byte joueur:joueurs)
			{
				contrat_tmp=strategieContrat(raison);
				ajouterContrat(contrat_tmp);
				if(chelem_contrat&&contrat_tmp.force()>4)
				{
					preneur=joueur;
					contrat=contrat_tmp;
					break;
				}
				if(!chelem_contrat&&contrat_tmp.force()>3)
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
			}
			Partie.chargement_simulation+=10;
		}
		if(contrat.force()<1&&pas_jeu_apres_passe())
		{
			return;
		}
		if(contrat.force()>0)
		{
			if(nombre_joueurs==4&&getInfos().get(8).contains("2 vs 2, appel"))
			{
				carteAppelee=strategieAppel(raison);
				appele=joueurAyantCarteAppelee();
			}
			if(nombre_joueurs==4&&getInfos().get(8).contains("2 vs 2, sans"))
			{
				appele=(byte)((preneur+2)%nombre_joueurs);
				faireConfiance(preneur,(byte) ((preneur+2)%nombre_joueurs));
				faireConfiance((byte) ((preneur+2)%nombre_joueurs),preneur);
			}
			if(nombre_joueurs==5&&getInfos().get(9).contains("2 vs 3, appel"))
			{
				carteAppelee=strategieAppel(raison);
				appele=joueurAyantCarteAppelee();
			}
			if(contrat.force()<3)
			{
				getDistribution().main(preneur).ajouterCartes(getDistribution().derniereMain());
				main=strategieEcart(raison);
				getDistribution().main(preneur).supprimerCartes(main);
				Vector<Annonce> va=strategieAnnonces(preneur,raison);
				if(!va.isEmpty())
				{
					ajouterAnnonces(preneur,va);
					setEntameur(preneur);
				}
				pli=new Pli(main,preneur);
				getPlisAttaque().addElement(pli);
			}
			if(contrat.force()==3)
			{
				Vector<Annonce> va=strategieAnnonces(preneur,raison);
				if(!va.isEmpty())
				{
					ajouterAnnonces(preneur,va);
					setEntameur(preneur);
				}
				pli=new Pli(getDistribution().derniereMain(),preneur);
				getPlisAttaque().addElement(pli);
			}
			if(contrat.force()>3)
			{
				Vector<Annonce> va=strategieAnnonces(preneur,raison);
				if(!va.isEmpty())
				{
					ajouterAnnonces(preneur,va);
					setEntameur(preneur);
				}
				pli=new Pli(getDistribution().derniereMain(),preneur);
				getPlisDefense().addElement(pli);
			}
			if(carteAppelee==null)
			{
				for(byte joueur=0;joueur<nombre_joueurs;joueur++)
				{
					if(joueur!=preneur&&joueur!=appele)
					{
						for(byte joueur2=0;joueur2<nombre_joueurs;joueur2++)
						{
							if(joueur2!=preneur&&joueur2!=appele&&joueur2!=joueur)
							{
								faireConfiance(joueur,joueur2);
							}
						}
					}
				}
			}
		}
		if(!annoncesInitialisees())
		{/*Si un joueur n'a pas annonce de Chelem on initialise l'entameur du premier pli*/
			entameur=(byte)((donneur+1)%nombre_joueurs);
		}
		if(carteAppelee!=null)
		{
			faireConfiance(appele,preneur);
		}
		Partie.chargement_simulation+=10;
		int rapport=75/getDistribution().main().total();
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
				if(!passe)
				{
					passe=true;
					if(pas_jeu_misere()||contrat!=null)
					{
						Vector<Annonce> va=strategieAnnonces(joueur,raison);
						setAnnonces(joueur,va);
						MainTarot poignee=strategiePoignee(joueur,raison);
						ajouterPoignee(poignee,joueur);
					}
				}
				getDistribution().main(joueur).jouer(ct);
				ajouterUneCarteDansPliEnCours(ct);
			}
			if(getDistribution().main().estVide()&&pliEnCours.contient(new CarteTarot((byte)1,(byte)1)))
			{/*Le Petit est mene au bout*/
				Vector<Annonce> va=new Vector<Annonce>();
				va.addElement(new Annonce(Annonce.petit_au_bout));
				ajouterAnnonces(ramasseur,va);
			}
			else if(getDistribution().main().total()==1&&pliEnCours.contient(new CarteTarot((byte)1,(byte)1)))
			{
				Vector<Byte> partenaires=coequipiers(ramasseur);
				boolean possedeExcuseMemeEquipe=false;
				for(byte b:partenaires)
					possedeExcuseMemeEquipe|=getDistribution().main(b).contient(new CarteTarot((byte)0));
				if(getDistribution().main(ramasseur).contient(new CarteTarot((byte)0))||possedeExcuseMemeEquipe)
				{
					if(!adversaireAFaitPlis(ramasseur))
					{
						Vector<Annonce> va=new Vector<Annonce>();
						va.addElement(new Annonce(Annonce.petit_au_bout));
						ajouterAnnonces(ramasseur,va);
					}
				}
			}
			Partie.chargement_simulation+=rapport;
			if(getDistribution().main().estVide())
			{
				ajouterPliEnCours();
				break;
			}
		}
		System.out.println("temps="+(System.currentTimeMillis()-debut));
	}
	public Vector<Annonce> strategieAnnonces(byte numeroJoueur,String[] raison)
	{
		return annonces(raison,numeroJoueur);
	}
	public boolean chelemContrat()
	{
		return getInfos().get(5).endsWith(Reponse.oui.toString());
	}
	public boolean avec_contrat()
	{
		return getInfos().get(7).split(":")[1].startsWith("Normal");
	}
	public boolean pas_jeu_apres_passe()
	{
		return getInfos().get(7).endsWith("Normal");
	}
	public boolean pas_jeu_misere()
	{
		return !getInfos().get(7).split(":")[1].endsWith("sere");
	}
	public Jeu jeu()
	{
		return Jeu.Tarot;
	}
	public boolean autorise(Carte c,String[] raison)
	{
		if(c.equals(new CarteTarot((byte)0)))
			return true;
		Main m=pliEnCours.getCartes();
		MainTarot main=(MainTarot)getDistribution().main();
		Vector<MainTarot> repartition=main.couleurs();
		int nombreDeTours=unionPlis().size();
		if(pliEnCours.estVide()||(m.total()==1&&m.carte(0).equals(new CarteTarot((byte)0))))
		{
			//Si un joueur a joue l'Excuse alors le joueur suivant joue une carte pour une nouvelle entame
			if(carteAppelee!=null&&nombreDeTours==1)
			{
				if(!c.equals(carteAppelee)&&c.couleur()==carteAppelee.couleur()&&main.total()>repartition.get(carteAppelee.couleur()).total())
				{
					raison[0]="Vous ne pouvez jouer la couleur "+Couleur.values()[carteAppelee.couleur()-1]+" sauf la carte "+carteAppelee;
					if(m.total()==1&&m.carte(0).equals(new CarteTarot((byte)0)))
					{
						raison[0]+=" meme sur l'Excuse";
					}
				}
				//L'entame de la couleur de la carte appelee est interdite au premier tour sauf par la carte appelee ou si le joueur ne peut pas faire autrement
				if(main.total()>repartition.get(carteAppelee.couleur()).total())
				{
					return c.equals(carteAppelee)||c.couleur()!=carteAppelee.couleur();
				}
			}
			return true;
			//Apres le permier tour on peut entamer par n'importe quelle carte
		}
		byte couleurDemandee=pliEnCours.couleurDemandee();
		//Si l'atout n'est pas demande
		if(couleurDemandee>1)
		{
			if(!repartition.get(couleurDemandee).estVide())
				//Le joueur fournit de la couleur demande s'il en a
			{
				if(couleurDemandee!=c.couleur())
				{
					raison[0]="Vous devez jouer "+Couleur.values()[couleurDemandee-1];
				}
				return couleurDemandee==c.couleur();
			}
			//Nombre d'atouts (Excuse exclue)
			if(!repartition.get(1).estVide())
			{
				//On recupere les eventuels atouts (Excuse exclue) joues dans le pli
				MainTarot atouts=((MainTarot)m).couleur((byte)1);
				if(atouts.estVide())
					//Si le pli n'est pas encore coupe
				{
					if(c.couleur()!=1)
					{
						raison[0]="Vous devez couper avec un atout";
					}
					return c.couleur()==1;
				}
				byte valeurMaxAtoutPli=atouts.carte(0).valeur();
				byte valeurMaxAtoutJoueur=repartition.get(1).carte(0).valeur();
				if(valeurMaxAtoutJoueur>valeurMaxAtoutPli)
					//On doit monter quand on peut
				{
					raison[0]="";
					if(c.couleur()!=1)
					{
						raison[0]+="Vous devez couper avec un atout.";
					}
					if(c.valeur()<=valeurMaxAtoutPli)
					{
						raison[0]+="Vous devez monter sur le "+new CarteTarot(valeurMaxAtoutPli,(byte)1)+".";
					}
					return c.couleur()==1&&c.valeur()>valeurMaxAtoutPli;
				}
				if(c.couleur()!=1)
				{
					raison[0]="Vous devez sous-couper avec un atout";
				}
				return c.couleur()==1;
				//Sinon on sous-coupe
			}
			return true;
			//Si on ne possede ni la couleur demandee ni de l'atout, on joue ce que l'on veut
		}
		//Atout demande
		byte nombreAtouts=(byte)repartition.get(1).total();
		if(nombreAtouts>0)
		{
			MainTarot atouts=((MainTarot)m).couleur((byte)1);
			byte valeurMaxAtoutPli=atouts.carte(0).valeur();
			byte valeurMaxAtoutJoueur=repartition.get(1).carte(0).valeur();
			if(valeurMaxAtoutJoueur>valeurMaxAtoutPli)
				//On doit monter quand on peut
			{
				raison[0]="";
				if(c.couleur()!=1)
				{
					raison[0]+="Vous devez couper avec un atout.\n";
				}
				if(c.valeur()<=valeurMaxAtoutPli)
				{
					raison[0]+="Vous devez monter sur le "+new CarteTarot(valeurMaxAtoutPli,(byte)1)+".";
				}
				return c.couleur()==1&&c.valeur()>valeurMaxAtoutPli;
			}
			if(c.couleur()!=1)
			{
				raison[0]="Vous devez sous-couper avec un atout";
			}
			return c.couleur()==1;
			//Sinon on fournit un atout de son choix
		}
		return true;
	}
	public boolean autorise_annonce(Annonce annonce,byte numero)
	{
		return getAnnoncesPossibles(numero).contains(annonce);
	}
	public boolean autorise_ecart_de(Carte c,String[] raison)
	{
		MainTarot m=(MainTarot)getDistribution().main();
		//Si les seules cartes de couleur sont des rois
		if(m.total()-m.tailleValeur((byte)14)-atoutsAvecExcuse(m.couleurs())<getDistribution().derniereMain().total())
		{
			if(c.valeur()==14&&c.couleur()>1)
			{
				raison[0]="Vous ne pouvez pas "+Lettre.ea+"carter un Roi.";
				return false;
			}
			if(c.couleur()==0||c.couleur()==1&&c.valeur()==1||c.valeur()==21)
			{
				raison[0]="Vous ne pouvez pas "+Lettre.ea+"carter un Bout.";
				return false;
			}
			return true;
		}
		if(c.couleur()==0)
		{
			raison[0]="Vous ne pouvez pas "+Lettre.ea+"carter la carte "+new CarteTarot((byte)0)+".";
			return false;
		}
		if(c.couleur()==1)
		{
			raison[0]="Vous ne pouvez pas "+Lettre.ea+"carter un atout.";
			return false;
		}
		if(c.valeur()==14)
		{
			raison[0]="Vous ne pouvez pas "+Lettre.ea+"carter un Roi.";
			return false;
		}
		return true;
	}
	public byte joueurAyantPetitSec()
	{
		boolean petitSec=false;
		byte joueur=-1;
		for(;joueur<getNombreDeJoueurs()&&!petitSec;joueur++)
		{
			if(joueur>-1)
			{
				petitSec|=((MainTarot)getDistribution().main(joueur)).petitSec();
			}
		}
		if(!petitSec)
		{
			return -1;
		}
		return joueur;
	}
	public Pli getPliEnCours()
	{
		return pliEnCours;
	}
	/**Appelee au debut d'une partie*/
	public void setEntameur(byte i)
	{
		if(new Contrat("Chelem").equals(contrat))
		{
			entameur=preneur;
		}
		else
		{
			entameur=i;
		}
	}
	public byte getRamasseur()
	{
		return ramasseur;
	}
	public Contrat strategieContrat(String[] raison)
	{
		byte nombre_joueurs=getNombreDeJoueurs();
		byte numero=(byte)((getDistribution().getDonneur()+1+contrats.size())%nombre_joueurs);
		Main mj=getDistribution().main(numero);
		Vector<MainTarot> couleurs=((MainTarot)mj).couleurs();
		Vector<String> infos=getInfos();
		int atouts=couleurs.get(0).total()+couleurs.get(1).total();
		boolean chelem=estUnJeuDeChelem(couleurs,infos,nombre_joueurs,raison);
		if(chelem&&infos.get(5).contains("oui"))
			return new Contrat("Chelem");
		if(chelem)
			return new Contrat("Garde contre");
		int valeurAtout;
		int valeurAtoutMoyen;
		int valeurAtoutMajeur;
		if(nombre_joueurs>3)
		{
			if(atouts<4)
				valeurAtoutMajeur=(valeurAtout=2)+4;
			else if(atouts<8)
				valeurAtoutMajeur=(valeurAtout=4)+4;
			else if(atouts<12)
				valeurAtoutMajeur=(valeurAtout=6)+4;
			else
				valeurAtoutMajeur=(valeurAtout=8)+4;
			if(atouts<8)
				valeurAtoutMoyen=2;
			else if(atouts<12)
				valeurAtoutMoyen=4;
			else
				valeurAtoutMoyen=6;
		}
		else
		{
			if(atouts<5)
				valeurAtoutMajeur=(valeurAtout=2)+4;
			else if(atouts<10)
				valeurAtoutMajeur=(valeurAtout=4)+4;
			else if(atouts<15)
				valeurAtoutMajeur=(valeurAtout=6)+4;
			else
				valeurAtoutMajeur=(valeurAtout=8)+4;
			if(atouts<10)
				valeurAtoutMoyen=2;
			else if(atouts<15)
				valeurAtoutMoyen=4;
			else
				valeurAtoutMoyen=6;
		}
		int atout_15_21=0;
		int atout_7_14=0;
		MainTarot bouts=new MainTarot();
		if(!couleurs.get(0).estVide())
		{
			bouts.ajouter(new CarteTarot((byte)0));
		}
		if(!couleurs.get(1).estVide())
		{
			if(couleurs.get(1).carte(0).valeur()>14)
			{
				if(couleurs.get(1).carte(0).valeur()==21)
				{
					bouts.ajouter(new CarteTarot((byte)21,(byte)1));
				}
				atout_15_21++;
			}
			else if(couleurs.get(1).carte(0).valeur()>6)
			{
				atout_7_14++;
			}
			if(couleurs.get(1).derniereCarte().valeur()==1)
			{
				bouts.ajouter(new CarteTarot((byte)1,(byte)1));
			}
		}
		for(int indice_carte=1;indice_carte<couleurs.get(1).total();indice_carte++)
		{
			if(couleurs.get(1).carte(indice_carte).valeur()>14)
			{
				atout_15_21++;
			}
			else if(couleurs.get(1).carte(indice_carte).valeur()>6)
			{
				atout_7_14++;
			}
			else
			{
				break;
			}
		}
		int total=valeurAtout*atouts+valeurAtoutMajeur*atout_15_21+valeurAtoutMoyen*atout_7_14;
		if(nombre_joueurs>3&&atouts<8||nombre_joueurs==3&&atouts<10)
		{
			if(bouts.contient(new CarteTarot((byte)0))&&bouts.contient(new CarteTarot((byte)21,(byte)1)))
				total+=60;
			else if(!bouts.contient(new CarteTarot((byte)21,(byte)1))&&bouts.contient(new CarteTarot((byte)0)))
				total+=20;
			else if(!bouts.contient(new CarteTarot((byte)0))&&bouts.contient(new CarteTarot((byte)21,(byte)1)))
				total+=36;
			if(bouts.contient(new CarteTarot((byte)1,(byte)1)))
				if((nombre_joueurs>3&&atouts>3||nombre_joueurs==3&&atouts>4))
					total+=10;
		}
		else
		{
			if(bouts.contient(new CarteTarot((byte)0))&&bouts.contient(new CarteTarot((byte)21,(byte)1)))
				total+=80;
			else if(!bouts.contient(new CarteTarot((byte)21,(byte)1))&&bouts.contient(new CarteTarot((byte)0)))
				total+=24;
			else if(!bouts.contient(new CarteTarot((byte)0))&&bouts.contient(new CarteTarot((byte)21,(byte)1)))
				total+=42;
			if(bouts.contient(new CarteTarot((byte)1,(byte)1)))
				if((nombre_joueurs>3&&atouts<12||nombre_joueurs==3&&atouts<15))
					total+=20;
				else
					total+=30;
		}
		for(byte couleur=2;couleur<6;couleur++)
		{
			MainTarot mt=couleurs.get(couleur);
			int rois=mt.tailleRois();
			int dames=mt.tailleDames();
			int cavaliers=mt.tailleCavaliers();
			int valets=mt.tailleValets();
			if(nombre_joueurs==3)
			{
				if(atouts<10)
				{
					total+=3*valets;
					if(rois+dames+cavaliers==3)
					{
						total+=24;
					}
					else if(rois+dames==2)
					{
						total+=17;
					}
					else if(rois+cavaliers==2)
					{
						total+=15;
					}
					else if(rois==1)
					{
						total+=10;
					}
					else if(dames+cavaliers==2)
					{
						total+=11;
					}
					else if(dames==1)
					{
						total+=6;
					}
					else if(cavaliers==1)
					{
						total+=4;
					}
					if(mt.total()==7&&atouts>2)
						total+=5;
					else if(mt.total()==8&&atouts>2)
						total+=6;
					else if(mt.total()>8&&atouts>2)
						total+=7;
					else if(mt.total()==1&&atouts>2)
						total+=3;
					else if(mt.total()==0&&atouts>2)
						total+=10;
				}
				else
				{
					total+=5*valets;
					if(rois+dames+cavaliers==3)
					{
						total+=39;
					}
					else if(rois+dames==2)
					{
						total+=28;
					}
					else if(rois+cavaliers==2)
					{
						total+=26;
					}
					else if(rois==1)
					{
						total+=16;
					}
					else if(dames+cavaliers==2)
					{
						total+=18;
					}
					else if(dames==1)
					{
						total+=10;
					}
					else if(cavaliers==1)
					{
						total+=7;
					}
					if(mt.total()==7)
						total+=10;
					else if(mt.total()==8)
						total+=12;
					else if(mt.total()>8)
						total+=14;
					else if(mt.total()==1)
						total+=7;
					else if(mt.total()==0)
						total+=20;
				}
			}
			else if(nombre_joueurs==4)
			{
				if(atouts<8)
				{
					total+=2*valets;
					if(rois+dames+cavaliers==3)
					{
						total+=20;
					}
					else if(rois+dames==2)
					{
						total+=15;
					}
					else if(rois+cavaliers==2)
					{
						total+=13;
					}
					else if(rois==1)
					{
						total+=9;
					}
					else if(dames+cavaliers==2)
					{
						total+=9;
					}
					else if(dames==1)
					{
						total+=5;
					}
					else if(cavaliers==1)
					{
						total+=3;
					}
					if(mt.total()==6&&atouts>2)
						total+=5;
					else if(mt.total()==7&&atouts>2)
						total+=6;
					else if(mt.total()>7&&atouts>2)
						total+=7;
					else if(mt.total()==1&&atouts>2)
						total+=3;
					else if(mt.total()==0&&atouts>2)
						total+=10;
				}
				else
				{
					total+=3*valets;
					if(rois+dames+cavaliers==3)
					{
						total+=32;
					}
					else if(rois+dames==2)
					{
						total+=24;
					}
					else if(rois+cavaliers==2)
					{
						total+=22;
					}
					else if(rois==1)
					{
						total+=14;
					}
					else if(dames+cavaliers==2)
					{
						total+=14;
					}
					else if(dames==1)
					{
						total+=8;
					}
					else if(cavaliers==1)
					{
						total+=5;
					}
					if(mt.total()==6)
						total+=10;
					else if(mt.total()==7)
						total+=12;
					else if(mt.total()>7)
						total+=14;
					else if(mt.total()==1)
						total+=7;
					else if(mt.total()==0)
						total+=20;
				}
			}
			else
			{
				total+=valets;
				if(atouts<8)
				{
					if(rois+dames+cavaliers==3)
					{
						total+=16;
					}
					else if(rois+dames==2)
					{
						total+=13;
					}
					else if(rois+cavaliers==2)
					{
						total+=11;
					}
					else if(rois==1)
					{
						total+=8;
					}
					else if(dames+cavaliers==2)
					{
						total+=7;
					}
					else if(dames==1)
					{
						total+=4;
					}
					else if(cavaliers==1)
					{
						total+=2;
					}
					if(mt.total()==5&&atouts>2)
						total+=5;
					else if(mt.total()==6&&atouts>2)
						total+=6;
					else if(mt.total()>6&&atouts>2)
						total+=7;
					else if(mt.total()==1&&atouts>2)
						total+=3;
					else if(mt.total()==0&&atouts>2)
						total+=10;
				}
				else
				{
					if(rois+dames+cavaliers==3)
					{
						total+=25;
					}
					else if(rois+dames==2)
					{
						total+=20;
					}
					else if(rois+cavaliers==2)
					{
						total+=18;
					}
					else if(rois==1)
					{
						total+=12;
					}
					else if(dames+cavaliers==2)
					{
						total+=10;
					}
					else if(dames==1)
					{
						total+=6;
					}
					else if(cavaliers==1)
					{
						total+=3;
					}
					if(mt.total()==5)
						total+=10;
					else if(mt.total()==6)
						total+=12;
					else if(mt.total()>6)
						total+=14;
					else if(mt.total()==1)
						total+=7;
					else if(mt.total()==0)
						total+=20;
				}
			}
		}
		int petite;
		int garde;
		int garde_sans;
		int garde_contre;
		if(nombre_joueurs==3)
		{
			petite=90;
			garde=140;
			garde_sans=200;
			garde_contre=280;
		}
		else if(nombre_joueurs==4)
		{
			if(infos.get(8).contains("1 vs 3"))
			{
				petite=90;
				garde=160;
				garde_sans=240;
				garde_contre=380;
			}
			else if(infos.get(8).contains("sans"))
			{
				petite=50;
				garde=90;
				garde_sans=150;
				garde_contre=210;
			}
			else
			{
				petite=60;
				garde=110;
				garde_sans=170;
				garde_contre=230;
			}
		}
		else
		{
			petite=70;
			garde=120;
			garde_sans=infos.lastElement().contains("1 vs 4")?210:190;
			garde_contre=infos.lastElement().contains("1 vs 4")?320:240;
		}
		Contrat c;
		if(total<petite)
			c=new Contrat("Passe");
		else if(total<garde)
			c=new Contrat("Petite");
		else if(total<garde_sans)
			c=new Contrat("Garde");
		else if(total<garde_contre)
			c=new Contrat("Garde sans");
		else
			c=new Contrat("Garde contre");
		if(c.estDemandable(contrat))
		{
			return c;
		}
		if(c.force()>0)
		{
			raison[0]="Votre main valait pour un contrat de "+c+" mais";
			raison[0]+="Vous ne pouvez pas surencherir avec le contrat "+c+" sur le contrat "+contrat+",\n";
			raison[0]+="il faut annoncer un contrat plus eleve que celui precedemment annonce ou passer.";
		}
		return new Contrat("Passe");
	}
	private static boolean estUnJeuDeChelem(Vector<MainTarot> couleurs,Vector<String> infos, byte joueurs,String[] raison)
	{
		if(estUnJeuDeChelemSur(couleurs))
		{
			raison[0]="Vous ne pouvez pas perdre la partie\n";
			raison[0]+="Vous allez faire tous les plis.";
			return true;
		}
		byte atouts=(byte)(couleurs.get(0).total()+couleurs.get(1).total());
		byte atoutsMaitres=nbAtoutsMaitres(couleurs);
		byte nombre_couleurs_larg_mait=nbCouleursLargementMaitresses(couleurs,joueurs);
		if(joueurs==3)
		{
			if(nombre_couleurs_larg_mait==4)
			{
				switch (atouts) {
				case 22:
				case 21:
					return atoutsMaitres>0;
				case 20:
				case 19:
					return atoutsMaitres>1;
				case 18:
				case 17:
					return atoutsMaitres>2;
				case 16:
				case 15:
					return atoutsMaitres>3;
				case 14:
				case 13:
					return atoutsMaitres>4;
				case 12:
				case 11:
					return atoutsMaitres>5;
				case 10:
				case 9:
					return atoutsMaitres>6;
				default:
					return false;}
			}
			return false;
		}
		byte nombre_couleurs_pseu_mait=nbCouleursPseudoMaitresses(couleurs,joueurs);
		if(joueurs==4)
		{
			switch (atouts) {
			case 18:
			case 17:
			case 16:if(infos.get(8).contains("2 vs 2")&&!infos.get(8).contains("sans"))
				return atoutsMaitres>1&&(nombre_couleurs_pseu_mait==1&&nombre_couleurs_larg_mait==3||nombre_couleurs_larg_mait==4);
				return atoutsMaitres>1&&nombre_couleurs_larg_mait==4;
			case 15:
			case 14:
				if(infos.get(8).contains("2 vs 2")&&!infos.get(8).contains("sans"))
					return atoutsMaitres>2&&(nombre_couleurs_pseu_mait==1&&nombre_couleurs_larg_mait==3||nombre_couleurs_larg_mait==4);
					return atoutsMaitres>2&&nombre_couleurs_larg_mait==4;
			case 13:
			case 12:
				if(infos.get(8).contains("2 vs 2")&&!infos.get(8).contains("sans"))
					return atoutsMaitres>3&&(nombre_couleurs_pseu_mait==1&&nombre_couleurs_larg_mait==3||nombre_couleurs_larg_mait==4);
					return atoutsMaitres>3&&nombre_couleurs_larg_mait==4;
			case 11:
			case 10:
				if(infos.get(8).contains("2 vs 2")&&!infos.get(8).contains("sans"))
					return atoutsMaitres>4&&(nombre_couleurs_pseu_mait==1&&nombre_couleurs_larg_mait==3||nombre_couleurs_larg_mait==4);
					return atoutsMaitres>4&&nombre_couleurs_larg_mait==4;
			case 9:
			case 8:
				if(infos.get(8).contains("2 vs 2")&&!infos.get(8).contains("sans"))
					return atoutsMaitres>5&&(nombre_couleurs_pseu_mait==1&&nombre_couleurs_larg_mait==3||nombre_couleurs_larg_mait==4);
					return atoutsMaitres>5&&nombre_couleurs_larg_mait==4;
			default:
				return false;}
		}
		switch (atouts) {
		case 15:if(infos.get(9).contains("2 vs 3"))
			return atoutsMaitres>1&&(nombre_couleurs_pseu_mait==1&&nombre_couleurs_larg_mait==3||nombre_couleurs_larg_mait==4);
			return atoutsMaitres>1&&nombre_couleurs_larg_mait==4;
		case 14:
		case 13:
			if(infos.get(9).contains("2 vs 3"))
				return atoutsMaitres>2&&(nombre_couleurs_pseu_mait==1&&nombre_couleurs_larg_mait==3||nombre_couleurs_larg_mait==4);
				return atoutsMaitres>2&&nombre_couleurs_larg_mait==4;
		case 12:
		case 11:
			if(infos.get(9).contains("2 vs 3"))
				return atoutsMaitres>3&&(nombre_couleurs_pseu_mait==1&&nombre_couleurs_larg_mait==3||nombre_couleurs_larg_mait==4);
				return atoutsMaitres>3&&nombre_couleurs_larg_mait==4;
		case 10:
		case 9:
			if(infos.get(9).contains("2 vs 3"))
				return atoutsMaitres>4&&(nombre_couleurs_pseu_mait==1&&nombre_couleurs_larg_mait==3||nombre_couleurs_larg_mait==4);
				return atoutsMaitres>4&&nombre_couleurs_larg_mait==4;
		case 8:
		case 7:
			if(infos.get(9).contains("2 vs 3"))
				return atoutsMaitres>5&&(nombre_couleurs_pseu_mait==1&&nombre_couleurs_larg_mait==3||nombre_couleurs_larg_mait==4);
				return atoutsMaitres>5&&nombre_couleurs_larg_mait==4;
		default:
			return false;}
	}
	private static boolean estUnJeuDeChelemSur(Vector<MainTarot> couleurs)
	{
		if(!couleurs.get(0).estVide())
		{
			if(nbAtoutsMaitres(couleurs)+couleurs.get(1).total()>20)
			{
				if(nbCouleursMaitresses(couleurs)>3)
				{
					return true;
				}
			}
		}
		if(nbAtoutsMaitres(couleurs)+couleurs.get(1).total()>21)
		{
			if(nbCouleursMaitresses(couleurs)>3)
			{
				return true;
			}
		}
		return false;
	}
	private static byte nbAtoutsMaitres(Vector<MainTarot> repartition)
	{
		byte nb=0;
		for(int i=0;i<repartition.get(1).total();i++)
			if(repartition.get(1).carte(i).valeur()+i==21)
				nb++;
		return nb;
	}
	private static byte nbCouleursMaitresses(Vector<MainTarot> couleurs)
	{
		byte nb=0;
		for(byte b=2;b<6;b++)
			if(MaitreDansUneCouleur(couleurs,b))
				nb++;
		return nb;
	}
	private static byte nbCouleursPseudoMaitresses(Vector<MainTarot> couleurs,byte nombre_joueurs)
	{
		byte nb=0;
		for(byte couleur=2;couleur<6;couleur++)
			if(pseudoMaitreDansUneCouleurContrat(couleurs,couleur,nombre_joueurs))
				nb++;
		return nb;
	}
	private static byte nbCouleursLargementMaitresses(Vector<MainTarot> couleurs,byte nombre_joueurs)
	{
		byte nb=0;
		for(byte couleur=2;couleur<6;couleur++)
			if(LargementMaitreDansUneCouleurAuDebut(couleurs,couleur,nombre_joueurs))
				nb++;
		return nb;
	}
	private static boolean pseudoMaitreDansUneCouleurContrat(Vector<MainTarot> couleurs,byte noCouleur,byte nombre_joueurs)
	{
		if(LargementMaitreDansUneCouleurAuDebut(couleurs,noCouleur,nombre_joueurs))
			return false;
		return pseudoMaitreDansUneCouleur(couleurs,noCouleur);
	}
	private static boolean LargementMaitreDansUneCouleurAuDebut(Vector<MainTarot> couleurs,byte noCouleur,byte nombre_joueurs)
	{
		if(MaitreDansUneCouleur(couleurs,noCouleur))
			return true;
		if(nombre_joueurs==3)
		{
			if(nbCartesMaitresses(couleurs,noCouleur)>5)
				return true;
		}
		else if(nombre_joueurs==4)
		{
			if(nbCartesMaitresses(couleurs,noCouleur)>4)
				return true;
		}
		else
		{
			if(nbCartesMaitresses(couleurs,noCouleur)>3)
				return true;
		}
		return false;
	}
	/**Renvoie la carte a appeler
	 * @param numero*/
	public Contrat getContrat()
	{
		return contrat;
	}
	public void setContrat(Contrat pcontrat)
	{
		contrat=pcontrat;
	}
	public void ajouterContrat(Contrat c)
	{
		contrats.addElement(c);
	}
	public Contrat contrat(int i)
	{
		return contrats.get(i);
	}
	public Vector<Contrat> tous_contrats()
	{
		return contrats;
	}
	public int contrats()
	{
		return contrats.size();
	}
	public void finEnchere()
	{
		finEncheres=true;
	}
	public boolean getFinEnchere()
	{
		return finEncheres;
	}
	public int max_contrat()
	{
		if(chelemContrat())
		{
			return new Contrat(Contrat.chelem).force();
		}
		return new Contrat(EncheresTarot.Garde_contre).force();
	}
	public void setPreneur(byte ppreneur)
	{
		preneur=ppreneur;
	}
	public byte getPreneur()
	{
		return preneur;
	}
	public CarteTarot strategieAppel(String[] raison)
	{
		byte joueurs=getNombreDeJoueurs();
		boolean figure=getInfos().get(8).contains("figure")&&joueurs==4||getInfos().get(9).contains("figure")&&joueurs==5;
		MainTarot main_preneur=(MainTarot)getDistribution().main(preneur);
		Vector<MainTarot> repartition=main_preneur.couleurs();
		if(estUnJeuDeChelemSur(repartition))
		{
			raison[0]="Vous allez marquer plus de points en jouant seul avec un grand chelem sur";
			MainTarot rois=carte(repartition,(byte)14);
			if(!figure)
			{
				MainTarot dames=carte(repartition,(byte)13);
				MainTarot cavaliers=carte(repartition,(byte)12);
				if(rois.total()>0&&rois.total()<4)
				{
					return (CarteTarot)rois.carte(0);
				}
				else if(rois.total()==4&&(dames.total()>0||dames.total()<4))
				{
					return (CarteTarot)dames.carte(0);
				}
				else if(rois.total()==4&&dames.total()==4&&(cavaliers.total()>0||cavaliers.total()<4))
				{
					return (CarteTarot)cavaliers.carte(0);
				}
			}
			if(rois.total()>0)
			{
				return (CarteTarot)rois.carte(0);
			}
			raison[0]="C'est une des quatre possibilites";
			return new CarteTarot((byte)14,(byte)2);
		}
		return new CarteTarot(valeurAappeler(joueurs,figure, repartition),couleurAappeler(joueurs,figure,raison, repartition));
	}
	public Carte getCarteAppelee() {
		return carteAppelee;
	}
	public void setCarteAppelee(Carte c)
	{
		carteAppelee=c;
	}
	/**Retourne la couleur a appeler
	 * @param raison
	 * @param repartition*/
	private byte couleurAappeler(byte nombreDeJoueurs,boolean figure, String[] raison, Vector<MainTarot> repartition)
	{
		MainTarot rois=carte(repartition,(byte)14);
		MainTarot dames=carte(repartition,(byte)13);
		MainTarot cavaliers=carte(repartition,(byte)12);
		MainTarot valets=carte(repartition,(byte)11);
		byte max=-101;
		Vector<Byte> couleurs=new Vector<Byte>();
		for (byte couleur=0;couleur<6;couleur++)
		{
			byte valeur_couleur=valeurCouleur(couleur,nombreDeJoueurs,figure,raison, repartition,rois,dames,cavaliers,valets);
			if(valeur_couleur>max)
			{
				couleurs=new Vector<Byte>();
				couleurs.addElement(couleur);
				max=valeur_couleur;
			}
			else if(valeur_couleur==max)
			{
				couleurs.addElement(couleur);
			}
		}
		if(couleurs.size()==1)
		{
			return couleurs.get(0);
		}
		if(couleurs.size()==2)
		{
			Main couleur1=repartition.get(couleurs.get(0));
			Main couleur2=repartition.get(couleurs.get(1));
			int taille=couleur1.total();
			for(byte b=0;b<taille;b++)
			{
				if(couleur1.carte(b).valeur()>couleur2.carte(b).valeur())
					return couleurs.get(0);
				if(couleur1.carte(b).valeur()<couleur2.carte(b).valeur())
					return couleurs.get(1);
			}
			return couleurs.get(0);
		}
		if(couleurs.size()==3)
		{
			Main couleur1=repartition.get(couleurs.get(0));
			Main couleur2=repartition.get(couleurs.get(1));
			int taille=couleur1.total();
			byte lePlusGrand=couleurs.get(0);
			for(byte b=0;b<taille;b++)
			{
				if(couleur1.carte(b).valeur()>couleur2.carte(b).valeur())
				{
					lePlusGrand=couleurs.get(0);
					break;
				}
				if(couleur1.carte(b).valeur()<couleur2.carte(b).valeur())
				{
					lePlusGrand=couleurs.get(1);
					break;
				}
			}
			couleur2=repartition.get(lePlusGrand);
			couleur1=repartition.get(couleurs.get(2));
			for(byte b=0;b<taille;b++)
			{
				if(couleur2.carte(b).valeur()>couleur1.carte(b).valeur())
					return couleurs.get(1);
				if(couleur2.carte(b).valeur()<couleur1.carte(b).valeur())
					return couleurs.get(2);
			}
			return lePlusGrand;
		}
		Main couleur1=repartition.get(couleurs.get(0));
		Main couleur2=repartition.get(couleurs.get(1));
		int taille=couleur1.total();
		byte lePlusGrand=couleurs.get(0);
		for(byte b=0;b<taille;b++)
		{
			if(couleur1.carte(b).valeur()>couleur2.carte(b).valeur())
			{
				lePlusGrand=couleurs.get(0);
				break;
			}
			if(couleur1.carte(b).valeur()<couleur2.carte(b).valeur())
			{
				lePlusGrand=couleurs.get(1);
				break;
			}
		}
		couleur2=repartition.get(lePlusGrand);
		couleur1=repartition.get(couleurs.get(2));
		for(byte b=0;b<taille;b++)
		{
			if(couleur1.carte(b).valeur()>couleur2.carte(b).valeur())
			{
				lePlusGrand=couleurs.get(2);
				break;
			}
			if(couleur1.carte(b).valeur()<couleur2.carte(b).valeur())
			{
				break;
			}
		}
		couleur2=repartition.get(lePlusGrand);
		couleur1=repartition.get(couleurs.get(3));
		for(byte b=0;b<taille;b++)
		{
			if(couleur2.carte(b).valeur()>couleur1.carte(b).valeur())
				return lePlusGrand;
			if(couleur2.carte(b).valeur()<couleur1.carte(b).valeur())
				return couleurs.get(3);
		}
		return lePlusGrand;
	}
	/**Pour determiner la couleur du roi a appeler
	 * @param raison
	 * @param repartition*/
	private byte valeurCouleur(byte couleur,byte nombreDeJoueurs,boolean figure, String[] raison, Vector<MainTarot> repartition,MainTarot rois,MainTarot dames,MainTarot cavaliers,MainTarot valets)
	{
		if(nombreDeJoueurs==5&&couleur<2)
		{
			return -20;
		}
		MainTarot cartesCouleur=repartition.get(couleur);
		byte nombre_rois_couleur=(byte)(!cartesCouleur.estVide()&&cartesCouleur.carte(0).valeur()==14?1:0);
		if(!figure)
		{
			if(rois.total()<4)
			{
				if(rois.total()==3)
				{
					raison[0]="C'est la seule carte a appeler pour ne pas etre seul";
				}
				if(atoutsAvecExcuse(repartition)<8)
				{
					byte nb=0;
					if(nombre_rois_couleur>0)
					{
						return -17;
					}
					if(cartesCouleur.total()>0&&cartesCouleur.total()<10-nombreDeJoueurs)
					{
						raison[0]="";
						for(int i=1;i<4;i++)
						{
							byte puissance=1;
							for(byte b=0;b<i;b++)
							{
								puissance*=3;
							}
							nb+=cartesCouleur.tailleValeur((byte)(10+i))*puissance;
						}
						nb-=cartesCouleur.total()-cartesCouleur.nombreDeFigures(couleur);
					}
					else if(cartesCouleur.total()>4)
						nb=(byte)-cartesCouleur.total();
					else
						nb=-16;
					return nb;
				}
				if(nombre_rois_couleur>0)
				{
					return -1;
				}
				raison[0]="";
				return (byte)cartesCouleur.total();
			}
			if(dames.total()<4)
			{
				if(dames.total()==3)
					raison[0]="";
				if(atoutsAvecExcuse(repartition)<8)
				{
					byte nb=0;
					if(cartesCouleur.tailleDames()>0)
					{
						return -17;
					}
					if(cartesCouleur.total()<10-nombreDeJoueurs)
					{
						for(int i=1;i<3;i++)
						{
							byte puissance=1;
							for(byte b=0;b<i;b++)
							{
								puissance*=3;
							}
							nb+=cartesCouleur.tailleValeur((byte)(10+i))*puissance;
						}
						byte puissance=1;
						for(byte b=0;b<4;b++)
						{
							puissance*=3;
						}
						nb+=cartesCouleur.tailleValeur((byte)14)*puissance;
						nb-=cartesCouleur.total()-cartesCouleur.nombreDeFigures(couleur);
					}
					else
						nb=(byte)-cartesCouleur.total();
					return nb;
				}
				if(cartesCouleur.tailleDames()>0)
				{
					return -1;
				}
				return (byte)cartesCouleur.total();
			}
			if(cavaliers.total()<4)
			{
				if(cavaliers.total()==3)
					raison[0]="";
				byte nb=0;
				if(cartesCouleur.tailleCavaliers()>0)
				{
					return -17;
				}
				if(cartesCouleur.total()<10-nombreDeJoueurs)
				{
					nb+=cartesCouleur.tailleValeur((byte)11)*3;
					for(int i=3;i<5;i++)
					{
						byte puissance=1;
						for(byte b=0;b<i-1;b++)
						{
							puissance*=3;
						}
						nb+=cartesCouleur.tailleValeur((byte)(10+i))*puissance;
					}
					nb-=cartesCouleur.total()-cartesCouleur.nombreDeFigures(couleur);
				}
				else
					nb=(byte)-cartesCouleur.total();
				return nb;
			}
			if(valets.total()<4)
			{
				if(valets.total()==3)
					raison[0]="";
				byte nb=0;
				if(cartesCouleur.tailleValets()>0)
				{
					return -17;
				}
				if(cartesCouleur.total()<10-nombreDeJoueurs)
				{
					for(int i=2;i<5;i++)
					{
						byte puissance=1;
						for(byte b=0;b<i-1;b++)
						{
							puissance*=3;
						}
						nb+=cartesCouleur.tailleValeur((byte)(10+i))*puissance;
					}
					nb-=cartesCouleur.total()-cartesCouleur.nombreDeFigures(couleur);
				}
				else
					nb=(byte)-cartesCouleur.total();
				return nb;
			}
			if(repartition.get(1).estVide()||repartition.get(1).carte(0).valeur()<21) {
				if(couleur>0)
					return (byte)(5-couleur);
				return 0;
			}
			if(!repartition.get(0).estVide()&(raison[0]="")=="")
				return (byte)(5-couleur);
			if(couleur>0)
				return (byte)(5-couleur);
			raison[0]="";
			return 0;
		}
		if(4-nombreDeCoupesFranches(repartition)!=rois.total())
		{
			if(atoutsAvecExcuse(repartition)<8)
			{
				byte nb=0;
				if(nombre_rois_couleur>0)
				{
					return -17;
				}
				if(cartesCouleur.total()>0&&cartesCouleur.total()<10-nombreDeJoueurs)
				{
					for(int i=1;i<4;i++)
					{
						byte puissance=1;
						for(byte b=0;b<i;b++)
						{
							puissance*=3;
						}
						nb+=cartesCouleur.tailleValeur((byte)(10+i))*puissance;
					}
					nb-=cartesCouleur.total()-cartesCouleur.nombreDeFigures(couleur);
				}
				else if(cartesCouleur.total()>4)
					nb=(byte)-cartesCouleur.total();
				else
					nb=-16;
				return nb;
			}
			if(nombre_rois_couleur>0)
			{
				return -1;
			}
			return (byte)cartesCouleur.total();
		}
		if(4-nombreDeCoupesFranches(repartition)!=dames.total())
		{
			if(atoutsAvecExcuse(repartition)<8)
			{
				byte nb=0;
				if(cartesCouleur.tailleDames()>0)
				{
					return -17;
				}
				if(cartesCouleur.total()>0&&cartesCouleur.total()<10-nombreDeJoueurs)
				{
					for(int i=1;i<3;i++)
					{
						byte puissance=1;
						for(byte b=0;b<i;b++)
						{
							puissance*=3;
						}
						nb+=cartesCouleur.tailleValeur((byte)(10+i))*puissance;
					}
					byte puissance=1;
					for(byte b=0;b<4;b++)
					{
						puissance*=3;
					}
					nb+=cartesCouleur.tailleValeur((byte)14)*puissance;
					nb-=cartesCouleur.total()-cartesCouleur.nombreDeFigures(couleur);
				}
				else if(cartesCouleur.total()>4)
					nb=(byte)-cartesCouleur.total();
				else
					nb=-16;
				return nb;
			}
			if(cartesCouleur.tailleDames()>0)
			{
				return -1;
			}
			return (byte)cartesCouleur.total();
		}
		if(4-nombreDeCoupesFranches(repartition)!=cavaliers.total())
		{
			if(atoutsAvecExcuse(repartition)<8)
			{
				byte nb=0;
				if(cartesCouleur.tailleCavaliers()>0)
				{
					return -17;
				}
				if(cartesCouleur.total()>0&&cartesCouleur.total()<10-nombreDeJoueurs)
				{
					nb+=cartesCouleur.tailleValeur((byte)11)*3;
					for(int i=3;i<5;i++)
					{
						byte puissance=1;
						for(byte b=0;b<i-1;b++)
						{
							puissance*=3;
						}
						nb+=cartesCouleur.tailleValeur((byte)(10+i))*puissance;
					}
					nb-=cartesCouleur.total()-cartesCouleur.nombreDeFigures(couleur);
				}
				else if(cartesCouleur.total()>4)
					nb=(byte)-cartesCouleur.total();
				else
					nb=-16;
				return nb;
			}
			if(cartesCouleur.tailleCavaliers()>0)
			{
				return -1;
			}
			return (byte)cartesCouleur.total();
		}
		if(4-nombreDeCoupesFranches(repartition)!=valets.total())
		{
			if(atoutsAvecExcuse(repartition)<8)
			{
				byte nb=0;
				if(cartesCouleur.tailleValets()>0)
				{
					return -17;
				}
				if(cartesCouleur.total()>0&&cartesCouleur.total()<10-nombreDeJoueurs)
				{
					for(int i=2;i<5;i++)
					{
						byte puissance=1;
						for(byte b=0;b<i-1;b++)
						{
							puissance*=3;
						}
						nb+=cartesCouleur.tailleValeur((byte)(10+i))*puissance;
					}
					nb-=cartesCouleur.total()-cartesCouleur.nombreDeFigures(couleur);
				}
				else if(cartesCouleur.total()>4)
					nb=(byte)-cartesCouleur.total();
				else
					nb=-16;
				return nb;
			}
			if(cartesCouleur.tailleValets()>0)
			{
				return -1;
			}
			return (byte)cartesCouleur.total();
		}
		Vector<Byte> couleurs_courtes=couleursLesPlusCourtes(repartition);
		if(nombreDeJoueurs==5) {
			if(couleurs_courtes.contains(couleur))
				return couleurs_courtes.get(0);
			return 0;
		}
		if(repartition.get(1).estVide()||repartition.get(1).carte(0).valeur()<21) {
			if(couleur>0)
				return (byte)(5-couleur);
			return 0;
		}
		if(!repartition.get(0).estVide())
			return (byte)(5-couleur);
		if(couleur>0)
			return (byte)(5-couleur);
		if(couleurs_courtes.contains(couleur))
			return couleurs_courtes.get(0);
		return 0;
	}
	private byte valeurAappeler(byte nombreDeJoueurs,boolean figure, Vector<MainTarot> repartition)
	{
		MainTarot rois=carte(repartition,(byte)14);
		MainTarot dames=carte(repartition,(byte)13);
		MainTarot cavaliers=carte(repartition,(byte)12);
		MainTarot valets=carte(repartition,(byte)11);
		if(!figure)
		{
			if(rois.total()<4)
				return 14;
			if(dames.total()<4)
				return 13;
			if(cavaliers.total()<4)
				return 12;
			if(valets.total()<4)
				return 11;
			if(!repartition.get(1).contient(new CarteTarot((byte)21,(byte)1)))
				return 21;
			if(repartition.get(0).estVide())
				return new CarteTarot((byte)0).valeur();
			return 1;
		}
		int nombreDeCoupes=nombreDeCoupesFranches(repartition);
		if(rois.total()!=4-nombreDeCoupes)
			return 14;
		if(dames.total()!=4-nombreDeCoupes)
			return 13;
		if(cavaliers.total()!=4-nombreDeCoupes)
			return 12;
		if(valets.total()!=4-nombreDeCoupes)
			return 11;
		if(nombreDeJoueurs==4)
		{
			if(!repartition.get(1).contient(new CarteTarot((byte)21,(byte)1)))
				return 21;
			if(repartition.get(0).estVide())
				return new CarteTarot((byte)0).valeur();
			if(!repartition.get(1).contient(new CarteTarot((byte)1,(byte)1)))
				return 1;
		}
		if(rois.total()<4)
			return 14;
		if(dames.total()<4)
			return 13;
		if(cavaliers.total()<4)
			return 12;
		return 11;
	}
	private static MainTarot carte(Vector<MainTarot> repartition,byte valeur)
	{
		MainTarot main=new MainTarot();
		for(byte couleur=2;couleur<6;couleur++)
		{
			if(repartition.get(couleur).contient(new CarteTarot(valeur,couleur)))
			{
				main.ajouter(new CarteTarot(valeur,couleur));
			}
		}
		return main;
	}
	private Vector<Byte> couleursLesPlusCourtes(Vector<MainTarot> repartition)
	{
		Vector<Byte> couleurs=new Vector<Byte>();
		byte min=0;
		for(MainTarot couleur:repartition)
		{
			min+=couleur.total();
		}
		for(byte couleur=2;couleur<6;couleur++)
		{
			byte nb=(byte)repartition.get(couleur).total();
			if(nb<min)
			{
				couleurs=new Vector<Byte>();
				couleurs.addElement(couleur);
				min=nb;
			}
			else if(nb==min)
			{
				couleurs.addElement(couleur);
			}
		}
		return couleurs;
	}
	public MainTarot strategieEcart(String[] raison)
	{
		MainTarot main_preneur=((MainTarot)getDistribution().main(preneur));
		Vector<MainTarot> repartition=main_preneur.couleurs();
		byte nombre_joueurs=getNombreDeJoueurs();
		int tailleChien=getDistribution().derniereMain().total();
		MainTarot ecartables=getCartesEcartables(tailleChien,repartition);
		Vector<MainTarot> rep_ecartables=ecartables.couleurs();
		int limiteAtouts=13-nombre_joueurs;
		MainTarot ecart=new MainTarot();
		if(!rep_ecartables.get(1).estVide())
		{//Si des atouts sont a ecarter
			for(byte couleur=2;couleur<6;couleur++)
			{
				ecart.ajouterCartes(rep_ecartables.get(couleur));
			}
			int reste=tailleChien-ecart.total();
			for(byte carte=0;carte<reste;carte++)
			{
				ecart.ajouter(rep_ecartables.get(1).carte(rep_ecartables.get(1).total()-1-carte));
			}
			return ecart;
		}
		if(ecartables.total()==tailleChien)
		{
			raison[0]="Ce sont les seules cartes a ecarter";
			return ecartables;
		}
		byte couleur_appelee=couleurAppelee();
		if(atoutsAvecExcuse(repartition)>=limiteAtouts)
		{
			MainTarot nonPseudoMaitres=carteAppelee!=null?cartesNonPseudoMaitresses(repartition):cartesNonMaitresses(repartition);
			if(nonPseudoMaitres.total()<=tailleChien)
			{
				ecart.ajouterCartes(nonPseudoMaitres);
				if(ecart.total()==tailleChien)
				{
					return ecart;
				}
				/*On supprime les cartes deja ecartees*/
				ecartables.supprimerCartes(nonPseudoMaitres);
				rep_ecartables=ecartables.couleurs();
			}
		}
		if(nombreDeCoupesFranches(repartition)==0)
		{
			Vector<Byte> couleursCourteMaxFigures=new Vector<Byte>();
			choisirPremiereCoupeFranche(tailleChien,nombre_joueurs,repartition,couleursCourteMaxFigures);
			if(repartition.get(1).estVide()||repartition.get(1).derniereCarte().valeur()>1)
			{//Si le preneur ne possede pas le Petit
				if(couleursCourteMaxFigures.isEmpty()||carteAppelee!=null&&!couleursCourteMaxFigures.isEmpty()&&couleursCourteMaxFigures.get(0)==couleur_appelee)
				{//Si la seule couleur ou il est possible une coupe franche est l'eventuelle couleur appelee ou il n'existe aucune couleur
					//ou faire sa coupe franche	alors on ecarte les figures en 1er
					sauvetage_figures(tailleChien, repartition, rep_ecartables, couleur_appelee, ecartables, ecart);
					return ecart;
				}
				/*Suppression d une couleur pour une coupe franche*/
				gerer_figures_et_faire_coupe(tailleChien,couleursCourteMaxFigures,repartition,rep_ecartables,couleur_appelee,ecartables,ecart);
				return ecart;
			}
			/*Possede le Petit*/
			if(couleursCourteMaxFigures.isEmpty())
			{//Si la seule couleur ou il est possible une coupe franche est l'eventuelle couleur appelee ou il n'existe aucune couleur
				//ou faire sa coupe franche	alors on ecarte les figures en 1er
				sauvetage_figures(tailleChien, repartition, rep_ecartables, couleur_appelee, ecartables, ecart);
				return ecart;
			}
			gerer_figures_et_faire_coupe(tailleChien,couleursCourteMaxFigures,repartition,rep_ecartables,couleur_appelee,ecartables,ecart);
			return ecart;
		}
		/*Au moins une coupe de base*/
		sauvetage_figures(tailleChien,repartition,rep_ecartables,couleur_appelee,ecartables,ecart);
		return ecart;
	}
	private static MainTarot getCartesEcartables(int nombre_cartes_chien,Vector<MainTarot> repartition)
	{
		MainTarot cartes_ecartables=new MainTarot();
		int atouts_excuse=atoutsAvecExcuse(repartition);
		int total=atouts_excuse;
		int rois=0;
		for(byte couleur=2;couleur<6;couleur++)
		{
			MainTarot main=repartition.get(couleur);
			if(!main.estVide())
			{
				total+=main.total();
				if(main.carte(0).valeur()>13)
				{
					rois++;
				}
				else
				{
					cartes_ecartables.ajouter(main.carte(0));
				}
				for(byte carte=1;carte<main.total();carte++)
				{
					cartes_ecartables.ajouter(main.carte(carte));
				}
			}
		}
		if(total-rois-atouts_excuse<nombre_cartes_chien)
		{/*S il y a moins de cartes de couleur autres que des rois que de cartes a ecarter
			alors il existe forcement au moins un atout dans la main*/
			MainTarot atouts=repartition.get(1);/*atouts est trie dans le sens decroissant des numeros*/
			if(atouts.carte(0).valeur()<21)
			{/*Pas de 21 d atout dans la main*/
				cartes_ecartables.ajouter(atouts.carte(0));
			}
			for(byte carte=1;carte<atouts.total()-1;carte++)
			{
				cartes_ecartables.ajouter(atouts.carte(carte));
			}
			if(atouts.derniereCarte().valeur()>1)
			{/*Pas de Petit d atout dans la main*/
				cartes_ecartables.ajouter(atouts.derniereCarte());
			}
		}
		return cartes_ecartables;
	}
	private static MainTarot figuresNAppartenantPas(Vector<MainTarot> repartition,byte[] couleurs)
	{
		MainTarot retour=new MainTarot();
		int indice=-1;
		for(MainTarot main:repartition)
		{
			if(!main.estVide())
			{
				indice=-1;
				for(byte couleur:couleurs)
				{
					if(main.carte(0).couleur()==couleur)
					{
						indice=couleur;
						break;
					}
				}
				if(indice==-1)
				{/*Si la couleur n est pas un element du tableau couleurs*/
					for(Carte carte:main)
					{
						if(carte.valeur()>10)
						{
							retour.ajouter(carte);
						}
					}
				}
			}
		}
		return retour;
	}
	/**On classe les figures selon certains criteres pour l ecart*/
	private static void classerFigures(Vector<MainTarot> couleurs,MainTarot main)
	{
		int[] nombreDeFigures=new int[4];
		for(byte b=2;b<6;b++)
		{
			nombreDeFigures[b-2]=couleurs.get(b).nombreDeFigures(b);
		}
		for(int b=0;b<main.total();b++)
		{
			for(int c=b+1;c<main.total();c++)
			{
				Carte c1=main.carte(b);
				Carte c2=main.carte(c);
				byte couleur1=c1.couleur();
				MainTarot main1=couleurs.get(couleur1);
				int nombre_figures1=nombreDeFigures[couleur1-2];
				byte couleur2=c2.couleur();
				MainTarot main2=couleurs.get(couleur2);
				int nombre_figures2=nombreDeFigures[couleur2-2];
				if(main1.carte(0).valeur()>13&&main2.carte(0).valeur()<14)
				{
					main.echanger(c,b);
				}
				else if(main1.carte(0).valeur()<14&&main2.carte(0).valeur()<14)
				{
					if(c1.valeur()<c2.valeur())
					{
						main.echanger(c,b);
					}
					else if(c1.valeur()==c2.valeur())
					{
						if(nombre_figures1>nombre_figures2)
						{
							main.echanger(c,b);
						}
						else if(nombre_figures1==nombre_figures2)
						{
							if(main1.total()>main2.total())
							{
								main.echanger(c,b);
							}
						}
					}
				}
				else if(main1.carte(0).valeur()>13&&main2.carte(0).valeur()>13)
				{/*Puisque les deux cartes main1.carte(0) et main2.carte(0) sont des rois et que les cartes c1 et c2 sont ecartables,
				alors il existe une autre carte dans la couleur de main1 et une autre carte dans la couleur de main2*/
					if(main1.carte(1).valeur()>12&&main2.carte(1).valeur()<13)
					{
						main.echanger(c,b);
					}
					else if(main1.carte(1).valeur()<13&&main2.carte(1).valeur()<13)
					{
						if(nombre_figures1>nombre_figures2)
						{
							main.echanger(c,b);
						}
						else if(nombre_figures1==nombre_figures2)
						{
							if(c1.valeur()<c2.valeur())
							{
								main.echanger(c,b);
							}
							else if(c1.valeur()==c2.valeur())
							{
								if(main1.total()>main2.total())
								{
									main.echanger(c,b);
								}
							}
						}
					}
					else if(main1.carte(1).valeur()>12&&main2.carte(1).valeur()>12)
					{
						int position1=main1.position(c1);
						int position2=main2.position(c2);
						if(c1.valeur()+position1>13&&c2.valeur()+position2<14)
						{
							main.echanger(c,b);
						}
						else if(c1.valeur()+position1>13&&c2.valeur()+position2>13)
						{
							if(main1.total()>main2.total())
							{
								main.echanger(c,b);
							}
						}
						else if(c1.valeur()+position1<14&&c2.valeur()+position2<14)
						{
							if(c1.valeur()<c2.valeur())
							{
								main.echanger(c,b);
							}
							else if(c1.valeur()==c2.valeur())
							{
								if(nombre_figures1<nombre_figures2)
								{
									main.echanger(c,b);
								}
								else if(nombre_figures1==nombre_figures2)
								{
									if(main1.total()>main2.total())
									{
										main.echanger(c,b);
									}
								}
							}
						}
					}
				}
			}
		}
	}
	/**Retourne l'ensemble des cartes non maitresses dans les couleurs non maitresses autre que l'atout*/
	private static MainTarot cartesNonMaitresses(Vector<MainTarot> couleurs)
	{
		MainTarot nb=new MainTarot();
		for (byte i=2;i<6;i++) {
			Main cartes=couleurs.get(i);
			if(!MaitreDansUneCouleur(couleurs,i))
				for(byte j=0;j<cartes.total();j++)
				{
					if(cartes.carte(j).valeur()+j!=14)
						nb.ajouter(cartes.carte(j));
				}
		}
		return nb;
	}
	/**Retourne l'ensemble des cartes non maitresses dans les couleurs non maitresses autre que l'atout et pseudo maitres a la couleur appelee*/
	private MainTarot cartesNonPseudoMaitresses(Vector<MainTarot> couleurs)
	{
		MainTarot nb=new MainTarot();
		for (byte couleur=2;couleur<6;couleur++) {
			Main cartes=couleurs.get(couleur);
			if(!pseudoMaitreDansUneCouleurEcart(couleurs,couleur)&&couleur==carteAppelee.couleur())
				for(byte j=0;j<cartes.total();j++)
				{
					if(cartes.carte(j).valeur()+j!=13)
						nb.ajouter(cartes.carte(j));
				}
			else if(!MaitreDansUneCouleur(couleurs,couleur))
				for(byte j=0;j<cartes.total();j++)
				{
					if(cartes.carte(j).valeur()+j!=14)
						nb.ajouter(cartes.carte(j));
				}
		}
		return nb;
	}
	private static boolean pseudoMaitreDansUneCouleurEcart(Vector<MainTarot> couleurs,byte noCouleur)
	{
		if(couleurs.get(noCouleur).estVide())
			return false;
		if(MaitreDansUneCouleur(couleurs,noCouleur))
			return true;
		return pseudoMaitreDansUneCouleur(couleurs,noCouleur);
	}
	private static boolean pseudoMaitreDansUneCouleur(Vector<MainTarot> couleurs,byte noCouleur)
	{
		Main couleur=couleurs.get(noCouleur);
		if(couleur.carte(0).valeur()<9)
		{
			return false;
		}
		if(!couleur.contient(new CarteTarot(noCouleur,(byte)14)))
		{
			for (int i=0;i<couleur.total()&&couleur.carte(i).valeur()>8;i++)
				if(couleur.carte(i).valeur()+i!=13)
					return false;
			return true;
		}
		if(!couleur.contient(new CarteTarot(noCouleur,(byte)13)))
		{
			for (int i=1;i<couleur.total()&&couleur.carte(i).valeur()>8;i++)
				if(couleur.carte(i).valeur()+i!=13)
					return false;
			return true;
		}
		if(!couleur.contient(new CarteTarot(noCouleur,(byte)12)))
		{
			for (int i=2;i<couleur.total()&&couleur.carte(i).valeur()>8;i++)
				if(couleur.carte(i).valeur()+i!=13)
					return false;
			return true;
		}
		return false;
	}
	private static boolean MaitreDansUneCouleur(Vector<MainTarot> couleurs,byte noCouleur)
	{
		int nombre_cartes_maitresses=nbCartesMaitresses(couleurs,noCouleur);
		if(nombre_cartes_maitresses==couleurs.get(noCouleur).total())
			return true;
		if(nombre_cartes_maitresses>6)
			return true;
		if(nombre_cartes_maitresses+couleurs.get(noCouleur).total()>14)
			return true;
		if(nombre_cartes_maitresses+couleurs.get(noCouleur).total()>13&&!couleurs.get(0).estVide())
			return true;
		return false;
	}
	private static int nbCartesMaitresses(Vector<MainTarot> couleurs,byte noCouleur)
	{
		int nb=0;
		Main couleur=couleurs.get(noCouleur);
		for (int i=0;i<couleur.total();i++) {
			if(couleur.carte(i).valeur()+i==14)
				nb++;
		}
		return nb;
	}
	private static void classerCartesBasses(Vector<MainTarot> couleurs,Vector<Byte> nocouleurs)
	{
		for(int b=0;b<nocouleurs.size();b++)
		{
			for(int c=b+1;c<nocouleurs.size();c++)
			{
				byte couleur1=nocouleurs.get(b);
				MainTarot main1=couleurs.get(couleur1);
				byte couleur2=nocouleurs.get(c);
				MainTarot main2=couleurs.get(couleur2);
				if(main1.carte(0).valeur()>13&&main2.carte(0).valeur()<14)
				{
					nocouleurs.setElementAt(couleur2,b);
					nocouleurs.setElementAt(couleur1,c);
				}
				else if((main1.carte(0).valeur()>13)==(main2.carte(0).valeur()>13))
				{
					if(main1.total()>main2.total())
					{
						nocouleurs.setElementAt(couleur2,b);
						nocouleurs.setElementAt(couleur1,c);
					}
					else if(main1.total()==main2.total())
					{
						int taille=main2.total();
						for(int i=0;i<taille;i++)
						{
							if(main1.carte(i).valeur()<main2.carte(i).valeur())
							{
								break;
							}
							else if(main1.carte(i).valeur()>main2.carte(i).valeur())
							{
								nocouleurs.setElementAt(couleur2,b);
								nocouleurs.setElementAt(couleur1,c);
								break;
							}
						}
					}
				}
			}
		}
	}
	private void choisirPremiereCoupeFranche(int tailleChien,byte joueurs,Vector<MainTarot> couleurs,Vector<Byte> nocouleurscourtes)
	{
		if(joueurs<5)
		{
			for(byte couleur=2;couleur<6;couleur++)
			{
				if(couleurs.get(couleur).total()<=tailleChien&&couleurs.get(couleur).carte(0).valeur()<14||couleurs.get(couleur).total()==tailleChien+1&&couleurs.get(couleur).carte(0).valeur()>13)
				{
					nocouleurscourtes.addElement(couleur);
				}
			}
		}
		else
		{
			for(byte couleur=2;couleur<6;couleur++)
			{
				if(couleurs.get(couleur).total()<=tailleChien&&couleurs.get(couleur).carte(0).valeur()<14)
				{
					nocouleurscourtes.addElement(couleur);
				}
			}
		}
		for(int b=1;b<nocouleurscourtes.size();b++)
		{
			byte couleur1=nocouleurscourtes.get(0);
			byte couleur2=nocouleurscourtes.get(b);
			MainTarot main1=couleurs.get(couleur1);
			MainTarot main2=couleurs.get(couleur2);
			if(main1.carte(0).valeur()>13&&main2.carte(0).valeur()<14)
			{
				byte clr=couleur1;
				nocouleurscourtes.setElementAt(couleur2,0);
				nocouleurscourtes.setElementAt(clr,b);
			}
			else if(main1.carte(0).valeur()<14&&main2.carte(0).valeur()<14)
			{
				if(carteAppelee!=null)
				{
					if(main1.carte(0).couleur()==carteAppelee.couleur())
					{//couleur1 est appelee donc couleur2 ne l'est pas par unicite de la couleur appelee
						byte clr=couleur1;
						nocouleurscourtes.setElementAt(couleur2,0);
						nocouleurscourtes.setElementAt(clr,b);
					}
					else if(main2.carte(0).couleur()!=carteAppelee.couleur())
					{//Aucune des deux couleurs n'est appelee
						int nombre_figures1=main1.nombreDeFigures(couleur1);
						int nombre_figures2=main1.nombreDeFigures(couleur2);
						if(nombre_figures1<nombre_figures2)
						{//On veut sauvegarder beaucoup de points dans le chien
							byte clr=couleur1;
							nocouleurscourtes.setElementAt(couleur2,0);
							nocouleurscourtes.setElementAt(clr,b);
						}
						else if(nombre_figures1==nombre_figures2)
						{
							if(main1.total()>main2.total())
							{//On ecarte une couleur courte
								byte clr=couleur1;
								nocouleurscourtes.setElementAt(couleur2,0);
								nocouleurscourtes.setElementAt(clr,b);
							}
							else if(main1.total()==main2.total())
							{
								for(int k=0;k<main2.total();k++)
								{
									if(main2.carte(k).valeur()>10)
									{
										if(main1.carte(k).valeur()<main2.carte(k).valeur())
										{
											byte clr=couleur2;
											nocouleurscourtes.setElementAt(couleur1,b);
											nocouleurscourtes.setElementAt(clr,0);
											break;
										}
										else if(main1.carte(k).valeur()>main2.carte(k).valeur())
											break;
									}
									else
									{
										if(main1.carte(k).valeur()>main2.carte(k).valeur())
										{
											byte clr=couleur2;
											nocouleurscourtes.setElementAt(couleur1,b);
											nocouleurscourtes.setElementAt(clr,0);
											break;
										}
										else if(main1.carte(k).valeur()<main2.carte(k).valeur())
											break;
									}
								}
							}
						}
					}
				}
				else
				{
					int nombre_figures1=main1.nombreDeFigures(couleur1);
					int nombre_figures2=main1.nombreDeFigures(couleur2);
					if(nombre_figures1<nombre_figures2)
					{//On veut sauvegarder beaucoup de points dans le chien
						byte clr=couleur1;
						nocouleurscourtes.setElementAt(couleur2,0);
						nocouleurscourtes.setElementAt(clr,b);
					}
					else if(nombre_figures1==nombre_figures2)
					{
						if(main1.total()>main2.total())
						{//On ecarte une couleur courte
							byte clr=couleur1;
							nocouleurscourtes.setElementAt(couleur2,0);
							nocouleurscourtes.setElementAt(clr,b);
						}
						else if(main1.total()==main2.total())
						{
							for(int k=0;k<main2.total();k++)
							{
								if(main2.carte(k).valeur()>10)
								{
									if(main1.carte(k).valeur()<main2.carte(k).valeur())
									{
										byte clr=couleur2;
										nocouleurscourtes.setElementAt(couleur1,b);
										nocouleurscourtes.setElementAt(clr,0);
										break;
									}
									else if(main1.carte(k).valeur()>main2.carte(k).valeur())
										break;
								}
								else
								{
									if(main1.carte(k).valeur()>main2.carte(k).valeur())
									{
										byte clr=couleur2;
										nocouleurscourtes.setElementAt(couleur1,b);
										nocouleurscourtes.setElementAt(clr,0);
										break;
									}
									else if(main1.carte(k).valeur()<main2.carte(k).valeur())
										break;
								}
							}
						}
					}
				}
			}
			else if(main1.carte(0).valeur()>13&&main2.carte(0).valeur()>13)
			{
				if(main1.total()>main2.total())
				{
					byte clr=couleur1;
					nocouleurscourtes.setElementAt(couleur2,0);
					nocouleurscourtes.setElementAt(clr,b);
				}
				else if(main1.total()==main2.total())
				{
					int nombre_figures1=main1.nombreDeFigures(couleur1);
					int nombre_figures2=main1.nombreDeFigures(couleur2);
					if(nombre_figures1<nombre_figures2)
					{
						byte clr=couleur1;
						nocouleurscourtes.setElementAt(couleur2,0);
						nocouleurscourtes.setElementAt(clr,b);
					}
					else if(nombre_figures1==nombre_figures2)
					{
						for(int k=0;k<main2.total();k++)
						{
							if(main2.carte(k).valeur()>10)
							{
								if(main1.carte(k).valeur()<main2.carte(k).valeur())
								{
									byte clr=couleur2;
									nocouleurscourtes.setElementAt(couleur1,b);
									nocouleurscourtes.setElementAt(clr,0);
									break;
								}
								else if(main1.carte(k).valeur()>main2.carte(k).valeur())
									break;
							}
							else
							{
								if(main1.carte(k).valeur()>main2.carte(k).valeur())
								{
									byte clr=couleur2;
									nocouleurscourtes.setElementAt(couleur1,b);
									nocouleurscourtes.setElementAt(clr,0);
									break;
								}
								else if(main1.carte(k).valeur()<main2.carte(k).valeur())
									break;
							}
						}
					}
				}
			}
		}
	}
	private void gerer_figures_et_faire_coupe(int tailleChien,Vector<Byte> couleursCourteMaxFigures,Vector<MainTarot> repartition,Vector<MainTarot> rep_ecartables,byte couleur_appelee,MainTarot ecartables,MainTarot ecart)
	{
		MainTarot figures;
		byte coupe_franche=couleursCourteMaxFigures.get(0);
		for(int i=0;i<repartition.get(coupe_franche).total();i++)
		{
			ecart.ajouter(repartition.get(coupe_franche).carte(repartition.get(coupe_franche).total()-1-i));
			if(ecart.total()==tailleChien)
			{
				return;
			}
		}
		if(carteAppelee==null||repartition.get(couleur_appelee).contient(carteAppelee)||carteAppelee.valeur()!=14)
			figures=figuresNAppartenantPas(rep_ecartables,new byte[]{coupe_franche});
		else
			figures=figuresNAppartenantPas(rep_ecartables,new byte[]{couleur_appelee,coupe_franche});
		classerFigures(repartition,figures);
		for(Carte figure:figures)
		{
			ecart.ajouter(figure);
			if(ecart.total()==tailleChien)
			{
				return;
			}
		}
		ecartables.supprimerCartes(figures);
		ecartables.supprimerCartes(repartition.get(coupe_franche));
		couleursCourteMaxFigures=new Vector<Byte>();
		rep_ecartables=ecartables.couleurs();
		for(byte couleur=2;couleur<6;couleur++)
			if(!rep_ecartables.get(couleur).estVide()&&(carteAppelee==null||couleur_appelee!=couleur))
				couleursCourteMaxFigures.addElement(couleur);
		classerCartesBasses(rep_ecartables,couleursCourteMaxFigures);
		for(byte couleur:couleursCourteMaxFigures)
		{
			Main mt=rep_ecartables.get(couleur);
			for(int j=0;j<mt.total();j++)
			{
				ecart.ajouter(mt.carte(mt.total()-1-j));
				if(ecart.total()==tailleChien)
					return;
			}
		}
		for(int i=0;i<rep_ecartables.get(couleur_appelee).total();i++)
		{
			ecart.ajouter(rep_ecartables.get(couleur_appelee).carte(rep_ecartables.get(couleur_appelee).total()-1-i));
			if(ecart.total()==tailleChien)
				return;
		}
	}
	private void sauvetage_figures(int tailleChien,Vector<MainTarot> repartition,Vector<MainTarot> rep_ecartables,byte couleur_appelee,MainTarot ecartables,MainTarot ecart)
	{
		MainTarot figures;
		Vector<Byte> couleursCourteMaxFigures=new Vector<Byte>();
		if(carteAppelee==null||repartition.get(couleur_appelee).contient(carteAppelee)||carteAppelee.valeur()!=14)
			figures=figuresNAppartenantPas(rep_ecartables,new byte[]{});//On recupere toutes les figures ecartables
		else
			figures=figuresNAppartenantPas(rep_ecartables,new byte[]{couleur_appelee});
		//On recupere toutes les figures ecartables sauf celle qui est appelee
		classerFigures(repartition,figures);
		for(Carte figure:figures)
		{
			ecart.ajouter(figure);
			if(ecart.total()==tailleChien)
			{
				return;
			}
		}
		ecartables.supprimerCartes(figures);
		rep_ecartables=ecartables.couleurs();
		for(byte couleur=2;couleur<6;couleur++)
			if(!rep_ecartables.get(couleur).estVide()&&(carteAppelee==null||couleur_appelee!=couleur))
				couleursCourteMaxFigures.addElement(couleur);
		classerCartesBasses(rep_ecartables,couleursCourteMaxFigures);
		for(byte couleur:couleursCourteMaxFigures)
		{
			Main mt=rep_ecartables.get(couleur);
			for(int j=0;j<mt.total();j++)
			{
				ecart.ajouter(mt.carte(mt.total()-1-j));
				if(ecart.total()==tailleChien)
				{
					return;
				}
			}
		}
		for(int i=0;i<rep_ecartables.get(couleur_appelee).total();i++)
		{//Sauver son Petit est tres important
			ecart.ajouter(rep_ecartables.get(couleur_appelee).carte(rep_ecartables.get(couleur_appelee).total()-1-i));
			if(ecart.total()==tailleChien)
			{
				return;
			}
		}
	}
	/**Est vrai si et seulement si un chelem est annonce*/
	public boolean annoncesInitialisees()
	{
		boolean contient_chelem=contrat.force()==5;
		for(Vector<Annonce> annonces_joueur:annonces)
		{
			contient_chelem|=annonces_joueur.contains(new Annonce(Primes.Chelem));
		}
		return contient_chelem;
	}
	public void setAnnonces(byte joueur,Vector<Annonce> ann)
	{
		annonces.setElementAt(ann,joueur);
	}
	public Vector<Annonce> getAnnonces(byte numero)
	{
		return annonces.get(numero);
	}
	public void ajouterAnnonces(byte b,Vector<Annonce> ann)
	{
		annonces.get(b).addAll(ann);
	}
	/**Appele au debut d'un pli mais pas d'une partie, celui qui ramasse entame le pli suivant*/
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
		pliEnCours=new Pli(new MainTarot(),entameur);
	}
	public Carte strategieJeuCarteUnique(String[] raison)
	{
		if(pliEnCours.estVide())
		{
			return entame(raison);
		}
		if(pliEnCours.total()<getNombreDeJoueurs()-1)
		{
			return en_cours(raison);
		}
		return fin(raison);
	}
	private Carte entame_sur_excuse(String[] raison,Vector<Byte> joueurs_pouvant_jouer,MainTarot cartes_jouables,Vector<Pli> plisFaits)
	{
		byte nombre_joueurs=getNombreDeJoueurs();
		byte numero=(byte)((entameur+pliEnCours.total())%nombre_joueurs);
		MainTarot main_joueur=(MainTarot)getDistribution().main(numero);
		MainTarot cartesJouees=cartesJouees(numero);
		cartesJouees.ajouterCartes(pliEnCours.getCartes());
		Vector<MainTarot> repartitionCartesJouees=cartesJouees.couleurs();
		byte couleurAppelee=couleurAppelee();
		Vector<MainTarot> repartition_couleurs=main_joueur.couleurs();
		boolean carteAppeleeJouee=carteAppeleeJouee();
		Vector<Byte> joueurs_joue=new Vector<Byte>();
		for(byte joueur=0;joueur<getNombreDeJoueurs();joueur++)
		{
			if(!joueurs_pouvant_jouer.contains(joueur)&&joueur!=numero)
				joueurs_joue.addElement(joueur);
		}
		Vector<Vector<MainTarot>> cartes_possibles=cartesPossibles(!repartitionCartesJouees.get(0).estVide(),repartitionCartesJouees,plisFaits,false,main_joueur.couleurs(), numero);
		Vector<Vector<MainTarot>> cartes_certaines=cartesCertaines(cartes_possibles);
		Vector<MainTarot> repartition_jou=cartes_jouables.couleurs();
		Vector<Vector<MainTarot>> suites_toute_couleur=new Vector<Vector<MainTarot>>();
		for(MainTarot main:repartition_couleurs)
		{
			suites_toute_couleur.addElement(main.eclater(repartitionCartesJouees));
		}
		Vector<Byte> couleurs_strictes_maitresses=StrictCouleursMaitres(suites_toute_couleur, repartitionCartesJouees, cartes_possibles, numero);
		Vector<Byte> couleurs_maitresses=CouleursMaitres(suites_toute_couleur,repartitionCartesJouees,cartes_possibles,numero);
		MainTarot atouts_maitres=repartition_jou.get(1).atoutsMaitres(repartitionCartesJouees);
		boolean maitre_atout=MaitreAtout(suites_toute_couleur.get(1), repartitionCartesJouees,false);
		boolean maitre_jeu=maitre_atout&&couleurs_maitresses.size()==4;
		boolean carte_maitresse;
		Vector<MainTarot> cartes_rel_maitres;
		Vector<Byte> couleurs=new Vector<Byte>();
		boolean strict_maitre_atout=StrictMaitreAtout(cartes_possibles, numero, suites_toute_couleur.get(1), repartitionCartesJouees);
		byte indice_couleur_jouer=0;
		Vector<Byte> couleurs_pseudosmaitres=couleursPseudosMaitres(repartition_jou,cartesPseudosMaitresses(repartition_jou, repartitionCartesJouees));
		if(preneur>-1)
		{
			changerConfiance(plisFaits,numero,cartes_possibles,cartes_certaines,carteAppeleeJouee);
			if(cartes_jouables.total()==1)
			{
				raison[0]="C'est la seule carte a jouer";
				return cartes_jouables.carte(0);
			}
			/*Variables locales avec jeu d'equipe*/
			Vector<Byte> joueurs_de_confiance=joueursDeConfiance(numero);
			Vector<Byte> equipe_numero=new Vector<Byte>(joueurs_de_confiance);
			equipe_numero.addElement(numero);
			Vector<Byte> joueurs_de_non_confiance=joueursDeNonConfiance(numero);
			MainTarot cartesChien;
			Vector<Byte> joueurs_de_non_confiance_non_joue=new Vector<Byte>(joueurs_de_non_confiance);
			Vector<Byte> joueurs_de_confiance_non_joue=new Vector<Byte>(joueurs_de_confiance);
			joueurs_de_confiance_non_joue.retainAll(joueurs_pouvant_jouer);
			joueurs_de_non_confiance_non_joue.retainAll(joueurs_pouvant_jouer);
			if(maitre_jeu)
			{
				carte_maitresse=true;
				for(byte joueur:joueurs_de_non_confiance_non_joue)
				{
					carte_maitresse&=cartes_possibles.get(1).get(joueur).estVide();
				}
				if(carte_maitresse)
				{
					for(byte couleur=2;couleur<6;couleur++)
					{
						if(!repartition_jou.get(couleur).estVide())
						{
							indice_couleur_jouer=couleur;
							break;
						}
					}
					if(indice_couleur_jouer>0)
					{/*Si le joueur ayant joue l Excuse coupe la couleur dite, alors il est preferable de jouer une carte de couleur*/
						return repartition_jou.get(indice_couleur_jouer).carte(0);
					}
					/*Sinon le joueur cherche a ramasser les atouts adverses pour devenir maitre*/
					return repartition_jou.get(1).carte(0);
				}
				if(cartes_possibles.get(1).get(entameur).estVide())
				{
					if(!repartition_jou.get(1).estVide()&&repartition_jou.get(1).derniereCarte().valeur()>1)
					{/*Le joueur cherche a ramasser les atouts adverses pour devenir maitre s il possede un atout autre que le Petit*/
						return repartition_jou.get(1).carte(0);
					}
					for(byte couleur=2;couleur<6;couleur++)
					{
						if(!repartition_jou.get(couleur).estVide())
						{
							indice_couleur_jouer=couleur;
							break;
						}
					}
					if(indice_couleur_jouer>0)
					{/*Si le joueur ayant joue l Excuse coupe la couleur dite, alors il est preferable de jouer une carte de couleur*/
						return repartition_jou.get(indice_couleur_jouer).carte(0);
					}
					/*Par defaut*/
					return new CarteTarot((byte)1,(byte)1);
				}
				cartes_rel_maitres=cartesRelativementMaitreEntameExcuse(suites_toute_couleur.get(1), cartes_possibles, joueurs_pouvant_jouer, (byte)1);
				if(!cartes_rel_maitres.isEmpty())
				{/*On cherche a economiser les grand atouts tout en prenant la main*/
					return cartes_rel_maitres.lastElement().carte(0);
				}
			}
			if(main_joueur.total()==repartition_jou.get(1).total())
			{/*Le joueur cherche a ramasser les atouts adverses pour devenir maitre par le plus grand atout*/
				if(!atouts_maitres.estVide())
				{
					return atouts_maitres.carte(0);
				}
				return atoutDansLaSuiteLongueForte(suites_toute_couleur.get(1));
			}
			if(couleurs_strictes_maitresses.size()==3&&couleurs_pseudosmaitres.contains(couleurAppelee))
			{
				if(strict_maitre_atout)
				{
					if(!repartition_jou.get(1).estVide()&&repartition_jou.get(1).carte(0).valeur()>1)
						/*Le joueur cherche a ramasser les atouts adverses pour devenir maitre s il possede un atout autre que le Petit*/
						return repartition_jou.get(1).carte(0);
					if(!a_pour_defenseur(numero))
					{
						if(carteAppelee.valeur()<14&&!repartitionCartesJouees.get(couleurAppelee).contient(carteAppelee))
							return suites_toute_couleur.get(couleurAppelee).get(1).carte(0);
						/*La carte appelee etant un roi, un attaquant ne peut pas perdre sa meilleure carte au profir de la defense*/
						return repartition_jou.get(couleurAppelee).carte(0);
					}
				}
			}
			if(joueurs_de_non_confiance_non_joue.isEmpty())
			{
				carte_maitresse=false;
				for(byte couleur=2;couleur<6;couleur++)
				{
					carte_maitresse|=peut_couper(couleur,entameur,cartes_possibles);
				}
				if(carte_maitresse)
				{/*Si l entameur peut couper une des couleurs*/
					carte_maitresse=false;
					for(byte couleur=2;couleur<6;couleur++)
					{
						if(!repartitionCartesJouees.get(couleur).nombreFiguresEgal(4))
						{
							carte_maitresse|=peut_couper(couleur,entameur,cartes_possibles);
						}
					}
					for(byte couleur=2;couleur<6;couleur++)
					{
						if(!repartition_jou.get(couleur).estVide())
						{
							couleurs.addElement(couleur);
						}
					}
					if(carte_maitresse)
					{/*Sauver une figure sur l Excuse de maniere certaine avec tous les autres partenaires, car c est le cas ou l entameur est contre les autres*/
						return jouer_figure_entame_excuse(cartes_possibles,couleurs,joueurs_de_confiance_non_joue,repartition_jou,suites_toute_couleur,repartitionCartesJouees);
					}
					/*Sauver une figure sur l Excuse de maniere certaine avec tous les autres partenaires, car c est le cas ou l entameur est contre les autres*/
					return jouer_figure_entame_excuse(cartes_possibles,couleurs,joueurs_de_confiance_non_joue,repartition_jou,suites_toute_couleur);
				}
				if(repartition_jou.get(1).derniereCarte().valeur()==1&&!cartes_possibles.get(1).get(entameur).estVide())
				{/*Sauver son Petit, pour eviter que l entameur le prenne*/
					return new CarteTarot((byte)1,(byte)1);
				}
				carte_maitresse=false;
				for(byte couleur=2;couleur<6;couleur++)
				{
					carte_maitresse|=!repartitionCartesJouees.get(couleur).nombreFiguresEgal(4);
				}
				if(carte_maitresse)
				{/*Sauver une figure sur l Excuse de maniere certaine avec tous les autres partenaires, car c est le cas ou l entameur est contre les autres*/
					return jouer_figure_entame_excuse(cartes_possibles,couleurs,joueurs_de_confiance_non_joue,repartition_jou,suites_toute_couleur,repartitionCartesJouees);
				}
				/*Sauver une figure sur l Excuse de maniere certaine avec tous les autres partenaires, car c est le cas ou l entameur est contre les autres*/
				return jouer_petite_entame_excuse(cartes_possibles, couleurs, joueurs_de_confiance_non_joue, repartition_jou, suites_toute_couleur);
			}
			/*IL existe un joueur de non confiance n'ayant pas joue*/
			carte_maitresse=true;
			for(byte joueur:joueurs_de_non_confiance_non_joue)
			{
				carte_maitresse&=cartes_possibles.get(1).get(joueur).estVide();
			}
			if(carte_maitresse)
			{/*Si tous les joueurs de non confiance ne peuvent avoir de l'atout*/
				for(byte couleur=2;couleur<6;couleur++)
				{
					boolean local=!repartition_jou.get(couleur).estVide();
					for(byte joueur:joueurs_de_non_confiance_non_joue)
					{
						local&=cartes_possibles.get(couleur).get(joueur).estVide();
					}
					if(local)
					{
						couleurs.addElement(couleur);
					}
				}
				if(!couleurs.isEmpty())
				{
					carte_maitresse=false;
					for(byte couleur=2;couleur<6;couleur++)
					{
						carte_maitresse|=!repartitionCartesJouees.get(couleur).nombreFiguresEgal(4);
					}
					if(carte_maitresse)
					{
						return jouer_figure_entame_excuse(cartes_possibles,couleurs,joueurs_de_confiance_non_joue,repartition_jou,suites_toute_couleur,repartitionCartesJouees);
					}
					return jouer_petite_entame_excuse(cartes_possibles, couleurs, joueurs_de_confiance_non_joue, repartition_jou, suites_toute_couleur);
				}
				if(repartition_jou.get(1).derniereCarte().valeur()==1&&!cartes_possibles.get(1).get(entameur).estVide()&&joueurs_de_non_confiance.contains(entameur))
				{
					return new CarteTarot((byte)1,(byte)1);
				}
				Vector<Vector<MainTarot>> cartes_relat_mai_local=new Vector<Vector<MainTarot>>();
				for(byte couleur=2;couleur<6;couleur++)
				{
					Vector<MainTarot> local=cartesRelativementMaitreEntameExcuse(suites_toute_couleur.get(couleur), cartes_possibles, joueurs_pouvant_jouer, couleur);
					if(!local.isEmpty())
					{
						cartes_relat_mai_local.addElement(local);
						couleurs.addElement(couleur);
					}
				}
				if(!cartes_relat_mai_local.isEmpty())
				{
					return jouer_carte_rel_maitre_entame_excuse(cartes_possibles, couleurs, joueurs_pouvant_jouer, repartition_jou, repartitionCartesJouees, cartes_relat_mai_local);
				}
				return jouer_petite_entame_excuse(cartes_possibles, couleurs, joueurs_de_confiance_non_joue, repartition_jou, suites_toute_couleur);
			}
			if(joueurs_de_non_confiance_non_joue.containsAll(joueurs_pouvant_jouer))
			{
				cartesChien=cartesVuesAuChien();
				if(numero==appele)
				{
					couleurs=new Vector<Byte>();
					for(byte couleur=2;couleur<6;couleur++)
					{
						if(!repartition_jou.get(couleur).estVide())
						{
							for(byte joueur=0;joueur<nombre_joueurs;joueur++)
							{
								if(joueur!=numero&&joueur!=preneur&&joueurs_pouvant_jouer.contains(joueur))
								{
									if(peut_couper(couleur,joueur,cartes_possibles)&&!couleurs.contains(couleur))
									{
										couleurs.addElement(couleur);
									}
								}
							}
						}
					}
					if(!carteAppeleeJouee)
						/*L appele ne joue pas la couleur appelee pour ne pas se devoiler*/
						couleurs.remove((Object)couleurAppelee);
					if(!couleurs.isEmpty())
						/*L appele aide le preneur a faire couper la defense*/
						return faireCouperAdversaires(suites_toute_couleur,repartition_jou,couleurs,repartitionCartesJouees);
					Vector<Byte> couleurs2=couleursNonOuvertes(plisFaits);
					couleurs=new Vector<Byte>();
					for(byte couleur=2;couleur<6;couleur++)
						if(!repartition_jou.get(couleur).estVide()&&!couleurs2.contains(couleur))
							couleurs.addElement(couleur);
					if(!carteAppeleeJouee)
						/*L appele ne joue pas la couleur appelee pour ne pas se devoiler*/
						couleurs.remove((Object)couleurAppelee);
					if(!couleurs.isEmpty())
						/*L appele rejoue des couleurs deja ouvertes pour ne pas faciliter le sauvetage en defense*/
						return jouerCouleurNonAppelee(suites_toute_couleur,repartition_jou,couleurs,repartitionCartesJouees);
					if(!repartition_jou.get(1).estVide())
					{/*Place avant le preneur, l appele joue de l atout pour faire monter la defense, en evitant de jouer le Petit*/
						if(suites_toute_couleur.get(1).lastElement().total()>1||suites_toute_couleur.get(1).lastElement().carte(0).valeur()>1)
						{
							return suites_toute_couleur.get(1).lastElement().carte(0);
						}
						if(suites_toute_couleur.get(1).size()==1&&suites_toute_couleur.get(1).lastElement().carte(0).valeur()>1)
						{
							return suites_toute_couleur.get(1).lastElement().carte(0);
						}
						if(suites_toute_couleur.get(1).size()>1)
						{
							return suites_toute_couleur.get(1).get(suites_toute_couleur.get(1).size()-2).carte(0);
						}
					}
					if(!carteAppeleeJouee)
						/*L appele ne joue pas la couleur appelee pour ne pas se devoiler*/
						couleurs2.remove((Object)couleurAppelee);
					for(byte b=0;b<couleurs2.size();)
						if(repartition_jou.get(couleurs2.get(b)).estVide())
							couleurs2.removeElementAt(b);
						else
							b++;
					if(!couleurs2.isEmpty())
						/*L appele ne peut pas faire autrement que d ouvrir a une couleur non appelee ou de jouer la couleur appelee*/
						return ouvrirPreneur(suites_toute_couleur,repartition_jou,couleurs2,repartitionCartesJouees,cartesChien);
					if(couleurAppelee>-1&&!repartition_jou.get(couleurAppelee).estVide())
					{
						if(repartition_jou.get(couleurAppelee).derniereCarte().valeur()<11)
							/*L appele cherche a faire peur aux defenseurs en jouant une carte de la couleur appelee par la plus petite si ce n est pas une figure*/
							return repartition_jou.get(couleurAppelee).derniereCarte();
						/*Sinon il cherche a faire le pli avec une figure*/
						return repartition_jou.get(couleurAppelee).carte(0);
					}
				}
				/*Cas d'un defenseur*/
				if(preneur!=numero)
				{
					for(byte couleur=2;couleur<6;couleur++)
						if(!repartition_jou.get(couleur).estVide())
							couleurs.addElement(couleur);
					if(couleurAppelee>-1)
					{//Si la couleur appelee existe
						if(cartes_jouables.tailleCouleur(couleurAppelee)>0)
						{//Si le premier tour est joue et si le defenseur possede au moins une carte de la couleur appelee
							if(!carteAppeleeJouee&&tours(couleurAppelee, plisFaits).isEmpty())
							{//Un defenseur cherche a connaitre l'appele
								if(suites_toute_couleur.get(couleurAppelee).size()>1)
									/*Decouverte du suspense*/
									return suites_toute_couleur.get(couleurAppelee).lastElement().carte(0);
								for(byte b=0;b<suites_toute_couleur.get(couleurAppelee).get(0).total();b++)
								{
									if(suites_toute_couleur.get(couleurAppelee).get(0).carte(b).valeur()<11)
									{/*Decouverte du suspense*/
										return suites_toute_couleur.get(couleurAppelee).get(0).carte(b);
									}
								}
								/*Decouverte du suspense*/
								return suites_toute_couleur.get(couleurAppelee).get(0).derniereCarte();
							}
						}
					}
					couleurs=new Vector<Byte>();
					for(byte couleur=2;couleur<6;couleur++)
						if(!repartition_jou.get(couleur).estVide())
							couleurs.addElement(couleur);
					if(premierTour())
					{/*Premiere entame d'un defenseur*/
						if(couleurAppelee>-1)
							/*La couleur appelee au premier tour n est pas jouable pour un defenseur*/
							couleurs.remove((Object)couleurAppelee);
						/*Le defenseur ouvre a une couleur car il ne peut pas jouer la couleur appelee*/
						return jouerCouleurNonAppelee2(suites_toute_couleur,repartition_jou,couleurs,repartitionCartesJouees);
					}
					if(couleurAppelee>-1)
					{//Si la couleur appelee existe
						if(!tours(couleurAppelee, plisFaits).isEmpty())
						{/*Si la couleur appelee a ete deja jouee*/
							if(!carteAppeleeJouee&&!repartition_jou.get(couleurAppelee).estVide())
							{/*Mais pas la carte appelee et de plus ce defenseur peut entamer par la couleur appelee*/
								Vector<Byte> coupes=new Vector<Byte>();
								for(byte joueur=0;joueur<nombre_joueurs;joueur++)
									if(joueur!=preneur&&peut_couper(couleurAppelee, joueur, cartes_possibles)&&joueurs_pouvant_jouer.contains(joueur))
										coupes.addElement(joueur);
								if(!coupes.isEmpty()&&repartition_jou.get(couleurAppelee).carte(0).valeur()>10)
									/*Decouverte du suspense, en sauvant une figure sur coupe de la couleur appelee d un autre defenseur*/
									return repartition_jou.get(couleurAppelee).carte(0);
								if(repartition_jou.get(couleurAppelee).carte(0).valeur()>10)
									/*Decouverte du suspense*/
									return suites_toute_couleur.get(couleurAppelee).lastElement().derniereCarte();
								if(suites_toute_couleur.get(couleurAppelee).size()==1)
								{
									for(byte b=0;b<suites_toute_couleur.get(couleurAppelee).get(0).total();b++)
									{
										if(suites_toute_couleur.get(couleurAppelee).get(0).carte(b).valeur()<11)
										{/*Decouverte du suspense*/
											return suites_toute_couleur.get(couleurAppelee).get(0).carte(b);
										}
									}
									/*Decouverte du suspense*/
									return suites_toute_couleur.get(couleurAppelee).get(0).derniereCarte();
								}
								for(byte b=0;b<suites_toute_couleur.get(couleurAppelee).lastElement().total();b++)
								{
									if(suites_toute_couleur.get(couleurAppelee).lastElement().carte(b).valeur()<11)
									{/*Decouverte du suspense*/
										return suites_toute_couleur.get(couleurAppelee).lastElement().carte(b);
									}
								}
								/*Decouverte du suspense*/
								return suites_toute_couleur.get(couleurAppelee).lastElement().carte(0);
							}
						}
					}
					couleurs=couleursNonOuvertes(plisFaits);
					for(byte b=0;b<couleurs.size();)
						if(repartition_jou.get(couleurs.get(b)).estVide())
							couleurs.removeElementAt(b);
						else
							b++;
					if(!couleurs.isEmpty())
						/*Pour ne pas faciliter la prise de connaissance de jeu pour le preneur, un defenseur cherche a ouvrir s il est place avant le preneur*/
						return ouvrirPreneur2(suites_toute_couleur,repartition_jou,couleurs,repartitionCartesJouees,cartesChien);
					Vector<Byte> joueurs_non_confiance=joueursDeNonConfiance(numero);
					for(byte couleur=2;couleur<6;couleur++)
					{
						if(!repartition_jou.get(couleur).estVide())
						{
							cartes_rel_maitres=cartesRelativementMaitreEntameExcuse(suites_toute_couleur.get(couleur), cartes_possibles, joueurs_pouvant_jouer, couleur);
							for(byte joueur=0;joueur<nombre_joueurs;joueur++)
							{
								if(joueurs_non_confiance.contains(joueur)&&joueurs_pouvant_jouer.contains(joueur))
								{
									if(defausse(cartes_possibles, numero, couleur)&&!cartes_rel_maitres.get(couleur).estVide())
									{
										if(!couleurs.contains(couleur))
											couleurs.addElement(couleur);
									}
								}
							}
						}
					}
					for(byte couleur=0;couleur<couleurs.size();)
						if(!coupesNonConf(numero,couleurs.get(couleur),cartes_possibles, joueurs_pouvant_jouer).isEmpty())
							couleurs.removeElementAt(couleur);
						else
							couleur++;
					if(!couleurs.isEmpty())
						/*Si le defenseur a vu qu un des joueurs de non confiance se defausse sans que les autres coupent alors il fait comme s il etait l appele*/
						return carteDansCouleursAvecCartesMaitresses(repartition_jou,couleurs,repartitionCartesJouees);
					couleurs=new Vector<Byte>();
					Vector<Byte> joueurs_confiance=joueursDeConfiance(numero);
					for(byte couleur=2;couleur<6;couleur++)
						if(!repartition_jou.get(couleur).estVide()&&peut_couper(couleur,preneur,cartes_possibles))
						{
							boolean neCoupePas=true;
							for(byte joueur:joueurs_confiance)
								if(joueurs_pouvant_jouer.contains(joueur))
									neCoupePas&=!peut_couper(couleur,joueur,cartes_possibles);
							if(neCoupePas)
								couleurs.addElement(couleur);
						}
					if(!couleurs.isEmpty())
						/*Le defenseur cherche a faire couper les joueurs de non confiance sans faire couper ses partenaires*/
						return faireCouperAdversaires(suites_toute_couleur,repartition_jou,couleurs,repartitionCartesJouees);
					couleurs=new Vector<Byte>();
					for(byte couleur=2;couleur<6;couleur++)
						if(!repartition_jou.get(couleur).estVide()&&peut_couper(couleur,preneur,cartes_possibles)&&joueurs_pouvant_jouer.contains(preneur))
							couleurs.addElement(couleur);
					if(!couleurs.isEmpty())
						/*Le defenseur cherche a faire couper les joueurs de non confiance en faisant couper ses partenaires*/
						return faireCouperAdversaires(suites_toute_couleur,repartition_jou,couleurs,repartitionCartesJouees);
					couleurs=new Vector<Byte>();
					for(byte couleur=2;couleur<6;couleur++)
						if(!repartition_jou.get(couleur).estVide())
							couleurs.addElement(couleur);
					/*Le defenseur cherche a jouer une couleur comme si c etait le premier tour*/
					return jouerCouleurNonAppelee2(suites_toute_couleur,repartition_jou,couleurs,repartitionCartesJouees);
				}
			}
		}
		return entame(raison);
	}
	private static Vector<MainTarot> cartesRelativementMaitreEntameExcuse(Vector<MainTarot> suites,Vector<Vector<MainTarot>> cartes_possibles,Vector<Byte> joueurs_n_ayant_pas_joue,byte couleur_joueur)
	{
		byte max_valeur=0;
		Vector<MainTarot> suites_bis=new Vector<MainTarot>();
		for(byte joueur:joueurs_n_ayant_pas_joue)
		{
			if(!cartes_possibles.get(couleur_joueur).get(joueur).estVide())
			{
				if(cartes_possibles.get(couleur_joueur).get(joueur).carte(0).valeur()>max_valeur)
				{
					max_valeur=cartes_possibles.get(couleur_joueur).get(joueur).carte(0).valeur();
				}
			}
		}
		for(MainTarot suite:suites)
		{
			if(suite.carte(0).valeur()>max_valeur)
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
	/**Renvoie la carte la plus forte dans la couleur avec le moins de points joues, le nombre de defausses le plus grand, la plus longue, avec le moins grand nombre de coupes*/
	private Carte jouer_figure_entame_excuse(Vector<Vector<MainTarot>> cartes_possibles,Vector<Byte> couleurs,Vector<Byte> joueurs,Vector<MainTarot> repartition,Vector<Vector<MainTarot>> suites,Vector<MainTarot> repartition_cartes_jouees)
	{
		int nombre_points=0,nombre_points_2=0;
		for(byte indice_couleur=1;indice_couleur<couleurs.size();indice_couleur++)
		{
			nombre_points=0;
			nombre_points_2=0;
			byte couleur1=couleurs.get(0);
			byte couleur2=couleurs.get(indice_couleur);
			for(byte indice_carte=0;indice_carte<repartition_cartes_jouees.get(couleur1).total();indice_carte++)
			{
				if(repartition_cartes_jouees.get(couleur1).carte(indice_carte).valeur()>10)
				{
					nombre_points+=((CarteTarot)repartition_cartes_jouees.get(couleur1).carte(indice_carte)).points();
				}
				else
				{
					break;
				}
			}
			for(byte indice_carte=0;indice_carte<repartition_cartes_jouees.get(couleur2).total();indice_carte++)
			{
				if(repartition_cartes_jouees.get(couleur2).carte(indice_carte).valeur()>10)
				{
					nombre_points_2+=((CarteTarot)repartition_cartes_jouees.get(couleur2).carte(indice_carte)).points();
				}
				else
				{
					break;
				}
			}
			if(nombre_points>nombre_points_2)
			{
				couleurs.setElementAt(couleurs.set(0,couleur2),indice_couleur);
			}
			else if(nombre_points==nombre_points_2)
			{
				if(nombre_defausses(cartes_possibles,joueurs,couleur1)<nombre_defausses(cartes_possibles,joueurs,couleur2))
				{
					couleurs.setElementAt(couleurs.set(0,couleur2),indice_couleur);
				}
				else if(nombre_defausses(cartes_possibles,joueurs,couleur1)==nombre_defausses(cartes_possibles,joueurs,couleur2))
				{
					if(repartition.get(couleur1).total()<repartition.get(couleur2).total())
					{
						couleurs.setElementAt(couleurs.set(0,couleur2),indice_couleur);
					}
					else if(repartition.get(couleur1).total()==repartition.get(couleur2).total())
					{
						if(nombre_coupes(cartes_possibles,joueurs,couleur1)>nombre_coupes(cartes_possibles,joueurs,couleur2))
						{
							couleurs.setElementAt(couleurs.set(0,couleur2),indice_couleur);
						}
					}
				}
			}
		}
		if(repartition.get(couleurs.get(0)).carte(0).valeur()>10)
		{
			return repartition.get(couleurs.get(0)).carte(0);
		}
		return suites.get(couleurs.get(0)).lastElement().carte(0);
	}
	private Carte jouer_carte_rel_maitre_entame_excuse(Vector<Vector<MainTarot>> cartes_possibles,Vector<Byte> couleurs,Vector<Byte> joueurs,Vector<MainTarot> repartition,Vector<MainTarot> repartition_cartes_jouees,Vector<Vector<MainTarot>> cartes_rel_maitres)
	{
		int nombre_points=0,nombre_points_2=0;
		for(byte indice_couleur=1;indice_couleur<couleurs.size();indice_couleur++)
		{
			nombre_points=0;
			nombre_points_2=0;
			byte couleur1=couleurs.get(0);
			byte couleur2=couleurs.get(indice_couleur);
			for(byte indice_carte=0;indice_carte<repartition_cartes_jouees.get(couleur1).total();indice_carte++)
			{
				if(repartition_cartes_jouees.get(couleur1).carte(indice_carte).valeur()>10)
				{
					nombre_points+=((CarteTarot)repartition_cartes_jouees.get(couleur1).carte(indice_carte)).points();
				}
				else
				{
					break;
				}
			}
			for(byte indice_carte=0;indice_carte<repartition_cartes_jouees.get(couleur2).total();indice_carte++)
			{
				if(repartition_cartes_jouees.get(couleur2).carte(indice_carte).valeur()>10)
				{
					nombre_points_2+=((CarteTarot)repartition_cartes_jouees.get(couleur2).carte(indice_carte)).points();
				}
				else
				{
					break;
				}
			}
			if(nombre_points>nombre_points_2)
			{
				couleurs.setElementAt(couleurs.set(0,couleur2),indice_couleur);
			}
			else if(nombre_points==nombre_points_2)
			{
				if(nombre_defausses(cartes_possibles,joueurs,couleur1)<nombre_defausses(cartes_possibles,joueurs,couleur2))
				{
					couleurs.setElementAt(couleurs.set(0,couleur2),indice_couleur);
				}
				else if(nombre_defausses(cartes_possibles,joueurs,couleur1)==nombre_defausses(cartes_possibles,joueurs,couleur2))
				{
					if(repartition.get(couleur1).total()<repartition.get(couleur2).total())
					{
						couleurs.setElementAt(couleurs.set(0,couleur2),indice_couleur);
					}
					else if(repartition.get(couleur1).total()==repartition.get(couleur2).total())
					{
						if(nombre_coupes(cartes_possibles,joueurs,couleur1)>nombre_coupes(cartes_possibles,joueurs,couleur2))
						{
							couleurs.setElementAt(couleurs.set(0,couleur2),indice_couleur);
						}
					}
				}
			}
		}
		byte couleur=couleurs.get(0);
		int indice=0;
		for(;cartes_rel_maitres.get(indice).get(0).carte(0).couleur()!=couleur;indice++);
		return cartes_rel_maitres.get(indice).get(0).carte(0);
	}
	/**Renvoie la carte la plus forte dans la couleur avec le nombre de defausses le plus grand, la plus longue, avec le moins grand nombre de coupes*/
	private Carte jouer_figure_entame_excuse(Vector<Vector<MainTarot>> cartes_possibles,Vector<Byte> couleurs,Vector<Byte> joueurs,Vector<MainTarot> repartition,Vector<Vector<MainTarot>> suites)
	{
		for(byte indice_couleur=1;indice_couleur<couleurs.size();indice_couleur++)
		{
			byte couleur1=couleurs.get(0);
			byte couleur2=couleurs.get(indice_couleur);
			if(nombre_defausses(cartes_possibles,joueurs,couleur1)<nombre_defausses(cartes_possibles,joueurs,couleur2))
			{
				couleurs.setElementAt(couleurs.set(0,couleur2),indice_couleur);
			}
			else if(nombre_defausses(cartes_possibles,joueurs,couleur1)==nombre_defausses(cartes_possibles,joueurs,couleur2))
			{
				if(repartition.get(couleur1).total()<repartition.get(couleur2).total())
				{
					couleurs.setElementAt(couleurs.set(0,couleur2),indice_couleur);
				}
				else if(repartition.get(couleur1).total()==repartition.get(couleur2).total())
				{
					if(nombre_coupes(cartes_possibles,joueurs,couleur1)>nombre_coupes(cartes_possibles,joueurs,couleur2))
					{
						couleurs.setElementAt(couleurs.set(0,couleur2),indice_couleur);
					}
				}
			}
		}
		if(repartition.get(couleurs.get(0)).carte(0).valeur()>10)
		{
			return repartition.get(couleurs.get(0)).carte(0);
		}
		return suites.get(couleurs.get(0)).lastElement().carte(0);
	}
	private byte nombre_defausses(Vector<Vector<MainTarot>> cartes_possibles,Vector<Byte> joueurs,byte couleur)
	{
		byte nombre=0;
		for(byte joueur:joueurs)
		{
			if(defausse(cartes_possibles, joueur, couleur))
			{
				nombre++;
			}
		}
		return nombre;
	}
	private Carte jouer_petite_entame_excuse(Vector<Vector<MainTarot>> cartes_possibles,Vector<Byte> couleurs,Vector<Byte> joueurs,Vector<MainTarot> repartition,Vector<Vector<MainTarot>> suites)
	{
		for(byte indice_couleur=1;indice_couleur<couleurs.size();indice_couleur++)
		{
			byte couleur1=couleurs.get(0);
			byte couleur2=couleurs.get(indice_couleur);
			if(nombre_coupes(cartes_possibles,joueurs,couleur1)>nombre_coupes(cartes_possibles,joueurs,couleur2))
			{
				if(repartition.get(couleur1).total()<repartition.get(couleur2).total())
				{
					couleurs.setElementAt(couleurs.set(0,couleur2),indice_couleur);
				}
			}
			else if(nombre_coupes(cartes_possibles,joueurs,couleur1)==nombre_coupes(cartes_possibles,joueurs,couleur2))
			{
				couleurs.setElementAt(couleurs.set(0,couleur2),indice_couleur);
			}
		}
		return suites.get(couleurs.get(0)).lastElement().carte(0);
	}
	private byte nombre_coupes(Vector<Vector<MainTarot>> cartes_possibles,Vector<Byte> joueurs,byte couleur)
	{
		byte nombre=0;
		for(byte joueur:joueurs)
		{
			if(peut_couper(couleur, joueur, cartes_possibles))
			{
				nombre++;
			}
		}
		return nombre;
	}
	private Carte entame(String[] raison)
	{
		byte nombre_joueurs=getNombreDeJoueurs();
		byte numero=(byte)((entameur+pliEnCours.total())%nombre_joueurs);
		MainTarot main_joueur=(MainTarot)getDistribution().main(numero);
		Vector<MainTarot> repartition=main_joueur.couleurs();
		Main cartes_jouables=cartes_jouables(repartition);
		if(preneur==-1&&cartes_jouables.total()==1)
		{
			raison[0]="C'est la seule carte a jouer";
			return cartes_jouables.carte(0);
		}
		Vector<Byte> joueurs_non_joue=joueursNAyantPasJoue(numero);
		MainTarot cartesJouees=cartesJouees(numero);
		cartesJouees.ajouterCartes(pliEnCours.getCartes());
		Vector<MainTarot> repartitionCartesJouees=cartesJouees.couleurs();
		Vector<Vector<MainTarot>> suites=new Vector<Vector<MainTarot>>();
		Vector<Pli> plisFaits=unionPlis();
		for(int i=0;i<6;i++)
			suites.addElement(repartition.get(i).eclater(repartitionCartesJouees));
		boolean contientExcuse=!repartition.get(0).estVide();
		boolean carteAppeleeJouee=carteAppeleeJouee();
		Vector<Vector<MainTarot>> cartes_possibles=cartesPossibles(!repartitionCartesJouees.get(0).estVide(),repartitionCartesJouees,plisFaits,contientExcuse,repartition, numero);
		Vector<Vector<MainTarot>> cartes_certaines=cartesCertaines(cartes_possibles);
		boolean strict_maitre_atout=StrictMaitreAtout(cartes_possibles, numero, suites.get(1), repartitionCartesJouees);
		boolean maitre_atout=MaitreAtout(suites.get(1), repartitionCartesJouees, contientExcuse);
		Vector<Byte> couleurs_maitres=CouleursMaitres(suites, repartitionCartesJouees, cartes_possibles,numero);
		MainTarot atouts=repartition.get(1);
		int indice_couleur_jouer=0;
		MainTarot atouts_maitres=repartition.get(1).atoutsMaitres(repartitionCartesJouees);
		byte nombreDeJoueursPossedantAtout=nombre_joueurs;
		byte couleurAppelee=couleurAppelee();
		MainTarot cartesChien;
		Vector<Byte> couleurs_strictement_maitresses=StrictCouleursMaitres(suites,repartitionCartesJouees,cartes_possibles,numero);
		Vector<MainTarot> cartes_maitresses=cartesMaitresses(repartition,repartitionCartesJouees);
		MainTarot cartesNonMaitresses=cartesNonMaitresses(repartition,cartes_maitresses,suites);
		Vector<Byte> ramasseurs=ramasseurs(plisFaits);
		Vector<Byte> couleurs_pseudosmaitres=couleursPseudosMaitres(repartition,cartesPseudosMaitresses(repartition, repartitionCartesJouees));
		if(preneur>-1)
		{
			changerConfiance(plisFaits,numero,cartes_possibles,cartes_certaines,carteAppeleeJouee);
			if(cartes_jouables.total()==1)
			{
				raison[0]="C'est la seule carte a jouer";
				return cartes_jouables.carte(0);
			}
			cartesChien=cartesVuesAuChien();
			if(couleurs_maitres.size()==4)
			{/*Cas ou le joueur entameur deborde les autres joueurs deborde les autres joueurs en couleurs*/
				if(preneur==numero)
				{
					if(contientExcuse)
					{
						if(strict_maitre_atout)
						{/*Cas ou le joueur entameur deborde les autres joueurs en atout*/
							if(carteAppeleeJouee||main_joueur.contient(carteAppelee))
							{
								if(!adversaireAFaitPlis(numero))
								{
									if(!atouts.estVide()&&(atouts.total()!=1||atouts.carte(0).valeur()>1))
									{/*Jouer le plus grand atout pour etre tranquil*/
										return atouts.carte(0);
									}
									for(int couleur=2;couleur<6;couleur++)
									{
										if(!repartition.get(couleur).estVide())
										{
											indice_couleur_jouer=couleur;
											break;
										}
									}
									if(indice_couleur_jouer>0)
									{/*Jouer la plus grande carte a la couleur pour etre tranquil*/
										return repartition.get(indice_couleur_jouer).carte(0);
									}
									/*Emmener l Excuse au bout pour un chelem est possible,
									 * dans ce cas, le Petit joue a l avant dernier tour est considere comme un petit au bout*/
									return new CarteTarot((byte)1,(byte)1);
								}
								/*Se resigner a laisser un pli a l adversaire pour ne pas etre bloque avec l Excuse*/
								return new CarteTarot((byte)0);
							}
							if(preneurEtUnAutreJoueurFontTousLesPlis())
							{
								if(!atouts.estVide()&&(atouts.total()!=1||atouts.carte(0).valeur()>1))
								{/*Jouer le plus grand atout pour etre tranquil*/
									return atouts.carte(0);
								}
								for(int couleur=2;couleur<6;couleur++)
								{
									if(!repartition.get(couleur).estVide())
									{
										indice_couleur_jouer=couleur;
										break;
									}
								}
								if(indice_couleur_jouer>0)
								{/*Jouer la plus grande carte a la couleur pour etre tranquil*/
									return repartition.get(indice_couleur_jouer).carte(0);
								}
								//LE Petit est mene a l'avant dernier tour pour un grand chelem pour emmener l'Excuse au bout
								return new CarteTarot((byte)1,(byte)1);
							}
							/*Se resigner a laisser un pli a l adversaire pour ne pas etre bloque avec l Excuse*/
							return new CarteTarot((byte)0);
						}
						if(maitre_atout)
						{
							if(!atouts_maitres.estVide())
							{/*Jouer le plus grand atout pour etre tranquil*/
								return atouts_maitres.carte(0);
							}
							/*Devenir maitre a l atout*/
							return atoutDansLaSuiteLongueForte(suites.get(1));
						}
					}
					if(!repartition.get(1).estVide())
					{
						if(repartition.get(1).total()>1||repartition.get(1).derniereCarte().valeur()>1)
						{/*Le Petit n est pas le seul atout dans la main*/
							if(!atouts_maitres.estVide())
							{/*Jouer le plus grand atout pour etre tranquil*/
								return atouts_maitres.carte(0);
							}
							if(repartition.get(1).total()>1)
							{
								/*Defendre les figures en jouant des atouts*/
								return atoutDansLaSuiteLongueForte(suites.get(1));
							}
						}
					}
					for(int couleur=2;couleur<6;couleur++)
					{
						if(!repartition.get(couleur).estVide())
						{
							indice_couleur_jouer=couleur;
							break;
						}
					}
					if(indice_couleur_jouer>0)
					{
						/*Jouer la plus grande carte a la couleur pour etre tranquil*/
						return repartition.get(indice_couleur_jouer).carte(0);
					}
					return repartition.get(1).derniereCarte();
				}
				if(appele==numero)//Si il existe un appele autre que le preneur
				{
					if(contientExcuse)
					{
						if(strict_maitre_atout)
						{
							if(!adversaireAFaitPlis(numero))
							{
								if(!atouts.estVide()&&(atouts.total()!=1||atouts.carte(0).valeur()>1))
								{/*Jouer le plus grand atout pour etre tranquil*/
									return atouts.carte(0);
								}
								for(int couleur=2;couleur<6;couleur++)
								{
									if(!repartition.get(couleur).estVide())
									{
										indice_couleur_jouer=couleur;
										break;
									}
								}
								if(indice_couleur_jouer>0)
								{/*Jouer la plus grande carte a la couleur pour etre tranquil*/
									return repartition.get(indice_couleur_jouer).carte(0);
								}
								//LE Petit est mene a l'avant dernier tour pour un grand chelem pour emmener l'Excuse au bout
								return new CarteTarot((byte)1,(byte)1);
							}
							return new CarteTarot((byte)0);
						}
						if(maitre_atout)
						{
							if(!atouts_maitres.estVide())
								/*Jouer le plus grand atout pour etre tranquil*/
								return atouts_maitres.carte(0);
							/*Devenir maitre a l atout*/
							return atoutDansLaSuiteLongueForte(suites.get(1));
						}
					}
					if(!atouts_maitres.estVide())
						/*Jouer le plus grand atout pour etre tranquil*/
						return atouts_maitres.carte(0);
					if(!suites.get(1).isEmpty())
						/*Devenir maitre a l atout*/
						return atoutDansLaSuiteLongueForte(suites.get(1));
					for(int couleur=2;couleur<6;couleur++)
					{
						if(!repartition.get(couleur).estVide())
						{
							indice_couleur_jouer=couleur;
							break;
						}
					}
					/*Jouer la plus grande carte a la couleur pour etre tranquil*/
					return repartition.get(indice_couleur_jouer).carte(0);
				}
				if(contientExcuse)
				{
					if(strict_maitre_atout)
					{
						if(carteAppeleeJouee)
						{
							if(!adversaireAFaitPlis(numero))
							{
								if(!atouts.estVide()&&(atouts.total()!=1||atouts.carte(0).valeur()>1))
								{/*Jouer le plus grand atout pour etre tranquil*/
									return atouts.carte(0);
								}
								for(int couleur=2;couleur<6;couleur++)
								{
									if(!repartition.get(couleur).estVide())
									{
										indice_couleur_jouer=couleur;
										break;
									}
								}
								if(indice_couleur_jouer>0)
								{/*Jouer la plus grande carte a la couleur pour etre tranquil*/
									return repartition.get(indice_couleur_jouer).carte(0);
								}
								//LE Petit est mene a l'avant dernier tour pour un grand chelem pour emmener l'Excuse au bout
								return new CarteTarot((byte)1,(byte)1);
							}
							return new CarteTarot((byte)0);
						}
						if(ramasseurs.size()<nombre_joueurs-1&&!ramasseurs.contains(preneur))
						{
							if(!atouts.estVide()&&(atouts.total()!=1||atouts.carte(0).valeur()>1))
							{/*Jouer le plus grand atout pour etre tranquil*/
								return atouts.carte(0);
							}
							for(int couleur=2;couleur<6;couleur++)
							{
								if(!repartition.get(couleur).estVide())
								{
									indice_couleur_jouer=couleur;
									break;
								}
							}
							if(indice_couleur_jouer>0)
							{/*Jouer la plus grande carte a la couleur pour etre tranquil*/
								return repartition.get(indice_couleur_jouer).carte(0);
							}
							return new CarteTarot((byte)1,(byte)1);//LE Petit est mene a l'avant dernier tour pour un grand chelem pour emmener l'Excuse au bout
						}
						return new CarteTarot((byte)0);
					}
					if(maitre_atout)
					{
						if(!atouts_maitres.estVide())
							/*Jouer le plus grand atout pour etre tranquil*/
							return atouts_maitres.carte(0);
						/*Devenir maitre a l atout*/
						return atoutDansLaSuiteLongueForte(suites.get(1));
					}
				}
				if(!atouts_maitres.estVide())
					/*Jouer le plus grand atout pour etre tranquil*/
					return atouts_maitres.carte(0);
				if(!suites.get(1).isEmpty())
					/*Devenir maitre a l atout*/
					return atoutDansLaSuiteLongueForte(suites.get(1));
				for(int couleur=2;couleur<6;couleur++)
				{
					if(!repartition.get(couleur).estVide())
					{
						indice_couleur_jouer=couleur;
						break;
					}
				}
				/*Jouer la plus grande carte a la couleur pour etre tranquil*/
				return repartition.get(indice_couleur_jouer).carte(0);
			}/*Fin couleurs_maitres.size()==4*/
			if(estUnJeuDeChelem(repartition,getInfos(),nombre_joueurs,raison))
			{//On est au premier tour et le preneur possede un jeu de chelem avec au moins un atout
				return repartition.get(1).carte(0);
			}
			for(byte joueur:joueurs_non_joue)
				if(cartes_possibles.get(1).get(joueur).estVide())
					nombreDeJoueursPossedantAtout--;/*On enleve les joueurs n'ayant pas d'atout de maniere certaine*/
			byte nombre_couleurs_non_maitres=0;
			if(!couleurs_maitres.contains(couleurAppelee)&&couleurs_pseudosmaitres.contains(couleurAppelee))
			{
				nombre_couleurs_non_maitres++;
			}
			if(nombreDeJoueursPossedantAtout>2&&nbCouleursLargementMaitressesJeu(cartes_maitresses,couleurs_maitres,nombre_joueurs)+nombre_couleurs_non_maitres==4)
			{//si 3 joueurs dont l'entameur ont joue de l'atout, on continue a jouer de l'atout
				if(!atouts_maitres.estVide())
					/*Faire tomber les atouts a condition d avoir des atouts maitres et d avoir des couleurs presque maitresses*/
					return suites.get(1).get(0).carte(0);
			}
			if(couleurAppelee>1&&!repartitionCartesJouees.get(couleurAppelee).contient(carteAppelee))
			{
				if(couleurs_pseudosmaitres.contains(couleurAppelee))
				{
					if(couleurs_strictement_maitresses.size()==3)
					{
						if(strict_maitre_atout)
						{
							if(!repartition.get(1).estVide()&&repartition.get(1).carte(0).valeur()>1)
								/*Jouer le plus grand atout pour etre tranquil*/
								return repartition.get(1).carte(0);
							if(carteAppelee.valeur()>13)
								/*Pour connaitre l appele*/
								return repartition.get(couleurAppelee).carte(0);
							/*Pour ne pas se faire pieger apres*/
							return suites.get(couleurAppelee).get(1).carte(0);
						}
					}
					if(couleurs_strictement_maitresses.size()==2&&!couleurs_strictement_maitresses.contains(couleurAppelee))
					{
						Vector<Byte> couleurs=new Vector<Byte>();
						for(byte couleur=2;couleur<6;couleur++)
							couleurs.addElement(couleur);
						couleurs.removeAll(couleurs_strictement_maitresses);
						couleurs.remove((Object)couleurAppelee);
						/*On a maintenant couleurs.size()==1*/
						if(repartition.get(couleurs.get(0)).estVide())
						{/*couleurs a pour nombre total d element valant 1*/
							/*Dans ce cas il y a deux couleurs imprenables et eventuellement la couleur appelee et l atout*/
							if(strict_maitre_atout)
							{
								if(repartition.get(1).total()==2&&repartition.get(1).derniereCarte().valeur()>1||repartition.get(1).total()>2)
								{/*Au moins deux atouts autre que Petit + un atout*/
									return suites.get(1).get(0).carte(0);
								}
								if(repartition.get(1).total()==1&&repartition.get(1).derniereCarte().valeur()>1||repartition.get(1).total()==2&&repartition.get(1).derniereCarte().valeur()==1)
								{/*Un atout + eventuellement le Petit*/
									if(suites.get(couleurAppelee).get(0).total()+1==repartition.get(couleurAppelee).total())
									{/*Jouer atout pour devenir maitre a la couleur appele
									sans se retrouver avec le Petit pour ne pas se faire pieger*/
										return suites.get(1).get(0).carte(0);
									}
									return suites.get(couleurAppelee).get(1).carte(0);
								}
								byte couleurLongue=1;
								int max=0;
								for(byte couleur=2;couleur<6;couleur++)
								{
									if(repartition.get(couleur).total()>max)
									{
										max=repartition.get(couleur).total();
										couleurLongue=couleur;
									}
								}
								/*Jouer long pour devenir plus rapidement maitre*/
								return repartition.get(couleurLongue).carte(0);
							}
							if(maitre_atout)
							{
								if(!atouts_maitres.estVide())
									/*Eliminer les atouts adverses*/
									return atouts_maitres.carte(0);
								/*Devenir maitre a l atout*/
								return atoutDansLaSuiteLongueForte(suites.get(1));
							}
						}
					}
				}
			}
			if(repartition.get(0).total()+repartition.get(1).total()==main_joueur.total())
			{/*La main ne contient que de l atout (avec eventuellement l Excuse)*/
				if(!contientExcuse)
				{
					if(!atouts_maitres.estVide())
						/*Eliminer les atouts adverses*/
						return atouts_maitres.carte(0);
					/*Devenir maitre a l atout*/
					return atoutDansLaSuiteLongueForte(suites.get(1));
				}
				if(main_joueur.total()==2)
				{
					if(!adversaireAFaitPlis(numero))
					{/*Realiser un chelem en emmenant l Excuse au bout*/
						return repartition.get(1).carte(0);
					}
					/*Sinon ne pas perdre l Excuse*/
					return new CarteTarot((byte)0);
				}
				if(!atouts_maitres.estVide())
					/*Eliminer les atouts adverses*/
					return atouts_maitres.carte(0);
				/*Devenir maitre a l atout*/
				return atoutDansLaSuiteLongueForte(suites.get(1));
			}
			if(repartition.get(0).total()+repartition.get(1).total()+1==main_joueur.total())
			{
				for(byte b=0;b<couleurs_strictement_maitresses.size();)
					if(repartition.get(couleurs_strictement_maitresses.get(b)).estVide())
						couleurs_strictement_maitresses.removeElementAt(b);
					else
						b++;
				byte seule_couleur;
				if(couleurs_strictement_maitresses.size()==1)
				{
					seule_couleur=couleurs_strictement_maitresses.get(0);
					if(atouts_maitres.total()==repartition.get(1).total()&&!atouts_maitres.estVide())
						/*Defendre la carte de couleur, lorsque tous les atouts sont maitres et qu il existe au moins un atout*/
						return atouts_maitres.carte(0);
					if(repartition.get(seule_couleur).carte(0).valeur()<11)
						/*Jouer la carte de couleur si elle n est pas une figure*/
						return repartition.get(seule_couleur).carte(0);
					if(main_joueur.total()==2)
					{
						if(contientExcuse)
						{/*Ne pas perdre son Excuse*/
							return new CarteTarot((byte)0);
						}
						/*Ne pas perdre l atout dans la main*/
						return repartition.get(seule_couleur).carte(0);
					}
					if(!atouts_maitres.estVide())
						/*Eliminer les atouts adverses*/
						return atouts_maitres.carte(0);
					/*Devenir maitre a l atout*/
					return atoutDansLaSuiteLongueForte(suites.get(1));
				}
				if(carteAppeleeJouee)
				{
					seule_couleur=cartesNonMaitresses.carte(0).couleur();
					Vector<Byte> equipe=coequipiers(numero);
					boolean coupeEquipe=false;
					for(byte joueur:equipe)
						coupeEquipe|=peut_couper(seule_couleur,joueur,cartes_possibles)&&joueurs_non_joue.contains(joueur);
					if(coupeEquipe)
					{/*Sauver la carte de couleur non maitresse*/
						return cartesNonMaitresses.carte(0);
					}
					if(maitre_atout&&carteAppelee!=null)
					{
						if(!atouts_maitres.estVide())
							/*Eliminer les atouts adverses*/
							return atouts_maitres.carte(0);
						if(repartition.get(1).derniereCarte().valeur()==1)
						{/*Petit dans la main present*/
							if(repartitionCartesJouees.get(seule_couleur).nombreFiguresEgal(4))
							{/*Pour defendre le Petit*/
								return cartesNonMaitresses.carte(0);
							}
							if(repartition.get(1).total()==1)
							{/*Pour defendre le Petit comme seul atout*/
								return cartesNonMaitresses.carte(0);
							}
							if(!contientExcuse)
								/*Devenir maitre a l atout*/
								return atoutDansLaSuiteLongueForte(suites.get(1));
							/*Ne pas se faire pieger par le dernier pli*/
							return new CarteTarot((byte)0);
						}
						if(contientExcuse)
							/*Ne pas se faire pieger par le dernier pli*/
							return new CarteTarot((byte)0);
						if(repartitionCartesJouees.get(seule_couleur).nombreFiguresEgal(4))
						{/*Jouer sa carte faible pour prendre plus de points a la fin*/
							return cartesNonMaitresses.carte(0);
						}
						if(repartition.get(1).total()==1)
						{/*Pour ne pas se faire pieger au dernier tour*/
							return cartesNonMaitresses.carte(0);
						}
						/*Devenir maitre a l atout*/
						return atoutDansLaSuiteLongueForte(suites.get(1));
					}
					if(repartition.get(1).total()==atouts_maitres.total())
					{/*Les eventuels atouts dans la main sont tous maitres*/
						Vector<Byte> adversaires=adversaires(numero);
						if(repartition.get(1).estVide())
							/*Ne pas se faire pieger par le dernier pli*/
							/*Dans ce cas, avec au moins deux cartes dans la main et aucun atout dans la main et une seule carte de couleur,
							 * il y a force l Excuse*/
							return new CarteTarot((byte)0);
						boolean coupeCouleur=true;
						for(byte joueur:adversaires)
							if(joueurs_non_joue.contains(joueur))
								coupeCouleur&=peut_couper(seule_couleur,joueur,cartes_possibles);
						if(coupeCouleur)
							/*Defendre une couleur coupe par tous les adversaires n ayant pas joue*/
							return atouts_maitres.carte(0);
						/*Ne pas se faire pieger par le dernier pli*/
						return cartesNonMaitresses.carte(0);
					}
					if(atouts_maitres.estVide())
						/*Ne pas se faire pieger par le dernier pli*/
						return cartesNonMaitresses.carte(0);
					/*Tenter de devenir maitre*/
					return atouts_maitres.carte(0);
				}
				if(repartition.get(1).estVide())
					/*Ne pas se faire pieger par le dernier pli*/
					/*Dans ce cas, avec au moins deux cartes dans la main et aucun atout dans la main et une seule carte de couleur,
					 * il y a force l Excuse*/
					return new CarteTarot((byte)0);
				if(repartition.get(1).total()==atouts_maitres.total())
					/*Les eventuels atouts dans la main sont tous maitres*/
					/*Eliminer les atouts adverses*/
					return atouts_maitres.carte(0);
				/*Ne pas se faire pieger par le dernier pli*/
				return cartesNonMaitresses.carte(0);
			}
			if(preneur==numero)
			{
				if(carteAppeleeJouee&&joueurs_non_joue.contains(appele)&&appele>-1&&!getPoignee(appele).estVide())
				{
					byte nombreAtoutsNonJouesPoignee=0;
					if(repartitionCartesJouees.get(0).estVide()&&getPoignee(appele).contient(new CarteTarot((byte)0)))
					{
						nombreAtoutsNonJouesPoignee++;
					}
					for(Carte carte:getPoignee(appele))
					{
						if(carte.couleur()>0&&!repartitionCartesJouees.get(1).contient(carte))
						{
							nombreAtoutsNonJouesPoignee++;
						}
					}
					if(nombreAtoutsNonJouesPoignee>5&&!repartition.get(1).estVide()&&repartition.get(1).carte(0).valeur()>1)
					{/*Si le preneur possede un atout different de l Excuse autre que le Petit,
					alors le preneur aide l appele a eliminer les atouts, car l appele possede une poignee avec peu d atouts joues*/
						if(suites.get(1).lastElement().carte(0).valeur()>1)
						{
							return suites.get(1).lastElement().carte(0);
						}
						return suites.get(1).get(suites.get(1).size()-2).carte(0);
					}
				}
				Vector<Byte> couleursNonMaitresses=new Vector<Byte>();
				for(byte couleur=2;couleur<6;couleur++)
				{
					if(cartes_maitresses.get(couleur-2).estVide()&&!repartition.get(couleur).estVide()&&(carteAppelee==null||couleurAppelee!=couleur))
					{
						couleursNonMaitresses.addElement(couleur);
					}
				}
				if(!couleursNonMaitresses.isEmpty())
				{/*Pour que le preneur affranchisse une couleur non appelee*/
					return carteDansCouleursSansCartesMaitresses(suites,repartition,couleursNonMaitresses,repartitionCartesJouees);
				}
				if(carteAppeleeJouee&&carteAppelee!=null)
				{
					if(cartes_maitresses.get(couleurAppelee-2).estVide())
					{
						if(!repartition.get(couleurAppelee).estVide())
						{/*Le roi de la couleur appelee appartient a l appele donc il n y a aucun souci pour jouer la plus forte carte de la couleur appelee*/
							return suites.get(couleurAppelee).lastElement().carte(0);
						}
					}
				}
				Vector<Byte> couleurs=couleursAvecUneCarteMaitresse(cartes_maitresses);
				if(strict_maitre_atout&&!couleurs.isEmpty())
				{
					if(repartition.get(0).total()+repartition.get(1).total()+repartitionCartesJouees.get(0).total()+repartitionCartesJouees.get(1).total()==22)
					{/*Si aucun joueur autre que le preneur ne possede de l atout alors le preneur cherche a devenir plus facilement maitre en jouant une suite de cartes maitresses d abord sans reprise puis avec reprise*/
						return carteDansCouleurCaMJouLg(suites,repartition,couleurs,repartitionCartesJouees,cartes_possibles,numero);
					}
					/*Sinon le preneur joue atout pour defendre les figures maitresses, par le plus grand atout*/
					return repartition.get(1).carte(0);
				}
				if(maitre_atout)
				{/*Pour devenir maitre a l atout*/
					return atoutDansLaSuiteLongueForte(suites.get(1));
				}
				if(nombreDeCoupesFranches(repartition)+couleurs.size()==4)
				{
					Vector<Byte> joueurs=joueursDeNonConfiance(numero);
					Vector<Byte> couleurs2=new Vector<Byte>();
					for(byte couleur=2;couleur<6;couleur++)
					{
						if(!repartition.get(couleur).estVide())
						{
							boolean coupeNonConf=false;
							for(byte joueur_non_confiance:joueurs)
							{
								coupeNonConf|=peut_couper(couleur,joueur_non_confiance,cartes_possibles)&&joueurs_non_joue.contains(joueur_non_confiance);
							}
							if(coupeNonConf)
							{
								couleurs2.addElement(couleur);
							}
						}
					}
					if(!couleurs2.isEmpty())
					{/*Faire couper ses adversaires avec une couleur d abord jouee puis longue puis faible, avec une petite carte*/
						return carteDansCouleursSansCartesMaitresses(suites,repartition,couleurs2,repartitionCartesJouees);
					}
					joueurs=joueursDeConfiance(numero);
					for(byte couleur=2;couleur<6;couleur++)
					{
						if(!repartition.get(couleur).estVide()&&repartition.get(couleur).carte(0).valeur()>10)
						{
							boolean coupeConf=false;
							for(byte joueur_confiance:joueurs)
							{
								coupeConf|=peut_couper(couleur,joueur_confiance,cartes_possibles)&&joueurs_non_joue.contains(joueur_confiance);
							}
							if(coupeConf)
							{
								couleurs2.addElement(couleur);
							}
						}
					}
					if(!couleurs2.isEmpty())
					{/*Sauver la carte la plus forte si c est une figure, en faisant couper le partenaire*/
						return carteDansCouleursAvecCartesMaitresses(repartition,couleurs2,repartitionCartesJouees);
					}
					if(!joueurs.isEmpty())
					{/*Il existe un partenaire*/
						if(repartition.get(1).derniereCarte().valeur()==1)
						{/*Le preneur possede le Petit dans la main*/
							boolean existePoignee=false;
							for(byte joueur:joueurs)
								existePoignee|=!getPoignee(joueur).estVide()&&joueurs_non_joue.contains(joueur);
							if(existePoignee)
							{
								MainTarot m2=new MainTarot();
								m2.ajouterCartes(getPoignee(joueurs.get(0)));
								MainTarot suitesAtout;
								if(getPoignee(joueurs.get(0)).carte(0).couleur()>0)
								{
									suitesAtout=m2.atoutsMaitres(repartitionCartesJouees);
									if(!suitesAtout.estVide())
									{/*Jouer son Petit, pour que le partenaire le sauve en le prenant avec un atout maitre*/
										return new CarteTarot((byte)1,(byte)1);
									}
								}
								else
								{
									m2.jouer(new CarteTarot((byte)0));
									suitesAtout=m2.atoutsMaitres(repartitionCartesJouees);
									if(!suitesAtout.estVide())
									{/*Jouer son Petit, pour que le partenaire le sauve en le prenant avec un atout maitre*/
										return new CarteTarot((byte)1,(byte)1);
									}
								}
							}
						}
					}
					couleurs=couleursNonOuvertes(plisFaits);
					for(byte b=0;b<couleurs.size();)
						if(/*repartition.get(couleurs.get(b)).estVide()||*/cartes_maitresses.get(couleurs.get(b)-2).estVide())
							couleurs.removeElementAt(b);
						else
							b++;
					if(!couleurs.isEmpty())
						/*Ouvrir avec une carte maitresse pour devenir maitre ou apres faire couper la defense*/
						return ouvrirAvecCarteMaitresse(repartition,couleurs);
					for(byte couleur=2;couleur<6;couleur++)
						if(!repartition.get(couleur).estVide())
							couleurs.addElement(couleur);
					/*Le preneur cherche a affranchir ses cartes dans les couleurs, en commencant par la plus petite
					 * dans une couleur jouee, longue puis faible*/
					return carteDansCouleurPeJouLgFai(suites,repartition,couleurs,repartitionCartesJouees);
				}
				couleurs=new Vector<Byte>();
				for(byte couleur=2;couleur<6;couleur++)
					if(!repartition.get(couleur).estVide()&&(carteAppeleeJouee||couleur!=couleurAppelee))
						couleurs.addElement(couleur);
				if(!couleurs.isEmpty())
					/*Tenter de faire couper ses adversaires avec une couleur d abord jouee puis longue puis faible, avec une petite carte*/
					return carteDansCouleursSansCartesMaitresses(suites,repartition,couleurs,repartitionCartesJouees);
				/*Le roi de la couleur appelee appartient a l appele donc il n y a aucun souci pour jouer la plus forte carte de la couleur appelee*/
				return repartition.get(couleurAppelee).carte(0);
			}
			if(appele==numero)
			{
				if(!placeAvantPreneur(numero))
				{
					Vector<Byte> couleurs=couleursNonOuvertes(plisFaits);
					if(!carteAppeleeJouee)
						/*L appele ne joue pas la couleur appelee pour ne pas se devoiler*/
						couleurs.remove((Object)couleurAppelee);
					for(byte b=0;b<couleurs.size();)
						if(repartition.get(couleurs.get(b)).estVide())
							couleurs.removeElementAt(b);
						else
							b++;
					if(!couleurs.isEmpty())
						/*L appele joue en premier et le preneur en dernier ou avant dernier, l appele a alors interet a faire des ouvertures au preneur*/
						return ouvrirPreneur(suites,repartition,couleurs,repartitionCartesJouees,cartesChien);
					couleurs=coupesJoueurHorsAtout(preneur,cartes_possibles);
					if(!carteAppeleeJouee)
						/*L appele ne joue pas la couleur appelee pour ne pas se devoiler*/
						couleurs.remove((Object)couleurAppelee);
					for(byte indice_couleur=0;indice_couleur<couleurs.size();)
						if(repartition.get(couleurs.get(indice_couleur)).estVide()||repartition.get(couleurs.get(indice_couleur)).carte(0).valeur()<11)
							couleurs.removeElementAt(indice_couleur);
						else
							indice_couleur++;
					if(!couleurs.isEmpty())
						/*L appele cherche a sauver des figures par coupe du preneur*/
						return sauverFiguresCoupe(repartition,couleurs);
					couleurs=coupesJoueurHorsAtout(preneur,cartes_possibles);
					if(!carteAppeleeJouee)
						/*L appele ne joue pas la couleur appelee pour ne pas se devoiler*/
						couleurs.remove((Object)couleurAppelee);
					for(byte indice_couleur=0;indice_couleur<couleurs.size();)
						if(repartition.get(couleurs.get(indice_couleur)).estVide()||repartitionCartesJouees.get(couleurs.get(indice_couleur)).nombreFiguresSuperieurOuEgal(4)||repartition.get(couleurs.get(indice_couleur)).carte(0).valeur()>10)
							couleurs.removeElementAt(indice_couleur);
						else
							indice_couleur++;
					//L'appele cherche a faire tomber les figures se trouvant en defense s'il est place apres le preneur
					if(!couleurs.isEmpty())
						return faireTomberFiguresPreneur(suites,repartition,couleurs,repartitionCartesJouees);
					for(byte couleur=2;couleur<6;couleur++)
					{
						if(!repartition.get(couleur).estVide())
						{
							for(byte joueur:joueurs_non_joue)
							{
								if(joueur!=numero&&joueur!=preneur)
								{
									if(defausse(cartes_possibles, numero, couleur)&&!couleurs.contains(couleur))
									{
										couleurs.addElement(couleur);
									}
								}
							}
						}
					}
					if(!carteAppeleeJouee)
						/*L appele ne joue pas la couleur appelee pour ne pas se devoiler*/
						couleurs.remove((Object)couleurAppelee);
					for(byte couleur=0;couleur<couleurs.size();)
						if(!coupesNonConf(numero,couleurs.get(couleur),cartes_possibles, joueurs_non_joue).isEmpty())
							couleurs.removeElementAt(couleur);
						else
							couleur++;
					if(!couleurs.isEmpty())
						/*En cas de defausse certaine d un adversaire l appele ouvre pour le preneur*/
						return ouvrirPreneur(suites,repartition,couleurs,repartitionCartesJouees,cartesChien);
					couleurs=new Vector<Byte>();
					for(byte couleur=2;couleur<6;couleur++)
					{
						if(!repartition.get(couleur).estVide()&&!peut_couper(couleur,preneur,cartes_possibles))
						{
							for(byte joueur:joueurs_non_joue)
							{
								if(joueur!=numero&&joueur!=preneur)
								{
									if(peut_couper(couleur,joueur,cartes_possibles)&&!couleurs.contains(couleur))
									{
										couleurs.addElement(couleur);
									}
								}
							}
						}
					}
					if(!carteAppeleeJouee)
						/*L appele ne joue pas la couleur appelee pour ne pas se devoiler*/
						couleurs.remove((Object)couleurAppelee);
					if(!couleurs.isEmpty())
						/*L appele aide le preneur a faire couper la defense sans faire couper le preneur*/
						return faireCouperAdversaires(suites,repartition,couleurs,repartitionCartesJouees);
					couleurs=new Vector<Byte>();
					for(byte couleur=2;couleur<6;couleur++)
					{
						if(!repartition.get(couleur).estVide())
						{
							for(byte joueur:joueurs_non_joue)
							{
								if(joueur!=numero&&joueur!=preneur)
								{
									if(peut_couper(couleur,joueur,cartes_possibles)&&!couleurs.contains(couleur))
									{
										couleurs.addElement(couleur);
									}
								}
							}
						}
					}
					if(!carteAppeleeJouee)
						/*L appele ne joue pas la couleur appelee pour ne pas se devoiler*/
						couleurs.remove((Object)couleurAppelee);
					if(!couleurs.isEmpty())
						/*L appele aide le preneur a faire couper la defense mais en faisant couper le preneur*/
						return faireCouperAdversaires(suites,repartition,couleurs,repartitionCartesJouees);
					couleurs=new Vector<Byte>();
					for(byte couleur=2;couleur<6;couleur++)
						if(!repartition.get(couleur).estVide())
							couleurs.addElement(couleur);
					if(!carteAppeleeJouee)
						/*L appele ne joue pas la couleur appelee pour ne pas se devoiler*/
						couleurs.remove((Object)couleurAppelee);
					if(!couleurs.isEmpty())
						return jouerCouleurNonAppelee(suites,repartition,couleurs,repartitionCartesJouees);
					MainTarot poigneePreneur=getPoignee(preneur);
					MainTarot atouts_maitres_preneur=new MainTarot();
					atouts_maitres_preneur.ajouterCartes(poigneePreneur);
					for(Carte carte:getDistribution().derniereMain())
					{
						if(!atouts_maitres_preneur.contient(carte)&&carte.couleur()==1)
						{
							if(atouts_maitres_preneur.estVide()||atouts_maitres_preneur.derniereCarte().valeur()>carte.valeur())
								atouts_maitres_preneur.ajouter(carte);
							else
							{
								byte c=0;
								for(;atouts_maitres_preneur.carte(c).valeur()>carte.valeur(););
								atouts_maitres_preneur.ajouter(carte,c);
							}
						}
					}
					atouts_maitres_preneur=atouts_maitres_preneur.atoutsMaitres(repartitionCartesJouees);
					if(!atouts_maitres_preneur.estVide())
					{//L'appele entame son Petit s'il sait que le preneur possede un atout maitre non joue
						if(!repartition.get(1).estVide()&&repartition.get(1).derniereCarte().valeur()==1)
							return repartition.get(1).derniereCarte();
					}
					if(couleurAppelee>-1&&!repartition.get(couleurAppelee).estVide())
					{
						if(repartition.get(couleurAppelee).derniereCarte().valeur()<11)
							/*L appele cherche a faire peur aux defenseurs en jouant une carte de la couleur appelee par la plus petite si ce n est pas une figure*/
							return repartition.get(couleurAppelee).derniereCarte();
						/*Sinon il cherche a faire le pli avec une figure*/
						return repartition.get(couleurAppelee).carte(0);
					}
					if(suites.get(1).lastElement().total()>1||suites.get(1).lastElement().carte(0).valeur()>1)
					{/*L appele joue un atout autre que le Petit dans la suite la plus faible*/
						return suites.get(1).lastElement().carte(0);
					}
					if(suites.get(1).size()==1)
					{/*L appele joue un atout autre que le Petit dans la suite la plus faible*/
						return suites.get(1).lastElement().carte(0);
					}/*L appele joue un atout autre que le Petit dans la suite la plus faible*/
					return suites.get(1).get(suites.get(1).size()-2).carte(0);
				}
				Vector<Byte> couleurs=new Vector<Byte>();
				for(byte couleur=2;couleur<6;couleur++)
				{
					if(!repartition.get(couleur).estVide()&&!peut_couper(couleur,preneur,cartes_possibles))
					{
						for(byte joueur:joueurs_non_joue)
						{
							if(joueur!=numero&&joueur!=preneur)
							{
								if(peut_couper(couleur,joueur,cartes_possibles)&&!couleurs.contains(couleur))
								{
									couleurs.addElement(couleur);
								}
							}
						}
					}
				}
				if(!carteAppeleeJouee)
					/*L appele ne joue pas la couleur appelee pour ne pas se devoiler*/
					couleurs.remove((Object)couleurAppelee);
				if(!couleurs.isEmpty())
					/*L appele aide le preneur a faire couper la defense sans faire couper le preneur*/
					return faireCouperAdversaires(suites,repartition,couleurs,repartitionCartesJouees);
				for(byte couleur=2;couleur<6;couleur++)
				{
					if(!repartition.get(couleur).estVide())
					{
						for(byte joueur:joueurs_non_joue)
						{
							if(joueur!=numero&&joueur!=preneur)
							{
								if(peut_couper(couleur,joueur,cartes_possibles)&&!couleurs.contains(couleur))
								{
									couleurs.addElement(couleur);
								}
							}
						}
					}
				}
				if(!carteAppeleeJouee)
					/*L appele ne joue pas la couleur appelee pour ne pas se devoiler*/
					couleurs.remove((Object)couleurAppelee);
				if(!couleurs.isEmpty())
					/*L appele aide le preneur a faire couper la defense en faisant couper le preneur*/
					return faireCouperAdversaires(suites,repartition,couleurs,repartitionCartesJouees);
				for(byte couleur=2;couleur<6;couleur++)
				{
					if(!repartition.get(couleur).estVide())
					{
						for(byte joueur:joueurs_non_joue)
						{
							if(joueur!=numero&&joueur!=preneur)
							{
								if(defausse(cartes_possibles, joueur, couleur)&&!couleurs.contains(couleur))
								{
									couleurs.addElement(couleur);
								}
							}
						}
					}
				}
				if(!carteAppeleeJouee)
					/*L appele ne joue pas la couleur appelee pour ne pas se devoiler*/
					couleurs.remove((Object)couleurAppelee);
				for(byte couleur=0;couleur<couleurs.size();)
					if(!coupesNonConf(numero,couleurs.get(couleur),cartes_possibles, joueurs_non_joue).isEmpty())
						couleurs.removeElementAt(couleur);
					else
						couleur++;
				if(!couleurs.isEmpty())
					/*Si l appele trouve pas une defausse sans coupe pour la defense, alors l appele a interet a faire des ouvertures au preneur, meme si le preneur est place juste derriere*/
					return ouvrirPreneur(suites,repartition,couleurs,repartitionCartesJouees,cartesChien);
				couleurs=coupesJoueurHorsAtout(preneur,cartes_possibles);
				if(!carteAppeleeJouee)
					/*L appele ne joue pas la couleur appelee pour ne pas se devoiler*/
					couleurs.remove((Object)couleurAppelee);
				for(byte indice_couleur=0;indice_couleur<couleurs.size();)
					if(repartition.get(couleurs.get(indice_couleur)).estVide()||repartitionCartesJouees.get(couleurs.get(indice_couleur)).total()>3&&repartitionCartesJouees.get(couleurs.get(indice_couleur)).carte(3).valeur()>10||repartition.get(couleurs.get(indice_couleur)).carte(0).valeur()>10)
						couleurs.removeElementAt(indice_couleur);
					else
						indice_couleur++;
				//L'appele cherche a faire tomber les figures se trouvant en defense s'il est place apres le preneur
				if(!couleurs.isEmpty())
					return faireTomberFiguresPreneur(suites,repartition,couleurs,repartitionCartesJouees);
				couleurs=coupesJoueurHorsAtout(preneur,cartes_possibles);
				if(!carteAppeleeJouee)
					/*L appele ne joue pas la couleur appelee pour ne pas se devoiler*/
					couleurs.remove((Object)couleurAppelee);
				for(byte indice_couleur=0;indice_couleur<couleurs.size();)
					if(repartition.get(couleurs.get(indice_couleur)).estVide()||repartition.get(couleurs.get(indice_couleur)).carte(0).valeur()<11)
						couleurs.removeElementAt(indice_couleur);
					else
						indice_couleur++;
				if(!couleurs.isEmpty())
					/*L appele cherche a sauver des figures par coupe du preneur*/
					return sauverFiguresCoupe(repartition,couleurs);
				MainTarot poigneePreneur=getPoignee(preneur);
				MainTarot maitres=new MainTarot();
				maitres.ajouterCartes(poigneePreneur);
				for(Carte carte:getDistribution().derniereMain())
				{
					if(!maitres.contient(carte)&&carte.couleur()==1)
					{
						if(maitres.estVide()||maitres.derniereCarte().valeur()>carte.valeur())
						{
							maitres.ajouter(carte);
						}
						else
						{
							byte c=0;
							for(;maitres.carte(c).valeur()>carte.valeur(););
							maitres.ajouter(carte,c);
						}
					}
				}
				maitres=maitres.atoutsMaitres(repartitionCartesJouees);
				if(!maitres.estVide())
				{//L'appele entame son Petit s'il sit que le preneur possede un atout maitre non joue
					if(!repartition.get(1).estVide()&&repartition.get(1).derniereCarte().valeur()==1)
						return repartition.get(1).derniereCarte();
				}
				if(!repartition.get(1).estVide())
				{/*Place avant le preneur, l appele joue de l atout pour faire monter la defense, en evitant de jouer le Petit*/
					if(suites.get(1).lastElement().total()>1||suites.get(1).lastElement().carte(0).valeur()>1)
					{
						return suites.get(1).lastElement().carte(0);
					}
					if(suites.get(1).size()==1&&suites.get(1).lastElement().carte(0).valeur()>1)
					{
						return suites.get(1).lastElement().carte(0);
					}
					if(suites.get(1).size()>1)
					{
						return suites.get(1).get(suites.get(1).size()-2).carte(0);
					}
				}
				Vector<Byte> couleurs2=couleursNonOuvertes(plisFaits);
				couleurs=new Vector<Byte>();
				for(byte couleur=2;couleur<6;couleur++)
					if(!repartition.get(couleur).estVide()&&!couleurs2.contains(couleur))
						couleurs.addElement(couleur);
				if(!carteAppeleeJouee)
					/*L appele ne joue pas la couleur appelee pour ne pas se devoiler*/
					couleurs.remove((Object)couleurAppelee);
				if(!couleurs.isEmpty())
					/*L appele rejoue des couleurs deja ouvertes pour ne pas faciliter le sauvetage en defense*/
					return jouerCouleurNonAppelee(suites,repartition,couleurs,repartitionCartesJouees);
				if(!carteAppeleeJouee)
					/*L appele ne joue pas la couleur appelee pour ne pas se devoiler*/
					couleurs2.remove((Object)couleurAppelee);
				for(byte b=0;b<couleurs2.size();)
					if(repartition.get(couleurs2.get(b)).estVide())
						couleurs2.removeElementAt(b);
					else
						b++;
				if(!couleurs2.isEmpty())
					/*L appele ne peut pas faire autrement que d ouvrir a une couleur non appelee ou de jouer la couleur appelee*/
					return ouvrirPreneur(suites,repartition,couleurs2,repartitionCartesJouees,cartesChien);
				if(couleurAppelee>-1&&!repartition.get(couleurAppelee).estVide())
				{
					if(repartition.get(couleurAppelee).derniereCarte().valeur()<11)
						/*L appele cherche a faire peur aux defenseurs en jouant une carte de la couleur appelee par la plus petite si ce n est pas une figure*/
						return repartition.get(couleurAppelee).derniereCarte();
					/*Sinon il cherche a faire le pli avec une figure*/
					return repartition.get(couleurAppelee).carte(0);
				}
			}
			/*Defenseur*/
			if(couleurAppelee>-1)
			{//Si la couleur appelee existe
				if(cartes_jouables.tailleCouleur(couleurAppelee)>0)
				{//Si le premier tour est joue et si le defenseur possede au moins une carte de la couleur appelee
					if(!carteAppeleeJouee&&tours(couleurAppelee, plisFaits).isEmpty())
					{//Un defenseur cherche a connaitre l'appele
						if(suites.get(couleurAppelee).size()>1)
							/*Decouverte du suspense*/
							return suites.get(couleurAppelee).lastElement().carte(0);
						for(Carte carte:suites.get(couleurAppelee).get(0))
						{
							if(carte.valeur()<11)
							{/*Decouverte du suspense*/
								return carte;
							}
						}
						/*Decouverte du suspense*/
						return suites.get(couleurAppelee).get(0).derniereCarte();
					}
				}
			}
			Vector<Byte> couleurs=new Vector<Byte>();
			for(byte couleur=2;couleur<6;couleur++)
				if(!repartition.get(couleur).estVide())
					couleurs.addElement(couleur);
			if(premierTour())
			{/*Premiere entame d'un defenseur*/
				if(couleurAppelee>-1)
					/*La couleur appelee au premier tour n est pas jouable pour un defenseur*/
					couleurs.remove((Object)couleurAppelee);
				/*Le defenseur ouvre a une couleur car il ne peut pas jouer la couleur appelee*/
				return jouerCouleurNonAppelee2(suites,repartition,couleurs,repartitionCartesJouees);
			}
			if(couleurAppelee>-1)
			{//Si la couleur appelee existe
				if(!tours(couleurAppelee, plisFaits).isEmpty())
				{/*Si la couleur appelee a ete deja jouee*/
					if(!carteAppeleeJouee&&!repartition.get(couleurAppelee).estVide())
					{/*Mais pas la carte appelee et de plus ce defenseur peut entamer par la couleur appelee*/
						Vector<Byte> coupes=new Vector<Byte>();
						for(byte joueur=0;joueur<nombre_joueurs;joueur++)
							if(joueur!=preneur&&peut_couper(couleurAppelee, joueur, cartes_possibles)&&joueurs_non_joue.contains(joueur))
								coupes.addElement(joueur);
						if(!coupes.isEmpty()&&repartition.get(couleurAppelee).carte(0).valeur()>10)
							/*Decouverte du suspense, en sauvant une figure sur coupe de la couleur appelee d un autre defenseur*/
							return repartition.get(couleurAppelee).carte(0);
						if(repartition.get(couleurAppelee).carte(0).valeur()>10)
							/*Decouverte du suspense*/
							return suites.get(couleurAppelee).lastElement().derniereCarte();
						if(suites.get(couleurAppelee).size()==1)
						{
							for(Carte carte:suites.get(couleurAppelee).get(0))
							{
								if(carte.valeur()<11)
								{/*Decouverte du suspense*/
									return carte;
								}
							}
							/*Decouverte du suspense*/
							return suites.get(couleurAppelee).get(0).derniereCarte();
						}
						for(byte b=0;b<suites.get(couleurAppelee).lastElement().total();b++)
						{
							if(suites.get(couleurAppelee).lastElement().carte(b).valeur()<11)
							{/*Decouverte du suspense*/
								return suites.get(couleurAppelee).lastElement().carte(b);
							}
						}
						/*Decouverte du suspense*/
						return suites.get(couleurAppelee).lastElement().carte(0);
					}
				}
			}
			if(placeAvantPreneur(numero))
			{
				couleurs=couleursNonOuvertes(plisFaits);
				for(byte b=0;b<couleurs.size();)
					if(repartition.get(couleurs.get(b)).estVide())
						couleurs.removeElementAt(b);
					else
						b++;
				if(!couleurs.isEmpty())
					/*Pour ne pas faciliter la prise de connaissance de jeu pour le preneur, un defenseur cherche a ouvrir s il est place avant le preneur*/
					return ouvrirPreneur2(suites,repartition,couleurs,repartitionCartesJouees,cartesChien);
				for(byte couleur=2;couleur<6;couleur++)
					if(!repartition.get(couleur).estVide()&&!peut_couper(couleur,preneur,cartes_possibles))
					{
						for(byte joueur:joueurs_non_joue)
						{
							if(peut_couper(couleur,joueur,cartes_possibles)&&confiance(numero,joueur)&&!couleurs.contains(couleur))
								couleurs.addElement(couleur);
						}
					}
				for(byte indice_couleur=0;indice_couleur<couleurs.size();)
					if(repartition.get(couleurs.get(indice_couleur)).carte(0).valeur()<11)
						couleurs.removeElementAt(indice_couleur);
					else
						indice_couleur++;
				if(!couleurs.isEmpty())
					/*Un defenseur va chercher a sauver ses figures par la coupe d un autre defenseur sans faire couper le preneur, sinon il perd des points*/
					return sauverFiguresCoupe(repartition,couleurs);
				couleurs=new Vector<Byte>();
				for(byte couleur=2;couleur<6;couleur++)
					if(!repartition.get(couleur).estVide()&&!peut_couper(couleur,preneur,cartes_possibles))
						for(byte joueur:joueurs_non_joue)
							if(peut_couper(couleur,joueur,cartes_possibles)&&confiance(numero,joueur)&&!couleurs.contains(couleur))
								couleurs.addElement(couleur);
				for(byte b=0;b<couleurs.size();)
					if(repartitionCartesJouees.get(couleurs.get(b)).total()>3&&repartitionCartesJouees.get(couleurs.get(b)).carte(3).valeur()>10||repartition.get(couleurs.get(b)).carte(0).valeur()>10)
						couleurs.removeElementAt(b);
					else
						b++;
				if(!couleurs.isEmpty())
					//Le defenseur cherche a faire tomber les figures se trouvant en attaque s'il est place avant le preneur
					return faireTomberFiguresPreneur(suites,repartition,couleurs,repartitionCartesJouees);
				Vector<Byte> joueurs_non_confiance=joueursDeNonConfiance(numero);
				couleurs=new Vector<Byte>();
				for(byte couleur=2;couleur<6;couleur++)
				{
					if(!repartition.get(couleur).estVide())
					{
						for(byte joueur:joueurs_non_joue)
						{
							if(joueurs_non_confiance.contains(joueur))
							{
								if(defausse(cartes_possibles, joueur, couleur)&&!cartes_maitresses.get(couleur-2).estVide())
								{
									if(!couleurs.contains(couleur))
										couleurs.addElement(couleur);
								}
							}
						}
					}
				}
				for(byte couleur=0;couleur<couleurs.size();)
					if(!coupesNonConf(numero,couleurs.get(couleur),cartes_possibles, joueurs_non_joue).isEmpty())
						couleurs.removeElementAt(couleur);
					else
						couleur++;
				if(!couleurs.isEmpty())
					/*Si le defenseur a vu qu un des joueurs de non confiance se defausse sans que les autres coupent alors il fait comme s il etait l appele*/
					return carteDansCouleursAvecCartesMaitresses(repartition,couleurs,repartitionCartesJouees);
				Vector<Byte> joueurs_confiance=joueursDeConfiance(numero);
				for(byte couleur=2;couleur<6;couleur++)
					if(!repartition.get(couleur).estVide()&&peut_couper(couleur,preneur,cartes_possibles))
					{
						boolean neCoupePas=true;
						for(byte joueur:joueurs_confiance)
							if(joueurs_non_joue.contains(joueur))
								neCoupePas&=!peut_couper(couleur,joueur,cartes_possibles);
						if(neCoupePas)
							couleurs.addElement(couleur);
					}
				if(!couleurs.isEmpty())
					/*Le defenseur cherche a faire couper les joueurs de non confiance sans faire couper ses partenaires*/
					return faireCouperAdversaires(suites,repartition,couleurs,repartitionCartesJouees);
				for(byte couleur=2;couleur<6;couleur++)
					if(!repartition.get(couleur).estVide()&&peut_couper(couleur,preneur,cartes_possibles)&&joueurs_non_joue.contains(preneur))
						couleurs.addElement(couleur);
				if(!couleurs.isEmpty())
					/*Le defenseur cherche a faire couper les joueurs de non confiance en faisant couper ses partenaires*/
					return faireCouperAdversaires(suites,repartition,couleurs,repartitionCartesJouees);
				for(byte couleur=2;couleur<6;couleur++)
					if(!repartition.get(couleur).estVide())
						couleurs.addElement(couleur);
				/*Le defenseur cherche a jouer une couleur comme si c etait le premier tour*/
				return jouerCouleurNonAppelee2(suites,repartition,couleurs,repartitionCartesJouees);
			}
			Vector<Byte> joueurs_non_confiance=joueursDeNonConfiance(numero);
			couleurs=new Vector<Byte>();
			Vector<Byte> joueurs_confiance=joueursDeConfiance(numero);
			for(byte couleur=2;couleur<6;couleur++)
				if(!repartition.get(couleur).estVide()&&peut_couper(couleur,preneur,cartes_possibles))
				{
					boolean neCoupePas=true;
					for(byte joueur:joueurs_confiance)
						if(joueurs_non_joue.contains(joueur))
							neCoupePas&=!peut_couper(couleur,joueur,cartes_possibles);
					if(neCoupePas)
						couleurs.addElement(couleur);
				}
			if(!couleurs.isEmpty())
				/*Place apres le preneur, le defenseur cherche a faire couper les joueurs de non confiance sans faire couper ses partenaires*/
				return faireCouperAdversaires(suites,repartition,couleurs,repartitionCartesJouees);
			couleurs=new Vector<Byte>();
			for(byte couleur=2;couleur<6;couleur++)
				if(!repartition.get(couleur).estVide()&&peut_couper(couleur,preneur,cartes_possibles)&&joueurs_non_joue.contains(preneur))
					couleurs.addElement(couleur);
			if(!couleurs.isEmpty())
				/*Le defenseur cherche a faire couper les joueurs de non confiance en faisant couper ses partenaires*/
				return faireCouperAdversaires(suites,repartition,couleurs,repartitionCartesJouees);
			couleurs=new Vector<Byte>();
			for(byte couleur=2;couleur<6;couleur++)
				if(!repartition.get(couleur).estVide())
					for(byte joueur:joueurs_non_joue)
						if(joueurs_non_confiance.contains(joueur)&&defausse(cartes_possibles, joueur, couleur)&&!cartes_maitresses.get(couleur-2).estVide()&&!couleurs.contains(couleur))
						{
							couleurs.addElement(couleur);
						}
			for(byte couleur=0;couleur<couleurs.size();)
				if(!coupesNonConf(numero,couleurs.get(couleur),cartes_possibles, joueurs_non_joue).isEmpty())
					couleurs.removeElementAt(couleur);
				else
					couleur++;
			if(!couleurs.isEmpty())
				/*Si le defenseur a vu qu un des joueurs de non confiance se defausse sans que les autres coupent alors il fait comme s il etait l appele*/
				return carteDansCouleursAvecCartesMaitresses(repartition,couleurs,repartitionCartesJouees);
			couleurs=new Vector<Byte>();
			for(byte couleur=2;couleur<6;couleur++)
				if(!repartition.get(couleur).estVide()&&!peut_couper(couleur,preneur,cartes_possibles))
					for(byte joueur:joueurs_non_joue)
						if(peut_couper(couleur,joueur,cartes_possibles)&&confiance(numero,joueur))
							couleurs.addElement(couleur);
			for(byte b=0;b<couleurs.size();)
				if(repartitionCartesJouees.get(couleurs.get(b)).total()>3&&repartitionCartesJouees.get(couleurs.get(b)).carte(3).valeur()>10||repartition.get(couleurs.get(b)).carte(0).valeur()>10)
					couleurs.removeElementAt(b);
				else
					b++;
			//Le defenseur cherche a faire tomber les figures se trouvant en attaque s'il est place apres le preneur
			if(!couleurs.isEmpty())
				return faireTomberFiguresPreneur(suites,repartition,couleurs,repartitionCartesJouees);
			couleurs=new Vector<Byte>();
			for(byte couleur=2;couleur<6;couleur++)
				if(!repartition.get(couleur).estVide()&&!peut_couper(couleur,preneur,cartes_possibles))
				{
					for(byte joueur:joueurs_non_joue)
					{
						if(peut_couper(couleur,joueur,cartes_possibles)&&confiance(numero,joueur))
							couleurs.addElement(couleur);
					}
				}
			for(byte b=0;b<couleurs.size();)
				if(repartition.get(couleurs.get(b)).carte(0).valeur()<11)
					couleurs.removeElementAt(b);
				else
					b++;
			if(!couleurs.isEmpty())
				/*Un defenseur va chercher a sauver ses figures par la coupe d un autre defenseur sans faire couper le preneur, sinon il perd des points*/
				return sauverFiguresCoupe(repartition,couleurs);
			if(!repartition.get(1).estVide())
			{
				if(suites.get(1).lastElement().total()>1||suites.get(1).lastElement().carte(0).valeur()>1)
				{/*Si la plus faible suite contient au moins deux cartes ou si la plus faible suite ne contient pas le Petit*/
					return suites.get(1).lastElement().carte(0);
				}
				if(suites.get(1).size()==1&&suites.get(1).lastElement().carte(0).valeur()>1)
				{
					suites.get(1).lastElement().carte(0);
				}
				if(suites.get(1).size()>1)
				{
					return suites.get(1).get(suites.get(1).size()-2).carte(0);
				}
			}
			Vector<Byte> couleurs2=couleursNonOuvertes(plisFaits);
			couleurs=new Vector<Byte>();
			for(byte couleur=2;couleur<6;couleur++)
				if(!repartition.get(couleur).estVide()&&!couleurs2.contains(couleur))
					couleurs.addElement(couleur);
			if(!carteAppeleeJouee)
				/*L appele ne joue pas la couleur appelee pour ne pas se devoiler*/
				couleurs.remove((Object)couleurAppelee);
			if(!couleurs.isEmpty())
				/*L appele rejoue des couleurs deja ouvertes pour ne pas faciliter le sauvetage en defense*/
				return jouerCouleurNonAppelee2(suites,repartition,couleurs,repartitionCartesJouees);
			for(byte b=0;b<couleurs2.size();)
				if(repartition.get(couleurs2.get(b)).estVide())
					couleurs2.removeElementAt(b);
				else
					b++;
			/*L ouverture pour le preneur est la derniere solution en etant place apres le preneur*/
			return ouvrirPreneur2(suites,repartition,couleurs2,repartitionCartesJouees,cartesChien);
		}
		if(pas_jeu_misere())
		{
			if(couleurs_maitres.size()==4)
			{
				if(contientExcuse)
				{
					if(strict_maitre_atout)
					{
						if(!adversaireAFaitPlis(numero))
						{
							if(!atouts.estVide()&&(atouts.total()!=1||atouts.carte(0).valeur()>1))
							{
								return atouts.carte(0);
							}
							for(int couleur=2;couleur<6;couleur++)
							{
								if(!repartition.get(couleur).estVide())
								{
									indice_couleur_jouer=couleur;
									break;
								}
							}
							if(indice_couleur_jouer>0)
							{
								return repartition.get(indice_couleur_jouer).carte(0);
							}
							return new CarteTarot((byte)1,(byte)1);
						}
						return new CarteTarot((byte)0);
					}
					if(maitre_atout)
					{
						if(!atouts_maitres.estVide())
							return atouts_maitres.carte(0);
						return atoutDansLaSuiteLongueForte(suites.get(1));
					}
				}
				if(!atouts_maitres.estVide())
					return atouts_maitres.carte(0);
				if(!suites.get(1).isEmpty())
					return atoutDansLaSuiteLongueForte(suites.get(1));
				for(int couleur=2;couleur<6;couleur++)
				{
					if(!repartition.get(couleur).estVide())
					{
						indice_couleur_jouer=couleur;
						break;
					}
				}
				return repartition.get(indice_couleur_jouer).carte(0);
			}
			if(estUnJeuDeChelem(repartition,getInfos(),nombre_joueurs,raison))
			{//On est au premier tour et le preneur possede un jeu de chelem avec au moins un atout
				return repartition.get(1).carte(0);
			}
			byte nombre_couleurs_non_maitres=0;
			for(byte couleur=2;couleur<6;couleur++)
			{
				if(!couleurs_maitres.contains(couleur)&&couleurs_pseudosmaitres.contains(couleur))
				{
					nombre_couleurs_non_maitres++;
				}
			}
			for(byte joueur:joueurs_non_joue)
				if(cartes_possibles.get(1).get(joueur).estVide())
					nombreDeJoueursPossedantAtout--;/*On enleve les joueurs n'ayant pas d'atout de maniere certaine*/
			if(nombreDeJoueursPossedantAtout>2&&nbCouleursLargementMaitressesJeu(cartes_maitresses,couleurs_maitres,nombre_joueurs)+nombre_couleurs_non_maitres==4)
			{//si 3 joueurs dont l'entameur ont joue de l'atout, on continue a jouer de l'atout
				if(!atouts_maitres.estVide())
					return suites.get(1).get(0).carte(0);
			}
			if(repartition.get(0).total()+repartition.get(1).total()==main_joueur.total())
			{
				if(!contientExcuse)
				{
					if(!atouts_maitres.estVide())
						return atouts_maitres.carte(0);
					return atoutDansLaSuiteLongueForte(suites.get(1));
				}
				if(2==main_joueur.total())
				{
					if(!adversaireAFaitPlis(numero))
					{
						return repartition.get(1).carte(0);
					}
					return new CarteTarot((byte)0);
				}
				if(!atouts_maitres.estVide())
					return atouts_maitres.carte(0);
				return atoutDansLaSuiteLongueForte(suites.get(1));
			}
			if(repartition.get(0).total()+repartition.get(1).total()+1==main_joueur.total())
			{
				for(byte b=0;b<couleurs_strictement_maitresses.size();)
					if(repartition.get(couleurs_strictement_maitresses.get(b)).estVide())
						couleurs_strictement_maitresses.removeElementAt(b);
					else
						b++;
				if(couleurs_strictement_maitresses.size()==1)
				{
					if(atouts_maitres.total()==repartition.get(1).total())
						if(!atouts_maitres.estVide())
							return atouts_maitres.carte(0);
					if(repartition.get(couleurs_strictement_maitresses.get(0)).carte(0).valeur()<11)
						return repartition.get(couleurs_strictement_maitresses.get(0)).carte(0);
					if(main_joueur.total()==2)
					{
						if(contientExcuse)
							return new CarteTarot((byte)0);
						return repartition.get(couleurs_strictement_maitresses.get(0)).carte(0);
					}
					if(!atouts_maitres.estVide())
						return atouts_maitres.carte(0);
					return atoutDansLaSuiteLongueForte(suites.get(1));
				}
				if(carteAppeleeJouee)
				{
					if(repartition.get(1).total()==atouts_maitres.total())
					{
						boolean coupeCouleur=true;
						for(byte joueur:joueurs_non_joue)
							coupeCouleur&=peut_couper(cartesNonMaitresses.carte(0).couleur(),joueur,cartes_possibles);
						if(repartition.get(1).estVide())
							return new CarteTarot((byte)0);
						if(coupeCouleur)
							return atouts_maitres.carte(0);
						return cartesNonMaitresses.carte(0);
					}
					if(atouts_maitres.estVide())
						return cartesNonMaitresses.carte(0);
					return cartesNonMaitresses.carte(0);
				}
				if(repartition.get(1).total()==atouts_maitres.total())
					return atouts_maitres.carte(0);
				return cartesNonMaitresses.carte(0);
			}
			Vector<Byte> couleursNonMaitresses=new Vector<Byte>();
			for(byte b=2;b<6;b++)
				if(cartes_maitresses.get(b-2).estVide()&&!repartition.get(b).estVide())
					couleursNonMaitresses.addElement(b);
			if(!couleursNonMaitresses.isEmpty())
			{
				return carteDansCouleursSansCartesMaitresses(suites,repartition,couleursNonMaitresses,repartitionCartesJouees);
			}
			Vector<Byte> couleurs=couleursAvecUneCarteMaitresse(cartes_maitresses);
			if(strict_maitre_atout)
			{
				if(repartition.get(0).total()+repartition.get(1).total()+repartitionCartesJouees.get(0).total()+repartitionCartesJouees.get(1).total()==22&&repartition.get(0).total()+repartition.get(1).total()+repartitionCartesJouees.get(0).total()!=main_joueur.total())
				{
					return carteDansCouleurCaMJouLg(suites,repartition,couleurs,repartitionCartesJouees,cartes_possibles,numero);
				}
				return repartition.get(1).carte(0);
			}
			if(maitre_atout)
			{
				return atoutDansLaSuiteLongueForte(suites.get(1));
			}
			if(nombreDeCoupesFranches(repartition)+couleurs.size()==4)
			{
				Vector<Byte> couleurs2=new Vector<Byte>();
				for(byte couleur:couleurs)
				{
					if(!coupes(couleur,joueurs_non_joue,cartes_possibles).isEmpty())
					{
						couleurs2.addElement(couleur);
					}
				}
				if(!couleurs2.isEmpty())
				{
					return carteDansCouleursSansCartesMaitresses(suites,repartition,couleurs2,repartitionCartesJouees);
				}
				couleurs2=couleursNonOuvertes(plisFaits);
				for(byte b=0;b<couleurs2.size();)
					if(repartition.get(couleurs2.get(b)).estVide()||repartition.get(couleurs2.get(b)).carte(0).valeur()<14)
						couleurs2.removeElementAt(b);
					else
						b++;
				if(!couleurs2.isEmpty())
					return ouvrirAvecCarteMaitresse(repartition,couleurs2);
				for(byte b=2;b<6;b++)
					if(!repartition.get(couleurs2.get(b)).estVide())
						couleurs2.addElement(b);
				return carteDansCouleurPeJouLgFai(suites,repartition,couleurs2,repartitionCartesJouees);
			}
			Vector<Byte> couleurs2=new Vector<Byte>();
			for(byte b=2;b<6;b++)
				if(!repartition.get(b).estVide())
					couleurs2.addElement(b);
			return carteDansCouleursSansCartesMaitresses(suites,repartition,couleurs2,repartitionCartesJouees);
		}
		/*Jeu de misere*/
		if(!repartition.get(1).estVide()&&repartition.get(1).derniereCarte().valeur()==1)
		{
			return new CarteTarot((byte)1,(byte)1);
		}
		if(repartition.get(1).total()+repartition.get(0).total()==main_joueur.total())
		{
			if(atouts_maitres.estVide())
			{
				return suites.get(1).get(0).derniereCarte();
			}
			return repartition.get(1).derniereCarte();
		}
		if(repartition.get(1).total()==1&&atouts_maitres.estVide())
		{
			return repartition.get(1).carte(0);
		}
		boolean coupe_sure=false;
		for(byte couleur=2;couleur<6;couleur++)
		{
			for(byte joueur:joueurs_non_joue)
			{
				coupe_sure|=va_couper(couleur, joueur, cartes_possibles, cartes_certaines);
			}
		}
		Vector<Byte> couleurs=new Vector<Byte>();
		if(coupe_sure)
		{
			for(byte couleur=2;couleur<6;couleur++)
			{
				if(!repartition.get(couleur).estVide()&&repartition.get(couleur).carte(0).valeur()>10)
				{
					couleurs.addElement(couleur);
				}
			}
			if(!couleurs.isEmpty())
			{
				return depouille_figure(couleurs, repartition, repartitionCartesJouees);
			}
			for(byte couleur=2;couleur<6;couleur++)
			{
				if(!repartition.get(couleur).estVide())
				{
					couleurs.addElement(couleur);
				}
			}
			return depouille_petite_carte(couleurs, repartition, repartitionCartesJouees);
		}
		for(byte couleur=2;couleur<6;couleur++)
		{
			if(!repartition.get(couleur).estVide())
			{
				couleurs.addElement(couleur);
			}
		}
		if(!couleurs.isEmpty()&&!repartition.get(1).estVide())
		{
			if(atouts_maitres.estVide())
			{
				return suites.get(1).get(0).derniereCarte();
			}
			return repartition.get(1).derniereCarte();
		}
		return entame_misere_petite(couleurs,repartition,repartitionCartesJouees);
	}
	private static int nombreDeCoupesFranches(Vector<MainTarot> repartition)
	{
		int nnombre=0;
		for(byte couleur=2;couleur<6;couleur++)
			if(repartition.get(couleur).estVide())
				nnombre++;
		return nnombre;
	}
	private Vector<Byte> coupesNonConf(byte numero,byte couleur,Vector<Vector<MainTarot>> cartes_possibles, Vector<Byte> joueurs_non_joue)
	{
		Vector<Byte> nb=new Vector<Byte>();
		for(byte joueur:joueurs_non_joue)
			if(!confiance(numero,joueur))
				if(peut_couper(couleur,joueur,cartes_possibles))
					nb.addElement(joueur);
		return nb;
	}
	private Vector<Byte> coupesJoueurHorsAtout(byte joueur,Vector<Vector<MainTarot>> cartes_possibles)
	{
		Vector<Byte> nb=new Vector<Byte>();
		for(byte couleur=2;couleur<6;couleur++)
			if(peut_couper(couleur,joueur,cartes_possibles))
				nb.addElement(couleur);
		return nb;
	}
	/**Renvoie l'ensemble des joueurs pouvant couper la couleur
	 * @param joueurs_non_joue
	 * @param cartes_possibles*/
	private Vector<Byte> coupes(byte couleur,Vector<Byte> joueurs_non_joue, Vector<Vector<MainTarot>> cartes_possibles)
	{
		Vector<Byte> nb=new Vector<Byte>();
		for(byte joueur:joueurs_non_joue)
			if(peut_couper(couleur,joueur,cartes_possibles))
				nb.addElement(joueur);
		return nb;
	}
	/**Methode permettant de dire si un joueur autre que le preneur est place avant le preneur (1 ou 2 places a 5 joueurs) (1 place
	 * a 3 ou 4 joueurs), ceci est utilise pour comment entameur en fonction de la position par rapport au preneur*/
	private boolean placeAvantPreneur(byte numero)
	{
		if(getNombreDeJoueurs()==4)
		{
			return(preneur==0)?numero==3:numero==preneur-1;
		}
		if(getNombreDeJoueurs()==5)
		{
			if(preneur==0)
			{
				return numero>2;
			}
			if(preneur==1)
			{
				return numero<1&&numero>3;
			}
			return preneur-numero>0&&preneur-numero<3;
		}
		return(preneur==0)?numero==2:numero==preneur-1;
	}
	private MainTarot cartesVuesAuChien()
	{
		MainTarot cartes=new MainTarot();
		if(contrat.force()>0&&contrat.force()<3)
			cartes.ajouterCartes(getDistribution().derniereMain());
		return cartes;
	}
	private static byte nbCouleursLargementMaitressesJeu(Vector<MainTarot> cartes_maitresses,Vector<Byte> couleurs_maitresses,byte nombre_joueurs)
	{
		byte nb=0;
		for(byte couleur=2;couleur<6;couleur++)
			if(LargementMaitreDansUneCouleur(cartes_maitresses,couleurs_maitresses,couleur,nombre_joueurs))
				nb++;
		return nb;
	}
	private static boolean LargementMaitreDansUneCouleur(Vector<MainTarot> cartes_maitresses,Vector<Byte> couleurs_maitresses,byte couleur,byte nombre_joueurs)
	{
		if(couleurs_maitresses.contains(couleur))
		{
			return true;
		}
		if(nombre_joueurs==3)
		{
			if(cartes_maitresses.get(couleur-2).total()>5)
				return true;
		}
		else if(nombre_joueurs==4)
		{
			if(cartes_maitresses.get(couleur-2).total()>4)
				return true;
		}
		else
		{
			if(cartes_maitresses.get(couleur-2).total()>3)
				return true;
		}
		return false;
	}
	/**Point de vue d&eacute;fenseur: renvoie la carte la plus faible, dans la couleur avec le moins de figures, la plus jouee, la plus courte, la plus faible*/
	private static CarteTarot jouerCouleurNonAppelee2(Vector<Vector<MainTarot>> suites,Vector<MainTarot> repartition,Vector<Byte> couleurs,Vector<MainTarot> cartesJouees)
	{
		for(int indice=1;indice<couleurs.size();indice++)
		{
			byte couleur1=couleurs.get(0);
			byte couleur2=couleurs.get(indice);
			if(repartition.get(couleur1).nombreDeFigures()>repartition.get(couleur2).nombreDeFigures())
			{
				couleurs.setElementAt(couleurs.set(0,couleur2),indice);
			}
			else if(repartition.get(couleur1).nombreDeFigures()==repartition.get(couleur2).nombreDeFigures())
			{
				if(cartesJouees.get(couleur1).total()<cartesJouees.get(couleur2).total())
				{
					couleurs.setElementAt(couleurs.set(0,couleur2),indice);
				}
				else if(cartesJouees.get(couleur1).total()==cartesJouees.get(couleur2).total())
				{
					if(repartition.get(couleur1).total()>repartition.get(couleur2).total())
					{
						couleurs.setElementAt(couleurs.set(0,couleur2),indice);
					}
					else if(repartition.get(couleur1).total()==repartition.get(couleur2).total())
					{
						for(int j=0;j<repartition.get(couleur1).total();j++)
						{
							if(repartition.get(couleur1).carte(j).valeur()>repartition.get(couleur2).carte(j).valeur())
							{
								couleurs.setElementAt(couleurs.set(0,couleur2),indice);
								break;
							}
							else if(repartition.get(couleur1).carte(j).valeur()<repartition.get(couleur2).carte(j).valeur())
								break;
						}
					}
				}
			}
		}
		return carteLaPlusPetite(suites.get(couleurs.get(0)));
	}
	/**Renvoie la carte la plus faible, dans la couleur la plus jouee, la plus longue, la plus faible*/
	private static CarteTarot faireCouperAdversaires(Vector<Vector<MainTarot>> suites,Vector<MainTarot> repartition,Vector<Byte> couleurs,Vector<MainTarot> cartesJouees)
	{
		for(int indice=1;indice<couleurs.size();indice++)
		{
			byte couleur1=couleurs.get(0);
			byte couleur2=couleurs.get(indice);
			if(cartesJouees.get(couleur1).total()<cartesJouees.get(couleur2).total())
			{
				couleurs.setElementAt(couleurs.set(0,couleur2),indice);
			}
			else if(cartesJouees.get(couleur1).total()==cartesJouees.get(couleur2).total())
			{
				if(repartition.get(couleur1).total()<repartition.get(couleur2).total())
				{
					couleurs.setElementAt(couleurs.set(0,couleur2),indice);
				}
				else if(repartition.get(couleur1).total()==repartition.get(couleur2).total())
				{
					for(int j=0;j<repartition.get(couleur1).total();j++)
					{
						if(repartition.get(couleur1).carte(j).valeur()>repartition.get(couleur2).carte(j).valeur())
						{
							couleurs.setElementAt(couleurs.set(0,couleur2),indice);
							break;
						}
						else if(repartition.get(couleur1).carte(j).valeur()<repartition.get(couleur2).carte(j).valeur())
							break;
					}
				}
			}
		}
		return carteLaPlusPetite(suites.get(couleurs.get(0)));
	}
	private static CarteTarot ouvrirAvecCarteMaitresse(Vector<MainTarot> repartition,Vector<Byte> couleurs)
	{
		for(int i=1;i<couleurs.size();i++)
		{
			byte couleur1=couleurs.get(0);
			byte couleur2=couleurs.get(i);
			if(repartition.get(couleur1).total()<repartition.get(couleur2).total())
			{
				couleurs.setElementAt(couleurs.set(0,couleur2),i);
			}
			else if(repartition.get(couleur1).total()==repartition.get(couleur2).total())
			{
				for(int j=0;j<repartition.get(couleur1).total();j++)
				{
					if(repartition.get(couleur1).carte(j).valeur()>repartition.get(couleur2).carte(j).valeur())
					{
						couleurs.setElementAt(couleurs.set(0,couleur2),i);
						break;
					}
					else if(repartition.get(couleur1).carte(j).valeur()<repartition.get(couleur2).carte(j).valeur())
						break;
				}
			}
		}
		return(CarteTarot)repartition.get(couleurs.get(0)).carte(0);
	}
	/**Renvoie la carte la plus forte dans la couleur avec la carte la plus forte, avec le nombre total de cartes le plus grand, avec le nombre de figures a jouer le plus petit
	 * la plus faible*/
	private static CarteTarot sauverFiguresCoupe(Vector<MainTarot> repartition,Vector<Byte> couleurs)
	{
		for(int indice=1;indice<couleurs.size();indice++)
		{
			byte couleur1=couleurs.get(0);
			byte couleur2=couleurs.get(indice);
			if(repartition.get(couleur1).carte(0).valeur()<repartition.get(couleur2).carte(0).valeur())
			{
				couleurs.setElementAt(couleurs.set(0,couleur2),indice);
			}
			else if(repartition.get(couleur1).carte(0).valeur()==repartition.get(couleur2).carte(0).valeur())
			{
				if(repartition.get(couleur1).total()<repartition.get(couleur2).total())
				{
					couleurs.setElementAt(couleurs.set(0,couleur2),indice);
				}
				else if(repartition.get(couleur1).total()==repartition.get(couleur2).total())
				{
					if(repartition.get(couleur1).nombreDeFigures()>repartition.get(couleur2).nombreDeFigures())
					{
						couleurs.setElementAt(couleurs.set(0,couleur2),indice);
					}
					else if(repartition.get(couleur1).nombreDeFigures()==repartition.get(couleur2).nombreDeFigures())
					{
						for(int j=0;j<repartition.get(couleur1).total();j++)
						{
							if(repartition.get(couleur1).carte(j).valeur()>repartition.get(couleur2).carte(j).valeur())
							{
								couleurs.setElementAt(couleurs.set(0,couleur2),indice);
								break;
							}
							else if(repartition.get(couleur1).carte(j).valeur()<repartition.get(couleur2).carte(j).valeur())
								break;
						}
					}
				}
			}
		}
		return(CarteTarot)repartition.get(couleurs.get(0)).carte(0);
	}
	private Vector<Byte> couleursNonOuvertes(Vector<Pli> plis_faits)
	{
		Vector<Byte> nb=new Vector<Byte>();
		for(byte couleur=2;couleur<6;couleur++)
			if(tours(couleur,plis_faits).isEmpty())
				nb.addElement(couleur);
		return nb;
	}
	/**Point de vue appel&eacute;: renvoie la carte la plus forte, dans la couleur la moins jouee, la plus longue, la plus faible, s&#39;il existe une figure pour ouvrir, la plus forte carte de la plus faible suite sinon*/
	private CarteTarot ouvrirPreneur(Vector<Vector<MainTarot>> suites,Vector<MainTarot> repartition,Vector<Byte> couleurs,Vector<MainTarot> cartesJouees,MainTarot cartesChien)
	{
		for(int indice=1;indice<couleurs.size();indice++)
		{
			byte couleur1=couleurs.get(0);
			byte couleur2=couleurs.get(indice);
			if(cartesJouees.get(couleur1).total()>cartesJouees.get(couleur2).total())
			{
				couleurs.setElementAt(couleurs.set(0,couleur2),indice);
			}
			else if(cartesJouees.get(couleur1).total()==cartesJouees.get(couleur2).total())
			{
				if(repartition.get(couleur1).total()<repartition.get(couleur2).total())
				{
					couleurs.setElementAt(couleurs.set(0,couleur2),indice);
				}
				else if(repartition.get(couleur1).total()==repartition.get(couleur2).total())
				{
					for(int j=0;j<repartition.get(couleur1).total();j++)
					{
						if(repartition.get(couleur1).carte(j).valeur()>repartition.get(couleur2).carte(j).valeur())
						{
							couleurs.setElementAt(couleurs.set(0,couleur2),indice);
							break;
						}
						else if(repartition.get(couleur1).carte(j).valeur()<repartition.get(couleur2).carte(j).valeur())
							break;
					}
				}
			}
		}
		Vector<MainTarot> cartesMaitresses=cartesMaitresses(repartition,cartesJouees);
		byte couleur_a_jouer=couleurs.get(0);
		if(!cartesMaitresses.isEmpty()&&!cartesMaitresses.get(couleur_a_jouer-2).estVide())
			return(CarteTarot)cartesMaitresses.get(couleur_a_jouer-2).carte(0);
		if(repartition.get(couleur_a_jouer).carte(0).valeur()>13)
			return new CarteTarot((byte)14,couleur_a_jouer);
		if(cartesChien.contient(new CarteTarot((byte)14,couleur_a_jouer))&&repartition.get(couleur_a_jouer).carte(0).valeur()>12)
			return new CarteTarot((byte)13,couleur_a_jouer);
		MainTarot repartition_chien=cartesChien.couleur(couleur_a_jouer);
		if(repartition_chien.total()>1&&repartition_chien.carte(1).valeur()>12&&repartition.get(couleur_a_jouer).carte(0).valeur()>11)
		{
			return new CarteTarot((byte)12,couleur_a_jouer);
		}
		if(repartition_chien.total()>2&&repartition_chien.carte(2).valeur()>11&&repartition.get(couleur_a_jouer).carte(0).valeur()>10)
		{
			return new CarteTarot((byte)12,couleur_a_jouer);
		}
		return(CarteTarot)suites.get(couleur_a_jouer).lastElement().carte(0);
	}
	/**Point de vue d&eacute;fenseur: renvoie la carte la plus petite, dans la couleur sans roi vu au chien, la moins jouee, la plus courte, la plus faible*/
	private static CarteTarot ouvrirPreneur2(Vector<Vector<MainTarot>> suites,Vector<MainTarot> repartition,Vector<Byte> couleurs,Vector<MainTarot> cartesJouees,MainTarot cartesChien)
	{
		for(int indice=1;indice<couleurs.size();indice++)
		{
			byte couleur1=couleurs.get(0);
			byte couleur2=couleurs.get(indice);
			if(cartesChien.contient(new CarteTarot((byte)14,couleur1))&&!cartesChien.contient(new CarteTarot((byte)14,couleur2)))
			{
				couleurs.setElementAt(couleurs.set(0,couleur2),indice);
			}
			else if(cartesJouees.get(couleur1).total()>cartesJouees.get(couleur2).total())
			{
				couleurs.setElementAt(couleurs.set(0,couleur2),indice);
			}
			else if(cartesJouees.get(couleur1).total()==cartesJouees.get(couleur2).total())
			{
				if(repartition.get(couleur1).total()>repartition.get(couleur2).total())
				{
					couleurs.setElementAt(couleurs.set(0,couleur2),indice);
				}
				else if(repartition.get(couleur1).total()==repartition.get(couleur2).total())
				{
					for(int j=0;j<repartition.get(couleur1).total();j++)
					{
						if(repartition.get(couleur1).carte(j).valeur()>repartition.get(couleur2).carte(j).valeur())
						{
							couleurs.setElementAt(couleurs.set(0,couleur2),indice);
							break;
						}
						else if(repartition.get(couleur1).carte(j).valeur()<repartition.get(couleur2).carte(j).valeur())
							break;
					}
				}
			}
		}
		return carteLaPlusPetite(suites.get(couleurs.get(0)));
	}
	private static CarteTarot faireTomberFiguresPreneur(Vector<Vector<MainTarot>> suites,Vector<MainTarot> repartition,Vector<Byte> couleurs,Vector<MainTarot> cartesJouees)
	{
		for(int i=1;i<couleurs.size();i++)
		{
			byte couleur1=couleurs.get(0);
			byte couleur2=couleurs.get(i);
			if(cartesJouees.get(couleur1).total()<cartesJouees.get(couleur2).total())
			{
				couleurs.setElementAt(couleurs.set(0,couleur2),i);
			}
			else if(cartesJouees.get(couleur1).total()==cartesJouees.get(couleur2).total())
			{
				if(repartition.get(couleur1).total()<repartition.get(couleur2).total())
				{
					couleurs.setElementAt(couleurs.set(0,couleur2),i);
				}
				else if(repartition.get(couleur1).total()==repartition.get(couleur2).total())
				{
					for(int j=0;j<repartition.get(couleur1).total();j++)
					{
						if(repartition.get(couleur1).carte(j).valeur()>repartition.get(couleur2).carte(j).valeur())
						{
							couleurs.setElementAt(couleurs.set(0,couleur2),i);
							break;
						}
						else if(repartition.get(couleur1).carte(j).valeur()<repartition.get(couleur2).carte(j).valeur())
							break;
					}
				}
			}
		}
		return carteLaPlusPetite(suites.get(couleurs.get(0)));
	}
	private CarteTarot carteDansCouleursSansCartesMaitresses(Vector<Vector<MainTarot>> suites,Vector<MainTarot> repartition,Vector<Byte> couleurs,Vector<MainTarot> cartesJouees)
	{
		for(int i=1;i<couleurs.size();i++)
		{
			byte couleur1=couleurs.get(0);
			byte couleur2=couleurs.get(i);
			if(cartesJouees.get(couleur1).total()<cartesJouees.get(couleur2).total())
			{
				couleurs.setElementAt(couleurs.set(0,couleur2),i);
			}
			else if(cartesJouees.get(couleur1).total()==cartesJouees.get(couleur2).total())
			{
				if(repartition.get(couleur1).total()<repartition.get(couleur2).total())
				{
					couleurs.setElementAt(couleurs.set(0,couleur2),i);
				}
				else if(repartition.get(couleur1).total()==repartition.get(couleur2).total())
				{
					for(int j=0;j<repartition.get(couleur1).total();j++)
					{
						if(repartition.get(couleur1).carte(j).valeur()>repartition.get(couleur2).carte(j).valeur())
						{
							couleurs.setElementAt(couleurs.set(0,couleur2),i);
							break;
						}
						else if(repartition.get(couleur1).carte(j).valeur()<repartition.get(couleur2).carte(j).valeur())
							break;
					}
				}
			}
		}
		return carteLaPlusPetite(suites.get(couleurs.get(0)));
	}
	private Vector<Byte> couleursAvecUneCarteMaitresse(Vector<MainTarot> cartesMaitresses)
	{
		Vector<Byte> couleurs=new Vector<Byte>();
		for(MainTarot main:cartesMaitresses)
			if(!main.estVide())
				couleurs.addElement(main.carte(0).couleur());
		return couleurs;
	}
	private MainTarot cartesNonMaitresses(Vector<MainTarot> couleurs,Vector<MainTarot> cartes_maitresses,Vector<Vector<MainTarot>> suites)
	{
		MainTarot nb=new MainTarot();
		for (byte couleur=2;couleur<6;couleur++) {
			MainTarot cartes_couleur_maitresse=cartes_maitresses.get(couleur-2);
			if(!couleurs.get(couleur).estVide())
			{
				if(cartes_couleur_maitresse.estVide())
				{
					nb.ajouterCartes(suites.get(couleur).get(0));
				}
				for(byte indice_suite=1;indice_suite<suites.get(couleur).size();indice_suite++)
				{
					nb.ajouterCartes(suites.get(couleur).get(indice_suite));
				}
			}
		}
		return nb;
	}
	private Vector<MainTarot> cartesPseudosMaitresses(Vector<MainTarot> couleurs,Vector<MainTarot> cartesJouees)
	{
		Vector<MainTarot> nb=new Vector<MainTarot>();
		for (byte i=2;i<6;i++) {
			Main cartes=couleurs.get(i);
			MainTarot suite=new MainTarot();
			MainTarot union=new MainTarot();
			union.ajouterCartes(cartesJouees.get(i));//C'est la reunion des cartes jouees dans le jeu et de celles du joueur
			for(Carte carte:cartes)
			{
				if(union.estVide()||union.derniereCarte().valeur()>carte.valeur())
				{
					union.ajouter(carte);
				}
				else
				{
					int k=0;
					for(;union.carte(k).valeur()>carte.valeur();k++);
					union.ajouter(carte,k);
				}
			}
			for(int j=0;j<union.total();j++)
			{
				if(j+union.carte(j).valeur()>=13)
				{
					if(cartes.contient(union.carte(j)))
						suite.ajouter(union.carte(j));
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
	private Vector<Byte> couleursPseudosMaitres(Vector<MainTarot> couleurs,Vector<MainTarot> cartes_pseudomaitres)
	{
		Vector<Byte> nombre=new Vector<Byte>();
		for(byte couleur=2;couleur<6;couleur++)
		{
			if(couleurs.get(couleur).total()==cartes_pseudomaitres.get(couleur-2).total()&&!couleurs.get(couleur).estVide())
			{
				nombre.addElement(couleur);
			}
		}
		return nombre;
	}
	private CarteTarot carteDansCouleursAvecCartesMaitresses(Vector<MainTarot> repartition,Vector<Byte> couleurs,Vector<MainTarot> cartesJouees)
	{
		for(int i=1;i<couleurs.size();i++)
		{
			byte couleur1=couleurs.get(0);
			byte couleur2=couleurs.get(i);
			if(repartition.get(couleur1).carte(0).valeur()<repartition.get(couleur2).carte(0).valeur())
			{
				couleurs.setElementAt(couleurs.set(0,couleur2),i);
			}
			else if(repartition.get(couleur1).carte(0).valeur()==repartition.get(couleur2).carte(0).valeur())
			{
				if(cartesJouees.get(couleur1).total()>cartesJouees.get(couleur2).total())
				{
					couleurs.setElementAt(couleurs.set(0,couleur2),i);
				}
				else if(cartesJouees.get(couleur1).total()==cartesJouees.get(couleur2).total())
				{
					if(repartition.get(couleur1).total()>repartition.get(couleur2).total())
					{
						couleurs.setElementAt(couleurs.set(0,couleur2),i);
					}
				}
			}
		}
		return(CarteTarot)repartition.get(couleurs.get(0)).carte(0);
	}
	private CarteTarot atoutDansLaSuiteLongueForte(Vector<MainTarot> suites)
	{
		for(int i=0;i<1;i++)
		{
			for(int j=i+1;j<suites.size();j++)
			{
				if(suites.get(i).total()<suites.get(j).total())
				{
					MainTarot mt=suites.get(i);
					suites.setElementAt(suites.get(j),i);
					suites.setElementAt(mt,j);
				}
				else if(suites.get(i).total()==suites.get(j).total())
				{
					if(suites.get(i).carte(0).valeur()<suites.get(j).carte(0).valeur())
					{
						MainTarot mt=suites.get(i);
						suites.setElementAt(suites.get(j),i);
						suites.setElementAt(mt,j);
					}
				}
			}
		}
		return (CarteTarot) suites.get(0).carte(0);
	}
	private static CarteTarot carteDansCouleurPeJouLgFai(Vector<Vector<MainTarot>> suites,Vector<MainTarot> repartition,Vector<Byte> couleurs,Vector<MainTarot> cartesJouees)
	{
		for(int i=1;i<couleurs.size();i++)
		{
			byte couleur1=couleurs.get(0);
			byte couleur2=couleurs.get(i);
			if(cartesJouees.get(couleur1).total()<cartesJouees.get(couleur2).total())
			{
				couleurs.setElementAt(couleurs.set(0,couleur2),i);
			}
			else if(cartesJouees.get(couleur1).total()==cartesJouees.get(couleur2).total())
			{
				if(repartition.get(couleur1).total()<repartition.get(couleur2).total())
				{
					couleurs.setElementAt(couleurs.set(0,couleur2),i);
				}
				else if(repartition.get(couleur1).total()==repartition.get(couleur2).total())
				{
					for(int j=0;j<repartition.get(couleur1).total();j++)
					{
						if(repartition.get(couleur1).carte(j).valeur()>repartition.get(couleur2).carte(j).valeur())
						{
							couleurs.setElementAt(couleurs.set(0,couleur2),i);
							break;
						}
						else if(repartition.get(couleur1).carte(j).valeur()<repartition.get(couleur2).carte(j).valeur())
							break;
					}
				}
			}
		}
		return carteLaPlusPetite(suites.get(couleurs.get(0)));
	}
	/**Point de vue appel&eacute;: renvoie la carte la plus faible, dans la couleur avec le moins de figures, la plus jouee, la plus longue, la plus faible*/
	private static CarteTarot jouerCouleurNonAppelee(Vector<Vector<MainTarot>> suites,Vector<MainTarot> repartition,Vector<Byte> couleurs,Vector<MainTarot> cartesJouees)
	{
		for(int indice=1;indice<couleurs.size();indice++)
		{
			byte couleur1=couleurs.get(0);
			byte couleur2=couleurs.get(indice);
			if(repartition.get(couleur1).nombreDeFigures()>repartition.get(couleur2).nombreDeFigures())
			{
				couleurs.setElementAt(couleurs.set(0,couleur2),indice);
			}
			else if(repartition.get(couleur1).nombreDeFigures()==repartition.get(couleur2).nombreDeFigures())
			{
				if(cartesJouees.get(couleur1).total()<cartesJouees.get(couleur2).total())
				{
					couleurs.setElementAt(couleurs.set(0,couleur2),indice);
				}
				else if(cartesJouees.get(couleur1).total()==cartesJouees.get(couleur2).total())
				{
					if(repartition.get(couleur1).total()<repartition.get(couleur2).total())
					{
						couleurs.setElementAt(couleurs.set(0,couleur2),indice);
					}
					else if(repartition.get(couleur1).total()==repartition.get(couleur2).total())
					{
						for(int j=0;j<repartition.get(couleur1).total();j++)
						{
							if(repartition.get(couleur1).carte(j).valeur()>repartition.get(couleur2).carte(j).valeur())
							{
								couleurs.setElementAt(couleurs.set(0,couleur2),indice);
								break;
							}
							else if(repartition.get(couleur1).carte(j).valeur()<repartition.get(couleur2).carte(j).valeur())
								break;
						}
					}
				}
			}
		}
		return carteLaPlusPetite(suites.get(couleurs.get(0)));
	}
	private Carte entame_misere_petite(Vector<Byte> couleurs,Vector<MainTarot> repartition,Vector<MainTarot> repartition_cartes_jouees)
	{
		for(byte indice_couleur=1;indice_couleur<couleurs.size();indice_couleur++)
		{
			byte couleur1=couleurs.get(0);
			byte couleur2=couleurs.get(indice_couleur);
			if(repartition.get(couleur1).total()>repartition.get(couleur2).total())
			{
				couleurs.setElementAt(couleurs.set(0,couleur2),indice_couleur);
			}
			else if(repartition.get(couleur1).total()==repartition.get(couleur2).total())
			{
				if(repartition.get(couleur1).nombreDeFigures()>repartition.get(couleur2).nombreDeFigures())
				{
					couleurs.setElementAt(couleurs.set(0,couleur2),indice_couleur);
				}
				else if(repartition.get(couleur1).nombreDeFigures()==repartition.get(couleur2).nombreDeFigures())
				{
					boolean egal=true;
					for(byte indice_carte=0;indice_carte<repartition.get(couleur1).total();indice_carte++)
					{
						if(repartition.get(couleur1).carte(indice_carte).valeur()>repartition.get(couleur2).carte(indice_carte).valeur())
						{
							couleurs.setElementAt(couleurs.set(0,couleur2),indice_couleur);
							egal=false;
							break;
						}
						else if(repartition.get(couleur1).carte(indice_carte).valeur()<repartition.get(couleur2).carte(indice_carte).valeur())
						{
							egal=false;
							break;
						}
					}
					if(egal&&repartition_cartes_jouees.get(couleur1).nombreDeFigures()<repartition_cartes_jouees.get(couleur2).nombreDeFigures())
					{
						couleurs.setElementAt(couleurs.set(0,couleur2),indice_couleur);
					}
				}
			}
		}
		return repartition.get(couleurs.get(0)).derniereCarte();
	}
	private Carte depouille_figure(Vector<Byte> couleurs,Vector<MainTarot> repartition,Vector<MainTarot> repartition_cartes_jouees)
	{
		for(byte indice_couleur=1;indice_couleur<couleurs.size();indice_couleur++)
		{
			byte couleur1=couleurs.get(0);
			byte couleur2=couleurs.get(indice_couleur);
			if(repartition.get(couleur1).total()>repartition.get(couleur2).total())
			{
				couleurs.setElementAt(couleurs.set(0,couleur2),indice_couleur);
			}
			else if(repartition.get(couleur1).total()==repartition.get(couleur2).total())
			{
				boolean egal=true;
				for(byte indice_carte=0;indice_carte<repartition.get(couleur1).total();indice_carte++)
				{
					if(repartition.get(couleur1).carte(indice_carte).valeur()<repartition.get(couleur2).carte(indice_carte).valeur())
					{
						couleurs.setElementAt(couleurs.set(0,couleur2),indice_couleur);
						egal=false;
						break;
					}
					else if(repartition.get(couleur1).carte(indice_carte).valeur()>repartition.get(couleur2).carte(indice_carte).valeur())
					{
						egal=false;
						break;
					}
				}
				if(egal&&repartition_cartes_jouees.get(couleur1).nombreDeFigures()>repartition_cartes_jouees.get(couleur2).nombreDeFigures())
				{
					couleurs.setElementAt(couleurs.set(0,couleur2),indice_couleur);
				}
			}
		}
		return repartition.get(couleurs.get(0)).carte(0);
	}
	private Carte depouille_petite_carte(Vector<Byte> couleurs,Vector<MainTarot> repartition,Vector<MainTarot> repartition_cartes_jouees)
	{
		for(byte indice_couleur=1;indice_couleur<couleurs.size();indice_couleur++)
		{
			byte couleur1=couleurs.get(0);
			byte couleur2=couleurs.get(indice_couleur);
			if(repartition.get(couleur1).total()>repartition.get(couleur2).total())
			{
				couleurs.setElementAt(couleurs.set(0,couleur2),indice_couleur);
			}
			else if(repartition.get(couleur1).total()==repartition.get(couleur2).total())
			{
				boolean egal=true;
				for(byte indice_carte=0;indice_carte<repartition.get(couleur1).total();indice_carte++)
				{
					if(repartition.get(couleur1).carte(indice_carte).valeur()<repartition.get(couleur2).carte(indice_carte).valeur())
					{
						couleurs.setElementAt(couleurs.set(0,couleur2),indice_couleur);
						egal=false;
						break;
					}
					else if(repartition.get(couleur1).carte(indice_carte).valeur()>repartition.get(couleur2).carte(indice_carte).valeur())
					{
						egal=false;
						break;
					}
				}
				if(egal&&repartition_cartes_jouees.get(couleur1).nombreDeFigures()>repartition_cartes_jouees.get(couleur2).nombreDeFigures())
				{
					couleurs.setElementAt(couleurs.set(0,couleur2),indice_couleur);
				}
			}
		}
		return repartition.get(couleurs.get(0)).derniereCarte();
	}
	public Vector<Annonce> annonces(String[] raison, byte numeroJoueur)
	{
		Vector<Annonce> va=new Vector<Annonce>();
		Vector<Annonce> vaa=getAnnonncesAutorisees();
		MainTarot main_joueur=(MainTarot)getDistribution().main(numeroJoueur);
		Vector<MainTarot> repartition=main_joueur.couleurs();
		if(getEtat()==Etat.Chien_Vu&&numeroJoueur!=0||getEtat()==Etat.Ecart&&numeroJoueur==0)
		{
			if(estUnJeuDeChelem(repartition,getInfos(),getNombreDeJoueurs(),raison))
			{
				raison[0]="Vous aurez finalement plus de chance de gagner comme cela.";
				va.addElement(new Annonce(Primes.Chelem));
			}
			return va;
		}
		Vector<Annonce> vap=getAnnoncesPossibles(numeroJoueur);
		Vector<Annonce> vainter=new Vector<Annonce>();//Intersection entre les annonces auorisees par les regles du jeu et celles
		//peuvent etre annoncees par le joueur si toutes les annonces etaient autorisees
		for(int i=0;i<vaa.size();i++)
		{
			if(vap.contains(vaa.get(i))&&!vaa.get(i).toString().endsWith(Poignees.Poignee.toString()))
				vainter.addElement(vaa.get(i));
		}
		if(vainter.contains(new Annonce(Primes.Chelem)))
			vainter.removeElementAt(vainter.indexOf(new Annonce(Primes.Chelem)));
		if(preneur==numeroJoueur)
		{
			if(estUnJeuDeChelem(repartition,getInfos(),getNombreDeJoueurs(),raison))
			{
				raison[0]="Vous aurez finalement plus de chance de gagner comme cela.";
				va.addElement(new Annonce(Primes.Chelem));
			}
		}
		byte[] bornes=poignees();
		MainTarot atouts=atouts(repartition);
		if(preneur!=numeroJoueur||appele>-1||carteAppelee!=null&&contrat.force()>2||estUnJeuDeChelemSur(repartition))
		{
			if(atouts.total()>=bornes[0]&&atouts.total()<bornes[1])
			{
				va.addElement(new Annonce(Poignees.Poignee));
			}
			else if(atouts.total()>=bornes[1]&&atouts.total()<bornes[2])
			{
				va.addElement(new Annonce(Poignees.Double_Poignee));
			}
			else if(atouts.total()>=bornes[2])
			{
				va.addElement(new Annonce(Poignees.Triple_Poignee));
			}
		}
		va.addAll(vainter);
		return va;
	}
	public Vector<Annonce> getAnnoncesPossibles(byte numero)
	{
		MainTarot main_joueur=(MainTarot)getDistribution().main(numero);
		Vector<MainTarot> repartition=main_joueur.couleurs();
		int nombre_atouts_ex=atoutsAvecExcuse(repartition);
		Vector<Annonce> annonces_possibles=new Vector<Annonce>();
		for(Poignees poignee:Poignees.values())
			if(nombre_atouts_ex>=poignees()[poignee.ordinal()])
				annonces_possibles.addElement(new Annonce(poignee));
		if(nombre_atouts_ex==0)
		{
			annonces_possibles.addElement(new Annonce(Miseres.Atout));
		}
		if(nombre_atouts_ex+main_joueur.nombreDeFigures()==main_joueur.total())
		{
			annonces_possibles.addElement(new Annonce(Miseres.Cartes_basses));
		}
		if(nombre_atouts_ex==main_joueur.total())
		{
			annonces_possibles.addElement(new Annonce(Miseres.Couleur));
		}
		if(main_joueur.nombreDeFigures()==0)
		{
			annonces_possibles.addElement(new Annonce(Miseres.Figure));
		}
		if(main_joueur.nombreDeFigures()+main_joueur.nombreDeBouts()==0)
		{
			annonces_possibles.addElement(new Annonce(Miseres.Tete));
		}
		if(!contrat.equals(new Contrat(Primes.Chelem.toString()))&&preneur==numero)
			annonces_possibles.addElement(new Annonce(Primes.Chelem));
		return annonces_possibles;
	}
	public static int atoutsAvecExcuse(Vector<MainTarot> couleurs)
	{
		return couleurs.get(0).total()+couleurs.get(1).total();
	}
	/**Retourne l'ensemble des annonces autorisees pour cette partie*/
	public Vector<Annonce> getAnnonncesAutorisees()
	{
		Vector<Annonce> va=new Vector<Annonce>();
		String annonces_autorisees=getInfos().get(4);
		int separation=annonces_autorisees.indexOf(';');
		int sep2=annonces_autorisees.indexOf(':');
		int sep3=annonces_autorisees.lastIndexOf(':');
		for(String poignee:annonces_autorisees.substring(sep2+1,separation).split(Parametres.separateur_tiret_slash))
		{
			if(!poignee.isEmpty())
			{
				va.addElement(new Annonce(poignee));
			}
		}
		for(String misere:annonces_autorisees.substring(sep3+1).split(Parametres.separateur_tiret_slash))
		{
			if(!misere.isEmpty())
			{
				va.addElement(new Annonce(misere));
			}
		}
		return va;
	}
	private Carte en_cours(String[] raison)
	{
		byte nombre_joueurs=getNombreDeJoueurs();
		byte numero=(byte)((pliEnCours.getEntameur()+pliEnCours.total())%nombre_joueurs);
		MainTarot main_joueur=(MainTarot)getDistribution().main(numero);
		Vector<MainTarot> repartition_couleurs=main_joueur.couleurs();
		MainTarot cartes_jouables=cartes_jouables(repartition_couleurs);
		if(preneur==-1&&cartes_jouables.total()==1)
		{
			raison[0]="C'est la seule carte a jouer";
			return cartes_jouables.carte(0);
		}
		Carte carteEntamee=pliEnCours.carte(0);
		Vector<Byte> joueurs_non_joue=joueursNAyantPasJoue(numero);
		Vector<Pli> plisFaits=unionPlis();
		if(carteEntamee.couleur()==0&&pliEnCours.total()==1)
		{//Cela se passe comme une entame avec un joueur en moins
			/*C'est une entame sur Excuse*/
			return entame_sur_excuse(raison,joueurs_non_joue,cartes_jouables,plisFaits);
		}
		byte couleurAppelee=couleurAppelee();
		MainTarot cartesJouees=cartesJouees(numero);
		cartesJouees.ajouterCartes(pliEnCours.getCartes());
		Vector<MainTarot> repartitionCartesJouees=cartesJouees.couleurs();
		boolean carteAppeleeJouee=carteAppeleeJouee();
		byte couleurDemandee=pliEnCours.couleurDemandee();
		boolean contientExcuse=cartes_jouables.contient(new CarteTarot((byte)0));
		Vector<Byte> joueurs_joue=new Vector<Byte>();
		for(byte joueur=0;joueur<getNombreDeJoueurs();joueur++)
		{
			if(!joueurs_non_joue.contains(joueur)&&joueur!=numero)
				joueurs_joue.addElement(joueur);
		}
		Vector<Vector<MainTarot>> cartes_possibles=cartesPossibles(!repartitionCartesJouees.get(0).estVide(),repartitionCartesJouees,plisFaits,contientExcuse,main_joueur.couleurs(), numero);
		Vector<Vector<MainTarot>> cartes_certaines=cartesCertaines(cartes_possibles);
		byte ramasseurVirtuel=pliEnCours.getRamasseurTarot(nombre_joueurs);
		CarteTarot carte_forte=(CarteTarot)pliEnCours.carteDuJoueur(ramasseurVirtuel,nombre_joueurs,null);/*Carte temporairement maitresse*/
		Vector<MainTarot> repartition_jou=cartes_jouables.couleurs();
		MainTarot repartition_coule_dem=repartition_jou.get(couleurDemandee);
		Vector<MainTarot> suites=repartition_coule_dem.eclater(repartitionCartesJouees);
		boolean maitre_atout=StrictMaitreAtout(cartes_possibles,numero,repartition_couleurs.get(1).eclater(repartitionCartesJouees),repartitionCartesJouees);
		Vector<Vector<MainTarot>> suites_toute_couleur=new Vector<Vector<MainTarot>>();
		for(byte couleur=0;couleur<6;couleur++)
			suites_toute_couleur.addElement(repartition_couleurs.get(couleur).eclater(repartitionCartesJouees));
		Vector<Byte> couleurs_strictes_maitresses=StrictCouleursMaitres(suites_toute_couleur, repartitionCartesJouees, cartes_possibles, numero);
		Vector<Byte> couleurs_maitresses=CouleursMaitres(suites_toute_couleur,repartitionCartesJouees,cartes_possibles,numero);
		Vector<Byte> tours=tours(couleurDemandee, plisFaits);
		Vector<MainTarot> cartes_maitresses=cartesMaitresses(repartition_couleurs,repartitionCartesJouees);
		boolean maitre_jeu=maitre_atout&&couleurs_maitresses.size()==4;
		byte max;
		boolean carte_maitresse;
		Pli dernier_pli;
		Vector<Byte> dernieres_coupes;
		Vector<Byte> dernieres_defausses;
		Pli pli_le_plus_petit=pliLePlusPetit(plisFaits);
		Vector<Byte> joueurs_susceptibles_de_couper=new Vector<Byte>();
		Vector<MainTarot> cartes_rel_maitres;
		Carte carte_haute_pas_atout;
		Vector<Byte> coupes_franches=coupes_franches_strictes(plisFaits, repartition_couleurs, nombre_joueurs, numero, pli_le_plus_petit);
		int nombre_points = 0;
		byte couleur_atout=1;
		if(preneur>-1)
		{
			changerConfiance(plisFaits,numero,cartes_possibles,cartes_certaines,carteAppeleeJouee);
			if(cartes_jouables.total()==1)
			{
				raison[0]="C'est la seule carte a jouer";
				return cartes_jouables.carte(0);
			}
			/*Variables locales avec jeu d'equipe*/
			Vector<Byte> joueurs_confiance=joueursDeConfiance(numero);
			Vector<Byte> equipe_numero=new Vector<Byte>();
			equipe_numero.addAll(joueurs_confiance);
			equipe_numero.addElement(numero);
			Vector<Byte> joueurs_non_confiance=joueursDeNonConfiance(numero);
			Vector<Byte> joueurs_non_confiance_non_joue=new Vector<Byte>();
			Vector<Byte> joueurs_confiance_non_joue=new Vector<Byte>();
			for(byte joueur:joueurs_confiance)
			{
				if(joueurs_non_joue.contains(joueur))
				{
					joueurs_confiance_non_joue.addElement(joueur);
				}
			}
			for(byte joueur:joueurs_non_confiance)
			{
				if(joueurs_non_joue.contains(joueur))
				{
					joueurs_non_confiance_non_joue.addElement(joueur);
				}
			}
			byte ramasseur_certain=equipe_qui_va_faire_pli(cartes_possibles, cartes_certaines, ramasseurVirtuel, carte_forte, joueurs_non_joue, joueurs_confiance, joueurs_non_confiance, numero, couleurAppelee, carteAppeleeJouee);
			if(ramasseur_certain==0)
			{
				raison[0]="Un adversaire va ramasser le pli.\n";
				if(maitre_jeu&&contientExcuse)
				{
					return new CarteTarot((byte)0);
				}
				if(main_joueur.total()==2&&contientExcuse)
				{
					raison[0]+="Vous ne pourrez pas de toute maniere faire tous les plis.";
					return new CarteTarot((byte)0);
				}
				if(couleurDemandee>1)
				{
					if(cartes_jouables.carte(0).couleur()==couleurDemandee||cartes_jouables.derniereCarte().couleur()==couleurDemandee)
					{
						return carteLaPlusPetite(suites);
					}
					if(cartes_jouables.carte(0).couleur()!=1&&cartes_jouables.derniereCarte().couleur()!=1)
					{/*Si le joueur se defausse*/
						return defausse_couleur_demandee_sur_adversaire(suites_toute_couleur, repartitionCartesJouees, repartition_couleurs, cartes_maitresses, couleurs_strictes_maitresses, couleurDemandee);
					}
				}
				/*La couleur demandee est atout*/
				if(cartes_jouables.carte(0).couleur()==1||cartes_jouables.derniereCarte().couleur()==1)
				{
					return atout_le_plus_petit(repartition_jou.get(1).eclater(repartitionCartesJouees), contientExcuse);
				}
				/*Maintenant le joueur se defausse sur demande atout*/
				return defausse_atout_sur_adversaire(suites_toute_couleur, repartitionCartesJouees, repartition_couleurs, cartes_maitresses, couleurs_strictes_maitresses);
			}
			if(ramasseur_certain==1)
			{
				raison[0]="Un partenaire va ramasser le pli.\n";
				if(maitre_jeu&&!repartition_coule_dem.estVide())
				{
					if(contientExcuse)
					{
						return new CarteTarot((byte)0);
					}
					if(suites.size()==1)
					{
						return repartition_coule_dem.carte(0);
					}
					max=0;
					for(byte joueur:autres_joueurs(numero))
					{
						max=(byte)Math.max(cartes_possibles.get(couleurDemandee).get(joueur).total(),max);
					}
					if(suites.get(0).total()>max&&repartition_coule_dem.carte(0).valeur()>10)
					{
						return repartition_coule_dem.carte(0);
					}
					return carteLaPlusPetite(suites);
				}
				if(main_joueur.total()==2&&contientExcuse)
				{
					if(equipe_numero.containsAll(ramasseurs(plisFaits)))
					{
						return (cartes_jouables.carte(0).couleur()==0)?cartes_jouables.carte(1):cartes_jouables.carte(0);
					}
					return new CarteTarot((byte)0);
				}
				if(couleurDemandee>1)
				{/*couleur demandee non atout*/
					if(cartes_jouables.carte(0).couleur()==couleurDemandee||cartes_jouables.derniereCarte().couleur()==couleurDemandee)
					{
						if(repartition_coule_dem.carte(0).valeur()>10)
						{
							return repartition_coule_dem.carte(0);
						}
						return carteLaPlusPetite(suites);
					}
					if(cartes_jouables.carte(0).couleur()!=1&&cartes_jouables.derniereCarte().couleur()!=1)
					{/*Si le joueur se defausse*/
						return defausse_couleur_demandee_sur_partenaire(suites_toute_couleur, repartitionCartesJouees, repartition_couleurs, cartes_maitresses, couleurs_strictes_maitresses, couleurDemandee);
					}
				}
				/*couleur demandee atout*/
				if(cartes_jouables.carte(0).couleur()==1||cartes_jouables.derniereCarte().couleur()==1)
				{
					repartition_coule_dem=repartition_jou.get(1);
					suites=repartition_coule_dem.eclater(repartitionCartesJouees);
					if(repartition_coule_dem.derniereCarte().valeur()>1||maitre_jeu)
					{
						return atout_le_plus_petit(suites,contientExcuse);
					}
					carte_maitresse=true;
					for(byte joueur:joueurs_non_confiance)
					{
						carte_maitresse&=cartes_possibles.get(1).get(joueur).estVide();
					}
					if(carte_maitresse)
					{
						raison[0]+="Ne pleurez pas, vous allez pouvoir mener votre Petit au bout";
						return atout_le_plus_petit(suites);
					}
					raison[0]+="Vous allez sauver votre Petit maintenant";
					return new CarteTarot((byte)1,(byte)1);
				}
				/*Maintenant le joueur se defausse*/
				return defausse_atout_sur_partenaire(suites_toute_couleur, repartitionCartesJouees, repartition_couleurs, cartes_maitresses, couleurs_strictes_maitresses);
			}
			if(main_joueur.total()==2&&contientExcuse)
			{
				return new CarteTarot((byte)0);
			}
			/*Maintenant on ne peut pas dire qui va faire le pli*/
			if(couleurDemandee>1)
			{
				carte_haute_pas_atout=((MainTarot)pliEnCours.getCartes()).couleurs().get(couleurDemandee).carte(0);
				if(cartes_jouables.carte(0).couleur()==couleurDemandee||cartes_jouables.derniereCarte().couleur()==couleurDemandee)
				{//Si le joueur ne coupe pas et ne se defauuse pas sur la couleur demandee
					cartes_rel_maitres=cartesRelativementMaitreEncours(suites, cartes_possibles, joueurs_non_joue, couleurDemandee, couleurDemandee, cartes_certaines,carte_forte);
					if(repartition_coule_dem.carte(0).valeur()<carte_forte.valeur()||carte_forte.couleur()==1)
					{
						/*Si le joueur ne peut pas prendre la main*/
						if(repartition_coule_dem.carte(0).valeur()<11)
						{/*Si le joueur ne possede pas de figure*/
							if(contientExcuse&&maitre_jeu)
							{
								return new CarteTarot((byte)0);
							}
							return carteLaPlusPetite(suites);
						}
						if(maitre_jeu)
						{
							if(contientExcuse&&maitre_jeu)
							{
								return new CarteTarot((byte)0);
							}
							return carteLaPlusPetite(suites);
						}
						/*Le joueur possede au moins une figure*/
						if(tours.isEmpty())
						{/*Si cette couleur est entamee pour la premiere fois*/
							if(joueurs_confiance.contains(ramasseurVirtuel))
							{
								max=0;
								carte_maitresse=true;
								for(byte joueur:joueurs_non_confiance_non_joue)
								{
									if(!cartes_possibles.get(couleurDemandee).get(joueur).estVide())
									{
										max=(byte)Math.max(cartes_possibles.get(couleurDemandee).get(joueur).carte(0).valeur(),max);
									}
									else
									{
										carte_maitresse=false;
									}
								}
								if(carte_maitresse&&carte_forte.valeur()>max)
								{
									return repartition_coule_dem.carte(0);
								}
							}
							return carteLaPlusPetite(suites);
						}
						dernier_pli=plisFaits.get(tours.lastElement());
						dernieres_coupes=dernier_pli.joueurs_coupes(nombre_joueurs, pli_le_plus_petit,couleur_atout);
						dernieres_defausses=dernier_pli.joueurs_defausses(nombre_joueurs, pli_le_plus_petit,couleur_atout);
						/*Maintenant on aborde au moins le deuxieme tour*/
						if(dernieres_coupes.isEmpty())
						{/*Si le dernier pli n'est pas coupe a cette couleur*/
							if(joueurs_confiance.contains(ramasseurVirtuel))
							{
								if(carte_forte.couleur()==1)
								{/*L'espoir fait vivre*/
									return repartition_coule_dem.carte(0);
								}
								max=0;
								carte_maitresse=true;
								for(byte joueur:joueurs_non_confiance_non_joue)
								{
									if(!cartes_possibles.get(couleurDemandee).get(joueur).estVide())
									{
										max=(byte)Math.max(cartes_possibles.get(couleurDemandee).get(joueur).carte(0).valeur(),max);
									}
									else
									{
										carte_maitresse=false;
									}
								}
								if(carte_maitresse&&carte_forte.valeur()>max)
								{
									return repartition_coule_dem.carte(0);
								}
							}
							return carteLaPlusPetite(suites);
						}
						/*Maintenant on sait qu'au dernier tour le pli a ete coupe*/
						if(joueurs_confiance.contains(ramasseur(plisFaits,tours.lastElement())))
						{
							if(carte_forte.couleur()==1)
							{/*L'espoir fait vivre*/
								return repartition_coule_dem.carte(0);
							}
							max=0;
							carte_maitresse=true;
							for(byte joueur:joueurs_non_confiance_non_joue)
							{
								if(!cartes_possibles.get(couleurDemandee).get(joueur).estVide())
								{
									max=(byte)Math.max(cartes_possibles.get(couleurDemandee).get(joueur).carte(0).valeur(),max);
								}
								else
								{
									carte_maitresse=false;
								}
							}
							if(carte_maitresse&&carte_forte.valeur()>max)
							{
								return repartition_coule_dem.carte(0);
							}
						}
						return carteLaPlusPetite(suites);
					}
					/*Maintenant on sait le joueur peut prendre la main*/
					if(repartition_coule_dem.carte(0).valeur()<11)
					{
						if(maitre_jeu)
						{
							return cartes_rel_maitres.lastElement().carte(0);
						}
						carte_maitresse=true;
						for(byte joueur:joueurs_non_confiance_non_joue)
						{
							if(!defausse(cartes_possibles, joueur, couleurDemandee)&&!ne_peut_pas_avoir_figures(cartes_possibles, joueur, couleurDemandee))
							{
								carte_maitresse=false;
								break;
							}
						}
						if(carte_maitresse)
						{
							if(!cartes_rel_maitres.isEmpty())
							{
								return cartes_rel_maitres.lastElement().carte(0);
							}
						}
						if(tours.isEmpty())
						{
							return repartition_coule_dem.carte(0);
						}
						return carteLaPlusPetite(suites);
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
							if(couleurDemandee!=couleurAppelee)
							{
								if(!cartes_maitresses.get(couleurDemandee-2).estVide())
								{
									if(suites.size()==1||suites.get(1).carte(0).valeur()<11)
									{
										return repartition_coule_dem.carte(0);
									}
									carte_maitresse=true;
									for(byte joueur:joueurs_non_confiance_non_joue)
									{
										if(!defausse(cartes_possibles, joueur, couleurDemandee)&&!ne_peut_pas_avoir_figures(cartes_possibles, joueur, couleurDemandee))
										{
											carte_maitresse=false;
											break;
										}
									}
									if(carte_maitresse&&(carte_forte.valeur()<11||joueurs_confiance.contains(ramasseurVirtuel)))
									{
										return jeuFigureHauteDePlusFaibleSuite(suites);
									}
									if(carte_forte.valeur()>10)
									{
										if(suites.size()==1||suites.get(1).carte(0).valeur()<11||!(joueurs_confiance.contains(ramasseurVirtuel)&&jc_bat_jncnj_non_joue(joueurs_non_confiance_non_joue, couleurDemandee, cartes_possibles, carte_forte)))
										{
											return repartition_coule_dem.carte(0);
										}
										return jeuFigureHauteDePlusFaibleSuite(suites);
									}
									if(joueurs_confiance.contains(ramasseurVirtuel)&&jc_bat_jncnj_non_joue(joueurs_non_confiance_non_joue, couleurDemandee, cartes_possibles, carte_forte))
									{
										return jeuFigureHauteDePlusFaibleSuite(suites);
									}
									return repartition_coule_dem.carte(0);
								}
								/*Le joueur n'a aucune cartes maitresses*/
								carte_maitresse=true;
								for(byte joueur:joueurs_non_confiance_non_joue)
								{
									if(!defausse(cartes_possibles, joueur, couleurDemandee)&&!ne_peut_pas_avoir_figures(cartes_possibles, joueur, couleurDemandee))
									{
										carte_maitresse=false;
										break;
									}
								}
								if(carte_maitresse)
								{
									return repartition_coule_dem.carte(0);
								}
								return carteLaPlusPetite(suites);
							}
							/*La couleur demandee est la couleur appelee*/
							if(cartes_maitresses.get(couleurDemandee-2).estVide())
							{
								return jeuFigureHauteDePlusFaibleSuite(suites);
							}
							return repartition_coule_dem.carte(0);
						}
						/*C'est au moins le deuxieme tour*/
						dernier_pli=plisFaits.get(tours.lastElement());
						dernieres_coupes=dernier_pli.joueurs_coupes(nombre_joueurs, pli_le_plus_petit,couleur_atout);
						dernieres_defausses=dernier_pli.joueurs_defausses(nombre_joueurs, pli_le_plus_petit,couleur_atout);
						for(byte joueur:joueurs_non_joue)
						{
							if(peut_couper(couleurDemandee, joueur, cartes_possibles))
							{
								joueurs_susceptibles_de_couper.addElement(joueur);
							}
						}
						if(!joueurs_susceptibles_de_couper.isEmpty())
						{
							carte_maitresse=false;
							for(byte joueur:joueurs_non_confiance)
							{
								carte_maitresse|=joueurs_susceptibles_de_couper.contains(joueur);
							}
							if(carte_maitresse)
							{
								if(maitre_jeu&&contientExcuse)
								{
									return new CarteTarot((byte)0);
								}
								return carteLaPlusPetite(suites);
							}
							carte_maitresse=false;
							for(byte joueur:joueurs_confiance)
							{
								carte_maitresse|=joueurs_susceptibles_de_couper.contains(joueur);
							}
							if(carte_maitresse)
							{
								if(maitre_jeu)
								{
									if(contientExcuse)
									{
										return new CarteTarot((byte)0);
									}
									max=0;
									for(byte joueur:autres_joueurs(numero))
									{
										max=(byte)Math.max(cartes_possibles.get(couleurDemandee).get(joueur).total(),max);
									}
									if(suites.get(0).total()>max)
									{
										if(repartition_coule_dem.carte(0).valeur()>10)
										{
											return repartition_coule_dem.carte(0);
										}
									}
									return carteLaPlusPetite(suites);
								}
								return jeuFigureHauteDePlusFaibleSuite(suites);
							}
							if(joueurs_confiance.contains(ramasseur(plisFaits,tours.lastElement())))
							{
								if(carte_forte.couleur()==1)
								{/*L'espoir fait vivre*/
									return repartition_coule_dem.carte(0);
								}
								max=0;
								carte_maitresse=true;
								for(byte joueur:joueurs_non_confiance_non_joue)
								{
									if(!cartes_possibles.get(couleurDemandee).get(joueur).estVide())
									{
										max=(byte)Math.max(cartes_possibles.get(couleurDemandee).get(joueur).carte(0).valeur(),max);
									}
									else
									{
										carte_maitresse=false;
									}
								}
								if(carte_maitresse&&carte_forte.valeur()>max)
								{
									return repartition_coule_dem.carte(0);
								}
							}
							return carteLaPlusPetite(suites);
						}
						/*Si la coupe semble improbable*/
						if(!dernieres_defausses.isEmpty()&&tours.size()==1)
						{
							if(maitre_jeu)
							{
								return cartes_rel_maitres.lastElement().carte(0);
							}
							if(couleurDemandee!=couleurAppelee)
							{
								if(!cartes_maitresses.get(couleurDemandee-2).estVide())
								{
									if(suites.size()==1||suites.get(1).carte(0).valeur()<11)
									{
										return repartition_coule_dem.carte(0);
									}
									carte_maitresse=true;
									for(byte joueur:joueurs_non_confiance_non_joue)
									{
										if(!defausse(cartes_possibles, joueur, couleurDemandee)&&!ne_peut_pas_avoir_figures(cartes_possibles, joueur, couleurDemandee))
										{
											carte_maitresse=false;
											break;
										}
									}
									if(carte_maitresse&&(carte_forte.valeur()<11||joueurs_confiance.contains(ramasseurVirtuel)))
									{
										return jeuFigureHauteDePlusFaibleSuite(suites);
									}
									if(carte_forte.valeur()>10)
									{
										if(suites.size()==1||suites.get(1).carte(0).valeur()<11||!(joueurs_confiance.contains(ramasseurVirtuel)&&jc_bat_jncnj_non_joue(joueurs_non_confiance_non_joue, couleurDemandee, cartes_possibles, carte_forte)))
										{
											return repartition_coule_dem.carte(0);
										}
										return jeuFigureHauteDePlusFaibleSuite(suites);
									}
									if(joueurs_confiance.contains(ramasseurVirtuel)&&jc_bat_jncnj_non_joue(joueurs_non_confiance_non_joue, couleurDemandee, cartes_possibles, carte_forte))
									{
										return jeuFigureHauteDePlusFaibleSuite(suites);
									}
								}
								/*Le joueur n'a aucune cartes maitresses*/
								carte_maitresse=true;
								for(byte joueur:joueurs_non_confiance_non_joue)
								{
									if(!defausse(cartes_possibles, joueur, couleurDemandee)&&!ne_peut_pas_avoir_figures(cartes_possibles, joueur, couleurDemandee))
									{
										carte_maitresse=false;
										break;
									}
								}
								if(carte_maitresse)
								{
									return repartition_coule_dem.carte(0);
								}
								return carteLaPlusPetite(suites);
							}
							/*La couleur demandee est la couleur appelee*/
							if(cartes_maitresses.get(couleurDemandee-2).estVide())
							{
								return jeuFigureHauteDePlusFaibleSuite(suites);
							}
							return repartition_coule_dem.carte(0);
						}
						/*Le pli d'avant n'est pas defausse ou c'est au moins le troisieme tour*/
						if(!cartes_maitresses.get(couleurDemandee-2).estVide())
						{
							return repartition_coule_dem.carte(0);
						}
						if(!cartes_rel_maitres.isEmpty())
						{
							return cartes_rel_maitres.lastElement().carte(0);
						}
						return carteLaPlusPetite(suites);
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
							MainTarot cartes_chien=((MainTarot)getDistribution().derniereMain()).couleurs().get(couleurDemandee);
							if(joueurs_non_joue.contains(preneur)&&contrat.force()<3&&cartes_chien.nombreFiguresSuperieurOuEgal(1))
							{/*Si l'appele joue avant le preneur*/
								carte_maitresse=true;
								for(byte joueur:joueurs_non_joue)
								{
									if(joueur!=preneur)
									{
										carte_maitresse&=peut_couper(couleurDemandee,joueur,cartes_possibles);
										if(!carte_maitresse)
										{
											carte_maitresse=true;
											if(!cartes_possibles.get(couleurDemandee).get(joueur).estVide())
											{
												carte_maitresse&=cartes_chien.carte(0).valeur()>cartes_possibles.get(couleurDemandee).get(joueur).carte(0).valeur();
											}
										}
										else
										{
											if(!cartes_possibles.get(1).get(joueur).estVide())
											{
												carte_maitresse=false;
												break;
											}
											carte_maitresse=true;
										}
									}
								}
								if(carte_maitresse&&repartition_coule_dem.carte(0).valeur()>10)
								{
									return jeuFigureHauteDePlusFaibleSuite(suites);
								}
							}
							if(!joueurs_non_joue.contains(preneur))
							{/*Si l'appele joue avant le preneur*/
								carte_maitresse=true;
								for(byte joueur:joueurs_non_joue)
								{
									carte_maitresse&=peut_couper(couleurDemandee,joueur,cartes_possibles);
									if(!carte_maitresse)
									{
										carte_maitresse=true;
										if(!cartes_possibles.get(couleurDemandee).get(joueur).estVide()&&!cartes_chien.estVide())
										{
											carte_maitresse&=cartes_chien.carte(0).valeur()>cartes_possibles.get(couleurDemandee).get(joueur).carte(0).valeur();
										}
									}
									else
									{
										if(!cartes_possibles.get(1).get(joueur).estVide())
										{
											carte_maitresse=false;
											break;
										}
										carte_maitresse=true;
									}
								}
								if(carte_maitresse&&repartition_coule_dem.carte(0).valeur()>10&&!cartes_rel_maitres.isEmpty())
								{
									if(cartes_rel_maitres.size()==1||cartes_rel_maitres.get(1).carte(0).valeur()<11)
									{
										return cartes_rel_maitres.get(0).carte(0);
									}
									return cartes_rel_maitres.get(1).carte(0);
								}
							}
							if(maitre_jeu)
							{
								return cartes_rel_maitres.lastElement().carte(0);
							}
							if(couleurDemandee!=couleurAppelee)
							{
								if(!cartes_maitresses.get(couleurDemandee-2).estVide())
								{
									if(suites.size()==1||suites.get(1).carte(0).valeur()<11)
									{
										return repartition_coule_dem.carte(0);
									}
									carte_maitresse=true;
									for(byte joueur:joueurs_non_confiance_non_joue)
									{
										if(!defausse(cartes_possibles, joueur, couleurDemandee)&&!ne_peut_pas_avoir_figures(cartes_possibles, joueur, couleurDemandee))
										{
											carte_maitresse=false;
											break;
										}
									}
									if(carte_maitresse&&(carte_forte.valeur()<11||joueurs_confiance.contains(ramasseurVirtuel)))
									{
										return jeuFigureHauteDePlusFaibleSuite(suites);
									}
									if(carte_forte.valeur()>10)
									{
										if(suites.size()==1||suites.get(1).carte(0).valeur()<11||!(joueurs_confiance.contains(ramasseurVirtuel)&&jc_bat_jncnj_non_joue(joueurs_non_confiance_non_joue, couleurDemandee, cartes_possibles, carte_forte)))
										{
											return repartition_coule_dem.carte(0);
										}
										return jeuFigureHauteDePlusFaibleSuite(suites);
									}
									if(joueurs_confiance.contains(ramasseurVirtuel)&&jc_bat_jncnj_non_joue(joueurs_non_confiance_non_joue, couleurDemandee, cartes_possibles, carte_forte))
									{
										return jeuFigureHauteDePlusFaibleSuite(suites);
									}
									return repartition_coule_dem.carte(0);
								}
								/*Le joueur n'a aucune cartes maitresses*/
								carte_maitresse=true;
								for(byte joueur:joueurs_non_confiance_non_joue)
								{
									if(!defausse(cartes_possibles, joueur, couleurDemandee)&&!ne_peut_pas_avoir_figures(cartes_possibles, joueur, couleurDemandee))
									{
										carte_maitresse=false;
										break;
									}
								}
								if(carte_maitresse)
								{
									return repartition_coule_dem.carte(0);
								}
								return carteLaPlusPetite(suites);
							}
							/*La couleur demandee est la couleur appelee*/
							if(cartes_maitresses.get(couleurDemandee-2).estVide())
							{
								return jeuFigureHauteDePlusFaibleSuite(suites);
							}
							return repartition_coule_dem.carte(0);
						}
						dernier_pli=plisFaits.get(tours.lastElement());
						dernieres_coupes=dernier_pli.joueurs_coupes(nombre_joueurs, pli_le_plus_petit,couleur_atout);
						dernieres_defausses=dernier_pli.joueurs_defausses(nombre_joueurs, pli_le_plus_petit,couleur_atout);
						/*Deuxieme tour pour un appele ne coupant pas la couleur demandee differente de l'atout*/
						for(byte joueur:joueurs_non_joue)
						{
							if(peut_couper(couleurDemandee, joueur, cartes_possibles))
							{
								joueurs_susceptibles_de_couper.addElement(joueur);
							}
						}
						if(!joueurs_susceptibles_de_couper.isEmpty())
						{
							carte_maitresse=false;
							for(byte joueur:joueurs_non_confiance)
							{
								carte_maitresse|=joueurs_susceptibles_de_couper.contains(joueur);
							}
							if(carte_maitresse)
							{
								if(maitre_jeu&&contientExcuse)
								{
									return new CarteTarot((byte)0);
								}
								return carteLaPlusPetite(suites);
							}
							carte_maitresse=false;
							for(byte joueur:joueurs_confiance)
							{
								carte_maitresse|=joueurs_susceptibles_de_couper.contains(joueur);
							}
							if(carte_maitresse)
							{
								if(maitre_jeu)
								{
									if(contientExcuse)
									{
										return new CarteTarot((byte)0);
									}
									max=0;
									for(byte joueur:autres_joueurs(numero))
									{
										max=(byte)Math.max(cartes_possibles.get(couleurDemandee).get(joueur).total(),max);
									}
									if(suites.get(0).total()>max)
									{
										if(repartition_coule_dem.carte(0).valeur()>10)
										{
											return repartition_coule_dem.carte(0);
										}
									}
									return carteLaPlusPetite(suites);
								}
								return jeuFigureHauteDePlusFaibleSuite(suites);
							}
							if(joueurs_confiance.contains(ramasseur(plisFaits,tours.lastElement())))
							{
								if(carte_forte.couleur()==1)
								{/*L'espoir fait vivre*/
									return repartition_coule_dem.carte(0);
								}
								max=0;
								carte_maitresse=true;
								for(byte joueur:joueurs_non_confiance_non_joue)
								{
									if(!cartes_possibles.get(couleurDemandee).get(joueur).estVide())
									{
										max=(byte)Math.max(cartes_possibles.get(couleurDemandee).get(joueur).carte(0).valeur(),max);
									}
									else
									{
										carte_maitresse=false;
									}
								}
								if(carte_maitresse&&carte_forte.valeur()>max)
								{
									return repartition_coule_dem.carte(0);
								}
							}
							return carteLaPlusPetite(suites);
						}
						/*Si la coupe semble improbable*/
						if(!dernieres_defausses.isEmpty()&&tours.size()==1)
						{
							if(maitre_jeu)
							{
								return cartes_rel_maitres.lastElement().carte(0);
							}
							if(couleurDemandee!=couleurAppelee)
							{
								if(!cartes_maitresses.get(couleurDemandee-2).estVide())
								{
									if(suites.size()==1||suites.get(1).carte(0).valeur()<11)
									{
										return repartition_coule_dem.carte(0);
									}
									carte_maitresse=true;
									for(byte joueur:joueurs_non_confiance_non_joue)
									{
										if(!defausse(cartes_possibles, joueur, couleurDemandee)&&!ne_peut_pas_avoir_figures(cartes_possibles, joueur, couleurDemandee))
										{
											carte_maitresse=false;
											break;
										}
									}
									if(carte_maitresse&&(carte_forte.valeur()<11||joueurs_confiance.contains(ramasseurVirtuel)))
									{
										return jeuFigureHauteDePlusFaibleSuite(suites);
									}
									if(carte_forte.valeur()>10)
									{
										if(suites.size()==1||suites.get(1).carte(0).valeur()<11||!(joueurs_confiance.contains(ramasseurVirtuel)&&jc_bat_jncnj_non_joue(joueurs_non_confiance_non_joue, couleurDemandee, cartes_possibles, carte_forte)))
										{
											return repartition_coule_dem.carte(0);
										}
										return jeuFigureHauteDePlusFaibleSuite(suites);
									}
									if(joueurs_confiance.contains(ramasseurVirtuel)&&jc_bat_jncnj_non_joue(joueurs_non_confiance_non_joue, couleurDemandee, cartes_possibles, carte_forte))
									{
										return jeuFigureHauteDePlusFaibleSuite(suites);
									}
									return repartition_coule_dem.carte(0);
								}
								/*Le joueur n'a aucune cartes maitresses*/
								carte_maitresse=true;
								for(byte joueur:joueurs_non_confiance_non_joue)
								{
									if(!defausse(cartes_possibles, joueur, couleurDemandee)&&!ne_peut_pas_avoir_figures(cartes_possibles, joueur, couleurDemandee))
									{
										carte_maitresse=false;
										break;
									}
								}
								if(carte_maitresse)
								{
									return repartition_coule_dem.carte(0);
								}
								return carteLaPlusPetite(suites);
							}
							/*La couleur demandee est la couleur appelee*/
							if(cartes_maitresses.get(couleurDemandee-2).estVide())
							{
								return jeuFigureHauteDePlusFaibleSuite(suites);
							}
							return repartition_coule_dem.carte(0);
						}
						/*Le pli d'avant n'est pas defausse ou c'est au moins le troisieme tour*/
						if(!cartes_maitresses.get(couleurDemandee-2).estVide())
						{
							return repartition_coule_dem.carte(0);
						}
						if(!cartes_rel_maitres.isEmpty())
						{
							return cartes_rel_maitres.lastElement().carte(0);
						}
						return carteLaPlusPetite(suites);
					}
					/*Defenseur*/
					if(tours.isEmpty())
					{
						if(maitre_jeu)
						{
							return cartes_rel_maitres.lastElement().carte(0);
						}
						if(couleurDemandee==couleurAppelee)
						{
							if(carteAppelee.valeur()==14&&pliEnCours.total()+2==nombre_joueurs&&joueurs_non_joue.get(0)==preneur)
							{
								if(!cartes_certaines.get(couleurDemandee).get(preneur).contient(carteAppelee))
								{
									if(repartition_coule_dem.carte(0).valeur()==13)
									{/*Tant pis si la dame du defenseur se fait prendre par le roi appele du preneur s'appelant volontairement*/
										return repartition_coule_dem.carte(0);
									}
								}
							}
							if(!cartes_certaines.get(1).get(preneur).estVide()&&cartes_possibles.get(couleurDemandee).get(preneur).estVide())
							{
								return carteLaPlusPetite(suites);
							}
							for(byte joueur:joueurs_non_joue)
							{
								if(!cartes_certaines.get(1).get(joueur).estVide()&&cartes_possibles.get(couleurDemandee).get(joueur).estVide())
								{
									return repartition_coule_dem.carte(0);
								}
							}
							return carteLaPlusPetite(suites);
						}
						if(pas_atout(joueurs_non_confiance_non_joue, cartes_possibles))
						{
							return sauve_qui_peut_figure(cartes_possibles, suites, cartes_rel_maitres, joueurs_non_confiance_non_joue, couleurDemandee);
						}
						if(!joueurs_non_joue.contains(preneur)||carte_forte.valeur()>10)
						{/*Si le joueur (defenseur) va jouer apres le preneur et il reste des joueurs susceptibles d'etre l'appele ou il existe une figure que peut prendre le joueur*/
							if(!cartes_rel_maitres.isEmpty())
							{
								if(cartes_rel_maitres.size()==1||cartes_rel_maitres.get(1).carte(0).valeur()<11)
									return suites.get(0).carte(0);
								return cartes_rel_maitres.get(1).carte(0);
							}
							return carteLaPlusPetite(suites);
						}
						if(!cartes_rel_maitres.isEmpty()&&cartes_rel_maitres.get(0).total()==1&&repartition_coule_dem.total()==2)
						{
							if(suites.size()==1)
							{
								return repartition_coule_dem.carte(0);
							}
							if(joueurs_confiance.contains(ramasseurVirtuel))
							{
								carte_maitresse=true;
								for(byte joueur:joueurs_non_confiance_non_joue)
								{
									boolean local=false;
									local|=defausse(cartes_possibles,joueur,couleurDemandee);
									if(!cartes_possibles.get(couleurDemandee).get(joueur).estVide())
									{
										local|=cartes_possibles.get(couleurDemandee).get(joueur).carte(0).valeur()<carte_forte.valeur();
									}
									carte_maitresse&=local;
								}
								if(carte_maitresse)
								{
									return repartition_coule_dem.carte(1);
								}
							}
							return repartition_coule_dem.carte(0);
						}
						return carteLaPlusPetite(suites);
					}
					/*Maintenant on est au moins au deuxieme tour*/
					if(maitre_jeu)
					{
						return cartes_rel_maitres.lastElement().carte(0);
					}
					if(couleurAppelee==couleurDemandee)
					{
						if(carteAppeleeJouee)
						{
							if(!cartes_maitresses.get(couleurDemandee-2).estVide()&&cartes_maitresses.get(couleurDemandee-2).carte(0).valeur()>10)
							{
								if(carteAppelee.valeur()==14)
								{
									return jeuFigureHauteDePlusFaibleSuite(suites);
								}
								return repartition_coule_dem.carte(0);
							}
							if(pas_atout(joueurs_non_confiance_non_joue, cartes_possibles))
							{
								return sauve_qui_peut_figure(cartes_possibles, suites, cartes_rel_maitres, joueurs_non_confiance_non_joue, couleurDemandee);
							}
						}
						return carteLaPlusPetite(suites);
					}
					if(pas_atout(joueurs_non_confiance_non_joue, cartes_possibles))
					{
						return sauve_qui_peut_figure(cartes_possibles, suites, cartes_rel_maitres, joueurs_non_confiance_non_joue, couleurDemandee);
					}
					return carteLaPlusPetite(suites);
				}
				if(cartes_jouables.carte(0).couleur()==1||cartes_jouables.derniereCarte().couleur()==1)
				{//Si le joueur coupe la couleur demandee maintenant
					suites=repartition_jou.get(1).eclater(repartitionCartesJouees);
					cartes_rel_maitres=cartesRelativementMaitreEncours(suites, cartes_possibles, joueurs_non_joue, (byte) 1, couleurDemandee, cartes_certaines,carte_forte);
					repartition_coule_dem=repartition_jou.get(1);
					if(repartition_coule_dem.carte(0).valeur()<carte_forte.valeur()&&carte_forte.couleur()==1)
					{/*Si le joueur ne peut pas surcouper*/
						if(repartition_coule_dem.derniereCarte().valeur()>1)
						{/*Si le joueur n'a pas le Petit*/
							return atout_le_plus_petit(suites, contientExcuse);
						}
						carte_maitresse=true;
						for(byte joueur:joueurs_non_confiance)
						{
							carte_maitresse&=cartes_possibles.get(1).get(joueur).estVide();
						}
						if(carte_maitresse)
						{
							raison[0]="Ne pleurez pas, vous allez pouvoir mener votre Petit au bout";
							return atout_le_plus_petit(suites);
						}
						if(tours.isEmpty())
						{/*Si c'est le premier tour*/
							if(joueurs_confiance.contains(ramasseurVirtuel))
							{
								raison[0]="Vous allez tres probablement sauver votre Petit";
								return repartition_coule_dem.derniereCarte();
							}
							return atout_le_plus_petit(suites,contientExcuse);
						}
						/*Deuxieme tour et plus*/
						if(joueurs_confiance.contains(ramasseurVirtuel))
						{
							if(repartitionCartesJouees.get(couleurDemandee).total()<8||pliEnCours.joueurs_coupes(nombre_joueurs, null,couleur_atout).size()>1)
							{
								raison[0]="Vous allez tres probablement sauver votre Petit";
								return repartition_coule_dem.derniereCarte();
							}
						}
						return atout_le_plus_petit(suites,contientExcuse);
					}
					if(tours.isEmpty())
					{
						/*Le joueur peut surcouper si le pli est deja coupe ou couper avec n'importe quel atout*/
						if(repartition_jou.get(1).derniereCarte().valeur()>1)
						{/*Si le joueur ne peut pas couper avec le Petit*/
							if(!contientExcuse)
							{
								if(maitre_jeu)
								{
									return cartes_rel_maitres.lastElement().carte(0);
								}
								return atout_le_plus_petit(suites,contientExcuse);
							}
							/*Maintenant le joueur possede l'Excuse*/
							if(carte_haute_pas_atout.valeur()>10)
							{/*S'il existe une figure de la couleur demandee*/
								return atout_le_plus_petit(suites);
							}
							if(carte_forte.couleur()>1)
							{/*Si le joueur ne surcoupe pas un autre joueur alors il n'a pas le Petit par hypothese par (m.derniereCarte().getValeur()>1)*/
								if(repartitionCartesJouees.get(couleurDemandee).nombreFiguresEgal(4)||joueurs_non_confiance_non_joue.isEmpty())
								{
									return new CarteTarot((byte)0);
								}
							}
							if(!carteAppeleeJouee&&appele>-1&&numero==preneur&&!pliEnCours.contient(new CarteTarot((byte)1,(byte)1)))
							{
								return new CarteTarot((byte)0);
							}
							return atout_le_plus_petit(suites);
						}
						/*Le joueur peut couper avec le Petit*/
						if(maitre_jeu)
						{
							if(contientExcuse)
							{
								return new CarteTarot((byte)0);
							}
							if(cartes_rel_maitres.lastElement().derniereCarte().valeur()>1||cartes_rel_maitres.lastElement().total()>1)
							{
								return cartes_rel_maitres.lastElement().carte(0);
							}
							return cartes_rel_maitres.get(cartes_rel_maitres.size()-2).carte(0);
						}
						carte_maitresse=true;
						for(byte joueur:joueurs_non_confiance)
						{
							carte_maitresse&=cartes_possibles.get(1).get(joueur).estVide();
						}
						if(carte_maitresse)
						{
							raison[0]="Ne pleurez pas, vous allez pouvoir mener votre Petit au bout";
							return atout_le_plus_petit(suites);
						}
						if(coupes_franches.size()==1)
						{
							if(numero==preneur)
							{
								if(repartition_coule_dem.total()+nombre_joueurs<=13)
								{
									return new CarteTarot((byte)1,(byte)1);
								}
								carte_maitresse=true;
								for(byte joueur:joueurs_non_confiance_non_joue)
								{
									carte_maitresse&=ne_peut_couper(couleurDemandee, joueur, cartes_possibles, cartes_certaines);
								}
								if(carte_maitresse)
								{
									raison[0]="Ne pleurez pas, votre Petit est en securite en le jouant maintenant";
									return new CarteTarot((byte)1,(byte)1);
								}
								return atout_le_plus_petit(suites);
							}
							/*Appele*/
							if(numero==appele)
							{
								return new CarteTarot((byte)1,(byte)1);
							}
							if(repartition_coule_dem.total()<5)
							{
								return new CarteTarot((byte)1,(byte)1);
							}
							carte_maitresse=true;
							for(byte joueur:joueurs_non_confiance_non_joue)
							{
								carte_maitresse&=ne_peut_couper(couleurDemandee, joueur, cartes_possibles, cartes_certaines);
							}
							if(carte_maitresse)
							{
								raison[0]="Ne pleurez pas, votre Petit est en securite en le jouant maintenant";
								return new CarteTarot((byte)1,(byte)1);
							}
							if(repartition_coule_dem.total()>1||repartition_coule_dem.carte(0).valeur()>1)
							{
								return atout_le_plus_petit(suites);
							}
							return new CarteTarot((byte)1,(byte)1);
						}
						carte_maitresse=true;
						/*Il existe au moins deux coupes franches*/
						for(byte coupe:coupes_franches)
						{
							if(coupe!=couleurDemandee)
							{
								carte_maitresse&=!tours(coupe, plisFaits).isEmpty();
							}
						}
						if(carte_maitresse)
						{
							return new CarteTarot((byte)1,(byte)1);
						}
						if(contrat.force()>2)
						{
							carte_maitresse=false;
							for(byte joueur:joueurs_non_confiance_non_joue)
							{
								carte_maitresse|=!cartes_possibles.get(1).get(joueur).estVide();
							}
							if(carte_maitresse)
							{
								if(repartition_coule_dem.total()>1||repartition_coule_dem.carte(0).valeur()>1)
								{
									return atout_le_plus_petit(suites);
								}
								return new CarteTarot((byte)1,(byte)1);
							}
						}
						carte_maitresse=true;
						for(byte joueur:joueurs_non_confiance_non_joue)
						{
							carte_maitresse&=ne_peut_couper(couleurDemandee, joueur, cartes_possibles, cartes_certaines);
						}
						if(carte_maitresse)
						{
							return new CarteTarot((byte)1,(byte)1);
						}
						if(contrat.force()<3&&numero==preneur)
						{
							Vector<Byte> coupes_non_joues=new Vector<Byte>();
							for(byte coupe:coupes_franches)
							{
								if(coupe!=couleurDemandee)
								{
									if(tours(coupe, plisFaits).isEmpty()&&((MainTarot)plisFaits.get(0).getCartes()).couleurs().get(coupe).total()<((MainTarot)plisFaits.get(0).getCartes()).couleurs().get(couleurDemandee).total())
									{
										coupes_non_joues.addElement(coupe);
									}
								}
							}
							if(!coupes_non_joues.isEmpty())
							{
								if(repartition_coule_dem.total()>1||repartition_coule_dem.carte(0).valeur()>1)
								{
									return atout_le_plus_petit(suites);
								}
								return new CarteTarot((byte)1,(byte)1);
							}
						}
						if(repartition_coule_dem.total()+nombre_joueurs<=15)
						{
							return new CarteTarot((byte)1,(byte)1);
						}
						if(repartition_coule_dem.total()>1||repartition_coule_dem.carte(0).valeur()>1)
						{
							return atout_le_plus_petit(suites);
						}
						return new CarteTarot((byte)1,(byte)1);
					}
					/*Deuxieme tour et plus*/
					if(repartition_jou.get(1).derniereCarte().valeur()>1)
					{
						if(maitre_jeu)
						{
							return cartes_rel_maitres.lastElement().carte(0);
						}
						carte_maitresse=false;
						for (byte joueur:joueurs_non_confiance_non_joue)
						{
							carte_maitresse|=peut_couper(couleurDemandee, joueur, cartes_possibles);
						}
						if(carte_maitresse)
						{
							nombre_points=0;
							for(Carte carte:pliEnCours)
							{
								CarteTarot carte_jouee=(CarteTarot)carte;
								if(carte_jouee.couleur()>0)
								{
									nombre_points+=carte_jouee.points();
								}
							}
							if(nombre_points>7)
							{
								if(!cartes_rel_maitres.isEmpty())
								{
									raison[0]="Capturer des figures ou le Petit est tres interessant";
									return cartes_rel_maitres.lastElement().carte(0);
								}
								carte_maitresse=false;
								for(byte joueur:joueurs_non_joue)
								{
									carte_maitresse|=va_surcouper(cartes_possibles, cartes_certaines, numero, joueur, couleurDemandee);
									if(carte_maitresse)
									{
										raison[0]="Rien ne sert d'acheter si on n'a pas les moyens";
										return atout_le_plus_petit(suites,contientExcuse);
									}
								}
								carte_maitresse=false;
								for(byte joueur:joueurs_non_joue)
								{
									boolean local=true;
									for(byte joueur2:joueurs_joue)
									{
										local&=peut_surcouper(cartes_possibles,joueur2, joueur, couleurDemandee);
									}
									carte_maitresse|=local;
									if(carte_maitresse)
									{
										return atout_le_plus_petit(suites,contientExcuse);
									}
								}
								return repartition_jou.get(1).carte(0);
							}
							/*Moins de 8 points (les points sont doubles pour eviter les 1/2 points)*/
							return suites_toute_couleur.get(1).lastElement().carte(0);
						}
						/*Il n'est pas probable qu'un joueur de non confiance n'ayant pas joue coupe le pli*/
						if(carte_forte.couleur()==1)
						{/*Si le pli est deja coupe*/
							if(joueurs_confiance.contains(ramasseurVirtuel))
							{
								return atout_le_plus_petit(suites, contientExcuse);
							}
							return atout_le_plus_petit(suites);
						}
						if(carte_haute_pas_atout.valeur()<11)
						{
							carte_maitresse=true;
							for(byte joueur:joueurs_non_confiance_non_joue)
							{
								carte_maitresse&=cartes_possibles.get(couleurDemandee).get(joueur).estVide()||cartes_possibles.get(couleurDemandee).get(joueur).carte(0).valeur()<11;
							}
							if(carte_maitresse)
							{
								return atout_le_plus_petit(suites,contientExcuse);
							}
							if(joueurs_confiance.contains(ramasseurVirtuel))
							{
								carte_maitresse=true;
								for(byte joueur:joueurs_non_confiance_non_joue)
								{
									carte_maitresse&=!peut_couper(couleurDemandee,joueur,cartes_possibles)&&(cartes_possibles.get(couleurDemandee).get(joueur).estVide()||cartes_possibles.get(couleurDemandee).get(joueur).carte(0).valeur()<carte_forte.valeur());
								}
								if(carte_maitresse)
								{
									return atout_le_plus_petit(suites,contientExcuse);
								}
							}
						}
						return atout_le_plus_petit(suites);
					}
					/*Le joueur possede le Petit et c'est le duxieme tour a cette couleur ou plus*/
					carte_maitresse=true;
					for(byte joueur:joueurs_non_confiance)
					{
						carte_maitresse&=cartes_possibles.get(1).get(joueur).estVide();
					}
					if(carte_maitresse)
					{
						raison[0]="Ne pleurez pas, vous allez pouvoir mener votre Petit au bout";
						return atout_le_plus_petit(suites);
					}
					if(maitre_jeu)
					{
						return cartes_rel_maitres.lastElement().carte(0);
					}
					carte_maitresse=true;
					for(byte joueur:joueurs_non_confiance_non_joue)
					{
						carte_maitresse&=ne_peut_couper(couleurDemandee,joueur,cartes_possibles,cartes_certaines);
					}
					if(carte_maitresse)
					{
						raison[0]="Ne pleurez pas, aucun joueur de non confiance ne peut surcouper votre Petit";
						return new CarteTarot((byte)1,(byte)1);
					}
					if(nombre_joueurs<5)
					{
						if(tours.size()==1)
						{
							if(repartition_coule_dem.total()>1||repartition_coule_dem.carte(0).valeur()>1)
							{
								return atout_le_plus_petit(suites);
							}
							Vector<Byte> joueurs_coupe_pre_tour=plisFaits.get(tours.get(0)).joueurs_coupes(nombre_joueurs,pli_le_plus_petit,couleur_atout);
							carte_maitresse=true;
							for(byte joueur:joueurs_non_confiance)
							{
								carte_maitresse&=!joueurs_coupe_pre_tour.contains(joueur);
							}
							if(carte_maitresse)
							{
								raison[0]="Seuls des joueurs de confiance ont eventuellement coupe au premier tour de cette couleur,\nvous avez de grandes chances de sauver votre Petit maintemant";
								return new CarteTarot((byte)1,(byte)1);
							}
						}
					}
					/*Le jeu s'effectue maintenant a 5 joueurs*/
					if(repartition_coule_dem.total()>1||repartition_coule_dem.carte(0).valeur()>1)
					{
						return atout_le_plus_petit(suites);
					}
					return new CarteTarot((byte)1,(byte)1);
				}
				/*Le joueur se defausse sur la couleur demandee*/
				if(tours.isEmpty())
				{
					if(joueurs_confiance.contains(ramasseurVirtuel))
					{
						return defausse_couleur_demandee_sur_partenaire(suites_toute_couleur, repartitionCartesJouees, repartition_couleurs, cartes_maitresses, couleurs_strictes_maitresses, couleurDemandee);
					}
					/*Le ramasseur virtuel n'est pas un joueur de confiance*/
					return defausse_couleur_demandee_sur_adversaire(suites_toute_couleur, repartitionCartesJouees, repartition_couleurs, cartes_maitresses, couleurs_strictes_maitresses, couleurDemandee);
				}
				/*Au dexieme tour et au de la il est preferable de jouer une petite carte*/
				Vector<Byte> couleurs=new Vector<Byte>();
				for(byte couleur=2;couleur<6;couleur++)
				{
					if(!repartition_couleurs.get(couleur).estVide()&&repartition_couleurs.get(couleur).carte(0).valeur()<11)
					{
						couleurs.addElement(couleur);
					}
				}
				if(!couleurs.isEmpty())
				{
					return jouer_petite_carte_defausse(suites_toute_couleur,couleurs,repartition_couleurs,repartitionCartesJouees);
				}
				for(byte couleur=2;couleur<6;couleur++)
				{
					if(!repartition_couleurs.get(couleur).estVide())
					{
						couleurs.addElement(couleur);
					}
				}
				return jeu_petite_carte_couleur_figure(suites_toute_couleur, couleurs, repartition_couleurs, repartitionCartesJouees);
			}
			/*Demande atout*/
			if(cartes_jouables.carte(0).couleur()==1||cartes_jouables.derniereCarte().couleur()==1)
			{
				suites=repartition_jou.get(1).eclater(repartitionCartesJouees);
				cartes_rel_maitres=cartesRelativementMaitreEncours(suites, cartes_possibles, joueurs_non_joue, (byte) 1, couleurDemandee, cartes_certaines,carte_forte);
				repartition_coule_dem=repartition_jou.get(1);
				if(repartition_coule_dem.carte(0).valeur()<carte_forte.valeur())
				{
					return atout_le_plus_petit(suites,contientExcuse);
				}
				if(pliEnCours.carte(0).valeur()==1)
				{
					if(!cartes_rel_maitres.isEmpty())
					{
						return cartes_rel_maitres.lastElement().carte(0);
					}
					if(peut_ramasser_atout(cartes_possibles, cartes_certaines, numero, joueurs_non_joue))
					{
						return repartition_coule_dem.carte(0);
					}
					return suites.lastElement().carte(0);
				}
				if(maitre_jeu)
				{
					return cartes_rel_maitres.lastElement().carte(0);
				}
				carte_maitresse=true;
				for(byte joueur:joueurs_non_confiance_non_joue)
				{
					carte_maitresse&=cartes_possibles.get(1).get(joueur).estVide()||cartes_possibles.get(1).get(joueur).carte(0).valeur()<carte_forte.valeur();
				}
				if(carte_maitresse&&joueurs_confiance.contains(ramasseurVirtuel))
				{
					return atout_le_plus_petit(suites,contientExcuse);
				}
				nombre_points=0;
				for(Carte carte:pliEnCours)
				{
					CarteTarot carte_jouee=(CarteTarot)carte;
					if(carte_jouee.couleur()>0)
					{
						nombre_points+=carte_jouee.points();
					}
				}
				if(nombre_points>6)
				{
					if(!cartes_rel_maitres.isEmpty())
					{
						return cartes_rel_maitres.lastElement().carte(0);
					}
					return atout_le_plus_petit(suites);
				}
				carte_maitresse=true;
				for(byte joueur:joueurs_non_confiance_non_joue)
				{
					carte_maitresse&=cartes_possibles.get(1).get(joueur).estVide()||cartes_possibles.get(1).get(joueur).carte(0).valeur()<carte_forte.valeur();
				}
				if(carte_maitresse)
				{
					return atout_le_plus_petit(suites);
				}
				carte_maitresse=true;
				for(byte joueur:joueurs_non_confiance_non_joue)
				{
					carte_maitresse&=!cartes_possibles.get(1).get(joueur).estVide();
				}
				if(carte_maitresse)
				{
					if(repartitionCartesJouees.get(1).derniereCarte().valeur()>1)
					{
						return atout_le_plus_petit(suites);
					}
					return atout_le_plus_petit(suites,contientExcuse);
				}
				if(!cartes_rel_maitres.isEmpty())
				{
					return cartes_rel_maitres.lastElement().carte(0);
				}
				return atout_le_plus_petit(suites,contientExcuse);
			}
			/*Le joueur se defausse sur demande d'atout*/
			Vector<Byte> couleurs=new Vector<Byte>();
			for(byte couleur=2;couleur<6;couleur++)
			{
				if(!repartition_couleurs.get(couleur).estVide()&&repartition_couleurs.get(couleur).carte(0).valeur()<11)
				{
					couleurs.addElement(couleur);
				}
			}
			if(!couleurs.isEmpty())
			{
				return jouer_petite_carte_defausse(suites_toute_couleur,couleurs,repartition_couleurs,repartitionCartesJouees);
			}
			for(byte couleur=2;couleur<6;couleur++)
			{
				if(!repartition_couleurs.get(couleur).estVide())
				{
					couleurs.addElement(couleur);
				}
			}
			return jeu_petite_carte_couleur_figure(suites_toute_couleur, couleurs, repartition_couleurs, repartitionCartesJouees);
		}
		/*Jeu non equipes*/
		byte ramasseur_certain=equipe_qui_va_faire_pli(cartes_possibles, cartes_certaines, ramasseurVirtuel, carte_forte, joueurs_non_joue,autres_joueurs(numero), numero);
		if(pas_jeu_misere())
		{
			if(ramasseur_certain==0)
			{
				if(maitre_jeu&&contientExcuse)
				{
					return new CarteTarot((byte)0);
				}
				if(main_joueur.total()==2&&contientExcuse)
				{
					raison[0]="Vous ne pourrez pas de toute maniere faire tous les plis.";
					return new CarteTarot((byte)0);
				}
				if(couleurDemandee>1)
				{
					if(cartes_jouables.carte(0).couleur()==couleurDemandee||cartes_jouables.derniereCarte().couleur()==couleurDemandee)
					{
						return carteLaPlusPetite(suites);
					}
					if(cartes_jouables.carte(0).couleur()!=1&&cartes_jouables.derniereCarte().couleur()!=1)
					{/*Si le joueur se defausse*/
						if(repartitionCartesJouees.get(1).total()>19&&repartitionCartesJouees.get(couleurDemandee).total()>12)
						{
							if(couleurs_strictes_maitresses.size()==3)
							{
								Vector<Byte> couleurs=new Vector<Byte>();
								for(byte couleur:couleurs_strictes_maitresses)
								{
									if(!repartition_couleurs.get(couleur).estVide())
									{
										couleurs.addElement(couleur);
									}
								}
								return jeu_petite_defausse_maitre(suites_toute_couleur, cartes_maitresses, repartition_couleurs, couleurs);
							}
							Vector<Byte> couleurs=new Vector<Byte>();
							for(byte couleur=2;couleur<6;couleur++)
							{
								if(!repartition_couleurs.get(couleur).estVide()&&repartition_couleurs.get(couleur).carte(0).valeur()<11)
								{
									couleurs.addElement(couleur);
								}
							}
							if(!couleurs.isEmpty())
							{
								return jouer_petite_carte_defausse(suites_toute_couleur,couleurs,repartition_couleurs,repartitionCartesJouees);
							}
							for(byte couleur=2;couleur<6;couleur++)
							{
								if(!repartition_couleurs.get(couleur).estVide())
								{
									couleurs.addElement(couleur);
								}
							}
							return jeu_petite_carte_couleur_figure(suites_toute_couleur, couleurs, repartition_couleurs, repartitionCartesJouees);
						}
						/*Moins de 20 atouts sont joues et moins de 13 cartes de la couleur demandee sont jouees*/
						Vector<Byte> couleurs=new Vector<Byte>();
						for(byte couleur=2;couleur<6;couleur++)
						{
							if(!repartition_couleurs.get(couleur).estVide()&&repartition_couleurs.get(couleur).carte(0).valeur()<11)
							{
								couleurs.addElement(couleur);
							}
						}
						if(!couleurs.isEmpty())
						{
							return jouer_petite_carte_defausse(suites_toute_couleur,couleurs,repartition_couleurs,repartitionCartesJouees);
						}
						for(byte couleur=2;couleur<6;couleur++)
						{
							if(!repartition_couleurs.get(couleur).estVide())
							{
								couleurs.addElement(couleur);
							}
						}
						return jeu_petite_carte_couleur_figure(suites_toute_couleur, couleurs, repartition_couleurs, repartitionCartesJouees);
					}
				}
				/*La couleur demandee est atout*/
				if(cartes_jouables.carte(0).couleur()==1||cartes_jouables.derniereCarte().couleur()==1)
				{
					repartition_coule_dem=repartition_jou.get(1);
					suites=repartition_coule_dem.eclater(repartitionCartesJouees);
					return atout_le_plus_petit(suites, contientExcuse);
				}
				/*Maintenant le joueur se defausse*/
				if(repartitionCartesJouees.get(1).total()>17)
				{
					if(couleurs_strictes_maitresses.size()==4)
					{
						Vector<Byte> couleurs=new Vector<Byte>();
						for(byte couleur:couleurs_strictes_maitresses)
						{
							if(!repartition_couleurs.get(couleur).estVide())
							{
								couleurs.addElement(couleur);
							}
						}
						return jeu_petite_defausse_maitre(suites_toute_couleur, cartes_maitresses, repartition_couleurs, couleurs);
					}
					Vector<Byte> couleurs=new Vector<Byte>();
					for(byte couleur=2;couleur<6;couleur++)
					{
						if(!repartition_couleurs.get(couleur).estVide()&&repartition_couleurs.get(couleur).carte(0).valeur()<11)
						{
							couleurs.addElement(couleur);
						}
					}
					if(!couleurs.isEmpty())
					{
						return jouer_petite_carte_defausse(suites_toute_couleur,couleurs,repartition_couleurs,repartitionCartesJouees);
					}
					for(byte couleur=2;couleur<6;couleur++)
					{
						if(!repartition_couleurs.get(couleur).estVide())
						{
							couleurs.addElement(couleur);
						}
					}
					return jeu_petite_carte_couleur_figure(suites_toute_couleur, couleurs, repartition_couleurs, repartitionCartesJouees);
				}
				/*Moins de 20 atouts sont joues et moins de 13 cartes de la couleur demandee sont jouees*/
				Vector<Byte> couleurs=new Vector<Byte>();
				for(byte couleur=2;couleur<6;couleur++)
				{
					if(!repartition_couleurs.get(couleur).estVide()&&repartition_couleurs.get(couleur).carte(0).valeur()<11)
					{
						couleurs.addElement(couleur);
					}
				}
				if(!couleurs.isEmpty())
				{
					return jouer_petite_carte_defausse(suites_toute_couleur,couleurs,repartition_couleurs,repartitionCartesJouees);
				}
				for(byte couleur=2;couleur<6;couleur++)
				{
					if(!repartition_couleurs.get(couleur).estVide())
					{
						couleurs.addElement(couleur);
					}
				}
				return jeu_petite_carte_couleur_figure(suites_toute_couleur, couleurs, repartition_couleurs, repartitionCartesJouees);
			}
			if(main_joueur.total()==2&&contientExcuse)
			{
				return new CarteTarot((byte)0);
			}
			Vector<Byte> joueurs_de_non_confiance=autres_joueurs(numero);
			Vector<Byte> joueurs_de_non_confiance_non_joue=new Vector<Byte>();
			for(byte joueur:joueurs_de_non_confiance)
			{
				if(joueurs_non_joue.contains(joueur))
				{
					joueurs_de_non_confiance_non_joue.addElement(joueur);
				}
			}
			/*Maintenant on ne peut pas dire qui va faire le pli*/
			if(couleurDemandee>1)
			{
				carte_haute_pas_atout=((MainTarot)pliEnCours.getCartes()).couleurs().get(couleurDemandee).carte(0);
				if(cartes_jouables.carte(0).couleur()==couleurDemandee||cartes_jouables.derniereCarte().couleur()==couleurDemandee)
				{//Si le joueur ne coupe pas et ne se defauuse pas sur la couleur demandee
					cartes_rel_maitres=cartesRelativementMaitreEncours(suites, cartes_possibles, joueurs_non_joue, couleurDemandee, couleurDemandee, cartes_certaines,carte_forte);
					if(repartition_coule_dem.carte(0).valeur()<carte_forte.valeur()||carte_forte.couleur()==1)
					{
						/*Si le joueur ne peut pas prendre la main*/
						if(repartition_coule_dem.carte(0).valeur()<11)
						{/*Si le joueur ne possede pas de figure*/
							if(contientExcuse&&maitre_jeu)
							{
								return new CarteTarot((byte)0);
							}
							return carteLaPlusPetite(suites);
						}
						if(maitre_jeu)
						{
							if(contientExcuse&&maitre_jeu)
							{
								return new CarteTarot((byte)0);
							}
							return carteLaPlusPetite(suites);
						}
						return carteLaPlusPetite(suites);
					}
					/*Maintenant on sait le joueur peut prendre la main*/
					if(repartition_coule_dem.carte(0).valeur()<11)
					{
						if(maitre_jeu)
						{
							return cartes_rel_maitres.lastElement().carte(0);
						}
						carte_maitresse=true;
						for(byte joueur:joueurs_de_non_confiance_non_joue)
						{
							if(!defausse(cartes_possibles, joueur, couleurDemandee)&&!ne_peut_pas_avoir_figures(cartes_possibles, joueur, couleurDemandee))
							{
								carte_maitresse=false;
								break;
							}
						}
						if(carte_maitresse)
						{
							if(!cartes_rel_maitres.isEmpty())
							{
								return cartes_rel_maitres.lastElement().carte(0);
							}
						}
						if(tours.isEmpty())
						{
							return repartition_coule_dem.carte(0);
						}
						return carteLaPlusPetite(suites);
					}
					/*Maintenant le joueur peut prendre la main avec une figure*/
					if(tours.isEmpty())
					{
						if(maitre_jeu)
						{
							return cartes_rel_maitres.lastElement().carte(0);
						}
						if(couleurDemandee!=couleurAppelee)
						{
							if(!cartes_maitresses.get(couleurDemandee-2).estVide())
							{
								if(suites.size()==1||suites.get(1).carte(0).valeur()<11)
								{
									return repartition_coule_dem.carte(0);
								}
								carte_maitresse=true;
								for(byte joueur:joueurs_de_non_confiance_non_joue)
								{
									if(!defausse(cartes_possibles, joueur, couleurDemandee)&&!ne_peut_pas_avoir_figures(cartes_possibles, joueur, couleurDemandee))
									{
										carte_maitresse=false;
										break;
									}
								}
								if(carte_maitresse&&carte_forte.valeur()<11)
								{
									return jeuFigureHauteDePlusFaibleSuite(suites);
								}
								return repartition_coule_dem.carte(0);
							}
							/*Le joueur n'a aucune cartes maitresses*/
							carte_maitresse=true;
							for(byte joueur:joueurs_de_non_confiance_non_joue)
							{
								if(!defausse(cartes_possibles, joueur, couleurDemandee)&&!ne_peut_pas_avoir_figures(cartes_possibles, joueur, couleurDemandee))
								{
									carte_maitresse=false;
									break;
								}
							}
							if(carte_maitresse)
							{
								return repartition_coule_dem.carte(0);
							}
							return carteLaPlusPetite(suites);
						}
						/*La couleur demandee est la couleur appelee*/
						if(cartes_maitresses.get(couleurDemandee-2).estVide())
						{
							return jeuFigureHauteDePlusFaibleSuite(suites);
						}
						return repartition_coule_dem.carte(0);
					}
					dernier_pli=plisFaits.get(tours.lastElement());
					dernieres_coupes=dernier_pli.joueurs_coupes(nombre_joueurs, pli_le_plus_petit,couleur_atout);
					dernieres_defausses=dernier_pli.joueurs_defausses(nombre_joueurs, pli_le_plus_petit,couleur_atout);
					/*C'est au moins le deuxieme tour*/
					for(byte joueur:joueurs_non_joue)
					{
						if(peut_couper(couleurDemandee, joueur, cartes_possibles))
						{
							joueurs_susceptibles_de_couper.addElement(joueur);
						}
					}
					if(!joueurs_susceptibles_de_couper.isEmpty())
					{
						carte_maitresse=false;
						for(byte joueur:joueurs_de_non_confiance)
						{
							carte_maitresse|=joueurs_susceptibles_de_couper.contains(joueur);
						}
						if(carte_maitresse)
						{
							if(maitre_jeu&&contientExcuse)
							{
								return new CarteTarot((byte)0);
							}
							return carteLaPlusPetite(suites);
						}
						return carteLaPlusPetite(suites);
					}
					/*Si la coupe semble improbable*/
					if(!dernieres_defausses.isEmpty()&&tours.size()==1)
					{
						if(maitre_jeu)
						{
							return cartes_rel_maitres.lastElement().carte(0);
						}
						if(couleurDemandee!=couleurAppelee)
						{
							if(!cartes_maitresses.get(couleurDemandee-2).estVide())
							{
								if(suites.size()==1||suites.get(1).carte(0).valeur()<11)
								{
									return repartition_coule_dem.carte(0);
								}
								carte_maitresse=true;
								for(byte joueur:joueurs_de_non_confiance_non_joue)
								{
									if(!defausse(cartes_possibles, joueur, couleurDemandee)&&!ne_peut_pas_avoir_figures(cartes_possibles, joueur, couleurDemandee))
									{
										carte_maitresse=false;
										break;
									}
								}
								if(carte_maitresse&&carte_forte.valeur()<11)
								{
									return jeuFigureHauteDePlusFaibleSuite(suites);
								}
								return repartition_coule_dem.carte(0);
							}
							/*Le joueur n'a aucune cartes maitresses*/
							carte_maitresse=true;
							for(byte joueur:joueurs_de_non_confiance_non_joue)
							{
								if(!defausse(cartes_possibles, joueur, couleurDemandee)&&!ne_peut_pas_avoir_figures(cartes_possibles, joueur, couleurDemandee))
								{
									carte_maitresse=false;
									break;
								}
							}
							if(carte_maitresse)
							{
								return repartition_coule_dem.carte(0);
							}
							return carteLaPlusPetite(suites);
						}
						/*La couleur demandee est la couleur appelee*/
						if(cartes_maitresses.get(couleurDemandee-2).estVide())
						{
							return jeuFigureHauteDePlusFaibleSuite(suites);
						}
						return repartition_coule_dem.carte(0);
					}
					/*Le pli d'avant n'est pas defausse ou c'est au moins le troisieme tour*/
					if(!cartes_maitresses.get(couleurDemandee-2).estVide())
					{
						return repartition_coule_dem.carte(0);
					}
					if(!cartes_rel_maitres.isEmpty())
					{
						return cartes_rel_maitres.lastElement().carte(0);
					}
					return carteLaPlusPetite(suites);
				}
				if(cartes_jouables.carte(0).couleur()==1||cartes_jouables.derniereCarte().couleur()==1)
				{//Si le joueur coupe la couleur demandee maintenant
					suites=repartition_jou.get(1).eclater(repartitionCartesJouees);
					cartes_rel_maitres=cartesRelativementMaitreEncours(suites, cartes_possibles, joueurs_non_joue, (byte) 1, couleurDemandee, cartes_certaines,carte_forte);
					repartition_coule_dem=repartition_jou.get(1);
					if(repartition_coule_dem.carte(0).valeur()<carte_forte.valeur()&&carte_forte.valeur()==1)
					{/*Si le joueur ne peut pas surcouper*/
						return atout_le_plus_petit(suites,contientExcuse);
					}
					if(tours.isEmpty())
					{
						/*Le joueur peut surcouper si le pli est deja coupe ou couper avec n'importe quel atout*/
						if(repartition_jou.get(1).derniereCarte().valeur()>1)
						{/*Si le joueur ne peut pas couper avec le Petit*/
							if(!contientExcuse)
							{
								if(maitre_jeu)
								{
									return cartes_rel_maitres.lastElement().carte(0);
								}
								return atout_le_plus_petit(suites,contientExcuse);
							}
							/*Maintenant le joueur possede l'Excuse*/
							if(carte_haute_pas_atout.valeur()>10)
							{/*S'il existe une figure de la couleur demandee*/
								return atout_le_plus_petit(suites);
							}
							if(carte_forte.couleur()>1)
							{/*Si le joueur ne surcoupe pas un autre joueur alors il n'a pas le Petit par hypothese par (m.derniereCarte().getValeur()>1)*/
								if(repartitionCartesJouees.get(couleurDemandee).nombreFiguresEgal(4))
								{
									return new CarteTarot((byte)0);
								}
							}
							return atout_le_plus_petit(suites);
						}
						/*Le joueur peut couper avec le Petit*/
						if(maitre_jeu)
						{
							if(contientExcuse)
							{
								return new CarteTarot((byte)0);
							}
							if(cartes_rel_maitres.lastElement().derniereCarte().valeur()>1||cartes_rel_maitres.lastElement().total()>1)
							{
								return cartes_rel_maitres.lastElement().carte(0);
							}
							return cartes_rel_maitres.get(cartes_rel_maitres.size()-2).carte(0);
						}
						carte_maitresse=true;
						for(byte joueur:joueurs_de_non_confiance)
						{
							carte_maitresse&=cartes_possibles.get(1).get(joueur).estVide();
						}
						if(carte_maitresse)
						{
							raison[0]="Ne pleurez pas, vous allez pouvoir mener votre Petit au bout";
							return atout_le_plus_petit(suites);
						}
						if(coupes_franches.size()==1)
						{
							if(repartition_coule_dem.total()+nombre_joueurs<=13)
							{
								return new CarteTarot((byte)1,(byte)1);
							}
							carte_maitresse=true;
							for(byte joueur:joueurs_de_non_confiance_non_joue)
							{
								carte_maitresse&=ne_peut_couper(couleurDemandee, joueur, cartes_possibles, cartes_certaines);
							}
							if(carte_maitresse)
							{
								raison[0]="Ne pleurez pas, votre Petit est en securite en le jouant maintenant";
								return new CarteTarot((byte)1,(byte)1);
							}
							return atout_le_plus_petit(suites);
						}
						carte_maitresse=true;
						/*Il existe au moins deux coupes franches*/
						for(byte coupe:coupes_franches)
						{
							if(coupe!=couleurDemandee)
							{
								carte_maitresse&=!tours(coupe, plisFaits).isEmpty();
							}
						}
						if(carte_maitresse)
						{
							return new CarteTarot((byte)1,(byte)1);
						}
						if(contrat.force()>2)
						{
							carte_maitresse=false;
							for(byte joueur:joueurs_de_non_confiance_non_joue)
							{
								carte_maitresse|=!cartes_possibles.get(1).get(joueur).estVide();
							}
							if(carte_maitresse)
							{
								if(repartition_coule_dem.total()>1||repartition_coule_dem.carte(0).valeur()>1)
								{
									return atout_le_plus_petit(suites);
								}
								return new CarteTarot((byte)1,(byte)1);
							}
						}
						carte_maitresse=true;
						for(byte joueur:joueurs_de_non_confiance_non_joue)
						{
							carte_maitresse&=ne_peut_couper(couleurDemandee, joueur, cartes_possibles, cartes_certaines);
						}
						if(carte_maitresse)
						{
							return new CarteTarot((byte)1,(byte)1);
						}
						if(repartition_coule_dem.total()+nombre_joueurs<=15)
						{
							return new CarteTarot((byte)1,(byte)1);
						}
						if(repartition_coule_dem.total()>1||repartition_coule_dem.carte(0).valeur()>1)
						{
							return atout_le_plus_petit(suites);
						}
						return new CarteTarot((byte)1,(byte)1);
					}
					/*Deuxieme tour et plus*/
					if(repartition_jou.get(1).derniereCarte().valeur()>1)
					{
						if(maitre_jeu)
						{
							return cartes_rel_maitres.lastElement().carte(0);
						}
						carte_maitresse=false;
						for (byte joueur:joueurs_de_non_confiance_non_joue)
						{
							carte_maitresse|=peut_couper(couleurDemandee, joueur, cartes_possibles);
						}
						if(carte_maitresse)
						{
							nombre_points=0;
							for(Carte carte:pliEnCours)
							{
								CarteTarot carte_jouee=(CarteTarot)carte;
								if(carte_jouee.couleur()>0)
								{
									nombre_points+=carte_jouee.points();
								}
							}
							if(nombre_points>7)
							{
								if(!cartes_rel_maitres.isEmpty())
								{
									raison[0]="Capturer des figures ou le Petit est tres interessant";
									return cartes_rel_maitres.lastElement().carte(0);
								}
								carte_maitresse=false;
								for(byte joueur:joueurs_non_joue)
								{
									carte_maitresse|=va_surcouper(cartes_possibles, cartes_certaines, numero, joueur, couleurDemandee);
									if(carte_maitresse)
									{
										return atout_le_plus_petit(suites,contientExcuse);
									}
								}
								carte_maitresse=false;
								for(byte joueur:joueurs_non_joue)
								{
									boolean local=true;
									for(byte joueur2:joueurs_joue)
									{
										local&=peut_surcouper(cartes_possibles,joueur2, joueur, couleurDemandee);
									}
									carte_maitresse|=local;
									if(carte_maitresse)
									{
										return atout_le_plus_petit(suites,contientExcuse);
									}
								}
								return repartition_jou.get(1).carte(0);
							}
							/*Moins de 8 points (les points sont doubles pour eviter les 1/2 points)*/
							return suites_toute_couleur.get(1).get(0).carte(0);
						}
						/*Il n'est pas probable qu'un joueur de non confiance n'ayant pas joue coupe le pli*/
						if(carte_forte.couleur()==1)
						{/*Si le pli est deja coupe*/
							return atout_le_plus_petit(suites);
						}
						if(carte_haute_pas_atout.valeur()<11)
						{
							carte_maitresse=true;
							for(byte joueur:joueurs_de_non_confiance_non_joue)
							{
								carte_maitresse&=cartes_possibles.get(couleurDemandee).get(joueur).estVide()||cartes_possibles.get(couleurDemandee).get(joueur).carte(0).valeur()<11;
							}
							if(carte_maitresse)
							{
								return atout_le_plus_petit(suites,contientExcuse);
							}
						}
						return atout_le_plus_petit(suites);
					}
					/*Le joueur possede le Petit et c'est le duxieme tour a cette couleur ou plus*/
					carte_maitresse=true;
					for(byte joueur:joueurs_de_non_confiance)
					{
						carte_maitresse&=cartes_possibles.get(1).get(joueur).estVide();
					}
					if(carte_maitresse)
					{
						raison[0]="Ne pleurez pas, vous allez pouvoir mener votre Petit au bout";
						return atout_le_plus_petit(suites);
					}
					if(maitre_jeu)
					{
						return cartes_rel_maitres.lastElement().carte(0);
					}
					carte_maitresse=true;
					for(byte joueur:joueurs_de_non_confiance_non_joue)
					{
						carte_maitresse&=!peut_couper(couleurDemandee,joueur,cartes_possibles);
					}
					if(carte_maitresse)
					{
						raison[0]="Ne pleurez pas, aucun joueur de non confiance ne peut surcouper votre Petit";
						return new CarteTarot((byte)1,(byte)1);
					}
					if(nombre_joueurs<5)
					{
						if(tours.size()==1)
						{
							if(repartition_coule_dem.total()>1||repartition_coule_dem.carte(0).valeur()>1)
							{
								return atout_le_plus_petit(suites);
							}
							Vector<Byte> joueurs_coupe_pre_tour=plisFaits.get(tours.get(0)).joueurs_coupes(nombre_joueurs,pli_le_plus_petit,couleur_atout);
							carte_maitresse=true;
							for(byte joueur:joueurs_de_non_confiance)
							{
								carte_maitresse&=!joueurs_coupe_pre_tour.contains(joueur);
							}
							if(carte_maitresse)
							{
								raison[0]="Seuls des joueurs de confiance ont eventuellement coupe au premier tour de cette couleur,\nvous avez de grandes chances de sauver votre Petit maintemant";
								return new CarteTarot((byte)1,(byte)1);
							}
						}
					}
					/*Le jeu s'effectue maintenant a 5 joueurs*/
					if(repartition_coule_dem.total()>1||repartition_coule_dem.carte(0).valeur()>1)
					{
						return atout_le_plus_petit(suites);
					}
					return new CarteTarot((byte)1,(byte)1);
				}
				/*Le joueur se defausse sur la couleur demandee*/
				/*Au dexieme tour et au de la il est preferable de jouer une petite carte*/
				Vector<Byte> couleurs=new Vector<Byte>();
				for(byte couleur=2;couleur<6;couleur++)
				{
					if(!repartition_couleurs.get(couleur).estVide()&&repartition_couleurs.get(couleur).carte(0).valeur()<11)
					{
						couleurs.addElement(couleur);
					}
				}
				if(!couleurs.isEmpty())
				{
					return jouer_petite_carte_defausse(suites_toute_couleur,couleurs,repartition_couleurs,repartitionCartesJouees);
				}
				for(byte couleur=2;couleur<6;couleur++)
				{
					if(!repartition_couleurs.get(couleur).estVide())
					{
						couleurs.addElement(couleur);
					}
				}
				return jeu_petite_carte_couleur_figure(suites_toute_couleur, couleurs, repartition_couleurs, repartitionCartesJouees);
			}
			/*Demande atout*/
			if(cartes_jouables.carte(0).couleur()==1||cartes_jouables.derniereCarte().couleur()==1)
			{
				suites=repartition_jou.get(1).eclater(repartitionCartesJouees);
				cartes_rel_maitres=cartesRelativementMaitreEncours(suites, cartes_possibles, joueurs_non_joue, (byte) 1, couleurDemandee, cartes_certaines,carte_forte);
				repartition_coule_dem=repartition_jou.get(1);
				if(repartition_coule_dem.carte(0).valeur()<carte_forte.valeur())
				{
					return atout_le_plus_petit(suites,contientExcuse);
				}
				if(pliEnCours.carte(0).valeur()==1)
				{
					if(!cartes_rel_maitres.isEmpty())
					{
						return cartes_rel_maitres.lastElement().carte(0);
					}
					if(peut_ramasser_atout(cartes_possibles, cartes_certaines, numero, joueurs_non_joue))
					{
						return repartition_coule_dem.carte(0);
					}
					return suites.lastElement().carte(0);
				}
				if(maitre_jeu)
				{
					return cartes_rel_maitres.lastElement().carte(0);
				}
				carte_maitresse=true;
				for(byte joueur:joueurs_de_non_confiance_non_joue)
				{
					carte_maitresse&=cartes_possibles.get(1).get(joueur).estVide()||cartes_possibles.get(1).get(joueur).carte(0).valeur()<carte_forte.valeur();
				}
				nombre_points=0;
				for(Carte carte:pliEnCours)
				{
					CarteTarot carte_jouee=(CarteTarot)carte;
					if(carte_jouee.couleur()>0)
					{
						nombre_points+=carte_jouee.points();
					}
				}
				if(nombre_points>6)
				{
					if(!cartes_rel_maitres.isEmpty())
					{
						return cartes_rel_maitres.lastElement().carte(0);
					}
					return atout_le_plus_petit(suites);
				}
				carte_maitresse=true;
				for(byte joueur:joueurs_de_non_confiance_non_joue)
				{
					carte_maitresse&=cartes_possibles.get(1).get(joueur).estVide()||cartes_possibles.get(1).get(joueur).carte(0).valeur()<carte_forte.valeur();
				}
				if(carte_maitresse)
				{
					return atout_le_plus_petit(suites);
				}
				carte_maitresse=true;
				for(byte joueur:joueurs_de_non_confiance_non_joue)
				{
					carte_maitresse&=!cartes_possibles.get(1).get(joueur).estVide();
				}
				if(carte_maitresse)
				{
					if(repartitionCartesJouees.get(1).derniereCarte().valeur()>1)
					{
						return atout_le_plus_petit(suites);
					}
					return atout_le_plus_petit(suites,contientExcuse);
				}
				if(!cartes_rel_maitres.isEmpty())
				{
					return cartes_rel_maitres.lastElement().carte(0);
				}
				return atout_le_plus_petit(suites,contientExcuse);
			}
			/*Le joueur se defausse sur demande d'atout*/
			Vector<Byte> couleurs=new Vector<Byte>();
			for(byte couleur=2;couleur<6;couleur++)
			{
				if(!repartition_couleurs.get(couleur).estVide()&&repartition_couleurs.get(couleur).carte(0).valeur()<11)
				{
					couleurs.addElement(couleur);
				}
			}
			if(!couleurs.isEmpty())
			{
				return jouer_petite_carte_defausse(suites_toute_couleur,couleurs,repartition_couleurs,repartitionCartesJouees);
			}
			for(byte couleur=2;couleur<6;couleur++)
			{
				if(!repartition_couleurs.get(couleur).estVide())
				{
					couleurs.addElement(couleur);
				}
			}
			return jeu_petite_carte_couleur_figure(suites_toute_couleur, couleurs, repartition_couleurs, repartitionCartesJouees);
		}
		/*Jeu de misere*/
		if(ramasseur_certain==0)
		{
			if(couleurDemandee>1)
			{
				if(cartes_jouables.carte(0).couleur()==couleurDemandee||cartes_jouables.derniereCarte().couleur()==couleurDemandee)
				{
					return repartition_coule_dem.carte(0);
				}
			}
			if(!repartition_jou.get(1).estVide())
			{
				if(repartition_jou.get(1).derniereCarte().valeur()==1)
				{
					return new CarteTarot((byte)1,(byte)1);
				}
				return repartition_jou.get(1).carte(0);
			}
			Vector<Byte> couleurs=new Vector<Byte>();
			Vector<Byte> couleurs_non_entamees=new Vector<Byte>();
			boolean cont_figure_tt_clr=true;
			for(byte couleur=2;couleur<6;couleur++)
			{
				if(!repartition_jou.get(couleur).estVide())
				{
					couleurs.addElement(couleur);
					cont_figure_tt_clr&=repartition_couleurs.get(couleur).carte(0).valeur()>10;
				}
			}
			if(cont_figure_tt_clr&&!couleurs.isEmpty())
			{
				return depouille_figure_en_cours(repartition_jou, couleurs, repartitionCartesJouees);
			}
			cont_figure_tt_clr=true;
			couleurs=new Vector<Byte>();
			for(byte couleur=2;couleur<6;couleur++)
			{
				cont_figure_tt_clr&=!repartition_couleurs.get(couleur).estVide()==repartitionCartesJouees.get(couleur).total()<14;
				if(cont_figure_tt_clr)
				{
					couleurs.addElement(couleur);
				}
			}
			if(cont_figure_tt_clr)
			{
				for(byte couleur:couleurs)
				{
					if(tours(couleur, plisFaits).isEmpty()&&repartition_couleurs.get(couleur).total()==1)
					{
						couleurs_non_entamees.addElement(couleur);
					}
				}
				if(!couleurs_non_entamees.isEmpty())
				{
					return singleton_fort_depouille(repartition_couleurs, couleurs_non_entamees);
				}
				for(byte couleur:couleurs)
				{
					if(repartition_couleurs.get(couleur).total()==1)
					{
						couleurs_non_entamees.addElement(couleur);
					}
				}
				if(!couleurs_non_entamees.isEmpty())
				{
					return singleton_fort_depouille(repartition_couleurs, couleurs_non_entamees);
				}
			}
			couleurs=new Vector<Byte>();
			for(byte couleur=2;couleur<6;couleur++)
			{
				if(!repartition_couleurs.get(couleur).estVide()&&repartition_couleurs.get(couleur).carte(0).valeur()>10)
				{
					couleurs.addElement(couleur);
				}
			}
			if(!couleurs.isEmpty())
			{
				return depouille_figure_defausse(repartition_couleurs, couleurs, repartitionCartesJouees);
			}
			for(byte couleur=2;couleur<6;couleur++)
			{
				if(!repartition_couleurs.get(couleur).estVide())
				{
					couleurs.addElement(couleur);
				}
			}
			return en_cours_misere_petite(couleurs, repartition_couleurs, repartitionCartesJouees);
		}
		if(couleurDemandee>1)
		{
			if(cartes_jouables.carte(0).couleur()==couleurDemandee||cartes_jouables.derniereCarte().couleur()==couleurDemandee)
			{
				if(repartition_coule_dem.derniereCarte().valeur()<carte_forte.valeur())
				{
					return carte_inf_virt(suites,carte_forte);
				}
				if(repartition_coule_dem.derniereCarte().valeur()<11)
				{
					return carte_inf_figure(suites);
				}
				return suites.lastElement().derniereCarte();
			}
			boolean surcoupe_sure=false;
			for(byte joueur:joueurs_non_joue)
			{
				surcoupe_sure|=va_couper(couleurDemandee,joueur,cartes_possibles,cartes_certaines);
			}
			if(surcoupe_sure&&repartition_couleurs.get(1).derniereCarte().valeur()==1)
			{
				return new CarteTarot((byte)1,(byte)1);
			}
			if(tours.isEmpty())
			{
				return repartition_couleurs.get(1).carte(0);
			}
			surcoupe_sure=false;
			byte valeur=0;
			for(byte joueur:joueurs_non_joue)
			{
				surcoupe_sure|=va_couper(couleurDemandee,joueur,cartes_possibles,cartes_certaines);
				if(surcoupe_sure&&cartes_certaines.get(1).get(joueur).carte(0).valeur()>valeur)
				{
					valeur=cartes_certaines.get(1).get(joueur).carte(0).valeur();
				}
			}
			if(surcoupe_sure)
			{
				if(repartition_couleurs.get(1).derniereCarte().valeur()<valeur)
				{
					byte carte_a_jouer=-1;
					for(byte indice_carte=(byte)(repartition_couleurs.get(1).total()-1);indice_carte>-1;indice_carte--)
					{
						if(repartition_couleurs.get(1).carte(indice_carte).valeur()<valeur)
						{
							carte_a_jouer++;
						}
						else
						{
							break;
						}
					}
					return repartition_couleurs.get(1).carte(carte_a_jouer);
				}
				return repartition_couleurs.get(1).derniereCarte();
			}
			return repartition_couleurs.get(1).carte(0);
		}
		return repartition_couleurs.get(1).carte(0);
	}
	public Pli pliLePlusPetit(Vector<Pli> plis_faits)
	{
		Pli pli_le_plus_petit=plis_faits.get(0);
		for(byte indice_pli=1;indice_pli<plis_faits.size();indice_pli++)
		{
			if(plis_faits.get(indice_pli).total()<pli_le_plus_petit.total())
			{
				pli_le_plus_petit=plis_faits.get(indice_pli);
			}
		}
		return pli_le_plus_petit;
	}
	public byte ramasseur(Vector<Pli> plis_faits,byte numero_pli)
	{
		if(numero_pli<plis_faits.size()-1)
		{
			if(plis_faits.get(numero_pli+1).total()>getNombreDeJoueurs()-2)
			{
				return plis_faits.get(numero_pli+1).getEntameur();
			}
			return plis_faits.get(numero_pli+2).getEntameur();
		}
		return ramasseur;
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
	private static Vector<MainTarot> cartesRelativementMaitreEncours(Vector<MainTarot> suites,Vector<Vector<MainTarot>> cartes_possibles,Vector<Byte> joueurs_n_ayant_pas_joue,byte couleur_joueur, byte couleur_demandee, Vector<Vector<MainTarot>> cartes_certaines,CarteTarot carte_forte)
	{
		byte max_valeur=0;
		Vector<MainTarot> suites_bis=new Vector<MainTarot>();
		if(couleur_joueur<2&&couleur_demandee>1)
		{/*Pour une coupe*/
			for(byte joueur:joueurs_n_ayant_pas_joue)
			{
				if(!cartes_possibles.get(couleur_joueur).get(joueur).estVide()&&cartes_certaines.get(couleur_demandee).get(joueur).estVide())
				{
					if(cartes_possibles.get(couleur_joueur).get(joueur).carte(0).valeur()>max_valeur)
					{
						max_valeur=cartes_possibles.get(couleur_joueur).get(joueur).carte(0).valeur();
					}
				}
			}
			if(carte_forte.couleur()==1)
			{
				max_valeur=(byte)Math.max(max_valeur,carte_forte.valeur());
			}
		}
		else
		{/*Pour fournir*//*demande atout*/
			for(byte joueur:joueurs_n_ayant_pas_joue)
			{
				if(!cartes_possibles.get(couleur_joueur).get(joueur).estVide())
				{
					if(cartes_possibles.get(couleur_joueur).get(joueur).carte(0).valeur()>max_valeur)
					{
						max_valeur=cartes_possibles.get(couleur_joueur).get(joueur).carte(0).valeur();
					}
				}
			}
			max_valeur=(byte)Math.max(max_valeur,carte_forte.valeur());
		}
		for(MainTarot suite:suites)
		{
			if(suite.carte(0).valeur()>max_valeur)
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
	/**Est vrai si et seulement si le partenaire du joueur qui va jouer domine l'adversaire n'ayant pas joue (uniquement si le partenaire est maitre temporairement du pli)*/
	private boolean jc_bat_jncnj_non_joue(Vector<Byte> joueurs_non_confiance_non_joue,byte couleur_demandee,Vector<Vector<MainTarot>> cartes_possibles,CarteTarot carte_forte)
	{
		boolean bat=true;
		for(byte joueur:joueurs_non_confiance_non_joue)
		{
			bat&=cartes_possibles.get(couleur_demandee).get(joueur).estVide()||carte_forte.valeur()>cartes_possibles.get(couleur_demandee).get(joueur).carte(0).valeur();
		}
		return bat;
	}
	/**Couleur demand&eacute;e atout: retourne vrai si et seulement si le joueur numero peut ramasser le pli en jouant son plus grand atout*/
	private boolean peut_ramasser_atout(Vector<Vector<MainTarot>> cartes_possibles,Vector<Vector<MainTarot>> cartes_certaines,byte numero,Vector<Byte> joueurs_non_joue)
	{
		boolean existe=false;
		Vector<Byte> joueurs_joue=new Vector<Byte>();
		for(byte joueur=0;joueur<getNombreDeJoueurs();joueur++)
		{
			if(!joueurs_non_joue.contains(joueur))
			{
				joueurs_joue.addElement(joueur);
			}
		}
		for(byte joueur:joueurs_non_joue)
		{
			existe|=!cartes_certaines.get(1).get(joueur).estVide()&&cartes_certaines.get(1).get(joueur).carte(0).valeur()>cartes_certaines.get(1).get(numero).carte(0).valeur();
		}
		if(existe)
		{
			return false;
		}
		for(byte joueur:joueurs_non_joue)
		{
			boolean local=true;
			for(byte joueur2:joueurs_joue)
			{
				local&=!cartes_possibles.get(1).get(joueur).estVide()&&cartes_possibles.get(1).get(joueur).carte(0).valeur()>cartes_possibles.get(1).get(joueur2).carte(0).valeur();
			}
			existe|=local;
		}
		return !existe;
	}
	private static boolean va_surcouper(Vector<Vector<MainTarot>> cartes_possibles,Vector<Vector<MainTarot>> cartes_certaines,byte numero,byte numero_bis,byte couleur_demandee)
	{
		return cartes_possibles.get(couleur_demandee).get(numero_bis).estVide()&&!cartes_certaines.get(1).get(numero_bis).estVide()&&cartes_certaines.get(1).get(numero_bis).carte(0).valeur()>cartes_certaines.get(1).get(numero).carte(0).valeur();
	}
	private static boolean peut_surcouper(Vector<Vector<MainTarot>> cartes_possibles,byte numero,byte numero_bis,byte couleur_demandee)
	{
		return cartes_possibles.get(couleur_demandee).get(numero_bis).estVide()&&!cartes_possibles.get(1).get(numero_bis).estVide()&&cartes_possibles.get(1).get(numero_bis).carte(0).valeur()>cartes_possibles.get(1).get(numero).carte(0).valeur();
	}
	private static Vector<Byte> coupes_franches_strictes(Vector<Pli> plisFaits,Vector<MainTarot> repartition_couleur,byte nombre_joueurs,byte numero,Pli pliLePlusPetitEnTaille)
	{
		Vector<Byte> coupes=new Vector<Byte>();
		for(byte couleur=2;couleur<6;couleur++)
		{
			if(repartition_couleur.get(couleur).estVide())
			{
				coupes.addElement(couleur);
			}
		}
		boolean coupe_toujours=true;
		byte couleur_donne;
		for(byte indice_couleur=0;indice_couleur<coupes.size();)
		{
			coupe_toujours=true;
			couleur_donne=coupes.get(indice_couleur);
			for(Pli pli:plisFaits)
			{
				if(pli.total()==nombre_joueurs||pli.total()==nombre_joueurs-1)
				{
					if(pli.couleurDemandee()==couleur_donne)
					{
						coupe_toujours&=pli.carteDuJoueur(numero, nombre_joueurs, pliLePlusPetitEnTaille).couleur()==1;
					}
				}
			}
			if(!coupe_toujours)
			{
				coupes.removeElementAt(indice_couleur);
			}
			else
			{
				indice_couleur++;
			}
		}
		return coupes;
	}
	/**Renvoie la carte la plus forte de la couleur ma&icirc;tresse sans reprise avec le plus de cartes ma&icirc;tresses, la plus jou&eacute;e (en nombre de cartes), la plus longue*/
	private CarteTarot carteDansCouleurCaMJouLg(Vector<Vector<MainTarot>> suites,Vector<MainTarot> repartition,Vector<Byte> couleurs,Vector<MainTarot> cartesJouees, Vector<Vector<MainTarot>> cartes_possibles, byte numero)
	{
		Vector<Byte> couleurs_maitres=couleurs_maitres_sans_reprise(suites, couleurs, repartition, cartes_possibles, cartesJouees, numero);
		int indice_couleur_1,indice_couleur_2;
		for(int indice=1;indice<couleurs.size();indice++)
		{
			byte couleur1=couleurs.get(0);
			byte couleur2=couleurs.get(indice);
			indice_couleur_1=couleurs_maitres.indexOf(couleur1);
			indice_couleur_2=couleurs_maitres.indexOf(couleur2);
			if(indice_couleur_1<0&&indice_couleur_2>-1)
			{
				couleurs.setElementAt(couleurs.set(0,couleur2),indice);
			}
			else if(indice_couleur_2>-1)
			{
				if(indice_couleur_1>indice_couleur_2)
				{
					couleurs.setElementAt(couleurs.set(0,couleur2),indice);
				}
			}
			else if(suites.get(couleur1).get(0).total()<suites.get(couleur2).get(0).total())
			{
				couleurs.setElementAt(couleurs.set(0,couleur2),indice);
			}
			else if(suites.get(couleur1).get(0).total()==suites.get(couleur2).get(0).total())
			{
				if(cartesJouees.get(couleur1).total()<cartesJouees.get(couleur2).total())
				{
					couleurs.setElementAt(couleurs.set(0,couleur2),indice);
				}
				else if(cartesJouees.get(couleur1).total()==cartesJouees.get(couleur2).total())
				{
					if(repartition.get(couleur1).total()<repartition.get(couleur2).total())
					{
						couleurs.setElementAt(couleurs.set(0,couleur2),indice);
					}
				}
			}
		}
		return(CarteTarot)suites.get(couleurs.get(0)).get(0).carte(0);
	}
	/**Renvoie l&#39;ensemble des couleurs ma&icirc;tresses telles que le maximum possible de cartes
	possed&eacute;es par les autres joueurs soit plus petit que le nombre de cartes ma&icirc;tresses du joueur num&eacute;ro
	Les couleurs sont tri&eacute;es par ordre d&eacute;croissant du nombre total de cartes dans une couleur poss&eacute;d&eacute;es par le joueur
	puis par ordre d&eacute;croissant du nombre total de cartes jou&eacute;es dans une couleur dans les plis*/
	private Vector<Byte> couleurs_maitres_sans_reprise(Vector<Vector<MainTarot>> suites,Vector<Byte> couleurs,Vector<MainTarot> repartition,Vector<Vector<MainTarot>> cartes_possibles,Vector<MainTarot> cartesJouees,byte numero)
	{
		Vector<Byte> couleurs_maitres=new Vector<Byte>();
		int max=0;
		for(byte couleur:couleurs)
		{
			for(byte joueur=0;joueur<getNombreDeJoueurs();joueur++)
			{
				if(joueur!=numero)
				{
					if(cartes_possibles.get(couleur).get(joueur).total()>max)
					{
						max=cartes_possibles.get(couleur).get(joueur).total();
					}
				}
			}
			if(suites.get(couleur).get(0).total()>=max)
			{
				couleurs_maitres.addElement(couleur);
			}
		}
		for(int indice=0;indice<couleurs_maitres.size();indice++)
		{
			for(int indice_2=indice+1;indice_2<couleurs_maitres.size();indice_2++)
			{
				byte couleur1=couleurs_maitres.get(indice);
				byte couleur2=couleurs_maitres.get(indice_2);
				if(repartition.get(couleur1).total()<repartition.get(couleur2).total())
				{
					couleurs_maitres.setElementAt(couleurs_maitres.set(indice,couleur2),indice_2);
				}
				else if(repartition.get(couleur1).total()==repartition.get(couleur2).total())
				{
					if(cartesJouees.get(couleur1).total()<cartesJouees.get(couleur2).total())
					{
						couleurs_maitres.setElementAt(couleurs_maitres.set(indice,couleur2),indice_2);
					}
				}
			}
		}
		return couleurs_maitres;
	}
	private Carte sauve_qui_peut_figure(Vector<Vector<MainTarot>> cartes_possibles,Vector<MainTarot> suites,Vector<MainTarot> cartes_rel_maitres,Vector<Byte> joueurs_de_non_confiance_non_joue,byte couleur_demandee)
	{
		if(joueurs_de_non_confiance_non_joue.isEmpty())
			return jeuFigureHauteDePlusFaibleSuite(suites);
		boolean pas_atout=true;
		for(byte joueur:joueurs_de_non_confiance_non_joue)
		{
			if(!cartes_possibles.get(couleur_demandee).get(joueur).estVide())
			{
				pas_atout&=cartes_possibles.get(couleur_demandee).get(joueur).carte(0).valeur()<11;
			}
			pas_atout&=cartes_possibles.get(1).get(joueur).estVide();
		}
		if(pas_atout)
		{
			return jeuFigureHauteDePlusFaibleSuite(suites);
		}
		if(!cartes_rel_maitres.isEmpty())
		{
			if(cartes_rel_maitres.size()==1||cartes_rel_maitres.get(1).carte(0).valeur()<11)
				return suites.get(0).carte(0);
			return cartes_rel_maitres.get(1).carte(0);
		}
		return carteLaPlusPetite(suites);
	}
	private boolean pas_atout(Vector<Byte> joueurs_de_non_confiance_non_joue,Vector<Vector<MainTarot>> cartes_possibles)
	{
		boolean pas_atout=true;
		for(byte joueur:joueurs_de_non_confiance_non_joue)
		{
			pas_atout&=cartes_possibles.get(1).get(joueur).estVide();
		}
		return pas_atout;
	}
	private boolean defausse(Vector<Vector<MainTarot>> cartes_possibles,byte numero,byte couleur)
	{
		return cartes_possibles.get(1).get(numero).estVide()&&cartes_possibles.get(couleur).get(numero).estVide();
	}
	private boolean ne_peut_pas_avoir_figures(Vector<Vector<MainTarot>> cartes_possibles,byte numero,byte couleur)
	{
		return cartes_possibles.get(couleur).get(numero).estVide()||cartes_possibles.get(couleur).get(numero).carte(0).valeur()<11;
	}
	/**Est vrai si et seulement si le nombre 2*nb_atout_maitres_joueur+3*nb_atout_non_maitres_joueur/2+atouts_jouees depasse strictement 21*/
	private static boolean MaitreAtout(Vector<MainTarot> suites,Vector<MainTarot> cartesJouees,boolean excuse)
	{
		int maitres;
		int nonmaitres;
		int nb;
		Carte c;
		if(cartesJouees.get(1).total()==21)
			return true;
		if(suites.isEmpty())
			return false;
		boolean existeAtoutMaitre=true;
		c=suites.get(0).carte(0);
		for(byte valeur=21;valeur>c.valeur();valeur--)
		{
			if(!cartesJouees.get(1).contient(new CarteTarot(valeur,(byte)1))&&!suites.get(0).contient(new CarteTarot(valeur,(byte)1)))
			{
				existeAtoutMaitre=false;
				break;
			}
		}
		maitres=(existeAtoutMaitre)?suites.get(0).total():0;
		nonmaitres=(existeAtoutMaitre)?0:suites.get(0).total();
		for(int indice_suite=1;indice_suite<suites.size();indice_suite++)
			nonmaitres+=suites.get(indice_suite).total();
		if(suites.lastElement().derniereCarte().valeur()==1)
			nonmaitres--;
		nb=2*maitres+3*nonmaitres/2+cartesJouees.get(1).total()+cartesJouees.get(0).total();
		if(suites.lastElement().derniereCarte().valeur()==1)
			nb++;
		if(excuse)
			nb++;
		return nb>21;
	}
	private Carte fin(String[] raison)
	{
		byte nombre_joueurs=getNombreDeJoueurs();
		byte numero=(byte)((pliEnCours.getEntameur()+pliEnCours.total())%nombre_joueurs);
		MainTarot main_joueur=(MainTarot)getDistribution().main(numero);
		Vector<MainTarot> repartition_couleurs=main_joueur.couleurs();
		MainTarot cartes_jouables=cartes_jouables(repartition_couleurs);
		if(preneur==-1&&cartes_jouables.total()==1)
		{
			raison[0]="C est la seule carte a jouer";
			return cartes_jouables.carte(0);
		}
		Vector<Pli> plisFaits=unionPlis();
		MainTarot cartesJouees=cartesJouees(numero);
		cartesJouees.ajouterCartes(pliEnCours.getCartes());
		Vector<MainTarot> repartitionCartesJouees=cartesJouees.couleurs();
		boolean carteAppeleeJouee=carteAppeleeJouee();
		byte couleurDemandee=pliEnCours.couleurDemandee();
		boolean contientExcuse=cartes_jouables.contient(new CarteTarot((byte)0));
		Vector<Vector<MainTarot>> cartes_possibles=cartesPossibles(!repartitionCartesJouees.get(0).estVide(),repartitionCartesJouees,plisFaits,contientExcuse,main_joueur.couleurs(), numero);
		Vector<Vector<MainTarot>> cartes_certaines=cartesCertaines(cartes_possibles);
		byte ramasseurVirtuel=pliEnCours.getRamasseurTarot(nombre_joueurs);
		CarteTarot carte_la_plus_forte_virtuelle=(CarteTarot)pliEnCours.carteDuJoueur(ramasseurVirtuel,nombre_joueurs,null);/*Carte temporairement maitresse*/
		MainTarot repartition=cartes_jouables.couleurs().get(couleurDemandee);
		Vector<MainTarot> suites=repartition.eclater(repartitionCartesJouees);
		boolean maitre_atout=StrictMaitreAtout(cartes_possibles,numero,main_joueur.couleurs().get(1).eclater(repartitionCartesJouees),repartitionCartesJouees);
		Vector<Vector<MainTarot>> suites_toute_couleur=new Vector<Vector<MainTarot>>();
		for(byte couleur=0;couleur<6;couleur++)
			suites_toute_couleur.addElement(repartition_couleurs.get(couleur).eclater(repartitionCartesJouees));
		Vector<Byte> couleurs_maitresses=CouleursMaitres(suites_toute_couleur,repartitionCartesJouees,cartes_possibles,numero);
		Vector<MainTarot> cartes_maitresses=cartesMaitresses(repartition_couleurs,repartitionCartesJouees);
		Vector<Byte> couleurs_strictes_maitresses=StrictCouleursMaitres(suites_toute_couleur, repartitionCartesJouees, cartes_possibles, numero);
		boolean maitre_jeu=maitre_atout&&couleurs_maitresses.size()==4;
		boolean carte_maitresse;
		if(preneur>-1)
		{
			changerConfiance(plisFaits, numero, cartes_possibles, cartes_certaines, carteAppeleeJouee);
			if(cartes_jouables.total()==1)
			{
				raison[0]="C est la seule carte a jouer";
				return cartes_jouables.carte(0);
			}
			Vector<Byte> joueurs_de_confiance=joueursDeConfiance(numero);
			Vector<Byte> equipe_numero=new Vector<Byte>();
			equipe_numero.addAll(joueurs_de_confiance);
			equipe_numero.addElement(numero);
			Vector<Byte> joueurs_de_non_confiance=joueursDeNonConfiance(numero);
			if(couleurDemandee>1)
			{
				if(cartes_jouables.carte(0).couleur()==couleurDemandee||cartes_jouables.derniereCarte().couleur()==couleurDemandee)
				{
					if(repartition.carte(0).valeur()<carte_la_plus_forte_virtuelle.valeur()||carte_la_plus_forte_virtuelle.couleur()==1)
					{
						if(joueurs_de_confiance.contains(ramasseurVirtuel))
						{
							if(maitre_jeu)
							{
								if(contientExcuse)
								{
									return new CarteTarot((byte)0);
								}
								if(suites.size()==1)
								{
									return repartition.carte(0);
								}
								byte max=0;
								for(byte joueur:autres_joueurs(numero))
								{
									max=(byte)Math.max(cartes_possibles.get(couleurDemandee).get(joueur).total(),max);
								}
								if(suites.get(0).total()>max)
								{
									if(repartition.carte(0).valeur()>10)
									{
										return repartition.carte(0);
									}
									return carteLaPlusPetite(suites);
								}
								return carteLaPlusPetite(suites);
							}
							if(main_joueur.total()==2&&contientExcuse)
							{
								if(equipe_numero.containsAll(ramasseurs(plisFaits)))
								{
									return (cartes_jouables.carte(0).couleur()==0)?cartes_jouables.carte(1):cartes_jouables.carte(0);
								}
								return new CarteTarot((byte)0);
							}
							if(repartition.carte(0).valeur()>10)
							{
								return repartition.carte(0);
							}
							return carteLaPlusPetite(suites);
						}
						if(maitre_jeu&&contientExcuse)
						{
							return new CarteTarot((byte)0);
						}
						if(main_joueur.total()==2&&contientExcuse)
						{
							raison[0]="Vous ne pourrez pas de toute maniere faire tous les plis.";
							return new CarteTarot((byte)0);
						}
						return carteLaPlusPetite(suites);
					}
					if(maitre_jeu)
					{
						return carte_au_dessus_de_carte_virtuelle(carte_la_plus_forte_virtuelle, suites);
					}
					if(repartition.carte(0).valeur()<11)
					{
						return carte_au_dessus_de_carte_virtuelle(carte_la_plus_forte_virtuelle, suites);
					}
					if(suites.size()==1)
					{
						return suites.get(0).carte(0);
					}
					if(repartition.derniereCarte().valeur()>10&&(joueurs_de_confiance.contains(ramasseurVirtuel)||carte_la_plus_forte_virtuelle.valeur()<11))
					{
						return jeuFigureHauteDePlusFaibleSuite(suites);
					}
					if(joueurs_de_confiance.contains(ramasseurVirtuel))
					{
						return petite_figure_joue_dernier_2(suites, carte_la_plus_forte_virtuelle);
					}
					return petite_figure_joue_dernier(carte_la_plus_forte_virtuelle,suites);
				}
				if(cartes_jouables.carte(0).couleur()!=1&&cartes_jouables.derniereCarte().couleur()!=1)
				{
					if(main_joueur.total()==2&&contientExcuse)
					{
						if(equipe_numero.containsAll(ramasseurs(plisFaits)))
						{
							return (cartes_jouables.carte(0).couleur()==0)?cartes_jouables.carte(1):cartes_jouables.carte(0);
						}
						return new CarteTarot((byte)0);
					}
					if(joueurs_de_confiance.contains(ramasseurVirtuel))
					{
						/*Si le joueur se defausse*/
						return defausse_couleur_demandee_sur_partenaire(suites_toute_couleur, repartitionCartesJouees, repartition_couleurs, cartes_maitresses, couleurs_strictes_maitresses, couleurDemandee);
					}
					/*Si le joueur se defausse*/
					return defausse_couleur_demandee_sur_adversaire(suites_toute_couleur, repartitionCartesJouees, repartition_couleurs, cartes_maitresses, couleurs_strictes_maitresses, couleurDemandee);
				}
			}
			if(couleurDemandee==1)
			{
				if(cartes_jouables.carte(0).couleur()!=1&&cartes_jouables.derniereCarte().couleur()!=1)
				{
					if(main_joueur.total()==2&&contientExcuse)
					{
						if(equipe_numero.containsAll(ramasseurs(plisFaits)))
						{
							return (cartes_jouables.carte(0).couleur()==0)?cartes_jouables.carte(1):cartes_jouables.carte(0);
						}
						return new CarteTarot((byte)0);
					}
					if(joueurs_de_confiance.contains(ramasseurVirtuel))
					{
						/*Maintenant le joueur se defausse*/
						return defausse_atout_sur_partenaire(suites_toute_couleur, repartitionCartesJouees, repartition_couleurs, cartes_maitresses, couleurs_strictes_maitresses);
					}
					/*Maintenant le joueur se defausse*/
					return defausse_atout_sur_adversaire(suites_toute_couleur, repartitionCartesJouees, repartition_couleurs, cartes_maitresses, couleurs_strictes_maitresses);
				}
			}
			/*Jeu atout (coupe ou non)*/
			repartition=cartes_jouables.couleurs().get(1);
			if(repartition.carte(0).valeur()<carte_la_plus_forte_virtuelle.valeur()&&carte_la_plus_forte_virtuelle.couleur()==1)
			{
				if(joueurs_de_confiance.contains(ramasseurVirtuel))
				{
					if(maitre_jeu)
					{
						if(contientExcuse)
						{
							return new CarteTarot((byte)0);
						}
						suites=repartition.eclater(repartitionCartesJouees);
						if(suites.size()==1)
						{
							return repartition.carte(0);
						}
						byte max=0;
						for(byte joueur:autres_joueurs(numero))
						{
							max=(byte)Math.max(cartes_possibles.get(couleurDemandee).get(joueur).total(),max);
						}
						if(suites.get(0).total()>max)
						{
							if(repartition.carte(0).valeur()>10)
							{
								return repartition.carte(0);
							}
							return atout_le_plus_petit(suites);
						}
						return atout_le_plus_petit(suites);
					}
					if(main_joueur.total()==2&&contientExcuse)
					{
						if(equipe_numero.containsAll(ramasseurs(plisFaits)))
						{
							return (cartes_jouables.carte(0).couleur()==0)?cartes_jouables.carte(1):cartes_jouables.carte(0);
						}
						return new CarteTarot((byte)0);
					}
					suites=repartition.eclater(repartitionCartesJouees);
					if(repartition.derniereCarte().valeur()>1)
					{
						return atout_le_plus_petit(suites,contientExcuse);
					}
					carte_maitresse=true;
					for(byte joueur:joueurs_de_non_confiance)
					{
						carte_maitresse&=cartes_possibles.get(1).get(joueur).estVide();
					}
					if(carte_maitresse)
					{
						raison[0]="Ne pleurez pas, vous allez pouvoir mener votre Petit au bout";
						return atout_le_plus_petit(suites);
					}
					raison[0]="Vous allez sauver votre Petit maintenant";
					return new CarteTarot((byte)1,(byte)1);
				}
				if(maitre_jeu&&contientExcuse)
				{
					return new CarteTarot((byte)0);
				}
				if(main_joueur.total()==2&&contientExcuse)
				{
					raison[0]="Vous ne pourrez pas de toute maniere faire tous les plis.";
					return new CarteTarot((byte)0);
				}
				return atout_le_plus_petit(repartition.eclater(repartitionCartesJouees), contientExcuse);
			}
			if(main_joueur.total()==2&&contientExcuse)
			{
				if(joueurs_de_confiance.containsAll(ramasseurs(plisFaits)))
				{
					return (main_joueur.carte(0).couleur()==0)?main_joueur.carte(1):main_joueur.carte(0);
				}
				return new CarteTarot((byte)0);
			}
			suites=repartition.eclater(repartitionCartesJouees);
			if(repartition.derniereCarte().valeur()>1)
			{
				if(!contientExcuse)
				{
					return atout_le_plus_petit(suites);
				}
				if(maitre_jeu&&joueurs_de_confiance.containsAll(ramasseurs(plisFaits)))
				{
					return atout_le_plus_petit(suites);
				}
				boolean pas_figure=true;
				for(Carte carte_pli:pliEnCours)
				{
					pas_figure&=((CarteTarot)carte_pli).points()==1;
				}
				if(joueurs_de_confiance.contains(ramasseurVirtuel)||pas_figure)
				{
					return new CarteTarot((byte)0);
				}
				return atout_le_plus_petit(suites);
			}
			carte_maitresse=true;
			for(byte joueur:joueurs_de_non_confiance)
			{
				carte_maitresse&=cartes_possibles.get(1).get(joueur).estVide();
			}
			if(carte_maitresse)
			{
				boolean pas_figure=true;
				for(Carte carte_pli:pliEnCours)
				{
					pas_figure&=((CarteTarot)carte_pli).points()==1;
				}
				if(contientExcuse&&(joueurs_de_confiance.contains(ramasseurVirtuel)||pas_figure))
				{
					return new CarteTarot((byte)0);
				}
				return atout_le_plus_petit(suites);
			}
			if(maitre_jeu)
			{
				if(!contientExcuse||joueurs_de_confiance.containsAll(ramasseurs(plisFaits)))
				{
					return atout_le_plus_petit(suites);
				}
				return new CarteTarot((byte)0);
			}
			return new CarteTarot((byte)1,(byte)1);
		}
		if(pas_jeu_misere())
		{
			if(couleurDemandee>1)
			{
				if(cartes_jouables.carte(0).couleur()==couleurDemandee||cartes_jouables.derniereCarte().couleur()==couleurDemandee)
				{
					if(repartition.carte(0).valeur()<carte_la_plus_forte_virtuelle.valeur()||carte_la_plus_forte_virtuelle.couleur()==1)
					{
						if(maitre_jeu&&contientExcuse)
						{
							return new CarteTarot((byte)0);
						}
						if(main_joueur.total()==2&&contientExcuse)
						{
							raison[0]="Vous ne pourrez pas de toute maniere faire tous les plis.";
							return new CarteTarot((byte)0);
						}
						return carteLaPlusPetite(suites);
					}
				}
				if(cartes_jouables.carte(0).couleur()!=1&&cartes_jouables.derniereCarte().couleur()!=1)
				{
					/*Si le joueur se defausse*/
					if(repartitionCartesJouees.get(1).total()>19&&repartitionCartesJouees.get(couleurDemandee).total()>12)
					{
						if(couleurs_strictes_maitresses.size()==3)
						{
							Vector<Byte> couleurs=new Vector<Byte>();
							for(byte couleur:couleurs_strictes_maitresses)
							{
								if(!repartition_couleurs.get(couleur).estVide())
								{
									couleurs.addElement(couleur);
								}
							}
							return jeu_petite_defausse_maitre(suites_toute_couleur, cartes_maitresses, repartition_couleurs, couleurs);
						}
						Vector<Byte> couleurs=new Vector<Byte>();
						for(byte couleur=2;couleur<6;couleur++)
						{
							if(!repartition_couleurs.get(couleur).estVide()&&repartition_couleurs.get(couleur).carte(0).valeur()<11)
							{
								couleurs.addElement(couleur);
							}
						}
						if(!couleurs.isEmpty())
						{
							return jouer_petite_carte_defausse(suites_toute_couleur,couleurs,repartition_couleurs,repartitionCartesJouees);
						}
						for(byte couleur=2;couleur<6;couleur++)
						{
							if(!repartition_couleurs.get(couleur).estVide())
							{
								couleurs.addElement(couleur);
							}
						}
						return jeu_petite_carte_couleur_figure(suites_toute_couleur, couleurs, repartition_couleurs, repartitionCartesJouees);
					}
					/*Moins de 20 atouts sont joues et moins de 13 cartes de la couleur demandee sont jouees*/
					Vector<Byte> couleurs=new Vector<Byte>();
					for(byte couleur=2;couleur<6;couleur++)
					{
						if(!repartition_couleurs.get(couleur).estVide()&&repartition_couleurs.get(couleur).carte(0).valeur()<11)
						{
							couleurs.addElement(couleur);
						}
					}
					if(!couleurs.isEmpty())
					{
						return jouer_petite_carte_defausse(suites_toute_couleur,couleurs,repartition_couleurs,repartitionCartesJouees);
					}
					for(byte couleur=2;couleur<6;couleur++)
					{
						if(!repartition_couleurs.get(couleur).estVide())
						{
							couleurs.addElement(couleur);
						}
					}
					return jeu_petite_carte_couleur_figure(suites_toute_couleur, couleurs, repartition_couleurs, repartitionCartesJouees);
				}
			}
			if(couleurDemandee==1)
			{
				if(cartes_jouables.carte(0).couleur()!=1&&cartes_jouables.derniereCarte().couleur()!=1)
				{
					/*Maintenant le joueur se defausse*/
					if(repartitionCartesJouees.get(1).total()>17)
					{
						if(couleurs_strictes_maitresses.size()==4)
						{
							Vector<Byte> couleurs=new Vector<Byte>();
							for(byte couleur:couleurs_strictes_maitresses)
							{
								if(!repartition_couleurs.get(couleur).estVide())
								{
									couleurs.addElement(couleur);
								}
							}
							return jeu_petite_defausse_maitre(suites_toute_couleur, cartes_maitresses, repartition_couleurs, couleurs);
						}
						Vector<Byte> couleurs=new Vector<Byte>();
						for(byte couleur=2;couleur<6;couleur++)
						{
							if(!repartition_couleurs.get(couleur).estVide()&&repartition_couleurs.get(couleur).carte(0).valeur()<11)
							{
								couleurs.addElement(couleur);
							}
						}
						if(!couleurs.isEmpty())
						{
							return jouer_petite_carte_defausse(suites_toute_couleur,couleurs,repartition_couleurs,repartitionCartesJouees);
						}
						for(byte couleur=2;couleur<6;couleur++)
						{
							if(!repartition_couleurs.get(couleur).estVide())
							{
								couleurs.addElement(couleur);
							}
						}
						return jeu_petite_carte_couleur_figure(suites_toute_couleur, couleurs, repartition_couleurs, repartitionCartesJouees);
					}
					/*Moins de 20 atouts sont joues et moins de 13 cartes de la couleur demandee sont jouees*/
					Vector<Byte> couleurs=new Vector<Byte>();
					for(byte couleur=2;couleur<6;couleur++)
					{
						if(!repartition_couleurs.get(couleur).estVide()&&repartition_couleurs.get(couleur).carte(0).valeur()<11)
						{
							couleurs.addElement(couleur);
						}
					}
					if(!couleurs.isEmpty())
					{
						return jouer_petite_carte_defausse(suites_toute_couleur,couleurs,repartition_couleurs,repartitionCartesJouees);
					}
					for(byte couleur=2;couleur<6;couleur++)
					{
						if(!repartition_couleurs.get(couleur).estVide())
						{
							couleurs.addElement(couleur);
						}
					}
					return jeu_petite_carte_couleur_figure(suites_toute_couleur, couleurs, repartition_couleurs, repartitionCartesJouees);
				}
			}
			if(cartes_jouables.carte(0).couleur()==1||cartes_jouables.derniereCarte().couleur()==1)
			{
				repartition=cartes_jouables.couleurs().get(1);
				if(repartition.carte(0).valeur()<carte_la_plus_forte_virtuelle.valeur())
				{
					if(maitre_jeu&&contientExcuse)
					{
						return new CarteTarot((byte)0);
					}
					if(main_joueur.total()==2&&contientExcuse)
					{
						raison[0]="Vous ne pourrez pas de toute maniere faire tous les plis.";
						return new CarteTarot((byte)0);
					}
					suites=repartition.eclater(repartitionCartesJouees);
					return atout_le_plus_petit(suites, contientExcuse);
				}
			}
			if(main_joueur.total()==2&&contientExcuse)
			{
				return new CarteTarot((byte)0);
			}
			if(couleurDemandee>1)
			{
				if(cartes_jouables.carte(0).couleur()==couleurDemandee||cartes_jouables.derniereCarte().couleur()==couleurDemandee)
				{
					if(repartition.carte(0).valeur()<11)
					{
						return carte_au_dessus_de_carte_virtuelle(carte_la_plus_forte_virtuelle, suites);
					}
					if(suites.size()==1)
					{
						return suites.get(0).carte(0);
					}
					if(repartition.derniereCarte().valeur()>10&&carte_la_plus_forte_virtuelle.valeur()<11)
					{
						return jeuFigureHauteDePlusFaibleSuite(suites);
					}
					return petite_figure_joue_dernier(carte_la_plus_forte_virtuelle, suites);
				}
			}
			repartition=cartes_jouables.couleurs().get(1);
			suites=repartition.eclater(repartitionCartesJouees);
			if(repartition.derniereCarte().valeur()>1)
			{
				if(!contientExcuse)
				{
					return atout_le_plus_petit(suites);
				}
				boolean pas_figure=true;
				for(Carte carte_pli:pliEnCours)
				{
					pas_figure&=((CarteTarot)carte_pli).points()==1;
				}
				if(pas_figure)
				{
					return new CarteTarot((byte)0);
				}
				return atout_le_plus_petit(suites);
			}
			carte_maitresse=true;
			for(byte joueur:autres_joueurs(numero))
			{
				carte_maitresse&=cartes_possibles.get(1).get(joueur).estVide();
			}
			if(carte_maitresse)
			{
				boolean pas_figure=true;
				for(Carte carte_pli:pliEnCours)
				{
					pas_figure&=((CarteTarot)carte_pli).points()==1;
				}
				if(contientExcuse&&pas_figure)
				{
					return new CarteTarot((byte)0);
				}
				return atout_le_plus_petit(suites);
			}
			if(maitre_jeu)
			{
				if(!contientExcuse)
				{
					return atout_le_plus_petit(suites);
				}
				return new CarteTarot((byte)0);
			}
			return new CarteTarot((byte)1,(byte)1);
		}
		/*Jeu misere*/
		if(couleurDemandee>1)
		{
			if(cartes_jouables.carte(0).couleur()==couleurDemandee||cartes_jouables.derniereCarte().couleur()==couleurDemandee)
			{
				if(repartition.carte(0).valeur()<carte_la_plus_forte_virtuelle.valeur()||carte_la_plus_forte_virtuelle.couleur()==1)
				{
					return repartition.carte(0);
				}
				if(repartition.derniereCarte().valeur()<carte_la_plus_forte_virtuelle.valeur())
				{
					return carte_inf_virt(suites,carte_la_plus_forte_virtuelle);
				}
				if(repartition.derniereCarte().valeur()<11)
				{
					return carte_inf_figure(suites);
				}
				return suites.lastElement().derniereCarte();
			}
			if(cartes_jouables.carte(0).couleur()==1||cartes_jouables.derniereCarte().couleur()==1)
			{
				if(repartition.carte(0).valeur()<carte_la_plus_forte_virtuelle.valeur()&&carte_la_plus_forte_virtuelle.couleur()==1)
				{
					if(repartition_couleurs.get(1).derniereCarte().valeur()==1)
					{
						return new CarteTarot((byte)1,(byte)1);
					}
					return repartition_couleurs.get(1).carte(0);
				}
				return repartition_couleurs.get(1).carte(0);
			}
			/*Defausse sur couleur demandee*/
			Vector<Byte> couleurs=new Vector<Byte>();
			Vector<Byte> couleurs_non_entamees=new Vector<Byte>();
			boolean cont_figure_tt_clr=true;
			for(byte couleur=2;couleur<6;couleur++)
			{
				if(!repartition_couleurs.get(couleur).estVide())
				{
					couleurs.addElement(couleur);
					cont_figure_tt_clr&=repartition_couleurs.get(couleur).carte(0).valeur()>10;
				}
			}
			if(cont_figure_tt_clr)
			{
				return depouille_figure_en_cours(repartition_couleurs, couleurs, repartitionCartesJouees);
			}
			cont_figure_tt_clr=true;
			couleurs=new Vector<Byte>();
			for(byte couleur=2;couleur<6;couleur++)
			{
				cont_figure_tt_clr&=!repartition_couleurs.get(couleur).estVide()==repartitionCartesJouees.get(couleur).total()<14;
				if(cont_figure_tt_clr)
				{
					couleurs.addElement(couleur);
				}
			}
			if(cont_figure_tt_clr)
			{
				for(byte couleur:couleurs)
				{
					if(tours(couleur, plisFaits).isEmpty()&&repartition_couleurs.get(couleur).total()==1)
					{
						couleurs_non_entamees.addElement(couleur);
					}
				}
				if(!couleurs_non_entamees.isEmpty())
				{
					return singleton_fort_depouille(repartition_couleurs, couleurs_non_entamees);
				}
				for(byte couleur:couleurs)
				{
					if(repartition_couleurs.get(couleur).total()==1)
					{
						couleurs_non_entamees.addElement(couleur);
					}
				}
				if(!couleurs_non_entamees.isEmpty())
				{
					return singleton_fort_depouille(repartition_couleurs, couleurs_non_entamees);
				}
			}
			couleurs=new Vector<Byte>();
			for(byte couleur=2;couleur<6;couleur++)
			{
				if(!repartition_couleurs.get(couleur).estVide()&&repartition_couleurs.get(couleur).carte(0).valeur()>10)
				{
					couleurs.addElement(couleur);
				}
			}
			if(!couleurs.isEmpty())
			{
				return depouille_figure_defausse(repartition_couleurs, couleurs, repartitionCartesJouees);
			}
			for(byte couleur=2;couleur<6;couleur++)
			{
				if(!repartition_couleurs.get(couleur).estVide())
				{
					couleurs.addElement(couleur);
				}
			}
			return en_cours_misere_petite(couleurs, repartition_couleurs, repartitionCartesJouees);
		}
		if(cartes_jouables.carte(0).couleur()==1||cartes_jouables.derniereCarte().couleur()==1)
		{
			if(repartition.carte(0).valeur()<carte_la_plus_forte_virtuelle.valeur())
			{
				if(repartition_couleurs.get(1).derniereCarte().valeur()==1)
				{
					return new CarteTarot((byte)1,(byte)1);
				}
			}
			return repartition_couleurs.get(1).carte(0);
		}
		/*Defausse sur couleur atout*/
		Vector<Byte> couleurs=new Vector<Byte>();
		Vector<Byte> couleurs_non_entamees=new Vector<Byte>();
		boolean cont_figure_tt_clr=true;
		for(byte couleur=2;couleur<6;couleur++)
		{
			if(!repartition_couleurs.get(couleur).estVide())
			{
				couleurs.addElement(couleur);
				cont_figure_tt_clr&=repartition_couleurs.get(couleur).carte(0).valeur()>10;
			}
		}
		if(cont_figure_tt_clr)
		{
			return depouille_figure_en_cours(repartition_couleurs, couleurs, repartitionCartesJouees);
		}
		cont_figure_tt_clr=true;
		couleurs=new Vector<Byte>();
		for(byte couleur=2;couleur<6;couleur++)
		{
			cont_figure_tt_clr&=!repartition_couleurs.get(couleur).estVide()==repartitionCartesJouees.get(couleur).total()<14;
			if(cont_figure_tt_clr)
			{
				couleurs.addElement(couleur);
			}
		}
		if(cont_figure_tt_clr)
		{
			for(byte couleur:couleurs)
			{
				if(tours(couleur, plisFaits).isEmpty()&&repartition_couleurs.get(couleur).total()==1)
				{
					couleurs_non_entamees.addElement(couleur);
				}
			}
			if(!couleurs_non_entamees.isEmpty())
			{
				return singleton_fort_depouille(repartition_couleurs, couleurs_non_entamees);
			}
			for(byte couleur:couleurs)
			{
				if(repartition_couleurs.get(couleur).total()==1)
				{
					couleurs_non_entamees.addElement(couleur);
				}
			}
			if(!couleurs_non_entamees.isEmpty())
			{
				return singleton_fort_depouille(repartition_couleurs, couleurs_non_entamees);
			}
		}
		couleurs=new Vector<Byte>();
		for(byte couleur=2;couleur<6;couleur++)
		{
			if(!repartition_couleurs.get(couleur).estVide()&&repartition_couleurs.get(couleur).carte(0).valeur()>10)
			{
				couleurs.addElement(couleur);
			}
		}
		if(!couleurs.isEmpty())
		{
			return depouille_figure_defausse(repartition_couleurs, couleurs, repartitionCartesJouees);
		}
		for(byte couleur=2;couleur<6;couleur++)
		{
			if(!repartition_couleurs.get(couleur).estVide())
			{
				couleurs.addElement(couleur);
			}
		}
		return en_cours_misere_petite(couleurs, repartition_couleurs, repartitionCartesJouees);
	}
	private MainTarot cartes_jouables(Vector<MainTarot> repartition_main)
	{
		Main atouts_joues=((MainTarot)pliEnCours.getCartes()).couleurs().get(1);
		byte couleurDemandee=pliEnCours.couleurDemandee();
		MainTarot cartes_jouables=new MainTarot();
		int nombre_cartes=0;/*Dans la main*/
		for(MainTarot main:repartition_main)
		{
			nombre_cartes+=main.total();
		}
		int nombreDeTours=unionPlis().size();
		if(pliEnCours.estVide()||(pliEnCours.total()==1&&pliEnCours.carte(0).couleur()==0))
		{
			if(carteAppelee!=null&&nombreDeTours==1)
			{
				if(nombre_cartes>repartition_main.get(carteAppelee.couleur()).total())
				{
					for(byte couleur=0;couleur<carteAppelee.couleur();couleur++)
					{
						cartes_jouables.ajouterCartes(repartition_main.get(couleur));
					}
					if(repartition_main.get(carteAppelee.couleur()).contient(carteAppelee))
					{
						cartes_jouables.ajouter(carteAppelee);
					}
					for(byte couleur=(byte)(carteAppelee.couleur()+1);couleur<6;couleur++)
					{
						cartes_jouables.ajouterCartes(repartition_main.get(couleur));
					}
					return cartes_jouables;
				}
			}
			for(byte couleur=0;couleur<6;couleur++)
			{
				cartes_jouables.ajouterCartes(repartition_main.get(couleur));
			}
			return cartes_jouables;
		}
		if(couleurDemandee>1&&!repartition_main.get(couleurDemandee).estVide())
		{
			cartes_jouables.ajouterCartes(repartition_main.get(0));
			cartes_jouables.ajouterCartes(repartition_main.get(couleurDemandee));
			return cartes_jouables;
		}
		if(repartition_main.get(1).estVide())
		{
			cartes_jouables.ajouterCartes(repartition_main.get(0));
			for(byte couleur=2;couleur<6;couleur++)
			{
				cartes_jouables.ajouterCartes(repartition_main.get(couleur));
			}
			return cartes_jouables;
		}
		if(atouts_joues.estVide())
		{
			cartes_jouables.ajouterCartes(repartition_main.get(0));
			cartes_jouables.ajouterCartes(repartition_main.get(1));
			return cartes_jouables;
		}
		byte valeur_forte=atouts_joues.carte(0).valeur();
		cartes_jouables.ajouterCartes(repartition_main.get(0));
		if(repartition_main.get(1).carte(0).valeur()<valeur_forte||valeur_forte<repartition_main.get(1).derniereCarte().valeur())
		{
			cartes_jouables.ajouterCartes(repartition_main.get(1));
			return cartes_jouables;
		}
		for(byte indice_carte=0;repartition_main.get(1).carte(indice_carte).valeur()>valeur_forte;indice_carte++)
		{
			cartes_jouables.ajouter(repartition_main.get(1).carte(indice_carte));
		}
		return cartes_jouables;
	}
	/**Retourne l'ensemble des joueurs ayant ramasse les plis
	 * Les entameurs des plis (sauf le premier car c'est une entame de jeu) sont les ramasseurs des plis precedents respectifs*/
	private Vector<Byte> ramasseurs(Vector<Pli> plisFaits)
	{
		Vector<Byte> ramasseurs=new Vector<Byte>();
		for(int i=2;i<plisFaits.size();i++)
			if(plisFaits.get(i).total()==getNombreDeJoueurs()||plisFaits.get(i).total()==getNombreDeJoueurs()-1)
				if(!ramasseurs.contains(plisFaits.get(i).getEntameur()))
					ramasseurs.addElement(plisFaits.get(i).getEntameur());
		if(pliEnCours.getNumero()>2)
			if(!ramasseurs.contains(pliEnCours.getEntameur()))
				ramasseurs.addElement(pliEnCours.getEntameur());
		return ramasseurs;
	}
	/**Renvoie la plus grande carte basse (n'etant pas une figure) a jouer dans la suite la plus faible de la couleur*/
	private static CarteTarot carteLaPlusPetite(Vector<MainTarot> suites)
	{
		if(suites.size()==1)
		{
			for(Carte carte:suites.get(0))
			{
				if(carte.valeur()<11)
					return(CarteTarot)carte;
			}
			return(CarteTarot)suites.get(0).derniereCarte();
		}
		if(suites.size()==2&&suites.get(1).carte(0).valeur()>10)
		{
			for(Carte carte:suites.get(1))
			{
				if(carte.valeur()<11)
					return(CarteTarot)carte;
			}
			return(CarteTarot)suites.get(1).derniereCarte();
		}
		return(CarteTarot)suites.lastElement().carte(0);
	}
	/**Renvoie la figure la plus grande dans la suite de figure la moins haute(en valeur)*/
	private static CarteTarot jeuFigureHauteDePlusFaibleSuite(Vector<MainTarot> suites)
	{
		if(suites.size()==1)
			return(CarteTarot)suites.get(0).carte(0);
		if(suites.get(1).carte(0).valeur()>10)
			return(CarteTarot)suites.get(1).carte(0);
		return(CarteTarot)suites.get(0).carte(0);
	}
	private static Vector<Byte> CouleursMaitres(Vector<Vector<MainTarot>> suites,Vector<MainTarot> cartesJouees,Vector<Vector<MainTarot>> cartesPossibles,byte numero)
	{
		Vector<Byte> couleurs=new Vector<Byte>();
		for(byte couleur=2;couleur<6;couleur++)
		{
			if(cartesJouees.get(couleur).total()==14||suites.get(couleur).isEmpty())
				couleurs.addElement(couleur);
			else
			{
				int max=0;/*max designe le nombre maximal de cartes probablement possedees par un joueur a une couleur donnee*/
				for(int joueur=0;joueur<cartesPossibles.get(couleur).size()-1;joueur++)
					if(joueur!=numero)
						if(cartesPossibles.get(couleur).get(joueur).total()+cartesPossibles.get(0).get(joueur).total()>max)
							max=cartesPossibles.get(couleur).get(joueur).total()+cartesPossibles.get(0).get(joueur).total();
				boolean existe_carte_maitresse=true;
				Carte c=suites.get(couleur).get(0).carte(0);
				for(byte valeur=14;valeur>c.valeur();valeur--)
				{
					if(!cartesJouees.get(couleur).contient(new CarteTarot(valeur,couleur))&&!suites.get(couleur).get(0).contient(new CarteTarot(valeur,couleur)))
					{
						existe_carte_maitresse=false;
						break;
					}
				}
				int maitres=(existe_carte_maitresse)?suites.get(couleur).get(0).total():0;
				if(maitres>=max||maitres>0&&suites.get(couleur).size()==1)
					couleurs.addElement(couleur);
			}
		}
		return couleurs;
	}
	private static Vector<Byte> StrictCouleursMaitres(Vector<Vector<MainTarot>> suites,Vector<MainTarot> cartesJouees,Vector<Vector<MainTarot>> cartesPossibles,byte numero)
	{
		Vector<Byte> couleurs=new Vector<Byte>();
		for(byte couleur=2;couleur<6;couleur++)
		{
			if(cartesJouees.get(couleur).total()==14)
				couleurs.addElement(couleur);
			else if(!suites.get(couleur).isEmpty())
			{
				int max=0;
				for(int joueur=0;joueur<cartesPossibles.get(couleur).size();joueur++)
					if(joueur!=numero)
						if(cartesPossibles.get(couleur).get(joueur).total()+cartesPossibles.get(0).get(joueur).total()>max)
							max=cartesPossibles.get(couleur).get(joueur).total()+cartesPossibles.get(0).get(joueur).total();
				boolean existeAtoutMaitre=true;
				Carte c=suites.get(couleur).get(0).carte(0);
				for(byte valeur=14;valeur>c.valeur();valeur--)
				{
					if(!cartesJouees.get(couleur).contient(new CarteTarot(valeur,couleur))&&!suites.get(couleur).get(0).contient(new CarteTarot(valeur,couleur)))
					{
						existeAtoutMaitre=false;
						break;
					}
				}
				int maitres=(existeAtoutMaitre)?suites.get(couleur).get(0).total():0;
				if(maitres>=max||maitres>0&&suites.get(couleur).size()==1)
					couleurs.addElement(couleur);
			}
		}
		return couleurs;
	}
	private static Carte atout_le_plus_petit(Vector<MainTarot> suites)
	{
		if(suites.lastElement().derniereCarte().valeur()>1)
			return suites.lastElement().carte(0);
		/*Le joueur a le Petit maintenant*/
		if(suites.lastElement().total()==1)
			return suites.get(suites.size()-2).carte(0);
		return suites.lastElement().carte(0);
	}
	/**Retourne l'atout le plus grand dans la suite la plus faible si l'Excuse n'est pas possedee et le Petit n'est pas dans la main; ou l'Excuse si elle est possedee*/
	private static Carte atout_le_plus_petit(Vector<MainTarot> suites,boolean contientExcuse)
	{
		if(contientExcuse)
			return new CarteTarot((byte)0);
		if(suites.lastElement().derniereCarte().valeur()>1)
			return suites.lastElement().carte(0);
		/*Le joueur a le Petit maintenant*/
		if(suites.lastElement().total()==1)
			return suites.get(suites.size()-2).carte(0);
		return suites.lastElement().carte(0);
	}
	private Carte carte_inf_virt(Vector<MainTarot> suites,Carte carte_virtuelle)
	{
		byte indice_suite_joue=(byte)suites.size();
		for(byte indice_suite=(byte)(indice_suite_joue-1);indice_suite>-1;indice_suite--)
		{
			if(suites.get(indice_suite).carte(0).valeur()<carte_virtuelle.valeur())
			{
				indice_suite_joue--;
			}
			else
			{
				break;
			}
		}
		return suites.get(indice_suite_joue).carte(0);
	}
	private Carte carte_inf_figure(Vector<MainTarot> suites)
	{
		byte indice_suite_joue=(byte)suites.size();
		for(byte indice_suite=(byte)(indice_suite_joue-1);indice_suite>-1;indice_suite--)
		{
			if(suites.get(indice_suite).carte(0).valeur()<11)
			{
				indice_suite_joue--;
			}
			else
			{
				break;
			}
		}
		return suites.get(indice_suite_joue).carte(0);
	}
	private Carte en_cours_misere_petite(Vector<Byte> couleurs,Vector<MainTarot> repartition,Vector<MainTarot> repartition_cartes_jouees)
	{
		for(byte indice_couleur=1;indice_couleur<couleurs.size();indice_couleur++)
		{
			byte couleur1=couleurs.get(0);
			byte couleur2=couleurs.get(indice_couleur);
			if(repartition.get(couleur1).total()>repartition.get(couleur2).total())
			{
				couleurs.setElementAt(couleurs.set(0,couleur2),indice_couleur);
			}
			else if(repartition.get(couleur1).total()==repartition.get(couleur2).total())
			{
				if(repartition.get(couleur1).nombreDeFigures()>repartition.get(couleur2).nombreDeFigures())
				{
					couleurs.setElementAt(couleurs.set(0,couleur2),indice_couleur);
				}
				else if(repartition.get(couleur1).nombreDeFigures()==repartition.get(couleur2).nombreDeFigures())
				{
					boolean egal=true;
					for(byte indice_carte=0;indice_carte<repartition.get(couleur1).total();indice_carte++)
					{
						if(repartition.get(couleur1).carte(indice_carte).valeur()>repartition.get(couleur2).carte(indice_carte).valeur())
						{
							couleurs.setElementAt(couleurs.set(0,couleur2),indice_couleur);
							egal=false;
							break;
						}
						else if(repartition.get(couleur1).carte(indice_carte).valeur()<repartition.get(couleur2).carte(indice_carte).valeur())
						{
							egal=false;
							break;
						}
					}
					if(egal&&repartition_cartes_jouees.get(couleur1).nombreDeFigures()>repartition_cartes_jouees.get(couleur2).nombreDeFigures())
					{
						couleurs.setElementAt(couleurs.set(0,couleur2),indice_couleur);
					}
				}
			}
		}
		return repartition.get(couleurs.get(0)).derniereCarte();
	}
	private Carte depouille_figure_defausse(Vector<MainTarot> repartition,Vector<Byte> couleurs,Vector<MainTarot> repartition_cartes_jouees)
	{
		for(byte indice_couleur=1;indice_couleur<couleurs.size();indice_couleur++)
		{
			byte couleur1=couleurs.get(0);
			byte couleur2=couleurs.get(indice_couleur);
			if(repartition.get(couleur1).total()>repartition.get(couleur2).total())
			{
				couleurs.setElementAt(couleurs.set(0,couleur2),indice_couleur);
			}
			else if(repartition.get(couleur1).total()==repartition.get(couleur2).total())
			{
				if(repartition_cartes_jouees.get(couleur1).total()>repartition_cartes_jouees.get(couleur2).total())
				{
					couleurs.setElementAt(couleurs.set(0,couleur2),indice_couleur);
				}
				else if(repartition_cartes_jouees.get(couleur1).total()==repartition_cartes_jouees.get(couleur2).total())
				{
					for(byte indice_carte=0;indice_carte<repartition.get(couleur1).total();indice_carte++)
					{
						if(repartition.get(couleur1).carte(indice_carte).valeur()<repartition.get(couleurs.get(indice_carte)).carte(indice_carte).valeur())
						{
							break;
						}
						else if(repartition.get(couleur1).carte(indice_carte).valeur()>repartition.get(couleurs.get(indice_carte)).carte(indice_carte).valeur())
						{
							couleurs.setElementAt(couleurs.set(0,couleur2),indice_couleur);
							break;
						}
					}
				}
			}
		}
		return repartition.get(couleurs.get(0)).carte(0);
	}
	private Carte singleton_fort_depouille(Vector<MainTarot> repartition,Vector<Byte> couleurs)
	{
		for(byte indice_couleur=1;indice_couleur<couleurs.size();indice_couleur++)
		{
			byte couleur1=couleurs.get(0);
			byte couleur2=couleur1;
			if(repartition.get(couleur1).carte(0).valeur()<repartition.get(couleur2).carte(0).valeur())
			{
				couleurs.setElementAt(couleurs.set(0,couleur2),indice_couleur);
			}
		}
		return repartition.get(couleurs.get(0)).carte(0);
	}
	private Carte depouille_figure_en_cours(Vector<MainTarot> repartition,Vector<Byte> couleurs,Vector<MainTarot> repartition_cartes_jouees)
	{
		for(byte indice_couleur=1;indice_couleur<couleurs.size();indice_couleur++)
		{
			byte couleur1=couleurs.get(0);
			byte couleur2=couleurs.get(indice_couleur);
			if(repartition.get(couleur1).total()>repartition.get(couleur2).total())
			{
				couleurs.setElementAt(couleurs.set(0,couleur2),indice_couleur);
			}
			else if(repartition.get(couleur1).total()==repartition.get(couleur2).total())
			{
				if(repartition_cartes_jouees.get(couleur1).total()<repartition_cartes_jouees.get(couleur2).total())
				{
					couleurs.setElementAt(couleurs.set(0,couleur2),indice_couleur);
				}
				else if(repartition_cartes_jouees.get(couleur1).total()==repartition_cartes_jouees.get(couleur2).total())
				{
					for(byte indice_carte=0;indice_carte<repartition.get(couleur1).total();indice_carte++)
					{
						if(repartition.get(couleur1).carte(indice_carte).valeur()<repartition.get(couleurs.get(indice_carte)).carte(indice_carte).valeur())
						{
							couleurs.setElementAt(couleurs.set(0,couleur2),indice_couleur);
							break;
						}
						else if(repartition.get(couleur1).carte(indice_carte).valeur()>repartition.get(couleurs.get(indice_carte)).carte(indice_carte).valeur())
						{
							break;
						}
					}
				}
			}
		}
		return repartition.get(couleurs.get(0)).carte(0);
	}
	public void changerConfiance(Vector<Pli> plisFaits,byte numero,Vector<Vector<MainTarot>> cartes_possibles,Vector<Vector<MainTarot>> cartes_certaines,boolean carte_appelee_jouee)
	{
		byte nombre_joueurs=getNombreDeJoueurs();
		if(appele>-1)
		{
			if(pliEnCours.contient(carteAppelee))
			{
				determiner_confiance(numero,nombre_joueurs);
			}
			else
			{
				if(!plisFaits.isEmpty()&&plisFaits.lastElement().contient(carteAppelee))
				{
					determiner_confiance(numero,nombre_joueurs);
				}
				else if(!carte_appelee_jouee)
				{
					for(byte joueur=0;joueur<nombre_joueurs;joueur++)
					{
						if(cartes_certaines.get(carteAppelee.couleur()).get(joueur).contient(carteAppelee))
						{
							determiner_confiance(numero,nombre_joueurs);
							break;
						}
						else if(cartes_possibles.get(carteAppelee.couleur()).get(joueur).estVide())
						{
							if(!meme_equipe(numero, joueur))
							{
								faireMefiance(numero,joueur);
							}
							else
							{
								faireConfiance(numero,joueur);
							}
						}
					}
				}
			}
		}
		//Fin plisFaits.size()>1
	}
	private void determiner_confiance(byte numero,byte nombre_joueurs)
	{
		if(!a_pour_defenseur(numero))
		{
			faireConfiance(numero,preneur);
			faireConfiance(numero,appele);
			for(byte joueur=0;joueur<nombre_joueurs;joueur++)
			{
				if(joueur!=preneur&&joueur!=appele)
					faireMefiance(numero,joueur);
			}
		}
		else
		{
			faireMefiance(numero,preneur);
			faireMefiance(numero,appele);
			for(byte joueur=0;joueur<nombre_joueurs;joueur++)
			{
				if(joueur!=preneur&&joueur!=appele)
					faireConfiance(numero,joueur);
			}
		}
	}
	private Carte defausse_atout_sur_adversaire(Vector<Vector<MainTarot>> suites_toute_couleur,Vector<MainTarot> repartitionCartesJouees,Vector<MainTarot> repartition,Vector<MainTarot> cartes_maitresses,Vector<Byte> couleurs_strictement_maitresses)
	{
		if(repartitionCartesJouees.get(1).total()>17)
		{
			if(couleurs_strictement_maitresses.size()==4)
			{
				Vector<Byte> couleurs=new Vector<Byte>();
				for(byte couleur:couleurs_strictement_maitresses)
				{
					if(!repartition.get(couleur).estVide())
					{
						couleurs.addElement(couleur);
					}
				}
				return jeu_petite_defausse_maitre(suites_toute_couleur, cartes_maitresses, repartition, couleurs);
			}
			Vector<Byte> couleurs=new Vector<Byte>();
			for(byte couleur=2;couleur<6;couleur++)
			{
				if(!repartition.get(couleur).estVide()&&repartition.get(couleur).carte(0).valeur()<11)
				{
					couleurs.addElement(couleur);
				}
			}
			if(!couleurs.isEmpty())
			{
				return jouer_petite_carte_defausse(suites_toute_couleur,couleurs,repartition,repartitionCartesJouees);
			}
			for(byte couleur=2;couleur<6;couleur++)
			{
				if(!repartition.get(couleur).estVide())
				{
					couleurs.addElement(couleur);
				}
			}
			return jeu_petite_carte_couleur_figure(suites_toute_couleur, couleurs, repartition, repartitionCartesJouees);
		}
		/*Moins de 20 atouts sont joues et moins de 13 cartes de la couleur demandee sont jouees*/
		Vector<Byte> couleurs=new Vector<Byte>();
		for(byte couleur=2;couleur<6;couleur++)
		{
			if(!repartition.get(couleur).estVide()&&repartition.get(couleur).carte(0).valeur()<11)
			{
				couleurs.addElement(couleur);
			}
		}
		if(!couleurs.isEmpty())
		{
			return jouer_petite_carte_defausse(suites_toute_couleur,couleurs,repartition,repartitionCartesJouees);
		}
		for(byte couleur=2;couleur<6;couleur++)
		{
			if(!repartition.get(couleur).estVide())
			{
				couleurs.addElement(couleur);
			}
		}
		return jeu_petite_carte_couleur_figure(suites_toute_couleur, couleurs, repartition, repartitionCartesJouees);
	}
	private Carte defausse_couleur_demandee_sur_adversaire(Vector<Vector<MainTarot>> suites_toute_couleur,Vector<MainTarot> repartitionCartesJouees,Vector<MainTarot> repartition,Vector<MainTarot> cartes_maitresses,Vector<Byte> couleurs_strictement_maitresses,byte couleurDemandee)
	{
		if(repartitionCartesJouees.get(1).total()>19&&repartitionCartesJouees.get(couleurDemandee).total()>12)
		{
			if(couleurs_strictement_maitresses.size()==3)
			{
				Vector<Byte> couleurs=new Vector<Byte>();
				for(byte couleur:couleurs_strictement_maitresses)
				{
					if(!repartition.get(couleur).estVide())
					{
						couleurs.addElement(couleur);
					}
				}
				if(!couleurs.isEmpty())
				{
					return jeu_petite_defausse_maitre(suites_toute_couleur, cartes_maitresses, repartition, couleurs);
				}
			}
			Vector<Byte> couleurs=new Vector<Byte>();
			for(byte couleur=2;couleur<6;couleur++)
			{
				if(!repartition.get(couleur).estVide()&&repartition.get(couleur).carte(0).valeur()<11)
				{
					couleurs.addElement(couleur);
				}
			}
			if(!couleurs.isEmpty())
			{
				return jouer_petite_carte_defausse(suites_toute_couleur,couleurs,repartition,repartitionCartesJouees);
			}
			for(byte couleur=2;couleur<6;couleur++)
			{
				if(!repartition.get(couleur).estVide())
				{
					couleurs.addElement(couleur);
				}
			}
			return jeu_petite_carte_couleur_figure(suites_toute_couleur, couleurs, repartition, repartitionCartesJouees);
		}
		/*Moins de 20 atouts sont joues et moins de 13 cartes de la couleur demandee sont jouees*/
		Vector<Byte> couleurs=new Vector<Byte>();
		for(byte couleur=2;couleur<6;couleur++)
		{
			if(!repartition.get(couleur).estVide()&&repartition.get(couleur).carte(0).valeur()<11)
			{
				couleurs.addElement(couleur);
			}
		}
		if(!couleurs.isEmpty())
		{
			return jouer_petite_carte_defausse(suites_toute_couleur,couleurs,repartition,repartitionCartesJouees);
		}
		for(byte couleur=2;couleur<6;couleur++)
		{
			if(!repartition.get(couleur).estVide())
			{
				couleurs.addElement(couleur);
			}
		}
		return jeu_petite_carte_couleur_figure(suites_toute_couleur, couleurs, repartition, repartitionCartesJouees);
	}
	private Carte jeu_petite_carte_couleur_figure(Vector<Vector<MainTarot>> suites,Vector<Byte> couleurs,Vector<MainTarot> repartition,Vector<MainTarot> repartition_cartes_jouees)
	{
		for(byte indice_couleur=1;indice_couleur<couleurs.size();indice_couleur++)
		{
			byte couleur1=couleurs.get(0);
			byte couleur2=couleurs.get(indice_couleur);
			if(repartition.get(couleur1).derniereCarte().valeur()>repartition.get(couleur2).derniereCarte().valeur())
			{
				couleurs.setElementAt(couleurs.set(0,couleur2),indice_couleur);
			}
			else if(repartition.get(couleur1).derniereCarte().valeur()==repartition.get(couleur2).derniereCarte().valeur())
			{
				if(repartition.get(couleur1).total()>repartition.get(couleur2).total())
				{
					couleurs.setElementAt(couleurs.set(0,couleur2),indice_couleur);
				}
				else if(repartition.get(couleur1).total()==repartition.get(couleur2).total())
				{
					if(repartition.get(couleur1).nombreDeFigures()>repartition.get(couleur2).nombreDeFigures())
					{
						couleurs.setElementAt(couleurs.set(0,couleur2),indice_couleur);
					}
					else if(repartition.get(couleur1).nombreDeFigures()==repartition.get(couleur2).nombreDeFigures())
					{
						if(repartition_cartes_jouees.get(couleur1).total()>repartition_cartes_jouees.get(couleur2).total())
						{
							couleurs.setElementAt(couleurs.set(0,couleur2),indice_couleur);
						}
					}
				}
			}
		}
		if(suites.get(couleurs.get(0)).lastElement().carte(0).valeur()<11)
		{
			return suites.get(couleurs.get(0)).lastElement().carte(0);
		}
		return repartition.get(couleurs.get(0)).derniereCarte();
	}
	private Carte defausse_atout_sur_partenaire(Vector<Vector<MainTarot>> suites_toute_couleur,Vector<MainTarot> repartitionCartesJouees,Vector<MainTarot> repartition,Vector<MainTarot> cartes_maitresses,Vector<Byte> couleurs_strictement_maitresses)
	{
		boolean carte_maitresse;
		if(repartitionCartesJouees.get(1).total()>17)
		{
			if(couleurs_strictement_maitresses.size()==4)
			{
				carte_maitresse=true;
				for(byte couleur:couleurs_strictement_maitresses)
				{
					carte_maitresse&=cartes_maitresses.get(couleur-2).total()==repartition.get(couleur).total();
				}
				if(carte_maitresse)
				{
					Vector<Byte> couleurs_figures=new Vector<Byte>();
					for(byte couleur:couleurs_strictement_maitresses)
					{
						if(!repartition.get(couleur).estVide()&&repartition.get(couleur).carte(0).valeur()>10)
						{
							couleurs_figures.addElement(couleur);
						}
					}
					if(!couleurs_figures.isEmpty())
					{
						return jeu_grande_carte_defausse_maitre(couleurs_figures, repartition);
					}
				}
				Vector<Byte> couleurs=new Vector<Byte>();
				for(byte couleur:couleurs_strictement_maitresses)
				{
					if(!repartition.get(couleur).estVide())
					{
						couleurs.addElement(couleur);
					}
				}
				return jeu_petite_defausse_maitre(suites_toute_couleur, cartes_maitresses, repartition, couleurs);
			}
			/*Il peut exister une couleur pour se defausser non strictement maitresse*/
			Vector<Byte> couleurs=new Vector<Byte>();
			for(byte couleur=2;couleur<6;couleur++)
			{
				if(!cartes_maitresses.get(couleur-2).estVide())
				{
					couleurs.addElement(couleur);
				}
			}
			if(!couleurs.isEmpty())
			{
				return jeu_petite_defausse_maitre(suites_toute_couleur, cartes_maitresses, repartition, couleurs);
			}
			for(byte couleur=2;couleur<6;couleur++)
			{
				if(!repartition.get(couleur).estVide()&&repartition.get(couleur).carte(0).valeur()>10)
				{
					couleurs.addElement(couleur);
				}
			}
			if(!couleurs.isEmpty())
			{
				return sauver_figures_defausse(couleurs,repartition,repartitionCartesJouees);
			}
			for(byte couleur=2;couleur<6;couleur++)
			{
				if(!repartition.get(couleur).estVide())
				{
					couleurs.addElement(couleur);
				}
			}
			return jouer_petite_carte_defausse(suites_toute_couleur,couleurs,repartition,repartitionCartesJouees);
		}
		/*Moins de 20 atouts sont joues ou moins de 13 cartes de la couleur demandee sont jouees*/
		if(couleurs_strictement_maitresses.size()==4)
		{
			carte_maitresse=true;
			for(byte couleur:couleurs_strictement_maitresses)
			{
				carte_maitresse&=cartes_maitresses.get(couleur-2).total()==repartition.get(couleur).total();
			}
			if(carte_maitresse)
			{
				Vector<Byte> couleurs_figures=new Vector<Byte>();
				for(byte couleur:couleurs_strictement_maitresses)
				{
					if(!repartition.get(couleur).estVide()&&repartition.get(couleur).carte(0).valeur()>10)
					{
						couleurs_figures.addElement(couleur);
					}
				}
				if(!couleurs_figures.isEmpty())
				{
					return jeu_grande_carte_defausse_maitre(couleurs_figures, repartition);
				}
			}
		}
		Vector<Byte> couleurs=new Vector<Byte>();
		for(byte couleur=2;couleur<6;couleur++)
		{
			if(!repartition.get(couleur).estVide()&&repartition.get(couleur).carte(0).valeur()>10)
			{
				couleurs.addElement(couleur);
			}
		}
		if(!couleurs.isEmpty())
		{
			return sauver_figures_defausse(couleurs,repartition,repartitionCartesJouees);
		}
		for(byte couleur=2;couleur<6;couleur++)
		{
			if(!repartition.get(couleur).estVide())
			{
				couleurs.addElement(couleur);
			}
		}
		return jouer_petite_carte_defausse(suites_toute_couleur,couleurs,repartition,repartitionCartesJouees);
	}
	private Carte defausse_couleur_demandee_sur_partenaire(Vector<Vector<MainTarot>> suites_toute_couleur,Vector<MainTarot> repartitionCartesJouees,Vector<MainTarot> repartition,Vector<MainTarot> cartes_maitresses,Vector<Byte> couleurs_strictement_maitresses,byte couleurDemandee)
	{
		boolean carte_maitresse;
		if(repartitionCartesJouees.get(1).total()>19&&repartitionCartesJouees.get(couleurDemandee).total()>12)
		{
			if(couleurs_strictement_maitresses.size()==3)
			{
				carte_maitresse=true;
				for(byte couleur:couleurs_strictement_maitresses)
				{
					carte_maitresse&=cartes_maitresses.get(couleur-2).total()==repartition.get(couleur).total();
				}
				if(carte_maitresse)
				{
					Vector<Byte> couleurs_figures=new Vector<Byte>();
					for(byte couleur:couleurs_strictement_maitresses)
					{
						if(!repartition.get(couleur).estVide()&&repartition.get(couleur).carte(0).valeur()>10)
						{
							couleurs_figures.addElement(couleur);
						}
					}
					if(!couleurs_figures.isEmpty())
					{
						return jeu_grande_carte_defausse_maitre(couleurs_figures, repartition);
					}
				}
				Vector<Byte> couleurs=new Vector<Byte>();
				for(byte couleur:couleurs_strictement_maitresses)
				{
					if(!repartition.get(couleur).estVide())
					{
						couleurs.addElement(couleur);
					}
				}
				if(!couleurs.isEmpty())
				{
					return jeu_petite_defausse_maitre(suites_toute_couleur, cartes_maitresses, repartition, couleurs);
				}
			}
			/*Il peut exister une couleur pour se defausser non strictement maitresse*/
			Vector<Byte> couleurs=new Vector<Byte>();
			for(byte couleur=2;couleur<6;couleur++)
			{
				if(!cartes_maitresses.get(couleur-2).estVide())
				{
					couleurs.addElement(couleur);
				}
			}
			if(!couleurs.isEmpty())
			{
				return jeu_petite_defausse_maitre(suites_toute_couleur, cartes_maitresses, repartition, couleurs);
			}
			for(byte couleur=2;couleur<6;couleur++)
			{
				if(!repartition.get(couleur).estVide()&&repartition.get(couleur).carte(0).valeur()>10)
				{
					couleurs.addElement(couleur);
				}
			}
			if(!couleurs.isEmpty())
			{
				return sauver_figures_defausse(couleurs,repartition,repartitionCartesJouees);
			}
			for(byte couleur=2;couleur<6;couleur++)
			{
				if(!repartition.get(couleur).estVide())
				{
					couleurs.addElement(couleur);
				}
			}
			return jouer_petite_carte_defausse(suites_toute_couleur,couleurs,repartition,repartitionCartesJouees);
		}
		/*Moins de 20 atouts sont joues ou moins de 13 cartes de la couleur demandee sont jouees*/
		if(couleurs_strictement_maitresses.size()==3)
		{
			carte_maitresse=true;
			for(byte couleur:couleurs_strictement_maitresses)
			{
				carte_maitresse&=cartes_maitresses.get(couleur-2).total()==repartition.get(couleur).total();
			}
			if(carte_maitresse)
			{
				Vector<Byte> couleurs_figures=new Vector<Byte>();
				for(byte couleur:couleurs_strictement_maitresses)
				{
					if(!repartition.get(couleur).estVide()&&repartition.get(couleur).carte(0).valeur()>10)
					{
						couleurs_figures.addElement(couleur);
					}
				}
				if(!couleurs_figures.isEmpty())
				{
					return jeu_grande_carte_defausse_maitre(couleurs_figures, repartition);
				}
			}
		}
		Vector<Byte> couleurs=new Vector<Byte>();
		for(byte couleur=2;couleur<6;couleur++)
		{
			if(!repartition.get(couleur).estVide()&&repartition.get(couleur).carte(0).valeur()>10)
			{
				couleurs.addElement(couleur);
			}
		}
		if(!couleurs.isEmpty())
		{
			return sauver_figures_defausse(couleurs,repartition,repartitionCartesJouees);
		}
		for(byte couleur=2;couleur<6;couleur++)
		{
			if(!repartition.get(couleur).estVide())
			{
				couleurs.addElement(couleur);
			}
		}
		return jouer_petite_carte_defausse(suites_toute_couleur,couleurs,repartition,repartitionCartesJouees);
	}
	private static Carte jeu_grande_carte_defausse_maitre(Vector<Byte> couleurs_figures,Vector<MainTarot> repartition)
	{
		for(byte indice_couleur=1;indice_couleur<couleurs_figures.size();indice_couleur++)
		{
			byte couleur1=couleurs_figures.get(0);
			byte couleur2=couleurs_figures.get(indice_couleur);
			if(repartition.get(couleur1).total()<repartition.get(couleur2).total())
			{
				couleurs_figures.setElementAt(couleurs_figures.set(0,couleur2),indice_couleur);
			}
			else if(repartition.get(couleur1).total()==repartition.get(couleur2).total())
			{
				boolean egal=true;
				for(byte indice_carte=0;indice_carte<repartition.get(couleur1).total();indice_carte++)
				{
					if(repartition.get(couleur1).carte(indice_carte).valeur()>repartition.get(couleur2).carte(indice_carte).valeur())
					{
						egal=false;
						break;
					}
					else if(repartition.get(couleur1).carte(indice_carte).valeur()<repartition.get(couleur2).carte(indice_carte).valeur())
					{
						couleurs_figures.setElementAt(couleurs_figures.set(0,couleur2),indice_couleur);
						egal=false;
						break;
					}
				}
				if(egal&&repartition.get(couleur1).nombreDeFigures()<repartition.get(couleur2).nombreDeFigures())
				{
					couleurs_figures.setElementAt(couleurs_figures.set(0,couleur2),indice_couleur);
				}
			}
		}
		return repartition.get(couleurs_figures.get(0)).carte(0);
	}
	private Carte sauver_figures_defausse(Vector<Byte> couleurs_figures,Vector<MainTarot> repartition,Vector<MainTarot> repartition_cartes_jouees)
	{
		for(byte indice_couleur=1;indice_couleur<couleurs_figures.size();indice_couleur++)
		{
			byte couleur1=couleurs_figures.get(0);
			byte couleur2=couleurs_figures.get(indice_couleur);
			if(repartition.get(couleur1).carte(0).valeur()<11&&repartition.get(couleur2).carte(0).valeur()>10)
			{
				couleurs_figures.setElementAt(couleurs_figures.set(0,couleur2),indice_couleur);
			}
			else if(repartition.get(couleur1).total()>repartition.get(couleur2).total())
			{
				couleurs_figures.setElementAt(couleurs_figures.set(0,couleur2),indice_couleur);
			}
			else if(repartition.get(couleur1).total()==repartition.get(couleur2).total())
			{
				if(repartition_cartes_jouees.get(couleur1).total()<repartition_cartes_jouees.get(couleur2).total())
				{
					couleurs_figures.setElementAt(couleurs_figures.set(0,couleur2),indice_couleur);
				}
				else if(repartition_cartes_jouees.get(couleur1).total()==repartition_cartes_jouees.get(couleur2).total())
				{
					for(int indice_carte=0;indice_carte<repartition.get(couleur1).total();indice_carte++)
					{
						if(repartition.get(couleur1).carte(indice_carte).valeur()<repartition.get(couleur2).carte(indice_carte).valeur())
						{
							couleurs_figures.setElementAt(couleurs_figures.set(0,couleur2),indice_couleur);
							break;
						}
						else if(repartition.get(couleur1).carte(indice_carte).valeur()>repartition.get(couleur2).carte(indice_carte).valeur())
						{
							break;
						}
					}
				}
			}
		}
		return repartition.get(couleurs_figures.get(0)).carte(0);
	}
	private Carte jeu_petite_defausse_maitre(Vector<Vector<MainTarot>> suites,Vector<MainTarot> cartes_maitresses,Vector<MainTarot> repartition,Vector<Byte> couleurs)
	{
		for(byte indice_couleur=1;indice_couleur<couleurs.size();indice_couleur++)
		{
			byte couleur1=couleurs.get(0);
			byte couleur2=couleurs.get(indice_couleur);
			if(repartition.get(couleur1).carte(0).valeur()>10&&repartition.get(couleur2).carte(0).valeur()<11)
			{
				couleurs.setElementAt(couleurs.set(0,couleur2),indice_couleur);
			}
			else if(repartition.get(couleur1).total()<repartition.get(couleur2).total())
			{
				couleurs.setElementAt(couleurs.set(0,couleur2),indice_couleur);
			}
			else if(repartition.get(couleur1).total()==repartition.get(couleur2).total())
			{
				if(cartes_maitresses.get(couleur1-2).total()>cartes_maitresses.get(couleur2-2).total())
				{
					couleurs.setElementAt(couleurs.set(0,couleur2),indice_couleur);
				}
				else if(cartes_maitresses.get(couleur1-2).total()==cartes_maitresses.get(couleur2-2).total())
				{
					boolean egal=true;
					for(byte indice_carte=0;indice_carte<repartition.get(couleur1).total();indice_carte++)
					{
						if(repartition.get(couleur1).carte(indice_carte).valeur()<repartition.get(couleur2).carte(indice_carte).valeur())
						{
							egal=false;
							break;
						}
						else if(repartition.get(couleur1).carte(indice_carte).valeur()>repartition.get(couleur2).carte(indice_carte).valeur())
						{
							couleurs.setElementAt(couleurs.set(0,couleur2),indice_couleur);
							egal=false;
							break;
						}
					}
					if(egal&&repartition.get(couleur1).nombreDeFigures()>repartition.get(couleur2).nombreDeFigures())
					{
						couleurs.setElementAt(couleurs.set(0,couleur2),indice_couleur);
					}
				}
			}
		}
		if(suites.get(couleurs.get(0)).lastElement().carte(0).valeur()<11)
		{
			return suites.get(couleurs.get(0)).lastElement().carte(0);
		}
		return repartition.get(couleurs.get(0)).derniereCarte();
	}
	private Carte jouer_petite_carte_defausse(Vector<Vector<MainTarot>> suites,Vector<Byte> couleurs,Vector<MainTarot> repartition,Vector<MainTarot> repartition_cartes_jouees)
	{
		for(byte indice_couleur=1;indice_couleur<couleurs.size();indice_couleur++)
		{
			byte couleur1=couleurs.get(0);
			byte couleur2=couleurs.get(indice_couleur);
			if(repartition.get(couleur1).carte(0).valeur()>10&&repartition.get(couleur2).carte(0).valeur()<11)
			{
				couleurs.setElementAt(couleurs.set(0,couleur2),indice_couleur);
			}
			else if(repartition.get(couleur1).total()>repartition.get(couleur2).total())
			{
				couleurs.setElementAt(couleurs.set(0,couleur2),indice_couleur);
			}
			else if(repartition.get(couleur1).total()==repartition.get(couleur2).total())
			{
				if(repartition_cartes_jouees.get(couleur1).total()>repartition_cartes_jouees.get(couleur2).total())
				{
					couleurs.setElementAt(couleurs.set(0,couleur2),indice_couleur);
				}
				else if(repartition_cartes_jouees.get(couleur1).total()==repartition_cartes_jouees.get(couleur2).total())
				{
					for(byte indice_carte=0;indice_carte<repartition.get(couleur1).total();indice_carte++)
					{
						if(repartition.get(couleur1).carte(indice_carte).valeur()>repartition.get(couleur2).carte(indice_carte).valeur())
						{
							couleurs.setElementAt(couleurs.set(0,couleur2),indice_couleur);
							break;
						}
						else if(repartition.get(couleur1).carte(indice_carte).valeur()<repartition.get(couleur2).carte(indice_carte).valeur())
						{
							break;
						}
					}
				}
			}
		}
		return suites.get(couleurs.get(0)).lastElement().carte(0);
	}
	/**Retourne l'ensemble des cartes maitresses dans leur propre couleur mais pas les atouts maitres donc pour recuperer
	 * la couleur n° i il faut ecrire cartesMaitresses.get(i-2)*/
	private Vector<MainTarot> cartesMaitresses(Vector<MainTarot> couleurs,Vector<MainTarot> cartesJouees)
	{
		Vector<MainTarot> nb=new Vector<MainTarot>();
		for (byte i=2;i<6;i++) {
			Main cartes=couleurs.get(i);
			MainTarot suite=new MainTarot();
			MainTarot union=new MainTarot();
			union.ajouterCartes(cartesJouees.get(i));//C'est la reunion des cartes jouees dans le jeu et de celles du joueur
			for(Carte carte:cartes)
			{
				if(union.estVide()||union.derniereCarte().valeur()>carte.valeur())
				{
					union.ajouter(carte);
				}
				else
				{
					int k=0;
					for(;union.carte(k).valeur()>carte.valeur();k++);
					union.ajouter(carte,k);
				}
			}
			for(int j=0;j<union.total();j++)
			{
				if(j+union.carte(j).valeur()==14)
				{
					if(cartes.contient(union.carte(j)))
						suite.ajouter(union.carte(j));
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
	/**Retourne vrai si et seulement si le joueur peut ramasser tous les atouts sans en perdre
	 * @param cartes_possibles les cartes probablement possedees par les autres joueurs
	 * @param numero le numero du joueur concerne
	 * @param suites l'ensemble des suites d'atout du joueur concerne
	 * @param cartesJouees l'ensemble de toutes les cartes jouees reparties dans toutes les couleurs*/
	private static boolean StrictMaitreAtout(Vector<Vector<MainTarot>> cartes_possibles,byte numero,Vector<MainTarot> suites,Vector<MainTarot> cartesJouees)
	{
		int max=0;/*max designe le nombre maximal de cartes probablement possedees par un joueur*/
		for(int joueur=0;joueur<cartes_possibles.get(1).size()-1;joueur++)//La taille de atoutsPossibles correspond au nombre de joueurs+1
			if(joueur!=numero)
				if(cartes_possibles.get(1).get(joueur).total()+cartes_possibles.get(0).get(joueur).total()>max)
					max=cartes_possibles.get(1).get(joueur).total()+cartes_possibles.get(0).get(joueur).total();
		/*Fin for int joueur=0;joueur<cartes_possibles.get(1).size()-1;joueur++
		 *(Fin boucle sur le calcul de la valeur maximale possible des atouts*/
		if(max==0)/*S'il est impossible que les autres joueurs aient de l'atout (Excuse incluse)*/
			return true;
		if(cartesJouees.get(1).total()==21)
			return true;
		if(suites.isEmpty())
			return false;
		boolean existeAtoutMaitre=true;
		Carte atout_petit_suite_haute=suites.get(0).carte(0);
		for(byte valeur=21;valeur>atout_petit_suite_haute.valeur();valeur--)
		{
			if(!cartesJouees.get(1).contient(new CarteTarot(valeur,(byte)1))&&!suites.get(0).contient(new CarteTarot(valeur,(byte)1)))
			{
				existeAtoutMaitre=false;
				break;
			}
		}
		return existeAtoutMaitre&&suites.get(0).total()>=max;
	}
	/**Retourne l'ensemble des cartes des couleurs (avec l'Excuse) probablement possedees par les autres joueurs
	 * Pour premier indice (premier get) couleur, deuxieme indice joueur
	 * @param numero*/
	public Vector<Vector<MainTarot>> cartesPossibles(boolean excuseJouee,Vector<MainTarot> repartitionCartesJouees,Vector<Pli> plisFaits,boolean joueurExcuse,Vector<MainTarot> cartesJoueur, byte numero)
	{
		Vector<Vector<MainTarot>> m=new Vector<Vector<MainTarot>>();
		m.addElement(new Vector<MainTarot>());
		byte joueur=0;
		for(;joueur<numero;joueur++)
		{
			m.get(0).addElement(new MainTarot());
			if(!excuseJouee&&!joueurExcuse)
			{
				m.get(0).lastElement().ajouter(new CarteTarot((byte)0));
			}
			if(annonces.get(joueur).contains(new Annonce(Miseres.Tete))||annonces.get(joueur).contains(new Annonce(Miseres.Atout)))
			{
				m.get(0).get(joueur).supprimerCartes();
			}
		}
		m.get(0).addElement(new MainTarot());
		if(joueurExcuse)
		{
			m.get(0).lastElement().ajouter(new CarteTarot((byte)0));
		}
		for(joueur++;joueur<getNombreDeJoueurs();joueur++)
		{
			m.get(0).addElement(new MainTarot());
			if(!excuseJouee&&!joueurExcuse)
			{
				m.get(0).lastElement().ajouter(new CarteTarot((byte)0));
			}
			if(annonces.get(joueur).contains(new Annonce(Miseres.Tete))||annonces.get(joueur).contains(new Annonce(Miseres.Atout)))
			{
				m.get(0).get(joueur).supprimerCartes();
			}
		}
		m.get(0).addElement(new MainTarot());
		if(contrat.force()==0||contrat.force()>2)
		{/*Si le contrat est Petite ou Garde alors l'Excuse ne peut pas appartenir au chien*/
			if(!excuseJouee&&!joueurExcuse)
			{
				m.get(0).lastElement().ajouter(new CarteTarot((byte)0));
			}
		}
		else
		{
			for(joueur=0;joueur<getNombreDeJoueurs();joueur++)
			{
				if(preneur!=joueur)
				{//L'Excuse du chien (si il est vu) ne peut etre possedee que par le preneur
					if(!m.get(0).get(joueur).estVide()&&getDistribution().derniereMain().contient(new CarteTarot((byte)0)))
					{
						m.get(0).get(joueur).jouer(new CarteTarot((byte)0));
					}
				}
			}
		}
		for(joueur=0;joueur<getNombreDeJoueurs();joueur++)
		{//L'Excuse dans une poignee annule toute possibilite qu'un autre joueur ait celle-ci
			for(byte i=0;i<poignees.size();i++)
			{
				if(getPoignee(i).contient(new CarteTarot((byte)0)))
				{
					if(i!=joueur&&!m.get(0).get(joueur).estVide())
					{
						m.get(0).get(joueur).jouer(new CarteTarot((byte)0));
					}
					else
					{/*Si l'Excuse est presente dans une poignee alors le joueur ne peut pas posseder d'autre atout que ceux de la poignee*/
						for(int j=0;j<m.get(0).get(joueur).total();)
						{
							if(!getPoignee(i).contient(m.get(0).get(joueur).carte(j)))
							{
								m.get(0).get(joueur).jouer(m.get(0).get(joueur).carte(j));
							}
							else
							{
								j++;
							}
						}
					}
				}
			}
			if(!m.get(0).get(joueur).estVide()&&pliEnCours.contient(new CarteTarot((byte)0)))
			{
				m.get(0).get(joueur).jouer(new CarteTarot((byte)0));
			}
		}
		if(contrat.force()==0||contrat.force()>2)
		{
			for(MainTarot poignee:poignees)
			{
				if(!m.get(0).lastElement().estVide()&&poignee.contient(new CarteTarot((byte)0)))
				{
					m.get(0).lastElement().jouer(new CarteTarot((byte)0));
				}
			}
			if(!m.get(0).lastElement().estVide()&&pliEnCours.contient(new CarteTarot((byte)0)))
			{
				m.get(0).lastElement().jouer(new CarteTarot((byte)0));
			}
		}
		m.addElement(atoutsPossibles(repartitionCartesJouees.get(1),plisFaits,cartesJoueur.get(1),numero));
		for(byte couleur=2;couleur<6;couleur++)//On fait une boucle sur les couleurs autres que l'atout
		{
			m.addElement(cartesPossibles(couleur,repartitionCartesJouees.get(couleur),plisFaits,cartesJoueur.get(couleur),numero));
		}
		return m;
	}
	/**Retourne l'ensemble des atouts (sans l'Excuse) probablement possedes par les autres joueurs
	 * @param numero*/
	private Vector<MainTarot> atoutsPossibles(MainTarot atoutsJoues,Vector<Pli> plisFaits,MainTarot atoutsJoueur, byte numero)
	{
		Vector<MainTarot> m=new Vector<MainTarot>();
		byte joueur=0;
		for(;joueur<numero;joueur++)
		{
			m.addElement(new MainTarot());
			for(byte valeur=21;valeur>0;valeur--)
			{
				if(!atoutsJoues.contient(new CarteTarot(valeur,(byte)1))&&!atoutsJoueur.contient(new CarteTarot(valeur,(byte)1)))
				{
					m.lastElement().ajouter(new CarteTarot(valeur,(byte)1));
				}
			}
			if(defausseTarot(joueur,plisFaits))
			{//Les joueurs se defaussant sur atout ou couleur demandee ne peuvent pas avoir de l'atout
				m.get(joueur).supprimerCartes();
			}
			if(annonces.get(joueur).contains(new Annonce(Miseres.Atout)))
			{
				m.get(joueur).supprimerCartes();
			}
			if(annonces.get(joueur).contains(new Annonce(Miseres.Tete)))
			{
				m.get(joueur).supprimerCartes(m.get(joueur).bouts());
			}
		}
		/*joueur, ici, vaut numero*/
		m.addElement(new MainTarot());
		m.lastElement().ajouterCartes(atoutsJoueur);
		for(joueur++;joueur<getNombreDeJoueurs();joueur++)
		{
			m.addElement(new MainTarot());
			for(byte valeur=21;valeur>0;valeur--)
			{
				if(!atoutsJoues.contient(new CarteTarot(valeur,(byte)1))&&!atoutsJoueur.contient(new CarteTarot(valeur,(byte)1)))
				{
					m.lastElement().ajouter(new CarteTarot(valeur,(byte)1));
				}
			}
			if(defausseTarot(joueur,plisFaits))
			{//Les joueurs se defaussant sur atout ou couleur demandee ne peuvent pas avoir de l'atout
				m.get(joueur).supprimerCartes();
			}
			if(annonces.get(joueur).contains(new Annonce(Miseres.Atout)))
			{
				m.get(joueur).supprimerCartes();
			}
			if(annonces.get(joueur).contains(new Annonce(Miseres.Tete)))
			{
				m.get(joueur).supprimerCartes(m.get(joueur).bouts());
			}
		}
		m.addElement(new MainTarot());
		if(contrat.force()>0&&contrat.force()<3)
		{
			/*Les atouts ecartes sont annonces donc certains de faire partie du chien*/
			m.lastElement().ajouterCartes(((MainTarot)plisFaits.get(0).getCartes()).couleur((byte)1));
		}
		else
		{
			/*Si le chien est inconnu de tous alors n'importe quel atout non joue et non possede par le joueur peut etre dans le chien*/
			for(byte valeur=21;valeur>0;valeur--)
			{
				if(!atoutsJoues.contient(new CarteTarot(valeur,(byte)1))&&!atoutsJoueur.contient(new CarteTarot(valeur,(byte)1)))
				{
					m.lastElement().ajouter(new CarteTarot(valeur,(byte)1));
				}
			}
		}
		Vector<Byte> numerosPlisAvec2Atouts=new Vector<Byte>();
		int indice=0;
		int min=getNombreDeJoueurs();
		for(byte indice_pli=1;indice_pli<plisFaits.size();indice_pli++)
		{
			if(plisFaits.get(indice_pli).getCartes().tailleCouleur((byte)1)>1)
			{
				numerosPlisAvec2Atouts.addElement(indice_pli);
				if(plisFaits.get(indice_pli).total()<min)
				{
					min=plisFaits.get(indice_pli).total();
					indice=indice_pli;
				}
			}
		}
		//pli le plus petit avec une seule carte
		Pli minPli=min<getNombreDeJoueurs()?plisFaits.get(indice+1):null;
		//Le pli apres celui qui est compose de nbJ-1 cartes est de une carte
		for(byte indice_pli:numerosPlisAvec2Atouts)
		{
			Pli pli=plisFaits.get(indice_pli);
			for(int indice_carte=0;indice_carte<pli.total();indice_carte++)
			{
				/*On parcourt les atouts joues dans le pli pour eliminer des possibilites de possession d'atouts par obligation de monter
				 * quand on peut*/
				Carte maxAtout=pli.carte(indice_carte);
				if(maxAtout.couleur()==1)
				{
					boolean max=true;//max represente l'assertion tout atout n°j est superieur a ceux qui sont deja joues
					for(int indice_carte_jouee_avant=0;indice_carte_jouee_avant<indice_carte;indice_carte_jouee_avant++)
					{
						Carte carte=pli.carte(indice_carte_jouee_avant);
						if(carte.couleur()==1)
						{
							max&=carte.valeur()<pli.carte(indice_carte).valeur();
							if(maxAtout.equals(pli.carte(indice_carte))||maxAtout.valeur()<carte.valeur())
							{
								maxAtout=carte;
							}
						}
					}
					if(!max)
					{/*Si max est faux alors maxAtout ne vaut pas null*/
						joueur=pli.joueurAyantJoue(pli.carte(indice_carte),getNombreDeJoueurs(),minPli);
						for(int indice_carte_possible=0;indice_carte_possible<m.get(joueur).total();)
						{
							if(m.get(joueur).carte(indice_carte_possible).valeur()>maxAtout.valeur())
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
		if(pliEnCours.getCartes().tailleCouleur((byte)1)>1)
		{
			for(int indice_carte=0;indice_carte<pliEnCours.total();indice_carte++)
			{
				/*On parcourt les atouts joues dans le pli pour eliminer des possibilites de possession d'atouts par obligation de monter
				 * quand on peut*/
				Carte maxAtout=pliEnCours.carte(indice_carte);
				if(maxAtout.couleur()==1)
				{
					boolean max=true;//max represente l'assertion tout atout n°j est superieur a ceux qui sont deja joues
					for(int indice_carte_jouee_avant=0;indice_carte_jouee_avant<indice_carte;indice_carte_jouee_avant++)
					{
						Carte carte=pliEnCours.carte(indice_carte_jouee_avant);
						if(carte.couleur()==1)
						{
							max&=carte.valeur()<pliEnCours.carte(indice_carte).valeur();
							if(maxAtout.equals(pliEnCours.carte(indice_carte))||maxAtout.valeur()<carte.valeur())
							{
								maxAtout=carte;
							}
						}
					}
					if(!max)
					{/*Si max est faux alors maxAtout ne vaut pas null*/
						joueur=pliEnCours.joueurAyantJoue(pliEnCours.carte(indice_carte),getNombreDeJoueurs(),minPli);
						for(int indice_carte_possible=0;indice_carte_possible<m.get(joueur).total();)
						{
							if(m.get(joueur).carte(indice_carte_possible).valeur()>maxAtout.valeur())
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
		if(contrat.force()>0&&contrat.force()<3)
		{
			for(joueur=0;joueur<getNombreDeJoueurs();joueur++)
			{
				if(preneur!=joueur)
				{
					//Les atouts du chien (si il est vu) ne peuvent possedes que par le preneur
					for(Carte carte:getDistribution().derniereMain())
					{
						if(carte.couleur()<2&&m.get(joueur).contient(carte))
						{
							m.get(joueur).jouer(carte);
						}
					}
				}
				/*Les atouts eventuellement ecartes au chien sont vus par les autres joueurs et ne peuvent pas etre joues dans les plis suivants*/
				for(Carte carte:plisFaits.get(0))
				{
					if(carte.couleur()==1&&m.get(joueur).contient(carte))
					{
						m.get(joueur).jouer(carte);
					}
				}
			}
		}
		for(joueur=0;joueur<getNombreDeJoueurs();joueur++)
		{
			for(byte joueur2=0;joueur2<poignees.size();joueur2++)
			{
				if(joueur2!=joueur)
				{
					for(Carte carte:getPoignee(joueur2))
					{
						if(m.get(joueur).contient(carte))
						{
							m.get(joueur).jouer(carte);
						}
					}
				}
				else if(getPoignee(joueur).contient(new CarteTarot((byte)0)))
				{
					for(int j=0;j<m.get(joueur).total();)
					{
						if(!getPoignee(joueur).contient(m.get(joueur).carte(j)))
						{
							m.get(joueur).jouer(m.get(joueur).carte(j));
						}
						else
						{
							j++;
						}
					}
				}
			}
			for(Carte carte:pliEnCours)
			{
				if(m.get(joueur).contient(carte))
				{
					m.get(joueur).jouer(carte);
				}
			}
			Carte carte_du_joueur=pliEnCours.carteDuJoueur(joueur, getNombreDeJoueurs(),null);
			byte couleur_demandee=pliEnCours.couleurDemandee();
			if(carte_du_joueur!=null&&carte_du_joueur.couleur()>1&&couleur_demandee!=carte_du_joueur.couleur())
			{/*Si le joueur a joue une carte autre que l'atout et l'Excuse et que la couleur demandee alors il se defausse*/
				m.get(joueur).supprimerCartes();
			}
		}
		if(contrat.force()==0||contrat.force()>2)
		{
			for(MainTarot main:poignees)
			{
				for(Carte carte:main)
				{
					if(m.lastElement().contient(carte))
					{
						m.lastElement().jouer(carte);
					}
				}
			}
			for(Carte carte:pliEnCours)
			{
				if(m.lastElement().contient(carte))
				{
					m.lastElement().jouer(carte);
				}
			}
		}
		return m;
	}
	public MainTarot getPoignee(byte b)
	{
		return poignees.get(b);
	}
	/**Retourne l'ensemble des cartes d'une meme couleur autre que l'atout probablement possedees par les autres joueurs on tient compte du pli en cours
	 * @param numero*/
	private Vector<MainTarot> cartesPossibles(byte couleur,MainTarot cartes_jouees,Vector<Pli> plisFaits,MainTarot cartes_joueur, byte numero)
	{
		Vector<MainTarot> m=new Vector<MainTarot>();
		byte joueur=0;
		for(;joueur<numero;joueur++)
		{
			m.addElement(new MainTarot());
			for(byte valeur=14;valeur>0;valeur--)
			{
				if(!cartes_jouees.contient(new CarteTarot(valeur,couleur))&&!cartes_joueur.contient(new CarteTarot(valeur,couleur)))
				{
					m.lastElement().ajouter(new CarteTarot(valeur,couleur));
				}
			}
			if(defausseTarot(joueur,couleur,plisFaits)||coupeTarot(couleur,joueur,plisFaits))
			{//Les joueurs se defaussant sur atout ou couleur demandee ne peuvent pas avoir de l'atout
				m.get(joueur).supprimerCartes();
			}
			if(annonces.get(joueur).contains(new Annonce(Miseres.Couleur)))
			{
				m.get(joueur).supprimerCartes();
			}
			if(annonces.get(joueur).contains(new Annonce(Miseres.Tete))||annonces.get(joueur).contains(new Annonce(Miseres.Figure)))
			{
				m.get(joueur).supprimerCartes(m.get(joueur).figures(couleur));
			}
			if(annonces.get(joueur).contains(new Annonce(Miseres.Cartes_basses)))
			{
				m.get(joueur).supprimerCartes(m.get(joueur).cartesBasses(couleur));
			}
		}
		m.addElement(new MainTarot());/*Pour le joueur numero*/
		m.lastElement().ajouterCartes(cartes_joueur);
		for(joueur++;joueur<getNombreDeJoueurs();joueur++)
		{
			m.addElement(new MainTarot());
			for(byte valeur=14;valeur>0;valeur--)
			{
				if(!cartes_jouees.contient(new CarteTarot(valeur,couleur))&&!cartes_joueur.contient(new CarteTarot(valeur,couleur)))
				{
					m.lastElement().ajouter(new CarteTarot(valeur,couleur));
				}
			}
			if(defausseTarot(joueur,couleur,plisFaits)||coupeTarot(couleur,joueur,plisFaits))
			{//Les joueurs se defaussant sur atout ou couleur demandee ne peuvent pas avoir de l'atout
				m.get(joueur).supprimerCartes();
			}
			if(annonces.get(joueur).contains(new Annonce(Miseres.Couleur)))
			{
				m.get(joueur).supprimerCartes();
			}
			if(annonces.get(joueur).contains(new Annonce(Miseres.Tete))||annonces.get(joueur).contains(new Annonce(Miseres.Figure)))
			{
				m.get(joueur).supprimerCartes(m.get(joueur).figures(couleur));
			}
			if(annonces.get(joueur).contains(new Annonce(Miseres.Cartes_basses)))
			{
				m.get(joueur).supprimerCartes(m.get(joueur).cartesBasses(couleur));
			}
		}
		m.addElement(new MainTarot());
		if(contrat.force()==0||contrat.force()>2)
		{
			for(byte valeur=14;valeur>0;valeur--)
			{
				if(!cartes_jouees.contient(new CarteTarot(valeur,couleur))&&!cartes_joueur.contient(new CarteTarot(valeur,couleur)))
				{
					m.lastElement().ajouter(new CarteTarot(valeur,couleur));
				}
			}
		}
		else
		{
			if(numero==preneur)
			{/*Le preneur sait ce qu'il a mis au chien pour une Petite ou une Garde*/
				m.lastElement().ajouterCartes(((MainTarot)plisFaits.get(0).getCartes()).couleur(couleur));
			}
			else
			{
				if(plisFaits.get(0).getCartes().tailleCouleur((byte)1)>0)
				{/*Si le preneur est oblige d'ecarter des atouts alors les cartes autre que le roi de couleur du chien sont certainement ecartees*/
					for(Carte carte:((MainTarot)getDistribution().derniereMain()).couleur(couleur))
					{
						if(carte.valeur()<14)
						{
							m.lastElement().ajouter(carte);
						}
					}
				}
				else
				{
					for(byte valeur=13;valeur>0;valeur--)
					{
						if(!cartes_jouees.contient(new CarteTarot(valeur,couleur))&&!cartes_joueur.contient(new CarteTarot(valeur,couleur)))
						{
							m.lastElement().ajouter(new CarteTarot(valeur,couleur));
						}
					}
				}
			}
			for(joueur=0;joueur<getNombreDeJoueurs();joueur++)
			{
				if(preneur!=joueur)
				{
					//Les cartes d'une couleur du chien (si il est vu) ne peuvent possedes que par le preneur ou etre ecartees
					for(Carte carte:getDistribution().derniereMain())
					{
						if(carte.couleur()==couleur&&m.get(joueur).contient(carte))
						{
							m.get(joueur).jouer(carte);
						}
					}
				}
				else if(plisFaits.get(0).getCartes().tailleCouleur((byte)1)>0)
				{/*Si le preneur a ecarte des atouts dans le chien alors les cartes autres que les atouts incluant l'Excuse et les rois ne peuvent pas etre possedees par le preneur*/
					for(byte valeur=1;valeur<13;valeur++)
					{
						if(m.get(preneur).contient(new CarteTarot(valeur,couleur)))
						{
							m.get(preneur).jouer(new CarteTarot(valeur,couleur));
						}
					}
				}
			}
		}
		/*Les cartes jouees dans le pli en cours ne peuvent pas (ou plus) etre possedees par les joueurs ni faire partie de l'ecart*/
		for(joueur=0;joueur<getNombreDeJoueurs();joueur++)
		{
			for(Carte carte:pliEnCours)
			{
				if(m.get(joueur).contient(carte))
				{
					m.get(joueur).jouer(carte);
				}
			}
		}
		for(Carte carte:pliEnCours)
		{
			if(m.lastElement().contient(carte))
			{
				m.lastElement().jouer(carte);
			}
		}
		if(pliEnCours.couleurDemandee()==couleur)
		{
			for(joueur=0;joueur<getNombreDeJoueurs();joueur++)
			{
				Carte carte_jouee=pliEnCours.carteDuJoueur(joueur, getNombreDeJoueurs(),null);
				if(carte_jouee!=null&&carte_jouee.couleur()!=couleur&&carte_jouee.couleur()!=0)
				{/*Si un joueur a joue une carte autre que l'Excuse et pas de la couleur demandee dans le pli en cours, alors il coupe ou se defausse*/
					m.get(joueur).supprimerCartes();
				}
			}
		}
		return m;
	}
	/**Retourne l'ensemble des cartes certainement possedees par les joueurs classees par couleur puis par joueurs
	 * @param cartesPossibles l'ensemble des cartes probablement possedees par les joueurs ou a l'ecart (visible uniquement
	 * pour un preneur ayant demande petite ou garde ou partiellement lorsque des atouts sont ecartes)
	 * Cet ensemble peut etre reduit apres appel de methode
	 * @return l'ensemble des cartes dont on connait par deduction la main*/
	public Vector<Vector<MainTarot>> cartesCertaines(Vector<Vector<MainTarot>> cartesPossibles)
	{
		Vector<Byte> joueurs_repartition_connue=new Vector<Byte>(),joueurs_repartition_connue_2=new Vector<Byte>(),joueurs_repartition_connue_memo=new Vector<Byte>();
		Vector<Byte> joueurs_repartition_inconnue=new Vector<Byte>();
		Vector<Vector<MainTarot>> cartes_certaines=new Vector<Vector<MainTarot>>();
		int nombre_d_apparition_carte=0;/*Indique le nombre de mains pour les cartes possibles ou apparait la carte*/
		for(byte couleur=0;couleur<6;couleur++)
		{
			cartes_certaines.addElement(new Vector<MainTarot>());
			for(byte joueur=0;joueur<getNombreDeJoueurs()+1;joueur++)
			{
				cartes_certaines.get(couleur).addElement(new MainTarot());
			}
		}
		int nombre_cartes_possibles_joueur=0;
		for(byte joueur=0;joueur<getNombreDeJoueurs()+1;joueur++)
		{
			nombre_cartes_possibles_joueur=0;
			for(byte couleur=0;couleur<6;couleur++)
			{
				nombre_cartes_possibles_joueur+=cartesPossibles.get(couleur).get(joueur).total();
			}
			if(nombre_cartes_possibles_joueur==getDistribution().main(joueur).total())
			{/*L'ensemble des cartes d'un joueur reellement possedees est inclus dans l'ensemble des cartes probablement possedees par ce joueur*/
				for(byte couleur=0;couleur<6;couleur++)
				{
					cartes_certaines.get(couleur).get(joueur).ajouterCartes(cartesPossibles.get(couleur).get(joueur));
				}
				joueurs_repartition_connue.addElement(joueur);
				joueurs_repartition_connue_memo.addElement(joueur);
			}
		}
		for(;!joueurs_repartition_connue.isEmpty();)
		{/*Tant qu'on arrive a deduire la repartition exacte des joueurs on boucle sur l'ensemble des joueurs dont la
		repartition vient juste d'etre connue pour eliminer les cartes impossibles d'etre possedees par les joueurs*/
			for(byte joueur:joueurs_repartition_connue)
			{
				for(byte joueur2=0;joueur2<getNombreDeJoueurs()+1;joueur2++)
				{
					if(!joueurs_repartition_connue_memo.contains(joueur2))
					{
						for(byte couleur=0;couleur<6;couleur++)
						{
							cartesPossibles.get(couleur).get(joueur2).supprimerCartes(cartes_certaines.get(couleur).get(joueur));
						}
					}
					nombre_cartes_possibles_joueur=0;
					for(byte couleur=0;couleur<6;couleur++)
					{
						nombre_cartes_possibles_joueur+=cartesPossibles.get(couleur).get(joueur2).total();
					}
					if(nombre_cartes_possibles_joueur==getDistribution().main(joueur2).total()&&!joueurs_repartition_connue_memo.contains(joueur2))
					{
						for(byte couleur=0;couleur<6;couleur++)
						{
							cartes_certaines.get(couleur).get(joueur2).supprimerCartes();
							cartes_certaines.get(couleur).get(joueur2).ajouterCartes(cartesPossibles.get(couleur).get(joueur2));
						}
						joueurs_repartition_connue_2.addElement(joueur2);
						joueurs_repartition_connue_memo.addElement(joueur2);
					}
				}
			}
			for(byte joueur=0;joueur<getNombreDeJoueurs()+1;joueur++)
			{
				if(!joueurs_repartition_connue_memo.contains(joueur))
				{
					joueurs_repartition_inconnue.addElement(joueur);
				}
			}
			for(byte joueur:joueurs_repartition_inconnue)
			{
				for(byte couleur=0;couleur<6;couleur++)
				{
					for(Carte carte:cartesPossibles.get(couleur).get(joueur))
					{
						nombre_d_apparition_carte=0;
						for(byte joueur2=0;joueur2<getNombreDeJoueurs()+1;joueur2++)
						{
							if(cartesPossibles.get(couleur).get(joueur2).contient(carte))
							{
								nombre_d_apparition_carte++;
							}
						}
						if(nombre_d_apparition_carte==1&&!cartes_certaines.get(couleur).get(joueur).contient(carte))
						{
							cartes_certaines.get(couleur).get(joueur).ajouter(carte);
						}
					}
				}
				nombre_cartes_possibles_joueur=0;
				for(byte couleur=0;couleur<6;couleur++)
				{
					nombre_cartes_possibles_joueur+=cartes_certaines.get(couleur).get(joueur).total();
				}
				if(nombre_cartes_possibles_joueur==getDistribution().main(joueur).total()&&!joueurs_repartition_connue_memo.contains(joueur))
				{
					for(byte couleur=0;couleur<6;couleur++)
					{
						cartesPossibles.get(couleur).get(joueur).supprimerCartes();
						cartesPossibles.get(couleur).get(joueur).ajouterCartes(cartes_certaines.get(couleur).get(joueur));
						cartesPossibles.get(couleur).get(joueur).trier();
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
		for(byte joueur=0;joueur<getNombreDeJoueurs()+1;joueur++)
		{
			if(!joueurs_repartition_connue_memo.contains(joueur))
			{
				joueurs_repartition_inconnue.addElement(joueur);
			}
		}
		for(byte joueur:joueurs_repartition_inconnue)
		{
			for(byte couleur=0;couleur<6;couleur++)
			{
				cartes_certaines.get(couleur).get(joueur).trier();
			}
		}
		return cartes_certaines;
	}
	/**Retourne vrai si le joueur ne peut pas jouer de l'atout
	 * sur demande d'atout ou sur demande de coupe de couleur sauf pli en cours*/
	private boolean defausseTarot(byte numero,Vector<Pli> unionPlis)
	{
		boolean coupe=false;//coupe retourne vrai si on sait que le joueur ne peut que jouer de l'atout sur des couleurs
		for(byte couleur=1;couleur<6;couleur++)
			coupe|=coupeTarot(couleur,numero,unionPlis);
		//coupe est vrai si et seulement si il existe au moins une coupe a une des couleurs
		if(!coupe)
			return false;
		if(coupeTarot((byte)1,numero,unionPlis))//Si le joueur ne joue pas d'atout sur demande d'atout
		{
			return true;
		}
		//Le joueur a deja joue une carte d'une autre couleur que celle demandee differente de l'atout
		byte minPli=3;
		byte numero_pli_petit=0;
		for(byte indice_pli=0;indice_pli<unionPlis.size();indice_pli++)
			if(unionPlis.get(indice_pli).total()<minPli)
			{
				minPli=(byte) unionPlis.get(indice_pli).total();
				numero_pli_petit=indice_pli;
			}
		Pli pliPetit=unionPlis.get(numero_pli_petit);
		for(int indice_pli=unionPlis.size()-1;indice_pli>-1;indice_pli--)
		{/*On effectue une boucle sur les plis faits par les joueurs en commencant par le plus recent(numero le plus eleve)*/
			Pli pli=unionPlis.get(indice_pli);
			if(pli.total()==getNombreDeJoueurs()-1||pli.total()==getNombreDeJoueurs())
			{
				byte couleur_demandee=pli.couleurDemandee();
				if(pli.carteDuJoueur(numero,getNombreDeJoueurs(),pliPetit).couleur()!=couleur_demandee&&pli.carteDuJoueur(numero,getNombreDeJoueurs(),pliPetit).couleur()>1)
					return true;
			}
		}
		return false;
	}
	/**Retourne vrai si le joueur ne peut pas jouer de l'atout ni fournir
	 * sur demande de la couleur "couleur" sauf pli en cours*/
	private boolean defausseTarot(byte numero,byte couleur_donnee,Vector<Pli> unionPlis)
	{
		boolean coupe=false;//coupe retourne vrai si on sait que le joueur ne peut que jouer de l'atout sur des couleurs
		for(byte couleur=1;couleur<6;couleur++)
			coupe|=coupeTarot(couleur,numero,unionPlis);
		//coupe est vrai si et seulement si il existe au moins une coupe a une des couleurs
		if(!coupe)
			return false;
		//Le joueur a deja joue une carte d'une autre couleur que celle demandee differente de l'atout
		byte minPli=3;
		byte numeroPli=0;
		for(byte b=0;b<unionPlis.size();b++)
			if(unionPlis.get(b).total()<minPli)
			{
				minPli=(byte) unionPlis.get(b).total();
				numeroPli=b;
			}
		Pli pliPetit=unionPlis.get(numeroPli);
		for(int b=unionPlis.size()-1;b>-1;b--)
		{
			Pli pli=unionPlis.get(b);
			if(pli.total()==getNombreDeJoueurs()-1||pli.total()==getNombreDeJoueurs())
			{
				if(couleur_donnee==pli.couleurDemandee())
					if(pli.carteDuJoueur(numero,getNombreDeJoueurs(),pliPetit).couleur()!=couleur_donnee&&pli.carteDuJoueur(numero,getNombreDeJoueurs(),pliPetit).couleur()>1)
						return true;
			}
		}
		return false;
	}
	/**Retourne vrai dans les cas suivants
	 * <ol>
	 * <li>Si couleur vaut 1(C'est la couleur de l'atout (Excuse exclue)), alors vrai est retourne lorsque le joueur a joue une couleur
	 * differente de l'atout sur entame atout ou sur une entame de couleur differente de l'atout en ayant fourni une carte
	 * autre que de l'atout et celle qui est demandee et l'Excuse sauf pli en cours</li>
	 * <li>Sinon vrai est retourne lorsque le joueur a joue un atout sur entame d'une couleur autre que de l'atout sauf pli en cours</li>
	 * </ol>*/
	private boolean coupeTarot(byte couleur,byte numero,Vector<Pli> unionPlis)
	{
		byte minPli=3;//Initialisation du minumum de pli
		byte numeroPli=0;
		for(byte b=0;b<unionPlis.size();b++)
			if(unionPlis.get(b).total()<minPli)
			{
				minPli=(byte) unionPlis.get(b).total();
				numeroPli=b;
			}
		Pli pliPetit=unionPlis.get(numeroPli);
		if(couleur>1)
		{
			for(int b=unionPlis.size()-1;b>-1;b--)
			{
				Pli pli=unionPlis.get(b);
				if(pli.total()==getNombreDeJoueurs()-1||pli.total()==getNombreDeJoueurs())
					if(pli.couleurDemandee()==couleur)
						//On ne cherche que les plis dont la couleur demande est couleur
						if(pli.carteDuJoueur(numero,getNombreDeJoueurs(),pliPetit).couleur()==1)
							return true;
			}
		}
		//Le joueur ne coupe pas la couleur passee en parametre a ce niveau si couleur > 1
		if(couleur==1)
		{
			for(int b=unionPlis.size()-1;b>-1;b--)
			{
				Pli pli=unionPlis.get(b);
				if(pli.total()==getNombreDeJoueurs()-1||pli.total()==getNombreDeJoueurs())
				{
					byte couleur_demandee=pli.couleurDemandee();
					if(couleur_demandee==couleur)
					{/*Si la couleur demandee est atout alors il suffit que le joueur n'ait pas joue de l'atout pour conclure qu'il ne possede pas d'atout
					sinon on verifie de plus que la couleur fournie par le joueur est une autre couleur que celle demandee*/
						if(pli.carteDuJoueur(numero,getNombreDeJoueurs(),pliPetit).couleur()>1)
							return true;
					}
					else
					{
						if(pli.carteDuJoueur(numero,getNombreDeJoueurs(),pliPetit).couleur()!=couleur_demandee&&pli.carteDuJoueur(numero,getNombreDeJoueurs(),pliPetit).couleur()>1)
							return true;
					}
				}
			}
		}
		return false;
	}
	private Carte carte_au_dessus_de_carte_virtuelle(CarteTarot carte_virtuelle,Vector<MainTarot> suites)
	{
		byte indice_suite_joue=-1;
		for(MainTarot suite:suites)
		{
			if(suite.carte(0).valeur()>carte_virtuelle.valeur())
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
	private Carte petite_figure_joue_dernier(Carte carte_la_plus_forte_virtuelle, Vector<MainTarot> suites)
	{
		byte indice_suite_joue=-1;
		for(MainTarot suite:suites)
		{
			if(suite.carte(0).valeur()>carte_la_plus_forte_virtuelle.valeur()&&suite.carte(0).valeur()>10)
			{
				indice_suite_joue++;
			}
			else
			{
				break;
			}
		}
		if(suites.size()>2)
		{
			if(suites.get(0).carte(0).valeur()==13&&suites.get(1).carte(0).valeur()==11)
			{
				return suites.get(0).carte(0);
			}
		}
		return suites.get(indice_suite_joue).carte(0);
	}
	private Carte petite_figure_joue_dernier_2(Vector<MainTarot> suites, Carte carte_la_plus_forte)
	{
		byte indice_suite_joue=-1;
		for(MainTarot suite:suites)
		{
			if(suite.carte(0).valeur()>10)
			{
				indice_suite_joue++;
			}
			else
			{
				break;
			}
		}
		if(suites.size()>2)
		{
			if(suites.get(0).carte(0).valeur()==13&&suites.get(1).carte(0).valeur()==11&&carte_la_plus_forte.valeur()<14)
			{
				return suites.get(0).carte(0);
			}
		}
		return suites.get(indice_suite_joue).carte(0);
	}
	public void ajouterUneCarteDansPliEnCours(Carte c)
	{
		pliEnCours.getCartes().ajouter(c);
	}
	/**A la fin d'un pli on ramasse les cartes
	 * et on les ajoute dans des tas*/
	public void ajouterPliEnCours()
	{
		byte nombre_joueurs=getNombreDeJoueurs();//nombre_joueurs jouant au tarot
		ramasseur=pliEnCours.getRamasseurTarot(nombre_joueurs);
		Vector<Pli> plis_attaque=getPlisAttaque();
		Vector<Pli> plis_defense=getPlisDefense();
		//On veut savoir si c'est le dernier tour
		int positionExcuse=pliEnCours.getCartes().position(new CarteTarot((byte)0));
		if(positionExcuse>-1)
		{
			if(getDistribution().main(entameur).estVide())
			{
				byte joueurLAyantPossede=(byte) ((positionExcuse+entameur)%nombre_joueurs);
				//Nombre de plis faits par les autres joueurs
				byte nombreDePlisAutres=0;
				if(preneur==-1)
				{
					for(byte joueur:autres_joueurs(joueurLAyantPossede))
					{
						nombreDePlisAutres+=plis.get(joueur).size();
					}
					if(nombreDePlisAutres>0)
					{
						plis.get(ramasseur).addElement(pliEnCours);
					}
					else
					{
						plis.get(joueurLAyantPossede).addElement(pliEnCours);
					}
				}
				else if(a_pour_defenseur(joueurLAyantPossede))
				{
					nombreDePlisAutres+=getPlisAttaque().size();
					//Le joueur est defenseur
					if(contrat.force()<4)
					{//Le chien appartient a l'attaque
						//Au dernier tour, ici la defense a effectue un pli de cartes, le nombre de cartes
						//de ce pli vaut le nombre de joueurs
						if(nombreDePlisAutres>1)
						{//Si la defense, avant le dernier tour n'avait pas fait de pli, et si l'Excuse est jouee
							//par la defense, elle ne permet pas de faire de pli
							if(a_pour_defenseur(ramasseur))
							{
								plis_defense.addElement(pliEnCours);
							}
							else
							{
								plis_attaque.addElement(pliEnCours);
							}
						}
						//Si la defense, avant le dernier tour n'avait pas fait de pli, et si l'Excuse est jouee
						//par l'attaque, elle permet non seulement de faire le pli mais aussi de realiser le grand chelem meme non demande
						else
						{
							plis_defense.addElement(pliEnCours);
						}
					}
					else if(nombreDePlisAutres>0)
					{
						if(a_pour_defenseur(ramasseur))
						{
							plis_defense.addElement(pliEnCours);
						}
						else
						{
							plis_attaque.addElement(pliEnCours);
						}
					}
					else
					{
						plis_defense.addElement(pliEnCours);
					}
				}
				else
				{
					nombreDePlisAutres+=getPlisDefense().size();
					if(contrat.force()<4)
					{
						if(nombreDePlisAutres>0)
						{
							if(a_pour_defenseur(ramasseur))
							{
								plis_defense.addElement(pliEnCours);
							}
							else
							{
								plis_attaque.addElement(pliEnCours);
							}
						}
						//Si la defense, avant le dernier tour n'avait pas fait de pli, et si l'Excuse est jouee
						//par l'attaque, elle permet non seulement de faire le pli mais aussi de realiser le grand chelem meme non demande
						else
						{
							plis_attaque.addElement(pliEnCours);
						}
					}
					//Au dernier tour, ici l'attaque a effectue un pli de cartes, le nombre de cartes
					//de ce pli vaut le nombre de joueurs, car le chien appartient a la defense
					else if(nombreDePlisAutres>1)
						//Si l'attaque, avant le dernier tour n'avait pas fait de pli, et si l'Excuse est jouee
						//par l'attaque, elle ne permet pas de faire de pli
					{
						if(a_pour_defenseur(ramasseur))
						{
							plis_defense.addElement(pliEnCours);
						}
						else
						{
							plis_attaque.addElement(pliEnCours);
						}
					}
					//Si la attaque, avant le dernier tour n'avait pas fait de pli, et si l'Excuse est jouee
					//par la defense, elle permet non seulement de faire le pli mais aussi de realiser le grand chelem meme non demande
					else
					{
						plis_attaque.addElement(pliEnCours);
					}
				}
			}
			else if(preneur==-1)
			{
				byte joueurLAyantPossede=(byte) ((positionExcuse+entameur)%nombre_joueurs);
				CarteTarot excuse=(CarteTarot) pliEnCours.getCartes().jouer(positionExcuse);
				MainTarot m=new MainTarot();
				m.ajouter(excuse);
				Pli pliExcuse=new Pli(m,joueurLAyantPossede);
				plis.get(joueurLAyantPossede).addElement(pliExcuse);
				plis.get(ramasseur).addElement(pliEnCours);
			}
			else
			{
				byte joueurLAyantPossede=(byte) ((positionExcuse+entameur)%nombre_joueurs);
				/*L'excuse avant le dernier tour vient dans les plis du joueur l'ayant joue
				 * si le joueur l'ayant joue fait equipe avec le ramasseur alors on laisse le pli tel quel
				 * sinon on cree un pli ne contenant qu'une seule carte qui est l'Excuse*/
				if(!meme_equipe(ramasseur, joueurLAyantPossede))
				{
					CarteTarot excuse=(CarteTarot) pliEnCours.getCartes().jouer(positionExcuse);
					MainTarot m=new MainTarot();
					m.ajouter(excuse);
					Pli pliExcuse=new Pli(m,joueurLAyantPossede);
					if(a_pour_defenseur(joueurLAyantPossede))
					{
						plis_defense.addElement(pliExcuse);
					}
					else
					{
						plis_attaque.addElement(pliExcuse);
					}
				}
				if(a_pour_defenseur(ramasseur))
				{
					plis_defense.addElement(pliEnCours);
				}
				else
				{
					plis_attaque.addElement(pliEnCours);
				}
			}
		}
		else if(preneur==-1)
		{
			plis.get(ramasseur).addElement(pliEnCours);
		}
		else
		{
			if(a_pour_defenseur(ramasseur))
			{
				plis_defense.addElement(pliEnCours);
			}
			else
			{
				plis_attaque.addElement(pliEnCours);
			}
		}
	}
	private boolean confiance(byte joueur,byte enjoueur)
	{
		return confiance.get(joueur).get(enjoueur);
	}
	public MainTarot cartesJouees(byte numero)
	{
		MainTarot m=new MainTarot();
		if(preneur==-1)
		{
			for(byte b=0;b<plis.size()-1;b++)
			{
				for(Pli pli:plis.get(b))
				{
					m.ajouterCartes(pli.getCartes());
				}
			}
		}
		else if(numero==preneur&&contrat.force()<3)
		{
			for(Vector<Pli> plis_equipe:plis)
			{
				for(Pli pli:plis_equipe)
				{
					m.ajouterCartes(pli.getCartes());
				}
			}
		}
		else if(contrat.force()<4)
		{
			for(Pli pli:getPlisAttaque())
			{
				if(pli.getNumero()>0)
				{
					m.ajouterCartes(pli.getCartes());
				}
			}
			for(Pli pli:getPlisDefense())
			{
				m.ajouterCartes(pli.getCartes());
			}
		}
		else
		{
			for(Pli pli:getPlisAttaque())
			{
				m.ajouterCartes(pli.getCartes());
			}
			for(Pli pli:getPlisDefense())
			{
				if(pli.getNumero()>0)
				{
					m.ajouterCartes(pli.getCartes());
				}
			}
		}
		return m;
	}
	private boolean preneurEtUnAutreJoueurFontTousLesPlis()
	{
		Vector<Byte> joueurs=new Vector<Byte>();
		for(Vector<Pli> plis_equipe:plis)
		{
			for(Pli pli:plis_equipe)
			{
				if(joueurs.isEmpty()||joueurs.size()==1)
				{
					joueurs.addElement(pli.getEntameur());
				}
			}
		}
		return joueurs.size()==2&&joueurs.contains(preneur);
	}
	private Vector<Byte> autres_joueurs(byte numero)
	{
		Vector<Byte> autres_joueurs=new Vector<Byte>();
		for(byte joueur=0;joueur<numero;joueur++)
		{
			autres_joueurs.addElement(joueur);
		}
		for(byte joueur=(byte)(numero+1);joueur<getNombreDeJoueurs();joueur++)
		{
			autres_joueurs.addElement(joueur);
		}
		return autres_joueurs;
	}
	/**Renvoie un entier 0 si joueur de non confiance qui va faire le pli et -1 sinon
	 * @param cartes_possibles l'ensemble des cartes probablement possedees par les joueurs
	 * @param cartes_certaines l'ensemble des cartes surement possedees par les joueurs
	 * @param ramasseur_virtuel le joueur, qui sans les cartes jouees par les derniers joueurs du pli est ramasseur
	 * @param carte_forte la carte qui est en train de dominer le pli
	 * @param joueurs_non_joue l'ensemble des joueurs n'ayant pas encore joue leur carte
	 * @param joueurs_de_confiance l'ensemble des joueurs de confiance
	 * @param adversaires l'ensemble des joueurs de non confiance
	 * @param numero le numero du joueur qui va jouer
	 * @param couleur_appelee la couleur appelee si elle existe -1 sinon
	 * @param carte_appelee_jouee une valeur booleenne vrai si et seulement si la carte appelee est jouee*/
	private byte equipe_qui_va_faire_pli(Vector<Vector<MainTarot>> cartes_possibles,Vector<Vector<MainTarot>> cartes_certaines,byte ramasseur_virtuel,CarteTarot carte_forte,Vector<Byte> joueurs_non_joue,Vector<Byte> adversaires,byte numero)
	{
		byte couleur_demandee=pliEnCours.couleurDemandee();
		boolean ramasseur_est_determine=true;
		boolean ramasseur_virtuel_meme_equipe_que_certain=false;
		boolean joueur_bat_adversaire=true;
		Vector<Byte> adversaires_non_joue=new Vector<Byte>(adversaires);
		Vector<Byte> joueurs_joue=new Vector<Byte>();
		adversaires_non_joue.retainAll(joueurs_joue);
		for(byte joueur=0;joueur<getNombreDeJoueurs();joueur++)
		{
			if(!joueurs_non_joue.contains(joueur))
			{
				joueurs_joue.addElement(joueur);
			}
		}
		if(carte_forte.couleur()==1&&couleur_demandee>1)
		{/*Le pli est coupe*/
			if(!cartes_certaines.get(couleur_demandee).get(numero).estVide()||cartes_certaines.get(1).get(numero).estVide()||cartes_certaines.get(1).get(numero).carte(0).valeur()<carte_forte.valeur())
			{/*Le joueur numero ne peut pas prendre la main*/
				/*ramasseur_virtuel n'est pas un joueur de confiance pour le joueur numero*/
				return 0;
			}/*Fin !cartes_certaines.get(couleur_demandee).get(numero).estVide()||cartes_certaines.get(1).get(numero).estVide()||cartes_certaines.get(1).get(numero).carte(0).getValeur()<carte_forte.getValeur()
 			(fin test de possibilite pour le joueur numero de prendre le pli)*/
			/*Le joueur numero peut prendre la main*/
			ramasseur_est_determine=false;
			for(byte joueur:adversaires_non_joue)
			{/*On cherche les joueurs de non confiance battant de maniere certaine les joueurs de confiance n'ayant pas joue ou possedant des cartes que les joueurs ayant joue n'ont pas ainsi que les joueurs de non confiance n'ayant pas joue*/
				joueur_bat_adversaire=true;
				if(!cartes_certaines.get(1).get(joueur).estVide()&&cartes_possibles.get(couleur_demandee).get(joueur).estVide())
				{
					joueur_bat_adversaire&=cartes_certaines.get(1).get(joueur).carte(0).valeur()>cartes_certaines.get(1).get(numero).carte(0).valeur();
					ramasseur_est_determine|=joueur_bat_adversaire;
				}
			}
			if(ramasseur_est_determine)
				return 0;
			for(byte joueur:adversaires_non_joue)
			{/*On cherche les joueurs de confiance battant de maniere certaine les joueurs de non confiance n'ayant pas joue ou possedant des cartes que les joueurs ayant joue n'ont pas ainsi que les joueurs de non confiance n'ayant pas joue*/
				joueur_bat_adversaire=true;
				if(!cartes_possibles.get(1).get(joueur).estVide()&&cartes_possibles.get(couleur_demandee).get(joueur).estVide())
				{
					for(byte joueur2:joueurs_joue)
					{
						ramasseur_virtuel_meme_equipe_que_certain=cartes_possibles.get(1).get(joueur2).estVide();
						if(!ramasseur_virtuel_meme_equipe_que_certain)
						{
							ramasseur_virtuel_meme_equipe_que_certain|=cartes_possibles.get(1).get(joueur).carte(0).valeur()>cartes_possibles.get(1).get(joueur2).carte(0).valeur();
						}
						joueur_bat_adversaire&=ramasseur_virtuel_meme_equipe_que_certain;
					}
					joueur_bat_adversaire&=cartes_possibles.get(1).get(joueur).carte(0).valeur()>cartes_certaines.get(1).get(numero).carte(0).valeur();
					ramasseur_est_determine|=joueur_bat_adversaire;
				}
			}
			if(ramasseur_est_determine)
				return 0;
			return-1;
		}
		if(carte_forte.couleur()==couleur_demandee&&couleur_demandee>1)
		{/*La couleur demandee n'est pas de l'atout et le pli n'est pas coupe*/
			if(!cartes_possibles.get(couleur_demandee).get(numero).estVide()&&cartes_possibles.get(couleur_demandee).get(numero).carte(0).valeur()>carte_forte.valeur())
			{/*Si le joueur numero peut prendre la main sans couper*/
				ramasseur_est_determine=false;
				for(byte joueur:adversaires_non_joue)
				{
					joueur_bat_adversaire=!cartes_certaines.get(couleur_demandee).get(joueur).estVide();
					if(joueur_bat_adversaire)
					{
						joueur_bat_adversaire&=cartes_certaines.get(couleur_demandee).get(joueur).carte(0).valeur()>cartes_possibles.get(couleur_demandee).get(numero).carte(0).valeur();
					}
					else
					{
						joueur_bat_adversaire=cartes_possibles.get(couleur_demandee).get(joueur).estVide()&&!cartes_certaines.get(1).get(joueur).estVide();
					}
					ramasseur_est_determine|=joueur_bat_adversaire;
				}
				/*Si un adversaire, n'ayant pas joue, coupe de maniere certaine le pli ou possede une carte de la couleur demandee certainement superieure a celle du joueur numero*/
				if(ramasseur_est_determine)
				{
					return 0;
				}
				for(byte joueur:adversaires_non_joue)
				{
					joueur_bat_adversaire=true;
					if(!cartes_certaines.get(couleur_demandee).get(joueur).estVide())
					{
						for(byte joueur2:joueurs_joue)
						{
							joueur_bat_adversaire=!cartes_possibles.get(couleur_demandee).get(joueur2).estVide();
							if(joueur_bat_adversaire)
							{
								joueur_bat_adversaire&=cartes_possibles.get(couleur_demandee).get(joueur).carte(0).valeur()>cartes_possibles.get(couleur_demandee).get(joueur2).carte(0).valeur();
							}
						}
						ramasseur_est_determine|=joueur_bat_adversaire;
					}
				}
				if(ramasseur_est_determine)
				{
					return 0;
				}
				return-1;
			}
			if(peut_couper(couleur_demandee,numero,cartes_possibles))
			{/*Si le joueur numero peut prendre la main en coupant*/
				ramasseur_est_determine=false;
				for(byte joueur:adversaires_non_joue)
				{/*On cherche les joueurs de non confiance battant de maniere certaine les joueurs de confiance n'ayant pas joue ou possedant des cartes que les joueurs ayant joue n'ont pas ainsi que les joueurs de non confiance n'ayant pas joue*/
					if(!cartes_certaines.get(1).get(joueur).estVide()&&cartes_possibles.get(couleur_demandee).get(joueur).estVide())
					{
						ramasseur_est_determine|=cartes_certaines.get(1).get(joueur).carte(0).valeur()>cartes_certaines.get(1).get(numero).carte(0).valeur();
					}
				}
				if(ramasseur_est_determine)
					return 0;
				for(byte joueur:adversaires_non_joue)
				{/*On cherche les joueurs de confiance battant de maniere certaine les joueurs de non confiance n'ayant pas joue ou possedant des cartes que les joueurs ayant joue n'ont pas ainsi que les joueurs de non confiance n'ayant pas joue*/
					if(!cartes_possibles.get(1).get(joueur).estVide()&&cartes_possibles.get(couleur_demandee).get(joueur).estVide())
					{
						for(byte joueur2:joueurs_joue)
						{
							ramasseur_virtuel_meme_equipe_que_certain=cartes_possibles.get(1).get(joueur2).estVide();
							if(!ramasseur_virtuel_meme_equipe_que_certain)
							{
								ramasseur_virtuel_meme_equipe_que_certain=cartes_possibles.get(1).get(joueur).carte(0).valeur()>cartes_possibles.get(1).get(joueur2).carte(0).valeur();
							}
							joueur_bat_adversaire=ramasseur_virtuel_meme_equipe_que_certain;
						}
						joueur_bat_adversaire&=cartes_possibles.get(1).get(joueur).carte(0).valeur()>cartes_certaines.get(1).get(numero).carte(0).valeur();
						ramasseur_est_determine|=joueur_bat_adversaire;
					}
				}
				if(ramasseur_est_determine)
					return 0;
				return-1;
			}
			/*Maintenant le ramasseur virtuel n'est pas un joueur de confiance*/
			return 0;
		}
		/*Le pli n'est pas coupe et la couleur demandee est l'atout*/
		if(cartes_certaines.get(1).get(numero).estVide()||cartes_certaines.get(1).get(numero).carte(0).valeur()<carte_forte.valeur())
		{
			/*ramasseur_virtuel n'est pas un joueur de confiance pour le joueur numero*/
			return 0;
			/*Fin joueur_de_confiance.contains(ramasseur_virtuel)*/
		}/*Fin !cartes_certaines.get(couleur_demandee).get(numero).estVide()||cartes_certaines.get(1).get(numero).estVide()||cartes_certaines.get(1).get(numero).carte(0).getValeur()<carte_forte.getValeur()
			(fin test de possibilite pour le joueur numero de prendre le pli)*/
		for(byte joueur:adversaires_non_joue)
		{/*On cherche les joueurs de non confiance battant de maniere certaine les joueurs de confiance n'ayant pas joue ou possedant des cartes que les joueurs ayant joue n'ont pas ainsi que les joueurs de non confiance n'ayant pas joue*/
			joueur_bat_adversaire=true;
			if(!cartes_certaines.get(1).get(joueur).estVide())
			{
				joueur_bat_adversaire&=cartes_certaines.get(1).get(joueur).carte(0).valeur()>cartes_certaines.get(1).get(numero).carte(0).valeur();
				ramasseur_est_determine|=joueur_bat_adversaire;
			}
		}
		if(ramasseur_est_determine)
			return 0;
		for(byte joueur:adversaires_non_joue)
		{/*On cherche les joueurs de confiance battant de maniere certaine les joueurs de non confiance n'ayant pas joue ou possedant des cartes que les joueurs ayant joue n'ont pas ainsi que les joueurs de non confiance n'ayant pas joue*/
			joueur_bat_adversaire=true;
			if(!cartes_possibles.get(1).get(joueur).estVide())
			{
				for(byte joueur2:joueurs_joue)
				{
					ramasseur_virtuel_meme_equipe_que_certain=cartes_possibles.get(1).get(joueur2).estVide();
					if(!ramasseur_virtuel_meme_equipe_que_certain)
					{
						ramasseur_virtuel_meme_equipe_que_certain=cartes_possibles.get(1).get(joueur).carte(0).valeur()>cartes_possibles.get(1).get(joueur2).carte(0).valeur();
					}
					joueur_bat_adversaire&=ramasseur_virtuel_meme_equipe_que_certain;
				}
				joueur_bat_adversaire&=cartes_possibles.get(1).get(joueur).carte(0).valeur()>cartes_certaines.get(1).get(numero).carte(0).valeur();
				ramasseur_est_determine|=joueur_bat_adversaire;
			}
		}
		if(ramasseur_est_determine)
			return 0;
		return-1;
	}
	/**Renvoie un entier 0 si joueur de non confiance qui va faire le pli 1 si joueur de confiance va faire le pli et -1 sinon
	 * @param cartes_possibles l'ensemble des cartes probablement possedees par les joueurs
	 * @param cartes_certaines l'ensemble des cartes surement possedees par les joueurs
	 * @param ramasseur_virtuel le joueur, qui sans les cartes jouees par les derniers joueurs du pli est ramasseur
	 * @param carte_forte la carte qui est en train de dominer le pli
	 * @param joueurs_non_joue l'ensemble des joueurs n'ayant pas encore joue leur carte
	 * @param joueurs_confiance l'ensemble des joueurs de confiance
	 * @param joueurs_non_confiance l'ensemble des joueurs de non confiance
	 * @param numero le numero du joueur qui va jouer
	 * @param couleur_appelee la couleur appelee si elle existe -1 sinon
	 * @param carte_appelee_jouee une valeur booleenne vrai si et seulement si la carte appelee est jouee*/
	private byte equipe_qui_va_faire_pli(Vector<Vector<MainTarot>> cartes_possibles,Vector<Vector<MainTarot>> cartes_certaines,byte ramasseur_virtuel,CarteTarot carte_forte,Vector<Byte> joueurs_non_joue,Vector<Byte> joueurs_confiance,Vector<Byte> joueurs_non_confiance,byte numero, byte couleur_appelee, boolean carte_appelee_jouee)
	{
		byte couleur_demandee=pliEnCours.couleurDemandee();
		boolean ramasseur_virtuel_egal_certain=false;
		Vector<Byte> joueurs_non_confiance_non_joue=new Vector<Byte>(joueurs_non_joue);
		Vector<Byte> joueurs_confiance_non_joue=new Vector<Byte>(joueurs_non_joue);
		joueurs_non_confiance_non_joue.retainAll(joueurs_non_confiance);
		joueurs_confiance_non_joue.retainAll(joueurs_confiance);
		Vector<Byte> joueurs_joue=new Vector<Byte>();
		for(byte joueur=0;joueur<getNombreDeJoueurs();joueur++)
		{
			if(!joueurs_non_joue.contains(joueur))
			{
				joueurs_joue.addElement(joueur);
			}
		}
		if(carte_forte.couleur()==1&&couleur_demandee>1)
		{/*Le pli est coupe*/
			if(!cartes_certaines.get(couleur_demandee).get(numero).estVide()||cartes_certaines.get(1).get(numero).estVide()||cartes_certaines.get(1).get(numero).carte(0).valeur()<carte_forte.valeur())
			{/*Le joueur numero ne peut pas prendre la main*/
				if(joueurs_confiance.contains(ramasseur_virtuel))
				{
					if(couleur_demandee==couleur_appelee&&!carte_appelee_jouee&&a_pour_defenseur(numero))
					{
						for(byte indice_joueur=0;indice_joueur<joueurs_non_confiance_non_joue.size();)
							if(joueurs_non_confiance_non_joue.get(indice_joueur)!=preneur)
								joueurs_non_confiance_non_joue.removeElementAt(indice_joueur);
							else
								indice_joueur++;
					}
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
					/*On cherche les joueurs de non confiance battant de maniere certaine les joueurs de confiance n'ayant pas joue ou possedant des cartes que les joueurs ayant joue n'ont pas ainsi que les joueurs de non confiance n'ayant pas joue*/
					if(existe_joueur_adv_ram_bat_ptm_sur(joueurs_confiance_non_joue, joueurs_non_confiance_non_joue, joueurs_joue, couleur_demandee, cartes_possibles, cartes_certaines))
					{
						return 0;
					}
					return-1;
				}
				/*ramasseur_virtuel n'est pas un joueur de confiance pour le joueur numero*/
				if(couleur_demandee==couleur_appelee&&!carte_appelee_jouee&&numero==preneur)
				{
					for(byte indice_joueur=0;indice_joueur<joueurs_confiance_non_joue.size();)
						if(joueurs_confiance_non_joue.get(indice_joueur)==appele)
							joueurs_confiance_non_joue.removeElementAt(indice_joueur);
						else
							indice_joueur++;
				}
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
		if(carte_forte.couleur()==couleur_demandee&&couleur_demandee>1)
		{/*La couleur demandee n'est pas de l'atout et le pli n'est pas coupe*/
			ramasseur_virtuel_egal_certain=false;
			for(byte joueur:joueurs_confiance_non_joue)
			{
				ramasseur_virtuel_egal_certain|=va_couper(couleur_demandee, joueur, cartes_possibles, cartes_certaines);
			}
			if(ramasseur_virtuel_egal_certain)
			{/*Si un joueur de confiance n ayant pas joue va surement couper le pli*/
				ramasseur_virtuel_egal_certain=true;
				for(byte joueur:joueurs_non_confiance_non_joue)
				{
					ramasseur_virtuel_egal_certain&=ne_peut_couper(couleur_demandee, joueur, cartes_possibles, cartes_certaines);
				}
				if(ramasseur_virtuel_egal_certain)
				{/*Si aucun joueur de non confiance n ayant pas joue ne va couper le pli*/
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
			{/*Si un joueur de non confiance n ayant pas joue va surement couper le pli*/
				for(byte joueur:joueurs_confiance_non_joue)
				{
					ramasseur_virtuel_egal_certain&=ne_peut_couper(couleur_demandee, joueur, cartes_possibles, cartes_certaines);
				}
				if(ramasseur_virtuel_egal_certain)
				{/*Si aucun joueur de confiance n ayant pas joue ne va couper le pli*/
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
			if(!cartes_possibles.get(couleur_demandee).get(numero).estVide()&&cartes_possibles.get(couleur_demandee).get(numero).carte(0).valeur()>carte_forte.valeur())
			{/*Si le joueur numero peut prendre la main sans couper*/
				/*On ne sait pas si un joueur n'ayant pas joue va couper le pli ou non*/
				if(joueurs_confiance.contains(ramasseur_virtuel))
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
				/*On cherche les joueurs de non confiance battant de maniere certaine les joueurs de confiance n'ayant pas joue ou possedant des cartes que les joueurs ayant joue n'ont pas ainsi que les joueurs de non confiance n'ayant pas joue*/
				if(existe_joueur_bat_ptm_num(joueurs_confiance_non_joue, joueurs_non_confiance_non_joue, joueurs_joue, numero, couleur_demandee, cartes_possibles, cartes_certaines))
				{
					return 0;
				}
				return-1;
			}
			/*Le joueur numero ne peut pas prendre la main*/
			if(joueurs_confiance.contains(ramasseur_virtuel))
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
		if(cartes_certaines.get(1).get(numero).estVide()||cartes_certaines.get(1).get(numero).carte(0).valeur()<carte_forte.valeur())
		{/*Si le joueur numero ne peut pas prendre la main sur demande d'atout*/
			if(joueurs_confiance.contains(ramasseur_virtuel))
			{
				/*Si le ramasseur virtuel (de confiance, ici) domine certainement les joueurs de non confiance n'ayant pas joue*/
				if(ramasseur_bat_adv_demat(joueurs_non_confiance_non_joue, carte_forte, cartes_possibles))
				{
					return 1;
				}
				/*On cherche les joueurs de confiance battant de maniere certaine les joueurs de non confiance n'ayant pas joue ou possedant des cartes que les joueurs ayant joue n'ont pas ainsi que les joueurs de non confiance n'ayant pas joue*/
				if(existe_jou_bat_adv_demat(joueurs_non_confiance_non_joue, joueurs_confiance_non_joue, cartes_possibles, cartes_certaines))
				{
					return 1;
				}
				/*On cherche les joueurs de confiance battant de maniere certaine les joueurs de non confiance n'ayant pas joue ou possedant des cartes que les joueurs ayant joue n'ont pas ainsi que les joueurs de non confiance n'ayant pas joue*/
				if(existe_jou_bat_ptm_demat(joueurs_non_confiance_non_joue,joueurs_confiance_non_joue,joueurs_joue,cartes_possibles))
				{
					return 1;
				}
				/*On cherche les joueurs de non confiance battant de maniere certaine les joueurs de confiance n'ayant pas joue ou possedant des cartes que les joueurs ayant joue n'ont pas ainsi que les joueurs de non confiance n'ayant pas joue*/
				if(existe_jou_bat_adv_sur_demat(joueurs_confiance_non_joue, joueurs_non_confiance_non_joue, carte_forte, cartes_possibles, cartes_certaines))
				{
					return 0;
				}
				/*On cherche les joueurs de non confiance battant de maniere certaine les joueurs de confiance n'ayant pas joue ou possedant des cartes que les joueurs ayant joue n'ont pas ainsi que les joueurs de non confiance n'ayant pas joue*/
				if(existe_jou_bat_ptm_sur_demat(joueurs_confiance_non_joue, joueurs_non_confiance_non_joue, joueurs_joue, carte_forte, cartes_possibles))
				{
					return 0;
				}
				return-1;
			}
			/*ramasseur_virtuel n'est pas un joueur de confiance pour le joueur numero*/
			/*Si le ramasseur virtuel (de non confiance, ici) domine certainement les joueurs de non confiance n'ayant pas joue*/
			if(ramasseur_bat_adv_demat(joueurs_confiance_non_joue, carte_forte, cartes_possibles))
			{
				return 0;
			}
			/*On cherche les joueurs de non confiance battant de maniere certaine les joueurs de confiance n'ayant pas joue ou possedant des cartes que les joueurs ayant joue n'ont pas ainsi que les joueurs de non confiance n'ayant pas joue*/
			if(existe_jou_bat_adv_demat(joueurs_confiance_non_joue, joueurs_non_confiance_non_joue, cartes_possibles, cartes_certaines))
			{
				return 0;
			}
			/*On cherche les joueurs de non confiance battant de maniere certaine les joueurs de confiance n'ayant pas joue ou possedant des cartes que les joueurs ayant joue n'ont pas ainsi que les joueurs de non confiance n'ayant pas joue*/
			if(existe_jou_bat_ptm_demat(joueurs_confiance_non_joue, joueurs_non_confiance_non_joue, joueurs_joue, cartes_possibles))
			{
				return 0;
			}
			/*On cherche les joueurs de confiance battant de maniere certaine les joueurs de non confiance n'ayant pas joue ou possedant des cartes que les joueurs ayant joue n'ont pas ainsi que les joueurs de non confiance n'ayant pas joue*/
			if(existe_jou_bat_adv_sur_demat(joueurs_non_confiance_non_joue, joueurs_confiance_non_joue, carte_forte, cartes_possibles, cartes_certaines))
			{
				return 1;
			}
			/*On cherche les joueurs de confiance battant de maniere certaine les joueurs de non confiance n'ayant pas joue ou possedant des cartes que les joueurs ayant joue n'ont pas ainsi que les joueurs de non confiance n'ayant pas joue*/
			if(existe_jou_bat_ptm_sur_demat(joueurs_non_confiance_non_joue, joueurs_confiance_non_joue, joueurs_joue, carte_forte, cartes_possibles))
			{
				return 1;
			}
			return-1;
			/*Fin joueur_de_confiance.contains(ramasseur_virtuel)*/
		}/*Fin !cartes_certaines.get(couleur_demandee).get(numero).estVide()||cartes_certaines.get(1).get(numero).estVide()||cartes_certaines.get(1).get(numero).carte(0).getValeur()<carte_forte.getValeur()
			(fin test de possibilite pour le joueur numero de prendre le pli)*/
		/*Le joueur numero peut prendre la main en utilisant un atout sur demande d'atout*/
		/*On cherche les joueurs de confiance battant de maniere certaine les joueurs de non confiance n'ayant pas joue ou possedant des cartes que les joueurs ayant joue n'ont pas ainsi que les joueurs de non confiance n'ayant pas joue*/
		if(existe_jou_bat_adv_num_demat(joueurs_non_confiance_non_joue,joueurs_confiance_non_joue,numero,cartes_possibles,cartes_certaines))
		{
			return 1;
		}
		/*On cherche les joueurs de confiance battant de maniere certaine les joueurs de non confiance n'ayant pas joue ou possedant des cartes que les joueurs ayant joue n'ont pas ainsi que les joueurs de non confiance n'ayant pas joue*/
		if(existe_jou_bat_ptm_num_demat(joueurs_non_confiance_non_joue,joueurs_confiance_non_joue,joueurs_joue,numero,cartes_possibles))
		{
			return 1;
		}
		/*On cherche les joueurs de non confiance battant de maniere certaine les joueurs de confiance n'ayant pas joue ou possedant des cartes que les joueurs ayant joue n'ont pas ainsi que les joueurs de non confiance n'ayant pas joue*/
		if(existe_jou_bat_adv_num_demat(joueurs_confiance_non_joue,joueurs_non_confiance_non_joue, numero, cartes_possibles, cartes_certaines))
		{
			return 0;
		}
		/*On cherche les joueurs de non confiance battant de maniere certaine les joueurs de confiance n'ayant pas joue ou possedant des cartes que les joueurs ayant joue n'ont pas ainsi que les joueurs de non confiance n'ayant pas joue*/
		if(existe_jou_bat_ptm_num_demat(joueurs_confiance_non_joue,joueurs_non_confiance_non_joue,joueurs_joue,numero,cartes_possibles))
		{
			return 0;
		}
		return-1;
	}
	private boolean existe_jou_bat_ptm_num_demat(Vector<Byte> equipe_a_battre,Vector<Byte> equipe_dom,Vector<Byte> joueurs_joue,byte numero,Vector<Vector<MainTarot>> cartes_possibles)
	{
		boolean ramasseur_deter=false;
		boolean ramasseur_virtuel_egal_certain=false;
		for(byte joueur:equipe_dom)
		{
			boolean joueur_bat_adversaire=true;
			if(!cartes_possibles.get(1).get(joueur).estVide())
			{
				for(byte joueur2:equipe_a_battre)
				{
					ramasseur_virtuel_egal_certain=cartes_possibles.get(1).get(joueur2).estVide();
					if(!ramasseur_virtuel_egal_certain)
					{
						ramasseur_virtuel_egal_certain|=cartes_possibles.get(1).get(joueur).carte(0).valeur()>cartes_possibles.get(1).get(joueur2).carte(0).valeur();
					}
					joueur_bat_adversaire&=ramasseur_virtuel_egal_certain;
				}
				for(byte joueur2:joueurs_joue)
				{
					ramasseur_virtuel_egal_certain=cartes_possibles.get(1).get(joueur2).estVide();
					if(!ramasseur_virtuel_egal_certain)
					{
						ramasseur_virtuel_egal_certain|=cartes_possibles.get(1).get(joueur).carte(0).valeur()>cartes_possibles.get(1).get(joueur2).carte(0).valeur();
					}
					joueur_bat_adversaire&=ramasseur_virtuel_egal_certain;
				}
				joueur_bat_adversaire&=cartes_possibles.get(1).get(joueur).carte(0).valeur()>cartes_possibles.get(1).get(numero).carte(0).valeur();
				ramasseur_deter|=joueur_bat_adversaire;
			}
		}
		return ramasseur_deter;
	}
	private boolean existe_jou_bat_adv_num_demat(Vector<Byte> equipe_a_battre,Vector<Byte> equipe_dom,byte numero,Vector<Vector<MainTarot>> cartes_possibles,Vector<Vector<MainTarot>> cartes_certaines)
	{
		boolean ramasseur_deter=false;
		boolean ramasseur_virtuel_egal_certain=false;
		for(byte joueur:equipe_dom)
		{
			boolean joueur_bat_adversaire=true;
			if(!cartes_certaines.get(1).get(joueur).estVide())
			{
				for(byte joueur2:equipe_a_battre)
				{
					ramasseur_virtuel_egal_certain=cartes_possibles.get(1).get(joueur2).estVide();
					if(!ramasseur_virtuel_egal_certain)
					{
						ramasseur_virtuel_egal_certain|=cartes_certaines.get(1).get(joueur).carte(0).valeur()>cartes_possibles.get(1).get(joueur2).carte(0).valeur();
					}
					joueur_bat_adversaire&=ramasseur_virtuel_egal_certain;
				}
				joueur_bat_adversaire&=cartes_certaines.get(1).get(joueur).carte(0).valeur()>cartes_possibles.get(1).get(numero).carte(0).valeur();
				ramasseur_deter|=joueur_bat_adversaire;
			}
		}
		return ramasseur_deter;
	}
	private boolean existe_jou_bat_ptm_sur_demat(Vector<Byte> equipe_a_battre,Vector<Byte> equipe_dom,Vector<Byte> joueurs_joue,Carte carte_forte,Vector<Vector<MainTarot>> cartes_possibles)
	{
		boolean ramasseur_deter=false;
		boolean ramasseur_virtuel_egal_certain=false;
		for(byte joueur:equipe_dom)
		{
			boolean joueur_bat_adversaire=true;
			if(!cartes_possibles.get(1).get(joueur).estVide())
			{
				for(byte joueur2:equipe_a_battre)
				{
					ramasseur_virtuel_egal_certain=cartes_possibles.get(1).get(joueur2).estVide();
					if(!ramasseur_virtuel_egal_certain)
					{
						ramasseur_virtuel_egal_certain|=cartes_possibles.get(1).get(joueur).carte(0).valeur()>cartes_possibles.get(1).get(joueur2).carte(0).valeur();
					}
					joueur_bat_adversaire&=ramasseur_virtuel_egal_certain;
				}
				for(byte joueur2:joueurs_joue)
				{
					ramasseur_virtuel_egal_certain=cartes_possibles.get(1).get(joueur2).estVide();
					if(!ramasseur_virtuel_egal_certain)
					{
						ramasseur_virtuel_egal_certain|=cartes_possibles.get(1).get(joueur).carte(0).valeur()>cartes_possibles.get(1).get(joueur2).carte(0).valeur();
					}
					joueur_bat_adversaire&=ramasseur_virtuel_egal_certain;
				}
				joueur_bat_adversaire&=cartes_possibles.get(1).get(joueur).carte(0).valeur()>carte_forte.valeur();
				ramasseur_deter|=joueur_bat_adversaire;
			}
		}
		return ramasseur_deter;
	}
	private boolean existe_jou_bat_adv_sur_demat(Vector<Byte> equipe_a_battre,Vector<Byte> equipe_dom,Carte carte_forte,Vector<Vector<MainTarot>> cartes_possibles,Vector<Vector<MainTarot>> cartes_certaines)
	{
		boolean ramasseur_deter=false;
		boolean ramasseur_virtuel_egal_certain=false;
		for(byte joueur:equipe_dom)
		{
			boolean joueur_bat_adversaire=true;
			if(!cartes_certaines.get(1).get(joueur).estVide())
			{
				for(byte joueur2:equipe_a_battre)
				{
					ramasseur_virtuel_egal_certain=cartes_possibles.get(1).get(joueur2).estVide();
					if(!ramasseur_virtuel_egal_certain)
					{
						ramasseur_virtuel_egal_certain|=cartes_certaines.get(1).get(joueur).carte(0).valeur()>cartes_possibles.get(1).get(joueur2).carte(0).valeur();
					}
					joueur_bat_adversaire&=ramasseur_virtuel_egal_certain;
				}
				joueur_bat_adversaire&=cartes_certaines.get(1).get(joueur).carte(0).valeur()>carte_forte.valeur();
				ramasseur_deter|=joueur_bat_adversaire;
			}
		}
		return ramasseur_deter;
	}
	private boolean existe_jou_bat_ptm_demat(Vector<Byte> equipe_a_battre,Vector<Byte> equipe_dom,Vector<Byte> joueurs_joue,Vector<Vector<MainTarot>> cartes_possibles)
	{
		boolean ramasseur_deter=false;
		boolean ramasseur_virtuel_egal_certain=false;
		for(byte joueur:equipe_dom)
		{
			boolean joueur_bat_adversaire=true;
			if(!cartes_possibles.get(1).get(joueur).estVide())
			{
				for(byte joueur2:equipe_a_battre)
				{
					ramasseur_virtuel_egal_certain=cartes_possibles.get(1).get(joueur2).estVide();
					if(!ramasseur_virtuel_egal_certain)
					{
						ramasseur_virtuel_egal_certain|=cartes_possibles.get(1).get(joueur).carte(0).valeur()>cartes_possibles.get(1).get(joueur2).carte(0).valeur();
					}
					joueur_bat_adversaire&=ramasseur_virtuel_egal_certain;
				}
				for(byte joueur2:joueurs_joue)
				{
					ramasseur_virtuel_egal_certain=cartes_possibles.get(1).get(joueur2).estVide();
					if(!ramasseur_virtuel_egal_certain)
					{
						ramasseur_virtuel_egal_certain|=cartes_possibles.get(1).get(joueur).carte(0).valeur()>cartes_possibles.get(1).get(joueur2).carte(0).valeur();
					}
					joueur_bat_adversaire&=ramasseur_virtuel_egal_certain;
				}
				ramasseur_deter|=joueur_bat_adversaire;
			}
		}
		return ramasseur_deter;
	}
	private boolean existe_jou_bat_adv_demat(Vector<Byte> equipe_a_battre,Vector<Byte> equipe_dom,Vector<Vector<MainTarot>> cartes_possibles,Vector<Vector<MainTarot>> cartes_certaines)
	{
		boolean ramasseur_deter=false;
		boolean ramasseur_virtuel_egal_certain=false;
		for(byte joueur:equipe_dom)
		{
			boolean joueur_bat_adversaire=true;
			if(!cartes_certaines.get(1).get(joueur).estVide())
			{
				for(byte joueur2:equipe_a_battre)
				{
					ramasseur_virtuel_egal_certain=cartes_possibles.get(1).get(joueur2).estVide();
					if(!ramasseur_virtuel_egal_certain)
					{
						ramasseur_virtuel_egal_certain|=cartes_certaines.get(1).get(joueur).carte(0).valeur()>cartes_possibles.get(1).get(joueur2).carte(0).valeur();
					}
					joueur_bat_adversaire&=ramasseur_virtuel_egal_certain;
				}
				ramasseur_deter|=joueur_bat_adversaire;
			}
		}
		return ramasseur_deter;
	}
	private boolean ramasseur_bat_adv_demat(Vector<Byte> equipe_a_battre,Carte carte_forte,Vector<Vector<MainTarot>> cartes_possibles)
	{
		boolean ramasseur_deter=true;
		boolean ramasseur_virtuel_egal_certain=false;
		for(byte joueur:equipe_a_battre)
		{
			ramasseur_virtuel_egal_certain=cartes_possibles.get(1).get(joueur).estVide();
			if(!ramasseur_virtuel_egal_certain)
			{
				ramasseur_virtuel_egal_certain|=carte_forte.valeur()>cartes_possibles.get(1).get(joueur).carte(0).valeur();
			}
			ramasseur_deter&=ramasseur_virtuel_egal_certain;
		}
		return ramasseur_deter;
	}
	private boolean existe_joueur_bat_ptm_num(Vector<Byte> equipe_a_battre,Vector<Byte> equipe_dom,Vector<Byte> joueurs_joue,byte numero,byte couleur_demandee,Vector<Vector<MainTarot>> cartes_possibles,Vector<Vector<MainTarot>> cartes_certaines)
	{
		boolean ramasseur_deter=false;
		boolean ramasseur_virtuel_egal_certain=false;
		for(byte joueur:equipe_dom)
		{
			boolean joueur_bat_adversaire=true;
			if(va_couper(couleur_demandee, joueur, cartes_possibles, cartes_certaines))
			{
				for(byte joueur2:equipe_a_battre)
				{
					ramasseur_virtuel_egal_certain=cartes_possibles.get(1).get(joueur2).estVide();
					if(!ramasseur_virtuel_egal_certain)
					{
						ramasseur_virtuel_egal_certain|=cartes_possibles.get(1).get(joueur).carte(0).valeur()>cartes_possibles.get(1).get(joueur2).carte(0).valeur();
					}
					ramasseur_virtuel_egal_certain|=!cartes_certaines.get(couleur_demandee).get(joueur2).estVide();
					joueur_bat_adversaire&=ramasseur_virtuel_egal_certain;
				}
				for(byte joueur2:joueurs_joue)
				{
					ramasseur_virtuel_egal_certain=cartes_possibles.get(1).get(joueur2).estVide();
					if(!ramasseur_virtuel_egal_certain)
					{
						ramasseur_virtuel_egal_certain|=cartes_possibles.get(1).get(joueur).carte(0).valeur()>cartes_possibles.get(1).get(joueur2).carte(0).valeur();
					}
					joueur_bat_adversaire&=ramasseur_virtuel_egal_certain;
				}
				joueur_bat_adversaire&=cartes_possibles.get(1).get(joueur).carte(0).valeur()>cartes_certaines.get(1).get(numero).carte(0).valeur();
				ramasseur_deter|=joueur_bat_adversaire;
			}
		}
		return ramasseur_deter;
	}
	private boolean existe_joueur_bat_adv_num(Vector<Byte> equipe_a_battre,Vector<Byte> equipe_dom,byte numero,byte couleur_demandee,Vector<Vector<MainTarot>> cartes_possibles,Vector<Vector<MainTarot>> cartes_certaines)
	{
		boolean ramasseur_deter=false;
		boolean ramasseur_virtuel_egal_certain=false;
		for(byte joueur:equipe_dom)
		{
			boolean joueur_bat_adversaire=true;
			if(va_couper(couleur_demandee, joueur, cartes_possibles, cartes_certaines))
			{
				for(byte joueur2:equipe_a_battre)
				{
					ramasseur_virtuel_egal_certain=cartes_possibles.get(1).get(joueur2).estVide();
					if(!ramasseur_virtuel_egal_certain)
					{
						ramasseur_virtuel_egal_certain|=cartes_certaines.get(1).get(joueur).carte(0).valeur()>cartes_possibles.get(1).get(joueur2).carte(0).valeur();
					}
					ramasseur_virtuel_egal_certain|=!cartes_certaines.get(couleur_demandee).get(joueur2).estVide();
					joueur_bat_adversaire&=ramasseur_virtuel_egal_certain;
				}
				joueur_bat_adversaire&=cartes_certaines.get(1).get(joueur).carte(0).valeur()>cartes_certaines.get(1).get(numero).carte(0).valeur();
				ramasseur_deter|=joueur_bat_adversaire;
			}
		}
		return ramasseur_deter;
	}
	private boolean existe_joueur_adv_ram_bat_ptm_sur(Vector<Byte> equipe_a_battre,Vector<Byte> equipe_dom,Vector<Byte> joueurs_joue,byte couleur_demandee,Vector<Vector<MainTarot>> cartes_possibles,Vector<Vector<MainTarot>> cartes_certaines)
	{
		boolean ramasseur_deter=false;
		boolean ramasseur_virtuel_egal_certain=false;
		for(byte joueur:equipe_dom)
		{
			boolean joueur_bat_adversaire=true;
			if(va_couper(couleur_demandee, joueur, cartes_possibles, cartes_certaines))
			{
				for(byte joueur2:equipe_a_battre)
				{
					ramasseur_virtuel_egal_certain=cartes_possibles.get(1).get(joueur2).estVide();
					if(!ramasseur_virtuel_egal_certain)
					{
						ramasseur_virtuel_egal_certain|=cartes_possibles.get(1).get(joueur).carte(0).valeur()>cartes_possibles.get(1).get(joueur2).carte(0).valeur();
					}
					ramasseur_virtuel_egal_certain|=!cartes_certaines.get(couleur_demandee).get(joueur2).estVide();
					joueur_bat_adversaire&=ramasseur_virtuel_egal_certain;
				}
				for(byte joueur2:joueurs_joue)
				{
					ramasseur_virtuel_egal_certain=cartes_possibles.get(1).get(joueur2).estVide();
					if(!ramasseur_virtuel_egal_certain)
					{
						ramasseur_virtuel_egal_certain|=cartes_possibles.get(1).get(joueur).carte(0).valeur()>cartes_possibles.get(1).get(joueur2).carte(0).valeur();
					}
					joueur_bat_adversaire&=ramasseur_virtuel_egal_certain;
				}
				ramasseur_deter|=joueur_bat_adversaire;
			}
		}
		return ramasseur_deter;
	}
	private boolean existe_joueur_adv_ram_bat_adv_sur(Vector<Byte> equipe_a_battre,Vector<Byte> equipe_dom,byte couleur_demandee,Carte carte_forte,Vector<Vector<MainTarot>> cartes_possibles,Vector<Vector<MainTarot>> cartes_certaines)
	{
		boolean ramasseur_deter=false;
		boolean ramasseur_virtuel_egal_certain=false;
		for(byte joueur:equipe_dom)
		{
			boolean joueur_bat_adversaire=true;
			if(va_couper(couleur_demandee, joueur, cartes_possibles, cartes_certaines))
			{
				for(byte joueur2:equipe_a_battre)
				{
					ramasseur_virtuel_egal_certain=cartes_possibles.get(1).get(joueur2).estVide();
					if(!ramasseur_virtuel_egal_certain)
					{
						ramasseur_virtuel_egal_certain|=cartes_certaines.get(1).get(joueur).carte(0).valeur()>cartes_possibles.get(1).get(joueur2).carte(0).valeur();
					}
					ramasseur_virtuel_egal_certain|=!cartes_certaines.get(couleur_demandee).get(joueur2).estVide();
					joueur_bat_adversaire&=ramasseur_virtuel_egal_certain;
				}
				joueur_bat_adversaire&=cartes_certaines.get(1).get(joueur).carte(0).valeur()>carte_forte.valeur();
				ramasseur_deter|=joueur_bat_adversaire;
			}
		}
		return ramasseur_deter;
	}
	private boolean existe_joueur_non_joue_battant_ptm(Vector<Byte> equipe_a_battre,Vector<Byte> equipe_dom,Vector<Byte> joueurs_joue,byte couleur_demandee,Vector<Vector<MainTarot>> cartes_possibles,Vector<Vector<MainTarot>> cartes_certaines)
	{
		boolean ramasseur_deter=false;
		boolean ramasseur_virtuel_egal_certain=false;
		for(byte joueur:equipe_dom)
		{
			boolean joueur_bat_adversaire=true;
			if(va_couper(couleur_demandee, joueur, cartes_possibles, cartes_certaines))
			{
				for(byte joueur2:equipe_a_battre)
				{
					ramasseur_virtuel_egal_certain=cartes_possibles.get(1).get(joueur2).estVide();
					if(!ramasseur_virtuel_egal_certain)
					{
						ramasseur_virtuel_egal_certain|=cartes_possibles.get(1).get(joueur).carte(0).valeur()>cartes_possibles.get(1).get(joueur2).carte(0).valeur();
					}
					ramasseur_virtuel_egal_certain|=!cartes_certaines.get(couleur_demandee).get(joueur2).estVide();
					joueur_bat_adversaire&=ramasseur_virtuel_egal_certain;
				}
				for(byte joueur2:joueurs_joue)
				{
					ramasseur_virtuel_egal_certain=cartes_possibles.get(1).get(joueur2).estVide();
					if(!ramasseur_virtuel_egal_certain)
					{
						ramasseur_virtuel_egal_certain|=cartes_possibles.get(1).get(joueur).carte(0).valeur()>cartes_possibles.get(1).get(joueur2).carte(0).valeur();
					}
					joueur_bat_adversaire&=ramasseur_virtuel_egal_certain;
				}
				ramasseur_deter|=joueur_bat_adversaire;
			}
		}
		return ramasseur_deter;
	}
	private boolean existe_joueur_non_joue_battant_adv(Vector<Byte> equipe_a_battre,Vector<Byte> equipe_dom,byte couleur_demandee,Vector<Vector<MainTarot>> cartes_possibles,Vector<Vector<MainTarot>> cartes_certaines)
	{
		boolean ramasseur_deter=false;
		boolean ramasseur_virtuel_egal_certain=false;
		for(byte joueur:equipe_dom)
		{/*On cherche les joueurs de confiance battant de maniere certaine les joueurs de non confiance n'ayant pas joue ou possedant des cartes que les joueurs ayant joue n'ont pas ainsi que les joueurs de non confiance n'ayant pas joue*/
			boolean joueur_bat_adversaire = true;
			if(va_couper(couleur_demandee,joueur,cartes_possibles,cartes_certaines))
			{
				for(byte joueur2:equipe_a_battre)
				{
					ramasseur_virtuel_egal_certain=cartes_possibles.get(1).get(joueur2).estVide();
					if(!ramasseur_virtuel_egal_certain)
					{
						ramasseur_virtuel_egal_certain|=cartes_certaines.get(1).get(joueur).carte(0).valeur()>cartes_possibles.get(1).get(joueur2).carte(0).valeur();
					}
					ramasseur_virtuel_egal_certain|=!cartes_certaines.get(couleur_demandee).get(joueur2).estVide();
					joueur_bat_adversaire&=ramasseur_virtuel_egal_certain;
				}
				ramasseur_deter|=joueur_bat_adversaire;
			}
		}
		return ramasseur_deter;
	}
	private boolean ramasseur_bat_adv_sur(Vector<Byte> equipe_a_battre,byte couleur_demandee,Carte carte_forte,Vector<Vector<MainTarot>> cartes_possibles,Vector<Vector<MainTarot>> cartes_certaines)
	{
		boolean ramasseur_virtuel_egal_certain=false;
		boolean ramasseur_deter=true;
		for(byte joueur:equipe_a_battre)
		{
			ramasseur_virtuel_egal_certain=cartes_possibles.get(1).get(joueur).estVide();
			if(!ramasseur_virtuel_egal_certain)
			{
				ramasseur_virtuel_egal_certain|=carte_forte.valeur()>cartes_possibles.get(1).get(joueur).carte(0).valeur();
			}
			ramasseur_virtuel_egal_certain|=!cartes_certaines.get(couleur_demandee).get(joueur).estVide();
			ramasseur_deter&=ramasseur_virtuel_egal_certain;
		}
		return ramasseur_deter;
	}
	private boolean ramasseur_bat_ss_cpr_adv(Vector<Byte> equipe_a_battre,byte couleur_demandee,Carte carte_forte,Vector<Vector<MainTarot>> cartes_possibles,Vector<Vector<MainTarot>> cartes_certaines)
	{
		boolean ramasseur_virtuel_egal_certain=false;
		boolean ramasseur_deter=true;
		for(byte joueur:equipe_a_battre)
		{
			ramasseur_virtuel_egal_certain=!cartes_certaines.get(couleur_demandee).get(joueur).estVide();
			if(ramasseur_virtuel_egal_certain)
			{
				ramasseur_virtuel_egal_certain&=carte_forte.valeur()>cartes_possibles.get(couleur_demandee).get(joueur).carte(0).valeur();
			}
			ramasseur_virtuel_egal_certain|=defausse(cartes_possibles, joueur, couleur_demandee);
			ramasseur_deter&=ramasseur_virtuel_egal_certain;
		}
		return ramasseur_deter;
	}
	/**Est vrai si et seulement si on est sur que le joueur va couper le pli a la couleur demandee*/
	private boolean va_couper(byte couleur,byte joueur,Vector<Vector<MainTarot>> cartes_possibles,Vector<Vector<MainTarot>> cartes_certaines)
	{
		return cartes_possibles.get(couleur).get(joueur).estVide()&&!cartes_certaines.get(1).get(joueur).estVide();
	}
	private boolean ne_peut_couper(byte couleur,byte numero,Vector<Vector<MainTarot>> cartes_possibles,Vector<Vector<MainTarot>> cartes_certaines)
	{
		return cartes_possibles.get(1).get(numero).estVide()||!cartes_certaines.get(couleur).get(numero).estVide();
	}
	/**Renvoie vrai si et seulement si la carte appelee n'existe pas pour cette partie ou si la carte appelee existe et est jouee*/
	public boolean carteAppeleeJouee()
	{
		if(carteAppelee==null)
			return true;
		return joue(carteAppelee,false);
	}
	/**@param ct la carte de tarot a recherche dans les plis
	 * @param chien est vrai si et seulement si on tient compte du chien
	 * @return true si la carte est deja jouee*/
	private boolean joue(Carte ct,boolean chien)
	{
		if(chien)
		{
			for(Vector<Pli> plis_equipe:plis)
			{
				for(Pli pli:plis_equipe)
				{
					if(pli.contient(ct))
					{
						return true;
					}
				}
			}
		}
		else
		{
			for(Vector<Pli> plis_equipe:plis)
			{
				for(Pli pli:plis_equipe)
				{
					if(pli.contient(ct)&&pli.getNumero()>0)
					{
						return true;
					}
				}
			}
		}
		return false;
	}
	public void faireConfiance(byte joueur,byte enjoueur)
	{
		confiance.get(joueur).setElementAt(true,enjoueur);
	}
	public void faireMefiance(byte joueur,byte enjoueur)
	{
		confiance.get(joueur).setElementAt(false,enjoueur);
	}
	private boolean meme_equipe(byte numero1,byte numero2)
	{
		return a_pour_defenseur(numero1)==a_pour_defenseur(numero2);
	}
	private Vector<Byte> joueursDeConfiance(byte joueur)
	{
		Vector<Byte> confiances=new Vector<Byte>();
		for(byte joueur2=0;joueur2<getNombreDeJoueurs();joueur2++)
			if(confiance(joueur,joueur2)&&joueur!=joueur2)
				confiances.addElement(joueur2);
		return confiances;
	}
	private Vector<Byte> joueursDeNonConfiance(byte joueur)
	{
		Vector<Byte> confiances=new Vector<Byte>();
		for(byte joueur2=0;joueur2<getNombreDeJoueurs();joueur2++)
			if(!confiance(joueur,joueur2))
				confiances.addElement(joueur2);
		return confiances;
	}
	public Vector<Byte> coequipiers(byte joueur)
	{
		Vector<Byte> equipe=new Vector<Byte>();
		for(byte joueur2=0;joueur2<getNombreDeJoueurs();joueur2++)
			if(meme_equipe(joueur, joueur2)&&joueur!=joueur2)
				equipe.addElement(joueur2);
		return equipe;
	}
	private Vector<Byte> adversaires(byte joueur)
	{
		Vector<Byte> equipe=new Vector<Byte>();
		for(byte joueur2=0;joueur2<getNombreDeJoueurs();joueur2++)
			if(!meme_equipe(joueur, joueur2))
				equipe.addElement(joueur2);
		return equipe;
	}
	public Vector<Byte> tours(byte couleur,Vector<Pli> plis_faits)
	{
		Vector<Byte> nb=new Vector<Byte>();
		for(Pli pli:plis_faits)
			if(pli.total()>getNombreDeJoueurs()-2)
				if(pli.couleurDemandee()==couleur&&pli.getNumero()>0)
					nb.addElement(pli.getNumero());
		return nb;
	}
	/**Retourne vrai si et seulement si le joueur ne peut pas fournir la couleur donnee et peut couper avec un atout*/
	private boolean peut_couper(byte couleur,byte numero,Vector<Vector<MainTarot>> cartes_possibles)
	{
		return cartes_possibles.get(couleur).get(numero).estVide()&&!cartes_possibles.get(1).get(numero).estVide();
	}
	/**Appele pour determiner le joueur ayant la carte appelee et initialiser sa confiance envers les autres joueurs*/
	public byte joueurAyantCarteAppelee()
	{
		boolean possedeCarteAppelee=false;
		byte b=0;
		for(;b<getNombreDeJoueurs()&&!possedeCarteAppelee;possedeCarteAppelee|=getDistribution().main(b).contient(carteAppelee),b++);
		if(!possedeCarteAppelee)
			return preneur;
		return --b;
	}
	public Vector<Pli> getPlisAttaque()
	{
		return plis.get(0);
	}
	public Vector<Pli> getPlisDefense()
	{
		return plis.get(1);
	}
	public boolean premierTour()
	{
		int plis_total=0;
		for(Vector<Pli> plis_equipe:plis)
		{
			plis_total+=plis_equipe.size();
		}
		return plis_total<=1;
	}
	/**Retourne vrai si et seulement si l'ensemble des joueurs adeverses a un joueur donne a fait au moins un pli*/
	public boolean adversaireAFaitPlis(byte numero)
	{
		if(preneur==-1)
		{
			Vector<Pli> plisFaits=unionPlis();
			int nombrePlis=0;
			for(int i=1;i<plisFaits.size()-1;i++)
				if(plisFaits.get(i).total()==getNombreDeJoueurs()||plisFaits.get(i).total()==getNombreDeJoueurs()-1)
					if(plisFaits.get(i+1).getEntameur()!=numero)
						nombrePlis++;
			if(pliEnCours.getEntameur()!=numero)
				nombrePlis++;
			return nombrePlis>0;
		}
		Vector<Pli> plisFaitsAdversaire;
		if(a_pour_defenseur(numero))
		{
			plisFaitsAdversaire=getPlisAttaque();
		}
		else
		{
			plisFaitsAdversaire=getPlisDefense();
		}
		int nombrePlis=0;
		for(Pli pli:plisFaitsAdversaire)
			if(pli.total()==getNombreDeJoueurs()||pli.total()==getNombreDeJoueurs()-1)
				nombrePlis++;
		return nombrePlis>0;
	}
	/**Inclut tous les plis sauf celui qui est en cours classes dans l'ordre chronologique (par leur numero)
	 * On a pour tout pli d'indice i unionPlis.get(i).getNumero()==i*/
	public Vector<Pli> unionPlis()
	{
		Vector<Pli> unionPlis=new Vector<Pli>();
		unionPlis.addAll(plis.get(0));
		for(byte b=1;b<plis.size();b++)
		{
			for(Pli pli:plis.get(b))
			{
				if(unionPlis.isEmpty())
					unionPlis.addElement(pli);
				else if(pli.getNumero()>unionPlis.lastElement().getNumero())
					unionPlis.addElement(pli);
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
	public void ajouterPoignee(MainTarot mt,byte numero)
	{
		poignees.setElementAt(mt, numero);
	}
	public MainTarot strategiePoignee(byte numeroJoueur,String[] raison)
	{
		MainTarot main_joueur=(MainTarot) getDistribution().main(numeroJoueur);
		Vector<MainTarot> repartition=main_joueur.couleurs();
		MainTarot atouts=atouts(repartition);
		MainTarot poignee=new MainTarot();
		byte max;
		if(getAnnonces(numeroJoueur).contains(new Annonce(Poignees.Poignee)))
		{
			max=poignees()[0];
			for(byte b=0;poignee.total()<max;b++)
				if(atouts.carte(b).couleur()>0||atouts.total()==max)
					poignee.ajouter(atouts.carte(b));
			raison[0]="Il faut cacher les atouts les plus petits.";
		}
		if(getAnnonces(numeroJoueur).contains(new Annonce(Poignees.Double_Poignee)))
		{
			max=poignees()[1];
			for(byte b=0;poignee.total()<max;b++)
				if(atouts.carte(b).couleur()>0||atouts.total()==max)
					poignee.ajouter(atouts.carte(b));
			raison[0]="Il faut cacher les atouts les plus petits.";
		}
		if(getAnnonces(numeroJoueur).contains(new Annonce(Poignees.Triple_Poignee)))
		{
			max=poignees()[2];
			for(byte b=0;poignee.total()<max;b++)
				if(atouts.carte(b).couleur()>0||atouts.total()==max)
					poignee.ajouter(atouts.carte(b));
			raison[0]="Il faut cacher les atouts les plus petits.";
		}
		return poignee;
	}
	/**trie les atouts de la maniere suivante: (Excuse, 21, 20, ... 1)*/
	public static MainTarot atouts(Vector<MainTarot> repartition)
	{
		MainTarot m=new MainTarot();
		m.ajouterCartes(repartition.get(1));
		if(!repartition.get(0).estVide())
			m.ajouter(new CarteTarot((byte)0),0);
		return m;
	}
	public byte[] poignees()
	{
		byte[] nombre=new byte[3];
		byte nombre_joueurs=getNombreDeJoueurs();
		if(nombre_joueurs==3)
		{
			nombre[0]=13;
			nombre[1]=15;
			nombre[2]=18;
		}
		else if(nombre_joueurs==4)
		{
			nombre[0]=10;
			nombre[1]=13;
			nombre[2]=15;
		}
		else
		{
			nombre[0]=8;
			nombre[1]=10;
			nombre[2]=13;
		}
		return nombre;
	}
	private byte couleurAppelee()
	{
		if(carteAppelee==null)
			return-1;
		return carteAppelee.couleur();
	}
	public short score_joueur_plis_double(Pli pli_petit,byte joueur)
	{
		short nbPointsAtt=0;
		boolean contient_excuse=false;
		Vector<Pli> plis_attaque=plis.get(joueur);
		for(Pli pli:plis_attaque)
		{
			for(Carte carte:pli)
			{
				nbPointsAtt+=((CarteTarot)carte).points();
				contient_excuse|=carte.couleur()==0;
			}
		}
		if(pli_petit!=null&&pli_petit.total()==1)
		{
			if(contient_excuse)
			{
				nbPointsAtt--;
			}
			else
			{
				nbPointsAtt++;
			}
		}
		return nbPointsAtt;
	}
	public short score_necessaire_joueur(byte joueur)
	{
		byte nombre_bouts=0;
		for(Pli pli:plis.get(joueur))
		{
			for(Carte carte:pli)
			{
				if(((CarteTarot)carte).estUnBout())
				{
					nombre_bouts++;
				}
			}
		}
		if(nombre_bouts==0)
		{
			return 56;
		}
		if(nombre_bouts==1)
		{
			return 51;
		}
		if(nombre_bouts==2)
		{
			return 41;
		}
		return 36;
	}
	public short difference_joueur_double(short score_necessaire_joueur,short score_joueur_plis_double)
	{
		return (short)(score_joueur_plis_double-2*score_necessaire_joueur);
	}
	public short difference_joueur_double_misere(short score_necessaire_joueur,short score_joueur_plis_double)
	{
		return (short)(2*score_necessaire_joueur-score_joueur_plis_double);
	}
	public byte[] positions_difference(short[] differences)
	{
		byte[] positions=new byte[differences.length];
		for(byte joueur=0;joueur<positions.length;joueur++)
		{
			positions[joueur]=1;
			for(short difference:differences)
			{
				if(difference>differences[joueur])
				{
					positions[joueur]++;
				}
			}
		}
		return positions;
	}
	/**On classe les joueurs selon certains criteres pour les departager en changeant le tableau des positions*/
	public void changer_positions1(byte[] positions,boolean pas_jeu_misere)
	{
		Vector<Vector<Byte>> groupes=new Vector<Vector<Byte>>();
		Vector<Byte> positions_distinctes=new Vector<Byte>();
		byte indice=0;
		MainTarot main;
		MainTarot main2;
		byte nombre_bouts;
		byte position_temporaire;
		byte nombre_bouts2;
		for(byte position:positions)
		{
			if(!positions_distinctes.contains(position))
			{
				positions_distinctes.addElement(position);
			}
		}
		for(byte position2:positions_distinctes)
		{
			groupes.addElement(new Vector<Byte>());
			indice=0;
			for(byte position:positions)
			{
				if(position==position2)
				{
					groupes.lastElement().addElement(indice);
				}
				indice++;
			}
		}
		for(indice=0;indice<groupes.size();)
		{
			if(groupes.get(indice).size()<2)
			{
				groupes.removeElementAt(indice);
			}
			else
			{
				indice++;
			}
		}
		if(pas_jeu_misere)
		{
			for(Vector<Byte> groupe:groupes)
			{
				for(byte joueur:groupe)
				{
					position_temporaire=1;
					if(!plis.get(joueur).isEmpty())
					{
						main=new MainTarot();
						for(Pli pli:plis.get(joueur))
						{
							main.ajouterCartes(pli.getCartes());
						}
						nombre_bouts=(byte)main.nombreDeBouts();
						for(byte joueur2:groupe)
						{
							main2=new MainTarot();
							for(Pli pli:plis.get(joueur2))
							{
								main2.ajouterCartes(pli.getCartes());
							}
							nombre_bouts2=(byte)main2.nombreDeBouts();
							if(nombre_bouts2>nombre_bouts)
							{
								position_temporaire++;
							}
						}
						positions[joueur]+=position_temporaire-1;
					}
				}
			}
		}
		else
		{
			for(Vector<Byte> groupe:groupes)
			{
				for(byte joueur:groupe)
				{
					position_temporaire=1;
					if(!plis.get(joueur).isEmpty())
					{
						main=new MainTarot();
						for(Pli pli:plis.get(joueur))
						{
							main.ajouterCartes(pli.getCartes());
						}
						nombre_bouts=(byte)main.nombreDeBouts();
						for(byte joueur2:groupe)
						{
							main2=new MainTarot();
							for(Pli pli:plis.get(joueur2))
							{
								main2.ajouterCartes(pli.getCartes());
							}
							nombre_bouts2=(byte)main2.nombreDeBouts();
							if(nombre_bouts2<nombre_bouts)
							{
								position_temporaire++;
							}
						}
						positions[joueur]+=position_temporaire-1;
					}
				}
			}
		}
	}
	/**On classe les joueurs selon certains criteres pour les departager en changeant le tableau des positions*/
	public void changer_positions2(byte[] positions,boolean pas_jeu_misere)
	{
		Vector<Vector<Byte>> groupes=new Vector<Vector<Byte>>();
		Vector<Byte> positions_distinctes=new Vector<Byte>();
		byte indice=0;
		MainTarot main;
		MainTarot main2;
		byte nombre_bouts;
		byte nombre_figures;
		byte nombre_figures2;
		byte position_temporaire;
		Carte bout;
		Carte bout2;
		groupes=new Vector<Vector<Byte>>();
		positions_distinctes=new Vector<Byte>();
		indice=0;
		for(byte position:positions)
		{
			if(!positions_distinctes.contains(position))
			{
				positions_distinctes.addElement(position);
			}
		}
		for(byte position2:positions_distinctes)
		{
			groupes.addElement(new Vector<Byte>());
			indice=0;
			for(byte position:positions)
			{
				if(position==position2)
				{
					groupes.lastElement().addElement(indice);
				}
				indice++;
			}
		}
		for(indice=0;indice<groupes.size();)
		{
			if(groupes.get(indice).size()<2)
			{
				groupes.removeElementAt(indice);
			}
			else
			{
				indice++;
			}
		}
		if(pas_jeu_misere)
		{
			for(Vector<Byte> groupe:groupes)
			{
				for(byte joueur:groupe)
				{
					position_temporaire=1;
					if(!plis.get(joueur).isEmpty())
					{
						main=new MainTarot();
						for(Pli pli:plis.get(joueur))
						{
							main.ajouterCartes(pli.getCartes());
						}
						nombre_bouts=(byte)main.nombreDeBouts();
						nombre_figures=(byte)main.nombreDeFigures();
						if(nombre_bouts==0)
						{
							for(byte joueur2:groupe)
							{
								main2=new MainTarot();
								for(Pli pli:plis.get(joueur2))
								{
									main2.ajouterCartes(pli.getCartes());
								}
								nombre_figures2=(byte)main2.nombreDeFigures();
								if(nombre_figures2>nombre_figures)
								{
									position_temporaire++;
								}
							}
						}
						else
						{
							bout=main.bouts().carte(0);
							if(bout.couleur()==0)
							{
								for(byte joueur2:groupe)
								{
									main2=new MainTarot();
									for(Pli pli:plis.get(joueur2))
									{
										main2.ajouterCartes(pli.getCartes());
									}
									bout2=main2.bouts().carte(0);
									if(bout2.equals(new CarteTarot((byte)21,(byte)1)))
									{
										position_temporaire++;
									}
								}
							}
							else if(bout.equals(new CarteTarot((byte)1,(byte)1)))
							{
								for(byte joueur2:groupe)
								{
									main2=new MainTarot();
									for(Pli pli:plis.get(joueur2))
									{
										main2.ajouterCartes(pli.getCartes());
									}
									bout2=main2.bouts().carte(0);
									if(bout2.couleur()==0||bout2.equals(new CarteTarot((byte)21,(byte)1)))
									{
										position_temporaire++;
									}
								}
							}
						}
						positions[joueur]+=position_temporaire-1;
					}
				}
			}
		}
		else
		{
			for(Vector<Byte> groupe:groupes)
			{
				for(byte joueur:groupe)
				{
					position_temporaire=1;
					if(!plis.get(joueur).isEmpty())
					{
						main=new MainTarot();
						for(Pli pli:plis.get(joueur))
						{
							main.ajouterCartes(pli.getCartes());
						}
						nombre_bouts=(byte)main.nombreDeBouts();
						nombre_figures=(byte)main.nombreDeFigures();
						if(nombre_bouts==0)
						{
							for(byte joueur2:groupe)
							{
								main2=new MainTarot();
								for(Pli pli:plis.get(joueur2))
								{
									main2.ajouterCartes(pli.getCartes());
								}
								nombre_figures2=(byte)main2.nombreDeFigures();
								if(nombre_figures2<nombre_figures)
								{
									position_temporaire++;
								}
							}
						}
						else
						{
							bout=main.bouts().carte(0);
							if(bout.couleur()==0)
							{
								for(byte joueur2:groupe)
								{
									main2=new MainTarot();
									for(Pli pli:plis.get(joueur2))
									{
										main2.ajouterCartes(pli.getCartes());
									}
									bout2=main2.bouts().carte(0);
									if(bout2.equals(new CarteTarot((byte)1,(byte)1)))
									{
										position_temporaire++;
									}
								}
							}
							else if(bout.equals(new CarteTarot((byte)21,(byte)1)))
							{
								for(byte joueur2:groupe)
								{
									main2=new MainTarot();
									for(Pli pli:plis.get(joueur2))
									{
										main2.ajouterCartes(pli.getCartes());
									}
									bout2=main2.bouts().carte(0);
									if(bout2.couleur()==0||bout2.equals(new CarteTarot((byte)1,(byte)1)))
									{
										position_temporaire++;
									}
								}
							}
						}
						positions[joueur]+=position_temporaire-1;
					}
				}
			}
		}
	}
	/**On classe les joueurs selon certains criteres pour les departager en changeant le tableau des positions*/
	public void changer_positions3(byte[] positions,boolean pas_jeu_misere)
	{
		Vector<Vector<Byte>> groupes=new Vector<Vector<Byte>>();
		Vector<Byte> positions_distinctes=new Vector<Byte>();
		byte indice=0;
		MainTarot main;
		MainTarot main2;
		MainTarot figures;
		MainTarot figures2;
		byte nombre_bouts;
		byte position_temporaire;
		groupes=new Vector<Vector<Byte>>();
		positions_distinctes=new Vector<Byte>();
		indice=0;
		groupes=new Vector<Vector<Byte>>();
		positions_distinctes=new Vector<Byte>();
		indice=0;
		for(byte position:positions)
		{
			if(!positions_distinctes.contains(position))
			{
				positions_distinctes.addElement(position);
			}
		}
		for(byte position2:positions_distinctes)
		{
			groupes.addElement(new Vector<Byte>());
			indice=0;
			for(byte position:positions)
			{
				if(position==position2)
				{
					groupes.lastElement().addElement(indice);
				}
				indice++;
			}
		}
		for(indice=0;indice<groupes.size();)
		{
			if(groupes.get(indice).size()<2)
			{
				groupes.removeElementAt(indice);
			}
			else
			{
				indice++;
			}
		}
		if(pas_jeu_misere)
		{
			for(Vector<Byte> groupe:groupes)
			{
				for(byte joueur:groupe)
				{
					position_temporaire=1;
					if(!plis.get(joueur).isEmpty())
					{
						main=new MainTarot();
						for(Pli pli:plis.get(joueur))
						{
							main.ajouterCartes(pli.getCartes());
						}
						nombre_bouts=(byte)main.nombreDeBouts();
						if(nombre_bouts==0)
						{
							figures=new MainTarot();
							for(byte couleur=2;couleur<6;couleur++)
							{
								figures.ajouterCartes(main.figures(couleur));
							}
							for(int indice_figure=0;indice_figure<figures.total();indice_figure++)
							{
								for(int indice_figure2=indice_figure+1;indice_figure2<figures.total();indice_figure2++)
								{
									if(figures.carte(indice_figure).valeur()<figures.carte(indice_figure2).valeur())
									{
										figures.echanger(indice_figure, indice_figure2);
									}
								}
							}
							for(byte joueur2:groupe)
							{
								main2=new MainTarot();
								for(Pli pli:plis.get(joueur2))
								{
									main2.ajouterCartes(pli.getCartes());
								}
								figures2=new MainTarot();
								for(byte couleur=2;couleur<6;couleur++)
								{
									figures2.ajouterCartes(main2.figures(couleur));
								}
								for(int indice_figure=0;indice_figure<figures2.total();indice_figure++)
								{
									for(int indice_figure2=indice_figure+1;indice_figure2<figures2.total();indice_figure2++)
									{
										if(figures2.carte(indice_figure).valeur()<figures2.carte(indice_figure2).valeur())
										{
											figures2.echanger(indice_figure, indice_figure2);
										}
									}
								}
								for(int indice_figure=0;indice_figure<figures.total();indice_figure++)
								{
									if(figures2.carte(indice_figure).valeur()>figures.carte(indice_figure).valeur())
									{
										position_temporaire++;
										break;
									}
									else if(figures2.carte(indice_figure).valeur()<figures.carte(indice_figure).valeur())
									{
										break;
									}
								}
							}
							positions[joueur]+=position_temporaire-1;
						}
					}
				}
			}
		}
		else
		{
			for(Vector<Byte> groupe:groupes)
			{
				for(byte joueur:groupe)
				{
					position_temporaire=1;
					if(!plis.get(joueur).isEmpty())
					{
						main=new MainTarot();
						for(Pli pli:plis.get(joueur))
						{
							main.ajouterCartes(pli.getCartes());
						}
						nombre_bouts=(byte)main.nombreDeBouts();
						if(nombre_bouts==0)
						{
							figures=new MainTarot();
							for(byte couleur=2;couleur<6;couleur++)
							{
								figures.ajouterCartes(main.figures(couleur));
							}
							for(int indice_figure=0;indice_figure<figures.total();indice_figure++)
							{
								for(int indice_figure2=indice_figure+1;indice_figure2<figures.total();indice_figure2++)
								{
									if(figures.carte(indice_figure).valeur()<figures.carte(indice_figure2).valeur())
									{
										figures.echanger(indice_figure, indice_figure2);
									}
								}
							}
							for(byte joueur2:groupe)
							{
								main2=new MainTarot();
								for(Pli pli:plis.get(joueur2))
								{
									main2.ajouterCartes(pli.getCartes());
								}
								figures2=new MainTarot();
								for(byte couleur=2;couleur<6;couleur++)
								{
									figures2.ajouterCartes(main2.figures(couleur));
								}
								for(int indice_figure=0;indice_figure<figures2.total();indice_figure++)
								{
									for(int indice_figure2=indice_figure+1;indice_figure2<figures2.total();indice_figure2++)
									{
										if(figures2.carte(indice_figure).valeur()<figures2.carte(indice_figure2).valeur())
										{
											figures2.echanger(indice_figure, indice_figure2);
										}
									}
								}
								for(int indice_figure=0;indice_figure<figures.total();indice_figure++)
								{
									if(figures2.carte(indice_figure).valeur()<figures.carte(indice_figure).valeur())
									{
										position_temporaire++;
										break;
									}
									else if(figures2.carte(indice_figure).valeur()>figures.carte(indice_figure).valeur())
									{
										break;
									}
								}
							}
							positions[joueur]+=position_temporaire-1;
						}
					}
				}
			}
		}
	}
	/**On classe les joueurs selon certains criteres pour les departager en changeant le tableau des positions*/
	public void changer_positions4(byte[] positions,boolean pas_jeu_misere)
	{
		Vector<Vector<Byte>> groupes=new Vector<Vector<Byte>>();
		Vector<Byte> positions_distinctes=new Vector<Byte>();
		boolean egalite_figures;
		byte indice=0;
		MainTarot main;
		MainTarot main2;
		MainTarot figures;
		MainTarot figures2;
		byte nombre_bouts;
		byte position_temporaire;
		groupes=new Vector<Vector<Byte>>();
		positions_distinctes=new Vector<Byte>();
		indice=0;
		groupes=new Vector<Vector<Byte>>();
		positions_distinctes=new Vector<Byte>();
		indice=0;
		groupes=new Vector<Vector<Byte>>();
		positions_distinctes=new Vector<Byte>();
		indice=0;
		for(byte position:positions)
		{
			if(!positions_distinctes.contains(position))
			{
				positions_distinctes.addElement(position);
			}
		}
		for(byte position2:positions_distinctes)
		{
			groupes.addElement(new Vector<Byte>());
			indice=0;
			for(byte position:positions)
			{
				if(position==position2)
				{
					groupes.lastElement().addElement(indice);
				}
				indice++;
			}
		}
		for(indice=0;indice<groupes.size();)
		{
			if(groupes.get(indice).size()<2)
			{
				groupes.removeElementAt(indice);
			}
			else
			{
				indice++;
			}
		}
		if(pas_jeu_misere)
		{
			for(Vector<Byte> groupe:groupes)
			{
				for(byte joueur:groupe)
				{
					position_temporaire=1;
					if(!plis.get(joueur).isEmpty())
					{
						main=new MainTarot();
						for(Pli pli:plis.get(joueur))
						{
							main.ajouterCartes(pli.getCartes());
						}
						nombre_bouts=(byte)main.nombreDeBouts();
						if(nombre_bouts==0)
						{
							figures=new MainTarot();
							for(byte couleur=2;couleur<6;couleur++)
							{
								figures.ajouterCartes(main.figures(couleur));
							}
							for(int indice_figure=0;indice_figure<figures.total();indice_figure++)
							{
								for(int indice_figure2=indice_figure+1;indice_figure2<figures.total();indice_figure2++)
								{
									if(figures.carte(indice_figure).valeur()<figures.carte(indice_figure2).valeur())
									{
										figures.echanger(indice_figure, indice_figure2);
									}
								}
							}
							for(byte joueur2:groupe)
							{
								main2=new MainTarot();
								for(Pli pli:plis.get(joueur2))
								{
									main2.ajouterCartes(pli.getCartes());
								}
								figures2=new MainTarot();
								for(byte couleur=2;couleur<6;couleur++)
								{
									figures2.ajouterCartes(main2.figures(couleur));
								}
								for(int indice_figure=0;indice_figure<figures2.total();indice_figure++)
								{
									for(int indice_figure2=indice_figure+1;indice_figure2<figures2.total();indice_figure2++)
									{
										if(figures2.carte(indice_figure).valeur()<figures2.carte(indice_figure2).valeur())
										{
											figures2.echanger(indice_figure, indice_figure2);
										}
									}
								}
								egalite_figures=true;
								for(int indice_figure=0;indice_figure<figures.total();indice_figure++)
								{
									if(figures2.carte(indice_figure).valeur()!=figures.carte(indice_figure).valeur())
									{
										egalite_figures=false;
										break;
									}
								}
								if(egalite_figures&&plis.get(joueur2).get(0).getNumero()<plis.get(joueur).get(0).getNumero())
								{
									position_temporaire++;
								}
							}
							positions[joueur]+=position_temporaire-1;
						}
					}
				}
			}
		}
		else
		{
			for(Vector<Byte> groupe:groupes)
			{
				for(byte joueur:groupe)
				{
					position_temporaire=1;
					if(!plis.get(joueur).isEmpty())
					{
						main=new MainTarot();
						for(Pli pli:plis.get(joueur))
						{
							main.ajouterCartes(pli.getCartes());
						}
						nombre_bouts=(byte)main.nombreDeBouts();
						if(nombre_bouts==0)
						{
							figures=new MainTarot();
							for(byte couleur=2;couleur<6;couleur++)
							{
								figures.ajouterCartes(main.figures(couleur));
							}
							for(int indice_figure=0;indice_figure<figures.total();indice_figure++)
							{
								for(int indice_figure2=indice_figure+1;indice_figure2<figures.total();indice_figure2++)
								{
									if(figures.carte(indice_figure).valeur()<figures.carte(indice_figure2).valeur())
									{
										figures.echanger(indice_figure, indice_figure2);
									}
								}
							}
							for(byte joueur2:groupe)
							{
								main2=new MainTarot();
								for(Pli pli:plis.get(joueur2))
								{
									main2.ajouterCartes(pli.getCartes());
								}
								figures2=new MainTarot();
								for(byte couleur=2;couleur<6;couleur++)
								{
									figures2.ajouterCartes(main2.figures(couleur));
								}
								for(int indice_figure=0;indice_figure<figures2.total();indice_figure++)
								{
									for(int indice_figure2=indice_figure+1;indice_figure2<figures2.total();indice_figure2++)
									{
										if(figures2.carte(indice_figure).valeur()<figures2.carte(indice_figure2).valeur())
										{
											figures2.echanger(indice_figure, indice_figure2);
										}
									}
								}
								egalite_figures=true;
								for(int indice_figure=0;indice_figure<figures.total();indice_figure++)
								{
									if(figures2.carte(indice_figure).valeur()!=figures.carte(indice_figure).valeur())
									{
										egalite_figures=false;
										break;
									}
								}
								if(egalite_figures&&plis.get(joueur2).get(0).getNumero()>plis.get(joueur).get(0).getNumero())
								{
									position_temporaire++;
								}
							}
							positions[joueur]+=position_temporaire-1;
						}
					}
				}
			}
		}
	}
	public byte[] coefficients(byte[] positions)
	{
		byte max_position=0;
		byte nombre_litiges;
		byte indice=0;
		byte nombre_joueurs=getNombreDeJoueurs();
		byte[] coefficients=new byte[nombre_joueurs];
		for(byte position:positions)
		{
			max_position=(byte)Math.max(position,max_position);
		}
		nombre_litiges=(byte)(nombre_joueurs-max_position+1);
		if(nombre_joueurs==3)
		{
			if(nombre_litiges==1)
			{
				for(byte position:positions)
				{
					if(position==1)
					{
						coefficients[indice]=1;
					}
					else if(position==2)
					{
						coefficients[indice]=0;
					}
					else
					{
						coefficients[indice]=-1;
					}
					indice++;
				}
				return coefficients;
			}
			for(byte position:positions)
			{
				if(position==1)
				{
					coefficients[indice]=2;
				}
				else
				{
					coefficients[indice]=-1;
				}
				indice++;
			}
			return coefficients;
		}
		if(nombre_joueurs==4)
		{
			if(nombre_litiges==1)
			{
				for(byte position:positions)
				{
					if(position==1)
					{
						coefficients[indice]=2;
					}
					else if(position==2)
					{
						coefficients[indice]=1;
					}
					else if(position==3)
					{
						coefficients[indice]=-1;
					}
					else
					{
						coefficients[indice]=-2;
					}
					indice++;
				}
				return coefficients;
			}
			if(nombre_litiges==2)
			{
				for(byte position:positions)
				{
					if(position==1)
					{
						coefficients[indice]=3;
					}
					else if(position==2)
					{
						coefficients[indice]=1;
					}
					else
					{
						coefficients[indice]=-2;
					}
					indice++;
				}
				return coefficients;
			}
			for(byte position:positions)
			{
				if(position==1)
				{
					coefficients[indice]=6;
				}
				else
				{
					coefficients[indice]=-2;
				}
				indice++;
			}
			return coefficients;
		}
		if(nombre_litiges==1)
		{
			for(byte position:positions)
			{
				if(position==1)
				{
					coefficients[indice]=2;
				}
				else if(position==2)
				{
					coefficients[indice]=1;
				}
				else if(position==3)
				{
					coefficients[indice]=0;
				}
				else if(position==4)
				{
					coefficients[indice]=-1;
				}
				else
				{
					coefficients[indice]=-2;
				}
				indice++;
			}
			return coefficients;
		}
		if(nombre_litiges==2)
		{
			for(byte position:positions)
			{
				if(position==1)
				{
					coefficients[indice]=3;
				}
				else if(position==2)
				{
					coefficients[indice]=1;
				}
				else if(position==3)
				{
					coefficients[indice]=0;
				}
				else
				{
					coefficients[indice]=-2;
				}
				indice++;
			}
			return coefficients;
		}
		if(nombre_litiges==3)
		{
			for(byte position:positions)
			{
				if(position==1)
				{
					coefficients[indice]=6;
				}
				else if(position==2)
				{
					coefficients[indice]=0;
				}
				else
				{
					coefficients[indice]=-2;
				}
				indice++;
			}
			return coefficients;
		}
		for(byte position:positions)
		{
			if(position==1)
			{
				coefficients[indice]=8;
			}
			else
			{
				coefficients[indice]=-2;
			}
			indice++;
		}
		return coefficients;
	}
	public byte[] coefficients_misere(byte[] positions)
	{
		byte max_position=0;
		byte nombre_litiges;
		byte indice=0;
		byte nombre_joueurs=getNombreDeJoueurs();
		byte[] coefficients=new byte[nombre_joueurs];
		for(byte position:positions)
		{
			max_position=(byte)Math.max(position,max_position);
		}
		nombre_litiges=(byte)(nombre_joueurs-max_position+1);
		if(nombre_joueurs==3)
		{
			if(nombre_litiges==1)
			{
				for(byte position:positions)
				{
					if(position==1)
					{
						coefficients[indice]=1;
					}
					else if(position==2)
					{
						coefficients[indice]=0;
					}
					else
					{
						coefficients[indice]=-1;
					}
					indice++;
				}
				return coefficients;
			}
			for(byte position:positions)
			{
				if(position==1)
				{
					coefficients[indice]=1;
				}
				else
				{
					coefficients[indice]=-2;
				}
				indice++;
			}
			return coefficients;
		}
		if(nombre_joueurs==4)
		{
			if(nombre_litiges==1)
			{
				for(byte position:positions)
				{
					if(position==1)
					{
						coefficients[indice]=2;
					}
					else if(position==2)
					{
						coefficients[indice]=1;
					}
					else if(position==3)
					{
						coefficients[indice]=-1;
					}
					else
					{
						coefficients[indice]=-2;
					}
					indice++;
				}
				return coefficients;
			}
			if(nombre_litiges==2)
			{
				for(byte position:positions)
				{
					if(position==1)
					{
						coefficients[indice]=2;
					}
					else if(position==2)
					{
						coefficients[indice]=-1;
					}
					else
					{
						coefficients[indice]=-3;
					}
					indice++;
				}
				return coefficients;
			}
			for(byte position:positions)
			{
				if(position==1)
				{
					coefficients[indice]=2;
				}
				else
				{
					coefficients[indice]=-6;
				}
				indice++;
			}
			return coefficients;
		}
		if(nombre_litiges==1)
		{
			for(byte position:positions)
			{
				if(position==1)
				{
					coefficients[indice]=2;
				}
				else if(position==2)
				{
					coefficients[indice]=1;
				}
				else if(position==3)
				{
					coefficients[indice]=0;
				}
				else if(position==4)
				{
					coefficients[indice]=-1;
				}
				else
				{
					coefficients[indice]=-2;
				}
				indice++;
			}
			return coefficients;
		}
		if(nombre_litiges==2)
		{
			for(byte position:positions)
			{
				if(position==1)
				{
					coefficients[indice]=2;
				}
				else if(position==2)
				{
					coefficients[indice]=0;
				}
				else if(position==3)
				{
					coefficients[indice]=-1;
				}
				else
				{
					coefficients[indice]=-3;
				}
				indice++;
			}
			return coefficients;
		}
		if(nombre_litiges==3)
		{
			for(byte position:positions)
			{
				if(position==1)
				{
					coefficients[indice]=2;
				}
				else if(position==2)
				{
					coefficients[indice]=0;
				}
				else
				{
					coefficients[indice]=-6;
				}
				indice++;
			}
			return coefficients;
		}
		for(byte position:positions)
		{
			if(position==1)
			{
				coefficients[indice]=2;
			}
			else
			{
				coefficients[indice]=-8;
			}
			indice++;
		}
		return coefficients;
	}
	public Vector<Vector<Short>> calcul_annonces_score_joueur(byte joueur)
	{
		Vector<Vector<Short>> scores=new Vector<Vector<Short>>();
		byte joueur2=0;
		for(Vector<Annonce> annonces_joueur:annonces)
		{
			scores.addElement(new Vector<Short>());
			if(joueur2==joueur)
			{
				for(Annonce annonce:annonces_joueur)
				{
					scores.lastElement().addElement(annonce.points());
				}
			}
			else
			{
				for(Annonce annonce:annonces_joueur)
				{
					scores.lastElement().addElement((short) -annonce.points());
				}
			}
			joueur2++;
		}
		return scores;
	}
	public short prime_supplementaire(byte joueur)
	{
		byte nombre_joueurs=getNombreDeJoueurs();
		Vector<Vector<Pli>> plis_adversaires=new Vector<Vector<Pli>>();
		for(byte joueur2=0;joueur2<nombre_joueurs;joueur2++)
		{
			if(joueur!=joueur2)
			{
				plis_adversaires.addElement(plis.get(joueur2));
			}
		}
		int nombre_cartes_plis_adversaire=0;
		for(Vector<Pli> plis_joueur2:plis_adversaires)
		{
			for(Pli pli_joueur:plis_joueur2)
			{
				nombre_cartes_plis_adversaire+=pli_joueur.total();
			}
		}
		if(nombre_cartes_plis_adversaire<2)
		{
			return (short)(new Annonce(Primes.Chelem).points()/2);
		}
		return 0;
	}
	public void calculer_scores_joueurs(byte[] coefficients,short difference_max_double)
	{
		byte nombre_joueurs=getNombreDeJoueurs();
		byte nombre_points_chien=0;
		Vector<Short> scores=getScores();
		for(Carte carte:plis.get(nombre_joueurs).get(0))
		{
			nombre_points_chien+=((CarteTarot)carte).points();
		}
		byte parite=(byte)((difference_max_double+nombre_points_chien)/2*2==difference_max_double+nombre_points_chien?0:1);
		for(byte joueur=0;joueur<nombre_joueurs;joueur++)
		{
			scores.setElementAt((short)(4*(coefficients[joueur]*(25+(difference_max_double+nombre_points_chien+parite)/2))),joueur);
		}
	}
	public short difference_max(short difference_max_double)
	{
		byte nombre_joueurs=getNombreDeJoueurs();
		byte nombre_points_chien=0;
		for(Carte carte:plis.get(nombre_joueurs).get(0))
		{
			nombre_points_chien+=((CarteTarot)carte).points();
		}
		byte parite=(byte)((difference_max_double+nombre_points_chien)/2*2==difference_max_double+nombre_points_chien?0:1);
		return (short)((difference_max_double+nombre_points_chien+parite)/2);
	}
	public void calculer_scores_joueurs(byte[] coefficients,Vector<Vector<Vector<Short>>> points_annonces,short difference_max_double,short[] prime_supplementaire)
	{
		byte nombre_joueurs=getNombreDeJoueurs();
		byte nombre_points_chien=0;
		byte joueur2=0;
		short points_annonces_joueur;
		short points_annonces_autres_joueurs;
		short somme_prime_supplementaire;
		Vector<Short> scores=getScores();
		for(Carte carte:plis.get(nombre_joueurs).get(0))
		{
			nombre_points_chien+=((CarteTarot)carte).points();
		}
		byte parite=(byte)((difference_max_double+nombre_points_chien)/2*2==difference_max_double+nombre_points_chien?0:1);
		for(byte joueur=0;joueur<nombre_joueurs;joueur++)
		{
			points_annonces_joueur=0;
			points_annonces_autres_joueurs=0;
			joueur2=0;
			somme_prime_supplementaire=0;
			for(Vector<Short> annonces_joueur:points_annonces.get(joueur))
			{
				if(joueur!=joueur2)
				{
					for(short points_annonce:annonces_joueur)
					{
						points_annonces_autres_joueurs+=points_annonce;
					}
					somme_prime_supplementaire+=prime_supplementaire[joueur2];
				}
				else
				{
					for(short points_annonce:annonces_joueur)
					{
						points_annonces_joueur+=points_annonce;
					}
				}
				joueur2++;
			}
			scores.setElementAt((short)(4*(coefficients[joueur]*(25+(difference_max_double+nombre_points_chien+parite)/2)+(nombre_joueurs-1)*(points_annonces_joueur+prime_supplementaire[joueur])+points_annonces_autres_joueurs-somme_prime_supplementaire)),joueur);
		}
	}
	/**Renvoie le nombre de points dans les plis du preneur mutlipli&eacute; par 2.*/
	public short score_preneur_plis_double(Pli pli_petit)
	{
		short nbPointsAtt=0;
		boolean contient_excuse=false;
		Vector<Pli> plis_attaque=getPlisAttaque();
		for(Pli pli:plis_attaque)
		{
			for(Carte carte:pli)
			{
				nbPointsAtt+=((CarteTarot)carte).points();
				contient_excuse|=carte.couleur()==0;
			}
		}
		if(pli_petit!=null&&pli_petit.total()==1)
		{
			if(contient_excuse)
			{
				nbPointsAtt--;
			}
			else
			{
				nbPointsAtt++;
			}
		}
		return nbPointsAtt;
	}
	public byte nombre_bouts_preneur()
	{
		byte nombre_bouts=0;
		for(Pli pli:getPlisAttaque())
		{
			for(Carte carte:pli)
			{
				if(((CarteTarot)carte).estUnBout())
				{
					nombre_bouts++;
				}
			}
		}
		return nombre_bouts;
	}
	public short score_necessaire_preneur()
	{
		byte nombre_bouts=nombre_bouts_preneur();
		if(nombre_bouts==0)
		{
			return 56;
		}
		if(nombre_bouts==1)
		{
			return 51;
		}
		if(nombre_bouts==2)
		{
			return 41;
		}
		return 36;
	}
	public short score_preneur_plis(short score_preneur_plis_double,short score_necessaire_preneur)
	{
		short score_preneur_plis=(short)(score_preneur_plis_double/2);
		if(score_preneur_plis>=score_necessaire_preneur)
		{
			if(score_preneur_plis_double%2==1)
			{
				score_preneur_plis++;
			}
		}
		else if(score_preneur_plis+1==score_necessaire_preneur)
		{
			if(!getInfos().get(6).contains("classique")&&!getInfos().get(6).contains("Match nul"))
			{
				if(score_preneur_plis_double%2==1)
				{
					score_preneur_plis++;
				}
			}
		}
		return score_preneur_plis;
	}
	public short base(short score_preneur_plis_double,short difference_score_preneur)
	{
		if(difference_score_preneur>=0)
		{
			return 25;
		}
		if(difference_score_preneur==-1&&score_preneur_plis_double%2==1)
		{
			if(getInfos().get(6).contains("classique"))
			{
				return -25;
			}
			if(getInfos().get(6).contains("Match nul"))
			{
				return 0;
			}
			return 25;
		}
		return -25;
	}
	public short score_preneur_sans_annonces(short difference_score_preneur,short base)
	{
		short score_preneur_sans_annonces=0;
		byte nombre_joueurs=getNombreDeJoueurs();
		if(base!=0)
		{
			score_preneur_sans_annonces=(short)(base+difference_score_preneur);
			if(getAnnonces(preneur).contains(new Annonce(Annonce.petit_au_bout)))
			{
				score_preneur_sans_annonces+=new Annonce(Annonce.petit_au_bout).points();
			}
			if(appele>-1&&getAnnonces(appele).contains(new Annonce(Annonce.petit_au_bout)))
			{
				score_preneur_sans_annonces+=new Annonce(Annonce.petit_au_bout).points();
			}
			for(byte joueur=0;joueur<nombre_joueurs;joueur++)
			{
				if(joueur!=preneur&&joueur!=appele&&getAnnonces(joueur).contains(new Annonce(Annonce.petit_au_bout)))
				{
					score_preneur_sans_annonces-=new Annonce(Annonce.petit_au_bout).points();
				}
			}
		}
		return score_preneur_sans_annonces;
	}
	public byte joueur_petit_au_bout()
	{
		byte nombre_joueurs=getNombreDeJoueurs();
		for(byte joueur=0;joueur<nombre_joueurs;joueur++)
		{
			if(annonces.get(joueur).contains(new Annonce(Annonce.petit_au_bout)))
			{
				return joueur;
			}
		}
		return -1;
	}
	public Vector<Vector<Annonce>> annonces_sans_petit_bout()
	{
		Vector<Vector<Annonce>> annonces_sans_petit_bout=new Vector<Vector<Annonce>>();
		byte nombre_joueurs=getNombreDeJoueurs();
		for(byte joueur=0;joueur<nombre_joueurs;joueur++)
		{
			annonces_sans_petit_bout.addElement(new Vector<Annonce>());
			for(Annonce annonce:annonces.get(joueur))
			{
				if(!annonce.equals(new Annonce(Annonce.petit_au_bout)))
				{
					annonces_sans_petit_bout.get(joueur).addElement(annonce);
				}
			}
		}
		return annonces_sans_petit_bout;
	}
	public Vector<Vector<Short>> calcul_annonces_score_preneur(short score_preneur_sans_annonces,Vector<Vector<Annonce>> annonces_sans_petit_bout)
	{
		Vector<Vector<Short>> scores=new Vector<Vector<Short>>();
		byte joueur=0;
		Vector<Pli> plis_defense=getPlisDefense();
		for(Vector<Annonce> annonces_joueur:annonces_sans_petit_bout)
		{
			scores.addElement(new Vector<Short>());
			if(joueur==preneur)
			{
				for(Annonce annonce:annonces_joueur)
				{
					if(annonce.toString().endsWith(Annonce.poignee))
					{
						if(score_preneur_sans_annonces>0)
						{
							scores.lastElement().addElement(annonce.points());
						}
						else
						{
							scores.lastElement().addElement((short) (-annonce.points()));
						}
					}
					else if(!annonce.equals(new Annonce(Primes.Chelem))||plis_defense.isEmpty()||plis_defense.lastElement().getNumero()==0)
					{
						scores.lastElement().addElement(annonce.points());
					}
					else if((plis_defense.size()==1||plis_defense.size()==2&&plis_defense.get(0).getNumero()==0)&&plis_defense.lastElement().total()==1)
					{
						scores.lastElement().addElement(annonce.points());
					}
					else
					{
						scores.lastElement().addElement((short) (-annonce.points()/2));
					}
				}
			}
			else if(joueur==appele)
			{
				for(Annonce annonce:annonces_joueur)
				{
					if(annonce.toString().endsWith(Annonce.poignee))
					{
						if(score_preneur_sans_annonces>0)
						{
							scores.lastElement().addElement(annonce.points());
						}
						else
						{
							scores.lastElement().addElement((short) -annonce.points());
						}
					}
					else
					{
						scores.lastElement().addElement(annonce.points());
					}
				}
			}
			else
			{
				for(Annonce annonce:annonces_joueur)
				{
					if(annonce.toString().endsWith(Annonce.poignee))
					{
						if(score_preneur_sans_annonces>0)
						{
							scores.lastElement().addElement(annonce.points());
						}
						else
						{
							scores.lastElement().addElement((short) -annonce.points());
						}
					}
					else
					{
						scores.lastElement().addElement((short) -annonce.points());
					}
				}
			}
			joueur++;
		}
		return scores;
	}
	public short[] primes_supplementaires()
	{
		short[] primes_supplementaires=new short[2];
		Vector<Pli> plis_defense=getPlisDefense();
		Vector<Pli> plis_attaque=getPlisAttaque();
		if(contrat.equals(new Contrat(Primes.Chelem.toString())))
		{
			if(plis_defense.isEmpty()||plis_defense.lastElement().getNumero()==0)
			{
				primes_supplementaires[0]=new Annonce(Primes.Chelem).points();
			}
			else if((plis_defense.size()==1||plis_defense.size()==2&&plis_defense.get(0).getNumero()==0)&&plis_defense.lastElement().total()==1)
			{
				primes_supplementaires[0]=new Annonce(Primes.Chelem).points();
			}
			else
			{
				primes_supplementaires[0]=(short) (-new Annonce(Primes.Chelem).points()/2);
				if(plis_attaque.isEmpty()||plis_attaque.lastElement().getNumero()==0)
				{
					primes_supplementaires[1]=(short) (new Annonce(Primes.Chelem).points()/2);
				}
				else if((plis_attaque.size()==1||plis_attaque.size()==2&&plis_attaque.get(0).getNumero()==0)&&plis_attaque.lastElement().total()==1)
				{
					primes_supplementaires[1]=(short) (new Annonce(Primes.Chelem).points()/2);
				}
			}
			return primes_supplementaires;
		}
		if(plis_defense.isEmpty()||plis_defense.lastElement().getNumero()==0)
		{
			if(!getAnnonces(preneur).contains(new Annonce(Primes.Chelem)))
			{
				primes_supplementaires[0]=(short) (new Annonce(Primes.Chelem).points()/2);
			}
		}
		else if((plis_defense.size()==1||plis_defense.size()==2&&plis_defense.get(0).getNumero()==0)&&plis_defense.lastElement().total()==1)
		{
			if(!getAnnonces(preneur).contains(new Annonce(Primes.Chelem)))
			{
				primes_supplementaires[0]=(short) (new Annonce(Primes.Chelem).points()/2);
			}
		}
		else
		{
			if(plis_attaque.isEmpty()||plis_attaque.lastElement().getNumero()==0)
			{
				primes_supplementaires[1]=(short) (new Annonce(Primes.Chelem).points()/2);
			}
			else if((plis_attaque.size()==1||plis_attaque.size()==2&&plis_attaque.get(0).getNumero()==0)&&plis_attaque.lastElement().total()==1)
			{
				primes_supplementaires[1]=(short) (new Annonce(Primes.Chelem).points()/2);
			}
		}
		return primes_supplementaires;
	}
	public short somme_temporaire(short score_preneur_sans_annonces,Vector<Vector<Short>> scores,short[] primes_supplementaires)
	{
		short somme_temporaire=0;
		for(Vector<Short> score_joueur:scores)
		{
			for(short score:score_joueur)
			{
				somme_temporaire+=score;
			}
		}
		somme_temporaire+=primes_supplementaires[0]-primes_supplementaires[1];
		if(score_preneur_sans_annonces!=0)
		{
			return (short) (contrat.force()*score_preneur_sans_annonces+somme_temporaire);
		}
		return 0;
	}
	public byte[][] coefficients_repartition()
	{
		byte[][] coefficients_repartition;
		byte nombre_joueurs=getNombreDeJoueurs();
		if(appele<0)
		{
			coefficients_repartition=new byte[2][1];
			coefficients_repartition[0][0]=(byte)(nombre_joueurs-1);
			coefficients_repartition[1][0]=-1;
		}
		else
		{
			if(nombre_joueurs==4)
			{
				coefficients_repartition=new byte[3][2];
				coefficients_repartition[0][0]=3;
				coefficients_repartition[0][1]=2;
				coefficients_repartition[1][0]=1;
			}
			else
			{
				coefficients_repartition=new byte[3][1];
				coefficients_repartition[0][0]=2;
				coefficients_repartition[1][0]=1;
			}
			coefficients_repartition[2][0]=-1;
		}
		return coefficients_repartition;
	}
	public byte gagne_nul_perd(short score_preneur_sans_annonces)
	{
		if(preneur==0||appele==0)
		{
			if(score_preneur_sans_annonces>0)
			{
				return 1;
			}
			if(score_preneur_sans_annonces==0)
			{
				return 0;
			}
			return-1;
		}
		if(score_preneur_sans_annonces<0)
		{
			return 1;
		}
		if(score_preneur_sans_annonces==0)
		{
			return 0;
		}
		return-1;
	}
	public void calculer_scores(byte[][] coefficients_repartition,short somme_temporaire,short score_preneur_sans_annonces)
	{
		byte nombre_joueurs=getNombreDeJoueurs();
		byte parite;
		Vector<Short> scores=getScores();
		if(somme_temporaire==0)
		{
			for(byte joueur=0;joueur<nombre_joueurs;joueur++)
			{
				scores.setElementAt((short) 0,joueur);
			}
		}
		else if(coefficients_repartition.length==2)
		{
			for(byte joueur=0;joueur<nombre_joueurs;joueur++)
			{
				if(joueur==preneur)
				{
					scores.setElementAt((short) (coefficients_repartition[0][0]*somme_temporaire),joueur);
				}
				else
				{
					scores.setElementAt((short) (coefficients_repartition[1][0]*somme_temporaire),joueur);
				}
			}
		}
		else if(coefficients_repartition[0].length==1)
		{
			for(byte joueur=0;joueur<nombre_joueurs;joueur++)
			{
				if(joueur==preneur)
				{
					scores.setElementAt((short) (coefficients_repartition[0][0]*somme_temporaire),joueur);
				}
				else if(joueur==appele)
				{
					scores.setElementAt((short) (coefficients_repartition[1][0]*somme_temporaire),joueur);
				}
				else
				{
					scores.setElementAt((short) (coefficients_repartition[2][0]*somme_temporaire),joueur);
				}
			}
		}
		else
		{
			parite=(byte)(somme_temporaire/2*2==somme_temporaire?0:1);
			for(byte joueur=0;joueur<nombre_joueurs;joueur++)
			{
				if(joueur==preneur)
				{
					if(score_preneur_sans_annonces>0)
					{
						scores.setElementAt((short)((coefficients_repartition[0][0]*somme_temporaire+parite)/coefficients_repartition[0][1]),joueur);
					}
					else
					{
						scores.setElementAt((short)((coefficients_repartition[0][0]*somme_temporaire-parite)/coefficients_repartition[0][1]),joueur);
					}
				}
				else if(joueur==appele)
				{
					if(score_preneur_sans_annonces>0)
					{
						scores.setElementAt((short)((coefficients_repartition[1][0]*somme_temporaire-parite)/coefficients_repartition[0][1]),joueur);
					}
					else
					{
						scores.setElementAt((short)((coefficients_repartition[1][0]*somme_temporaire+parite)/coefficients_repartition[0][1]),joueur);
					}
				}
				else
				{
					scores.setElementAt((short) (coefficients_repartition[2][0]*somme_temporaire),joueur);
				}
			}
		}
	}
	public MainTarot empiler()
	{
		MainTarot m=new MainTarot();
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
				if(pli.getNumero()>0)
				{
					getDistribution().main(pli.joueurAyantJoue(carte,nombre_joueurs,pli_petit)).ajouter(carte);
				}
			}
		}
		if(preneur>-1)
		{
			getDistribution().main(preneur).ajouterCartes(plis_faits.get(0).getCartes());
			getDistribution().main(preneur).supprimerCartes(getDistribution().derniereMain());
		}
		for(joueur=0;joueur<nombre_joueurs;joueur++)
		{
			((MainTarot)getDistribution().main(joueur)).trier(couleurs,sens);
		}
	}
	public void restituerMainsDepartRejouerDonne(Vector<Pli> plis_faits,byte nombre_joueurs)
	{
		byte joueur;
		Pli pli_petit=pliLePlusPetit(plis_faits);
		for(joueur=0;joueur<nombre_joueurs;joueur++)
		{
			getDistribution().main(joueur).supprimerCartes();
		}
		for(Pli pli:plis_faits)
		{
			for(Carte carte:pli)
			{
				if(pli.getNumero()>0)
				{
					getDistribution().main(pli.joueurAyantJoue(carte,nombre_joueurs,pli_petit)).ajouter(carte);
				}
			}
		}
		if(preneur>-1)
		{
			getDistribution().main(preneur).ajouterCartes(plis_faits.get(0).getCartes());
			getDistribution().main(preneur).supprimerCartes(getDistribution().derniereMain());
		}
	}
	public byte numero_pli(Pli pli_petit,byte choix)
	{
		if(pli_petit!=null&&pli_petit.total()==1)
		{
			if(choix>=pli_petit.getNumero())
			{
				return (byte) (choix+1);
			}
		}
		return choix;
	}
	public void restituerMains(Vector<Pli> plis_faits,Pli pli_petit,byte nombre_joueurs,String couleurs,String sens,byte numero_pli)
	{
		byte joueur;
		for(joueur=0;joueur<nombre_joueurs;joueur++)
		{
			getDistribution().main(joueur).supprimerCartes();
		}
		Vector<Pli> plis_faits2=new Vector<Pli>();
		plis_faits2.addAll(plis_faits);
		for(Pli pli:plis_faits2)
		{
			Pli pli_excuse=plis_faits2.get(pli.getNumero());
			byte entameur3=pli.getEntameur();
			if(pli.total()==nombre_joueurs-1)
			{
				byte entameur2=plis_faits2.get(pli.getNumero()+1).getEntameur();
				Carte excuse=plis_faits2.get(pli.getNumero()+1).carte(0);
				if(entameur2>=entameur3)
				{
					pli_excuse.getCartes().ajouter(excuse,entameur2-entameur3);
				}
				else
				{
					pli_excuse.getCartes().ajouter(excuse,entameur2-entameur3+nombre_joueurs);
				}
			}
			if(pli.total()>1)
			{
				if(pli.getNumero()>numero_pli&&pli.getNumero()>0)
				{
					if(pli.total()==nombre_joueurs)
					{
						for(Carte carte:pli)
						{
							getDistribution().main(pli.joueurAyantJoue(carte,nombre_joueurs,pli_petit)).ajouter(carte);
						}
					}
					else
					{
						for(Carte carte:pli_excuse)
						{
							getDistribution().main(pli_excuse.joueurAyantJoue(carte,nombre_joueurs,pli_petit)).ajouter(carte);
						}
					}
				}
			}
		}
		if(preneur>-1)
		{
			if(numero_pli<0)
			{
				getDistribution().main(preneur).ajouterCartes(plis_faits.get(0).getCartes());
				getDistribution().main(preneur).supprimerCartes(getDistribution().derniereMain());
			}
		}
		for(joueur=0;joueur<nombre_joueurs;joueur++)
		{
			((MainTarot)getDistribution().main(joueur)).trier(couleurs,sens);
		}
	}
	public void restituerMains(Vector<Pli> plis_faits,Pli pli_petit,byte nombre_joueurs,String couleurs,String sens,byte numero_pli,byte numero_carte)
	{
		byte joueur;
		for(joueur=0;joueur<nombre_joueurs;joueur++)
		{
			getDistribution().main(joueur).supprimerCartes();
		}
		Vector<Pli> plis_faits2=new Vector<Pli>();
		plis_faits2.addAll(plis_faits);
		for(Pli pli:plis_faits2)
		{
			Pli pli_excuse=plis_faits2.get(pli.getNumero());
			byte entameur3=pli.getEntameur();
			if(pli.total()==nombre_joueurs-1)
			{
				byte entameur2=plis_faits2.get(pli.getNumero()+1).getEntameur();
				Carte excuse=plis_faits2.get(pli.getNumero()+1).carte(0);
				if(entameur2>=entameur3)
				{
					pli_excuse.getCartes().ajouter(excuse,entameur2-entameur3);
				}
				else
				{
					pli_excuse.getCartes().ajouter(excuse,entameur2-entameur3+nombre_joueurs);
				}
			}
			if(pli.total()>1)
			{
				if(pli.getNumero()>numero_pli&&pli.getNumero()>0)
				{
					if(pli.total()==nombre_joueurs)
					{
						for(Carte carte:pli)
						{
							getDistribution().main(pli.joueurAyantJoue(carte,nombre_joueurs,pli_petit)).ajouter(carte);
						}
					}
					else
					{
						for(Carte carte:pli_excuse)
						{
							getDistribution().main(pli_excuse.joueurAyantJoue(carte,nombre_joueurs,pli_petit)).ajouter(carte);
						}
					}
				}
				else if(pli.getNumero()==numero_pli&&pli.getNumero()>0)
				{
					byte indice=0;
					if(pli.total()==nombre_joueurs)
					{
						for(Carte carte:pli)
						{
							if(indice>numero_carte)
							{
								getDistribution().main(pli.joueurAyantJoue(carte,nombre_joueurs,pli_petit)).ajouter(carte);
							}
							indice++;
						}
					}
					else
					{
						for(Carte carte:pli_excuse)
						{
							if(indice>numero_carte)
							{
								getDistribution().main(pli_excuse.joueurAyantJoue(carte,nombre_joueurs,pli_petit)).ajouter(carte);
							}
							indice++;
						}
					}
				}
			}
		}
		if(preneur>-1)
		{
			if(numero_pli<0)
			{
				getDistribution().main(preneur).ajouterCartes(plis_faits.get(0).getCartes());
				getDistribution().main(preneur).supprimerCartes(getDistribution().derniereMain());
			}
		}
		for(joueur=0;joueur<nombre_joueurs;joueur++)
		{
			((MainTarot)getDistribution().main(joueur)).trier(couleurs,sens);
		}
	}
}