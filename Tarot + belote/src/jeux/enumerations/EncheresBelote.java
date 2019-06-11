package jeux.enumerations;
import jeux.*;
public enum EncheresBelote
{Passe,Couleur,Autre_couleur,Sans_atout,Tout_atout;
	private static final char _=Lettre._;
	private static final char es=Lettre.es;
	public String toString()
	{
		return name().replace(_,es);
	}
}