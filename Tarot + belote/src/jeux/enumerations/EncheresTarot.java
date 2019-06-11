package jeux.enumerations;
import jeux.*;
public enum EncheresTarot
{Passe,Petite,Garde,Garde_sans,Garde_contre;
	private static final char _=Lettre._;
	private static final char es=Lettre.es;
	public String toString()
	{
		return name().replace(_,es);
	}
}