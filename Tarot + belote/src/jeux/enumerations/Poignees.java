package jeux.enumerations;
import jeux.*;
/**Poignees utilisees au tarot*/
public enum Poignees
{Poignee,Double_Poignee,Triple_Poignee;
	private static final char _=Lettre._;
	private static final char es=Lettre.es;
	public String toString()
	{
		return name().replace(_,es);
	}
}