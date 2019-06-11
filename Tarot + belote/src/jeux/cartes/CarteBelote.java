package jeux.cartes;
import jeux.encheres.*;
import jeux.enumerations.*;
/**
 *
 */
public class CarteBelote extends Carte implements Triable{
	private static final long serialVersionUID = 1L;
	/**Ex: 1e carte (indice 0): le valet (valeur 11)*/
	public final static byte[] cartesAtout=new byte[8];
	/**Ex: 1e carte (indice 0): l'as (valeur 1)*/
	public final static byte[] cartesCouleur=new byte[8];
	static{
		cartesAtout[0]=11;
		cartesAtout[1]=9;
		cartesAtout[2]=1;
		cartesAtout[3]=10;
		cartesAtout[4]=14;
		cartesAtout[5]=13;
		cartesAtout[6]=8;
		cartesAtout[7]=7;
		cartesCouleur[0]=1;
		cartesCouleur[1]=10;
		cartesCouleur[2]=14;
		cartesCouleur[3]=13;
		cartesCouleur[4]=11;
		cartesCouleur[5]=9;
		cartesCouleur[6]=8;
		cartesCouleur[7]=7;
	}
	public CarteBelote(byte valeur, byte couleur) {
		super(valeur, couleur);
	}
	public CarteBelote(byte valeur)
	{
		this(cartesAtout[valeur%8],(byte)(valeur/8+2));
	}
	public byte force(byte atout,byte dem,Contrat contrat)
	{
		if(contrat.equals(new Contrat(EncheresBelote.Couleur))||contrat.equals(new Contrat(EncheresBelote.Autre_couleur)))
			return force(atout, dem);
		if(contrat.equals(new Contrat(EncheresBelote.Sans_atout)))
			if(couleur()==dem)
			{
				switch (valeur()) {
				case 1: return 8;
				case 7: return 1;
				case 8: return 2;
				case 9: return 3;
				case 10: return 7;
				case 11: return 4;
				case 13: return 5;
				default: return 6;
				}
			}
		if(contrat.equals(new Contrat(EncheresBelote.Tout_atout)))
			if(couleur()==dem)
			{
				switch (valeur()) {
				case 1: return 6;
				case 7: return 1;
				case 8: return 2;
				case 9: return 7;
				case 10: return 5;
				case 11: return 8;
				case 13: return 3;
				default: return 4;
				}
			}
		return 0;
	}
	public byte force(byte atout,byte dem)
	{
		if(dem==atout)
		{
			if(couleur()==atout)
			{
				switch (valeur()) {
				case 1: return 6;
				case 7: return 1;
				case 8: return 2;
				case 9: return 7;
				case 10: return 5;
				case 11: return 8;
				case 13: return 3;
				default: return 4;
				}
			}
		}
		else
		{
			if(couleur()==atout)
			{
				switch (valeur()) {
				case 1: return 14;
				case 7: return 9;
				case 8: return 10;
				case 9: return 15;
				case 10: return 13;
				case 11: return 16;
				case 13: return 11;
				default: return 12;
				}
			}
			if(couleur()==dem)
			{
				switch (valeur()) {
				case 1: return 8;
				case 7: return 1;
				case 8: return 2;
				case 9: return 3;
				case 10: return 7;
				case 11: return 4;
				case 13: return 5;
				default: return 6;
				}
			}
		}
		//Si on n'a ni de l'atout ni la couleur demandee
		return (byte)0;
	}
	public byte forceAnnonce()
	{
		return forceAnnonce(valeur());
	}
	public static byte forceAtout(byte valeur)
	{
		switch (valeur) {
		case 1: return 6;
		case 7: return 1;
		case 8: return 2;
		case 9: return 7;
		case 10: return 5;
		case 11: return 8;
		case 13: return 3;
		default: return 4;
		}
	}
	public static byte forceAnnonce(byte valeur)
	{
		switch (valeur) {
		case 1: return 8;
		case 7: return 1;
		case 8: return 2;
		case 9: return 3;
		case 10: return 4;
		case 11: return 5;
		case 13: return 6;
		default: return 7;
		}
	}
	private byte points(byte atout)
	{
		if(atout==couleur())
			switch (valeur()) {
				case 1: return 11;
				case 7:
				case 8: return 0;
				case 9: return 14;
				case 10: return 10;
				case 11: return 20;
				case 13: return 3;
				default: return 4;
			}
		switch (valeur()) {
			case 1: return 11;
			case 7:
			case 8:
			case 9: return 0;
			case 10: return 10;
			case 11: return 2;
			case 13: return 3;
			default: return 4;
		}
	}
	public byte points(Contrat contrat,Carte carteAppelee)
	{
		if(contrat.equals(new Contrat(EncheresBelote.Tout_atout)))
		{
			switch (valeur()) {
				case 1: return 6;
				case 7:
				case 8: return 0;
				case 9: return 9;
				case 10: return 4;
				case 11: return 14;
				case 13: return 2;
				default: return 3;
			}
		}
		else if(contrat.equals(new Contrat(EncheresBelote.Sans_atout)))
		{
			switch (valeur()) {
				case 1: return 19;
				case 7:
				case 8:
				case 9: return 0;
				case 10: return 10;
				case 11: return 2;
				case 13: return 3;
				default: return 4;
			}
		}
		return points(carteAppelee.couleur());
	}
	public byte forceValeurDansUnTri(String sens,String ordre)
	{
		if(sens.contains(Monotonie.Decroissant.toString()))
		{
			if(ordre.contains(Ordre.Atout.toString()))
				return (byte)(9-force(couleur(),couleur()));
			return (byte)(9-force((byte)0,couleur()));
		}
		if(ordre.contains(Ordre.Atout.toString()))
			return force(couleur(),couleur());
		return force((byte)0,couleur());
	}
	/**Par defaut sens vaut Decroissant*/
	public byte forceValeurDansUnTri(String ordre)
	{
		if(ordre.equals(Ordre.Atout.toString()))
			return (byte)(9-force(couleur(),couleur()));
		return (byte)(9-force((byte)0,couleur()));
	}
	/**Par defaut ordre vaut Atout*/
	public byte forceValeurDansUnTri()
	{
		return (byte)(9-force(couleur(),couleur()));
	}
	public boolean vientAvant(Triable c,String sens,String ordre,String couleurs)
	{
		byte forceCouleur=forceCouleurDansUnTri(couleurs);
		byte forceCouleur2=((Carte)c).forceCouleurDansUnTri(couleurs);
		byte forceValeur=forceValeurDansUnTri(sens,ordre);
		byte forceValeur2=c.forceValeurDansUnTri(sens,ordre);
		if(forceCouleur<forceCouleur2)
			return true;
		if(forceCouleur==forceCouleur2)
			return forceValeur<forceValeur2;
		return false;
	}
}