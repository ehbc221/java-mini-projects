package jeux.cartes;
import jeux.enumerations.Monotonie;
/**
 *
 */
public class CarteTarot extends Carte implements Triable{
	private static final long serialVersionUID = 1L;
	private final static byte[] ordreAtout=new byte[22];
	private final static byte[] ordreCouleur=new byte[14];
	private final static byte[] couleurCartes=new byte[78];
	private final static byte[] valeurCartes=new byte[78];
	static
	{
		for (byte i = 1; i < 23; i++)
			ordreAtout[i-1]=i;
		for (byte i = 1; i < 15; i++)
			ordreCouleur[i-1]=i;
		//L'Excuse
		couleurCartes[0]=0;
		valeurCartes[0]=8;
		//Les atouts
		for (int i = 1; i < 22; i++) {
			couleurCartes[i]=1;
			valeurCartes[i]=(byte)(22-i);
		}
		//Les cartes de couleurs
		for (int i = 1; i < 15; i++) {
			for (int j = 2; j < 6; j++) {
				couleurCartes[21+i+(j-2)*14]=(byte) j;
				valeurCartes[21+i+(j-2)*14]=(byte)(15-i);
			}
		}
	}
	public CarteTarot(byte valeur, byte couleur) {
		super(valeur, couleur);
	}
	public CarteTarot(byte valeur)
	{
		this(valeurCartes[valeur],couleurCartes[valeur]);
	}
	public byte forceTri()
	{
		if(couleur()==0)
			return ordreAtout[21];
		if(couleur()==1)
			return ordreAtout[valeur()-1];
		return ordreCouleur[valeur()-1];
	}
	/**Appele lors de la fin d'un pli pour determiner le ramasseur
	 * mais aussi pour dire qui est en train de prendre la main du pli qui est en cours d'etre joue
	 * si ce pli n'est pas reduit a l'Excuse
	 * @param couleur_demande couleur demandee du pli*/
	public byte forceJeu(byte couleur_demande)
	{
		if(couleur()==1)
		{//L'atout est plus fort que n'importe quelle couleur
			return (byte)(valeur()+14);
		}
		/*Maintenant on traite le cas d'une Excuse et des cartes de couleur*/
		if(couleur_demande==couleur())
		{
			//Une carte de la meme couleur que celle de la carte entamee et autre que l'Excuse
			return valeur();
		}
		//L'Excuse n'est pas entamee dans un pli
			//Les cartes qui ne sont pas de l'atout ne permettent pas de faire un pli ou l'atout est demande
		//Les cartes de couleur qui n'ont pas la meme couleur que celle entamee
		//ne permettent pas de faire un pli
		return 0;
	}
	/**Retourne le nombre de points d'une carte (multiplie par 2)
	 * au tarot, on evite d utiliser les flottants car la valeur de chaque carte au tarot est un nombre non entier de points*/
	public byte points()
	{
		if(estUnBout())
			return 9;
		if(couleur()==1)
			return 1;
		if(valeur()==14)
			return 9;
		if(valeur()==13)
			return 7;
		if(valeur()==12)
			return 5;
		if(valeur()==11)
			return 3;
		return 1;
	}
	public boolean estUnBout()
	{
		return couleur()==0||couleur()==1&&valeur()==1||valeur()==21;
	}
	public byte forceValeurDansUnTri(String sens,String ordre)
	{
		if(sens.contains(Monotonie.Decroissant.toString()))
		{
			if(couleur()<2)
				return (byte)(23-forceTri());
			return (byte)(15-forceTri());
		}
		return forceTri();
	}
	/**Par defaut sens vaut Decroissant*/
	public byte forceValeurDansUnTri(String ordre)
	{
		if(couleur()<2)
			return (byte)(23-forceTri());
		return (byte)(15-forceTri());
	}
	/**Par defaut ordre vaut Atout*/
	public byte forceValeurDansUnTri()
	{
		if(couleur()<2)
			return (byte)(23-this.forceTri());
		return (byte)(15-this.forceTri());
	}
	public boolean vientAvant(Triable c,String sens,String ordre,String couleurs)
	{
		byte  forceCouleur=forceCouleurDansUnTri(couleurs);
		byte  forceCouleur2=((Carte)c).forceCouleurDansUnTri(couleurs);
		byte  forceValeur=forceValeurDansUnTri(sens,ordre);
		byte  forceValeur2=c.forceValeurDansUnTri(sens,ordre);
		if(forceCouleur<forceCouleur2)
			return true;
		if(forceCouleur==forceCouleur2)
			return forceValeur<forceValeur2;
		return false;
	}
}