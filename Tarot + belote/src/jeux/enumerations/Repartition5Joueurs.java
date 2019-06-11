package jeux.enumerations;
import jeux.*;
public enum Repartition5Joueurs
{c1_vs_4,c2_vs_3_appel_au_roi,c2_vs_3_appel_a_la_figure;
	private static final String ch_v=Lettre.ch_v;
	private static final char _=Lettre._;
	private static final char ag=Lettre.ag;
	private static final char a=Lettre.a;
	private static final char vi=Lettre.vi;
	private static final char es=Lettre.es;
	public String toString()
	{
		return name().substring(1).replace(ch_v+_+a,ch_v+vi+a).replace(ch_v+_+a+_,ch_v+es+ag+es).replace(_,es);
	}
}