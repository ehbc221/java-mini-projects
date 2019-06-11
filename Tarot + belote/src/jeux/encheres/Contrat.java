package jeux.encheres;
import java.io.*;
import jeux.enumerations.*;
/**Pour le tarot et la belote*/
public class Contrat implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final String chelem=Primes.Chelem.toString();
	/**Nom du contrat a annoncer*/
	private String nom;
	public Contrat(String pnom)
	{
		nom=pnom;
	}
	public Contrat(EncheresBelote pnom)
	{
		nom=pnom.toString();
	}
	public Contrat(EncheresTarot pnom)
	{
		nom=pnom.toString();
	}
	public int coefficients()
	{
		if(nom.equals(EncheresTarot.Petite.toString()))
			return 1;
		if(nom.equals(EncheresTarot.Garde.toString()))
			return 2;
		if(nom.equals(EncheresTarot.Garde_sans.toString()))
			return 4;
		if(nom.equals(EncheresTarot.Garde_contre.toString()))
			return 6;
		if(nom.equals(chelem))
			return 10;
		if(nom.equals(EncheresBelote.Couleur.toString())||nom.equals(EncheresBelote.Autre_couleur.toString()))
			return 1;
		if(nom.equals(EncheresBelote.Sans_atout.toString()))
			return 1;
		if(nom.equals(EncheresBelote.Tout_atout.toString()))
			return 1;
		return 0;
	}
	private boolean estPlusFortQue(Contrat c)
	{
		return force()>c.force();
	}
	public int force()
	{
		if(nom.equals(EncheresTarot.Petite.toString()))
			return 1;
		if(nom.equals(EncheresTarot.Garde.toString()))
			return 2;
		if(nom.equals(EncheresTarot.Garde_sans.toString()))
			return 3;
		if(nom.equals(EncheresTarot.Garde_contre.toString()))
			return 4;
		if(nom.equals(chelem))
			return 5;
		if(nom.equals(EncheresBelote.Couleur.toString())||nom.equals(EncheresBelote.Autre_couleur.toString()))
			return 1;
		if(nom.equals(EncheresBelote.Sans_atout.toString()))
			return 3;
		if(nom.equals(EncheresBelote.Tout_atout.toString()))
			return 4;
		return 0;
	}
	public boolean estDemandable(Contrat c)
	{
		if(force()==0)
			return true;
		return estPlusFortQue(c);
	}
	/**Deux contrats sont egaux si et seulement si leur nom le sont*/
	public boolean equals(Object o)
	{
		if(o==null)
			return false;
		if(!(o instanceof Contrat))
			return false;
		return nom.equals(((Contrat)o).nom);
	}
	/**Ecrit le chaine de nom*/
	public String toString()
	{
		return nom;
	}
}