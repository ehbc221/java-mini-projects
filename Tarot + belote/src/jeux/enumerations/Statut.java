package jeux.enumerations;
import jeux.*;
/**Statut du joueur*/
public enum Statut
{Preneur,Appele,Defenseur;
	private static final char D=Lettre.D;
	private static final char ea=Lettre.ea;
	private static final char f=Lettre.f;
	private static final char e=Lettre.e;
	private static final char n=Lettre.n;
	private static final char s=Lettre.s;
	private static final char u=Lettre.u;
	private static final char r=Lettre.r;
	private static final char A=Lettre.A;
	private static final char p=Lettre.p;
	private static final char l=Lettre.l;
	public String toString()
	{
		if(name().charAt(0)==A)
		{
			return new String(new char[]{A,p,p,e,l,ea});
		}
		if(name().charAt(0)==D)
		{
			return new String(new char[]{D,ea,f,e,n,s,e,u,r});
		}
		return name();
	}
}