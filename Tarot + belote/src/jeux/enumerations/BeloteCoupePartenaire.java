package jeux.enumerations;
import jeux.*;
public enum BeloteCoupePartenaire {
Ni_sur_coupe_ni_sous_coupe_obligatoire,Sur_couper_uniquement,Sous_couper_sur_couper;
	private static final char _=Lettre._;
	private static final char es=Lettre.es;
	private static final char N=Lettre.N;
	private static final char tt=Lettre.tt;
	private static final char e=Lettre.e;
	private static final char t=Lettre.t;
	private static final char s=Lettre.s;
	private static final char c=Lettre.c;
	private static final char o=Lettre.o;
	private static final char u=Lettre.u;
	private static final char p=Lettre.p;
	private static final char r=Lettre.r;
	private static final String ch_v=Lettre.ch_v;
	public String toString()
	{
		String nom=name();
		if(nom.charAt(0)==N)
		{
			return nom.replace(ch_v+_+c+o+u+p+e,ch_v+es+tt+es+c+o+u+p+e).replace(_,es);
		}
		return nom.replace(ch_v+_+c+o+u+p+e+r,ch_v+es+tt+es+c+o+u+p+e+r).replace(ch_v+_+s+u+r,ch_v+es+e+t+es+s+u+r).replace(_,es);
	}
}