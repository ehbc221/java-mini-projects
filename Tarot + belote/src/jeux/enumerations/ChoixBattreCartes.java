package jeux.enumerations;
import jeux.*;
public enum ChoixBattreCartes
{a_chaque_partie,a_chaque_lancement,une_seule_fois,jamais;
	private final static char a=Lettre.a;
	private final static char ag=Lettre.ag;
	private final static char es=Lettre.es;
	private final static char _=Lettre._;
	private final static String ch_v=Lettre.ch_v;
	public String toString()
	{
		return name().replace(ch_v+a+_, ch_v+ag+es).replace(_,es);
	}
}