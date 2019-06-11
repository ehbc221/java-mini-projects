package jeux;
import java.io.*;
import java.util.*;
public class Entier implements Serializable{
	private static final long serialVersionUID = 1L;
	private Vector<Long> nombre=new Vector<Long>();
	private static final long base=1000000000L;
	public Entier(){}
	public Entier(long p_nombre)
	{
		long quotient;
		long reste;
		quotient=p_nombre/base;
		if(p_nombre>=0)
		{
			reste=p_nombre-quotient*base;
		}
		else
		{
			reste=quotient*base-p_nombre;
		}
		nombre.add(0,reste);
		for(;quotient>base||quotient<-base;)
		{
			p_nombre=quotient;
			quotient=p_nombre/base;
			if(p_nombre>=0)
			{
				reste=p_nombre-quotient*base;
			}
			else
			{
				reste=quotient*base-p_nombre;
			}
			nombre.add(0,reste);
		}
		if(quotient!=0)
		{
			nombre.add(0,quotient);
		}
	}
	public Entier fois(double db)
	{
		Entier resultat=new Entier();
		double tmp;
		Vector<Long> resultat_nombre=resultat.nombre;
		for(int indice=0;indice<nombre.size();indice++)
		{
			resultat_nombre.addElement(0L);
		}
		for(int indice=resultat_nombre.size()-1;indice>-1;indice--)
		{
			resultat_nombre.setElementAt((long)Math.floor(nombre.get(indice)*db),indice);
			tmp=(nombre.get(indice)*db-resultat_nombre.get(indice))*base;
			for(int indice_2=indice+1;indice_2<resultat_nombre.size();indice_2++)
			{
				resultat_nombre.setElementAt(resultat_nombre.get(indice_2)+(long)Math.floor(tmp),indice_2);
				tmp=(tmp-(long)Math.floor(tmp))*base;
			}
		}//123000000000000000000
		long retenue=0;
		long quotient;
		long reste;
		for(int indice=resultat_nombre.size()-1;indice>-1;indice--)
		{
			resultat_nombre.setElementAt(resultat_nombre.get(indice)+retenue,indice);
			quotient=resultat_nombre.get(indice)/base;
			reste=resultat_nombre.get(indice)-base*quotient;
			retenue=quotient;
			resultat_nombre.setElementAt(reste,indice);
		}
		if(retenue>0)
		{
			resultat_nombre.add(0,retenue);
		}
		int taille=resultat_nombre.size();
		for(int i=0;i<taille-1&&resultat_nombre.get(0)==0;i++)
		{
			resultat_nombre.removeElementAt(0);
		}
		return resultat;
	}
	public Entier multiplierPositif(Entier e)
	{
		Entier resultat=new Entier(0L);
		Entier tmp;
		int longueur1=nombre.size();
		int longueur2=e.nombre.size();
		int puissance;
		if(longueur1<longueur2)
		{
			for(int indice_nombre=longueur1-1;indice_nombre>-1;indice_nombre--)
			{
				tmp=e.multiplier(nombre.get(indice_nombre));
				puissance=longueur1-indice_nombre-1;
				for(int facteur=0;facteur<puissance;facteur++)
				{
					tmp=tmp.multiplierParBase();
				}
				resultat=resultat.ajouterPositif(tmp);
			}
		}
		else
		{
			for(int indice_nombre=longueur2-1;indice_nombre>-1;indice_nombre--)
			{
				tmp=multiplier(e.nombre.get(indice_nombre));
				puissance=longueur2-indice_nombre-1;
				for(int facteur=0;facteur<puissance;facteur++)
				{
					tmp=tmp.multiplierParBase();
				}
				resultat=resultat.ajouterPositif(tmp);
			}
		}
		return resultat;
	}
	private Entier multiplierParBase()
	{
		Entier resultat=new Entier();
		resultat.nombre=nombre;
		resultat.nombre.addElement(base);
		return resultat;
	}
	private Entier multiplier(long l)
	{
		Entier resultat=new Entier();
		if(l==0L)
		{
			resultat.nombre.addElement(0L);
			return resultat;
		}
		long quotient;
		long retenue=0;
		long reste;
		for(int indice_nombre=nombre.size()-1;indice_nombre>-1;indice_nombre--)
		{
			quotient=nombre.get(indice_nombre)*l+retenue;
			retenue=quotient/base;
			reste=quotient-base*retenue;
			resultat.nombre.add(0,reste);
		}
		if(retenue>0)
		{
			resultat.nombre.add(0,retenue);
		}
		return resultat;
	}
	private static long pgcd(long a,long b)
	{
		long reste;
		if(b==0)
		{
			return a;
		}
		reste=a%b;
		for(;reste!=0;)
		{
			a=b;
			b=reste;
			reste=a%b;
		}
		return b;
	}
	public static Entier combinaison(int elements,int total_elements)
	{
		Entier cardinal=new Entier(1L);
		int nombre;
		long pgcd;
		long[] numerateur;
		long[] denominateur;
		if(elements>total_elements)
		{
			return new Entier(0L);
		}
		nombre=Math.min(elements,total_elements-elements);
		numerateur=new long[nombre];
		denominateur=new long[nombre];
		for(int indice_facteur=0;indice_facteur<nombre;indice_facteur++)
		{
			numerateur[indice_facteur]=total_elements-indice_facteur;
			denominateur[indice_facteur]=nombre-indice_facteur;
		}
		for(int indice_facteur=0;indice_facteur<nombre-1;indice_facteur++)
		{
			for(int indice_facteur2=0;denominateur[indice_facteur]!=1;indice_facteur2++)
			{
				pgcd=pgcd(numerateur[indice_facteur2],denominateur[indice_facteur]);
				numerateur[indice_facteur2]/=pgcd;
				denominateur[indice_facteur]/=pgcd;
			}
		}
		for(int indice_facteur=0;indice_facteur<nombre;indice_facteur++)
		{
			cardinal=cardinal.multiplier(numerateur[indice_facteur]);
		}
		return cardinal;
	}
	public Entier ajouterPositif(Entier e)
	{
		byte retenue=0;
		long somme;
		Entier resultat=new Entier();
		long reste;
		int longueur1=nombre.size();
		int longueur2=e.nombre.size();
		for(int indice_nombre=longueur1-1,indice_nombre2=longueur2-1;indice_nombre>-1&&indice_nombre2>-1;)
		{
			somme=nombre.get(indice_nombre)+e.nombre.get(indice_nombre2)+retenue;
			if(somme<base)
			{
				resultat.nombre.add(0,somme);
				retenue=0;
			}
			else
			{
				reste=somme-base;
				resultat.nombre.add(0,reste);
				retenue=1;
			}
			indice_nombre--;
			indice_nombre2--;
		}
		if(longueur1>longueur2)
		{
			for(int indice_nombre=longueur1-longueur2-1;indice_nombre>-1;indice_nombre--)
			{
				somme=nombre.get(indice_nombre)+retenue;
				if(somme<base)
				{
					resultat.nombre.add(0,somme);
					retenue=0;
				}
				else
				{
					reste=somme-base;
					resultat.nombre.add(0,reste);
					retenue=1;
				}
			}
		}
		else if(longueur1<longueur2)
		{
			for(int indice_nombre=longueur2-longueur1-1;indice_nombre>-1;indice_nombre--)
			{
				somme=e.nombre.get(indice_nombre)+retenue;
				if(somme<base)
				{
					resultat.nombre.add(0,somme);
					retenue=0;
				}
				else
				{
					reste=somme-base;
					resultat.nombre.add(0,reste);
					retenue=1;
				}
			}
		}
		if(retenue==1)
		{
			resultat.nombre.add(0,(long)retenue);
		}
		return resultat;
	}
	/**Au sens strict du terme*/
	public boolean plusPetitQue(Entier e)
	{
		if(nombre.get(0)>=0&&e.nombre.get(0)<=0)
		{
			return false;
		}
		if(nombre.get(0)<=0&&e.nombre.get(0)>=0)
		{
			return true;
		}
		if(positif())
		{
			if(nombre.size()>e.nombre.size())
			{
				return false;
			}
			if(nombre.size()<e.nombre.size())
			{
				return true;
			}
			for(int indice_nombre=0;indice_nombre<nombre.size();indice_nombre++)
			{
				if(nombre.get(indice_nombre)<e.nombre.get(indice_nombre))
				{
					return true;
				}
				else if(nombre.get(indice_nombre)>e.nombre.get(indice_nombre))
				{
					return false;
				}
			}
			return false;
		}
		if(nombre.size()<e.nombre.size())
		{
			return false;
		}
		if(nombre.size()>e.nombre.size())
		{
			return true;
		}
		if(nombre.get(0)<e.nombre.get(0))
		{
			return true;
		}
		else if(nombre.get(0)>e.nombre.get(0))
		{
			return false;
		}
		for(int indice_nombre=0;indice_nombre<nombre.size();indice_nombre++)
		{
			if(nombre.get(indice_nombre)>e.nombre.get(indice_nombre))
			{
				return true;
			}
			else if(nombre.get(indice_nombre)<e.nombre.get(indice_nombre))
			{
				return false;
			}
		}
		return false;
	}
	private boolean positif()
	{
		return nombre.get(0)>=0;
	}
}