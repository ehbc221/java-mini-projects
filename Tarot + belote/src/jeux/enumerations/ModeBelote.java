package jeux.enumerations;
import jeux.*;
public enum ModeBelote
{Avec_surcontrat,Sans_surcontrat;
	private static final char _=Lettre._;
	private static final char es=Lettre.es;
	public String toString()
	{
		return name().replace(_,es);
	}
}