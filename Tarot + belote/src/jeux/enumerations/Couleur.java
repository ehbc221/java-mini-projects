package jeux.enumerations;
import jeux.*;
/**Couleurs existantes dans les jeux de cartes (Atout n'existe que pour le Tarot)*/
public enum Couleur
{Atout,Coeur,Pique,Carreau,Trefle;
	public int position()
	{
		return ordinal()+1;
	}
	public String toString()
	{
		if(name().startsWith("T"))
		{
			return "Tr"+Lettre.eg+"fle";
		}
		return name();
	}
}