package jeux.enumerations;
import jeux.*;
import jeux.encheres.*;
public enum Boutons
{Continuer_sur_le_jeu_actuel,Rejouer_la_donne,Arreter,Revoir_le_pli_precedent,Pli_suivant,Revenir_au_pli_actuel,Fin_de_partie,Revoir_le_chien,Voir_le_chien,Prendre_les_cartes_du_chien,Valider_le_chien,Passe_au_jeu_de_la_carte,_Chelem;
	private static final char _=Lettre._;
	private static final char A=Lettre.A;
	private static final char r=Lettre.r;
	private static final char ec=Lettre.ec;
	private static final char t=Lettre.t;
	private static final char e=Lettre.e; 
	private static final char p=Lettre.p;
	private static final char ea=Lettre.ea;
	private static final char c=Lettre.c;
	private static final char d=Lettre.d;
	private static final char n=Lettre.n;
	private static final char es=Lettre.es;
	private static final String ch_v=Lettre.ch_v;
	public String toString()
	{
		if(name().charAt(0)==_)
		{
			return Contrat.chelem;
		}
		return name().replace(_,es).replace(Arreter.name(),ch_v+A+r+r+ec+t+e+r).replace(ch_v+p+r+e+c+e+d+e+n+t,ch_v+p+r+ea+c+ea+d+e+n+t);
	}
}