package jeux.encheres;
import java.io.*;
import jeux.enumerations.*;
/**
 *
 */
public class Annonce implements Serializable{
	private static final long serialVersionUID = 1L;
	private String nom;
	public static final String petit_au_bout="Petit au bout";
	public static final String belote_rebelote="Belote rebelote";
	public static final String dix_de_der="Dix de der";
	public static final String poignee=Poignees.Poignee.toString();
	public Annonce(String pnom)
	{
		nom=pnom;
	}
	public Annonce(Poignees pnom)
	{
		nom=pnom.toString();
	}
	public Annonce(Primes pnom)
	{
		nom=pnom.toString();
	}
	public Annonce(Miseres pnom)
	{
		nom=pnom.toString();
	}
	/**Retourne le nombre de points
	 * pour une annonce donnee*/
	public short points()
	{
		if(equals(new Annonce(petit_au_bout)))
			return 10;
		if(equals(new Annonce(dix_de_der)))
			return 10;
		if(equals(new Annonce(belote_rebelote)))
			return 20;
		if(equals(new Annonce(Poignees.Poignee)))
			return 20;
		if(equals(new Annonce(Poignees.Double_Poignee)))
			return 30;
		if(equals(new Annonce(Poignees.Triple_Poignee)))
			return 40;
		if(equals(new Annonce(Miseres.Atout)))
			return 10;
		if(equals(new Annonce(Miseres.Cartes_basses)))
			return 20;
		if(equals(new Annonce(Miseres.Couleur)))
			return 30;
		if(equals(new Annonce(Miseres.Figure)))
			return 5;
		if(equals(new Annonce(Miseres.Tete)))
			return 10;
		if(equals(new Annonce(Primes.Chelem)))
			return 400;
		return 0;
	}
	public boolean equals(Object o)
	{
		if(o==null)
			return false;
		else if(!(o instanceof Annonce))
			return false;
		return toString().equals(((Annonce)o).toString());
	}
	public String toString()
	{
		return nom;
	}
}