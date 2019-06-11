package jeux.plis;
import java.util.*;
import jeux.cartes.*;
import jeux.encheres.*;
import jeux.mains.*;
/*TODO president*/
/**Classe constituee d'une main, d'un entameur
 * Les raisons pour lesquelles cette classe est abstraite sont que:<br>
 * <ol><li>Les attributs des objets des classes concretes ne sont pas les memes</li>
 * <li>Les methides total(),ajouterCartes(Pli p) et getRamasseur(Partie p) doivent etre redefinies</li></ol>*/
public class Pli implements java.io.Serializable,Iterable<Carte>{
	private static final long serialVersionUID = 1L;
	/**Nombre total de plis faits pendant la partie
	 * Ce nombre est remis a zero lors d'une nouvelle partie*/
	public static byte nombreTotal;
	/**Entameur du pli*/
	private byte entameur;
	/**Numero du pli courant*/
	private byte numero;
	/**m est l'ensemble de cartes jouees pendant le pli a la belote ou au tarot*/
	private Main m;
	/**Constructeur permettant de creer un pli avec un entameur et un numero qui sert a pouvoir revoir les plis qui sont faits*/
	public Pli(byte pentameur)
	{
		entameur=pentameur;
		numero=nombreTotal;
		nombreTotal++;
	}
	public Pli(Main pm,byte pentameur)
	{
		this(pentameur);
		m=pm;
	}
	/**Retourne le numero du pli*/
	public byte getNumero()
	{
		return numero;
	}
	/**Retourne l'entameur du pli*/
	public byte getEntameur()
	{
		return entameur;
	}
	//abstract int total();
	public Main getCartes()
	{
		return m;
	}
	/**Indique le joueur qui doit ramasser le pli &agrave; la belote
	 * pour entamer l'eventuel suivant
	 * @param nombre_joueurs nombre de joueurs qui jouent a cette partie
	 * @param contrat contrat de la partie
	 * @param couleur_atout la couleur d'atout si elle existe*/
	public byte getRamasseurBelote(byte nombre_joueurs, Contrat contrat, byte couleur_atout)
	{
		byte ramasseur;
		byte max=0;
		byte i=0;
		byte position=0;
		byte demande=couleurDemandee();
		byte val_force;
		for(Carte c:m)
		{
			val_force=((CarteBelote)c).force(couleur_atout,demande,contrat);
			if(val_force>max)
			{
				position=i;
				max=val_force;
			}
			i++;
		}
		ramasseur=position;//Ramasseur est la position du ramasseur par rapport a l'entameur
		//On calcule la position de ramasseur par rapport a celle de l'utilisateur
		return (byte) ((ramasseur+getEntameur())%nombre_joueurs);
		//On renvoie le ramasseur du pli courant
	}
	/**Indique le joueur qui doit ramasser le pli au tarot
	 * pour entamer l'eventuel suivant
	 * @param nombre_joueurs nombre de joueurs qui jouent a cette partie*/
	public byte getRamasseurTarot(byte nombre_joueurs)
	{
		byte ramasseur;
		byte max=0;
		byte i=0;
		byte position=0;
		byte demande=couleurDemandee();
		byte val_force;
		for(Carte c:m)
		{
			val_force=((CarteTarot)c).forceJeu(demande);
			if(val_force>max)
			{
				position=i;
				max=val_force;
			}
			i++;
		}
		ramasseur=position;//Ramasseur est la position du ramasseur par rapport a l'entameur
		//On calcule la position de ramasseur par rapport a celle de l'utilisateur
		return (byte) ((ramasseur+getEntameur())%nombre_joueurs);
		//On renvoie le ramasseur du pli courant
	}
	public Carte carte(int i)
	{
		return m.carte(i);
	}
	public byte joueurAyantJoue(Carte c,byte nombreDeJoueurs,Pli pliLePlusPetitEnTaille)
	{
		if(!contient(c))
			return -1;
		byte position=(byte)m.position(c);
		if(pliLePlusPetitEnTaille!=null&&pliLePlusPetitEnTaille.total()==1)
		{
			if(total()<nombreDeJoueurs)
			{
				if(getEntameur()<pliLePlusPetitEnTaille.getEntameur())
				{
					if(position+getEntameur()>=pliLePlusPetitEnTaille.getEntameur())
						return (byte)((position+getEntameur()+1)%nombreDeJoueurs);
					return (byte)((position+getEntameur())%nombreDeJoueurs);
				}
				if(position+getEntameur()>=pliLePlusPetitEnTaille.getEntameur()+nombreDeJoueurs)
					return (byte)((position+getEntameur()+1)%nombreDeJoueurs);
				return (byte)((position+getEntameur())%nombreDeJoueurs);
			}
		}
		return (byte)((position+getEntameur())%nombreDeJoueurs);
	}
	public Vector<Byte> joueursAyantJoueAvant(byte pnumero,byte nombreDeJoueurs)
	{
		Vector<Byte> joueurs=new Vector<Byte>();
		int nombre_joueurs=getEntameur()<=pnumero?pnumero-getEntameur():nombreDeJoueurs+pnumero-getEntameur();
		for(int joueur_avant=0;joueur_avant<nombre_joueurs;joueur_avant++)
		{
			joueurs.addElement((byte)((joueur_avant+getEntameur())%nombre_joueurs));
		}
		return joueurs;
	}
	/**Retourne la carte du joueur de variable joueur en fonction du nombre de joueurs et du pli ayant la plus petite longueur*/
	public Carte carteDuJoueur(byte joueur,byte nombreDeJoueurs,Pli pliLePlusPetitEnTaille)
	{
		if(pliLePlusPetitEnTaille!=null&&pliLePlusPetitEnTaille.total()==1)
		{
			if(total()<nombreDeJoueurs)
			{//Pli separe
				if(pliLePlusPetitEnTaille.getEntameur()==joueur)
					return pliLePlusPetitEnTaille.carte(0);
				if(getEntameur()<pliLePlusPetitEnTaille.getEntameur())
				{
					if(joueur>=getEntameur())
					{
						if(joueur<pliLePlusPetitEnTaille.getEntameur())
							return carte(joueur-getEntameur());
						return carte(joueur-getEntameur()-1);
					}
					return carte(joueur-getEntameur()+nombreDeJoueurs-1);
				}
				else if(getEntameur()>pliLePlusPetitEnTaille.getEntameur())
				{
					if(joueur>=getEntameur())
						return carte(joueur-getEntameur());
					if(joueur<pliLePlusPetitEnTaille.getEntameur())
							return carte(joueur-getEntameur()+nombreDeJoueurs);
					return carte(joueur-getEntameur()-1+nombreDeJoueurs);
				}
				if(joueur>getEntameur())
				{
					return carte(joueur-getEntameur()-1);
				}
				return carte(joueur-getEntameur()+nombreDeJoueurs-1);
			}
		}
		if(total()<nombreDeJoueurs)
		{//Pli en cours
			if(joueur>=getEntameur())
			{
				if(joueur-getEntameur()+1>total())
					return null;
				return carte(joueur-getEntameur());
			}
			if(joueur-getEntameur()+1+nombreDeJoueurs>total())
				return null;
			return carte(joueur+nombreDeJoueurs-getEntameur());
		}
		//Pli non separe
		if(joueur>=getEntameur())
			return carte(joueur-getEntameur());
		return carte(joueur-getEntameur()+nombreDeJoueurs);
	}
	/**Retourne la couleur demandee du pli*/
	public byte couleurDemandee()
	{
		if(estVide())
			return -1;
		if(carte(0).couleur()>0)
			return carte(0).couleur();
		if(total()>1)
			return carte(1).couleur();
		return-1;
	}
	/**Retourne l'ensemble des joueurs qui coupent ce pli<br>
	 * <ol><li>si la couleur demandee est de l'atout alors on cherche l'ensemble des joueurs n'ayant pas joue de l'atout(Excuse incluse)</li>
	 * <li>sinon on cherche les joueurs ayant joue de l'atout sur une couleur</li></ol>
	 * Ces joueurs sont classes par ordre chronologique de jeu*/
	public Vector<Byte> joueurs_coupes(byte nombreDeJoueurs,Pli pliLePlusPetit,byte couleur_atout)
	{
		Vector<Byte> coupes=new Vector<Byte>();
		byte couleur;
		if(total()<2)
			return coupes;
		couleur=couleurDemandee();
		if(couleur==couleur_atout)
			for(byte carte=0;carte<total();carte++)
			{//carte est un indice de position de carte dans le pli
				Carte c=carte(carte);
				if(c.couleur()!=couleur_atout)
				{
					coupes.addElement(joueurAyantJoue(c,nombreDeJoueurs,pliLePlusPetit));
				}
			}
		else
			for(byte carte=0;carte<total();carte++)
			{//carte est un indice de position de carte dans le pli
				Carte c=carte(carte);
				if(c.couleur()==couleur_atout)
				{
					coupes.addElement(joueurAyantJoue(c,nombreDeJoueurs,pliLePlusPetit));
				}
			}
		return coupes;
	}
	public Vector<Byte> joueurs_defausses(byte nombreDeJoueurs,Pli pliLePlusPetit, byte couleur_atout)
	{
		Vector<Byte> coupes=new Vector<Byte>();
		byte couleur;
		if(total()<2)
			return coupes;
		couleur=couleurDemandee();
		if(couleur==couleur_atout)
			for(byte carte=0;carte<total();carte++)
			{//carte est un indice de position de carte dans le pli
				Carte c=carte(carte);
				if(c.couleur()!=couleur_atout)
				{
					coupes.addElement(joueurAyantJoue(c,nombreDeJoueurs,pliLePlusPetit));
				}
			}
		else
			for(byte carte=0;carte<total();carte++)
			{//carte est un indice de position de carte dans le pli
				Carte c=carte(carte);
				if(c.couleur()!=couleur_atout&&c.couleur()!=couleur)
				{
					coupes.addElement(joueurAyantJoue(c,nombreDeJoueurs,pliLePlusPetit));
				}
			}
		return coupes;
	}
	public double points_pli_tarot()
	{
		byte nombre_points=0;
		for(Carte carte:this)
		{
			nombre_points+=((CarteTarot)carte).points();
		}
		return nombre_points/2.0;
	}
	public double points_pli_belote(Contrat contrat,Carte carteAppelee)
	{
		byte nombre_points=0;
		for(Carte carte:this)
		{
			nombre_points+=((CarteBelote)carte).points(contrat,carteAppelee);
		}
		return nombre_points/2.0;
	}
	/**Retourne vrai si et seulement si la carte est jouee dans ce pli*/
	public boolean contient(Carte c)
	{
		return m.contient(c);
	}
	/**Si tous les joueurs ne joue qu'une carte alors total() renvoie le nombre de cartes du pli
	 * sinon il renvoie le nombre total de cartes divise par le nombres de cartes jouees par joueur pour le pli*/
	public int total()
	{
		return m.total();
	}
	public boolean estVide()
	{
		return m.estVide();
	}
	/**Permet d'iterer carte par carte dans l'ordre dans lesquelles elles sont jouees le pli*/
	public Iterator<Carte> iterator() {
		return m.iterator();
	}
}