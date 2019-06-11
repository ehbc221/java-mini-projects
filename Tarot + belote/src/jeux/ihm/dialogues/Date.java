package jeux.ihm.dialogues;
import jeux.*;
class Date {
	private static final char m=Lettre.m;
	private static final char h=Lettre.h;
	private static final char es=Lettre.es;
	private static final char quote=Lettre.quote;
	private static final String ch_v=Lettre.ch_v;
	private final static char ea=Lettre.ea;
	private int an;
	private int mois;
	private int jour;
	private int heures;
	private int minutes;
	private int secondes;
	private int millisecondes;
	private Jour jour_semaine;
	private enum Jour{Lundi,Mardi,Mercredi,Jeudi,Vendredi,Samedi,Dimanche}
	private enum Mois
	{Janvier,Fevrier,Mars,Avril,Mai,Juin,Juillet,Aout,Septembre,Octobre,Novembre,Decembre;
		private static final char F=Lettre.F;
		private static final char v=Lettre.v;
		private static final char r=Lettre.r;
		private static final char i=Lettre.i;
		private static final char e=Lettre.e;
		private static final char A=Lettre.A;
		private static final char o=Lettre.o;
		private static final char uc=Lettre.uc;
		private static final char t=Lettre.t;
		private static final char D=Lettre.D;
		private static final char c=Lettre.c;
		private static final char b=Lettre.b;
		public String toString()
		{
			String nom=name();
			if(nom.charAt(0)==F)
			{
				return new String(new char[]{F,ea,v,r,i,e,r});
			}
			if(nom.charAt(0)==A)
			{
				return new String(new char[]{A,o,uc,t});
			}
			if(nom.charAt(0)==D)
			{
				return new String(new char[]{D,ea,c,e,m,b,r,e});
			}
			return nom;
		}
	}
	public Date(int pan,int pmois,int pjour,int pheures,int pminutes,int psecondes,int pmillisecondes,int nombre_jours_mois,int position_jour)
	{
		an=pan;
		mois=pmois+1;
		jour=pjour;
		heures=pheures;
		minutes=pminutes;
		secondes=psecondes;
		millisecondes=pmillisecondes;
		Jour[] jours=Jour.values();
		jour_semaine=jours[position_jour];
		heures+=2;
		if(heures>23)
		{
			jour++;
			jour_semaine=jours[(position_jour+1)%7];
			heures=heures-24;
			if(jour>nombre_jours_mois)
			{
				jour=1;
				mois++;
				if(mois>12)
				{
					an++;
					mois=1;
				}
			}
		}
	}
	public boolean plusVieuxQue(Date d)
	{
		if(an<d.an)
			return true;
		if(an>d.an)
			return false;
		if(mois<d.mois)
			return true;
		if(mois>d.mois)
			return false;
		if(jour<d.jour)
			return true;
		if(jour>d.jour)
			return false;
		if(heures<d.heures)
			return true;
		if(heures>d.heures)
			return false;
		if(minutes<d.minutes)
			return true;
		if(minutes>d.minutes)
			return false;
		if(secondes<d.secondes)
			return true;
		if(secondes>d.secondes)
			return false;
		if(millisecondes<d.millisecondes)
			return true;
		if(millisecondes>d.millisecondes)
			return false;
		return true;
	}
	public String toString() {
		return ch_v+jour_semaine+es+jour+es+Mois.values()[mois-1]+es+an+es+heures+h+minutes+m+secondes+quote+millisecondes;
	}
}
