package jeux.cartes;
import java.io.*;
import java.util.*;
import jeux.Lettre;
import jeux.Parametres;
import jeux.enumerations.*;
/**
 *
 */
public abstract class Carte implements Serializable{
	private static final long serialVersionUID = 1L;
	/**Numero de couleur de la carte (0: Excuse Tarot,1: Atout Tarot,2: Coeur,3: Pique,4: Carreau,5: Tr&egrave;fle)*/
	private byte couleur;
	/**Numero de valeur de la carte (Numeros pour les atouts du tarot, et pour les cartes chiffrees, position pour les figures avec Roi, Dame, Cavalier, Valet)*/
	private byte valeur;
	Carte(byte pvaleur,byte pcouleur)
	{
		valeur=pvaleur;
		couleur=pcouleur;
	}
	public byte couleur()
	{
		return couleur;
	}
	public byte valeur()
	{
		return valeur;
	}
	public static String chaine_couleur(byte couleur)
	{
		if(couleur>0)
		{
			return Couleur.values()[couleur-1].toString();
		}
		return Couleur.Atout.toString();
	}
	public static String chaine_valeur(byte valeur)
	{
		if(valeur<11)
		{
			return valeur+Lettre.ch_v;
		}
		return Figure.values()[15-valeur].toString();
	}
	public static byte valeur_chaine(String valeur)
	{
		return (byte)(15-Figure.valueOf(valeur).ordinal());
	}
	byte forceCouleurDansUnTri(String couleurs)
	{
		Vector<String> vs=new Vector<String>();
		for (String chaine_couleur:couleurs.split(Parametres.separateur_tiret_slash))
		{
			vs.addElement(chaine_couleur);
		}
		return (byte) (vs.indexOf(chaine_couleur(couleur))+1);
	}
	/**Deux cartes sont egales si et seulement si elles ont meme couleur et meme valeur*/
	public boolean equals(Object o)
	{
		if(o==null)
			return false;
		if(!(o instanceof Carte))
			return false;
		return ((Carte)o).valeur==valeur&&((Carte)o).couleur==couleur;
	}
	public String toString()
	{
		if(couleur==0)
			return Figure.values()[0].toString();
		else if(couleur==1)
			return valeur+" d'"+chaine_couleur(couleur);
		return chaine_valeur(valeur)+" de "+chaine_couleur(couleur);
	}
}